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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  public TalonFX flyWheel;
  private TalonFX turret;
  public TalonFX hood;
  private CANcoder turretEncoder;
  private CANcoder hoodEncoder;
  
  private TalonFXConfiguration flyWheelConfiguration;
  private TalonFXConfiguration turretConfiguration;
  private TalonFXConfiguration hoodConfiguration;
  private CANcoderConfiguration turretEncoderConfig;
  private CANcoderConfiguration hoodEncoderConfig;
  public double flywheelSpeed = 1850; // 2000
  // @ 2k rpm:
  // 0 Rot = 2.05 m
  // 0.05 Rot = 3.67m;

  public double wantedHoodSpeed = 0;
  private double wantedTurretSpeed = 0;

  public double wantedHoodAngle = 0;
  public double wantedTurretAngle = 2.7;

  private double wantedFlyWheelVoltage;

  public String limelightName = "limelight-hub";

  private PIDController hoodPID;
  private PIDController turretPID;
  public VelocityVoltage flywheelPID;
  public Shooter() {
    flyWheel = new TalonFX(ShooterConstants.flyWheel, "canivore");
    turret = new TalonFX(ShooterConstants.turret, "canivore");
    hood = new TalonFX(ShooterConstants.hood, "canivore");
    turretEncoder = new CANcoder(ShooterConstants.turretEncoder, "canivore");

    hoodPID = new PIDController(6, 0 , 0);
    turretPID = new PIDController(6, 0, 0);
    flywheelPID = new VelocityVoltage(flywheelSpeed / 60);


    hoodEncoder = new CANcoder(Constants.ShooterConstants.hoodEncoder, "canivore");


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

    flyWheelConfiguration.Slot0.kP = 0.8;
    flyWheelConfiguration.Slot0.kV = 0.121;
    

    hoodEncoderConfig.MagnetSensor.MagnetOffset = 0;
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
    wantedTurretAngle = MathUtil.clamp(wantedTurretAngle, 0, 4.65);
    hoodPID.setSetpoint(wantedHoodAngle);
    wantedHoodSpeed = hoodPID.calculate(hoodEncoder.getPosition().getValueAsDouble() + Constants.ShooterConstants.hoodEncoderOffset.getRotations());

    
    turretPID.setSetpoint(wantedTurretAngle);
    wantedTurretSpeed = -turretPID.calculate(turretEncoder.getPosition().getValueAsDouble());
    turret.setVoltage(MathUtil.clamp(wantedTurretSpeed, -1, 1));
    
    flywheelPID = flywheelPID.withVelocity(flywheelSpeed / 60);
    


    SmartDashboard.putNumber("Hood Wanted Speed", wantedHoodSpeed);
    SmartDashboard.putNumber("Turret Wanted Speed", wantedTurretSpeed);

    SmartDashboard.putNumber("Hood Position", hoodEncoder.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Turret Position", turretEncoder.getPosition().getValueAsDouble());

    SmartDashboard.putNumber("Motor RPS", flyWheel.getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("Wanted Voltage", wantedFlyWheelVoltage);
    SmartDashboard.putNumber("Flywheel ", flywheelSpeed / 60);
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
