package org.firstinspires.ftc.teamcode;

public enum ArmMode {
    PLACING,
    GRABBING,
    STARTING,
    STORING;

    public String getMode() {
        switch (this) {
            case PLACING:   return "Placing";
            case GRABBING:  return "Grabbing";
            case STARTING:  return "Starting";
            case STORING:   return "Storing";
            default:        return "[Defaulted]";
        }
    }

}
