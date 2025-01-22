package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.exoplayer2.util.Consumer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ChatEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LoadingSpan;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ChatCustomReactionsEditActivity;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.FilterCreateActivity;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stars.BotStarsActivity;
import org.telegram.ui.Stars.BotStarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.bots.AffiliateProgramFragment;
import org.telegram.ui.bots.BotVerifySheet;
import org.telegram.ui.bots.ChannelAffiliateProgramsFragment;

/* loaded from: classes4.dex */
public class ChatEditActivity extends BaseFragment implements ImageUpdater.ImageUpdaterDelegate, NotificationCenter.NotificationCenterDelegate {
    private TextCell adminCell;
    private TLRPC.ChatReactions availableReactions;
    private TLRPC.FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private LinearLayout avatarContainer;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private LinearLayout balanceContainer;
    private TextCell blockCell;
    private TL_stories.TL_premium_boostsStatus boostsStatus;
    private TextCell botAffiliateProgramCell;
    private TextInfoPrivacyCell botInfoCell;
    RLottieDrawable cameraDrawable;
    private boolean canForum;
    private TextCell changeBotSettingsCell;
    private TextCell channelAffiliateProgramsCell;
    private TLRPC.TL_chatAdminRights chatAdminRights;
    private TLRPC.TL_chatBannedRights chatBannedRights;
    private TLRPC.TL_chatBannedRights chatDefaultBannedRights;
    private long chatId;
    private PeerColorActivity.ChangeNameColorCell colorCell;
    private boolean createAfterUpload;
    private TLRPC.Chat currentChat;
    private TLRPC.User currentUser;
    private TextSettingsCell deleteCell;
    private FrameLayout deleteContainer;
    private ShadowSectionCell deleteInfoCell;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private boolean donePressed;
    private TextCell editCommandsCell;
    private TextCell editIntroCell;
    private boolean forum;
    private TextCell forumsCell;
    private boolean hasUploadedPhoto;
    private TextCell historyCell;
    private boolean historyHidden;
    private ImageUpdater imageUpdater;
    private TLRPC.ChatFull info;
    private LinearLayout infoContainer;
    private ShadowSectionCell infoSectionCell;
    private TextCell inviteLinksCell;
    private boolean isChannel;
    private LinearLayout linearLayout;
    private TextCell linkedCell;
    private TextCell locationCell;
    private TextCell logCell;
    private TextCell memberRequestsCell;
    private TextCell membersCell;
    private EditTextEmoji nameTextView;
    private final List preloadedReactions;
    private AlertDialog progressDialog;
    private PhotoViewer.PhotoViewerProvider provider;
    private TextCell publicLinkCell;
    private TextCell reactionsCell;
    private TextCell setAvatarCell;
    private LinearLayout settingsContainer;
    private TextInfoPrivacyCell settingsSectionCell;
    private ShadowSectionCell settingsTopSectionCell;
    private TextCell starsBalanceCell;
    private TextCell statsAndBoosts;
    private TextCell stickersCell;
    private FrameLayout stickersContainer;
    private TextInfoPrivacyCell stickersInfoCell;
    private TextCell tonBalanceCell;
    private TextCell typeCell;
    private LinearLayout typeEditContainer;
    private UndoView undoView;
    private ValueAnimator updateHistoryShowAnimator;
    private long userId;
    private TLRPC.UserFull userInfo;
    private TextCell verifyCell;
    private TextInfoPrivacyCell verifyInfoCell;

    class 1 extends PhotoViewer.EmptyPhotoViewerProvider {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeletePhoto$0() {
            ChatEditActivity.this.avatarImage.setImageDrawable(ChatEditActivity.this.avatarDrawable);
            ChatEditActivity.this.setAvatarCell.setTextAndIcon((CharSequence) LocaleController.getString("ChatSetPhotoOrVideo", R.string.ChatSetPhotoOrVideo), R.drawable.msg_addphoto, true);
            if (ChatEditActivity.this.currentUser != null) {
                ChatEditActivity.this.currentUser.photo = null;
                ChatEditActivity.this.getMessagesController().putUser(ChatEditActivity.this.currentUser, true);
            }
            ChatEditActivity.this.hasUploadedPhoto = true;
            ChatEditActivity chatEditActivity = ChatEditActivity.this;
            if (chatEditActivity.cameraDrawable == null) {
                int i = R.raw.camera_outline;
                chatEditActivity.cameraDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
            }
            ChatEditActivity.this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
            ChatEditActivity.this.setAvatarCell.imageView.setAnimation(ChatEditActivity.this.cameraDrawable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeletePhoto$1(TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatEditActivity.1.this.lambda$onDeletePhoto$0();
                }
            });
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean canLoadMoreAvatars() {
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x0036, code lost:
        
            if (r8 != null) goto L25;
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x0055, code lost:
        
            if (r8 != null) goto L25;
         */
        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC.FileLocation fileLocation, int i, boolean z, boolean z2) {
            TLRPC.FileLocation fileLocation2;
            if (fileLocation == null) {
                return null;
            }
            if (ChatEditActivity.this.currentUser != null) {
                TLRPC.User user = ChatEditActivity.this.userId == 0 ? null : ChatEditActivity.this.getMessagesController().getUser(Long.valueOf(ChatEditActivity.this.userId));
                if (user != null) {
                    TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
                    if (userProfilePhoto != null) {
                        fileLocation2 = userProfilePhoto.photo_big;
                    }
                }
                fileLocation2 = null;
            } else {
                TLRPC.Chat chat = ChatEditActivity.this.getMessagesController().getChat(Long.valueOf(ChatEditActivity.this.chatId));
                if (chat != null) {
                    TLRPC.ChatPhoto chatPhoto = chat.photo;
                    if (chatPhoto != null) {
                        fileLocation2 = chatPhoto.photo_big;
                    }
                }
                fileLocation2 = null;
            }
            if (fileLocation2 == null || fileLocation2.local_id != fileLocation.local_id || fileLocation2.volume_id != fileLocation.volume_id || fileLocation2.dc_id != fileLocation.dc_id) {
                return null;
            }
            int[] iArr = new int[2];
            ChatEditActivity.this.avatarImage.getLocationInWindow(iArr);
            PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
            placeProviderObject.viewX = iArr[0];
            placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT < 21 ? AndroidUtilities.statusBarHeight : 0);
            placeProviderObject.parentView = ChatEditActivity.this.avatarImage;
            placeProviderObject.imageReceiver = ChatEditActivity.this.avatarImage.getImageReceiver();
            placeProviderObject.dialogId = ChatEditActivity.this.userId != 0 ? ChatEditActivity.this.userId : -ChatEditActivity.this.chatId;
            placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
            placeProviderObject.size = -1L;
            placeProviderObject.radius = ChatEditActivity.this.avatarImage.getImageReceiver().getRoundRadius(true);
            placeProviderObject.scale = ChatEditActivity.this.avatarContainer.getScaleX();
            placeProviderObject.canEdit = true;
            return placeProviderObject;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public int getTotalImageCount() {
            return 1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean onDeletePhoto(int i) {
            if (ChatEditActivity.this.userId == 0) {
                return true;
            }
            TLRPC.TL_photos_updateProfilePhoto tL_photos_updateProfilePhoto = new TLRPC.TL_photos_updateProfilePhoto();
            tL_photos_updateProfilePhoto.bot = ChatEditActivity.this.getMessagesController().getInputUser(ChatEditActivity.this.userId);
            tL_photos_updateProfilePhoto.flags |= 2;
            tL_photos_updateProfilePhoto.id = new TLRPC.TL_inputPhotoEmpty();
            ChatEditActivity.this.getConnectionsManager().sendRequest(tL_photos_updateProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.ChatEditActivity$1$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChatEditActivity.1.this.lambda$onDeletePhoto$1(tLObject, tL_error);
                }
            });
            return false;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void openPhotoForEdit(String str, String str2, boolean z) {
            ChatEditActivity.this.imageUpdater.openPhotoForEdit(str, str2, 0, z);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void willHidePhotoViewer() {
            ChatEditActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
        }
    }

    public ChatEditActivity(Bundle bundle) {
        super(bundle);
        this.preloadedReactions = new ArrayList();
        this.provider = new 1();
        this.avatarDrawable = new AvatarDrawable();
        this.chatId = bundle.getLong("chat_id", 0L);
        this.userId = bundle.getLong("user_id", 0L);
        if (this.chatId == 0) {
            this.imageUpdater = new ImageUpdater(false, 0, false);
        } else {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            this.imageUpdater = new ImageUpdater(true, (chat == null || !ChatObject.isChannelAndNotMegaGroup(chat)) ? 2 : 1, true);
        }
    }

