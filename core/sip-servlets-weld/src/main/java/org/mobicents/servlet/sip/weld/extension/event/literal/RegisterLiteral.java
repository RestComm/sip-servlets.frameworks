package org.mobicents.servlet.sip.weld.extension.event.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Register;

public class RegisterLiteral extends AnnotationLiteral<Register> implements Register {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Register INSTANCE = new RegisterLiteral();
}
