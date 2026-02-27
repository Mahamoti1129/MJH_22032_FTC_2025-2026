package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

public class Intake extends SubsystemBase {
    private final Motor intakeMotor;

    private final double ON = 1.0;
    private final double OFF = 0.0;
    private final double REVERSE = -0.25;

    public Intake(HardwareMap hardwareMap) {
        this.intakeMotor = new Motor(hardwareMap, "intake", Motor.GoBILDA.RPM_312);
        intakeMotor.setInverted(true);
        off();
    };

    public void setPower(double power){
        intakeMotor.set(power);
    }

    public void on(){
        intakeMotor.set(ON);
    }

    public void off(){
        intakeMotor.set(OFF);
    }

    public void reverse(){ intakeMotor.set(REVERSE); }
}
