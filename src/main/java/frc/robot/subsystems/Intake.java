// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private PIDController pid;
  private double intakeVoltage;
  private double intakeRollerSpeed;

  private TalonFX topIntake;
  private TalonFX topIntake2;
  private TalonFX bottomIntake;
  private TalonFX fastIntake;
  private TalonFX funnelIntake;

  private CANcoder intakeEncoder;

  Rotation2d intakePosition;
  double intakeSpeed;

  private TalonFXConfiguration topIntakeConfiguration = new TalonFXConfiguration();
  private TalonFXConfiguration bottomIntakeConfiguration = new TalonFXConfiguration();
  private TalonFXConfiguration topIntake2Configuration = new TalonFXConfiguration();
  private TalonFXConfiguration funnelIntakeConfiguration = new TalonFXConfiguration();

  public Intake() {
    topIntake = new TalonFX(IntakeConstants.topIntake);
    bottomIntake = new TalonFX(IntakeConstants.bottomIntake, "canivore");
    topIntake2 = new TalonFX(IntakeConstants.topIntake2);
    funnelIntake = new TalonFX(IntakeConstants.funnelIntake, "canivore");
    fastIntake = new TalonFX(IntakeConstants.fastIntake);

    intakeEncoder = new CANcoder(IntakeConstants.intakeEncoder);

    pid = new PIDController(15, 0, 0.01);

    topIntakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    bottomIntakeConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    funnelIntakeConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    topIntake2Configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    //set the idle mode to break

    topIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    bottomIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    funnelIntakeConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    topIntake2Configuration.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    topIntakeConfiguration.Slot0.kP = 0;
    topIntake2Configuration.Slot0.kP = 0;
    
    topIntake.getConfigurator().apply(topIntakeConfiguration);
    bottomIntake.getConfigurator().apply(bottomIntakeConfiguration);
    // funnelIntake.getConfigurator().apply(funnelIntakeConfiguration);
    fastIntake.getConfigurator().apply(funnelIntakeConfiguration); // Change this if we EVER change the funnel motor
    topIntake2.getConfigurator().apply(topIntake2Configuration);
  }

  public void setSpeed(double intakeSpeed, double intakeRollerSpeed) {//changing name of revspeed later - Merrick
    this.intakeSpeed = intakeSpeed; 
    this.intakeRollerSpeed = intakeRollerSpeed;
  }
  public void setPosition(Rotation2d position){
    pid.setSetpoint(MathUtil.clamp(position.getRotations(), 0, 0.3));
  }

  @Override

  public void periodic() {
    intakeVoltage = MathUtil.clamp(pid.calculate(intakeEncoder.getAbsolutePosition().getValueAsDouble()+0.23), -2, 2);
    SmartDashboard.putNumber("Intake Voltage", intakeVoltage);
    topIntake.set(intakeVoltage / 12);
    topIntake2.set(intakeVoltage / 12);
    funnelIntake.set(0);

    if (intakeEncoder.getAbsolutePosition().getValueAsDouble() <= -0.12)
      fastIntake.set(-1);
    else
      fastIntake.set(0);
    if (bottomIntake.get() != 0)
      funnelIntake.set(1);

    bottomIntake.set(intakeSpeed);
  }
  
}
