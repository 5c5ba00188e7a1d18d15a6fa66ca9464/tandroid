package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import j$.wrappers.$r8$wrapper$java$util$stream$IntStream$-V-WRP;
import j$.wrappers.$r8$wrapper$java$util$stream$IntStream$-WRP;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
/* loaded from: classes4.dex */
public class AnimatedTextView extends View {
    public boolean adaptWidth;
    private final AnimatedTextDrawable drawable;
    private boolean first;
    private int lastMaxWidth;
    private int maxWidth;
    private boolean toSetMoveDown;
    private CharSequence toSetText;

    /* loaded from: classes4.dex */
    public static class AnimatedTextDrawable extends Drawable {
        private boolean allowCancel;
        private int alpha;
        private long animateDelay;
        private long animateDuration;
        private TimeInterpolator animateInterpolator;
        private ValueAnimator animator;
        private final android.graphics.Rect bounds;
        private float currentHeight;
        private Part[] currentParts;
        private CharSequence currentText;
        private float currentWidth;
        private boolean ellipsizeByGradient;
        private LinearGradient ellipsizeGradient;
        private Matrix ellipsizeGradientMatrix;
        private Paint ellipsizePaint;
        private int gravity;
        public boolean ignoreRTL;
        private boolean isRTL;
        private float moveAmplitude;
        private boolean moveDown;
        private float oldHeight;
        private Part[] oldParts;
        private CharSequence oldText;
        private float oldWidth;
        private Runnable onAnimationFinishListener;
        private int overrideFullWidth;
        private float rightPadding;
        private int shadowColor;
        private float shadowDx;
        private float shadowDy;
        private float shadowRadius;
        private boolean shadowed;
        private boolean splitByWords;
        private float t;
        private final TextPaint textPaint;
        private CharSequence toSetText;
        private boolean toSetTextMoveDown;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public interface RegionCallback {
            void run(CharSequence charSequence, int i, int i2);
        }

