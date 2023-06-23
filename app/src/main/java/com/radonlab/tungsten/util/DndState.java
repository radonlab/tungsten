package com.radonlab.tungsten.util;

public class DndState {

    private float x;

    private float y;

    private float targetX;

    private float targetY;

    private boolean moving;

    public void startDrag(float x, float y, float targetX, float targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.moving = true;
    }

    public void endDrag() {
        this.moving = false;
    }

    public float getDeltaX(float x) {
        return x - this.x;
    }

    public float getDeltaY(float y) {
        return y - this.y;
    }

    public float getCurrentX(float x) {
        if (!moving) {
            return this.targetX;
        }
        return this.targetX + getDeltaX(x);
    }

    public float getCurrentY(float y) {
        if (!moving) {
            return this.targetY;
        }
        return this.targetY + getDeltaY(y);
    }
}