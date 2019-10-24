package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Test: driveInches()", group = "Testing")
public class TestDriveInches extends LinearOpMode {

    SLICBotHardware hardware = new SLICBotHardware();

    public void runOpMode() {

        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {

            if(gamepad1.x) resetDriveEncoders();



            telemetry.addData("FL encoder", hardware.frontLeft.getCurrentPosition());
            telemetry.addData("FR encoder", hardware.frontRight.getCurrentPosition());
            telemetry.addData("RL encoder", hardware.rearLeft.getCurrentPosition());
            telemetry.addData("RR encoder", hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

    }

    public void resetDriveEncoders() {
        hardware.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
