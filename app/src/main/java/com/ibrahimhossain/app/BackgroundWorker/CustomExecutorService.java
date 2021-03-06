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

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings({"UnusedDeclaration"})
public interface CustomExecutorService extends CustomExecutor {

    void shutdownThread();


    List<Runnable> shutDownThreadNow();

    boolean isThreadShutDown();


    boolean isThreadTerminated();

    boolean awaitingThreadTermination(long timeout, TimeUnit unit)
            throws InterruptedException;

    <T> CustomFuture<T> submit(CustomCallable<T> task);


    <T> CustomFuture<T> submit(Runnable task, T result);


    CustomFuture<?> submit(Runnable task);

    <T> List<CustomFuture<T>> invokeAll(CustomCollection<? extends CustomCallable<T>> tasks)
            throws InterruptedException;

    <T> List<CustomFuture<T>> invokeAll(CustomCollection<? extends CustomCallable<T>> tasks,
                                  long timeout, TimeUnit unit)
            throws InterruptedException;

    <T> T invokeAny(CustomCollection<? extends CustomCallable<T>> tasks)
            throws InterruptedException, ExecutionException;


    <T> T invokeAny(CustomCollection<? extends CustomCallable<T>> tasks,
                    long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;
}
