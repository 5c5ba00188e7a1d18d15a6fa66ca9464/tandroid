package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VoIPController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_stats$BroadcastRevenueTransaction;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.BusinessLinksActivity;
import org.telegram.ui.Business.QuickRepliesActivity;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.CollapseTextCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogRadioCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.SlideIntChooseView;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextRightIconCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChannelMonetizationLayout;
import org.telegram.ui.Charts.BaseChartView;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.StatisticActivity;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;
/* loaded from: classes3.dex */
public class UniversalAdapter extends AdapterWithDiffUtils {
    private boolean allowReorder;
    private BaseChartView.SharedUiComponents chartSharedUI;
    private final int classGuid;
    private final Context context;
    private final int currentAccount;
    private Section currentReorderSection;
    private Section currentWhiteSection;
    private final boolean dialog;
    protected Utilities.Callback2<ArrayList<UItem>, UniversalAdapter> fillItems;
    private final ArrayList<UItem> items;
    protected final RecyclerListView listView;
    private final ArrayList<UItem> oldItems;
    private Utilities.Callback2<Integer, ArrayList<UItem>> onReordered;
    private boolean orderChanged;
    private int orderChangedId;
    private final ArrayList<Section> reorderSections;
    private final Theme.ResourcesProvider resourcesProvider;
    private final ArrayList<Section> whiteSections;

    public UniversalAdapter(RecyclerListView recyclerListView, Context context, int i, int i2, Utilities.Callback2<ArrayList<UItem>, UniversalAdapter> callback2, Theme.ResourcesProvider resourcesProvider) {
        this(recyclerListView, context, i, i2, false, callback2, resourcesProvider);
    }

