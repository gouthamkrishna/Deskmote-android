package com.beaconapp.user.navigation.classes;

/**
 * Created by user on 14/7/15.
 */

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;


public class CircularProgressDrawable extends Drawable {
    public static float PROGRESS_FACTOR = 360.0F;
    public static final String PROGRESS_PROPERTY = "progress";
    public static final String RING_COLOR_PROPERTY = "ringColor";
    private final Paint paint;
    protected float progress = 0.0F;
    protected int outlineColor;
    protected int ringColor;
    protected int centerColor;
    protected final RectF arcElements;
    protected final int ringWidth;
    protected float circleScale;
    protected boolean indeterminate;

    public CircularProgressDrawable(int ringWidth, float circleScale, int outlineColor, int ringColor, int centerColor) {
        this.outlineColor = outlineColor;
        this.ringColor = ringColor;
        this.centerColor = centerColor;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.ringWidth = ringWidth;
        this.arcElements = new RectF();
        this.circleScale = circleScale;
        this.indeterminate = false;
    }

    public void draw(Canvas canvas) {
        Rect bounds = this.getBounds();
        int size = Math.min(bounds.height(), bounds.width());
        float outerRadius = (float)(size / 2 - this.ringWidth / 2);
        float innerRadius = outerRadius * this.circleScale;
        float offsetX = ((float)bounds.width() - outerRadius * 2.0F) / 2.0F;
        float offsetY = ((float)bounds.height() - outerRadius * 2.0F) / 2.0F;
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(1.0F);
        this.paint.setColor(this.outlineColor);
        canvas.drawCircle((float)bounds.centerX(), (float)bounds.centerY(), outerRadius, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.centerColor);
        canvas.drawCircle((float)bounds.centerX(), (float)bounds.centerY(), innerRadius, this.paint);
        int halfRingWidth = this.ringWidth / 2;
        float arcX0 = offsetX + (float)halfRingWidth;
        float arcY0 = offsetY + (float)halfRingWidth;
        float arcX = offsetX + outerRadius * 2.0F - (float)halfRingWidth;
        float arcY = offsetY + outerRadius * 2.0F - (float)halfRingWidth;
        this.paint.setColor(this.ringColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth((float)this.ringWidth);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.arcElements.set(arcX0, arcY0, arcX, arcY);
        if(this.indeterminate) {
            canvas.drawArc(this.arcElements, this.progress, 90.0F, false, this.paint);
        } else {
            canvas.drawArc(this.arcElements, 270F, this.progress, false, this.paint);
        }

    }

    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.paint.setColorFilter(cf);
    }

    public int getOpacity() {
        return 1 - this.paint.getAlpha();
    }

    public float getProgress() {
        return this.progress / PROGRESS_FACTOR;
    }

    public void setProgress(float progress) {
        if(this.indeterminate) {
            this.progress = progress;
        } else {
            this.progress = PROGRESS_FACTOR * progress;
        }

        this.invalidateSelf();
    }

    public float getCircleScale() {
        return this.circleScale;
    }

    public void setCircleScale(float circleScale) {
        this.circleScale = circleScale;
        this.invalidateSelf();
    }

    public boolean isIndeterminate() {
        return this.indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public int getOutlineColor() {
        return this.outlineColor;
    }

    public int getRingColor() {
        return this.ringColor;
    }

    public int getCenterColor() {
        return this.centerColor;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
        this.invalidateSelf();
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
        this.invalidateSelf();
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
        this.invalidateSelf();
    }

    public static class Builder {
        int ringWidth;
        int outlineColor;
        int ringColor;
        int centerColor;
        float circleScale = 0.75F;

        public Builder() {
        }

        public CircularProgressDrawable.Builder setRingWidth(int ringWidth) {
            this.ringWidth = ringWidth;
            return this;
        }

        public CircularProgressDrawable.Builder setOutlineColor(int outlineColor) {
            this.outlineColor = outlineColor;
            return this;
        }


        public CircularProgressDrawable.Builder setRingColor(int ringColor) {
            this.ringColor = ringColor;
            return this;
        }

        public CircularProgressDrawable.Builder setCenterColor(int centerColor) {
            this.centerColor = centerColor;
            return this;
        }

        public CircularProgressDrawable.Builder setInnerCircleScale(float circleScale) {
            this.circleScale = circleScale;
            return this;
        }


        public CircularProgressDrawable create() {
            return new CircularProgressDrawable(this.ringWidth, this.circleScale, this.outlineColor, this.ringColor, this.centerColor);
        }
    }
}