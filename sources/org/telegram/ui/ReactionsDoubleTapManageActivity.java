package org.telegram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AvailableReactionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.SelectAnimatedEmojiDialog;

/* loaded from: classes4.dex */
public class ReactionsDoubleTapManageActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private LinearLayout contentView;
    int infoRow;
    private RecyclerView.Adapter listAdapter;
    private RecyclerListView listView;
    int premiumReactionRow;
    int previewRow;
    int reactionsStartRow = -1;
    int rowCount;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SetDefaultReactionCell extends FrameLayout {
        private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable imageDrawable;
        private TextView textView;

        public SetDefaultReactionCell(Context context) {
            super(context);
            setBackgroundColor(ReactionsDoubleTapManageActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextSize(1, 16.0f);
            this.textView.setTextColor(ReactionsDoubleTapManageActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            this.textView.setText(LocaleController.getString(R.string.DoubleTapSetting));
            addView(this.textView, LayoutHelper.createFrame(-1, -2.0f, 23, 20.0f, 0.0f, 48.0f, 0.0f));
            this.imageDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(24.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            updateImageBounds();
            this.imageDrawable.draw(canvas);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageDrawable.attach();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageDrawable.detach();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        public void update(boolean z) {
            String doubleTapReaction = MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).getDoubleTapReaction();
            if (doubleTapReaction != null && doubleTapReaction.startsWith("animated_")) {
                try {
                    this.imageDrawable.set(Long.parseLong(doubleTapReaction.substring(9)), z);
                    return;
                } catch (Exception unused) {
                }
            }
            TLRPC.TL_availableReaction tL_availableReaction = MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).getReactionsMap().get(doubleTapReaction);
            if (tL_availableReaction != null) {
                this.imageDrawable.set(tL_availableReaction.static_icon, z);
            }
        }

        public void updateImageBounds() {
            this.imageDrawable.setBounds((getWidth() - this.imageDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(21.0f), (getHeight() - this.imageDrawable.getIntrinsicHeight()) / 2, getWidth() - AndroidUtilities.dp(21.0f), (getHeight() + this.imageDrawable.getIntrinsicHeight()) / 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List getAvailableReactions() {
        return getMediaDataController().getReactionsList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view, int i) {
        if (!(view instanceof AvailableReactionCell)) {
            if (view instanceof SetDefaultReactionCell) {
                showSelectStatusDialog((SetDefaultReactionCell) view);
                return;
            }
            return;
        }
        AvailableReactionCell availableReactionCell = (AvailableReactionCell) view;
        if (availableReactionCell.locked && !getUserConfig().isPremium()) {
            showDialog(new PremiumFeatureBottomSheet(this, 4, true));
        } else {
            MediaDataController.getInstance(this.currentAccount).setDoubleTapReaction(availableReactionCell.react.reaction);
            this.listView.getAdapter().notifyItemRangeChanged(0, this.listView.getAdapter().getItemCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColors() {
        this.contentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listAdapter.notifyDataSetChanged();
    }

    private void updateRows() {
        this.previewRow = 0;
        this.rowCount = 2;
        this.infoRow = 1;
        if (!UserConfig.getInstance(this.currentAccount).isPremium()) {
            this.premiumReactionRow = -1;
            this.reactionsStartRow = this.rowCount;
        } else {
            this.reactionsStartRow = -1;
            int i = this.rowCount;
            this.rowCount = i + 1;
            this.premiumReactionRow = i;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setTitle(LocaleController.getString(R.string.Reactions));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    ReactionsDoubleTapManageActivity.this.lambda$onBackPressed$319();
                }
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        ((DefaultItemAnimator) recyclerListView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView2 = this.listView;
        RecyclerListView.SelectionAdapter selectionAdapter = new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.2
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                ReactionsDoubleTapManageActivity reactionsDoubleTapManageActivity = ReactionsDoubleTapManageActivity.this;
                return reactionsDoubleTapManageActivity.rowCount + (reactionsDoubleTapManageActivity.premiumReactionRow < 0 ? reactionsDoubleTapManageActivity.getAvailableReactions().size() : 0) + 1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                ReactionsDoubleTapManageActivity reactionsDoubleTapManageActivity = ReactionsDoubleTapManageActivity.this;
                if (i == reactionsDoubleTapManageActivity.previewRow) {
                    return 0;
                }
                if (i == reactionsDoubleTapManageActivity.infoRow) {
                    return 2;
                }
                if (i == reactionsDoubleTapManageActivity.premiumReactionRow) {
                    return 3;
                }
                return i == getItemCount() - 1 ? 4 : 1;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 2;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if (getItemViewType(i) != 1) {
                    return;
                }
                AvailableReactionCell availableReactionCell = (AvailableReactionCell) viewHolder.itemView;
                TLRPC.TL_availableReaction tL_availableReaction = (TLRPC.TL_availableReaction) ReactionsDoubleTapManageActivity.this.getAvailableReactions().get(i - ReactionsDoubleTapManageActivity.this.reactionsStartRow);
                availableReactionCell.bind(tL_availableReaction, tL_availableReaction.reaction.contains(MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).getDoubleTapReaction()), ((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view;
                Context context2;
                int i2;
                View view2;
                if (i != 0) {
                    if (i == 2) {
                        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.DoubleTapPreviewRational));
                        context2 = context;
                        i2 = R.drawable.greydivider;
                        view2 = textInfoPrivacyCell;
                    } else if (i == 3) {
                        SetDefaultReactionCell setDefaultReactionCell = new SetDefaultReactionCell(context);
                        setDefaultReactionCell.update(false);
                        view = setDefaultReactionCell;
                    } else if (i != 4) {
                        view = new AvailableReactionCell(context, true, true);
                    } else {
                        View view3 = new View(context) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.2.1
                            @Override // android.view.View
                            protected void onMeasure(int i3, int i4) {
                                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.0f), 1073741824));
                            }
                        };
                        context2 = context;
                        i2 = R.drawable.greydivider_bottom;
                        view2 = view3;
                    }
                    view2.setBackground(Theme.getThemedDrawableByKey(context2, i2, Theme.key_windowBackgroundGrayShadow));
                    view = view2;
                } else {
                    ThemePreviewMessagesCell themePreviewMessagesCell = new ThemePreviewMessagesCell(context, ((BaseFragment) ReactionsDoubleTapManageActivity.this).parentLayout, 2);
                    themePreviewMessagesCell.setImportantForAccessibility(4);
                    themePreviewMessagesCell.fragment = ReactionsDoubleTapManageActivity.this;
                    view = themePreviewMessagesCell;
                }
                return new RecyclerListView.Holder(view);
            }
        };
        this.listAdapter = selectionAdapter;
        recyclerListView2.setAdapter(selectionAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                ReactionsDoubleTapManageActivity.this.lambda$createView$0(view, i);
            }
        });
        linearLayout.addView(this.listView, LayoutHelper.createLinear(-1, -1));
        this.contentView = linearLayout;
        this.fragmentView = linearLayout;
        updateColors();
        updateRows();
        return this.contentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i2 != this.currentAccount) {
            return;
        }
        if (i != NotificationCenter.reactionsDidLoad) {
            if (i != NotificationCenter.currentUserPremiumStatusChanged) {
                return;
            } else {
                updateRows();
            }
        }
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        return SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ReactionsDoubleTapManageActivity.this.updateColors();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, Theme.key_windowBackgroundWhite, Theme.key_windowBackgroundWhiteBlackText, Theme.key_windowBackgroundWhiteGrayText2, Theme.key_listSelector, Theme.key_windowBackgroundGray, Theme.key_windowBackgroundWhiteGrayText4, Theme.key_text_RedRegular, Theme.key_windowBackgroundChecked, Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.reactionsDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.reactionsDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00bd A[LOOP:0: B:20:0x00b7->B:22:0x00bd, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showSelectStatusDialog(final SetDefaultReactionCell setDefaultReactionCell) {
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        SetDefaultReactionCell setDefaultReactionCell2;
        int i;
        int i2;
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog;
        String doubleTapReaction;
        List availableReactions;
        int i3;
        if (this.selectAnimatedEmojiDialog != null) {
            return;
        }
        final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        if (setDefaultReactionCell != null) {
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = setDefaultReactionCell.imageDrawable;
            if (setDefaultReactionCell.imageDrawable != null) {
                setDefaultReactionCell.imageDrawable.play();
                setDefaultReactionCell.updateImageBounds();
                Rect rect = AndroidUtilities.rectTmp2;
                rect.set(setDefaultReactionCell.imageDrawable.getBounds());
                int dp = (-(setDefaultReactionCell.getHeight() - rect.centerY())) - AndroidUtilities.dp(16.0f);
                i = rect.centerX() - (AndroidUtilities.displaySize.x - ((int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f)));
                swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable2;
                i2 = dp;
                setDefaultReactionCell2 = setDefaultReactionCell;
                int i4 = i2;
                selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), false, Integer.valueOf(i), 2, null) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.3
                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog
                    protected void onEmojiSelected(View view, Long l, TLRPC.Document document, Integer num) {
                        if (l == null) {
                            return;
                        }
                        MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).setDoubleTapReaction("animated_" + l);
                        SetDefaultReactionCell setDefaultReactionCell3 = setDefaultReactionCell;
                        if (setDefaultReactionCell3 != null) {
                            setDefaultReactionCell3.update(true);
                        }
                        if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                            ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                            selectAnimatedEmojiDialogWindowArr[0].dismiss();
                        }
                    }

                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog
                    protected void onReactionClick(SelectAnimatedEmojiDialog.ImageViewEmoji imageViewEmoji, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
                        MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).setDoubleTapReaction(visibleReaction.emojicon);
                        SetDefaultReactionCell setDefaultReactionCell3 = setDefaultReactionCell;
                        if (setDefaultReactionCell3 != null) {
                            setDefaultReactionCell3.update(true);
                        }
                        if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                            ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                            selectAnimatedEmojiDialogWindowArr[0].dismiss();
                        }
                    }
                };
                doubleTapReaction = getMediaDataController().getDoubleTapReaction();
                if (doubleTapReaction != null && doubleTapReaction.startsWith("animated_")) {
                    try {
                        selectAnimatedEmojiDialog.setSelected(Long.valueOf(Long.parseLong(doubleTapReaction.substring(9))));
                    } catch (Exception unused) {
                    }
                }
                availableReactions = getAvailableReactions();
                ArrayList arrayList = new ArrayList(20);
                for (i3 = 0; i3 < availableReactions.size(); i3++) {
                    ReactionsLayoutInBubble.VisibleReaction visibleReaction = new ReactionsLayoutInBubble.VisibleReaction();
                    visibleReaction.emojicon = ((TLRPC.TL_availableReaction) availableReactions.get(i3)).reaction;
                    arrayList.add(visibleReaction);
                }
                selectAnimatedEmojiDialog.setRecentReactions(arrayList);
                selectAnimatedEmojiDialog.setSaveState(3);
                selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, setDefaultReactionCell2);
                int i5 = -2;
                SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, i5, i5) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.4
                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                    }
                };
                this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindow.showAsDropDown(setDefaultReactionCell, 0, i4, 53);
                selectAnimatedEmojiDialogWindowArr[0].dimBehind();
            }
            swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable2;
            setDefaultReactionCell2 = setDefaultReactionCell;
        } else {
            swapAnimatedEmojiDrawable = null;
            setDefaultReactionCell2 = null;
        }
        i = 0;
        i2 = 0;
        int i42 = i2;
        selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), false, Integer.valueOf(i), 2, null) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.3
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view, Long l, TLRPC.Document document, Integer num) {
                if (l == null) {
                    return;
                }
                MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).setDoubleTapReaction("animated_" + l);
                SetDefaultReactionCell setDefaultReactionCell3 = setDefaultReactionCell;
                if (setDefaultReactionCell3 != null) {
                    setDefaultReactionCell3.update(true);
                }
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onReactionClick(SelectAnimatedEmojiDialog.ImageViewEmoji imageViewEmoji, ReactionsLayoutInBubble.VisibleReaction visibleReaction2) {
                MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).setDoubleTapReaction(visibleReaction2.emojicon);
                SetDefaultReactionCell setDefaultReactionCell3 = setDefaultReactionCell;
                if (setDefaultReactionCell3 != null) {
                    setDefaultReactionCell3.update(true);
                }
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }
        };
        doubleTapReaction = getMediaDataController().getDoubleTapReaction();
        if (doubleTapReaction != null) {
            selectAnimatedEmojiDialog.setSelected(Long.valueOf(Long.parseLong(doubleTapReaction.substring(9))));
        }
        availableReactions = getAvailableReactions();
        ArrayList arrayList2 = new ArrayList(20);
        while (i3 < availableReactions.size()) {
        }
        selectAnimatedEmojiDialog.setRecentReactions(arrayList2);
        selectAnimatedEmojiDialog.setSaveState(3);
        selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, setDefaultReactionCell2);
        int i52 = -2;
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow2 = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, i52, i52) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.4
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindow2.showAsDropDown(setDefaultReactionCell, 0, i42, 53);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }
}
