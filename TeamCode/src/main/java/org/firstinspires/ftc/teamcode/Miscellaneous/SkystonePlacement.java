package org.firstinspires.ftc.teamcode.miscellaneous;

public enum SkystonePlacement {
    LEFT,
    CENTER,
    RIGHT,
    UNKNOWN;

    @Override
    public String toString() {
        switch(this) {
            case LEFT:      return "Left";
            case CENTER:    return "Center";
            case RIGHT:     return "Right";
            case UNKNOWN:   return "Unknown";
            default:        return "[Defaulted]";
        }
    }
}
