package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class SlippyBotTeleOp extends OpMode {

    SlippyBotHardware hardware = new SlippyBotHardware();

    public void init() {
        hardware.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
    }

    public void loop() {



        telemetry.addLine("Running");
    }
}
