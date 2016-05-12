package org.checkerframework.checker.nonneg;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.checker.nonneg.qual.NonNegative;
import org.checkerframework.checker.nonneg.qual.UnknownInt;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.GenericAnnotatedTypeFactory;
import org.checkerframework.framework.type.treeannotator.ImplicitsTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.PropagationTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;
import org.checkerframework.framework.util.AnnotationBuilder;
import org.checkerframework.javacutil.AnnotationUtils;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;

public class NonNegAnnotatedTypeFactory extends GenericAnnotatedTypeFactory<CFValue, CFStore, NonNegTransfer, NonNegAnalysis> {
	protected final AnnotationMirror UNKNOWNINT, NONNEGATIVE;
	
	public NonNegAnnotatedTypeFactory(BaseTypeChecker checker) {
		super(checker);
		UNKNOWNINT = AnnotationUtils.fromClass(elements, UnknownInt.class);
		NONNEGATIVE = AnnotationUtils.fromClass(elements, NonNegative.class);
		this.postInit();
	}
	
	@Override
    public TreeAnnotator createTreeAnnotator() {
        return new ListTreeAnnotator(
                new ImplicitsTreeAnnotator(this),
                new NonNegTreeAnnotator(this),
                new PropagationTreeAnnotator(this)
        );
    }
	
	AnnotationMirror createUnknownAnnotation() {
        AnnotationBuilder builder =
            new AnnotationBuilder(processingEnv, UnknownInt.class);
        return builder.build();
    }
	
	AnnotationMirror createNonNegAnnotation() {
        AnnotationBuilder builder =
            new AnnotationBuilder(processingEnv, NonNegative.class);
        return builder.build();
    }
	
	private class NonNegTreeAnnotator extends TreeAnnotator {

		public NonNegTreeAnnotator(AnnotatedTypeFactory atypeFactory) {
			super(atypeFactory);
		}
		
		@Override
        public Void visitLiteral(LiteralTree tree, AnnotatedTypeMirror type) {
            if (!type.isAnnotatedInHierarchy(NONNEGATIVE)) {
                if (tree.getKind() == Tree.Kind.INT_LITERAL) {
                    int i = (int) tree.getValue();
                    if (i >= 0) {
                    	type.addAnnotation(createNonNegAnnotation());
                    }
                }
            }
            return super.visitLiteral(tree, type);
        }
		
		@Override
		public Void visitUnary(UnaryTree tree, AnnotatedTypeMirror type) {
			if (tree.getKind() == Tree.Kind.POSTFIX_DECREMENT) {
				type.addAnnotation(createUnknownAnnotation());
			}
			return super.visitUnary(tree, type);
		}
	}
}
