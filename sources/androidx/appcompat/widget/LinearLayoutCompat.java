package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.appcompat.R$styleable;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public abstract class LinearLayoutCompat extends ViewGroup {
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    /* loaded from: classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        int[] iArr = R$styleable.LinearLayoutCompat;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, iArr, i, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes.getWrappedTypeArray(), i, 0);
        int i2 = obtainStyledAttributes.getInt(R$styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R$styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R$styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R$styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R$styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R$styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R$styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R$styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R$styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).height == -1) {
                    int i4 = ((LinearLayout.LayoutParams) layoutParams).width;
                    ((LinearLayout.LayoutParams) layoutParams).width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    ((LinearLayout.LayoutParams) layoutParams).width = i4;
                }
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).width == -1) {
                    int i4 = ((LinearLayout.LayoutParams) layoutParams).height;
                    ((LinearLayout.LayoutParams) layoutParams).height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    ((LinearLayout.LayoutParams) layoutParams).height = i4;
                }
            }
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int i;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i2)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                drawVerticalDivider(canvas, isLayoutRtl ? virtualChildAt.getRight() + ((LinearLayout.LayoutParams) layoutParams).rightMargin : (virtualChildAt.getLeft() - ((LinearLayout.LayoutParams) layoutParams).leftMargin) - this.mDividerWidth);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    left = virtualChildAt2.getLeft();
                    i = ((LinearLayout.LayoutParams) layoutParams2).leftMargin;
                    right = (left - i) - this.mDividerWidth;
                } else {
                    right = virtualChildAt2.getRight() + ((LinearLayout.LayoutParams) layoutParams2).rightMargin;
                }
            } else if (isLayoutRtl) {
                right = getPaddingLeft();
            } else {
                left = getWidth();
                i = getPaddingRight();
                right = (left - i) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt.getLayoutParams())).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            drawHorizontalDivider(canvas, virtualChildAt2 == null ? (getHeight() - getPaddingBottom()) - this.mDividerHeight : virtualChildAt2.getBottom() + ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt2.getLayoutParams())).bottomMargin);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.View
    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i2 = this.mBaselineAlignedChildIndex;
        if (childCount <= i2) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(i2);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            }
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
        int i3 = this.mBaselineChildTop;
        if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
            if (i == 16) {
                i3 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
            } else if (i == 80) {
                i3 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
            }
        }
        return i3 + ((LinearLayout.LayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        }
        if ((this.mShowDividers & 2) == 0) {
            return false;
        }
        for (int i2 = i - 1; i2 >= 0; i2--) {
            if (getChildAt(i2).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i14 = i4 - i2;
        int paddingBottom = i14 - getPaddingBottom();
        int paddingBottom2 = (i14 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i15 = this.mGravity;
        int i16 = i15 & 112;
        boolean z = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(8388615 & i15, ViewCompat.getLayoutDirection(this));
        int paddingLeft = absoluteGravity != 1 ? absoluteGravity != 5 ? getPaddingLeft() : ((getPaddingLeft() + i3) - i) - this.mTotalLength : getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        if (isLayoutRtl) {
            i5 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i5 = 0;
            i6 = 1;
        }
        int i17 = 0;
        while (i17 < virtualChildCount) {
            int i18 = i5 + (i6 * i17);
            View virtualChildAt = getVirtualChildAt(i18);
            if (virtualChildAt == null) {
                paddingLeft += measureNullChild(i18);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i19 = i17;
                if (z) {
                    i7 = virtualChildCount;
                    if (((LinearLayout.LayoutParams) layoutParams).height != -1) {
                        i8 = virtualChildAt.getBaseline();
                        i9 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                        if (i9 < 0) {
                            i9 = i16;
                        }
                        i10 = i9 & 112;
                        i11 = i16;
                        if (i10 != 16) {
                            i12 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + ((LinearLayout.LayoutParams) layoutParams).topMargin) - ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        } else if (i10 == 48) {
                            i12 = ((LinearLayout.LayoutParams) layoutParams).topMargin + paddingTop;
                            if (i8 != -1) {
                                i12 += iArr[1] - i8;
                            }
                        } else if (i10 != 80) {
                            i12 = paddingTop;
                        } else {
                            i12 = (paddingBottom - measuredHeight) - ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                            if (i8 != -1) {
                                i12 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i8);
                            }
                        }
                        if (hasDividerBeforeChildAt(i18)) {
                            paddingLeft += this.mDividerWidth;
                        }
                        int i20 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                        i13 = paddingTop;
                        setChildFrame(virtualChildAt, i20 + getLocationOffset(virtualChildAt), i12, measuredWidth, measuredHeight);
                        int nextLocationOffset = i20 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                        i17 = i19 + getChildrenSkipCount(virtualChildAt, i18);
                        paddingLeft = nextLocationOffset;
                        i17++;
                        virtualChildCount = i7;
                        i16 = i11;
                        paddingTop = i13;
                    }
                } else {
                    i7 = virtualChildCount;
                }
                i8 = -1;
                i9 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (i9 < 0) {
                }
                i10 = i9 & 112;
                i11 = i16;
                if (i10 != 16) {
                }
                if (hasDividerBeforeChildAt(i18)) {
                }
                int i202 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                i13 = paddingTop;
                setChildFrame(virtualChildAt, i202 + getLocationOffset(virtualChildAt), i12, measuredWidth, measuredHeight);
                int nextLocationOffset2 = i202 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                i17 = i19 + getChildrenSkipCount(virtualChildAt, i18);
                paddingLeft = nextLocationOffset2;
                i17++;
                virtualChildCount = i7;
                i16 = i11;
                paddingTop = i13;
            }
            i13 = paddingTop;
            i7 = virtualChildCount;
            i11 = i16;
            i17++;
            virtualChildCount = i7;
            i16 = i11;
            paddingTop = i13;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x009f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int paddingLeft = getPaddingLeft();
        int i7 = i3 - i;
        int paddingRight = i7 - getPaddingRight();
        int paddingRight2 = (i7 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i8 = this.mGravity;
        int i9 = i8 & 112;
        int i10 = i8 & 8388615;
        int paddingTop = i9 != 16 ? i9 != 80 ? getPaddingTop() : ((getPaddingTop() + i4) - i2) - this.mTotalLength : getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        int i11 = 0;
        while (i11 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i11);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(i11);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i12 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (i12 < 0) {
                    i12 = i10;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i12, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i5 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + ((LinearLayout.LayoutParams) layoutParams).leftMargin;
                } else if (absoluteGravity != 5) {
                    i6 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                    int i13 = i6;
                    if (hasDividerBeforeChildAt(i11)) {
                        paddingTop += this.mDividerHeight;
                    }
                    int i14 = paddingTop + ((LinearLayout.LayoutParams) layoutParams).topMargin;
                    setChildFrame(virtualChildAt, i13, i14 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    int nextLocationOffset = i14 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                    i11 += getChildrenSkipCount(virtualChildAt, i11);
                    paddingTop = nextLocationOffset;
                } else {
                    i5 = paddingRight - measuredWidth;
                }
                i6 = i5 - ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                int i132 = i6;
                if (hasDividerBeforeChildAt(i11)) {
                }
                int i142 = paddingTop + ((LinearLayout.LayoutParams) layoutParams).topMargin;
                setChildFrame(virtualChildAt, i132, i142 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                int nextLocationOffset2 = i142 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                i11 += getChildrenSkipCount(virtualChildAt, i11);
                paddingTop = nextLocationOffset2;
            }
            i11++;
        }
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* JADX WARN: Code restructure failed: missing block: B:179:0x03aa, code lost:
    
        if (r8 > 0) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x03ad, code lost:
    
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x03ae, code lost:
    
        r14.measure(android.view.View.MeasureSpec.makeMeasureSpec(r8, r3), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x03c1, code lost:
    
        r9 = android.view.View.combineMeasuredStates(r9, r14.getMeasuredState() & (-16777216));
        r0 = r0;
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x03be, code lost:
    
        if (r8 < 0) goto L169;
     */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int max;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        float f;
        int i10;
        boolean z;
        int baseline;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        boolean z2;
        boolean z3;
        View view;
        int max2;
        int i16;
        boolean z4;
        int measuredHeight;
        int childrenSkipCount;
        int baseline2;
        int i17;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z5 = this.mBaselineAligned;
        boolean z6 = this.mUseLargestChild;
        int i18 = 1073741824;
        boolean z7 = mode == 1073741824;
        float f2 = 0.0f;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        boolean z8 = false;
        int i24 = 0;
        boolean z9 = true;
        boolean z10 = false;
        while (true) {
            iArr = iArr3;
            if (i19 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i19);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i19);
            } else if (virtualChildAt.getVisibility() == 8) {
                i19 += getChildrenSkipCount(virtualChildAt, i19);
            } else {
                if (hasDividerBeforeChildAt(i19)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = ((LinearLayout.LayoutParams) layoutParams).weight;
                float f4 = f2 + f3;
                if (mode == i18 && ((LinearLayout.LayoutParams) layoutParams).width == 0 && f3 > 0.0f) {
                    int i25 = this.mTotalLength;
                    this.mTotalLength = z7 ? i25 + ((LinearLayout.LayoutParams) layoutParams).leftMargin + ((LinearLayout.LayoutParams) layoutParams).rightMargin : Math.max(i25, ((LinearLayout.LayoutParams) layoutParams).leftMargin + i25 + ((LinearLayout.LayoutParams) layoutParams).rightMargin);
                    if (z5) {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i15 = i19;
                        z2 = z6;
                        z3 = z5;
                        view = virtualChildAt;
                    } else {
                        i15 = i19;
                        z2 = z6;
                        z3 = z5;
                        view = virtualChildAt;
                        i16 = 1073741824;
                        z8 = true;
                        if (mode2 == i16 && ((LinearLayout.LayoutParams) layoutParams).height == -1) {
                            z4 = true;
                            z10 = true;
                        } else {
                            z4 = false;
                        }
                        int i26 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        measuredHeight = view.getMeasuredHeight() + i26;
                        i24 = View.combineMeasuredStates(i24, view.getMeasuredState());
                        if (z3 && (baseline2 = view.getBaseline()) != -1) {
                            i17 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                            if (i17 < 0) {
                                i17 = this.mGravity;
                            }
                            int i27 = (((i17 & 112) >> 4) & (-2)) >> 1;
                            iArr2[i27] = Math.max(iArr2[i27], baseline2);
                            iArr[i27] = Math.max(iArr[i27], measuredHeight - baseline2);
                        }
                        i21 = Math.max(i21, measuredHeight);
                        z9 = !z9 && ((LinearLayout.LayoutParams) layoutParams).height == -1;
                        if (((LinearLayout.LayoutParams) layoutParams).weight <= 0.0f) {
                            if (!z4) {
                                i26 = measuredHeight;
                            }
                            i23 = Math.max(i23, i26);
                        } else {
                            int i28 = i23;
                            if (!z4) {
                                i26 = measuredHeight;
                            }
                            i22 = Math.max(i22, i26);
                            i23 = i28;
                        }
                        int i29 = i15;
                        childrenSkipCount = getChildrenSkipCount(view, i29) + i29;
                        f2 = f4;
                        i19 = childrenSkipCount + 1;
                        iArr3 = iArr;
                        z6 = z2;
                        z5 = z3;
                        i18 = 1073741824;
                    }
                } else {
                    if (((LinearLayout.LayoutParams) layoutParams).width != 0 || f3 <= 0.0f) {
                        i14 = Integer.MIN_VALUE;
                    } else {
                        ((LinearLayout.LayoutParams) layoutParams).width = -2;
                        i14 = 0;
                    }
                    i15 = i19;
                    int i30 = i14;
                    z2 = z6;
                    z3 = z5;
                    measureChildBeforeLayout(virtualChildAt, i15, i, f4 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    if (i30 != Integer.MIN_VALUE) {
                        ((LinearLayout.LayoutParams) layoutParams).width = i30;
                    }
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    if (z7) {
                        view = virtualChildAt;
                        max2 = this.mTotalLength + ((LinearLayout.LayoutParams) layoutParams).leftMargin + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(view);
                    } else {
                        view = virtualChildAt;
                        int i31 = this.mTotalLength;
                        max2 = Math.max(i31, i31 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).leftMargin + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(view));
                    }
                    this.mTotalLength = max2;
                    if (z2) {
                        i20 = Math.max(measuredWidth, i20);
                    }
                }
                i16 = 1073741824;
                if (mode2 == i16) {
                }
                z4 = false;
                int i262 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                measuredHeight = view.getMeasuredHeight() + i262;
                i24 = View.combineMeasuredStates(i24, view.getMeasuredState());
                if (z3) {
                    i17 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                    if (i17 < 0) {
                    }
                    int i272 = (((i17 & 112) >> 4) & (-2)) >> 1;
                    iArr2[i272] = Math.max(iArr2[i272], baseline2);
                    iArr[i272] = Math.max(iArr[i272], measuredHeight - baseline2);
                }
                i21 = Math.max(i21, measuredHeight);
                if (z9) {
                }
                if (((LinearLayout.LayoutParams) layoutParams).weight <= 0.0f) {
                }
                int i292 = i15;
                childrenSkipCount = getChildrenSkipCount(view, i292) + i292;
                f2 = f4;
                i19 = childrenSkipCount + 1;
                iArr3 = iArr;
                z6 = z2;
                z5 = z3;
                i18 = 1073741824;
            }
            childrenSkipCount = i19;
            z2 = z6;
            z3 = z5;
            i19 = childrenSkipCount + 1;
            iArr3 = iArr;
            z6 = z2;
            z5 = z3;
            i18 = 1073741824;
        }
        boolean z11 = z6;
        boolean z12 = z5;
        int i32 = i21;
        int i33 = i22;
        int i34 = i23;
        int i35 = i24;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        int i36 = iArr2[1];
        if (i36 == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            max = i32;
            i3 = i35;
        } else {
            i3 = i35;
            max = Math.max(i32, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(i36, iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z11 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i37 = 0;
            while (i37 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i37);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i37);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i37 += getChildrenSkipCount(virtualChildAt2, i37);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    int i38 = this.mTotalLength;
                    if (z7) {
                        this.mTotalLength = i38 + ((LinearLayout.LayoutParams) layoutParams2).leftMargin + i20 + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        i13 = max;
                        this.mTotalLength = Math.max(i38, i38 + i20 + ((LinearLayout.LayoutParams) layoutParams2).leftMargin + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2));
                        i37++;
                        max = i13;
                    }
                }
                i13 = max;
                i37++;
                max = i13;
            }
        }
        int i39 = max;
        int paddingLeft = this.mTotalLength + getPaddingLeft() + getPaddingRight();
        this.mTotalLength = paddingLeft;
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i, 0);
        int i40 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z8 || (i40 != 0 && f2 > 0.0f)) {
            float f5 = this.mWeightSum;
            if (f5 > 0.0f) {
                f2 = f5;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i41 = i33;
            i4 = i3;
            int i42 = -1;
            int i43 = 0;
            while (i43 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i43);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i8 = i40;
                    i9 = virtualChildCount;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f6 = ((LinearLayout.LayoutParams) layoutParams3).weight;
                    if (f6 > 0.0f) {
                        int i44 = (int) ((i40 * f6) / f2);
                        float f7 = f2 - f6;
                        int i45 = i40 - i44;
                        i9 = virtualChildCount;
                        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin, ((LinearLayout.LayoutParams) layoutParams3).height);
                        if (((LinearLayout.LayoutParams) layoutParams3).width == 0) {
                            i12 = 1073741824;
                            if (mode == 1073741824) {
                            }
                        } else {
                            i12 = 1073741824;
                        }
                        i44 = virtualChildAt3.getMeasuredWidth() + i44;
                    } else {
                        i8 = i40;
                        i9 = virtualChildCount;
                    }
                    int i46 = this.mTotalLength;
                    if (z7) {
                        this.mTotalLength = i46 + virtualChildAt3.getMeasuredWidth() + ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f2;
                    } else {
                        f = f2;
                        this.mTotalLength = Math.max(i46, virtualChildAt3.getMeasuredWidth() + i46 + ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z13 = mode2 != 1073741824 && ((LinearLayout.LayoutParams) layoutParams3).height == -1;
                    int i47 = ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin;
                    int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i47;
                    i42 = Math.max(i42, measuredHeight2);
                    if (!z13) {
                        i47 = measuredHeight2;
                    }
                    int max3 = Math.max(i41, i47);
                    if (z9) {
                        i10 = -1;
                        if (((LinearLayout.LayoutParams) layoutParams3).height == -1) {
                            z = true;
                            if (z12 && (baseline = virtualChildAt3.getBaseline()) != i10) {
                                i11 = ((LinearLayout.LayoutParams) layoutParams3).gravity;
                                if (i11 < 0) {
                                    i11 = this.mGravity;
                                }
                                int i48 = (((i11 & 112) >> 4) & (-2)) >> 1;
                                iArr2[i48] = Math.max(iArr2[i48], baseline);
                                iArr[i48] = Math.max(iArr[i48], measuredHeight2 - baseline);
                            }
                            z9 = z;
                            i41 = max3;
                            f2 = f;
                        }
                    } else {
                        i10 = -1;
                    }
                    z = false;
                    if (z12) {
                        i11 = ((LinearLayout.LayoutParams) layoutParams3).gravity;
                        if (i11 < 0) {
                        }
                        int i482 = (((i11 & 112) >> 4) & (-2)) >> 1;
                        iArr2[i482] = Math.max(iArr2[i482], baseline);
                        iArr[i482] = Math.max(iArr[i482], measuredHeight2 - baseline);
                    }
                    z9 = z;
                    i41 = max3;
                    f2 = f;
                }
                i43++;
                i40 = i8;
                virtualChildCount = i9;
            }
            i5 = i2;
            i6 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            int i49 = iArr2[1];
            i39 = (i49 == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) ? i42 : Math.max(i42, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(i49, iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            i7 = i41;
        } else {
            i7 = Math.max(i33, i34);
            if (z11 && mode != 1073741824) {
                for (int i50 = 0; i50 < virtualChildCount; i50++) {
                    View virtualChildAt4 = getVirtualChildAt(i50);
                    if (virtualChildAt4 != null && virtualChildAt4.getVisibility() != 8 && ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt4.getLayoutParams())).weight > 0.0f) {
                        virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(i20, 1073741824), View.MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i5 = i2;
            i6 = virtualChildCount;
            i4 = i3;
        }
        if (z9 || mode2 == 1073741824) {
            i7 = i39;
        }
        setMeasuredDimension(resolveSizeAndState | ((-16777216) & i4), View.resolveSizeAndState(Math.max(i7 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i5, i4 << 16));
        if (z10) {
            forceUniformHeight(i6, i);
        }
    }

    int measureNullChild(int i) {
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:149:0x02d0, code lost:
    
        if (r15 > 0) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x02d3, code lost:
    
        r15 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x02d4, code lost:
    
        r13.measure(r0, android.view.View.MeasureSpec.makeMeasureSpec(r15, r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x02e7, code lost:
    
        r1 = android.view.View.combineMeasuredStates(r1, r13.getMeasuredState() & (-256));
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0312, code lost:
    
        if (((android.widget.LinearLayout.LayoutParams) r14).width == (-1)) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x02e4, code lost:
    
        if (r15 < 0) goto L132;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int max;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        View view;
        int max2;
        boolean z;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i19 = this.mBaselineAlignedChildIndex;
        boolean z2 = this.mUseLargestChild;
        float f = 0.0f;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        boolean z3 = false;
        boolean z4 = true;
        boolean z5 = false;
        while (true) {
            int i26 = 8;
            int i27 = i23;
            if (i25 >= virtualChildCount) {
                int i28 = i20;
                int i29 = i22;
                int i30 = i24;
                int i31 = mode2;
                int i32 = i21;
                int i33 = virtualChildCount;
                if (this.mTotalLength > 0) {
                    i3 = i33;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i33;
                }
                if (z2 && (i31 == Integer.MIN_VALUE || i31 == 0)) {
                    this.mTotalLength = 0;
                    int i34 = 0;
                    while (i34 < i3) {
                        View virtualChildAt = getVirtualChildAt(i34);
                        if (virtualChildAt == null) {
                            max = this.mTotalLength + measureNullChild(i34);
                        } else if (virtualChildAt.getVisibility() == i26) {
                            i34 += getChildrenSkipCount(virtualChildAt, i34);
                            i34++;
                            i26 = 8;
                        } else {
                            LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                            int i35 = this.mTotalLength;
                            max = Math.max(i35, i35 + i29 + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt));
                        }
                        this.mTotalLength = max;
                        i34++;
                        i26 = 8;
                    }
                }
                int paddingTop = this.mTotalLength + getPaddingTop() + getPaddingBottom();
                this.mTotalLength = paddingTop;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i2, 0);
                int i36 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z3 || (i36 != 0 && f > 0.0f)) {
                    float f2 = this.mWeightSum;
                    if (f2 > 0.0f) {
                        f = f2;
                    }
                    this.mTotalLength = 0;
                    int i37 = i36;
                    int i38 = i30;
                    i4 = i28;
                    int i39 = 0;
                    while (i39 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i39);
                        if (virtualChildAt2.getVisibility() == 8) {
                            i7 = i37;
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            float f3 = ((LinearLayout.LayoutParams) layoutParams2).weight;
                            if (f3 > 0.0f) {
                                int i40 = (int) ((i37 * f3) / f);
                                float f4 = f - f3;
                                i7 = i37 - i40;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + ((LinearLayout.LayoutParams) layoutParams2).leftMargin + ((LinearLayout.LayoutParams) layoutParams2).rightMargin, ((LinearLayout.LayoutParams) layoutParams2).width);
                                if (((LinearLayout.LayoutParams) layoutParams2).height == 0) {
                                    i10 = 1073741824;
                                    if (i31 == 1073741824) {
                                    }
                                } else {
                                    i10 = 1073741824;
                                }
                                i40 = virtualChildAt2.getMeasuredHeight() + i40;
                            } else {
                                i7 = i37;
                            }
                            int i41 = ((LinearLayout.LayoutParams) layoutParams2).leftMargin + ((LinearLayout.LayoutParams) layoutParams2).rightMargin;
                            int measuredWidth = virtualChildAt2.getMeasuredWidth() + i41;
                            i32 = Math.max(i32, measuredWidth);
                            float f5 = f;
                            if (mode != 1073741824) {
                                i8 = i4;
                                i9 = -1;
                            } else {
                                i8 = i4;
                                i9 = -1;
                            }
                            i41 = measuredWidth;
                            int max3 = Math.max(i38, i41);
                            boolean z6 = z4 && ((LinearLayout.LayoutParams) layoutParams2).width == i9;
                            int i42 = this.mTotalLength;
                            this.mTotalLength = Math.max(i42, virtualChildAt2.getMeasuredHeight() + i42 + ((LinearLayout.LayoutParams) layoutParams2).topMargin + ((LinearLayout.LayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                            z4 = z6;
                            i4 = i8;
                            i38 = max3;
                            f = f5;
                        }
                        i39++;
                        i37 = i7;
                    }
                    i5 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i38;
                } else {
                    i6 = Math.max(i30, i27);
                    if (z2 && i31 != 1073741824) {
                        for (int i43 = 0; i43 < i3; i43++) {
                            View virtualChildAt3 = getVirtualChildAt(i43);
                            if (virtualChildAt3 != null && virtualChildAt3.getVisibility() != 8 && ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt3.getLayoutParams())).weight > 0.0f) {
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i29, 1073741824));
                            }
                        }
                    }
                    i5 = i;
                    i4 = i28;
                }
                if (z4 || mode == 1073741824) {
                    i6 = i32;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i5, i4), resolveSizeAndState);
                if (z5) {
                    forceUniformWidth(i3, i2);
                    return;
                }
                return;
            }
            View virtualChildAt4 = getVirtualChildAt(i25);
            if (virtualChildAt4 == null) {
                this.mTotalLength += measureNullChild(i25);
                i14 = mode2;
                i23 = i27;
                i16 = virtualChildCount;
            } else {
                int i44 = i20;
                if (virtualChildAt4.getVisibility() == 8) {
                    i25 += getChildrenSkipCount(virtualChildAt4, i25);
                    i23 = i27;
                    i20 = i44;
                    i16 = virtualChildCount;
                    i14 = mode2;
                } else {
                    if (hasDividerBeforeChildAt(i25)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt4.getLayoutParams();
                    float f6 = ((LinearLayout.LayoutParams) layoutParams3).weight;
                    float f7 = f + f6;
                    if (mode2 == 1073741824 && ((LinearLayout.LayoutParams) layoutParams3).height == 0 && f6 > 0.0f) {
                        int i45 = this.mTotalLength;
                        this.mTotalLength = Math.max(i45, ((LinearLayout.LayoutParams) layoutParams3).topMargin + i45 + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin);
                        max2 = i22;
                        view = virtualChildAt4;
                        i17 = i24;
                        i12 = i44;
                        i13 = i21;
                        z3 = true;
                        i14 = mode2;
                        i15 = i27;
                        i16 = virtualChildCount;
                        i18 = i25;
                    } else {
                        int i46 = i21;
                        if (((LinearLayout.LayoutParams) layoutParams3).height != 0 || f6 <= 0.0f) {
                            i11 = Integer.MIN_VALUE;
                        } else {
                            ((LinearLayout.LayoutParams) layoutParams3).height = -2;
                            i11 = 0;
                        }
                        i12 = i44;
                        int i47 = i11;
                        i13 = i46;
                        int i48 = i22;
                        i14 = mode2;
                        i15 = i27;
                        i16 = virtualChildCount;
                        i17 = i24;
                        i18 = i25;
                        measureChildBeforeLayout(virtualChildAt4, i25, i, 0, i2, f7 == 0.0f ? this.mTotalLength : 0);
                        if (i47 != Integer.MIN_VALUE) {
                            ((LinearLayout.LayoutParams) layoutParams3).height = i47;
                        }
                        int measuredHeight = virtualChildAt4.getMeasuredHeight();
                        int i49 = this.mTotalLength;
                        view = virtualChildAt4;
                        this.mTotalLength = Math.max(i49, i49 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(view));
                        max2 = z2 ? Math.max(measuredHeight, i48) : i48;
                    }
                    if (i19 >= 0 && i19 == i18 + 1) {
                        this.mBaselineChildTop = this.mTotalLength;
                    }
                    if (i18 < i19 && ((LinearLayout.LayoutParams) layoutParams3).weight > 0.0f) {
                        throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                    }
                    if (mode == 1073741824 || ((LinearLayout.LayoutParams) layoutParams3).width != -1) {
                        z = false;
                    } else {
                        z = true;
                        z5 = true;
                    }
                    int i50 = ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin;
                    int measuredWidth2 = view.getMeasuredWidth() + i50;
                    int max4 = Math.max(i13, measuredWidth2);
                    int combineMeasuredStates = View.combineMeasuredStates(i12, view.getMeasuredState());
                    z4 = z4 && ((LinearLayout.LayoutParams) layoutParams3).width == -1;
                    if (((LinearLayout.LayoutParams) layoutParams3).weight > 0.0f) {
                        if (!z) {
                            i50 = measuredWidth2;
                        }
                        i23 = Math.max(i15, i50);
                    } else {
                        if (!z) {
                            i50 = measuredWidth2;
                        }
                        i17 = Math.max(i17, i50);
                        i23 = i15;
                    }
                    int childrenSkipCount = getChildrenSkipCount(view, i18) + i18;
                    i22 = max2;
                    i21 = max4;
                    f = f7;
                    i24 = i17;
                    i25 = childrenSkipCount;
                    i20 = combineMeasuredStates;
                }
            }
            i25++;
            virtualChildCount = i16;
            mode2 = i14;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i >= 0 && i < getChildCount()) {
            this.mBaselineAlignedChildIndex = i;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= 8388611;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & 8388615;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | ((-8388616) & i3);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