        @Override // android.graphics.drawable.Drawable
        @Deprecated
        public int getOpacity() {
            return -2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Part {
            StaticLayout layout;
            float left;
            float offset;
            int toOppositeIndex;
            float width;

            public Part(StaticLayout staticLayout, float f, int i) {
                this.layout = staticLayout;
                this.offset = f;
                this.toOppositeIndex = i;
                float f2 = 0.0f;
                this.left = (staticLayout == null || staticLayout.getLineCount() <= 0) ? 0.0f : staticLayout.getLineLeft(0);
                if (staticLayout != null && staticLayout.getLineCount() > 0) {
                    f2 = staticLayout.getLineWidth(0);
                }
                this.width = f2;
            }
        }

        public void setOverrideFullWidth(int i) {
            this.overrideFullWidth = i;
        }

        public AnimatedTextDrawable() {
            this(false, false, false);
        }

        public AnimatedTextDrawable(boolean z, boolean z2, boolean z3) {
            this.textPaint = new TextPaint(1);
            this.gravity = 0;
            this.isRTL = false;
            this.t = 0.0f;
            this.moveDown = true;
            this.animateDelay = 0L;
            this.animateDuration = 450L;
            this.animateInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.moveAmplitude = 1.0f;
            this.alpha = 255;
            this.bounds = new android.graphics.Rect();
            this.shadowed = false;
            this.splitByWords = z;
        }

        public void setAllowCancel(boolean z) {
            this.allowCancel = z;
        }

        public void setEllipsizeByGradient(boolean z) {
            this.ellipsizeByGradient = z;
            invalidateSelf();
        }

        public void setOnAnimationFinishListener(Runnable runnable) {
            this.onAnimationFinishListener = runnable;
        }

        private void applyAlphaInternal(float f) {
            this.textPaint.setAlpha((int) (this.alpha * f));
            if (this.shadowed) {
                this.textPaint.setShadowLayer(this.shadowRadius, this.shadowDx, this.shadowDy, Theme.multAlpha(this.shadowColor, f));
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:75:0x0162, code lost:
            if (r16.ignoreRTL == false) goto L69;
         */
        /* JADX WARN: Removed duplicated region for block: B:106:0x01dc  */
        /* JADX WARN: Removed duplicated region for block: B:124:? A[RETURN, SYNTHETIC] */
        @Override // android.graphics.drawable.Drawable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas) {
            float f;
            float f2;
            float f3;
            android.graphics.Rect rect;
            float f4;
            float f5;
            float f6;
            float f7;
            if (this.ellipsizeByGradient) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(this.bounds);
                rectF.right -= this.rightPadding;
                canvas.saveLayerAlpha(rectF, 255, 31);
            }
            canvas.save();
            android.graphics.Rect rect2 = this.bounds;
            canvas.translate(rect2.left, rect2.top);
            int width = this.bounds.width();
            int height = this.bounds.height();
            if (this.currentParts != null && this.oldParts != null) {
                float f8 = this.t;
                if (f8 != 1.0f) {
                    float lerp = AndroidUtilities.lerp(this.oldWidth, this.currentWidth, f8);
                    canvas.translate(0.0f, (height - AndroidUtilities.lerp(this.oldHeight, this.currentHeight, this.t)) / 2.0f);
                    int i = 0;
                    while (true) {
                        Part[] partArr = this.currentParts;
                        if (i >= partArr.length) {
                            break;
                        }
                        Part part = partArr[i];
                        int i2 = part.toOppositeIndex;
                        float f9 = (part.offset / this.currentWidth) * lerp;
                        boolean z = this.isRTL;
                        if (z && !this.ignoreRTL) {
                            f9 = lerp - (f9 + part.width);
                        }
                        if (i2 >= 0) {
                            Part part2 = this.oldParts[i2];
                            float f10 = (part2.offset / this.oldWidth) * lerp;
                            if (z && !this.ignoreRTL) {
                                f10 = lerp - (f10 + part2.width);
                            }
                            f5 = AndroidUtilities.lerp(f10 - part2.left, f9 - part.left, this.t);
                            applyAlphaInternal(1.0f);
                            f6 = 0.0f;
                        } else {
                            f5 = f9 - part.left;
                            float f11 = (-this.textPaint.getTextSize()) * this.moveAmplitude;
                            float f12 = this.t;
                            f6 = f11 * (1.0f - f12) * (this.moveDown ? 1.0f : -1.0f);
                            applyAlphaInternal(f12);
                        }
                        canvas.save();
                        float f13 = i2 >= 0 ? lerp : this.currentWidth;
                        int i3 = this.gravity;
                        if ((i3 | (-4)) != -1) {
                            if ((i3 | (-6)) != -1) {
                                if ((i3 | (-2)) == -1) {
                                    f7 = (width - f13) / 2.0f;
                                    f5 += f7;
                                } else if (this.isRTL) {
                                    if (this.ignoreRTL) {
                                    }
                                }
                            }
                            f7 = width - f13;
                            f5 += f7;
                        }
                        canvas.translate(f5, f6);
                        part.layout.draw(canvas);
                        canvas.restore();
                        i++;
                    }
                    int i4 = 0;
                    while (true) {
                        Part[] partArr2 = this.oldParts;
                        if (i4 >= partArr2.length) {
                            break;
                        }
                        Part part3 = partArr2[i4];
                        if (part3.toOppositeIndex < 0) {
                            float f14 = (part3.offset / this.oldWidth) * lerp;
                            float textSize = this.textPaint.getTextSize() * this.moveAmplitude;
                            float f15 = this.t;
                            float f16 = textSize * f15 * (this.moveDown ? 1.0f : -1.0f);
                            applyAlphaInternal(1.0f - f15);
                            canvas.save();
                            boolean z2 = this.isRTL;
                            if (z2 && !this.ignoreRTL) {
                                f14 = lerp - (f14 + part3.width);
                            }
                            float f17 = f14 - part3.left;
                            int i5 = this.gravity;
                            if ((i5 | (-4)) != -1) {
                                if ((i5 | (-6)) != -1) {
                                    if ((i5 | (-2)) == -1) {
                                        f4 = (width - lerp) / 2.0f;
                                        f17 += f4;
                                    } else if (z2) {
                                    }
                                }
                                f4 = width - lerp;
                                f17 += f4;
                            }
                            canvas.translate(f17, f16);
                            part3.layout.draw(canvas);
                            canvas.restore();
                        }
                        i4++;
                    }
                    canvas.restore();
                    if (this.ellipsizeByGradient) {
                        return;
                    }
                    float dp = AndroidUtilities.dp(16.0f);
                    if (this.ellipsizeGradient == null) {
                        this.ellipsizeGradient = new LinearGradient(0.0f, 0.0f, dp, 0.0f, new int[]{16711680, -65536}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                        this.ellipsizeGradientMatrix = new Matrix();
                        Paint paint = new Paint(1);
                        this.ellipsizePaint = paint;
                        paint.setShader(this.ellipsizeGradient);
                        this.ellipsizePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    }
                    this.ellipsizeGradientMatrix.reset();
                    this.ellipsizeGradientMatrix.postTranslate((this.bounds.right - this.rightPadding) - dp, 0.0f);
                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeGradientMatrix);
                    canvas.save();
                    int i6 = this.bounds.right;
                    float f18 = this.rightPadding;
                    canvas.drawRect((i6 - f18) - dp, rect.top, i6 - f18, rect.bottom, this.ellipsizePaint);
                    canvas.restore();
                    canvas.restore();
                    return;
                }
            }
            canvas.translate(0.0f, (height - this.currentHeight) / 2.0f);
            if (this.currentParts != null) {
                applyAlphaInternal(1.0f);
                for (int i7 = 0; i7 < this.currentParts.length; i7++) {
                    canvas.save();
                    Part part4 = this.currentParts[i7];
                    float f19 = part4.offset;
                    boolean z3 = this.isRTL;
                    if (z3 && !this.ignoreRTL) {
                        f19 = this.currentWidth - (f19 + part4.width);
                    }
                    float f20 = f19 - part4.left;
                    int i8 = this.gravity;
                    if ((i8 | (-4)) != -1) {
                        if ((i8 | (-6)) == -1) {
                            f = width;
                            f2 = this.currentWidth;
                        } else if ((i8 | (-2)) == -1) {
                            f3 = (width - this.currentWidth) / 2.0f;
                            f20 += f3;
                        } else if (z3 && !this.ignoreRTL) {
                            f = width;
                            f2 = this.currentWidth;
                        }
                        f3 = f - f2;
                        f20 += f3;
                    }
                    canvas.translate(f20, 0.0f);
                    part4.layout.draw(canvas);
                    canvas.restore();
                }
            }
            canvas.restore();
            if (this.ellipsizeByGradient) {
            }
        }

        public void setRightPadding(float f) {
            this.rightPadding = f;
            invalidateSelf();
        }

        public void cancelAnimation() {
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }

        public boolean isAnimating() {
            ValueAnimator valueAnimator = this.animator;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        public void setText(CharSequence charSequence) {
            setText(charSequence, true);
        }

        public void setText(CharSequence charSequence, boolean z) {
            setText(charSequence, z, true);
        }

        public void setText(CharSequence charSequence, boolean z, boolean z2) {
            z = (this.currentText == null || charSequence == null) ? false : false;
            if (charSequence == null) {
                charSequence = "";
            }
            final int i = this.overrideFullWidth;
            if (i <= 0) {
                i = this.bounds.width();
            }
            if (z) {
                if (this.allowCancel) {
                    ValueAnimator valueAnimator = this.animator;
                    if (valueAnimator != null) {
                        valueAnimator.cancel();
                        this.animator = null;
                    }
                } else if (isAnimating()) {
                    this.toSetText = charSequence;
                    this.toSetTextMoveDown = z2;
                    return;
                }
                if (charSequence.equals(this.currentText)) {
                    return;
                }
                this.oldText = this.currentText;
                this.currentText = charSequence;
                final ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                this.currentHeight = 0.0f;
                this.currentWidth = 0.0f;
                this.oldHeight = 0.0f;
                this.oldWidth = 0.0f;
                this.isRTL = AndroidUtilities.isRTL(this.currentText);
                betterDiff(this.splitByWords ? new WordSequence(this.oldText) : this.oldText, this.splitByWords ? new WordSequence(this.currentText) : this.currentText, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda8
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$0(i, arrayList2, arrayList, charSequence2, i2, i3);
                    }
                }, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda7
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$1(i, arrayList, charSequence2, i2, i3);
                    }
                }, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda6
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$2(i, arrayList2, charSequence2, i2, i3);
                    }
                });
                Part[] partArr = this.currentParts;
                if (partArr == null || partArr.length != arrayList.size()) {
                    this.currentParts = new Part[arrayList.size()];
                }
                arrayList.toArray(this.currentParts);
                Part[] partArr2 = this.oldParts;
                if (partArr2 == null || partArr2.length != arrayList2.size()) {
                    this.oldParts = new Part[arrayList2.size()];
                }
                arrayList2.toArray(this.oldParts);
                ValueAnimator valueAnimator2 = this.animator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.moveDown = z2;
                this.t = 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$3(valueAnimator3);
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        AnimatedTextDrawable.this.oldParts = null;
                        AnimatedTextDrawable.this.oldText = null;
                        AnimatedTextDrawable.this.oldWidth = 0.0f;
                        AnimatedTextDrawable.this.t = 0.0f;
                        AnimatedTextDrawable.this.invalidateSelf();
                        AnimatedTextDrawable.this.animator = null;
                        if (AnimatedTextDrawable.this.toSetText == null) {
                            if (AnimatedTextDrawable.this.onAnimationFinishListener != null) {
                                AnimatedTextDrawable.this.onAnimationFinishListener.run();
                                return;
                            }
                            return;
                        }
                        AnimatedTextDrawable animatedTextDrawable = AnimatedTextDrawable.this;
                        animatedTextDrawable.setText(animatedTextDrawable.toSetText, true, AnimatedTextDrawable.this.toSetTextMoveDown);
                        AnimatedTextDrawable.this.toSetText = null;
                        AnimatedTextDrawable.this.toSetTextMoveDown = false;
                    }
                });
                this.animator.setStartDelay(this.animateDelay);
                this.animator.setDuration(this.animateDuration);
                this.animator.setInterpolator(this.animateInterpolator);
                this.animator.start();
                return;
            }
            ValueAnimator valueAnimator3 = this.animator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
            this.animator = null;
            this.toSetText = null;
            this.toSetTextMoveDown = false;
            this.t = 0.0f;
            if (!charSequence.equals(this.currentText)) {
                this.currentParts = r12;
                this.currentText = charSequence;
                Part[] partArr3 = {new Part(makeLayout(charSequence, i), 0.0f, -1)};
                Part[] partArr4 = this.currentParts;
                this.currentWidth = partArr4[0].width;
                this.currentHeight = partArr4[0].layout.getHeight();
                this.isRTL = AndroidUtilities.isRTL(this.currentText);
            }
            this.oldParts = null;
            this.oldText = null;
            this.oldWidth = 0.0f;
            this.oldHeight = 0.0f;
            invalidateSelf();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$0(int i, ArrayList arrayList, ArrayList arrayList2, CharSequence charSequence, int i2, int i3) {
            StaticLayout makeLayout = makeLayout(charSequence, i - ((int) Math.ceil(Math.min(this.currentWidth, this.oldWidth))));
            Part part = new Part(makeLayout, this.currentWidth, arrayList.size());
            Part part2 = new Part(makeLayout, this.oldWidth, arrayList.size());
            arrayList2.add(part);
            arrayList.add(part2);
            float f = part.width;
            this.currentWidth += f;
            this.oldWidth += f;
            this.currentHeight = Math.max(this.currentHeight, makeLayout.getHeight());
            this.oldHeight = Math.max(this.oldHeight, makeLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$1(int i, ArrayList arrayList, CharSequence charSequence, int i2, int i3) {
            StaticLayout makeLayout;
            Part part = new Part(makeLayout(charSequence, i - ((int) Math.ceil(this.currentWidth))), this.currentWidth, -1);
            arrayList.add(part);
            this.currentWidth += part.width;
            this.currentHeight = Math.max(this.currentHeight, makeLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$2(int i, ArrayList arrayList, CharSequence charSequence, int i2, int i3) {
            StaticLayout makeLayout;
            Part part = new Part(makeLayout(charSequence, i - ((int) Math.ceil(this.oldWidth))), this.oldWidth, -1);
            arrayList.add(part);
            this.oldWidth += part.width;
            this.oldHeight = Math.max(this.oldHeight, makeLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$3(ValueAnimator valueAnimator) {
            this.t = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidateSelf();
        }

        public CharSequence getText() {
            return this.currentText;
        }

        public float getWidth() {
            return Math.max(this.currentWidth, this.oldWidth);
        }

        public float getCurrentWidth() {
            if (this.currentParts != null && this.oldParts != null) {
                return AndroidUtilities.lerp(this.oldWidth, this.currentWidth, this.t);
            }
            return this.currentWidth;
        }

        public float getHeight() {
            return this.currentHeight;
        }

        private StaticLayout makeLayout(CharSequence charSequence, int i) {
            if (i <= 0) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                i = Math.min(point.x, point.y);
            }
            int i2 = i;
            if (Build.VERSION.SDK_INT >= 23) {
                return StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), this.textPaint, i2).setMaxLines(1).setLineSpacing(0.0f, 1.0f).setAlignment(Layout.Alignment.ALIGN_NORMAL).setEllipsize(TextUtils.TruncateAt.END).setEllipsizedWidth(i2).build();
            }
            return new StaticLayout(charSequence, 0, charSequence.length(), this.textPaint, i2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class WordSequence implements CharSequence {
            private final int length;
            private final CharSequence[] words;

            @Override // java.lang.CharSequence
            public /* synthetic */ IntStream chars() {
                return $r8$wrapper$java$util$stream$IntStream$-WRP.convert(chars());
            }

            @Override // java.lang.CharSequence
            public /* synthetic */ IntStream codePoints() {
                return $r8$wrapper$java$util$stream$IntStream$-WRP.convert(codePoints());
            }

            public WordSequence(CharSequence charSequence) {
                if (charSequence == null) {
                    this.words = new CharSequence[0];
                    this.length = 0;
                    return;
                }
                this.length = charSequence.length();
                int i = 0;
                for (int i2 = 0; i2 < this.length; i2++) {
                    if (charSequence.charAt(i2) == ' ') {
                        i++;
                    }
                }
                this.words = new CharSequence[i + 1];
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                while (true) {
                    int i6 = this.length;
                    if (i3 > i6) {
                        return;
                    }
                    if (i3 == i6 || charSequence.charAt(i3) == ' ') {
                        int i7 = i4 + 1;
                        this.words[i4] = charSequence.subSequence(i5, (i3 < this.length ? 1 : 0) + i3);
                        i5 = i3 + 1;
                        i4 = i7;
                    }
                    i3++;
                }
            }

            public CharSequence wordAt(int i) {
                if (i >= 0) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i >= charSequenceArr.length) {
                        return null;
                    }
                    return charSequenceArr[i];
                }
                return null;
            }

            @Override // java.lang.CharSequence
            public int length() {
                return this.words.length;
            }

            @Override // java.lang.CharSequence
            public char charAt(int i) {
                int i2 = 0;
                while (true) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i2 >= charSequenceArr.length) {
                        return (char) 0;
                    }
                    if (i < charSequenceArr[i2].length()) {
                        return this.words[i2].charAt(i);
                    }
                    i -= this.words[i2].length();
                    i2++;
                }
            }

            @Override // java.lang.CharSequence
            public CharSequence subSequence(int i, int i2) {
                return TextUtils.concat((CharSequence[]) Arrays.copyOfRange(this.words, i, i2));
            }

            @Override // java.lang.CharSequence
            public String toString() {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (true) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i < charSequenceArr.length) {
                        sb.append(charSequenceArr[i]);
                        i++;
                    } else {
                        return sb.toString();
                    }
                }
            }

            public CharSequence toCharSequence() {
                return TextUtils.concat(this.words);
            }

            @Override // java.lang.CharSequence
            public j$.util.stream.IntStream chars() {
                if (Build.VERSION.SDK_INT >= 24) {
                    return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(toCharSequence().chars());
                }
                return null;
            }

            @Override // java.lang.CharSequence
            public j$.util.stream.IntStream codePoints() {
                if (Build.VERSION.SDK_INT >= 24) {
                    return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(toCharSequence().codePoints());
                }
                return null;
            }
        }

        public static boolean partEquals(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
            if (!(charSequence instanceof WordSequence) || !(charSequence2 instanceof WordSequence)) {
                if (charSequence == null && charSequence2 == null) {
                    return true;
                }
                return (charSequence == null || charSequence2 == null || charSequence.charAt(i) != charSequence2.charAt(i2)) ? false : true;
            }
            CharSequence wordAt = ((WordSequence) charSequence).wordAt(i);
            CharSequence wordAt2 = ((WordSequence) charSequence2).wordAt(i2);
            if (wordAt == null && wordAt2 == null) {
                return true;
            }
            return wordAt != null && wordAt.equals(wordAt2);
        }

        private void betterDiff(final CharSequence charSequence, final CharSequence charSequence2, final RegionCallback regionCallback, final RegionCallback regionCallback2, final RegionCallback regionCallback3) {
            int length = charSequence.length();
            int length2 = charSequence2.length();
            int[][] iArr = (int[][]) Array.newInstance(int.class, length + 1, length2 + 1);
            for (int i = 0; i <= length; i++) {
                for (int i2 = 0; i2 <= length2; i2++) {
                    if (i == 0 || i2 == 0) {
                        iArr[i][i2] = 0;
                    } else {
                        int i3 = i - 1;
                        int i4 = i2 - 1;
                        if (partEquals(charSequence, charSequence2, i3, i4)) {
                            iArr[i][i2] = iArr[i3][i4] + 1;
                        } else {
                            iArr[i][i2] = Math.max(iArr[i3][i2], iArr[i][i4]);
                        }
                    }
                }
            }
            ArrayList<Runnable> arrayList = new ArrayList();
            while (length > 0 && length2 > 0) {
                final int i5 = length - 1;
                final int i6 = length2 - 1;
                if (partEquals(charSequence, charSequence2, i5, i6)) {
                    while (length > 1 && length2 > 1 && partEquals(charSequence, charSequence2, length - 2, length2 - 2)) {
                        length--;
                        length2--;
                    }
                    final int i7 = length - 1;
                    arrayList.add(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            AnimatedTextView.AnimatedTextDrawable.lambda$betterDiff$4(AnimatedTextView.AnimatedTextDrawable.RegionCallback.this, charSequence, i7, i5);
                        }
                    });
                    length--;
                } else if (iArr[i5][length2] > iArr[length][i6]) {
                    while (length > 1 && iArr[length - 2][length2] > iArr[length - 1][i6]) {
                        length--;
                    }
                    final int i8 = length - 1;
                    arrayList.add(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            AnimatedTextView.AnimatedTextDrawable.lambda$betterDiff$5(AnimatedTextView.AnimatedTextDrawable.RegionCallback.this, charSequence, i8, i5);
                        }
                    });
                    length--;
                } else {
                    while (length2 > 1 && iArr[length][length2 - 2] > iArr[i5][length2 - 1]) {
                        length2--;
                    }
                    final int i9 = length2 - 1;
                    arrayList.add(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            AnimatedTextView.AnimatedTextDrawable.lambda$betterDiff$6(AnimatedTextView.AnimatedTextDrawable.RegionCallback.this, charSequence2, i9, i6);
                        }
                    });
                }
                length2--;
            }
            while (length > 0) {
                final int i10 = length - 1;
                while (length > 1 && iArr[length - 2][length2] >= iArr[length - 1][length2]) {
                    length--;
                }
                final int i11 = length - 1;
                arrayList.add(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        AnimatedTextView.AnimatedTextDrawable.lambda$betterDiff$7(AnimatedTextView.AnimatedTextDrawable.RegionCallback.this, charSequence, i11, i10);
                    }
                });
                length--;
            }
            while (length2 > 0) {
                final int i12 = length2 - 1;
                while (length2 > 1 && iArr[length][length2 - 2] >= iArr[length][length2 - 1]) {
                    length2--;
                }
                final int i13 = length2 - 1;
                arrayList.add(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        AnimatedTextView.AnimatedTextDrawable.lambda$betterDiff$8(AnimatedTextView.AnimatedTextDrawable.RegionCallback.this, charSequence2, i13, i12);
                    }
                });
                length2--;
            }
            Collections.reverse(arrayList);
            for (Runnable runnable : arrayList) {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$betterDiff$4(RegionCallback regionCallback, CharSequence charSequence, int i, int i2) {
            int i3 = i2 + 1;
            regionCallback.run(charSequence.subSequence(i, i3), i, i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$betterDiff$5(RegionCallback regionCallback, CharSequence charSequence, int i, int i2) {
            int i3 = i2 + 1;
            regionCallback.run(charSequence.subSequence(i, i3), i, i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$betterDiff$6(RegionCallback regionCallback, CharSequence charSequence, int i, int i2) {
            int i3 = i2 + 1;
            regionCallback.run(charSequence.subSequence(i, i3), i, i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$betterDiff$7(RegionCallback regionCallback, CharSequence charSequence, int i, int i2) {
            int i3 = i2 + 1;
            regionCallback.run(charSequence.subSequence(i, i3), i, i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$betterDiff$8(RegionCallback regionCallback, CharSequence charSequence, int i, int i2) {
            int i3 = i2 + 1;
            regionCallback.run(charSequence.subSequence(i, i3), i, i3);
        }

        public void setTextSize(float f) {
            this.textPaint.setTextSize(f);
        }

        public float getTextSize() {
            return this.textPaint.getTextSize();
        }

        public void setTextColor(int i) {
            this.textPaint.setColor(i);
            this.alpha = Color.alpha(i);
        }

        public void setShadowLayer(float f, float f2, float f3, int i) {
            this.shadowed = true;
            TextPaint textPaint = this.textPaint;
            this.shadowRadius = f;
            this.shadowDx = f2;
            this.shadowDy = f3;
            this.shadowColor = i;
            textPaint.setShadowLayer(f, f2, f3, i);
        }

        public int getTextColor() {
            return this.textPaint.getColor();
        }

        public void setTypeface(Typeface typeface) {
            this.textPaint.setTypeface(typeface);
        }

        public void setGravity(int i) {
            this.gravity = i;
        }

        public void setAnimationProperties(float f, long j, long j2, TimeInterpolator timeInterpolator) {
            this.moveAmplitude = f;
            this.animateDelay = j;
            this.animateDuration = j2;
            this.animateInterpolator = timeInterpolator;
        }

        public TextPaint getPaint() {
            return this.textPaint;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.alpha = i;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.textPaint.setColorFilter(colorFilter);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(android.graphics.Rect rect) {
            super.setBounds(rect);
            this.bounds.set(rect);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            this.bounds.set(i, i2, i3, i4);
        }

        @Override // android.graphics.drawable.Drawable
        public android.graphics.Rect getDirtyBounds() {
            return this.bounds;
        }
    }

    public AnimatedTextView(Context context) {
        this(context, false, false, false);
    }

    public AnimatedTextView(Context context, boolean z, boolean z2, boolean z3) {
        super(context);
        this.adaptWidth = true;
        this.first = true;
        AnimatedTextDrawable animatedTextDrawable = new AnimatedTextDrawable(z, z2, z3);
        this.drawable = animatedTextDrawable;
        animatedTextDrawable.setCallback(this);
        animatedTextDrawable.setOnAnimationFinishListener(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedTextView.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        CharSequence charSequence = this.toSetText;
        if (charSequence != null) {
            setText(charSequence, this.toSetMoveDown, true);
            this.toSetText = null;
            this.toSetMoveDown = false;
        }
    }

    public void setMaxWidth(int i) {
        this.maxWidth = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int i3 = this.maxWidth;
        if (i3 > 0) {
            size = Math.min(size, i3);
        }
        if (this.lastMaxWidth != size && getLayoutParams().width != 0) {
            this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), size - getPaddingRight(), size2 - getPaddingBottom());
            setText(this.drawable.getText(), false);
        }
        this.lastMaxWidth = size;
        if (this.adaptWidth && View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            size = getPaddingRight() + getPaddingLeft() + ((int) Math.ceil(this.drawable.getWidth()));
        }
        setMeasuredDimension(size, size2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        this.drawable.draw(canvas);
    }

    public void setText(CharSequence charSequence) {
        setText(charSequence, true, true);
    }

    public void setText(CharSequence charSequence, boolean z) {
        setText(charSequence, z, true);
    }

    public void cancelAnimation() {
        this.drawable.cancelAnimation();
    }

    public boolean isAnimating() {
        return this.drawable.isAnimating();
    }

    public void setIgnoreRTL(boolean z) {
        this.drawable.ignoreRTL = z;
    }

    public void setText(CharSequence charSequence, boolean z, boolean z2) {
        boolean z3 = !this.first && z;
        this.first = false;
        if (z3) {
            if (this.drawable.allowCancel) {
                if (this.drawable.animator != null) {
                    this.drawable.animator.cancel();
                    this.drawable.animator = null;
                }
            } else if (this.drawable.isAnimating()) {
                this.toSetText = charSequence;
                this.toSetMoveDown = z2;
                return;
            }
        }
        this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), this.lastMaxWidth - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        this.drawable.setText(charSequence, z3, z2);
        float width = (int) this.drawable.getWidth();
        if (width < this.drawable.getWidth() || !(z3 || width == this.drawable.getWidth())) {
            requestLayout();
        }
    }

    public int width() {
        return getPaddingLeft() + ((int) Math.ceil(this.drawable.getCurrentWidth())) + getPaddingRight();
    }

    public CharSequence getText() {
        return this.drawable.getText();
    }

    public int getTextHeight() {
        return getPaint().getFontMetricsInt().descent - getPaint().getFontMetricsInt().ascent;
    }

    public void setTextSize(float f) {
        this.drawable.setTextSize(f);
    }

    public void setTextColor(int i) {
        this.drawable.setTextColor(i);
        invalidate();
    }

    public int getTextColor() {
        return this.drawable.getTextColor();
    }

    public void setTypeface(Typeface typeface) {
        this.drawable.setTypeface(typeface);
    }

    public void setGravity(int i) {
        this.drawable.setGravity(i);
    }

    public void setAnimationProperties(float f, long j, long j2, TimeInterpolator timeInterpolator) {
        this.drawable.setAnimationProperties(f, j, j2, timeInterpolator);
    }

    public AnimatedTextDrawable getDrawable() {
        return this.drawable;
    }

    public TextPaint getPaint() {
        return this.drawable.getPaint();
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidate();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.TextView");
        accessibilityNodeInfo.setText(getText());
    }

    public void setEllipsizeByGradient(boolean z) {
        this.drawable.setEllipsizeByGradient(z);
    }

    public void setRightPadding(float f) {
        this.drawable.setRightPadding(f);
    }
}
