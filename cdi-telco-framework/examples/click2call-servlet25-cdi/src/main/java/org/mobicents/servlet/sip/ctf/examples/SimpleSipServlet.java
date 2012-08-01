package org.mobicents.servlet.sip.ctf.examples;

import java.io.IOException;
import java.util.HashMap;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;

import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.ctf.core.extension.event.error.NoAckReceived;
import org.mobicents.servlet.sip.ctf.core.extension.event.error.NoPrackReceived;
import org.mobicents.servlet.sip.ctf.core.extension.event.request.Bye;
import org.mobicents.servlet.sip.ctf.core.extension.event.request.Invite;
import org.mobicents.servlet.sip.ctf.core.extension.event.request.Options;
import org.mobicents.servlet.sip.ctf.core.extension.event.request.Register;
import org.mobicents.servlet.sip.ctf.core.extension.event.response.ErrorResponse;
import org.mobicents.servlet.sip.ctf.core.extension.event.response.SuccessResponse;

public class SimpleSipServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SimpleSipServlet.class);
	private static final String CONTACT_HEADER = "Contact";
	
	@Inject
	private SipFactory sipFactory;

	@Inject
	SipSession session;
	
	@Inject
	SipApplicationSession sipAppSession;
	
	@Inject
	ServletContext servletContext;
	
	public SimpleSipServlet() {
	}

	protected void doInvite(@Observes @Invite SipServletRequest req) throws ServletException,
	IOException {
		logger.info("Click2Dial don't handle INVITE. Here's the one we got :  " + req.toString());

	}

	protected void doOptions(@Observes @Options SipServletRequest req) throws ServletException,
	IOException {
		logger.info("Got :  " + req.toString());
		req.createResponse(SipServletResponse.SC_OK).send();
	}

	protected void doSuccessResponse(@Observes @SuccessResponse SipServletResponse resp)
	throws ServletException, IOException {
		logger.info("Got OK");
//		SipSession session = resp.getSession();

		if (resp.getStatus() == SipServletResponse.SC_OK) {

			Boolean inviteSent = (Boolean) session.getAttribute("InviteSent");
			if (inviteSent != null && inviteSent.booleanValue()) {
				return;
			}
			Address secondPartyAddress = (Address) session.getAttribute("SecondPartyAddress");
			if (secondPartyAddress != null) {

				SipServletRequest invite = sipFactory.createRequest(sipAppSession, "INVITE", session
						.getRemoteParty(), secondPartyAddress);

				logger.info("Found second party -- sending INVITE to "
						+ secondPartyAddress);

				String contentType = resp.getContentType();
				if (contentType.trim().equals("application/sdp")) {
					invite.setContent(resp.getContent(), "application/sdp");
				}

				session.setAttribute("LinkedSession", invite.getSession());
				invite.getSession().setAttribute("LinkedSession", session);

				SipServletRequest ack = resp.createAck();
				invite.getSession().setAttribute("FirstPartyAck", ack);
				invite.getSession().setAttribute("FirstPartyContent", resp.getContent());

				Call call = (Call) session.getAttribute("call");

				// The call links the two sessions, add the new session to the call
				call.addSession(invite.getSession());
				invite.getSession().setAttribute("call", call);

				invite.send();

				session.setAttribute("InviteSent", Boolean.TRUE);
			} else {
				String cSeqValue = resp.getHeader("CSeq");
				if(cSeqValue.indexOf("INVITE") != -1) {				
					logger.info("Got OK from second party -- sending ACK");

					SipServletRequest secondPartyAck = resp.createAck();
					SipServletRequest firstPartyAck = (SipServletRequest) session.getAttribute("FirstPartyAck");

					//			if (resp.getContentType() != null && resp.getContentType().equals("application/sdp")) {
					firstPartyAck.setContent(resp.getContent(),
					"application/sdp");
					secondPartyAck.setContent(session.getAttribute("FirstPartyContent"),
					"application/sdp");
					//			}

					firstPartyAck.send();
					secondPartyAck.send();
				}
			}
		}
	}

	protected void doErrorResponse(@Observes @ErrorResponse SipServletResponse resp) throws ServletException,
	IOException {
		// If someone rejects it remove the call from the table
		CallStatusContainer calls = (CallStatusContainer) servletContext.getAttribute("activeCalls");
		calls.removeCall(resp.getFrom().getURI().toString(), resp.getTo().getURI().toString());
		calls.removeCall(resp.getTo().getURI().toString(), resp.getFrom().getURI().toString());

	}

	protected void doBye(@Observes @Bye SipServletRequest request) throws ServletException,
	IOException {
		logger.info("Got bye");
//		SipSession session = request.getSession();
		SipSession linkedSession = (SipSession) session
		.getAttribute("LinkedSession");
		if (linkedSession != null) {
			SipServletRequest bye = linkedSession.createRequest("BYE");
			logger.info("Sending bye to " + linkedSession.getRemoteParty());
			bye.send();
		}
		CallStatusContainer calls = (CallStatusContainer) servletContext.getAttribute("activeCalls");
		calls.removeCall(request.getFrom().getURI().toString(), request.getTo().getURI().toString());
		calls.removeCall(request.getTo().getURI().toString(), request.getFrom().getURI().toString());
		SipServletResponse ok = request
		.createResponse(SipServletResponse.SC_OK);
		ok.send();
	}

	public void noAckReceived(@Observes @NoAckReceived SipErrorEvent ee) {
		logger.info("SimpleProxyServlet: Error: noAckReceived.");
	}

	public void noPrackReceived(@Observes @NoPrackReceived SipErrorEvent ee) {
		logger.info("SimpleProxyServlet: Error: noPrackReceived.");
	}

	protected void doRegister(@Observes @Register SipServletRequest req) throws ServletException, IOException {
		logger.info("Received register request: " + req.getTo());
		int response = SipServletResponse.SC_OK;
		SipServletResponse resp = req.createResponse(response);
		HashMap<String, String> users = (HashMap<String, String>) servletContext.getAttribute("registeredUsersMap");
		if(users == null) users = new HashMap<String, String>();
		servletContext.setAttribute("registeredUsersMap", users);

		Address address = req.getAddressHeader(CONTACT_HEADER);
		String fromURI = req.getFrom().getURI().toString();

		int expires = address.getExpires();
		if(expires < 0) {
			expires = req.getExpires();
		}
		if(expires == 0) {
			users.remove(fromURI);
			logger.info("User " + fromURI + " unregistered");
		} else {
			resp.setAddressHeader(CONTACT_HEADER, address);
			users.put(fromURI, address.getURI().toString());
			logger.info("User " + fromURI + 
					" registered with an Expire time of " + expires);
		}				

		resp.send();
	}

}
