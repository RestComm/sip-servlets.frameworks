package org.mobicents.servlet.sip.weld.extension.event.session.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.session.SipApplicationSessionEv;

public class SipApplicationSessionLiteral extends AnnotationLiteral<SipApplicationSessionEv> implements SipApplicationSessionEv {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final SipApplicationSessionEv INSTANCE = new SipApplicationSessionLiteral();
}