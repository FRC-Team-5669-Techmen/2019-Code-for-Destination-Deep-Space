/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.boscotech.robot2019;

import edu.boscotech.frc.commands.ManualMecanumDrive;
import edu.boscotech.frc.components.Lifecam;
import edu.boscotech.frc.subsystems.LidarSubsystem;
import edu.boscotech.frc.subsystems.MecanumDriveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends edu.boscotech.frc.Robot {
  public static MecanumDriveSubsystem m_mecanumDrive
    = new MecanumDriveSubsystem();
  public static LidarSubsystem m_lidar = new LidarSubsystem();
  
  private Lifecam camera = new Lifecam("front");

  public Robot() {
    super();
    camera.startCameraStream();
  }

  @Override
  protected void setupTeleopCommands() {
    addTeleopCommand(new ManualMecanumDrive(m_mecanumDrive));
  }
}
