package co.mobiwise.materialintro.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.mobiwise.materialintro.InfoViewConfiguration;
import co.mobiwise.materialintro.MaterialIntroConfiguration;
import co.mobiwise.materialintro.R;
import co.mobiwise.materialintro.animation.AnimationFactory;
import co.mobiwise.materialintro.animation.AnimationListener;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.prefs.PreferencesManager;
import co.mobiwise.materialintro.shape.Circle;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.Rect;
import co.mobiwise.materialintro.shape.Shape;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.target.Target;
import co.mobiwise.materialintro.target.ViewTarget;
import co.mobiwise.materialintro.utils.Constants;
import co.mobiwise.materialintro.utils.Utils;

/**
 * Created by mertsimsek on 22/01/16.
 */
public class MaterialIntroView extends RelativeLayout {
    private static final String TAG = "MaterialIntroView";
    /**
     * Mask color
     */
    private int maskColor;

    /**
     * MaterialIntroView will start
     * showing after delayMillis seconds
     * passed
     */
    private long delayMillis;

    /**
     * We don't draw MaterialIntroView
     * until isReady field set to true
     */
    private boolean isReady;

    /**
     * Show/Dismiss MaterialIntroView
     * with fade in/out animation if
     * this is enabled.
     */
    private boolean isFadeAnimationEnabled;

    /**
     * Animation duration
     */
    private long fadeAnimationDuration;

    /**
     * targetShape focus on target
     * and clear circle to focus
     */
    private Shape targetShape;

    /**
     * Focus Type
     */
    private Focus focusType;

    /**
     * FocusGravity type
     */
    private FocusGravity focusGravity;

    /**
     * Target View
     */
    private Target targetView;

    /**
     * Eraser
     */
    private Paint eraser;

    /**
     * Handler will be used to
     * delay MaterialIntroView
     */
    private Handler handler;

    /**
     * All views will be drawn to
     * this bitmap and canvas then
     * bitmap will be drawn to canvas
     */
    private Bitmap bitmap;
    private Canvas canvas;

    /**
     * Circle padding
     */
    private int padding;

    /**
     * Layout width/height
     */
    private int width;
    private int height;

    /**
     * Dismiss on touch any position
     */
    private boolean dismissOnTouch;

    /**
     * Info dialog view
     */
    private View infoView;

    /**
     * Info Dialog Text
     */
    private TextView textViewInfo;

    /**
     * Info dialog text color
     */
    private int colorTextViewInfo;

    /**
     * Info dialog will be shown
     * If this value true
     */
    private boolean isInfoEnabled;

    /**
     * Dot view will appear center of
     * cleared target area
     */
    private View dotView;

    /**
     * Dot View will be shown if
     * this is true
     */
    private boolean isDotViewEnabled;

    /**
     * Info Dialog Icon
     */
    private ImageView imageViewIcon;

    /**
     * Image View will be shown if
     * this is true
     */
    private boolean isImageViewEnabled;

    /**
     * Save/Retrieve status of MaterialIntroView
     * If Intro is already learnt then don't show
     * it again.
     */
    private PreferencesManager preferencesManager;

    /**
     * Check using this Id whether user learned
     * or not.
     */
    private String materialIntroViewId;

    /**
     * When layout completed, we set this true
     * Otherwise onGlobalLayoutListener stuck on loop.
     */
    private boolean isLayoutCompleted;

    /**
     * Notify user when MaterialIntroView is dismissed
     */
    private MaterialIntroListener materialIntroListener;

    /**
     * Perform click operation to target
     * if this is true
     */
    private boolean isPerformClick;
    private Activity activity;
    private InfoViewConfiguration infoViewConfiguration;

    /**
     * Disallow this MaterialIntroView from showing up more than once at a time
     */
    private boolean isIdempotent;

    /**
     * Shape of target
     */
    private ShapeType shapeType;

    /**
     * Use custom shape
     */
    private boolean usesCustomShape = false;
    /**
     * click focus area then dismiss intro view
     */
    private boolean isClickFocusThenDismiss = true;

    public MaterialIntroView(Context context) {
        super(context);
        init(context);
    }

    public MaterialIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialIntroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        setWillNotDraw(false);

