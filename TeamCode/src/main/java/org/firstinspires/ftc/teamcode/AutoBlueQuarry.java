package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Blue Quarry", group = "Autonomous")
public class AutoBlueQuarry extends LinearOpMode {

    SLICBotHardware hardware = new SLICBotHardware();


    public void runOpMode() {

        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        telemetry.addLine("Step 1");
        telemetry.update();
        sleep(1000);

        hardware.clamp.setPosition(0.0);
        moveArmCounts(hardware.ARM_COUNTS_DEPLOY, 0.3);

        telemetry.addLine("Step 2");
        telemetry.update();
        sleep(1000);

        driveInches(37.0, 0.4);

        telemetry.addLine("Step 3");
        telemetry.update();
        sleep(1000);

        hardware.clamp.setPosition(1.0);

        hardware.setLeftPower(-0.2);
        hardware.setRightPower(-0.4);
        sleep(700);

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        while(opModeIsActive()) {
            telemetry.addLine("Finished.");
            telemetry.update();
        }

    }





    public void moveArmCounts(int counts, double speed) {
        hardware.arm.setTargetPosition (hardware.arm.getCurrentPosition() + counts);

        hardware.arm.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.arm.setPower(speed);

        while(opModeIsActive() && hardware.arm.isBusy()) {
            telemetry.addData("Arm encoder", hardware.arm.getCurrentPosition());
            telemetry.update();
        }

        hardware.arm.setPower(0.0);

        hardware.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void driveInches(double inches, double speed) {
        driveEncoderCounts((int) (inches * hardware.COUNTS_PER_INCH), speed);

    }

    public void driveEncoderCounts(int counts, double speed) {
        hardware.frontLeft.setTargetPosition    (hardware.frontLeft.getCurrentPosition() - counts);
        hardware.frontRight.setTargetPosition   (hardware.frontRight.getCurrentPosition() - counts);
        hardware.rearLeft.setTargetPosition     (hardware.rearLeft.getCurrentPosition() - counts);
        hardware.rearRight.setTargetPosition    (hardware.rearRight.getCurrentPosition() - counts);

        hardware.frontLeft.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.frontRight.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearLeft.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        hardware.rearRight.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        hardware.setLeftPower(speed);
        hardware.setRightPower(speed);

        while(opModeIsActive() &&
                hardware.frontLeft.isBusy() &&
                hardware.frontRight.isBusy() &&
                hardware.rearLeft.isBusy() &&
                hardware.rearRight.isBusy()) {
            telemetry.addData("Front left encoder", hardware.frontLeft.getCurrentPosition());
            telemetry.addData("Front right encoder", hardware.frontRight.getCurrentPosition());
            telemetry.update();
        }

        hardware.setLeftPower(0.0);
        hardware.setRightPower(0.0);

        hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}
