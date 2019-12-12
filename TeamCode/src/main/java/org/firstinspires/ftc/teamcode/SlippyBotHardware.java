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
