/**
 * 
 */
package org.mobicents.servlet.sip.ctf.core.extension.event.servlet.literal;

import javax.enterprise.util.AnnotationLiteral;
import org.mobicents.servlet.sip.ctf.core.extension.event.servlet.ServletInitialized;

/**
 * @author gvagenas 
 * gvagenas@gmail.com
 * 
 */
public class ServletInitializedLiteral extends AnnotationLiteral<ServletInitialized> implements ServletInitialized {
    private static final long serialVersionUID = 1340645410138297467L;
    public static final ServletInitialized INSTANCE = new ServletInitializedLiteral();
}
