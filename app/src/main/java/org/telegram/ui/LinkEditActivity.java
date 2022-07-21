package org.telegram.ui;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_editExportedChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_exportChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_exportedChatInvite;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SlideChooseView;
/* loaded from: classes3.dex */
public class LinkEditActivity extends BaseFragment {
    private TextCheckCell approveCell;
    private TextView buttonTextView;
    private Callback callback;
    private final long chatId;
    private TextView createTextView;
    int currentInviteDate;
    private TextInfoPrivacyCell divider;
    private TextInfoPrivacyCell dividerName;
    private TextInfoPrivacyCell dividerUses;
    private boolean finished;
    private boolean ignoreSet;
    TLRPC$TL_chatInviteExported inviteToEdit;
    boolean loading;
    private EditText nameEditText;
    AlertDialog progressDialog;
    private TextSettingsCell revokeLink;
    boolean scrollToEnd;
    boolean scrollToStart;
    private ScrollView scrollView;
    private SlideChooseView timeChooseView;
    private TextView timeEditText;
    private HeaderCell timeHeaderCell;
    private int type;
    private SlideChooseView usesChooseView;
    private EditText usesEditText;
    private HeaderCell usesHeaderCell;
    private boolean firstLayout = true;
    private ArrayList<Integer> dispalyedDates = new ArrayList<>();
    private final int[] defaultDates = {3600, 86400, 604800};
    private ArrayList<Integer> dispalyedUses = new ArrayList<>();
    private final int[] defaultUses = {1, 10, 100};

    /* loaded from: classes3.dex */
    public interface Callback {
        void onLinkCreated(TLObject tLObject);

        void onLinkEdited(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported, TLObject tLObject);

        void onLinkRemoved(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);

