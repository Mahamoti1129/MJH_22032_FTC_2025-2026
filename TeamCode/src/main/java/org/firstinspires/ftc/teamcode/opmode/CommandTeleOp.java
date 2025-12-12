package org.firstinspires.ftc.teamcode.opmode;

import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.A;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.B;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.LEFT_BUMPER;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.RIGHT_BUMPER;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.X;
import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@TeleOp(name="Competition TeleOp", group="TeleOp")
public class CommandTeleOp extends CommandOpMode {
    private Shooter shooter;
    private Drivetrain drivetrain;
    private Camera camera;

    private GamepadEx driverOp, toolOp;

    @Override
    public void initialize() {
        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        shooter = new Shooter();
        shooter.init(hardwareMap, telemetry);

        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap, driverOp, false);
        drivetrain.follower.startTeleopDrive();

        camera = new Camera();
        camera.init(hardwareMap);

        register(shooter, drivetrain, camera);

        toolOp.getGamepadButton(A).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(1400)));
        toolOp.getGamepadButton(B).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(0)));
        toolOp.getGamepadButton(X).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(shooter.getRequestedVelocity() + 280)));
        toolOp.getGamepadButton(Y).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(shooter.getRequestedVelocity() - 280)));

        driverOp.getGamepadButton(LEFT_BUMPER)
                .whileHeld(new InstantCommand(() -> drivetrain.follower.setMaxPower(0.25)))
                .whenReleased(new InstantCommand(() -> drivetrain.follower.setMaxPower(1)));

        toolOp.getGamepadButton(RIGHT_BUMPER)
                .whenPressed(shooter.fireSequence());

        toolOp.getGamepadButton(LEFT_BUMPER)
                .whileHeld(new InstantCommand(() -> shooter.setLaunchServoPower(-1)))
                .whenReleased(new InstantCommand(() -> shooter.setLaunchServoPower(0)));

        InterpLUT flywheelDistancePowerLookup = new InterpLUT();
        flywheelDistancePowerLookup.add(10, 280);
        flywheelDistancePowerLookup.add(100, 2800);
        flywheelDistancePowerLookup.createLUT();
    }
}
