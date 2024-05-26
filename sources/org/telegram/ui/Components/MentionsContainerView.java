package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_inlineBotSwitchPM;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.PaddedListAdapter;
import org.telegram.ui.Business.QuickRepliesActivity;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.PhotoViewer;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class MentionsContainerView extends BlurredFrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private MentionsAdapter adapter;
    private boolean allowBlur;
    BaseFragment baseFragment;
    private PhotoViewer.PhotoViewerProvider botContextProvider;
    private ArrayList<Object> botContextResults;
    private Integer color;
    private float containerBottom;
    private float containerPadding;
    private float containerTop;
    private Delegate delegate;
    private ExtendedGridLayoutManager gridLayoutManager;
    private float hideT;
    private boolean ignoreLayout;
    private LinearLayoutManager linearLayoutManager;
    private MentionsListView listView;
    private boolean listViewHiding;
    private float listViewPadding;
    private SpringAnimation listViewTranslationAnimator;
    private RecyclerListView.OnItemClickListener mentionsOnItemClickListener;
    private PaddedListAdapter paddedAdapter;
    private Paint paint;
    private Path path;
    private android.graphics.Rect rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private int scrollRangeUpdateTries;
    private boolean scrollToFirst;
    private boolean shouldLiftMentions;
    private boolean shown;
    private final SizeNotifierFrameLayout sizeNotifierFrameLayout;
    private boolean switchLayoutManagerOnEnd;
    private Runnable updateVisibilityRunnable;

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static void $default$addEmojiToRecent(Delegate delegate, String str) {
            }

            public static void $default$onStickerSelected(Delegate delegate, TLRPC$TL_document tLRPC$TL_document, String str, Object obj) {
            }

            public static void $default$sendBotInlineResult(Delegate delegate, TLRPC$BotInlineResult tLRPC$BotInlineResult, boolean z, int i) {
            }
        }

        void addEmojiToRecent(String str);

        Paint.FontMetricsInt getFontMetrics();

        void onStickerSelected(TLRPC$TL_document tLRPC$TL_document, String str, Object obj);

        void replaceText(int i, int i2, CharSequence charSequence, boolean z);

        void sendBotInlineResult(TLRPC$BotInlineResult tLRPC$BotInlineResult, boolean z, int i);
    }

    public static /* synthetic */ void lambda$updateListViewTranslation$3(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
    }

    protected boolean canOpen() {
        return true;
    }

    protected void onAnimationScroll() {
    }

    protected void onClose() {
    }

    protected void onContextClick(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
    }

    protected void onContextSearch(boolean z) {
    }

    protected void onOpen() {
    }

    public void onPanTransitionEnd() {
    }

    protected void onScrolled(boolean z, boolean z2) {
    }

    public MentionsContainerView(Context context, long j, long j2, final BaseFragment baseFragment, SizeNotifierFrameLayout sizeNotifierFrameLayout, Theme.ResourcesProvider resourcesProvider) {
        super(context, sizeNotifierFrameLayout);
        this.shouldLiftMentions = false;
        this.rect = new android.graphics.Rect();
        this.ignoreLayout = false;
        this.scrollToFirst = false;
        this.shown = false;
        this.updateVisibilityRunnable = new Runnable() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MentionsContainerView.this.lambda$new$0();
            }
        };
        this.listViewHiding = false;
        this.hideT = 0.0f;
        this.switchLayoutManagerOnEnd = false;
        this.botContextProvider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Components.MentionsContainerView.5
            {
                MentionsContainerView.this = this;
            }

            /* JADX WARN: Removed duplicated region for block: B:48:0x007c A[LOOP:0: B:34:0x0026->B:48:0x007c, LOOP_END] */
            /* JADX WARN: Removed duplicated region for block: B:50:0x0047 A[SYNTHETIC] */
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
                ImageReceiver imageReceiver;
                if (i >= 0 && i < MentionsContainerView.this.botContextResults.size()) {
                    int childCount = MentionsContainerView.this.getListView().getChildCount();
                    Object obj = MentionsContainerView.this.botContextResults.get(i);
                    for (int i2 = 0; i2 < childCount; i2++) {
                        View childAt = MentionsContainerView.this.getListView().getChildAt(i2);
                        if (childAt instanceof ContextLinkCell) {
                            ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                            if (contextLinkCell.getResult() == obj) {
                                imageReceiver = contextLinkCell.getPhotoImage();
                                if (imageReceiver == null) {
                                    int[] iArr = new int[2];
                                    childAt.getLocationInWindow(iArr);
                                    PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                                    placeProviderObject.viewX = iArr[0];
                                    placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT < 21 ? AndroidUtilities.statusBarHeight : 0);
                                    placeProviderObject.parentView = MentionsContainerView.this.getListView();
                                    placeProviderObject.imageReceiver = imageReceiver;
                                    placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                                    placeProviderObject.radius = imageReceiver.getRoundRadius(true);
                                    return placeProviderObject;
                                }
                            }
                        }
                        imageReceiver = null;
                        if (imageReceiver == null) {
                        }
                    }
                }
                return null;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo, boolean z, int i2, boolean z2) {
                if (i < 0 || i >= MentionsContainerView.this.botContextResults.size()) {
                    return;
                }
                MentionsContainerView.this.delegate.sendBotInlineResult((TLRPC$BotInlineResult) MentionsContainerView.this.botContextResults.get(i), z, i2);
            }
        };
        this.baseFragment = baseFragment;
        this.sizeNotifierFrameLayout = sizeNotifierFrameLayout;
        this.resourcesProvider = resourcesProvider;
        this.drawBlur = false;
        this.isTopView = false;
        setVisibility(8);
        setWillNotDraw(false);
        this.listViewPadding = (int) Math.min(AndroidUtilities.dp(126.0f), AndroidUtilities.displaySize.y * 0.22f);
        MentionsListView mentionsListView = new MentionsListView(context, resourcesProvider);
        this.listView = mentionsListView;
        mentionsListView.setTranslationY(AndroidUtilities.dp(6.0f));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) { // from class: org.telegram.ui.Components.MentionsContainerView.1
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            {
                MentionsContainerView.this = this;
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager
            public void setReverseLayout(boolean z) {
                super.setReverseLayout(z);
                MentionsContainerView.this.listView.setTranslationY((z ? -1 : 1) * AndroidUtilities.dp(6.0f));
            }
        };
        this.linearLayoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        ExtendedGridLayoutManager extendedGridLayoutManager = new ExtendedGridLayoutManager(context, 100, false, false) { // from class: org.telegram.ui.Components.MentionsContainerView.2
            private Size size = new Size();

            {
                MentionsContainerView.this = this;
            }

            @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
            protected Size getSizeForItem(int i) {
                TLRPC$PhotoSize closestPhotoSizeWithSize;
                if (i != 0) {
                    int i2 = i - 1;
                    if (MentionsContainerView.this.adapter.getBotContextSwitch() != null || MentionsContainerView.this.adapter.getBotWebViewSwitch() != null) {
                        i2++;
                    }
                    Size size = this.size;
                    size.width = 0.0f;
                    size.height = 0.0f;
                    Object item = MentionsContainerView.this.adapter.getItem(i2);
                    if (item instanceof TLRPC$BotInlineResult) {
                        TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) item;
                        TLRPC$Document tLRPC$Document = tLRPC$BotInlineResult.document;
                        int i3 = 0;
                        if (tLRPC$Document != null) {
                            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                            Size size2 = this.size;
                            size2.width = closestPhotoSizeWithSize2 != null ? closestPhotoSizeWithSize2.w : 100.0f;
                            size2.height = closestPhotoSizeWithSize2 != null ? closestPhotoSizeWithSize2.h : 100.0f;
                            while (i3 < tLRPC$BotInlineResult.document.attributes.size()) {
                                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$BotInlineResult.document.attributes.get(i3);
                                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                                    Size size3 = this.size;
                                    size3.width = tLRPC$DocumentAttribute.w;
                                    size3.height = tLRPC$DocumentAttribute.h;
                                    break;
                                }
                                i3++;
                            }
                        } else if (tLRPC$BotInlineResult.content != null) {
                            while (i3 < tLRPC$BotInlineResult.content.attributes.size()) {
                                TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = tLRPC$BotInlineResult.content.attributes.get(i3);
                                if ((tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeVideo)) {
                                    Size size4 = this.size;
                                    size4.width = tLRPC$DocumentAttribute2.w;
                                    size4.height = tLRPC$DocumentAttribute2.h;
                                    break;
                                }
                                i3++;
                            }
                        } else if (tLRPC$BotInlineResult.thumb != null) {
                            while (i3 < tLRPC$BotInlineResult.thumb.attributes.size()) {
                                TLRPC$DocumentAttribute tLRPC$DocumentAttribute3 = tLRPC$BotInlineResult.thumb.attributes.get(i3);
                                if ((tLRPC$DocumentAttribute3 instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute3 instanceof TLRPC$TL_documentAttributeVideo)) {
                                    Size size5 = this.size;
                                    size5.width = tLRPC$DocumentAttribute3.w;
                                    size5.height = tLRPC$DocumentAttribute3.h;
                                    break;
                                }
                                i3++;
                            }
                        } else {
                            TLRPC$Photo tLRPC$Photo = tLRPC$BotInlineResult.photo;
                            if (tLRPC$Photo != null && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, AndroidUtilities.photoSize.intValue())) != null) {
                                Size size6 = this.size;
                                size6.width = closestPhotoSizeWithSize.w;
                                size6.height = closestPhotoSizeWithSize.h;
                            }
                        }
                    }
                    return this.size;
                }
                this.size.width = getWidth();
                this.size.height = MentionsContainerView.this.paddedAdapter.getPadding();
                return this.size;
            }

            @Override // org.telegram.ui.Components.ExtendedGridLayoutManager
            public int getFlowItemCount() {
                if (MentionsContainerView.this.adapter.getBotContextSwitch() != null || MentionsContainerView.this.adapter.getBotWebViewSwitch() != null) {
                    return getItemCount() - 2;
                }
                return super.getFlowItemCount() - 1;
            }
        };
        this.gridLayoutManager = extendedGridLayoutManager;
        extendedGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.MentionsContainerView.3
            {
                MentionsContainerView.this = this;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                if (i == 0) {
                    return 100;
                }
                int i2 = i - 1;
                Object item = MentionsContainerView.this.adapter.getItem(i2);
                if (item instanceof TLRPC$TL_inlineBotSwitchPM) {
                    return 100;
                }
                if (item instanceof TLRPC$Document) {
                    return 20;
                }
                if (MentionsContainerView.this.adapter.getBotContextSwitch() != null || MentionsContainerView.this.adapter.getBotWebViewSwitch() != null) {
                    i2--;
                }
                return MentionsContainerView.this.gridLayoutManager.getSpanSizeForItem(i2);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(150L);
        defaultItemAnimator.setMoveDuration(150L);
        defaultItemAnimator.setChangeDuration(150L);
        defaultItemAnimator.setRemoveDuration(150L);
        defaultItemAnimator.setTranslationInterpolator(CubicBezierInterpolator.DEFAULT);
        defaultItemAnimator.setDelayAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setClipToPadding(false);
        this.listView.setLayoutManager(this.linearLayoutManager);
        MentionsAdapter mentionsAdapter = new MentionsAdapter(context, false, j, j2, new MentionsAdapter.MentionsAdapterDelegate() { // from class: org.telegram.ui.Components.MentionsContainerView.4
            {
                MentionsContainerView.this = this;
            }

            @Override // org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate
            public void onItemCountUpdate(int i, int i2) {
                if (MentionsContainerView.this.listView.getLayoutManager() == MentionsContainerView.this.gridLayoutManager || !MentionsContainerView.this.shown) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(MentionsContainerView.this.updateVisibilityRunnable);
                AndroidUtilities.runOnUIThread(MentionsContainerView.this.updateVisibilityRunnable, baseFragment.getFragmentBeginToShow() ? 0L : 100L);
            }

            @Override // org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate
            public void needChangePanelVisibility(boolean z) {
                boolean z2 = false;
                if (MentionsContainerView.this.getNeededLayoutManager() != MentionsContainerView.this.getCurrentLayoutManager() && MentionsContainerView.this.canOpen()) {
                    if (MentionsContainerView.this.adapter.getLastItemCount() > 0) {
                        MentionsContainerView.this.switchLayoutManagerOnEnd = true;
                        MentionsContainerView.this.updateVisibility(false);
                        return;
                    }
                    MentionsContainerView.this.listView.setLayoutManager(MentionsContainerView.this.getNeededLayoutManager());
                }
                if (z && !MentionsContainerView.this.canOpen()) {
                    z = false;
                }
                if (!z || MentionsContainerView.this.adapter.getItemCountInternal() > 0) {
                    z2 = z;
                }
                MentionsContainerView.this.updateVisibility(z2);
            }

            @Override // org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate
            public void onContextSearch(boolean z) {
                MentionsContainerView.this.onContextSearch(z);
            }

            @Override // org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate
            public void onContextClick(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
                MentionsContainerView.this.onContextClick(tLRPC$BotInlineResult);
            }
        }, resourcesProvider);
        this.adapter = mentionsAdapter;
        PaddedListAdapter paddedListAdapter = new PaddedListAdapter(mentionsAdapter);
        this.paddedAdapter = paddedListAdapter;
        this.listView.setAdapter(paddedListAdapter);
        addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        setReversed(false);
    }

    public void onPanTransitionStart() {
        this.shouldLiftMentions = isReversed();
    }

    public void onPanTransitionUpdate(float f) {
        if (this.shouldLiftMentions) {
            setTranslationY(f);
        }
    }

    public MentionsListView getListView() {
        return this.listView;
    }

    public MentionsAdapter getAdapter() {
        return this.adapter;
    }

    public void setReversed(boolean z) {
        this.scrollToFirst = true;
        this.linearLayoutManager.setReverseLayout(z);
        this.adapter.setIsReversed(z);
    }

    public boolean isReversed() {
        RecyclerView.LayoutManager layoutManager = this.listView.getLayoutManager();
        LinearLayoutManager linearLayoutManager = this.linearLayoutManager;
        return layoutManager == linearLayoutManager && linearLayoutManager.getReverseLayout();
    }

    public LinearLayoutManager getCurrentLayoutManager() {
        RecyclerView.LayoutManager layoutManager = this.listView.getLayoutManager();
        LinearLayoutManager linearLayoutManager = this.linearLayoutManager;
        return layoutManager == linearLayoutManager ? linearLayoutManager : this.gridLayoutManager;
    }

    public LinearLayoutManager getNeededLayoutManager() {
        return ((this.adapter.isStickers() || this.adapter.isBotContext()) && this.adapter.isMediaLayout()) ? this.gridLayoutManager : this.linearLayoutManager;
    }

    public float clipBottom() {
        if (getVisibility() == 0 && !isReversed()) {
            return getMeasuredHeight() - this.containerTop;
        }
        return 0.0f;
    }

    public float clipTop() {
        if (getVisibility() == 0 && isReversed()) {
            return this.containerBottom;
        }
        return 0.0f;
    }

    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        PaddedListAdapter paddedListAdapter;
        float min;
        PaddedListAdapter paddedListAdapter2;
        boolean isReversed = isReversed();
        this.containerPadding = AndroidUtilities.dp(((this.adapter.isStickers() || this.adapter.isBotContext()) && this.adapter.isMediaLayout() && this.adapter.getBotContextSwitch() == null && this.adapter.getBotWebViewSwitch() == null ? 2 : 0) + 2);
        float dp = AndroidUtilities.dp(6.0f);
        float f = this.containerTop;
        if (isReversed) {
            float min2 = Math.min(Math.max(0.0f, (this.paddedAdapter.paddingViewAttached ? paddedListAdapter2.paddingView.getTop() : getHeight()) + this.listView.getTranslationY()) + this.containerPadding, (1.0f - this.hideT) * getHeight());
            android.graphics.Rect rect = this.rect;
            this.containerTop = 0.0f;
            int measuredWidth = getMeasuredWidth();
            this.containerBottom = min2;
            rect.set(0, (int) 0.0f, measuredWidth, (int) min2);
            min = Math.min(dp, Math.abs(getMeasuredHeight() - this.containerBottom));
            if (min > 0.0f) {
                this.rect.top -= (int) min;
            }
        } else {
            if (this.listView.getLayoutManager() == this.gridLayoutManager) {
                this.containerPadding += AndroidUtilities.dp(2.0f);
                dp += AndroidUtilities.dp(2.0f);
            }
            float max = Math.max(0.0f, (this.paddedAdapter.paddingViewAttached ? paddedListAdapter.paddingView.getBottom() : 0) + this.listView.getTranslationY()) - this.containerPadding;
            this.containerTop = max;
            float max2 = Math.max(max, this.hideT * getHeight());
            android.graphics.Rect rect2 = this.rect;
            this.containerTop = max2;
            int measuredWidth2 = getMeasuredWidth();
            float measuredHeight = getMeasuredHeight();
            this.containerBottom = measuredHeight;
            rect2.set(0, (int) max2, measuredWidth2, (int) measuredHeight);
            min = Math.min(dp, Math.abs(this.containerTop));
            if (min > 0.0f) {
                this.rect.bottom += (int) min;
            }
        }
        float f2 = min;
        if (Math.abs(f - this.containerTop) > 0.1f) {
            onAnimationScroll();
        }
        if (this.paint == null) {
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, 0.0f, 503316480);
        }
        Paint paint2 = this.paint;
        Integer num = this.color;
        paint2.setColor(num != null ? num.intValue() : getThemedColor(Theme.key_chat_messagePanelBackground));
        if (this.allowBlur && SharedConfig.chatBlurEnabled() && this.sizeNotifierFrameLayout != null) {
            if (f2 > 0.0f) {
                canvas.save();
                Path path = this.path;
                if (path == null) {
                    this.path = new Path();
                } else {
                    path.reset();
                }
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(this.rect);
                this.path.addRoundRect(rectF, f2, f2, Path.Direction.CW);
                canvas.clipPath(this.path);
            }
            this.sizeNotifierFrameLayout.drawBlurRect(canvas, getY(), this.rect, this.paint, isReversed);
            if (f2 > 0.0f) {
                canvas.restore();
            }
        } else {
            drawRoundRect(canvas, this.rect, f2);
        }
        canvas.save();
        canvas.clipRect(this.rect);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void drawRoundRect(Canvas canvas, android.graphics.Rect rect, float f) {
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(rect);
        canvas.drawRoundRect(rectF, f, f, this.paint);
    }

    public void setOverrideColor(int i) {
        this.color = Integer.valueOf(i);
        invalidate();
    }

    public void setIgnoreLayout(boolean z) {
        this.ignoreLayout = z;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    public /* synthetic */ void lambda$new$0() {
        updateListViewTranslation(!this.shown, true);
    }

    public void updateVisibility(boolean z) {
        if (z) {
            boolean isReversed = isReversed();
            if (!this.shown) {
                this.scrollToFirst = true;
                RecyclerView.LayoutManager layoutManager = this.listView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = this.linearLayoutManager;
                if (layoutManager == linearLayoutManager) {
                    linearLayoutManager.scrollToPositionWithOffset(0, isReversed ? -100000 : 100000);
                }
                if (getVisibility() == 8) {
                    this.hideT = 1.0f;
                    MentionsListView mentionsListView = this.listView;
                    mentionsListView.setTranslationY(isReversed ? -(this.listViewPadding + AndroidUtilities.dp(12.0f)) : mentionsListView.computeVerticalScrollOffset() + this.listViewPadding);
                }
            }
            setVisibility(0);
        } else {
            this.scrollToFirst = false;
        }
        this.shown = z;
        AndroidUtilities.cancelRunOnUIThread(this.updateVisibilityRunnable);
        SpringAnimation springAnimation = this.listViewTranslationAnimator;
        if (springAnimation != null) {
            springAnimation.cancel();
        }
        Runnable runnable = this.updateVisibilityRunnable;
        BaseFragment baseFragment = this.baseFragment;
        AndroidUtilities.runOnUIThread(runnable, (baseFragment == null || !baseFragment.getFragmentBeginToShow()) ? 100L : 0L);
        if (z) {
            onOpen();
        } else {
            onClose();
        }
    }

    public boolean isOpen() {
        return this.shown;
    }

    private void updateListViewTranslation(final boolean z, boolean z2) {
        float f;
        int i;
        SpringAnimation springAnimation;
        if (this.listView == null || this.paddedAdapter == null) {
            this.scrollRangeUpdateTries = 0;
        } else if (this.listViewHiding && (springAnimation = this.listViewTranslationAnimator) != null && springAnimation.isRunning() && z) {
            this.scrollRangeUpdateTries = 0;
        } else {
            boolean isReversed = isReversed();
            if (z) {
                f = (-this.containerPadding) - AndroidUtilities.dp(6.0f);
            } else {
                int computeVerticalScrollRange = this.listView.computeVerticalScrollRange();
                float padding = (computeVerticalScrollRange - this.paddedAdapter.getPadding()) + this.containerPadding;
                if (computeVerticalScrollRange <= 0 && this.adapter.getItemCountInternal() > 0 && (i = this.scrollRangeUpdateTries) < 3) {
                    this.scrollRangeUpdateTries = i + 1;
                    updateVisibility(true);
                    return;
                }
                f = padding;
            }
            this.scrollRangeUpdateTries = 0;
            float f2 = this.listViewPadding;
            float max = isReversed ? -Math.max(0.0f, f2 - f) : Math.max(0.0f, f2 - f) + (-f2);
            if (z && !isReversed) {
                max += this.listView.computeVerticalScrollOffset();
            }
            final float f3 = max;
            SpringAnimation springAnimation2 = this.listViewTranslationAnimator;
            if (springAnimation2 != null) {
                springAnimation2.cancel();
            }
            Integer num = null;
            if (z2) {
                this.listViewHiding = z;
                final float translationY = this.listView.getTranslationY();
                final float f4 = this.hideT;
                final float f5 = z ? 1.0f : 0.0f;
                if (translationY == f3) {
                    this.listViewTranslationAnimator = null;
                    Integer valueOf = Integer.valueOf(z ? 8 : 0);
                    if (this.switchLayoutManagerOnEnd && z) {
                        this.switchLayoutManagerOnEnd = false;
                        this.listView.setLayoutManager(getNeededLayoutManager());
                        this.shown = true;
                        updateVisibility(true);
                    }
                    num = valueOf;
                } else {
                    SpringAnimation spring = new SpringAnimation(new FloatValueHolder(translationY)).setSpring(new SpringForce(f3).setDampingRatio(1.0f).setStiffness(550.0f));
                    this.listViewTranslationAnimator = spring;
                    spring.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda3
                        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                        public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f6, float f7) {
                            MentionsContainerView.this.lambda$updateListViewTranslation$1(f4, f5, translationY, f3, dynamicAnimation, f6, f7);
                        }
                    });
                    if (z) {
                        this.listViewTranslationAnimator.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda1
                            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z3, float f6, float f7) {
                                MentionsContainerView.this.lambda$updateListViewTranslation$2(z, dynamicAnimation, z3, f6, f7);
                            }
                        });
                    }
                    this.listViewTranslationAnimator.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda2
                        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                        public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z3, float f6, float f7) {
                            MentionsContainerView.lambda$updateListViewTranslation$3(dynamicAnimation, z3, f6, f7);
                        }
                    });
                    this.listViewTranslationAnimator.start();
                }
            } else {
                this.hideT = z ? 1.0f : 0.0f;
                this.listView.setTranslationY(f3);
                if (z) {
                    num = 8;
                }
            }
            if (num == null || getVisibility() == num.intValue()) {
                return;
            }
            setVisibility(num.intValue());
        }
    }

    public /* synthetic */ void lambda$updateListViewTranslation$1(float f, float f2, float f3, float f4, DynamicAnimation dynamicAnimation, float f5, float f6) {
        this.listView.setTranslationY(f5);
        onAnimationScroll();
        this.hideT = AndroidUtilities.lerp(f, f2, (f5 - f3) / (f4 - f3));
    }

    public /* synthetic */ void lambda$updateListViewTranslation$2(boolean z, DynamicAnimation dynamicAnimation, boolean z2, float f, float f2) {
        if (z2) {
            return;
        }
        this.listViewTranslationAnimator = null;
        setVisibility(z ? 8 : 0);
        if (this.switchLayoutManagerOnEnd && z) {
            this.switchLayoutManagerOnEnd = false;
            this.listView.setLayoutManager(getNeededLayoutManager());
            this.shown = true;
            updateVisibility(true);
        }
    }

    public void setDialogId(long j) {
        this.adapter.setDialogId(j);
    }

    public void withDelegate(final Delegate delegate) {
        this.delegate = delegate;
        MentionsListView listView = getListView();
        RecyclerListView.OnItemClickListener onItemClickListener = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                MentionsContainerView.this.lambda$withDelegate$4(delegate, view, i);
            }
        };
        this.mentionsOnItemClickListener = onItemClickListener;
        listView.setOnItemClickListener(onItemClickListener);
        getListView().setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$withDelegate$5;
                lambda$withDelegate$5 = MentionsContainerView.this.lambda$withDelegate$5(view, motionEvent);
                return lambda$withDelegate$5;
            }
        });
    }

    public /* synthetic */ void lambda$withDelegate$4(Delegate delegate, View view, int i) {
        AnimatedEmojiSpan animatedEmojiSpan;
        if (i == 0 || getAdapter().isBannedInline()) {
            return;
        }
        int i2 = i - 1;
        Object item = getAdapter().getItem(i2);
        int resultStartPosition = getAdapter().getResultStartPosition();
        int resultLength = getAdapter().getResultLength();
        if (item instanceof TLRPC$TL_document) {
            if (view instanceof StickerCell) {
                ((StickerCell) view).getSendAnimationData();
            }
            TLRPC$TL_document tLRPC$TL_document = (TLRPC$TL_document) item;
            delegate.onStickerSelected(tLRPC$TL_document, MessageObject.findAnimatedEmojiEmoticon(tLRPC$TL_document), getAdapter().getItemParent(i2));
        } else if (item instanceof TLRPC$Chat) {
            String publicUsername = ChatObject.getPublicUsername((TLRPC$Chat) item);
            if (publicUsername != null) {
                delegate.replaceText(resultStartPosition, resultLength, "@" + publicUsername + " ", false);
            }
        } else if (item instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) item;
            if (UserObject.getPublicUsername(tLRPC$User) != null) {
                delegate.replaceText(resultStartPosition, resultLength, "@" + UserObject.getPublicUsername(tLRPC$User) + " ", false);
            } else {
                SpannableString spannableString = new SpannableString(UserObject.getFirstName(tLRPC$User, false) + " ");
                spannableString.setSpan(new URLSpanUserMention("" + tLRPC$User.id, 3), 0, spannableString.length(), 33);
                delegate.replaceText(resultStartPosition, resultLength, spannableString, false);
            }
        } else if (item instanceof String) {
            delegate.replaceText(resultStartPosition, resultLength, item + " ", false);
        } else if (item instanceof MediaDataController.KeywordResult) {
            String str = ((MediaDataController.KeywordResult) item).emoji;
            delegate.addEmojiToRecent(str);
            if (str != null && str.startsWith("animated_")) {
                Paint.FontMetricsInt fontMetricsInt = null;
                try {
                    try {
                        fontMetricsInt = delegate.getFontMetrics();
                    } catch (Exception e) {
                        FileLog.e((Throwable) e, false);
                    }
                    long parseLong = Long.parseLong(str.substring(9));
                    TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(UserConfig.selectedAccount, parseLong);
                    SpannableString spannableString2 = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(findDocument));
                    if (findDocument != null) {
                        animatedEmojiSpan = new AnimatedEmojiSpan(findDocument, fontMetricsInt);
                    } else {
                        animatedEmojiSpan = new AnimatedEmojiSpan(parseLong, fontMetricsInt);
                    }
                    spannableString2.setSpan(animatedEmojiSpan, 0, spannableString2.length(), 33);
                    delegate.replaceText(resultStartPosition, resultLength, spannableString2, false);
                } catch (Exception unused) {
                    delegate.replaceText(resultStartPosition, resultLength, str, true);
                }
            } else {
                delegate.replaceText(resultStartPosition, resultLength, str, true);
            }
            updateVisibility(false);
        }
        if (item instanceof TLRPC$BotInlineResult) {
            TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) item;
            if ((tLRPC$BotInlineResult.type.equals("photo") && (tLRPC$BotInlineResult.photo != null || tLRPC$BotInlineResult.content != null)) || ((tLRPC$BotInlineResult.type.equals("gif") && (tLRPC$BotInlineResult.document != null || tLRPC$BotInlineResult.content != null)) || (tLRPC$BotInlineResult.type.equals(MediaStreamTrack.VIDEO_TRACK_KIND) && tLRPC$BotInlineResult.document != null))) {
                ArrayList<Object> arrayList = new ArrayList<>(getAdapter().getSearchResultBotContext());
                this.botContextResults = arrayList;
                PhotoViewer.getInstance().setParentActivity(this.baseFragment, this.resourcesProvider);
                PhotoViewer.getInstance().openPhotoForSelect(arrayList, getAdapter().getItemPosition(i2), 3, false, this.botContextProvider, null);
                return;
            }
            delegate.sendBotInlineResult(tLRPC$BotInlineResult, true, 0);
        }
    }

    public /* synthetic */ boolean lambda$withDelegate$5(View view, MotionEvent motionEvent) {
        return ContentPreviewViewer.getInstance().onTouch(motionEvent, getListView(), 0, this.mentionsOnItemClickListener, null, this.resourcesProvider);
    }

    /* loaded from: classes3.dex */
    public class MentionsListView extends RecyclerListView {
        private boolean isDragging;
        private boolean isScrolling;
        private int lastHeight;
        private int lastWidth;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MentionsListView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            MentionsContainerView.this = r1;
            setOnScrollListener(new RecyclerView.OnScrollListener(r1) { // from class: org.telegram.ui.Components.MentionsContainerView.MentionsListView.1
                {
                    MentionsListView.this = this;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                    MentionsListView.this.isScrolling = i != 0;
                    MentionsListView.this.isDragging = i == 1;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    int findLastVisibleItemPosition = MentionsListView.this.getLayoutManager() == MentionsContainerView.this.gridLayoutManager ? MentionsContainerView.this.gridLayoutManager.findLastVisibleItemPosition() : MentionsContainerView.this.linearLayoutManager.findLastVisibleItemPosition();
                    if ((findLastVisibleItemPosition == -1 ? 0 : findLastVisibleItemPosition) > 0 && findLastVisibleItemPosition > MentionsContainerView.this.adapter.getLastItemCount() - 5) {
                        MentionsContainerView.this.adapter.searchForContextBotForNextOffset();
                    }
                    MentionsListView mentionsListView = MentionsListView.this;
                    MentionsContainerView.this.onScrolled(!mentionsListView.canScrollVertically(-1), true ^ MentionsListView.this.canScrollVertically(1));
                }
            });
            addItemDecoration(new RecyclerView.ItemDecoration(r1) { // from class: org.telegram.ui.Components.MentionsContainerView.MentionsListView.2
                {
                    MentionsListView.this = this;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    int childAdapterPosition;
                    rect.left = 0;
                    rect.right = 0;
                    rect.top = 0;
                    rect.bottom = 0;
                    if (recyclerView.getLayoutManager() != MentionsContainerView.this.gridLayoutManager || (childAdapterPosition = recyclerView.getChildAdapterPosition(view)) == 0) {
                        return;
                    }
                    int i = childAdapterPosition - 1;
                    if (MentionsContainerView.this.adapter.isStickers()) {
                        return;
                    }
                    if (MentionsContainerView.this.adapter.getBotContextSwitch() == null && MentionsContainerView.this.adapter.getBotWebViewSwitch() == null) {
                        rect.top = AndroidUtilities.dp(2.0f);
                    } else if (i == 0) {
                        return;
                    } else {
                        i--;
                        if (!MentionsContainerView.this.gridLayoutManager.isFirstRow(i)) {
                            rect.top = AndroidUtilities.dp(2.0f);
                        }
                    }
                    rect.right = MentionsContainerView.this.gridLayoutManager.isLastInRow(i) ? 0 : AndroidUtilities.dp(2.0f);
                }
            });
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (MentionsContainerView.this.linearLayoutManager.getReverseLayout()) {
                if (!this.isDragging && MentionsContainerView.this.paddedAdapter != null && MentionsContainerView.this.paddedAdapter.paddingView != null && MentionsContainerView.this.paddedAdapter.paddingViewAttached && motionEvent.getY() > MentionsContainerView.this.paddedAdapter.paddingView.getTop()) {
                    return false;
                }
            } else if (!this.isDragging && MentionsContainerView.this.paddedAdapter != null && MentionsContainerView.this.paddedAdapter.paddingView != null && MentionsContainerView.this.paddedAdapter.paddingViewAttached && motionEvent.getY() < MentionsContainerView.this.paddedAdapter.paddingView.getBottom()) {
                return false;
            }
            boolean z = !this.isScrolling && ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, MentionsContainerView.this.listView, 0, null, this.resourcesProvider);
            if ((MentionsContainerView.this.adapter.isStickers() && motionEvent.getAction() == 0) || motionEvent.getAction() == 2) {
                MentionsContainerView.this.adapter.doSomeStickersAction();
            }
            return super.onInterceptTouchEvent(motionEvent) || z;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (MentionsContainerView.this.linearLayoutManager.getReverseLayout()) {
                if (!this.isDragging && MentionsContainerView.this.paddedAdapter != null && MentionsContainerView.this.paddedAdapter.paddingView != null && MentionsContainerView.this.paddedAdapter.paddingViewAttached && motionEvent.getY() > MentionsContainerView.this.paddedAdapter.paddingView.getTop()) {
                    return false;
                }
            } else if (!this.isDragging && MentionsContainerView.this.paddedAdapter != null && MentionsContainerView.this.paddedAdapter.paddingView != null && MentionsContainerView.this.paddedAdapter.paddingViewAttached && motionEvent.getY() < MentionsContainerView.this.paddedAdapter.paddingView.getBottom()) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (MentionsContainerView.this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6 = i3 - i;
            int i7 = i4 - i2;
            boolean isReversed = MentionsContainerView.this.isReversed();
            LinearLayoutManager currentLayoutManager = MentionsContainerView.this.getCurrentLayoutManager();
            int findFirstVisibleItemPosition = isReversed ? currentLayoutManager.findFirstVisibleItemPosition() : currentLayoutManager.findLastVisibleItemPosition();
            View findViewByPosition = currentLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
            if (findViewByPosition != null) {
                i5 = findViewByPosition.getTop() - (isReversed ? 0 : this.lastHeight - i7);
            } else {
                i5 = 0;
            }
            super.onLayout(z, i, i2, i3, i4);
            if (MentionsContainerView.this.scrollToFirst) {
                MentionsContainerView.this.ignoreLayout = true;
                currentLayoutManager.scrollToPositionWithOffset(0, 100000);
                super.onLayout(false, i, i2, i3, i4);
                MentionsContainerView.this.ignoreLayout = false;
                MentionsContainerView.this.scrollToFirst = false;
            } else if (findFirstVisibleItemPosition != -1 && i6 == this.lastWidth && i7 - this.lastHeight != 0) {
                MentionsContainerView.this.ignoreLayout = true;
                currentLayoutManager.scrollToPositionWithOffset(findFirstVisibleItemPosition, i5, false);
                super.onLayout(false, i, i2, i3, i4);
                MentionsContainerView.this.ignoreLayout = false;
            }
            this.lastHeight = i7;
            this.lastWidth = i6;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            MentionsContainerView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            if (MentionsContainerView.this.paddedAdapter != null) {
                MentionsContainerView.this.paddedAdapter.setPadding(size);
            }
            MentionsContainerView.this.listViewPadding = (int) Math.min(AndroidUtilities.dp(126.0f), AndroidUtilities.displaySize.y * 0.22f);
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size + ((int) MentionsContainerView.this.listViewPadding), 1073741824));
        }

        @Override // androidx.recyclerview.widget.RecyclerView
        public void onScrolled(int i, int i2) {
            super.onScrolled(i, i2);
            MentionsContainerView.this.invalidate();
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            AndroidUtilities.forEachViews((RecyclerView) this.listView, (Consumer<View>) new Consumer() { // from class: org.telegram.ui.Components.MentionsContainerView$$ExternalSyntheticLambda4
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    MentionsContainerView.lambda$didReceivedNotification$6((View) obj);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$6(View view) {
        if (view instanceof MentionCell) {
            ((MentionCell) view).invalidateEmojis();
        } else if (view instanceof QuickRepliesActivity.QuickReplyView) {
            ((QuickRepliesActivity.QuickReplyView) view).invalidateEmojis();
        } else {
            view.invalidate();
        }
    }
}
