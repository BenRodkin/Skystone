package org.firstinspires.ftc.teamcode.miscellaneous;

public enum SkystonePlacement {
    LEFT,
    CENTER,
    RIGHT;

    @Override
    public String toString() {
        switch(this) {
            case LEFT:      return "Left";
            case CENTER:    return "Center";
            case RIGHT:     return "Right";
            default:        return "[Defaulted]";
        }
    }
}
