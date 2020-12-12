package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs

@Autonomous(name = "InitialAuto", group = "")
class InitialAuto : LinearOpMode() {
    private var dt: DriveTrain? = null
    private val vel: Double = 118.0/3000
    private val power: Double = 0.5
    override fun runOpMode() {
        dt = DriveTrain(hardwareMap, DcMotor.RunMode.RUN_USING_ENCODER)
        (dt as DriveTrain).halfDtLength = 6.0
        (dt as DriveTrain).halfDtWidth = 6.0
        (dt as DriveTrain).wheelR = 2.0
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        waitForStart()
        (dt as DriveTrain).setTurnPos(100)
        // moveDist(250.0)
        // moveDist(-30.0)
        // dt.setPower(0.0)
    }

    private fun moveDist(cm: Double) {
        val direction = abs(cm)
        val time = (cm/vel).toLong()
        (dt as DriveTrain).setPower(power * direction)
        sleep(time)
    }
}
