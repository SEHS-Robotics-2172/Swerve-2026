// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.Robot;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DefaultShooter extends Command {
  /** Creates a new DefaultShooter. */
  Shooter shooter;
  BooleanSupplier up;
  BooleanSupplier down;
  Rotation2d wantedHoodAngle = Rotation2d.kZero;
  Pose3d tagPosition;
  DoubleSupplier speed;
  public DefaultShooter(Shooter shooter_, BooleanSupplier up_, BooleanSupplier down_, DoubleSupplier speed_) {
    // Use addRequirements() here to declare subsystem dependencies.
    shooter = shooter_;
    up = up_;
    down = down_;
    speed = speed_;
    addRequirements(shooter);
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Temporary Code for manual control
    // tagPosition = LimelightHelpers.getTargetPose3d_CameraSpace("limelight");
    // shooter.setFlyWheelSpeed(speed.getAsDouble() * 12);
    

    if (up.getAsBoolean()){
      shooter.setWantedHoodAngle(shooter.getWantedHoodAngle().plus(Rotation2d.fromRotations(0.1 * Robot.kDefaultPeriod)));
    }
    if(down.getAsBoolean()){
      shooter.setWantedHoodAngle(shooter.getWantedHoodAngle().minus(Rotation2d.fromRotations(0.1 * Robot.kDefaultPeriod)));
    }
    
  }
}
