package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SlippyBotHardware {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor pulley;

    public final double COUNTS_PER_REV_HD_20    = 560; // REV HD Hex 20:1 motor
    public final double DRIVE_GEAR_REDUCTION    = 20.0 / 26.0; // 15 tooth on motor shaft to 15 tooth on wheel shaft
    public final double WHEEL_DI_INCHES         = 90.0 / 25.4; // 90mm diameter wheel divided by 25.4(in/mm)
    public final double COUNTS_PER_INCH         = (COUNTS_PER_REV_HD_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DI_INCHES * Math.PI);


    // PID variables
    public final double MAX_SPEED = 0.4;
    public final double P = 0.02;
    public final double I = 0.0;
    public final double D = 0.1;
    public final double TOLERANCE = 1;

    public final SynchronousPID pid = new SynchronousPID(P, I, D);


    public void init(HardwareMap hardwareMap) {
        frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");

        pulley      = hardwareMap.dcMotor.get("pulley");


        frontLeft.  setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.   setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight. setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.   setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.  setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pulley.     setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}
