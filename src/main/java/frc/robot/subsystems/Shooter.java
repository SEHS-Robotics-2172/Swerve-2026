// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private TalonFX flyWheel;
  private TalonFX turret;
  private TalonFX hood;
  private CANcoder encoder;
  
  private TalonFXConfiguration flyWheelConfiguration;
  private TalonFXConfiguration turretConfiguration;
  private TalonFXConfiguration hoodConfiguration;
  private CANcoderConfiguration rubanTransparentConfiguration;

  public Shooter() {
    flyWheel = new TalonFX(ShooterConstants.flyWheel);
    turret = new TalonFX(ShooterConstants.turret);
    hood = new TalonFX(ShooterConstants.hood);
    encoder = new CANcoder(ShooterConstants.turretEncoder);

    //ruban transparent

    flyWheelConfiguration = new TalonFXConfiguration();
    turretConfiguration = new TalonFXConfiguration();
    hoodConfiguration = new TalonFXConfiguration();
    rubanTransparentConfiguration = new CANcoderConfiguration();

    flyWheelConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    turretConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    //ruban transparent 🦐🦐🦐🦐🦐
    flyWheel.getConfigurator().apply(flyWheelConfiguration);
    turret.getConfigurator().apply(turretConfiguration);
    hood.getConfigurator().apply(hoodConfiguration);
    
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
