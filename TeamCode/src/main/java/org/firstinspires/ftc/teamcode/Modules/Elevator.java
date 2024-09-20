package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class Elevator {

    ElapsedTime t;
    PidControler pid;
    DcMotorEx un,doi,trei;
    double pwr;
    static int groundPos = 0, level = 0, incrementPos = 50, firstLevelPos = 20;
    private static final PIDFCoefficients MOTOR_PIDF = new PIDFCoefficients(10.0, 3.0, 0.0, 0.0);
    ArrayList<DcMotorEx> motorListElevator = new ArrayList<>();

    public void add()
    {
        motorListElevator.add(un);
        motorListElevator.add(doi);
        motorListElevator.add(trei);
    }

    public enum State{
        DOWN(groundPos), GOING_DOWN(groundPos, DOWN),
        UP(groundPos + firstLevelPos +level* incrementPos), GOING_UP(groundPos + firstLevelPos +level* incrementPos, UP);

        public int position;
        public final State nextState;
        State(int position){
            this.position = position;
            this.nextState = this;
        }
        State(int position, State nextState)
        {
            this.position = position;
            this.nextState = nextState;
        }
    }
    State state = State.DOWN;


    public Elevator(HardwareMap hm)
    {
        un = hm.get(DcMotorEx.class, "un");
        doi = hm.get(DcMotorEx.class, "doi");
        trei = hm.get(DcMotorEx.class, "trei");
        add();
        //un.setDirection(DcMotorSimple.Direction.REVERSE);
        //doi.setDirection(DcMotorSimple.Direction.REVERSE);
        //trei.setDirection(DcMotorSimple.Direction.REVERSE);

        for(DcMotorEx motorElevator : motorListElevator){
            motorElevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorElevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorElevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            MotorConfigurationType motorConfigurationType = motorElevator.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motorElevator.setMotorType(motorConfigurationType);
            motorElevator.setTargetPosition(motorElevator.getCurrentPosition());
            motorElevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorElevator.setPower(-1);
        }
    }


    public void update(){
        if(Controls.elevup && state != State.GOING_UP)
        {state = State.GOING_UP;t.reset();pid.setTargetPosition(state.position);}
        if(Controls.elevdown && state != State.GOING_DOWN)
        {state = State.GOING_DOWN;t.reset();pid.setTargetPosition(state.position);}
        switch (state) {
            case GOING_UP:
            case GOING_DOWN:
                go();
                if(t.milliseconds()>2600)
                    state = state.nextState;
                break;
            case UP:
            case DOWN:
                rest();
                break;

        }
    }
    public void go()
    {
        pwr = pid.calculate(un.getCurrentPosition());
        for(DcMotorEx motorElevator : motorListElevator) {
            motorElevator.setPower(pwr);
        }

    }
    public void rest()
    {
        for(DcMotorEx motorElevator : motorListElevator) {
            motorElevator.setPower(0);
        }
    }
    public State getState(){
        return state;
    }
}
