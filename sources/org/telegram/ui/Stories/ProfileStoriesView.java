package org.telegram.ui.Stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import androidx.annotation.Keep;
import androidx.core.graphics.ColorUtils;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.tl.TL_stories$PeerStories;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemSkipped;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.ProfileStoriesView;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.StoryViewer;
/* loaded from: classes4.dex */
public class ProfileStoriesView extends View implements NotificationCenter.NotificationCenterDelegate {
    private float actionBarProgress;
    private boolean attached;
    private final View avatarContainer;
    private final ProfileActivity.AvatarImageView avatarImage;
    private float bounceScale;
    private final ArrayList<StoryCircle> circles;
    private final Paint clipOutAvatar;
    private final Path clipPath;
    private int count;
    private final int currentAccount;
    private float cy;
    private final long dialogId;
    private float expandProgress;
    private float expandRight;
    private boolean expandRightPad;
    private final AnimatedFloat expandRightPadAnimated;
    private float expandY;
    private float fragmentTransitionProgress;
    private final StoriesUtilities.StoryGradientTools gradientTools;
    private StoriesController.UploadingStory lastUploadingStory;
    private float left;
    private StoryCircle mainCircle;
    private ValueAnimator newStoryBounce;
    private float newStoryBounceT;
    private Runnable onLongPressRunnable;
    Paint paint;
    private boolean progressIsDone;
    private float progressToInsets;
    private final AnimatedFloat progressToUploading;
    private boolean progressWasDrawn;
    private final StoryViewer.PlaceProvider provider;
    private RadialProgress radialProgress;
    private final Paint readPaint;
    private int readPaintAlpha;
    private final RectF rect1;
    private final RectF rect2;
    private final RectF rect3;
    private float right;
    private final AnimatedFloat rightAnimated;
    private final AnimatedFloat segmentsCountAnimated;
    private final AnimatedFloat segmentsUnreadCountAnimated;
    StoriesController storiesController;
    private long tapTime;
    private float tapX;
    private float tapY;
    private final AnimatedTextView.AnimatedTextDrawable titleDrawable;
    private int unreadCount;
    private int uploadingStoriesCount;
    float w;
    private final Paint whitePaint;

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onLongPress */
    public void lambda$new$4() {
    }

    protected void onTap(StoryViewer.PlaceProvider placeProvider) {
    }

