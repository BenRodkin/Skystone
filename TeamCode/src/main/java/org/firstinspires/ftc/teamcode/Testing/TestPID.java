package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.miscellaneous.GamepadCooldowns;
import org.firstinspires.ftc.teamcode.SlippyBotHardware;

import static org.firstinspires.ftc.teamcode.SlippyBotHardware.TIMEOUT;

//@Disabled
@Autonomous(name = "Test: SynchronousPID", group = "Testing")
public class TestPID extends LinearOpMode {

    // Hardware class
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Button cooldowns
    GamepadCooldowns cooldowns = new GamepadCooldowns();

    // Threshold for using triggers as binary input (e.g. if(gamepad1.right_trigger > TRIGGER_THRESHOLD) )
    public final double TRIGGER_THRESHOLD = 0.7;

    // Local runtime variable (cuts down on the number of calls to getRuntime() )
    private double runtime;

    // PID coefficients (start with val in hardware)
    private double kP = hardware.P;
    private double kI = hardware.I;
    private double kD = hardware.D;
    private double maxSpeed = 0.25;

    // Increment for increasing or decreasing PID coefficients
    private final double K_STEP = 0.005;

    // Increment for increasing or decreasing max speed
    private final double SPEED_STEP = 0.025;

    private final boolean INIT_CAMERA   = false;
    private final boolean INIT_IMU      = true;

    @Override public void runOpMode() throws InterruptedException {

        telemetry.addLine("Initializing hardware");
        telemetry.update();

        hardware.init(hardwareMap, INIT_CAMERA, INIT_IMU);

        cooldowns.rb.setCooldown(0.500);    // 500 ms
        cooldowns.rt.setCooldown(0.500);    // 500 ms

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();


        while(opModeIsActive()) {

            //--------------------------------------------------------------------------------------
            // START PID COEFFICIENT CONTROLS
            //--------------------------------------------------------------------------------------

                /*
                    CONTROLS: (increase, decrease)
                    P: gp1.up,      gp1.down
                    I: gp1.right,   gp1.left
                    D: gp1.lb,      gp1.lt
                */

            runtime = getRuntime();


            // Proportional coefficient-------------------------------------------------------------
            if(gamepad1.dpad_up && cooldowns.dpUp.ready(runtime)) {
                kP += K_STEP;
                cooldowns.dpUp.updateSnapshot(runtime);
            }

            if(gamepad1.dpad_down && cooldowns.dpDown.ready(runtime)) {
                if(kP < K_STEP) kP = 0.0;
                else            kP -= K_STEP;
                cooldowns.dpDown.updateSnapshot(runtime);
            }


            // Integral coefficient-----------------------------------------------------------------
            if(gamepad1.dpad_right && cooldowns.dpRight.ready(runtime)) {
                kI += K_STEP;
                cooldowns.dpRight.updateSnapshot(runtime);
            }

            if(gamepad1.dpad_left && cooldowns.dpLeft.ready(runtime)) {
                if(kI < K_STEP) kI = 0.0;
                else            kI -= K_STEP;
                cooldowns.dpLeft.updateSnapshot(runtime);
            }


            // Derivative coefficient---------------------------------------------------------------
            if(gamepad1.left_bumper && cooldowns.lb.ready(runtime)) {
                kD += K_STEP;
                cooldowns.lb.updateSnapshot(runtime);
            }

            if(gamepad1.left_trigger > TRIGGER_THRESHOLD && cooldowns.lt.ready(runtime)) {
                if(kD < K_STEP) kD = 0.0;
                else            kD -= K_STEP;
                cooldowns.lt.updateSnapshot(runtime);
            }


            // Max speed----------------------------------------------------------------------------
            if(gamepad1.right_bumper && cooldowns.rb.ready(runtime)) {
                maxSpeed = Range.clip(maxSpeed + SPEED_STEP, 0.0, 1.0); // Range from 0.0 to 1.0

                cooldowns.rb.updateSnapshot(runtime);
            }

            if(gamepad1.right_trigger > TRIGGER_THRESHOLD && cooldowns.rt.ready(runtime)) {
                maxSpeed = Range.clip(maxSpeed - SPEED_STEP, 0.0, 1.0); // Range from 0.0 to 1.0

                cooldowns.rt.updateSnapshot(runtime);
            }

            //--------------------------------------------------------------------------------------
            // END PID COEFFICIENT CONTROLS
            //--------------------------------------------------------------------------------------

            // Set PID coefficients
            hardware.pid.setPID(kP, kI, kD);



            /*
                    CONTROLS: (Target heading listed)
                    0:      gp2.y
                    45:     gp2.a
                    90:     gp2.x
            */
            if(gamepad2.y) turnToHeadingPID(0);
            else if(gamepad2.a) turnToHeadingPID(45);
            else if(gamepad2.x) turnToHeadingPID(90);



            telemetry.addData("kP", hardware.pid.getP());
            telemetry.addData("kI", hardware.pid.getI());
            telemetry.addData("kD", hardware.pid.getD());
            telemetry.addLine();
            telemetry.addData("Max Speed", maxSpeed);
            telemetry.addLine();
            telemetry.addData("Heading", hardware.heading());
            telemetry.update();
        }


    }

    //----------------------------------------------------------------------------------------------
    // PID controller methods
    //----------------------------------------------------------------------------------------------

    public void turnToHeadingPID(int target) throws InterruptedException {

        telemetry.addData("Turning to target", target);
        telemetry.addLine("Press dpad_down to stop.");

        hardware.pid.setSetpoint(target);                                       // Set target final heading relative to current
        hardware.pid.setOutputRange(-maxSpeed, maxSpeed);                       // Set maximum motor power
        hardware.pid.setDeadband(hardware.TOLERANCE);                           // Set how far off you can safely be from your target

        double prevError = -185.0;  // > 180 in order to keep error from reaching zero on first run through loop
        double turnStart = getRuntime();
        while (opModeIsActive() &&
                (getRuntime() - turnStart) < TIMEOUT) {
            double error = hardware.normalize180(-(target - hardware.heading()));
            double power = hardware.pid.calculateGivenError(error);

            double dv = prevError - error;

            telemetry.addData("Runtime - turnStart", getRuntime() - turnStart);
            telemetry.addData("Current error", error);
            telemetry.addData("Previous error", prevError);
            telemetry.addData("dv", dv);
            telemetry.addLine();
            telemetry.addData("Current error", error);
            telemetry.addData("Current power", power);

            hardware.setLeftPower(-power);
            hardware.setRightPower(power);

            prevError = error;      // Calculate after setting power and updating telemetry

            if ( (Math.abs(error) < hardware.TOLERANCE && Math.abs(dv) < hardware.DV_TOLERANCE)
                    || gamepad2.dpad_down) {
                break;
            }

            Thread.sleep(1);

            telemetry.update();
        }

        hardware.setLeftPower(0);
        hardware.setRightPower(0);
    }
}
