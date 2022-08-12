package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipantVideo;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BlobDrawable;
import org.telegram.ui.Components.CrossOutDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.GroupCallFullscreenAdapter;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.GroupCallStatusIcon;
import org.telegram.ui.GroupCallActivity;
import org.webrtc.GlGenericDrawer;
import org.webrtc.RendererCommon;
import org.webrtc.TextureViewRenderer;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class GroupCallMiniTextureView extends FrameLayout implements GroupCallStatusIcon.Callback {
    GroupCallActivity activity;
    boolean animateEnter;
    int animateToColor;
    public boolean animateToFullscreen;
    public boolean animateToScrimView;
    boolean attached;
    ArrayList<GroupCallMiniTextureView> attachedRenderers;
    ImageView blurredFlippingStub;
    ChatObject.Call call;
    private Drawable castingScreenDrawable;
    private boolean checkScale;
    int collapseSize;
    ValueAnimator colorAnimator;
    int currentAccount;
    public boolean drawFirst;
    ValueAnimator flipAnimator;
    boolean flipHalfReached;
    public boolean forceDetached;
    int fullSize;
    LinearGradient gradientShader;
    int gridItemsCount;
    public boolean hasVideo;
    boolean inPinchToZoom;
    FrameLayout infoContainer;
    private boolean invalidateFromChild;
    boolean isFullscreenMode;
    int lastIconColor;
    private boolean lastLandscapeMode;
    private int lastSize;
    int lastSpeakingFrameColor;
    private final RLottieImageView micIconView;
    private final SimpleTextView nameView;
    private TextView noRtmpStreamTextView;
    ValueAnimator noVideoStubAnimator;
    private NoVideoStubLayout noVideoStubLayout;
    float overlayIconAlpha;
    GroupCallRenderersContainer parentContainer;
    public ChatObject.VideoParticipant participant;
    private CrossOutDrawable pausedVideoDrawable;
    float pinchCenterX;
    float pinchCenterY;
    float pinchScale;
    float pinchTranslationX;
    float pinchTranslationY;
    private boolean postedNoRtmpStreamCallback;
    public GroupCallGridCell primaryView;
    private float progressToBackground;
    float progressToSpeaking;
    private final ImageView screencastIcon;
    public GroupCallFullscreenAdapter.GroupCallUserCell secondaryView;
    private boolean showingAsScrimView;
    public boolean showingInFullscreen;
    float spanCount;
    private GroupCallStatusIcon statusIcon;
    private TextView stopSharingTextView;
    private boolean swipeToBack;
    private float swipeToBackDy;
    public GroupCallGridCell tabletGridView;
    public VoIPTextureView textureView;
    Bitmap thumb;
    Paint thumbPaint;
    private boolean updateNextLayoutAnimated;
    boolean useSpanSize;
    private boolean videoIsPaused;
    private float videoIsPausedProgress;
    Paint gradientPaint = new Paint(1);
    Paint speakingPaint = new Paint(1);
    public float progressToNoVideoStub = 1.0f;
    ImageReceiver imageReceiver = new ImageReceiver();
    ArrayList<Runnable> onFirstFrameRunnables = new ArrayList<>();
    private Runnable noRtmpStreamCallback = new Runnable() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda5
        @Override // java.lang.Runnable
        public final void run() {
            GroupCallMiniTextureView.this.lambda$new$0();
        }
    };
    private Rect rect = new Rect();

    static /* synthetic */ float access$116(GroupCallMiniTextureView groupCallMiniTextureView, float f) {
        float f2 = groupCallMiniTextureView.progressToBackground + f;
        groupCallMiniTextureView.progressToBackground = f2;
        return f2;
    }

    static /* synthetic */ float access$716(GroupCallMiniTextureView groupCallMiniTextureView, float f) {
        float f2 = groupCallMiniTextureView.videoIsPausedProgress + f;
        groupCallMiniTextureView.videoIsPausedProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$724(GroupCallMiniTextureView groupCallMiniTextureView, float f) {
        float f2 = groupCallMiniTextureView.videoIsPausedProgress - f;
        groupCallMiniTextureView.videoIsPausedProgress = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.textureView.renderer.isFirstFrameRendered()) {
            return;
        }
        this.textureView.animate().cancel();
        this.textureView.animate().alpha(0.0f).setDuration(150L).start();
        this.noRtmpStreamTextView.animate().cancel();
        this.noRtmpStreamTextView.animate().alpha(1.0f).setDuration(150L).start();
    }

    public GroupCallMiniTextureView(final GroupCallRenderersContainer groupCallRenderersContainer, ArrayList<GroupCallMiniTextureView> arrayList, final ChatObject.Call call, final GroupCallActivity groupCallActivity) {
        super(groupCallRenderersContainer.getContext());
        this.call = call;
        this.currentAccount = groupCallActivity.getCurrentAccount();
        CrossOutDrawable crossOutDrawable = new CrossOutDrawable(groupCallRenderersContainer.getContext(), R.drawable.calls_video, null);
        this.pausedVideoDrawable = crossOutDrawable;
        crossOutDrawable.setCrossOut(true, false);
        this.pausedVideoDrawable.setOffsets(-AndroidUtilities.dp(4.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        this.pausedVideoDrawable.setStrokeWidth(AndroidUtilities.dpf2(3.4f));
        this.castingScreenDrawable = groupCallRenderersContainer.getContext().getResources().getDrawable(R.drawable.screencast_big).mutate();
        final TextPaint textPaint = new TextPaint(1);
        textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        textPaint.setColor(-1);
        final TextPaint textPaint2 = new TextPaint(1);
        textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textPaint2.setTextSize(AndroidUtilities.dp(15.0f));
        textPaint2.setColor(-1);
        final String string = LocaleController.getString("VoipVideoOnPause", R.string.VoipVideoOnPause);
        final StaticLayout staticLayout = new StaticLayout(LocaleController.getString("VoipVideoScreenSharingTwoLines", R.string.VoipVideoScreenSharingTwoLines), textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(call.chatId));
        final StaticLayout staticLayout2 = new StaticLayout(LocaleController.formatString("VoipVideoNotAvailable", R.string.VoipVideoNotAvailable, LocaleController.formatPluralString("Participants", MessagesController.getInstance(this.currentAccount).groupCallVideoMaxParticipants, new Object[0])), textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        final String string2 = LocaleController.getString("VoipVideoScreenSharing", R.string.VoipVideoScreenSharing);
        final float measureText = textPaint.measureText(string);
        final float measureText2 = textPaint2.measureText(string2);
        VoIPTextureView voIPTextureView = new VoIPTextureView(groupCallRenderersContainer.getContext(), false, false, true, true) { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.1
            float overlayIconAlphaFrom;

            @Override // org.telegram.ui.Components.voip.VoIPTextureView
            public void animateToLayout() {
                super.animateToLayout();
                this.overlayIconAlphaFrom = GroupCallMiniTextureView.this.overlayIconAlpha;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.voip.VoIPTextureView
            public void updateRendererSize() {
                super.updateRendererSize();
                ImageView imageView = GroupCallMiniTextureView.this.blurredFlippingStub;
                if (imageView == null || imageView.getParent() == null) {
                    return;
                }
                GroupCallMiniTextureView.this.blurredFlippingStub.getLayoutParams().width = GroupCallMiniTextureView.this.textureView.renderer.getMeasuredWidth();
                GroupCallMiniTextureView.this.blurredFlippingStub.getLayoutParams().height = GroupCallMiniTextureView.this.textureView.renderer.getMeasuredHeight();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.voip.VoIPTextureView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                float f;
                float dp;
                float f2;
                int i;
                float f3;
                if (!this.renderer.isFirstFrameRendered() || ((this.renderer.getAlpha() != 1.0f && this.blurRenderer.getAlpha() != 1.0f) || GroupCallMiniTextureView.this.videoIsPaused)) {
                    if (GroupCallMiniTextureView.this.progressToBackground != 1.0f) {
                        GroupCallMiniTextureView.access$116(GroupCallMiniTextureView.this, 0.10666667f);
                        if (GroupCallMiniTextureView.this.progressToBackground > 1.0f) {
                            GroupCallMiniTextureView.this.progressToBackground = 1.0f;
                        } else {
                            invalidate();
                        }
                    }
                    GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                    if (groupCallMiniTextureView.thumb != null) {
                        canvas.save();
                        float f4 = this.currentThumbScale;
                        canvas.scale(f4, f4, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
                        GroupCallMiniTextureView groupCallMiniTextureView2 = GroupCallMiniTextureView.this;
                        if (groupCallMiniTextureView2.thumbPaint == null) {
                            groupCallMiniTextureView2.thumbPaint = new Paint(1);
                            GroupCallMiniTextureView.this.thumbPaint.setFilterBitmap(true);
                        }
                        canvas.drawBitmap(GroupCallMiniTextureView.this.thumb, (getMeasuredWidth() - GroupCallMiniTextureView.this.thumb.getWidth()) / 2.0f, (getMeasuredHeight() - GroupCallMiniTextureView.this.thumb.getHeight()) / 2.0f, GroupCallMiniTextureView.this.thumbPaint);
                        canvas.restore();
                    } else {
                        groupCallMiniTextureView.imageReceiver.setImageCoords(this.currentClipHorizontal, this.currentClipVertical, getMeasuredWidth() - (this.currentClipHorizontal * 2.0f), getMeasuredHeight() - (this.currentClipVertical * 2.0f));
                        GroupCallMiniTextureView groupCallMiniTextureView3 = GroupCallMiniTextureView.this;
                        groupCallMiniTextureView3.imageReceiver.setAlpha(groupCallMiniTextureView3.progressToBackground);
                        GroupCallMiniTextureView.this.imageReceiver.draw(canvas);
                    }
                    GroupCallMiniTextureView groupCallMiniTextureView4 = GroupCallMiniTextureView.this;
                    ChatObject.VideoParticipant videoParticipant = groupCallMiniTextureView4.participant;
                    if (videoParticipant == call.videoNotAvailableParticipant) {
                        if (groupCallMiniTextureView4.showingInFullscreen || !groupCallRenderersContainer.inFullscreenMode) {
                            float dp2 = AndroidUtilities.dp(48.0f);
                            textPaint.setAlpha(255);
                            canvas.save();
                            canvas.translate((((getMeasuredWidth() - dp2) / 2.0f) - (AndroidUtilities.dp(400.0f) / 2.0f)) + (dp2 / 2.0f), ((getMeasuredHeight() / 2) - dp2) + dp2 + AndroidUtilities.dp(10.0f));
                            staticLayout2.draw(canvas);
                            canvas.restore();
                        }
                        if (GroupCallMiniTextureView.this.stopSharingTextView.getVisibility() != 4) {
                            GroupCallMiniTextureView.this.stopSharingTextView.setVisibility(4);
                        }
                    } else if (!videoParticipant.presentation || !videoParticipant.participant.self) {
                        if (groupCallMiniTextureView4.stopSharingTextView.getVisibility() != 4) {
                            GroupCallMiniTextureView.this.stopSharingTextView.setVisibility(4);
                        }
                        groupCallActivity.cellFlickerDrawable.draw(canvas, GroupCallMiniTextureView.this);
                    } else {
                        if (groupCallMiniTextureView4.stopSharingTextView.getVisibility() != 0) {
                            GroupCallMiniTextureView.this.stopSharingTextView.setVisibility(0);
                            GroupCallMiniTextureView.this.stopSharingTextView.setScaleX(1.0f);
                            GroupCallMiniTextureView.this.stopSharingTextView.setScaleY(1.0f);
                        }
                        float f5 = GroupCallMiniTextureView.this.drawFirst ? 0.0f : groupCallRenderersContainer.progressToFullscreenMode;
                        int dp3 = AndroidUtilities.dp(33.0f);
                        GroupCallMiniTextureView groupCallMiniTextureView5 = GroupCallMiniTextureView.this;
                        if (groupCallMiniTextureView5.animateToFullscreen || groupCallMiniTextureView5.showingInFullscreen) {
                            f = dp3;
                            dp = AndroidUtilities.dp(10.0f) + (AndroidUtilities.dp(39.0f) * groupCallRenderersContainer.progressToFullscreenMode);
                        } else {
                            f = dp3;
                            dp = AndroidUtilities.dp(10.0f) * Math.max(1.0f - groupCallRenderersContainer.progressToFullscreenMode, (GroupCallMiniTextureView.this.showingAsScrimView || GroupCallMiniTextureView.this.animateToScrimView) ? groupCallRenderersContainer.progressToScrimView : 0.0f);
                        }
                        int i2 = (int) (f + dp);
                        int measuredWidth = (getMeasuredWidth() - i2) / 2;
                        float f6 = (GroupCallMiniTextureView.this.showingAsScrimView || GroupCallMiniTextureView.this.animateToScrimView) ? groupCallRenderersContainer.progressToScrimView : 0.0f;
                        GroupCallMiniTextureView groupCallMiniTextureView6 = GroupCallMiniTextureView.this;
                        if (groupCallMiniTextureView6.showingInFullscreen) {
                            f2 = f5;
                        } else {
                            f5 = groupCallMiniTextureView6.animateToFullscreen ? groupCallRenderersContainer.progressToFullscreenMode : f6;
                            f2 = (groupCallMiniTextureView6.showingAsScrimView || GroupCallMiniTextureView.this.animateToScrimView) ? groupCallRenderersContainer.progressToScrimView : groupCallRenderersContainer.progressToFullscreenMode;
                        }
                        float measuredHeight = ((getMeasuredHeight() - i2) / 2) - AndroidUtilities.dp(28.0f);
                        float dp4 = AndroidUtilities.dp(17.0f);
                        float dp5 = AndroidUtilities.dp(74.0f);
                        GroupCallMiniTextureView groupCallMiniTextureView7 = GroupCallMiniTextureView.this;
                        int dp6 = (int) ((measuredHeight - ((dp4 + (dp5 * ((groupCallMiniTextureView7.showingInFullscreen || groupCallMiniTextureView7.animateToFullscreen) ? groupCallRenderersContainer.progressToFullscreenMode : 0.0f))) * f5)) + (AndroidUtilities.dp(17.0f) * f2));
                        GroupCallMiniTextureView.this.castingScreenDrawable.setBounds(measuredWidth, dp6, measuredWidth + i2, dp6 + i2);
                        GroupCallMiniTextureView.this.castingScreenDrawable.draw(canvas);
                        float f7 = groupCallRenderersContainer.progressToFullscreenMode;
                        if (f7 <= 0.0f && f6 <= 0.0f) {
                            GroupCallMiniTextureView.this.stopSharingTextView.setAlpha(0.0f);
                        } else {
                            float max = Math.max(f7, f6) * f5;
                            textPaint2.setAlpha((int) (max * 255.0f));
                            GroupCallMiniTextureView groupCallMiniTextureView8 = GroupCallMiniTextureView.this;
                            if (groupCallMiniTextureView8.animateToFullscreen || groupCallMiniTextureView8.showingInFullscreen) {
                                groupCallMiniTextureView8.stopSharingTextView.setAlpha(max * (1.0f - f6));
                            } else {
                                groupCallMiniTextureView8.stopSharingTextView.setAlpha(0.0f);
                            }
                            canvas.drawText(string2, (measuredWidth - (measureText2 / 2.0f)) + (i2 / 2.0f), AndroidUtilities.dp(32.0f) + i, textPaint2);
                        }
                        GroupCallMiniTextureView.this.stopSharingTextView.setTranslationY(((AndroidUtilities.dp(72.0f) + i) + GroupCallMiniTextureView.this.swipeToBackDy) - this.currentClipVertical);
                        GroupCallMiniTextureView.this.stopSharingTextView.setTranslationX(((getMeasuredWidth() - GroupCallMiniTextureView.this.stopSharingTextView.getMeasuredWidth()) / 2.0f) - this.currentClipHorizontal);
                        float f8 = groupCallRenderersContainer.progressToFullscreenMode;
                        if (f8 < 1.0f && f6 < 1.0f) {
                            TextPaint textPaint3 = textPaint;
                            double max2 = Math.max(f8, f6);
                            Double.isNaN(max2);
                            textPaint3.setAlpha((int) ((1.0d - max2) * 255.0d));
                            canvas.save();
                            canvas.translate((measuredWidth - (AndroidUtilities.dp(400.0f) / 2.0f)) + (i2 / 2.0f), i + AndroidUtilities.dp(10.0f));
                            staticLayout.draw(canvas);
                            canvas.restore();
                        }
                    }
                    invalidate();
                }
                GroupCallMiniTextureView.this.noRtmpStreamTextView.setTranslationY((((getMeasuredHeight() - GroupCallMiniTextureView.this.noRtmpStreamTextView.getMeasuredHeight()) / 2.0f) + GroupCallMiniTextureView.this.swipeToBackDy) - this.currentClipVertical);
                GroupCallMiniTextureView.this.noRtmpStreamTextView.setTranslationX(((getMeasuredWidth() - GroupCallMiniTextureView.this.noRtmpStreamTextView.getMeasuredWidth()) / 2.0f) - this.currentClipHorizontal);
                ImageView imageView = GroupCallMiniTextureView.this.blurredFlippingStub;
                if (imageView != null && imageView.getParent() != null) {
                    GroupCallMiniTextureView groupCallMiniTextureView9 = GroupCallMiniTextureView.this;
                    groupCallMiniTextureView9.blurredFlippingStub.setScaleX(groupCallMiniTextureView9.textureView.renderer.getScaleX());
                    GroupCallMiniTextureView groupCallMiniTextureView10 = GroupCallMiniTextureView.this;
                    groupCallMiniTextureView10.blurredFlippingStub.setScaleY(groupCallMiniTextureView10.textureView.renderer.getScaleY());
                }
                super.dispatchDraw(canvas);
                float measuredHeight2 = (getMeasuredHeight() - this.currentClipVertical) - AndroidUtilities.dp(80.0f);
                if (GroupCallMiniTextureView.this.participant != call.videoNotAvailableParticipant) {
                    canvas.save();
                    GroupCallMiniTextureView groupCallMiniTextureView11 = GroupCallMiniTextureView.this;
                    if ((groupCallMiniTextureView11.showingInFullscreen || groupCallMiniTextureView11.animateToFullscreen) && !GroupCallActivity.isLandscapeMode && !GroupCallActivity.isTabletMode) {
                        GroupCallRenderersContainer groupCallRenderersContainer2 = groupCallRenderersContainer;
                        measuredHeight2 -= (AndroidUtilities.dp(90.0f) * groupCallRenderersContainer2.progressToFullscreenMode) * (1.0f - groupCallRenderersContainer2.progressToHideUi);
                    }
                    canvas.translate(0.0f, measuredHeight2);
                    canvas.drawPaint(GroupCallMiniTextureView.this.gradientPaint);
                    canvas.restore();
                }
                if (GroupCallMiniTextureView.this.videoIsPaused || GroupCallMiniTextureView.this.videoIsPausedProgress != 0.0f) {
                    if (!GroupCallMiniTextureView.this.videoIsPaused || GroupCallMiniTextureView.this.videoIsPausedProgress == 1.0f) {
                        if (!GroupCallMiniTextureView.this.videoIsPaused && GroupCallMiniTextureView.this.videoIsPausedProgress != 0.0f) {
                            GroupCallMiniTextureView.access$724(GroupCallMiniTextureView.this, 0.064f);
                            if (GroupCallMiniTextureView.this.videoIsPausedProgress < 0.0f) {
                                GroupCallMiniTextureView.this.videoIsPausedProgress = 0.0f;
                            } else {
                                invalidate();
                            }
                        }
                    } else {
                        GroupCallMiniTextureView.access$716(GroupCallMiniTextureView.this, 0.064f);
                        if (GroupCallMiniTextureView.this.videoIsPausedProgress > 1.0f) {
                            GroupCallMiniTextureView.this.videoIsPausedProgress = 1.0f;
                        } else {
                            invalidate();
                        }
                    }
                    float f9 = GroupCallMiniTextureView.this.videoIsPausedProgress;
                    if (isInAnimation()) {
                        float f10 = this.overlayIconAlphaFrom;
                        float f11 = this.animationProgress;
                        f3 = (f10 * (1.0f - f11)) + (GroupCallMiniTextureView.this.overlayIconAlpha * f11);
                    } else {
                        f3 = GroupCallMiniTextureView.this.overlayIconAlpha;
                    }
                    float f12 = f9 * f3;
                    if (f12 <= 0.0f) {
                        return;
                    }
                    float dp7 = AndroidUtilities.dp(48.0f);
                    float measuredWidth2 = (getMeasuredWidth() - dp7) / 2.0f;
                    float measuredHeight3 = (getMeasuredHeight() - dp7) / 2.0f;
                    if (GroupCallMiniTextureView.this.participant == call.videoNotAvailableParticipant) {
                        measuredHeight3 -= dp7 / 2.5f;
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    float f13 = measuredHeight3 + dp7;
                    rectF.set((int) measuredWidth2, (int) measuredHeight3, (int) (measuredWidth2 + dp7), (int) f13);
                    if (f12 != 1.0f) {
                        canvas.saveLayerAlpha(rectF, (int) (f12 * 255.0f), 31);
                    } else {
                        canvas.save();
                    }
                    GroupCallMiniTextureView.this.pausedVideoDrawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                    GroupCallMiniTextureView.this.pausedVideoDrawable.draw(canvas);
                    canvas.restore();
                    float f14 = f12 * groupCallRenderersContainer.progressToFullscreenMode;
                    if (f14 <= 0.0f || GroupCallMiniTextureView.this.participant == call.videoNotAvailableParticipant) {
                        return;
                    }
                    textPaint.setAlpha((int) (f14 * 255.0f));
                    canvas.drawText(string, (measuredWidth2 - (measureText / 2.0f)) + (dp7 / 2.0f), f13 + AndroidUtilities.dp(16.0f), textPaint);
                }
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                if (groupCallMiniTextureView.inPinchToZoom && view == groupCallMiniTextureView.textureView.renderer) {
                    canvas.save();
                    GroupCallMiniTextureView groupCallMiniTextureView2 = GroupCallMiniTextureView.this;
                    float f = groupCallMiniTextureView2.pinchScale;
                    canvas.scale(f, f, groupCallMiniTextureView2.pinchCenterX, groupCallMiniTextureView2.pinchCenterY);
                    GroupCallMiniTextureView groupCallMiniTextureView3 = GroupCallMiniTextureView.this;
                    canvas.translate(groupCallMiniTextureView3.pinchTranslationX, groupCallMiniTextureView3.pinchTranslationY);
                    boolean drawChild = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return drawChild;
                }
                return super.drawChild(canvas, view, j);
            }

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                GroupCallMiniTextureView.this.invalidateFromChild = true;
                GroupCallMiniTextureView.this.invalidate();
                GroupCallMiniTextureView.this.invalidateFromChild = false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.voip.VoIPTextureView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                int i5;
                ChatObject.VideoParticipant videoParticipant;
                GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                if (groupCallMiniTextureView.attached && groupCallMiniTextureView.checkScale) {
                    TextureViewRenderer textureViewRenderer = this.renderer;
                    if (textureViewRenderer.rotatedFrameHeight != 0 && textureViewRenderer.rotatedFrameWidth != 0) {
                        if (GroupCallMiniTextureView.this.showingAsScrimView) {
                            GroupCallMiniTextureView.this.textureView.scaleType = VoIPTextureView.SCALE_TYPE_FIT;
                        } else {
                            GroupCallMiniTextureView groupCallMiniTextureView2 = GroupCallMiniTextureView.this;
                            if (groupCallMiniTextureView2.showingInFullscreen) {
                                groupCallMiniTextureView2.textureView.scaleType = VoIPTextureView.SCALE_TYPE_FIT;
                            } else if (groupCallRenderersContainer.inFullscreenMode) {
                                groupCallMiniTextureView2.textureView.scaleType = VoIPTextureView.SCALE_TYPE_FILL;
                            } else if (groupCallMiniTextureView2.participant.presentation) {
                                groupCallMiniTextureView2.textureView.scaleType = VoIPTextureView.SCALE_TYPE_FIT;
                            } else {
                                groupCallMiniTextureView2.textureView.scaleType = VoIPTextureView.SCALE_TYPE_ADAPTIVE;
                            }
                        }
                        GroupCallMiniTextureView.this.checkScale = false;
                    }
                }
                super.onLayout(z, i, i2, i3, i4);
                TextureViewRenderer textureViewRenderer2 = this.renderer;
                int i6 = textureViewRenderer2.rotatedFrameHeight;
                if (i6 == 0 || (i5 = textureViewRenderer2.rotatedFrameWidth) == 0 || (videoParticipant = GroupCallMiniTextureView.this.participant) == null) {
                    return;
                }
                videoParticipant.setAspectRatio(i5, i6, call);
            }

            @Override // org.telegram.ui.Components.voip.VoIPTextureView, android.view.View, android.view.ViewParent
            public void requestLayout() {
                GroupCallMiniTextureView.this.requestLayout();
                super.requestLayout();
            }

            @Override // org.telegram.ui.Components.voip.VoIPTextureView
            protected void onFirstFrameRendered() {
                int i;
                ChatObject.VideoParticipant videoParticipant;
                invalidate();
                ChatObject.Call call2 = call;
                if (call2 != null && call2.call.rtmp_stream && GroupCallMiniTextureView.this.postedNoRtmpStreamCallback) {
                    AndroidUtilities.cancelRunOnUIThread(GroupCallMiniTextureView.this.noRtmpStreamCallback);
                    GroupCallMiniTextureView.this.postedNoRtmpStreamCallback = false;
                    GroupCallMiniTextureView.this.noRtmpStreamTextView.animate().cancel();
                    GroupCallMiniTextureView.this.noRtmpStreamTextView.animate().alpha(0.0f).setDuration(150L).start();
                    GroupCallMiniTextureView.this.textureView.animate().cancel();
                    GroupCallMiniTextureView.this.textureView.animate().alpha(1.0f).setDuration(150L).start();
                }
                if (!GroupCallMiniTextureView.this.videoIsPaused && this.renderer.getAlpha() != 1.0f) {
                    this.renderer.animate().setDuration(300L).alpha(1.0f);
                }
                TextureView textureView = this.blurRenderer;
                if (textureView != null && textureView.getAlpha() != 1.0f) {
                    this.blurRenderer.animate().setDuration(300L).alpha(1.0f);
                }
                ImageView imageView = GroupCallMiniTextureView.this.blurredFlippingStub;
                if (imageView != null && imageView.getParent() != null) {
                    if (GroupCallMiniTextureView.this.blurredFlippingStub.getAlpha() == 1.0f) {
                        GroupCallMiniTextureView.this.blurredFlippingStub.animate().alpha(0.0f).setDuration(300L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.1.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (GroupCallMiniTextureView.this.blurredFlippingStub.getParent() != null) {
                                    GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                                    groupCallMiniTextureView.textureView.removeView(groupCallMiniTextureView.blurredFlippingStub);
                                }
                            }
                        }).start();
                    } else if (GroupCallMiniTextureView.this.blurredFlippingStub.getParent() != null) {
                        GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                        groupCallMiniTextureView.textureView.removeView(groupCallMiniTextureView.blurredFlippingStub);
                    }
                }
                TextureViewRenderer textureViewRenderer = this.renderer;
                int i2 = textureViewRenderer.rotatedFrameHeight;
                if (i2 == 0 || (i = textureViewRenderer.rotatedFrameWidth) == 0 || (videoParticipant = GroupCallMiniTextureView.this.participant) == null) {
                    return;
                }
                videoParticipant.setAspectRatio(i, i2, call);
            }
        };
        this.textureView = voIPTextureView;
        voIPTextureView.renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        this.parentContainer = groupCallRenderersContainer;
        this.attachedRenderers = arrayList;
        this.activity = groupCallActivity;
        this.textureView.renderer.init(VideoCapturerDevice.getEglBase().getEglBaseContext(), new RendererCommon.RendererEvents() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.2
            @Override // org.webrtc.RendererCommon.RendererEvents
            public void onFrameResolutionChanged(int i, int i2, int i3) {
            }

            @Override // org.webrtc.RendererCommon.RendererEvents
            public void onFirstFrameRendered() {
                for (int i = 0; i < GroupCallMiniTextureView.this.onFirstFrameRunnables.size(); i++) {
                    AndroidUtilities.cancelRunOnUIThread(GroupCallMiniTextureView.this.onFirstFrameRunnables.get(i));
                    GroupCallMiniTextureView.this.onFirstFrameRunnables.get(i).run();
                }
                GroupCallMiniTextureView.this.onFirstFrameRunnables.clear();
            }
        });
        this.textureView.attachBackgroundRenderer();
        setClipChildren(false);
        this.textureView.renderer.setAlpha(0.0f);
        addView(this.textureView);
        NoVideoStubLayout noVideoStubLayout = new NoVideoStubLayout(getContext());
        this.noVideoStubLayout = noVideoStubLayout;
        addView(noVideoStubLayout);
        SimpleTextView simpleTextView = new SimpleTextView(groupCallRenderersContainer.getContext());
        this.nameView = simpleTextView;
        simpleTextView.setTextSize(13);
        simpleTextView.setTextColor(ColorUtils.setAlphaComponent(-1, 229));
        simpleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        simpleTextView.setFullTextMaxLines(1);
        simpleTextView.setBuildFullLayout(true);
        FrameLayout frameLayout = new FrameLayout(groupCallRenderersContainer.getContext());
        this.infoContainer = frameLayout;
        frameLayout.addView(simpleTextView, LayoutHelper.createFrame(-1, -2.0f, 19, 32.0f, 0.0f, 8.0f, 0.0f));
        addView(this.infoContainer, LayoutHelper.createFrame(-1, 32.0f));
        this.speakingPaint.setStyle(Paint.Style.STROKE);
        this.speakingPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.speakingPaint.setColor(Theme.getColor("voipgroup_speakingText"));
        this.infoContainer.setClipChildren(false);
        RLottieImageView rLottieImageView = new RLottieImageView(groupCallRenderersContainer.getContext());
        this.micIconView = rLottieImageView;
        addView(rLottieImageView, LayoutHelper.createFrame(24, 24.0f, 0, 4.0f, 6.0f, 4.0f, 0.0f));
        ImageView imageView = new ImageView(groupCallRenderersContainer.getContext());
        this.screencastIcon = imageView;
        addView(imageView, LayoutHelper.createFrame(24, 24.0f, 0, 4.0f, 6.0f, 4.0f, 0.0f));
        imageView.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
        imageView.setImageDrawable(ContextCompat.getDrawable(groupCallRenderersContainer.getContext(), R.drawable.voicechat_screencast));
        imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY));
        Drawable createSimpleSelectorRoundRectDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(19.0f), 0, ColorUtils.setAlphaComponent(-1, 100));
        TextView textView = new TextView(groupCallRenderersContainer.getContext()) { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.3
            @Override // android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (Math.abs(GroupCallMiniTextureView.this.stopSharingTextView.getAlpha() - 1.0f) > 0.001f) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.stopSharingTextView = textView;
        textView.setText(LocaleController.getString("VoipVideoScreenStopSharing", R.string.VoipVideoScreenStopSharing));
        this.stopSharingTextView.setTextSize(1, 15.0f);
        this.stopSharingTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.stopSharingTextView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
        this.stopSharingTextView.setTextColor(-1);
        this.stopSharingTextView.setBackground(createSimpleSelectorRoundRectDrawable);
        this.stopSharingTextView.setGravity(17);
        this.stopSharingTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallMiniTextureView.this.lambda$new$1(view);
            }
        });
        addView(this.stopSharingTextView, LayoutHelper.createFrame(-2, 38, 51));
        TextView textView2 = new TextView(groupCallRenderersContainer.getContext());
        this.noRtmpStreamTextView = textView2;
        textView2.setTextSize(1, 15.0f);
        this.noRtmpStreamTextView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
        this.noRtmpStreamTextView.setTextColor(Theme.getColor("voipgroup_lastSeenText"));
        this.noRtmpStreamTextView.setBackground(createSimpleSelectorRoundRectDrawable);
        this.noRtmpStreamTextView.setGravity(17);
        this.noRtmpStreamTextView.setAlpha(0.0f);
        if (ChatObject.canManageCalls(chat)) {
            this.noRtmpStreamTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.NoRtmpStreamFromAppOwner)));
        } else {
            this.noRtmpStreamTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("NoRtmpStreamFromAppViewer", R.string.NoRtmpStreamFromAppViewer, chat.title)));
        }
        addView(this.noRtmpStreamTextView, LayoutHelper.createFrame(-2, -2, 51));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().stopScreenCapture();
        }
        this.stopSharingTextView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(180L).start();
    }

    public boolean isInsideStopScreenButton(float f, float f2) {
        this.stopSharingTextView.getHitRect(this.rect);
        return this.rect.contains((int) f, (int) f2);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.attached) {
            float y = (((this.textureView.getY() + this.textureView.getMeasuredHeight()) - this.textureView.currentClipVertical) - this.infoContainer.getMeasuredHeight()) + this.swipeToBackDy;
            if (this.showingAsScrimView || this.animateToScrimView) {
                this.infoContainer.setAlpha(1.0f - this.parentContainer.progressToScrimView);
                this.micIconView.setAlpha(1.0f - this.parentContainer.progressToScrimView);
            } else if (this.showingInFullscreen || this.animateToFullscreen) {
                if (!GroupCallActivity.isLandscapeMode && !GroupCallActivity.isTabletMode) {
                    GroupCallRenderersContainer groupCallRenderersContainer = this.parentContainer;
                    y -= (AndroidUtilities.dp(90.0f) * groupCallRenderersContainer.progressToFullscreenMode) * (1.0f - groupCallRenderersContainer.progressToHideUi);
                }
                this.infoContainer.setAlpha(1.0f);
                this.micIconView.setAlpha(1.0f);
            } else if (this.secondaryView != null) {
                this.infoContainer.setAlpha(1.0f - this.parentContainer.progressToFullscreenMode);
                this.micIconView.setAlpha(1.0f - this.parentContainer.progressToFullscreenMode);
            } else {
                this.infoContainer.setAlpha(1.0f);
                this.micIconView.setAlpha(1.0f);
            }
            if (this.showingInFullscreen || this.animateToFullscreen) {
                this.nameView.setFullAlpha(this.parentContainer.progressToFullscreenMode);
            } else {
                this.nameView.setFullAlpha(0.0f);
            }
            this.micIconView.setTranslationX(this.infoContainer.getX());
            this.micIconView.setTranslationY(y - AndroidUtilities.dp(2.0f));
            if (this.screencastIcon.getVisibility() == 0) {
                this.screencastIcon.setTranslationX((this.textureView.getMeasuredWidth() - (this.textureView.currentClipHorizontal * 2.0f)) - AndroidUtilities.dp(32.0f));
                this.screencastIcon.setTranslationY(y - AndroidUtilities.dp(2.0f));
                ImageView imageView = this.screencastIcon;
                GroupCallRenderersContainer groupCallRenderersContainer2 = this.parentContainer;
                imageView.setAlpha(Math.min(1.0f - groupCallRenderersContainer2.progressToFullscreenMode, 1.0f - groupCallRenderersContainer2.progressToScrimView));
            }
            this.infoContainer.setTranslationY(y);
            this.infoContainer.setTranslationX(this.drawFirst ? 0.0f : AndroidUtilities.dp(6.0f) * this.parentContainer.progressToFullscreenMode);
        }
        super.dispatchDraw(canvas);
        if (this.attached) {
            GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
            if (groupCallStatusIcon != null) {
                boolean z = groupCallStatusIcon.isSpeaking;
                if (z) {
                    float f = this.progressToSpeaking;
                    if (f != 1.0f) {
                        float f2 = f + 0.053333335f;
                        this.progressToSpeaking = f2;
                        if (f2 > 1.0f) {
                            this.progressToSpeaking = 1.0f;
                        } else {
                            invalidate();
                        }
                    }
                }
                if (!z) {
                    float f3 = this.progressToSpeaking;
                    if (f3 != 0.0f) {
                        float f4 = f3 - 0.053333335f;
                        this.progressToSpeaking = f4;
                        if (f4 < 0.0f) {
                            this.progressToSpeaking = 0.0f;
                        } else {
                            invalidate();
                        }
                    }
                }
            }
            float f5 = this.progressToSpeaking;
            GroupCallRenderersContainer groupCallRenderersContainer3 = this.parentContainer;
            float f6 = (1.0f - groupCallRenderersContainer3.progressToFullscreenMode) * f5 * (1.0f - groupCallRenderersContainer3.progressToScrimView);
            if (f5 <= 0.0f) {
                return;
            }
            this.speakingPaint.setAlpha((int) (f6 * 255.0f));
            float max = (Math.max(0.0f, 1.0f - (Math.abs(this.swipeToBackDy) / AndroidUtilities.dp(300.0f))) * 0.1f) + 0.9f;
            canvas.save();
            RectF rectF = AndroidUtilities.rectTmp;
            float x = this.textureView.getX();
            VoIPTextureView voIPTextureView = this.textureView;
            float f7 = x + voIPTextureView.currentClipHorizontal;
            float y2 = voIPTextureView.getY();
            VoIPTextureView voIPTextureView2 = this.textureView;
            float f8 = y2 + voIPTextureView2.currentClipVertical;
            float x2 = voIPTextureView2.getX() + this.textureView.getMeasuredWidth();
            VoIPTextureView voIPTextureView3 = this.textureView;
            rectF.set(f7, f8, x2 - voIPTextureView3.currentClipHorizontal, (voIPTextureView3.getY() + this.textureView.getMeasuredHeight()) - this.textureView.currentClipVertical);
            canvas.scale(max, max, rectF.centerX(), rectF.centerY());
            canvas.translate(0.0f, this.swipeToBackDy);
            float f9 = this.textureView.roundRadius;
            canvas.drawRoundRect(rectF, f9, f9, this.speakingPaint);
            canvas.restore();
        }
    }

    public void getRenderBufferBitmap(GlGenericDrawer.TextureCallback textureCallback) {
        this.textureView.renderer.getRenderBufferBitmap(textureCallback);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (this.swipeToBack && (view == this.textureView || view == this.noVideoStubLayout)) {
            float max = (Math.max(0.0f, 1.0f - (Math.abs(this.swipeToBackDy) / AndroidUtilities.dp(300.0f))) * 0.1f) + 0.9f;
            canvas.save();
            canvas.scale(max, max, view.getX() + (view.getMeasuredWidth() / 2.0f), view.getY() + (view.getMeasuredHeight() / 2.0f));
            canvas.translate(0.0f, this.swipeToBackDy);
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }
        return super.drawChild(canvas, view, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0196  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        float f;
        int dp;
        GroupCallGridCell groupCallGridCell;
        float f2;
        int dp2;
        SimpleTextView simpleTextView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.infoContainer.getLayoutParams();
        int i3 = layoutParams.leftMargin;
        float f3 = this.call.call.rtmp_stream ? 0.0f : 1.0f;
        boolean z = this.lastLandscapeMode;
        boolean z2 = GroupCallActivity.isLandscapeMode;
        if (z != z2) {
            this.checkScale = true;
            this.lastLandscapeMode = z2;
        }
        int dp3 = AndroidUtilities.dp(2.0f);
        layoutParams.rightMargin = dp3;
        layoutParams.leftMargin = dp3;
        if (this.updateNextLayoutAnimated) {
            this.nameView.animate().scaleX(f3).scaleY(f3).start();
            this.micIconView.animate().scaleX(f3).scaleY(f3).start();
        } else {
            this.nameView.animate().cancel();
            this.nameView.setScaleX(f3);
            this.nameView.setScaleY(f3);
            this.micIconView.animate().cancel();
            this.micIconView.setScaleX(f3);
            this.micIconView.setScaleY(f3);
            this.infoContainer.animate().cancel();
        }
        int i4 = 0;
        this.updateNextLayoutAnimated = false;
        if (this.showingInFullscreen) {
            updateSize(0);
            this.overlayIconAlpha = 1.0f;
            if (GroupCallActivity.isTabletMode) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) - AndroidUtilities.dp(328.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(4.0f), 1073741824));
            } else if (!GroupCallActivity.isLandscapeMode) {
                int size = View.MeasureSpec.getSize(i2);
                if (!this.call.call.rtmp_stream) {
                    size -= AndroidUtilities.dp(92.0f);
                }
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            } else {
                int size2 = View.MeasureSpec.getSize(i);
                if (!this.call.call.rtmp_stream) {
                    size2 -= AndroidUtilities.dp(92.0f);
                }
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(size2, 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
            }
        } else if (this.showingAsScrimView) {
            this.overlayIconAlpha = 1.0f;
            int min = Math.min(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2)) - (AndroidUtilities.dp(14.0f) * 2);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), View.MeasureSpec.makeMeasureSpec(min + getPaddingBottom(), 1073741824));
        } else if (this.useSpanSize) {
            this.overlayIconAlpha = 1.0f;
            int i5 = 6;
            if ((!GroupCallActivity.isTabletMode || this.tabletGridView == null) && !GroupCallActivity.isLandscapeMode) {
                i5 = 2;
            }
            if (this.tabletGridView != null) {
                dp = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(344.0f);
            } else if (GroupCallActivity.isTabletMode) {
                dp = AndroidUtilities.dp(320.0f);
            } else {
                int size3 = View.MeasureSpec.getSize(i) - (AndroidUtilities.dp(14.0f) * 2);
                if (GroupCallActivity.isLandscapeMode) {
                    i4 = -AndroidUtilities.dp(90.0f);
                }
                f = size3 + i4;
                float f4 = (this.spanCount / i5) * f;
                groupCallGridCell = this.tabletGridView;
                if (groupCallGridCell == null) {
                    f2 = groupCallGridCell.getItemHeight() - AndroidUtilities.dp(4.0f);
                    dp2 = AndroidUtilities.dp(4.0f);
                } else {
                    if (GroupCallActivity.isTabletMode) {
                        f2 = f / 2.0f;
                    } else {
                        f2 = f / (GroupCallActivity.isLandscapeMode ? 3 : 2);
                    }
                    dp2 = AndroidUtilities.dp(2.0f);
                }
                float f5 = f4 - dp2;
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.infoContainer.getLayoutParams();
                float dp4 = this.screencastIcon.getVisibility() != 0 ? f5 - AndroidUtilities.dp(28.0f) : f5;
                updateSize((int) dp4);
                layoutParams2.width = (int) (dp4 - (layoutParams2.leftMargin * 2));
                super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) f5, 1073741824), View.MeasureSpec.makeMeasureSpec((int) f2, 1073741824));
            }
            f = dp;
            float f42 = (this.spanCount / i5) * f;
            groupCallGridCell = this.tabletGridView;
            if (groupCallGridCell == null) {
            }
            float f52 = f42 - dp2;
            FrameLayout.LayoutParams layoutParams22 = (FrameLayout.LayoutParams) this.infoContainer.getLayoutParams();
            if (this.screencastIcon.getVisibility() != 0) {
            }
            updateSize((int) dp4);
            layoutParams22.width = (int) (dp4 - (layoutParams22.leftMargin * 2));
            super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) f52, 1073741824), View.MeasureSpec.makeMeasureSpec((int) f2, 1073741824));
        } else {
            this.overlayIconAlpha = 0.0f;
            super.onMeasure(i, i2);
        }
        int size4 = View.MeasureSpec.getSize(i2) + (View.MeasureSpec.getSize(i) << 16);
        if (this.lastSize != size4) {
            this.lastSize = size4;
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(120.0f), 0, ColorUtils.setAlphaComponent(-16777216, 120), Shader.TileMode.CLAMP);
            this.gradientShader = linearGradient;
            this.gradientPaint.setShader(linearGradient);
        }
        this.nameView.setPivotX(0.0f);
        this.nameView.setPivotY(simpleTextView.getMeasuredHeight() / 2.0f);
    }

    public static GroupCallMiniTextureView getOrCreate(ArrayList<GroupCallMiniTextureView> arrayList, GroupCallRenderersContainer groupCallRenderersContainer, GroupCallGridCell groupCallGridCell, GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell, GroupCallGridCell groupCallGridCell2, ChatObject.VideoParticipant videoParticipant, ChatObject.Call call, GroupCallActivity groupCallActivity) {
        GroupCallMiniTextureView groupCallMiniTextureView;
        int i = 0;
        while (true) {
            if (i >= arrayList.size()) {
                groupCallMiniTextureView = null;
                break;
            } else if (videoParticipant.equals(arrayList.get(i).participant)) {
                groupCallMiniTextureView = arrayList.get(i);
                break;
            } else {
                i++;
            }
        }
        if (groupCallMiniTextureView == null) {
            groupCallMiniTextureView = new GroupCallMiniTextureView(groupCallRenderersContainer, arrayList, call, groupCallActivity);
        }
        if (groupCallGridCell != null) {
            groupCallMiniTextureView.setPrimaryView(groupCallGridCell);
        }
        if (groupCallUserCell != null) {
            groupCallMiniTextureView.setSecondaryView(groupCallUserCell);
        }
        if (groupCallGridCell2 != null) {
            groupCallMiniTextureView.setTabletGridView(groupCallGridCell2);
        }
        return groupCallMiniTextureView;
    }

    public void setTabletGridView(GroupCallGridCell groupCallGridCell) {
        if (this.tabletGridView != groupCallGridCell) {
            this.tabletGridView = groupCallGridCell;
            updateAttachState(true);
        }
    }

    public void setPrimaryView(GroupCallGridCell groupCallGridCell) {
        if (this.primaryView != groupCallGridCell) {
            this.primaryView = groupCallGridCell;
            this.checkScale = true;
            updateAttachState(true);
        }
    }

    public void setSecondaryView(GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell) {
        if (this.secondaryView != groupCallUserCell) {
            this.secondaryView = groupCallUserCell;
            this.checkScale = true;
            updateAttachState(true);
        }
    }

    public void setShowingAsScrimView(boolean z, boolean z2) {
        this.showingAsScrimView = z;
        updateAttachState(z2);
    }

    public void setShowingInFullscreen(boolean z, boolean z2) {
        if (this.showingInFullscreen != z) {
            this.showingInFullscreen = z;
            this.checkScale = true;
            updateAttachState(z2);
        }
    }

    public void setFullscreenMode(boolean z, boolean z2) {
        if (this.isFullscreenMode != z) {
            this.isFullscreenMode = z;
            updateAttachState(!(this.primaryView == null && this.tabletGridView == null) && z2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:303:0x0180, code lost:
        if (org.telegram.messenger.voip.VoIPService.getSharedInstance().getVideoState(r23.participant.presentation) == 2) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0182, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x01bd, code lost:
        if (r1 != false) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x018e, code lost:
        if (r1 != r11.videoNotAvailableParticipant) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0196, code lost:
        if (org.telegram.messenger.ChatObject.Call.videoIsActive(r10, r1.presentation, r11) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0089, code lost:
        if (r23.participant != r10.videoNotAvailableParticipant) goto L282;
     */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0404  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x04e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x04f0  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0563  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x053e  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0570  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x05a1  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0664  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x069a  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0676  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x044c  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0496  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x046e  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x02d1  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02e7  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x036f  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x039d  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x03ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateAttachState(boolean z) {
        boolean z2;
        boolean z3;
        int dp;
        float f;
        int i;
        boolean z4;
        ViewGroup.MarginLayoutParams marginLayoutParams;
        ChatObject.VideoParticipant videoParticipant;
        ChatObject.VideoParticipant videoParticipant2;
        long peerId;
        ImageLocation forChat;
        ImageLocation forChat2;
        TLRPC$User tLRPC$User;
        boolean z5;
        BitmapDrawable imageFromMemory;
        ChatObject.VideoParticipant videoParticipant3;
        TLRPC$TL_groupCallParticipantVideo tLRPC$TL_groupCallParticipantVideo;
        TLRPC$TL_groupCallParticipantVideo tLRPC$TL_groupCallParticipantVideo2;
        ValueAnimator valueAnimator;
        GroupCallRenderersContainer groupCallRenderersContainer;
        ValueAnimator valueAnimator2;
        ChatObject.VideoParticipant videoParticipant4;
        GroupCallGridCell groupCallGridCell;
        if (this.forceDetached) {
            return;
        }
        boolean z6 = false;
        if (this.call.call.rtmp_stream) {
            int dp2 = AndroidUtilities.dp(this.showingInFullscreen ? 36.0f : 21.0f);
            this.noRtmpStreamTextView.setPadding(dp2, 0, dp2, 0);
        }
        if (this.participant == null && ((groupCallGridCell = this.primaryView) != null || this.secondaryView != null || this.tabletGridView != null)) {
            if (groupCallGridCell != null) {
                this.participant = groupCallGridCell.getParticipant();
            } else {
                GroupCallGridCell groupCallGridCell2 = this.tabletGridView;
                if (groupCallGridCell2 != null) {
                    this.participant = groupCallGridCell2.getParticipant();
                } else {
                    this.participant = this.secondaryView.getVideoParticipant();
                }
            }
        }
        boolean z7 = this.attached;
        float f2 = 1.0f;
        if (z7 && !this.showingInFullscreen) {
            boolean z8 = VoIPService.getSharedInstance() == null;
            if (!GroupCallActivity.paused && (videoParticipant4 = this.participant) != null) {
                if (this.secondaryView == null) {
                    if (ChatObject.Call.videoIsActive(videoParticipant4.participant, videoParticipant4.presentation, this.call)) {
                        ChatObject.Call call = this.call;
                        if (!call.canStreamVideo) {
                        }
                    }
                }
                if (!z8 || (this.primaryView == null && this.secondaryView == null && this.tabletGridView == null && !this.showingAsScrimView && !this.animateToScrimView)) {
                    this.attached = false;
                    saveThumb();
                    if (this.textureView.currentAnimation != null && z8) {
                        this.parentContainer.detach(this);
                        animate().scaleX(0.5f).scaleY(0.5f).alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.4
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                this.setScaleX(1.0f);
                                this.setScaleY(1.0f);
                                this.setAlpha(1.0f);
                                GroupCallMiniTextureView.this.parentContainer.removeView(this);
                                GroupCallMiniTextureView.this.release();
                            }
                        }).setDuration(150L).start();
                    } else {
                        groupCallRenderersContainer = this.parentContainer;
                        if (!groupCallRenderersContainer.inLayout) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda7
                                @Override // java.lang.Runnable
                                public final void run() {
                                    GroupCallMiniTextureView.this.lambda$updateAttachState$2(this);
                                }
                            });
                        } else {
                            groupCallRenderersContainer.removeView(this);
                        }
                        this.parentContainer.detach(this);
                        release();
                    }
                    if (!this.participant.participant.self) {
                        if (VoIPService.getSharedInstance() != null) {
                            VoIPService.getSharedInstance().setLocalSink(null, this.participant.presentation);
                        }
                    } else if (VoIPService.getSharedInstance() != null) {
                        VoIPService sharedInstance = VoIPService.getSharedInstance();
                        ChatObject.VideoParticipant videoParticipant5 = this.participant;
                        sharedInstance.removeRemoteSink(videoParticipant5.participant, videoParticipant5.presentation);
                    }
                    invalidate();
                    valueAnimator2 = this.noVideoStubAnimator;
                    if (valueAnimator2 != null) {
                        valueAnimator2.removeAllListeners();
                        this.noVideoStubAnimator.cancel();
                    }
                }
            }
            z8 = true;
            if (!z8) {
            }
            this.attached = false;
            saveThumb();
            if (this.textureView.currentAnimation != null) {
            }
            groupCallRenderersContainer = this.parentContainer;
            if (!groupCallRenderersContainer.inLayout) {
            }
            this.parentContainer.detach(this);
            release();
            if (!this.participant.participant.self) {
            }
            invalidate();
            valueAnimator2 = this.noVideoStubAnimator;
            if (valueAnimator2 != null) {
            }
        } else if (!z7) {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            GroupCallGridCell groupCallGridCell3 = this.primaryView;
            if (groupCallGridCell3 != null || this.secondaryView != null || this.tabletGridView != null || this.showingInFullscreen) {
                if (groupCallGridCell3 != null) {
                    this.participant = groupCallGridCell3.getParticipant();
                } else {
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = this.secondaryView;
                    if (groupCallUserCell != null) {
                        this.participant = groupCallUserCell.getVideoParticipant();
                    } else {
                        GroupCallGridCell groupCallGridCell4 = this.tabletGridView;
                        if (groupCallGridCell4 != null) {
                            this.participant = groupCallGridCell4.getParticipant();
                        }
                    }
                }
                ChatObject.VideoParticipant videoParticipant6 = this.participant;
                TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = videoParticipant6.participant;
                if (tLRPC$TL_groupCallParticipant.self) {
                    if (VoIPService.getSharedInstance() != null) {
                    }
                    boolean z9 = false;
                } else {
                    ChatObject.Call call2 = this.call;
                    if (!call2.canStreamVideo) {
                    }
                }
                if (!this.showingInFullscreen) {
                    VoIPService sharedInstance2 = VoIPService.getSharedInstance();
                    ChatObject.VideoParticipant videoParticipant7 = this.participant;
                    if (!sharedInstance2.isFullscreen(videoParticipant7.participant, videoParticipant7.presentation)) {
                        VoIPService sharedInstance3 = VoIPService.getSharedInstance();
                        ChatObject.VideoParticipant videoParticipant8 = this.participant;
                        if (!sharedInstance3.isFullscreen(videoParticipant8.participant, videoParticipant8.presentation)) {
                        }
                    }
                }
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    for (int i2 = 0; i2 < this.attachedRenderers.size(); i2++) {
                        if (this.attachedRenderers.get(i2).participant.equals(this.participant)) {
                            throw new RuntimeException("try add two same renderers");
                        }
                    }
                }
                this.attached = true;
                if (this.activity.statusIconPool.size() > 0) {
                    ArrayList<GroupCallStatusIcon> arrayList = this.activity.statusIconPool;
                    this.statusIcon = arrayList.remove(arrayList.size() - 1);
                } else {
                    this.statusIcon = new GroupCallStatusIcon();
                }
                this.statusIcon.setCallback(this);
                this.statusIcon.setImageView(this.micIconView);
                updateIconColor(false);
                if (getParent() == null) {
                    this.parentContainer.addView(this, LayoutHelper.createFrame(46, 46, 51));
                    this.parentContainer.attach(this);
                }
                this.checkScale = true;
                this.animateEnter = false;
                animate().setListener(null).cancel();
                if (this.textureView.currentAnimation == null && this.secondaryView != null && this.primaryView == null && !hasImage()) {
                    setScaleX(0.5f);
                    setScaleY(0.5f);
                    setAlpha(0.0f);
                    this.animateEnter = true;
                    invalidate();
                    animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.5
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                            groupCallMiniTextureView.animateEnter = false;
                            groupCallMiniTextureView.invalidate();
                        }
                    }).setDuration(100L).start();
                    invalidate();
                } else {
                    setScaleY(1.0f);
                    setScaleX(1.0f);
                    setAlpha(1.0f);
                }
                loadThumb();
                this.screencastIcon.setVisibility((!this.participant.presentation || this.call.call.rtmp_stream) ? 8 : 0);
                z2 = false;
                z3 = true;
                if (this.participant != this.call.videoNotAvailableParticipant) {
                    if (this.nameView.getVisibility() != 4) {
                        this.nameView.setVisibility(4);
                        this.micIconView.setVisibility(4);
                    }
                } else if (this.nameView.getVisibility() != 0) {
                    this.nameView.setVisibility(0);
                    this.micIconView.setVisibility(0);
                }
                if (this.attached) {
                    boolean z10 = GroupCallActivity.isTabletMode && (!this.parentContainer.inFullscreenMode || (this.secondaryView == null && this.primaryView == null));
                    int i3 = -1;
                    if (!this.showingInFullscreen) {
                        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = this.secondaryView;
                        if (groupCallUserCell2 == null || this.primaryView != null || this.parentContainer.inFullscreenMode) {
                            if (!this.showingAsScrimView) {
                                if (groupCallUserCell2 != null && this.primaryView == null) {
                                    dp = AndroidUtilities.dp(80.0f);
                                } else {
                                    GroupCallGridCell groupCallGridCell5 = this.tabletGridView;
                                    if (groupCallGridCell5 == null || !z10) {
                                        GroupCallGridCell groupCallGridCell6 = this.primaryView;
                                        if ((groupCallGridCell6 == null || groupCallUserCell2 != null) && this.isFullscreenMode) {
                                            if (groupCallGridCell6 != null) {
                                                dp = AndroidUtilities.dp(80.0f);
                                            }
                                        } else if (groupCallGridCell6 != null) {
                                            f = groupCallGridCell6.spanCount;
                                            dp = -1;
                                            i = 0;
                                            z4 = true;
                                            marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                                            if (dp != 0 && (marginLayoutParams.height != dp || z3 || this.useSpanSize != z4 || ((z4 && this.spanCount != f) || this.gridItemsCount != i))) {
                                                marginLayoutParams.height = dp;
                                                if (!z4) {
                                                    i3 = dp;
                                                }
                                                marginLayoutParams.width = i3;
                                                this.useSpanSize = z4;
                                                this.spanCount = f;
                                                this.checkScale = true;
                                                if (!z2) {
                                                    this.textureView.animateToLayout();
                                                    this.updateNextLayoutAnimated = true;
                                                } else {
                                                    this.textureView.requestLayout();
                                                }
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda4
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        GroupCallMiniTextureView.this.requestLayout();
                                                    }
                                                });
                                                this.parentContainer.requestLayout();
                                                invalidate();
                                            }
                                            videoParticipant = this.participant;
                                            if (!videoParticipant.participant.self && !videoParticipant.presentation && VoIPService.getSharedInstance() != null) {
                                                this.textureView.renderer.setMirror(VoIPService.getSharedInstance().isFrontFaceCamera());
                                                this.textureView.renderer.setRotateTextureWithScreen(true);
                                                this.textureView.renderer.setUseCameraRotation(true);
                                            } else {
                                                this.textureView.renderer.setMirror(false);
                                                this.textureView.renderer.setRotateTextureWithScreen(true);
                                                this.textureView.renderer.setUseCameraRotation(false);
                                            }
                                            this.textureView.updateRotation();
                                            if (this.participant.participant.self) {
                                                this.textureView.renderer.setMaxTextureSize(720);
                                            } else {
                                                this.textureView.renderer.setMaxTextureSize(0);
                                            }
                                            videoParticipant2 = this.participant;
                                            if (ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
                                                ChatObject.Call call3 = this.call;
                                                if (call3.canStreamVideo || this.participant == call3.videoNotAvailableParticipant) {
                                                    z5 = true;
                                                    boolean z11 = !z2 && this.secondaryView != null && !this.showingInFullscreen && !z5;
                                                    if (z5 != this.hasVideo && !z11) {
                                                        this.hasVideo = z5;
                                                        valueAnimator = this.noVideoStubAnimator;
                                                        if (valueAnimator != null) {
                                                            valueAnimator.removeAllListeners();
                                                            this.noVideoStubAnimator.cancel();
                                                        }
                                                        if (!z2) {
                                                            if (!this.hasVideo && this.noVideoStubLayout.getVisibility() != 0) {
                                                                this.noVideoStubLayout.setVisibility(0);
                                                                this.noVideoStubLayout.setAlpha(0.0f);
                                                            }
                                                            float[] fArr = new float[2];
                                                            fArr[0] = this.progressToNoVideoStub;
                                                            fArr[1] = this.hasVideo ? 0.0f : 1.0f;
                                                            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                                                            this.noVideoStubAnimator = ofFloat;
                                                            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda1
                                                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                                                public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                                                                    GroupCallMiniTextureView.this.lambda$updateAttachState$3(valueAnimator3);
                                                                }
                                                            });
                                                            this.noVideoStubAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.6
                                                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                                                public void onAnimationEnd(Animator animator) {
                                                                    GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                                                                    groupCallMiniTextureView.progressToNoVideoStub = groupCallMiniTextureView.hasVideo ? 0.0f : 1.0f;
                                                                    groupCallMiniTextureView.noVideoStubLayout.setAlpha(GroupCallMiniTextureView.this.progressToNoVideoStub);
                                                                    GroupCallMiniTextureView.this.noVideoStubLayout.setVisibility(GroupCallMiniTextureView.this.hasVideo ? 8 : 0);
                                                                    GroupCallMiniTextureView.this.textureView.invalidate();
                                                                }
                                                            });
                                                            this.noVideoStubAnimator.start();
                                                        } else {
                                                            boolean z12 = this.hasVideo;
                                                            this.progressToNoVideoStub = z12 ? 0.0f : 1.0f;
                                                            this.noVideoStubLayout.setVisibility(z12 ? 8 : 0);
                                                            this.noVideoStubLayout.setAlpha(this.progressToNoVideoStub);
                                                            this.textureView.invalidate();
                                                        }
                                                        if (this.hasVideo) {
                                                            this.noVideoStubLayout.updateMuteButtonState(false);
                                                        }
                                                    }
                                                    if (this.participant.participant.self && VoIPService.getSharedInstance() != null) {
                                                        VoIPService.getSharedInstance().setLocalSink(this.textureView.renderer, this.participant.presentation);
                                                    }
                                                    this.statusIcon.setParticipant(this.participant.participant, z2);
                                                    if (this.noVideoStubLayout.getVisibility() == 0) {
                                                        this.noVideoStubLayout.updateMuteButtonState(true);
                                                    }
                                                    videoParticipant3 = this.participant;
                                                    if (videoParticipant3.presentation ? !((tLRPC$TL_groupCallParticipantVideo = videoParticipant3.participant.video) == null || !tLRPC$TL_groupCallParticipantVideo.paused) : !((tLRPC$TL_groupCallParticipantVideo2 = videoParticipant3.participant.presentation) == null || !tLRPC$TL_groupCallParticipantVideo2.paused)) {
                                                        z6 = true;
                                                    }
                                                    if (this.videoIsPaused != z6) {
                                                        this.videoIsPaused = z6;
                                                        ViewPropertyAnimator animate = this.textureView.renderer.animate();
                                                        if (this.videoIsPaused) {
                                                            f2 = 0.0f;
                                                        }
                                                        animate.alpha(f2).setDuration(250L).start();
                                                        this.textureView.invalidate();
                                                    }
                                                    if (!GroupCallActivity.paused || !this.hasVideo) {
                                                        if (this.participant.participant.self) {
                                                            if (VoIPService.getSharedInstance() != null) {
                                                                VoIPService.getSharedInstance().setLocalSink(null, this.participant.presentation);
                                                            }
                                                        } else if (VoIPService.getSharedInstance() != null) {
                                                            VoIPService sharedInstance4 = VoIPService.getSharedInstance();
                                                            ChatObject.VideoParticipant videoParticipant9 = this.participant;
                                                            sharedInstance4.removeRemoteSink(videoParticipant9.participant, videoParticipant9.presentation);
                                                            VoIPService sharedInstance5 = VoIPService.getSharedInstance();
                                                            ChatObject.VideoParticipant videoParticipant10 = this.participant;
                                                            sharedInstance5.removeRemoteSink(videoParticipant10.participant, videoParticipant10.presentation);
                                                        }
                                                        if (GroupCallActivity.paused && this.textureView.renderer.isFirstFrameRendered()) {
                                                            saveThumb();
                                                            this.textureView.renderer.clearFirstFrame();
                                                            this.textureView.renderer.setAlpha(0.0f);
                                                            this.textureView.blurRenderer.setAlpha(0.0f);
                                                        }
                                                    } else {
                                                        if (!this.textureView.renderer.isFirstFrameRendered()) {
                                                            loadThumb();
                                                        }
                                                        if (this.participant.participant.self) {
                                                            if (VoIPService.getSharedInstance() != null) {
                                                                VoIPService.getSharedInstance().setLocalSink(this.textureView.renderer, this.participant.presentation);
                                                            }
                                                        } else if (VoIPService.getSharedInstance() != null) {
                                                            VoIPService sharedInstance6 = VoIPService.getSharedInstance();
                                                            ChatObject.VideoParticipant videoParticipant11 = this.participant;
                                                            sharedInstance6.addRemoteSink(videoParticipant11.participant, videoParticipant11.presentation, this.textureView.renderer, null);
                                                            VoIPService sharedInstance7 = VoIPService.getSharedInstance();
                                                            ChatObject.VideoParticipant videoParticipant12 = this.participant;
                                                            sharedInstance7.addRemoteSink(videoParticipant12.participant, videoParticipant12.presentation, this.textureView.renderer, null);
                                                            ChatObject.Call call4 = this.call;
                                                            if (call4 != null && call4.call.rtmp_stream && !this.textureView.renderer.isFirstFrameRendered() && !this.postedNoRtmpStreamCallback) {
                                                                AndroidUtilities.runOnUIThread(this.noRtmpStreamCallback, 15000L);
                                                                this.postedNoRtmpStreamCallback = true;
                                                            }
                                                        }
                                                    }
                                                    updateIconColor(true);
                                                }
                                            }
                                            this.noVideoStubLayout.avatarImageReceiver.setCurrentAccount(this.currentAccount);
                                            peerId = MessageObject.getPeerId(this.participant.participant.peer);
                                            if (DialogObject.isUserDialog(peerId)) {
                                                TLRPC$User user = AccountInstance.getInstance(this.currentAccount).getMessagesController().getUser(Long.valueOf(peerId));
                                                this.noVideoStubLayout.avatarDrawable.setInfo(user);
                                                forChat = ImageLocation.getForUser(user, 0);
                                                forChat2 = ImageLocation.getForUser(user, 1);
                                                tLRPC$User = user;
                                            } else {
                                                TLRPC$Chat chat = AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().getChat(Long.valueOf(-peerId));
                                                this.noVideoStubLayout.avatarDrawable.setInfo(chat);
                                                forChat = ImageLocation.getForChat(chat, 0);
                                                forChat2 = ImageLocation.getForChat(chat, 1);
                                                tLRPC$User = chat;
                                            }
                                            ImageLocation imageLocation = forChat;
                                            TLRPC$Chat tLRPC$Chat = tLRPC$User;
                                            this.noVideoStubLayout.avatarImageReceiver.setImage(imageLocation, null, (forChat2 != null || (imageFromMemory = ImageLoader.getInstance().getImageFromMemory(forChat2.location, null, "50_50")) == null) ? this.noVideoStubLayout.avatarDrawable : imageFromMemory, null, tLRPC$Chat, 0);
                                            this.noVideoStubLayout.backgroundImageReceiver.setImage(imageLocation, "50_50_b", new ColorDrawable(Theme.getColor("voipgroup_listViewBackground")), null, tLRPC$Chat, 0);
                                            z5 = false;
                                            if (!z2) {
                                            }
                                            if (z5 != this.hasVideo) {
                                                this.hasVideo = z5;
                                                valueAnimator = this.noVideoStubAnimator;
                                                if (valueAnimator != null) {
                                                }
                                                if (!z2) {
                                                }
                                                if (this.hasVideo) {
                                                }
                                            }
                                            if (this.participant.participant.self) {
                                                VoIPService.getSharedInstance().setLocalSink(this.textureView.renderer, this.participant.presentation);
                                            }
                                            this.statusIcon.setParticipant(this.participant.participant, z2);
                                            if (this.noVideoStubLayout.getVisibility() == 0) {
                                            }
                                            videoParticipant3 = this.participant;
                                            if (videoParticipant3.presentation) {
                                                if (this.videoIsPaused != z6) {
                                                }
                                                if (!GroupCallActivity.paused) {
                                                }
                                                if (this.participant.participant.self) {
                                                }
                                                if (GroupCallActivity.paused) {
                                                    saveThumb();
                                                    this.textureView.renderer.clearFirstFrame();
                                                    this.textureView.renderer.setAlpha(0.0f);
                                                    this.textureView.blurRenderer.setAlpha(0.0f);
                                                }
                                                updateIconColor(true);
                                            } else {
                                                if (this.videoIsPaused != z6) {
                                                }
                                                if (!GroupCallActivity.paused) {
                                                }
                                                if (this.participant.participant.self) {
                                                }
                                                if (GroupCallActivity.paused) {
                                                }
                                                updateIconColor(true);
                                            }
                                        } else {
                                            dp = AndroidUtilities.dp(46.0f);
                                        }
                                    } else if (groupCallGridCell5 != null) {
                                        i = groupCallGridCell5.gridAdapter.getItemCount();
                                        z4 = true;
                                        f = groupCallGridCell5.spanCount;
                                        dp = -1;
                                        marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                                        if (dp != 0) {
                                            marginLayoutParams.height = dp;
                                            if (!z4) {
                                            }
                                            marginLayoutParams.width = i3;
                                            this.useSpanSize = z4;
                                            this.spanCount = f;
                                            this.checkScale = true;
                                            if (!z2) {
                                            }
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda4
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    GroupCallMiniTextureView.this.requestLayout();
                                                }
                                            });
                                            this.parentContainer.requestLayout();
                                            invalidate();
                                        }
                                        videoParticipant = this.participant;
                                        if (!videoParticipant.participant.self) {
                                        }
                                        this.textureView.renderer.setMirror(false);
                                        this.textureView.renderer.setRotateTextureWithScreen(true);
                                        this.textureView.renderer.setUseCameraRotation(false);
                                        this.textureView.updateRotation();
                                        if (this.participant.participant.self) {
                                        }
                                        videoParticipant2 = this.participant;
                                        if (ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
                                        }
                                        this.noVideoStubLayout.avatarImageReceiver.setCurrentAccount(this.currentAccount);
                                        peerId = MessageObject.getPeerId(this.participant.participant.peer);
                                        if (DialogObject.isUserDialog(peerId)) {
                                        }
                                        ImageLocation imageLocation2 = forChat;
                                        TLRPC$Chat tLRPC$Chat2 = tLRPC$User;
                                        this.noVideoStubLayout.avatarImageReceiver.setImage(imageLocation2, null, (forChat2 != null || (imageFromMemory = ImageLoader.getInstance().getImageFromMemory(forChat2.location, null, "50_50")) == null) ? this.noVideoStubLayout.avatarDrawable : imageFromMemory, null, tLRPC$Chat2, 0);
                                        this.noVideoStubLayout.backgroundImageReceiver.setImage(imageLocation2, "50_50_b", new ColorDrawable(Theme.getColor("voipgroup_listViewBackground")), null, tLRPC$Chat2, 0);
                                        z5 = false;
                                        if (!z2) {
                                        }
                                        if (z5 != this.hasVideo) {
                                        }
                                        if (this.participant.participant.self) {
                                        }
                                        this.statusIcon.setParticipant(this.participant.participant, z2);
                                        if (this.noVideoStubLayout.getVisibility() == 0) {
                                        }
                                        videoParticipant3 = this.participant;
                                        if (videoParticipant3.presentation) {
                                        }
                                    } else {
                                        dp = AndroidUtilities.dp(46.0f);
                                    }
                                }
                                f = 1.0f;
                                i = 0;
                                z4 = false;
                                marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                                if (dp != 0) {
                                }
                                videoParticipant = this.participant;
                                if (!videoParticipant.participant.self) {
                                }
                                this.textureView.renderer.setMirror(false);
                                this.textureView.renderer.setRotateTextureWithScreen(true);
                                this.textureView.renderer.setUseCameraRotation(false);
                                this.textureView.updateRotation();
                                if (this.participant.participant.self) {
                                }
                                videoParticipant2 = this.participant;
                                if (ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
                                }
                                this.noVideoStubLayout.avatarImageReceiver.setCurrentAccount(this.currentAccount);
                                peerId = MessageObject.getPeerId(this.participant.participant.peer);
                                if (DialogObject.isUserDialog(peerId)) {
                                }
                                ImageLocation imageLocation22 = forChat;
                                TLRPC$Chat tLRPC$Chat22 = tLRPC$User;
                                this.noVideoStubLayout.avatarImageReceiver.setImage(imageLocation22, null, (forChat2 != null || (imageFromMemory = ImageLoader.getInstance().getImageFromMemory(forChat2.location, null, "50_50")) == null) ? this.noVideoStubLayout.avatarDrawable : imageFromMemory, null, tLRPC$Chat22, 0);
                                this.noVideoStubLayout.backgroundImageReceiver.setImage(imageLocation22, "50_50_b", new ColorDrawable(Theme.getColor("voipgroup_listViewBackground")), null, tLRPC$Chat22, 0);
                                z5 = false;
                                if (!z2) {
                                }
                                if (z5 != this.hasVideo) {
                                }
                                if (this.participant.participant.self) {
                                }
                                this.statusIcon.setParticipant(this.participant.participant, z2);
                                if (this.noVideoStubLayout.getVisibility() == 0) {
                                }
                                videoParticipant3 = this.participant;
                                if (videoParticipant3.presentation) {
                                }
                            }
                        }
                        dp = 0;
                        f = 1.0f;
                        i = 0;
                        z4 = false;
                        marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                        if (dp != 0) {
                        }
                        videoParticipant = this.participant;
                        if (!videoParticipant.participant.self) {
                        }
                        this.textureView.renderer.setMirror(false);
                        this.textureView.renderer.setRotateTextureWithScreen(true);
                        this.textureView.renderer.setUseCameraRotation(false);
                        this.textureView.updateRotation();
                        if (this.participant.participant.self) {
                        }
                        videoParticipant2 = this.participant;
                        if (ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
                        }
                        this.noVideoStubLayout.avatarImageReceiver.setCurrentAccount(this.currentAccount);
                        peerId = MessageObject.getPeerId(this.participant.participant.peer);
                        if (DialogObject.isUserDialog(peerId)) {
                        }
                        ImageLocation imageLocation222 = forChat;
                        TLRPC$Chat tLRPC$Chat222 = tLRPC$User;
                        this.noVideoStubLayout.avatarImageReceiver.setImage(imageLocation222, null, (forChat2 != null || (imageFromMemory = ImageLoader.getInstance().getImageFromMemory(forChat2.location, null, "50_50")) == null) ? this.noVideoStubLayout.avatarDrawable : imageFromMemory, null, tLRPC$Chat222, 0);
                        this.noVideoStubLayout.backgroundImageReceiver.setImage(imageLocation222, "50_50_b", new ColorDrawable(Theme.getColor("voipgroup_listViewBackground")), null, tLRPC$Chat222, 0);
                        z5 = false;
                        if (!z2) {
                        }
                        if (z5 != this.hasVideo) {
                        }
                        if (this.participant.participant.self) {
                        }
                        this.statusIcon.setParticipant(this.participant.participant, z2);
                        if (this.noVideoStubLayout.getVisibility() == 0) {
                        }
                        videoParticipant3 = this.participant;
                        if (videoParticipant3.presentation) {
                        }
                    }
                    dp = -1;
                    f = 1.0f;
                    i = 0;
                    z4 = false;
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                    if (dp != 0) {
                    }
                    videoParticipant = this.participant;
                    if (!videoParticipant.participant.self) {
                    }
                    this.textureView.renderer.setMirror(false);
                    this.textureView.renderer.setRotateTextureWithScreen(true);
                    this.textureView.renderer.setUseCameraRotation(false);
                    this.textureView.updateRotation();
                    if (this.participant.participant.self) {
                    }
                    videoParticipant2 = this.participant;
                    if (ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
                    }
                    this.noVideoStubLayout.avatarImageReceiver.setCurrentAccount(this.currentAccount);
                    peerId = MessageObject.getPeerId(this.participant.participant.peer);
                    if (DialogObject.isUserDialog(peerId)) {
                    }
                    ImageLocation imageLocation2222 = forChat;
                    TLRPC$Chat tLRPC$Chat2222 = tLRPC$User;
                    this.noVideoStubLayout.avatarImageReceiver.setImage(imageLocation2222, null, (forChat2 != null || (imageFromMemory = ImageLoader.getInstance().getImageFromMemory(forChat2.location, null, "50_50")) == null) ? this.noVideoStubLayout.avatarDrawable : imageFromMemory, null, tLRPC$Chat2222, 0);
                    this.noVideoStubLayout.backgroundImageReceiver.setImage(imageLocation2222, "50_50_b", new ColorDrawable(Theme.getColor("voipgroup_listViewBackground")), null, tLRPC$Chat2222, 0);
                    z5 = false;
                    if (!z2) {
                    }
                    if (z5 != this.hasVideo) {
                    }
                    if (this.participant.participant.self) {
                    }
                    this.statusIcon.setParticipant(this.participant.participant, z2);
                    if (this.noVideoStubLayout.getVisibility() == 0) {
                    }
                    videoParticipant3 = this.participant;
                    if (videoParticipant3.presentation) {
                    }
                }
                updateInfo();
            }
        }
        z2 = z;
        z3 = false;
        if (this.participant != this.call.videoNotAvailableParticipant) {
        }
        if (this.attached) {
        }
        updateInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAttachState$2(View view) {
        this.parentContainer.removeView(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAttachState$3(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.progressToNoVideoStub = floatValue;
        this.noVideoStubLayout.setAlpha(floatValue);
        this.textureView.invalidate();
    }

    private void loadThumb() {
        if (this.thumb != null) {
            return;
        }
        HashMap<String, Bitmap> hashMap = this.call.thumbs;
        ChatObject.VideoParticipant videoParticipant = this.participant;
        boolean z = videoParticipant.presentation;
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = videoParticipant.participant;
        Bitmap bitmap = hashMap.get(z ? tLRPC$TL_groupCallParticipant.presentationEndpoint : tLRPC$TL_groupCallParticipant.videoEndpoint);
        this.thumb = bitmap;
        this.textureView.setThumb(bitmap);
        if (this.thumb != null) {
            return;
        }
        long peerId = MessageObject.getPeerId(this.participant.participant.peer);
        ChatObject.VideoParticipant videoParticipant2 = this.participant;
        if (videoParticipant2.participant.self && videoParticipant2.presentation) {
            this.imageReceiver.setImageBitmap(new MotionBackgroundDrawable(-14602694, -13935795, -14395293, -14203560, true));
        } else if (peerId > 0) {
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId));
            ImageLocation forUser = ImageLocation.getForUser(user, 1);
            int colorForId = user != null ? AvatarDrawable.getColorForId(user.id) : ColorUtils.blendARGB(-16777216, -1, 0.2f);
            this.imageReceiver.setImage(forUser, "50_50_b", new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{ColorUtils.blendARGB(colorForId, -16777216, 0.2f), ColorUtils.blendARGB(colorForId, -16777216, 0.4f)}), null, user, 0);
        } else {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
            ImageLocation forChat = ImageLocation.getForChat(chat, 1);
            int colorForId2 = chat != null ? AvatarDrawable.getColorForId(chat.id) : ColorUtils.blendARGB(-16777216, -1, 0.2f);
            this.imageReceiver.setImage(forChat, "50_50_b", new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{ColorUtils.blendARGB(colorForId2, -16777216, 0.2f), ColorUtils.blendARGB(colorForId2, -16777216, 0.4f)}), null, chat, 0);
        }
    }

    public void updateInfo() {
        if (!this.attached) {
            return;
        }
        String str = null;
        long peerId = MessageObject.getPeerId(this.participant.participant.peer);
        if (DialogObject.isUserDialog(peerId)) {
            str = UserObject.getUserName(AccountInstance.getInstance(this.currentAccount).getMessagesController().getUser(Long.valueOf(peerId)));
        } else {
            TLRPC$Chat chat = AccountInstance.getInstance(this.currentAccount).getMessagesController().getChat(Long.valueOf(-peerId));
            if (chat != null) {
                str = chat.title;
            }
        }
        this.nameView.setText(str);
    }

    public boolean hasImage() {
        return this.textureView.stubVisibleProgress == 1.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c4, code lost:
        if (r0 != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c6, code lost:
        r9 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ce, code lost:
        setTranslationX(((r11.getX() + r9.getX()) - getLeft()) - r12.getLeft());
        setTranslationY((((r11.getY() + org.telegram.messenger.AndroidUtilities.dp(2.0f)) + r9.getY()) - getTop()) - r12.getTop());
        r8.textureView.setRoundCorners(org.telegram.messenger.AndroidUtilities.dp(8.0f));
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0110, code lost:
        if (r8.attached == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0114, code lost:
        if (r8.animateEnter != false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0118, code lost:
        if (org.telegram.ui.GroupCallActivity.isTabletMode != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011a, code lost:
        r8.drawFirst = true;
        setAlpha((1.0f - r1) * r11.getAlpha());
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x012a, code lost:
        if (r8.primaryView == null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x012e, code lost:
        if (r8.tabletGridView != null) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0130, code lost:
        setAlpha(r1 * r11.getAlpha());
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00cb, code lost:
        if (r2 != null) goto L48;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updatePosition(ViewGroup viewGroup, ViewGroup viewGroup2, RecyclerListView recyclerListView, GroupCallRenderersContainer groupCallRenderersContainer) {
        if (this.showingAsScrimView || this.animateToScrimView || this.forceDetached) {
            return;
        }
        boolean z = false;
        this.drawFirst = false;
        float f = groupCallRenderersContainer.progressToFullscreenMode;
        if (this.animateToFullscreen || this.showingInFullscreen) {
            GroupCallGridCell groupCallGridCell = this.primaryView;
            if (groupCallGridCell != null || this.tabletGridView != null) {
                GroupCallGridCell groupCallGridCell2 = this.tabletGridView;
                if (groupCallGridCell2 != null) {
                    groupCallGridCell = groupCallGridCell2;
                }
                if (groupCallGridCell2 != null) {
                    viewGroup = viewGroup2;
                }
                float x = ((groupCallGridCell.getX() + viewGroup.getX()) - getLeft()) - groupCallRenderersContainer.getLeft();
                float y = (((groupCallGridCell.getY() + AndroidUtilities.dp(2.0f)) + viewGroup.getY()) - getTop()) - groupCallRenderersContainer.getTop();
                float f2 = 1.0f - f;
                float f3 = 0.0f * f;
                setTranslationX((x * f2) + f3);
                setTranslationY((y * f2) + f3);
            } else {
                setTranslationX(0.0f);
                setTranslationY(0.0f);
            }
            this.textureView.setRoundCorners(AndroidUtilities.dp(8.0f));
            GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = this.secondaryView;
            if (groupCallUserCell != null) {
                groupCallUserCell.setAlpha(f);
            }
            if (!this.showingInFullscreen && this.primaryView == null && this.tabletGridView == null) {
                setAlpha(f);
                return;
            } else if (this.animateEnter) {
                return;
            } else {
                setAlpha(1.0f);
                return;
            }
        }
        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = this.secondaryView;
        if (groupCallUserCell2 != null) {
            if (groupCallUserCell2.isRemoving(recyclerListView)) {
                setAlpha(this.secondaryView.getAlpha());
            } else if (this.primaryView == null) {
                if (this.attached && !this.animateEnter) {
                    setAlpha(f);
                }
                this.secondaryView.setAlpha(f);
                f = 1.0f;
            } else {
                this.secondaryView.setAlpha(1.0f);
                if (this.attached && !this.animateEnter) {
                    setAlpha(1.0f);
                }
            }
            setTranslationX((this.secondaryView.getX() + recyclerListView.getX()) - getLeft());
            float f4 = 1.0f - f;
            setTranslationY((((AndroidUtilities.dp(2.0f) * f4) + this.secondaryView.getY()) + recyclerListView.getY()) - getTop());
            this.textureView.setRoundCorners((AndroidUtilities.dp(13.0f) * f) + (AndroidUtilities.dp(8.0f) * f4));
            return;
        }
        GroupCallGridCell groupCallGridCell3 = this.primaryView;
        if (groupCallGridCell3 == null && this.tabletGridView == null) {
            return;
        }
        GroupCallGridCell groupCallGridCell4 = this.tabletGridView;
        if (groupCallGridCell4 != null && groupCallGridCell3 != null) {
            if (GroupCallActivity.isTabletMode && !this.parentContainer.inFullscreenMode) {
                z = true;
            }
            if (z) {
                groupCallGridCell3 = groupCallGridCell4;
            }
        } else if (groupCallGridCell4 != null) {
            groupCallGridCell3 = groupCallGridCell4;
        }
    }

    public boolean isAttached() {
        return this.attached;
    }

    public void release() {
        this.textureView.renderer.release();
        GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
        if (groupCallStatusIcon != null) {
            this.activity.statusIconPool.add(groupCallStatusIcon);
            this.statusIcon.setCallback(null);
            this.statusIcon.setImageView(null);
        }
        this.statusIcon = null;
    }

    public boolean isFullyVisible() {
        return !this.showingInFullscreen && !this.animateToFullscreen && this.attached && this.textureView.renderer.isFirstFrameRendered() && getAlpha() == 1.0f;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        if (!this.invalidateFromChild) {
            this.textureView.invalidate();
        }
        GroupCallGridCell groupCallGridCell = this.primaryView;
        if (groupCallGridCell != null) {
            groupCallGridCell.invalidate();
            if (this.activity.getScrimView() == this.primaryView) {
                this.activity.getContainerView().invalidate();
            }
        }
        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = this.secondaryView;
        if (groupCallUserCell != null) {
            groupCallUserCell.invalidate();
            if (this.secondaryView.getParent() != null) {
                ((View) this.secondaryView.getParent()).invalidate();
            }
        }
        if (getParent() != null) {
            ((View) getParent()).invalidate();
        }
    }

    public void forceDetach(boolean z) {
        this.forceDetached = true;
        this.attached = false;
        this.parentContainer.detach(this);
        if (z) {
            if (this.participant.participant.self) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().setLocalSink(null, this.participant.presentation);
                }
            } else if (VoIPService.getSharedInstance() != null && !RTMPStreamPipOverlay.isVisible()) {
                VoIPService sharedInstance = VoIPService.getSharedInstance();
                ChatObject.VideoParticipant videoParticipant = this.participant;
                sharedInstance.removeRemoteSink(videoParticipant.participant, videoParticipant.presentation);
            }
        }
        saveThumb();
        ValueAnimator valueAnimator = this.noVideoStubAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.noVideoStubAnimator.cancel();
        }
        this.textureView.renderer.release();
    }

    public void saveThumb() {
        if (this.participant == null || this.textureView.renderer.getMeasuredHeight() == 0 || this.textureView.renderer.getMeasuredWidth() == 0) {
            return;
        }
        getRenderBufferBitmap(new GlGenericDrawer.TextureCallback() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda8
            @Override // org.webrtc.GlGenericDrawer.TextureCallback
            public final void run(Bitmap bitmap, int i) {
                GroupCallMiniTextureView.this.lambda$saveThumb$5(bitmap, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveThumb$5(final Bitmap bitmap, int i) {
        if (bitmap == null || bitmap.getPixel(0, 0) == 0) {
            return;
        }
        Utilities.stackBlurBitmap(bitmap, Math.max(7, Math.max(bitmap.getWidth(), bitmap.getHeight()) / 180));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallMiniTextureView.this.lambda$saveThumb$4(bitmap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveThumb$4(Bitmap bitmap) {
        HashMap<String, Bitmap> hashMap = this.call.thumbs;
        ChatObject.VideoParticipant videoParticipant = this.participant;
        boolean z = videoParticipant.presentation;
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = videoParticipant.participant;
        hashMap.put(z ? tLRPC$TL_groupCallParticipant.presentationEndpoint : tLRPC$TL_groupCallParticipant.videoEndpoint, bitmap);
    }

    public void setViews(GroupCallGridCell groupCallGridCell, GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell, GroupCallGridCell groupCallGridCell2) {
        this.primaryView = groupCallGridCell;
        this.secondaryView = groupCallUserCell;
        this.tabletGridView = groupCallGridCell2;
    }

    public void setAmplitude(double d) {
        this.statusIcon.setAmplitude(d);
        this.noVideoStubLayout.setAmplitude(d);
    }

    public void setZoom(boolean z, float f, float f2, float f3, float f4, float f5) {
        if (this.pinchScale == f && this.pinchCenterX == f2 && this.pinchCenterY == f3 && this.pinchTranslationX == f4 && this.pinchTranslationY == f5) {
            return;
        }
        this.inPinchToZoom = z;
        this.pinchScale = f;
        this.pinchCenterX = f2;
        this.pinchCenterY = f3;
        this.pinchTranslationX = f4;
        this.pinchTranslationY = f5;
        this.textureView.invalidate();
    }

    public void setSwipeToBack(boolean z, float f) {
        if (this.swipeToBack == z && this.swipeToBackDy == f) {
            return;
        }
        this.swipeToBack = z;
        this.swipeToBackDy = f;
        this.textureView.invalidate();
        invalidate();
    }

    public void runOnFrameRendered(Runnable runnable) {
        if (this.textureView.renderer.isFirstFrameRendered()) {
            runnable.run();
            return;
        }
        AndroidUtilities.runOnUIThread(runnable, 250L);
        this.onFirstFrameRunnables.add(runnable);
    }

    @Override // org.telegram.ui.Components.voip.GroupCallStatusIcon.Callback
    public void onStatusChanged() {
        invalidate();
        updateIconColor(true);
        if (this.noVideoStubLayout.getVisibility() == 0) {
            this.noVideoStubLayout.updateMuteButtonState(true);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateIconColor(boolean z) {
        final int color;
        final int i;
        GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
        if (groupCallStatusIcon == null) {
            return;
        }
        if (groupCallStatusIcon.isMutedByMe()) {
            i = Theme.getColor("voipgroup_mutedByAdminIcon");
        } else if (this.statusIcon.isSpeaking()) {
            i = Theme.getColor("voipgroup_speakingText");
        } else {
            color = Theme.getColor("voipgroup_speakingText");
            i = -1;
            if (this.animateToColor != i) {
                return;
            }
            ValueAnimator valueAnimator = this.colorAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.colorAnimator.cancel();
            }
            if (!z) {
                Paint paint = this.speakingPaint;
                this.lastSpeakingFrameColor = color;
                paint.setColor(color);
                return;
            }
            final int i2 = this.lastIconColor;
            final int i3 = this.lastSpeakingFrameColor;
            this.animateToColor = i;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.colorAnimator = ofFloat;
            final int i4 = i;
            final int i5 = color;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    GroupCallMiniTextureView.this.lambda$updateIconColor$6(i2, i4, i3, i5, valueAnimator2);
                }
            });
            this.colorAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                    int i6 = i;
                    groupCallMiniTextureView.lastIconColor = i6;
                    groupCallMiniTextureView.animateToColor = i6;
                    int i7 = color;
                    groupCallMiniTextureView.lastSpeakingFrameColor = i7;
                    groupCallMiniTextureView.speakingPaint.setColor(i7);
                    GroupCallMiniTextureView groupCallMiniTextureView2 = GroupCallMiniTextureView.this;
                    if (groupCallMiniTextureView2.progressToSpeaking > 0.0f) {
                        groupCallMiniTextureView2.invalidate();
                    }
                }
            });
            this.colorAnimator.start();
            return;
        }
        color = i;
        if (this.animateToColor != i) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateIconColor$6(int i, int i2, int i3, int i4, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.lastIconColor = ColorUtils.blendARGB(i, i2, floatValue);
        int blendARGB = ColorUtils.blendARGB(i3, i4, floatValue);
        this.lastSpeakingFrameColor = blendARGB;
        this.speakingPaint.setColor(blendARGB);
        if (this.progressToSpeaking > 0.0f) {
            invalidate();
        }
    }

    public void runDelayedAnimations() {
        for (int i = 0; i < this.onFirstFrameRunnables.size(); i++) {
            this.onFirstFrameRunnables.get(i).run();
        }
        this.onFirstFrameRunnables.clear();
    }

    public void updateSize(int i) {
        int measuredWidth = this.parentContainer.getMeasuredWidth() - AndroidUtilities.dp(6.0f);
        if ((this.collapseSize == i || i <= 0) && (this.fullSize == measuredWidth || measuredWidth <= 0)) {
            return;
        }
        if (i != 0) {
            this.collapseSize = i;
        }
        if (measuredWidth != 0) {
            this.fullSize = measuredWidth;
        }
        this.nameView.setFullLayoutAdditionalWidth(measuredWidth - i, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class NoVideoStubLayout extends View {
        float amplitude;
        float animateAmplitudeDiff;
        float animateToAmplitude;
        private GroupCallActivity.WeavingState currentState;
        float cx;
        float cy;
        private GroupCallActivity.WeavingState prevState;
        float speakingProgress;
        public ImageReceiver avatarImageReceiver = new ImageReceiver();
        public ImageReceiver backgroundImageReceiver = new ImageReceiver();
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        Paint paint = new Paint(1);
        Paint backgroundPaint = new Paint(1);
        private GroupCallActivity.WeavingState[] states = new GroupCallActivity.WeavingState[3];
        int muteButtonState = -1;
        float switchProgress = 1.0f;
        BlobDrawable tinyWaveDrawable = new BlobDrawable(9);
        BlobDrawable bigWaveDrawable = new BlobDrawable(12);

        public NoVideoStubLayout(Context context) {
            super(context);
            this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(76.0f);
            this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(92.0f);
            this.tinyWaveDrawable.generateBlob();
            this.bigWaveDrawable.minRadius = AndroidUtilities.dp(80.0f);
            this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(95.0f);
            this.bigWaveDrawable.generateBlob();
            this.paint.setColor(ColorUtils.blendARGB(Theme.getColor("voipgroup_listeningText"), Theme.getColor("voipgroup_speakingText"), this.speakingProgress));
            this.paint.setAlpha(102);
            this.backgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            float dp = AndroidUtilities.dp(157.0f);
            this.cx = getMeasuredWidth() >> 1;
            this.cy = (getMeasuredHeight() >> 1) + (GroupCallActivity.isLandscapeMode ? 0.0f : (-getMeasuredHeight()) * 0.12f);
            float f = dp / 2.0f;
            this.avatarImageReceiver.setRoundRadius((int) f);
            this.avatarImageReceiver.setImageCoords(this.cx - f, this.cy - f, dp, dp);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            GroupCallActivity.WeavingState weavingState;
            float f;
            GroupCallActivity.WeavingState weavingState2;
            super.onDraw(canvas);
            RectF rectF = AndroidUtilities.rectTmp;
            float x = GroupCallMiniTextureView.this.textureView.getX();
            VoIPTextureView voIPTextureView = GroupCallMiniTextureView.this.textureView;
            float f2 = x + voIPTextureView.currentClipHorizontal;
            float y = voIPTextureView.getY();
            VoIPTextureView voIPTextureView2 = GroupCallMiniTextureView.this.textureView;
            float f3 = y + voIPTextureView2.currentClipVertical;
            float x2 = voIPTextureView2.getX() + GroupCallMiniTextureView.this.textureView.getMeasuredWidth();
            VoIPTextureView voIPTextureView3 = GroupCallMiniTextureView.this.textureView;
            rectF.set(f2, f3, x2 - voIPTextureView3.currentClipHorizontal, voIPTextureView3.getY() + GroupCallMiniTextureView.this.textureView.getMeasuredHeight() + GroupCallMiniTextureView.this.textureView.currentClipVertical);
            this.backgroundImageReceiver.setImageCoords(rectF.left, rectF.top, rectF.width(), rectF.height());
            this.backgroundImageReceiver.setRoundRadius((int) GroupCallMiniTextureView.this.textureView.roundRadius);
            this.backgroundImageReceiver.draw(canvas);
            float f4 = GroupCallMiniTextureView.this.textureView.roundRadius;
            canvas.drawRoundRect(rectF, f4, f4, this.backgroundPaint);
            float f5 = this.animateToAmplitude;
            float f6 = this.amplitude;
            if (f5 != f6) {
                float f7 = this.animateAmplitudeDiff;
                float f8 = f6 + (16.0f * f7);
                this.amplitude = f8;
                if (f7 > 0.0f) {
                    if (f8 > f5) {
                        this.amplitude = f5;
                    }
                } else if (f8 < f5) {
                    this.amplitude = f5;
                }
            }
            float f9 = this.switchProgress;
            if (f9 != 1.0f) {
                if (this.prevState != null) {
                    this.switchProgress = f9 + 0.07272727f;
                }
                if (this.switchProgress >= 1.0f) {
                    this.switchProgress = 1.0f;
                    this.prevState = null;
                }
            }
            float f10 = (this.amplitude * 0.8f) + 1.0f;
            canvas.save();
            canvas.scale(f10, f10, this.cx, this.cy);
            GroupCallActivity.WeavingState weavingState3 = this.currentState;
            if (weavingState3 != null) {
                weavingState3.update((int) (this.cy - AndroidUtilities.dp(100.0f)), (int) (this.cx - AndroidUtilities.dp(100.0f)), AndroidUtilities.dp(200.0f), 16L, this.amplitude);
            }
            this.bigWaveDrawable.update(this.amplitude, 1.0f);
            this.tinyWaveDrawable.update(this.amplitude, 1.0f);
            for (int i = 0; i < 2; i++) {
                if (i != 0 || (weavingState2 = this.prevState) == null) {
                    if (i == 1 && (weavingState = this.currentState) != null) {
                        this.paint.setShader(weavingState.shader);
                        f = this.switchProgress;
                    }
                } else {
                    this.paint.setShader(weavingState2.shader);
                    f = 1.0f - this.switchProgress;
                }
                this.paint.setAlpha((int) (f * 76.0f));
                this.bigWaveDrawable.draw(this.cx, this.cy, canvas, this.paint);
                this.tinyWaveDrawable.draw(this.cx, this.cy, canvas, this.paint);
            }
            canvas.restore();
            float f11 = (this.amplitude * 0.2f) + 1.0f;
            canvas.save();
            canvas.scale(f11, f11, this.cx, this.cy);
            this.avatarImageReceiver.draw(canvas);
            canvas.restore();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMuteButtonState(boolean z) {
            int i;
            if (GroupCallMiniTextureView.this.statusIcon.isMutedByMe() || GroupCallMiniTextureView.this.statusIcon.isMutedByAdmin()) {
                i = 2;
            } else {
                i = GroupCallMiniTextureView.this.statusIcon.isSpeaking() ? 1 : 0;
            }
            if (i == this.muteButtonState) {
                return;
            }
            this.muteButtonState = i;
            GroupCallActivity.WeavingState[] weavingStateArr = this.states;
            if (weavingStateArr[i] == null) {
                weavingStateArr[i] = new GroupCallActivity.WeavingState(i);
                int i2 = this.muteButtonState;
                if (i2 == 2) {
                    this.states[i2].shader = new LinearGradient(0.0f, 400.0f, 400.0f, 0.0f, new int[]{Theme.getColor("voipgroup_mutedByAdminGradient"), Theme.getColor("voipgroup_mutedByAdminGradient3"), Theme.getColor("voipgroup_mutedByAdminGradient2")}, (float[]) null, Shader.TileMode.CLAMP);
                } else if (i2 == 1) {
                    this.states[i2].shader = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{Theme.getColor("voipgroup_muteButton"), Theme.getColor("voipgroup_muteButton3")}, (float[]) null, Shader.TileMode.CLAMP);
                } else {
                    this.states[i2].shader = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{Theme.getColor("voipgroup_unmuteButton2"), Theme.getColor("voipgroup_unmuteButton")}, (float[]) null, Shader.TileMode.CLAMP);
                }
            }
            GroupCallActivity.WeavingState[] weavingStateArr2 = this.states;
            int i3 = this.muteButtonState;
            GroupCallActivity.WeavingState weavingState = weavingStateArr2[i3];
            GroupCallActivity.WeavingState weavingState2 = this.currentState;
            if (weavingState != weavingState2) {
                this.prevState = weavingState2;
                this.currentState = weavingStateArr2[i3];
                if (weavingState2 == null || !z) {
                    this.switchProgress = 1.0f;
                    this.prevState = null;
                } else {
                    this.switchProgress = 0.0f;
                }
            }
            invalidate();
        }

        public void setAmplitude(double d) {
            float f = ((float) d) / 80.0f;
            if (f > 1.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = 0.0f;
            }
            this.animateToAmplitude = f;
            this.animateAmplitudeDiff = (f - this.amplitude) / 200.0f;
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.avatarImageReceiver.onAttachedToWindow();
            this.backgroundImageReceiver.onAttachedToWindow();
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.avatarImageReceiver.onDetachedFromWindow();
            this.backgroundImageReceiver.onDetachedFromWindow();
        }
    }

    public String getName() {
        long peerId = MessageObject.getPeerId(this.participant.participant.peer);
        if (DialogObject.isUserDialog(peerId)) {
            return UserObject.getUserName(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().getUser(Long.valueOf(peerId)));
        }
        return AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().getChat(Long.valueOf(-peerId)).title;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.imageReceiver.onDetachedFromWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
    }

    public void startFlipAnimation() {
        if (this.flipAnimator != null) {
            return;
        }
        this.flipHalfReached = false;
        ImageView imageView = this.blurredFlippingStub;
        if (imageView == null) {
            this.blurredFlippingStub = new ImageView(getContext());
        } else {
            imageView.animate().cancel();
        }
        if (this.textureView.renderer.isFirstFrameRendered()) {
            Bitmap bitmap = this.textureView.blurRenderer.getBitmap(100, 100);
            if (bitmap != null) {
                Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                this.blurredFlippingStub.setBackground(new BitmapDrawable(bitmap));
            }
            this.blurredFlippingStub.setAlpha(0.0f);
        } else {
            this.blurredFlippingStub.setAlpha(1.0f);
        }
        if (this.blurredFlippingStub.getParent() == null) {
            this.textureView.addView(this.blurredFlippingStub);
        }
        ((FrameLayout.LayoutParams) this.blurredFlippingStub.getLayoutParams()).gravity = 17;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.flipAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                GroupCallMiniTextureView.this.lambda$startFlipAnimation$7(valueAnimator);
            }
        });
        this.flipAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.GroupCallMiniTextureView.8
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                GroupCallMiniTextureView groupCallMiniTextureView = GroupCallMiniTextureView.this;
                groupCallMiniTextureView.flipAnimator = null;
                groupCallMiniTextureView.textureView.setRotationY(0.0f);
                GroupCallMiniTextureView groupCallMiniTextureView2 = GroupCallMiniTextureView.this;
                if (!groupCallMiniTextureView2.flipHalfReached) {
                    groupCallMiniTextureView2.textureView.renderer.clearImage();
                }
            }
        });
        this.flipAnimator.setDuration(400L);
        this.flipAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.flipAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startFlipAnimation$7(ValueAnimator valueAnimator) {
        boolean z;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (floatValue < 0.5f) {
            z = false;
        } else {
            floatValue -= 1.0f;
            z = true;
        }
        if (z && !this.flipHalfReached) {
            this.blurredFlippingStub.setAlpha(1.0f);
            this.flipHalfReached = true;
            this.textureView.renderer.clearImage();
        }
        float f = floatValue * 180.0f;
        this.blurredFlippingStub.setRotationY(f);
        this.textureView.renderer.setRotationY(f);
    }
}
