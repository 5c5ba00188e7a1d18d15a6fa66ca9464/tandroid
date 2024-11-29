package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
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
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatActivityEnterViewAnimatedIconView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertPollLayout;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SuggestEmojiView;
import org.telegram.ui.Stories.recorder.KeyboardNotifier;

/* loaded from: classes3.dex */
public class ChatAttachAlertPollLayout extends ChatAttachAlert.AttachAlertLayout implements SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate, NotificationCenter.NotificationCenterDelegate {
    private int addAnswerRow;
    private boolean allowNesterScroll;
    private boolean anonymousPoll;
    private int anonymousRow;
    private int answerHeaderRow;
    private int answerSectionRow;
    private int answerStartRow;
    private CharSequence[] answers;
    private boolean[] answersChecks;
    private int answersCount;
    private PollEditTextCell currentCell;
    private PollCreateActivityDelegate delegate;
    private boolean destroyed;
    private int emojiPadding;
    public EmojiView emojiView;
    public boolean emojiViewVisible;
    public boolean emojiViewWasVisible;
    private int emptyRow;
    private boolean hintShowed;
    private HintView hintView;
    private boolean ignoreLayout;
    private boolean isAnimatePopupClosing;
    public boolean isEmojiSearchOpened;
    private boolean isPremium;
    private SimpleItemAnimator itemAnimator;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private KeyboardNotifier keyboardNotifier;
    private boolean keyboardVisible;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private FillLastLinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean multipleChoise;
    private int multipleRow;
    private final Runnable openKeyboardRunnable;
    private int paddingRow;
    private int questionHeaderRow;
    private int questionRow;
    private int questionSectionRow;
    private CharSequence questionString;
    private int quizOnly;
    private boolean quizPoll;
    private int quizRow;
    private int requestFieldFocusAtPosition;
    private int rowCount;
    private int settingsHeaderRow;
    private int settingsSectionRow;
    private int solutionInfoRow;
    private int solutionRow;
    private CharSequence solutionString;
    private SuggestEmojiView suggestEmojiPanel;
    private int topPadding;
    private boolean waitingForKeyboardOpen;
    public boolean wasEmojiSearchOpened;

