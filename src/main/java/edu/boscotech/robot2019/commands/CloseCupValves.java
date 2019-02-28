package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class CloseCupValves extends SingleActionCommand<SuctionCupSubsystem> {
  public CloseCupValves(SuctionCupSubsystem cups) {
    super(cups);
  }

  @Override
  protected void doAction() {
    getTarget().closeValves();
  }
}