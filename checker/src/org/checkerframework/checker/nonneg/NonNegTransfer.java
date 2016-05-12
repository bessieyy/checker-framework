package org.checkerframework.checker.nonneg;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.checker.nonneg.qual.NonNegative;
import org.checkerframework.dataflow.analysis.ConditionalTransferResult;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.analysis.FlowExpressions.LocalVariable;
import org.checkerframework.dataflow.cfg.node.GreaterThanOrEqualNode;
import org.checkerframework.dataflow.cfg.node.LocalVariableNode;
import org.checkerframework.dataflow.cfg.node.ValueLiteralNode;
import org.checkerframework.framework.flow.CFAbstractTransfer;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.type.AnnotatedTypeMirror;

public class NonNegTransfer extends CFAbstractTransfer<CFValue, CFStore, NonNegTransfer> {

	private NonNegAnnotatedTypeFactory factory;
	
	public NonNegTransfer(NonNegAnalysis analysis) {
		super(analysis);
		factory = (NonNegAnnotatedTypeFactory) analysis.getTypeFactory();
	}
	
	@Override
	public TransferResult<CFValue, CFStore> visitGreaterThanOrEqual(GreaterThanOrEqualNode node, TransferInput<CFValue, CFStore> in) {
		TransferResult<CFValue, CFStore> result = super.visitGreaterThanOrEqual(node, in);
		if (node.getLeftOperand() instanceof LocalVariableNode) {
			LocalVariableNode leftNode = (LocalVariableNode) node.getLeftOperand();
			
			CFStore thenStore = result.getRegularStore();
            CFStore elseStore = thenStore.copy();
            ConditionalTransferResult<CFValue, CFStore> newResult = new ConditionalTransferResult<>(
                    result.getResultValue(), thenStore, elseStore);
            elseStore.insertValue(new LocalVariable(leftNode), factory.createUnknownAnnotation());
            
            if (node.getRightOperand() instanceof ValueLiteralNode) {
            	ValueLiteralNode rightNode = (ValueLiteralNode) node.getRightOperand();
            	int v = (int) rightNode.getValue();
            	if (v >= 0) {
					AnnotationMirror an = factory.createNonNegAnnotation();
					thenStore.insertValue(new LocalVariable(leftNode), an);
					return newResult;
				}
            } else {
            	AnnotatedTypeMirror operand = factory.getAnnotatedType(node.getRightOperand().getTree());
            	if(operand.hasAnnotation(NonNegative.class)) {
    				AnnotationMirror an = factory.createNonNegAnnotation();
    				thenStore.insertValue(new LocalVariable(leftNode), an);
    				return newResult;
    			}
            }
		}
		return result;
	}
}