    public void setProgressToStoriesInsets(float f) {
        if (this.progressToInsets == f) {
            return;
        }
        this.progressToInsets = f;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class StoryCircle {
        final RectF borderRect;
        float cachedIndex;
        float cachedRead;
        final RectF cachedRect;
        float cachedScale;
        final AnimatedFloat indexAnimated;
        final AnimatedFloat readAnimated;
        final AnimatedFloat scaleAnimated;
        int storyId;
        ImageReceiver imageReceiver = new ImageReceiver();
        int index = 0;
        boolean read = false;
        float scale = 1.0f;

        public StoryCircle(TL_stories$StoryItem tL_stories$StoryItem) {
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.readAnimated = new AnimatedFloat(ProfileStoriesView.this, 420L, cubicBezierInterpolator);
            this.indexAnimated = new AnimatedFloat(ProfileStoriesView.this, 420L, cubicBezierInterpolator);
            this.scaleAnimated = new AnimatedFloat(ProfileStoriesView.this, 420L, cubicBezierInterpolator);
            this.cachedRect = new RectF();
            this.borderRect = new RectF();
            this.storyId = tL_stories$StoryItem.id;
            this.imageReceiver.setRoundRadius(AndroidUtilities.dp(200.0f));
            this.imageReceiver.setParentView(ProfileStoriesView.this);
            if (ProfileStoriesView.this.attached) {
                this.imageReceiver.onAttachedToWindow();
            }
            StoriesUtilities.setThumbImage(this.imageReceiver, tL_stories$StoryItem, 25, 25);
        }

        public void destroy() {
            this.imageReceiver.onDetachedFromWindow();
        }

        public void apply() {
            this.readAnimated.set(this.read, true);
            this.indexAnimated.set(this.index, true);
            this.scaleAnimated.set(this.scale, true);
        }
    }

    public ProfileStoriesView(Context context, int i, long j, View view, ProfileActivity.AvatarImageView avatarImageView, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        Paint paint = new Paint(1);
        this.readPaint = paint;
        Paint paint2 = new Paint(1);
        this.whitePaint = paint2;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.titleDrawable = animatedTextDrawable;
        Paint paint3 = new Paint(1);
        this.clipOutAvatar = paint3;
        this.circles = new ArrayList<>();
        this.paint = new Paint(1);
        this.bounceScale = 1.0f;
        this.progressToInsets = 1.0f;
        this.gradientTools = new StoriesUtilities.StoryGradientTools((View) this, false);
        this.rect1 = new RectF();
        this.rect2 = new RectF();
        this.rect3 = new RectF();
        this.clipPath = new Path();
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.segmentsCountAnimated = new AnimatedFloat(this, 0L, 480L, cubicBezierInterpolator);
        this.segmentsUnreadCountAnimated = new AnimatedFloat(this, 0L, 240L, cubicBezierInterpolator);
        this.progressToUploading = new AnimatedFloat(this, 0L, 150L, CubicBezierInterpolator.DEFAULT);
        this.newStoryBounceT = 1.0f;
        this.expandRightPadAnimated = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.rightAnimated = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.provider = new 3();
        this.onLongPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.ProfileStoriesView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ProfileStoriesView.this.lambda$new$4();
            }
        };
        this.currentAccount = i;
        this.dialogId = j;
        this.avatarContainer = view;
        this.avatarImage = avatarImageView;
        this.storiesController = MessagesController.getInstance(i).getStoriesController();
        paint.setColor(1526726655);
        this.readPaintAlpha = paint.getAlpha();
        paint.setStrokeWidth(AndroidUtilities.dpf2(1.5f));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint2.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
        animatedTextDrawable.setTextSize(AndroidUtilities.dp(18.0f));
        animatedTextDrawable.setAnimationProperties(0.4f, 0L, 320L, cubicBezierInterpolator);
        animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        animatedTextDrawable.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle, resourcesProvider));
        animatedTextDrawable.setEllipsizeByGradient(true);
        animatedTextDrawable.setCallback(this);
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.paint.setStrokeWidth(AndroidUtilities.dpf2(2.33f));
        this.paint.setStyle(Paint.Style.STROKE);
        updateStories(false, false);
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.titleDrawable || super.verifyDrawable(drawable);
    }

    public void setStories(TL_stories$PeerStories tL_stories$PeerStories) {
        updateStories(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00f0, code lost:
        if (r7 != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x016f, code lost:
        if (r9 != false) goto L99;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateStories(boolean z, boolean z2) {
        ArrayList<TL_stories$StoryItem> arrayList;
        int i;
        int i2;
        TL_stories$StoryItem tL_stories$StoryItem;
        boolean z3 = this.dialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
        TL_stories$PeerStories storiesFromFullPeer = MessagesController.getInstance(this.currentAccount).getStoriesController().getStoriesFromFullPeer(this.dialogId);
        TL_stories$PeerStories stories = MessagesController.getInstance(this.currentAccount).getStoriesController().getStories(this.dialogId);
        TL_stories$PeerStories tL_stories$PeerStories = this.dialogId == 0 ? null : storiesFromFullPeer;
        int max = storiesFromFullPeer != null ? Math.max(0, storiesFromFullPeer.max_read_id) : 0;
        if (stories != null) {
            max = Math.max(max, stories.max_read_id);
        }
        if (tL_stories$PeerStories == null || (arrayList = tL_stories$PeerStories.stories) == null) {
            arrayList = new ArrayList<>();
        }
        ArrayList arrayList2 = new ArrayList();
        int i3 = this.unreadCount;
        this.unreadCount = 0;
        if (arrayList != null) {
            i2 = 0;
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                TL_stories$StoryItem tL_stories$StoryItem2 = arrayList.get(i4);
                if (!(tL_stories$StoryItem2 instanceof TL_stories$TL_storyItemDeleted)) {
                    if (tL_stories$StoryItem2.id > max) {
                        this.unreadCount++;
                    }
                    i2++;
                }
            }
            int i5 = 0;
            while (true) {
                if (i5 >= arrayList.size()) {
                    i = 3;
                    break;
                }
                TL_stories$StoryItem tL_stories$StoryItem3 = arrayList.get(i5);
                if (!(tL_stories$StoryItem3 instanceof TL_stories$TL_storyItemDeleted)) {
                    if (tL_stories$StoryItem3 instanceof TL_stories$TL_storyItemSkipped) {
                        int i6 = tL_stories$StoryItem3.id;
                        if (stories != null) {
                            int i7 = 0;
                            while (true) {
                                if (i7 >= stories.stories.size()) {
                                    break;
                                } else if (stories.stories.get(i7).id == i6) {
                                    tL_stories$StoryItem3 = stories.stories.get(i7);
                                    break;
                                } else {
                                    i7++;
                                }
                            }
                        }
                        boolean z4 = tL_stories$StoryItem3 instanceof TL_stories$TL_storyItemSkipped;
                        if (z4) {
                            if (storiesFromFullPeer != null) {
                                int i8 = 0;
                                while (true) {
                                    if (i8 >= storiesFromFullPeer.stories.size()) {
                                        break;
                                    } else if (storiesFromFullPeer.stories.get(i8).id == i6) {
                                        storiesFromFullPeer.stories.get(i8);
                                        break;
                                    } else {
                                        i8++;
                                    }
                                }
                            }
                        }
                    }
                    if (z3 || tL_stories$StoryItem3.id > max) {
                        arrayList2.add(tL_stories$StoryItem3);
                        i = 3;
                        if (arrayList2.size() >= 3) {
                            break;
                        }
                        i5++;
                    }
                }
                i5++;
            }
        } else {
            i = 3;
            i2 = 0;
        }
        if (arrayList2.size() < i) {
            for (int i9 = 0; i9 < arrayList.size(); i9++) {
                TL_stories$StoryItem tL_stories$StoryItem4 = arrayList.get(i9);
                if (tL_stories$StoryItem4 instanceof TL_stories$TL_storyItemSkipped) {
                    int i10 = tL_stories$StoryItem4.id;
                    if (stories != null) {
                        int i11 = 0;
                        while (true) {
                            if (i11 >= stories.stories.size()) {
                                break;
                            } else if (stories.stories.get(i11).id == i10) {
                                tL_stories$StoryItem4 = stories.stories.get(i11);
                                break;
                            } else {
                                i11++;
                            }
                        }
                    }
                    boolean z5 = tL_stories$StoryItem4 instanceof TL_stories$TL_storyItemSkipped;
                    if (z5) {
                        if (storiesFromFullPeer != null) {
                            int i12 = 0;
                            while (true) {
                                if (i12 >= storiesFromFullPeer.stories.size()) {
                                    break;
                                } else if (storiesFromFullPeer.stories.get(i12).id == i10) {
                                    storiesFromFullPeer.stories.get(i12);
                                    break;
                                } else {
                                    i12++;
                                }
                            }
                        }
                    }
                }
                if (!(tL_stories$StoryItem4 instanceof TL_stories$TL_storyItemDeleted) && !arrayList2.contains(tL_stories$StoryItem4)) {
                    arrayList2.add(tL_stories$StoryItem4);
                    if (arrayList2.size() >= 3) {
                        break;
                    }
                }
            }
        }
        for (int i13 = 0; i13 < this.circles.size(); i13++) {
            StoryCircle storyCircle = this.circles.get(i13);
            int i14 = 0;
            while (true) {
                if (i14 >= arrayList2.size()) {
                    i14 = -1;
                    tL_stories$StoryItem = null;
                    break;
                }
                tL_stories$StoryItem = (TL_stories$StoryItem) arrayList2.get(i14);
                if (tL_stories$StoryItem.id == storyCircle.storyId) {
                    break;
                }
                i14++;
            }
            if (i14 == -1) {
                storyCircle.scale = 0.0f;
            } else {
                storyCircle.index = i14;
                storyCircle.read = z3 || !(tL_stories$PeerStories == null || tL_stories$StoryItem == null || tL_stories$StoryItem.id > this.storiesController.getMaxStoriesReadId(this.dialogId));
            }
            if (!z) {
                storyCircle.apply();
            }
        }
        for (int i15 = 0; i15 < arrayList2.size(); i15++) {
            TL_stories$StoryItem tL_stories$StoryItem5 = (TL_stories$StoryItem) arrayList2.get(i15);
            int i16 = 0;
            while (true) {
                if (i16 >= this.circles.size()) {
                    i16 = -1;
                    break;
                } else if (this.circles.get(i16).storyId == tL_stories$StoryItem5.id) {
                    break;
                } else {
                    i16++;
                }
            }
            if (i16 == -1) {
                tL_stories$StoryItem5.dialogId = this.dialogId;
                StoryCircle storyCircle2 = new StoryCircle(tL_stories$StoryItem5);
                storyCircle2.index = i15;
                storyCircle2.scale = 1.0f;
                storyCircle2.scaleAnimated.set(0.0f, true);
                storyCircle2.read = z3 || (tL_stories$PeerStories != null && tL_stories$StoryItem5.id <= tL_stories$PeerStories.max_read_id);
                if (!z) {
                    storyCircle2.apply();
                }
                this.circles.add(storyCircle2);
            }
        }
        this.mainCircle = null;
        int i17 = 0;
        while (true) {
            if (i17 >= this.circles.size()) {
                break;
            }
            StoryCircle storyCircle3 = this.circles.get(i17);
            if (storyCircle3.scale > 0.0f) {
                this.mainCircle = storyCircle3;
                break;
            }
            i17++;
        }
        ArrayList<StoriesController.UploadingStory> uploadingStories = this.storiesController.getUploadingStories(this.dialogId);
        this.uploadingStoriesCount = uploadingStories == null ? 0 : uploadingStories.size();
        int max2 = Math.max(arrayList2.size(), i2);
        if (max2 == 0 && this.uploadingStoriesCount != 0) {
            max2 = 1;
        }
        if (z2 && z) {
            if (max2 == this.count + 1 && this.unreadCount == i3 + 1) {
                animateNewStory();
            }
        }
        this.count = max2;
        this.titleDrawable.setText(max2 > 0 ? LocaleController.formatPluralString("Stories", max2, new Object[0]) : "", z && !LocaleController.isRTL);
        if (this.dialogId >= 0) {
            this.gradientTools.setUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId)), z);
        } else {
            this.gradientTools.setChat(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)), z);
        }
        invalidate();
    }

    public void setExpandProgress(float f) {
        if (this.expandProgress != f) {
            this.expandProgress = f;
            invalidate();
        }
    }

    public void setActionBarActionMode(float f) {
        if (Theme.isCurrentThemeDark()) {
            return;
        }
        this.actionBarProgress = f;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void vibrateNewStory() {
        if (SharedConfig.getDevicePerformanceClass() <= 0) {
            return;
        }
        try {
            performHapticFeedback(9, 1);
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.ProfileStoriesView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ProfileStoriesView.this.lambda$vibrateNewStory$0();
            }
        }, 180L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$vibrateNewStory$0() {
        try {
            performHapticFeedback(9, 1);
        } catch (Exception unused) {
        }
    }

    public void animateNewStory() {
        ValueAnimator valueAnimator = this.newStoryBounce;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        final boolean[] zArr = {false};
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.newStoryBounce = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.ProfileStoriesView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ProfileStoriesView.this.lambda$animateNewStory$1(zArr, valueAnimator2);
            }
        });
        this.newStoryBounce.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.ProfileStoriesView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                boolean[] zArr2 = zArr;
                if (!zArr2[0]) {
                    zArr2[0] = true;
                    ProfileStoriesView.this.vibrateNewStory();
                }
                ProfileStoriesView.this.newStoryBounceT = 1.0f;
                ProfileStoriesView.this.invalidate();
            }
        });
        this.newStoryBounce.setInterpolator(new OvershootInterpolator(3.0f));
        this.newStoryBounce.setDuration(400L);
        this.newStoryBounce.setStartDelay(120L);
        this.newStoryBounce.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateNewStory$1(boolean[] zArr, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!zArr[0] && floatValue > 0.2f) {
            zArr[0] = true;
            vibrateNewStory();
        }
        this.newStoryBounceT = Math.max(1.0f, floatValue);
        invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:160:0x06db, code lost:
        if (java.lang.Math.abs(r1.borderRect.centerX() - r7.borderRect.centerX()) > ((r1.borderRect.width() / 2.0f) + (r7.borderRect.width() / 2.0f))) goto L123;
     */
    /* JADX WARN: Removed duplicated region for block: B:168:0x06ed  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0730 A[SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        boolean z;
        Paint paint;
        float f;
        float f2;
        StoryCircle storyCircle;
        float f3;
        float f4;
        float f5;
        int i;
        float f6;
        Paint paint2;
        float f7;
        StoriesController.UploadingStory uploadingStory;
        Canvas canvas2 = canvas;
        this.rightAnimated.set(this.right);
        float clamp = Utilities.clamp((this.avatarContainer.getScaleX() - 1.0f) / 0.4f, 1.0f, 0.0f);
        float lerp = AndroidUtilities.lerp(AndroidUtilities.dpf2(4.0f), AndroidUtilities.dpf2(3.5f), clamp) * this.progressToInsets;
        float x = this.avatarContainer.getX() + (this.avatarContainer.getScaleX() * lerp);
        float y = this.avatarContainer.getY() + (this.avatarContainer.getScaleY() * lerp);
        float f8 = lerp * 2.0f;
        this.rect1.set(x, y, ((this.avatarContainer.getWidth() - f8) * this.avatarContainer.getScaleX()) + x, ((this.avatarContainer.getHeight() - f8) * this.avatarContainer.getScaleY()) + y);
        float f9 = this.left;
        int i2 = 0;
        while (true) {
            if (i2 >= this.circles.size()) {
                z = false;
                break;
            }
            StoryCircle storyCircle2 = this.circles.get(i2);
            float f10 = storyCircle2.scaleAnimated.set(storyCircle2.scale);
            storyCircle2.cachedScale = f10;
            if (f10 <= 0.0f && storyCircle2.scale <= 0.0f) {
                storyCircle2.destroy();
                this.circles.remove(i2);
                i2--;
            } else {
                storyCircle2.cachedIndex = storyCircle2.indexAnimated.set(storyCircle2.index);
                storyCircle2.cachedRead = storyCircle2.readAnimated.set(storyCircle2.read);
                if (i2 > 0 && this.circles.get(i2 - 1).cachedIndex > storyCircle2.cachedIndex) {
                    z = true;
                    break;
                }
            }
            i2++;
        }
        if (z) {
            Collections.sort(this.circles, ProfileStoriesView$$ExternalSyntheticLambda4.INSTANCE);
        }
        float clamp2 = Utilities.clamp(1.0f - (this.expandProgress / 0.2f), 1.0f, 0.0f);
        boolean isLastUploadingFailed = this.storiesController.isLastUploadingFailed(this.dialogId);
        boolean hasUploadingStories = this.storiesController.hasUploadingStories(this.dialogId);
        if (!hasUploadingStories && (uploadingStory = this.lastUploadingStory) != null && uploadingStory.canceled) {
            this.progressWasDrawn = false;
            this.progressIsDone = false;
            this.progressToUploading.set(false, true);
        }
        float lerp2 = AndroidUtilities.lerp(0.0f, this.progressToUploading.set((hasUploadingStories && !isLastUploadingFailed) || (this.progressWasDrawn && !this.progressIsDone)), this.fragmentTransitionProgress);
        canvas.save();
        float f11 = this.bounceScale;
        canvas2.scale(f11, f11, this.rect1.centerX(), this.rect1.centerY());
        float lerp3 = AndroidUtilities.lerp(this.rect1.centerY(), this.expandY, this.expandProgress);
        this.lastUploadingStory = null;
        if (lerp2 > 0.0f) {
            this.rect2.set(this.rect1);
            this.rect2.inset(-AndroidUtilities.dpf2(3.775f), -AndroidUtilities.dpf2(3.775f));
            paint = this.gradientTools.getPaint(this.rect2);
            if (this.radialProgress == null) {
                RadialProgress radialProgress = new RadialProgress(this);
                this.radialProgress = radialProgress;
                radialProgress.setBackground(null, true, false);
            }
            if (!this.storiesController.hasUploadingStories(this.dialogId) || this.storiesController.isLastUploadingFailed(this.dialogId)) {
                f7 = 1.0f;
            } else {
                ArrayList<StoriesController.UploadingStory> uploadingStories = this.storiesController.getUploadingStories(this.dialogId);
                if (uploadingStories != null) {
                    if (uploadingStories.size() > 0) {
                        this.lastUploadingStory = uploadingStories.get(0);
                    }
                    float f12 = 0.0f;
                    for (int i3 = 0; i3 < uploadingStories.size(); i3++) {
                        f12 += uploadingStories.get(i3).progress;
                    }
                    f7 = f12 / uploadingStories.size();
                } else {
                    f7 = 0.0f;
                }
            }
            this.radialProgress.setDiff(0);
            paint.setAlpha((int) (clamp2 * 255.0f * lerp2));
            paint.setStrokeWidth(AndroidUtilities.dpf2(2.33f));
            this.radialProgress.setPaint(paint);
            RadialProgress radialProgress2 = this.radialProgress;
            RectF rectF = this.rect2;
            radialProgress2.setProgressRect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            this.radialProgress.setProgress(Utilities.clamp(f7, 1.0f, 0.0f), true);
            if (this.avatarImage.drawAvatar) {
                canvas2 = canvas;
                this.radialProgress.draw(canvas2);
            } else {
                canvas2 = canvas;
            }
            this.progressWasDrawn = true;
            boolean z2 = this.progressIsDone;
            boolean z3 = this.radialProgress.getAnimatedProgress() >= 0.98f;
            this.progressIsDone = z3;
            if (z2 != z3) {
                this.segmentsCountAnimated.set(this.count, true);
                this.segmentsUnreadCountAnimated.set(this.unreadCount, true);
                animateBounce();
            }
        } else {
            this.progressWasDrawn = false;
            paint = null;
        }
        if (lerp2 < 1.0f) {
            f2 = Utilities.clamp(1.0f - (this.expandProgress / 0.2f), 1.0f, 0.0f) * (1.0f - lerp2);
            float f13 = this.segmentsCountAnimated.set(this.count);
            float f14 = this.segmentsUnreadCountAnimated.set(this.unreadCount);
            if (isLastUploadingFailed) {
                this.rect2.set(this.rect1);
                this.rect2.inset(-AndroidUtilities.dpf2(3.775f), -AndroidUtilities.dpf2(3.775f));
                Paint errorPaint = StoriesUtilities.getErrorPaint(this.rect2);
                errorPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                errorPaint.setAlpha((int) (f2 * 255.0f));
                canvas2.drawCircle(this.rect2.centerX(), this.rect2.centerY(), this.rect2.width() / 2.0f, errorPaint);
            } else if ((this.mainCircle != null || this.uploadingStoriesCount > 0) && f2 > 0.0f) {
                this.rect2.set(this.rect1);
                this.rect2.inset(-AndroidUtilities.dpf2(3.775f), -AndroidUtilities.dpf2(3.775f));
                this.rect3.set(this.rect1);
                this.rect3.inset(-AndroidUtilities.dpf2(3.41f), -AndroidUtilities.dpf2(3.41f));
                RectF rectF2 = this.rect2;
                RectF rectF3 = this.rect3;
                AndroidUtilities.lerp(rectF2, rectF3, clamp, rectF3);
                double dpf2 = AndroidUtilities.dpf2(4.23f);
                f = lerp3;
                double width = this.rect1.width();
                Double.isNaN(width);
                Double.isNaN(dpf2);
                float lerp4 = AndroidUtilities.lerp(0.0f, (float) ((dpf2 / (width * 3.141592653589793d)) * 360.0d), Utilities.clamp(f13 - 1.0f, 1.0f, 0.0f) * f2);
                int min = Math.min(this.count, 50);
                float min2 = Math.min(f13, 50.0f);
                int i4 = min > 20 ? 3 : 5;
                if (min <= 1) {
                    i4 = 0;
                }
                float lerp5 = AndroidUtilities.lerp(i4 * 2, lerp4, clamp);
                float max = (360.0f - (Math.max(0.0f, min2) * lerp5)) / Math.max(1.0f, min2);
                this.readPaint.setColor(ColorUtils.blendARGB(1526726655, 973078528, this.actionBarProgress));
                this.readPaintAlpha = this.readPaint.getAlpha();
                float f15 = (-90.0f) - (lerp5 / 2.0f);
                int i5 = 0;
                while (i5 < min) {
                    float f16 = i5;
                    float clamp3 = 1.0f - Utilities.clamp(f14 - f16, 1.0f, 0.0f);
                    float clamp4 = 1.0f - Utilities.clamp((min - min2) - f16, 1.0f, 0.0f);
                    if (clamp4 < 0.0f) {
                        i = i5;
                        f6 = max;
                    } else {
                        float f17 = i5 == 0 ? ((this.newStoryBounceT - 1.0f) / 2.5f) + 1.0f : 1.0f;
                        if (f17 != 1.0f) {
                            canvas.save();
                            canvas2.scale(f17, f17, this.rect2.centerX(), this.rect2.centerY());
                        }
                        if (clamp3 < 1.0f) {
                            paint2 = this.gradientTools.getPaint(this.rect2);
                            paint2.setAlpha((int) ((1.0f - clamp3) * 255.0f * f2));
                            paint2.setStrokeWidth(AndroidUtilities.dpf2(2.33f));
                            f5 = f17;
                            i = i5;
                            f6 = max;
                            canvas.drawArc(this.rect2, f15, (-max) * clamp4, false, paint2);
                        } else {
                            f5 = f17;
                            i = i5;
                            f6 = max;
                            paint2 = paint;
                        }
                        if (clamp3 > 0.0f) {
                            this.readPaint.setAlpha((int) (this.readPaintAlpha * clamp3 * f2));
                            this.readPaint.setStrokeWidth(AndroidUtilities.dpf2(1.5f));
                            canvas.drawArc(this.rect3, f15, (-f6) * clamp4, false, this.readPaint);
                        }
                        if (f5 != 1.0f) {
                            canvas.restore();
                        }
                        f15 -= (f6 * clamp4) + (clamp4 * lerp5);
                        paint = paint2;
                    }
                    i5 = i + 1;
                    max = f6;
                }
            }
            f = lerp3;
        } else {
            f = lerp3;
            f2 = clamp2;
        }
        float expandRight = getExpandRight();
        if (this.expandProgress > 0.0f && f2 < 1.0f) {
            this.w = 0.0f;
            for (int i6 = 0; i6 < this.circles.size(); i6++) {
                this.w += AndroidUtilities.dp(14.0f) * this.circles.get(i6).cachedScale;
            }
            float f18 = 0.0f;
            for (int i7 = 0; i7 < this.circles.size(); i7++) {
                StoryCircle storyCircle3 = this.circles.get(i7);
                float f19 = storyCircle3.cachedScale;
                float f20 = storyCircle3.cachedRead;
                float dp = (AndroidUtilities.dp(28.0f) / 2.0f) * f19;
                float f21 = (expandRight - this.w) + dp + f18;
                f18 += AndroidUtilities.dp(18.0f) * f19;
                float f22 = f21 + dp;
                f9 = Math.max(f9, f22);
                this.rect2.set(f21 - dp, f - dp, f22, f + dp);
                lerpCentered(this.rect1, this.rect2, this.expandProgress, this.rect3);
                storyCircle3.cachedRect.set(this.rect3);
                storyCircle3.borderRect.set(this.rect3);
                float f23 = (-AndroidUtilities.lerp(AndroidUtilities.dpf2(2.66f), AndroidUtilities.lerp(AndroidUtilities.dpf2(1.33f), AndroidUtilities.dpf2(2.33f), this.expandProgress), f20 * this.expandProgress)) * f19;
                storyCircle3.borderRect.inset(f23, f23);
            }
            this.readPaint.setColor(ColorUtils.blendARGB(1526726655, -2135178036, this.expandProgress));
            this.readPaintAlpha = this.readPaint.getAlpha();
            Paint paint3 = this.gradientTools.getPaint(this.rect2);
            paint3.setStrokeWidth(AndroidUtilities.lerp(AndroidUtilities.dpf2(2.33f), AndroidUtilities.dpf2(1.5f), this.expandProgress));
            this.readPaint.setStrokeWidth(AndroidUtilities.lerp(AndroidUtilities.dpf2(1.125f), AndroidUtilities.dpf2(1.5f), this.expandProgress));
            if (this.expandProgress > 0.0f) {
                for (int i8 = 0; i8 < this.circles.size(); i8++) {
                    StoryCircle storyCircle4 = this.circles.get(i8);
                    int alpha = this.whitePaint.getAlpha();
                    this.whitePaint.setAlpha((int) (alpha * this.expandProgress));
                    canvas2.drawCircle(storyCircle4.cachedRect.centerX(), storyCircle4.cachedRect.centerY(), (Math.min(storyCircle4.cachedRect.width(), storyCircle4.cachedRect.height()) / 2.0f) + (AndroidUtilities.lerp(AndroidUtilities.dpf2(2.66f) + (paint3.getStrokeWidth() / 2.0f), AndroidUtilities.dpf2(2.33f) - (this.readPaint.getStrokeWidth() / 2.0f), storyCircle4.cachedRead) * this.expandProgress), this.whitePaint);
                    this.whitePaint.setAlpha(alpha);
                }
            }
            int i9 = 0;
            while (i9 < this.circles.size()) {
                StoryCircle storyCircle5 = this.circles.get(i9);
                int i10 = i9 - 2;
                int i11 = i9 - 1;
                StoryCircle nearest = nearest(i10 >= 0 ? this.circles.get(i10) : null, i11 >= 0 ? this.circles.get(i11) : null, storyCircle5);
                int i12 = i9 + 1;
                int i13 = i9 + 2;
                StoryCircle nearest2 = nearest(i12 < this.circles.size() ? this.circles.get(i12) : null, i13 < this.circles.size() ? this.circles.get(i13) : null, storyCircle5);
                StoryCircle storyCircle6 = (nearest == null || (Math.abs(nearest.borderRect.centerX() - storyCircle5.borderRect.centerX()) >= Math.abs((storyCircle5.borderRect.width() / 2.0f) - (nearest.borderRect.width() / 2.0f)) && Math.abs(nearest.borderRect.centerX() - storyCircle5.borderRect.centerX()) <= (nearest.borderRect.width() / 2.0f) + (storyCircle5.borderRect.width() / 2.0f))) ? nearest : null;
                if (nearest2 != null) {
                    if (Math.abs(nearest2.borderRect.centerX() - storyCircle5.borderRect.centerX()) >= Math.abs((storyCircle5.borderRect.width() / 2.0f) - (nearest2.borderRect.width() / 2.0f))) {
                    }
                    storyCircle = null;
                    f3 = storyCircle5.cachedRead;
                    if (f3 < 1.0f) {
                        paint3.setAlpha((int) (storyCircle5.cachedScale * 255.0f * (1.0f - f3) * (1.0f - f2)));
                        drawArcs(canvas, storyCircle6, storyCircle5, storyCircle, paint3);
                    }
                    f4 = storyCircle5.cachedRead;
                    if (f4 <= 0.0f) {
                        this.readPaint.setAlpha((int) (this.readPaintAlpha * storyCircle5.cachedScale * f4 * (1.0f - f2)));
                        drawArcs(canvas, storyCircle6, storyCircle5, storyCircle, this.readPaint);
                    }
                    i9 = i12;
                }
                storyCircle = nearest2;
                f3 = storyCircle5.cachedRead;
                if (f3 < 1.0f) {
                }
                f4 = storyCircle5.cachedRead;
                if (f4 <= 0.0f) {
                }
                i9 = i12;
            }
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (this.expandProgress * 255.0f * (1.0f - f2)), 31);
            for (int size = this.circles.size() - 1; size >= 0; size--) {
                StoryCircle storyCircle7 = this.circles.get(size);
                if (storyCircle7.imageReceiver.getVisible()) {
                    int saveCount = canvas.getSaveCount();
                    int i14 = size - 1;
                    StoryCircle storyCircle8 = i14 >= 0 ? this.circles.get(i14) : null;
                    int i15 = size - 2;
                    clipCircle(canvas2, storyCircle7, nearest(storyCircle8, i15 >= 0 ? this.circles.get(i15) : null, storyCircle7));
                    storyCircle7.imageReceiver.setImageCoords(storyCircle7.cachedRect);
                    storyCircle7.imageReceiver.draw(canvas2);
                    canvas2.restoreToCount(saveCount);
                }
            }
            canvas.restore();
            paint = paint3;
        }
        if (paint != null) {
            paint.setStrokeWidth(AndroidUtilities.dpf2(2.3f));
        }
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$dispatchDraw$2(StoryCircle storyCircle, StoryCircle storyCircle2) {
        return (int) (storyCircle2.cachedIndex - storyCircle.cachedIndex);
    }

    private void animateBounce() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 1.05f);
        ofFloat.setDuration(100L);
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.05f, 1.0f);
        ofFloat2.setDuration(250L);
        ofFloat2.setInterpolator(new OvershootInterpolator());
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.ProfileStoriesView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProfileStoriesView.this.lambda$animateBounce$3(valueAnimator);
            }
        };
        ofFloat.addUpdateListener(animatorUpdateListener);
        ofFloat2.addUpdateListener(animatorUpdateListener);
        animatorSet.playSequentially(ofFloat, ofFloat2);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.ProfileStoriesView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ProfileStoriesView.this.avatarImage.bounceScale = ProfileStoriesView.this.bounceScale = 1.0f;
                ProfileStoriesView.this.avatarImage.invalidate();
                ProfileStoriesView.this.invalidate();
            }
        });
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateBounce$3(ValueAnimator valueAnimator) {
        ProfileActivity.AvatarImageView avatarImageView = this.avatarImage;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.bounceScale = floatValue;
        avatarImageView.bounceScale = floatValue;
        this.avatarImage.invalidate();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clipCircle(Canvas canvas, StoryCircle storyCircle, StoryCircle storyCircle2) {
        if (storyCircle2 == null) {
            return;
        }
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(storyCircle2.cachedRect);
        float f = -(AndroidUtilities.dpf2(1.66f) * storyCircle2.cachedScale);
        rectF.inset(f, f);
        float centerX = storyCircle2.cachedRect.centerX();
        float width = storyCircle2.cachedRect.width() / 2.0f;
        float centerX2 = storyCircle.cachedRect.centerX();
        float width2 = storyCircle.cachedRect.width() / 2.0f;
        this.clipPath.rewind();
        if (centerX > centerX2) {
            float degrees = (float) Math.toDegrees(Math.acos(Math.abs((((centerX - width) + (centerX2 + width2)) / 2.0f) - centerX2) / width2));
            this.clipPath.arcTo(rectF, 180.0f + degrees, (-degrees) * 2.0f);
            this.clipPath.arcTo(storyCircle.cachedRect, degrees, 360.0f - (2.0f * degrees));
        } else {
            float degrees2 = (float) Math.toDegrees(Math.acos(Math.abs((((centerX + width) + (centerX2 - width2)) / 2.0f) - centerX2) / width2));
            float f2 = 2.0f * degrees2;
            this.clipPath.arcTo(rectF, -degrees2, f2);
            this.clipPath.arcTo(storyCircle.cachedRect, 180.0f - degrees2, -(360.0f - f2));
        }
        this.clipPath.close();
        canvas.save();
        canvas.clipPath(this.clipPath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StoryCircle nearest(StoryCircle storyCircle, StoryCircle storyCircle2, StoryCircle storyCircle3) {
        if (storyCircle3 != null) {
            if (storyCircle == null && storyCircle2 == null) {
                return null;
            }
            return (storyCircle == null || storyCircle2 == null) ? storyCircle != null ? storyCircle : storyCircle2 : Math.min(Math.abs(storyCircle.borderRect.left - storyCircle3.borderRect.right), Math.abs(storyCircle.borderRect.right - storyCircle3.borderRect.left)) > Math.min(Math.abs(storyCircle2.borderRect.left - storyCircle3.borderRect.right), Math.abs(storyCircle2.borderRect.right - storyCircle3.borderRect.left)) ? storyCircle : storyCircle2;
        }
        return null;
    }

    private void drawArcs(Canvas canvas, StoryCircle storyCircle, StoryCircle storyCircle2, StoryCircle storyCircle3, Paint paint) {
        double degrees;
        double degrees2;
        StoryCircle storyCircle4 = storyCircle;
        if (storyCircle4 == null && storyCircle3 == null) {
            canvas.drawArc(storyCircle2.borderRect, 0.0f, 360.0f, false, paint);
        } else if (storyCircle4 == null || storyCircle3 == null) {
            if (storyCircle4 == null && storyCircle3 == null) {
                return;
            }
            if (storyCircle4 == null) {
                storyCircle4 = storyCircle3;
            }
            float centerX = storyCircle4.borderRect.centerX();
            float width = storyCircle4.borderRect.width() / 2.0f;
            float centerX2 = storyCircle2.borderRect.centerX();
            float width2 = storyCircle2.borderRect.width() / 2.0f;
            if (Math.abs(centerX - centerX2) > width + width2) {
                canvas.drawArc(storyCircle2.borderRect, 0.0f, 360.0f, false, paint);
            } else if (centerX > centerX2) {
                float degrees3 = (float) Math.toDegrees(Math.acos(Math.abs((((centerX - width) + (centerX2 + width2)) / 2.0f) - centerX2) / width2));
                canvas.drawArc(storyCircle2.borderRect, degrees3, 360.0f - (2.0f * degrees3), false, paint);
            } else {
                float degrees4 = (float) Math.toDegrees(Math.acos(Math.abs((((centerX + width) + (centerX2 - width2)) / 2.0f) - centerX2) / width2));
                canvas.drawArc(storyCircle2.borderRect, degrees4 + 180.0f, 360.0f - (degrees4 * 2.0f), false, paint);
            }
        } else {
            float centerX3 = storyCircle4.borderRect.centerX();
            float width3 = storyCircle4.borderRect.width() / 2.0f;
            float centerX4 = storyCircle2.borderRect.centerX();
            float width4 = storyCircle2.borderRect.width() / 2.0f;
            float centerX5 = storyCircle3.borderRect.centerX();
            float width5 = storyCircle3.borderRect.width() / 2.0f;
            boolean z = centerX3 > centerX4;
            if (z) {
                degrees = Math.toDegrees(Math.acos(Math.abs((((centerX3 - width3) + (centerX4 + width4)) / 2.0f) - centerX4) / width4));
            } else {
                degrees = Math.toDegrees(Math.acos(Math.abs((((centerX3 + width3) + (centerX4 - width4)) / 2.0f) - centerX4) / width4));
            }
            float f = (float) degrees;
            boolean z2 = centerX5 > centerX4;
            if (z2) {
                degrees2 = Math.toDegrees(Math.acos(Math.abs((((centerX5 - width5) + (centerX4 + width4)) / 2.0f) - centerX4) / width4));
            } else {
                degrees2 = Math.toDegrees(Math.acos(Math.abs((((centerX5 + width5) + (centerX4 - width4)) / 2.0f) - centerX4) / width4));
            }
            float f2 = (float) degrees2;
            if (z && z2) {
                float max = Math.max(f, f2);
                canvas.drawArc(storyCircle2.borderRect, max, 360.0f - (2.0f * max), false, paint);
            } else if (z) {
                canvas.drawArc(storyCircle2.borderRect, f2 + 180.0f, 180.0f - (f + f2), false, paint);
                canvas.drawArc(storyCircle2.borderRect, f, (180.0f - f2) - f, false, paint);
            } else if (z2) {
                canvas.drawArc(storyCircle2.borderRect, f + 180.0f, 180.0f - (f2 + f), false, paint);
                canvas.drawArc(storyCircle2.borderRect, f2, (180.0f - f2) - f, false, paint);
            } else {
                float max2 = Math.max(f, f2);
                canvas.drawArc(storyCircle2.borderRect, max2 + 180.0f, 360.0f - (max2 * 2.0f), false, paint);
            }
        }
    }

    private void lerpCentered(RectF rectF, RectF rectF2, float f, RectF rectF3) {
        float lerp = AndroidUtilities.lerp(rectF.centerX(), rectF2.centerX(), f);
        float lerp2 = AndroidUtilities.lerp(rectF.centerY(), rectF2.centerY(), f);
        float lerp3 = AndroidUtilities.lerp(Math.min(rectF.width(), rectF.height()), Math.min(rectF2.width(), rectF2.height()), f) / 2.0f;
        rectF3.set(lerp - lerp3, lerp2 - lerp3, lerp + lerp3, lerp2 + lerp3);
    }

    public void setBounds(float f, float f2, float f3, boolean z) {
        boolean z2 = Math.abs(f - this.left) > 0.1f || Math.abs(f2 - this.right) > 0.1f || Math.abs(f3 - this.cy) > 0.1f;
        this.left = f;
        this.right = f2;
        if (!z) {
            this.rightAnimated.set(f2, true);
        }
        this.cy = f3;
        if (z2) {
            invalidate();
        }
    }

    public void setExpandCoords(float f, boolean z, float f2) {
        this.expandRight = f;
        this.expandRightPad = z;
        this.expandY = f2;
        invalidate();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.storiesUpdated) {
            updateStories(true, true);
        }
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
        for (int i = 0; i < this.circles.size(); i++) {
            this.circles.get(i).imageReceiver.onAttachedToWindow();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesUpdated);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
        for (int i = 0; i < this.circles.size(); i++) {
            this.circles.get(i).imageReceiver.onDetachedFromWindow();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesUpdated);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 3 implements StoryViewer.PlaceProvider {
        @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
        public /* synthetic */ void loadNext(boolean z) {
            StoryViewer.PlaceProvider.-CC.$default$loadNext(this, z);
        }

        3() {
        }

        @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
        public boolean findView(long j, int i, int i2, int i3, StoryViewer.TransitionViewHolder transitionViewHolder) {
            ImageReceiver imageReceiver;
            final StoryCircle storyCircle;
            final StoryCircle storyCircle2;
            transitionViewHolder.avatarImage = null;
            transitionViewHolder.storyImage = null;
            if (ProfileStoriesView.this.expandProgress < 0.2f) {
                transitionViewHolder.avatarImage = ProfileStoriesView.this.avatarImage.getImageReceiver();
                transitionViewHolder.storyImage = null;
                transitionViewHolder.view = ProfileStoriesView.this.avatarImage;
                transitionViewHolder.clipTop = 0.0f;
                transitionViewHolder.clipBottom = AndroidUtilities.displaySize.y;
                transitionViewHolder.clipParent = (View) ProfileStoriesView.this.getParent();
                transitionViewHolder.radialProgressUpload = ProfileStoriesView.this.radialProgress;
                return true;
            }
            int i4 = 0;
            while (true) {
                if (i4 >= ProfileStoriesView.this.circles.size()) {
                    imageReceiver = null;
                    storyCircle = null;
                    storyCircle2 = null;
                    break;
                }
                StoryCircle storyCircle3 = (StoryCircle) ProfileStoriesView.this.circles.get(i4);
                if (storyCircle3.scale < 1.0f || storyCircle3.storyId != i2) {
                    i4++;
                } else {
                    ProfileStoriesView profileStoriesView = ProfileStoriesView.this;
                    int i5 = i4 - 1;
                    int i6 = i4 - 2;
                    StoryCircle nearest = profileStoriesView.nearest(i5 >= 0 ? (StoryCircle) profileStoriesView.circles.get(i5) : null, i6 >= 0 ? (StoryCircle) ProfileStoriesView.this.circles.get(i6) : null, storyCircle3);
                    imageReceiver = storyCircle3.imageReceiver;
                    storyCircle2 = nearest;
                    storyCircle = storyCircle3;
                }
            }
            if (imageReceiver == null) {
                return false;
            }
            transitionViewHolder.storyImage = imageReceiver;
            transitionViewHolder.avatarImage = null;
            ProfileStoriesView profileStoriesView2 = ProfileStoriesView.this;
            transitionViewHolder.view = profileStoriesView2;
            transitionViewHolder.clipTop = 0.0f;
            transitionViewHolder.clipBottom = AndroidUtilities.displaySize.y;
            transitionViewHolder.clipParent = (View) profileStoriesView2.getParent();
            if (storyCircle != null && storyCircle2 != null) {
                final RectF rectF = new RectF(storyCircle.cachedRect);
                final RectF rectF2 = new RectF(storyCircle2.cachedRect);
                transitionViewHolder.drawClip = new StoryViewer.HolderClip() { // from class: org.telegram.ui.Stories.ProfileStoriesView$3$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Stories.StoryViewer.HolderClip
                    public final void clip(Canvas canvas, RectF rectF3, float f, boolean z) {
                        ProfileStoriesView.3.this.lambda$findView$0(rectF, storyCircle, rectF2, storyCircle2, canvas, rectF3, f, z);
                    }
                };
            } else {
                transitionViewHolder.drawClip = null;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$findView$0(RectF rectF, StoryCircle storyCircle, RectF rectF2, StoryCircle storyCircle2, Canvas canvas, RectF rectF3, float f, boolean z) {
            rectF.set(storyCircle.cachedRect);
            rectF2.set(storyCircle2.cachedRect);
            storyCircle.cachedRect.set(rectF3);
            try {
                float width = rectF3.width() / rectF.width();
                float centerX = rectF3.centerX() - ((rectF.centerX() - rectF2.centerX()) * (((1.0f - f) * 2.0f) + width));
                float centerY = rectF3.centerY();
                float width2 = (rectF2.width() / 2.0f) * width;
                float height = (rectF2.height() / 2.0f) * width;
                storyCircle2.cachedRect.set(centerX - width2, centerY - height, centerX + width2, centerY + height);
            } catch (Exception unused) {
            }
            ProfileStoriesView.this.clipCircle(canvas, storyCircle, storyCircle2);
            storyCircle.cachedRect.set(rectF);
            storyCircle2.cachedRect.set(rectF2);
        }

        @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
        public void preLayout(long j, int i, Runnable runnable) {
            ProfileStoriesView.this.updateStories(true, false);
            runnable.run();
        }
    }

    public boolean isEmpty() {
        return this.circles.isEmpty();
    }

    private float getExpandRight() {
        return this.expandRight - (this.expandRightPadAnimated.set(this.expandRightPad) * AndroidUtilities.dp(71.0f));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (this.expandProgress < 0.9f) {
            z = this.rect2.contains(motionEvent.getX(), motionEvent.getY());
        } else {
            z = motionEvent.getX() >= (getExpandRight() - this.w) - ((float) AndroidUtilities.dp(32.0f)) && motionEvent.getX() <= getExpandRight() + ((float) AndroidUtilities.dp(32.0f)) && Math.abs(motionEvent.getY() - this.expandY) < ((float) AndroidUtilities.dp(32.0f));
        }
        if (z && motionEvent.getAction() == 0) {
            this.tapTime = System.currentTimeMillis();
            this.tapX = motionEvent.getX();
            this.tapY = motionEvent.getY();
            AndroidUtilities.cancelRunOnUIThread(this.onLongPressRunnable);
            AndroidUtilities.runOnUIThread(this.onLongPressRunnable, ViewConfiguration.getLongPressTimeout());
            return true;
        }
        if (motionEvent.getAction() == 1) {
            AndroidUtilities.cancelRunOnUIThread(this.onLongPressRunnable);
            if (z && System.currentTimeMillis() - this.tapTime <= ViewConfiguration.getTapTimeout() && MathUtils.distance(this.tapX, this.tapY, motionEvent.getX(), motionEvent.getY()) <= AndroidUtilities.dp(12.0f) && (this.storiesController.hasUploadingStories(this.dialogId) || this.storiesController.hasStories(this.dialogId) || !this.circles.isEmpty())) {
                onTap(this.provider);
                return true;
            }
        } else if (motionEvent.getAction() == 3) {
            this.tapTime = -1L;
            AndroidUtilities.cancelRunOnUIThread(this.onLongPressRunnable);
        }
        return super.onTouchEvent(motionEvent);
    }

    @Keep
    public void setFragmentTransitionProgress(float f) {
        if (this.fragmentTransitionProgress == f) {
            return;
        }
        this.fragmentTransitionProgress = f;
        invalidate();
    }

    @Keep
    public float getFragmentTransitionProgress() {
        return this.fragmentTransitionProgress;
    }
}
