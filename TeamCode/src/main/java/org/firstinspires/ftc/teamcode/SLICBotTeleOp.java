package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Driver Control", group = "TeleOp")
public class SLICBotTeleOp extends OpMode {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor intake1;
    public DcMotor intake2;

    public DcMotor pulley;

    public DcMotor arm;

    public Servo deploy1;
    public Servo deploy2;

    public Servo clamp;

    public Servo holder;

    // Deploy servo positions (index 0 is the left servo, index 1 is the right)
    public final double[] STOWED =      {0.0, 0.0};
    public final double[] DEPLOYED =    {1.0, 1.0};
    public final double[] GRABBING =    {0.5, 0.4};

    public final boolean BRAKE_ON_ZERO = true;

    // Speed modifiers
    public final double SLOW = 0.4;
    public final double NORMAL = 0.8;
    public final double FAST = 1.0;

    public double driverSpeedMod = NORMAL;



    // Button cooldowns
    ButtonCooldown gp2_a = new ButtonCooldown();
    ButtonCooldown gp2_x = new ButtonCooldown();

    public void init() {
        frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");


        intake1     = hardwareMap.dcMotor.get("intake1");
        intake2     = hardwareMap.dcMotor.get("intake2");

        pulley      = hardwareMap.dcMotor.get("pulley");

        arm         = hardwareMap.dcMotor.get("arm");

        deploy1     = hardwareMap.servo.get("deploy1");
        deploy2     = hardwareMap.servo.get("deploy2");

        clamp       = hardwareMap.servo.get("clamp");

        holder      = hardwareMap.servo.get("holder");



        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

        pulley.setDirection(DcMotorSimple.Direction.REVERSE);

        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if(BRAKE_ON_ZERO) {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        }

        gp2_a.setCooldown(1.000);
        gp2_x.setCooldown(1.000);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {



        // Speed Modifiers
        if(gamepad1.left_bumper) driverSpeedMod = SLOW;
        else if(gamepad1.right_bumper) driverSpeedMod = FAST;
        else driverSpeedMod = NORMAL;





        double drive = gamepad1.left_trigger - gamepad1.right_trigger;
        double turn = gamepad1.left_stick_x * 3 / 4;

        double speedMod = 1.0;

        if(drive > 0.0) {
            setLeftPower((drive + turn) * speedMod);
            setRightPower((drive - turn) * speedMod);
        } else {
            setLeftPower((drive - turn) * speedMod);
            setRightPower((drive + turn) * speedMod);
        }

        double pulleySpeed = (gamepad2.right_trigger - gamepad2.left_trigger);
        telemetry.addData("motor position", pulley.getCurrentPosition());

        pulley.setPower(pulleySpeed);

        arm.setPower(gamepad2.left_stick_y * 0.4);

        double runtime = getRuntime();

        if (gamepad2.a && gp2_a.ready(runtime)) {
            clamp.setPosition(Math.abs(clamp.getPosition() - 1));
            gp2_a.updateSnapshot(runtime);
        }

        if (gamepad2.x && gp2_x.ready(runtime)) {
            holder.setPosition((Math.abs(holder.getPosition() - 1)));
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
            deploy2.setPosition(STOWED[0]);
            deploy1.setPosition(STOWED[1]);
        } else if(gamepad2.dpad_left) { // DEPLOYED
            deploy2.setPosition(DEPLOYED[0]);
            deploy1.setPosition(DEPLOYED[1]);
        } else if(gamepad2.dpad_up) {   // GRABBING
            deploy2.setPosition(GRABBING[0]);
            deploy1.setPosition(GRABBING[1]);
        }


        telemetry.addData("Drive power", drive);
        telemetry.addData("Turn power", turn);
        telemetry.addLine();
        telemetry.addData("Intake1 power", intake1.getPower());
        telemetry.addData("Intake2 power", intake2.getPower());
        telemetry.addLine();
        telemetry.addData("Deploy1 position",deploy1.getPosition());
        telemetry.addData("Deploy2 position",deploy2.getPosition());
        telemetry.update();
    }

    public void setLeftPower(double power) {
        rearLeft.setPower(power);
        frontLeft.setPower(power);

    }

    public void setRightPower(double power) {
        rearRight.setPower(power);
        frontRight.setPower(power);
    }

    public void setIntakePower(double power) {
        intake1.setPower(power);
        intake2.setPower(power);
    }
}
