package edu.boscotech.frc.subsystems;

import java.util.ArrayList;
import java.util.List;

import edu.boscotech.frc.components.StreamedCamera;

public class CameraSubsystem {
  private List<StreamedCamera> cameras = new ArrayList<>();

  public CameraSubsystem(String... cameraNames) {
    for (String name : cameraNames) {
      cameras.add(new StreamedCamera(name));
    }
  }

  public void startCameraStreams() {
    for (StreamedCamera camera : cameras) {
      camera.startCameraStream();
    }
  }
}