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
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.SlideChooseView;

/* loaded from: classes3.dex */
public class ClearHistoryAlert extends BottomSheet {
    private boolean autoDeleteOnly;
    private CheckBoxCell cell;
    private int currentTimer;
    private ClearHistoryAlertDelegate delegate;
    private boolean dismissedDelayed;
    private LinearLayout linearLayout;
    private int[] location;
    private int newTimer;
    private int scrollOffsetY;
    private BottomSheetCell setTimerButton;
    private Drawable shadowDrawable;

    /* loaded from: classes3.dex */
    public static class BottomSheetCell extends FrameLayout {
        private View background;
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
            this.textView.setTypeface(AndroidUtilities.bold());
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }

        protected int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }
    }

    /* loaded from: classes3.dex */
    public interface ClearHistoryAlertDelegate {

        /* loaded from: classes3.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$onClearHistory(ClearHistoryAlertDelegate clearHistoryAlertDelegate, boolean z) {
            }
        }

        void onAutoDeleteHistory(int i, int i2);

        void onClearHistory(boolean z);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x02ab  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x040d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0419  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02ae  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ClearHistoryAlert(Context context, TLRPC.User user, TLRPC.Chat chat, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context, false, resourcesProvider);
        int i;
        int i2;
        boolean z2;
        String string;
        BottomSheetCell bottomSheetCell;
        int i3;
        String formatString;
        CharSequence string2;
        this.location = new int[2];
        this.autoDeleteOnly = !z;
        setApplyBottomPadding(false);
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (user != null) {
            TLRPC.UserFull userFull = messagesController.getUserFull(user.id);
            if (userFull != null) {
                i = userFull.ttl_period;
            }
            i = 0;
        } else {
            TLRPC.ChatFull chatFull = messagesController.getChatFull(chat.id);
            if (chatFull != null) {
                i = chatFull.ttl_period;
            }
            i = 0;
        }
        if (i == 0) {
            this.currentTimer = 0;
            this.newTimer = 0;
        } else if (i == 86400) {
            this.currentTimer = 1;
            this.newTimer = 1;
        } else if (i == 604800) {
            this.currentTimer = 2;
            this.newTimer = 2;
        } else {
            this.currentTimer = 3;
            this.newTimer = 3;
        }
        Drawable mutate = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.shadowDrawable = mutate;
        int i4 = Theme.key_dialogBackground;
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(i4), PorterDuff.Mode.MULTIPLY));
        final NestedScrollView nestedScrollView = new NestedScrollView(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.1
            private boolean ignoreLayout;

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int scrollY = (int) (((ClearHistoryAlert.this.scrollOffsetY - ((BottomSheet) ClearHistoryAlert.this).backgroundPaddingTop) + getScrollY()) - getTranslationY());
                ClearHistoryAlert.this.shadowDrawable.setBounds(0, scrollY, getMeasuredWidth(), ClearHistoryAlert.this.linearLayout.getMeasuredHeight() + scrollY + ((BottomSheet) ClearHistoryAlert.this).backgroundPaddingTop + AndroidUtilities.dp(19.0f));
                ClearHistoryAlert.this.shadowDrawable.draw(canvas);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || ClearHistoryAlert.this.scrollOffsetY == 0 || motionEvent.getY() >= ClearHistoryAlert.this.scrollOffsetY) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                ClearHistoryAlert.this.dismiss();
                return true;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z3, int i5, int i6, int i7, int i8) {
                super.onLayout(z3, i5, i6, i7, i8);
                ClearHistoryAlert.this.updateLayout();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i5, int i6) {
                int size = View.MeasureSpec.getSize(i6);
                measureChildWithMargins(ClearHistoryAlert.this.linearLayout, i5, 0, i6, 0);
                int measuredHeight = ClearHistoryAlert.this.linearLayout.getMeasuredHeight();
                int i7 = (size / 5) * 3;
                int i8 = size - i7;
                if (ClearHistoryAlert.this.autoDeleteOnly || measuredHeight - i8 < AndroidUtilities.dp(90.0f) || measuredHeight < (size / 2) + AndroidUtilities.dp(90.0f) || i8 < (measuredHeight = (measuredHeight / 2) + AndroidUtilities.dp(108.0f))) {
                    i7 = size - measuredHeight;
                }
                if (getPaddingTop() != i7) {
                    this.ignoreLayout = true;
                    setPadding(0, i7, 0, 0);
                    this.ignoreLayout = false;
                }
                super.onMeasure(i5, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public void onScrollChanged(int i5, int i6, int i7, int i8) {
                super.onScrollChanged(i5, i6, i7, i8);
                ClearHistoryAlert.this.updateLayout();
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !ClearHistoryAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }

            @Override // android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        nestedScrollView.setFillViewport(true);
        nestedScrollView.setWillNotDraw(false);
        nestedScrollView.setClipToPadding(false);
        int i5 = this.backgroundPaddingLeft;
        nestedScrollView.setPadding(i5, 0, i5, 0);
        this.containerView = nestedScrollView;
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.2
            @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z3, int i6, int i7, int i8, int i9) {
                super.onLayout(z3, i6, i7, i8, i9);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        this.linearLayout = linearLayout;
        linearLayout.setOrientation(1);
        nestedScrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -2, 80));
        setCustomView(this.linearLayout);
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (user == null || user.bot) {
            i2 = i4;
        } else {
            i2 = i4;
            if (user.id != clientUserId && MessagesController.getInstance(this.currentAccount).canRevokePmInbox) {
                z2 = true;
                MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                boolean z3 = user == null && z2 && (user == null ? messagesController2.revokeTimePmLimit : messagesController2.revokeTimeLimit) == Integer.MAX_VALUE;
                final boolean[] zArr = {false};
                if (this.autoDeleteOnly) {
                    TextView textView = new TextView(context);
                    textView.setTypeface(AndroidUtilities.bold());
                    textView.setTextSize(1, 20.0f);
                    int i6 = Theme.key_dialogTextBlack;
                    textView.setTextColor(getThemedColor(i6));
                    textView.setText(LocaleController.getString(R.string.ClearHistory));
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    this.linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 51, 23, 20, 23, 0));
                    TextView textView2 = new TextView(getContext());
                    textView2.setTextColor(getThemedColor(i6));
                    textView2.setTextSize(1, 16.0f);
                    textView2.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    textView2.setLinkTextColor(getThemedColor(Theme.key_dialogTextLink));
                    textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    this.linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 51, 23, 16, 23, 5));
                    if (user != null) {
                        formatString = LocaleController.formatString("AreYouSureClearHistoryWithUser", R.string.AreYouSureClearHistoryWithUser, UserObject.getUserName(user));
                    } else if (!ChatObject.isChannel(chat) || (chat.megagroup && !ChatObject.isPublic(chat))) {
                        formatString = LocaleController.formatString("AreYouSureClearHistoryWithChat", R.string.AreYouSureClearHistoryWithChat, chat.title);
                    } else {
                        string2 = LocaleController.getString(chat.megagroup ? R.string.AreYouSureClearHistoryGroup : R.string.AreYouSureClearHistoryChannel);
                        textView2.setText(string2);
                        if (z3 && !UserObject.isDeleted(user)) {
                            CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1, resourcesProvider);
                            this.cell = checkBoxCell;
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            this.cell.setText(LocaleController.formatString("ClearHistoryOptionAlso", R.string.ClearHistoryOptionAlso, UserObject.getFirstName(user)), "", false, false);
                            this.cell.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(5.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(16.0f), 0);
                            this.linearLayout.addView(this.cell, LayoutHelper.createLinear(-1, 48, 51, 0, 0, 0, 0));
                            this.cell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda0
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    ClearHistoryAlert.lambda$new$0(zArr, view);
                                }
                            });
                        }
                        BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context, resourcesProvider);
                        bottomSheetCell2.setBackground(null);
                        bottomSheetCell2.setText(LocaleController.getString(R.string.AlertClearHistory));
                        bottomSheetCell2.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda1
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ClearHistoryAlert.this.lambda$new$1(view);
                            }
                        });
                        this.linearLayout.addView(bottomSheetCell2, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
                        View shadowSectionCell = new ShadowSectionCell(context);
                        CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        combinedDrawable.setFullsize(true);
                        shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                        this.linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
                        HeaderCell headerCell = new HeaderCell(context, resourcesProvider);
                        headerCell.setText(LocaleController.getString(R.string.AutoDeleteHeader));
                        this.linearLayout.addView(headerCell, LayoutHelper.createLinear(-1, -2, 1.0f, !this.autoDeleteOnly ? 20.0f : 0.0f, 1.0f, 0.0f));
                    }
                    string2 = AndroidUtilities.replaceTags(formatString);
                    textView2.setText(string2);
                    if (z3) {
                        CheckBoxCell checkBoxCell2 = new CheckBoxCell(context, 1, resourcesProvider);
                        this.cell = checkBoxCell2;
                        checkBoxCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        this.cell.setText(LocaleController.formatString("ClearHistoryOptionAlso", R.string.ClearHistoryOptionAlso, UserObject.getFirstName(user)), "", false, false);
                        this.cell.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(5.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(16.0f), 0);
                        this.linearLayout.addView(this.cell, LayoutHelper.createLinear(-1, 48, 51, 0, 0, 0, 0));
                        this.cell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ClearHistoryAlert.lambda$new$0(zArr, view);
                            }
                        });
                    }
                    BottomSheetCell bottomSheetCell22 = new BottomSheetCell(context, resourcesProvider);
                    bottomSheetCell22.setBackground(null);
                    bottomSheetCell22.setText(LocaleController.getString(R.string.AlertClearHistory));
                    bottomSheetCell22.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ClearHistoryAlert.this.lambda$new$1(view);
                        }
                    });
                    this.linearLayout.addView(bottomSheetCell22, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
                    View shadowSectionCell2 = new ShadowSectionCell(context);
                    CombinedDrawable combinedDrawable2 = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable2.setFullsize(true);
                    shadowSectionCell2.setBackgroundDrawable(combinedDrawable2);
                    this.linearLayout.addView(shadowSectionCell2, LayoutHelper.createLinear(-1, -2));
                    HeaderCell headerCell2 = new HeaderCell(context, resourcesProvider);
                    headerCell2.setText(LocaleController.getString(R.string.AutoDeleteHeader));
                    this.linearLayout.addView(headerCell2, LayoutHelper.createLinear(-1, -2, 1.0f, !this.autoDeleteOnly ? 20.0f : 0.0f, 1.0f, 0.0f));
                } else {
                    RLottieImageView rLottieImageView = new RLottieImageView(context);
                    rLottieImageView.setAutoRepeat(false);
                    rLottieImageView.setAnimation(R.raw.utyan_private, 120, 120);
                    rLottieImageView.setPadding(0, AndroidUtilities.dp(20.0f), 0, 0);
                    rLottieImageView.playAnimation();
                    this.linearLayout.addView(rLottieImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 49, 17, 0, 17, 0));
                    TextView textView3 = new TextView(context);
                    textView3.setTypeface(AndroidUtilities.bold());
                    textView3.setTextSize(1, 24.0f);
                    textView3.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                    textView3.setText(LocaleController.getString(R.string.AutoDeleteAlertTitle));
                    this.linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2, 49, 17, 18, 17, 0));
                    TextView textView4 = new TextView(context);
                    textView4.setTextSize(1, 14.0f);
                    textView4.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
                    textView4.setGravity(1);
                    if (user != null) {
                        string = LocaleController.formatString("AutoDeleteAlertUserInfo", R.string.AutoDeleteAlertUserInfo, UserObject.getFirstName(user));
                    } else {
                        string = LocaleController.getString((!ChatObject.isChannel(chat) || chat.megagroup) ? R.string.AutoDeleteAlertGroupInfo : R.string.AutoDeleteAlertChannelInfo);
                    }
                    textView4.setText(string);
                    this.linearLayout.addView(textView4, LayoutHelper.createLinear(-2, -2, 49, 30, 22, 30, 20));
                }
                SlideChooseView slideChooseView = new SlideChooseView(context, resourcesProvider);
                slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onOptionSelected(int i7) {
                        ClearHistoryAlert.this.newTimer = i7;
                        ClearHistoryAlert.this.updateTimerButton(true);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onTouchEnd() {
                        nestedScrollView.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
                    }
                });
                slideChooseView.setOptions(this.currentTimer, LocaleController.getString(R.string.AutoDeleteNever), LocaleController.getString(R.string.AutoDelete24Hours), LocaleController.getString(R.string.AutoDelete7Days), LocaleController.getString(R.string.AutoDelete1Month));
                this.linearLayout.addView(slideChooseView, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
                FrameLayout frameLayout = new FrameLayout(context);
                CombinedDrawable combinedDrawable3 = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                combinedDrawable3.setFullsize(true);
                frameLayout.setBackgroundDrawable(combinedDrawable3);
                this.linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
                TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context, resourcesProvider);
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.AutoDeleteInfo));
                frameLayout.addView(textInfoPrivacyCell);
                BottomSheetCell bottomSheetCell3 = new BottomSheetCell(context, resourcesProvider);
                this.setTimerButton = bottomSheetCell3;
                bottomSheetCell3.setBackgroundColor(getThemedColor(i2));
                if (!this.autoDeleteOnly) {
                    bottomSheetCell = this.setTimerButton;
                    i3 = R.string.AutoDeleteSet;
                } else if (z && this.currentTimer == 0) {
                    bottomSheetCell = this.setTimerButton;
                    i3 = R.string.EnableAutoDelete;
                } else {
                    bottomSheetCell = this.setTimerButton;
                    i3 = R.string.AutoDeleteConfirm;
                }
                bottomSheetCell.setText(LocaleController.getString(i3));
                this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ClearHistoryAlert.this.lambda$new$2(view);
                    }
                });
                frameLayout.addView(this.setTimerButton);
                updateTimerButton(false);
            }
        }
        z2 = false;
        MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
        if (user == null) {
        }
        final boolean[] zArr2 = {false};
        if (this.autoDeleteOnly) {
        }
        SlideChooseView slideChooseView2 = new SlideChooseView(context, resourcesProvider);
        slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onOptionSelected(int i7) {
                ClearHistoryAlert.this.newTimer = i7;
                ClearHistoryAlert.this.updateTimerButton(true);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onTouchEnd() {
                nestedScrollView.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
            }
        });
        slideChooseView2.setOptions(this.currentTimer, LocaleController.getString(R.string.AutoDeleteNever), LocaleController.getString(R.string.AutoDelete24Hours), LocaleController.getString(R.string.AutoDelete7Days), LocaleController.getString(R.string.AutoDelete1Month));
        this.linearLayout.addView(slideChooseView2, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        CombinedDrawable combinedDrawable32 = new CombinedDrawable(new ColorDrawable(getThemedColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        combinedDrawable32.setFullsize(true);
        frameLayout2.setBackgroundDrawable(combinedDrawable32);
        this.linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context, resourcesProvider);
        textInfoPrivacyCell2.setText(LocaleController.getString(R.string.AutoDeleteInfo));
        frameLayout2.addView(textInfoPrivacyCell2);
        BottomSheetCell bottomSheetCell32 = new BottomSheetCell(context, resourcesProvider);
        this.setTimerButton = bottomSheetCell32;
        bottomSheetCell32.setBackgroundColor(getThemedColor(i2));
        if (!this.autoDeleteOnly) {
        }
        bottomSheetCell.setText(LocaleController.getString(i3));
        this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ClearHistoryAlert.this.lambda$new$2(view);
            }
        });
        frameLayout2.addView(this.setTimerButton);
        updateTimerButton(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        if (this.dismissedDelayed) {
            return;
        }
        ClearHistoryAlertDelegate clearHistoryAlertDelegate = this.delegate;
        CheckBoxCell checkBoxCell = this.cell;
        clearHistoryAlertDelegate.onClearHistory(checkBoxCell != null && checkBoxCell.isChecked());
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        int i;
        if (this.dismissedDelayed) {
            return;
        }
        int i2 = this.newTimer;
        if (i2 != this.currentTimer) {
            this.dismissedDelayed = true;
            int i3 = 70;
            if (i2 == 3) {
                i = 2678400;
            } else if (i2 == 2) {
                i = 604800;
            } else if (i2 == 1) {
                i = 86400;
            } else {
                i = 0;
                i3 = 71;
            }
            this.delegate.onAutoDeleteHistory(i, i3);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout() {
        this.linearLayout.getChildAt(0).getLocationInWindow(this.location);
        int max = Math.max(this.location[1] - AndroidUtilities.dp(this.autoDeleteOnly ? 6.0f : 19.0f), 0);
        if (this.scrollOffsetY != max) {
            this.scrollOffsetY = max;
            this.containerView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0035, code lost:
    
        if (r5 != false) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateTimerButton(boolean z) {
        float f;
        if (this.currentTimer == this.newTimer && !this.autoDeleteOnly) {
            f = 0.0f;
            if (!z) {
                this.setTimerButton.setVisibility(4);
                this.setTimerButton.setAlpha(f);
                return;
            }
            this.setTimerButton.animate().alpha(f).setDuration(180L).start();
            return;
        }
        this.setTimerButton.setVisibility(0);
        f = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean canDismissWithSwipe() {
        return false;
    }

    public void setDelegate(ClearHistoryAlertDelegate clearHistoryAlertDelegate) {
        this.delegate = clearHistoryAlertDelegate;
    }
}
