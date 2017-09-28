package com.bettercodebytes.objectcreation;

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
