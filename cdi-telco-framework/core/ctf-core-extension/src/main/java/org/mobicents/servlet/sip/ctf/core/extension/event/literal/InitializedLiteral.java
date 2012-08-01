package org.mobicents.servlet.sip.ctf.core.extension.event.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.Initialized;

/**
 * @author Nicklas Karlsson
 */
public class InitializedLiteral extends AnnotationLiteral<Initialized> implements Initialized {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Initialized INSTANCE = new InitializedLiteral();
}
