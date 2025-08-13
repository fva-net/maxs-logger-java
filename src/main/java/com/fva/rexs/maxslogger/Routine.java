package com.fva.rexs.maxslogger;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing routine values.
 */
@XmlEnum
@Getter
@AllArgsConstructor
public enum Routine {
    /**
     * Represents the tr06 routine.
     */
    @XmlEnumValue("tr06")
    TR06,
    /**
     * Represents the dr14028_2020 routine.
     */
    @XmlEnumValue("dr14028_2020")
    DR14028_2020,
    /**
     * Represents the dr401_2014 routine.
     */
    @XmlEnumValue("dr401_2014")
    DR401_2014,
    /**
     * Represents the DIN 51563:2011-04 routine.
     */
    @XmlEnumValue("din51563_2011")
    DIN51563_2011,
    /**
     * Represents the iso21771_2007 routine.
     */
    @XmlEnumValue("iso21771_2007")
    ISO21771_2007,
    /**
     * Represents the iso21771_2024 routine.
     */
    @XmlEnumValue("iso21771_draft")
    ISO21771_DRAFT,
    /**
     * Represents the iso6336_2019 routine.
     */
    @XmlEnumValue("is06336_2019")
    ISO6336_2019,
    /**
     * Represents an unknown routine.
     */
    @XmlEnumValue("unknown")
    UNKNOWN
}
