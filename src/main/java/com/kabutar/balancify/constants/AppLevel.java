package com.kabutar.balancify.constants;


import java.util.Objects;

public class AppLevel {
    private static final ClassLoader classLoader = AppLevel.class.getClassLoader();
    public static final String BASE_CONFIG_FILE_PATH = Objects.requireNonNull(classLoader.getResource("application.yaml")).getPath();

}
