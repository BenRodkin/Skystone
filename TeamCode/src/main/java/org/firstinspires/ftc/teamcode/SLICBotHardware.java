package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
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



    public void init() {

    }
}
