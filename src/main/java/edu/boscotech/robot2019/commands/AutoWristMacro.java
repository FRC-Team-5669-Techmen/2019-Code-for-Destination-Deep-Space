package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class AutoWristMacro extends Command {
    public static final double
        LOW_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "low"),
        HIGH_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "high"),
        CARGO_ANGLE = Config.getInstance().getDouble("macros", "wristAngles", "cargo");
    private WristSubsystem m_wrist;
    private double m_angle;
    private int m_heightLevel;

    public AutoWristMacro(WristSubsystem wrist, int heightLevel) {
        requires(wrist);
        m_wrist = wrist;
        m_heightLevel = heightLevel;


    }

    private double getTarget() {
        if (ModeTracker.getInstance().getMode() == Mode.kCargo) {
            if (m_heightLevel == 0) {
                return LOW_ANGLE;
            } else {
                return CARGO_ANGLE;
            }
        } else {
            return HIGH_ANGLE;
        }
    }

    @Override
    protected void execute() {
        m_wrist.setPosition(getTarget());
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(m_wrist.getPosition() - getTarget()) < 0.05;
    }
}