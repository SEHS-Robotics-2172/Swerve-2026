// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterTest extends Command {
  Shooter shooter;
  Swerve swerve;
  RobotContainer container;
  Pose3d robotPose3d;
  Rotation2d turretRotation;
  Rotation2d robotRotation;
  double wantedTurretAngle;
  double ZTranslation;
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
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ZTranslation = LimelightHelpers.getTargetPose3d_CameraSpace(shooter.limelightName).getZ();

    robotPose3d = LimelightHelpers.getBotPose3d_TargetSpace("limelight-hub").plus(new Transform3d(0, 0, -0.595, Rotation3d.kZero));
    robotRotation = swerve.getGyroYaw();
    turretRotation = shooter.getCurrentTurretAngle().div(10).plus(robotRotation);
    wantedTurretAngle = Math.atan2(-robotPose3d.getZ() , -robotPose3d.getX()) / (2 * Math.PI) - robotRotation.getRotations();
    if (LimelightHelpers.getBotPose3d_TargetSpace("limelight-hub") != Pose3d.kZero)
      shooter.setWantedTurretAngle(Math.abs(wantedTurretAngle));
    else{
      shooter.setWantedTurretAngle(wantedTurretAngle);
    }


    if (ZTranslation <= 1.7){
      shooter.wantedHoodAngle = -0.01;
    }
    else if (ZTranslation > 1.7 && ZTranslation <= 3.7){
      shooter.wantedHoodAngle = 0.05 * (ZTranslation - 2) / (3.7 - 2);
    }
    shooter.flywheelSpeed = 0; // 2000;
    shooter.flyWheel.setControl(shooter.flywheelPID);

    SmartDashboard.putNumber("Gyro Rotation", swerve.getGyroYaw().getRotations());
    SmartDashboard.putNumber("Test Turret Wanted Angle", wantedTurretAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.flywheelSpeed = 0;
    shooter.flyWheel.set(0);
    shooter.wantedHoodAngle = 0;

  }
}
