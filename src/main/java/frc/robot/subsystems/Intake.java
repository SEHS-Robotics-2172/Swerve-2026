// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private TalonFX topIntake;
  private TalonFX bottomIntake;
  private TalonFX topIntake2;
  private TalonFX funnelIntake;
  private TalonFX randomAddedMotor;

  private TalonFXConfiguration topIntakeConfiguration = new TalonFXConfiguration();
  private TalonFXConfiguration bottomIntakeConfiguration = new TalonFXConfiguration();
    private TalonFXConfiguration topIntake2Configuration = new TalonFXConfiguration();
  private TalonFXConfiguration funnelIntakeConfiguration = new TalonFXConfiguration();

  public Intake() {
    topIntake = new TalonFX(IntakeConstants.topIntake);
    bottomIntake = new TalonFX(IntakeConstants.bottomIntake);
    topIntake2 = new TalonFX(IntakeConstants.topIntake2);
    funnelIntake = new TalonFX(IntakeConstants.funnelIntake, "canivore");
    randomAddedMotor = new TalonFX(IntakeConstants.randomAddedMotor);

    topIntakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    bottomIntakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    funnelIntakeConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    topIntake2Configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    //set the idle mode to break

    topIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    bottomIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    funnelIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    topIntake2Configuration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    
    topIntake.getConfigurator().apply(topIntakeConfiguration);
    bottomIntake.getConfigurator().apply(bottomIntakeConfiguration);
    funnelIntake.getConfigurator().apply(funnelIntakeConfiguration);
    randomAddedMotor.getConfigurator().apply(funnelIntakeConfiguration); // Change this if we EVER change the funnel motor
    topIntake2.getConfigurator().apply(topIntake2Configuration);
  }

  public void setSpeed(double intakeSpeed, boolean shooterMode) {//changing name of revspeed later - Merrick
    topIntake.set(intakeSpeed);
    topIntake2.set(intakeSpeed);
    if (shooterMode){
      funnelIntake.set(intakeSpeed);
      bottomIntake.set(intakeSpeed);
      randomAddedMotor.set(intakeSpeed);
    }
    else{
      funnelIntake.set(0);
      bottomIntake.set(0);
      randomAddedMotor.set(0);
    }
  }

  @Override
  public void periodic() {
    
  }
}
