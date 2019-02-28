package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.commands.SingleActionCommand;

public class ToggleCups extends SingleActionCommand<SuctionCupSubsystem> {
  public ToggleCups(SuctionCupSubsystem cups) {
    super(cups);
  }

  @Override
  protected void doAction() {
    getTarget().setDeployed(!getTarget().isDeployed());
  }
}