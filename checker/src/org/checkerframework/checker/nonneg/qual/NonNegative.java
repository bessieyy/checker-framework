package org.checkerframework.checker.nonneg.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf( {UnknownInt.class } )
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface NonNegative {

}