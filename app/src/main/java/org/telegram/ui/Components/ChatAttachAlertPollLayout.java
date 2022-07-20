package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_poll;
import org.telegram.tgnet.TLRPC$TL_pollAnswer;
import org.telegram.tgnet.TLRPC$TL_pollResults;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
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
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ChatAttachAlertPollLayout extends ChatAttachAlert.AttachAlertLayout {
    private int addAnswerRow;
    private boolean allowNesterScroll;
    private int anonymousRow;
    private int answerHeaderRow;
    private int answerSectionRow;
    private int answerStartRow;
    private PollCreateActivityDelegate delegate;
    private int emptyRow;
    private boolean hintShowed;
    private HintView hintView;
    private boolean ignoreLayout;
    private SimpleItemAnimator itemAnimator;
    private FillLastLinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean multipleChoise;
    private int multipleRow;
    private int paddingRow;
    private int questionHeaderRow;
    private int questionRow;
    private int questionSectionRow;
    private String questionString;
    private int quizOnly;
    private boolean quizPoll;
    private int quizRow;
    private int rowCount;
    private int settingsHeaderRow;
    private int settingsSectionRow;
    private int solutionInfoRow;
    private int solutionRow;
    private CharSequence solutionString;
    private int topPadding;
    private String[] answers = new String[10];
    private boolean[] answersChecks = new boolean[10];
    private int answersCount = 1;
    private boolean anonymousPoll = true;
    private int requestFieldFocusAtPosition = -1;

    /* loaded from: classes3.dex */
    public interface PollCreateActivityDelegate {
        void sendPoll(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, HashMap<String, String> hashMap, boolean z, int i);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int needsActionBar() {
        return 1;
    }

    static /* synthetic */ int access$1210(ChatAttachAlertPollLayout chatAttachAlertPollLayout) {
        int i = chatAttachAlertPollLayout.answersCount;
        chatAttachAlertPollLayout.answersCount = i - 1;
        return i;
    }

    /* loaded from: classes3.dex */
    private static class EmptyView extends View {
        public EmptyView(Context context) {
            super(context);
        }
    }

    /* loaded from: classes3.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public TouchHelperCallback() {
            ChatAttachAlertPollLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 5) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
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
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                ChatAttachAlertPollLayout.this.listView.setItemAnimator(ChatAttachAlertPollLayout.this.itemAnimator);
                ChatAttachAlertPollLayout.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
                viewHolder.itemView.setBackgroundColor(ChatAttachAlertPollLayout.this.getThemedColor("dialogBackground"));
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
            viewHolder.itemView.setBackground(null);
        }
    }

    public ChatAttachAlertPollLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        updateRows();
        this.listAdapter = new ListAdapter(context);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, context);
        this.listView = anonymousClass1;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        this.itemAnimator = anonymousClass2;
        anonymousClass1.setItemAnimator(anonymousClass2);
        this.listView.setClipToPadding(false);
        this.listView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        RecyclerListView recyclerListView = this.listView;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, 1, false, AndroidUtilities.dp(53.0f), this.listView);
        this.layoutManager = anonymousClass3;
        recyclerListView.setLayoutManager(anonymousClass3);
        this.layoutManager.setSkipFirstItem();
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setPreserveFocusAfterLayout(true);
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new ChatAttachAlertPollLayout$$ExternalSyntheticLambda2(this));
        this.listView.setOnScrollListener(new AnonymousClass4());
        HintView hintView = new HintView(context, 4);
        this.hintView = hintView;
        hintView.setText(LocaleController.getString("PollTapToSelect", 2131627588));
        this.hintView.setAlpha(0.0f);
        this.hintView.setVisibility(4);
        addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        checkDoneButton();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends RecyclerListView {
        AnonymousClass1(ChatAttachAlertPollLayout chatAttachAlertPollLayout, Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView
        public void requestChildOnScreen(View view, View view2) {
            if (!(view instanceof PollEditTextCell)) {
                return;
            }
            super.requestChildOnScreen(view, view2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, android.graphics.Rect rect, boolean z) {
            rect.bottom += AndroidUtilities.dp(60.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends DefaultItemAnimator {
        AnonymousClass2() {
            ChatAttachAlertPollLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() == 0) {
                ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                chatAttachAlertPollLayout.parentAlert.updateLayout(chatAttachAlertPollLayout, true, 0);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends FillLastLinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, int i, boolean z, int i2, RecyclerView recyclerView) {
            super(context, i, z, i2, recyclerView);
            ChatAttachAlertPollLayout.this = r7;
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends LinearSmoothScroller {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                AnonymousClass3.this = r1;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateDyToMakeVisible(View view, int i) {
                return super.calculateDyToMakeVisible(view, i) - (ChatAttachAlertPollLayout.this.topPadding - AndroidUtilities.dp(7.0f));
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateTimeForDeceleration(int i) {
                return super.calculateTimeForDeceleration(i) * 2;
            }
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(recyclerView.getContext());
            anonymousClass1.setTargetPosition(i);
            startSmoothScroll(anonymousClass1);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        protected int[] getChildRectangleOnScreenScrollAmount(View view, android.graphics.Rect rect) {
            int[] iArr = new int[2];
            int height = getHeight() - getPaddingBottom();
            int top = (view.getTop() + rect.top) - view.getScrollY();
            int height2 = rect.height() + top;
            int i = top - 0;
            int min = Math.min(0, i);
            int max = Math.max(0, height2 - height);
            if (min == 0) {
                min = Math.min(i, max);
            }
            iArr[0] = 0;
            iArr[1] = min;
            return iArr;
        }
    }

    public /* synthetic */ void lambda$new$0(View view, int i) {
        boolean z;
        if (i == this.addAnswerRow) {
            addNewField();
        } else if (view instanceof TextCheckCell) {
            TextCheckCell textCheckCell = (TextCheckCell) view;
            boolean z2 = this.quizPoll;
            if (i == this.anonymousRow) {
                z = !this.anonymousPoll;
                this.anonymousPoll = z;
            } else if (i == this.multipleRow) {
                z = !this.multipleChoise;
                this.multipleChoise = z;
                if (z && z2) {
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
            } else if (this.quizOnly != 0) {
                return;
            } else {
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

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends RecyclerView.OnScrollListener {
        AnonymousClass4() {
            ChatAttachAlertPollLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
            chatAttachAlertPollLayout.parentAlert.updateLayout(chatAttachAlertPollLayout, true, i2);
            if (i2 == 0 || ChatAttachAlertPollLayout.this.hintView == null) {
                return;
            }
            ChatAttachAlertPollLayout.this.hintView.hide();
        }

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
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onResume() {
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onHideShowProgress(float f) {
        ActionBarMenuItem actionBarMenuItem = this.parentAlert.doneItem;
        actionBarMenuItem.setAlpha((actionBarMenuItem.isEnabled() ? 1.0f : 0.5f) * f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onMenuItemClick(int i) {
        if (i == 40) {
            if (this.quizPoll && this.parentAlert.doneItem.getAlpha() != 1.0f) {
                int i2 = 0;
                for (int i3 = 0; i3 < this.answersChecks.length; i3++) {
                    if (!TextUtils.isEmpty(getFixedString(this.answers[i3])) && this.answersChecks[i3]) {
                        i2++;
                    }
                }
                if (i2 > 0) {
                    return;
                }
                showQuizHint();
                return;
            }
            TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = new TLRPC$TL_messageMediaPoll();
            TLRPC$TL_poll tLRPC$TL_poll = new TLRPC$TL_poll();
            tLRPC$TL_messageMediaPoll.poll = tLRPC$TL_poll;
            tLRPC$TL_poll.multiple_choice = this.multipleChoise;
            tLRPC$TL_poll.quiz = this.quizPoll;
            tLRPC$TL_poll.public_voters = !this.anonymousPoll;
            tLRPC$TL_poll.question = getFixedString(this.questionString).toString();
            SerializedData serializedData = new SerializedData(10);
            int i4 = 0;
            while (true) {
                String[] strArr = this.answers;
                if (i4 >= strArr.length) {
                    break;
                }
                if (!TextUtils.isEmpty(getFixedString(strArr[i4]))) {
                    TLRPC$TL_pollAnswer tLRPC$TL_pollAnswer = new TLRPC$TL_pollAnswer();
                    tLRPC$TL_pollAnswer.text = getFixedString(this.answers[i4]).toString();
                    tLRPC$TL_pollAnswer.option = r5;
                    byte[] bArr = {(byte) (tLRPC$TL_messageMediaPoll.poll.answers.size() + 48)};
                    tLRPC$TL_messageMediaPoll.poll.answers.add(tLRPC$TL_pollAnswer);
                    if ((this.multipleChoise || this.quizPoll) && this.answersChecks[i4]) {
                        serializedData.writeByte(tLRPC$TL_pollAnswer.option[0]);
                    }
                }
                i4++;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("answers", Utilities.bytesToHex(serializedData.toByteArray()));
            tLRPC$TL_messageMediaPoll.results = new TLRPC$TL_pollResults();
            CharSequence fixedString = getFixedString(this.solutionString);
            if (fixedString != null) {
                tLRPC$TL_messageMediaPoll.results.solution = fixedString.toString();
                ArrayList<TLRPC$MessageEntity> entities = MediaDataController.getInstance(this.parentAlert.currentAccount).getEntities(new CharSequence[]{fixedString}, true);
                if (entities != null && !entities.isEmpty()) {
                    tLRPC$TL_messageMediaPoll.results.solution_entities = entities;
                }
                if (!TextUtils.isEmpty(tLRPC$TL_messageMediaPoll.results.solution)) {
                    tLRPC$TL_messageMediaPoll.results.flags |= 16;
                }
            }
            ChatActivity chatActivity = (ChatActivity) this.parentAlert.baseFragment;
            if (chatActivity.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(chatActivity.getParentActivity(), chatActivity.getDialogId(), new ChatAttachAlertPollLayout$$ExternalSyntheticLambda1(this, tLRPC$TL_messageMediaPoll, hashMap));
                return;
            }
            this.delegate.sendPoll(tLRPC$TL_messageMediaPoll, hashMap, true, 0);
            this.parentAlert.dismiss(true);
        }
    }

    public /* synthetic */ void lambda$onMenuItemClick$1(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
        this.delegate.sendPoll(tLRPC$TL_messageMediaPoll, hashMap, z, i);
        this.parentAlert.dismiss(true);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        View childAt;
        if (this.listView.getChildCount() > 1 && (childAt = this.listView.getChildAt(1)) != null) {
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
            int y = ((int) childAt.getY()) - AndroidUtilities.dp(8.0f);
            int i = (y <= 0 || holder == null || holder.getAdapterPosition() != 1) ? 0 : y;
            if (y < 0 || holder == null || holder.getAdapterPosition() != 1) {
                y = i;
            }
            return y + AndroidUtilities.dp(25.0f);
        }
        return Integer.MAX_VALUE;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(17.0f);
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.topPadding;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003e  */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void onPreMeasure(int i, int i2) {
        int i3;
        int i4;
        if (this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            i3 = AndroidUtilities.dp(52.0f);
            this.parentAlert.setAllowNestedScroll(false);
        } else {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i4 = (int) (i2 / 3.5f);
                    i3 = i4 - AndroidUtilities.dp(13.0f);
                    if (i3 < 0) {
                        i3 = 0;
                    }
                    this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
                }
            }
            i4 = (i2 / 5) * 2;
            i3 = i4 - AndroidUtilities.dp(13.0f);
            if (i3 < 0) {
            }
            this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
        }
        this.ignoreLayout = true;
        if (this.topPadding != i3) {
            this.topPadding = i3;
            this.listView.setItemAnimator(null);
            this.listAdapter.notifyItemChanged(this.paddingRow);
        }
        this.ignoreLayout = false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getButtonsHideOffset() {
        return AndroidUtilities.dp(70.0f);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void scrollToTop() {
        this.listView.smoothScrollToPosition(1);
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

    private void showQuizHint() {
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

    public void checkDoneButton() {
        int i;
        boolean z = false;
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
        boolean z2 = (TextUtils.isEmpty(getFixedString(this.solutionString)) || this.solutionString.length() <= 200) && !TextUtils.isEmpty(getFixedString(this.questionString)) && this.questionString.length() <= 255;
        int i3 = 0;
        int i4 = 0;
        boolean z3 = false;
        while (true) {
            String[] strArr = this.answers;
            if (i3 >= strArr.length) {
                break;
            }
            if (!TextUtils.isEmpty(getFixedString(strArr[i3]))) {
                if (this.answers[i3].length() > 100) {
                    i4 = 0;
                    z3 = true;
                    break;
                }
                i4++;
                z3 = true;
            }
            i3++;
        }
        if (i4 < 2 || (this.quizPoll && i < 1)) {
            z2 = false;
        }
        if (!TextUtils.isEmpty(this.solutionString) || !TextUtils.isEmpty(this.questionString) || z3) {
            this.allowNesterScroll = false;
        } else {
            this.allowNesterScroll = true;
        }
        this.parentAlert.setAllowNestedScroll(this.allowNesterScroll);
        ActionBarMenuItem actionBarMenuItem = this.parentAlert.doneItem;
        if ((this.quizPoll && i == 0) || z2) {
            z = true;
        }
        actionBarMenuItem.setEnabled(z);
        this.parentAlert.doneItem.setAlpha(z2 ? 1.0f : 0.5f);
    }

    public void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.paddingRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.questionHeaderRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.questionRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.questionSectionRow = i3;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.answerHeaderRow = i4;
        int i6 = this.answersCount;
        if (i6 != 0) {
            this.answerStartRow = i5;
            this.rowCount = i5 + i6;
        } else {
            this.answerStartRow = -1;
        }
        if (i6 != this.answers.length) {
            int i7 = this.rowCount;
            this.rowCount = i7 + 1;
            this.addAnswerRow = i7;
        } else {
            this.addAnswerRow = -1;
        }
        int i8 = this.rowCount;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.answerSectionRow = i8;
        this.rowCount = i9 + 1;
        this.settingsHeaderRow = i9;
        TLRPC$Chat currentChat = ((ChatActivity) this.parentAlert.baseFragment).getCurrentChat();
        if (!ChatObject.isChannel(currentChat) || currentChat.megagroup) {
            int i10 = this.rowCount;
            this.rowCount = i10 + 1;
            this.anonymousRow = i10;
        } else {
            this.anonymousRow = -1;
        }
        int i11 = this.quizOnly;
        if (i11 != 1) {
            int i12 = this.rowCount;
            this.rowCount = i12 + 1;
            this.multipleRow = i12;
        } else {
            this.multipleRow = -1;
        }
        if (i11 == 0) {
            int i13 = this.rowCount;
            this.rowCount = i13 + 1;
            this.quizRow = i13;
        } else {
            this.quizRow = -1;
        }
        int i14 = this.rowCount;
        int i15 = i14 + 1;
        this.rowCount = i15;
        this.settingsSectionRow = i14;
        if (this.quizPoll) {
            int i16 = i15 + 1;
            this.rowCount = i16;
            this.solutionRow = i15;
            this.rowCount = i16 + 1;
            this.solutionInfoRow = i16;
        } else {
            this.solutionRow = -1;
            this.solutionInfoRow = -1;
        }
        int i17 = this.rowCount;
        this.rowCount = i17 + 1;
        this.emptyRow = i17;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        if (this.quizOnly == 1) {
            this.parentAlert.actionBar.setTitle(LocaleController.getString("NewQuiz", 2131626792));
        } else {
            this.parentAlert.actionBar.setTitle(LocaleController.getString("NewPoll", 2131626791));
        }
        this.parentAlert.doneItem.setVisibility(0);
        this.layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        this.parentAlert.doneItem.setVisibility(4);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        if (!checkDiscard()) {
            return true;
        }
        return super.onBackPressed();
    }

    private boolean checkDiscard() {
        boolean isEmpty = TextUtils.isEmpty(getFixedString(this.questionString));
        if (isEmpty) {
            for (int i = 0; i < this.answersCount && (isEmpty = TextUtils.isEmpty(getFixedString(this.answers[i]))); i++) {
            }
        }
        if (!isEmpty) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.parentAlert.baseFragment.getParentActivity());
            builder.setTitle(LocaleController.getString("CancelPollAlertTitle", 2131624837));
            builder.setMessage(LocaleController.getString("CancelPollAlertText", 2131624836));
            builder.setPositiveButton(LocaleController.getString("PassportDiscard", 2131627220), new ChatAttachAlertPollLayout$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            builder.show();
        }
        return isEmpty;
    }

    public /* synthetic */ void lambda$checkDiscard$2(DialogInterface dialogInterface, int i) {
        this.parentAlert.dismiss();
    }

    public void setDelegate(PollCreateActivityDelegate pollCreateActivityDelegate) {
        this.delegate = pollCreateActivityDelegate;
    }

    public void setTextLeft(View view, int i) {
        int i2;
        if (!(view instanceof PollEditTextCell)) {
            return;
        }
        PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
        int i3 = 100;
        if (i == this.questionRow) {
            String str = this.questionString;
            i2 = 255 - (str != null ? str.length() : 0);
            i3 = 255;
        } else if (i == this.solutionRow) {
            CharSequence charSequence = this.solutionString;
            i2 = 200 - (charSequence != null ? charSequence.length() : 0);
            i3 = 200;
        } else {
            int i4 = this.answerStartRow;
            if (i < i4 || i >= this.answersCount + i4) {
                return;
            }
            int i5 = i - i4;
            String[] strArr = this.answers;
            i2 = 100 - (strArr[i5] != null ? strArr[i5].length() : 0);
        }
        float f = i3;
        if (i2 <= f - (0.7f * f)) {
            pollEditTextCell.setText2(String.format("%d", Integer.valueOf(i2)));
            SimpleTextView textView2 = pollEditTextCell.getTextView2();
            String str2 = i2 < 0 ? "windowBackgroundWhiteRedText5" : "windowBackgroundWhiteGrayText3";
            textView2.setTextColor(getThemedColor(str2));
            textView2.setTag(str2);
            return;
        }
        pollEditTextCell.setText2("");
    }

    public void addNewField() {
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

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            ChatAttachAlertPollLayout.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChatAttachAlertPollLayout.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            int i2 = 3;
            boolean z = true;
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == ChatAttachAlertPollLayout.this.questionHeaderRow) {
                    headerCell.getTextView().setGravity(19);
                    headerCell.setText(LocaleController.getString("PollQuestion", 2131627583));
                    return;
                }
                TextView textView = headerCell.getTextView();
                if (LocaleController.isRTL) {
                    i2 = 5;
                }
                textView.setGravity(i2 | 16);
                if (i == ChatAttachAlertPollLayout.this.answerHeaderRow) {
                    if (ChatAttachAlertPollLayout.this.quizOnly == 1) {
                        headerCell.setText(LocaleController.getString("QuizAnswers", 2131627821));
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString("AnswerOptions", 2131624358));
                        return;
                    }
                } else if (i != ChatAttachAlertPollLayout.this.settingsHeaderRow) {
                    return;
                } else {
                    headerCell.setText(LocaleController.getString("Settings", 2131628259));
                    return;
                }
            }
            boolean z2 = false;
            if (itemViewType == 6) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i != ChatAttachAlertPollLayout.this.anonymousRow) {
                    if (i != ChatAttachAlertPollLayout.this.multipleRow) {
                        if (i == ChatAttachAlertPollLayout.this.quizRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("PollQuiz", 2131627584), ChatAttachAlertPollLayout.this.quizPoll, false);
                            if (ChatAttachAlertPollLayout.this.quizOnly != 0) {
                                z = false;
                            }
                            textCheckCell.setEnabled(z, null);
                        }
                    } else {
                        String string = LocaleController.getString("PollMultiple", 2131627582);
                        boolean z3 = ChatAttachAlertPollLayout.this.multipleChoise;
                        if (ChatAttachAlertPollLayout.this.quizRow != -1) {
                            z2 = true;
                        }
                        textCheckCell.setTextAndCheck(string, z3, z2);
                        textCheckCell.setEnabled(true, null);
                    }
                } else {
                    String string2 = LocaleController.getString("PollAnonymous", 2131627578);
                    boolean z4 = ChatAttachAlertPollLayout.this.anonymousPoll;
                    if (ChatAttachAlertPollLayout.this.multipleRow != -1 || ChatAttachAlertPollLayout.this.quizRow != -1) {
                        z2 = true;
                    }
                    textCheckCell.setTextAndCheck(string2, z4, z2);
                    textCheckCell.setEnabled(true, null);
                }
            } else if (itemViewType != 9) {
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        return;
                    }
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    textCell.setColors(null, "windowBackgroundWhiteBlueText4");
                    Drawable drawable = this.mContext.getResources().getDrawable(2131166078);
                    Drawable drawable2 = this.mContext.getResources().getDrawable(2131166079);
                    drawable.setColorFilter(new PorterDuffColorFilter(ChatAttachAlertPollLayout.this.getThemedColor("switchTrackChecked"), PorterDuff.Mode.MULTIPLY));
                    drawable2.setColorFilter(new PorterDuffColorFilter(ChatAttachAlertPollLayout.this.getThemedColor("checkboxCheck"), PorterDuff.Mode.MULTIPLY));
                    textCell.setTextAndIcon(LocaleController.getString("AddAnOption", 2131624243), (Drawable) new CombinedDrawable(drawable, drawable2), false);
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(ChatAttachAlertPollLayout.this.getThemedColor("windowBackgroundGray")), Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                combinedDrawable.setFullsize(true);
                textInfoPrivacyCell.setBackgroundDrawable(combinedDrawable);
                if (i != ChatAttachAlertPollLayout.this.solutionInfoRow) {
                    if (i == ChatAttachAlertPollLayout.this.settingsSectionRow) {
                        if (ChatAttachAlertPollLayout.this.quizOnly != 0) {
                            textInfoPrivacyCell.setText(null);
                            return;
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("QuizInfo", 2131627822));
                            return;
                        }
                    } else if (10 - ChatAttachAlertPollLayout.this.answersCount <= 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("AddAnOptionInfoMax", 2131624245));
                        return;
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.formatString("AddAnOptionInfo", 2131624244, LocaleController.formatPluralString("Option", 10 - ChatAttachAlertPollLayout.this.answersCount, new Object[0])));
                        return;
                    }
                }
                textInfoPrivacyCell.setText(LocaleController.getString("AddAnExplanationInfo", 2131624242));
                return;
            }
            viewHolder.itemView.requestLayout();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            CharSequence charSequence = "";
            if (itemViewType == 4) {
                PollEditTextCell pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                if (ChatAttachAlertPollLayout.this.questionString != null) {
                    charSequence = ChatAttachAlertPollLayout.this.questionString;
                }
                pollEditTextCell.setTextAndHint(charSequence, LocaleController.getString("QuestionHint", 2131627815), false);
                pollEditTextCell.setTag(null);
                ChatAttachAlertPollLayout.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
            } else if (itemViewType != 5) {
                if (itemViewType != 7) {
                    return;
                }
                PollEditTextCell pollEditTextCell2 = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell2.setTag(1);
                if (ChatAttachAlertPollLayout.this.solutionString != null) {
                    charSequence = ChatAttachAlertPollLayout.this.solutionString;
                }
                pollEditTextCell2.setTextAndHint(charSequence, LocaleController.getString("AddAnExplanation", 2131624241), false);
                pollEditTextCell2.setTag(null);
                ChatAttachAlertPollLayout.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
            } else {
                int adapterPosition = viewHolder.getAdapterPosition();
                PollEditTextCell pollEditTextCell3 = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell3.setTag(1);
                pollEditTextCell3.setTextAndHint(ChatAttachAlertPollLayout.this.answers[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow], LocaleController.getString("OptionHint", 2131627112), true);
                pollEditTextCell3.setTag(null);
                if (ChatAttachAlertPollLayout.this.requestFieldFocusAtPosition == adapterPosition) {
                    EditTextBoldCursor textView = pollEditTextCell3.getTextView();
                    textView.requestFocus();
                    AndroidUtilities.showKeyboard(textView);
                    ChatAttachAlertPollLayout.this.requestFieldFocusAtPosition = -1;
                }
                ChatAttachAlertPollLayout.this.setTextLeft(viewHolder.itemView, adapterPosition);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 4) {
                EditTextBoldCursor textView = ((PollEditTextCell) viewHolder.itemView).getTextView();
                if (!textView.isFocused()) {
                    return;
                }
                textView.clearFocus();
                AndroidUtilities.hideKeyboard(textView);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == ChatAttachAlertPollLayout.this.addAnswerRow || adapterPosition == ChatAttachAlertPollLayout.this.anonymousRow || adapterPosition == ChatAttachAlertPollLayout.this.multipleRow || (ChatAttachAlertPollLayout.this.quizOnly == 0 && adapterPosition == ChatAttachAlertPollLayout.this.quizRow);
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends PollEditTextCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, View.OnClickListener onClickListener) {
                super(context, onClickListener);
                ListAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass2(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.val$cell.getTag() != null) {
                    return;
                }
                ChatAttachAlertPollLayout.this.questionString = editable.toString();
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(ChatAttachAlertPollLayout.this.questionRow);
                if (findViewHolderForAdapterPosition != null) {
                    ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                    chatAttachAlertPollLayout.setTextLeft(findViewHolderForAdapterPosition.itemView, chatAttachAlertPollLayout.questionRow);
                }
                ChatAttachAlertPollLayout.this.checkDoneButton();
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 extends PollEditTextCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Context context, boolean z, View.OnClickListener onClickListener) {
                super(context, z, onClickListener);
                ListAdapter.this = r1;
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected void onActionModeStart(EditTextBoldCursor editTextBoldCursor, ActionMode actionMode) {
                if (!editTextBoldCursor.isFocused() || !editTextBoldCursor.hasSelection()) {
                    return;
                }
                Menu menu = actionMode.getMenu();
                if (menu.findItem(16908321) == null) {
                    return;
                }
                ((ChatActivity) ChatAttachAlertPollLayout.this.parentAlert.baseFragment).fillActionModeMenu(menu);
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$4 */
        /* loaded from: classes3.dex */
        class AnonymousClass4 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass4(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.val$cell.getTag() != null) {
                    return;
                }
                ChatAttachAlertPollLayout.this.solutionString = editable;
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(ChatAttachAlertPollLayout.this.solutionRow);
                if (findViewHolderForAdapterPosition != null) {
                    ChatAttachAlertPollLayout chatAttachAlertPollLayout = ChatAttachAlertPollLayout.this;
                    chatAttachAlertPollLayout.setTextLeft(findViewHolderForAdapterPosition.itemView, chatAttachAlertPollLayout.solutionRow);
                }
                ChatAttachAlertPollLayout.this.checkDoneButton();
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$5 */
        /* loaded from: classes3.dex */
        class AnonymousClass5 extends View {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass5(Context context) {
                super(context);
                ListAdapter.this = r1;
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                setMeasuredDimension(View.MeasureSpec.getSize(i), ChatAttachAlertPollLayout.this.topPadding);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            PollEditTextCell pollEditTextCell;
            PollEditTextCell pollEditTextCell2;
            switch (i) {
                case 0:
                    pollEditTextCell = new HeaderCell(this.mContext, "windowBackgroundWhiteBlueHeader", 21, 15, false);
                    break;
                case 1:
                    View shadowSectionCell = new ShadowSectionCell(this.mContext);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(ChatAttachAlertPollLayout.this.getThemedColor("windowBackgroundGray")), Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    combinedDrawable.setFullsize(true);
                    shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                    pollEditTextCell = shadowSectionCell;
                    break;
                case 2:
                    pollEditTextCell = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 3:
                    pollEditTextCell = new TextCell(this.mContext);
                    break;
                case 4:
                    pollEditTextCell2 = new AnonymousClass1(this.mContext, null);
                    pollEditTextCell2.createErrorTextView();
                    pollEditTextCell2.addTextWatcher(new AnonymousClass2(pollEditTextCell2));
                    pollEditTextCell = pollEditTextCell2;
                    break;
                case 5:
                default:
                    AnonymousClass6 anonymousClass6 = new AnonymousClass6(this.mContext, new ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda0(this));
                    anonymousClass6.addTextWatcher(new AnonymousClass7(anonymousClass6));
                    anonymousClass6.setShowNextButton(true);
                    EditTextBoldCursor textView = anonymousClass6.getTextView();
                    textView.setImeOptions(textView.getImeOptions() | 5);
                    textView.setOnEditorActionListener(new ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda2(this, anonymousClass6));
                    textView.setOnKeyListener(new ChatAttachAlertPollLayout$ListAdapter$$ExternalSyntheticLambda1(anonymousClass6));
                    pollEditTextCell = anonymousClass6;
                    break;
                case 6:
                    pollEditTextCell = new TextCheckCell(this.mContext);
                    break;
                case 7:
                    pollEditTextCell2 = new AnonymousClass3(this.mContext, true, null);
                    pollEditTextCell2.createErrorTextView();
                    pollEditTextCell2.addTextWatcher(new AnonymousClass4(pollEditTextCell2));
                    pollEditTextCell = pollEditTextCell2;
                    break;
                case 8:
                    EmptyView emptyView = new EmptyView(this.mContext);
                    emptyView.setBackgroundColor(ChatAttachAlertPollLayout.this.getThemedColor("windowBackgroundGray"));
                    pollEditTextCell = emptyView;
                    break;
                case 9:
                    pollEditTextCell = new AnonymousClass5(this.mContext);
                    break;
            }
            pollEditTextCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(pollEditTextCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            int adapterPosition;
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
            ChatAttachAlertPollLayout.access$1210(ChatAttachAlertPollLayout.this);
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
                    ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.answerSectionRow);
                    ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.emptyRow);
                }
            }
            if (textView.isFocused()) {
                AndroidUtilities.hideKeyboard(textView);
            }
            textView.clearFocus();
            ChatAttachAlertPollLayout.this.checkDoneButton();
            ChatAttachAlertPollLayout.this.updateRows();
            ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.answerSectionRow);
            ChatAttachAlertPollLayout.this.listAdapter.notifyItemChanged(ChatAttachAlertPollLayout.this.emptyRow);
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$6 */
        /* loaded from: classes3.dex */
        class AnonymousClass6 extends PollEditTextCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass6(Context context, View.OnClickListener onClickListener) {
                super(context, onClickListener);
                ListAdapter.this = r1;
            }

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
            protected boolean shouldShowCheckBox() {
                return ChatAttachAlertPollLayout.this.quizPoll;
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                ChatAttachAlertPollLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            public void onCheckBoxClick(PollEditTextCell pollEditTextCell, boolean z) {
                int adapterPosition;
                if (z && ChatAttachAlertPollLayout.this.quizPoll) {
                    Arrays.fill(ChatAttachAlertPollLayout.this.answersChecks, false);
                    ChatAttachAlertPollLayout.this.listView.getChildCount();
                    for (int i = ChatAttachAlertPollLayout.this.answerStartRow; i < ChatAttachAlertPollLayout.this.answerStartRow + ChatAttachAlertPollLayout.this.answersCount; i++) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(i);
                        if (findViewHolderForAdapterPosition != null) {
                            View view = findViewHolderForAdapterPosition.itemView;
                            if (view instanceof PollEditTextCell) {
                                ((PollEditTextCell) view).setChecked(false, true);
                            }
                        }
                    }
                }
                super.onCheckBoxClick(pollEditTextCell, z);
                RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                    ChatAttachAlertPollLayout.this.answersChecks[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow] = z;
                }
                ChatAttachAlertPollLayout.this.checkDoneButton();
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected boolean isChecked(PollEditTextCell pollEditTextCell) {
                int adapterPosition;
                RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                    return false;
                }
                return ChatAttachAlertPollLayout.this.answersChecks[adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow];
            }
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertPollLayout$ListAdapter$7 */
        /* loaded from: classes3.dex */
        class AnonymousClass7 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass7(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int adapterPosition;
                int adapterPosition2;
                RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(this.val$cell);
                if (findContainingViewHolder == null || (adapterPosition2 = (adapterPosition = findContainingViewHolder.getAdapterPosition()) - ChatAttachAlertPollLayout.this.answerStartRow) < 0 || adapterPosition2 >= ChatAttachAlertPollLayout.this.answers.length) {
                    return;
                }
                ChatAttachAlertPollLayout.this.answers[adapterPosition2] = editable.toString();
                ChatAttachAlertPollLayout.this.setTextLeft(this.val$cell, adapterPosition);
                ChatAttachAlertPollLayout.this.checkDoneButton();
            }
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$1(PollEditTextCell pollEditTextCell, TextView textView, int i, KeyEvent keyEvent) {
            int adapterPosition;
            if (i == 5) {
                RecyclerView.ViewHolder findContainingViewHolder = ChatAttachAlertPollLayout.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                    int i2 = adapterPosition - ChatAttachAlertPollLayout.this.answerStartRow;
                    if (i2 != ChatAttachAlertPollLayout.this.answersCount - 1 || ChatAttachAlertPollLayout.this.answersCount >= 10) {
                        if (i2 != ChatAttachAlertPollLayout.this.answersCount - 1) {
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatAttachAlertPollLayout.this.listView.findViewHolderForAdapterPosition(adapterPosition + 1);
                            if (findViewHolderForAdapterPosition != null) {
                                View view = findViewHolderForAdapterPosition.itemView;
                                if (view instanceof PollEditTextCell) {
                                    ((PollEditTextCell) view).getTextView().requestFocus();
                                }
                            }
                        } else {
                            AndroidUtilities.hideKeyboard(pollEditTextCell.getTextView());
                        }
                    } else {
                        ChatAttachAlertPollLayout.this.addNewField();
                    }
                }
                return true;
            }
            return false;
        }

        public static /* synthetic */ boolean lambda$onCreateViewHolder$2(PollEditTextCell pollEditTextCell, View view, int i, KeyEvent keyEvent) {
            EditTextBoldCursor editTextBoldCursor = (EditTextBoldCursor) view;
            if (i == 67 && keyEvent.getAction() == 0 && editTextBoldCursor.length() == 0) {
                pollEditTextCell.callOnDelete();
                return true;
            }
            return false;
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

        public void swapElements(int i, int i2) {
            int i3 = i - ChatAttachAlertPollLayout.this.answerStartRow;
            int i4 = i2 - ChatAttachAlertPollLayout.this.answerStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= ChatAttachAlertPollLayout.this.answersCount || i4 >= ChatAttachAlertPollLayout.this.answersCount) {
                return;
            }
            String str = ChatAttachAlertPollLayout.this.answers[i3];
            ChatAttachAlertPollLayout.this.answers[i3] = ChatAttachAlertPollLayout.this.answers[i4];
            ChatAttachAlertPollLayout.this.answers[i4] = str;
            boolean z = ChatAttachAlertPollLayout.this.answersChecks[i3];
            ChatAttachAlertPollLayout.this.answersChecks[i3] = ChatAttachAlertPollLayout.this.answersChecks[i4];
            ChatAttachAlertPollLayout.this.answersChecks[i4] = z;
            notifyItemMoved(i, i2);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "dialogScrollGlow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{EmptyView.class}, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"moveImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menuSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{PollEditTextCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{PollEditTextCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{PollEditTextCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
        return arrayList;
    }
}
