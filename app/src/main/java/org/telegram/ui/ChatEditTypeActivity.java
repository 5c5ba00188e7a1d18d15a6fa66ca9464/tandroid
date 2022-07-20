package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_checkUsername;
import org.telegram.tgnet.TLRPC$TL_channels_getAdminedPublicChannels;
import org.telegram.tgnet.TLRPC$TL_channels_updateUsername;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputChannelEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_exportChatInvite;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.InviteLinkBottomSheet;
import org.telegram.ui.Components.JoinToSendSettingsView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkActionView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
/* loaded from: classes3.dex */
public class ChatEditTypeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ShadowSectionCell adminedInfoCell;
    private LinearLayout adminnedChannelsLayout;
    private long chatId;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextInfoPrivacyCell checkTextView;
    private TLRPC$Chat currentChat;
    private ActionBarMenuItem doneButton;
    private EditTextBoldCursor editText;
    private HeaderCell headerCell;
    private HeaderCell headerCell2;
    private boolean ignoreTextChanges;
    private TLRPC$ChatFull info;
    private TextInfoPrivacyCell infoCell;
    private TLRPC$TL_chatInviteExported invite;
    private InviteLinkBottomSheet inviteLinkBottomSheet;
    private boolean isChannel;
    private boolean isForcePublic;
    private boolean isPrivate;
    private boolean isSaveRestricted;
    private JoinToSendSettingsView joinContainer;
    private String lastCheckName;
    private boolean lastNameAvailable;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutTypeContainer;
    private LinearLayout linkContainer;
    private LoadingCell loadingAdminedCell;
    private boolean loadingAdminedChannels;
    private TextInfoPrivacyCell manageLinksInfoCell;
    private TextCell manageLinksTextView;
    private LinkActionView permanentLinkView;
    private LinearLayout privateContainer;
    private LinearLayout publicContainer;
    private RadioButtonCell radioButtonCell1;
    private RadioButtonCell radioButtonCell2;
    private LinearLayout saveContainer;
    private HeaderCell saveHeaderCell;
    private TextCheckCell saveRestrictCell;
    private TextInfoPrivacyCell saveRestrictInfoCell;
    private ShadowSectionCell sectionCell2;
    private TextSettingsCell textCell;
    private TextSettingsCell textCell2;
    private TextInfoPrivacyCell typeInfoCell;
    private EditTextBoldCursor usernameTextView;
    private boolean canCreatePublic = true;
    private ArrayList<AdminedChannelCell> adminedChannelCells = new ArrayList<>();
    HashMap<Long, TLRPC$User> usersMap = new HashMap<>();

    public ChatEditTypeActivity(long j, boolean z) {
        this.chatId = j;
        this.isForcePublic = z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0048, code lost:
        if (r0 == null) goto L10;
     */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        boolean z = false;
        if (chat == null) {
            TLRPC$Chat chatSync = getMessagesStorage().getChatSync(this.chatId);
            this.currentChat = chatSync;
            if (chatSync != null) {
                getMessagesController().putChat(this.currentChat, true);
                if (this.info == null) {
                    TLRPC$ChatFull loadChatInfo = getMessagesStorage().loadChatInfo(this.chatId, ChatObject.isChannel(this.currentChat), new CountDownLatch(1), false, false);
                    this.info = loadChatInfo;
                }
            }
            return false;
        }
        this.isPrivate = !this.isForcePublic && TextUtils.isEmpty(this.currentChat.username);
        if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
            z = true;
        }
        this.isChannel = z;
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        this.isSaveRestricted = tLRPC$Chat.noforwards;
        if ((this.isForcePublic && TextUtils.isEmpty(tLRPC$Chat.username)) || (this.isPrivate && this.currentChat.creator)) {
            TLRPC$TL_channels_checkUsername tLRPC$TL_channels_checkUsername = new TLRPC$TL_channels_checkUsername();
            tLRPC$TL_channels_checkUsername.username = "1";
            tLRPC$TL_channels_checkUsername.channel = new TLRPC$TL_inputChannelEmpty();
            getConnectionsManager().sendRequest(tLRPC$TL_channels_checkUsername, new ChatEditTypeActivity$$ExternalSyntheticLambda15(this));
        }
        if (this.isPrivate && this.info != null) {
            getMessagesController().loadFullChat(this.chatId, this.classGuid, true);
        }
        getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
        return super.onFragmentCreate();
    }

    public /* synthetic */ void lambda$onFragmentCreate$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatEditTypeActivity$$ExternalSyntheticLambda11(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$onFragmentCreate$0(TLRPC$TL_error tLRPC$TL_error) {
        boolean z = tLRPC$TL_error == null || !tLRPC$TL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH");
        this.canCreatePublic = z;
        if (z || !getUserConfig().isPremium()) {
            return;
        }
        loadAdminedChannels();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        TLRPC$ChatFull tLRPC$ChatFull;
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        TextSettingsCell textSettingsCell = this.textCell2;
        if (textSettingsCell != null && (tLRPC$ChatFull = this.info) != null) {
            if (tLRPC$ChatFull.stickerset != null) {
                textSettingsCell.setTextAndValue(LocaleController.getString("GroupStickers", 2131626104), this.info.stickerset.title, false);
            } else {
                textSettingsCell.setText(LocaleController.getString("GroupStickers", 2131626104), false);
            }
        }
        TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
        if (tLRPC$ChatFull2 != null) {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = tLRPC$ChatFull2.exported_invite;
            this.invite = tLRPC$TL_chatInviteExported;
            this.permanentLinkView.setLink(tLRPC$TL_chatInviteExported == null ? null : tLRPC$TL_chatInviteExported.link);
            this.permanentLinkView.loadUsers(this.invite, this.chatId);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        EditTextBoldCursor editTextBoldCursor;
        super.onBecomeFullyVisible();
        if (!this.isForcePublic || (editTextBoldCursor = this.usernameTextView) == null) {
            return;
        }
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(this.usernameTextView);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        String str;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, 2131165450, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131625525));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.fragmentView = anonymousClass2;
        anonymousClass2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        ScrollView scrollView = (ScrollView) this.fragmentView;
        scrollView.setFillViewport(true);
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout = linearLayout;
        scrollView.addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
        if (this.isForcePublic) {
            this.actionBar.setTitle(LocaleController.getString("TypeLocationGroup", 2131628709));
        } else if (this.isChannel) {
            this.actionBar.setTitle(LocaleController.getString("ChannelSettingsTitle", 2131624964));
        } else {
            this.actionBar.setTitle(LocaleController.getString("GroupSettingsTitle", 2131626103));
        }
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.linearLayoutTypeContainer = linearLayout2;
        linearLayout2.setOrientation(1);
        this.linearLayoutTypeContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout.addView(this.linearLayoutTypeContainer, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell = new HeaderCell(context, 23);
        this.headerCell2 = headerCell;
        headerCell.setHeight(46);
        if (this.isChannel) {
            this.headerCell2.setText(LocaleController.getString("ChannelTypeHeader", 2131624974));
        } else {
            this.headerCell2.setText(LocaleController.getString("GroupTypeHeader", 2131626107));
        }
        this.linearLayoutTypeContainer.addView(this.headerCell2);
        RadioButtonCell radioButtonCell = new RadioButtonCell(context);
        this.radioButtonCell2 = radioButtonCell;
        radioButtonCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131624946), LocaleController.getString("ChannelPrivateInfo", 2131624947), false, this.isPrivate);
        } else {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("MegaPrivate", 2131626586), LocaleController.getString("MegaPrivateInfo", 2131626587), false, this.isPrivate);
        }
        this.linearLayoutTypeContainer.addView(this.radioButtonCell2, LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell2.setOnClickListener(new ChatEditTypeActivity$$ExternalSyntheticLambda4(this));
        RadioButtonCell radioButtonCell2 = new RadioButtonCell(context);
        this.radioButtonCell1 = radioButtonCell2;
        radioButtonCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131624949), LocaleController.getString("ChannelPublicInfo", 2131624952), false, !this.isPrivate);
        } else {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("MegaPublic", 2131626589), LocaleController.getString("MegaPublicInfo", 2131626590), false, !this.isPrivate);
        }
        this.linearLayoutTypeContainer.addView(this.radioButtonCell1, LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell1.setOnClickListener(new ChatEditTypeActivity$$ExternalSyntheticLambda1(this));
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.sectionCell2 = shadowSectionCell;
        this.linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
        if (this.isForcePublic) {
            this.radioButtonCell2.setVisibility(8);
            this.radioButtonCell1.setVisibility(8);
            this.sectionCell2.setVisibility(8);
            this.headerCell2.setVisibility(8);
        }
        LinearLayout linearLayout3 = new LinearLayout(context);
        this.linkContainer = linearLayout3;
        linearLayout3.setOrientation(1);
        this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout.addView(this.linkContainer, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell2 = new HeaderCell(context, 23);
        this.headerCell = headerCell2;
        this.linkContainer.addView(headerCell2);
        LinearLayout linearLayout4 = new LinearLayout(context);
        this.publicContainer = linearLayout4;
        linearLayout4.setOrientation(0);
        this.linkContainer.addView(this.publicContainer, LayoutHelper.createLinear(-1, 36, 23.0f, 7.0f, 23.0f, 0.0f));
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        this.editText = editTextBoldCursor;
        editTextBoldCursor.setText(getMessagesController().linkPrefix + "/");
        this.editText.setTextSize(1, 18.0f);
        this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.editText.setMaxLines(1);
        this.editText.setLines(1);
        this.editText.setEnabled(false);
        this.editText.setBackgroundDrawable(null);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setSingleLine(true);
        this.editText.setInputType(163840);
        this.editText.setImeOptions(6);
        this.publicContainer.addView(this.editText, LayoutHelper.createLinear(-2, 36));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.usernameTextView = anonymousClass3;
        anonymousClass3.setTextSize(1, 18.0f);
        this.usernameTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usernameTextView.setMaxLines(1);
        this.usernameTextView.setLines(1);
        this.usernameTextView.setBackgroundDrawable(null);
        this.usernameTextView.setPadding(0, 0, 0, 0);
        this.usernameTextView.setSingleLine(true);
        this.usernameTextView.setInputType(163872);
        this.usernameTextView.setImeOptions(6);
        this.usernameTextView.setHint(LocaleController.getString("ChannelUsernamePlaceholder", 2131624982));
        this.usernameTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usernameTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.usernameTextView.setCursorWidth(1.5f);
        this.publicContainer.addView(this.usernameTextView, LayoutHelper.createLinear(-1, 36));
        this.usernameTextView.addTextChangedListener(new AnonymousClass4());
        LinearLayout linearLayout5 = new LinearLayout(context);
        this.privateContainer = linearLayout5;
        linearLayout5.setOrientation(1);
        this.linkContainer.addView(this.privateContainer, LayoutHelper.createLinear(-1, -2));
        LinkActionView linkActionView = new LinkActionView(context, this, null, this.chatId, true, ChatObject.isChannel(this.currentChat));
        this.permanentLinkView = linkActionView;
        linkActionView.setDelegate(new AnonymousClass5(context));
        this.permanentLinkView.setUsers(0, null);
        this.privateContainer.addView(this.permanentLinkView);
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.checkTextView = textInfoPrivacyCell;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.checkTextView.setBottomPadding(6);
        this.linearLayout.addView(this.checkTextView, LayoutHelper.createLinear(-2, -2));
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.typeInfoCell = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setImportantForAccessibility(1);
        this.linearLayout.addView(this.typeInfoCell, LayoutHelper.createLinear(-1, -2));
        LoadingCell loadingCell = new LoadingCell(context);
        this.loadingAdminedCell = loadingCell;
        this.linearLayout.addView(loadingCell, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout6 = new LinearLayout(context);
        this.adminnedChannelsLayout = linearLayout6;
        linearLayout6.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.adminnedChannelsLayout.setOrientation(1);
        this.linearLayout.addView(this.adminnedChannelsLayout, LayoutHelper.createLinear(-1, -2));
        ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
        this.adminedInfoCell = shadowSectionCell2;
        this.linearLayout.addView(shadowSectionCell2, LayoutHelper.createLinear(-1, -2));
        TextCell textCell = new TextCell(context);
        this.manageLinksTextView = textCell;
        textCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.manageLinksTextView.setTextAndIcon(LocaleController.getString("ManageInviteLinks", 2131626531), 2131165783, false);
        this.manageLinksTextView.setOnClickListener(new ChatEditTypeActivity$$ExternalSyntheticLambda3(this));
        this.linearLayout.addView(this.manageLinksTextView, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
        this.manageLinksInfoCell = textInfoPrivacyCell3;
        this.linearLayout.addView(textInfoPrivacyCell3, LayoutHelper.createLinear(-1, -2));
        JoinToSendSettingsView joinToSendSettingsView = new JoinToSendSettingsView(context, this.currentChat);
        this.joinContainer = joinToSendSettingsView;
        this.linearLayout.addView(joinToSendSettingsView);
        LinearLayout linearLayout7 = new LinearLayout(context);
        this.saveContainer = linearLayout7;
        linearLayout7.setOrientation(1);
        this.linearLayout.addView(this.saveContainer);
        HeaderCell headerCell3 = new HeaderCell(context, 23);
        this.saveHeaderCell = headerCell3;
        headerCell3.setHeight(46);
        this.saveHeaderCell.setText(LocaleController.getString("SavingContentTitle", 2131628079));
        this.saveHeaderCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.saveContainer.addView(this.saveHeaderCell, LayoutHelper.createLinear(-1, -2));
        TextCheckCell textCheckCell = new TextCheckCell(context);
        this.saveRestrictCell = textCheckCell;
        textCheckCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.saveRestrictCell.setTextAndCheck(LocaleController.getString("RestrictSavingContent", 2131628026), this.isSaveRestricted, false);
        this.saveRestrictCell.setOnClickListener(new ChatEditTypeActivity$$ExternalSyntheticLambda5(this));
        this.saveContainer.addView(this.saveRestrictCell, LayoutHelper.createLinear(-1, -2));
        this.saveRestrictInfoCell = new TextInfoPrivacyCell(context);
        if (this.isChannel && !ChatObject.isMegagroup(this.currentChat)) {
            this.saveRestrictInfoCell.setText(LocaleController.getString("RestrictSavingContentInfoChannel", 2131628027));
        } else {
            this.saveRestrictInfoCell.setText(LocaleController.getString("RestrictSavingContentInfoGroup", 2131628028));
        }
        this.saveContainer.addView(this.saveRestrictInfoCell, LayoutHelper.createLinear(-1, -2));
        if (!this.isPrivate && (str = this.currentChat.username) != null) {
            this.ignoreTextChanges = true;
            this.usernameTextView.setText(str);
            this.usernameTextView.setSelection(this.currentChat.username.length());
            this.ignoreTextChanges = false;
        }
        updatePrivatePublic();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChatEditTypeActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ChatEditTypeActivity.this.finishFragment();
            } else if (i != 1) {
            } else {
                ChatEditTypeActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ScrollView {
        AnonymousClass2(ChatEditTypeActivity chatEditTypeActivity, Context context) {
            super(context);
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            rect.bottom += AndroidUtilities.dp(60.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    public /* synthetic */ void lambda$createView$2(View view) {
        if (this.isPrivate) {
            return;
        }
        this.isPrivate = true;
        updatePrivatePublic();
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        if (!this.isPrivate) {
            return;
        }
        if (!this.canCreatePublic) {
            showPremiumIncreaseLimitDialog();
            return;
        }
        this.isPrivate = false;
        updatePrivatePublic();
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends EditTextBoldCursor {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ChatEditTypeActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            StringBuilder sb = new StringBuilder();
            sb.append((CharSequence) getText());
            if (ChatEditTypeActivity.this.checkTextView != null && ChatEditTypeActivity.this.checkTextView.getTextView() != null && !TextUtils.isEmpty(ChatEditTypeActivity.this.checkTextView.getTextView().getText())) {
                sb.append("\n");
                sb.append(ChatEditTypeActivity.this.checkTextView.getTextView().getText());
            }
            accessibilityNodeInfo.setText(sb);
        }
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass4() {
            ChatEditTypeActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (ChatEditTypeActivity.this.ignoreTextChanges) {
                return;
            }
            ChatEditTypeActivity chatEditTypeActivity = ChatEditTypeActivity.this;
            chatEditTypeActivity.checkUserName(chatEditTypeActivity.usernameTextView.getText().toString());
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            ChatEditTypeActivity.this.checkDoneButton();
        }
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 implements LinkActionView.Delegate {
        final /* synthetic */ Context val$context;

        @Override // org.telegram.ui.Components.LinkActionView.Delegate
        public /* synthetic */ void editLink() {
            LinkActionView.Delegate.CC.$default$editLink(this);
        }

        @Override // org.telegram.ui.Components.LinkActionView.Delegate
        public /* synthetic */ void removeLink() {
            LinkActionView.Delegate.CC.$default$removeLink(this);
        }

        AnonymousClass5(Context context) {
            ChatEditTypeActivity.this = r1;
            this.val$context = context;
        }

        @Override // org.telegram.ui.Components.LinkActionView.Delegate
        public void revokeLink() {
            ChatEditTypeActivity.this.generateLink(true);
        }

        @Override // org.telegram.ui.Components.LinkActionView.Delegate
        public void showUsersForPermanentLink() {
            ChatEditTypeActivity chatEditTypeActivity = ChatEditTypeActivity.this;
            Context context = this.val$context;
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = ChatEditTypeActivity.this.invite;
            TLRPC$ChatFull tLRPC$ChatFull = ChatEditTypeActivity.this.info;
            ChatEditTypeActivity chatEditTypeActivity2 = ChatEditTypeActivity.this;
            chatEditTypeActivity.inviteLinkBottomSheet = new InviteLinkBottomSheet(context, tLRPC$TL_chatInviteExported, tLRPC$ChatFull, chatEditTypeActivity2.usersMap, chatEditTypeActivity2, chatEditTypeActivity2.chatId, true, ChatObject.isChannel(ChatEditTypeActivity.this.currentChat));
            ChatEditTypeActivity.this.inviteLinkBottomSheet.show();
        }
    }

    public /* synthetic */ void lambda$createView$4(View view) {
        ManageLinksActivity manageLinksActivity = new ManageLinksActivity(this.chatId, 0L, 0);
        manageLinksActivity.setInfo(this.info, this.invite);
        presentFragment(manageLinksActivity);
    }

    public /* synthetic */ void lambda$createView$5(View view) {
        boolean z = !this.isSaveRestricted;
        this.isSaveRestricted = z;
        ((TextCheckCell) view).setChecked(z);
    }

    private void showPremiumIncreaseLimitDialog() {
        if (getParentActivity() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getParentActivity(), 2, this.currentAccount);
        limitReachedBottomSheet.parentIsChannel = this.isChannel;
        limitReachedBottomSheet.onSuccessRunnable = new ChatEditTypeActivity$$ExternalSyntheticLambda6(this);
        showDialog(limitReachedBottomSheet);
    }

    public /* synthetic */ void lambda$showPremiumIncreaseLimitDialog$6() {
        this.canCreatePublic = true;
        updatePrivatePublic();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            if (tLRPC$ChatFull.id != this.chatId) {
                return;
            }
            this.info = tLRPC$ChatFull;
            this.invite = tLRPC$ChatFull.exported_invite;
            updatePrivatePublic();
        }
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = tLRPC$ChatFull.exported_invite;
            if (tLRPC$TL_chatInviteExported != null) {
                this.invite = tLRPC$TL_chatInviteExported;
            } else {
                generateLink(false);
            }
        }
    }

    public void processDone() {
        if (this.currentChat.noforwards != this.isSaveRestricted) {
            MessagesController messagesController = getMessagesController();
            long j = this.chatId;
            TLRPC$Chat tLRPC$Chat = this.currentChat;
            boolean z = this.isSaveRestricted;
            tLRPC$Chat.noforwards = z;
            messagesController.toggleChatNoForwards(j, z);
        }
        if (!trySetUsername() || !tryUpdateJoinSettings()) {
            return;
        }
        finishFragment();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean tryUpdateJoinSettings() {
        boolean z;
        if (!this.isChannel && this.joinContainer != null) {
            if (getParentActivity() == null) {
                return false;
            }
            if (!ChatObject.isChannel(this.currentChat)) {
                JoinToSendSettingsView joinToSendSettingsView = this.joinContainer;
                if (joinToSendSettingsView.isJoinToSend || joinToSendSettingsView.isJoinRequest) {
                    z = true;
                    if (!z) {
                        getMessagesController().convertToMegaGroup(getParentActivity(), this.chatId, this, new ChatEditTypeActivity$$ExternalSyntheticLambda14(this));
                        return false;
                    }
                    if (this.currentChat.join_to_send != this.joinContainer.isJoinToSend) {
                        MessagesController messagesController = getMessagesController();
                        long j = this.chatId;
                        TLRPC$Chat tLRPC$Chat = this.currentChat;
                        boolean z2 = this.joinContainer.isJoinToSend;
                        tLRPC$Chat.join_to_send = z2;
                        messagesController.toggleChatJoinToSend(j, z2, null, null);
                    }
                    if (this.currentChat.join_request != this.joinContainer.isJoinRequest) {
                        MessagesController messagesController2 = getMessagesController();
                        long j2 = this.chatId;
                        TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                        boolean z3 = this.joinContainer.isJoinRequest;
                        tLRPC$Chat2.join_request = z3;
                        messagesController2.toggleChatJoinRequest(j2, z3, null, null);
                    }
                }
            }
            z = false;
            if (!z) {
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$tryUpdateJoinSettings$7(long j) {
        if (j != 0) {
            this.chatId = j;
            this.currentChat = getMessagesController().getChat(Long.valueOf(j));
            processDone();
        }
    }

    private boolean trySetUsername() {
        String str;
        if (getParentActivity() == null) {
            return false;
        }
        if (!this.isPrivate && (((this.currentChat.username == null && this.usernameTextView.length() != 0) || ((str = this.currentChat.username) != null && !str.equalsIgnoreCase(this.usernameTextView.getText().toString()))) && this.usernameTextView.length() != 0 && !this.lastNameAvailable)) {
            Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.checkTextView, 2.0f, 0);
            return false;
        }
        String str2 = this.currentChat.username;
        String str3 = "";
        if (str2 == null) {
            str2 = str3;
        }
        if (!this.isPrivate) {
            str3 = this.usernameTextView.getText().toString();
        }
        if (str2.equals(str3)) {
            return true;
        }
        if (!ChatObject.isChannel(this.currentChat)) {
            getMessagesController().convertToMegaGroup(getParentActivity(), this.chatId, this, new ChatEditTypeActivity$$ExternalSyntheticLambda13(this));
            return false;
        }
        getMessagesController().updateChannelUserName(this.chatId, str3);
        this.currentChat.username = str3;
        return true;
    }

    public /* synthetic */ void lambda$trySetUsername$8(long j) {
        if (j != 0) {
            this.chatId = j;
            this.currentChat = getMessagesController().getChat(Long.valueOf(j));
            processDone();
        }
    }

    private void loadAdminedChannels() {
        if (this.loadingAdminedChannels || this.adminnedChannelsLayout == null) {
            return;
        }
        this.loadingAdminedChannels = true;
        updatePrivatePublic();
        getConnectionsManager().sendRequest(new TLRPC$TL_channels_getAdminedPublicChannels(), new ChatEditTypeActivity$$ExternalSyntheticLambda17(this));
    }

    public /* synthetic */ void lambda$loadAdminedChannels$14(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatEditTypeActivity$$ExternalSyntheticLambda10(this, tLObject));
    }

    public /* synthetic */ void lambda$loadAdminedChannels$13(TLObject tLObject) {
        this.loadingAdminedChannels = false;
        if (tLObject == null || getParentActivity() == null) {
            return;
        }
        for (int i = 0; i < this.adminedChannelCells.size(); i++) {
            this.linearLayout.removeView(this.adminedChannelCells.get(i));
        }
        this.adminedChannelCells.clear();
        TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
        for (int i2 = 0; i2 < tLRPC$TL_messages_chats.chats.size(); i2++) {
            AdminedChannelCell adminedChannelCell = new AdminedChannelCell(getParentActivity(), new ChatEditTypeActivity$$ExternalSyntheticLambda2(this), false, 0);
            TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_chats.chats.get(i2);
            boolean z = true;
            if (i2 != tLRPC$TL_messages_chats.chats.size() - 1) {
                z = false;
            }
            adminedChannelCell.setChannel(tLRPC$Chat, z);
            this.adminedChannelCells.add(adminedChannelCell);
            this.adminnedChannelsLayout.addView(adminedChannelCell, LayoutHelper.createLinear(-1, 72));
        }
        updatePrivatePublic();
    }

    public /* synthetic */ void lambda$loadAdminedChannels$12(View view) {
        TLRPC$Chat currentChannel = ((AdminedChannelCell) view.getParent()).getCurrentChannel();
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        if (this.isChannel) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", 2131628045, getMessagesController().linkPrefix + "/" + currentChannel.username, currentChannel.title)));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", 2131628044, getMessagesController().linkPrefix + "/" + currentChannel.username, currentChannel.title)));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        builder.setPositiveButton(LocaleController.getString("RevokeButton", 2131628042), new ChatEditTypeActivity$$ExternalSyntheticLambda0(this, currentChannel));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$loadAdminedChannels$11(TLRPC$Chat tLRPC$Chat, DialogInterface dialogInterface, int i) {
        TLRPC$TL_channels_updateUsername tLRPC$TL_channels_updateUsername = new TLRPC$TL_channels_updateUsername();
        tLRPC$TL_channels_updateUsername.channel = MessagesController.getInputChannel(tLRPC$Chat);
        tLRPC$TL_channels_updateUsername.username = "";
        getConnectionsManager().sendRequest(tLRPC$TL_channels_updateUsername, new ChatEditTypeActivity$$ExternalSyntheticLambda16(this), 64);
    }

    public /* synthetic */ void lambda$loadAdminedChannels$10(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            AndroidUtilities.runOnUIThread(new ChatEditTypeActivity$$ExternalSyntheticLambda7(this));
        }
    }

    public /* synthetic */ void lambda$loadAdminedChannels$9() {
        this.canCreatePublic = true;
        if (this.usernameTextView.length() > 0) {
            checkUserName(this.usernameTextView.getText().toString());
        }
        updatePrivatePublic();
    }

    private void updatePrivatePublic() {
        String str;
        int i;
        String str2;
        int i2;
        if (this.sectionCell2 == null) {
            return;
        }
        Drawable drawable = null;
        int i3 = 8;
        if (!this.isPrivate && !this.canCreatePublic && getUserConfig().isPremium()) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131624863));
            this.typeInfoCell.setTag("windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.checkTextView.setVisibility(8);
            this.sectionCell2.setVisibility(8);
            this.adminedInfoCell.setVisibility(0);
            if (this.loadingAdminedChannels) {
                this.loadingAdminedCell.setVisibility(0);
                this.adminnedChannelsLayout.setVisibility(8);
                this.typeInfoCell.setBackgroundDrawable(this.checkTextView.getVisibility() == 0 ? null : Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165436, "windowBackgroundGrayShadow"));
                this.adminedInfoCell.setBackgroundDrawable(null);
            } else {
                ShadowSectionCell shadowSectionCell = this.adminedInfoCell;
                shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCell.getContext(), 2131165436, "windowBackgroundGrayShadow"));
                TextInfoPrivacyCell textInfoPrivacyCell = this.typeInfoCell;
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell.getContext(), 2131165437, "windowBackgroundGrayShadow"));
                this.loadingAdminedCell.setVisibility(8);
                this.adminnedChannelsLayout.setVisibility(0);
            }
        } else {
            this.typeInfoCell.setTag("windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            if (this.isForcePublic) {
                this.sectionCell2.setVisibility(8);
            } else {
                this.sectionCell2.setVisibility(0);
            }
            this.adminedInfoCell.setVisibility(8);
            TextInfoPrivacyCell textInfoPrivacyCell2 = this.typeInfoCell;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell2.getContext(), 2131165436, "windowBackgroundGrayShadow"));
            this.adminnedChannelsLayout.setVisibility(8);
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            if (this.isChannel) {
                TextInfoPrivacyCell textInfoPrivacyCell3 = this.typeInfoCell;
                if (this.isPrivate) {
                    i2 = 2131624948;
                    str2 = "ChannelPrivateLinkHelp";
                } else {
                    i2 = 2131624981;
                    str2 = "ChannelUsernameHelp";
                }
                textInfoPrivacyCell3.setText(LocaleController.getString(str2, i2));
                this.headerCell.setText(this.isPrivate ? LocaleController.getString("ChannelInviteLinkTitle", 2131624905) : LocaleController.getString("ChannelLinkTitle", 2131624915));
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell4 = this.typeInfoCell;
                if (this.isPrivate) {
                    i = 2131626588;
                    str = "MegaPrivateLinkHelp";
                } else {
                    i = 2131626591;
                    str = "MegaUsernameHelp";
                }
                textInfoPrivacyCell4.setText(LocaleController.getString(str, i));
                this.headerCell.setText(this.isPrivate ? LocaleController.getString("ChannelInviteLinkTitle", 2131624905) : LocaleController.getString("ChannelLinkTitle", 2131624915));
            }
            this.publicContainer.setVisibility(this.isPrivate ? 8 : 0);
            this.privateContainer.setVisibility(this.isPrivate ? 0 : 8);
            this.saveContainer.setVisibility(0);
            this.manageLinksTextView.setVisibility(0);
            this.manageLinksInfoCell.setVisibility(0);
            this.linkContainer.setPadding(0, 0, 0, this.isPrivate ? 0 : AndroidUtilities.dp(7.0f));
            LinkActionView linkActionView = this.permanentLinkView;
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = this.invite;
            linkActionView.setLink(tLRPC$TL_chatInviteExported != null ? tLRPC$TL_chatInviteExported.link : null);
            this.permanentLinkView.loadUsers(this.invite, this.chatId);
            TextInfoPrivacyCell textInfoPrivacyCell5 = this.checkTextView;
            textInfoPrivacyCell5.setVisibility((this.isPrivate || textInfoPrivacyCell5.length() == 0) ? 8 : 0);
            this.manageLinksInfoCell.setText(LocaleController.getString("ManageLinksInfoHelp", 2131626532));
            if (this.isPrivate) {
                TextInfoPrivacyCell textInfoPrivacyCell6 = this.typeInfoCell;
                textInfoPrivacyCell6.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell6.getContext(), 2131165435, "windowBackgroundGrayShadow"));
                this.manageLinksInfoCell.setBackground(Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165436, "windowBackgroundGrayShadow"));
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell7 = this.typeInfoCell;
                if (this.checkTextView.getVisibility() != 0) {
                    drawable = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165436, "windowBackgroundGrayShadow");
                }
                textInfoPrivacyCell7.setBackgroundDrawable(drawable);
            }
        }
        this.radioButtonCell1.setChecked(!this.isPrivate, true);
        this.radioButtonCell2.setChecked(this.isPrivate, true);
        this.usernameTextView.clearFocus();
        JoinToSendSettingsView joinToSendSettingsView = this.joinContainer;
        if (joinToSendSettingsView != null) {
            if (!this.isChannel && !this.isPrivate) {
                i3 = 0;
            }
            joinToSendSettingsView.setVisibility(i3);
        }
        checkDoneButton();
    }

    public void checkDoneButton() {
        if (this.isPrivate || this.usernameTextView.length() > 0) {
            this.doneButton.setEnabled(true);
            this.doneButton.setAlpha(1.0f);
            return;
        }
        this.doneButton.setEnabled(false);
        this.doneButton.setAlpha(0.5f);
    }

    public boolean checkUserName(String str) {
        if (str != null && str.length() > 0) {
            this.checkTextView.setVisibility(0);
        } else {
            this.checkTextView.setVisibility(8);
        }
        this.typeInfoCell.setBackgroundDrawable(this.checkTextView.getVisibility() == 0 ? null : Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165436, "windowBackgroundGrayShadow"));
        Runnable runnable = this.checkRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
                getConnectionsManager().cancelRequest(this.checkReqId, true);
            }
        }
        this.lastNameAvailable = false;
        if (str != null) {
            if (str.startsWith("_") || str.endsWith("_")) {
                this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131626444));
                this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                return false;
            }
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (i == 0 && charAt >= '0' && charAt <= '9') {
                    if (this.isChannel) {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", 2131626448));
                    } else {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumberMega", 2131626449));
                    }
                    this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                    return false;
                } else if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && charAt != '_'))) {
                    this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131626444));
                    this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                    return false;
                }
            }
        }
        if (str == null || str.length() < 5) {
            if (this.isChannel) {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", 2131626446));
            } else {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShortMega", 2131626447));
            }
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
        } else if (str.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", 2131626445));
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
        } else {
            this.checkTextView.setText(LocaleController.getString("LinkChecking", 2131626432));
            this.checkTextView.setTextColor("windowBackgroundWhiteGrayText8");
            this.lastCheckName = str;
            ChatEditTypeActivity$$ExternalSyntheticLambda8 chatEditTypeActivity$$ExternalSyntheticLambda8 = new ChatEditTypeActivity$$ExternalSyntheticLambda8(this, str);
            this.checkRunnable = chatEditTypeActivity$$ExternalSyntheticLambda8;
            AndroidUtilities.runOnUIThread(chatEditTypeActivity$$ExternalSyntheticLambda8, 300L);
            return true;
        }
    }

    public /* synthetic */ void lambda$checkUserName$17(String str) {
        TLRPC$TL_channels_checkUsername tLRPC$TL_channels_checkUsername = new TLRPC$TL_channels_checkUsername();
        tLRPC$TL_channels_checkUsername.username = str;
        tLRPC$TL_channels_checkUsername.channel = getMessagesController().getInputChannel(this.chatId);
        this.checkReqId = getConnectionsManager().sendRequest(tLRPC$TL_channels_checkUsername, new ChatEditTypeActivity$$ExternalSyntheticLambda18(this, str), 2);
    }

    public /* synthetic */ void lambda$checkUserName$16(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatEditTypeActivity$$ExternalSyntheticLambda9(this, str, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$checkUserName$15(String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.checkReqId = 0;
        String str2 = this.lastCheckName;
        if (str2 == null || !str2.equals(str)) {
            return;
        }
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            this.checkTextView.setText(LocaleController.formatString("LinkAvailable", 2131626431, str));
            this.checkTextView.setTextColor("windowBackgroundWhiteGreenText");
            this.lastNameAvailable = true;
            return;
        }
        if (tLRPC$TL_error != null && tLRPC$TL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
            this.canCreatePublic = false;
            showPremiumIncreaseLimitDialog();
        } else {
            this.checkTextView.setText(LocaleController.getString("LinkInUse", 2131626441));
        }
        this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
        this.lastNameAvailable = false;
    }

    public void generateLink(boolean z) {
        TLRPC$TL_messages_exportChatInvite tLRPC$TL_messages_exportChatInvite = new TLRPC$TL_messages_exportChatInvite();
        tLRPC$TL_messages_exportChatInvite.legacy_revoke_permanent = true;
        tLRPC$TL_messages_exportChatInvite.peer = getMessagesController().getInputPeer(-this.chatId);
        getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_messages_exportChatInvite, new ChatEditTypeActivity$$ExternalSyntheticLambda19(this, z)), this.classGuid);
    }

    public /* synthetic */ void lambda$generateLink$19(boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatEditTypeActivity$$ExternalSyntheticLambda12(this, tLRPC$TL_error, tLObject, z));
    }

    public /* synthetic */ void lambda$generateLink$18(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
        String str = null;
        if (tLRPC$TL_error == null) {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = (TLRPC$TL_chatInviteExported) tLObject;
            this.invite = tLRPC$TL_chatInviteExported;
            TLRPC$ChatFull tLRPC$ChatFull = this.info;
            if (tLRPC$ChatFull != null) {
                tLRPC$ChatFull.exported_invite = tLRPC$TL_chatInviteExported;
            }
            if (z) {
                if (getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setMessage(LocaleController.getString("RevokeAlertNewLink", 2131628041));
                builder.setTitle(LocaleController.getString("RevokeLink", 2131628043));
                builder.setNegativeButton(LocaleController.getString("OK", 2131627075), null);
                showDialog(builder.create());
            }
        }
        LinkActionView linkActionView = this.permanentLinkView;
        if (linkActionView != null) {
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported2 = this.invite;
            if (tLRPC$TL_chatInviteExported2 != null) {
                str = tLRPC$TL_chatInviteExported2.link;
            }
            linkActionView.setLink(str);
            this.permanentLinkView.loadUsers(this.invite, this.chatId);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChatEditTypeActivity$$ExternalSyntheticLambda20 chatEditTypeActivity$$ExternalSyntheticLambda20 = new ChatEditTypeActivity$$ExternalSyntheticLambda20(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.sectionCell2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.infoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.infoCell, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.textCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.textCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.textCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.textCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.linearLayoutTypeContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linkContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.headerCell2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.saveHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.saveRestrictCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.saveRestrictCell, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.saveRestrictCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.saveRestrictCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText8"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGreenText"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.manageLinksInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.manageLinksInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.manageLinksInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.saveRestrictInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.saveRestrictInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.saveRestrictInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.loadingAdminedCell, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackground"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackground"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"deleteButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditTypeActivity$$ExternalSyntheticLambda20, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.manageLinksTextView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.manageLinksTextView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.manageLinksTextView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$20() {
        LinearLayout linearLayout = this.adminnedChannelsLayout;
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.adminnedChannelsLayout.getChildAt(i);
                if (childAt instanceof AdminedChannelCell) {
                    ((AdminedChannelCell) childAt).update();
                }
            }
        }
        this.permanentLinkView.updateColors();
        this.manageLinksTextView.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        InviteLinkBottomSheet inviteLinkBottomSheet = this.inviteLinkBottomSheet;
        if (inviteLinkBottomSheet != null) {
            inviteLinkBottomSheet.updateColors();
        }
    }
}
