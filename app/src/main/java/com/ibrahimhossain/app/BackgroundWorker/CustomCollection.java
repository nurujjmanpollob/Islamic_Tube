/*
 *
 *  Copyright 2021 Nurujjaman Pollob.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.ibrahimhossain.app.BackgroundWorker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings({"UnusedDeclaration"})
public interface CustomCollection<E> extends Iterable<E> {
    // Query Operations

    @SuppressWarnings({"UnusedDeclaration"})
    int size();

    @SuppressWarnings({"UnusedDeclaration"})
    boolean isEmpty();


    @SuppressWarnings({"UnusedDeclaration"})
    boolean contains(Object o);

    @NotNull Iterator<E> iterator();

    @SuppressWarnings({"UnusedDeclaration"})
    Object[] toArray();

    <T> T[] toArray(T[] a);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings({"UnusedDeclaration"})
    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }

    // Modification Operations
    @SuppressWarnings({"UnusedDeclaration"})
    boolean add(E e);

    @SuppressWarnings({"UnusedDeclaration"})
    boolean remove(Object o);


    // Bulk Operations

    @SuppressWarnings({"UnusedDeclaration"})
    boolean containsAll(CustomCollection<?> c);

    @SuppressWarnings({"UnusedDeclaration"})
    boolean addAll(CustomCollection<? extends E> c);

    @SuppressWarnings({"UnusedDeclaration"})
   boolean removeAll(CustomCollection<?> c);


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings({"UnusedDeclaration"})
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    boolean retainAll(CustomCollection<?> c);

    @SuppressWarnings({"UnusedDeclaration"})
    void clear();


    // Comparison and hashing
    boolean equals(Object o);

    int hashCode();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NotNull
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(new CustomCollection[]{this}, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
