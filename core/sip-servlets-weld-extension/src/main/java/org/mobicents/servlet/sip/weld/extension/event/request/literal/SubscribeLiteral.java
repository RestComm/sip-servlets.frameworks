package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Subscribe;

public class SubscribeLiteral extends AnnotationLiteral<Subscribe> implements Subscribe {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Subscribe INSTANCE = new SubscribeLiteral();
}
