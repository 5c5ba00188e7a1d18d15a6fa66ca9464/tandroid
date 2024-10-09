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
        /* JADX WARN: Code restructure failed: missing block: B:137:0x022c, code lost:
        
            if (r6 < 160.0f) goto L156;
         */
        /* JADX WARN: Code restructure failed: missing block: B:138:0x028c, code lost:
        
            r13.rectSizeY = 160.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x028a, code lost:
        
            if (r13.rectSizeY < 160.0f) goto L156;
         */
        /* JADX WARN: Removed duplicated region for block: B:14:0x00b5  */
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
                        float f21 = f17 + f20;
                        float f22 = i2 + this.bitmapWidth;
                        if (f21 > f22) {
                            this.rectX = f22 - f20;
                        }
                    }
                    int i3 = this.bitmapY;
                    float f23 = i3;
                    if (f18 < f23) {
                        this.rectY = f23;
                    } else {
                        float f24 = this.rectSizeY;
                        float f25 = f18 + f24;
                        float f26 = i3 + this.bitmapHeight;
                        if (f25 > f26) {
                            this.rectY = f26 - f24;
                        }
                    }
                } else if (i == 1) {
                    f = this.rectSizeX;
                    if (f - f15 < 160.0f) {
                        f15 = f - 160.0f;
                    }
                    float f27 = this.rectX;
                    float f28 = f27 + f15;
                    float f29 = this.bitmapX;
                    if (f28 < f29) {
                        f15 = f29 - f27;
                    }
                    if (this.freeform) {
                        float f30 = this.rectSizeY;
                        if (f30 - f16 < 160.0f) {
                            f16 = f30 - 160.0f;
                        }
                        float f31 = this.rectY;
                        float f32 = f31 + f16;
                        float f33 = this.bitmapY;
                        if (f32 < f33) {
                            f16 = f33 - f31;
                        }
                        this.rectX = f27 + f15;
                        this.rectY = f31 + f16;
                        this.rectSizeX = f - f15;
                        this.rectSizeY = f30 - f16;
                    } else {
                        float f34 = this.rectY;
                        float f35 = f34 + f15;
                        float f36 = this.bitmapY;
                        if (f35 < f36) {
                            f15 = f36 - f34;
                        }
                        this.rectX = f27 + f15;
                        this.rectY = f34 + f15;
                        this.rectSizeX = f - f15;
                        f2 = this.rectSizeY - f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 2) {
                    float f37 = this.rectSizeX;
                    if (f37 + f15 < 160.0f) {
                        f15 = -(f37 - 160.0f);
                    }
                    float f38 = this.rectX;
                    float f39 = f38 + f37 + f15;
                    float f40 = this.bitmapX + this.bitmapWidth;
                    if (f39 > f40) {
                        f15 = (f40 - f38) - f37;
                    }
                    if (this.freeform) {
                        float f41 = this.rectSizeY;
                        if (f41 - f16 < 160.0f) {
                            f16 = f41 - 160.0f;
                        }
                        float f42 = this.rectY;
                        float f43 = f42 + f16;
                        float f44 = this.bitmapY;
                        if (f43 < f44) {
                            f16 = f44 - f42;
                        }
                        this.rectY = f42 + f16;
                        this.rectSizeX = f37 + f15;
                        this.rectSizeY = f41 - f16;
                    } else {
                        float f45 = this.rectY;
                        float f46 = f45 - f15;
                        float f47 = this.bitmapY;
                        if (f46 < f47) {
                            f15 = f45 - f47;
                        }
                        this.rectY = f45 - f15;
                        this.rectSizeX = f37 + f15;
                        f2 = this.rectSizeY + f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 3) {
                    f = this.rectSizeX;
                    if (f - f15 < 160.0f) {
                        f15 = f - 160.0f;
                    }
                    float f48 = this.rectX;
                    float f49 = f48 + f15;
                    float f50 = this.bitmapX;
                    if (f49 < f50) {
                        f15 = f50 - f48;
                    }
                    if (this.freeform) {
                        float f51 = this.rectY;
                        float f52 = this.rectSizeY;
                        float f53 = f51 + f52 + f16;
                        float f54 = this.bitmapY + this.bitmapHeight;
                        if (f53 > f54) {
                            f16 = (f54 - f51) - f52;
                        }
                        this.rectX = f48 + f15;
                        this.rectSizeX = f - f15;
                        float f55 = f52 + f16;
                        this.rectSizeY = f55;
                    } else {
                        float f56 = this.rectY + f;
                        float f57 = f56 - f15;
                        int i4 = this.bitmapY;
                        int i5 = this.bitmapHeight;
                        if (f57 > i4 + i5) {
                            f15 = (f56 - i4) - i5;
                        }
                        this.rectX = f48 + f15;
                        this.rectSizeX = f - f15;
                        f2 = this.rectSizeY - f15;
                        this.rectSizeY = f2;
                    }
                } else if (i == 4) {
                    float f58 = this.rectX;
                    float f59 = this.rectSizeX;
                    float f60 = f58 + f59 + f15;
                    float f61 = this.bitmapX + this.bitmapWidth;
                    if (f60 > f61) {
                        f15 = (f61 - f58) - f59;
                    }
                    if (this.freeform) {
                        float f62 = this.rectY;
                        float f63 = this.rectSizeY;
                        float f64 = f62 + f63 + f16;
                        float f65 = this.bitmapY + this.bitmapHeight;
                        if (f64 > f65) {
                            f16 = (f65 - f62) - f63;
                        }
                        this.rectSizeX = f59 + f15;
                        this.rectSizeY = f63 + f16;
                    } else {
                        float f66 = this.rectY;
                        float f67 = f66 + f59 + f15;
                        float f68 = this.bitmapY + this.bitmapHeight;
                        if (f67 > f68) {
                            f15 = (f68 - f66) - f59;
                        }
                        this.rectSizeX = f59 + f15;
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
            int i;
            float f;
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
            int i2 = this.viewWidth;
            float f10 = i2 / width;
            int i3 = this.viewHeight;
            if (f10 > i3 / height) {
                this.bitmapHeight = i3;
                this.bitmapWidth = (int) Math.ceil(width * r9);
            } else {
                this.bitmapWidth = i2;
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
                    f = this.bitmapHeight;
                } else {
                    if (this.bitmapWidth > this.bitmapHeight) {
                        this.rectY = dp;
                        this.rectX = ((this.viewWidth - r1) / 2) + AndroidUtilities.dp(14.0f);
                        i = this.bitmapHeight;
                    } else {
                        this.rectX = this.bitmapX;
                        this.rectY = ((this.viewHeight - r0) / 2) + AndroidUtilities.dp(14.0f);
                        i = this.bitmapWidth;
                    }
                    f = i;
                    this.rectSizeX = f;
                }
                this.rectSizeY = f;
            } else {
                float f11 = this.bitmapWidth;
                this.rectX = (f4 * f11) + this.bitmapX;
                float f12 = this.bitmapHeight;
                this.rectY = (f7 * f12) + dp;
                this.rectSizeX = f8 * f11;
                this.rectSizeY = f9 * f12;
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

        /* JADX WARN: Removed duplicated region for block: B:6:0x01a3 A[LOOP:0: B:5:0x01a1->B:6:0x01a3, LOOP_END] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            if (PhotoCropActivity.this.drawable != null) {
                try {
                    BitmapDrawable bitmapDrawable = PhotoCropActivity.this.drawable;
                    int i2 = this.bitmapX;
                    int i3 = this.bitmapY;
                    bitmapDrawable.setBounds(i2, i3, this.bitmapWidth + i2, this.bitmapHeight + i3);
                    try {
                        PhotoCropActivity.this.drawable.draw(canvas);
                    } catch (Throwable th) {
                        th = th;
                        FileLog.e(th);
                        canvas.drawRect(this.bitmapX, this.bitmapY, r0 + this.bitmapWidth, this.rectY, this.halfPaint);
                        float f = this.bitmapX;
                        float f2 = this.rectY;
                        canvas.drawRect(f, f2, this.rectX, f2 + this.rectSizeY, this.halfPaint);
                        float f3 = this.rectX + this.rectSizeX;
                        float f4 = this.rectY;
                        canvas.drawRect(f3, f4, this.bitmapX + this.bitmapWidth, f4 + this.rectSizeY, this.halfPaint);
                        canvas.drawRect(this.bitmapX, this.rectSizeY + this.rectY, r0 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
                        float f5 = this.rectX;
                        float f6 = this.rectY;
                        canvas.drawRect(f5, f6, f5 + this.rectSizeX, f6 + this.rectSizeY, this.rectPaint);
                        int dp = AndroidUtilities.dp(1.0f);
                        float f7 = dp;
                        float f8 = this.rectX + f7;
                        float f9 = dp * 3;
                        canvas.drawRect(f8, this.rectY + f7, f8 + AndroidUtilities.dp(20.0f), this.rectY + f9, this.circlePaint);
                        float f10 = this.rectX;
                        float f11 = this.rectY + f7;
                        canvas.drawRect(f10 + f7, f11, f10 + f9, f11 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        float dp2 = ((this.rectX + this.rectSizeX) - f7) - AndroidUtilities.dp(20.0f);
                        float f12 = this.rectY;
                        canvas.drawRect(dp2, f12 + f7, (this.rectX + this.rectSizeX) - f7, f12 + f9, this.circlePaint);
                        float f13 = this.rectX + this.rectSizeX;
                        float f14 = this.rectY + f7;
                        canvas.drawRect(f13 - f9, f14, f13 - f7, f14 + AndroidUtilities.dp(20.0f), this.circlePaint);
                        canvas.drawRect(this.rectX + f7, ((this.rectY + this.rectSizeY) - f7) - AndroidUtilities.dp(20.0f), this.rectX + f9, (this.rectY + this.rectSizeY) - f7, this.circlePaint);
                        float f15 = this.rectX + f7;
                        canvas.drawRect(f15, (this.rectY + this.rectSizeY) - f9, f15 + AndroidUtilities.dp(20.0f), (this.rectY + this.rectSizeY) - f7, this.circlePaint);
                        float dp3 = ((this.rectX + this.rectSizeX) - f7) - AndroidUtilities.dp(20.0f);
                        float f16 = this.rectY + this.rectSizeY;
                        canvas.drawRect(dp3, f16 - f9, (this.rectX + this.rectSizeX) - f7, f16 - f7, this.circlePaint);
                        canvas.drawRect((this.rectX + this.rectSizeX) - f9, ((this.rectY + this.rectSizeY) - f7) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f7, (this.rectY + this.rectSizeY) - f7, this.circlePaint);
                        while (i < 3) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            canvas.drawRect(this.bitmapX, this.bitmapY, r0 + this.bitmapWidth, this.rectY, this.halfPaint);
            float f17 = this.bitmapX;
            float f22 = this.rectY;
            canvas.drawRect(f17, f22, this.rectX, f22 + this.rectSizeY, this.halfPaint);
            float f32 = this.rectX + this.rectSizeX;
            float f42 = this.rectY;
            canvas.drawRect(f32, f42, this.bitmapX + this.bitmapWidth, f42 + this.rectSizeY, this.halfPaint);
            canvas.drawRect(this.bitmapX, this.rectSizeY + this.rectY, r0 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
            float f52 = this.rectX;
            float f62 = this.rectY;
            canvas.drawRect(f52, f62, f52 + this.rectSizeX, f62 + this.rectSizeY, this.rectPaint);
            int dp4 = AndroidUtilities.dp(1.0f);
            float f72 = dp4;
            float f82 = this.rectX + f72;
            float f92 = dp4 * 3;
            canvas.drawRect(f82, this.rectY + f72, f82 + AndroidUtilities.dp(20.0f), this.rectY + f92, this.circlePaint);
            float f102 = this.rectX;
            float f112 = this.rectY + f72;
            canvas.drawRect(f102 + f72, f112, f102 + f92, f112 + AndroidUtilities.dp(20.0f), this.circlePaint);
            float dp22 = ((this.rectX + this.rectSizeX) - f72) - AndroidUtilities.dp(20.0f);
            float f122 = this.rectY;
            canvas.drawRect(dp22, f122 + f72, (this.rectX + this.rectSizeX) - f72, f122 + f92, this.circlePaint);
            float f132 = this.rectX + this.rectSizeX;
            float f142 = this.rectY + f72;
            canvas.drawRect(f132 - f92, f142, f132 - f72, f142 + AndroidUtilities.dp(20.0f), this.circlePaint);
            canvas.drawRect(this.rectX + f72, ((this.rectY + this.rectSizeY) - f72) - AndroidUtilities.dp(20.0f), this.rectX + f92, (this.rectY + this.rectSizeY) - f72, this.circlePaint);
            float f152 = this.rectX + f72;
            canvas.drawRect(f152, (this.rectY + this.rectSizeY) - f92, f152 + AndroidUtilities.dp(20.0f), (this.rectY + this.rectSizeY) - f72, this.circlePaint);
            float dp32 = ((this.rectX + this.rectSizeX) - f72) - AndroidUtilities.dp(20.0f);
            float f162 = this.rectY + this.rectSizeY;
            canvas.drawRect(dp32, f162 - f92, (this.rectX + this.rectSizeX) - f72, f162 - f72, this.circlePaint);
            canvas.drawRect((this.rectX + this.rectSizeX) - f92, ((this.rectY + this.rectSizeY) - f72) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - f72, (this.rectY + this.rectSizeY) - f72, this.circlePaint);
            for (i = 1; i < 3; i++) {
                float f18 = this.rectX;
                float f19 = i;
                float f20 = (this.rectSizeX / 3.0f) * f19;
                float f21 = this.rectY;
                canvas.drawRect(f18 + f20, f21 + f72, f18 + f72 + f20, (f21 + this.rectSizeY) - f72, this.circlePaint);
                float f23 = this.rectX;
                float f24 = this.rectY + ((this.rectSizeY / 3.0f) * f19);
                canvas.drawRect(f23 + f72, f24, this.rectSizeX + (f23 - f72), f24 + f72, this.circlePaint);
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
                PhotoCropActivity.this.lambda$onBackPressed$300();
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
