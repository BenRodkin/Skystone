package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@Disabled
@TeleOp(name = "Test: Grippy Boi", group = "Testing")
public class TestGrippyBoi extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();


    public void init() {
        hardware.init(hardwareMap);
        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        hardware.testGripper.setPosition(hardware.testGripper.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));




        telemetry.addLine("Running");
        telemetry.addData("Test gripper pos", hardware.testGripper.getPosition());
        telemetry.update();
    }
}
