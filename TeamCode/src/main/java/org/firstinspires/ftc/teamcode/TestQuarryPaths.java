package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.teamcode.SkystonePlacement.CENTER;
import static org.firstinspires.ftc.teamcode.SkystonePlacement.LEFT;
import static org.firstinspires.ftc.teamcode.SkystonePlacement.RIGHT;

@Autonomous(name = "Test: Quarry-Side Paths", group = "Testing")
public class TestQuarryPaths extends LinearOpMode {

    SkystonePlacement placement = CENTER; // Default is center



    public void runOpMode() {



        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {

            if(gamepad1.dpad_left)  placement   = LEFT;
            if(gamepad1.dpad_up)    placement   = CENTER;
            if(gamepad1.dpad_right) placement   = RIGHT;

            if(gamepad1.y) {
                switch(placement) {
                    case LEFT:
                        runLeft();
                        break;
                    case CENTER:
                        runCenter();
                        break;
                    case RIGHT:
                        runRight();
                        break;
                }
            }




            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addData("Skystone Placement", placement);
            telemetry.update();
        }

    }


    public void runLeft() {
        telemetry.addLine("Running LEFT");
        telemetry.update();
        sleep(1000);
    }

    public void runCenter() {
        telemetry.addLine("Running CENTER");
        telemetry.update();
        sleep(1000);
    }

    public void runRight() {
        telemetry.addLine("Running RIGHT");
        telemetry.update();
        sleep(1000);
    }
}
