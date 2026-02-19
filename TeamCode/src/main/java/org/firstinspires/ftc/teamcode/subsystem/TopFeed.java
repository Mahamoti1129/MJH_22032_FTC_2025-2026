package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TopFeed extends SubsystemBase {

    private final ServoEx topFeedServo;
    private final GamepadEx toolOp;
    private final Telemetry telemetry;
    private final double OPEN = 0.7;
    private final double CLOSED = 0.45;

    public TopFeed(HardwareMap hardwareMap, GamepadEx toolOp, Telemetry telemetry) {
        this.topFeedServo = new ServoEx(hardwareMap, "topFeedServo");
        this.toolOp = toolOp;
        this.telemetry = telemetry;
        this.close();
    }

    public void open(){
        topFeedServo.set(OPEN);
    }

    public void close(){
        topFeedServo.set(CLOSED);
    }
}
