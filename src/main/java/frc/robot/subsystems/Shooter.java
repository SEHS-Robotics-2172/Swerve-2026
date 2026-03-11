// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
import com.pathplanner.lib.events.CancelCommandEvent;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private TalonFX flyWheel;
  private TalonFX turret;
  private TalonFX hood;
  private CANcoder turretEncoder;
  private CANcoder hoodEncoder;
  
  private TalonFXConfiguration flyWheelConfiguration;
  private TalonFXConfiguration turretConfiguration;
  private TalonFXConfiguration hoodConfiguration;
  private CANcoderConfiguration turretEncoderConfig;
  private CANcoderConfiguration hoodEncoderConfig;
  private double flywheelSpeed = 1850;
  private double theoreticalDistance;

  // 4 = 94 in
  // 6 = 

  private double wantedHoodSpeed = 0;
  private double wantedTurretSpeed = 0;

  private double wantedHoodAngle = 0;
  public double wantedTurretAngle = 0;

  private double wantedFlyWheelVoltage = 0;

  private PIDController hoodPID;
  private PIDController turretPID;
  private VelocityVoltage flywheelPID;
  public Shooter() {
    flyWheel = new TalonFX(ShooterConstants.flyWheel);
    turret = new TalonFX(ShooterConstants.turret);
    hood = new TalonFX(ShooterConstants.hood);
    turretEncoder = new CANcoder(ShooterConstants.turretEncoder);

    hoodPID = new PIDController(1, 0.05 , 0);
    turretPID = new PIDController(0.5, 0, 0);
    flywheelPID = new VelocityVoltage(flywheelSpeed / 60);


    hoodEncoder = new CANcoder(Constants.ShooterConstants.hoodEncoder);


    //ruban transparent

    flyWheelConfiguration = new TalonFXConfiguration();
    turretConfiguration = new TalonFXConfiguration();
    hoodConfiguration = new TalonFXConfiguration();
    turretEncoderConfig = new CANcoderConfiguration();
    hoodEncoderConfig = new CANcoderConfiguration();

    flyWheelConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    turretConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    flyWheelConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    turretConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    hoodConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    hoodEncoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    turretEncoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;

    flyWheelConfiguration.Slot0.kP = 1.2;
    flyWheelConfiguration.Slot0.kV = 0.133;

    hoodEncoderConfig.MagnetSensor.MagnetOffset = Constants.ShooterConstants.hoodEncoderOffset.getRotations();
    turretEncoderConfig.MagnetSensor.MagnetOffset = Constants.ShooterConstants.turretEncoderOffset.getRotations();

    //ruban transparent 🦐🦐🦐🦐🦐
    flyWheel.getConfigurator().apply(flyWheelConfiguration);
    turret.getConfigurator().apply(turretConfiguration);
    hood.getConfigurator().apply(hoodConfiguration);    

    hoodEncoder.getConfigurator().apply(hoodEncoderConfig);
    turretEncoder.getConfigurator().apply(turretEncoderConfig);
  }

  @Override
  public void periodic() {
    hoodPID.setSetpoint(wantedHoodAngle);
    wantedHoodSpeed = hoodPID.calculate(hoodEncoder.getPosition().getValueAsDouble());
    hood.set(wantedHoodSpeed);
    
    turretPID.setSetpoint(wantedTurretAngle);
    wantedTurretSpeed = -turretPID.calculate(turretEncoder.getPosition().getValueAsDouble());
    turret.set(MathUtil.clamp(wantedTurretSpeed, -0.1, 0.1));
    
    flywheelPID = flywheelPID.withVelocity(flywheelSpeed / 60);
    
    flyWheel.setControl(flywheelPID);

    theoreticalDistance = 
    (((-10 * Math.sin(
      Math.PI - ((hoodEncoder.getPosition().getValueAsDouble()  * 2 * Math.PI) + Math.PI/2)
      )
    )
     - 
    (Math.sqrt(
      Math.pow(
        10 * Math.sin(
          Math.PI - ((hoodEncoder.getPosition().getValueAsDouble()  * 2 * Math.PI) + Math.PI/2)
          ), 2
        )
      - (4 * (4.905) * 1.83)
      )
    ) / -9.81) / 10 * Math.cos((Math.PI - ((hoodEncoder.getPosition().getValueAsDouble()  * 2 * Math.PI) + Math.PI/2)))
    );

    wantedTurretAngle += LimelightHelpers.getTX("limelight-Reuben") * 0.01;

    SmartDashboard.putNumber("Hood Wanted Speed", wantedHoodSpeed);
    SmartDashboard.putNumber("Turret Wanted Speed", wantedTurretSpeed);

    SmartDashboard.putNumber("Hood Position", hoodEncoder.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Turret Position", turretEncoder.getPosition().getValueAsDouble());

    SmartDashboard.putNumber("Motor RPS", flyWheel.getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("Wanted Voltage", wantedFlyWheelVoltage);
    SmartDashboard.putNumber("Flywheel ", flywheelSpeed / 60);
    SmartDashboard.putNumber("Theoretical Distance", theoreticalDistance);

  }

  public void setWantedHoodAngle(Rotation2d angle){
    wantedHoodAngle = angle.getRotations();
  }
  public Rotation2d getWantedHoodAngle(){
    return Rotation2d.fromRotations(wantedHoodAngle);
  }

  public void setTurretSpeed(double speed){
    turret.set(speed);
  }

  public void setHoodSpeed(double speed){
    hood.set(speed);
  }
  public void setFlyWheelSpeed(double speed){
    flywheelSpeed = speed;
  }
  public void setStaticBrake(){

  }
}
