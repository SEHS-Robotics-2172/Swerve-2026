// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  Pose2d difference;
  Rotation2d turretRotation;
  Rotation2d robotRotation;
  double wantedTurretAngle;

  double distance;
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
    robotRotation = robotPose.getRotation();
    robotPose = new Pose2d(robotPose.getX(), robotPose.getY(), Rotation2d.kZero);

    difference = new Pose2d(hubPose.getX() - robotPose.getX(), hubPose.getY() - robotPose.getY(), Rotation2d.kZero);

    distance = Math.sqrt((difference.getX() * difference.getX()) + (difference.getY() * difference.getY()));

    // Blue Top
    // if (robotPose.getY() >= hubPose.getY())
    //   wantedTurretAngle = ((Math.atan(difference.getX() / difference.getY())) / (2 * Math.PI));

    // Blue Bottom 
    // if (robotPose.getY() < hubPose.getY())
      wantedTurretAngle = ((Math.atan(difference.getY() / difference.getX())) / (2 * Math.PI)) + 0.25;

    SmartDashboard.putNumber("Turret Angle Pre-Gyro", wantedTurretAngle);
    wantedTurretAngle -= swerve.getGyroYaw().getRotations();
    // SmartDashboard.putNumber("Turret Angle Post-Gyro", wantedTurretAngle);
    
    
    shooter.setWantedTurretAngle(wantedTurretAngle);

    // Hood if we have one

    if (distance <= 3.8)
      shooter.flywheelSpeed = -(distance*300 + 2000);
    else
      shooter.flywheelSpeed = -(distance*350 + 1810);

    shooter.flyWheel.setControl(shooter.flywheelPID);

    SmartDashboard.putNumber("Test Turret Wanted Angle", wantedTurretAngle);
    SmartDashboard.putNumber("Arc Tangent", Math.atan2(difference.getY(), difference.getX()) / (2 * Math.PI));
    SmartDashboard.putNumber("Test Gyro Angle Rotations", robotRotation.getRotations());
    // shooter.setWantedHoodAngle(Rotation2d.fromRotations(-0.2));
    SmartDashboard.putNumber("Distance from Hub", distance);

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
