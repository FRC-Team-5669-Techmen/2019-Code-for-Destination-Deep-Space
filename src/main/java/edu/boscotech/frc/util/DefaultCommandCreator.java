package edu.boscotech.frc.util;

import edu.wpi.first.wpilibj.command.Command;

public interface DefaultCommandCreator {
  public Command createDefaultTeleopCommand();
  public Command createDefaultTestCommand();
}