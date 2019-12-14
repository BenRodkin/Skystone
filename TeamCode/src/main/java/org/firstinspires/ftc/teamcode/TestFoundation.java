package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

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


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("found_left position", hardware.foundLeft.getPosition());
        telemetry.addData("found_right position", hardware.foundRight.getPosition());
        telemetry.update();
    }



}
