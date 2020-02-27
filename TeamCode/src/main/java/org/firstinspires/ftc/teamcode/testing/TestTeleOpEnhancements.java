package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;
import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.POWER_THRESHOLD;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.resetPulleys;

@Disabled
@TeleOp(name = "Test: TeleOp Enhancements", group = "Testing")
public class TestTeleOpEnhancements extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();
    private final boolean INIT_CAM = false;
    private final boolean INIT_IMU = true;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    double runtime = 0.0;

    private int armScalarLocal = 25;
    private final int ARM_STEP = 5;
    private final double ARM_POWER = 1.0;


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

        hardware.arm.setPower(ARM_POWER);
        hardware.arm.setTargetPosition(hardware.arm.getTargetPosition() + (int)(gamepad1.left_stick_y * armScalarLocal));

        hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));


        /*
            - Normal operation: setPower(pulleyPower);
            - Reset button: target = 0, run to position
            - Interrupt for reset: input from driver (Math.abs(pulleyPower) > THRESHOLD)

            if(gp2.lb) resetting = true

            if(resetting) {
                pulley.setTargetPosition(0)
                pulley.setMode(RUN_TO_POSITION)
                pulley.setPower(1.0)
            }

            if( !pulley.isBusy() || Math.abs(pulleyPower) > POWER_THRESHOLD ) {
                resetting = false

                pulley.setMode(RUN_WITHOUT_ENCODER)
                pulley.setPower(pulleyPower)
            }
         */


        if(gamepad2.left_bumper) resetPulleys = true;

        double pulleyPower = gamepad2.left_trigger - gamepad2.right_trigger;

        if(resetPulleys && Math.abs(pulleyPower) < POWER_THRESHOLD) {   // Resetting and receiving no driver pulley controls
            hardware.setPulleyTargets(0);
            hardware.setPulleyMode(DcMotor.RunMode.RUN_TO_POSITION);
            hardware.setPulleyPower(1.0);
        }

        if( !hardware.getPulleyIsBusy() || Math.abs(pulleyPower) > POWER_THRESHOLD ) { // Pulley is busy or receiving driver pulley controls
            resetPulleys = false;

            hardware.setPulleyMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            hardware.setPulleyPower(pulleyPower);
        }



        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("resetPulleys", resetPulleys);
        telemetry.addLine();
        telemetry.addData("Left pulley RunMode", hardware.pulleyLeft.getMode());
        telemetry.addData("Right pulley RunMode", hardware.pulleyRight.getMode());
        telemetry.addLine();
        telemetry.addData("Left pulley target", hardware.pulleyLeft.getTargetPosition());
        telemetry.addData("Right pulley target", hardware.pulleyRight.getTargetPosition());
        telemetry.addLine();
        telemetry.addData("Left pulley power", hardware.pulleyLeft.getPower());
        telemetry.addData("Right pulley power", hardware.pulleyRight.getPower());
        telemetry.update();
    }
}
