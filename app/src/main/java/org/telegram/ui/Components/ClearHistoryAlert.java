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
import com.huawei.hms.push.constant.RemoteMessageConst;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
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
    private int[] location = new int[2];
    private int newTimer;
    private int scrollOffsetY;
    private BottomSheetCell setTimerButton;
    private Drawable shadowDrawable;

    /* loaded from: classes3.dex */
    public interface ClearHistoryAlertDelegate {

        /* renamed from: org.telegram.ui.Components.ClearHistoryAlert$ClearHistoryAlertDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$onClearHistory(ClearHistoryAlertDelegate clearHistoryAlertDelegate, boolean z) {
            }
        }

        void onAutoDeleteHistory(int i, int i2);

        void onClearHistory(boolean z);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

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
            view.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), getThemedColor("featuredStickers_addButton"), getThemedColor("featuredStickers_addButtonPressed")));
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(17);
            this.textView.setTextColor(getThemedColor("featuredStickers_buttonText"));
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ClearHistoryAlert(Context context, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context, false, resourcesProvider);
        int i;
        NestedScrollView nestedScrollView;
        boolean z2;
        int i2;
        this.autoDeleteOnly = !z;
        setApplyBottomPadding(false);
        if (tLRPC$User != null) {
            TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
            if (userFull != null) {
                i = userFull.ttl_period;
            }
            i = 0;
        } else {
            TLRPC$ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(tLRPC$Chat.id);
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
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogBackground"), PorterDuff.Mode.MULTIPLY));
        NestedScrollView nestedScrollView2 = new NestedScrollView(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.1
            private boolean ignoreLayout;

            {
                ClearHistoryAlert.this = this;
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && ClearHistoryAlert.this.scrollOffsetY != 0 && motionEvent.getY() < ClearHistoryAlert.this.scrollOffsetY) {
                    ClearHistoryAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !ClearHistoryAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
                ClearHistoryAlert.this.updateLayout();
            }

            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i3, int i4) {
                int size = View.MeasureSpec.getSize(i4);
                measureChildWithMargins(ClearHistoryAlert.this.linearLayout, i3, 0, i4, 0);
                int measuredHeight = ClearHistoryAlert.this.linearLayout.getMeasuredHeight();
                int i5 = (size / 5) * 3;
                int i6 = size - i5;
                if (ClearHistoryAlert.this.autoDeleteOnly || measuredHeight - i6 < AndroidUtilities.dp(90.0f) || measuredHeight < (size / 2) + AndroidUtilities.dp(90.0f) || i6 < (measuredHeight = (measuredHeight / 2) + AndroidUtilities.dp(108.0f))) {
                    i5 = size - measuredHeight;
                }
                if (getPaddingTop() != i5) {
                    this.ignoreLayout = true;
                    setPadding(0, i5, 0, 0);
                    this.ignoreLayout = false;
                }
                super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            }

            @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
                super.onLayout(z3, i3, i4, i5, i6);
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
                int scrollY = (int) (((ClearHistoryAlert.this.scrollOffsetY - ((BottomSheet) ClearHistoryAlert.this).backgroundPaddingTop) + getScrollY()) - getTranslationY());
                ClearHistoryAlert.this.shadowDrawable.setBounds(0, scrollY, getMeasuredWidth(), ClearHistoryAlert.this.linearLayout.getMeasuredHeight() + scrollY + ((BottomSheet) ClearHistoryAlert.this).backgroundPaddingTop + AndroidUtilities.dp(19.0f));
                ClearHistoryAlert.this.shadowDrawable.draw(canvas);
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.View
            public void onScrollChanged(int i3, int i4, int i5, int i6) {
                super.onScrollChanged(i3, i4, i5, i6);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        nestedScrollView2.setFillViewport(true);
        nestedScrollView2.setWillNotDraw(false);
        nestedScrollView2.setClipToPadding(false);
        int i3 = this.backgroundPaddingLeft;
        nestedScrollView2.setPadding(i3, 0, i3, 0);
        this.containerView = nestedScrollView2;
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.ClearHistoryAlert.2
            {
                ClearHistoryAlert.this = this;
            }

            @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z3, int i4, int i5, int i6, int i7) {
                super.onLayout(z3, i4, i5, i6, i7);
                ClearHistoryAlert.this.updateLayout();
            }
        };
        this.linearLayout = linearLayout;
        linearLayout.setOrientation(1);
        nestedScrollView2.addView(this.linearLayout, LayoutHelper.createScroll(-1, -2, 80));
        setCustomView(this.linearLayout);
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (tLRPC$User == null || tLRPC$User.bot) {
            nestedScrollView = nestedScrollView2;
        } else {
            nestedScrollView = nestedScrollView2;
            if (tLRPC$User.id != clientUserId && MessagesController.getInstance(this.currentAccount).canRevokePmInbox) {
                z2 = true;
                if (tLRPC$User == null) {
                    i2 = MessagesController.getInstance(this.currentAccount).revokeTimePmLimit;
                } else {
                    i2 = MessagesController.getInstance(this.currentAccount).revokeTimeLimit;
                }
                boolean z3 = tLRPC$User == null && z2 && i2 == Integer.MAX_VALUE;
                final boolean[] zArr = {false};
                if (this.autoDeleteOnly) {
                    TextView textView = new TextView(context);
                    textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    textView.setTextSize(1, 20.0f);
                    textView.setTextColor(getThemedColor("dialogTextBlack"));
                    textView.setText(LocaleController.getString("ClearHistory", R.string.ClearHistory));
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    this.linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 51, 23, 20, 23, 0));
                    TextView textView2 = new TextView(getContext());
                    textView2.setTextColor(getThemedColor("dialogTextBlack"));
                    textView2.setTextSize(1, 16.0f);
                    textView2.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    textView2.setLinkTextColor(getThemedColor("dialogTextLink"));
                    textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    this.linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 51, 23, 16, 23, 5));
                    if (tLRPC$User != null) {
                        textView2.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", R.string.AreYouSureClearHistoryWithUser, UserObject.getUserName(tLRPC$User))));
                    } else if (!ChatObject.isChannel(tLRPC$Chat) || (tLRPC$Chat.megagroup && TextUtils.isEmpty(tLRPC$Chat.username))) {
                        textView2.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", R.string.AreYouSureClearHistoryWithChat, tLRPC$Chat.title)));
                    } else if (tLRPC$Chat.megagroup) {
                        textView2.setText(LocaleController.getString("AreYouSureClearHistoryGroup", R.string.AreYouSureClearHistoryGroup));
                    } else {
                        textView2.setText(LocaleController.getString("AreYouSureClearHistoryChannel", R.string.AreYouSureClearHistoryChannel));
                    }
                    if (z3 && !UserObject.isDeleted(tLRPC$User)) {
                        CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1, resourcesProvider);
                        this.cell = checkBoxCell;
                        checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        this.cell.setText(LocaleController.formatString("ClearHistoryOptionAlso", R.string.ClearHistoryOptionAlso, UserObject.getFirstName(tLRPC$User)), "", false, false);
                        this.cell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(5.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(16.0f), 0);
                        this.linearLayout.addView(this.cell, LayoutHelper.createLinear(-1, 48, 51, 0, 0, 0, 0));
                        this.cell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ClearHistoryAlert.lambda$new$0(zArr, view);
                            }
                        });
                    }
                    BottomSheetCell bottomSheetCell = new BottomSheetCell(context, resourcesProvider);
                    bottomSheetCell.setBackground(null);
                    bottomSheetCell.setText(LocaleController.getString("AlertClearHistory", R.string.AlertClearHistory));
                    bottomSheetCell.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ClearHistoryAlert.this.lambda$new$1(view);
                        }
                    });
                    this.linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
                    View shadowSectionCell = new ShadowSectionCell(context);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(getThemedColor("windowBackgroundGray")), Theme.getThemedDrawable(context, (int) R.drawable.greydivider, "windowBackgroundGrayShadow"));
                    combinedDrawable.setFullsize(true);
                    shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                    this.linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
                    HeaderCell headerCell = new HeaderCell(context, resourcesProvider);
                    headerCell.setText(LocaleController.getString("AutoDeleteHeader", R.string.AutoDeleteHeader));
                    this.linearLayout.addView(headerCell, LayoutHelper.createLinear(-1, -2, 1.0f, this.autoDeleteOnly ? 20.0f : 0.0f, 1.0f, 0.0f));
                } else {
                    RLottieImageView rLottieImageView = new RLottieImageView(context);
                    rLottieImageView.setAutoRepeat(false);
                    rLottieImageView.setAnimation(R.raw.utyan_private, 120, 120);
                    rLottieImageView.setPadding(0, AndroidUtilities.dp(20.0f), 0, 0);
                    rLottieImageView.playAnimation();
                    this.linearLayout.addView(rLottieImageView, LayoutHelper.createLinear(160, 160, 49, 17, 0, 17, 0));
                    TextView textView3 = new TextView(context);
                    textView3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    textView3.setTextSize(1, 24.0f);
                    textView3.setTextColor(getThemedColor("dialogTextBlack"));
                    textView3.setText(LocaleController.getString("AutoDeleteAlertTitle", R.string.AutoDeleteAlertTitle));
                    this.linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2, 49, 17, 18, 17, 0));
                    TextView textView4 = new TextView(context);
                    textView4.setTextSize(1, 14.0f);
                    textView4.setTextColor(getThemedColor("dialogTextGray3"));
                    textView4.setGravity(1);
                    if (tLRPC$User != null) {
                        textView4.setText(LocaleController.formatString("AutoDeleteAlertUserInfo", R.string.AutoDeleteAlertUserInfo, UserObject.getFirstName(tLRPC$User)));
                    } else if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                        textView4.setText(LocaleController.getString("AutoDeleteAlertChannelInfo", R.string.AutoDeleteAlertChannelInfo));
                    } else {
                        textView4.setText(LocaleController.getString("AutoDeleteAlertGroupInfo", R.string.AutoDeleteAlertGroupInfo));
                    }
                    this.linearLayout.addView(textView4, LayoutHelper.createLinear(-2, -2, 49, 30, 22, 30, 20));
                }
                SlideChooseView slideChooseView = new SlideChooseView(context, resourcesProvider);
                final NestedScrollView nestedScrollView3 = nestedScrollView;
                slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
                    {
                        ClearHistoryAlert.this = this;
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onOptionSelected(int i4) {
                        ClearHistoryAlert.this.newTimer = i4;
                        ClearHistoryAlert.this.updateTimerButton(true);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public void onTouchEnd() {
                        nestedScrollView3.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
                    }
                });
                slideChooseView.setOptions(this.currentTimer, LocaleController.getString("AutoDeleteNever", R.string.AutoDeleteNever), LocaleController.getString("AutoDelete24Hours", R.string.AutoDelete24Hours), LocaleController.getString("AutoDelete7Days", R.string.AutoDelete7Days), LocaleController.getString("AutoDelete1Month", R.string.AutoDelete1Month));
                this.linearLayout.addView(slideChooseView, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
                FrameLayout frameLayout = new FrameLayout(context);
                CombinedDrawable combinedDrawable2 = new CombinedDrawable(new ColorDrawable(getThemedColor("windowBackgroundGray")), Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                combinedDrawable2.setFullsize(true);
                frameLayout.setBackgroundDrawable(combinedDrawable2);
                this.linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
                TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context, resourcesProvider);
                textInfoPrivacyCell.setText(LocaleController.getString("AutoDeleteInfo", R.string.AutoDeleteInfo));
                frameLayout.addView(textInfoPrivacyCell);
                BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context, resourcesProvider);
                this.setTimerButton = bottomSheetCell2;
                bottomSheetCell2.setBackgroundColor(getThemedColor("dialogBackground"));
                if (!this.autoDeleteOnly) {
                    this.setTimerButton.setText(LocaleController.getString("AutoDeleteSet", R.string.AutoDeleteSet));
                } else if (z && this.currentTimer == 0) {
                    this.setTimerButton.setText(LocaleController.getString("EnableAutoDelete", R.string.EnableAutoDelete));
                } else {
                    this.setTimerButton.setText(LocaleController.getString("AutoDeleteConfirm", R.string.AutoDeleteConfirm));
                }
                this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda0
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
        if (tLRPC$User == null) {
        }
        if (tLRPC$User == null) {
        }
        final boolean[] zArr2 = {false};
        if (this.autoDeleteOnly) {
        }
        SlideChooseView slideChooseView2 = new SlideChooseView(context, resourcesProvider);
        final NestedScrollView nestedScrollView32 = nestedScrollView;
        slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.ClearHistoryAlert.3
            {
                ClearHistoryAlert.this = this;
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onOptionSelected(int i4) {
                ClearHistoryAlert.this.newTimer = i4;
                ClearHistoryAlert.this.updateTimerButton(true);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public void onTouchEnd() {
                nestedScrollView32.smoothScrollTo(0, ClearHistoryAlert.this.linearLayout.getMeasuredHeight());
            }
        });
        slideChooseView2.setOptions(this.currentTimer, LocaleController.getString("AutoDeleteNever", R.string.AutoDeleteNever), LocaleController.getString("AutoDelete24Hours", R.string.AutoDelete24Hours), LocaleController.getString("AutoDelete7Days", R.string.AutoDelete7Days), LocaleController.getString("AutoDelete1Month", R.string.AutoDelete1Month));
        this.linearLayout.addView(slideChooseView2, LayoutHelper.createLinear(-1, -2, 0.0f, 8.0f, 0.0f, 0.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        CombinedDrawable combinedDrawable22 = new CombinedDrawable(new ColorDrawable(getThemedColor("windowBackgroundGray")), Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
        combinedDrawable22.setFullsize(true);
        frameLayout2.setBackgroundDrawable(combinedDrawable22);
        this.linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context, resourcesProvider);
        textInfoPrivacyCell2.setText(LocaleController.getString("AutoDeleteInfo", R.string.AutoDeleteInfo));
        frameLayout2.addView(textInfoPrivacyCell2);
        BottomSheetCell bottomSheetCell22 = new BottomSheetCell(context, resourcesProvider);
        this.setTimerButton = bottomSheetCell22;
        bottomSheetCell22.setBackgroundColor(getThemedColor("dialogBackground"));
        if (!this.autoDeleteOnly) {
        }
        this.setTimerButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ClearHistoryAlert$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ClearHistoryAlert.this.lambda$new$2(view);
            }
        });
        frameLayout2.addView(this.setTimerButton);
        updateTimerButton(false);
    }

    public static /* synthetic */ void lambda$new$0(boolean[] zArr, View view) {
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    public /* synthetic */ void lambda$new$1(View view) {
        if (this.dismissedDelayed) {
            return;
        }
        ClearHistoryAlertDelegate clearHistoryAlertDelegate = this.delegate;
        CheckBoxCell checkBoxCell = this.cell;
        clearHistoryAlertDelegate.onClearHistory(checkBoxCell != null && checkBoxCell.isChecked());
        dismiss();
    }

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
                i = RemoteMessageConst.DEFAULT_TTL;
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

    public void updateTimerButton(boolean z) {
        if (this.currentTimer != this.newTimer || this.autoDeleteOnly) {
            this.setTimerButton.setVisibility(0);
            if (z) {
                this.setTimerButton.animate().alpha(1.0f).setDuration(180L).start();
            } else {
                this.setTimerButton.setAlpha(1.0f);
            }
        } else if (z) {
            this.setTimerButton.animate().alpha(0.0f).setDuration(180L).start();
        } else {
            this.setTimerButton.setVisibility(4);
            this.setTimerButton.setAlpha(0.0f);
        }
    }

    public void updateLayout() {
        this.linearLayout.getChildAt(0).getLocationInWindow(this.location);
        int max = Math.max(this.location[1] - AndroidUtilities.dp(this.autoDeleteOnly ? 6.0f : 19.0f), 0);
        if (this.scrollOffsetY != max) {
            this.scrollOffsetY = max;
            this.containerView.invalidate();
        }
    }

    public void setDelegate(ClearHistoryAlertDelegate clearHistoryAlertDelegate) {
        this.delegate = clearHistoryAlertDelegate;
    }
}
