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

    GamepadCooldowns gp1 = new GamepadCooldowns();

    public double rectTop   = 0.0;
    public double rectLeft  = 0.0;
    public double rectBot   = 0.0;
    public double rectRight = 0.0;

    public boolean grabbingTopLeft = false;


    public final double TRIGGER_THRESHOLD = 0.7;

    public final double RECT_STEP = 1.0;
    public final double RECT_MIN = 0.0;

    public static int numCols = -1;
    public static int numRows = -1;

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

            double runtime = getRuntime();


            //////////////////////////////////////////////////
            // START CROP CONTROLS
            //////////////////////////////////////////////////

            /*
                Controls:
                    - Holding gp1.lt grabs top left corner
                    - Releasing gp1.lt grabs bottom right corner
                    - Dpad controls:
                        ~ gp1.dp_up:    move horizontal bound up
                        ~ gp1.dp_down:  move horizontal bound down
                        ~ gp1.dp_left:  move vertical bound left
                        ~ gp1.dp_right: move vertical bound right
                    - Bounds moved depends on which corner is grabbed
                        ~ top left corner means top and left move, similar for bottom right corner
             */

            if(gamepad1.dpad_up && gp1.dpUp.ready(runtime)) {
                if(grabbingTopLeft) rectTop     = trim(rectTop - RECT_STEP, RECT_MIN, numCols);
                else                rectBot     = trim(rectBot - RECT_STEP, RECT_MIN, numCols);
                gp1.dpUp.updateSnapshot(runtime);
            } else if(gamepad1.dpad_down && gp1.dpDown.ready(runtime)) {
                if(grabbingTopLeft) rectTop     = trim(rectTop + RECT_STEP, RECT_MIN, numCols);
                else                rectBot     = trim(rectBot + RECT_STEP, RECT_MIN, numCols);
                gp1.dpDown.updateSnapshot(runtime);
            }

            if(gamepad1.dpad_left && gp1.dpLeft.ready(runtime)) {
                if(grabbingTopLeft) rectLeft    = trim(rectLeft - RECT_STEP, RECT_MIN, numRows);
                else                rectRight   = trim(rectRight - RECT_STEP, RECT_MIN, numRows);
                gp1.dpLeft.updateSnapshot(runtime);
            } else if(gamepad1.dpad_right && gp1.dpRight.ready(runtime)) {
                if(grabbingTopLeft) rectLeft    = trim(rectLeft + RECT_STEP, RECT_MIN, numRows);
                else                rectRight   = trim(rectRight + RECT_STEP, RECT_MIN, numRows);
                gp1.dpRight.updateSnapshot(runtime);
            }

            grabbingTopLeft = (gamepad1.left_trigger > TRIGGER_THRESHOLD); // True if left trigger is held down

            //////////////////////////////////////////////////
            // END CROP CONTROLS
            //////////////////////////////////////////////////




            telemetry.addLine("Running");
            telemetry.addData("rectTop",    rectTop);
            telemetry.addData("rectLeft",   rectLeft);
            telemetry.addData("rectBot",    rectBot);
            telemetry.addData("rectRight",  rectRight);
            telemetry.addLine();
            telemetry.addData("grabbingTopLeft", grabbingTopLeft);
            telemetry.addLine();
            telemetry.addData("Columns", numCols);
            telemetry.addData("Rows", numRows);
            telemetry.update();
        }



    }

    public double trim(double input, double min, double max) {
        if(input < min) input = min;
        if(input > max) input = max;
        return input;
    }

    static class StageSwitchingPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {

            numCols = input.cols();
            numRows = input.rows();


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
