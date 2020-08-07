package com.matjazmav.googlekickstartme.util.function;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}