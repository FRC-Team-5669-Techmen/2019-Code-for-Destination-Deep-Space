package edu.boscotech.robot2019.util;

import edu.boscotech.robot2019.commands.AutoCupMacro;
import edu.boscotech.robot2019.commands.AutoLiftMacro;
import edu.boscotech.robot2019.commands.AutoWristMacro;
import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MacroFactory {
    private MacroFactory() { }

    public static CommandGroup createLiftHeightMacro(LiftSubsystem lift, 
        WristSubsystem wrist, SuctionCupSubsystem cups, int heightLevel) {
        CommandGroup group = new CommandGroup();
        group.addParallel(new AutoLiftMacro(lift, heightLevel));
        group.addParallel(new AutoWristMacro(wrist, heightLevel));
        group.addParallel(new AutoCupMacro(cups, heightLevel));
        return group;
    }
}