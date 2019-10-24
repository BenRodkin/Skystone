package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Test: driveInches()", group = "Testing")
public class TestDriveInches extends LinearOpMode {

    SLICBotHardware hardware = new SLICBotHardware();

    public void runOpMode() {

        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {


            telemetry.addData("FL encoder", hardware.frontLeft.getCurrentPosition());
            telemetry.addData("FR encoder", hardware.frontRight.getCurrentPosition());
            telemetry.addData("RL encoder", hardware.rearLeft.getCurrentPosition());
            telemetry.addData("RR encoder", hardware.rearRight.getCurrentPosition());
            telemetry.update();
        }

    }
}
