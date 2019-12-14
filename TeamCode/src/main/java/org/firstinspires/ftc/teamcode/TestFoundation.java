package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Test: Foundation grabbers", group = "Testing")
public class TestFoundation extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    public void init() {
        hardware.init(hardwareMap); // Default to false for camera and IMU

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {


        telemetry.addLine("Running");
        telemetry.update();
    }
}
