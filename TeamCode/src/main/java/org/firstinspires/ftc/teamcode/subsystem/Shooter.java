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

    public MotorEx flywheelLeft;
    public MotorEx flywheelRight;
    CRServo leftServo;
    CRServo rightServo;

    private double requestedVelocity = 0;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        this.telemetry = telemetry;

        flywheelLeft = new MotorEx(hardwareMap, "leftFlywheel", Motor.GoBILDA.BARE);
        flywheelLeft.setRunMode(Motor.RunMode.VelocityControl);

        flywheelRight = new MotorEx(hardwareMap, "rightFlywheel", Motor.GoBILDA.BARE);
        flywheelRight.setRunMode(Motor.RunMode.VelocityControl);
        flywheelRight.setInverted(true);

/*
        flywheelMotor.setVeloCoefficients(
                ShooterConstants.kP,
                ShooterConstants.kI,
                ShooterConstants.kD
        );
*/
        leftServo = hardwareMap.get(CRServo.class, "leftShooterServo");
        rightServo = hardwareMap.get(CRServo.class, "rightShooter");
        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        this.setLaunchServoPower(0);
    }

    public void setFlywheelVelocity(double v){
        flywheelLeft.setVelocity(v);
        flywheelRight.setVelocity(v);
    }
    public double getFlywheelVelocity(){ return flywheelLeft.getVelocity(); }
    public double getFlywheelCorrectedVelocity(){ return flywheelLeft.getCorrectedVelocity(); }

    public void stopFlywheel(){
        flywheelLeft.stopMotor();
        flywheelRight.stopMotor();
    }

    public void setLaunchServoPower(double power){
        leftServo.setPower(power);
        rightServo.setPower(power);
    }

    public double getRequestedVelocity() {
        return requestedVelocity;
    }

    public void setRequestedVelocity(double requestedVelocity) {
        this.requestedVelocity = Math.clamp(requestedVelocity, 0, flywheelLeft.ACHIEVABLE_MAX_TICKS_PER_SECOND);
    }

    @Override
    public void periodic() {
        flywheelLeft.setVelocity(requestedVelocity);

        telemetryManager.addData("flywheelCorrectedVelocity", flywheelLeft.getCorrectedVelocity());
        telemetryManager.addData("requestedVelocity", requestedVelocity);
        telemetryManager.addData("flywheelAtRequestedVelocity", Math.abs(requestedVelocity - flywheelLeft.getCorrectedVelocity()) < 28);
        telemetryManager.update(telemetry);
    }

    public SequentialCommandGroup fireSequence(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.setLaunchServoPower(1)),
                new WaitCommand(640),
                new InstantCommand(() -> this.setLaunchServoPower(0))
        );
    }

}
