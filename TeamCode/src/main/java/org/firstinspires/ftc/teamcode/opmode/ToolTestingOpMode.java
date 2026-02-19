package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Greg;

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

        toolOp.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(new InstantCommand(() -> greg.intake.on()))
                .whenReleased(new InstantCommand(() -> greg.intake.off()));

        toolOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whileHeld(new InstantCommand(() -> greg.topFeed.open()))
                .whenReleased(new InstantCommand(() -> greg.topFeed.close()));



        toolOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileHeld(new InstantCommand(() -> greg.transfer.shoot()))
                .whenReleased(new InstantCommand(() -> greg.transfer.intake()));


    }
}
