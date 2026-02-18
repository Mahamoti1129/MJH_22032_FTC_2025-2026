package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class Transfer extends SubsystemBase {

    private final ServoEx transferServo;
    private final ServoEx gateServo;

    private final double OPEN = 1.0;
    private final double CLOSED = 0.0;

    private final double FEED = 1.0;
    private final double INTAKE = -1.0;

    public Transfer(HardwareMap hardwareMap){
        this.transferServo = new ServoEx(hardwareMap, "transferServo");
        this.gateServo = new ServoEx(hardwareMap, "gateServo");

        close();
    }

    public void open(){
        gateServo.set(OPEN);
        transferServo.set(FEED);
    }

    public void close(){
        gateServo.set(CLOSED);
        transferServo.set(INTAKE);
    }
}
