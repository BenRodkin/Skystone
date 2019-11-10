package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;


@Autonomous(name = "Test: Crop Camera", group = "Testing")
public class TestCropCamera extends LinearOpMode {

    OpenCvCamera phoneCam;
    StageSwitchingPipeline stageSwitchingPipeline;

    public double rectTop = 0.0;
    public double rectLeft = 0.0;
    public double rectBot = 0.0;
    public double rectRight = 0.0;


    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        stageSwitchingPipeline = new StageSwitchingPipeline();
        phoneCam.setPipeline(stageSwitchingPipeline);
        phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);


        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {


            


            telemetry.addLine("Running");
            telemetry.addData("rectTop",    rectTop);
            telemetry.addData("rectLeft",   rectLeft);
            telemetry.addData("rectBot",    rectBot);
            telemetry.addData("rectRight",  rectRight);
            telemetry.update();
        }



    }

    static class StageSwitchingPipeline extends OpenCvPipeline {


        @Override
        public Mat processFrame(Mat input) {

            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/4,
                            input.rows()/4),
                    new Point(
                            input.cols()*(3f/4f),
                            input.rows()*(3f/4f)),
                    new Scalar(0, 255, 0), 4);

            return input;
        }
    }

}
