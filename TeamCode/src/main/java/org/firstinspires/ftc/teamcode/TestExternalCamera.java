package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name = "Test: External Camera", group = "Testing")
public class TestExternalCamera extends LinearOpMode {

    SLICBotHardware hardware = new SLICBotHardware();
    WebcamName webcamName;

    public void runOpMode() {

        hardware.init(hardwareMap);
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");


        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();


        while(opModeIsActive()) {
            
        }

    }

    static class StageSwitchingPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {

//            numCols = input.cols();
//            numRows = input.rows();


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


