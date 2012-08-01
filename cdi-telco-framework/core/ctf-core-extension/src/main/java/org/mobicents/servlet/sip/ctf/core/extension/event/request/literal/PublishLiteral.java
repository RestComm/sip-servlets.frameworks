package org.mobicents.servlet.sip.ctf.core.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.request.Publish;

public class PublishLiteral extends AnnotationLiteral<Publish> implements Publish {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Publish INSTANCE = new PublishLiteral();
}
