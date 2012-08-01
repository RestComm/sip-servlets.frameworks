package org.mobicents.servlet.sip.ctf.core.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.request.Options;

public class OptionsLiteral extends AnnotationLiteral<Options> implements Options {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Options INSTANCE = new OptionsLiteral();
}
