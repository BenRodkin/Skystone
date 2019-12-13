package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_CLOSED;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_OPEN;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@TeleOp(name = "Test: Sideways Gripper", group = "Testing")
public class TestSidewaysGripper extends LinearOpMode {

    // Hardware class
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Wrist positions
    private final double WRIST_PLACING  = 0.20;
    private final double WRIST_GRABBING = 0.76;
    private final double WRIST_STARTING = 0.52;
    private final double WRIST_STORING  = 0.40;

    // Arm positions (to seperate different modes of operation for the wrist)
    private final int ARM_PLACING   = 1900;
    private final int ARM_GRABBING  = 40;
    private final int ARM_STARTING  = 0;
    private final int ARM_STORING   = 200;

    ArmMode armMode = ArmMode.STARTING;


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

            hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad1.right_stick_y * WRIST_SCALAR));

            hardware.arm.setPower(gamepad1.left_stick_y * 0.6);

            // Decide which region of operation the arm is in
            int armPos = hardware.arm.getCurrentPosition();
            if(armPos >= ARM_PLACING) armMode = ArmMode.PLACING;
            else if(armPos > ARM_GRABBING) armMode = ArmMode.STORING;
            else if(armPos > ARM_STARTING &&
                        armPos < ARM_GRABBING) armMode = ArmMode.GRABBING;




            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("armMode", armMode);
            telemetry.addLine();
            telemetry.addData("Gripper position", hardware.gripper.getPosition());
            telemetry.addData("Wrist position", hardware.wrist.getPosition());
            telemetry.addLine();
            telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}