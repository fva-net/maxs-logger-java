package de.fva_net.maxs.logger.xml;

import de.fva_net.maxs.logger.MaxsLoggableRoutine;
import de.fva_net.maxs.logger.MaxsMessageType;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a notification entry in the MAXS logging system.
 * <p>
 * A notification contains a message, its type, associated routine, component ID, and a list of data items.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "message", "data" })
@XmlRootElement(name = "notification")
public class Notification {

    /**
	 * The component ID associated with this notification.
     */
    @XmlAttribute
    private int compId;

    /**
	 * The list of items (attributes and values) associated with this notification.
	 * <p>
	 * This list is initialized on first access if it is null.
     */
    @XmlElementWrapper
    @XmlElement(name = "item")
    private List<Item> data;

    /**
	 * The message describing the notification.
     */
    @XmlElement(required = true)
    private String message;

    /**
	 * The routine associated with this notification, as a string identifier.
	 */
    @XmlAttribute
    private String routine;

    /**
	 * The type of the notification message.
     */
    @XmlAttribute(required = true)
	private MaxsMessageType type;

    /**
	 * Sets the routine for this notification.
	 * @param routine the routine to associate
	 */
	public void setRoutine(MaxsLoggableRoutine routine) {
		this.routine = routine != null ? routine.getMaxsId() : null;
	}

	/**
	 * Returns the list of data items, initializing it if necessary.
     * @return the list of items
	 */
	public List<Item> getData() {
        if (data == null) {
			data = new ArrayList<>();
        }
        return data;
    }
}
