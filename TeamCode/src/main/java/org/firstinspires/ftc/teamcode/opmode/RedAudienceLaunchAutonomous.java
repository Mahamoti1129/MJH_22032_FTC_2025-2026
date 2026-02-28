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

@Autonomous(name="Red Audience Launch", group="Autonomous")
public class RedAudienceLaunchAutonomous extends CommandOpMode {
    private TelemetryManager telemetryManager;
    private Greg greg;
    private static double FLYWHEEL_SPEED = 1450;
    private GamepadEx driverOp, toolOp;

    private final Pose
            startingPose = new Pose(95.875, 8.052, Math.toRadians(90)),
            shootingPose = new Pose(88.644, 19.482, Math.toRadians(70)),
            parkPose = new Pose(95.126, 36, Math.toRadians(90));

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
                // shooting position
                new FollowPathCommand(greg.drivetrain.follower, startToShootingPosition),

                // initial flywheel spinup
                new InstantCommand(() -> greg.shooter.setRequestedVelocity(FLYWHEEL_SPEED)),
                new WaitCommand(3500),

                // fire 1
                greg.fireSequence(),
                new WaitCommand(2500),

                // fire 2
                greg.fireSequence(),
                new WaitCommand(2500),

                // fire 3
                greg.fireSequence(),

                // shut down flywheel
                new InstantCommand(() -> greg.shooter.setRequestedVelocity(0)),
                // park
                new FollowPathCommand(greg.drivetrain.follower, shootingPositionToPark)
        );


        schedule(autonomousSequence);
    }


}
