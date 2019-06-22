package com.duy.adruino.car.controller;

public enum Direction {
    RIGHT(0, 45f, "R"),
    TOP_RIGHT(45f, 90.0f, "TR"),
    TOP(90.0f, 135.0f, "T"),
    TOP_LEFT(135.0f, 180.0f, "TL"),
    LEFT(180.0f, 225.0f, "L"),
    LEFT_BOTTOM(225.0f, 270.0f, "LB"),
    BOTTOM(270.0f, 315.0f, "B"),
    RIGHT_BOTTOM(315.0f, 360.0f, "RB"),
    NONE(-1, -1, "S"),
    ;

    public static final float step = 45f;
    private final float start;
    private final float end;
    private String command;

    Direction(float start, float end, String command) {
        this.start = start;
        this.end = end;
        this.command = command;
    }

    public static Direction getDirection(float angle) {
        angle = (angle + 360) % 360;

        for (Direction value : Direction.values()) {
            if (value == NONE) {
                continue;
            }
            if (value.contain(angle)) {
                return value;
            }
        }

        return NONE;
    }

    boolean contain(float angle) {
        if (end <= start) {
            return start <= angle + 360 && angle <= end;
        }
        return start <= angle && angle <= end;
    }

    public String getArduinoCommand() {
        return command;
    }
}
