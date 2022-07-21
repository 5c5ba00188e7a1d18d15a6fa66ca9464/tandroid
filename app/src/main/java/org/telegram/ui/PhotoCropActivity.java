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
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.PhotoCropActivity;
/* loaded from: classes3.dex */
public class PhotoCropActivity extends BaseFragment {
    private String bitmapKey;
    private BitmapDrawable drawable;
    private Bitmap imageToCrop;
    private PhotoCropView view;
    private PhotoEditActivityDelegate delegate = null;
    private boolean sameBitmap = false;
    private boolean doneButtonPressed = false;

    /* loaded from: classes3.dex */
    public interface PhotoEditActivityDelegate {
        void didFinishEdit(Bitmap bitmap);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    /* loaded from: classes3.dex */
    public class PhotoCropView extends FrameLayout {
        int bitmapHeight;
        int bitmapWidth;
        int bitmapX;
        int bitmapY;
        boolean freeform;
        int viewHeight;
        int viewWidth;
        Paint rectPaint = null;
        Paint circlePaint = null;
        Paint halfPaint = null;
        float rectSizeX = 600.0f;
        float rectSizeY = 600.0f;
        float rectX = -1.0f;
        float rectY = -1.0f;
        int draggingState = 0;
        float oldX = 0.0f;
        float oldY = 0.0f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PhotoCropView(Context context) {
            super(context);
            PhotoCropActivity.this = r1;
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

        /* JADX WARN: Removed duplicated region for block: B:52:0x00bb  */
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
                if (f - f2 < x && f + f2 > x) {
                    float f3 = this.rectY;
                    if (f3 - f2 < y && f3 + f2 > y) {
                        this.draggingState = 1;
                        if (this.draggingState != 0) {
                            requestDisallowInterceptTouchEvent(true);
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                float f4 = this.rectSizeX;
                if ((f - f2) + f4 < x && f + f2 + f4 > x) {
                    float f5 = this.rectY;
                    if (f5 - f2 < y && f5 + f2 > y) {
                        this.draggingState = 2;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f - f2 < x && f + f2 > x) {
                    float f6 = this.rectY;
                    float f7 = this.rectSizeY;
                    if ((f6 - f2) + f7 < y && f6 + f2 + f7 > y) {
                        this.draggingState = 3;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if ((f - f2) + f4 < x && f + f2 + f4 > x) {
                    float f8 = this.rectY;
                    float f9 = this.rectSizeY;
                    if ((f8 - f2) + f9 < y && f8 + f2 + f9 > y) {
                        this.draggingState = 4;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f < x && f + f4 > x) {
                    float f10 = this.rectY;
                    if (f10 < y && f10 + this.rectSizeY > y) {
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
                float f11 = x - this.oldX;
                float f12 = y - this.oldY;
                if (i == 5) {
                    float f13 = this.rectX + f11;
                    this.rectX = f13;
                    float f14 = this.rectY + f12;
                    this.rectY = f14;
                    int i2 = this.bitmapX;
                    if (f13 < i2) {
                        this.rectX = i2;
                    } else {
                        float f15 = this.rectSizeX;
                        int i3 = this.bitmapWidth;
                        if (f13 + f15 > i2 + i3) {
                            this.rectX = (i2 + i3) - f15;
                        }
                    }
                    int i4 = this.bitmapY;
                    if (f14 < i4) {
                        this.rectY = i4;
                    } else {
                        float f16 = this.rectSizeY;
                        int i5 = this.bitmapHeight;
                        if (f14 + f16 > i4 + i5) {
                            this.rectY = (i4 + i5) - f16;
                        }
                    }
                } else if (i == 1) {
                    float f17 = this.rectSizeX;
                    if (f17 - f11 < 160.0f) {
                        f11 = f17 - 160.0f;
                    }
                    float f18 = this.rectX;
                    int i6 = this.bitmapX;
                    if (f18 + f11 < i6) {
                        f11 = i6 - f18;
                    }
                    if (!this.freeform) {
                        float f19 = this.rectY;
                        int i7 = this.bitmapY;
                        if (f19 + f11 < i7) {
                            f11 = i7 - f19;
                        }
                        this.rectX = f18 + f11;
                        this.rectY = f19 + f11;
                        this.rectSizeX = f17 - f11;
                        this.rectSizeY -= f11;
                    } else {
                        float f20 = this.rectSizeY;
                        if (f20 - f12 < 160.0f) {
                            f12 = f20 - 160.0f;
                        }
                        float f21 = this.rectY;
                        int i8 = this.bitmapY;
                        if (f21 + f12 < i8) {
                            f12 = i8 - f21;
                        }
                        this.rectX = f18 + f11;
                        this.rectY = f21 + f12;
                        this.rectSizeX = f17 - f11;
                        this.rectSizeY = f20 - f12;
                    }
                } else if (i == 2) {
                    float f22 = this.rectSizeX;
                    if (f22 + f11 < 160.0f) {
                        f11 = -(f22 - 160.0f);
                    }
                    float f23 = this.rectX;
                    int i9 = this.bitmapX;
                    int i10 = this.bitmapWidth;
                    if (f23 + f22 + f11 > i9 + i10) {
                        f11 = ((i9 + i10) - f23) - f22;
                    }
                    if (!this.freeform) {
                        float f24 = this.rectY;
                        int i11 = this.bitmapY;
                        if (f24 - f11 < i11) {
                            f11 = f24 - i11;
                        }
                        this.rectY = f24 - f11;
                        this.rectSizeX = f22 + f11;
                        this.rectSizeY += f11;
                    } else {
                        float f25 = this.rectSizeY;
                        if (f25 - f12 < 160.0f) {
                            f12 = f25 - 160.0f;
                        }
                        float f26 = this.rectY;
                        int i12 = this.bitmapY;
                        if (f26 + f12 < i12) {
                            f12 = i12 - f26;
                        }
                        this.rectY = f26 + f12;
                        this.rectSizeX = f22 + f11;
                        this.rectSizeY = f25 - f12;
                    }
                } else if (i == 3) {
                    float f27 = this.rectSizeX;
                    if (f27 - f11 < 160.0f) {
                        f11 = f27 - 160.0f;
                    }
                    float f28 = this.rectX;
                    int i13 = this.bitmapX;
                    if (f28 + f11 < i13) {
                        f11 = i13 - f28;
                    }
                    if (!this.freeform) {
                        float f29 = this.rectY;
                        int i14 = this.bitmapY;
                        int i15 = this.bitmapHeight;
                        if ((f29 + f27) - f11 > i14 + i15) {
                            f11 = ((f29 + f27) - i14) - i15;
                        }
                        this.rectX = f28 + f11;
                        this.rectSizeX = f27 - f11;
                        this.rectSizeY -= f11;
                    } else {
                        float f30 = this.rectY;
                        float f31 = this.rectSizeY;
                        int i16 = this.bitmapY;
                        int i17 = this.bitmapHeight;
                        if (f30 + f31 + f12 > i16 + i17) {
                            f12 = ((i16 + i17) - f30) - f31;
                        }
                        this.rectX = f28 + f11;
                        this.rectSizeX = f27 - f11;
                        float f32 = f31 + f12;
                        this.rectSizeY = f32;
                        if (f32 < 160.0f) {
                            this.rectSizeY = 160.0f;
                        }
                    }
                } else if (i == 4) {
                    float f33 = this.rectX;
                    float f34 = this.rectSizeX;
                    int i18 = this.bitmapX;
                    int i19 = this.bitmapWidth;
                    if (f33 + f34 + f11 > i18 + i19) {
                        f11 = ((i18 + i19) - f33) - f34;
                    }
                    if (!this.freeform) {
                        float f35 = this.rectY;
                        int i20 = this.bitmapY;
                        int i21 = this.bitmapHeight;
                        if (f35 + f34 + f11 > i20 + i21) {
                            f11 = ((i20 + i21) - f35) - f34;
                        }
                        this.rectSizeX = f34 + f11;
                        this.rectSizeY += f11;
                    } else {
                        float f36 = this.rectY;
                        float f37 = this.rectSizeY;
                        int i22 = this.bitmapY;
                        int i23 = this.bitmapHeight;
                        if (f36 + f37 + f12 > i22 + i23) {
                            f12 = ((i22 + i23) - f36) - f37;
                        }
                        this.rectSizeX = f34 + f11;
                        this.rectSizeY = f37 + f12;
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
            int i3 = this.bitmapWidth;
            float f3 = f2 / i3;
            float f4 = this.rectY - this.bitmapY;
            int i4 = this.bitmapHeight;
            float f5 = f4 / i4;
            float f6 = this.rectSizeX / i3;
            float f7 = this.rectSizeY / i4;
            float width = PhotoCropActivity.this.imageToCrop.getWidth();
            float height = PhotoCropActivity.this.imageToCrop.getHeight();
            int i5 = this.viewWidth;
            float f8 = i5 / width;
            int i6 = this.viewHeight;
            if (f8 > i6 / height) {
                this.bitmapHeight = i6;
                this.bitmapWidth = (int) Math.ceil(width * f);
            } else {
                this.bitmapWidth = i5;
                this.bitmapHeight = (int) Math.ceil(height * f8);
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
                        int i7 = this.bitmapHeight;
                        this.rectSizeX = i7;
                        this.rectSizeY = i7;
                    } else {
                        this.rectX = this.bitmapX;
                        this.rectY = ((this.viewHeight - i) / 2) + AndroidUtilities.dp(14.0f);
                        int i8 = this.bitmapWidth;
                        this.rectSizeX = i8;
                        this.rectSizeY = i8;
                    }
                }
            } else {
                int i9 = this.bitmapWidth;
                this.rectX = (f3 * i9) + this.bitmapX;
                int i10 = this.bitmapHeight;
                this.rectY = (f5 * i10) + dp;
                this.rectSizeX = f6 * i9;
                this.rectSizeY = f7 * i10;
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
            int i = this.bitmapWidth;
            float f2 = (this.rectY - this.bitmapY) / this.bitmapHeight;
            float f3 = this.rectSizeX / i;
            float f4 = this.rectSizeY / i;
            int width = (int) ((f / i) * PhotoCropActivity.this.imageToCrop.getWidth());
            int height = (int) (f2 * PhotoCropActivity.this.imageToCrop.getHeight());
            int width2 = (int) (f3 * PhotoCropActivity.this.imageToCrop.getWidth());
            int width3 = (int) (f4 * PhotoCropActivity.this.imageToCrop.getWidth());
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

        /* JADX WARN: Removed duplicated region for block: B:14:0x01b4 A[LOOP:0: B:13:0x01b2->B:14:0x01b4, LOOP_END] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            Throwable th;
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
                    } catch (Throwable th2) {
                        th = th2;
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
                        float f5 = this.rectX;
                        float f6 = dp;
                        float f7 = dp * 3;
                        canvas.drawRect(f5 + f6, this.rectY + f6, AndroidUtilities.dp(20.0f) + f5 + f6, this.rectY + f7, this.circlePaint);
                        float f8 = this.rectX;
                        float f9 = this.rectY;
                        canvas.drawRect(f8 + f6, f9 + f6, f8 + f7, f9 + f6 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        float dp2 = ((this.rectX + this.rectSizeX) - f6) - AndroidUtilities.dp(20.0f);
                        float f10 = this.rectY;
                        canvas.drawRect(dp2, f10 + f6, (this.rectX + this.rectSizeX) - f6, f10 + f7, this.circlePaint);
                        float f11 = this.rectX;
                        float f12 = this.rectSizeX;
                        float f13 = this.rectY;
                        canvas.drawRect((f11 + f12) - f7, f13 + f6, (f11 + f12) - f6, f13 + f6 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        canvas.drawRect(this.rectX + f6, ((this.rectY + this.rectSizeY) - f6) - AndroidUtilities.dp(20.0f), this.rectX + f7, (this.rectY + this.rectSizeY) - f6, this.circlePaint);
                        float f14 = this.rectX;
                        canvas.drawRect(f14 + f6, (this.rectY + this.rectSizeY) - f7, AndroidUtilities.dp(20.0f) + f14 + f6, (this.rectY + this.rectSizeY) - f6, this.circlePaint);
                        float dp3 = ((this.rectX + this.rectSizeX) - f6) - AndroidUtilities.dp(20.0f);
                        float f15 = this.rectY;
                        float f16 = this.rectSizeY;
                        canvas.drawRect(dp3, (f15 + f16) - f7, (this.rectX + this.rectSizeX) - f6, (f15 + f16) - f6, this.circlePaint);
                        canvas.drawRect((this.rectX + this.rectSizeX) - f7, ((this.rectY + this.rectSizeY) - f6) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f6, (this.rectY + this.rectSizeY) - f6, this.circlePaint);
                        while (i3 < 3) {
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            canvas.drawRect(this.bitmapX, this.bitmapY, i + this.bitmapWidth, this.rectY, this.halfPaint);
            float f17 = this.rectY;
            canvas.drawRect(this.bitmapX, f17, this.rectX, f17 + this.rectSizeY, this.halfPaint);
            float f22 = this.rectY;
            canvas.drawRect(this.rectX + this.rectSizeX, f22, this.bitmapX + this.bitmapWidth, f22 + this.rectSizeY, this.halfPaint);
            canvas.drawRect(this.bitmapX, this.rectSizeY + this.rectY, i2 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
            float f32 = this.rectX;
            float f42 = this.rectY;
            canvas.drawRect(f32, f42, f32 + this.rectSizeX, f42 + this.rectSizeY, this.rectPaint);
            int dp4 = AndroidUtilities.dp(1.0f);
            float f52 = this.rectX;
            float f62 = dp4;
            float f72 = dp4 * 3;
            canvas.drawRect(f52 + f62, this.rectY + f62, AndroidUtilities.dp(20.0f) + f52 + f62, this.rectY + f72, this.circlePaint);
            float f82 = this.rectX;
            float f92 = this.rectY;
            canvas.drawRect(f82 + f62, f92 + f62, f82 + f72, f92 + f62 + AndroidUtilities.dp(20.0f), this.circlePaint);
            float dp22 = ((this.rectX + this.rectSizeX) - f62) - AndroidUtilities.dp(20.0f);
            float f102 = this.rectY;
            canvas.drawRect(dp22, f102 + f62, (this.rectX + this.rectSizeX) - f62, f102 + f72, this.circlePaint);
            float f112 = this.rectX;
            float f122 = this.rectSizeX;
            float f132 = this.rectY;
            canvas.drawRect((f112 + f122) - f72, f132 + f62, (f112 + f122) - f62, f132 + f62 + AndroidUtilities.dp(20.0f), this.circlePaint);
            canvas.drawRect(this.rectX + f62, ((this.rectY + this.rectSizeY) - f62) - AndroidUtilities.dp(20.0f), this.rectX + f72, (this.rectY + this.rectSizeY) - f62, this.circlePaint);
            float f142 = this.rectX;
            canvas.drawRect(f142 + f62, (this.rectY + this.rectSizeY) - f72, AndroidUtilities.dp(20.0f) + f142 + f62, (this.rectY + this.rectSizeY) - f62, this.circlePaint);
            float dp32 = ((this.rectX + this.rectSizeX) - f62) - AndroidUtilities.dp(20.0f);
            float f152 = this.rectY;
            float f162 = this.rectSizeY;
            canvas.drawRect(dp32, (f152 + f162) - f72, (this.rectX + this.rectSizeX) - f62, (f152 + f162) - f62, this.circlePaint);
            canvas.drawRect((this.rectX + this.rectSizeX) - f72, ((this.rectY + this.rectSizeY) - f62) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f62, (this.rectY + this.rectSizeY) - f62, this.circlePaint);
            for (i3 = 1; i3 < 3; i3++) {
                float f18 = this.rectX;
                float f19 = this.rectSizeX;
                float f20 = i3;
                float f21 = this.rectY;
                canvas.drawRect(((f19 / 3.0f) * f20) + f18, f21 + f62, f18 + f62 + ((f19 / 3.0f) * f20), (f21 + this.rectSizeY) - f62, this.circlePaint);
                float f23 = this.rectX;
                float f24 = this.rectY;
                float f25 = this.rectSizeY;
                canvas.drawRect(f23 + f62, ((f25 / 3.0f) * f20) + f24, this.rectSizeX + (f23 - f62), f24 + ((f25 / 3.0f) * f20) + f62, this.circlePaint);
            }
        }
    }

    public PhotoCropActivity(Bundle bundle) {
        super(bundle);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        int i;
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
                i = AndroidUtilities.dp(520.0f);
            } else {
                Point point = AndroidUtilities.displaySize;
                i = Math.max(point.x, point.y);
            }
            float f = i;
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
                } else if (i != 1) {
                } else {
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
