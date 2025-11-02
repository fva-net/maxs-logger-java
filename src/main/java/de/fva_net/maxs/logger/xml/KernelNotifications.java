package de.fva_net.maxs.logger.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of notifications in the MAXS logging system.
 * <p>
 * This class holds all notifications for an application run, along with application metadata.
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "kernelNotifications")
public class KernelNotifications {

    /**
     * The list of notifications.
     */
    @XmlElement(name = "notification")
    private final List<Notification> notifications = new ArrayList<>();
    /**
     * The application ID.
     */
    @Setter
    @XmlAttribute
    private String appId;
    /**
     * The application version.
     */
    @Setter
    @XmlAttribute
    private String appVersion;

    /**
     * Adds a notification to the list of notifications.
     *
	 * @param notification the notification to add
     */
    public void add(final Notification notification)
    {
        notifications.add(notification);
    }

    /**
     * Clears all notifications from the list.
     */
	public void clear() {
        notifications.clear();
    }

    /**
	 * Returns the number of notifications.
     * @return the number of notifications
     */
	public int size() {
        return notifications.size();
    }

	/**
	 * Returns the notification at the specified index.
	 *
	 * @param index the index of the notification
	 * @return the notification at the given index
	 */
	public Notification get(int index) {
		return notifications.get(index);
	}
}
