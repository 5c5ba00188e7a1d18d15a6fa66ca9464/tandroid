package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import com.google.android.exoplayer2.C;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.SlideChooseView;
/* loaded from: classes5.dex */
public class ClearHistoryAlert extends BottomSheet {
    private boolean autoDeleteOnly;
    private CheckBoxCell cell;
    private int currentTimer;
    private ClearHistoryAlertDelegate delegate;
    private boolean dismissedDelayed;
    private LinearLayout linearLayout;
    private int[] location = new int[2];
    private int newTimer;
    private int scrollOffsetY;
    private BottomSheetCell setTimerButton;
    private Drawable shadowDrawable;

    /* loaded from: classes5.dex */
    public interface ClearHistoryAlertDelegate {
        void onAutoDeleteHistory(int i, int i2);

        void onClearHistory(boolean z);

        /* renamed from: org.telegram.ui.Components.ClearHistoryAlert$ClearHistoryAlertDelegate$-CC */
        /* loaded from: classes5.dex */
        public final /* synthetic */ class CC {
            public static void $default$onClearHistory(ClearHistoryAlertDelegate _this, boolean revoke) {
            }

            public static void $default$onAutoDeleteHistory(ClearHistoryAlertDelegate _this, int ttl, int action) {
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class BottomSheetCell extends FrameLayout {
        private View background;
        private LinearLayout linearLayout;
        private final Theme.ResourcesProvider resourcesProvider;
        private TextView textView;

        public BottomSheetCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            View view = new View(context);
            this.background = view;
            view.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), getThemedColor(Theme.key_featuredStickers_addButton), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(17);
            this.textView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), C.BUFFER_FLAG_ENCRYPTED));
        }

        public void setText(CharSequence text) {
            this.textView.setText(text);
        }

        private int getThemedColor(String key) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(key) : null;
            return color != null ? color.intValue() : Theme.getColor(key);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x04bd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ClearHistoryAlert(Context context, TLRPC.User user, TLRPC.Chat chat, boolean full, Theme.ResourcesProvider resourcesProvider) {
        super(context, false, resourcesProvider);
        int ttl;
        int ttl2;
        boolean canRevokeInbox;
        int revokeTimeLimit;
        this.autoDeleteOnly = !full;
        setApplyBottomPadding(false);
        if (user != null) {
            TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
            ttl = userFull != null ? userFull.ttl_period : 0;
        } else {
            TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(chat.id);
            ttl = chatFull != null ? chatFull.ttl_period : 0;
        }
        if (ttl == 0) {
            this.currentTimer = 0;
            this.newTimer = 0;
        } else if (ttl == 86400) {
            this.currentTimer = 1;
            this.newTimer = 1;
        } else if (ttl == 604800) {
            this.currentTimer = 2;
            this.newTimer = 2;
        } else {
            this.currentTimer = 3;
            this.newTimer = 3;
        }
        Drawable mutate = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.shadowDrawable = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogBackground), PorterDuff.Mode.MULTIPLY));
        final NestedScrollView scrollView = new NestedScrollView(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.1
            private boolean ignoreLayout;

            {
                ClearHistoryAlert.this = this;
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (ev.getAction() == 0 && ClearHistoryAlert.this.scrollOffsetY != 0 && ev.getY() < ClearHistoryAlert.this.scrollOffsetY) {
                    ClearHistoryAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(ev);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public boolean onTouchEvent(MotionEvent e) {
                return !ClearHistoryAlert.this.isDismissed() && super.onTouchEvent(e);
            }

            @Override // android.view.View
            public void setTranslationY(float translationY) {
                super.setTranslationY(translationY);
                ClearHistoryAlert.this.updateLayout();
            }

            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.View
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int height = View.MeasureSpec.getSize(heightMeasureSpec);
                measureChildWithMargins(ClearHistoryAlert.this.linearLayout, widthMeasureSpec, 0, heightMeasureSpec, 0);
                int contentHeight = ClearHistoryAlert.this.linearLayout.getMeasuredHeight();
                int padding = (height / 5) * 3;
                int visiblePart = height - padding;
                if (ClearHistoryAlert.this.autoDeleteOnly || contentHeight - visiblePart < AndroidUtilities.dp(90.0f) || contentHeight < (height / 2) + AndroidUtilities.dp(90.0f)) {
                    padding = height - contentHeight;
                } else {
                    int minHeight = (contentHeight / 2) + AndroidUtilities.dp(108.0f);
                    if (visiblePart < minHeight) {
                        padding = height - minHeight;
                    }
                }
                if (getPaddingTop() != padding) {
                    this.ignoreLayout = true;
                    setPadding(0, padding, 0, 0);
                    this.ignoreLayout = false;
                }
                super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
            }

            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                ClearHistoryAlert.this.updateLayout();
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int top = (int) (((ClearHistoryAlert.this.scrollOffsetY - ClearHistoryAlert.this.backgroundPaddingTop) + getScrollY()) - getTranslationY());
                ClearHistoryAlert.this.shadowDrawable.setBounds(0, top, getMeasuredWidth(), ClearHistoryAlert.this.linearLayout.getMeasuredHeight() + top + ClearHistoryAlert.this.backgroundPaddingTop + AndroidUtilities.dp(19.0f));
                ClearHistoryAlert.this.shadowDrawable.draw(canvas);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                super.onScrollChanged(l, t, oldl, oldt);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        scrollView.setFillViewport(true);
        scrollView.setWillNotDraw(false);
        scrollView.setClipToPadding(false);
        scrollView.setPadding(this.backgroundPaddingLeft, 0, this.backgroundPaddingLeft, 0);
        this.containerView = scrollView;
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.2
            {
                ClearHistoryAlert.this = this;
            }

            @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        this.linearLayout = linearLayout;
        linearLayout.setOrientation(1);
        scrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -2, 80));
        setCustomView(this.linearLayout);
        long selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (user == null || user.bot) {
            ttl2 = ttl;
        } else {
            ttl2 = ttl;
            if (user.id != selfUserId && MessagesController.getInstance(this.currentAccount).canRevokePmInbox) {
                canRevokeInbox = true;
                if (user == null) {
                    revokeTimeLimit = MessagesController.getInstance(this.currentAccount).revokeTimePmLimit;
                } else {
                    int revokeTimeLimit2 = this.currentAccount;
                    revokeTimeLimit = MessagesController.getInstance(revokeTimeLimit2).revokeTimeLimit;
                }
                boolean canDeleteInbox = user == null && canRevokeInbox && revokeTimeLimit == Integer.MAX_VALUE;
                final boolean[] deleteForAll = {false};
                if (this.autoDeleteOnly) {
                    TextView textView = new TextView(context);
                    textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    textView.setTextSize(1, 20.0f);
                    textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                    textView.setText(LocaleController.getString("ClearHistory", R.string.ClearHistory));
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    this.linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 51, 23, 20, 23, 0));
                    TextView messageTextView = new TextView(getContext());
                    messageTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                    messageTextView.setTextSize(1, 16.0f);
                    messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    messageTextView.setLinkTextColor(getThemedColor(Theme.key_dialogTextLink));
                    messageTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    this.linearLayout.addView(messageTextView, LayoutHelper.createLinear(-2, -2, 51, 23, 16, 23, 5));
                    if (user != null) {
                        messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", R.string.AreYouSureClearHistoryWithUser, UserObject.getUserName(user))));
                    } else if (!ChatObject.isChannel(chat) || (chat.megagroup && TextUtils.isEmpty(chat.username))) {
                        messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", R.string.AreYouSureClearHistoryWithChat, chat.title)));
                    } else if (chat.megagroup) {
                        messageTextView.setText(LocaleController.getString("AreYouSureClearHistoryGroup", R.string.AreYouSureClearHistoryGroup));
                    } else {
                        messageTextView.setText(LocaleController.getString("AreYouSureClearHistoryChannel", R.string.AreYouSureClearHistoryChannel));
                    }
                    if (canDeleteInbox && !UserObject.isDeleted(user)) {
                        CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1, resourcesProvider);
                        this.cell = checkBoxCell;
                        checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        this.cell.setText(LocaleController.formatString("ClearHistoryOptionAlso", R.string.ClearHistoryOptionAlso, UserObject.getFirstName(user)), "", false, false);
                        this.cell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(5.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(16.0f), 0);
                        this.linearLayout.addView(this.cell, LayoutHelper.createLinear(-1, 48, 51, 0, 0, 0, 0));
                        this.cell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ClearHistoryAlert.lambda$new$0(deleteForAll, view);
                            }
                        });
                    }
                    BottomSheetCell clearButton = new BottomSheetCell(context, resourcesProvider);
                    clearButton.setBackground(null);
                    clearButton.setText(LocaleController.getString("AlertClearHistory", R.string.AlertClearHistory));
                    clearButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ClearHistoryAlert.this.m2535lambda$new$1$orgtelegramuiComponentsClearHistoryAlert(view);
                        }
                    });
                    this.linearLayout.addView(clearButton, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
                    ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
                    Drawable drawable = Theme.getThemedDrawable(context, (int) R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), drawable);
                    combinedDrawable.setFullsize(true);
                    shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                    this.linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
                    HeaderCell headerCell = new HeaderCell(context, resourcesProvider);
                    headerCell.setText(LocaleController.getString("AutoDeleteHeader", R.string.AutoDeleteHeader));
                    this.linearLayout.addView(headerCell, LayoutHelper.createLinear(-1, -2, 1.0f, this.autoDeleteOnly ? 20.0f : 0.0f, 1.0f, 0.0f));
                } else {
                    RLottieImageView lottieImageView = new RLottieImageView(context);
                    lottieImageView.setAutoRepeat(false);
                    lottieImageView.setAnimation(R.raw.utyan_private, 120, 120);
                    lottieImageView.setPadding(0, AndroidUtilities.dp(20.0f), 0, 0);
                    lottieImageView.playAnimation();
                    this.linearLayout.addView(lottieImageView, LayoutHelper.createLinear(160, 160, 49, 17, 0, 17, 0));
                    TextView percentTextView = new TextView(context);
                    percentTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    percentTextView.setTextSize(1, 24.0f);
                    percentTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                    percentTextView.setText(LocaleController.getString("AutoDeleteAlertTitle", R.string.AutoDeleteAlertTitle));
                    this.linearLayout.addView(percentTextView, LayoutHelper.createLinear(-2, -2, 49, 17, 18, 17, 0));
                    TextView infoTextView = new TextView(context);
                    infoTextView.setTextSize(1, 14.0f);
                    infoTextView.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
                    infoTextView.setGravity(1);
                    if (user != null) {
                        infoTextView.setText(LocaleController.formatString("AutoDeleteAlertUserInfo", R.string.AutoDeleteAlertUserInfo, UserObject.getFirstName(user)));
                    } else if (ChatObject.isChannel(chat) && !chat.megagroup) {
                        infoTextView.setText(LocaleController.getString("AutoDeleteAlertChannelInfo", R.string.AutoDeleteAlertChannelInfo));
                    } else {
                        infoTextView.setText(LocaleController.getString("AutoDeleteAlertGroupInfo", R.string.AutoDeleteAlertGroupInfo));
                    }
                    this.linearLayout.addView(infoTextView, LayoutHelper.createLinear(-2, -2, 49, 30, 22, 30, 20));
                }
                SlideChooseView slideChooseView = new SlideChooseView(context, resourcesProvider);
                slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
                    {
                        ClearHistoryAlert.this = this;
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onOptionSelected(int index) {
                        ClearHistoryAlert.this.newTimer = index;
                        ClearHistoryAlert.this.updateTimerButton(true);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onTouchEnd() {
                        scrollView.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
                    }
                });
                String[] strings = {LocaleController.getString("AutoDeleteNever", R.string.AutoDeleteNever), LocaleController.getString("AutoDelete24Hours", R.string.AutoDelete24Hours), LocaleController.getString("AutoDelete7Days", R.string.AutoDelete7Days), LocaleController.getString("AutoDelete1Month", R.string.AutoDelete1Month)};
                slideChooseView.setOptions(this.currentTimer, strings);
                this.linearLayout.addView(slideChooseView, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
                FrameLayout buttonContainer = new FrameLayout(context);
                Drawable drawable2 = Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
                CombinedDrawable combinedDrawable2 = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), drawable2);
                combinedDrawable2.setFullsize(true);
                buttonContainer.setBackgroundDrawable(combinedDrawable2);
                this.linearLayout.addView(buttonContainer, LayoutHelper.createLinear(-1, -2));
                TextInfoPrivacyCell infoCell = new TextInfoPrivacyCell(context, resourcesProvider);
                infoCell.setText(LocaleController.getString("AutoDeleteInfo", R.string.AutoDeleteInfo));
                buttonContainer.addView(infoCell);
                BottomSheetCell bottomSheetCell = new BottomSheetCell(context, resourcesProvider);
                this.setTimerButton = bottomSheetCell;
                bottomSheetCell.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
                if (!this.autoDeleteOnly) {
                    this.setTimerButton.setText(LocaleController.getString("AutoDeleteSet", R.string.AutoDeleteSet));
                } else if (full && this.currentTimer == 0) {
                    this.setTimerButton.setText(LocaleController.getString("EnableAutoDelete", R.string.EnableAutoDelete));
                } else {
                    this.setTimerButton.setText(LocaleController.getString("AutoDeleteConfirm", R.string.AutoDeleteConfirm));
                }
                this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ClearHistoryAlert.this.m2536lambda$new$2$orgtelegramuiComponentsClearHistoryAlert(view);
                    }
                });
                buttonContainer.addView(this.setTimerButton);
                updateTimerButton(false);
            }
        }
        canRevokeInbox = false;
        if (user == null) {
        }
        if (user == null) {
        }
        final boolean[] deleteForAll2 = {false};
        if (this.autoDeleteOnly) {
        }
        SlideChooseView slideChooseView2 = new SlideChooseView(context, resourcesProvider);
        slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
            {
                ClearHistoryAlert.this = this;
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onOptionSelected(int index) {
                ClearHistoryAlert.this.newTimer = index;
                ClearHistoryAlert.this.updateTimerButton(true);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onTouchEnd() {
                scrollView.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
            }
        });
        String[] strings2 = {LocaleController.getString("AutoDeleteNever", R.string.AutoDeleteNever), LocaleController.getString("AutoDelete24Hours", R.string.AutoDelete24Hours), LocaleController.getString("AutoDelete7Days", R.string.AutoDelete7Days), LocaleController.getString("AutoDelete1Month", R.string.AutoDelete1Month)};
        slideChooseView2.setOptions(this.currentTimer, strings2);
        this.linearLayout.addView(slideChooseView2, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
        FrameLayout buttonContainer2 = new FrameLayout(context);
        Drawable drawable22 = Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
        CombinedDrawable combinedDrawable22 = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), drawable22);
        combinedDrawable22.setFullsize(true);
        buttonContainer2.setBackgroundDrawable(combinedDrawable22);
        this.linearLayout.addView(buttonContainer2, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell infoCell2 = new TextInfoPrivacyCell(context, resourcesProvider);
        infoCell2.setText(LocaleController.getString("AutoDeleteInfo", R.string.AutoDeleteInfo));
        buttonContainer2.addView(infoCell2);
        BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context, resourcesProvider);
        this.setTimerButton = bottomSheetCell2;
        bottomSheetCell2.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        if (!this.autoDeleteOnly) {
        }
        this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ClearHistoryAlert.this.m2536lambda$new$2$orgtelegramuiComponentsClearHistoryAlert(view);
            }
        });
        buttonContainer2.addView(this.setTimerButton);
        updateTimerButton(false);
    }

    public static /* synthetic */ void lambda$new$0(boolean[] deleteForAll, View v) {
        CheckBoxCell cell1 = (CheckBoxCell) v;
        deleteForAll[0] = !deleteForAll[0];
        cell1.setChecked(deleteForAll[0], true);
    }

    /* renamed from: lambda$new$1$org-telegram-ui-Components-ClearHistoryAlert */
    public /* synthetic */ void m2535lambda$new$1$orgtelegramuiComponentsClearHistoryAlert(View v) {
        if (this.dismissedDelayed) {
            return;
        }
        ClearHistoryAlertDelegate clearHistoryAlertDelegate = this.delegate;
        CheckBoxCell checkBoxCell = this.cell;
        clearHistoryAlertDelegate.onClearHistory(checkBoxCell != null && checkBoxCell.isChecked());
        dismiss();
    }

    /* renamed from: lambda$new$2$org-telegram-ui-Components-ClearHistoryAlert */
    public /* synthetic */ void m2536lambda$new$2$orgtelegramuiComponentsClearHistoryAlert(View v) {
        int action;
        int time;
        if (this.dismissedDelayed) {
            return;
        }
        int time2 = this.newTimer;
        if (time2 != this.currentTimer) {
            this.dismissedDelayed = true;
            if (time2 == 3) {
                time = 2678400;
                action = 70;
            } else if (time2 == 2) {
                time = 604800;
                action = 70;
            } else if (time2 == 1) {
                time = 86400;
                action = 70;
            } else {
                time = 0;
                action = 71;
            }
            this.delegate.onAutoDeleteHistory(time, action);
        }
        if (this.dismissedDelayed) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ClearHistoryAlert.this.dismiss();
                }
            }, 200L);
        } else {
            dismiss();
        }
    }

    public void updateTimerButton(boolean animated) {
        if (this.currentTimer != this.newTimer || this.autoDeleteOnly) {
            this.setTimerButton.setVisibility(0);
            if (animated) {
                this.setTimerButton.animate().alpha(1.0f).setDuration(180L).start();
            } else {
                this.setTimerButton.setAlpha(1.0f);
            }
        } else if (animated) {
            this.setTimerButton.animate().alpha(0.0f).setDuration(180L).start();
        } else {
            this.setTimerButton.setVisibility(4);
            this.setTimerButton.setAlpha(0.0f);
        }
    }

    public void updateLayout() {
        View child = this.linearLayout.getChildAt(0);
        child.getLocationInWindow(this.location);
        int top = this.location[1] - AndroidUtilities.dp(this.autoDeleteOnly ? 6.0f : 19.0f);
        int newOffset = Math.max(top, 0);
        if (this.scrollOffsetY != newOffset) {
            this.scrollOffsetY = newOffset;
            this.containerView.invalidate();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    public void setDelegate(ClearHistoryAlertDelegate clearHistoryAlertDelegate) {
        this.delegate = clearHistoryAlertDelegate;
    }
}
