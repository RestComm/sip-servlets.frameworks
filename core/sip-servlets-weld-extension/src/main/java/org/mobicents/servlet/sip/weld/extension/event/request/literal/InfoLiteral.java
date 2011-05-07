package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Info;

public class InfoLiteral extends AnnotationLiteral<Info> implements Info {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Info INSTANCE = new InfoLiteral();
}
