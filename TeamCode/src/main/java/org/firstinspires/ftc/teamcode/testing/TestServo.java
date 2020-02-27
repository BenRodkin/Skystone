package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;


@TeleOp(name = "Test: New Servo", group = "Testing")
public class TestServo extends OpMode {

    Servo testServo;

    double pos1 = 1.00; // Wrist grabbing
    double pos2 = 0.20; // Wrist placing
    double pos3 = 0.37; // Wrist stowed

    public void init() {
        testServo = hardwareMap.servo.get("wrist");

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        testServo.setPosition(testServo.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

        if(gamepad2.a) testServo.setPosition(pos1);
        if(gamepad2.b) testServo.setPosition(pos2);
        if(gamepad2.x) testServo.setPosition(pos3);



        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Servo position", testServo.getPosition());
        telemetry.update();
    }
}
