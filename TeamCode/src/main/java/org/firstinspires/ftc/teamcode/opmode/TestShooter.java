package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Shooter;

@TeleOp(name="Test Shooter")
public class TestShooter extends OpMode {
    Shooter shooter;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);
    }

    @Override
    public void loop() {
        shooter.setShooterMotorPower(gamepad1.left_stick_y);

        if(gamepad1.a)
            shooter.launch();
    }
}
