package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.CAP_DEPLOYED;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.CAP_STOWED;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.FAST;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.GRIPPER_OPEN;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.POWER_THRESHOLD;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.SLOW;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_GRABBING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_SCALAR;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.WRIST_STORING;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.resetPulleys;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.wheelSpeedMod;

@TeleOp(name = "Mecanum Driver Control", group = "TeleOp")
public class SlippyBotTeleOp extends OpMode {

    private final double TEST_OPEN = 0.8;
    private final double TEST_CLOSED = 0.43;
    private final int PULLEY_UP_COUNTS = -600;
    private final double PULLEY_POWER = 1.0;
    private final int ARM_UP_COUNTS = -1200;
    private final double ARM_POWER = 0.3;
    private final int PULLEY_DOWN_COUNTS = 0;
    private final int ARM_DOWN_COUNTS = 0;


    SlippyBotHardware hardware = new SlippyBotHardware();


    double pulleyPower = 0.0;

    double armPower = 0.0;
//    double clampPos = 0.0;

    int capStage = 0;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    GamepadCooldowns gp1 = new GamepadCooldowns();
    double runtime = 0.0;
    boolean openGripper = false;
    boolean clamping = true;
    boolean capstone = true;
    boolean capping  = false;

    public static final double ARM_SCALAR = 0.6;

