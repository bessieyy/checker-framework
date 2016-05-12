package org.checkerframework.checker.nonneg;

import java.util.List;

import javax.lang.model.element.VariableElement;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.GenericAnnotatedTypeFactory;
import org.checkerframework.javacutil.Pair;

public class NonNegAnalysis extends CFAbstractAnalysis<CFValue, CFStore, NonNegTransfer> {

	public NonNegAnalysis(BaseTypeChecker checker,
			NonNegAnnotatedTypeFactory factory,
			List<Pair<VariableElement, CFValue>> fieldValues) {
		super(checker, factory, fieldValues);
	}

	@Override
	public CFValue createAbstractValue(AnnotatedTypeMirror type) {
		return defaultCreateAbstractValue(this, type);
	}

	@Override
	public CFStore createCopiedStore(CFStore s) {
		return new CFStore(this, s);
	}

	@Override
	public CFStore createEmptyStore(boolean sequentialSemantics) {
		return new CFStore(this, sequentialSemantics);
	}

	@Override
    public NonNegTransfer createTransferFunction() {
        return new NonNegTransfer(this);
    }
}

