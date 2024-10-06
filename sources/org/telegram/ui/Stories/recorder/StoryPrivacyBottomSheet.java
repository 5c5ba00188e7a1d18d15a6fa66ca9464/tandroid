package org.telegram.ui.Stories.recorder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerCustom;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RadioButton;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;
/* loaded from: classes4.dex */
public class StoryPrivacyBottomSheet extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private int activePage;
    private boolean allowScreenshots;
    private boolean allowSmallChats;
    private boolean applyWhenDismiss;
    private final Paint backgroundPaint;
    private boolean canChangePeer;
    private Drawable coverDrawable;
    private final ArrayList excludedContacts;
    private final ArrayList excludedEveryone;
    private final HashMap excludedEveryoneByGroup;
    private int excludedEveryoneCount;
    private boolean isEdit;
    private boolean keepOnMyPage;
    private boolean loadedContacts;
    private final ArrayList messageUsers;
    private Utilities.Callback onDismiss;
    private DoneCallback onDone;
    private Utilities.Callback onDone2;
    private Utilities.Callback onSelectedPeer;
    private final ArrayList selectedContacts;
    private final HashMap selectedContactsByGroup;
    private int selectedContactsCount;
    public TLRPC.InputPeer selectedPeer;
    private int selectedType;
    private boolean sendAsMessageEnabled;
    private int shiftDp;
    private HashMap smallChatsParticipantsCount;
    private boolean startedFromSendAsMessage;
    private int storyPeriod;
    private ViewPagerFixed viewPager;
    private ArrayList warnUsers;
    private Runnable whenCoverClicked;

    /* loaded from: classes4.dex */
    public static class ChoosePeerSheet extends BottomSheet {
        private final int currentAccount;
        private final TextView headerView;
        private final RecyclerListView listView;
        private final Utilities.Callback onPeerSelected;
        private final List peers;
        private final TLRPC.InputPeer selectedPeer;

        /* loaded from: classes4.dex */
        private class Adapter extends RecyclerListView.SelectionAdapter {
            private Adapter() {
            }

            private long did(TLRPC.InputPeer inputPeer) {
                return inputPeer instanceof TLRPC.TL_inputPeerSelf ? UserConfig.getInstance(ChoosePeerSheet.this.currentAccount).getClientUserId() : DialogObject.getPeerDialogId(inputPeer);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return ChoosePeerSheet.this.peers.size() + 2;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                if (i == 0) {
                    return 0;
                }
                return i == 1 ? 1 : 2;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return viewHolder.getItemViewType() == 2;
            }

            /* JADX WARN: Removed duplicated region for block: B:20:0x008e A[ADDED_TO_REGION] */
            /* JADX WARN: Removed duplicated region for block: B:28:0x00b0  */
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                MessagesController messagesController;
                long j;
                TLRPC.User user;
                if (viewHolder.getItemViewType() == 2) {
                    UserCell userCell = (UserCell) viewHolder.itemView;
                    userCell.setIsSendAs(true, true);
                    TLRPC.InputPeer inputPeer = (TLRPC.InputPeer) ChoosePeerSheet.this.peers.get(i - 2);
                    if (inputPeer instanceof TLRPC.TL_inputPeerSelf) {
                        user = UserConfig.getInstance(ChoosePeerSheet.this.currentAccount).getCurrentUser();
                    } else if (!(inputPeer instanceof TLRPC.TL_inputPeerUser)) {
                        if (!(inputPeer instanceof TLRPC.TL_inputPeerChat)) {
                            if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
                                messagesController = MessagesController.getInstance(ChoosePeerSheet.this.currentAccount);
                                j = inputPeer.channel_id;
                            }
                            userCell.checkBox.setVisibility(8);
                            userCell.radioButton.setVisibility(0);
                            userCell.setChecked((ChoosePeerSheet.this.selectedPeer != null && i == 2) || did(ChoosePeerSheet.this.selectedPeer) == did(inputPeer), false);
                            userCell.setDivider(i != getItemCount() - 1);
                        }
                        messagesController = MessagesController.getInstance(ChoosePeerSheet.this.currentAccount);
                        j = inputPeer.chat_id;
                        userCell.setChat(messagesController.getChat(Long.valueOf(j)), 0);
                        userCell.checkBox.setVisibility(8);
                        userCell.radioButton.setVisibility(0);
                        userCell.setChecked((ChoosePeerSheet.this.selectedPeer != null && i == 2) || did(ChoosePeerSheet.this.selectedPeer) == did(inputPeer), false);
                        userCell.setDivider(i != getItemCount() - 1);
                    } else {
                        user = MessagesController.getInstance(ChoosePeerSheet.this.currentAccount).getUser(Long.valueOf(inputPeer.user_id));
                    }
                    userCell.setUser(user);
                    userCell.checkBox.setVisibility(8);
                    userCell.radioButton.setVisibility(0);
                    userCell.setChecked((ChoosePeerSheet.this.selectedPeer != null && i == 2) || did(ChoosePeerSheet.this.selectedPeer) == did(inputPeer), false);
                    userCell.setDivider(i != getItemCount() - 1);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view;
                if (i == 0 || i == 1) {
                    view = new View(ChoosePeerSheet.this.getContext());
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, i == 0 ? (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(306.0f) : AndroidUtilities.dp(54.0f)));
                } else {
                    view = new UserCell(ChoosePeerSheet.this.getContext(), ((BottomSheet) ChoosePeerSheet.this).resourcesProvider);
                }
                return new RecyclerListView.Holder(view);
            }
        }

        public ChoosePeerSheet(Context context, final int i, TLRPC.InputPeer inputPeer, final Utilities.Callback callback, final Theme.ResourcesProvider resourcesProvider) {
            super(context, false, resourcesProvider);
            fixNavigationBar();
            this.currentAccount = i;
            this.peers = MessagesController.getInstance(i).getStoriesController().sendAs;
            this.selectedPeer = inputPeer;
            this.onPeerSelected = callback;
            this.containerView = new FrameLayout(context) { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.ChoosePeerSheet.1
                private final Paint backgroundPaint = new Paint(1);
                private final AnimatedFloat statusBarT = new AnimatedFloat(this, 0, 350, CubicBezierInterpolator.EASE_OUT_QUINT);

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    this.backgroundPaint.setColor(Theme.getColor(Theme.key_dialogBackground, resourcesProvider));
                    float max = Math.max(0.0f, ChoosePeerSheet.this.top());
                    float lerp = AndroidUtilities.lerp(max, 0.0f, this.statusBarT.set(max < ((float) AndroidUtilities.statusBarHeight)));
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(((BottomSheet) ChoosePeerSheet.this).backgroundPaddingLeft, lerp, getWidth() - ((BottomSheet) ChoosePeerSheet.this).backgroundPaddingLeft, getHeight() + AndroidUtilities.dp(14.0f));
                    float dp = AndroidUtilities.dp(14.0f) * (1.0f - this.statusBarT.get());
                    canvas.drawRoundRect(rectF, dp, dp, this.backgroundPaint);
                    ChoosePeerSheet.this.headerView.setTranslationY(Math.max(AndroidUtilities.statusBarHeight + AndroidUtilities.dp(8.0f), AndroidUtilities.dp(14.0f) + lerp));
                    canvas.save();
                    canvas.clipRect(((BottomSheet) ChoosePeerSheet.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight + AndroidUtilities.dp(14.0f), getWidth() - ((BottomSheet) ChoosePeerSheet.this).backgroundPaddingLeft, getHeight());
                    super.dispatchDraw(canvas);
                    canvas.restore();
                }

                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getY() < ChoosePeerSheet.this.top()) {
                        ChoosePeerSheet.this.dismiss();
                        return true;
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }
            };
            RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider);
            this.listView = recyclerListView;
            int i2 = this.backgroundPaddingLeft;
            recyclerListView.setPadding(i2, 0, i2, 0);
            recyclerListView.setAdapter(new Adapter());
            recyclerListView.setLayoutManager(new LinearLayoutManager(context));
            this.containerView.addView(recyclerListView, LayoutHelper.createFrame(-1, -1, 119));
            recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$ChoosePeerSheet$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i3) {
                    StoryPrivacyBottomSheet.ChoosePeerSheet.this.lambda$new$1(callback, resourcesProvider, i, view, i3);
                }
            });
            recyclerListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.ChoosePeerSheet.2
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                    ((BottomSheet) ChoosePeerSheet.this).containerView.invalidate();
                }
            });
            TextView textView = new TextView(getContext());
            this.headerView = textView;
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            textView.setTextSize(1, 20.0f);
            textView.setPadding(this.backgroundPaddingLeft + AndroidUtilities.dp(22.0f), AndroidUtilities.dp(2.0f), this.backgroundPaddingLeft + AndroidUtilities.dp(22.0f), AndroidUtilities.dp(14.0f));
            textView.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground, resourcesProvider));
            textView.setTypeface(AndroidUtilities.bold());
            textView.setText(LocaleController.getString(R.string.StoryPrivacyPublishAs));
            this.containerView.addView(textView, LayoutHelper.createFrame(-1, -2.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0(AlertDialog alertDialog, Utilities.Callback callback, TLRPC.InputPeer inputPeer, Boolean bool) {
            alertDialog.dismiss();
            if (!bool.booleanValue() || callback == null) {
                return;
            }
            callback.run(inputPeer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(final Utilities.Callback callback, Theme.ResourcesProvider resourcesProvider, int i, View view, int i2) {
            if (i2 <= 1) {
                return;
            }
            final TLRPC.InputPeer inputPeer = (TLRPC.InputPeer) this.peers.get(i2 - 2);
            if (inputPeer.channel_id == 0 && inputPeer.chat_id == 0) {
                callback.run(inputPeer);
            } else {
                final AlertDialog alertDialog = new AlertDialog(getContext(), 3, resourcesProvider);
                alertDialog.showDelayed(200L);
                MessagesController.getInstance(i).getStoriesController().canSendStoryFor(DialogObject.getPeerDialogId(inputPeer), new Consumer() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$ChoosePeerSheet$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        StoryPrivacyBottomSheet.ChoosePeerSheet.lambda$new$0(AlertDialog.this, callback, inputPeer, (Boolean) obj);
                    }
                }, true, resourcesProvider);
            }
            dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float top() {
            int childAdapterPosition;
            float measuredHeight = this.containerView.getMeasuredHeight();
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt != null && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt)) != -1 && childAdapterPosition > 0) {
                    measuredHeight = Math.min(AndroidUtilities.lerp(measuredHeight, childAt.getY(), childAt.getAlpha()), measuredHeight);
                }
            }
            return measuredHeight;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        protected boolean canDismissWithSwipe() {
            return top() > ((float) ((int) (((float) AndroidUtilities.displaySize.y) * 0.5f)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ContainerView extends FrameLayout {
        private final AnimatedFloat isActionBar;
        private final Path path;
        private float top;

        public ContainerView(Context context) {
            super(context);
            this.isActionBar = new AnimatedFloat(this, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.path = new Path();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            View[] viewPages = StoryPrivacyBottomSheet.this.viewPager.getViewPages();
            this.top = 0.0f;
            for (View view : viewPages) {
                if (view != null) {
                    Page page = (Page) view;
                    this.top += page.top() * Utilities.clamp(1.0f - Math.abs(page.getTranslationX() / page.getMeasuredWidth()), 1.0f, 0.0f);
                    boolean unused = ((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible;
                    if (page.getVisibility() == 0) {
                        page.updateTops();
                    }
                }
            }
            float f = this.isActionBar.set(this.top <= ((float) AndroidUtilities.statusBarHeight) ? 1.0f : 0.0f);
            this.top = Math.max(AndroidUtilities.statusBarHeight, this.top) - (AndroidUtilities.statusBarHeight * f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(((BottomSheet) StoryPrivacyBottomSheet.this).backgroundPaddingLeft, this.top, getWidth() - ((BottomSheet) StoryPrivacyBottomSheet.this).backgroundPaddingLeft, getHeight() + AndroidUtilities.dp(8.0f));
            float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(14.0f), 0, f);
            canvas.drawRoundRect(rectF, lerp, lerp, StoryPrivacyBottomSheet.this.backgroundPaint);
            canvas.save();
            this.path.rewind();
            this.path.addRoundRect(rectF, lerp, lerp, Path.Direction.CW);
            canvas.clipPath(this.path);
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 || motionEvent.getY() >= this.top) {
                return super.dispatchTouchEvent(motionEvent);
            }
            StoryPrivacyBottomSheet.this.dismiss();
            return true;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
        }
    }

    /* loaded from: classes4.dex */
    public interface DoneCallback {
        void done(StoryPrivacy storyPrivacy, boolean z, boolean z2, TLRPC.InputPeer inputPeer, Runnable runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class HeaderCell extends FrameLayout {
        public BackDrawable backDrawable;
        private ImageView closeView;
        private final Paint dividerPaint;
        private Runnable onCloseClickListener;
        private final Theme.ResourcesProvider resourcesProvider;
        private TextView textView;

        public HeaderCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.dividerPaint = new Paint(1);
            this.resourcesProvider = resourcesProvider;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTypeface(AndroidUtilities.bold());
            this.textView.setTextSize(1, 20.0f);
            this.textView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
            TextView textView2 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 23, z ? 16.0f : 53.0f, 0.0f, z ? 53.0f : 16.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            this.closeView = imageView;
            BackDrawable backDrawable = new BackDrawable(false);
            this.backDrawable = backDrawable;
            imageView.setImageDrawable(backDrawable);
            this.backDrawable.setColor(-1);
            this.backDrawable.setRotatedColor(-1);
            this.backDrawable.setAnimationTime(220.0f);
            addView(this.closeView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 16, 16.0f, 0.0f, 16.0f, 0.0f));
            this.closeView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$HeaderCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StoryPrivacyBottomSheet.HeaderCell.this.lambda$new$0(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            Runnable runnable = this.onCloseClickListener;
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            this.dividerPaint.setColor(Theme.getColor(Theme.key_divider, this.resourcesProvider));
            canvas.drawRect(0.0f, getHeight() - AndroidUtilities.getShadowHeight(), getWidth(), getHeight(), this.dividerPaint);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
        }

        public void setCloseImageVisible(boolean z) {
            this.closeView.setVisibility(z ? 0 : 8);
            TextView textView = this.textView;
            boolean z2 = LocaleController.isRTL;
            textView.setLayoutParams(LayoutHelper.createFrame(-1, -2.0f, 23, (z2 || !z) ? 22.0f : 53.0f, 0.0f, (z2 && z) ? 53.0f : 22.0f, 0.0f));
        }

        public void setOnCloseClickListener(Runnable runnable) {
            this.onCloseClickListener = runnable;
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }
    }

    /* loaded from: classes4.dex */
    private static class HeaderCell2 extends LinearLayout {
        private final Theme.ResourcesProvider resourcesProvider;
        private final TextView subtitleTextView;
        private final TextView titleTextView;

        public HeaderCell2(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            setOrientation(1);
            this.resourcesProvider = resourcesProvider;
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
            textView.setTextSize(1, 20.0f);
            textView.setTypeface(AndroidUtilities.bold());
            addView(textView, LayoutHelper.createLinear(-1, -2, 55, 27, 16, 27, 0));
            TextView textView2 = new TextView(context);
            this.subtitleTextView = textView2;
            textView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider));
            textView2.setTextSize(1, 14.0f);
            addView(textView2, LayoutHelper.createLinear(-1, -2, 55, 27, 5, 27, 13));
        }

        public void setText(CharSequence charSequence, CharSequence charSequence2) {
            this.titleTextView.setText(charSequence);
            this.subtitleTextView.setText(charSequence2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ItemInner extends AdapterWithDiffUtils.Item {
        public TLRPC.Chat chat;
        public boolean checked;
        public Drawable drawable;
        public boolean halfChecked;
        public int padHeight;
        public boolean red;
        public int resId;
        public boolean sendAs;
        public int subtractHeight;
        public CharSequence text;
        public CharSequence text2;
        public int type;
        public int typeCount;
        public TLRPC.User user;

        private ItemInner(int i, boolean z) {
            super(i, z);
            this.padHeight = -1;
        }

        public static ItemInner asButton(CharSequence charSequence, Drawable drawable) {
            ItemInner itemInner = new ItemInner(9, false);
            itemInner.text = charSequence;
            itemInner.drawable = drawable;
            return itemInner;
        }

        public static ItemInner asChat(TLRPC.Chat chat, boolean z) {
            ItemInner itemInner = new ItemInner(3, true);
            itemInner.chat = chat;
            itemInner.checked = z;
            return itemInner;
        }

        public static ItemInner asCheck(CharSequence charSequence, int i, boolean z) {
            ItemInner itemInner = new ItemInner(7, false);
            itemInner.resId = i;
            itemInner.text = charSequence;
            itemInner.checked = z;
            return itemInner;
        }

        public static ItemInner asHeader() {
            return new ItemInner(0, false);
        }

        public static ItemInner asHeader2(CharSequence charSequence, CharSequence charSequence2) {
            ItemInner itemInner = new ItemInner(4, false);
            itemInner.text = charSequence;
            itemInner.text2 = charSequence2;
            return itemInner;
        }

        public static ItemInner asHeaderCell(CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(8, false);
            itemInner.text = charSequence;
            return itemInner;
        }

        public static ItemInner asNoUsers() {
            return new ItemInner(5, false);
        }

        public static ItemInner asPad() {
            return asPad(-1);
        }

        public static ItemInner asPad(int i) {
            ItemInner itemInner = new ItemInner(-1, false);
            itemInner.subtractHeight = i;
            return itemInner;
        }

        public static ItemInner asPadding(int i) {
            ItemInner itemInner = new ItemInner(-1, false);
            itemInner.padHeight = i;
            return itemInner;
        }

        public static ItemInner asSearchField() {
            return new ItemInner(1, false);
        }

        public static ItemInner asSection() {
            return new ItemInner(2, false);
        }

        public static ItemInner asShadow(CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(6, false);
            itemInner.text = charSequence;
            return itemInner;
        }

        public static ItemInner asType(int i, boolean z, int i2) {
            ItemInner itemInner = new ItemInner(3, false);
            itemInner.type = i;
            itemInner.checked = z;
            itemInner.typeCount = i2;
            return itemInner;
        }

        public static ItemInner asUser(TLRPC.User user, boolean z, boolean z2) {
            ItemInner itemInner = new ItemInner(3, true);
            itemInner.user = user;
            itemInner.checked = z;
            itemInner.halfChecked = z2;
            return itemInner;
        }

        public ItemInner asSendAs() {
            this.sendAs = true;
            return this;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ItemInner itemInner = (ItemInner) obj;
            int i = this.viewType;
            if (i != itemInner.viewType) {
                return false;
            }
            if (i != -1 || (this.subtractHeight == itemInner.subtractHeight && this.padHeight == itemInner.padHeight)) {
                if (i != 3 || (this.user == itemInner.user && this.chat == itemInner.chat && this.type == itemInner.type && this.typeCount == itemInner.typeCount && this.checked == itemInner.checked && this.red == itemInner.red && this.sendAs == itemInner.sendAs)) {
                    if (i != 0 || this.resId == itemInner.resId) {
                        if (i != 2 || TextUtils.equals(this.text, itemInner.text)) {
                            if (this.viewType != 8 || TextUtils.equals(this.text, itemInner.text)) {
                                if (this.viewType != 4 || (TextUtils.equals(this.text, itemInner.text) && TextUtils.equals(this.text2, itemInner.text2))) {
                                    if (this.viewType != 6 || (TextUtils.equals(this.text, itemInner.text) && this.resId == itemInner.resId)) {
                                        if (this.viewType != 7 || (this.resId == itemInner.resId && TextUtils.equals(this.text, itemInner.text) && this.checked == itemInner.checked)) {
                                            return this.viewType != 9 || (this.drawable == itemInner.drawable && TextUtils.equals(this.text, itemInner.text) && TextUtils.equals(this.text2, itemInner.text2));
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        public ItemInner red(boolean z) {
            this.red = z;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class Page extends FrameLayout implements View.OnClickListener, NotificationCenter.NotificationCenterDelegate {
        private Adapter adapter;
        private final ArrayList atTop;
        private final ButtonWithCounterView button;
        private final ButtonWithCounterView button2;
        private final ButtonContainer buttonContainer;
        private final LongSparseArray changelog;
        private boolean containsHeader;
        private final FrameLayout contentView;
        private HeaderCell headerView;
        private boolean isActionBar;
        private final ArrayList items;
        private int keyboardHeight;
        private boolean keyboardMoving;
        private int lastSelectedType;
        private LinearLayoutManager layoutManager;
        private RecyclerListView listView;
        private final ArrayList oldItems;
        public int pageType;
        private AlertDialog progressDialog;
        private String query;
        private boolean scrolling;
        private SearchUsersCell searchField;
        private ValueAnimator searchFieldAnimator;
        private int searchPosition;
        private boolean searchTranslationAnimating;
        private float searchTranslationAnimatingTo;
        private GraySectionCell sectionCell;
        private final ArrayList selectedUsers;
        private final HashMap selectedUsersByGroup;
        private final View underKeyboardView;
        private long waitingForChatId;
        public boolean wasAtBottom;
        public boolean wasAtTop;
        private boolean wasKeyboardVisible;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class Adapter extends AdapterWithDiffUtils {
            private Context context;
            private RecyclerListView listView;
            private Runnable onBack;
            private Theme.ResourcesProvider resourcesProvider;
            public boolean reversedLayout;
            private SearchUsersCell searchField;

            public Adapter(Context context, Theme.ResourcesProvider resourcesProvider, SearchUsersCell searchUsersCell, Runnable runnable) {
                this.context = context;
                this.resourcesProvider = resourcesProvider;
                this.searchField = searchUsersCell;
                this.onBack = runnable;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                if (Page.this.items == null) {
                    return 0;
                }
                return Page.this.items.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                if (Page.this.items == null || i < 0 || i >= Page.this.items.size()) {
                    return -1;
                }
                return ((ItemInner) Page.this.items.get(i)).viewType;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return (viewHolder.getItemViewType() == 3 && StoryPrivacyBottomSheet.this.canChangePeer) || viewHolder.getItemViewType() == 7 || viewHolder.getItemViewType() == 9;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                int i2;
                ArrayList arrayList;
                ItemInner itemInner;
                View view;
                RecyclerView.LayoutParams layoutParams;
                int i3;
                if (Page.this.items == null || i < 0 || i >= Page.this.items.size()) {
                    return;
                }
                ItemInner itemInner2 = (ItemInner) Page.this.items.get(i);
                int itemViewType = viewHolder.getItemViewType();
                boolean z = true;
                if (this.reversedLayout) {
                    if (i > 0) {
                        arrayList = Page.this.items;
                        i2 = i - 1;
                        itemInner = (ItemInner) arrayList.get(i2);
                    }
                    itemInner = null;
                } else {
                    i2 = i + 1;
                    if (i2 < Page.this.items.size()) {
                        arrayList = Page.this.items;
                        itemInner = (ItemInner) arrayList.get(i2);
                    }
                    itemInner = null;
                }
                boolean z2 = itemInner != null && itemInner.viewType == itemViewType;
                if (itemViewType == 3) {
                    UserCell userCell = (UserCell) viewHolder.itemView;
                    boolean z3 = itemInner2.sendAs;
                    userCell.setIsSendAs(z3, !z3);
                    int i4 = itemInner2.type;
                    float f = 1.0f;
                    if (i4 > 0) {
                        userCell.setType(i4, itemInner2.typeCount, itemInner2.user);
                    } else {
                        TLRPC.User user = itemInner2.user;
                        if (user == null) {
                            TLRPC.Chat chat = itemInner2.chat;
                            if (chat != null) {
                                userCell.setChat(chat, StoryPrivacyBottomSheet.this.getParticipantsCount(chat));
                            }
                            if (!itemInner2.checked && !itemInner2.halfChecked) {
                                z = false;
                            }
                            userCell.setChecked(z, false);
                            userCell.setDivider(z2);
                            userCell.setRedCheckbox(itemInner2.red);
                            userCell.drawArrow = StoryPrivacyBottomSheet.this.canChangePeer;
                        }
                        userCell.setUser(user);
                        if (itemInner2.halfChecked && !itemInner2.checked) {
                            f = 0.5f;
                        }
                    }
                    userCell.setCheckboxAlpha(f, false);
                    if (!itemInner2.checked) {
                        z = false;
                    }
                    userCell.setChecked(z, false);
                    userCell.setDivider(z2);
                    userCell.setRedCheckbox(itemInner2.red);
                    userCell.drawArrow = StoryPrivacyBottomSheet.this.canChangePeer;
                } else if (itemViewType == 2) {
                } else {
                    if (itemViewType == 0) {
                        view = viewHolder.itemView;
                        layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f));
                    } else if (itemViewType == -1) {
                        if (itemInner2.subtractHeight > 0) {
                            RecyclerListView recyclerListView = this.listView;
                            i3 = ((recyclerListView == null || recyclerListView.getMeasuredHeight() <= 0) ? AndroidUtilities.displaySize.y : this.listView.getMeasuredHeight() + Page.this.keyboardHeight) - itemInner2.subtractHeight;
                        } else {
                            i3 = itemInner2.padHeight;
                            if (i3 >= 0) {
                                viewHolder.itemView.setTag(null);
                                viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, i3));
                                return;
                            }
                            i3 = (int) (AndroidUtilities.displaySize.y * 0.3f);
                        }
                        viewHolder.itemView.setTag(33);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(-1, i3));
                        return;
                    } else if (itemViewType != 1) {
                        if (itemViewType == 4) {
                            ((HeaderCell2) viewHolder.itemView).setText(itemInner2.text, itemInner2.text2);
                            return;
                        } else if (itemViewType == 5) {
                            try {
                                ((StickerEmptyView) viewHolder.itemView).stickerView.getImageReceiver().startAnimation();
                                return;
                            } catch (Exception unused) {
                                return;
                            }
                        } else if (itemViewType == 6) {
                            TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                            if (itemInner2.text == null) {
                                textInfoPrivacyCell.setFixedSize(12);
                                textInfoPrivacyCell.setText(null);
                                return;
                            }
                            textInfoPrivacyCell.setFixedSize(0);
                            textInfoPrivacyCell.setText(itemInner2.text);
                            return;
                        } else if (itemViewType == 7) {
                            ((TextCell) viewHolder.itemView).setTextAndCheck(itemInner2.text, itemInner2.resId == 0 ? StoryPrivacyBottomSheet.this.allowScreenshots : StoryPrivacyBottomSheet.this.keepOnMyPage, z2);
                            return;
                        } else if (itemViewType != 9) {
                            if (itemViewType == 8) {
                                ((org.telegram.ui.Cells.HeaderCell) viewHolder.itemView).setText(itemInner2.text);
                                return;
                            }
                            return;
                        } else {
                            Drawable drawable = itemInner2.drawable;
                            TextCell textCell = (TextCell) viewHolder.itemView;
                            if (drawable != null) {
                                textCell.setTextAndValueDrawable(itemInner2.text, drawable, z2);
                                return;
                            } else {
                                textCell.setTextAndValue(itemInner2.text, itemInner2.text2, z2);
                                return;
                            }
                        }
                    } else {
                        view = viewHolder.itemView;
                        layoutParams = new RecyclerView.LayoutParams(-1, Math.min(AndroidUtilities.dp(150.0f), this.searchField.resultContainerHeight));
                    }
                    view.setLayoutParams(layoutParams);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View textCell;
                int i2;
                int i3;
                if (i == -1) {
                    textCell = new PadView(this.context);
                } else {
                    if (i == 0) {
                        textCell = new View(this.context);
                        i3 = 35;
                    } else if (i == 1) {
                        textCell = new View(this.context);
                        i3 = 34;
                    } else if (i == 3) {
                        textCell = new UserCell(this.context, this.resourcesProvider);
                    } else if (i == 4) {
                        textCell = new HeaderCell2(this.context, this.resourcesProvider);
                    } else {
                        if (i == 8) {
                            textCell = new org.telegram.ui.Cells.HeaderCell(this.context, this.resourcesProvider);
                            i2 = Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider);
                        } else if (i == 5) {
                            StickerEmptyView stickerEmptyView = new StickerEmptyView(this.context, null, 1, this.resourcesProvider);
                            stickerEmptyView.title.setText(LocaleController.getString(R.string.NoResult));
                            stickerEmptyView.subtitle.setText(LocaleController.getString(R.string.SearchEmptyViewFilteredSubtitle2));
                            stickerEmptyView.linearLayout.setTranslationY(AndroidUtilities.dp(24.0f));
                            textCell = stickerEmptyView;
                        } else if (i == 6) {
                            textCell = new TextInfoPrivacyCell(this.context, this.resourcesProvider);
                            i2 = -15921907;
                        } else {
                            textCell = i == 7 ? new TextCell(this.context, 23, true, true, this.resourcesProvider) : i == 9 ? new TextCell(this.context, 23, true, false, this.resourcesProvider) : new View(this.context) { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.Adapter.1
                                @Override // android.view.View
                                protected void onMeasure(int i4, int i5) {
                                    super.onMeasure(i4, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                                }
                            };
                        }
                        textCell.setBackgroundColor(i2);
                    }
                    textCell.setTag(Integer.valueOf(i3));
                }
                return new RecyclerListView.Holder(textCell);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class ButtonContainer extends LinearLayout {
            final AnimatedFloat alpha;
            private ValueAnimator animator;
            final Paint dividerPaint;
            private ValueAnimator hideAnimator;
            private float translationY;
            private float translationY2;

            public ButtonContainer(Context context) {
                super(context);
                this.dividerPaint = new Paint(1);
                this.alpha = new AnimatedFloat(this);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$hide$0(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                this.translationY2 = floatValue;
                super.setTranslationY(floatValue + this.translationY);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$translateY$1(ValueAnimator valueAnimator) {
                setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                this.dividerPaint.setColor(Theme.getColor(Theme.key_windowBackgroundGray, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider));
                this.dividerPaint.setAlpha((int) (this.alpha.set(Page.this.listView.canScrollVertically(1) ? 1.0f : 0.0f) * 255.0f));
                canvas.drawRect(0.0f, 0.0f, getWidth(), 1.0f, this.dividerPaint);
            }

            public void hide(final boolean z, boolean z2) {
                ValueAnimator valueAnimator = this.hideAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                if (!z2) {
                    setVisibility(z ? 8 : 0);
                    float measuredHeight = z ? getMeasuredHeight() : 0.0f;
                    this.translationY2 = measuredHeight;
                    super.setTranslationY(measuredHeight + this.translationY);
                    return;
                }
                setVisibility(0);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.translationY2, z ? getMeasuredHeight() : 0.0f);
                this.hideAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$ButtonContainer$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        StoryPrivacyBottomSheet.Page.ButtonContainer.this.lambda$hide$0(valueAnimator2);
                    }
                });
                this.hideAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.ButtonContainer.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (z) {
                            ButtonContainer.this.setVisibility(8);
                        }
                        ButtonContainer.this.hideAnimator = null;
                    }
                });
                this.hideAnimator.setDuration(320L);
                this.hideAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.hideAnimator.start();
            }

            @Override // android.view.View
            public void setTranslationY(float f) {
                float f2 = this.translationY2;
                this.translationY = f;
                super.setTranslationY(f2 + f);
            }

            public void translateY(float f, final float f2) {
                ValueAnimator valueAnimator = this.animator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.animator = null;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(f, f2);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$ButtonContainer$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        StoryPrivacyBottomSheet.Page.ButtonContainer.this.lambda$translateY$1(valueAnimator2);
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.ButtonContainer.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ButtonContainer.this.setTranslationY(f2);
                        ButtonContainer.this.animator = null;
                    }
                });
                this.animator.setDuration(250L);
                this.animator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                this.animator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class PadView extends View {
            public PadView(Context context) {
                super(context);
            }
        }

        public Page(final Context context) {
            super(context);
            this.changelog = new LongSparseArray();
            this.selectedUsers = new ArrayList();
            this.selectedUsersByGroup = new HashMap();
            this.searchPosition = -1;
            this.atTop = new ArrayList();
            this.oldItems = new ArrayList();
            this.items = new ArrayList();
            this.lastSelectedType = -1;
            this.sectionCell = new GraySectionCell(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
            SearchUsersCell searchUsersCell = new SearchUsersCell(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider, new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    StoryPrivacyBottomSheet.Page.this.lambda$new$0();
                }
            }) { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.1
                {
                    StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                }

                @Override // org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell
                public void setContainerHeight(float f) {
                    super.setContainerHeight(f);
                    Page.this.sectionCell.setTranslationY(((getY() - (Page.this.contentView == null ? 0 : Page.this.contentView.getPaddingTop())) + Math.min(AndroidUtilities.dp(150.0f), this.containerHeight)) - 1.0f);
                    if (Page.this.contentView != null) {
                        Page.this.contentView.invalidate();
                    }
                }

                @Override // android.view.View
                public void setTranslationY(float f) {
                    super.setTranslationY(f);
                    Page.this.sectionCell.setTranslationY(((getY() - (Page.this.contentView == null ? 0 : Page.this.contentView.getPaddingTop())) + Math.min(AndroidUtilities.dp(150.0f), this.containerHeight)) - 1.0f);
                    if (Page.this.contentView != null) {
                        Page.this.contentView.invalidate();
                    }
                }
            };
            this.searchField = searchUsersCell;
            int i = Theme.key_dialogBackground;
            searchUsersCell.setBackgroundColor(StoryPrivacyBottomSheet.this.getThemedColor(i));
            this.searchField.setOnSearchTextChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    StoryPrivacyBottomSheet.Page.this.onSearch((String) obj);
                }
            });
            HeaderCell headerCell = new HeaderCell(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
            this.headerView = headerCell;
            headerCell.setOnCloseClickListener(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    StoryPrivacyBottomSheet.Page.this.lambda$new$1();
                }
            });
            FrameLayout frameLayout = new FrameLayout(context);
            this.contentView = frameLayout;
            frameLayout.setPadding(0, AndroidUtilities.statusBarHeight + AndroidUtilities.dp(56.0f), 0, 0);
            frameLayout.setClipToPadding(true);
            addView(frameLayout, LayoutHelper.createFrame(-1, -1, 119));
            RecyclerListView recyclerListView = new RecyclerListView(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
            this.listView = recyclerListView;
            recyclerListView.setClipToPadding(false);
            this.listView.setTranslateSelector(true);
            RecyclerListView recyclerListView2 = this.listView;
            Adapter adapter = new Adapter(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider, this.searchField, new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    StoryPrivacyBottomSheet.this.onBackPressed();
                }
            });
            this.adapter = adapter;
            recyclerListView2.setAdapter(adapter);
            this.adapter.listView = this.listView;
            RecyclerListView recyclerListView3 = this.listView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            this.layoutManager = linearLayoutManager;
            recyclerListView3.setLayoutManager(linearLayoutManager);
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.2
                private boolean canScrollDown;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i2) {
                    if (i2 == 1 && ((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible && Page.this.searchField != null) {
                        StoryPrivacyBottomSheet.this.closeKeyboard();
                    }
                    if (i2 == 0) {
                        Page page = Page.this;
                        page.wasAtTop = page.atTop();
                        Page page2 = Page.this;
                        page2.wasAtBottom = page2.atBottom();
                    }
                    Page.this.scrolling = i2 != 0;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                    boolean canScrollVertically = Page.this.listView.canScrollVertically(1);
                    if (canScrollVertically != this.canScrollDown) {
                        Page.this.buttonContainer.invalidate();
                        this.canScrollDown = canScrollVertically;
                    }
                    Page.this.contentView.invalidate();
                    ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
                    Page page = Page.this;
                    if (page.pageType != 6 || page.listView.getChildCount() <= 0 || Page.this.listView.getChildAdapterPosition(Page.this.listView.getChildAt(0)) < MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().blocklist.size()) {
                        return;
                    }
                    MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().loadBlocklist(false);
                }
            });
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda11
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i2, float f, float f2) {
                    StoryPrivacyBottomSheet.Page.this.lambda$new$7(context, view, i2, f, f2);
                }
            });
            frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.3
                @Override // androidx.recyclerview.widget.SimpleItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
                public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                protected void onAddAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
                    Page.this.contentView.invalidate();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                public void onChangeAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
                    Page.this.contentView.invalidate();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
                    Page.this.contentView.invalidate();
                    Page.this.listView.invalidate();
                }

                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                protected void onRemoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
                    Page.this.contentView.invalidate();
                }
            };
            defaultItemAnimator.setDurations(350L);
            defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setSupportsChangeAnimations(false);
            this.listView.setItemAnimator(defaultItemAnimator);
            frameLayout.addView(this.searchField, LayoutHelper.createFrame(-1, -2, 55));
            frameLayout.addView(this.sectionCell, LayoutHelper.createFrame(-1, 32, 55));
            addView(this.headerView, LayoutHelper.createFrame(-1, -2, 55));
            ButtonContainer buttonContainer = new ButtonContainer(context);
            this.buttonContainer = buttonContainer;
            buttonContainer.setClickable(true);
            buttonContainer.setOrientation(1);
            buttonContainer.setPadding(AndroidUtilities.dp(10.0f) + ((BottomSheet) StoryPrivacyBottomSheet.this).backgroundPaddingLeft, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f) + ((BottomSheet) StoryPrivacyBottomSheet.this).backgroundPaddingLeft, AndroidUtilities.dp(10.0f));
            buttonContainer.setBackgroundColor(Theme.getColor(i, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider));
            ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
            this.button = buttonWithCounterView;
            buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda12
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StoryPrivacyBottomSheet.Page.this.onButton1Click(view);
                }
            });
            buttonContainer.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 87));
            ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, false, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
            this.button2 = buttonWithCounterView2;
            buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StoryPrivacyBottomSheet.Page.this.onButton2Click(view);
                }
            });
            buttonContainer.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48, 87, 0, 8, 0, 0));
            View view = new View(context);
            this.underKeyboardView = view;
            view.setBackgroundColor(Theme.getColor(i, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider));
            addView(view, LayoutHelper.createFrame(-1, 500.0f, 87, 0.0f, 0.0f, 0.0f, -500.0f));
            addView(buttonContainer, LayoutHelper.createFrame(-1, -2, 87));
        }

        private float getSearchFieldTop() {
            float f = -Math.max(0, Math.min(AndroidUtilities.dp(150.0f), this.searchField.resultContainerHeight) - AndroidUtilities.dp(150.0f));
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                if ((childAt.getTag() instanceof Integer) && ((Integer) childAt.getTag()).intValue() == 34) {
                    return Math.max(f, childAt.getY());
                }
            }
            return f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            this.adapter.notifyItemChanged(2);
            this.listView.forceLayout();
            updateTops();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            if (this.pageType == 0) {
                StoryPrivacyBottomSheet.this.dismiss();
            } else {
                StoryPrivacyBottomSheet.this.onBackPressed();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(TLRPC.InputPeer inputPeer) {
            StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
            storyPrivacyBottomSheet.selectedPeer = inputPeer;
            if (storyPrivacyBottomSheet.onSelectedPeer != null) {
                StoryPrivacyBottomSheet.this.onSelectedPeer.run(StoryPrivacyBottomSheet.this.selectedPeer);
            }
            updateItems(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(long j, TLRPC.TL_channels_channelParticipants tL_channels_channelParticipants) {
            int i;
            long j2;
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                alertDialog.dismissUnless(350L);
                this.progressDialog = null;
            }
            if (tL_channels_channelParticipants == null || tL_channels_channelParticipants.participants.isEmpty()) {
                return;
            }
            TLRPC.TL_chatParticipants tL_chatParticipants = new TLRPC.TL_chatParticipants();
            while (i < tL_channels_channelParticipants.participants.size()) {
                TLRPC.ChannelParticipant channelParticipant = tL_channels_channelParticipants.participants.get(i);
                TLRPC.TL_chatParticipant tL_chatParticipant = new TLRPC.TL_chatParticipant();
                TLRPC.Peer peer = channelParticipant.peer;
                if (peer != null) {
                    j2 = DialogObject.getPeerDialogId(peer);
                    i = j2 < 0 ? i + 1 : 0;
                } else {
                    j2 = channelParticipant.user_id;
                }
                tL_chatParticipant.user_id = j2;
                tL_chatParticipants.participants.add(tL_chatParticipant);
            }
            selectChat(j, tL_chatParticipants);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(boolean z, final long j) {
            if (z) {
                MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).loadChannelParticipants(Long.valueOf(j), new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda0
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        StoryPrivacyBottomSheet.Page.this.lambda$new$3(j, (TLRPC.TL_channels_channelParticipants) obj);
                    }
                }, NotificationCenter.storyQualityUpdate);
            } else {
                MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).loadFullChat(j, 0, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(long j, TLRPC.ChatFull chatFull) {
            selectChat(j, chatFull.participants);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(TLRPC.Chat chat, MessagesStorage messagesStorage, final long j) {
            TLRPC.ChatParticipants chatParticipants;
            ArrayList<TLRPC.ChatParticipant> arrayList;
            final boolean isChannel = ChatObject.isChannel(chat);
            final TLRPC.ChatFull loadChatInfoInQueue = messagesStorage.loadChatInfoInQueue(j, isChannel, true, true, 0);
            if (loadChatInfoInQueue == null || (chatParticipants = loadChatInfoInQueue.participants) == null || ((arrayList = chatParticipants.participants) != null && arrayList.size() < loadChatInfoInQueue.participants_count - 1)) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda21
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryPrivacyBottomSheet.Page.this.lambda$new$4(isChannel, j);
                    }
                });
            } else {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryPrivacyBottomSheet.Page.this.lambda$new$5(j, loadChatInfoInQueue);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$7(Context context, View view, int i, float f, float f2) {
            BulletinFactory of;
            int i2;
            int i3;
            BulletinFactory of2;
            int i4;
            int i5;
            LongSparseArray longSparseArray;
            Boolean bool;
            TLRPC.ChatParticipants chatParticipants;
            ArrayList<TLRPC.ChatParticipant> arrayList;
            if (i < 0 || i >= this.items.size()) {
                return;
            }
            ItemInner itemInner = (ItemInner) this.items.get(i);
            int i6 = itemInner.viewType;
            if (i6 != 3) {
                if (i6 != 7) {
                    if (i6 != 9 || StoryPrivacyBottomSheet.this.whenCoverClicked == null) {
                        return;
                    }
                    StoryPrivacyBottomSheet.this.whenCoverClicked.run();
                } else if (view instanceof TextCell) {
                    TextCell textCell = (TextCell) view;
                    textCell.setChecked(!textCell.isChecked());
                    itemInner.checked = textCell.isChecked();
                    if (itemInner.resId == 0) {
                        StoryPrivacyBottomSheet.this.allowScreenshots = textCell.isChecked();
                        boolean z = StoryPrivacyBottomSheet.this.selectedType == 4;
                        if (StoryPrivacyBottomSheet.this.allowScreenshots) {
                            StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                            of2 = BulletinFactory.of(storyPrivacyBottomSheet.container, ((BottomSheet) storyPrivacyBottomSheet).resourcesProvider);
                            i4 = R.raw.ic_save_to_gallery;
                            i5 = z ? R.string.StoryEnabledScreenshotsShare : R.string.StoryEnabledScreenshots;
                        } else {
                            StoryPrivacyBottomSheet storyPrivacyBottomSheet2 = StoryPrivacyBottomSheet.this;
                            of2 = BulletinFactory.of(storyPrivacyBottomSheet2.container, ((BottomSheet) storyPrivacyBottomSheet2).resourcesProvider);
                            i4 = R.raw.passcode_lock_close;
                            i5 = z ? R.string.StoryDisabledScreenshotsShare : R.string.StoryDisabledScreenshots;
                        }
                        of2.createSimpleBulletin(i4, LocaleController.getString(i5), 4).setDuration(5000).show(true);
                        return;
                    }
                    StoryPrivacyBottomSheet.this.keepOnMyPage = textCell.isChecked();
                    StoryPrivacyBottomSheet storyPrivacyBottomSheet3 = StoryPrivacyBottomSheet.this;
                    boolean z2 = storyPrivacyBottomSheet3.selectedPeer instanceof TLRPC.TL_inputPeerChannel;
                    if (storyPrivacyBottomSheet3.keepOnMyPage) {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet4 = StoryPrivacyBottomSheet.this;
                        of = BulletinFactory.of(storyPrivacyBottomSheet4.container, ((BottomSheet) storyPrivacyBottomSheet4).resourcesProvider);
                        i2 = R.raw.msg_story_keep;
                        i3 = z2 ? R.string.StoryChannelEnableKeep : R.string.StoryEnableKeep;
                    } else {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet5 = StoryPrivacyBottomSheet.this;
                        of = BulletinFactory.of(storyPrivacyBottomSheet5.container, ((BottomSheet) storyPrivacyBottomSheet5).resourcesProvider);
                        i2 = R.raw.fire_on;
                        i3 = z2 ? R.string.StoryChannelDisableKeep : R.string.StoryDisableKeep;
                    }
                    of.createSimpleBulletin(i2, LocaleController.getString(i3), 4).setDuration(5000).show(true);
                    if (StoryPrivacyBottomSheet.this.whenCoverClicked != null) {
                        updateItems(true);
                    }
                }
            } else if (itemInner.sendAs && StoryPrivacyBottomSheet.this.canChangePeer) {
                new ChoosePeerSheet(context, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, StoryPrivacyBottomSheet.this.selectedPeer, new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda17
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        StoryPrivacyBottomSheet.Page.this.lambda$new$2((TLRPC.InputPeer) obj);
                    }
                }, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider).show();
            } else {
                int i7 = itemInner.type;
                if (i7 == 1) {
                    if (StoryPrivacyBottomSheet.this.selectedType == 1 || StoryPrivacyBottomSheet.this.getCloseFriends().isEmpty()) {
                        StoryPrivacyBottomSheet.this.activePage = 1;
                        StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
                    }
                    StoryPrivacyBottomSheet.this.selectedType = 1;
                    updateCheckboxes(true);
                } else if (i7 == 3) {
                    if (StoryPrivacyBottomSheet.this.selectedType == 3 || (StoryPrivacyBottomSheet.this.selectedContacts.isEmpty() && StoryPrivacyBottomSheet.this.selectedContactsByGroup.isEmpty())) {
                        StoryPrivacyBottomSheet.this.activePage = 3;
                        StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
                    }
                    StoryPrivacyBottomSheet.this.selectedType = 3;
                    updateCheckboxes(true);
                } else if (i7 == 2) {
                    if (StoryPrivacyBottomSheet.this.selectedType == 2) {
                        StoryPrivacyBottomSheet.this.activePage = 2;
                        StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
                    }
                    StoryPrivacyBottomSheet.this.selectedType = 2;
                    updateCheckboxes(true);
                } else if (i7 == 4) {
                    if (StoryPrivacyBottomSheet.this.selectedType == 4) {
                        StoryPrivacyBottomSheet.this.activePage = 4;
                        StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
                    }
                    StoryPrivacyBottomSheet.this.selectedType = 4;
                    updateCheckboxes(true);
                } else {
                    if (i7 > 0) {
                        this.selectedUsers.clear();
                        this.selectedUsersByGroup.clear();
                        StoryPrivacyBottomSheet.this.selectedType = itemInner.type;
                        this.searchField.spansContainer.removeAllSpans(true);
                    } else {
                        TLRPC.Chat chat = itemInner.chat;
                        if (chat != null) {
                            final long j = chat.id;
                            if (StoryPrivacyBottomSheet.this.getParticipantsCount(chat) > 200) {
                                try {
                                    performHapticFeedback(3, 1);
                                } catch (Throwable unused) {
                                }
                                new AlertDialog.Builder(getContext(), ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider).setTitle(LocaleController.getString(R.string.GroupTooLarge)).setMessage(LocaleController.getString(R.string.GroupTooLargeMessage)).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                            } else if (this.selectedUsersByGroup.containsKey(Long.valueOf(j))) {
                                ArrayList arrayList2 = (ArrayList) this.selectedUsersByGroup.get(Long.valueOf(j));
                                if (arrayList2 != null) {
                                    Iterator it = arrayList2.iterator();
                                    while (it.hasNext()) {
                                        this.changelog.put(((Long) it.next()).longValue(), Boolean.FALSE);
                                    }
                                }
                                this.selectedUsersByGroup.remove(Long.valueOf(j));
                                updateSpans(true);
                            } else {
                                final TLRPC.Chat chat2 = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getChat(Long.valueOf(j));
                                TLRPC.ChatFull chatFull = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getChatFull(j);
                                if (chatFull == null || (chatParticipants = chatFull.participants) == null || (arrayList = chatParticipants.participants) == null || arrayList.isEmpty() || chatFull.participants.participants.size() < chatFull.participants_count - 1) {
                                    AlertDialog alertDialog = this.progressDialog;
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                        this.progressDialog = null;
                                    }
                                    this.waitingForChatId = j;
                                    AlertDialog alertDialog2 = new AlertDialog(getContext(), 3, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
                                    this.progressDialog = alertDialog2;
                                    alertDialog2.showDelayed(50L);
                                    final MessagesStorage messagesStorage = MessagesStorage.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount);
                                    messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda18
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            StoryPrivacyBottomSheet.Page.this.lambda$new$6(chat2, messagesStorage, j);
                                        }
                                    });
                                } else {
                                    selectChat(j, chatFull.participants);
                                }
                                if (!TextUtils.isEmpty(this.query)) {
                                    this.searchField.setText("");
                                    this.query = null;
                                    updateItems(false);
                                }
                            }
                        } else if (itemInner.user != null) {
                            if (this.pageType == 0) {
                                StoryPrivacyBottomSheet.this.selectedType = 0;
                            }
                            long j2 = itemInner.user.id;
                            HashSet hashSet = new HashSet(this.selectedUsers);
                            if (this.selectedUsers.contains(Long.valueOf(j2))) {
                                Iterator it2 = this.selectedUsersByGroup.entrySet().iterator();
                                while (it2.hasNext()) {
                                    Map.Entry entry = (Map.Entry) it2.next();
                                    if (((ArrayList) entry.getValue()).contains(Long.valueOf(j2))) {
                                        it2.remove();
                                        hashSet.addAll((Collection) entry.getValue());
                                    }
                                }
                                hashSet.remove(Long.valueOf(j2));
                                longSparseArray = this.changelog;
                                bool = Boolean.FALSE;
                            } else {
                                Iterator it3 = this.selectedUsersByGroup.entrySet().iterator();
                                while (it3.hasNext()) {
                                    Map.Entry entry2 = (Map.Entry) it3.next();
                                    if (((ArrayList) entry2.getValue()).contains(Long.valueOf(j2))) {
                                        it3.remove();
                                        hashSet.addAll((Collection) entry2.getValue());
                                    }
                                }
                                hashSet.add(Long.valueOf(j2));
                                if (!TextUtils.isEmpty(this.query)) {
                                    this.searchField.setText("");
                                    this.query = null;
                                    updateItems(false);
                                }
                                longSparseArray = this.changelog;
                                bool = Boolean.TRUE;
                            }
                            longSparseArray.put(j2, bool);
                            this.selectedUsers.clear();
                            this.selectedUsers.addAll(hashSet);
                            updateSpans(true);
                        }
                    }
                    updateCheckboxes(true);
                    updateButton(true);
                    this.searchField.scrollToBottom();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onButton1Click$10(final MessagesController messagesController, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    StoryPrivacyBottomSheet.Page.this.lambda$onButton1Click$9(tLObject, messagesController);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onButton1Click$11() {
            Bulletin.removeDelegate(StoryPrivacyBottomSheet.this.container);
            StoryPrivacyBottomSheet.super.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onButton1Click$12() {
            this.button.setLoading(false);
            StoryPrivacyBottomSheet.this.closeKeyboard();
            StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onButton1Click$9(TLObject tLObject, MessagesController messagesController) {
            boolean contains;
            this.button.setLoading(false);
            if (tLObject != null) {
                ArrayList contacts = StoryPrivacyBottomSheet.this.getContacts();
                for (int i = 0; i < contacts.size(); i++) {
                    TLRPC.User user = (TLRPC.User) contacts.get(i);
                    if (user != null && (contains = this.selectedUsers.contains(Long.valueOf(user.id))) != user.close_friend) {
                        user.close_friend = contains;
                        user.flags2 = contains ? user.flags2 | 4 : user.flags2 & (-5);
                        messagesController.putUser(user, false);
                    }
                }
            }
            StoryPrivacyBottomSheet.this.closeKeyboard();
            if (StoryPrivacyBottomSheet.this.isEdit) {
                StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                storyPrivacyBottomSheet.done(new StoryPrivacy(1, ((BottomSheet) storyPrivacyBottomSheet).currentAccount, (ArrayList) null), new StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda1(StoryPrivacyBottomSheet.this));
                return;
            }
            StoryPrivacyBottomSheet.this.closeKeyboard();
            StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onButton2Click$13(ArrayList arrayList) {
            StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
            storyPrivacyBottomSheet.done(new StoryPrivacy(5, ((BottomSheet) storyPrivacyBottomSheet).currentAccount, arrayList), new StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda1(StoryPrivacyBottomSheet.this));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$17() {
            this.keyboardMoving = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$selectChat$8(long j, ArrayList arrayList, DialogInterface dialogInterface, int i) {
            this.selectedUsersByGroup.put(Long.valueOf(j), arrayList);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.changelog.put(((Long) it.next()).longValue(), Boolean.TRUE);
            }
            updateSpans(true);
            updateButton(true);
            updateCheckboxes(true);
            dialogInterface.dismiss();
            this.searchField.scrollToBottom();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateItems$14() {
            StoryPrivacyBottomSheet.this.activePage = 6;
            StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchFieldTop$15(ValueAnimator valueAnimator) {
            this.searchField.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSectionCell$16(View view) {
            Iterator it = this.selectedUsers.iterator();
            while (it.hasNext()) {
                this.changelog.put(((Long) it.next()).longValue(), Boolean.FALSE);
            }
            for (ArrayList arrayList : this.selectedUsersByGroup.values()) {
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    this.changelog.put(((Long) it2.next()).longValue(), Boolean.FALSE);
                }
            }
            this.selectedUsers.clear();
            this.selectedUsersByGroup.clear();
            StoryPrivacyBottomSheet.this.messageUsers.clear();
            this.searchField.spansContainer.removeAllSpans(true);
            updateCheckboxes(true);
            updateButton(true);
        }

        private boolean match(TLObject tLObject, String str) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            if (tLObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) tLObject;
                String lowerCase = AndroidUtilities.translitSafe(UserObject.getUserName(user)).toLowerCase();
                if (!lowerCase.startsWith(str)) {
                    if (!lowerCase.contains(" " + str)) {
                        String lowerCase2 = AndroidUtilities.translitSafe(UserObject.getPublicUsername(user)).toLowerCase();
                        if (!lowerCase2.startsWith(str)) {
                            if (!lowerCase2.contains(" " + str)) {
                                ArrayList<TLRPC.TL_username> arrayList = user.usernames;
                                if (arrayList != null) {
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        TLRPC.TL_username tL_username = arrayList.get(i);
                                        if (tL_username.active && AndroidUtilities.translitSafe(tL_username.username).toLowerCase().startsWith(str)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            } else if (tLObject instanceof TLRPC.Chat) {
                TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                String lowerCase3 = AndroidUtilities.translitSafe(chat.title).toLowerCase();
                if (!lowerCase3.startsWith(str)) {
                    if (!lowerCase3.contains(" " + str)) {
                        String lowerCase4 = AndroidUtilities.translitSafe(ChatObject.getPublicUsername(chat)).toLowerCase();
                        if (!lowerCase4.startsWith(str)) {
                            if (!lowerCase4.contains(" " + str)) {
                                ArrayList<TLRPC.TL_username> arrayList2 = chat.usernames;
                                if (arrayList2 != null) {
                                    for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                                        TLRPC.TL_username tL_username2 = arrayList2.get(i2);
                                        if (tL_username2.active && AndroidUtilities.translitSafe(tL_username2.username).toLowerCase().startsWith(str)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onButton1Click(View view) {
            StoryPrivacy storyPrivacy;
            StoryPrivacyBottomSheet storyPrivacyBottomSheet;
            Runnable runnable;
            HashMap hashMap;
            HashMap hashMap2;
            if (this.button.isLoading()) {
                return;
            }
            final MessagesController messagesController = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount);
            int i = this.pageType;
            if (i != 5) {
                if (i == 1) {
                    TLRPC.TL_editCloseFriends tL_editCloseFriends = new TLRPC.TL_editCloseFriends();
                    tL_editCloseFriends.id.addAll(this.selectedUsers);
                    this.button.setLoading(true);
                    ConnectionsManager.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).sendRequest(tL_editCloseFriends, new RequestDelegate() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda14
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            StoryPrivacyBottomSheet.Page.this.lambda$onButton1Click$10(messagesController, tLObject, tL_error);
                        }
                    });
                    return;
                }
                if (i != 0) {
                    if (i == 2) {
                        if (StoryPrivacyBottomSheet.this.isEdit) {
                            StoryPrivacyBottomSheet.this.closeKeyboard();
                            StoryPrivacyBottomSheet storyPrivacyBottomSheet2 = StoryPrivacyBottomSheet.this;
                            storyPrivacyBottomSheet2.done(new StoryPrivacy(2, ((BottomSheet) storyPrivacyBottomSheet2).currentAccount, this.selectedUsers), new StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda1(StoryPrivacyBottomSheet.this));
                            return;
                        }
                    } else if (i == 3) {
                        if (StoryPrivacyBottomSheet.this.isEdit) {
                            HashSet mergeUsers = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
                            if (mergeUsers.isEmpty()) {
                                return;
                            }
                            StoryPrivacyBottomSheet.this.closeKeyboard();
                            storyPrivacy = new StoryPrivacy(3, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, new ArrayList(mergeUsers));
                            storyPrivacy.selectedUserIds.clear();
                            storyPrivacy.selectedUserIds.addAll(this.selectedUsers);
                            storyPrivacy.selectedUserIdsByGroup.clear();
                            storyPrivacy.selectedUserIdsByGroup.putAll(this.selectedUsersByGroup);
                            storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                            runnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda15
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StoryPrivacyBottomSheet.Page.this.lambda$onButton1Click$11();
                                }
                            };
                        } else if (StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup).isEmpty()) {
                            return;
                        } else {
                            StoryPrivacyBottomSheet.this.selectedType = 3;
                        }
                    } else if (i == 6) {
                        HashSet mergeUsers2 = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
                        this.button.setLoading(true);
                        MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().updateBlockedUsers(mergeUsers2, new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda16
                            @Override // java.lang.Runnable
                            public final void run() {
                                StoryPrivacyBottomSheet.Page.this.lambda$onButton1Click$12();
                            }
                        });
                        return;
                    } else {
                        StoryPrivacyBottomSheet.this.selectedType = i;
                    }
                    StoryPrivacyBottomSheet.this.closeKeyboard();
                    StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(0);
                    return;
                } else if (!StoryPrivacyBottomSheet.this.applyWhenDismiss) {
                    if (StoryPrivacyBottomSheet.this.selectedType == 3) {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet3 = StoryPrivacyBottomSheet.this;
                        storyPrivacy = new StoryPrivacy(StoryPrivacyBottomSheet.this.selectedType, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, new ArrayList(storyPrivacyBottomSheet3.mergeUsers(storyPrivacyBottomSheet3.selectedContacts, StoryPrivacyBottomSheet.this.selectedContactsByGroup)));
                        storyPrivacy.selectedUserIds.clear();
                        storyPrivacy.selectedUserIds.addAll(StoryPrivacyBottomSheet.this.selectedContacts);
                        storyPrivacy.selectedUserIdsByGroup.clear();
                        hashMap = storyPrivacy.selectedUserIdsByGroup;
                        hashMap2 = StoryPrivacyBottomSheet.this.selectedContactsByGroup;
                    } else {
                        if (StoryPrivacyBottomSheet.this.selectedType == 2) {
                            storyPrivacy = new StoryPrivacy(StoryPrivacyBottomSheet.this.selectedType, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, StoryPrivacyBottomSheet.this.excludedContacts);
                        } else if (StoryPrivacyBottomSheet.this.selectedType == 4) {
                            StoryPrivacyBottomSheet storyPrivacyBottomSheet4 = StoryPrivacyBottomSheet.this;
                            storyPrivacy = new StoryPrivacy(StoryPrivacyBottomSheet.this.selectedType, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, new ArrayList(storyPrivacyBottomSheet4.mergeUsers(storyPrivacyBottomSheet4.excludedEveryone, StoryPrivacyBottomSheet.this.excludedEveryoneByGroup)));
                            storyPrivacy.selectedUserIds.clear();
                            storyPrivacy.selectedUserIds.addAll(StoryPrivacyBottomSheet.this.excludedEveryone);
                            storyPrivacy.selectedUserIdsByGroup.clear();
                            hashMap = storyPrivacy.selectedUserIdsByGroup;
                            hashMap2 = StoryPrivacyBottomSheet.this.excludedEveryoneByGroup;
                        } else {
                            storyPrivacy = new StoryPrivacy(StoryPrivacyBottomSheet.this.selectedType, ((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount, (ArrayList) null);
                        }
                        storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                        runnable = new StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda1(storyPrivacyBottomSheet);
                    }
                    hashMap.putAll(hashMap2);
                    storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                    runnable = new StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda1(storyPrivacyBottomSheet);
                }
                storyPrivacyBottomSheet.done(storyPrivacy, runnable);
                return;
            } else if (StoryPrivacyBottomSheet.this.onDone2 != null) {
                StoryPrivacyBottomSheet.this.onDone2.run(this.selectedUsers);
            }
            StoryPrivacyBottomSheet.this.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onButton2Click(View view) {
            if (StoryPrivacyBottomSheet.this.startedFromSendAsMessage) {
                StoryPrivacyBottomSheet.this.activePage = 5;
                StoryPrivacyBottomSheet.this.viewPager.scrollToPosition(1);
                return;
            }
            StoryPrivacyBottomSheet whenSelectedShare = new StoryPrivacyBottomSheet(5, getContext(), ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider).whenSelectedShare(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda19
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    StoryPrivacyBottomSheet.Page.this.lambda$onButton2Click$13((ArrayList) obj);
                }
            });
            whenSelectedShare.storyPeriod = StoryPrivacyBottomSheet.this.storyPeriod;
            whenSelectedShare.show();
        }

        private void selectChat(final long j, TLRPC.ChatParticipants chatParticipants) {
            AlertDialog.Builder positiveButton;
            final ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i = this.pageType;
            boolean z = i == 1 || i == 2;
            if (chatParticipants != null && chatParticipants.participants != null) {
                for (int i2 = 0; i2 < chatParticipants.participants.size(); i2++) {
                    long j2 = chatParticipants.participants.get(i2).user_id;
                    TLRPC.User user = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getUser(Long.valueOf(j2));
                    if (user != null && !UserObject.isUserSelf(user) && !user.bot && user.id != 777000 && j2 != 0) {
                        if (!z || user.contact) {
                            arrayList.add(Long.valueOf(j2));
                        } else {
                            arrayList2.add(Long.valueOf(j2));
                        }
                        this.selectedUsers.remove(Long.valueOf(j2));
                    }
                }
            }
            if (arrayList2.isEmpty()) {
                this.selectedUsersByGroup.put(Long.valueOf(j), arrayList);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    this.changelog.put(((Long) it.next()).longValue(), Boolean.TRUE);
                }
                updateSpans(true);
                updateButton(true);
                updateCheckboxes(true);
                this.searchField.scrollToBottom();
                return;
            }
            if (arrayList.isEmpty()) {
                positiveButton = new AlertDialog.Builder(getContext(), ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider).setMessage("All group members are not in your contact list.");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
                AlertDialog.Builder message = builder.setMessage(arrayList2.size() + " members are not in your contact list");
                positiveButton = message.setPositiveButton("Add " + arrayList.size() + " contacts", new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        StoryPrivacyBottomSheet.Page.this.lambda$selectChat$8(j, arrayList, dialogInterface, i3);
                    }
                });
            }
            positiveButton.setNegativeButton("Cancel", null).show();
        }

        private void updateHeaderTop() {
            if (!this.containsHeader) {
                this.headerView.setVisibility(8);
                return;
            }
            boolean z = false;
            this.headerView.setVisibility(0);
            float f = -this.headerView.getHeight();
            int i = 0;
            while (true) {
                if (i >= this.listView.getChildCount()) {
                    z = true;
                    break;
                }
                View childAt = this.listView.getChildAt(i);
                if ((childAt.getTag() instanceof Integer) && ((Integer) childAt.getTag()).intValue() == 35) {
                    f = this.contentView.getPaddingTop() + childAt.getY();
                    break;
                }
                i++;
            }
            if (this.isActionBar != z) {
                this.isActionBar = z;
                this.headerView.backDrawable.setRotation((z || this.pageType != 0) ? 0.0f : 1.0f, true);
            }
            this.headerView.setTranslationY(Math.max(AndroidUtilities.statusBarHeight, f));
        }

        private void updateSearchFieldTop() {
            float searchFieldTop = getSearchFieldTop();
            if (this.scrolling || this.keyboardMoving || getTranslationX() != 0.0f) {
                this.searchTranslationAnimating = false;
                ValueAnimator valueAnimator = this.searchFieldAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.searchFieldAnimator = null;
                }
                this.searchField.setTranslationY(searchFieldTop);
            } else if (!this.searchTranslationAnimating || Math.abs(this.searchTranslationAnimatingTo - searchFieldTop) > 1.0f) {
                this.searchTranslationAnimating = true;
                ValueAnimator valueAnimator2 = this.searchFieldAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                    this.searchFieldAnimator = null;
                }
                float translationY = this.searchField.getTranslationY();
                this.searchTranslationAnimatingTo = searchFieldTop;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(translationY, searchFieldTop);
                this.searchFieldAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        StoryPrivacyBottomSheet.Page.this.lambda$updateSearchFieldTop$15(valueAnimator3);
                    }
                });
                this.searchFieldAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.Page.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        Page.this.searchTranslationAnimating = false;
                    }
                });
                this.searchFieldAnimator.setInterpolator(new LinearInterpolator());
                this.searchFieldAnimator.setDuration(180L);
                this.searchFieldAnimator.start();
            }
        }

        private void updateSectionCell(boolean z) {
            if (this.sectionCell == null) {
                return;
            }
            if (StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup).size() > 0) {
                this.sectionCell.setRightText(LocaleController.getString(R.string.UsersDeselectAll), true, new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StoryPrivacyBottomSheet.Page.this.lambda$updateSectionCell$16(view);
                    }
                });
            } else if (z) {
                this.sectionCell.setRightText(null);
            } else {
                this.sectionCell.setRightText((CharSequence) null, (View.OnClickListener) null);
            }
        }

        private void updateSpans(boolean z) {
            HashSet mergeUsers = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
            int i = this.pageType;
            if (i == 3) {
                StoryPrivacyBottomSheet.this.selectedContactsCount = mergeUsers.size();
            } else if (i == 4) {
                StoryPrivacyBottomSheet.this.excludedEveryoneCount = mergeUsers.size();
            }
            MessagesController messagesController = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < this.searchField.allSpans.size(); i2++) {
                GroupCreateSpan groupCreateSpan = (GroupCreateSpan) this.searchField.allSpans.get(i2);
                if (!mergeUsers.contains(Long.valueOf(groupCreateSpan.getUid()))) {
                    arrayList.add(groupCreateSpan);
                }
            }
            Iterator it = mergeUsers.iterator();
            while (it.hasNext()) {
                Long l = (Long) it.next();
                long longValue = l.longValue();
                int i3 = 0;
                while (true) {
                    if (i3 >= this.searchField.allSpans.size()) {
                        Object user = longValue >= 0 ? messagesController.getUser(l) : messagesController.getChat(l);
                        if (user != null) {
                            GroupCreateSpan groupCreateSpan2 = new GroupCreateSpan(getContext(), user, null, true, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider);
                            groupCreateSpan2.setOnClickListener(this);
                            arrayList2.add(groupCreateSpan2);
                        }
                    } else if (((GroupCreateSpan) this.searchField.allSpans.get(i3)).getUid() == longValue) {
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            if (arrayList.isEmpty() && arrayList2.isEmpty()) {
                return;
            }
            this.searchField.spansContainer.updateSpans(arrayList, arrayList2, z);
        }

        public void applyBlocklist(boolean z) {
            if (this.pageType != 6) {
                return;
            }
            this.selectedUsers.clear();
            this.selectedUsers.addAll(MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().blocklist);
            for (int i = 0; i < this.changelog.size(); i++) {
                long keyAt = this.changelog.keyAt(i);
                if (!((Boolean) this.changelog.valueAt(i)).booleanValue()) {
                    this.selectedUsers.remove(Long.valueOf(keyAt));
                } else if (!this.selectedUsers.contains(Long.valueOf(keyAt))) {
                    this.selectedUsers.add(Long.valueOf(keyAt));
                }
            }
            if (z) {
                updateItems(true);
                updateButton(true);
                updateCheckboxes(true);
            }
        }

        public boolean atBottom() {
            return !this.listView.canScrollVertically(1);
        }

        public boolean atTop() {
            return !this.listView.canScrollVertically(-1);
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x009f  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00a2  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void bind(int i) {
            HashMap hashMap;
            HashMap hashMap2;
            ArrayList arrayList;
            ArrayList arrayList2;
            this.pageType = i;
            this.changelog.clear();
            this.selectedUsers.clear();
            this.selectedUsersByGroup.clear();
            if (i != 4) {
                if (i != 5) {
                    if (i == 1) {
                        ArrayList closeFriends = StoryPrivacyBottomSheet.this.getCloseFriends();
                        for (int i2 = 0; i2 < closeFriends.size(); i2++) {
                            this.selectedUsers.add(Long.valueOf(((TLRPC.User) closeFriends.get(i2)).id));
                        }
                    } else if (i == 2) {
                        arrayList = this.selectedUsers;
                        arrayList2 = StoryPrivacyBottomSheet.this.excludedContacts;
                    } else if (i == 3) {
                        this.selectedUsers.addAll(StoryPrivacyBottomSheet.this.selectedContacts);
                        hashMap = this.selectedUsersByGroup;
                        hashMap2 = StoryPrivacyBottomSheet.this.selectedContactsByGroup;
                    } else if (i == 6) {
                        applyBlocklist(false);
                    }
                    LinearLayoutManager linearLayoutManager = this.layoutManager;
                    this.adapter.reversedLayout = false;
                    linearLayoutManager.setReverseLayout(false);
                    updateSpans(false);
                    this.searchField.setText("");
                    this.searchField.setVisibility(i == 0 ? 8 : 0);
                    this.searchField.scrollToBottom();
                    this.query = null;
                    updateItems(false);
                    updateButton(false);
                    updateCheckboxes(false);
                    scrollToTop();
                    this.listView.requestLayout();
                    this.lastSelectedType = -1;
                }
                arrayList = this.selectedUsers;
                arrayList2 = StoryPrivacyBottomSheet.this.messageUsers;
                arrayList.addAll(arrayList2);
                LinearLayoutManager linearLayoutManager2 = this.layoutManager;
                this.adapter.reversedLayout = false;
                linearLayoutManager2.setReverseLayout(false);
                updateSpans(false);
                this.searchField.setText("");
                this.searchField.setVisibility(i == 0 ? 8 : 0);
                this.searchField.scrollToBottom();
                this.query = null;
                updateItems(false);
                updateButton(false);
                updateCheckboxes(false);
                scrollToTop();
                this.listView.requestLayout();
                this.lastSelectedType = -1;
            }
            this.selectedUsers.addAll(StoryPrivacyBottomSheet.this.excludedEveryone);
            hashMap = this.selectedUsersByGroup;
            hashMap2 = StoryPrivacyBottomSheet.this.excludedEveryoneByGroup;
            hashMap.putAll(hashMap2);
            LinearLayoutManager linearLayoutManager22 = this.layoutManager;
            this.adapter.reversedLayout = false;
            linearLayoutManager22.setReverseLayout(false);
            updateSpans(false);
            this.searchField.setText("");
            this.searchField.setVisibility(i == 0 ? 8 : 0);
            this.searchField.scrollToBottom();
            this.query = null;
            updateItems(false);
            updateButton(false);
            updateCheckboxes(false);
            scrollToTop();
            this.listView.requestLayout();
            this.lastSelectedType = -1;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            TLRPC.ChatFull chatFull;
            AlertDialog alertDialog;
            if (i != NotificationCenter.chatInfoDidLoad || (chatFull = (TLRPC.ChatFull) objArr[0]) == null || (alertDialog = this.progressDialog) == null || this.waitingForChatId != chatFull.id) {
                return;
            }
            alertDialog.dismissUnless(350L);
            this.progressDialog = null;
            this.waitingForChatId = -1L;
            selectChat(chatFull.id, chatFull.participants);
        }

        public int getTypeOn(MotionEvent motionEvent) {
            View findChildViewUnder;
            int childAdapterPosition;
            if (this.pageType == 0 && motionEvent != null && (findChildViewUnder = this.listView.findChildViewUnder(motionEvent.getX(), motionEvent.getY() - this.contentView.getPaddingTop())) != null && (childAdapterPosition = this.listView.getChildAdapterPosition(findChildViewUnder)) >= 0 && childAdapterPosition < this.items.size()) {
                ItemInner itemInner = (ItemInner) this.items.get(childAdapterPosition);
                if (itemInner.viewType == 3 && !itemInner.sendAs) {
                    boolean z = LocaleController.isRTL;
                    float x = motionEvent.getX();
                    if (!z ? x > AndroidUtilities.dp(100.0f) : x < getWidth() - AndroidUtilities.dp(100.0f)) {
                        return itemInner.type;
                    }
                }
            }
            return -1;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            NotificationCenter.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.searchField.allSpans.contains(view)) {
                GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
                if (!groupCreateSpan.isDeleting()) {
                    if (this.searchField.currentDeletingSpan != null) {
                        this.searchField.currentDeletingSpan.cancelDeleteAnimation();
                        this.searchField.currentDeletingSpan = null;
                    }
                    this.searchField.currentDeletingSpan = groupCreateSpan;
                    groupCreateSpan.startDeleteAnimation();
                    return;
                }
                this.searchField.currentDeletingSpan = null;
                this.searchField.spansContainer.removeSpan(groupCreateSpan);
                long uid = groupCreateSpan.getUid();
                Iterator it = this.selectedUsersByGroup.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (((ArrayList) entry.getValue()).contains(Long.valueOf(uid))) {
                        it.remove();
                        this.selectedUsers.addAll((Collection) entry.getValue());
                        this.selectedUsers.remove(Long.valueOf(uid));
                    }
                }
                this.selectedUsers.remove(Long.valueOf(uid));
                updateCheckboxes(true);
                updateButton(true);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            if (((BottomSheet) StoryPrivacyBottomSheet.this).keyboardHeight > 0) {
                this.keyboardHeight = ((BottomSheet) StoryPrivacyBottomSheet.this).keyboardHeight;
            }
            super.onMeasure(i, i2);
            this.contentView.setPadding(0, AndroidUtilities.statusBarHeight + (this.pageType == 0 ? 0 : AndroidUtilities.dp(56.0f)), 0, 0);
            if (this.wasKeyboardVisible != ((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible) {
                float searchFieldTop = getSearchFieldTop();
                if (((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible && searchFieldTop + Math.min(AndroidUtilities.dp(150.0f), this.searchField.resultContainerHeight) > this.listView.getPaddingTop()) {
                    scrollToTopSmoothly();
                }
                if (this.pageType == 0) {
                    this.buttonContainer.setTranslationY(((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible ? this.keyboardHeight : 0.0f);
                    this.underKeyboardView.setTranslationY(((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible ? this.keyboardHeight : 0.0f);
                } else {
                    this.buttonContainer.translateY(((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible ? this.keyboardHeight : -this.keyboardHeight, 0.0f);
                    this.underKeyboardView.setTranslationY(((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible ? this.keyboardHeight : -this.keyboardHeight);
                    this.keyboardMoving = true;
                    this.underKeyboardView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoryPrivacyBottomSheet.Page.this.lambda$onMeasure$17();
                        }
                    }).start();
                }
                this.wasKeyboardVisible = ((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible;
            }
            this.listView.setPadding(0, 0, 0, this.buttonContainer.getMeasuredHeight());
        }

        public void onSearch(String str) {
            if (str != null && str.isEmpty()) {
                str = null;
            }
            this.query = str;
            updateItems(false);
        }

        public void scrollToTop() {
            if (this.pageType != 0) {
                this.listView.scrollToPosition(0);
            }
        }

        public void scrollToTopSmoothly() {
            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(getContext(), 2, 0.7f);
            linearSmoothScrollerCustom.setTargetPosition(1);
            linearSmoothScrollerCustom.setOffset(-AndroidUtilities.dp(56.0f));
            this.layoutManager.startSmoothScroll(linearSmoothScrollerCustom);
        }

        public float top() {
            float paddingTop;
            float y;
            float f = (this.layoutManager.getReverseLayout() || this.pageType == 0) ? AndroidUtilities.displaySize.y : 0.0f;
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                if (this.layoutManager.getReverseLayout()) {
                    float paddingTop2 = this.contentView.getPaddingTop() + childAt.getY();
                    float alpha = childAt.getAlpha();
                    if (paddingTop2 < f) {
                        f = AndroidUtilities.lerp(f, paddingTop2, alpha);
                    }
                } else if (this.pageType != 0) {
                    if ((childAt.getTag() instanceof Integer) && ((Integer) childAt.getTag()).intValue() == 33) {
                        paddingTop = this.contentView.getPaddingTop() + childAt.getBottom();
                        y = childAt.getTranslationY();
                    } else if ((childAt.getTag() instanceof Integer) && ((Integer) childAt.getTag()).intValue() == 35) {
                        paddingTop = this.contentView.getPaddingTop();
                        y = childAt.getY();
                    }
                    return paddingTop + y;
                } else if (!(childAt instanceof PadView)) {
                    f = Math.min(this.contentView.getPaddingTop() + childAt.getY(), f);
                }
            }
            return f;
        }

        public void updateButton(boolean z) {
            ButtonWithCounterView buttonWithCounterView;
            ButtonWithCounterView buttonWithCounterView2;
            ButtonWithCounterView buttonWithCounterView3;
            int i;
            int i2 = this.pageType;
            int i3 = 8;
            boolean z2 = true;
            if (i2 == 0) {
                this.button.setShowZero(false);
                this.button.setEnabled(true);
                this.button.setCount(0, z);
                if (StoryPrivacyBottomSheet.this.isEdit) {
                    buttonWithCounterView3 = this.button;
                    i = R.string.StoryPrivacyButtonSave;
                } else {
                    buttonWithCounterView3 = this.button;
                    i = R.string.StoryPrivacyButtonPost;
                }
                buttonWithCounterView3.setText(LocaleController.getString(i), z);
                buttonWithCounterView2 = this.button2;
                if (StoryPrivacyBottomSheet.this.sendAsMessageEnabled) {
                    i3 = 0;
                }
            } else {
                if (i2 == 1) {
                    this.button.setShowZero(false);
                    this.button.setEnabled(true);
                    this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonSaveCloseFriends), z);
                } else {
                    if (i2 == 3) {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                        int i4 = storyPrivacyBottomSheet.selectedContactsCount = storyPrivacyBottomSheet.mergeUsers(this.selectedUsers, this.selectedUsersByGroup).size();
                        this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonSave), z);
                        this.button.setShowZero(false);
                        this.buttonContainer.hide(i4 <= 0, z);
                        this.button.setCount(i4, z);
                        buttonWithCounterView = this.button;
                        if (i4 <= 0) {
                            z2 = false;
                        }
                    } else if (i2 == 2) {
                        this.button.setShowZero(false);
                        this.button.setEnabled(true);
                        if (this.selectedUsers.isEmpty()) {
                            this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonSave), z);
                            this.button.setCount(0, z);
                            buttonWithCounterView2 = this.button2;
                        } else {
                            this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonExcludeContacts), z);
                        }
                    } else if (i2 == 5) {
                        this.button.setShowZero(true);
                        this.button.setEnabled(true ^ this.selectedUsers.isEmpty());
                    } else if (i2 == 6) {
                        this.button.setShowZero(false);
                        this.button.setEnabled(true);
                        this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonSaveCloseFriends), z);
                        StoriesController storiesController = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController();
                        if (!storiesController.blocklistFull) {
                            storiesController.getBlocklistCount();
                            for (int i5 = 0; i5 < this.changelog.size(); i5++) {
                                long keyAt = this.changelog.keyAt(i5);
                                ((Boolean) this.changelog.valueAt(i5)).booleanValue();
                                storiesController.blocklist.contains(Long.valueOf(keyAt));
                            }
                            buttonWithCounterView2 = this.button2;
                        }
                    } else if (i2 != 4) {
                        return;
                    } else {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet2 = StoryPrivacyBottomSheet.this;
                        int i6 = storyPrivacyBottomSheet2.excludedEveryoneCount = storyPrivacyBottomSheet2.mergeUsers(storyPrivacyBottomSheet2.excludedEveryone, StoryPrivacyBottomSheet.this.excludedEveryoneByGroup).size();
                        this.button.setText(LocaleController.getString(R.string.StoryPrivacyButtonSave), z);
                        this.button.setShowZero(false);
                        this.buttonContainer.hide(false, z);
                        this.button.setCount(i6, z);
                        buttonWithCounterView = this.button;
                    }
                    buttonWithCounterView.setEnabled(z2);
                    buttonWithCounterView2 = this.button2;
                }
                this.button.setCount(this.selectedUsers.size(), z);
                buttonWithCounterView2 = this.button2;
            }
            buttonWithCounterView2.setVisibility(i3);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00d9  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x013c  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x0133 A[EDGE_INSN: B:90:0x0133->B:56:0x0133 ?: BREAK  , SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateCheckboxes(boolean z) {
            ArrayList arrayList;
            HashMap hashMap;
            int i;
            int i2;
            int childAdapterPosition;
            float f;
            int i3 = this.pageType;
            if (i3 != 4) {
                if (i3 == 2) {
                    StoryPrivacyBottomSheet.this.excludedContacts.clear();
                    arrayList = StoryPrivacyBottomSheet.this.excludedContacts;
                } else if (i3 != 3) {
                    if (i3 == 0) {
                        StoryPrivacyBottomSheet.this.messageUsers.clear();
                        arrayList = StoryPrivacyBottomSheet.this.messageUsers;
                    }
                    if (this.pageType == 3 && (StoryPrivacyBottomSheet.this.selectedType != 3 || (this.selectedUsers.isEmpty() && this.selectedUsersByGroup.isEmpty()))) {
                        if (this.selectedUsers.isEmpty() || !this.selectedUsersByGroup.isEmpty()) {
                            this.lastSelectedType = StoryPrivacyBottomSheet.this.selectedType;
                            StoryPrivacyBottomSheet.this.selectedType = 3;
                        } else {
                            int i4 = this.lastSelectedType;
                            if (i4 != -1) {
                                StoryPrivacyBottomSheet.this.selectedType = i4;
                            }
                        }
                    }
                    HashSet mergeUsers = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
                    i = 0;
                    while (true) {
                        boolean z2 = true;
                        if (i >= this.items.size()) {
                            break;
                        }
                        ItemInner itemInner = (ItemInner) this.items.get(i);
                        if (itemInner != null) {
                            if (itemInner.type <= 0) {
                                TLRPC.User user = itemInner.user;
                                if (user != null) {
                                    boolean contains = this.selectedUsers.contains(Long.valueOf(user.id));
                                    itemInner.checked = contains;
                                    itemInner.halfChecked = (contains || !mergeUsers.contains(Long.valueOf(itemInner.user.id))) ? false : false;
                                } else {
                                    TLRPC.Chat chat = itemInner.chat;
                                    if (chat != null) {
                                        z2 = this.selectedUsersByGroup.containsKey(Long.valueOf(chat.id));
                                    }
                                }
                            } else if (StoryPrivacyBottomSheet.this.selectedType != itemInner.type) {
                                z2 = false;
                            }
                            itemInner.checked = z2;
                            itemInner.halfChecked = false;
                        }
                        i++;
                    }
                    for (i2 = 0; i2 < this.listView.getChildCount(); i2++) {
                        View childAt = this.listView.getChildAt(i2);
                        if ((childAt instanceof UserCell) && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt)) >= 0 && childAdapterPosition < this.items.size() && (childAt instanceof UserCell)) {
                            ItemInner itemInner2 = (ItemInner) this.items.get(childAdapterPosition);
                            UserCell userCell = (UserCell) childAt;
                            userCell.setChecked(itemInner2.checked || itemInner2.halfChecked, z);
                            TLRPC.Chat chat2 = itemInner2.chat;
                            if (chat2 != null) {
                                if (StoryPrivacyBottomSheet.this.getParticipantsCount(chat2) > 200) {
                                    f = 0.3f;
                                    userCell.setCheckboxAlpha(f, z);
                                }
                                f = 1.0f;
                                userCell.setCheckboxAlpha(f, z);
                            } else {
                                if (itemInner2.halfChecked && !itemInner2.checked) {
                                    f = 0.5f;
                                    userCell.setCheckboxAlpha(f, z);
                                }
                                f = 1.0f;
                                userCell.setCheckboxAlpha(f, z);
                            }
                        }
                    }
                    updateSectionCell(z);
                } else {
                    StoryPrivacyBottomSheet.this.selectedContacts.clear();
                    StoryPrivacyBottomSheet.this.selectedContactsByGroup.clear();
                    StoryPrivacyBottomSheet.this.selectedContacts.addAll(this.selectedUsers);
                    hashMap = StoryPrivacyBottomSheet.this.selectedContactsByGroup;
                }
                arrayList.addAll(this.selectedUsers);
                if (this.pageType == 3) {
                    if (this.selectedUsers.isEmpty()) {
                    }
                    this.lastSelectedType = StoryPrivacyBottomSheet.this.selectedType;
                    StoryPrivacyBottomSheet.this.selectedType = 3;
                }
                HashSet mergeUsers2 = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
                i = 0;
                while (true) {
                    boolean z22 = true;
                    if (i >= this.items.size()) {
                    }
                    i++;
                }
                while (i2 < this.listView.getChildCount()) {
                }
                updateSectionCell(z);
            }
            StoryPrivacyBottomSheet.this.excludedEveryone.clear();
            StoryPrivacyBottomSheet.this.excludedEveryoneByGroup.clear();
            StoryPrivacyBottomSheet.this.excludedEveryone.addAll(this.selectedUsers);
            hashMap = StoryPrivacyBottomSheet.this.excludedEveryoneByGroup;
            hashMap.putAll(this.selectedUsersByGroup);
            if (this.pageType == 3) {
            }
            HashSet mergeUsers22 = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
            i = 0;
            while (true) {
                boolean z222 = true;
                if (i >= this.items.size()) {
                }
                i++;
            }
            while (i2 < this.listView.getChildCount()) {
            }
            updateSectionCell(z);
        }

        public void updateItems(boolean z) {
            updateItems(z, true);
        }

        /* JADX WARN: Code restructure failed: missing block: B:201:0x07b1, code lost:
            if (r8 != 4) goto L140;
         */
        /* JADX WARN: Code restructure failed: missing block: B:242:0x0849, code lost:
            if (r10 == 4) goto L185;
         */
        /* JADX WARN: Removed duplicated region for block: B:144:0x06c0  */
        /* JADX WARN: Removed duplicated region for block: B:265:0x08cb  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0182  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0184  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x0199  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateItems(boolean z, boolean z2) {
            float dp;
            GraySectionCell graySectionCell;
            int i;
            ArrayList users;
            int i2;
            int i3;
            ArrayList arrayList;
            ItemInner asChat;
            boolean z3;
            ArrayList arrayList2;
            int i4;
            ItemInner asChat2;
            boolean z4;
            TLRPC.User currentUser;
            boolean z5;
            boolean z6;
            MessagesController messagesController;
            this.oldItems.clear();
            this.oldItems.addAll(this.items);
            this.items.clear();
            int i5 = this.pageType;
            if (i5 != 0) {
                if (i5 == 1) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertCloseFriendsTitle));
                    this.headerView.setCloseImageVisible(true);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertCloseFriendsSubtitle;
                } else if (i5 == 2) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertExcludedContactsTitle));
                    this.headerView.setCloseImageVisible(true);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertExcludedContactsSubtitle;
                } else if (i5 == 3) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertSelectContactsTitle));
                    this.headerView.setCloseImageVisible(true);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertSelectContactsSubtitle;
                } else if (i5 == 5) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertAsMessageTitle));
                    this.headerView.setCloseImageVisible(StoryPrivacyBottomSheet.this.startedFromSendAsMessage);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertAsMessageSubtitle;
                } else if (i5 == 6) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertBlocklistTitle));
                    this.headerView.setCloseImageVisible(true);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertBlocklistSubtitle;
                } else if (i5 == 4) {
                    this.headerView.setText(LocaleController.getString(R.string.StoryPrivacyAlertExcludeFromEveryoneTitle));
                    this.headerView.setCloseImageVisible(true);
                    this.headerView.backDrawable.setRotation(0.0f, false);
                    this.items.add(ItemInner.asPad());
                    this.items.add(ItemInner.asHeader());
                    this.searchPosition = this.items.size();
                    this.items.add(ItemInner.asSearchField());
                    this.items.add(ItemInner.asSection());
                    dp = AndroidUtilities.dp(56.0f) + 0.0f + AndroidUtilities.dp(150.0f) + AndroidUtilities.dp(32.0f);
                    graySectionCell = this.sectionCell;
                    i = R.string.StoryPrivacyAlertExcludeFromEveryoneSubtitle;
                }
                graySectionCell.setText(LocaleController.getString(i));
                updateSectionCell(z);
                this.containsHeader = true;
                boolean z7 = !TextUtils.isEmpty(this.query);
                if (this.pageType != 0) {
                    String lowerCase = AndroidUtilities.translitSafe(this.query).toLowerCase();
                    int i6 = this.pageType;
                    if (i6 == 5) {
                        users = StoryPrivacyBottomSheet.this.getChats();
                    } else {
                        StoryPrivacyBottomSheet storyPrivacyBottomSheet = StoryPrivacyBottomSheet.this;
                        users = storyPrivacyBottomSheet.getUsers(i6 == 1 || i6 == 2, storyPrivacyBottomSheet.allowSmallChats && ((i2 = this.pageType) == 3 || i2 == 6));
                    }
                    HashSet mergeUsers = StoryPrivacyBottomSheet.this.mergeUsers(this.selectedUsers, this.selectedUsersByGroup);
                    if (z7) {
                        i3 = 0;
                    } else {
                        if (!z) {
                            this.atTop.clear();
                            for (int i7 = 0; i7 < users.size(); i7++) {
                                TLObject tLObject = (TLObject) users.get(i7);
                                if (tLObject instanceof TLRPC.User ? this.selectedUsers.contains(Long.valueOf(((TLRPC.User) tLObject).id)) : tLObject instanceof TLRPC.Chat ? this.selectedUsersByGroup.containsKey(Long.valueOf(((TLRPC.Chat) tLObject).id)) : false) {
                                    this.atTop.add(tLObject);
                                }
                            }
                        }
                        i3 = 0;
                        for (int i8 = 0; i8 < this.atTop.size(); i8++) {
                            TLObject tLObject2 = (TLObject) this.atTop.get(i8);
                            if (tLObject2 instanceof TLRPC.User) {
                                TLRPC.User user = (TLRPC.User) tLObject2;
                                boolean contains = this.selectedUsers.contains(Long.valueOf(user.id));
                                boolean z8 = !contains && mergeUsers.contains(Long.valueOf(user.id));
                                arrayList2 = this.items;
                                asChat2 = ItemInner.asUser(user, contains, z8);
                                int i9 = this.pageType;
                                if (i9 == 2 || i9 == 4) {
                                    i4 = i3;
                                    z4 = true;
                                } else {
                                    i4 = i3;
                                    z4 = false;
                                }
                            } else if (tLObject2 instanceof TLRPC.Chat) {
                                TLRPC.Chat chat = (TLRPC.Chat) tLObject2;
                                arrayList2 = this.items;
                                i4 = i3;
                                asChat2 = ItemInner.asChat(chat, this.selectedUsersByGroup.containsKey(Long.valueOf(chat.id)));
                                int i10 = this.pageType;
                                if (i10 != 2) {
                                }
                                z4 = true;
                            }
                            arrayList2.add(asChat2.red(z4));
                            dp += AndroidUtilities.dp(56.0f);
                            i3 = i4 + 1;
                        }
                    }
                    for (int i11 = 0; i11 < users.size(); i11++) {
                        TLObject tLObject3 = (TLObject) users.get(i11);
                        if ((z7 || !this.atTop.contains(tLObject3)) && match(tLObject3, lowerCase)) {
                            if (tLObject3 instanceof TLRPC.User) {
                                TLRPC.User user2 = (TLRPC.User) tLObject3;
                                boolean contains2 = this.selectedUsers.contains(Long.valueOf(user2.id));
                                boolean z9 = !contains2 && mergeUsers.contains(Long.valueOf(user2.id));
                                arrayList = this.items;
                                asChat = ItemInner.asUser(user2, contains2, z9);
                                int i12 = this.pageType;
                                if (i12 != 2 && i12 != 4) {
                                    z3 = false;
                                    arrayList.add(asChat.red(z3));
                                    dp += AndroidUtilities.dp(56.0f);
                                    i3++;
                                }
                                z3 = true;
                                arrayList.add(asChat.red(z3));
                                dp += AndroidUtilities.dp(56.0f);
                                i3++;
                            } else if (tLObject3 instanceof TLRPC.Chat) {
                                TLRPC.Chat chat2 = (TLRPC.Chat) tLObject3;
                                arrayList = this.items;
                                asChat = ItemInner.asChat(chat2, this.selectedUsersByGroup.containsKey(Long.valueOf(chat2.id)));
                                int i13 = this.pageType;
                                if (i13 != 2) {
                                }
                                z3 = true;
                                arrayList.add(asChat.red(z3));
                                dp += AndroidUtilities.dp(56.0f);
                                i3++;
                            }
                        }
                    }
                    if (z7) {
                        if (i3 == 0) {
                            this.items.add(ItemInner.asNoUsers());
                            dp += AndroidUtilities.dp(150.0f);
                        }
                        RecyclerListView recyclerListView = this.listView;
                        float measuredHeight = (recyclerListView != null ? ((recyclerListView.getMeasuredHeight() - this.listView.getPaddingTop()) - this.listView.getPaddingBottom()) + (((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible ? this.keyboardHeight : 0) : ((AndroidUtilities.displaySize.y - AndroidUtilities.dp(56.0f)) - AndroidUtilities.navigationBarHeight) - AndroidUtilities.dp(42.0f)) - dp;
                        if (measuredHeight > 0.0f) {
                            this.items.add(ItemInner.asPadding((int) measuredHeight));
                        }
                    }
                }
                if (this.layoutManager.getReverseLayout()) {
                    Collections.reverse(this.items);
                }
                if (z2 && this.adapter != null) {
                    if (z || StoryPrivacyBottomSheet.this.selectedType == 0) {
                        this.adapter.notifyDataSetChanged();
                    } else {
                        this.adapter.setItems(this.oldItems, this.items);
                    }
                }
                this.contentView.invalidate();
            }
            this.containsHeader = false;
            this.sectionCell.setVisibility(8);
            ArrayList arrayList3 = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().sendAs;
            ItemInner asPad = ItemInner.asPad(24);
            this.items.add(asPad);
            if (StoryPrivacyBottomSheet.this.canChangePeer && (StoryPrivacyBottomSheet.this.isEdit || arrayList3 == null || arrayList3.size() <= 1)) {
                this.items.add(ItemInner.asHeader2(LocaleController.getString(StoryPrivacyBottomSheet.this.isEdit ? R.string.StoryPrivacyAlertEditTitle : R.string.StoryPrivacyAlertTitle), StoryPrivacyBottomSheet.this.storyPeriod != Integer.MAX_VALUE ? LocaleController.formatPluralString("StoryPrivacyAlertSubtitle", StoryPrivacyBottomSheet.this.storyPeriod / 3600, new Object[0]) : LocaleController.getString(R.string.StoryPrivacyAlertSubtitleProfile)));
                asPad.subtractHeight += AndroidUtilities.dp(72.0f);
                z6 = false;
                z5 = true;
            } else {
                this.items.add(ItemInner.asHeaderCell(LocaleController.getString(R.string.StoryPrivacyPublishAs)));
                asPad.subtractHeight += AndroidUtilities.dp(50.0f);
                StoryPrivacyBottomSheet storyPrivacyBottomSheet2 = StoryPrivacyBottomSheet.this;
                TLRPC.InputPeer inputPeer = storyPrivacyBottomSheet2.selectedPeer;
                if (inputPeer == null || (inputPeer instanceof TLRPC.TL_inputPeerSelf)) {
                    currentUser = UserConfig.getInstance(((BottomSheet) storyPrivacyBottomSheet2).currentAccount).getCurrentUser();
                } else if (inputPeer instanceof TLRPC.TL_inputPeerUser) {
                    currentUser = MessagesController.getInstance(((BottomSheet) storyPrivacyBottomSheet2).currentAccount).getUser(Long.valueOf(StoryPrivacyBottomSheet.this.selectedPeer.user_id));
                } else {
                    if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
                        TLRPC.Chat chat3 = MessagesController.getInstance(((BottomSheet) storyPrivacyBottomSheet2).currentAccount).getChat(Long.valueOf(StoryPrivacyBottomSheet.this.selectedPeer.channel_id));
                        this.items.add(ItemInner.asChat(chat3, false).asSendAs());
                        z6 = ChatObject.isChannelAndNotMegaGroup(chat3);
                        asPad.subtractHeight += AndroidUtilities.dp(104.0f);
                    } else {
                        if (inputPeer instanceof TLRPC.TL_inputPeerChat) {
                            this.items.add(ItemInner.asChat(MessagesController.getInstance(((BottomSheet) storyPrivacyBottomSheet2).currentAccount).getChat(Long.valueOf(StoryPrivacyBottomSheet.this.selectedPeer.chat_id)), false).asSendAs());
                            asPad.subtractHeight += AndroidUtilities.dp(104.0f);
                            z6 = false;
                        }
                        z6 = false;
                        z5 = true;
                        ItemInner asShadow = ItemInner.asShadow(null);
                        asShadow.resId = z5 ? 1 : 2;
                        this.items.add(asShadow);
                        asPad.subtractHeight += AndroidUtilities.dp(12.0f);
                        if (z5) {
                            this.items.add(ItemInner.asHeaderCell(LocaleController.getString(R.string.StoryPrivacyWhoCanView)));
                            asPad.subtractHeight += AndroidUtilities.dp(40.0f);
                        }
                    }
                    z5 = false;
                    ItemInner asShadow2 = ItemInner.asShadow(null);
                    asShadow2.resId = z5 ? 1 : 2;
                    this.items.add(asShadow2);
                    asPad.subtractHeight += AndroidUtilities.dp(12.0f);
                    if (z5) {
                    }
                }
                this.items.add(ItemInner.asUser(currentUser, false, false).asSendAs());
                asPad.subtractHeight += AndroidUtilities.dp(62.0f);
                z6 = false;
                z5 = true;
                ItemInner asShadow22 = ItemInner.asShadow(null);
                asShadow22.resId = z5 ? 1 : 2;
                this.items.add(asShadow22);
                asPad.subtractHeight += AndroidUtilities.dp(12.0f);
                if (z5) {
                }
            }
            if (z5) {
                ArrayList arrayList4 = this.items;
                ItemInner asType = ItemInner.asType(4, StoryPrivacyBottomSheet.this.selectedType == 4, StoryPrivacyBottomSheet.this.excludedEveryoneCount);
                arrayList4.add(asType);
                asPad.subtractHeight += AndroidUtilities.dp(56.0f);
                if (StoryPrivacyBottomSheet.this.excludedEveryoneCount == 1) {
                    if (StoryPrivacyBottomSheet.this.excludedEveryone.size() != 1) {
                        Iterator it = StoryPrivacyBottomSheet.this.excludedEveryoneByGroup.values().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ArrayList arrayList5 = (ArrayList) it.next();
                            if (arrayList5.size() >= 1) {
                                asType.user = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getUser((Long) arrayList5.get(0));
                                break;
                            }
                        }
                    } else {
                        asType.user = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getUser((Long) StoryPrivacyBottomSheet.this.excludedEveryone.get(0));
                    }
                }
                ArrayList arrayList6 = this.items;
                ItemInner asType2 = ItemInner.asType(2, StoryPrivacyBottomSheet.this.selectedType == 2, StoryPrivacyBottomSheet.this.excludedContacts.size());
                arrayList6.add(asType2);
                asPad.subtractHeight += AndroidUtilities.dp(56.0f);
                if (StoryPrivacyBottomSheet.this.excludedContacts.size() == 1) {
                    asType2.user = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getUser((Long) StoryPrivacyBottomSheet.this.excludedContacts.get(0));
                }
                ArrayList closeFriends = StoryPrivacyBottomSheet.this.getCloseFriends();
                ArrayList arrayList7 = this.items;
                ItemInner asType3 = ItemInner.asType(1, StoryPrivacyBottomSheet.this.selectedType == 1, closeFriends.size());
                arrayList7.add(asType3);
                asPad.subtractHeight += AndroidUtilities.dp(56.0f);
                if (closeFriends.size() == 1 && (closeFriends.get(0) instanceof TLRPC.User)) {
                    asType3.user = (TLRPC.User) closeFriends.get(0);
                }
                ArrayList arrayList8 = this.items;
                ItemInner asType4 = ItemInner.asType(3, StoryPrivacyBottomSheet.this.selectedType == 3, StoryPrivacyBottomSheet.this.selectedContactsCount);
                arrayList8.add(asType4);
                asPad.subtractHeight += AndroidUtilities.dp(56.0f);
                if (StoryPrivacyBottomSheet.this.selectedContactsCount == 1) {
                    if (StoryPrivacyBottomSheet.this.selectedContacts.size() == 1) {
                        messagesController = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount);
                        r14 = StoryPrivacyBottomSheet.this.selectedContacts;
                    } else {
                        for (ArrayList arrayList9 : StoryPrivacyBottomSheet.this.selectedContactsByGroup.values()) {
                            if (arrayList9.size() >= 1) {
                                messagesController = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount);
                            }
                        }
                    }
                    asType4.user = messagesController.getUser((Long) arrayList9.get(0));
                    break;
                }
                int blocklistCount = MessagesController.getInstance(((BottomSheet) StoryPrivacyBottomSheet.this).currentAccount).getStoriesController().getBlocklistCount();
                this.items.add(ItemInner.asShadow(AndroidUtilities.replaceSingleTag(blocklistCount <= 0 ? LocaleController.getString(R.string.StoryBlockListEmpty) : LocaleController.formatPluralString("StoryBlockList", blocklistCount, new Object[0]), Theme.key_chat_messageLinkIn, 0, new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$Page$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryPrivacyBottomSheet.Page.this.lambda$updateItems$14();
                    }
                }, ((BottomSheet) StoryPrivacyBottomSheet.this).resourcesProvider)));
                asPad.subtractHeight += AndroidUtilities.dp(80.0f);
            }
            if (!StoryPrivacyBottomSheet.this.isEdit) {
                this.items.add(ItemInner.asCheck(LocaleController.getString(R.string.StoryAllowScreenshots), 0, StoryPrivacyBottomSheet.this.allowScreenshots));
                asPad.subtractHeight += AndroidUtilities.dp(50.0f);
                this.items.add(ItemInner.asCheck(LocaleController.getString(z5 ? R.string.StoryKeep : z6 ? R.string.StoryKeepChannel : R.string.StoryKeepGroup), 1, StoryPrivacyBottomSheet.this.keepOnMyPage));
                asPad.subtractHeight += AndroidUtilities.dp(50.0f);
                this.items.add(ItemInner.asShadow(LocaleController.formatPluralString(z5 ? "StoryKeepInfo" : z6 ? "StoryKeepChannelInfo" : "StoryKeepGroupInfo", (StoryPrivacyBottomSheet.this.storyPeriod == Integer.MAX_VALUE ? 86400 : StoryPrivacyBottomSheet.this.storyPeriod) / 3600, new Object[0])));
                asPad.subtractHeight += AndroidUtilities.dp(80.0f);
            }
            if (StoryPrivacyBottomSheet.this.keepOnMyPage && StoryPrivacyBottomSheet.this.whenCoverClicked != null) {
                this.items.add(ItemInner.asButton(LocaleController.getString(R.string.StoryEditCover), StoryPrivacyBottomSheet.this.coverDrawable));
                asPad.subtractHeight += AndroidUtilities.dp(50.0f);
                this.items.add(ItemInner.asShadow(LocaleController.getString(R.string.StoryEditCoverInfo)));
                asPad.subtractHeight += AndroidUtilities.dp(72.0f);
            }
            dp = 0.0f;
            boolean z72 = !TextUtils.isEmpty(this.query);
            if (this.pageType != 0) {
            }
            if (this.layoutManager.getReverseLayout()) {
            }
            if (z2) {
                if (z) {
                }
                this.adapter.notifyDataSetChanged();
            }
            this.contentView.invalidate();
        }

        public void updateTops() {
            updateSearchFieldTop();
            updateHeaderTop();
        }
    }

    /* loaded from: classes4.dex */
    public class SearchUsersCell extends ScrollView {
        public ArrayList allSpans;
        private final LinearGradient bottomGradient;
        private final AnimatedFloat bottomGradientAlpha;
        private final Matrix bottomGradientMatrix;
        private final Paint bottomGradientPaint;
        public float containerHeight;
        private GroupCreateSpan currentDeletingSpan;
        private EditTextBoldCursor editText;
        private int fieldY;
        private int hintTextWidth;
        private boolean ignoreScrollEvent;
        private boolean ignoreTextChange;
        private Utilities.Callback onSearchTextChange;
        private int prevResultContainerHeight;
        private final Theme.ResourcesProvider resourcesProvider;
        public int resultContainerHeight;
        private boolean scroll;
        public SpansContainer spansContainer;
        private final LinearGradient topGradient;
        private final AnimatedFloat topGradientAlpha;
        private final Matrix topGradientMatrix;
        private final Paint topGradientPaint;
        private Runnable updateHeight;

        /* loaded from: classes4.dex */
        public class SpansContainer extends ViewGroup {
            private View addingSpan;
            private ArrayList animAddingSpans;
            private ArrayList animRemovingSpans;
            private boolean animationStarted;
            private ArrayList animators;
            private AnimatorSet currentAnimation;
            private final int heightDp;
            private final int padDp;
            private final int padXDp;
            private final int padYDp;
            private final ArrayList removingSpans;

            public SpansContainer(Context context) {
                super(context);
                this.animAddingSpans = new ArrayList();
                this.animRemovingSpans = new ArrayList();
                this.animators = new ArrayList();
                this.removingSpans = new ArrayList();
                this.padDp = 7;
                this.padYDp = 4;
                this.padXDp = 4;
                this.heightDp = 28;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onMeasure$0() {
                SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
            }

            private void setupEndValues() {
                AnimatorSet animatorSet = this.currentAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                for (int i = 0; i < this.animAddingSpans.size(); i++) {
                    ((View) this.animAddingSpans.get(i)).setScaleX(1.0f);
                    ((View) this.animAddingSpans.get(i)).setScaleY(1.0f);
                    ((View) this.animAddingSpans.get(i)).setAlpha(1.0f);
                }
                for (int i2 = 0; i2 < this.animRemovingSpans.size(); i2++) {
                    ((View) this.animRemovingSpans.get(i2)).setScaleX(0.0f);
                    ((View) this.animRemovingSpans.get(i2)).setScaleY(0.0f);
                    ((View) this.animRemovingSpans.get(i2)).setAlpha(0.0f);
                }
                this.animAddingSpans.clear();
                this.animRemovingSpans.clear();
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                int childCount = getChildCount();
                for (int i5 = 0; i5 < childCount; i5++) {
                    View childAt = getChildAt(i5);
                    childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:32:0x00d9  */
            /* JADX WARN: Removed duplicated region for block: B:33:0x00e6  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onMeasure(int i, int i2) {
                int min;
                int i3;
                float f;
                int childCount = getChildCount();
                int size = View.MeasureSpec.getSize(i);
                int dp = size - AndroidUtilities.dp(14.0f);
                int dp2 = AndroidUtilities.dp(10.0f);
                int dp3 = AndroidUtilities.dp(10.0f);
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                while (i4 < childCount) {
                    View childAt = getChildAt(i4);
                    if (childAt instanceof GroupCreateSpan) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
                        boolean contains = this.removingSpans.contains(childAt);
                        if (!contains && childAt.getMeasuredWidth() + i5 > dp) {
                            dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(4.0f);
                            i5 = 0;
                        }
                        if (childAt.getMeasuredWidth() + i6 > dp) {
                            dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(4.0f);
                            i6 = 0;
                        }
                        int dp4 = AndroidUtilities.dp(7.0f) + i5;
                        if (!this.animationStarted) {
                            if (contains) {
                                childAt.setTranslationX(AndroidUtilities.dp(7.0f) + i6);
                                childAt.setTranslationY(dp3);
                            } else {
                                if (this.removingSpans.isEmpty()) {
                                    i3 = childCount;
                                    childAt.setTranslationX(dp4);
                                    childAt.setTranslationY(dp2);
                                } else {
                                    float f2 = dp4;
                                    if (childAt.getTranslationX() != f2) {
                                        i3 = childCount;
                                        this.animators.add(ObjectAnimator.ofFloat(childAt, View.TRANSLATION_X, f2));
                                    } else {
                                        i3 = childCount;
                                    }
                                    float f3 = dp2;
                                    if (childAt.getTranslationY() != f3) {
                                        this.animators.add(ObjectAnimator.ofFloat(childAt, View.TRANSLATION_Y, f3));
                                    }
                                }
                                if (contains) {
                                    f = 4.0f;
                                    i5 += childAt.getMeasuredWidth() + AndroidUtilities.dp(4.0f);
                                } else {
                                    f = 4.0f;
                                }
                                i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(f);
                            }
                        }
                        i3 = childCount;
                        if (contains) {
                        }
                        i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(f);
                    } else {
                        i3 = childCount;
                    }
                    i4++;
                    childCount = i3;
                }
                if (AndroidUtilities.isTablet()) {
                    min = AndroidUtilities.dp(394.0f);
                } else {
                    Point point = AndroidUtilities.displaySize;
                    min = Math.min(point.x, point.y) - AndroidUtilities.dp(136.0f);
                }
                int i7 = min / 3;
                if (dp - i5 < i7) {
                    dp2 += AndroidUtilities.dp(36.0f);
                    i5 = 0;
                }
                if (dp - i6 < i7) {
                    dp3 += AndroidUtilities.dp(36.0f);
                }
                SearchUsersCell.this.editText.measure(View.MeasureSpec.makeMeasureSpec(dp - i5, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
                SearchUsersCell.this.editText.setHintVisible(SearchUsersCell.this.editText.getMeasuredWidth() > SearchUsersCell.this.hintTextWidth, true);
                if (!this.animationStarted) {
                    int dp5 = dp3 + AndroidUtilities.dp(38.0f);
                    int dp6 = i5 + AndroidUtilities.dp(16.0f);
                    SearchUsersCell.this.fieldY = dp2;
                    if (this.currentAnimation != null) {
                        int dp7 = dp2 + AndroidUtilities.dp(38.0f);
                        SearchUsersCell searchUsersCell = SearchUsersCell.this;
                        searchUsersCell.resultContainerHeight = dp7;
                        float f4 = dp7;
                        if (searchUsersCell.containerHeight != f4) {
                            this.animators.add(searchUsersCell.getContainerHeightAnimator(f4));
                        }
                        float f5 = dp6;
                        if (SearchUsersCell.this.editText.getTranslationX() != f5) {
                            this.animators.add(ObjectAnimator.ofFloat(SearchUsersCell.this.editText, View.TRANSLATION_X, f5));
                        }
                        if (SearchUsersCell.this.editText.getTranslationY() != SearchUsersCell.this.fieldY) {
                            this.animators.add(ObjectAnimator.ofFloat(SearchUsersCell.this.editText, View.TRANSLATION_Y, SearchUsersCell.this.fieldY));
                        }
                        SearchUsersCell.this.editText.setAllowDrawCursor(false);
                        this.currentAnimation.playTogether(this.animators);
                        this.currentAnimation.setDuration(180L);
                        this.currentAnimation.setInterpolator(new LinearInterpolator());
                        this.currentAnimation.start();
                        this.animationStarted = true;
                        if (SearchUsersCell.this.updateHeight != null) {
                            SearchUsersCell.this.updateHeight.run();
                        }
                    } else {
                        SearchUsersCell searchUsersCell2 = SearchUsersCell.this;
                        searchUsersCell2.resultContainerHeight = dp5;
                        searchUsersCell2.containerHeight = dp5;
                        searchUsersCell2.editText.setTranslationX(dp6);
                        SearchUsersCell.this.editText.setTranslationY(SearchUsersCell.this.fieldY);
                        if (SearchUsersCell.this.updateHeight != null) {
                            SearchUsersCell.this.updateHeight.run();
                        }
                        if (SearchUsersCell.this.scroll) {
                            post(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$SearchUsersCell$SpansContainer$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StoryPrivacyBottomSheet.SearchUsersCell.SpansContainer.this.lambda$onMeasure$0();
                                }
                            });
                            SearchUsersCell.this.scroll = false;
                        }
                    }
                    SearchUsersCell searchUsersCell3 = SearchUsersCell.this;
                    searchUsersCell3.prevResultContainerHeight = searchUsersCell3.resultContainerHeight;
                } else if (this.currentAnimation != null) {
                    if (!SearchUsersCell.this.ignoreScrollEvent && this.removingSpans.isEmpty()) {
                        SearchUsersCell.this.editText.bringPointIntoView(SearchUsersCell.this.editText.getSelectionStart());
                    }
                    if (SearchUsersCell.this.scroll) {
                        SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                        SearchUsersCell.this.scroll = false;
                    }
                }
                setMeasuredDimension(size, (int) SearchUsersCell.this.containerHeight);
            }

            public void removeAllSpans(boolean z) {
                SearchUsersCell.this.ignoreScrollEvent = true;
                final ArrayList arrayList = new ArrayList(SearchUsersCell.this.allSpans);
                this.removingSpans.clear();
                this.removingSpans.addAll(SearchUsersCell.this.allSpans);
                SearchUsersCell.this.allSpans.clear();
                for (int i = 0; i < arrayList.size(); i++) {
                    ((GroupCreateSpan) arrayList.get(i)).setOnClickListener(null);
                }
                setupEndValues();
                if (z) {
                    this.animationStarted = false;
                    AnimatorSet animatorSet = new AnimatorSet();
                    this.currentAnimation = animatorSet;
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell.SpansContainer.3
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                SpansContainer.this.removeView((View) arrayList.get(i2));
                            }
                            SpansContainer.this.removingSpans.clear();
                            SpansContainer.this.currentAnimation = null;
                            SpansContainer.this.animationStarted = false;
                            SearchUsersCell.this.editText.setAllowDrawCursor(true);
                            if (SearchUsersCell.this.updateHeight != null) {
                                SearchUsersCell.this.updateHeight.run();
                            }
                            if (SearchUsersCell.this.scroll) {
                                SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                                SearchUsersCell.this.scroll = false;
                            }
                        }
                    });
                    this.animators.clear();
                    this.animAddingSpans.clear();
                    this.animRemovingSpans.clear();
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) arrayList.get(i2);
                        this.animAddingSpans.add(groupCreateSpan);
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_X, 1.0f, 0.01f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_Y, 1.0f, 0.01f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.ALPHA, 1.0f, 0.0f));
                    }
                } else {
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        removeView((View) arrayList.get(i3));
                    }
                    this.removingSpans.clear();
                    this.currentAnimation = null;
                    this.animationStarted = false;
                    SearchUsersCell.this.editText.setAllowDrawCursor(true);
                }
                requestLayout();
            }

            public void removeSpan(final GroupCreateSpan groupCreateSpan) {
                SearchUsersCell.this.ignoreScrollEvent = true;
                SearchUsersCell.this.allSpans.remove(groupCreateSpan);
                groupCreateSpan.setOnClickListener(null);
                setupEndValues();
                this.animationStarted = false;
                AnimatorSet animatorSet = new AnimatorSet();
                this.currentAnimation = animatorSet;
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell.SpansContainer.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SpansContainer.this.removeView(groupCreateSpan);
                        SpansContainer.this.removingSpans.clear();
                        SpansContainer.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                        SearchUsersCell.this.editText.setAllowDrawCursor(true);
                        if (SearchUsersCell.this.updateHeight != null) {
                            SearchUsersCell.this.updateHeight.run();
                        }
                        if (SearchUsersCell.this.scroll) {
                            SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                            SearchUsersCell.this.scroll = false;
                        }
                    }
                });
                this.removingSpans.clear();
                this.removingSpans.add(groupCreateSpan);
                this.animAddingSpans.clear();
                this.animRemovingSpans.clear();
                this.animAddingSpans.add(groupCreateSpan);
                this.animators.clear();
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_X, 1.0f, 0.01f));
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_Y, 1.0f, 0.01f));
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.ALPHA, 1.0f, 0.0f));
                requestLayout();
            }

            public void updateSpans(final ArrayList arrayList, ArrayList arrayList2, boolean z) {
                SearchUsersCell.this.ignoreScrollEvent = true;
                SearchUsersCell.this.allSpans.removeAll(arrayList);
                SearchUsersCell.this.allSpans.addAll(arrayList2);
                this.removingSpans.clear();
                this.removingSpans.addAll(arrayList);
                for (int i = 0; i < arrayList.size(); i++) {
                    ((GroupCreateSpan) arrayList.get(i)).setOnClickListener(null);
                }
                setupEndValues();
                if (z) {
                    this.animationStarted = false;
                    AnimatorSet animatorSet = new AnimatorSet();
                    this.currentAnimation = animatorSet;
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell.SpansContainer.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                SpansContainer.this.removeView((View) arrayList.get(i2));
                            }
                            SpansContainer.this.addingSpan = null;
                            SpansContainer.this.removingSpans.clear();
                            SpansContainer.this.currentAnimation = null;
                            SpansContainer.this.animationStarted = false;
                            SearchUsersCell.this.editText.setAllowDrawCursor(true);
                            if (SearchUsersCell.this.updateHeight != null) {
                                SearchUsersCell.this.updateHeight.run();
                            }
                            if (SearchUsersCell.this.scroll) {
                                SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                                SearchUsersCell.this.scroll = false;
                            }
                        }
                    });
                    this.animators.clear();
                    this.animAddingSpans.clear();
                    this.animRemovingSpans.clear();
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) arrayList.get(i2);
                        this.animRemovingSpans.add(groupCreateSpan);
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_X, 1.0f, 0.01f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.SCALE_Y, 1.0f, 0.01f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, View.ALPHA, 1.0f, 0.0f));
                    }
                    for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                        GroupCreateSpan groupCreateSpan2 = (GroupCreateSpan) arrayList2.get(i3);
                        this.animAddingSpans.add(groupCreateSpan2);
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, View.SCALE_X, 0.01f, 1.0f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, View.SCALE_Y, 0.01f, 1.0f));
                        this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, View.ALPHA, 0.0f, 1.0f));
                    }
                } else {
                    for (int i4 = 0; i4 < arrayList.size(); i4++) {
                        removeView((View) arrayList.get(i4));
                    }
                    this.addingSpan = null;
                    this.removingSpans.clear();
                    this.currentAnimation = null;
                    this.animationStarted = false;
                    SearchUsersCell.this.editText.setAllowDrawCursor(true);
                }
                for (int i5 = 0; i5 < arrayList2.size(); i5++) {
                    addView((View) arrayList2.get(i5));
                }
                requestLayout();
            }
        }

        public SearchUsersCell(Context context, Theme.ResourcesProvider resourcesProvider, Runnable runnable) {
            super(context);
            this.allSpans = new ArrayList();
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.topGradientAlpha = new AnimatedFloat(this, 0L, 300L, cubicBezierInterpolator);
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{-16777216, 0}, new float[]{0.0f, 1.0f}, tileMode);
            this.topGradient = linearGradient;
            Paint paint = new Paint(1);
            this.topGradientPaint = paint;
            this.topGradientMatrix = new Matrix();
            this.bottomGradientAlpha = new AnimatedFloat(this, 0L, 300L, cubicBezierInterpolator);
            LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{0, -16777216}, new float[]{0.0f, 1.0f}, tileMode);
            this.bottomGradient = linearGradient2;
            Paint paint2 = new Paint(1);
            this.bottomGradientPaint = paint2;
            this.bottomGradientMatrix = new Matrix();
            paint.setShader(linearGradient);
            PorterDuff.Mode mode = PorterDuff.Mode.DST_OUT;
            paint.setXfermode(new PorterDuffXfermode(mode));
            paint2.setShader(linearGradient2);
            paint2.setXfermode(new PorterDuffXfermode(mode));
            this.resourcesProvider = resourcesProvider;
            this.updateHeight = runnable;
            setVerticalScrollBarEnabled(false);
            AndroidUtilities.setScrollViewEdgeEffectColor(this, Theme.getColor(Theme.key_windowBackgroundWhite));
            SpansContainer spansContainer = new SpansContainer(context);
            this.spansContainer = spansContainer;
            addView(spansContainer, LayoutHelper.createFrame(-1, -2.0f));
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context) { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell.1
                @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (SearchUsersCell.this.currentDeletingSpan != null) {
                        SearchUsersCell.this.currentDeletingSpan.cancelDeleteAnimation();
                        SearchUsersCell.this.currentDeletingSpan = null;
                    }
                    if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                        SearchUsersCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                        clearFocus();
                        requestFocus();
                    }
                    return super.onTouchEvent(motionEvent);
                }
            };
            this.editText = editTextBoldCursor;
            if (Build.VERSION.SDK_INT >= 25) {
                editTextBoldCursor.setRevealOnFocusHint(false);
            }
            this.editText.setTextSize(1, 16.0f);
            this.editText.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText, resourcesProvider));
            this.editText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            EditTextBoldCursor editTextBoldCursor2 = this.editText;
            int i = Theme.key_groupcreate_cursor;
            editTextBoldCursor2.setCursorColor(Theme.getColor(i, resourcesProvider));
            this.editText.setHandlesColor(Theme.getColor(i, resourcesProvider));
            this.editText.setCursorWidth(1.5f);
            this.editText.setInputType(655536);
            this.editText.setSingleLine(true);
            this.editText.setBackgroundDrawable(null);
            this.editText.setVerticalScrollBarEnabled(false);
            this.editText.setHorizontalScrollBarEnabled(false);
            this.editText.setTextIsSelectable(false);
            this.editText.setPadding(0, 0, 0, 0);
            this.editText.setImeOptions(268435462);
            this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.spansContainer.addView(this.editText);
            EditTextBoldCursor editTextBoldCursor3 = this.editText;
            int i2 = R.string.Search;
            editTextBoldCursor3.setHintText(LocaleController.getString(i2));
            this.hintTextWidth = (int) this.editText.getPaint().measureText(LocaleController.getString(i2));
            this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.SearchUsersCell.2
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (SearchUsersCell.this.ignoreTextChange || SearchUsersCell.this.onSearchTextChange == null || editable == null) {
                        return;
                    }
                    SearchUsersCell.this.onSearchTextChange.run(editable.toString());
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Animator getContainerHeightAnimator(float f) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.containerHeight, f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$SearchUsersCell$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StoryPrivacyBottomSheet.SearchUsersCell.this.lambda$getContainerHeightAnimator$0(valueAnimator);
                }
            });
            return ofFloat;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getContainerHeightAnimator$0(ValueAnimator valueAnimator) {
            setContainerHeight(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            int scrollY;
            float scrollY2 = getScrollY();
            canvas.saveLayerAlpha(0.0f, scrollY2, getWidth(), getHeight() + scrollY, NotificationCenter.closeSearchByActiveAction, 31);
            super.dispatchDraw(canvas);
            canvas.save();
            float f = this.topGradientAlpha.set(canScrollVertically(-1));
            this.topGradientMatrix.reset();
            this.topGradientMatrix.postTranslate(0.0f, scrollY2);
            this.topGradient.setLocalMatrix(this.topGradientMatrix);
            this.topGradientPaint.setAlpha((int) (f * 255.0f));
            canvas.drawRect(0.0f, scrollY2, getWidth(), AndroidUtilities.dp(8.0f) + scrollY, this.topGradientPaint);
            float f2 = this.bottomGradientAlpha.set(canScrollVertically(1));
            this.bottomGradientMatrix.reset();
            this.bottomGradientMatrix.postTranslate(0.0f, (getHeight() + scrollY) - AndroidUtilities.dp(8.0f));
            this.bottomGradient.setLocalMatrix(this.bottomGradientMatrix);
            this.bottomGradientPaint.setAlpha((int) (f2 * 255.0f));
            canvas.drawRect(0.0f, (getHeight() + scrollY) - AndroidUtilities.dp(8.0f), getWidth(), scrollY + getHeight(), this.bottomGradientPaint);
            canvas.restore();
            canvas.restore();
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(150.0f), Integer.MIN_VALUE));
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            if (this.ignoreScrollEvent) {
                this.ignoreScrollEvent = false;
                return false;
            }
            rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
            rect.top += this.fieldY + AndroidUtilities.dp(20.0f);
            rect.bottom += this.fieldY + AndroidUtilities.dp(50.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }

        public void scrollToBottom() {
            this.scroll = true;
        }

        public void setContainerHeight(float f) {
            this.containerHeight = f;
            SpansContainer spansContainer = this.spansContainer;
            if (spansContainer != null) {
                spansContainer.requestLayout();
            }
        }

        public void setOnSearchTextChange(Utilities.Callback<String> callback) {
            this.onSearchTextChange = callback;
        }

        public void setText(CharSequence charSequence) {
            this.ignoreTextChange = true;
            this.editText.setText(charSequence);
            this.ignoreTextChange = false;
        }
    }

    /* loaded from: classes4.dex */
    public static class StoryPrivacy {
        public final ArrayList rules;
        public final ArrayList selectedInputUsers;
        public final ArrayList selectedUserIds;
        public final HashMap selectedUserIdsByGroup;
        public final ArrayList sendToUsers;
        public final int type;

        public StoryPrivacy() {
            ArrayList arrayList = new ArrayList();
            this.rules = arrayList;
            this.selectedUserIds = new ArrayList();
            this.selectedUserIdsByGroup = new HashMap();
            this.selectedInputUsers = new ArrayList();
            this.sendToUsers = new ArrayList();
            this.type = 4;
            arrayList.add(new TLRPC.TL_inputPrivacyValueAllowAll());
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r6v10, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        /* JADX WARN: Type inference failed for: r6v2, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r6v5, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        /* JADX WARN: Type inference failed for: r6v7, types: [java.lang.Object] */
        public StoryPrivacy(int i, int i2, ArrayList arrayList) {
            ?? tL_inputPrivacyValueAllowUsers;
            ArrayList arrayList2 = new ArrayList();
            this.rules = arrayList2;
            this.selectedUserIds = new ArrayList();
            this.selectedUserIdsByGroup = new HashMap();
            this.selectedInputUsers = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            this.sendToUsers = arrayList3;
            this.type = i;
            int i3 = 0;
            if (i == 4) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowAll());
                if (i2 < 0 || arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                while (i3 < arrayList.size()) {
                    Long l = (Long) arrayList.get(i3);
                    long longValue = l.longValue();
                    this.selectedUserIds.add(l);
                    TLRPC.InputUser inputUser = MessagesController.getInstance(i2).getInputUser(longValue);
                    if (inputUser != null && !(inputUser instanceof TLRPC.TL_inputUserEmpty)) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser);
                        this.selectedInputUsers.add(inputUser);
                    }
                    i3++;
                }
            } else if (i == 1) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowCloseFriends());
                return;
            } else if (i == 2) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowContacts());
                if (i2 < 0 || arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                while (i3 < arrayList.size()) {
                    Long l2 = (Long) arrayList.get(i3);
                    long longValue2 = l2.longValue();
                    this.selectedUserIds.add(l2);
                    TLRPC.InputUser inputUser2 = MessagesController.getInstance(i2).getInputUser(longValue2);
                    if (inputUser2 != null && !(inputUser2 instanceof TLRPC.TL_inputUserEmpty)) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser2);
                        this.selectedInputUsers.add(inputUser2);
                    }
                    i3++;
                }
            } else if (i != 3) {
                if (i != 5 || arrayList == null) {
                    return;
                }
                arrayList3.addAll(arrayList);
                return;
            } else if (i2 < 0 || arrayList == null || arrayList.isEmpty()) {
                return;
            } else {
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueAllowUsers();
                while (i3 < arrayList.size()) {
                    Long l3 = (Long) arrayList.get(i3);
                    long longValue3 = l3.longValue();
                    this.selectedUserIds.add(l3);
                    TLRPC.InputUser inputUser3 = MessagesController.getInstance(i2).getInputUser(longValue3);
                    if (inputUser3 != null && !(inputUser3 instanceof TLRPC.TL_inputUserEmpty)) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser3);
                        this.selectedInputUsers.add(inputUser3);
                    }
                    i3++;
                }
            }
            this.rules.add(tL_inputPrivacyValueAllowUsers);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v1, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        /* JADX WARN: Type inference failed for: r0v2, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v4, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        public StoryPrivacy(int i, ArrayList arrayList) {
            ?? tL_inputPrivacyValueDisallowUsers;
            ArrayList arrayList2 = new ArrayList();
            this.rules = arrayList2;
            this.selectedUserIds = new ArrayList();
            this.selectedUserIdsByGroup = new HashMap();
            this.selectedInputUsers = new ArrayList();
            this.sendToUsers = new ArrayList();
            int i2 = 0;
            if (containsRule(arrayList, TLRPC.TL_privacyValueAllowAll.class) != null) {
                this.type = 4;
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowAll());
                TLRPC.TL_privacyValueDisallowUsers tL_privacyValueDisallowUsers = (TLRPC.TL_privacyValueDisallowUsers) containsRule(arrayList, TLRPC.TL_privacyValueDisallowUsers.class);
                if (tL_privacyValueDisallowUsers == null) {
                    return;
                }
                tL_inputPrivacyValueDisallowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                MessagesController messagesController = MessagesController.getInstance(i);
                while (i2 < tL_privacyValueDisallowUsers.users.size()) {
                    Long l = tL_privacyValueDisallowUsers.users.get(i2);
                    TLRPC.InputUser inputUser = messagesController.getInputUser(l.longValue());
                    if (!(inputUser instanceof TLRPC.TL_inputUserEmpty)) {
                        tL_inputPrivacyValueDisallowUsers.users.add(inputUser);
                        this.selectedUserIds.add(l);
                        this.selectedInputUsers.add(inputUser);
                    }
                    i2++;
                }
            } else if (containsRule(arrayList, TLRPC.TL_privacyValueAllowCloseFriends.class) != null) {
                this.type = 1;
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowCloseFriends());
                return;
            } else {
                TLRPC.TL_privacyValueAllowUsers tL_privacyValueAllowUsers = (TLRPC.TL_privacyValueAllowUsers) containsRule(arrayList, TLRPC.TL_privacyValueAllowUsers.class);
                if (tL_privacyValueAllowUsers != null) {
                    this.type = 3;
                    tL_inputPrivacyValueDisallowUsers = new TLRPC.TL_inputPrivacyValueAllowUsers();
                    MessagesController messagesController2 = MessagesController.getInstance(i);
                    while (i2 < tL_privacyValueAllowUsers.users.size()) {
                        Long l2 = tL_privacyValueAllowUsers.users.get(i2);
                        TLRPC.InputUser inputUser2 = messagesController2.getInputUser(l2.longValue());
                        if (inputUser2 != null && !(inputUser2 instanceof TLRPC.TL_inputUserEmpty)) {
                            tL_inputPrivacyValueDisallowUsers.users.add(inputUser2);
                            this.selectedUserIds.add(l2);
                            this.selectedInputUsers.add(inputUser2);
                        }
                        i2++;
                    }
                } else if (containsRule(arrayList, TLRPC.TL_privacyValueAllowContacts.class) == null) {
                    this.type = 4;
                    return;
                } else {
                    this.type = 2;
                    arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowContacts());
                    TLRPC.TL_privacyValueDisallowUsers tL_privacyValueDisallowUsers2 = (TLRPC.TL_privacyValueDisallowUsers) containsRule(arrayList, TLRPC.TL_privacyValueDisallowUsers.class);
                    if (tL_privacyValueDisallowUsers2 == null) {
                        return;
                    }
                    tL_inputPrivacyValueDisallowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                    MessagesController messagesController3 = MessagesController.getInstance(i);
                    while (i2 < tL_privacyValueDisallowUsers2.users.size()) {
                        Long l3 = tL_privacyValueDisallowUsers2.users.get(i2);
                        TLRPC.InputUser inputUser3 = messagesController3.getInputUser(l3.longValue());
                        if (!(inputUser3 instanceof TLRPC.TL_inputUserEmpty)) {
                            tL_inputPrivacyValueDisallowUsers.users.add(inputUser3);
                            this.selectedUserIds.add(l3);
                            this.selectedInputUsers.add(inputUser3);
                        }
                        i2++;
                    }
                }
            }
            this.rules.add(tL_inputPrivacyValueDisallowUsers);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r5v11, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r5v14, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        /* JADX WARN: Type inference failed for: r5v6, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r5v9, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        public StoryPrivacy(int i, ArrayList arrayList, int i2) {
            ?? tL_inputPrivacyValueAllowUsers;
            ArrayList arrayList2 = new ArrayList();
            this.rules = arrayList2;
            this.selectedUserIds = new ArrayList();
            this.selectedUserIdsByGroup = new HashMap();
            this.selectedInputUsers = new ArrayList();
            this.sendToUsers = new ArrayList();
            this.type = i;
            int i3 = 0;
            if (i == 4) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowAll());
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                while (i3 < arrayList.size()) {
                    TLRPC.InputUser inputUser = (TLRPC.InputUser) arrayList.get(i3);
                    if (inputUser != null) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser);
                        this.selectedUserIds.add(Long.valueOf(inputUser.user_id));
                        this.selectedInputUsers.add(inputUser);
                    }
                    i3++;
                }
            } else if (i == 1) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowCloseFriends());
                return;
            } else if (i == 2) {
                arrayList2.add(new TLRPC.TL_inputPrivacyValueAllowContacts());
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                while (i3 < arrayList.size()) {
                    TLRPC.InputUser inputUser2 = (TLRPC.InputUser) arrayList.get(i3);
                    if (inputUser2 != null) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser2);
                        this.selectedUserIds.add(Long.valueOf(inputUser2.user_id));
                        this.selectedInputUsers.add(inputUser2);
                    }
                    i3++;
                }
            } else if (i != 3) {
                if (i != 5 || arrayList == null) {
                    return;
                }
                while (i3 < arrayList.size()) {
                    TLRPC.InputUser inputUser3 = (TLRPC.InputUser) arrayList.get(i3);
                    if (inputUser3 != null) {
                        this.sendToUsers.add(Long.valueOf(inputUser3.user_id));
                    }
                    i3++;
                }
                return;
            } else if (arrayList == null || arrayList.isEmpty()) {
                return;
            } else {
                tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueAllowUsers();
                while (i3 < arrayList.size()) {
                    TLRPC.InputUser inputUser4 = (TLRPC.InputUser) arrayList.get(i3);
                    if (inputUser4 != null) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser4);
                        this.selectedUserIds.add(Long.valueOf(inputUser4.user_id));
                        this.selectedInputUsers.add(inputUser4);
                    }
                    i3++;
                }
            }
            this.rules.add(tL_inputPrivacyValueAllowUsers);
        }

        private Object containsRule(ArrayList arrayList, Class cls) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.PrivacyRule privacyRule = (TLRPC.PrivacyRule) arrayList.get(i);
                if (cls.isInstance(privacyRule)) {
                    return privacyRule;
                }
            }
            return null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v5, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r4v7, types: [org.telegram.tgnet.TLRPC$TL_inputPrivacyValueDisallowUsers] */
        public static ArrayList toInput(int i, ArrayList arrayList) {
            ?? tL_inputPrivacyValueAllowUsers;
            TLRPC.InputPrivacyRule tL_inputPrivacyValueAllowContacts;
            MessagesController messagesController = MessagesController.getInstance(i);
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLRPC.PrivacyRule privacyRule = (TLRPC.PrivacyRule) arrayList.get(i2);
                if (privacyRule != null) {
                    if (privacyRule instanceof TLRPC.TL_privacyValueAllowAll) {
                        tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueAllowAll();
                    } else if (privacyRule instanceof TLRPC.TL_privacyValueAllowCloseFriends) {
                        tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueAllowCloseFriends();
                    } else if (privacyRule instanceof TLRPC.TL_privacyValueAllowContacts) {
                        tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueAllowContacts();
                    } else {
                        if (privacyRule instanceof TLRPC.TL_privacyValueDisallowUsers) {
                            TLRPC.TL_privacyValueDisallowUsers tL_privacyValueDisallowUsers = (TLRPC.TL_privacyValueDisallowUsers) privacyRule;
                            tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
                            for (int i3 = 0; i3 < tL_privacyValueDisallowUsers.users.size(); i3++) {
                                TLRPC.InputUser inputUser = messagesController.getInputUser(tL_privacyValueDisallowUsers.users.get(i3).longValue());
                                if (!(inputUser instanceof TLRPC.TL_inputUserEmpty)) {
                                    tL_inputPrivacyValueAllowUsers.users.add(inputUser);
                                }
                            }
                        } else if (privacyRule instanceof TLRPC.TL_privacyValueAllowUsers) {
                            TLRPC.TL_privacyValueAllowUsers tL_privacyValueAllowUsers = (TLRPC.TL_privacyValueAllowUsers) privacyRule;
                            tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueAllowUsers();
                            for (int i4 = 0; i4 < tL_privacyValueAllowUsers.users.size(); i4++) {
                                TLRPC.InputUser inputUser2 = messagesController.getInputUser(tL_privacyValueAllowUsers.users.get(i4).longValue());
                                if (!(inputUser2 instanceof TLRPC.TL_inputUserEmpty)) {
                                    tL_inputPrivacyValueAllowUsers.users.add(inputUser2);
                                }
                            }
                        }
                        arrayList2.add(tL_inputPrivacyValueAllowUsers);
                    }
                    arrayList2.add(tL_inputPrivacyValueAllowContacts);
                }
            }
            return arrayList2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v5, types: [org.telegram.tgnet.TLRPC$TL_privacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r4v7, types: [org.telegram.tgnet.TLRPC$TL_privacyValueDisallowUsers] */
        public static ArrayList toOutput(ArrayList arrayList) {
            ?? tL_privacyValueAllowUsers;
            TLRPC.PrivacyRule tL_privacyValueAllowContacts;
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.InputPrivacyRule inputPrivacyRule = (TLRPC.InputPrivacyRule) arrayList.get(i);
                if (inputPrivacyRule != null) {
                    if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowAll) {
                        tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowAll();
                    } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowCloseFriends) {
                        tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowCloseFriends();
                    } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowContacts) {
                        tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowContacts();
                    } else {
                        if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueDisallowUsers) {
                            TLRPC.TL_inputPrivacyValueDisallowUsers tL_inputPrivacyValueDisallowUsers = (TLRPC.TL_inputPrivacyValueDisallowUsers) inputPrivacyRule;
                            tL_privacyValueAllowUsers = new TLRPC.TL_privacyValueDisallowUsers();
                            for (int i2 = 0; i2 < tL_inputPrivacyValueDisallowUsers.users.size(); i2++) {
                                tL_privacyValueAllowUsers.users.add(Long.valueOf(tL_inputPrivacyValueDisallowUsers.users.get(i2).user_id));
                            }
                        } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowUsers) {
                            TLRPC.TL_inputPrivacyValueAllowUsers tL_inputPrivacyValueAllowUsers = (TLRPC.TL_inputPrivacyValueAllowUsers) inputPrivacyRule;
                            tL_privacyValueAllowUsers = new TLRPC.TL_privacyValueAllowUsers();
                            for (int i3 = 0; i3 < tL_inputPrivacyValueAllowUsers.users.size(); i3++) {
                                tL_privacyValueAllowUsers.users.add(Long.valueOf(tL_inputPrivacyValueAllowUsers.users.get(i3).user_id));
                            }
                        }
                        arrayList2.add(tL_privacyValueAllowUsers);
                    }
                    arrayList2.add(tL_privacyValueAllowContacts);
                }
            }
            return arrayList2;
        }

        public boolean containsUser(TLRPC.User user) {
            if (user == null) {
                return false;
            }
            int i = this.type;
            if (i == 4) {
                return !this.selectedUserIds.contains(Long.valueOf(user.id));
            }
            if (i == 2) {
                return !this.selectedUserIds.contains(Long.valueOf(user.id)) && user.contact;
            } else if (i == 1) {
                return user.close_friend;
            } else {
                if (i == 3) {
                    if (this.selectedUserIds.contains(Long.valueOf(user.id))) {
                        return true;
                    }
                    for (ArrayList arrayList : this.selectedUserIdsByGroup.values()) {
                        if (arrayList.contains(Long.valueOf(user.id))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        public boolean isCloseFriends() {
            return this.type == 1;
        }

        public boolean isNone() {
            return this.sendToUsers.isEmpty() && this.rules.isEmpty();
        }

        public boolean isShare() {
            return this.type == 5;
        }

        public String toString() {
            int size;
            TLRPC.InputPrivacyRule inputPrivacyRule;
            int size2;
            int size3;
            if (this.sendToUsers.isEmpty()) {
                if (this.rules.isEmpty()) {
                    return LocaleController.getString(R.string.StoryPrivacyNone);
                }
                TLRPC.InputPrivacyRule inputPrivacyRule2 = (TLRPC.InputPrivacyRule) this.rules.get(0);
                int i = this.type;
                if (i == 4) {
                    inputPrivacyRule = this.rules.size() >= 2 ? (TLRPC.InputPrivacyRule) this.rules.get(1) : null;
                    return (!(inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueDisallowUsers) || (size3 = ((TLRPC.TL_inputPrivacyValueDisallowUsers) inputPrivacyRule).users.size()) <= 0) ? LocaleController.getString(R.string.StoryPrivacyEveryone) : LocaleController.formatPluralString("StoryPrivacyEveryoneExclude", size3, new Object[0]);
                } else if (i == 1) {
                    return LocaleController.getString(R.string.StoryPrivacyCloseFriends);
                } else {
                    if (i == 3 && (inputPrivacyRule2 instanceof TLRPC.TL_inputPrivacyValueAllowUsers)) {
                        return LocaleController.formatPluralString("StoryPrivacyContacts", ((TLRPC.TL_inputPrivacyValueAllowUsers) inputPrivacyRule2).users.size(), new Object[0]);
                    }
                    if (i == 2) {
                        inputPrivacyRule = this.rules.size() >= 2 ? (TLRPC.InputPrivacyRule) this.rules.get(1) : null;
                        if ((inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueDisallowUsers) && (size2 = ((TLRPC.TL_inputPrivacyValueDisallowUsers) inputPrivacyRule).users.size()) > 0) {
                            return LocaleController.formatPluralString("StoryPrivacyContactsExclude", size2, new Object[0]);
                        }
                        return LocaleController.getString(R.string.StoryPrivacyAllContacts);
                    }
                    if (i == 0 && (inputPrivacyRule2 instanceof TLRPC.TL_inputPrivacyValueAllowUsers) && (size = ((TLRPC.TL_inputPrivacyValueAllowUsers) inputPrivacyRule2).users.size()) > 0) {
                        return LocaleController.formatPluralString("StoryPrivacyContacts", size, new Object[0]);
                    }
                    return LocaleController.getString(R.string.StoryPrivacyNone);
                }
            }
            return LocaleController.formatPluralString("StoryPrivacyRecipients", this.sendToUsers.size(), new Object[0]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v5, types: [org.telegram.tgnet.TLRPC$TL_privacyValueAllowUsers] */
        /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r4v7, types: [org.telegram.tgnet.TLRPC$TL_privacyValueDisallowUsers] */
        public ArrayList toValue() {
            ?? tL_privacyValueAllowUsers;
            TLRPC.PrivacyRule tL_privacyValueAllowContacts;
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.rules.size(); i++) {
                TLRPC.InputPrivacyRule inputPrivacyRule = (TLRPC.InputPrivacyRule) this.rules.get(i);
                if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowAll) {
                    tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowAll();
                } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowCloseFriends) {
                    tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowCloseFriends();
                } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowContacts) {
                    tL_privacyValueAllowContacts = new TLRPC.TL_privacyValueAllowContacts();
                } else {
                    if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueDisallowUsers) {
                        TLRPC.TL_inputPrivacyValueDisallowUsers tL_inputPrivacyValueDisallowUsers = (TLRPC.TL_inputPrivacyValueDisallowUsers) inputPrivacyRule;
                        tL_privacyValueAllowUsers = new TLRPC.TL_privacyValueDisallowUsers();
                        for (int i2 = 0; i2 < tL_inputPrivacyValueDisallowUsers.users.size(); i2++) {
                            tL_privacyValueAllowUsers.users.add(Long.valueOf(tL_inputPrivacyValueDisallowUsers.users.get(i2).user_id));
                        }
                    } else if (inputPrivacyRule instanceof TLRPC.TL_inputPrivacyValueAllowUsers) {
                        TLRPC.TL_inputPrivacyValueAllowUsers tL_inputPrivacyValueAllowUsers = (TLRPC.TL_inputPrivacyValueAllowUsers) inputPrivacyRule;
                        tL_privacyValueAllowUsers = new TLRPC.TL_privacyValueAllowUsers();
                        for (int i3 = 0; i3 < tL_inputPrivacyValueAllowUsers.users.size(); i3++) {
                            tL_privacyValueAllowUsers.users.add(Long.valueOf(tL_inputPrivacyValueAllowUsers.users.get(i3).user_id));
                        }
                    }
                    arrayList.add(tL_privacyValueAllowUsers);
                }
                arrayList.add(tL_privacyValueAllowContacts);
            }
            return arrayList;
        }
    }

    /* loaded from: classes4.dex */
    public static class UserCell extends FrameLayout {
        private Paint arrowPaint;
        private Path arrowPath;
        private final AvatarDrawable avatarDrawable;
        public final CheckBox2 checkBox;
        public long dialogId;
        private final Paint dividerPaint;
        private boolean drawArrow;
        private final BackupImageView imageView;
        private boolean[] isOnline;
        private boolean needCheck;
        private boolean needDivider;
        public final RadioButton radioButton;
        private final Theme.ResourcesProvider resourcesProvider;
        private boolean sendAs;
        private final SimpleTextView subtitleTextView;
        private final SimpleTextView titleTextView;

        public UserCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            this.avatarDrawable = avatarDrawable;
            this.dividerPaint = new Paint(1);
            this.sendAs = false;
            this.needCheck = true;
            this.drawArrow = true;
            this.isOnline = new boolean[1];
            this.resourcesProvider = resourcesProvider;
            avatarDrawable.setRoundRadius(AndroidUtilities.dp(40.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            addView(backupImageView);
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.titleTextView = simpleTextView;
            simpleTextView.setTypeface(AndroidUtilities.bold());
            simpleTextView.setTextSize(16);
            int i = Theme.key_dialogTextBlack;
            simpleTextView.setTextColor(Theme.getColor(i, resourcesProvider));
            simpleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            NotificationCenter.listenEmojiLoading(simpleTextView);
            addView(simpleTextView);
            SimpleTextView simpleTextView2 = new SimpleTextView(context);
            this.subtitleTextView = simpleTextView2;
            simpleTextView2.setTextSize(14);
            simpleTextView2.setTextColor(Theme.getColor(i, resourcesProvider));
            simpleTextView2.setGravity(LocaleController.isRTL ? 5 : 3);
            NotificationCenter.listenEmojiLoading(simpleTextView2);
            addView(simpleTextView2);
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            int i2 = Theme.key_dialogRoundCheckBox;
            int i3 = Theme.key_checkboxDisabled;
            checkBox2.setColor(i2, i3, Theme.key_dialogRoundCheckBoxCheck);
            checkBox2.setDrawUnchecked(true);
            checkBox2.setDrawBackgroundAsArc(10);
            addView(checkBox2);
            checkBox2.setChecked(false, false);
            checkBox2.setVisibility(8);
            RadioButton radioButton = new RadioButton(context);
            this.radioButton = radioButton;
            radioButton.setSize(AndroidUtilities.dp(20.0f));
            radioButton.setColor(Theme.getColor(i3, resourcesProvider), Theme.getColor(Theme.key_dialogRadioBackgroundChecked, resourcesProvider));
            addView(radioButton);
            radioButton.setVisibility(8);
            updateLayouts();
        }

        private void setSubtitle(CharSequence charSequence) {
            SimpleTextView simpleTextView;
            int i;
            if (charSequence == null) {
                this.titleTextView.setTranslationY(0.0f);
                simpleTextView = this.subtitleTextView;
                i = 8;
            } else {
                this.titleTextView.setTranslationY(AndroidUtilities.dp(-9.0f));
                this.subtitleTextView.setTranslationY(AndroidUtilities.dp(12.0f));
                this.subtitleTextView.setText(charSequence);
                simpleTextView = this.subtitleTextView;
                i = 0;
            }
            simpleTextView.setVisibility(i);
        }

        private void updateLayouts() {
            float f;
            float f2;
            float f3;
            float f4;
            BackupImageView backupImageView = this.imageView;
            int i = (LocaleController.isRTL ? 5 : 3) | 16;
            boolean z = this.needCheck;
            backupImageView.setLayoutParams(LayoutHelper.createFrame(40, 40.0f, i, z ? 53.0f : 16.0f, 0.0f, z ? 53.0f : 16.0f, 0.0f));
            SimpleTextView simpleTextView = this.titleTextView;
            boolean z2 = LocaleController.isRTL;
            int i2 = (z2 ? 5 : 3) | 16;
            if (z2) {
                f = 20.0f;
            } else {
                f = this.needCheck ? 105 : 68;
            }
            if (z2) {
                f2 = this.needCheck ? 105 : 68;
            } else {
                f2 = 20.0f;
            }
            simpleTextView.setLayoutParams(LayoutHelper.createFrame(-1, -2.0f, i2, f, 0.0f, f2, 0.0f));
            SimpleTextView simpleTextView2 = this.subtitleTextView;
            boolean z3 = LocaleController.isRTL;
            int i3 = (z3 ? 5 : 3) | 16;
            if (z3) {
                f3 = 20.0f;
            } else {
                f3 = this.needCheck ? 105 : 68;
            }
            if (z3) {
                f4 = this.needCheck ? 105 : 68;
            } else {
                f4 = 20.0f;
            }
            simpleTextView2.setLayoutParams(LayoutHelper.createFrame(-1, -2.0f, i3, f3, 0.0f, f4, 0.0f));
            this.checkBox.setLayoutParams(LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 16, 13.0f, 0.0f, 14.0f, 0.0f));
            this.radioButton.setLayoutParams(LayoutHelper.createFrame(22, 22.0f, (LocaleController.isRTL ? 5 : 3) | 16, 14.0f, 0.0f, 15.0f, 0.0f));
        }

        private CharSequence withArrow(CharSequence charSequence) {
            SpannableString spannableString = new SpannableString(">");
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.attach_arrow_right);
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(drawable, 2);
            drawable.setBounds(0, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(12.0f));
            spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(charSequence).append((CharSequence) " ").append((CharSequence) spannableString);
            return spannableStringBuilder;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Paint paint;
            super.onDraw(canvas);
            if (this.needDivider) {
                this.dividerPaint.setColor(Theme.getColor(Theme.key_divider, this.resourcesProvider));
                if (LocaleController.isRTL) {
                    canvas.drawRect(0.0f, getHeight() - 1, getWidth() - AndroidUtilities.dp(105.0f), getHeight(), this.dividerPaint);
                } else {
                    canvas.drawRect(AndroidUtilities.dp(105.0f), getHeight() - 1, getWidth(), getHeight(), this.dividerPaint);
                }
            }
            Path path = this.arrowPath;
            if (path == null || (paint = this.arrowPaint) == null || this.needCheck || !this.sendAs || !this.drawArrow) {
                return;
            }
            canvas.drawPath(path, paint);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((!this.sendAs || this.needCheck) ? 56.0f : 62.0f), 1073741824));
            if (this.needCheck || !this.sendAs) {
                return;
            }
            Path path = this.arrowPath;
            if (path == null) {
                this.arrowPath = new Path();
            } else {
                path.rewind();
            }
            float dp = LocaleController.isRTL ? AndroidUtilities.dp(31.0f) : getMeasuredWidth() - AndroidUtilities.dp(31.0f);
            float measuredHeight = getMeasuredHeight() / 2.0f;
            float f = LocaleController.isRTL ? -1.0f : 1.0f;
            this.arrowPath.moveTo(dp, measuredHeight - AndroidUtilities.dp(6.0f));
            this.arrowPath.lineTo((f * AndroidUtilities.dp(6.0f)) + dp, measuredHeight);
            this.arrowPath.lineTo(dp, measuredHeight + AndroidUtilities.dp(6.0f));
            if (this.arrowPaint == null) {
                Paint paint = new Paint(1);
                this.arrowPaint = paint;
                paint.setStyle(Paint.Style.STROKE);
                this.arrowPaint.setStrokeCap(Paint.Cap.ROUND);
            }
            this.arrowPaint.setStrokeWidth(AndroidUtilities.dpf2(1.86f));
            this.arrowPaint.setColor(Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider), 0.3f));
        }

        public void set(Object obj) {
            if (obj instanceof TLRPC.User) {
                this.titleTextView.setTypeface(AndroidUtilities.bold());
                this.titleTextView.setTranslationX(0.0f);
                setUser((TLRPC.User) obj);
            } else if (obj instanceof TLRPC.Chat) {
                this.titleTextView.setTypeface(AndroidUtilities.bold());
                this.titleTextView.setTranslationX(0.0f);
                setChat((TLRPC.Chat) obj, 0);
            } else if (obj instanceof String) {
                this.titleTextView.setTypeface(null);
                this.titleTextView.setTranslationX((-AndroidUtilities.dp(52.0f)) * (LocaleController.isRTL ? -1 : 1));
                this.titleTextView.setText((String) obj);
            }
        }

        public void setChat(TLRPC.Chat chat, int i) {
            int i2;
            int i3;
            String formatPluralStringComma;
            this.dialogId = chat == null ? 0L : -chat.id;
            this.avatarDrawable.setInfo(chat);
            this.imageView.setRoundRadius(AndroidUtilities.dp(ChatObject.isForum(chat) ? 12.0f : 20.0f));
            this.imageView.setForUserOrChat(chat, this.avatarDrawable);
            this.titleTextView.setText(Emoji.replaceEmoji(chat.title, this.titleTextView.getPaint().getFontMetricsInt(), false));
            this.isOnline[0] = false;
            if (this.sendAs) {
                if (i <= 0) {
                    i = chat.participants_count;
                }
                boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(chat);
                if (i >= 1) {
                    formatPluralStringComma = LocaleController.formatPluralString(isChannelAndNotMegaGroup ? "Subscribers" : "Members", i, new Object[0]);
                } else {
                    i3 = isChannelAndNotMegaGroup ? R.string.DiscussChannel : R.string.AccDescrGroup;
                    formatPluralStringComma = LocaleController.getString(i3);
                }
            } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                if (i >= 1) {
                    formatPluralStringComma = LocaleController.formatPluralStringComma("Members", i - 1);
                } else if (chat.has_geo) {
                    i3 = R.string.MegaLocation;
                    formatPluralStringComma = LocaleController.getString(i3);
                } else {
                    i2 = !ChatObject.isPublic(chat) ? R.string.MegaPrivate : R.string.MegaPublic;
                    formatPluralStringComma = LocaleController.getString(i2).toLowerCase();
                }
            } else if (i >= 1) {
                formatPluralStringComma = LocaleController.formatPluralStringComma("Subscribers", i - 1);
            } else {
                i2 = !ChatObject.isPublic(chat) ? R.string.ChannelPrivate : R.string.ChannelPublic;
                formatPluralStringComma = LocaleController.getString(i2).toLowerCase();
            }
            setSubtitle(formatPluralStringComma);
            this.subtitleTextView.setTextColor(Theme.getColor(this.isOnline[0] ? Theme.key_dialogTextBlue2 : Theme.key_dialogTextGray3, this.resourcesProvider));
            this.checkBox.setVisibility(this.needCheck ? 0 : 8);
            this.radioButton.setVisibility(8);
            setCheckboxAlpha(i > 200 ? 0.3f : 1.0f, false);
        }

        public void setCheckboxAlpha(float f, boolean z) {
            if (!z) {
                this.checkBox.animate().cancel();
                this.checkBox.setAlpha(f);
                this.radioButton.animate().cancel();
                this.radioButton.setAlpha(f);
                return;
            }
            if (Math.abs(this.checkBox.getAlpha() - f) > 0.1d) {
                this.checkBox.animate().cancel();
                this.checkBox.animate().alpha(f).start();
            }
            if (Math.abs(this.radioButton.getAlpha() - f) > 0.1d) {
                this.radioButton.animate().cancel();
                this.radioButton.animate().alpha(f).start();
            }
        }

        public void setChecked(boolean z, boolean z2) {
            if (this.checkBox.getVisibility() == 0) {
                this.checkBox.setChecked(z, z2);
            }
            if (this.radioButton.getVisibility() == 0) {
                this.radioButton.setChecked(z, z2);
            }
        }

        public void setDivider(boolean z) {
            this.needDivider = z;
            setWillNotDraw(!z && (this.needCheck || !this.sendAs));
        }

        public void setIsSendAs(boolean z, boolean z2) {
            this.sendAs = z;
            if (z2 != this.needCheck) {
                this.needCheck = z2;
                updateLayouts();
            }
            if (!this.needCheck) {
                this.radioButton.setVisibility(8);
                this.checkBox.setVisibility(8);
            }
            setWillNotDraw(!this.needDivider && (this.needCheck || !this.sendAs));
        }

        public void setRedCheckbox(boolean z) {
            this.checkBox.setColor(z ? Theme.key_color_red : Theme.key_dialogRoundCheckBox, Theme.key_checkboxDisabled, Theme.key_dialogRoundCheckBoxCheck);
        }

        /* JADX WARN: Removed duplicated region for block: B:50:0x018c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setType(int i, int i2, TLRPC.User user) {
            AvatarDrawable avatarDrawable;
            int i3;
            int i4;
            if (i == 4) {
                this.titleTextView.setText(LocaleController.getString(R.string.StoryPrivacyOptionEveryone));
                setSubtitle(withArrow((i2 != 1 || user == null) ? i2 > 0 ? LocaleController.formatPluralString("StoryPrivacyOptionExcludePeople", i2, new Object[0]) : LocaleController.getString(R.string.StoryPrivacyOptionContactsDetail) : Emoji.replaceEmoji(LocaleController.formatString(R.string.StoryPrivacyOptionExcludePerson, UserObject.getUserName(user)), this.subtitleTextView.getPaint().getFontMetricsInt(), false)));
                this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
                this.avatarDrawable.setAvatarType(7);
                avatarDrawable = this.avatarDrawable;
                i3 = -15292942;
                i4 = -15630089;
            } else if (i == 2) {
                this.titleTextView.setText(LocaleController.getString(R.string.StoryPrivacyOptionContacts));
                setSubtitle(withArrow((i2 != 1 || user == null) ? i2 > 0 ? LocaleController.formatPluralString("StoryPrivacyOptionExcludePeople", i2, new Object[0]) : LocaleController.getString(R.string.StoryPrivacyOptionContactsDetail) : Emoji.replaceEmoji(LocaleController.formatString(R.string.StoryPrivacyOptionExcludePerson, UserObject.getUserName(user)), this.subtitleTextView.getPaint().getFontMetricsInt(), false)));
                this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
                this.avatarDrawable.setAvatarType(4);
                avatarDrawable = this.avatarDrawable;
                i3 = -3905294;
                i4 = -6923014;
            } else if (i != 1) {
                if (i == 3) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StoryPrivacyOptionSelectedContacts));
                    setSubtitle(withArrow((i2 != 1 || user == null) ? i2 > 0 ? LocaleController.formatPluralString("StoryPrivacyOptionPeople", i2, new Object[0]) : LocaleController.getString(R.string.StoryPrivacyOptionSelectedContactsDetail) : Emoji.replaceEmoji(UserObject.getUserName(user), this.subtitleTextView.getPaint().getFontMetricsInt(), false)));
                    this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
                    this.avatarDrawable.setAvatarType(6);
                    avatarDrawable = this.avatarDrawable;
                    i3 = -18621;
                    i4 = -618956;
                }
                this.checkBox.setVisibility(8);
                this.radioButton.setVisibility(this.needCheck ? 0 : 8);
                this.imageView.setImageDrawable(this.avatarDrawable);
                this.imageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            } else {
                this.titleTextView.setText(LocaleController.getString(R.string.StoryPrivacyOptionCloseFriends));
                setSubtitle(withArrow((i2 != 1 || user == null) ? i2 > 0 ? LocaleController.formatPluralString("StoryPrivacyOptionPeople", i2, new Object[0]) : LocaleController.getString(R.string.StoryPrivacyOptionCloseFriendsDetail) : Emoji.replaceEmoji(UserObject.getUserName(user), this.subtitleTextView.getPaint().getFontMetricsInt(), false)));
                this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
                this.avatarDrawable.setAvatarType(15);
                avatarDrawable = this.avatarDrawable;
                i3 = -7808710;
                i4 = -13781445;
            }
            avatarDrawable.setColor(i3, i4);
            this.checkBox.setVisibility(8);
            this.radioButton.setVisibility(this.needCheck ? 0 : 8);
            this.imageView.setImageDrawable(this.avatarDrawable);
            this.imageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0074  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setUser(TLRPC.User user) {
            SimpleTextView simpleTextView;
            int i;
            this.dialogId = user == null ? 0L : user.id;
            this.avatarDrawable.setInfo(user);
            this.imageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            this.imageView.setForUserOrChat(user, this.avatarDrawable);
            this.titleTextView.setText(Emoji.replaceEmoji(UserObject.getUserName(user), this.titleTextView.getPaint().getFontMetricsInt(), false));
            boolean[] zArr = this.isOnline;
            zArr[0] = false;
            if (this.sendAs) {
                setSubtitle(LocaleController.getString(R.string.VoipGroupPersonalAccount));
                simpleTextView = this.subtitleTextView;
            } else {
                setSubtitle(LocaleController.formatUserStatus(UserConfig.selectedAccount, user, zArr));
                simpleTextView = this.subtitleTextView;
                if (this.isOnline[0]) {
                    i = Theme.key_dialogTextBlue2;
                    simpleTextView.setTextColor(Theme.getColor(i, this.resourcesProvider));
                    this.checkBox.setVisibility(this.needCheck ? 0 : 8);
                    this.checkBox.setAlpha(1.0f);
                    this.radioButton.setVisibility(8);
                }
            }
            i = Theme.key_dialogTextGray3;
            simpleTextView.setTextColor(Theme.getColor(i, this.resourcesProvider));
            this.checkBox.setVisibility(this.needCheck ? 0 : 8);
            this.checkBox.setAlpha(1.0f);
            this.radioButton.setVisibility(8);
        }
    }

    private StoryPrivacyBottomSheet(final int i, final Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        this.excludedEveryone = new ArrayList();
        this.excludedEveryoneByGroup = new HashMap();
        this.excludedEveryoneCount = 0;
        this.excludedContacts = new ArrayList();
        this.selectedContacts = new ArrayList();
        this.selectedContactsByGroup = new HashMap();
        this.selectedContactsCount = 0;
        this.allowScreenshots = true;
        this.keepOnMyPage = false;
        this.canChangePeer = true;
        this.messageUsers = new ArrayList();
        this.activePage = 1;
        this.selectedType = 4;
        this.sendAsMessageEnabled = false;
        this.smallChatsParticipantsCount = new HashMap();
        this.shiftDp = -6;
        this.storyPeriod = 86400;
        this.backgroundPaint = new Paint(1);
        this.applyWhenDismiss = false;
        this.allowSmallChats = true;
        this.isEdit = false;
        init(context);
        this.viewPager.setAdapter(new ViewPagerFixed.Adapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.4
            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i2, int i3) {
                ((Page) view).bind(i3);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i2) {
                return new Page(context);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return 1;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i2) {
                return i;
            }
        });
    }

    public StoryPrivacyBottomSheet(final Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        this.excludedEveryone = new ArrayList();
        this.excludedEveryoneByGroup = new HashMap();
        this.excludedEveryoneCount = 0;
        this.excludedContacts = new ArrayList();
        this.selectedContacts = new ArrayList();
        this.selectedContactsByGroup = new HashMap();
        this.selectedContactsCount = 0;
        this.allowScreenshots = true;
        this.keepOnMyPage = false;
        this.canChangePeer = true;
        this.messageUsers = new ArrayList();
        this.activePage = 1;
        this.selectedType = 4;
        this.sendAsMessageEnabled = false;
        this.smallChatsParticipantsCount = new HashMap();
        this.shiftDp = -6;
        this.storyPeriod = 86400;
        this.backgroundPaint = new Paint(1);
        this.applyWhenDismiss = false;
        this.allowSmallChats = true;
        this.isEdit = false;
        this.storyPeriod = i;
        pullSaved();
        init(context);
        this.viewPager.setAdapter(new ViewPagerFixed.Adapter() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.1
            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i2, int i3) {
                ((Page) view).bind(i3);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i2) {
                return new Page(context);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return 2;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i2) {
                if (i2 == 0) {
                    return 0;
                }
                return StoryPrivacyBottomSheet.this.activePage;
            }
        });
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StoryPrivacyBottomSheet.this.lambda$new$1(messagesStorage);
            }
        });
        MessagesController.getInstance(this.currentAccount).getStoriesController().loadBlocklist(false);
        MessagesController.getInstance(this.currentAccount).getStoriesController().loadSendAs();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void done(StoryPrivacy storyPrivacy, Runnable runnable) {
        done(storyPrivacy, runnable, false);
    }

    private void done(final StoryPrivacy storyPrivacy, final Runnable runnable, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (this.warnUsers != null && storyPrivacy != null) {
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            for (int i = 0; i < this.warnUsers.size(); i++) {
                String str = (String) this.warnUsers.get(i);
                TLObject userOrChat = messagesController.getUserOrChat(str);
                if (userOrChat instanceof TLRPC.User) {
                    TLRPC.User user = (TLRPC.User) userOrChat;
                    TLRPC.User user2 = messagesController.getUser(Long.valueOf(user.id));
                    if (user2 != null) {
                        user = user2;
                    }
                    if (!user.bot && !storyPrivacy.containsUser(user)) {
                        arrayList.add(str);
                    }
                }
            }
        }
        if (arrayList.isEmpty() || z) {
            View view = this.viewPager.getViewPages()[0];
            final ButtonWithCounterView buttonWithCounterView = view instanceof Page ? ((Page) view).button : null;
            if (runnable != null && buttonWithCounterView != null) {
                buttonWithCounterView.setLoading(true);
            }
            DoneCallback doneCallback = this.onDone;
            if (doneCallback != null) {
                doneCallback.done(storyPrivacy, this.allowScreenshots, this.keepOnMyPage, this.selectedPeer, runnable != null ? new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryPrivacyBottomSheet.lambda$done$3(ButtonWithCounterView.this, runnable);
                    }
                } : null);
                return;
            } else if (runnable != null) {
                runnable.run();
                return;
            } else {
                return;
            }
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int i2 = 0; i2 < Math.min(2, arrayList.size()); i2++) {
            if (i2 > 0) {
                spannableStringBuilder.append((CharSequence) ", ");
            }
            SpannableString spannableString = new SpannableString("@" + ((String) arrayList.get(i2)));
            spannableString.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString.length(), 33);
            spannableStringBuilder.append((CharSequence) spannableString);
        }
        new AlertDialog.Builder(getContext(), this.resourcesProvider).setTitle(LocaleController.getString(R.string.StoryRestrictions)).setMessage(AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.StoryRestrictionsInfo), spannableStringBuilder)).setPositiveButton(LocaleController.getString(R.string.Proceed), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                StoryPrivacyBottomSheet.this.lambda$done$2(storyPrivacy, runnable, dialogInterface, i3);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getChats() {
        TLRPC.Chat chat;
        TLRPC.Chat chat2;
        ArrayList arrayList = new ArrayList();
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList<TLRPC.Dialog> allDialogs = messagesController.getAllDialogs();
        for (int i = 0; i < allDialogs.size(); i++) {
            TLRPC.Dialog dialog = allDialogs.get(i);
            if (messagesController.canAddToForward(dialog)) {
                if (DialogObject.isUserDialog(dialog.id)) {
                    TLRPC.User user = messagesController.getUser(Long.valueOf(dialog.id));
                    if (user != null && !user.bot && user.id != 777000) {
                        boolean isUserSelf = UserObject.isUserSelf(user);
                        chat2 = user;
                        if (isUserSelf) {
                        }
                        arrayList.add(chat2);
                    }
                } else if (DialogObject.isChatDialog(dialog.id) && (chat = messagesController.getChat(Long.valueOf(-dialog.id))) != null) {
                    boolean isForum = ChatObject.isForum(chat);
                    chat2 = chat;
                    if (isForum) {
                    }
                    arrayList.add(chat2);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getCloseFriends() {
        TLRPC.User user;
        ArrayList contacts = getContacts();
        int i = 0;
        while (i < contacts.size()) {
            TLObject tLObject = (TLObject) contacts.get(i);
            if ((tLObject instanceof TLRPC.User) && ((user = (TLRPC.User) tLObject) == null || !user.close_friend)) {
                contacts.remove(i);
                i--;
            }
            i++;
        }
        return contacts;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getContacts() {
        TLRPC.User user;
        ArrayList arrayList = new ArrayList();
        ArrayList<TLRPC.TL_contact> arrayList2 = ContactsController.getInstance(this.currentAccount).contacts;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            ContactsController.getInstance(this.currentAccount).loadContacts(false, 0L);
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (arrayList2 != null) {
            for (int i = 0; i < arrayList2.size(); i++) {
                TLRPC.TL_contact tL_contact = arrayList2.get(i);
                if (tL_contact != null && (user = messagesController.getUser(Long.valueOf(tL_contact.user_id))) != null && !UserObject.isUserSelf(user) && !user.bot && user.id != 777000) {
                    arrayList.add(user);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getParticipantsCount(TLRPC.Chat chat) {
        Integer num;
        int i;
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(chat.id);
        if (chatFull == null || (i = chatFull.participants_count) <= 0) {
            HashMap hashMap = this.smallChatsParticipantsCount;
            return (hashMap == null || (num = (Integer) hashMap.get(Long.valueOf(chat.id))) == null) ? chat.participants_count : num.intValue();
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getUsers(boolean z, boolean z2) {
        TLRPC.User user;
        TLRPC.Chat chat;
        long j;
        TLRPC.Chat chat2;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList<TLRPC.Dialog> allDialogs = messagesController.getAllDialogs();
        ConcurrentHashMap<Long, TLRPC.TL_contact> concurrentHashMap = ContactsController.getInstance(this.currentAccount).contactsDict;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            if (!this.loadedContacts) {
                ContactsController.getInstance(this.currentAccount).loadContacts(false, 0L);
            }
            this.loadedContacts = true;
        }
        for (int i = 0; i < allDialogs.size(); i++) {
            TLRPC.Dialog dialog = allDialogs.get(i);
            if (DialogObject.isUserDialog(dialog.id)) {
                TLRPC.User user2 = messagesController.getUser(Long.valueOf(dialog.id));
                if (user2 != null && !user2.bot && user2.id != 777000 && !UserObject.isUserSelf(user2) && !user2.deleted && (!z || (concurrentHashMap != null && concurrentHashMap.get(Long.valueOf(user2.id)) != null))) {
                    j = user2.id;
                    chat2 = user2;
                    hashMap.put(Long.valueOf(j), Boolean.TRUE);
                    arrayList.add(chat2);
                }
            } else if (z2 && DialogObject.isChatDialog(dialog.id) && (chat = messagesController.getChat(Long.valueOf(-dialog.id))) != null && !ChatObject.isChannelAndNotMegaGroup(chat)) {
                j = -chat.id;
                chat2 = chat;
                hashMap.put(Long.valueOf(j), Boolean.TRUE);
                arrayList.add(chat2);
            }
        }
        if (concurrentHashMap != null) {
            for (Map.Entry<Long, TLRPC.TL_contact> entry : concurrentHashMap.entrySet()) {
                Long key = entry.getKey();
                key.longValue();
                if (!hashMap.containsKey(key) && (user = messagesController.getUser(key)) != null && !user.bot && user.id != 777000 && !UserObject.isUserSelf(user)) {
                    arrayList.add(user);
                    hashMap.put(Long.valueOf(user.id), Boolean.TRUE);
                }
            }
        }
        return arrayList;
    }

    private void init(Context context) {
        Bulletin.addDelegate(this.container, new Bulletin.Delegate() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.2
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i) {
                return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getBottomOffset(int i) {
                return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getTopOffset(int i) {
                return AndroidUtilities.statusBarHeight;
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
            }
        });
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesBlocklistUpdate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesSendAsUpdate);
        Paint paint = this.backgroundPaint;
        int i = Theme.key_dialogBackground;
        paint.setColor(Theme.getColor(i, this.resourcesProvider));
        fixNavigationBar(Theme.getColor(i, this.resourcesProvider));
        this.containerView = new ContainerView(context);
        ViewPagerFixed viewPagerFixed = new ViewPagerFixed(context) { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.3
            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected boolean canScroll(MotionEvent motionEvent) {
                View currentView = StoryPrivacyBottomSheet.this.viewPager.getCurrentView();
                if (currentView instanceof Page) {
                    if (getCurrentPosition() > 0) {
                        StoryPrivacyBottomSheet.this.closeKeyboard();
                        return true;
                    }
                    Page page = (Page) currentView;
                    int typeOn = page.getTypeOn(motionEvent);
                    if (typeOn != -1) {
                        StoryPrivacyBottomSheet.this.activePage = typeOn;
                        if (typeOn != 3 ? typeOn != 4 || (!StoryPrivacyBottomSheet.this.excludedEveryone.isEmpty() && !StoryPrivacyBottomSheet.this.excludedEveryoneByGroup.isEmpty()) : !StoryPrivacyBottomSheet.this.selectedContacts.isEmpty() && !StoryPrivacyBottomSheet.this.selectedContactsByGroup.isEmpty()) {
                            StoryPrivacyBottomSheet.this.selectedType = typeOn;
                        }
                        page.updateCheckboxes(true);
                        page.updateButton(true);
                    }
                    if (typeOn != -1) {
                        StoryPrivacyBottomSheet.this.closeKeyboard();
                    }
                    return typeOn != -1;
                }
                return true;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onItemSelected(View view, View view2, int i2, int i3) {
                if (((BottomSheet) StoryPrivacyBottomSheet.this).keyboardVisible) {
                    StoryPrivacyBottomSheet.this.closeKeyboard();
                }
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onTabAnimationUpdate(boolean z) {
                ((BottomSheet) StoryPrivacyBottomSheet.this).containerView.invalidate();
            }
        };
        this.viewPager = viewPagerFixed;
        int i2 = this.backgroundPaddingLeft;
        viewPagerFixed.setPadding(i2, 0, i2, 0);
        this.containerView.addView(this.viewPager, LayoutHelper.createFrame(-1, -1, 119));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$done$2(StoryPrivacy storyPrivacy, Runnable runnable, DialogInterface dialogInterface, int i) {
        done(storyPrivacy, runnable, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$done$3(ButtonWithCounterView buttonWithCounterView, Runnable runnable) {
        if (buttonWithCounterView != null) {
            buttonWithCounterView.setLoading(false);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(HashMap hashMap) {
        if (this.smallChatsParticipantsCount == null) {
            this.smallChatsParticipantsCount = new HashMap();
        }
        this.smallChatsParticipantsCount.putAll(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(MessagesStorage messagesStorage) {
        final HashMap<Long, Integer> smallGroupsParticipantsCount = messagesStorage.getSmallGroupsParticipantsCount();
        if (smallGroupsParticipantsCount == null || smallGroupsParticipantsCount.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StoryPrivacyBottomSheet.this.lambda$new$0(smallGroupsParticipantsCount);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashSet mergeUsers(ArrayList arrayList, HashMap hashMap) {
        HashSet hashSet = new HashSet();
        if (arrayList != null) {
            hashSet.addAll(arrayList);
        }
        if (hashMap != null) {
            for (ArrayList arrayList2 : hashMap.values()) {
                hashSet.addAll(arrayList2);
            }
        }
        return hashSet;
    }

    private void pullSaved() {
        String string = MessagesController.getInstance(this.currentAccount).getMainSettings().getString("story_prv_contacts", null);
        if (string != null) {
            String[] split = string.split(",");
            this.selectedContacts.clear();
            for (String str : split) {
                try {
                    this.selectedContacts.add(Long.valueOf(Long.parseLong(str)));
                } catch (Exception unused) {
                }
            }
        }
        String string2 = MessagesController.getInstance(this.currentAccount).getMainSettings().getString("story_prv_grpcontacts", null);
        if (string2 != null) {
            String[] split2 = string2.split(";");
            this.selectedContactsByGroup.clear();
            for (String str2 : split2) {
                String[] split3 = str2.split(",");
                if (split3.length > 0) {
                    try {
                        long parseLong = Long.parseLong(split3[0]);
                        ArrayList arrayList = new ArrayList();
                        for (int i = 1; i < split3.length; i++) {
                            arrayList.add(Long.valueOf(Long.parseLong(split3[i])));
                        }
                        this.selectedContactsByGroup.put(Long.valueOf(parseLong), arrayList);
                    } catch (Exception unused2) {
                    }
                }
            }
        }
        String string3 = MessagesController.getInstance(this.currentAccount).getMainSettings().getString("story_prv_everyoneexcept", null);
        if (string3 != null) {
            String[] split4 = string3.split(",");
            this.excludedEveryone.clear();
            for (String str3 : split4) {
                try {
                    this.excludedEveryone.add(Long.valueOf(Long.parseLong(str3)));
                } catch (Exception unused3) {
                }
            }
        }
        String string4 = MessagesController.getInstance(this.currentAccount).getMainSettings().getString("story_prv_grpeveryoneexcept", null);
        if (string4 != null) {
            String[] split5 = string4.split(";");
            this.excludedEveryoneByGroup.clear();
            for (String str4 : split5) {
                String[] split6 = str4.split(",");
                if (split6.length > 0) {
                    try {
                        long parseLong2 = Long.parseLong(split6[0]);
                        ArrayList arrayList2 = new ArrayList();
                        for (int i2 = 1; i2 < split6.length; i2++) {
                            arrayList2.add(Long.valueOf(Long.parseLong(split6[i2])));
                        }
                        this.excludedEveryoneByGroup.put(Long.valueOf(parseLong2), arrayList2);
                    } catch (Exception unused4) {
                    }
                }
            }
        }
        String string5 = MessagesController.getInstance(this.currentAccount).getMainSettings().getString("story_prv_excluded", null);
        if (string5 != null) {
            String[] split7 = string5.split(",");
            this.excludedContacts.clear();
            for (String str5 : split7) {
                try {
                    this.excludedContacts.add(Long.valueOf(Long.parseLong(str5)));
                } catch (Exception unused5) {
                }
            }
        }
        this.selectedContactsCount = mergeUsers(this.selectedContacts, this.selectedContactsByGroup).size();
        this.excludedEveryoneCount = mergeUsers(this.excludedEveryone, this.excludedEveryoneByGroup).size();
        this.allowScreenshots = !MessagesController.getInstance(this.currentAccount).getMainSettings().getBoolean("story_noforwards", false);
        this.keepOnMyPage = MessagesController.getInstance(this.currentAccount).getMainSettings().getBoolean("story_keep", true);
    }

    private void save() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : this.selectedContactsByGroup.entrySet()) {
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(entry.getKey());
            sb.append(",");
            sb.append(TextUtils.join(",", (Iterable) entry.getValue()));
        }
        StringBuilder sb2 = new StringBuilder();
        for (Map.Entry entry2 : this.excludedEveryoneByGroup.entrySet()) {
            if (sb2.length() > 0) {
                sb2.append(";");
            }
            sb2.append(entry2.getKey());
            sb2.append(",");
            sb2.append(TextUtils.join(",", (Iterable) entry2.getValue()));
        }
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putString("story_prv_everyoneexcept", TextUtils.join(",", this.excludedEveryone)).putString("story_prv_grpeveryoneexcept", sb2.toString()).putString("story_prv_contacts", TextUtils.join(",", this.selectedContacts)).putString("story_prv_grpcontacts", sb.toString()).putString("story_prv_excluded", TextUtils.join(",", this.excludedContacts)).putBoolean("story_noforwards", !this.allowScreenshots).putBoolean("story_keep", this.keepOnMyPage).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StoryPrivacyBottomSheet whenSelectedShare(Utilities.Callback callback) {
        this.onDone2 = callback;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        View currentView = this.viewPager.getCurrentView();
        if (currentView instanceof Page) {
            return ((Page) currentView).wasAtTop;
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canSwipeToBack(MotionEvent motionEvent) {
        return false;
    }

    public void closeKeyboard() {
        View[] viewPages;
        for (View view : this.viewPager.getViewPages()) {
            if (view instanceof Page) {
                Page page = (Page) view;
                if (page.searchField != null) {
                    AndroidUtilities.hideKeyboard(page.searchField.editText);
                }
            }
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ViewPagerFixed viewPagerFixed = this.viewPager;
        if (viewPagerFixed == null) {
            return;
        }
        int i3 = 0;
        if (i == NotificationCenter.contactsDidLoad) {
            View[] viewPages = viewPagerFixed.getViewPages();
            View view = viewPages[0];
            if (view instanceof Page) {
                ((Page) view).updateItems(true);
            }
            View view2 = viewPages[1];
            if (view2 instanceof Page) {
                ((Page) view2).updateItems(true);
            }
        } else if (i != NotificationCenter.storiesBlocklistUpdate) {
            if (i == NotificationCenter.storiesSendAsUpdate) {
                View[] viewPages2 = viewPagerFixed.getViewPages();
                while (i3 < viewPages2.length) {
                    View view3 = viewPages2[i3];
                    if (view3 instanceof Page) {
                        Page page = (Page) view3;
                        if (page.pageType == 0) {
                            page.updateItems(true);
                        }
                    }
                    i3++;
                }
            }
        } else {
            View[] viewPages3 = viewPagerFixed.getViewPages();
            while (i3 < viewPages3.length) {
                View view4 = viewPages3[i3];
                if (view4 instanceof Page) {
                    Page page2 = (Page) view4;
                    int i4 = page2.pageType;
                    if (i4 == 6) {
                        page2.applyBlocklist(true);
                    } else if (i4 == 0) {
                        page2.updateItems(true);
                    }
                }
                i3++;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        StoryPrivacy storyPrivacy;
        HashMap hashMap;
        HashMap hashMap2;
        if (this.onDismiss != null) {
            int i = this.selectedType;
            if (i == 3) {
                storyPrivacy = new StoryPrivacy(this.selectedType, this.currentAccount, new ArrayList(mergeUsers(this.selectedContacts, this.selectedContactsByGroup)));
                storyPrivacy.selectedUserIds.clear();
                storyPrivacy.selectedUserIds.addAll(this.selectedContacts);
                storyPrivacy.selectedUserIdsByGroup.clear();
                hashMap = storyPrivacy.selectedUserIdsByGroup;
                hashMap2 = this.selectedContactsByGroup;
            } else if (i == 4) {
                storyPrivacy = new StoryPrivacy(this.selectedType, this.currentAccount, new ArrayList(mergeUsers(this.excludedEveryone, this.excludedEveryoneByGroup)));
                storyPrivacy.selectedUserIds.clear();
                storyPrivacy.selectedUserIds.addAll(this.excludedEveryone);
                storyPrivacy.selectedUserIdsByGroup.clear();
                hashMap = storyPrivacy.selectedUserIdsByGroup;
                hashMap2 = this.excludedEveryoneByGroup;
            } else {
                storyPrivacy = i == 2 ? new StoryPrivacy(i, this.currentAccount, this.excludedContacts) : new StoryPrivacy(i, this.currentAccount, (ArrayList) null);
                this.onDismiss.run(storyPrivacy);
                this.onDismiss = null;
            }
            hashMap.putAll(hashMap2);
            this.onDismiss.run(storyPrivacy);
            this.onDismiss = null;
        }
        Bulletin.removeDelegate(this.container);
        save();
        super.dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesBlocklistUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesSendAsUpdate);
        super.dismissInternal();
    }

    public StoryPrivacyBottomSheet enableSharing(boolean z) {
        View[] viewPages;
        this.sendAsMessageEnabled = z;
        ViewPagerFixed viewPagerFixed = this.viewPager;
        if (viewPagerFixed != null) {
            for (View view : viewPagerFixed.getViewPages()) {
                if (view instanceof Page) {
                    ((Page) view).updateButton(false);
                }
            }
        }
        return this;
    }

    public StoryPrivacyBottomSheet isEdit(boolean z) {
        View[] viewPages;
        this.isEdit = z;
        ViewPagerFixed viewPagerFixed = this.viewPager;
        if (viewPagerFixed != null) {
            for (View view : viewPagerFixed.getViewPages()) {
                if (view instanceof Page) {
                    Page page = (Page) view;
                    page.updateItems(false);
                    page.updateButton(false);
                }
            }
        }
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (this.viewPager.getCurrentPosition() <= 0) {
            super.onBackPressed();
            return;
        }
        closeKeyboard();
        ViewPagerFixed viewPagerFixed = this.viewPager;
        viewPagerFixed.scrollToPosition(viewPagerFixed.getCurrentPosition() - 1);
    }

    public StoryPrivacyBottomSheet setCanChangePeer(boolean z) {
        this.canChangePeer = z;
        return this;
    }

    public StoryPrivacyBottomSheet setCover(Bitmap bitmap) {
        View[] viewPages;
        this.coverDrawable = bitmap == null ? null : new BitmapDrawable(bitmap);
        ViewPagerFixed viewPagerFixed = this.viewPager;
        if (viewPagerFixed != null) {
            for (View view : viewPagerFixed.getViewPages()) {
                if (view instanceof Page) {
                    Page page = (Page) view;
                    page.updateItems(false);
                    page.updateButton(false);
                }
            }
        }
        return this;
    }

    public StoryPrivacyBottomSheet setCover(Bitmap bitmap, Runnable runnable) {
        View[] viewPages;
        this.coverDrawable = bitmap == null ? null : new BitmapDrawable(bitmap);
        this.whenCoverClicked = runnable;
        ViewPagerFixed viewPagerFixed = this.viewPager;
        if (viewPagerFixed != null) {
            for (View view : viewPagerFixed.getViewPages()) {
                if (view instanceof Page) {
                    Page page = (Page) view;
                    page.updateItems(false);
                    page.updateButton(false);
                }
            }
        }
        return this;
    }

    public StoryPrivacyBottomSheet setPeer(TLRPC.InputPeer inputPeer) {
        this.selectedPeer = inputPeer;
        View[] viewPages = this.viewPager.getViewPages();
        View view = viewPages[0];
        if (view instanceof Page) {
            Page page = (Page) view;
            page.bind(page.pageType);
        }
        View view2 = viewPages[1];
        if (view2 instanceof Page) {
            Page page2 = (Page) view2;
            page2.bind(page2.pageType);
        }
        return this;
    }

    public StoryPrivacyBottomSheet setValue(StoryPrivacy storyPrivacy) {
        if (storyPrivacy == null) {
            return this;
        }
        int i = storyPrivacy.type;
        this.selectedType = i;
        if (i == 2) {
            this.excludedContacts.clear();
            this.excludedContacts.addAll(storyPrivacy.selectedUserIds);
        } else if (i == 3) {
            this.selectedContacts.clear();
            this.selectedContacts.addAll(storyPrivacy.selectedUserIds);
            this.selectedContactsByGroup.clear();
            this.selectedContactsByGroup.putAll(storyPrivacy.selectedUserIdsByGroup);
            this.selectedContactsCount = mergeUsers(this.selectedContacts, this.selectedContactsByGroup).size();
        } else if (i == 4) {
            this.excludedEveryone.clear();
            this.excludedEveryone.addAll(storyPrivacy.selectedUserIds);
            this.excludedEveryoneByGroup.clear();
            this.excludedEveryoneByGroup.putAll(storyPrivacy.selectedUserIdsByGroup);
            this.excludedEveryoneCount = mergeUsers(this.excludedEveryone, this.excludedEveryoneByGroup).size();
        }
        if (storyPrivacy.isShare()) {
            this.startedFromSendAsMessage = true;
            this.activePage = 5;
            this.messageUsers.clear();
            this.messageUsers.addAll(storyPrivacy.sendToUsers);
            this.viewPager.setPosition(1);
        }
        View[] viewPages = this.viewPager.getViewPages();
        View view = viewPages[0];
        if (view instanceof Page) {
            Page page = (Page) view;
            page.bind(page.pageType);
        }
        View view2 = viewPages[1];
        if (view2 instanceof Page) {
            Page page2 = (Page) view2;
            page2.bind(page2.pageType);
        }
        return this;
    }

    public StoryPrivacyBottomSheet setWarnUsers(ArrayList arrayList) {
        this.warnUsers = arrayList;
        return this;
    }

    public StoryPrivacyBottomSheet whenDismiss(Utilities.Callback callback) {
        this.onDismiss = callback;
        return this;
    }

    public StoryPrivacyBottomSheet whenSelectedPeer(Utilities.Callback callback) {
        this.onSelectedPeer = callback;
        return this;
    }

    public StoryPrivacyBottomSheet whenSelectedRules(DoneCallback doneCallback, boolean z) {
        this.onDone = doneCallback;
        this.applyWhenDismiss = z;
        return this;
    }
}