    public UniversalAdapter(RecyclerListView recyclerListView, Context context, int i, int i2, boolean z, Utilities.Callback2<ArrayList<UItem>, UniversalAdapter> callback2, Theme.ResourcesProvider resourcesProvider) {
        this.oldItems = new ArrayList<>();
        this.items = new ArrayList<>();
        this.whiteSections = new ArrayList<>();
        this.reorderSections = new ArrayList<>();
        this.listView = recyclerListView;
        this.context = context;
        this.currentAccount = i;
        this.classGuid = i2;
        this.dialog = z;
        this.fillItems = callback2;
        this.resourcesProvider = resourcesProvider;
        update(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Section {
        public int end;
        public int start;

        private Section() {
        }

        public boolean contains(int i) {
            return i >= this.start && i <= this.end;
        }
    }

    public void whiteSectionStart() {
        Section section = new Section();
        this.currentWhiteSection = section;
        section.start = this.items.size();
        Section section2 = this.currentWhiteSection;
        section2.end = -1;
        this.whiteSections.add(section2);
    }

    public void whiteSectionEnd() {
        Section section = this.currentWhiteSection;
        if (section != null) {
            section.end = Math.max(0, this.items.size() - 1);
        }
    }

    public int reorderSectionStart() {
        Section section = new Section();
        this.currentReorderSection = section;
        section.start = this.items.size();
        Section section2 = this.currentReorderSection;
        section2.end = -1;
        this.reorderSections.add(section2);
        return this.reorderSections.size() - 1;
    }

    public void reorderSectionEnd() {
        Section section = this.currentReorderSection;
        if (section != null) {
            section.end = Math.max(0, this.items.size() - 1);
        }
    }

    public boolean isReorderItem(int i) {
        return getReorderSectionId(i) >= 0;
    }

    public int getReorderSectionId(int i) {
        for (int i2 = 0; i2 < this.reorderSections.size(); i2++) {
            if (this.reorderSections.get(i2).contains(i)) {
                return i2;
            }
        }
        return -1;
    }

    public void swapElements(int i, int i2) {
        int i3;
        if (this.onReordered == null) {
            return;
        }
        int reorderSectionId = getReorderSectionId(i);
        int reorderSectionId2 = getReorderSectionId(i2);
        if (reorderSectionId < 0 || reorderSectionId != reorderSectionId2) {
            return;
        }
        boolean hasDivider = hasDivider(i);
        boolean hasDivider2 = hasDivider(i2);
        this.items.set(i, this.items.get(i2));
        this.items.set(i2, this.items.get(i));
        notifyItemMoved(i, i2);
        if (hasDivider(i2) != hasDivider) {
            notifyItemChanged(i2, 3);
        }
        if (hasDivider(i) != hasDivider2) {
            notifyItemChanged(i, 3);
        }
        if (this.orderChanged && (i3 = this.orderChangedId) != reorderSectionId) {
            callReorder(i3);
        }
        this.orderChanged = true;
        this.orderChangedId = reorderSectionId;
    }

    private void callReorder(int i) {
        if (i < 0 || i >= this.reorderSections.size()) {
            return;
        }
        Section section = this.reorderSections.get(i);
        this.onReordered.run(Integer.valueOf(i), new ArrayList<>(this.items.subList(section.start, section.end + 1)));
        this.orderChanged = false;
    }

    public void reorderDone() {
        if (this.orderChanged) {
            callReorder(this.orderChangedId);
        }
    }

    public void listenReorder(Utilities.Callback2<Integer, ArrayList<UItem>> callback2) {
        this.onReordered = callback2;
    }

    public void updateReorder(boolean z) {
        this.allowReorder = z;
    }

    public void drawWhiteSections(Canvas canvas, RecyclerListView recyclerListView) {
        for (int i = 0; i < this.whiteSections.size(); i++) {
            Section section = this.whiteSections.get(i);
            int i2 = section.end;
            if (i2 >= 0) {
                recyclerListView.drawSectionBackground(canvas, section.start, i2, getThemedColor(this.dialog ? Theme.key_dialogBackground : Theme.key_windowBackgroundWhite));
            }
        }
    }

    public void update(final boolean z) {
        this.oldItems.clear();
        this.oldItems.addAll(this.items);
        this.items.clear();
        this.whiteSections.clear();
        this.reorderSections.clear();
        Utilities.Callback2<ArrayList<UItem>, UniversalAdapter> callback2 = this.fillItems;
        if (callback2 != null) {
            callback2.run(this.items, this);
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView != null && recyclerListView.isComputingLayout()) {
                this.listView.post(new Runnable() { // from class: org.telegram.ui.Components.UniversalAdapter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        UniversalAdapter.this.lambda$update$0(z);
                    }
                });
            } else if (z) {
                setItems(this.oldItems, this.items);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0(boolean z) {
        if (z) {
            setItems(this.oldItems, this.items);
        } else {
            notifyDataSetChanged();
        }
    }

    /* JADX WARN: Type inference failed for: r15v48, types: [android.view.View] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View headerCell;
        View view;
        FlickerLoadingView flickerLoadingView;
        boolean z = this.dialog;
        int i2 = z ? Theme.key_dialogBackground : Theme.key_windowBackgroundWhite;
        if (i >= UItem.factoryViewTypeStartsWith) {
            UItem.UItemFactory<?> findFactory = UItem.findFactory(i);
            if (findFactory != null) {
                ?? createView = findFactory.createView(this.context, this.currentAccount, this.classGuid, this.resourcesProvider);
                flickerLoadingView = createView;
                if (!findFactory.isShadow()) {
                    createView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = createView;
                }
            } else {
                flickerLoadingView = new View(this.context);
            }
        } else {
            switch (i) {
                case VoIPController.ERROR_LOCALIZED /* -3 */:
                    View view2 = new FrameLayout(this, this.context) { // from class: org.telegram.ui.Components.UniversalAdapter.3
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i3, int i4) {
                            int size = View.MeasureSpec.getSize(i4);
                            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824);
                            measureChildren(makeMeasureSpec, i4);
                            int i5 = 0;
                            for (int i6 = 0; i6 < getChildCount(); i6++) {
                                i5 = Math.max(i5, getChildAt(i6).getMeasuredHeight());
                            }
                            if (size > 0) {
                                i5 = Math.min(i5, size - (AndroidUtilities.statusBarHeight + ActionBar.getCurrentActionBarHeight()));
                            }
                            super.onMeasure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i5, 1073741824));
                        }
                    };
                    view2.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                    flickerLoadingView = view2;
                    break;
                case VoIPController.ERROR_PRIVACY /* -2 */:
                    flickerLoadingView = new FrameLayout(this, this.context) { // from class: org.telegram.ui.Components.UniversalAdapter.2
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i3, int i4) {
                            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824);
                            measureChildren(makeMeasureSpec, i4);
                            int i5 = 0;
                            for (int i6 = 0; i6 < getChildCount(); i6++) {
                                i5 = Math.max(i5, getChildAt(i6).getMeasuredHeight());
                            }
                            super.onMeasure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i5, 1073741824));
                        }
                    };
                    break;
                case -1:
                    flickerLoadingView = new FrameLayout(this, this.context) { // from class: org.telegram.ui.Components.UniversalAdapter.1
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i3, int i4) {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824), i4);
                        }
                    };
                    break;
                case 0:
                    if (z) {
                        headerCell = new HeaderCell(this.context, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, 0, false, this.resourcesProvider);
                    } else {
                        headerCell = new HeaderCell(this.context, this.resourcesProvider);
                    }
                    View view3 = headerCell;
                    view3.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = view3;
                    break;
                case 1:
                    View headerCell2 = new HeaderCell(this.context, Theme.key_windowBackgroundWhiteBlackText, 17, 15, false, this.resourcesProvider);
                    headerCell2.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = headerCell2;
                    break;
                case 2:
                    flickerLoadingView = new TopViewCell(this.context, this.resourcesProvider);
                    break;
                case 3:
                    View textCell = new TextCell(this.context, this.resourcesProvider);
                    textCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = textCell;
                    break;
                case 4:
                case 9:
                    TextCheckCell textCheckCell = new TextCheckCell(this.context, this.resourcesProvider);
                    if (i == 9) {
                        textCheckCell.setDrawCheckRipple(true);
                        textCheckCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                        textCheckCell.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        textCheckCell.setHeight(56);
                    }
                    textCheckCell.setBackgroundColor(getThemedColor(i2));
                    view = textCheckCell;
                    flickerLoadingView = view;
                    break;
                case 5:
                case 6:
                    View notificationsCheckCell = new NotificationsCheckCell(this.context, 21, 60, i == 6, this.resourcesProvider);
                    notificationsCheckCell.setBackgroundColor(getThemedColor(i2));
                    view = notificationsCheckCell;
                    flickerLoadingView = view;
                    break;
                case 7:
                case 8:
                default:
                    flickerLoadingView = new TextInfoPrivacyCell(this.context, this.resourcesProvider);
                    break;
                case 10:
                    View dialogRadioCell = new DialogRadioCell(this.context);
                    dialogRadioCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = dialogRadioCell;
                    break;
                case 11:
                case 12:
                    UserCell userCell = new UserCell(this.context, 6, i == 12 ? 3 : 0, false);
                    userCell.setSelfAsSavedMessages(true);
                    userCell.setBackgroundColor(getThemedColor(i2));
                    view = userCell;
                    flickerLoadingView = view;
                    break;
                case 13:
                    UserCell userCell2 = new UserCell(this.context, 6, 0, false, true);
                    userCell2.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = userCell2;
                    break;
                case 14:
                    View slideChooseView = new SlideChooseView(this.context, this.resourcesProvider);
                    slideChooseView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = slideChooseView;
                    break;
                case 15:
                    View slideIntChooseView = new SlideIntChooseView(this.context, this.resourcesProvider);
                    slideIntChooseView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = slideIntChooseView;
                    break;
                case 16:
                    View quickReplyView = new QuickRepliesActivity.QuickReplyView(this.context, this.onReordered != null, this.resourcesProvider);
                    quickReplyView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = quickReplyView;
                    break;
                case 17:
                    View largeQuickReplyView = new QuickRepliesActivity.LargeQuickReplyView(this.context, this.resourcesProvider);
                    largeQuickReplyView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = largeQuickReplyView;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    if (this.chartSharedUI == null) {
                        this.chartSharedUI = new BaseChartView.SharedUiComponents();
                    }
                    View universalChartCell = new StatisticActivity.UniversalChartCell(this.context, this.currentAccount, i - 18, this.chartSharedUI, this.classGuid);
                    universalChartCell.setBackgroundColor(getThemedColor(i2));
                    view = universalChartCell;
                    flickerLoadingView = view;
                    break;
                case 24:
                    View proceedOverviewCell = new ChannelMonetizationLayout.ProceedOverviewCell(this.context, this.resourcesProvider);
                    proceedOverviewCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = proceedOverviewCell;
                    break;
                case 25:
                    View transactionCell = new ChannelMonetizationLayout.TransactionCell(this.context, this.resourcesProvider);
                    transactionCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = transactionCell;
                    break;
                case 26:
                    HeaderCell headerCell3 = new HeaderCell(this.context, Theme.key_windowBackgroundWhiteBlackText, 23, 20, 0, false, this.resourcesProvider);
                    headerCell3.setTextSize(20.0f);
                    flickerLoadingView = headerCell3;
                    break;
                case 27:
                    StoryPrivacyBottomSheet.UserCell userCell3 = new StoryPrivacyBottomSheet.UserCell(this.context, this.resourcesProvider);
                    userCell3.setIsSendAs(false, false);
                    userCell3.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = userCell3;
                    break;
                case 28:
                    View view4 = new View(this.context);
                    view4.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = view4;
                    break;
                case 29:
                    View businessLinkView = new BusinessLinksActivity.BusinessLinkView(this.context, this.resourcesProvider);
                    businessLinkView.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = businessLinkView;
                    break;
                case R.styleable.AppCompatTheme_actionModeTheme /* 30 */:
                    View textRightIconCell = new TextRightIconCell(this.context, this.resourcesProvider);
                    textRightIconCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = textRightIconCell;
                    break;
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                    flickerLoadingView = new GraySectionCell(this.context, this.resourcesProvider);
                    break;
                case 32:
                    View profileSearchCell = new ProfileSearchCell(this.context);
                    profileSearchCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    flickerLoadingView = profileSearchCell;
                    break;
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                    View dialogCell = new DialogCell(null, this.context, false, true);
                    dialogCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    flickerLoadingView = dialogCell;
                    break;
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                    FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(this.context, this.resourcesProvider);
                    flickerLoadingView2.setIsSingleCell(true);
                    flickerLoadingView2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    flickerLoadingView = flickerLoadingView2;
                    break;
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                    CheckBoxCell checkBoxCell = new CheckBoxCell(this.context, i == 35 ? 4 : i == 36 ? 6 : i == 37 ? 7 : i == 41 ? 8 : 0, 21, true, this.resourcesProvider);
                    checkBoxCell.getCheckBoxRound().setColor(Theme.key_switch2TrackChecked, Theme.key_radioBackground, Theme.key_checkboxCheck);
                    checkBoxCell.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = checkBoxCell;
                    break;
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                    flickerLoadingView = new CollapseTextCell(this.context, this.resourcesProvider);
                    break;
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                    View textCheckCell2 = new TextCheckCell2(this.context);
                    textCheckCell2.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = textCheckCell2;
                    break;
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                    View headerCell4 = new HeaderCell(this.context, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, 0, false, true, this.resourcesProvider);
                    headerCell4.setBackgroundColor(getThemedColor(i2));
                    flickerLoadingView = headerCell4;
                    break;
            }
        }
        return new RecyclerListView.Holder(flickerLoadingView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        UItem item = getItem(i);
        if (item == null) {
            return 0;
        }
        return item.viewType;
    }

    private boolean hasDivider(int i) {
        UItem item = getItem(i);
        UItem item2 = getItem(i + 1);
        return (item == null || item.hideDivider || item2 == null || isShadow(item2.viewType) != isShadow(item.viewType)) ? false : true;
    }

    public boolean isShadow(int i) {
        if (i < UItem.factoryViewTypeStartsWith) {
            return i == 7 || i == 8 || i == 38 || i == 31 || i == 34;
        }
        UItem.UItemFactory<?> findFactory = UItem.findFactory(i);
        return findFactory != null && findFactory.isShadow();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:66:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01b7  */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FrameLayout.LayoutParams createFrame;
        int i2;
        CharSequence charSequence;
        String formatPluralStringComma;
        final UItem item = getItem(i);
        UItem item2 = getItem(i + 1);
        UItem item3 = getItem(i - 1);
        if (item == null) {
            return;
        }
        int itemViewType = viewHolder.getItemViewType();
        boolean hasDivider = hasDivider(i);
        if (itemViewType >= UItem.factoryViewTypeStartsWith) {
            UItem.UItemFactory<?> findFactory = UItem.findFactory(itemViewType);
            if (findFactory != null) {
                findFactory.bindView(viewHolder.itemView, item, hasDivider);
                return;
            }
            return;
        }
        String str = "";
        TextInfoPrivacyCell textInfoPrivacyCell = null;
        String publicUsername = null;
        switch (itemViewType) {
            case VoIPController.ERROR_LOCALIZED /* -3 */:
            case VoIPController.ERROR_PRIVACY /* -2 */:
            case -1:
                FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
                if (frameLayout.getChildCount() == (item.view != null) && frameLayout.getChildAt(0) == item.view) {
                    return;
                }
                frameLayout.removeAllViews();
                View view = item.view;
                if (view != null) {
                    AndroidUtilities.removeFromParent(view);
                    if (itemViewType == -1 || itemViewType == -3) {
                        createFrame = LayoutHelper.createFrame(-1, -1.0f);
                    } else {
                        createFrame = LayoutHelper.createFrame(-2, -2.0f);
                    }
                    frameLayout.addView(item.view, createFrame);
                    return;
                }
                return;
            case 0:
            case 1:
            case 26:
                ((HeaderCell) viewHolder.itemView).setText(item.text);
                return;
            case 2:
                TopViewCell topViewCell = (TopViewCell) viewHolder.itemView;
                int i3 = item.iconResId;
                if (i3 != 0) {
                    topViewCell.setEmoji(i3);
                } else {
                    topViewCell.setEmoji(item.subtext.toString(), item.textValue.toString());
                }
                topViewCell.setText(item.text);
                return;
            case 3:
                TextCell textCell = (TextCell) viewHolder.itemView;
                Object obj = item.object;
                if (obj instanceof TLRPC$Document) {
                    textCell.setTextAndSticker(item.text, (TLRPC$Document) obj, hasDivider);
                } else if (obj instanceof String) {
                    textCell.setTextAndSticker(item.text, (String) obj, hasDivider);
                } else if (TextUtils.isEmpty(item.textValue)) {
                    int i4 = item.iconResId;
                    if (i4 == 0) {
                        textCell.setText(item.text, hasDivider);
                    } else {
                        textCell.setTextAndIcon(item.text, i4, hasDivider);
                    }
                } else {
                    int i5 = item.iconResId;
                    if (i5 == 0) {
                        textCell.setTextAndValue(item.text, item.textValue, hasDivider);
                    } else {
                        textCell.setTextAndValueAndIcon(item.text, item.textValue, i5, hasDivider);
                    }
                }
                if (item.accent) {
                    int i6 = Theme.key_windowBackgroundWhiteBlueText4;
                    textCell.setColors(i6, i6);
                    return;
                } else if (item.red) {
                    textCell.setColors(Theme.key_text_RedBold, Theme.key_text_RedRegular);
                    return;
                } else {
                    textCell.setColors(Theme.key_windowBackgroundWhiteGrayIcon, Theme.key_windowBackgroundWhiteBlackText);
                    return;
                }
            case 4:
            case 9:
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (textCheckCell.itemId == item.id) {
                    textCheckCell.setChecked(item.checked);
                }
                textCheckCell.setTextAndCheck(item.text, item.checked, hasDivider);
                textCheckCell.itemId = item.id;
                if (itemViewType == 9) {
                    viewHolder.itemView.setBackgroundColor(Theme.getColor(item.checked ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                    return;
                }
                return;
            case 5:
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                CharSequence charSequence2 = item.subtext;
                notificationsCheckCell.setTextAndValueAndCheck(item.text, item.subtext, item.checked, 0, (charSequence2 == null || !charSequence2.toString().contains("\n")) ? false : false, hasDivider);
                return;
            case 6:
                ((NotificationsCheckCell) viewHolder.itemView).setTextAndValueAndCheck(item.text, item.subtext, item.checked, hasDivider);
                return;
            case 7:
            case 8:
            case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                if (itemViewType == 7 || itemViewType == 8) {
                    TextInfoPrivacyCell textInfoPrivacyCell2 = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (TextUtils.isEmpty(item.text)) {
                        textInfoPrivacyCell2.setFixedSize(itemViewType == 8 ? 220 : 12);
                        textInfoPrivacyCell2.setText("");
                    } else {
                        textInfoPrivacyCell2.setFixedSize(0);
                        textInfoPrivacyCell2.setText(item.text);
                    }
                    if (item.accent) {
                        textInfoPrivacyCell2.setTextGravity(17);
                        textInfoPrivacyCell2.getTextView().setWidth(Math.min(HintView2.cutInFancyHalf(textInfoPrivacyCell2.getText(), textInfoPrivacyCell2.getTextView().getPaint()), AndroidUtilities.displaySize.x - AndroidUtilities.dp(60.0f)));
                        textInfoPrivacyCell2.getTextView().setPadding(0, AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(17.0f));
                        textInfoPrivacyCell = textInfoPrivacyCell2;
                    } else {
                        textInfoPrivacyCell2.setTextGravity(8388611);
                        textInfoPrivacyCell2.getTextView().setMinWidth(0);
                        textInfoPrivacyCell2.getTextView().setMaxWidth(AndroidUtilities.displaySize.x);
                        textInfoPrivacyCell2.getTextView().setPadding(0, AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(17.0f));
                        textInfoPrivacyCell = textInfoPrivacyCell2;
                    }
                } else if (itemViewType == 38) {
                    CollapseTextCell collapseTextCell = (CollapseTextCell) viewHolder.itemView;
                    collapseTextCell.set(item.animatedText, item.collapsed);
                    if (item.accent) {
                        collapseTextCell.setColor(Theme.key_windowBackgroundWhiteBlueText4);
                        textInfoPrivacyCell = collapseTextCell;
                    } else if (item.red) {
                        collapseTextCell.setColor(Theme.key_text_RedRegular);
                        textInfoPrivacyCell = collapseTextCell;
                    } else {
                        collapseTextCell.setColor(Theme.key_windowBackgroundWhiteBlackText);
                        textInfoPrivacyCell = collapseTextCell;
                    }
                }
                boolean z = (item3 == null || isShadow(item3.viewType)) ? false : true;
                boolean z2 = (item2 == null || isShadow(item2.viewType)) ? false : true;
                if (z && z2) {
                    i2 = R.drawable.greydivider;
                } else if (z) {
                    i2 = R.drawable.greydivider_bottom;
                } else if (z2) {
                    i2 = R.drawable.greydivider_top;
                } else {
                    i2 = R.drawable.field_carret_empty;
                }
                Drawable themedDrawableByKey = Theme.getThemedDrawableByKey(this.context, i2, Theme.key_windowBackgroundGrayShadow, this.resourcesProvider);
                if (this.dialog) {
                    textInfoPrivacyCell.setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(getThemedColor(Theme.key_dialogBackgroundGray)), themedDrawableByKey}));
                    return;
                } else {
                    textInfoPrivacyCell.setBackground(themedDrawableByKey);
                    return;
                }
            case 10:
                DialogRadioCell dialogRadioCell = (DialogRadioCell) viewHolder.itemView;
                if (dialogRadioCell.itemId == item.id) {
                    dialogRadioCell.setChecked(item.checked, true);
                    dialogRadioCell.setEnabled(item.enabled, true);
                } else {
                    dialogRadioCell.setEnabled(item.enabled, false);
                }
                if (TextUtils.isEmpty(item.textValue)) {
                    dialogRadioCell.setText(item.text, item.checked, hasDivider);
                } else {
                    dialogRadioCell.setTextAndValue(item.text, item.textValue, item.checked, hasDivider);
                }
                dialogRadioCell.itemId = item.id;
                return;
            case 11:
            case 12:
                UserCell userCell = (UserCell) viewHolder.itemView;
                userCell.setFromUItem(this.currentAccount, item, hasDivider);
                if (itemViewType == 12) {
                    userCell.setChecked(item.checked, false);
                    return;
                }
                return;
            case 13:
                UserCell userCell2 = (UserCell) viewHolder.itemView;
                userCell2.setFromUItem(this.currentAccount, item, hasDivider);
                userCell2.setAddButtonVisible(!item.checked);
                userCell2.setCloseIcon(item.clickCallback);
                return;
            case 14:
                SlideChooseView slideChooseView = (SlideChooseView) viewHolder.itemView;
                slideChooseView.setOptions(item.intValue, item.texts);
                slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.Components.UniversalAdapter$$ExternalSyntheticLambda3
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i7) {
                        UniversalAdapter.lambda$onBindViewHolder$1(UItem.this, i7);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                return;
            case 15:
                ((SlideIntChooseView) viewHolder.itemView).set(item.intValue, (SlideIntChooseView.Options) item.object, item.intCallback);
                return;
            case 16:
                QuickRepliesActivity.QuickReplyView quickReplyView = (QuickRepliesActivity.QuickReplyView) viewHolder.itemView;
                quickReplyView.setChecked(item.checked, false);
                quickReplyView.setReorder(this.allowReorder);
                Object obj2 = item.object;
                if (obj2 instanceof QuickRepliesController.QuickReply) {
                    quickReplyView.set((QuickRepliesController.QuickReply) obj2, null, hasDivider);
                    return;
                }
                return;
            case 17:
                QuickRepliesActivity.LargeQuickReplyView largeQuickReplyView = (QuickRepliesActivity.LargeQuickReplyView) viewHolder.itemView;
                largeQuickReplyView.setChecked(item.checked, false);
                Object obj3 = item.object;
                if (obj3 instanceof QuickRepliesController.QuickReply) {
                    largeQuickReplyView.set((QuickRepliesController.QuickReply) obj3, hasDivider);
                    return;
                }
                return;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                ((StatisticActivity.UniversalChartCell) viewHolder.itemView).set(item.intValue, (StatisticActivity.ChartViewData) item.object, new Utilities.Callback0Return() { // from class: org.telegram.ui.Components.UniversalAdapter$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback0Return
                    public final Object run() {
                        StatisticActivity.BaseChartCell lambda$onBindViewHolder$2;
                        lambda$onBindViewHolder$2 = UniversalAdapter.this.lambda$onBindViewHolder$2(item);
                        return lambda$onBindViewHolder$2;
                    }
                });
                return;
            case 24:
                ((ChannelMonetizationLayout.ProceedOverviewCell) viewHolder.itemView).set((ChannelMonetizationLayout.ProceedOverview) item.object);
                return;
            case 25:
                ((ChannelMonetizationLayout.TransactionCell) viewHolder.itemView).set((TL_stats$BroadcastRevenueTransaction) item.object, hasDivider);
                return;
            case 27:
                StoryPrivacyBottomSheet.UserCell userCell3 = (StoryPrivacyBottomSheet.UserCell) viewHolder.itemView;
                long j = userCell3.dialogId;
                Object obj4 = item.object;
                boolean z3 = j == (obj4 instanceof TLRPC$User ? ((TLRPC$User) obj4).id : obj4 instanceof TLRPC$Chat ? -((TLRPC$Chat) obj4).id : 0L);
                userCell3.setIsSendAs(false, true);
                userCell3.set(item.object);
                userCell3.checkBox.setVisibility(8);
                userCell3.radioButton.setVisibility(0);
                userCell3.setChecked(item.checked, z3);
                userCell3.setDivider(hasDivider);
                return;
            case 28:
                if (item.transparent) {
                    viewHolder.itemView.setBackgroundColor(0);
                }
                viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, item.intValue));
                return;
            case 29:
                BusinessLinksActivity.BusinessLinkView businessLinkView = (BusinessLinksActivity.BusinessLinkView) viewHolder.itemView;
                Object obj5 = item.object;
                if (obj5 instanceof BusinessLinksActivity.BusinessLinkWrapper) {
                    businessLinkView.set((BusinessLinksActivity.BusinessLinkWrapper) obj5, hasDivider);
                    return;
                }
                return;
            case R.styleable.AppCompatTheme_actionModeTheme /* 30 */:
                TextRightIconCell textRightIconCell = (TextRightIconCell) viewHolder.itemView;
                textRightIconCell.setTextAndIcon(item.text, item.iconResId);
                textRightIconCell.setDivider(hasDivider);
                textRightIconCell.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
                return;
            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (TextUtils.equals(graySectionCell.getText(), item.text)) {
                    graySectionCell.setRightText(item.subtext, true, item.clickCallback);
                    return;
                } else {
                    graySectionCell.setText(item.text, item.subtext, item.clickCallback);
                    return;
                }
            case 32:
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                Object obj6 = item.object;
                if (item.withUsername) {
                    if (obj6 instanceof TLRPC$User) {
                        publicUsername = UserObject.getPublicUsername((TLRPC$User) obj6);
                    } else if (obj6 instanceof TLRPC$Chat) {
                        publicUsername = ChatObject.getPublicUsername((TLRPC$Chat) obj6);
                    }
                    if (publicUsername != null) {
                        charSequence = ((Object) "") + "@" + publicUsername;
                        if (!(obj6 instanceof TLRPC$Chat)) {
                            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj6;
                            if (tLRPC$Chat.participants_count != 0) {
                                if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                                    formatPluralStringComma = LocaleController.formatPluralStringComma("Subscribers", tLRPC$Chat.participants_count, ' ');
                                } else {
                                    formatPluralStringComma = LocaleController.formatPluralStringComma("Members", tLRPC$Chat.participants_count, ' ');
                                }
                                if (charSequence instanceof SpannableStringBuilder) {
                                    ((SpannableStringBuilder) charSequence).append((CharSequence) ", ").append((CharSequence) formatPluralStringComma);
                                } else {
                                    charSequence = !TextUtils.isEmpty(charSequence) ? TextUtils.concat(charSequence, ", ", formatPluralStringComma) : formatPluralStringComma;
                                }
                            }
                            str = tLRPC$Chat.title;
                        } else if (obj6 instanceof TLRPC$User) {
                            str = UserObject.getUserName((TLRPC$User) obj6);
                        }
                        profileSearchCell.setData(obj6, null, str, charSequence, false, false);
                        profileSearchCell.useSeparator = hasDivider;
                        return;
                    }
                }
                charSequence = "";
                if (!(obj6 instanceof TLRPC$Chat)) {
                }
                profileSearchCell.setData(obj6, null, str, charSequence, false, false);
                profileSearchCell.useSeparator = hasDivider;
                return;
            case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                Object obj7 = item.object;
                MessageObject messageObject = obj7 instanceof MessageObject ? (MessageObject) obj7 : null;
                dialogCell.useSeparator = hasDivider;
                if (messageObject == null) {
                    dialogCell.setDialog(0L, null, 0, false, false);
                    return;
                } else {
                    dialogCell.setDialog(messageObject.getDialogId(), messageObject, messageObject.messageOwner.date, false, false);
                    return;
                }
            case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                ((FlickerLoadingView) viewHolder.itemView).setViewType(item.intValue);
                return;
            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
            case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
            case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                CheckBoxCell checkBoxCell = (CheckBoxCell) viewHolder.itemView;
                checkBoxCell.setPad(item.pad);
                checkBoxCell.setText(item.text, "", item.checked, hasDivider, checkBoxCell.itemId == item.id);
                checkBoxCell.itemId = item.id;
                checkBoxCell.setIcon(item.locked ? R.drawable.permission_locked : 0);
                if (itemViewType == 36 || itemViewType == 41) {
                    checkBoxCell.setCollapseButton(item.collapsed, item.animatedText, item.clickCallback);
                    return;
                }
                return;
            case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                CheckBoxCell checkBoxCell2 = (CheckBoxCell) viewHolder.itemView;
                checkBoxCell2.setPad(item.pad);
                checkBoxCell2.setUserOrChat((TLObject) item.object);
                checkBoxCell2.setChecked(item.checked, checkBoxCell2.itemId == item.id);
                checkBoxCell2.itemId = item.id;
                checkBoxCell2.setNeedDivider(hasDivider);
                return;
            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
            case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                final TextCheckCell2 textCheckCell2 = (TextCheckCell2) viewHolder.itemView;
                textCheckCell2.setTextAndCheck(item.text.toString(), item.checked, hasDivider, textCheckCell2.id == item.id);
                textCheckCell2.id = item.id;
                textCheckCell2.setIcon(item.locked ? R.drawable.permission_locked : 0);
                if (itemViewType == 40) {
                    textCheckCell2.setCollapseArrow(item.animatedText.toString(), item.collapsed, new Runnable() { // from class: org.telegram.ui.Components.UniversalAdapter$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            UniversalAdapter.lambda$onBindViewHolder$3(UItem.this, textCheckCell2);
                        }
                    });
                    return;
                }
                return;
            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                headerCell.setText(item.animatedText, headerCell.id == item.id);
                headerCell.id = item.id;
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onBindViewHolder$1(UItem uItem, int i) {
        Utilities.Callback<Integer> callback = uItem.intCallback;
        if (callback != null) {
            callback.run(Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ StatisticActivity.BaseChartCell lambda$onBindViewHolder$2(UItem uItem) {
        View findViewByItemObject = findViewByItemObject(uItem.object);
        if (findViewByItemObject instanceof StatisticActivity.UniversalChartCell) {
            return (StatisticActivity.UniversalChartCell) findViewByItemObject;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onBindViewHolder$3(UItem uItem, TextCheckCell2 textCheckCell2) {
        uItem.clickCallback.onClick(textCheckCell2);
    }

    private View findViewByItemObject(Object obj) {
        int i = 0;
        while (true) {
            if (i >= getItemCount()) {
                i = -1;
                break;
            }
            UItem item = getItem(i);
            if (item != null && item.object == obj) {
                break;
            }
            i++;
        }
        if (i == -1) {
            return null;
        }
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            View childAt = this.listView.getChildAt(i2);
            int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
            if (childAdapterPosition != -1 && childAdapterPosition == i) {
                return childAt;
            }
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        updateReorder(viewHolder, this.allowReorder);
    }

    public void updateReorder(RecyclerView.ViewHolder viewHolder, boolean z) {
        if (viewHolder != null && viewHolder.getItemViewType() == 16) {
            ((QuickRepliesActivity.QuickReplyView) viewHolder.itemView).setReorder(z);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        UItem.UItemFactory<?> findFactory;
        int itemViewType = viewHolder.getItemViewType();
        UItem item = getItem(viewHolder.getAdapterPosition());
        if (itemViewType < UItem.factoryViewTypeStartsWith ? itemViewType == 3 || itemViewType == 5 || itemViewType == 6 || itemViewType == 30 || itemViewType == 4 || itemViewType == 10 || itemViewType == 11 || itemViewType == 12 || itemViewType == 17 || itemViewType == 16 || itemViewType == 29 || itemViewType == 25 || itemViewType == 27 || itemViewType == 32 || itemViewType == 33 || itemViewType == 35 || itemViewType == 36 || itemViewType == 37 || itemViewType == 41 || itemViewType == 39 || itemViewType == 40 || itemViewType == 38 : (findFactory = UItem.findFactory(itemViewType)) != null && findFactory.isClickable()) {
            return item == null || item.enabled;
        }
        return false;
    }

    public UItem getItem(int i) {
        if (i < 0 || i >= this.items.size()) {
            return null;
        }
        return this.items.get(i);
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }
}
