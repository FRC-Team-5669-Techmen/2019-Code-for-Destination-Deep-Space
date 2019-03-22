package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.subsystems.ClawWheelsSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class GenericNeutral extends Command {
    private ClawWheelsSubsystem m_wheels;
    private SuctionCupSubsystem m_suction;

    public GenericNeutral(ClawWheelsSubsystem wheels, SuctionCupSubsystem suction) {
        requires(wheels);
        requires(suction);
        m_wheels = wheels;
        m_suction = suction;
    }

    @Override
    protected void execute() {
        m_suction.closeValves();
        m_wheels.stop();
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