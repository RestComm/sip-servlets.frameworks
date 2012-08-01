package org.mobicents.servlet.sip.ctf.examples.modules;

import java.util.concurrent.LinkedBlockingQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class EventsModule {

	//Produces the eventsQueue to be processed by the StatusServlet
	@Produces
	LinkedBlockingQueue<String> eventsQueue = new LinkedBlockingQueue<String>();

	/*
	 * Any SIP Servlets method that wish to trigger an update to the client's page will use this method.
	 * For example doRegister use it to update the page every time a new user registers. 
	 * The method is using CDI's @Observes in order to receive notifications from various parts of the project
	 */
	public void notifyStatus(@Observes String event){

		try {
			eventsQueue.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
