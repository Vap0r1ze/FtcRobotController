package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

class DriveTrain {
    public final DcMotor[] dtMotors;
    public double wheelR;
    public double halfDtWidth;
    public double halfDtLength;
    public double vx;
    public double vy;
    public double w0;
    public final double[] wn;
    private final DcMotor[][] motorGroups;
    private final Servo[][] servoGroups;
    DriveTrain (HardwareMap hardwareMap) {
        DcMotor dtLeftFront = hardwareMap.get(DcMotor.class, "dt lf");
        DcMotor dtRightFront = hardwareMap.get(DcMotor.class, "dt rf");
        DcMotor dtLeftBack = hardwareMap.get(DcMotor.class, "dt lb");
        DcMotor dtRightBack = hardwareMap.get(DcMotor.class, "dt rb");
        dtMotors = new DcMotor[] { dtLeftFront, dtRightFront, dtLeftBack, dtRightBack };
        motorGroups = new DcMotor[][] { dtMotors };
        servoGroups = new Servo[][] {};
        for (DcMotor[] motorGroup : motorGroups) {
            for (int i = 0; i < motorGroup.length; i++) {
                motorGroup[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorGroup[i].setDirection(i % 2 == 0 ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
                motorGroup[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
        for (Servo[] servoGroup : servoGroups) {
            for (int i = 0; i < servoGroup.length; i++) {
                servoGroup[i].setDirection(i % 2 == 0 ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
            }
        }

        wn = new double[] {0, 0, 0, 0};
    }
    /* <w1, w2, w3, w4> = 1/r [<1, 1, 1, 1> <1, -1, -1, 1> (L1 + L2)<-1, 1, -1, 1>] <vx,vy, w0> */
    void logic () {
        double max = 0;
        for (int i = 0; i < 4; i++) {
            double w = 0;
            w += vx;
            w += ((i % 3) == 0 ? -1 : 1) * vy;
            w += ((i % 2) == 0 ? -1 : 1) * (halfDtLength + halfDtWidth) * w0;
            wn[i] = w;
            if (Math.abs(w) > max) {
                max = Math.abs(w);
            }
        }
        if (max > 0) {
            for (int i = 0; i < 4; i++) {
                wn[i] /= max;
            }
        }
    }
    void mech () {
        for (int i = 0; i < 4; i++) {
            dtMotors[i].setPower(wn[i]);
        }
    }
}
