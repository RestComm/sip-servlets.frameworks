package org.mobicents.servlet.sip.ctf.core.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.request.Ack;


public class AckLiteral extends AnnotationLiteral<Ack> implements Ack {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Ack INSTANCE = new AckLiteral();
}
