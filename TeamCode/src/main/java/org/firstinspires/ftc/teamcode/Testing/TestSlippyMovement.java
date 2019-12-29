package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

@Disabled
@Autonomous(name = "Test: Slippy Movement", group = "Testing")
public class TestSlippyMovement extends LinearOpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    private final double DRIVE_SPEED = 0.5;


    @Override
    public void runOpMode() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {

            // Movement testing controls
            // Convention: forward and right (from the robot's perspective) are positive movement
            if(gamepad1.dpad_up)    driveEncoderCounts(1000, DRIVE_SPEED);   // Drive forward
            if(gamepad1.dpad_right) strafeEncoderCounts(1000, DRIVE_SPEED);  // Strafe right
            if(gamepad1.dpad_down)  driveEncoderCounts(-1000, DRIVE_SPEED);  // Drive backward
            if(gamepad1.dpad_left)  strafeEncoderCounts(-1000, DRIVE_SPEED); // Strafe left

            if(gamepad1.left_bumper) driveInches(24.0, DRIVE_SPEED);


            telemetry.addData("Front left position",    hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right position",   hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left position",     hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right position",    hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }
    }

    private void driveInches(double inches, double speed) {
        driveEncoderCounts((int)(inches * hardware.COUNTS_PER_INCH_EMPIRICAL), speed);
    }

    private void driveEncoderCounts(int counts, double speed) {
        hardware.frontLeft.setTargetPosition    (hardware.frontLeft.getCurrentPosition()    + counts);
        hardware.frontRight.setTargetPosition   (hardware.frontRight.getCurrentPosition()   + counts);
        hardware.rearLeft.setTargetPosition     (hardware.rearLeft.getCurrentPosition()     + counts);
        hardware.rearRight.setTargetPosition    (hardware.rearRight.getCurrentPosition()    + counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder",     hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder",    hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left encoder",      hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right encoder",     hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void strafeEncoderCounts(int counts, double speed) {
        hardware.frontLeft.setTargetPosition    (hardware.frontLeft.getCurrentPosition()    + counts);
        hardware.frontRight.setTargetPosition   (hardware.frontRight.getCurrentPosition()   - counts);
        hardware.rearLeft.setTargetPosition     (hardware.rearLeft.getCurrentPosition()     - counts);
        hardware.rearRight.setTargetPosition    (hardware.rearRight.getCurrentPosition()    + counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder",     hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder",    hardware.frontRight.getCurrentPosition());
            telemetry.addData("Rear left encoder",      hardware.rearLeft.getCurrentPosition());
            telemetry.addData("Rear right encoder",     hardware.rearRight.getCurrentPosition());
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
