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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_editExportedChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_exportChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_exportedChatInvite;
import org.telegram.tgnet.TLRPC$TL_starsSubscriptionPricing;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Stories.recorder.KeyboardNotifier;
/* loaded from: classes4.dex */
public class LinkEditActivity extends BaseFragment {
    private TextCheckCell approveCell;
    private TextInfoPrivacyCell approveHintCell;
    private FrameLayout buttonLayout;
    private TextView buttonTextView;
    private Callback callback;
    private final long chatId;
    private TextView createTextView;
    int currentInviteDate;
    private TextInfoPrivacyCell divider;
    private TextInfoPrivacyCell dividerName;
    private TextInfoPrivacyCell dividerUses;
    private boolean firstLayout;
    private boolean ignoreSet;
    TLRPC$TL_chatInviteExported inviteToEdit;
    boolean loading;
    private EditText nameEditText;
    AlertDialog progressDialog;
    private TextSettingsCell revokeLink;
    boolean scrollToEnd;
    boolean scrollToStart;
    private ScrollView scrollView;
    private TextCheckCell subCell;
    private EditTextCell subEditPriceCell;
    private TextInfoPrivacyCell subInfoCell;
    private TextView subPriceView;
    private SlideChooseView timeChooseView;
    private TextView timeEditText;
    private HeaderCell timeHeaderCell;
    private int type;
    private SlideChooseView usesChooseView;
    private EditText usesEditText;
    private HeaderCell usesHeaderCell;
    private int shakeDp = -3;
    private ArrayList<Integer> dispalyedDates = new ArrayList<>();
    private final int[] defaultDates = {3600, 86400, 604800};
    private ArrayList<Integer> dispalyedUses = new ArrayList<>();
    private final int[] defaultUses = {1, 10, 100};

    /* loaded from: classes4.dex */
    public interface Callback {
        void onLinkCreated(TLObject tLObject);

        void onLinkEdited(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported, TLObject tLObject);

        void onLinkRemoved(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);

        void revokeLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$11(Integer num) {
    }

