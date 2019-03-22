package edu.boscotech.robot2019.commands;

import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.boscotech.techlib.vision.AutoCenterPipeline;
import edu.wpi.first.wpilibj.command.Command;

public class AutoCenter extends Command {
    // If it is trying to align for longer than 2 seconds, something is wrong
    // and it should give up.
    private static final long TIME_LIMIT = 2000;
    // At MAX_DISTANCE and beyond, MAX_POWER will be used.
    // Between MAX_DISTANCE and MIN_DISTANCE, a linear blend of MAX_POWER and
    // MIN_POWER will be used depending on how close to the min it is.
    // At MIN_DISTANCE and less, no power will be applied.
    private static final double
        MAX_POWER = 0.5,
        MAX_DISTANCE = 0.8,
        MIN_POWER = 0.2,
        MIN_DISTANCE = 0.1;
    private MecanumDriveSubsystem m_drive;
    private AutoCenterPipeline m_pipeline;
    private long m_startTime = 0;

    public AutoCenter(MecanumDriveSubsystem drive, AutoCenterPipeline pipeline) {
        requires(drive);
        m_drive = drive;
        m_pipeline = pipeline;
    }

    @Override
    protected void execute() {
        if (m_startTime == 0) m_startTime = System.currentTimeMillis();

        double distance = Math.abs(m_pipeline.getCenterX());
        double power = 0.0;
        if (distance >= MAX_DISTANCE) {
            power = MAX_POWER;
        } else if (distance >= MIN_DISTANCE) {
            // 0.0 - 1.0 range, with 0 being min and 1 being max.
            power = (distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
            // Convert 0 - 1 range to MIN_POWER - MAX_POWER range.
            power = power * (MAX_POWER - MIN_POWER) + MIN_POWER;
        } else {
            power = 0.0;
        }
        // Go left if line is to the left.
        if (m_pipeline.getCenterX() < 0.0) power = -power;
        m_drive.driveCartesian(0.0, power, 0.0);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(m_pipeline.getCenterX()) < MIN_DISTANCE
            || System.currentTimeMillis() - m_startTime > TIME_LIMIT;
    }

    @Override
    protected void end() {
        m_drive.driveCartesian(0.0, 0.0, 0.0);
    }
}