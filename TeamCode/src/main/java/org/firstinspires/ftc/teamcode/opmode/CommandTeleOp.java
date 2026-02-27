package org.firstinspires.ftc.teamcode.opmode;

import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.A;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.B;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.DPAD_DOWN;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.DPAD_LEFT;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.DPAD_UP;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.LEFT_BUMPER;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.RIGHT_BUMPER;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.X;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.Greg;
import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name="Competition TeleOp", group="TeleOp")
public class CommandTeleOp extends CommandOpMode {
    private GamepadEx driverOp, toolOp;
    private Greg greg;

    @Override
    public void initialize() {
        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        greg = new Greg(hardwareMap, driverOp, toolOp, telemetry, false);

        greg.drivetrain.follower.startTeleopDrive();
        // engage drivetrain slow mode
        driverOp.getGamepadButton(LEFT_BUMPER)
                .whileHeld(new InstantCommand(() -> greg.drivetrain.follower.setMaxPower(0.25)))
                .whenReleased(new InstantCommand(() -> greg.drivetrain.follower.setMaxPower(1)));

        // enter intake mode
        toolOp.getGamepadButton(DPAD_LEFT).whenPressed(greg.intakeMode());
        // enter human player feed mode
        toolOp.getGamepadButton(DPAD_UP).whenPressed(greg.humanFeedMode());
        // enter shooting mode
        toolOp.getGamepadButton(DPAD_DOWN).whenPressed(greg.shootMode());


        toolOp.getGamepadButton(A).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(Shooter.STARTUP)));
        toolOp.getGamepadButton(B).whenPressed(new InstantCommand(() -> greg.shooter.stopFlywheel()));
        toolOp.getGamepadButton(X).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(greg.shooter.getRequestedVelocity() + Shooter.STEP)));
        toolOp.getGamepadButton(Y).whenPressed(new InstantCommand(() -> greg.shooter.setRequestedVelocity(greg.shooter.getRequestedVelocity() - Shooter.STEP)));

        toolOp.getGamepadButton(RIGHT_BUMPER)
                .whenPressed(greg.shooter.fireSequence());
    }
}
