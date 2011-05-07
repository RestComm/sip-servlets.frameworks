package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Cancel;


public class CancelLiteral extends AnnotationLiteral<Cancel> implements Cancel {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Cancel INSTANCE = new CancelLiteral();
}