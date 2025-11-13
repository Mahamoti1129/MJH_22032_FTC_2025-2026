package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@TeleOp(name="CommandOpMode Test", group="TeleOp")
public class CommandTeleOp extends CommandOpMode {
    private Shooter shooter;
    private Drivetrain drivetrain;

    private GamepadEx driverOp, toolOp;

    @Override
    public void initialize() {
        shooter = new Shooter();
        shooter.init(hardwareMap);
        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap, driverOp);
        drivetrain.follower.startTeleopDrive();

        register(shooter, drivetrain);

        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        driverOp.getGamepadButton(GamepadKeys.Button.A).whenPressed(() -> shooter.setRequestedVelocity(1500));
        driverOp.getGamepadButton(GamepadKeys.Button.B).whenPressed(() -> shooter.setRequestedVelocity(0));
        driverOp.getGamepadButton(GamepadKeys.Button.X).whenPressed(() -> shooter.setRequestedVelocity(shooter.getFlywheelVelocity() + 500));
        driverOp.getGamepadButton(GamepadKeys.Button.Y).whenPressed(() -> shooter.setRequestedVelocity(shooter.getFlywheelVelocity() - 500));

        driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenPressed(
                () -> drivetrain.follower.setMaxPower(0.25),
                () -> drivetrain.follower.setMaxPower(1.0)
        );

        driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenPressed(
                () -> shooter.setLaunchServoPower(1),
                () -> shooter.setLaunchServoPower(0)
        );
    }
}
