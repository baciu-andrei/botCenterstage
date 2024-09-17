package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Controls {

    public static Gamepad gamepad1;
    public static Gamepad gamepad2;
    public static boolean intakefr,intakerev,elevup,lgc,rgc,elevdown,plane,climb,ready4int = true;
    public static int rotatelvl, elevatorLvl;
    ElapsedTime cnt;
    public Controls(Gamepad gp1, Gamepad gp2){
        gamepad1 = gp1;
        gamepad2 = gp2;
        cnt = new ElapsedTime();
    }
    public void loop()
    {
        /*
        TODO :  1. add grippers(for lgc and rgc)
                2. add sequence in elevator for elev down
         */
        //intake
        if(gamepad1.a &&!grippersClosed() && ready4int)
            intakefr = true;
        if(grippersClosed() && gamepad1.a && ready4int)
        {
            ready4int = false;
            intakefr = false;
            cnt.reset();
        }
        if(grippersClosed() && !ready4int && goodTime())
            intakerev = true;
        if(grippersClosed() && !ready4int && cnt.milliseconds()>2200)
        {
            intakerev = false;
        }

        //grippers
        if(lgc && gamepad1.x)
            lgc = false;
        if(rgc && gamepad1.b)
            rgc = false;

        //elevator
        if(gamepad1.y && grippersClosed() && !elevup) {elevup = true;elevdown = false;}
        if(elevup && !lgc && !rgc && !elevdown){cnt.reset();elevup = false;}
        if(!elevup && !lgc && !rgc && !elevdown && goodTime()){elevdown = true; ready4int = true;}
        if(gamepad1.dpad_up && elevatorLvl<=6) elevatorLvl++;
        if(gamepad1.dpad_down && elevatorLvl>0) elevatorLvl--;
        if(gamepad1.dpad_right) elevatorLvl = 6;
        if(gamepad1.dpad_left) elevatorLvl = 0;


        //brat
        if(gamepad1.options)
        {
            if(rotatelvl<3)
                rotatelvl++;
            else
                rotatelvl = 0;
        }

        //end game
        if(gamepad1.share)
        {
            if(!plane)
                plane = true;
            else
                climb = true;
        }
    }
    public boolean grippersClosed(){
        return lgc && rgc;
    }
    public boolean goodTime(){
        return cnt.milliseconds()>200 && cnt.milliseconds()<2200;
    }
}