package edu.boscotech.mecanumbot.commands;

import edu.boscotech.mecanumbot.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private Joystick m_mainStick = new Joystick(0), 
    m_throttle = new Joystick(1);
  private double m_xThrottle = 1.0, m_yThrottle = 1.0, m_rThrottle = 0.6;
  private double m_throttleCurve = 2.0;

  public ManualMecanumDrive() {
    requires(Robot.m_mecanumDrive);
  }

  /**
   * Sets the maximum speed the robot can move in the X and Y directions.
   * @param xMax Maximum speed the robot should be capable of in the X
   *             direction, in the range 0.0 - 1.0. (Default is 1.0)
   * @param yMax Maximum speed the robot should be capable of in the Y
   *             direction, in the range 0.0 - 1.0. (Default is 1.0)
   */
  public void setMaxSpeedXY(double xMax, double yMax) {
    m_xThrottle = xMax;
    m_yThrottle = yMax;
  }

  /**
   * Sets the maximum speed the robot can drive the wheels at to rotate around
   * the Z axis.
   * @param rMax Maximum speed the robot can apply to its wheels to rotate, in
   *             the range 0.0 - 1.0. (Default is 0.6)
   */
  public void setMaxSpeedR(double rMax) {
    m_rThrottle = rMax;
  }

  /**
   * Sets the steepness of the curve used to translate the throttle's position
   * into a value to limit the robot's speed by. For example, a slope of 1 will
   * give a direct, 1:1 correlation between the throttle and the code. If the
   * throttle is at 50%, only 50% power will be given to the motors. If the
   * slope is 2.0, then input values will be squared, so a 50% throttle will
   * only give 25% power. This allows for more precision in the lower areas of
   * the throttle while still allowing for the full speed of the robot to be
   * utilized.
   * @param curve The exponent that should be used to translate throttle
   *              position into motor power. (Default is 2.0)
   */
  public void setThrottleCurve(double curve) {
    m_throttleCurve = curve;
  }

  @Override
  protected void execute() {
    double manualThrottle = Math.pow(m_throttle.getRawAxis(0), m_throttleCurve);
    Robot.m_mecanumDrive.driveCartesian(
      m_mainStick.getX() * m_xThrottle * manualThrottle, 
      m_mainStick.getY() * m_yThrottle * manualThrottle,
      m_mainStick.getZ() * m_rThrottle
    );
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}