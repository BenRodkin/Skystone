package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;
import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;

@TeleOp(name = "Test: TeleOp Enhancements", group = "Testing")
public class TestTeleOpEnhancements extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();
    private final boolean INIT_CAM = false;
    private final boolean INIT_IMU = true;

    private double wristScalarLocal = 0.008;
    private final double WRIST_STEP = 0.0005;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    double runtime = 0.0;


    public void init() {
        hardware.init(hardwareMap, INIT_CAM, INIT_IMU);

        gp2.dpUp.setCooldown(0.500);    // 500ms
        gp2.dpDown.setCooldown(0.500);  // 500ms

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        runtime = getRuntime();

        hardware.mecanumDriveFieldCentric(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, hardware.heading());

        // Dpad up increases wrist scalar
        hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

        if(gamepad2.dpad_up && gp2.dpUp.ready(runtime)) {
            wristScalarLocal += WRIST_STEP;

            gp2.dpUp.updateSnapshot(runtime);
        }

        // Dpad down decreases wrist scalar
        if(gamepad2.dpad_down && gp2.dpDown.ready(runtime)){
            wristScalarLocal -= WRIST_STEP;

            gp2.dpDown.updateSnapshot(runtime);
        }

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Heading", hardware.heading());
        telemetry.addLine();
        telemetry.addData("Wrist pos", hardware.wrist.getPosition());
        telemetry.addData("Wrist scalar (local)", wristScalarLocal);
        telemetry.update();
    }
}
