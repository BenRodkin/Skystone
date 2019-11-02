package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "Test: Arm & Lift Presets", group = "Testing")
public class TestArm extends OpMode {
    SLICBotHardware hardware = new SLICBotHardware();

    ButtonCooldown gp2_a    = new ButtonCooldown();
    ButtonCooldown gp1_lb   = new ButtonCooldown();
    ButtonCooldown gp1_rb   = new ButtonCooldown();
    ButtonCooldown gp1_y    = new ButtonCooldown();


    public final int ARM_STOWED         = 0;
    public final int ARM_GRABBING       = -1200;
    public final int ARM_PLACING_LOW    = -1200;
    public final int ARM_PLACING_HIGH   = -900;

    public final int LIFT_STOWED    = 0;
    public final int LIFT_1         = 3468;
    public final int LIFT_2         = 7951;
    public final int LIFT_3         = 5879;
    public final int LIFT_4         = 9642;

    public int liftStep = 0;
    public int armStep  = 0;



    @Override
    public void init() {
        hardware.init(hardwareMap);

        gp2_a   .setCooldown(1.000);
        gp1_lb  .setCooldown(1.000);
        gp1_rb  .setCooldown(1.000);
        gp1_y   .setCooldown(1.000);

        hardware.arm.setTargetPosition(ARM_STOWED); // Default target
        hardware.pulley.setTargetPosition(LIFT_STOWED); // Default target

        hardware.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.pulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        telemetry.addLine("Ready");
        telemetry.update();
    }


    @Override
    public void loop() {
        double runtime = getRuntime();


        // Position cycling
        if(gamepad1.right_bumper && gp1_rb.ready(runtime) && liftStep < 4) {
            liftStep ++;
            gp1_rb.updateSnapshot(runtime);
        }
        if(gamepad1.left_bumper && gp1_lb.ready(runtime) && liftStep > 0) {
            liftStep --;
            gp1_lb.updateSnapshot(runtime);
        }

        if(gamepad1.y && gp1_y.ready(runtime)) {
            armStep ++;
            armStep %= 3;
            gp1_y.updateSnapshot(runtime);
        }
        // End position cycling

        hardware.arm.setPower(0.3);
        hardware.pulley.setPower(0.3);

        switch (liftStep) {
            case 0:
                hardware.pulley.setTargetPosition(LIFT_STOWED);
                break;
            case 1:
                hardware.pulley.setTargetPosition(LIFT_1);
                break;
            case 2:
                hardware.pulley.setTargetPosition(LIFT_2);
                break;
            case 3:
                hardware.pulley.setTargetPosition(LIFT_3);
                break;
            case 4:
                hardware.pulley.setTargetPosition(LIFT_4);
                break;
            default:
                hardware.pulley.setTargetPosition(LIFT_STOWED);
                break;
        }

        switch (armStep) {
            case 0:
                hardware.arm.setTargetPosition(ARM_STOWED);
                break;
            case 1:
                hardware.arm.setTargetPosition(ARM_GRABBING);
                break;
            case 2:
                if(liftStep <= 2) {
                    hardware.arm.setTargetPosition(ARM_PLACING_LOW);
                } else {
                    hardware.arm.setTargetPosition(ARM_PLACING_HIGH);
                }
                break;
            default:
                hardware.arm.setTargetPosition(ARM_STOWED);
                break;
        }



        if(gamepad2.x) {
            resetEncoder(hardware.arm);
        }
        if(gamepad2.y) {
            resetEncoder(hardware.pulley);
        }


//        if (gamepad2.a && gp2_a.ready(runtime)) {
//            hardware.clamp.setPosition(Math.abs(hardware.clamp.getPosition() - 1));
//            gp2_a.updateSnapshot(runtime);
//        }
//
//        hardware.arm.setPower(gamepad2.left_stick_y * 0.3);
//
//        double pulleySpeed = gamepad2.right_trigger - gamepad2.left_trigger;
//
//        hardware.pulley.setPower(pulleySpeed);

        telemetry.addData("Arm step", armStep);
        telemetry.addData("Lift step", liftStep);
        telemetry.addLine();
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
