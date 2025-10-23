package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    DcMotor shooterMotor;
    CRServo leftServo;
    CRServo rightServo;
    public Shooter(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
        leftServo = hardwareMap.get(CRServo.class, "leftShooterServo");
        rightServo = hardwareMap.get(CRServo.class, "rightShooterServo");

        //TODO: choose correct servo
        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setShooterMotorPower(double power){
        shooterMotor.setPower(power);
    }

    public void setLaunchServoPower(double power){
        leftServo.setPower(power);
        rightServo.setPower(power);
    }
}
