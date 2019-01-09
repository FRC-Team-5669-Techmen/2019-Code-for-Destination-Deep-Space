package edu.boscotech.mecanumbot.commands;

import edu.boscotech.mecanumbot.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private Joystick m_mainStick = new Joystick(0), 
    m_throttle = new Joystick(1);

  public ManualMecanumDrive() {
    requires(Robot.m_mecanumDrive);
  }

  @Override
  protected void execute() {
    Robot.m_mecanumDrive.driveCartesian(
      m_mainStick.getX() * 0.5, 
      m_mainStick.getY() * 0.5,
      m_mainStick.getZ() * 0.3
    );
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}