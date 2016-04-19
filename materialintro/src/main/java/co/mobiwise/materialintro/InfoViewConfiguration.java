package co.mobiwise.materialintro;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by yamlee on 4/5/16.
 */
public class InfoViewConfiguration {
    private View infoView;
    private ObjectAnimator animator;
    private boolean alignCenter;
    private int targetMargin;

    public ObjectAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(ObjectAnimator animator) {
        this.animator = animator;
    }

    public View getInfoView() {
        return infoView;
    }

    public void setInfoView(View inoView) {
        this.infoView = inoView;
    }

    public boolean isAlignCenter() {
        return alignCenter;
    }

    public void setAlignCenter(boolean alignCenter) {
        this.alignCenter = alignCenter;
    }

    public int getTargetMargin() {
        return targetMargin;
    }

    public void setTargetMargin(int targetMargin) {
        this.targetMargin = targetMargin;
    }
}
