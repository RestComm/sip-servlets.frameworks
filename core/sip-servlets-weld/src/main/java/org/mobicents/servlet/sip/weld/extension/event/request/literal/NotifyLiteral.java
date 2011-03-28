package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Notify;

public class NotifyLiteral extends AnnotationLiteral<Notify> implements Notify {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Notify INSTANCE = new NotifyLiteral();
}

