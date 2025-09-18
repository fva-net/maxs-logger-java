package io.github.fvanet.maxsloggerjava.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the kernel notifications of a notification logger.
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
     * @param notification
     *         the notification to add
     */
    public void add(final Notification notification)
    {
        notifications.add(notification);
    }

    /**
     * Clears all notifications from the list.
     */
    public void clear()
    {
        notifications.clear();
    }

    /**
     * Retrieves a notification by its index.
     *
     * @param index
     *         the index of the notification to retrieve
     *
     * @return the notification at the specified index
     */
    public Notification get(final int index)
    {
        return notifications.get(index);
    }

    /**
     * Returns the number of notifications in the list.
     *
     * @return the number of notifications
     */
    public int size()
    {
        return notifications.size();
    }
}
