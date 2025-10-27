package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@TeleOp(name="Test Shooter")
public class TestShooter extends OpMode {
    Shooter shooter;
    Drivetrain drivetrain;

    private TelemetryManager telemetryManager;

    @Override
    public void init() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        shooter = new Shooter(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void start() {
        drivetrain.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        shooter.setShooterMotorPower(gamepad1.left_trigger);

        if(gamepad1.a) {
            shooter.setLaunchServoPower(1);
        }else {
            shooter.setLaunchServoPower(0);
        }

        drivetrain.drive(
                -gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                -gamepad1.right_stick_x
        );

        telemetryManager.debug("position", drivetrain.follower.getPose());
        telemetryManager.debug("velocity", drivetrain.follower.getVelocity());
        telemetryManager.debug("heading", drivetrain.follower.getHeading());
        telemetryManager.update();
    }
}
