// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private TalonFX topIntake;
  private TalonFX bottomIntake;

  private SparkMax GoodRevProduct;

  private TalonFXConfiguration topIntakeConfiguration = new TalonFXConfiguration();
  private TalonFXConfiguration bottomIntakeConfiguration = new TalonFXConfiguration();
  private SparkMaxConfig LowkirkenuallyGoodRevProductConfiguration = new SparkMaxConfig();


  public Intake() {
    topIntake = new TalonFX(IntakeConstants.topIntake);
    bottomIntake = new TalonFX(IntakeConstants.bottomIntake);
    GoodRevProduct = new SparkMax(IntakeConstants.GoodRevProduct, MotorType.kBrushless);

    topIntakeConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    bottomIntakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    //set the idle mode to break

    LowkirkenuallyGoodRevProductConfiguration.inverted(false);

    topIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    bottomIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    
    topIntake.getConfigurator().apply(topIntakeConfiguration);
    bottomIntake.getConfigurator().apply(bottomIntakeConfiguration);
    GoodRevProduct.configure(LowkirkenuallyGoodRevProductConfiguration, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setSpeed(double intakeSpeed, double LowkirkenuallyGoodRevProductSpeed) {//changing name of revspeed later - Merrick
    topIntake.set(intakeSpeed);
    bottomIntake.set(intakeSpeed);
    GoodRevProduct.set(LowkirkenuallyGoodRevProductSpeed);
  }

  @Override
  public void periodic() {
    
  }
}
