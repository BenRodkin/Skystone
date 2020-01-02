package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;


@TeleOp(name = "Test: TeleOp Enhancements", group = "Testing")
public class TestTeleOpEnhancements extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();
    private final boolean INIT_CAM = false;
    private final boolean INIT_IMU = true;

    public void init() {
        hardware.init(hardwareMap, INIT_CAM, INIT_IMU);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        mecanumDriveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, hardware.heading());


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", hardware.heading());
        telemetry.update();
    }


    public void mecanumDriveFieldCentric(double x, double y, double twist, double heading) {
        // Account for the gyro heading in the drive vector
        double[] rotated = rotateVector(x, y, -1 * heading);    // Heading is negated to correct vector rotation

        // We are making seperate variables here for clarity's sake
        double xIn = rotated[0];
        double yIn = rotated[1];

        // Put motor powers in an array to be normalized to largest value
        double[] motorSpeeds = {
                yIn + xIn + twist,  // Front left
                yIn - xIn - twist,  // Front right
                yIn - xIn + twist,  // Rear left
                yIn + xIn - twist   // Rear right
        };

        // Normalize so largest speed is 1.0
        normalize(motorSpeeds);


        // Set motor powers
        hardware.frontLeft  .setPower(motorSpeeds[0]);
        hardware.frontRight .setPower(motorSpeeds[1]);
        hardware.rearLeft   .setPower(motorSpeeds[2]);
        hardware.rearRight  .setPower(motorSpeeds[3]);
    }

    protected static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (Math.PI / 180.0));
        double sinA = Math.sin(angle * (Math.PI / 180.0));
        double[] out = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }


    protected static void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        if (maxMagnitude > 1.0) {
            for (int i = 0; i < wheelSpeeds.length; i++) {
                wheelSpeeds[i] /= maxMagnitude;
            }
        }
    }
}
