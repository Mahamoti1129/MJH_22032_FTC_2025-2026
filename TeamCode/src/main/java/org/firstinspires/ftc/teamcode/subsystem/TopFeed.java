package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class TopFeed extends SubsystemBase {

    private final ServoEx topFeedServo;
    private final double OPEN = 1.0;
    private final double CLOSED = 0.0;

    public TopFeed(HardwareMap hardwareMap) {
        this.topFeedServo = new ServoEx(hardwareMap, "topFeedServo");
        this.close();
    }

    public void open(){
        topFeedServo.set(OPEN);
    }

    public void close(){
        topFeedServo.set(CLOSED);
    }
}
