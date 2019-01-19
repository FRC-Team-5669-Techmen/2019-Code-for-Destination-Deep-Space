package edu.boscotech.frc.subsystems;

import edu.boscotech.frc.components.LidarLite3;
import edu.boscotech.frc.util.DefaultCommandCreator;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LidarSubsystem extends Subsystem implements DefaultCommandCreator {
  private LidarLite3 sensor = new LidarLite3();

  public LidarSubsystem() {
    super();
    sensor.useFreeRunningMode();
  }

  /**
   * Gets a measurement from the sensor (non-blocking).
   * @return The distance read by the sensor, in centimeters.
   */
  public int getDistance() {
    return sensor.getLastMeasurement();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  @Override
  public Command createDefaultTeleopCommand() {
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return null;
  }
}