        void revokeLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);
    }

    public LinkEditActivity(int i, long j) {
        this.type = i;
        this.chatId = j;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        int i = this.type;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("NewLink", 2131626833));
        } else if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("EditLink", 2131625592));
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        TextView textView = new TextView(context);
        this.createTextView = textView;
        textView.setEllipsize(TextUtils.TruncateAt.END);
        this.createTextView.setGravity(16);
        this.createTextView.setOnClickListener(new LinkEditActivity$$ExternalSyntheticLambda2(this));
        this.createTextView.setSingleLine();
        int i2 = this.type;
        if (i2 == 0) {
            this.createTextView.setText(LocaleController.getString("CreateLinkHeader", 2131625289));
        } else if (i2 == 1) {
            this.createTextView.setText(LocaleController.getString("SaveLinkHeader", 2131628128));
        }
        this.createTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
        this.createTextView.setTextSize(1, 14.0f);
        this.createTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.createTextView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f));
        this.actionBar.addView(this.createTextView, LayoutHelper.createFrame(-2, -2.0f, 8388629, 0.0f, this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight / AndroidUtilities.dp(2.0f) : 0, 0.0f, 0.0f));
        this.scrollView = new ScrollView(context);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.fragmentView = anonymousClass2;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(100L);
        anonymousClass3.setLayoutTransition(layoutTransition);
        anonymousClass3.setOrientation(1);
        this.scrollView.addView(anonymousClass3);
        TextView textView2 = new TextView(context);
        this.buttonTextView = textView2;
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextSize(1, 14.0f);
        this.buttonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        int i3 = this.type;
        if (i3 == 0) {
            this.buttonTextView.setText(LocaleController.getString("CreateLink", 2131625288));
        } else if (i3 == 1) {
            this.buttonTextView.setText(LocaleController.getString("SaveLink", 2131628127));
        }
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(this, context);
        this.approveCell = anonymousClass4;
        anonymousClass4.setBackgroundColor(Theme.getColor("windowBackgroundUnchecked"));
        this.approveCell.setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
        this.approveCell.setDrawCheckRipple(true);
        this.approveCell.setHeight(56);
        this.approveCell.setTag("windowBackgroundUnchecked");
        this.approveCell.setTextAndCheck(LocaleController.getString("ApproveNewMembers", 2131624398), false, false);
        this.approveCell.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.approveCell.setOnClickListener(new LinkEditActivity$$ExternalSyntheticLambda1(this));
        anonymousClass3.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
        textInfoPrivacyCell.setText(LocaleController.getString("ApproveNewMembersDescription", 2131624399));
        anonymousClass3.addView(textInfoPrivacyCell);
        HeaderCell headerCell = new HeaderCell(context);
        this.timeHeaderCell = headerCell;
        headerCell.setText(LocaleController.getString("LimitByPeriod", 2131626445));
        anonymousClass3.addView(this.timeHeaderCell);
        SlideChooseView slideChooseView = new SlideChooseView(context);
        this.timeChooseView = slideChooseView;
        anonymousClass3.addView(slideChooseView);
        TextView textView3 = new TextView(context);
        this.timeEditText = textView3;
        textView3.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.timeEditText.setGravity(16);
        this.timeEditText.setTextSize(1, 16.0f);
        this.timeEditText.setHint(LocaleController.getString("TimeLimitHint", 2131628724));
        this.timeEditText.setOnClickListener(new LinkEditActivity$$ExternalSyntheticLambda4(this, context));
        this.timeChooseView.setCallback(new LinkEditActivity$$ExternalSyntheticLambda11(this));
        resetDates();
        anonymousClass3.addView(this.timeEditText, LayoutHelper.createLinear(-1, 50));
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.divider = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setText(LocaleController.getString("TimeLimitHelp", 2131628723));
        anonymousClass3.addView(this.divider);
        HeaderCell headerCell2 = new HeaderCell(context);
        this.usesHeaderCell = headerCell2;
        headerCell2.setText(LocaleController.getString("LimitNumberOfUses", 2131626447));
        anonymousClass3.addView(this.usesHeaderCell);
        SlideChooseView slideChooseView2 = new SlideChooseView(context);
        this.usesChooseView = slideChooseView2;
        slideChooseView2.setCallback(new LinkEditActivity$$ExternalSyntheticLambda12(this));
        resetUses();
        anonymousClass3.addView(this.usesChooseView);
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(this, context);
        this.usesEditText = anonymousClass5;
        anonymousClass5.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.usesEditText.setGravity(16);
        this.usesEditText.setTextSize(1, 16.0f);
        this.usesEditText.setHint(LocaleController.getString("UsesLimitHint", 2131628948));
        this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        this.usesEditText.setInputType(2);
        this.usesEditText.addTextChangedListener(new AnonymousClass6());
        anonymousClass3.addView(this.usesEditText, LayoutHelper.createLinear(-1, 50));
        TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
        this.dividerUses = textInfoPrivacyCell3;
        textInfoPrivacyCell3.setText(LocaleController.getString("UsesLimitHelp", 2131628947));
        anonymousClass3.addView(this.dividerUses);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(this, context);
        this.nameEditText = anonymousClass7;
        anonymousClass7.addTextChangedListener(new AnonymousClass8());
        this.nameEditText.setCursorVisible(false);
        this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        this.nameEditText.setGravity(16);
        this.nameEditText.setHint(LocaleController.getString("LinkNameHint", 2131626502));
        this.nameEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.nameEditText.setLines(1);
        this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.nameEditText.setSingleLine();
        this.nameEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameEditText.setTextSize(1, 16.0f);
        anonymousClass3.addView(this.nameEditText, LayoutHelper.createLinear(-1, 50));
        TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
        this.dividerName = textInfoPrivacyCell4;
        textInfoPrivacyCell4.setBackground(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.dividerName.setText(LocaleController.getString("LinkNameHelp", 2131626501));
        anonymousClass3.addView(this.dividerName);
        if (this.type == 1) {
            TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            this.revokeLink = textSettingsCell;
            textSettingsCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.revokeLink.setText(LocaleController.getString("RevokeLink", 2131628106), false);
            this.revokeLink.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
            this.revokeLink.setOnClickListener(new LinkEditActivity$$ExternalSyntheticLambda3(this));
            anonymousClass3.addView(this.revokeLink);
        }
        anonymousClass2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f));
        anonymousClass3.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 80, 16.0f, 15.0f, 16.0f, 16.0f));
        this.timeHeaderCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.timeChooseView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.timeEditText.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.usesHeaderCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.usesChooseView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.usesEditText.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.nameEditText.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        anonymousClass2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.buttonTextView.setOnClickListener(new LinkEditActivity$$ExternalSyntheticLambda2(this));
        this.buttonTextView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.divider.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
        this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        this.usesEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usesEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.timeEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.timeEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.usesEditText.setCursorVisible(false);
        setInviteToEdit(this.inviteToEdit);
        anonymousClass2.setClipChildren(false);
        this.scrollView.setClipChildren(false);
        anonymousClass3.setClipChildren(false);
        return anonymousClass2;
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            LinkEditActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                LinkEditActivity.this.finishFragment();
                AndroidUtilities.hideKeyboard(LinkEditActivity.this.usesEditText);
            }
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends SizeNotifierFrameLayout {
        int oldKeyboardHeight;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            LinkEditActivity.this = r1;
        }

        /* renamed from: org.telegram.ui.LinkEditActivity$2$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AdjustPanLayoutHelper {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(View view) {
                super(view);
                AnonymousClass2.this = r1;
            }

            @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
            public void onTransitionStart(boolean z, int i) {
                super.onTransitionStart(z, i);
                LinkEditActivity.this.scrollView.getLayoutParams().height = i;
            }

            @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
            public void onTransitionEnd() {
                super.onTransitionEnd();
                LinkEditActivity.this.scrollView.getLayoutParams().height = -1;
                LinkEditActivity.this.scrollView.requestLayout();
            }

            @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
            public void onPanTranslationUpdate(float f, float f2, boolean z) {
                super.onPanTranslationUpdate(f, f2, z);
                AnonymousClass2.this.setTranslationY(0.0f);
            }

            @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
            protected boolean heightAnimationEnabled() {
                return !LinkEditActivity.this.finished;
            }
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected AdjustPanLayoutHelper createAdjustPanLayoutHelper() {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this);
            anonymousClass1.setCheckHierarchyHeight(true);
            return anonymousClass1;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.adjustPanLayoutHelper.onAttach();
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.adjustPanLayoutHelper.onDetach();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            measureKeyboardHeight();
            boolean z = LinkEditActivity.this.usesEditText.isCursorVisible() || LinkEditActivity.this.nameEditText.isCursorVisible();
            int i3 = this.oldKeyboardHeight;
            int i4 = this.keyboardHeight;
            if (i3 == i4 || i4 <= AndroidUtilities.dp(20.0f) || !z) {
                if (LinkEditActivity.this.scrollView.getScrollY() == 0 && !z) {
                    LinkEditActivity.this.scrollToStart = true;
                    invalidate();
                }
            } else {
                LinkEditActivity.this.scrollToEnd = true;
                invalidate();
            }
            int i5 = this.keyboardHeight;
            if (i5 != 0 && i5 < AndroidUtilities.dp(20.0f)) {
                LinkEditActivity.this.usesEditText.clearFocus();
                LinkEditActivity.this.nameEditText.clearFocus();
            }
            this.oldKeyboardHeight = this.keyboardHeight;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int scrollY = LinkEditActivity.this.scrollView.getScrollY();
            super.onLayout(z, i, i2, i3, i4);
            if (scrollY != LinkEditActivity.this.scrollView.getScrollY()) {
                LinkEditActivity linkEditActivity = LinkEditActivity.this;
                if (linkEditActivity.scrollToEnd) {
                    return;
                }
                linkEditActivity.scrollView.setTranslationY(LinkEditActivity.this.scrollView.getScrollY() - scrollY);
                LinkEditActivity.this.scrollView.animate().cancel();
                LinkEditActivity.this.scrollView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
            }
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            LinkEditActivity linkEditActivity = LinkEditActivity.this;
            if (linkEditActivity.scrollToEnd) {
                linkEditActivity.scrollToEnd = false;
                linkEditActivity.scrollView.smoothScrollTo(0, Math.max(0, LinkEditActivity.this.scrollView.getChildAt(0).getMeasuredHeight() - LinkEditActivity.this.scrollView.getMeasuredHeight()));
            } else if (!linkEditActivity.scrollToStart) {
            } else {
                linkEditActivity.scrollToStart = false;
                linkEditActivity.scrollView.smoothScrollTo(0, 0);
            }
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends LinearLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            LinkEditActivity.this = r1;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            super.onMeasure(i, i2);
            int size = View.MeasureSpec.getSize(i2);
            int i4 = 0;
            for (int i5 = 0; i5 < getChildCount(); i5++) {
                View childAt = getChildAt(i5);
                if (childAt != LinkEditActivity.this.buttonTextView && childAt.getVisibility() != 8) {
                    i4 += childAt.getMeasuredHeight();
                }
            }
            int dp = size - ((AndroidUtilities.dp(48.0f) + AndroidUtilities.dp(24.0f)) + AndroidUtilities.dp(16.0f));
            if (i4 >= dp) {
                i3 = AndroidUtilities.dp(24.0f);
            } else {
                i3 = (AndroidUtilities.dp(24.0f) + dp) - i4;
            }
            if (((LinearLayout.LayoutParams) LinkEditActivity.this.buttonTextView.getLayoutParams()).topMargin != i3) {
                int i6 = ((LinearLayout.LayoutParams) LinkEditActivity.this.buttonTextView.getLayoutParams()).topMargin;
                ((LinearLayout.LayoutParams) LinkEditActivity.this.buttonTextView.getLayoutParams()).topMargin = i3;
                if (!LinkEditActivity.this.firstLayout) {
                    LinkEditActivity.this.buttonTextView.setTranslationY(i6 - i3);
                    LinkEditActivity.this.buttonTextView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
                }
                super.onMeasure(i, i2);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            LinkEditActivity.this.firstLayout = false;
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends TextCheckCell {
        AnonymousClass4(LinkEditActivity linkEditActivity, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
        public void onDraw(Canvas canvas) {
            canvas.save();
            canvas.clipRect(0, 0, getWidth(), getHeight());
            super.onDraw(canvas);
            canvas.restore();
        }
    }

    public /* synthetic */ void lambda$createView$0(View view) {
        TextCheckCell textCheckCell = (TextCheckCell) view;
        boolean z = !textCheckCell.isChecked();
        textCheckCell.setBackgroundColorAnimated(z, Theme.getColor(z ? "windowBackgroundChecked" : "windowBackgroundUnchecked"));
        textCheckCell.setChecked(z);
        setUsesVisible(!z);
        this.firstLayout = true;
    }

    public /* synthetic */ void lambda$createView$1(boolean z, int i) {
        chooseDate(i);
    }

    public /* synthetic */ void lambda$createView$2(Context context, View view) {
        AlertsCreator.createDatePickerDialog(context, -1L, new LinkEditActivity$$ExternalSyntheticLambda10(this));
    }

    public /* synthetic */ void lambda$createView$3(int i) {
        if (i < this.dispalyedDates.size()) {
            this.timeEditText.setText(LocaleController.formatDateAudio(this.dispalyedDates.get(i).intValue() + getConnectionsManager().getCurrentTime(), false));
            return;
        }
        this.timeEditText.setText("");
    }

    public /* synthetic */ void lambda$createView$4(int i) {
        this.usesEditText.clearFocus();
        this.ignoreSet = true;
        if (i < this.dispalyedUses.size()) {
            this.usesEditText.setText(this.dispalyedUses.get(i).toString());
        } else {
            this.usesEditText.setText("");
        }
        this.ignoreSet = false;
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends EditText {
        AnonymousClass5(LinkEditActivity linkEditActivity, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                setCursorVisible(true);
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6() {
            LinkEditActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (LinkEditActivity.this.ignoreSet) {
                return;
            }
            if (editable.toString().equals("0")) {
                LinkEditActivity.this.usesEditText.setText("");
                return;
            }
            try {
                int parseInt = Integer.parseInt(editable.toString());
                if (parseInt > 100000) {
                    LinkEditActivity.this.resetUses();
                } else {
                    LinkEditActivity.this.chooseUses(parseInt);
                }
            } catch (NumberFormatException unused) {
                LinkEditActivity.this.resetUses();
            }
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends EditText {
        AnonymousClass7(LinkEditActivity linkEditActivity, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                setCursorVisible(true);
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.LinkEditActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass8() {
            LinkEditActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editable);
            Emoji.replaceEmoji(spannableStringBuilder, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
            int selectionStart = LinkEditActivity.this.nameEditText.getSelectionStart();
            LinkEditActivity.this.nameEditText.removeTextChangedListener(this);
            LinkEditActivity.this.nameEditText.setText(spannableStringBuilder);
            if (selectionStart >= 0) {
                LinkEditActivity.this.nameEditText.setSelection(selectionStart);
            }
            LinkEditActivity.this.nameEditText.addTextChangedListener(this);
        }
    }

    public /* synthetic */ void lambda$createView$6(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("RevokeAlert", 2131628103));
        builder.setTitle(LocaleController.getString("RevokeLink", 2131628106));
        builder.setPositiveButton(LocaleController.getString("RevokeButton", 2131628105), new LinkEditActivity$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$createView$5(DialogInterface dialogInterface, int i) {
        this.callback.revokeLink(this.inviteToEdit);
        finishFragment();
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreateClicked(View view) {
        boolean z;
        String obj;
        if (this.loading) {
            return;
        }
        int selectedIndex = this.timeChooseView.getSelectedIndex();
        if (selectedIndex < this.dispalyedDates.size() && this.dispalyedDates.get(selectedIndex).intValue() < 0) {
            AndroidUtilities.shakeView(this.timeEditText, 2.0f, 0);
            Vibrator vibrator = (Vibrator) this.timeEditText.getContext().getSystemService("vibrator");
            if (vibrator == null) {
                return;
            }
            vibrator.vibrate(200L);
            return;
        }
        int i = this.type;
        if (i == 0) {
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            this.loading = true;
            AlertDialog alertDialog2 = new AlertDialog(getParentActivity(), 3);
            this.progressDialog = alertDialog2;
            alertDialog2.showDelayed(500L);
            TLRPC$TL_messages_exportChatInvite tLRPC$TL_messages_exportChatInvite = new TLRPC$TL_messages_exportChatInvite();
            tLRPC$TL_messages_exportChatInvite.peer = getMessagesController().getInputPeer(-this.chatId);
            tLRPC$TL_messages_exportChatInvite.legacy_revoke_permanent = false;
            int selectedIndex2 = this.timeChooseView.getSelectedIndex();
            tLRPC$TL_messages_exportChatInvite.flags |= 1;
            if (selectedIndex2 < this.dispalyedDates.size()) {
                tLRPC$TL_messages_exportChatInvite.expire_date = this.dispalyedDates.get(selectedIndex2).intValue() + getConnectionsManager().getCurrentTime();
            } else {
                tLRPC$TL_messages_exportChatInvite.expire_date = 0;
            }
            int selectedIndex3 = this.usesChooseView.getSelectedIndex();
            tLRPC$TL_messages_exportChatInvite.flags |= 2;
            if (selectedIndex3 < this.dispalyedUses.size()) {
                tLRPC$TL_messages_exportChatInvite.usage_limit = this.dispalyedUses.get(selectedIndex3).intValue();
            } else {
                tLRPC$TL_messages_exportChatInvite.usage_limit = 0;
            }
            boolean isChecked = this.approveCell.isChecked();
            tLRPC$TL_messages_exportChatInvite.request_needed = isChecked;
            if (isChecked) {
                tLRPC$TL_messages_exportChatInvite.usage_limit = 0;
            }
            String obj2 = this.nameEditText.getText().toString();
            tLRPC$TL_messages_exportChatInvite.title = obj2;
            if (!TextUtils.isEmpty(obj2)) {
                tLRPC$TL_messages_exportChatInvite.flags |= 16;
            }
            getConnectionsManager().sendRequest(tLRPC$TL_messages_exportChatInvite, new LinkEditActivity$$ExternalSyntheticLambda7(this));
        } else if (i != 1) {
        } else {
            AlertDialog alertDialog3 = this.progressDialog;
            if (alertDialog3 != null) {
                alertDialog3.dismiss();
            }
            TLRPC$TL_messages_editExportedChatInvite tLRPC$TL_messages_editExportedChatInvite = new TLRPC$TL_messages_editExportedChatInvite();
            tLRPC$TL_messages_editExportedChatInvite.link = this.inviteToEdit.link;
            tLRPC$TL_messages_editExportedChatInvite.revoked = false;
            tLRPC$TL_messages_editExportedChatInvite.peer = getMessagesController().getInputPeer(-this.chatId);
            int selectedIndex4 = this.timeChooseView.getSelectedIndex();
            if (selectedIndex4 < this.dispalyedDates.size()) {
                if (this.currentInviteDate != this.dispalyedDates.get(selectedIndex4).intValue()) {
                    tLRPC$TL_messages_editExportedChatInvite.flags |= 1;
                    tLRPC$TL_messages_editExportedChatInvite.expire_date = this.dispalyedDates.get(selectedIndex4).intValue() + getConnectionsManager().getCurrentTime();
                    z = true;
                }
                z = false;
            } else {
                if (this.currentInviteDate != 0) {
                    tLRPC$TL_messages_editExportedChatInvite.flags |= 1;
                    tLRPC$TL_messages_editExportedChatInvite.expire_date = 0;
                    z = true;
                }
                z = false;
            }
            int selectedIndex5 = this.usesChooseView.getSelectedIndex();
            if (selectedIndex5 < this.dispalyedUses.size()) {
                int intValue = this.dispalyedUses.get(selectedIndex5).intValue();
                if (this.inviteToEdit.usage_limit != intValue) {
                    tLRPC$TL_messages_editExportedChatInvite.flags |= 2;
                    tLRPC$TL_messages_editExportedChatInvite.usage_limit = intValue;
                    z = true;
                }
                if (this.inviteToEdit.request_needed != this.approveCell.isChecked()) {
                    tLRPC$TL_messages_editExportedChatInvite.flags |= 8;
                    boolean isChecked2 = this.approveCell.isChecked();
                    tLRPC$TL_messages_editExportedChatInvite.request_needed = isChecked2;
                    if (isChecked2) {
                        tLRPC$TL_messages_editExportedChatInvite.flags |= 2;
                        tLRPC$TL_messages_editExportedChatInvite.usage_limit = 0;
                    }
                    z = true;
                }
                obj = this.nameEditText.getText().toString();
                if (!TextUtils.equals(this.inviteToEdit.title, obj)) {
                    tLRPC$TL_messages_editExportedChatInvite.title = obj;
                    tLRPC$TL_messages_editExportedChatInvite.flags |= 16;
                    z = true;
                }
                if (!z) {
                    this.loading = true;
                    AlertDialog alertDialog4 = new AlertDialog(getParentActivity(), 3);
                    this.progressDialog = alertDialog4;
                    alertDialog4.showDelayed(500L);
                    getConnectionsManager().sendRequest(tLRPC$TL_messages_editExportedChatInvite, new LinkEditActivity$$ExternalSyntheticLambda8(this));
                    return;
                }
                finishFragment();
                return;
            }
            if (this.inviteToEdit.usage_limit != 0) {
                tLRPC$TL_messages_editExportedChatInvite.flags |= 2;
                tLRPC$TL_messages_editExportedChatInvite.usage_limit = 0;
                z = true;
            }
            if (this.inviteToEdit.request_needed != this.approveCell.isChecked()) {
            }
            obj = this.nameEditText.getText().toString();
            if (!TextUtils.equals(this.inviteToEdit.title, obj)) {
            }
            if (!z) {
            }
        }
    }

    public /* synthetic */ void lambda$onCreateClicked$8(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LinkEditActivity$$ExternalSyntheticLambda6(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$onCreateClicked$7(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.loading = false;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (tLRPC$TL_error == null) {
            Callback callback = this.callback;
            if (callback != null) {
                callback.onLinkCreated(tLObject);
            }
            finishFragment();
            return;
        }
        AlertsCreator.showSimpleAlert(this, tLRPC$TL_error.text);
    }

    public /* synthetic */ void lambda$onCreateClicked$10(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LinkEditActivity$$ExternalSyntheticLambda5(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$onCreateClicked$9(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.loading = false;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (tLRPC$TL_error == null) {
            if (tLObject instanceof TLRPC$TL_messages_exportedChatInvite) {
                this.inviteToEdit = (TLRPC$TL_chatInviteExported) ((TLRPC$TL_messages_exportedChatInvite) tLObject).invite;
            }
            Callback callback = this.callback;
            if (callback != null) {
                callback.onLinkEdited(this.inviteToEdit, tLObject);
            }
            finishFragment();
            return;
        }
        AlertsCreator.showSimpleAlert(this, tLRPC$TL_error.text);
    }

    public void chooseUses(int i) {
        this.dispalyedUses.clear();
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        while (true) {
            int[] iArr = this.defaultUses;
            if (i2 >= iArr.length) {
                break;
            }
            if (!z && i <= iArr[i2]) {
                if (i != iArr[i2]) {
                    this.dispalyedUses.add(Integer.valueOf(i));
                }
                i3 = i2;
                z = true;
            }
            this.dispalyedUses.add(Integer.valueOf(this.defaultUses[i2]));
            i2++;
        }
        if (!z) {
            this.dispalyedUses.add(Integer.valueOf(i));
            i3 = this.defaultUses.length;
        }
        int size = this.dispalyedUses.size() + 1;
        String[] strArr = new String[size];
        for (int i4 = 0; i4 < size; i4++) {
            if (i4 == size - 1) {
                strArr[i4] = LocaleController.getString("NoLimit", 2131626881);
            } else {
                strArr[i4] = this.dispalyedUses.get(i4).toString();
            }
        }
        this.usesChooseView.setOptions(i3, strArr);
    }

    private void chooseDate(int i) {
        long j = i;
        this.timeEditText.setText(LocaleController.formatDateAudio(j, false));
        int currentTime = i - getConnectionsManager().getCurrentTime();
        this.dispalyedDates.clear();
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        while (true) {
            int[] iArr = this.defaultDates;
            if (i2 >= iArr.length) {
                break;
            }
            if (!z && currentTime < iArr[i2]) {
                this.dispalyedDates.add(Integer.valueOf(currentTime));
                i3 = i2;
                z = true;
            }
            this.dispalyedDates.add(Integer.valueOf(this.defaultDates[i2]));
            i2++;
        }
        if (!z) {
            this.dispalyedDates.add(Integer.valueOf(currentTime));
            i3 = this.defaultDates.length;
        }
        int size = this.dispalyedDates.size() + 1;
        String[] strArr = new String[size];
        for (int i4 = 0; i4 < size; i4++) {
            if (i4 == size - 1) {
                strArr[i4] = LocaleController.getString("NoLimit", 2131626881);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[0]) {
                strArr[i4] = LocaleController.formatPluralString("Hours", 1, new Object[0]);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[1]) {
                strArr[i4] = LocaleController.formatPluralString("Days", 1, new Object[0]);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[2]) {
                strArr[i4] = LocaleController.formatPluralString("Weeks", 1, new Object[0]);
            } else {
                long j2 = currentTime;
                if (j2 < 86400) {
                    strArr[i4] = LocaleController.getString("MessageScheduleToday", 2131626695);
                } else if (j2 < 31449600) {
                    strArr[i4] = LocaleController.getInstance().formatterScheduleDay.format(j * 1000);
                } else {
                    strArr[i4] = LocaleController.getInstance().formatterYear.format(j * 1000);
                }
            }
        }
        this.timeChooseView.setOptions(i3, strArr);
    }

    private void resetDates() {
        this.dispalyedDates.clear();
        int i = 0;
        while (true) {
            int[] iArr = this.defaultDates;
            if (i < iArr.length) {
                this.dispalyedDates.add(Integer.valueOf(iArr[i]));
                i++;
            } else {
                this.timeChooseView.setOptions(3, LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Days", 1, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.getString("NoLimit", 2131626881));
                return;
            }
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void resetUses() {
        this.dispalyedUses.clear();
        int i = 0;
        while (true) {
            int[] iArr = this.defaultUses;
            if (i < iArr.length) {
                this.dispalyedUses.add(Integer.valueOf(iArr[i]));
                i++;
            } else {
                this.usesChooseView.setOptions(3, "1", "10", "100", LocaleController.getString("NoLimit", 2131626881));
                return;
            }
        }
    }

    public void setInviteToEdit(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
        this.inviteToEdit = tLRPC$TL_chatInviteExported;
        if (this.fragmentView == null || tLRPC$TL_chatInviteExported == null) {
            return;
        }
        int i = tLRPC$TL_chatInviteExported.expire_date;
        if (i > 0) {
            chooseDate(i);
            this.currentInviteDate = this.dispalyedDates.get(this.timeChooseView.getSelectedIndex()).intValue();
        } else {
            this.currentInviteDate = 0;
        }
        int i2 = tLRPC$TL_chatInviteExported.usage_limit;
        if (i2 > 0) {
            chooseUses(i2);
            this.usesEditText.setText(Integer.toString(tLRPC$TL_chatInviteExported.usage_limit));
        }
        this.approveCell.setBackgroundColor(Theme.getColor(tLRPC$TL_chatInviteExported.request_needed ? "windowBackgroundChecked" : "windowBackgroundUnchecked"));
        this.approveCell.setChecked(tLRPC$TL_chatInviteExported.request_needed);
        setUsesVisible(!tLRPC$TL_chatInviteExported.request_needed);
        if (TextUtils.isEmpty(tLRPC$TL_chatInviteExported.title)) {
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tLRPC$TL_chatInviteExported.title);
        Emoji.replaceEmoji(spannableStringBuilder, this.nameEditText.getPaint().getFontMetricsInt(), (int) this.nameEditText.getPaint().getTextSize(), false);
        this.nameEditText.setText(spannableStringBuilder);
    }

    private void setUsesVisible(boolean z) {
        int i = 0;
        this.usesHeaderCell.setVisibility(z ? 0 : 8);
        this.usesChooseView.setVisibility(z ? 0 : 8);
        this.usesEditText.setVisibility(z ? 0 : 8);
        TextInfoPrivacyCell textInfoPrivacyCell = this.dividerUses;
        if (!z) {
            i = 8;
        }
        textInfoPrivacyCell.setVisibility(i);
        this.divider.setBackground(Theme.getThemedDrawable(getParentActivity(), z ? 2131165435 : 2131165436, "windowBackgroundGrayShadow"));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void finishFragment() {
        this.scrollView.getLayoutParams().height = this.scrollView.getHeight();
        this.finished = true;
        super.finishFragment();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        LinkEditActivity$$ExternalSyntheticLambda9 linkEditActivity$$ExternalSyntheticLambda9 = new LinkEditActivity$$ExternalSyntheticLambda9(this);
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.timeHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.timeHeaderCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.timeChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.usesChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.timeEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.usesEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.revokeLink, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.divider, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.dividerUses, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.dividerName, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "featuredStickers_addButtonPressed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, linkEditActivity$$ExternalSyntheticLambda9, "windowBackgroundWhiteRedText5"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$11() {
        TextInfoPrivacyCell textInfoPrivacyCell = this.dividerUses;
        if (textInfoPrivacyCell != null) {
            Context context = textInfoPrivacyCell.getContext();
            this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.divider.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
            this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
            this.usesEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.usesEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            this.timeEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.timeEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            this.buttonTextView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
            TextSettingsCell textSettingsCell = this.revokeLink;
            if (textSettingsCell != null) {
                textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
            }
            this.createTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
            this.dividerName.setBackground(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.nameEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.nameEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        }
    }
}
