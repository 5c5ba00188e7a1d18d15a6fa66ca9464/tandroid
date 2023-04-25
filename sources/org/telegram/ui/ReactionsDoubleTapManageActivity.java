package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
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
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
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
/* loaded from: classes3.dex */
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.reactionsDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setTitle(LocaleController.getString("Reactions", R.string.Reactions));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    ReactionsDoubleTapManageActivity.this.finishFragment();
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
            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 2;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                SetDefaultReactionCell setDefaultReactionCell;
                if (i == 0) {
                    ThemePreviewMessagesCell themePreviewMessagesCell = new ThemePreviewMessagesCell(context, ((BaseFragment) ReactionsDoubleTapManageActivity.this).parentLayout, 2);
                    if (Build.VERSION.SDK_INT >= 19) {
                        themePreviewMessagesCell.setImportantForAccessibility(4);
                    }
                    themePreviewMessagesCell.fragment = ReactionsDoubleTapManageActivity.this;
                    setDefaultReactionCell = themePreviewMessagesCell;
                } else if (i == 2) {
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
                    textInfoPrivacyCell.setText(LocaleController.getString("DoubleTapPreviewRational", R.string.DoubleTapPreviewRational));
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    setDefaultReactionCell = textInfoPrivacyCell;
                } else if (i == 3) {
                    SetDefaultReactionCell setDefaultReactionCell2 = new SetDefaultReactionCell(context);
                    setDefaultReactionCell2.update(false);
                    setDefaultReactionCell = setDefaultReactionCell2;
                } else if (i == 4) {
                    View view = new View(this, context) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.2.1
                        @Override // android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.0f), 1073741824));
                        }
                    };
                    view.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    setDefaultReactionCell = view;
                } else {
                    setDefaultReactionCell = new AvailableReactionCell(context, true, true);
                }
                return new RecyclerListView.Holder(setDefaultReactionCell);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                if (getItemViewType(i) != 1) {
                    return;
                }
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = (TLRPC$TL_availableReaction) ReactionsDoubleTapManageActivity.this.getAvailableReactions().get(i - ReactionsDoubleTapManageActivity.this.reactionsStartRow);
                ((AvailableReactionCell) viewHolder.itemView).bind(tLRPC$TL_availableReaction, tLRPC$TL_availableReaction.reaction.contains(MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).getDoubleTapReaction()), ((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount);
            }

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
        };
        this.listAdapter = selectionAdapter;
        recyclerListView2.setAdapter(selectionAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda1
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view, int i) {
        if (view instanceof AvailableReactionCell) {
            AvailableReactionCell availableReactionCell = (AvailableReactionCell) view;
            if (availableReactionCell.locked && !getUserConfig().isPremium()) {
                showDialog(new PremiumFeatureBottomSheet(this, 4, true));
                return;
            }
            MediaDataController.getInstance(this.currentAccount).setDoubleTapReaction(availableReactionCell.react.reaction);
            this.listView.getAdapter().notifyItemRangeChanged(0, this.listView.getAdapter().getItemCount());
        } else if (view instanceof SetDefaultReactionCell) {
            showSelectStatusDialog((SetDefaultReactionCell) view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
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
            this.textView.setText(LocaleController.getString("DoubleTapSetting", R.string.DoubleTapSetting));
            addView(this.textView, LayoutHelper.createFrame(-1, -2.0f, 23, 20.0f, 0.0f, 48.0f, 0.0f));
            this.imageDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(24.0f));
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
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount).getReactionsMap().get(doubleTapReaction);
            if (tLRPC$TL_availableReaction != null) {
                this.imageDrawable.set(tLRPC$TL_availableReaction.static_icon, z);
            }
        }

        public void updateImageBounds() {
            this.imageDrawable.setBounds((getWidth() - this.imageDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(21.0f), (getHeight() - this.imageDrawable.getIntrinsicHeight()) / 2, getWidth() - AndroidUtilities.dp(21.0f), (getHeight() + this.imageDrawable.getIntrinsicHeight()) / 2);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            updateImageBounds();
            this.imageDrawable.draw(canvas);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageDrawable.detach();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageDrawable.attach();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00bb A[LOOP:0: B:20:0x00b5->B:22:0x00bb, LOOP_END] */
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
        List<TLRPC$TL_availableReaction> availableReactions;
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
                    protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                        if (l == null) {
                            return;
                        }
                        MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount);
                        mediaDataController.setDoubleTapReaction("animated_" + l);
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
                    visibleReaction.emojicon = availableReactions.get(i3).reaction;
                    arrayList.add(visibleReaction);
                }
                selectAnimatedEmojiDialog.setRecentReactions(arrayList);
                selectAnimatedEmojiDialog.setSaveState(3);
                selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, setDefaultReactionCell2);
                SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.4
                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
                    }
                };
                this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(setDefaultReactionCell, 0, i4, 53);
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
            protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                if (l == null) {
                    return;
                }
                MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) ReactionsDoubleTapManageActivity.this).currentAccount);
                mediaDataController.setDoubleTapReaction("animated_" + l);
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
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow2 = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity.4
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                ReactionsDoubleTapManageActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(setDefaultReactionCell, 0, i42, 53);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.previewRow = 0;
        this.rowCount = i + 1;
        this.infoRow = i;
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            this.reactionsStartRow = -1;
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.premiumReactionRow = i2;
            return;
        }
        this.premiumReactionRow = -1;
        this.reactionsStartRow = this.rowCount;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.reactionsDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<TLRPC$TL_availableReaction> getAvailableReactions() {
        return getMediaDataController().getReactionsList();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda0
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

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NotifyDataSetChanged"})
    public void updateColors() {
        this.contentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    @SuppressLint({"NotifyDataSetChanged"})
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i2 != this.currentAccount) {
            return;
        }
        if (i == NotificationCenter.reactionsDidLoad) {
            this.listAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
            updateRows();
            this.listAdapter.notifyDataSetChanged();
        }
    }
}
