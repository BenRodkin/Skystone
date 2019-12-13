package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.MatOfPoint;

import java.util.List;

import static org.firstinspires.ftc.teamcode.SkystonePlacement.CENTER;
import static org.firstinspires.ftc.teamcode.SlippyBotHardware.TIMEOUT;

@Autonomous(name = "Blue Quarry", group = "Autonomous")
public class AutoBlueQuarry extends LinearOpMode {

    // Hardware
    SlippyBotHardware hardware = new SlippyBotHardware();

    // Contours from pipeline after filtering
    private List<MatOfPoint> contours;

    // Skystone placement for path decision
    private SkystonePlacement placement = CENTER;

    // Hardware class initialization variables
    private final boolean INIT_CAMERA   = true;
    private final boolean INIT_IMU      = true;

    public void runOpMode() {

        telemetry.addLine("Initializing hardware... do not move robot!");
        telemetry.update();

        hardware.init(hardwareMap, INIT_CAMERA, INIT_IMU);

        while(!isStarted() && !isStopRequested()) {






            telemetry.addLine("Ready");
            telemetry.addLine();
            telemetry.addData("Heading", hardware.heading());
            telemetry.update();
        }


        while(opModeIsActive()) {
            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Heading", hardware.heading());
            telemetry.update();
        }

    }

    public void turnToHeadingPID(int target) throws InterruptedException {

        telemetry.addData("Turning to target", target);
        telemetry.addLine("Press dpad_down to stop.");

        hardware.pid.setSetpoint(target);                                       // Set target final heading relative to current
        hardware.pid.setOutputRange(-hardware.MAX_SPEED, hardware.MAX_SPEED);   // Set maximum motor power
        hardware.pid.setDeadband(hardware.TOLERANCE);                           // Set how far off you can safely be from your target

        double turnStart = getRuntime();
        while (opModeIsActive() &&
                (getRuntime() - turnStart) < TIMEOUT) {
            double error = hardware.normalize180(-(target - hardware.heading()));
            double power = hardware.pid.calculateGivenError(error);

            telemetry.addData("Runtime - turnStart", getRuntime() - turnStart);
            telemetry.addData("Current error", error);
            telemetry.addData("Current power", power);

            hardware.setLeftPower(-power);
            hardware.setRightPower(power);

            if (Math.abs(error) < hardware.TOLERANCE || gamepad2.dpad_down) {
                break;
            }

            Thread.sleep(1);

            telemetry.update();
        }

        hardware.setLeftPower(0);
        hardware.setRightPower(0);
    }
}
