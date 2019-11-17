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

    public final int IMG_WIDTH = 480;
    public final int IMG_HEIGHT = 640;

    public static double rectTop   = 0.0;
    public static double rectLeft  = 0.0;
    public static double rectBot   = 0.0;
    public static double rectRight = 0.0;

    public boolean grabbingTopLeft = false;


    public final double TRIGGER_THRESHOLD = 0.7;

    public final double RECT_STEP = 4.0;
    public final double RECT_MIN = 0.0;


    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        stageSwitchingPipeline = new StageSwitchingPipeline();
        phoneCam.setPipeline(stageSwitchingPipeline);
        phoneCam.startStreaming(IMG_HEIGHT, IMG_WIDTH, OpenCvCameraRotation.UPRIGHT);


        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {

            double runtime = getRuntime();


            //////////////////////////////////////////////////
            // START CROP CONTROLS
            //////////////////////////////////////////////////

            /*
                OLD Controls: (grabbing corner and dpad controls)
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

//            if(gamepad1.dpad_up && gp1.dpUp.ready(runtime)) {
//                if(grabbingTopLeft) rectTop     = trim(rectTop - RECT_STEP, RECT_MIN, IMG_HEIGHT);
//                else                rectBot     = trim(rectBot - RECT_STEP, RECT_MIN, IMG_HEIGHT);
//                gp1.dpUp.updateSnapshot(runtime);
//            } else if(gamepad1.dpad_down && gp1.dpDown.ready(runtime)) {
//                if(grabbingTopLeft) rectTop     = trim(rectTop + RECT_STEP, RECT_MIN, IMG_HEIGHT);
//                else                rectBot     = trim(rectBot + RECT_STEP, RECT_MIN, IMG_HEIGHT);
//                gp1.dpDown.updateSnapshot(runtime);
//            }
//
//            if(gamepad1.dpad_left && gp1.dpLeft.ready(runtime)) {
//                if(grabbingTopLeft) rectLeft    = trim(rectLeft - RECT_STEP, RECT_MIN, IMG_WIDTH);
//                else                rectRight   = trim(rectRight - RECT_STEP, RECT_MIN, IMG_WIDTH);
//                gp1.dpLeft.updateSnapshot(runtime);
//            } else if(gamepad1.dpad_right && gp1.dpRight.ready(runtime)) {
//                if(grabbingTopLeft) rectLeft    = trim(rectLeft + RECT_STEP, RECT_MIN, IMG_WIDTH);
//                else                rectRight   = trim(rectRight + RECT_STEP, RECT_MIN, IMG_WIDTH);
//                gp1.dpRight.updateSnapshot(runtime);
//            }
//
//            grabbingTopLeft = (gamepad1.left_trigger > TRIGGER_THRESHOLD); // True if left trigger is held down

            //////////////////////////////////////////////////
            // END CROP CONTROLS
            //////////////////////////////////////////////////
            /*
                NEW Controls: (left stick and right stick configuration)
                    - Left stick: change top-left corner values relative to
                        ~ left_stick_x (changes left bound)
                        ~ left_stick_y (changes top bound)
                    - Right stick: change bottom-right corner values relative to
                        ~ right_stick_x (changes right bound)
                        ~ right_stick_y (changes bottom bound)
             */

            rectTop     += gamepad2.left_stick_y * RECT_STEP;
            rectLeft    += gamepad2.left_stick_x * RECT_STEP;

            rectBot     += gamepad2.right_stick_y * RECT_STEP;
            rectRight   += gamepad2.right_stick_x * RECT_STEP;




            telemetry.addLine("Running");
            telemetry.addData("rectTop",    rectTop);
            telemetry.addData("rectLeft",   rectLeft);
            telemetry.addData("rectBot",    rectBot);
            telemetry.addData("rectRight",  rectRight);
            telemetry.addLine();
            telemetry.addData("grabbingTopLeft", grabbingTopLeft);
            telemetry.addLine();
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

            Imgproc.rectangle(
                    input,
                    new Point(  // Top left corner
                            rectLeft,   // Left value
                            rectTop),   // Top value
                    new Point( // Bottom right corner
                            rectRight,  // Right value
                            rectBot),   // Bottom value
                    new Scalar(0, 255, 0), 4); // Line color and thickness

            return input;
        }
    }

}
