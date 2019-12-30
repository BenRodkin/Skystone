package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.FAST;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.SLOW;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_GRABBING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.wheelSpeedMod;

@TeleOp(name = "Mecanum Driver Control", group = "TeleOp")
public class SlippyBotTeleOp extends OpMode {

    private final double TEST_OPEN = 0.0;
    private final double TEST_CLOSED = 1.0;


    SlippyBotHardware hardware = new SlippyBotHardware();

    double flPower = 0.0;
    double frPower = 0.0;
    double rlPower = 0.0;
    double rrPower = 0.0;

    double pullyPower = 0.0;

    double armPower = 0.0;
//    double clampPos = 0.0;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    GamepadCooldowns gp1 = new GamepadCooldowns();
    double runtime = 0.0;

    public static final double ARM_SCALAR = 0.6;

    public void init() {
        hardware.init(hardwareMap);

        gp2.a.setCooldown(1.000);   // 1000 milliseconds
        gp1.a.setCooldown(1.000);   // 1000 milliseconds
        gp1.lb.setCooldown(1.000);  // 1000 milliseconds

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        runtime = getRuntime();

        // Do the math
        double drive  = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double twist  = -gamepad1.right_stick_x;

        flPower = (drive + strafe + twist);
        frPower = (drive - strafe - twist);
        rlPower = (drive - strafe + twist);
        rrPower = (drive + strafe - twist);

        pullyPower = (gamepad2.left_trigger - gamepad2.right_trigger);

        armPower = gamepad2.left_stick_y * ARM_SCALAR;

        if(gamepad2.b) {
            hardware.intakeLeft.setPower(1.0);
            hardware.intakeRight.setPower(1.0);
        } else if(gamepad2.y) {
            hardware.intakeLeft.setPower(-1.0);
            hardware.intakeRight.setPower(-1.0);
        } else {
            hardware.intakeLeft.setPower(0.0);
            hardware.intakeRight.setPower(0.0);
        }

//        if(gamepad2.a && gp2.a.ready(runtime)) {
//            clampPos = Math.abs(1 - clampPos);
//            gp2.a.updateSnapshot(runtime);
//        }

//        if(gamepad2.a && gp2.a.ready(runtime)) {
////            hardware.gripper.setPosition(Math.abs(1 - hardware.gripper.getPosition()));
//            gp2.a.updateSnapshot(runtime);
//        }
//        else {
////            hardware.gripper.setPosition(hardware.gripper.getPosition());   // Always send a new setPosition() command each loop because linear actuators give up
//        }
        if(gamepad2.a)  hardware.testGripper.setPosition(TEST_OPEN);
        else            hardware.testGripper.setPosition(TEST_CLOSED);

        hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

        if(gamepad1.a && gp1.a.ready(runtime)) {
            hardware.foundLeft.setPosition(Math.abs(1 - hardware.foundLeft.getPosition()));
            hardware.foundRight.setPosition(Math.abs(1 - hardware.foundRight.getPosition()));

            gp2.a.updateSnapshot(runtime);
        }

        if(gamepad2.x) hardware.wrist.setPosition(WRIST_GRABBING);



        // Handle speed modifiers
        if(gamepad1.left_bumper && gp1.lb.ready(runtime)) {
            if(wheelSpeedMod == FAST) wheelSpeedMod = SLOW;
            else if(wheelSpeedMod == SLOW) wheelSpeedMod = FAST;

            gp1.lb.updateSnapshot(runtime);
        }


        // Set the power
        hardware.frontLeft. setPower(flPower * wheelSpeedMod);
        hardware.frontRight.setPower(frPower * wheelSpeedMod);
        hardware.rearLeft.  setPower(rlPower * wheelSpeedMod);
        hardware.rearRight. setPower(rrPower * wheelSpeedMod);

        hardware.pulleyLeft.    setPower(pullyPower);
        hardware.pulleyRight.   setPower(pullyPower);

        hardware.arm.           setPower(armPower);
//        hardware.clamp.         setPosition(clampPos);


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Wheel driver speed mod", wheelSpeedMod);
        telemetry.addLine();
        telemetry.addData("Wrist position", hardware.wrist.getPosition());
        telemetry.addLine();
        telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
        telemetry.addLine();
//        telemetry.addData("Clamp position", hardware.clamp.getPosition());
        telemetry.addLine();
        telemetry.addData("Left pulley position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right pulley position", hardware.pulleyRight.getCurrentPosition());
        telemetry.update();
    }
}
