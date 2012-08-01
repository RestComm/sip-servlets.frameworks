package org.mobicents.servlet.sip.ctf.core.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.request.Update;

public class UpdateLiteral extends AnnotationLiteral<Update> implements Update {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Update INSTANCE = new UpdateLiteral();
}
