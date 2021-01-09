package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.sign

@Autonomous(name = "Autonomous (yes)", group = "Concept")
class MuchCoolerAuto : LinearOpMode() {
    private var dt: DriveTrain? = null
    private var vuforia: VuforiaLocalizer? = null
    private var tfod: TFObjectDetector? = null
    private var targetZone = TargetZone.A
    private val vel: Double = (118.0 / 3000) * (1.0 / 2.54) // (cm/s) * (in/cm)
    private val radVel: Double = (2*PI - acos(22.625/23.75)) / 5000
    private var power = 0.5
    override fun runOpMode() {
        // Init
        dt = DriveTrain(hardwareMap, DcMotor.RunMode.RUN_USING_ENCODER)
        initVuforia()
        initTfod()
        if (tfod != null) {
            tfod!!.activate()
        }

        // Active
        waitForStart()
        if (opModeIsActive()) {
            // move wobble to intermediate position
            moveDist(45.0)
            // get into ring viewing position
            moveDist(-30.0)
            turnRads(-acos(23.5 / 28.5))
            // detect target zone
            sleep(1000)
            updateTargetZone()
            // undo ring viewing position
            turnRads(acos(23.5 / 28.5))
            moveDist(30.0)
            // move wobble into target zone
            when (targetZone) {
                TargetZone.A -> {
                    moveDist(20.0)
                    moveDist(-15.0)
                    turnRads(-acos(2.3/3.0))
                    moveDist(27.0)
                }
                TargetZone.B -> {
                    moveDist(15.0)
                    moveDist(-8.0)
                    turnRads(acos(2.8/3.5))
                    moveDist(9.5)
                    turnRads(-acos(3.5/3.9) - acos(2.8/3.5))
                    moveDist(28.0)
                    moveDist(-15.0)
                }
                TargetZone.C -> {
                    moveDist(64.0)
                    moveDist(-38.0)
                }
            }
        }

        // Post
        if (tfod != null) {
            tfod!!.shutdown()
        }
    }

    private fun moveDist(inch: Double) {
        val direction = sign(inch)
        val time = (abs(inch) / vel).toLong()
        dt!!.setPower(power * direction)
        sleep(time)
    }
    private fun turnRads(rads: Double) {
        val direction = sign(rads)
        val time = (abs(rads) / radVel).toLong()
        dt!!.setTurn(power * direction)
        sleep(time)
    }

    private enum class TargetZone {
        A,
        B,
        C
    }

    private fun initVuforia() {
        val parameters = VuforiaLocalizer.Parameters()
        parameters.vuforiaLicenseKey = VUFORIA_KEY
        parameters.cameraName = hardwareMap.get(WebcamName::class.java, "Webcam 1")
        vuforia = ClassFactory.getInstance().createVuforia(parameters)
    }

    private fun initTfod() {
        val tfodMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.packageName)
        val tfodParameters = TFObjectDetector.Parameters(tfodMonitorViewId)
        tfodParameters.minResultConfidence = 0.8f
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia)
        tfod!!.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT)
    }

    private fun updateTargetZone() {
        if (tfod != null) {
            val updatedRecognitions = tfod!!.updatedRecognitions
            for (recognition in updatedRecognitions) {
                when (recognition.label) {
                    LABEL_FIRST_ELEMENT -> targetZone = TargetZone.B
                    LABEL_SECOND_ELEMENT -> targetZone = TargetZone.C
                }
            }
        }
    }

    companion object {
        private const val TFOD_MODEL_ASSET = "UltimateGoal.tflite"
        private const val LABEL_FIRST_ELEMENT = "Quad"
        private const val LABEL_SECOND_ELEMENT = "Single"
        private const val VUFORIA_KEY = BuildConfig.VUFORIA_KEY
    }
}