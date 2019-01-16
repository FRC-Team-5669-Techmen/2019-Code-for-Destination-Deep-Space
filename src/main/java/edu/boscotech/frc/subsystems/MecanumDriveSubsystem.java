package edu.boscotech.frc.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * An adapter class for {@link MecanumDrive} that also sets up its motor
 * controllers.
 */
public class MecanumDriveSubsystem extends Subsystem {
  MecanumDrive drive = new MecanumDrive(
    new Victor(1),
    new Victor(2),
    new Victor(3),
    new Victor(4)
  );

  public MecanumDriveSubsystem() { }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's 
   * speed is independent from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is 
   *                  positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is
   *                  positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
    drive.driveCartesian(ySpeed, xSpeed, zRotation);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured counter-clockwise from straight ahead. The speed at
   * which the robot drives (translation) is independent from its angle or 
   * rotation rate.
   *
   * @param magnitude The robot's speed at a given angle [-1.0..1.0]. Forward is
   *                  positive.
   * @param angle     The angle around the Z axis at which the robot drives in 
   *                  degrees [-180..180].
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void drivePolar(double magnitude, double angle, double zRotation) {
    drive.drivePolar(magnitude, angle, zRotation);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}