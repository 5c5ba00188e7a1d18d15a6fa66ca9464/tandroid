package org.telegram.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.PhotoCropActivity;
/* loaded from: classes4.dex */
public class PhotoCropActivity extends BaseFragment {
    private static final int done_button = 1;
    private String bitmapKey;
    private BitmapDrawable drawable;
    private Bitmap imageToCrop;
    private PhotoCropView view;
    private PhotoEditActivityDelegate delegate = null;
    private boolean sameBitmap = false;
    private boolean doneButtonPressed = false;

    /* loaded from: classes4.dex */
    public interface PhotoEditActivityDelegate {
        void didFinishEdit(Bitmap bitmap);
    }

    /* loaded from: classes4.dex */
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
            setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PhotoCropActivity$PhotoCropView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return PhotoCropActivity.PhotoCropView.this.m4179lambda$init$0$orgtelegramuiPhotoCropActivity$PhotoCropView(view, motionEvent);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:52:0x00c8  */
        /* renamed from: lambda$init$0$org-telegram-ui-PhotoCropActivity$PhotoCropView */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ boolean m4179lambda$init$0$orgtelegramuiPhotoCropActivity$PhotoCropView(View view, MotionEvent motionEvent) {
            int i;
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int cornerSide = AndroidUtilities.dp(14.0f);
            if (motionEvent.getAction() == 0) {
                float f = this.rectX;
                if (f - cornerSide < x && cornerSide + f > x) {
                    float f2 = this.rectY;
                    if (f2 - cornerSide < y && f2 + cornerSide > y) {
                        this.draggingState = 1;
                        if (this.draggingState != 0) {
                            requestDisallowInterceptTouchEvent(true);
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                float f3 = this.rectSizeX;
                if ((f - cornerSide) + f3 < x && cornerSide + f + f3 > x) {
                    float f4 = this.rectY;
                    if (f4 - cornerSide < y && f4 + cornerSide > y) {
                        this.draggingState = 2;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f - cornerSide < x && cornerSide + f > x) {
                    float f5 = this.rectY;
                    float f6 = this.rectSizeY;
                    if ((f5 - cornerSide) + f6 < y && f5 + cornerSide + f6 > y) {
                        this.draggingState = 3;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if ((f - cornerSide) + f3 < x && cornerSide + f + f3 > x) {
                    float f7 = this.rectY;
                    float f8 = this.rectSizeY;
                    if ((f7 - cornerSide) + f8 < y && f7 + cornerSide + f8 > y) {
                        this.draggingState = 4;
                        if (this.draggingState != 0) {
                        }
                        this.oldX = x;
                        this.oldY = y;
                    }
                }
                if (f < x && f + f3 > x) {
                    float f9 = this.rectY;
                    if (f9 < y && f9 + this.rectSizeY > y) {
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
                float diffX = x - this.oldX;
                float diffY = y - this.oldY;
                if (i == 5) {
                    float f10 = this.rectX + diffX;
                    this.rectX = f10;
                    float f11 = this.rectY + diffY;
                    this.rectY = f11;
                    int i2 = this.bitmapX;
                    if (f10 < i2) {
                        this.rectX = i2;
                    } else {
                        float f12 = this.rectSizeX;
                        int i3 = this.bitmapWidth;
                        if (f10 + f12 > i2 + i3) {
                            this.rectX = (i2 + i3) - f12;
                        }
                    }
                    int i4 = this.bitmapY;
                    if (f11 < i4) {
                        this.rectY = i4;
                    } else {
                        float f13 = this.rectSizeY;
                        int i5 = this.bitmapHeight;
                        if (f11 + f13 > i4 + i5) {
                            this.rectY = (i4 + i5) - f13;
                        }
                    }
                } else if (i == 1) {
                    float f14 = this.rectSizeX;
                    if (f14 - diffX < 160.0f) {
                        diffX = f14 - 160.0f;
                    }
                    float f15 = this.rectX;
                    int i6 = this.bitmapX;
                    if (f15 + diffX < i6) {
                        diffX = i6 - f15;
                    }
                    if (!this.freeform) {
                        float f16 = this.rectY;
                        int i7 = this.bitmapY;
                        if (f16 + diffX < i7) {
                            diffX = i7 - f16;
                        }
                        this.rectX = f15 + diffX;
                        this.rectY = f16 + diffX;
                        this.rectSizeX = f14 - diffX;
                        this.rectSizeY -= diffX;
                    } else {
                        float f17 = this.rectSizeY;
                        if (f17 - diffY < 160.0f) {
                            diffY = f17 - 160.0f;
                        }
                        float f18 = this.rectY;
                        int i8 = this.bitmapY;
                        if (f18 + diffY < i8) {
                            diffY = i8 - f18;
                        }
                        this.rectX = f15 + diffX;
                        this.rectY = f18 + diffY;
                        this.rectSizeX = f14 - diffX;
                        this.rectSizeY = f17 - diffY;
                    }
                } else if (i == 2) {
                    float f19 = this.rectSizeX;
                    if (f19 + diffX < 160.0f) {
                        diffX = -(f19 - 160.0f);
                    }
                    float f20 = this.rectX;
                    int i9 = this.bitmapX;
                    int i10 = this.bitmapWidth;
                    if (f20 + f19 + diffX > i9 + i10) {
                        diffX = ((i9 + i10) - f20) - f19;
                    }
                    if (!this.freeform) {
                        float f21 = this.rectY;
                        int i11 = this.bitmapY;
                        if (f21 - diffX < i11) {
                            diffX = f21 - i11;
                        }
                        this.rectY = f21 - diffX;
                        this.rectSizeX = f19 + diffX;
                        this.rectSizeY += diffX;
                    } else {
                        float f22 = this.rectSizeY;
                        if (f22 - diffY < 160.0f) {
                            diffY = f22 - 160.0f;
                        }
                        float f23 = this.rectY;
                        int i12 = this.bitmapY;
                        if (f23 + diffY < i12) {
                            diffY = i12 - f23;
                        }
                        this.rectY = f23 + diffY;
                        this.rectSizeX = f19 + diffX;
                        this.rectSizeY = f22 - diffY;
                    }
                } else if (i == 3) {
                    float f24 = this.rectSizeX;
                    if (f24 - diffX < 160.0f) {
                        diffX = f24 - 160.0f;
                    }
                    float f25 = this.rectX;
                    int i13 = this.bitmapX;
                    if (f25 + diffX < i13) {
                        diffX = i13 - f25;
                    }
                    if (!this.freeform) {
                        float f26 = this.rectY;
                        int i14 = this.bitmapY;
                        int i15 = this.bitmapHeight;
                        if ((f26 + f24) - diffX > i14 + i15) {
                            diffX = ((f26 + f24) - i14) - i15;
                        }
                        this.rectX = f25 + diffX;
                        this.rectSizeX = f24 - diffX;
                        this.rectSizeY -= diffX;
                    } else {
                        float f27 = this.rectY;
                        float f28 = this.rectSizeY;
                        int i16 = this.bitmapY;
                        int i17 = this.bitmapHeight;
                        if (f27 + f28 + diffY > i16 + i17) {
                            diffY = ((i16 + i17) - f27) - f28;
                        }
                        this.rectX = f25 + diffX;
                        this.rectSizeX = f24 - diffX;
                        float f29 = f28 + diffY;
                        this.rectSizeY = f29;
                        if (f29 < 160.0f) {
                            this.rectSizeY = 160.0f;
                        }
                    }
                } else if (i == 4) {
                    float f30 = this.rectX;
                    float f31 = this.rectSizeX;
                    int i18 = this.bitmapX;
                    int i19 = this.bitmapWidth;
                    if (f30 + f31 + diffX > i18 + i19) {
                        diffX = ((i18 + i19) - f30) - f31;
                    }
                    if (!this.freeform) {
                        float f32 = this.rectY;
                        int i20 = this.bitmapY;
                        int i21 = this.bitmapHeight;
                        if (f32 + f31 + diffX > i20 + i21) {
                            diffX = ((i20 + i21) - f32) - f31;
                        }
                        this.rectSizeX = f31 + diffX;
                        this.rectSizeY += diffX;
                    } else {
                        float f33 = this.rectY;
                        float f34 = this.rectSizeY;
                        int i22 = this.bitmapY;
                        int i23 = this.bitmapHeight;
                        if (f33 + f34 + diffY > i22 + i23) {
                            diffY = ((i22 + i23) - f33) - f34;
                        }
                        this.rectSizeX = f31 + diffX;
                        this.rectSizeY = f34 + diffY;
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
            int i;
            int i2;
            if (this.viewWidth == 0 || this.viewHeight == 0 || PhotoCropActivity.this.imageToCrop == null) {
                return;
            }
            float f = this.rectX - this.bitmapX;
            int i3 = this.bitmapWidth;
            float percX = f / i3;
            float f2 = this.rectY - this.bitmapY;
            int i4 = this.bitmapHeight;
            float percY = f2 / i4;
            float percSizeX = this.rectSizeX / i3;
            float percSizeY = this.rectSizeY / i4;
            float w = PhotoCropActivity.this.imageToCrop.getWidth();
            float h = PhotoCropActivity.this.imageToCrop.getHeight();
            int i5 = this.viewWidth;
            float scaleX = i5 / w;
            int i6 = this.viewHeight;
            float scaleY = i6 / h;
            if (scaleX > scaleY) {
                this.bitmapHeight = i6;
                this.bitmapWidth = (int) Math.ceil(w * scaleY);
            } else {
                this.bitmapWidth = i5;
                this.bitmapHeight = (int) Math.ceil(h * scaleX);
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
                this.rectX = (i9 * percX) + this.bitmapX;
                int i10 = this.bitmapHeight;
                this.rectY = (i10 * percY) + dp;
                this.rectSizeX = i9 * percSizeX;
                this.rectSizeY = i10 * percSizeY;
            }
            invalidate();
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            this.viewWidth = (right - left) - AndroidUtilities.dp(28.0f);
            this.viewHeight = (bottom - top) - AndroidUtilities.dp(28.0f);
            updateBitmapSize();
        }

        public Bitmap getBitmap() {
            float f = this.rectX - this.bitmapX;
            int i = this.bitmapWidth;
            float percX = f / i;
            float percY = (this.rectY - this.bitmapY) / this.bitmapHeight;
            float percSizeX = this.rectSizeX / i;
            float percSizeY = this.rectSizeY / i;
            int x = (int) (PhotoCropActivity.this.imageToCrop.getWidth() * percX);
            int y = (int) (PhotoCropActivity.this.imageToCrop.getHeight() * percY);
            int sizeX = (int) (PhotoCropActivity.this.imageToCrop.getWidth() * percSizeX);
            int sizeY = (int) (PhotoCropActivity.this.imageToCrop.getWidth() * percSizeY);
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x + sizeX > PhotoCropActivity.this.imageToCrop.getWidth()) {
                sizeX = PhotoCropActivity.this.imageToCrop.getWidth() - x;
            }
            if (y + sizeY > PhotoCropActivity.this.imageToCrop.getHeight()) {
                sizeY = PhotoCropActivity.this.imageToCrop.getHeight() - y;
            }
            try {
                return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, x, y, sizeX, sizeY);
            } catch (Throwable e) {
                FileLog.e(e);
                System.gc();
                try {
                    return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, x, y, sizeX, sizeY);
                } catch (Throwable e2) {
                    FileLog.e(e2);
                    return null;
                }
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (PhotoCropActivity.this.drawable != null) {
                try {
                    BitmapDrawable bitmapDrawable = PhotoCropActivity.this.drawable;
                    int i = this.bitmapX;
                    int i2 = this.bitmapY;
                    bitmapDrawable.setBounds(i, i2, this.bitmapWidth + i, this.bitmapHeight + i2);
                    PhotoCropActivity.this.drawable.draw(canvas);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
            int i3 = this.bitmapX;
            canvas.drawRect(i3, this.bitmapY, i3 + this.bitmapWidth, this.rectY, this.halfPaint);
            float f = this.rectY;
            canvas.drawRect(this.bitmapX, f, this.rectX, f + this.rectSizeY, this.halfPaint);
            float f2 = this.rectY;
            canvas.drawRect(this.rectX + this.rectSizeX, f2, this.bitmapX + this.bitmapWidth, f2 + this.rectSizeY, this.halfPaint);
            int i4 = this.bitmapX;
            canvas.drawRect(i4, this.rectSizeY + this.rectY, i4 + this.bitmapWidth, this.bitmapY + this.bitmapHeight, this.halfPaint);
            float f3 = this.rectX;
            float f4 = this.rectY;
            canvas.drawRect(f3, f4, f3 + this.rectSizeX, f4 + this.rectSizeY, this.rectPaint);
            int side = AndroidUtilities.dp(1.0f);
            float f5 = this.rectX;
            canvas.drawRect(f5 + side, this.rectY + side, f5 + side + AndroidUtilities.dp(20.0f), this.rectY + (side * 3), this.circlePaint);
            float f6 = this.rectX;
            float f7 = this.rectY;
            canvas.drawRect(f6 + side, f7 + side, f6 + (side * 3), f7 + side + AndroidUtilities.dp(20.0f), this.circlePaint);
            float dp = ((this.rectX + this.rectSizeX) - side) - AndroidUtilities.dp(20.0f);
            float f8 = this.rectY;
            canvas.drawRect(dp, f8 + side, (this.rectX + this.rectSizeX) - side, f8 + (side * 3), this.circlePaint);
            float f9 = this.rectX;
            float f10 = this.rectSizeX;
            float f11 = this.rectY;
            canvas.drawRect((f9 + f10) - (side * 3), f11 + side, (f9 + f10) - side, f11 + side + AndroidUtilities.dp(20.0f), this.circlePaint);
            canvas.drawRect(this.rectX + side, ((this.rectY + this.rectSizeY) - side) - AndroidUtilities.dp(20.0f), this.rectX + (side * 3), (this.rectY + this.rectSizeY) - side, this.circlePaint);
            float f12 = this.rectX;
            canvas.drawRect(f12 + side, (this.rectY + this.rectSizeY) - (side * 3), f12 + side + AndroidUtilities.dp(20.0f), (this.rectY + this.rectSizeY) - side, this.circlePaint);
            float dp2 = ((this.rectX + this.rectSizeX) - side) - AndroidUtilities.dp(20.0f);
            float f13 = this.rectY;
            float f14 = this.rectSizeY;
            canvas.drawRect(dp2, (f13 + f14) - (side * 3), (this.rectX + this.rectSizeX) - side, (f13 + f14) - side, this.circlePaint);
            canvas.drawRect((this.rectX + this.rectSizeX) - (side * 3), ((this.rectY + this.rectSizeY) - side) - AndroidUtilities.dp(20.0f), (this.rectX + this.rectSizeX) - side, (this.rectY + this.rectSizeY) - side, this.circlePaint);
            for (int a = 1; a < 3; a++) {
                float f15 = this.rectX;
                float f16 = this.rectSizeX;
                float f17 = this.rectY;
                canvas.drawRect(f15 + ((f16 / 3.0f) * a), f17 + side, f15 + side + ((f16 / 3.0f) * a), (f17 + this.rectSizeY) - side, this.circlePaint);
                float f18 = this.rectX;
                float f19 = this.rectY;
                float f20 = this.rectSizeY;
                canvas.drawRect(f18 + side, ((f20 / 3.0f) * a) + f19, this.rectSizeX + (f18 - side), f19 + ((f20 / 3.0f) * a) + side, this.circlePaint);
            }
        }
    }

    public PhotoCropActivity(Bundle args) {
        super(args);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        int size;
        if (this.imageToCrop == null) {
            String photoPath = getArguments().getString("photoPath");
            Uri photoUri = (Uri) getArguments().getParcelable("photoUri");
            if (photoPath == null && photoUri == null) {
                return false;
            }
            if (photoPath != null) {
                File f = new File(photoPath);
                if (!f.exists()) {
                    return false;
                }
            }
            if (AndroidUtilities.isTablet()) {
                size = AndroidUtilities.dp(520.0f);
            } else {
                size = Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
            }
            Bitmap loadBitmap = ImageLoader.loadBitmap(photoPath, photoUri, size, size, true);
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
        this.actionBar.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        this.actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, false);
        this.actionBar.setTitleColor(-1);
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("CropImage", R.string.CropImage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PhotoCropActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int id) {
                if (id == -1) {
                    PhotoCropActivity.this.finishFragment();
                } else if (id == 1) {
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
        ActionBarMenu menu = this.actionBar.createMenu();
        menu.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", R.string.Done));
        PhotoCropView photoCropView = new PhotoCropView(context);
        this.view = photoCropView;
        this.fragmentView = photoCropView;
        ((PhotoCropView) this.fragmentView).freeform = getArguments().getBoolean("freeform", false);
        this.fragmentView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        return this.fragmentView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent event) {
        return false;
    }

    public void setDelegate(PhotoEditActivityDelegate delegate) {
        this.delegate = delegate;
    }
}
