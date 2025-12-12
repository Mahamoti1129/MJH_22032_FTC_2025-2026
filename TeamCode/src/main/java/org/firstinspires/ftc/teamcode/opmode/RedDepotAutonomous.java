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

@Autonomous(name="Red Depot Launch", group="Autonomous")
public class RedDepotAutonomous extends CommandOpMode {
    private TelemetryManager telemetryManager;
    private Shooter shooter;
    private Drivetrain drivetrain;
    private Camera camera;

    private GamepadEx driverOp, toolOp;

    private final Pose
            startingPose = new Pose(123.027, 123.215, Math.toRadians(37)),
            shootingPose = new Pose(96.437, 95.688, Math.toRadians(46)),
            parkPose = new Pose(96.250, 72.281, Math.toRadians(90));

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
                new InstantCommand(() -> telemetryManager.addLine("waiting 2s for flywheel spinup 1")),
                new WaitCommand(2000),

                new InstantCommand(() -> telemetryManager.addLine("waited 2s, firing 1")),

                // fire 1
                fireSequence(1),
                new InstantCommand(() -> telemetryManager.addLine("waiting 2s for flywheel spinup 2")),
                new WaitCommand(2000),
                new InstantCommand(() -> telemetryManager.addLine("waited 2s, firing 2")),

                // fire 2
                fireSequence(2),
                new InstantCommand(() -> telemetryManager.addLine("waiting 2s for flywheel spinup 3")),
                new WaitCommand(2000),
                new InstantCommand(() -> telemetryManager.addLine("waited 2s, firing 3")),

                // fire 3
                fireSequence(3),
                new InstantCommand(() -> telemetryManager.addLine("navigating to park")),

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

    private SequentialCommandGroup fireSequence(int count){
        return new SequentialCommandGroup(
                new InstantCommand(() -> telemetryManager.addLine("firing " + count + " start")),
                new InstantCommand(() -> shooter.setLaunchServoPower(1)),
                new WaitCommand(640),
                new InstantCommand(() -> shooter.setLaunchServoPower(0)),
                new InstantCommand(() -> telemetryManager.addLine("firing " + count + " complete"))
        );
    }

    @Override
    public void run() {
        super.run();

        telemetryManager.update(telemetry);
    }
}
