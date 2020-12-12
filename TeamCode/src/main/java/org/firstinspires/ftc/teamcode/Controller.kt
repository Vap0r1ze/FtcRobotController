package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name = "Manual Driving", group = "Driving")
class Controller : LinearOpMode() {
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        telemetry.addData("Status", "Initializing...")
        telemetry.update()
        val dt = DriveTrain(hardwareMap, DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        dt.halfDtLength = 6.0
        dt.halfDtWidth = 6.0
        dt.wheelR = 4.0
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        waitForStart()
        runtime.reset()
        while (opModeIsActive()) {
            dt.vx = -gamepad1.left_stick_y.toDouble()
            dt.vy = gamepad1.left_stick_x.toDouble()
            dt.w0 = gamepad1.right_stick_x.toDouble()
            dt.doLogic()
            dt.handleHardware()
            telemetry.addData("<Vx, Vy, W0>", "<%.3f, %.3f, %.3f>", dt.vx, dt.vy, dt.w0)
            telemetry.addData("<W1, W2, W3, W4>", "<%.3f, %.3f, %.3f, %.3f>", dt.wn[0], dt.wn[1], dt.wn[2], dt.wn[3])
            telemetry.update()
        }
    }
}