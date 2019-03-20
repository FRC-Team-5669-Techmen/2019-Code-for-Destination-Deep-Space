package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.subsystems.ClawWheelsSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class GenericSpit extends Command {
    private ClawWheelsSubsystem m_wheels;
    private SuctionCupSubsystem m_suction;

    public GenericSpit(ClawWheelsSubsystem wheels, SuctionCupSubsystem suction) {
        requires(wheels);
        requires(suction);
        m_wheels = wheels;
        m_suction = suction;
    }

    @Override
    protected void execute() {
        if (ModeTracker.getInstance().getMode() == Mode.kHatch) {
            m_suction.openValves();
        } else {
            m_wheels.spit();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        m_suction.closeValves();
        m_wheels.stop();
    }

    @Override
    protected void interrupted() {
        m_suction.closeValves();
        m_wheels.stop();
    }
}