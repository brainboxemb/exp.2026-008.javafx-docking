package io.github.brainboxemb.experimental.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.lang.module.ModuleDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

/**
 * Small runtime diagnostic used while qualifying the JavaFX docking candidates.
 */
public final class JavaFxRuntimeDiagnostics {

    private static final String PREFIX =
            "[JavaFX runtime] ";

    private JavaFxRuntimeDiagnostics() {
    }

    public static void log(
            String context,
            Class<?>... extraClasses) {
        System.out.println();
        System.out.println(
                PREFIX + "=== " + context + " ===");
        System.out.println(
                PREFIX + "java.version = "
                + System.getProperty("java.version"));
        System.out.println(
                PREFIX + "java.vm.name = "
                + System.getProperty("java.vm.name"));
        System.out.println(
                PREFIX + "javafx.runtime.version = "
                + System.getProperty("javafx.runtime.version"));
        System.out.println(
                PREFIX + "javafx.version = "
                + System.getProperty("javafx.version"));

        describeClass(
                ObjectProperty.class);
        describeClass(
                ObservableValue.class);

        for (Class<?> extraClass : extraClasses) {
            describeClass(extraClass);
        }

        describeMapMethod();
        describeRelevantPath(
                "java.class.path");
        describeRelevantPath(
                "jdk.module.path");

        System.out.println(
                PREFIX + "=== end ===");
        System.out.println();
    }

    private static void describeClass(
            Class<?> type) {
        Module module =
                type.getModule();
        ModuleDescriptor descriptor =
                module.getDescriptor();

        String moduleName =
                module.isNamed()
                        ? module.getName()
                        : "<unnamed>";
        String moduleVersion =
                descriptor == null
                        ? "<none>"
                        : descriptor.rawVersion()
                                .orElse("<none>");

        Package typePackage =
                type.getPackage();

        System.out.println(
                PREFIX + "class = "
                + type.getName());
        System.out.println(
                PREFIX + "  module = "
                + moduleName
                + " version="
                + moduleVersion);
        System.out.println(
                PREFIX + "  package implementationVersion = "
                + (typePackage == null
                        ? "<none>"
                        : String.valueOf(
                                typePackage
                                        .getImplementationVersion())));
        System.out.println(
                PREFIX + "  classLoader = "
                + String.valueOf(
                        type.getClassLoader()));
        System.out.println(
                PREFIX + "  codeSource = "
                + codeSource(type));
    }

    private static String codeSource(
            Class<?> type) {
        try {
            CodeSource codeSource =
                    type.getProtectionDomain()
                            .getCodeSource();

            if (codeSource == null) {
                return "<none>";
            }

            URL location =
                    codeSource.getLocation();
            return String.valueOf(location);
        } catch (SecurityException exception) {
            return "<unavailable: "
                    + exception.getMessage()
                    + ">";
        }
    }

    private static void describeMapMethod() {
        try {
            Method method =
                    ObjectProperty.class.getMethod(
                            "map",
                            Function.class);

            System.out.println(
                    PREFIX + "ObjectProperty.map(Function) = PRESENT");
            System.out.println(
                    PREFIX + "  declaringClass = "
                    + method.getDeclaringClass().getName());
            System.out.println(
                    PREFIX + "  returnType = "
                    + method.getReturnType().getName());
            System.out.println(
                    PREFIX + "  signature = "
                    + method);
        } catch (NoSuchMethodException exception) {
            System.out.println(
                    PREFIX + "ObjectProperty.map(Function) = MISSING");
        }
    }

    private static void describeRelevantPath(
            String propertyName) {
        String value =
                System.getProperty(propertyName);

        System.out.println(
                PREFIX + propertyName
                + " relevant entries:");

        if (value == null || value.isBlank()) {
            System.out.println(
                    PREFIX + "  <none>");
            return;
        }

        String[] entries =
                value.split(
                        java.util.regex.Pattern.quote(
                                File.pathSeparator));

        String[] relevantEntries =
                Arrays.stream(entries)
                        .filter(JavaFxRuntimeDiagnostics
                                ::isRelevantPath)
                        .toArray(String[]::new);

        if (relevantEntries.length == 0) {
            System.out.println(
                    PREFIX + "  <no JavaFX/docking entries>");
            return;
        }

        for (String entry : relevantEntries) {
            System.out.println(
                    PREFIX + "  " + entry);
        }
    }

    private static boolean isRelevantPath(
            String path) {
        String lower =
                path.toLowerCase(Locale.ROOT);

        return lower.contains("javafx")
                || lower.contains("bento")
                || lower.contains("transit")
                || lower.contains("snapfx")
                || lower.contains("fxskins")
                || lower.contains("fxthemes")
                || lower.contains("controlsfx");
    }
}
