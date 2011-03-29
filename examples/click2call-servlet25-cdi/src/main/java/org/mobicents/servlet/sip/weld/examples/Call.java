package org.mobicents.servlet.sip.weld.examples;

import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.sip.SipSession;


public class Call
{
	private String from;
	private String to;
	private String status;
	private HashSet<SipSession> sessions = new HashSet<SipSession>();

	public Call(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
	public void addSession(SipSession session) {
		this.sessions.add(session);
	}

	public void end() {
		Iterator it = this.sessions.iterator();
		while(it.hasNext()) {
			SipSession session = (SipSession) it.next();
			try {
				session.createRequest("BYE").send();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Call other = (Call) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
