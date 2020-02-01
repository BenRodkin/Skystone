package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;


@TeleOp(name = "Test: Trackstar Servo", group = "Testing")
public class TestServo extends OpMode {

    Servo testServo;

    public void init() {
        testServo = hardwareMap.servo.get("found_right");

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        testServo.setPosition(testServo.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Servo position", testServo.getPosition());
        telemetry.update();
    }
}
