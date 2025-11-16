package org.firstinspires.ftc.teamcode.subsystem;

import android.graphics.Canvas;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class Camera extends SubsystemBase {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    private List<AprilTagDetection> aprilTagDetections = new ArrayList<>();

    public void init(HardwareMap hardwareMap){
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
//        PanelsVisionProcessor panelsProcessor = new PanelsVisionProcessor();

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"),
                aprilTagProcessor
//                ,panelsProcessor
        );

//        PanelsCameraStream.startStream(panelsProcessor);
    }

    @Override
    public void periodic() {
        aprilTagDetections = aprilTagProcessor.getDetections();
    }
}
