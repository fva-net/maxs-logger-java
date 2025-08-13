package com.fva.rexs.maxslogger;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing message types.
 */
@XmlEnum
@Getter
@AllArgsConstructor
public enum MessageType {
    /**
     * Represents an error message type.
     */
    @XmlEnumValue("ERROR")
    ERROR,

    /**
     * Represents a warning message type.
     */
    @XmlEnumValue("WARNING")
    WARNING,

    /**
     * Represents an info message type.
     */
    @XmlEnumValue("INFO")
    INFO,

    /**
     * Represents a debug error message type.
     */
    @XmlEnumValue("DEBUG_ERROR")
    DEBUG_ERROR,

    /**
     * Represents a debug warning message type.
     */
    @XmlEnumValue("DEBUG_WARNING")
    DEBUG_WARNING,

    /**
     * Represents a debug info message type.
     */
    @XmlEnumValue("DEBUG_INFO")
    DEBUG_INFO
}
