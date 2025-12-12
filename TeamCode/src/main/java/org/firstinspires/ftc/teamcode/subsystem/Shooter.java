package org.firstinspires.ftc.teamcode.subsystem;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends SubsystemBase {
    private TelemetryManager telemetryManager;
    private Telemetry telemetry;

    public MotorEx flywheelMotor;
    CRServo leftServo;
    CRServo rightServo;

    private double requestedVelocity = 0;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        this.telemetry = telemetry;

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
        this.requestedVelocity = Math.clamp(requestedVelocity, 0, flywheelMotor.ACHIEVABLE_MAX_TICKS_PER_SECOND);
    }

    @Override
    public void periodic() {
        flywheelMotor.setVelocity(requestedVelocity);
//
//        telemetryManager.addData("flywheelCorrectedVelocity", flywheelMotor.getCorrectedVelocity());
//        telemetryManager.addData("requestedVelocity", requestedVelocity);
//        telemetryManager.addData("flywheelAtRequestedVelocity", Math.abs(requestedVelocity - flywheelMotor.getCorrectedVelocity()) < 28);
//        telemetryManager.update(telemetry);
    }

    public SequentialCommandGroup fireSequence(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.setLaunchServoPower(1)),
                new WaitCommand(640),
                new InstantCommand(() -> this.setLaunchServoPower(0))
        );
    }

}
