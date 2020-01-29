package org.firstinspires.ftc.teamcode.testing;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;
import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_GRABBING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@TeleOp(name = "Test: Arm Wrist Sync", group = "Testing")
public class TestTeleOpArmWristSync extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();
    private final boolean INIT_CAM = false;
    private final boolean INIT_IMU = true;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    double runtime = 0.0;

    private int armScalarLocal = 25;
    private final int ARM_STEP = 5;
    private final double ARM_POWER = 1.0;
    private final double WRIST_SCALAR = 0.4;

    private final double[][] DRIVER_ENCHANCE_ENCODER_VALUES = new double[][]{{},{},{}};

    private final double GRIP_MAX = 1.0;
    private final double GRIP_MIN = 0.3;
    public void init() {
        hardware.init(hardwareMap, INIT_CAM, INIT_IMU);

        hardware.arm.setTargetPosition(0);
        hardware.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        gp2.dpUp.setCooldown(0.500);    // 500ms
        gp2.dpDown.setCooldown(0.500);  // 500ms

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        runtime = getRuntime();

        hardware.mecanumDriveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, hardware.heading());

//        hardware.arm.setPower(ARM_POWER);
//        hardware.arm.setTargetPosition(hardware.arm.getTargetPosition() + (int)(gamepad2.left_stick_y * armScalarLocal));
//
//        if(gamepad2.x)
//            hardware.wrist.setPosition(WRIST_GRABBING);
//        else
//            hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

        hardware.arm.setPower(gamepad2.left_stick_y * ARM_POWER);

        //hardware.wrist.setPosition(hardware.arm.getCurrentPosition()/100 * WRIST_SCALAR);

        //hardware.testGripper.setPosition(gamepad2.right_stick_y);
        hardware.testGripper.setPosition(gamepad2.x ? GRIP_MAX : GRIP_MIN);

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", hardware.heading());
        telemetry.addLine();
        telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
        telemetry.addData("Arm target", hardware.arm.getTargetPosition());
        telemetry.addData("Arm scalar (local)", armScalarLocal);
        telemetry.addLine();
        telemetry.addData("Wrist Position", hardware.wrist.getPosition());
        telemetry.addLine();
        telemetry.addData("Gripper Position", hardware.testGripper.getPosition());
        telemetry.update();
    }
}