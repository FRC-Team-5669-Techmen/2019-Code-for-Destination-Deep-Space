package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.robot2019.util.ModeTracker;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class GenericToggleClaw extends Command {
    private PneumaticClawSubsystem m_claw;

    public GenericToggleClaw(PneumaticClawSubsystem claw) {
        m_claw = claw;
    }

    @Override
    protected void execute() {
        System.out.print(ModeTracker.getInstance().getMode());
        System.out.println(Mode.kHatch);
        if (ModeTracker.getInstance().getMode() == Mode.kHatch) {
            m_claw.close();
        } else {
            m_claw.setClawOpen(!m_claw.isClawOpen());
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}