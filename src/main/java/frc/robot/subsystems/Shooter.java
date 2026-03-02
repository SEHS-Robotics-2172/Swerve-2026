// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private TalonFX flyWheel;
  private TalonFX turret;
  private TalonFX hood;
  private CANcoder rubanEncoder;
  
  private TalonFXConfiguration flyWheelConfiguration;
  private TalonFXConfiguration turretConfiguration;
  private TalonFXConfiguration hoodConfiguration;
  private CANcoderConfiguration rubanEncoderConfig;
  private Rotation2d wantedAngle = Rotation2d.kZero;

  private PIDController hoodPID;
  public Shooter() {
    flyWheel = new TalonFX(ShooterConstants.flyWheel);
    turret = new TalonFX(ShooterConstants.turret);
    hood = new TalonFX(ShooterConstants.hood);
    rubanEncoder = new CANcoder(ShooterConstants.turretEncoder);
    hoodPID = new PIDController(0.1, 0, 0);

    //ruban transparent

    flyWheelConfiguration = new TalonFXConfiguration();
    turretConfiguration = new TalonFXConfiguration();
    hoodConfiguration = new TalonFXConfiguration();
    rubanEncoderConfig = new CANcoderConfiguration();

    flyWheelConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    turretConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    flyWheelConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    turretConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    hoodConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    //ruban transparent 🦐🦐🦐🦐🦐
    flyWheel.getConfigurator().apply(flyWheelConfiguration);
    turret.getConfigurator().apply(turretConfiguration);
    hood.getConfigurator().apply(hoodConfiguration);    
  }

  @Override
  public void periodic() {
    hoodPID.setSetpoint(wantedAngle.getRotations());
    hood.set(hoodPID.calculate(rubanEncoder.getAbsolutePosition().getValueAsDouble()));
    flyWheel.setVoltage(3);
  }

  public void setWantedAngle(Rotation2d angle){
    wantedAngle = angle;
  }
  public Rotation2d getWantedAngle(){
    return wantedAngle;
  }
}
