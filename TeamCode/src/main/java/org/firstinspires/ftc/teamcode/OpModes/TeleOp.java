package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.System.DriveTrain;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "magic")
public class TeleOp extends LinearOpMode {

    DriveTrain driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(hardwareMap , gamepad1 , gamepad2 , telemetry);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            driveTrain.loop();
        }
    }
}
