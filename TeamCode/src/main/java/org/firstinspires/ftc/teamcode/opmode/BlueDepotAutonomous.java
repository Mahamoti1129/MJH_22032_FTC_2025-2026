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

import org.firstinspires.ftc.teamcode.Greg;
import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@Autonomous(name="Blue Depot Launch", group="Autonomous")
public class BlueDepotAutonomous extends CommandOpMode {
    private TelemetryManager telemetryManager;

    private Greg greg;
    private GamepadEx driverOp, toolOp;

    private static double FLYWHEEL_VELOCITY = 1100;

    private final Pose
            startingPose = new Pose(21.160, 123.776, Math.toRadians(144)),
            shootingPose = new Pose(47.938, 96.062, Math.toRadians(139)),
            parkPose = new Pose(47.938, 128.281, Math.toRadians(90));

    private PathChain startToShootingPosition, shootingPositionToPark;

    public void buildPaths(){
        startToShootingPosition = greg.drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startingPose, shootingPose))
                .setLinearHeadingInterpolation(startingPose.getHeading(), shootingPose.getHeading())
                .build();

        shootingPositionToPark = greg.drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootingPose, parkPose))
                .setLinearHeadingInterpolation(shootingPose.getHeading(), parkPose.getHeading())
                .build();
    }

    @Override
    public void initialize() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

        driverOp = new GamepadEx(gamepad1);
        toolOp = new GamepadEx(gamepad2);

        greg = new Greg(hardwareMap, driverOp, toolOp, telemetry, true);

        buildPaths();
        greg.drivetrain.follower.setStartingPose(startingPose);
        greg.drivetrain.follower.setMaxPower(0.5);

        SequentialCommandGroup autonomousSequence = new SequentialCommandGroup(
                greg.shootMode(),

                // shooting position
                new FollowPathCommand(greg.drivetrain.follower, startToShootingPosition),

                // initial flywheel spinup
                new InstantCommand(() -> greg.shooter.setRequestedVelocity(FLYWHEEL_VELOCITY)),
                new WaitCommand(3000),

                // fire 1
                greg.fireSequence(),
                new WaitCommand(2000),

                // fire 2
                greg.fireSequence(),
                new WaitCommand(2000),

                // fire 3
                greg.fireSequence(),

                // shut down flywheel
                new InstantCommand(() -> greg.shooter.setRequestedVelocity(Shooter.OFF)),
                // park
                new FollowPathCommand(greg.drivetrain.follower, shootingPositionToPark)
        );

        schedule(autonomousSequence);
    }
}
