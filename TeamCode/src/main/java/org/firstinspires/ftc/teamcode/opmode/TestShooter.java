package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.subsystem.ShooterConstants;

@TeleOp(name="Test Shooter")
public class TestShooter extends OpMode {
    Shooter shooter;
    Drivetrain drivetrain;

    private TelemetryManager telemetryManager;

    @Override
    public void init() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        shooter = new Shooter();
        shooter.init(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void start() {
        drivetrain.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) shooter.setFlywheelVelocity(1500);
        if (gamepad1.xWasPressed()) shooter.stopFlywheel();
        if (gamepad1.aWasPressed()) shooter.setFlywheelVelocity(shooter.getFlywheelVelocity() + 500);
        if (gamepad1.bWasPressed()) shooter.setFlywheelVelocity(shooter.getFlywheelVelocity() - 500);

        if(gamepad1.right_bumper) {
            shooter.setLaunchServoPower(1);
        }else {
            shooter.setLaunchServoPower(0);
        }

        if(gamepad1.left_bumper){
            drivetrain.follower.setMaxPower(0.25);
        }else {
            drivetrain.follower.setMaxPower(1.0);
        }

        drivetrain.drive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

//        telemetryManager.debug("position", drivetrain.follower.getPose());
//        telemetryManager.debug("velocity", drivetrain.follower.getVelocity());
//        telemetryManager.debug("heading", drivetrain.follower.getHeading());
        telemetryManager.addData("flywheelVelocity", shooter.getFlywheelVelocity());
        telemetryManager.addData("flywheelCorrectedVelocity", shooter.getFlywheelCorrectedVelocity());
        telemetryManager.addData("flywheelVelP", ShooterConstants.kP);
        telemetryManager.addData("flywheelVelI", ShooterConstants.kI);
        telemetryManager.addData("flywheelVelD", ShooterConstants.kD);
        telemetryManager.addData("flywheelMotorPID", shooter.flywheelMotor.getVeloCoefficients());
        telemetryManager.update(telemetry);
    }
}
