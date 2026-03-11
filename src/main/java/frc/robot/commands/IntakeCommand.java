package frc.robot.commands;
//Merrick (All)
import frc.robot.subsystems.Intake;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommand extends Command{
    private Intake i_Intake;

    private DoubleSupplier IntakeButton;
    BooleanSupplier IntakeMode;
    
    public IntakeCommand(Intake i_Intake, DoubleSupplier IntakeButton, BooleanSupplier IntakeMode) {
        this.i_Intake = i_Intake;
        addRequirements(i_Intake); //Causes the robot to ****ing explode (not actually)(actually)

        this.IntakeButton = IntakeButton;
        this.IntakeMode = IntakeMode;
    }

    @Override

    public void execute() {
        /* Get Values?, Deadband?*/

        /* Used */
        i_Intake.setSpeed(IntakeButton.getAsDouble(), IntakeMode.getAsBoolean());

    }

  @Override
  public void end(boolean interrupted) {
    i_Intake.setSpeed(0, IntakeMode.getAsBoolean());
    //Ruban transparent
  }

}
