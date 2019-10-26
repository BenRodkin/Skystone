package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Driver Control", group = "TeleOp")
public class SLICBotTeleOp extends OpMode {

    SLICBotHardware hardware = new SLICBotHardware();

    // Button cooldowns
    ButtonCooldown gp2_a = new ButtonCooldown();
    ButtonCooldown gp2_x = new ButtonCooldown();

    public void init() {

        hardware.init(hardwareMap);
        gp2_a.setCooldown(1.000);
        gp2_x.setCooldown(1.000);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {



        // Speed Modifiers
        if(gamepad1.left_bumper) hardware.driverSpeedMod = hardware.SLOW;
        else if(gamepad1.right_bumper) hardware.driverSpeedMod = hardware.FAST;
        else hardware.driverSpeedMod = hardware.NORMAL;





        double drive = -gamepad1.left_trigger + gamepad1.right_trigger;
        double turn = -gamepad1.left_stick_x * 3 / 4;

        if(drive < 0.0) {
            hardware.setLeftPower((drive + turn) * hardware.driverSpeedMod);
            hardware.setRightPower((drive - turn) * hardware.driverSpeedMod);
        } else {
            hardware.setLeftPower((drive - turn) * hardware.driverSpeedMod);
            hardware.setRightPower((drive + turn) * hardware.driverSpeedMod);
        }

        double pulleySpeed = (gamepad2.right_trigger - gamepad2.left_trigger);

        hardware.pulley.setPower(pulleySpeed);

        hardware.arm.setPower(gamepad2.left_stick_y * 0.4);

        double runtime = getRuntime();

        if (gamepad2.a && gp2_a.ready(runtime)) {
            hardware.clamp.setPosition(Math.abs(hardware.clamp.getPosition() - 1));
            gp2_a.updateSnapshot(runtime);
        }

        if (gamepad2.x && gp2_x.ready(runtime)) {
            hardware.holder.setPosition((Math.abs(hardware.holder.getPosition() - 1)));
            gp2_x.updateSnapshot(runtime);
        }

//        if(gamepad1.a || gamepad2.a) {
//            setIntakePower(1.0);
//        } else if(gamepad1.b || gamepad2.b) {
//            setIntakePower(-1.0);
//        } else {
//            setIntakePower(0.0);
//        }

        // Deploy servo controls
        if(gamepad2.dpad_down) {        // STOWED
            hardware.deploy2.setPosition(hardware.STOWED[0]);
            hardware.deploy1.setPosition(hardware.STOWED[1]);
        } else if(gamepad2.dpad_left) { // DEPLOYED
            hardware.deploy2.setPosition(hardware.DEPLOYED[0]);
            hardware.deploy1.setPosition(hardware.DEPLOYED[1]);
        } else if(gamepad2.dpad_up) {   // GRABBING
            hardware.deploy2.setPosition(hardware.GRABBING[0]);
            hardware.deploy1.setPosition(hardware.GRABBING[1]);
        }


        telemetry.addData("Drive power", drive);
        telemetry.addData("Turn power", turn);
        telemetry.addLine();
//        telemetry.addData("Intake1 power", hardware.intake1.getPower());
//        telemetry.addData("Intake2 power", hardware.intake2.getPower());
//        telemetry.addLine();
//        telemetry.addData("Deploy1 position", hardware.deploy1.getPosition());
//        telemetry.addData("Deploy2 position", hardware.deploy2.getPosition());
        telemetry.update();
    }

    public void setIntakePower(double power) {
        hardware.intake1.setPower(power);
        hardware.intake2.setPower(power);
    }
}
