package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

internal class DriveTrain(hardwareMap: HardwareMap, mode: DcMotor.RunMode) {
    private val dtMotors: Array<DcMotor>
    var wheelR = 0.0
    var halfDtWidth = 0.0
    var halfDtLength = 0.0
    var vx = 0.0
    var vy = 0.0
    var w0 = 0.0
    val wn: DoubleArray
    public val motorGroups: Array<Array<DcMotor>>
    private val servoGroups: Array<Array<Servo>>

    /* <w1, w2, w3, w4> = 1/r [<1, 1, 1, 1> <1, -1, -1, 1> (L1 + L2)<-1, 1, -1, 1>] <vx,vy, w0> */
    fun doLogic() {
        var max = 0.0
        for (i in 0..3) {
            var w = 0.0
            w += vx
            w += (if (i % 3 == 0) -1 else 1) * vy
            w += (if (i % 2 == 0) -1 else 1) * (halfDtLength + halfDtWidth) * w0
            wn[i] = w
            if (abs(w) > max) {
                max = abs(w)
            }
        }
        if (max > 0) {
            for (i in 0..3) {
                wn[i] /= max
            }
        }
    }

    fun handleHardware() {
        for (i in 0..3) {
            dtMotors[i].power = wn[i]
        }
    }

    fun setPower(power: Double) {
        for (i in 0..3) {
            dtMotors[i].power = power
        }
    }
    fun setTurn(power: Double) {
        for (i in 0..3) {
            val d = if (i % 2 == 0) { 1.0 } else { -1.0 }
            dtMotors[i].power = power * d
        }
    }
    fun setPos(pos: Int) {
        for (i in 0..3) {
            dtMotors[i].targetPosition = pos
        }
        for (i in 0..3) {
            while (dtMotors[i].isBusy) {
                // wait
            }
        }
    }
    fun setTurnPos(pos: Int) {
        for (i in 0..3) {
            val d = if (i % 2 == 0) { 1 } else { -1 }
            dtMotors[i].targetPosition = pos * d
        }
        for (i in 0..3) {
            while (dtMotors[i].isBusy) {
                // wait
            }
        }
    }

    init {
        val dtLeftFront = hardwareMap.get(DcMotor::class.java, "dt lf")
        val dtRightFront = hardwareMap.get(DcMotor::class.java, "dt rf")
        val dtLeftBack = hardwareMap.get(DcMotor::class.java, "dt lb")
        val dtRightBack = hardwareMap.get(DcMotor::class.java, "dt rb")
        dtMotors = arrayOf(dtLeftFront, dtRightFront, dtLeftBack, dtRightBack)
        motorGroups = arrayOf(dtMotors)
        servoGroups = arrayOf()
        for (motorGroup in motorGroups) {
            for (i in motorGroup.indices) {
                motorGroup[i].mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                motorGroup[i].direction = if (i % 2 == 0) DcMotorSimple.Direction.FORWARD else DcMotorSimple.Direction.REVERSE
                motorGroup[i].zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            }
        }
        for (servoGroup in servoGroups) {
            for (i in servoGroup.indices) {
                servoGroup[i].direction = if (i % 2 == 0) Servo.Direction.FORWARD else Servo.Direction.REVERSE
            }
        }
        wn = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
    }
}