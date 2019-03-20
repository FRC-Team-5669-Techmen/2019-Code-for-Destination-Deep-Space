package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class AutoLiftMacro extends Command {
    public static final double[] CARGO_HEIGHTS = {
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level0"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level1"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level2"),
        Config.getInstance().getDouble("macros", "liftHeight", "cargo", "level3")
    }, HATCH_HEIGHTS = {
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level0"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level1"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level2"),
        Config.getInstance().getDouble("macros", "liftHeight", "hatch", "level3")
    };
    private LiftSubsystem m_lift;
    private int m_heightLevel;

    public AutoLiftMacro(LiftSubsystem lift, int heightLevel) {
        requires(lift);
        m_lift = lift;
        m_heightLevel = heightLevel;


    }

    private double getTarget() {
        if (ModeTracker.getInstance().getMode() == Mode.kCargo) {
            return CARGO_HEIGHTS[m_heightLevel];
        } else {
            return HATCH_HEIGHTS[m_heightLevel];
        }
    }

    @Override
    protected void execute() {
        m_lift.setPosition(getTarget());
    }

    @Override
    protected boolean isFinished() {
        // Units measured in inches.
        return Math.abs(m_lift.getPosition() - getTarget()) < 1.0;
    }

    @Override
    protected void end() {
        ModeTracker.getInstance().setLastLiftHeight(m_heightLevel);
    }
}