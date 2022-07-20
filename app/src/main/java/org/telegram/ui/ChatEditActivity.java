package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_chatParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_exportedChatInvites;
import org.telegram.tgnet.TLRPC$TL_messages_getExportedChatInvites;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class ChatEditActivity extends BaseFragment implements ImageUpdater.ImageUpdaterDelegate, NotificationCenter.NotificationCenterDelegate {
    private TextCell adminCell;
    private TLRPC$FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private LinearLayout avatarContainer;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private TextCell blockCell;
    RLottieDrawable cameraDrawable;
    private long chatId;
    private boolean createAfterUpload;
    private TLRPC$Chat currentChat;
    private TextSettingsCell deleteCell;
    private FrameLayout deleteContainer;
    private ShadowSectionCell deleteInfoCell;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private boolean donePressed;
    private TextDetailCell historyCell;
    private boolean historyHidden;
    private TLRPC$ChatFull info;
    private LinearLayout infoContainer;
    private ShadowSectionCell infoSectionCell;
    private TextCell inviteLinksCell;
    private boolean isChannel;
    private TextDetailCell linkedCell;
    private TextDetailCell locationCell;
    private TextCell logCell;
    private TextCell memberRequestsCell;
    private TextCell membersCell;
    private EditTextEmoji nameTextView;
    private AlertDialog progressDialog;
    private TextCell reactionsCell;
    private TextCell setAvatarCell;
    private LinearLayout settingsContainer;
    private ShadowSectionCell settingsSectionCell;
    private ShadowSectionCell settingsTopSectionCell;
    private TextCheckCell signCell;
    private boolean signMessages;
    private TextCell stickersCell;
    private FrameLayout stickersContainer;
    private TextInfoPrivacyCell stickersInfoCell;
    private TextDetailCell typeCell;
    private LinearLayout typeEditContainer;
    private UndoView undoView;
    private List<String> availableReactions = Collections.emptyList();
    private PhotoViewer.PhotoViewerProvider provider = new AnonymousClass1();
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageUpdater imageUpdater = new ImageUpdater(true);

    public static /* synthetic */ boolean lambda$createView$2(View view, MotionEvent motionEvent) {
        return true;
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends PhotoViewer.EmptyPhotoViewerProvider {
        AnonymousClass1() {
            ChatEditActivity.this = r1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            TLRPC$FileLocation tLRPC$FileLocation2;
            TLRPC$ChatPhoto tLRPC$ChatPhoto;
            if (tLRPC$FileLocation == null) {
                return null;
            }
            TLRPC$Chat chat = ChatEditActivity.this.getMessagesController().getChat(Long.valueOf(ChatEditActivity.this.chatId));
            if (chat == null || (tLRPC$ChatPhoto = chat.photo) == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_big) == null) {
                tLRPC$FileLocation2 = null;
            }
            if (tLRPC$FileLocation2 == null || tLRPC$FileLocation2.local_id != tLRPC$FileLocation.local_id || tLRPC$FileLocation2.volume_id != tLRPC$FileLocation.volume_id || tLRPC$FileLocation2.dc_id != tLRPC$FileLocation.dc_id) {
                return null;
            }
            int[] iArr = new int[2];
            ChatEditActivity.this.avatarImage.getLocationInWindow(iArr);
            PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
            int i2 = 0;
            placeProviderObject.viewX = iArr[0];
            int i3 = iArr[1];
            if (Build.VERSION.SDK_INT < 21) {
                i2 = AndroidUtilities.statusBarHeight;
            }
            placeProviderObject.viewY = i3 - i2;
            placeProviderObject.parentView = ChatEditActivity.this.avatarImage;
            placeProviderObject.imageReceiver = ChatEditActivity.this.avatarImage.getImageReceiver();
            placeProviderObject.dialogId = -ChatEditActivity.this.chatId;
            placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
            placeProviderObject.size = -1L;
            placeProviderObject.radius = ChatEditActivity.this.avatarImage.getImageReceiver().getRoundRadius();
            placeProviderObject.scale = ChatEditActivity.this.avatarContainer.getScaleX();
            placeProviderObject.canEdit = true;
            return placeProviderObject;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void willHidePhotoViewer() {
            ChatEditActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void openPhotoForEdit(String str, String str2, boolean z) {
            ChatEditActivity.this.imageUpdater.openPhotoForEdit(str, str2, 0, z);
        }
    }

    public ChatEditActivity(Bundle bundle) {
        super(bundle);
        this.chatId = bundle.getLong("chat_id", 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
        if (r0 == null) goto L10;
     */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        boolean z = true;
        if (chat == null) {
            TLRPC$Chat chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(this.chatId);
            this.currentChat = chatSync;
            if (chatSync != null) {
                getMessagesController().putChat(this.currentChat, true);
                if (this.info == null) {
                    TLRPC$ChatFull loadChatInfo = MessagesStorage.getInstance(this.currentAccount).loadChatInfo(this.chatId, ChatObject.isChannel(this.currentChat), new CountDownLatch(1), false, false);
                    this.info = loadChatInfo;
                }
            }
            return false;
        }
        this.avatarDrawable.setInfo(5L, this.currentChat.title, null);
        if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
            z = false;
        }
        this.isChannel = z;
        ImageUpdater imageUpdater = this.imageUpdater;
        imageUpdater.parentFragment = this;
        imageUpdater.setDelegate(this);
        this.signMessages = this.currentChat.signatures;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatAvailableReactionsUpdated);
        if (this.info != null) {
            loadLinksCount();
        }
        return super.onFragmentCreate();
    }

    private void loadLinksCount() {
        TLRPC$TL_messages_getExportedChatInvites tLRPC$TL_messages_getExportedChatInvites = new TLRPC$TL_messages_getExportedChatInvites();
        tLRPC$TL_messages_getExportedChatInvites.peer = getMessagesController().getInputPeer(-this.chatId);
        tLRPC$TL_messages_getExportedChatInvites.admin_id = getMessagesController().getInputUser(getUserConfig().getCurrentUser());
        tLRPC$TL_messages_getExportedChatInvites.limit = 0;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getExportedChatInvites, new ChatEditActivity$$ExternalSyntheticLambda29(this));
    }

    public /* synthetic */ void lambda$loadLinksCount$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatEditActivity$$ExternalSyntheticLambda26(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$loadLinksCount$0(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            this.info.invitesCount = ((TLRPC$TL_messages_exportedChatInvites) tLObject).count;
            getMessagesStorage().saveChatLinksCount(this.chatId, this.info.invitesCount);
            updateFields(false);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatAvailableReactionsUpdated);
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onResume();
            this.nameTextView.getEditText().requestFocus();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        updateFields(true);
        this.imageUpdater.onResume();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onPause();
        }
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        this.imageUpdater.onPause();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void dismissCurrentDialog() {
        if (this.imageUpdater.dismissCurrentDialog(this.visibleDialog)) {
            return;
        }
        super.dismissCurrentDialog();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        return this.imageUpdater.dismissDialogOnPause(dialog) && super.dismissDialogOnPause(dialog);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        this.imageUpdater.onRequestPermissionsResultFragment(i, strArr, iArr);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null && editTextEmoji.isPopupShowing()) {
            this.nameTextView.hidePopup(true);
            return false;
        }
        return checkDiscard();
    }

    /* JADX WARN: Removed duplicated region for block: B:144:0x05c5  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0638  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0672  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x06ec  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x06fd  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x070b  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x074a  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x07cb  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0811  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        TextCell textCell;
        TextInfoPrivacyCell textInfoPrivacyCell;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TextCell textCell2;
        TLRPC$ChatFull tLRPC$ChatFull3;
        TLRPC$ChatFull tLRPC$ChatFull4;
        TLRPC$ChatFull tLRPC$ChatFull5;
        TLRPC$ChatFull tLRPC$ChatFull6;
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass2());
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        anonymousClass3.setOnTouchListener(ChatEditActivity$$ExternalSyntheticLambda22.INSTANCE);
        this.fragmentView = anonymousClass3;
        anonymousClass3.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        anonymousClass3.addView(scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        scrollView.addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        this.actionBar.setTitle(LocaleController.getString("ChannelEdit", 2131624903));
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.avatarContainer = linearLayout2;
        linearLayout2.setOrientation(1);
        this.avatarContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.avatarContainer, LayoutHelper.createLinear(-1, -2));
        FrameLayout frameLayout = new FrameLayout(context);
        this.avatarContainer.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
        this.avatarImage = anonymousClass4;
        anonymousClass4.setRoundRadius(AndroidUtilities.dp(32.0f));
        int i2 = 5;
        float f = 0.0f;
        if (ChatObject.canChangeChatInfo(this.currentChat)) {
            BackupImageView backupImageView = this.avatarImage;
            boolean z = LocaleController.isRTL;
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(64, 64.0f, (z ? 5 : 3) | 48, z ? 0.0f : 16.0f, 12.0f, z ? 16.0f : 0.0f, 8.0f));
            Paint paint = new Paint(1);
            paint.setColor(1426063360);
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, paint);
            this.avatarOverlay = anonymousClass5;
            boolean z2 = LocaleController.isRTL;
            frameLayout.addView(anonymousClass5, LayoutHelper.createFrame(64, 64.0f, (z2 ? 5 : 3) | 48, z2 ? 0.0f : 16.0f, 12.0f, z2 ? 16.0f : 0.0f, 8.0f));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.avatarProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            this.avatarProgressView.setNoProgress(false);
            RadialProgressView radialProgressView2 = this.avatarProgressView;
            boolean z3 = LocaleController.isRTL;
            int i3 = (z3 ? 5 : 3) | 48;
            float f2 = z3 ? 0.0f : 16.0f;
            if (z3) {
                f = 16.0f;
            }
            frameLayout.addView(radialProgressView2, LayoutHelper.createFrame(64, 64.0f, i3, f2, 12.0f, f, 8.0f));
            showAvatarProgress(false, false);
            this.avatarContainer.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda18(this));
        } else {
            BackupImageView backupImageView2 = this.avatarImage;
            boolean z4 = LocaleController.isRTL;
            int i4 = (z4 ? 5 : 3) | 48;
            float f3 = z4 ? 0.0f : 16.0f;
            if (z4) {
                f = 16.0f;
            }
            frameLayout.addView(backupImageView2, LayoutHelper.createFrame(64, 64.0f, i4, f3, 12.0f, f, 12.0f));
        }
        EditTextEmoji editTextEmoji2 = new EditTextEmoji(context, anonymousClass3, this, 0);
        this.nameTextView = editTextEmoji2;
        if (this.isChannel) {
            editTextEmoji2.setHint(LocaleController.getString("EnterChannelName", 2131625644));
        } else {
            editTextEmoji2.setHint(LocaleController.getString("GroupName", 2131626099));
        }
        this.nameTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        EditTextEmoji editTextEmoji3 = this.nameTextView;
        editTextEmoji3.setFocusable(editTextEmoji3.isEnabled());
        this.nameTextView.getEditText().addTextChangedListener(new AnonymousClass6());
        this.nameTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(128)});
        EditTextEmoji editTextEmoji4 = this.nameTextView;
        boolean z5 = LocaleController.isRTL;
        frameLayout.addView(editTextEmoji4, LayoutHelper.createFrame(-1, -2.0f, 16, z5 ? 5.0f : 96.0f, 0.0f, z5 ? 96.0f : 5.0f, 0.0f));
        LinearLayout linearLayout3 = new LinearLayout(context);
        this.settingsContainer = linearLayout3;
        linearLayout3.setOrientation(1);
        this.settingsContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.settingsContainer, LayoutHelper.createLinear(-1, -2));
        if (ChatObject.canChangeChatInfo(this.currentChat)) {
            AnonymousClass7 anonymousClass7 = new AnonymousClass7(this, context);
            this.setAvatarCell = anonymousClass7;
            anonymousClass7.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.setAvatarCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
            this.setAvatarCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda12(this));
            this.settingsContainer.addView(this.setAvatarCell, LayoutHelper.createLinear(-1, -2));
        }
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        this.descriptionTextView = editTextBoldCursor;
        editTextBoldCursor.setTextSize(1, 16.0f);
        this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.descriptionTextView.setBackgroundDrawable(null);
        EditTextBoldCursor editTextBoldCursor2 = this.descriptionTextView;
        if (!LocaleController.isRTL) {
            i2 = 3;
        }
        editTextBoldCursor2.setGravity(i2);
        this.descriptionTextView.setInputType(180225);
        this.descriptionTextView.setImeOptions(6);
        this.descriptionTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        EditTextBoldCursor editTextBoldCursor3 = this.descriptionTextView;
        editTextBoldCursor3.setFocusable(editTextBoldCursor3.isEnabled());
        this.descriptionTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        this.descriptionTextView.setHint(LocaleController.getString("DescriptionOptionalPlaceholder", 2131625462));
        this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.descriptionTextView.setCursorWidth(1.5f);
        if (this.descriptionTextView.isEnabled()) {
            this.settingsContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 23.0f, 15.0f, 23.0f, 9.0f));
        } else {
            this.settingsContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 23.0f, 12.0f, 23.0f, 6.0f));
        }
        this.descriptionTextView.setOnEditorActionListener(new ChatEditActivity$$ExternalSyntheticLambda23(this));
        this.descriptionTextView.addTextChangedListener(new AnonymousClass8(this));
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.settingsTopSectionCell = shadowSectionCell;
        linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout4 = new LinearLayout(context);
        this.typeEditContainer = linearLayout4;
        linearLayout4.setOrientation(1);
        this.typeEditContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.typeEditContainer, LayoutHelper.createLinear(-1, -2));
        if (this.currentChat.megagroup && ((tLRPC$ChatFull6 = this.info) == null || tLRPC$ChatFull6.can_set_location)) {
            TextDetailCell textDetailCell = new TextDetailCell(context);
            this.locationCell = textDetailCell;
            textDetailCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.locationCell, LayoutHelper.createLinear(-1, -2));
            this.locationCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda11(this));
        }
        if (this.currentChat.creator && ((tLRPC$ChatFull5 = this.info) == null || tLRPC$ChatFull5.can_set_username)) {
            TextDetailCell textDetailCell2 = new TextDetailCell(context);
            this.typeCell = textDetailCell2;
            textDetailCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.typeCell, LayoutHelper.createLinear(-1, -2));
            this.typeCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda6(this));
        }
        if (ChatObject.isChannel(this.currentChat) && ((this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 1)) || (!this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 0)))) {
            TextDetailCell textDetailCell3 = new TextDetailCell(context);
            this.linkedCell = textDetailCell3;
            textDetailCell3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.linkedCell, LayoutHelper.createLinear(-1, -2));
            this.linkedCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda7(this));
        }
        if (!this.isChannel && ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
            TextDetailCell textDetailCell4 = new TextDetailCell(context);
            this.historyCell = textDetailCell4;
            textDetailCell4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.historyCell, LayoutHelper.createLinear(-1, -2));
            this.historyCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda20(this, context));
        }
        if (this.isChannel) {
            TextCheckCell textCheckCell = new TextCheckCell(context);
            this.signCell = textCheckCell;
            textCheckCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.signCell.setTextAndValueAndCheck(LocaleController.getString("ChannelSignMessages", 2131624965), LocaleController.getString("ChannelSignMessagesInfo", 2131624966), this.signMessages, true, false);
            this.typeEditContainer.addView(this.signCell, LayoutHelper.createFrame(-1, -2.0f));
            this.signCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda17(this));
        }
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (ChatObject.canChangeChatInfo(this.currentChat) || this.signCell != null || this.historyCell != null) {
            ActionBarMenuItem addItemWithWidth = createMenu.addItemWithWidth(1, 2131165450, AndroidUtilities.dp(56.0f));
            this.doneButton = addItemWithWidth;
            addItemWithWidth.setContentDescription(LocaleController.getString("Done", 2131625525));
        }
        if (this.locationCell != null || this.signCell != null || this.historyCell != null || this.typeCell != null || this.linkedCell != null) {
            ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
            this.settingsSectionCell = shadowSectionCell2;
            linearLayout.addView(shadowSectionCell2, LayoutHelper.createLinear(-1, -2));
        }
        LinearLayout linearLayout5 = new LinearLayout(context);
        this.infoContainer = linearLayout5;
        linearLayout5.setOrientation(1);
        this.infoContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.infoContainer, LayoutHelper.createLinear(-1, -2));
        TextCell textCell3 = new TextCell(context);
        this.blockCell = textCell3;
        textCell3.setBackground(Theme.getSelectorDrawable(false));
        TextCell textCell4 = this.blockCell;
        if (!ChatObject.isChannel(this.currentChat)) {
            TLRPC$Chat tLRPC$Chat = this.currentChat;
            if (!tLRPC$Chat.creator && (!ChatObject.hasAdminRights(tLRPC$Chat) || !ChatObject.canChangeChatInfo(this.currentChat))) {
                i = 8;
                textCell4.setVisibility(i);
                this.blockCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda10(this));
                TextCell textCell5 = new TextCell(context);
                this.inviteLinksCell = textCell5;
                textCell5.setBackground(Theme.getSelectorDrawable(false));
                this.inviteLinksCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda4(this));
                TextCell textCell6 = new TextCell(context);
                this.reactionsCell = textCell6;
                textCell6.setBackground(Theme.getSelectorDrawable(false));
                this.reactionsCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda8(this));
                TextCell textCell7 = new TextCell(context);
                this.adminCell = textCell7;
                textCell7.setBackground(Theme.getSelectorDrawable(false));
                this.adminCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda13(this));
                TextCell textCell8 = new TextCell(context);
                this.membersCell = textCell8;
                textCell8.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                this.membersCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda19(this));
                if (!ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
                    TextCell textCell9 = new TextCell(context);
                    this.memberRequestsCell = textCell9;
                    textCell9.setBackground(Theme.getSelectorDrawable(false));
                    this.memberRequestsCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda16(this));
                }
                if (!ChatObject.isChannel(this.currentChat) || this.currentChat.gigagroup) {
                    TextCell textCell10 = new TextCell(context);
                    this.logCell = textCell10;
                    textCell10.setTextAndIcon(LocaleController.getString("EventLog", 2131625667), 2131165791, false);
                    this.logCell.setBackground(Theme.getSelectorDrawable(false));
                    this.logCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda9(this));
                }
                this.infoContainer.addView(this.reactionsCell, LayoutHelper.createLinear(-1, -2));
                if (!this.isChannel && !this.currentChat.gigagroup) {
                    this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
                }
                if (!this.isChannel) {
                    this.infoContainer.addView(this.inviteLinksCell, LayoutHelper.createLinear(-1, -2));
                }
                this.infoContainer.addView(this.adminCell, LayoutHelper.createLinear(-1, -2));
                this.infoContainer.addView(this.membersCell, LayoutHelper.createLinear(-1, -2));
                textCell = this.memberRequestsCell;
                if (textCell != null && (tLRPC$ChatFull4 = this.info) != null && tLRPC$ChatFull4.requests_pending > 0) {
                    this.infoContainer.addView(textCell, LayoutHelper.createLinear(-1, -2));
                }
                if (this.isChannel) {
                    this.infoContainer.addView(this.inviteLinksCell, LayoutHelper.createLinear(-1, -2));
                }
                if (!this.isChannel || this.currentChat.gigagroup) {
                    this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
                }
                if (this.isChannel && (tLRPC$ChatFull3 = this.info) != null && tLRPC$ChatFull3.can_set_stickers) {
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    this.stickersContainer = frameLayout2;
                    frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    linearLayout.addView(this.stickersContainer, LayoutHelper.createLinear(-1, -2));
                    TextCell textCell11 = new TextCell(context);
                    this.stickersCell = textCell11;
                    textCell11.setBackground(Theme.getSelectorDrawable(false));
                    this.stickersCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda5(this));
                    this.stickersCell.setPrioritizeTitleOverValue(true);
                    this.stickersContainer.addView(this.stickersCell, LayoutHelper.createFrame(-1, -2.0f));
                    this.stickersCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda15(this));
                } else {
                    textCell2 = this.logCell;
                    if (textCell2 != null) {
                        this.infoContainer.addView(textCell2, LayoutHelper.createLinear(-1, -2));
                    }
                }
                if (!ChatObject.hasAdminRights(this.currentChat)) {
                    this.infoContainer.setVisibility(8);
                    this.settingsTopSectionCell.setVisibility(8);
                }
                if (this.stickersCell == null) {
                    ShadowSectionCell shadowSectionCell3 = new ShadowSectionCell(context);
                    this.infoSectionCell = shadowSectionCell3;
                    linearLayout.addView(shadowSectionCell3, LayoutHelper.createLinear(-1, -2));
                }
                if (!this.isChannel && (tLRPC$ChatFull2 = this.info) != null && tLRPC$ChatFull2.can_set_stickers) {
                    TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
                    this.stickersInfoCell = textInfoPrivacyCell2;
                    textInfoPrivacyCell2.setText(LocaleController.getString(2131626105));
                    linearLayout.addView(this.stickersInfoCell, LayoutHelper.createLinear(-1, -2));
                }
                if (this.currentChat.creator) {
                    FrameLayout frameLayout3 = new FrameLayout(context);
                    this.deleteContainer = frameLayout3;
                    frameLayout3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    linearLayout.addView(this.deleteContainer, LayoutHelper.createLinear(-1, -2));
                    TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                    this.deleteCell = textSettingsCell;
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
                    this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    if (this.isChannel) {
                        this.deleteCell.setText(LocaleController.getString("ChannelDelete", 2131624895), false);
                    } else {
                        this.deleteCell.setText(LocaleController.getString("DeleteAndExitButton", 2131625383), false);
                    }
                    this.deleteContainer.addView(this.deleteCell, LayoutHelper.createFrame(-1, -2.0f));
                    this.deleteCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda14(this));
                    ShadowSectionCell shadowSectionCell4 = new ShadowSectionCell(context);
                    this.deleteInfoCell = shadowSectionCell4;
                    shadowSectionCell4.setBackground(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
                    linearLayout.addView(this.deleteInfoCell, LayoutHelper.createLinear(-1, -2));
                }
                textInfoPrivacyCell = this.stickersInfoCell;
                if (textInfoPrivacyCell != null) {
                    if (this.deleteInfoCell == null) {
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
                    } else {
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
                    }
                }
                UndoView undoView = new UndoView(context);
                this.undoView = undoView;
                anonymousClass3.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                this.nameTextView.setText(this.currentChat.title);
                EditTextEmoji editTextEmoji5 = this.nameTextView;
                editTextEmoji5.setSelection(editTextEmoji5.length());
                tLRPC$ChatFull = this.info;
                if (tLRPC$ChatFull != null) {
                    this.descriptionTextView.setText(tLRPC$ChatFull.about);
                }
                setAvatar();
                updateFields(true);
                return this.fragmentView;
            }
        }
        i = 0;
        textCell4.setVisibility(i);
        this.blockCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda10(this));
        TextCell textCell52 = new TextCell(context);
        this.inviteLinksCell = textCell52;
        textCell52.setBackground(Theme.getSelectorDrawable(false));
        this.inviteLinksCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda4(this));
        TextCell textCell62 = new TextCell(context);
        this.reactionsCell = textCell62;
        textCell62.setBackground(Theme.getSelectorDrawable(false));
        this.reactionsCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda8(this));
        TextCell textCell72 = new TextCell(context);
        this.adminCell = textCell72;
        textCell72.setBackground(Theme.getSelectorDrawable(false));
        this.adminCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda13(this));
        TextCell textCell82 = new TextCell(context);
        this.membersCell = textCell82;
        textCell82.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.membersCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda19(this));
        if (!ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
        }
        if (!ChatObject.isChannel(this.currentChat)) {
        }
        TextCell textCell102 = new TextCell(context);
        this.logCell = textCell102;
        textCell102.setTextAndIcon(LocaleController.getString("EventLog", 2131625667), 2131165791, false);
        this.logCell.setBackground(Theme.getSelectorDrawable(false));
        this.logCell.setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda9(this));
        this.infoContainer.addView(this.reactionsCell, LayoutHelper.createLinear(-1, -2));
        if (!this.isChannel) {
            this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
        }
        if (!this.isChannel) {
        }
        this.infoContainer.addView(this.adminCell, LayoutHelper.createLinear(-1, -2));
        this.infoContainer.addView(this.membersCell, LayoutHelper.createLinear(-1, -2));
        textCell = this.memberRequestsCell;
        if (textCell != null) {
            this.infoContainer.addView(textCell, LayoutHelper.createLinear(-1, -2));
        }
        if (this.isChannel) {
        }
        if (!this.isChannel) {
        }
        this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
        if (this.isChannel) {
        }
        textCell2 = this.logCell;
        if (textCell2 != null) {
        }
        if (!ChatObject.hasAdminRights(this.currentChat)) {
        }
        if (this.stickersCell == null) {
        }
        if (!this.isChannel) {
            TextInfoPrivacyCell textInfoPrivacyCell22 = new TextInfoPrivacyCell(context);
            this.stickersInfoCell = textInfoPrivacyCell22;
            textInfoPrivacyCell22.setText(LocaleController.getString(2131626105));
            linearLayout.addView(this.stickersInfoCell, LayoutHelper.createLinear(-1, -2));
        }
        if (this.currentChat.creator) {
        }
        textInfoPrivacyCell = this.stickersInfoCell;
        if (textInfoPrivacyCell != null) {
        }
        UndoView undoView2 = new UndoView(context);
        this.undoView = undoView2;
        anonymousClass3.addView(undoView2, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        this.nameTextView.setText(this.currentChat.title);
        EditTextEmoji editTextEmoji52 = this.nameTextView;
        editTextEmoji52.setSelection(editTextEmoji52.length());
        tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
        }
        setAvatar();
        updateFields(true);
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass2() {
            ChatEditActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!ChatEditActivity.this.checkDiscard()) {
                    return;
                }
                ChatEditActivity.this.finishFragment();
            } else if (i != 1) {
            } else {
                ChatEditActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends SizeNotifierFrameLayout {
        private boolean ignoreLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ChatEditActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            int paddingTop = size2 - getPaddingTop();
            measureChildWithMargins(((BaseFragment) ChatEditActivity.this).actionBar, i, 0, i2, 0);
            if (measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                this.ignoreLayout = true;
                ChatEditActivity.this.nameTextView.hideEmojiView();
                this.ignoreLayout = false;
            }
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) ChatEditActivity.this).actionBar) {
                    if (ChatEditActivity.this.nameTextView != null && ChatEditActivity.this.nameTextView.isPopupView(childAt)) {
                        if (AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) {
                            if (AndroidUtilities.isTablet()) {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (paddingTop - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                            } else {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((paddingTop - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                            }
                        } else {
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                        }
                    } else {
                        measureChildWithMargins(childAt, i, 0, i2, 0);
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x0072  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x008c  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00a1  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00b3  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00bc  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int childCount = getChildCount();
            int measureKeyboardHeight = measureKeyboardHeight();
            int emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : ChatEditActivity.this.nameTextView.getEmojiPadding();
            setBottomClip(emojiPadding);
            for (int i13 = 0; i13 < childCount; i13++) {
                View childAt = getChildAt(i13);
                if (childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    int i14 = layoutParams.gravity;
                    if (i14 == -1) {
                        i14 = 51;
                    }
                    int i15 = i14 & 7;
                    int i16 = i14 & 112;
                    int i17 = i15 & 7;
                    if (i17 == 1) {
                        i12 = (((i3 - i) - measuredWidth) / 2) + layoutParams.leftMargin;
                        i11 = layoutParams.rightMargin;
                    } else if (i17 == 5) {
                        i12 = i3 - measuredWidth;
                        i11 = layoutParams.rightMargin;
                    } else {
                        i5 = layoutParams.leftMargin;
                        if (i16 == 16) {
                            if (i16 == 48) {
                                i6 = layoutParams.topMargin + getPaddingTop();
                            } else if (i16 == 80) {
                                i9 = ((i4 - emojiPadding) - i2) - measuredHeight;
                                i10 = layoutParams.bottomMargin;
                            } else {
                                i6 = layoutParams.topMargin;
                            }
                            if (ChatEditActivity.this.nameTextView != null && ChatEditActivity.this.nameTextView.isPopupView(childAt)) {
                                if (!AndroidUtilities.isTablet()) {
                                    i8 = getMeasuredHeight();
                                    i7 = childAt.getMeasuredHeight();
                                } else {
                                    i8 = getMeasuredHeight() + measureKeyboardHeight;
                                    i7 = childAt.getMeasuredHeight();
                                }
                                i6 = i8 - i7;
                            }
                            childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                        } else {
                            i9 = ((((i4 - emojiPadding) - i2) - measuredHeight) / 2) + layoutParams.topMargin;
                            i10 = layoutParams.bottomMargin;
                        }
                        i6 = i9 - i10;
                        if (ChatEditActivity.this.nameTextView != null) {
                            if (!AndroidUtilities.isTablet()) {
                            }
                            i6 = i8 - i7;
                        }
                        childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                    }
                    i5 = i12 - i11;
                    if (i16 == 16) {
                    }
                    i6 = i9 - i10;
                    if (ChatEditActivity.this.nameTextView != null) {
                    }
                    childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                }
            }
            notifyHeightChanged();
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends BackupImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            ChatEditActivity.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            if (ChatEditActivity.this.avatarOverlay != null) {
                ChatEditActivity.this.avatarOverlay.invalidate();
            }
            super.invalidate();
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            if (ChatEditActivity.this.avatarOverlay != null) {
                ChatEditActivity.this.avatarOverlay.invalidate();
            }
            super.invalidate(i, i2, i3, i4);
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends View {
        final /* synthetic */ Paint val$paint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, Paint paint) {
            super(context);
            ChatEditActivity.this = r1;
            this.val$paint = paint;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (ChatEditActivity.this.avatarImage == null || !ChatEditActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                return;
            }
            this.val$paint.setAlpha((int) (ChatEditActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
            canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.val$paint);
        }
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        TLRPC$Chat chat;
        TLRPC$ChatPhoto tLRPC$ChatPhoto;
        ImageLocation imageLocation;
        if (this.imageUpdater.isUploadingImage() || (tLRPC$ChatPhoto = (chat = getMessagesController().getChat(Long.valueOf(this.chatId))).photo) == null || tLRPC$ChatPhoto.photo_big == null) {
            return;
        }
        PhotoViewer.getInstance().setParentActivity(getParentActivity());
        TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat.photo;
        int i = tLRPC$ChatPhoto2.dc_id;
        if (i != 0) {
            tLRPC$ChatPhoto2.photo_big.dc_id = i;
        }
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
            TLRPC$Photo tLRPC$Photo = tLRPC$ChatFull.chat_photo;
            if ((tLRPC$Photo instanceof TLRPC$TL_photo) && !tLRPC$Photo.video_sizes.isEmpty()) {
                imageLocation = ImageLocation.getForPhoto(this.info.chat_photo.video_sizes.get(0), this.info.chat_photo);
                PhotoViewer.getInstance().openPhotoWithVideo(chat.photo.photo_big, imageLocation, this.provider);
            }
        }
        imageLocation = null;
        PhotoViewer.getInstance().openPhotoWithVideo(chat.photo.photo_big, imageLocation, this.provider);
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6() {
            ChatEditActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            ChatEditActivity.this.avatarDrawable.setInfo(5L, ChatEditActivity.this.nameTextView.getText().toString(), null);
            if (ChatEditActivity.this.avatarImage != null) {
                ChatEditActivity.this.avatarImage.invalidate();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends TextCell {
        AnonymousClass7(ChatEditActivity chatEditActivity, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Cells.TextCell, android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    public /* synthetic */ void lambda$createView$6(View view) {
        this.imageUpdater.openMenu(this.avatar != null, new ChatEditActivity$$ExternalSyntheticLambda24(this), new ChatEditActivity$$ExternalSyntheticLambda3(this));
        this.cameraDrawable.setCurrentFrame(0);
        this.cameraDrawable.setCustomEndFrame(43);
        this.setAvatarCell.imageView.playAnimation();
    }

    public /* synthetic */ void lambda$createView$4() {
        this.avatar = null;
        MessagesController.getInstance(this.currentAccount).changeChatAvatar(this.chatId, null, null, null, 0.0d, null, null, null, null);
        showAvatarProgress(false, true);
        this.avatarImage.setImage((ImageLocation) null, (String) null, this.avatarDrawable, this.currentChat);
        this.cameraDrawable.setCurrentFrame(0);
        this.setAvatarCell.imageView.playAnimation();
    }

    public /* synthetic */ void lambda$createView$5(DialogInterface dialogInterface) {
        if (!this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCustomEndFrame(86);
            this.setAvatarCell.imageView.playAnimation();
            return;
        }
        this.cameraDrawable.setCurrentFrame(0, false);
    }

    public /* synthetic */ boolean lambda$createView$7(TextView textView, int i, KeyEvent keyEvent) {
        View view;
        if (i != 6 || (view = this.doneButton) == null) {
            return false;
        }
        view.performClick();
        return true;
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass8(ChatEditActivity chatEditActivity) {
        }
    }

    public /* synthetic */ void lambda$createView$9(View view) {
        if (!AndroidUtilities.isGoogleMapsInstalled(this)) {
            return;
        }
        LocationActivity locationActivity = new LocationActivity(4);
        locationActivity.setDialogId(-this.chatId);
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
            TLRPC$ChannelLocation tLRPC$ChannelLocation = tLRPC$ChatFull.location;
            if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocation) {
                locationActivity.setInitialLocation((TLRPC$TL_channelLocation) tLRPC$ChannelLocation);
            }
        }
        locationActivity.setDelegate(new ChatEditActivity$$ExternalSyntheticLambda31(this));
        presentFragment(locationActivity);
    }

    public /* synthetic */ void lambda$createView$8(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        TLRPC$TL_channelLocation tLRPC$TL_channelLocation = new TLRPC$TL_channelLocation();
        tLRPC$TL_channelLocation.address = tLRPC$MessageMedia.address;
        tLRPC$TL_channelLocation.geo_point = tLRPC$MessageMedia.geo;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        tLRPC$ChatFull.location = tLRPC$TL_channelLocation;
        tLRPC$ChatFull.flags |= 32768;
        updateFields(false);
        getMessagesController().loadFullChat(this.chatId, 0, true);
    }

    public /* synthetic */ void lambda$createView$10(View view) {
        long j = this.chatId;
        TextDetailCell textDetailCell = this.locationCell;
        ChatEditTypeActivity chatEditTypeActivity = new ChatEditTypeActivity(j, textDetailCell != null && textDetailCell.getVisibility() == 0);
        chatEditTypeActivity.setInfo(this.info);
        presentFragment(chatEditTypeActivity);
    }

    public /* synthetic */ void lambda$createView$11(View view) {
        ChatLinkActivity chatLinkActivity = new ChatLinkActivity(this.chatId);
        chatLinkActivity.setInfo(this.info);
        presentFragment(chatLinkActivity);
    }

    public /* synthetic */ void lambda$createView$13(Context context, View view) {
        BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setApplyTopPadding(false);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        HeaderCell headerCell = new HeaderCell(context, "dialogTextBlue2", 23, 15, false);
        headerCell.setHeight(47);
        headerCell.setText(LocaleController.getString("ChatHistory", 2131625012));
        linearLayout.addView(headerCell);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2));
        RadioButtonCell[] radioButtonCellArr = new RadioButtonCell[2];
        for (int i = 0; i < 2; i++) {
            radioButtonCellArr[i] = new RadioButtonCell(context, true);
            radioButtonCellArr[i].setTag(Integer.valueOf(i));
            radioButtonCellArr[i].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (i == 0) {
                radioButtonCellArr[i].setTextAndValue(LocaleController.getString("ChatHistoryVisible", 2131625016), LocaleController.getString("ChatHistoryVisibleInfo", 2131625017), true, !this.historyHidden);
            } else if (ChatObject.isChannel(this.currentChat)) {
                radioButtonCellArr[i].setTextAndValue(LocaleController.getString("ChatHistoryHidden", 2131625013), LocaleController.getString("ChatHistoryHiddenInfo", 2131625014), false, this.historyHidden);
            } else {
                radioButtonCellArr[i].setTextAndValue(LocaleController.getString("ChatHistoryHidden", 2131625013), LocaleController.getString("ChatHistoryHiddenInfo2", 2131625015), false, this.historyHidden);
            }
            linearLayout2.addView(radioButtonCellArr[i], LayoutHelper.createLinear(-1, -2));
            radioButtonCellArr[i].setOnClickListener(new ChatEditActivity$$ExternalSyntheticLambda21(this, radioButtonCellArr, builder));
        }
        builder.setCustomView(linearLayout);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$createView$12(RadioButtonCell[] radioButtonCellArr, BottomSheet.Builder builder, View view) {
        Integer num = (Integer) view.getTag();
        boolean z = false;
        radioButtonCellArr[0].setChecked(num.intValue() == 0, true);
        radioButtonCellArr[1].setChecked(num.intValue() == 1, true);
        if (num.intValue() == 1) {
            z = true;
        }
        this.historyHidden = z;
        builder.getDismissRunnable().run();
        updateFields(true);
    }

    public /* synthetic */ void lambda$createView$14(View view) {
        boolean z = !this.signMessages;
        this.signMessages = z;
        ((TextCheckCell) view).setChecked(z);
    }

    public /* synthetic */ void lambda$createView$15(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", (this.isChannel || this.currentChat.gigagroup) ? 0 : 3);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$16(View view) {
        ManageLinksActivity manageLinksActivity = new ManageLinksActivity(this.chatId, 0L, 0);
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        manageLinksActivity.setInfo(tLRPC$ChatFull, tLRPC$ChatFull.exported_invite);
        presentFragment(manageLinksActivity);
    }

    public /* synthetic */ void lambda$createView$17(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        ChatReactionsEditActivity chatReactionsEditActivity = new ChatReactionsEditActivity(bundle);
        chatReactionsEditActivity.setInfo(this.info);
        presentFragment(chatReactionsEditActivity);
    }

    public /* synthetic */ void lambda$createView$18(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", 1);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$19(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", 2);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$20(View view) {
        presentFragment(new MemberRequestsActivity(this.chatId));
    }

    public /* synthetic */ void lambda$createView$21(View view) {
        presentFragment(new ChannelAdminLogActivity(this.currentChat));
    }

    public /* synthetic */ void lambda$createView$22(View view) {
        presentFragment(new ChannelAdminLogActivity(this.currentChat));
    }

    public /* synthetic */ void lambda$createView$23(View view) {
        GroupStickersActivity groupStickersActivity = new GroupStickersActivity(this.currentChat.id);
        groupStickersActivity.setInfo(this.info);
        presentFragment(groupStickersActivity);
    }

    public /* synthetic */ void lambda$createView$25(View view) {
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, true, false, this.currentChat, null, false, true, false, new ChatEditActivity$$ExternalSyntheticLambda27(this), null);
    }

    public /* synthetic */ void lambda$createView$24(boolean z) {
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().postNotificationName(NotificationCenter.closeChats, Long.valueOf(-this.chatId));
        } else {
            getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        finishFragment();
        getNotificationCenter().postNotificationName(NotificationCenter.needDeleteDialog, Long.valueOf(-this.currentChat.id), null, this.currentChat, Boolean.valueOf(z));
    }

    private void setAvatar() {
        TLRPC$Chat chat;
        if (this.avatarImage == null || (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) == null) {
            return;
        }
        this.currentChat = chat;
        TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
        boolean z = false;
        if (tLRPC$ChatPhoto != null) {
            this.avatar = tLRPC$ChatPhoto.photo_small;
            ImageLocation forUserOrChat = ImageLocation.getForUserOrChat(chat, 1);
            this.avatarImage.setForUserOrChat(this.currentChat, this.avatarDrawable);
            if (forUserOrChat != null) {
                z = true;
            }
        } else {
            this.avatarImage.setImageDrawable(this.avatarDrawable);
        }
        if (this.setAvatarCell != null) {
            if (z || this.imageUpdater.isUploadingImage()) {
                this.setAvatarCell.setTextAndIcon(LocaleController.getString("ChatSetNewPhoto", 2131625029), 2131165633, true);
            } else {
                this.setAvatarCell.setTextAndIcon(LocaleController.getString("ChatSetPhotoOrVideo", 2131625030), 2131165633, true);
            }
            if (this.cameraDrawable == null) {
                this.cameraDrawable = new RLottieDrawable(2131558415, "2131558415", AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
            }
            this.setAvatarCell.imageView.setTranslationY(-AndroidUtilities.dp(9.0f));
            this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
            this.setAvatarCell.imageView.setAnimation(this.cameraDrawable);
        }
        if (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) {
            return;
        }
        PhotoViewer.getInstance().checkCurrentImageVisibility();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        EditTextBoldCursor editTextBoldCursor;
        if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            if (tLRPC$ChatFull.id != this.chatId) {
                return;
            }
            if (this.info == null && (editTextBoldCursor = this.descriptionTextView) != null) {
                editTextBoldCursor.setText(tLRPC$ChatFull.about);
            }
            boolean z = true;
            boolean z2 = this.info == null;
            this.info = tLRPC$ChatFull;
            if (ChatObject.isChannel(this.currentChat) && !this.info.hidden_prehistory) {
                z = false;
            }
            this.historyHidden = z;
            updateFields(false);
            if (!z2) {
                return;
            }
            loadLinksCount();
        } else if (i == NotificationCenter.updateInterfaces) {
            if ((((Integer) objArr[0]).intValue() & MessagesController.UPDATE_MASK_AVATAR) == 0) {
                return;
            }
            setAvatar();
        } else if (i != NotificationCenter.chatAvailableReactionsUpdated) {
        } else {
            long longValue = ((Long) objArr[0]).longValue();
            if (longValue != this.chatId) {
                return;
            }
            TLRPC$ChatFull chatFull = getMessagesController().getChatFull(longValue);
            this.info = chatFull;
            if (chatFull != null) {
                this.availableReactions = chatFull.available_reactions;
            }
            updateReactionsCell();
        }
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void onUploadProgressChanged(float f) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didStartUpload(boolean z) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(0.0f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        AndroidUtilities.runOnUIThread(new ChatEditActivity$$ExternalSyntheticLambda25(this, tLRPC$PhotoSize2, tLRPC$InputFile, tLRPC$InputFile2, d, str, tLRPC$PhotoSize));
    }

    public /* synthetic */ void lambda$didUploadPhoto$26(TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
        this.avatar = tLRPC$FileLocation;
        if (tLRPC$InputFile != null || tLRPC$InputFile2 != null) {
            getMessagesController().changeChatAvatar(this.chatId, null, tLRPC$InputFile, tLRPC$InputFile2, d, str, tLRPC$PhotoSize.location, tLRPC$PhotoSize2.location, null);
            if (this.createAfterUpload) {
                try {
                    AlertDialog alertDialog = this.progressDialog;
                    if (alertDialog != null && alertDialog.isShowing()) {
                        this.progressDialog.dismiss();
                        this.progressDialog = null;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.donePressed = false;
                this.doneButton.performClick();
            }
            showAvatarProgress(false, true);
            return;
        }
        this.avatarImage.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, this.currentChat);
        this.setAvatarCell.setTextAndIcon(LocaleController.getString("ChatSetNewPhoto", 2131625029), 2131165633, true);
        if (this.cameraDrawable == null) {
            this.cameraDrawable = new RLottieDrawable(2131558415, "2131558415", AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
        }
        this.setAvatarCell.imageView.setTranslationY(-AndroidUtilities.dp(9.0f));
        this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
        this.setAvatarCell.imageView.setAnimation(this.cameraDrawable);
        showAvatarProgress(true, false);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }

    public void showConvertTooltip() {
        this.undoView.showWithAction(0L, 76, (Runnable) null);
    }

    public boolean checkDiscard() {
        String str;
        EditTextEmoji editTextEmoji;
        EditTextBoldCursor editTextBoldCursor;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull == null || (str = tLRPC$ChatFull.about) == null) {
            str = "";
        }
        if ((tLRPC$ChatFull == null || !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory == this.historyHidden) && (((editTextEmoji = this.nameTextView) == null || this.currentChat.title.equals(editTextEmoji.getText().toString())) && (((editTextBoldCursor = this.descriptionTextView) == null || str.equals(editTextBoldCursor.getText().toString())) && this.signMessages == this.currentChat.signatures))) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131628812));
        if (this.isChannel) {
            builder.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131624958));
        } else {
            builder.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131626102));
        }
        builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131624387), new ChatEditActivity$$ExternalSyntheticLambda2(this));
        builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131627220), new ChatEditActivity$$ExternalSyntheticLambda1(this));
        showDialog(builder.create());
        return false;
    }

    public /* synthetic */ void lambda$checkDiscard$27(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$28(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    private int getAdminCount() {
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull == null) {
            return 1;
        }
        int size = tLRPC$ChatFull.participants.participants.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant = this.info.participants.participants.get(i2);
            if ((tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin) || (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator)) {
                i++;
            }
        }
        return i;
    }

    public void processDone() {
        EditTextEmoji editTextEmoji;
        String str;
        if (this.donePressed || (editTextEmoji = this.nameTextView) == null) {
            return;
        }
        if (editTextEmoji.length() == 0) {
            Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.nameTextView, 2.0f, 0);
            return;
        }
        this.donePressed = true;
        if (!ChatObject.isChannel(this.currentChat) && !this.historyHidden) {
            getMessagesController().convertToMegaGroup(getParentActivity(), this.chatId, this, new ChatEditActivity$$ExternalSyntheticLambda28(this));
            return;
        }
        if (this.info != null && ChatObject.isChannel(this.currentChat)) {
            TLRPC$ChatFull tLRPC$ChatFull = this.info;
            boolean z = tLRPC$ChatFull.hidden_prehistory;
            boolean z2 = this.historyHidden;
            if (z != z2) {
                tLRPC$ChatFull.hidden_prehistory = z2;
                getMessagesController().toggleChannelInvitesHistory(this.chatId, this.historyHidden);
            }
        }
        if (this.imageUpdater.isUploadingImage()) {
            this.createAfterUpload = true;
            AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            this.progressDialog = alertDialog;
            alertDialog.setOnCancelListener(new ChatEditActivity$$ExternalSyntheticLambda0(this));
            this.progressDialog.show();
            return;
        }
        if (!this.currentChat.title.equals(this.nameTextView.getText().toString())) {
            getMessagesController().changeChatTitle(this.chatId, this.nameTextView.getText().toString());
        }
        TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
        if (tLRPC$ChatFull2 == null || (str = tLRPC$ChatFull2.about) == null) {
            str = "";
        }
        EditTextBoldCursor editTextBoldCursor = this.descriptionTextView;
        if (editTextBoldCursor != null && !str.equals(editTextBoldCursor.getText().toString())) {
            getMessagesController().updateChatAbout(this.chatId, this.descriptionTextView.getText().toString(), this.info);
        }
        boolean z3 = this.signMessages;
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        if (z3 != tLRPC$Chat.signatures) {
            tLRPC$Chat.signatures = true;
            getMessagesController().toggleChannelSignatures(this.chatId, this.signMessages);
        }
        finishFragment();
    }

    public /* synthetic */ void lambda$processDone$29(long j) {
        if (j == 0) {
            this.donePressed = false;
            return;
        }
        this.chatId = j;
        this.currentChat = getMessagesController().getChat(Long.valueOf(j));
        this.donePressed = false;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
            tLRPC$ChatFull.hidden_prehistory = true;
        }
        processDone();
    }

    public /* synthetic */ void lambda$processDone$30(DialogInterface dialogInterface) {
        this.createAfterUpload = false;
        this.progressDialog = null;
        this.donePressed = false;
    }

    private void showAvatarProgress(boolean z, boolean z2) {
        if (this.avatarProgressView == null) {
            return;
        }
        AnimatorSet animatorSet = this.avatarAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.avatarAnimation = null;
        }
        if (z2) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.avatarAnimation = animatorSet2;
            if (z) {
                this.avatarProgressView.setVisibility(0);
                this.avatarOverlay.setVisibility(0);
                this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.avatarOverlay, View.ALPHA, 1.0f));
            } else {
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.avatarOverlay, View.ALPHA, 0.0f));
            }
            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener(new AnonymousClass9(z));
            this.avatarAnimation.start();
        } else if (z) {
            this.avatarProgressView.setAlpha(1.0f);
            this.avatarProgressView.setVisibility(0);
            this.avatarOverlay.setAlpha(1.0f);
            this.avatarOverlay.setVisibility(0);
        } else {
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
            this.avatarOverlay.setAlpha(0.0f);
            this.avatarOverlay.setVisibility(4);
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass9(boolean z) {
            ChatEditActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChatEditActivity.this.avatarAnimation == null || ChatEditActivity.this.avatarProgressView == null) {
                return;
            }
            if (!this.val$show) {
                ChatEditActivity.this.avatarProgressView.setVisibility(4);
                ChatEditActivity.this.avatarOverlay.setVisibility(4);
            }
            ChatEditActivity.this.avatarAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ChatEditActivity.this.avatarAnimation = null;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        this.imageUpdater.onActivityResult(i, i2, intent);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String str;
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null && (str = imageUpdater.currentPicturePath) != null) {
            bundle.putString("path", str);
        }
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            String obj = editTextEmoji.getText().toString();
            if (obj.length() == 0) {
                return;
            }
            bundle.putString("nameTextView", obj);
        }
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            if (this.currentChat == null) {
                this.currentChat = getMessagesController().getChat(Long.valueOf(this.chatId));
            }
            this.historyHidden = !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory;
            this.availableReactions = this.info.available_reactions;
        }
    }

    private void updateFields(boolean z) {
        int i;
        String str;
        int i2;
        String str2;
        TextDetailCell textDetailCell;
        TextDetailCell textDetailCell2;
        String str3;
        int i3;
        String str4;
        int i4;
        String str5;
        TextDetailCell textDetailCell3;
        TLRPC$ChatFull tLRPC$ChatFull;
        TextDetailCell textDetailCell4;
        TextDetailCell textDetailCell5;
        TextDetailCell textDetailCell6;
        TLRPC$Chat chat;
        if (z && (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) != null) {
            this.currentChat = chat;
        }
        boolean isEmpty = TextUtils.isEmpty(this.currentChat.username);
        TextDetailCell textDetailCell7 = this.historyCell;
        if (textDetailCell7 != null) {
            TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
            if (tLRPC$ChatFull2 != null && (tLRPC$ChatFull2.location instanceof TLRPC$TL_channelLocation)) {
                textDetailCell7.setVisibility(8);
            } else {
                textDetailCell7.setVisibility((!isEmpty || !(tLRPC$ChatFull2 == null || tLRPC$ChatFull2.linked_chat_id == 0)) ? 8 : 0);
            }
        }
        ShadowSectionCell shadowSectionCell = this.settingsSectionCell;
        if (shadowSectionCell != null) {
            shadowSectionCell.setVisibility((this.signCell == null && this.typeCell == null && ((textDetailCell4 = this.linkedCell) == null || textDetailCell4.getVisibility() != 0) && (((textDetailCell5 = this.historyCell) == null || textDetailCell5.getVisibility() != 0) && ((textDetailCell6 = this.locationCell) == null || textDetailCell6.getVisibility() != 0))) ? 8 : 0);
        }
        TextCell textCell = this.logCell;
        if (textCell != null) {
            TLRPC$Chat tLRPC$Chat = this.currentChat;
            textCell.setVisibility((!tLRPC$Chat.megagroup || tLRPC$Chat.gigagroup || ((tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.participants_count > 200)) ? 0 : 8);
        }
        TextDetailCell textDetailCell8 = this.linkedCell;
        if (textDetailCell8 != null) {
            TLRPC$ChatFull tLRPC$ChatFull3 = this.info;
            if (tLRPC$ChatFull3 == null || (!this.isChannel && tLRPC$ChatFull3.linked_chat_id == 0)) {
                textDetailCell8.setVisibility(8);
            } else {
                textDetailCell8.setVisibility(0);
                if (this.info.linked_chat_id == 0) {
                    this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131625496), LocaleController.getString("DiscussionInfo", 2131625503), true);
                } else {
                    TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(this.info.linked_chat_id));
                    if (chat2 == null) {
                        this.linkedCell.setVisibility(8);
                    } else if (this.isChannel) {
                        if (TextUtils.isEmpty(chat2.username)) {
                            this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131625496), chat2.title, true);
                        } else {
                            TextDetailCell textDetailCell9 = this.linkedCell;
                            String string = LocaleController.getString("Discussion", 2131625496);
                            textDetailCell9.setTextAndValue(string, "@" + chat2.username, true);
                        }
                    } else if (TextUtils.isEmpty(chat2.username)) {
                        this.linkedCell.setTextAndValue(LocaleController.getString("LinkedChannel", 2131626458), chat2.title, false);
                    } else {
                        TextDetailCell textDetailCell10 = this.linkedCell;
                        String string2 = LocaleController.getString("LinkedChannel", 2131626458);
                        textDetailCell10.setTextAndValue(string2, "@" + chat2.username, false);
                    }
                }
            }
        }
        TextDetailCell textDetailCell11 = this.locationCell;
        if (textDetailCell11 != null) {
            TLRPC$ChatFull tLRPC$ChatFull4 = this.info;
            if (tLRPC$ChatFull4 != null && tLRPC$ChatFull4.can_set_location) {
                textDetailCell11.setVisibility(0);
                TLRPC$ChannelLocation tLRPC$ChannelLocation = this.info.location;
                if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocation) {
                    this.locationCell.setTextAndValue(LocaleController.getString("AttachLocation", 2131624492), ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address, true);
                } else {
                    this.locationCell.setTextAndValue(LocaleController.getString("AttachLocation", 2131624492), "Unknown address", true);
                }
            } else {
                textDetailCell11.setVisibility(8);
            }
        }
        if (this.typeCell != null) {
            TLRPC$ChatFull tLRPC$ChatFull5 = this.info;
            if (tLRPC$ChatFull5 != null && (tLRPC$ChatFull5.location instanceof TLRPC$TL_channelLocation)) {
                if (isEmpty) {
                    str5 = LocaleController.getString("TypeLocationGroupEdit", 2131628710);
                } else {
                    str5 = String.format("https://" + getMessagesController().linkPrefix + "/%s", this.currentChat.username);
                }
                TextDetailCell textDetailCell12 = this.typeCell;
                String string3 = LocaleController.getString("TypeLocationGroup", 2131628709);
                TextDetailCell textDetailCell13 = this.historyCell;
                textDetailCell12.setTextAndValue(string3, str5, (textDetailCell13 != null && textDetailCell13.getVisibility() == 0) || ((textDetailCell3 = this.linkedCell) != null && textDetailCell3.getVisibility() == 0));
            } else {
                boolean z2 = this.currentChat.noforwards;
                if (this.isChannel) {
                    if (!isEmpty) {
                        i4 = 2131628716;
                        str4 = "TypePublic";
                    } else if (z2) {
                        i4 = 2131628715;
                        str4 = "TypePrivateRestrictedForwards";
                    } else {
                        i4 = 2131628712;
                        str4 = "TypePrivate";
                    }
                    str2 = LocaleController.getString(str4, i4);
                } else {
                    if (!isEmpty) {
                        i3 = 2131628717;
                        str3 = "TypePublicGroup";
                    } else if (z2) {
                        i3 = 2131628714;
                        str3 = "TypePrivateGroupRestrictedForwards";
                    } else {
                        i3 = 2131628713;
                        str3 = "TypePrivateGroup";
                    }
                    str2 = LocaleController.getString(str3, i3);
                }
                if (this.isChannel) {
                    TextDetailCell textDetailCell14 = this.typeCell;
                    String string4 = LocaleController.getString("ChannelType", 2131624973);
                    TextDetailCell textDetailCell15 = this.historyCell;
                    textDetailCell14.setTextAndValue(string4, str2, (textDetailCell15 != null && textDetailCell15.getVisibility() == 0) || ((textDetailCell2 = this.linkedCell) != null && textDetailCell2.getVisibility() == 0));
                } else {
                    TextDetailCell textDetailCell16 = this.typeCell;
                    String string5 = LocaleController.getString("GroupType", 2131626106);
                    TextDetailCell textDetailCell17 = this.historyCell;
                    textDetailCell16.setTextAndValue(string5, str2, (textDetailCell17 != null && textDetailCell17.getVisibility() == 0) || ((textDetailCell = this.linkedCell) != null && textDetailCell.getVisibility() == 0));
                }
            }
        }
        if (this.historyCell != null) {
            if (this.historyHidden) {
                i2 = 2131625013;
                str = "ChatHistoryHidden";
            } else {
                i2 = 2131625016;
                str = "ChatHistoryVisible";
            }
            this.historyCell.setTextAndValue(LocaleController.getString("ChatHistory", 2131625012), LocaleController.getString(str, i2), false);
        }
        TextCell textCell2 = this.membersCell;
        if (textCell2 != null) {
            if (this.info != null) {
                TextCell textCell3 = this.memberRequestsCell;
                if (textCell3 != null) {
                    if (textCell3.getParent() == null) {
                        this.infoContainer.addView(this.memberRequestsCell, this.infoContainer.indexOfChild(this.membersCell) + 1, LayoutHelper.createLinear(-1, -2));
                    }
                    this.memberRequestsCell.setVisibility(this.info.requests_pending > 0 ? 0 : 8);
                }
                if (this.isChannel) {
                    this.membersCell.setTextAndValueAndIcon(LocaleController.getString("ChannelSubscribers", 2131624968), String.format("%d", Integer.valueOf(this.info.participants_count)), 2131165749, true);
                    TextCell textCell4 = this.blockCell;
                    String string6 = LocaleController.getString("ChannelBlacklist", 2131624882);
                    TLRPC$ChatFull tLRPC$ChatFull6 = this.info;
                    String format = String.format("%d", Integer.valueOf(Math.max(tLRPC$ChatFull6.banned_count, tLRPC$ChatFull6.kicked_count)));
                    TextCell textCell5 = this.logCell;
                    textCell4.setTextAndValueAndIcon(string6, format, 2131165970, textCell5 != null && textCell5.getVisibility() == 0);
                } else {
                    if (ChatObject.isChannel(this.currentChat)) {
                        this.membersCell.setTextAndValueAndIcon(LocaleController.getString("ChannelMembers", 2131624917), String.format("%d", Integer.valueOf(this.info.participants_count)), 2131165749, true);
                    } else {
                        this.membersCell.setTextAndValueAndIcon(LocaleController.getString("ChannelMembers", 2131624917), String.format("%d", Integer.valueOf(this.info.participants.participants.size())), 2131165749, this.memberRequestsCell.getVisibility() == 0);
                    }
                    TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                    if (tLRPC$Chat2.gigagroup) {
                        TextCell textCell6 = this.blockCell;
                        String string7 = LocaleController.getString("ChannelBlacklist", 2131624882);
                        TLRPC$ChatFull tLRPC$ChatFull7 = this.info;
                        String format2 = String.format("%d", Integer.valueOf(Math.max(tLRPC$ChatFull7.banned_count, tLRPC$ChatFull7.kicked_count)));
                        TextCell textCell7 = this.logCell;
                        textCell6.setTextAndValueAndIcon(string7, format2, 2131165970, textCell7 != null && textCell7.getVisibility() == 0);
                    } else {
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$Chat2.default_banned_rights;
                        if (tLRPC$TL_chatBannedRights != null) {
                            int i5 = !tLRPC$TL_chatBannedRights.send_stickers ? 1 : 0;
                            if (!tLRPC$TL_chatBannedRights.send_media) {
                                i5++;
                            }
                            if (!tLRPC$TL_chatBannedRights.embed_links) {
                                i5++;
                            }
                            if (!tLRPC$TL_chatBannedRights.send_messages) {
                                i5++;
                            }
                            if (!tLRPC$TL_chatBannedRights.pin_messages) {
                                i5++;
                            }
                            if (!tLRPC$TL_chatBannedRights.send_polls) {
                                i5++;
                            }
                            if (!tLRPC$TL_chatBannedRights.invite_users) {
                                i5++;
                            }
                            i = !tLRPC$TL_chatBannedRights.change_info ? i5 + 1 : i5;
                        } else {
                            i = 8;
                        }
                        this.blockCell.setTextAndValueAndIcon(LocaleController.getString("ChannelPermissions", 2131624942), String.format("%d/%d", Integer.valueOf(i), 8), 2131165841, true);
                    }
                    TextCell textCell8 = this.memberRequestsCell;
                    if (textCell8 != null) {
                        String string8 = LocaleController.getString("MemberRequests", 2131626592);
                        String format3 = String.format("%d", Integer.valueOf(this.info.requests_pending));
                        TextCell textCell9 = this.logCell;
                        textCell8.setTextAndValueAndIcon(string8, format3, 2131165905, textCell9 != null && textCell9.getVisibility() == 0);
                    }
                }
                TextCell textCell10 = this.adminCell;
                String string9 = LocaleController.getString("ChannelAdministrators", 2131624877);
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf(ChatObject.isChannel(this.currentChat) ? this.info.admins_count : getAdminCount());
                textCell10.setTextAndValueAndIcon(string9, String.format("%d", objArr), 2131165635, true);
            } else {
                if (this.isChannel) {
                    textCell2.setTextAndIcon(LocaleController.getString("ChannelSubscribers", 2131624968), 2131165749, true);
                    TextCell textCell11 = this.blockCell;
                    String string10 = LocaleController.getString("ChannelBlacklist", 2131624882);
                    TextCell textCell12 = this.logCell;
                    textCell11.setTextAndIcon(string10, 2131165678, textCell12 != null && textCell12.getVisibility() == 0);
                } else {
                    String string11 = LocaleController.getString("ChannelMembers", 2131624917);
                    TextCell textCell13 = this.logCell;
                    textCell2.setTextAndIcon(string11, 2131165749, textCell13 != null && textCell13.getVisibility() == 0);
                    if (this.currentChat.gigagroup) {
                        TextCell textCell14 = this.blockCell;
                        String string12 = LocaleController.getString("ChannelBlacklist", 2131624882);
                        TextCell textCell15 = this.logCell;
                        textCell14.setTextAndIcon(string12, 2131165678, textCell15 != null && textCell15.getVisibility() == 0);
                    } else {
                        this.blockCell.setTextAndIcon(LocaleController.getString("ChannelPermissions", 2131624942), 2131165841, true);
                    }
                }
                this.adminCell.setTextAndIcon(LocaleController.getString("ChannelAdministrators", 2131624877), 2131165635, true);
            }
            this.reactionsCell.setVisibility(ChatObject.canChangeChatInfo(this.currentChat) ? 0 : 8);
            updateReactionsCell();
            if (this.info == null || !ChatObject.canUserDoAdminAction(this.currentChat, 3) || (!isEmpty && this.currentChat.creator)) {
                this.inviteLinksCell.setVisibility(8);
            } else if (this.info.invitesCount > 0) {
                this.inviteLinksCell.setTextAndValueAndIcon(LocaleController.getString("InviteLinks", 2131626270), Integer.toString(this.info.invitesCount), 2131165783, true);
            } else {
                this.inviteLinksCell.setTextAndValueAndIcon(LocaleController.getString("InviteLinks", 2131626270), "1", 2131165783, true);
            }
        }
        TextCell textCell16 = this.stickersCell;
        if (textCell16 == null || this.info == null) {
            return;
        }
        String string13 = LocaleController.getString(2131626104);
        TLRPC$StickerSet tLRPC$StickerSet = this.info.stickerset;
        textCell16.setTextAndValueAndIcon(string13, tLRPC$StickerSet != null ? tLRPC$StickerSet.title : LocaleController.getString(2131624237), 2131165948, false);
    }

    private void updateReactionsCell() {
        int i = 0;
        for (int i2 = 0; i2 < this.availableReactions.size(); i2++) {
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction = getMediaDataController().getReactionsMap().get(this.availableReactions.get(i2));
            if (tLRPC$TL_availableReaction != null && !tLRPC$TL_availableReaction.inactive) {
                i++;
            }
        }
        int min = Math.min(getMediaDataController().getEnabledReactionsList().size(), i);
        this.reactionsCell.setTextAndValueAndIcon(LocaleController.getString("Reactions", 2131627847), min == 0 ? LocaleController.getString("ReactionsOff", 2131627855) : LocaleController.formatString("ReactionsCount", 2131627848, Integer.valueOf(min), Integer.valueOf(getMediaDataController().getEnabledReactionsList().size())), 2131165889, true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChatEditActivity$$ExternalSyntheticLambda30 chatEditActivity$$ExternalSyntheticLambda30 = new ChatEditActivity$$ExternalSyntheticLambda30(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.setAvatarCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.setAvatarCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.setAvatarCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        arrayList.add(new ThemeDescription(this.membersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.membersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.membersCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.adminCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.adminCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.adminCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        if (this.memberRequestsCell != null) {
            arrayList.add(new ThemeDescription(this.memberRequestsCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
            arrayList.add(new ThemeDescription(this.memberRequestsCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(this.memberRequestsCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        }
        arrayList.add(new ThemeDescription(this.blockCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.blockCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.blockCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.logCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.logCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.logCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.typeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.historyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.locationCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.locationCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.locationCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.avatarContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.settingsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.typeEditContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.deleteContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.stickersContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.infoContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.settingsTopSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.settingsSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.deleteInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.signCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.stickersInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.stickersInfoCell, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, chatEditActivity$$ExternalSyntheticLambda30, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatEditActivity$$ExternalSyntheticLambda30, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "undo_background"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{UndoView.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.reactionsCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.reactionsCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.reactionsCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$31() {
        BackupImageView backupImageView = this.avatarImage;
        if (backupImageView != null) {
            backupImageView.invalidate();
        }
    }
}
