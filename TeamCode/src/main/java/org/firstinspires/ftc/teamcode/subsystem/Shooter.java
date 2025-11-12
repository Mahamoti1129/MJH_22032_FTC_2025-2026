package org.firstinspires.ftc.teamcode.subsystem;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

public class Shooter extends SubsystemBase {
    private TelemetryManager telemetryManager;

    public MotorEx flywheelMotor;
    CRServo leftServo;
    CRServo rightServo;

    private double requestedVelocity = 0;

    public void init(HardwareMap hardwareMap) {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

        flywheelMotor = new MotorEx(hardwareMap, "shooterMotor", Motor.GoBILDA.BARE);
        flywheelMotor.setRunMode(Motor.RunMode.VelocityControl);
        flywheelMotor.setVeloCoefficients(
                ShooterConstants.kP,
                ShooterConstants.kI,
                ShooterConstants.kD
        );

        leftServo = hardwareMap.get(CRServo.class, "leftShooterServo");
        rightServo = hardwareMap.get(CRServo.class, "rightShooterServo");
        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setFlywheelVelocity(double v){
        flywheelMotor.setVelocity(v);
    }
    public double getFlywheelVelocity(){ return flywheelMotor.getVelocity(); }
    public double getFlywheelCorrectedVelocity(){ return flywheelMotor.getCorrectedVelocity(); }

    public void stopFlywheel(){ flywheelMotor.stopMotor(); }

    public void setLaunchServoPower(double power){
        leftServo.setPower(power);
        rightServo.setPower(power);
    }

    public double getRequestedVelocity() {
        return requestedVelocity;
    }

    public void setRequestedVelocity(double requestedVelocity) {
        this.requestedVelocity = requestedVelocity;
    }

    public void flywheelToRequestedVelocity(){
        setFlywheelVelocity(requestedVelocity);
    }

    @Override
    public void periodic() {
        flywheelMotor.setVelocity(requestedVelocity);

        telemetryManager.addData("flywheelVelocity", flywheelMotor.getVelocity());
        telemetryManager.addData("flywheelCorrectedVelocity", flywheelMotor.getCorrectedVelocity());
        telemetryManager.addData("requestedVelocity", requestedVelocity);
        telemetryManager.addData("flywheelAtRequestedVelocity", Math.abs(requestedVelocity - flywheelMotor.getVelocity()) < 10);
        telemetryManager.update();
    }
}
