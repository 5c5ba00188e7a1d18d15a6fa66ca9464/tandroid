package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.style.CharacterStyle;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.collection.LongSparseArray;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatMessageSharedResources;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.EmojiAnimationsOverlay;
import org.telegram.ui.MessageSendPreview;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.KeyboardNotifier;

/* loaded from: classes4.dex */
public class MessageSendPreview extends Dialog implements NotificationCenter.NotificationCenterDelegate {
    private final RecyclerView.Adapter adapter;
    public boolean allowRelayout;
    private ChatActivityEnterView.SendButton anchorSendButton;
    private Bitmap blurBitmap;
    private Paint blurBitmapPaint;
    private BitmapShader blurBitmapShader;
    private Matrix blurMatrix;
    private Paint buttonBgPaint;
    private Text buttonText;
    private RectF cameraRect;
    private Rect cellDelta;
    private final GridLayoutManagerFixed chatLayoutManager;
    private final RecyclerListView chatListView;
    private boolean closing;
    private final FrameLayout containerView;
    public final Context context;
    public final int currentAccount;
    private ChatMessageCell destCell;
    private float destClipBottom;
    private float destClipTop;
    private boolean dismissing;
    private Utilities.Callback2 drawEditText;
    private Utilities.Callback drawEditTextBackground;
    private ChatMessageCell dummyMessageCell;
    private EditTextCaption editText;
    private Paint editTextBackgroundPaint;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable effectDrawable;
    private long effectId;
    private EmojiAnimationsOverlay effectOverlay;
    private ReactionsContainerLayout effectSelector;
    private FrameLayout effectSelectorContainer;
    private float effectSelectorContainerY;
    private boolean effectSelectorShown;
    private final FrameLayout effectsView;
    private boolean firstOpenFrame;
    private boolean firstOpenFrame2;
    private boolean focusable;
    private VisiblePart fromPart;
    private final LongSparseArray groupedMessagesMap;
    private final Rect insets;
    private boolean keyboardVisible;
    private boolean layoutDone;
    private ChatMessageCell mainMessageCell;
    private int mainMessageCellId;
    private final ArrayList messageObjects;
    private int messageObjectsWidth;
    private ValueAnimator openAnimator;
    private boolean openInProgress;
    private float openProgress;
    private boolean opening;
    private View optionsView;
    public final Theme.ResourcesProvider resourcesProvider;
    private boolean scrolledToLast;
    private ChatActivityEnterView.SendButton sendButton;
    private final int[] sendButtonInitialPosition;
    private int sendButtonWidth;
    private boolean sent;
    private boolean sentEffect;
    private SpoilerEffect2 spoilerEffect2;
    private final FrameLayout windowView;

    class 16 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        final /* synthetic */ BaseFragment val$fragment;

