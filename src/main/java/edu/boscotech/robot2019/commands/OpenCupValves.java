package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class OpenCupValves extends SingleActionCommand<SuctionCupSubsystem> {
  public OpenCupValves(SuctionCupSubsystem cups) {
    super(cups);
  }

  @Override
  protected void doAction() {
    getTarget().openValves();
  }
}