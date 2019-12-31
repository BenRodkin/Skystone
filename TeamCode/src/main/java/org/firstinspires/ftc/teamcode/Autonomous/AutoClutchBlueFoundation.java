package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;


@Autonomous(name = "COMP: BLUE FOUNDATION", group = "COMP")
public class AutoClutchBlueFoundation extends LinearOpMode {

    // Hardware
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Hardware class initialization variables
    private final boolean INIT_CAMERA = false;
    private final boolean INIT_IMU = true;

    // Drive motor speeds
    private final double DRIVE_SPEED = 0.5;
    private final double STRAFE_SPEED = 0.8;

    // Variables for sleeping at start
    private final boolean SLEEP_AT_START = false;
    private final int SLEEP_TIME_MILLIS = 10000;    // 10,000 milliseconds = 10.0 seconds. May not finish if above 17 seconds.


    public void runOpMode() {

        hardware.init(hardwareMap, INIT_CAMERA, INIT_IMU);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        // Raise foundation servos in preparation for grabbing Foundation
        hardware.releaseFoundation();

        // Optional pause at start for compliance with alliance partner strategy
        if (SLEEP_AT_START) sleep(SLEEP_TIME_MILLIS);

        driveInches(24.0);

        strafeEncoderCounts(-500, STRAFE_SPEED);

        driveInches(6.5, DRIVE_SPEED);

        hardware.setLeftPower(0.2);
        hardware.setRightPower(0.2);

        sleep(700);
        hardware.clampFoundation();
        sleep(150);
        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        driveInches(-42.0);


        hardware.releaseFoundation();
        sleep(150);

        strafeEncoderCounts(2500, STRAFE_SPEED);



        while (opModeIsActive()) {
            telemetry.addLine("Running");
            telemetry.update();
        }
    }


    // Encoder-controlled movement
    private void driveInches ( double inches){
        driveInches(inches, DRIVE_SPEED);   // Defaults to local field member speed
    }
    private void driveInches ( double inches, double speed){
        driveEncoderCounts((int) (inches * hardware.COUNTS_PER_INCH_EMPIRICAL), speed);
    }

    private void driveEncoderCounts ( int counts, double speed){
        hardware.setDriveCounts(counts);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while (opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder", hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder", hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left encoder", hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right encoder", hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void strafeEncoderCounts ( int counts, double speed){
        hardware.setStrafeCounts(counts);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while (opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder", hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder", hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left encoder", hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right encoder", hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
