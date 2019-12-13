package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class SlippyBotHardware {

    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor rearLeft;
    public DcMotor rearRight;

    public DcMotor pulleyLeft;
    public DcMotor pulleyRight;

    public DcMotor arm;

    // Servos
    public CRServo intakeLeft;
    public CRServo intakeRight;

//    public Servo clamp;

    Servo gripper;
    Servo wrist;


//    // IMU
    BNO055IMU imu;

    // Drive encoder variables
    public final double COUNTS_PER_REV_HD_20    = 560; // REV HD Hex 20:1 motor
    public final double DRIVE_GEAR_REDUCTION    = 20.0 / 26.0; // 15 tooth on motor shaft to 15 tooth on wheel shaft
    public final double WHEEL_DI_INCHES         = 90.0 / 25.4; // 90mm diameter wheel divided by 25.4(in/mm)
//    public final double COUNTS_PER_INCH         = (COUNTS_PER_REV_HD_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DI_INCHES * Math.PI);

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
    public final double MAX_SPEED = 0.3;
    public final double P = 0.025;
    public final double I = 0.0;
    public final double D = 0.0;
    public final double TOLERANCE = 1;

    public final SynchronousPID pid = new SynchronousPID(P, I, D);

    // Timeout variable for looping methods
    public static final double TIMEOUT = 2.0;  // 2 seconds

    // Camera variables
    OpenCvCamera phoneCam;
    SkystonePatternPipeline vision;

    public void init(HardwareMap hwMap) {
        init(hwMap, false, false); // Default is to not initialize the camera or the IMU
    }


    public void init(HardwareMap hardwareMap, boolean initCamera, boolean initIMU) {
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


        // IMU
        if(initIMU) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }

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

    // Gyroscope methods
    public double normalize180(double angle) {
        while(angle > 180) {
            angle -= 360;
        }
        while(angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    public float heading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}
