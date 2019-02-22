package edu.boscotech.robot2019.util;

import edu.boscotech.techlib.commands.SetEncodedMotor;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MacroFactory {
    private MacroFactory() { }

    public static CommandGroup createLiftHeightMacro(LiftSubsystem lift, 
        WristSubsystem wrist, int heightLevel) {
        boolean isCargoMode = ModeTracker.getInstance().getMode() == Mode.kCargo;
        String modeString = isCargoMode ? "cargo" : "hatch";
        double position = Config.getInstance().getDouble("macros", "liftHeight",
            modeString, "level" + heightLevel 
        );

        CommandGroup group = new CommandGroup();
        group.addParallel(new SetEncodedMotor(lift, position));
        if (heightLevel == 3) {
            double angle = Config.getInstance().getDouble("macros", 
                "liftHeight", "topLevelAngle"
            );
            group.addParallel(new SetEncodedMotor(wrist, angle));
        } else {
            if (isCargoMode) {
                group.addParallel(new SetEncodedMotor(wrist, 1.0));
            } else {
                group.addParallel(new SetEncodedMotor(wrist, 0.0));
            }
        }
        return group;
    }
}