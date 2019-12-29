package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp(name = "Test: Slippy arm", group = "Testing")
public class TestSlippyArm extends OpMode {
    DcMotor arm;

    public void init() {
        arm = hardwareMap.dcMotor.get("arm");

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {
        arm.setPower(gamepad1.left_stick_y * 0.5);

        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Arm power", arm.getPower());
        telemetry.update();
    }


}
