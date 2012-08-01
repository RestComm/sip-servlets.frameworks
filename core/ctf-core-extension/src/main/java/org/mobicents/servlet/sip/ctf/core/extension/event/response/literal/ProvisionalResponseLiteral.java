package org.mobicents.servlet.sip.ctf.core.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.response.ProvisionalResponse;

public class ProvisionalResponseLiteral extends AnnotationLiteral<ProvisionalResponse> implements ProvisionalResponse {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final ProvisionalResponse INSTANCE = new ProvisionalResponseLiteral();
}
