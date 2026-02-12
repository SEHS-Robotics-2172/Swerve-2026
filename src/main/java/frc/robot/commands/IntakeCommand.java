package frc.robot.commands;
//Merrick (All)
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommand extends Command{
    private Intake i_Intake;

    private BooleanSupplier xPressed;
    
    public IntakeCommand(Intake i_Intake, BooleanSupplier xPressed) {
        this.i_Intake = i_Intake;
        addRequirements(i_Intake); //Causes the robot to ****ing explode (not actually)

        this.xPressed = xPressed;
    }

    @Override

    public void execute() {
        /* Get Values?, Deadband?*/

        /* Used */
        if (xPressed.getAsBoolean()) {
            i_Intake.setSpeed(
                1.0,
                1.0
            );
        }
        else {
            i_Intake.setSpeed(
                0.0,
                0.0
            );
        }

    }

}
