package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.ARM_GRABBING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.ARM_PLACING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.ARM_STARTING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_CLOSED;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_OPEN;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_GRABBING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_PLACING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_STORING;

@TeleOp(name = "Test: Sideways Gripper", group = "Testing")
public class TestSidewaysGripper extends LinearOpMode {

    // Hardware class
    SlippyBotHardware hardware = new SlippyBotHardware();

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

            switch(armMode) {
                case PLACING: hardware.wrist.setPosition(WRIST_PLACING);    break;
                case STORING: hardware.wrist.setPosition(WRIST_STORING);    break;
                case GRABBING: hardware.wrist.setPosition(WRIST_GRABBING);  break;
            }




            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("armMode", armMode);
            telemetry.addLine();
//            telemetry.addData("Gripper position", hardware.gripper.getPosition());
            telemetry.addData("Wrist position", hardware.wrist.getPosition());
            telemetry.addLine();
            telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
