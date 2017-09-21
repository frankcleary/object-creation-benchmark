/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.agoncal.sample.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ObjectCreation {
    @Param({"10000000"})
    private int LOOP_SIZE;
    private static final Long ONE = 1L;

    public Long escapee;
    public long primitiveEscapee;

    @Benchmark
    public Long createNoObjects() {
        for (int i = 0; i < LOOP_SIZE; i++) {
            primitiveEscapee = i;
        }
        return ONE;
    }

    @Benchmark
    public Long createShortLivedObjects() {
        for (int i = 0; i < LOOP_SIZE; i++) {
            escapee = Long.valueOf(i);
        }
        return escapee;
    }

    @Benchmark
    public List<Long> createLongLivedObjectsArrayList() {
        List<Long> result = new ArrayList<>(LOOP_SIZE);
        for (int i = 0; i < LOOP_SIZE; i++) {
            result.add(Long.valueOf(i));
        }
        return result;
    }

    @Benchmark
    public List<Long> createLongLivedObjectsLinkedList() {
        List<Long> result = new LinkedList<>();
        for (int i = 0; i < LOOP_SIZE; i++) {
            result.add(Long.valueOf(i));
        }
        return result;
    }

    @Benchmark
    public List<Long> createListOneObject() {
        List<Long> result = new ArrayList<>(LOOP_SIZE);
        for (int i = 0; i < LOOP_SIZE; i++) {
            result.add(ONE);
        }
        return result;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ObjectCreation.class.getSimpleName())
                .warmupIterations(10)
                .jvmArgs("-Xms4G", "-Xmx4G")
                .measurementIterations(20)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
