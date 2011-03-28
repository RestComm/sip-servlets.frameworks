package org.mobicents.servlet.sip.weld.extension.event.error.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.error.NoPrackReceived;

public class NoPrackReceivedLiteral extends AnnotationLiteral<NoPrackReceived> implements NoPrackReceived {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final NoPrackReceived INSTANCE = new NoPrackReceivedLiteral();
}