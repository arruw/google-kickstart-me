package com.matjazmav.googlekickstartme.util;

import java.util.*;

public class LanguageImageMapper {
    private static final Map<String, String> PROGRAMMING_LANGUAGE_IMAGE_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER){{
        put("Bash", f("linux"));
        put("C", f("c"));
        put("Cpp", f("cplusplus"));
        put("CS", f("csharp"));
        put("Clojure", f("clojure"));
        put("Go", f("go"));
        put("Groovy", f("groovy"));
        put("Java", f("java"));
        put("JavaScript", f("javascript"));
        put("PHP", f("php"));
        put("Python2", f("python"));
        put("Python3", f("python"));
        put("PyPy2", f("python"));
        put("Ruby", f("ruby"));
        put("Rust", f("rust"));
        put("Scala", f("scala"));
        put("Swift", f("swift"));
        put("TypeScript", f("typescript"));
    }};

    private static String f(String token) {
        return String.format("https://devicons.github.io/devicon/devicon.git/icons/%s/%s-original.svg", token, token);
    }

    public static String get(String key) {
        return PROGRAMMING_LANGUAGE_IMAGE_MAP.getOrDefault(key, null);
    }
}
