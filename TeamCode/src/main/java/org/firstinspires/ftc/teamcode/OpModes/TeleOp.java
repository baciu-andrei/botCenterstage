package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Controls;
import org.firstinspires.ftc.teamcode.System.DriveTrain;
import org.firstinspires.ftc.teamcode.System.Intake;
import org.firstinspires.ftc.teamcode.System.Outtake;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "magic")
public class TeleOp extends LinearOpMode {

    DriveTrain driveTrain;
    Controls gp;
    Intake intake;
    Outtake outtake;
    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(hardwareMap , gamepad1 , gamepad2 , telemetry);
        intake = new Intake(hardwareMap);
        gp = new Controls(gamepad1, gamepad2);
        outtake = new Outtake(hardwareMap);
        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            driveTrain.loop();
            gp.loop();
            intake.loop();
            outtake.loop();
        }
    }
}
