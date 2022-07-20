package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.CrossOutDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.GroupCallFullscreenAdapter;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.GroupCallActivity;
import org.webrtc.TextureViewRenderer;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class GroupCallRenderersContainer extends FrameLayout {
    int animationIndex;
    private final ArrayList<GroupCallMiniTextureView> attachedRenderers;
    private final ImageView backButton;
    ChatObject.Call call;
    private boolean canZoomGesture;
    private boolean drawFirst;
    private boolean drawRenderesOnly;
    ValueAnimator fullscreenAnimator;
    private final RecyclerView fullscreenListView;
    public ChatObject.VideoParticipant fullscreenParticipant;
    public long fullscreenPeerId;
    public GroupCallMiniTextureView fullscreenTextureView;
    GroupCallActivity groupCallActivity;
    public boolean hasPinnedVideo;
    boolean hideUiRunnableIsScheduled;
    public boolean inFullscreenMode;
    public boolean inLayout;
    private boolean isInPinchToZoomTouchMode;
    private boolean isTablet;
    public long lastUpdateTime;
    long lastUpdateTooltipTime;
    private final RecyclerView listView;
    public int listWidth;
    boolean maybeSwipeToBackGesture;
    private boolean notDrawRenderes;
    private GroupCallMiniTextureView outFullscreenTextureView;
    private final ImageView pinButton;
    View pinContainer;
    CrossOutDrawable pinDrawable;
    TextView pinTextView;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartCenterX;
    private float pinchStartCenterY;
    private float pinchStartDistance;
    private float pinchTranslationX;
    private float pinchTranslationY;
    public ImageView pipView;
    private int pointerId1;
    private int pointerId2;
    public float progressToFullscreenMode;
    float progressToHideUi;
    public float progressToScrimView;
    ValueAnimator replaceFullscreenViewAnimator;
    Drawable rightShadowDrawable;
    private final View rightShadowView;
    private boolean showSpeakingMembersToast;
    private float showSpeakingMembersToastProgress;
    private final AvatarsImageView speakingMembersAvatars;
    private final TextView speakingMembersText;
    private final FrameLayout speakingMembersToast;
    private float speakingMembersToastFromLeft;
    private float speakingMembersToastFromRight;
    private float speakingMembersToastFromTextLeft;
    private long speakingToastPeerId;
    ValueAnimator swipeToBackAnimator;
    float swipeToBackDy;
    boolean swipeToBackGesture;
    public boolean swipedBack;
    boolean tapGesture;
    long tapTime;
    float tapX;
    float tapY;
    Drawable topShadowDrawable;
    private final View topShadowView;
    private final int touchSlop;
    TextView unpinTextView;
    Runnable updateTooltipRunnbale;
    ValueAnimator zoomBackAnimator;
    private boolean zoomStarted;
    private LongSparseIntArray attachedPeerIds = new LongSparseIntArray();
    private float speakingMembersToastChangeProgress = 1.0f;
    boolean uiVisible = true;
    Runnable hideUiRunnable = new AnonymousClass1();
    float pinchScale = 1.0f;
    public UndoView[] undoView = new UndoView[2];

    protected void onBackPressed() {
    }

    protected void onFullScreenModeChanged(boolean z) {
    }

    protected void onUiVisibilityChanged() {
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            GroupCallRenderersContainer.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!GroupCallRenderersContainer.this.canHideUI()) {
                AndroidUtilities.runOnUIThread(GroupCallRenderersContainer.this.hideUiRunnable, 3000L);
                return;
            }
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.hideUiRunnableIsScheduled = false;
            groupCallRenderersContainer.setUiVisible(false);
        }
    }

    public GroupCallRenderersContainer(Context context, RecyclerView recyclerView, RecyclerView recyclerView2, ArrayList<GroupCallMiniTextureView> arrayList, ChatObject.Call call, GroupCallActivity groupCallActivity) {
        super(context);
        this.listView = recyclerView;
        this.fullscreenListView = recyclerView2;
        this.attachedRenderers = arrayList;
        this.call = call;
        this.groupCallActivity = groupCallActivity;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.backButton = anonymousClass2;
        BackDrawable backDrawable = new BackDrawable(false);
        backDrawable.setColor(-1);
        anonymousClass2.setImageDrawable(backDrawable);
        anonymousClass2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        anonymousClass2.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        anonymousClass2.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(-1, 55)));
        View view = new View(context);
        this.topShadowView = view;
        Drawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, ColorUtils.setAlphaComponent(-16777216, 114)});
        this.topShadowDrawable = gradientDrawable;
        view.setBackground(gradientDrawable);
        addView(view, LayoutHelper.createFrame(-1, 120.0f));
        View view2 = new View(context);
        this.rightShadowView = view2;
        Drawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0, ColorUtils.setAlphaComponent(-16777216, 114)});
        this.rightShadowDrawable = gradientDrawable2;
        view2.setBackground(gradientDrawable2);
        view2.setVisibility((call == null || !isRtmpStream()) ? 8 : 0);
        addView(view2, LayoutHelper.createFrame(160, -1, 5));
        addView(anonymousClass2, LayoutHelper.createFrame(56, -1, 51));
        anonymousClass2.setOnClickListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda5(this));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.pinButton = anonymousClass3;
        Drawable createSimpleSelectorRoundRectDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100));
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, createSimpleSelectorRoundRectDrawable);
        this.pinContainer = anonymousClass4;
        anonymousClass4.setOnClickListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda4(this));
        createSimpleSelectorRoundRectDrawable.setCallback(this.pinContainer);
        addView(this.pinContainer);
        CrossOutDrawable crossOutDrawable = new CrossOutDrawable(context, 2131165861, null);
        this.pinDrawable = crossOutDrawable;
        crossOutDrawable.setOffsets(-AndroidUtilities.dp(1.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(1.0f));
        anonymousClass3.setImageDrawable(this.pinDrawable);
        anonymousClass3.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        addView(anonymousClass3, LayoutHelper.createFrame(56, -1, 51));
        TextView textView = new TextView(context);
        this.pinTextView = textView;
        textView.setTextColor(-1);
        this.pinTextView.setTextSize(1, 15.0f);
        this.pinTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.pinTextView.setText(LocaleController.getString("CallVideoPin", 2131624805));
        TextView textView2 = new TextView(context);
        this.unpinTextView = textView2;
        textView2.setTextColor(-1);
        this.unpinTextView.setTextSize(1, 15.0f);
        this.unpinTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.unpinTextView.setText(LocaleController.getString("CallVideoUnpin", 2131624807));
        addView(this.pinTextView, LayoutHelper.createFrame(-2, -2, 51));
        addView(this.unpinTextView, LayoutHelper.createFrame(-2, -2, 51));
        ImageView imageView = new ImageView(context);
        this.pipView = imageView;
        imageView.setVisibility(4);
        this.pipView.setAlpha(0.0f);
        this.pipView.setImageResource(2131165478);
        this.pipView.setContentDescription(LocaleController.getString(2131624040));
        int dp = AndroidUtilities.dp(4.0f);
        this.pipView.setPadding(dp, dp, dp, dp);
        this.pipView.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(-1, 55)));
        this.pipView.setOnClickListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda6(this, groupCallActivity));
        addView(this.pipView, LayoutHelper.createFrame(32, 32.0f, 53, 12.0f, 12.0f, 12.0f, 12.0f));
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_listViewBackground"), 204)));
        this.speakingMembersToast = anonymousClass5;
        AvatarsImageView avatarsImageView = new AvatarsImageView(context, true);
        this.speakingMembersAvatars = avatarsImageView;
        avatarsImageView.setStyle(10);
        anonymousClass5.setClipChildren(false);
        anonymousClass5.setClipToPadding(false);
        anonymousClass5.addView(avatarsImageView, LayoutHelper.createFrame(100, 32.0f, 16, 0.0f, 0.0f, 0.0f, 0.0f));
        TextView textView3 = new TextView(context);
        this.speakingMembersText = textView3;
        textView3.setTextSize(1, 14.0f);
        textView3.setTextColor(-1);
        textView3.setLines(1);
        textView3.setEllipsize(TextUtils.TruncateAt.END);
        anonymousClass5.addView(textView3, LayoutHelper.createFrame(-2, -2, 16));
        addView(anonymousClass5, LayoutHelper.createFrame(-2, 36.0f, 1, 0.0f, 0.0f, 0.0f, 0.0f));
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        for (int i = 0; i < 2; i++) {
            this.undoView[i] = new AnonymousClass6(context);
            this.undoView[i].setHideAnimationType(2);
            this.undoView[i].setAdditionalTranslationY(AndroidUtilities.dp(10.0f));
            addView(this.undoView[i], LayoutHelper.createFrame(-1, -2.0f, 80, 16.0f, 0.0f, 0.0f, 8.0f));
        }
        this.pinContainer.setVisibility(8);
        setIsTablet(GroupCallActivity.isTabletMode);
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends ImageView {
        AnonymousClass2(GroupCallRenderersContainer groupCallRenderersContainer, Context context) {
            super(context);
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(ActionBar.getCurrentActionBarHeight(), 1073741824));
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        onBackPressed();
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            GroupCallRenderersContainer.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            GroupCallRenderersContainer.this.pinContainer.invalidate();
            GroupCallRenderersContainer.this.invalidate();
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(ActionBar.getCurrentActionBarHeight(), 1073741824));
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends View {
        final /* synthetic */ Drawable val$pinRippleDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, Drawable drawable) {
            super(context);
            GroupCallRenderersContainer.this = r1;
            this.val$pinRippleDrawable = drawable;
        }

        @Override // android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            this.val$pinRippleDrawable.setState(getDrawableState());
        }

        @Override // android.view.View
        public boolean verifyDrawable(Drawable drawable) {
            return this.val$pinRippleDrawable == drawable || super.verifyDrawable(drawable);
        }

        @Override // android.view.View
        public void jumpDrawablesToCurrentState() {
            super.jumpDrawablesToCurrentState();
            this.val$pinRippleDrawable.jumpToCurrentState();
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            float measuredWidth = (GroupCallRenderersContainer.this.pinTextView.getMeasuredWidth() * (1.0f - GroupCallRenderersContainer.this.pinDrawable.getProgress())) + (GroupCallRenderersContainer.this.unpinTextView.getMeasuredWidth() * GroupCallRenderersContainer.this.pinDrawable.getProgress());
            canvas.save();
            this.val$pinRippleDrawable.setBounds(0, 0, AndroidUtilities.dp(50.0f) + ((int) measuredWidth), getMeasuredHeight());
            this.val$pinRippleDrawable.draw(canvas);
            super.dispatchDraw(canvas);
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        if (this.inFullscreenMode) {
            boolean z = !this.hasPinnedVideo;
            this.hasPinnedVideo = z;
            this.pinDrawable.setCrossOut(z, true);
            requestLayout();
        }
    }

    public /* synthetic */ void lambda$new$2(GroupCallActivity groupCallActivity, View view) {
        if (isRtmpStream()) {
            if (AndroidUtilities.checkInlinePermissions(groupCallActivity.getParentActivity())) {
                RTMPStreamPipOverlay.show();
                groupCallActivity.dismiss();
                return;
            }
            AlertsCreator.createDrawOverlayPermissionDialog(groupCallActivity.getParentActivity(), null).show();
        } else if (AndroidUtilities.checkInlinePermissions(groupCallActivity.getParentActivity())) {
            GroupCallPip.clearForce();
            groupCallActivity.dismiss();
        } else {
            AlertsCreator.createDrawOverlayGroupCallPermissionDialog(getContext()).show();
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends FrameLayout {
        final /* synthetic */ Drawable val$toastBackgroundDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, Drawable drawable) {
            super(context);
            GroupCallRenderersContainer.this = r1;
            this.val$toastBackgroundDrawable = drawable;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (GroupCallRenderersContainer.this.speakingMembersToastChangeProgress != 1.0f) {
                float interpolation = 1.0f - CubicBezierInterpolator.DEFAULT.getInterpolation(GroupCallRenderersContainer.this.speakingMembersToastChangeProgress);
                float left = (GroupCallRenderersContainer.this.speakingMembersToastFromLeft - getLeft()) * interpolation;
                float left2 = (GroupCallRenderersContainer.this.speakingMembersToastFromTextLeft - GroupCallRenderersContainer.this.speakingMembersText.getLeft()) * interpolation;
                this.val$toastBackgroundDrawable.setBounds((int) left, 0, getMeasuredWidth() + ((int) ((GroupCallRenderersContainer.this.speakingMembersToastFromRight - getRight()) * interpolation)), getMeasuredHeight());
                GroupCallRenderersContainer.this.speakingMembersAvatars.setTranslationX(left);
                GroupCallRenderersContainer.this.speakingMembersText.setTranslationX(-left2);
            } else {
                this.val$toastBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                GroupCallRenderersContainer.this.speakingMembersAvatars.setTranslationX(0.0f);
                GroupCallRenderersContainer.this.speakingMembersText.setTranslationX(0.0f);
            }
            this.val$toastBackgroundDrawable.draw(canvas);
            super.dispatchDraw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends UndoView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            GroupCallRenderersContainer.this = r1;
        }

        @Override // org.telegram.ui.Components.UndoView, android.view.View
        public void invalidate() {
            super.invalidate();
            GroupCallRenderersContainer.this.invalidate();
        }
    }

    private boolean isRtmpStream() {
        ChatObject.Call call = this.call;
        return call != null && call.call.rtmp_stream;
    }

    public void setIsTablet(boolean z) {
        if (this.isTablet != z) {
            this.isTablet = z;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.backButton.getLayoutParams();
            layoutParams.gravity = z ? 85 : 51;
            layoutParams.rightMargin = z ? AndroidUtilities.dp(328.0f) : 0;
            layoutParams.bottomMargin = z ? -AndroidUtilities.dp(8.0f) : 0;
            if (this.isTablet) {
                this.backButton.setImageDrawable(ContextCompat.getDrawable(getContext(), 2131165667));
                return;
            }
            BackDrawable backDrawable = new BackDrawable(false);
            backDrawable.setColor(-1);
            this.backButton.setImageDrawable(backDrawable);
        }
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j) {
        if (this.drawFirst) {
            if (!(view instanceof GroupCallMiniTextureView) || !((GroupCallMiniTextureView) view).drawFirst) {
                return true;
            }
            float y = this.listView.getY() - getTop();
            float measuredHeight = (this.listView.getMeasuredHeight() + y) - this.listView.getTranslationY();
            canvas.save();
            canvas.clipRect(0.0f, y, getMeasuredWidth(), measuredHeight);
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }
        UndoView[] undoViewArr = this.undoView;
        if (view == undoViewArr[0] || view == undoViewArr[1]) {
            return true;
        }
        if (view instanceof GroupCallMiniTextureView) {
            GroupCallMiniTextureView groupCallMiniTextureView = (GroupCallMiniTextureView) view;
            if (groupCallMiniTextureView == this.fullscreenTextureView || groupCallMiniTextureView == this.outFullscreenTextureView || this.notDrawRenderes || groupCallMiniTextureView.drawFirst) {
                return true;
            }
            if (groupCallMiniTextureView.primaryView != null) {
                float y2 = this.listView.getY() - getTop();
                float measuredHeight2 = (this.listView.getMeasuredHeight() + y2) - this.listView.getTranslationY();
                float f = this.progressToFullscreenMode;
                if (groupCallMiniTextureView.secondaryView == null) {
                    f = 0.0f;
                }
                canvas.save();
                float f2 = 1.0f - f;
                canvas.clipRect(0.0f, y2 * f2, getMeasuredWidth(), (measuredHeight2 * f2) + (getMeasuredHeight() * f));
                boolean drawChild2 = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild2;
            } else if (GroupCallActivity.isTabletMode) {
                canvas.save();
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                boolean drawChild3 = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild3;
            } else {
                return super.drawChild(canvas, view, j);
            }
        } else if (!this.drawRenderesOnly) {
            return super.drawChild(canvas, view, j);
        } else {
            return true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04b2  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x04bd A[LOOP:1: B:155:0x04bd->B:162:0x0516, LOOP_START, PHI: r10 
      PHI: (r10v1 int) = (r10v0 int), (r10v2 int) binds: [B:154:0x04bb, B:162:0x0516] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0519 A[ORIG_RETURN, RETURN] */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        int dp;
        GroupCallMiniTextureView groupCallMiniTextureView;
        GroupCallMiniTextureView groupCallMiniTextureView2;
        float f;
        float f2;
        GroupCallMiniTextureView groupCallMiniTextureView3;
        if (GroupCallActivity.isTabletMode) {
            this.drawRenderesOnly = true;
            super.dispatchDraw(canvas);
            this.drawRenderesOnly = false;
        }
        this.drawFirst = true;
        super.dispatchDraw(canvas);
        this.drawFirst = false;
        if (this.outFullscreenTextureView != null || this.fullscreenTextureView != null) {
            float y = this.listView.getY() - getTop();
            float measuredHeight = (this.listView.getMeasuredHeight() + y) - this.listView.getTranslationY();
            float f3 = this.progressToFullscreenMode;
            canvas.save();
            boolean z = GroupCallActivity.isTabletMode;
            if (!z && (groupCallMiniTextureView3 = this.fullscreenTextureView) != null && !groupCallMiniTextureView3.forceDetached && groupCallMiniTextureView3.primaryView != null) {
                float f4 = 1.0f - f3;
                canvas.clipRect(0.0f, y * f4, getMeasuredWidth(), (measuredHeight * f4) + (getMeasuredHeight() * f3));
            } else if (z) {
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
            GroupCallMiniTextureView groupCallMiniTextureView4 = this.outFullscreenTextureView;
            if (groupCallMiniTextureView4 != null && groupCallMiniTextureView4.getParent() != null) {
                canvas.save();
                canvas.translate(this.outFullscreenTextureView.getX(), this.outFullscreenTextureView.getY());
                this.outFullscreenTextureView.draw(canvas);
                canvas.restore();
            }
            GroupCallMiniTextureView groupCallMiniTextureView5 = this.fullscreenTextureView;
            if (groupCallMiniTextureView5 != null && groupCallMiniTextureView5.getParent() != null) {
                if (this.fullscreenTextureView.getAlpha() != 1.0f) {
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(this.fullscreenTextureView.getX(), this.fullscreenTextureView.getY(), this.fullscreenTextureView.getX() + this.fullscreenTextureView.getMeasuredWidth(), this.fullscreenTextureView.getY() + this.fullscreenTextureView.getMeasuredHeight());
                    canvas.saveLayerAlpha(rectF, (int) (this.fullscreenTextureView.getAlpha() * 255.0f), 31);
                } else {
                    canvas.save();
                }
                boolean z2 = this.swipeToBackGesture || this.swipeToBackAnimator != null;
                if (z2 && !isRtmpStream()) {
                    canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ((GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) ? 0 : AndroidUtilities.dp(90.0f)));
                }
                canvas.translate(this.fullscreenTextureView.getX(), this.fullscreenTextureView.getY());
                this.fullscreenTextureView.setSwipeToBack(z2, this.swipeToBackDy);
                this.fullscreenTextureView.setZoom(this.zoomStarted || this.zoomBackAnimator != null, this.pinchScale, this.pinchCenterX, this.pinchCenterY, this.pinchTranslationX, this.pinchTranslationY);
                this.fullscreenTextureView.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
        }
        for (int i = 0; i < 2; i++) {
            if (this.undoView[i].getVisibility() == 0) {
                canvas.save();
                float f5 = GroupCallActivity.isLandscapeMode ? 0.0f : (-AndroidUtilities.dp(90.0f)) * (1.0f - this.progressToHideUi);
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), ((getMeasuredHeight() - (GroupCallActivity.isLandscapeMode ? 0 : AndroidUtilities.dp(90.0f))) + f5) - AndroidUtilities.dp(18.0f));
                if (this.isTablet) {
                    canvas.translate(this.undoView[i].getX() - AndroidUtilities.dp(8.0f), this.undoView[i].getY() - AndroidUtilities.dp(8.0f));
                } else {
                    canvas.translate(this.undoView[i].getX() - AndroidUtilities.dp(8.0f), ((this.undoView[i].getY() - (GroupCallActivity.isLandscapeMode ? 0 : AndroidUtilities.dp(90.0f))) + f5) - AndroidUtilities.dp(26.0f));
                }
                if (this.undoView[i].getAlpha() != 1.0f) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, this.undoView[i].getMeasuredWidth(), this.undoView[i].getMeasuredHeight(), (int) (this.undoView[i].getAlpha() * 255.0f), 31);
                } else {
                    canvas.save();
                }
                canvas.scale(this.undoView[i].getScaleX(), this.undoView[i].getScaleY(), this.undoView[i].getMeasuredWidth() / 2.0f, this.undoView[i].getMeasuredHeight() / 2.0f);
                this.undoView[i].draw(canvas);
                canvas.restore();
                canvas.restore();
            }
        }
        float f6 = this.progressToFullscreenMode * (1.0f - this.progressToHideUi);
        if (this.replaceFullscreenViewAnimator != null && (groupCallMiniTextureView = this.outFullscreenTextureView) != null && (groupCallMiniTextureView2 = this.fullscreenTextureView) != null) {
            boolean z3 = groupCallMiniTextureView.hasVideo;
            boolean z4 = groupCallMiniTextureView2.hasVideo;
            if (z3 != z4) {
                if (!z4) {
                    f2 = 1.0f - groupCallMiniTextureView2.getAlpha();
                } else {
                    f2 = groupCallMiniTextureView2.getAlpha();
                }
                f = f2 * f6;
            } else {
                f = !z4 ? 0.0f : f6;
            }
            int i2 = (int) (f * 255.0f);
            this.topShadowDrawable.setAlpha(i2);
            this.rightShadowDrawable.setAlpha(i2);
        } else {
            GroupCallMiniTextureView groupCallMiniTextureView6 = this.fullscreenTextureView;
            if (groupCallMiniTextureView6 != null) {
                float f7 = 255.0f * f6;
                this.topShadowDrawable.setAlpha((int) ((1.0f - groupCallMiniTextureView6.progressToNoVideoStub) * f7));
                this.rightShadowDrawable.setAlpha((int) (f7 * (1.0f - this.fullscreenTextureView.progressToNoVideoStub)));
            } else {
                int i3 = (int) (255.0f * f6);
                this.topShadowDrawable.setAlpha(i3);
                this.rightShadowDrawable.setAlpha(i3);
            }
        }
        this.backButton.setAlpha(f6);
        if (isRtmpStream()) {
            this.pinButton.setAlpha(0.0f);
            this.pinButton.setVisibility(4);
            this.pipView.setAlpha(f6);
            this.pipView.setVisibility(0);
            if (GroupCallActivity.isLandscapeMode) {
                this.pipView.setTranslationX((-AndroidUtilities.dp(72.0f)) * (1.0f - this.progressToHideUi));
            } else {
                this.pipView.setTranslationX(0.0f);
            }
        } else {
            this.pinButton.setAlpha(f6);
            this.pinButton.setVisibility(0);
            this.pipView.setAlpha(0.0f);
            this.pipView.setVisibility(4);
        }
        float currentActionBarHeight = ((ActionBar.getCurrentActionBarHeight() - this.pinTextView.getMeasuredHeight()) / 2.0f) - AndroidUtilities.dp(1.0f);
        float measuredWidth = (((getMeasuredWidth() - this.unpinTextView.getMeasuredWidth()) * this.pinDrawable.getProgress()) + ((getMeasuredWidth() - this.pinTextView.getMeasuredWidth()) * (1.0f - this.pinDrawable.getProgress()))) - AndroidUtilities.dp(21.0f);
        if (GroupCallActivity.isTabletMode) {
            dp = AndroidUtilities.dp(328.0f);
        } else {
            dp = GroupCallActivity.isLandscapeMode ? AndroidUtilities.dp(180.0f) : 0;
        }
        float f8 = measuredWidth - dp;
        this.pinTextView.setTranslationX(f8);
        this.unpinTextView.setTranslationX(f8);
        this.pinTextView.setTranslationY(currentActionBarHeight);
        this.unpinTextView.setTranslationY(currentActionBarHeight);
        this.pinContainer.setTranslationX(f8 - AndroidUtilities.dp(36.0f));
        this.pinContainer.setTranslationY((ActionBar.getCurrentActionBarHeight() - this.pinContainer.getMeasuredHeight()) / 2.0f);
        this.pinButton.setTranslationX(f8 - AndroidUtilities.dp(44.0f));
        if (isRtmpStream()) {
            this.pinTextView.setAlpha(0.0f);
            this.unpinTextView.setAlpha(0.0f);
            this.pinContainer.setAlpha(0.0f);
        } else {
            this.pinTextView.setAlpha((1.0f - this.pinDrawable.getProgress()) * f6);
            this.unpinTextView.setAlpha(this.pinDrawable.getProgress() * f6);
            this.pinContainer.setAlpha(f6);
        }
        float f9 = this.speakingMembersToastChangeProgress;
        if (f9 != 1.0f) {
            float f10 = f9 + 0.07272727f;
            this.speakingMembersToastChangeProgress = f10;
            if (f10 > 1.0f) {
                this.speakingMembersToastChangeProgress = 1.0f;
            } else {
                invalidate();
            }
            this.speakingMembersToast.invalidate();
        }
        boolean z5 = this.showSpeakingMembersToast;
        if (z5) {
            float f11 = this.showSpeakingMembersToastProgress;
            if (f11 != 1.0f) {
                float f12 = f11 + 0.10666667f;
                this.showSpeakingMembersToastProgress = f12;
                if (f12 > 1.0f) {
                    this.showSpeakingMembersToastProgress = 1.0f;
                } else {
                    invalidate();
                }
                if (!GroupCallActivity.isLandscapeMode) {
                    this.speakingMembersToast.setTranslationY(AndroidUtilities.dp(16.0f));
                } else {
                    this.speakingMembersToast.setTranslationY((ActionBar.getCurrentActionBarHeight() * (1.0f - this.progressToHideUi)) + AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(8.0f) * this.progressToHideUi));
                }
                this.speakingMembersToast.setAlpha(this.showSpeakingMembersToastProgress * this.progressToFullscreenMode);
                this.speakingMembersToast.setScaleX((this.showSpeakingMembersToastProgress * 0.5f) + 0.5f);
                this.speakingMembersToast.setScaleY((this.showSpeakingMembersToastProgress * 0.5f) + 0.5f);
                if (!GroupCallActivity.isTabletMode) {
                    this.notDrawRenderes = true;
                    super.dispatchDraw(canvas);
                    this.notDrawRenderes = false;
                } else {
                    super.dispatchDraw(canvas);
                }
                if (this.fullscreenListView.getVisibility() == 0) {
                    return;
                }
                for (int i4 = 0; i4 < this.fullscreenListView.getChildCount(); i4++) {
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) this.fullscreenListView.getChildAt(i4);
                    if (groupCallUserCell.getVisibility() == 0 && groupCallUserCell.getAlpha() != 0.0f) {
                        canvas.save();
                        canvas.translate(groupCallUserCell.getX() + this.fullscreenListView.getX(), groupCallUserCell.getY() + this.fullscreenListView.getY());
                        canvas.scale(groupCallUserCell.getScaleX(), groupCallUserCell.getScaleY(), groupCallUserCell.getMeasuredWidth() / 2.0f, groupCallUserCell.getMeasuredHeight() / 2.0f);
                        groupCallUserCell.drawOverlays(canvas);
                        canvas.restore();
                    }
                }
                return;
            }
        }
        if (!z5) {
            float f13 = this.showSpeakingMembersToastProgress;
            if (f13 != 0.0f) {
                float f14 = f13 - 0.10666667f;
                this.showSpeakingMembersToastProgress = f14;
                if (f14 < 0.0f) {
                    this.showSpeakingMembersToastProgress = 0.0f;
                } else {
                    invalidate();
                }
            }
        }
        if (!GroupCallActivity.isLandscapeMode) {
        }
        this.speakingMembersToast.setAlpha(this.showSpeakingMembersToastProgress * this.progressToFullscreenMode);
        this.speakingMembersToast.setScaleX((this.showSpeakingMembersToastProgress * 0.5f) + 0.5f);
        this.speakingMembersToast.setScaleY((this.showSpeakingMembersToastProgress * 0.5f) + 0.5f);
        if (!GroupCallActivity.isTabletMode) {
        }
        if (this.fullscreenListView.getVisibility() == 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01bc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void requestFullscreen(ChatObject.VideoParticipant videoParticipant) {
        GroupCallMiniTextureView groupCallMiniTextureView;
        GroupCallMiniTextureView groupCallMiniTextureView2;
        GroupCallGridCell groupCallGridCell;
        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell;
        GroupCallGridCell groupCallGridCell2;
        ChatObject.VideoParticipant videoParticipant2;
        if (videoParticipant == null && this.fullscreenParticipant == null) {
            return;
        }
        if (videoParticipant != null && videoParticipant.equals(this.fullscreenParticipant)) {
            return;
        }
        long peerId = videoParticipant == null ? 0L : MessageObject.getPeerId(videoParticipant.participant.peer);
        GroupCallMiniTextureView groupCallMiniTextureView3 = this.fullscreenTextureView;
        if (groupCallMiniTextureView3 != null) {
            groupCallMiniTextureView3.runDelayedAnimations();
        }
        ValueAnimator valueAnimator = this.replaceFullscreenViewAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z = false;
        if (sharedInstance != null && (videoParticipant2 = this.fullscreenParticipant) != null) {
            sharedInstance.requestFullScreen(videoParticipant2.participant, false, videoParticipant2.presentation);
        }
        this.fullscreenParticipant = videoParticipant;
        if (sharedInstance != null && videoParticipant != null) {
            sharedInstance.requestFullScreen(videoParticipant.participant, true, videoParticipant.presentation);
        }
        this.fullscreenPeerId = peerId;
        boolean z2 = this.inFullscreenMode;
        this.lastUpdateTime = System.currentTimeMillis();
        float f = 1.0f;
        if (videoParticipant == null) {
            if (this.inFullscreenMode) {
                ValueAnimator valueAnimator2 = this.fullscreenAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.inFullscreenMode = false;
                GroupCallMiniTextureView groupCallMiniTextureView4 = this.fullscreenTextureView;
                if (groupCallMiniTextureView4.primaryView != null || groupCallMiniTextureView4.secondaryView != null || groupCallMiniTextureView4.tabletGridView != null) {
                    ChatObject.VideoParticipant videoParticipant3 = groupCallMiniTextureView4.participant;
                    if (ChatObject.Call.videoIsActive(videoParticipant3.participant, videoParticipant3.presentation, this.call)) {
                        this.fullscreenTextureView.setShowingInFullscreen(false, true);
                    }
                }
                this.fullscreenTextureView.forceDetach(true);
                GroupCallGridCell groupCallGridCell3 = this.fullscreenTextureView.primaryView;
                if (groupCallGridCell3 != null) {
                    groupCallGridCell3.setRenderer(null);
                }
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = this.fullscreenTextureView.secondaryView;
                if (groupCallUserCell2 != null) {
                    groupCallUserCell2.setRenderer(null);
                }
                GroupCallGridCell groupCallGridCell4 = this.fullscreenTextureView.tabletGridView;
                if (groupCallGridCell4 != null) {
                    groupCallGridCell4.setRenderer(null);
                }
                GroupCallMiniTextureView groupCallMiniTextureView5 = this.fullscreenTextureView;
                groupCallMiniTextureView5.animate().alpha(0.0f).setListener(new AnonymousClass7(groupCallMiniTextureView5)).setDuration(350L).start();
            }
            this.backButton.setEnabled(false);
            this.hasPinnedVideo = false;
        } else {
            int i = 0;
            while (true) {
                if (i >= this.attachedRenderers.size()) {
                    groupCallMiniTextureView = null;
                    break;
                } else if (this.attachedRenderers.get(i).participant.equals(videoParticipant)) {
                    groupCallMiniTextureView = this.attachedRenderers.get(i);
                    break;
                } else {
                    i++;
                }
            }
            if (groupCallMiniTextureView != null) {
                ValueAnimator valueAnimator3 = this.fullscreenAnimator;
                if (valueAnimator3 != null) {
                    valueAnimator3.cancel();
                }
                if (!this.inFullscreenMode) {
                    this.inFullscreenMode = true;
                    clearCurrentFullscreenTextureView();
                    this.fullscreenTextureView = groupCallMiniTextureView;
                    groupCallMiniTextureView.setShowingInFullscreen(true, true);
                    invalidate();
                    this.pinDrawable.setCrossOut(this.hasPinnedVideo, false);
                } else {
                    this.hasPinnedVideo = false;
                    this.pinDrawable.setCrossOut(false, false);
                    this.fullscreenTextureView.forceDetach(false);
                    groupCallMiniTextureView.forceDetach(false);
                    if (!this.isTablet) {
                        GroupCallMiniTextureView groupCallMiniTextureView6 = this.fullscreenTextureView;
                        if (groupCallMiniTextureView6.primaryView != null || groupCallMiniTextureView6.secondaryView != null || groupCallMiniTextureView6.tabletGridView != null) {
                            groupCallMiniTextureView2 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                            GroupCallMiniTextureView groupCallMiniTextureView7 = this.fullscreenTextureView;
                            groupCallMiniTextureView2.setViews(groupCallMiniTextureView7.primaryView, groupCallMiniTextureView7.secondaryView, groupCallMiniTextureView7.tabletGridView);
                            groupCallMiniTextureView2.setFullscreenMode(this.inFullscreenMode, false);
                            groupCallMiniTextureView2.updateAttachState(false);
                            GroupCallGridCell groupCallGridCell5 = this.fullscreenTextureView.primaryView;
                            if (groupCallGridCell5 != null) {
                                groupCallGridCell5.setRenderer(groupCallMiniTextureView2);
                            }
                            GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell3 = this.fullscreenTextureView.secondaryView;
                            if (groupCallUserCell3 != null) {
                                groupCallUserCell3.setRenderer(groupCallMiniTextureView2);
                            }
                            GroupCallGridCell groupCallGridCell6 = this.fullscreenTextureView.tabletGridView;
                            if (groupCallGridCell6 != null) {
                                groupCallGridCell6.setRenderer(groupCallMiniTextureView2);
                            }
                            GroupCallMiniTextureView groupCallMiniTextureView8 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                            groupCallMiniTextureView8.participant = groupCallMiniTextureView.participant;
                            groupCallMiniTextureView8.setViews(groupCallMiniTextureView.primaryView, groupCallMiniTextureView.secondaryView, groupCallMiniTextureView.tabletGridView);
                            groupCallMiniTextureView8.setFullscreenMode(this.inFullscreenMode, false);
                            groupCallMiniTextureView8.updateAttachState(false);
                            groupCallMiniTextureView8.textureView.renderer.setAlpha(1.0f);
                            groupCallMiniTextureView8.textureView.blurRenderer.setAlpha(1.0f);
                            groupCallGridCell = groupCallMiniTextureView.primaryView;
                            if (groupCallGridCell != null) {
                                groupCallGridCell.setRenderer(groupCallMiniTextureView8);
                            }
                            groupCallUserCell = groupCallMiniTextureView.secondaryView;
                            if (groupCallUserCell != null) {
                                groupCallUserCell.setRenderer(groupCallMiniTextureView8);
                            }
                            groupCallGridCell2 = groupCallMiniTextureView.tabletGridView;
                            if (groupCallGridCell2 != null) {
                                groupCallGridCell2.setRenderer(groupCallMiniTextureView8);
                            }
                            groupCallMiniTextureView8.animateEnter = true;
                            groupCallMiniTextureView8.setAlpha(0.0f);
                            this.outFullscreenTextureView = this.fullscreenTextureView;
                            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(groupCallMiniTextureView8, View.ALPHA, 0.0f, 1.0f);
                            this.replaceFullscreenViewAnimator = ofFloat;
                            ofFloat.addListener(new AnonymousClass8(groupCallMiniTextureView8, groupCallMiniTextureView));
                            if (groupCallMiniTextureView2 != null) {
                                groupCallMiniTextureView2.setAlpha(0.0f);
                                groupCallMiniTextureView2.setScaleX(0.5f);
                                groupCallMiniTextureView2.setScaleY(0.5f);
                                groupCallMiniTextureView2.animateEnter = true;
                            }
                            groupCallMiniTextureView8.runOnFrameRendered(new GroupCallRenderersContainer$$ExternalSyntheticLambda9(this, groupCallMiniTextureView, groupCallMiniTextureView2));
                            clearCurrentFullscreenTextureView();
                            this.fullscreenTextureView = groupCallMiniTextureView8;
                            groupCallMiniTextureView8.setShowingInFullscreen(true, false);
                            update();
                        }
                    }
                    groupCallMiniTextureView2 = null;
                    GroupCallMiniTextureView groupCallMiniTextureView82 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                    groupCallMiniTextureView82.participant = groupCallMiniTextureView.participant;
                    groupCallMiniTextureView82.setViews(groupCallMiniTextureView.primaryView, groupCallMiniTextureView.secondaryView, groupCallMiniTextureView.tabletGridView);
                    groupCallMiniTextureView82.setFullscreenMode(this.inFullscreenMode, false);
                    groupCallMiniTextureView82.updateAttachState(false);
                    groupCallMiniTextureView82.textureView.renderer.setAlpha(1.0f);
                    groupCallMiniTextureView82.textureView.blurRenderer.setAlpha(1.0f);
                    groupCallGridCell = groupCallMiniTextureView.primaryView;
                    if (groupCallGridCell != null) {
                    }
                    groupCallUserCell = groupCallMiniTextureView.secondaryView;
                    if (groupCallUserCell != null) {
                    }
                    groupCallGridCell2 = groupCallMiniTextureView.tabletGridView;
                    if (groupCallGridCell2 != null) {
                    }
                    groupCallMiniTextureView82.animateEnter = true;
                    groupCallMiniTextureView82.setAlpha(0.0f);
                    this.outFullscreenTextureView = this.fullscreenTextureView;
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(groupCallMiniTextureView82, View.ALPHA, 0.0f, 1.0f);
                    this.replaceFullscreenViewAnimator = ofFloat2;
                    ofFloat2.addListener(new AnonymousClass8(groupCallMiniTextureView82, groupCallMiniTextureView));
                    if (groupCallMiniTextureView2 != null) {
                    }
                    groupCallMiniTextureView82.runOnFrameRendered(new GroupCallRenderersContainer$$ExternalSyntheticLambda9(this, groupCallMiniTextureView, groupCallMiniTextureView2));
                    clearCurrentFullscreenTextureView();
                    this.fullscreenTextureView = groupCallMiniTextureView82;
                    groupCallMiniTextureView82.setShowingInFullscreen(true, false);
                    update();
                }
            } else if (this.inFullscreenMode) {
                GroupCallMiniTextureView groupCallMiniTextureView9 = this.fullscreenTextureView;
                if (groupCallMiniTextureView9.primaryView == null) {
                    if (!((groupCallMiniTextureView9.secondaryView != null) | (groupCallMiniTextureView9.tabletGridView != null))) {
                        groupCallMiniTextureView9.forceDetach(true);
                        GroupCallMiniTextureView groupCallMiniTextureView10 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                        groupCallMiniTextureView10.participant = videoParticipant;
                        groupCallMiniTextureView10.setFullscreenMode(this.inFullscreenMode, false);
                        groupCallMiniTextureView10.setShowingInFullscreen(true, false);
                        groupCallMiniTextureView10.animateEnter = true;
                        groupCallMiniTextureView10.setAlpha(0.0f);
                        this.outFullscreenTextureView = this.fullscreenTextureView;
                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        this.replaceFullscreenViewAnimator = ofFloat3;
                        ofFloat3.addUpdateListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda3(this, groupCallMiniTextureView10));
                        this.replaceFullscreenViewAnimator.addListener(new AnonymousClass12(groupCallMiniTextureView10));
                        this.replaceFullscreenViewAnimator.start();
                        clearCurrentFullscreenTextureView();
                        this.fullscreenTextureView = groupCallMiniTextureView10;
                        groupCallMiniTextureView10.setShowingInFullscreen(true, false);
                        this.fullscreenTextureView.updateAttachState(false);
                        update();
                    }
                }
                groupCallMiniTextureView9.forceDetach(false);
                GroupCallMiniTextureView groupCallMiniTextureView11 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                GroupCallMiniTextureView groupCallMiniTextureView12 = this.fullscreenTextureView;
                groupCallMiniTextureView11.setViews(groupCallMiniTextureView12.primaryView, groupCallMiniTextureView12.secondaryView, groupCallMiniTextureView12.tabletGridView);
                groupCallMiniTextureView11.setFullscreenMode(this.inFullscreenMode, false);
                groupCallMiniTextureView11.updateAttachState(false);
                GroupCallGridCell groupCallGridCell7 = this.fullscreenTextureView.primaryView;
                if (groupCallGridCell7 != null) {
                    groupCallGridCell7.setRenderer(groupCallMiniTextureView11);
                }
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell4 = this.fullscreenTextureView.secondaryView;
                if (groupCallUserCell4 != null) {
                    groupCallUserCell4.setRenderer(groupCallMiniTextureView11);
                }
                GroupCallGridCell groupCallGridCell8 = this.fullscreenTextureView.tabletGridView;
                if (groupCallGridCell8 != null) {
                    groupCallGridCell8.setRenderer(groupCallMiniTextureView11);
                }
                groupCallMiniTextureView11.setAlpha(0.0f);
                groupCallMiniTextureView11.setScaleX(0.5f);
                groupCallMiniTextureView11.setScaleY(0.5f);
                groupCallMiniTextureView11.animateEnter = true;
                groupCallMiniTextureView11.runOnFrameRendered(new GroupCallRenderersContainer$$ExternalSyntheticLambda8(this, groupCallMiniTextureView11));
                GroupCallMiniTextureView groupCallMiniTextureView102 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                groupCallMiniTextureView102.participant = videoParticipant;
                groupCallMiniTextureView102.setFullscreenMode(this.inFullscreenMode, false);
                groupCallMiniTextureView102.setShowingInFullscreen(true, false);
                groupCallMiniTextureView102.animateEnter = true;
                groupCallMiniTextureView102.setAlpha(0.0f);
                this.outFullscreenTextureView = this.fullscreenTextureView;
                ValueAnimator ofFloat32 = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.replaceFullscreenViewAnimator = ofFloat32;
                ofFloat32.addUpdateListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda3(this, groupCallMiniTextureView102));
                this.replaceFullscreenViewAnimator.addListener(new AnonymousClass12(groupCallMiniTextureView102));
                this.replaceFullscreenViewAnimator.start();
                clearCurrentFullscreenTextureView();
                this.fullscreenTextureView = groupCallMiniTextureView102;
                groupCallMiniTextureView102.setShowingInFullscreen(true, false);
                this.fullscreenTextureView.updateAttachState(false);
                update();
            } else {
                this.inFullscreenMode = true;
                clearCurrentFullscreenTextureView();
                GroupCallMiniTextureView groupCallMiniTextureView13 = new GroupCallMiniTextureView(this, this.attachedRenderers, this.call, this.groupCallActivity);
                this.fullscreenTextureView = groupCallMiniTextureView13;
                groupCallMiniTextureView13.participant = videoParticipant;
                groupCallMiniTextureView13.setFullscreenMode(this.inFullscreenMode, false);
                this.fullscreenTextureView.setShowingInFullscreen(true, false);
                this.fullscreenTextureView.setShowingInFullscreen(true, false);
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.fullscreenTextureView, View.ALPHA, 0.0f, 1.0f);
                this.replaceFullscreenViewAnimator = ofFloat4;
                ofFloat4.addListener(new AnonymousClass13());
                this.replaceFullscreenViewAnimator.start();
                invalidate();
                this.pinDrawable.setCrossOut(this.hasPinnedVideo, false);
            }
            this.backButton.setEnabled(true);
        }
        boolean z3 = this.inFullscreenMode;
        if (z2 != z3) {
            if (!z3) {
                setUiVisible(true);
                if (this.hideUiRunnableIsScheduled) {
                    this.hideUiRunnableIsScheduled = false;
                    AndroidUtilities.cancelRunOnUIThread(this.hideUiRunnable);
                }
            } else {
                this.backButton.setVisibility(0);
                this.pinButton.setVisibility(0);
                this.unpinTextView.setVisibility(0);
                this.pinContainer.setVisibility(0);
            }
            onFullScreenModeChanged(true);
            float[] fArr = new float[2];
            fArr[0] = this.progressToFullscreenMode;
            if (!this.inFullscreenMode) {
                f = 0.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat5 = ValueAnimator.ofFloat(fArr);
            this.fullscreenAnimator = ofFloat5;
            ofFloat5.addUpdateListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda0(this));
            GroupCallMiniTextureView groupCallMiniTextureView14 = this.fullscreenTextureView;
            groupCallMiniTextureView14.animateToFullscreen = true;
            int currentAccount = this.groupCallActivity.getCurrentAccount();
            this.swipedBack = this.swipeToBackGesture;
            this.animationIndex = NotificationCenter.getInstance(currentAccount).setAnimationInProgress(this.animationIndex, null);
            this.fullscreenAnimator.addListener(new AnonymousClass14(currentAccount, groupCallMiniTextureView14));
            this.fullscreenAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.fullscreenAnimator.setDuration(350L);
            this.fullscreenTextureView.textureView.synchOrRunAnimation(this.fullscreenAnimator);
        }
        if (this.fullscreenParticipant == null) {
            z = true;
        }
        animateSwipeToBack(z);
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$removingMiniView;

        AnonymousClass7(GroupCallMiniTextureView groupCallMiniTextureView) {
            GroupCallRenderersContainer.this = r1;
            this.val$removingMiniView = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$removingMiniView.getParent() != null) {
                GroupCallRenderersContainer.this.removeView(this.val$removingMiniView);
                this.val$removingMiniView.release();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$newFullscreenTextureView;
        final /* synthetic */ GroupCallMiniTextureView val$removingMiniView;

        AnonymousClass8(GroupCallMiniTextureView groupCallMiniTextureView, GroupCallMiniTextureView groupCallMiniTextureView2) {
            GroupCallRenderersContainer.this = r1;
            this.val$newFullscreenTextureView = groupCallMiniTextureView;
            this.val$removingMiniView = groupCallMiniTextureView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.replaceFullscreenViewAnimator = null;
            this.val$newFullscreenTextureView.animateEnter = false;
            if (groupCallRenderersContainer.outFullscreenTextureView != null) {
                if (GroupCallRenderersContainer.this.outFullscreenTextureView.getParent() != null) {
                    GroupCallRenderersContainer groupCallRenderersContainer2 = GroupCallRenderersContainer.this;
                    groupCallRenderersContainer2.removeView(groupCallRenderersContainer2.outFullscreenTextureView);
                    this.val$removingMiniView.release();
                }
                GroupCallRenderersContainer.this.outFullscreenTextureView = null;
            }
        }
    }

    public /* synthetic */ void lambda$requestFullscreen$3(GroupCallMiniTextureView groupCallMiniTextureView, GroupCallMiniTextureView groupCallMiniTextureView2) {
        ValueAnimator valueAnimator = this.replaceFullscreenViewAnimator;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
        groupCallMiniTextureView.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.0f).setListener(new AnonymousClass9(groupCallMiniTextureView)).setDuration(100L).start();
        if (groupCallMiniTextureView2 != null) {
            groupCallMiniTextureView2.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(100L).setListener(new AnonymousClass10(this, groupCallMiniTextureView2)).start();
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$removingMiniView;

        AnonymousClass9(GroupCallMiniTextureView groupCallMiniTextureView) {
            GroupCallRenderersContainer.this = r1;
            this.val$removingMiniView = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$removingMiniView.getParent() != null) {
                GroupCallRenderersContainer.this.removeView(this.val$removingMiniView);
                this.val$removingMiniView.release();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$finalNewSmallTextureView;

        AnonymousClass10(GroupCallRenderersContainer groupCallRenderersContainer, GroupCallMiniTextureView groupCallMiniTextureView) {
            this.val$finalNewSmallTextureView = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$finalNewSmallTextureView.animateEnter = false;
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$newSmallTextureView;

        AnonymousClass11(GroupCallRenderersContainer groupCallRenderersContainer, GroupCallMiniTextureView groupCallMiniTextureView) {
            this.val$newSmallTextureView = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$newSmallTextureView.animateEnter = false;
        }
    }

    public /* synthetic */ void lambda$requestFullscreen$4(GroupCallMiniTextureView groupCallMiniTextureView) {
        groupCallMiniTextureView.animate().alpha(1.0f).scaleY(1.0f).scaleX(1.0f).setListener(new AnonymousClass11(this, groupCallMiniTextureView)).setDuration(150L).start();
    }

    public /* synthetic */ void lambda$requestFullscreen$5(GroupCallMiniTextureView groupCallMiniTextureView, ValueAnimator valueAnimator) {
        groupCallMiniTextureView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        invalidate();
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends AnimatorListenerAdapter {
        final /* synthetic */ GroupCallMiniTextureView val$newFullscreenTextureView;

        AnonymousClass12(GroupCallMiniTextureView groupCallMiniTextureView) {
            GroupCallRenderersContainer.this = r1;
            this.val$newFullscreenTextureView = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.replaceFullscreenViewAnimator = null;
            this.val$newFullscreenTextureView.animateEnter = false;
            if (groupCallRenderersContainer.outFullscreenTextureView != null) {
                if (GroupCallRenderersContainer.this.outFullscreenTextureView.getParent() != null) {
                    GroupCallRenderersContainer groupCallRenderersContainer2 = GroupCallRenderersContainer.this;
                    groupCallRenderersContainer2.removeView(groupCallRenderersContainer2.outFullscreenTextureView);
                    GroupCallRenderersContainer.this.outFullscreenTextureView.release();
                }
                GroupCallRenderersContainer.this.outFullscreenTextureView = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends AnimatorListenerAdapter {
        AnonymousClass13() {
            GroupCallRenderersContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.replaceFullscreenViewAnimator = null;
            groupCallRenderersContainer.fullscreenTextureView.animateEnter = false;
            if (groupCallRenderersContainer.outFullscreenTextureView != null) {
                if (GroupCallRenderersContainer.this.outFullscreenTextureView.getParent() != null) {
                    GroupCallRenderersContainer groupCallRenderersContainer2 = GroupCallRenderersContainer.this;
                    groupCallRenderersContainer2.removeView(groupCallRenderersContainer2.outFullscreenTextureView);
                    GroupCallRenderersContainer.this.outFullscreenTextureView.release();
                }
                GroupCallRenderersContainer.this.outFullscreenTextureView = null;
            }
        }
    }

    public /* synthetic */ void lambda$requestFullscreen$6(ValueAnimator valueAnimator) {
        this.progressToFullscreenMode = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.groupCallActivity.getMenuItemsContainer().setAlpha(1.0f - this.progressToFullscreenMode);
        this.groupCallActivity.invalidateActionBarAlpha();
        this.groupCallActivity.invalidateScrollOffsetY();
        update();
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ GroupCallMiniTextureView val$textureViewFinal;

        AnonymousClass14(int i, GroupCallMiniTextureView groupCallMiniTextureView) {
            GroupCallRenderersContainer.this = r1;
            this.val$currentAccount = i;
            this.val$textureViewFinal = groupCallMiniTextureView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(this.val$currentAccount).onAnimationFinish(GroupCallRenderersContainer.this.animationIndex);
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.fullscreenAnimator = null;
            this.val$textureViewFinal.animateToFullscreen = false;
            if (!groupCallRenderersContainer.inFullscreenMode) {
                groupCallRenderersContainer.clearCurrentFullscreenTextureView();
                GroupCallRenderersContainer groupCallRenderersContainer2 = GroupCallRenderersContainer.this;
                groupCallRenderersContainer2.fullscreenTextureView = null;
                groupCallRenderersContainer2.fullscreenPeerId = 0L;
            }
            GroupCallRenderersContainer groupCallRenderersContainer3 = GroupCallRenderersContainer.this;
            groupCallRenderersContainer3.progressToFullscreenMode = groupCallRenderersContainer3.inFullscreenMode ? 1.0f : 0.0f;
            groupCallRenderersContainer3.update();
            GroupCallRenderersContainer.this.onFullScreenModeChanged(false);
            GroupCallRenderersContainer groupCallRenderersContainer4 = GroupCallRenderersContainer.this;
            if (!groupCallRenderersContainer4.inFullscreenMode) {
                groupCallRenderersContainer4.backButton.setVisibility(8);
                GroupCallRenderersContainer.this.pinButton.setVisibility(8);
                GroupCallRenderersContainer.this.unpinTextView.setVisibility(8);
                GroupCallRenderersContainer.this.pinContainer.setVisibility(8);
            }
        }
    }

    public void clearCurrentFullscreenTextureView() {
        GroupCallMiniTextureView groupCallMiniTextureView = this.fullscreenTextureView;
        if (groupCallMiniTextureView != null) {
            groupCallMiniTextureView.setSwipeToBack(false, 0.0f);
            this.fullscreenTextureView.setZoom(false, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void update() {
        invalidate();
    }

    public void setUiVisible(boolean z) {
        if (this.uiVisible != z) {
            this.uiVisible = z;
            onUiVisibilityChanged();
            if (z && this.inFullscreenMode) {
                if (!this.hideUiRunnableIsScheduled) {
                    this.hideUiRunnableIsScheduled = true;
                    AndroidUtilities.runOnUIThread(this.hideUiRunnable, 3000L);
                }
            } else {
                this.hideUiRunnableIsScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(this.hideUiRunnable);
            }
            GroupCallMiniTextureView groupCallMiniTextureView = this.fullscreenTextureView;
            if (groupCallMiniTextureView == null) {
                return;
            }
            groupCallMiniTextureView.requestLayout();
        }
    }

    public boolean canHideUI() {
        return this.inFullscreenMode;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return onTouchEvent(motionEvent);
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x025d  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if ((this.maybeSwipeToBackGesture || this.swipeToBackGesture) && (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3)) {
            this.maybeSwipeToBackGesture = false;
            if (this.swipeToBackGesture) {
                if (motionEvent.getActionMasked() == 1 && Math.abs(this.swipeToBackDy) > AndroidUtilities.dp(120.0f)) {
                    this.groupCallActivity.fullscreenFor(null);
                } else {
                    animateSwipeToBack(false);
                }
            }
            invalidate();
        }
        if (!this.inFullscreenMode || ((!this.maybeSwipeToBackGesture && !this.swipeToBackGesture && !this.tapGesture && !this.canZoomGesture && !this.isInPinchToZoomTouchMode && !this.zoomStarted && motionEvent.getActionMasked() != 0) || this.fullscreenTextureView == null)) {
            finishZoom();
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.maybeSwipeToBackGesture = false;
            this.swipeToBackGesture = false;
            this.canZoomGesture = false;
            this.isInPinchToZoomTouchMode = false;
            this.zoomStarted = false;
        }
        if (motionEvent.getActionMasked() == 0 && this.swipeToBackAnimator != null) {
            this.maybeSwipeToBackGesture = false;
            this.swipeToBackGesture = true;
            this.tapY = motionEvent.getY() - this.swipeToBackDy;
            this.swipeToBackAnimator.removeAllListeners();
            this.swipeToBackAnimator.cancel();
            this.swipeToBackAnimator = null;
        } else if (this.swipeToBackAnimator != null) {
            finishZoom();
            return false;
        }
        if (this.fullscreenTextureView.isInsideStopScreenButton(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0 && !this.swipeToBackGesture) {
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, ActionBar.getCurrentActionBarHeight(), this.fullscreenTextureView.getMeasuredWidth() + ((!GroupCallActivity.isLandscapeMode || !this.uiVisible) ? 0 : -AndroidUtilities.dp(90.0f)), this.fullscreenTextureView.getMeasuredHeight() + ((GroupCallActivity.isLandscapeMode || !this.uiVisible) ? 0 : -AndroidUtilities.dp(90.0f)));
            if (rectF.contains(motionEvent.getX(), motionEvent.getY())) {
                this.tapTime = System.currentTimeMillis();
                this.tapGesture = true;
                this.maybeSwipeToBackGesture = true;
                this.tapX = motionEvent.getX();
                this.tapY = motionEvent.getY();
            }
        } else if ((this.maybeSwipeToBackGesture || this.swipeToBackGesture || this.tapGesture) && motionEvent.getActionMasked() == 2) {
            if (Math.abs(this.tapX - motionEvent.getX()) > this.touchSlop || Math.abs(this.tapY - motionEvent.getY()) > this.touchSlop) {
                this.tapGesture = false;
            }
            if (this.maybeSwipeToBackGesture && !this.zoomStarted && Math.abs(this.tapY - motionEvent.getY()) > this.touchSlop * 2) {
                this.tapY = motionEvent.getY();
                this.maybeSwipeToBackGesture = false;
                this.swipeToBackGesture = true;
            } else if (this.swipeToBackGesture) {
                this.swipeToBackDy = motionEvent.getY() - this.tapY;
                invalidate();
            }
            if (this.maybeSwipeToBackGesture && Math.abs(this.tapX - motionEvent.getX()) > this.touchSlop * 4) {
                this.maybeSwipeToBackGesture = false;
            }
        }
        if (this.tapGesture && motionEvent.getActionMasked() == 1 && System.currentTimeMillis() - this.tapTime < 200) {
            this.tapGesture = false;
            if (this.showSpeakingMembersToast) {
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(this.speakingMembersToast.getX(), this.speakingMembersToast.getY(), this.speakingMembersToast.getX() + this.speakingMembersToast.getWidth(), this.speakingMembersToast.getY() + this.speakingMembersToast.getHeight());
                if (this.call != null && rectF2.contains(motionEvent.getX(), motionEvent.getY())) {
                    boolean z2 = false;
                    z = false;
                    for (int i = 0; i < this.call.visibleVideoParticipants.size(); i++) {
                        if (this.speakingToastPeerId == MessageObject.getPeerId(this.call.visibleVideoParticipants.get(i).participant.peer)) {
                            this.groupCallActivity.fullscreenFor(this.call.visibleVideoParticipants.get(i));
                            z2 = true;
                            z = true;
                        }
                    }
                    if (!z2) {
                        this.groupCallActivity.fullscreenFor(new ChatObject.VideoParticipant(this.call.participants.get(this.speakingToastPeerId), false, false));
                        z = true;
                    }
                    if (!z) {
                        setUiVisible(!this.uiVisible);
                    }
                    this.swipeToBackDy = 0.0f;
                    invalidate();
                }
            }
            z = false;
            if (!z) {
            }
            this.swipeToBackDy = 0.0f;
            invalidate();
        }
        if (!this.fullscreenTextureView.hasVideo || this.swipeToBackGesture) {
            finishZoom();
            return this.tapGesture || this.swipeToBackGesture || this.maybeSwipeToBackGesture;
        }
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            if (motionEvent.getActionMasked() == 0) {
                TextureViewRenderer textureViewRenderer = this.fullscreenTextureView.textureView.renderer;
                RectF rectF3 = AndroidUtilities.rectTmp;
                rectF3.set(textureViewRenderer.getX(), textureViewRenderer.getY(), textureViewRenderer.getX() + textureViewRenderer.getMeasuredWidth(), textureViewRenderer.getY() + textureViewRenderer.getMeasuredHeight());
                rectF3.inset(((textureViewRenderer.getMeasuredHeight() * this.fullscreenTextureView.textureView.scaleTextureToFill) - textureViewRenderer.getMeasuredHeight()) / 2.0f, ((textureViewRenderer.getMeasuredWidth() * this.fullscreenTextureView.textureView.scaleTextureToFill) - textureViewRenderer.getMeasuredWidth()) / 2.0f);
                if (!GroupCallActivity.isLandscapeMode) {
                    rectF3.top = Math.max(rectF3.top, ActionBar.getCurrentActionBarHeight());
                    rectF3.bottom = Math.min(rectF3.bottom, this.fullscreenTextureView.getMeasuredHeight() - AndroidUtilities.dp(90.0f));
                } else {
                    rectF3.top = Math.max(rectF3.top, ActionBar.getCurrentActionBarHeight());
                    rectF3.right = Math.min(rectF3.right, this.fullscreenTextureView.getMeasuredWidth() - AndroidUtilities.dp(90.0f));
                }
                boolean contains = rectF3.contains(motionEvent.getX(), motionEvent.getY());
                this.canZoomGesture = contains;
                if (!contains) {
                    finishZoom();
                    return this.maybeSwipeToBackGesture;
                }
            }
            if (!this.isInPinchToZoomTouchMode && motionEvent.getPointerCount() == 2) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                float x = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                this.pinchCenterX = x;
                this.pinchStartCenterX = x;
                float y = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                this.pinchCenterY = y;
                this.pinchStartCenterY = y;
                this.pinchScale = 1.0f;
                this.pointerId1 = motionEvent.getPointerId(0);
                this.pointerId2 = motionEvent.getPointerId(1);
                this.isInPinchToZoomTouchMode = true;
            }
        } else if (motionEvent.getActionMasked() == 2 && this.isInPinchToZoomTouchMode) {
            int i2 = -1;
            int i3 = -1;
            for (int i4 = 0; i4 < motionEvent.getPointerCount(); i4++) {
                if (this.pointerId1 == motionEvent.getPointerId(i4)) {
                    i2 = i4;
                }
                if (this.pointerId2 == motionEvent.getPointerId(i4)) {
                    i3 = i4;
                }
            }
            if (i2 == -1 || i3 == -1) {
                getParent().requestDisallowInterceptTouchEvent(false);
                finishZoom();
                return this.maybeSwipeToBackGesture;
            }
            float hypot = ((float) Math.hypot(motionEvent.getX(i3) - motionEvent.getX(i2), motionEvent.getY(i3) - motionEvent.getY(i2))) / this.pinchStartDistance;
            this.pinchScale = hypot;
            if (hypot > 1.005f && !this.zoomStarted) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(i3) - motionEvent.getX(i2), motionEvent.getY(i3) - motionEvent.getY(i2));
                float x2 = (motionEvent.getX(i2) + motionEvent.getX(i3)) / 2.0f;
                this.pinchCenterX = x2;
                this.pinchStartCenterX = x2;
                float y2 = (motionEvent.getY(i2) + motionEvent.getY(i3)) / 2.0f;
                this.pinchCenterY = y2;
                this.pinchStartCenterY = y2;
                this.pinchScale = 1.0f;
                this.pinchTranslationX = 0.0f;
                this.pinchTranslationY = 0.0f;
                getParent().requestDisallowInterceptTouchEvent(true);
                this.zoomStarted = true;
                this.isInPinchToZoomTouchMode = true;
            }
            float x3 = this.pinchStartCenterX - ((motionEvent.getX(i2) + motionEvent.getX(i3)) / 2.0f);
            float y3 = this.pinchStartCenterY - ((motionEvent.getY(i2) + motionEvent.getY(i3)) / 2.0f);
            float f = this.pinchScale;
            this.pinchTranslationX = (-x3) / f;
            this.pinchTranslationY = (-y3) / f;
            invalidate();
        } else if (motionEvent.getActionMasked() == 1 || ((motionEvent.getActionMasked() == 6 && checkPointerIds(motionEvent)) || motionEvent.getActionMasked() == 3)) {
            getParent().requestDisallowInterceptTouchEvent(false);
            finishZoom();
        }
        return this.canZoomGesture || this.tapGesture || this.maybeSwipeToBackGesture;
    }

    private void animateSwipeToBack(boolean z) {
        ValueAnimator valueAnimator;
        if (this.swipeToBackGesture) {
            this.swipeToBackGesture = false;
            float[] fArr = new float[2];
            float f = this.swipeToBackDy;
            if (z) {
                fArr[0] = f;
                fArr[1] = 0.0f;
                valueAnimator = ValueAnimator.ofFloat(fArr);
            } else {
                fArr[0] = f;
                fArr[1] = 0.0f;
                valueAnimator = ValueAnimator.ofFloat(fArr);
            }
            this.swipeToBackAnimator = valueAnimator;
            valueAnimator.addUpdateListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda1(this));
            this.swipeToBackAnimator.addListener(new AnonymousClass15());
            ValueAnimator valueAnimator2 = this.swipeToBackAnimator;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            valueAnimator2.setInterpolator(cubicBezierInterpolator);
            this.swipeToBackAnimator.setDuration(z ? 350L : 200L);
            this.swipeToBackAnimator.setInterpolator(cubicBezierInterpolator);
            GroupCallMiniTextureView groupCallMiniTextureView = this.fullscreenTextureView;
            if (groupCallMiniTextureView != null) {
                groupCallMiniTextureView.textureView.synchOrRunAnimation(this.swipeToBackAnimator);
            } else {
                this.swipeToBackAnimator.start();
            }
            this.lastUpdateTime = System.currentTimeMillis();
        }
        this.maybeSwipeToBackGesture = false;
    }

    public /* synthetic */ void lambda$animateSwipeToBack$7(ValueAnimator valueAnimator) {
        this.swipeToBackDy = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends AnimatorListenerAdapter {
        AnonymousClass15() {
            GroupCallRenderersContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.swipeToBackAnimator = null;
            groupCallRenderersContainer.swipeToBackDy = 0.0f;
            groupCallRenderersContainer.invalidate();
        }
    }

    private void finishZoom() {
        if (this.zoomStarted) {
            this.zoomStarted = false;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.zoomBackAnimator = ofFloat;
            ofFloat.addUpdateListener(new GroupCallRenderersContainer$$ExternalSyntheticLambda2(this, this.pinchScale, this.pinchTranslationX, this.pinchTranslationY));
            this.zoomBackAnimator.addListener(new AnonymousClass16());
            this.zoomBackAnimator.setDuration(350L);
            this.zoomBackAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.zoomBackAnimator.start();
            this.lastUpdateTime = System.currentTimeMillis();
        }
        this.canZoomGesture = false;
        this.isInPinchToZoomTouchMode = false;
    }

    public /* synthetic */ void lambda$finishZoom$8(float f, float f2, float f3, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.pinchScale = (f * floatValue) + ((1.0f - floatValue) * 1.0f);
        this.pinchTranslationX = f2 * floatValue;
        this.pinchTranslationY = f3 * floatValue;
        invalidate();
    }

    /* renamed from: org.telegram.ui.Components.voip.GroupCallRenderersContainer$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends AnimatorListenerAdapter {
        AnonymousClass16() {
            GroupCallRenderersContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            GroupCallRenderersContainer groupCallRenderersContainer = GroupCallRenderersContainer.this;
            groupCallRenderersContainer.zoomBackAnimator = null;
            groupCallRenderersContainer.pinchScale = 1.0f;
            groupCallRenderersContainer.pinchTranslationX = 0.0f;
            GroupCallRenderersContainer.this.pinchTranslationY = 0.0f;
            GroupCallRenderersContainer.this.invalidate();
        }
    }

    private boolean checkPointerIds(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 2) {
            return false;
        }
        if (this.pointerId1 == motionEvent.getPointerId(0) && this.pointerId2 == motionEvent.getPointerId(1)) {
            return true;
        }
        return this.pointerId1 == motionEvent.getPointerId(1) && this.pointerId2 == motionEvent.getPointerId(0);
    }

    public void delayHideUi() {
        if (this.hideUiRunnableIsScheduled) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUiRunnable);
        }
        AndroidUtilities.runOnUIThread(this.hideUiRunnable, 3000L);
        this.hideUiRunnableIsScheduled = true;
    }

    public boolean isUiVisible() {
        return this.uiVisible;
    }

    public void setProgressToHideUi(float f) {
        if (this.progressToHideUi != f) {
            this.progressToHideUi = f;
            invalidate();
            GroupCallMiniTextureView groupCallMiniTextureView = this.fullscreenTextureView;
            if (groupCallMiniTextureView == null) {
                return;
            }
            groupCallMiniTextureView.invalidate();
        }
    }

    public void setAmplitude(TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant, float f) {
        for (int i = 0; i < this.attachedRenderers.size(); i++) {
            if (MessageObject.getPeerId(this.attachedRenderers.get(i).participant.participant.peer) == MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer)) {
                this.attachedRenderers.get(i).setAmplitude(f);
            }
        }
    }

    public boolean isAnimating() {
        return this.fullscreenAnimator != null;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (GroupCallActivity.isTabletMode) {
            ((ViewGroup.MarginLayoutParams) this.topShadowView.getLayoutParams()).rightMargin = AndroidUtilities.dp(328.0f);
        } else if (GroupCallActivity.isLandscapeMode) {
            ((ViewGroup.MarginLayoutParams) this.topShadowView.getLayoutParams()).rightMargin = isRtmpStream() ? 0 : AndroidUtilities.dp(90.0f);
        } else {
            ((ViewGroup.MarginLayoutParams) this.topShadowView.getLayoutParams()).rightMargin = 0;
        }
        this.rightShadowView.setVisibility((!GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) ? 8 : 0);
        this.pinContainer.getLayoutParams().height = AndroidUtilities.dp(40.0f);
        this.pinTextView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 0), i2);
        this.unpinTextView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 0), i2);
        this.pinContainer.getLayoutParams().width = AndroidUtilities.dp(46.0f) + (!this.hasPinnedVideo ? this.pinTextView : this.unpinTextView).getMeasuredWidth();
        ((ViewGroup.MarginLayoutParams) this.speakingMembersToast.getLayoutParams()).rightMargin = GroupCallActivity.isLandscapeMode ? AndroidUtilities.dp(45.0f) : 0;
        for (int i3 = 0; i3 < 2; i3++) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.undoView[i3].getLayoutParams();
            if (this.isTablet) {
                marginLayoutParams.rightMargin = AndroidUtilities.dp(344.0f);
            } else {
                marginLayoutParams.rightMargin = GroupCallActivity.isLandscapeMode ? AndroidUtilities.dp(180.0f) : 0;
            }
        }
        super.onMeasure(i, i2);
    }

    public boolean autoPinEnabled() {
        return !this.hasPinnedVideo && System.currentTimeMillis() - this.lastUpdateTime > 2000 && !this.swipeToBackGesture && !this.isInPinchToZoomTouchMode;
    }

    public void setVisibleParticipant(boolean z) {
        boolean z2;
        int i;
        int i2 = 0;
        if (!this.inFullscreenMode || this.isTablet || this.fullscreenParticipant == null || this.fullscreenAnimator != null || this.call == null) {
            if (!this.showSpeakingMembersToast) {
                return;
            }
            this.showSpeakingMembersToast = false;
            this.showSpeakingMembersToastProgress = 0.0f;
            return;
        }
        int currentAccount = this.groupCallActivity.getCurrentAccount();
        long j = 500;
        if (System.currentTimeMillis() - this.lastUpdateTooltipTime < 500) {
            if (this.updateTooltipRunnbale != null) {
                return;
            }
            GroupCallRenderersContainer$$ExternalSyntheticLambda7 groupCallRenderersContainer$$ExternalSyntheticLambda7 = new GroupCallRenderersContainer$$ExternalSyntheticLambda7(this);
            this.updateTooltipRunnbale = groupCallRenderersContainer$$ExternalSyntheticLambda7;
            AndroidUtilities.runOnUIThread(groupCallRenderersContainer$$ExternalSyntheticLambda7, (System.currentTimeMillis() - this.lastUpdateTooltipTime) + 50);
            return;
        }
        this.lastUpdateTooltipTime = System.currentTimeMillis();
        int i3 = 0;
        SpannableStringBuilder spannableStringBuilder = null;
        int i4 = 0;
        while (i3 < this.call.currentSpeakingPeers.size()) {
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = this.call.currentSpeakingPeers.get(this.call.currentSpeakingPeers.keyAt(i3));
            if (tLRPC$TL_groupCallParticipant.self || tLRPC$TL_groupCallParticipant.muted_by_you || MessageObject.getPeerId(this.fullscreenParticipant.participant.peer) == MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer)) {
                i = i3;
            } else {
                long peerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
                i = i3;
                if (!(SystemClock.uptimeMillis() - tLRPC$TL_groupCallParticipant.lastSpeakTime < j)) {
                    continue;
                } else {
                    if (spannableStringBuilder == null) {
                        spannableStringBuilder = new SpannableStringBuilder();
                    }
                    if (i4 == 0) {
                        this.speakingToastPeerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
                    }
                    if (i4 < 3) {
                        TLRPC$User user = peerId > 0 ? MessagesController.getInstance(currentAccount).getUser(Long.valueOf(peerId)) : null;
                        TLRPC$Chat chat = peerId <= 0 ? MessagesController.getInstance(currentAccount).getChat(Long.valueOf(peerId)) : null;
                        if (user != null || chat != null) {
                            this.speakingMembersAvatars.setObject(i4, currentAccount, tLRPC$TL_groupCallParticipant);
                            if (i4 != 0) {
                                spannableStringBuilder.append((CharSequence) ", ");
                            }
                            if (user != null) {
                                if (Build.VERSION.SDK_INT >= 21) {
                                    spannableStringBuilder.append(UserObject.getFirstName(user), new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0);
                                } else {
                                    spannableStringBuilder.append((CharSequence) UserObject.getFirstName(user));
                                }
                            } else if (Build.VERSION.SDK_INT >= 21) {
                                spannableStringBuilder.append(chat.title, new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0);
                            } else {
                                spannableStringBuilder.append((CharSequence) chat.title);
                            }
                        }
                    }
                    i4++;
                    if (i4 == 3) {
                        break;
                    }
                }
            }
            i3 = i + 1;
            j = 500;
        }
        boolean z3 = i4 != 0;
        boolean z4 = this.showSpeakingMembersToast;
        if (!z4 && z3) {
            z2 = false;
        } else if (!z3 && z4) {
            this.showSpeakingMembersToast = z3;
            invalidate();
            return;
        } else {
            if (z4 && z3) {
                this.speakingMembersToastFromLeft = this.speakingMembersToast.getLeft();
                this.speakingMembersToastFromRight = this.speakingMembersToast.getRight();
                this.speakingMembersToastFromTextLeft = this.speakingMembersText.getLeft();
                this.speakingMembersToastChangeProgress = 0.0f;
            }
            z2 = z;
        }
        if (!z3) {
            this.showSpeakingMembersToast = z3;
            invalidate();
            return;
        }
        String pluralString = LocaleController.getPluralString("MembersAreSpeakingToast", i4);
        int indexOf = pluralString.indexOf("un1");
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(pluralString);
        spannableStringBuilder2.replace(indexOf, indexOf + 3, (CharSequence) spannableStringBuilder);
        this.speakingMembersText.setText(spannableStringBuilder2);
        if (i4 != 0) {
            if (i4 == 1) {
                i2 = AndroidUtilities.dp(40.0f);
            } else if (i4 == 2) {
                i2 = AndroidUtilities.dp(64.0f);
            } else {
                i2 = AndroidUtilities.dp(88.0f);
            }
        }
        ((FrameLayout.LayoutParams) this.speakingMembersText.getLayoutParams()).leftMargin = i2;
        ((FrameLayout.LayoutParams) this.speakingMembersText.getLayoutParams()).rightMargin = AndroidUtilities.dp(16.0f);
        this.showSpeakingMembersToast = z3;
        invalidate();
        while (i4 < 3) {
            this.speakingMembersAvatars.setObject(i4, currentAccount, null);
            i4++;
        }
        this.speakingMembersAvatars.commitTransition(z2);
    }

    public /* synthetic */ void lambda$setVisibleParticipant$9() {
        this.updateTooltipRunnbale = null;
        setVisibleParticipant(true);
    }

    public UndoView getUndoView() {
        if (this.undoView[0].getVisibility() == 0) {
            UndoView[] undoViewArr = this.undoView;
            UndoView undoView = undoViewArr[0];
            undoViewArr[0] = undoViewArr[1];
            undoViewArr[1] = undoView;
            undoView.hide(true, 2);
            removeView(this.undoView[0]);
            addView(this.undoView[0]);
        }
        return this.undoView[0];
    }

    public boolean isVisible(TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant) {
        return this.attachedPeerIds.get(MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer)) > 0;
    }

    public void attach(GroupCallMiniTextureView groupCallMiniTextureView) {
        this.attachedRenderers.add(groupCallMiniTextureView);
        long peerId = MessageObject.getPeerId(groupCallMiniTextureView.participant.participant.peer);
        LongSparseIntArray longSparseIntArray = this.attachedPeerIds;
        longSparseIntArray.put(peerId, longSparseIntArray.get(peerId, 0) + 1);
    }

    public void detach(GroupCallMiniTextureView groupCallMiniTextureView) {
        this.attachedRenderers.remove(groupCallMiniTextureView);
        long peerId = MessageObject.getPeerId(groupCallMiniTextureView.participant.participant.peer);
        LongSparseIntArray longSparseIntArray = this.attachedPeerIds;
        longSparseIntArray.put(peerId, longSparseIntArray.get(peerId, 0) - 1);
    }

    public void setGroupCall(ChatObject.Call call) {
        this.call = call;
    }
}
