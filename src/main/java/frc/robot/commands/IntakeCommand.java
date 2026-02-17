package frc.robot.commands;
//Merrick (All)
import frc.robot.subsystems.Intake;
import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommand extends Command{
    private Intake i_Intake;

    private BooleanSupplier IntakeButton;
    
    public IntakeCommand(Intake i_Intake, BooleanSupplier IntakeButton) {
        this.i_Intake = i_Intake;
        addRequirements(i_Intake); //Causes the robot to ****ing explode (not actually)(actually)

        this.IntakeButton = IntakeButton;
    }

    @Override

    public void execute() {
        /* Get Values?, Deadband?*/

        /* Used */
        if (IntakeButton.getAsBoolean()) {
            i_Intake.setSpeed(0.5);
        }
        else {
            i_Intake.setSpeed(0.0);
        }

    }

  @Override
  public void end(boolean interrupted) {
    i_Intake.setSpeed(0);
    //Ruban transparent
  }

}
