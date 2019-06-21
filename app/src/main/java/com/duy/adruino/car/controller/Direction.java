package com.duy.adruino.car.controller;

public enum Direction {
    RIGHT(0, 45f),
    TOP_RIGHT(45f, 90.0f),
    TOP(90.0f, 135.0f),
    TOP_LEFT(135.0f, 180.0f),
    LEFT(180.0f, 225.0f),
    LEFT_BOTTOM(225.0f, 270.0f),
    BOTTOM(270.0f, 315.0f),
    RIGHT_BOTTOM(315.0f, 360.0f),
    ;

    public static final float step = 45f;
    private final float start;
    private final float end;

    Direction(float start, float end) {
        this.start = start;
        this.end = end;
    }

    public static Direction getDirection(float angle) {
        angle = (angle + 360) % 360;

        for (Direction value : Direction.values()) {
            if (value.contain((float) angle)) {
                return value;
            }
        }

        return null;
    }

    boolean contain(float angle) {
        if (end <= start) {
            return start <= angle + 360 && angle <= end;
        }
        return start <= angle && angle <= end;
    }

}
