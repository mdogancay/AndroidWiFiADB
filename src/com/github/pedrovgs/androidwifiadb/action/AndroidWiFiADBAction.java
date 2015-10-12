package com.github.pedrovgs.androidwifiadb.action;

import com.github.pedrovgs.androidwifiadb.adb.ADB;
import com.github.pedrovgs.androidwifiadb.AndroidWiFiADB;
import com.github.pedrovgs.androidwifiadb.Device;
import com.github.pedrovgs.androidwifiadb.View;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import java.util.List;

public class AndroidWiFiADBAction extends AnAction implements View {

  private static final String ANDROID_WIFI_ADB_TITLE = "Android WiFi ADB";
  private static final NotificationGroup NOTIFICATION_GROUP =
      NotificationGroup.balloonGroup(ANDROID_WIFI_ADB_TITLE);

  private final AndroidWiFiADB androidWifiADB;

  public AndroidWiFiADBAction() {
    ADB adb = new ADB();
    this.androidWifiADB = new AndroidWiFiADB(adb, this);
  }

  public void actionPerformed(AnActionEvent event) {
    androidWifiADB.connectDevices();
  }

  @Override public void showNoConnectedDevicesNotification() {
    showNotification(ANDROID_WIFI_ADB_TITLE, "There are no connected devices with a USB cable.",
        NotificationType.INFORMATION);
  }

  @Override public void showConnectedDevicesNotification(List<Device> devices) {
    for (Device device : devices) {
      showNotification(ANDROID_WIFI_ADB_TITLE, "Device '" + device.getName() + "' connected.",
          NotificationType.INFORMATION);
    }
  }

  private void showNotification(final String title, final String message,
      final NotificationType type) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override public void run() {
        Notification notification =
            NOTIFICATION_GROUP.createNotification(title, message, type, null);
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Notifications.Bus.notify(notification, projects[0]);
      }
    });
  }
}
