package edu.boscotech.frc;

public class Config {
  //////////////////////////////////////////////////////////////////////////////
  // PARAMETERS
  //////////////////////////////////////////////////////////////////////////////

  // Maximum speed along X axis.
  public static final double MECANUM_X_SPEED = 1.0;
  // Maximum speed along Y axis.
  public static final double MECANUM_Y_SPEED = 1.0; 
  // Maximum speed when rotating.
  public static final double MECANUM_R_SPEED = 0.6; 
  // Slope of throttle curve.
  public static final double MECANUM_SPEED_THROTTLE_CURVE = 2.0;
  // Slope of rotational throttle curve.
  public static final double MECANUM_ROTATE_THROTTLE_CURVE = 2.0;

  //////////////////////////////////////////////////////////////////////////////
  // CONTROLS
  //////////////////////////////////////////////////////////////////////////////

  // Axis that will be used for X movement.
  public static final int MECANUM_X_INPUT = 0;
  // Axis that will be used for Y movement.
  public static final int MECANUM_Y_INPUT = 1;
  // Axis that will be used for rotation.
  public static final int MECANUM_R_INPUT = 2;
  // Axis that will be used for speed throttle.
  public static final int MECANUM_SPEED_THROTTLE_INPUT = 0;
  // Axis that will be used for rotational throttle.
  public static final int MECANUM_ROTATE_THROTTLE_INPUT = 1;
}