    public static CharSequence applyNewSpan(String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.append((CharSequence) "  d");
        FilterCreateActivity.NewSpan newSpan = new FilterCreateActivity.NewSpan(false, 10);
        newSpan.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
        newSpan.setColor(Theme.getColor(Theme.key_premiumGradient1));
        spannableStringBuilder.setSpan(newSpan, spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
        return spannableStringBuilder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        EditTextEmoji editTextEmoji;
        EditTextBoldCursor editTextBoldCursor;
        AlertDialog.Builder builder;
        int i;
        String str;
        String str2;
        EditTextBoldCursor editTextBoldCursor2;
        String str3;
        String str4 = "";
        if (this.userId != 0) {
            TLRPC.UserFull userFull = this.userInfo;
            if (userFull != null && (str3 = userFull.about) != null) {
                str4 = str3;
            }
            EditTextEmoji editTextEmoji2 = this.nameTextView;
            if ((editTextEmoji2 == null || this.currentUser.first_name.equals(editTextEmoji2.getText().toString())) && ((editTextBoldCursor2 = this.descriptionTextView) == null || str4.equals(editTextBoldCursor2.getText().toString()))) {
                return true;
            }
            builder = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString("UserRestrictionsApplyChanges", R.string.UserRestrictionsApplyChanges)).setMessage(LocaleController.getString(R.string.BotSettingsChangedAlert)).setPositiveButton(LocaleController.getString("ApplyTheme", R.string.ApplyTheme), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda32
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    ChatEditActivity.this.lambda$checkDiscard$42(alertDialog, i2);
                }
            }).setNegativeButton(LocaleController.getString("PassportDiscard", R.string.PassportDiscard), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda33
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    ChatEditActivity.this.lambda$checkDiscard$43(alertDialog, i2);
                }
            });
        } else {
            TLRPC.ChatFull chatFull = this.info;
            if (chatFull != null && (str2 = chatFull.about) != null) {
                str4 = str2;
            }
            if ((chatFull == null || !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory == this.historyHidden) && (((editTextEmoji = this.nameTextView) == null || this.currentChat.title.equals(editTextEmoji.getText().toString())) && (((editTextBoldCursor = this.descriptionTextView) == null || str4.equals(editTextBoldCursor.getText().toString())) && this.forum == this.currentChat.forum))) {
                return true;
            }
            builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", R.string.UserRestrictionsApplyChanges));
            if (this.isChannel) {
                i = R.string.ChannelSettingsChangedAlert;
                str = "ChannelSettingsChangedAlert";
            } else {
                i = R.string.GroupSettingsChangedAlert;
                str = "GroupSettingsChangedAlert";
            }
            builder.setMessage(LocaleController.getString(str, i));
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", R.string.ApplyTheme), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda34
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    ChatEditActivity.this.lambda$checkDiscard$44(alertDialog, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", R.string.PassportDiscard), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda35
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i2) {
                    ChatEditActivity.this.lambda$checkDiscard$45(alertDialog, i2);
                }
            });
        }
        showDialog(builder.create());
        return false;
    }

    private String getActiveUsername(TLRPC.User user) {
        String str = user.username;
        if (str != null) {
            return str;
        }
        Iterator<TLRPC.TL_username> it = user.usernames.iterator();
        while (it.hasNext()) {
            TLRPC.TL_username next = it.next();
            if (next.active) {
                return next.username;
            }
        }
        return null;
    }

    private int getAdminCount() {
        TLRPC.ChatFull chatFull = this.info;
        if (chatFull == null) {
            return 1;
        }
        int size = chatFull.participants.participants.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC.ChatParticipant chatParticipant = this.info.participants.participants.get(i2);
            if ((chatParticipant instanceof TLRPC.TL_chatParticipantAdmin) || (chatParticipant instanceof TLRPC.TL_chatParticipantCreator)) {
                i++;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$42(AlertDialog alertDialog, int i) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$43(AlertDialog alertDialog, int i) {
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$44(AlertDialog alertDialog, int i) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$45(AlertDialog alertDialog, int i) {
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
        TLRPC.TL_channelLocation tL_channelLocation = new TLRPC.TL_channelLocation();
        tL_channelLocation.address = messageMedia.address;
        tL_channelLocation.geo_point = messageMedia.geo;
        TLRPC.ChatFull chatFull = this.info;
        chatFull.location = tL_channelLocation;
        chatFull.flags |= 32768;
        updateFields(false, true);
        getMessagesController().loadFullChat(this.chatId, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view) {
        if (AndroidUtilities.isMapsInstalled(this)) {
            LocationActivity locationActivity = new LocationActivity(4);
            locationActivity.setDialogId(-this.chatId);
            TLRPC.ChatFull chatFull = this.info;
            if (chatFull != null) {
                TLRPC.ChannelLocation channelLocation = chatFull.location;
                if (channelLocation instanceof TLRPC.TL_channelLocation) {
                    locationActivity.setInitialLocation((TLRPC.TL_channelLocation) channelLocation);
                }
            }
            locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda39
                @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
                public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
                    ChatEditActivity.this.lambda$createView$10(messageMedia, i, z, i2);
                }
            });
            presentFragment(locationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view) {
        long j = this.chatId;
        TextCell textCell = this.locationCell;
        ChatEditTypeActivity chatEditTypeActivity = new ChatEditTypeActivity(j, textCell != null && textCell.getVisibility() == 0);
        chatEditTypeActivity.setInfo(this.info);
        presentFragment(chatEditTypeActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(View view) {
        ChatLinkActivity chatLinkActivity = new ChatLinkActivity(this.chatId);
        chatLinkActivity.setInfo(this.info);
        presentFragment(chatLinkActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(View view) {
        presentFragment(new ChannelColorActivity(-this.currentChat.id).setOnApplied(this));
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putInt("boostingappearance", MessagesController.getInstance(this.currentAccount).getMainSettings().getInt("boostingappearance", 0) + 1).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(RadioButtonCell[] radioButtonCellArr, BottomSheet.Builder builder, View view) {
        Integer num = (Integer) view.getTag();
        radioButtonCellArr[0].setChecked(num.intValue() == 0, true);
        radioButtonCellArr[1].setChecked(num.intValue() == 1, true);
        this.historyHidden = num.intValue() == 1;
        builder.getDismissRunnable().run();
        updateFields(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(Context context, View view) {
        RadioButtonCell radioButtonCell;
        String string;
        int i;
        String str;
        final BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setApplyTopPadding(false);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        HeaderCell headerCell = new HeaderCell(context, Theme.key_dialogTextBlue2, 23, 15, false);
        headerCell.setHeight(47);
        headerCell.setText(LocaleController.getString("ChatHistory", R.string.ChatHistory));
        linearLayout.addView(headerCell);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2));
        final RadioButtonCell[] radioButtonCellArr = new RadioButtonCell[2];
        for (int i2 = 0; i2 < 2; i2++) {
            RadioButtonCell radioButtonCell2 = new RadioButtonCell(context, true);
            radioButtonCellArr[i2] = radioButtonCell2;
            radioButtonCell2.setTag(Integer.valueOf(i2));
            radioButtonCellArr[i2].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (i2 == 0) {
                radioButtonCellArr[i2].setTextAndValue(LocaleController.getString("ChatHistoryVisible", R.string.ChatHistoryVisible), LocaleController.getString("ChatHistoryVisibleInfo", R.string.ChatHistoryVisibleInfo), true, !this.historyHidden);
            } else {
                if (ChatObject.isChannel(this.currentChat)) {
                    radioButtonCell = radioButtonCellArr[i2];
                    string = LocaleController.getString("ChatHistoryHidden", R.string.ChatHistoryHidden);
                    i = R.string.ChatHistoryHiddenInfo;
                    str = "ChatHistoryHiddenInfo";
                } else {
                    radioButtonCell = radioButtonCellArr[i2];
                    string = LocaleController.getString("ChatHistoryHidden", R.string.ChatHistoryHidden);
                    i = R.string.ChatHistoryHiddenInfo2;
                    str = "ChatHistoryHiddenInfo2";
                }
                radioButtonCell.setTextAndValue(string, LocaleController.getString(str, i), false, this.historyHidden);
            }
            linearLayout2.addView(radioButtonCellArr[i2], LayoutHelper.createLinear(-1, -2));
            radioButtonCellArr[i2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda48
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$15(radioButtonCellArr, builder, view2);
                }
            });
        }
        builder.setCustomView(linearLayout);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        this.boostsStatus = tL_premium_boostsStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        GroupColorActivity groupColorActivity = new GroupColorActivity(-this.currentChat.id);
        groupColorActivity.boostsStatus = this.boostsStatus;
        groupColorActivity.setOnApplied(this);
        presentFragment(groupColorActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$19(FrameLayout frameLayout, View view) {
        if (!this.canForum) {
            TLRPC.ChatFull chatFull = this.info;
            BulletinFactory.of(this).createSimpleBulletin(R.raw.topics, AndroidUtilities.replaceTags((chatFull == null || chatFull.linked_chat_id == 0) ? LocaleController.formatPluralString("ChannelTopicsForbidden", getMessagesController().forumUpgradeParticipantsMin, new Object[0]) : LocaleController.getString("ChannelTopicsDiscussionForbidden", R.string.ChannelTopicsDiscussionForbidden))).show();
            try {
                frameLayout.performHapticFeedback(3);
                return;
            } catch (Exception unused) {
                return;
            }
        }
        boolean z = !this.forum;
        this.forum = z;
        this.avatarImage.animateToRoundRadius(AndroidUtilities.dp(z ? 16.0f : 32.0f));
        ((TextCell) view).setChecked(this.forum);
        updateFields(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$2(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$20(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", (this.isChannel || this.currentChat.gigagroup) ? 0 : 3);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$21(View view) {
        ManageLinksActivity manageLinksActivity = new ManageLinksActivity(this.chatId, 0L, 0);
        TLRPC.ChatFull chatFull = this.info;
        manageLinksActivity.setInfo(chatFull, chatFull.exported_invite);
        presentFragment(manageLinksActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$22(View view) {
        if (ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
            presentFragment(new ChatCustomReactionsEditActivity(this.chatId, this.info));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        ChatReactionsEditActivity chatReactionsEditActivity = new ChatReactionsEditActivity(bundle);
        chatReactionsEditActivity.setInfo(this.info);
        presentFragment(chatReactionsEditActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$23(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", 1);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$24(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", this.chatId);
        bundle.putInt("type", 2);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$25(View view) {
        presentFragment(new MemberRequestsActivity(this.chatId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$26(View view) {
        presentFragment(new ChannelAffiliateProgramsFragment(-this.chatId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$27(View view) {
        presentFragment(new ChannelAdminLogActivity(this.currentChat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$28(View view) {
        presentFragment(StatisticActivity.create(this.currentChat, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$29(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("bot_id", this.userId);
        presentFragment(new ChangeUsernameActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        if (this.imageUpdater.isUploadingImage()) {
            return;
        }
        ImageLocation imageLocation = null;
        TLRPC.User user = this.userId == 0 ? null : getMessagesController().getUser(Long.valueOf(this.userId));
        if (user != null) {
            TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
            if (userProfilePhoto == null || userProfilePhoto.photo_big == null) {
                return;
            }
            PhotoViewer.getInstance().setParentActivity(this);
            TLRPC.UserProfilePhoto userProfilePhoto2 = user.photo;
            int i = userProfilePhoto2.dc_id;
            if (i != 0) {
                userProfilePhoto2.photo_big.dc_id = i;
            }
            PhotoViewer.getInstance().openPhoto(user.photo.photo_big, this.provider);
            return;
        }
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        TLRPC.ChatPhoto chatPhoto = chat.photo;
        if (chatPhoto == null || chatPhoto.photo_big == null) {
            return;
        }
        PhotoViewer.getInstance().setParentActivity(this);
        TLRPC.ChatPhoto chatPhoto2 = chat.photo;
        int i2 = chatPhoto2.dc_id;
        if (i2 != 0) {
            chatPhoto2.photo_big.dc_id = i2;
        }
        TLRPC.ChatFull chatFull = this.info;
        if (chatFull != null) {
            TLRPC.Photo photo = chatFull.chat_photo;
            if ((photo instanceof TLRPC.TL_photo) && !photo.video_sizes.isEmpty()) {
                imageLocation = ImageLocation.getForPhoto(this.info.chat_photo.video_sizes.get(0), this.info.chat_photo);
            }
        }
        PhotoViewer.getInstance().openPhotoWithVideo(chat.photo.photo_big, imageLocation, this.provider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$30(View view) {
        presentFragment(new AffiliateProgramFragment(this.userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$31(View view) {
        Browser.openUrl(view.getContext(), "https://t.me/BotFather?start=" + getActiveUsername(this.currentUser) + "-intro");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$32(View view) {
        Browser.openUrl(view.getContext(), "https://t.me/BotFather?start=" + getActiveUsername(this.currentUser) + "-commands");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$33(View view) {
        Browser.openUrl(view.getContext(), "https://t.me/BotFather?start=" + getActiveUsername(this.currentUser));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$34(View view) {
        BotVerifySheet.openVerify(this.currentAccount, this.userId, this.userInfo.bot_info.verifier_settings);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$35(BotStarsController botStarsController, View view) {
        if (botStarsController.isStarsBalanceAvailable(this.userId)) {
            presentFragment(new BotStarsActivity(1, this.userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$36(BotStarsController botStarsController, View view) {
        if (botStarsController.isStarsBalanceAvailable(this.userId)) {
            presentFragment(new BotStarsActivity(0, this.userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$37(boolean z) {
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, Long.valueOf(-this.chatId));
        } else {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        }
        lambda$onBackPressed$323();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needDeleteDialog, Long.valueOf(-this.currentChat.id), null, this.currentChat, Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$38(View view) {
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, true, false, this.currentChat, null, false, true, false, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda40
            @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
            public final void run(boolean z) {
                ChatEditActivity.this.lambda$createView$37(z);
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        this.avatarImage.setImageDrawable(this.avatarDrawable);
        this.setAvatarCell.setTextAndIcon((CharSequence) LocaleController.getString("ChatSetPhotoOrVideo", R.string.ChatSetPhotoOrVideo), R.drawable.msg_addphoto, true);
        TLRPC.User user = this.currentUser;
        if (user != null) {
            user.photo = null;
            getMessagesController().putUser(this.currentUser, true);
        }
        this.hasUploadedPhoto = true;
        if (this.cameraDrawable == null) {
            int i = R.raw.camera_outline;
            this.cameraDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
        }
        this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
        this.setAvatarCell.imageView.setAnimation(this.cameraDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$createView$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        this.avatar = null;
        if (this.userId == 0) {
            MessagesController.getInstance(this.currentAccount).changeChatAvatar(this.chatId, null, null, null, null, 0.0d, null, null, null, null);
        } else {
            TLRPC.TL_photos_updateProfilePhoto tL_photos_updateProfilePhoto = new TLRPC.TL_photos_updateProfilePhoto();
            tL_photos_updateProfilePhoto.bot = getMessagesController().getInputUser(this.userId);
            tL_photos_updateProfilePhoto.flags |= 2;
            tL_photos_updateProfilePhoto.id = new TLRPC.TL_inputPhotoEmpty();
            getConnectionsManager().sendRequest(tL_photos_updateProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda49
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChatEditActivity.this.lambda$createView$5(tLObject, tL_error);
                }
            });
        }
        showAvatarProgress(false, true);
        BackupImageView backupImageView = this.avatarImage;
        AvatarDrawable avatarDrawable = this.avatarDrawable;
        Object obj = this.currentUser;
        if (obj == null) {
            obj = this.currentChat;
        }
        backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, obj);
        this.cameraDrawable.setCurrentFrame(0);
        this.setAvatarCell.imageView.playAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(DialogInterface dialogInterface) {
        if (this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCurrentFrame(0, false);
        } else {
            this.cameraDrawable.setCustomEndFrame(86);
            this.setAvatarCell.imageView.playAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view) {
        this.imageUpdater.openMenu(this.avatar != null, new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$createView$6();
            }
        }, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda42
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                ChatEditActivity.this.lambda$createView$7(dialogInterface);
            }
        }, 0);
        this.cameraDrawable.setCurrentFrame(0);
        this.cameraDrawable.setCustomEndFrame(43);
        this.setAvatarCell.imageView.playAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$9(TextView textView, int i, KeyEvent keyEvent) {
        View view;
        if (i != 6 || (view = this.doneButton) == null) {
            return false;
        }
        view.performClick();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$39() {
        this.hasUploadedPhoto = true;
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_AVATAR));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$40(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$didUploadPhoto$39();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$41(TLRPC.PhotoSize photoSize, TLRPC.InputFile inputFile, TLRPC.InputFile inputFile2, TLRPC.VideoSize videoSize, TLRPC.PhotoSize photoSize2, double d, String str) {
        TLRPC.FileLocation fileLocation = photoSize.location;
        this.avatar = fileLocation;
        if (inputFile == null && inputFile2 == null && videoSize == null) {
            BackupImageView backupImageView = this.avatarImage;
            ImageLocation forLocal = ImageLocation.getForLocal(fileLocation);
            AvatarDrawable avatarDrawable = this.avatarDrawable;
            Object obj = this.currentUser;
            if (obj == null) {
                obj = this.currentChat;
            }
            backupImageView.setImage(forLocal, "50_50", avatarDrawable, obj);
            this.setAvatarCell.setTextAndIcon((CharSequence) LocaleController.getString("ChatSetNewPhoto", R.string.ChatSetNewPhoto), R.drawable.msg_addphoto, true);
            if (this.cameraDrawable == null) {
                int i = R.raw.camera_outline;
                this.cameraDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
            }
            this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
            this.setAvatarCell.imageView.setAnimation(this.cameraDrawable);
            showAvatarProgress(true, false);
            return;
        }
        long j = 0;
        if (this.userId != 0) {
            TLRPC.User user = this.currentUser;
            if (user != null) {
                user.photo = new TLRPC.TL_userProfilePhoto();
                TLRPC.UserProfilePhoto userProfilePhoto = this.currentUser.photo;
                if (inputFile != null) {
                    j = inputFile.id;
                } else if (inputFile2 != null) {
                    j = inputFile2.id;
                }
                userProfilePhoto.photo_id = j;
                userProfilePhoto.photo_big = photoSize2.location;
                userProfilePhoto.photo_small = photoSize.location;
                getMessagesController().putUser(this.currentUser, true);
            }
            TLRPC.TL_photos_uploadProfilePhoto tL_photos_uploadProfilePhoto = new TLRPC.TL_photos_uploadProfilePhoto();
            if (inputFile != null) {
                tL_photos_uploadProfilePhoto.file = inputFile;
                tL_photos_uploadProfilePhoto.flags |= 1;
            }
            if (inputFile2 != null) {
                tL_photos_uploadProfilePhoto.video = inputFile2;
                int i2 = tL_photos_uploadProfilePhoto.flags;
                tL_photos_uploadProfilePhoto.video_start_ts = d;
                tL_photos_uploadProfilePhoto.flags = i2 | 6;
            }
            if (videoSize != null) {
                tL_photos_uploadProfilePhoto.video_emoji_markup = videoSize;
                tL_photos_uploadProfilePhoto.flags |= 16;
            }
            tL_photos_uploadProfilePhoto.bot = getMessagesController().getInputUser(this.currentUser);
            tL_photos_uploadProfilePhoto.flags |= 32;
            getConnectionsManager().sendRequest(tL_photos_uploadProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda47
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChatEditActivity.this.lambda$didUploadPhoto$40(tLObject, tL_error);
                }
            });
        } else {
            getMessagesController().changeChatAvatar(this.chatId, null, inputFile, inputFile2, videoSize, d, str, photoSize.location, photoSize2.location, null);
        }
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$52() {
        BackupImageView backupImageView = this.avatarImage;
        if (backupImageView != null) {
            backupImageView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLinksCount$0(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            this.info.invitesCount = ((TLRPC.TL_messages_exportedChatInvites) tLObject).count;
            getMessagesStorage().saveChatLinksCount(this.chatId, this.info.invitesCount);
            updateFields(false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLinksCount$1(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$loadLinksCount$0(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$46() {
        this.progressDialog.dismiss();
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$47(TL_bots.setBotInfo setbotinfo, TLObject tLObject, TLRPC.TL_error tL_error) {
        TLRPC.UserFull userFull = this.userInfo;
        if (userFull != null) {
            userFull.about = setbotinfo.about;
            getMessagesStorage().updateUserInfo(this.userInfo, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$processDone$46();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$48(int i, DialogInterface dialogInterface) {
        this.donePressed = false;
        this.progressDialog = null;
        getConnectionsManager().cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$49(long j) {
        if (j == 0) {
            this.donePressed = false;
            return;
        }
        this.chatId = j;
        this.currentChat = getMessagesController().getChat(Long.valueOf(j));
        this.donePressed = false;
        TLRPC.ChatFull chatFull = this.info;
        if (chatFull != null) {
            chatFull.hidden_prehistory = true;
        }
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$50(DialogInterface dialogInterface) {
        this.createAfterUpload = false;
        this.progressDialog = null;
        this.donePressed = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateHistoryShow$51(ArrayList arrayList, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.historyCell.setAlpha(floatValue);
        float f = 1.0f - floatValue;
        this.historyCell.setTranslationY(((-r0.getHeight()) / 2.0f) * f);
        this.historyCell.setScaleY((floatValue * 0.8f) + 0.2f);
        for (int i = 0; i < arrayList.size(); i++) {
            ((View) arrayList.get(i)).setTranslationY((-this.historyCell.getHeight()) * f);
        }
    }

    private void loadLinksCount() {
        TLRPC.TL_messages_getExportedChatInvites tL_messages_getExportedChatInvites = new TLRPC.TL_messages_getExportedChatInvites();
        tL_messages_getExportedChatInvites.peer = getMessagesController().getInputPeer(-this.chatId);
        tL_messages_getExportedChatInvites.admin_id = getMessagesController().getInputUser(getUserConfig().getCurrentUser());
        tL_messages_getExportedChatInvites.limit = 0;
        getConnectionsManager().sendRequest(tL_messages_getExportedChatInvites, new RequestDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda37
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ChatEditActivity.this.lambda$loadLinksCount$1(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDone() {
        EditTextEmoji editTextEmoji;
        String str;
        String str2;
        if (this.donePressed || (editTextEmoji = this.nameTextView) == null) {
            return;
        }
        if (editTextEmoji.length() == 0) {
            Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.nameTextView);
            return;
        }
        this.donePressed = true;
        String str3 = "";
        if (this.currentUser != null) {
            final TL_bots.setBotInfo setbotinfo = new TL_bots.setBotInfo();
            setbotinfo.bot = getMessagesController().getInputUser(this.currentUser);
            setbotinfo.flags |= 4;
            setbotinfo.lang_code = "";
            if (!this.currentUser.first_name.equals(this.nameTextView.getText().toString())) {
                setbotinfo.name = this.nameTextView.getText().toString();
                setbotinfo.flags |= 8;
            }
            TLRPC.UserFull userFull = this.userInfo;
            if (userFull != null && (str2 = userFull.about) != null) {
                str3 = str2;
            }
            EditTextBoldCursor editTextBoldCursor = this.descriptionTextView;
            if (editTextBoldCursor != null && !str3.equals(editTextBoldCursor.getText().toString())) {
                setbotinfo.about = this.descriptionTextView.getText().toString();
                setbotinfo.flags = 1 | setbotinfo.flags;
            }
            this.progressDialog = new AlertDialog(getParentActivity(), 3);
            final int sendRequest = getConnectionsManager().sendRequest(setbotinfo, new RequestDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda43
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChatEditActivity.this.lambda$processDone$47(setbotinfo, tLObject, tL_error);
                }
            });
            this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda44
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    ChatEditActivity.this.lambda$processDone$48(sendRequest, dialogInterface);
                }
            });
            this.progressDialog.show();
            return;
        }
        if (!ChatObject.isChannel(this.currentChat) && (!this.historyHidden || this.forum)) {
            getMessagesController().convertToMegaGroup(getParentActivity(), this.chatId, this, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda45
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    ChatEditActivity.this.lambda$processDone$49(j);
                }
            });
            return;
        }
        if (this.info != null && ChatObject.isChannel(this.currentChat)) {
            TLRPC.ChatFull chatFull = this.info;
            boolean z = chatFull.hidden_prehistory;
            boolean z2 = this.historyHidden;
            if (z != z2) {
                chatFull.hidden_prehistory = z2;
                getMessagesController().toggleChannelInvitesHistory(this.chatId, this.historyHidden);
            }
        }
        if (this.imageUpdater.isUploadingImage()) {
            this.createAfterUpload = true;
            AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            this.progressDialog = alertDialog;
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda46
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    ChatEditActivity.this.lambda$processDone$50(dialogInterface);
                }
            });
            this.progressDialog.show();
            return;
        }
        if (!this.currentChat.title.equals(this.nameTextView.getText().toString())) {
            getMessagesController().changeChatTitle(this.chatId, this.nameTextView.getText().toString());
        }
        TLRPC.ChatFull chatFull2 = this.info;
        if (chatFull2 != null && (str = chatFull2.about) != null) {
            str3 = str;
        }
        EditTextBoldCursor editTextBoldCursor2 = this.descriptionTextView;
        if (editTextBoldCursor2 != null && !str3.equals(editTextBoldCursor2.getText().toString())) {
            getMessagesController().updateChatAbout(this.chatId, this.descriptionTextView.getText().toString(), this.info);
        }
        if (this.forum != this.currentChat.forum) {
            getMessagesController().toggleChannelForum(this.chatId, this.forum);
            List fragmentStack = getParentLayout().getFragmentStack();
            for (int i = 0; i < fragmentStack.size(); i++) {
                if ((fragmentStack.get(i) instanceof ChatActivity) && ((ChatActivity) fragmentStack.get(i)).getArguments().getLong("chat_id") == this.chatId) {
                    getParentLayout().removeFragmentFromStack(i);
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", this.chatId);
                    getParentLayout().addFragmentToStack(TopicsFragment.getTopicsOrChat(this, bundle), i);
                }
            }
        }
        lambda$onBackPressed$323();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setAvatar() {
        boolean z;
        TextCell textCell;
        int i;
        String str;
        if (this.avatarImage == null || this.hasUploadedPhoto) {
            return;
        }
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        TLRPC.User user = this.userId == 0 ? null : getMessagesController().getUser(Long.valueOf(this.userId));
        if (chat == null && user == null) {
            return;
        }
        this.currentUser = user;
        this.currentChat = chat;
        if (user == null ? chat.photo == null : user.photo == null) {
            this.avatarImage.setImageDrawable(this.avatarDrawable);
        } else {
            TLRPC.Chat chat2 = user != null ? user : chat;
            this.avatar = user != null ? user.photo.photo_small : chat.photo.photo_small;
            ImageLocation forUserOrChat = ImageLocation.getForUserOrChat(chat2, 1);
            this.avatarImage.setForUserOrChat(chat2, this.avatarDrawable);
            if (forUserOrChat != null) {
                z = true;
                if (this.setAvatarCell != null) {
                    if (z || this.imageUpdater.isUploadingImage()) {
                        textCell = this.setAvatarCell;
                        i = R.string.ChatSetNewPhoto;
                        str = "ChatSetNewPhoto";
                    } else {
                        textCell = this.setAvatarCell;
                        i = R.string.ChatSetPhotoOrVideo;
                        str = "ChatSetPhotoOrVideo";
                    }
                    textCell.setTextAndIcon((CharSequence) LocaleController.getString(str, i), R.drawable.msg_addphoto, true);
                    if (this.cameraDrawable == null) {
                        int i2 = R.raw.camera_outline;
                        this.cameraDrawable = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
                    }
                    this.setAvatarCell.imageView.setTranslationX(-AndroidUtilities.dp(8.0f));
                    this.setAvatarCell.imageView.setAnimation(this.cameraDrawable);
                }
                if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                    PhotoViewer.getInstance().checkCurrentImageVisibility();
                }
                if (this.channelAffiliateProgramsCell == null && getMessagesController().starrefConnectAllowed && ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
                    this.channelAffiliateProgramsCell.setVisibility(0);
                    return;
                }
                return;
            }
        }
        z = false;
        if (this.setAvatarCell != null) {
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().checkCurrentImageVisibility();
        }
        if (this.channelAffiliateProgramsCell == null) {
        }
    }

    private void showAvatarProgress(final boolean z, boolean z2) {
        if (this.avatarProgressView == null) {
            return;
        }
        AnimatorSet animatorSet = this.avatarAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.avatarAnimation = null;
        }
        if (!z2) {
            if (z) {
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
                this.avatarOverlay.setAlpha(1.0f);
                this.avatarOverlay.setVisibility(0);
                return;
            }
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
            this.avatarOverlay.setAlpha(0.0f);
            this.avatarOverlay.setVisibility(4);
            return;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.avatarAnimation = animatorSet2;
        if (z) {
            this.avatarProgressView.setVisibility(0);
            this.avatarOverlay.setVisibility(0);
            AnimatorSet animatorSet3 = this.avatarAnimation;
            RadialProgressView radialProgressView = this.avatarProgressView;
            Property property = View.ALPHA;
            animatorSet3.playTogether(ObjectAnimator.ofFloat(radialProgressView, (Property<RadialProgressView, Float>) property, 1.0f), ObjectAnimator.ofFloat(this.avatarOverlay, (Property<View, Float>) property, 1.0f));
        } else {
            RadialProgressView radialProgressView2 = this.avatarProgressView;
            Property property2 = View.ALPHA;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(radialProgressView2, (Property<RadialProgressView, Float>) property2, 0.0f), ObjectAnimator.ofFloat(this.avatarOverlay, (Property<View, Float>) property2, 0.0f));
        }
        this.avatarAnimation.setDuration(180L);
        this.avatarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ChatEditActivity.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                ChatEditActivity.this.avatarAnimation = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ChatEditActivity.this.avatarAnimation == null || ChatEditActivity.this.avatarProgressView == null) {
                    return;
                }
                if (!z) {
                    ChatEditActivity.this.avatarProgressView.setVisibility(4);
                    ChatEditActivity.this.avatarOverlay.setVisibility(4);
                }
                ChatEditActivity.this.avatarAnimation = null;
            }
        });
        this.avatarAnimation.start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
    
        if (java.lang.Math.max(r0 == null ? 0 : r0.participants_count, r6.currentChat.participants_count) >= getMessagesController().forumUpgradeParticipantsMin) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateCanForum() {
        boolean z;
        TextCell textCell;
        if (this.userId != 0) {
            this.canForum = false;
            return;
        }
        if (!this.forum) {
            TLRPC.ChatFull chatFull = this.info;
        }
        TLRPC.ChatFull chatFull2 = this.info;
        if (chatFull2 == null || chatFull2.linked_chat_id == 0) {
            z = true;
            this.canForum = z;
            textCell = this.forumsCell;
            if (textCell == null) {
                textCell.getCheckBox().setIcon(this.canForum ? 0 : R.drawable.permission_locked);
                return;
            }
            return;
        }
        z = false;
        this.canForum = z;
        textCell = this.forumsCell;
        if (textCell == null) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x01ea, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0238, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0242, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x024c, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0263, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x026d, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0277, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0364, code lost:
    
        if (r5.getVisibility() == 0) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x047e, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x047c, code lost:
    
        if (r5.getVisibility() == 0) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x04d1, code lost:
    
        if (r4.getVisibility() == 0) goto L268;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0506, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0504, code lost:
    
        if (r4.getVisibility() == 0) goto L268;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01d6, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0279, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01e0, code lost:
    
        if (r5.getVisibility() == 0) goto L144;
     */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0499  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateFields(boolean z, boolean z2) {
        TextCell textCell;
        String string;
        int i;
        boolean z3;
        int i2;
        TextCell textCell2;
        String string2;
        String format;
        int i3;
        boolean z4;
        int i4;
        String str;
        TLRPC.ChatFull chatFull;
        int i5;
        String str2;
        String string3;
        TextCell textCell3;
        String string4;
        int i6;
        boolean z5;
        TextCell textCell4;
        TextCell textCell5;
        TextCell textCell6;
        TLRPC.Chat chat;
        if (z && (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) != null) {
            this.currentChat = chat;
        }
        boolean z6 = !ChatObject.isPublic(this.currentChat);
        TextInfoPrivacyCell textInfoPrivacyCell = this.settingsSectionCell;
        if (textInfoPrivacyCell != null) {
            textInfoPrivacyCell.setVisibility((this.typeCell != null || ((textCell4 = this.linkedCell) != null && textCell4.getVisibility() == 0) || (((textCell5 = this.historyCell) != null && textCell5.getVisibility() == 0) || ((textCell6 = this.locationCell) != null && textCell6.getVisibility() == 0))) ? 0 : 8);
        }
        TextCell textCell7 = this.logCell;
        if (textCell7 != null) {
            textCell7.setVisibility(ChatObject.isChannel(this.currentChat) ? 0 : 8);
        }
        TextCell textCell8 = this.linkedCell;
        if (textCell8 != null) {
            TLRPC.ChatFull chatFull2 = this.info;
            if (chatFull2 != null && (this.isChannel || chatFull2.linked_chat_id != 0)) {
                textCell8.setVisibility(0);
                if (this.info.linked_chat_id == 0) {
                    this.linkedCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("Discussion", R.string.Discussion), (CharSequence) LocaleController.getString("DiscussionInfoShort", R.string.DiscussionInfoShort), R.drawable.msg_discuss, true);
                } else {
                    TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(this.info.linked_chat_id));
                    if (chat2 == null) {
                        textCell8 = this.linkedCell;
                    } else if (this.isChannel) {
                        String publicUsername = ChatObject.getPublicUsername(chat2);
                        if (TextUtils.isEmpty(publicUsername)) {
                            this.linkedCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("Discussion", R.string.Discussion), (CharSequence) chat2.title, R.drawable.msg_discuss, true);
                        } else {
                            this.linkedCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("Discussion", R.string.Discussion), (CharSequence) ("@" + publicUsername), R.drawable.msg_discuss, true);
                        }
                    } else {
                        String publicUsername2 = ChatObject.getPublicUsername(chat2);
                        if (TextUtils.isEmpty(publicUsername2)) {
                            TextCell textCell9 = this.linkedCell;
                            String string5 = LocaleController.getString("LinkedChannel", R.string.LinkedChannel);
                            String str3 = chat2.title;
                            int i7 = R.drawable.msg_channel;
                            TextCell textCell10 = this.forumsCell;
                            textCell9.setTextAndValueAndIcon(string5, str3, i7, textCell10 != null && textCell10.getVisibility() == 0);
                        } else {
                            TextCell textCell11 = this.linkedCell;
                            String string6 = LocaleController.getString("LinkedChannel", R.string.LinkedChannel);
                            String str4 = "@" + publicUsername2;
                            int i8 = R.drawable.msg_channel;
                            TextCell textCell12 = this.forumsCell;
                            textCell11.setTextAndValueAndIcon(string6, str4, i8, textCell12 != null && textCell12.getVisibility() == 0);
                        }
                    }
                }
            }
            textCell8.setVisibility(8);
        }
        TextCell textCell13 = this.locationCell;
        if (textCell13 != null) {
            TLRPC.ChatFull chatFull3 = this.info;
            if (chatFull3 == null || !chatFull3.can_set_location) {
                textCell13.setVisibility(8);
            } else {
                textCell13.setVisibility(0);
                TLRPC.ChannelLocation channelLocation = this.info.location;
                if (channelLocation instanceof TLRPC.TL_channelLocation) {
                    this.locationCell.setTextAndValue(LocaleController.getString("AttachLocation", R.string.AttachLocation), ((TLRPC.TL_channelLocation) channelLocation).address, z2, true);
                } else {
                    this.locationCell.setTextAndValue(LocaleController.getString("AttachLocation", R.string.AttachLocation), "Unknown address", z2, true);
                }
            }
        }
        if (this.typeCell != null) {
            TLRPC.ChatFull chatFull4 = this.info;
            if (chatFull4 == null || !(chatFull4.location instanceof TLRPC.TL_channelLocation)) {
                boolean z7 = this.currentChat.noforwards;
                if (this.isChannel) {
                    if (!z6) {
                        i5 = R.string.TypePublic;
                        str2 = "TypePublic";
                    } else if (z7) {
                        i5 = R.string.TypePrivateRestrictedForwards;
                        str2 = "TypePrivateRestrictedForwards";
                    } else {
                        i5 = R.string.TypePrivate;
                        str2 = "TypePrivate";
                    }
                } else if (!z6) {
                    i5 = R.string.TypePublicGroup;
                    str2 = "TypePublicGroup";
                } else if (z7) {
                    i5 = R.string.TypePrivateGroupRestrictedForwards;
                    str2 = "TypePrivateGroupRestrictedForwards";
                } else {
                    i5 = R.string.TypePrivateGroup;
                    str2 = "TypePrivateGroup";
                }
                string3 = LocaleController.getString(str2, i5);
                if (this.isChannel) {
                    textCell3 = this.typeCell;
                    string4 = LocaleController.getString("ChannelType", R.string.ChannelType);
                    i6 = R.drawable.msg_channel;
                    TextCell textCell14 = this.historyCell;
                    if (textCell14 != null) {
                    }
                    TextCell textCell15 = this.linkedCell;
                    if (textCell15 != null) {
                    }
                    TextCell textCell16 = this.forumsCell;
                    if (textCell16 != null) {
                    }
                    z5 = false;
                } else {
                    textCell3 = this.typeCell;
                    string4 = LocaleController.getString("GroupType", R.string.GroupType);
                    i6 = R.drawable.msg_groups;
                    TextCell textCell17 = this.historyCell;
                    if (textCell17 != null) {
                    }
                    TextCell textCell18 = this.linkedCell;
                    if (textCell18 != null) {
                    }
                    TextCell textCell19 = this.forumsCell;
                    if (textCell19 != null) {
                    }
                    z5 = false;
                }
            } else {
                if (z6) {
                    string3 = LocaleController.getString("TypeLocationGroupEdit", R.string.TypeLocationGroupEdit);
                } else {
                    string3 = String.format("https://" + getMessagesController().linkPrefix + "/%s", ChatObject.getPublicUsername(this.currentChat));
                }
                textCell3 = this.typeCell;
                string4 = LocaleController.getString("TypeLocationGroup", R.string.TypeLocationGroup);
                i6 = R.drawable.msg_channel;
                TextCell textCell20 = this.historyCell;
                if (textCell20 != null) {
                }
                TextCell textCell21 = this.linkedCell;
                if (textCell21 != null) {
                }
                TextCell textCell22 = this.forumsCell;
                if (textCell22 != null) {
                }
                z5 = false;
            }
            textCell3.setTextAndValueAndIcon(string4, string3, i6, z5);
        }
        if (this.historyCell != null) {
            if (!this.historyHidden || this.forum) {
                i4 = R.string.ChatHistoryVisible;
                str = "ChatHistoryVisible";
            } else {
                i4 = R.string.ChatHistoryHidden;
                str = "ChatHistoryHidden";
            }
            this.historyCell.setTextAndValueAndIcon(LocaleController.getString("ChatHistoryShort", R.string.ChatHistoryShort), LocaleController.getString(str, i4), z2, R.drawable.msg_discuss, this.forumsCell != null);
            this.historyCell.setEnabled(!this.forum);
            updateHistoryShow(!this.forum && z6 && ((chatFull = this.info) == null || chatFull.linked_chat_id == 0) && (chatFull == null || !(chatFull.location instanceof TLRPC.TL_channelLocation)), z2);
        }
        TextCell textCell23 = this.membersCell;
        if (textCell23 != null) {
            if (this.info != null) {
                TextCell textCell24 = this.memberRequestsCell;
                if (textCell24 != null) {
                    if (textCell24.getParent() == null) {
                        this.infoContainer.addView(this.memberRequestsCell, this.infoContainer.indexOfChild(this.membersCell) + 1, LayoutHelper.createLinear(-1, -2));
                    }
                    this.memberRequestsCell.setVisibility(this.info.requests_pending > 0 ? 0 : 8);
                }
                if (this.isChannel) {
                    this.membersCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers), (CharSequence) String.format("%d", Integer.valueOf(this.info.participants_count)), R.drawable.msg_groups, true);
                    textCell2 = this.blockCell;
                    string2 = LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist);
                    TLRPC.ChatFull chatFull5 = this.info;
                    format = String.format("%d", Integer.valueOf(Math.max(chatFull5.banned_count, chatFull5.kicked_count)));
                    i3 = R.drawable.msg_user_remove;
                    TextCell textCell25 = this.logCell;
                    if (textCell25 != null) {
                    }
                    z4 = false;
                } else {
                    if (ChatObject.isChannel(this.currentChat)) {
                        this.membersCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("ChannelMembers", R.string.ChannelMembers), (CharSequence) String.format("%d", Integer.valueOf(this.info.participants_count)), R.drawable.msg_groups, true);
                    } else {
                        this.membersCell.setTextAndValueAndIcon(LocaleController.getString("ChannelMembers", R.string.ChannelMembers), String.format("%d", Integer.valueOf(this.info.participants.participants.size())), R.drawable.msg_groups, this.memberRequestsCell.getVisibility() == 0);
                    }
                    TLRPC.Chat chat3 = this.currentChat;
                    if (chat3.gigagroup) {
                        TextCell textCell26 = this.blockCell;
                        String string7 = LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist);
                        TLRPC.ChatFull chatFull6 = this.info;
                        String format2 = String.format("%d", Integer.valueOf(Math.max(chatFull6.banned_count, chatFull6.kicked_count)));
                        int i9 = R.drawable.msg_user_remove;
                        TextCell textCell27 = this.logCell;
                        textCell26.setTextAndValueAndIcon(string7, format2, i9, textCell27 != null && textCell27.getVisibility() == 0);
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights = chat3.default_banned_rights;
                        if (tL_chatBannedRights != null) {
                            i2 = (!tL_chatBannedRights.send_plain ? 1 : 0) + ChatUsersActivity.getSendMediaSelectedCount(tL_chatBannedRights);
                            TLRPC.TL_chatBannedRights tL_chatBannedRights2 = this.currentChat.default_banned_rights;
                            if (!tL_chatBannedRights2.pin_messages) {
                                i2++;
                            }
                            if (!tL_chatBannedRights2.invite_users) {
                                i2++;
                            }
                            if (this.forum && !tL_chatBannedRights2.manage_topics) {
                                i2++;
                            }
                            if (!tL_chatBannedRights2.change_info) {
                                i2++;
                            }
                        } else {
                            i2 = this.forum ? 14 : 13;
                        }
                        this.blockCell.setTextAndValueAndIcon(LocaleController.getString("ChannelPermissions", R.string.ChannelPermissions), String.format("%d/%d", Integer.valueOf(i2), Integer.valueOf(this.forum ? 14 : 13)), z2, R.drawable.msg_permissions, true);
                    }
                    textCell2 = this.memberRequestsCell;
                    if (textCell2 != null) {
                        string2 = LocaleController.getString("MemberRequests", R.string.MemberRequests);
                        format = String.format("%d", Integer.valueOf(this.info.requests_pending));
                        i3 = R.drawable.msg_requests;
                        TextCell textCell28 = this.logCell;
                        if (textCell28 != null) {
                        }
                        z4 = false;
                    }
                    this.adminCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), (CharSequence) String.format("%d", Integer.valueOf(!ChatObject.isChannel(this.currentChat) ? this.info.admins_count : getAdminCount())), R.drawable.msg_admins, true);
                }
                textCell2.setTextAndValueAndIcon(string2, format, i3, z4);
                this.adminCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), (CharSequence) String.format("%d", Integer.valueOf(!ChatObject.isChannel(this.currentChat) ? this.info.admins_count : getAdminCount())), R.drawable.msg_admins, true);
            } else {
                if (this.isChannel) {
                    textCell23.setTextAndIcon((CharSequence) LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers), R.drawable.msg_groups, true);
                    textCell = this.blockCell;
                    string = LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist);
                    i = R.drawable.msg_chats_remove;
                    TextCell textCell29 = this.logCell;
                    if (textCell29 != null) {
                    }
                    z3 = false;
                } else {
                    String string8 = LocaleController.getString("ChannelMembers", R.string.ChannelMembers);
                    int i10 = R.drawable.msg_groups;
                    TextCell textCell30 = this.logCell;
                    textCell23.setTextAndIcon(string8, i10, textCell30 != null && textCell30.getVisibility() == 0);
                    if (this.currentChat.gigagroup) {
                        textCell = this.blockCell;
                        string = LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist);
                        i = R.drawable.msg_chats_remove;
                        TextCell textCell31 = this.logCell;
                        if (textCell31 != null) {
                        }
                        z3 = false;
                    } else {
                        this.blockCell.setTextAndIcon((CharSequence) LocaleController.getString("ChannelPermissions", R.string.ChannelPermissions), R.drawable.msg_permissions, true);
                        this.adminCell.setTextAndIcon((CharSequence) LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), R.drawable.msg_admins, true);
                    }
                }
                textCell.setTextAndIcon(string, i, z3);
                this.adminCell.setTextAndIcon((CharSequence) LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators), R.drawable.msg_admins, true);
            }
            this.reactionsCell.setVisibility(ChatObject.canChangeChatInfo(this.currentChat) ? 0 : 8);
            updateReactionsCell(z2);
            if (this.info == null || !ChatObject.canUserDoAdminAction(this.currentChat, 3) || (!z6 && this.currentChat.creator)) {
                this.inviteLinksCell.setVisibility(8);
            } else if (this.info.invitesCount > 0) {
                this.inviteLinksCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("InviteLinks", R.string.InviteLinks), (CharSequence) Integer.toString(this.info.invitesCount), R.drawable.msg_link2, true);
            } else {
                this.inviteLinksCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString("InviteLinks", R.string.InviteLinks), (CharSequence) "1", R.drawable.msg_link2, true);
            }
        }
        TextCell textCell32 = this.stickersCell;
        if (textCell32 == null || this.info == null) {
            return;
        }
        String string9 = LocaleController.getString(R.string.GroupStickers);
        TLRPC.StickerSet stickerSet = this.info.stickerset;
        textCell32.setTextAndValueAndIcon((CharSequence) string9, (CharSequence) (stickerSet != null ? stickerSet.title : LocaleController.getString(R.string.Add)), R.drawable.msg_sticker, false);
    }

    private void updateHistoryShow(final boolean z, boolean z2) {
        ValueAnimator valueAnimator = this.updateHistoryShowAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.historyCell.getAlpha() <= 0.0f && !z) {
            this.historyCell.setVisibility(8);
            updateColorCell();
            return;
        }
        if (this.historyCell.getVisibility() == 0 && this.historyCell.getAlpha() >= 1.0f && z) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        boolean z3 = false;
        for (int i = 0; i < this.typeEditContainer.getChildCount(); i++) {
            if (!z3 && this.typeEditContainer.getChildAt(i) == this.historyCell) {
                z3 = true;
            } else if (z3) {
                arrayList.add(this.typeEditContainer.getChildAt(i));
            }
        }
        boolean z4 = false;
        for (int i2 = 0; i2 < this.linearLayout.getChildCount(); i2++) {
            if (!z4 && this.linearLayout.getChildAt(i2) == this.typeEditContainer) {
                z4 = true;
            } else if (z4) {
                arrayList.add(this.linearLayout.getChildAt(i2));
            }
        }
        if (this.historyCell.getVisibility() != 0) {
            this.historyCell.setAlpha(0.0f);
            this.historyCell.setTranslationY((-r6.getHeight()) / 2.0f);
        }
        this.historyCell.setVisibility(0);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            ((View) arrayList.get(i3)).setTranslationY((-this.historyCell.getHeight()) * (1.0f - this.historyCell.getAlpha()));
        }
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.historyCell.getAlpha(), z ? 1.0f : 0.0f);
            this.updateHistoryShowAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda30
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ChatEditActivity.this.lambda$updateHistoryShow$51(arrayList, valueAnimator2);
                }
            });
            this.updateHistoryShowAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ChatEditActivity.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatEditActivity.this.historyCell.setVisibility(z ? 0 : 8);
                    for (int i4 = 0; i4 < arrayList.size(); i4++) {
                        ((View) arrayList.get(i4)).setTranslationY(0.0f);
                    }
                }
            });
            this.updateHistoryShowAnimator.setDuration(320L);
            this.updateHistoryShowAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.updateHistoryShowAnimator.start();
            return;
        }
        this.historyCell.setAlpha(z ? 1.0f : 0.0f);
        this.historyCell.setTranslationY(((-r13.getHeight()) / 2.0f) * (z ? 0.0f : 1.0f));
        this.historyCell.setScaleY(((z ? 1.0f : 0.0f) * 0.8f) + 0.2f);
        this.historyCell.setVisibility(z ? 0 : 8);
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            ((View) arrayList.get(i4)).setTranslationY(0.0f);
        }
        this.updateHistoryShowAnimator = null;
    }

    private void updatePublicLinksCount() {
        if (this.publicLinkCell == null) {
            return;
        }
        if (this.currentUser.usernames.size() > 1) {
            Iterator<TLRPC.TL_username> it = this.currentUser.usernames.iterator();
            int i = 0;
            while (it.hasNext()) {
                if (it.next().active) {
                    i++;
                }
            }
            this.publicLinkCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString(R.string.BotPublicLinks), (CharSequence) LocaleController.formatString(R.string.BotPublicLinksCount, Integer.valueOf(i), Integer.valueOf(this.currentUser.usernames.size())), R.drawable.msg_link2, true);
            return;
        }
        this.publicLinkCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString(R.string.BotPublicLink), (CharSequence) ("t.me/" + this.currentUser.username), R.drawable.msg_link2, true);
    }

    private void updateReactionsCell(boolean z) {
        String string;
        int i;
        int i2;
        TLRPC.ChatFull chatFull = getMessagesController().getChatFull(this.chatId);
        boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(this.currentChat);
        TLRPC.ChatReactions chatReactions = this.availableReactions;
        if (chatReactions == null || (chatReactions instanceof TLRPC.TL_chatReactionsNone)) {
            string = LocaleController.getString(R.string.ReactionsOff);
            if (chatFull != null && chatFull.paid_reactions_available) {
                string = "1";
            }
        } else {
            if (chatReactions instanceof TLRPC.TL_chatReactionsSome) {
                TLRPC.TL_chatReactionsSome tL_chatReactionsSome = (TLRPC.TL_chatReactionsSome) chatReactions;
                int i3 = 0;
                while (i2 < tL_chatReactionsSome.reactions.size()) {
                    TLRPC.Reaction reaction = tL_chatReactionsSome.reactions.get(i2);
                    if (reaction instanceof TLRPC.TL_reactionEmoji) {
                        TLRPC.TL_availableReaction tL_availableReaction = getMediaDataController().getReactionsMap().get(((TLRPC.TL_reactionEmoji) reaction).emoticon);
                        if (tL_availableReaction != null) {
                            if (tL_availableReaction.inactive) {
                            }
                            i3++;
                        }
                    } else {
                        i2 = reaction instanceof TLRPC.TL_reactionCustomEmoji ? 0 : i2 + 1;
                        i3++;
                    }
                }
                if (isChannelAndNotMegaGroup) {
                    if (chatFull != null && chatFull.paid_reactions_available) {
                        i3++;
                    }
                    if (i3 != 0) {
                        string = String.valueOf(i3);
                    }
                    i = R.string.ReactionsOff;
                } else {
                    int min = Math.min(getMediaDataController().getEnabledReactionsList().size(), i3);
                    if (min != 0) {
                        string = LocaleController.formatString(R.string.ReactionsCount, Integer.valueOf(min), Integer.valueOf(getMediaDataController().getEnabledReactionsList().size()));
                    }
                    i = R.string.ReactionsOff;
                }
            } else {
                i = R.string.ReactionsAll;
            }
            string = LocaleController.getString(i);
        }
        this.reactionsCell.setTextAndValueAndIcon(LocaleController.getString(R.string.Reactions), string, z, R.drawable.msg_reactions2, true);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ boolean canFinishFragment() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:136:0x06e3  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0764  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x07ae  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x07e8  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0810  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x081d  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x082a  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0850  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0cd4  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0ce0  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0d2f  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0d62  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0d8e  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0d96  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0d67  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        int i;
        String str;
        String string;
        LinearLayout linearLayout;
        EditTextBoldCursor editTextBoldCursor;
        float f;
        float f2;
        int i2;
        int i3;
        float f3;
        float f4;
        int i4;
        SizeNotifierFrameLayout sizeNotifierFrameLayout;
        boolean z;
        TL_bots.BotInfo botInfo;
        TL_bots.BotInfo botInfo2;
        TLRPC.Chat chat;
        TextInfoPrivacyCell textInfoPrivacyCell;
        TLRPC.ChatFull chatFull;
        EditTextBoldCursor editTextBoldCursor2;
        String str2;
        TextSettingsCell textSettingsCell;
        int i5;
        String str3;
        String string2;
        int i6;
        TextCell textCell;
        int i7;
        TextCell textCell2;
        TextCell textCell3;
        TextCell textCell4;
        TextCell textCell5;
        TLRPC.ChatFull chatFull2;
        TLRPC.ChatFull chatFull3;
        TLRPC.ChatFull chatFull4;
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ChatEditActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i8) {
                if (i8 == -1) {
                    if (ChatEditActivity.this.checkDiscard()) {
                        ChatEditActivity.this.lambda$onBackPressed$323();
                    }
                } else if (i8 == 1) {
                    ChatEditActivity.this.processDone();
                }
            }
        });
        SizeNotifierFrameLayout sizeNotifierFrameLayout2 = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.ChatEditActivity.3
            private boolean ignoreLayout;

            /* JADX WARN: Removed duplicated region for block: B:22:0x006f  */
            /* JADX WARN: Removed duplicated region for block: B:29:0x009d  */
            /* JADX WARN: Removed duplicated region for block: B:33:0x00af  */
            /* JADX WARN: Removed duplicated region for block: B:35:0x00b9  */
            /* JADX WARN: Removed duplicated region for block: B:42:0x008b  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onLayout(boolean z2, int i8, int i9, int i10, int i11) {
                int i12;
                int i13;
                int i14;
                int i15;
                int childCount = getChildCount();
                int measureKeyboardHeight = measureKeyboardHeight();
                int emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : ChatEditActivity.this.nameTextView.getEmojiPadding();
                setBottomClip(emojiPadding);
                for (int i16 = 0; i16 < childCount; i16++) {
                    View childAt = getChildAt(i16);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i17 = layoutParams.gravity;
                        if (i17 == -1) {
                            i17 = 51;
                        }
                        int i18 = i17 & 112;
                        int i19 = i17 & 7;
                        if (i19 == 1) {
                            i12 = (((i10 - i8) - measuredWidth) / 2) + layoutParams.leftMargin;
                        } else if (i19 != 5) {
                            i13 = layoutParams.leftMargin;
                            if (i18 == 16) {
                                if (i18 == 48) {
                                    i15 = layoutParams.topMargin + getPaddingTop();
                                } else if (i18 != 80) {
                                    i15 = layoutParams.topMargin;
                                } else {
                                    i14 = ((i11 - emojiPadding) - i9) - measuredHeight;
                                }
                                if (ChatEditActivity.this.nameTextView != null && ChatEditActivity.this.nameTextView.isPopupView(childAt)) {
                                    i15 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                                }
                                childAt.layout(i13, i15, measuredWidth + i13, measuredHeight + i15);
                            } else {
                                i14 = ((((i11 - emojiPadding) - i9) - measuredHeight) / 2) + layoutParams.topMargin;
                            }
                            i15 = i14 - layoutParams.bottomMargin;
                            if (ChatEditActivity.this.nameTextView != null) {
                                i15 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                            }
                            childAt.layout(i13, i15, measuredWidth + i13, measuredHeight + i15);
                        } else {
                            i12 = i10 - measuredWidth;
                        }
                        i13 = i12 - layoutParams.rightMargin;
                        if (i18 == 16) {
                        }
                        i15 = i14 - layoutParams.bottomMargin;
                        if (ChatEditActivity.this.nameTextView != null) {
                        }
                        childAt.layout(i13, i15, measuredWidth + i13, measuredHeight + i15);
                    }
                }
                notifyHeightChanged();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i8, int i9) {
                int makeMeasureSpec;
                int paddingTop;
                int size = View.MeasureSpec.getSize(i8);
                int size2 = View.MeasureSpec.getSize(i9);
                setMeasuredDimension(size, size2);
                int paddingTop2 = size2 - getPaddingTop();
                measureChildWithMargins(((BaseFragment) ChatEditActivity.this).actionBar, i8, 0, i9, 0);
                if (measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                    this.ignoreLayout = true;
                    ChatEditActivity.this.nameTextView.hideEmojiView();
                    this.ignoreLayout = false;
                }
                int childCount = getChildCount();
                for (int i10 = 0; i10 < childCount; i10++) {
                    View childAt = getChildAt(i10);
                    if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) ChatEditActivity.this).actionBar) {
                        if (ChatEditActivity.this.nameTextView == null || !ChatEditActivity.this.nameTextView.isPopupView(childAt)) {
                            measureChildWithMargins(childAt, i8, 0, i9, 0);
                        } else {
                            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = childAt.getLayoutParams().height;
                            } else if (AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (paddingTop2 - AndroidUtilities.statusBarHeight) + getPaddingTop());
                            } else {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = (paddingTop2 - AndroidUtilities.statusBarHeight) + getPaddingTop();
                            }
                            childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                        }
                    }
                }
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        sizeNotifierFrameLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createView$2;
                lambda$createView$2 = ChatEditActivity.lambda$createView$2(view, motionEvent);
                return lambda$createView$2;
            }
        });
        this.fragmentView = sizeNotifierFrameLayout2;
        int i8 = Theme.key_windowBackgroundGray;
        sizeNotifierFrameLayout2.setBackgroundColor(Theme.getColor(i8));
        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        sizeNotifierFrameLayout2.addView(scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.linearLayout = linearLayout2;
        scrollView.addView(linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        linearLayout2.setOrientation(1);
        this.actionBar.setTitle(LocaleController.getString("ChannelEdit", R.string.ChannelEdit));
        LinearLayout linearLayout3 = new LinearLayout(context);
        this.avatarContainer = linearLayout3;
        linearLayout3.setOrientation(1);
        LinearLayout linearLayout4 = this.avatarContainer;
        int i9 = Theme.key_windowBackgroundWhite;
        linearLayout4.setBackgroundColor(Theme.getColor(i9));
        linearLayout2.addView(this.avatarContainer, LayoutHelper.createLinear(-1, -2));
        final FrameLayout frameLayout = new FrameLayout(context);
        this.avatarContainer.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.ChatEditActivity.4
            @Override // android.view.View
            public void invalidate() {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate();
            }

            @Override // android.view.View
            public void invalidate(int i10, int i11, int i12, int i13) {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate(i10, i11, i12, i13);
            }
        };
        this.avatarImage = backupImageView;
        backupImageView.setRoundRadius(this.forum ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(32.0f));
        if (this.currentUser != null || ChatObject.canChangeChatInfo(this.currentChat)) {
            BackupImageView backupImageView2 = this.avatarImage;
            boolean z2 = LocaleController.isRTL;
            frameLayout.addView(backupImageView2, LayoutHelper.createFrame(64, 64.0f, (z2 ? 5 : 3) | 48, z2 ? 0.0f : 16.0f, 12.0f, z2 ? 16.0f : 0.0f, 8.0f));
            final Paint paint = new Paint(1);
            paint.setColor(1426063360);
            View view = new View(context) { // from class: org.telegram.ui.ChatEditActivity.5
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    if (ChatEditActivity.this.avatarImage == null || !ChatEditActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                        return;
                    }
                    paint.setAlpha((int) (ChatEditActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, paint);
                }
            };
            this.avatarOverlay = view;
            boolean z3 = LocaleController.isRTL;
            frameLayout.addView(view, LayoutHelper.createFrame(64, 64.0f, (z3 ? 5 : 3) | 48, z3 ? 0.0f : 16.0f, 12.0f, z3 ? 16.0f : 0.0f, 8.0f));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.avatarProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            this.avatarProgressView.setNoProgress(false);
            RadialProgressView radialProgressView2 = this.avatarProgressView;
            boolean z4 = LocaleController.isRTL;
            frameLayout.addView(radialProgressView2, LayoutHelper.createFrame(64, 64.0f, (z4 ? 5 : 3) | 48, z4 ? 0.0f : 16.0f, 12.0f, z4 ? 16.0f : 0.0f, 8.0f));
            showAvatarProgress(false, false);
            this.avatarContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda11
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$3(view2);
                }
            });
        } else {
            BackupImageView backupImageView3 = this.avatarImage;
            boolean z5 = LocaleController.isRTL;
            frameLayout.addView(backupImageView3, LayoutHelper.createFrame(64, 64.0f, (z5 ? 5 : 3) | 48, z5 ? 0.0f : 16.0f, 12.0f, z5 ? 16.0f : 0.0f, 12.0f));
        }
        EditTextEmoji editTextEmoji2 = new EditTextEmoji(context, sizeNotifierFrameLayout2, this, 0, false);
        this.nameTextView = editTextEmoji2;
        if (this.userId != 0) {
            string = LocaleController.getString(R.string.BotName);
        } else {
            if (this.isChannel) {
                i = R.string.EnterChannelName;
                str = "EnterChannelName";
            } else {
                i = R.string.GroupName;
                str = "GroupName";
            }
            string = LocaleController.getString(str, i);
        }
        editTextEmoji2.setHint(string);
        EditTextEmoji editTextEmoji3 = this.nameTextView;
        TLRPC.Chat chat2 = this.currentChat;
        editTextEmoji3.setEnabled(chat2 != null || ChatObject.canChangeChatInfo(chat2));
        EditTextEmoji editTextEmoji4 = this.nameTextView;
        editTextEmoji4.setFocusable(editTextEmoji4.isEnabled());
        this.nameTextView.getEditText().addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.ChatEditActivity.6
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                ChatEditActivity.this.avatarDrawable.setInfo(5L, ChatEditActivity.this.nameTextView.getText().toString(), null);
                if (ChatEditActivity.this.avatarImage != null) {
                    ChatEditActivity.this.avatarImage.invalidate();
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
            }
        });
        this.nameTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(128)});
        EditTextEmoji editTextEmoji5 = this.nameTextView;
        boolean z6 = LocaleController.isRTL;
        frameLayout.addView(editTextEmoji5, LayoutHelper.createFrame(-1, -2.0f, 16, z6 ? 5.0f : 96.0f, 0.0f, z6 ? 96.0f : 5.0f, 0.0f));
        LinearLayout linearLayout5 = new LinearLayout(context);
        this.settingsContainer = linearLayout5;
        linearLayout5.setOrientation(1);
        this.settingsContainer.setBackgroundColor(Theme.getColor(i9));
        linearLayout2.addView(this.settingsContainer, LayoutHelper.createLinear(-1, -2));
        if (this.currentUser != null || ChatObject.canChangeChatInfo(this.currentChat)) {
            TextCell textCell6 = new TextCell(context) { // from class: org.telegram.ui.ChatEditActivity.7
                @Override // org.telegram.ui.Cells.TextCell, android.view.View
                protected void onDraw(Canvas canvas) {
                    canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            };
            this.setAvatarCell = textCell6;
            textCell6.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.setAvatarCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
            this.setAvatarCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda22
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$8(view2);
                }
            });
            this.settingsContainer.addView(this.setAvatarCell, LayoutHelper.createLinear(-1, -2));
        }
        EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
        this.descriptionTextView = editTextBoldCursor3;
        editTextBoldCursor3.setTextSize(1, 16.0f);
        this.descriptionTextView.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor4 = this.descriptionTextView;
        int i10 = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor4.setTextColor(Theme.getColor(i10));
        this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.descriptionTextView.setBackgroundDrawable(null);
        this.descriptionTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.descriptionTextView.setInputType(180225);
        this.descriptionTextView.setImeOptions(6);
        this.descriptionTextView.setEnabled(this.currentUser != null || ChatObject.canChangeChatInfo(this.currentChat));
        EditTextBoldCursor editTextBoldCursor5 = this.descriptionTextView;
        editTextBoldCursor5.setFocusable(editTextBoldCursor5.isEnabled());
        this.descriptionTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NotificationCenter.newLocationAvailable)});
        this.descriptionTextView.setHint(LocaleController.getString("DescriptionOptionalPlaceholder", R.string.DescriptionOptionalPlaceholder));
        this.descriptionTextView.setCursorColor(Theme.getColor(i10));
        this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.descriptionTextView.setCursorWidth(1.5f);
        if (this.descriptionTextView.isEnabled()) {
            linearLayout = this.settingsContainer;
            editTextBoldCursor = this.descriptionTextView;
            f = 23.0f;
            f2 = 9.0f;
            i2 = -1;
            i3 = -2;
            f3 = 23.0f;
            f4 = 15.0f;
        } else {
            linearLayout = this.settingsContainer;
            editTextBoldCursor = this.descriptionTextView;
            f = 23.0f;
            f2 = 6.0f;
            i2 = -1;
            i3 = -2;
            f3 = 23.0f;
            f4 = 12.0f;
        }
        linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(i2, i3, f3, f4, f, f2));
        this.descriptionTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda23
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i11, KeyEvent keyEvent) {
                boolean lambda$createView$9;
                lambda$createView$9 = ChatEditActivity.this.lambda$createView$9(textView, i11, keyEvent);
                return lambda$createView$9;
            }
        });
        this.descriptionTextView.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.ChatEditActivity.8
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
            }
        });
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.settingsTopSectionCell = shadowSectionCell;
        linearLayout2.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout6 = new LinearLayout(context);
        this.typeEditContainer = linearLayout6;
        linearLayout6.setOrientation(1);
        linearLayout2.addView(this.typeEditContainer, LayoutHelper.createLinear(-1, -2));
        TLRPC.Chat chat3 = this.currentChat;
        if (chat3 != null) {
            if (chat3.megagroup && ((chatFull4 = this.info) == null || chatFull4.can_set_location)) {
                TextCell textCell7 = new TextCell(context);
                this.locationCell = textCell7;
                textCell7.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.locationCell, LayoutHelper.createLinear(-1, -2));
                this.locationCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda24
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$11(view2);
                    }
                });
            }
            if (this.currentChat.creator && ((chatFull3 = this.info) == null || chatFull3.can_set_username)) {
                TextCell textCell8 = new TextCell(context);
                this.typeCell = textCell8;
                textCell8.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.typeCell, LayoutHelper.createLinear(-1, -2));
                this.typeCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda25
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$12(view2);
                    }
                });
            }
            if (ChatObject.isChannel(this.currentChat) && ((this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 1)) || (!this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 0)))) {
                TextCell textCell9 = new TextCell(context);
                this.linkedCell = textCell9;
                textCell9.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.linkedCell, LayoutHelper.createLinear(-1, -2));
                this.linkedCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda26
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$13(view2);
                    }
                });
            }
            if (ChatObject.isChannelAndNotMegaGroup(this.currentChat) && ChatObject.canChangeChatInfo(this.currentChat)) {
                PeerColorActivity.ChangeNameColorCell changeNameColorCell = new PeerColorActivity.ChangeNameColorCell(this.currentAccount, -this.currentChat.id, context, getResourceProvider());
                this.colorCell = changeNameColorCell;
                changeNameColorCell.setBackground(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.colorCell, LayoutHelper.createLinear(-1, -2));
                this.colorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda27
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$14(view2);
                    }
                });
            }
            if (!this.isChannel && ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
                TextCell textCell10 = new TextCell(context);
                this.historyCell = textCell10;
                textCell10.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.historyCell, LayoutHelper.createLinear(-1, -2));
                this.historyCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda28
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$16(context, view2);
                    }
                });
            }
            if (ChatObject.isMegagroup(this.currentChat) && ChatObject.hasAdminRights(this.currentChat)) {
                MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(-this.currentChat.id, new Consumer() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda29
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        ChatEditActivity.this.lambda$createView$17((TL_stories.TL_premium_boostsStatus) obj);
                    }
                });
                PeerColorActivity.ChangeNameColorCell changeNameColorCell2 = new PeerColorActivity.ChangeNameColorCell(this.currentAccount, -this.currentChat.id, context, getResourceProvider());
                this.colorCell = changeNameColorCell2;
                changeNameColorCell2.setBackground(Theme.getSelectorDrawable(true));
                this.typeEditContainer.addView(this.colorCell, LayoutHelper.createLinear(-1, -2));
                this.colorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$18(view2);
                    }
                });
            }
            if (!this.isChannel && this.currentChat.creator) {
                TextCell textCell11 = new TextCell(context, 23, false, true, null);
                this.forumsCell = textCell11;
                textCell11.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.forumsCell.setTextAndCheckAndIcon(LocaleController.getString("ChannelTopics", R.string.ChannelTopics), this.forum, R.drawable.msg_topics, false);
                this.forumsCell.getCheckBox().setIcon(this.canForum ? 0 : R.drawable.permission_locked);
                this.typeEditContainer.addView(this.forumsCell, LayoutHelper.createFrame(-1, -2.0f));
                this.forumsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$19(frameLayout, view2);
                    }
                });
            }
            updateColorCell();
        }
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (this.currentUser != null || ChatObject.canChangeChatInfo(this.currentChat) || this.historyCell != null) {
            ActionBarMenuItem addItemWithWidth = createMenu.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f));
            this.doneButton = addItemWithWidth;
            addItemWithWidth.setContentDescription(LocaleController.getString("Done", R.string.Done));
        }
        if (this.locationCell == null && this.historyCell == null && this.typeCell == null && this.linkedCell == null && this.forumsCell == null) {
            i4 = -1;
        } else {
            this.settingsSectionCell = new TextInfoPrivacyCell(context);
            CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(getThemedColor(i8)), Theme.getThemedDrawable(context, R.drawable.greydivider, Theme.getColor(Theme.key_windowBackgroundGrayShadow, getResourceProvider())), 0, 0);
            combinedDrawable.setFullsize(true);
            this.settingsSectionCell.setBackground(combinedDrawable);
            if (this.forumsCell != null) {
                this.settingsSectionCell.setText(LocaleController.getString("ForumToggleDescription", R.string.ForumToggleDescription));
            } else {
                this.settingsSectionCell.setFixedSize(12);
            }
            i4 = -1;
            linearLayout2.addView(this.settingsSectionCell, LayoutHelper.createLinear(-1, -2));
        }
        LinearLayout linearLayout7 = new LinearLayout(context);
        this.infoContainer = linearLayout7;
        linearLayout7.setOrientation(1);
        this.infoContainer.setBackgroundColor(Theme.getColor(i9));
        linearLayout2.addView(this.infoContainer, LayoutHelper.createLinear(i4, -2));
        if (this.currentChat != null) {
            TextCell textCell12 = new TextCell(context);
            this.blockCell = textCell12;
            textCell12.setBackground(Theme.getSelectorDrawable(false));
            TextCell textCell13 = this.blockCell;
            if (!ChatObject.isChannel(this.currentChat)) {
                TLRPC.Chat chat4 = this.currentChat;
                if (!chat4.creator && (!ChatObject.hasAdminRights(chat4) || !ChatObject.canChangeChatInfo(this.currentChat))) {
                    i6 = 8;
                    textCell13.setVisibility(i6);
                    this.blockCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$20(view2);
                        }
                    });
                    TextCell textCell14 = new TextCell(context);
                    this.inviteLinksCell = textCell14;
                    textCell14.setBackground(Theme.getSelectorDrawable(false));
                    this.inviteLinksCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$21(view2);
                        }
                    });
                    TextCell textCell15 = new TextCell(context);
                    this.reactionsCell = textCell15;
                    textCell15.setBackground(Theme.getSelectorDrawable(false));
                    this.reactionsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$22(view2);
                        }
                    });
                    TextCell textCell16 = new TextCell(context);
                    this.adminCell = textCell16;
                    textCell16.setBackground(Theme.getSelectorDrawable(false));
                    this.adminCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$23(view2);
                        }
                    });
                    TextCell textCell17 = new TextCell(context);
                    this.membersCell = textCell17;
                    textCell17.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    this.membersCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda7
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$24(view2);
                        }
                    });
                    if (!ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
                        TextCell textCell18 = new TextCell(context);
                        this.memberRequestsCell = textCell18;
                        textCell18.setBackground(Theme.getSelectorDrawable(false));
                        this.memberRequestsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda8
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ChatEditActivity.this.lambda$createView$25(view2);
                            }
                        });
                    }
                    TextCell textCell19 = new TextCell(context);
                    this.channelAffiliateProgramsCell = textCell19;
                    textCell19.setTextAndIcon(applyNewSpan(LocaleController.getString(R.string.ChannelAffiliatePrograms)), R.drawable.menu_feature_premium, false);
                    this.channelAffiliateProgramsCell.setBackground(Theme.getSelectorDrawable(false));
                    this.channelAffiliateProgramsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda9
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$26(view2);
                        }
                    });
                    this.channelAffiliateProgramsCell.setVisibility(8);
                    if (!ChatObject.isChannel(this.currentChat) || this.currentChat.gigagroup) {
                        TextCell textCell20 = new TextCell(context);
                        this.logCell = textCell20;
                        textCell20.setTextAndIcon((CharSequence) LocaleController.getString(R.string.EventLog), R.drawable.msg_log, false);
                        this.logCell.setBackground(Theme.getSelectorDrawable(false));
                        this.logCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda10
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ChatEditActivity.this.lambda$createView$27(view2);
                            }
                        });
                    }
                    if (ChatObject.isBoostSupported(this.currentChat)) {
                        TextCell textCell21 = new TextCell(context);
                        this.statsAndBoosts = textCell21;
                        textCell21.setTextAndIcon((CharSequence) LocaleController.getString(R.string.StatisticsAndBoosts), R.drawable.msg_stats, true);
                        this.statsAndBoosts.setBackground(Theme.getSelectorDrawable(false));
                        this.statsAndBoosts.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda12
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ChatEditActivity.this.lambda$createView$28(view2);
                            }
                        });
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
                    if (textCell != null && (chatFull2 = this.info) != null && chatFull2.requests_pending > 0) {
                        this.infoContainer.addView(textCell, LayoutHelper.createLinear(-1, -2));
                    }
                    if (this.isChannel) {
                        this.infoContainer.addView(this.inviteLinksCell, LayoutHelper.createLinear(-1, -2));
                    }
                    if (!this.isChannel || this.currentChat.gigagroup) {
                        i7 = -1;
                        this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
                    } else {
                        i7 = -1;
                    }
                    textCell2 = this.statsAndBoosts;
                    if (textCell2 != null) {
                        this.infoContainer.addView(textCell2, LayoutHelper.createLinear(i7, -2));
                    }
                    textCell3 = this.logCell;
                    if (textCell3 != null) {
                        this.infoContainer.addView(textCell3, LayoutHelper.createLinear(i7, -2));
                    }
                    textCell4 = this.channelAffiliateProgramsCell;
                    if (textCell4 != null) {
                        this.infoContainer.addView(textCell4, LayoutHelper.createLinear(i7, -2));
                    }
                    if (this.channelAffiliateProgramsCell != null && getMessagesController().starrefConnectAllowed && ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
                        this.channelAffiliateProgramsCell.setVisibility(0);
                    }
                    textCell5 = this.logCell;
                    if (textCell5 != null) {
                        TextCell textCell22 = this.channelAffiliateProgramsCell;
                        textCell5.setNeedDivider(textCell22 != null && textCell22.getVisibility() == 0);
                    }
                }
            }
            i6 = 0;
            textCell13.setVisibility(i6);
            this.blockCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$20(view2);
                }
            });
            TextCell textCell142 = new TextCell(context);
            this.inviteLinksCell = textCell142;
            textCell142.setBackground(Theme.getSelectorDrawable(false));
            this.inviteLinksCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$21(view2);
                }
            });
            TextCell textCell152 = new TextCell(context);
            this.reactionsCell = textCell152;
            textCell152.setBackground(Theme.getSelectorDrawable(false));
            this.reactionsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$22(view2);
                }
            });
            TextCell textCell162 = new TextCell(context);
            this.adminCell = textCell162;
            textCell162.setBackground(Theme.getSelectorDrawable(false));
            this.adminCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$23(view2);
                }
            });
            TextCell textCell172 = new TextCell(context);
            this.membersCell = textCell172;
            textCell172.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.membersCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$24(view2);
                }
            });
            if (!ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
            }
            TextCell textCell192 = new TextCell(context);
            this.channelAffiliateProgramsCell = textCell192;
            textCell192.setTextAndIcon(applyNewSpan(LocaleController.getString(R.string.ChannelAffiliatePrograms)), R.drawable.menu_feature_premium, false);
            this.channelAffiliateProgramsCell.setBackground(Theme.getSelectorDrawable(false));
            this.channelAffiliateProgramsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$26(view2);
                }
            });
            this.channelAffiliateProgramsCell.setVisibility(8);
            if (!ChatObject.isChannel(this.currentChat)) {
            }
            TextCell textCell202 = new TextCell(context);
            this.logCell = textCell202;
            textCell202.setTextAndIcon((CharSequence) LocaleController.getString(R.string.EventLog), R.drawable.msg_log, false);
            this.logCell.setBackground(Theme.getSelectorDrawable(false));
            this.logCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$27(view2);
                }
            });
            if (ChatObject.isBoostSupported(this.currentChat)) {
            }
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
            if (this.isChannel) {
            }
            i7 = -1;
            this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
            textCell2 = this.statsAndBoosts;
            if (textCell2 != null) {
            }
            textCell3 = this.logCell;
            if (textCell3 != null) {
            }
            textCell4 = this.channelAffiliateProgramsCell;
            if (textCell4 != null) {
            }
            if (this.channelAffiliateProgramsCell != null) {
                this.channelAffiliateProgramsCell.setVisibility(0);
            }
            textCell5 = this.logCell;
            if (textCell5 != null) {
            }
        }
        if (this.currentUser != null) {
            TextCell textCell23 = new TextCell(context);
            this.publicLinkCell = textCell23;
            textCell23.setBackground(Theme.getSelectorDrawable(false));
            this.publicLinkCell.setPrioritizeTitleOverValue(true);
            this.infoContainer.addView(this.publicLinkCell, LayoutHelper.createLinear(-1, -2));
            this.publicLinkCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$29(view2);
                }
            });
            updatePublicLinksCount();
            TextCell textCell24 = new TextCell(context);
            this.botAffiliateProgramCell = textCell24;
            textCell24.setBackground(Theme.getSelectorDrawable(false));
            this.botAffiliateProgramCell.setTextAndValueAndIcon(applyNewSpan(LocaleController.getString(R.string.AffiliateProgramBot)), (CharSequence) "", R.drawable.msg_shareout, true);
            this.infoContainer.addView(this.botAffiliateProgramCell, LayoutHelper.createLinear(-1, -2));
            this.botAffiliateProgramCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$30(view2);
                }
            });
            this.botAffiliateProgramCell.setDrawLoading(this.userInfo == null, 45, false);
            TLRPC.UserFull userFull = this.userInfo;
            if (userFull != null) {
                this.botAffiliateProgramCell.setValue(userFull.starref_program == null ? LocaleController.getString(R.string.AffiliateProgramBotOff) : String.format(Locale.US, "%.1f%%", Float.valueOf(r0.commission_permille / 10.0f)), false);
            }
            if (!getMessagesController().starrefProgramAllowed) {
                this.botAffiliateProgramCell.setVisibility(8);
            }
            TextCell textCell25 = new TextCell(context);
            this.editIntroCell = textCell25;
            textCell25.setBackground(Theme.getSelectorDrawable(false));
            this.editIntroCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.BotEditIntro), R.drawable.msg_log, true);
            this.infoContainer.addView(this.editIntroCell, LayoutHelper.createLinear(-1, -2));
            this.editIntroCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$31(view2);
                }
            });
            TextCell textCell26 = new TextCell(context);
            this.editCommandsCell = textCell26;
            textCell26.setBackground(Theme.getSelectorDrawable(false));
            this.editCommandsCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.BotEditCommands), R.drawable.msg_media, true);
            this.infoContainer.addView(this.editCommandsCell, LayoutHelper.createLinear(-1, -2));
            this.editCommandsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda16
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$32(view2);
                }
            });
            TextCell textCell27 = new TextCell(context);
            this.changeBotSettingsCell = textCell27;
            textCell27.setBackground(Theme.getSelectorDrawable(false));
            this.changeBotSettingsCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.BotChangeSettings), R.drawable.msg_bot, true);
            this.infoContainer.addView(this.changeBotSettingsCell, LayoutHelper.createLinear(-1, -2));
            this.changeBotSettingsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$33(view2);
                }
            });
        }
        TLRPC.Chat chat5 = this.currentChat;
        if (chat5 != null) {
            if (!ChatObject.hasAdminRights(chat5)) {
                this.infoContainer.setVisibility(8);
                this.settingsTopSectionCell.setVisibility(8);
            }
            if (this.stickersCell == null) {
                ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
                this.infoSectionCell = shadowSectionCell2;
                linearLayout2.addView(shadowSectionCell2, LayoutHelper.createLinear(-1, -2));
            }
        } else if (this.currentUser != null) {
            this.botInfoCell = new TextInfoPrivacyCell(context);
            String string3 = LocaleController.getString(R.string.BotManageInfo);
            SpannableString valueOf = SpannableString.valueOf(string3);
            int indexOf = string3.indexOf("@BotFather");
            if (indexOf != -1) {
                valueOf.setSpan(new ClickableSpan() { // from class: org.telegram.ui.ChatEditActivity.9
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view2) {
                        Browser.openUrl(view2.getContext(), "https://t.me/BotFather");
                    }

                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        super.updateDrawState(textPaint);
                        textPaint.setUnderlineText(false);
                    }
                }, indexOf, indexOf + 10, 33);
            }
            TextInfoPrivacyCell textInfoPrivacyCell2 = this.botInfoCell;
            Context context2 = getContext();
            int i11 = R.drawable.greydivider_bottom;
            int i12 = Theme.key_windowBackgroundGrayShadow;
            textInfoPrivacyCell2.setBackground(Theme.getThemedDrawableByKey(context2, i11, i12));
            this.botInfoCell.setText(valueOf);
            linearLayout2.addView(this.botInfoCell, LayoutHelper.createLinear(-1, -2));
            TextCell textCell28 = new TextCell(context);
            this.verifyCell = textCell28;
            textCell28.setBackground(Theme.getSelectorDrawable(true));
            this.verifyCell.setTextAndIcon((CharSequence) LocaleController.getString(R.string.BotVerifyAccounts), R.drawable.menu_factcheck, false);
            TextCell textCell29 = this.verifyCell;
            int i13 = Theme.key_windowBackgroundWhiteBlueText4;
            textCell29.setColors(i13, i13);
            linearLayout2.addView(this.verifyCell, LayoutHelper.createLinear(-1, -2));
            this.verifyCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$34(view2);
                }
            });
            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
            this.verifyInfoCell = textInfoPrivacyCell3;
            textInfoPrivacyCell3.setFixedSize(12);
            this.verifyInfoCell.setBackground(Theme.getThemedDrawableByKey(getContext(), i11, i12));
            linearLayout2.addView(this.verifyInfoCell, LayoutHelper.createLinear(-1, -2));
            TextCell textCell30 = this.verifyCell;
            TLRPC.UserFull userFull2 = this.userInfo;
            textCell30.setVisibility((userFull2 == null || (botInfo2 = userFull2.bot_info) == null || botInfo2.verifier_settings == null) ? 8 : 0);
            TextInfoPrivacyCell textInfoPrivacyCell4 = this.verifyInfoCell;
            TLRPC.UserFull userFull3 = this.userInfo;
            textInfoPrivacyCell4.setVisibility((userFull3 == null || (botInfo = userFull3.bot_info) == null || botInfo.verifier_settings == null) ? 8 : 0);
            TLRPC.User user = this.currentUser;
            if (user.bot && user.bot_can_edit) {
                LinearLayout linearLayout8 = new LinearLayout(context);
                this.balanceContainer = linearLayout8;
                linearLayout8.setOrientation(1);
                this.balanceContainer.setBackgroundColor(Theme.getColor(i9));
                linearLayout2.addView(this.balanceContainer, LayoutHelper.createLinear(-1, -2));
                HeaderCell headerCell = new HeaderCell(context);
                headerCell.setText(LocaleController.getString(R.string.BotBalance));
                this.balanceContainer.addView(headerCell, LayoutHelper.createLinear(-1, -2));
                TextCell textCell31 = new TextCell(context);
                this.tonBalanceCell = textCell31;
                textCell31.setBackground(Theme.getSelectorDrawable(false));
                this.tonBalanceCell.setPrioritizeTitleOverValue(true);
                this.balanceContainer.addView(this.tonBalanceCell, LayoutHelper.createLinear(-1, -2));
                final BotStarsController botStarsController = BotStarsController.getInstance(this.currentAccount);
                this.tonBalanceCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda19
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$35(botStarsController, view2);
                    }
                });
                CharSequence charSequence = "";
                if (botStarsController.isTONBalanceAvailable(this.userId)) {
                    sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
                    long tONBalance = botStarsController.getTONBalance(this.userId);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (tONBalance > 0) {
                        double d = tONBalance;
                        Double.isNaN(d);
                        double d2 = d / 1.0E9d;
                        if (d2 > 1000.0d) {
                            spannableStringBuilder.append((CharSequence) "TON ").append((CharSequence) AndroidUtilities.formatWholeNumber((int) d2, 0));
                        } else {
                            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
                            decimalFormatSymbols.setDecimalSeparator('.');
                            DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
                            decimalFormat.setMinimumFractionDigits(2);
                            decimalFormat.setMaximumFractionDigits(3);
                            decimalFormat.setGroupingUsed(false);
                            spannableStringBuilder.append((CharSequence) "TON ").append((CharSequence) decimalFormat.format(d2));
                        }
                    }
                    this.tonBalanceCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString(R.string.BotBalanceTON), (CharSequence) spannableStringBuilder, R.drawable.msg_ton, true);
                } else {
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("x");
                    spannableStringBuilder2.setSpan(new LoadingSpan(this.tonBalanceCell.valueTextView, AndroidUtilities.dp(30.0f)), 0, spannableStringBuilder2.length(), 33);
                    this.tonBalanceCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString(R.string.BotBalanceTON), (CharSequence) spannableStringBuilder2, R.drawable.msg_ton, false);
                    sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
                }
                this.tonBalanceCell.setVisibility(botStarsController.botHasTON(this.userId) ? 0 : 8);
                TextCell textCell32 = new TextCell(context);
                this.starsBalanceCell = textCell32;
                textCell32.setBackground(Theme.getSelectorDrawable(false));
                this.starsBalanceCell.setPrioritizeTitleOverValue(true);
                this.balanceContainer.addView(this.starsBalanceCell, LayoutHelper.createLinear(-1, -2));
                this.starsBalanceCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda20
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatEditActivity.this.lambda$createView$36(botStarsController, view2);
                    }
                });
                if (botStarsController.isStarsBalanceAvailable(this.userId)) {
                    TextCell textCell33 = this.starsBalanceCell;
                    String string4 = LocaleController.getString(R.string.BotBalanceStars);
                    if (botStarsController.getBotStarsBalance(this.userId).amount <= 0) {
                        z = false;
                    } else {
                        z = false;
                        charSequence = StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("XTR", StarsIntroActivity.formatStarsAmountShort(botStarsController.getBotStarsBalance(this.userId), 0.85f, ' ')), 0.85f);
                    }
                    textCell33.setTextAndValueAndIcon(string4, charSequence, R.drawable.menu_premium_main, z);
                } else {
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder("x");
                    spannableStringBuilder3.setSpan(new LoadingSpan(this.starsBalanceCell.valueTextView, AndroidUtilities.dp(30.0f)), 0, spannableStringBuilder3.length(), 33);
                    this.starsBalanceCell.setTextAndValueAndIcon((CharSequence) LocaleController.getString(R.string.BotBalanceStars), (CharSequence) spannableStringBuilder3, R.drawable.menu_premium_main, false);
                }
                this.starsBalanceCell.setVisibility(botStarsController.botHasStars(this.userId) ? 0 : 8);
                TextInfoPrivacyCell textInfoPrivacyCell5 = new TextInfoPrivacyCell(context, getResourceProvider());
                textInfoPrivacyCell5.setBackground(Theme.getThemedDrawableByKey(context, i11, i12));
                textInfoPrivacyCell5.setTag(R.id.fit_width_tag, 1);
                linearLayout2.addView(textInfoPrivacyCell5, LayoutHelper.createLinear(-1, 8));
                this.balanceContainer.setVisibility((this.starsBalanceCell.getVisibility() == 0 || this.tonBalanceCell.getVisibility() == 0) ? 0 : 8);
                chat = this.currentChat;
                if (chat != null && chat.creator) {
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    this.deleteContainer = frameLayout2;
                    frameLayout2.setBackgroundColor(Theme.getColor(i9));
                    linearLayout2.addView(this.deleteContainer, LayoutHelper.createLinear(-1, -2));
                    TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
                    this.deleteCell = textSettingsCell2;
                    textSettingsCell2.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                    this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    if (this.currentUser == null) {
                        textSettingsCell = this.deleteCell;
                        string2 = LocaleController.getString(R.string.DeleteBot);
                    } else {
                        if (this.isChannel) {
                            textSettingsCell = this.deleteCell;
                            i5 = R.string.ChannelDelete;
                            str3 = "ChannelDelete";
                        } else {
                            textSettingsCell = this.deleteCell;
                            i5 = R.string.DeleteAndExitButton;
                            str3 = "DeleteAndExitButton";
                        }
                        string2 = LocaleController.getString(str3, i5);
                    }
                    textSettingsCell.setText(string2, false);
                    this.deleteContainer.addView(this.deleteCell, LayoutHelper.createFrame(-1, -2.0f));
                    this.deleteCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda21
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatEditActivity.this.lambda$createView$38(view2);
                        }
                    });
                    ShadowSectionCell shadowSectionCell3 = new ShadowSectionCell(context);
                    this.deleteInfoCell = shadowSectionCell3;
                    shadowSectionCell3.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    linearLayout2.addView(this.deleteInfoCell, LayoutHelper.createLinear(-1, -2));
                }
                textInfoPrivacyCell = this.stickersInfoCell;
                if (textInfoPrivacyCell != null) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, this.deleteInfoCell == null ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                }
                UndoView undoView = new UndoView(context);
                this.undoView = undoView;
                sizeNotifierFrameLayout.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                EditTextEmoji editTextEmoji6 = this.nameTextView;
                TLRPC.User user2 = this.currentUser;
                editTextEmoji6.setText(Emoji.replaceEmoji(user2 == null ? ContactsController.formatName(user2) : this.currentChat.title, this.nameTextView.getEditText().getPaint().getFontMetricsInt(), true));
                EditTextEmoji editTextEmoji7 = this.nameTextView;
                editTextEmoji7.setSelection(editTextEmoji7.length());
                chatFull = this.info;
                if (chatFull != null) {
                    TLRPC.UserFull userFull4 = this.userInfo;
                    if (userFull4 != null) {
                        editTextBoldCursor2 = this.descriptionTextView;
                        str2 = userFull4.about;
                    }
                    setAvatar();
                    updateFields(true, false);
                    return this.fragmentView;
                }
                editTextBoldCursor2 = this.descriptionTextView;
                str2 = chatFull.about;
                editTextBoldCursor2.setText(str2);
                setAvatar();
                updateFields(true, false);
                return this.fragmentView;
            }
        }
        sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
        chat = this.currentChat;
        if (chat != null) {
            FrameLayout frameLayout22 = new FrameLayout(context);
            this.deleteContainer = frameLayout22;
            frameLayout22.setBackgroundColor(Theme.getColor(i9));
            linearLayout2.addView(this.deleteContainer, LayoutHelper.createLinear(-1, -2));
            TextSettingsCell textSettingsCell22 = new TextSettingsCell(context);
            this.deleteCell = textSettingsCell22;
            textSettingsCell22.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
            this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (this.currentUser == null) {
            }
            textSettingsCell.setText(string2, false);
            this.deleteContainer.addView(this.deleteCell, LayoutHelper.createFrame(-1, -2.0f));
            this.deleteCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda21
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatEditActivity.this.lambda$createView$38(view2);
                }
            });
            ShadowSectionCell shadowSectionCell32 = new ShadowSectionCell(context);
            this.deleteInfoCell = shadowSectionCell32;
            shadowSectionCell32.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            linearLayout2.addView(this.deleteInfoCell, LayoutHelper.createLinear(-1, -2));
        }
        textInfoPrivacyCell = this.stickersInfoCell;
        if (textInfoPrivacyCell != null) {
        }
        UndoView undoView2 = new UndoView(context);
        this.undoView = undoView2;
        sizeNotifierFrameLayout.addView(undoView2, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        EditTextEmoji editTextEmoji62 = this.nameTextView;
        TLRPC.User user22 = this.currentUser;
        editTextEmoji62.setText(Emoji.replaceEmoji(user22 == null ? ContactsController.formatName(user22) : this.currentChat.title, this.nameTextView.getEditText().getPaint().getFontMetricsInt(), true));
        EditTextEmoji editTextEmoji72 = this.nameTextView;
        editTextEmoji72.setSelection(editTextEmoji72.length());
        chatFull = this.info;
        if (chatFull != null) {
        }
        editTextBoldCursor2.setText(str2);
        setAvatar();
        updateFields(true, false);
        return this.fragmentView;
    }

    /* JADX WARN: Code restructure failed: missing block: B:150:0x024c, code lost:
    
        if (r10.getLastFragment() == r9) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00a4, code lost:
    
        if (r10.getLastFragment() == r9) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x024e, code lost:
    
        lambda$onBackPressed$323();
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
    
        return;
     */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        SpannableStringBuilder append;
        String format;
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        TLRPC.TL_chatBannedRights tL_chatBannedRights2;
        EditTextBoldCursor editTextBoldCursor;
        boolean z = true;
        int i3 = 0;
        if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC.ChatFull chatFull = (TLRPC.ChatFull) objArr[0];
            if (chatFull.id == this.chatId) {
                if (this.info == null && (editTextBoldCursor = this.descriptionTextView) != null) {
                    editTextBoldCursor.setText(chatFull.about);
                }
                boolean z2 = this.info == null;
                this.info = chatFull;
                updateCanForum();
                if (ChatObject.isChannel(this.currentChat) && !this.info.hidden_prehistory) {
                    z = false;
                }
                this.historyHidden = z;
                updateFields(false, false);
                if (z2) {
                    loadLinksCount();
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            if ((MessagesController.UPDATE_MASK_AVATAR & intValue) != 0) {
                setAvatar();
            }
            if ((intValue & MessagesController.UPDATE_MASK_NAME) != 0) {
                updatePublicLinksCount();
                return;
            }
            return;
        }
        if (i == NotificationCenter.channelRightsUpdated) {
            TLRPC.Chat chat = (TLRPC.Chat) objArr[0];
            if (chat == null || chat.id != this.chatId) {
                return;
            }
            TLRPC.TL_chatAdminRights tL_chatAdminRights = this.chatAdminRights;
            if ((tL_chatAdminRights == null || tL_chatAdminRights.equals(chat.admin_rights)) && (((tL_chatBannedRights = this.chatBannedRights) == null || tL_chatBannedRights.equals(chat.banned_rights)) && ((tL_chatBannedRights2 = this.chatDefaultBannedRights) == null || tL_chatBannedRights2.equals(chat.default_banned_rights)))) {
                return;
            }
            INavigationLayout iNavigationLayout = this.parentLayout;
            if (iNavigationLayout != null) {
            }
            removeSelfFromStack();
            return;
        }
        if (i == NotificationCenter.chatAvailableReactionsUpdated) {
            long longValue = ((Long) objArr[0]).longValue();
            if (longValue == this.chatId) {
                TLRPC.ChatFull chatFull2 = getMessagesController().getChatFull(longValue);
                this.info = chatFull2;
                if (chatFull2 != null) {
                    this.availableReactions = chatFull2.available_reactions;
                }
                updateReactionsCell(true);
                return;
            }
            return;
        }
        if (i != NotificationCenter.botStarsUpdated) {
            if (i == NotificationCenter.userInfoDidLoad) {
                if (((Long) objArr[0]).longValue() == this.userId) {
                    setInfo(getMessagesController().getUserFull(this.userId));
                    return;
                }
                return;
            } else if (i == NotificationCenter.channelConnectedBotsUpdate) {
                ((Long) objArr[0]).longValue();
                return;
            } else {
                if (i == NotificationCenter.dialogDeleted && (-this.chatId) == ((Long) objArr[0]).longValue()) {
                    INavigationLayout iNavigationLayout2 = this.parentLayout;
                    if (iNavigationLayout2 != null) {
                    }
                    removeSelfFromStack();
                    return;
                }
                return;
            }
        }
        if (((Long) objArr[0]).longValue() == this.userId) {
            if (this.starsBalanceCell != null) {
                BotStarsController botStarsController = BotStarsController.getInstance(this.currentAccount);
                this.starsBalanceCell.setVisibility(botStarsController.botHasStars(this.userId) ? 0 : 8);
                this.starsBalanceCell.setValue(StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("XTR", StarsIntroActivity.formatStarsAmount(botStarsController.getBotStarsBalance(this.userId), 0.8f, ' ')), 0.85f), true);
                TextCell textCell = this.publicLinkCell;
                if (textCell != null) {
                    textCell.setNeedDivider(botStarsController.botHasStars(this.userId) || botStarsController.botHasTON(this.userId));
                }
                this.balanceContainer.setVisibility((this.starsBalanceCell.getVisibility() == 0 || this.tonBalanceCell.getVisibility() == 0) ? 0 : 8);
            }
            if (this.tonBalanceCell != null) {
                BotStarsController botStarsController2 = BotStarsController.getInstance(this.currentAccount);
                this.tonBalanceCell.setVisibility(botStarsController2.botHasTON(this.userId) ? 0 : 8);
                long tONBalance = botStarsController2.getTONBalance(this.userId);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                if (tONBalance > 0) {
                    double d = tONBalance;
                    Double.isNaN(d);
                    double d2 = d / 1.0E9d;
                    if (d2 > 1000.0d) {
                        append = spannableStringBuilder.append((CharSequence) "TON ");
                        format = AndroidUtilities.formatWholeNumber((int) d2, 0);
                    } else {
                        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
                        decimalFormatSymbols.setDecimalSeparator('.');
                        DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
                        decimalFormat.setMinimumFractionDigits(2);
                        decimalFormat.setMaximumFractionDigits(3);
                        decimalFormat.setGroupingUsed(false);
                        append = spannableStringBuilder.append((CharSequence) "TON ");
                        format = decimalFormat.format(d2);
                    }
                    append.append((CharSequence) format);
                }
                this.tonBalanceCell.setValue(spannableStringBuilder, true);
                TextCell textCell2 = this.publicLinkCell;
                if (textCell2 != null) {
                    if (!botStarsController2.botHasStars(this.userId) && !botStarsController2.botHasTON(this.userId)) {
                        z = false;
                    }
                    textCell2.setNeedDivider(z);
                }
                LinearLayout linearLayout = this.balanceContainer;
                if (this.starsBalanceCell.getVisibility() != 0 && this.tonBalanceCell.getVisibility() != 0) {
                    i3 = 8;
                }
                linearLayout.setVisibility(i3);
            }
        }
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didStartUpload(boolean z, boolean z2) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(0.0f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ void didUploadFailed() {
        ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.InputFile inputFile2, final double d, final String str, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2, boolean z, final TLRPC.VideoSize videoSize) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                ChatEditActivity.this.lambda$didUploadPhoto$41(photoSize2, inputFile, inputFile2, videoSize, photoSize, d, str);
            }
        });
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

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ PhotoViewer.PlaceProviderObject getCloseIntoObject() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getCloseIntoObject(this);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ChatEditActivity$$ExternalSyntheticLambda36
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ChatEditActivity.this.lambda$getThemeDescriptions$52();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        TextCell textCell = this.setAvatarCell;
        int i = ThemeDescription.FLAG_SELECTOR;
        int i2 = Theme.key_listSelector;
        arrayList.add(new ThemeDescription(textCell, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.setAvatarCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueButton));
        arrayList.add(new ThemeDescription(this.setAvatarCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueIcon));
        arrayList.add(new ThemeDescription(this.membersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.membersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayIcon;
        arrayList.add(new ThemeDescription(this.membersCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.adminCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.adminCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.adminCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.inviteLinksCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        if (this.memberRequestsCell != null) {
            arrayList.add(new ThemeDescription(this.memberRequestsCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
            arrayList.add(new ThemeDescription(this.memberRequestsCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
            arrayList.add(new ThemeDescription(this.memberRequestsCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        }
        arrayList.add(new ThemeDescription(this.blockCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.blockCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.blockCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.logCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.logCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.logCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.typeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i5 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.historyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.locationCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.locationCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.locationCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i3));
        EditTextEmoji editTextEmoji = this.nameTextView;
        int i6 = ThemeDescription.FLAG_HINTTEXTCOLOR;
        int i7 = Theme.key_windowBackgroundWhiteHintText;
        arrayList.add(new ThemeDescription(editTextEmoji, i6, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, i7));
        LinearLayout linearLayout = this.avatarContainer;
        int i8 = ThemeDescription.FLAG_BACKGROUND;
        int i9 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(linearLayout, i8, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.settingsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.typeEditContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.deleteContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.stickersContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.infoContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i9));
        int i10 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.settingsTopSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.settingsSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.deleteInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.stickersInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.stickersInfoCell, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, themeDescriptionDelegate, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_undo_background));
        int i11 = Theme.key_undo_cancelColor;
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        int i12 = Theme.key_undo_infoColor;
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{UndoView.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.reactionsCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.reactionsCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.reactionsCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        if (this.statsAndBoosts != null) {
            arrayList.add(new ThemeDescription(this.statsAndBoosts, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i2));
            arrayList.add(new ThemeDescription(this.statsAndBoosts, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
            arrayList.add(new ThemeDescription(this.statsAndBoosts, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        }
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        this.imageUpdater.onActivityResult(i, i2, intent);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji == null || !editTextEmoji.isPopupShowing()) {
            return checkDiscard();
        }
        this.nameTextView.hidePopup(true);
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0055, code lost:
    
        if (r0 == null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0117, code lost:
    
        if (java.lang.Math.max(r1 == null ? 0 : r1.participants_count, r0.participants_count) >= getMessagesController().forumUpgradeParticipantsMin) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0121, code lost:
    
        if (r0.linked_chat_id != 0) goto L49;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x019e  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        NotificationCenter notificationCenter;
        int i;
        boolean z = true;
        if (this.chatId != 0) {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            this.currentChat = chat;
            if (chat == null) {
                TLRPC.Chat chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(this.chatId);
                this.currentChat = chatSync;
                if (chatSync != null) {
                    getMessagesController().putChat(this.currentChat, true);
                    if (this.info == null) {
                        TLRPC.ChatFull loadChatInfo = MessagesStorage.getInstance(this.currentAccount).loadChatInfo(this.chatId, ChatObject.isChannel(this.currentChat), new CountDownLatch(1), false, false);
                        this.info = loadChatInfo;
                    }
                }
                return false;
            }
        } else {
            TLRPC.User user = this.userId == 0 ? null : getMessagesController().getUser(Long.valueOf(this.userId));
            this.currentUser = user;
            if (user == null) {
                TLRPC.User userSync = MessagesStorage.getInstance(this.currentAccount).getUserSync(this.userId);
                this.currentUser = userSync;
                if (userSync != null) {
                    getMessagesController().putUser(this.currentUser, true);
                    if (this.userInfo == null) {
                        HashSet<Long> hashSet = new HashSet<>();
                        hashSet.add(Long.valueOf(this.userId));
                        ArrayList<TLRPC.UserFull> loadUserInfos = MessagesStorage.getInstance(this.currentAccount).loadUserInfos(hashSet);
                        if (!loadUserInfos.isEmpty()) {
                            this.userInfo = loadUserInfos.get(0);
                        }
                    }
                }
                return false;
            }
        }
        TLRPC.Chat chat2 = this.currentChat;
        if (chat2 == null) {
            this.avatarDrawable.setInfo(5L, this.currentUser.first_name, null);
            this.isChannel = false;
            this.forum = false;
            this.canForum = false;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoad);
            if (this.currentUser.bot) {
                notificationCenter = NotificationCenter.getInstance(this.currentAccount);
                i = NotificationCenter.botStarsUpdated;
            }
            ImageUpdater imageUpdater = this.imageUpdater;
            imageUpdater.parentFragment = this;
            imageUpdater.setDelegate(this);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogDeleted);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.channelRightsUpdated);
            if (this.info != null) {
                loadLinksCount();
            }
            return super.onFragmentCreate();
        }
        this.chatAdminRights = TLRPC.TL_chatAdminRights.clone(chat2.admin_rights);
        this.chatBannedRights = TLRPC.TL_chatBannedRights.clone(this.currentChat.banned_rights);
        this.chatDefaultBannedRights = TLRPC.TL_chatBannedRights.clone(this.currentChat.default_banned_rights);
        this.avatarDrawable.setInfo(5L, this.currentChat.title, null);
        this.isChannel = ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup;
        TLRPC.Chat chat3 = this.currentChat;
        boolean z2 = chat3.forum;
        this.forum = z2;
        if (this.userId == 0) {
            if (!z2) {
                TLRPC.ChatFull chatFull = this.info;
            }
            TLRPC.ChatFull chatFull2 = this.info;
            if (chatFull2 != null) {
            }
            this.canForum = z;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatAvailableReactionsUpdated);
            notificationCenter = NotificationCenter.getInstance(this.currentAccount);
            i = NotificationCenter.channelConnectedBotsUpdate;
        }
        z = false;
        this.canForum = z;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatAvailableReactionsUpdated);
        notificationCenter = NotificationCenter.getInstance(this.currentAccount);
        i = NotificationCenter.channelConnectedBotsUpdate;
        notificationCenter.addObserver(this, i);
        ImageUpdater imageUpdater2 = this.imageUpdater;
        imageUpdater2.parentFragment = this;
        imageUpdater2.setDelegate(this);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogDeleted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.channelRightsUpdated);
        if (this.info != null) {
        }
        return super.onFragmentCreate();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onFragmentDestroy() {
        NotificationCenter notificationCenter;
        int i;
        EditTextEmoji editTextEmoji;
        super.onFragmentDestroy();
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        if (this.currentChat == null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoad);
            if (this.currentUser.bot) {
                notificationCenter = NotificationCenter.getInstance(this.currentAccount);
                i = NotificationCenter.botStarsUpdated;
            }
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogDeleted);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.channelRightsUpdated);
            editTextEmoji = this.nameTextView;
            if (editTextEmoji == null) {
                editTextEmoji.onDestroy();
                return;
            }
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatAvailableReactionsUpdated);
        notificationCenter = NotificationCenter.getInstance(this.currentAccount);
        i = NotificationCenter.channelConnectedBotsUpdate;
        notificationCenter.removeObserver(this, i);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.channelRightsUpdated);
        editTextEmoji = this.nameTextView;
        if (editTextEmoji == null) {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        ReactionsUtils.stopPreloadReactions(this.preloadedReactions);
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
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        this.imageUpdater.onRequestPermissionsResultFragment(i, strArr, iArr);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onResume();
            this.nameTextView.getEditText().requestFocus();
        }
        updateColorCell();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        updateFields(true, true);
        this.imageUpdater.onResume();
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void onUploadProgressChanged(float f) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(f);
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
            if (obj.length() != 0) {
                bundle.putString("nameTextView", obj);
            }
        }
    }

    public void setInfo(TLRPC.ChatFull chatFull) {
        this.info = chatFull;
        if (chatFull != null) {
            if (this.currentChat == null) {
                this.currentChat = getMessagesController().getChat(Long.valueOf(this.chatId));
            }
            this.historyHidden = !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory;
            this.availableReactions = this.info.available_reactions;
            this.preloadedReactions.clear();
            this.preloadedReactions.addAll(ReactionsUtils.startPreloadReactions(this.currentChat, this.info));
            if (this.channelAffiliateProgramsCell != null && getMessagesController().starrefConnectAllowed && ChatObject.isChannelAndNotMegaGroup(this.currentChat)) {
                this.channelAffiliateProgramsCell.setVisibility(0);
            }
        }
    }

    public void setInfo(TLRPC.UserFull userFull) {
        TL_bots.BotInfo botInfo;
        TL_bots.BotInfo botInfo2;
        this.userInfo = userFull;
        if (userFull != null) {
            if (this.currentUser == null) {
                this.currentUser = this.userId == 0 ? null : getMessagesController().getUser(Long.valueOf(this.userId));
            }
            TextCell textCell = this.botAffiliateProgramCell;
            if (textCell != null) {
                textCell.setDrawLoading(this.userInfo == null, 45, true);
                TLRPC.UserFull userFull2 = this.userInfo;
                if (userFull2 != null) {
                    this.botAffiliateProgramCell.setValue(userFull2.starref_program == null ? LocaleController.getString(R.string.AffiliateProgramBotOff) : String.format(Locale.US, "%.1f%%", Float.valueOf(r7.commission_permille / 10.0f)), false);
                }
            }
            TextCell textCell2 = this.verifyCell;
            if (textCell2 != null) {
                TLRPC.UserFull userFull3 = this.userInfo;
                textCell2.setVisibility((userFull3 == null || (botInfo2 = userFull3.bot_info) == null || botInfo2.verifier_settings == null) ? 8 : 0);
            }
            TextInfoPrivacyCell textInfoPrivacyCell = this.verifyInfoCell;
            if (textInfoPrivacyCell != null) {
                TLRPC.UserFull userFull4 = this.userInfo;
                textInfoPrivacyCell.setVisibility((userFull4 == null || (botInfo = userFull4.bot_info) == null || botInfo.verifier_settings == null) ? 8 : 0);
            }
        }
    }

    public void showConvertTooltip() {
        this.undoView.showWithAction(0L, 76, (Runnable) null);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ boolean supportsBulletin() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$supportsBulletin(this);
    }

    public void updateColorCell() {
        TextCell textCell;
        PeerColorActivity.ChangeNameColorCell changeNameColorCell = this.colorCell;
        if (changeNameColorCell != null) {
            TLRPC.Chat chat = this.currentChat;
            TextCell textCell2 = this.historyCell;
            changeNameColorCell.set(chat, (textCell2 != null && textCell2.getVisibility() == 0) || ((textCell = this.forumsCell) != null && textCell.getVisibility() == 0) || (ChatObject.isMegagroup(this.currentChat) && ChatObject.hasAdminRights(this.currentChat)));
        }
    }
}
