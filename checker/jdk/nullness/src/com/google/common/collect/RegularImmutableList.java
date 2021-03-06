/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

/**
 * Implementation of {@link ImmutableList} with one or more elements.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(serializable = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
final class RegularImmutableList<E> extends ImmutableList<E> {
  private final transient int offset;
  private final transient int size;
  private final transient Object[] array;

  RegularImmutableList(Object[] array, int offset, int size) {
    this.offset = offset;
    this.size = size;
    this.array = array;
  }

  RegularImmutableList(Object[] array) {
    this(array, 0, array.length);
  }

  @Override
@Pure public int size() {
    return size;
  }

  @Pure @Override public boolean isEmpty() {
    return false;
  }

  @Pure @Override public boolean contains(/*@Nullable*/ Object target) {
    return indexOf(target) != -1;
  }

  // The fake cast to E is safe because the creation methods only allow E's
  @SuppressWarnings("unchecked")
  @Override public UnmodifiableIterator<E> iterator() {
    return (UnmodifiableIterator<E>) Iterators.forArray(array, offset, size);
  }

  @Override public Object[] toArray() {
    Object[] newArray = new Object[size()];
    System.arraycopy(array, offset, newArray, 0, size);
    return newArray;
  }

  @SuppressWarnings("nullness")
      //Suppressed due to annotations on toArray
  @Override public <T extends /*@Nullable*/ Object> T[] toArray(T[] other) {
    if (other.length < size) {
      other = ObjectArrays.newArray(other, size);
    } else if (other.length > size) {
      other[size] = null;
    }
    System.arraycopy(array, offset, other, 0, size);
    return other;
  }

  // The fake cast to E is safe because the creation methods only allow E's
  @Override
@SuppressWarnings("unchecked")
  public E get(int index) {
    Preconditions.checkElementIndex(index, size);
    return (E) array[index + offset];
  }

  @Pure @Override public int indexOf(/*@Nullable*/ Object target) {
    if (target != null) {
      for (int i = offset; i < offset + size; i++) {
        if (array[i].equals(target)) {
          return i - offset;
        }
      }
    }
    return -1;
  }

  @Pure @Override public int lastIndexOf(/*@Nullable*/ Object target) {
    if (target != null) {
      for (int i = offset + size - 1; i >= offset; i--) {
        if (array[i].equals(target)) {
          return i - offset;
        }
      }
    }
    return -1;
  }

  @SideEffectFree @Override public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
    return (fromIndex == toIndex)
        ? ImmutableList.<E>of()
        : new RegularImmutableList<E>(
            array, offset + fromIndex, toIndex - fromIndex);
  }

  @Override
public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  @Override
public ListIterator<E> listIterator(final int start) {
    Preconditions.checkPositionIndex(start, size);

    return new ListIterator<E>() {
      int index = start;

      @Override
    public boolean hasNext() {
        return index < size;
      }
      @Override
    public boolean hasPrevious() {
        return index > 0;
      }

      @Override
    public int nextIndex() {
        return index;
      }
      @Override
    public int previousIndex() {
        return index - 1;
      }

      @Override
    public E next() {
        E result;
        try {
          result = get(index);
        } catch (IndexOutOfBoundsException rethrown) {
          throw new NoSuchElementException();
        }
        index++;
        return result;
      }
      @Override
    public E previous() {
        E result;
        try {
          result = get(index - 1);
        } catch (IndexOutOfBoundsException rethrown) {
          throw new NoSuchElementException();
        }
        index--;
        return result;
      }

      @Override
    public void set(E o) {
        throw new UnsupportedOperationException();
      }
      @Override
    public void add(E o) {
        throw new UnsupportedOperationException();
      }
      @Override
    public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Pure @Override public boolean equals(@Nullable Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof List)) {
      return false;
    }

    List<?> that = (List<?>) object;
    if (this.size() != that.size()) {
      return false;
    }

    int index = offset;
    if (object instanceof RegularImmutableList) {
      RegularImmutableList<?> other = (RegularImmutableList<?>) object;
      for (int i = other.offset; i < other.offset + other.size; i++) {
        if (!array[index++].equals(other.array[i])) {
          return false;
        }
      }
    } else {
      for (Object element : that) {
        if (!array[index++].equals(element)) {
          return false;
        }
      }
    }
    return true;
  }

  @Pure @Override public int hashCode() {
    // not caching hash code since it could change if the elements are mutable
    // in a way that modifies their hash codes
    int hashCode = 1;
    for (int i = offset; i < offset + size; i++) {
      hashCode = 31 * hashCode + array[i].hashCode();
    }
    return hashCode;
  }

  @Pure @Override public String toString() {
    StringBuilder sb = new StringBuilder(size() * 16);
    sb.append('[').append(array[offset]);
    for (int i = offset + 1; i < offset + size; i++) {
      sb.append(", ").append(array[i]);
    }
    return sb.append(']').toString();
  }
}
