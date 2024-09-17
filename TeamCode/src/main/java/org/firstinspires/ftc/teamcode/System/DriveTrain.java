package org.firstinspires.ftc.teamcode.System;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class DriveTrain {

    DcMotorEx mfl;
    DcMotorEx mfr;
    DcMotorEx mbl;
    DcMotorEx mbr;
    Gamepad gamepad1, gamepad2;
    Telemetry tele;
    IMU imu;

    public enum SPEED{
        SLOW(0.5),
        FAST(1);
        public final double multiplier;

        SPEED(double multiplier) {
            this.multiplier = multiplier;
        }
    }
    public SPEED speed = SPEED.FAST;

    public DriveTrain (HardwareMap hm, Gamepad gamepad1, Gamepad gamepad2, Telemetry tele)
    {
        mfl = hm.get(DcMotorEx.class, "mfl");
        mfr = hm.get(DcMotorEx.class, "mfr");
        mbl = hm.get(DcMotorEx.class, "mbl");
        mbr = hm.get(DcMotorEx.class, "mbr");

//        mfl.setDirection(DcMotorSimple.Direction.REVERSE);
        mfr.setDirection(DcMotorSimple.Direction.REVERSE);
//        mbl.setDirection(DcMotorSimple.Direction.REVERSE);
        mbr.setDirection(DcMotorSimple.Direction.REVERSE);

        ArrayList<DcMotorEx> motorListDrive = new ArrayList<>();
        motorListDrive.add(mfl);motorListDrive.add(mfr);
        motorListDrive.add(mbl);motorListDrive.add(mbr);

        for(DcMotorEx motor: motorListDrive){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);

        }

        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.tele = tele;

        imu = hm.get(IMU.class,"imuControl");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        if(imu!=null)
            imu.initialize(parameters);
    }

    public static class DriveParameters{
        public double forward, right , turn;

        public DriveParameters(double forward, double right, double turn) {
            this.forward = forward;
            this.right = right;
            this.turn = turn;
        }

        void normalize(){
            double denominator = Math.abs(forward) + Math.abs(right) + Math.abs(turn);
            denominator = Math.max(1, denominator);
            forward /= denominator;
            right /= denominator;
            turn /= denominator;
        }

        void setMultiplier(double multiplier){
            forward *= multiplier;
            right *= multiplier;
            turn *= multiplier;
        }

    }

    public void switchSpeed()
    {
        if(speed == SPEED.FAST)
            speed = SPEED.SLOW;
        else
            speed = SPEED.FAST;
    }

    public DriveParameters getDriveParameters  (double forward , double right , double turn){
        DriveParameters driveParameters = new DriveParameters(forward,right,turn);
        driveParameters.setMultiplier(speed.multiplier);
        return driveParameters;
    }
    private void driveForValues(DriveParameters parameters){
        parameters.normalize();
        mfl.setPower(parameters.forward + parameters.right + parameters.turn);
        mfr.setPower(parameters.forward - parameters.right - parameters.turn);
        mbl.setPower(parameters.forward - parameters.right + parameters.turn);
        mbr.setPower(parameters.forward + parameters.right - parameters.turn);

    }

    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double turn = gamepad1.right_trigger - gamepad1.left_trigger;
        driveForValues(getDriveParameters(forward, right, turn));
    }
}
