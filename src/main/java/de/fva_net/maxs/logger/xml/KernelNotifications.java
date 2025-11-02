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
}
