package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector

@TeleOp(name = "Autonomous (yes)", group = "Concept")
class MuchCoolerAuto : LinearOpMode() {
    private var vuforia: VuforiaLocalizer? = null
    private var tfod: TFObjectDetector? = null
    private var targetZone = TargetZone.A
    override fun runOpMode() {
        // Init
        initVuforia()
        initTfod()
        if (tfod != null) {
            tfod!!.activate()
        }

        // Active
        waitForStart()
        if (opModeIsActive()) {
            // TODO: move wobble to intermediary position
            // TODO: get into ring viewing position
            updateTargetZone()
            // TODO: undo ring viewing position
            // TODO: move wobble into target zone
        }

        // Post
        if (tfod != null) {
            tfod!!.shutdown()
        }
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