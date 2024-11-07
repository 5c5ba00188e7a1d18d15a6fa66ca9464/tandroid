package org.telegram.ui.Components.Reactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoaderPriorityQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.SlideIntChooseView;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;

/* loaded from: classes3.dex */
public class ChatCustomReactionsEditActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private UpdateReactionsButton actionButton;
    private FrameLayout actionButtonContainer;
    private ImageView actionButtonContainerGradient;
    private BackSpaceButtonView backSpaceButtonView;
    private TL_stories.TL_premium_boostsStatus boostsStatus;
    private FrameLayout bottomDialogLayout;
    private final long chatId;
    private LinearLayout contentLayout;
    private TLRPC.Chat currentChat;
    private int currentReactionsCount;
    private CustomReactionEditText editText;
    private TextCheckCell enableReactionsCell;
    private final TLRPC.ChatFull info;
    private boolean initialPaid;
    private boolean isPaused;
    private boolean paid;
    private TextCheckCell paidCheckCell;
    private int reactionsCount;
    private ScrollView scrollView;
    private SelectAnimatedEmojiDialog selectAnimatedEmojiDialog;
    private int selectedCustomReactions;
    private SlideIntChooseView slideView;
    private LinearLayout switchLayout;
    private final HashMap selectedEmojisMap = new LinkedHashMap();
    private final List selectedEmojisIds = new ArrayList();
    private final HashMap initialSelectedEmojis = new LinkedHashMap();
    private final List allAvailableReactions = new ArrayList();
    private final int maxReactionsCount = getMessagesController().boostsChannelLevelMax;
    private boolean emojiKeyboardVisible = false;
    private int selectedType = -1;
    private final Runnable checkAfterFastDeleteRunnable = new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda11
        @Override // java.lang.Runnable
        public final void run() {
            ChatCustomReactionsEditActivity.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 5 extends SelectAnimatedEmojiDialog {
        private boolean firstLayout;

        5(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, boolean z2, Theme.ResourcesProvider resourcesProvider, int i2, int i3) {
            super(baseFragment, context, z, num, i, z2, resourcesProvider, i2, i3);
            this.firstLayout = true;
            setDrawBackground(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmojiSelected$0(AnimatedEmojiSpan animatedEmojiSpan) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(ChatCustomReactionsEditActivity.this.editText.getText());
            for (AnimatedEmojiSpan animatedEmojiSpan2 : (AnimatedEmojiSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), AnimatedEmojiSpan.class)) {
                if (animatedEmojiSpan2 == animatedEmojiSpan) {
                    int editTextSelectionEnd = ChatCustomReactionsEditActivity.this.editText.getEditTextSelectionEnd();
                    int spanEnd = spannableStringBuilder.getSpanEnd(animatedEmojiSpan2);
                    int spanStart = spannableStringBuilder.getSpanStart(animatedEmojiSpan2);
                    ChatCustomReactionsEditActivity.this.editText.getText().delete(spanStart, spanEnd);
                    int i = spanEnd - spanStart;
                    CustomReactionEditText customReactionEditText = ChatCustomReactionsEditActivity.this.editText;
                    if (spanEnd <= editTextSelectionEnd) {
                        editTextSelectionEnd -= i;
                    }
                    customReactionEditText.setSelection(editTextSelectionEnd);
                    return;
                }
            }
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        protected void onEmojiSelected(View view, Long l, TLRPC.Document document, Integer num) {
            if (ChatCustomReactionsEditActivity.this.selectedEmojisMap.containsKey(l)) {
                ChatCustomReactionsEditActivity.this.selectedEmojisIds.remove(l);
                final AnimatedEmojiSpan animatedEmojiSpan = (AnimatedEmojiSpan) ChatCustomReactionsEditActivity.this.selectedEmojisMap.remove(l);
                animatedEmojiSpan.setRemoved(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatCustomReactionsEditActivity.5.this.lambda$onEmojiSelected$0(animatedEmojiSpan);
                    }
                });
                ChatCustomReactionsEditActivity.this.animateChangesInNextRows(animatedEmojiSpan);
                ChatCustomReactionsEditActivity.this.selectAnimatedEmojiDialog.setMultiSelected(l, true);
                ChatCustomReactionsEditActivity.this.checkMaxCustomReactions(false);
                return;
            }
            if (ChatCustomReactionsEditActivity.this.selectedEmojisMap.size() - (ChatCustomReactionsEditActivity.this.selectedEmojisMap.containsKey(-1L) ? 1 : 0) >= ChatCustomReactionsEditActivity.this.maxReactionsCount) {
                BulletinFactory.of(ChatCustomReactionsEditActivity.this).createErrorBulletin(LocaleController.formatPluralString("ReactionMaxCountError", ChatCustomReactionsEditActivity.this.maxReactionsCount, new Object[0])).show();
                return;
            }
            try {
                int editTextSelectionEnd = ChatCustomReactionsEditActivity.this.editText.getEditTextSelectionEnd();
                SpannableString spannableString = new SpannableString("b");
                AnimatedEmojiSpan createAnimatedEmojiSpan = ReactionsUtils.createAnimatedEmojiSpan(document, l, ChatCustomReactionsEditActivity.this.editText.getFontMetricsInt());
                createAnimatedEmojiSpan.cacheType = AnimatedEmojiDrawable.getCacheTypeForEnterView();
                createAnimatedEmojiSpan.setAdded();
                ChatCustomReactionsEditActivity.this.selectedEmojisIds.add(editTextSelectionEnd, l);
                ChatCustomReactionsEditActivity.this.selectedEmojisMap.put(l, createAnimatedEmojiSpan);
                spannableString.setSpan(createAnimatedEmojiSpan, 0, spannableString.length(), 33);
                ChatCustomReactionsEditActivity.this.editText.getText().insert(editTextSelectionEnd, spannableString);
                ChatCustomReactionsEditActivity.this.editText.setSelection(editTextSelectionEnd + spannableString.length());
                ChatCustomReactionsEditActivity.this.selectAnimatedEmojiDialog.setMultiSelected(l, true);
                ChatCustomReactionsEditActivity.this.checkMaxCustomReactions(true);
                ChatCustomReactionsEditActivity.this.animateChangesInNextRows(createAnimatedEmojiSpan);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.SelectAnimatedEmojiDialog, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (this.firstLayout) {
                this.firstLayout = false;
                ChatCustomReactionsEditActivity.this.selectAnimatedEmojiDialog.onShow(null);
            }
        }
    }

    public ChatCustomReactionsEditActivity(long j, TLRPC.ChatFull chatFull) {
        this.chatId = j;
        this.info = chatFull;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateChangesInNextRows(AnimatedEmojiSpan animatedEmojiSpan) {
        Editable text = this.editText.getText();
        Layout layout = this.editText.getLayout();
        int lineForOffset = layout.getLineForOffset(text.getSpanStart(animatedEmojiSpan)) + 1;
        if (lineForOffset < layout.getLineCount()) {
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) text.getSpans(layout.getLineStart(lineForOffset), text.length(), AnimatedEmojiSpan.class);
            for (AnimatedEmojiSpan animatedEmojiSpan2 : animatedEmojiSpanArr) {
                animatedEmojiSpan2.setAnimateChanges();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkChangesBeforeExit() {
        boolean z = !this.selectedEmojisMap.keySet().equals(this.initialSelectedEmojis.keySet());
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        if (tL_premium_boostsStatus != null && tL_premium_boostsStatus.level < this.selectedCustomReactions) {
            z = false;
        }
        boolean z2 = this.initialPaid == this.paid ? z : true;
        if (z2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), getResourceProvider());
            builder.setTitle(LocaleController.getString("UnsavedChanges", R.string.UnsavedChanges));
            builder.setMessage(LocaleController.getString("ReactionApplyChangesDialog", R.string.ReactionApplyChangesDialog));
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", R.string.ApplyTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatCustomReactionsEditActivity.this.lambda$checkChangesBeforeExit$14(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Discard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatCustomReactionsEditActivity.this.lambda$checkChangesBeforeExit$15(dialogInterface, i);
                }
            });
            builder.show();
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMaxCustomReactions(boolean z) {
        if (this.boostsStatus == null) {
            return;
        }
        if (this.selectedType == 0) {
            this.selectedType = 1;
        }
        int size = grabReactions(true).size();
        this.selectedCustomReactions = size;
        if (this.boostsStatus.level >= size) {
            this.actionButton.removeLvlRequiredState();
            return;
        }
        if (z) {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.chats_infotip, AndroidUtilities.replaceTags(LocaleController.formatPluralString("ReactionReachLvlForReactionShort", size, Integer.valueOf(size)))).show();
        }
        this.actionButton.setLvlRequiredState(this.selectedCustomReactions);
    }

    private boolean closeKeyboard() {
        if (!this.emojiKeyboardVisible) {
            return false;
        }
        this.emojiKeyboardVisible = false;
        if (isClearFocusNotWorking()) {
            this.switchLayout.setFocusableInTouchMode(true);
            this.switchLayout.requestFocus();
        } else {
            this.editText.clearFocus();
        }
        updateScrollViewMarginBottom(0);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        this.bottomDialogLayout.animate().setListener(null).cancel();
        this.bottomDialogLayout.animate().translationY(this.bottomDialogLayout.getMeasuredHeight()).setDuration(350L).withLayer().setInterpolator(CubicBezierInterpolator.DEFAULT).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatCustomReactionsEditActivity.this.lambda$closeKeyboard$17(valueAnimator);
            }
        }).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
                ChatCustomReactionsEditActivity.this.bottomDialogLayout.setVisibility(4);
                if (ChatCustomReactionsEditActivity.this.isClearFocusNotWorking()) {
                    ChatCustomReactionsEditActivity.this.switchLayout.setFocusableInTouchMode(false);
                }
            }
        }).start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean deleteSelectedEmojis() {
        int editTextSelectionEnd = this.editText.getEditTextSelectionEnd();
        int editTextSelectionStart = this.editText.getEditTextSelectionStart();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.editText.getText());
        if (!this.editText.hasSelection()) {
            return false;
        }
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannableStringBuilder.getSpans(editTextSelectionStart, editTextSelectionEnd, AnimatedEmojiSpan.class);
        for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
            this.selectedEmojisMap.remove(Long.valueOf(animatedEmojiSpan.documentId));
            this.selectedEmojisIds.remove(Long.valueOf(animatedEmojiSpan.documentId));
            this.selectAnimatedEmojiDialog.unselect(Long.valueOf(animatedEmojiSpan.documentId));
        }
        this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
        checkMaxCustomReactions(false);
        return true;
    }

    private List grabReactions(boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Long l : this.selectedEmojisIds) {
            if (l.longValue() != -1) {
                Iterator it = this.allAvailableReactions.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                        tL_reactionCustomEmoji.document_id = l.longValue();
                        arrayList.add(tL_reactionCustomEmoji);
                        arrayList2.add(tL_reactionCustomEmoji);
                        break;
                    }
                    TLRPC.TL_availableReaction tL_availableReaction = (TLRPC.TL_availableReaction) it.next();
                    if (l.longValue() == tL_availableReaction.activate_animation.id) {
                        TLRPC.TL_reactionEmoji tL_reactionEmoji = new TLRPC.TL_reactionEmoji();
                        tL_reactionEmoji.emoticon = tL_availableReaction.reaction;
                        arrayList.add(tL_reactionEmoji);
                        break;
                    }
                }
            }
        }
        return z ? arrayList2 : arrayList;
    }

    private void initSelectAnimatedEmojiDialog() {
        if (this.selectAnimatedEmojiDialog != null) {
            return;
        }
        5 r0 = new 5(this, getContext(), false, null, 6, false, getResourceProvider(), 16, Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, getResourceProvider()));
        this.selectAnimatedEmojiDialog = r0;
        r0.setAnimationsEnabled(false);
        this.selectAnimatedEmojiDialog.setClipChildren(false);
        this.selectAnimatedEmojiDialog.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.bottomDialogLayout.addView(this.selectAnimatedEmojiDialog, LayoutHelper.createFrame(-1, -2, 80));
        BackSpaceButtonView backSpaceButtonView = new BackSpaceButtonView(getContext(), getResourceProvider());
        this.backSpaceButtonView = backSpaceButtonView;
        backSpaceButtonView.setOnBackspace(new Utilities.Callback() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ChatCustomReactionsEditActivity.this.lambda$initSelectAnimatedEmojiDialog$11((Boolean) obj);
            }
        });
        this.bottomDialogLayout.addView(this.backSpaceButtonView, LayoutHelper.createFrame(-1, -2.0f, 85, 0.0f, 0.0f, 8.0f, 8.0f));
        Iterator it = this.selectedEmojisIds.iterator();
        while (it.hasNext()) {
            this.selectAnimatedEmojiDialog.setMultiSelected((Long) it.next(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isClearFocusNotWorking() {
        return Build.MODEL.toLowerCase().startsWith("zte") && Build.VERSION.SDK_INT <= 28;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkChangesBeforeExit$14(DialogInterface dialogInterface, int i) {
        this.actionButton.performClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkChangesBeforeExit$15(DialogInterface dialogInterface, int i) {
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeKeyboard$17(ValueAnimator valueAnimator) {
        this.actionButtonContainer.setTranslationY((-(1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue())) * this.bottomDialogLayout.getMeasuredHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        TextCheckCell textCheckCell;
        if (this.enableReactionsCell.isChecked() && (textCheckCell = this.paidCheckCell) != null && textCheckCell.isChecked()) {
            toggleStarsEnabled();
        }
        setCheckedEnableReactionCell(this.enableReactionsCell.isChecked() ? 2 : 1, this.enableReactionsCell.isChecked() ? false : this.paid, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3() {
        Browser.openUrl(getContext(), "https://t.me/stickers");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(Integer num) {
        this.reactionsCount = num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        toggleStarsEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        Browser.openUrl(getContext(), LocaleController.getString(R.string.ChannelEnablePaidReactionsInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(TLRPC.TL_error tL_error) {
        if (this.boostsStatus != null && tL_error.text.equals("BOOSTS_REQUIRED")) {
            ReactionsUtils.showLimitReachedDialogForReactions(-this.chatId, this.selectedCustomReactions, this.boostsStatus);
            return;
        }
        String str = tL_error.text;
        if (str.equals("REACTIONS_TOO_MANY")) {
            str = LocaleController.formatPluralString("ReactionMaxCountError", this.maxReactionsCount, new Object[0]);
        }
        BulletinFactory.of(this).createErrorBulletin(str).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(final TLRPC.TL_error tL_error) {
        if (isFinishing()) {
            return;
        }
        this.actionButton.setLoading(false);
        if (tL_error.text.equals("CHAT_NOT_MODIFIED")) {
            lambda$onBackPressed$321();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    ChatCustomReactionsEditActivity.this.lambda$createView$7(tL_error);
                }
            }, this.boostsStatus == null ? 200L : 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view) {
        if (this.actionButton.isLoading()) {
            return;
        }
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        if (tL_premium_boostsStatus != null) {
            int i = tL_premium_boostsStatus.level;
            int i2 = this.selectedCustomReactions;
            if (i < i2) {
                ReactionsUtils.showLimitReachedDialogForReactions(-this.chatId, i2, tL_premium_boostsStatus);
                return;
            }
        }
        TextCheckCell textCheckCell = this.paidCheckCell;
        Boolean valueOf = (textCheckCell == null || !this.info.paid_media_allowed) ? null : Boolean.valueOf(textCheckCell.isChecked());
        this.actionButton.setLoading(true);
        MessagesController messagesController = getMessagesController();
        long j = this.chatId;
        int i3 = this.selectedType;
        List<TLRPC.Reaction> grabReactions = grabReactions(false);
        int i4 = this.reactionsCount;
        this.currentReactionsCount = i4;
        messagesController.setCustomChatReactions(j, i3, grabReactions, i4, valueOf, new Utilities.Callback() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda17
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ChatCustomReactionsEditActivity.this.lambda$createView$8((TLRPC.TL_error) obj);
            }
        }, new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                ChatCustomReactionsEditActivity.this.lambda$onBackPressed$321();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initSelectAnimatedEmojiDialog$10(AnimatedEmojiSpan animatedEmojiSpan, int i) {
        Editable text = this.editText.getText();
        int spanStart = text.getSpanStart(animatedEmojiSpan);
        int spanEnd = text.getSpanEnd(animatedEmojiSpan);
        int i2 = spanEnd - spanStart;
        if (spanStart == -1 || spanEnd == -1) {
            return;
        }
        this.editText.getText().delete(spanStart, spanEnd);
        CustomReactionEditText customReactionEditText = this.editText;
        customReactionEditText.setSelection(Math.min(i - i2, customReactionEditText.getText().length()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initSelectAnimatedEmojiDialog$11(Boolean bool) {
        TextCheckCell textCheckCell;
        if (deleteSelectedEmojis()) {
            return;
        }
        final int editTextSelectionEnd = this.editText.getEditTextSelectionEnd();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.editText.getText());
        for (final AnimatedEmojiSpan animatedEmojiSpan : (AnimatedEmojiSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), AnimatedEmojiSpan.class)) {
            if (spannableStringBuilder.getSpanEnd(animatedEmojiSpan) == editTextSelectionEnd) {
                this.selectedEmojisMap.remove(Long.valueOf(animatedEmojiSpan.documentId));
                this.selectedEmojisIds.remove(Long.valueOf(animatedEmojiSpan.documentId));
                this.selectAnimatedEmojiDialog.unselect(Long.valueOf(animatedEmojiSpan.documentId));
                if (animatedEmojiSpan.documentId == -1 && (textCheckCell = this.paidCheckCell) != null) {
                    textCheckCell.setChecked(false);
                    this.editText.setMaxLength(this.maxReactionsCount);
                }
                if (bool.booleanValue()) {
                    this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
                    AndroidUtilities.cancelRunOnUIThread(this.checkAfterFastDeleteRunnable);
                    AndroidUtilities.runOnUIThread(this.checkAfterFastDeleteRunnable, 350L);
                    return;
                } else {
                    animatedEmojiSpan.setRemoved(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda19
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatCustomReactionsEditActivity.this.lambda$initSelectAnimatedEmojiDialog$10(animatedEmojiSpan, editTextSelectionEnd);
                        }
                    });
                    animateChangesInNextRows(animatedEmojiSpan);
                    checkMaxCustomReactions(false);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        checkMaxCustomReactions(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFragmentCreate$1(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        this.boostsStatus = tL_premium_boostsStatus;
        if (!this.selectedEmojisMap.keySet().equals(this.initialSelectedEmojis.keySet())) {
            checkMaxCustomReactions(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$13() {
        this.editText.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onTransitionAnimationEnd$12() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showKeyboard$16(ValueAnimator valueAnimator) {
        this.actionButtonContainer.setTranslationY((-((Float) valueAnimator.getAnimatedValue()).floatValue()) * this.bottomDialogLayout.getMeasuredHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStarsEnabled$18(AnimatedEmojiSpan animatedEmojiSpan) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.editText.getText());
        for (AnimatedEmojiSpan animatedEmojiSpan2 : (AnimatedEmojiSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), AnimatedEmojiSpan.class)) {
            if (animatedEmojiSpan2 == animatedEmojiSpan) {
                int editTextSelectionEnd = this.editText.getEditTextSelectionEnd();
                int spanEnd = spannableStringBuilder.getSpanEnd(animatedEmojiSpan2);
                int spanStart = spannableStringBuilder.getSpanStart(animatedEmojiSpan2);
                this.editText.getText().delete(spanStart, spanEnd);
                int i = spanEnd - spanStart;
                CustomReactionEditText customReactionEditText = this.editText;
                if (spanEnd <= editTextSelectionEnd) {
                    editTextSelectionEnd -= i;
                }
                customReactionEditText.setSelection(editTextSelectionEnd);
                return;
            }
        }
    }

    private void setCheckedEnableReactionCell(int i, boolean z, boolean z2) {
        if (this.selectedType == i && this.paid == z) {
            return;
        }
        this.paid = z;
        boolean z3 = i == 1 || i == 0 || z;
        this.enableReactionsCell.setChecked(z3);
        int color = Theme.getColor(z3 ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked);
        if (!z2) {
            this.enableReactionsCell.setBackgroundColor(color);
        } else if (z3) {
            this.enableReactionsCell.setBackgroundColorAnimated(true, color);
        } else {
            this.enableReactionsCell.setBackgroundColorAnimatedReverse(color);
        }
        this.selectedType = i;
        if (i != 1 && i != 0 && !z) {
            if (!z2) {
                this.switchLayout.setVisibility(4);
                this.actionButtonContainer.setVisibility(4);
                return;
            }
            closeKeyboard();
            this.actionButtonContainer.animate().setListener(null).cancel();
            this.switchLayout.animate().setListener(null).cancel();
            ViewPropertyAnimator duration = this.actionButtonContainer.animate().alpha(0.0f).setDuration(350L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            duration.setInterpolator(cubicBezierInterpolator).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatCustomReactionsEditActivity.this.actionButtonContainer.setVisibility(4);
                }
            }).start();
            this.switchLayout.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatCustomReactionsEditActivity.this.editText.setFocusableInTouchMode(false);
                    ChatCustomReactionsEditActivity.this.switchLayout.setVisibility(4);
                }
            }).start();
            return;
        }
        this.switchLayout.setVisibility(0);
        this.actionButtonContainer.setVisibility(0);
        if (z2) {
            this.actionButtonContainer.animate().setListener(null).cancel();
            this.switchLayout.animate().setListener(null).cancel();
            ViewPropertyAnimator duration2 = this.switchLayout.animate().alpha(1.0f).setDuration(350L);
            CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
            duration2.setInterpolator(cubicBezierInterpolator2).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatCustomReactionsEditActivity.this.editText.setFocusableInTouchMode(true);
                }
            }).start();
            this.actionButtonContainer.animate().alpha(1.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator2).start();
            if (this.selectedEmojisMap.isEmpty()) {
                this.selectAnimatedEmojiDialog.clearSelectedDocuments();
                this.editText.setText("");
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                Iterator it = this.allAvailableReactions.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    ReactionsUtils.addReactionToEditText((TLRPC.TL_availableReaction) it.next(), this.selectedEmojisMap, this.selectedEmojisIds, spannableStringBuilder, this.selectAnimatedEmojiDialog, this.editText.getFontMetricsInt());
                    i2++;
                    if (i2 >= this.maxReactionsCount) {
                        break;
                    }
                }
                this.editText.append(spannableStringBuilder);
                this.editText.addReactionsSpan();
                this.selectAnimatedEmojiDialog.notifyDataSetChanged();
                checkMaxCustomReactions(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboard() {
        if (this.emojiKeyboardVisible) {
            return;
        }
        this.emojiKeyboardVisible = true;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        updateScrollViewMarginBottom(this.bottomDialogLayout.getMeasuredHeight());
        this.bottomDialogLayout.setVisibility(0);
        this.bottomDialogLayout.setTranslationY(r0.getMeasuredHeight());
        this.bottomDialogLayout.animate().setListener(null).cancel();
        this.bottomDialogLayout.animate().translationY(0.0f).withLayer().setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda14
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatCustomReactionsEditActivity.this.lambda$showKeyboard$16(valueAnimator);
            }
        }).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScrollViewMarginBottom(int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.scrollView.getLayoutParams();
        marginLayoutParams.bottomMargin = i;
        this.scrollView.setLayoutParams(marginLayoutParams);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        if (checkChangesBeforeExit()) {
            return false;
        }
        return super.canBeginSlide();
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x03ab A[EDGE_INSN: B:51:0x03ab->B:52:0x03ab BREAK  A[LOOP:1: B:40:0x0352->B:54:0x0352], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0352 A[SYNTHETIC] */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        this.actionBar.setTitle(LocaleController.getString(R.string.Reactions));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        boolean z = true;
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i != -1 || ChatCustomReactionsEditActivity.this.checkChangesBeforeExit()) {
                    return;
                }
                ChatCustomReactionsEditActivity.this.lambda$onBackPressed$321();
            }
        });
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.2
            final AdjustPanLayoutHelper adjustPanLayoutHelper = new 1(this);

            /* JADX INFO: Access modifiers changed from: package-private */
            /* loaded from: classes3.dex */
            public class 1 extends AdjustPanLayoutHelper {
                1(View view) {
                    super(view);
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$onTransitionStart$0(boolean z) {
                    if (z) {
                        ChatCustomReactionsEditActivity.this.actionButtonContainer.setVisibility(4);
                    }
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                protected boolean applyTranslation() {
                    return false;
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                protected boolean heightAnimationEnabled() {
                    return (((BaseFragment) ChatCustomReactionsEditActivity.this).inPreviewMode || AndroidUtilities.isTablet() || ((BaseFragment) ChatCustomReactionsEditActivity.this).inBubbleMode || AndroidUtilities.isInMultiwindow || ChatCustomReactionsEditActivity.this.getParentLayout() == null) ? false : true;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onPanTranslationUpdate(float f, float f2, boolean z) {
                    if (ChatCustomReactionsEditActivity.this.getParentLayout() != null) {
                        ChatCustomReactionsEditActivity.this.getParentLayout().isPreviewOpenAnimationInProgress();
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onTransitionEnd() {
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onTransitionStart(final boolean z, int i) {
                    ChatCustomReactionsEditActivity.this.actionButtonContainer.setVisibility(0);
                    ChatCustomReactionsEditActivity.this.actionButtonContainer.animate().alpha(!z ? 1.0f : 0.0f).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$2$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatCustomReactionsEditActivity.2.1.this.lambda$onTransitionStart$0(z);
                        }
                    }).start();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                this.adjustPanLayoutHelper.onAttach();
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                this.adjustPanLayoutHelper.onDetach();
            }
        };
        LinearLayout linearLayout = new LinearLayout(context);
        this.contentLayout = linearLayout;
        linearLayout.setOrientation(1);
        this.scrollView.addView(this.contentLayout);
        TextCheckCell textCheckCell = new TextCheckCell(context);
        this.enableReactionsCell = textCheckCell;
        textCheckCell.setHeight(56);
        TextCheckCell textCheckCell2 = this.enableReactionsCell;
        textCheckCell2.setBackgroundColor(Theme.getColor(textCheckCell2.isChecked() ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        this.enableReactionsCell.setTypeface(AndroidUtilities.bold());
        this.enableReactionsCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
        this.enableReactionsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatCustomReactionsEditActivity.this.lambda$createView$2(view);
            }
        });
        this.contentLayout.addView(this.enableReactionsCell, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        int i = Theme.key_windowBackgroundWhiteGrayText4;
        textInfoPrivacyCell.setTextColor(Theme.getColor(i));
        textInfoPrivacyCell.setTopPadding(12);
        textInfoPrivacyCell.setBottomPadding(16);
        textInfoPrivacyCell.setText(LocaleController.getString(R.string.ReactionAddEmojiFromAnyPack));
        this.contentLayout.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell = new HeaderCell(context);
        headerCell.setText(LocaleController.getString(R.string.AvailableReactions));
        int i2 = Theme.key_windowBackgroundWhite;
        headerCell.setBackgroundColor(Theme.getColor(i2));
        headerCell.setTextSize(15.0f);
        headerCell.setTopMargin(14);
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.switchLayout = linearLayout2;
        linearLayout2.setOrientation(1);
        this.contentLayout.addView(this.switchLayout, LayoutHelper.createFrame(-1, -2.0f));
        this.switchLayout.addView(headerCell, LayoutHelper.createLinear(-1, -2));
        CustomReactionEditText customReactionEditText = new CustomReactionEditText(context, getResourceProvider(), this.maxReactionsCount) { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.3
            @Override // org.telegram.ui.Components.EditTextCaption
            protected void onLineCountChanged(int i3, int i4) {
                if (i4 > i3) {
                    ChatCustomReactionsEditActivity.this.scrollView.smoothScrollBy(0, AndroidUtilities.dp(30.0f));
                }
            }

            @Override // org.telegram.ui.Components.EditTextCaption, android.widget.EditText, android.widget.TextView
            public boolean onTextContextMenuItem(int i3) {
                if (i3 == R.id.menu_delete || i3 == 16908320) {
                    return ChatCustomReactionsEditActivity.this.deleteSelectedEmojis();
                }
                if (i3 == 16908322 || i3 == 16908321) {
                    return false;
                }
                return super.onTextContextMenuItem(i3);
            }
        };
        this.editText = customReactionEditText;
        customReactionEditText.setOnFocused(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ChatCustomReactionsEditActivity.this.showKeyboard();
            }
        });
        this.switchLayout.addView(this.editText, LayoutHelper.createLinear(-1, -2));
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200L);
        layoutTransition.enableTransitionType(4);
        this.switchLayout.setLayoutTransition(layoutTransition);
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        textInfoPrivacyCell2.setTextColor(Theme.getColor(i));
        textInfoPrivacyCell2.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ReactionCreateOwnPack), Theme.key_chat_messageLinkIn, 0, new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ChatCustomReactionsEditActivity.this.lambda$createView$3();
            }
        }, getResourceProvider()));
        this.switchLayout.addView(textInfoPrivacyCell2, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell2 = new HeaderCell(context, this.resourceProvider);
        headerCell2.setBackgroundColor(getThemedColor(i2));
        headerCell2.setText(LocaleController.getString(R.string.MaximumReactionsHeader));
        this.switchLayout.addView(headerCell2, LayoutHelper.createLinear(-1, -2));
        SlideIntChooseView slideIntChooseView = new SlideIntChooseView(context, this.resourceProvider);
        this.slideView = slideIntChooseView;
        slideIntChooseView.setBackgroundColor(getThemedColor(i2));
        TLRPC.ChatFull chatFull = this.info;
        int i3 = (!(chatFull instanceof TLRPC.TL_chatFull) ? (chatFull.flags2 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 : (chatFull.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) ? getMessagesController().reactionsUniqMax : chatFull.reactions_limit;
        this.reactionsCount = i3;
        this.currentReactionsCount = i3;
        this.slideView.set(this.reactionsCount, SlideIntChooseView.Options.make(0, "MaximumReactionsValue", 1, getMessagesController().reactionsUniqMax), new Utilities.Callback() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ChatCustomReactionsEditActivity.this.lambda$createView$4((Integer) obj);
            }
        });
        this.switchLayout.addView(this.slideView, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
        textInfoPrivacyCell3.setTopPadding(12);
        textInfoPrivacyCell3.setBottomPadding(16);
        textInfoPrivacyCell3.setText(LocaleController.getString(R.string.MaximumReactionsInfo));
        this.switchLayout.addView(textInfoPrivacyCell3, LayoutHelper.createLinear(-1, -2));
        if (this.info.paid_media_allowed) {
            TextCheckCell textCheckCell3 = new TextCheckCell(context);
            this.paidCheckCell = textCheckCell3;
            textCheckCell3.setBackgroundColor(getThemedColor(i2));
            this.paidCheckCell.setTextAndCheck(LocaleController.getString(R.string.ChannelEnablePaidReactions), false, false);
            this.switchLayout.addView(this.paidCheckCell, LayoutHelper.createLinear(-1, -2));
            this.paidCheckCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChatCustomReactionsEditActivity.this.lambda$createView$5(view);
                }
            });
            TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
            textInfoPrivacyCell4.setTextColor(Theme.getColor(i));
            textInfoPrivacyCell4.setTopPadding(12);
            textInfoPrivacyCell4.setBottomPadding(70);
            textInfoPrivacyCell4.setText(AndroidUtilities.withLearnMore(LocaleController.getString(R.string.ChannelEnablePaidReactionsInfo), new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ChatCustomReactionsEditActivity.this.lambda$createView$6();
                }
            }));
            this.switchLayout.addView(textInfoPrivacyCell4, LayoutHelper.createLinear(-1, -2));
        } else {
            textInfoPrivacyCell3.setBottomPadding(70);
        }
        this.actionButtonContainer = new FrameLayout(context);
        ImageView imageView = new ImageView(context);
        this.actionButtonContainerGradient = imageView;
        imageView.setImageResource(R.drawable.gradient_bottom);
        this.actionButtonContainerGradient.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageView imageView2 = this.actionButtonContainerGradient;
        int i4 = Theme.key_windowBackgroundGray;
        imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i4, this.resourceProvider), PorterDuff.Mode.SRC_ATOP));
        this.actionButtonContainer.addView(this.actionButtonContainerGradient, LayoutHelper.createFrame(-1, -1, 119));
        UpdateReactionsButton updateReactionsButton = new UpdateReactionsButton(context, getResourceProvider());
        this.actionButton = updateReactionsButton;
        updateReactionsButton.setDefaultState();
        this.actionButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatCustomReactionsEditActivity.this.lambda$createView$9(view);
            }
        });
        frameLayout.addView(this.scrollView);
        frameLayout.addView(this.actionButtonContainer, LayoutHelper.createFrame(-1, 74.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        this.actionButtonContainer.addView(this.actionButton, LayoutHelper.createFrame(-1, 48.0f, 80, 13.0f, 13.0f, 13.0f, 13.0f));
        frameLayout.setBackgroundColor(Theme.getColor(i4));
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.4
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i5, int i6, int i7, int i8) {
                super.onLayout(z2, i5, i6, i7, i8);
                if (ChatCustomReactionsEditActivity.this.emojiKeyboardVisible && z2) {
                    ChatCustomReactionsEditActivity.this.actionButtonContainer.setTranslationY(-ChatCustomReactionsEditActivity.this.bottomDialogLayout.getMeasuredHeight());
                    ChatCustomReactionsEditActivity chatCustomReactionsEditActivity = ChatCustomReactionsEditActivity.this;
                    chatCustomReactionsEditActivity.updateScrollViewMarginBottom(chatCustomReactionsEditActivity.bottomDialogLayout.getMeasuredHeight());
                }
            }
        };
        this.bottomDialogLayout = frameLayout2;
        frameLayout2.setVisibility(4);
        frameLayout.addView(this.bottomDialogLayout, LayoutHelper.createFrame(-1, -2, 80));
        TLRPC.ChatFull chatFull2 = this.info;
        TLRPC.ChatReactions chatReactions = chatFull2.available_reactions;
        if (chatReactions instanceof TLRPC.TL_chatReactionsAll) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            Iterator it = this.allAvailableReactions.iterator();
            int i5 = 0;
            while (it.hasNext()) {
                ReactionsUtils.addReactionToEditText((TLRPC.TL_availableReaction) it.next(), this.selectedEmojisMap, this.selectedEmojisIds, spannableStringBuilder, this.selectAnimatedEmojiDialog, this.editText.getFontMetricsInt());
                i5++;
                if (i5 >= this.maxReactionsCount) {
                    break;
                }
            }
            this.editText.append(spannableStringBuilder);
            setCheckedEnableReactionCell(0, this.paid, false);
        } else if (chatReactions instanceof TLRPC.TL_chatReactionsSome) {
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
            Iterator<TLRPC.Reaction> it2 = ((TLRPC.TL_chatReactionsSome) chatReactions).reactions.iterator();
            int i6 = 0;
            while (it2.hasNext()) {
                TLRPC.Reaction next = it2.next();
                if (next instanceof TLRPC.TL_reactionEmoji) {
                    TLRPC.TL_availableReaction tL_availableReaction = getMediaDataController().getReactionsMap().get(((TLRPC.TL_reactionEmoji) next).emoticon);
                    if (tL_availableReaction == null) {
                        continue;
                    } else {
                        ReactionsUtils.addReactionToEditText(tL_availableReaction, this.selectedEmojisMap, this.selectedEmojisIds, spannableStringBuilder2, this.selectAnimatedEmojiDialog, this.editText.getFontMetricsInt());
                    }
                } else {
                    if (next instanceof TLRPC.TL_reactionCustomEmoji) {
                        ReactionsUtils.addReactionToEditText((TLRPC.TL_reactionCustomEmoji) next, this.selectedEmojisMap, this.selectedEmojisIds, spannableStringBuilder2, this.selectAnimatedEmojiDialog, this.editText.getFontMetricsInt());
                    }
                    if (i6 < this.maxReactionsCount) {
                        break;
                    }
                }
                i6++;
                if (i6 < this.maxReactionsCount) {
                }
            }
            this.editText.append(spannableStringBuilder2);
            setCheckedEnableReactionCell(1, this.paid, false);
        } else {
            boolean z2 = chatReactions instanceof TLRPC.TL_chatReactionsNone;
            if (!z2 || !chatFull2.paid_media_allowed || !chatFull2.paid_reactions_available) {
                if (z2) {
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
                    Iterator it3 = this.allAvailableReactions.iterator();
                    int i7 = 0;
                    while (it3.hasNext()) {
                        ReactionsUtils.addReactionToEditText((TLRPC.TL_availableReaction) it3.next(), this.selectedEmojisMap, this.selectedEmojisIds, spannableStringBuilder3, this.selectAnimatedEmojiDialog, this.editText.getFontMetricsInt());
                        i7++;
                        if (i7 >= this.maxReactionsCount) {
                            break;
                        }
                    }
                    this.editText.append(spannableStringBuilder3);
                }
            }
            setCheckedEnableReactionCell(2, this.paid, false);
        }
        TextCheckCell textCheckCell4 = this.enableReactionsCell;
        String string = LocaleController.getString(R.string.EnableReactions);
        if (this.selectedType == 2 && !this.paid) {
            z = false;
        }
        textCheckCell4.setTextAndCheck(string, z, false);
        this.editText.addReactionsSpan();
        TLRPC.ChatFull chatFull3 = this.info;
        if (chatFull3.paid_media_allowed && chatFull3.paid_reactions_available) {
            toggleStarsEnabled();
        }
        this.initialSelectedEmojis.putAll(this.selectedEmojisMap);
        this.initialPaid = this.paid;
        this.fragmentView = frameLayout;
        return frameLayout;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (closeKeyboard() || checkChangesBeforeExit()) {
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        if (chat == null) {
            TLRPC.Chat chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(this.chatId);
            this.currentChat = chatSync;
            if (chatSync == null) {
                return false;
            }
            getMessagesController().putChat(this.currentChat, true);
        }
        if (this.info == null) {
            return false;
        }
        getMessagesController().getBoostsController().getBoostsStats(-this.chatId, new Consumer() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda13
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ChatCustomReactionsEditActivity.this.lambda$onFragmentCreate$1((TL_stories.TL_premium_boostsStatus) obj);
            }
        });
        getNotificationCenter().addObserver(this, NotificationCenter.reactionsDidLoad);
        this.allAvailableReactions.addAll(getMediaDataController().getEnabledReactionsList());
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        AndroidUtilities.cancelRunOnUIThread(this.checkAfterFastDeleteRunnable);
        if (this.selectedType != 2 || this.reactionsCount == this.currentReactionsCount) {
            return;
        }
        getMessagesController().setCustomChatReactions(this.chatId, this.selectedType, grabReactions(false), this.reactionsCount, null, null, null);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        this.isPaused = true;
        this.editText.setFocusable(false);
        super.onPause();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        if (this.isPaused) {
            this.isPaused = false;
            this.editText.setFocusable(true);
            this.editText.setFocusableInTouchMode(true);
            if (this.emojiKeyboardVisible) {
                this.editText.removeReactionsSpan(false);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatCustomReactionsEditActivity.this.lambda$onResume$13();
                    }
                }, 250L);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        super.onTransitionAnimationEnd(z, z2);
        if (z && this.selectedType != 2) {
            this.editText.setFocusableInTouchMode(true);
        }
        if (!z || z2) {
            return;
        }
        initSelectAnimatedEmojiDialog();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ChatCustomReactionsEditActivity.lambda$onTransitionAnimationEnd$12();
            }
        }, 200L);
    }

    public void toggleStarsEnabled() {
        long j = -1;
        if (this.paidCheckCell.isChecked()) {
            this.paidCheckCell.setChecked(false);
            this.selectedEmojisIds.remove((Object) (-1L));
            final AnimatedEmojiSpan animatedEmojiSpan = (AnimatedEmojiSpan) this.selectedEmojisMap.remove(-1L);
            if (animatedEmojiSpan != null) {
                animatedEmojiSpan.setRemoved(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatCustomReactionsEditActivity.this.lambda$toggleStarsEnabled$18(animatedEmojiSpan);
                    }
                });
            }
            animateChangesInNextRows(animatedEmojiSpan);
            this.selectAnimatedEmojiDialog.setMultiSelected(-1L, true);
            checkMaxCustomReactions(false);
            this.editText.setMaxLength(this.maxReactionsCount);
            setCheckedEnableReactionCell(this.selectedType, this.paid, true);
        } else {
            this.paidCheckCell.setChecked(true);
            try {
                this.editText.setMaxLength(this.maxReactionsCount + 1);
                SpannableString spannableString = new SpannableString("b");
                AnimatedEmojiSpan animatedEmojiSpan2 = new AnimatedEmojiSpan(j, null) { // from class: org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity.11
                    private final Bitmap bitmap;

                    {
                        Bitmap createBitmap = Bitmap.createBitmap(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), Bitmap.Config.ARGB_8888);
                        this.bitmap = createBitmap;
                        Drawable mutate = ChatCustomReactionsEditActivity.this.getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate();
                        mutate.setBounds(0, 0, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                        mutate.draw(new Canvas(createBitmap));
                    }

                    @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
                    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
                        super.draw(canvas, charSequence, i, i2, f, i3, i4, i5, paint);
                        canvas.save();
                        canvas.translate(f, ((i3 + i5) / 2.0f) - AndroidUtilities.dp(12.0f));
                        float f2 = this.extraScale;
                        canvas.scale(f2, f2, f + AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f));
                        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, (Paint) null);
                        canvas.restore();
                    }

                    @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
                    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
                        return super.getSize(paint, charSequence, i, i2, fontMetricsInt) + AndroidUtilities.dp(5.0f);
                    }
                };
                animatedEmojiSpan2.cacheType = AnimatedEmojiDrawable.getCacheTypeForEnterView();
                animatedEmojiSpan2.setAdded();
                this.selectedEmojisIds.add(0, -1L);
                this.selectedEmojisMap.put(-1L, animatedEmojiSpan2);
                spannableString.setSpan(animatedEmojiSpan2, 0, spannableString.length(), 33);
                this.editText.getText().insert(0, spannableString);
                this.selectAnimatedEmojiDialog.setMultiSelected(-1L, true);
                checkMaxCustomReactions(true);
                animateChangesInNextRows(animatedEmojiSpan2);
            } catch (Exception e) {
                FileLog.e(e);
            }
            setCheckedEnableReactionCell(this.selectedType, true, true);
        }
        this.editText.updateAnimatedEmoji(true);
    }
}
