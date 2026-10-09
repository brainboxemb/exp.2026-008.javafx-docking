package io.github.brainboxemb.experimental.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaFxRuntimeCompatibilityTest {

    private static final String JAVAFX_VERSION =
            "21.0.10";

    @Test
    void objectPropertyExposesMapFunction() throws Exception {
        JavaFxRuntimeDiagnostics.log(
                "Maven test runtime");

        Method method =
                ObjectProperty.class.getMethod(
                        "map",
                        Function.class);

        assertNotNull(method);
        assertEquals(
                ObservableValue.class,
                method.getReturnType());
    }

    @Test
    void classpathContainsOnlyConfiguredJavaFxVersion() {
        String classPath =
                System.getProperty("java.class.path", "");

        List<String> conflictingEntries =
                Arrays.stream(
                                classPath.split(
                                        java.util.regex.Pattern.quote(
                                                File.pathSeparator)))
                        .map(path ->
                                path.replace('\\', '/'))
                        .filter(path ->
                                path.contains(
                                        "/org/openjfx/javafx-"))
                        .filter(path ->
                                !path.contains(
                                        "/" + JAVAFX_VERSION + "/"))
                        .toList();

        org.junit.jupiter.api.Assertions.assertTrue(
                conflictingEntries.isEmpty(),
                () -> "Conflicting JavaFX artifacts on classpath: "
                        + conflictingEntries);
    }
}
