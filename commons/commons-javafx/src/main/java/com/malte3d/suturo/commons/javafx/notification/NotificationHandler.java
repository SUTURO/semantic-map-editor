package com.malte3d.suturo.commons.javafx.notification;

import com.google.common.base.Preconditions;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.geometry.Pos;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.experimental.UtilityClass;
import org.controlsfx.control.Notifications;

/**
 * Utility class for notifications.
 */
@UtilityClass
public class NotificationHandler {

    private static final int MAX_NOTIFICATIONS = 10;

    public static Window stage;

    /**
     * Creates a new {@link Notifications} instance with the given parent window.
     *
     * @return a new {@link Notifications} instance with the given parent window.
     */
    public static Notifications create() {

        Preconditions.checkNotNull(stage, "The NotificationHandler parent window must be initialized!");

        return Notifications.create()
                .owner(stage)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(10))
                .threshold(MAX_NOTIFICATIONS, thresholdNotification());
    }

    private static Notifications thresholdNotification() {

        return Notifications.create()
                .owner(stage)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(10))
                .title(Messages.getString("Application.Notification.Title.Error"))
                .text(Messages.getString("Application.Notification.MaxThreshold.Text"));
    }

}
