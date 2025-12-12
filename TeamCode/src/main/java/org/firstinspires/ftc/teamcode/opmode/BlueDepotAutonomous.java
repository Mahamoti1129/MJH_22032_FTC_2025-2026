package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@Autonomous(name="Blue Depot Launch", group="Autonomous")
public class BlueDepotAutonomous extends CommandOpMode {
    private TelemetryManager telemetryManager;
    private Shooter shooter;
    private Drivetrain drivetrain;
    private Camera camera;

    private GamepadEx driverOp, toolOp;

    private final Pose
            startingPose = new Pose(21.160, 123.776, Math.toRadians(144)),
            shootingPose = new Pose(47.938, 96.062, Math.toRadians(139)),
            parkPose = new Pose(47.938, 72.281, Math.toRadians(90));

    private PathChain startToShootingPosition, shootingPositionToPark;

    public void buildPaths(){
        startToShootingPosition = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startingPose, shootingPose))
                .setLinearHeadingInterpolation(startingPose.getHeading(), shootingPose.getHeading())
                .build();

        shootingPositionToPark = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootingPose, parkPose))
                .setLinearHeadingInterpolation(shootingPose.getHeading(), parkPose.getHeading())
                .build();
    }

    @Override
    public void initialize() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        shooter = new Shooter();
        shooter.init(hardwareMap, telemetry);

        drivetrain = new Drivetrain();
        drivetrain.init(hardwareMap, driverOp, true);

        camera = new Camera();
        camera.init(hardwareMap);

        register(shooter, drivetrain, camera);

        buildPaths();
        drivetrain.follower.setStartingPose(startingPose);
        drivetrain.follower.setMaxPower(0.5);

        SequentialCommandGroup autonomousSequence = new SequentialCommandGroup(
                // shooting position
                new FollowPathCommand(drivetrain.follower, startToShootingPosition),

                // initial flywheel spinup
                new InstantCommand(() -> shooter.setRequestedVelocity(getShooterVelocityFromDistance())),
                new WaitCommand(3000),

                // fire 1
                fireSequence(),
                new WaitCommand(2000),

                // fire 2
                fireSequence(),
                new WaitCommand(2000),

                // fire 3
                fireSequence(),

                // shut down flywheel
                new InstantCommand(() -> shooter.setRequestedVelocity(0)),
                // park
                new FollowPathCommand(drivetrain.follower, shootingPositionToPark)
        );

        schedule(autonomousSequence);
    }

    private double getShooterVelocityFromDistance() {
        //TODO: use camera to detect distance to QR code, use interpolated lookup table to calculate velocity
        return 1400 + 2*280;
    }

    private SequentialCommandGroup fireSequence(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> shooter.setLaunchServoPower(1)),
                new WaitCommand(640),
                new InstantCommand(() -> shooter.setLaunchServoPower(0))
        );
    }

    @Override
    public void run() {
        super.run();

        telemetryManager.addData("X", drivetrain.follower.getPose().getX());
        telemetryManager.addData("Y", drivetrain.follower.getPose().getY());
        telemetryManager.addData("Heading", drivetrain.follower.getPose().getHeading());
        telemetryManager.update(telemetry);
    }
}