    public void init() {
        hardware.init(hardwareMap);

        hardware.pulleyLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.pulleyRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hardware.pulleyLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hardware.pulleyRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hardware.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hardware.testGripper.setPosition(GRIPPER_OPEN);
        hardware.capstone.setPosition(CAP_STOWED);


        gp2.a.setCooldown(0.250);   // 0250 milliseconds
        gp1.a.setCooldown(0.250);   // 0250 milliseconds
        gp1.lb.setCooldown(1.000);  // 1000 milliseconds
        gp1.x.setCooldown(0.752);   // 0752 milliseconds
        gp2.dpLeft.setCooldown(0.250); // 0250 milliseconds


        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        runtime = getRuntime();

        // Do the math
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double twist = -gamepad1.right_stick_x;


        pulleyPower = (gamepad2.left_trigger - gamepad2.right_trigger);

        armPower = gamepad2.left_stick_y * ARM_SCALAR;

        if (gamepad2.b) {
            hardware.intakeLeft.setPower(1.0);
            hardware.intakeRight.setPower(1.0);
        } else if (gamepad2.y) {
            hardware.intakeLeft.setPower(-1.0);
            hardware.intakeRight.setPower(-1.0);
        } else {
            hardware.intakeLeft.setPower(0.0);
            hardware.intakeRight.setPower(0.0);
        }

        if (gamepad2.a && gp2.a.ready(runtime)) {
            if (openGripper) {
                hardware.testGripper.setPosition(TEST_OPEN);
            } else {
                hardware.testGripper.setPosition(TEST_CLOSED);
            }
            openGripper = !openGripper;

            gp2.a.updateSnapshot(runtime);
        }

        hardware.wrist.setPosition(hardware.wrist.getPosition() + (gamepad2.right_stick_y * WRIST_SCALAR));

        if (gamepad1.a && gp1.a.ready(runtime)) {
            if (clamping) {
                hardware.clampFoundation();
            } else {
                hardware.releaseFoundation();
            }
            clamping = !clamping;

            gp1.a.updateSnapshot(runtime);
        }


        if (gamepad2.x) {
            hardware.wrist.setPosition(WRIST_GRABBING);
            hardware.testGripper.setPosition(GRIPPER_OPEN);
        }
        if (gamepad2.right_bumper) hardware.wrist.setPosition(WRIST_STORING);

        // Handle speed modifiers
        if (gamepad1.left_bumper && gp1.lb.ready(runtime)) {
            if (wheelSpeedMod == FAST) wheelSpeedMod = SLOW;
            else if (wheelSpeedMod == SLOW) wheelSpeedMod = FAST;

            gp1.lb.updateSnapshot(runtime);
        }


        // Set the power
        hardware.setMecanumPower(drive, strafe, twist, wheelSpeedMod);

        if(gamepad2.dpad_left && gp2.dpLeft.ready(runtime))
            capping = true;

        if(capping) {

            telemetry.addLine("Capping mode");
            telemetry.addData("Capping stage", capStage-1);

            if(gamepad2.dpad_left && gp2.dpLeft.ready(runtime)) {
                capStage = AutoCap(capStage);
                capStage++;

                gp2.dpLeft.updateSnapshot(runtime);
            }

           if(gamepad2.dpad_right){
               capping = false;

               hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
               hardware.setPulleyMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
           }
        } else {


            if (gamepad2.left_bumper) resetPulleys = true;

            double pulleyPower = gamepad2.left_trigger - gamepad2.right_trigger;

            if (resetPulleys && Math.abs(pulleyPower) < POWER_THRESHOLD) {   // Resetting and receiving no driver pulley controls
                hardware.setPulleyTargets(0);
                hardware.setPulleyMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.setPulleyPower(1.0);
            }

            if (!hardware.getPulleyIsBusy() || Math.abs(pulleyPower) > POWER_THRESHOLD) { // Pulley is busy or receiving driver pulley controls
                resetPulleys = false;

                hardware.setPulleyMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                hardware.setPulleyPower(pulleyPower);
            }

            // Set up initial state
        /*
            Lift up
            Arm up
            Capstone down
            Lift down
            Wrist
            Gripper open
            Arm down
            Capstone up
            Profit
         */

            if (gamepad2.dpad_down) hardware.capstone.setPosition(CAP_DEPLOYED);
            if (gamepad2.dpad_up) hardware.capstone.setPosition(CAP_STOWED);


            hardware.arm.setPower(armPower);
//        hardware.clamp.         setPosition(clampPos);
        }


        // Tape drive controls
        hardware.tapeMeasure.setPower(gamepad1.right_trigger - gamepad1.left_trigger);


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Wheel driver speed mod", wheelSpeedMod);
        telemetry.addLine();
        telemetry.addData("Wrist position", hardware.wrist.getPosition());
        telemetry.addLine();
        telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
        telemetry.addLine();
        telemetry.addData("Left pulley position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right pulley position", hardware.pulleyRight.getCurrentPosition());
//        telemetry.addData("Clamp position", hardware.clamp.getPosition());
        telemetry.addLine();
        telemetry.addData("Left pulley position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right pulley position", hardware.pulleyRight.getCurrentPosition());
        telemetry.addLine();
        telemetry.addData("Capstone position", hardware.capstone.getPosition());
        telemetry.update();
    }

    private int AutoCap(int stage){
        switch (stage){
            case 0 :
                hardware.stopAllMotors();
                hardware.intakeLeft.setPower(0.0);
                hardware.intakeRight.setPower(0.0);

                hardware.setPulleyTargets(PULLEY_UP_COUNTS);
                hardware.setPulleyMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.setPulleyPower(PULLEY_POWER);

                hardware.arm.setTargetPosition(ARM_UP_COUNTS);
                hardware.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.arm.setPower(ARM_POWER);

                break;
            case 1 :
                hardware.capstone.setPosition(CAP_DEPLOYED);

                hardware.setPulleyTargets(PULLEY_DOWN_COUNTS);
                hardware.setPulleyPower(PULLEY_POWER);

                break;
            case 2 :
                hardware.wrist.setPosition(WRIST_GRABBING);
                hardware.testGripper.setPosition(GRIPPER_OPEN);

                hardware.arm.setTargetPosition(ARM_DOWN_COUNTS);
                hardware.arm.setPower(ARM_POWER);

                break;
            case 3 :
                hardware.capstone.setPosition(CAP_STOWED);

                hardware.arm.setPower(0);
                hardware.setPulleyPower(0);
                hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                hardware.setPulleyMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


                break;
            default:
                return -1;

        }
        return stage;
}

}