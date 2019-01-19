package edu.boscotech.frc.components;

import edu.boscotech.frc.config.Config;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Lifecam {
  UsbCamera camera;
  String name;

  public Lifecam(String name) {
    this.name = name;
  }

  public void startCameraStream() {
    Config cfg = Config.getInstance();
    int 
      port = cfg.getInt("cameras", name, "port"),
      width = cfg.getInt("cameras", name, "width"),
      height = cfg.getInt("cameras", name, "height"),
      fps = cfg.getInt("cameras", name, "fps");

    camera = CameraServer.getInstance().startAutomaticCapture(port);
    camera.setResolution(width, height);
    camera.setFPS(fps);
  }
}