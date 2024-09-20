package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Grippers {
    public enum State {
        OPEN,
        CLOSE
    }

    public State state=State.OPEN;
    public ColorRangeSensor sensor;
    private final Servo servo;
    int treshHold;
    public double opened, closed;
    private final ElapsedTime time = new ElapsedTime();

    public Grippers(HardwareMap hm,int th, double open, double close, int id) {
        sensor = hm.get(ColorRangeSensor.class, "sensor_color_distance" + id);
        servo = hm.get(Servo.class, "grabber" + id);
        state = State.OPEN;
        opened = open;
        closed = close;
        this.servo.setPosition(opened);
        treshHold = th;
        update();

    }

    public void update() {
        if(sensor.getDistance(DistanceUnit.MM) < treshHold)
            state = State.CLOSE;
        else
            state = State.OPEN;
        switch (state) {
            case OPEN:
                servo.setPosition(opened);
                break;
            case CLOSE:
                servo.setPosition(closed);
                break;
        }
    }
    public State getState() {
        return state;
    }
}
