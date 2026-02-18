package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.subsystem.TopFeed;
import org.firstinspires.ftc.teamcode.subsystem.Transfer;

public class Greg extends Robot {
    public final Drivetrain drivetrain;
    public final Shooter shooter;
    public final Intake intake;
    public final Transfer transfer;
    public final TopFeed topFeed;

    public final TelemetryManager telemetryManager;

    public Greg(HardwareMap hardwareMap, GamepadEx driveOp, GamepadEx toolOp, Telemetry telemetry, boolean autonomous) {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

        this.drivetrain = new Drivetrain(hardwareMap, driveOp, autonomous);
        this.shooter = new Shooter(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap);
        this.transfer = new Transfer(hardwareMap);
        this.topFeed = new TopFeed(hardwareMap);

        register(drivetrain, shooter, intake, transfer, topFeed);
    }

}