    class 9 implements EmojiView.EmojiViewDelegate {
        9() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClearEmojiRecent$0(DialogInterface dialogInterface, int i) {
            ChatAttachAlertPollLayout.this.emojiView.clearRecentEmoji();
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
            return ChatAttachAlertPollLayout.this.isEmojiSearchOpened;
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
            EditTextBoldCursor editField;
            if (ChatAttachAlertPollLayout.this.currentCell == null || (editField = ChatAttachAlertPollLayout.this.currentCell.getEditField()) == null) {
                return false;
            }
            editField.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onClearEmojiRecent() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatAttachAlertPollLayout.this.getContext(), ChatAttachAlertPollLayout.this.resourcesProvider);
            builder.setTitle(LocaleController.getString(R.string.ClearRecentEmojiTitle));
            builder.setMessage(LocaleController.getString(R.string.ClearRecentEmojiText));
            builder.setPositiveButton(LocaleController.getString(R.string.ClearButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$9$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatAttachAlertPollLayout.9.this.lambda$onClearEmojiRecent$0(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.show();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onCustomEmojiSelected(long j, TLRPC.Document document, String str, boolean z) {
            EditTextBoldCursor editField;
            if (ChatAttachAlertPollLayout.this.currentCell == null || (editField = ChatAttachAlertPollLayout.this.currentCell.getEditField()) == null) {
                return;
            }
            int selectionEnd = editField.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                SpannableString spannableString = new SpannableString(str);
                AnimatedEmojiSpan animatedEmojiSpan = document != null ? new AnimatedEmojiSpan(document, editField.getPaint().getFontMetricsInt()) : new AnimatedEmojiSpan(j, editField.getPaint().getFontMetricsInt());
                animatedEmojiSpan.cacheType = 3;
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
            EditTextBoldCursor editField;
            if (ChatAttachAlertPollLayout.this.currentCell == null || (editField = ChatAttachAlertPollLayout.this.currentCell.getEditField()) == null) {
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
            ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
            chatAttachAlertPollLayout.isEmojiSearchOpened = i != 0;
            chatAttachAlertPollLayout.parentAlert.sizeNotifierFrameLayout.requestLayout();
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

    private static class EmptyView extends View {
        public EmptyView(Context context) {
            super(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00fd, code lost:
        
            if (r1.isEmojiSearchOpened != false) goto L20;
         */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0115  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            int adapterPosition;
            ChatAttachAlertPollLayout chatAttachAlertPollLayout;
            if (view.getTag() != null) {
                return;
            }
            view.setTag(1);
            PollEditTextCell pollEditTextCell = (PollEditTextCell) view.getParent();
            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell);
            if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                return;
            }
            ChatAttachAlertPollLayout.this.listView.setItemAnimator(ChatAttachAlertPollLayout.this.itemAnimator);
            int i = adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow;
            ChatAttachAlertPollLayout.this.listAdapter.notifyItemRemoved(adapterPosition);
            int i2 = i + 1;
            System.arraycopy(ChatAttachAlertPollLayout.this.answers, i2, ChatAttachAlertPollLayout.this.answers, i, (ChatAttachAlertPollLayout.this.answers.length - 1) - i);
            System.arraycopy(ChatAttachAlertPollLayout.this.answersChecks, i2, ChatAttachAlertPollLayout.this.answersChecks, i, (ChatAttachAlertPollLayout.this.answersChecks.length - 1) - i);
            ChatAttachAlertPollLayout.this.answers[ChatAttachAlertPollLayout.this.answers.length - 1] = null;
            ChatAttachAlertPollLayout.this.answersChecks[ChatAttachAlertPollLayout.this.answersChecks.length - 1] = false;
            ChatAttachAlertPollLayout.access$2010(ChatAttachAlertPollLayout.this);
            if (ChatAttachAlertPollLayout.this.answersCount == ChatAttachAlertPollLayout.this.answers.length - 1) {
                ChatAttachAlertPollLayout.this.listAdapter.notifyItemInserted((ChatAttachAlertPollLayout.this.answerStartRow + ChatAttachAlertPollLayout.this.answers.length) - 1);
            }
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(adapterPosition - 1);
            EditTextBoldCursor textView = pollEditTextCell.getTextView();
            if (findViewHolderForAdapterPosition != null) {
                View view2 = findViewHolderForAdapterPosition.itemView;
                if (view2 instanceof PollEditTextCell) {
                    ((PollEditTextCell) view2).getTextView().requestFocus();
                    textView.clearFocus();
                    ChatAttachAlertPollLayout.this.checkDoneButton();
                    ChatAttachAlertPollLayout.this.updateRows();
                    if (ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
                        ChatAttachAlertPollLayout.this.suggestEmojiPanel.forceClose();
                        ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDelegate(null);
                    }
                    ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.answerSectionRow);
                    ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.emptyRow);
                }
            }
            if (textView.isFocused()) {
                AndroidUtilities.hideKeyboard(textView);
                chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
            } else {
                chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
            }
            chatAttachAlertPollLayout.hideEmojiPopup(true);
            textView.clearFocus();
            ChatAttachAlertPollLayout.this.checkDoneButton();
            ChatAttachAlertPollLayout.this.updateRows();
            if (ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
            }
            ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.answerSectionRow);
            ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.emptyRow);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$1(PollEditTextCell pollEditTextCell, TextView textView, int i, KeyEvent keyEvent) {
            int adapterPosition;
            if (i != 5) {
                return false;
            }
            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell);
            if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                int i2 = adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow;
                if (i2 == ChatAttachAlertPollLayout.this.answersCount - 1 && ChatAttachAlertPollLayout.this.answersCount < 10) {
                    ChatAttachAlertPollLayout.this.addNewField();
                } else if (i2 == ChatAttachAlertPollLayout.this.answersCount - 1) {
                    AndroidUtilities.hideKeyboard(pollEditTextCell.getTextView());
                } else {
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(adapterPosition + 1);
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
            return ChatAttachAlertPollLayout.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ChatAttachAlertPollLayout.this.questionHeaderRow || i == ChatAttachAlertPollLayout.this.answerHeaderRow || i == ChatAttachAlertPollLayout.this.settingsHeaderRow) {
                return 0;
            }
            if (i == ChatAttachAlertPollLayout.this.questionSectionRow) {
                return 1;
            }
            if (i == ChatAttachAlertPollLayout.this.answerSectionRow || i == ChatAttachAlertPollLayout.this.settingsSectionRow || i == ChatAttachAlertPollLayout.this.solutionInfoRow) {
                return 2;
            }
            if (i == ChatAttachAlertPollLayout.this.addAnswerRow) {
                return 3;
            }
            if (i == ChatAttachAlertPollLayout.this.questionRow) {
                return 4;
            }
            if (i == ChatAttachAlertPollLayout.this.solutionRow) {
                return 7;
            }
            if (i == ChatAttachAlertPollLayout.this.anonymousRow || i == ChatAttachAlertPollLayout.this.multipleRow || i == ChatAttachAlertPollLayout.this.quizRow) {
                return 6;
            }
            if (i == ChatAttachAlertPollLayout.this.emptyRow) {
                return 8;
            }
            return i == ChatAttachAlertPollLayout.this.paddingRow ? 9 : 5;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == ChatAttachAlertPollLayout.this.addAnswerRow || adapterPosition == ChatAttachAlertPollLayout.this.anonymousRow || adapterPosition == ChatAttachAlertPollLayout.this.multipleRow || (ChatAttachAlertPollLayout.this.quizOnly == 0 && adapterPosition == ChatAttachAlertPollLayout.this.quizRow);
        }

        /* JADX WARN: Code restructure failed: missing block: B:38:0x010d, code lost:
        
            if (r8.this$0.quizRow == (-1)) goto L40;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x012a, code lost:
        
            if (r8.this$0.quizRow != (-1)) goto L39;
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
                if (i == ChatAttachAlertPollLayout.this.questionHeaderRow) {
                    headerCell.getTextView().setGravity(19);
                    i2 = R.string.PollQuestion;
                } else {
                    headerCell.getTextView().setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                    if (i == ChatAttachAlertPollLayout.this.answerHeaderRow) {
                        i2 = ChatAttachAlertPollLayout.this.quizOnly == 1 ? R.string.QuizAnswers : R.string.AnswerOptions;
                    } else if (i != ChatAttachAlertPollLayout.this.settingsHeaderRow) {
                        return;
                    } else {
                        i2 = R.string.Settings;
                    }
                }
                headerCell.setText(LocaleController.getString(i2));
                return;
            }
            if (itemViewType == 6) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == ChatAttachAlertPollLayout.this.anonymousRow) {
                    string = LocaleController.getString(R.string.PollAnonymous);
                    z = ChatAttachAlertPollLayout.this.anonymousPoll;
                    if (ChatAttachAlertPollLayout.this.multipleRow == -1) {
                    }
                    r0 = true;
                    textCheckCell.setTextAndCheck(string, z, r0);
                    textCheckCell.setEnabled(true, null);
                } else if (i == ChatAttachAlertPollLayout.this.multipleRow) {
                    string = LocaleController.getString(R.string.PollMultiple);
                    z = ChatAttachAlertPollLayout.this.multipleChoise;
                } else if (i == ChatAttachAlertPollLayout.this.quizRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.PollQuiz), ChatAttachAlertPollLayout.this.quizPoll, false);
                    textCheckCell.setEnabled(ChatAttachAlertPollLayout.this.quizOnly == 0, null);
                }
            } else if (itemViewType != 9) {
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        return;
                    }
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText4);
                    Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
                    Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
                    int themedColor = ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_switchTrackChecked);
                    PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                    drawable.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
                    drawable2.setColorFilter(new PorterDuffColorFilter(ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_checkboxCheck), mode));
                    textCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.AddAnOption), (Drawable) new CombinedDrawable(drawable, drawable2), false);
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                combinedDrawable.setFullsize(true);
                textInfoPrivacyCell.setBackgroundDrawable(combinedDrawable);
                if (i == ChatAttachAlertPollLayout.this.solutionInfoRow) {
                    i3 = R.string.AddAnExplanationInfo;
                } else if (i == ChatAttachAlertPollLayout.this.settingsSectionRow) {
                    if (ChatAttachAlertPollLayout.this.quizOnly != 0) {
                        textInfoPrivacyCell.setText(null);
                        return;
                    }
                    i3 = R.string.QuizInfo;
                } else {
                    if (10 - ChatAttachAlertPollLayout.this.answersCount > 0) {
                        formatString = LocaleController.formatString("AddAnOptionInfo", R.string.AddAnOptionInfo, LocaleController.formatPluralString("Option", 10 - ChatAttachAlertPollLayout.this.answersCount, new Object[0]));
                        textInfoPrivacyCell.setText(formatString);
                        return;
                    }
                    i3 = R.string.AddAnOptionInfoMax;
                }
                formatString = LocaleController.getString(i3);
                textInfoPrivacyCell.setText(formatString);
                return;
            }
            viewHolder.itemView.requestLayout();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextWatcher textWatcher;
            PollEditTextCell pollEditTextCell;
            View view;
            switch (i) {
                case 0:
                    view = new HeaderCell(this.mContext, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, false);
                    break;
                case 1:
                    View shadowSectionCell = new ShadowSectionCell(this.mContext);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable.setFullsize(true);
                    shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                    view = shadowSectionCell;
                    break;
                case 2:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 3:
                    view = new TextCell(this.mContext);
                    break;
                case 4:
                    final PollEditTextCell pollEditTextCell2 = new PollEditTextCell(this.mContext, false, ChatAttachAlertPollLayout.this.isPremium ? 1 : 0, null) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.1
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onActionModeStart(EditTextBoldCursor editTextBoldCursor, ActionMode actionMode) {
                            if (editTextBoldCursor.isFocused() && editTextBoldCursor.hasSelection()) {
                                Menu menu = actionMode.getMenu();
                                if (menu.findItem(android.R.id.copy) == null) {
                                    return;
                                }
                                ChatActivity.fillActionModeMenu(menu, ((ChatActivity) ChatAttachAlertPollLayout.this.parentAlert.baseFragment).getCurrentEncryptedChat(), true);
                            }
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onEditTextFocusChanged(boolean z) {
                            ChatAttachAlertPollLayout.this.onCellFocusChanges(this, z);
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        /* renamed from: onEmojiButtonClicked */
                        public void lambda$new$1(PollEditTextCell pollEditTextCell3) {
                            ChatAttachAlertPollLayout.this.onEmojiClicked(pollEditTextCell3);
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                            ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
                        }
                    };
                    pollEditTextCell2.createErrorTextView();
                    textWatcher = new TextWatcher() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.2
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (pollEditTextCell2.getTag() != null) {
                                return;
                            }
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(ChatAttachAlertPollLayout.this.questionRow);
                            if (findViewHolderForAdapterPosition != null && ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
                                for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                    editable.removeSpan(imageSpan);
                                }
                                Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell2.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDirection(1);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDelegate(pollEditTextCell2);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY(findViewHolderForAdapterPosition.itemView.getY());
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.fireUpdate();
                            }
                            ChatAttachAlertPollLayout.this.questionString = editable;
                            if (findViewHolderForAdapterPosition != null) {
                                ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                                chatAttachAlertPollLayout.setTextLeft(findViewHolderForAdapterPosition.itemView, chatAttachAlertPollLayout.questionRow);
                            }
                            ChatAttachAlertPollLayout.this.checkDoneButton();
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }
                    };
                    pollEditTextCell = pollEditTextCell2;
                    pollEditTextCell.addTextWatcher(textWatcher);
                    view = pollEditTextCell;
                    break;
                case 5:
                default:
                    Context context = this.mContext;
                    boolean z = ChatAttachAlertPollLayout.this.isPremium;
                    final PollEditTextCell pollEditTextCell3 = new PollEditTextCell(context, false, z ? 1 : 0, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatAttachAlertPollLayout.ListAdapter.this.lambda$onCreateViewHolder$0(view2);
                        }
                    }) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.6
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected boolean drawDivider() {
                            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(this);
                            if (findContainingViewHolder != null) {
                                int adapterPosition = findContainingViewHolder.getAdapterPosition();
                                if (ChatAttachAlertPollLayout.this.answersCount == 10 && adapterPosition == (ChatAttachAlertPollLayout.this.answerStartRow + ChatAttachAlertPollLayout.this.answersCount) - 1) {
                                    return false;
                                }
                            }
                            return true;
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected boolean isChecked(PollEditTextCell pollEditTextCell4) {
                            int adapterPosition;
                            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell4);
                            if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                                return false;
                            }
                            return ChatAttachAlertPollLayout.this.answersChecks[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow];
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onCheckBoxClick(PollEditTextCell pollEditTextCell4, boolean z2) {
                            int adapterPosition;
                            if (z2 && ChatAttachAlertPollLayout.this.quizPoll) {
                                Arrays.fill(ChatAttachAlertPollLayout.this.answersChecks, false);
                                ChatAttachAlertPollLayout.this.listView.getChildCount();
                                for (int i2 = ChatAttachAlertPollLayout.this.answerStartRow; i2 < ChatAttachAlertPollLayout.this.answerStartRow + ChatAttachAlertPollLayout.this.answersCount; i2++) {
                                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(i2);
                                    if (findViewHolderForAdapterPosition != null) {
                                        View view2 = findViewHolderForAdapterPosition.itemView;
                                        if (view2 instanceof PollEditTextCell) {
                                            ((PollEditTextCell) view2).setChecked(false, true);
                                        }
                                    }
                                }
                            }
                            super.onCheckBoxClick(pollEditTextCell4, z2);
                            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell4);
                            if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                                ChatAttachAlertPollLayout.this.answersChecks[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow] = z2;
                            }
                            ChatAttachAlertPollLayout.this.checkDoneButton();
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onEditTextFocusChanged(boolean z2) {
                            ChatAttachAlertPollLayout.this.onCellFocusChanges(this, z2);
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        /* renamed from: onEmojiButtonClicked */
                        public void lambda$new$1(PollEditTextCell pollEditTextCell4) {
                            ChatAttachAlertPollLayout.this.onEmojiClicked(pollEditTextCell4);
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                            ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected boolean shouldShowCheckBox() {
                            return ChatAttachAlertPollLayout.this.quizPoll;
                        }
                    };
                    pollEditTextCell3.addTextWatcher(new TextWatcher() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.7
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            int adapterPosition;
                            int adapterPosition2;
                            RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell3);
                            if (findContainingViewHolder == null || (adapterPosition2 = (adapterPosition = findContainingViewHolder.getAdapterPosition()) - ChatAttachAlertPollLayout.this.answerStartRow) < 0 || adapterPosition2 >= ChatAttachAlertPollLayout.this.answers.length) {
                                return;
                            }
                            if (ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
                                for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                    editable.removeSpan(imageSpan);
                                }
                                Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell3.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                float y = (findContainingViewHolder.itemView.getY() - AndroidUtilities.dp(166.0f)) + findContainingViewHolder.itemView.getMeasuredHeight();
                                if (y > 0.0f) {
                                    ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDirection(0);
                                    ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY(y);
                                } else {
                                    ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDirection(1);
                                    ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY(findContainingViewHolder.itemView.getY());
                                }
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDelegate(pollEditTextCell3);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.fireUpdate();
                            }
                            ChatAttachAlertPollLayout.this.answers[adapterPosition2] = editable;
                            ChatAttachAlertPollLayout.this.setTextLeft(pollEditTextCell3, adapterPosition);
                            ChatAttachAlertPollLayout.this.checkDoneButton();
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
                    textView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda1
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i2, KeyEvent keyEvent) {
                            boolean lambda$onCreateViewHolder$1;
                            lambda$onCreateViewHolder$1 = ChatAttachAlertPollLayout.ListAdapter.this.lambda$onCreateViewHolder$1(pollEditTextCell3, textView2, i2, keyEvent);
                            return lambda$onCreateViewHolder$1;
                        }
                    });
                    textView.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnKeyListener
                        public final boolean onKey(View view2, int i2, KeyEvent keyEvent) {
                            boolean lambda$onCreateViewHolder$2;
                            lambda$onCreateViewHolder$2 = ChatAttachAlertPollLayout.ListAdapter.lambda$onCreateViewHolder$2(PollEditTextCell.this, view2, i2, keyEvent);
                            return lambda$onCreateViewHolder$2;
                        }
                    });
                    view = pollEditTextCell3;
                    break;
                case 6:
                    view = new TextCheckCell(this.mContext);
                    break;
                case 7:
                    final PollEditTextCell pollEditTextCell4 = new PollEditTextCell(this.mContext, false, ChatAttachAlertPollLayout.this.isPremium ? 1 : 0, null) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.3
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onActionModeStart(EditTextBoldCursor editTextBoldCursor, ActionMode actionMode) {
                            if (editTextBoldCursor.isFocused() && editTextBoldCursor.hasSelection()) {
                                Menu menu = actionMode.getMenu();
                                if (menu.findItem(android.R.id.copy) == null) {
                                    return;
                                }
                                ChatActivity.fillActionModeMenu(menu, ((ChatActivity) ChatAttachAlertPollLayout.this.parentAlert.baseFragment).getCurrentEncryptedChat(), true);
                            }
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onEditTextFocusChanged(boolean z2) {
                            ChatAttachAlertPollLayout.this.onCellFocusChanges(this, z2);
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        /* renamed from: onEmojiButtonClicked */
                        public void lambda$new$1(PollEditTextCell pollEditTextCell5) {
                            ChatAttachAlertPollLayout.this.onEmojiClicked(pollEditTextCell5);
                        }

                        @Override // org.telegram.ui.Cells.PollEditTextCell
                        protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                            ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
                        }
                    };
                    pollEditTextCell4.createErrorTextView();
                    textWatcher = new TextWatcher() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.4
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (pollEditTextCell4.getTag() != null) {
                                return;
                            }
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(ChatAttachAlertPollLayout.this.questionRow);
                            if (findViewHolderForAdapterPosition != null && ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
                                for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                                    editable.removeSpan(imageSpan);
                                }
                                Emoji.replaceEmoji((CharSequence) editable, pollEditTextCell4.getEditField().getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDirection(1);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setDelegate(pollEditTextCell4);
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY(findViewHolderForAdapterPosition.itemView.getY());
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.fireUpdate();
                            }
                            ChatAttachAlertPollLayout.this.solutionString = editable;
                            if (findViewHolderForAdapterPosition != null) {
                                ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                                chatAttachAlertPollLayout.setTextLeft(findViewHolderForAdapterPosition.itemView, chatAttachAlertPollLayout.solutionRow);
                            }
                            ChatAttachAlertPollLayout.this.checkDoneButton();
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }
                    };
                    pollEditTextCell = pollEditTextCell4;
                    pollEditTextCell.addTextWatcher(textWatcher);
                    view = pollEditTextCell;
                    break;
                case 8:
                    View emptyView = new EmptyView(this.mContext);
                    emptyView.setBackgroundColor(ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_windowBackgroundGray));
                    view = emptyView;
                    break;
                case 9:
                    view = new View(this.mContext) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.ListAdapter.5
                        @Override // android.view.View
                        protected void onMeasure(int i2, int i3) {
                            setMeasuredDimension(View.MeasureSpec.getSize(i2), ChatAttachAlertPollLayout.this.topPadding);
                        }
                    };
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
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
                str = ChatAttachAlertPollLayout.this.questionString != null ? ChatAttachAlertPollLayout.this.questionString : "";
                i = R.string.QuestionHint;
            } else {
                if (itemViewType == 5) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    PollEditTextCell pollEditTextCell2 = (PollEditTextCell) viewHolder.itemView;
                    pollEditTextCell2.setTag(1);
                    pollEditTextCell2.setTextAndHint(ChatAttachAlertPollLayout.this.answers[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow], LocaleController.getString(R.string.OptionHint), true);
                    pollEditTextCell2.setTag(null);
                    if (ChatAttachAlertPollLayout.this.requestFieldFocusAtPosition == adapterPosition) {
                        EditTextBoldCursor textView = pollEditTextCell2.getTextView();
                        textView.requestFocus();
                        AndroidUtilities.showKeyboard(textView);
                        ChatAttachAlertPollLayout.this.requestFieldFocusAtPosition = -1;
                    }
                    ChatAttachAlertPollLayout.this.setTextLeft(viewHolder.itemView, adapterPosition);
                    return;
                }
                if (itemViewType != 7) {
                    return;
                }
                pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                str = ChatAttachAlertPollLayout.this.solutionString != null ? ChatAttachAlertPollLayout.this.solutionString : "";
                i = R.string.AddAnExplanation;
            }
            pollEditTextCell.setTextAndHint(str, LocaleController.getString(i), false);
            pollEditTextCell.setTag(null);
            ChatAttachAlertPollLayout.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 5) {
                EditTextBoldCursor textView = ((PollEditTextCell) viewHolder.itemView).getTextView();
                if (textView.isFocused()) {
                    if (ChatAttachAlertPollLayout.this.isPremium) {
                        if (ChatAttachAlertPollLayout.this.suggestEmojiPanel != null) {
                            ChatAttachAlertPollLayout.this.suggestEmojiPanel.forceClose();
                        }
                        ChatAttachAlertPollLayout.this.hideEmojiPopup(true);
                    }
                    ChatAttachAlertPollLayout.this.currentCell = null;
                    textView.clearFocus();
                    AndroidUtilities.hideKeyboard(textView);
                }
            }
        }

        public void swapElements(int i, int i2) {
            int i3 = i - ChatAttachAlertPollLayout.this.answerStartRow;
            int i4 = i2 - ChatAttachAlertPollLayout.this.answerStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= ChatAttachAlertPollLayout.this.answersCount || i4 >= ChatAttachAlertPollLayout.this.answersCount) {
                return;
            }
            CharSequence charSequence = ChatAttachAlertPollLayout.this.answers[i3];
            ChatAttachAlertPollLayout.this.answers[i3] = ChatAttachAlertPollLayout.this.answers[i4];
            ChatAttachAlertPollLayout.this.answers[i4] = charSequence;
            boolean z = ChatAttachAlertPollLayout.this.answersChecks[i3];
            ChatAttachAlertPollLayout.this.answersChecks[i3] = ChatAttachAlertPollLayout.this.answersChecks[i4];
            ChatAttachAlertPollLayout.this.answersChecks[i4] = z;
            notifyItemMoved(i, i2);
        }
    }

    public interface PollCreateActivityDelegate {
        void sendPoll(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i);
    }

    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
            viewHolder.itemView.setBackground(null);
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
            ChatAttachAlertPollLayout.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                ChatAttachAlertPollLayout.this.listView.setItemAnimator(ChatAttachAlertPollLayout.this.itemAnimator);
                ChatAttachAlertPollLayout.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
                viewHolder.itemView.setBackgroundColor(ChatAttachAlertPollLayout.this.getThemedColor(Theme.key_dialogBackground));
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    public ChatAttachAlertPollLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.answers = new CharSequence[10];
        this.answersChecks = new boolean[10];
        this.answersCount = 1;
        this.anonymousPoll = true;
        this.requestFieldFocusAtPosition = -1;
        this.openKeyboardRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.1
            @Override // java.lang.Runnable
            public void run() {
                if (ChatAttachAlertPollLayout.this.currentCell != null) {
                    EditTextBoldCursor editField = ChatAttachAlertPollLayout.this.currentCell.getEditField();
                    if (ChatAttachAlertPollLayout.this.destroyed || editField == null || !ChatAttachAlertPollLayout.this.waitingForKeyboardOpen || ChatAttachAlertPollLayout.this.keyboardVisible || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow || !AndroidUtilities.isTablet()) {
                        return;
                    }
                    editField.requestFocus();
                    AndroidUtilities.showKeyboard(editField);
                    AndroidUtilities.cancelRunOnUIThread(ChatAttachAlertPollLayout.this.openKeyboardRunnable);
                    AndroidUtilities.runOnUIThread(ChatAttachAlertPollLayout.this.openKeyboardRunnable, 100L);
                }
            }
        };
        this.isEmojiSearchOpened = false;
        this.wasEmojiSearchOpened = false;
        updateRows();
        this.isPremium = AccountInstance.getInstance(this.parentAlert.currentAccount).getUserConfig().isPremium();
        this.parentAlert.sizeNotifierFrameLayout.setDelegate(this);
        this.listAdapter = new ListAdapter(context);
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.2
            @Override // androidx.recyclerview.widget.RecyclerView
            protected void requestChildOnScreen(View view, View view2) {
                if (view instanceof PollEditTextCell) {
                    super.requestChildOnScreen(view, view2);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.ViewParent
            public boolean requestChildRectangleOnScreen(View view, android.graphics.Rect rect, boolean z) {
                rect.bottom += AndroidUtilities.dp(60.0f);
                return super.requestChildRectangleOnScreen(view, rect, z);
            }
        };
        this.listView = recyclerListView;
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.3
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition() == 0) {
                    ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                    chatAttachAlertPollLayout.parentAlert.updateLayout(chatAttachAlertPollLayout, true, 0);
                }
            }
        };
        this.itemAnimator = defaultItemAnimator;
        recyclerListView.setItemAnimator(defaultItemAnimator);
        this.listView.setClipToPadding(false);
        this.listView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        RecyclerListView recyclerListView2 = this.listView;
        FillLastLinearLayoutManager fillLastLinearLayoutManager = new FillLastLinearLayoutManager(context, 1, false, AndroidUtilities.dp(53.0f), this.listView) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.4
            @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
            protected int[] getChildRectangleOnScreenScrollAmount(View view, android.graphics.Rect rect) {
                int height = getHeight() - getPaddingBottom();
                int top = (view.getTop() + rect.top) - view.getScrollY();
                int height2 = rect.height() + top;
                int min = Math.min(0, top);
                int max = Math.max(0, height2 - height);
                if (min == 0) {
                    min = Math.min(top, max);
                }
                return new int[]{0, min};
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.4.1
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    public int calculateDyToMakeVisible(View view, int i2) {
                        return super.calculateDyToMakeVisible(view, i2) - (ChatAttachAlertPollLayout.this.topPadding - AndroidUtilities.dp(7.0f));
                    }

                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    protected int calculateTimeForDeceleration(int i2) {
                        return super.calculateTimeForDeceleration(i2) * 2;
                    }
                };
                linearSmoothScroller.setTargetPosition(i);
                startSmoothScroll(linearSmoothScroller);
            }
        };
        this.layoutManager = fillLastLinearLayoutManager;
        recyclerListView2.setLayoutManager(fillLastLinearLayoutManager);
        this.layoutManager.setSkipFirstItem();
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setPreserveFocusAfterLayout(true);
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                ChatAttachAlertPollLayout.this.lambda$new$0(view, i);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                RecyclerListView.Holder holder;
                if (i == 0) {
                    int dp = AndroidUtilities.dp(13.0f);
                    int backgroundPaddingTop = ChatAttachAlertPollLayout.this.parentAlert.getBackgroundPaddingTop();
                    if (((ChatAttachAlertPollLayout.this.parentAlert.scrollOffsetY[0] - backgroundPaddingTop) - dp) + backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || (holder = (RecyclerListView.Holder) ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(1)) == null || holder.itemView.getTop() <= AndroidUtilities.dp(53.0f)) {
                        return;
                    }
                    ChatAttachAlertPollLayout.this.listView.smoothScrollBy(0, holder.itemView.getTop() - AndroidUtilities.dp(53.0f));
                }
            }

            /* JADX WARN: Code restructure failed: missing block: B:15:0x008d, code lost:
            
                if (r5 <= r3.this$0.layoutManager.findLastVisibleItemPosition()) goto L19;
             */
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                chatAttachAlertPollLayout.parentAlert.updateLayout(chatAttachAlertPollLayout, true, i2);
                if (ChatAttachAlertPollLayout.this.suggestEmojiPanel != null && ChatAttachAlertPollLayout.this.suggestEmojiPanel.isShown()) {
                    SuggestEmojiView.AnchorViewDelegate delegate = ChatAttachAlertPollLayout.this.suggestEmojiPanel.getDelegate();
                    if (delegate instanceof PollEditTextCell) {
                        RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder((PollEditTextCell) delegate);
                        if (findContainingViewHolder != null) {
                            int adapterPosition = findContainingViewHolder.getAdapterPosition();
                            if (ChatAttachAlertPollLayout.this.suggestEmojiPanel.getDirection() == 0) {
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY((findContainingViewHolder.itemView.getY() - AndroidUtilities.dp(166.0f)) + findContainingViewHolder.itemView.getMeasuredHeight());
                            } else {
                                ChatAttachAlertPollLayout.this.suggestEmojiPanel.setTranslationY(findContainingViewHolder.itemView.getY());
                            }
                            if (adapterPosition >= ChatAttachAlertPollLayout.this.layoutManager.findFirstVisibleItemPosition()) {
                            }
                        }
                    }
                    ChatAttachAlertPollLayout.this.suggestEmojiPanel.forceClose();
                }
                if (i2 == 0 || ChatAttachAlertPollLayout.this.hintView == null) {
                    return;
                }
                ChatAttachAlertPollLayout.this.hintView.hide();
            }
        });
        HintView hintView = new HintView(context, 4);
        this.hintView = hintView;
        hintView.setText(LocaleController.getString(R.string.PollTapToSelect));
        this.hintView.setAlpha(0.0f);
        this.hintView.setVisibility(4);
        addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        if (this.isPremium) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
            SuggestEmojiView suggestEmojiView = new SuggestEmojiView(context, this.parentAlert.currentAccount, null, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.6
                @Override // org.telegram.ui.Components.SuggestEmojiView
                protected int emojiCacheType() {
                    return 3;
                }
            };
            this.suggestEmojiPanel = suggestEmojiView;
            suggestEmojiView.forbidCopy();
            this.suggestEmojiPanel.forbidSetAsStatus();
            this.suggestEmojiPanel.setHorizontalPadding(AndroidUtilities.dp(24.0f));
            addView(this.suggestEmojiPanel, LayoutHelper.createFrame(-2, NotificationCenter.audioRouteChanged, 51));
        }
        this.keyboardNotifier = new KeyboardNotifier(this.parentAlert.sizeNotifierFrameLayout, null);
        checkDoneButton();
    }

    static /* synthetic */ int access$2010(ChatAttachAlertPollLayout chatAttachAlertPollLayout) {
        int i = chatAttachAlertPollLayout.answersCount;
        chatAttachAlertPollLayout.answersCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addNewField() {
        resetSuggestEmojiPanel();
        this.listView.setItemAnimator(this.itemAnimator);
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
        this.listAdapter.notifyItemChanged(this.emptyRow);
    }

    private void animateEmojiViewTranslationY(final float f, final float f2) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatAttachAlertPollLayout.this.lambda$animateEmojiViewTranslationY$5(f, f2, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ChatAttachAlertPollLayout.this.emojiView.setTranslationY(f2);
            }
        });
        ofFloat.setDuration(250L);
        ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
        ofFloat.start();
    }

    private boolean checkDiscard() {
        boolean isEmpty = TextUtils.isEmpty(getFixedString(this.questionString));
        if (isEmpty) {
            for (int i = 0; i < this.answersCount && (isEmpty = TextUtils.isEmpty(getFixedString(this.answers[i]))); i++) {
            }
        }
        if (!isEmpty) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.parentAlert.baseFragment.getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.CancelPollAlertTitle));
            builder.setMessage(LocaleController.getString(R.string.CancelPollAlertText));
            builder.setPositiveButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    ChatAttachAlertPollLayout.this.lambda$checkDiscard$2(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.show();
        }
        return isEmpty;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDoneButton() {
        int i;
        if (this.quizPoll) {
            i = 0;
            for (int i2 = 0; i2 < this.answersChecks.length; i2++) {
                if (!TextUtils.isEmpty(getFixedString(this.answers[i2])) && this.answersChecks[i2]) {
                    i++;
                }
            }
        } else {
            i = 0;
        }
        boolean z = (TextUtils.isEmpty(getFixedString(this.solutionString)) || this.solutionString.length() <= 200) && !TextUtils.isEmpty(getFixedString(this.questionString)) && this.questionString.length() <= 255;
        int i3 = 0;
        int i4 = 0;
        boolean z2 = false;
        while (true) {
            CharSequence[] charSequenceArr = this.answers;
            if (i3 >= charSequenceArr.length) {
                break;
            }
            if (!TextUtils.isEmpty(getFixedString(charSequenceArr[i3]))) {
                if (this.answers[i3].length() > 100) {
                    i4 = 0;
                    z2 = true;
                    break;
                } else {
                    i4++;
                    z2 = true;
                }
            }
            i3++;
        }
        if (i4 < 2 || (this.quizPoll && i < 1)) {
            z = false;
        }
        if (TextUtils.isEmpty(this.solutionString) && TextUtils.isEmpty(this.questionString) && !z2) {
            this.allowNesterScroll = true;
        } else {
            this.allowNesterScroll = false;
        }
        this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
        this.parentAlert.doneItem.setEnabled((this.quizPoll && i == 0) || z);
        this.parentAlert.doneItem.setAlpha(z ? 1.0f : 0.5f);
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
            this.parentAlert.sizeNotifierFrameLayout.removeView(emojiView);
            this.emojiView = null;
        }
        if (this.emojiView != null) {
            return;
        }
        EmojiView emojiView2 = new EmojiView(null, true, false, false, getContext(), true, null, null, true, this.resourcesProvider, false);
        this.emojiView = emojiView2;
        emojiView2.emojiCacheType = 3;
        emojiView2.fixBottomTabContainerTranslation = false;
        emojiView2.allowEmojisForNonPremium(false);
        this.emojiView.setVisibility(8);
        if (AndroidUtilities.isTablet()) {
            this.emojiView.setForseMultiwindowLayout(true);
        }
        this.emojiView.setDelegate(new 9());
        this.parentAlert.sizeNotifierFrameLayout.addView(this.emojiView);
    }

    public static CharSequence getFixedString(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return charSequence;
        }
        CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence);
        while (TextUtils.indexOf(trimmedString, "\n\n\n") >= 0) {
            trimmedString = TextUtils.replace(trimmedString, new String[]{"\n\n\n"}, new CharSequence[]{"\n\n"});
        }
        while (TextUtils.indexOf(trimmedString, "\n\n\n") == 0) {
            trimmedString = TextUtils.replace(trimmedString, new String[]{"\n\n\n"}, new CharSequence[]{"\n\n"});
        }
        return trimmedString;
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
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatAttachAlertPollLayout.this.lambda$hideEmojiPopup$4(valueAnimator);
                    }
                });
                this.isAnimatePopupClosing = true;
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.8
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChatAttachAlertPollLayout.this.isAnimatePopupClosing = false;
                        ChatAttachAlertPollLayout.this.emojiView.setTranslationY(0.0f);
                        ChatAttachAlertPollLayout.this.hideEmojiView();
                    }
                });
                ofFloat.setDuration(250L);
                ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                ofFloat.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEmojiViewTranslationY$5(float f, float f2, ValueAnimator valueAnimator) {
        this.emojiView.setTranslationY(AndroidUtilities.lerp(f, f2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$2(DialogInterface dialogInterface, int i) {
        this.parentAlert.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideEmojiPopup$4(ValueAnimator valueAnimator) {
        this.emojiView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i) {
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
                    this.listView.setItemAnimator(this.itemAnimator);
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.quizRow);
                    if (findViewHolderForAdapterPosition != null) {
                        ((TextCheckCell) findViewHolderForAdapterPosition.itemView).setChecked(false);
                    } else {
                        this.listAdapter.notifyItemChanged(this.quizRow);
                    }
                    this.listAdapter.notifyItemRangeRemoved(i2, 2);
                    this.listAdapter.notifyItemChanged(this.emptyRow);
                }
            } else {
                if (this.quizOnly != 0) {
                    return;
                }
                this.listView.setItemAnimator(this.itemAnimator);
                z = !this.quizPoll;
                this.quizPoll = z;
                int i3 = this.solutionRow;
                updateRows();
                if (this.quizPoll) {
                    this.listAdapter.notifyItemRangeInserted(this.solutionRow, 2);
                } else {
                    this.listAdapter.notifyItemRangeRemoved(i3, 2);
                }
                this.listAdapter.notifyItemChanged(this.emptyRow);
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
    public /* synthetic */ void lambda$onMenuItemClick$1(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
        this.delegate.sendPoll(tL_messageMediaPoll, hashMap, z, i);
        this.parentAlert.dismiss(true);
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
        if (this.currentCell != null) {
            this.keyboardNotifier.awaitKeyboard();
            EditTextBoldCursor editField = this.currentCell.getEditField();
            editField.requestFocus();
            AndroidUtilities.showKeyboard(editField);
        }
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
            i3 = NotificationCenter.newLocationAvailable;
            if (charSequence != null) {
                i2 = NotificationCenter.newLocationAvailable;
                i5 = charSequence.length();
            } else {
                i4 = NotificationCenter.newLocationAvailable;
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
                textView2.setTextColor(getThemedColor(i7));
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
        ChatActivityEnterViewAnimatedIconView emojiButton;
        PollEditTextCell pollEditTextCell;
        if (this.isPremium) {
            if (i != 1) {
                PollEditTextCell pollEditTextCell2 = this.currentCell;
                emojiButton = pollEditTextCell2 != null ? pollEditTextCell2.getEmojiButton() : null;
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
                this.parentAlert.sizeNotifierFrameLayout.requestLayout();
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
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i2 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) emojiView3.getLayoutParams();
            layoutParams.height = i2;
            emojiView3.setLayoutParams(layoutParams);
            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet() && (pollEditTextCell = this.currentCell) != null) {
                AndroidUtilities.hideKeyboard(pollEditTextCell.getEditField());
            }
            this.emojiPadding = i2;
            this.keyboardNotifier.fire();
            this.parentAlert.sizeNotifierFrameLayout.requestLayout();
            PollEditTextCell pollEditTextCell3 = this.currentCell;
            emojiButton = pollEditTextCell3 != null ? pollEditTextCell3.getEmojiButton() : null;
            if (emojiButton != null) {
                emojiButton.setState(ChatActivityEnterViewAnimatedIconView.State.KEYBOARD, true);
            }
            if (z || this.keyboardVisible) {
                return;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.emojiPadding, 0.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlertPollLayout.this.lambda$showEmojiPopup$3(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatAttachAlertPollLayout.this.emojiView.setTranslationY(0.0f);
                }
            });
            ofFloat.setDuration(250L);
            ofFloat.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
            ofFloat.start();
        }
    }

    private void showQuizHint() {
        for (int i = this.answerStartRow; i < this.answerStartRow + this.answersCount; i++) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
            if (findViewHolderForAdapterPosition != null) {
                View view = findViewHolderForAdapterPosition.itemView;
                if (view instanceof PollEditTextCell) {
                    PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
                    if (pollEditTextCell.getTop() > AndroidUtilities.dp(40.0f)) {
                        SuggestEmojiView suggestEmojiView = this.suggestEmojiPanel;
                        if (suggestEmojiView != null) {
                            suggestEmojiView.forceClose();
                        }
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
        this.paddingRow = 0;
        this.questionHeaderRow = 1;
        this.questionRow = 2;
        this.questionSectionRow = 3;
        this.rowCount = 5;
        this.answerHeaderRow = 4;
        int i = this.answersCount;
        if (i != 0) {
            this.answerStartRow = 5;
            this.rowCount = 5 + i;
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
        TLRPC.Chat currentChat = ((ChatActivity) this.parentAlert.baseFragment).getCurrentChat();
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
        if (this.quizPoll) {
            this.solutionRow = i9;
            this.rowCount = i8 + 3;
            this.solutionInfoRow = i8 + 2;
        } else {
            this.solutionRow = -1;
            this.solutionInfoRow = -1;
        }
        int i10 = this.rowCount;
        this.rowCount = i10 + 1;
        this.emptyRow = i10;
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

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getButtonsHideOffset() {
        return AndroidUtilities.dp(70.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        View childAt;
        if (this.listView.getChildCount() <= 1 || (childAt = this.listView.getChildAt(1)) == null) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int y = ((int) childAt.getY()) - AndroidUtilities.dp(8.0f);
        int i = (y <= 0 || holder == null || holder.getAdapterPosition() != 1) ? 0 : y;
        if (y < 0 || holder == null || holder.getAdapterPosition() != 1) {
            y = i;
        }
        return y + AndroidUtilities.dp(25.0f);
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(17.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.topPadding;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_dialogScrollGlow));
        int i = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i));
        int i2 = Theme.key_windowBackgroundGray;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{EmptyView.class}, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
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

    public boolean isAnimatePopupClosing() {
        return this.isAnimatePopupClosing;
    }

    public boolean isPopupShowing() {
        return this.emojiViewVisible;
    }

    public boolean isPopupVisible() {
        EmojiView emojiView = this.emojiView;
        return emojiView != null && emojiView.getVisibility() == 0;
    }

    public boolean isWaitingForKeyboardOpen() {
        return this.waitingForKeyboardOpen;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int needsActionBar() {
        return 1;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        if (this.emojiViewVisible) {
            hideEmojiPopup(true);
            return true;
        }
        if (checkDiscard()) {
            return super.onBackPressed();
        }
        return true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onDestroy() {
        super.onDestroy();
        this.destroyed = true;
        if (this.isPremium) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                this.parentAlert.sizeNotifierFrameLayout.removeView(emojiView);
            }
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        this.parentAlert.doneItem.setVisibility(4);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHideShowProgress(float f) {
        ActionBarMenuItem actionBarMenuItem = this.parentAlert.doneItem;
        actionBarMenuItem.setAlpha((actionBarMenuItem.isEnabled() ? 1.0f : 0.5f) * f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onMenuItemClick(int i) {
        if (i == 40) {
            if (this.quizPoll && this.parentAlert.doneItem.getAlpha() != 1.0f) {
                int i2 = 0;
                for (int i3 = 0; i3 < this.answersChecks.length; i3++) {
                    if (!TextUtils.isEmpty(getFixedString(this.answers[i3])) && this.answersChecks[i3]) {
                        i2++;
                    }
                }
                if (i2 <= 0) {
                    showQuizHint();
                    return;
                }
                return;
            }
            CharSequence[] charSequenceArr = {getFixedString(this.questionString)};
            ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.parentAlert.currentAccount).getEntities(charSequenceArr, true);
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
            tL_poll.multiple_choice = this.multipleChoise;
            tL_poll.quiz = this.quizPoll;
            tL_poll.public_voters = !this.anonymousPoll;
            tL_poll.question = new TLRPC.TL_textWithEntities();
            tL_messageMediaPoll.poll.question.text = charSequence.toString();
            tL_messageMediaPoll.poll.question.entities = entities;
            SerializedData serializedData = new SerializedData(10);
            int i5 = 0;
            while (true) {
                CharSequence[] charSequenceArr2 = this.answers;
                if (i5 >= charSequenceArr2.length) {
                    break;
                }
                if (!TextUtils.isEmpty(getFixedString(charSequenceArr2[i5]))) {
                    CharSequence[] charSequenceArr3 = {getFixedString(this.answers[i5])};
                    ArrayList<TLRPC.MessageEntity> entities2 = MediaDataController.getInstance(this.parentAlert.currentAccount).getEntities(charSequenceArr3, true);
                    CharSequence charSequence2 = charSequenceArr3[0];
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
                    tL_pollAnswer.option = new byte[]{(byte) (tL_messageMediaPoll.poll.answers.size() + 48)};
                    tL_messageMediaPoll.poll.answers.add(tL_pollAnswer);
                    if ((this.multipleChoise || this.quizPoll) && this.answersChecks[i5]) {
                        serializedData.writeByte(tL_pollAnswer.option[0]);
                    }
                }
                i5++;
            }
            final HashMap hashMap = new HashMap();
            hashMap.put("answers", Utilities.bytesToHex(serializedData.toByteArray()));
            tL_messageMediaPoll.results = new TLRPC.TL_pollResults();
            CharSequence fixedString = getFixedString(this.solutionString);
            if (fixedString != null) {
                tL_messageMediaPoll.results.solution = fixedString.toString();
                ArrayList<TLRPC.MessageEntity> entities3 = MediaDataController.getInstance(this.parentAlert.currentAccount).getEntities(new CharSequence[]{fixedString}, true);
                if (entities3 != null && !entities3.isEmpty()) {
                    tL_messageMediaPoll.results.solution_entities = entities3;
                }
                if (!TextUtils.isEmpty(tL_messageMediaPoll.results.solution)) {
                    tL_messageMediaPoll.results.flags |= 16;
                }
            }
            ChatActivity chatActivity = (ChatActivity) this.parentAlert.baseFragment;
            if (chatActivity.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(chatActivity.getParentActivity(), chatActivity.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlertPollLayout$$ExternalSyntheticLambda4
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i7) {
                        ChatAttachAlertPollLayout.this.lambda$onMenuItemClick$1(tL_messageMediaPoll, hashMap, z, i7);
                    }
                });
            } else {
                this.delegate.sendPoll(tL_messageMediaPoll, hashMap, true, 0);
                this.parentAlert.dismiss(true);
            }
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPause() {
        super.onPause();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
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

    /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPreMeasure(int i, int i2) {
        int dp;
        int i3;
        if (this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f) || this.emojiViewVisible || this.isAnimatePopupClosing || this.isEmojiSearchOpened) {
            dp = AndroidUtilities.dp(52.0f);
            this.parentAlert.setAllowNestedScroll(false);
        } else {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i3 = (int) (i2 / 3.5f);
                    dp = i3 - AndroidUtilities.dp(13.0f);
                    if (dp < 0) {
                        dp = 0;
                    }
                    this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
                }
            }
            i3 = (i2 / 5) * 2;
            dp = i3 - AndroidUtilities.dp(13.0f);
            if (dp < 0) {
            }
            this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
        }
        this.ignoreLayout = true;
        if (this.topPadding != dp) {
            this.topPadding = dp;
            this.listView.setItemAnimator(null);
            this.listAdapter.notifyItemChanged(this.paddingRow);
        }
        this.ignoreLayout = false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        ActionBar actionBar;
        int i;
        try {
            this.parentAlert.actionBar.getTitleTextView().setBuildFullLayout(true);
        } catch (Exception unused) {
        }
        if (this.quizOnly == 1) {
            actionBar = this.parentAlert.actionBar;
            i = R.string.NewQuiz;
        } else {
            actionBar = this.parentAlert.actionBar;
            i = R.string.NewPoll;
        }
        actionBar.setTitle(LocaleController.getString(i));
        this.parentAlert.doneItem.setVisibility(0);
        this.layoutManager.scrollToPositionWithOffset(0, 0);
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
                    this.parentAlert.sizeNotifierFrameLayout.requestLayout();
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
                this.parentAlert.sizeNotifierFrameLayout.requestLayout();
            }
            if (this.keyboardVisible && this.waitingForKeyboardOpen) {
                this.waitingForKeyboardOpen = false;
                AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
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

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        this.listView.smoothScrollToPosition(1);
    }

    public void setDelegate(PollCreateActivityDelegate pollCreateActivityDelegate) {
        this.delegate = pollCreateActivityDelegate;
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }
}
