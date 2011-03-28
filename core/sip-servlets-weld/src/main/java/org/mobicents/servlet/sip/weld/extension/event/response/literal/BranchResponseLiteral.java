package org.mobicents.servlet.sip.weld.extension.event.response.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.weld.extension.event.response.BranchResponse;

public class BranchResponseLiteral extends AnnotationLiteral<BranchResponse> implements BranchResponse {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final BranchResponse INSTANCE = new BranchResponseLiteral();
}
