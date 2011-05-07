package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Prack;

public class PrackLiteral extends AnnotationLiteral<Prack> implements Prack {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Prack INSTANCE = new PrackLiteral();
}

