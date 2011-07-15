package org.mobicents.servlet.sip.ctf.core.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;


import org.mobicents.servlet.sip.ctf.core.extension.event.request.Bye;

public class ByeLiteral extends AnnotationLiteral<Bye> implements Bye {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Bye INSTANCE = new ByeLiteral();
}
