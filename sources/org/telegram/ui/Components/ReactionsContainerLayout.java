package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.PremiumLockIconView;
import org.telegram.ui.Components.Reactions.CustomEmojiReactionsWindow;
import org.telegram.ui.Components.Reactions.HwEmojis;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Stars.StarsReactionsSheet;
import org.telegram.ui.Stories.recorder.HintView2;
/* loaded from: classes3.dex */
public class ReactionsContainerLayout extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    public static final Property TRANSITION_PROGRESS_VALUE = new Property(Float.class, "transitionProgress") { // from class: org.telegram.ui.Components.ReactionsContainerLayout.1
        @Override // android.util.Property
        public Float get(ReactionsContainerLayout reactionsContainerLayout) {
            return Float.valueOf(reactionsContainerLayout.transitionProgress);
        }

        @Override // android.util.Property
        public void set(ReactionsContainerLayout reactionsContainerLayout, Float f) {
            reactionsContainerLayout.setTransitionProgress(f.floatValue());
        }
    };
    private boolean allReactionsAvailable;
    private boolean allReactionsIsDefault;
    public List allReactionsList;
    final HashSet alwaysSelectedReactions;
    private boolean animatePopup;
    private final boolean animationEnabled;
    private Paint bgPaint;
    public int bigCircleOffset;
    private float bigCircleRadius;
    public float bubblesOffset;
    ValueAnimator cancelPressedAnimation;
    private float cancelPressedProgress;
    public boolean channelReactions;
    ChatScrimPopupContainerLayout chatScrimPopupContainerLayout;
    private boolean clicked;
    private int currentAccount;
    private float customEmojiReactionsEnterProgress;
    private InternalImageView customEmojiReactionsIconView;
    FrameLayout customReactionsContainer;
    private ReactionsContainerDelegate delegate;
    public final float durationScale;
    private float flipVerticalProgress;
    BaseFragment fragment;
    public boolean hasHint;
    private boolean hasStar;
    private boolean hintMeasured;
    public TextView hintView;
    public int hintViewHeight;
    public int hintViewWidth;
    public boolean hitLimit;
    private boolean isFlippedVertically;
    public boolean isHiddenNextReaction;
    private boolean isTop;
    public ArrayList items;
    long lastReactionSentTime;
    private long lastUpdate;
    HashSet lastVisibleViews;
    HashSet lastVisibleViewsTmp;
    private float leftAlpha;
    private Paint leftShadowPaint;
    private LinearLayoutManager linearLayoutManager;
    private Adapter listAdapter;
    private int[] location;
    private Path mPath;
    public MessageObject messageObject;
    private float miniBubblesOffset;
    private boolean mirrorX;
    public ReactionHolderView nextRecentReaction;
    public final AnimationNotificationsLocker notificationsLocker;
    public ArrayList oldItems;
    private Runnable onSwitchedToLoopView;
    private float otherViewsScale;
    ChatScrimPopupContainerLayout parentLayout;
    public boolean paused;
    public boolean pausedExceptSelected;
    FrameLayout premiumLockContainer;
    private PremiumLockIconView premiumLockIconView;
    private List premiumLockedReactions;
    private boolean prepareAnimation;
    private float pressedProgress;
    private ReactionsLayoutInBubble.VisibleReaction pressedReaction;
    private int pressedReactionPosition;
    private float pressedViewScale;
    ValueAnimator pullingDownBackAnimator;
    float pullingLeftOffset;
    public float radius;
    CustomEmojiReactionsWindow reactionsWindow;
    public RectF rect;
    RectF rectF;
    public final RecyclerListView recyclerListView;
    Theme.ResourcesProvider resourcesProvider;
    private float rightAlpha;
    private Paint rightShadowPaint;
    private final Paint selectedPaint;
    final HashSet selectedReactions;
    private Drawable shadow;
    private android.graphics.Rect shadowPad;
    private boolean showExpandableReactions;
    boolean skipDraw;
    public boolean skipEnterAnimation;
    private float smallCircleRadius;
    private LinearGradient starSelectedGradient;
    private Matrix starSelectedGradientMatrix;
    private Paint starSelectedGradientPaint;
    private final Paint starSelectedPaint;
    private float transitionProgress;
    private List triggeredReactions;
    private final int type;
    private List visibleReactionsList;
    private long waitingLoadingChatId;

    /* loaded from: classes3.dex */
    public class Adapter extends AdapterWithDiffUtils {
        public Adapter() {
            ReactionsContainerLayout.this = r1;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            ReactionsContainerLayout.this.showUnlockPremium(iArr[0] + (view.getMeasuredWidth() / 2.0f), iArr[1] + (view.getMeasuredHeight() / 2.0f));
        }

        public /* synthetic */ void lambda$onCreateViewHolder$1(View view) {
            ReactionsContainerLayout.this.showCustomEmojiReactionDialog();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ReactionsContainerLayout.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return ((InnerItem) ReactionsContainerLayout.this.items.get(i)).viewType;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0 || viewHolder.getItemViewType() == 3) {
                ReactionHolderView reactionHolderView = (ReactionHolderView) viewHolder.itemView;
                reactionHolderView.setScaleX(1.0f);
                reactionHolderView.setScaleY(1.0f);
                reactionHolderView.setReaction(((InnerItem) ReactionsContainerLayout.this.items.get(i)).reaction, i);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            InternalImageView internalImageView;
            PorterDuffColorFilter porterDuffColorFilter;
            if (i == 1) {
                ReactionsContainerLayout.this.premiumLockContainer = new FrameLayout(ReactionsContainerLayout.this.getContext());
                ReactionsContainerLayout.this.premiumLockIconView = new PremiumLockIconView(ReactionsContainerLayout.this.getContext(), PremiumLockIconView.TYPE_REACTIONS);
                PremiumLockIconView premiumLockIconView = ReactionsContainerLayout.this.premiumLockIconView;
                int color = Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon);
                int i2 = Theme.key_dialogBackground;
                premiumLockIconView.setColor(ColorUtils.blendARGB(color, Theme.getColor(i2), 0.7f));
                ReactionsContainerLayout.this.premiumLockIconView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i2), PorterDuff.Mode.MULTIPLY));
                ReactionsContainerLayout.this.premiumLockIconView.setScaleX(0.0f);
                ReactionsContainerLayout.this.premiumLockIconView.setScaleY(0.0f);
                ReactionsContainerLayout.this.premiumLockIconView.setPadding(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                reactionsContainerLayout.premiumLockContainer.addView(reactionsContainerLayout.premiumLockIconView, LayoutHelper.createFrame(26, 26, 17));
                ReactionsContainerLayout.this.premiumLockIconView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$Adapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ReactionsContainerLayout.Adapter.this.lambda$onCreateViewHolder$0(view2);
                    }
                });
                view = ReactionsContainerLayout.this.premiumLockContainer;
            } else if (i != 2) {
                ReactionsContainerLayout reactionsContainerLayout2 = ReactionsContainerLayout.this;
                view = new ReactionHolderView(reactionsContainerLayout2.getContext(), true);
            } else {
                ReactionsContainerLayout reactionsContainerLayout3 = ReactionsContainerLayout.this;
                ReactionsContainerLayout reactionsContainerLayout4 = ReactionsContainerLayout.this;
                reactionsContainerLayout3.customReactionsContainer = new CustomReactionsContainer(reactionsContainerLayout4.getContext());
                ReactionsContainerLayout reactionsContainerLayout5 = ReactionsContainerLayout.this;
                ReactionsContainerLayout reactionsContainerLayout6 = ReactionsContainerLayout.this;
                reactionsContainerLayout5.customEmojiReactionsIconView = new InternalImageView(reactionsContainerLayout6.getContext());
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setImageResource(R.drawable.msg_reactions_expand);
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                if (ReactionsContainerLayout.this.type == 1 || ReactionsContainerLayout.this.type == 2 || ReactionsContainerLayout.this.type == 4) {
                    internalImageView = ReactionsContainerLayout.this.customEmojiReactionsIconView;
                    porterDuffColorFilter = new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY);
                } else {
                    internalImageView = ReactionsContainerLayout.this.customEmojiReactionsIconView;
                    porterDuffColorFilter = new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground), PorterDuff.Mode.MULTIPLY);
                }
                internalImageView.setColorFilter(porterDuffColorFilter);
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setBackground(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(28.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_listSelector), 40)));
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setPadding(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setContentDescription(LocaleController.getString(R.string.AccDescrExpandPanel));
                ReactionsContainerLayout reactionsContainerLayout7 = ReactionsContainerLayout.this;
                reactionsContainerLayout7.customReactionsContainer.addView(reactionsContainerLayout7.customEmojiReactionsIconView, LayoutHelper.createFrame(30, 30, 17));
                ReactionsContainerLayout.this.customEmojiReactionsIconView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$Adapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ReactionsContainerLayout.Adapter.this.lambda$onCreateViewHolder$1(view2);
                    }
                });
                view = ReactionsContainerLayout.this.customReactionsContainer;
            }
            int topOffset = ((ReactionsContainerLayout.this.getLayoutParams().height - ((int) ReactionsContainerLayout.this.getTopOffset())) - ReactionsContainerLayout.this.getPaddingTop()) - ReactionsContainerLayout.this.getPaddingBottom();
            view.setLayoutParams(new RecyclerView.LayoutParams(topOffset - AndroidUtilities.dp(12.0f), topOffset));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition;
            if ((viewHolder.getItemViewType() == 0 || viewHolder.getItemViewType() == 3) && (adapterPosition = viewHolder.getAdapterPosition()) >= 0 && adapterPosition < ReactionsContainerLayout.this.items.size()) {
                ((ReactionHolderView) viewHolder.itemView).updateSelected(((InnerItem) ReactionsContainerLayout.this.items.get(adapterPosition)).reaction, false);
            }
            super.onViewAttachedToWindow(viewHolder);
        }

        public void updateItems(boolean z) {
            ReactionsContainerLayout.this.oldItems.clear();
            ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
            reactionsContainerLayout.oldItems.addAll(reactionsContainerLayout.items);
            ReactionsContainerLayout.this.items.clear();
            for (int i = 0; i < ReactionsContainerLayout.this.visibleReactionsList.size(); i++) {
                ReactionsLayoutInBubble.VisibleReaction visibleReaction = (ReactionsLayoutInBubble.VisibleReaction) ReactionsContainerLayout.this.visibleReactionsList.get(i);
                ReactionsContainerLayout reactionsContainerLayout2 = ReactionsContainerLayout.this;
                reactionsContainerLayout2.items.add(new InnerItem(visibleReaction.emojicon == null ? 3 : 0, visibleReaction));
            }
            if (ReactionsContainerLayout.this.showUnlockPremiumButton()) {
                ReactionsContainerLayout reactionsContainerLayout3 = ReactionsContainerLayout.this;
                reactionsContainerLayout3.items.add(new InnerItem(1, null));
            }
            if (ReactionsContainerLayout.this.showCustomEmojiReaction()) {
                ReactionsContainerLayout reactionsContainerLayout4 = ReactionsContainerLayout.this;
                reactionsContainerLayout4.items.add(new InnerItem(2, null));
            }
            if (!z) {
                super.notifyDataSetChanged();
                return;
            }
            ReactionsContainerLayout reactionsContainerLayout5 = ReactionsContainerLayout.this;
            setItems(reactionsContainerLayout5.oldItems, reactionsContainerLayout5.items);
        }
    }

    /* loaded from: classes3.dex */
    private class CustomReactionsContainer extends FrameLayout {
        Paint backgroundPaint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CustomReactionsContainer(Context context) {
            super(context);
            ReactionsContainerLayout.this = r1;
            this.backgroundPaint = new Paint(1);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            this.backgroundPaint.setColor((ReactionsContainerLayout.this.type == 1 || ReactionsContainerLayout.this.type == 2 || ReactionsContainerLayout.this.type == 4) ? ColorUtils.setAlphaComponent(-1, 30) : ColorUtils.blendARGB(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon, ReactionsContainerLayout.this.resourcesProvider), Theme.getColor(Theme.key_dialogBackground, ReactionsContainerLayout.this.resourcesProvider), 0.7f));
            float measuredHeight = getMeasuredHeight() / 2.0f;
            float measuredWidth = getMeasuredWidth() / 2.0f;
            View childAt = getChildAt(0);
            float measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dpf2(6.0f)) / 2.0f;
            float expandSize = ReactionsContainerLayout.this.expandSize();
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(measuredWidth - measuredWidth2, (measuredHeight - measuredWidth2) - expandSize, measuredWidth + measuredWidth2, measuredHeight + measuredWidth2 + expandSize);
            canvas.save();
            canvas.scale(childAt.getScaleX(), childAt.getScaleY(), measuredWidth, measuredHeight);
            canvas.drawRoundRect(rectF, measuredWidth2, measuredWidth2, this.backgroundPaint);
            canvas.restore();
            canvas.save();
            canvas.translate(0.0f, expandSize);
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }

    /* loaded from: classes3.dex */
    public class InnerItem extends AdapterWithDiffUtils.Item {
        ReactionsLayoutInBubble.VisibleReaction reaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InnerItem(int i, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(i, false);
            ReactionsContainerLayout.this = r1;
            this.reaction = visibleReaction;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            InnerItem innerItem = (InnerItem) obj;
            int i = this.viewType;
            int i2 = innerItem.viewType;
            if (i != i2 || (i != 0 && i != 3)) {
                return i == i2;
            }
            ReactionsLayoutInBubble.VisibleReaction visibleReaction = this.reaction;
            return visibleReaction != null && visibleReaction.equals(innerItem.reaction);
        }
    }

    /* loaded from: classes3.dex */
    public class InternalImageView extends ImageView {
        boolean isEnter;
        ValueAnimator valueAnimator;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InternalImageView(Context context) {
            super(context);
            ReactionsContainerLayout.this = r1;
        }

        public /* synthetic */ void lambda$play$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            setScaleX(floatValue);
            setScaleY(floatValue);
            ReactionsContainerLayout.this.customReactionsContainer.invalidate();
        }

        public void play(int i, boolean z) {
            this.isEnter = true;
            invalidate();
            ValueAnimator valueAnimator = this.valueAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.valueAnimator.cancel();
            }
            if (!z) {
                setScaleX(1.0f);
                setScaleY(1.0f);
                return;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(getScaleX(), 1.0f);
            this.valueAnimator = ofFloat;
            ofFloat.setInterpolator(AndroidUtilities.overshootInterpolator);
            this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$InternalImageView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ReactionsContainerLayout.InternalImageView.this.lambda$play$0(valueAnimator2);
                }
            });
            this.valueAnimator.setStartDelay(i * ReactionsContainerLayout.this.durationScale);
            this.valueAnimator.setDuration(300L);
            this.valueAnimator.start();
        }

        public void resetAnimation() {
            this.isEnter = false;
            setScaleX(0.0f);
            setScaleY(0.0f);
            ReactionsContainerLayout.this.customReactionsContainer.invalidate();
            ValueAnimator valueAnimator = this.valueAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class LeftRightShadowsListener extends RecyclerView.OnScrollListener {
        private ValueAnimator leftAnimator;
        private boolean leftVisible;
        private ValueAnimator rightAnimator;
        private boolean rightVisible;

        private LeftRightShadowsListener() {
            ReactionsContainerLayout.this = r1;
        }

        public /* synthetic */ void lambda$onScrolled$0(Float f) {
            ReactionsContainerLayout.this.leftShadowPaint.setAlpha((int) (ReactionsContainerLayout.this.leftAlpha = f.floatValue() * 255.0f));
            ReactionsContainerLayout.this.invalidate();
        }

        public /* synthetic */ void lambda$onScrolled$1() {
            this.leftAnimator = null;
        }

        public /* synthetic */ void lambda$onScrolled$2(Float f) {
            ReactionsContainerLayout.this.rightShadowPaint.setAlpha((int) (ReactionsContainerLayout.this.rightAlpha = f.floatValue() * 255.0f));
            ReactionsContainerLayout.this.invalidate();
        }

        public /* synthetic */ void lambda$onScrolled$3() {
            this.rightAnimator = null;
        }

        public static /* synthetic */ void lambda$startAnimator$4(Consumer consumer, ValueAnimator valueAnimator) {
            consumer.accept((Float) valueAnimator.getAnimatedValue());
        }

        private ValueAnimator startAnimator(float f, float f2, final Consumer consumer, final Runnable runnable) {
            ValueAnimator duration = ValueAnimator.ofFloat(f, f2).setDuration(Math.abs(f2 - f) * 150.0f);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ReactionsContainerLayout.LeftRightShadowsListener.lambda$startAnimator$4(Consumer.this, valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.LeftRightShadowsListener.1
                {
                    LeftRightShadowsListener.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
            duration.start();
            return duration;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            boolean z = ReactionsContainerLayout.this.linearLayoutManager.findFirstVisibleItemPosition() != 0;
            if (z != this.leftVisible) {
                ValueAnimator valueAnimator = this.leftAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                this.leftAnimator = startAnimator(ReactionsContainerLayout.this.leftAlpha, z ? 1.0f : 0.0f, new Consumer() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda0
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        ReactionsContainerLayout.LeftRightShadowsListener.this.lambda$onScrolled$0((Float) obj);
                    }
                }, new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ReactionsContainerLayout.LeftRightShadowsListener.this.lambda$onScrolled$1();
                    }
                });
                this.leftVisible = z;
            }
            boolean z2 = ReactionsContainerLayout.this.linearLayoutManager.findLastVisibleItemPosition() != ReactionsContainerLayout.this.listAdapter.getItemCount() - 1;
            if (z2 != this.rightVisible) {
                ValueAnimator valueAnimator2 = this.rightAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.rightAnimator = startAnimator(ReactionsContainerLayout.this.rightAlpha, z2 ? 1.0f : 0.0f, new Consumer() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda2
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        ReactionsContainerLayout.LeftRightShadowsListener.this.lambda$onScrolled$2((Float) obj);
                    }
                }, new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ReactionsContainerLayout.LeftRightShadowsListener.this.lambda$onScrolled$3();
                    }
                });
                this.rightVisible = z2;
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class ReactionHolderView extends FrameLayout {
        public ReactionsLayoutInBubble.VisibleReaction currentReaction;
        public boolean drawSelected;
        public ValueAnimator enterAnimator;
        public BackupImageView enterImageView;
        public float enterScale;
        public boolean hasEnterAnimation;
        private boolean isEnter;
        public boolean isLocked;
        public PremiumLockIconView lockIconView;
        Runnable longPressRunnable;
        public BackupImageView loopImageView;
        public StarsReactionsSheet.Particles particles;
        Runnable playRunnable;
        public int position;
        private ImageReceiver preloadImageReceiver;
        boolean pressed;
        public BackupImageView pressedBackupImageView;
        float pressedX;
        float pressedY;
        private final boolean recyclerReaction;
        public boolean selected;
        public boolean shouldSwitchToLoopView;
        public float sideScale;
        public boolean switchedToLoopView;
        boolean touchable;
        public boolean waitingAnimation;

        /* loaded from: classes3.dex */
        public class 2 extends BackupImageView {
            final /* synthetic */ ReactionsContainerLayout val$this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            2(Context context, ReactionsContainerLayout reactionsContainerLayout) {
                super(context);
                ReactionHolderView.this = r1;
                this.val$this$0 = reactionsContainerLayout;
            }

            public /* synthetic */ void lambda$dispatchDraw$0() {
                ReactionHolderView.this.enterImageView.setVisibility(4);
            }

            @Override // org.telegram.ui.Components.BackupImageView
            protected ImageReceiver createImageReciever() {
                return new ImageReceiver(this) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.2.1
                    {
                        2.this = this;
                    }

                    @Override // org.telegram.messenger.ImageReceiver
                    public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                        if (drawable instanceof RLottieDrawable) {
                            ((RLottieDrawable) drawable).setCurrentFrame(0, false, true);
                        }
                        return super.setImageBitmapByKey(drawable, str, i, z, i2);
                    }
                };
            }

            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                if (this.imageReceiver.getLottieAnimation() != null && !ReactionHolderView.this.waitingAnimation) {
                    this.imageReceiver.getLottieAnimation().start();
                }
                ReactionHolderView reactionHolderView = ReactionHolderView.this;
                if (reactionHolderView.shouldSwitchToLoopView && !reactionHolderView.switchedToLoopView && this.imageReceiver.getLottieAnimation() != null && this.imageReceiver.getLottieAnimation().isLastFrame() && ReactionHolderView.this.loopImageView.imageReceiver.getLottieAnimation() != null && ReactionHolderView.this.loopImageView.imageReceiver.getLottieAnimation().hasBitmap()) {
                    ReactionHolderView reactionHolderView2 = ReactionHolderView.this;
                    reactionHolderView2.switchedToLoopView = true;
                    reactionHolderView2.loopImageView.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                    ReactionHolderView.this.loopImageView.setVisibility(0);
                    if (ReactionsContainerLayout.this.onSwitchedToLoopView != null) {
                        ReactionsContainerLayout.this.onSwitchedToLoopView.run();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$ReactionHolderView$2$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ReactionsContainerLayout.ReactionHolderView.2.this.lambda$dispatchDraw$0();
                        }
                    });
                }
                invalidate();
            }

            @Override // android.view.View
            public void invalidate() {
                if (HwEmojis.grabIfWeakDevice(this, ReactionsContainerLayout.this)) {
                    return;
                }
                super.invalidate();
                ReactionsContainerLayout.this.invalidate();
            }

            @Override // android.view.View
            public void invalidate(int i, int i2, int i3, int i4) {
                if (HwEmojis.grabIfWeakDevice(this)) {
                    return;
                }
                super.invalidate(i, i2, i3, i4);
            }

            @Override // android.view.View
            public void invalidate(android.graphics.Rect rect) {
                if (HwEmojis.grabIfWeakDevice(this, ReactionsContainerLayout.this)) {
                    return;
                }
                super.invalidate(rect);
                ReactionsContainerLayout.this.invalidate();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ReactionHolderView(Context context, boolean z) {
            super(context);
            ReactionsContainerLayout.this = r4;
            this.preloadImageReceiver = new ImageReceiver();
            this.sideScale = 1.0f;
            this.drawSelected = true;
            this.playRunnable = new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.1
                {
                    ReactionHolderView.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (ReactionHolderView.this.enterImageView.getImageReceiver().getLottieAnimation() != null && !ReactionHolderView.this.enterImageView.getImageReceiver().getLottieAnimation().isRunning() && !ReactionHolderView.this.enterImageView.getImageReceiver().getLottieAnimation().isGeneratingCache()) {
                        ReactionHolderView.this.enterImageView.getImageReceiver().getLottieAnimation().start();
                    }
                    ReactionHolderView.this.waitingAnimation = false;
                }
            };
            this.enterScale = 1.0f;
            this.longPressRunnable = new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.5
                {
                    ReactionHolderView.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    ReactionHolderView.this.performHapticFeedback(0);
                    ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                    reactionsContainerLayout.pressedReactionPosition = reactionsContainerLayout.visibleReactionsList.indexOf(ReactionHolderView.this.currentReaction);
                    ReactionHolderView reactionHolderView = ReactionHolderView.this;
                    ReactionsContainerLayout.this.pressedReaction = reactionHolderView.currentReaction;
                    ReactionsContainerLayout.this.invalidate();
                }
            };
            this.touchable = true;
            this.recyclerReaction = z;
            this.enterImageView = new 2(context, r4);
            this.loopImageView = new BackupImageView(context) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.3
                {
                    ReactionHolderView.this = this;
                }

                @Override // org.telegram.ui.Components.BackupImageView
                protected ImageReceiver createImageReciever() {
                    return new ImageReceiver(this) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.3.1
                        {
                            3.this = this;
                        }

                        @Override // org.telegram.messenger.ImageReceiver
                        public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z2, int i2) {
                            boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z2, i2);
                            if (imageBitmapByKey && (drawable instanceof RLottieDrawable)) {
                                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                                rLottieDrawable.setCurrentFrame(0, false, true);
                                rLottieDrawable.stop();
                            }
                            return imageBitmapByKey;
                        }
                    };
                }

                @Override // android.view.View
                public void invalidate() {
                    if (HwEmojis.grabIfWeakDevice(this)) {
                        return;
                    }
                    super.invalidate();
                }

                @Override // android.view.View
                public void invalidate(int i, int i2, int i3, int i4) {
                    if (HwEmojis.grabIfWeakDevice(this)) {
                        return;
                    }
                    super.invalidate(i, i2, i3, i4);
                }

                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                public void onDraw(Canvas canvas) {
                    ReactionHolderView.this.checkPlayLoopImage();
                    super.onDraw(canvas);
                }
            };
            this.enterImageView.getImageReceiver().setAutoRepeat(0);
            this.enterImageView.getImageReceiver().setAllowStartLottieAnimation(false);
            this.pressedBackupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.ReactionHolderView.4
                {
                    ReactionHolderView.this = this;
                }

                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    ReactionsContainerLayout.this.invalidate();
                }

                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                public void onDraw(Canvas canvas) {
                    AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
                    ImageReceiver imageReceiver = animatedEmojiDrawable != null ? animatedEmojiDrawable.getImageReceiver() : this.imageReceiver;
                    if (imageReceiver != null && imageReceiver.getLottieAnimation() != null) {
                        imageReceiver.getLottieAnimation().start();
                    }
                    super.onDraw(canvas);
                }
            };
            addView(this.enterImageView, LayoutHelper.createFrame(34, 34, 17));
            addView(this.pressedBackupImageView, LayoutHelper.createFrame(34, 34, 17));
            addView(this.loopImageView, LayoutHelper.createFrame(34, 34, 17));
            if (r4.type == 4) {
                LayoutTransition layoutTransition = new LayoutTransition();
                layoutTransition.setDuration(100L);
                layoutTransition.enableTransitionType(4);
                setLayoutTransition(layoutTransition);
            }
            this.enterImageView.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
            this.loopImageView.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
            this.loopImageView.imageReceiver.setAutoRepeat(0);
            this.loopImageView.imageReceiver.setAllowStartAnimation(false);
            this.loopImageView.imageReceiver.setAllowStartLottieAnimation(false);
            this.pressedBackupImageView.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
        }

        public /* synthetic */ void lambda$play$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.enterScale = floatValue;
            this.loopImageView.setScaleY(floatValue * (this.selected ? 0.76f : 1.0f));
            this.loopImageView.setScaleX(this.enterScale * (this.selected ? 0.76f : 1.0f));
        }

        /* JADX WARN: Code restructure failed: missing block: B:142:0x015f, code lost:
            if (r12 != null) goto L53;
         */
        /* JADX WARN: Code restructure failed: missing block: B:143:0x0161, code lost:
            r12.setAnimatedEmojiDrawable(r13);
         */
        /* JADX WARN: Code restructure failed: missing block: B:148:0x0189, code lost:
            if (r12 != null) goto L53;
         */
        /* JADX WARN: Code restructure failed: missing block: B:150:0x018c, code lost:
            setFocusable(true);
            r12 = r11.hasEnterAnimation;
            r11.shouldSwitchToLoopView = r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:151:0x0193, code lost:
            if (r12 != false) goto L59;
         */
        /* JADX WARN: Code restructure failed: missing block: B:152:0x0195, code lost:
            r11.enterImageView.setVisibility(8);
            r11.loopImageView.setVisibility(0);
            r11.switchedToLoopView = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x01a2, code lost:
            r11.switchedToLoopView = false;
            r11.enterImageView.setVisibility(0);
            r11.loopImageView.setVisibility(8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:154:0x01ae, code lost:
            r12 = r11.loopImageView.getLayoutParams();
            r13 = r11.loopImageView.getLayoutParams();
            r1 = org.telegram.messenger.AndroidUtilities.dp(34.0f);
            r13.height = r1;
            r12.width = r1;
            r12 = r11.enterImageView.getLayoutParams();
            r13 = r11.enterImageView.getLayoutParams();
            r0 = org.telegram.messenger.AndroidUtilities.dp(34.0f);
            r13.height = r0;
            r12.width = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:155:0x01d8, code lost:
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i) {
            AnimatedEmojiDrawable animatedEmojiDrawable;
            PremiumLockIconView premiumLockIconView;
            PorterDuffColorFilter porterDuffColorFilter;
            updateSelected(visibleReaction, false);
            ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = this.currentReaction;
            if (visibleReaction2 != null && visibleReaction2.equals(visibleReaction)) {
                this.position = i;
                updateImage(visibleReaction);
                return;
            }
            boolean isPremium = UserConfig.getInstance(ReactionsContainerLayout.this.currentAccount).isPremium();
            boolean z = (ReactionsContainerLayout.this.type == 3 && !isPremium) || (ReactionsContainerLayout.this.type == 5 && visibleReaction.premium && !isPremium);
            this.isLocked = z;
            if (z && this.lockIconView == null) {
                PremiumLockIconView premiumLockIconView2 = new PremiumLockIconView(getContext(), PremiumLockIconView.TYPE_STICKERS_PREMIUM_LOCKED);
                this.lockIconView = premiumLockIconView2;
                premiumLockIconView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                this.lockIconView.setImageReceiver(this.loopImageView.getImageReceiver());
                addView(this.lockIconView, LayoutHelper.createFrame(18, 18.0f, 17, 8.0f, 8.0f, 0.0f, 0.0f));
            }
            PremiumLockIconView premiumLockIconView3 = this.lockIconView;
            if (premiumLockIconView3 != null) {
                premiumLockIconView3.setVisibility(this.isLocked ? 0 : 8);
            }
            resetAnimation();
            this.currentReaction = visibleReaction;
            this.hasEnterAnimation = visibleReaction.isStar || (visibleReaction.emojicon != null && ((ReactionsContainerLayout.this.showCustomEmojiReaction() || ReactionsContainerLayout.this.allReactionsIsDefault) && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)));
            if (ReactionsContainerLayout.this.type == 4 || this.currentReaction.isEffect) {
                this.hasEnterAnimation = false;
            }
            ReactionsLayoutInBubble.VisibleReaction visibleReaction3 = this.currentReaction;
            if (visibleReaction3.isStar || visibleReaction3.emojicon != null) {
                updateImage(visibleReaction);
                animatedEmojiDrawable = null;
                this.pressedBackupImageView.setAnimatedEmojiDrawable(null);
                if (this.enterImageView.getImageReceiver().getLottieAnimation() != null) {
                    this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                }
                premiumLockIconView = this.lockIconView;
            } else {
                this.pressedBackupImageView.getImageReceiver().clearImage();
                this.loopImageView.getImageReceiver().clearImage();
                AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(4, ReactionsContainerLayout.this.currentAccount, this.currentReaction.documentId);
                animatedEmojiDrawable = new AnimatedEmojiDrawable(3, ReactionsContainerLayout.this.currentAccount, this.currentReaction.documentId);
                if (ReactionsContainerLayout.this.type == 1 || ReactionsContainerLayout.this.type == 2 || ReactionsContainerLayout.this.type == 4) {
                    PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
                    animatedEmojiDrawable2.setColorFilter(new PorterDuffColorFilter(-1, mode));
                    porterDuffColorFilter = new PorterDuffColorFilter(-1, mode);
                } else {
                    int i2 = Theme.key_windowBackgroundWhiteBlueIcon;
                    int color = Theme.getColor(i2, ReactionsContainerLayout.this.resourcesProvider);
                    PorterDuff.Mode mode2 = PorterDuff.Mode.SRC_IN;
                    animatedEmojiDrawable2.setColorFilter(new PorterDuffColorFilter(color, mode2));
                    porterDuffColorFilter = new PorterDuffColorFilter(Theme.getColor(i2, ReactionsContainerLayout.this.resourcesProvider), mode2);
                }
                animatedEmojiDrawable.setColorFilter(porterDuffColorFilter);
                this.pressedBackupImageView.setAnimatedEmojiDrawable(animatedEmojiDrawable2);
                this.loopImageView.setAnimatedEmojiDrawable(animatedEmojiDrawable);
                premiumLockIconView = this.lockIconView;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:102:0x0137, code lost:
            if (r28.hasEnterAnimation != false) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0176, code lost:
            if (r28.hasEnterAnimation != false) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:111:0x0178, code lost:
            r4 = "60_60_firstframe";
         */
        /* JADX WARN: Code restructure failed: missing block: B:112:0x017b, code lost:
            r4 = "60_60_firstframe";
         */
        /* JADX WARN: Code restructure failed: missing block: B:114:0x018c, code lost:
            if (r28.hasEnterAnimation != false) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x018f, code lost:
            r17 = r1;
            r18 = r2;
            r19 = r4;
            r22 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:117:0x0198, code lost:
            r17 = r1;
            r18 = r2;
            r19 = r4;
            r22 = r16;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void updateImage(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            ImageReceiver imageReceiver;
            ImageLocation forDocument;
            ImageReceiver imageReceiver2;
            ImageLocation imageLocation;
            String str;
            SvgHelper.SvgDrawable svgDrawable;
            if (visibleReaction != null && visibleReaction.isStar) {
                this.enterImageView.getImageReceiver().setImageBitmap(new RLottieDrawable(R.raw.star_reaction, "star_reaction", AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f)));
                this.loopImageView.getImageReceiver().setImageBitmap(getContext().getResources().getDrawable(R.drawable.star_reaction));
                if (this.particles == null) {
                    this.particles = new StarsReactionsSheet.Particles(1, SharedConfig.getDevicePerformanceClass() == 2 ? 45 : 18);
                }
            } else if (ReactionsContainerLayout.this.type == 4 && visibleReaction != null && visibleReaction.emojicon != null) {
                this.enterImageView.getImageReceiver().setImageBitmap(Emoji.getEmojiDrawable(visibleReaction.emojicon));
                this.loopImageView.getImageReceiver().setImageBitmap(Emoji.getEmojiDrawable(visibleReaction.emojicon));
            } else {
                ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = this.currentReaction;
                if (visibleReaction2.isEffect) {
                    TLRPC.Document effectDocument = MessagesController.getInstance(ReactionsContainerLayout.this.currentAccount).getEffectDocument(this.currentReaction.documentId);
                    this.loopImageView.getImageReceiver().setImage(ImageLocation.getForDocument(effectDocument), "60_60_firstframe", null, null, this.hasEnterAnimation ? null : DocumentObject.getSvgThumb(effectDocument, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f), 0L, "tgs", this.currentReaction, 0);
                } else if (visibleReaction2.emojicon != null) {
                    TLRPC.TL_availableReaction tL_availableReaction = MediaDataController.getInstance(ReactionsContainerLayout.this.currentAccount).getReactionsMap().get(this.currentReaction.emojicon);
                    if (tL_availableReaction != null) {
                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tL_availableReaction.activate_animation, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
                        if (LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS) && ReactionsContainerLayout.this.type != 4) {
                            this.enterImageView.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.appear_animation), "30_30_nolimit", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                            imageReceiver = this.loopImageView.getImageReceiver();
                            forDocument = ImageLocation.getForDocument(tL_availableReaction.select_animation);
                            String str2 = "60_60_pcache";
                        } else if (SharedConfig.getDevicePerformanceClass() <= 0 || ReactionsContainerLayout.this.type == 4) {
                            imageReceiver = this.loopImageView.getImageReceiver();
                            forDocument = ImageLocation.getForDocument(tL_availableReaction.select_animation);
                        } else {
                            this.enterImageView.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.appear_animation), "30_30_nolimit", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                            imageReceiver = this.loopImageView.getImageReceiver();
                            forDocument = ImageLocation.getForDocument(tL_availableReaction.select_animation);
                        }
                        imageReceiver2.setImage(imageLocation, str, null, null, svgDrawable, 0L, "tgs", this.currentReaction, 0);
                        if (this.enterImageView.getImageReceiver().getLottieAnimation() != null) {
                            this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false, true);
                        }
                        this.pressedBackupImageView.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.select_animation), "60_60_pcache", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                        this.preloadImageReceiver.setAllowStartLottieAnimation(false);
                        MediaDataController.getInstance(ReactionsContainerLayout.this.currentAccount).preloadImage(this.preloadImageReceiver, ImageLocation.getForDocument(tL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation());
                    }
                    PremiumLockIconView premiumLockIconView = this.lockIconView;
                    if (premiumLockIconView != null) {
                        premiumLockIconView.setImageReceiver(this.loopImageView.getImageReceiver());
                    }
                }
            }
        }

        public void checkPlayLoopImage() {
            BackupImageView backupImageView = this.loopImageView;
            AnimatedEmojiDrawable animatedEmojiDrawable = backupImageView.animatedEmojiDrawable;
            ImageReceiver imageReceiver = animatedEmojiDrawable != null ? animatedEmojiDrawable.getImageReceiver() : backupImageView.imageReceiver;
            if (imageReceiver == null || imageReceiver.getLottieAnimation() == null) {
                return;
            }
            ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
            if (reactionsContainerLayout.reactionsWindow != null || this.pressed || !reactionsContainerLayout.allReactionsIsDefault) {
                imageReceiver.getLottieAnimation().start();
            } else if (imageReceiver.getLottieAnimation().getCurrentFrame() <= 2) {
                imageReceiver.getLottieAnimation().stop();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.selected && this.drawSelected) {
                float measuredWidth = getMeasuredWidth() >> 1;
                float measuredHeight = getMeasuredHeight() >> 1;
                float measuredWidth2 = (getMeasuredWidth() >> 1) - AndroidUtilities.dp(1.0f);
                ReactionsLayoutInBubble.VisibleReaction visibleReaction = this.currentReaction;
                canvas.drawCircle(measuredWidth, measuredHeight, measuredWidth2, (visibleReaction == null || !visibleReaction.isStar) ? ReactionsContainerLayout.this.selectedPaint : ReactionsContainerLayout.this.starSelectedPaint);
            }
            AnimatedEmojiDrawable animatedEmojiDrawable = this.loopImageView.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null) {
                if (this.position == 0) {
                    this.loopImageView.animatedEmojiDrawable.getImageReceiver().setRoundRadius(AndroidUtilities.dp(6.0f), 0, 0, AndroidUtilities.dp(6.0f));
                } else {
                    this.loopImageView.animatedEmojiDrawable.getImageReceiver().setRoundRadius(this.selected ? AndroidUtilities.dp(6.0f) : 0);
                }
            }
            ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = this.currentReaction;
            if (visibleReaction2 != null && visibleReaction2.isStar && this.particles != null && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
                RectF rectF = AndroidUtilities.rectTmp;
                float height = ((int) (getHeight() * 0.7f)) / 2.0f;
                rectF.set((getWidth() / 2.0f) - height, (getHeight() / 2.0f) - height, (getWidth() / 2.0f) + height, (getHeight() / 2.0f) + height);
                RLottieDrawable lottieAnimation = this.enterImageView.getImageReceiver().getLottieAnimation();
                this.particles.setVisible((lottieAnimation == null || lottieAnimation.getCurrentFrame() <= 30) ? 0.0f : Utilities.clamp01((lottieAnimation.getCurrentFrame() - 30) / 30.0f));
                this.particles.setBounds(rectF);
                this.particles.process();
                this.particles.draw(canvas, -673522);
                invalidate();
            }
            super.dispatchDraw(canvas);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            resetAnimation();
            this.preloadImageReceiver.onAttachedToWindow();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.preloadImageReceiver.onDetachedFromWindow();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            ReactionsLayoutInBubble.VisibleReaction visibleReaction = this.currentReaction;
            if (visibleReaction != null) {
                String str = visibleReaction.emojicon;
                if (str == null) {
                    str = LocaleController.getString(R.string.AccDescrCustomEmoji);
                }
                accessibilityNodeInfo.setText(str);
                accessibilityNodeInfo.setEnabled(true);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.touchable && ReactionsContainerLayout.this.cancelPressedAnimation == null) {
                if (motionEvent.getAction() == 0) {
                    this.pressed = true;
                    this.pressedX = motionEvent.getX();
                    this.pressedY = motionEvent.getY();
                    if (this.sideScale == 1.0f && !this.isLocked && ReactionsContainerLayout.this.type != 3 && ReactionsContainerLayout.this.type != 4 && ReactionsContainerLayout.this.type != 5) {
                        AndroidUtilities.runOnUIThread(this.longPressRunnable, ViewConfiguration.getLongPressTimeout());
                    }
                }
                float scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2.0f;
                if ((motionEvent.getAction() == 2 && (Math.abs(this.pressedX - motionEvent.getX()) > scaledTouchSlop || Math.abs(this.pressedY - motionEvent.getY()) > scaledTouchSlop)) || motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    if (motionEvent.getAction() == 1 && this.pressed && ((ReactionsContainerLayout.this.pressedReaction == null || ReactionsContainerLayout.this.pressedProgress > 0.8f) && ReactionsContainerLayout.this.delegate != null)) {
                        ReactionsContainerLayout.this.clicked = true;
                        long currentTimeMillis = System.currentTimeMillis();
                        ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                        if (currentTimeMillis - reactionsContainerLayout.lastReactionSentTime > 300) {
                            reactionsContainerLayout.lastReactionSentTime = System.currentTimeMillis();
                            ReactionsContainerLayout.this.delegate.onReactionClicked(this, this.currentReaction, ReactionsContainerLayout.this.pressedProgress > 0.8f, false);
                        }
                    }
                    if (!ReactionsContainerLayout.this.clicked) {
                        ReactionsContainerLayout.this.cancelPressed();
                    }
                    AndroidUtilities.cancelRunOnUIThread(this.longPressRunnable);
                    this.pressed = false;
                }
                return true;
            }
            return false;
        }

        public boolean play(int i) {
            if (!ReactionsContainerLayout.this.animationEnabled) {
                resetAnimation();
                this.isEnter = true;
                if (!this.hasEnterAnimation) {
                    this.loopImageView.setVisibility(0);
                    this.loopImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                    this.loopImageView.setScaleX(this.enterScale * (this.selected ? 0.76f : 1.0f));
                }
                return false;
            }
            AndroidUtilities.cancelRunOnUIThread(this.playRunnable);
            if (this.hasEnterAnimation) {
                if (this.enterImageView.getImageReceiver().getLottieAnimation() != null && !this.enterImageView.getImageReceiver().getLottieAnimation().isGeneratingCache() && !this.isEnter) {
                    this.isEnter = true;
                    if (i == 0) {
                        this.waitingAnimation = false;
                        this.enterImageView.getImageReceiver().getLottieAnimation().stop();
                        this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                        this.playRunnable.run();
                    } else {
                        this.waitingAnimation = true;
                        this.enterImageView.getImageReceiver().getLottieAnimation().stop();
                        this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                        AndroidUtilities.runOnUIThread(this.playRunnable, i);
                    }
                    return true;
                }
                if (this.enterImageView.getImageReceiver().getLottieAnimation() != null && this.isEnter && !this.enterImageView.getImageReceiver().getLottieAnimation().isRunning() && !this.enterImageView.getImageReceiver().getLottieAnimation().isGeneratingCache()) {
                    this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(this.enterImageView.getImageReceiver().getLottieAnimation().getFramesCount() - 1, false);
                }
                this.loopImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                this.loopImageView.setScaleX(this.enterScale * (this.selected ? 0.76f : 1.0f));
            } else if (!this.isEnter) {
                this.enterScale = 0.0f;
                this.loopImageView.setScaleX(0.0f);
                this.loopImageView.setScaleY(0.0f);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.enterAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$ReactionHolderView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ReactionsContainerLayout.ReactionHolderView.this.lambda$play$0(valueAnimator);
                    }
                });
                this.enterAnimator.setDuration(150L);
                this.enterAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.enterAnimator.setStartDelay(i * ReactionsContainerLayout.this.durationScale);
                this.enterAnimator.start();
                this.isEnter = true;
            }
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:49:0x0091, code lost:
            if (r6.selected != false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x00bc, code lost:
            if (r6.selected != false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:59:0x00be, code lost:
            r1 = 0.76f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x00c1, code lost:
            r0.setScaleX(r4 * r1);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void resetAnimation() {
            BackupImageView backupImageView;
            float f;
            float f2 = 1.0f;
            if (this.hasEnterAnimation) {
                AndroidUtilities.cancelRunOnUIThread(this.playRunnable);
                if (this.enterImageView.getImageReceiver().getLottieAnimation() != null && !this.enterImageView.getImageReceiver().getLottieAnimation().isGeneratingCache()) {
                    this.enterImageView.getImageReceiver().getLottieAnimation().stop();
                    if (ReactionsContainerLayout.this.animationEnabled) {
                        this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false, true);
                    } else {
                        this.enterImageView.getImageReceiver().getLottieAnimation().setCurrentFrame(this.enterImageView.getImageReceiver().getLottieAnimation().getFramesCount() - 1, false, true);
                    }
                }
                this.loopImageView.setVisibility(4);
                this.enterImageView.setVisibility(0);
                this.switchedToLoopView = false;
                this.loopImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                backupImageView = this.loopImageView;
                f = this.enterScale;
            } else {
                this.loopImageView.animate().cancel();
                if (ReactionsContainerLayout.this.skipEnterAnimation) {
                    this.loopImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                    backupImageView = this.loopImageView;
                    f = this.enterScale;
                } else {
                    this.loopImageView.setScaleY(0.0f);
                    this.loopImageView.setScaleX(0.0f);
                }
            }
            this.isEnter = false;
        }

        public void updateSelected(ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z) {
            boolean z2 = this.selected;
            boolean contains = ReactionsContainerLayout.this.selectedReactions.contains(visibleReaction);
            this.selected = contains;
            if (contains != z2) {
                if (z) {
                    ViewPropertyAnimator duration = this.loopImageView.animate().scaleX(this.enterScale * (this.selected ? 0.76f : 1.0f)).scaleY(this.enterScale * (this.selected ? 0.76f : 1.0f)).setDuration(240L);
                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                    duration.setInterpolator(cubicBezierInterpolator).start();
                    this.enterImageView.animate().scaleX(this.enterScale * (this.selected ? 0.76f : 1.0f)).scaleY(this.enterScale * (this.selected ? 0.76f : 1.0f)).setDuration(240L).setInterpolator(cubicBezierInterpolator).start();
                } else {
                    this.loopImageView.setScaleX(this.enterScale * (contains ? 0.76f : 1.0f));
                    this.loopImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                    this.enterImageView.setScaleX(this.enterScale * (this.selected ? 0.76f : 1.0f));
                    this.enterImageView.setScaleY(this.enterScale * (this.selected ? 0.76f : 1.0f));
                }
                requestLayout();
                invalidate();
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface ReactionsContainerDelegate {

        /* loaded from: classes3.dex */
        public abstract /* synthetic */ class -CC {
            public static boolean $default$drawBackground(ReactionsContainerDelegate reactionsContainerDelegate) {
                return false;
            }

            public static void $default$drawRoundRect(ReactionsContainerDelegate reactionsContainerDelegate, Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
            }

            public static boolean $default$needEnterText(ReactionsContainerDelegate reactionsContainerDelegate) {
                return false;
            }

            public static void $default$onEmojiWindowDismissed(ReactionsContainerDelegate reactionsContainerDelegate) {
            }
        }

        boolean drawBackground();

        void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z);

        boolean needEnterText();

        void onEmojiWindowDismissed();

        void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2);
    }

    public ReactionsContainerLayout(final int i, BaseFragment baseFragment, Context context, int i2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        Paint paint;
        int blendARGB;
        this.items = new ArrayList();
        this.oldItems = new ArrayList();
        boolean z = true;
        this.bgPaint = new Paint(1);
        this.leftShadowPaint = new Paint(1);
        this.rightShadowPaint = new Paint(1);
        this.transitionProgress = 1.0f;
        this.rect = new RectF();
        this.mPath = new Path();
        this.radius = AndroidUtilities.dp(72.0f);
        float dp = AndroidUtilities.dp(8.0f);
        this.bigCircleRadius = dp;
        this.smallCircleRadius = dp / 2.0f;
        this.bigCircleOffset = AndroidUtilities.dp(36.0f);
        this.visibleReactionsList = new ArrayList(20);
        this.premiumLockedReactions = new ArrayList(10);
        this.allReactionsList = new ArrayList(20);
        this.rectF = new RectF();
        this.hasStar = false;
        this.selectedReactions = new HashSet();
        this.alwaysSelectedReactions = new HashSet();
        this.location = new int[2];
        this.shadowPad = new android.graphics.Rect();
        this.triggeredReactions = new ArrayList();
        this.lastVisibleViews = new HashSet();
        this.lastVisibleViewsTmp = new HashSet();
        Paint paint2 = new Paint(1);
        this.selectedPaint = paint2;
        Paint paint3 = new Paint(1);
        this.starSelectedPaint = paint3;
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.isHiddenNextReaction = true;
        this.paused = false;
        this.type = i;
        this.durationScale = Settings.Global.getFloat(context.getContentResolver(), "animator_duration_scale", 1.0f);
        paint2.setColor(Theme.getColor(Theme.key_listSelector, resourcesProvider));
        paint3.setColor(Theme.getColor(Theme.key_reactionStarSelector, resourcesProvider));
        this.resourcesProvider = resourcesProvider;
        this.currentAccount = i2;
        this.fragment = baseFragment;
        ReactionHolderView reactionHolderView = new ReactionHolderView(context, false);
        this.nextRecentReaction = reactionHolderView;
        reactionHolderView.setVisibility(8);
        ReactionHolderView reactionHolderView2 = this.nextRecentReaction;
        reactionHolderView2.touchable = false;
        reactionHolderView2.pressedBackupImageView.setVisibility(8);
        addView(this.nextRecentReaction);
        this.animationEnabled = (!SharedConfig.animationsEnabled() || SharedConfig.getDevicePerformanceClass() == 0) ? false : false;
        this.shadow = ContextCompat.getDrawable(context, R.drawable.reactions_bubble_shadow).mutate();
        android.graphics.Rect rect = this.shadowPad;
        int dp2 = AndroidUtilities.dp(7.0f);
        rect.bottom = dp2;
        rect.right = dp2;
        rect.top = dp2;
        rect.left = dp2;
        this.shadow.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelShadow), PorterDuff.Mode.MULTIPLY));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.2
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    if (motionEvent.getAction() != 1 || ReactionsContainerLayout.this.getPullingLeftProgress() <= 0.95f) {
                        ReactionsContainerLayout.this.animatePullingBack();
                    } else {
                        ReactionsContainerLayout.this.showCustomEmojiReactionDialog();
                    }
                }
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                if (ReactionsContainerLayout.this.pressedReaction != null && (view instanceof ReactionHolderView) && ((ReactionHolderView) view).currentReaction.equals(ReactionsContainerLayout.this.pressedReaction)) {
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }
        };
        this.recyclerListView = recyclerListView;
        recyclerListView.setClipChildren(false);
        recyclerListView.setClipToPadding(false);
        this.linearLayoutManager = new LinearLayoutManager(context, 0, false) { // from class: org.telegram.ui.Components.ReactionsContainerLayout.3
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int scrollHorizontallyBy(int i3, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int i4;
                if (i3 < 0) {
                    ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                    if (reactionsContainerLayout.pullingLeftOffset != 0.0f) {
                        float pullingLeftProgress = reactionsContainerLayout.getPullingLeftProgress();
                        ReactionsContainerLayout reactionsContainerLayout2 = ReactionsContainerLayout.this;
                        reactionsContainerLayout2.pullingLeftOffset += i3;
                        if ((pullingLeftProgress > 1.0f) != (reactionsContainerLayout2.getPullingLeftProgress() > 1.0f)) {
                            ReactionsContainerLayout.this.recyclerListView.performHapticFeedback(3);
                        }
                        ReactionsContainerLayout reactionsContainerLayout3 = ReactionsContainerLayout.this;
                        float f = reactionsContainerLayout3.pullingLeftOffset;
                        if (f < 0.0f) {
                            i4 = (int) f;
                            reactionsContainerLayout3.pullingLeftOffset = 0.0f;
                        } else {
                            i4 = 0;
                        }
                        FrameLayout frameLayout = reactionsContainerLayout3.customReactionsContainer;
                        if (frameLayout != null) {
                            frameLayout.invalidate();
                        }
                        ReactionsContainerLayout.this.recyclerListView.invalidate();
                        i3 = i4;
                    }
                }
                int scrollHorizontallyBy = super.scrollHorizontallyBy(i3, recycler, state);
                if (i3 > 0 && scrollHorizontallyBy == 0 && ReactionsContainerLayout.this.recyclerListView.getScrollState() == 1 && ReactionsContainerLayout.this.showCustomEmojiReaction()) {
                    ValueAnimator valueAnimator = ReactionsContainerLayout.this.pullingDownBackAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        ReactionsContainerLayout.this.pullingDownBackAnimator.cancel();
                    }
                    float pullingLeftProgress2 = ReactionsContainerLayout.this.getPullingLeftProgress();
                    float f2 = pullingLeftProgress2 > 1.0f ? 0.05f : 0.6f;
                    ReactionsContainerLayout reactionsContainerLayout4 = ReactionsContainerLayout.this;
                    reactionsContainerLayout4.pullingLeftOffset += i3 * f2;
                    if ((pullingLeftProgress2 > 1.0f) != (reactionsContainerLayout4.getPullingLeftProgress() > 1.0f)) {
                        ReactionsContainerLayout.this.recyclerListView.performHapticFeedback(3);
                    }
                    FrameLayout frameLayout2 = ReactionsContainerLayout.this.customReactionsContainer;
                    if (frameLayout2 != null) {
                        frameLayout2.invalidate();
                    }
                    ReactionsContainerLayout.this.recyclerListView.invalidate();
                }
                return scrollHorizontallyBy;
            }
        };
        recyclerListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.4
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect2, View view, RecyclerView recyclerView, RecyclerView.State state) {
                int i3;
                super.getItemOffsets(rect2, view, recyclerView, state);
                if (ReactionsContainerLayout.this.showCustomEmojiReaction()) {
                    i3 = 0;
                    rect2.left = 0;
                } else {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    if (childAdapterPosition == 0) {
                        rect2.left = AndroidUtilities.dp(6.0f);
                    }
                    rect2.right = AndroidUtilities.dp(4.0f);
                    if (childAdapterPosition != ReactionsContainerLayout.this.listAdapter.getItemCount() - 1) {
                        return;
                    }
                    i3 = (ReactionsContainerLayout.this.showUnlockPremiumButton() || ReactionsContainerLayout.this.showCustomEmojiReaction()) ? AndroidUtilities.dp(2.0f) : AndroidUtilities.dp(6.0f);
                }
                rect2.right = i3;
            }
        });
        recyclerListView.setLayoutManager(this.linearLayoutManager);
        recyclerListView.setOverScrollMode(2);
        Adapter adapter = new Adapter();
        this.listAdapter = adapter;
        recyclerListView.setAdapter(adapter);
        recyclerListView.addOnScrollListener(new LeftRightShadowsListener());
        recyclerListView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.5
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                if (recyclerView.getChildCount() > 2) {
                    recyclerView.getLocationInWindow(ReactionsContainerLayout.this.location);
                    int i5 = ReactionsContainerLayout.this.location[0];
                    View childAt = recyclerView.getChildAt(0);
                    childAt.getLocationInWindow(ReactionsContainerLayout.this.location);
                    float min = ((1.0f - Math.min(1.0f, (-Math.min(ReactionsContainerLayout.this.location[0] - i5, 0.0f)) / childAt.getWidth())) * 0.39999998f) + 0.6f;
                    if (Float.isNaN(min)) {
                        min = 1.0f;
                    }
                    ReactionsContainerLayout.this.setChildScale(childAt, min);
                    View childAt2 = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    childAt2.getLocationInWindow(ReactionsContainerLayout.this.location);
                    float min2 = ((1.0f - Math.min(1.0f, (-Math.min((i5 + recyclerView.getWidth()) - (ReactionsContainerLayout.this.location[0] + childAt2.getWidth()), 0.0f)) / childAt2.getWidth())) * 0.39999998f) + 0.6f;
                    if (Float.isNaN(min2)) {
                        min2 = 1.0f;
                    }
                    ReactionsContainerLayout.this.setChildScale(childAt2, min2);
                }
                for (int i6 = 1; i6 < ReactionsContainerLayout.this.recyclerListView.getChildCount() - 1; i6++) {
                    ReactionsContainerLayout.this.setChildScale(ReactionsContainerLayout.this.recyclerListView.getChildAt(i6), 1.0f);
                }
                ReactionsContainerLayout.this.invalidate();
            }
        });
        recyclerListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.6
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect2, View view, RecyclerView recyclerView, RecyclerView.State state) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                if (childAdapterPosition == 0) {
                    rect2.left = AndroidUtilities.dp(8.0f);
                }
                if (childAdapterPosition == ReactionsContainerLayout.this.listAdapter.getItemCount() - 1) {
                    rect2.right = AndroidUtilities.dp(8.0f);
                }
            }
        });
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                ReactionsContainerLayout.this.lambda$new$0(view, i3);
            }
        });
        recyclerListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i3) {
                boolean lambda$new$1;
                lambda$new$1 = ReactionsContainerLayout.this.lambda$new$1(i, view, i3);
                return lambda$new$1;
            }
        });
        addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
        setClipChildren(false);
        setClipToPadding(false);
        invalidateShaders();
        int paddingTop = (recyclerListView.getLayoutParams().height - recyclerListView.getPaddingTop()) - recyclerListView.getPaddingBottom();
        this.nextRecentReaction.getLayoutParams().width = paddingTop - AndroidUtilities.dp(12.0f);
        this.nextRecentReaction.getLayoutParams().height = paddingTop;
        if (i == 2 || i == 4) {
            paint = this.bgPaint;
            blendARGB = ColorUtils.blendARGB(-16777216, -1, 0.13f);
        } else {
            paint = this.bgPaint;
            blendARGB = Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider);
        }
        paint.setColor(blendARGB);
        MediaDataController.getInstance(i2).preloadDefaultReactions();
    }

    public static boolean allowSmoothEnterTransition() {
        return SharedConfig.deviceIsHigh();
    }

    public void animatePullingBack() {
        if (this.pullingLeftOffset != 0.0f) {
            ValueAnimator valueAnimator = this.pullingDownBackAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pullingLeftOffset, 0.0f);
            this.pullingDownBackAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ReactionsContainerLayout.this.lambda$animatePullingBack$2(valueAnimator2);
                }
            });
            this.pullingDownBackAnimator.setDuration(150L);
            this.pullingDownBackAnimator.start();
        }
    }

    public void cancelPressed() {
        if (this.pressedReaction != null) {
            this.cancelPressedProgress = 0.0f;
            final float f = this.pressedProgress;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.cancelPressedAnimation = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.8
                {
                    ReactionsContainerLayout.this = this;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ReactionsContainerLayout.this.cancelPressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                    reactionsContainerLayout.pressedProgress = f * (1.0f - reactionsContainerLayout.cancelPressedProgress);
                    ReactionsContainerLayout.this.invalidate();
                }
            });
            this.cancelPressedAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.9
                {
                    ReactionsContainerLayout.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    ReactionsContainerLayout reactionsContainerLayout = ReactionsContainerLayout.this;
                    reactionsContainerLayout.cancelPressedAnimation = null;
                    reactionsContainerLayout.pressedProgress = 0.0f;
                    ReactionsContainerLayout.this.pressedReaction = null;
                    ReactionsContainerLayout.this.invalidate();
                }
            });
            this.cancelPressedAnimation.setDuration(150L);
            this.cancelPressedAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.cancelPressedAnimation.start();
        }
    }

    private void checkPressedProgress(Canvas canvas, ReactionHolderView reactionHolderView) {
        AnimatedEmojiDrawable animatedEmojiDrawable;
        float f = 0.0f;
        float clamp = this.pullingLeftOffset != 0.0f ? Utilities.clamp(reactionHolderView.getLeft() / (getMeasuredWidth() - AndroidUtilities.dp(34.0f)), 1.0f, 0.0f) * getPullingLeftProgress() * AndroidUtilities.dp(46.0f) : 0.0f;
        if (!reactionHolderView.currentReaction.equals(this.pressedReaction)) {
            int childAdapterPosition = this.recyclerListView.getChildAdapterPosition(reactionHolderView);
            float measuredWidth = ((reactionHolderView.getMeasuredWidth() * (this.pressedViewScale - 1.0f)) / 3.0f) - ((reactionHolderView.getMeasuredWidth() * (1.0f - this.otherViewsScale)) * (Math.abs(this.pressedReactionPosition - childAdapterPosition) - 1));
            if (childAdapterPosition < this.pressedReactionPosition) {
                reactionHolderView.setPivotX(0.0f);
                reactionHolderView.setTranslationX(-measuredWidth);
            } else {
                reactionHolderView.setPivotX(reactionHolderView.getMeasuredWidth() - clamp);
                reactionHolderView.setTranslationX(measuredWidth - clamp);
            }
            reactionHolderView.setPivotY(reactionHolderView.enterImageView.getY() + reactionHolderView.enterImageView.getMeasuredHeight());
            reactionHolderView.setScaleX(this.otherViewsScale);
            reactionHolderView.setScaleY(this.otherViewsScale);
            reactionHolderView.pressedBackupImageView.setVisibility(4);
            reactionHolderView.enterImageView.setAlpha(1.0f);
            return;
        }
        BackupImageView backupImageView = reactionHolderView.loopImageView.getVisibility() == 0 ? reactionHolderView.loopImageView : reactionHolderView.enterImageView;
        reactionHolderView.setPivotX(reactionHolderView.getMeasuredWidth() >> 1);
        reactionHolderView.setPivotY(backupImageView.getY() + backupImageView.getMeasuredHeight());
        reactionHolderView.setScaleX(this.pressedViewScale);
        reactionHolderView.setScaleY(this.pressedViewScale);
        if (!this.clicked) {
            if (this.cancelPressedAnimation == null) {
                reactionHolderView.pressedBackupImageView.setVisibility(0);
                reactionHolderView.pressedBackupImageView.setAlpha(1.0f);
                if (reactionHolderView.pressedBackupImageView.getImageReceiver().hasBitmapImage() || ((animatedEmojiDrawable = reactionHolderView.pressedBackupImageView.animatedEmojiDrawable) != null && animatedEmojiDrawable.getImageReceiver() != null && reactionHolderView.pressedBackupImageView.animatedEmojiDrawable.getImageReceiver().hasBitmapImage())) {
                    backupImageView.setAlpha(0.0f);
                }
            } else {
                reactionHolderView.pressedBackupImageView.setAlpha(1.0f - this.cancelPressedProgress);
                backupImageView.setAlpha(this.cancelPressedProgress);
            }
            if (this.pressedProgress == 1.0f) {
                this.clicked = true;
                if (System.currentTimeMillis() - this.lastReactionSentTime > 300) {
                    this.lastReactionSentTime = System.currentTimeMillis();
                    this.delegate.onReactionClicked(reactionHolderView, reactionHolderView.currentReaction, true, false);
                }
            }
        }
        canvas.save();
        float x = this.recyclerListView.getX() + reactionHolderView.getX();
        float measuredWidth2 = ((reactionHolderView.getMeasuredWidth() * reactionHolderView.getScaleX()) - reactionHolderView.getMeasuredWidth()) / 2.0f;
        float f2 = x - measuredWidth2;
        if (f2 >= 0.0f || reactionHolderView.getTranslationX() < 0.0f) {
            if (reactionHolderView.getMeasuredWidth() + x + measuredWidth2 > getMeasuredWidth() && reactionHolderView.getTranslationX() <= 0.0f) {
                f = ((getMeasuredWidth() - x) - reactionHolderView.getMeasuredWidth()) - measuredWidth2;
            }
            reactionHolderView.setTranslationX(f - clamp);
        } else {
            reactionHolderView.setTranslationX((-f2) - clamp);
        }
        canvas.translate(this.recyclerListView.getX() + reactionHolderView.getX(), this.recyclerListView.getY() + reactionHolderView.getY());
        canvas.scale(reactionHolderView.getScaleX(), reactionHolderView.getScaleY(), reactionHolderView.getPivotX(), reactionHolderView.getPivotY());
        reactionHolderView.draw(canvas);
        canvas.restore();
    }

    private void checkPressedProgressForOtherViews(View view) {
        int childAdapterPosition = this.recyclerListView.getChildAdapterPosition(view);
        float measuredWidth = ((view.getMeasuredWidth() * (this.pressedViewScale - 1.0f)) / 3.0f) - ((view.getMeasuredWidth() * (1.0f - this.otherViewsScale)) * (Math.abs(this.pressedReactionPosition - childAdapterPosition) - 1));
        if (childAdapterPosition < this.pressedReactionPosition) {
            view.setPivotX(0.0f);
            view.setTranslationX(-measuredWidth);
        } else {
            view.setPivotX(view.getMeasuredWidth());
            view.setTranslationX(measuredWidth);
        }
        view.setScaleX(this.otherViewsScale);
        view.setScaleY(this.otherViewsScale);
    }

    private void drawBubbles(Canvas canvas, float f, float f2, float f3, int i) {
        if (this.type == 1) {
            return;
        }
        canvas.save();
        if (this.isTop) {
            canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), (AndroidUtilities.lerp(this.rect.top, getMeasuredHeight(), CubicBezierInterpolator.DEFAULT.getInterpolation(this.flipVerticalProgress)) - ((int) Math.ceil((this.rect.height() / 2.0f) * (1.0f - this.transitionProgress)))) + 1.0f);
        } else {
            float f4 = this.rect.bottom;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            canvas.clipRect(0.0f, (AndroidUtilities.lerp(f4, 0.0f, cubicBezierInterpolator.getInterpolation(this.flipVerticalProgress)) - ((int) Math.ceil((this.rect.height() / 2.0f) * (1.0f - this.transitionProgress)))) - 1.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight() + AndroidUtilities.dp(8.0f), getPaddingTop() - expandSize(), cubicBezierInterpolator.getInterpolation(this.flipVerticalProgress)));
        }
        float width = ((LocaleController.isRTL || this.mirrorX) ? this.bigCircleOffset : getWidth() - this.bigCircleOffset) + this.bubblesOffset;
        float paddingTop = this.isTop ? getPaddingTop() - expandSize() : (getHeight() - getPaddingBottom()) + expandSize();
        int dp = AndroidUtilities.dp(3.0f);
        this.shadow.setAlpha(i);
        this.bgPaint.setAlpha(i);
        float f5 = width - f;
        float f6 = dp;
        float f7 = f6 * f2;
        float f8 = paddingTop - f;
        float f9 = width + f;
        float f10 = paddingTop + f;
        this.shadow.setBounds((int) (f5 - f7), (int) (f8 - f7), (int) (f9 + f7), (int) (f7 + f10));
        this.shadow.draw(canvas);
        if (this.delegate.drawBackground()) {
            this.rectF.set(f5, f8, f9, f10);
            this.delegate.drawRoundRect(canvas, this.rectF, f, getX(), getY(), i, false);
        } else {
            canvas.drawCircle(width, paddingTop, f, this.bgPaint);
        }
        float width2 = ((LocaleController.isRTL || this.mirrorX) ? this.bigCircleOffset - this.bigCircleRadius : (getWidth() - this.bigCircleOffset) + this.bigCircleRadius) + this.bubblesOffset + this.miniBubblesOffset;
        float lerp = AndroidUtilities.lerp(this.isTop ? (getPaddingTop() - expandSize()) - AndroidUtilities.dp(16.0f) : ((getHeight() - this.smallCircleRadius) - f6) + expandSize(), (this.smallCircleRadius + f6) - expandSize(), CubicBezierInterpolator.DEFAULT.getInterpolation(this.flipVerticalProgress));
        float f11 = (-AndroidUtilities.dp(1.0f)) * f2;
        this.shadow.setBounds((int) ((width2 - f) - f11), (int) ((lerp - f) - f11), (int) (width2 + f + f11), (int) (lerp + f + f11));
        this.shadow.draw(canvas);
        if (this.delegate.drawBackground()) {
            this.rectF.set(width2 - f3, lerp - f3, width2 + f3, lerp + f3);
            this.delegate.drawRoundRect(canvas, this.rectF, f3, getX(), getY(), i, false);
        } else {
            canvas.drawCircle(width2, lerp, f3, this.bgPaint);
        }
        canvas.restore();
        this.shadow.setAlpha(NotificationCenter.messagePlayingSpeedChanged);
        this.bgPaint.setAlpha(NotificationCenter.messagePlayingSpeedChanged);
    }

    private void fillRecentReactionsList(List list) {
        HashSet hashSet = new HashSet();
        int i = this.type;
        int i2 = 0;
        if (i == 4) {
            Iterator it = this.selectedReactions.iterator();
            int i3 = 0;
            while (it.hasNext()) {
                ReactionsLayoutInBubble.VisibleReaction visibleReaction = (ReactionsLayoutInBubble.VisibleReaction) it.next();
                if (!hashSet.contains(visibleReaction)) {
                    hashSet.add(visibleReaction);
                    list.add(visibleReaction);
                    i3++;
                    if (i3 >= 8) {
                        return;
                    }
                }
            }
            List<TLRPC.TL_availableReaction> enabledReactionsList = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
            while (i2 < enabledReactionsList.size()) {
                ReactionsLayoutInBubble.VisibleReaction fromEmojicon = ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(enabledReactionsList.get(i2));
                if (!hashSet.contains(fromEmojicon)) {
                    hashSet.add(fromEmojicon);
                    list.add(fromEmojicon);
                    i3++;
                    if (i3 >= 8) {
                        return;
                    }
                }
                i2++;
            }
        } else if (!this.allReactionsAvailable || i == 4) {
            if (i != 3) {
                List<TLRPC.TL_availableReaction> enabledReactionsList2 = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
                while (i2 < enabledReactionsList2.size()) {
                    list.add(ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(enabledReactionsList2.get(i2)));
                    i2++;
                }
                return;
            }
            ArrayList<TLRPC.Reaction> savedReactions = MediaDataController.getInstance(this.currentAccount).getSavedReactions();
            int i4 = 0;
            while (i2 < savedReactions.size()) {
                ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(savedReactions.get(i2));
                if (!hashSet.contains(fromTL)) {
                    hashSet.add(fromTL);
                    list.add(fromTL);
                    i4++;
                }
                if (i4 == 16) {
                    return;
                }
                i2++;
            }
        } else if (i == 5) {
            TLRPC.messages_AvailableEffects availableEffects = MessagesController.getInstance(this.currentAccount).getAvailableEffects();
            if (availableEffects != null) {
                while (i2 < availableEffects.effects.size()) {
                    ReactionsLayoutInBubble.VisibleReaction fromTL2 = ReactionsLayoutInBubble.VisibleReaction.fromTL(availableEffects.effects.get(i2));
                    if (!hashSet.contains(fromTL2)) {
                        hashSet.add(fromTL2);
                        list.add(fromTL2);
                    }
                    i2++;
                }
            }
        } else {
            ArrayList<TLRPC.Reaction> savedReactions2 = i == 3 ? MediaDataController.getInstance(this.currentAccount).getSavedReactions() : MediaDataController.getInstance(this.currentAccount).getTopReactions();
            if (this.type == 3) {
                TLRPC.TL_messages_savedReactionsTags savedReactionTags = MessagesController.getInstance(this.currentAccount).getSavedReactionTags(0L);
                if (savedReactionTags != null) {
                    for (int i5 = 0; i5 < savedReactionTags.tags.size(); i5++) {
                        ReactionsLayoutInBubble.VisibleReaction fromTL3 = ReactionsLayoutInBubble.VisibleReaction.fromTL(savedReactionTags.tags.get(i5).reaction);
                        if (!hashSet.contains(fromTL3)) {
                            hashSet.add(fromTL3);
                            list.add(fromTL3);
                        }
                    }
                }
                for (int i6 = 0; i6 < savedReactions2.size(); i6++) {
                    ReactionsLayoutInBubble.VisibleReaction fromTL4 = ReactionsLayoutInBubble.VisibleReaction.fromTL(savedReactions2.get(i6));
                    if (!hashSet.contains(fromTL4)) {
                        hashSet.add(fromTL4);
                        list.add(fromTL4);
                    }
                }
            } else {
                for (int i7 = 0; i7 < savedReactions2.size(); i7++) {
                    ReactionsLayoutInBubble.VisibleReaction fromTL5 = ReactionsLayoutInBubble.VisibleReaction.fromTL(savedReactions2.get(i7));
                    if (!hashSet.contains(fromTL5) && (this.type == 3 || UserConfig.getInstance(this.currentAccount).isPremium() || fromTL5.documentId == 0)) {
                        hashSet.add(fromTL5);
                        list.add(fromTL5);
                    }
                }
            }
            if (this.type != 3 || UserConfig.getInstance(this.currentAccount).isPremium()) {
                ArrayList<TLRPC.Reaction> recentReactions = MediaDataController.getInstance(this.currentAccount).getRecentReactions();
                for (int i8 = 0; i8 < recentReactions.size(); i8++) {
                    ReactionsLayoutInBubble.VisibleReaction fromTL6 = ReactionsLayoutInBubble.VisibleReaction.fromTL(recentReactions.get(i8));
                    if (!hashSet.contains(fromTL6)) {
                        hashSet.add(fromTL6);
                        list.add(fromTL6);
                    }
                }
                List<TLRPC.TL_availableReaction> enabledReactionsList3 = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
                while (i2 < enabledReactionsList3.size()) {
                    ReactionsLayoutInBubble.VisibleReaction fromEmojicon2 = ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(enabledReactionsList3.get(i2));
                    if (!hashSet.contains(fromEmojicon2)) {
                        hashSet.add(fromEmojicon2);
                        list.add(fromEmojicon2);
                    }
                    i2++;
                }
            }
        }
    }

    private void filterReactions(List list) {
        HashSet hashSet = new HashSet();
        int i = 0;
        while (i < list.size()) {
            if (hashSet.contains(list.get(i))) {
                i--;
                list.remove(i);
            } else {
                hashSet.add((ReactionsLayoutInBubble.VisibleReaction) list.get(i));
            }
            i++;
        }
    }

    public static HashSet getInclusiveReactions(ArrayList arrayList) {
        TLRPC.TL_messageReactions tL_messageReactions;
        LongSparseArray longSparseArray = new LongSparseArray();
        HashSet hashSet = new HashSet();
        int i = 0;
        boolean z = true;
        while (i < arrayList.size()) {
            MessageObject messageObject = (MessageObject) arrayList.get(i);
            hashSet.clear();
            if (messageObject != null && (tL_messageReactions = messageObject.messageOwner.reactions) != null && tL_messageReactions.results != null) {
                for (int i2 = 0; i2 < messageObject.messageOwner.reactions.results.size(); i2++) {
                    if (messageObject.messageOwner.reactions.results.get(i2).chosen) {
                        ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(messageObject.messageOwner.reactions.results.get(i2).reaction);
                        if (z || longSparseArray.indexOfKey(fromTL.hash) >= 0) {
                            hashSet.add(Long.valueOf(fromTL.hash));
                            longSparseArray.put(fromTL.hash, fromTL);
                        }
                    }
                }
            }
            int i3 = 0;
            while (i3 < longSparseArray.size()) {
                if (!hashSet.contains(Long.valueOf(longSparseArray.keyAt(i3)))) {
                    longSparseArray.removeAt(i3);
                    i3--;
                }
                i3++;
            }
            i++;
            z = false;
        }
        HashSet hashSet2 = new HashSet();
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            if (longSparseArray.valueAt(i4) != null) {
                hashSet2.add((ReactionsLayoutInBubble.VisibleReaction) longSparseArray.valueAt(i4));
            }
        }
        return hashSet2;
    }

    private Paint getStarGradientPaint(RectF rectF, float f) {
        if (this.starSelectedGradientPaint == null) {
            this.starSelectedGradientPaint = new Paint(1);
        }
        if (this.starSelectedGradientMatrix == null) {
            this.starSelectedGradientMatrix = new Matrix();
        }
        if (this.starSelectedGradient == null) {
            int color = Theme.getColor(Theme.key_reactionStarSelector, this.resourcesProvider);
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(64.0f), 0.0f, new int[]{color, Theme.multAlpha(color, 0.0f)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
            this.starSelectedGradient = linearGradient;
            this.starSelectedGradientPaint.setShader(linearGradient);
        }
        this.starSelectedGradientMatrix.reset();
        this.starSelectedGradientMatrix.postTranslate(rectF.left, rectF.top);
        this.starSelectedGradient.setLocalMatrix(this.starSelectedGradientMatrix);
        this.starSelectedGradientPaint.setAlpha((int) (f * 255.0f));
        return this.starSelectedGradientPaint;
    }

    private void invalidateEmojis() {
        if (this.type != 4) {
            return;
        }
        invalidate();
        this.recyclerListView.invalidate();
        this.recyclerListView.invalidateViews();
        for (int i = 0; i < this.recyclerListView.getChildCount(); i++) {
            View childAt = this.recyclerListView.getChildAt(i);
            if (childAt instanceof ReactionHolderView) {
                ReactionHolderView reactionHolderView = (ReactionHolderView) childAt;
                reactionHolderView.enterImageView.invalidate();
                childAt = reactionHolderView.loopImageView;
            }
            childAt.invalidate();
        }
    }

    private void invalidateShaders() {
        int dp = AndroidUtilities.dp(24.0f);
        float height = getHeight() / 2.0f;
        int color = Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground);
        Paint paint = this.leftShadowPaint;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint.setShader(new LinearGradient(0.0f, height, dp, height, color, 0, tileMode));
        this.rightShadowPaint.setShader(new LinearGradient(getWidth(), height, getWidth() - dp, height, color, 0, tileMode));
        invalidate();
    }

    public /* synthetic */ void lambda$animatePullingBack$2(ValueAnimator valueAnimator) {
        this.pullingLeftOffset = ((Float) this.pullingDownBackAnimator.getAnimatedValue()).floatValue();
        FrameLayout frameLayout = this.customReactionsContainer;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        invalidate();
    }

    public /* synthetic */ void lambda$new$0(View view, int i) {
        ReactionsContainerDelegate reactionsContainerDelegate = this.delegate;
        if (reactionsContainerDelegate == null || !(view instanceof ReactionHolderView)) {
            return;
        }
        reactionsContainerDelegate.onReactionClicked(this, ((ReactionHolderView) view).currentReaction, false, false);
    }

    public /* synthetic */ boolean lambda$new$1(int i, View view, int i2) {
        ReactionsContainerDelegate reactionsContainerDelegate;
        if (i == 5 || (reactionsContainerDelegate = this.delegate) == null || !(view instanceof ReactionHolderView)) {
            return false;
        }
        reactionsContainerDelegate.onReactionClicked(this, ((ReactionHolderView) view).currentReaction, true, false);
        return true;
    }

    public /* synthetic */ void lambda$reset$6(View view) {
        if (view instanceof ReactionHolderView) {
            ReactionHolderView reactionHolderView = (ReactionHolderView) view;
            reactionHolderView.pressed = false;
            reactionHolderView.loopImageView.setAlpha(1.0f);
            if (!this.skipEnterAnimation) {
                reactionHolderView.resetAnimation();
                return;
            }
            reactionHolderView.loopImageView.setScaleX(reactionHolderView.enterScale * (reactionHolderView.selected ? 0.76f : 1.0f));
            reactionHolderView.loopImageView.setScaleY(reactionHolderView.enterScale * (reactionHolderView.selected ? 0.76f : 1.0f));
        }
    }

    public /* synthetic */ void lambda$showCustomEmojiReactionDialog$3() {
        this.reactionsWindow = null;
        invalidateLoopViews();
        ReactionsContainerDelegate reactionsContainerDelegate = this.delegate;
        if (reactionsContainerDelegate != null) {
            reactionsContainerDelegate.onEmojiWindowDismissed();
        }
    }

    public /* synthetic */ void lambda$updateSelected$4(boolean z, View view) {
        int childAdapterPosition = this.recyclerListView.getChildAdapterPosition(view);
        if (childAdapterPosition < 0 || childAdapterPosition >= this.items.size() || !(view instanceof ReactionHolderView)) {
            return;
        }
        ((ReactionHolderView) view).updateSelected(((InnerItem) this.items.get(childAdapterPosition)).reaction, z);
    }

    public void setChildScale(View view, float f) {
        if (view instanceof ReactionHolderView) {
            ((ReactionHolderView) view).sideScale = f;
            return;
        }
        view.setScaleX(f);
        view.setScaleY(f);
    }

    private void setVisibleReactionsList(List list, boolean z) {
        this.visibleReactionsList.clear();
        if (showCustomEmojiReaction()) {
            int dp = (AndroidUtilities.displaySize.x - AndroidUtilities.dp(36.0f)) / AndroidUtilities.dp(34.0f);
            if (dp > 7) {
                dp = 7;
            }
            if (dp < 1) {
                dp = 1;
            }
            int i = 0;
            while (i < Math.min(list.size(), dp)) {
                this.visibleReactionsList.add((ReactionsLayoutInBubble.VisibleReaction) list.get(i));
                i++;
            }
            if (i < list.size()) {
                this.nextRecentReaction.setReaction((ReactionsLayoutInBubble.VisibleReaction) list.get(i), -1);
            }
        } else {
            this.visibleReactionsList.addAll(list);
        }
        this.allReactionsIsDefault = true;
        for (int i2 = 0; i2 < this.visibleReactionsList.size(); i2++) {
            if (((ReactionsLayoutInBubble.VisibleReaction) this.visibleReactionsList.get(i2)).documentId != 0) {
                this.allReactionsIsDefault = false;
            }
        }
        this.allReactionsList.clear();
        this.allReactionsList.addAll(list);
        if ((((getLayoutParams().height - ((int) getTopOffset())) - getPaddingTop()) - getPaddingBottom()) * list.size() < AndroidUtilities.dp(200.0f)) {
            getLayoutParams().width = -2;
        }
        this.listAdapter.updateItems(z);
    }

    public void showCustomEmojiReactionDialog() {
        if (this.reactionsWindow != null) {
            return;
        }
        this.reactionsWindow = new CustomEmojiReactionsWindow(this.type, this.fragment, this.allReactionsList, this.selectedReactions, this, this.resourcesProvider);
        invalidateLoopViews();
        this.reactionsWindow.onDismissListener(new Runnable() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ReactionsContainerLayout.this.lambda$showCustomEmojiReactionDialog$3();
            }
        });
        onShownCustomEmojiReactionDialog();
    }

    public void showUnlockPremium(float f, float f2) {
        new PremiumFeatureBottomSheet(this.fragment, 4, true).show();
    }

    public boolean showUnlockPremiumButton() {
        return (this.premiumLockedReactions.isEmpty() || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()) ? false : true;
    }

    private void updateSelected(final boolean z) {
        AndroidUtilities.forEachViews((RecyclerView) this.recyclerListView, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda2
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ReactionsContainerLayout.this.lambda$updateSelected$4(z, (View) obj);
            }
        });
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.chatInfoDidLoad) {
            if (i == NotificationCenter.emojiLoaded) {
                invalidateEmojis();
                return;
            } else if (i == NotificationCenter.availableEffectsUpdate) {
                setMessage(this.messageObject, null, true);
                return;
            } else {
                return;
            }
        }
        TLRPC.ChatFull chatFull = (TLRPC.ChatFull) objArr[0];
        if (chatFull.id != this.waitingLoadingChatId || getVisibility() == 0 || (chatFull.available_reactions instanceof TLRPC.TL_chatReactionsNone)) {
            return;
        }
        setMessage(this.messageObject, null, true);
        setVisibility(0);
        startEnterAnimation(false);
    }

    public void dismissParent(boolean z) {
        CustomEmojiReactionsWindow customEmojiReactionsWindow = this.reactionsWindow;
        if (customEmojiReactionsWindow != null) {
            customEmojiReactionsWindow.dismiss(z);
            this.reactionsWindow = null;
        }
    }

    public void dismissWindow() {
        CustomEmojiReactionsWindow customEmojiReactionsWindow = this.reactionsWindow;
        if (customEmojiReactionsWindow != null) {
            customEmojiReactionsWindow.dismiss();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:236:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0507  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x0536  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        float max;
        TextView textView;
        float max2;
        float f;
        float f2;
        ReactionsLayoutInBubble.VisibleReaction visibleReaction;
        ReactionsLayoutInBubble.VisibleReaction visibleReaction2;
        float width;
        float f3;
        float f4;
        float max3;
        float f5;
        ChatScrimPopupContainerLayout chatScrimPopupContainerLayout;
        float f6;
        float f7;
        int i;
        float f8;
        boolean showCustomEmojiReaction;
        float f9;
        long min = Math.min(16L, System.currentTimeMillis() - this.lastUpdate);
        this.lastUpdate = System.currentTimeMillis();
        boolean z = this.isFlippedVertically;
        if (z) {
            float f10 = this.flipVerticalProgress;
            if (f10 != 1.0f) {
                max = Math.min(1.0f, f10 + (((float) min) / 220.0f));
                this.flipVerticalProgress = max;
                invalidate();
                textView = this.hintView;
                if (textView != null) {
                    textView.setTranslationY(-expandSize());
                }
                max2 = (Math.max(0.25f, Math.min(this.transitionProgress, 1.0f)) - 0.25f) / 0.75f;
                f = this.bigCircleRadius * max2;
                f2 = this.smallCircleRadius * max2;
                this.lastVisibleViewsTmp.clear();
                this.lastVisibleViewsTmp.addAll(this.lastVisibleViews);
                this.lastVisibleViews.clear();
                if (this.prepareAnimation) {
                    invalidate();
                }
                visibleReaction = this.pressedReaction;
                if (visibleReaction != null && this.type != 5) {
                    f9 = this.pressedProgress;
                    if (f9 != 1.0f) {
                        float longPressTimeout = f9 + (16.0f / (visibleReaction.isStar ? ViewConfiguration.getLongPressTimeout() : 1500.0f));
                        this.pressedProgress = longPressTimeout;
                        if (longPressTimeout >= 1.0f) {
                            this.pressedProgress = 1.0f;
                        }
                        invalidate();
                    }
                }
                visibleReaction2 = this.pressedReaction;
                if (visibleReaction2 == null && visibleReaction2.isStar) {
                    this.pressedViewScale = 1.0f;
                    this.otherViewsScale = 1.0f;
                } else {
                    float f11 = this.pressedProgress;
                    this.pressedViewScale = (f11 * 2.0f) + 1.0f;
                    this.otherViewsScale = 1.0f - (f11 * 0.15f);
                }
                int save = canvas.save();
                if (!LocaleController.isRTL || this.mirrorX) {
                    width = getWidth();
                    f3 = 0.125f;
                } else {
                    width = getWidth();
                    f3 = 0.875f;
                }
                float f12 = width * f3;
                f4 = this.transitionProgress;
                if (f4 != 1.0f) {
                    canvas.scale(f4, f4, f12, getHeight() / 2.0f);
                }
                if (!LocaleController.isRTL || this.mirrorX) {
                    max3 = Math.max(0.25f, this.transitionProgress);
                    f5 = 0.0f;
                } else {
                    f5 = 1.0f - Math.max(0.25f, this.transitionProgress);
                    max3 = 1.0f;
                }
                float pullingLeftProgress = getPullingLeftProgress();
                float expandSize = expandSize();
                chatScrimPopupContainerLayout = this.chatScrimPopupContainerLayout;
                if (chatScrimPopupContainerLayout != null) {
                    chatScrimPopupContainerLayout.setExpandSize(expandSize);
                }
                float width2 = (getWidth() - getPaddingRight()) * Math.min(1.0f, f5);
                float topOffset = getTopOffset();
                this.rect.set(getPaddingLeft() + width2, (getPaddingTop() + (this.recyclerListView.getMeasuredHeight() * (1.0f - this.otherViewsScale))) - expandSize, (getWidth() - getPaddingRight()) * max3, (getHeight() - getPaddingBottom()) + expandSize);
                this.radius = ((this.rect.height() - topOffset) - (expandSize * 2.0f)) / 2.0f;
                if (this.type != 1) {
                    this.shadow.setAlpha((int) (Utilities.clamp(1.0f - (this.customEmojiReactionsEnterProgress / 0.05f), 1.0f, 0.0f) * 255.0f));
                    Drawable drawable = this.shadow;
                    int width3 = getWidth() - getPaddingRight();
                    android.graphics.Rect rect = this.shadowPad;
                    int i2 = (int) expandSize;
                    drawable.setBounds((int) ((getPaddingLeft() + ((width3 + rect.right) * f5)) - rect.left), (getPaddingTop() - this.shadowPad.top) - i2, (int) (((getWidth() - getPaddingRight()) + this.shadowPad.right) * max3), (getHeight() - getPaddingBottom()) + this.shadowPad.bottom + i2);
                    this.shadow.draw(canvas);
                }
                canvas.restoreToCount(save);
                if (this.skipDraw) {
                    int save2 = canvas.save();
                    float f13 = this.transitionProgress;
                    if (f13 != 1.0f) {
                        canvas.scale(f13, f13, f12, getHeight() / 2.0f);
                    }
                    if (this.type == 1 || this.delegate.drawBackground()) {
                        f7 = width2;
                        f6 = f12;
                        i = 5;
                        this.delegate.drawRoundRect(canvas, this.rect, this.radius, getX(), getY(), NotificationCenter.messagePlayingSpeedChanged, false);
                    } else {
                        RectF rectF = this.rect;
                        float f14 = this.radius;
                        canvas.drawRoundRect(rectF, f14, f14, this.bgPaint);
                        f6 = f12;
                        f7 = width2;
                        i = 5;
                    }
                    if (this.hasStar) {
                        Iterator it = this.selectedReactions.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (((ReactionsLayoutInBubble.VisibleReaction) it.next()).isStar) {
                                    break;
                                }
                            } else {
                                RectF rectF2 = this.rect;
                                float f15 = this.radius;
                                canvas.drawRoundRect(rectF2, f15, f15, getStarGradientPaint(rectF2, Utilities.clamp01(1.0f - getPullingLeftProgress())));
                                break;
                            }
                        }
                    }
                    canvas.restoreToCount(save2);
                } else {
                    f6 = f12;
                    f7 = width2;
                    i = 5;
                }
                this.mPath.rewind();
                Path path = this.mPath;
                RectF rectF3 = this.rect;
                float f16 = this.radius;
                path.addRoundRect(rectF3, f16, f16, Path.Direction.CW);
                int save3 = canvas.save();
                f8 = this.transitionProgress;
                if (f8 != 1.0f) {
                    canvas.scale(f8, f8, f6, getHeight() / 2.0f);
                }
                if (this.transitionProgress == 0.0f && (getAlpha() == 1.0f || this.type == i)) {
                    int i3 = 0;
                    int i4 = 0;
                    for (int i5 = 0; i5 < this.recyclerListView.getChildCount(); i5++) {
                        View childAt = this.recyclerListView.getChildAt(i5);
                        if (this.transitionProgress != 1.0f && allowSmoothEnterTransition()) {
                            i4 = (int) (Math.abs(((childAt.getLeft() + (childAt.getMeasuredWidth() / 2.0f)) / this.recyclerListView.getMeasuredWidth()) - 0.8f) * 200.0f);
                        }
                        if (childAt instanceof ReactionHolderView) {
                            ReactionHolderView reactionHolderView = (ReactionHolderView) this.recyclerListView.getChildAt(i5);
                            checkPressedProgress(canvas, reactionHolderView);
                            if (childAt.getLeft() > i3) {
                                i3 = childAt.getLeft();
                            }
                            if (!this.skipEnterAnimation && (!reactionHolderView.hasEnterAnimation || reactionHolderView.enterImageView.getImageReceiver().getLottieAnimation() != null)) {
                                if (reactionHolderView.getX() + (reactionHolderView.getMeasuredWidth() / 2.0f) > 0.0f && reactionHolderView.getX() + (reactionHolderView.getMeasuredWidth() / 2.0f) < this.recyclerListView.getWidth()) {
                                    if (!this.lastVisibleViewsTmp.contains(reactionHolderView)) {
                                        reactionHolderView.play(i4);
                                        i4 += 30;
                                    }
                                    this.lastVisibleViews.add(reactionHolderView);
                                } else if (!reactionHolderView.isEnter) {
                                    reactionHolderView.resetAnimation();
                                }
                            }
                        } else {
                            if (childAt == this.premiumLockContainer) {
                                if (childAt.getX() + (childAt.getMeasuredWidth() / 2.0f) <= 0.0f || childAt.getX() + (childAt.getMeasuredWidth() / 2.0f) >= this.recyclerListView.getWidth()) {
                                    this.premiumLockIconView.resetAnimation();
                                } else {
                                    if (!this.lastVisibleViewsTmp.contains(childAt)) {
                                        if (this.transitionProgress != 1.0f) {
                                            this.premiumLockIconView.resetAnimation();
                                        }
                                        this.premiumLockIconView.play(i4);
                                        i4 += 30;
                                    }
                                    this.lastVisibleViews.add(childAt);
                                }
                            }
                            if (childAt == this.customReactionsContainer) {
                                if (childAt.getX() + (childAt.getMeasuredWidth() / 2.0f) <= 0.0f || childAt.getX() + (childAt.getMeasuredWidth() / 2.0f) >= this.recyclerListView.getWidth()) {
                                    this.customEmojiReactionsIconView.resetAnimation();
                                } else {
                                    if (!this.lastVisibleViewsTmp.contains(childAt)) {
                                        if (this.transitionProgress != 1.0f) {
                                            this.customEmojiReactionsIconView.resetAnimation();
                                        }
                                        this.customEmojiReactionsIconView.play(i4, LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS) || SharedConfig.getDevicePerformanceClass() >= 1);
                                        i4 += 30;
                                    }
                                    this.lastVisibleViews.add(childAt);
                                }
                            }
                            checkPressedProgressForOtherViews(childAt);
                        }
                    }
                    if (pullingLeftProgress > 0.0f) {
                        float pullingLeftProgress2 = getPullingLeftProgress();
                        int measuredWidth = this.nextRecentReaction.getMeasuredWidth() - AndroidUtilities.dp(2.0f);
                        float f17 = i3 + measuredWidth;
                        float clamp = Utilities.clamp(f17 / (getMeasuredWidth() - this.nextRecentReaction.getMeasuredWidth()), 1.0f, 0.0f) * pullingLeftProgress2 * measuredWidth;
                        if (this.nextRecentReaction.getTag() == null) {
                            this.nextRecentReaction.setTag(Float.valueOf(1.0f));
                            this.nextRecentReaction.resetAnimation();
                            this.nextRecentReaction.play(0);
                        }
                        float clamp2 = Utilities.clamp(pullingLeftProgress2, 1.0f, 0.0f);
                        this.nextRecentReaction.setScaleX(clamp2);
                        this.nextRecentReaction.setScaleY(clamp2);
                        int i6 = this.type;
                        this.nextRecentReaction.setTranslationX(((this.recyclerListView.getX() + f17) - clamp) + (-AndroidUtilities.dp((i6 == 1 || i6 == 2) ? 8.0f : 20.0f)));
                        if (this.nextRecentReaction.getVisibility() != 0) {
                            this.nextRecentReaction.setVisibility(0);
                        }
                    } else {
                        if (this.nextRecentReaction.getVisibility() != 8 && this.isHiddenNextReaction) {
                            this.nextRecentReaction.setVisibility(8);
                        }
                        if (this.nextRecentReaction.getTag() != null) {
                            this.nextRecentReaction.setTag(null);
                        }
                    }
                }
                if (!this.skipDraw && this.reactionsWindow != null) {
                    canvas.save();
                    drawBubbles(canvas, f, max2, f2, (int) (Utilities.clamp(1.0f - (this.customEmojiReactionsEnterProgress / 0.2f), 1.0f, 0.0f) * (1.0f - this.customEmojiReactionsEnterProgress) * 255.0f));
                    canvas.restore();
                    return;
                }
                showCustomEmojiReaction = showCustomEmojiReaction();
                if (!showCustomEmojiReaction) {
                    canvas.clipPath(this.mPath);
                }
                canvas.translate(((!LocaleController.isRTL || this.mirrorX) ? -1 : 1) * getWidth() * (1.0f - this.transitionProgress), 0.0f);
                this.recyclerListView.setTranslationX(-f7);
                super.dispatchDraw(canvas);
                if (!showCustomEmojiReaction) {
                    if (this.leftShadowPaint != null) {
                        this.leftShadowPaint.setAlpha((int) (Utilities.clamp(this.leftAlpha * this.transitionProgress, 1.0f, 0.0f) * 255.0f));
                        canvas.drawRect(this.rect, this.leftShadowPaint);
                    }
                    if (this.rightShadowPaint != null) {
                        this.rightShadowPaint.setAlpha((int) (Utilities.clamp(this.rightAlpha * this.transitionProgress, 1.0f, 0.0f) * 255.0f));
                        canvas.drawRect(this.rect, this.rightShadowPaint);
                    }
                }
                canvas.restoreToCount(save3);
                drawBubbles(canvas, f, max2, f2, NotificationCenter.messagePlayingSpeedChanged);
                invalidate();
            }
        }
        if (!z) {
            float f18 = this.flipVerticalProgress;
            if (f18 != 0.0f) {
                max = Math.max(0.0f, f18 - (((float) min) / 220.0f));
                this.flipVerticalProgress = max;
                invalidate();
            }
        }
        textView = this.hintView;
        if (textView != null) {
        }
        max2 = (Math.max(0.25f, Math.min(this.transitionProgress, 1.0f)) - 0.25f) / 0.75f;
        f = this.bigCircleRadius * max2;
        f2 = this.smallCircleRadius * max2;
        this.lastVisibleViewsTmp.clear();
        this.lastVisibleViewsTmp.addAll(this.lastVisibleViews);
        this.lastVisibleViews.clear();
        if (this.prepareAnimation) {
        }
        visibleReaction = this.pressedReaction;
        if (visibleReaction != null) {
            f9 = this.pressedProgress;
            if (f9 != 1.0f) {
            }
        }
        visibleReaction2 = this.pressedReaction;
        if (visibleReaction2 == null) {
        }
        float f112 = this.pressedProgress;
        this.pressedViewScale = (f112 * 2.0f) + 1.0f;
        this.otherViewsScale = 1.0f - (f112 * 0.15f);
        int save4 = canvas.save();
        if (LocaleController.isRTL) {
        }
        width = getWidth();
        f3 = 0.125f;
        float f122 = width * f3;
        f4 = this.transitionProgress;
        if (f4 != 1.0f) {
        }
        if (LocaleController.isRTL) {
        }
        max3 = Math.max(0.25f, this.transitionProgress);
        f5 = 0.0f;
        float pullingLeftProgress3 = getPullingLeftProgress();
        float expandSize2 = expandSize();
        chatScrimPopupContainerLayout = this.chatScrimPopupContainerLayout;
        if (chatScrimPopupContainerLayout != null) {
        }
        float width22 = (getWidth() - getPaddingRight()) * Math.min(1.0f, f5);
        float topOffset2 = getTopOffset();
        this.rect.set(getPaddingLeft() + width22, (getPaddingTop() + (this.recyclerListView.getMeasuredHeight() * (1.0f - this.otherViewsScale))) - expandSize2, (getWidth() - getPaddingRight()) * max3, (getHeight() - getPaddingBottom()) + expandSize2);
        this.radius = ((this.rect.height() - topOffset2) - (expandSize2 * 2.0f)) / 2.0f;
        if (this.type != 1) {
        }
        canvas.restoreToCount(save4);
        if (this.skipDraw) {
        }
        this.mPath.rewind();
        Path path2 = this.mPath;
        RectF rectF32 = this.rect;
        float f162 = this.radius;
        path2.addRoundRect(rectF32, f162, f162, Path.Direction.CW);
        int save32 = canvas.save();
        f8 = this.transitionProgress;
        if (f8 != 1.0f) {
        }
        if (this.transitionProgress == 0.0f) {
        }
        if (!this.skipDraw) {
        }
        showCustomEmojiReaction = showCustomEmojiReaction();
        if (!showCustomEmojiReaction) {
        }
        canvas.translate(((!LocaleController.isRTL || this.mirrorX) ? -1 : 1) * getWidth() * (1.0f - this.transitionProgress), 0.0f);
        this.recyclerListView.setTranslationX(-f7);
        super.dispatchDraw(canvas);
        if (!showCustomEmojiReaction) {
        }
        canvas.restoreToCount(save32);
        drawBubbles(canvas, f, max2, f2, NotificationCenter.messagePlayingSpeedChanged);
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (getAlpha() < 0.5f) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void drawBubbles(Canvas canvas) {
        float max = (Math.max(0.25f, Math.min(this.transitionProgress, 1.0f)) - 0.25f) / 0.75f;
        drawBubbles(canvas, this.bigCircleRadius * max, max, this.smallCircleRadius * max, this.type == 5 ? NotificationCenter.messagePlayingSpeedChanged : (int) (Utilities.clamp(this.customEmojiReactionsEnterProgress / 0.2f, 1.0f, 0.0f) * (1.0f - this.customEmojiReactionsEnterProgress) * 255.0f));
    }

    public float expandSize() {
        return (int) (getPullingLeftProgress() * AndroidUtilities.dp(6.0f));
    }

    public ReactionsContainerDelegate getDelegate() {
        return this.delegate;
    }

    public int getHintTextWidth() {
        return this.hintViewWidth;
    }

    public int getItemsCount() {
        return this.visibleReactionsList.size() + (showCustomEmojiReaction() ? 1 : 0) + 1;
    }

    public float getPullingLeftProgress() {
        return Utilities.clamp(this.pullingLeftOffset / AndroidUtilities.dp(42.0f), 2.0f, 0.0f);
    }

    public CustomEmojiReactionsWindow getReactionsWindow() {
        return this.reactionsWindow;
    }

    public String getSelectedEmoji() {
        TLRPC.Document findDocument;
        if (this.selectedReactions.isEmpty()) {
            return "";
        }
        ReactionsLayoutInBubble.VisibleReaction visibleReaction = (ReactionsLayoutInBubble.VisibleReaction) this.selectedReactions.iterator().next();
        long j = visibleReaction.documentId;
        String str = null;
        if (j != 0 && (findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, j)) != null) {
            str = MessageObject.findAnimatedEmojiEmoticon(findDocument, null);
        }
        if (TextUtils.isEmpty(str)) {
            str = visibleReaction.emojicon;
        }
        return TextUtils.isEmpty(str) ? "👍" : str;
    }

    public HashSet<ReactionsLayoutInBubble.VisibleReaction> getSelectedReactions() {
        return this.selectedReactions;
    }

    public float getTopOffset() {
        if (this.hasHint) {
            return ((FrameLayout.LayoutParams) this.recyclerListView.getLayoutParams()).topMargin;
        }
        return 0.0f;
    }

    public int getTotalWidth() {
        int itemsCount = getItemsCount();
        return !showCustomEmojiReaction() ? (AndroidUtilities.dp(36.0f) * itemsCount) + (AndroidUtilities.dp(2.0f) * (itemsCount - 1)) + AndroidUtilities.dp(16.0f) : (AndroidUtilities.dp(36.0f) * itemsCount) - AndroidUtilities.dp(4.0f);
    }

    public List<ReactionsLayoutInBubble.VisibleReaction> getVisibleReactionsList() {
        return this.visibleReactionsList;
    }

    public int getWindowType() {
        int i = this.type;
        if (i == 4) {
            return 13;
        }
        if (i == 3) {
            return 11;
        }
        if (i == 5) {
            return 14;
        }
        return this.showExpandableReactions ? 8 : 1;
    }

    public void invalidateLoopViews() {
        for (int i = 0; i < this.recyclerListView.getChildCount(); i++) {
            View childAt = this.recyclerListView.getChildAt(i);
            if (childAt instanceof ReactionHolderView) {
                ((ReactionHolderView) childAt).loopImageView.invalidate();
            }
        }
    }

    public boolean isFlippedVertically() {
        return this.isFlippedVertically;
    }

    public void measureHint() {
        TextView textView;
        if (this.hintMeasured || !this.hasHint || getMeasuredWidth() <= 0) {
            return;
        }
        float f = 16.0f;
        int min = Math.min(AndroidUtilities.dp(320.0f), getMeasuredWidth() - AndroidUtilities.dp(16.0f));
        StaticLayout staticLayout = new StaticLayout(this.hintView.getText(), this.hintView.getPaint(), min, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.hintViewHeight = staticLayout.getHeight();
        this.hintViewWidth = 0;
        for (int i = 0; i < staticLayout.getLineCount(); i++) {
            this.hintViewWidth = Math.max(this.hintViewWidth, (int) Math.ceil(staticLayout.getLineWidth(i)));
        }
        if (staticLayout.getLineCount() <= 1 || this.hintView.getText().toString().contains("\n")) {
            textView = this.hintView;
        } else {
            min = HintView2.cutInFancyHalf(this.hintView.getText(), this.hintView.getPaint());
            StaticLayout staticLayout2 = new StaticLayout(this.hintView.getText(), this.hintView.getPaint(), min, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.hintViewHeight = staticLayout2.getHeight();
            this.hintViewWidth = 0;
            for (int i2 = 0; i2 < staticLayout2.getLineCount(); i2++) {
                this.hintViewWidth = Math.max(this.hintViewWidth, (int) Math.ceil(staticLayout2.getLineWidth(i2)));
            }
            this.hintView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
            textView = this.hintView;
            f = 48.0f;
        }
        textView.setWidth(AndroidUtilities.dp(f) + min);
        int max = Math.max(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(7.0f) + this.hintViewHeight);
        int i3 = this.type;
        if (i3 == 1 || i3 == 2) {
            max = AndroidUtilities.dp(20.0f);
        } else {
            getLayoutParams().height = AndroidUtilities.dp(52.0f) + max + AndroidUtilities.dp(22.0f);
        }
        ((FrameLayout.LayoutParams) this.nextRecentReaction.getLayoutParams()).topMargin = max;
        ((FrameLayout.LayoutParams) this.recyclerListView.getLayoutParams()).topMargin = max;
        this.hintMeasured = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        if (this.type == 5) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.availableEffectsUpdate);
        }
    }

    public void onCustomEmojiWindowClosing() {
        ValueAnimator valueAnimator = this.pullingDownBackAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.pullingLeftOffset = 0.0f;
        FrameLayout frameLayout = this.customReactionsContainer;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        invalidate();
    }

    public void onCustomEmojiWindowOpened() {
        ValueAnimator valueAnimator = this.pullingDownBackAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.pullingLeftOffset = 0.0f;
        FrameLayout frameLayout = this.customReactionsContainer;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        if (this.type == 5) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.availableEffectsUpdate);
        }
    }

    public void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z) {
        ReactionsContainerDelegate reactionsContainerDelegate = this.delegate;
        if (reactionsContainerDelegate != null) {
            reactionsContainerDelegate.onReactionClicked(view, visibleReaction, z, true);
        }
        if (this.type == 5) {
            try {
                performHapticFeedback(3, 1);
            } catch (Exception unused) {
            }
        }
    }

    protected void onShownCustomEmojiReactionDialog() {
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        invalidateShaders();
    }

    public void prepareAnimation(boolean z) {
        this.prepareAnimation = z;
        invalidate();
    }

    public void reset() {
        this.isHiddenNextReaction = true;
        this.pressedReactionPosition = 0;
        this.pressedProgress = 0.0f;
        this.pullingLeftOffset = 0.0f;
        this.pressedReaction = null;
        this.clicked = false;
        AndroidUtilities.forEachViews((RecyclerView) this.recyclerListView, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.Components.ReactionsContainerLayout$$ExternalSyntheticLambda5
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ReactionsContainerLayout.this.lambda$reset$6((View) obj);
            }
        });
        this.lastVisibleViews.clear();
        this.recyclerListView.invalidate();
        FrameLayout frameLayout = this.customReactionsContainer;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        invalidate();
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        if (getAlpha() != f && f == 0.0f) {
            this.lastVisibleViews.clear();
            for (int i = 0; i < this.recyclerListView.getChildCount(); i++) {
                if (this.recyclerListView.getChildAt(i) instanceof ReactionHolderView) {
                    ((ReactionHolderView) this.recyclerListView.getChildAt(i)).resetAnimation();
                }
            }
        }
        super.setAlpha(f);
    }

    public void setBubbleOffset(float f) {
        this.bubblesOffset = f;
    }

    public void setChatScrimView(ChatScrimPopupContainerLayout chatScrimPopupContainerLayout) {
        this.chatScrimPopupContainerLayout = chatScrimPopupContainerLayout;
    }

    public void setCurrentAccount(int i) {
        this.currentAccount = i;
    }

    public void setCustomEmojiEnterProgress(float f) {
        this.customEmojiReactionsEnterProgress = f;
        ChatScrimPopupContainerLayout chatScrimPopupContainerLayout = this.chatScrimPopupContainerLayout;
        if (chatScrimPopupContainerLayout != null) {
            chatScrimPopupContainerLayout.setPopupAlpha(1.0f - f);
        }
        invalidate();
    }

    public void setCustomEmojiReactionsBackground(boolean z) {
        InternalImageView internalImageView;
        Drawable drawable;
        if (z) {
            internalImageView = this.customEmojiReactionsIconView;
            drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(28.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_listSelector), 40));
        } else {
            internalImageView = this.customEmojiReactionsIconView;
            drawable = null;
        }
        internalImageView.setBackground(drawable);
    }

    public void setDelegate(ReactionsContainerDelegate reactionsContainerDelegate) {
        this.delegate = reactionsContainerDelegate;
    }

    public void setFlippedVertically(boolean z) {
        this.isFlippedVertically = z;
        invalidate();
    }

    public void setFragment(BaseFragment baseFragment) {
        this.fragment = baseFragment;
    }

    public void setHint(CharSequence charSequence) {
        this.hasHint = true;
        if (this.hintView == null) {
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(getContext(), this.resourcesProvider);
            this.hintView = linksTextView;
            linksTextView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.hintView.setClickable(true);
            this.hintView.setTextSize(1, 12.0f);
            int i = this.type;
            if (i == 1 || i == 2 || i == 4) {
                this.hintView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
                this.hintView.setAlpha(0.5f);
            } else {
                this.hintView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, this.resourcesProvider));
            }
            this.hintView.setGravity(1);
            addView(this.hintView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 6.0f, 0.0f, 0.0f));
        }
        this.hintView.setText(charSequence);
        this.hintMeasured = false;
        ((FrameLayout.LayoutParams) this.nextRecentReaction.getLayoutParams()).topMargin = AndroidUtilities.dp(20.0f);
        ((FrameLayout.LayoutParams) this.recyclerListView.getLayoutParams()).topMargin = AndroidUtilities.dp(20.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:216:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x01cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessage(MessageObject messageObject, TLRPC.ChatFull chatFull, boolean z) {
        int i;
        TLRPC.TL_messageReactions tL_messageReactions;
        TLRPC.Message message;
        TLRPC.TL_messageReactions tL_messageReactions2;
        this.messageObject = messageObject;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (tL_messageReactions2 = message.reactions) == null) {
            i = 0;
        } else {
            Iterator<TLRPC.ReactionCount> it = tL_messageReactions2.results.iterator();
            i = 0;
            while (it.hasNext()) {
                if (!(it.next().reaction instanceof TLRPC.TL_reactionPaid)) {
                    i++;
                }
            }
        }
        this.hitLimit = this.type == 0 && this.messageObject != null && i >= MessagesController.getInstance(this.currentAccount).getChatMaxUniqReactions(this.messageObject.getDialogId());
        this.channelReactions = this.type == 0 && this.messageObject != null && ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.messageObject.getDialogId())));
        ArrayList arrayList = new ArrayList();
        if (messageObject != null && messageObject.isForwardedChannelPost() && (chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(-messageObject.getFromChatId())) == null) {
            this.waitingLoadingChatId = -messageObject.getFromChatId();
            MessagesController.getInstance(this.currentAccount).loadFullChat(-messageObject.getFromChatId(), 0, true);
            setVisibility(4);
            return;
        }
        this.hasStar = false;
        int i2 = this.type;
        if (i2 == 3) {
            this.allReactionsAvailable = UserConfig.getInstance(this.currentAccount).isPremium();
        } else {
            if (i2 != 5) {
                if (this.hitLimit) {
                    this.allReactionsAvailable = false;
                    if (chatFull != null && chatFull.paid_reactions_available) {
                        this.hasStar = true;
                        arrayList.add(ReactionsLayoutInBubble.VisibleReaction.asStar());
                    }
                    Iterator<TLRPC.ReactionCount> it2 = this.messageObject.messageOwner.reactions.results.iterator();
                    while (it2.hasNext()) {
                        arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(it2.next().reaction));
                    }
                } else if (chatFull != null) {
                    if (chatFull.paid_reactions_available) {
                        this.hasStar = true;
                        arrayList.add(ReactionsLayoutInBubble.VisibleReaction.asStar());
                    }
                    TLRPC.ChatReactions chatReactions = chatFull.available_reactions;
                    if (chatReactions instanceof TLRPC.TL_chatReactionsAll) {
                        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(chatFull.id));
                        if (chat == null || ChatObject.isChannelAndNotMegaGroup(chat)) {
                            this.allReactionsAvailable = false;
                        }
                    } else if (chatReactions instanceof TLRPC.TL_chatReactionsSome) {
                        Iterator<TLRPC.Reaction> it3 = ((TLRPC.TL_chatReactionsSome) chatReactions).reactions.iterator();
                        while (it3.hasNext()) {
                            TLRPC.Reaction next = it3.next();
                            for (TLRPC.TL_availableReaction tL_availableReaction : MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList()) {
                                if (((next instanceof TLRPC.TL_reactionEmoji) && tL_availableReaction.reaction.equals(((TLRPC.TL_reactionEmoji) next).emoticon)) || (next instanceof TLRPC.TL_reactionCustomEmoji)) {
                                    arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(next));
                                    break;
                                }
                                while (r6.hasNext()) {
                                }
                            }
                        }
                    }
                }
                filterReactions(arrayList);
                this.showExpandableReactions = this.hitLimit && ((!this.allReactionsAvailable && arrayList.size() > 16) || (this.allReactionsAvailable && !UserConfig.getInstance(this.currentAccount).isPremium() && MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()));
                if (this.type == 3 && !UserConfig.getInstance(this.currentAccount).isPremium()) {
                    this.showExpandableReactions = false;
                }
                if (this.type == 4) {
                    this.showExpandableReactions = true;
                }
                setVisibleReactionsList(arrayList, z);
                if (messageObject != null || (tL_messageReactions = messageObject.messageOwner.reactions) == null || tL_messageReactions.results == null) {
                    return;
                }
                for (int i3 = 0; i3 < messageObject.messageOwner.reactions.results.size(); i3++) {
                    if (messageObject.messageOwner.reactions.results.get(i3).chosen) {
                        this.selectedReactions.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(messageObject.messageOwner.reactions.results.get(i3).reaction));
                    }
                }
                return;
            }
            this.allReactionsAvailable = true;
        }
        fillRecentReactionsList(arrayList);
        filterReactions(arrayList);
        this.showExpandableReactions = this.hitLimit && ((!this.allReactionsAvailable && arrayList.size() > 16) || (this.allReactionsAvailable && !UserConfig.getInstance(this.currentAccount).isPremium() && MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()));
        if (this.type == 3) {
            this.showExpandableReactions = false;
        }
        if (this.type == 4) {
        }
        setVisibleReactionsList(arrayList, z);
        if (messageObject != null) {
        }
    }

    public void setMiniBubblesOffset(float f) {
        this.miniBubblesOffset = f;
    }

    public void setMirrorX(boolean z) {
        this.mirrorX = z;
        invalidate();
    }

    public void setOnSwitchedToLoopView(Runnable runnable) {
        this.onSwitchedToLoopView = runnable;
    }

    public void setParentLayout(ChatScrimPopupContainerLayout chatScrimPopupContainerLayout) {
        this.parentLayout = chatScrimPopupContainerLayout;
    }

    public void setPaused(boolean z, boolean z2) {
        if (this.paused == z) {
            return;
        }
        this.paused = z;
        this.pausedExceptSelected = z2;
        CustomEmojiReactionsWindow customEmojiReactionsWindow = this.reactionsWindow;
        if (customEmojiReactionsWindow == null || customEmojiReactionsWindow.getSelectAnimatedEmojiDialog() == null) {
            return;
        }
        this.reactionsWindow.getSelectAnimatedEmojiDialog().setPaused(this.paused, this.pausedExceptSelected);
    }

    public void setSelectedEmojis(ArrayList<String> arrayList) {
        this.selectedReactions.clear();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            ReactionsLayoutInBubble.VisibleReaction fromEmojicon = ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(it.next());
            if (fromEmojicon != null) {
                this.selectedReactions.add(fromEmojicon);
                this.alwaysSelectedReactions.add(fromEmojicon);
            }
        }
        updateSelected(true);
    }

    public void setSelectedReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        this.selectedReactions.clear();
        if (visibleReaction != null) {
            this.selectedReactions.add(visibleReaction);
        }
        this.listAdapter.notifyDataSetChanged();
    }

    public void setSelectedReactionAnimated(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        this.selectedReactions.clear();
        if (visibleReaction != null) {
            this.selectedReactions.add(visibleReaction);
        }
        updateSelected(true);
    }

    public void setSelectedReactionInclusive(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        this.selectedReactions.clear();
        if (visibleReaction != null) {
            this.selectedReactions.add(visibleReaction);
        }
        updateSelected(true);
    }

    public void setSelectedReactions(ArrayList<MessageObject> arrayList) {
        TLRPC.TL_messageReactions tL_messageReactions;
        this.selectedReactions.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = arrayList.get(i);
            if (messageObject != null && (tL_messageReactions = messageObject.messageOwner.reactions) != null && tL_messageReactions.results != null) {
                for (int i2 = 0; i2 < messageObject.messageOwner.reactions.results.size(); i2++) {
                    if (messageObject.messageOwner.reactions.results.get(i2).chosen) {
                        this.selectedReactions.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(messageObject.messageOwner.reactions.results.get(i2).reaction));
                    }
                }
            }
        }
        this.listAdapter.notifyDataSetChanged();
    }

    public void setSelectedReactionsInclusive(ArrayList<MessageObject> arrayList) {
        this.selectedReactions.clear();
        this.selectedReactions.addAll(getInclusiveReactions(arrayList));
        updateSelected(true);
    }

    public void setSkipDraw(boolean z) {
        if (this.skipDraw != z) {
            this.skipDraw = z;
            if (!z) {
                for (int i = 0; i < this.recyclerListView.getChildCount(); i++) {
                    if (this.recyclerListView.getChildAt(i) instanceof ReactionHolderView) {
                        ReactionHolderView reactionHolderView = (ReactionHolderView) this.recyclerListView.getChildAt(i);
                        if (reactionHolderView.hasEnterAnimation && (reactionHolderView.loopImageView.getImageReceiver().getLottieAnimation() != null || reactionHolderView.loopImageView.getImageReceiver().getAnimation() != null)) {
                            reactionHolderView.loopImageView.setVisibility(0);
                            reactionHolderView.enterImageView.setVisibility(4);
                            if (reactionHolderView.shouldSwitchToLoopView) {
                                reactionHolderView.switchedToLoopView = true;
                            }
                        }
                        reactionHolderView.invalidate();
                    }
                }
            }
            invalidate();
        }
    }

    public void setStoryItem(TL_stories.StoryItem storyItem) {
        TLRPC.Reaction reaction;
        this.selectedReactions.clear();
        if (storyItem != null && (reaction = storyItem.sent_reaction) != null) {
            this.selectedReactions.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(reaction));
        }
        this.listAdapter.notifyDataSetChanged();
    }

    public void setTop(boolean z) {
        this.isTop = z;
    }

    public void setTransitionProgress(float f) {
        this.transitionProgress = f;
        ChatScrimPopupContainerLayout chatScrimPopupContainerLayout = this.parentLayout;
        if (chatScrimPopupContainerLayout != null) {
            chatScrimPopupContainerLayout.setReactionsTransitionProgress((this.animatePopup && allowSmoothEnterTransition()) ? 1.0f : 1.0f);
        }
        invalidate();
    }

    @Override // android.view.View
    public void setTranslationX(float f) {
        if (f != getTranslationX()) {
            super.setTranslationX(f);
        }
    }

    public boolean showCustomEmojiReaction() {
        return this.allReactionsAvailable || this.showExpandableReactions;
    }

    public boolean showExpandableReactions() {
        return this.showExpandableReactions;
    }

    public void startEnterAnimation(boolean z) {
        ObjectAnimator duration;
        OvershootInterpolator overshootInterpolator;
        this.animatePopup = z;
        setTransitionProgress(0.0f);
        setAlpha(1.0f);
        this.notificationsLocker.lock();
        if (allowSmoothEnterTransition()) {
            duration = ObjectAnimator.ofFloat(this, TRANSITION_PROGRESS_VALUE, 0.0f, 1.0f).setDuration(250L);
            overshootInterpolator = new OvershootInterpolator(0.5f);
        } else {
            duration = ObjectAnimator.ofFloat(this, TRANSITION_PROGRESS_VALUE, 0.0f, 1.0f).setDuration(250L);
            overshootInterpolator = new OvershootInterpolator(0.5f);
        }
        duration.setInterpolator(overshootInterpolator);
        duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ReactionsContainerLayout.7
            {
                ReactionsContainerLayout.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ReactionsContainerLayout.this.notificationsLocker.unlock();
            }
        });
        duration.start();
    }
}
