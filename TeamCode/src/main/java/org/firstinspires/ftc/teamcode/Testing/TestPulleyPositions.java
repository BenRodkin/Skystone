package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

@Disabled
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

        if(gamepad1.b)
            resetPulleyEncoders(hardware.pulleyLeft.getMode(), hardware.pulleyRight.getMode());

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Left Pulley Position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right Pulley Position", hardware.pulleyRight.getCurrentPosition());
        telemetry.update();
    }

    public void resetPulleyEncoders(DcMotor.RunMode runModeLeft, DcMotor.RunMode runModeRight) {
        hardware.pulleyLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.pulleyRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hardware.pulleyLeft.setMode(runModeLeft);
        hardware.pulleyRight.setMode(runModeRight);
    }
}
