package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.ChannelColorActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatEditActivity;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.Loadable;
import org.telegram.ui.Components.LoginOrView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.boosts.BoostCounterView;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostPagerBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.ReassignBoostBottomSheet;
import org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupColorActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.StatisticActivity;
import org.telegram.ui.Stories.ChannelBoostUtilities;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.StoryRecorder;

/* loaded from: classes3.dex */
public class LimitReachedBottomSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
    TextView actionBtn;
    ArrayList boostFeatures;
    int boostFeaturesStartRow;
    ButtonWithCounterView boostMiniBtn;
    private ButtonWithCounterView boostToUnlockGroupBtn;
    private TL_stories.TL_premium_boostsStatus boostsStatus;
    int bottomRow;
    private ChannelBoostsController.CanApplyBoost canApplyBoost;
    private boolean canSendLink;
    int chatEndRow;
    private ChatMessageCell chatMessageCell;
    int chatStartRow;
    ArrayList chats;
    int chatsTitleRow;
    private int currentValue;
    private long dialogId;
    View divider;
    int dividerRow;
    int emptyViewDividerRow;
    RecyclerItemsEnterAnimator enterAnimator;
    FireworksOverlay fireworksOverlay;
    private TLRPC.Chat fromChat;
    int headerRow;
    private HeaderView headerView;
    private ArrayList inactiveChats;
    private ArrayList inactiveChatsSignatures;
    private boolean isCurrentChat;
    private boolean isVeryLargeFile;
    LimitParams limitParams;
    LimitPreviewView limitPreviewView;
    private int linkRow;
    private boolean loading;
    boolean loadingAdminedChannels;
    int loadingRow;
    private boolean lockInvalidation;
    public Runnable onShowPremiumScreenRunnable;
    public Runnable onSuccessRunnable;
    BaseFragment parentFragment;
    public boolean parentIsChannel;
    PremiumButtonView premiumButtonView;
    private ArrayList premiumInviteBlockedUsers;
    private ArrayList premiumMessagingBlockedUsers;
    private int requiredLvl;
    private ArrayList restrictedUsers;
    int rowCount;
    HashSet selectedChats;
    private int shiftDp;
    Runnable statisticClickRunnable;
    final int type;

    class 5 extends RecyclerListView.SelectionAdapter {
        5() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            BoostPagerBottomSheet.show(LimitReachedBottomSheet.this.getBaseFragment(), LimitReachedBottomSheet.this.dialogId, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$2(View view) {
            AndroidUtilities.addToClipboard(LimitReachedBottomSheet.this.getBoostLink());
            LimitReachedBottomSheet.this.lambda$new$0();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$3() {
            LimitReachedBottomSheet.this.getBaseFragment().presentFragment(StatisticActivity.create(LimitReachedBottomSheet.this.getChat()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$4(View view) {
            AndroidUtilities.addToClipboard(LimitReachedBottomSheet.this.getBoostLink());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$5(View view) {
            LimitReachedBottomSheet.this.statisticClickRunnable.run();
            LimitReachedBottomSheet.this.lambda$new$0();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return LimitReachedBottomSheet.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            int i2;
            LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet.headerRow == i) {
                return 0;
            }
            if (limitReachedBottomSheet.dividerRow == i) {
                return 2;
            }
            if (limitReachedBottomSheet.chatsTitleRow == i) {
                return 3;
            }
            if (limitReachedBottomSheet.loadingRow == i) {
                return 5;
            }
            if (limitReachedBottomSheet.emptyViewDividerRow == i) {
                return 6;
            }
            if (limitReachedBottomSheet.linkRow == i) {
                return 7;
            }
            LimitReachedBottomSheet limitReachedBottomSheet2 = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet2.bottomRow == i) {
                return 8;
            }
            ArrayList arrayList = limitReachedBottomSheet2.boostFeatures;
            if (arrayList != null && i >= (i2 = limitReachedBottomSheet2.boostFeaturesStartRow) && i <= i2 + arrayList.size()) {
                return 9;
            }
            int i3 = LimitReachedBottomSheet.this.type;
            return (i3 == 5 || i3 == 11) ? 4 : 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
            if (limitReachedBottomSheet.type != 11 || limitReachedBottomSheet.canSendLink) {
                return viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 4;
            }
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x0050, code lost:
        
            if (r9 != (r7.this$0.chatEndRow - 1.0f)) goto L17;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x0053, code lost:
        
            r2 = false;
            r0 = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x0054, code lost:
        
            r8.setObject(r0, r4, r3, r2);
            r8.setChecked(r7.this$0.selectedChats.contains(r0), false);
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00bb, code lost:
        
            if (r9 != (r7.this$0.chatEndRow - 1.0f)) goto L17;
         */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            String formatName;
            TLRPC.User user;
            TLRPC.User user2;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType == 1) {
                LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                TLRPC.Chat chat = (TLRPC.Chat) limitReachedBottomSheet.chats.get(i - limitReachedBottomSheet.chatStartRow);
                AdminedChannelCell adminedChannelCell = (AdminedChannelCell) viewHolder.itemView;
                TLRPC.Chat currentChannel = adminedChannelCell.getCurrentChannel();
                adminedChannelCell.setChannel(chat, false);
                adminedChannelCell.setChecked(LimitReachedBottomSheet.this.selectedChats.contains(chat), currentChannel == chat);
                return;
            }
            if (itemViewType == 9) {
                LimitReachedBottomSheet limitReachedBottomSheet2 = LimitReachedBottomSheet.this;
                int i2 = i - limitReachedBottomSheet2.boostFeaturesStartRow;
                ArrayList arrayList = limitReachedBottomSheet2.boostFeatures;
                if (arrayList == null || i2 < 0 || i2 >= arrayList.size()) {
                    return;
                }
                ((BoostFeatureCell) viewHolder.itemView).set((BoostFeature) LimitReachedBottomSheet.this.boostFeatures.get(i2));
                return;
            }
            if (itemViewType == 3) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                LimitReachedBottomSheet limitReachedBottomSheet3 = LimitReachedBottomSheet.this;
                int i3 = limitReachedBottomSheet3.type;
                headerCell.setText(LocaleController.getString(i3 == 11 ? limitReachedBottomSheet3.canSendLink ? R.string.ChannelInviteViaLink : LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? R.string.ChannelInviteViaLinkRestricted2 : R.string.ChannelInviteViaLinkRestricted3 : i3 == 2 ? R.string.YourPublicCommunities : R.string.LastActiveCommunities));
                return;
            }
            if (itemViewType != 4) {
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            LimitReachedBottomSheet limitReachedBottomSheet4 = LimitReachedBottomSheet.this;
            int i4 = limitReachedBottomSheet4.type;
            if (i4 == 5) {
                TLRPC.Chat chat2 = (TLRPC.Chat) limitReachedBottomSheet4.inactiveChats.get(i - LimitReachedBottomSheet.this.chatStartRow);
                string = (String) LimitReachedBottomSheet.this.inactiveChatsSignatures.get(i - LimitReachedBottomSheet.this.chatStartRow);
                formatName = chat2.title;
                user2 = chat2;
                user = chat2;
            } else {
                if (i4 != 11) {
                    return;
                }
                TLRPC.User user3 = (TLRPC.User) limitReachedBottomSheet4.restrictedUsers.get(i - LimitReachedBottomSheet.this.chatStartRow);
                boolean z2 = LimitReachedBottomSheet.this.premiumMessagingBlockedUsers != null && LimitReachedBottomSheet.this.premiumMessagingBlockedUsers.contains(Long.valueOf(user3.id));
                groupCreateUserCell.overridePremiumBlocked(z2, false);
                string = z2 ? LocaleController.getString(R.string.InvitePremiumBlockedUser) : LocaleController.formatUserStatus(((BottomSheet) LimitReachedBottomSheet.this).currentAccount, user3, null, null);
                formatName = ContactsController.formatName(user3.first_name, user3.last_name);
                user2 = user3;
                user = user3;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            int i2;
            Theme.ResourcesProvider resourcesProvider;
            View view;
            Context context = viewGroup.getContext();
            switch (i) {
                case 1:
                    view = new AdminedChannelCell(context, new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view2) {
                            AdminedChannelCell adminedChannelCell = (AdminedChannelCell) view2.getParent();
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(adminedChannelCell.getCurrentChannel());
                            LimitReachedBottomSheet.this.revokeLinks(arrayList);
                        }
                    }, true, 9);
                    break;
                case 2:
                    view = new ShadowSectionCell(context, 12, Theme.getColor(Theme.key_windowBackgroundGray, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    break;
                case 3:
                    View headerCell = new HeaderCell(context);
                    headerCell.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
                    view = headerCell;
                    break;
                case 4:
                    View groupCreateUserCell = new GroupCreateUserCell(context, 1, 0, false);
                    groupCreateUserCell.setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft, 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft, 0);
                    view = groupCreateUserCell;
                    break;
                case 5:
                    FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context, null);
                    flickerLoadingView.setViewType(LimitReachedBottomSheet.this.type == 2 ? 22 : 21);
                    flickerLoadingView.setIsSingleCell(true);
                    flickerLoadingView.setIgnoreHeightCheck(true);
                    flickerLoadingView.setItemsCount(10);
                    view = flickerLoadingView;
                    break;
                case 6:
                    view = new View(LimitReachedBottomSheet.this.getContext()) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.3
                        @Override // android.view.View
                        protected void onMeasure(int i3, int i4) {
                            super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.0f), 1073741824));
                        }
                    };
                    break;
                case 7:
                    FrameLayout frameLayout = new FrameLayout(LimitReachedBottomSheet.this.getContext());
                    frameLayout.setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
                    TextView textView = new TextView(context);
                    LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                    if (limitReachedBottomSheet.statisticClickRunnable == null && ChatObject.hasAdminRights(limitReachedBottomSheet.getChat())) {
                        LimitReachedBottomSheet.this.statisticClickRunnable = new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$3();
                            }
                        };
                    }
                    textView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(LimitReachedBottomSheet.this.statisticClickRunnable != null ? 50.0f : 18.0f), AndroidUtilities.dp(13.0f));
                    textView.setTextSize(1, 16.0f);
                    textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    textView.setSingleLine(true);
                    frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 11.0f, 0.0f, 11.0f, 0.0f));
                    int dp = AndroidUtilities.dp(8.0f);
                    int color = Theme.getColor(Theme.key_graySection, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                    int i3 = Theme.key_listSelector;
                    textView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp, color, ColorUtils.setAlphaComponent(Theme.getColor(i3, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider), 76)));
                    textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                    textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$4(view2);
                        }
                    });
                    if (LimitReachedBottomSheet.this.statisticClickRunnable != null) {
                        ImageView imageView = new ImageView(LimitReachedBottomSheet.this.getContext());
                        imageView.setImageResource(R.drawable.msg_stats);
                        imageView.setColorFilter(Theme.getColor(Theme.key_dialogTextBlack, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                        imageView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor(i3, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider), 76)));
                        frameLayout.addView(imageView, LayoutHelper.createFrame(40, 40.0f, 21, 15.0f, 0.0f, 15.0f, 0.0f));
                        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$5(view2);
                            }
                        });
                    }
                    textView.setText(LimitReachedBottomSheet.this.getBoostLink());
                    textView.setGravity(17);
                    view = frameLayout;
                    break;
                case 8:
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
                    linearLayout.setOrientation(1);
                    LoginOrView loginOrView = new LoginOrView(context);
                    final LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
                    SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(LimitReachedBottomSheet.this.isGroup() ? R.string.BoostingStoriesByGiftingGroup2 : R.string.BoostingStoriesByGiftingChannel2));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.BoostingStoriesByGiftingLink));
                    spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.5.1
                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view2) {
                            BoostPagerBottomSheet.show(LimitReachedBottomSheet.this.getBaseFragment(), LimitReachedBottomSheet.this.dialogId, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                        }

                        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                        public void updateDrawState(TextPaint textPaint) {
                            super.updateDrawState(textPaint);
                            textPaint.setUnderlineText(false);
                            textPaint.setColor(Theme.getColor(Theme.key_chat_messageLinkIn, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                        }
                    }, 0, spannableStringBuilder.length(), 33);
                    SpannableString spannableString = new SpannableString(">");
                    Drawable mutate = LimitReachedBottomSheet.this.getContext().getResources().getDrawable(R.drawable.msg_arrowright).mutate();
                    int i4 = Theme.key_chat_messageLinkIn;
                    mutate.setColorFilter(new PorterDuffColorFilter(i4, PorterDuff.Mode.SRC_IN));
                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(mutate);
                    coloredImageSpan.setColorKey(i4);
                    coloredImageSpan.setSize(AndroidUtilities.dp(18.0f));
                    coloredImageSpan.setWidth(AndroidUtilities.dp(11.0f));
                    coloredImageSpan.setTranslateX(-AndroidUtilities.dp(5.0f));
                    spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
                    linksTextView.setText(TextUtils.concat(replaceTags, " ", AndroidUtilities.replaceCharSequence(">", spannableStringBuilder, spannableString)));
                    linksTextView.setTextSize(1, 14.0f);
                    linksTextView.setLineSpacing(AndroidUtilities.dp(3.0f), 1.0f);
                    if (((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider instanceof DarkThemeResourceProvider) {
                        i2 = Theme.key_windowBackgroundWhiteGrayText;
                        resourcesProvider = ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider;
                    } else {
                        i2 = Theme.key_windowBackgroundWhiteBlackText;
                        resourcesProvider = ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider;
                    }
                    linksTextView.setTextColor(Theme.getColor(i2, resourcesProvider));
                    linksTextView.setGravity(1);
                    linksTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$0(view2);
                        }
                    });
                    loginOrView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            linksTextView.performClick();
                        }
                    });
                    if (LimitReachedBottomSheet.this.isMiniBoostBtnForAdminAvailable()) {
                        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                        buttonWithCounterView.setText(LocaleController.getString(R.string.Copy), false);
                        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$5$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                LimitReachedBottomSheet.5.this.lambda$onCreateViewHolder$2(view2);
                            }
                        });
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.addView(LimitReachedBottomSheet.this.boostMiniBtn, LayoutHelper.createLinear(-1, 44, 1.0f, 0, 0, 0, 4, 0));
                        linearLayout2.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 44, 1.0f, 0, 4, 0, 0, 0));
                        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, 44, 12.0f, 12.0f, 12.0f, 8.0f));
                    } else {
                        linearLayout.addView(LimitReachedBottomSheet.this.actionBtn, LayoutHelper.createLinear(-1, 48, 12.0f, 12.0f, 12.0f, 8.0f));
                    }
                    linearLayout.addView(loginOrView, LayoutHelper.createLinear(-1, 48, 0.0f, -5.0f, 0.0f, 0.0f));
                    linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 12.0f, -6.0f, 12.0f, 17.0f));
                    view = linearLayout;
                    break;
                case 9:
                    LimitReachedBottomSheet limitReachedBottomSheet2 = LimitReachedBottomSheet.this;
                    view = limitReachedBottomSheet2.new BoostFeatureCell(context, ((BottomSheet) limitReachedBottomSheet2).resourcesProvider);
                    break;
                default:
                    view = LimitReachedBottomSheet.this.headerView = LimitReachedBottomSheet.this.new HeaderView(context);
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }
    }

    private static class BoostFeature {
        public final int countPlural;
        public final String countValue;
        public final int iconResId;
        public boolean incremental;
        public final int textKey;
        public final String textKeyPlural;

        public static class BoostFeatureLevel extends BoostFeature {
            public final boolean isFirst;
            public final int lvl;

            public BoostFeatureLevel(int i, boolean z) {
                super(-1, -1, null, null, -1);
                this.lvl = i;
                this.isFirst = z;
            }

            @Override // org.telegram.ui.Components.Premium.LimitReachedBottomSheet.BoostFeature
            public /* bridge */ /* synthetic */ BoostFeature asIncremental() {
                return super.asIncremental();
            }

            @Override // org.telegram.ui.Components.Premium.LimitReachedBottomSheet.BoostFeature
            public /* bridge */ /* synthetic */ boolean equals(BoostFeature boostFeature) {
                return super.equals(boostFeature);
            }
        }

        private BoostFeature(int i, int i2, String str, String str2, int i3) {
            this.iconResId = i;
            this.textKey = i2;
            this.countValue = str;
            this.textKeyPlural = str2;
            this.countPlural = i3;
        }

        public static boolean arraysEqual(ArrayList arrayList, ArrayList arrayList2) {
            if (arrayList == null && arrayList2 == null) {
                return true;
            }
            if ((arrayList != null && arrayList2 == null) || ((arrayList == null && arrayList2 != null) || arrayList.size() != arrayList2.size())) {
                return false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (!((BoostFeature) arrayList.get(i)).equals((BoostFeature) arrayList2.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public static BoostFeature of(int i, int i2) {
            return new BoostFeature(i, i2, null, null, -1);
        }

        public static BoostFeature of(int i, int i2, String str) {
            return new BoostFeature(i, i2, str, null, -1);
        }

        public static BoostFeature of(int i, String str, int i2) {
            return new BoostFeature(i, -1, null, str, i2);
        }

        public BoostFeature asIncremental() {
            this.incremental = true;
            return this;
        }

        public boolean equals(BoostFeature boostFeature) {
            if (boostFeature == null) {
                return false;
            }
            if (!this.incremental || this.countPlural <= 2) {
                return this.iconResId == boostFeature.iconResId && this.textKey == boostFeature.textKey && TextUtils.equals(this.countValue, boostFeature.countValue) && TextUtils.equals(this.textKeyPlural, boostFeature.textKeyPlural) && this.countPlural == boostFeature.countPlural;
            }
            return true;
        }
    }

    private class BoostFeatureCell extends FrameLayout {
        public BoostFeature feature;
        private final ImageView imageView;
        public BoostFeature.BoostFeatureLevel level;
        private final FrameLayout levelLayout;
        private final SimpleTextView levelTextView;
        private final Theme.ResourcesProvider resourcesProvider;
        private final SimpleTextView textView;

        public BoostFeatureCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft, 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft, 0);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_premiumGradient1, resourcesProvider), PorterDuff.Mode.SRC_IN));
            addView(imageView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 16, 24.0f, 0.0f, 24.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setWidthWrapContent(true);
            simpleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
            simpleTextView.setTextSize(14);
            boolean z = LocaleController.isRTL;
            addView(simpleTextView, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 16, z ? 30.0f : 60.0f, 0.0f, z ? 60.0f : 30.0f, 0.0f));
            SimpleTextView simpleTextView2 = new SimpleTextView(context);
            this.levelTextView = simpleTextView2;
            simpleTextView2.setTextColor(-1);
            simpleTextView2.setWidthWrapContent(true);
            simpleTextView2.setTypeface(AndroidUtilities.bold());
            simpleTextView2.setTextSize(14);
            FrameLayout frameLayout = new FrameLayout(context, LimitReachedBottomSheet.this, resourcesProvider) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.BoostFeatureCell.1
                private final Paint dividerPaint;
                private final PremiumGradient.PremiumGradientTools gradientTools;
                final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
                final /* synthetic */ LimitReachedBottomSheet val$this$0;

                {
                    this.val$resourcesProvider = resourcesProvider;
                    this.gradientTools = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradient1, Theme.key_premiumGradient2, -1, -1, -1, resourcesProvider);
                    Paint paint = new Paint(1);
                    this.dividerPaint = paint;
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(1.0f);
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    this.dividerPaint.setColor(Theme.getColor(Theme.key_sheet_scrollUp, this.val$resourcesProvider));
                    canvas.drawLine(AndroidUtilities.dp(18.0f), getHeight() / 2.0f, BoostFeatureCell.this.levelTextView.getLeft() - AndroidUtilities.dp(20.0f), getHeight() / 2.0f, this.dividerPaint);
                    canvas.drawLine(BoostFeatureCell.this.levelTextView.getRight() + AndroidUtilities.dp(20.0f), getHeight() / 2.0f, getWidth() - AndroidUtilities.dp(18.0f), getHeight() / 2.0f, this.dividerPaint);
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(BoostFeatureCell.this.levelTextView.getLeft() - AndroidUtilities.dp(15.0f), ((BoostFeatureCell.this.levelTextView.getTop() + BoostFeatureCell.this.levelTextView.getBottom()) - AndroidUtilities.dp(30.0f)) / 2.0f, BoostFeatureCell.this.levelTextView.getRight() + AndroidUtilities.dp(15.0f), ((BoostFeatureCell.this.levelTextView.getTop() + BoostFeatureCell.this.levelTextView.getBottom()) + AndroidUtilities.dp(30.0f)) / 2.0f);
                    canvas.save();
                    canvas.translate(rectF.left, rectF.top);
                    rectF.set(0.0f, 0.0f, rectF.width(), rectF.height());
                    this.gradientTools.gradientMatrix(rectF);
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), this.gradientTools.paint);
                    canvas.restore();
                    super.dispatchDraw(canvas);
                }
            };
            this.levelLayout = frameLayout;
            frameLayout.setWillNotDraw(false);
            frameLayout.addView(simpleTextView2, LayoutHelper.createFrame(-2, -2, 17));
            addView(frameLayout, LayoutHelper.createFrame(-1, -1.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.level != null ? 49.0f : 36.0f), 1073741824));
        }

        public void set(BoostFeature boostFeature) {
            SpannableStringBuilder spannableStringBuilder;
            if (boostFeature instanceof BoostFeature.BoostFeatureLevel) {
                this.level = (BoostFeature.BoostFeatureLevel) boostFeature;
                this.feature = null;
                this.imageView.setVisibility(8);
                this.textView.setVisibility(8);
                this.levelLayout.setVisibility(0);
                SimpleTextView simpleTextView = this.levelTextView;
                BoostFeature.BoostFeatureLevel boostFeatureLevel = this.level;
                simpleTextView.setText(LocaleController.formatPluralString(boostFeatureLevel.isFirst ? "BoostLevelUnlocks" : "BoostLevel", boostFeatureLevel.lvl, new Object[0]));
                return;
            }
            if (boostFeature != null) {
                this.level = null;
                this.feature = boostFeature;
                this.imageView.setVisibility(0);
                this.imageView.setImageResource(this.feature.iconResId);
                this.textView.setVisibility(0);
                BoostFeature boostFeature2 = this.feature;
                if (boostFeature2.textKeyPlural != null) {
                    String string = LocaleController.getString(this.feature.textKeyPlural + "_" + LocaleController.getStringParamForNumber(this.feature.countPlural));
                    if (string == null || string.startsWith("LOC_ERR")) {
                        string = LocaleController.getString(this.feature.textKeyPlural + "_other");
                    }
                    if (string == null) {
                        string = "";
                    }
                    spannableStringBuilder = new SpannableStringBuilder(string);
                    int indexOf = string.indexOf("%d");
                    if (indexOf >= 0) {
                        spannableStringBuilder = new SpannableStringBuilder(string);
                        SpannableString spannableString = new SpannableString(this.feature.countPlural + "");
                        spannableString.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString.length(), 33);
                        spannableStringBuilder.replace(indexOf, indexOf + 2, (CharSequence) spannableString);
                    }
                } else {
                    String string2 = LocaleController.getString(boostFeature2.textKey);
                    String str = string2 != null ? string2 : "";
                    if (this.feature.countValue == null) {
                        this.textView.setText(str);
                        this.levelLayout.setVisibility(8);
                    }
                    spannableStringBuilder = new SpannableStringBuilder(str);
                    int indexOf2 = str.indexOf("%s");
                    if (indexOf2 >= 0) {
                        spannableStringBuilder = new SpannableStringBuilder(str);
                        SpannableString spannableString2 = new SpannableString(this.feature.countValue);
                        spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString2.length(), 33);
                        spannableStringBuilder.replace(indexOf2, indexOf2 + 2, (CharSequence) spannableString2);
                    }
                }
                this.textView.setText(spannableStringBuilder);
                this.levelLayout.setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class HeaderView extends LinearLayout {
        BoostCounterView boostCounterView;
        TextView description;
        TextView title;
        LinearLayout titleLinearLayout;

        /* JADX WARN: Removed duplicated region for block: B:129:0x0897  */
        /* JADX WARN: Removed duplicated region for block: B:147:0x095e  */
        /* JADX WARN: Removed duplicated region for block: B:150:0x09b8  */
        /* JADX WARN: Removed duplicated region for block: B:155:0x0a9e  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0c89  */
        /* JADX WARN: Removed duplicated region for block: B:172:0x0cb9  */
        /* JADX WARN: Removed duplicated region for block: B:176:0x0ce6  */
        /* JADX WARN: Removed duplicated region for block: B:179:0x0cd1  */
        /* JADX WARN: Removed duplicated region for block: B:182:0x0cac  */
        /* JADX WARN: Removed duplicated region for block: B:186:0x0ae2  */
        /* JADX WARN: Removed duplicated region for block: B:189:0x0aed  */
        /* JADX WARN: Removed duplicated region for block: B:194:0x0b39  */
        /* JADX WARN: Removed duplicated region for block: B:202:0x09c1  */
        /* JADX WARN: Removed duplicated region for block: B:271:0x0953  */
        /* JADX WARN: Removed duplicated region for block: B:272:0x04e9  */
        /* JADX WARN: Removed duplicated region for block: B:279:0x0432  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x04e6  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x04f3  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0422  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public HeaderView(Context context) {
            super(context);
            String str;
            String formatString;
            int i;
            int i2;
            int i3;
            float f;
            int i4;
            boolean z;
            int i5;
            float f2;
            int i6;
            TextView textView;
            String boostsTitleString;
            int i7;
            int i8;
            int i9;
            View view;
            int i10;
            int i11;
            int i12;
            int i13;
            int i14;
            int i15;
            int i16;
            TextView textView2;
            Theme.ResourcesProvider resourcesProvider;
            int color;
            View view2;
            int i17;
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            int i23;
            TextView textView3;
            String num;
            String str2;
            float f3;
            float f4;
            setOrientation(1);
            setPadding(((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0, ((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft + AndroidUtilities.dp(6.0f), 0);
            LimitParams limitParams = LimitReachedBottomSheet.getLimitParams(LimitReachedBottomSheet.this.type, ((BottomSheet) LimitReachedBottomSheet.this).currentAccount);
            LimitReachedBottomSheet.this.limitParams = limitParams;
            int i24 = limitParams.icon;
            MessagesController messagesController = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount);
            boolean premiumFeaturesBlocked = messagesController.premiumFeaturesBlocked();
            boolean isGroup = LimitReachedBottomSheet.this.isGroup();
            int i25 = LimitReachedBottomSheet.this.type;
            if (i25 == 31) {
                str = LocaleController.getString(isGroup ? R.string.BoostingAdditionalFeaturesSubtitle : R.string.BoostingAdditionalFeaturesSubtitleChannel);
            } else if (i25 == 32) {
                str = LimitReachedBottomSheet.this.getBoostsDescriptionString(true);
            } else if (i25 == 19) {
                if (LimitReachedBottomSheet.this.chatMessageCell != null) {
                    int i26 = LimitReachedBottomSheet.this.chatMessageCell.getMessageObject().messageOwner.from_boosts_applied;
                    TLRPC.Chat chat = LimitReachedBottomSheet.this.getChat();
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString("GroupBoostedByUserWithTimes", i26, UserObject.getFirstName(LimitReachedBottomSheet.this.chatMessageCell.getCurrentUser())));
                    spannableStringBuilder.append((CharSequence) " ");
                    spannableStringBuilder.append((CharSequence) LocaleController.formatString(R.string.GroupBoostedByUserWithDescription, chat == null ? "" : chat.title));
                    str = spannableStringBuilder.toString();
                } else if (LimitReachedBottomSheet.this.getBaseFragment() instanceof GroupColorActivity) {
                    str = LocaleController.formatPluralString("BoostingGroupBoostWhatAreBoostsDescription", BoostRepository.giveawayBoostsPerPremium(), new Object[0]);
                } else {
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(LimitReachedBottomSheet.this.getBoostsDescriptionString(true));
                    if (ChatObject.hasAdminRights(LimitReachedBottomSheet.this.getChat()) && isGroup) {
                        spannableStringBuilder2.append((CharSequence) " ").append((CharSequence) LocaleController.getString(R.string.BoostingPremiumUserCanBoostGroupWithLink));
                    }
                    str = spannableStringBuilder2.toString();
                }
            } else if (i25 == 18) {
                str = LimitReachedBottomSheet.this.boostsStatus.level == 0 ? LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsDescription : R.string.ChannelNeedBoostsDescription, LocaleController.formatPluralString("MoreBoosts", LimitReachedBottomSheet.this.boostsStatus.next_level_boosts, Integer.valueOf(LimitReachedBottomSheet.this.boostsStatus.next_level_boosts))) : LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsDescriptionNextLevel : R.string.ChannelNeedBoostsDescriptionNextLevel, LocaleController.formatPluralString("MoreBoosts", LimitReachedBottomSheet.this.boostsStatus.next_level_boosts - LimitReachedBottomSheet.this.boostsStatus.boosts, Integer.valueOf(LimitReachedBottomSheet.this.boostsStatus.next_level_boosts - LimitReachedBottomSheet.this.boostsStatus.boosts)), LocaleController.formatPluralString("BoostStories", LimitReachedBottomSheet.this.boostsStatus.level + 1, new Object[0]));
            } else if (i25 == 20) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForColorDescription : R.string.ChannelNeedBoostsForColorDescription, Integer.valueOf(LimitReachedBottomSheet.this.channelColorLevelMin()));
            } else if (i25 == 24) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForProfileColorDescription : R.string.ChannelNeedBoostsForProfileColorDescription, Integer.valueOf(LimitReachedBottomSheet.this.channelColorLevelMin()));
            } else if (i25 == 29) {
                str = LocaleController.formatString(R.string.GroupNeedBoostsForCustomEmojiPackDescription, Integer.valueOf(messagesController.groupEmojiStickersLevelMin));
            } else if (i25 == 30) {
                str = LocaleController.formatString(R.string.ChannelNeedBoostsForSwitchOffAdsDescription, Integer.valueOf(messagesController.channelRestrictSponsoredLevelMin));
            } else if (i25 == 25) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForEmojiStatusDescription : R.string.ChannelNeedBoostsForEmojiStatusDescription, Integer.valueOf(isGroup ? messagesController.groupEmojiStatusLevelMin : messagesController.channelEmojiStatusLevelMin));
            } else if (i25 == 26) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForWearCollectiblesDescription : R.string.ChannelNeedBoostsForWearCollectiblesDescription, Integer.valueOf(isGroup ? messagesController.groupEmojiStatusLevelMin : messagesController.channelEmojiStatusLevelMin));
            } else if (i25 == 27) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForReplyIconDescription : R.string.ChannelNeedBoostsForReplyIconDescription, Integer.valueOf(messagesController.channelBgIconLevelMin));
            } else if (i25 == 28) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForProfileIconDescription : R.string.ChannelNeedBoostsForProfileIconDescription, Integer.valueOf(isGroup ? messagesController.groupProfileBgIconLevelMin : messagesController.channelProfileIconLevelMin));
            } else if (i25 == 22) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForWallpaperDescription : R.string.ChannelNeedBoostsForWallpaperDescription, Integer.valueOf(isGroup ? messagesController.groupWallpaperLevelMin : messagesController.channelWallpaperLevelMin));
            } else if (i25 == 23) {
                str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForCustomWallpaperDescription : R.string.ChannelNeedBoostsForCustomWallpaperDescription, Integer.valueOf(isGroup ? messagesController.groupCustomWallpaperLevelMin : messagesController.channelCustomWallpaperLevelMin));
            } else if (i25 == 21) {
                str = LocaleController.formatPluralString("ReactionReachLvlForReaction", LimitReachedBottomSheet.this.requiredLvl, Integer.valueOf(LimitReachedBottomSheet.this.requiredLvl));
            } else if (i25 == 11) {
                formatString = !LimitReachedBottomSheet.this.canSendLink ? ChatObject.isChannelAndNotMegaGroup(LimitReachedBottomSheet.this.fromChat) ? LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteChannelRestrictedUsers2One", R.string.InviteChannelRestrictedUsers2One, ContactsController.formatName((TLRPC.User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteChannelRestrictedUsers2", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size())) : LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteRestrictedUsers2One", R.string.InviteRestrictedUsers2One, ContactsController.formatName((TLRPC.User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteRestrictedUsers2", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size())) : ChatObject.isChannelAndNotMegaGroup(LimitReachedBottomSheet.this.fromChat) ? LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteChannelRestrictedUsersOne", R.string.InviteChannelRestrictedUsersOne, ContactsController.formatName((TLRPC.User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteChannelRestrictedUsers", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size())) : LimitReachedBottomSheet.this.restrictedUsers.size() == 1 ? LocaleController.formatString("InviteRestrictedUsersOne", R.string.InviteRestrictedUsersOne, ContactsController.formatName((TLRPC.User) LimitReachedBottomSheet.this.restrictedUsers.get(0))) : LocaleController.formatPluralString("InviteRestrictedUsers", LimitReachedBottomSheet.this.restrictedUsers.size(), Integer.valueOf(LimitReachedBottomSheet.this.restrictedUsers.size()));
                premiumFeaturesBlocked = true;
                LimitParams limitParams2 = LimitReachedBottomSheet.this.limitParams;
                int i27 = limitParams2.defaultLimit;
                i = limitParams2.premiumLimit;
                i2 = LimitReachedBottomSheet.this.currentValue;
                i3 = LimitReachedBottomSheet.this.type;
                if (i3 != 3) {
                    i2 = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).dialogFilters.size() - 1;
                } else if (i3 == 7) {
                    i2 = UserConfig.getActivatedAccountsCount();
                } else if (i3 == 0) {
                    ArrayList<TLRPC.Dialog> dialogs = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getDialogs(0);
                    int size = dialogs.size();
                    int i28 = 0;
                    for (int i29 = 0; i29 < size; i29++) {
                        TLRPC.Dialog dialog = dialogs.get(i29);
                        if (!(dialog instanceof TLRPC.TL_dialogFolder) && dialog.pinned) {
                            i28++;
                        }
                    }
                    i2 = i28;
                }
                if (!UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) {
                    i2 = i;
                    f = 1.0f;
                } else {
                    i2 = i2 < 0 ? i27 : i2;
                    if (LimitReachedBottomSheet.this.type != 7) {
                        f3 = i2;
                        f4 = i;
                    } else if (i2 > i27) {
                        f3 = i2 - i27;
                        f4 = i - i27;
                    } else {
                        f = 0.5f;
                    }
                    f = f3 / f4;
                }
                float f5 = i27 / i;
                i4 = LimitReachedBottomSheet.this.type;
                if (i4 == 18 && i4 != 20) {
                    if (i4 != 24 && i4 != 25 && i4 != 26 && i4 != 29 && i4 != 22) {
                        if (i4 != 23 && i4 != 19 && i4 != 30 && i4 != 21 && i4 != 27 && i4 != 28 && i4 != 32) {
                            z = false;
                            int i30 = z ? 0 : i2;
                            if (i4 != 11 && !MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).premiumFeaturesBlocked() && ((LimitReachedBottomSheet.this.premiumInviteBlockedUsers != null && !LimitReachedBottomSheet.this.premiumInviteBlockedUsers.isEmpty()) || (LimitReachedBottomSheet.this.premiumMessagingBlockedUsers != null && LimitReachedBottomSheet.this.premiumMessagingBlockedUsers.size() >= LimitReachedBottomSheet.this.restrictedUsers.size()))) {
                                ArrayList arrayList = LimitReachedBottomSheet.this.premiumInviteBlockedUsers.isEmpty() ? LimitReachedBottomSheet.this.premiumMessagingBlockedUsers : LimitReachedBottomSheet.this.premiumInviteBlockedUsers;
                                AvatarsImageView avatarsImageView = new AvatarsImageView(context, false);
                                avatarsImageView.avatarsDrawable.strokeWidth = AndroidUtilities.dp(3.33f);
                                avatarsImageView.setSize(AndroidUtilities.dp(72.0f));
                                avatarsImageView.setStepFactor(0.4f);
                                int min = Math.min(arrayList.size(), 3);
                                avatarsImageView.setCount(min);
                                for (int i31 = 0; i31 < min; i31++) {
                                    Long l = (Long) arrayList.get(i31);
                                    l.longValue();
                                    avatarsImageView.setObject(i31, ((BottomSheet) LimitReachedBottomSheet.this).currentAccount, MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser(l));
                                }
                                avatarsImageView.commitTransition(false);
                                addView(avatarsImageView, LayoutHelper.createLinear(((min - 1) * 30) + 72, 72, 1, 0, 16, 0, 13));
                                TextView textView4 = new TextView(context);
                                textView4.setGravity(17);
                                textView4.setTypeface(AndroidUtilities.bold());
                                textView4.setTextSize(1, 20.0f);
                                int i32 = Theme.key_dialogTextBlack;
                                textView4.setTextColor(Theme.getColor(i32));
                                textView4.setText(LocaleController.getString(R.string.InvitePremiumBlockedTitle));
                                addView(textView4, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 9));
                                TextView textView5 = new TextView(context);
                                textView5.setGravity(17);
                                textView5.setTextSize(1, 14.0f);
                                textView5.setTextColor(Theme.getColor(i32));
                                addView(textView5, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 19));
                                boolean z2 = LimitReachedBottomSheet.this.premiumMessagingBlockedUsers != null && LimitReachedBottomSheet.this.premiumMessagingBlockedUsers.size() >= LimitReachedBottomSheet.this.premiumInviteBlockedUsers.size();
                                if (LimitReachedBottomSheet.this.premiumInviteBlockedUsers != null) {
                                    LimitReachedBottomSheet.this.premiumInviteBlockedUsers.isEmpty();
                                }
                                if (arrayList.size() == 1) {
                                    str2 = LocaleController.formatString(z2 ? R.string.InviteMessagePremiumBlockedOne : R.string.InvitePremiumBlockedOne, UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(0))));
                                } else if (arrayList.size() == 2) {
                                    str2 = LocaleController.formatString(z2 ? R.string.InviteMessagePremiumBlockedTwo : R.string.InvitePremiumBlockedTwo, UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(0))), UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(1))));
                                } else if (arrayList.size() == 3) {
                                    str2 = LocaleController.formatString(z2 ? R.string.InviteMessagePremiumBlockedThree : R.string.InvitePremiumBlockedThree, UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(0))), UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(1))), UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(2))));
                                } else {
                                    String formatPluralString = LocaleController.formatPluralString(z2 ? "InviteMessagePremiumBlockedMany" : "InvitePremiumBlockedMany", arrayList.size() - 2, UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(0))), UserObject.getForcedFirstName(MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getUser((Long) arrayList.get(1))));
                                    avatarsImageView.setPlus(arrayList.size() - 2, LimitReachedBottomSheet.this.getThemedColor(Theme.key_dialogBackground));
                                    str2 = formatPluralString;
                                }
                                textView5.setText(AndroidUtilities.replaceTags(str2));
                                PremiumButtonView premiumButtonView = new PremiumButtonView(context, false, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                                ScaleStateListAnimator.apply(premiumButtonView, 0.02f, 1.2f);
                                premiumButtonView.setButton(LocaleController.getString(R.string.InvitePremiumBlockedSubscribe), new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$HeaderView$$ExternalSyntheticLambda0
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view3) {
                                        LimitReachedBottomSheet.HeaderView.this.lambda$new$0(view3);
                                    }
                                });
                                addView(premiumButtonView, LayoutHelper.createLinear(-1, 48, (((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft / AndroidUtilities.density) + 4.0f, 0.0f, (((BottomSheet) LimitReachedBottomSheet.this).backgroundPaddingLeft / AndroidUtilities.density) + 4.0f, 18.0f));
                                int size2 = LimitReachedBottomSheet.this.premiumInviteBlockedUsers == null ? 0 : LimitReachedBottomSheet.this.premiumInviteBlockedUsers.size();
                                int size3 = LimitReachedBottomSheet.this.premiumMessagingBlockedUsers == null ? 0 : LimitReachedBottomSheet.this.premiumMessagingBlockedUsers.size();
                                if (size2 - size3 > 0 && ((size2 != 1 || size3 != 1) && LimitReachedBottomSheet.this.canSendLink)) {
                                    SimpleTextView simpleTextView = new SimpleTextView(context) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.1
                                        private final Paint paint = new Paint(1);

                                        @Override // android.view.View
                                        protected void dispatchDraw(Canvas canvas) {
                                            this.paint.setColor(Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider), 0.8f));
                                            this.paint.setStyle(Paint.Style.STROKE);
                                            this.paint.setStrokeWidth(1.0f);
                                            float height = getHeight() / 2.0f;
                                            canvas.drawLine(0.0f, height, ((getWidth() / 2.0f) - (getTextWidth() / 2.0f)) - AndroidUtilities.dp(8.0f), height, this.paint);
                                            canvas.drawLine((getWidth() / 2.0f) + (getTextWidth() / 2.0f) + AndroidUtilities.dp(8.0f), height, getWidth(), height, this.paint);
                                            super.dispatchDraw(canvas);
                                        }
                                    };
                                    simpleTextView.setGravity(17);
                                    simpleTextView.setAlignment(Layout.Alignment.ALIGN_CENTER);
                                    simpleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                    simpleTextView.setText(" " + LocaleController.getString(R.string.InvitePremiumBlockedOr) + " ");
                                    simpleTextView.setTextSize(14);
                                    addView(simpleTextView, LayoutHelper.createLinear(NotificationCenter.storiesSendAsUpdate, -2, 1, 12, 0, 12, 20));
                                    TextView textView6 = new TextView(context);
                                    textView6.setGravity(17);
                                    textView6.setTypeface(AndroidUtilities.bold());
                                    textView6.setTextSize(1, 20.0f);
                                    textView6.setTextColor(Theme.getColor(i32));
                                    textView6.setText(LocaleController.getString(R.string.InviteBlockedTitle));
                                    addView(textView6, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 9));
                                    TextView textView7 = new TextView(context);
                                    textView7.setGravity(17);
                                    textView7.setTextSize(1, 14.0f);
                                    textView7.setTextColor(Theme.getColor(i32));
                                    textView7.setText(LocaleController.getString(LimitReachedBottomSheet.this.premiumInviteBlockedUsers.size() <= 1 ? R.string.InviteBlockedOneMessage : R.string.InviteBlockedManyMessage));
                                    addView(textView7, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 19));
                                }
                                LimitReachedBottomSheet.this.updatePremiumButtonText();
                                return;
                            }
                            if (LimitReachedBottomSheet.this.type != 31) {
                                f2 = 14.0f;
                                i5 = 8;
                                LimitPreviewView limitPreviewView = new LimitPreviewView(context, i24, i30, i, f5, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.HeaderView.2
                                    @Override // android.view.View
                                    public void invalidate() {
                                        if (LimitReachedBottomSheet.this.lockInvalidation) {
                                            return;
                                        }
                                        super.invalidate();
                                    }
                                };
                                LimitReachedBottomSheet.this.limitPreviewView = limitPreviewView;
                                if (!z) {
                                    limitPreviewView.setBagePosition(f);
                                    LimitReachedBottomSheet.this.limitPreviewView.setType(LimitReachedBottomSheet.this.type);
                                    LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setVisibility(8);
                                    if (premiumFeaturesBlocked) {
                                        LimitReachedBottomSheet.this.limitPreviewView.setPremiumLocked();
                                    } else if (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) {
                                        LimitReachedBottomSheet.this.limitPreviewView.premiumCount.setVisibility(8);
                                        if (LimitReachedBottomSheet.this.type == 6) {
                                            textView3 = LimitReachedBottomSheet.this.limitPreviewView.defaultCount;
                                            num = "2 GB";
                                        } else {
                                            textView3 = LimitReachedBottomSheet.this.limitPreviewView.defaultCount;
                                            num = Integer.toString(i27);
                                        }
                                        textView3.setText(num);
                                        LimitReachedBottomSheet.this.limitPreviewView.defaultCount.setVisibility(0);
                                    }
                                } else if (LimitReachedBottomSheet.this.boostsStatus != null) {
                                    LimitReachedBottomSheet.this.limitPreviewView.setBoosts(LimitReachedBottomSheet.this.boostsStatus, LimitReachedBottomSheet.this.canApplyBoost != null && LimitReachedBottomSheet.this.canApplyBoost.boostedNow);
                                }
                                int i33 = LimitReachedBottomSheet.this.type;
                                if (i33 == 2 || i33 == 5) {
                                    LimitReachedBottomSheet.this.limitPreviewView.setDelayedAnimation();
                                }
                                addView(LimitReachedBottomSheet.this.limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, -4, 0, -4, 0));
                            } else {
                                i5 = 8;
                                f2 = 14.0f;
                            }
                            if (LimitReachedBottomSheet.this.type == 31) {
                                FrameLayout frameLayout = new FrameLayout(context);
                                ImageView imageView = new ImageView(context);
                                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.large_boosts));
                                frameLayout.addView(imageView, LayoutHelper.createFrame(-2, -2, 17));
                                frameLayout.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(79.0f), Theme.getColor(Theme.key_featuredStickers_addButton)));
                                addView(frameLayout, LayoutHelper.createLinear(79, 79, 1, 0, 23, 0, 0));
                            }
                            TextView textView8 = new TextView(context);
                            this.title = textView8;
                            textView8.setTypeface(AndroidUtilities.bold());
                            i6 = LimitReachedBottomSheet.this.type;
                            if (i6 == 31) {
                                textView = this.title;
                                i8 = R.string.BoostingAdditionalFeaturesTitle;
                            } else {
                                if (i6 != 32) {
                                    if (i6 == 19) {
                                        if (LimitReachedBottomSheet.this.getBaseFragment() instanceof GroupColorActivity) {
                                            textView = this.title;
                                            i8 = R.string.BoostingGroupBoostWhatAreBoosts;
                                        }
                                    } else if (i6 != 30) {
                                        if (i6 == 18) {
                                            if (LimitReachedBottomSheet.this.boostsStatus.level == 0) {
                                                textView = this.title;
                                                i8 = R.string.BoostingEnableStories;
                                            } else {
                                                textView = this.title;
                                                i8 = R.string.BoostingIncreaseLevel;
                                            }
                                        } else if (i6 == 21) {
                                            textView = this.title;
                                            i8 = R.string.ReactionCustomReactions;
                                        } else if (i6 == 20) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableColor;
                                        } else if (i6 == 24) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableProfileColor;
                                        } else if (i6 == 27) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableLinkIcon;
                                        } else if (i6 == 28) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableProfileIcon;
                                        } else if (i6 == 25) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableEmojiStatus;
                                        } else if (i6 == 26) {
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableWearCollectibles;
                                        } else {
                                            if (i6 != 29) {
                                                if (i6 == 22 || i6 == 23) {
                                                    textView = this.title;
                                                    i7 = R.string.BoostingEnableWallpaper;
                                                } else if (i6 == 11) {
                                                    if (LimitReachedBottomSheet.this.canSendLink) {
                                                        textView = this.title;
                                                        i7 = R.string.ChannelInviteViaLink;
                                                    } else {
                                                        textView = this.title;
                                                        i7 = R.string.ChannelInviteViaLinkRestricted;
                                                    }
                                                } else if (i6 == 6) {
                                                    textView = this.title;
                                                    i7 = R.string.FileTooLarge;
                                                } else {
                                                    textView = this.title;
                                                    i7 = R.string.LimitReached;
                                                }
                                                boostsTitleString = LocaleController.getString(i7);
                                                textView.setText(boostsTitleString);
                                                this.title.setTextSize(1, 20.0f);
                                                TextView textView9 = this.title;
                                                int i34 = Theme.key_windowBackgroundWhiteBlackText;
                                                textView9.setTextColor(Theme.getColor(i34, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                                this.title.setGravity(17);
                                                i9 = LimitReachedBottomSheet.this.type;
                                                if (i9 != 19 || i9 == 32 || LimitReachedBottomSheet.this.isMiniBoostBtnForAdminAvailable()) {
                                                    BoostCounterView boostCounterView = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                                                    this.boostCounterView = boostCounterView;
                                                    boostCounterView.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
                                                    if (LimitReachedBottomSheet.this.type == 32) {
                                                        this.boostCounterView.setVisibility(i5);
                                                    }
                                                    if (!LimitReachedBottomSheet.this.isCurrentChat) {
                                                        addView(this.title, LayoutHelper.createLinear(-2, -2, 1, 0, premiumFeaturesBlocked ? 8 : 22, 0, 0));
                                                        LinearLayout linearLayout = new LinearLayout(getContext());
                                                        linearLayout.setOrientation(0);
                                                        linearLayout.setClipChildren(false);
                                                        FrameLayout frameLayout2 = new FrameLayout(getContext());
                                                        frameLayout2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(f2), Theme.getColor(Theme.key_windowBackgroundGray, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider)));
                                                        BackupImageView backupImageView = new BackupImageView(getContext());
                                                        backupImageView.setRoundRadius(AndroidUtilities.dp(f2));
                                                        TLRPC.Chat chat2 = MessagesController.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).getChat(Long.valueOf(-LimitReachedBottomSheet.this.dialogId));
                                                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                                                        avatarDrawable.setInfo(((BottomSheet) LimitReachedBottomSheet.this).currentAccount, chat2);
                                                        backupImageView.setForUserOrChat(chat2, avatarDrawable);
                                                        frameLayout2.addView(backupImageView, LayoutHelper.createFrame(28, 28.0f));
                                                        TextView textView10 = new TextView(getContext());
                                                        if (chat2 != null) {
                                                            textView10.setText(chat2.title);
                                                        }
                                                        textView10.setSingleLine(true);
                                                        textView10.setMaxLines(1);
                                                        textView10.setEllipsize(TextUtils.TruncateAt.END);
                                                        textView10.setTextSize(1, 13.0f);
                                                        textView10.setTextColor(Theme.getColor(i34, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                                        frameLayout2.addView(textView10, LayoutHelper.createFrame(-2, -2.0f, 16, 36.0f, 0.0f, 12.0f, 0.0f));
                                                        linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-2, 28, 80, 18, 0, 18, 0));
                                                        LayoutTransition layoutTransition = new LayoutTransition();
                                                        layoutTransition.setDuration(100L);
                                                        layoutTransition.enableTransitionType(4);
                                                        linearLayout.setLayoutTransition(layoutTransition);
                                                        linearLayout.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, -30, 2, 18, 0));
                                                        addView(linearLayout, LayoutHelper.createLinear(-2, 38, 17, 0, -4, 0, 12));
                                                        ScaleStateListAnimator.apply(linearLayout);
                                                        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$HeaderView$$ExternalSyntheticLambda1
                                                            @Override // android.view.View.OnClickListener
                                                            public final void onClick(View view3) {
                                                                LimitReachedBottomSheet.HeaderView.this.lambda$new$1(view3);
                                                            }
                                                        });
                                                        TextView textView11 = new TextView(context);
                                                        this.description = textView11;
                                                        textView11.setText(AndroidUtilities.replaceTags(formatString));
                                                        this.description.setTextSize(1, 14.0f);
                                                        this.description.setGravity(1);
                                                        TextView textView12 = this.description;
                                                        textView12.setLineSpacing(textView12.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                                                        if (LimitReachedBottomSheet.this.type == 18) {
                                                            textView2 = this.description;
                                                            resourcesProvider = ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider;
                                                        } else {
                                                            if (((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider instanceof DarkThemeResourceProvider) {
                                                                textView2 = this.description;
                                                                color = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                                                                textView2.setTextColor(color);
                                                                if (LimitReachedBottomSheet.this.type == 19) {
                                                                    view2 = this.description;
                                                                    i17 = 24;
                                                                    i18 = 17;
                                                                    i19 = -2;
                                                                    i20 = -2;
                                                                    i21 = 1;
                                                                    i22 = 24;
                                                                    i23 = -2;
                                                                } else {
                                                                    view2 = this.description;
                                                                    i17 = 24;
                                                                    i18 = 24;
                                                                    i19 = -2;
                                                                    i20 = -2;
                                                                    i21 = 1;
                                                                    i22 = 24;
                                                                    i23 = 0;
                                                                }
                                                                addView(view2, LayoutHelper.createLinear(i19, i20, i21, i22, i23, i17, i18));
                                                                if (LimitReachedBottomSheet.this.type == 31) {
                                                                    ((ViewGroup.MarginLayoutParams) this.description.getLayoutParams()).bottomMargin = AndroidUtilities.dp(15.0f);
                                                                    ((ViewGroup.MarginLayoutParams) this.title.getLayoutParams()).bottomMargin = AndroidUtilities.dp(6.0f);
                                                                    ((ViewGroup.MarginLayoutParams) this.title.getLayoutParams()).topMargin = AndroidUtilities.dp(12.0f);
                                                                }
                                                                LimitReachedBottomSheet.this.updatePremiumButtonText();
                                                                return;
                                                            }
                                                            textView2 = this.description;
                                                            resourcesProvider = ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider;
                                                        }
                                                        color = Theme.getColor(i34, resourcesProvider);
                                                        textView2.setTextColor(color);
                                                        if (LimitReachedBottomSheet.this.type == 19) {
                                                        }
                                                        addView(view2, LayoutHelper.createLinear(i19, i20, i21, i22, i23, i17, i18));
                                                        if (LimitReachedBottomSheet.this.type == 31) {
                                                        }
                                                        LimitReachedBottomSheet.this.updatePremiumButtonText();
                                                        return;
                                                    }
                                                    LinearLayout linearLayout2 = new LinearLayout(context);
                                                    this.titleLinearLayout = linearLayout2;
                                                    linearLayout2.setOrientation(0);
                                                    this.titleLinearLayout.setWeightSum(1.0f);
                                                    this.titleLinearLayout.addView(this.title, LayoutHelper.createLinear(-2, -2, 1.0f, 0));
                                                    this.titleLinearLayout.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, 0, 2, 0, 0));
                                                    view = this.titleLinearLayout;
                                                    i10 = premiumFeaturesBlocked ? 8 : 22;
                                                    i11 = 12;
                                                    i12 = 9;
                                                    i13 = -2;
                                                    i14 = -2;
                                                    i15 = 1;
                                                    i16 = 12;
                                                } else {
                                                    view = this.title;
                                                    i10 = premiumFeaturesBlocked ? 8 : 22;
                                                    i11 = 0;
                                                    i12 = 10;
                                                    i13 = -2;
                                                    i14 = -2;
                                                    i15 = 1;
                                                    i16 = 0;
                                                }
                                                addView(view, LayoutHelper.createLinear(i13, i14, i15, i16, i10, i11, i12));
                                                TextView textView112 = new TextView(context);
                                                this.description = textView112;
                                                textView112.setText(AndroidUtilities.replaceTags(formatString));
                                                this.description.setTextSize(1, 14.0f);
                                                this.description.setGravity(1);
                                                TextView textView122 = this.description;
                                                textView122.setLineSpacing(textView122.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
                                                if (LimitReachedBottomSheet.this.type == 18) {
                                                }
                                                color = Theme.getColor(i34, resourcesProvider);
                                                textView2.setTextColor(color);
                                                if (LimitReachedBottomSheet.this.type == 19) {
                                                }
                                                addView(view2, LayoutHelper.createLinear(i19, i20, i21, i22, i23, i17, i18));
                                                if (LimitReachedBottomSheet.this.type == 31) {
                                                }
                                                LimitReachedBottomSheet.this.updatePremiumButtonText();
                                                return;
                                            }
                                            textView = this.title;
                                            i8 = R.string.BoostingEnableGroupEmojiPack;
                                        }
                                    }
                                }
                                textView = this.title;
                                boostsTitleString = LimitReachedBottomSheet.this.getBoostsTitleString();
                                textView.setText(boostsTitleString);
                                this.title.setTextSize(1, 20.0f);
                                TextView textView92 = this.title;
                                int i342 = Theme.key_windowBackgroundWhiteBlackText;
                                textView92.setTextColor(Theme.getColor(i342, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                                this.title.setGravity(17);
                                i9 = LimitReachedBottomSheet.this.type;
                                if (i9 != 19) {
                                }
                                BoostCounterView boostCounterView2 = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                                this.boostCounterView = boostCounterView2;
                                boostCounterView2.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
                                if (LimitReachedBottomSheet.this.type == 32) {
                                }
                                if (!LimitReachedBottomSheet.this.isCurrentChat) {
                                }
                            }
                            i7 = i8;
                            boostsTitleString = LocaleController.getString(i7);
                            textView.setText(boostsTitleString);
                            this.title.setTextSize(1, 20.0f);
                            TextView textView922 = this.title;
                            int i3422 = Theme.key_windowBackgroundWhiteBlackText;
                            textView922.setTextColor(Theme.getColor(i3422, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                            this.title.setGravity(17);
                            i9 = LimitReachedBottomSheet.this.type;
                            if (i9 != 19) {
                            }
                            BoostCounterView boostCounterView22 = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                            this.boostCounterView = boostCounterView22;
                            boostCounterView22.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
                            if (LimitReachedBottomSheet.this.type == 32) {
                            }
                            if (!LimitReachedBottomSheet.this.isCurrentChat) {
                            }
                        }
                    }
                }
                z = true;
                if (z) {
                }
                if (i4 != 11) {
                }
                if (LimitReachedBottomSheet.this.type != 31) {
                }
                if (LimitReachedBottomSheet.this.type == 31) {
                }
                TextView textView82 = new TextView(context);
                this.title = textView82;
                textView82.setTypeface(AndroidUtilities.bold());
                i6 = LimitReachedBottomSheet.this.type;
                if (i6 == 31) {
                }
                i7 = i8;
                boostsTitleString = LocaleController.getString(i7);
                textView.setText(boostsTitleString);
                this.title.setTextSize(1, 20.0f);
                TextView textView9222 = this.title;
                int i34222 = Theme.key_windowBackgroundWhiteBlackText;
                textView9222.setTextColor(Theme.getColor(i34222, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                this.title.setGravity(17);
                i9 = LimitReachedBottomSheet.this.type;
                if (i9 != 19) {
                }
                BoostCounterView boostCounterView222 = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
                this.boostCounterView = boostCounterView222;
                boostCounterView222.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
                if (LimitReachedBottomSheet.this.type == 32) {
                }
                if (!LimitReachedBottomSheet.this.isCurrentChat) {
                }
            } else {
                str = premiumFeaturesBlocked ? LimitReachedBottomSheet.this.limitParams.descriptionStrLocked : (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium() || LimitReachedBottomSheet.this.isVeryLargeFile) ? LimitReachedBottomSheet.this.limitParams.descriptionStrPremium : LimitReachedBottomSheet.this.limitParams.descriptionStr;
            }
            formatString = str;
            LimitParams limitParams22 = LimitReachedBottomSheet.this.limitParams;
            int i272 = limitParams22.defaultLimit;
            i = limitParams22.premiumLimit;
            i2 = LimitReachedBottomSheet.this.currentValue;
            i3 = LimitReachedBottomSheet.this.type;
            if (i3 != 3) {
            }
            if (UserConfig.getInstance(((BottomSheet) LimitReachedBottomSheet.this).currentAccount).isPremium()) {
            }
            i2 = i;
            f = 1.0f;
            float f52 = i272 / i;
            i4 = LimitReachedBottomSheet.this.type;
            if (i4 == 18) {
            }
            z = true;
            if (z) {
            }
            if (i4 != 11) {
            }
            if (LimitReachedBottomSheet.this.type != 31) {
            }
            if (LimitReachedBottomSheet.this.type == 31) {
            }
            TextView textView822 = new TextView(context);
            this.title = textView822;
            textView822.setTypeface(AndroidUtilities.bold());
            i6 = LimitReachedBottomSheet.this.type;
            if (i6 == 31) {
            }
            i7 = i8;
            boostsTitleString = LocaleController.getString(i7);
            textView.setText(boostsTitleString);
            this.title.setTextSize(1, 20.0f);
            TextView textView92222 = this.title;
            int i342222 = Theme.key_windowBackgroundWhiteBlackText;
            textView92222.setTextColor(Theme.getColor(i342222, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
            this.title.setGravity(17);
            i9 = LimitReachedBottomSheet.this.type;
            if (i9 != 19) {
            }
            BoostCounterView boostCounterView2222 = new BoostCounterView(context, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider);
            this.boostCounterView = boostCounterView2222;
            boostCounterView2222.setCount(LimitReachedBottomSheet.this.canApplyBoost.boostCount, false);
            if (LimitReachedBottomSheet.this.type == 32) {
            }
            if (!LimitReachedBottomSheet.this.isCurrentChat) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            if (LimitReachedBottomSheet.this.parentFragment == null) {
                return;
            }
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            LimitReachedBottomSheet.this.parentFragment.showAsSheet(new PremiumPreviewFragment("invite_privacy"), bottomSheetParams);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            LimitReachedBottomSheet.this.getBaseFragment().presentFragment(ChatActivity.of(LimitReachedBottomSheet.this.dialogId));
            LimitReachedBottomSheet.this.lambda$new$0();
        }

        public void recreateTitleAndDescription() {
            int indexOfChild;
            View view;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int indexOfChild2 = indexOfChild(this.description);
            if (LimitReachedBottomSheet.this.isCurrentChat) {
                indexOfChild = indexOfChild(this.titleLinearLayout);
                removeView(this.titleLinearLayout);
                this.titleLinearLayout.removeView(this.title);
                this.titleLinearLayout.removeView(this.boostCounterView);
                LinearLayout linearLayout = new LinearLayout(getContext());
                this.titleLinearLayout = linearLayout;
                linearLayout.setOrientation(0);
                this.titleLinearLayout.setWeightSum(1.0f);
                this.titleLinearLayout.addView(this.title, LayoutHelper.createLinear(-2, -2, 1.0f, 0));
                this.titleLinearLayout.addView(this.boostCounterView, LayoutHelper.createLinear(-2, -2, 48, 0, 2, 0, 0));
                view = this.titleLinearLayout;
                i = 12;
                i2 = 9;
                i3 = -2;
                i4 = -2;
                i5 = 1;
                i6 = 25;
            } else {
                indexOfChild = indexOfChild(this.title);
                removeView(this.title);
                TextView textView = new TextView(getContext());
                this.title = textView;
                textView.setTypeface(AndroidUtilities.bold());
                this.title.setTextSize(1, 20.0f);
                this.title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
                this.title.setGravity(17);
                view = this.title;
                i = 0;
                i2 = 0;
                i3 = -2;
                i4 = -2;
                i5 = 1;
                i6 = 0;
            }
            addView(view, indexOfChild, LayoutHelper.createLinear(i3, i4, i5, i6, 22, i, i2));
            removeView(this.description);
            TextView textView2 = new TextView(getContext());
            this.description = textView2;
            textView2.setTextSize(1, 14.0f);
            TextView textView3 = this.description;
            textView3.setLineSpacing(textView3.getLineSpacingExtra(), this.description.getLineSpacingMultiplier() * 1.1f);
            this.description.setGravity(1);
            this.description.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, ((BottomSheet) LimitReachedBottomSheet.this).resourcesProvider));
            addView(this.description, indexOfChild2, LayoutHelper.createLinear(-2, -2, 1, 24, -2, 24, 17));
        }
    }

    public static class LimitParams {
        int icon = 0;
        String descriptionStr = null;
        String descriptionStrPremium = null;
        String descriptionStrLocked = null;
        int defaultLimit = 0;
        int premiumLimit = 0;
    }

    public LimitReachedBottomSheet(BaseFragment baseFragment, Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        super(context, baseFragment, false, hasFixedSize(i), false, resourcesProvider);
        this.linkRow = -1;
        this.lockInvalidation = false;
        this.chats = new ArrayList();
        this.headerRow = -1;
        this.dividerRow = -1;
        this.chatsTitleRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.loadingRow = -1;
        this.emptyViewDividerRow = -1;
        this.bottomRow = -1;
        this.boostFeaturesStartRow = -1;
        this.currentValue = -1;
        this.selectedChats = new HashSet();
        this.inactiveChats = new ArrayList();
        this.inactiveChatsSignatures = new ArrayList();
        this.restrictedUsers = new ArrayList();
        this.premiumMessagingBlockedUsers = new ArrayList();
        this.premiumInviteBlockedUsers = new ArrayList();
        this.loading = false;
        this.requiredLvl = 0;
        this.shiftDp = -4;
        fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider));
        this.parentFragment = baseFragment;
        this.currentAccount = i2;
        this.type = i;
        updateTitle();
        updateRows();
        if (i == 2) {
            loadAdminedChannels();
        } else if (i == 5) {
            loadInactiveChannels();
        }
        updatePremiumButtonText();
        if (i == 32 || isBoostingForAdminPossible()) {
            FireworksOverlay fireworksOverlay = new FireworksOverlay(getContext());
            this.fireworksOverlay = fireworksOverlay;
            this.container.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
        }
        if (i == 18 || i == 20 || i == 24 || i == 25 || i == 26 || i == 29 || i == 22 || i == 23 || i == 21 || i == 27 || i == 28 || i == 30 || i == 31) {
            ((ViewGroup) this.premiumButtonView.getParent()).removeView(this.premiumButtonView);
            View view = this.divider;
            if (view != null) {
                ((ViewGroup) view.getParent()).removeView(this.divider);
            }
            this.recyclerListView.setPadding(0, 0, 0, 0);
            TextView textView = new TextView(context);
            this.actionBtn = textView;
            textView.setGravity(17);
            this.actionBtn.setEllipsize(TextUtils.TruncateAt.END);
            this.actionBtn.setSingleLine(true);
            this.actionBtn.setTextSize(1, 14.0f);
            this.actionBtn.setTypeface(AndroidUtilities.bold());
            this.actionBtn.setText(this.premiumButtonView.getTextView().getText());
            this.actionBtn.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            this.actionBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LimitReachedBottomSheet.this.lambda$new$0(view2);
                }
            });
            this.actionBtn.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider), 120)));
        }
        if (i == 32) {
            ((ViewGroup) this.premiumButtonView.getParent()).removeView(this.premiumButtonView);
            ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
            this.boostToUnlockGroupBtn = buttonWithCounterView;
            buttonWithCounterView.withCounterIcon();
            this.boostToUnlockGroupBtn.setText(LocaleController.getString(R.string.BoostGroup), false);
            this.boostToUnlockGroupBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LimitReachedBottomSheet.this.lambda$new$1(view2);
                }
            });
            this.containerView.addView(this.boostToUnlockGroupBtn, LayoutHelper.createFrame(-1, 48.0f, 80, 16.0f, 2.0f, 16.0f, 12.0f));
            this.containerView.post(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    LimitReachedBottomSheet.this.lambda$new$2();
                }
            });
        }
        if (i == 19 || i == 18) {
            this.containerView.post(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    LimitReachedBottomSheet.this.lambda$new$3();
                }
            });
        }
    }

    private void boostChannel() {
        Loadable loadable;
        if (this.boostMiniBtn.isAttachedToWindow()) {
            loadable = this.boostMiniBtn;
        } else {
            ButtonWithCounterView buttonWithCounterView = this.boostToUnlockGroupBtn;
            loadable = (buttonWithCounterView == null || !buttonWithCounterView.isAttachedToWindow()) ? this.premiumButtonView : this.boostToUnlockGroupBtn;
        }
        boostChannel(loadable);
    }

    private void boostChannel(Loadable loadable) {
        boostChannel(loadable, false);
    }

    private void boostChannel(final Loadable loadable, boolean z) {
        if (!loadable.isLoading() || z) {
            loadable.setLoading(true);
            MessagesController.getInstance(this.currentAccount).getBoostsController().applyBoost(this.dialogId, this.canApplyBoost.slot, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda22
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    LimitReachedBottomSheet.this.lambda$boostChannel$17(loadable, (TL_stories.TL_premium_myBoosts) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda23
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    LimitReachedBottomSheet.this.lambda$boostChannel$19(loadable, (TLRPC.TL_error) obj);
                }
            });
        }
    }

    private ArrayList boostFeaturesForLevel(int i) {
        boolean isGroup = isGroup();
        ArrayList arrayList = new ArrayList();
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (messagesController == null) {
            return arrayList;
        }
        arrayList.add(BoostFeature.of(R.drawable.menu_feature_stories, "BoostFeatureStoriesPerDay", i).asIncremental());
        if (!isGroup) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_reactions, "BoostFeatureCustomReaction", i).asIncremental());
        }
        MessagesController.PeerColors peerColors = messagesController.peerColors;
        int colorsAvailable = peerColors != null ? peerColors.colorsAvailable(i, false) : 0;
        MessagesController.PeerColors peerColors2 = messagesController.profilePeerColors;
        int colorsAvailable2 = peerColors2 != null ? peerColors2.colorsAvailable(i, isGroup) : 0;
        if (!isGroup && colorsAvailable > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_color_name, "BoostFeatureNameColor", 7));
        }
        if (!isGroup && colorsAvailable > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_links, "BoostFeatureReplyColor", colorsAvailable));
        }
        if (!isGroup && i >= messagesController.channelBgIconLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_links2, R.string.BoostFeatureReplyIcon));
        }
        if (colorsAvailable2 > 0) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_color_profile, isGroup ? "BoostFeatureProfileColorGroup" : "BoostFeatureProfileColor", colorsAvailable2));
        }
        if (isGroup && i >= messagesController.groupEmojiStickersLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_pack, R.string.BoostFeatureCustomEmojiPack));
        }
        if ((!isGroup && i >= messagesController.channelProfileIconLevelMin) || (isGroup && i >= messagesController.groupProfileBgIconLevelMin)) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_cover, isGroup ? R.string.BoostFeatureProfileIconGroup : R.string.BoostFeatureProfileIcon));
        }
        if (isGroup && i >= messagesController.groupTranscribeLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_voice, R.string.BoostFeatureVoiceToTextConversion));
        }
        if ((!isGroup && i >= messagesController.channelEmojiStatusLevelMin) || (isGroup && i >= messagesController.groupEmojiStatusLevelMin)) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_status, R.string.BoostFeatureEmojiStatuses, "1000+"));
        }
        if ((!isGroup && i >= messagesController.channelWallpaperLevelMin) || (isGroup && i >= messagesController.groupWallpaperLevelMin)) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_wallpaper, isGroup ? "BoostFeatureBackgroundGroup" : "BoostFeatureBackground", 8));
        }
        if ((!isGroup && i >= messagesController.channelCustomWallpaperLevelMin) || (isGroup && i >= messagesController.groupCustomWallpaperLevelMin)) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_custombg, isGroup ? R.string.BoostFeatureCustomBackgroundGroup : R.string.BoostFeatureCustomBackground));
        }
        if (!isGroup && i >= messagesController.channelRestrictSponsoredLevelMin) {
            arrayList.add(BoostFeature.of(R.drawable.menu_feature_noads, R.string.BoostFeatureSwitchOffAds));
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    private String getBoostDescriptionStringAfterBoost() {
        String str;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        boolean isGroup = isGroup();
        int i = this.type;
        if (i == 20) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForColorDescription : R.string.ChannelNeedBoostsForColorDescription, Integer.valueOf(channelColorLevelMin()));
        } else if (i == 24) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForProfileColorDescription : R.string.ChannelNeedBoostsForProfileColorDescription, Integer.valueOf(channelColorLevelMin()));
        } else if (i == 29) {
            str = LocaleController.formatString(R.string.GroupNeedBoostsForCustomEmojiPackDescription, Integer.valueOf(messagesController.groupEmojiStickersLevelMin));
        } else if (i == 30) {
            str = LocaleController.formatString(R.string.ChannelNeedBoostsForSwitchOffAdsDescription, Integer.valueOf(messagesController.channelRestrictSponsoredLevelMin));
        } else if (i == 25) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForEmojiStatusDescription : R.string.ChannelNeedBoostsForEmojiStatusDescription, Integer.valueOf(isGroup ? messagesController.groupEmojiStatusLevelMin : messagesController.channelEmojiStatusLevelMin));
        } else if (i == 26) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForWearCollectiblesDescription : R.string.ChannelNeedBoostsForWearCollectiblesDescription, Integer.valueOf(isGroup ? messagesController.groupEmojiStatusLevelMin : messagesController.channelEmojiStatusLevelMin));
        } else if (i == 27) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForReplyIconDescription : R.string.ChannelNeedBoostsForReplyIconDescription, Integer.valueOf(messagesController.channelBgIconLevelMin));
        } else if (i == 28) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForProfileIconDescription : R.string.ChannelNeedBoostsForProfileIconDescription, Integer.valueOf(isGroup ? messagesController.groupProfileBgIconLevelMin : messagesController.channelProfileIconLevelMin));
        } else if (i == 22) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForWallpaperDescription : R.string.ChannelNeedBoostsForWallpaperDescription, Integer.valueOf(isGroup ? messagesController.groupWallpaperLevelMin : messagesController.channelWallpaperLevelMin));
        } else if (i == 23) {
            str = LocaleController.formatString(isGroup ? R.string.GroupNeedBoostsForCustomWallpaperDescription : R.string.ChannelNeedBoostsForCustomWallpaperDescription, Integer.valueOf(isGroup ? messagesController.groupCustomWallpaperLevelMin : messagesController.channelCustomWallpaperLevelMin));
        } else {
            str = null;
        }
        return str != null ? str : getBoostsDescriptionString(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostLink() {
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        return (tL_premium_boostsStatus == null || TextUtils.isEmpty(tL_premium_boostsStatus.boost_url)) ? ChannelBoostUtilities.createLink(this.currentAccount, this.dialogId) : this.boostsStatus.boost_url;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostsDescriptionString(boolean z) {
        String str;
        if (this.type == 32) {
            return getDescriptionForRemoveRestrictions();
        }
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        if (chat == null) {
            str = LocaleController.getString(isGroup() ? R.string.AccDescrGroup : R.string.AccDescrChannel);
        } else {
            str = chat.title;
        }
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        boolean z2 = tL_premium_boostsStatus.boosts == tL_premium_boostsStatus.current_level_boosts;
        if (isMiniBoostBtnForAdminAvailable() && this.boostsStatus.next_level_boosts != 0 && z) {
            int i = isGroup() ? R.string.GroupNeedBoostsDescriptionForNewFeatures : R.string.ChannelNeedBoostsDescriptionForNewFeatures;
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus2 = this.boostsStatus;
            int i2 = tL_premium_boostsStatus2.next_level_boosts - tL_premium_boostsStatus2.boosts;
            return LocaleController.formatString(i, str, LocaleController.formatPluralString("MoreBoosts", i2, Integer.valueOf(i2)));
        }
        if (z2 && this.canApplyBoost.alreadyActive) {
            if (this.boostsStatus.level == 1) {
                return LocaleController.formatString(isGroup() ? R.string.GroupBoostsJustReachedLevel1 : R.string.ChannelBoostsJustReachedLevel1, new Object[0]);
            }
            return LocaleController.formatString(isGroup() ? R.string.GroupBoostsJustReachedLevelNext : R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(this.boostsStatus.level), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level, new Object[0]));
        }
        if (this.canApplyBoost.alreadyActive) {
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus3 = this.boostsStatus;
            if (tL_premium_boostsStatus3.level == 0) {
                int i3 = isGroup() ? R.string.GroupNeedBoostsDescriptionForNewFeatures : R.string.ChannelNeedBoostsDescriptionForNewFeatures;
                TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus4 = this.boostsStatus;
                int i4 = tL_premium_boostsStatus4.next_level_boosts - tL_premium_boostsStatus4.boosts;
                return LocaleController.formatString(i3, str, LocaleController.formatPluralString("MoreBoosts", i4, Integer.valueOf(i4)));
            }
            if (tL_premium_boostsStatus3.next_level_boosts == 0) {
                return LocaleController.formatString(isGroup() ? R.string.GroupBoostsJustReachedLevelNext : R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(this.boostsStatus.level), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level + 1, new Object[0]));
            }
            int i5 = isGroup() ? R.string.GroupNeedBoostsDescriptionForNewFeatures : R.string.ChannelNeedBoostsDescriptionForNewFeatures;
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus5 = this.boostsStatus;
            int i6 = tL_premium_boostsStatus5.next_level_boosts - tL_premium_boostsStatus5.boosts;
            return LocaleController.formatString(i5, str, LocaleController.formatPluralString("MoreBoosts", i6, Integer.valueOf(i6)));
        }
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus6 = this.boostsStatus;
        if (tL_premium_boostsStatus6.level == 0) {
            int i7 = isGroup() ? R.string.GroupNeedBoostsDescriptionForNewFeatures : R.string.ChannelNeedBoostsDescriptionForNewFeatures;
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus7 = this.boostsStatus;
            int i8 = tL_premium_boostsStatus7.next_level_boosts - tL_premium_boostsStatus7.boosts;
            return LocaleController.formatString(i7, str, LocaleController.formatPluralString("MoreBoosts", i8, Integer.valueOf(i8)));
        }
        if (tL_premium_boostsStatus6.next_level_boosts == 0) {
            return LocaleController.formatString(isGroup() ? R.string.GroupBoostsJustReachedLevelNext : R.string.ChannelBoostsJustReachedLevelNext, Integer.valueOf(this.boostsStatus.level), LocaleController.formatPluralString("BoostStories", this.boostsStatus.level + 1, new Object[0]));
        }
        int i9 = isGroup() ? R.string.GroupNeedBoostsDescriptionForNewFeatures : R.string.ChannelNeedBoostsDescriptionForNewFeatures;
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus8 = this.boostsStatus;
        int i10 = tL_premium_boostsStatus8.next_level_boosts - tL_premium_boostsStatus8.boosts;
        return LocaleController.formatString(i9, str, LocaleController.formatPluralString("MoreBoosts", i10, Integer.valueOf(i10)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoostsTitleString() {
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        if (tL_premium_boostsStatus.next_level_boosts == 0) {
            return LocaleController.formatString("BoostsMaxLevelReached", R.string.BoostsMaxLevelReached, new Object[0]);
        }
        if (tL_premium_boostsStatus.level > 0 && !this.canApplyBoost.alreadyActive) {
            return LocaleController.getString(isGroup() ? R.string.BoostGroup : R.string.BoostChannel);
        }
        if (!this.isCurrentChat) {
            if (this.type == 32) {
                return LocaleController.getString(isGroup() ? R.string.BoostGroup : R.string.BoostChannel);
            }
            if (this.canApplyBoost.alreadyActive) {
                return LocaleController.getString(isGroup() ? R.string.YouBoostedGroup : R.string.YouBoostedChannel);
            }
            return LocaleController.getString(isGroup() ? R.string.BoostingEnableStoriesForGroup : R.string.BoostingEnableStoriesForChannel);
        }
        if (this.type == 32) {
            return LocaleController.getString(isGroup() ? R.string.BoostGroup : R.string.BoostChannel);
        }
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        if (this.canApplyBoost.alreadyActive) {
            return LocaleController.formatString("YouBoostedChannel2", R.string.YouBoostedChannel2, chat.title);
        }
        return LocaleController.getString(isGroup() ? R.string.BoostGroup : R.string.BoostChannel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TLRPC.Chat getChat() {
        return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
    }

    private TLRPC.ChatFull getChatFull() {
        return MessagesController.getInstance(this.currentAccount).getChatFull(-this.dialogId);
    }

    private String getDescriptionForRemoveRestrictions() {
        TLRPC.Chat chat = getChat();
        return LocaleController.formatPluralString("BoostingRemoveRestrictionsSubtitle", getNeededBoostsForUnlockGroup(), chat == null ? "" : chat.title);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LimitParams getLimitParams(int i, int i2) {
        String formatString;
        LimitParams limitParams = new LimitParams();
        if (i == 0) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersPinnedLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersPinnedLimitPremium;
            limitParams.icon = R.drawable.msg_limit_pin;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedPinDialogs", R.string.LimitReachedPinDialogs, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedPinDialogsPremium", R.string.LimitReachedPinDialogsPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedPinDialogsLocked", R.string.LimitReachedPinDialogsLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 33) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).savedDialogsPinnedLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).savedDialogsPinnedLimitPremium;
            limitParams.icon = R.drawable.msg_limit_pin;
            limitParams.descriptionStr = LocaleController.formatString(R.string.LimitReachedPinSavedDialogs, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString(R.string.LimitReachedPinSavedDialogsPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString(R.string.LimitReachedPinSavedDialogsLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 2) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).publicLinksLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).publicLinksLimitPremium;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedPublicLinks", R.string.LimitReachedPublicLinks, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedPublicLinksPremium", R.string.LimitReachedPublicLinksPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedPublicLinksLocked", R.string.LimitReachedPublicLinksLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 12) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).chatlistInvitesLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).chatlistInvitesLimitPremium;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFolderLinks", R.string.LimitReachedFolderLinks, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFolderLinksPremium", R.string.LimitReachedFolderLinksPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedFolderLinksLocked", R.string.LimitReachedFolderLinksLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 13) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).chatlistJoinedLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).chatlistJoinedLimitPremium;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedSharedFolders", R.string.LimitReachedSharedFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedSharedFoldersPremium", R.string.LimitReachedSharedFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedSharedFoldersLocked", R.string.LimitReachedSharedFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 3) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersLimitPremium;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFolders", R.string.LimitReachedFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFoldersPremium", R.string.LimitReachedFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedFoldersLocked", R.string.LimitReachedFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 4) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).dialogFiltersChatsLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).dialogFiltersChatsLimitPremium;
            limitParams.icon = R.drawable.msg_limit_chats;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedChatInFolders", R.string.LimitReachedChatInFolders, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedChatInFoldersPremium", R.string.LimitReachedChatInFoldersPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedChatInFoldersLocked", R.string.LimitReachedChatInFoldersLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 5) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).channelsLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).channelsLimitPremium;
            limitParams.icon = R.drawable.msg_limit_groups;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedCommunities", R.string.LimitReachedCommunities, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedCommunitiesPremium", R.string.LimitReachedCommunitiesPremium, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedCommunitiesLocked", R.string.LimitReachedCommunitiesLocked, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 6) {
            limitParams.defaultLimit = 100;
            limitParams.premiumLimit = NotificationCenter.storyQualityUpdate;
            limitParams.icon = R.drawable.msg_limit_folder;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedFileSize", R.string.LimitReachedFileSize, "2 GB", "4 GB");
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedFileSizePremium", R.string.LimitReachedFileSizePremium, "4 GB");
            formatString = LocaleController.formatString("LimitReachedFileSizeLocked", R.string.LimitReachedFileSizeLocked, "2 GB");
        } else if (i == 7) {
            limitParams.defaultLimit = 3;
            limitParams.premiumLimit = 4;
            limitParams.icon = R.drawable.msg_limit_accounts;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedAccounts", R.string.LimitReachedAccounts, 3, Integer.valueOf(limitParams.premiumLimit));
            int i3 = R.string.LimitReachedAccountsPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedAccountsPremium", i3, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedAccountsPremium", i3, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 11) {
            limitParams.defaultLimit = 0;
            limitParams.premiumLimit = 0;
            limitParams.icon = R.drawable.msg_limit_links;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedAccounts", R.string.LimitReachedAccounts, 0, Integer.valueOf(limitParams.premiumLimit));
            formatString = "";
            limitParams.descriptionStrPremium = "";
        } else if (i == 14) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storyExpiringLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storyExpiringLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesCount", R.string.LimitReachedStoriesCount, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i4 = R.string.LimitReachedStoriesCountPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesCountPremium", i4, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedStoriesCountPremium", i4, Integer.valueOf(limitParams.defaultLimit));
        } else if (i == 15) {
            limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentWeeklyLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentWeeklyLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesWeekly", R.string.LimitReachedStoriesWeekly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i5 = R.string.LimitReachedStoriesWeeklyPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesWeeklyPremium", i5, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedStoriesWeeklyPremium", i5, Integer.valueOf(limitParams.defaultLimit));
        } else {
            if (i != 16) {
                if (i == 18 || i == 32 || i == 20 || i == 24 || i == 27 || i == 28 || i == 25 || i == 30 || i == 29 || i == 22 || i == 23 || i == 19 || i == 21 || i == 26) {
                    limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitDefault;
                    limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitPremium;
                    limitParams.icon = R.drawable.filled_limit_boost;
                    limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesMonthly", R.string.LimitReachedStoriesMonthly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
                    int i6 = R.string.LimitReachedStoriesMonthlyPremium;
                    limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i6, Integer.valueOf(limitParams.premiumLimit));
                    formatString = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i6, Integer.valueOf(limitParams.defaultLimit));
                }
                return limitParams;
            }
            limitParams.defaultLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitDefault;
            limitParams.premiumLimit = MessagesController.getInstance(i2).storiesSentMonthlyLimitPremium;
            limitParams.icon = R.drawable.msg_limit_stories;
            limitParams.descriptionStr = LocaleController.formatString("LimitReachedStoriesMonthly", R.string.LimitReachedStoriesMonthly, Integer.valueOf(limitParams.defaultLimit), Integer.valueOf(limitParams.premiumLimit));
            int i7 = R.string.LimitReachedStoriesMonthlyPremium;
            limitParams.descriptionStrPremium = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i7, Integer.valueOf(limitParams.premiumLimit));
            formatString = LocaleController.formatString("LimitReachedStoriesMonthlyPremium", i7, Integer.valueOf(limitParams.defaultLimit));
        }
        limitParams.descriptionStrLocked = formatString;
        return limitParams;
    }

    private int getNeededBoostsForUnlockGroup() {
        TLRPC.ChatFull chatFull = getChatFull();
        return Math.max(chatFull.boosts_unrestrict - chatFull.boosts_applied, 0);
    }

    private static boolean hasFixedSize(int i) {
        return i == 0 || i == 33 || i == 3 || i == 4 || i == 6 || i == 7 || i == 12 || i == 13 || i == 14 || i == 15 || i == 16;
    }

    private boolean isBoostingForAdminPossible() {
        int i = this.type;
        return i == 19 || i == 18 || i == 20 || i == 24 || i == 25 || i == 26 || i == 29 || i == 22 || i == 27 || i == 28 || i == 23 || i == 30;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGroup() {
        return !ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMiniBoostBtnForAdminAvailable() {
        return isBoostingForAdminPossible() && ChatObject.hasAdminRights(getChat());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$16(Loadable loadable, TL_stories.TL_premium_myBoosts tL_premium_myBoosts, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        TLRPC.ChatFull chatFull;
        loadable.setLoading(false);
        if (tL_premium_boostsStatus == null) {
            return;
        }
        this.boostsStatus.boosts++;
        if (this.type == 32 && (chatFull = getChatFull()) != null) {
            chatFull.boosts_applied++;
        }
        limitPreviewIncreaseCurrentValue();
        setBoostsStats(tL_premium_boostsStatus, this.isCurrentChat);
        ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
        canApplyBoost.isMaxLvl = this.boostsStatus.next_level_boosts <= 0;
        canApplyBoost.boostedNow = true;
        canApplyBoost.setMyBoosts(tL_premium_myBoosts);
        onBoostSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$17(final Loadable loadable, final TL_stories.TL_premium_myBoosts tL_premium_myBoosts) {
        MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda27
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LimitReachedBottomSheet.this.lambda$boostChannel$16(loadable, tL_premium_myBoosts, (TL_stories.TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$18(Loadable loadable) {
        boostChannel(loadable, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$boostChannel$19(final Loadable loadable, TLRPC.TL_error tL_error) {
        if (tL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tL_error.text).intValue();
            if (intValue <= 5) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        LimitReachedBottomSheet.this.lambda$boostChannel$18(loadable);
                    }
                }, intValue * 1000);
                return;
            }
            BoostDialogs.showFloodWait(intValue);
        }
        loadable.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$leaveFromSelectedGroups$21(ArrayList arrayList, TLRPC.User user, AlertDialog alertDialog, int i) {
        lambda$new$0();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC.Chat chat = (TLRPC.Chat) arrayList.get(i2);
            MessagesController.getInstance(this.currentAccount).putChat(chat, false);
            MessagesController.getInstance(this.currentAccount).deleteParticipantFromChat(chat.id, user);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAdminedChannels$22(TLObject tLObject) {
        int i;
        this.loadingAdminedChannels = false;
        if (tLObject != null) {
            this.chats.clear();
            this.chats.addAll(((TLRPC.TL_messages_chats) tLObject).chats);
            this.loading = false;
            this.enterAnimator.showItemsAnimated(this.chatsTitleRow + 4);
            int i2 = 0;
            while (true) {
                if (i2 >= this.recyclerListView.getChildCount()) {
                    i = 0;
                    break;
                } else {
                    if (this.recyclerListView.getChildAt(i2) instanceof HeaderView) {
                        i = this.recyclerListView.getChildAt(i2).getTop();
                        break;
                    }
                    i2++;
                }
            }
            updateRows();
            if (this.headerRow >= 0 && i != 0) {
                ((LinearLayoutManager) this.recyclerListView.getLayoutManager()).scrollToPositionWithOffset(this.headerRow + 1, i);
            }
        }
        int max = Math.max(this.chats.size(), this.limitParams.defaultLimit);
        this.limitPreviewView.setIconValue(max, false);
        this.limitPreviewView.setBagePosition(max / this.limitParams.premiumLimit);
        this.limitPreviewView.startDelayedAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAdminedChannels$23(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                LimitReachedBottomSheet.this.lambda$loadAdminedChannels$22(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInactiveChannels$26(ArrayList arrayList, int i, TLRPC.TL_messages_inactiveChats tL_messages_inactiveChats) {
        int i2;
        this.inactiveChatsSignatures.clear();
        this.inactiveChats.clear();
        this.inactiveChatsSignatures.addAll(arrayList);
        for (int i3 = 0; i3 < i; i3++) {
            this.inactiveChats.add(tL_messages_inactiveChats.chats.get(i3));
        }
        this.loading = false;
        this.enterAnimator.showItemsAnimated(this.chatsTitleRow + 4);
        int i4 = 0;
        while (true) {
            if (i4 >= this.recyclerListView.getChildCount()) {
                i2 = 0;
                break;
            } else {
                if (this.recyclerListView.getChildAt(i4) instanceof HeaderView) {
                    i2 = this.recyclerListView.getChildAt(i4).getTop();
                    break;
                }
                i4++;
            }
        }
        updateRows();
        if (this.headerRow >= 0 && i2 != 0) {
            ((LinearLayoutManager) this.recyclerListView.getLayoutManager()).scrollToPositionWithOffset(this.headerRow + 1, i2);
        }
        if (this.limitParams == null) {
            this.limitParams = getLimitParams(this.type, this.currentAccount);
        }
        int max = Math.max(this.inactiveChats.size(), this.limitParams.defaultLimit);
        LimitPreviewView limitPreviewView = this.limitPreviewView;
        if (limitPreviewView != null) {
            limitPreviewView.setIconValue(max, false);
            this.limitPreviewView.setBagePosition(max / this.limitParams.premiumLimit);
            this.limitPreviewView.startDelayedAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInactiveChannels$27(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            final TLRPC.TL_messages_inactiveChats tL_messages_inactiveChats = (TLRPC.TL_messages_inactiveChats) tLObject;
            final ArrayList arrayList = new ArrayList();
            final int min = Math.min(tL_messages_inactiveChats.chats.size(), tL_messages_inactiveChats.dates.size());
            for (int i = 0; i < min; i++) {
                TLRPC.Chat chat = tL_messages_inactiveChats.chats.get(i);
                int currentTime = (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - tL_messages_inactiveChats.dates.get(i).intValue()) / 86400;
                String formatPluralString = currentTime < 30 ? LocaleController.formatPluralString("Days", currentTime, new Object[0]) : currentTime < 365 ? LocaleController.formatPluralString("Months", currentTime / 30, new Object[0]) : LocaleController.formatPluralString("Years", currentTime / 365, new Object[0]);
                arrayList.add(ChatObject.isMegagroup(chat) ? LocaleController.formatString("InactiveChatSignature", R.string.InactiveChatSignature, LocaleController.formatPluralString("Members", chat.participants_count, new Object[0]), formatPluralString) : ChatObject.isChannel(chat) ? LocaleController.formatString("InactiveChannelSignature", R.string.InactiveChannelSignature, formatPluralString) : LocaleController.formatString("InactiveChatSignature", R.string.InactiveChatSignature, LocaleController.formatPluralString("Members", chat.participants_count, new Object[0]), formatPluralString));
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    LimitReachedBottomSheet.this.lambda$loadInactiveChannels$26(arrayList, min, tL_messages_inactiveChats);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        AndroidUtilities.addToClipboard(getBoostLink());
        lambda$new$0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        (this.premiumButtonView.isShowOverlay() ? this.premiumButtonView.overlayTextView : this.premiumButtonView.buttonLayout).performClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        this.boostToUnlockGroupBtn.setCount(getNeededBoostsForUnlockGroup(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        if (ChatObject.hasAdminRights(getChat())) {
            if (this.premiumButtonView.getParent() != null) {
                ((ViewGroup) this.premiumButtonView.getParent()).removeView(this.premiumButtonView);
            }
            View view = this.divider;
            if (view != null && view.getParent() != null) {
                ((ViewGroup) this.divider.getParent()).removeView(this.divider);
            }
            this.recyclerListView.setPadding(0, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$10(AlertDialog alertDialog, int i) {
        alertDialog.dismiss();
        boostChannel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$12(Context context, View view) {
        int i = this.type;
        if (i == 11) {
            return;
        }
        if (i != 19 && i != 32 && !isMiniBoostBtnForAdminAvailable()) {
            int i2 = this.type;
            if (i2 == 18 || i2 == 20 || i2 == 24 || i2 == 25 || i2 == 26 || i2 == 29 || i2 == 22 || i2 == 23 || i2 == 21 || i2 == 27 || i2 == 28 || i2 == 30) {
                AndroidUtilities.addToClipboard(getBoostLink());
                lambda$new$0();
                return;
            }
            if (UserConfig.getInstance(this.currentAccount).isPremium() || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.isVeryLargeFile) {
                lambda$new$0();
                return;
            }
            BaseFragment baseFragment = this.parentFragment;
            if (baseFragment == null) {
                return;
            }
            if (baseFragment.getVisibleDialog() != null) {
                this.parentFragment.getVisibleDialog().dismiss();
            }
            this.parentFragment.presentFragment(new PremiumPreviewFragment(limitTypeToServerString(this.type)));
            Runnable runnable = this.onShowPremiumScreenRunnable;
            if (runnable != null) {
                runnable.run();
            }
            lambda$new$0();
            return;
        }
        ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
        if (canApplyBoost.empty) {
            if (UserConfig.getInstance(this.currentAccount).isPremium() && BoostRepository.isMultiBoostsAvailable()) {
                BoostDialogs.showMoreBoostsNeeded(this.dialogId, this);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context, this.resourcesProvider);
            builder.setTitle(LocaleController.getString(R.string.PremiumNeeded));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(isGroup() ? R.string.PremiumNeededForBoostingGroup : R.string.PremiumNeededForBoosting)));
            builder.setPositiveButton(LocaleController.getString(R.string.CheckPhoneNumberYes), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i3) {
                    LimitReachedBottomSheet.this.lambda$onViewCreated$7(alertDialog, i3);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda16
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i3) {
                    alertDialog.dismiss();
                }
            });
            builder.show();
            return;
        }
        boolean z = canApplyBoost.canApply;
        if (z && canApplyBoost.replaceDialogId == 0) {
            if (!canApplyBoost.needSelector || !BoostRepository.isMultiBoostsAvailable()) {
                boostChannel();
                return;
            }
            this.lockInvalidation = true;
            this.limitPreviewView.invalidationEnabled = false;
            BaseFragment baseFragment2 = getBaseFragment();
            ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
            ReassignBoostBottomSheet.show(baseFragment2, canApplyBoost2.myBoosts, canApplyBoost2.currentChat).setOnHideListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda17
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LimitReachedBottomSheet.this.lambda$onViewCreated$9(dialogInterface);
                }
            });
            return;
        }
        if (!z) {
            int i3 = canApplyBoost.floodWait;
            if (i3 != 0) {
                BoostDialogs.showFloodWait(i3);
                return;
            }
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        BackupImageView backupImageView = new BackupImageView(getContext());
        backupImageView.setRoundRadius(AndroidUtilities.dp(30.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(60, 60.0f));
        frameLayout.setClipChildren(false);
        final Paint paint = new Paint(1);
        paint.setColor(Theme.getColor(Theme.key_dialogBackground));
        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.filled_limit_boost);
        frameLayout.addView(new View(getContext()) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.3
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float measuredWidth = getMeasuredWidth() / 2.0f;
                float measuredHeight = getMeasuredHeight() / 2.0f;
                canvas.drawCircle(measuredWidth, measuredHeight, getMeasuredWidth() / 2.0f, paint);
                PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), -AndroidUtilities.dp(10.0f), 0.0f);
                canvas.drawCircle(measuredWidth, measuredHeight, (getMeasuredWidth() / 2.0f) - AndroidUtilities.dp(2.0f), PremiumGradient.getInstance().getMainGradientPaint());
                float dp = AndroidUtilities.dp(18.0f) / 2.0f;
                drawable.setBounds((int) (measuredWidth - dp), (int) (measuredHeight - dp), (int) (measuredWidth + dp), (int) (measuredHeight + dp));
                drawable.draw(canvas);
            }
        }, LayoutHelper.createFrame(28, 28.0f, 0, 34.0f, 34.0f, 0.0f, 0.0f));
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.msg_arrow_avatar);
        imageView.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon));
        frameLayout.addView(imageView, LayoutHelper.createFrame(24, 24, 17));
        BackupImageView backupImageView2 = new BackupImageView(getContext());
        backupImageView2.setRoundRadius(AndroidUtilities.dp(30.0f));
        frameLayout.addView(backupImageView2, LayoutHelper.createFrame(60, 60.0f, 0, 96.0f, 0.0f, 0.0f, 0.0f));
        FrameLayout frameLayout2 = new FrameLayout(getContext());
        frameLayout2.addView(frameLayout, LayoutHelper.createFrame(-2, 60, 1));
        frameLayout2.setClipChildren(false);
        TextView textView = new TextView(context);
        if (Build.VERSION.SDK_INT >= 21) {
            textView.setLetterSpacing(0.025f);
        }
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setTextSize(1, 16.0f);
        frameLayout2.addView(textView, LayoutHelper.createLinear(-1, -2, 0, 24, 80, 24, 0));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.canApplyBoost.replaceDialogId));
        avatarDrawable.setInfo(this.currentAccount, chat);
        backupImageView.setForUserOrChat(chat, avatarDrawable);
        AvatarDrawable avatarDrawable2 = new AvatarDrawable();
        TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        avatarDrawable2.setInfo(this.currentAccount, chat2);
        backupImageView2.setForUserOrChat(chat2, avatarDrawable2);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setView(frameLayout2);
        textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ReplaceBoostChannelDescription", R.string.ReplaceBoostChannelDescription, chat.title, chat2.title)));
        builder2.setPositiveButton(LocaleController.getString(R.string.Replace), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda18
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i4) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$10(alertDialog, i4);
            }
        });
        builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i4) {
                alertDialog.dismiss();
            }
        });
        builder2.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$13() {
        this.limitPreviewView.setBoosts(this.boostsStatus, false);
        limitPreviewIncreaseCurrentValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$14(View view) {
        int i = this.type;
        if (i == 19 || i == 32 || isMiniBoostBtnForAdminAvailable()) {
            ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
            if (canApplyBoost.canApply) {
                this.premiumButtonView.buttonLayout.callOnClick();
                ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
                if (canApplyBoost2.alreadyActive && canApplyBoost2.boostedNow) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            LimitReachedBottomSheet.this.lambda$onViewCreated$13();
                        }
                    }, this.canApplyBoost.needSelector ? 300L : 0L);
                    return;
                }
                return;
            }
            if (canApplyBoost.alreadyActive && BoostRepository.isMultiBoostsAvailable() && !this.canApplyBoost.isMaxLvl) {
                BoostDialogs.showMoreBoostsNeeded(this.dialogId, this);
                return;
            } else {
                lambda$new$0();
                return;
            }
        }
        if (this.type == 11) {
            if (this.selectedChats.isEmpty()) {
                lambda$new$0();
                return;
            } else {
                sendInviteMessages();
                return;
            }
        }
        if (this.selectedChats.isEmpty()) {
            return;
        }
        int i2 = this.type;
        if (i2 == 2) {
            revokeSelectedLinks();
        } else if (i2 == 5) {
            leaveFromSelectedGroups();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$4(View view) {
        (this.premiumButtonView.isShowOverlay() ? this.premiumButtonView.overlayTextView : this.premiumButtonView.buttonLayout).performClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$5(View view, int i) {
        if (view instanceof AdminedChannelCell) {
            AdminedChannelCell adminedChannelCell = (AdminedChannelCell) view;
            TLRPC.Chat currentChannel = adminedChannelCell.getCurrentChannel();
            if (this.selectedChats.contains(currentChannel)) {
                this.selectedChats.remove(currentChannel);
            } else {
                this.selectedChats.add(currentChannel);
            }
            adminedChannelCell.setChecked(this.selectedChats.contains(currentChannel), true);
        } else {
            if (!(view instanceof GroupCreateUserCell)) {
                return;
            }
            if (!this.canSendLink && this.type == 11) {
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) view;
            Object object = groupCreateUserCell.getObject();
            if (groupCreateUserCell.isBlocked()) {
                if (object instanceof TLRPC.User) {
                    showPremiumBlockedToast(groupCreateUserCell, ((TLRPC.User) object).id);
                    return;
                }
                return;
            } else {
                if (this.selectedChats.contains(object)) {
                    this.selectedChats.remove(object);
                } else {
                    this.selectedChats.add(object);
                }
                groupCreateUserCell.setChecked(this.selectedChats.contains(object), true);
            }
        }
        updateButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onViewCreated$6(View view, int i) {
        this.recyclerListView.getOnItemClickListener().onItemClick(view, i);
        if (this.type != 19) {
            try {
                view.performHapticFeedback(0);
            } catch (Exception unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$7(AlertDialog alertDialog, int i) {
        this.parentFragment.presentFragment(new PremiumPreviewFragment(null));
        lambda$new$0();
        alertDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$9(DialogInterface dialogInterface) {
        this.lockInvalidation = false;
        this.limitPreviewView.invalidationEnabled = true;
        this.premiumButtonView.invalidate();
        this.limitPreviewView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeLinks$24(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            AndroidUtilities.runOnUIThread(this.onSuccessRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeLinks$25(ArrayList arrayList, AlertDialog alertDialog, int i) {
        lambda$new$0();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC.TL_channels_updateUsername tL_channels_updateUsername = new TLRPC.TL_channels_updateUsername();
            tL_channels_updateUsername.channel = MessagesController.getInputChannel((TLRPC.Chat) arrayList.get(i2));
            tL_channels_updateUsername.username = "";
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_updateUsername, new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LimitReachedBottomSheet.this.lambda$revokeLinks$24(tLObject, tL_error);
                }
            }, 64);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendInviteMessages$20() {
        Bulletin createSimpleBulletin;
        BulletinFactory global = BulletinFactory.global();
        if (global != null) {
            if (this.selectedChats.size() == 1) {
                createSimpleBulletin = global.createSimpleBulletin(R.raw.voip_invite, AndroidUtilities.replaceTags(LocaleController.formatString("InviteLinkSentSingle", R.string.InviteLinkSentSingle, ContactsController.formatName((TLRPC.User) this.selectedChats.iterator().next()))));
            } else {
                createSimpleBulletin = global.createSimpleBulletin(R.raw.voip_invite, AndroidUtilities.replaceTags(LocaleController.formatPluralString("InviteLinkSent", this.selectedChats.size(), Integer.valueOf(this.selectedChats.size()))));
            }
            createSimpleBulletin.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$15() {
        if (LaunchActivity.getLastFragment() == null) {
            return;
        }
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        this.parentFragment.showAsSheet(new PremiumPreviewFragment("noncontacts"), bottomSheetParams);
    }

    private void leaveFromSelectedGroups() {
        String formatString;
        final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
        final ArrayList arrayList = new ArrayList();
        Iterator it = this.selectedChats.iterator();
        while (it.hasNext()) {
            arrayList.add((TLRPC.Chat) it.next());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.formatPluralString("LeaveCommunities", arrayList.size(), new Object[0]));
        if (arrayList.size() == 1) {
            formatString = LocaleController.formatString("ChannelLeaveAlertWithName", R.string.ChannelLeaveAlertWithName, ((TLRPC.Chat) arrayList.get(0)).title);
        } else {
            formatString = LocaleController.formatString("ChatsLeaveAlert", R.string.ChatsLeaveAlert, new Object[0]);
        }
        builder.setMessage(AndroidUtilities.replaceTags(formatString));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.VoipGroupLeave), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda21
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                LimitReachedBottomSheet.this.lambda$leaveFromSelectedGroups$21(arrayList, user, alertDialog, i);
            }
        });
        AlertDialog create = builder.create();
        create.show();
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold, this.resourcesProvider));
        }
    }

    private void limitPreviewIncreaseCurrentValue() {
        LimitPreviewView limitPreviewView = this.limitPreviewView;
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        int i = tL_premium_boostsStatus.boosts;
        int i2 = tL_premium_boostsStatus.current_level_boosts;
        limitPreviewView.increaseCurrentValue(i, i - i2, tL_premium_boostsStatus.next_level_boosts - i2);
    }

    public static String limitTypeToServerString(int i) {
        switch (i) {
            case 0:
                return "double_limits__dialog_pinned";
            case 1:
            case 7:
            case 11:
            default:
                return null;
            case 2:
                return "double_limits__channels_public";
            case 3:
                return "double_limits__dialog_filters";
            case 4:
                return "double_limits__dialog_filters_chats";
            case 5:
                return "double_limits__channels";
            case 6:
                return "double_limits__upload_max_fileparts";
            case 8:
                return "double_limits__caption_length";
            case 9:
                return "double_limits__saved_gifs";
            case 10:
                return "double_limits__stickers_faved";
            case 12:
                return "double_limits__chatlist_invites";
            case 13:
                return "double_limits__chatlists_joined";
        }
    }

    private void loadAdminedChannels() {
        this.loadingAdminedChannels = true;
        this.loading = true;
        updateRows();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_channels_getAdminedPublicChannels(), new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LimitReachedBottomSheet.this.lambda$loadAdminedChannels$23(tLObject, tL_error);
            }
        });
    }

    private void loadInactiveChannels() {
        this.loading = true;
        updateRows();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_channels_getInactiveChannels(), new RequestDelegate() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda4
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LimitReachedBottomSheet.this.lambda$loadInactiveChannels$27(tLObject, tL_error);
            }
        });
    }

    private boolean onBoostSuccess() {
        NotificationCenter.getInstance(this.currentAccount).postNotificationNameOnUIThread(NotificationCenter.chatWasBoostedByUser, this.boostsStatus, this.canApplyBoost.copy(), Long.valueOf(this.dialogId));
        if (this.boostToUnlockGroupBtn != null) {
            int neededBoostsForUnlockGroup = getNeededBoostsForUnlockGroup();
            if (neededBoostsForUnlockGroup == 0) {
                NotificationCenter.getInstance(this.currentAccount).postNotificationNameOnUIThread(NotificationCenter.groupRestrictionsUnlockedByBoosts, new Object[0]);
                lambda$new$0();
                return false;
            }
            this.boostToUnlockGroupBtn.setCount(neededBoostsForUnlockGroup, true);
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new Visibility() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.4
            @Override // android.transition.Visibility
            public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(view, (Property<View, Float>) View.TRANSLATION_Y, AndroidUtilities.dp(20.0f), 0.0f));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }

            @Override // android.transition.Visibility
            public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 0.0f), ObjectAnimator.ofFloat(view, (Property<View, Float>) View.TRANSLATION_Y, 0.0f, -AndroidUtilities.dp(20.0f)));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }
        });
        transitionSet.setOrdering(0);
        TransitionManager.beginDelayedTransition(this.headerView, transitionSet);
        this.headerView.recreateTitleAndDescription();
        this.headerView.title.setText(getBoostsTitleString());
        this.headerView.description.setText(AndroidUtilities.replaceTags(getBoostDescriptionStringAfterBoost()));
        updateButton();
        this.fireworksOverlay.start();
        try {
            this.fireworksOverlay.performHapticFeedback(3);
        } catch (Exception unused) {
        }
        this.headerView.boostCounterView.setCount(this.canApplyBoost.boostCount, true);
        this.recyclerListView.smoothScrollToPosition(0);
        if (this.type == 32) {
            this.headerView.boostCounterView.setVisibility(8);
        }
        return true;
    }

    public static void openBoostsForPostingStories(BaseFragment baseFragment, long j, ChannelBoostsController.CanApplyBoost canApplyBoost, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, Runnable runnable) {
        if (baseFragment == null || canApplyBoost == null || tL_premium_boostsStatus == null || baseFragment.getContext() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, baseFragment.getContext(), 18, baseFragment.getCurrentAccount(), baseFragment.getResourceProvider());
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(tL_premium_boostsStatus, true);
        limitReachedBottomSheet.setDialogId(j);
        limitReachedBottomSheet.showStatisticButtonInLink(runnable);
        limitReachedBottomSheet.show();
    }

    public static LimitReachedBottomSheet openBoostsForRemoveRestrictions(BaseFragment baseFragment, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, ChannelBoostsController.CanApplyBoost canApplyBoost, long j, boolean z) {
        if (baseFragment == null || tL_premium_boostsStatus == null || canApplyBoost == null || baseFragment.getContext() == null) {
            return null;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, baseFragment.getContext(), 32, baseFragment.getCurrentAccount(), baseFragment.getResourceProvider());
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(tL_premium_boostsStatus, true);
        limitReachedBottomSheet.setDialogId(j);
        if (z) {
            baseFragment.showDialog(limitReachedBottomSheet);
        } else {
            limitReachedBottomSheet.show();
        }
        return limitReachedBottomSheet;
    }

    public static void openBoostsForUsers(BaseFragment baseFragment, boolean z, long j, ChannelBoostsController.CanApplyBoost canApplyBoost, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, ChatMessageCell chatMessageCell) {
        if (baseFragment == null || canApplyBoost == null || tL_premium_boostsStatus == null || baseFragment.getContext() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, baseFragment.getContext(), 19, baseFragment.getCurrentAccount(), baseFragment.getResourceProvider());
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(tL_premium_boostsStatus, z);
        limitReachedBottomSheet.setDialogId(j);
        limitReachedBottomSheet.setChatMessageCell(chatMessageCell);
        baseFragment.showDialog(limitReachedBottomSheet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeLinks(final ArrayList arrayList) {
        String formatString;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.formatPluralString("RevokeLinks", arrayList.size(), new Object[0]));
        if (arrayList.size() == 1) {
            TLRPC.Chat chat = (TLRPC.Chat) arrayList.get(0);
            if (this.parentIsChannel) {
                formatString = LocaleController.formatString("RevokeLinkAlertChannel", R.string.RevokeLinkAlertChannel, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ChatObject.getPublicUsername(chat), chat.title);
            } else {
                formatString = LocaleController.formatString("RevokeLinkAlert", R.string.RevokeLinkAlert, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ChatObject.getPublicUsername(chat), chat.title);
            }
        } else {
            formatString = this.parentIsChannel ? LocaleController.formatString("RevokeLinksAlertChannel", R.string.RevokeLinksAlertChannel, new Object[0]) : LocaleController.formatString("RevokeLinksAlert", R.string.RevokeLinksAlert, new Object[0]);
        }
        builder.setMessage(AndroidUtilities.replaceTags(formatString));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.RevokeButton), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda13
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                LimitReachedBottomSheet.this.lambda$revokeLinks$25(arrayList, alertDialog, i);
            }
        });
        AlertDialog create = builder.create();
        create.show();
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold, this.resourcesProvider));
        }
    }

    private void revokeSelectedLinks() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.selectedChats.iterator();
        while (it.hasNext()) {
            this.chats.add((TLRPC.Chat) it.next());
        }
        revokeLinks(arrayList);
    }

    private void sendInviteMessages() {
        String str;
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.fromChat.id);
        if (chatFull == null) {
            lambda$new$0();
            return;
        }
        if (this.fromChat.username != null) {
            str = "@" + this.fromChat.username;
        } else {
            TLRPC.TL_chatInviteExported tL_chatInviteExported = chatFull.exported_invite;
            if (tL_chatInviteExported == null) {
                lambda$new$0();
                return;
            }
            str = tL_chatInviteExported.link;
        }
        Iterator it = this.selectedChats.iterator();
        while (it.hasNext()) {
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(str, ((TLRPC.User) it.next()).id, null, null, null, true, null, null, null, false, 0, null, false));
            str = str;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                LimitReachedBottomSheet.this.lambda$sendInviteMessages$20();
            }
        });
        lambda$new$0();
    }

    private void setupBoostFeatures() {
        int max;
        int i;
        this.boostFeatures = new ArrayList();
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList arrayList = null;
        int i2 = 10;
        if (messagesController != null) {
            MessagesController.PeerColors peerColors = messagesController.peerColors;
            int max2 = Math.max(10, peerColors != null ? peerColors.maxLevel(isGroup()) : 0);
            MessagesController.PeerColors peerColors2 = messagesController.profilePeerColors;
            int max3 = Math.max(max2, peerColors2 != null ? peerColors2.maxLevel(isGroup()) : 0);
            if (isGroup()) {
                max = Math.max(Math.max(Math.max(Math.max(max3, messagesController.groupTranscribeLevelMin), messagesController.groupWallpaperLevelMin), messagesController.groupCustomWallpaperLevelMin), messagesController.groupEmojiStatusLevelMin);
                i = messagesController.groupProfileBgIconLevelMin;
            } else {
                max = Math.max(Math.max(Math.max(Math.max(Math.max(max3, messagesController.channelBgIconLevelMin), messagesController.channelProfileIconLevelMin), messagesController.channelEmojiStatusLevelMin), messagesController.channelWallpaperLevelMin), messagesController.channelCustomWallpaperLevelMin);
                i = messagesController.channelRestrictSponsoredLevelMin;
            }
            i2 = Math.max(max, i);
        }
        for (int i3 = this.type != 31 ? tL_premium_boostsStatus != null ? tL_premium_boostsStatus.level + 1 : 1 : 1; i3 <= i2; i3++) {
            ArrayList boostFeaturesForLevel = boostFeaturesForLevel(i3);
            if (arrayList == null || !BoostFeature.arraysEqual(arrayList, boostFeaturesForLevel)) {
                ArrayList arrayList2 = this.boostFeatures;
                arrayList2.add(new BoostFeature.BoostFeatureLevel(i3, arrayList2.isEmpty()));
                this.boostFeatures.addAll(boostFeaturesForLevel);
                arrayList = boostFeaturesForLevel;
            }
        }
    }

    private void showPremiumBlockedToast(View view, long j) {
        int i = -this.shiftDp;
        this.shiftDp = i;
        AndroidUtilities.shakeViewSpring(view, i);
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        String forcedFirstName = j >= 0 ? UserObject.getForcedFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j))) : "";
        (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() ? BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, forcedFirstName))) : BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, forcedFirstName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                LimitReachedBottomSheet.this.lambda$showPremiumBlockedToast$15();
            }
        })).show();
    }

    /* JADX WARN: Code restructure failed: missing block: B:74:0x00a0, code lost:
    
        if (r0.isMaxLvl != false) goto L60;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateButton() {
        int i;
        PremiumButtonView premiumButtonView;
        PremiumButtonView premiumButtonView2;
        int i2;
        PremiumButtonView premiumButtonView3;
        int i3;
        int i4 = this.type;
        if (i4 == 19 || i4 == 32 || isMiniBoostBtnForAdminAvailable()) {
            ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
            boolean z = canApplyBoost.canApply;
            if ((!z && !canApplyBoost.empty) || canApplyBoost.boostedNow || canApplyBoost.alreadyActive) {
                if (z) {
                    if (BoostRepository.isMultiBoostsAvailable()) {
                        premiumButtonView2 = this.premiumButtonView;
                        i2 = R.string.BoostingBoostAgain;
                    } else {
                        premiumButtonView2 = this.premiumButtonView;
                        i2 = isGroup() ? R.string.BoostGroup : R.string.BoostChannel;
                    }
                    premiumButtonView2.setOverlayText(LocaleController.getString(i2), true, true);
                    this.boostMiniBtn.setText(LocaleController.getString(R.string.BoostBtn), true);
                    ButtonWithCounterView buttonWithCounterView = this.boostToUnlockGroupBtn;
                    if (buttonWithCounterView != null) {
                        buttonWithCounterView.setText(LocaleController.getString(R.string.BoostGroup), true);
                        return;
                    }
                    return;
                }
                if (!canApplyBoost.isMaxLvl) {
                    if (BoostRepository.isMultiBoostsAvailable()) {
                        ButtonWithCounterView buttonWithCounterView2 = this.boostToUnlockGroupBtn;
                        if (buttonWithCounterView2 != null) {
                            buttonWithCounterView2.setText(LocaleController.getString(R.string.BoostGroup), true);
                        }
                        this.boostMiniBtn.setText(LocaleController.getString(R.string.BoostBtn), true);
                        premiumButtonView = this.premiumButtonView;
                        i = R.string.BoostingBoostAgain;
                    } else {
                        ButtonWithCounterView buttonWithCounterView3 = this.boostToUnlockGroupBtn;
                        if (buttonWithCounterView3 != null) {
                            buttonWithCounterView3.setText(LocaleController.getString(R.string.OK), true);
                        }
                        ButtonWithCounterView buttonWithCounterView4 = this.boostMiniBtn;
                        i = R.string.OK;
                        buttonWithCounterView4.setText(LocaleController.getString(i), true);
                        premiumButtonView = this.premiumButtonView;
                    }
                    premiumButtonView.setOverlayText(LocaleController.getString(i), true, true);
                    return;
                }
                ButtonWithCounterView buttonWithCounterView5 = this.boostMiniBtn;
                int i5 = R.string.OK;
                buttonWithCounterView5.setText(LocaleController.getString(i5), true);
                ButtonWithCounterView buttonWithCounterView6 = this.boostToUnlockGroupBtn;
                if (buttonWithCounterView6 != null) {
                    buttonWithCounterView6.setText(LocaleController.getString(i5), true);
                }
            }
            premiumButtonView = this.premiumButtonView;
            i = R.string.OK;
            premiumButtonView.setOverlayText(LocaleController.getString(i), true, true);
            return;
        }
        if (this.type == 11) {
            this.premiumButtonView.checkCounterView();
            if (!this.canSendLink) {
                premiumButtonView3 = this.premiumButtonView;
                i3 = R.string.Close;
            } else if (this.selectedChats.size() > 0) {
                premiumButtonView3 = this.premiumButtonView;
                i3 = R.string.SendInviteLink;
            } else {
                premiumButtonView3 = this.premiumButtonView;
                i3 = R.string.ActionSkip;
            }
            premiumButtonView3.setOverlayText(LocaleController.getString(i3), true, true);
            this.premiumButtonView.counterView.setCount(this.selectedChats.size(), true);
            this.premiumButtonView.invalidate();
            return;
        }
        if (this.selectedChats.size() > 0) {
            int i6 = this.type;
            this.premiumButtonView.setOverlayText(i6 == 2 ? LocaleController.formatPluralString("RevokeLinks", this.selectedChats.size(), new Object[0]) : i6 == 5 ? LocaleController.formatPluralString("LeaveCommunities", this.selectedChats.size(), new Object[0]) : null, true, true);
            return;
        }
        this.premiumButtonView.clearOverlayText();
    }

    private void updateRows() {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        this.dividerRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.loadingRow = -1;
        this.linkRow = -1;
        this.emptyViewDividerRow = -1;
        this.boostFeaturesStartRow = -1;
        this.rowCount = 1;
        this.headerRow = 0;
        int i = this.type;
        if (i == 19 || i == 18 || i == 20 || i == 24 || i == 27 || i == 28 || i == 22 || i == 23 || i == 25 || i == 26 || i == 29 || i == 21 || i == 30) {
            if (i != 19 || ChatObject.hasAdminRights(getChat())) {
                this.topPadding = 0.24f;
                int i2 = this.rowCount;
                this.rowCount = i2 + 1;
                this.linkRow = i2;
                if (MessagesController.getInstance(this.currentAccount).giveawayGiftsPurchaseAvailable) {
                    int i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.bottomRow = i3;
                }
            }
            setupBoostFeatures();
            int i4 = this.rowCount;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.boostFeaturesStartRow = i4;
            this.rowCount = i5 + (this.boostFeatures.size() - 1);
        } else if (i == 31 || i == 32) {
            this.topPadding = 0.24f;
            setupBoostFeatures();
            int i6 = this.rowCount;
            this.chatStartRow = i6;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.boostFeaturesStartRow = i6;
            int size = i7 + (this.boostFeatures.size() - 1);
            this.rowCount = size;
            this.chatEndRow = size;
        } else if (!hasFixedSize(i)) {
            int i8 = this.type;
            if (i8 != 11) {
                int i9 = this.rowCount;
                this.dividerRow = i9;
                this.rowCount = i9 + 2;
                this.chatsTitleRow = i9 + 1;
            } else {
                this.topPadding = 0.24f;
            }
            if (this.loading) {
                int i10 = this.rowCount;
                this.rowCount = i10 + 1;
                this.loadingRow = i10;
            } else {
                if (i8 != 11 || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || ((((arrayList = this.premiumInviteBlockedUsers) == null || arrayList.isEmpty()) && ((arrayList2 = this.premiumMessagingBlockedUsers) == null || arrayList2.size() < this.restrictedUsers.size())) || (arrayList3 = this.premiumInviteBlockedUsers) == null || arrayList3.size() != 1 || (arrayList4 = this.premiumMessagingBlockedUsers) == null || arrayList4.size() != 1 || !this.canSendLink)) {
                    int i11 = this.rowCount;
                    this.chatStartRow = i11;
                    int i12 = this.type;
                    this.rowCount = i11 + (i12 == 11 ? this.restrictedUsers : i12 == 5 ? this.inactiveChats : this.chats).size();
                    this.chatEndRow = this.rowCount;
                }
                if (this.chatEndRow - this.chatStartRow > 1) {
                    int i13 = this.rowCount;
                    this.rowCount = i13 + 1;
                    this.emptyViewDividerRow = i13;
                }
            }
        }
        notifyDataSetChanged();
    }

    protected int channelColorLevelMin() {
        return 0;
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
        return new 5();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC.ChatFull chatFull;
        BaseFragment lastFragment;
        if (i != NotificationCenter.boostByChannelCreated) {
            if (i != NotificationCenter.boostedChannelByUser) {
                if (i == NotificationCenter.didStartedMultiGiftsSelector) {
                    lambda$new$0();
                    return;
                }
                return;
            }
            TL_stories.TL_premium_myBoosts tL_premium_myBoosts = (TL_stories.TL_premium_myBoosts) objArr[0];
            int intValue = ((Integer) objArr[1]).intValue();
            int intValue2 = ((Integer) objArr[2]).intValue();
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = (TL_stories.TL_premium_boostsStatus) objArr[3];
            if (tL_premium_boostsStatus == null || this.canApplyBoost == null) {
                return;
            }
            this.boostsStatus.boosts += intValue;
            if (this.type == 32 && (chatFull = getChatFull()) != null) {
                chatFull.boosts_applied += intValue;
            }
            limitPreviewIncreaseCurrentValue();
            setBoostsStats(tL_premium_boostsStatus, this.isCurrentChat);
            ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
            canApplyBoost.isMaxLvl = this.boostsStatus.next_level_boosts <= 0;
            canApplyBoost.boostedNow = true;
            canApplyBoost.setMyBoosts(tL_premium_myBoosts);
            if (onBoostSuccess()) {
                BulletinFactory.of(this.container, this.resourcesProvider).createSimpleBulletinWithIconSize(R.raw.ic_boosts_replace, LocaleController.formatPluralString("BoostingReassignedFromPlural", intValue, LocaleController.formatPluralString("BoostingFromOtherChannel", intValue2, new Object[0])), 30).setDuration(4000).show(true);
                return;
            }
            return;
        }
        TLRPC.Chat chat = (TLRPC.Chat) objArr[0];
        boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
        BaseFragment lastFragment2 = getBaseFragment().getParentLayout().getLastFragment();
        if (lastFragment2 instanceof ChatCustomReactionsEditActivity) {
            List fragmentStack = getBaseFragment().getParentLayout().getFragmentStack();
            BaseFragment baseFragment = fragmentStack.size() >= 2 ? (BaseFragment) fragmentStack.get(fragmentStack.size() - 2) : null;
            BaseFragment baseFragment2 = fragmentStack.size() >= 3 ? (BaseFragment) fragmentStack.get(fragmentStack.size() - 3) : null;
            r5 = fragmentStack.size() >= 4 ? (BaseFragment) fragmentStack.get(fragmentStack.size() - 4) : null;
            if (baseFragment instanceof ChatEditActivity) {
                getBaseFragment().getParentLayout().removeFragmentFromStack(baseFragment);
            }
            lambda$new$0();
            if (!booleanValue) {
                lastFragment2.lambda$onBackPressed$323();
                BoostDialogs.showBulletin(baseFragment2, chat, false);
                return;
            } else {
                if (baseFragment2 instanceof ProfileActivity) {
                    getBaseFragment().getParentLayout().removeFragmentFromStack(baseFragment2);
                }
                lastFragment2.lambda$onBackPressed$323();
                BoostDialogs.showBulletin(r5, chat, true);
                return;
            }
        }
        if ((lastFragment2 instanceof ChatActivity) && booleanValue) {
            BoostDialogs.showBulletin(lastFragment2, chat, true);
            return;
        }
        if ((lastFragment2 instanceof ChannelColorActivity) && booleanValue) {
            List fragmentStack2 = getBaseFragment().getParentLayout().getFragmentStack();
            ArrayList arrayList = new ArrayList();
            for (int size = fragmentStack2.size() - 2; size >= 0; size--) {
                BaseFragment baseFragment3 = (BaseFragment) fragmentStack2.get(size);
                if ((baseFragment3 instanceof ChatActivity) || (baseFragment3 instanceof DialogsActivity)) {
                    r5 = baseFragment3;
                    break;
                }
                arrayList.add(baseFragment3);
            }
            if (r5 == null) {
                return;
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                getBaseFragment().getParentLayout().removeFragmentFromStack((BaseFragment) it.next());
            }
            getBaseFragment().lambda$onBackPressed$323();
            lambda$new$0();
            BoostDialogs.showBulletin(r5, chat, true);
            return;
        }
        if (!booleanValue) {
            if (StoryRecorder.isVisible()) {
                lastFragment = ChatActivity.of(-chat.id);
                LaunchActivity.getLastFragment().presentFragment(lastFragment, false, false);
                StoryRecorder.destroyInstance();
                lambda$new$0();
            } else {
                lambda$new$0();
                lastFragment = LaunchActivity.getLastFragment();
            }
            BoostDialogs.showBulletin(lastFragment, chat, false);
            return;
        }
        if (StoryRecorder.isVisible()) {
            ChatActivity of = ChatActivity.of(-chat.id);
            LaunchActivity.getLastFragment().presentFragment(of, false, false);
            StoryRecorder.destroyInstance();
            lambda$new$0();
            BoostDialogs.showBulletin(of, chat, true);
            return;
        }
        List fragmentStack3 = getBaseFragment().getParentLayout().getFragmentStack();
        r5 = fragmentStack3.size() >= 2 ? (BaseFragment) fragmentStack3.get(fragmentStack3.size() - 2) : null;
        getBaseFragment().lambda$onBackPressed$323();
        lambda$new$0();
        if (r5 instanceof ChatActivity) {
            BoostDialogs.showBulletin(r5, chat, true);
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public CharSequence getTitle() {
        int i;
        int i2 = this.type;
        if (i2 != 11) {
            switch (i2) {
                case 18:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                    i = R.string.UnlockBoostChannelFeatures;
                    break;
                case 19:
                case 30:
                    return LocaleController.getString(isGroup() ? R.string.BoostGroup : R.string.BoostChannel);
                case 31:
                    i = R.string.BoostingAdditionalFeaturesTitle;
                    break;
                case 32:
                    i = R.string.BoostGroup;
                    break;
                default:
                    i = R.string.LimitReached;
                    break;
            }
        } else {
            i = R.string.ChannelInviteViaLink2;
        }
        return LocaleController.getString(i);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.boostedChannelByUser);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didStartedMultiGiftsSelector);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.boostedChannelByUser);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didStartedMultiGiftsSelector);
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public void onViewCreated(FrameLayout frameLayout) {
        int i;
        super.onViewCreated(frameLayout);
        final Context context = frameLayout.getContext();
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, this.resourcesProvider);
        this.boostMiniBtn = buttonWithCounterView;
        buttonWithCounterView.setFlickeringLoading(true);
        this.boostMiniBtn.setText(LocaleController.getString(R.string.BoostBtn), false);
        this.boostMiniBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$4(view);
            }
        });
        PremiumButtonView premiumButtonView = new PremiumButtonView(context, true, this.resourcesProvider) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.1
            @Override // android.view.View
            public void invalidate() {
                if (LimitReachedBottomSheet.this.lockInvalidation) {
                    return;
                }
                super.invalidate();
            }
        };
        this.premiumButtonView = premiumButtonView;
        ScaleStateListAnimator.apply(premiumButtonView, 0.02f, 1.2f);
        if (!this.hasFixedSize && (i = this.type) != 18 && i != 20 && i != 24 && i != 25 && i != 26 && i != 29 && i != 22 && i != 23 && i != 21 && i != 27 && i != 28 && i != 30) {
            View view = new View(context) { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet.2
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    LimitReachedBottomSheet limitReachedBottomSheet = LimitReachedBottomSheet.this;
                    if (limitReachedBottomSheet.chatEndRow - limitReachedBottomSheet.chatStartRow > 1) {
                        Paint themePaint = Theme.getThemePaint("paintDivider", ((BottomSheet) limitReachedBottomSheet).resourcesProvider);
                        if (themePaint == null) {
                            themePaint = Theme.dividerPaint;
                        }
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), 1.0f, themePaint);
                    }
                }
            };
            this.divider = view;
            view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider));
            frameLayout.addView(this.divider, LayoutHelper.createFrame(-1, 72.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        PremiumButtonView premiumButtonView2 = this.premiumButtonView;
        float f = (this.backgroundPaddingLeft / AndroidUtilities.density) + 16.0f;
        frameLayout.addView(premiumButtonView2, LayoutHelper.createFrame(-1, 48.0f, 80, f, 0.0f, f, 12.0f));
        this.recyclerListView.setPadding(0, 0, 0, AndroidUtilities.dp(72.0f));
        this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$5(view2, i2);
            }
        });
        this.recyclerListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i2) {
                boolean lambda$onViewCreated$6;
                lambda$onViewCreated$6 = LimitReachedBottomSheet.this.lambda$onViewCreated$6(view2, i2);
                return lambda$onViewCreated$6;
            }
        });
        this.premiumButtonView.buttonLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$12(context, view2);
            }
        });
        this.premiumButtonView.overlayTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.LimitReachedBottomSheet$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LimitReachedBottomSheet.this.lambda$onViewCreated$14(view2);
            }
        });
        this.enterAnimator = new RecyclerItemsEnterAnimator(this.recyclerListView, true);
    }

    public void setBoostsStats(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, boolean z) {
        this.boostsStatus = tL_premium_boostsStatus;
        this.isCurrentChat = z;
        updateRows();
    }

    public void setCanApplyBoost(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        this.canApplyBoost = canApplyBoost;
        updateButton();
        updatePremiumButtonText();
    }

    public void setChatMessageCell(ChatMessageCell chatMessageCell) {
        this.chatMessageCell = chatMessageCell;
    }

    public void setCurrentValue(int i) {
        this.currentValue = i;
    }

    public void setDialogId(long j) {
        this.dialogId = j;
        updateRows();
    }

    public void setRequiredLvl(int i) {
        this.requiredLvl = i;
    }

    public void setRestrictedUsers(TLRPC.Chat chat, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
        this.fromChat = chat;
        this.canSendLink = ChatObject.canUserDoAdminAction(chat, 3);
        this.restrictedUsers = new ArrayList(arrayList);
        this.premiumMessagingBlockedUsers = arrayList2;
        this.premiumInviteBlockedUsers = arrayList3;
        this.selectedChats.clear();
        if (this.canSendLink) {
            Iterator it = this.restrictedUsers.iterator();
            while (it.hasNext()) {
                TLRPC.User user = (TLRPC.User) it.next();
                if (arrayList2 == null || !arrayList2.contains(Long.valueOf(user.id))) {
                    this.selectedChats.add(user);
                }
            }
        }
        updateRows();
        updateButton();
        if (this.type != 11 || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()) {
            return;
        }
        if (((arrayList3 == null || arrayList3.isEmpty()) && (arrayList2 == null || arrayList2.size() < this.restrictedUsers.size())) || arrayList3 == null || arrayList2 == null) {
            return;
        }
        if (!(arrayList3.size() == 1 && arrayList2.size() == 1) && arrayList2.size() < arrayList3.size()) {
            return;
        }
        PremiumButtonView premiumButtonView = this.premiumButtonView;
        if (premiumButtonView != null && premiumButtonView.getParent() != null) {
            ((ViewGroup) this.premiumButtonView.getParent()).removeView(this.premiumButtonView);
        }
        View view = this.divider;
        if (view != null && view.getParent() != null) {
            ((ViewGroup) this.divider.getParent()).removeView(this.divider);
        }
        RecyclerListView recyclerListView = this.recyclerListView;
        if (recyclerListView != null) {
            recyclerListView.setPadding(0, 0, 0, 0);
        }
    }

    public void setVeryLargeFile(boolean z) {
        this.isVeryLargeFile = z;
        updatePremiumButtonText();
    }

    public void showStatisticButtonInLink(Runnable runnable) {
        this.statisticClickRunnable = runnable;
    }

    public void updatePremiumButtonText() {
        AnimatedTextView animatedTextView;
        int i;
        PremiumButtonView premiumButtonView;
        int i2;
        int i3 = this.type;
        if (i3 == 19 || i3 == 32 || isMiniBoostBtnForAdminAvailable()) {
            if (BoostRepository.isMultiBoostsAvailable()) {
                AnimatedTextView animatedTextView2 = this.premiumButtonView.buttonTextView;
                ChannelBoostsController.CanApplyBoost canApplyBoost = this.canApplyBoost;
                animatedTextView2.setText(LocaleController.getString((canApplyBoost == null || !canApplyBoost.alreadyActive) ? isGroup() ? R.string.BoostGroup : R.string.BoostChannel : R.string.BoostingBoostAgain));
                ChannelBoostsController.CanApplyBoost canApplyBoost2 = this.canApplyBoost;
                if (canApplyBoost2 == null || !canApplyBoost2.isMaxLvl) {
                    return;
                }
                animatedTextView = this.premiumButtonView.buttonTextView;
                i = R.string.OK;
            } else {
                animatedTextView = this.premiumButtonView.buttonTextView;
                i = isGroup() ? R.string.BoostGroup : R.string.BoostChannel;
            }
            animatedTextView.setText(LocaleController.getString(i));
            return;
        }
        int i4 = this.type;
        if (i4 == 18 || i4 == 20 || i4 == 24 || i4 == 25 || i4 == 26 || i4 == 29 || i4 == 22 || i4 == 23 || i4 == 21 || i4 == 27 || i4 == 28 || i4 == 30) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("d ");
            spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_copy_filled), 0, 1, 0);
            spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.CopyLink));
            this.premiumButtonView.buttonTextView.setText(spannableStringBuilder);
            return;
        }
        if (UserConfig.getInstance(this.currentAccount).isPremium() || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || this.isVeryLargeFile) {
            this.premiumButtonView.buttonTextView.setText(LocaleController.getString(R.string.OK));
        } else {
            this.premiumButtonView.buttonTextView.setText(LocaleController.getString(R.string.IncreaseLimit));
            LimitParams limitParams = this.limitParams;
            if (limitParams != null) {
                int i5 = limitParams.defaultLimit;
                int i6 = i5 + 1;
                int i7 = limitParams.premiumLimit;
                if (i6 == i7) {
                    premiumButtonView = this.premiumButtonView;
                    i2 = R.raw.addone_icon;
                } else if (i5 != 0 && i7 != 0) {
                    float f = i7 / i5;
                    if (f >= 1.6f && f <= 2.5f) {
                        premiumButtonView = this.premiumButtonView;
                        i2 = R.raw.double_icon;
                    }
                }
                premiumButtonView.setIcon(i2);
                return;
            }
        }
        this.premiumButtonView.hideIcon();
    }
}
