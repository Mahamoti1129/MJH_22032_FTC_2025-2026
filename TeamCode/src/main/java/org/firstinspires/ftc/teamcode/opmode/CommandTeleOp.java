package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

import static com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button.*;

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
        shooter.init(hardwareMap);

        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap, driverOp);
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
                .whileHeld(new InstantCommand(() -> shooter.setLaunchServoPower(1)))
                .whenReleased(new InstantCommand(() -> shooter.setLaunchServoPower(0)));
    }
}
