package edu.boscotech.robot2019.subsystems;

import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.boscotech.techlib.subsystems.BetterSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;

public class SuctionCupSubsystem extends BetterSubsystem {
    private DoubleSolenoid m_actuator;
    private Servo m_leftValve, m_rightValve;

    public SuctionCupSubsystem() {
        super("Suction Cups", "suctionCups", "SuctionCups");
    }

    public SuctionCupSubsystem(String hrName, String cfgName) {
        super(hrName, cfgName, "SuctionCups");
    }

    @Override
    protected void setup() {
        m_actuator = new DoubleSolenoid(
            getCfgInt("deploy"), getCfgInt("retract")
        );
        m_leftValve = new Servo(getCfgInt("leftServo"));
        m_rightValve = new Servo(getCfgInt("rightServo"));
    }

    public void deploy() {
        m_actuator.set(Value.kForward);
    }

    public void retract() {
        m_actuator.set(Value.kReverse);
    }

    public boolean isDeployed() {
        return m_actuator.get() == Value.kForward;
    }

    public void setDeployed(boolean deploy) {
        m_actuator.set(deploy ? Value.kForward : Value.kReverse);
    }

    public void closeValves() {
        m_leftValve.set(0.2);
        m_rightValve.set(0.9);
    }

    public void openValves() {
        m_leftValve.set(0.38);
        m_rightValve.set(0.7);
    }

    public boolean isValvesOpen() {
        return m_leftValve.get() > 0.25;
    }

    public void setValvesOpen(boolean open) {
        if (open) openValves(); else closeValves();
    }

    @Override
    protected void enterSafeState() {
        m_actuator.set(Value.kOff);
    }

    @Override
    public Command createDefaultTeleopCommand() {
        return null;
    }

    @Override
    public Command createDefaultTestCommand() {
        return new GenericTestCommand(this);
    }
}