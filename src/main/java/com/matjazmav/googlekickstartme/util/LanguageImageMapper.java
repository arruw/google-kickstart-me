package com.matjazmav.googlekickstartme.util;

import java.util.*;

public class LanguageImageMapper {
    public static final Map<String, String> MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER){{
        put("Bash", f("linux/linux-original"));
        put("C", f("c/c-original"));
        put("Cpp", f("cplusplus/cplusplus-original"));
        put("CS", f("csharp/csharp-original"));
        put("Clojure", f("clojure/clojure-original"));
        put("Go", f("go/go-original"));
        put("Groovy", f("groovy/groovy-original"));
        put("Java", f("java/java-original"));
        put("JavaScript", f("javascript/javascript-original"));
        put("PHP", f("php/php-original"));
        put("Python2", f("python/python-original"));
        put("Python3", f("python/python-original"));
        put("PyPy2", f("python/python-original"));
        put("Ruby", f("ruby/ruby-original"));
        put("Rust", f("rust/rust-plain"));
        put("Scala", f("scala/scala-original"));
        put("Swift", f("swift/swift-original"));
        put("TypeScript", f("typescript/typescript-original"));
    }};

    private static String f(String token) {
        return String.format("https://devicons.github.io/devicon/devicon.git/icons/%s.svg", token);
    }

    public static String get(String key) {
        return MAP.getOrDefault(key, null);
    }
}
