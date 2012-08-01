/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.ctf.examples;

import java.io.IOException;

import javax.annotation.Resource;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;

import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.ctf.examples.modules.CallStatusContainer;
import org.mobicents.servlet.sip.ctf.examples.modules.SipRegistarModule;
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
	
	/*
	 * Inject the required beans
	 */
	
	//SipFactory injection
	@Inject
	private SipFactory sipFactory;
	
	//sipRegistar will process the Registration request 
	@Inject
	SipRegistarModule sipRegistar;

	//calls bean will be used to keep track of the active calls
	@Inject
	CallStatusContainer calls;
	
	//event bean will be used to notify StatusServlet for new events such as a new call is placed
	@Inject
	Event<String> event;
	
	public SimpleSipServlet() {
	}
	

	/*
	 * The following methods will handle SIP requests and responses.  Using CTF, in order to get notified for a SIP request or response you
	 * have to register for the appropriate event by using @Observe @SipEvent. The name of the method is insignificant but here we kept the
	 * names of methods to doMethod, similar to SipServlet methods, for the sake of clarity.  
	 */
	
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
		SipSession session = resp.getSession();

		if (resp.getStatus() == SipServletResponse.SC_OK) {

			Boolean inviteSent = (Boolean) session.getAttribute("InviteSent");
			if (inviteSent != null && inviteSent.booleanValue()) {
				return;
			}
			Address secondPartyAddress = (Address) resp.getSession()
					.getAttribute("SecondPartyAddress");
			if (secondPartyAddress != null) {

				SipServletRequest invite = sipFactory.createRequest(resp
						.getApplicationSession(), "INVITE", session
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
				//secondPartyAddress == null means that this OK is from the second party
				//so we should send ACK to both parties
				String cSeqValue = resp.getHeader("CSeq");
				if(cSeqValue.indexOf("INVITE") != -1) {				
					logger.info("Got OK from second party -- sending ACK");
	
					SipServletRequest secondPartyAck = resp.createAck();
					SipServletRequest firstPartyAck = (SipServletRequest) resp
							.getSession().getAttribute("FirstPartyAck");
	
//					if (resp.getContentType() != null && resp.getContentType().equals("application/sdp")) {
						firstPartyAck.setContent(resp.getContent(),
								"application/sdp");
						secondPartyAck.setContent(resp.getSession().getAttribute("FirstPartyContent"),
								"application/sdp");
//					}
	
					firstPartyAck.send();
					secondPartyAck.send();
				}
			}
		}
	}
	
	protected void doErrorResponse(@Observes @ErrorResponse SipServletResponse resp) throws ServletException,
			IOException {
		// If someone rejects it remove the call from the table
		calls.removeCall(resp.getFrom().getURI().toString(), resp.getTo().getURI().toString());
		calls.removeCall(resp.getTo().getURI().toString(), resp.getFrom().getURI().toString());

	}

	protected void doBye(@Observes @Bye SipServletRequest request) throws ServletException,
			IOException {
		logger.info("Got bye");
		SipSession session = request.getSession();
		SipSession linkedSession = (SipSession) session
				.getAttribute("LinkedSession");
		if (linkedSession != null) {
			SipServletRequest bye = linkedSession.createRequest("BYE");
			logger.info("Sending bye to " + linkedSession.getRemoteParty());
			bye.send();
		}
		calls.removeCall(request.getFrom().getURI().toString(), request.getTo().getURI().toString());
		calls.removeCall(request.getTo().getURI().toString(), request.getFrom().getURI().toString());
		SipServletResponse ok = request
				.createResponse(SipServletResponse.SC_OK);
		ok.send();
		event.fire("Received Bye request");
	}
    
	public void noAckReceived(@Observes @NoAckReceived SipErrorEvent ee) {
		logger.info("SimpleProxyServlet: Error: noAckReceived.");
	}
	
	public void noPrackReceived(@Observes @NoPrackReceived SipErrorEvent ee) {
		logger.info("SimpleProxyServlet: Error: noPrackReceived.");
	}
		
	protected void doRegister(@Observes @Register SipServletRequest req) throws ServletException, IOException {
		
		sipRegistar.doRegister(req);
		event.fire("Received Register event");
	}
}