package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Manual Driving", group="Driving")

public class Controller extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode () {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        DriveTrain dt = new DriveTrain(hardwareMap);
        dt.halfDtLength = 6;
        dt.halfDtWidth = 6;
        dt.wheelR = 2;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            dt.vx = gamepad1.left_stick_x;
            dt.vy = -gamepad1.left_stick_y;
            dt.w0 = gamepad1.right_stick_x;
            dt.logic();
            dt.mech();

            telemetry.addData("<Vx, Vy, W0>", "<%.3f, %.3f, %.3f>", dt.vx, dt.vy, dt.w0);
            telemetry.addData("<W1, W2, W3, W4>", "<%.3f, %.3f, %.3f, %.3f>", dt.wn[0], dt.wn[1], dt.wn[2], dt.wn[3]);
            telemetry.update();
        }
    }
}