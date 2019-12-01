package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum Driver Control", group = "TeleOp")
public class SlippyBotTeleOp extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    double flPower = 0.0;
    double frPower = 0.0;
    double rlPower = 0.0;
    double rrPower = 0.0;

    double pullyPower = 0.0;

    double armPower = 0.0;
    double clampPos = 0.0;

    GamepadCooldowns gp2 = new GamepadCooldowns();
    double runtime = 0.0;

    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {

        runtime = getRuntime();

        // Do the math
        double drive  = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double twist  = -gamepad1.right_stick_x;

        flPower = (drive + strafe + twist);
        frPower = (drive - strafe - twist);
        rlPower = (drive - strafe + twist);
        rrPower = (drive + strafe - twist);

        pullyPower = (gamepad2.left_trigger - gamepad2.right_trigger);

        armPower = gamepad2.left_stick_y * 1.0;

        if(gamepad2.a && gp2.a.ready(runtime)) {
            clampPos = Math.abs(1 - clampPos);
            gp2.a.updateSnapshot(runtime);
        }




        // Set the power
        hardware.frontLeft. setPower(flPower);
        hardware.frontRight.setPower(frPower);
        hardware.rearLeft.  setPower(rlPower);
        hardware.rearRight. setPower(rrPower);

        hardware.pulleyLeft.    setPower(pullyPower);
        hardware.pulleyRight.   setPower(pullyPower);

        hardware.arm.           setPower(armPower);
        hardware.clamp.         setPosition(clampPos);


        telemetry.addLine("Running");
        telemetry.addLine();
        telemetry.addData("Arm position", hardware.arm.getCurrentPosition());
        telemetry.addLine();
        telemetry.addData("Clamp position", hardware.clamp.getPosition());
        telemetry.addLine();
        telemetry.addData("Left pulley position", hardware.pulleyLeft.getCurrentPosition());
        telemetry.addData("Right pulley position", hardware.pulleyRight.getCurrentPosition());
        telemetry.update();
    }
}
