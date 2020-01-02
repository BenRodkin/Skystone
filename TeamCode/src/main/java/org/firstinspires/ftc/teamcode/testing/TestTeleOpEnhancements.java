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

        hardware.mecanumDriveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, hardware.heading());


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", hardware.heading());
        telemetry.update();
    }
}
