package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RadialProgress2;
/* loaded from: classes4.dex */
public class PatternCell extends BackupImageView implements DownloadController.FileDownloadProgressListener {
    private final int SIZE;
    private int TAG;
    private MotionBackgroundDrawable backgroundDrawable;
    private Paint backgroundPaint;
    private int currentAccount;
    private int currentBackgroundColor;
    private int currentGradientAngle;
    private int currentGradientColor1;
    private int currentGradientColor2;
    private int currentGradientColor3;
    private TLRPC.TL_wallPaper currentPattern;
    private PatternCellDelegate delegate;
    private LinearGradient gradientShader;
    private int maxWallpaperSize;
    private RadialProgress2 radialProgress;
    private RectF rect;

    /* loaded from: classes4.dex */
    public interface PatternCellDelegate {
        int getBackgroundColor();

        int getBackgroundGradientAngle();

        int getBackgroundGradientColor1();

        int getBackgroundGradientColor2();

        int getBackgroundGradientColor3();

        int getCheckColor();

        float getIntensity();

        int getPatternColor();

        TLRPC.TL_wallPaper getSelectedPattern();
    }

    public PatternCell(Context context, int i, PatternCellDelegate patternCellDelegate) {
        super(context);
        this.SIZE = 100;
        this.rect = new RectF();
        this.currentAccount = UserConfig.selectedAccount;
        setRoundRadius(AndroidUtilities.dp(6.0f));
        this.maxWallpaperSize = i;
        this.delegate = patternCellDelegate;
        RadialProgress2 radialProgress2 = new RadialProgress2(this);
        this.radialProgress = radialProgress2;
        radialProgress2.setProgressRect(AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f));
        this.backgroundPaint = new Paint(3);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Cells.PatternCell.1
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), view.getMeasuredWidth() - AndroidUtilities.dp(1.0f), view.getMeasuredHeight() - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(6.0f));
                }
            });
            setClipToOutline(true);
        }
    }

    private void updateButtonState(Object obj, boolean z, boolean z2) {
        File httpFilePath;
        String name;
        boolean z3 = obj instanceof TLRPC.TL_wallPaper;
        if (z3 || (obj instanceof MediaController.SearchImage)) {
            if (z3) {
                TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj;
                name = FileLoader.getAttachFileName(tL_wallPaper.document);
                if (TextUtils.isEmpty(name)) {
                    return;
                }
                httpFilePath = FileLoader.getInstance(this.currentAccount).getPathToAttach(tL_wallPaper.document, true);
            } else {
                MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                TLRPC.Photo photo = searchImage.photo;
                if (photo != null) {
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, this.maxWallpaperSize, true);
                    File pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                    name = FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    httpFilePath = pathToAttach;
                } else {
                    httpFilePath = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                    name = httpFilePath.getName();
                }
                if (TextUtils.isEmpty(name)) {
                    return;
                }
            }
            if (!httpFilePath.exists()) {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(name, null, this);
                FileLoader.getInstance(this.currentAccount).isLoadingFile(name);
                Float fileProgress = ImageLoader.getInstance().getFileProgress(name);
                if (fileProgress != null) {
                    this.radialProgress.setProgress(fileProgress.floatValue(), z2);
                } else {
                    this.radialProgress.setProgress(0.0f, z2);
                }
                this.radialProgress.setIcon(10, z, z2);
                return;
            }
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.radialProgress.setProgress(1.0f, z2);
        }
        this.radialProgress.setIcon(6, z, z2);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.BackupImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateSelected(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.Components.BackupImageView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        ImageReceiver imageReceiver;
        MotionBackgroundDrawable motionBackgroundDrawable;
        BlendMode blendMode;
        float intensity = this.delegate.getIntensity();
        Bitmap bitmap = null;
        this.imageReceiver.setBlendMode(null);
        int backgroundColor = this.delegate.getBackgroundColor();
        int backgroundGradientColor1 = this.delegate.getBackgroundGradientColor1();
        int backgroundGradientColor2 = this.delegate.getBackgroundGradientColor2();
        int backgroundGradientColor3 = this.delegate.getBackgroundGradientColor3();
        int backgroundGradientAngle = this.delegate.getBackgroundGradientAngle();
        int checkColor = this.delegate.getCheckColor();
        if (backgroundGradientColor1 != 0) {
            if (this.gradientShader == null || backgroundColor != this.currentBackgroundColor || backgroundGradientColor1 != this.currentGradientColor1 || backgroundGradientColor2 != this.currentGradientColor2 || backgroundGradientColor3 != this.currentGradientColor3 || backgroundGradientAngle != this.currentGradientAngle) {
                this.currentBackgroundColor = backgroundColor;
                this.currentGradientColor1 = backgroundGradientColor1;
                this.currentGradientColor2 = backgroundGradientColor2;
                this.currentGradientColor3 = backgroundGradientColor3;
                this.currentGradientAngle = backgroundGradientAngle;
                if (backgroundGradientColor2 != 0) {
                    this.gradientShader = null;
                    MotionBackgroundDrawable motionBackgroundDrawable2 = this.backgroundDrawable;
                    if (motionBackgroundDrawable2 != null) {
                        motionBackgroundDrawable2.setColors(backgroundColor, backgroundGradientColor1, backgroundGradientColor2, backgroundGradientColor3, 0, false);
                    } else {
                        MotionBackgroundDrawable motionBackgroundDrawable3 = new MotionBackgroundDrawable(backgroundColor, backgroundGradientColor1, backgroundGradientColor2, backgroundGradientColor3, true);
                        this.backgroundDrawable = motionBackgroundDrawable3;
                        motionBackgroundDrawable3.setRoundRadius(AndroidUtilities.dp(6.0f));
                        this.backgroundDrawable.setParentView(this);
                    }
                    if (intensity < 0.0f) {
                        imageReceiver = this.imageReceiver;
                        bitmap = this.backgroundDrawable.getBitmap();
                        imageReceiver.setGradientBitmap(bitmap);
                    } else {
                        this.imageReceiver.setGradientBitmap(null);
                        if (Build.VERSION.SDK_INT >= 29) {
                            ImageReceiver imageReceiver2 = this.imageReceiver;
                            blendMode = BlendMode.SOFT_LIGHT;
                            imageReceiver2.setBlendMode(blendMode);
                        } else {
                            this.imageReceiver.setColorFilter(new PorterDuffColorFilter(this.delegate.getPatternColor(), PorterDuff.Mode.SRC_IN));
                        }
                    }
                } else {
                    Rect gradientPoints = BackgroundGradientDrawable.getGradientPoints(backgroundGradientAngle, getMeasuredWidth(), getMeasuredHeight());
                    this.gradientShader = new LinearGradient(gradientPoints.left, gradientPoints.top, gradientPoints.right, gradientPoints.bottom, new int[]{backgroundColor, backgroundGradientColor1}, (float[]) null, Shader.TileMode.CLAMP);
                }
            }
            motionBackgroundDrawable = this.backgroundDrawable;
            if (motionBackgroundDrawable == null) {
                motionBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
            } else {
                this.backgroundPaint.setShader(this.gradientShader);
                if (this.gradientShader == null) {
                    this.backgroundPaint.setColor(backgroundColor);
                }
                this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.backgroundPaint);
            }
            super.onDraw(canvas);
            if (this.radialProgress.getIcon() == 4) {
                this.radialProgress.setColors(checkColor, checkColor, -1, -1);
                this.radialProgress.draw(canvas);
                return;
            }
            return;
        }
        this.gradientShader = null;
        this.backgroundDrawable = null;
        imageReceiver = this.imageReceiver;
        imageReceiver.setGradientBitmap(bitmap);
        motionBackgroundDrawable = this.backgroundDrawable;
        if (motionBackgroundDrawable == null) {
        }
        super.onDraw(canvas);
        if (this.radialProgress.getIcon() == 4) {
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        TLRPC.TL_wallPaper selectedPattern = this.delegate.getSelectedPattern();
        TLRPC.TL_wallPaper tL_wallPaper = this.currentPattern;
        if (!(tL_wallPaper == null && selectedPattern == null) && (selectedPattern == null || tL_wallPaper == null || tL_wallPaper.id != selectedPattern.id)) {
            return;
        }
        if (z) {
            this.radialProgress.setIcon(4, false, true);
        } else {
            updateButtonState(tL_wallPaper, true, z);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
        TLRPC.TL_wallPaper selectedPattern = this.delegate.getSelectedPattern();
        TLRPC.TL_wallPaper tL_wallPaper = this.currentPattern;
        if ((!(tL_wallPaper == null && selectedPattern == null) && (selectedPattern == null || tL_wallPaper == null || tL_wallPaper.id != selectedPattern.id)) || this.radialProgress.getIcon() == 10) {
            return;
        }
        updateButtonState(this.currentPattern, false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        this.radialProgress.setProgress(1.0f, true);
        TLRPC.TL_wallPaper selectedPattern = this.delegate.getSelectedPattern();
        TLRPC.TL_wallPaper tL_wallPaper = this.currentPattern;
        if (!(tL_wallPaper == null && selectedPattern == null) && (selectedPattern == null || tL_wallPaper == null || tL_wallPaper.id != selectedPattern.id)) {
            return;
        }
        updateButtonState(tL_wallPaper, false, true);
    }

    public void setPattern(TLRPC.TL_wallPaper tL_wallPaper) {
        this.currentPattern = tL_wallPaper;
        if (tL_wallPaper != null) {
            setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tL_wallPaper.document.thumbs, AndroidUtilities.dp(100.0f)), tL_wallPaper.document), "100_100", null, null, "png", 0L, 1, tL_wallPaper);
        } else {
            setImageDrawable(null);
        }
        updateSelected(false);
    }

    public void updateSelected(boolean z) {
        TLRPC.TL_wallPaper selectedPattern = this.delegate.getSelectedPattern();
        TLRPC.TL_wallPaper tL_wallPaper = this.currentPattern;
        if (!(tL_wallPaper == null && selectedPattern == null) && (selectedPattern == null || tL_wallPaper == null || tL_wallPaper.id != selectedPattern.id)) {
            this.radialProgress.setIcon(4, false, z);
        } else {
            updateButtonState(selectedPattern, false, z);
        }
        invalidate();
    }
}
