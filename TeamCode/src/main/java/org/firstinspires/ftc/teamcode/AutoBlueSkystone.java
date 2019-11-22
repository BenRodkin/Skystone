package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "Blue Skystone", group = "Autonomous")
public class AutoBlueSkystone extends LinearOpMode {




    public void runOpMode() throws InterruptedException {

        while(!isStarted() && !isStopRequested()) {
            telemetry.addLine("looping in initialization");
            telemetry.update();
        }

        // Auto program starts here


        while(opModeIsActive()) {
            telemetry.addLine("Running");
            telemetry.update();
        }
    }
}