        16(BaseFragment baseFragment) {
            this.val$fragment = baseFragment;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onReactionClicked$0(BaseFragment baseFragment) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            baseFragment.showAsSheet(new PremiumPreviewFragment("effect"), bottomSheetParams);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean drawBackground() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawBackground(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
            ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawRoundRect(this, canvas, rectF, f, f2, f3, i, z);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean needEnterText() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$needEnterText(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void onEmojiWindowDismissed() {
            ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$onEmojiWindowDismissed(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            boolean z3;
            boolean z4;
            if (visibleReaction == null || MessageSendPreview.this.effectSelector == null) {
                return;
            }
            boolean z5 = !UserConfig.getInstance(MessageSendPreview.this.currentAccount).isPremium() && visibleReaction.premium;
            if (MessageSendPreview.this.mainMessageCell != null) {
                MessageObject messageObject = MessageSendPreview.this.mainMessageCell.getMessageObject();
                if (messageObject == null) {
                    return;
                }
                TLRPC.Message message = messageObject.messageOwner;
                long j = message.effect;
                long j2 = visibleReaction.effectId;
                if (j2 == j) {
                    message.flags2 &= -5;
                    message.effect = 0L;
                    z4 = true;
                } else {
                    message.flags2 |= 4;
                    message.effect = j2;
                    z4 = false;
                }
                if (!z5) {
                    MessageSendPreview.this.mainMessageCell.setMessageObject(messageObject, MessageSendPreview.this.getValidGroupedMessage(messageObject), MessageSendPreview.this.messageObjects.size() > 1, false);
                    MessageSendPreview.this.effectSelector.setSelectedReactionAnimated(z4 ? null : visibleReaction);
                    if (MessageSendPreview.this.effectSelector.getReactionsWindow() != null && MessageSendPreview.this.effectSelector.getReactionsWindow().getSelectAnimatedEmojiDialog() != null) {
                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = MessageSendPreview.this.effectSelector.getReactionsWindow().getSelectAnimatedEmojiDialog();
                        if (z4) {
                            visibleReaction = null;
                        }
                        selectAnimatedEmojiDialog.setSelectedReaction(visibleReaction);
                        MessageSendPreview.this.effectSelector.getReactionsWindow().containerView.invalidate();
                    }
                }
                MessageSendPreview.this.effectOverlay.clear();
                if (!z4) {
                    MessageSendPreview.this.effectOverlay.showAnimationForCell(MessageSendPreview.this.mainMessageCell, 0, false, false);
                }
                if (z5) {
                    TLRPC.Message message2 = messageObject.messageOwner;
                    message2.effect = j;
                    if (j == 0) {
                        message2.flags2 &= -5;
                    }
                }
                if (MessageSendPreview.this.sendButton != null) {
                    MessageSendPreview.this.sendButton.setEffect(messageObject.messageOwner.effect);
                }
                MessageSendPreview.this.onEffectChange(messageObject.messageOwner.effect);
            } else if (MessageSendPreview.this.cameraRect != null) {
                if (visibleReaction.effectId == MessageSendPreview.this.effectId) {
                    MessageSendPreview.this.effectId = 0L;
                    z3 = true;
                } else {
                    MessageSendPreview.this.effectId = visibleReaction.effectId;
                    z3 = false;
                }
                if (MessageSendPreview.this.sendButton != null) {
                    MessageSendPreview.this.sendButton.setEffect(MessageSendPreview.this.effectId);
                }
                MessageSendPreview messageSendPreview = MessageSendPreview.this;
                messageSendPreview.onEffectChange(messageSendPreview.effectId);
                if (!z5) {
                    TLRPC.TL_availableEffect effect = MessageSendPreview.this.effectId == 0 ? null : MessagesController.getInstance(MessageSendPreview.this.currentAccount).getEffect(MessageSendPreview.this.effectId);
                    if (MessageSendPreview.this.effectDrawable != null) {
                        if (MessageSendPreview.this.effectId == 0 || effect == null) {
                            MessageSendPreview.this.effectDrawable.set((Drawable) null, true);
                        } else {
                            MessageSendPreview.this.effectDrawable.set((Drawable) Emoji.getEmojiDrawable(effect.emoticon), true);
                        }
                    }
                    MessageSendPreview.this.effectSelector.setSelectedReactionAnimated(z3 ? null : visibleReaction);
                    if (MessageSendPreview.this.effectSelector.getReactionsWindow() != null && MessageSendPreview.this.effectSelector.getReactionsWindow().getSelectAnimatedEmojiDialog() != null) {
                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = MessageSendPreview.this.effectSelector.getReactionsWindow().getSelectAnimatedEmojiDialog();
                        if (z3) {
                            visibleReaction = null;
                        }
                        selectAnimatedEmojiDialog2.setSelectedReaction(visibleReaction);
                        MessageSendPreview.this.effectSelector.getReactionsWindow().containerView.invalidate();
                    }
                }
                MessageSendPreview.this.effectOverlay.clear();
                if (!z3) {
                    TLRPC.TL_message tL_message = new TLRPC.TL_message();
                    tL_message.effect = MessageSendPreview.this.effectId;
                    if (MessageSendPreview.this.effectId != 0) {
                        tL_message.flags2 |= 4;
                    }
                    MessageSendPreview.this.effectOverlay.createDrawingObject(null, 0, null, new MessageObject(MessageSendPreview.this.currentAccount, tL_message, false, false), 0, false, false, 0.0f, 0.0f, true);
                }
            }
            if (z5 && this.val$fragment != null) {
                BulletinFactory of = BulletinFactory.of(MessageSendPreview.this.containerView, MessageSendPreview.this.resourcesProvider);
                int i = R.raw.star_premium_2;
                String string = LocaleController.getString(R.string.AnimatedEffectPremium);
                final BaseFragment baseFragment = this.val$fragment;
                of.createSimpleBulletin(i, AndroidUtilities.premiumText(string, new Runnable() { // from class: org.telegram.ui.MessageSendPreview$16$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessageSendPreview.16.lambda$onReactionClicked$0(BaseFragment.this);
                    }
                })).show();
            }
            MessageSendPreview.this.effectsView.invalidate();
        }
    }

    class 2 implements ViewTreeObserver.OnGlobalFocusChangeListener {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGlobalFocusChanged$0(View view) {
            AndroidUtilities.showKeyboard(view);
            if (MessageSendPreview.this.anchorSendButton != null) {
                MessageSendPreview.this.anchorSendButton.getLocationOnScreen(MessageSendPreview.this.sendButtonInitialPosition);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGlobalFocusChanged$1(final View view) {
            MessageSendPreview.this.makeFocusable();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSendPreview$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MessageSendPreview.2.this.lambda$onGlobalFocusChanged$0(view);
                }
            }, 100L);
        }

        @Override // android.view.ViewTreeObserver.OnGlobalFocusChangeListener
        public void onGlobalFocusChanged(View view, final View view2) {
            if (MessageSendPreview.this.focusable || !(view2 instanceof EditText)) {
                return;
            }
            AndroidUtilities.hideKeyboard(MessageSendPreview.this.editText);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSendPreview$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MessageSendPreview.2.this.lambda$onGlobalFocusChanged$1(view2);
                }
            }, 200L);
        }
    }

    class 3 extends SizeNotifierFrameLayout {
        private Paint backgroundPaint;
        int chatListViewTy;
        private GradientClip clip;
        final int[] destCellPos;
        private AnimatedFloat destCellY;
        final int[] pos;
        final int[] pos2;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        3(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.val$resourcesProvider = resourcesProvider;
            this.pos = new int[2];
            this.pos2 = new int[2];
            this.chatListViewTy = 0;
            this.destCellPos = new int[2];
            this.clip = new GradientClip();
            this.destCellY = new AnimatedFloat(0L, 100L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.backgroundPaint = new Paint(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$dispatchDraw$0(Canvas canvas, float f) {
            canvas.save();
            canvas.translate(MessageSendPreview.this.editText.getX(), MessageSendPreview.this.editText.getY() - MessageSendPreview.this.editText.getScrollY());
            float textSize = f / MessageSendPreview.this.editText.getTextSize();
            canvas.scale(textSize, textSize, MessageSendPreview.this.editText.getPaddingLeft(), MessageSendPreview.this.editText.getPaddingTop());
            MessageSendPreview.this.editText.draw(canvas);
            canvas.restore();
            return Boolean.TRUE;
        }

        /* JADX WARN: Code restructure failed: missing block: B:70:0x04f9, code lost:
        
            if ((r29.destCellPos[1] - r29.pos2[1]) > r2) goto L82;
         */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(final Canvas canvas) {
            char c;
            float f;
            float f2;
            float height;
            float f3;
            float f4;
            float f5;
            if (MessageSendPreview.this.openInProgress && MessageSendPreview.this.mainMessageCell != null && MessageSendPreview.this.mainMessageCell.getCurrentPosition() == null) {
                if (MessageSendPreview.this.firstOpenFrame) {
                    if (MessageSendPreview.this.editText != null) {
                        MessageSendPreview.this.editText.setAlpha(0.0f);
                    }
                    MessageSendPreview.this.firstOpenFrame = false;
                }
                boolean z = MessageSendPreview.this.mainMessageCell.getMessageObject() != null && MessageSendPreview.this.mainMessageCell.getMessageObject().type == 15;
                float imageX = z ? MessageSendPreview.this.mainMessageCell.getPhotoImage().getImageX() : r2.getTextX();
                float imageY = z ? MessageSendPreview.this.mainMessageCell.getPhotoImage().getImageY() : MessageSendPreview.this.mainMessageCell.getTextY();
                float x = MessageSendPreview.this.chatListView.getX() + MessageSendPreview.this.mainMessageCell.getX() + imageX;
                float y = MessageSendPreview.this.chatListView.getY() + MessageSendPreview.this.mainMessageCell.getY() + imageY;
                float textSize = (MessageSendPreview.this.mainMessageCell.getMessageObject() != null ? MessageSendPreview.this.mainMessageCell.getMessageObject().getTextPaint() : Theme.chat_msgTextPaint).getTextSize();
                if (MessageSendPreview.this.editText != null) {
                    MessageSendPreview.this.editText.getLocationOnScreen(this.pos);
                    float paddingLeft = this.pos[0] + MessageSendPreview.this.editText.getPaddingLeft();
                    float paddingTop = (this.pos[1] + MessageSendPreview.this.editText.getPaddingTop()) - MessageSendPreview.this.editText.getScrollY();
                    float textSize2 = MessageSendPreview.this.editText.getTextSize();
                    int i = this.pos[1];
                    float f6 = i;
                    float measuredHeight = i + MessageSendPreview.this.editText.getMeasuredHeight();
                    float lerp = AndroidUtilities.lerp(paddingLeft, x, MessageSendPreview.this.openProgress);
                    float lerp2 = AndroidUtilities.lerp(paddingTop, y, MessageSendPreview.this.openProgress);
                    f = lerp;
                    height = measuredHeight;
                    f3 = AndroidUtilities.lerp(textSize2, textSize, MessageSendPreview.this.openProgress);
                    f2 = lerp2;
                    f4 = f6;
                } else {
                    f = x;
                    f2 = y;
                    height = getHeight();
                    f3 = textSize;
                    f4 = 0.0f;
                }
                float f7 = MessageSendPreview.this.openProgress;
                if (MessageSendPreview.this.destCell != null) {
                    f4 = MessageSendPreview.this.destClipTop;
                }
                float lerp3 = AndroidUtilities.lerp(f4, MessageSendPreview.this.chatListView.getY() + (MessageSendPreview.this.chatListView.getHeight() * (1.0f - MessageSendPreview.this.chatListView.getScaleY())), MessageSendPreview.this.openProgress);
                float lerp4 = AndroidUtilities.lerp(0.0f, MessageSendPreview.this.chatListView.canScrollVertically(-1) ? 1.0f : 0.0f, MessageSendPreview.this.openProgress);
                if (MessageSendPreview.this.destCell != null) {
                    height = MessageSendPreview.this.destClipBottom;
                }
                float lerp5 = AndroidUtilities.lerp(height, MessageSendPreview.this.chatListView.getY() + MessageSendPreview.this.chatListView.getHeight(), MessageSendPreview.this.openProgress);
                float lerp6 = AndroidUtilities.lerp(0.0f, MessageSendPreview.this.chatListView.canScrollVertically(1) ? 1.0f : 0.0f, MessageSendPreview.this.openProgress);
                final float f8 = f3;
                float f9 = f2;
                canvas.saveLayerAlpha(0.0f, lerp3 + 1.0f, getWidth(), lerp5 - 1.0f, NotificationCenter.newLocationAvailable, 31);
                if (MessageSendPreview.this.editText != null) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((1.0f - f7) * 255.0f), 31);
                    canvas.translate(f, f9);
                    canvas.translate((-MessageSendPreview.this.editText.getX()) - MessageSendPreview.this.editText.getPaddingLeft(), ((-MessageSendPreview.this.editText.getY()) - MessageSendPreview.this.editText.getPaddingTop()) + MessageSendPreview.this.editText.getScrollY());
                    float alpha = MessageSendPreview.this.editText.getAlpha();
                    MessageSendPreview.this.editText.setAlpha(1.0f);
                    if (MessageSendPreview.this.openProgress >= 0.001f) {
                        f5 = alpha;
                    } else if (MessageSendPreview.this.drawEditTextBackground != null) {
                        canvas.save();
                        canvas.translate(0.0f, MessageSendPreview.this.editText.getY());
                        f5 = alpha;
                        canvas.saveLayerAlpha(MessageSendPreview.this.editText.getPaddingLeft() + MessageSendPreview.this.editText.getX(), 0.0f, ((MessageSendPreview.this.editText.getX() + MessageSendPreview.this.editText.getPaddingLeft()) + MessageSendPreview.this.editText.getWidth()) - MessageSendPreview.this.editText.getPaddingRight(), MessageSendPreview.this.editText.getHeight(), (int) ((1.0f - (MessageSendPreview.this.openProgress / 0.1f)) * 255.0f), 31);
                        MessageSendPreview.this.drawEditTextBackground.run(canvas);
                        canvas.restore();
                        canvas.restore();
                    } else {
                        f5 = alpha;
                        MessageSendPreview.this.editTextBackgroundPaint.setColor(Theme.getColor(Theme.key_chat_messagePanelBackground, this.val$resourcesProvider));
                        MessageSendPreview.this.editTextBackgroundPaint.setAlpha((int) (MessageSendPreview.this.editTextBackgroundPaint.getAlpha() * (1.0f - (MessageSendPreview.this.openProgress / 0.1f))));
                        canvas.drawRect(MessageSendPreview.this.editText.getPaddingLeft(), MessageSendPreview.this.editText.getY(), ((MessageSendPreview.this.editText.getX() + MessageSendPreview.this.editText.getPaddingLeft()) + MessageSendPreview.this.editText.getWidth()) - MessageSendPreview.this.editText.getPaddingRight(), MessageSendPreview.this.editText.getHeight() + MessageSendPreview.this.editText.getY(), MessageSendPreview.this.editTextBackgroundPaint);
                    }
                    if (MessageSendPreview.this.drawEditText != null) {
                        MessageSendPreview.this.drawEditText.run(canvas, new Utilities.Callback0Return() { // from class: org.telegram.ui.MessageSendPreview$3$$ExternalSyntheticLambda0
                            @Override // org.telegram.messenger.Utilities.Callback0Return
                            public final Object run() {
                                Boolean lambda$dispatchDraw$0;
                                lambda$dispatchDraw$0 = MessageSendPreview.3.this.lambda$dispatchDraw$0(canvas, f8);
                                return lambda$dispatchDraw$0;
                            }
                        });
                    }
                    MessageSendPreview.this.editText.setAlpha(f5);
                    canvas.restore();
                }
                MessageSendPreview.this.mainMessageCell.getTransitionParams().ignoreAlpha = true;
                if (MessageSendPreview.this.destCell == null) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (f7 * 255.0f), 31);
                    canvas.translate(f, f9);
                    canvas.translate(-imageX, -imageY);
                    float lerp7 = AndroidUtilities.lerp(1.0f, MessageSendPreview.this.chatListView.getScaleX(), MessageSendPreview.this.openProgress);
                    canvas.scale(lerp7, lerp7, (-MessageSendPreview.this.mainMessageCell.getX()) + MessageSendPreview.this.chatListView.getWidth(), (-MessageSendPreview.this.mainMessageCell.getY()) + MessageSendPreview.this.chatListView.getHeight());
                    float f10 = f8 / textSize;
                    canvas.scale(f10, f10, imageX, imageY);
                    if (MessageSendPreview.this.mainMessageCell.drawBackgroundInParent()) {
                        MessageSendPreview.this.mainMessageCell.drawBackgroundInternal(canvas, true);
                    }
                    MessageSendPreview.this.mainMessageCell.draw(canvas);
                } else {
                    MessageSendPreview.this.destCell.getLocationInWindow(this.pos2);
                    int translationY = MessageSendPreview.this.destCell.getParent() instanceof View ? (int) ((View) MessageSendPreview.this.destCell.getParent()).getTranslationY() : 0;
                    int i2 = this.chatListViewTy;
                    char c2 = i2 > translationY ? (char) 1 : (char) 1;
                    int[] iArr = this.destCellPos;
                    int[] iArr2 = this.pos2;
                    iArr[0] = iArr2[0];
                    iArr[c2] = iArr2[c2];
                    this.chatListViewTy = translationY;
                    float lerp8 = AndroidUtilities.lerp(MessageSendPreview.this.chatListView.getX() + MessageSendPreview.this.mainMessageCell.getX(), this.destCellPos[0], 1.0f - MessageSendPreview.this.openProgress);
                    float lerp9 = AndroidUtilities.lerp(MessageSendPreview.this.chatListView.getY() + MessageSendPreview.this.mainMessageCell.getY(), this.destCellPos[1], 1.0f - MessageSendPreview.this.openProgress);
                    canvas.save();
                    canvas.translate(lerp8, lerp9);
                    float lerp10 = AndroidUtilities.lerp(1.0f, MessageSendPreview.this.chatListView.getScaleX(), MessageSendPreview.this.openProgress);
                    canvas.scale(lerp10, lerp10, (-MessageSendPreview.this.mainMessageCell.getX()) + MessageSendPreview.this.chatListView.getWidth(), (-MessageSendPreview.this.mainMessageCell.getY()) + MessageSendPreview.this.chatListView.getHeight());
                    MessageSendPreview.this.mainMessageCell.getTransitionParams().animateChangeProgress = 1.0f - MessageSendPreview.this.openProgress;
                    MessageSendPreview.this.mainMessageCell.getTransitionParams().deltaLeft = MessageSendPreview.this.cellDelta.left * MessageSendPreview.this.openProgress;
                    MessageSendPreview.this.mainMessageCell.getTransitionParams().deltaTop = MessageSendPreview.this.cellDelta.top * MessageSendPreview.this.openProgress;
                    MessageSendPreview.this.mainMessageCell.getTransitionParams().deltaRight = MessageSendPreview.this.cellDelta.right * MessageSendPreview.this.openProgress;
                    MessageSendPreview.this.mainMessageCell.getTransitionParams().deltaBottom = MessageSendPreview.this.cellDelta.bottom * MessageSendPreview.this.openProgress;
                    MessageSendPreview.this.mainMessageCell.setTimeAlpha(1.0f - MessageSendPreview.this.openProgress);
                    if (MessageSendPreview.this.mainMessageCell.drawBackgroundInParent()) {
                        canvas.saveLayerAlpha(0.0f, 0.0f, MessageSendPreview.this.destCell.getWidth(), MessageSendPreview.this.destCell.getHeight(), (int) (MessageSendPreview.this.openProgress * 255.0f), 31);
                        MessageSendPreview.this.mainMessageCell.drawBackgroundInternal(canvas, true);
                        canvas.restore();
                        canvas.saveLayerAlpha(0.0f, 0.0f, MessageSendPreview.this.destCell.getWidth(), MessageSendPreview.this.destCell.getHeight(), (int) ((1.0f - MessageSendPreview.this.openProgress) * 255.0f), 31);
                        MessageSendPreview.this.destCell.drawBackgroundInternal(canvas, true);
                        canvas.restore();
                    }
                    MessageSendPreview.this.mainMessageCell.draw(canvas);
                    if (MessageSendPreview.this.mainMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                        MessageSendPreview.this.mainMessageCell.drawNamesLayout(canvas, 1.0f);
                        MessageSendPreview.this.mainMessageCell.drawTime(canvas, 1.0f - MessageSendPreview.this.openProgress, true);
                    }
                }
                canvas.restore();
                canvas.save();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, lerp3, getWidth(), AndroidUtilities.dp(14.0f) + lerp3);
                this.clip.draw(canvas, rectF, true, lerp4);
                rectF.set(0.0f, lerp5 - AndroidUtilities.dp(14.0f), getWidth(), lerp5);
                this.clip.draw(canvas, rectF, false, lerp6);
                canvas.restore();
                canvas.restore();
            }
            if (MessageSendPreview.this.openInProgress) {
                if (MessageSendPreview.this.firstOpenFrame2) {
                    if (MessageSendPreview.this.anchorSendButton != null) {
                        MessageSendPreview.this.anchorSendButton.setAlpha(0.0f);
                    }
                    c = 0;
                    MessageSendPreview.this.firstOpenFrame2 = false;
                } else {
                    c = 0;
                }
                canvas.save();
                canvas.translate(AndroidUtilities.lerp(MessageSendPreview.this.sendButtonInitialPosition[c], MessageSendPreview.this.sendButton.getX(), MessageSendPreview.this.openProgress), AndroidUtilities.lerp(MessageSendPreview.this.sendButtonInitialPosition[1], MessageSendPreview.this.sendButton.getY(), MessageSendPreview.this.openProgress));
                if (MessageSendPreview.this.closing && MessageSendPreview.this.sent) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, MessageSendPreview.this.sendButton.getWidth(), MessageSendPreview.this.sendButton.getHeight(), (int) (MessageSendPreview.this.openProgress * 255.0f), 31);
                }
                MessageSendPreview.this.sendButton.draw(canvas);
                if (MessageSendPreview.this.closing && MessageSendPreview.this.sent) {
                    canvas.restore();
                }
                canvas.restore();
            }
            super.dispatchDraw(canvas);
            if (MessageSendPreview.this.cameraRect != null) {
                if (MessageSendPreview.this.effectDrawable == null) {
                    MessageSendPreview.this.effectDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(24.0f), 23);
                }
                Rect rect = AndroidUtilities.rectTmp2;
                rect.set((int) ((MessageSendPreview.this.cameraRect.right - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(24.0f)), (int) ((MessageSendPreview.this.cameraRect.bottom - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(24.0f)), (int) (MessageSendPreview.this.cameraRect.right - AndroidUtilities.dp(12.0f)), (int) (MessageSendPreview.this.cameraRect.bottom - AndroidUtilities.dp(12.0f)));
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(rect);
                rectF2.inset(-AndroidUtilities.dp(12.0f), -AndroidUtilities.dp(6.0f));
                float height2 = rectF2.height() / 2.0f;
                this.backgroundPaint.setColor(503316480);
                this.backgroundPaint.setAlpha((int) (MessageSendPreview.this.effectDrawable.isNotEmpty() * 30.0f * MessageSendPreview.this.openProgress));
                canvas.drawRoundRect(rectF2, height2, height2, this.backgroundPaint);
                MessageSendPreview.this.effectDrawable.setBounds(rect);
                MessageSendPreview.this.effectDrawable.setAlpha((int) (MessageSendPreview.this.openProgress * 255.0f));
                MessageSendPreview.this.effectDrawable.draw(canvas);
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (MessageSendPreview.this.openInProgress) {
                if (view == MessageSendPreview.this.sendButton) {
                    return false;
                }
                if (view == MessageSendPreview.this.mainMessageCell && MessageSendPreview.this.mainMessageCell != null && MessageSendPreview.this.mainMessageCell.getCurrentPosition() == null) {
                    return false;
                }
            }
            return super.drawChild(canvas, view, j);
        }
    }

    class 7 extends ChatListItemAnimator {
        Runnable finishRunnable;

        7(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider) {
            super(chatActivity, recyclerListView, resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$endAnimations$1() {
            this.finishRunnable = null;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("chatItemAnimator enable notifications");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAllAnimationsDone$0() {
            this.finishRunnable = null;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("chatItemAnimator enable notifications");
            }
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        public void checkIsRunning() {
        }

        @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void endAnimations() {
            super.endAnimations();
            Runnable runnable = this.finishRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.MessageSendPreview$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MessageSendPreview.7.this.lambda$endAnimations$1();
                }
            };
            this.finishRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2);
        }

        @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator
        protected void onAllAnimationsDone() {
            super.onAllAnimationsDone();
            Runnable runnable = this.finishRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.finishRunnable = null;
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.MessageSendPreview$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MessageSendPreview.7.this.lambda$onAllAnimationsDone$0();
                }
            };
            this.finishRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2);
        }

        @Override // androidx.recyclerview.widget.ChatListItemAnimator
        public void onAnimationStart() {
            Runnable runnable = this.finishRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.finishRunnable = null;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("chatItemAnimator disable notifications");
            }
        }
    }

    private class MessageCell extends ChatMessageCell {
        public int bottom;
        private int pastId;
        public int top;

        public MessageCell(Context context, int i, boolean z, ChatMessageSharedResources chatMessageSharedResources, Theme.ResourcesProvider resourcesProvider) {
            super(context, i, z, chatMessageSharedResources, resourcesProvider);
            this.top = ConnectionsManager.DEFAULT_DATACENTER_ID;
            this.bottom = ConnectionsManager.DEFAULT_DATACENTER_ID;
            this.pastId = -1;
        }

        @Override // android.view.View
        public boolean isPressed() {
            return false;
        }

        @Override // org.telegram.ui.Cells.ChatMessageCell
        protected SpoilerEffect2 makeSpoilerEffect() {
            return SpoilerEffect2.getInstance(1, this, MessageSendPreview.this.windowView);
        }

        @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (!this.transitionParams.animateBackgroundBoundsInner || i2 == 0 || this.top == Integer.MAX_VALUE || i4 == 0 || this.bottom == Integer.MAX_VALUE) {
                return;
            }
            if (this.pastId == (getMessageObject() == null ? 0 : getMessageObject().getId())) {
                if (!MessageSendPreview.this.scrolledToLast) {
                    setTranslationY(-(i2 - this.top));
                    animate().translationY(0.0f).setDuration(320L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                }
                this.top = getTop();
                this.bottom = getBottom();
                this.pastId = getMessageObject() != null ? getMessageObject().getId() : 0;
            }
        }
    }

    private static class VisiblePart {
        private int blurredViewBottomOffset;
        private int blurredViewTopOffset;
        private int childPosition;
        public int parentHeight;
        public int parentWidth;
        private int visibleHeight;
        private int visibleParent;
        private float visibleParentOffset;
        private float visibleTop;

        private VisiblePart() {
        }

        public static VisiblePart of(ChatMessageCell chatMessageCell) {
            VisiblePart visiblePart = new VisiblePart();
            visiblePart.childPosition = chatMessageCell.childPosition;
            visiblePart.visibleHeight = chatMessageCell.visibleHeight;
            visiblePart.visibleParent = chatMessageCell.visibleParent;
            visiblePart.parentWidth = chatMessageCell.parentWidth;
            visiblePart.parentHeight = chatMessageCell.parentHeight;
            visiblePart.visibleTop = chatMessageCell.visibleTop;
            visiblePart.visibleParentOffset = chatMessageCell.visibleParentOffset;
            visiblePart.blurredViewTopOffset = chatMessageCell.blurredViewTopOffset;
            visiblePart.blurredViewBottomOffset = chatMessageCell.blurredViewBottomOffset;
            return visiblePart;
        }
    }

    public MessageSendPreview(final Context context, final Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.insets = new Rect();
        this.messageObjects = new ArrayList();
        this.groupedMessagesMap = new LongSparseArray();
        this.editTextBackgroundPaint = new Paint(1);
        this.sendButtonInitialPosition = new int[2];
        this.dismissing = false;
        this.cellDelta = new Rect();
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.MessageSendPreview.1
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (MessageSendPreview.this.openProgress > 0.0f && MessageSendPreview.this.blurBitmapPaint != null) {
                    MessageSendPreview.this.blurMatrix.reset();
                    float width = getWidth() / MessageSendPreview.this.blurBitmap.getWidth();
                    MessageSendPreview.this.blurMatrix.postScale(width, width);
                    MessageSendPreview.this.blurBitmapShader.setLocalMatrix(MessageSendPreview.this.blurMatrix);
                    MessageSendPreview.this.blurBitmapPaint.setAlpha((int) (MessageSendPreview.this.openProgress * 255.0f));
                    canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), MessageSendPreview.this.blurBitmapPaint);
                }
                super.dispatchDraw(canvas);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
                if (keyEvent == null || keyEvent.getKeyCode() != 4 || keyEvent.getAction() != 1) {
                    return super.dispatchKeyEventPreIme(keyEvent);
                }
                MessageSendPreview.this.onBackPressed();
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                if (!MessageSendPreview.this.layoutDone || MessageSendPreview.this.allowRelayout) {
                    MessageSendPreview.this.layout();
                    MessageSendPreview.this.layoutDone = true;
                }
            }
        };
        this.windowView = frameLayout;
        this.spoilerEffect2 = SpoilerEffect2.getInstance(1, frameLayout, frameLayout);
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MessageSendPreview.this.lambda$new$0(view);
            }
        });
        frameLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new 2());
        3 r13 = new 3(context, resourcesProvider);
        this.containerView = r13;
        r13.setClipToPadding(false);
        frameLayout.addView(r13, LayoutHelper.createFrame(-1, -1, 119));
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setFitsSystemWindows(true);
            frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.MessageSendPreview.4
                @Override // android.view.View.OnApplyWindowInsetsListener
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    int systemWindowInsetLeft;
                    int systemWindowInsetTop;
                    int systemWindowInsetRight;
                    int systemWindowInsetBottom;
                    WindowInsets consumeSystemWindowInsets;
                    WindowInsets windowInsets2;
                    Insets insets;
                    int i2;
                    int i3;
                    int i4;
                    int i5;
                    int i6 = Build.VERSION.SDK_INT;
                    if (i6 >= 30) {
                        insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.systemBars());
                        Rect rect = MessageSendPreview.this.insets;
                        i2 = insets.left;
                        i3 = insets.top;
                        i4 = insets.right;
                        i5 = insets.bottom;
                        rect.set(i2, i3, i4, i5);
                    } else {
                        Rect rect2 = MessageSendPreview.this.insets;
                        systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
                        systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                        systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
                        systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                        rect2.set(systemWindowInsetLeft, systemWindowInsetTop, systemWindowInsetRight, systemWindowInsetBottom);
                    }
                    MessageSendPreview.this.containerView.setPadding(MessageSendPreview.this.insets.left, MessageSendPreview.this.insets.top, MessageSendPreview.this.insets.right, MessageSendPreview.this.insets.bottom);
                    MessageSendPreview.this.windowView.requestLayout();
                    if (i6 >= 30) {
                        windowInsets2 = WindowInsets.CONSUMED;
                        return windowInsets2;
                    }
                    consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
                    return consumeSystemWindowInsets;
                }
            });
        }
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.MessageSendPreview.5
            private final AnimatedFloat bottom;
            private final GradientClip clip;
            private final ArrayList drawingGroups = new ArrayList(10);
            private final AnimatedFloat top;

            {
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                this.top = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
                this.bottom = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
                this.clip = new GradientClip();
            }

            /* JADX WARN: Type inference failed for: r3v0 */
            /* JADX WARN: Type inference failed for: r3v1, types: [boolean, int] */
            /* JADX WARN: Type inference failed for: r3v9 */
            private void drawChatBackgroundElements(Canvas canvas) {
                boolean z;
                int i2;
                MessageObject.GroupedMessages currentMessagesGroup;
                MessageObject.GroupedMessages currentMessagesGroup2;
                int childCount = getChildCount();
                ?? r3 = 0;
                MessageObject.GroupedMessages groupedMessages = null;
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt = getChildAt(i3);
                    if ((childAt instanceof ChatMessageCell) && ((currentMessagesGroup2 = ((ChatMessageCell) childAt).getCurrentMessagesGroup()) == null || currentMessagesGroup2 != groupedMessages)) {
                        groupedMessages = currentMessagesGroup2;
                    }
                }
                int i4 = 0;
                while (i4 < 3) {
                    this.drawingGroups.clear();
                    if (i4 != 2 || MessageSendPreview.this.chatListView.isFastScrollAnimationRunning()) {
                        int i5 = 0;
                        while (true) {
                            z = true;
                            if (i5 >= childCount) {
                                break;
                            }
                            View childAt2 = MessageSendPreview.this.chatListView.getChildAt(i5);
                            if (childAt2 instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell = (ChatMessageCell) childAt2;
                                if (childAt2.getY() <= MessageSendPreview.this.chatListView.getHeight() && childAt2.getY() + childAt2.getHeight() >= 0.0f && (currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup()) != null && ((i4 != 0 || currentMessagesGroup.messages.size() != 1) && ((i4 != 1 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i4 != 0 || !chatMessageCell.getMessageObject().deleted) && ((i4 != 1 || chatMessageCell.getMessageObject().deleted) && ((i4 != 2 || chatMessageCell.willRemovedAfterAnimation()) && (i4 == 2 || !chatMessageCell.willRemovedAfterAnimation()))))))) {
                                    if (!this.drawingGroups.contains(currentMessagesGroup)) {
                                        MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                                        transitionParams.left = r3;
                                        transitionParams.top = r3;
                                        transitionParams.right = r3;
                                        transitionParams.bottom = r3;
                                        transitionParams.pinnedBotton = r3;
                                        transitionParams.pinnedTop = r3;
                                        transitionParams.cell = chatMessageCell;
                                        this.drawingGroups.add(currentMessagesGroup);
                                    }
                                    currentMessagesGroup.transitionParams.pinnedTop = chatMessageCell.isPinnedTop();
                                    currentMessagesGroup.transitionParams.pinnedBotton = chatMessageCell.isPinnedBottom();
                                    int x = (int) (chatMessageCell.getX() + chatMessageCell.getBackgroundDrawableLeft());
                                    int x2 = (int) (chatMessageCell.getX() + chatMessageCell.getBackgroundDrawableRight());
                                    int y = (int) (chatMessageCell.getY() + chatMessageCell.getBackgroundDrawableTop());
                                    int y2 = (int) (chatMessageCell.getY() + chatMessageCell.getBackgroundDrawableBottom());
                                    if ((chatMessageCell.getCurrentPosition().flags & 4) == 0) {
                                        y -= AndroidUtilities.dp(10.0f);
                                    }
                                    if ((chatMessageCell.getCurrentPosition().flags & 8) == 0) {
                                        y2 += AndroidUtilities.dp(10.0f);
                                    }
                                    if (chatMessageCell.willRemovedAfterAnimation()) {
                                        currentMessagesGroup.transitionParams.cell = chatMessageCell;
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                    int i6 = transitionParams2.top;
                                    if (i6 == 0 || y < i6) {
                                        transitionParams2.top = y;
                                    }
                                    int i7 = transitionParams2.bottom;
                                    if (i7 == 0 || y2 > i7) {
                                        transitionParams2.bottom = y2;
                                    }
                                    int i8 = transitionParams2.left;
                                    if (i8 == 0 || x < i8) {
                                        transitionParams2.left = x;
                                    }
                                    int i9 = transitionParams2.right;
                                    if (i9 == 0 || x2 > i9) {
                                        transitionParams2.right = x2;
                                    }
                                }
                            }
                            i5++;
                        }
                        int i10 = 0;
                        while (i10 < this.drawingGroups.size()) {
                            MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) this.drawingGroups.get(i10);
                            if (groupedMessages2 == null) {
                                i2 = i4;
                            } else {
                                float nonAnimationTranslationX = groupedMessages2.transitionParams.cell.getNonAnimationTranslationX(z);
                                MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages2.transitionParams;
                                float f = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                                float f2 = transitionParams3.top + transitionParams3.offsetTop;
                                float f3 = transitionParams3.right + nonAnimationTranslationX + transitionParams3.offsetRight;
                                float f4 = transitionParams3.bottom + transitionParams3.offsetBottom;
                                if (f2 < (-AndroidUtilities.dp(20.0f))) {
                                    f2 = -AndroidUtilities.dp(20.0f);
                                }
                                if (f4 > MessageSendPreview.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) {
                                    f4 = MessageSendPreview.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f);
                                }
                                boolean z2 = (groupedMessages2.transitionParams.cell.getScaleX() == 1.0f && groupedMessages2.transitionParams.cell.getScaleY() == 1.0f) ? false : true;
                                if (z2) {
                                    canvas.save();
                                    canvas.scale(groupedMessages2.transitionParams.cell.getScaleX(), groupedMessages2.transitionParams.cell.getScaleY(), f + ((f3 - f) / 2.0f), f2 + ((f4 - f2) / 2.0f));
                                }
                                MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages2.transitionParams;
                                i2 = i4;
                                transitionParams4.cell.drawBackground(canvas, (int) f, (int) f2, (int) f3, (int) f4, transitionParams4.pinnedTop, transitionParams4.pinnedBotton, false, 0);
                                MessageObject.GroupedMessages.TransitionParams transitionParams5 = groupedMessages2.transitionParams;
                                transitionParams5.cell = null;
                                transitionParams5.drawCaptionLayout = groupedMessages2.hasCaption;
                                if (z2) {
                                    canvas.restore();
                                    for (int i11 = 0; i11 < childCount; i11++) {
                                        View childAt3 = MessageSendPreview.this.chatListView.getChildAt(i11);
                                        if (childAt3 instanceof ChatMessageCell) {
                                            ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt3;
                                            if (chatMessageCell2.getCurrentMessagesGroup() == groupedMessages2) {
                                                int left = chatMessageCell2.getLeft();
                                                int top = chatMessageCell2.getTop();
                                                childAt3.setPivotX((f - left) + ((f3 - f) / 2.0f));
                                                childAt3.setPivotY((f2 - top) + ((f4 - f2) / 2.0f));
                                            }
                                        }
                                    }
                                }
                            }
                            i10++;
                            i4 = i2;
                            z = true;
                        }
                    }
                    i4++;
                    r3 = 0;
                }
            }

            private void drawChatForegroundElements(Canvas canvas) {
                MessageObject.GroupedMessages currentMessagesGroup;
                ChatMessageCell chatMessageCell;
                MessageObject.GroupedMessages currentMessagesGroup2;
                int childCount = getChildCount();
                MessageObject.GroupedMessages groupedMessages = null;
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = getChildAt(i2);
                    if ((childAt instanceof ChatMessageCell) && ((currentMessagesGroup2 = (chatMessageCell = (ChatMessageCell) childAt).getCurrentMessagesGroup()) == null || currentMessagesGroup2 != groupedMessages)) {
                        if (currentMessagesGroup2 == null) {
                            MessageSendPreview.this.drawStarsPrice(canvas, chatMessageCell.getBoundsLeft(), chatMessageCell.getY(), chatMessageCell.getBoundsRight(), chatMessageCell.getY() + chatMessageCell.getHeight());
                        }
                        groupedMessages = currentMessagesGroup2;
                    }
                }
                for (int i3 = 0; i3 < 3; i3++) {
                    this.drawingGroups.clear();
                    if (i3 != 2 || MessageSendPreview.this.chatListView.isFastScrollAnimationRunning()) {
                        for (int i4 = 0; i4 < childCount; i4++) {
                            View childAt2 = MessageSendPreview.this.chatListView.getChildAt(i4);
                            if (childAt2 instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt2;
                                if (childAt2.getY() <= MessageSendPreview.this.chatListView.getHeight() && childAt2.getY() + childAt2.getHeight() >= 0.0f && (currentMessagesGroup = chatMessageCell2.getCurrentMessagesGroup()) != null && ((i3 != 0 || currentMessagesGroup.messages.size() != 1) && ((i3 != 1 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i3 != 0 || !chatMessageCell2.getMessageObject().deleted) && ((i3 != 1 || chatMessageCell2.getMessageObject().deleted) && ((i3 != 2 || chatMessageCell2.willRemovedAfterAnimation()) && (i3 == 2 || !chatMessageCell2.willRemovedAfterAnimation()))))))) {
                                    if (!this.drawingGroups.contains(currentMessagesGroup)) {
                                        MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                                        transitionParams.left = 0;
                                        transitionParams.top = 0;
                                        transitionParams.right = 0;
                                        transitionParams.bottom = 0;
                                        transitionParams.pinnedBotton = false;
                                        transitionParams.pinnedTop = false;
                                        transitionParams.cell = chatMessageCell2;
                                        this.drawingGroups.add(currentMessagesGroup);
                                    }
                                    currentMessagesGroup.transitionParams.pinnedTop = chatMessageCell2.isPinnedTop();
                                    currentMessagesGroup.transitionParams.pinnedBotton = chatMessageCell2.isPinnedBottom();
                                    int x = (int) (chatMessageCell2.getX() + chatMessageCell2.getBackgroundDrawableLeft());
                                    int x2 = (int) (chatMessageCell2.getX() + chatMessageCell2.getBackgroundDrawableRight());
                                    int y = (int) (chatMessageCell2.getY() + chatMessageCell2.getBackgroundDrawableTop());
                                    int y2 = (int) (chatMessageCell2.getY() + chatMessageCell2.getBackgroundDrawableBottom());
                                    if ((chatMessageCell2.getCurrentPosition().flags & 4) == 0) {
                                        y -= AndroidUtilities.dp(10.0f);
                                    }
                                    if ((chatMessageCell2.getCurrentPosition().flags & 8) == 0) {
                                        y2 += AndroidUtilities.dp(10.0f);
                                    }
                                    if (chatMessageCell2.willRemovedAfterAnimation()) {
                                        currentMessagesGroup.transitionParams.cell = chatMessageCell2;
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                    int i5 = transitionParams2.top;
                                    if (i5 == 0 || y < i5) {
                                        transitionParams2.top = y;
                                    }
                                    int i6 = transitionParams2.bottom;
                                    if (i6 == 0 || y2 > i6) {
                                        transitionParams2.bottom = y2;
                                    }
                                    int i7 = transitionParams2.left;
                                    if (i7 == 0 || x < i7) {
                                        transitionParams2.left = x;
                                    }
                                    int i8 = transitionParams2.right;
                                    if (i8 == 0 || x2 > i8) {
                                        transitionParams2.right = x2;
                                    }
                                }
                            }
                        }
                        for (int i9 = 0; i9 < this.drawingGroups.size(); i9++) {
                            MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) this.drawingGroups.get(i9);
                            float nonAnimationTranslationX = groupedMessages2.transitionParams.cell.getNonAnimationTranslationX(true);
                            MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages2.transitionParams;
                            float f = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                            float f2 = transitionParams3.top + transitionParams3.offsetTop;
                            float f3 = transitionParams3.right + nonAnimationTranslationX + transitionParams3.offsetRight;
                            float f4 = transitionParams3.bottom + transitionParams3.offsetBottom;
                            if (f2 < (-AndroidUtilities.dp(20.0f))) {
                                f2 = -AndroidUtilities.dp(20.0f);
                            }
                            float f5 = f2;
                            if (f4 > MessageSendPreview.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) {
                                f4 = MessageSendPreview.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f);
                            }
                            MessageSendPreview.this.drawStarsPrice(canvas, f, f5, f3, f4);
                            groupedMessages2.transitionParams.cell = null;
                        }
                    }
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.saveLayerAlpha(0.0f, getScrollY() + 1, getWidth(), (getScrollY() + getHeight()) - 1, NotificationCenter.newLocationAvailable, 31);
                canvas.save();
                drawChatBackgroundElements(canvas);
                super.dispatchDraw(canvas);
                drawChatForegroundElements(canvas);
                canvas.save();
                float f = this.top.set(canScrollVertically(-1));
                float f2 = this.bottom.set(canScrollVertically(1));
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, getScrollY(), getWidth(), getScrollY() + AndroidUtilities.dp(14.0f));
                this.clip.draw(canvas, rectF, true, f);
                rectF.set(0.0f, (getScrollY() + getHeight()) - AndroidUtilities.dp(14.0f), getWidth(), getScrollY() + getHeight());
                this.clip.draw(canvas, rectF, false, f2);
                canvas.restore();
                canvas.restore();
                canvas.restore();
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                if (MessageSendPreview.this.openInProgress && ((view == MessageSendPreview.this.mainMessageCell && MessageSendPreview.this.mainMessageCell != null && MessageSendPreview.this.mainMessageCell.getCurrentPosition() == null) || view == MessageSendPreview.this.sendButton)) {
                    return false;
                }
                if (!(view instanceof ChatMessageCell)) {
                    return true;
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                chatMessageCell.setInvalidatesParent(true);
                chatMessageCell.drawCheckBox(canvas);
                canvas.save();
                canvas.translate(chatMessageCell.getX(), chatMessageCell.getY());
                canvas.scale(chatMessageCell.getScaleX(), chatMessageCell.getScaleY(), chatMessageCell.getPivotX(), chatMessageCell.getPivotY());
                if (chatMessageCell.drawBackgroundInParent() && chatMessageCell.getCurrentPosition() == null) {
                    chatMessageCell.drawBackgroundInternal(canvas, true);
                }
                canvas.restore();
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.save();
                canvas.translate(chatMessageCell.getX(), chatMessageCell.getY());
                canvas.scale(chatMessageCell.getScaleX(), chatMessageCell.getScaleY(), chatMessageCell.getPivotX(), chatMessageCell.getPivotY());
                if (chatMessageCell.getCurrentPosition() != null && (((chatMessageCell.getCurrentPosition().flags & chatMessageCell.captionFlag()) != 0 && (chatMessageCell.getCurrentPosition().flags & 1) != 0) || (chatMessageCell.getCurrentMessagesGroup() != null && chatMessageCell.getCurrentMessagesGroup().isDocuments))) {
                    chatMessageCell.drawCaptionLayout(canvas, false, chatMessageCell.getAlpha());
                }
                if (chatMessageCell.getCurrentPosition() != null && (((chatMessageCell.getCurrentPosition().flags & 8) != 0 && (chatMessageCell.getCurrentPosition().flags & 1) != 0) || (chatMessageCell.getCurrentMessagesGroup() != null && chatMessageCell.getCurrentMessagesGroup().isDocuments))) {
                    chatMessageCell.drawReactionsLayout(canvas, chatMessageCell.getAlpha(), null);
                    chatMessageCell.drawCommentLayout(canvas, chatMessageCell.getAlpha());
                }
                if (chatMessageCell.getCurrentPosition() != null) {
                    chatMessageCell.drawNamesLayout(canvas, chatMessageCell.getAlpha());
                }
                if (chatMessageCell.getCurrentPosition() == null || chatMessageCell.getCurrentPosition().last) {
                    chatMessageCell.drawTime(canvas, chatMessageCell.getAlpha(), true);
                }
                chatMessageCell.drawOutboundsContent(canvas);
                chatMessageCell.getTransitionParams().recordDrawingStatePreview();
                canvas.restore();
                chatMessageCell.setInvalidatesParent(false);
                return drawChild;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                for (int i6 = 0; i6 < getChildCount(); i6++) {
                    View childAt = getChildAt(i6);
                    if (childAt.getTop() != 0 && (childAt instanceof MessageCell)) {
                        MessageCell messageCell = (MessageCell) childAt;
                        messageCell.top = childAt.getTop();
                        messageCell.bottom = childAt.getBottom();
                        messageCell.pastId = messageCell.getMessageObject().getId();
                    }
                }
                super.onLayout(z, i2, i3, i4, i5);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            protected void onMeasure(int i2, int i3) {
                super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(Math.max(0, ((AndroidUtilities.displaySize.y - (AndroidUtilities.dp(MessageSendPreview.this.messageObjects.isEmpty() ? -6.0f : 48.0f) + (MessageSendPreview.this.optionsView == null ? 0 : MessageSendPreview.this.optionsView.getMeasuredHeight()))) - AndroidUtilities.dp(8.0f)) - MessageSendPreview.this.insets.top), Integer.MIN_VALUE));
                int max = Math.max(MessageSendPreview.this.sendButtonWidth, -((MessageSendPreview.this.sendButtonInitialPosition[0] + AndroidUtilities.dp(7.0f)) - getMeasuredWidth()));
                float max2 = Math.max(1, getMeasuredWidth() - max) / Math.max(1, ((getMeasuredWidth() - max) - AndroidUtilities.dp(8.0f)) + Math.max(0, MessageSendPreview.this.messageObjectsWidth - ((getMeasuredWidth() - max) - AndroidUtilities.dp((MessageSendPreview.this.groupedMessagesMap.isEmpty() ? 0 : 40) + 8))));
                setPivotX(getMeasuredWidth());
                setPivotY(getMeasuredHeight());
                setScaleX(max2);
                setScaleY(max2);
            }
        };
        this.chatListView = recyclerListView;
        recyclerListView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MessageSendPreview.this.lambda$new$1(view);
            }
        });
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                MessageSendPreview.this.lambda$new$2(view, i2);
            }
        });
        recyclerListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.MessageSendPreview.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                MessageSendPreview.this.chatListView.invalidate();
            }
        });
        recyclerListView.setItemAnimator(new 7(null, recyclerListView, resourcesProvider));
        GridLayoutManagerFixed gridLayoutManagerFixed = new GridLayoutManagerFixed(context, MediaDataController.MAX_STYLE_RUNS_COUNT, 1, true) { // from class: org.telegram.ui.MessageSendPreview.8
            boolean computingScroll;

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int computeVerticalScrollExtent(RecyclerView.State state) {
                this.computingScroll = true;
                int computeVerticalScrollExtent = super.computeVerticalScrollExtent(state);
                this.computingScroll = false;
                return computeVerticalScrollExtent;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int computeVerticalScrollOffset(RecyclerView.State state) {
                this.computingScroll = true;
                int computeVerticalScrollOffset = super.computeVerticalScrollOffset(state);
                this.computingScroll = false;
                return computeVerticalScrollOffset;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int computeVerticalScrollRange(RecyclerView.State state) {
                this.computingScroll = true;
                int computeVerticalScrollRange = super.computeVerticalScrollRange(state);
                this.computingScroll = false;
                return computeVerticalScrollRange;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            protected boolean hasSiblingChild(int i2) {
                byte b;
                MessageObject messageObject = (MessageObject) MessageSendPreview.this.messageObjects.get((getItemCount() - 1) - i2);
                MessageObject.GroupedMessages validGroupedMessage = MessageSendPreview.this.getValidGroupedMessage(messageObject);
                if (validGroupedMessage != null) {
                    MessageObject.GroupedMessagePosition position = validGroupedMessage.getPosition(messageObject);
                    if (position.minX != position.maxX && (b = position.minY) == position.maxY && b != 0) {
                        int size = validGroupedMessage.posArray.size();
                        for (int i3 = 0; i3 < size; i3++) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition = validGroupedMessage.posArray.get(i3);
                            if (groupedMessagePosition != position) {
                                byte b2 = groupedMessagePosition.minY;
                                byte b3 = position.minY;
                                if (b2 <= b3 && groupedMessagePosition.maxY >= b3) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            public boolean shouldLayoutChildFromOpositeSide(View view) {
                if (view instanceof ChatMessageCell) {
                    return !((ChatMessageCell) view).getMessageObject().isOutOwner();
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        this.chatLayoutManager = gridLayoutManagerFixed;
        gridLayoutManagerFixed.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.MessageSendPreview.9
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i2) {
                MessageObject messageObject = (MessageObject) MessageSendPreview.this.messageObjects.get((MessageSendPreview.this.messageObjects.size() - 1) - i2);
                MessageObject.GroupedMessages validGroupedMessage = MessageSendPreview.this.getValidGroupedMessage(messageObject);
                return validGroupedMessage != null ? validGroupedMessage.getPosition(messageObject).spanSize : MediaDataController.MAX_STYLE_RUNS_COUNT;
            }
        });
        recyclerListView.setLayoutManager(gridLayoutManagerFixed);
        recyclerListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.MessageSendPreview.10
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                ChatMessageCell chatMessageCell;
                MessageObject.GroupedMessages currentMessagesGroup;
                MessageObject.GroupedMessagePosition currentPosition;
                int i2 = 0;
                rect.bottom = 0;
                if (!(view instanceof ChatMessageCell) || (currentMessagesGroup = (chatMessageCell = (ChatMessageCell) view).getCurrentMessagesGroup()) == null || (currentPosition = chatMessageCell.getCurrentPosition()) == null || currentPosition.siblingHeights == null) {
                    return;
                }
                Point point = AndroidUtilities.displaySize;
                float max = Math.max(point.x, point.y) * 0.5f;
                int extraInsetHeight = chatMessageCell.getExtraInsetHeight();
                int i3 = 0;
                while (true) {
                    if (i3 >= currentPosition.siblingHeights.length) {
                        break;
                    }
                    extraInsetHeight += (int) Math.ceil(r3[i3] * max);
                    i3++;
                }
                int round = extraInsetHeight + ((currentPosition.maxY - currentPosition.minY) * Math.round(AndroidUtilities.density * 7.0f));
                int size = currentMessagesGroup.posArray.size();
                while (true) {
                    if (i2 < size) {
                        MessageObject.GroupedMessagePosition groupedMessagePosition = currentMessagesGroup.posArray.get(i2);
                        byte b = groupedMessagePosition.minY;
                        byte b2 = currentPosition.minY;
                        if (b == b2 && ((groupedMessagePosition.minX != currentPosition.minX || groupedMessagePosition.maxX != currentPosition.maxX || b != b2 || groupedMessagePosition.maxY != currentPosition.maxY) && b == b2)) {
                            round -= ((int) Math.ceil(max * groupedMessagePosition.ph)) - AndroidUtilities.dp(4.0f);
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                rect.bottom = -round;
            }
        });
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() { // from class: org.telegram.ui.MessageSendPreview.11
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return MessageSendPreview.this.messageObjects.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
                MessageObject messageObject = (MessageObject) MessageSendPreview.this.messageObjects.get((getItemCount() - 1) - i2);
                ChatMessageCell chatMessageCell = (ChatMessageCell) viewHolder.itemView;
                MessageObject.GroupedMessages validGroupedMessage = MessageSendPreview.this.getValidGroupedMessage(messageObject);
                chatMessageCell.setInvalidatesParent(validGroupedMessage != null);
                chatMessageCell.setMessageObject(messageObject, validGroupedMessage, false, false);
                if (i2 != MessageSendPreview.this.getMainMessageCellPosition() || messageObject.needDrawForwarded()) {
                    return;
                }
                MessageSendPreview.this.mainMessageCell = chatMessageCell;
                MessageSendPreview.this.mainMessageCellId = messageObject.getId();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
                MessageSendPreview messageSendPreview = MessageSendPreview.this;
                MessageCell messageCell = messageSendPreview.new MessageCell(context, messageSendPreview.currentAccount, true, null, resourcesProvider);
                messageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() { // from class: org.telegram.ui.MessageSendPreview.11.1
                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean canPerformActions() {
                        return false;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canPerformReply() {
                        boolean canPerformActions;
                        canPerformActions = canPerformActions();
                        return canPerformActions;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC.Chat chat, int i3, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell, chat, i3, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell, TLRPC.User user, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell, user, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressAboutRevenueSharingAds() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAboutRevenueSharingAds(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell, AnimatedEmojiSpan animatedEmojiSpan) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell, animatedEmojiSpan);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBoostCounter(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC.Chat chat, int i3, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell, chat, i3, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell, TLObject tLObject, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell, tLObject, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCodeCopy(ChatMessageCell chatMessageCell, MessageObject.TextLayoutBlock textLayoutBlock) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCodeCopy(this, chatMessageCell, textLayoutBlock);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressDialogButton(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressDialogButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressEffect(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheck(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell, int i3, int i4) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheckWhat(this, chatMessageCell, i3, i4);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell, ImageReceiver imageReceiver, TLRPC.MessageExtendedMedia messageExtendedMedia, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGroupImage(this, chatMessageCell, imageReceiver, messageExtendedMedia, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHiddenForward(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHint(this, chatMessageCell, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressImage(this, chatMessageCell, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressInstantButton(this, chatMessageCell, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell, reactionCount, z, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell, int i3, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell, i3, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressRevealSensitiveContent(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredInfo(this, chatMessageCell, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTime(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTopicButton(ChatMessageCell chatMessageCell) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTopicButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUrl(ChatMessageCell chatMessageCell, CharacterStyle characterStyle, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUrl(this, chatMessageCell, characterStyle, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell, TLRPC.User user, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell, user, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell, TLRPC.User user, TLRPC.Document document, String str) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell, user, document, str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell, String str) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBot(this, chatMessageCell, str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell, long j) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBotNotInline(this, chatMessageCell, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell, ArrayList arrayList, int i3, int i4, int i5) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressVoteButtons(this, chatMessageCell, arrayList, i3, i4, i5);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell, TLRPC.WebPage webPage, String str, boolean z) {
                        Browser.openUrl(chatMessageCell.getContext(), str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean doNotShowLoadingReply(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$doNotShowLoadingReply(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void forceUpdate(ChatMessageCell chatMessageCell, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$forceUpdate(this, chatMessageCell, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getAdminRank(long j) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getAdminRank(this, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getPinchToZoomHelper(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingLink(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getTextSelectionHelper(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean hasSelectedMessages() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$hasSelectedMessages(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void invalidateBlur() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$invalidateBlur(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isLandscape() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isLandscape(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isProgressLoading(ChatMessageCell chatMessageCell, int i3) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isProgressLoading(this, chatMessageCell, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isReplyOrSelf() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isReplyOrSelf(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean keyboardIsOpened() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$keyboardIsOpened(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i3, int i4) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needOpenWebView(this, messageObject, str, str2, str3, str4, i3, i4);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean needPlayMessage(ChatMessageCell chatMessageCell, MessageObject messageObject, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, chatMessageCell, messageObject, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needReloadPolls() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needReloadPolls(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needShowPremiumBulletin(int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needShowPremiumBulletin(this, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean onAccessibilityAction(int i3, Bundle bundle) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onAccessibilityAction(this, i3, bundle);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void onDiceFinished() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onDiceFinished(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$setShouldNotRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowDialogButton(ChatMessageCell chatMessageCell) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowDialogButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowTopicButton(ChatMessageCell chatMessageCell) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this, chatMessageCell);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void videoTimerReached() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                    }
                });
                return new RecyclerListView.Holder(messageCell);
            }
        };
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        recyclerListView.setVerticalScrollBarEnabled(false);
        recyclerListView.setOverScrollMode(2);
        r13.addView(recyclerListView, LayoutHelper.createFrame(-1, -2.0f));
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.MessageSendPreview.12
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                MessageSendPreview.this.effectOverlay.draw(canvas);
                float progress = MessageSendPreview.this.effectOverlay.getProgress();
                if (progress != -2.0f) {
                    MessageSendPreview.this.sendButton.setLoading(progress >= 0.0f && progress < 1.0f, -3.0f);
                }
                if (MessageSendPreview.this.effectOverlay.isIdle()) {
                    return;
                }
                invalidate();
            }
        };
        this.effectsView = frameLayout2;
        frameLayout.addView(frameLayout2, LayoutHelper.createFrame(-1, -1.0f));
        this.effectOverlay = new EmojiAnimationsOverlay(frameLayout2, i) { // from class: org.telegram.ui.MessageSendPreview.13
            int[] messagePos = new int[2];

            @Override // org.telegram.ui.EmojiAnimationsOverlay
            protected void layoutObject(EmojiAnimationsOverlay.DrawingObject drawingObject) {
                if (drawingObject == null) {
                    return;
                }
                if (MessageSendPreview.this.cameraRect != null) {
                    drawingObject.viewFound = true;
                    float filterWidth = (EmojiAnimationsOverlay.getFilterWidth() * AndroidUtilities.density) / 1.3f;
                    float f = filterWidth / 3.0f;
                    drawingObject.lastW = f;
                    drawingObject.lastH = f;
                    drawingObject.lastX = Utilities.clamp(MessageSendPreview.this.cameraRect.right - (0.75f * filterWidth), AndroidUtilities.displaySize.x - filterWidth, 0.0f);
                    drawingObject.lastY = MessageSendPreview.this.cameraRect.bottom - (filterWidth / 2.0f);
                    return;
                }
                if (MessageSendPreview.this.mainMessageCell == null || !MessageSendPreview.this.mainMessageCell.isAttachedToWindow() || MessageSendPreview.this.mainMessageCell.getMessageObject() == null || MessageSendPreview.this.mainMessageCell.getMessageObject().getId() != MessageSendPreview.this.mainMessageCellId) {
                    return;
                }
                MessageSendPreview.this.mainMessageCell.getLocationOnScreen(this.messagePos);
                drawingObject.viewFound = true;
                float filterWidth2 = (EmojiAnimationsOverlay.getFilterWidth() * AndroidUtilities.density) / 1.3f;
                float f2 = filterWidth2 / 3.0f;
                drawingObject.lastW = f2;
                drawingObject.lastH = f2;
                float f3 = filterWidth2 / 2.0f;
                drawingObject.lastX = Utilities.clamp((this.messagePos[0] + (MessageSendPreview.this.mainMessageCell.getTimeX() * MessageSendPreview.this.chatListView.getScaleX())) - f3, AndroidUtilities.displaySize.x - filterWidth2, 0.0f);
                drawingObject.lastY = (this.messagePos[1] + (MessageSendPreview.this.mainMessageCell.getTimeY() * MessageSendPreview.this.chatListView.getScaleY())) - f3;
            }
        };
    }

    private void animateOpenTo(final boolean z, final Runnable runnable) {
        View view;
        ValueAnimator valueAnimator = this.openAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        final boolean z2 = z && (view = this.optionsView) != null && (view instanceof ActionBarPopupWindow.ActionBarPopupWindowLayout);
        if (z2) {
            ActionBarPopupWindow.startAnimation((ActionBarPopupWindow.ActionBarPopupWindowLayout) this.optionsView);
        }
        if (!z) {
            hideEffectSelector();
        }
        this.openInProgress = true;
        this.opening = z;
        this.closing = !z;
        this.chatListView.invalidate();
        this.firstOpenFrame = true;
        this.firstOpenFrame2 = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.openProgress, z ? 1.0f : 0.0f);
        this.openAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                MessageSendPreview.this.lambda$animateOpenTo$8(z2, valueAnimator2);
            }
        });
        this.openAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.MessageSendPreview.17
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                View view2;
                MessageSendPreview.this.openProgress = z ? 1.0f : 0.0f;
                MessageSendPreview.this.firstOpenFrame = false;
                MessageSendPreview.this.firstOpenFrame2 = false;
                MessageSendPreview.this.effectsView.setAlpha(MessageSendPreview.this.openProgress);
                if (z) {
                    MessageSendPreview.this.openInProgress = false;
                    MessageSendPreview.this.opening = false;
                    MessageSendPreview.this.closing = false;
                }
                if (MessageSendPreview.this.editText != null) {
                    MessageSendPreview.this.editText.setAlpha(1.0f);
                }
                if (MessageSendPreview.this.destCell != null) {
                    MessageSendPreview.this.destCell.setVisibility(0);
                }
                if (MessageSendPreview.this.anchorSendButton != null && !MessageSendPreview.this.sent) {
                    MessageSendPreview.this.anchorSendButton.setAlpha(1.0f);
                }
                if (!z && MessageSendPreview.this.sendButton != null) {
                    MessageSendPreview.this.sendButton.setAlpha(0.0f);
                }
                if (!z2 && MessageSendPreview.this.optionsView != null) {
                    MessageSendPreview.this.optionsView.setAlpha(MessageSendPreview.this.openProgress);
                }
                MessageSendPreview.this.chatListView.invalidate();
                MessageSendPreview.this.chatListView.setAlpha(MessageSendPreview.this.openProgress);
                MessageSendPreview.this.windowView.invalidate();
                MessageSendPreview.this.containerView.invalidate();
                if (runnable != null) {
                    if (!z && MessageSendPreview.this.destCell != null && MessageSendPreview.this.destCell.isAttachedToWindow()) {
                        view2 = MessageSendPreview.this.destCell;
                    } else {
                        if (z || MessageSendPreview.this.editText == null || !MessageSendPreview.this.editText.isAttachedToWindow()) {
                            AndroidUtilities.runOnUIThread(runnable);
                            return;
                        }
                        view2 = MessageSendPreview.this.editText;
                    }
                    view2.post(runnable);
                }
            }
        });
        this.openAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.openAnimator.setDuration(350L);
        this.openAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMainMessageCellPosition() {
        if (this.groupedMessagesMap.isEmpty() || this.messageObjects.size() < 10) {
            return 0;
        }
        return this.messageObjects.size() % 10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MessageObject.GroupedMessages getValidGroupedMessage(MessageObject messageObject) {
        if (messageObject.getGroupId() == 0) {
            return null;
        }
        MessageObject.GroupedMessages groupedMessages = (MessageObject.GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupId());
        if (groupedMessages == null || (groupedMessages.messages.size() > 1 && groupedMessages.getPosition(messageObject) != null)) {
            return groupedMessages;
        }
        return null;
    }

    private int getWidthForMessage(MessageObject messageObject) {
        if (getContext() == null) {
            return 0;
        }
        if (this.dummyMessageCell == null) {
            this.dummyMessageCell = new ChatMessageCell(getContext(), this.currentAccount, true, null, this.resourcesProvider);
        }
        ChatMessageCell chatMessageCell = this.dummyMessageCell;
        chatMessageCell.isChat = false;
        chatMessageCell.isSavedChat = false;
        chatMessageCell.isSavedPreviewChat = false;
        chatMessageCell.isBot = false;
        chatMessageCell.isMegagroup = false;
        return chatMessageCell.computeWidth(messageObject, (MessageObject.GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$allowEffectSelector$3(Integer num) {
        boolean z = num.intValue() - this.insets.bottom > AndroidUtilities.dp(20.0f);
        this.keyboardVisible = z;
        this.effectSelectorContainer.animate().translationY((z ? Math.min(this.effectSelectorContainerY, (this.windowView.getHeight() - num.intValue()) - this.effectSelectorContainer.getMeasuredHeight()) : this.effectSelectorContainerY) - this.effectSelectorContainer.getTop()).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpenTo$8(boolean z, ValueAnimator valueAnimator) {
        View view;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.openProgress = floatValue;
        this.effectsView.setAlpha(floatValue);
        this.chatListView.setAlpha(this.openProgress);
        if (!z && (view = this.optionsView) != null) {
            view.setAlpha(this.openProgress);
        }
        this.windowView.invalidate();
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$6() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$7() {
        SpoilerEffect2.pause(0, false);
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this.windowView);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MessageSendPreview.this.lambda$dismiss$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissInto$4() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissInto$5() {
        SpoilerEffect2.pause(0, false);
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this.windowView);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                MessageSendPreview.this.lambda$dismissInto$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view, int i) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareBlur$9(View view, Bitmap bitmap) {
        if (view != null) {
            view.setVisibility(0);
        }
        this.blurBitmap = bitmap;
        Paint paint = new Paint(1);
        this.blurBitmapPaint = paint;
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        this.blurBitmapShader = bitmapShader;
        paint.setShader(bitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? 0.08f : 0.25f);
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? -0.02f : -0.07f);
        this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.blurMatrix = new Matrix();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layout() {
        if (this.windowView.getWidth() <= 0) {
            return;
        }
        int[] iArr = new int[2];
        this.anchorSendButton.getLocationOnScreen(iArr);
        int[] iArr2 = this.sendButtonInitialPosition;
        iArr2[0] = iArr[0];
        iArr2[1] = iArr[1];
        int measuredHeight = (this.chatListView.getMeasuredHeight() - this.sendButton.getWidth()) + (this.effectSelector != null ? AndroidUtilities.dp(320.0f) : 0);
        int dp = this.insets.top + AndroidUtilities.dp(8.0f);
        int dp2 = AndroidUtilities.dp(this.messageObjects.isEmpty() ? -6.0f : 48.0f);
        View view = this.optionsView;
        int measuredHeight2 = dp2 + (view == null ? 0 : view.getMeasuredHeight());
        int measuredHeight3 = (this.containerView.getMeasuredHeight() - AndroidUtilities.dp(8.0f)) - this.insets.bottom;
        if (iArr[1] + measuredHeight2 > measuredHeight3) {
            iArr[1] = measuredHeight3 - measuredHeight2;
        }
        if (iArr[1] - measuredHeight < dp) {
            iArr[1] = dp + measuredHeight;
        }
        if (iArr[1] + measuredHeight2 > measuredHeight3) {
            iArr[1] = measuredHeight3 - measuredHeight2;
        }
        this.sendButton.setX(iArr[0]);
        this.sendButton.setY(iArr[1]);
        this.chatListView.setX((iArr[0] + AndroidUtilities.dp(7.0f)) - this.chatListView.getMeasuredWidth());
        if (this.layoutDone) {
            this.chatListView.animate().translationY(((iArr[1] + this.sendButton.getWidth()) - this.chatListView.getMeasuredHeight()) - this.chatListView.getTop()).setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR).setDuration(250L).start();
        } else {
            this.chatListView.setY((iArr[1] + this.sendButton.getWidth()) - this.chatListView.getMeasuredHeight());
        }
        View view2 = this.optionsView;
        if (view2 != null) {
            view2.setX((iArr[0] + AndroidUtilities.dp(7.0f)) - this.optionsView.getMeasuredWidth());
            this.optionsView.setY(iArr[1] + (this.messageObjects.isEmpty() ? -AndroidUtilities.dp(6.0f) : this.sendButton.getHeight()));
        }
        FrameLayout frameLayout = this.effectSelectorContainer;
        if (frameLayout != null) {
            frameLayout.setX(Math.max(0, ((iArr[0] + this.sendButton.getWidth()) - this.effectSelectorContainer.getMeasuredWidth()) - AndroidUtilities.dp(6.0f)));
            RectF rectF = this.cameraRect;
            if (rectF != null) {
                FrameLayout frameLayout2 = this.effectSelectorContainer;
                float max = Math.max(this.insets.top, rectF.top - frameLayout2.getMeasuredWidth());
                this.effectSelectorContainerY = max;
                frameLayout2.setY(max);
                ReactionsContainerLayout reactionsContainerLayout = this.effectSelector;
                if (reactionsContainerLayout != null) {
                    reactionsContainerLayout.setY(Math.max(this.insets.top, (this.cameraRect.top - AndroidUtilities.dp(24.0f)) - this.effectSelector.getMeasuredHeight()));
                    return;
                }
                return;
            }
            float width = (iArr[1] + this.sendButton.getWidth()) - this.chatListView.getMeasuredHeight();
            FrameLayout frameLayout3 = this.effectSelectorContainer;
            float max2 = Math.max(this.insets.top, width - frameLayout3.getMeasuredHeight()) + AndroidUtilities.dp(24.0f);
            this.effectSelectorContainerY = max2;
            frameLayout3.setY(max2);
            ReactionsContainerLayout reactionsContainerLayout2 = this.effectSelector;
            if (reactionsContainerLayout2 != null) {
                reactionsContainerLayout2.setY(Math.max(0.0f, (width - reactionsContainerLayout2.getMeasuredHeight()) - this.effectSelectorContainerY));
            }
        }
    }

    private void prepareBlur(final View view) {
        if (view != null) {
            view.setVisibility(4);
        }
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MessageSendPreview.this.lambda$prepareBlur$9(view, (Bitmap) obj);
            }
        }, 14.0f);
    }

    public void allowEffectSelector(BaseFragment baseFragment) {
        if (this.effectSelector != null || baseFragment == null) {
            return;
        }
        MessagesController.getInstance(this.currentAccount).getAvailableEffects();
        FrameLayout frameLayout = new FrameLayout(this.context);
        this.effectSelectorContainer = frameLayout;
        frameLayout.setClipChildren(false);
        this.effectSelectorContainer.setClipToPadding(false);
        this.effectSelectorContainer.setPadding(0, 0, 0, AndroidUtilities.dp(24.0f));
        ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout(5, null, getContext(), this.currentAccount, this.resourcesProvider) { // from class: org.telegram.ui.MessageSendPreview.15
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
                setPivotX(getMeasuredWidth());
                setPivotY(getMeasuredHeight());
            }
        };
        this.effectSelector = reactionsContainerLayout;
        reactionsContainerLayout.setClipChildren(false);
        this.effectSelector.setClipToPadding(false);
        this.effectSelector.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(22.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(22.0f));
        this.effectSelector.setDelegate(new 16(baseFragment));
        this.effectSelector.setTop(false);
        this.effectSelector.setClipChildren(false);
        this.effectSelector.setClipToPadding(false);
        this.effectSelector.setVisibility(0);
        this.effectSelector.setHint(LocaleController.getString(R.string.AddEffectMessageHint));
        this.effectSelector.setBubbleOffset(AndroidUtilities.dp(-25.0f));
        this.effectSelector.setMiniBubblesOffset(AndroidUtilities.dp(2.0f));
        this.containerView.addView(this.effectSelectorContainer, LayoutHelper.createFrame(-2, 300.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.effectSelectorContainer.addView(this.effectSelector, LayoutHelper.createFrame(-1, 116.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        this.effectSelector.setScaleY(0.4f);
        this.effectSelector.setScaleX(0.4f);
        this.effectSelector.setAlpha(0.0f);
        if (MessagesController.getInstance(this.currentAccount).hasAvailableEffects()) {
            showEffectSelector();
        } else {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.availableEffectsUpdate);
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.effectSelector;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.setPaused(true, true);
        }
        new KeyboardNotifier(this.windowView, new Utilities.Callback() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MessageSendPreview.this.lambda$allowEffectSelector$3((Integer) obj);
            }
        });
    }

    public void changeMessage(MessageObject messageObject) {
        MessageObject.GroupedMessages validGroupedMessage = getValidGroupedMessage(messageObject);
        if (validGroupedMessage == null) {
            changeMessageInternal(messageObject);
            return;
        }
        validGroupedMessage.calculate();
        Iterator<MessageObject> it = validGroupedMessage.messages.iterator();
        while (it.hasNext()) {
            changeMessageInternal(it.next());
        }
    }

    public void changeMessageInternal(MessageObject messageObject) {
        ChatMessageCell chatMessageCell;
        if (this.chatListView == null) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.chatListView.getChildCount()) {
                chatMessageCell = null;
                break;
            }
            View childAt = this.chatListView.getChildAt(i);
            if (childAt instanceof ChatMessageCell) {
                chatMessageCell = (ChatMessageCell) childAt;
                if (chatMessageCell.getMessageObject() == messageObject) {
                    break;
                }
            }
            i++;
        }
        int i2 = -1;
        for (int i3 = 0; i3 < this.messageObjects.size(); i3++) {
            if (this.messageObjects.get(i3) == messageObject) {
                i2 = (this.messageObjects.size() - 1) - i3;
            }
        }
        if (chatMessageCell != null) {
            messageObject.forceUpdate = true;
            chatMessageCell.setMessageObject(messageObject, chatMessageCell.getCurrentMessagesGroup(), chatMessageCell.isPinnedBottom(), chatMessageCell.isPinnedTop());
        }
        this.chatListView.getAdapter().notifyItemChanged(i2);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.availableEffectsUpdate && MessagesController.getInstance(this.currentAccount).hasAvailableEffects()) {
            showEffectSelector();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        ChatActivityEnterView.SendButton sendButton = this.sendButton;
        if (sendButton != null) {
            sendButton.invalidate();
        }
        ChatActivityEnterView.SendButton sendButton2 = this.anchorSendButton;
        if (sendButton2 != null) {
            sendButton2.invalidate();
        }
        animateOpenTo(false, new Runnable() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MessageSendPreview.this.lambda$dismiss$7();
            }
        });
        this.windowView.invalidate();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.availableEffectsUpdate);
    }

    public void dismiss(boolean z) {
        this.sent = z;
        dismiss();
    }

    public void dismissInto(ChatMessageCell chatMessageCell, float f, float f2) {
        if (this.dismissing) {
            return;
        }
        this.sent = true;
        this.dismissing = true;
        ChatActivityEnterView.SendButton sendButton = this.sendButton;
        if (sendButton != null) {
            sendButton.invalidate();
        }
        ChatActivityEnterView.SendButton sendButton2 = this.anchorSendButton;
        if (sendButton2 != null) {
            sendButton2.invalidate();
        }
        if (this.mainMessageCell != null && chatMessageCell != null) {
            this.destCell = chatMessageCell;
            chatMessageCell.setVisibility(4);
            this.destClipTop = f;
            this.destClipBottom = f2;
            ChatMessageCell chatMessageCell2 = this.mainMessageCell;
            ChatMessageCell chatMessageCell3 = this.destCell;
            chatMessageCell2.isChat = chatMessageCell3.isChat;
            chatMessageCell2.isThreadChat = chatMessageCell3.isThreadChat;
            chatMessageCell2.isSavedChat = chatMessageCell3.isSavedChat;
            chatMessageCell2.isBot = chatMessageCell3.isBot;
            chatMessageCell2.isForum = chatMessageCell3.isForum;
            chatMessageCell2.isForumGeneral = chatMessageCell3.isForumGeneral;
            chatMessageCell2.setMessageObject(chatMessageCell.getMessageObject(), null, chatMessageCell.isPinnedBottom(), chatMessageCell.isPinnedTop());
            ChatMessageCell.TransitionParams transitionParams = this.mainMessageCell.getTransitionParams();
            transitionParams.animateChange = this.mainMessageCell.getTransitionParams().animateChange();
            transitionParams.animateChangeProgress = 0.0f;
            boolean z = this.mainMessageCell.getTransitionParams().lastDrawingBackgroundRect.left != this.mainMessageCell.getBackgroundDrawableLeft();
            if (z || transitionParams.lastDrawingBackgroundRect.top != this.mainMessageCell.getBackgroundDrawableTop() || transitionParams.lastDrawingBackgroundRect.bottom != this.mainMessageCell.getBackgroundDrawableBottom()) {
                this.cellDelta.bottom = -(this.mainMessageCell.getBackgroundDrawableBottom() - transitionParams.lastDrawingBackgroundRect.bottom);
                this.cellDelta.top = -(this.mainMessageCell.getBackgroundDrawableTop() - transitionParams.lastDrawingBackgroundRect.top);
                if (chatMessageCell.getMessageObject().isOutOwner()) {
                    this.cellDelta.left = -(this.mainMessageCell.getBackgroundDrawableLeft() - transitionParams.lastDrawingBackgroundRect.left);
                    this.cellDelta.right = 0;
                } else {
                    Rect rect = this.cellDelta;
                    rect.left = 0;
                    rect.right = this.mainMessageCell.getBackgroundDrawableRight() - transitionParams.lastDrawingBackgroundRect.right;
                }
                transitionParams.animateBackgroundBoundsInner = true;
                transitionParams.animateBackgroundWidth = z;
            }
            this.fromPart = VisiblePart.of(this.mainMessageCell);
        }
        animateOpenTo(false, new Runnable() { // from class: org.telegram.ui.MessageSendPreview$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MessageSendPreview.this.lambda$dismissInto$5();
            }
        });
        this.windowView.invalidate();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.availableEffectsUpdate);
    }

    public void drawStarsPrice(Canvas canvas, float f, float f2, float f3, float f4) {
        if (this.buttonText == null || this.buttonBgPaint == null) {
            return;
        }
        float f5 = (f + f3) / 2.0f;
        float f6 = (f2 + f4) / 2.0f;
        float dp = AndroidUtilities.dp(28.0f) + this.buttonText.getCurrentWidth();
        float dp2 = AndroidUtilities.dp(32.0f);
        RectF rectF = AndroidUtilities.rectTmp;
        float f7 = dp / 2.0f;
        float f8 = f5 - f7;
        float f9 = dp2 / 2.0f;
        rectF.set(f8, f6 - f9, f5 + f7, f6 + f9);
        canvas.save();
        canvas.drawRoundRect(rectF, f9, f9, this.buttonBgPaint);
        this.buttonText.draw(canvas, f8 + AndroidUtilities.dp(14.0f), f6, -1, 1.0f);
        canvas.restore();
    }

    public long getSelectedEffect() {
        MessageObject messageObject;
        if (!this.sentEffect && this.effectSelector != null) {
            if (this.cameraRect != null) {
                this.sentEffect = true;
                return this.effectId;
            }
            ChatMessageCell chatMessageCell = this.mainMessageCell;
            if (chatMessageCell == null || (messageObject = chatMessageCell.getMessageObject()) == null) {
                return 0L;
            }
            TLRPC.Message message = messageObject.messageOwner;
            if ((message.flags2 & 4) == 0) {
                return 0L;
            }
            this.sentEffect = true;
            return message.effect;
        }
        return 0L;
    }

    public void hideEffectSelector() {
        ReactionsContainerLayout reactionsContainerLayout = this.effectSelector;
        if (reactionsContainerLayout != null && this.effectSelectorShown) {
            reactionsContainerLayout.dismissWindow();
            if (this.effectSelector.getReactionsWindow() != null && this.effectSelector.getReactionsWindow().containerView != null) {
                this.effectSelector.getReactionsWindow().containerView.animate().alpha(0.0f).setDuration(180L).start();
            }
            this.effectSelector.animate().alpha(0.01f).translationY(-AndroidUtilities.dp(12.0f)).scaleX(0.6f).scaleY(0.6f).setDuration(180L).start();
        }
    }

    @Override // android.app.Dialog
    public boolean isShowing() {
        return !this.dismissing;
    }

    public void makeFocusable() {
        if (this.focusable) {
            return;
        }
        try {
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags &= -131073;
            window.setAttributes(attributes);
            this.focusable = true;
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.keyboardVisible) {
            AndroidUtilities.hideKeyboard(getCurrentFocus());
            this.keyboardVisible = false;
            return;
        }
        ReactionsContainerLayout reactionsContainerLayout = this.effectSelector;
        if (reactionsContainerLayout == null || reactionsContainerLayout.getReactionsWindow() == null) {
            this.sentEffect = true;
            super.onBackPressed();
        } else {
            if (this.effectSelector.getReactionsWindow().transition) {
                return;
            }
            this.effectSelector.getReactionsWindow().dismiss();
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        attributes.gravity = 119;
        attributes.dimAmount = 0.0f;
        int i = attributes.flags & (-3);
        attributes.softInputMode = 16;
        attributes.flags = 131072 | i;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            attributes.flags = i | (-1945960192);
        }
        attributes.flags |= 1152;
        if (i2 >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        this.windowView.setSystemUiVisibility(256);
        AndroidUtilities.setLightNavigationBar(this.windowView, !Theme.isCurrentThemeDark());
    }

    protected void onEffectChange(long j) {
    }

    public void scrollTo(boolean z) {
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || recyclerListView.getAdapter() == null || this.chatLayoutManager == null) {
            return;
        }
        int itemCount = this.chatListView.getAdapter().getItemCount();
        this.chatLayoutManager.scrollToPositionWithOffset(z ? itemCount > 10 ? itemCount % 10 : 0 : itemCount - 1, AndroidUtilities.dp(12.0f), z);
        this.scrolledToLast = z;
    }

    public void setCameraTexture(TextureView textureView) {
        if (textureView == null) {
            return;
        }
        this.cameraRect = new RectF();
        int[] iArr = new int[2];
        textureView.getLocationOnScreen(iArr);
        this.cameraRect.set(iArr[0], iArr[1], r2 + textureView.getWidth(), iArr[1] + textureView.getHeight());
    }

    public void setEditText(EditTextCaption editTextCaption, Utilities.Callback2 callback2, Utilities.Callback callback) {
        this.editText = editTextCaption;
        this.drawEditText = callback2;
        this.drawEditTextBackground = callback;
    }

    public void setEffectId(long j) {
        TLRPC.TL_availableEffect effect;
        this.effectId = j;
        int mainMessageCellPosition = getMainMessageCellPosition();
        MessageObject messageObject = (mainMessageCellPosition < 0 || mainMessageCellPosition >= this.messageObjects.size()) ? null : (MessageObject) this.messageObjects.get(mainMessageCellPosition);
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            message.flags2 |= 4;
            message.effect = j;
        }
        if (this.effectSelector == null || (effect = MessagesController.getInstance(this.currentAccount).getEffect(j)) == null) {
            return;
        }
        this.effectSelector.setSelectedReactionAnimated(ReactionsLayoutInBubble.VisibleReaction.fromTL(effect));
    }

    public void setItemOptions(ItemOptions itemOptions) {
        ViewGroup layout = itemOptions.getLayout();
        this.optionsView = layout;
        this.containerView.addView(layout, LayoutHelper.createFrame(-2, -2.0f));
    }

    public void setMessageObjects(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = (MessageObject) arrayList.get(i);
            if (messageObject.hasValidGroupId()) {
                MessageObject.GroupedMessages groupedMessages = (MessageObject.GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupIdForUse());
                if (groupedMessages == null) {
                    groupedMessages = new MessageObject.GroupedMessages();
                    groupedMessages.reversed = false;
                    long groupId = messageObject.getGroupId();
                    groupedMessages.groupId = groupId;
                    this.groupedMessagesMap.put(groupId, groupedMessages);
                }
                if (groupedMessages.getPosition(messageObject) == null) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= groupedMessages.messages.size()) {
                            groupedMessages.messages.add(messageObject);
                            break;
                        } else if (groupedMessages.messages.get(i2).getId() == messageObject.getId()) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            } else if (messageObject.getGroupIdForUse() != 0) {
                messageObject.messageOwner.grouped_id = 0L;
                messageObject.localSentGroupId = 0L;
            }
        }
        for (int i3 = 0; i3 < this.groupedMessagesMap.size(); i3++) {
            ((MessageObject.GroupedMessages) this.groupedMessagesMap.valueAt(i3)).calculate();
        }
        this.messageObjects.addAll(arrayList);
        for (int i4 = 0; i4 < this.messageObjects.size(); i4++) {
            this.messageObjectsWidth = Math.max(this.messageObjectsWidth, getWidthForMessage((MessageObject) this.messageObjects.get(i4)));
        }
        this.chatListView.getAdapter().notifyDataSetChanged();
        int itemCount = this.chatListView.getAdapter().getItemCount();
        this.chatLayoutManager.scrollToPositionWithOffset(itemCount > 10 ? itemCount % 10 : 0, AndroidUtilities.dp(12.0f), true);
    }

    public void setSendButton(final ChatActivityEnterView.SendButton sendButton, final boolean z, View.OnClickListener onClickListener) {
        this.anchorSendButton = sendButton;
        sendButton.getLocationOnScreen(this.sendButtonInitialPosition);
        this.sendButtonWidth = this.anchorSendButton.getWidth();
        ChatActivityEnterView.SendButton sendButton2 = new ChatActivityEnterView.SendButton(getContext(), sendButton.resId, this.resourcesProvider) { // from class: org.telegram.ui.MessageSendPreview.14
            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public int getFillColor() {
                return sendButton.getFillColor();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInScheduleMode() {
                return sendButton.isInScheduleMode();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInactive() {
                return sendButton.isInactive();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isOpen() {
                return (z && MessageSendPreview.this.dismissing) ? false : true;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean shouldDrawBackground() {
                return sendButton.shouldDrawBackground();
            }
        };
        this.sendButton = sendButton2;
        this.anchorSendButton.copyCountTo(sendButton2);
        this.anchorSendButton.copyEmojiTo(this.sendButton);
        ChatActivityEnterView.SendButton sendButton3 = this.sendButton;
        sendButton3.center = sendButton.center;
        sendButton3.open.set(sendButton.open.get(), true);
        this.sendButton.setOnClickListener(onClickListener);
        this.containerView.addView(this.sendButton, new ViewGroup.LayoutParams(sendButton.getWidth(), sendButton.getHeight()));
    }

    public void setStars(long j) {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        this.buttonText = j <= 0 ? null : new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) j), 0.7f), 14.0f, AndroidUtilities.bold());
        if (this.buttonBgPaint == null) {
            Paint paint = new Paint(1);
            this.buttonBgPaint = paint;
            paint.setColor(1073741824);
        }
        this.chatListView.invalidate();
        for (int i = 0; i < this.messageObjects.size(); i++) {
            MessageObject messageObject = (MessageObject) this.messageObjects.get(i);
            if (messageObject != null && (message = messageObject.messageOwner) != null && (messageMedia = message.media) != null) {
                messageMedia.spoiler = j > 0;
            }
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            SpoilerEffect2.pause(0, true);
            super.show();
            prepareBlur(null);
            FrameLayout frameLayout = this.effectsView;
            if (frameLayout != null) {
                frameLayout.bringToFront();
            }
            animateOpenTo(true, null);
        }
    }

    public void showEffectSelector() {
        if (this.effectSelectorShown) {
            return;
        }
        this.layoutDone = false;
        this.effectSelectorShown = true;
        this.effectSelector.setMessage(null, null, true);
        this.effectSelector.animate().scaleY(1.0f).scaleX(1.0f).alpha(1.0f).setDuration(420L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
        this.effectSelector.startEnterAnimation(false);
    }

    public void updateColors() {
    }
}
