package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Test: Sideways Gripper", group = "Testing")
public class TestSidewaysGripper extends LinearOpMode {

    // Hardware class
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Sideways gripper hardware
    Servo gripper;     // Grips the Stone. This object controls both linear actuators at once because they are wired with a Y-splitter.
    Servo wrist;    // Controls the rotation of the gripping mechanism.

    @Override
    public void runOpMode() {

        // Initialize hardware
        hardware.init(hardwareMap);

        // Initialize sideways gripper hardware
        gripper = hardwareMap.servo.get("gripper");
        wrist = hardwareMap.servo.get("wrist");

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {


            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Gripper position", gripper.getPosition());
            telemetry.addData("Wrist position", wrist.getPosition());
            telemetry.addLine();
            telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
