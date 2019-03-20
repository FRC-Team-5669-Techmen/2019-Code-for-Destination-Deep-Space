package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.MacroFactory;
import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.subsystems.ClawWheelsSubsystem;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class HatchMode extends Command {
    private LiftSubsystem m_lift;
    private PneumaticClawSubsystem m_claw;
    private ClawWheelsSubsystem m_wheels;
    private SuctionCupSubsystem m_cups;
    private WristSubsystem m_wrist;
    private boolean m_setupDone = false;

    public HatchMode(LiftSubsystem lift, PneumaticClawSubsystem claw,
        ClawWheelsSubsystem wheels, SuctionCupSubsystem cups, WristSubsystem wrist) {
        requires(lift);
        requires(claw);
        requires(wheels);
        requires(cups);
        requires(wrist);
        m_lift = lift;
        m_claw = claw;
        m_wheels = wheels;
        m_cups = cups;
        m_wrist = wrist;
    }
    
    @Override
    protected void execute() {
        ModeTracker.getInstance().setMode(Mode.kHatch);
        boolean cargoMode = ModeTracker.getInstance().getMode() == Mode.kCargo;
        int lastLiftHeight = ModeTracker.getInstance().getLastLiftHeight();
        m_wrist.setPosition(AutoWristMacro.HIGH_ANGLE);
        m_lift.setPosition(AutoLiftMacro.HATCH_HEIGHTS[lastLiftHeight]);
        if (m_wrist.getPosition() < 0.3) {
            if (lastLiftHeight > 0) {
                m_cups.deploy();
            }
            m_claw.closeClaw();
        }
    }

    @Override
    protected boolean isFinished() {
        boolean cargoMode = ModeTracker.getInstance().getMode() == Mode.kCargo;
        int lastLiftHeight = ModeTracker.getInstance().getLastLiftHeight();
        double wristTarget;
        wristTarget = AutoWristMacro.HIGH_ANGLE;

        return Math.abs(m_wrist.getPosition() - wristTarget) < 0.1;
    }

    @Override
    protected void end() {
        m_setupDone = false;
    }

    @Override
    protected void interrupted() {
        m_setupDone = false;
    }
}