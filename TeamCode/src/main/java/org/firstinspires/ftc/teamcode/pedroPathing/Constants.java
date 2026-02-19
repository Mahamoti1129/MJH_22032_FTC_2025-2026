package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5)
//            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0.01, 0.0))
//            .headingPIDFCoefficients(new PIDFCoefficients(0.4, 0, 0.01, 0.02))
//            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.02, 0, 0.00035, 0.6, 0.015))
            .centripetalScaling(0.005)
            .forwardZeroPowerAcceleration(-29.0)
            .lateralZeroPowerAcceleration(-45.25);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 10, 1);

    public static MecanumConstants drivetrainConstants = new MecanumConstants()
            .maxPower(1)
            .leftFrontMotorName("frontLeftDrive")
            .leftRearMotorName("rearLeftDrive")
            .rightFrontMotorName("frontRightDrive")
            .rightRearMotorName("rearRightDrive")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .useBrakeModeInTeleOp(true)
            .xVelocity(21.0078)
            .yVelocity(16.728);


    public static ThreeWheelIMUConstants localizerConstants = new ThreeWheelIMUConstants()
            .forwardTicksToInches(.001996612866)
            .strafeTicksToInches(0.002029922169)
            .turnTicksToInches(.0019805)
            .leftPodY(1.875)
            .rightPodY(-2.125)
            .strafePodX(-4.25)
            .leftEncoder_HardwareMapName("frontLeftDrive")
            .rightEncoder_HardwareMapName("frontRightDrive")
            .strafeEncoder_HardwareMapName("rearLeftDrive")
            .leftEncoderDirection(Encoder.REVERSE)
            .rightEncoderDirection(Encoder.REVERSE)
            .strafeEncoderDirection(Encoder.FORWARD)
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(drivetrainConstants)
                .threeWheelIMULocalizer(localizerConstants)
                .build();
    }
}
