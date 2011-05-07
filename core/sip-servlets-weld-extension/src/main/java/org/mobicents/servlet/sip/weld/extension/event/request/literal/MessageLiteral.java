package org.mobicents.servlet.sip.weld.extension.event.request.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.request.Message;

public class MessageLiteral extends AnnotationLiteral<Message> implements Message {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final Message INSTANCE = new MessageLiteral();
}
