package edu.boscotech.robot2019.commands;

import edu.boscotech.techlib.subsystems.PneumaticRaiserSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ToggleRaiserState extends Command {
    private PneumaticRaiserSubsystem m_raiser;
    private static int s_state = 0;

    public ToggleRaiserState(PneumaticRaiserSubsystem raiser) {
        m_raiser = raiser;
    }

    @Override
    protected void execute() {
        if (s_state == 0) {
            m_raiser.extendAllCylinders();
            s_state = 1;
        } else if (s_state == 1) {
            m_raiser.retractFrontCylinders();
            s_state = 2;
        } else if (s_state == 2) {
            m_raiser.retractBackCylinders();
            s_state = 0;
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}