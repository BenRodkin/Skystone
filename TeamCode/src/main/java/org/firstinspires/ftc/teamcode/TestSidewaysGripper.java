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

    private final double GRIPPER_CLOSED = 0.0;
    private final double GRIPPER_OPEN = 1.0;

    private final double WRIST_SCALAR = 0.01;

    @Override
    public void runOpMode() {

        // Initialize hardware
        hardware.init(hardwareMap);

        // Initialize sideways gripper hardware
        gripper = hardwareMap.servo.get("gripper");
        wrist = hardwareMap.servo.get("wrist");

        /*
            We are using Actuonix PQ-12 linear actuators for the grippers as of Dec 12 2019. They
            take a signal between 1.0 milliseconds (extend; 1000 microseconds) and 2.0 milliseconds
            (retract; 2000 microseconds) according to Actuonix's datasheet. REV Smart Robot Servos,
            for comparison, take a signal between 500 microseconds (-90 degrees) and 2500
            microseconds (90 degrees), so a 2500 microseconds signal is sent when setPosition(1.0)
            is called. To correct the range for the linear actuator servos, We are setting them to
            0.4 (1000 / 2500) and 0.8 (2000 / 2500).
         */
        gripper.scaleRange(0.4, 0.8);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {

            if(gamepad1.a) gripper.setPosition(GRIPPER_CLOSED);
            else gripper.setPosition(GRIPPER_OPEN);

            wrist.setPosition(wrist.getPosition() + (gamepad1.left_stick_y * WRIST_SCALAR));


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
