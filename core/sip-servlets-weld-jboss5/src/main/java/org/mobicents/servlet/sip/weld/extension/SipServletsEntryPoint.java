package org.mobicents.servlet.sip.weld.extension;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipErrorListener;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.mobicents.servlet.sip.weld.extension.SipSessionsHolder.InternalSipSessionEvent;
import org.mobicents.servlet.sip.weld.extension.event.error.literal.NoAckReceivedLiteral;
import org.mobicents.servlet.sip.weld.extension.event.error.literal.NoPrackReceivedLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.AckLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.ByeLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.CancelLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.InfoLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.InviteLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.MessageLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.NotifyLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.OptionsLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.PrackLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.PublishLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.ReferLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.RegisterLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.SubscribeLiteral;
import org.mobicents.servlet.sip.weld.extension.event.request.literal.UpdateLiteral;
import org.mobicents.servlet.sip.weld.extension.event.response.literal.BranchResponseLiteral;
import org.mobicents.servlet.sip.weld.extension.event.response.literal.ErrorResponseLiteral;
import org.mobicents.servlet.sip.weld.extension.event.response.literal.ProvisionalResponseLiteral;
import org.mobicents.servlet.sip.weld.extension.event.response.literal.RedirectResponseLiteral;
import org.mobicents.servlet.sip.weld.extension.event.response.literal.SuccessResponseLiteral;
import org.mobicents.servlet.sip.weld.extension.event.session.literal.SipApplicationSessionLiteral;
import org.mobicents.servlet.sip.weld.extension.event.session.literal.SipSessionLiteral;

@ApplicationScoped
@javax.servlet.sip.annotation.SipServlet(loadOnStartup=1)
public class SipServletsEntryPoint extends SipServlet implements SipErrorListener{

	private static final long serialVersionUID = 1L;

	@Inject
	BeanManager beanManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	//	private SipSessionContext sessionContext()
	//	{
	//		if (sipSessionContext == null)
	//		{
	//			this.sipSessionContext = (SipSessionContext) beanManager.getBeans(SipSessionContextImpl.class, AnyLiteral.INSTANCE);
	//		}
	//		return sipSessionContext;
	//	}

	@Override
	protected void doRequest(SipServletRequest req) throws ServletException,
	IOException {

		propagateSessions(req);
		
//		InternalSipSessionEvent event = new InternalSipSessionEvent(req);
//		beanManager.fireEvent(event, SipSessionLiteral.INSTANCE);
//		beanManager.fireEvent(event, SipApplicationSessionLiteral.INSTANCE);

		//Associate the SipServletRequest with the SipSessionContext

		//		sipSessionContext = (SipSessionContext) beanManager.getContext(SessionScoped.class);
		//		sipSessionContext = sessionContext();
		//		sipApplicationSessionContext = (SipApplicationSessionContext) beanManager.getContext(SipApplicationSessionScoped.class);

//		sipSessionContext = SipSessionContextInitialization.getSipSessionContext();
//		sipApplicationSessionContext = SipApplicationSessionContextInitialization.getSipApplicationSessionContext();

//		if (Container.available())
//		{
//			if (req instanceof SipServletRequest)
//			{
//				sipSessionContext.associate(req);
//				sipApplicationSessionContext.associate(req);
//
//				sipSessionContext.activate();
//				sipApplicationSessionContext.activate();
//			}
//			else
//			{
//				throw new IllegalStateException(ONLY_HTTP_SERVLET_LIFECYCLE_DEFINED);
//			}
//		}

		//Depending of the SIP Method fire the appropriate event

		String sipMethod = req.getMethod();

		if (sipMethod.equalsIgnoreCase("REGISTER"))
			beanManager.fireEvent(req, RegisterLiteral.INSTANCE );

		if (sipMethod.equalsIgnoreCase("INVITE"))
			beanManager.fireEvent(req, InviteLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("ACK"))
			beanManager.fireEvent(req, AckLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("BYE"))
			beanManager.fireEvent(req, ByeLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("CANCEL"))
			beanManager.fireEvent(req, CancelLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("OPTIONS"))
			beanManager.fireEvent(req, OptionsLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("PRACK"))
			beanManager.fireEvent(req, PrackLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("SUBSCRIBE"))
			beanManager.fireEvent(req, SubscribeLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("NOTIFY"))
			beanManager.fireEvent(req, NotifyLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("PUBLISH"))
			beanManager.fireEvent(req, PublishLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("INFO"))
			beanManager.fireEvent(req, InfoLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("REFER"))
			beanManager.fireEvent(req, ReferLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("MESSAGE"))
			beanManager.fireEvent(req, MessageLiteral.INSTANCE);

		if (sipMethod.equalsIgnoreCase("UPDATE"))
			beanManager.fireEvent(req, UpdateLiteral.INSTANCE);
	}

//	@Override
//	protected void doResponse(SipServletResponse resp) throws ServletException,
//	IOException {
//		propagateSessions(resp);
//		int respStatus = resp.getStatus();
//		
//		beanManager.fireEvent(resp, ResponseLiteral.INSTANCE);
//	}

	@Override
	protected void doErrorResponse(SipServletResponse resp)
	throws ServletException, IOException {
		propagateSessions(resp);
		beanManager.fireEvent(resp, ErrorResponseLiteral.INSTANCE);
	}

	@Override
	protected void doSuccessResponse(SipServletResponse resp)
	throws ServletException, IOException {
		propagateSessions(resp);
		beanManager.fireEvent(resp, SuccessResponseLiteral.INSTANCE);
	}

	@Override
	protected void doProvisionalResponse(SipServletResponse resp)
	throws ServletException, IOException {
		propagateSessions(resp);
		beanManager.fireEvent(resp, ProvisionalResponseLiteral.INSTANCE);
	}

	@Override
	protected void doBranchResponse(SipServletResponse resp)
	throws ServletException, IOException {
		propagateSessions(resp);
		beanManager.fireEvent(resp, BranchResponseLiteral.INSTANCE);
	}

	@Override
	protected void doRedirectResponse(SipServletResponse resp)
	throws ServletException, IOException {
		propagateSessions(resp);
		beanManager.fireEvent(resp, RedirectResponseLiteral.INSTANCE);
	}

	@Override
	public void noAckReceived(SipErrorEvent noAckEvent) {
		propagateSessions(noAckEvent.getRequest());
		beanManager.fireEvent(noAckEvent, NoAckReceivedLiteral.INSTANCE);
	}

	@Override
	public void noPrackReceived(SipErrorEvent noPrackEvent) {
		propagateSessions(noPrackEvent.getRequest());
		beanManager.fireEvent(noPrackEvent, NoPrackReceivedLiteral.INSTANCE);
	}
	
	protected void propagateSessions(SipServletMessage msg){
		InternalSipSessionEvent event = new InternalSipSessionEvent(msg);
		beanManager.fireEvent(event, SipSessionLiteral.INSTANCE);
		beanManager.fireEvent(event, SipApplicationSessionLiteral.INSTANCE);
	}
}
