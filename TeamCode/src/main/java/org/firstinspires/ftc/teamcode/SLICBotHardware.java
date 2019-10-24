package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SLICBotHardware {

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



    public void init(HardwareMap hardwareMap) {
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
    }





    public void setLeftPower(double power) {
        rearLeft.setPower(power);
        frontLeft.setPower(power);

    }

    public void setRightPower(double power) {
        rearRight.setPower(power);
        frontRight.setPower(power);
    }
}
