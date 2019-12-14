package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Test: Foundation grabbers", group = "Testing")
public class TestFoundation extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    Servo found_left;
    Servo found_right;

    private final double CLAMP_LEFT     = 0.0;
    private final double RELEASE_LEFT   = 1.0;

    private final double CLAMP_RIGHT    = 1.0;
    private final double RELEASE_RIGHT  = 0.0;



    public void init() {
        hardware.init(hardwareMap); // Default to false for camera and IMU

        found_left  = hardwareMap.servo.get("found_left");
        found_right = hardwareMap.servo.get("found_right");

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("found_left position", found_left.getPosition());
        telemetry.addData("found_right position", found_right.getPosition());
        telemetry.update();
    }


    public void clampFoundation() {
        found_left.setPosition(CLAMP_LEFT);
        found_right.setPosition(CLAMP_RIGHT);
    }

    public void releaseFoundation() {
        found_left.setPosition(RELEASE_LEFT);
        found_right.setPosition(RELEASE_RIGHT);
    }
}
