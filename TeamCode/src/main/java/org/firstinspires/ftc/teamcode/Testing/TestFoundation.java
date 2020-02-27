package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

@Disabled
@TeleOp(name = "Test: Foundation grabbers", group = "Testing")
public class TestFoundation extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();


    public void init() {

        // Hardware
        hardware.init(hardwareMap); // Default to false for camera and IMU

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        if(gamepad1.x) hardware.clampFoundation();
        if(gamepad1.y) hardware.releaseFoundation();


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("found_left position", hardware.foundLeft.getPosition());
        telemetry.addData("found_right position", hardware.foundRight.getPosition());
        telemetry.update();
    }



}
