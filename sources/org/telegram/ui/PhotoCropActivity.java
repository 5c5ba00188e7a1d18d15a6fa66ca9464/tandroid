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
        /* JADX WARN: Code restructure failed: missing block: B:135:0x022c, code lost:
            if (r6 < 160.0f) goto L138;
         */
        /* JADX WARN: Code restructure failed: missing block: B:155:0x028a, code lost:
            if (r13.rectSizeY < 160.0f) goto L138;
         */
        /* JADX WARN: Code restructure failed: missing block: B:156:0x028c, code lost:
            r13.rectSizeY = 160.0f;
         */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00b5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ boolean lambda$init$0(View view, MotionEvent motionEvent) {
            int i;
            float f;
            float f2;
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int dp = AndroidUtilities.dp(14.0f);
            if (motionEvent.getAction() == 0) {
                float f3 = this.rectX;
                float f4 = dp;
                float f5 = f3 - f4;
                if (f5 < x && f3 + f4 > x) {
                    float f6 = this.rectY;
                    if (f6 - f4 < y && f6 + f4 > y) {
                        this.draggingState = 1;
                        if (this.draggingState != 0) {
                            requestDisallowInterceptTouchEvent(true);
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                float f7 = this.rectSizeX;
                float f8 = f5 + f7;
                if (f8 < x && f3 + f4 + f7 > x) {
                    float f9 = this.rectY;
                    if (f9 - f4 < y && f9 + f4 > y) {
                        this.draggingState = 2;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f5 < x && f3 + f4 > x) {
                    float f10 = this.rectY;
                    float f11 = this.rectSizeY;
                    if ((f10 - f4) + f11 < y && f10 + f4 + f11 > y) {
                        this.draggingState = 3;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f8 < x && f3 + f4 + f7 > x) {
                    float f12 = this.rectY;
                    float f13 = this.rectSizeY;
                    if ((f12 - f4) + f13 < y && f12 + f4 + f13 > y) {
                        this.draggingState = 4;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f3 < x && f3 + f7 > x) {
                    float f14 = this.rectY;
                    if (f14 < y && f14 + this.rectSizeY > y) {
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
                float f15 = x - this.oldX;
                float f16 = y - this.oldY;
                if (i == 5) {
                    float f17 = this.rectX + f15;
                    this.rectX = f17;
                    float f18 = this.rectY + f16;
                    this.rectY = f18;
                    int i2 = this.bitmapX;
                    float f19 = i2;
                    if (f17 < f19) {
                        this.rectX = f19;
                    } else {
                        float f20 = this.rectSizeX;
                        float f21 = i2 + this.bitmapWidth;
                        if (f17 + f20 > f21) {
                            this.rectX = f21 - f20;
                        }
                    }
                    int i3 = this.bitmapY;
                    float f22 = i3;
                    if (f18 < f22) {
                        this.rectY = f22;
                    } else {
                        float f23 = this.rectSizeY;
                        float f24 = i3 + this.bitmapHeight;
                        if (f18 + f23 > f24) {
                            this.rectY = f24 - f23;
                        }
                    }
                } else if (i == 1) {
                    f = this.rectSizeX;
                    if (f - f15 < 160.0f) {
                        f15 = f - 160.0f;
                    }
                    float f25 = this.rectX;
                    float f26 = this.bitmapX;
                    if (f25 + f15 < f26) {
                        f15 = f26 - f25;
                    }
                    if (this.freeform) {
                        float f27 = this.rectSizeY;
                        if (f27 - f16 < 160.0f) {
                            f16 = f27 - 160.0f;
                        }
                        float f28 = this.rectY;
                        float f29 = this.bitmapY;
                        if (f28 + f16 < f29) {
                            f16 = f29 - f28;
                        }
                        this.rectX = f25 + f15;
                        this.rectY = f28 + f16;
                        this.rectSizeX = f - f15;
                        this.rectSizeY = f27 - f16;
                    } else {
                        float f30 = this.rectY;
                        float f31 = this.bitmapY;
                        if (f30 + f15 < f31) {
                            f15 = f31 - f30;
                        }
                        this.rectX = f25 + f15;
                        this.rectY = f30 + f15;
                        this.rectSizeX = f - f15;
                        f2 = this.rectSizeY - f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 2) {
                    float f32 = this.rectSizeX;
                    if (f32 + f15 < 160.0f) {
                        f15 = -(f32 - 160.0f);
                    }
                    float f33 = this.rectX;
                    float f34 = this.bitmapX + this.bitmapWidth;
                    if (f33 + f32 + f15 > f34) {
                        f15 = (f34 - f33) - f32;
                    }
                    if (this.freeform) {
                        float f35 = this.rectSizeY;
                        if (f35 - f16 < 160.0f) {
                            f16 = f35 - 160.0f;
                        }
                        float f36 = this.rectY;
                        float f37 = this.bitmapY;
                        if (f36 + f16 < f37) {
                            f16 = f37 - f36;
                        }
                        this.rectY = f36 + f16;
                        this.rectSizeX = f32 + f15;
                        this.rectSizeY = f35 - f16;
                    } else {
                        float f38 = this.rectY;
                        float f39 = this.bitmapY;
                        if (f38 - f15 < f39) {
                            f15 = f38 - f39;
                        }
                        this.rectY = f38 - f15;
                        this.rectSizeX = f32 + f15;
                        f2 = this.rectSizeY + f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 3) {
                    f = this.rectSizeX;
                    if (f - f15 < 160.0f) {
                        f15 = f - 160.0f;
                    }
                    float f40 = this.rectX;
                    float f41 = this.bitmapX;
                    if (f40 + f15 < f41) {
                        f15 = f41 - f40;
                    }
                    if (this.freeform) {
                        float f42 = this.rectY;
                        float f43 = this.rectSizeY;
                        float f44 = this.bitmapY + this.bitmapHeight;
                        if (f42 + f43 + f16 > f44) {
                            f16 = (f44 - f42) - f43;
                        }
                        this.rectX = f40 + f15;
                        this.rectSizeX = f - f15;
                        float f45 = f43 + f16;
                        this.rectSizeY = f45;
                    } else {
                        float f46 = this.rectY + f;
                        int i4 = this.bitmapY;
                        int i5 = this.bitmapHeight;
                        if (f46 - f15 > i4 + i5) {
                            f15 = (f46 - i4) - i5;
                        }
                        this.rectX = f40 + f15;
                        this.rectSizeX = f - f15;
                        f2 = this.rectSizeY - f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 4) {
                    float f47 = this.rectX;
                    float f48 = this.rectSizeX;
                    float f49 = this.bitmapX + this.bitmapWidth;
                    if (f47 + f48 + f15 > f49) {
                        f15 = (f49 - f47) - f48;
                    }
                    if (this.freeform) {
                        float f50 = this.rectY;
                        float f51 = this.rectSizeY;
                        float f52 = this.bitmapY + this.bitmapHeight;
                        if (f50 + f51 + f16 > f52) {
                            f16 = (f52 - f50) - f51;
                        }
                        this.rectSizeX = f48 + f15;
                        this.rectSizeY = f51 + f16;
                    } else {
                        float f53 = this.rectY;
                        float f54 = this.bitmapY + this.bitmapHeight;
                        if (f53 + f48 + f15 > f54) {
                            f15 = (f54 - f53) - f48;
                        }
                        this.rectSizeX = f48 + f15;
                        this.rectSizeY += f15;
                    }
                    if (this.rectSizeX < 160.0f) {
                        this.rectSizeX = 160.0f;
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
            int i3;
            float f2;
            if (this.viewWidth == 0 || this.viewHeight == 0 || PhotoCropActivity.this.imageToCrop == null) {
                return;
            }
            float f3 = this.rectX - this.bitmapX;
            float f4 = this.bitmapWidth;
            float f5 = f3 / f4;
            float f6 = this.rectY - this.bitmapY;
            float f7 = this.bitmapHeight;
            float f8 = f6 / f7;
            float f9 = this.rectSizeX / f4;
            float f10 = this.rectSizeY / f7;
            float width = PhotoCropActivity.this.imageToCrop.getWidth();
            float height = PhotoCropActivity.this.imageToCrop.getHeight();
            int i4 = this.viewWidth;
            float f11 = i4 / width;
            int i5 = this.viewHeight;
            if (f11 > i5 / height) {
                this.bitmapHeight = i5;
                this.bitmapWidth = (int) Math.ceil(width * f);
            } else {
                this.bitmapWidth = i4;
                this.bitmapHeight = (int) Math.ceil(height * f11);
            }
            this.bitmapX = ((this.viewWidth - this.bitmapWidth) / 2) + AndroidUtilities.dp(14.0f);
            int dp = ((this.viewHeight - this.bitmapHeight) / 2) + AndroidUtilities.dp(14.0f);
            this.bitmapY = dp;
            if (this.rectX == -1.0f && this.rectY == -1.0f) {
                if (this.freeform) {
                    this.rectY = dp;
                    this.rectX = this.bitmapX;
                    this.rectSizeX = this.bitmapWidth;
                    f2 = this.bitmapHeight;
                } else {
                    if (this.bitmapWidth > this.bitmapHeight) {
                        this.rectY = dp;
                        this.rectX = ((this.viewWidth - i2) / 2) + AndroidUtilities.dp(14.0f);
                        i3 = this.bitmapHeight;
                    } else {
                        this.rectX = this.bitmapX;
                        this.rectY = ((this.viewHeight - i) / 2) + AndroidUtilities.dp(14.0f);
                        i3 = this.bitmapWidth;
                    }
                    f2 = i3;
                    this.rectSizeX = f2;
                }
                this.rectSizeY = f2;
            } else {
                float f12 = this.bitmapWidth;
                this.rectX = (f5 * f12) + this.bitmapX;
                float f13 = this.bitmapHeight;
                this.rectY = (f8 * f13) + dp;
                this.rectSizeX = f9 * f12;
                this.rectSizeY = f10 * f13;
            }
            invalidate();
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

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.viewWidth = (i3 - i) - AndroidUtilities.dp(28.0f);
            this.viewHeight = (i4 - i2) - AndroidUtilities.dp(28.0f);
            updateBitmapSize();
        }
    }

    /* loaded from: classes4.dex */
    public interface PhotoEditActivityDelegate {
        void didFinishEdit(Bitmap bitmap);
    }

    public PhotoCropActivity(Bundle bundle) {
        super(bundle);
        this.delegate = null;
        this.sameBitmap = false;
        this.doneButtonPressed = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackgroundColor(-13421773);
        this.actionBar.setItemsBackgroundColor(-12763843, false);
        this.actionBar.setTitleColor(-1);
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString(R.string.CropImage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PhotoCropActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i != -1) {
                    if (i != 1) {
                        return;
                    }
                    if (PhotoCropActivity.this.delegate != null && !PhotoCropActivity.this.doneButtonPressed) {
                        Bitmap bitmap = PhotoCropActivity.this.view.getBitmap();
                        if (bitmap == PhotoCropActivity.this.imageToCrop) {
                            PhotoCropActivity.this.sameBitmap = true;
                        }
                        PhotoCropActivity.this.delegate.didFinishEdit(bitmap);
                        PhotoCropActivity.this.doneButtonPressed = true;
                    }
                }
                PhotoCropActivity.this.finishFragment();
            }
        });
        this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
        PhotoCropView photoCropView = new PhotoCropView(context);
        this.view = photoCropView;
        this.fragmentView = photoCropView;
        photoCropView.freeform = getArguments().getBoolean("freeform", false);
        this.fragmentView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        return this.fragmentView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
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

    public void setDelegate(PhotoEditActivityDelegate photoEditActivityDelegate) {
        this.delegate = photoEditActivityDelegate;
    }
}
