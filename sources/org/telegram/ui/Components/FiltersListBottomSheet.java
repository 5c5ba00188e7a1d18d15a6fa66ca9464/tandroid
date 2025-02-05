package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;

/* loaded from: classes3.dex */
public class FiltersListBottomSheet extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private FiltersListBottomSheetDelegate delegate;
    private ArrayList dialogFilters;
    private final DialogsActivity fragment;
    private boolean ignoreLayout;
    private RecyclerListView listView;
    private int scrollOffsetY;
    private final ArrayList selectedDialogs;
    private View shadow;
    private AnimatorSet shadowAnimation;
    private TextView titleTextView;

    public interface FiltersListBottomSheetDelegate {
        void didSelectFilter(MessagesController.DialogFilter dialogFilter, boolean z);
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;

        public ListAdapter(Context context) {
            this.context = context;
        }

        public MessagesController.DialogFilter getItem(int i) {
            if (i < FiltersListBottomSheet.this.dialogFilters.size()) {
                return (MessagesController.DialogFilter) FiltersListBottomSheet.this.dialogFilters.get(i);
            }
            return null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = FiltersListBottomSheet.this.dialogFilters.size();
            return size < 10 ? size + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            BottomSheet.BottomSheetCell bottomSheetCell = (BottomSheet.BottomSheetCell) viewHolder.itemView;
            if (i >= FiltersListBottomSheet.this.dialogFilters.size()) {
                bottomSheetCell.getImageView().setColorFilter((ColorFilter) null);
                Drawable drawable = this.context.getResources().getDrawable(R.drawable.poll_add_circle);
                Drawable drawable2 = this.context.getResources().getDrawable(R.drawable.poll_add_plus);
                int color = Theme.getColor(Theme.key_switchTrackChecked);
                PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                drawable.setColorFilter(new PorterDuffColorFilter(color, mode));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), mode));
                CombinedDrawable combinedDrawable = new CombinedDrawable(drawable, drawable2);
                bottomSheetCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
                bottomSheetCell.setTextAndIcon(LocaleController.getString(R.string.CreateNewFilter), combinedDrawable);
                return;
            }
            bottomSheetCell.getImageView().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), PorterDuff.Mode.MULTIPLY));
            MessagesController.DialogFilter dialogFilter = (MessagesController.DialogFilter) FiltersListBottomSheet.this.dialogFilters.get(i);
            bottomSheetCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
            int i3 = dialogFilter.flags;
            if ((MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS & i3) == (MessagesController.DIALOG_FILTER_FLAG_CONTACTS | MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS)) {
                i2 = R.drawable.msg_openprofile;
            } else {
                if ((MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ & i3) != 0) {
                    int i4 = MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS;
                    if ((i3 & i4) == i4) {
                        i2 = R.drawable.msg_markunread;
                    }
                }
                i2 = (MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS & i3) == MessagesController.DIALOG_FILTER_FLAG_CHANNELS ? R.drawable.msg_channel : (MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS & i3) == MessagesController.DIALOG_FILTER_FLAG_GROUPS ? R.drawable.msg_groups : (MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS & i3) == MessagesController.DIALOG_FILTER_FLAG_CONTACTS ? R.drawable.msg_contacts : (i3 & MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS) == MessagesController.DIALOG_FILTER_FLAG_BOTS ? R.drawable.msg_bots : R.drawable.msg_folders;
            }
            bottomSheetCell.setTextAndIcon(MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji(dialogFilter.name, bottomSheetCell.getTextView().getPaint().getFontMetricsInt(), false), dialogFilter.entities, bottomSheetCell.getTextView().getPaint().getFontMetricsInt()), 0, new FolderDrawable(FiltersListBottomSheet.this.getContext(), i2, dialogFilter.color), false);
            bottomSheetCell.getTextView().setEmojiColor(Theme.getColor(Theme.key_featuredStickers_addButton, ((BottomSheet) FiltersListBottomSheet.this).resourcesProvider));
            boolean z = true;
            for (int i5 = 0; i5 < FiltersListBottomSheet.this.selectedDialogs.size(); i5++) {
                if (!dialogFilter.includesDialog(AccountInstance.getInstance(((BottomSheet) FiltersListBottomSheet.this).currentAccount), ((Long) FiltersListBottomSheet.this.selectedDialogs.get(i5)).longValue())) {
                    z = false;
                }
            }
            bottomSheetCell.setChecked(z);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            BottomSheet.BottomSheetCell bottomSheetCell = new BottomSheet.BottomSheetCell(this.context, 0);
            bottomSheetCell.setBackground(null);
            bottomSheetCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(bottomSheetCell);
        }
    }

    public FiltersListBottomSheet(DialogsActivity dialogsActivity, ArrayList arrayList) {
        super(dialogsActivity.getParentActivity(), false);
        fixNavigationBar();
        this.selectedDialogs = arrayList;
        this.fragment = dialogsActivity;
        this.dialogFilters = new ArrayList(dialogsActivity.getMessagesController().dialogFilters);
        int i = 0;
        while (i < this.dialogFilters.size()) {
            if (((MessagesController.DialogFilter) this.dialogFilters.get(i)).isDefault()) {
                this.dialogFilters.remove(i);
                i--;
            }
            i++;
        }
        Activity parentActivity = dialogsActivity.getParentActivity();
        FrameLayout frameLayout = new FrameLayout(parentActivity) { // from class: org.telegram.ui.Components.FiltersListBottomSheet.1
            private boolean fullHeight;
            private RectF rect = new RectF();
            private Boolean statusBarOpen;

            private void updateLightStatusBar(boolean z) {
                Boolean bool = this.statusBarOpen;
                if (bool == null || bool.booleanValue() != z) {
                    boolean z2 = AndroidUtilities.computePerceivedBrightness(FiltersListBottomSheet.this.getThemedColor(Theme.key_dialogBackground)) > 0.721f;
                    boolean z3 = AndroidUtilities.computePerceivedBrightness(Theme.blendOver(FiltersListBottomSheet.this.getThemedColor(Theme.key_actionBarDefault), AndroidUtilities.DARK_STATUS_BAR_OVERLAY)) > 0.721f;
                    this.statusBarOpen = Boolean.valueOf(z);
                    if (!z) {
                        z2 = z3;
                    }
                    AndroidUtilities.setLightStatusBar(FiltersListBottomSheet.this.getWindow(), z2);
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:13:0x009a  */
            /* JADX WARN: Removed duplicated region for block: B:15:0x00ed  */
            /* JADX WARN: Removed duplicated region for block: B:18:0x011e  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onDraw(Canvas canvas) {
                float f;
                int i2;
                int dp = (FiltersListBottomSheet.this.scrollOffsetY - ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop) - AndroidUtilities.dp(8.0f);
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(36.0f) + ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop;
                if (Build.VERSION.SDK_INT >= 21) {
                    int i3 = AndroidUtilities.statusBarHeight;
                    dp += i3;
                    measuredHeight -= i3;
                    if (this.fullHeight) {
                        int i4 = ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop + dp;
                        int i5 = AndroidUtilities.statusBarHeight;
                        int i6 = i5 * 2;
                        if (i4 < i6) {
                            int min = Math.min(i5, (i6 - dp) - ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop);
                            dp -= min;
                            measuredHeight += min;
                            f = 1.0f - Math.min(1.0f, (min * 2) / AndroidUtilities.statusBarHeight);
                        } else {
                            f = 1.0f;
                        }
                        int i7 = ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop + dp;
                        int i8 = AndroidUtilities.statusBarHeight;
                        if (i7 < i8) {
                            i2 = Math.min(i8, (i8 - dp) - ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop);
                            ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.setBounds(0, dp, getMeasuredWidth(), measuredHeight);
                            ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.draw(canvas);
                            if (f != 1.0f) {
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_dialogBackground));
                                this.rect.set(((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop + dp, getMeasuredWidth() - ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop + dp + AndroidUtilities.dp(24.0f));
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                            }
                            if (i2 > 0) {
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_dialogBackground));
                                canvas.drawRect(((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight - i2, getMeasuredWidth() - ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
                            }
                            updateLightStatusBar(i2 > AndroidUtilities.statusBarHeight / 2);
                        }
                        i2 = 0;
                        ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.setBounds(0, dp, getMeasuredWidth(), measuredHeight);
                        ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.draw(canvas);
                        if (f != 1.0f) {
                        }
                        if (i2 > 0) {
                        }
                        updateLightStatusBar(i2 > AndroidUtilities.statusBarHeight / 2);
                    }
                }
                f = 1.0f;
                i2 = 0;
                ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.setBounds(0, dp, getMeasuredWidth(), measuredHeight);
                ((BottomSheet) FiltersListBottomSheet.this).shadowDrawable.draw(canvas);
                if (f != 1.0f) {
                }
                if (i2 > 0) {
                }
                updateLightStatusBar(i2 > AndroidUtilities.statusBarHeight / 2);
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || FiltersListBottomSheet.this.scrollOffsetY == 0 || motionEvent.getY() >= FiltersListBottomSheet.this.scrollOffsetY) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                FiltersListBottomSheet.this.lambda$new$0();
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                FiltersListBottomSheet.this.updateLayout();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                int size = View.MeasureSpec.getSize(i3);
                if (Build.VERSION.SDK_INT >= 21) {
                    FiltersListBottomSheet.this.ignoreLayout = true;
                    setPadding(((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingLeft, 0);
                    FiltersListBottomSheet.this.ignoreLayout = false;
                }
                int dp = AndroidUtilities.dp(48.0f) + (AndroidUtilities.dp(48.0f) * FiltersListBottomSheet.this.adapter.getItemCount()) + ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop + AndroidUtilities.statusBarHeight;
                double d = dp;
                int i4 = size / 5;
                double d2 = i4;
                Double.isNaN(d2);
                int i5 = d < d2 * 3.2d ? 0 : i4 * 2;
                if (i5 != 0 && dp < size) {
                    i5 -= size - dp;
                }
                if (i5 == 0) {
                    i5 = ((BottomSheet) FiltersListBottomSheet.this).backgroundPaddingTop;
                }
                if (FiltersListBottomSheet.this.listView.getPaddingTop() != i5) {
                    FiltersListBottomSheet.this.ignoreLayout = true;
                    FiltersListBottomSheet.this.listView.setPadding(AndroidUtilities.dp(10.0f), i5, AndroidUtilities.dp(10.0f), 0);
                    FiltersListBottomSheet.this.ignoreLayout = false;
                }
                this.fullHeight = dp >= size;
                super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(Math.min(dp, size), 1073741824));
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !FiltersListBottomSheet.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (FiltersListBottomSheet.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.containerView = frameLayout;
        frameLayout.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i2 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i2, 0, i2, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp(48.0f);
        View view = new View(parentActivity);
        this.shadow = view;
        view.setBackgroundColor(Theme.getColor(Theme.key_dialogShadowLine));
        this.shadow.setAlpha(0.0f);
        this.shadow.setVisibility(4);
        this.shadow.setTag(1);
        this.containerView.addView(this.shadow, layoutParams);
        RecyclerListView recyclerListView = new RecyclerListView(parentActivity) { // from class: org.telegram.ui.Components.FiltersListBottomSheet.2
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (FiltersListBottomSheet.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setTag(14);
        this.listView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(parentActivity);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
        this.listView.setClipToPadding(false);
        this.listView.setGlowColor(Theme.getColor(Theme.key_dialogScrollGlow));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.FiltersListBottomSheet.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                FiltersListBottomSheet.this.updateLayout();
            }
        });
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.FiltersListBottomSheet$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i3) {
                FiltersListBottomSheet.this.lambda$new$0(view2, i3);
            }
        });
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        TextView textView = new TextView(parentActivity);
        this.titleTextView = textView;
        textView.setLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        this.titleTextView.setTextSize(1, 20.0f);
        this.titleTextView.setLinkTextColor(Theme.getColor(Theme.key_dialogTextLink));
        this.titleTextView.setHighlightColor(Theme.getColor(Theme.key_dialogLinkSelection));
        this.titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.titleTextView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
        this.titleTextView.setGravity(16);
        this.titleTextView.setText(LocaleController.getString(R.string.FilterChoose));
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        this.containerView.addView(this.titleTextView, LayoutHelper.createFrame(-1, 50.0f, 51, 0.0f, 0.0f, 40.0f, 0.0f));
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
    }

    public static ArrayList getCanAddDialogFilters(BaseFragment baseFragment, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        ArrayList<MessagesController.DialogFilter> arrayList3 = baseFragment.getMessagesController().dialogFilters;
        int size = arrayList3.size();
        for (int i = 0; i < size; i++) {
            MessagesController.DialogFilter dialogFilter = arrayList3.get(i);
            if (!getDialogsCount(baseFragment, dialogFilter, arrayList, true, true).isEmpty() && !dialogFilter.isDefault()) {
                arrayList2.add(dialogFilter);
            }
        }
        return arrayList2;
    }

    public static ArrayList getDialogsCount(BaseFragment baseFragment, MessagesController.DialogFilter dialogFilter, ArrayList arrayList, boolean z, boolean z2) {
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            long longValue = ((Long) arrayList.get(i)).longValue();
            if (DialogObject.isEncryptedDialog(longValue)) {
                TLRPC.EncryptedChat encryptedChat = baseFragment.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue)));
                if (encryptedChat != null) {
                    longValue = encryptedChat.user_id;
                    if (arrayList2.contains(Long.valueOf(longValue))) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            if (dialogFilter == null || ((!z || !dialogFilter.alwaysShow.contains(Long.valueOf(longValue))) && (z || !dialogFilter.neverShow.contains(Long.valueOf(longValue))))) {
                arrayList2.add(Long.valueOf(longValue));
                if (z2) {
                    break;
                }
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$1(View view) {
        if (view instanceof BottomSheet.BottomSheetCell) {
            view = ((BottomSheet.BottomSheetCell) view).getTextView();
        }
        view.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i) {
        this.delegate.didSelectFilter(this.adapter.getItem(i), view instanceof BottomSheet.BottomSheetCell ? ((BottomSheet.BottomSheetCell) view).isChecked() : false);
        lambda$new$0();
    }

    private void runShadowAnimation(final boolean z) {
        if ((!z || this.shadow.getTag() == null) && (z || this.shadow.getTag() != null)) {
            return;
        }
        this.shadow.setTag(z ? null : 1);
        if (z) {
            this.shadow.setVisibility(0);
        }
        AnimatorSet animatorSet = this.shadowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.shadowAnimation = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.shadow, (Property<View, Float>) View.ALPHA, z ? 1.0f : 0.0f));
        this.shadowAnimation.setDuration(150L);
        this.shadowAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.FiltersListBottomSheet.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (FiltersListBottomSheet.this.shadowAnimation == null || !FiltersListBottomSheet.this.shadowAnimation.equals(animator)) {
                    return;
                }
                FiltersListBottomSheet.this.shadowAnimation = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (FiltersListBottomSheet.this.shadowAnimation == null || !FiltersListBottomSheet.this.shadowAnimation.equals(animator)) {
                    return;
                }
                if (!z) {
                    FiltersListBottomSheet.this.shadow.setVisibility(4);
                }
                FiltersListBottomSheet.this.shadowAnimation = null;
            }
        });
        this.shadowAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.titleTextView.setTranslationY(this.scrollOffsetY);
            this.shadow.setTranslationY(this.scrollOffsetY);
            this.containerView.invalidate();
            return;
        }
        int i = 0;
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        if (top < 0 || holder == null || holder.getAdapterPosition() != 0) {
            runShadowAnimation(true);
        } else {
            runShadowAnimation(false);
            i = top;
        }
        if (this.scrollOffsetY != i) {
            RecyclerListView recyclerListView2 = this.listView;
            this.scrollOffsetY = i;
            recyclerListView2.setTopGlowOffset(i);
            this.titleTextView.setTranslationY(this.scrollOffsetY);
            this.shadow.setTranslationY(this.scrollOffsetY);
            this.containerView.invalidate();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.Components.FiltersListBottomSheet$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    FiltersListBottomSheet.lambda$didReceivedNotification$1((View) obj);
                }
            });
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    /* renamed from: dismiss */
    public void lambda$new$0() {
        super.lambda$new$0();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    public void setDelegate(FiltersListBottomSheetDelegate filtersListBottomSheetDelegate) {
        this.delegate = filtersListBottomSheetDelegate;
    }
}
