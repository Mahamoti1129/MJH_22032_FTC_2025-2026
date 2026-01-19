package org.firstinspires.ftc.teamcode.subsystem;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class Camera extends SubsystemBase {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    private TelemetryManager telemetryManager;
    private Telemetry telemetry;

    private List<AprilTagDetection> aprilTagDetections = new ArrayList<>();

    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        this.telemetry = telemetry;

        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"),
                aprilTagProcessor
        );
    }

    @Override
    public void periodic() {
        aprilTagDetections = aprilTagProcessor.getDetections();

        AprilTagDetection red = getAprilTagDetection(24);
        if (red != null){
            //RED
            telemetryManager.addData("RED depot ftcPose.range", red.ftcPose.range);
        }
        telemetryManager.update(telemetry);
    }

    public List<AprilTagDetection> getAprilTagDetections(){
        return aprilTagDetections;
    }

    public AprilTagDetection getAprilTagDetection(int id){
        for (AprilTagDetection detection : aprilTagDetections){
            if (detection.id == id){
                return detection;
            }
        }
        return null;
    }
}
