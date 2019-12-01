package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Test: Pulley Positions", group = "Testing")
public class TestPulleyPositions extends OpMode {
    SlippyBotHardware hardware = new SlippyBotHardware();



    @Override
    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    @Override
    public void loop() {
        
        hardware.pulleyLeft.setPower(gamepad1.left_stick_y * 0.8);
        hardware.pulleyRight.setPower(gamepad1.right_stick_y * 0.8);

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Left Pulley Position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right Pulley Position", hardware.pulleyRight.getCurrentPosition());
        telemetry.update();
    }
}
