package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp(name = "Test: Mecanum Drive, Field Centric", group = "Testing")
public class TestMecanumFieldCentric extends OpMode {

    // IMU
    BNO055IMU imu;

    // Robot hardware
    SlippyBotHardware hardware = new SlippyBotHardware();

    @Override
    public void init() {
        // Initialize hardware
        hardware.init(hardwareMap);

        // Initialize IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        // Done!
        telemetry.addLine("Ready");
        telemetry.update();
    }

    @Override
    public void loop() {


        mecanumDriveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, heading());


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", heading());
        telemetry.update();
    }



    public void mecanumDriveFieldCentric(double x, double y, double twist, double heading) {
        // Account for the gyro heading in the drive vector
        double[] rotated = rotateVector(x, y, heading);

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

    protected float heading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}
