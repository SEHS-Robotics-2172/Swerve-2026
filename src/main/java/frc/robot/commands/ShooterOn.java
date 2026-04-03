// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.LimelightHelpers;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterOn extends Command {
  /** Creates a new Shoot. */
  double timer;
  RobotContainer container;
  
  double ZTranslation = 0;
  double XTranslation = 0;

  public ShooterOn(RobotContainer container_, double timer_) {
    // Use addRequirements() here to declare subsystem dependencies.
    container = container_;
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
    CommandScheduler.getInstance().schedule(container.testShooter);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
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
