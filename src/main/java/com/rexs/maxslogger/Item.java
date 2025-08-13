package com.rexs.maxslogger;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

/**
 * Represents an item inside the data.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "compId", "attrId", "value" })
@XmlRootElement(name = "item")
class Item {

    /**
     * The attribute ID of the item.
     */
    @XmlAttribute(name = "attrId", required = true)
    private String attrId = "";

    /**
     * The component ID of the item.
     */
    @XmlAttribute(name = "compId", required = true)
    private int compId;

    /**
     * The value of the item.
     */
    @XmlAttribute(name = "value", required = true)
    private double value;
}