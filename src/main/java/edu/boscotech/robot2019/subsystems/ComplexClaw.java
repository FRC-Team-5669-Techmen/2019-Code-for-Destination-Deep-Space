package edu.boscotech.robot2019.subsystems;

import edu.boscotech.robot2019.commands.CloseCupValves;
import edu.boscotech.robot2019.commands.OpenCupValves;
import edu.boscotech.robot2019.commands.SpitClaw;
import edu.boscotech.robot2019.commands.StopClawWheels;
import edu.boscotech.robot2019.commands.SuckClaw;
import edu.boscotech.robot2019.commands.ToggleClaw;
import edu.boscotech.robot2019.commands.ToggleCups;
import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.boscotech.techlib.subsystems.BetterSubsystem;
import edu.boscotech.techlib.util.TalonSRXAdapter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;

public class ComplexClaw extends BetterSubsystem {
  private SpeedController m_leftWheels, m_rightWheels;
  private DoubleSolenoid m_clawActuator, m_cupActuator;
  private Servo m_leftCupServo, m_rightCupServo;

  public ComplexClaw() {
    super("Complex Claw", "claw", "ComplexClaw");
  }

  public ComplexClaw(String hrName, String cfgName) {
    super(hrName, cfgName, "ComplexClaw");
  }

  @Override
  protected void setup() {
    m_leftWheels = new TalonSRXAdapter(getCfgInt("leftWheel"));
    m_leftWheels.setInverted(true);
    m_rightWheels = new TalonSRXAdapter(getCfgInt("rightWheel"));
    m_clawActuator = new DoubleSolenoid(
      getCfgInt("expand"), getCfgInt("contract")
    );
    m_cupActuator = new DoubleSolenoid(
      getCfgInt("deployCups"), getCfgInt("retractCups")
    );
    m_leftCupServo = new Servo(getCfgInt("cupServo1"));
    m_rightCupServo = new Servo(getCfgInt("cupServo2"));
  }

  public void suck() {
    m_leftWheels.set(1.0);
    m_rightWheels.set(1.0);
  }

  public void spit() {
    m_leftWheels.set(-1.0);
    m_rightWheels.set(-1.0);
  }

  public void stopWheels() {
    m_leftWheels.stopMotor();
    m_rightWheels.stopMotor();
  }

  public void openClaw() {
    m_clawActuator.set(Value.kForward);
  }

  public void closeClaw() {
    m_clawActuator.set(Value.kReverse);
  }

  public boolean isClawOpen() {
    return m_clawActuator.get() == Value.kForward;
  }

  public void setClawOpen(boolean open) {
    m_clawActuator.set(open ? Value.kForward : Value.kReverse);
  }

  public void deployCups() {
    m_cupActuator.set(Value.kForward);
  }

  public void retractCups() {
    m_cupActuator.set(Value.kReverse);
  }

  public boolean isCupsDeployed() {
    return m_cupActuator.get() == Value.kForward;
  }

  public void setCupsDeployed(boolean deploy) {
    m_cupActuator.set(deploy ? Value.kForward : Value.kReverse);
  }

  public void closeCupValves() {
    m_leftCupServo.set(0.2);
    m_rightCupServo.set(0.8);
  }

  public void openCupValves() {
    m_leftCupServo.set(0.3);
    m_rightCupServo.set(0.7);
  }

  public boolean isCupValvesOpen() {
    return m_leftCupServo.get() > 0.25;
  }

  public void setCupValvesOpen(boolean open) {
    if (open) openCupValves(); else closeCupValves();
  }

  @Override
  protected void enterSafeState() {
    closeCupValves();
    stopWheels();
    closeClaw();
    retractCups();
  }

  @Override
  public Command createDefaultTeleopCommand() {
    getDigitalControl("toggle").whenPressed(new ToggleClaw(this));
    getDigitalControl("spit").whenPressed(new SpitClaw(this));
    getDigitalControl("spit").whenReleased(new StopClawWheels(this));
    getDigitalControl("suck").whenPressed(new SuckClaw(this));
    getDigitalControl("suck").whenReleased(new StopClawWheels(this));
    getDigitalControl("toggleCups").whenPressed(new ToggleCups(this));
    getDigitalControl("releaseCups").whenPressed(new OpenCupValves(this));
    getDigitalControl("releaseCups").whenReleased(new CloseCupValves(this));
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}