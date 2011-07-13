package org.mobicents.framework.servlet.sip.ctf.examples;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.URI;

import org.mobicents.framework.servlet.sip.components.CallComponent;
import org.mobicents.framework.servlet.sip.services.beans.CommunicationBean;
import org.mobicents.framework.servlet.sip.services.beans.RegistrationBean;
import org.mobicents.servlet.sip.weld.extension.event.request.Bye;
import org.mobicents.servlet.sip.weld.extension.event.request.Options;
import org.mobicents.servlet.sip.weld.extension.event.request.Register;
import org.mobicents.servlet.sip.weld.extension.event.response.SuccessResponse;
import org.mobicents.servlet.sip.weld.extension.mtf.SipEndpointContainer;

@Named
@ApplicationScoped
public class SipController implements Serializable {

	@Inject 
	CommunicationBean communicationService;
	@Inject
	SipEndpointContainer sipEndpointContainer;
	@Inject 
	RegistrationBean registar;
	@Inject
	SipFactory sipFactory;

	//	SipSession firstSipSession;
	//	SipSession secondSipSession;

	private int users;
	private ArrayList<String> sipEndpoints;
	
	private CallComponent call;

	private String fromURI;
	private String toURI;

	public void handleRegistration(@Observes @Register SipServletRequest req){
		try {
			registar.doRegister(req);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getUsers() {
		this.users = sipEndpointContainer.getUsers().size();
		return users;
	}

	public void setUsers(int users){
		this.users = users;
	}

//	@PostConstruct
	public ArrayList<String> getSipEndpoints() {
		Set<URI> uris = sipEndpointContainer.getUsers().keySet();
		ArrayList<String> result = new ArrayList<String>();

		for (Iterator iter = uris.iterator(); iter.hasNext();) {
			URI uri = (URI) iter.next();
			result.add(uri.toString());
		}
		return result;
	}

	public String getFromURI() {
		return fromURI;
	}

	public void setFromURI(String fromURI) {
		this.fromURI = fromURI;
	}

	public String getToURI() {
		return toURI;
	}

	public void setToURI(String toURI) {
		this.toURI = toURI;
	}

	public void call(){
		try {
			call = communicationService.call(fromURI, toURI);
		} catch (ServletParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//The functionality provided here should be part of the MTF-JSR289 CallFlowBean
	protected void doSuccessResponse(@Observes @SuccessResponse SipServletResponse resp)
	throws ServletException, IOException {
		SipSession session = resp.getSession();

		if (resp.getStatus() == SipServletResponse.SC_OK) {

			Boolean inviteSent = (Boolean) session.getAttribute("InviteSent");
			if (inviteSent != null && inviteSent.booleanValue()) {
				return;
			}

			Address secondPartyAddress = (Address) resp.getSession().getAttribute("SecondPartyAddress");

			if (secondPartyAddress != null) {

				SipServletRequest invite = sipFactory.createRequest(resp
						.getApplicationSession(), "INVITE", session
						.getRemoteParty(), secondPartyAddress);

				String contentType = resp.getContentType();
				if (contentType.trim().equals("application/sdp")) {
					invite.setContent(resp.getContent(), "application/sdp");
				}

				session.setAttribute("LinkedSession", invite.getSession());
				invite.getSession().setAttribute("LinkedSession", session);

				SipServletRequest ack = resp.createAck();
				invite.getSession().setAttribute("FirstPartyAck", ack);
				invite.getSession().setAttribute("FirstPartyContent", resp.getContent());

				//				Call call = (Call) session.getAttribute("call");
				//
				//				// The call links the two sessions, add the new session to the call
				//				call.addSession(invite.getSession());
				//				invite.getSession().setAttribute("call", call);

				invite.send();

				session.setAttribute("InviteSent", Boolean.TRUE);
			} else {
				String cSeqValue = resp.getHeader("CSeq");
				if(cSeqValue.indexOf("INVITE") != -1) {				

					SipServletRequest secondPartyAck = resp.createAck();
					SipServletRequest firstPartyAck = (SipServletRequest) resp
					.getSession().getAttribute("FirstPartyAck");

					//			if (resp.getContentType() != null && resp.getContentType().equals("application/sdp")) {
					firstPartyAck.setContent(resp.getContent(),
					"application/sdp");
					secondPartyAck.setContent(resp.getSession().getAttribute("FirstPartyContent"),
					"application/sdp");
					//			}

					firstPartyAck.send();
					secondPartyAck.send();
				}
			}
		}
	}


	protected void doOptions(@Observes @Options SipServletRequest req) throws ServletException,
	IOException {
		req.createResponse(SipServletResponse.SC_OK).send();
	}
	
	public void hangup(){
		try {
			communicationService.hangup(call);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void doBye(@Observes @Bye SipServletRequest req) throws IOException{
		SipSession session = req.getSession();
		SipSession linkedSession = (SipSession) session
				.getAttribute("LinkedSession");
		if (linkedSession != null) {
			SipServletRequest bye = linkedSession.createRequest("BYE");
			bye.send();
		}
//		calls.removeCall(request.getFrom().getURI().toString(), request.getTo().getURI().toString());
//		calls.removeCall(request.getTo().getURI().toString(), request.getFrom().getURI().toString());
		SipServletResponse ok = req.createResponse(SipServletResponse.SC_OK);
		ok.send();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromURI == null) ? 0 : fromURI.hashCode());
		result = prime * result
		+ ((sipEndpoints == null) ? 0 : sipEndpoints.hashCode());
		result = prime * result + ((toURI == null) ? 0 : toURI.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SipController other = (SipController) obj;
		if (fromURI == null) {
			if (other.fromURI != null)
				return false;
		} else if (!fromURI.equals(other.fromURI))
			return false;
		if (sipEndpoints == null) {
			if (other.sipEndpoints != null)
				return false;
		} else if (!sipEndpoints.equals(other.sipEndpoints))
			return false;
		if (toURI == null) {
			if (other.toURI != null)
				return false;
		} else if (!toURI.equals(other.toURI))
			return false;
		return true;
	}
}
