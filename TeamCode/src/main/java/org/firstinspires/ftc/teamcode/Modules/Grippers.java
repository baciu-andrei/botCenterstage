package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Grippers {
    public enum State {
        OPEN,
        CLOSE
    }

    public State state;
    public ColorSensor sensor;
    private Servo servo;
    public double opened, closed;
    private final ElapsedTime time = new ElapsedTime();

    public Grippers(HardwareMap hm, ColorSensor sensor, Servo servo, int treshHold, double open, double close, int id) {
        sensor = hm.get(ColorSensor.class, "sensor_color_distance" + id);
        servo = hm.get(Servo.class, "g" + id);
        this.sensor = sensor;
        this.servo = servo;
        state = State.OPEN;
        opened = open;
        closed = close;
        this.servo.setPosition(opened);

        update();

    }

    public void update() {
        switch (state) {
            case OPEN:
                servo.setPosition(opened);
                break;
            case CLOSE:
                servo.setPosition(closed);
                break;
        }
    }
}
