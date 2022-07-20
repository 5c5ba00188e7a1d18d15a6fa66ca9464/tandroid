package org.telegram.ui.Components.spoilers;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GestureDetectorCompat;
import java.util.List;
/* loaded from: classes3.dex */
public class SpoilersClickDetector {
    private GestureDetectorCompat gestureDetector;
    private boolean trackingTap;

    /* loaded from: classes3.dex */
    public interface OnSpoilerClickedListener {
        void onSpoilerClicked(SpoilerEffect spoilerEffect, float f, float f2);
    }

    public SpoilersClickDetector(View view, List<SpoilerEffect> list, OnSpoilerClickedListener onSpoilerClickedListener) {
        this(view, list, true, onSpoilerClickedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.spoilers.SpoilersClickDetector$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends GestureDetector.SimpleOnGestureListener {
        final /* synthetic */ OnSpoilerClickedListener val$clickedListener;
        final /* synthetic */ boolean val$offsetPadding;
        final /* synthetic */ List val$spoilers;
        final /* synthetic */ View val$v;

        AnonymousClass1(View view, boolean z, List list, OnSpoilerClickedListener onSpoilerClickedListener) {
            SpoilersClickDetector.this = r1;
            this.val$v = view;
            this.val$offsetPadding = z;
            this.val$spoilers = list;
            this.val$clickedListener = onSpoilerClickedListener;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            int x = (int) motionEvent.getX();
            int y = ((int) motionEvent.getY()) + this.val$v.getScrollY();
            if (this.val$offsetPadding) {
                x -= this.val$v.getPaddingLeft();
                y -= this.val$v.getPaddingTop();
            }
            for (SpoilerEffect spoilerEffect : this.val$spoilers) {
                if (spoilerEffect.getBounds().contains(x, y)) {
                    SpoilersClickDetector.this.trackingTap = true;
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (SpoilersClickDetector.this.trackingTap) {
                this.val$v.playSoundEffect(0);
                SpoilersClickDetector.this.trackingTap = false;
                int x = (int) motionEvent.getX();
                int y = ((int) motionEvent.getY()) + this.val$v.getScrollY();
                if (this.val$offsetPadding) {
                    x -= this.val$v.getPaddingLeft();
                    y -= this.val$v.getPaddingTop();
                }
                for (SpoilerEffect spoilerEffect : this.val$spoilers) {
                    if (spoilerEffect.getBounds().contains(x, y)) {
                        this.val$clickedListener.onSpoilerClicked(spoilerEffect, x, y);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public SpoilersClickDetector(View view, List<SpoilerEffect> list, boolean z, OnSpoilerClickedListener onSpoilerClickedListener) {
        this.gestureDetector = new GestureDetectorCompat(view.getContext(), new AnonymousClass1(view, z, list, onSpoilerClickedListener));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }
}
