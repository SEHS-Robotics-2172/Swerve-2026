// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TrackFuel extends Command {
  /** Creates a new TrackFuel. */
  Intake intake;
  Swerve swerve;
  public TrackFuel(Intake intake_, Swerve swerve_) {
    // Use addRequirements() here to declare subsystem dependencies.
    swerve = swerve_;
    intake = intake_;
    addRequirements(intake);
    addRequirements(swerve);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setSpeed(1, false);
    swerve.drive(
      new Translation2d(/*(-LimelightHelpers.getTY("limelight-balls") * 0.05)*/ - 3, 0),
      -LimelightHelpers.getTX("limelight-balls") * 0.8,
      false,
      false
      );
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setSpeed(0, false);
  }

}
