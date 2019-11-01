package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "Test: Arm & Lift Presets", group = "Testing")
public class TestArm extends OpMode {
    SLICBotHardware hardware = new SLICBotHardware();

    @Override
    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
    }


    @Override
    public void loop() {

        if(gamepad2.x) {
            resetEncoder(hardware.arm);
        }
        if(gamepad2.y) {
            resetEncoder(hardware.pulley);
        }


        hardware.arm.setPower(gamepad2.left_stick_y * 0.3);

        double pulleySpeed = gamepad2.right_trigger - gamepad2.left_trigger;

        hardware.pulley.setPower(pulleySpeed);


        telemetry.addData("Arm Position", hardware.arm.getCurrentPosition());
        telemetry.addData("Lift Position", hardware.pulley.getCurrentPosition());
        telemetry.update();


    }

    public void resetEncoder(DcMotor motor){
        DcMotor.RunMode runMode = motor.getMode();
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(runMode);
    }

}
