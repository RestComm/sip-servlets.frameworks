package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Refer;

public class ReferLiteral extends AnnotationLiteral<Refer> implements Refer {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Refer INSTANCE = new ReferLiteral();
}
