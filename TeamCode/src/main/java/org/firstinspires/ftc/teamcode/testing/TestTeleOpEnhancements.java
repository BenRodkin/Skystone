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

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", hardware.heading());
        telemetry.update();
    }
}
