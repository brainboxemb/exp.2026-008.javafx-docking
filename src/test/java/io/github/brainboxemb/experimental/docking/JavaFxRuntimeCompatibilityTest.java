package io.github.brainboxemb.experimental.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaFxRuntimeCompatibilityTest {

    @Test
    void objectPropertyExposesMapFunction() throws Exception {
        Method method =
                ObjectProperty.class.getMethod(
                        "map",
                        Function.class);

        assertNotNull(method);
        assertEquals(
                ObservableValue.class,
                method.getReturnType());

        JavaFxRuntimeDiagnostics.log(
                "Maven test runtime");
    }
}
