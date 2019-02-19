package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class CloseCupValves extends SingleActionCommand<ComplexClaw> {
  public CloseCupValves(ComplexClaw claw) {
    super(claw);
  }

  @Override
  protected void doAction() {
    getTarget().closeCupValves();
  }
}