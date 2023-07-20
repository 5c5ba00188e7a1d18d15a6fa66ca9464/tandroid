package org.telegram.ui.Stories;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StoryItem;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_stories_getUserStories;
import org.telegram.tgnet.TLRPC$TL_stories_userStories;
import org.telegram.tgnet.TLRPC$TL_storyViews;
import org.telegram.tgnet.TLRPC$TL_userStories;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.GradientTools;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesUtilities;
/* loaded from: classes4.dex */
public class StoriesUtilities {
    public static GradientTools closeFriendsGradientTools;
    public static Drawable expiredStoryDrawable;
    public static int grayLastColor;
    public static Paint grayPaint;
    public static int storyCellGrayLastColor;
    public static GradientTools[] storiesGradientTools = new GradientTools[2];
    public static Paint[] storyCellGreyPaint = new Paint[2];
    private static final RectF rectTmp = new RectF();
    static int debugState = 0;
    static Runnable debugRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities.1
        @Override // java.lang.Runnable
        public void run() {
            int abs = Math.abs(Utilities.random.nextInt() % 3);
            StoriesUtilities.debugState = abs;
            if (abs == 2) {
                StoriesUtilities.debugState = 1;
            } else {
                StoriesUtilities.debugState = 2;
            }
            NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, 0);
            AndroidUtilities.runOnUIThread(StoriesUtilities.debugRunnable, 1000L);
            LaunchActivity.getLastFragment().getFragmentView();
        }
    };

    public static void drawAvatarWithStory(long j, Canvas canvas, ImageReceiver imageReceiver, AvatarStoryParams avatarStoryParams) {
        drawAvatarWithStory(j, canvas, imageReceiver, UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId() != j && MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController().hasStories(j), avatarStoryParams);
    }

    /* JADX WARN: Removed duplicated region for block: B:208:0x03f0  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0402  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0412  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0435  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0461  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:236:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void drawAvatarWithStory(long j, Canvas canvas, ImageReceiver imageReceiver, boolean z, AvatarStoryParams avatarStoryParams) {
        int predictiveUnreadState;
        boolean z2;
        int i;
        int i2;
        int i3;
        float f;
        Paint paint;
        float dp;
        Paint paint2;
        Paint paint3;
        Paint paint4;
        float dpf2;
        float f2;
        float dp2;
        int size;
        Paint paint5;
        float f3;
        Paint paint6;
        float f4;
        float f5;
        RectF rectF;
        Paint paint7;
        GradientTools gradientTools;
        float dp3;
        StoriesController storiesController = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController();
        boolean z3 = avatarStoryParams.animate;
        if (avatarStoryParams.dialogId != j) {
            avatarStoryParams.dialogId = j;
            avatarStoryParams.reset();
            z3 = false;
        }
        boolean isLoading = storiesController.isLoading(j);
        boolean hasHiddenStories = avatarStoryParams.drawHiddenStoriesAsSegments ? storiesController.hasHiddenStories() : z;
        if (avatarStoryParams.storyItem != null) {
            storiesController.getUnreadState(j, avatarStoryParams.storyId);
            isLoading = false;
        }
        int i4 = 2;
        if (isLoading) {
            i = storiesController.hasStories(j) ? 2 : getPredictiveUnreadState(storiesController, j);
            z2 = false;
            predictiveUnreadState = 3;
        } else if (hasHiddenStories) {
            if (avatarStoryParams.drawSegments) {
                z2 = z3;
                i = 2;
                predictiveUnreadState = 2;
            } else {
                int unreadState = storiesController.getUnreadState(j, avatarStoryParams.storyId);
                predictiveUnreadState = unreadState == 0 ? 2 : 1;
                z2 = z3;
                i = unreadState;
            }
        } else {
            predictiveUnreadState = getPredictiveUnreadState(storiesController, j);
            z2 = z3;
            i = predictiveUnreadState;
        }
        int i5 = avatarStoryParams.currentState;
        if (i5 != predictiveUnreadState) {
            if (i5 == 3) {
                z2 = true;
            }
            if (predictiveUnreadState == 3) {
                avatarStoryParams.animateFromUnreadState = i;
            }
            if (z2) {
                avatarStoryParams.prevState = i5;
                avatarStoryParams.currentState = predictiveUnreadState;
                avatarStoryParams.progressToSate = 0.0f;
            } else {
                avatarStoryParams.currentState = predictiveUnreadState;
                avatarStoryParams.progressToSate = 1.0f;
            }
        }
        avatarStoryParams.unreadState = i;
        ButtonBounce buttonBounce = avatarStoryParams.buttonBounce;
        float scale = buttonBounce != null ? buttonBounce.getScale(0.08f) : 1.0f;
        if (avatarStoryParams.showProgress != isLoading && isLoading) {
            avatarStoryParams.alphas = new float[20];
            for (int i6 = 0; i6 < 20; i6++) {
                avatarStoryParams.alphas[i6] = 1.0f;
            }
            avatarStoryParams.sweepAngle = 180.0f;
        }
        avatarStoryParams.showProgress = isLoading;
        if (avatarStoryParams.currentState == 0 && avatarStoryParams.progressToSate == 1.0f) {
            imageReceiver.setImageCoords(avatarStoryParams.originalAvatarRect);
            imageReceiver.draw(canvas);
            return;
        }
        if (scale != 1.0f) {
            int save = canvas.save();
            canvas.scale(scale, scale, avatarStoryParams.originalAvatarRect.centerX(), avatarStoryParams.originalAvatarRect.centerY());
            i2 = save;
        } else {
            i2 = 0;
        }
        float f6 = avatarStoryParams.progressToSate;
        if (f6 != 1.0f) {
            f6 = CubicBezierInterpolator.DEFAULT.getInterpolation(f6);
        }
        float f7 = f6;
        float lerp = avatarStoryParams.isStoryCell ? 0.0f : AndroidUtilities.lerp(getInset(avatarStoryParams.prevState, avatarStoryParams.animateFromUnreadState), getInset(avatarStoryParams.currentState, avatarStoryParams.animateFromUnreadState), avatarStoryParams.progressToSate);
        if (lerp == 0.0f) {
            imageReceiver.setImageCoords(avatarStoryParams.originalAvatarRect);
        } else {
            RectF rectF2 = rectTmp;
            rectF2.set(avatarStoryParams.originalAvatarRect);
            rectF2.inset(lerp, lerp);
            imageReceiver.setImageCoords(rectF2);
        }
        if ((avatarStoryParams.prevState == 1 && avatarStoryParams.progressToSate != 1.0f) || avatarStoryParams.currentState == 1) {
            if (i == 2) {
                getCloseFriendsPaint(imageReceiver);
                gradientTools = closeFriendsGradientTools;
            } else {
                getActiveCirclePaint(imageReceiver, avatarStoryParams.isStoryCell);
                gradientTools = storiesGradientTools[avatarStoryParams.isStoryCell ? 1 : 0];
            }
            boolean z4 = avatarStoryParams.prevState == 1 && avatarStoryParams.progressToSate != 1.0f;
            float f8 = avatarStoryParams.isStoryCell ? -AndroidUtilities.dp(4.0f) : 0.0f;
            if (z4) {
                dp3 = f8 + (AndroidUtilities.dp(5.0f) * f7);
                gradientTools.paint.setAlpha((int) ((1.0f - f7) * 255.0f));
            } else {
                gradientTools.paint.setAlpha((int) (f7 * 255.0f));
                dp3 = f8 + (AndroidUtilities.dp(5.0f) * (1.0f - f7));
            }
            RectF rectF3 = rectTmp;
            rectF3.set(avatarStoryParams.originalAvatarRect);
            rectF3.inset(dp3, dp3);
            drawCircleInternal(canvas, imageReceiver.getParentView(), avatarStoryParams, gradientTools.paint);
        }
        int i7 = avatarStoryParams.prevState;
        if ((i7 != 2 || avatarStoryParams.progressToSate == 1.0f) && avatarStoryParams.currentState != 2) {
            i3 = i2;
        } else {
            boolean z5 = i7 == 2 && avatarStoryParams.progressToSate != 1.0f;
            if (avatarStoryParams.isStoryCell) {
                checkStoryCellGrayPaint(avatarStoryParams.isArchive);
                paint2 = storyCellGreyPaint[avatarStoryParams.isArchive ? 1 : 0];
            } else {
                checkGrayPaint();
                paint2 = grayPaint;
            }
            if (avatarStoryParams.drawSegments) {
                Paint activeCirclePaint = getActiveCirclePaint(imageReceiver, avatarStoryParams.isStoryCell);
                activeCirclePaint.setAlpha(255);
                Paint closeFriendsPaint = getCloseFriendsPaint(imageReceiver);
                closeFriendsPaint.setAlpha(255);
                checkGrayPaint();
                paint4 = closeFriendsPaint;
                paint3 = activeCirclePaint;
            } else {
                paint3 = null;
                paint4 = null;
            }
            if (avatarStoryParams.drawSegments) {
                if (avatarStoryParams.isStoryCell) {
                    dpf2 = AndroidUtilities.dpf2(3.5f);
                    f2 = -dpf2;
                }
                f2 = 0.0f;
            } else {
                if (avatarStoryParams.isStoryCell) {
                    dpf2 = AndroidUtilities.dpf2(2.7f);
                    f2 = -dpf2;
                }
                f2 = 0.0f;
            }
            if (z5) {
                dp2 = f2 + (AndroidUtilities.dp(5.0f) * f7);
                paint2.setAlpha((int) ((1.0f - f7) * 255.0f));
            } else {
                paint2.setAlpha((int) (f7 * 255.0f));
                dp2 = f2 + (AndroidUtilities.dp(5.0f) * (1.0f - f7));
            }
            RectF rectF4 = rectTmp;
            rectF4.set(avatarStoryParams.originalAvatarRect);
            rectF4.inset(dp2, dp2);
            if (avatarStoryParams.drawSegments) {
                checkGrayPaint();
                checkStoryCellGrayPaint(avatarStoryParams.isArchive);
                int unreadState2 = storiesController.getUnreadState(j);
                avatarStoryParams.globalState = unreadState2 == 0 ? 2 : 1;
                i3 = i2;
                TLRPC$TL_userStories stories = storiesController.getStories(avatarStoryParams.dialogId);
                if (avatarStoryParams.drawHiddenStoriesAsSegments) {
                    size = storiesController.getHiddenList().size();
                } else {
                    size = (stories == null || stories.stories.size() == 1) ? 1 : stories.stories.size();
                }
                int i8 = size;
                if (i8 == 1) {
                    if (!storiesController.hasUnreadStories(j)) {
                        paint3 = paint2;
                    }
                    Paint paint8 = paint3;
                    drawSegment(canvas, rectF4, paint8, -90.0f, 90.0f, avatarStoryParams);
                    drawSegment(canvas, rectF4, paint8, 90.0f, 270.0f, avatarStoryParams);
                } else {
                    float f9 = 360.0f / i8;
                    float f10 = (i8 > 20 ? 3 : 5) * avatarStoryParams.progressToSegments;
                    float f11 = f10 > f9 ? 0.0f : f10;
                    if (unreadState2 == 2) {
                        getCloseFriendsPaint(imageReceiver);
                        paint5 = closeFriendsGradientTools.paint;
                    } else if (unreadState2 == 1) {
                        getActiveCirclePaint(imageReceiver, avatarStoryParams.isStoryCell);
                        paint5 = storiesGradientTools[avatarStoryParams.isStoryCell ? 1 : 0].paint;
                    } else {
                        paint5 = avatarStoryParams.isStoryCell ? storyCellGreyPaint[avatarStoryParams.isArchive ? 1 : 0] : grayPaint;
                    }
                    Paint paint9 = paint5;
                    int max = avatarStoryParams.drawHiddenStoriesAsSegments ? 0 : Math.max(stories.max_read_id, storiesController.dialogIdToMaxReadId.get(j, 0));
                    int i9 = 0;
                    while (i9 < i8) {
                        Paint paint10 = avatarStoryParams.isStoryCell ? storyCellGreyPaint[avatarStoryParams.isArchive ? 1 : 0] : grayPaint;
                        if (avatarStoryParams.drawHiddenStoriesAsSegments) {
                            f3 = f11;
                            int unreadState3 = storiesController.getUnreadState(storiesController.getHiddenList().get((i8 - 1) - i9).user_id);
                            if (unreadState3 == i4) {
                                paint10 = paint4;
                            } else if (unreadState3 == 1) {
                                paint10 = paint3;
                            }
                        } else {
                            f3 = f11;
                            if (stories.stories.get(i9).justUploaded || stories.stories.get(i9).id > max) {
                                paint6 = stories.stories.get(i9).close_friends ? paint4 : paint3;
                                float f12 = (i9 * f9) - 90.0f;
                                f4 = f12 + f3;
                                f5 = (f12 + f9) - f3;
                                rectF = rectTmp;
                                int i10 = i9;
                                int i11 = max;
                                paint7 = paint9;
                                drawSegment(canvas, rectF, paint6, f4, f5, avatarStoryParams);
                                if (avatarStoryParams.progressToSegments != 1.0f && paint6 != paint7) {
                                    paint7.getStrokeWidth();
                                    paint7.setAlpha((int) ((1.0f - avatarStoryParams.progressToSegments) * 255.0f));
                                    drawSegment(canvas, rectF, paint7, f4, f5, avatarStoryParams);
                                    paint7.setAlpha(255);
                                }
                                i9 = i10 + 1;
                                paint9 = paint7;
                                f11 = f3;
                                max = i11;
                                i4 = 2;
                            }
                        }
                        paint6 = paint10;
                        float f122 = (i9 * f9) - 90.0f;
                        f4 = f122 + f3;
                        f5 = (f122 + f9) - f3;
                        rectF = rectTmp;
                        int i102 = i9;
                        int i112 = max;
                        paint7 = paint9;
                        drawSegment(canvas, rectF, paint6, f4, f5, avatarStoryParams);
                        if (avatarStoryParams.progressToSegments != 1.0f) {
                            paint7.getStrokeWidth();
                            paint7.setAlpha((int) ((1.0f - avatarStoryParams.progressToSegments) * 255.0f));
                            drawSegment(canvas, rectF, paint7, f4, f5, avatarStoryParams);
                            paint7.setAlpha(255);
                        }
                        i9 = i102 + 1;
                        paint9 = paint7;
                        f11 = f3;
                        max = i112;
                        i4 = 2;
                    }
                }
            } else {
                i3 = i2;
                drawCircleInternal(canvas, imageReceiver.getParentView(), avatarStoryParams, paint2);
                if ((avatarStoryParams.prevState == 3 && avatarStoryParams.progressToSate != 1.0f) || avatarStoryParams.currentState == 3) {
                    if (avatarStoryParams.animateFromUnreadState != 1) {
                        checkGrayPaint();
                        paint = grayPaint;
                    } else {
                        getActiveCirclePaint(imageReceiver, avatarStoryParams.isStoryCell);
                        paint = storiesGradientTools[avatarStoryParams.isStoryCell ? 1 : 0].paint;
                    }
                    int i12 = (int) (f7 * 255.0f);
                    paint.setAlpha(i12);
                    if ((avatarStoryParams.prevState == 3 || avatarStoryParams.progressToSate == 1.0f) ? false : true) {
                        dp = (AndroidUtilities.dp(7.0f) * f7) + 0.0f;
                        paint.setAlpha((int) ((1.0f - f7) * 255.0f));
                    } else {
                        paint.setAlpha(i12);
                        dp = (AndroidUtilities.dp(5.0f) * (1.0f - f7)) + 0.0f;
                    }
                    RectF rectF5 = rectTmp;
                    rectF5.set(avatarStoryParams.originalAvatarRect);
                    rectF5.inset(dp, dp);
                    drawProgress(canvas, avatarStoryParams, imageReceiver.getParentView(), paint);
                }
                imageReceiver.draw(canvas);
                f = avatarStoryParams.progressToSate;
                if (f != 1.0f) {
                    float f13 = f + (AndroidUtilities.screenRefreshTime / 250.0f);
                    avatarStoryParams.progressToSate = f13;
                    if (f13 > 1.0f) {
                        avatarStoryParams.progressToSate = 1.0f;
                    }
                    if (imageReceiver.getParentView() != null) {
                        imageReceiver.invalidate();
                        imageReceiver.getParentView().invalidate();
                    }
                }
                if (i3 != 0) {
                    canvas.restoreToCount(i3);
                    return;
                }
                return;
            }
        }
        if (avatarStoryParams.prevState == 3) {
            if (avatarStoryParams.animateFromUnreadState != 1) {
            }
            int i122 = (int) (f7 * 255.0f);
            paint.setAlpha(i122);
            if ((avatarStoryParams.prevState == 3 || avatarStoryParams.progressToSate == 1.0f) ? false : true) {
            }
            RectF rectF52 = rectTmp;
            rectF52.set(avatarStoryParams.originalAvatarRect);
            rectF52.inset(dp, dp);
            drawProgress(canvas, avatarStoryParams, imageReceiver.getParentView(), paint);
            imageReceiver.draw(canvas);
            f = avatarStoryParams.progressToSate;
            if (f != 1.0f) {
            }
            if (i3 != 0) {
            }
        }
        if (avatarStoryParams.animateFromUnreadState != 1) {
        }
        int i1222 = (int) (f7 * 255.0f);
        paint.setAlpha(i1222);
        if ((avatarStoryParams.prevState == 3 || avatarStoryParams.progressToSate == 1.0f) ? false : true) {
        }
        RectF rectF522 = rectTmp;
        rectF522.set(avatarStoryParams.originalAvatarRect);
        rectF522.inset(dp, dp);
        drawProgress(canvas, avatarStoryParams, imageReceiver.getParentView(), paint);
        imageReceiver.draw(canvas);
        f = avatarStoryParams.progressToSate;
        if (f != 1.0f) {
        }
        if (i3 != 0) {
        }
    }

    private static int getPredictiveUnreadState(StoriesController storiesController, long j) {
        TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j));
        if (j == UserConfig.getInstance(UserConfig.selectedAccount).clientUserId || user == null || user.stories_max_id <= 0 || user.stories_unavailable) {
            return 0;
        }
        return user.stories_max_id > storiesController.dialogIdToMaxReadId.get(j, 0) ? 1 : 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0099 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void drawProgress(Canvas canvas, AvatarStoryParams avatarStoryParams, View view, Paint paint) {
        float[] fArr;
        avatarStoryParams.updateProgressParams();
        view.invalidate();
        float strokeWidth = paint.getStrokeWidth();
        for (int i = 0; i < 20; i++) {
            float f = i * 18.0f;
            float f2 = 18.0f + f;
            float f3 = (f2 - f) / 2.0f;
            float f4 = f + f3;
            if (avatarStoryParams.alphas == null) {
                avatarStoryParams.alphas = new float[20];
            }
            float f5 = avatarStoryParams.globalAngle;
            if (f4 <= f5 || f4 >= avatarStoryParams.sweepAngle + f5) {
                float f6 = f4 + 360.0f;
                if (f6 <= f5 || f6 >= f5 + avatarStoryParams.sweepAngle) {
                    float[] fArr2 = avatarStoryParams.alphas;
                    fArr2[i] = fArr2[i] - (AndroidUtilities.screenRefreshTime / 500.0f);
                    if (fArr2[i] < 0.0f) {
                        fArr2[i] = 0.0f;
                    }
                    fArr = avatarStoryParams.alphas;
                    if (fArr[i] == 0.0f) {
                        float f7 = ((1.0f - fArr[i]) * f3) + f;
                        paint.setStrokeWidth(fArr[i] * strokeWidth);
                        canvas.drawArc(rectTmp, f7, (f2 - (f3 * (1.0f - fArr[i]))) - f7, false, paint);
                    }
                }
            }
            float[] fArr3 = avatarStoryParams.alphas;
            fArr3[i] = fArr3[i] + (AndroidUtilities.screenRefreshTime / 150.0f);
            if (fArr3[i] > 1.0f) {
                fArr3[i] = 1.0f;
            }
            fArr = avatarStoryParams.alphas;
            if (fArr[i] == 0.0f) {
            }
        }
        paint.setStrokeWidth(strokeWidth);
    }

    private static void checkStoryCellGrayPaint(boolean z) {
        Paint[] paintArr = storyCellGreyPaint;
        if (paintArr[z ? 1 : 0] == null) {
            paintArr[z ? 1 : 0] = new Paint(1);
            storyCellGreyPaint[z ? 1 : 0].setStyle(Paint.Style.STROKE);
            storyCellGreyPaint[z ? 1 : 0].setStrokeWidth(AndroidUtilities.dpf2(1.3f));
            storyCellGreyPaint[z ? 1 : 0].setStrokeCap(Paint.Cap.ROUND);
        }
        int color = Theme.getColor(!z ? Theme.key_actionBarDefault : Theme.key_actionBarDefaultArchived);
        if (storyCellGrayLastColor != color) {
            storyCellGrayLastColor = color;
            float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(color);
            if (!(computePerceivedBrightness < 0.721f)) {
                storyCellGreyPaint[z ? 1 : 0].setColor(ColorUtils.blendARGB(color, -16777216, 0.2f));
            } else if (computePerceivedBrightness < 0.25f) {
                storyCellGreyPaint[z ? 1 : 0].setColor(ColorUtils.blendARGB(color, -1, 0.2f));
            } else {
                storyCellGreyPaint[z ? 1 : 0].setColor(ColorUtils.blendARGB(color, -1, 0.44f));
            }
        }
    }

    private static void checkGrayPaint() {
        if (grayPaint == null) {
            Paint paint = new Paint(1);
            grayPaint = paint;
            paint.setStyle(Paint.Style.STROKE);
            grayPaint.setStrokeWidth(AndroidUtilities.dpf2(1.3f));
            grayPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        int color = Theme.getColor(Theme.key_windowBackgroundWhite);
        if (grayLastColor != color) {
            grayLastColor = color;
            float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(color);
            if (!(computePerceivedBrightness < 0.721f)) {
                grayPaint.setColor(ColorUtils.blendARGB(color, -16777216, 0.2f));
            } else if (computePerceivedBrightness < 0.25f) {
                grayPaint.setColor(ColorUtils.blendARGB(color, -1, 0.2f));
            } else {
                grayPaint.setColor(ColorUtils.blendARGB(color, -1, 0.44f));
            }
        }
    }

    private static void drawCircleInternal(Canvas canvas, View view, AvatarStoryParams avatarStoryParams, Paint paint) {
        float f = avatarStoryParams.progressToArc;
        if (f == 0.0f) {
            RectF rectF = rectTmp;
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, paint);
            return;
        }
        canvas.drawArc(rectTmp, (f / 2.0f) + 360.0f, 360.0f - f, false, paint);
    }

    private static void drawSegment(Canvas canvas, RectF rectF, Paint paint, float f, float f2, AvatarStoryParams avatarStoryParams) {
        boolean z = avatarStoryParams.isFirst;
        if (!z && !avatarStoryParams.isLast) {
            if (f < 90.0f) {
                float f3 = avatarStoryParams.progressToArc;
                drawArcExcludeArc(canvas, rectF, paint, f, f2, (-f3) / 2.0f, f3 / 2.0f);
                return;
            }
            float f4 = avatarStoryParams.progressToArc;
            drawArcExcludeArc(canvas, rectF, paint, f, f2, ((-f4) / 2.0f) + 180.0f, (f4 / 2.0f) + 180.0f);
        } else if (avatarStoryParams.isLast) {
            float f5 = avatarStoryParams.progressToArc;
            drawArcExcludeArc(canvas, rectF, paint, f, f2, ((-f5) / 2.0f) + 180.0f, (f5 / 2.0f) + 180.0f);
        } else if (z) {
            float f6 = avatarStoryParams.progressToArc;
            drawArcExcludeArc(canvas, rectF, paint, f, f2, (-f6) / 2.0f, f6 / 2.0f);
        } else {
            canvas.drawArc(rectF, f, f2 - f, false, paint);
        }
    }

    private static int getInset(int i, int i2) {
        if (i == 3) {
            i = i2;
        }
        if (i == 2) {
            return AndroidUtilities.dp(3.0f);
        }
        if (i == 1) {
            return AndroidUtilities.dp(4.0f);
        }
        return 0;
    }

    public static Paint getActiveCirclePaint(ImageReceiver imageReceiver, boolean z) {
        GradientTools[] gradientToolsArr = storiesGradientTools;
        if (gradientToolsArr[z ? 1 : 0] == null) {
            gradientToolsArr[z ? 1 : 0] = new GradientTools();
            GradientTools[] gradientToolsArr2 = storiesGradientTools;
            gradientToolsArr2[z ? 1 : 0].isDiagonal = true;
            gradientToolsArr2[z ? 1 : 0].isRotate = true;
            if (z) {
                gradientToolsArr2[z ? 1 : 0].setColors(-11866795, -11680769);
            } else {
                gradientToolsArr2[z ? 1 : 0].setColors(-12984516, -11682817);
            }
            storiesGradientTools[z ? 1 : 0].paint.setStrokeWidth(AndroidUtilities.dpf2(2.3f));
            storiesGradientTools[z ? 1 : 0].paint.setStyle(Paint.Style.STROKE);
            storiesGradientTools[z ? 1 : 0].paint.setStrokeCap(Paint.Cap.ROUND);
        }
        storiesGradientTools[z ? 1 : 0].setBounds(imageReceiver.getImageX(), imageReceiver.getImageY(), imageReceiver.getImageX2(), imageReceiver.getImageY2());
        return storiesGradientTools[z ? 1 : 0].paint;
    }

    public static Paint getCloseFriendsPaint(ImageReceiver imageReceiver) {
        if (closeFriendsGradientTools == null) {
            GradientTools gradientTools = new GradientTools();
            closeFriendsGradientTools = gradientTools;
            gradientTools.isDiagonal = true;
            gradientTools.isRotate = true;
            gradientTools.setColors(-3544264, -16137881);
            closeFriendsGradientTools.paint.setStrokeWidth(AndroidUtilities.dp(2.3f));
            closeFriendsGradientTools.paint.setStyle(Paint.Style.STROKE);
            closeFriendsGradientTools.paint.setStrokeCap(Paint.Cap.ROUND);
        }
        closeFriendsGradientTools.setBounds(imageReceiver.getImageX(), imageReceiver.getImageY(), imageReceiver.getImageX2(), imageReceiver.getImageY2());
        return closeFriendsGradientTools.paint;
    }

    public static void setStoryMiniImage(ImageReceiver imageReceiver, TLRPC$StoryItem tLRPC$StoryItem) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        if (tLRPC$StoryItem == null) {
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
        TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
        if (tLRPC$Document != null) {
            imageReceiver.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 1000), tLRPC$StoryItem.media.document), "100_100", null, null, ImageLoader.createStripedBitmap(tLRPC$StoryItem.media.document.thumbs), 0L, null, tLRPC$StoryItem, 0);
            return;
        }
        TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.photo : null;
        if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.sizes) != null) {
            imageReceiver.setImage(null, null, ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(arrayList, 1000), tLRPC$Photo), "100_100", null, null, ImageLoader.createStripedBitmap(tLRPC$Photo.sizes), 0L, null, tLRPC$StoryItem, 0);
        } else {
            imageReceiver.clearImage();
        }
    }

    public static void setImage(ImageReceiver imageReceiver, TLRPC$StoryItem tLRPC$StoryItem) {
        setImage(imageReceiver, tLRPC$StoryItem, "320_320");
    }

    public static void setImage(ImageReceiver imageReceiver, TLRPC$StoryItem tLRPC$StoryItem, String str) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        TLRPC$Document tLRPC$Document;
        if (tLRPC$StoryItem == null) {
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
        if (tLRPC$MessageMedia != null && (tLRPC$Document = tLRPC$MessageMedia.document) != null) {
            imageReceiver.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, ConnectionsManager.DEFAULT_DATACENTER_ID), tLRPC$StoryItem.media.document), str, null, null, ImageLoader.createStripedBitmap(tLRPC$StoryItem.media.document.thumbs), 0L, null, tLRPC$StoryItem, 0);
            return;
        }
        TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.photo : null;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaUnsupported) {
            Bitmap createBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            createBitmap.eraseColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
            imageReceiver.setImageBitmap(createBitmap);
        } else if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.sizes) != null) {
            imageReceiver.setImage(null, null, ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(arrayList, ConnectionsManager.DEFAULT_DATACENTER_ID), tLRPC$Photo), str, null, null, ImageLoader.createStripedBitmap(tLRPC$Photo.sizes), 0L, null, tLRPC$StoryItem, 0);
        } else {
            imageReceiver.clearImage();
        }
    }

    public static void setImage(ImageReceiver imageReceiver, StoriesController.UploadingStory uploadingStory) {
        if (uploadingStory.entry.isVideo) {
            imageReceiver.setImage(ImageLocation.getForPath(uploadingStory.firstFramePath), "320_180", null, null, null, 0L, null, null, 0);
        } else {
            imageReceiver.setImage(ImageLocation.getForPath(uploadingStory.path), "320_180", null, null, null, 0L, null, null, 0);
        }
    }

    public static void setThumbImage(ImageReceiver imageReceiver, TLRPC$StoryItem tLRPC$StoryItem, int i, int i2) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        TLRPC$Document tLRPC$Document;
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
        if (tLRPC$MessageMedia != null && (tLRPC$Document = tLRPC$MessageMedia.document) != null) {
            ImageLocation forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, AndroidUtilities.dp(Math.max(i, i2)), false, null, true), tLRPC$StoryItem.media.document);
            imageReceiver.setImage(forDocument, i + "_" + i2, null, null, ImageLoader.createStripedBitmap(tLRPC$StoryItem.media.document.thumbs), 0L, null, tLRPC$StoryItem, 0);
            return;
        }
        TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.photo : null;
        if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.sizes) != null) {
            ImageLocation forPhoto = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.dp(Math.max(i, i2)), false, null, true), tLRPC$Photo);
            imageReceiver.setImage(null, null, forPhoto, i + "_" + i2, null, null, ImageLoader.createStripedBitmap(tLRPC$Photo.sizes), 0L, null, tLRPC$StoryItem, 0);
            return;
        }
        imageReceiver.clearImage();
    }

    public static Drawable getExpiredStoryDrawable() {
        if (expiredStoryDrawable == null) {
            Bitmap createBitmap = Bitmap.createBitmap(360, 180, Bitmap.Config.ARGB_8888);
            createBitmap.eraseColor(-7829368);
            Canvas canvas = new Canvas(createBitmap);
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTextSize(15.0f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(ColorUtils.setAlphaComponent(-16777216, 100));
            canvas.drawText("expired", 180.0f, 86.0f, textPaint);
            canvas.drawText("story", 180.0f, 106.0f, textPaint);
            expiredStoryDrawable = new BitmapDrawable(createBitmap);
        }
        return expiredStoryDrawable;
    }

    public static CharSequence getUploadingStr(TextView textView, boolean z, boolean z2) {
        String string;
        if (z2) {
            string = LocaleController.getString("StoryEditing", R.string.StoryEditing);
        } else {
            string = LocaleController.getString("UploadingStory", R.string.UploadingStory);
        }
        if (string.indexOf("…") > 0) {
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
            UploadingDotsSpannable uploadingDotsSpannable = new UploadingDotsSpannable();
            valueOf.setSpan(uploadingDotsSpannable, valueOf.length() - 1, valueOf.length(), 0);
            uploadingDotsSpannable.setParent(textView, z);
            return valueOf;
        }
        return string;
    }

    public static void applyUploadingStr(SimpleTextView simpleTextView, boolean z, boolean z2) {
        String string;
        if (z2) {
            string = LocaleController.getString("StoryEditing", R.string.StoryEditing);
        } else {
            string = LocaleController.getString("UploadingStory", R.string.UploadingStory);
        }
        if (string.indexOf("…") > 0) {
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
            UploadingDotsSpannable uploadingDotsSpannable = new UploadingDotsSpannable();
            valueOf.setSpan(uploadingDotsSpannable, valueOf.length() - 1, valueOf.length(), 0);
            uploadingDotsSpannable.setParent(simpleTextView, z);
            simpleTextView.setText(valueOf);
            return;
        }
        simpleTextView.setText(string);
    }

    public static CharSequence createExpiredStoryString() {
        return createExpiredStoryString(false, "ExpiredStory", R.string.ExpiredStory, new Object[0]);
    }

    public static CharSequence createExpiredStoryString(boolean z, String str, int i, Object... objArr) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "d ").append((CharSequence) LocaleController.formatString(str, i, objArr));
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_mini_bomb);
        if (z) {
            coloredImageSpan.setScale(0.8f);
        } else {
            coloredImageSpan.setTopOffset(-1);
        }
        spannableStringBuilder.setSpan(coloredImageSpan, 0, 1, 0);
        return spannableStringBuilder;
    }

    public static CharSequence createReplyStoryString() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "d ").append((CharSequence) LocaleController.getString("Story", R.string.Story));
        spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_mini_replystory2), 0, 1, 0);
        return spannableStringBuilder;
    }

    public static boolean hasExpiredViews(TLRPC$StoryItem tLRPC$StoryItem) {
        return tLRPC$StoryItem != null && ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() > tLRPC$StoryItem.expire_date + 86400;
    }

    public static void applyViewedUser(TLRPC$StoryItem tLRPC$StoryItem, TLRPC$User tLRPC$User) {
        if (tLRPC$StoryItem.dialogId != UserConfig.getInstance(UserConfig.selectedAccount).clientUserId || hasExpiredViews(tLRPC$StoryItem)) {
            return;
        }
        if (tLRPC$StoryItem.views == null) {
            tLRPC$StoryItem.views = new TLRPC$TL_storyViews();
        }
        TLRPC$TL_storyViews tLRPC$TL_storyViews = tLRPC$StoryItem.views;
        if (tLRPC$TL_storyViews.views_count == 0) {
            tLRPC$TL_storyViews.views_count = 1;
            tLRPC$TL_storyViews.recent_viewers.add(Long.valueOf(tLRPC$User.id));
        }
    }

    public static void drawArcExcludeArc(Canvas canvas, RectF rectF, Paint paint, float f, float f2, float f3, float f4) {
        boolean z;
        float f5 = f2 - f;
        if (f >= f3 || f2 >= f3 + f5) {
            z = false;
        } else {
            z = true;
            canvas.drawArc(rectF, f, Math.min(f2, f3) - f, false, paint);
        }
        float max = Math.max(f, f4);
        float min = Math.min(f2, f3 + 360.0f);
        if (min >= max) {
            canvas.drawArc(rectF, max, min - max, false, paint);
        } else if (z) {
        } else {
            if (f <= f3 || f2 >= f4) {
                canvas.drawArc(rectF, f, f5, false, paint);
            }
        }
    }

    public static boolean isExpired(int i, TLRPC$StoryItem tLRPC$StoryItem) {
        return ConnectionsManager.getInstance(i).getCurrentTime() > tLRPC$StoryItem.expire_date;
    }

    public static String getStoryImageFilter() {
        int max = (int) (Math.max(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y) / AndroidUtilities.density);
        return max + "_" + max;
    }

    /* loaded from: classes4.dex */
    public static class AvatarStoryParams {
        public float[] alphas;
        public int animateFromUnreadState;
        ButtonBounce buttonBounce;
        public int currentState;
        private long dialogId;
        public boolean drawHiddenStoriesAsSegments;
        public boolean forceAnimateProgressToSegments;
        float globalAngle;
        public int globalState;
        boolean inc;
        public boolean isArchive;
        public boolean isFirst;
        public boolean isLast;
        private final boolean isStoryCell;
        Runnable longPressRunnable;
        UserStoriesLoadOperation operation;
        boolean pressed;
        public int prevState;
        float startX;
        float startY;
        public int storyId;
        public TLRPC$StoryItem storyItem;
        float sweepAngle;
        public int unreadState;
        public boolean drawSegments = true;
        public boolean animate = true;
        public float progressToSegments = 1.0f;
        public float progressToArc = 0.0f;
        public float progressToSate = 1.0f;
        public boolean showProgress = false;
        public RectF originalAvatarRect = new RectF();
        public boolean allowLongress = false;

        public void onLongPress() {
        }

        public void openStory(long j, Runnable runnable) {
        }

        public AvatarStoryParams(boolean z) {
            this.isStoryCell = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateProgressParams() {
            float f = AndroidUtilities.screenRefreshTime;
            float f2 = (f * 360.0f) / 1500.0f;
            if (this.inc) {
                float f3 = this.sweepAngle + f2;
                this.sweepAngle = f3;
                if (f3 >= 300.0f) {
                    this.inc = false;
                }
            } else {
                float f4 = this.sweepAngle - f2;
                this.sweepAngle = f4;
                if (f4 < 10.0f) {
                    this.inc = true;
                }
            }
            float f5 = this.globalAngle + ((f * 360.0f) / 900.0f);
            this.globalAngle = f5;
            if (!this.inc) {
                this.globalAngle = f5 + f2;
            }
            float f6 = this.globalAngle;
            if (f6 > 360.0f) {
                this.globalAngle = f6 % 360.0f;
            }
        }

        public boolean checkOnTouchEvent(MotionEvent motionEvent, final View view) {
            boolean z;
            StoriesController storiesController = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController();
            boolean z2 = false;
            if (motionEvent.getAction() == 0 && this.originalAvatarRect.contains(motionEvent.getX(), motionEvent.getY())) {
                TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(this.dialogId));
                if (this.drawHiddenStoriesAsSegments) {
                    z = storiesController.hasHiddenStories();
                } else {
                    if (MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController().hasStories(this.dialogId) || (user != null && !user.stories_unavailable && user.stories_max_id > 0)) {
                        z2 = true;
                    }
                    z = z2;
                }
                if (this.dialogId != UserConfig.getInstance(UserConfig.selectedAccount).clientUserId && z) {
                    ButtonBounce buttonBounce = this.buttonBounce;
                    if (buttonBounce == null) {
                        this.buttonBounce = new ButtonBounce(view, 1.5f);
                    } else {
                        buttonBounce.setView(view);
                    }
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    this.buttonBounce.setPressed(true);
                    this.pressed = true;
                    this.startX = motionEvent.getX();
                    this.startY = motionEvent.getY();
                    if (this.allowLongress) {
                        Runnable runnable = this.longPressRunnable;
                        if (runnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                        }
                        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$AvatarStoryParams$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                StoriesUtilities.AvatarStoryParams.this.lambda$checkOnTouchEvent$0(view);
                            }
                        };
                        this.longPressRunnable = runnable2;
                        AndroidUtilities.runOnUIThread(runnable2, ViewConfiguration.getLongPressTimeout());
                    }
                }
            } else if (motionEvent.getAction() == 2 && this.pressed) {
                if (Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop || Math.abs(this.startY - motionEvent.getY()) > AndroidUtilities.touchSlop) {
                    ButtonBounce buttonBounce2 = this.buttonBounce;
                    if (buttonBounce2 != null) {
                        buttonBounce2.setView(view);
                        this.buttonBounce.setPressed(false);
                    }
                    Runnable runnable3 = this.longPressRunnable;
                    if (runnable3 != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable3);
                    }
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    this.pressed = false;
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                ButtonBounce buttonBounce3 = this.buttonBounce;
                if (buttonBounce3 != null) {
                    buttonBounce3.setView(view);
                    this.buttonBounce.setPressed(false);
                }
                if (this.pressed && motionEvent.getAction() == 1) {
                    processOpenStory(view);
                }
                view.getParent().requestDisallowInterceptTouchEvent(false);
                this.pressed = false;
                Runnable runnable4 = this.longPressRunnable;
                if (runnable4 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable4);
                }
            }
            return this.pressed;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkOnTouchEvent$0(View view) {
            view.performHapticFeedback(0);
            ButtonBounce buttonBounce = this.buttonBounce;
            if (buttonBounce != null) {
                buttonBounce.setPressed(false);
            }
            view.getParent().requestDisallowInterceptTouchEvent(false);
            this.pressed = false;
            onLongPress();
        }

        private void processOpenStory(View view) {
            MessagesController messagesController = MessagesController.getInstance(UserConfig.selectedAccount);
            StoriesController storiesController = messagesController.getStoriesController();
            if (this.drawHiddenStoriesAsSegments) {
                openStory(0L, null);
            } else if (this.dialogId != UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()) {
                if (storiesController.hasStories(this.dialogId)) {
                    openStory(this.dialogId, null);
                    return;
                }
                TLRPC$User user = messagesController.getUser(Long.valueOf(this.dialogId));
                if (user == null || user.stories_unavailable || user.stories_max_id <= 0) {
                    return;
                }
                new UserStoriesLoadOperation().load(this.dialogId, view, this);
            }
        }

        public float getScale() {
            ButtonBounce buttonBounce = this.buttonBounce;
            if (buttonBounce == null) {
                return 1.0f;
            }
            return buttonBounce.getScale(0.08f);
        }

        public void reset() {
            UserStoriesLoadOperation userStoriesLoadOperation = this.operation;
            if (userStoriesLoadOperation != null) {
                userStoriesLoadOperation.cancel();
                this.operation = null;
            }
            this.buttonBounce = null;
            this.pressed = false;
        }

        public void onDetachFromWindow() {
            reset();
        }
    }

    /* loaded from: classes4.dex */
    public static class UserStoriesLoadOperation {
        private int currentAccount;
        int reqId;

        public UserStoriesLoadOperation() {
            ConnectionsManager.generateClassGuid();
        }

        void load(final long j, final View view, final AvatarStoryParams avatarStoryParams) {
            int i = UserConfig.selectedAccount;
            this.currentAccount = i;
            final MessagesController messagesController = MessagesController.getInstance(i);
            messagesController.getStoriesController().setLoading(j, true);
            view.invalidate();
            messagesController.getUser(Long.valueOf(j));
            TLRPC$TL_stories_getUserStories tLRPC$TL_stories_getUserStories = new TLRPC$TL_stories_getUserStories();
            tLRPC$TL_stories_getUserStories.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(j);
            this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_stories_getUserStories, new RequestDelegate() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$$ExternalSyntheticLambda4
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    StoriesUtilities.UserStoriesLoadOperation.this.lambda$load$3(j, view, avatarStoryParams, messagesController, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$load$3(final long j, final View view, final AvatarStoryParams avatarStoryParams, final MessagesController messagesController, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StoriesUtilities.UserStoriesLoadOperation.this.lambda$load$2(tLObject, j, view, avatarStoryParams, messagesController);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:10:0x0055  */
        /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$load$2(TLObject tLObject, final long j, final View view, final AvatarStoryParams avatarStoryParams, MessagesController messagesController) {
            boolean z;
            if (tLObject != null) {
                TLRPC$TL_stories_userStories tLRPC$TL_stories_userStories = (TLRPC$TL_stories_userStories) tLObject;
                MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_stories_userStories.users, false);
                TLRPC$TL_userStories tLRPC$TL_userStories = tLRPC$TL_stories_userStories.stories;
                if (!tLRPC$TL_userStories.stories.isEmpty()) {
                    MessagesController.getInstance(this.currentAccount).getStoriesController().putStories(j, tLRPC$TL_userStories);
                    ensureStoryFileLoaded(tLRPC$TL_userStories, new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoriesUtilities.UserStoriesLoadOperation.this.lambda$load$1(view, j, avatarStoryParams);
                        }
                    });
                    z = false;
                    TLRPC$User user = messagesController.getUser(Long.valueOf(j));
                    user.stories_unavailable = true;
                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(Collections.singletonList(user), null, false, true);
                    messagesController.putUser(user, false);
                    if (z) {
                        return;
                    }
                    view.invalidate();
                    MessagesController.getInstance(this.currentAccount).getStoriesController().setLoading(j, false);
                    return;
                }
            }
            z = true;
            TLRPC$User user2 = messagesController.getUser(Long.valueOf(j));
            user2.stories_unavailable = true;
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(Collections.singletonList(user2), null, false, true);
            messagesController.putUser(user2, false);
            if (z) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$load$1(final View view, final long j, AvatarStoryParams avatarStoryParams) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StoriesUtilities.UserStoriesLoadOperation.this.lambda$load$0(view, j);
                }
            }, 500L);
            avatarStoryParams.openStory(j, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$load$0(View view, long j) {
            view.invalidate();
            MessagesController.getInstance(this.currentAccount).getStoriesController().setLoading(j, false);
        }

        private void ensureStoryFileLoaded(TLRPC$TL_userStories tLRPC$TL_userStories, final Runnable runnable) {
            TLRPC$StoryItem tLRPC$StoryItem;
            ArrayList<TLRPC$PhotoSize> arrayList;
            TLRPC$Document tLRPC$Document;
            if (tLRPC$TL_userStories == null || tLRPC$TL_userStories.stories.isEmpty()) {
                runnable.run();
                return;
            }
            int i = MessagesController.getInstance(this.currentAccount).storiesController.dialogIdToMaxReadId.get(tLRPC$TL_userStories.user_id);
            int i2 = 0;
            while (true) {
                if (i2 >= tLRPC$TL_userStories.stories.size()) {
                    tLRPC$StoryItem = null;
                    break;
                } else if (tLRPC$TL_userStories.stories.get(i2).id > i) {
                    tLRPC$StoryItem = tLRPC$TL_userStories.stories.get(i2);
                    break;
                } else {
                    i2++;
                }
            }
            if (tLRPC$StoryItem == null) {
                tLRPC$StoryItem = tLRPC$TL_userStories.stories.get(0);
            }
            final Runnable[] runnableArr = {new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    StoriesUtilities.UserStoriesLoadOperation.lambda$ensureStoryFileLoaded$4(runnableArr, runnable);
                }
            }};
            AndroidUtilities.runOnUIThread(runnableArr[0], 1000L);
            ImageReceiver imageReceiver = new ImageReceiver(this) { // from class: org.telegram.ui.Stories.StoriesUtilities.UserStoriesLoadOperation.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.messenger.ImageReceiver
                public boolean setImageBitmapByKey(Drawable drawable, String str, int i3, boolean z, int i4) {
                    boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i3, z, i4);
                    Runnable[] runnableArr2 = runnableArr;
                    if (runnableArr2[0] != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnableArr2[0]);
                        runnable.run();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesUtilities$UserStoriesLoadOperation$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            onDetachedFromWindow();
                        }
                    });
                    return imageBitmapByKey;
                }
            };
            imageReceiver.setAllowLoadingOnAttachedOnly(true);
            imageReceiver.onAttachedToWindow();
            String storyImageFilter = StoriesUtilities.getStoryImageFilter();
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
            if (tLRPC$MessageMedia != null && (tLRPC$Document = tLRPC$MessageMedia.document) != null) {
                imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), storyImageFilter + "_pframe", null, null, null, 0L, null, tLRPC$StoryItem, 0);
                return;
            }
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.photo : null;
            if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.sizes) != null) {
                imageReceiver.setImage(null, null, ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(arrayList, ConnectionsManager.DEFAULT_DATACENTER_ID), tLRPC$Photo), storyImageFilter, null, null, null, 0L, null, tLRPC$StoryItem, 0);
            } else {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$ensureStoryFileLoaded$4(Runnable[] runnableArr, Runnable runnable) {
            runnableArr[0] = null;
            runnable.run();
        }

        void cancel() {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, false);
        }
    }
}
