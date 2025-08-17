package com.fva.rexs.maxslogger;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

import java.util.List;

/**
 * Represents a notification.
 */
@lombok.Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "message", "data" })
@XmlRootElement(name = "notification")
class Notification {

    /**
     * The component ID of the notification.
     */
    @XmlAttribute
    private int compId;

    /**
     * The list of items associated with the notification.
     */
    @XmlElementWrapper
    @XmlElement(name = "item")
    private List<Item> data;

    /**
     * The message of the notification.
     */
    @XmlElement(required = true)
    private String message;

    /**
     * The routine associated with the notification.
     * -- GETTER --
     *  Gets the routine as a string.

     */
    @Getter
    @XmlAttribute
    private String routine;

    /**
     * The type of the notification.
     */
    @XmlAttribute(required = true)
    private MessageType type;

    /**
     * Retrieves the list of items associated with the notification. If the list is null, it initializes it to an empty list.
     *
     * @return the list of items
     */
    public List<Item> getData()
    {
        if (data == null) {
            data = new java.util.ArrayList<>();
        }
        return data;
    }

    /**
     * Sets the routine from a Routine object.
     */
    public void setRoutine(Routine routine) {
        this.routine = (routine != null) ? routine.getId() : null;
    }

}