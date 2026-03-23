// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterTest extends Command {
  Shooter shooter;
  Swerve swerve;
  RobotContainer container;
  Pose2d robotPose;
  Pose2d hubPose;
  Transform2d difference;
  Rotation2d turretRotation;
  Rotation2d robotRotation;
  double wantedTurretAngle;
  /** Creates a new ShooterTest. */
  public ShooterTest(Shooter shooter_ ,Swerve swerve_, RobotContainer container_) {
    // Use addRequirements() here to declare subsystem dependencies.
    shooter = shooter_;
    swerve = swerve_;
    container = container_;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (container.defaultShooter != null){
      container.defaultShooter.cancel();
    }
    if (container.hoardShooter != null){
      container.hoardShooter.cancel();
    }
    if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
      hubPose = new Pose2d(4.595, 4.030, Rotation2d.kZero);
    else
      hubPose = new Pose2d(11.900, 4.030, Rotation2d.kZero);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    robotPose = swerve.poseEstimator.getEstimatedPosition();
    difference = robotPose.minus(hubPose);
    robotRotation = swerve.getGyroYaw();

    wantedTurretAngle = (Math.atan2(difference.getY(), difference.getX()) / (2 * Math.PI)) + robotRotation.getRotations() + 0.5;

    shooter.setWantedTurretAngle(wantedTurretAngle);

    // Hood if we have one

    shooter.flywheelSpeed = 2000;
    shooter.flyWheel.setControl(shooter.flywheelPID);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.flywheelSpeed = 0;
    shooter.flyWheel.set(0);
    shooter.wantedHoodAngle = 0;
    shooter.setWantedTurretAngle(0);

  }
}
