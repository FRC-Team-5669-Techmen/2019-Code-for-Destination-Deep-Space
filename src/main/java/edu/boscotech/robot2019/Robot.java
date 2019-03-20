/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.boscotech.robot2019;

import edu.boscotech.robot2019.commands.CargoMode;
import edu.boscotech.robot2019.commands.GenericNeutral;
import edu.boscotech.robot2019.commands.GenericSpit;
import edu.boscotech.robot2019.commands.GenericSuck;
import edu.boscotech.robot2019.commands.GenericToggleClaw;
import edu.boscotech.robot2019.commands.HatchMode;
import edu.boscotech.robot2019.commands.ResetEverything;
import edu.boscotech.robot2019.commands.ToggleMode;
import edu.boscotech.robot2019.subsystems.SuctionCupSubsystem;
import edu.boscotech.robot2019.util.MacroFactory;
import edu.boscotech.techlib.config.Controls;
import edu.boscotech.techlib.subsystems.CameraSubsystem;
import edu.boscotech.techlib.subsystems.LidarSubsystem;
import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.boscotech.techlib.subsystems.WristSubsystem;
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
  // private LidarSubsystem m_lidar = new LidarSubsystem();
  private CameraSubsystem m_camera = new CameraSubsystem("front");
  private WristSubsystem m_wrist = new WristSubsystem();
  private LiftSubsystem m_lift = new LiftSubsystem();
  private ClawWheelsSubsystem m_clawWheels = new ClawWheelsSubsystem();
  private PneumaticClawSubsystem m_claw = new PneumaticClawSubsystem();
  private SuctionCupSubsystem m_cups = new SuctionCupSubsystem();

  public Robot() {
    super();
    // m_camera.startCameraStreams();
    useDefaultCommandsFrom(m_mecanumDrive, m_wrist, m_lift, m_clawWheels, m_claw, m_cups);
    Controls.getInstance().getDigitalControl("controls", "hatchMode").whenPressed(
      new HatchMode(m_lift, m_claw, m_clawWheels, m_cups, m_wrist)
    );
    Controls.getInstance().getDigitalControl("controls", "cargoMode").whenPressed(
      new CargoMode(m_lift, m_claw, m_clawWheels, m_cups, m_wrist)
    );
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
    Controls.getInstance().getDigitalControl("controls", "lift0").whenPressed(
      MacroFactory.createLiftHeightMacro(m_lift, m_wrist, m_cups, 0)
    );
    Controls.getInstance().getDigitalControl("controls", "lift1").whenPressed(
      MacroFactory.createLiftHeightMacro(m_lift, m_wrist, m_cups, 1)
    );
    Controls.getInstance().getDigitalControl("controls", "lift2").whenPressed(
      MacroFactory.createLiftHeightMacro(m_lift, m_wrist, m_cups, 2)
    );
    Controls.getInstance().getDigitalControl("controls", "lift3").whenPressed(
      MacroFactory.createLiftHeightMacro(m_lift, m_wrist, m_cups, 3)
    );
    Controls.getInstance().getDigitalControl("controls", "resetEverything").whenPressed(
      new ResetEverything(m_lift, m_wrist, m_claw, m_cups)
    );
    m_camera.startCameraStreams();
  }

  @Override
  protected void setupTest() {
    System.out.println("owieuotriqwuetoi");
  }
}
