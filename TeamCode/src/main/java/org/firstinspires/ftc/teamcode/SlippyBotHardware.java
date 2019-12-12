package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class SlippyBotHardware {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor pulleyLeft;
    public DcMotor pulleyRight;

    public DcMotor arm;

    public CRServo intakeLeft;
    public CRServo intakeRight;

//    public Servo clamp;

    Servo gripper;
    Servo wrist;

    // Drive encoder variables
    public final double COUNTS_PER_REV_HD_20    = 560; // REV HD Hex 20:1 motor
    public final double DRIVE_GEAR_REDUCTION    = 20.0 / 26.0; // 15 tooth on motor shaft to 15 tooth on wheel shaft
    public final double WHEEL_DI_INCHES         = 90.0 / 25.4; // 90mm diameter wheel divided by 25.4(in/mm)
    public final double COUNTS_PER_INCH         = (COUNTS_PER_REV_HD_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DI_INCHES * Math.PI);

    // Servo-specific variables
    public static final double GRIPPER_CLOSED = 0.0;
    public static final double GRIPPER_OPEN = 1.0;


    // Speed modifier variables
    public static final double SLOW     = 0.5;
    public static final double NORMAL   = 0.8;
    public static final double FAST     = 1.0;

    public static double wheelSpeedMod  = FAST;
    public static double armSpeedMod    = FAST;

    public static final double WRIST_SCALAR = 0.008;


    // PID variables
    public final double MAX_SPEED = 0.4;
    public final double P = 0.02;
    public final double I = 0.0;
    public final double D = 0.1;
    public final double TOLERANCE = 1;

    public final SynchronousPID pid = new SynchronousPID(P, I, D);

    // Camera variables
    OpenCvCamera phoneCam;
    SkystonePatternPipeline vision;

    public void init(HardwareMap hwMap) {
        init(hwMap, false); // Default is to not initialize the camera
    }


    public void init(HardwareMap hardwareMap, boolean initCamera) {
        frontLeft   = hardwareMap.dcMotor.get("fl_drive");
        frontRight  = hardwareMap.dcMotor.get("fr_drive");
        rearLeft    = hardwareMap.dcMotor.get("rl_drive");
        rearRight   = hardwareMap.dcMotor.get("rr_drive");

        pulleyLeft      = hardwareMap.dcMotor.get("pulley_left");
        pulleyRight     = hardwareMap.dcMotor.get("pulley_right");

        arm         = hardwareMap.dcMotor.get("arm");

        intakeLeft  = hardwareMap.crservo.get("intake_left");
        intakeRight = hardwareMap.crservo.get("intake_right");

//        clamp       = hardwareMap.servo.get("clamp");


        gripper = hardwareMap.servo.get("gripper");
        wrist = hardwareMap.servo.get("wrist");



        frontLeft.  setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.   setDirection(DcMotorSimple.Direction.REVERSE);

        pulleyLeft. setDirection(DcMotorSimple.Direction.REVERSE);

        intakeLeft. setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.      setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.     setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.       setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.      setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pulleyLeft.     setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pulleyRight.    setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.            setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        /*
            We are using Actuonix PQ-12 linear actuators for the grippers as of Dec 12 2019. They
            take a signal between 1.0 milliseconds (extend; 1000 microseconds) and 2.0 milliseconds
            (retract; 2000 microseconds) according to Actuonix's datasheet. REV Smart Robot Servos,
            for comparison, take a signal between 500 microseconds (-90 degrees) and 2500
            microseconds (90 degrees), so a 2500 microseconds signal is sent when setPosition(1.0)
            is called. To correct the range for the linear actuator servos, We are setting them to
            0.4 (1000 / 2500) and 0.8 (2000 / 2500).
         */
        gripper.scaleRange(0.4, 0.8);



        // Camera
        if(initCamera) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
            phoneCam.openCameraDevice();
            vision = new SkystonePatternPipeline();
            phoneCam.setPipeline(vision);
            phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
        }
    }


    public void setLeftPower(double power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
    }

    public void setRightPower(double power) {
        frontRight.setPower(power);
        rearRight.setPower(power);
    }

}
