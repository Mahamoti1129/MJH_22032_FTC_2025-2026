package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.subsystem.ShooterConstants;

@TeleOp(name="Test Shooter")
public class TestShooter extends OpMode {
    Shooter shooter;

    private TelemetryManager telemetryManager;

    @Override
    public void init() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        shooter = new Shooter(hardwareMap, telemetry);
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
//            shooter.setLaunchServoPower(0);
        }

        telemetryManager.addData("flywheelCorrectedVelocity", shooter.getFlywheelCorrectedVelocity());
        telemetryManager.update(telemetry);
    }
}
