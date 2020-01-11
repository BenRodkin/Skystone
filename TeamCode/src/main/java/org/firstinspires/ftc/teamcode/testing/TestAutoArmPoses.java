package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@Autonomous(name = "Test: Auto Arm Poses", group = "Testing")
public class TestAutoArmPoses extends LinearOpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    public void runOpMode() {

        hardware.init(hardwareMap); // Default to no camera and no imu initialization

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {
            hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));


            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Wrist pos", hardware.wrist.getPosition());
            telemetry.update();
        }

    }
}