        /**
         * set default values
         */
        maskColor = Constants.DEFAULT_MASK_COLOR;
        delayMillis = Constants.DEFAULT_DELAY_MILLIS;
        fadeAnimationDuration = Constants.DEFAULT_FADE_DURATION;
        padding = Constants.DEFAULT_TARGET_PADDING;
        colorTextViewInfo = Constants.DEFAULT_COLOR_TEXTVIEW_INFO;
        focusType = Focus.ALL;
        focusGravity = FocusGravity.CENTER;
        shapeType = ShapeType.CIRCLE;
        isReady = false;
        isFadeAnimationEnabled = true;
        dismissOnTouch = false;
        isLayoutCompleted = false;
        isInfoEnabled = false;
        isDotViewEnabled = false;
        isPerformClick = false;
        isImageViewEnabled = true;
        isIdempotent = false;

        /**
         * initialize objects
         */
        handler = new Handler();

        preferencesManager = new PreferencesManager(context);

        eraser = new Paint();
        eraser.setColor(0xFFFFFFFF);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);

        View layoutInfo = LayoutInflater.from(getContext()).inflate(R.layout.material_intro_card, null);

        infoView = layoutInfo.findViewById(R.id.info_layout);
        textViewInfo = (TextView) layoutInfo.findViewById(R.id.textview_info);
        textViewInfo.setTextColor(colorTextViewInfo);
        imageViewIcon = (ImageView) layoutInfo.findViewById(R.id.imageview_icon);

        dotView = LayoutInflater.from(getContext()).inflate(R.layout.dotview, null);
        dotView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (targetShape != null) {
                    targetShape.reCalculateAll();
                }
                if (targetShape != null && targetShape.getPoint().y != 0 && !isLayoutCompleted) {
                    if (isInfoEnabled)
                        setInfoLayout();
                    if (isDotViewEnabled)
                        setDotViewLayout();
                    removeOnGlobalLayoutListener(MaterialIntroView.this, this);
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady) return;

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        if (width == 0 || height == 0) {
            return;
        }

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }

        /**
         * Draw mask
         */
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);

        /**
         * Clear focus area
         */
        targetShape.draw(this.canvas, eraser, padding);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * Perform click operation when user
     * touches on target circle.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xT = event.getX();
        float yT = event.getY();

        boolean isTouchOnFocus = targetShape.isTouchOnFocus(xT, yT);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                }

                return true;
            case MotionEvent.ACTION_UP:

                if (isTouchOnFocus || dismissOnTouch)
                    dismissWithUserClick();

                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().performClick();
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                    targetView.getView().setPressed(false);
                    targetView.getView().invalidate();
                }

                return true;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Shows material view with fade in
     * animation
     */
    public void show() {
        Log.i(TAG, "show method called");
        if (preferencesManager.isDisplayed(materialIntroViewId))
            return;

        if (activity == null) {
            throw new RuntimeException("MaterialIntroView need a activity context");
        }
        if (getParent() == null) {
            setVisibility(INVISIBLE);
            ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
        }

        setReady(true);
        int height = getNavigationBarHeight();


        setPadding(0, 0, 0, height);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFadeAnimationEnabled)
                    AnimationFactory.animateFadeIn(MaterialIntroView.this, fadeAnimationDuration, new AnimationListener.OnAnimationStartListener() {
                        @Override
                        public void onAnimationStart() {
                            setVisibility(VISIBLE);
                        }
                    });
                else
                    setVisibility(VISIBLE);
            }
        }, delayMillis);

        if (isIdempotent) {
            preferencesManager.setDisplayed(materialIntroViewId);
        }
    }

    private int getNavigationBarHeight() {
        //set padding distance to bottom navigation bar if device has bottom navigation bar
        int identifier = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(identifier);
    }

    @SuppressLint("NewApi")
    private boolean hasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * Dismiss Material Intro View
     */
    public void dismiss() {
        if (!isIdempotent) {
            preferencesManager.setDisplayed(materialIntroViewId);
        }

        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, new AnimationListener.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(GONE);
                removeMaterialView();
            }
        });
    }

    private void dismissWithUserClick() {
        if (!isClickFocusThenDismiss) {
            return;
        }
        preferencesManager.setDisplayed(materialIntroViewId);
        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, new AnimationListener.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(GONE);
                removeMaterialView();

                if (materialIntroListener != null)
                    materialIntroListener.onUserClicked(materialIntroViewId);
            }
        });
    }

    private void removeMaterialView() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    /**
     * locate info card view above/below the
     * circle. If circle's Y coordiante is bigger than
     * Y coordinate of root view, then locate cardview
     * above the circle. Otherwise locate below.
     */
    private void setInfoLayout() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                isLayoutCompleted = true;

                if (infoViewConfiguration != null) {
                    infoView = infoViewConfiguration.getInfoView();
                }

                if (infoView.getParent() != null)
                    ((ViewGroup) infoView.getParent()).removeView(infoView);

                if (infoViewConfiguration != null) {
                    setCustomInfoLayout();
                } else {
                    setDefaultInfoLayout();
                }
            }


        });
    }


    private void setCustomInfoLayout() {
        RelativeLayout.LayoutParams infoDialogParams =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        Log.i(TAG, "infoDialogParams: width=" + infoDialogParams.width + " height=" + infoDialogParams.height);
        int leftMargin = 0;
        int targetMargin = targetShape.getHeight() / 2;
        if (infoViewConfiguration.isAlignCenter()) {
            leftMargin = Math.max(targetShape.getPoint().x - Utils.dpToPx(130), 0);
        }
        if (infoViewConfiguration.getTargetMargin() != 0) {
            targetMargin = infoViewConfiguration.getTargetMargin();
        }
        if (targetShape.getPoint().y < height / 2) {
            ((RelativeLayout) infoView).setGravity(Gravity.TOP);
            infoDialogParams.setMargins(
                    leftMargin,
                    targetShape.getPoint().y + targetShape.getHeight() / 2 + targetMargin,
                    0,
                    0);
        } else {
            ((RelativeLayout) infoView).setGravity(Gravity.BOTTOM);
            infoDialogParams.setMargins(
                    leftMargin,
                    0,
                    0,
                    height - getNavigationBarHeight() - targetShape.getPoint().y +
                            targetShape.getHeight() / 2 + targetMargin);
        }

        infoView.setLayoutParams(infoDialogParams);
        infoView.postInvalidate();

        addView(infoView);

        if (!isImageViewEnabled) {
            imageViewIcon.setVisibility(GONE);
        }

        infoView.setVisibility(VISIBLE);
        if (infoViewConfiguration != null && infoViewConfiguration.getAnimator() != null) {
            infoViewConfiguration.getAnimator().start();
        }
        Log.i(TAG, "infoView: width=" + infoView.getTranslationX() + " height=" + infoView.getTranslationY());
        Log.i(TAG, "infoView: x=" + infoView.getX() + " y=" + infoView.getY());
    }

    private void setDefaultInfoLayout() {
        RelativeLayout.LayoutParams infoDialogParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        Log.i(TAG, "infoDialogParams: width=" + infoDialogParams.width + " height=" + infoDialogParams.height);

        if (targetShape.getPoint().y < height / 2) {
            ((RelativeLayout) infoView).setGravity(Gravity.TOP);
            infoDialogParams.setMargins(
                    0,
                    targetShape.getPoint().y + targetShape.getHeight() / 2,
                    0,
                    0);
        } else {
            ((RelativeLayout) infoView).setGravity(Gravity.BOTTOM);
            infoDialogParams.setMargins(
                    0,
                    0,
                    0,
                    height - (targetShape.getPoint().y + targetShape.getHeight() / 2) + 2 * targetShape.getHeight() / 2);
        }

        infoView.setLayoutParams(infoDialogParams);
        infoView.postInvalidate();

        addView(infoView);

        if (!isImageViewEnabled) {
            imageViewIcon.setVisibility(GONE);
        }

        infoView.setVisibility(VISIBLE);

        Log.i(TAG, "infoView: width=" + infoView.getTranslationX() + " height=" + infoView.getTranslationY());
        Log.i(TAG, "infoView: x=" + infoView.getX() + " y=" + infoView.getY());
    }


    private void setDotViewLayout() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (dotView.getParent() != null)
                    ((ViewGroup) dotView.getParent()).removeView(dotView);

                RelativeLayout.LayoutParams dotViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dotViewLayoutParams.height = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE);
                dotViewLayoutParams.width = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE);
                dotViewLayoutParams.setMargins(
                        targetShape.getPoint().x - (dotViewLayoutParams.width / 2),
                        targetShape.getPoint().y - (dotViewLayoutParams.height / 2),
                        0,
                        0);
                dotView.setLayoutParams(dotViewLayoutParams);
                dotView.postInvalidate();
                addView(dotView);

                dotView.setVisibility(VISIBLE);
                AnimationFactory.performAnimation(dotView);
            }
        });
    }

    /**
     * SETTERS
     */

    private void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    private void setDelay(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    private void enableFadeAnimation(boolean isFadeAnimationEnabled) {
        this.isFadeAnimationEnabled = isFadeAnimationEnabled;
    }

    private void setShapeType(ShapeType shape) {
        this.shapeType = shape;
    }

    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    private void setTarget(Target target) {
        targetView = target;
    }

    private void setFocusType(Focus focusType) {
        this.focusType = focusType;
    }

    private void setShape(Shape shape) {
        this.targetShape = shape;
    }

    private void setPadding(int padding) {
        this.padding = padding;
    }

    private void setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
    }

    private void setFocusGravity(FocusGravity focusGravity) {
        this.focusGravity = focusGravity;
    }

    private void setColorTextViewInfo(int colorTextViewInfo) {
        this.colorTextViewInfo = colorTextViewInfo;
        textViewInfo.setTextColor(this.colorTextViewInfo);
    }

    private void setTextViewInfo(String textViewInfo) {
        this.textViewInfo.setText(textViewInfo);
    }

    private void setTextViewInfoSize(int textViewInfoSize) {
        this.textViewInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textViewInfoSize);
    }

    private void enableInfoDialog(boolean isInfoEnabled) {
        this.isInfoEnabled = isInfoEnabled;
    }

    private void enableImageViewIcon(boolean isImageViewEnabled) {
        this.isImageViewEnabled = isImageViewEnabled;
    }

    private void setIdempotent(boolean idempotent) {
        this.isIdempotent = idempotent;
    }

    private void enableDotView(boolean isDotViewEnabled) {
        this.isDotViewEnabled = isDotViewEnabled;
    }

    public void setConfiguration(MaterialIntroConfiguration configuration) {

        if (configuration != null) {
            this.maskColor = configuration.getMaskColor();
            this.delayMillis = configuration.getDelayMillis();
            this.isFadeAnimationEnabled = configuration.isFadeAnimationEnabled();
            this.colorTextViewInfo = configuration.getColorTextViewInfo();
            this.isDotViewEnabled = configuration.isDotViewEnabled();
            this.dismissOnTouch = configuration.isDismissOnTouch();
            this.colorTextViewInfo = configuration.getColorTextViewInfo();
            this.focusType = configuration.getFocusType();
            this.focusGravity = configuration.getFocusGravity();
        }
    }

    private void setUsageId(String materialIntroViewId) {
        this.materialIntroViewId = materialIntroViewId;
    }

    private void setListener(MaterialIntroListener materialIntroListener) {
        this.materialIntroListener = materialIntroListener;
    }

    private void setPerformClick(boolean isPerformClick) {
        this.isPerformClick = isPerformClick;
    }

    private void setInfoViewConfiguration(InfoViewConfiguration infoViewConfiguration) {
        this.infoViewConfiguration = infoViewConfiguration;
    }

    private void setClickFocusThenDismiss(boolean isClickFocusThenDismiss) {
        this.isClickFocusThenDismiss = isClickFocusThenDismiss;
    }


    /**
     * Builder Class
     */
    public static class Builder {

        private MaterialIntroView materialIntroView;

        private Focus focusType = Focus.MINIMUM;

        public Builder(Activity activity) {
            materialIntroView = new MaterialIntroView(activity);
        }

        public Builder(MaterialIntroView materialIntroView) {
            this.materialIntroView = materialIntroView;
        }

        public Builder setMaskColor(int maskColor) {
            materialIntroView.setMaskColor(maskColor);
            return this;
        }

        public Builder setDelayMillis(int delayMillis) {
            materialIntroView.setDelay(delayMillis);
            return this;
        }

        public Builder enableFadeAnimation(boolean isFadeAnimationEnabled) {
            materialIntroView.enableFadeAnimation(isFadeAnimationEnabled);
            return this;
        }

        public Builder setShape(ShapeType shape) {
            materialIntroView.setShapeType(shape);
            return this;
        }

        public Builder setFocusType(Focus focusType) {
            materialIntroView.setFocusType(focusType);
            return this;
        }

        public Builder setFocusGravity(FocusGravity focusGravity) {
            materialIntroView.setFocusGravity(focusGravity);
            return this;
        }

        public Builder setTarget(View view) {
            materialIntroView.setTarget(new ViewTarget(view));
            return this;
        }

        public Builder setTargetPadding(int padding) {
            materialIntroView.setPadding(padding);
            return this;
        }

        public Builder setTextColor(int textColor) {
            materialIntroView.setColorTextViewInfo(textColor);
            return this;
        }

        public Builder setInfoText(String infoText) {
            materialIntroView.enableInfoDialog(true);
            materialIntroView.setTextViewInfo(infoText);
            return this;
        }

        public Builder setInfoTextSize(int textSize) {
            materialIntroView.setTextViewInfoSize(textSize);
            return this;
        }

        public Builder dismissOnTouch(boolean dismissOnTouch) {
            materialIntroView.setDismissOnTouch(dismissOnTouch);
            return this;
        }

        public Builder setUsageId(String materialIntroViewId) {
            materialIntroView.setUsageId(materialIntroViewId);
            return this;
        }

        public Builder enableDotAnimation(boolean isDotAnimationEnabled) {
            materialIntroView.enableDotView(isDotAnimationEnabled);
            return this;
        }

        public Builder enableIcon(boolean isImageViewIconEnabled) {
            materialIntroView.enableImageViewIcon(isImageViewIconEnabled);
            return this;
        }

        public Builder setIdempotent(boolean idempotent) {
            materialIntroView.setIdempotent(idempotent);
            return this;
        }

        public Builder setConfiguration(MaterialIntroConfiguration configuration) {
            materialIntroView.setConfiguration(configuration);
            return this;
        }

        public Builder setInfoViewConfiguration(InfoViewConfiguration configuration) {
            materialIntroView.setInfoViewConfiguration(configuration);
            return this;
        }

        public Builder setListener(MaterialIntroListener materialIntroListener) {
            materialIntroView.setListener(materialIntroListener);
            return this;
        }

        public Builder setCustomShape(Shape shape) {
            materialIntroView.usesCustomShape = true;
            materialIntroView.setShape(shape);
            return this;
        }

        public Builder performClick(boolean isPerformClick) {
            materialIntroView.setPerformClick(isPerformClick);
            return this;
        }

        /**
         * set if click the focus area the dismiss intro view
         *
         * @param isClickThenDismiss
         * @return
         */
        public Builder clickFocusThenDismiss(boolean isClickThenDismiss) {
            materialIntroView.setClickFocusThenDismiss(isClickThenDismiss);
            return this;
        }

        public MaterialIntroView build() {
            if (materialIntroView.usesCustomShape) {
                return materialIntroView;
            }

            // no custom shape supplied, build our own
            Shape shape;

            if (materialIntroView.shapeType == ShapeType.CIRCLE) {
                shape = new Circle(
                        materialIntroView.targetView,
                        materialIntroView.focusType,
                        materialIntroView.focusGravity,
                        materialIntroView.padding);
            } else {
                shape = new Rect(
                        materialIntroView.targetView,
                        materialIntroView.focusType,
                        materialIntroView.focusGravity,
                        materialIntroView.padding);
            }

            materialIntroView.setShape(shape);
            return materialIntroView;
        }

        /**
         * this method is deprecated,change to use MaterialIntroView.show() method directly
         *
         * @return MaterialIntroView
         */
        @Deprecated
        public MaterialIntroView show() {
            build().show();
            return materialIntroView;
        }

    }

}