    public LinkEditActivity(int i, long j) {
        this.type = i;
        this.chatId = j;
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x0539  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported;
        int i;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.type;
        if (i2 == 0) {
            this.actionBar.setTitle(LocaleController.getString("NewLink", R.string.NewLink));
        } else if (i2 == 1) {
            this.actionBar.setTitle(LocaleController.getString("EditLink", R.string.EditLink));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LinkEditActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    LinkEditActivity.this.finishFragment();
                    AndroidUtilities.hideKeyboard(LinkEditActivity.this.usesEditText);
                }
            }
        });
        TextView textView = new TextView(context);
        this.createTextView = textView;
        textView.setEllipsize(TextUtils.TruncateAt.END);
        this.createTextView.setGravity(16);
        this.createTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.onCreateClicked(view);
            }
        });
        this.createTextView.setSingleLine();
        int i3 = this.type;
        if (i3 == 0) {
            this.createTextView.setText(LocaleController.getString("CreateLinkHeader", R.string.CreateLinkHeader));
        } else if (i3 == 1) {
            this.createTextView.setText(LocaleController.getString("SaveLinkHeader", R.string.SaveLinkHeader));
        }
        this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        this.createTextView.setTextSize(1, 14.0f);
        this.createTextView.setTypeface(AndroidUtilities.bold());
        this.createTextView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f));
        this.actionBar.addView(this.createTextView, LayoutHelper.createFrame(-2, -2.0f, 8388629, 0.0f, this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight / AndroidUtilities.dp(2.0f) : 0, 0.0f, 0.0f));
        this.scrollView = new ScrollView(context);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.LinkEditActivity.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                super.onMeasure(i4, i5);
                measureKeyboardHeight();
                int i6 = this.keyboardHeight;
                if (i6 != 0 && i6 < AndroidUtilities.dp(20.0f)) {
                    LinkEditActivity.this.usesEditText.clearFocus();
                    LinkEditActivity.this.nameEditText.clearFocus();
                }
                LinkEditActivity.this.buttonLayout.setVisibility(this.keyboardHeight > AndroidUtilities.dp(20.0f) ? 8 : 0);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i4, int i5, int i6, int i7) {
                int scrollY = LinkEditActivity.this.scrollView.getScrollY();
                super.onLayout(z, i4, i5, i6, i7);
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

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LinkEditActivity linkEditActivity = LinkEditActivity.this;
                if (linkEditActivity.scrollToEnd) {
                    linkEditActivity.scrollToEnd = false;
                    linkEditActivity.scrollView.smoothScrollTo(0, Math.max(0, LinkEditActivity.this.scrollView.getChildAt(0).getMeasuredHeight() - LinkEditActivity.this.scrollView.getMeasuredHeight()));
                } else if (linkEditActivity.scrollToStart) {
                    linkEditActivity.scrollToStart = false;
                    linkEditActivity.scrollView.smoothScrollTo(0, 0);
                }
            }
        };
        this.fragmentView = sizeNotifierFrameLayout;
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.LinkEditActivity.3
            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                super.onMeasure(i4, i5);
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LinkEditActivity.this.firstLayout = false;
            }
        };
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(420L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        layoutTransition.setInterpolator(2, cubicBezierInterpolator);
        layoutTransition.setInterpolator(0, cubicBezierInterpolator);
        layoutTransition.setInterpolator(4, cubicBezierInterpolator);
        layoutTransition.setInterpolator(1, cubicBezierInterpolator);
        layoutTransition.setInterpolator(3, cubicBezierInterpolator);
        linearLayout.setLayoutTransition(layoutTransition);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, 0, 0, AndroidUtilities.dp(79.0f));
        this.scrollView.addView(linearLayout);
        TextView textView2 = new TextView(context);
        this.buttonTextView = textView2;
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextSize(1, 14.0f);
        this.buttonTextView.setTypeface(AndroidUtilities.bold());
        int i4 = this.type;
        if (i4 == 0) {
            this.buttonTextView.setText(LocaleController.getString("CreateLink", R.string.CreateLink));
        } else if (i4 == 1) {
            this.buttonTextView.setText(LocaleController.getString("SaveLink", R.string.SaveLink));
        }
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        if (chat == null || chat.username == null) {
            TextCheckCell textCheckCell = new TextCheckCell(this, context) { // from class: org.telegram.ui.LinkEditActivity.4
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
                public void onDraw(Canvas canvas) {
                    canvas.save();
                    canvas.clipRect(0, 0, getWidth(), getHeight());
                    super.onDraw(canvas);
                    canvas.restore();
                }
            };
            this.approveCell = textCheckCell;
            int i5 = Theme.key_windowBackgroundUnchecked;
            textCheckCell.setBackgroundColor(Theme.getColor(i5));
            this.approveCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
            this.approveCell.setDrawCheckRipple(true);
            this.approveCell.setHeight(56);
            this.approveCell.setTag(Integer.valueOf(i5));
            this.approveCell.setTextAndCheck(LocaleController.getString(R.string.ApproveNewMembers), false, false);
            this.approveCell.setTypeface(AndroidUtilities.bold());
            this.approveCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.lambda$createView$0(view);
                }
            });
            linearLayout.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.approveHintCell = textInfoPrivacyCell;
            textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
            linearLayout.addView(this.approveHintCell);
            TLRPC$ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
            if ((this.inviteToEdit == null && ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chatId))) && chatFull != null && chatFull.paid_media_allowed) || ((tLRPC$TL_chatInviteExported = this.inviteToEdit) != null && tLRPC$TL_chatInviteExported.subscription_pricing != null)) {
                TextCheckCell textCheckCell2 = new TextCheckCell(context);
                this.subCell = textCheckCell2;
                int i6 = Theme.key_windowBackgroundWhite;
                textCheckCell2.setBackgroundColor(Theme.getColor(i6));
                this.subCell.setDrawCheckRipple(true);
                this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
                if (this.inviteToEdit != null) {
                    this.subCell.setCheckBoxIcon(R.drawable.permission_locked);
                    this.subCell.setEnabled(false);
                }
                final Runnable[] runnableArr = new Runnable[1];
                this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.lambda$createView$3(runnableArr, view);
                    }
                });
                linearLayout.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
                TextView textView3 = new TextView(context);
                this.subPriceView = textView3;
                textView3.setTextSize(1, 16.0f);
                this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
                i = -1;
                EditTextCell editTextCell = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
                    private boolean ignoreTextChanged;

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Cells.EditTextCell
                    public void onTextChanged(CharSequence charSequence) {
                        super.onTextChanged(charSequence);
                        if (this.ignoreTextChanged) {
                            return;
                        }
                        if (TextUtils.isEmpty(charSequence)) {
                            LinkEditActivity.this.subPriceView.setText("");
                            return;
                        }
                        try {
                            long parseLong = Long.parseLong(charSequence.toString());
                            if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                                this.ignoreTextChanged = true;
                                parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                                setText(Long.toString(parseLong));
                                this.ignoreTextChanged = false;
                            }
                            TextView textView4 = LinkEditActivity.this.subPriceView;
                            int i7 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                            Object[] objArr = new Object[1];
                            BillingController billingController = BillingController.getInstance();
                            double d = parseLong;
                            Double.isNaN(d);
                            double d2 = d / 1000.0d;
                            double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                            Double.isNaN(d3);
                            objArr[0] = billingController.formatCurrency((long) (d2 * d3), "USD");
                            textView4.setText(LocaleController.formatString(i7, objArr));
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                };
                this.subEditPriceCell = editTextCell;
                editTextCell.editText.setInputType(2);
                this.subEditPriceCell.editText.setRawInputType(2);
                this.subEditPriceCell.setBackgroundColor(getThemedColor(i6));
                this.subEditPriceCell.hideKeyboardOnEnter();
                this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
                ImageView leftDrawable = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
                leftDrawable.setScaleX(0.83f);
                leftDrawable.setScaleY(0.83f);
                leftDrawable.setTranslationY(AndroidUtilities.dp(-1.0f));
                leftDrawable.setTranslationX(AndroidUtilities.dp(1.0f));
                linearLayout.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
                this.subEditPriceCell.setVisibility(8);
                TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
                this.subInfoCell = textInfoPrivacyCell2;
                if (this.inviteToEdit != null) {
                    textInfoPrivacyCell2.setText(LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen));
                } else {
                    textInfoPrivacyCell2.setText(AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            LinkEditActivity.this.lambda$createView$4();
                        }
                    }));
                }
                linearLayout.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
                HeaderCell headerCell = new HeaderCell(context);
                this.timeHeaderCell = headerCell;
                headerCell.setText(LocaleController.getString(R.string.LimitByPeriod));
                linearLayout.addView(this.timeHeaderCell);
                SlideChooseView slideChooseView = new SlideChooseView(context);
                this.timeChooseView = slideChooseView;
                linearLayout.addView(slideChooseView);
                TextView textView4 = new TextView(context);
                this.timeEditText = textView4;
                textView4.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.timeEditText.setGravity(16);
                this.timeEditText.setTextSize(1, 16.0f);
                this.timeEditText.setHint(LocaleController.getString("TimeLimitHint", R.string.TimeLimitHint));
                this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.lambda$createView$6(context, view);
                    }
                });
                this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda17
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i7) {
                        LinkEditActivity.this.lambda$createView$7(i7);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                resetDates();
                linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i, 50));
                TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
                this.divider = textInfoPrivacyCell3;
                textInfoPrivacyCell3.setText(LocaleController.getString("TimeLimitHelp", R.string.TimeLimitHelp));
                linearLayout.addView(this.divider);
                HeaderCell headerCell2 = new HeaderCell(context);
                this.usesHeaderCell = headerCell2;
                headerCell2.setText(LocaleController.getString("LimitNumberOfUses", R.string.LimitNumberOfUses));
                linearLayout.addView(this.usesHeaderCell);
                SlideChooseView slideChooseView2 = new SlideChooseView(context);
                this.usesChooseView = slideChooseView2;
                slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda16
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i7) {
                        LinkEditActivity.this.lambda$createView$8(i7);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                resetUses();
                linearLayout.addView(this.usesChooseView);
                EditText editText = new EditText(this, context) { // from class: org.telegram.ui.LinkEditActivity.6
                    @Override // android.widget.TextView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            setCursorVisible(true);
                        }
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.usesEditText = editText;
                editText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.usesEditText.setGravity(16);
                this.usesEditText.setTextSize(1, 16.0f);
                this.usesEditText.setHint(LocaleController.getString("UsesLimitHint", R.string.UsesLimitHint));
                this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                this.usesEditText.setInputType(2);
                this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
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
                });
                linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i, 50));
                TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
                this.dividerUses = textInfoPrivacyCell4;
                textInfoPrivacyCell4.setText(LocaleController.getString("UsesLimitHelp", R.string.UsesLimitHelp));
                linearLayout.addView(this.dividerUses);
                EditText editText2 = new EditText(this, context) { // from class: org.telegram.ui.LinkEditActivity.8
                    @Override // android.widget.TextView, android.view.View
                    @SuppressLint({"ClickableViewAccessibility"})
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            setCursorVisible(true);
                        }
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.nameEditText = editText2;
                editText2.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                    }
                });
                this.nameEditText.setCursorVisible(false);
                this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                this.nameEditText.setGravity(16);
                this.nameEditText.setHint(LocaleController.getString("LinkNameHint", R.string.LinkNameHint));
                EditText editText3 = this.nameEditText;
                int i7 = Theme.key_windowBackgroundWhiteGrayText;
                editText3.setHintTextColor(Theme.getColor(i7));
                this.nameEditText.setLines(1);
                this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.nameEditText.setSingleLine();
                EditText editText4 = this.nameEditText;
                int i8 = Theme.key_windowBackgroundWhiteBlackText;
                editText4.setTextColor(Theme.getColor(i8));
                this.nameEditText.setTextSize(1, 16.0f);
                linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i, 50));
                TextInfoPrivacyCell textInfoPrivacyCell5 = new TextInfoPrivacyCell(context);
                this.dividerName = textInfoPrivacyCell5;
                int i9 = R.drawable.greydivider_bottom;
                int i10 = Theme.key_windowBackgroundGrayShadow;
                textInfoPrivacyCell5.setBackground(Theme.getThemedDrawableByKey(context, i9, i10));
                this.dividerName.setText(LocaleController.getString("LinkNameHelp", R.string.LinkNameHelp));
                linearLayout.addView(this.dividerName);
                if (this.type == 1) {
                    TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                    this.revokeLink = textSettingsCell;
                    textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    this.revokeLink.setText(LocaleController.getString("RevokeLink", R.string.RevokeLink), false);
                    this.revokeLink.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                    this.revokeLink.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.lambda$createView$10(view);
                        }
                    });
                    linearLayout.addView(this.revokeLink);
                }
                sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i, -1.0f));
                FrameLayout frameLayout = new FrameLayout(context);
                this.buttonLayout = frameLayout;
                int i11 = Theme.key_windowBackgroundGray;
                frameLayout.setBackgroundColor(getThemedColor(i11));
                new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda11
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        LinkEditActivity.lambda$createView$11((Integer) obj);
                    }
                });
                this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
                sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i, -2, 80));
                HeaderCell headerCell3 = this.timeHeaderCell;
                int i12 = Theme.key_windowBackgroundWhite;
                headerCell3.setBackgroundColor(Theme.getColor(i12));
                this.timeChooseView.setBackgroundColor(Theme.getColor(i12));
                this.timeEditText.setBackgroundColor(Theme.getColor(i12));
                this.usesHeaderCell.setBackgroundColor(Theme.getColor(i12));
                this.usesChooseView.setBackgroundColor(Theme.getColor(i12));
                this.usesEditText.setBackgroundColor(Theme.getColor(i12));
                this.nameEditText.setBackgroundColor(Theme.getColor(i12));
                sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i11));
                this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.onCreateClicked(view);
                    }
                });
                this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i9, i10));
                this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i10));
                this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
                this.usesEditText.setTextColor(Theme.getColor(i8));
                this.usesEditText.setHintTextColor(Theme.getColor(i7));
                this.timeEditText.setTextColor(Theme.getColor(i8));
                this.timeEditText.setHintTextColor(Theme.getColor(i7));
                this.usesEditText.setCursorVisible(false);
                setInviteToEdit(this.inviteToEdit);
                sizeNotifierFrameLayout.setClipChildren(false);
                this.scrollView.setClipChildren(false);
                linearLayout.setClipChildren(false);
                return sizeNotifierFrameLayout;
            }
        }
        i = -1;
        HeaderCell headerCell4 = new HeaderCell(context);
        this.timeHeaderCell = headerCell4;
        headerCell4.setText(LocaleController.getString(R.string.LimitByPeriod));
        linearLayout.addView(this.timeHeaderCell);
        SlideChooseView slideChooseView3 = new SlideChooseView(context);
        this.timeChooseView = slideChooseView3;
        linearLayout.addView(slideChooseView3);
        TextView textView42 = new TextView(context);
        this.timeEditText = textView42;
        textView42.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.timeEditText.setGravity(16);
        this.timeEditText.setTextSize(1, 16.0f);
        this.timeEditText.setHint(LocaleController.getString("TimeLimitHint", R.string.TimeLimitHint));
        this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.lambda$createView$6(context, view);
            }
        });
        this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda17
            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public final void onOptionSelected(int i72) {
                LinkEditActivity.this.lambda$createView$7(i72);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public /* synthetic */ void onTouchEnd() {
                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
            }
        });
        resetDates();
        linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i, 50));
        TextInfoPrivacyCell textInfoPrivacyCell32 = new TextInfoPrivacyCell(context);
        this.divider = textInfoPrivacyCell32;
        textInfoPrivacyCell32.setText(LocaleController.getString("TimeLimitHelp", R.string.TimeLimitHelp));
        linearLayout.addView(this.divider);
        HeaderCell headerCell22 = new HeaderCell(context);
        this.usesHeaderCell = headerCell22;
        headerCell22.setText(LocaleController.getString("LimitNumberOfUses", R.string.LimitNumberOfUses));
        linearLayout.addView(this.usesHeaderCell);
        SlideChooseView slideChooseView22 = new SlideChooseView(context);
        this.usesChooseView = slideChooseView22;
        slideChooseView22.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda16
            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public final void onOptionSelected(int i72) {
                LinkEditActivity.this.lambda$createView$8(i72);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public /* synthetic */ void onTouchEnd() {
                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
            }
        });
        resetUses();
        linearLayout.addView(this.usesChooseView);
        EditText editText5 = new EditText(this, context) { // from class: org.telegram.ui.LinkEditActivity.6
            @Override // android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    setCursorVisible(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.usesEditText = editText5;
        editText5.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.usesEditText.setGravity(16);
        this.usesEditText.setTextSize(1, 16.0f);
        this.usesEditText.setHint(LocaleController.getString("UsesLimitHint", R.string.UsesLimitHint));
        this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        this.usesEditText.setInputType(2);
        this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i72, int i82, int i92) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i72, int i82, int i92) {
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
        });
        linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i, 50));
        TextInfoPrivacyCell textInfoPrivacyCell42 = new TextInfoPrivacyCell(context);
        this.dividerUses = textInfoPrivacyCell42;
        textInfoPrivacyCell42.setText(LocaleController.getString("UsesLimitHelp", R.string.UsesLimitHelp));
        linearLayout.addView(this.dividerUses);
        EditText editText22 = new EditText(this, context) { // from class: org.telegram.ui.LinkEditActivity.8
            @Override // android.widget.TextView, android.view.View
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    setCursorVisible(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.nameEditText = editText22;
        editText22.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i72, int i82, int i92) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i72, int i82, int i92) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
            }
        });
        this.nameEditText.setCursorVisible(false);
        this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        this.nameEditText.setGravity(16);
        this.nameEditText.setHint(LocaleController.getString("LinkNameHint", R.string.LinkNameHint));
        EditText editText32 = this.nameEditText;
        int i72 = Theme.key_windowBackgroundWhiteGrayText;
        editText32.setHintTextColor(Theme.getColor(i72));
        this.nameEditText.setLines(1);
        this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.nameEditText.setSingleLine();
        EditText editText42 = this.nameEditText;
        int i82 = Theme.key_windowBackgroundWhiteBlackText;
        editText42.setTextColor(Theme.getColor(i82));
        this.nameEditText.setTextSize(1, 16.0f);
        linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i, 50));
        TextInfoPrivacyCell textInfoPrivacyCell52 = new TextInfoPrivacyCell(context);
        this.dividerName = textInfoPrivacyCell52;
        int i92 = R.drawable.greydivider_bottom;
        int i102 = Theme.key_windowBackgroundGrayShadow;
        textInfoPrivacyCell52.setBackground(Theme.getThemedDrawableByKey(context, i92, i102));
        this.dividerName.setText(LocaleController.getString("LinkNameHelp", R.string.LinkNameHelp));
        linearLayout.addView(this.dividerName);
        if (this.type == 1) {
        }
        sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i, -1.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.buttonLayout = frameLayout2;
        int i112 = Theme.key_windowBackgroundGray;
        frameLayout2.setBackgroundColor(getThemedColor(i112));
        new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                LinkEditActivity.lambda$createView$11((Integer) obj);
            }
        });
        this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
        sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i, -2, 80));
        HeaderCell headerCell32 = this.timeHeaderCell;
        int i122 = Theme.key_windowBackgroundWhite;
        headerCell32.setBackgroundColor(Theme.getColor(i122));
        this.timeChooseView.setBackgroundColor(Theme.getColor(i122));
        this.timeEditText.setBackgroundColor(Theme.getColor(i122));
        this.usesHeaderCell.setBackgroundColor(Theme.getColor(i122));
        this.usesChooseView.setBackgroundColor(Theme.getColor(i122));
        this.usesEditText.setBackgroundColor(Theme.getColor(i122));
        this.nameEditText.setBackgroundColor(Theme.getColor(i122));
        sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i112));
        this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.onCreateClicked(view);
            }
        });
        this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i92, i102));
        this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i102));
        this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
        this.usesEditText.setTextColor(Theme.getColor(i82));
        this.usesEditText.setHintTextColor(Theme.getColor(i72));
        this.timeEditText.setTextColor(Theme.getColor(i82));
        this.timeEditText.setHintTextColor(Theme.getColor(i72));
        this.usesEditText.setCursorVisible(false);
        setInviteToEdit(this.inviteToEdit);
        sizeNotifierFrameLayout.setClipChildren(false);
        this.scrollView.setClipChildren(false);
        linearLayout.setClipChildren(false);
        return sizeNotifierFrameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        TextCheckCell textCheckCell = this.subCell;
        if (textCheckCell != null && textCheckCell.isChecked()) {
            TextCheckCell textCheckCell2 = this.subCell;
            int i = -this.shakeDp;
            this.shakeDp = i;
            AndroidUtilities.shakeViewSpring(textCheckCell2, i);
            return;
        }
        TextCheckCell textCheckCell3 = (TextCheckCell) view;
        boolean z = !textCheckCell3.isChecked();
        textCheckCell3.setBackgroundColorAnimated(z, Theme.getColor(z ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        textCheckCell3.setChecked(z);
        setUsesVisible(!z);
        if (this.subCell != null) {
            if (textCheckCell3.isChecked()) {
                this.subCell.setChecked(false);
                this.subCell.setCheckBoxIcon(R.drawable.permission_locked);
                this.subEditPriceCell.setVisibility(8);
            } else if (this.inviteToEdit == null) {
                this.subCell.setCheckBoxIcon(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(Runnable[] runnableArr, View view) {
        if (this.inviteToEdit != null) {
            return;
        }
        if (this.approveCell.isChecked()) {
            TextCheckCell textCheckCell = this.approveCell;
            int i = -this.shakeDp;
            this.shakeDp = i;
            AndroidUtilities.shakeViewSpring(textCheckCell, i);
            return;
        }
        TextCheckCell textCheckCell2 = (TextCheckCell) view;
        textCheckCell2.setChecked(!textCheckCell2.isChecked());
        this.subEditPriceCell.setVisibility(textCheckCell2.isChecked() ? 0 : 8);
        AndroidUtilities.cancelRunOnUIThread(runnableArr[0]);
        if (textCheckCell2.isChecked()) {
            this.approveCell.setChecked(false);
            this.approveCell.setCheckBoxIcon(R.drawable.permission_locked);
            this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescriptionFrozen));
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LinkEditActivity.this.lambda$createView$1();
                }
            };
            runnableArr[0] = runnable;
            AndroidUtilities.runOnUIThread(runnable, 60L);
            return;
        }
        this.approveCell.setCheckBoxIcon(0);
        this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$createView$2();
            }
        };
        runnableArr[0] = runnable2;
        AndroidUtilities.runOnUIThread(runnable2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1() {
        this.subEditPriceCell.editText.requestFocus();
        AndroidUtilities.showKeyboard(this.subEditPriceCell.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        this.subEditPriceCell.editText.clearFocus();
        AndroidUtilities.hideKeyboard(this.subEditPriceCell.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        Browser.openUrl(getContext(), LocaleController.getString(R.string.RequireMonthlyFeeInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(boolean z, int i) {
        chooseDate(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(Context context, View view) {
        AlertsCreator.createDatePickerDialog(context, LocaleController.getString(R.string.ExpireAfter), LocaleController.getString(R.string.SetTimeLimit), -1L, new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public final void didSelectDate(boolean z, int i) {
                LinkEditActivity.this.lambda$createView$5(z, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(int i) {
        if (i < this.dispalyedDates.size()) {
            this.timeEditText.setText(LocaleController.formatDateAudio(this.dispalyedDates.get(i).intValue() + getConnectionsManager().getCurrentTime(), false));
            return;
        }
        this.timeEditText.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(int i) {
        this.usesEditText.clearFocus();
        this.ignoreSet = true;
        if (i < this.dispalyedUses.size()) {
            this.usesEditText.setText(this.dispalyedUses.get(i).toString());
        } else {
            this.usesEditText.setText("");
        }
        this.ignoreSet = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("RevokeAlert", R.string.RevokeAlert));
        builder.setTitle(LocaleController.getString("RevokeLink", R.string.RevokeLink));
        builder.setPositiveButton(LocaleController.getString("RevokeButton", R.string.RevokeButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LinkEditActivity.this.lambda$createView$9(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(DialogInterface dialogInterface, int i) {
        this.callback.revokeLink(this.inviteToEdit);
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0239  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreateClicked(View view) {
        long j;
        int i;
        boolean z;
        boolean z2;
        TextCheckCell textCheckCell;
        String obj;
        if (this.loading) {
            return;
        }
        int selectedIndex = this.timeChooseView.getSelectedIndex();
        if (selectedIndex < this.dispalyedDates.size() && this.dispalyedDates.get(selectedIndex).intValue() < 0) {
            AndroidUtilities.shakeView(this.timeEditText);
            Vibrator vibrator = (Vibrator) this.timeEditText.getContext().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
                return;
            }
            return;
        }
        TextCheckCell textCheckCell2 = this.subCell;
        if (textCheckCell2 != null && textCheckCell2.isChecked()) {
            try {
                j = Long.parseLong(this.subEditPriceCell.editText.getText().toString());
            } catch (Exception e) {
                FileLog.e(e);
            }
            i = this.type;
            boolean z3 = true;
            if (i != 0) {
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
                TextCheckCell textCheckCell3 = this.approveCell;
                z3 = (textCheckCell3 == null || !textCheckCell3.isChecked()) ? false : false;
                tLRPC$TL_messages_exportChatInvite.request_needed = z3;
                if (z3) {
                    tLRPC$TL_messages_exportChatInvite.usage_limit = 0;
                }
                String obj2 = this.nameEditText.getText().toString();
                tLRPC$TL_messages_exportChatInvite.title = obj2;
                if (!TextUtils.isEmpty(obj2)) {
                    tLRPC$TL_messages_exportChatInvite.flags |= 16;
                }
                if (j > 0) {
                    tLRPC$TL_messages_exportChatInvite.flags |= 32;
                    TLRPC$TL_starsSubscriptionPricing tLRPC$TL_starsSubscriptionPricing = new TLRPC$TL_starsSubscriptionPricing();
                    tLRPC$TL_messages_exportChatInvite.subscription_pricing = tLRPC$TL_starsSubscriptionPricing;
                    tLRPC$TL_starsSubscriptionPricing.period = getConnectionsManager().isTestBackend() ? 300 : 2592000;
                    tLRPC$TL_messages_exportChatInvite.subscription_pricing.amount = j;
                }
                getConnectionsManager().sendRequest(tLRPC$TL_messages_exportChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda12
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LinkEditActivity.this.lambda$onCreateClicked$13(tLObject, tLRPC$TL_error);
                    }
                });
                return;
            } else if (i != 1) {
                return;
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
                    z2 = this.inviteToEdit.request_needed;
                    textCheckCell = this.approveCell;
                    if (z2 != (textCheckCell == null && textCheckCell.isChecked())) {
                        tLRPC$TL_messages_editExportedChatInvite.flags |= 8;
                        TextCheckCell textCheckCell4 = this.approveCell;
                        boolean z4 = textCheckCell4 != null && textCheckCell4.isChecked();
                        tLRPC$TL_messages_editExportedChatInvite.request_needed = z4;
                        if (z4) {
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
                        getConnectionsManager().sendRequest(tLRPC$TL_messages_editExportedChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda13
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LinkEditActivity.this.lambda$onCreateClicked$15(tLObject, tLRPC$TL_error);
                            }
                        });
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
                z2 = this.inviteToEdit.request_needed;
                textCheckCell = this.approveCell;
                if (z2 != (textCheckCell == null && textCheckCell.isChecked())) {
                }
                obj = this.nameEditText.getText().toString();
                if (!TextUtils.equals(this.inviteToEdit.title, obj)) {
                }
                if (!z) {
                }
            }
        }
        j = 0;
        i = this.type;
        boolean z32 = true;
        if (i != 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$13(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$onCreateClicked$12(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$12(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$15(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$onCreateClicked$14(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$14(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
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

    /* JADX INFO: Access modifiers changed from: private */
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
                strArr[i4] = LocaleController.getString("NoLimit", R.string.NoLimit);
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
                strArr[i4] = LocaleController.getString("NoLimit", R.string.NoLimit);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[0]) {
                strArr[i4] = LocaleController.formatPluralString("Hours", 1, new Object[0]);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[1]) {
                strArr[i4] = LocaleController.formatPluralString("Days", 1, new Object[0]);
            } else if (this.dispalyedDates.get(i4).intValue() == this.defaultDates[2]) {
                strArr[i4] = LocaleController.formatPluralString("Weeks", 1, new Object[0]);
            } else {
                long j2 = currentTime;
                if (j2 < 86400) {
                    strArr[i4] = LocaleController.getString("MessageScheduleToday", R.string.MessageScheduleToday);
                } else if (j2 < 31449600) {
                    strArr[i4] = LocaleController.getInstance().getFormatterScheduleDay().format(j * 1000);
                } else {
                    strArr[i4] = LocaleController.getInstance().getFormatterYear().format(j * 1000);
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
                this.timeChooseView.setOptions(3, LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Days", 1, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.getString("NoLimit", R.string.NoLimit));
                return;
            }
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetUses() {
        this.dispalyedUses.clear();
        int i = 0;
        while (true) {
            int[] iArr = this.defaultUses;
            if (i < iArr.length) {
                this.dispalyedUses.add(Integer.valueOf(iArr[i]));
                i++;
            } else {
                this.usesChooseView.setOptions(3, "1", "10", "100", LocaleController.getString("NoLimit", R.string.NoLimit));
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
        TextCheckCell textCheckCell = this.approveCell;
        if (textCheckCell != null) {
            textCheckCell.setBackgroundColor(Theme.getColor(tLRPC$TL_chatInviteExported.request_needed ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
            this.approveCell.setChecked(tLRPC$TL_chatInviteExported.request_needed);
        }
        setUsesVisible(!tLRPC$TL_chatInviteExported.request_needed);
        if (!TextUtils.isEmpty(tLRPC$TL_chatInviteExported.title)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tLRPC$TL_chatInviteExported.title);
            Emoji.replaceEmoji((CharSequence) spannableStringBuilder, this.nameEditText.getPaint().getFontMetricsInt(), (int) this.nameEditText.getPaint().getTextSize(), false);
            this.nameEditText.setText(spannableStringBuilder);
        }
        TextCheckCell textCheckCell2 = this.subCell;
        if (textCheckCell2 != null) {
            textCheckCell2.setChecked(tLRPC$TL_chatInviteExported.subscription_pricing != null);
        }
        if (tLRPC$TL_chatInviteExported.subscription_pricing != null) {
            TextCheckCell textCheckCell3 = this.approveCell;
            if (textCheckCell3 != null) {
                textCheckCell3.setChecked(false);
                this.approveCell.setCheckBoxIcon(R.drawable.permission_locked);
            }
            TextInfoPrivacyCell textInfoPrivacyCell = this.approveHintCell;
            if (textInfoPrivacyCell != null) {
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescriptionFrozen));
            }
        }
        EditTextCell editTextCell = this.subEditPriceCell;
        if (editTextCell != null) {
            editTextCell.setVisibility(tLRPC$TL_chatInviteExported.subscription_pricing != null ? 0 : 8);
            this.subEditPriceCell.setText(Long.toString(tLRPC$TL_chatInviteExported.subscription_pricing.amount));
            this.subEditPriceCell.editText.setClickable(false);
            this.subEditPriceCell.editText.setFocusable(false);
            this.subEditPriceCell.editText.setFocusableInTouchMode(false);
            this.subEditPriceCell.editText.setLongClickable(false);
        }
    }

    private void setUsesVisible(boolean z) {
        this.usesHeaderCell.setVisibility(z ? 0 : 8);
        this.usesChooseView.setVisibility(z ? 0 : 8);
        this.usesEditText.setVisibility(z ? 0 : 8);
        this.dividerUses.setVisibility(z ? 0 : 8);
        this.divider.setBackground(Theme.getThemedDrawableByKey(getParentActivity(), z ? R.drawable.greydivider : R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void finishFragment() {
        this.scrollView.getLayoutParams().height = this.scrollView.getHeight();
        super.finishFragment();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda14
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                LinkEditActivity.this.lambda$getThemeDescriptions$16();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        int i = Theme.key_windowBackgroundWhiteBlueHeader;
        arrayList.add(new ThemeDescription(this.timeHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        HeaderCell headerCell = this.timeHeaderCell;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(headerCell, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.timeChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.timeEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.revokeLink, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayText4;
        arrayList.add(new ThemeDescription(this.divider, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.dividerUses, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.dividerName, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_addButton));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_addButtonPressed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_buttonText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_text_RedRegular));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$16() {
        TextInfoPrivacyCell textInfoPrivacyCell = this.dividerUses;
        if (textInfoPrivacyCell != null) {
            Context context = textInfoPrivacyCell.getContext();
            TextInfoPrivacyCell textInfoPrivacyCell2 = this.dividerUses;
            int i = R.drawable.greydivider_bottom;
            int i2 = Theme.key_windowBackgroundGrayShadow;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i, i2));
            this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i2));
            this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
            EditText editText = this.usesEditText;
            int i3 = Theme.key_windowBackgroundWhiteBlackText;
            editText.setTextColor(Theme.getColor(i3));
            EditText editText2 = this.usesEditText;
            int i4 = Theme.key_windowBackgroundWhiteGrayText;
            editText2.setHintTextColor(Theme.getColor(i4));
            this.timeEditText.setTextColor(Theme.getColor(i3));
            this.timeEditText.setHintTextColor(Theme.getColor(i4));
            this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            TextSettingsCell textSettingsCell = this.revokeLink;
            if (textSettingsCell != null) {
                textSettingsCell.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
            }
            this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
            this.dividerName.setBackground(Theme.getThemedDrawableByKey(context, i, i2));
            this.nameEditText.setTextColor(Theme.getColor(i3));
            this.nameEditText.setHintTextColor(Theme.getColor(i4));
        }
    }
}
