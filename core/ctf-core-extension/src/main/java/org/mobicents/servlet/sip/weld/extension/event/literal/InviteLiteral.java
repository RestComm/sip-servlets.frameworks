package org.mobicents.servlet.sip.weld.extension.event.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Invite;

public class InviteLiteral extends AnnotationLiteral<Invite> implements Invite {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Invite INSTANCE = new InviteLiteral();
}