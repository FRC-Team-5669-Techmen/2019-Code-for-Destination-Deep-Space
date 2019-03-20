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

public class ToggleMode extends Command {
    private LiftSubsystem m_lift;
    private PneumaticClawSubsystem m_claw;
    private ClawWheelsSubsystem m_wheels;
    private SuctionCupSubsystem m_cups;
    private WristSubsystem m_wrist;
    private boolean m_setupDone = false;

    public ToggleMode(LiftSubsystem lift, PneumaticClawSubsystem claw,
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
        if (!m_setupDone) {
            if (ModeTracker.getInstance().getMode() == Mode.kCargo) {
                ModeTracker.getInstance().setMode(Mode.kHatch);
            } else {
                ModeTracker.getInstance().setMode(Mode.kCargo);
            }
            m_setupDone = true;
        }
        boolean cargoMode = ModeTracker.getInstance().getMode() == Mode.kCargo;
        int lastLiftHeight = ModeTracker.getInstance().getLastLiftHeight();
        if (cargoMode) {
            m_cups.retract();
            if (lastLiftHeight == 0) {
                m_wrist.setPosition(AutoWristMacro.LOW_ANGLE);
            } else {
                m_wrist.setPosition(AutoWristMacro.CARGO_ANGLE);
            }
        } else {
            m_wrist.setPosition(AutoWristMacro.HIGH_ANGLE);
            if (m_wrist.getPosition() < 0.3) {
                if (lastLiftHeight > 0) {
                    m_cups.deploy();
                }
                m_claw.close();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        boolean cargoMode = ModeTracker.getInstance().getMode() == Mode.kCargo;
        int lastLiftHeight = ModeTracker.getInstance().getLastLiftHeight();
        double wristTarget;
        if (cargoMode) {
            if (lastLiftHeight == 0) {
                wristTarget = AutoWristMacro.LOW_ANGLE;
            } else {
                wristTarget = AutoWristMacro.CARGO_ANGLE;
            }
        } else {
            wristTarget = AutoWristMacro.HIGH_ANGLE;
        }

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