package org.telegram.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.PhotoCropActivity;
/* loaded from: classes4.dex */
public class PhotoCropActivity extends BaseFragment {
    private String bitmapKey;
    private PhotoEditActivityDelegate delegate;
    private boolean doneButtonPressed;
    private BitmapDrawable drawable;
    private Bitmap imageToCrop;
    private boolean sameBitmap;
    private PhotoCropView view;

    /* loaded from: classes4.dex */
    public interface PhotoEditActivityDelegate {
        void didFinishEdit(Bitmap bitmap);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class PhotoCropView extends FrameLayout {
        int bitmapHeight;
        int bitmapWidth;
        int bitmapX;
        int bitmapY;
        Paint circlePaint;
        int draggingState;
        boolean freeform;
        Paint halfPaint;
        float oldX;
        float oldY;
        Paint rectPaint;
        float rectSizeX;
        float rectSizeY;
        float rectX;
        float rectY;
        int viewHeight;
        int viewWidth;

        public PhotoCropView(Context context) {
            super(context);
            this.rectPaint = null;
            this.circlePaint = null;
            this.halfPaint = null;
            this.rectSizeX = 600.0f;
            this.rectSizeY = 600.0f;
            this.rectX = -1.0f;
            this.rectY = -1.0f;
            this.draggingState = 0;
            this.oldX = 0.0f;
            this.oldY = 0.0f;
            init();
        }

        private void init() {
            Paint paint = new Paint();
            this.rectPaint = paint;
            paint.setColor(1073412858);
            this.rectPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.rectPaint.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint();
            this.circlePaint = paint2;
            paint2.setColor(-1);
            Paint paint3 = new Paint();
            this.halfPaint = paint3;
            paint3.setColor(-939524096);
            setBackgroundColor(-13421773);
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PhotoCropActivity$PhotoCropView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$init$0;
                    lambda$init$0 = PhotoCropActivity.PhotoCropView.this.lambda$init$0(view, motionEvent);
                    return lambda$init$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00b5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ boolean lambda$init$0(View view, MotionEvent motionEvent) {
            int i;
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int dp = AndroidUtilities.dp(14.0f);
            if (motionEvent.getAction() == 0) {
                float f = this.rectX;
                float f2 = dp;
                float f3 = f - f2;
                if (f3 < x && f + f2 > x) {
                    float f4 = this.rectY;
                    if (f4 - f2 < y && f4 + f2 > y) {
                        this.draggingState = 1;
                        if (this.draggingState != 0) {
                            requestDisallowInterceptTouchEvent(true);
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                float f5 = this.rectSizeX;
                float f6 = f3 + f5;
                if (f6 < x && f + f2 + f5 > x) {
                    float f7 = this.rectY;
                    if (f7 - f2 < y && f7 + f2 > y) {
                        this.draggingState = 2;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f3 < x && f + f2 > x) {
                    float f8 = this.rectY;
                    float f9 = this.rectSizeY;
                    if ((f8 - f2) + f9 < y && f8 + f2 + f9 > y) {
                        this.draggingState = 3;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f6 < x && f + f2 + f5 > x) {
                    float f10 = this.rectY;
                    float f11 = this.rectSizeY;
                    if ((f10 - f2) + f11 < y && f10 + f2 + f11 > y) {
                        this.draggingState = 4;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f < x && f + f5 > x) {
                    float f12 = this.rectY;
                    if (f12 < y && f12 + this.rectSizeY > y) {
                        this.draggingState = 5;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                this.draggingState = 0;
                if (this.draggingState != 0) {
                }
                this.oldX = x;
                this.oldY = y;
            } else if (motionEvent.getAction() == 1) {
                this.draggingState = 0;
            } else if (motionEvent.getAction() == 2 && (i = this.draggingState) != 0) {
                float f13 = x - this.oldX;
                float f14 = y - this.oldY;
                if (i == 5) {
                    float f15 = this.rectX + f13;
                    this.rectX = f15;
                    float f16 = this.rectY + f14;
                    this.rectY = f16;
                    int i2 = this.bitmapX;
                    float f17 = i2;
                    if (f15 < f17) {
                        this.rectX = f17;
                    } else {
                        float f18 = this.rectSizeX;
                        float f19 = i2 + this.bitmapWidth;
                        if (f15 + f18 > f19) {
                            this.rectX = f19 - f18;
                        }
                    }
                    int i3 = this.bitmapY;
                    float f20 = i3;
                    if (f16 < f20) {
                        this.rectY = f20;
                    } else {
                        float f21 = this.rectSizeY;
                        float f22 = i3 + this.bitmapHeight;
                        if (f16 + f21 > f22) {
                            this.rectY = f22 - f21;
                        }
                    }
                } else if (i == 1) {
                    float f23 = this.rectSizeX;
                    if (f23 - f13 < 160.0f) {
                        f13 = f23 - 160.0f;
                    }
                    float f24 = this.rectX;
                    float f25 = this.bitmapX;
                    if (f24 + f13 < f25) {
                        f13 = f25 - f24;
                    }
                    if (!this.freeform) {
                        float f26 = this.rectY;
                        float f27 = this.bitmapY;
                        if (f26 + f13 < f27) {
                            f13 = f27 - f26;
                        }
                        this.rectX = f24 + f13;
                        this.rectY = f26 + f13;
                        this.rectSizeX = f23 - f13;
                        this.rectSizeY -= f13;
                    } else {
                        float f28 = this.rectSizeY;
                        if (f28 - f14 < 160.0f) {
                            f14 = f28 - 160.0f;
                        }
                        float f29 = this.rectY;
                        float f30 = this.bitmapY;
                        if (f29 + f14 < f30) {
                            f14 = f30 - f29;
                        }
                        this.rectX = f24 + f13;
                        this.rectY = f29 + f14;
                        this.rectSizeX = f23 - f13;
                        this.rectSizeY = f28 - f14;
                    }
                } else if (i == 2) {
                    float f31 = this.rectSizeX;
                    if (f31 + f13 < 160.0f) {
                        f13 = -(f31 - 160.0f);
                    }
                    float f32 = this.rectX;
                    float f33 = this.bitmapX + this.bitmapWidth;
                    if (f32 + f31 + f13 > f33) {
                        f13 = (f33 - f32) - f31;
                    }
                    if (!this.freeform) {
                        float f34 = this.rectY;
                        float f35 = this.bitmapY;
                        if (f34 - f13 < f35) {
                            f13 = f34 - f35;
                        }
                        this.rectY = f34 - f13;
                        this.rectSizeX = f31 + f13;
                        this.rectSizeY += f13;
                    } else {
                        float f36 = this.rectSizeY;
                        if (f36 - f14 < 160.0f) {
                            f14 = f36 - 160.0f;
                        }
                        float f37 = this.rectY;
                        float f38 = this.bitmapY;
                        if (f37 + f14 < f38) {
                            f14 = f38 - f37;
                        }
                        this.rectY = f37 + f14;
                        this.rectSizeX = f31 + f13;
                        this.rectSizeY = f36 - f14;
                    }
                } else if (i == 3) {
                    float f39 = this.rectSizeX;
                    if (f39 - f13 < 160.0f) {
                        f13 = f39 - 160.0f;
                    }
                    float f40 = this.rectX;
                    float f41 = this.bitmapX;
                    if (f40 + f13 < f41) {
                        f13 = f41 - f40;
                    }
                    if (!this.freeform) {
                        float f42 = this.rectY + f39;
                        int i4 = this.bitmapY;
                        int i5 = this.bitmapHeight;
                        if (f42 - f13 > i4 + i5) {
                            f13 = (f42 - i4) - i5;
                        }
                        this.rectX = f40 + f13;
                        this.rectSizeX = f39 - f13;
                        this.rectSizeY -= f13;
                    } else {
                        float f43 = this.rectY;
                        float f44 = this.rectSizeY;
                        float f45 = this.bitmapY + this.bitmapHeight;
                        if (f43 + f44 + f14 > f45) {
                            f14 = (f45 - f43) - f44;
                        }
                        this.rectX = f40 + f13;
                        this.rectSizeX = f39 - f13;
                        float f46 = f44 + f14;
                        this.rectSizeY = f46;
                        if (f46 < 160.0f) {
                            this.rectSizeY = 160.0f;
                        }
                    }
                } else if (i == 4) {
                    float f47 = this.rectX;
                    float f48 = this.rectSizeX;
                    float f49 = this.bitmapX + this.bitmapWidth;
                    if (f47 + f48 + f13 > f49) {
                        f13 = (f49 - f47) - f48;
                    }
                    if (!this.freeform) {
                        float f50 = this.rectY;
                        float f51 = this.bitmapY + this.bitmapHeight;
                        if (f50 + f48 + f13 > f51) {
                            f13 = (f51 - f50) - f48;
                        }
                        this.rectSizeX = f48 + f13;
                        this.rectSizeY += f13;
                    } else {
                        float f52 = this.rectY;
                        float f53 = this.rectSizeY;
                        float f54 = this.bitmapY + this.bitmapHeight;
                        if (f52 + f53 + f14 > f54) {
                            f14 = (f54 - f52) - f53;
                        }
                        this.rectSizeX = f48 + f13;
                        this.rectSizeY = f53 + f14;
                    }
                    if (this.rectSizeX < 160.0f) {
                        this.rectSizeX = 160.0f;
                    }
                    if (this.rectSizeY < 160.0f) {
                        this.rectSizeY = 160.0f;
                    }
                }
                this.oldX = x;
                this.oldY = y;
                invalidate();
            }
            return true;
        }

        private void updateBitmapSize() {
            float f;
            int i;
            int i2;
            if (this.viewWidth == 0 || this.viewHeight == 0 || PhotoCropActivity.this.imageToCrop == null) {
                return;
            }
            float f2 = this.rectX - this.bitmapX;
            float f3 = this.bitmapWidth;
            float f4 = f2 / f3;
            float f5 = this.rectY - this.bitmapY;
            float f6 = this.bitmapHeight;
            float f7 = f5 / f6;
            float f8 = this.rectSizeX / f3;
            float f9 = this.rectSizeY / f6;
            float width = PhotoCropActivity.this.imageToCrop.getWidth();
            float height = PhotoCropActivity.this.imageToCrop.getHeight();
            int i3 = this.viewWidth;
            float f10 = i3 / width;
            int i4 = this.viewHeight;
            if (f10 > i4 / height) {
                this.bitmapHeight = i4;
                this.bitmapWidth = (int) Math.ceil(width * f);
            } else {
                this.bitmapWidth = i3;
                this.bitmapHeight = (int) Math.ceil(height * f10);
            }
            this.bitmapX = ((this.viewWidth - this.bitmapWidth) / 2) + AndroidUtilities.dp(14.0f);
            int dp = ((this.viewHeight - this.bitmapHeight) / 2) + AndroidUtilities.dp(14.0f);
            this.bitmapY = dp;
            if (this.rectX == -1.0f && this.rectY == -1.0f) {
                if (this.freeform) {
                    this.rectY = dp;
                    this.rectX = this.bitmapX;
                    this.rectSizeX = this.bitmapWidth;
                    this.rectSizeY = this.bitmapHeight;
                } else {
                    if (this.bitmapWidth > this.bitmapHeight) {
                        this.rectY = dp;
                        this.rectX = ((this.viewWidth - i2) / 2) + AndroidUtilities.dp(14.0f);
                        float f11 = this.bitmapHeight;
                        this.rectSizeX = f11;
                        this.rectSizeY = f11;
                    } else {
                        this.rectX = this.bitmapX;
                        this.rectY = ((this.viewHeight - i) / 2) + AndroidUtilities.dp(14.0f);
                        float f12 = this.bitmapWidth;
                        this.rectSizeX = f12;
                        this.rectSizeY = f12;
                    }
                }
            } else {
                float f13 = this.bitmapWidth;
                this.rectX = (f4 * f13) + this.bitmapX;
                float f14 = this.bitmapHeight;
                this.rectY = (f7 * f14) + dp;
                this.rectSizeX = f8 * f13;
                this.rectSizeY = f9 * f14;
            }
            invalidate();
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.viewWidth = (i3 - i) - AndroidUtilities.dp(28.0f);
            this.viewHeight = (i4 - i2) - AndroidUtilities.dp(28.0f);
            updateBitmapSize();
        }

        public Bitmap getBitmap() {
            float f = this.rectX - this.bitmapX;
            float f2 = this.bitmapWidth;
            float f3 = (this.rectY - this.bitmapY) / this.bitmapHeight;
            float f4 = this.rectSizeX / f2;
            float f5 = this.rectSizeY / f2;
            int width = (int) ((f / f2) * PhotoCropActivity.this.imageToCrop.getWidth());
            int height = (int) (f3 * PhotoCropActivity.this.imageToCrop.getHeight());
            int width2 = (int) (f4 * PhotoCropActivity.this.imageToCrop.getWidth());
            int width3 = (int) (f5 * PhotoCropActivity.this.imageToCrop.getWidth());
            if (width < 0) {
                width = 0;
            }
            if (height < 0) {
                height = 0;
            }
            if (width + width2 > PhotoCropActivity.this.imageToCrop.getWidth()) {
                width2 = PhotoCropActivity.this.imageToCrop.getWidth() - width;
            }
            if (height + width3 > PhotoCropActivity.this.imageToCrop.getHeight()) {
                width3 = PhotoCropActivity.this.imageToCrop.getHeight() - height;
            }
            try {
                return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, width, height, width2, width3);
            } catch (Throwable th) {
                FileLog.e(th);
                System.gc();
                try {
                    return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, width, height, width2, width3);
                } catch (Throwable th2) {
                    FileLog.e(th2);
                    return null;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x01a3 A[LOOP:0: B:15:0x01a1->B:16:0x01a3, LOOP_END] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            int i2;
            int i3;
            if (PhotoCropActivity.this.drawable != null) {
                try {
                    BitmapDrawable bitmapDrawable = PhotoCropActivity.this.drawable;
                    int i4 = this.bitmapX;
                    int i5 = this.bitmapY;
                    bitmapDrawable.setBounds(i4, i5, this.bitmapWidth + i4, this.bitmapHeight + i5);
                    try {
                        PhotoCropActivity.this.drawable.draw(canvas);
                    } catch (Throwable th) {
                        th = th;
                        FileLog.e(th);
                        canvas.drawRect(this.bitmapX, this.bitmapY, i + this.bitmapWidth, this.rectY, this.halfPaint);
                        float f = this.rectY;
                        canvas.drawRect(this.bitmapX, f, this.rectX, f + this.rectSizeY, this.halfPaint);
                        float f2 = this.rectY;
                        canvas.drawRect(this.rectX + this.rectSizeX, f2, this.bitmapX + this.bitmapWidth, f2 + this.rectSizeY, this.halfPaint);
                        canvas.drawRect(this.bitmapX, this.rectSizeY + this.rectY, i2 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
                        float f3 = this.rectX;
                        float f4 = this.rectY;
                        canvas.drawRect(f3, f4, f3 + this.rectSizeX, f4 + this.rectSizeY, this.rectPaint);
                        int dp = AndroidUtilities.dp(1.0f);
                        float f5 = dp;
                        float f6 = this.rectX + f5;
                        float f7 = dp * 3;
                        canvas.drawRect(f6, this.rectY + f5, f6 + AndroidUtilities.dp(20.0f), this.rectY + f7, this.circlePaint);
                        float f8 = this.rectX;
                        float f9 = this.rectY + f5;
                        canvas.drawRect(f8 + f5, f9, f8 + f7, f9 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        float dp2 = ((this.rectX + this.rectSizeX) - f5) - AndroidUtilities.dp(20.0f);
                        float f10 = this.rectY;
                        canvas.drawRect(dp2, f10 + f5, (this.rectX + this.rectSizeX) - f5, f10 + f7, this.circlePaint);
                        float f11 = this.rectX + this.rectSizeX;
                        float f12 = this.rectY + f5;
                        canvas.drawRect(f11 - f7, f12, f11 - f5, f12 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        canvas.drawRect(this.rectX + f5, ((this.rectY + this.rectSizeY) - f5) - AndroidUtilities.dp(20.0f), this.rectX + f7, (this.rectY + this.rectSizeY) - f5, this.circlePaint);
                        float f13 = this.rectX + f5;
                        canvas.drawRect(f13, (this.rectY + this.rectSizeY) - f7, f13 + AndroidUtilities.dp(20.0f), (this.rectY + this.rectSizeY) - f5, this.circlePaint);
                        float dp3 = ((this.rectX + this.rectSizeX) - f5) - AndroidUtilities.dp(20.0f);
                        float f14 = this.rectY + this.rectSizeY;
                        canvas.drawRect(dp3, f14 - f7, (this.rectX + this.rectSizeX) - f5, f14 - f5, this.circlePaint);
                        canvas.drawRect((this.rectX + this.rectSizeX) - f7, ((this.rectY + this.rectSizeY) - f5) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f5, (this.rectY + this.rectSizeY) - f5, this.circlePaint);
                        while (i3 < 3) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            canvas.drawRect(this.bitmapX, this.bitmapY, i + this.bitmapWidth, this.rectY, this.halfPaint);
            float f15 = this.rectY;
            canvas.drawRect(this.bitmapX, f15, this.rectX, f15 + this.rectSizeY, this.halfPaint);
            float f22 = this.rectY;
            canvas.drawRect(this.rectX + this.rectSizeX, f22, this.bitmapX + this.bitmapWidth, f22 + this.rectSizeY, this.halfPaint);
            canvas.drawRect(this.bitmapX, this.rectSizeY + this.rectY, i2 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
            float f32 = this.rectX;
            float f42 = this.rectY;
            canvas.drawRect(f32, f42, f32 + this.rectSizeX, f42 + this.rectSizeY, this.rectPaint);
            int dp4 = AndroidUtilities.dp(1.0f);
            float f52 = dp4;
            float f62 = this.rectX + f52;
            float f72 = dp4 * 3;
            canvas.drawRect(f62, this.rectY + f52, f62 + AndroidUtilities.dp(20.0f), this.rectY + f72, this.circlePaint);
            float f82 = this.rectX;
            float f92 = this.rectY + f52;
            canvas.drawRect(f82 + f52, f92, f82 + f72, f92 + AndroidUtilities.dp(20.0f), this.circlePaint);
            float dp22 = ((this.rectX + this.rectSizeX) - f52) - AndroidUtilities.dp(20.0f);
            float f102 = this.rectY;
            canvas.drawRect(dp22, f102 + f52, (this.rectX + this.rectSizeX) - f52, f102 + f72, this.circlePaint);
            float f112 = this.rectX + this.rectSizeX;
            float f122 = this.rectY + f52;
            canvas.drawRect(f112 - f72, f122, f112 - f52, f122 + AndroidUtilities.dp(20.0f), this.circlePaint);
            canvas.drawRect(this.rectX + f52, ((this.rectY + this.rectSizeY) - f52) - AndroidUtilities.dp(20.0f), this.rectX + f72, (this.rectY + this.rectSizeY) - f52, this.circlePaint);
            float f132 = this.rectX + f52;
            canvas.drawRect(f132, (this.rectY + this.rectSizeY) - f72, f132 + AndroidUtilities.dp(20.0f), (this.rectY + this.rectSizeY) - f52, this.circlePaint);
            float dp32 = ((this.rectX + this.rectSizeX) - f52) - AndroidUtilities.dp(20.0f);
            float f142 = this.rectY + this.rectSizeY;
            canvas.drawRect(dp32, f142 - f72, (this.rectX + this.rectSizeX) - f52, f142 - f52, this.circlePaint);
            canvas.drawRect((this.rectX + this.rectSizeX) - f72, ((this.rectY + this.rectSizeY) - f52) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f52, (this.rectY + this.rectSizeY) - f52, this.circlePaint);
            for (i3 = 1; i3 < 3; i3++) {
                float f16 = this.rectX;
                float f17 = i3;
                float f18 = (this.rectSizeX / 3.0f) * f17;
                float f19 = this.rectY;
                canvas.drawRect(f16 + f18, f19 + f52, f16 + f52 + f18, (f19 + this.rectSizeY) - f52, this.circlePaint);
                float f20 = this.rectX;
                float f21 = this.rectY + ((this.rectSizeY / 3.0f) * f17);
                canvas.drawRect(f20 + f52, f21, this.rectSizeX + (f20 - f52), f21 + f52, this.circlePaint);
            }
        }
    }

    public PhotoCropActivity(Bundle bundle) {
        super(bundle);
        this.delegate = null;
        this.sameBitmap = false;
        this.doneButtonPressed = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        int max;
        if (this.imageToCrop == null) {
            String string = getArguments().getString("photoPath");
            Uri uri = (Uri) getArguments().getParcelable("photoUri");
            if (string == null && uri == null) {
                return false;
            }
            if (string != null && !new File(string).exists()) {
                return false;
            }
            if (AndroidUtilities.isTablet()) {
                max = AndroidUtilities.dp(520.0f);
            } else {
                Point point = AndroidUtilities.displaySize;
                max = Math.max(point.x, point.y);
            }
            float f = max;
            Bitmap loadBitmap = ImageLoader.loadBitmap(string, uri, f, f, true);
            this.imageToCrop = loadBitmap;
            if (loadBitmap == null) {
                return false;
            }
        }
        this.drawable = new BitmapDrawable(this.imageToCrop);
        super.onFragmentCreate();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        Bitmap bitmap;
        super.onFragmentDestroy();
        if (this.bitmapKey != null && ImageLoader.getInstance().decrementUseCount(this.bitmapKey) && !ImageLoader.getInstance().isInMemCache(this.bitmapKey, false)) {
            this.bitmapKey = null;
        }
        if (this.bitmapKey == null && (bitmap = this.imageToCrop) != null && !this.sameBitmap) {
            bitmap.recycle();
            this.imageToCrop = null;
        }
        this.drawable = null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackgroundColor(-13421773);
        this.actionBar.setItemsBackgroundColor(-12763843, false);
        this.actionBar.setTitleColor(-1);
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("CropImage", R.string.CropImage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PhotoCropActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    PhotoCropActivity.this.finishFragment();
                } else if (i == 1) {
                    if (PhotoCropActivity.this.delegate != null && !PhotoCropActivity.this.doneButtonPressed) {
                        Bitmap bitmap = PhotoCropActivity.this.view.getBitmap();
                        if (bitmap == PhotoCropActivity.this.imageToCrop) {
                            PhotoCropActivity.this.sameBitmap = true;
                        }
                        PhotoCropActivity.this.delegate.didFinishEdit(bitmap);
                        PhotoCropActivity.this.doneButtonPressed = true;
                    }
                    PhotoCropActivity.this.finishFragment();
                }
            }
        });
        this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", R.string.Done));
        PhotoCropView photoCropView = new PhotoCropView(context);
        this.view = photoCropView;
        this.fragmentView = photoCropView;
        photoCropView.freeform = getArguments().getBoolean("freeform", false);
        this.fragmentView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        return this.fragmentView;
    }

    public void setDelegate(PhotoEditActivityDelegate photoEditActivityDelegate) {
        this.delegate = photoEditActivityDelegate;
    }
}
