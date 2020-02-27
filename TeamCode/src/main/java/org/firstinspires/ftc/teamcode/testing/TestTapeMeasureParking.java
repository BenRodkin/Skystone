package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SlippyBotHardware;

@Disabled
@TeleOp(name = "Test: Tape Measure", group = "Testing")
public class TestTapeMeasureParking extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();


    @Override
    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready!");
        telemetry.update();
    }

    @Override
    public void loop() {

//        hardware.tapeMeasure.setPower((gamepad1.dpad_up ? .6 : 0) - (gamepad1.dpad_down ? .4 : 0));

        hardware.tapeMeasure.setPower(gamepad1.right_trigger - gamepad1.left_trigger);


        telemetry.addLine("Running");
        telemetry.addData("Tape Measure Position", hardware.tapeMeasure.getCurrentPosition());
        telemetry.update();
    }
}
