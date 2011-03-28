package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipSession;

import org.mobicents.servlet.sip.weld.extension.event.session.SipApplicationSessionEv;
import org.mobicents.servlet.sip.weld.extension.event.session.SipSessionEv;

@ApplicationScoped
public class SipSessionsHolder {

	private SipSession sipSession;
	private SipApplicationSession sipApplicationSession;
	
	private SipServletMessage msg;
	
	public void setSipSession(@Observes @SipSessionEv final InternalSipSessionEvent e){
		msg = e.getSipServletMessage();
		sipSession = msg.getSession();
	}
	
	public SipSession getSipSession(){
		return sipSession;
	}
	
	public void setSipApplicationSession(@Observes @SipApplicationSessionEv final InternalSipSessionEvent e){
		msg = e.getSipServletMessage();
		sipApplicationSession = msg.getApplicationSession();
	}
	
	public SipApplicationSession getSipApplicationSession(){
		return sipApplicationSession;
	}
	
	
    public static class InternalSipSessionEvent {
        private SipServletMessage msg;

        public InternalSipSessionEvent(SipServletMessage msg) {
            this.msg = msg;
        }

        public SipServletMessage getSipServletMessage() {
            return msg;
        }
    }
}
