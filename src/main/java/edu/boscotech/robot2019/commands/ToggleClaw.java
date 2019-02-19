package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class ToggleClaw extends SingleActionCommand<ComplexClaw> {
  public ToggleClaw(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().setClawOpen(!getTarget().isClawOpen());
  }
}