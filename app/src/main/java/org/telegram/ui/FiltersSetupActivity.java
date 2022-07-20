package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$DialogFilter;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$TL_dialogFilterSuggested;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFilter;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFiltersOrder;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.ProgressButton;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class FiltersSetupActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private int createFilterRow;
    private int createSectionRow;
    private int filterHelpRow;
    private int filtersEndRow;
    private int filtersHeaderRow;
    private int filtersStartRow;
    private boolean ignoreUpdates;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerListView listView;
    private boolean orderChanged;
    private int recommendedEndRow;
    private int recommendedHeaderRow;
    private int recommendedSectionRow;
    private int recommendedStartRow;
    private int rowCount = 0;
    private boolean showAllChats;

    public static /* synthetic */ void lambda$onFragmentDestroy$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* loaded from: classes3.dex */
    public static class TextCell extends FrameLayout {
        private ImageView imageView;
        private SimpleTextView textView;

        public TextCell(Context context) {
            super(context);
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setTextSize(16);
            this.textView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText2"));
            this.textView.setTag("windowBackgroundWhiteBlueText2");
            addView(this.textView);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            addView(this.imageView);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            AndroidUtilities.dp(48.0f);
            this.textView.measure(View.MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(94.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
            this.imageView.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
            setMeasuredDimension(size, AndroidUtilities.dp(50.0f));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6 = i3 - i;
            int textHeight = ((i4 - i2) - this.textView.getTextHeight()) / 2;
            float f = 64.0f;
            if (LocaleController.isRTL) {
                int measuredWidth = getMeasuredWidth() - this.textView.getMeasuredWidth();
                if (this.imageView.getVisibility() != 0) {
                    f = 23.0f;
                }
                i5 = measuredWidth - AndroidUtilities.dp(f);
            } else {
                if (this.imageView.getVisibility() != 0) {
                    f = 23.0f;
                }
                i5 = AndroidUtilities.dp(f);
            }
            SimpleTextView simpleTextView = this.textView;
            simpleTextView.layout(i5, textHeight, simpleTextView.getMeasuredWidth() + i5, this.textView.getMeasuredHeight() + textHeight);
            int dp = !LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : (i6 - this.imageView.getMeasuredWidth()) - AndroidUtilities.dp(20.0f);
            ImageView imageView = this.imageView;
            imageView.layout(dp, 0, imageView.getMeasuredWidth() + dp, this.imageView.getMeasuredHeight());
        }

        public void setTextAndIcon(String str, Drawable drawable, boolean z) {
            this.textView.setText(str);
            this.imageView.setImageDrawable(drawable);
        }
    }

    /* loaded from: classes3.dex */
    public static class SuggestedFilterCell extends FrameLayout {
        private ProgressButton addButton;
        private boolean needDivider;
        private TLRPC$TL_dialogFilterSuggested suggestedFilter;
        private TextView textView;
        private TextView valueTextView;

        public SuggestedFilterCell(Context context) {
            super(context);
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(LocaleController.isRTL ? 5 : 3);
            addView(this.textView, LayoutHelper.createFrame(-2, -2.0f, LocaleController.isRTL ? 5 : 3, 22.0f, 10.0f, 22.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.valueTextView = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            addView(this.valueTextView, LayoutHelper.createFrame(-2, -2.0f, LocaleController.isRTL ? 5 : 3, 22.0f, 35.0f, 22.0f, 0.0f));
            ProgressButton progressButton = new ProgressButton(context);
            this.addButton = progressButton;
            progressButton.setText(LocaleController.getString("Add", 2131624242));
            this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
            this.addButton.setProgressColor(Theme.getColor("featuredStickers_buttonProgress"));
            this.addButton.setBackgroundRoundRect(Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed"));
            addView(this.addButton, LayoutHelper.createFrameRelatively(-2.0f, 28.0f, 8388661, 0.0f, 18.0f, 14.0f, 0.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(64.0f));
            measureChildWithMargins(this.addButton, i, 0, i2, 0);
            measureChildWithMargins(this.textView, i, this.addButton.getMeasuredWidth(), i2, 0);
            measureChildWithMargins(this.valueTextView, i, this.addButton.getMeasuredWidth(), i2, 0);
        }

        public void setFilter(TLRPC$TL_dialogFilterSuggested tLRPC$TL_dialogFilterSuggested, boolean z) {
            this.needDivider = z;
            this.suggestedFilter = tLRPC$TL_dialogFilterSuggested;
            setWillNotDraw(!z);
            this.textView.setText(tLRPC$TL_dialogFilterSuggested.filter.title);
            this.valueTextView.setText(tLRPC$TL_dialogFilterSuggested.description);
        }

        public TLRPC$TL_dialogFilterSuggested getSuggestedFilter() {
            return this.suggestedFilter;
        }

        public void setAddOnClickListener(View.OnClickListener onClickListener) {
            this.addButton.setOnClickListener(onClickListener);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(0.0f, getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, Theme.dividerPaint);
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            accessibilityNodeInfo.setText(this.addButton.getText());
            accessibilityNodeInfo.setClassName("android.widget.Button");
        }
    }

    /* loaded from: classes3.dex */
    public static class HintInnerCell extends FrameLayout {
        private RLottieImageView imageView;
        private TextView messageTextView;

        public HintInnerCell(Context context) {
            super(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setAnimation(2131558448, 90, 90);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.playAnimation();
            this.imageView.setImportantForAccessibility(2);
            addView(this.imageView, LayoutHelper.createFrame(90, 90.0f, 49, 0.0f, 14.0f, 0.0f, 0.0f));
            this.imageView.setOnClickListener(new FiltersSetupActivity$HintInnerCell$$ExternalSyntheticLambda0(this));
            TextView textView = new TextView(context);
            this.messageTextView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.messageTextView.setTextSize(1, 14.0f);
            this.messageTextView.setGravity(17);
            this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("CreateNewFilterInfo", 2131625292, new Object[0])));
            addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0f, 49, 40.0f, 121.0f, 40.0f, 24.0f));
        }

        public /* synthetic */ void lambda$new$0(View view) {
            if (!this.imageView.isPlaying()) {
                this.imageView.setProgress(0.0f);
                this.imageView.playAnimation();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }
    }

    /* loaded from: classes3.dex */
    public static class FilterCell extends FrameLayout {
        private MessagesController.DialogFilter currentFilter;
        private ImageView moveImageView;
        private boolean needDivider;
        private ImageView optionsImageView;
        float progressToLock;
        private SimpleTextView textView;
        private TextView valueTextView;

        public FilterCell(Context context) {
            super(context);
            setWillNotDraw(false);
            ImageView imageView = new ImageView(context);
            this.moveImageView = imageView;
            imageView.setFocusable(false);
            this.moveImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.moveImageView.setImageResource(2131165577);
            this.moveImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff.Mode.MULTIPLY));
            this.moveImageView.setContentDescription(LocaleController.getString("FilterReorder", 2131625929));
            this.moveImageView.setClickable(true);
            int i = 5;
            addView(this.moveImageView, LayoutHelper.createFrame(48, 48.0f, (LocaleController.isRTL ? 5 : 3) | 16, 6.0f, 0.0f, 6.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(16);
            this.textView.setMaxLines(1);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            Drawable drawable = ContextCompat.getDrawable(getContext(), 2131166027);
            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff.Mode.MULTIPLY));
            this.textView.setRightDrawable(drawable);
            SimpleTextView simpleTextView2 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(simpleTextView2, LayoutHelper.createFrame(-1, -2.0f, (z ? 5 : 3) | 48, z ? 80.0f : 64.0f, 14.0f, z ? 64.0f : 80.0f, 0.0f));
            TextView textView = new TextView(context);
            this.valueTextView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setPadding(0, 0, 0, 0);
            this.valueTextView.setEllipsize(TextUtils.TruncateAt.END);
            TextView textView2 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 80.0f : 64.0f, 35.0f, z2 ? 64.0f : 80.0f, 0.0f));
            this.valueTextView.setVisibility(8);
            ImageView imageView2 = new ImageView(context);
            this.optionsImageView = imageView2;
            imageView2.setFocusable(false);
            this.optionsImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.optionsImageView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
            this.optionsImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff.Mode.MULTIPLY));
            this.optionsImageView.setImageResource(2131165624);
            this.optionsImageView.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
            addView(this.optionsImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 3 : i) | 16, 6.0f, 0.0f, 6.0f, 0.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        /* JADX WARN: Removed duplicated region for block: B:54:0x00da  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x00fb  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x010f  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0115  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0145  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x014d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setFilter(MessagesController.DialogFilter dialogFilter, boolean z) {
            MessagesController.DialogFilter dialogFilter2 = this.currentFilter;
            int i = -1;
            int i2 = dialogFilter2 == null ? -1 : dialogFilter2.id;
            this.currentFilter = dialogFilter;
            if (dialogFilter != null) {
                i = dialogFilter.id;
            }
            boolean z2 = i2 != i;
            StringBuilder sb = new StringBuilder();
            if (!dialogFilter.isDefault()) {
                int i3 = dialogFilter.flags;
                int i4 = MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS;
                if ((i3 & i4) != i4) {
                    if ((i3 & MessagesController.DIALOG_FILTER_FLAG_CONTACTS) != 0) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString("FilterContacts", 2131625886));
                    }
                    if ((dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS) != 0) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString("FilterNonContacts", 2131625916));
                    }
                    if ((dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_GROUPS) != 0) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString("FilterGroups", 2131625903));
                    }
                    if ((dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_CHANNELS) != 0) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString("FilterChannels", 2131625877));
                    }
                    if ((dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_BOTS) != 0) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.getString("FilterBots", 2131625876));
                    }
                    if (dialogFilter.alwaysShow.isEmpty() || !dialogFilter.neverShow.isEmpty()) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.formatPluralString("Exception", dialogFilter.alwaysShow.size() + dialogFilter.neverShow.size(), new Object[0]));
                    }
                    if (sb.length() == 0) {
                        sb.append(LocaleController.getString("FilterNoChats", 2131625912));
                    }
                    String str = dialogFilter.name;
                    if (dialogFilter.isDefault()) {
                        str = LocaleController.getString("FilterAllChats", 2131625870);
                    }
                    if (!z2) {
                        this.progressToLock = this.currentFilter.locked ? 1.0f : 0.0f;
                    }
                    SimpleTextView simpleTextView = this.textView;
                    simpleTextView.setText(Emoji.replaceEmoji(str, simpleTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
                    this.valueTextView.setText(sb);
                    this.needDivider = z;
                    if (!dialogFilter.isDefault()) {
                        this.optionsImageView.setVisibility(8);
                    } else {
                        this.optionsImageView.setVisibility(0);
                    }
                    invalidate();
                }
            }
            sb.append(LocaleController.getString("FilterAllChats", 2131625870));
            if (dialogFilter.alwaysShow.isEmpty()) {
            }
            if (sb.length() != 0) {
            }
            sb.append(LocaleController.formatPluralString("Exception", dialogFilter.alwaysShow.size() + dialogFilter.neverShow.size(), new Object[0]));
            if (sb.length() == 0) {
            }
            String str2 = dialogFilter.name;
            if (dialogFilter.isDefault()) {
            }
            if (!z2) {
            }
            SimpleTextView simpleTextView2 = this.textView;
            simpleTextView2.setText(Emoji.replaceEmoji(str2, simpleTextView2.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
            this.valueTextView.setText(sb);
            this.needDivider = z;
            if (!dialogFilter.isDefault()) {
            }
            invalidate();
        }

        public MessagesController.DialogFilter getCurrentFilter() {
            return this.currentFilter;
        }

        public void setOnOptionsClick(View.OnClickListener onClickListener) {
            this.optionsImageView.setOnClickListener(onClickListener);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(62.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(62.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
            MessagesController.DialogFilter dialogFilter = this.currentFilter;
            if (dialogFilter != null) {
                boolean z = dialogFilter.locked;
                if (z) {
                    float f = this.progressToLock;
                    if (f != 1.0f) {
                        this.progressToLock = f + 0.10666667f;
                        invalidate();
                    }
                }
                if (!z) {
                    float f2 = this.progressToLock;
                    if (f2 != 0.0f) {
                        this.progressToLock = f2 - 0.10666667f;
                        invalidate();
                    }
                }
            }
            float clamp = Utilities.clamp(this.progressToLock, 1.0f, 0.0f);
            this.progressToLock = clamp;
            this.textView.setRightDrawableScale(clamp);
            this.textView.invalidate();
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public void setOnReorderButtonTouchListener(View.OnTouchListener onTouchListener) {
            this.moveImageView.setOnTouchListener(onTouchListener);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        updateRows(true);
        getMessagesController().loadRemoteFilters(true);
        getNotificationCenter().addObserver(this, NotificationCenter.dialogFiltersUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.suggestedFiltersLoaded);
        if (getMessagesController().suggestedFilters.isEmpty()) {
            getMessagesController().loadSuggestedFilters();
        }
        return super.onFragmentCreate();
    }

    public void updateRows(boolean z) {
        ListAdapter listAdapter;
        this.recommendedHeaderRow = -1;
        this.recommendedStartRow = -1;
        this.recommendedEndRow = -1;
        this.recommendedSectionRow = -1;
        ArrayList<TLRPC$TL_dialogFilterSuggested> arrayList = getMessagesController().suggestedFilters;
        this.rowCount = 0;
        this.rowCount = 0 + 1;
        this.filterHelpRow = 0;
        int size = getMessagesController().dialogFilters.size();
        if (!getUserConfig().isPremium()) {
            size--;
            this.showAllChats = false;
        } else {
            this.showAllChats = true;
        }
        if (!arrayList.isEmpty() && size < 10) {
            int i = this.rowCount;
            int i2 = i + 1;
            this.rowCount = i2;
            this.recommendedHeaderRow = i;
            this.recommendedStartRow = i2;
            int size2 = i2 + arrayList.size();
            this.rowCount = size2;
            this.recommendedEndRow = size2;
            this.rowCount = size2 + 1;
            this.recommendedSectionRow = size2;
        }
        if (size != 0) {
            int i3 = this.rowCount;
            int i4 = i3 + 1;
            this.rowCount = i4;
            this.filtersHeaderRow = i3;
            this.filtersStartRow = i4;
            int i5 = i4 + size;
            this.rowCount = i5;
            this.filtersEndRow = i5;
        } else {
            this.filtersHeaderRow = -1;
            this.filtersStartRow = -1;
            this.filtersEndRow = -1;
        }
        if (size < getMessagesController().dialogFiltersLimitPremium) {
            int i6 = this.rowCount;
            this.rowCount = i6 + 1;
            this.createFilterRow = i6;
        } else {
            this.createFilterRow = -1;
        }
        int i7 = this.rowCount;
        this.rowCount = i7 + 1;
        this.createSectionRow = i7;
        if (!z || (listAdapter = this.adapter) == null) {
            return;
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.dialogFiltersUpdated;
        notificationCenter.removeObserver(this, i);
        getNotificationCenter().removeObserver(this, NotificationCenter.suggestedFiltersLoaded);
        if (this.orderChanged) {
            getNotificationCenter().postNotificationName(i, new Object[0]);
            getMessagesStorage().saveDialogFiltersOrder();
            TLRPC$TL_messages_updateDialogFiltersOrder tLRPC$TL_messages_updateDialogFiltersOrder = new TLRPC$TL_messages_updateDialogFiltersOrder();
            ArrayList<MessagesController.DialogFilter> arrayList = getMessagesController().dialogFilters;
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                tLRPC$TL_messages_updateDialogFiltersOrder.order.add(Integer.valueOf(arrayList.get(i2).id));
            }
            getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFiltersOrder, FiltersSetupActivity$$ExternalSyntheticLambda0.INSTANCE);
        }
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Filters", 2131625940));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.listView = anonymousClass2;
        ((DefaultItemAnimator) anonymousClass2.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this.listView);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new FiltersSetupActivity$$ExternalSyntheticLambda1(this, context));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.FiltersSetupActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            FiltersSetupActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                FiltersSetupActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.FiltersSetupActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            FiltersSetupActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                AndroidUtilities.runOnUIThread(new FiltersSetupActivity$2$$ExternalSyntheticLambda0(this), 250L);
            }
            return super.onTouchEvent(motionEvent);
        }

        public /* synthetic */ void lambda$onTouchEvent$0() {
            FiltersSetupActivity.this.getMessagesController().lockFiltersInternal();
        }
    }

    public /* synthetic */ void lambda$createView$1(Context context, View view, int i, float f, float f2) {
        int i2 = this.filtersStartRow;
        if (i >= i2 && i < this.filtersEndRow) {
            int i3 = i - i2;
            if (!this.showAllChats) {
                i3++;
            }
            if (getMessagesController().dialogFilters.get(i3).isDefault()) {
                return;
            }
            if (getMessagesController().dialogFilters.get(i3).locked) {
                showDialog(new LimitReachedBottomSheet(this, context, 3, this.currentAccount));
            } else {
                presentFragment(new FilterCreateActivity(getMessagesController().dialogFilters.get(i3)));
            }
        } else if (i != this.createFilterRow) {
        } else {
            if ((getMessagesController().dialogFilters.size() - 1 >= getMessagesController().dialogFiltersLimitDefault && !getUserConfig().isPremium()) || getMessagesController().dialogFilters.size() >= getMessagesController().dialogFiltersLimitPremium) {
                showDialog(new LimitReachedBottomSheet(this, context, 3, this.currentAccount));
            } else {
                presentFragment(new FilterCreateActivity());
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.dialogFiltersUpdated) {
            if (this.ignoreUpdates) {
                return;
            }
            int i3 = this.rowCount;
            updateRows(false);
            if (i3 != this.rowCount) {
                this.adapter.notifyDataSetChanged();
            } else {
                this.adapter.notifyItemRangeChanged(0, i3);
            }
        } else if (i != NotificationCenter.suggestedFiltersLoaded) {
        } else {
            updateRows(true);
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            FiltersSetupActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 3 || itemViewType == 0 || itemViewType == 5 || itemViewType == 1) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return FiltersSetupActivity.this.rowCount;
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$0(FilterCell filterCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                FiltersSetupActivity.this.itemTouchHelper.startDrag(FiltersSetupActivity.this.listView.getChildViewHolder(filterCell));
                return false;
            }
            return false;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$5(View view) {
            MessagesController.DialogFilter currentFilter = ((FilterCell) view.getParent()).getCurrentFilter();
            AlertDialog.Builder builder = new AlertDialog.Builder(FiltersSetupActivity.this.getParentActivity());
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTextSize(AndroidUtilities.dp(20.0f));
            builder.setTitle(Emoji.replaceEmoji(currentFilter.name, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
            builder.setItems(new CharSequence[]{LocaleController.getString("FilterEditItem", 2131625897), LocaleController.getString("FilterDeleteItem", 2131625889)}, new int[]{2131165714, 2131165702}, new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda1(this, currentFilter));
            AlertDialog create = builder.create();
            FiltersSetupActivity.this.showDialog(create);
            create.setItemColor(1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
        }

        public /* synthetic */ void lambda$onCreateViewHolder$4(MessagesController.DialogFilter dialogFilter, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                if (dialogFilter.locked) {
                    FiltersSetupActivity filtersSetupActivity = FiltersSetupActivity.this;
                    FiltersSetupActivity filtersSetupActivity2 = FiltersSetupActivity.this;
                    filtersSetupActivity.showDialog(new LimitReachedBottomSheet(filtersSetupActivity2, this.mContext, 3, ((BaseFragment) filtersSetupActivity2).currentAccount));
                    return;
                }
                FiltersSetupActivity.this.presentFragment(new FilterCreateActivity(dialogFilter));
            } else if (i != 1) {
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersSetupActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("FilterDelete", 2131625887));
                builder.setMessage(LocaleController.getString("FilterDeleteAlert", 2131625888));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda0(this, dialogFilter));
                AlertDialog create = builder.create();
                FiltersSetupActivity.this.showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        public /* synthetic */ void lambda$onCreateViewHolder$3(MessagesController.DialogFilter dialogFilter, DialogInterface dialogInterface, int i) {
            AlertDialog alertDialog;
            if (FiltersSetupActivity.this.getParentActivity() != null) {
                alertDialog = new AlertDialog(FiltersSetupActivity.this.getParentActivity(), 3);
                alertDialog.setCanCancel(false);
                alertDialog.show();
            } else {
                alertDialog = null;
            }
            TLRPC$TL_messages_updateDialogFilter tLRPC$TL_messages_updateDialogFilter = new TLRPC$TL_messages_updateDialogFilter();
            tLRPC$TL_messages_updateDialogFilter.id = dialogFilter.id;
            FiltersSetupActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFilter, new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda7(this, alertDialog, dialogFilter));
        }

        public /* synthetic */ void lambda$onCreateViewHolder$2(AlertDialog alertDialog, MessagesController.DialogFilter dialogFilter, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda6(this, alertDialog, dialogFilter));
        }

        public /* synthetic */ void lambda$onCreateViewHolder$1(AlertDialog alertDialog, MessagesController.DialogFilter dialogFilter) {
            if (alertDialog != null) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            int indexOf = FiltersSetupActivity.this.getMessagesController().dialogFilters.indexOf(dialogFilter);
            if (indexOf >= 0) {
                indexOf += FiltersSetupActivity.this.filtersStartRow;
            }
            if (!FiltersSetupActivity.this.showAllChats) {
                indexOf--;
            }
            FiltersSetupActivity.this.ignoreUpdates = true;
            FiltersSetupActivity.this.getMessagesController().removeFilter(dialogFilter);
            FiltersSetupActivity.this.getMessagesStorage().deleteDialogFilter(dialogFilter);
            boolean z = false;
            FiltersSetupActivity.this.ignoreUpdates = false;
            int i = FiltersSetupActivity.this.createFilterRow;
            int i2 = FiltersSetupActivity.this.recommendedHeaderRow;
            FiltersSetupActivity filtersSetupActivity = FiltersSetupActivity.this;
            if (indexOf == -1) {
                z = true;
            }
            filtersSetupActivity.updateRows(z);
            if (indexOf != -1) {
                if (FiltersSetupActivity.this.filtersStartRow == -1) {
                    FiltersSetupActivity.this.adapter.notifyItemRangeRemoved(indexOf - 1, 2);
                } else {
                    FiltersSetupActivity.this.adapter.notifyItemRemoved(indexOf);
                }
                if (i2 == -1 && FiltersSetupActivity.this.recommendedHeaderRow != -1) {
                    FiltersSetupActivity.this.adapter.notifyItemRangeInserted(i2, (FiltersSetupActivity.this.recommendedSectionRow - FiltersSetupActivity.this.recommendedHeaderRow) + 1);
                }
                if (i != -1 || FiltersSetupActivity.this.createFilterRow == -1) {
                    return;
                }
                FiltersSetupActivity.this.adapter.notifyItemInserted(FiltersSetupActivity.this.createFilterRow);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SuggestedFilterCell suggestedFilterCell;
            if (i == 0) {
                HeaderCell headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                suggestedFilterCell = headerCell;
            } else if (i == 1) {
                HintInnerCell hintInnerCell = new HintInnerCell(this.mContext);
                hintInnerCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165437, "windowBackgroundGrayShadow"));
                suggestedFilterCell = hintInnerCell;
            } else if (i == 2) {
                FilterCell filterCell = new FilterCell(this.mContext);
                filterCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                filterCell.setOnReorderButtonTouchListener(new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda4(this, filterCell));
                filterCell.setOnOptionsClick(new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda2(this));
                suggestedFilterCell = filterCell;
            } else if (i == 3) {
                suggestedFilterCell = new ShadowSectionCell(this.mContext);
            } else if (i == 4) {
                TextCell textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                suggestedFilterCell = textCell;
            } else {
                SuggestedFilterCell suggestedFilterCell2 = new SuggestedFilterCell(this.mContext);
                suggestedFilterCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                suggestedFilterCell2.setAddOnClickListener(new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda3(this, suggestedFilterCell2));
                suggestedFilterCell = suggestedFilterCell2;
            }
            return new RecyclerListView.Holder(suggestedFilterCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$7(SuggestedFilterCell suggestedFilterCell, View view) {
            TLRPC$TL_dialogFilterSuggested suggestedFilter = suggestedFilterCell.getSuggestedFilter();
            MessagesController.DialogFilter dialogFilter = new MessagesController.DialogFilter();
            dialogFilter.name = suggestedFilter.filter.title;
            dialogFilter.id = 2;
            while (FiltersSetupActivity.this.getMessagesController().dialogFiltersById.get(dialogFilter.id) != null) {
                dialogFilter.id++;
            }
            dialogFilter.unreadCount = -1;
            dialogFilter.pendingUnreadCount = -1;
            int i = 0;
            while (i < 2) {
                TLRPC$DialogFilter tLRPC$DialogFilter = suggestedFilter.filter;
                ArrayList<TLRPC$InputPeer> arrayList = i == 0 ? tLRPC$DialogFilter.include_peers : tLRPC$DialogFilter.exclude_peers;
                ArrayList<Long> arrayList2 = i == 0 ? dialogFilter.alwaysShow : dialogFilter.neverShow;
                int size = arrayList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    TLRPC$InputPeer tLRPC$InputPeer = arrayList.get(i2);
                    long j = tLRPC$InputPeer.user_id;
                    if (j == 0) {
                        long j2 = tLRPC$InputPeer.chat_id;
                        j = j2 != 0 ? -j2 : -tLRPC$InputPeer.channel_id;
                    }
                    arrayList2.add(Long.valueOf(j));
                }
                i++;
            }
            TLRPC$DialogFilter tLRPC$DialogFilter2 = suggestedFilter.filter;
            if (tLRPC$DialogFilter2.groups) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_GROUPS;
            }
            if (tLRPC$DialogFilter2.bots) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_BOTS;
            }
            if (tLRPC$DialogFilter2.contacts) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
            }
            if (tLRPC$DialogFilter2.non_contacts) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
            }
            if (tLRPC$DialogFilter2.broadcasts) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
            }
            if (tLRPC$DialogFilter2.exclude_archived) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
            }
            if (tLRPC$DialogFilter2.exclude_read) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
            }
            if (tLRPC$DialogFilter2.exclude_muted) {
                dialogFilter.flags |= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
            }
            FiltersSetupActivity.this.ignoreUpdates = true;
            FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, true, true, true, true, false, FiltersSetupActivity.this, new FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda5(this, suggestedFilter));
        }

        public /* synthetic */ void lambda$onCreateViewHolder$6(TLRPC$TL_dialogFilterSuggested tLRPC$TL_dialogFilterSuggested) {
            FiltersSetupActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated, new Object[0]);
            FiltersSetupActivity.this.ignoreUpdates = false;
            ArrayList<TLRPC$TL_dialogFilterSuggested> arrayList = FiltersSetupActivity.this.getMessagesController().suggestedFilters;
            int indexOf = arrayList.indexOf(tLRPC$TL_dialogFilterSuggested);
            if (indexOf != -1) {
                boolean z = FiltersSetupActivity.this.filtersStartRow == -1;
                arrayList.remove(indexOf);
                int i = indexOf + FiltersSetupActivity.this.recommendedStartRow;
                int i2 = FiltersSetupActivity.this.createFilterRow;
                int i3 = FiltersSetupActivity.this.recommendedHeaderRow;
                int i4 = FiltersSetupActivity.this.recommendedSectionRow;
                FiltersSetupActivity.this.updateRows(false);
                if (i2 != -1 && FiltersSetupActivity.this.createFilterRow == -1) {
                    FiltersSetupActivity.this.adapter.notifyItemRemoved(i2);
                }
                if (i3 == -1 || FiltersSetupActivity.this.recommendedHeaderRow != -1) {
                    FiltersSetupActivity.this.adapter.notifyItemRemoved(i);
                } else {
                    FiltersSetupActivity.this.adapter.notifyItemRangeRemoved(i3, (i4 - i3) + 1);
                }
                if (z) {
                    FiltersSetupActivity.this.adapter.notifyItemInserted(FiltersSetupActivity.this.filtersHeaderRow);
                }
                FiltersSetupActivity.this.adapter.notifyItemInserted(FiltersSetupActivity.this.filtersStartRow);
                return;
            }
            FiltersSetupActivity.this.updateRows(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != FiltersSetupActivity.this.filtersHeaderRow) {
                    if (i != FiltersSetupActivity.this.recommendedHeaderRow) {
                        return;
                    }
                    headerCell.setText(LocaleController.getString("FilterRecommended", 2131625918));
                    return;
                }
                headerCell.setText(LocaleController.getString("Filters", 2131625940));
                return;
            }
            boolean z = true;
            if (itemViewType == 2) {
                FilterCell filterCell = (FilterCell) viewHolder.itemView;
                int i2 = i - FiltersSetupActivity.this.filtersStartRow;
                if (!FiltersSetupActivity.this.showAllChats) {
                    i2++;
                }
                filterCell.setFilter(FiltersSetupActivity.this.getMessagesController().dialogFilters.get(i2), true);
            } else if (itemViewType == 3) {
                if (i == FiltersSetupActivity.this.createSectionRow) {
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                } else {
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                }
            } else if (itemViewType != 4) {
                if (itemViewType != 5) {
                    return;
                }
                SuggestedFilterCell suggestedFilterCell = (SuggestedFilterCell) viewHolder.itemView;
                TLRPC$TL_dialogFilterSuggested tLRPC$TL_dialogFilterSuggested = FiltersSetupActivity.this.getMessagesController().suggestedFilters.get(i - FiltersSetupActivity.this.recommendedStartRow);
                if (FiltersSetupActivity.this.recommendedStartRow == FiltersSetupActivity.this.recommendedEndRow - 1) {
                    z = false;
                }
                suggestedFilterCell.setFilter(tLRPC$TL_dialogFilterSuggested, z);
            } else {
                TextCell textCell = (TextCell) viewHolder.itemView;
                MessagesController.getNotificationsSettings(((BaseFragment) FiltersSetupActivity.this).currentAccount);
                if (i != FiltersSetupActivity.this.createFilterRow) {
                    return;
                }
                Drawable drawable = this.mContext.getResources().getDrawable(2131166080);
                Drawable drawable2 = this.mContext.getResources().getDrawable(2131166081);
                drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("switchTrackChecked"), PorterDuff.Mode.MULTIPLY));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("checkboxCheck"), PorterDuff.Mode.MULTIPLY));
                textCell.setTextAndIcon(LocaleController.getString("CreateNewFilter", 2131625291), new CombinedDrawable(drawable, drawable2), false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == FiltersSetupActivity.this.filtersHeaderRow || i == FiltersSetupActivity.this.recommendedHeaderRow) {
                return 0;
            }
            if (i == FiltersSetupActivity.this.filterHelpRow) {
                return 1;
            }
            if (i >= FiltersSetupActivity.this.filtersStartRow && i < FiltersSetupActivity.this.filtersEndRow) {
                return 2;
            }
            if (i == FiltersSetupActivity.this.createSectionRow || i == FiltersSetupActivity.this.recommendedSectionRow) {
                return 3;
            }
            return i == FiltersSetupActivity.this.createFilterRow ? 4 : 5;
        }

        public void swapElements(int i, int i2) {
            int i3 = i - FiltersSetupActivity.this.filtersStartRow;
            int i4 = i2 - FiltersSetupActivity.this.filtersStartRow;
            int i5 = FiltersSetupActivity.this.filtersEndRow - FiltersSetupActivity.this.filtersStartRow;
            if (!FiltersSetupActivity.this.showAllChats) {
                i3++;
                i4++;
                i5++;
            }
            if (i3 < 0 || i4 < 0 || i3 >= i5 || i4 >= i5) {
                return;
            }
            ArrayList<MessagesController.DialogFilter> arrayList = FiltersSetupActivity.this.getMessagesController().dialogFilters;
            MessagesController.DialogFilter dialogFilter = arrayList.get(i3);
            MessagesController.DialogFilter dialogFilter2 = arrayList.get(i4);
            int i6 = dialogFilter.order;
            dialogFilter.order = dialogFilter2.order;
            dialogFilter2.order = i6;
            arrayList.set(i3, dialogFilter2);
            arrayList.set(i4, dialogFilter);
            FiltersSetupActivity.this.orderChanged = true;
            notifyItemMoved(i, i2);
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
            FiltersSetupActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            boolean z;
            if (!FiltersSetupActivity.this.getUserConfig().isPremium()) {
                View view = viewHolder.itemView;
                if ((view instanceof FilterCell) && ((FilterCell) view).currentFilter.isDefault()) {
                    z = false;
                    if (viewHolder.getItemViewType() == 2 || !z) {
                        return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
                    }
                    return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
                }
            }
            z = true;
            if (viewHolder.getItemViewType() == 2) {
            }
            return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            boolean z;
            if (!FiltersSetupActivity.this.getUserConfig().isPremium()) {
                View view = viewHolder2.itemView;
                if ((view instanceof FilterCell) && ((FilterCell) view).currentFilter.isDefault()) {
                    z = false;
                    if (viewHolder.getItemViewType() == viewHolder2.getItemViewType() || !z) {
                        return false;
                    }
                    FiltersSetupActivity.this.adapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
                    return true;
                }
            }
            z = true;
            if (viewHolder.getItemViewType() == viewHolder2.getItemViewType()) {
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                FiltersSetupActivity.this.listView.cancelClickRunnables(false);
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCell.class, FilterCell.class, SuggestedFilterCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FilterCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FilterCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FilterCell.class}, new String[]{"moveImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menu"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FilterCell.class}, new String[]{"optionsImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menu"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FilterCell.class}, new String[]{"optionsImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menuSelector"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        return arrayList;
    }
}
