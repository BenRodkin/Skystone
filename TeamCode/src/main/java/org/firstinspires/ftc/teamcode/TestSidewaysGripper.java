package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_CLOSED;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_OPEN;

@TeleOp(name = "Test: Sideways Gripper", group = "Testing")
public class TestSidewaysGripper extends LinearOpMode {

    // Hardware class
    SlippyBotHardware hardware = new SlippyBotHardware();

    private final double WRIST_SCALAR = 0.01;

    @Override
    public void runOpMode() {

        // Initialize hardware
        hardware.init(hardwareMap);


        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {

            if(gamepad1.a) hardware.gripper.setPosition(GRIPPER_CLOSED);
            else hardware.gripper.setPosition(GRIPPER_OPEN);

            hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad1.left_stick_y * WRIST_SCALAR));


            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Gripper position", hardware.gripper.getPosition());
            telemetry.addData("Wrist position", hardware.wrist.getPosition());
            telemetry.addLine();
            telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
