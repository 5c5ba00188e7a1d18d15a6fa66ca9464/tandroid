package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes3.dex */
public class NumberTextView extends View {
    private boolean addNumber;
    private ObjectAnimator animator;
    private boolean center;
    private int currentNumber;
    private ArrayList letters;
    private ArrayList oldLetters;
    private float oldTextWidth;
    private float progress;
    private TextPaint textPaint;
    private float textWidth;

    public interface OnTextWidthProgressChangedListener {
    }

    public NumberTextView(Context context) {
        super(context);
        this.letters = new ArrayList();
        this.oldLetters = new ArrayList();
        this.textPaint = new TextPaint(1);
        this.progress = 0.0f;
        this.currentNumber = 1;
    }

    public float getOldTextWidth() {
        return this.oldTextWidth;
    }

    public float getProgress() {
        return this.progress;
    }

    public float getTextWidth() {
        return this.textWidth;
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f6, code lost:
    
        if (r7 != null) goto L39;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        TextPaint textPaint;
        float f3;
        if (this.letters.isEmpty()) {
            return;
        }
        float height = ((StaticLayout) this.letters.get(0)).getHeight();
        float dp = this.addNumber ? AndroidUtilities.dp(4.0f) : height;
        if (this.center) {
            f = (getMeasuredWidth() - this.textWidth) / 2.0f;
            f2 = ((getMeasuredWidth() - this.oldTextWidth) / 2.0f) - f;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        canvas.save();
        canvas.translate(getPaddingLeft() + f, (getMeasuredHeight() - height) / 2.0f);
        int max = Math.max(this.letters.size(), this.oldLetters.size());
        int i = 0;
        while (i < max) {
            canvas.save();
            StaticLayout staticLayout = i < this.oldLetters.size() ? (StaticLayout) this.oldLetters.get(i) : null;
            StaticLayout staticLayout2 = i < this.letters.size() ? (StaticLayout) this.letters.get(i) : null;
            float f4 = this.progress;
            if (f4 > 0.0f) {
                if (staticLayout != null) {
                    this.textPaint.setAlpha((int) (f4 * 255.0f));
                    canvas.save();
                    canvas.translate(f2, (this.progress - 1.0f) * dp);
                    staticLayout.draw(canvas);
                    canvas.restore();
                    if (staticLayout2 != null) {
                        textPaint = this.textPaint;
                        f3 = 1.0f - this.progress;
                        textPaint.setAlpha((int) (f3 * 255.0f));
                        canvas.translate(0.0f, this.progress * dp);
                    }
                }
                this.textPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
            } else if (f4 < 0.0f) {
                if (staticLayout != null) {
                    this.textPaint.setAlpha((int) ((-f4) * 255.0f));
                    canvas.save();
                    canvas.translate(f2, (this.progress + 1.0f) * dp);
                    staticLayout.draw(canvas);
                    canvas.restore();
                }
                if (staticLayout2 != null) {
                    if (i == max - 1 || staticLayout != null) {
                        textPaint = this.textPaint;
                        f3 = this.progress + 1.0f;
                        textPaint.setAlpha((int) (f3 * 255.0f));
                        canvas.translate(0.0f, this.progress * dp);
                    }
                    this.textPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
                }
            }
            if (staticLayout2 != null) {
                staticLayout2.draw(canvas);
            }
            canvas.restore();
            canvas.translate(staticLayout2 != null ? staticLayout2.getLineWidth(0) : staticLayout.getLineWidth(0) + AndroidUtilities.dp(1.0f), 0.0f);
            if (staticLayout2 != null && staticLayout != null) {
                f2 += staticLayout.getLineWidth(0) - staticLayout2.getLineWidth(0);
            }
            i++;
        }
        canvas.restore();
    }

    public void setAddNumber() {
        this.addNumber = true;
    }

    public void setCenterAlign(boolean z) {
        this.center = z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x004c, code lost:
    
        if (r22 < r21.currentNumber) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0073, code lost:
    
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0071, code lost:
    
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x006f, code lost:
    
        if (r22 > r21.currentNumber) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setNumber(int i, boolean z) {
        String format;
        String format2;
        boolean z2;
        if (this.currentNumber == i && z) {
            return;
        }
        ObjectAnimator objectAnimator = this.animator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.animator = null;
        }
        this.oldLetters.clear();
        this.oldLetters.addAll(this.letters);
        this.letters.clear();
        if (this.addNumber) {
            Locale locale = Locale.US;
            format = String.format(locale, "#%d", Integer.valueOf(this.currentNumber));
            format2 = String.format(locale, "#%d", Integer.valueOf(i));
        } else {
            Locale locale2 = Locale.US;
            format = String.format(locale2, "%d", Integer.valueOf(this.currentNumber));
            format2 = String.format(locale2, "%d", Integer.valueOf(i));
        }
        this.textWidth = this.textPaint.measureText(format2);
        float measureText = this.textPaint.measureText(format);
        this.oldTextWidth = measureText;
        boolean z3 = this.center && this.textWidth != measureText;
        this.currentNumber = i;
        this.progress = 0.0f;
        int i2 = 0;
        while (i2 < format2.length()) {
            int i3 = i2 + 1;
            String substring = format2.substring(i2, i3);
            String substring2 = (this.oldLetters.isEmpty() || i2 >= format.length()) ? null : format.substring(i2, i3);
            if (z3 || substring2 == null || !substring2.equals(substring)) {
                if (z3 && substring2 == null) {
                    this.oldLetters.add(new StaticLayout("", this.textPaint, 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
                }
                this.letters.add(new StaticLayout(substring, this.textPaint, (int) Math.ceil(r13.measureText(substring)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
            } else {
                this.letters.add((StaticLayout) this.oldLetters.get(i2));
                this.oldLetters.set(i2, null);
            }
            i2 = i3;
        }
        if (z && !this.oldLetters.isEmpty()) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", z2 ? -1.0f : 1.0f, 0.0f);
            this.animator = ofFloat;
            ofFloat.setDuration(this.addNumber ? 180L : 150L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.NumberTextView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NumberTextView.this.animator = null;
                    NumberTextView.this.oldLetters.clear();
                }
            });
            this.animator.start();
        }
        invalidate();
    }

    public void setOnTextWidthProgressChangedListener(OnTextWidthProgressChangedListener onTextWidthProgressChangedListener) {
    }

    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        invalidate();
    }

    public void setTextColor(int i) {
        this.textPaint.setColor(i);
        invalidate();
    }

    public void setTextSize(int i) {
        this.textPaint.setTextSize(AndroidUtilities.dp(i));
        this.oldLetters.clear();
        this.letters.clear();
        setNumber(this.currentNumber, false);
    }

    public void setTypeface(Typeface typeface) {
        this.textPaint.setTypeface(typeface);
        this.oldLetters.clear();
        this.letters.clear();
        setNumber(this.currentNumber, false);
    }
}
