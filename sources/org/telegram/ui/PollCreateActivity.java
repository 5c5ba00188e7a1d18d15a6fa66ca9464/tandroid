package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.ChatActivityEnterViewAnimatedIconView;
import org.telegram.ui.Components.ChatAttachAlertPollLayout;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SuggestEmojiView;
import org.telegram.ui.Components.TrendingStickersLayout;
import org.telegram.ui.PollCreateActivity;
import org.telegram.ui.Stories.recorder.KeyboardNotifier;

/* loaded from: classes4.dex */
public class PollCreateActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate {
    private int addAnswerRow;
    private int anonymousRow;
    private int answerHeaderRow;
    private int answerSectionRow;
    private int answerStartRow;
    private PollEditTextCell currentCell;
    private PollCreateActivityDelegate delegate;
    private boolean destroyed;
    private ActionBarMenuItem doneItem;
    private int emojiPadding;
    private EmojiView emojiView;
    public boolean emojiViewVisible;
    public boolean emojiViewWasVisible;
    private boolean hintShowed;
    private HintView hintView;
    private boolean isAnimatePopupClosing;
    boolean isEmojiSearchOpened;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private KeyboardNotifier keyboardNotifier;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean multipleChoise;
    private int multipleRow;
    private ChatActivity parentFragment;
    private int questionHeaderRow;
    private int questionRow;
    private int questionSectionRow;
    private CharSequence questionString;
    private int quizOnly;
    private boolean quizPoll;
    private int quizRow;
    private int rowCount;
    private int settingsHeaderRow;
    private int settingsSectionRow;
    private SizeNotifierFrameLayout sizeNotifierFrameLayout;
    private int solutionInfoRow;
    private int solutionRow;
    private CharSequence solutionString;
    private SuggestEmojiView suggestEmojiPanel;
    private boolean waitingForKeyboardOpen;
    boolean wasEmojiSearchOpened;
    private CharSequence[] answers = new CharSequence[10];
    private boolean[] answersChecks = new boolean[10];
    private int answersCount = 1;
    private boolean anonymousPoll = true;
    private int requestFieldFocusAtPosition = -1;
    private Runnable openKeyboardRunnable = new Runnable() { // from class: org.telegram.ui.PollCreateActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (PollCreateActivity.this.currentCell != null) {
                EditTextBoldCursor editField = PollCreateActivity.this.currentCell.getEditField();
                if (PollCreateActivity.this.destroyed || editField == null || !PollCreateActivity.this.waitingForKeyboardOpen || PollCreateActivity.this.keyboardVisible || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow || !AndroidUtilities.isTablet()) {
                    return;
                }
                editField.requestFocus();
                AndroidUtilities.showKeyboard(editField);
                AndroidUtilities.cancelRunOnUIThread(PollCreateActivity.this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(PollCreateActivity.this.openKeyboardRunnable, 100L);
            }
        }
    };
    private boolean isPremium = AccountInstance.getInstance(this.currentAccount).getUserConfig().isPremium();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 extends ActionBar.ActionBarMenuOnItemClick {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
            PollCreateActivity.this.delegate.sendPoll(tL_messageMediaPoll, hashMap, z, i);
            PollCreateActivity.this.lambda$onBackPressed$319();
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i != -1) {
                if (i != 1) {
                    return;
                }
                if (PollCreateActivity.this.quizPoll && PollCreateActivity.this.doneItem.getAlpha() != 1.0f) {
                    int i2 = 0;
                    for (int i3 = 0; i3 < PollCreateActivity.this.answersChecks.length; i3++) {
                        if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i3])) && PollCreateActivity.this.answersChecks[i3]) {
                            i2++;
                        }
                    }
                    if (i2 <= 0) {
                        PollCreateActivity.this.showQuizHint();
                        return;
                    }
                    return;
                }
                CharSequence[] charSequenceArr = {ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.questionString)};
                ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(((BaseFragment) PollCreateActivity.this).currentAccount).getEntities(charSequenceArr, true);
                CharSequence charSequence = charSequenceArr[0];
                int size = entities.size();
                for (int i4 = 0; i4 < size; i4++) {
                    TLRPC.MessageEntity messageEntity = entities.get(i4);
                    if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                        messageEntity.length = charSequence.length() - messageEntity.offset;
                    }
                }
                final TLRPC.TL_messageMediaPoll tL_messageMediaPoll = new TLRPC.TL_messageMediaPoll();
                TLRPC.TL_poll tL_poll = new TLRPC.TL_poll();
                tL_messageMediaPoll.poll = tL_poll;
                tL_poll.multiple_choice = PollCreateActivity.this.multipleChoise;
                tL_messageMediaPoll.poll.quiz = PollCreateActivity.this.quizPoll;
                tL_messageMediaPoll.poll.public_voters = !PollCreateActivity.this.anonymousPoll;
                tL_messageMediaPoll.poll.question = new TLRPC.TL_textWithEntities();
                tL_messageMediaPoll.poll.question.text = charSequence.toString();
                tL_messageMediaPoll.poll.question.entities = entities;
                SerializedData serializedData = new SerializedData(10);
                for (int i5 = 0; i5 < PollCreateActivity.this.answers.length; i5++) {
                    if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i5]))) {
                        CharSequence[] charSequenceArr2 = {ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i5])};
                        ArrayList<TLRPC.MessageEntity> entities2 = MediaDataController.getInstance(((BaseFragment) PollCreateActivity.this).currentAccount).getEntities(charSequenceArr2, true);
                        CharSequence charSequence2 = charSequenceArr2[0];
                        int size2 = entities2.size();
                        for (int i6 = 0; i6 < size2; i6++) {
                            TLRPC.MessageEntity messageEntity2 = entities2.get(i6);
                            if (messageEntity2.offset + messageEntity2.length > charSequence2.length()) {
                                messageEntity2.length = charSequence2.length() - messageEntity2.offset;
                            }
                        }
                        TLRPC.TL_pollAnswer tL_pollAnswer = new TLRPC.TL_pollAnswer();
                        TLRPC.TL_textWithEntities tL_textWithEntities = new TLRPC.TL_textWithEntities();
                        tL_pollAnswer.text = tL_textWithEntities;
                        tL_textWithEntities.text = charSequence2.toString();
                        tL_pollAnswer.text.entities = entities2;
                        tL_pollAnswer.option = r4;
                        byte[] bArr = {(byte) (tL_messageMediaPoll.poll.answers.size() + 48)};
                        tL_messageMediaPoll.poll.answers.add(tL_pollAnswer);
                        if ((PollCreateActivity.this.multipleChoise || PollCreateActivity.this.quizPoll) && PollCreateActivity.this.answersChecks[i5]) {
                            serializedData.writeByte(tL_pollAnswer.option[0]);
                        }
                    }
                }
                final HashMap hashMap = new HashMap();
                hashMap.put("answers", Utilities.bytesToHex(serializedData.toByteArray()));
                tL_messageMediaPoll.results = new TLRPC.TL_pollResults();
                CharSequence fixedString = ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.solutionString);
                if (fixedString != null) {
                    tL_messageMediaPoll.results.solution = fixedString.toString();
                    ArrayList<TLRPC.MessageEntity> entities3 = PollCreateActivity.this.getMediaDataController().getEntities(new CharSequence[]{fixedString}, true);
                    if (entities3 != null && !entities3.isEmpty()) {
                        tL_messageMediaPoll.results.solution_entities = entities3;
                    }
                    if (!TextUtils.isEmpty(tL_messageMediaPoll.results.solution)) {
                        tL_messageMediaPoll.results.flags |= 16;
                    }
                }
                if (PollCreateActivity.this.parentFragment.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(PollCreateActivity.this.getParentActivity(), PollCreateActivity.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.PollCreateActivity$2$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                        public final void didSelectDate(boolean z, int i7) {
                            PollCreateActivity.2.this.lambda$onItemClick$0(tL_messageMediaPoll, hashMap, z, i7);
                        }
                    });
                    return;
                }
                PollCreateActivity.this.delegate.sendPoll(tL_messageMediaPoll, hashMap, true, 0);
            } else if (!PollCreateActivity.this.checkDiscard()) {
                return;
            }
            PollCreateActivity.this.lambda$onBackPressed$319();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 9 implements EmojiView.EmojiViewDelegate {
        9() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClearEmojiRecent$0(DialogInterface dialogInterface, int i) {
            PollCreateActivity.this.emojiView.clearRecentEmoji();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ boolean canSchedule() {
            return EmojiView.EmojiViewDelegate.-CC.$default$canSchedule(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ long getDialogId() {
            return EmojiView.EmojiViewDelegate.-CC.$default$getDialogId(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ float getProgressToSearchOpened() {
            return EmojiView.EmojiViewDelegate.-CC.$default$getProgressToSearchOpened(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ int getThreadId() {
            return EmojiView.EmojiViewDelegate.-CC.$default$getThreadId(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void invalidateEnterView() {
            EmojiView.EmojiViewDelegate.-CC.$default$invalidateEnterView(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ boolean isExpanded() {
            return EmojiView.EmojiViewDelegate.-CC.$default$isExpanded(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ boolean isInScheduleMode() {
            return EmojiView.EmojiViewDelegate.-CC.$default$isInScheduleMode(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isSearchOpened() {
            return PollCreateActivity.this.isEmojiSearchOpened;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ boolean isUserSelf() {
            return EmojiView.EmojiViewDelegate.-CC.$default$isUserSelf(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onAnimatedEmojiUnlockClick() {
            EmojiView.EmojiViewDelegate.-CC.$default$onAnimatedEmojiUnlockClick(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean onBackspace() {
            EditTextBoldCursor editField = PollCreateActivity.this.currentCell.getEditField();
            if (editField == null) {
                return false;
            }
            editField.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onClearEmojiRecent() {
            AlertDialog.Builder builder = new AlertDialog.Builder(PollCreateActivity.this.getContext(), ((BaseFragment) PollCreateActivity.this).resourceProvider);
            builder.setTitle(LocaleController.getString(R.string.ClearRecentEmojiTitle));
            builder.setMessage(LocaleController.getString(R.string.ClearRecentEmojiText));
            builder.setPositiveButton(LocaleController.getString(R.string.ClearButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PollCreateActivity$9$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PollCreateActivity.9.this.lambda$onClearEmojiRecent$0(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.show();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onCustomEmojiSelected(long j, TLRPC.Document document, String str, boolean z) {
            EditTextBoldCursor editField = PollCreateActivity.this.currentCell.getEditField();
            if (editField == null) {
                return;
            }
            int selectionEnd = editField.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                SpannableString spannableString = new SpannableString(str);
                AnimatedEmojiSpan animatedEmojiSpan = document != null ? new AnimatedEmojiSpan(document, editField.getPaint().getFontMetricsInt()) : new AnimatedEmojiSpan(j, editField.getPaint().getFontMetricsInt());
                animatedEmojiSpan.cacheType = PollCreateActivity.this.emojiView.emojiCacheType;
                spannableString.setSpan(animatedEmojiSpan, 0, spannableString.length(), 33);
                editField.setText(editField.getText().insert(selectionEnd, spannableString));
                int length = selectionEnd + spannableString.length();
                editField.setSelection(length, length);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onEmojiSelected(String str) {
            EditTextBoldCursor editField = PollCreateActivity.this.currentCell.getEditField();
            if (editField == null) {
                return;
            }
            int selectionEnd = editField.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) str, editField.getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                editField.setText(editField.getText().insert(selectionEnd, replaceEmoji));
                int length = selectionEnd + replaceEmoji.length();
                editField.setSelection(length, length);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onEmojiSettingsClick(ArrayList arrayList) {
            EmojiView.EmojiViewDelegate.-CC.$default$onEmojiSettingsClick(this, arrayList);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        /* renamed from: onGifSelected */
        public /* synthetic */ void lambda$onGifSelected$1(View view, Object obj, String str, Object obj2, boolean z, int i) {
            EmojiView.EmojiViewDelegate.-CC.$default$onGifSelected(this, view, obj, str, obj2, z, i);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onSearchOpenClose(int i) {
            PollCreateActivity pollCreateActivity = PollCreateActivity.this;
            pollCreateActivity.isEmojiSearchOpened = i != 0;
            pollCreateActivity.sizeNotifierFrameLayout.requestLayout();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onShowStickerSet(TLRPC.StickerSet stickerSet, TLRPC.InputStickerSet inputStickerSet, boolean z) {
            EmojiView.EmojiViewDelegate.-CC.$default$onShowStickerSet(this, stickerSet, inputStickerSet, z);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onStickerSelected(View view, TLRPC.Document document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, int i) {
            EmojiView.EmojiViewDelegate.-CC.$default$onStickerSelected(this, view, document, str, obj, sendAnimationData, z, i);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onStickerSetAdd(TLRPC.StickerSetCovered stickerSetCovered) {
            EmojiView.EmojiViewDelegate.-CC.$default$onStickerSetAdd(this, stickerSetCovered);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onStickerSetRemove(TLRPC.StickerSetCovered stickerSetCovered) {
            EmojiView.EmojiViewDelegate.-CC.$default$onStickerSetRemove(this, stickerSetCovered);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onStickersGroupClick(long j) {
            EmojiView.EmojiViewDelegate.-CC.$default$onStickersGroupClick(this, j);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onStickersSettingsClick() {
            EmojiView.EmojiViewDelegate.-CC.$default$onStickersSettingsClick(this);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void onTabOpened(int i) {
            EmojiView.EmojiViewDelegate.-CC.$default$onTabOpened(this, i);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public /* synthetic */ void showTrendingStickersAlert(TrendingStickersLayout trendingStickersLayout) {
            EmojiView.EmojiViewDelegate.-CC.$default$showTrendingStickersAlert(this, trendingStickersLayout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00ee, code lost:
        
            if (r1.isEmojiSearchOpened != false) goto L20;
         */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0106  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            int adapterPosition;
            PollCreateActivity pollCreateActivity;
            if (view.getTag() != null) {
                return;
            }
            view.setTag(1);
            PollEditTextCell pollEditTextCell = (PollEditTextCell) view.getParent();
            RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell);
            if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                return;
            }
            int i = adapterPosition - PollCreateActivity.this.answerStartRow;
            PollCreateActivity.this.listAdapter.notifyItemRemoved(adapterPosition);
            int i2 = i + 1;
            System.arraycopy(PollCreateActivity.this.answers, i2, PollCreateActivity.this.answers, i, (PollCreateActivity.this.answers.length - 1) - i);
            System.arraycopy(PollCreateActivity.this.answersChecks, i2, PollCreateActivity.this.answersChecks, i, (PollCreateActivity.this.answersChecks.length - 1) - i);
            PollCreateActivity.this.answers[PollCreateActivity.this.answers.length - 1] = null;
            PollCreateActivity.this.answersChecks[PollCreateActivity.this.answersChecks.length - 1] = false;
            PollCreateActivity.access$3710(PollCreateActivity.this);
            if (PollCreateActivity.this.answersCount == PollCreateActivity.this.answers.length - 1) {
                PollCreateActivity.this.listAdapter.notifyItemInserted((PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answers.length) - 1);
            }
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(adapterPosition - 1);
            EditTextBoldCursor textView = pollEditTextCell.getTextView();
            if (findViewHolderForAdapterPosition != null) {
                View view2 = findViewHolderForAdapterPosition.itemView;
                if (view2 instanceof PollEditTextCell) {
                    ((PollEditTextCell) view2).getTextView().requestFocus();
                    textView.clearFocus();
                    PollCreateActivity.this.checkDoneButton();
                    PollCreateActivity.this.updateRows();
                    if (PollCreateActivity.this.suggestEmojiPanel != null) {
                        PollCreateActivity.this.suggestEmojiPanel.forceClose();
                        PollCreateActivity.this.suggestEmojiPanel.setDelegate(null);
                    }
                    PollCreateActivity.this.listAdapter.notifyItemChanged(PollCreateActivity.this.answerSectionRow);
                }
            }
            if (textView.isFocused()) {
                AndroidUtilities.hideKeyboard(textView);
                pollCreateActivity = PollCreateActivity.this;
            } else {
                pollCreateActivity = PollCreateActivity.this;
            }
            pollCreateActivity.hideEmojiPopup(true);
            textView.clearFocus();
            PollCreateActivity.this.checkDoneButton();
            PollCreateActivity.this.updateRows();
            if (PollCreateActivity.this.suggestEmojiPanel != null) {
            }
            PollCreateActivity.this.listAdapter.notifyItemChanged(PollCreateActivity.this.answerSectionRow);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$1(PollEditTextCell pollEditTextCell, TextView textView, int i, KeyEvent keyEvent) {
            int adapterPosition;
            if (i != 5) {
                return false;
            }
            RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell);
            if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                int i2 = adapterPosition - PollCreateActivity.this.answerStartRow;
                if (i2 == PollCreateActivity.this.answersCount - 1 && PollCreateActivity.this.answersCount < 10) {
                    PollCreateActivity.this.addNewField();
                } else if (i2 == PollCreateActivity.this.answersCount - 1) {
                    AndroidUtilities.hideKeyboard(pollEditTextCell.getTextView());
                } else {
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(adapterPosition + 1);
                    if (findViewHolderForAdapterPosition != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (view instanceof PollEditTextCell) {
                            ((PollEditTextCell) view).getTextView().requestFocus();
                        }
                    }
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onCreateViewHolder$2(PollEditTextCell pollEditTextCell, View view, int i, KeyEvent keyEvent) {
            EditTextBoldCursor editTextBoldCursor = (EditTextBoldCursor) view;
            if (i != 67 || keyEvent.getAction() != 0 || editTextBoldCursor.length() != 0) {
                return false;
            }
            pollEditTextCell.callOnDelete();
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PollCreateActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PollCreateActivity.this.questionHeaderRow || i == PollCreateActivity.this.answerHeaderRow || i == PollCreateActivity.this.settingsHeaderRow) {
                return 0;
            }
            if (i == PollCreateActivity.this.questionSectionRow) {
                return 1;
            }
            if (i == PollCreateActivity.this.answerSectionRow || i == PollCreateActivity.this.settingsSectionRow || i == PollCreateActivity.this.solutionInfoRow) {
                return 2;
            }
            if (i == PollCreateActivity.this.addAnswerRow) {
                return 3;
            }
            if (i == PollCreateActivity.this.questionRow) {
                return 4;
            }
            if (i == PollCreateActivity.this.solutionRow) {
                return 7;
            }
            return (i == PollCreateActivity.this.anonymousRow || i == PollCreateActivity.this.multipleRow || i == PollCreateActivity.this.quizRow) ? 6 : 5;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PollCreateActivity.this.addAnswerRow || adapterPosition == PollCreateActivity.this.anonymousRow || adapterPosition == PollCreateActivity.this.multipleRow || (PollCreateActivity.this.quizOnly == 0 && adapterPosition == PollCreateActivity.this.quizRow);
        }

        /* JADX WARN: Code restructure failed: missing block: B:35:0x00f8, code lost:
        
            if (r6.this$0.quizRow == (-1)) goto L39;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x0115, code lost:
        
            if (r6.this$0.quizRow != (-1)) goto L38;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String string;
            boolean z;
            String formatString;
            int i3;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == PollCreateActivity.this.questionHeaderRow) {
                    i2 = R.string.PollQuestion;
                } else if (i == PollCreateActivity.this.answerHeaderRow) {
                    i2 = PollCreateActivity.this.quizOnly == 1 ? R.string.QuizAnswers : R.string.AnswerOptions;
                } else if (i != PollCreateActivity.this.settingsHeaderRow) {
                    return;
                } else {
                    i2 = R.string.Settings;
                }
                headerCell.setText(LocaleController.getString(i2));
                return;
            }
            if (itemViewType == 6) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == PollCreateActivity.this.anonymousRow) {
                    string = LocaleController.getString(R.string.PollAnonymous);
                    z = PollCreateActivity.this.anonymousPoll;
                    if (PollCreateActivity.this.multipleRow == -1) {
                    }
                    r0 = true;
                } else {
                    if (i != PollCreateActivity.this.multipleRow) {
                        if (i == PollCreateActivity.this.quizRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString(R.string.PollQuiz), PollCreateActivity.this.quizPoll, false);
                            textCheckCell.setEnabled(PollCreateActivity.this.quizOnly == 0, null);
                            return;
                        }
                        return;
                    }
                    string = LocaleController.getString(R.string.PollMultiple);
                    z = PollCreateActivity.this.multipleChoise;
                }
                textCheckCell.setTextAndCheck(string, z, r0);
                textCheckCell.setEnabled(true, null);
                return;
            }
            if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText4);
                Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
                Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
                int color = Theme.getColor(Theme.key_switchTrackChecked);
                PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                drawable.setColorFilter(new PorterDuffColorFilter(color, mode));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), mode));
                textCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.AddAnOption), (Drawable) new CombinedDrawable(drawable, drawable2), false);
                return;
            }
            TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
            textInfoPrivacyCell.setFixedSize(0);
            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            if (i == PollCreateActivity.this.solutionInfoRow) {
                i3 = R.string.AddAnExplanationInfo;
            } else if (i == PollCreateActivity.this.settingsSectionRow) {
                if (PollCreateActivity.this.quizOnly != 0) {
                    textInfoPrivacyCell.setFixedSize(12);
                    textInfoPrivacyCell.setText(null);
                    return;
                }
                i3 = R.string.QuizInfo;
            } else {
                if (10 - PollCreateActivity.this.answersCount > 0) {
                    formatString = LocaleController.formatString("AddAnOptionInfo", R.string.AddAnOptionInfo, LocaleController.formatPluralString("Option", 10 - PollCreateActivity.this.answersCount, new Object[0]));
                    textInfoPrivacyCell.setText(formatString);
                }
                i3 = R.string.AddAnOptionInfoMax;
            }
            formatString = LocaleController.getString(i3);
            textInfoPrivacyCell.setText(formatString);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            TextWatcher textWatcher;
            PollEditTextCell pollEditTextCell;
            if (i != 0) {
                if (i == 1) {
                    view2 = new ShadowSectionCell(this.mContext);
                } else if (i == 2) {
                    view2 = new TextInfoPrivacyCell(this.mContext);
                } else if (i != 3) {
                    if (i == 4) {
                        final PollEditTextCell pollEditTextCell2 = new PollEditTextCell(this.mContext, false, PollCreateActivity.this.isPremium ? 1 : 0, null) { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.1
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected void onActionModeStart(EditTextBoldCursor editTextBoldCursor, ActionMode actionMode) {
                                if (editTextBoldCursor.isFocused() && editTextBoldCursor.hasSelection()) {
                                    Menu menu = actionMode.getMenu();
                                    if (menu.findItem(android.R.id.copy) == null) {
                                        return;
                                    }
                                    ChatActivity.fillActionModeMenu(menu, PollCreateActivity.this.parentFragment.getCurrentEncryptedChat(), false);
                                }
                            }

                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected void onEditTextFocusChanged(boolean z) {
                                PollCreateActivity.this.onCellFocusChanges(this, z);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            /* renamed from: onEmojiButtonClicked */
                            public void lambda$new$1(PollEditTextCell pollEditTextCell3) {
                                PollCreateActivity.this.onEmojiClicked(pollEditTextCell3);
                            }
                        };
                        pollEditTextCell2.createErrorTextView();
                        pollEditTextCell2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        textWatcher = new TextWatcher() { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.2
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                if (pollEditTextCell2.getTag() != null) {
                                    return;
                                }
                                RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.questionRow);
                                if (findViewHolderForAdapterPosition != null && PollCreateActivity.this.suggestEmojiPanel != null) {
                                    for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                        editable.removeSpan(imageSpan);
                                    }
                                    Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell2.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                    PollCreateActivity.this.suggestEmojiPanel.setDirection(1);
                                    PollCreateActivity.this.suggestEmojiPanel.setDelegate(pollEditTextCell2);
                                    PollCreateActivity.this.suggestEmojiPanel.setTranslationY(findViewHolderForAdapterPosition.itemView.getY());
                                    PollCreateActivity.this.suggestEmojiPanel.fireUpdate();
                                }
                                PollCreateActivity.this.questionString = editable;
                                if (findViewHolderForAdapterPosition != null) {
                                    PollCreateActivity pollCreateActivity = PollCreateActivity.this;
                                    pollCreateActivity.setTextLeft(findViewHolderForAdapterPosition.itemView, pollCreateActivity.questionRow);
                                }
                                PollCreateActivity.this.checkDoneButton();
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }
                        };
                        pollEditTextCell = pollEditTextCell2;
                    } else if (i == 6) {
                        view = new TextCheckCell(this.mContext);
                    } else if (i != 7) {
                        Context context = this.mContext;
                        boolean z = PollCreateActivity.this.isPremium;
                        final PollEditTextCell pollEditTextCell3 = new PollEditTextCell(context, false, z ? 1 : 0, new View.OnClickListener() { // from class: org.telegram.ui.PollCreateActivity$ListAdapter$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view3) {
                                PollCreateActivity.ListAdapter.this.lambda$onCreateViewHolder$0(view3);
                            }
                        }) { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.5
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected boolean drawDivider() {
                                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(this);
                                if (findContainingViewHolder != null) {
                                    int adapterPosition = findContainingViewHolder.getAdapterPosition();
                                    if (PollCreateActivity.this.answersCount == 10 && adapterPosition == (PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answersCount) - 1) {
                                        return false;
                                    }
                                }
                                return true;
                            }

                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected boolean isChecked(PollEditTextCell pollEditTextCell4) {
                                int adapterPosition;
                                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell4);
                                if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                                    return false;
                                }
                                return PollCreateActivity.this.answersChecks[adapterPosition - PollCreateActivity.this.answerStartRow];
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            public void onCheckBoxClick(PollEditTextCell pollEditTextCell4, boolean z2) {
                                int adapterPosition;
                                if (z2 && PollCreateActivity.this.quizPoll) {
                                    Arrays.fill(PollCreateActivity.this.answersChecks, false);
                                    PollCreateActivity.this.listView.getChildCount();
                                    for (int i2 = PollCreateActivity.this.answerStartRow; i2 < PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answersCount; i2++) {
                                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(i2);
                                        if (findViewHolderForAdapterPosition != null) {
                                            View view3 = findViewHolderForAdapterPosition.itemView;
                                            if (view3 instanceof PollEditTextCell) {
                                                ((PollEditTextCell) view3).setChecked(false, true);
                                            }
                                        }
                                    }
                                }
                                super.onCheckBoxClick(pollEditTextCell4, z2);
                                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell4);
                                if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                                    PollCreateActivity.this.answersChecks[adapterPosition - PollCreateActivity.this.answerStartRow] = z2;
                                }
                                PollCreateActivity.this.checkDoneButton();
                            }

                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected void onEditTextFocusChanged(boolean z2) {
                                PollCreateActivity.this.onCellFocusChanges(this, z2);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            /* renamed from: onEmojiButtonClicked */
                            public void lambda$new$1(PollEditTextCell pollEditTextCell4) {
                                PollCreateActivity.this.onEmojiClicked(pollEditTextCell4);
                            }

                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected boolean shouldShowCheckBox() {
                                return PollCreateActivity.this.quizPoll;
                            }
                        };
                        pollEditTextCell3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        pollEditTextCell3.addTextWatcher(new TextWatcher() { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.6
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                int adapterPosition;
                                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell3);
                                if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition() - PollCreateActivity.this.answerStartRow) < 0 || adapterPosition >= PollCreateActivity.this.answers.length) {
                                    return;
                                }
                                if (PollCreateActivity.this.suggestEmojiPanel != null) {
                                    for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                        editable.removeSpan(imageSpan);
                                    }
                                    Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell3.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                    float y = (findContainingViewHolder.itemView.getY() - AndroidUtilities.dp(166.0f)) + findContainingViewHolder.itemView.getMeasuredHeight();
                                    if (y > 0.0f) {
                                        PollCreateActivity.this.suggestEmojiPanel.setDirection(0);
                                        PollCreateActivity.this.suggestEmojiPanel.setTranslationY(y);
                                    } else {
                                        PollCreateActivity.this.suggestEmojiPanel.setDirection(1);
                                        PollCreateActivity.this.suggestEmojiPanel.setTranslationY(findContainingViewHolder.itemView.getY());
                                    }
                                    PollCreateActivity.this.suggestEmojiPanel.setDelegate(pollEditTextCell3);
                                    PollCreateActivity.this.suggestEmojiPanel.fireUpdate();
                                }
                                PollCreateActivity.this.answers[adapterPosition] = editable;
                                PollCreateActivity.this.setTextLeft(pollEditTextCell3, adapterPosition);
                                PollCreateActivity.this.checkDoneButton();
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }
                        });
                        pollEditTextCell3.setShowNextButton(true);
                        EditTextBoldCursor textView = pollEditTextCell3.getTextView();
                        textView.setImeOptions(textView.getImeOptions() | 5);
                        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PollCreateActivity$ListAdapter$$ExternalSyntheticLambda1
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i2, KeyEvent keyEvent) {
                                boolean lambda$onCreateViewHolder$1;
                                lambda$onCreateViewHolder$1 = PollCreateActivity.ListAdapter.this.lambda$onCreateViewHolder$1(pollEditTextCell3, textView2, i2, keyEvent);
                                return lambda$onCreateViewHolder$1;
                            }
                        });
                        textView.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.PollCreateActivity$ListAdapter$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnKeyListener
                            public final boolean onKey(View view3, int i2, KeyEvent keyEvent) {
                                boolean lambda$onCreateViewHolder$2;
                                lambda$onCreateViewHolder$2 = PollCreateActivity.ListAdapter.lambda$onCreateViewHolder$2(PollEditTextCell.this, view3, i2, keyEvent);
                                return lambda$onCreateViewHolder$2;
                            }
                        });
                        view2 = pollEditTextCell3;
                    } else {
                        final PollEditTextCell pollEditTextCell4 = new PollEditTextCell(this.mContext, false, PollCreateActivity.this.isPremium ? 1 : 0, null) { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.3
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected void onActionModeStart(EditTextBoldCursor editTextBoldCursor, ActionMode actionMode) {
                                if (editTextBoldCursor.isFocused() && editTextBoldCursor.hasSelection()) {
                                    Menu menu = actionMode.getMenu();
                                    if (menu.findItem(android.R.id.copy) == null) {
                                        return;
                                    }
                                    ChatActivity.fillActionModeMenu(menu, PollCreateActivity.this.parentFragment.getCurrentEncryptedChat(), false);
                                }
                            }

                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            protected void onEditTextFocusChanged(boolean z2) {
                                PollCreateActivity.this.onCellFocusChanges(this, z2);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Cells.PollEditTextCell
                            /* renamed from: onEmojiButtonClicked */
                            public void lambda$new$1(PollEditTextCell pollEditTextCell5) {
                                PollCreateActivity.this.onEmojiClicked(pollEditTextCell5);
                            }
                        };
                        pollEditTextCell4.createErrorTextView();
                        pollEditTextCell4.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        textWatcher = new TextWatcher() { // from class: org.telegram.ui.PollCreateActivity.ListAdapter.4
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                if (pollEditTextCell4.getTag() != null) {
                                    return;
                                }
                                RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.questionRow);
                                if (findViewHolderForAdapterPosition != null && PollCreateActivity.this.suggestEmojiPanel != null) {
                                    for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                        editable.removeSpan(imageSpan);
                                    }
                                    Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell4.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                    PollCreateActivity.this.suggestEmojiPanel.setDirection(1);
                                    PollCreateActivity.this.suggestEmojiPanel.setDelegate(pollEditTextCell4);
                                    PollCreateActivity.this.suggestEmojiPanel.setTranslationY(findViewHolderForAdapterPosition.itemView.getY());
                                    PollCreateActivity.this.suggestEmojiPanel.fireUpdate();
                                }
                                PollCreateActivity.this.solutionString = editable;
                                if (findViewHolderForAdapterPosition != null) {
                                    PollCreateActivity pollCreateActivity = PollCreateActivity.this;
                                    pollCreateActivity.setTextLeft(findViewHolderForAdapterPosition.itemView, pollCreateActivity.solutionRow);
                                }
                                PollCreateActivity.this.checkDoneButton();
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }
                        };
                        pollEditTextCell = pollEditTextCell4;
                    }
                    pollEditTextCell.addTextWatcher(textWatcher);
                    view2 = pollEditTextCell;
                } else {
                    view = new TextCell(this.mContext);
                }
                view2.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                return new RecyclerListView.Holder(view2);
            }
            view = new HeaderCell(this.mContext, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, false);
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            view2 = view;
            view2.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            PollEditTextCell pollEditTextCell;
            String str;
            int i;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 4) {
                pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                str = PollCreateActivity.this.questionString != null ? PollCreateActivity.this.questionString : "";
                i = R.string.QuestionHint;
            } else {
                if (itemViewType == 5) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    PollEditTextCell pollEditTextCell2 = (PollEditTextCell) viewHolder.itemView;
                    pollEditTextCell2.setTag(1);
                    pollEditTextCell2.setTextAndHint(PollCreateActivity.this.answers[adapterPosition - PollCreateActivity.this.answerStartRow], LocaleController.getString(R.string.OptionHint), true);
                    pollEditTextCell2.setTag(null);
                    if (PollCreateActivity.this.requestFieldFocusAtPosition == adapterPosition) {
                        EditTextBoldCursor textView = pollEditTextCell2.getTextView();
                        textView.requestFocus();
                        AndroidUtilities.showKeyboard(textView);
                        PollCreateActivity.this.requestFieldFocusAtPosition = -1;
                    }
                    PollCreateActivity.this.setTextLeft(viewHolder.itemView, adapterPosition);
                    return;
                }
                if (itemViewType != 7) {
                    return;
                }
                pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                str = PollCreateActivity.this.solutionString != null ? PollCreateActivity.this.solutionString : "";
                i = R.string.AddAnExplanation;
            }
            pollEditTextCell.setTextAndHint(str, LocaleController.getString(i), false);
            pollEditTextCell.setTag(null);
            PollCreateActivity.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 5) {
                EditTextBoldCursor textView = ((PollEditTextCell) viewHolder.itemView).getTextView();
                if (textView.isFocused()) {
                    if (PollCreateActivity.this.isPremium) {
                        if (PollCreateActivity.this.suggestEmojiPanel != null) {
                            PollCreateActivity.this.suggestEmojiPanel.forceClose();
                        }
                        PollCreateActivity.this.hideEmojiPopup(true);
                    }
                    PollCreateActivity.this.currentCell = null;
                    textView.clearFocus();
                    AndroidUtilities.hideKeyboard(textView);
                }
            }
        }

        public void swapElements(int i, int i2) {
            int i3 = i - PollCreateActivity.this.answerStartRow;
            int i4 = i2 - PollCreateActivity.this.answerStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= PollCreateActivity.this.answersCount || i4 >= PollCreateActivity.this.answersCount) {
                return;
            }
            CharSequence charSequence = PollCreateActivity.this.answers[i3];
            PollCreateActivity.this.answers[i3] = PollCreateActivity.this.answers[i4];
            PollCreateActivity.this.answers[i4] = charSequence;
            boolean z = PollCreateActivity.this.answersChecks[i3];
            PollCreateActivity.this.answersChecks[i3] = PollCreateActivity.this.answersChecks[i4];
            PollCreateActivity.this.answersChecks[i4] = z;
            notifyItemMoved(i, i2);
        }
    }

    /* loaded from: classes4.dex */
    public interface PollCreateActivityDelegate {
        void sendPoll(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i);
    }

    /* loaded from: classes4.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 5 ? ItemTouchHelper.Callback.makeMovementFlags(0, 0) : ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            PollCreateActivity.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                PollCreateActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    public PollCreateActivity(ChatActivity chatActivity, Boolean bool) {
        this.parentFragment = chatActivity;
        if (bool != null) {
            boolean booleanValue = bool.booleanValue();
            this.quizPoll = booleanValue;
            this.quizOnly = booleanValue ? 1 : 2;
        }
    }

    static /* synthetic */ int access$3710(PollCreateActivity pollCreateActivity) {
        int i = pollCreateActivity.answersCount;
        pollCreateActivity.answersCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addNewField() {
        resetSuggestEmojiPanel();
        boolean[] zArr = this.answersChecks;
        int i = this.answersCount;
        zArr[i] = false;
        int i2 = i + 1;
        this.answersCount = i2;
        if (i2 == this.answers.length) {
            this.listAdapter.notifyItemRemoved(this.addAnswerRow);
        }
        this.listAdapter.notifyItemInserted(this.addAnswerRow);
        updateRows();
        this.requestFieldFocusAtPosition = (this.answerStartRow + this.answersCount) - 1;
        this.listAdapter.notifyItemChanged(this.answerSectionRow);
    }

    private void animateEmojiViewTranslationY(final float f, final float f2) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PollCreateActivity$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PollCreateActivity.this.lambda$animateEmojiViewTranslationY$2(f, f2, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PollCreateActivity.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PollCreateActivity.this.emojiView.setTranslationY(f2);
            }
        });
        ofFloat.setDuration(250L);
        ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        boolean isEmpty = TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.questionString));
        if (isEmpty) {
            for (int i = 0; i < this.answersCount && (isEmpty = TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.answers[i]))); i++) {
            }
        }
        if (!isEmpty) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.CancelPollAlertTitle));
            builder.setMessage(LocaleController.getString(R.string.CancelPollAlertText));
            builder.setPositiveButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PollCreateActivity$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    PollCreateActivity.this.lambda$checkDiscard$1(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            showDialog(builder.create());
        }
        return isEmpty;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkDoneButton() {
        int i;
        boolean z;
        if (this.quizPoll) {
            i = 0;
            for (int i2 = 0; i2 < this.answersChecks.length; i2++) {
                if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.answers[i2])) && this.answersChecks[i2]) {
                    i++;
                }
            }
        } else {
            i = 0;
        }
        if ((TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.solutionString)) || this.solutionString.length() <= 200) && !TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.questionString)) && this.questionString.length() <= 255) {
            int i3 = 0;
            int i4 = 0;
            while (true) {
                CharSequence[] charSequenceArr = this.answers;
                if (i3 >= charSequenceArr.length) {
                    break;
                }
                if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(charSequenceArr[i3]))) {
                    if (this.answers[i3].length() > 100) {
                        i4 = 0;
                        break;
                    }
                    i4++;
                }
                i3++;
            }
            if (i4 >= 2 && (!this.quizPoll || i >= 1)) {
                z = true;
                this.doneItem.setEnabled((this.quizPoll && i == 0) || z);
                this.doneItem.setAlpha(z ? 1.0f : 0.5f);
            }
        }
        z = false;
        if (this.quizPoll) {
            this.doneItem.setEnabled((this.quizPoll && i == 0) || z);
            this.doneItem.setAlpha(z ? 1.0f : 0.5f);
        }
        this.doneItem.setEnabled((this.quizPoll && i == 0) || z);
        this.doneItem.setAlpha(z ? 1.0f : 0.5f);
    }

    private void collapseSearchEmojiView() {
        if (this.isEmojiSearchOpened) {
            this.emojiView.closeSearch(false);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.emojiView.getLayoutParams();
            layoutParams.height -= AndroidUtilities.dp(120.0f);
            this.emojiView.setLayoutParams(layoutParams);
            this.emojiPadding = layoutParams.height;
            this.wasEmojiSearchOpened = this.isEmojiSearchOpened;
            this.isEmojiSearchOpened = false;
            animateEmojiViewTranslationY(-AndroidUtilities.dp(120.0f), 0.0f);
        }
    }

    private void createEmojiView() {
        EmojiView emojiView = this.emojiView;
        if (emojiView != null && emojiView.currentAccount != UserConfig.selectedAccount) {
            this.sizeNotifierFrameLayout.removeView(emojiView);
            this.emojiView = null;
        }
        if (this.emojiView != null) {
            return;
        }
        EmojiView emojiView2 = new EmojiView(null, true, false, false, getContext(), true, null, null, true, this.resourceProvider, false);
        this.emojiView = emojiView2;
        emojiView2.fixBottomTabContainerTranslation = false;
        emojiView2.allowEmojisForNonPremium(false);
        this.emojiView.setVisibility(8);
        if (AndroidUtilities.isTablet()) {
            this.emojiView.setForseMultiwindowLayout(true);
        }
        this.emojiView.setDelegate(new 9());
        this.sizeNotifierFrameLayout.addView(this.emojiView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEmojiPopup(boolean z) {
        if (this.isPremium) {
            if (this.emojiViewVisible) {
                this.emojiView.scrollEmojiToTop();
                this.emojiView.closeSearch(false);
                if (z) {
                    this.emojiView.hideSearchKeyboard();
                }
                this.isEmojiSearchOpened = false;
                showEmojiPopup(0);
            }
            if (z) {
                EmojiView emojiView = this.emojiView;
                if (emojiView == null || emojiView.getVisibility() != 0) {
                    hideEmojiView();
                    return;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, this.emojiView.getMeasuredHeight());
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PollCreateActivity$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PollCreateActivity.this.lambda$hideEmojiPopup$4(valueAnimator);
                    }
                });
                this.isAnimatePopupClosing = true;
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PollCreateActivity.8
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PollCreateActivity.this.isAnimatePopupClosing = false;
                        PollCreateActivity.this.emojiView.setTranslationY(0.0f);
                        PollCreateActivity.this.hideEmojiView();
                    }
                });
                ofFloat.setDuration(250L);
                ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                ofFloat.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEmojiViewTranslationY$2(float f, float f2, ValueAnimator valueAnimator) {
        this.emojiView.setTranslationY(AndroidUtilities.lerp(f, f2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$1(DialogInterface dialogInterface, int i) {
        lambda$onBackPressed$319();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view, int i) {
        boolean z;
        if (i == this.addAnswerRow) {
            addNewField();
            return;
        }
        if (view instanceof TextCheckCell) {
            TextCheckCell textCheckCell = (TextCheckCell) view;
            boolean z2 = this.quizPoll;
            SuggestEmojiView suggestEmojiView = this.suggestEmojiPanel;
            if (suggestEmojiView != null) {
                suggestEmojiView.forceClose();
            }
            if (i == this.anonymousRow) {
                z = !this.anonymousPoll;
                this.anonymousPoll = z;
            } else if (i == this.multipleRow) {
                z = !this.multipleChoise;
                this.multipleChoise = z;
                if (z && this.quizPoll) {
                    int i2 = this.solutionRow;
                    this.quizPoll = false;
                    updateRows();
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.quizRow);
                    if (findViewHolderForAdapterPosition != null) {
                        ((TextCheckCell) findViewHolderForAdapterPosition.itemView).setChecked(false);
                    } else {
                        this.listAdapter.notifyItemChanged(this.quizRow);
                    }
                    this.listAdapter.notifyItemRangeRemoved(i2, 2);
                }
            } else {
                if (this.quizOnly != 0) {
                    return;
                }
                z = !this.quizPoll;
                this.quizPoll = z;
                int i3 = this.solutionRow;
                updateRows();
                if (this.quizPoll) {
                    this.listAdapter.notifyItemRangeInserted(this.solutionRow, 2);
                } else {
                    this.listAdapter.notifyItemRangeRemoved(i3, 2);
                }
                if (this.quizPoll && this.multipleChoise) {
                    this.multipleChoise = false;
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.multipleRow);
                    if (findViewHolderForAdapterPosition2 != null) {
                        ((TextCheckCell) findViewHolderForAdapterPosition2.itemView).setChecked(false);
                    } else {
                        this.listAdapter.notifyItemChanged(this.multipleRow);
                    }
                }
                if (this.quizPoll) {
                    int i4 = 0;
                    boolean z3 = false;
                    while (true) {
                        boolean[] zArr = this.answersChecks;
                        if (i4 >= zArr.length) {
                            break;
                        }
                        if (z3) {
                            zArr[i4] = false;
                        } else if (zArr[i4]) {
                            z3 = true;
                        }
                        i4++;
                    }
                }
            }
            if (this.hintShowed && !this.quizPoll) {
                this.hintView.hide();
            }
            this.listView.getChildCount();
            for (int i5 = this.answerStartRow; i5 < this.answerStartRow + this.answersCount; i5++) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition3 = this.listView.findViewHolderForAdapterPosition(i5);
                if (findViewHolderForAdapterPosition3 != null) {
                    View view2 = findViewHolderForAdapterPosition3.itemView;
                    if (view2 instanceof PollEditTextCell) {
                        PollEditTextCell pollEditTextCell = (PollEditTextCell) view2;
                        pollEditTextCell.setShowCheckBox(this.quizPoll, true);
                        pollEditTextCell.setChecked(this.answersChecks[i5 - this.answerStartRow], z2);
                        if (pollEditTextCell.getTop() > AndroidUtilities.dp(40.0f) && i == this.quizRow && !this.hintShowed) {
                            this.hintView.showForView(pollEditTextCell.getCheckBox(), true);
                            this.hintShowed = true;
                        }
                    }
                }
            }
            textCheckCell.setChecked(z);
            checkDoneButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideEmojiPopup$4(ValueAnimator valueAnimator) {
        this.emojiView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showEmojiPopup$3(ValueAnimator valueAnimator) {
        this.emojiView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCellFocusChanges(PollEditTextCell pollEditTextCell, boolean z) {
        if (this.isPremium && z) {
            if (this.currentCell == pollEditTextCell && this.emojiViewVisible && this.isEmojiSearchOpened) {
                collapseSearchEmojiView();
                this.emojiViewVisible = false;
            }
            PollEditTextCell pollEditTextCell2 = this.currentCell;
            this.currentCell = pollEditTextCell;
            pollEditTextCell.setEmojiButtonVisibility(true);
            ChatActivityEnterViewAnimatedIconView emojiButton = pollEditTextCell.getEmojiButton();
            ChatActivityEnterViewAnimatedIconView.State state = ChatActivityEnterViewAnimatedIconView.State.SMILE;
            emojiButton.setState(state, false);
            updateSuggestEmojiPanelDelegate(this.listView.findContainingViewHolder(pollEditTextCell));
            if (pollEditTextCell2 == null || pollEditTextCell2 == pollEditTextCell) {
                return;
            }
            if (this.emojiViewVisible) {
                collapseSearchEmojiView();
                hideEmojiPopup(false);
                openKeyboardInternal();
            }
            pollEditTextCell2.setEmojiButtonVisibility(false);
            pollEditTextCell2.getEmojiButton().setState(state, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEmojiClicked(PollEditTextCell pollEditTextCell) {
        this.currentCell = pollEditTextCell;
        if (!this.emojiViewVisible) {
            showEmojiPopup(1);
        } else {
            collapseSearchEmojiView();
            openKeyboardInternal();
        }
    }

    private void openKeyboardInternal() {
        this.keyboardNotifier.awaitKeyboard();
        EditTextBoldCursor editField = this.currentCell.getEditField();
        editField.requestFocus();
        AndroidUtilities.showKeyboard(editField);
        showEmojiPopup(AndroidUtilities.usingHardwareInput ? 0 : 2);
        if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) {
            return;
        }
        this.waitingForKeyboardOpen = true;
        AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
    }

    private void resetSuggestEmojiPanel() {
        SuggestEmojiView suggestEmojiView = this.suggestEmojiPanel;
        if (suggestEmojiView != null) {
            suggestEmojiView.setDelegate(null);
            this.suggestEmojiPanel.forceClose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0081  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTextLeft(View view, int i) {
        int i2;
        int length;
        CharSequence charSequence;
        int i3;
        int i4;
        int i5;
        float f;
        if (!(view instanceof PollEditTextCell)) {
            return;
        }
        PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
        if (i == this.questionRow) {
            charSequence = this.questionString;
            i3 = NotificationCenter.playerDidStartPlaying;
            if (charSequence != null) {
                i2 = NotificationCenter.playerDidStartPlaying;
                i5 = charSequence.length();
            } else {
                i4 = NotificationCenter.playerDidStartPlaying;
                i2 = i4;
                i5 = 0;
            }
        } else {
            if (i != this.solutionRow) {
                int i6 = this.answerStartRow;
                if (i < i6 || i >= this.answersCount + i6) {
                    return;
                }
                CharSequence charSequence2 = this.answers[i - i6];
                i2 = 100;
                length = 100 - (charSequence2 != null ? charSequence2.length() : 0);
                f = i2;
                if (length <= f - (0.7f * f)) {
                    pollEditTextCell.setText2("");
                    return;
                }
                pollEditTextCell.setText2(String.format("%d", Integer.valueOf(length)));
                SimpleTextView textView2 = pollEditTextCell.getTextView2();
                int i7 = length < 0 ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayText3;
                textView2.setTextColor(Theme.getColor(i7));
                textView2.setTag(Integer.valueOf(i7));
                return;
            }
            charSequence = this.solutionString;
            i3 = NotificationCenter.storyQualityUpdate;
            if (charSequence != null) {
                i2 = NotificationCenter.storyQualityUpdate;
                i5 = charSequence.length();
            } else {
                i4 = NotificationCenter.storyQualityUpdate;
                i2 = i4;
                i5 = 0;
            }
        }
        length = i3 - i5;
        f = i2;
        if (length <= f - (0.7f * f)) {
        }
    }

    private void showEmojiPopup(int i) {
        PollEditTextCell pollEditTextCell;
        if (this.isPremium) {
            if (i != 1) {
                ChatActivityEnterViewAnimatedIconView emojiButton = this.currentCell.getEmojiButton();
                if (emojiButton != null) {
                    emojiButton.setState(ChatActivityEnterViewAnimatedIconView.State.SMILE, true);
                }
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    this.emojiViewWasVisible = this.emojiViewVisible;
                    this.emojiViewVisible = false;
                    this.isEmojiSearchOpened = false;
                    if (AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                        emojiView.setVisibility(8);
                    }
                }
                if (i == 0) {
                    this.emojiPadding = 0;
                }
                this.keyboardNotifier.fire();
                this.sizeNotifierFrameLayout.requestLayout();
                return;
            }
            EmojiView emojiView2 = this.emojiView;
            boolean z = emojiView2 != null && emojiView2.getVisibility() == 0;
            createEmojiView();
            this.emojiView.setVisibility(0);
            this.emojiViewWasVisible = this.emojiViewVisible;
            this.emojiViewVisible = true;
            EmojiView emojiView3 = this.emojiView;
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = AndroidUtilities.isTablet() ? AndroidUtilities.dp(150.0f) : MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = AndroidUtilities.isTablet() ? AndroidUtilities.dp(150.0f) : MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            Point point = AndroidUtilities.displaySize;
            int i2 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) emojiView3.getLayoutParams();
            layoutParams.height = i2;
            emojiView3.setLayoutParams(layoutParams);
            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet() && (pollEditTextCell = this.currentCell) != null) {
                AndroidUtilities.hideKeyboard(pollEditTextCell.getEditField());
            }
            this.emojiPadding = i2;
            this.keyboardNotifier.fire();
            this.sizeNotifierFrameLayout.requestLayout();
            ChatActivityEnterViewAnimatedIconView emojiButton2 = this.currentCell.getEmojiButton();
            if (emojiButton2 != null) {
                emojiButton2.setState(ChatActivityEnterViewAnimatedIconView.State.KEYBOARD, true);
            }
            if (z || this.keyboardVisible) {
                return;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.emojiPadding, 0.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PollCreateActivity$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PollCreateActivity.this.lambda$showEmojiPopup$3(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PollCreateActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PollCreateActivity.this.emojiView.setTranslationY(0.0f);
                }
            });
            ofFloat.setDuration(250L);
            ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
            ofFloat.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showQuizHint() {
        this.listView.getChildCount();
        for (int i = this.answerStartRow; i < this.answerStartRow + this.answersCount; i++) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
            if (findViewHolderForAdapterPosition != null) {
                View view = findViewHolderForAdapterPosition.itemView;
                if (view instanceof PollEditTextCell) {
                    PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
                    if (pollEditTextCell.getTop() > AndroidUtilities.dp(40.0f)) {
                        this.hintView.showForView(pollEditTextCell.getCheckBox(), true);
                        return;
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows() {
        this.questionHeaderRow = 0;
        this.questionRow = 1;
        this.questionSectionRow = 2;
        this.rowCount = 4;
        this.answerHeaderRow = 3;
        int i = this.answersCount;
        if (i != 0) {
            this.answerStartRow = 4;
            this.rowCount = 4 + i;
        } else {
            this.answerStartRow = -1;
        }
        if (i != this.answers.length) {
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.addAnswerRow = i2;
        } else {
            this.addAnswerRow = -1;
        }
        int i3 = this.rowCount;
        this.answerSectionRow = i3;
        this.rowCount = i3 + 2;
        this.settingsHeaderRow = i3 + 1;
        TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
        if (!ChatObject.isChannel(currentChat) || currentChat.megagroup) {
            int i4 = this.rowCount;
            this.rowCount = i4 + 1;
            this.anonymousRow = i4;
        } else {
            this.anonymousRow = -1;
        }
        int i5 = this.quizOnly;
        if (i5 != 1) {
            int i6 = this.rowCount;
            this.rowCount = i6 + 1;
            this.multipleRow = i6;
        } else {
            this.multipleRow = -1;
        }
        if (i5 == 0) {
            int i7 = this.rowCount;
            this.rowCount = i7 + 1;
            this.quizRow = i7;
        } else {
            this.quizRow = -1;
        }
        int i8 = this.rowCount;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.settingsSectionRow = i8;
        if (!this.quizPoll) {
            this.solutionRow = -1;
            this.solutionInfoRow = -1;
        } else {
            this.solutionRow = i9;
            this.rowCount = i8 + 3;
            this.solutionInfoRow = i8 + 2;
        }
    }

    private void updateSuggestEmojiPanelDelegate(RecyclerView.ViewHolder viewHolder) {
        SuggestEmojiView suggestEmojiView = this.suggestEmojiPanel;
        if (suggestEmojiView != null) {
            suggestEmojiView.forceClose();
            SuggestEmojiView suggestEmojiView2 = this.suggestEmojiPanel;
            if (suggestEmojiView2 == null || viewHolder == null || !(viewHolder.itemView instanceof PollEditTextCell)) {
                return;
            }
            SuggestEmojiView.AnchorViewDelegate delegate = suggestEmojiView2.getDelegate();
            View view = viewHolder.itemView;
            if (delegate != view) {
                this.suggestEmojiPanel.setDelegate((PollEditTextCell) view);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        ActionBar actionBar;
        int i;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        if (this.quizOnly == 1) {
            actionBar = this.actionBar;
            i = R.string.NewQuiz;
        } else {
            actionBar = this.actionBar;
            i = R.string.NewPoll;
        }
        actionBar.setTitle(LocaleController.getString(i));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new 2());
        this.doneItem = this.actionBar.createMenu().addItem(1, LocaleController.getString(R.string.Create).toUpperCase());
        this.listAdapter = new ListAdapter(context);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.PollCreateActivity.3
            private boolean ignoreLayout;

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:22:0x006b  */
            /* JADX WARN: Removed duplicated region for block: B:29:0x0099  */
            /* JADX WARN: Removed duplicated region for block: B:33:0x00a7  */
            /* JADX WARN: Removed duplicated region for block: B:35:0x00b1  */
            /* JADX WARN: Removed duplicated region for block: B:42:0x0087  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                int i6;
                int i7;
                int i8;
                int i9;
                int childCount = getChildCount();
                int measureKeyboardHeight = measureKeyboardHeight();
                int emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : PollCreateActivity.this.getEmojiPadding();
                setBottomClip(emojiPadding);
                for (int i10 = 0; i10 < childCount; i10++) {
                    View childAt = getChildAt(i10);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i11 = layoutParams.gravity;
                        if (i11 == -1) {
                            i11 = 51;
                        }
                        int i12 = i11 & 112;
                        int i13 = i11 & 7;
                        if (i13 == 1) {
                            i6 = (((i4 - i2) - measuredWidth) / 2) + layoutParams.leftMargin;
                        } else if (i13 != 5) {
                            i7 = layoutParams.leftMargin;
                            if (i12 == 16) {
                                if (i12 == 48) {
                                    i9 = layoutParams.topMargin + getPaddingTop();
                                } else if (i12 != 80) {
                                    i9 = layoutParams.topMargin;
                                } else {
                                    i8 = ((i5 - emojiPadding) - i3) - measuredHeight;
                                }
                                if (PollCreateActivity.this.emojiView != null && PollCreateActivity.this.emojiView == childAt) {
                                    i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                                }
                                childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                            } else {
                                i8 = ((((i5 - emojiPadding) - i3) - measuredHeight) / 2) + layoutParams.topMargin;
                            }
                            i9 = i8 - layoutParams.bottomMargin;
                            if (PollCreateActivity.this.emojiView != null) {
                                i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                            }
                            childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                        } else {
                            i6 = i4 - measuredWidth;
                        }
                        i7 = i6 - layoutParams.rightMargin;
                        if (i12 == 16) {
                        }
                        i9 = i8 - layoutParams.bottomMargin;
                        if (PollCreateActivity.this.emojiView != null) {
                        }
                        childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                    }
                }
                notifyHeightChanged();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                int makeMeasureSpec;
                int paddingTop;
                int size = View.MeasureSpec.getSize(i2);
                int size2 = View.MeasureSpec.getSize(i3);
                setMeasuredDimension(size, size2);
                int paddingTop2 = size2 - getPaddingTop();
                measureChildWithMargins(((BaseFragment) PollCreateActivity.this).actionBar, i2, 0, i3, 0);
                int measureKeyboardHeight = measureKeyboardHeight();
                if (measureKeyboardHeight > AndroidUtilities.dp(20.0f)) {
                    PollCreateActivity pollCreateActivity = PollCreateActivity.this;
                    if (!pollCreateActivity.emojiViewVisible && !pollCreateActivity.isEmojiSearchOpened) {
                        this.ignoreLayout = true;
                        pollCreateActivity.hideEmojiView();
                        this.ignoreLayout = false;
                    }
                }
                int emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : PollCreateActivity.this.getEmojiPadding();
                if (measureKeyboardHeight > AndroidUtilities.dp(20.0f) && PollCreateActivity.this.isEmojiSearchOpened) {
                    emojiPadding = AndroidUtilities.dp(120.0f);
                }
                int childCount = getChildCount();
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = getChildAt(i4);
                    if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) PollCreateActivity.this).actionBar) {
                        if (PollCreateActivity.this.emojiView != null && PollCreateActivity.this.emojiView == childAt) {
                            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = childAt.getLayoutParams().height;
                            } else if (AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (paddingTop2 - AndroidUtilities.statusBarHeight) + getPaddingTop());
                            } else {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = (paddingTop2 - AndroidUtilities.statusBarHeight) + getPaddingTop();
                            }
                            childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                        } else if (PollCreateActivity.this.listView == childAt) {
                            childAt.measure(i2, View.MeasureSpec.makeMeasureSpec(paddingTop2 - emojiPadding, 1073741824));
                        } else {
                            measureChildWithMargins(childAt, i2, 0, i3, 0);
                        }
                    }
                }
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.sizeNotifierFrameLayout = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setDelegate(this);
        SizeNotifierFrameLayout sizeNotifierFrameLayout2 = this.sizeNotifierFrameLayout;
        this.fragmentView = sizeNotifierFrameLayout2;
        sizeNotifierFrameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.PollCreateActivity.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.RecyclerView
            public void requestChildOnScreen(View view, View view2) {
                if (view instanceof PollEditTextCell) {
                    super.requestChildOnScreen(view, view2);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.ViewParent
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                rect.bottom += AndroidUtilities.dp(60.0f);
                return super.requestChildRectangleOnScreen(view, rect, z);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        this.listView.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PollCreateActivity$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                PollCreateActivity.this.lambda$createView$0(view, i2);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.PollCreateActivity.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i2) {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                SuggestEmojiView suggestEmojiView;
                float y;
                if (i3 != 0 && PollCreateActivity.this.hintView != null) {
                    PollCreateActivity.this.hintView.hide();
                }
                if (PollCreateActivity.this.suggestEmojiPanel == null || !PollCreateActivity.this.suggestEmojiPanel.isShown()) {
                    return;
                }
                SuggestEmojiView.AnchorViewDelegate delegate = PollCreateActivity.this.suggestEmojiPanel.getDelegate();
                if (delegate instanceof PollEditTextCell) {
                    RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder((PollEditTextCell) delegate);
                    if (findContainingViewHolder != null) {
                        if (PollCreateActivity.this.suggestEmojiPanel.getDirection() == 0) {
                            suggestEmojiView = PollCreateActivity.this.suggestEmojiPanel;
                            y = (findContainingViewHolder.itemView.getY() - AndroidUtilities.dp(166.0f)) + findContainingViewHolder.itemView.getMeasuredHeight();
                        } else {
                            suggestEmojiView = PollCreateActivity.this.suggestEmojiPanel;
                            y = findContainingViewHolder.itemView.getY();
                        }
                        suggestEmojiView.setTranslationY(y);
                        if (PollCreateActivity.this.layoutManager.isViewPartiallyVisible(findContainingViewHolder.itemView, true, true)) {
                            return;
                        }
                    }
                }
                PollCreateActivity.this.suggestEmojiPanel.forceClose();
            }
        });
        HintView hintView = new HintView(context, 4);
        this.hintView = hintView;
        hintView.setText(LocaleController.getString(R.string.PollTapToSelect));
        this.hintView.setAlpha(0.0f);
        this.hintView.setVisibility(4);
        frameLayout.addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        if (this.isPremium) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
            SuggestEmojiView suggestEmojiView = new SuggestEmojiView(context, this.currentAccount, null, this.resourceProvider);
            this.suggestEmojiPanel = suggestEmojiView;
            suggestEmojiView.forbidCopy();
            this.suggestEmojiPanel.forbidSetAsStatus();
            this.suggestEmojiPanel.setHorizontalPadding(AndroidUtilities.dp(24.0f));
            frameLayout.addView(this.suggestEmojiPanel, LayoutHelper.createFrame(-2, NotificationCenter.audioRouteChanged, 51));
        }
        this.keyboardNotifier = new KeyboardNotifier(this.sizeNotifierFrameLayout, null);
        checkDoneButton();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
            PollEditTextCell pollEditTextCell = this.currentCell;
            if (pollEditTextCell != null) {
                int currentTextColor = pollEditTextCell.getEditField().getCurrentTextColor();
                this.currentCell.getEditField().setTextColor(-1);
                this.currentCell.getEditField().setTextColor(currentTextColor);
            }
        }
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCell.class, PollEditTextCell.class, TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i3 = Theme.key_text_RedRegular;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText3));
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteHintText));
        int i5 = Theme.key_windowBackgroundWhiteGrayIcon;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"moveImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_stickers_menuSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{PollEditTextCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{PollEditTextCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_checkboxCheck;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{PollEditTextCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        int i7 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        return arrayList;
    }

    public void hideEmojiView() {
        EmojiView emojiView;
        ChatActivityEnterViewAnimatedIconView emojiButton;
        if (!this.emojiViewVisible && (emojiView = this.emojiView) != null && emojiView.getVisibility() != 8) {
            PollEditTextCell pollEditTextCell = this.currentCell;
            if (pollEditTextCell != null && (emojiButton = pollEditTextCell.getEmojiButton()) != null) {
                emojiButton.setState(ChatActivityEnterViewAnimatedIconView.State.SMILE, false);
            }
            this.emojiView.setVisibility(8);
        }
        int i = this.emojiPadding;
        this.emojiPadding = 0;
        if (i != 0) {
            this.keyboardNotifier.fire();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (!this.emojiViewVisible) {
            return checkDiscard();
        }
        hideEmojiPopup(true);
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.destroyed = true;
        if (this.isPremium) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                this.sizeNotifierFrameLayout.removeView(emojiView);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.isPremium) {
            hideEmojiPopup(false);
            SuggestEmojiView suggestEmojiView = this.suggestEmojiPanel;
            if (suggestEmojiView != null) {
                suggestEmojiView.forceClose();
            }
            PollEditTextCell pollEditTextCell = this.currentCell;
            if (pollEditTextCell != null) {
                pollEditTextCell.setEmojiButtonVisibility(false);
                this.currentCell.getTextView().clearFocus();
                AndroidUtilities.hideKeyboard(this.currentCell.getEditField());
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
    public void onSizeChanged(int i, boolean z) {
        boolean z2;
        SharedPreferences.Editor edit;
        int i2;
        String str;
        if (this.isPremium) {
            if (i > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                if (z) {
                    this.keyboardHeightLand = i;
                    edit = MessagesController.getGlobalEmojiSettings().edit();
                    i2 = this.keyboardHeightLand;
                    str = "kbd_height_land3";
                } else {
                    this.keyboardHeight = i;
                    edit = MessagesController.getGlobalEmojiSettings().edit();
                    i2 = this.keyboardHeight;
                    str = "kbd_height";
                }
                edit.putInt(str, i2).commit();
            }
            if (this.emojiViewVisible) {
                int i3 = z ? this.keyboardHeightLand : this.keyboardHeight;
                if (this.isEmojiSearchOpened) {
                    i3 += AndroidUtilities.dp(120.0f);
                }
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.emojiView.getLayoutParams();
                int i4 = layoutParams.width;
                int i5 = AndroidUtilities.displaySize.x;
                if (i4 != i5 || layoutParams.height != i3 || this.wasEmojiSearchOpened != this.isEmojiSearchOpened) {
                    layoutParams.width = i5;
                    layoutParams.height = i3;
                    this.emojiView.setLayoutParams(layoutParams);
                    this.emojiPadding = layoutParams.height;
                    this.keyboardNotifier.fire();
                    this.sizeNotifierFrameLayout.requestLayout();
                    boolean z3 = this.wasEmojiSearchOpened;
                    if (z3 != this.isEmojiSearchOpened) {
                        animateEmojiViewTranslationY(z3 ? -AndroidUtilities.dp(120.0f) : AndroidUtilities.dp(120.0f), 0.0f);
                    }
                    this.wasEmojiSearchOpened = this.isEmojiSearchOpened;
                }
            }
            if (this.lastSizeChangeValue1 == i && this.lastSizeChangeValue2 == z) {
                return;
            }
            this.lastSizeChangeValue1 = i;
            this.lastSizeChangeValue2 = z;
            boolean z4 = this.keyboardVisible;
            PollEditTextCell pollEditTextCell = this.currentCell;
            if (pollEditTextCell != null) {
                this.keyboardVisible = pollEditTextCell.getEditField().isFocused() && this.keyboardNotifier.keyboardVisible() && i > 0;
            } else {
                this.keyboardVisible = false;
            }
            if (this.keyboardVisible && this.emojiViewVisible) {
                showEmojiPopup(0);
            }
            if (this.emojiPadding != 0 && !(z2 = this.keyboardVisible) && z2 != z4 && !this.emojiViewVisible) {
                this.emojiPadding = 0;
                this.keyboardNotifier.fire();
                this.sizeNotifierFrameLayout.requestLayout();
            }
            if (this.keyboardVisible && this.waitingForKeyboardOpen) {
                this.waitingForKeyboardOpen = false;
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            }
        }
    }

    public void setDelegate(PollCreateActivityDelegate pollCreateActivityDelegate) {
        this.delegate = pollCreateActivityDelegate;
    }
}
