package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.sign

@Autonomous(name = "InitialAuto", group = "")
class InitialAuto : LinearOpMode() {
    private var dt: DriveTrain? = null
    private val vel: Double = 118.0 / 3000
    private val radVel: Double = (PI + asin(5.0/7.0)) / 3000
    private val power: Double = 0.5
    override fun runOpMode() {
        dt = DriveTrain(hardwareMap, DcMotor.RunMode.RUN_USING_ENCODER)
        dt!!.halfDtLength = 6.0
        dt!!.halfDtWidth = 6.0
        dt!!.wheelR = 2.0
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        waitForStart()
        turnRads(2.0 * PI)
        moveDist(50.0)
        // moveDist(-30.0)
        // dt!!.setPower(0.0)
    }

    private fun moveDist(cm: Double) {
        val direction = sign(cm)
        val time = (abs(cm)/vel).toLong()
        dt!!.setPower(power * direction)
        sleep(time)
    }
    private fun turnRads(rads: Double) {
        val direction = sign(rads)
        val time = (abs(rads)/radVel).toLong()
        dt!!.setTurn(power * direction)
        sleep(time)
    }
}
