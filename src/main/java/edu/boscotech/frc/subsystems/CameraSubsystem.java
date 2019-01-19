package edu.boscotech.frc.subsystems;

import java.util.ArrayList;
import java.util.List;

import edu.boscotech.frc.components.StreamedCamera;
import edu.boscotech.frc.util.DefaultCommandCreator;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraSubsystem extends Subsystem 
  implements DefaultCommandCreator {
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

  @Override
  protected void initDefaultCommand() { }

  // Everything is already handled by the cameras themselves, no need to run
  // commands during teleop or test.
  @Override
  public Command createDefaultTeleopCommand() {
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return null;
  }
}