// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HoardShooter extends Command {
  /** Creates a new DefaultShooter. */
  Shooter shooter;

  double ZTranslation = 0;
  double XTranslation = 0;
  RobotContainer container;
  public HoardShooter(Shooter shooter_, RobotContainer container_) {
    // Use addRequirements() here to declare subsystem dependencies.
    shooter = shooter_;
    container = container_;
  }

  @Override
  public void initialize(){
    if (container.defaultShooter != null)
      container.defaultShooter.cancel();
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.wantedTurretAngle = 2.7;
    shooter.wantedHoodAngle = 0.1;
    shooter.flywheelSpeed = 4000;
    
    shooter.flyWheel.setControl(shooter.flywheelPID);
  }
  @Override
  public void end(boolean interupted){
    shooter.flywheelSpeed = 0;
    shooter.hood.set(0);
    shooter.flyWheel.set(0);
    shooter.wantedHoodAngle = 0;
  }
}
