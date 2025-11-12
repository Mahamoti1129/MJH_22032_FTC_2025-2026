package org.firstinspires.ftc.teamcode.subsystem;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Configurable
public class Drivetrain extends SubsystemBase {

    public static boolean robotCentric = true;
    public Follower follower;
    public GamepadEx driverOp;

    public void init(HardwareMap hardwareMap, GamepadEx driverOp){
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose());
        follower.update();

        this.driverOp = driverOp;
    }

    public void drive(double x, double y, double z){
        follower.setTeleOpDrive(x, y, z, robotCentric);
        follower.update();
    }

    @Override
    public void periodic() {
        drive(
                -driverOp.getLeftY(),
                -driverOp.getLeftX(),
                -driverOp.getRightX()
        );
    }
}
