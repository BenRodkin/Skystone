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

    public Servo deploy1;
    public Servo deploy2;

    // Deploy servo positions (index 0 is the left servo, index 1 is the right)
    public final double[] STOWED =      {0.0, 0.0};
    public final double[] DEPLOYED =    {1.0, 1.0};
    public final double[] GRABBING =    {0.5, 0.4};

    public final boolean BRAKE_ON_ZERO = true;

    public void init() {
        frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");


        intake1     = hardwareMap.dcMotor.get("intake1");
        intake2     = hardwareMap.dcMotor.get("intake2");

        deploy1     = hardwareMap.servo.get("deploy1");
        deploy2     = hardwareMap.servo.get("deploy2");



        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

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

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {


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


        if(gamepad1.a || gamepad2.a) {
            setIntakePower(1.0);
        } else if(gamepad1.b || gamepad2.b) {
            setIntakePower(-1.0);
        } else {
            setIntakePower(0.0);
        }

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
