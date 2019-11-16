package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name = "Test: Skystone Pipeline", group = "Testing")
public class TestSkystonePipeline extends LinearOpMode {

    OpenCvCamera phoneCam;
    SkystonePatternPipeline skystonePatternPipeline;


    public void runOpMode() {


        waitForStart();


        while(opModeIsActive()) {

        }
    }

    static class SkystonePatternPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {

            

            return input;
        }
    }
}
