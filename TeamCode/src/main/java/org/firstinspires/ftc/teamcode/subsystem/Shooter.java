package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;

public class Shooter extends SubsystemBase {
    DcMotor shooterMotor;
    CRServo leftServo;
    CRServo rightServo;
    public Shooter(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
        leftServo = hardwareMap.get(CRServo.class, "leftShooterServo");
        rightServo = hardwareMap.get(CRServo.class, "rightShooterServo");

        //TODO: choose correct servo
        leftServo.setInverted(true);
    }

    public void setShooterMotorPower(double power){
        shooterMotor.setPower(power);
    }

    public void launch(){
        leftServo.set(1);
        rightServo.set(1);

        ElapsedTime timer = new ElapsedTime();
        while (timer.time() < 0.5){}

        leftServo.stop();
        rightServo.stop();
    }
}
