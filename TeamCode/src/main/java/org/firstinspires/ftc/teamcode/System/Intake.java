package org.firstinspires.ftc.teamcode.System;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Modules.Controls;

public class Intake {

    DcMotorEx intakeMotor;

    public Intake(HardwareMap hm)
    {
        intakeMotor = hm.get(DcMotorEx.class, "intake_motor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        loop();
    }
    public void loop()
    {
        if(Controls.intakefr)
            intakeMotor.setPower(1);
        else if(Controls.intakerev)
            intakeMotor.setPower(-1);
        else
            intakeMotor.setPower(0);
    }

}
