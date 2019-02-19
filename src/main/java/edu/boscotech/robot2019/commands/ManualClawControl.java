package edu.boscotech.robot2019.commands;

import edu.boscotech.robot2019.subsystems.ComplexClaw;
import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class ManualClawControl extends Command {
  private WristSubsystem m_wrist;
  private ComplexClaw m_claw;
  private AnalogControl m_wristControl;
  private JoystickButton m_clawButton, m_cupButton, m_valveButton,
    m_spitButton, m_suckButton;
  boolean m_clawPressed = false, m_cupPressed = false, m_valvePressed = false;

  public ManualClawControl(WristSubsystem wrist, ComplexClaw claw) {
    requires(wrist); 
    requires(claw);
    m_wrist = wrist;
    m_claw = claw;

    m_wristControl = wrist.getAnalogControl("manual");
    m_clawButton = claw.getDigitalControl("toggleClaw");
    m_cupButton = claw.getDigitalControl("toggleCups");
    m_valveButton = claw.getDigitalControl("toggleValves");
    m_spitButton = claw.getDigitalControl("spit");
    m_suckButton = claw.getDigitalControl("suck");
  }

  @Override
  protected void execute() {
    m_wrist.setManualSpeed(m_wristControl.getValue());

    if (m_spitButton.get()) {
      m_claw.spit();
    } else if (m_suckButton.get()) {
      m_claw.suck();
    } else {
      m_claw.stopWheels();
    }

    if (m_clawButton.get()) {
      if (!m_clawPressed) {
        m_claw.setClawOpen(!m_claw.isClawOpen());
      }
      m_clawPressed = true;
    } else {
      m_clawPressed = false;
    }

    if (m_cupButton.get()) {
      if (!m_cupPressed) {
        m_claw.setCupsDeployed(!m_claw.isCupsDeployed());
      }
      m_cupPressed = true;
    } else {
      m_cupPressed = false;
    }

    if (m_valveButton.get()) {
      if (!m_valvePressed) {
        m_claw.setCupValvesOpen(!m_claw.isCupValvesOpen());
      }
      m_valvePressed = true;
    } else {
      m_valvePressed = false;
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}