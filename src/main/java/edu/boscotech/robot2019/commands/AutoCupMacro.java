package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class AutoCupMacro extends Command {
    private int m_heightLevel;
    private SuctionCupSubsystem m_cups;

    public AutoCupMacro(SuctionCupSubsystem cups, int heightLevel) {
        requires(cups);
        m_cups = cups;
        m_heightLevel = heightLevel;


    }

    @Override
    protected void execute() {
        if (ModeTracker.getInstance().getMode() == Mode.kHatch && m_heightLevel > 0) {
            m_cups.deploy();
        } else {
            m_cups.retract();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}