package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum Driver Control", group = "TeleOp")
public class SlippyBotTeleOp extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {



        telemetry.addLine("Running");
        telemetry.update();
    }
}
