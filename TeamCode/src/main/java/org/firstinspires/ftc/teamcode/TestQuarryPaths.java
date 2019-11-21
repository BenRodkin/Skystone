package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Test: Quarry-Side Paths", group = "Testing")
public class TestQuarryPaths extends LinearOpMode {



    public void runOpMode() {



        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {

            telemetry.addLine("Running");
            telemetry.update();
        }

    }
}
