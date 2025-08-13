package com.fva.rexs.maxslogger;

import jakarta.xml.bind.annotation.XmlEnumValue;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoutineTest {

    private static String getXmlEnumValue(final Enum<?> enumValue) throws NoSuchFieldException
    {
        final Field field = enumValue.getDeclaringClass().getField(enumValue.name());
        final XmlEnumValue annotation = field.getAnnotation(XmlEnumValue.class);
        return annotation.value();
    }

    @Test
    void testGetRoutineString() throws NoSuchFieldException
    {
        assertEquals("tr06", getXmlEnumValue(Routine.TR06));
        assertEquals("dr14028_2020", getXmlEnumValue(Routine.DR14028_2020));
        assertEquals("iso21771_2007", getXmlEnumValue(Routine.ISO21771_2007));
        assertEquals("iso21771_draft", getXmlEnumValue(Routine.ISO21771_DRAFT));
        assertEquals("dr401_2014", getXmlEnumValue(Routine.DR401_2014));
        assertEquals("unknown", getXmlEnumValue(Routine.UNKNOWN));
    }
}
