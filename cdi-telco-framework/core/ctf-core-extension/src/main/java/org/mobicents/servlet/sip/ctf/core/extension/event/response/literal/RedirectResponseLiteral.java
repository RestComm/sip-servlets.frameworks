package org.mobicents.servlet.sip.ctf.core.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.response.RedirectResponse;

public class RedirectResponseLiteral extends AnnotationLiteral<RedirectResponse> implements RedirectResponse {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final RedirectResponse INSTANCE = new RedirectResponseLiteral();
}
