/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.boscotech.robot2019;

import edu.boscotech.robot2019.commands.AutoCenter;
import edu.boscotech.robot2019.commands.GenericNeutral;
import edu.boscotech.robot2019.commands.GenericSpit;
import edu.boscotech.robot2019.commands.GenericSuck;
import edu.boscotech.robot2019.commands.GenericToggleClaw;
import edu.boscotech.robot2019.commands.ResetEverything;
import edu.boscotech.robot2019.commands.SetMode;
import edu.boscotech.robot2019.commands.ToggleRaiserState;
import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.Mode;
import edu.boscotech.techlib.commands.ManualMecanumDrive;
import edu.boscotech.techlib.components.StreamedCamera;
import edu.boscotech.techlib.config.Controls;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticRaiserSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.boscotech.techlib.vision.AutoCenterPipeline;
import edu.boscotech.techlib.vision.DummyPipeline;
import edu.boscotech.techlib.vision.LinePipeline;
import edu.boscotech.techlib.subsystems.AuxMotorSubsystem;
import edu.boscotech.techlib.subsystems.ClawWheelsSubsystem;

/**
 * 
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends edu.boscotech.techlib.Robot {
  private MecanumDriveSubsystem m_mecanumDrive
    = new MecanumDriveSubsystem();
  // private AuxMotorSubsystem m_liftWheel 
    // = new AuxMotorSubsystem("Lift Wheel", "liftWheel");
  // private LidarSubsystem m_lidar = new LidarSubsystem();
  // private AutoCenterPipeline m_pipeline = new AutoCenterPipeline();
  private StreamedCamera m_camera = new StreamedCamera("front", new DummyPipeline());
  private WristSubsystem m_wrist = new WristSubsystem();
  private LiftSubsystem m_lift = new LiftSubsystem();
  private ClawWheelsSubsystem m_clawWheels = new ClawWheelsSubsystem();
  private PneumaticClawSubsystem m_claw = new PneumaticClawSubsystem();
  private SuctionCupSubsystem m_cups = new SuctionCupSubsystem();
  private PneumaticRaiserSubsystem m_raiser = new PneumaticRaiserSubsystem();

  public Robot() {
    super();
    // m_camera.startCameraStreams();
    // useDefaultCommandsFrom(m_mecanumDrive, m_wrist, m_lift, 
    //   m_clawWheels, m_claw, m_cups);
    useDefaultCommandsFrom(m_mecanumDrive, m_claw, m_wrist, m_lift, m_clawWheels, m_claw, m_cups);
    Controls.getInstance().getDigitalControl("controls", "spit").whenPressed(
      new GenericSpit(m_clawWheels, m_cups)
    );
    Controls.getInstance().getDigitalControl("controls", "spit").whenReleased(
      new GenericNeutral(m_clawWheels, m_cups)
    );
    Controls.getInstance().getDigitalControl("controls", "suck").whenPressed(
      new GenericSuck(m_clawWheels, m_cups)
    );
    Controls.getInstance().getDigitalControl("controls", "suck").whenReleased(
      new GenericNeutral(m_clawWheels, m_cups)
    );
    Controls.getInstance().getDigitalControl("controls", "toggleClaw").whenPressed(
      new GenericToggleClaw(m_claw)
    );
    Controls.getInstance().getDigitalControl("controls", "toggleRaiser").whenPressed(
      new ToggleRaiserState(m_raiser)
    );
    // Controls.getInstance().getDigitalControl("controls", "autoAlign").whenPressed(
    //   new AutoCenter(m_mecanumDrive, m_pipeline)
    // );
    Controls.getInstance().getDigitalControl("controls", "autoAlign").whenReleased(
      new ManualMecanumDrive(m_mecanumDrive)
    );
    Controls.getInstance().getDigitalControl("controls", "modeH0").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kHatch, 0)
    );
    Controls.getInstance().getDigitalControl("controls", "modeH1").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kHatch, 1)
    );
    Controls.getInstance().getDigitalControl("controls", "modeH2").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kHatch, 2)
    );
    Controls.getInstance().getDigitalControl("controls", "modeH3").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kHatch, 3)
    );
    Controls.getInstance().getDigitalControl("controls", "modeC0").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kCargo, 0)
    );
    Controls.getInstance().getDigitalControl("controls", "modeC1").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kCargo, 1)
    );
    Controls.getInstance().getDigitalControl("controls", "modeC2").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kCargo, 2)
    );
    Controls.getInstance().getDigitalControl("controls", "modeC3").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kCargo, 3)
    );
    Controls.getInstance().getDigitalControl("controls", "modeCC").whenPressed(
      new SetMode(m_lift, m_claw, m_cups, m_wrist, Mode.kCargo, 4)
    );
    Controls.getInstance().getDigitalControl("controls", "resetEverything").whenPressed(
      new ResetEverything(m_lift, m_wrist, m_claw, m_cups)
    );
    m_camera.startCameraStream();
  }

  @Override
  protected void setupTest() {
    System.out.println("owieuotriqwuetoi");
  }
}
