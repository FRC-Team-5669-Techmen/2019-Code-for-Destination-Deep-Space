package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class SetMode extends Command {
    public static final double[] CARGO_HEIGHTS = {
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level0"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level1"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level2"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level3"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "levelC")
    }, HATCH_HEIGHTS = {
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level0"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level1"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level2"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level3")
    };
    public static final double
        LOW_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "low"),
        HIGH_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "high"),
        ROCKET_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "rocket"),
        CARGO_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "cargo");

    private LiftSubsystem m_lift;
    private PneumaticClawSubsystem m_claw;
    private SuctionCupSubsystem m_cups;
    private WristSubsystem m_wrist;
    private double m_wristTarget, m_liftTarget;
    private Mode m_mode;
    private int m_heightLevel;
    private boolean m_setupDone = false;

    public SetMode(LiftSubsystem lift, PneumaticClawSubsystem claw,
        SuctionCupSubsystem cups, WristSubsystem wrist,
        Mode mode, int heightLevel) {
        requires(lift);
        requires(claw);
        requires(cups);
        requires(wrist);
        m_lift = lift;
        m_claw = claw;
        m_cups = cups;
        m_wrist = wrist;
        m_mode = mode;
        m_heightLevel = heightLevel;

        if (mode == Mode.kCargo) {
            m_liftTarget = CARGO_HEIGHTS[heightLevel];
        } else {
            m_liftTarget = HATCH_HEIGHTS[heightLevel];
        }

        if (mode == Mode.kCargo) {
            if (heightLevel == 0) {
                m_wristTarget = LOW_ANGLE;
            } else if (heightLevel == 4) {
                m_wristTarget = CARGO_ANGLE;
            } else {
                m_wristTarget = ROCKET_ANGLE;
            }
        } else {
            m_wristTarget = HIGH_ANGLE;
        }
    }
    
    @Override
    protected void execute() {
        if (!m_setupDone) {
            ModeTracker.getInstance().setMode(m_mode);
            m_setupDone = true;
        }
        m_wrist.setPosition(m_wristTarget);
        m_lift.setPosition(m_liftTarget);
        if (m_mode == Mode.kCargo) {
            m_cups.retract();
        } else {
            if (m_wrist.getPosition() < 0.3) {
                if (m_heightLevel > 0) {
                    m_cups.deploy();
                }
                m_claw.closeClaw();
            }
            if (m_heightLevel == 0) {
                m_cups.retract();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(m_wrist.getPosition() - m_wristTarget) < 0.05
            && Math.abs(m_lift.getPosition() - m_liftTarget) < 1.0;
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