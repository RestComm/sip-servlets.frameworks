package org.mobicents.servlet.sip.ctf.core.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.response.Response;


public class ResponseLiteral extends AnnotationLiteral<Response> implements Response {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Response INSTANCE = new ResponseLiteral();
}
