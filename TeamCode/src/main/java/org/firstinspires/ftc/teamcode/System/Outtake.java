package org.firstinspires.ftc.teamcode.System;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.Controls;
import org.firstinspires.ftc.teamcode.Modules.Elevator;
import org.firstinspires.ftc.teamcode.Modules.Grippers;

public class Outtake {

    public enum States{
        GDOWN,GUP,SCORING,IDLE;
    };
    States state = States.IDLE;

    Grippers left, right;
    ElapsedTime timer;
    double lopen,lclose,ropen,rclose,extendedLinkage,retractedLinkage, parallelTPos, parallelGPos;
    double[] pos ={0.2,0.4,0.6};
    Elevator elevator;
    Servo linkage, parallel, pivot, turret;
    boolean rtimer;

    public Outtake(HardwareMap hm){
        left = new Grippers(hm, 50,lopen,lclose,1);
        right = new Grippers(hm, 50,ropen,rclose,2);
        left.update();
        right.update();
        elevator = new Elevator(hm);
        elevator.update();
        linkage = hm.get(Servo.class, "linkage");
        parallel = hm.get(Servo.class, "parallel");
        pivot = hm.get(Servo.class, "pivot");
        turret = hm.get(Servo.class, "turret");
        timer.reset();
    }

    public void loop(){

        if((elevator.getState() == Elevator.State.GOING_UP || elevator.getState() == Elevator.State.UP)&&state!=States.SCORING)
            state = States.GUP;
        switch (state){
            case GUP:
                if(timer.milliseconds() >= 1200) state = States.SCORING;
                else if(timer.milliseconds()>800) parallel.setPosition(parallelTPos);
                else if(timer.milliseconds()>100) linkage.setPosition(extendedLinkage);
                break;
            case GDOWN:
                Controls.rotatelvl=1;
                parallel.setPosition(parallelGPos);
                pivot.setPosition(pos[Controls.rotatelvl]);
                linkage.setPosition(retractedLinkage);
                break;
            case SCORING:
                if(Controls.rot) pivot.setPosition(pos[Controls.rotatelvl]);
                //TODO: tureta
                if(left.getState() == Grippers.State.OPEN )
                {
                    Controls.lgc = false;
                }
                if(right.getState() == Grippers.State.OPEN )
                {
                    Controls.rgc = false;
                }
                if(left.getState() == Grippers.State.OPEN && right.getState() == Grippers.State.OPEN)
                    state = States.GDOWN;
                break;
            case IDLE:
                break;

        }



    }


}
