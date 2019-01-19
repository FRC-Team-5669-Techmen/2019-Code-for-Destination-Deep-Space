/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.boscotech.robot2019;

import edu.boscotech.frc.subsystems.CameraSubsystem;
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
  private MecanumDriveSubsystem m_mecanumDrive
    = new MecanumDriveSubsystem();
  private LidarSubsystem m_lidar = new LidarSubsystem();
  private CameraSubsystem m_camera = new CameraSubsystem("front");

  public Robot() {
    super();
    m_camera.startCameraStreams();
    useDefaultCommandsFrom(m_mecanumDrive, m_lidar, m_camera);
  }
}
