package org.checkerframework.checker.nonneg;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;

public class NonNegVisitor extends BaseTypeVisitor<NonNegAnnotatedTypeFactory> {

	public NonNegVisitor(BaseTypeChecker checker) {
		super(checker);
	}
}
