package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer extends SubsystemBase {

    private final ServoEx transferServo;
    private final ServoEx gateServo;
    private final GamepadEx toolOp;
    private final Telemetry telemetry;

    private final double OPEN = 0.3;
    private final double CLOSED = 0.65;

    private final double FEED = 0.56;
    private final double INTAKE = 0.4;

    public Transfer(HardwareMap hardwareMap, GamepadEx toolOp, Telemetry telemetry){
        this.transferServo = new ServoEx(hardwareMap, "transferServo");
        this.gateServo = new ServoEx(hardwareMap, "gateServo");
        this.toolOp = toolOp;
        this.telemetry = telemetry;
        intake();
    }

    public void shoot(){
        gateServo.set(OPEN);
        transferServo.set(FEED);
    }

    public void intake(){
        gateServo.set(CLOSED);
        transferServo.set(INTAKE);
    }
}
