package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class CloseClaw extends SingleActionCommand<ComplexClaw> {
  public CloseClaw(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().closeClaw();
  }
}