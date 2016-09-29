package com.akexorcist.library.flexystepindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 9/28/2016 AD.
 */

@SuppressWarnings({"unused", "DefaultFileTemplate"})
public class FlexyStepIndicator extends LinearLayout implements View.OnClickListener {
    private static final int DEFAULT_COLOR = Color.parseColor("#DDDDDD");
    private List<Integer> descriptionResIdList;
    private List<String> descriptionStringList;
    private int currentIndex = -1;
    private int descriptionTextColor;
    private int descriptionTextSize;
    private int numberBackgroundResId;
    private int numberTextSize;
    private int numberTextColor;
    private int numberSize;
    private int doneIconResId;
    private int doneBackgroundResId;
    private int lineActiveColor;
    private int lineInactiveColor;
    private int lineHeight;
    private int lineLength;
    private int lineDashCount;
    private boolean isStepClickable;

    private List<StepProperty> stepPropertyList;
    private StepClickListener stepClickListener;

    public FlexyStepIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public FlexyStepIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlexyStepIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setupStyleable(attrs);
        setupThing();
    }

    private void setupStyleable(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlexyStepIndicator);
        descriptionTextColor = typedArray.getResourceId(R.styleable.FlexyStepIndicator_flexy_descriptionTextColor, -1);
        descriptionTextSize = (int) typedArray.getDimension(R.styleable.FlexyStepIndicator_flexy_descriptionTextSize, 16);
        numberBackgroundResId = typedArray.getResourceId(R.styleable.FlexyStepIndicator_flexy_numberBackgroundDrawable, -1);
        numberTextSize = (int) typedArray.getDimension(R.styleable.FlexyStepIndicator_flexy_numberTextSize, 16);
        numberTextColor = typedArray.getResourceId(R.styleable.FlexyStepIndicator_flexy_numberTextColor, -1);
        numberSize = (int) typedArray.getDimension(R.styleable.FlexyStepIndicator_flexy_numberSize, 30);
        doneIconResId = typedArray.getResourceId(R.styleable.FlexyStepIndicator_flexy_doneIconDrawable, -1);
        doneBackgroundResId = typedArray.getResourceId(R.styleable.FlexyStepIndicator_flexy_doneBackgroundDrawable, -1);
        lineActiveColor = typedArray.getColor(R.styleable.FlexyStepIndicator_flexy_lineActiveColor, DEFAULT_COLOR);
        lineInactiveColor = typedArray.getColor(R.styleable.FlexyStepIndicator_flexy_lineInactiveColor, DEFAULT_COLOR);
        lineHeight = (int) typedArray.getDimension(R.styleable.FlexyStepIndicator_flexy_lineHeight, 4);
        lineLength = (int) typedArray.getDimension(R.styleable.FlexyStepIndicator_flexy_lineLength, 30);
        lineDashCount = typedArray.getInt(R.styleable.FlexyStepIndicator_flexy_lineDashCount, 5);
        typedArray.recycle();
    }

    private void setupThing() {
        stepPropertyList = new ArrayList<>();
    }

    /**
     * Set current step index that will be selected.
     *
     * @param selectedIndex an index of the step. The step will be set to all done
     *                      if current step index is more than actual step size
     */
    public void setCurrentIndex(int selectedIndex) {
        this.currentIndex = selectedIndex;
        updateView();
    }

    /**
     * Get current selected step index
     *
     * @return the selected step index
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Set all step to done
     */
    public void setAllStepDone() {
        if (stepPropertyList != null) {
            setCurrentIndex(stepPropertyList.size() + 1);
        }
    }

    /**
     * Set description text color with color resource.
     * You can use selector to set difference color when step is active
     * or use color resource directly
     * <pre>
     * {@code
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     *     <item android:color="#ffffff" android:state_selected="true" />
     *     <item android:color="#8dc73c" />
     * </selector>
     * }
     * </pre>
     *
     * @param resId a color resource
     */
    public void setStepDescriptionTextColorRes(int resId) {
        this.descriptionTextColor = resId;
        updateView();
    }

    /**
     * Set step description text size.
     *
     * @param size a size of step description text (px)
     */
    public void setStepDescriptionTextSize(int size) {
        this.descriptionTextSize = size;
        updateView();
    }

    /**
     * Set step number text background with drawable resource
     * You can use selector to set difference drawable when step is active
     * or use drawable resource directly
     * <pre>
     * {@code
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     *     <item android:drawable="@drawable/any_drawable" android:state_selected="true" />
     *     <item android:drawable="@drawable/any_drawable" />
     * </selector>
     * }
     * </pre>
     *
     * @param resId a resource id of drawable
     */
    public void setNumberBackgroundDrawableRes(int resId) {
        this.numberBackgroundResId = resId;
        updateView();
    }

    /**
     * Set step number text size.
     *
     * @param size a size of step number text (px)
     */
    public void setNumberTextSize(int size) {
        this.numberTextSize = size;
        updateView();
    }

    /**
     * Set step number text color with color resource.
     * You can use selector to set difference color when step is active
     * or use color resource directly
     * <pre>
     * {@code
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     *     <item android:color="#ffffff" android:state_selected="true" />
     *     <item android:color="#8dc73c" />
     * </selector>
     * }
     * </pre>
     *
     * @param resId a color resource
     */
    public void setNumberTextColorRes(int resId) {
        this.numberTextColor = resId;
        updateView();
    }

    /**
     * Set done icon/symbol with drawable resource
     * You can use selector to set difference drawable when step is active
     * or use drawable resource directly
     * <pre>
     * {@code
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     *     <item android:drawable="@drawable/any_drawable" android:state_selected="true" />
     *     <item android:drawable="@drawable/any_drawable" />
     * </selector>
     * }
     * </pre>
     *
     * @param resId a resource id of drawable
     */
    public void setDoneIconDrawableRes(int resId) {
        this.doneIconResId = resId;
        updateView();
    }

    /**
     * Set done icon/symbol background with drawable resource
     * You can use selector to set difference drawable when step is active
     * or use drawable resource directly
     * <pre>
     * {@code
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     *     <item android:drawable="@drawable/any_drawable" android:state_selected="true" />
     *     <item android:drawable="@drawable/any_drawable" />
     * </selector>
     * }
     * </pre>
     *
     * @param resId a resource id of drawable
     */
    public void setDoneBackgroundDrawableRes(int resId) {
        this.doneBackgroundResId = resId;
        updateView();
    }

    /**
     * Set height of the step line
     *
     * @param height a height of the step line (px)
     */
    public void setLineHeight(int height) {
        this.lineHeight = height;
        updateView();
    }

    /**
     * Set length of the step line.
     * This length should be the total of both line side (left and right)
     *
     * @param length a length of the step line (px)
     */
    public void setLineLength(int length) {
        this.lineLength = length;
        updateView();
    }

    /**
     * Set number of dash in the step line
     * This number should be the total of both line side (left and right)
     *
     * @param lineDashCount a number of dash in the step line
     */
    public void setLineDashCount(int lineDashCount) {
        this.lineDashCount = lineDashCount;
        updateView();
    }

    /**
     * Set step line color with color resource when active
     *
     * @param color a color
     */
    public void setLineActiveColor(int color) {
        this.lineActiveColor = color;
        updateView();
    }

    /**
     * Set step line color with color resource when inactive
     *
     * @param color a color
     */
    public void setLineInactiveColor(int color) {
        this.lineInactiveColor = color;
        updateView();
    }

    /**
     * Set step view can be clickable or not
     *
     * @param clickable a boolean of step view clickable state
     */
    public void setStepClickable(boolean clickable) {
        this.isStepClickable = clickable;
        updateStepClickableState();
    }

    /**
     * Get step view clickable state
     *
     * @return the step view clickable state
     */
    public boolean isStepClickable() {
        return this.isStepClickable;
    }

    /**
     * Set event listener for step view click event
     *
     * @param listener a step click listener
     */
    public void setStepClickListener(StepClickListener listener) {
        this.stepClickListener = listener;
    }

    /**
     * Invalidate this view
     */
    @Override
    public void invalidate() {
        setupThing();
        updateIndicatorView();
        updateStepClickableState();
        updateView();
    }

    private void updateStepClickableState() {
        if (stepPropertyList != null) {
            for (StepProperty stepProperty : stepPropertyList) {
                stepProperty.getRootView().setClickable(isStepClickable());
            }
        }
    }

    private void updateView() {
        if (stepPropertyList != null) {
            int selectedIndex = getCurrentIndex();
            for (int index = 0; index < stepPropertyList.size(); index++) {
                updateRootViewSelection(stepPropertyList.get(index), index, selectedIndex);
                updateIconDrawableVisibility(stepPropertyList.get(index), index, selectedIndex);
                updateLeftDividerDrawable(stepPropertyList.get(index), index, selectedIndex);
                updateRightDividerDrawable(stepPropertyList.get(index), index, selectedIndex);
                updateActiveState(stepPropertyList.get(index), index, selectedIndex);
            }
        }
    }

    private void updateRootViewSelection(StepProperty stepProperty, int index, int selectedIndex) {
        stepProperty.getRootView().setSelected(index <= selectedIndex);
    }

    private void updateIconDrawableVisibility(StepProperty stepProperty, int index, int selectedIndex) {
        if (stepProperty.getDoneImageView().isEnabled()) {
            updateDoneVisibility(stepProperty.getNumberTextView(), stepProperty.getDoneImageView(), index, selectedIndex);
        }
    }

    /**
     * Update an visibility of done icon and number of the step text.
     * <p/>
     * You can custom code to design how these view can be show or hide depending on current
     * step index as you want
     *
     * @param tvNumber      a TextView that display the number of the that step
     * @param ivIcon        a ImageView that display done icon or image in that step
     * @param index         expect step index that will be custom in this method
     * @param selectedIndex current step index that has selected
     */
    protected void updateDoneVisibility(TextView tvNumber, ImageView ivIcon, int index, int selectedIndex) {
        if (index < selectedIndex) {
            tvNumber.setVisibility(View.INVISIBLE);
            ivIcon.setVisibility(View.VISIBLE);
        } else {
            tvNumber.setVisibility(View.VISIBLE);
            ivIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void updateLeftDividerDrawable(StepProperty stepProperty, int index, int selectedIndex) {
        updateDivider(stepProperty.getLeftDividerView(), true, index > selectedIndex);
    }

    private void updateRightDividerDrawable(StepProperty stepProperty, int index, int selectedIndex) {
        updateDivider(stepProperty.getRightDividerView(), false, index >= selectedIndex);
    }

    private void updateActiveState(StepProperty stepProperty, int index, int selectedIndex) {
        stepProperty.setActive(index <= selectedIndex);
        stepProperty.setActiveWithLastIndex(index == selectedIndex);
    }

    /**
     * Set description with string resource id to show in step view.
     * <p/>
     * This method should be called to setup step view
     * Step view won't show if you didn't call this method or
     * {@link #setStepDescriptionList(List String descriptionList)} method
     *
     * @param descriptionList List of string resource for step description
     */
    public void setStepDescriptionResourceList(List<Integer> descriptionList) {
        this.descriptionResIdList = descriptionList;
        descriptionStringList = null;
        currentIndex = -1;
        updateIndicatorView();
    }


    /**
     * Set description with string to show in step view.
     * <p/>
     * This method should be called to setup step view
     * Step view won't show if you didn't call this method or
     * {@link #setStepDescriptionResourceList(List Integer descriptionList)} method
     *
     * @param descriptionList List of string for step description
     */
    public void setStepDescriptionList(List<String> descriptionList) {
        this.descriptionStringList = descriptionList;
        descriptionResIdList = null;
        currentIndex = -1;
        updateIndicatorView();
    }

    private void updateIndicatorView() {
        int stepCount = 0;
        if (descriptionResIdList != null) {
            stepCount = descriptionResIdList.size();
        } else if (descriptionStringList != null) {
            stepCount = descriptionStringList.size();
        }
        removeAllViews();
        stepPropertyList.clear();
        for (int index = 0; index < stepCount; index++) {
            View view = createStepView(index, stepCount);
            addView(view);
            stepPropertyList.add(getStepProperty(view));
        }
    }

    private void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        params.width = width;
        view.setLayoutParams(params);
    }

    @SuppressWarnings("deprecation")
    private View createStepView(int index, int totalStep) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_flexy_step_indicator, this, false);
        view.setSelected(false);
        view.setOnClickListener(this);

        // Number Layout Container
        FrameLayout layoutNumberContainer = (FrameLayout) view.findViewById(R.id.flexy_step_indicator_layout_number_container);
        setViewSize(layoutNumberContainer, numberSize, numberSize);

        // Done Image View
        ImageView ivDone = (ImageView) view.findViewById(R.id.flexy_step_indicator_iv_done);
        ivDone.setVisibility(View.INVISIBLE);
        ivDone.setEnabled(true);
        if (doneIconResId != -1) {
            ivDone.setImageResource(doneIconResId);
        }
        if (doneBackgroundResId != -1) {
            ivDone.setBackgroundResource(doneBackgroundResId);
        }
        if (doneBackgroundResId == -1 && doneIconResId == -1) {
            ivDone.setEnabled(false);
        }

        // Number Text View
        TextView tvNumber = (TextView) view.findViewById(R.id.flexy_step_indicator_tv_number);
        tvNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, numberTextSize);
        tvNumber.setText(String.valueOf(index + 1));
        if (numberBackgroundResId != -1) {
            tvNumber.setBackgroundResource(numberBackgroundResId);
        }
        if (numberTextColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvNumber.setTextColor(getResources().getColorStateList(numberTextColor, getContext().getTheme()));
            } else {
                tvNumber.setTextColor(getResources().getColorStateList(numberTextColor));
            }
        }

        // Left Divider
        View viewLeftDivider = view.findViewById(R.id.flexy_step_indicator_view_left_line);
        if (index == 0) {
            viewLeftDivider.setVisibility(View.INVISIBLE);
        }
        updateDivider(viewLeftDivider, true, true);
        setLineSize(viewLeftDivider, lineDashCount, lineLength, lineHeight);

        // Right Divider
        View viewRightDivider = view.findViewById(R.id.flexy_step_indicator_view_right_divider);
        if (index == totalStep - 1) {
            viewRightDivider.setVisibility(View.INVISIBLE);
        }
        updateDivider(viewRightDivider, false, true);
        setLineSize(viewRightDivider, lineDashCount, lineLength, lineHeight);

        // Description Text View
        TextView tvDescription = (TextView) view.findViewById(R.id.flexy_step_indicator_tv_description);
        String description = getDescriptionText(index);
        if (description != null) {
            tvDescription.setText(description);
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.INVISIBLE);
        }
        tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, descriptionTextSize);
        if (descriptionTextColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvDescription.setTextColor(getResources().getColorStateList(descriptionTextColor, getContext().getTheme()));
            } else {
                tvDescription.setTextColor(getResources().getColorStateList(descriptionTextColor));
            }
        }

        return view;
    }

    private String getDescriptionText(int index) {
        if (descriptionResIdList != null && descriptionResIdList.size() > index) {
            return getResources().getString(descriptionResIdList.get(index));
        }
        if (descriptionStringList != null && descriptionStringList.size() > index) {
            return descriptionStringList.get(index);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private void updateDivider(View view, boolean isMirror, boolean isInactive) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(createLineDrawable(lineDashCount, lineLength, lineHeight, lineActiveColor, lineInactiveColor, isMirror, isInactive));
        } else {
            view.setBackgroundDrawable(createLineDrawable(lineDashCount, lineLength, lineHeight, lineActiveColor, lineInactiveColor, isMirror, isInactive));
        }
    }

    private void setLineSize(View view, int dashCount, int lineLength, int lineHeight) {
        setViewSize(view, lineLength, lineHeight);
    }

    private StepProperty getStepProperty(View rootView) {
        TextView tvNumber = (TextView) rootView.findViewById(R.id.flexy_step_indicator_tv_number);
        View viewLeftDivider = rootView.findViewById(R.id.flexy_step_indicator_view_left_line);
        View viewRightDivider = rootView.findViewById(R.id.flexy_step_indicator_view_right_divider);
        ImageView ivDone = (ImageView) rootView.findViewById(R.id.flexy_step_indicator_iv_done);
        StepProperty stepProperty = new StepProperty();
        stepProperty.setRootView(rootView);
        stepProperty.setLeftDividerView(viewLeftDivider);
        stepProperty.setRightDividerView(viewRightDivider);
        stepProperty.setNumberTextView(tvNumber);
        stepProperty.setDoneImageView(ivDone);
        stepProperty.setActive(false);
        return stepProperty;
    }

    private Drawable createLineDrawable(int dashCount, int lineLength, int lineHeight, int normalColor, int dashColor, boolean isMirror, boolean isActive) {
        int totalWidth = (int) ((dashCount - 0.5f) * lineLength);
        Paint paint = new Paint();
        if (isActive) {
            paint.setColor(dashColor);
        } else {
            paint.setColor(normalColor);
        }
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, lineHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (isActive) {
            drawActiveLine(canvas, dashCount, lineHeight, lineLength, paint);
        } else {
            drawInactiveLine(canvas, dashCount, lineHeight, lineLength, paint);
        }
        if (isMirror) {
            Matrix matrix = new Matrix();
            matrix.preScale(-1, 1);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        }
        return new BitmapDrawable(getResources(), bitmap);
    }

    /**
     * Draw the step line with canvas when expect step is active
     * <p/>
     * Horizontal line of step that link to side step was create by canvas.
     * So you can custom the step line with your code
     *
     * @param canvas     a canvas instance to draw the step line
     * @param dashCount  a number of dash for the step line (include left and right side)
     * @param lineHeight a size of step line height (px)
     * @param lineLength a size of step line width in each side (px)
     * @param paint      a paint instance to draw something on canvas
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    protected void drawActiveLine(Canvas canvas, int dashCount, int lineHeight, int lineLength, Paint paint) {
        for (int i = 0; i < dashCount; i++) {
            float x1 = lineLength * 2 * i;
            float x2 = x1 + lineLength;
            float y1 = 0;
            float y2 = lineHeight;
            canvas.drawRect(x1, y1, x2, y2, paint);
        }
    }

    /**
     * Draw the step line with canvas when expect step is inactive
     * <p/>
     * Horizontal line of step that link to side step was create by canvas.
     * So you can custom the step line with your code
     *
     * @param canvas     a canvas instance to draw the step line
     * @param dashCount  a number of dash for the step line (include left and right side)
     * @param lineHeight a size of step line height (px)
     * @param lineLength a size of step line width in each side (px)
     * @param paint      a paint instance to draw something on canvas
     */
    protected void drawInactiveLine(Canvas canvas, int dashCount, int lineHeight, int lineLength, Paint paint) {
        canvas.drawRect(0, 0, canvas.getWidth(), lineHeight, paint);
    }

    @Override
    public void onClick(View view) {
        int stepIndex = getStepViewIndexClicked(view);
        if (stepIndex != -1) {
            setCurrentIndex(stepIndex);
            if (stepClickListener != null) {
                stepClickListener.onStepClick(stepIndex);
            }
        }
    }

    private int getStepViewIndexClicked(View view) {
        if (stepPropertyList != null) {
            for (int index = 0; index < stepPropertyList.size(); index++) {
                StepProperty stepProperty = stepPropertyList.get(index);
                if (stepProperty.getRootView() == view) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentIndex = this.currentIndex;
        ss.descriptionTextColor = this.descriptionTextColor;
        ss.descriptionTextSize = this.descriptionTextSize;
        ss.numberBackgroundResId = this.numberBackgroundResId;
        ss.numberTextSize = this.numberTextSize;
        ss.numberTextColor = this.numberTextColor;
        ss.numberSize = this.numberSize;
        ss.doneIconResId = this.doneIconResId;
        ss.doneBackgroundResId = this.doneBackgroundResId;
        ss.lineActiveColor = this.lineActiveColor;
        ss.lineInactiveColor = this.lineInactiveColor;
        ss.lineHeight = this.lineHeight;
        ss.lineLength = this.lineLength;
        ss.lineDashCount = this.lineDashCount;
        ss.isStepClickable = this.isStepClickable;
        ss.descriptionResIdList = this.descriptionResIdList;
        ss.descriptionStringList = this.descriptionStringList;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.currentIndex = ss.currentIndex;
        this.descriptionTextColor = ss.descriptionTextColor;
        this.descriptionTextSize = ss.descriptionTextSize;
        this.numberBackgroundResId = ss.numberBackgroundResId;
        this.numberTextSize = ss.numberTextSize;
        this.numberTextColor = ss.numberTextColor;
        this.numberSize = ss.numberSize;
        this.doneIconResId = ss.doneIconResId;
        this.doneBackgroundResId = ss.doneBackgroundResId;
        this.lineActiveColor = ss.lineActiveColor;
        this.lineInactiveColor = ss.lineInactiveColor;
        this.lineHeight = ss.lineHeight;
        this.lineLength = ss.lineLength;
        this.lineDashCount = ss.lineDashCount;
        this.isStepClickable = ss.isStepClickable;
        this.descriptionResIdList = ss.descriptionResIdList;
        this.descriptionStringList = ss.descriptionStringList;
        invalidate();
    }

    private static class SavedState extends BaseSavedState {
        List<Integer> descriptionResIdList;
        List<String> descriptionStringList;
        int currentIndex;
        int descriptionTextColor;
        int descriptionTextSize;
        int numberBackgroundResId;
        int numberTextSize;
        int numberTextColor;
        int numberSize;
        int doneIconResId;
        int doneBackgroundResId;
        int lineActiveColor;
        int lineInactiveColor;
        int lineHeight;
        int lineLength;
        int lineDashCount;
        boolean isStepClickable;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentIndex = in.readInt();
            this.descriptionTextColor = in.readInt();
            this.descriptionTextSize = in.readInt();
            this.numberBackgroundResId = in.readInt();
            this.numberTextSize = in.readInt();
            this.numberTextColor = in.readInt();
            this.numberSize = in.readInt();
            this.doneIconResId = in.readInt();
            this.doneBackgroundResId = in.readInt();
            this.lineActiveColor = in.readInt();
            this.lineInactiveColor = in.readInt();
            this.lineHeight = in.readInt();
            this.lineLength = in.readInt();
            this.lineDashCount = in.readInt();
            this.isStepClickable = in.readInt() != 0;
            this.descriptionStringList = restoreDescriptionStringList(in);
            this.descriptionResIdList = restoreDescriptionResIdList(in);
        }

        private List<String> restoreDescriptionStringList(Parcel in) {
            List<String> descriptionStringList = in.createStringArrayList();
            in.readStringList(descriptionStringList);
            return descriptionStringList;
        }

        private List<Integer> restoreDescriptionResIdList(Parcel in) {
            int[] descriptionResIds = in.createIntArray();
            in.readIntArray(descriptionResIds);
            descriptionResIdList = new ArrayList<>();
            for (int descriptionResId : descriptionResIds) {
                descriptionResIdList.add(descriptionResId);
            }
            return descriptionResIdList;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentIndex);
            out.writeInt(this.descriptionTextColor);
            out.writeInt(this.descriptionTextSize);
            out.writeInt(this.numberBackgroundResId);
            out.writeInt(this.numberTextSize);
            out.writeInt(this.numberTextColor);
            out.writeInt(this.numberSize);
            out.writeInt(this.doneIconResId);
            out.writeInt(this.doneBackgroundResId);
            out.writeInt(this.lineActiveColor);
            out.writeInt(this.lineInactiveColor);
            out.writeInt(this.lineHeight);
            out.writeInt(this.lineLength);
            out.writeInt(this.lineDashCount);
            out.writeInt(this.isStepClickable ? 1 : 0);
            out.writeStringArray(saveDescriptionStringList(this.descriptionStringList));
            out.writeIntArray(saveDescriptionResIdList(this.descriptionResIdList));
        }

        private String[] saveDescriptionStringList(List<String> descriptionStringList) {
            String[] descriptionStrings = new String[descriptionStringList.size()];
            for (int index = 0; index < descriptionStringList.size(); index++) {
                String descriptionString = descriptionStringList.get(index);
                descriptionStrings[index] = descriptionString;
            }
            return descriptionStrings;
        }

        private int[] saveDescriptionResIdList(List<Integer> descriptionResIdList) {
            int[] descriptionResIds = new int[descriptionResIdList.size()];
            for (int index = 0; index < descriptionResIdList.size(); index++) {
                Integer descriptionResId = descriptionResIdList.get(index);
                descriptionResIds[index] = descriptionResId;
            }
            return descriptionResIds;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private static class StepProperty {
        View rootView;
        TextView tvNumber;
        View viewLeftDivider;
        View viewRightDivider;
        ImageView ivDone;
        boolean isActive;
        boolean isActiveWithLastIndex;

        StepProperty() {
        }

        View getRootView() {
            return rootView;
        }

        void setRootView(View rootView) {
            this.rootView = rootView;
        }

        TextView getNumberTextView() {
            return tvNumber;
        }

        void setNumberTextView(TextView tvNumber) {
            this.tvNumber = tvNumber;
        }

        View getLeftDividerView() {
            return viewLeftDivider;
        }

        void setLeftDividerView(View viewLeftDivider) {
            this.viewLeftDivider = viewLeftDivider;
        }

        View getRightDividerView() {
            return viewRightDivider;
        }

        void setRightDividerView(View viewRightDivider) {
            this.viewRightDivider = viewRightDivider;
        }

        ImageView getDoneImageView() {
            return ivDone;
        }

        void setDoneImageView(ImageView ivDone) {
            this.ivDone = ivDone;
        }

        boolean isActive() {
            return isActive;
        }

        void setActive(boolean active) {
            isActive = active;
        }

        boolean isActiveWithLastIndex() {
            return isActiveWithLastIndex;
        }

        void setActiveWithLastIndex(boolean activeWithLastIndex) {
            isActiveWithLastIndex = activeWithLastIndex;
        }
    }

    public interface StepClickListener {
        void onStepClick(int index);
    }
}
