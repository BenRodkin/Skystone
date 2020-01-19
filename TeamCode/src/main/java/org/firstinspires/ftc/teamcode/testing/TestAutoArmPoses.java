package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;
import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@Autonomous(name = "Test: Auto Arm Poses", group = "Testing")
public class TestAutoArmPoses extends LinearOpMode {

    // State variables
    private final double TEST_OPEN = 0.0;
    private final double TEST_CLOSED = 1.0;

    double armPower = 0.0;

    private final double ARM_SCALAR = 0.6;

    // Hardware
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Button cooldowns
    GamepadCooldowns gp2 = new GamepadCooldowns();
    private double runtime = 0.0;
    private boolean gripOpen = true;

    public void runOpMode() {

        hardware.init(hardwareMap); // Default to no camera and no imu initialization

        gp2.a.setCooldown(1.000);   // 1000 milliseconds

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {

            runtime = getRuntime();

            // Wrist
            hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

            // Gripper
            if(gamepad2.a && gp2.a.ready(runtime)) {
                if(gripOpen) {
                    hardware.testGripper.setPosition(TEST_OPEN);
                } else {
                    hardware.testGripper.setPosition(TEST_CLOSED);
                }
                gripOpen = !gripOpen;

                gp2.a.updateSnapshot(runtime);
            }

            // Arm
            armPower = gamepad2.left_stick_y * ARM_SCALAR;
            hardware.arm.setPower(armPower);


            // Intake
            if(gamepad2.b) {
                hardware.intakeLeft.setPower(1.0);
                hardware.intakeRight.setPower(1.0);
            } else if(gamepad2.y) {
                hardware.intakeLeft.setPower(-1.0);
                hardware.intakeRight.setPower(-1.0);
            } else {
                hardware.intakeLeft.setPower(0.0);
                hardware.intakeRight.setPower(0.0);
            }


            // Telemetry
            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Wrist pos", hardware.wrist.getPosition());
            telemetry.update();
        }

    }
}
