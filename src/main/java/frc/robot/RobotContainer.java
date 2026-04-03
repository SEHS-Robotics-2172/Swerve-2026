package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private final SendableChooser<Command> autoChooser;
    /* Controllers */
    public final XboxController driver = new XboxController(1);
    public final XboxController co_driver = new XboxController(2);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final Trigger zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final Trigger robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);

    private final Trigger shooterToggle = new JoystickButton(driver, XboxController.Button.kX.value);
    private final Trigger hoardToggle = new JoystickButton(driver, XboxController.Button.kA.value);
    private final Trigger testToggle = new JoystickButton(driver, XboxController.Button.kB.value);

    private final Trigger AutoIntakeTrigger = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
    
    private Trigger intakeUp = new Trigger(()->driver.getPOV()==0);
    private Trigger intakeDown = new Trigger(()->driver.getPOV()==180);
    
    
    
    /* Co-Driver Buttons */
    
    
    /* Subsystems */
    public final Swerve s_Swerve = new Swerve();
    public final Intake i_Intake = new Intake(); 
    public final Shooter shooter = new Shooter();
    
    private final Command AutoIntake = new TrackFuel(i_Intake, s_Swerve);
    public final Command defaultShooter =  new DefaultShooter(shooter, this);
    public final Command hoardShooter = new HoardShooter(shooter, this);
    public final Command testShooter = new ShooterTest(shooter, s_Swerve, this);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        NamedCommands.registerCommand("IntakeOn", new IntakeOn(i_Intake));
        NamedCommands.registerCommand("IntakeShootOn", new IntakeOn(i_Intake));
        NamedCommands.registerCommand("IntakeShootOff", new IntakeOff(i_Intake));
        NamedCommands.registerCommand("IntakeOff", new IntakeOff(i_Intake));

        NamedCommands.registerCommand("Shoot", new Shoot(i_Intake, this));
        NamedCommands.registerCommand("Delay", new Delay(3));
        NamedCommands.registerCommand("ResetGyro", new InstantCommand(() -> s_Swerve.zeroHeading()));
        
        

        autoChooser = AutoBuilder.buildAutoChooser();

        SmartDashboard.putData("Auto Chooser", autoChooser);

        s_Swerve.setDefaultCommand(
              new TeleopSwerve(
                  s_Swerve, 
                  () -> -driver.getRawAxis(translationAxis), 
                  () -> -driver.getRawAxis(strafeAxis), 
                  () -> -driver.getRawAxis(rotationAxis), 
                  () -> robotCentric.getAsBoolean()
              )
          );
        i_Intake.setDefaultCommand(new IntakeCommand(i_Intake, () -> driver.getRightTriggerAxis()-driver.getLeftTriggerAxis())); // -driver.getLeftTriggerAxis()
        // shooter.setDefaultCommand(new DefaultShooter(shooter, () -> driver.getPOV() == 0, () -> driver.getPOV() == 180, driver::getLeftTriggerAxis));

        //Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        
        AutoIntakeTrigger.toggleOnTrue(AutoIntake);

        shooterToggle.toggleOnTrue(defaultShooter);
        hoardToggle.toggleOnTrue(hoardShooter);
        testToggle.toggleOnTrue(testShooter);
        intakeUp.onTrue(new InstantCommand(()->i_Intake.setPosition(Rotation2d.fromRotations(0.24))));
        intakeDown.onTrue(new InstantCommand(()->i_Intake.setPosition(Rotation2d.fromRotations(0))));

        new Trigger(()->driver.getPOV() == 90).onTrue(new InstantCommand(()->shooter.wantedHoodAngle += (0.01)));

        new Trigger(()->driver.getPOV() == 270).onTrue(new InstantCommand(()->shooter.wantedHoodAngle -= (0.01)));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
