package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.*;

import org.firstinspires.ftc.teamcode.Greg;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@TeleOp(name="Tool Testing")
public class ToolTestingOpMode extends CommandOpMode {

    private Greg greg;
    private GamepadEx driverOp, toolOp;


    @Override
    public void initialize() {
        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        greg = new Greg(hardwareMap, driverOp, toolOp, telemetry, false);
//        greg.drivetrain.follower.startTeleopDrive();

/*
        toolOp.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(new InstantCommand(() -> greg.intake.on()))
                .whenReleased(new InstantCommand(() -> greg.intake.off()));

        toolOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whileHeld(new InstantCommand(() -> greg.topFeed.open()))
                .whenReleased(new InstantCommand(() -> greg.topFeed.close()));


        toolOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileHeld(new InstantCommand(() -> greg.transfer.shoot()))
                .whenReleased(new InstantCommand(() -> greg.transfer.intake()));
*/

        toolOp.getGamepadButton(DPAD_LEFT)
                .whenPressed(greg.intakeMode());

        toolOp.getGamepadButton(DPAD_UP)
                .whenPressed(greg.humanFeedMode());

        toolOp.getGamepadButton(DPAD_RIGHT)
                .whenPressed(greg.shootMode());

        toolOp.getGamepadButton(RIGHT_BUMPER)
                .whenPressed(greg.fireSequence());

        toolOp.getGamepadButton(A).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(Shooter.STARTUP)));
        toolOp.getGamepadButton(B).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(0)));
        toolOp.getGamepadButton(X).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(greg.shooter.getRequestedVelocity() + Shooter.STEP)));
        toolOp.getGamepadButton(Y).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(greg.shooter.getRequestedVelocity() - Shooter.STEP)));
    }
}
