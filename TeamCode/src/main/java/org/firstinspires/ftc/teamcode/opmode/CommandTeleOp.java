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
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name="Competition TeleOp", group="TeleOp")
public class CommandTeleOp extends CommandOpMode {
    private Shooter shooter;
    private Drivetrain drivetrain;
    private Camera camera;

    private GamepadEx driverOp, toolOp;

    private static double shooterDistanceMin = 27;
    private static double shooterDistanceMax = 80;
    private static int redDepotAprilTag = 24;
    private static int blueDepotAprilTag = 20;

    private InterpLUT shooterDistanceSpeedLookupTable = new InterpLUT(){{
        add(shooterDistanceMin-1, 5*280);
        add(41.5, 6.5*280);
        add(shooterDistanceMax+1, 10*280);
    }};

    @Override
    public void initialize() {
        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        shooter = new Shooter();
        shooter.init(hardwareMap, telemetry);

        shooterDistanceSpeedLookupTable.createLUT();

        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap, driverOp, false);
        drivetrain.follower.startTeleopDrive();

        camera = new Camera();
        camera.init(hardwareMap, telemetry);

        register(shooter, drivetrain, camera);

        toolOp.getGamepadButton(A).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(1400)));
        toolOp.getGamepadButton(B).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(0)));
        toolOp.getGamepadButton(X).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(shooter.getRequestedVelocity() + 280)));
        toolOp.getGamepadButton(Y).whenPressed(new InstantCommand(() -> shooter.setRequestedVelocity(shooter.getRequestedVelocity() - 280)));

        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new InstantCommand(() -> {
                    AprilTagDetection red = camera.getAprilTagDetection(redDepotAprilTag);
                    if (red != null){
                        shooter.setRequestedVelocity(shooterDistanceSpeedLookupTable.get(Math.clamp(red.ftcPose.range, shooterDistanceMin, shooterDistanceMax)));
                    }
                }));

        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new InstantCommand(() -> {
                    AprilTagDetection blue = camera.getAprilTagDetection(blueDepotAprilTag);
                    if (blue != null){
                        shooter.setRequestedVelocity(shooterDistanceSpeedLookupTable.get(Math.clamp(blue.ftcPose.range, shooterDistanceMin, shooterDistanceMax)));
                    }
                }));

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
