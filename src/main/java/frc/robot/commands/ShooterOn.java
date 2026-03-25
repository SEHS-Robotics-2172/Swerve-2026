// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.Robot;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterOn extends Command {
  /** Creates a new Shoot. */
  double timer;
  Shooter shooter;
  
  double ZTranslation = 0;
  double XTranslation = 0;

  public ShooterOn(Shooter shooter_, double timer_) {
    // Use addRequirements() here to declare subsystem dependencies.
    shooter = shooter_;
    timer = timer_;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer = 5;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    timer -= Robot.kDefaultPeriod;
    ZTranslation = LimelightHelpers.getTargetPose3d_CameraSpace(shooter.limelightName).getZ();
    XTranslation = LimelightHelpers.getCameraPose3d_TargetSpace(shooter.limelightName).getX();
    

    /*System.out.println(LimelightHelpers.getTargetCount("limelight-old"));
    LimelightHelpers.SetRobotOrientation("limelight-new", swerve.gyro.getYaw().getValueAsDouble(), 0, 0, 0, 0, 0);
    */
    shooter.wantedTurretAngle -= (LimelightHelpers.getTX(shooter.limelightName) + (XTranslation * 4)) * 0.1 * Robot.kDefaultPeriod;
    if (ZTranslation <= 1.7){
      shooter.wantedHoodAngle = -0.01;
    }
    else if (ZTranslation > 2 && ZTranslation <= 3.7){
      shooter.wantedHoodAngle = 0.05 * (ZTranslation - 2) / (3.7 - 2);
    }
    shooter.flywheelSpeed = 2000;
    shooter.hood.set(MathUtil.clamp(shooter.wantedHoodSpeed, -0.05, 0.05));
    shooter.flyWheel.setControl(shooter.flywheelPID);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.flywheelSpeed = 0;
    shooter.hood.set(0);
    shooter.flyWheel.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (timer <= 0){
      return true;
    }
    return false;
  }
}
