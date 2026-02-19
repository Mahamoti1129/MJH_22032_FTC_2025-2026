package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.Shooter;
import org.firstinspires.ftc.teamcode.subsystem.TopFeed;
import org.firstinspires.ftc.teamcode.subsystem.Transfer;

public class Greg extends Robot {
    public final Drivetrain drivetrain;
//    public final Shooter shooter;
    public final Intake intake;
    public final Transfer transfer;
    public final TopFeed topFeed;

    public final TelemetryManager telemetryManager;

    public Greg(HardwareMap hardwareMap, GamepadEx driveOp, GamepadEx toolOp, Telemetry telemetry, boolean autonomous) {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        drivetrain = new Drivetrain(hardwareMap, driveOp, autonomous);
//        this.shooter = new Shooter(hardwareMap, telemetry);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap, toolOp, telemetry);
        topFeed = new TopFeed(hardwareMap, toolOp, telemetry);

        register(drivetrain, intake, topFeed, transfer);
    }

    public SequentialCommandGroup intakeMode(){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(transfer::intake),
                        new InstantCommand(topFeed::close)
                ),
                new WaitCommand(250),
                new InstantCommand(intake::on)
        );
    }

    public SequentialCommandGroup humanFeedMode(){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(transfer::shoot),
                        new InstantCommand(topFeed::open),
                        new InstantCommand(intake::off)
                ),
                new WaitCommand(250)
        );
    }

    public SequentialCommandGroup shootMode(){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(transfer::shoot),
                        new InstantCommand(topFeed::close),
                        new InstantCommand(intake::off)
                ),
                new WaitCommand(250)
        );
    }
}
