package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.subsystem.Camera;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@Autonomous(name="Simple Leave Only Autonomous", group="Autonomous")
public class StupidAutonomous extends CommandOpMode {
    private TelemetryManager telemetryManager;
    private Shooter shooter;
    private Drivetrain drivetrain;
    private Camera camera;

    private GamepadEx driverOp, toolOp;

    private final Pose
            startingPose = new Pose(0, 0, 0), //TODO
            parkPose = new Pose(48, 0, 0); //TODO

    private PathChain startToPark;

    public void buildPaths(){
        startToPark = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startingPose, parkPose))
                .setLinearHeadingInterpolation(startingPose.getHeading(), parkPose.getHeading())
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
        camera.init(hardwareMap, telemetry);

        register(shooter, drivetrain, camera);

        drivetrain.follower.setStartingPose(startingPose);
        drivetrain.follower.setMaxPower(0.5);
        buildPaths();

        schedule(new FollowPathCommand(drivetrain.follower, startToPark));
    }
}
