package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PidControler {
    ElapsedTime timer;
    PIDCoefficients pid;
    double ct;
    double lastError;
    double Isum = 0;
    public PidControler(PIDCoefficients pid){
        timer.reset();
        this.pid = pid;
    }
    public void setTargetPosition(double target)
    {
        ct = target;
        timer.reset();
        Isum = 0;
    }
    public double calculate(double cp)
    {
        double dtime = timer.seconds();
        double error = ct -cp;
        double D = (lastError - error)/dtime;
        Isum += error*dtime;
        lastError = error;
        timer.reset();
        return error*pid.p+D*pid.d+Isum*pid.i;
    }
}
