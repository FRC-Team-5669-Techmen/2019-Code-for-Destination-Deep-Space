package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class ToggleCups extends SingleActionCommand<ComplexClaw> {
  public ToggleCups(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().setCupsDeployed(!getTarget().isCupsDeployed());
  }
}