package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class DeployCups extends SingleActionCommand<ComplexClaw> {
  public DeployCups(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().deployCups();
  }
}