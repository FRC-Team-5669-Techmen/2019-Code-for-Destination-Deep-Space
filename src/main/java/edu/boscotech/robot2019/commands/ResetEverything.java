package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ResetEverything extends Command {
    private LiftSubsystem m_lift;
    private WristSubsystem m_wrist;
    private PneumaticClawSubsystem m_claw;
    private SuctionCupSubsystem m_cups;
    private boolean m_ncStopped = false;

    public ResetEverything(LiftSubsystem lift, WristSubsystem wrist, 
        PneumaticClawSubsystem claw, SuctionCupSubsystem cups) {
        requires(lift);
        requires(wrist);
        requires(claw);
        requires(cups);
        m_lift = lift;
        m_wrist = wrist;
        m_claw = claw;
        m_cups = cups;
    }

    @Override
    protected void execute() {
        if (!m_ncStopped) {
            m_lift.stopNetworkTablesControl();
            m_wrist.stopNetworkTablesControl();
            m_claw.stopNetworkTablesControl();
            m_cups.stopNetworkTablesControl();
            m_ncStopped = true;
        }
        m_lift.startNCalibration();
        m_wrist.startNCalibration();
        m_claw.closeClaw();
        m_cups.retract();
        System.out.println("asdf");
    }

    @Override
    protected boolean isFinished() {
        System.out.println("asdf2");
        return m_lift.isNLimitEngaged() && m_wrist.isNLimitEngaged();
    }
}
