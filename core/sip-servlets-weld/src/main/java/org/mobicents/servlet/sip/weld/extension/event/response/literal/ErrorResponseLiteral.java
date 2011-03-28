package org.mobicents.servlet.sip.weld.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.response.ErrorResponse;

public class ErrorResponseLiteral extends AnnotationLiteral<ErrorResponse> implements ErrorResponse {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final ErrorResponse INSTANCE = new ErrorResponseLiteral();
}
