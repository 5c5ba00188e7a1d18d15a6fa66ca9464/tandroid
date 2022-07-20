package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
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
import org.telegram.ui.Components.ChatAttachAlertPollLayout;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class PollCreateActivity extends BaseFragment {
    private int addAnswerRow;
    private int anonymousRow;
    private int answerHeaderRow;
    private int answerSectionRow;
    private int answerStartRow;
    private PollCreateActivityDelegate delegate;
    private ActionBarMenuItem doneItem;
    private boolean hintShowed;
    private HintView hintView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean multipleChoise;
    private int multipleRow;
    private ChatActivity parentFragment;
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
    private String[] answers = new String[10];
    private boolean[] answersChecks = new boolean[10];
    private int answersCount = 1;
    private boolean anonymousPoll = true;
    private int requestFieldFocusAtPosition = -1;

    /* loaded from: classes3.dex */
    public interface PollCreateActivityDelegate {
        void sendPoll(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, HashMap<String, String> hashMap, boolean z, int i);
    }

    static /* synthetic */ int access$2210(PollCreateActivity pollCreateActivity) {
        int i = pollCreateActivity.answersCount;
        pollCreateActivity.answersCount = i - 1;
        return i;
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
            PollCreateActivity.this = r1;
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
            PollCreateActivity.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
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
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    public PollCreateActivity(ChatActivity chatActivity, Boolean bool) {
        int i = 1;
        this.parentFragment = chatActivity;
        if (bool != null) {
            boolean booleanValue = bool.booleanValue();
            this.quizPoll = booleanValue;
            this.quizOnly = !booleanValue ? 2 : i;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        if (this.quizOnly == 1) {
            this.actionBar.setTitle(LocaleController.getString("NewQuiz", 2131626844));
        } else {
            this.actionBar.setTitle(LocaleController.getString("NewPoll", 2131626843));
        }
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.doneItem = this.actionBar.createMenu().addItem(1, LocaleController.getString("Create", 2131625284).toUpperCase());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.listView = anonymousClass2;
        anonymousClass2.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new PollCreateActivity$$ExternalSyntheticLambda1(this));
        this.listView.setOnScrollListener(new AnonymousClass3());
        HintView hintView = new HintView(context, 4);
        this.hintView = hintView;
        hintView.setText(LocaleController.getString("PollTapToSelect", 2131627640));
        this.hintView.setAlpha(0.0f);
        this.hintView.setVisibility(4);
        frameLayout2.addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        checkDoneButton();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PollCreateActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            PollCreateActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!PollCreateActivity.this.checkDiscard()) {
                    return;
                }
                PollCreateActivity.this.finishFragment();
            } else if (i == 1) {
                if (PollCreateActivity.this.quizPoll && PollCreateActivity.this.doneItem.getAlpha() != 1.0f) {
                    int i2 = 0;
                    for (int i3 = 0; i3 < PollCreateActivity.this.answersChecks.length; i3++) {
                        if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i3])) && PollCreateActivity.this.answersChecks[i3]) {
                            i2++;
                        }
                    }
                    if (i2 > 0) {
                        return;
                    }
                    PollCreateActivity.this.showQuizHint();
                    return;
                }
                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = new TLRPC$TL_messageMediaPoll();
                TLRPC$TL_poll tLRPC$TL_poll = new TLRPC$TL_poll();
                tLRPC$TL_messageMediaPoll.poll = tLRPC$TL_poll;
                tLRPC$TL_poll.multiple_choice = PollCreateActivity.this.multipleChoise;
                tLRPC$TL_messageMediaPoll.poll.quiz = PollCreateActivity.this.quizPoll;
                tLRPC$TL_messageMediaPoll.poll.public_voters = !PollCreateActivity.this.anonymousPoll;
                tLRPC$TL_messageMediaPoll.poll.question = ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.questionString).toString();
                SerializedData serializedData = new SerializedData(10);
                for (int i4 = 0; i4 < PollCreateActivity.this.answers.length; i4++) {
                    if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i4]))) {
                        TLRPC$TL_pollAnswer tLRPC$TL_pollAnswer = new TLRPC$TL_pollAnswer();
                        tLRPC$TL_pollAnswer.text = ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.answers[i4]).toString();
                        tLRPC$TL_pollAnswer.option = r5;
                        byte[] bArr = {(byte) (tLRPC$TL_messageMediaPoll.poll.answers.size() + 48)};
                        tLRPC$TL_messageMediaPoll.poll.answers.add(tLRPC$TL_pollAnswer);
                        if ((PollCreateActivity.this.multipleChoise || PollCreateActivity.this.quizPoll) && PollCreateActivity.this.answersChecks[i4]) {
                            serializedData.writeByte(tLRPC$TL_pollAnswer.option[0]);
                        }
                    }
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("answers", Utilities.bytesToHex(serializedData.toByteArray()));
                tLRPC$TL_messageMediaPoll.results = new TLRPC$TL_pollResults();
                CharSequence fixedString = ChatAttachAlertPollLayout.getFixedString(PollCreateActivity.this.solutionString);
                if (fixedString != null) {
                    tLRPC$TL_messageMediaPoll.results.solution = fixedString.toString();
                    ArrayList<TLRPC$MessageEntity> entities = PollCreateActivity.this.getMediaDataController().getEntities(new CharSequence[]{fixedString}, true);
                    if (entities != null && !entities.isEmpty()) {
                        tLRPC$TL_messageMediaPoll.results.solution_entities = entities;
                    }
                    if (!TextUtils.isEmpty(tLRPC$TL_messageMediaPoll.results.solution)) {
                        tLRPC$TL_messageMediaPoll.results.flags |= 16;
                    }
                }
                if (PollCreateActivity.this.parentFragment.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(PollCreateActivity.this.getParentActivity(), PollCreateActivity.this.parentFragment.getDialogId(), new PollCreateActivity$1$$ExternalSyntheticLambda0(this, tLRPC$TL_messageMediaPoll, hashMap));
                    return;
                }
                PollCreateActivity.this.delegate.sendPoll(tLRPC$TL_messageMediaPoll, hashMap, true, 0);
                PollCreateActivity.this.finishFragment();
            }
        }

        public /* synthetic */ void lambda$onItemClick$0(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
            PollCreateActivity.this.delegate.sendPoll(tLRPC$TL_messageMediaPoll, hashMap, z, i);
            PollCreateActivity.this.finishFragment();
        }
    }

    /* renamed from: org.telegram.ui.PollCreateActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerListView {
        AnonymousClass2(PollCreateActivity pollCreateActivity, Context context) {
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
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            rect.bottom += AndroidUtilities.dp(60.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    public /* synthetic */ void lambda$createView$0(View view, int i) {
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
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.quizRow);
                    if (findViewHolderForAdapterPosition != null) {
                        ((TextCheckCell) findViewHolderForAdapterPosition.itemView).setChecked(false);
                    } else {
                        this.listAdapter.notifyItemChanged(this.quizRow);
                    }
                    this.listAdapter.notifyItemRangeRemoved(i2, 2);
                }
            } else if (this.quizOnly != 0) {
                return;
            } else {
                z = !z2;
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

    /* renamed from: org.telegram.ui.PollCreateActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends RecyclerView.OnScrollListener {
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        }

        AnonymousClass3() {
            PollCreateActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (i2 == 0 || PollCreateActivity.this.hintView == null) {
                return;
            }
            PollCreateActivity.this.hintView.hide();
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

    /* JADX WARN: Removed duplicated region for block: B:46:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkDoneButton() {
        int i;
        boolean z;
        boolean z2 = false;
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
                String[] strArr = this.answers;
                if (i3 >= strArr.length) {
                    break;
                }
                if (!TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(strArr[i3]))) {
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
                ActionBarMenuItem actionBarMenuItem = this.doneItem;
                if ((this.quizPoll && i == 0) || z) {
                    z2 = true;
                }
                actionBarMenuItem.setEnabled(z2);
                this.doneItem.setAlpha(z ? 1.0f : 0.5f);
            }
        }
        z = false;
        ActionBarMenuItem actionBarMenuItem2 = this.doneItem;
        if (this.quizPoll) {
            z2 = true;
            actionBarMenuItem2.setEnabled(z2);
            this.doneItem.setAlpha(z ? 1.0f : 0.5f);
        }
        z2 = true;
        actionBarMenuItem2.setEnabled(z2);
        this.doneItem.setAlpha(z ? 1.0f : 0.5f);
    }

    public void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.questionHeaderRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.questionRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.questionSectionRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.answerHeaderRow = i3;
        int i5 = this.answersCount;
        if (i5 != 0) {
            this.answerStartRow = i4;
            this.rowCount = i4 + i5;
        } else {
            this.answerStartRow = -1;
        }
        if (i5 != this.answers.length) {
            int i6 = this.rowCount;
            this.rowCount = i6 + 1;
            this.addAnswerRow = i6;
        } else {
            this.addAnswerRow = -1;
        }
        int i7 = this.rowCount;
        int i8 = i7 + 1;
        this.rowCount = i8;
        this.answerSectionRow = i7;
        this.rowCount = i8 + 1;
        this.settingsHeaderRow = i8;
        TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
        if (!ChatObject.isChannel(currentChat) || currentChat.megagroup) {
            int i9 = this.rowCount;
            this.rowCount = i9 + 1;
            this.anonymousRow = i9;
        } else {
            this.anonymousRow = -1;
        }
        int i10 = this.quizOnly;
        if (i10 != 1) {
            int i11 = this.rowCount;
            this.rowCount = i11 + 1;
            this.multipleRow = i11;
        } else {
            this.multipleRow = -1;
        }
        if (i10 == 0) {
            int i12 = this.rowCount;
            this.rowCount = i12 + 1;
            this.quizRow = i12;
        } else {
            this.quizRow = -1;
        }
        int i13 = this.rowCount;
        int i14 = i13 + 1;
        this.rowCount = i14;
        this.settingsSectionRow = i13;
        if (this.quizPoll) {
            int i15 = i14 + 1;
            this.rowCount = i15;
            this.solutionRow = i14;
            this.rowCount = i15 + 1;
            this.solutionInfoRow = i15;
            return;
        }
        this.solutionRow = -1;
        this.solutionInfoRow = -1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return checkDiscard();
    }

    public boolean checkDiscard() {
        boolean isEmpty = TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.questionString));
        if (isEmpty) {
            for (int i = 0; i < this.answersCount && (isEmpty = TextUtils.isEmpty(ChatAttachAlertPollLayout.getFixedString(this.answers[i]))); i++) {
            }
        }
        if (!isEmpty) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("CancelPollAlertTitle", 2131624850));
            builder.setMessage(LocaleController.getString("CancelPollAlertText", 2131624849));
            builder.setPositiveButton(LocaleController.getString("PassportDiscard", 2131627272), new PollCreateActivity$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            showDialog(builder.create());
        }
        return isEmpty;
    }

    public /* synthetic */ void lambda$checkDiscard$1(DialogInterface dialogInterface, int i) {
        finishFragment();
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
            textView2.setTextColor(Theme.getColor(str2));
            textView2.setTag(str2);
            return;
        }
        pollEditTextCell.setText2("");
    }

    public void addNewField() {
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

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            PollCreateActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PollCreateActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != PollCreateActivity.this.questionHeaderRow) {
                    if (i == PollCreateActivity.this.answerHeaderRow) {
                        if (PollCreateActivity.this.quizOnly == 1) {
                            headerCell.setText(LocaleController.getString("QuizAnswers", 2131627883));
                            return;
                        } else {
                            headerCell.setText(LocaleController.getString("AnswerOptions", 2131624367));
                            return;
                        }
                    } else if (i != PollCreateActivity.this.settingsHeaderRow) {
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString("Settings", 2131628321));
                        return;
                    }
                }
                headerCell.setText(LocaleController.getString("PollQuestion", 2131627635));
                return;
            }
            boolean z2 = false;
            if (itemViewType == 6) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i != PollCreateActivity.this.anonymousRow) {
                    if (i != PollCreateActivity.this.multipleRow) {
                        if (i != PollCreateActivity.this.quizRow) {
                            return;
                        }
                        textCheckCell.setTextAndCheck(LocaleController.getString("PollQuiz", 2131627636), PollCreateActivity.this.quizPoll, false);
                        if (PollCreateActivity.this.quizOnly != 0) {
                            z = false;
                        }
                        textCheckCell.setEnabled(z, null);
                        return;
                    }
                    String string = LocaleController.getString("PollMultiple", 2131627634);
                    boolean z3 = PollCreateActivity.this.multipleChoise;
                    if (PollCreateActivity.this.quizRow != -1) {
                        z2 = true;
                    }
                    textCheckCell.setTextAndCheck(string, z3, z2);
                    textCheckCell.setEnabled(true, null);
                    return;
                }
                String string2 = LocaleController.getString("PollAnonymous", 2131627630);
                boolean z4 = PollCreateActivity.this.anonymousPoll;
                if (PollCreateActivity.this.multipleRow != -1 || PollCreateActivity.this.quizRow != -1) {
                    z2 = true;
                }
                textCheckCell.setTextAndCheck(string2, z4, z2);
                textCheckCell.setEnabled(true, null);
            } else if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(null, "windowBackgroundWhiteBlueText4");
                Drawable drawable = this.mContext.getResources().getDrawable(2131166080);
                Drawable drawable2 = this.mContext.getResources().getDrawable(2131166081);
                drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("switchTrackChecked"), PorterDuff.Mode.MULTIPLY));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("checkboxCheck"), PorterDuff.Mode.MULTIPLY));
                textCell.setTextAndIcon(LocaleController.getString("AddAnOption", 2131624248), (Drawable) new CombinedDrawable(drawable, drawable2), false);
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setFixedSize(0);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                if (i != PollCreateActivity.this.solutionInfoRow) {
                    if (i == PollCreateActivity.this.settingsSectionRow) {
                        if (PollCreateActivity.this.quizOnly != 0) {
                            textInfoPrivacyCell.setFixedSize(12);
                            textInfoPrivacyCell.setText(null);
                            return;
                        }
                        textInfoPrivacyCell.setText(LocaleController.getString("QuizInfo", 2131627884));
                        return;
                    } else if (10 - PollCreateActivity.this.answersCount <= 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("AddAnOptionInfoMax", 2131624250));
                        return;
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.formatString("AddAnOptionInfo", 2131624249, LocaleController.formatPluralString("Option", 10 - PollCreateActivity.this.answersCount, new Object[0])));
                        return;
                    }
                }
                textInfoPrivacyCell.setText(LocaleController.getString("AddAnExplanationInfo", 2131624247));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            CharSequence charSequence = "";
            if (itemViewType == 4) {
                PollEditTextCell pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                if (PollCreateActivity.this.questionString != null) {
                    charSequence = PollCreateActivity.this.questionString;
                }
                pollEditTextCell.setTextAndHint(charSequence, LocaleController.getString("QuestionHint", 2131627877), false);
                pollEditTextCell.setTag(null);
                PollCreateActivity.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
            } else if (itemViewType != 5) {
                if (itemViewType != 7) {
                    return;
                }
                PollEditTextCell pollEditTextCell2 = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell2.setTag(1);
                if (PollCreateActivity.this.solutionString != null) {
                    charSequence = PollCreateActivity.this.solutionString;
                }
                pollEditTextCell2.setTextAndHint(charSequence, LocaleController.getString("AddAnExplanation", 2131624246), false);
                pollEditTextCell2.setTag(null);
                PollCreateActivity.this.setTextLeft(viewHolder.itemView, viewHolder.getAdapterPosition());
            } else {
                int adapterPosition = viewHolder.getAdapterPosition();
                PollEditTextCell pollEditTextCell3 = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell3.setTag(1);
                pollEditTextCell3.setTextAndHint(PollCreateActivity.this.answers[adapterPosition - PollCreateActivity.this.answerStartRow], LocaleController.getString("OptionHint", 2131627164), true);
                pollEditTextCell3.setTag(null);
                if (PollCreateActivity.this.requestFieldFocusAtPosition == adapterPosition) {
                    EditTextBoldCursor textView = pollEditTextCell3.getTextView();
                    textView.requestFocus();
                    AndroidUtilities.showKeyboard(textView);
                    PollCreateActivity.this.requestFieldFocusAtPosition = -1;
                }
                PollCreateActivity.this.setTextLeft(viewHolder.itemView, adapterPosition);
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
            return adapterPosition == PollCreateActivity.this.addAnswerRow || adapterPosition == PollCreateActivity.this.anonymousRow || adapterPosition == PollCreateActivity.this.multipleRow || (PollCreateActivity.this.quizOnly == 0 && adapterPosition == PollCreateActivity.this.quizRow);
        }

        /* renamed from: org.telegram.ui.PollCreateActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass1(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.val$cell.getTag() != null) {
                    return;
                }
                PollCreateActivity.this.questionString = editable.toString();
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.questionRow);
                if (findViewHolderForAdapterPosition != null) {
                    PollCreateActivity pollCreateActivity = PollCreateActivity.this;
                    pollCreateActivity.setTextLeft(findViewHolderForAdapterPosition.itemView, pollCreateActivity.questionRow);
                }
                PollCreateActivity.this.checkDoneButton();
            }
        }

        /* renamed from: org.telegram.ui.PollCreateActivity$ListAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends PollEditTextCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, boolean z, View.OnClickListener onClickListener) {
                super(context, z, onClickListener);
                ListAdapter.this = r1;
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
                PollCreateActivity.this.parentFragment.fillActionModeMenu(menu);
            }
        }

        /* renamed from: org.telegram.ui.PollCreateActivity$ListAdapter$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass3(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.val$cell.getTag() != null) {
                    return;
                }
                PollCreateActivity.this.solutionString = editable;
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.solutionRow);
                if (findViewHolderForAdapterPosition != null) {
                    PollCreateActivity pollCreateActivity = PollCreateActivity.this;
                    pollCreateActivity.setTextLeft(findViewHolderForAdapterPosition.itemView, pollCreateActivity.solutionRow);
                }
                PollCreateActivity.this.checkDoneButton();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            AnonymousClass4 anonymousClass4;
            if (i == 0) {
                HeaderCell headerCell = new HeaderCell(this.mContext, "windowBackgroundWhiteBlueHeader", 21, 15, false);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass4 = headerCell;
            } else if (i == 1) {
                anonymousClass4 = new ShadowSectionCell(this.mContext);
            } else if (i == 2) {
                anonymousClass4 = new TextInfoPrivacyCell(this.mContext);
            } else if (i == 3) {
                TextCell textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass4 = textCell;
            } else if (i == 4) {
                PollEditTextCell pollEditTextCell = new PollEditTextCell(this.mContext, null);
                pollEditTextCell.createErrorTextView();
                pollEditTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                pollEditTextCell.addTextWatcher(new AnonymousClass1(pollEditTextCell));
                anonymousClass4 = pollEditTextCell;
            } else if (i == 6) {
                TextCheckCell textCheckCell = new TextCheckCell(this.mContext);
                textCheckCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass4 = textCheckCell;
            } else if (i == 7) {
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.mContext, true, null);
                anonymousClass2.createErrorTextView();
                anonymousClass2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass2.addTextWatcher(new AnonymousClass3(anonymousClass2));
                anonymousClass4 = anonymousClass2;
            } else {
                AnonymousClass4 anonymousClass42 = new AnonymousClass4(this.mContext, new PollCreateActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                anonymousClass42.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass42.addTextWatcher(new AnonymousClass5(anonymousClass42));
                anonymousClass42.setShowNextButton(true);
                EditTextBoldCursor textView = anonymousClass42.getTextView();
                textView.setImeOptions(textView.getImeOptions() | 5);
                textView.setOnEditorActionListener(new PollCreateActivity$ListAdapter$$ExternalSyntheticLambda2(this, anonymousClass42));
                textView.setOnKeyListener(new PollCreateActivity$ListAdapter$$ExternalSyntheticLambda1(anonymousClass42));
                anonymousClass4 = anonymousClass42;
            }
            anonymousClass4.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(anonymousClass4);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            int adapterPosition;
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
            PollCreateActivity.access$2210(PollCreateActivity.this);
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
                    PollCreateActivity.this.listAdapter.notifyItemChanged(PollCreateActivity.this.answerSectionRow);
                }
            }
            if (textView.isFocused()) {
                AndroidUtilities.hideKeyboard(textView);
            }
            textView.clearFocus();
            PollCreateActivity.this.checkDoneButton();
            PollCreateActivity.this.updateRows();
            PollCreateActivity.this.listAdapter.notifyItemChanged(PollCreateActivity.this.answerSectionRow);
        }

        /* renamed from: org.telegram.ui.PollCreateActivity$ListAdapter$4 */
        /* loaded from: classes3.dex */
        class AnonymousClass4 extends PollEditTextCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass4(Context context, View.OnClickListener onClickListener) {
                super(context, onClickListener);
                ListAdapter.this = r1;
            }

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
            protected boolean shouldShowCheckBox() {
                return PollCreateActivity.this.quizPoll;
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            public void onCheckBoxClick(PollEditTextCell pollEditTextCell, boolean z) {
                int adapterPosition;
                if (z && PollCreateActivity.this.quizPoll) {
                    Arrays.fill(PollCreateActivity.this.answersChecks, false);
                    PollCreateActivity.this.listView.getChildCount();
                    for (int i = PollCreateActivity.this.answerStartRow; i < PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answersCount; i++) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(i);
                        if (findViewHolderForAdapterPosition != null) {
                            View view = findViewHolderForAdapterPosition.itemView;
                            if (view instanceof PollEditTextCell) {
                                ((PollEditTextCell) view).setChecked(false, true);
                            }
                        }
                    }
                }
                super.onCheckBoxClick(pollEditTextCell, z);
                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                    PollCreateActivity.this.answersChecks[adapterPosition - PollCreateActivity.this.answerStartRow] = z;
                }
                PollCreateActivity.this.checkDoneButton();
            }

            @Override // org.telegram.ui.Cells.PollEditTextCell
            protected boolean isChecked(PollEditTextCell pollEditTextCell) {
                int adapterPosition;
                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition()) == -1) {
                    return false;
                }
                return PollCreateActivity.this.answersChecks[adapterPosition - PollCreateActivity.this.answerStartRow];
            }
        }

        /* renamed from: org.telegram.ui.PollCreateActivity$ListAdapter$5 */
        /* loaded from: classes3.dex */
        class AnonymousClass5 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass5(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int adapterPosition;
                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(this.val$cell);
                if (findContainingViewHolder == null || (adapterPosition = findContainingViewHolder.getAdapterPosition() - PollCreateActivity.this.answerStartRow) < 0 || adapterPosition >= PollCreateActivity.this.answers.length) {
                    return;
                }
                PollCreateActivity.this.answers[adapterPosition] = editable.toString();
                PollCreateActivity.this.setTextLeft(this.val$cell, adapterPosition);
                PollCreateActivity.this.checkDoneButton();
            }
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$1(PollEditTextCell pollEditTextCell, TextView textView, int i, KeyEvent keyEvent) {
            int adapterPosition;
            if (i == 5) {
                RecyclerView.ViewHolder findContainingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder(pollEditTextCell);
                if (findContainingViewHolder != null && (adapterPosition = findContainingViewHolder.getAdapterPosition()) != -1) {
                    int i2 = adapterPosition - PollCreateActivity.this.answerStartRow;
                    if (i2 != PollCreateActivity.this.answersCount - 1 || PollCreateActivity.this.answersCount >= 10) {
                        if (i2 != PollCreateActivity.this.answersCount - 1) {
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(adapterPosition + 1);
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
                        PollCreateActivity.this.addNewField();
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

        public void swapElements(int i, int i2) {
            int i3 = i - PollCreateActivity.this.answerStartRow;
            int i4 = i2 - PollCreateActivity.this.answerStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= PollCreateActivity.this.answersCount || i4 >= PollCreateActivity.this.answersCount) {
                return;
            }
            String str = PollCreateActivity.this.answers[i3];
            PollCreateActivity.this.answers[i3] = PollCreateActivity.this.answers[i4];
            PollCreateActivity.this.answers[i4] = str;
            boolean z = PollCreateActivity.this.answersChecks[i3];
            PollCreateActivity.this.answersChecks[i3] = PollCreateActivity.this.answersChecks[i4];
            PollCreateActivity.this.answersChecks[i4] = z;
            notifyItemMoved(i, i2);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCell.class, PollEditTextCell.class, TextCheckCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
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
