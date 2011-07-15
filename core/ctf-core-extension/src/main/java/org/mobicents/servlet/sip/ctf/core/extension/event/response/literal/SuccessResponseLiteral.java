package org.mobicents.servlet.sip.ctf.core.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.response.SuccessResponse;

public class SuccessResponseLiteral extends AnnotationLiteral<SuccessResponse> implements SuccessResponse {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final SuccessResponse INSTANCE = new SuccessResponseLiteral();
}
