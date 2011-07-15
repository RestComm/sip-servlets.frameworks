package org.mobicents.servlet.sip.ctf.core.extension.event.error.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.error.NoAckReceived;

public class NoAckReceivedLiteral extends AnnotationLiteral<NoAckReceived> implements NoAckReceived {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final NoAckReceived INSTANCE = new NoAckReceivedLiteral();
}