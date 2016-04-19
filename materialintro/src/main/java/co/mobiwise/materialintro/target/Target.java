package co.mobiwise.materialintro.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by mertsimsek on 25/01/16.
 */
public interface Target {
    /**
     * Returns center point of target.
     * We can get x and y coordinates using
     * point object
     * @return Point for location
     */
    Point getPoint();

    /**
     * Returns Rectangle points of target view
     * @return rect
     */
    Rect getRect();

    /**
     * return target view
     * @return view
     */
    View getView();
}
