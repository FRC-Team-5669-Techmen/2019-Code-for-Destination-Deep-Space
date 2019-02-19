package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class RetractCups extends SingleActionCommand<ComplexClaw> {
  public RetractCups(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().retractCups();
  }
}