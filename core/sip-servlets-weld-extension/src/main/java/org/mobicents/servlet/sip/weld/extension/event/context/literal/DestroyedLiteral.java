package org.mobicents.servlet.sip.weld.extension.event.context.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.context.Destroyed;

/**
 * @author Nicklas Karlsson
 */
public class DestroyedLiteral extends AnnotationLiteral<Destroyed> implements Destroyed {
    private static final long serialVersionUID = -6004283843896030539L;
    public static final Destroyed INSTANCE = new DestroyedLiteral();
}

