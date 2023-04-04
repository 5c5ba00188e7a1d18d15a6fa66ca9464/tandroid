package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.collection.LongSparseArray;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$ChannelParticipantsFilter;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$TL_channelFull;
import org.telegram.tgnet.TLRPC$TL_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantBanned;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_channelParticipantSelf;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsAdmins;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsBanned;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsBots;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsContacts;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsKicked;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsRecent;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_editBanned;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_toggleAntiSpam;
import org.telegram.tgnet.TLRPC$TL_channels_toggleParticipantsHidden;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatParticipant;
import org.telegram.tgnet.TLRPC$TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_chatParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.ChatUsersActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.GigagroupConvertAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.GroupCreateActivity;
/* loaded from: classes3.dex */
public class ChatUsersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int addNew2Row;
    private int addNewRow;
    private int addNewSectionRow;
    private int addUsersRow;
    private int antiSpamInfoRow;
    private int antiSpamRow;
    private boolean antiSpamToggleLoading;
    private int blockedEmptyRow;
    private int botEndRow;
    private int botHeaderRow;
    private int botStartRow;
    private ArrayList<TLObject> bots;
    private boolean botsEndReached;
    private LongSparseArray<TLObject> botsMap;
    private int changeInfoRow;
    private long chatId;
    private ArrayList<TLObject> contacts;
    private boolean contactsEndReached;
    private int contactsEndRow;
    private int contactsHeaderRow;
    private LongSparseArray<TLObject> contactsMap;
    private int contactsStartRow;
    private TLRPC$Chat currentChat;
    private TLRPC$TL_chatBannedRights defaultBannedRights;
    private int delayResults;
    private ChatUsersActivityDelegate delegate;
    private ActionBarMenuItem doneItem;
    private int embedLinksRow;
    private StickerEmptyView emptyView;
    private boolean firstLoaded;
    private FlickerLoadingView flickerLoadingView;
    private int gigaConvertRow;
    private int gigaHeaderRow;
    private int gigaInfoRow;
    private int hideMembersInfoRow;
    private int hideMembersRow;
    private boolean hideMembersToggleLoading;
    private LongSparseArray<TLRPC$TL_groupCallParticipant> ignoredUsers;
    private TLRPC$ChatFull info;
    private String initialBannedRights;
    private int initialSlowmode;
    private boolean isChannel;
    private boolean isForum;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private int loadingHeaderRow;
    private int loadingProgressRow;
    private int loadingUserCellRow;
    private boolean loadingUsers;
    private int manageTopicsRow;
    private int membersHeaderRow;
    private boolean needOpenSearch;
    private boolean openTransitionStarted;
    private ArrayList<TLObject> participants;
    private int participantsDivider2Row;
    private int participantsDividerRow;
    private int participantsEndRow;
    private int participantsInfoRow;
    private LongSparseArray<TLObject> participantsMap;
    private int participantsStartRow;
    private int permissionsSectionRow;
    private int pinMessagesRow;
    private View progressBar;
    private int recentActionsRow;
    private int removedUsersRow;
    private int restricted1SectionRow;
    private int rowCount;
    private ActionBarMenuItem searchItem;
    private SearchAdapter searchListViewAdapter;
    private boolean searching;
    private int selectType;
    private int selectedSlowmode;
    private int sendMediaEmbededLinksRow;
    private boolean sendMediaExpanded;
    private int sendMediaFilesRow;
    private int sendMediaMusicRow;
    private int sendMediaPhotosRow;
    private int sendMediaRow;
    private int sendMediaStickerGifsRow;
    private int sendMediaVideoMessagesRow;
    private int sendMediaVideosRow;
    private int sendMediaVoiceMessagesRow;
    private int sendMessagesRow;
    private int sendPollsRow;
    private int sendStickersRow;
    private int slowmodeInfoRow;
    private int slowmodeRow;
    private int slowmodeSelectRow;
    private int type;
    private UndoView undoView;

    /* loaded from: classes3.dex */
    public interface ChatUsersActivityDelegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static void $default$didChangeOwner(ChatUsersActivityDelegate chatUsersActivityDelegate, TLRPC$User tLRPC$User) {
            }

            public static void $default$didKickParticipant(ChatUsersActivityDelegate chatUsersActivityDelegate, long j) {
            }

            public static void $default$didSelectUser(ChatUsersActivityDelegate chatUsersActivityDelegate, long j) {
            }
        }

        void didAddParticipantToList(long j, TLObject tLObject);

        void didChangeOwner(TLRPC$User tLRPC$User);

        void didKickParticipant(long j);

        void didSelectUser(long j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSecondsForIndex(int i) {
        if (i == 1) {
            return 10;
        }
        if (i == 2) {
            return 30;
        }
        if (i == 3) {
            return 60;
        }
        if (i == 4) {
            return 300;
        }
        if (i == 5) {
            return 900;
        }
        return i == 6 ? 3600 : 0;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean needDelayOpenAnimation() {
        return true;
    }

    public ChatUsersActivity(Bundle bundle) {
        super(bundle);
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        this.defaultBannedRights = new TLRPC$TL_chatBannedRights();
        this.participants = new ArrayList<>();
        this.bots = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.participantsMap = new LongSparseArray<>();
        this.botsMap = new LongSparseArray<>();
        this.contactsMap = new LongSparseArray<>();
        this.chatId = this.arguments.getLong("chat_id");
        this.type = this.arguments.getInt("type");
        this.needOpenSearch = this.arguments.getBoolean("open_search");
        this.selectType = this.arguments.getInt("selectType");
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        boolean z = false;
        if (chat != null && (tLRPC$TL_chatBannedRights = chat.default_banned_rights) != null) {
            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = this.defaultBannedRights;
            tLRPC$TL_chatBannedRights2.view_messages = tLRPC$TL_chatBannedRights.view_messages;
            tLRPC$TL_chatBannedRights2.send_stickers = tLRPC$TL_chatBannedRights.send_stickers;
            boolean z2 = tLRPC$TL_chatBannedRights.send_media;
            tLRPC$TL_chatBannedRights2.send_media = z2;
            tLRPC$TL_chatBannedRights2.embed_links = tLRPC$TL_chatBannedRights.embed_links;
            tLRPC$TL_chatBannedRights2.send_messages = tLRPC$TL_chatBannedRights.send_messages;
            tLRPC$TL_chatBannedRights2.send_games = tLRPC$TL_chatBannedRights.send_games;
            tLRPC$TL_chatBannedRights2.send_inline = tLRPC$TL_chatBannedRights.send_inline;
            tLRPC$TL_chatBannedRights2.send_gifs = tLRPC$TL_chatBannedRights.send_gifs;
            tLRPC$TL_chatBannedRights2.pin_messages = tLRPC$TL_chatBannedRights.pin_messages;
            tLRPC$TL_chatBannedRights2.send_polls = tLRPC$TL_chatBannedRights.send_polls;
            tLRPC$TL_chatBannedRights2.invite_users = tLRPC$TL_chatBannedRights.invite_users;
            tLRPC$TL_chatBannedRights2.manage_topics = tLRPC$TL_chatBannedRights.manage_topics;
            tLRPC$TL_chatBannedRights2.change_info = tLRPC$TL_chatBannedRights.change_info;
            boolean z3 = tLRPC$TL_chatBannedRights.send_photos;
            tLRPC$TL_chatBannedRights2.send_photos = z3;
            boolean z4 = tLRPC$TL_chatBannedRights.send_videos;
            tLRPC$TL_chatBannedRights2.send_videos = z4;
            boolean z5 = tLRPC$TL_chatBannedRights.send_roundvideos;
            tLRPC$TL_chatBannedRights2.send_roundvideos = z5;
            boolean z6 = tLRPC$TL_chatBannedRights.send_audios;
            tLRPC$TL_chatBannedRights2.send_audios = z6;
            boolean z7 = tLRPC$TL_chatBannedRights.send_voices;
            tLRPC$TL_chatBannedRights2.send_voices = z7;
            boolean z8 = tLRPC$TL_chatBannedRights.send_docs;
            tLRPC$TL_chatBannedRights2.send_docs = z8;
            tLRPC$TL_chatBannedRights2.send_plain = tLRPC$TL_chatBannedRights.send_plain;
            if (!z2 && z8 && z7 && z6 && z5 && z4 && z3) {
                tLRPC$TL_chatBannedRights2.send_photos = false;
                tLRPC$TL_chatBannedRights2.send_videos = false;
                tLRPC$TL_chatBannedRights2.send_roundvideos = false;
                tLRPC$TL_chatBannedRights2.send_audios = false;
                tLRPC$TL_chatBannedRights2.send_voices = false;
                tLRPC$TL_chatBannedRights2.send_docs = false;
            }
        }
        this.initialBannedRights = ChatObject.getBannedRightsString(this.defaultBannedRights);
        if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
            z = true;
        }
        this.isChannel = z;
        this.isForum = ChatObject.isForum(this.currentChat);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x014d, code lost:
        if (org.telegram.messenger.ChatObject.canBlockUsers(r1) != false) goto L31;
     */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01f4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateRows() {
        boolean z;
        boolean z2;
        TLRPC$ChatFull tLRPC$ChatFull;
        boolean z3;
        TLRPC$ChatFull tLRPC$ChatFull2;
        boolean z4;
        TLRPC$ChatFull tLRPC$ChatFull3;
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        if (chat == null) {
            return;
        }
        this.recentActionsRow = -1;
        this.antiSpamRow = -1;
        this.antiSpamInfoRow = -1;
        this.addNewRow = -1;
        this.addNew2Row = -1;
        this.hideMembersRow = -1;
        this.hideMembersInfoRow = -1;
        this.addNewSectionRow = -1;
        this.restricted1SectionRow = -1;
        this.participantsStartRow = -1;
        this.participantsDividerRow = -1;
        this.participantsDivider2Row = -1;
        this.gigaInfoRow = -1;
        this.gigaConvertRow = -1;
        this.gigaHeaderRow = -1;
        this.participantsEndRow = -1;
        this.participantsInfoRow = -1;
        this.blockedEmptyRow = -1;
        this.permissionsSectionRow = -1;
        this.sendMessagesRow = -1;
        this.sendMediaRow = -1;
        this.sendStickersRow = -1;
        this.sendPollsRow = -1;
        this.embedLinksRow = -1;
        this.addUsersRow = -1;
        this.manageTopicsRow = -1;
        this.pinMessagesRow = -1;
        this.changeInfoRow = -1;
        this.removedUsersRow = -1;
        this.contactsHeaderRow = -1;
        this.contactsStartRow = -1;
        this.contactsEndRow = -1;
        this.botHeaderRow = -1;
        this.botStartRow = -1;
        this.botEndRow = -1;
        this.membersHeaderRow = -1;
        this.slowmodeRow = -1;
        this.slowmodeSelectRow = -1;
        this.slowmodeInfoRow = -1;
        this.loadingProgressRow = -1;
        this.loadingUserCellRow = -1;
        this.loadingHeaderRow = -1;
        this.sendMediaPhotosRow = -1;
        this.sendMediaVideosRow = -1;
        this.sendMediaStickerGifsRow = -1;
        this.sendMediaMusicRow = -1;
        this.sendMediaFilesRow = -1;
        this.sendMediaVoiceMessagesRow = -1;
        this.sendMediaVideoMessagesRow = -1;
        this.sendMediaEmbededLinksRow = -1;
        this.rowCount = 0;
        int i = this.type;
        int i2 = 1;
        if (i == 3) {
            int i3 = 0 + 1;
            this.rowCount = i3;
            this.permissionsSectionRow = 0;
            int i4 = i3 + 1;
            this.rowCount = i4;
            this.sendMessagesRow = i3;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.sendMediaRow = i4;
            if (this.sendMediaExpanded) {
                int i6 = i5 + 1;
                this.rowCount = i6;
                this.sendMediaPhotosRow = i5;
                int i7 = i6 + 1;
                this.rowCount = i7;
                this.sendMediaVideosRow = i6;
                int i8 = i7 + 1;
                this.rowCount = i8;
                this.sendMediaStickerGifsRow = i7;
                int i9 = i8 + 1;
                this.rowCount = i9;
                this.sendMediaMusicRow = i8;
                int i10 = i9 + 1;
                this.rowCount = i10;
                this.sendMediaFilesRow = i9;
                int i11 = i10 + 1;
                this.rowCount = i11;
                this.sendMediaVoiceMessagesRow = i10;
                int i12 = i11 + 1;
                this.rowCount = i12;
                this.sendMediaVideoMessagesRow = i11;
                int i13 = i12 + 1;
                this.rowCount = i13;
                this.sendMediaEmbededLinksRow = i12;
                this.rowCount = i13 + 1;
                this.sendPollsRow = i13;
            }
            int i14 = this.rowCount;
            int i15 = i14 + 1;
            this.rowCount = i15;
            this.addUsersRow = i14;
            int i16 = i15 + 1;
            this.rowCount = i16;
            this.pinMessagesRow = i15;
            int i17 = i16 + 1;
            this.rowCount = i17;
            this.changeInfoRow = i16;
            if (this.isForum) {
                this.rowCount = i17 + 1;
                this.manageTopicsRow = i17;
            }
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                if (tLRPC$Chat.creator && tLRPC$Chat.megagroup && !tLRPC$Chat.gigagroup) {
                    int i18 = tLRPC$Chat.participants_count;
                    TLRPC$ChatFull tLRPC$ChatFull4 = this.info;
                    if (Math.max(i18, tLRPC$ChatFull4 != null ? tLRPC$ChatFull4.participants_count : 0) >= getMessagesController().maxMegagroupCount - 1000) {
                        int i19 = this.rowCount;
                        int i20 = i19 + 1;
                        this.rowCount = i20;
                        this.participantsDivider2Row = i19;
                        int i21 = i20 + 1;
                        this.rowCount = i21;
                        this.gigaHeaderRow = i20;
                        int i22 = i21 + 1;
                        this.rowCount = i22;
                        this.gigaConvertRow = i21;
                        this.rowCount = i22 + 1;
                        this.gigaInfoRow = i22;
                    }
                }
            }
            if (ChatObject.isChannel(this.currentChat) || !this.currentChat.creator) {
                TLRPC$Chat tLRPC$Chat2 = this.currentChat;
                if (tLRPC$Chat2.megagroup) {
                    if (!tLRPC$Chat2.gigagroup) {
                    }
                }
                if (ChatObject.isChannel(this.currentChat)) {
                    if (this.participantsDivider2Row == -1) {
                        int i23 = this.rowCount;
                        this.rowCount = i23 + 1;
                        this.participantsDivider2Row = i23;
                    }
                    int i24 = this.rowCount;
                    this.rowCount = i24 + 1;
                    this.removedUsersRow = i24;
                }
                if ((this.slowmodeInfoRow == -1 && this.gigaHeaderRow == -1) || this.removedUsersRow != -1) {
                    int i25 = this.rowCount;
                    this.rowCount = i25 + 1;
                    this.participantsDividerRow = i25;
                }
                if (ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
                    int i26 = this.rowCount;
                    this.rowCount = i26 + 1;
                    this.addNewRow = i26;
                }
                if (!this.loadingUsers && !(z4 = this.firstLoaded)) {
                    if (z4 || (tLRPC$ChatFull3 = this.info) == null || tLRPC$ChatFull3.banned_count <= 0) {
                        return;
                    }
                    int i27 = this.rowCount;
                    this.rowCount = i27 + 1;
                    this.loadingUserCellRow = i27;
                    return;
                }
                if (!this.participants.isEmpty()) {
                    int i28 = this.rowCount;
                    this.participantsStartRow = i28;
                    int size = i28 + this.participants.size();
                    this.rowCount = size;
                    this.participantsEndRow = size;
                }
                if (this.addNewRow == -1 || this.participantsStartRow != -1) {
                    int i29 = this.rowCount;
                    this.rowCount = i29 + 1;
                    this.addNewSectionRow = i29;
                }
                return;
            }
            if (this.participantsDivider2Row == -1) {
                int i30 = this.rowCount;
                this.rowCount = i30 + 1;
                this.participantsDivider2Row = i30;
            }
            int i31 = this.rowCount;
            int i32 = i31 + 1;
            this.rowCount = i32;
            this.slowmodeRow = i31;
            int i33 = i32 + 1;
            this.rowCount = i33;
            this.slowmodeSelectRow = i32;
            this.rowCount = i33 + 1;
            this.slowmodeInfoRow = i33;
            if (ChatObject.isChannel(this.currentChat)) {
            }
            if (this.slowmodeInfoRow == -1) {
                int i252 = this.rowCount;
                this.rowCount = i252 + 1;
                this.participantsDividerRow = i252;
                if (ChatObject.canBlockUsers(this.currentChat)) {
                    int i262 = this.rowCount;
                    this.rowCount = i262 + 1;
                    this.addNewRow = i262;
                }
                if (!this.loadingUsers) {
                }
                if (!this.participants.isEmpty()) {
                }
                if (this.addNewRow == -1) {
                }
                int i292 = this.rowCount;
                this.rowCount = i292 + 1;
                this.addNewSectionRow = i292;
            }
            int i2522 = this.rowCount;
            this.rowCount = i2522 + 1;
            this.participantsDividerRow = i2522;
            if (ChatObject.canBlockUsers(this.currentChat)) {
            }
            if (!this.loadingUsers) {
            }
            if (!this.participants.isEmpty()) {
            }
            if (this.addNewRow == -1) {
            }
            int i2922 = this.rowCount;
            this.rowCount = i2922 + 1;
            this.addNewSectionRow = i2922;
        } else if (i == 0) {
            if (ChatObject.canBlockUsers(this.currentChat)) {
                int i34 = this.rowCount;
                this.rowCount = i34 + 1;
                this.addNewRow = i34;
                if (!this.participants.isEmpty() || (this.loadingUsers && !this.firstLoaded && (tLRPC$ChatFull2 = this.info) != null && tLRPC$ChatFull2.kicked_count > 0)) {
                    int i35 = this.rowCount;
                    this.rowCount = i35 + 1;
                    this.participantsInfoRow = i35;
                }
            }
            if (this.loadingUsers && !(z3 = this.firstLoaded)) {
                if (z3) {
                    return;
                }
                int i36 = this.rowCount;
                int i37 = i36 + 1;
                this.rowCount = i37;
                this.restricted1SectionRow = i36;
                this.rowCount = i37 + 1;
                this.loadingUserCellRow = i37;
                return;
            }
            if (!this.participants.isEmpty()) {
                int i38 = this.rowCount;
                int i39 = i38 + 1;
                this.rowCount = i39;
                this.restricted1SectionRow = i38;
                this.participantsStartRow = i39;
                int size2 = i39 + this.participants.size();
                this.rowCount = size2;
                this.participantsEndRow = size2;
            }
            if (this.participantsStartRow != -1) {
                if (this.participantsInfoRow == -1) {
                    int i40 = this.rowCount;
                    this.rowCount = i40 + 1;
                    this.participantsInfoRow = i40;
                    return;
                }
                int i41 = this.rowCount;
                this.rowCount = i41 + 1;
                this.addNewSectionRow = i41;
                return;
            }
            int i42 = this.rowCount;
            this.rowCount = i42 + 1;
            this.blockedEmptyRow = i42;
        } else if (i == 1) {
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC$Chat tLRPC$Chat3 = this.currentChat;
                if (tLRPC$Chat3.megagroup && !tLRPC$Chat3.gigagroup && ((tLRPC$ChatFull = this.info) == null || tLRPC$ChatFull.participants_count <= 200 || (!this.isChannel && tLRPC$ChatFull.can_set_stickers))) {
                    int i43 = this.rowCount;
                    this.rowCount = i43 + 1;
                    this.recentActionsRow = i43;
                    if (ChatObject.hasAdminRights(tLRPC$Chat3)) {
                        int i44 = this.rowCount;
                        int i45 = i44 + 1;
                        this.rowCount = i45;
                        this.antiSpamRow = i44;
                        this.rowCount = i45 + 1;
                        this.antiSpamInfoRow = i45;
                    } else {
                        int i46 = this.rowCount;
                        this.rowCount = i46 + 1;
                        this.addNewSectionRow = i46;
                    }
                }
            }
            if (ChatObject.canAddAdmins(this.currentChat)) {
                int i47 = this.rowCount;
                this.rowCount = i47 + 1;
                this.addNewRow = i47;
            }
            if (this.loadingUsers && !(z2 = this.firstLoaded)) {
                if (z2) {
                    return;
                }
                int i48 = this.rowCount;
                this.rowCount = i48 + 1;
                this.loadingUserCellRow = i48;
                return;
            }
            if (!this.participants.isEmpty()) {
                int i49 = this.rowCount;
                this.participantsStartRow = i49;
                int size3 = i49 + this.participants.size();
                this.rowCount = size3;
                this.participantsEndRow = size3;
            }
            int i50 = this.rowCount;
            this.rowCount = i50 + 1;
            this.participantsInfoRow = i50;
        } else if (i == 2) {
            if (!ChatObject.isChannelAndNotMegaGroup(this.currentChat) && !this.needOpenSearch) {
                int i51 = this.rowCount;
                int i52 = i51 + 1;
                this.rowCount = i52;
                this.hideMembersRow = i51;
                this.rowCount = i52 + 1;
                this.hideMembersInfoRow = i52;
            }
            if (this.selectType == 0 && ChatObject.canAddUsers(this.currentChat)) {
                int i53 = this.rowCount;
                this.rowCount = i53 + 1;
                this.addNewRow = i53;
            }
            if (this.selectType == 0 && ChatObject.canUserDoAdminAction(this.currentChat, 3)) {
                int i54 = this.rowCount;
                this.rowCount = i54 + 1;
                this.addNew2Row = i54;
            }
            if (this.loadingUsers && !(z = this.firstLoaded)) {
                if (z) {
                    return;
                }
                if (this.selectType == 0) {
                    int i55 = this.rowCount;
                    this.rowCount = i55 + 1;
                    this.loadingHeaderRow = i55;
                }
                int i56 = this.rowCount;
                this.rowCount = i56 + 1;
                this.loadingUserCellRow = i56;
                return;
            }
            if (!this.contacts.isEmpty()) {
                int i57 = this.rowCount;
                int i58 = i57 + 1;
                this.rowCount = i58;
                this.contactsHeaderRow = i57;
                this.contactsStartRow = i58;
                int size4 = i58 + this.contacts.size();
                this.rowCount = size4;
                this.contactsEndRow = size4;
                r1 = 1;
            }
            if (this.bots.isEmpty()) {
                i2 = r1;
            } else {
                int i59 = this.rowCount;
                int i60 = i59 + 1;
                this.rowCount = i60;
                this.botHeaderRow = i59;
                this.botStartRow = i60;
                int size5 = i60 + this.bots.size();
                this.rowCount = size5;
                this.botEndRow = size5;
            }
            if (!this.participants.isEmpty()) {
                if (i2 != 0) {
                    int i61 = this.rowCount;
                    this.rowCount = i61 + 1;
                    this.membersHeaderRow = i61;
                }
                int i62 = this.rowCount;
                this.participantsStartRow = i62;
                int size6 = i62 + this.participants.size();
                this.rowCount = size6;
                this.participantsEndRow = size6;
            }
            int i63 = this.rowCount;
            if (i63 != 0) {
                this.rowCount = i63 + 1;
                this.participantsInfoRow = i63;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
        loadChatParticipants(0, 200);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        int i;
        this.searching = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.type;
        if (i2 == 3) {
            this.actionBar.setTitle(LocaleController.getString("ChannelPermissions", R.string.ChannelPermissions));
        } else if (i2 == 0) {
            this.actionBar.setTitle(LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist));
        } else if (i2 == 1) {
            this.actionBar.setTitle(LocaleController.getString("ChannelAdministrators", R.string.ChannelAdministrators));
        } else if (i2 == 2) {
            int i3 = this.selectType;
            if (i3 == 0) {
                if (this.isChannel) {
                    this.actionBar.setTitle(LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("ChannelMembers", R.string.ChannelMembers));
                }
            } else if (i3 == 1) {
                this.actionBar.setTitle(LocaleController.getString("ChannelAddAdmin", R.string.ChannelAddAdmin));
            } else if (i3 == 2) {
                this.actionBar.setTitle(LocaleController.getString("ChannelBlockUser", R.string.ChannelBlockUser));
            } else if (i3 == 3) {
                this.actionBar.setTitle(LocaleController.getString("ChannelAddException", R.string.ChannelAddException));
            }
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ChatUsersActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                if (i4 == -1) {
                    if (ChatUsersActivity.this.checkDiscard()) {
                        ChatUsersActivity.this.finishFragment();
                    }
                } else if (i4 == 1) {
                    ChatUsersActivity.this.processDone();
                }
            }
        });
        if (this.selectType != 0 || (i = this.type) == 2 || i == 0 || i == 3) {
            this.searchListViewAdapter = new SearchAdapter(context);
            ActionBarMenu createMenu = this.actionBar.createMenu();
            ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.ChatUsersActivity.2
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchExpand() {
                    ChatUsersActivity.this.searching = true;
                    if (ChatUsersActivity.this.doneItem != null) {
                        ChatUsersActivity.this.doneItem.setVisibility(8);
                    }
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchCollapse() {
                    ChatUsersActivity.this.searchListViewAdapter.searchUsers(null);
                    ChatUsersActivity.this.searching = false;
                    ChatUsersActivity.this.listView.setAnimateEmptyView(false, 0);
                    ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.listViewAdapter);
                    ChatUsersActivity.this.listViewAdapter.notifyDataSetChanged();
                    ChatUsersActivity.this.listView.setFastScrollVisible(true);
                    ChatUsersActivity.this.listView.setVerticalScrollBarEnabled(false);
                    if (ChatUsersActivity.this.doneItem != null) {
                        ChatUsersActivity.this.doneItem.setVisibility(0);
                    }
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onTextChanged(EditText editText) {
                    if (ChatUsersActivity.this.searchListViewAdapter == null) {
                        return;
                    }
                    String obj = editText.getText().toString();
                    int itemCount = ChatUsersActivity.this.listView.getAdapter() == null ? 0 : ChatUsersActivity.this.listView.getAdapter().getItemCount();
                    ChatUsersActivity.this.searchListViewAdapter.searchUsers(obj);
                    if (TextUtils.isEmpty(obj) && ChatUsersActivity.this.listView != null && ChatUsersActivity.this.listView.getAdapter() != ChatUsersActivity.this.listViewAdapter) {
                        ChatUsersActivity.this.listView.setAnimateEmptyView(false, 0);
                        ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.listViewAdapter);
                        if (itemCount == 0) {
                            ChatUsersActivity.this.showItemsAnimated(0);
                        }
                    }
                    ChatUsersActivity.this.progressBar.setVisibility(8);
                    ChatUsersActivity.this.flickerLoadingView.setVisibility(0);
                }
            });
            this.searchItem = actionBarMenuItemSearchListener;
            if (this.type == 0 && !this.firstLoaded) {
                actionBarMenuItemSearchListener.setVisibility(8);
            }
            if (this.type == 3) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("ChannelSearchException", R.string.ChannelSearchException));
            } else {
                this.searchItem.setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
            }
            if (!ChatObject.isChannel(this.currentChat) && !this.currentChat.creator) {
                this.searchItem.setVisibility(8);
            }
            if (this.type == 3) {
                this.doneItem = createMenu.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", R.string.Done));
            }
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.ChatUsersActivity.3
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.drawColor(Theme.getColor(ChatUsersActivity.this.listView.getAdapter() == ChatUsersActivity.this.searchListViewAdapter ? "windowBackgroundWhite" : "windowBackgroundGray"));
                super.dispatchDraw(canvas);
            }
        };
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        FrameLayout frameLayout3 = new FrameLayout(context);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.flickerLoadingView = flickerLoadingView;
        flickerLoadingView.setViewType(6);
        this.flickerLoadingView.showDate(false);
        this.flickerLoadingView.setUseHeaderOffset(true);
        frameLayout3.addView(this.flickerLoadingView);
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressBar = radialProgressView;
        frameLayout3.addView(radialProgressView, LayoutHelper.createFrame(-2, -2, 17));
        this.flickerLoadingView.setVisibility(8);
        this.progressBar.setVisibility(8);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, frameLayout3, 1);
        this.emptyView = stickerEmptyView;
        stickerEmptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
        this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
        this.emptyView.setVisibility(8);
        this.emptyView.setAnimateLayoutChange(true);
        this.emptyView.showProgress(true, false);
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.addView(frameLayout3, 0);
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.ChatUsersActivity.4
            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                if (((BaseFragment) ChatUsersActivity.this).fragmentView != null) {
                    ((BaseFragment) ChatUsersActivity.this).fragmentView.invalidate();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (ChatUsersActivity.this.permissionsSectionRow >= 0 && ChatUsersActivity.this.participantsDivider2Row >= 0) {
                    drawSectionBackground(canvas, ChatUsersActivity.this.permissionsSectionRow, Math.max(0, ChatUsersActivity.this.participantsDivider2Row - 1), getThemedColor("windowBackgroundWhite"));
                }
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.ChatUsersActivity.5
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int scrollVerticallyBy(int i4, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (!ChatUsersActivity.this.firstLoaded && ChatUsersActivity.this.type == 0 && ChatUsersActivity.this.participants.size() == 0) {
                    return 0;
                }
                return super.scrollVerticallyBy(i4, recycler, state);
            }
        };
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.ChatUsersActivity.6
            int animationIndex = -1;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onAllAnimationsDone() {
                super.onAllAnimationsDone();
                ChatUsersActivity.this.getNotificationCenter().onAnimationFinish(this.animationIndex);
            }

            @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
            public void runPendingAnimations() {
                boolean z = !this.mPendingRemovals.isEmpty();
                boolean z2 = !this.mPendingMoves.isEmpty();
                boolean z3 = !this.mPendingChanges.isEmpty();
                boolean z4 = !this.mPendingAdditions.isEmpty();
                if (z || z2 || z4 || z3) {
                    this.animationIndex = ChatUsersActivity.this.getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
                }
                super.runPendingAnimations();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                super.onMoveAnimationUpdate(viewHolder);
                ChatUsersActivity.this.listView.invalidate();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onChangeAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                super.onChangeAnimationUpdate(viewHolder);
                ChatUsersActivity.this.listView.invalidate();
            }
        };
        defaultItemAnimator.setDurations(320L);
        defaultItemAnimator.setMoveDelay(0L);
        defaultItemAnimator.setAddDelay(0L);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setAnimateEmptyView(true, 0);
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda21
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i4) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i4);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i4, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i4, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i4, float f, float f2) {
                ChatUsersActivity.this.lambda$createView$5(view, i4, f, f2);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda22
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i4) {
                boolean lambda$createView$6;
                lambda$createView$6 = ChatUsersActivity.this.lambda$createView$6(view, i4);
                return lambda$createView$6;
            }
        });
        if (this.searchItem != null) {
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ChatUsersActivity.12
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i4, int i5) {
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i4) {
                    if (i4 == 1) {
                        AndroidUtilities.hideKeyboard(ChatUsersActivity.this.getParentActivity().getCurrentFocus());
                    }
                }
            });
        }
        UndoView undoView = new UndoView(context);
        this.undoView = undoView;
        frameLayout2.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        updateRows();
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(false, 0);
        if (this.needOpenSearch) {
            this.searchItem.openSearch(false);
        }
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:317:0x0663 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0664  */
    /* JADX WARN: Type inference failed for: r19v1 */
    /* JADX WARN: Type inference failed for: r29v0, types: [org.telegram.ui.ActionBar.BaseFragment, org.telegram.ui.ChatUsersActivity] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$5(View view, int i, float f, float f2) {
        long j;
        final TLObject tLObject;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        String str;
        boolean z;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        long peerId;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        boolean canBlockUsers;
        boolean z2;
        int i2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights4;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights5;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights6;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights7;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights8;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights9;
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$ChatFull tLRPC$ChatFull2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights10;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights11;
        View findViewByPosition;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights12;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights13;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights14;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights15;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights16;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights17;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights18;
        int i3 = 0;
        boolean z3 = this.listView.getAdapter() == this.listViewAdapter;
        if (z3) {
            if (isExpandableSendMediaRow(i)) {
                CheckBoxCell checkBoxCell = (CheckBoxCell) view;
                if (i == this.sendMediaPhotosRow) {
                    this.defaultBannedRights.send_photos = !tLRPC$TL_chatBannedRights18.send_photos;
                } else if (i == this.sendMediaVideosRow) {
                    this.defaultBannedRights.send_videos = !tLRPC$TL_chatBannedRights17.send_videos;
                } else if (i == this.sendMediaStickerGifsRow) {
                    this.defaultBannedRights.send_stickers = !tLRPC$TL_chatBannedRights16.send_stickers;
                } else if (i == this.sendMediaMusicRow) {
                    this.defaultBannedRights.send_audios = !tLRPC$TL_chatBannedRights15.send_audios;
                } else if (i == this.sendMediaFilesRow) {
                    this.defaultBannedRights.send_docs = !tLRPC$TL_chatBannedRights14.send_docs;
                } else if (i == this.sendMediaVoiceMessagesRow) {
                    this.defaultBannedRights.send_voices = !tLRPC$TL_chatBannedRights13.send_voices;
                } else if (i == this.sendMediaVideoMessagesRow) {
                    this.defaultBannedRights.send_roundvideos = !tLRPC$TL_chatBannedRights12.send_roundvideos;
                } else if (i == this.sendMediaEmbededLinksRow) {
                    if (this.defaultBannedRights.send_plain && (findViewByPosition = this.layoutManager.findViewByPosition(this.sendMessagesRow)) != null) {
                        AndroidUtilities.shakeViewSpring(findViewByPosition);
                        BotWebViewVibrationEffect.APP_ERROR.vibrate();
                        return;
                    }
                    this.defaultBannedRights.embed_links = !tLRPC$TL_chatBannedRights11.embed_links;
                } else if (i == this.sendPollsRow) {
                    this.defaultBannedRights.send_polls = !tLRPC$TL_chatBannedRights10.send_polls;
                }
                checkBoxCell.setChecked(!checkBoxCell.isChecked(), true);
                AndroidUtilities.updateVisibleRows(this.listView);
            } else if (i == this.addNewRow) {
                int i4 = this.type;
                if (i4 == 0 || i4 == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", this.chatId);
                    bundle.putInt("type", 2);
                    bundle.putInt("selectType", this.type == 0 ? 2 : 3);
                    ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
                    chatUsersActivity.setInfo(this.info);
                    chatUsersActivity.setBannedRights(this.defaultBannedRights);
                    chatUsersActivity.setDelegate(new ChatUsersActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.7
                        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
                        public /* synthetic */ void didChangeOwner(TLRPC$User tLRPC$User) {
                            ChatUsersActivityDelegate.-CC.$default$didChangeOwner(this, tLRPC$User);
                        }

                        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
                        public /* synthetic */ void didSelectUser(long j2) {
                            ChatUsersActivityDelegate.-CC.$default$didSelectUser(this, j2);
                        }

                        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
                        public void didAddParticipantToList(long j2, TLObject tLObject2) {
                            if (ChatUsersActivity.this.participantsMap.get(j2) == null) {
                                DiffCallback saveState = ChatUsersActivity.this.saveState();
                                ChatUsersActivity.this.participants.add(tLObject2);
                                ChatUsersActivity.this.participantsMap.put(j2, tLObject2);
                                ChatUsersActivity chatUsersActivity2 = ChatUsersActivity.this;
                                chatUsersActivity2.sortUsers(chatUsersActivity2.participants);
                                ChatUsersActivity.this.updateListAnimated(saveState);
                            }
                        }

                        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
                        public void didKickParticipant(long j2) {
                            if (ChatUsersActivity.this.participantsMap.get(j2) == null) {
                                DiffCallback saveState = ChatUsersActivity.this.saveState();
                                TLRPC$TL_channelParticipantBanned tLRPC$TL_channelParticipantBanned = new TLRPC$TL_channelParticipantBanned();
                                if (j2 > 0) {
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                    tLRPC$TL_channelParticipantBanned.peer = tLRPC$TL_peerUser;
                                    tLRPC$TL_peerUser.user_id = j2;
                                } else {
                                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                    tLRPC$TL_channelParticipantBanned.peer = tLRPC$TL_peerChannel;
                                    tLRPC$TL_peerChannel.channel_id = -j2;
                                }
                                tLRPC$TL_channelParticipantBanned.date = ChatUsersActivity.this.getConnectionsManager().getCurrentTime();
                                tLRPC$TL_channelParticipantBanned.kicked_by = ChatUsersActivity.this.getAccountInstance().getUserConfig().clientUserId;
                                ChatUsersActivity.this.info.kicked_count++;
                                ChatUsersActivity.this.participants.add(tLRPC$TL_channelParticipantBanned);
                                ChatUsersActivity.this.participantsMap.put(j2, tLRPC$TL_channelParticipantBanned);
                                ChatUsersActivity chatUsersActivity2 = ChatUsersActivity.this;
                                chatUsersActivity2.sortUsers(chatUsersActivity2.participants);
                                ChatUsersActivity.this.updateListAnimated(saveState);
                            }
                        }
                    });
                    presentFragment(chatUsersActivity);
                    return;
                } else if (i4 == 1) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", this.chatId);
                    bundle2.putInt("type", 2);
                    bundle2.putInt("selectType", 1);
                    ChatUsersActivity chatUsersActivity2 = new ChatUsersActivity(bundle2);
                    chatUsersActivity2.setDelegate(new 8());
                    chatUsersActivity2.setInfo(this.info);
                    presentFragment(chatUsersActivity2);
                    return;
                } else if (i4 == 2) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("addToGroup", true);
                    bundle3.putLong(this.isChannel ? "channelId" : "chatId", this.currentChat.id);
                    GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle3);
                    groupCreateActivity.setInfo(this.info);
                    LongSparseArray<TLObject> longSparseArray = this.contactsMap;
                    groupCreateActivity.setIgnoreUsers((longSparseArray == null || longSparseArray.size() == 0) ? this.participantsMap : this.contactsMap);
                    groupCreateActivity.setDelegate(new 9(groupCreateActivity));
                    presentFragment(groupCreateActivity);
                    return;
                } else {
                    return;
                }
            } else if (i == this.recentActionsRow) {
                presentFragment(new ChannelAdminLogActivity(this.currentChat));
                return;
            } else if (i == this.antiSpamRow) {
                final TextCell textCell = (TextCell) view;
                TLRPC$ChatFull tLRPC$ChatFull3 = this.info;
                if (tLRPC$ChatFull3 != null && !tLRPC$ChatFull3.antispam && tLRPC$ChatFull3.participants_count < getMessagesController().telegramAntispamGroupSizeMin) {
                    BulletinFactory.of(this).createSimpleBulletin(R.raw.msg_antispam, AndroidUtilities.replaceTags(LocaleController.formatPluralString("ChannelAntiSpamForbidden", getMessagesController().telegramAntispamGroupSizeMin, new Object[0]))).show();
                    return;
                } else if (this.info == null || !ChatObject.canUserDoAdminAction(this.currentChat, 13) || this.antiSpamToggleLoading) {
                    return;
                } else {
                    this.antiSpamToggleLoading = true;
                    final boolean z4 = this.info.antispam;
                    TLRPC$TL_channels_toggleAntiSpam tLRPC$TL_channels_toggleAntiSpam = new TLRPC$TL_channels_toggleAntiSpam();
                    tLRPC$TL_channels_toggleAntiSpam.channel = getMessagesController().getInputChannel(this.chatId);
                    TLRPC$ChatFull tLRPC$ChatFull4 = this.info;
                    boolean z5 = true ^ tLRPC$ChatFull4.antispam;
                    tLRPC$ChatFull4.antispam = z5;
                    tLRPC$TL_channels_toggleAntiSpam.enabled = z5;
                    textCell.setChecked(z5);
                    Switch checkBox = textCell.getCheckBox();
                    if (!ChatObject.canUserDoAdminAction(this.currentChat, 13) || ((tLRPC$ChatFull2 = this.info) != null && !tLRPC$ChatFull2.antispam && tLRPC$ChatFull2.participants_count < getMessagesController().telegramAntispamGroupSizeMin)) {
                        i3 = R.drawable.permission_locked;
                    }
                    checkBox.setIcon(i3);
                    getConnectionsManager().sendRequest(tLRPC$TL_channels_toggleAntiSpam, new RequestDelegate() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda19
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                            ChatUsersActivity.this.lambda$createView$1(textCell, z4, tLObject2, tLRPC$TL_error);
                        }
                    });
                    return;
                }
            } else if (i == this.hideMembersRow) {
                final TextCell textCell2 = (TextCell) view;
                TLRPC$ChatFull tLRPC$ChatFull5 = this.info;
                if (tLRPC$ChatFull5 != null && !tLRPC$ChatFull5.participants_hidden && tLRPC$ChatFull5.participants_count < getMessagesController().hiddenMembersGroupSizeMin) {
                    BulletinFactory.of(this).createSimpleBulletin(R.raw.contacts_sync_off, AndroidUtilities.replaceTags(LocaleController.formatPluralString("ChannelHiddenMembersForbidden", getMessagesController().hiddenMembersGroupSizeMin, new Object[0]))).show();
                    return;
                } else if (this.info == null || !ChatObject.canUserDoAdminAction(this.currentChat, 2) || this.hideMembersToggleLoading) {
                    return;
                } else {
                    this.hideMembersToggleLoading = true;
                    final boolean z6 = this.info.participants_hidden;
                    TLRPC$TL_channels_toggleParticipantsHidden tLRPC$TL_channels_toggleParticipantsHidden = new TLRPC$TL_channels_toggleParticipantsHidden();
                    tLRPC$TL_channels_toggleParticipantsHidden.channel = getMessagesController().getInputChannel(this.chatId);
                    TLRPC$ChatFull tLRPC$ChatFull6 = this.info;
                    boolean z7 = true ^ tLRPC$ChatFull6.participants_hidden;
                    tLRPC$ChatFull6.participants_hidden = z7;
                    tLRPC$TL_channels_toggleParticipantsHidden.enabled = z7;
                    textCell2.setChecked(z7);
                    Switch checkBox2 = textCell2.getCheckBox();
                    if (!ChatObject.canUserDoAdminAction(this.currentChat, 2) || ((tLRPC$ChatFull = this.info) != null && !tLRPC$ChatFull.participants_hidden && tLRPC$ChatFull.participants_count < getMessagesController().hiddenMembersGroupSizeMin)) {
                        i3 = R.drawable.permission_locked;
                    }
                    checkBox2.setIcon(i3);
                    getConnectionsManager().sendRequest(tLRPC$TL_channels_toggleParticipantsHidden, new RequestDelegate() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda18
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                            ChatUsersActivity.this.lambda$createView$3(textCell2, z6, tLObject2, tLRPC$TL_error);
                        }
                    });
                    return;
                }
            } else if (i == this.removedUsersRow) {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("chat_id", this.chatId);
                bundle4.putInt("type", 0);
                ChatUsersActivity chatUsersActivity3 = new ChatUsersActivity(bundle4);
                chatUsersActivity3.setInfo(this.info);
                presentFragment(chatUsersActivity3);
                return;
            } else if (i == this.gigaConvertRow) {
                showDialog(new 10(getParentActivity(), this));
            } else if (i == this.addNew2Row) {
                if (this.info != null) {
                    ManageLinksActivity manageLinksActivity = new ManageLinksActivity(this.chatId, 0L, 0);
                    TLRPC$ChatFull tLRPC$ChatFull7 = this.info;
                    manageLinksActivity.setInfo(tLRPC$ChatFull7, tLRPC$ChatFull7.exported_invite);
                    presentFragment(manageLinksActivity);
                    return;
                }
                return;
            } else if (i > this.permissionsSectionRow && i <= Math.max(this.manageTopicsRow, this.changeInfoRow)) {
                TextCheckCell2 textCheckCell2 = (TextCheckCell2) view;
                if (textCheckCell2.isEnabled()) {
                    if (textCheckCell2.hasIcon()) {
                        if (ChatObject.isPublic(this.currentChat) && (i == this.pinMessagesRow || i == this.changeInfoRow)) {
                            BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("EditCantEditPermissionsPublic", R.string.EditCantEditPermissionsPublic)).show();
                            return;
                        } else {
                            BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("EditCantEditPermissions", R.string.EditCantEditPermissions)).show();
                            return;
                        }
                    } else if (i == this.sendMediaRow) {
                        DiffCallback saveState = saveState();
                        this.sendMediaExpanded = !this.sendMediaExpanded;
                        AndroidUtilities.updateVisibleRows(this.listView);
                        updateListAnimated(saveState);
                        return;
                    } else {
                        textCheckCell2.setChecked(!textCheckCell2.isChecked());
                        if (i == this.changeInfoRow) {
                            this.defaultBannedRights.change_info = !tLRPC$TL_chatBannedRights9.change_info;
                            return;
                        } else if (i == this.addUsersRow) {
                            this.defaultBannedRights.invite_users = !tLRPC$TL_chatBannedRights8.invite_users;
                            return;
                        } else if (i == this.manageTopicsRow) {
                            this.defaultBannedRights.manage_topics = !tLRPC$TL_chatBannedRights7.manage_topics;
                            return;
                        } else if (i == this.pinMessagesRow) {
                            this.defaultBannedRights.pin_messages = !tLRPC$TL_chatBannedRights6.pin_messages;
                            return;
                        } else if (i == this.sendMessagesRow) {
                            this.defaultBannedRights.send_plain = !tLRPC$TL_chatBannedRights5.send_plain;
                            int i5 = this.sendMediaEmbededLinksRow;
                            if (i5 >= 0) {
                                this.listViewAdapter.notifyItemChanged(i5);
                            }
                            int i6 = this.sendMediaRow;
                            if (i6 >= 0) {
                                this.listViewAdapter.notifyItemChanged(i6);
                                return;
                            }
                            return;
                        } else if (i == this.sendMediaRow) {
                            DiffCallback saveState2 = saveState();
                            this.sendMediaExpanded = !this.sendMediaExpanded;
                            AndroidUtilities.updateVisibleRows(this.listView);
                            updateListAnimated(saveState2);
                            return;
                        } else if (i == this.sendStickersRow) {
                            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights19 = this.defaultBannedRights;
                            boolean z8 = !tLRPC$TL_chatBannedRights19.send_stickers;
                            tLRPC$TL_chatBannedRights19.send_inline = z8;
                            tLRPC$TL_chatBannedRights19.send_gifs = z8;
                            tLRPC$TL_chatBannedRights19.send_games = z8;
                            tLRPC$TL_chatBannedRights19.send_stickers = z8;
                            return;
                        } else if (i == this.embedLinksRow) {
                            this.defaultBannedRights.embed_links = !tLRPC$TL_chatBannedRights4.embed_links;
                            return;
                        } else if (i == this.sendPollsRow) {
                            this.defaultBannedRights.send_polls = !tLRPC$TL_chatBannedRights3.send_polls;
                            return;
                        } else {
                            return;
                        }
                    }
                }
                return;
            }
        }
        if (z3) {
            TLObject item = this.listViewAdapter.getItem(i);
            if (item instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) item;
                peerId = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                tLRPC$TL_chatBannedRights = tLRPC$ChannelParticipant.banned_rights;
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights5 = tLRPC$ChannelParticipant.admin_rights;
                String str2 = tLRPC$ChannelParticipant.rank;
                boolean z9 = !((tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) || (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant.can_edit;
                if ((item instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$TL_chatAdminRights5 = ((TLRPC$TL_channelParticipantCreator) item).admin_rights) == null) {
                    tLRPC$TL_chatAdminRights5 = new TLRPC$TL_chatAdminRights();
                    tLRPC$TL_chatAdminRights5.add_admins = true;
                    tLRPC$TL_chatAdminRights5.pin_messages = true;
                    tLRPC$TL_chatAdminRights5.manage_topics = true;
                    tLRPC$TL_chatAdminRights5.invite_users = true;
                    tLRPC$TL_chatAdminRights5.ban_users = true;
                    tLRPC$TL_chatAdminRights5.delete_messages = true;
                    tLRPC$TL_chatAdminRights5.edit_messages = true;
                    tLRPC$TL_chatAdminRights5.post_messages = true;
                    tLRPC$TL_chatAdminRights5.change_info = true;
                    if (!this.isChannel) {
                        tLRPC$TL_chatAdminRights5.manage_call = true;
                    }
                }
                tLObject = item;
                tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights5;
                str = str2;
                z = z9;
                long j2 = peerId;
                tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatBannedRights;
                j = j2;
            } else if (item instanceof TLRPC$ChatParticipant) {
                j = ((TLRPC$ChatParticipant) item).user_id;
                boolean z10 = this.currentChat.creator;
                if (item instanceof TLRPC$TL_chatParticipantCreator) {
                    tLRPC$TL_chatAdminRights4 = new TLRPC$TL_chatAdminRights();
                    tLRPC$TL_chatAdminRights4.add_admins = true;
                    tLRPC$TL_chatAdminRights4.pin_messages = true;
                    tLRPC$TL_chatAdminRights4.manage_topics = true;
                    tLRPC$TL_chatAdminRights4.invite_users = true;
                    tLRPC$TL_chatAdminRights4.ban_users = true;
                    tLRPC$TL_chatAdminRights4.delete_messages = true;
                    tLRPC$TL_chatAdminRights4.edit_messages = true;
                    tLRPC$TL_chatAdminRights4.post_messages = true;
                    tLRPC$TL_chatAdminRights4.change_info = true;
                    if (!this.isChannel) {
                        tLRPC$TL_chatAdminRights4.manage_call = true;
                    }
                } else {
                    tLRPC$TL_chatAdminRights4 = null;
                }
                z = z10;
                tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights4;
                str = "";
                tLObject = item;
                tLRPC$TL_chatAdminRights3 = null;
            } else {
                tLObject = item;
                j = 0;
                tLRPC$TL_chatAdminRights = null;
                str = "";
                z = false;
                tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights;
                tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights2;
            }
        } else {
            TLObject item2 = this.searchListViewAdapter.getItem(i);
            if (item2 instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) item2;
                getMessagesController().putUser(tLRPC$User, false);
                long j3 = tLRPC$User.id;
                j = j3;
                item2 = getAnyParticipant(j3);
            } else if ((item2 instanceof TLRPC$ChannelParticipant) || (item2 instanceof TLRPC$ChatParticipant)) {
                j = 0;
            } else {
                j = 0;
                item2 = null;
            }
            if (item2 instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant2 = (TLRPC$ChannelParticipant) item2;
                peerId = MessageObject.getPeerId(tLRPC$ChannelParticipant2.peer);
                boolean z11 = !((tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) || (tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant2.can_edit;
                tLRPC$TL_chatBannedRights = tLRPC$ChannelParticipant2.banned_rights;
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights6 = tLRPC$ChannelParticipant2.admin_rights;
                str = tLRPC$ChannelParticipant2.rank;
                tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights6;
                z = z11;
                tLObject = item2;
                long j22 = peerId;
                tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatBannedRights;
                j = j22;
            } else {
                if (item2 instanceof TLRPC$ChatParticipant) {
                    j = ((TLRPC$ChatParticipant) item2).user_id;
                    tLObject = item2;
                    z = this.currentChat.creator;
                    tLRPC$TL_chatAdminRights2 = 0;
                    str = "";
                } else {
                    tLObject = item2;
                    tLRPC$TL_chatAdminRights = null;
                    str = "";
                    if (item2 == null) {
                        z = true;
                        tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights;
                    }
                    z = false;
                    tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights;
                }
                tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights2;
            }
        }
        if (j == 0) {
            return;
        }
        int i7 = this.selectType;
        if (i7 != 0) {
            if (i7 == 3 || i7 == 1) {
                if (i7 != 1 && z && ((tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin))) {
                    final TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.formatString("AdminWillBeRemoved", R.string.AdminWillBeRemoved, UserObject.getUserName(user)));
                    final TLObject tLObject2 = tLObject;
                    final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights7 = tLRPC$TL_chatAdminRights2;
                    final TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights20 = tLRPC$TL_chatAdminRights3;
                    final String str3 = str;
                    final boolean z12 = z;
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i8) {
                            ChatUsersActivity.this.lambda$createView$4(user, tLObject2, tLRPC$TL_chatAdminRights7, tLRPC$TL_chatBannedRights20, str3, z12, dialogInterface, i8);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder.create());
                    return;
                }
                openRightsEdit(j, tLObject, tLRPC$TL_chatAdminRights2, tLRPC$TL_chatAdminRights3, str, z, i7 == 1 ? 0 : 1, i7 == 1 || i7 == 3);
                return;
            }
            removeParticipant(j);
            return;
        }
        int i8 = this.type;
        if (i8 == 1) {
            canBlockUsers = j != getUserConfig().getClientUserId() && (this.currentChat.creator || z);
        } else if (i8 == 0 || i8 == 3) {
            canBlockUsers = ChatObject.canBlockUsers(this.currentChat);
        } else {
            z2 = false;
            i2 = this.type;
            if (i2 != 0 || ((i2 != 1 && this.isChannel) || (i2 == 2 && this.selectType == 0))) {
                if (j != getUserConfig().getClientUserId()) {
                    return;
                }
                Bundle bundle5 = new Bundle();
                if (j > 0) {
                    bundle5.putLong("user_id", j);
                } else {
                    bundle5.putLong("chat_id", -j);
                }
                presentFragment(new ProfileActivity(bundle5));
                return;
            }
            if (tLRPC$TL_chatAdminRights3 == null) {
                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights21 = new TLRPC$TL_chatBannedRights();
                tLRPC$TL_chatBannedRights21.view_messages = true;
                tLRPC$TL_chatBannedRights21.send_stickers = true;
                tLRPC$TL_chatBannedRights21.send_media = true;
                tLRPC$TL_chatBannedRights21.send_photos = true;
                tLRPC$TL_chatBannedRights21.send_videos = true;
                tLRPC$TL_chatBannedRights21.send_roundvideos = true;
                tLRPC$TL_chatBannedRights21.send_audios = true;
                tLRPC$TL_chatBannedRights21.send_voices = true;
                tLRPC$TL_chatBannedRights21.send_docs = true;
                tLRPC$TL_chatBannedRights21.embed_links = true;
                tLRPC$TL_chatBannedRights21.send_plain = true;
                tLRPC$TL_chatBannedRights21.send_messages = true;
                tLRPC$TL_chatBannedRights21.send_games = true;
                tLRPC$TL_chatBannedRights21.send_inline = true;
                tLRPC$TL_chatBannedRights21.send_gifs = true;
                tLRPC$TL_chatBannedRights21.pin_messages = true;
                tLRPC$TL_chatBannedRights21.send_polls = true;
                tLRPC$TL_chatBannedRights21.invite_users = true;
                tLRPC$TL_chatBannedRights21.manage_topics = true;
                tLRPC$TL_chatBannedRights21.change_info = true;
                tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights21;
            } else {
                tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatAdminRights3;
            }
            ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights2, this.defaultBannedRights, tLRPC$TL_chatBannedRights2, str, this.type == 1 ? 0 : 1, z2, tLObject == null, null);
            chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.11
                @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                public void didSetRights(int i9, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights8, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights22, String str4) {
                    TLObject tLObject3 = tLObject;
                    if (tLObject3 instanceof TLRPC$ChannelParticipant) {
                        TLRPC$ChannelParticipant tLRPC$ChannelParticipant3 = (TLRPC$ChannelParticipant) tLObject3;
                        tLRPC$ChannelParticipant3.admin_rights = tLRPC$TL_chatAdminRights8;
                        tLRPC$ChannelParticipant3.banned_rights = tLRPC$TL_chatBannedRights22;
                        tLRPC$ChannelParticipant3.rank = str4;
                        ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant3, tLRPC$TL_chatAdminRights8, tLRPC$TL_chatBannedRights22, 0L, false);
                    }
                }

                @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                public void didChangeOwner(TLRPC$User tLRPC$User2) {
                    ChatUsersActivity.this.onOwnerChaged(tLRPC$User2);
                }
            });
            presentFragment(chatRightsEditActivity);
            return;
        }
        z2 = canBlockUsers;
        i2 = this.type;
        if (i2 != 0) {
        }
        if (j != getUserConfig().getClientUserId()) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 8 implements ChatUsersActivityDelegate {
        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public /* synthetic */ void didKickParticipant(long j) {
            ChatUsersActivityDelegate.-CC.$default$didKickParticipant(this, j);
        }

        8() {
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public void didAddParticipantToList(long j, TLObject tLObject) {
            if (tLObject == null || ChatUsersActivity.this.participantsMap.get(j) != null) {
                return;
            }
            DiffCallback saveState = ChatUsersActivity.this.saveState();
            ChatUsersActivity.this.participants.add(tLObject);
            ChatUsersActivity.this.participantsMap.put(j, tLObject);
            ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
            chatUsersActivity.sortAdmins(chatUsersActivity.participants);
            ChatUsersActivity.this.updateListAnimated(saveState);
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public void didSelectUser(long j) {
            final TLRPC$User user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j));
            if (user != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatUsersActivity.8.this.lambda$didSelectUser$0(user);
                    }
                }, 200L);
            }
            if (ChatUsersActivity.this.participantsMap.get(j) == null) {
                DiffCallback saveState = ChatUsersActivity.this.saveState();
                TLRPC$TL_channelParticipantAdmin tLRPC$TL_channelParticipantAdmin = new TLRPC$TL_channelParticipantAdmin();
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_channelParticipantAdmin.peer = tLRPC$TL_peerUser;
                tLRPC$TL_peerUser.user_id = user.id;
                tLRPC$TL_channelParticipantAdmin.date = ChatUsersActivity.this.getConnectionsManager().getCurrentTime();
                tLRPC$TL_channelParticipantAdmin.promoted_by = ChatUsersActivity.this.getAccountInstance().getUserConfig().clientUserId;
                ChatUsersActivity.this.participants.add(tLRPC$TL_channelParticipantAdmin);
                ChatUsersActivity.this.participantsMap.put(user.id, tLRPC$TL_channelParticipantAdmin);
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                chatUsersActivity.sortAdmins(chatUsersActivity.participants);
                ChatUsersActivity.this.updateListAnimated(saveState);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectUser$0(TLRPC$User tLRPC$User) {
            if (BulletinFactory.canShowBulletin(ChatUsersActivity.this)) {
                BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, tLRPC$User.first_name).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 9 implements GroupCreateActivity.ContactsAddActivityDelegate {
        final /* synthetic */ GroupCreateActivity val$fragment;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$didSelectUsers$1(TLRPC$User tLRPC$User) {
        }

        9(GroupCreateActivity groupCreateActivity) {
            this.val$fragment = groupCreateActivity;
        }

        @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
        public void didSelectUsers(ArrayList<TLRPC$User> arrayList, int i) {
            if (this.val$fragment.getParentActivity() == null) {
                return;
            }
            ChatUsersActivity.this.getMessagesController().addUsersToChat(ChatUsersActivity.this.currentChat, ChatUsersActivity.this, arrayList, i, new Consumer() { // from class: org.telegram.ui.ChatUsersActivity$9$$ExternalSyntheticLambda0
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    ChatUsersActivity.9.this.lambda$didSelectUsers$0((TLRPC$User) obj);
                }
            }, ChatUsersActivity$9$$ExternalSyntheticLambda1.INSTANCE, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectUsers$0(TLRPC$User tLRPC$User) {
            DiffCallback saveState = ChatUsersActivity.this.saveState();
            ArrayList arrayList = (ChatUsersActivity.this.contactsMap == null || ChatUsersActivity.this.contactsMap.size() == 0) ? ChatUsersActivity.this.participants : ChatUsersActivity.this.contacts;
            LongSparseArray longSparseArray = (ChatUsersActivity.this.contactsMap == null || ChatUsersActivity.this.contactsMap.size() == 0) ? ChatUsersActivity.this.participantsMap : ChatUsersActivity.this.contactsMap;
            if (longSparseArray.get(tLRPC$User.id) == null) {
                if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                    TLRPC$TL_channelParticipant tLRPC$TL_channelParticipant = new TLRPC$TL_channelParticipant();
                    tLRPC$TL_channelParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_channelParticipant.peer = tLRPC$TL_peerUser;
                    tLRPC$TL_peerUser.user_id = tLRPC$User.id;
                    tLRPC$TL_channelParticipant.date = ChatUsersActivity.this.getConnectionsManager().getCurrentTime();
                    arrayList.add(0, tLRPC$TL_channelParticipant);
                    longSparseArray.put(tLRPC$User.id, tLRPC$TL_channelParticipant);
                } else {
                    TLRPC$TL_chatParticipant tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipant();
                    tLRPC$TL_chatParticipant.user_id = tLRPC$User.id;
                    tLRPC$TL_chatParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                    arrayList.add(0, tLRPC$TL_chatParticipant);
                    longSparseArray.put(tLRPC$User.id, tLRPC$TL_chatParticipant);
                }
            }
            if (arrayList == ChatUsersActivity.this.participants) {
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                chatUsersActivity.sortAdmins(chatUsersActivity.participants);
            }
            ChatUsersActivity.this.updateListAnimated(saveState);
        }

        @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
        public void needAddBot(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.openRightsEdit(tLRPC$User.id, null, null, null, "", true, 0, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(final TextCell textCell, final boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            getMessagesController().processUpdates((TLRPC$Updates) tLObject, false);
            getMessagesController().putChatFull(this.info);
        }
        if (tLRPC$TL_error != null && !"CHAT_NOT_MODIFIED".equals(tLRPC$TL_error.text)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.this.lambda$createView$0(textCell, z);
                }
            });
        }
        this.antiSpamToggleLoading = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(TextCell textCell, boolean z) {
        TLRPC$ChatFull tLRPC$ChatFull;
        if (getParentActivity() == null) {
            return;
        }
        this.info.antispam = z;
        textCell.setChecked(z);
        textCell.getCheckBox().setIcon((!ChatObject.canUserDoAdminAction(this.currentChat, 13) || ((tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.antispam && tLRPC$ChatFull.participants_count < getMessagesController().telegramAntispamGroupSizeMin)) ? R.drawable.permission_locked : 0);
        BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString("UnknownError", R.string.UnknownError)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(final TextCell textCell, final boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            getMessagesController().processUpdates((TLRPC$Updates) tLObject, false);
            getMessagesController().putChatFull(this.info);
        }
        if (tLRPC$TL_error != null && !"CHAT_NOT_MODIFIED".equals(tLRPC$TL_error.text)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.this.lambda$createView$2(textCell, z);
                }
            });
        }
        this.hideMembersToggleLoading = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(TextCell textCell, boolean z) {
        TLRPC$ChatFull tLRPC$ChatFull;
        if (getParentActivity() == null) {
            return;
        }
        this.info.participants_hidden = z;
        textCell.setChecked(z);
        textCell.getCheckBox().setIcon((!ChatObject.canUserDoAdminAction(this.currentChat, 2) || ((tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.participants_hidden && tLRPC$ChatFull.participants_count < getMessagesController().hiddenMembersGroupSizeMin)) ? R.drawable.permission_locked : 0);
        BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString("UnknownError", R.string.UnknownError)).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 10 extends GigagroupConvertAlert {
        @Override // org.telegram.ui.Components.GigagroupConvertAlert
        protected void onCancel() {
        }

        10(Context context, BaseFragment baseFragment) {
            super(context, baseFragment);
        }

        @Override // org.telegram.ui.Components.GigagroupConvertAlert
        protected void onCovert() {
            ChatUsersActivity.this.getMessagesController().convertToGigaGroup(ChatUsersActivity.this.getParentActivity(), ChatUsersActivity.this.currentChat, ChatUsersActivity.this, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.ChatUsersActivity$10$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
                public final void run(boolean z) {
                    ChatUsersActivity.10.this.lambda$onCovert$0(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCovert$0(boolean z) {
            if (!z || ((BaseFragment) ChatUsersActivity.this).parentLayout == null) {
                return;
            }
            BaseFragment baseFragment = ((BaseFragment) ChatUsersActivity.this).parentLayout.getFragmentStack().get(((BaseFragment) ChatUsersActivity.this).parentLayout.getFragmentStack().size() - 2);
            if (baseFragment instanceof ChatEditActivity) {
                baseFragment.removeSelfFromStack();
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", ChatUsersActivity.this.chatId);
                ChatEditActivity chatEditActivity = new ChatEditActivity(bundle);
                chatEditActivity.setInfo(ChatUsersActivity.this.info);
                ((BaseFragment) ChatUsersActivity.this).parentLayout.addFragmentToStack(chatEditActivity, ((BaseFragment) ChatUsersActivity.this).parentLayout.getFragmentStack().size() - 1);
                ChatUsersActivity.this.finishFragment();
                chatEditActivity.showConvertTooltip();
                return;
            }
            ChatUsersActivity.this.finishFragment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(TLRPC$User tLRPC$User, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, DialogInterface dialogInterface, int i) {
        openRightsEdit(tLRPC$User.id, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, this.selectType == 1 ? 0 : 1, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$6(View view, int i) {
        if (getParentActivity() != null) {
            RecyclerView.Adapter adapter = this.listView.getAdapter();
            ListAdapter listAdapter = this.listViewAdapter;
            return adapter == listAdapter && createMenuForParticipant(listAdapter.getItem(i), false);
        }
        return false;
    }

    private void setBannedRights(TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights) {
        if (tLRPC$TL_chatBannedRights != null) {
            this.defaultBannedRights = tLRPC$TL_chatBannedRights;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sortAdmins(ArrayList<TLObject> arrayList) {
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda13
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortAdmins$7;
                lambda$sortAdmins$7 = ChatUsersActivity.this.lambda$sortAdmins$7((TLObject) obj, (TLObject) obj2);
                return lambda$sortAdmins$7;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$sortAdmins$7(TLObject tLObject, TLObject tLObject2) {
        int channelAdminParticipantType = getChannelAdminParticipantType(tLObject);
        int channelAdminParticipantType2 = getChannelAdminParticipantType(tLObject2);
        if (channelAdminParticipantType > channelAdminParticipantType2) {
            return 1;
        }
        if (channelAdminParticipantType < channelAdminParticipantType2) {
            return -1;
        }
        if ((tLObject instanceof TLRPC$ChannelParticipant) && (tLObject2 instanceof TLRPC$ChannelParticipant)) {
            return (int) (MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer) - MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject2).peer));
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showItemsAnimated(final int i) {
        if (this.isPaused || !this.openTransitionStarted) {
            return;
        }
        if (this.listView.getAdapter() == this.listViewAdapter && this.firstLoaded) {
            return;
        }
        final View view = null;
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof FlickerLoadingView) {
                view = childAt;
            }
        }
        if (view != null) {
            this.listView.removeView(view);
            i--;
        }
        this.listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.ChatUsersActivity.13
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                ChatUsersActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                int childCount = ChatUsersActivity.this.listView.getChildCount();
                AnimatorSet animatorSet = new AnimatorSet();
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt2 = ChatUsersActivity.this.listView.getChildAt(i3);
                    if (childAt2 != view && ChatUsersActivity.this.listView.getChildAdapterPosition(childAt2) >= i) {
                        childAt2.setAlpha(0.0f);
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt2, View.ALPHA, 0.0f, 1.0f);
                        ofFloat.setStartDelay((int) ((Math.min(ChatUsersActivity.this.listView.getMeasuredHeight(), Math.max(0, childAt2.getTop())) / ChatUsersActivity.this.listView.getMeasuredHeight()) * 100.0f));
                        ofFloat.setDuration(200L);
                        animatorSet.playTogether(ofFloat);
                    }
                }
                View view2 = view;
                if (view2 != null && view2.getParent() == null) {
                    ChatUsersActivity.this.listView.addView(view);
                    final RecyclerView.LayoutManager layoutManager = ChatUsersActivity.this.listView.getLayoutManager();
                    if (layoutManager != null) {
                        layoutManager.ignoreView(view);
                        View view3 = view;
                        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view3, View.ALPHA, view3.getAlpha(), 0.0f);
                        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ChatUsersActivity.13.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                view.setAlpha(1.0f);
                                layoutManager.stopIgnoringView(view);
                                ChatUsersActivity.this.listView.removeView(view);
                            }
                        });
                        ofFloat2.start();
                    }
                }
                animatorSet.start();
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOwnerChaged(TLRPC$User tLRPC$User) {
        LongSparseArray<TLObject> longSparseArray;
        ArrayList<TLObject> arrayList;
        boolean z;
        this.undoView.showWithAction(-this.chatId, this.isChannel ? 9 : 10, tLRPC$User);
        this.currentChat.creator = false;
        boolean z2 = false;
        for (int i = 0; i < 3; i++) {
            boolean z3 = true;
            if (i == 0) {
                longSparseArray = this.contactsMap;
                arrayList = this.contacts;
            } else if (i == 1) {
                longSparseArray = this.botsMap;
                arrayList = this.bots;
            } else {
                longSparseArray = this.participantsMap;
                arrayList = this.participants;
            }
            TLObject tLObject = longSparseArray.get(tLRPC$User.id);
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$TL_channelParticipantCreator tLRPC$TL_channelParticipantCreator = new TLRPC$TL_channelParticipantCreator();
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_channelParticipantCreator.peer = tLRPC$TL_peerUser;
                long j = tLRPC$User.id;
                tLRPC$TL_peerUser.user_id = j;
                longSparseArray.put(j, tLRPC$TL_channelParticipantCreator);
                int indexOf = arrayList.indexOf(tLObject);
                if (indexOf >= 0) {
                    arrayList.set(indexOf, tLRPC$TL_channelParticipantCreator);
                }
                z2 = true;
                z = true;
            } else {
                z = false;
            }
            long clientUserId = getUserConfig().getClientUserId();
            TLObject tLObject2 = longSparseArray.get(clientUserId);
            if (tLObject2 instanceof TLRPC$ChannelParticipant) {
                TLRPC$TL_channelParticipantAdmin tLRPC$TL_channelParticipantAdmin = new TLRPC$TL_channelParticipantAdmin();
                TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                tLRPC$TL_channelParticipantAdmin.peer = tLRPC$TL_peerUser2;
                tLRPC$TL_peerUser2.user_id = clientUserId;
                tLRPC$TL_channelParticipantAdmin.self = true;
                tLRPC$TL_channelParticipantAdmin.inviter_id = clientUserId;
                tLRPC$TL_channelParticipantAdmin.promoted_by = clientUserId;
                tLRPC$TL_channelParticipantAdmin.date = (int) (System.currentTimeMillis() / 1000);
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = new TLRPC$TL_chatAdminRights();
                tLRPC$TL_channelParticipantAdmin.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$TL_chatAdminRights.add_admins = true;
                tLRPC$TL_chatAdminRights.pin_messages = true;
                tLRPC$TL_chatAdminRights.manage_topics = true;
                tLRPC$TL_chatAdminRights.invite_users = true;
                tLRPC$TL_chatAdminRights.ban_users = true;
                tLRPC$TL_chatAdminRights.delete_messages = true;
                tLRPC$TL_chatAdminRights.edit_messages = true;
                tLRPC$TL_chatAdminRights.post_messages = true;
                tLRPC$TL_chatAdminRights.change_info = true;
                if (!this.isChannel) {
                    tLRPC$TL_chatAdminRights.manage_call = true;
                }
                longSparseArray.put(clientUserId, tLRPC$TL_channelParticipantAdmin);
                int indexOf2 = arrayList.indexOf(tLObject2);
                if (indexOf2 >= 0) {
                    arrayList.set(indexOf2, tLRPC$TL_channelParticipantAdmin);
                }
            } else {
                z3 = z;
            }
            if (z3) {
                Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda12
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$onOwnerChaged$8;
                        lambda$onOwnerChaged$8 = ChatUsersActivity.this.lambda$onOwnerChaged$8((TLObject) obj, (TLObject) obj2);
                        return lambda$onOwnerChaged$8;
                    }
                });
            }
        }
        if (!z2) {
            TLRPC$TL_channelParticipantCreator tLRPC$TL_channelParticipantCreator2 = new TLRPC$TL_channelParticipantCreator();
            TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
            tLRPC$TL_channelParticipantCreator2.peer = tLRPC$TL_peerUser3;
            long j2 = tLRPC$User.id;
            tLRPC$TL_peerUser3.user_id = j2;
            this.participantsMap.put(j2, tLRPC$TL_channelParticipantCreator2);
            this.participants.add(tLRPC$TL_channelParticipantCreator2);
            sortAdmins(this.participants);
            updateRows();
        }
        this.listViewAdapter.notifyDataSetChanged();
        ChatUsersActivityDelegate chatUsersActivityDelegate = this.delegate;
        if (chatUsersActivityDelegate != null) {
            chatUsersActivityDelegate.didChangeOwner(tLRPC$User);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$onOwnerChaged$8(TLObject tLObject, TLObject tLObject2) {
        int channelAdminParticipantType = getChannelAdminParticipantType(tLObject);
        int channelAdminParticipantType2 = getChannelAdminParticipantType(tLObject2);
        if (channelAdminParticipantType > channelAdminParticipantType2) {
            return 1;
        }
        return channelAdminParticipantType < channelAdminParticipantType2 ? -1 : 0;
    }

    private void openRightsEdit2(final long j, final int i, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, final int i2, boolean z2) {
        final boolean[] zArr = new boolean[1];
        boolean z3 = (tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin);
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, i2, true, false, null) { // from class: org.telegram.ui.ChatUsersActivity.14
            @Override // org.telegram.ui.ActionBar.BaseFragment
            public void onTransitionAnimationEnd(boolean z4, boolean z5) {
                if (!z4 && z5 && zArr[0] && BulletinFactory.canShowBulletin(ChatUsersActivity.this)) {
                    if (j > 0) {
                        TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
                        if (user != null) {
                            BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, user.first_name).show();
                            return;
                        }
                        return;
                    }
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
                    if (chat != null) {
                        BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, chat.title).show();
                    }
                }
            }
        };
        final boolean z4 = z3;
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.15
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i3, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str2) {
                TLRPC$ChatParticipant tLRPC$TL_chatParticipant;
                TLRPC$ChannelParticipant tLRPC$TL_channelParticipant;
                int i4 = i2;
                if (i4 != 0) {
                    if (i4 == 1 && i3 == 0) {
                        ChatUsersActivity.this.removeParticipants(j);
                        return;
                    }
                    return;
                }
                int i5 = 0;
                while (true) {
                    if (i5 >= ChatUsersActivity.this.participants.size()) {
                        break;
                    }
                    TLObject tLObject2 = (TLObject) ChatUsersActivity.this.participants.get(i5);
                    if (tLObject2 instanceof TLRPC$ChannelParticipant) {
                        if (MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject2).peer) == j) {
                            if (i3 == 1) {
                                tLRPC$TL_channelParticipant = new TLRPC$TL_channelParticipantAdmin();
                            } else {
                                tLRPC$TL_channelParticipant = new TLRPC$TL_channelParticipant();
                            }
                            tLRPC$TL_channelParticipant.admin_rights = tLRPC$TL_chatAdminRights2;
                            tLRPC$TL_channelParticipant.banned_rights = tLRPC$TL_chatBannedRights2;
                            tLRPC$TL_channelParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                            if (j > 0) {
                                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                tLRPC$TL_channelParticipant.peer = tLRPC$TL_peerUser;
                                tLRPC$TL_peerUser.user_id = j;
                            } else {
                                TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                tLRPC$TL_channelParticipant.peer = tLRPC$TL_peerChannel;
                                tLRPC$TL_peerChannel.channel_id = -j;
                            }
                            tLRPC$TL_channelParticipant.date = i;
                            tLRPC$TL_channelParticipant.flags |= 4;
                            tLRPC$TL_channelParticipant.rank = str2;
                            ChatUsersActivity.this.participants.set(i5, tLRPC$TL_channelParticipant);
                        }
                    } else if (tLObject2 instanceof TLRPC$ChatParticipant) {
                        TLRPC$ChatParticipant tLRPC$ChatParticipant = (TLRPC$ChatParticipant) tLObject2;
                        if (i3 == 1) {
                            tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipantAdmin();
                        } else {
                            tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipant();
                        }
                        tLRPC$TL_chatParticipant.user_id = tLRPC$ChatParticipant.user_id;
                        tLRPC$TL_chatParticipant.date = tLRPC$ChatParticipant.date;
                        tLRPC$TL_chatParticipant.inviter_id = tLRPC$ChatParticipant.inviter_id;
                        int indexOf = ChatUsersActivity.this.info.participants.participants.indexOf(tLRPC$ChatParticipant);
                        if (indexOf >= 0) {
                            ChatUsersActivity.this.info.participants.participants.set(indexOf, tLRPC$TL_chatParticipant);
                        }
                        ChatUsersActivity.this.loadChatParticipants(0, 200);
                    }
                    i5++;
                }
                if (i3 != 1 || z4) {
                    return;
                }
                zArr[0] = true;
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User) {
                ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
            }
        });
        presentFragment(chatRightsEditActivity);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        return checkDiscard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRightsEdit(final long j, final TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, int i, final boolean z2) {
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, i, z, tLObject == null, null);
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.16
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str2) {
                TLObject tLObject2 = tLObject;
                if (tLObject2 instanceof TLRPC$ChannelParticipant) {
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject2;
                    tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights2;
                    tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights2;
                    tLRPC$ChannelParticipant.rank = str2;
                }
                if (ChatUsersActivity.this.delegate == null || i2 != 1) {
                    if (ChatUsersActivity.this.delegate != null) {
                        ChatUsersActivity.this.delegate.didAddParticipantToList(j, tLObject);
                    }
                } else {
                    ChatUsersActivity.this.delegate.didSelectUser(j);
                }
                if (z2) {
                    ChatUsersActivity.this.removeSelfFromStack();
                }
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User) {
                ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
            }
        });
        presentFragment(chatRightsEditActivity, z2);
    }

    private void removeParticipant(long j) {
        if (ChatObject.isChannel(this.currentChat)) {
            getMessagesController().deleteParticipantFromChat(this.chatId, getMessagesController().getUser(Long.valueOf(j)));
            ChatUsersActivityDelegate chatUsersActivityDelegate = this.delegate;
            if (chatUsersActivityDelegate != null) {
                chatUsersActivityDelegate.didKickParticipant(j);
            }
            finishFragment();
        }
    }

    private TLObject getAnyParticipant(long j) {
        LongSparseArray<TLObject> longSparseArray;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                longSparseArray = this.contactsMap;
            } else if (i == 1) {
                longSparseArray = this.botsMap;
            } else {
                longSparseArray = this.participantsMap;
            }
            TLObject tLObject = longSparseArray.get(j);
            if (tLObject != null) {
                return tLObject;
            }
        }
        return null;
    }

    private void removeParticipants(TLObject tLObject) {
        if (tLObject instanceof TLRPC$ChatParticipant) {
            removeParticipants(((TLRPC$ChatParticipant) tLObject).user_id);
        } else if (tLObject instanceof TLRPC$ChannelParticipant) {
            removeParticipants(MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeParticipants(long j) {
        LongSparseArray<TLObject> longSparseArray;
        ArrayList<TLObject> arrayList;
        TLRPC$ChatFull tLRPC$ChatFull;
        DiffCallback saveState = saveState();
        boolean z = false;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                longSparseArray = this.contactsMap;
                arrayList = this.contacts;
            } else if (i == 1) {
                longSparseArray = this.botsMap;
                arrayList = this.bots;
            } else {
                longSparseArray = this.participantsMap;
                arrayList = this.participants;
            }
            TLObject tLObject = longSparseArray.get(j);
            if (tLObject != null) {
                longSparseArray.remove(j);
                arrayList.remove(tLObject);
                if (this.type == 0 && (tLRPC$ChatFull = this.info) != null) {
                    tLRPC$ChatFull.kicked_count--;
                }
                z = true;
            }
        }
        if (z) {
            updateListAnimated(saveState);
        }
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        SearchAdapter searchAdapter = this.searchListViewAdapter;
        if (adapter == searchAdapter) {
            searchAdapter.removeUserId(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateParticipantWithRights(TLRPC$ChannelParticipant tLRPC$ChannelParticipant, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, long j, boolean z) {
        LongSparseArray<TLObject> longSparseArray;
        ChatUsersActivityDelegate chatUsersActivityDelegate;
        boolean z2 = false;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                longSparseArray = this.contactsMap;
            } else if (i == 1) {
                longSparseArray = this.botsMap;
            } else {
                longSparseArray = this.participantsMap;
            }
            TLObject tLObject = longSparseArray.get(MessageObject.getPeerId(tLRPC$ChannelParticipant.peer));
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                if (z) {
                    tLRPC$ChannelParticipant.promoted_by = getUserConfig().getClientUserId();
                }
            }
            if (z && tLObject != null && !z2 && (chatUsersActivityDelegate = this.delegate) != null) {
                chatUsersActivityDelegate.didAddParticipantToList(j, tLObject);
                z2 = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:136:0x02ad A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02ae  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean createMenuForParticipant(final TLObject tLObject, boolean z) {
        long j;
        boolean z2;
        int i;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final String str;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        boolean z3;
        CharSequence[] charSequenceArr;
        int[] iArr;
        int i2;
        CharSequence[] charSequenceArr2;
        int[] iArr2;
        CharSequence[] charSequenceArr3;
        String str2;
        int[] iArr3;
        int i3;
        String str3;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        boolean z4;
        if (tLObject == null || this.selectType != 0) {
            return false;
        }
        if (tLObject instanceof TLRPC$ChannelParticipant) {
            TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
            j = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
            z2 = tLRPC$ChannelParticipant.can_edit;
            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = tLRPC$ChannelParticipant.banned_rights;
            tLRPC$TL_chatAdminRights = tLRPC$ChannelParticipant.admin_rights;
            tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights2;
            i = tLRPC$ChannelParticipant.date;
            str = tLRPC$ChannelParticipant.rank;
        } else if (tLObject instanceof TLRPC$ChatParticipant) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant = (TLRPC$ChatParticipant) tLObject;
            j = tLRPC$ChatParticipant.user_id;
            int i4 = tLRPC$ChatParticipant.date;
            z2 = ChatObject.canAddAdmins(this.currentChat);
            str = "";
            tLRPC$TL_chatAdminRights = null;
            tLRPC$TL_chatBannedRights = null;
            i = i4;
        } else {
            j = 0;
            z2 = false;
            i = 0;
            tLRPC$TL_chatAdminRights = null;
            str = null;
            tLRPC$TL_chatBannedRights = null;
        }
        if (j == 0 || j == getUserConfig().getClientUserId()) {
            return false;
        }
        int i5 = this.type;
        if (i5 == 2) {
            final TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
            boolean z5 = ChatObject.canAddAdmins(this.currentChat) && ((tLObject instanceof TLRPC$TL_channelParticipant) || (tLObject instanceof TLRPC$TL_channelParticipantBanned) || (tLObject instanceof TLRPC$TL_chatParticipant) || z2);
            boolean z6 = tLObject instanceof TLRPC$TL_channelParticipantAdmin;
            boolean z7 = !(z6 || (tLObject instanceof TLRPC$TL_channelParticipantCreator) || (tLObject instanceof TLRPC$TL_chatParticipantCreator) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin)) || z2;
            boolean z8 = z6 || (tLObject instanceof TLRPC$TL_chatParticipantAdmin);
            if (this.selectType == 0) {
                z5 &= !UserObject.isDeleted(user);
            }
            if (z) {
                arrayList = null;
                arrayList2 = null;
                arrayList3 = null;
            } else {
                arrayList2 = new ArrayList();
                arrayList = new ArrayList();
                arrayList3 = new ArrayList();
            }
            if (!z5) {
                arrayList4 = arrayList2;
                arrayList5 = arrayList3;
            } else if (z) {
                return true;
            } else {
                arrayList4 = arrayList2;
                arrayList4.add(z8 ? LocaleController.getString("EditAdminRights", R.string.EditAdminRights) : LocaleController.getString("SetAsAdmin", R.string.SetAsAdmin));
                arrayList5 = arrayList3;
                arrayList5.add(Integer.valueOf(R.drawable.msg_admins));
                arrayList.add(0);
            }
            if (!ChatObject.canBlockUsers(this.currentChat) || !z7) {
                z4 = false;
            } else if (z) {
                return true;
            } else {
                if (!this.isChannel) {
                    if (ChatObject.isChannel(this.currentChat) && !this.currentChat.gigagroup) {
                        arrayList4.add(LocaleController.getString("ChangePermissions", R.string.ChangePermissions));
                        arrayList5.add(Integer.valueOf(R.drawable.msg_permissions));
                        arrayList.add(1);
                    }
                    arrayList4.add(LocaleController.getString("KickFromGroup", R.string.KickFromGroup));
                } else {
                    arrayList4.add(LocaleController.getString("ChannelRemoveUser", R.string.ChannelRemoveUser));
                }
                arrayList5.add(Integer.valueOf(R.drawable.msg_remove));
                arrayList.add(2);
                z4 = true;
            }
            if (arrayList == null || arrayList.isEmpty()) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            int[] intArray = AndroidUtilities.toIntArray(arrayList5);
            ArrayList arrayList6 = arrayList4;
            final ArrayList arrayList7 = arrayList;
            final long j2 = j;
            final boolean z9 = z7;
            final int i6 = i;
            final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights;
            final TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_chatBannedRights;
            builder.setItems((CharSequence[]) arrayList4.toArray(new CharSequence[arrayList.size()]), intArray, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    ChatUsersActivity.this.lambda$createMenuForParticipant$10(arrayList7, user, j2, z9, tLObject, i6, tLRPC$TL_chatAdminRights2, tLRPC$TL_chatBannedRights3, str, dialogInterface, i7);
                }
            });
            AlertDialog create = builder.create();
            showDialog(create);
            if (z4) {
                create.setItemColor(arrayList6.size() - 1, Theme.getColor("text_RedBold"), Theme.getColor("text_RedRegular"));
            }
            return true;
        }
        if (i5 == 3 && ChatObject.canBlockUsers(this.currentChat)) {
            if (z) {
                return true;
            }
            charSequenceArr3 = new CharSequence[]{LocaleController.getString("ChannelEditPermissions", R.string.ChannelEditPermissions), LocaleController.getString("ChannelDeleteFromList", R.string.ChannelDeleteFromList)};
            iArr3 = new int[]{R.drawable.msg_permissions, R.drawable.msg_delete};
        } else if (this.type != 0 || !ChatObject.canBlockUsers(this.currentChat)) {
            if (this.type != 1 || !ChatObject.canAddAdmins(this.currentChat) || !z2) {
                z3 = false;
                charSequenceArr = null;
                iArr = null;
            } else if (z) {
                return true;
            } else {
                if (this.currentChat.creator) {
                    z3 = false;
                    i2 = 2;
                } else if ((tLObject instanceof TLRPC$TL_channelParticipantCreator) || !z2) {
                    z3 = false;
                    charSequenceArr2 = new CharSequence[]{LocaleController.getString("ChannelRemoveUserAdmin", R.string.ChannelRemoveUserAdmin)};
                    iArr2 = new int[]{R.drawable.msg_remove};
                    iArr = iArr2;
                    charSequenceArr = charSequenceArr2;
                } else {
                    i2 = 2;
                    z3 = false;
                }
                charSequenceArr2 = new CharSequence[i2];
                charSequenceArr2[z3 ? 1 : 0] = LocaleController.getString("EditAdminRights", R.string.EditAdminRights);
                charSequenceArr2[1] = LocaleController.getString("ChannelRemoveUserAdmin", R.string.ChannelRemoveUserAdmin);
                iArr2 = new int[i2];
                iArr2[z3 ? 1 : 0] = R.drawable.msg_admins;
                iArr2[1] = R.drawable.msg_remove;
                iArr = iArr2;
                charSequenceArr = charSequenceArr2;
            }
            if (charSequenceArr != null) {
                return z3;
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            final CharSequence[] charSequenceArr4 = charSequenceArr;
            final long j3 = j;
            final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights;
            final String str4 = str;
            final TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_chatBannedRights;
            builder2.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    ChatUsersActivity.this.lambda$createMenuForParticipant$13(charSequenceArr4, j3, tLRPC$TL_chatAdminRights3, str4, tLObject, tLRPC$TL_chatBannedRights4, dialogInterface, i7);
                }
            });
            AlertDialog create2 = builder2.create();
            showDialog(create2);
            if (this.type == 1) {
                create2.setItemColor(charSequenceArr.length - 1, Theme.getColor("text_RedBold"), Theme.getColor("text_RedRegular"));
                return true;
            }
            return true;
        } else if (z) {
            return true;
        } else {
            charSequenceArr3 = new CharSequence[2];
            if (!ChatObject.canAddUsers(this.currentChat) || j <= 0) {
                str2 = null;
            } else {
                if (this.isChannel) {
                    i3 = R.string.ChannelAddToChannel;
                    str3 = "ChannelAddToChannel";
                } else {
                    i3 = R.string.ChannelAddToGroup;
                    str3 = "ChannelAddToGroup";
                }
                str2 = LocaleController.getString(str3, i3);
            }
            charSequenceArr3[0] = str2;
            charSequenceArr3[1] = LocaleController.getString("ChannelDeleteFromList", R.string.ChannelDeleteFromList);
            iArr3 = new int[]{R.drawable.msg_contact_add, R.drawable.msg_delete};
        }
        charSequenceArr = charSequenceArr3;
        iArr = iArr3;
        z3 = false;
        if (charSequenceArr != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenuForParticipant$10(final ArrayList arrayList, TLRPC$User tLRPC$User, final long j, final boolean z, final TLObject tLObject, final int i, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, final String str, DialogInterface dialogInterface, final int i2) {
        if (((Integer) arrayList.get(i2)).intValue() == 2) {
            getMessagesController().deleteParticipantFromChat(this.chatId, tLRPC$User);
            removeParticipants(j);
            if (this.currentChat == null || tLRPC$User == null || !BulletinFactory.canShowBulletin(this)) {
                return;
            }
            BulletinFactory.createRemoveFromChatBulletin(this, tLRPC$User, this.currentChat.title).show();
        } else if (((Integer) arrayList.get(i2)).intValue() == 1 && z && ((tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.formatString("AdminWillBeRemoved", R.string.AdminWillBeRemoved, UserObject.getUserName(tLRPC$User)));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i3) {
                    ChatUsersActivity.this.lambda$createMenuForParticipant$9(j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, arrayList, i2, dialogInterface2, i3);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        } else {
            openRightsEdit2(j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, ((Integer) arrayList.get(i2)).intValue(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenuForParticipant$9(long j, int i, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, ArrayList arrayList, int i2, DialogInterface dialogInterface, int i3) {
        openRightsEdit2(j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, ((Integer) arrayList.get(i2)).intValue(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenuForParticipant$13(CharSequence[] charSequenceArr, long j, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str, final TLObject tLObject, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, DialogInterface dialogInterface, int i) {
        TLRPC$Chat chat;
        int i2 = this.type;
        if (i2 == 1) {
            if (i == 0 && charSequenceArr.length == 2) {
                ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, null, null, str, 0, true, false, null);
                chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.17
                    @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                    public void didSetRights(int i3, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str2) {
                        TLObject tLObject2 = tLObject;
                        if (tLObject2 instanceof TLRPC$ChannelParticipant) {
                            TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject2;
                            tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights2;
                            tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights2;
                            tLRPC$ChannelParticipant.rank = str2;
                            ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant, tLRPC$TL_chatAdminRights2, tLRPC$TL_chatBannedRights2, 0L, false);
                        }
                    }

                    @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                    public void didChangeOwner(TLRPC$User tLRPC$User) {
                        ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
                    }
                });
                presentFragment(chatRightsEditActivity);
                return;
            }
            getMessagesController().setUserAdminRole(this.chatId, getMessagesController().getUser(Long.valueOf(j)), new TLRPC$TL_chatAdminRights(), "", !this.isChannel, this, false, false, null, null);
            removeParticipants(j);
        } else if (i2 != 0 && i2 != 3) {
            if (i == 0) {
                TLRPC$User tLRPC$User = null;
                if (j > 0) {
                    chat = null;
                    tLRPC$User = getMessagesController().getUser(Long.valueOf(j));
                } else {
                    chat = getMessagesController().getChat(Long.valueOf(-j));
                }
                getMessagesController().deleteParticipantFromChat(this.chatId, tLRPC$User, chat, false, false);
            }
        } else {
            if (i == 0) {
                if (i2 == 3) {
                    ChatRightsEditActivity chatRightsEditActivity2 = new ChatRightsEditActivity(j, this.chatId, null, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, 1, true, false, null);
                    chatRightsEditActivity2.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.ChatUsersActivity.18
                        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                        public void didSetRights(int i3, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str2) {
                            TLObject tLObject2 = tLObject;
                            if (tLObject2 instanceof TLRPC$ChannelParticipant) {
                                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject2;
                                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights2;
                                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights2;
                                tLRPC$ChannelParticipant.rank = str2;
                                ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant, tLRPC$TL_chatAdminRights2, tLRPC$TL_chatBannedRights2, 0L, false);
                            }
                        }

                        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
                        public void didChangeOwner(TLRPC$User tLRPC$User2) {
                            ChatUsersActivity.this.onOwnerChaged(tLRPC$User2);
                        }
                    });
                    presentFragment(chatRightsEditActivity2);
                } else if (i2 == 0 && j > 0) {
                    getMessagesController().addUserToChat(this.chatId, getMessagesController().getUser(Long.valueOf(j)), 0, null, this, null);
                }
            } else if (i == 1) {
                TLRPC$TL_channels_editBanned tLRPC$TL_channels_editBanned = new TLRPC$TL_channels_editBanned();
                tLRPC$TL_channels_editBanned.participant = getMessagesController().getInputPeer(j);
                tLRPC$TL_channels_editBanned.channel = getMessagesController().getInputChannel(this.chatId);
                tLRPC$TL_channels_editBanned.banned_rights = new TLRPC$TL_chatBannedRights();
                getConnectionsManager().sendRequest(tLRPC$TL_channels_editBanned, new RequestDelegate() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda17
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                        ChatUsersActivity.this.lambda$createMenuForParticipant$12(tLObject2, tLRPC$TL_error);
                    }
                });
            }
            if ((i == 0 && this.type == 0) || i == 1) {
                removeParticipants(tLObject);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenuForParticipant$12(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            final TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
            getMessagesController().processUpdates(tLRPC$Updates, false);
            if (tLRPC$Updates.chats.isEmpty()) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.this.lambda$createMenuForParticipant$11(tLRPC$Updates);
                }
            }, 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMenuForParticipant$11(TLRPC$Updates tLRPC$Updates) {
        getMessagesController().loadFullChat(tLRPC$Updates.chats.get(0).id, 0, true);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            if (tLRPC$ChatFull.id == this.chatId) {
                if (booleanValue && ChatObject.isChannel(this.currentChat)) {
                    return;
                }
                boolean z = this.info != null;
                this.info = tLRPC$ChatFull;
                if (!z) {
                    int currentSlowmode = getCurrentSlowmode();
                    this.initialSlowmode = currentSlowmode;
                    this.selectedSlowmode = currentSlowmode;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatUsersActivity.this.lambda$didReceivedNotification$14();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$14() {
        loadChatParticipants(0, 200);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return checkDiscard();
    }

    public void setDelegate(ChatUsersActivityDelegate chatUsersActivityDelegate) {
        this.delegate = chatUsersActivityDelegate;
    }

    private int getCurrentSlowmode() {
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
            int i = tLRPC$ChatFull.slowmode_seconds;
            if (i == 10) {
                return 1;
            }
            if (i == 30) {
                return 2;
            }
            if (i == 60) {
                return 3;
            }
            if (i == 300) {
                return 4;
            }
            if (i == 900) {
                return 5;
            }
            return i == 3600 ? 6 : 0;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatSeconds(int i) {
        if (i < 60) {
            return LocaleController.formatPluralString("Seconds", i, new Object[0]);
        }
        if (i < 3600) {
            return LocaleController.formatPluralString("Minutes", i / 60, new Object[0]);
        }
        return LocaleController.formatPluralString("Hours", (i / 60) / 60, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        if (ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights) && this.initialSlowmode == this.selectedSlowmode) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", R.string.UserRestrictionsApplyChanges));
        if (this.isChannel) {
            builder.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", R.string.ChannelSettingsChangedAlert));
        } else {
            builder.setMessage(LocaleController.getString("GroupSettingsChangedAlert", R.string.GroupSettingsChangedAlert));
        }
        builder.setPositiveButton(LocaleController.getString("ApplyTheme", R.string.ApplyTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChatUsersActivity.this.lambda$checkDiscard$15(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("PassportDiscard", R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChatUsersActivity.this.lambda$checkDiscard$16(dialogInterface, i);
            }
        });
        showDialog(builder.create());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$15(DialogInterface dialogInterface, int i) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$16(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    public boolean hasSelectType() {
        return this.selectType != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatUserPermissions(TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights) {
        if (tLRPC$TL_chatBannedRights == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = tLRPC$TL_chatBannedRights.view_messages;
        if (z && this.defaultBannedRights.view_messages != z) {
            sb.append(LocaleController.getString("UserRestrictionsNoRead", R.string.UserRestrictionsNoRead));
        }
        if (tLRPC$TL_chatBannedRights.send_messages && this.defaultBannedRights.send_plain != tLRPC$TL_chatBannedRights.send_plain) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendText", R.string.UserRestrictionsNoSendText));
        }
        boolean z2 = tLRPC$TL_chatBannedRights.send_media;
        if (z2 && this.defaultBannedRights.send_media != z2) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendMedia", R.string.UserRestrictionsNoSendMedia));
        } else {
            boolean z3 = tLRPC$TL_chatBannedRights.send_photos;
            if (z3 && this.defaultBannedRights.send_photos != z3) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendPhotos", R.string.UserRestrictionsNoSendPhotos));
            }
            boolean z4 = tLRPC$TL_chatBannedRights.send_videos;
            if (z4 && this.defaultBannedRights.send_videos != z4) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendVideos", R.string.UserRestrictionsNoSendVideos));
            }
            boolean z5 = tLRPC$TL_chatBannedRights.send_audios;
            if (z5 && this.defaultBannedRights.send_audios != z5) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendMusic", R.string.UserRestrictionsNoSendMusic));
            }
            boolean z6 = tLRPC$TL_chatBannedRights.send_docs;
            if (z6 && this.defaultBannedRights.send_docs != z6) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendDocs", R.string.UserRestrictionsNoSendDocs));
            }
            boolean z7 = tLRPC$TL_chatBannedRights.send_voices;
            if (z7 && this.defaultBannedRights.send_voices != z7) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendVoice", R.string.UserRestrictionsNoSendVoice));
            }
            boolean z8 = tLRPC$TL_chatBannedRights.send_roundvideos;
            if (z8 && this.defaultBannedRights.send_roundvideos != z8) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.getString("UserRestrictionsNoSendRound", R.string.UserRestrictionsNoSendRound));
            }
        }
        boolean z9 = tLRPC$TL_chatBannedRights.send_stickers;
        if (z9 && this.defaultBannedRights.send_stickers != z9) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendStickers", R.string.UserRestrictionsNoSendStickers));
        }
        boolean z10 = tLRPC$TL_chatBannedRights.send_polls;
        if (z10 && this.defaultBannedRights.send_polls != z10) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendPolls", R.string.UserRestrictionsNoSendPolls));
        }
        boolean z11 = tLRPC$TL_chatBannedRights.embed_links;
        if (z11 && !tLRPC$TL_chatBannedRights.send_plain && this.defaultBannedRights.embed_links != z11) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoEmbedLinks", R.string.UserRestrictionsNoEmbedLinks));
        }
        boolean z12 = tLRPC$TL_chatBannedRights.invite_users;
        if (z12 && this.defaultBannedRights.invite_users != z12) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoInviteUsers", R.string.UserRestrictionsNoInviteUsers));
        }
        boolean z13 = tLRPC$TL_chatBannedRights.pin_messages;
        if (z13 && this.defaultBannedRights.pin_messages != z13) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoPinMessages", R.string.UserRestrictionsNoPinMessages));
        }
        boolean z14 = tLRPC$TL_chatBannedRights.change_info;
        if (z14 && this.defaultBannedRights.change_info != z14) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoChangeInfo", R.string.UserRestrictionsNoChangeInfo));
        }
        if (sb.length() != 0) {
            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
            sb.append('.');
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDone() {
        TLRPC$ChatFull tLRPC$ChatFull;
        if (this.type != 3) {
            return;
        }
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        if (tLRPC$Chat.creator && !ChatObject.isChannel(tLRPC$Chat) && this.selectedSlowmode != this.initialSlowmode && this.info != null) {
            MessagesController.getInstance(this.currentAccount).convertToMegaGroup(getParentActivity(), this.chatId, this, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda15
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    ChatUsersActivity.this.lambda$processDone$17(j);
                }
            });
            return;
        }
        if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights)) {
            getMessagesController().setDefaultBannedRole(this.chatId, this.defaultBannedRights, ChatObject.isChannel(this.currentChat), this);
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            if (chat != null) {
                chat.default_banned_rights = this.defaultBannedRights;
            }
        }
        int i = this.selectedSlowmode;
        if (i != this.initialSlowmode && (tLRPC$ChatFull = this.info) != null) {
            tLRPC$ChatFull.slowmode_seconds = getSecondsForIndex(i);
            this.info.flags |= 131072;
            getMessagesController().setChannelSlowMode(this.chatId, this.info.slowmode_seconds);
        }
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$17(long j) {
        if (j != 0) {
            this.chatId = j;
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
            processDone();
        }
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            int currentSlowmode = getCurrentSlowmode();
            this.initialSlowmode = currentSlowmode;
            this.selectedSlowmode = currentSlowmode;
        }
    }

    private int getChannelAdminParticipantType(TLObject tLObject) {
        if ((tLObject instanceof TLRPC$TL_channelParticipantCreator) || (tLObject instanceof TLRPC$TL_channelParticipantSelf)) {
            return 0;
        }
        return ((tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_channelParticipant)) ? 1 : 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadChatParticipants(int i, int i2) {
        if (this.loadingUsers) {
            return;
        }
        this.contactsEndReached = false;
        this.botsEndReached = false;
        loadChatParticipants(i, i2, true);
    }

    private ArrayList<TLRPC$TL_channels_getParticipants> loadChatParticipantsRequests(int i, int i2, boolean z) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
        ArrayList<TLRPC$TL_channels_getParticipants> arrayList = new ArrayList<>();
        arrayList.add(tLRPC$TL_channels_getParticipants);
        tLRPC$TL_channels_getParticipants.channel = getMessagesController().getInputChannel(this.chatId);
        int i3 = this.type;
        if (i3 == 0) {
            tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsKicked();
        } else if (i3 == 1) {
            tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsAdmins();
        } else if (i3 == 2) {
            TLRPC$ChatFull tLRPC$ChatFull = this.info;
            if (tLRPC$ChatFull != null && tLRPC$ChatFull.participants_count <= 200 && (tLRPC$Chat = this.currentChat) != null && tLRPC$Chat.megagroup) {
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
            } else if (this.selectType == 1) {
                if (!this.contactsEndReached) {
                    this.delayResults = 2;
                    tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsContacts();
                    this.contactsEndReached = true;
                    arrayList.addAll(loadChatParticipantsRequests(0, 200, false));
                } else {
                    tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
                }
            } else if (!this.contactsEndReached) {
                this.delayResults = 3;
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsContacts();
                this.contactsEndReached = true;
                arrayList.addAll(loadChatParticipantsRequests(0, 200, false));
            } else if (!this.botsEndReached) {
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$ChannelParticipantsFilter() { // from class: org.telegram.tgnet.TLRPC$TL_channelParticipantsBots
                    public static int constructor = -1328445861;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(constructor);
                    }
                };
                this.botsEndReached = true;
                arrayList.addAll(loadChatParticipantsRequests(0, 200, false));
            } else {
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
            }
        } else if (i3 == 3) {
            tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsBanned();
        }
        tLRPC$TL_channels_getParticipants.filter.q = "";
        tLRPC$TL_channels_getParticipants.offset = i;
        tLRPC$TL_channels_getParticipants.limit = i2;
        return arrayList;
    }

    private void loadChatParticipants(int i, int i2, boolean z) {
        LongSparseArray<TLRPC$TL_groupCallParticipant> longSparseArray;
        int i3 = 0;
        if (!ChatObject.isChannel(this.currentChat)) {
            this.loadingUsers = false;
            this.participants.clear();
            this.bots.clear();
            this.contacts.clear();
            this.participantsMap.clear();
            this.contactsMap.clear();
            this.botsMap.clear();
            int i4 = this.type;
            if (i4 == 1) {
                TLRPC$ChatFull tLRPC$ChatFull = this.info;
                if (tLRPC$ChatFull != null) {
                    int size = tLRPC$ChatFull.participants.participants.size();
                    while (i3 < size) {
                        TLRPC$ChatParticipant tLRPC$ChatParticipant = this.info.participants.participants.get(i3);
                        if ((tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator) || (tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin)) {
                            this.participants.add(tLRPC$ChatParticipant);
                        }
                        this.participantsMap.put(tLRPC$ChatParticipant.user_id, tLRPC$ChatParticipant);
                        i3++;
                    }
                }
            } else if (i4 == 2 && this.info != null) {
                long j = getUserConfig().clientUserId;
                int size2 = this.info.participants.participants.size();
                while (i3 < size2) {
                    TLRPC$ChatParticipant tLRPC$ChatParticipant2 = this.info.participants.participants.get(i3);
                    if ((this.selectType == 0 || tLRPC$ChatParticipant2.user_id != j) && ((longSparseArray = this.ignoredUsers) == null || longSparseArray.indexOfKey(tLRPC$ChatParticipant2.user_id) < 0)) {
                        if (this.selectType == 1) {
                            if (getContactsController().isContact(tLRPC$ChatParticipant2.user_id)) {
                                this.contacts.add(tLRPC$ChatParticipant2);
                                this.contactsMap.put(tLRPC$ChatParticipant2.user_id, tLRPC$ChatParticipant2);
                            } else if (!UserObject.isDeleted(getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant2.user_id)))) {
                                this.participants.add(tLRPC$ChatParticipant2);
                                this.participantsMap.put(tLRPC$ChatParticipant2.user_id, tLRPC$ChatParticipant2);
                            }
                        } else if (getContactsController().isContact(tLRPC$ChatParticipant2.user_id)) {
                            this.contacts.add(tLRPC$ChatParticipant2);
                            this.contactsMap.put(tLRPC$ChatParticipant2.user_id, tLRPC$ChatParticipant2);
                        } else {
                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$ChatParticipant2.user_id));
                            if (user != null && user.bot) {
                                this.bots.add(tLRPC$ChatParticipant2);
                                this.botsMap.put(tLRPC$ChatParticipant2.user_id, tLRPC$ChatParticipant2);
                            } else {
                                this.participants.add(tLRPC$ChatParticipant2);
                                this.participantsMap.put(tLRPC$ChatParticipant2.user_id, tLRPC$ChatParticipant2);
                            }
                        }
                    }
                    i3++;
                }
            }
            ListAdapter listAdapter = this.listViewAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
            updateRows();
            ListAdapter listAdapter2 = this.listViewAdapter;
            if (listAdapter2 != null) {
                listAdapter2.notifyDataSetChanged();
                return;
            }
            return;
        }
        this.loadingUsers = true;
        StickerEmptyView stickerEmptyView = this.emptyView;
        if (stickerEmptyView != null) {
            stickerEmptyView.showProgress(true, false);
        }
        ListAdapter listAdapter3 = this.listViewAdapter;
        if (listAdapter3 != null) {
            listAdapter3.notifyDataSetChanged();
        }
        final ArrayList<TLRPC$TL_channels_getParticipants> loadChatParticipantsRequests = loadChatParticipantsRequests(i, i2, z);
        final ArrayList arrayList = new ArrayList();
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ChatUsersActivity.this.lambda$loadChatParticipants$18(loadChatParticipantsRequests, arrayList);
            }
        };
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        while (i3 < loadChatParticipantsRequests.size()) {
            arrayList.add(null);
            final int i5 = i3;
            getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(loadChatParticipantsRequests.get(i3), new RequestDelegate() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda16
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ChatUsersActivity.lambda$loadChatParticipants$20(arrayList, i5, atomicInteger, loadChatParticipantsRequests, runnable, tLObject, tLRPC$TL_error);
                }
            }), this.classGuid);
            i3++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadChatParticipants$18(ArrayList arrayList, ArrayList arrayList2) {
        int i;
        ArrayList<TLObject> arrayList3;
        LongSparseArray<TLObject> longSparseArray;
        int i2;
        TLRPC$Chat tLRPC$Chat;
        LongSparseArray<TLRPC$TL_groupCallParticipant> longSparseArray2;
        boolean z = false;
        int i3 = 0;
        int i4 = 0;
        while (i4 < arrayList.size()) {
            TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = (TLRPC$TL_channels_getParticipants) arrayList.get(i4);
            TLRPC$TL_channels_channelParticipants tLRPC$TL_channels_channelParticipants = (TLRPC$TL_channels_channelParticipants) arrayList2.get(i4);
            if (tLRPC$TL_channels_getParticipants == null || tLRPC$TL_channels_channelParticipants == null) {
                i = i4;
            } else {
                if (this.type == 1) {
                    getMessagesController().processLoadedAdminsResponse(this.chatId, tLRPC$TL_channels_channelParticipants);
                }
                getMessagesController().putUsers(tLRPC$TL_channels_channelParticipants.users, z);
                getMessagesController().putChats(tLRPC$TL_channels_channelParticipants.chats, z);
                long clientUserId = getUserConfig().getClientUserId();
                if (this.selectType != 0) {
                    int i5 = 0;
                    while (true) {
                        if (i5 >= tLRPC$TL_channels_channelParticipants.participants.size()) {
                            break;
                        } else if (MessageObject.getPeerId(tLRPC$TL_channels_channelParticipants.participants.get(i5).peer) == clientUserId) {
                            tLRPC$TL_channels_channelParticipants.participants.remove(i5);
                            break;
                        } else {
                            i5++;
                        }
                    }
                }
                if (this.type == 2) {
                    this.delayResults--;
                    TLRPC$ChannelParticipantsFilter tLRPC$ChannelParticipantsFilter = tLRPC$TL_channels_getParticipants.filter;
                    if (tLRPC$ChannelParticipantsFilter instanceof TLRPC$TL_channelParticipantsContacts) {
                        arrayList3 = this.contacts;
                        longSparseArray = this.contactsMap;
                    } else if (tLRPC$ChannelParticipantsFilter instanceof TLRPC$TL_channelParticipantsBots) {
                        arrayList3 = this.bots;
                        longSparseArray = this.botsMap;
                    } else {
                        arrayList3 = this.participants;
                        longSparseArray = this.participantsMap;
                    }
                } else {
                    arrayList3 = this.participants;
                    longSparseArray = this.participantsMap;
                    longSparseArray.clear();
                }
                arrayList3.clear();
                arrayList3.addAll(tLRPC$TL_channels_channelParticipants.participants);
                int size = tLRPC$TL_channels_channelParticipants.participants.size();
                int i6 = 0;
                while (i6 < size) {
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = tLRPC$TL_channels_channelParticipants.participants.get(i6);
                    int i7 = i4;
                    if (tLRPC$ChannelParticipant.user_id == clientUserId) {
                        arrayList3.remove(tLRPC$ChannelParticipant);
                    } else {
                        longSparseArray.put(MessageObject.getPeerId(tLRPC$ChannelParticipant.peer), tLRPC$ChannelParticipant);
                    }
                    i6++;
                    i4 = i7;
                }
                i = i4;
                int size2 = arrayList3.size() + i3;
                if (this.type == 2) {
                    int size3 = this.participants.size();
                    int i8 = 0;
                    while (i8 < size3) {
                        TLObject tLObject = this.participants.get(i8);
                        if (!(tLObject instanceof TLRPC$ChannelParticipant)) {
                            this.participants.remove(i8);
                        } else {
                            long peerId = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer);
                            if ((this.contactsMap.get(peerId) == null && this.botsMap.get(peerId) == null && (this.selectType != 1 || peerId <= 0 || !UserObject.isDeleted(getMessagesController().getUser(Long.valueOf(peerId)))) && ((longSparseArray2 = this.ignoredUsers) == null || longSparseArray2.indexOfKey(peerId) < 0)) ? false : true) {
                                this.participants.remove(i8);
                                this.participantsMap.remove(peerId);
                            } else {
                                i8++;
                            }
                        }
                        i8--;
                        size3--;
                        i8++;
                    }
                }
                try {
                    i2 = this.type;
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if ((i2 == 0 || i2 == 3 || i2 == 2) && (tLRPC$Chat = this.currentChat) != null && tLRPC$Chat.megagroup) {
                    TLRPC$ChatFull tLRPC$ChatFull = this.info;
                    if ((tLRPC$ChatFull instanceof TLRPC$TL_channelFull) && tLRPC$ChatFull.participants_count <= 200) {
                        sortUsers(arrayList3);
                        i3 = size2;
                    }
                }
                if (i2 == 1) {
                    sortAdmins(this.participants);
                }
                i3 = size2;
            }
            i4 = i + 1;
            z = false;
        }
        if (this.type != 2 || this.delayResults <= 0) {
            ListAdapter listAdapter = this.listViewAdapter;
            showItemsAnimated(listAdapter != null ? listAdapter.getItemCount() : 0);
            this.loadingUsers = false;
            this.firstLoaded = true;
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility((this.type != 0 || i3 > 5) ? 0 : 8);
            }
        }
        updateRows();
        if (this.listViewAdapter != null) {
            this.listView.setAnimateEmptyView(this.openTransitionStarted, 0);
            this.listViewAdapter.notifyDataSetChanged();
            if (this.emptyView != null && this.listViewAdapter.getItemCount() == 0 && this.firstLoaded) {
                this.emptyView.showProgress(false, true);
            }
        }
        resumeDelayedFragmentAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadChatParticipants$20(final ArrayList arrayList, final int i, final AtomicInteger atomicInteger, final ArrayList arrayList2, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ChatUsersActivity.lambda$loadChatParticipants$19(TLRPC$TL_error.this, tLObject, arrayList, i, atomicInteger, arrayList2, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadChatParticipants$19(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, ArrayList arrayList, int i, AtomicInteger atomicInteger, ArrayList arrayList2, Runnable runnable) {
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_channels_channelParticipants)) {
            arrayList.set(i, (TLRPC$TL_channels_channelParticipants) tLObject);
        }
        atomicInteger.getAndIncrement();
        if (atomicInteger.get() == arrayList2.size()) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sortUsers(ArrayList<TLObject> arrayList) {
        final int currentTime = getConnectionsManager().getCurrentTime();
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda14
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortUsers$21;
                lambda$sortUsers$21 = ChatUsersActivity.this.lambda$sortUsers$21(currentTime, (TLObject) obj, (TLObject) obj2);
                return lambda$sortUsers$21;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$sortUsers$21(int i, TLObject tLObject, TLObject tLObject2) {
        int i2;
        TLRPC$UserStatus tLRPC$UserStatus;
        TLRPC$UserStatus tLRPC$UserStatus2;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant2 = (TLRPC$ChannelParticipant) tLObject2;
        long peerId = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
        long peerId2 = MessageObject.getPeerId(tLRPC$ChannelParticipant2.peer);
        int i3 = -100;
        if (peerId > 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(MessageObject.getPeerId(tLRPC$ChannelParticipant.peer)));
            if (user == null || (tLRPC$UserStatus2 = user.status) == null) {
                i2 = 0;
            } else {
                i2 = user.self ? i + 50000 : tLRPC$UserStatus2.expires;
            }
        } else {
            i2 = -100;
        }
        if (peerId2 > 0) {
            TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(MessageObject.getPeerId(tLRPC$ChannelParticipant2.peer)));
            if (user2 == null || (tLRPC$UserStatus = user2.status) == null) {
                i3 = 0;
            } else {
                i3 = user2.self ? i + 50000 : tLRPC$UserStatus.expires;
            }
        }
        if (i2 > 0 && i3 > 0) {
            if (i2 > i3) {
                return 1;
            }
            return i2 < i3 ? -1 : 0;
        } else if (i2 < 0 && i3 < 0) {
            if (i2 > i3) {
                return 1;
            }
            return i2 < i3 ? -1 : 0;
        } else if ((i2 >= 0 || i3 <= 0) && (i2 != 0 || i3 == 0)) {
            return ((i3 >= 0 || i2 <= 0) && (i3 != 0 || i2 == 0)) ? 0 : 1;
        } else {
            return -1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        StickerEmptyView stickerEmptyView = this.emptyView;
        if (stickerEmptyView != null) {
            stickerEmptyView.requestLayout();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.openTransitionStarted = true;
        }
        if (z && !z2 && this.needOpenSearch) {
            this.searchItem.getSearchField().requestFocus();
            AndroidUtilities.showKeyboard(this.searchItem.getSearchField());
            this.searchItem.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private int contactsStartRow;
        private int globalStartRow;
        private int groupStartRow;
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private boolean searchInProgress;
        private Runnable searchRunnable;
        private ArrayList<Object> searchResult = new ArrayList<>();
        private LongSparseArray<TLObject> searchResultMap = new LongSparseArray<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private int totalCount = 0;

        public SearchAdapter(Context context) {
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public final void onDataSetChanged(int i) {
                    ChatUsersActivity.SearchAdapter.this.lambda$new$0(i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            if (this.searchAdapterHelper.isSearchInProgress()) {
                return;
            }
            int itemCount = getItemCount();
            notifyDataSetChanged();
            if (getItemCount() > itemCount) {
                ChatUsersActivity.this.showItemsAnimated(itemCount);
            }
            if (this.searchInProgress || getItemCount() != 0 || i == 0) {
                return;
            }
            ChatUsersActivity.this.emptyView.showProgress(false, true);
        }

        public void searchUsers(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            this.searchResult.clear();
            this.searchResultMap.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, ChatUsersActivity.this.type != 0, false, true, false, false, ChatObject.isChannel(ChatUsersActivity.this.currentChat) ? ChatUsersActivity.this.chatId : 0L, false, ChatUsersActivity.this.type, 0);
            notifyDataSetChanged();
            if (TextUtils.isEmpty(str)) {
                return;
            }
            this.searchInProgress = true;
            ChatUsersActivity.this.emptyView.showProgress(true, true);
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.SearchAdapter.this.lambda$searchUsers$1(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: processSearch */
        public void lambda$searchUsers$1(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.SearchAdapter.this.lambda$processSearch$3(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$3(final String str) {
            Runnable runnable = null;
            this.searchRunnable = null;
            final ArrayList arrayList = (ChatObject.isChannel(ChatUsersActivity.this.currentChat) || ChatUsersActivity.this.info == null) ? null : new ArrayList(ChatUsersActivity.this.info.participants.participants);
            final ArrayList arrayList2 = ChatUsersActivity.this.selectType == 1 ? new ArrayList(ChatUsersActivity.this.getContactsController().contacts) : null;
            if (arrayList != null || arrayList2 != null) {
                runnable = new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatUsersActivity.SearchAdapter.this.lambda$processSearch$2(str, arrayList, arrayList2);
                    }
                };
            } else {
                this.searchInProgress = false;
            }
            this.searchAdapterHelper.queryServerSearch(str, ChatUsersActivity.this.selectType != 0, false, true, false, false, ChatObject.isChannel(ChatUsersActivity.this.currentChat) ? ChatUsersActivity.this.chatId : 0L, false, ChatUsersActivity.this.type, 1, 0L, runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0142, code lost:
            if (r15.contains(" " + r4) != false) goto L58;
         */
        /* JADX WARN: Code restructure failed: missing block: B:90:0x024b, code lost:
            if (r5.contains(" " + r9) != false) goto L109;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:104:0x02a9 A[LOOP:3: B:81:0x0211->B:104:0x02a9, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x0157 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x025f A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x018c A[LOOP:1: B:39:0x0104->B:64:0x018c, LOOP_END] */
        /* JADX WARN: Type inference failed for: r15v3, types: [java.util.ArrayList] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList, ArrayList arrayList2) {
            int i;
            LongSparseArray<TLObject> longSparseArray;
            ArrayList<Object> arrayList3;
            int i2;
            long peerId;
            ArrayList<Object> arrayList4;
            LongSparseArray<TLObject> longSparseArray2;
            String[] strArr;
            int i3;
            int i4;
            String publicUsername;
            String str2;
            String str3;
            String str4;
            char c;
            ArrayList arrayList5 = arrayList;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new LongSparseArray<>(), new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
            int i5 = 0;
            int i6 = (translitString != null ? 1 : 0) + 1;
            String[] strArr2 = new String[i6];
            strArr2[0] = lowerCase;
            if (translitString != null) {
                strArr2[1] = translitString;
            }
            ArrayList<Object> arrayList6 = new ArrayList<>();
            LongSparseArray<TLObject> longSparseArray3 = new LongSparseArray<>();
            ArrayList<CharSequence> arrayList7 = new ArrayList<>();
            ArrayList<TLObject> arrayList8 = new ArrayList<>();
            if (arrayList5 != null) {
                int size = arrayList.size();
                while (i5 < size) {
                    TLObject tLObject = (TLObject) arrayList5.get(i5);
                    if (tLObject instanceof TLRPC$ChatParticipant) {
                        i2 = i6;
                        peerId = ((TLRPC$ChatParticipant) tLObject).user_id;
                    } else {
                        i2 = i6;
                        if (tLObject instanceof TLRPC$ChannelParticipant) {
                            peerId = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer);
                        }
                        arrayList4 = arrayList6;
                        longSparseArray2 = longSparseArray3;
                        strArr = strArr2;
                        i3 = size;
                        i4 = i2;
                        i5++;
                        arrayList5 = arrayList;
                        size = i3;
                        longSparseArray3 = longSparseArray2;
                        arrayList6 = arrayList4;
                        i6 = i4;
                        strArr2 = strArr;
                    }
                    if (peerId > 0) {
                        TLRPC$User user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(peerId));
                        if (user.id != ChatUsersActivity.this.getUserConfig().getClientUserId()) {
                            str3 = UserObject.getUserName(user).toLowerCase();
                            publicUsername = UserObject.getPublicUsername(user);
                            str2 = user.first_name;
                            str4 = user.last_name;
                            i3 = size;
                        }
                        arrayList4 = arrayList6;
                        longSparseArray2 = longSparseArray3;
                        strArr = strArr2;
                        i3 = size;
                        i4 = i2;
                        i5++;
                        arrayList5 = arrayList;
                        size = i3;
                        longSparseArray3 = longSparseArray2;
                        arrayList6 = arrayList4;
                        i6 = i4;
                        strArr2 = strArr;
                    } else {
                        TLRPC$Chat chat = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-peerId));
                        String lowerCase2 = chat.title.toLowerCase();
                        publicUsername = ChatObject.getPublicUsername(chat);
                        str2 = chat.title;
                        str3 = lowerCase2;
                        i3 = size;
                        str4 = null;
                    }
                    String translitString2 = LocaleController.getInstance().getTranslitString(str3);
                    if (str3.equals(translitString2)) {
                        translitString2 = null;
                    }
                    arrayList4 = arrayList6;
                    longSparseArray2 = longSparseArray3;
                    int i7 = i2;
                    int i8 = 0;
                    char c2 = 0;
                    while (true) {
                        i4 = i7;
                        if (i8 >= i7) {
                            strArr = strArr2;
                            break;
                        }
                        String str5 = strArr2[i8];
                        if (str3.startsWith(str5)) {
                            strArr = strArr2;
                        } else {
                            strArr = strArr2;
                            if (!str3.contains(" " + str5)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str5)) {
                                    }
                                }
                                c = (publicUsername == null || !publicUsername.startsWith(str5)) ? c2 : (char) 2;
                                if (c == 0) {
                                    if (c == 1) {
                                        arrayList7.add(AndroidUtilities.generateSearchName(str2, str4, str5));
                                    } else {
                                        arrayList7.add(AndroidUtilities.generateSearchName("@" + publicUsername, null, "@" + str5));
                                    }
                                    arrayList8.add(tLObject);
                                } else {
                                    i8++;
                                    i7 = i4;
                                    c2 = c;
                                    strArr2 = strArr;
                                }
                            }
                        }
                        c = 1;
                        if (c == 0) {
                        }
                    }
                    i5++;
                    arrayList5 = arrayList;
                    size = i3;
                    longSparseArray3 = longSparseArray2;
                    arrayList6 = arrayList4;
                    i6 = i4;
                    strArr2 = strArr;
                }
            }
            ArrayList<Object> arrayList9 = arrayList6;
            LongSparseArray<TLObject> longSparseArray4 = longSparseArray3;
            int i9 = i6;
            String[] strArr3 = strArr2;
            if (arrayList2 != null) {
                int i10 = 0;
                while (i10 < arrayList2.size()) {
                    TLRPC$User user2 = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(((TLRPC$TL_contact) arrayList2.get(i10)).user_id));
                    if (user2.id == ChatUsersActivity.this.getUserConfig().getClientUserId()) {
                        longSparseArray = longSparseArray4;
                        arrayList3 = arrayList9;
                        i = i9;
                    } else {
                        String lowerCase3 = UserObject.getUserName(user2).toLowerCase();
                        String translitString3 = LocaleController.getInstance().getTranslitString(lowerCase3);
                        if (lowerCase3.equals(translitString3)) {
                            translitString3 = null;
                        }
                        i = i9;
                        char c3 = 0;
                        for (int i11 = 0; i11 < i; i11++) {
                            String str6 = strArr3[i11];
                            if (!lowerCase3.startsWith(str6)) {
                                if (!lowerCase3.contains(" " + str6)) {
                                    if (translitString3 != null) {
                                        if (!translitString3.startsWith(str6)) {
                                        }
                                    }
                                    String publicUsername2 = UserObject.getPublicUsername(user2);
                                    if (publicUsername2 != null && publicUsername2.startsWith(str6)) {
                                        c3 = 2;
                                    }
                                    if (c3 == 0) {
                                        if (c3 == 1) {
                                            arrayList7.add(AndroidUtilities.generateSearchName(user2.first_name, user2.last_name, str6));
                                            arrayList3 = arrayList9;
                                        } else {
                                            arrayList7.add(AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(user2), null, "@" + str6));
                                            arrayList3 = arrayList9;
                                        }
                                        arrayList3.add(user2);
                                        LongSparseArray<TLObject> longSparseArray5 = longSparseArray4;
                                        longSparseArray5.put(user2.id, user2);
                                        longSparseArray = longSparseArray5;
                                        i10++;
                                        i9 = i;
                                        longSparseArray4 = longSparseArray;
                                        arrayList9 = arrayList3;
                                    }
                                }
                            }
                            c3 = 1;
                            if (c3 == 0) {
                            }
                        }
                        longSparseArray = longSparseArray4;
                        arrayList3 = arrayList9;
                    }
                    i10++;
                    i9 = i;
                    longSparseArray4 = longSparseArray;
                    arrayList9 = arrayList3;
                }
            }
            updateSearchResults(arrayList9, longSparseArray4, arrayList7, arrayList8);
        }

        private void updateSearchResults(final ArrayList<Object> arrayList, final LongSparseArray<TLObject> longSparseArray, final ArrayList<CharSequence> arrayList2, final ArrayList<TLObject> arrayList3) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatUsersActivity.SearchAdapter.this.lambda$updateSearchResults$4(arrayList, longSparseArray, arrayList2, arrayList3);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, ArrayList arrayList3) {
            if (ChatUsersActivity.this.searching) {
                this.searchInProgress = false;
                this.searchResult = arrayList;
                this.searchResultMap = longSparseArray;
                this.searchResultNames = arrayList2;
                this.searchAdapterHelper.mergeResults(arrayList);
                if (!ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                    ArrayList<TLObject> groupSearch = this.searchAdapterHelper.getGroupSearch();
                    groupSearch.clear();
                    groupSearch.addAll(arrayList3);
                }
                int itemCount = getItemCount();
                notifyDataSetChanged();
                if (getItemCount() > itemCount) {
                    ChatUsersActivity.this.showItemsAnimated(itemCount);
                }
                if (this.searchAdapterHelper.isSearchInProgress() || getItemCount() != 0) {
                    return;
                }
                ChatUsersActivity.this.emptyView.showProgress(false, true);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.totalCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            this.totalCount = 0;
            int size = this.searchAdapterHelper.getGroupSearch().size();
            if (size != 0) {
                this.groupStartRow = 0;
                this.totalCount += size + 1;
            } else {
                this.groupStartRow = -1;
            }
            int size2 = this.searchResult.size();
            if (size2 != 0) {
                int i = this.totalCount;
                this.contactsStartRow = i;
                this.totalCount = i + size2 + 1;
            } else {
                this.contactsStartRow = -1;
            }
            int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            if (size3 != 0) {
                int i2 = this.totalCount;
                this.globalStartRow = i2;
                this.totalCount = i2 + size3 + 1;
            } else {
                this.globalStartRow = -1;
            }
            if (ChatUsersActivity.this.searching && ChatUsersActivity.this.listView != null && ChatUsersActivity.this.listView.getAdapter() != ChatUsersActivity.this.searchListViewAdapter) {
                ChatUsersActivity.this.listView.setAnimateEmptyView(true, 0);
                ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.searchListViewAdapter);
                ChatUsersActivity.this.listView.setFastScrollVisible(false);
                ChatUsersActivity.this.listView.setVerticalScrollBarEnabled(true);
            }
            super.notifyDataSetChanged();
        }

        public void removeUserId(long j) {
            this.searchAdapterHelper.removeUserId(j);
            TLObject tLObject = this.searchResultMap.get(j);
            if (tLObject != null) {
                this.searchResult.remove(tLObject);
            }
            notifyDataSetChanged();
        }

        public TLObject getItem(int i) {
            int size = this.searchAdapterHelper.getGroupSearch().size();
            if (size != 0) {
                int i2 = size + 1;
                if (i2 > i) {
                    if (i == 0) {
                        return null;
                    }
                    return this.searchAdapterHelper.getGroupSearch().get(i - 1);
                }
                i -= i2;
            }
            int size2 = this.searchResult.size();
            if (size2 != 0) {
                int i3 = size2 + 1;
                if (i3 > i) {
                    if (i == 0) {
                        return null;
                    }
                    return (TLObject) this.searchResult.get(i - 1);
                }
                i -= i3;
            }
            int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            if (size3 == 0 || size3 + 1 <= i || i == 0) {
                return null;
            }
            return this.searchAdapterHelper.getGlobalSearch().get(i - 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$5(ManageChatUserCell manageChatUserCell, boolean z) {
            TLObject item = getItem(((Integer) manageChatUserCell.getTag()).intValue());
            if (item instanceof TLRPC$ChannelParticipant) {
                return ChatUsersActivity.this.createMenuForParticipant((TLRPC$ChannelParticipant) item, !z);
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            if (i == 0) {
                ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 2, 2, ChatUsersActivity.this.selectType == 0);
                manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                manageChatUserCell.setDelegate(new ManageChatUserCell.ManageChatUserCellDelegate() { // from class: org.telegram.ui.ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
                    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell2, boolean z) {
                        boolean lambda$onCreateViewHolder$5;
                        lambda$onCreateViewHolder$5 = ChatUsersActivity.SearchAdapter.this.lambda$onCreateViewHolder$5(manageChatUserCell2, z);
                        return lambda$onCreateViewHolder$5;
                    }
                });
                frameLayout = manageChatUserCell;
            } else {
                frameLayout = new GraySectionCell(this.mContext);
            }
            return new RecyclerListView.Holder(frameLayout);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x0111  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0143  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x014f A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x015d  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01a1 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01ae  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String publicUsername;
            TLRPC$User tLRPC$User;
            int size;
            String str;
            boolean z;
            SpannableStringBuilder spannableStringBuilder;
            int i2;
            boolean z2;
            String str2;
            String str3;
            int indexOfIgnoreCase;
            int size2;
            int size3;
            int i3;
            String str4;
            int i4 = i;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (i4 == this.groupStartRow) {
                    if (ChatUsersActivity.this.type != 0) {
                        if (ChatUsersActivity.this.type != 3) {
                            if (ChatUsersActivity.this.isChannel) {
                                graySectionCell.setText(LocaleController.getString("ChannelSubscribers", R.string.ChannelSubscribers));
                                return;
                            } else {
                                graySectionCell.setText(LocaleController.getString("ChannelMembers", R.string.ChannelMembers));
                                return;
                            }
                        }
                        graySectionCell.setText(LocaleController.getString("ChannelRestrictedUsers", R.string.ChannelRestrictedUsers));
                        return;
                    }
                    graySectionCell.setText(LocaleController.getString("ChannelBlockedUsers", R.string.ChannelBlockedUsers));
                    return;
                } else if (i4 == this.globalStartRow) {
                    graySectionCell.setText(LocaleController.getString("GlobalSearch", R.string.GlobalSearch));
                    return;
                } else if (i4 == this.contactsStartRow) {
                    graySectionCell.setText(LocaleController.getString("Contacts", R.string.Contacts));
                    return;
                } else {
                    return;
                }
            }
            TLObject item = getItem(i4);
            boolean z3 = item instanceof TLRPC$User;
            TLRPC$User tLRPC$User2 = item;
            if (!z3) {
                if (item instanceof TLRPC$ChannelParticipant) {
                    long peerId = MessageObject.getPeerId(((TLRPC$ChannelParticipant) item).peer);
                    if (peerId >= 0) {
                        TLRPC$User user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(peerId));
                        tLRPC$User2 = user;
                        if (user != null) {
                            publicUsername = UserObject.getPublicUsername(user);
                            tLRPC$User = user;
                            size = this.searchAdapterHelper.getGroupSearch().size();
                            if (size != 0) {
                                int i5 = size + 1;
                                if (i5 > i4) {
                                    str = this.searchAdapterHelper.getLastFoundChannel();
                                    z = true;
                                    if (!z && (size3 = this.searchResult.size()) != 0) {
                                        i3 = size3 + 1;
                                        if (i3 <= i4) {
                                            CharSequence charSequence = this.searchResultNames.get(i4 - 1);
                                            if (charSequence != 0 && !TextUtils.isEmpty(publicUsername)) {
                                                if (charSequence.toString().startsWith("@" + publicUsername)) {
                                                    spannableStringBuilder = null;
                                                    str4 = charSequence;
                                                    i2 = i4;
                                                    z2 = true;
                                                    str2 = str4;
                                                    str3 = str2;
                                                    str3 = str2;
                                                    if (!z2 && publicUsername != null) {
                                                        size2 = this.searchAdapterHelper.getGlobalSearch().size();
                                                        str3 = str2;
                                                        if (size2 != 0) {
                                                            str3 = str2;
                                                            if (size2 + 1 > i2) {
                                                                String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                                                                if (lastFoundUsername.startsWith("@")) {
                                                                    lastFoundUsername = lastFoundUsername.substring(1);
                                                                }
                                                                try {
                                                                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                                                    spannableStringBuilder2.append((CharSequence) "@");
                                                                    spannableStringBuilder2.append((CharSequence) publicUsername);
                                                                    int indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(publicUsername, lastFoundUsername);
                                                                    str3 = spannableStringBuilder2;
                                                                    if (indexOfIgnoreCase2 != -1) {
                                                                        int length = lastFoundUsername.length();
                                                                        if (indexOfIgnoreCase2 == 0) {
                                                                            length++;
                                                                        } else {
                                                                            indexOfIgnoreCase2++;
                                                                        }
                                                                        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), indexOfIgnoreCase2, length + indexOfIgnoreCase2, 33);
                                                                        str3 = spannableStringBuilder2;
                                                                    }
                                                                } catch (Exception e) {
                                                                    FileLog.e(e);
                                                                    str3 = publicUsername;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (str != null && publicUsername != null) {
                                                        spannableStringBuilder = new SpannableStringBuilder(publicUsername);
                                                        indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(publicUsername, str);
                                                        if (indexOfIgnoreCase != -1) {
                                                            spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), indexOfIgnoreCase, str.length() + indexOfIgnoreCase, 33);
                                                        }
                                                    }
                                                    ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                                                    manageChatUserCell.setTag(Integer.valueOf(i2));
                                                    manageChatUserCell.setData(tLRPC$User, spannableStringBuilder, str3, false);
                                                }
                                            }
                                            spannableStringBuilder = charSequence;
                                            str4 = null;
                                            i2 = i4;
                                            z2 = true;
                                            str2 = str4;
                                            str3 = str2;
                                            str3 = str2;
                                            if (!z2) {
                                                size2 = this.searchAdapterHelper.getGlobalSearch().size();
                                                str3 = str2;
                                                if (size2 != 0) {
                                                }
                                            }
                                            if (str != null) {
                                                spannableStringBuilder = new SpannableStringBuilder(publicUsername);
                                                indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(publicUsername, str);
                                                if (indexOfIgnoreCase != -1) {
                                                }
                                            }
                                            ManageChatUserCell manageChatUserCell2 = (ManageChatUserCell) viewHolder.itemView;
                                            manageChatUserCell2.setTag(Integer.valueOf(i2));
                                            manageChatUserCell2.setData(tLRPC$User, spannableStringBuilder, str3, false);
                                        }
                                        i4 -= i3;
                                    }
                                    spannableStringBuilder = null;
                                    i2 = i4;
                                    z2 = z;
                                    str2 = null;
                                    str3 = str2;
                                    str3 = str2;
                                    if (!z2) {
                                    }
                                    if (str != null) {
                                    }
                                    ManageChatUserCell manageChatUserCell22 = (ManageChatUserCell) viewHolder.itemView;
                                    manageChatUserCell22.setTag(Integer.valueOf(i2));
                                    manageChatUserCell22.setData(tLRPC$User, spannableStringBuilder, str3, false);
                                }
                                i4 -= i5;
                            }
                            str = null;
                            z = false;
                            if (!z) {
                                i3 = size3 + 1;
                                if (i3 <= i4) {
                                }
                            }
                            spannableStringBuilder = null;
                            i2 = i4;
                            z2 = z;
                            str2 = null;
                            str3 = str2;
                            str3 = str2;
                            if (!z2) {
                            }
                            if (str != null) {
                            }
                            ManageChatUserCell manageChatUserCell222 = (ManageChatUserCell) viewHolder.itemView;
                            manageChatUserCell222.setTag(Integer.valueOf(i2));
                            manageChatUserCell222.setData(tLRPC$User, spannableStringBuilder, str3, false);
                        }
                    } else {
                        TLRPC$Chat chat = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-peerId));
                        tLRPC$User2 = chat;
                        if (chat != null) {
                            publicUsername = ChatObject.getPublicUsername(chat);
                            tLRPC$User = chat;
                            size = this.searchAdapterHelper.getGroupSearch().size();
                            if (size != 0) {
                            }
                            str = null;
                            z = false;
                            if (!z) {
                            }
                            spannableStringBuilder = null;
                            i2 = i4;
                            z2 = z;
                            str2 = null;
                            str3 = str2;
                            str3 = str2;
                            if (!z2) {
                            }
                            if (str != null) {
                            }
                            ManageChatUserCell manageChatUserCell2222 = (ManageChatUserCell) viewHolder.itemView;
                            manageChatUserCell2222.setTag(Integer.valueOf(i2));
                            manageChatUserCell2222.setData(tLRPC$User, spannableStringBuilder, str3, false);
                        }
                    }
                } else if (!(item instanceof TLRPC$ChatParticipant)) {
                    return;
                } else {
                    tLRPC$User2 = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(((TLRPC$ChatParticipant) item).user_id));
                }
            }
            publicUsername = null;
            tLRPC$User = tLRPC$User2;
            size = this.searchAdapterHelper.getGroupSearch().size();
            if (size != 0) {
            }
            str = null;
            z = false;
            if (!z) {
            }
            spannableStringBuilder = null;
            i2 = i4;
            z2 = z;
            str2 = null;
            str3 = str2;
            str3 = str2;
            if (!z2) {
            }
            if (str != null) {
            }
            ManageChatUserCell manageChatUserCell22222 = (ManageChatUserCell) viewHolder.itemView;
            manageChatUserCell22222.setTag(Integer.valueOf(i2));
            manageChatUserCell22222.setData(tLRPC$User, spannableStringBuilder, str3, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (i == this.globalStartRow || i == this.groupStartRow || i == this.contactsStartRow) ? 1 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 7 || itemViewType == 14) {
                return ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat);
            }
            if (itemViewType == 0) {
                Object currentObject = ((ManageChatUserCell) viewHolder.itemView).getCurrentObject();
                return (ChatUsersActivity.this.type != 1 && (currentObject instanceof TLRPC$User) && ((TLRPC$User) currentObject).self) ? false : true;
            }
            int adapterPosition = viewHolder.getAdapterPosition();
            if (itemViewType == 0 || itemViewType == 2 || itemViewType == 6) {
                return true;
            }
            if (itemViewType == 12) {
                if (adapterPosition == ChatUsersActivity.this.antiSpamRow) {
                    return ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 13);
                }
                if (adapterPosition == ChatUsersActivity.this.hideMembersRow) {
                    return ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 2);
                }
            }
            return itemViewType == 13;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChatUsersActivity.this.rowCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$0(ManageChatUserCell manageChatUserCell, boolean z) {
            return ChatUsersActivity.this.createMenuForParticipant(ChatUsersActivity.this.listViewAdapter.getItem(((Integer) manageChatUserCell.getTag()).intValue()), !z);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SlideChooseView slideChooseView;
            int i2 = 2;
            int i3 = 7;
            switch (i) {
                case 0:
                    Context context = this.mContext;
                    if (ChatUsersActivity.this.type != 0 && ChatUsersActivity.this.type != 3) {
                        i3 = 6;
                    }
                    ManageChatUserCell manageChatUserCell = new ManageChatUserCell(context, i3, (ChatUsersActivity.this.type == 0 || ChatUsersActivity.this.type == 3) ? 6 : 6, ChatUsersActivity.this.selectType == 0);
                    manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    manageChatUserCell.setDelegate(new ManageChatUserCell.ManageChatUserCellDelegate() { // from class: org.telegram.ui.ChatUsersActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
                        public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell2, boolean z) {
                            boolean lambda$onCreateViewHolder$0;
                            lambda$onCreateViewHolder$0 = ChatUsersActivity.ListAdapter.this.lambda$onCreateViewHolder$0(manageChatUserCell2, z);
                            return lambda$onCreateViewHolder$0;
                        }
                    });
                    slideChooseView = manageChatUserCell;
                    break;
                case 1:
                    slideChooseView = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 2:
                    View manageChatTextCell = new ManageChatTextCell(this.mContext);
                    manageChatTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    slideChooseView = manageChatTextCell;
                    break;
                case 3:
                    slideChooseView = new ShadowSectionCell(this.mContext);
                    break;
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                    if (ChatUsersActivity.this.isChannel) {
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.NoBlockedChannel2));
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.NoBlockedGroup2));
                    }
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                    slideChooseView = textInfoPrivacyCell;
                    break;
                case 5:
                    HeaderCell headerCell = new HeaderCell(this.mContext, "windowBackgroundWhiteBlueHeader", 21, 11, false);
                    headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    headerCell.setHeight(43);
                    slideChooseView = headerCell;
                    break;
                case 6:
                    View textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    slideChooseView = textSettingsCell;
                    break;
                case 7:
                case 14:
                    View textCheckCell2 = new TextCheckCell2(this.mContext);
                    textCheckCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    slideChooseView = textCheckCell2;
                    break;
                case 8:
                    View graySectionCell = new GraySectionCell(this.mContext);
                    graySectionCell.setBackground(null);
                    slideChooseView = graySectionCell;
                    break;
                case 9:
                default:
                    SlideChooseView slideChooseView2 = new SlideChooseView(this.mContext);
                    slideChooseView2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    int i4 = ChatUsersActivity.this.selectedSlowmode;
                    int i5 = R.string.SlowmodeSeconds;
                    int i6 = R.string.SlowmodeMinutes;
                    slideChooseView2.setOptions(i4, LocaleController.getString("SlowmodeOff", R.string.SlowmodeOff), LocaleController.formatString("SlowmodeSeconds", i5, 10), LocaleController.formatString("SlowmodeSeconds", i5, 30), LocaleController.formatString("SlowmodeMinutes", i6, 1), LocaleController.formatString("SlowmodeMinutes", i6, 5), LocaleController.formatString("SlowmodeMinutes", i6, 15), LocaleController.formatString("SlowmodeHours", R.string.SlowmodeHours, 1));
                    slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.ChatUsersActivity$ListAdapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public final void onOptionSelected(int i7) {
                            ChatUsersActivity.ListAdapter.this.lambda$onCreateViewHolder$1(i7);
                        }

                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public /* synthetic */ void onTouchEnd() {
                            SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                        }
                    });
                    slideChooseView = slideChooseView2;
                    break;
                case 10:
                    slideChooseView = new LoadingCell(this.mContext, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(120.0f));
                    break;
                case 11:
                    FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext);
                    flickerLoadingView.setIsSingleCell(true);
                    flickerLoadingView.setViewType(6);
                    flickerLoadingView.showDate(false);
                    flickerLoadingView.setPaddingLeft(AndroidUtilities.dp(5.0f));
                    flickerLoadingView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    flickerLoadingView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                    slideChooseView = flickerLoadingView;
                    break;
                case 12:
                    TextCell textCell = new TextCell(this.mContext, 23, false, true, ChatUsersActivity.this.getResourceProvider());
                    textCell.heightDp = 50;
                    textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    slideChooseView = textCell;
                    break;
                case 13:
                    CheckBoxCell checkBoxCell = new CheckBoxCell(this.mContext, 4, 21, ChatUsersActivity.this.getResourceProvider());
                    checkBoxCell.getCheckBoxRound().setDrawBackgroundAsArc(14);
                    checkBoxCell.getCheckBoxRound().setColor("switch2TrackChecked", "radioBackground", "checkboxCheck");
                    checkBoxCell.setEnabled(true);
                    checkBoxCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    slideChooseView = checkBoxCell;
                    break;
            }
            return new RecyclerListView.Holder(slideChooseView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$1(int i) {
            if (ChatUsersActivity.this.info == null) {
                return;
            }
            ChatUsersActivity.this.selectedSlowmode = i;
            ChatUsersActivity.this.listViewAdapter.notifyItemChanged(ChatUsersActivity.this.slowmodeInfoRow);
        }

        /* JADX WARN: Code restructure failed: missing block: B:345:0x090e, code lost:
            if (r22.this$0.currentChat.megagroup == false) goto L384;
         */
        /* JADX WARN: Code restructure failed: missing block: B:346:0x0910, code lost:
            r7 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:354:0x093c, code lost:
            if (r22.this$0.currentChat.megagroup == false) goto L384;
         */
        /* JADX WARN: Removed duplicated region for block: B:403:0x0a4e  */
        /* JADX WARN: Removed duplicated region for block: B:404:0x0a51  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            boolean z;
            long j;
            int i3;
            boolean z2;
            boolean z3;
            long j2;
            long j3;
            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
            boolean z4;
            long j4;
            Object chat;
            CharSequence charSequence;
            boolean z5;
            int i4;
            CharSequence charSequence2;
            TLRPC$User user;
            CharSequence charSequence3;
            boolean z6;
            int i5;
            CharSequence charSequence4;
            CharSequence charSequence5;
            boolean z7;
            TLRPC$User user2;
            CharSequence charSequence6;
            boolean z8;
            boolean z9 = true;
            boolean z10 = true;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                    manageChatUserCell.setTag(Integer.valueOf(i));
                    TLObject item = getItem(i);
                    if (i < ChatUsersActivity.this.participantsStartRow || i >= ChatUsersActivity.this.participantsEndRow) {
                        if (i < ChatUsersActivity.this.contactsStartRow || i >= ChatUsersActivity.this.contactsEndRow) {
                            i2 = ChatUsersActivity.this.botEndRow;
                        } else {
                            i2 = ChatUsersActivity.this.contactsEndRow;
                            if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                                break;
                            }
                        }
                        z = false;
                    } else {
                        i2 = ChatUsersActivity.this.participantsEndRow;
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                            break;
                        }
                        z = false;
                    }
                    if (item instanceof TLRPC$ChannelParticipant) {
                        TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) item;
                        j = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                        j3 = tLRPC$ChannelParticipant.kicked_by;
                        long j5 = tLRPC$ChannelParticipant.promoted_by;
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = tLRPC$ChannelParticipant.banned_rights;
                        i3 = tLRPC$ChannelParticipant.date;
                        z4 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantBanned;
                        z2 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator;
                        z3 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin;
                        tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights2;
                        j2 = j5;
                    } else if (!(item instanceof TLRPC$ChatParticipant)) {
                        return;
                    } else {
                        TLRPC$ChatParticipant tLRPC$ChatParticipant = (TLRPC$ChatParticipant) item;
                        j = tLRPC$ChatParticipant.user_id;
                        i3 = tLRPC$ChatParticipant.date;
                        z2 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator;
                        z3 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin;
                        j2 = 0;
                        j3 = 0;
                        tLRPC$TL_chatBannedRights = null;
                        z4 = false;
                    }
                    if (j > 0) {
                        chat = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j));
                        j4 = j2;
                    } else {
                        j4 = j2;
                        chat = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-j));
                    }
                    if (chat != null) {
                        if (ChatUsersActivity.this.type == 3) {
                            CharSequence formatUserPermissions = ChatUsersActivity.this.formatUserPermissions(tLRPC$TL_chatBannedRights);
                            if (i != i2 - 1) {
                                charSequence6 = null;
                                z8 = true;
                            } else {
                                charSequence6 = null;
                                z8 = false;
                            }
                            manageChatUserCell.setData(chat, charSequence6, formatUserPermissions, z8);
                            return;
                        } else if (ChatUsersActivity.this.type != 0) {
                            if (ChatUsersActivity.this.type != 1) {
                                if (ChatUsersActivity.this.type == 2) {
                                    CharSequence formatJoined = (!z || i3 == 0) ? null : LocaleController.formatJoined(i3);
                                    if (i != i2 - 1) {
                                        charSequence = null;
                                        z5 = true;
                                    } else {
                                        charSequence = null;
                                        z5 = false;
                                    }
                                    manageChatUserCell.setData(chat, charSequence, formatJoined, z5);
                                    return;
                                }
                                return;
                            }
                            if (z2) {
                                charSequence2 = LocaleController.getString("ChannelCreator", R.string.ChannelCreator);
                            } else {
                                if (!z3 || (user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j4))) == null) {
                                    i4 = 1;
                                    charSequence2 = null;
                                } else if (user.id == j) {
                                    charSequence2 = LocaleController.getString("ChannelAdministrator", R.string.ChannelAdministrator);
                                } else {
                                    i4 = 1;
                                    charSequence2 = LocaleController.formatString("EditAdminPromotedBy", R.string.EditAdminPromotedBy, UserObject.getUserName(user));
                                }
                                if (i == i2 - i4) {
                                    charSequence3 = null;
                                    z6 = true;
                                } else {
                                    charSequence3 = null;
                                    z6 = false;
                                }
                                manageChatUserCell.setData(chat, charSequence3, charSequence2, z6);
                                return;
                            }
                            i4 = 1;
                            if (i == i2 - i4) {
                            }
                            manageChatUserCell.setData(chat, charSequence3, charSequence2, z6);
                            return;
                        } else {
                            if (!z4 || (user2 = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j3))) == null) {
                                i5 = 1;
                                charSequence4 = null;
                            } else {
                                i5 = 1;
                                charSequence4 = LocaleController.formatString("UserRemovedBy", R.string.UserRemovedBy, UserObject.getUserName(user2));
                            }
                            if (i != i2 - i5) {
                                charSequence5 = null;
                                z7 = true;
                            } else {
                                charSequence5 = null;
                                z7 = false;
                            }
                            manageChatUserCell.setData(chat, charSequence5, charSequence4, z7);
                            return;
                        }
                    }
                    return;
                case 1:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != ChatUsersActivity.this.antiSpamInfoRow) {
                        if (i == ChatUsersActivity.this.participantsInfoRow) {
                            if (ChatUsersActivity.this.type == 0 || ChatUsersActivity.this.type == 3) {
                                if (ChatUsersActivity.this.isChannel) {
                                    textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedChannel2", R.string.NoBlockedChannel2));
                                } else {
                                    textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedGroup2", R.string.NoBlockedGroup2));
                                }
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                                return;
                            } else if (ChatUsersActivity.this.type == 1) {
                                if (ChatUsersActivity.this.addNewRow != -1) {
                                    if (ChatUsersActivity.this.isChannel) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("ChannelAdminsInfo", R.string.ChannelAdminsInfo));
                                    } else {
                                        textInfoPrivacyCell.setText(LocaleController.getString("MegaAdminsInfo", R.string.MegaAdminsInfo));
                                    }
                                } else {
                                    textInfoPrivacyCell.setText("");
                                }
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                                return;
                            } else if (ChatUsersActivity.this.type == 2) {
                                if (!ChatUsersActivity.this.isChannel || ChatUsersActivity.this.selectType != 0) {
                                    textInfoPrivacyCell.setText("");
                                } else {
                                    textInfoPrivacyCell.setText(LocaleController.getString("ChannelMembersInfo", R.string.ChannelMembersInfo));
                                }
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                                return;
                            } else {
                                return;
                            }
                        } else if (i != ChatUsersActivity.this.slowmodeInfoRow) {
                            if (i != ChatUsersActivity.this.hideMembersInfoRow) {
                                if (i == ChatUsersActivity.this.gigaInfoRow) {
                                    textInfoPrivacyCell.setText(LocaleController.getString("BroadcastGroupConvertInfo", R.string.BroadcastGroupConvertInfo));
                                    return;
                                }
                                return;
                            }
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, "windowBackgroundGrayShadow"));
                            textInfoPrivacyCell.setText(LocaleController.getString("ChannelHideMembersInfo", R.string.ChannelHideMembersInfo));
                            return;
                        } else {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, "windowBackgroundGrayShadow"));
                            ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                            int secondsForIndex = chatUsersActivity.getSecondsForIndex(chatUsersActivity.selectedSlowmode);
                            if (ChatUsersActivity.this.info == null || secondsForIndex == 0) {
                                textInfoPrivacyCell.setText(LocaleController.getString("SlowmodeInfoOff", R.string.SlowmodeInfoOff));
                                return;
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.formatString("SlowmodeInfoSelected", R.string.SlowmodeInfoSelected, ChatUsersActivity.this.formatSeconds(secondsForIndex)));
                                return;
                            }
                        }
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("ChannelAntiSpamInfo", R.string.ChannelAntiSpamInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, "windowBackgroundGrayShadow"));
                    return;
                case 2:
                    ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                    manageChatTextCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                    if (i == ChatUsersActivity.this.addNewRow) {
                        if (ChatUsersActivity.this.type != 3) {
                            if (ChatUsersActivity.this.type != 0) {
                                if (ChatUsersActivity.this.type != 1) {
                                    if (ChatUsersActivity.this.type == 2) {
                                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                        if (ChatUsersActivity.this.addNew2Row != -1 || ((!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) && ChatUsersActivity.this.membersHeaderRow == -1 && !ChatUsersActivity.this.participants.isEmpty())) {
                                            r10 = true;
                                        }
                                        if (ChatUsersActivity.this.isChannel) {
                                            manageChatTextCell.setText(LocaleController.getString("AddSubscriber", R.string.AddSubscriber), null, R.drawable.msg_contact_add, r10);
                                            return;
                                        } else {
                                            manageChatTextCell.setText(LocaleController.getString("AddMember", R.string.AddMember), null, R.drawable.msg_contact_add, r10);
                                            return;
                                        }
                                    }
                                    return;
                                }
                                manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                manageChatTextCell.setText(LocaleController.getString("ChannelAddAdmin", R.string.ChannelAddAdmin), null, R.drawable.msg_admin_add, (!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) ? true : true);
                                return;
                            }
                            manageChatTextCell.setText(LocaleController.getString("ChannelBlockUser", R.string.ChannelBlockUser), null, R.drawable.msg_user_remove, false);
                            return;
                        }
                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        manageChatTextCell.setText(LocaleController.getString("ChannelAddException", R.string.ChannelAddException), null, R.drawable.msg_contact_add, ChatUsersActivity.this.participantsStartRow != -1);
                        return;
                    } else if (i == ChatUsersActivity.this.recentActionsRow) {
                        manageChatTextCell.setText(LocaleController.getString("EventLog", R.string.EventLog), null, R.drawable.msg_log, ChatUsersActivity.this.antiSpamRow > ChatUsersActivity.this.recentActionsRow);
                        return;
                    } else if (i != ChatUsersActivity.this.addNew2Row) {
                        if (i == ChatUsersActivity.this.gigaConvertRow) {
                            manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                            manageChatTextCell.setText(LocaleController.getString("BroadcastGroupConvert", R.string.BroadcastGroupConvert), null, R.drawable.msg_channel, false);
                            return;
                        }
                        return;
                    } else {
                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        if ((!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) && ChatUsersActivity.this.membersHeaderRow == -1 && !ChatUsersActivity.this.participants.isEmpty()) {
                            r10 = true;
                        }
                        manageChatTextCell.setText(LocaleController.getString("ChannelInviteViaLink", R.string.ChannelInviteViaLink), null, R.drawable.msg_link2, r10);
                        return;
                    }
                case 3:
                    if (i == ChatUsersActivity.this.addNewSectionRow || (ChatUsersActivity.this.type == 3 && i == ChatUsersActivity.this.participantsDividerRow && ChatUsersActivity.this.addNewRow == -1 && ChatUsersActivity.this.participantsStartRow == -1)) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, "windowBackgroundGrayShadow"));
                        return;
                    }
                case 4:
                case 9:
                case 10:
                default:
                    return;
                case 5:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == ChatUsersActivity.this.restricted1SectionRow) {
                        if (ChatUsersActivity.this.type == 0) {
                            int size = ChatUsersActivity.this.info != null ? ChatUsersActivity.this.info.kicked_count : ChatUsersActivity.this.participants.size();
                            if (size != 0) {
                                headerCell.setText(LocaleController.formatPluralString("RemovedUser", size, new Object[0]));
                                return;
                            } else {
                                headerCell.setText(LocaleController.getString("ChannelBlockedUsers", R.string.ChannelBlockedUsers));
                                return;
                            }
                        }
                        headerCell.setText(LocaleController.getString("ChannelRestrictedUsers", R.string.ChannelRestrictedUsers));
                        return;
                    } else if (i != ChatUsersActivity.this.permissionsSectionRow) {
                        if (i != ChatUsersActivity.this.slowmodeRow) {
                            if (i == ChatUsersActivity.this.gigaHeaderRow) {
                                headerCell.setText(LocaleController.getString("BroadcastGroup", R.string.BroadcastGroup));
                                return;
                            }
                            return;
                        }
                        headerCell.setText(LocaleController.getString("Slowmode", R.string.Slowmode));
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString("ChannelPermissionsHeader", R.string.ChannelPermissionsHeader));
                        return;
                    }
                case 6:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    String string = LocaleController.getString("ChannelBlacklist", R.string.ChannelBlacklist);
                    Object[] objArr = new Object[1];
                    objArr[0] = Integer.valueOf(ChatUsersActivity.this.info != null ? ChatUsersActivity.this.info.kicked_count : 0);
                    textSettingsCell.setTextAndValue(string, String.format("%d", objArr), false);
                    return;
                case 7:
                case 14:
                    final TextCheckCell2 textCheckCell2 = (TextCheckCell2) viewHolder.itemView;
                    boolean z11 = textCheckCell2.getTag() != null && ((Integer) textCheckCell2.getTag()).intValue() == i;
                    textCheckCell2.setTag(Integer.valueOf(i));
                    if (i == ChatUsersActivity.this.changeInfoRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsChangeInfo", R.string.UserRestrictionsChangeInfo), (ChatUsersActivity.this.defaultBannedRights.change_info || ChatObject.isPublic(ChatUsersActivity.this.currentChat)) ? false : true, ChatUsersActivity.this.manageTopicsRow != -1, z11);
                    } else if (i == ChatUsersActivity.this.addUsersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsInviteUsers", R.string.UserRestrictionsInviteUsers), !ChatUsersActivity.this.defaultBannedRights.invite_users, true, z11);
                    } else if (i == ChatUsersActivity.this.pinMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsPinMessages", R.string.UserRestrictionsPinMessages), (ChatUsersActivity.this.defaultBannedRights.pin_messages || ChatObject.isPublic(ChatUsersActivity.this.currentChat)) ? false : true, true, z11);
                    } else if (i == ChatUsersActivity.this.sendMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendText", R.string.UserRestrictionsSendText), !ChatUsersActivity.this.defaultBannedRights.send_plain, true, z11);
                    } else if (i == ChatUsersActivity.this.sendMediaRow) {
                        int sendMediaSelectedCount = ChatUsersActivity.this.getSendMediaSelectedCount();
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendMedia", R.string.UserRestrictionsSendMedia), sendMediaSelectedCount > 0, true, z11);
                        textCheckCell2.setCollapseArrow(String.format(Locale.US, "%d/9", Integer.valueOf(sendMediaSelectedCount)), !ChatUsersActivity.this.sendMediaExpanded, new Runnable() { // from class: org.telegram.ui.ChatUsersActivity.ListAdapter.1
                            @Override // java.lang.Runnable
                            public void run() {
                                boolean z12 = !textCheckCell2.isChecked();
                                textCheckCell2.setChecked(z12);
                                ChatUsersActivity.this.setSendMediaEnabled(z12);
                            }
                        });
                    } else if (i == ChatUsersActivity.this.sendStickersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendStickers", R.string.UserRestrictionsSendStickers), !ChatUsersActivity.this.defaultBannedRights.send_stickers, true, z11);
                    } else if (i == ChatUsersActivity.this.embedLinksRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsEmbedLinks", R.string.UserRestrictionsEmbedLinks), !ChatUsersActivity.this.defaultBannedRights.embed_links, true, z11);
                    } else if (i == ChatUsersActivity.this.sendPollsRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendPollsShort", R.string.UserRestrictionsSendPollsShort), !ChatUsersActivity.this.defaultBannedRights.send_polls, true);
                    } else if (i == ChatUsersActivity.this.manageTopicsRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("CreateTopicsPermission", R.string.CreateTopicsPermission), !ChatUsersActivity.this.defaultBannedRights.manage_topics, false, z11);
                    }
                    if (ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
                        if ((i == ChatUsersActivity.this.addUsersRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 3)) || ((i == ChatUsersActivity.this.pinMessagesRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 0)) || ((i == ChatUsersActivity.this.changeInfoRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 1)) || ((i == ChatUsersActivity.this.manageTopicsRow && !ChatObject.canManageTopics(ChatUsersActivity.this.currentChat)) || (ChatObject.isPublic(ChatUsersActivity.this.currentChat) && (i == ChatUsersActivity.this.pinMessagesRow || i == ChatUsersActivity.this.changeInfoRow)))))) {
                            textCheckCell2.setIcon(R.drawable.permission_locked);
                            return;
                        } else {
                            textCheckCell2.setIcon(0);
                            return;
                        }
                    }
                    textCheckCell2.setIcon(0);
                    return;
                case 8:
                    GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                    if (i == ChatUsersActivity.this.membersHeaderRow) {
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                            graySectionCell.setText(LocaleController.getString("ChannelOtherSubscribers", R.string.ChannelOtherSubscribers));
                            return;
                        } else {
                            graySectionCell.setText(LocaleController.getString("ChannelOtherMembers", R.string.ChannelOtherMembers));
                            return;
                        }
                    } else if (i != ChatUsersActivity.this.botHeaderRow) {
                        if (i == ChatUsersActivity.this.contactsHeaderRow) {
                            if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                                graySectionCell.setText(LocaleController.getString("ChannelContacts", R.string.ChannelContacts));
                                return;
                            } else {
                                graySectionCell.setText(LocaleController.getString("GroupContacts", R.string.GroupContacts));
                                return;
                            }
                        } else if (i == ChatUsersActivity.this.loadingHeaderRow) {
                            graySectionCell.setText("");
                            return;
                        } else {
                            return;
                        }
                    } else {
                        graySectionCell.setText(LocaleController.getString("ChannelBots", R.string.ChannelBots));
                        return;
                    }
                case 11:
                    FlickerLoadingView flickerLoadingView = (FlickerLoadingView) viewHolder.itemView;
                    if (ChatUsersActivity.this.type == 0) {
                        flickerLoadingView.setItemsCount(ChatUsersActivity.this.info != null ? ChatUsersActivity.this.info.kicked_count : 1);
                        return;
                    } else {
                        flickerLoadingView.setItemsCount(1);
                        return;
                    }
                case 12:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i == ChatUsersActivity.this.antiSpamRow) {
                        textCell.getCheckBox().setIcon((ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 13) && (ChatUsersActivity.this.info == null || ChatUsersActivity.this.info.antispam || ChatUsersActivity.this.info.participants_count >= ChatUsersActivity.this.getMessagesController().telegramAntispamGroupSizeMin)) ? 0 : R.drawable.permission_locked);
                        textCell.setTextAndCheckAndIcon(LocaleController.getString("ChannelAntiSpam", R.string.ChannelAntiSpam), (ChatUsersActivity.this.info == null || !ChatUsersActivity.this.info.antispam) ? false : false, R.drawable.msg_policy, false);
                        return;
                    } else if (i == ChatUsersActivity.this.hideMembersRow) {
                        textCell.getCheckBox().setIcon((ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 2) && (ChatUsersActivity.this.info == null || ChatUsersActivity.this.info.participants_hidden || ChatUsersActivity.this.info.participants_count >= ChatUsersActivity.this.getMessagesController().hiddenMembersGroupSizeMin)) ? 0 : R.drawable.permission_locked);
                        textCell.setTextAndCheck(LocaleController.getString("ChannelHideMembers", R.string.ChannelHideMembers), (ChatUsersActivity.this.info == null || !ChatUsersActivity.this.info.participants_hidden) ? false : false, false);
                        return;
                    } else {
                        return;
                    }
                case 13:
                    CheckBoxCell checkBoxCell = (CheckBoxCell) viewHolder.itemView;
                    boolean z12 = checkBoxCell.getTag() != null && ((Integer) checkBoxCell.getTag()).intValue() == i;
                    checkBoxCell.setTag(Integer.valueOf(i));
                    if (i == ChatUsersActivity.this.sendMediaPhotosRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionPhotos", R.string.SendMediaPermissionPhotos), "", !ChatUsersActivity.this.defaultBannedRights.send_photos, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaVideosRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionVideos", R.string.SendMediaPermissionVideos), "", !ChatUsersActivity.this.defaultBannedRights.send_videos, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaStickerGifsRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionStickersGifs", R.string.SendMediaPermissionStickersGifs), "", !ChatUsersActivity.this.defaultBannedRights.send_stickers, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaMusicRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionMusic", R.string.SendMediaPermissionMusic), "", !ChatUsersActivity.this.defaultBannedRights.send_audios, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaFilesRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionFiles", R.string.SendMediaPermissionFiles), "", !ChatUsersActivity.this.defaultBannedRights.send_docs, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaVoiceMessagesRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionVoice", R.string.SendMediaPermissionVoice), "", !ChatUsersActivity.this.defaultBannedRights.send_voices, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaVideoMessagesRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPermissionRound", R.string.SendMediaPermissionRound), "", !ChatUsersActivity.this.defaultBannedRights.send_roundvideos, true, z12);
                    } else if (i == ChatUsersActivity.this.sendMediaEmbededLinksRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaEmbededLinks", R.string.SendMediaEmbededLinks), "", (ChatUsersActivity.this.defaultBannedRights.embed_links || ChatUsersActivity.this.defaultBannedRights.send_plain) ? false : true, false, z12);
                    } else if (i == ChatUsersActivity.this.sendPollsRow) {
                        checkBoxCell.setText(LocaleController.getString("SendMediaPolls", R.string.SendMediaPolls), "", !ChatUsersActivity.this.defaultBannedRights.send_polls, false, z12);
                    }
                    checkBoxCell.setPad(1);
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ChatUsersActivity.this.addNewRow || i == ChatUsersActivity.this.addNew2Row || i == ChatUsersActivity.this.recentActionsRow || i == ChatUsersActivity.this.gigaConvertRow) {
                return 2;
            }
            if ((i < ChatUsersActivity.this.participantsStartRow || i >= ChatUsersActivity.this.participantsEndRow) && ((i < ChatUsersActivity.this.botStartRow || i >= ChatUsersActivity.this.botEndRow) && (i < ChatUsersActivity.this.contactsStartRow || i >= ChatUsersActivity.this.contactsEndRow))) {
                if (i == ChatUsersActivity.this.addNewSectionRow || i == ChatUsersActivity.this.participantsDividerRow || i == ChatUsersActivity.this.participantsDivider2Row) {
                    return 3;
                }
                if (i == ChatUsersActivity.this.restricted1SectionRow || i == ChatUsersActivity.this.permissionsSectionRow || i == ChatUsersActivity.this.slowmodeRow || i == ChatUsersActivity.this.gigaHeaderRow) {
                    return 5;
                }
                if (i == ChatUsersActivity.this.participantsInfoRow || i == ChatUsersActivity.this.slowmodeInfoRow || i == ChatUsersActivity.this.gigaInfoRow || i == ChatUsersActivity.this.antiSpamInfoRow || i == ChatUsersActivity.this.hideMembersInfoRow) {
                    return 1;
                }
                if (i == ChatUsersActivity.this.blockedEmptyRow) {
                    return 4;
                }
                if (i == ChatUsersActivity.this.removedUsersRow) {
                    return 6;
                }
                if (i == ChatUsersActivity.this.changeInfoRow || i == ChatUsersActivity.this.addUsersRow || i == ChatUsersActivity.this.pinMessagesRow || i == ChatUsersActivity.this.sendMessagesRow || i == ChatUsersActivity.this.sendStickersRow || i == ChatUsersActivity.this.embedLinksRow || i == ChatUsersActivity.this.manageTopicsRow) {
                    return 7;
                }
                if (i == ChatUsersActivity.this.membersHeaderRow || i == ChatUsersActivity.this.contactsHeaderRow || i == ChatUsersActivity.this.botHeaderRow || i == ChatUsersActivity.this.loadingHeaderRow) {
                    return 8;
                }
                if (i == ChatUsersActivity.this.slowmodeSelectRow) {
                    return 9;
                }
                if (i == ChatUsersActivity.this.loadingProgressRow) {
                    return 10;
                }
                if (i == ChatUsersActivity.this.loadingUserCellRow) {
                    return 11;
                }
                if (i == ChatUsersActivity.this.antiSpamRow || i == ChatUsersActivity.this.hideMembersRow) {
                    return 12;
                }
                if (ChatUsersActivity.this.isExpandableSendMediaRow(i)) {
                    return 13;
                }
                return i == ChatUsersActivity.this.sendMediaRow ? 14 : 0;
            }
            return 0;
        }

        public TLObject getItem(int i) {
            if (i < ChatUsersActivity.this.participantsStartRow || i >= ChatUsersActivity.this.participantsEndRow) {
                if (i < ChatUsersActivity.this.contactsStartRow || i >= ChatUsersActivity.this.contactsEndRow) {
                    if (i < ChatUsersActivity.this.botStartRow || i >= ChatUsersActivity.this.botEndRow) {
                        return null;
                    }
                    return (TLObject) ChatUsersActivity.this.bots.get(i - ChatUsersActivity.this.botStartRow);
                }
                return (TLObject) ChatUsersActivity.this.contacts.get(i - ChatUsersActivity.this.contactsStartRow);
            }
            return (TLObject) ChatUsersActivity.this.participants.get(i - ChatUsersActivity.this.participantsStartRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSendMediaEnabled(boolean z) {
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = this.defaultBannedRights;
        tLRPC$TL_chatBannedRights.send_media = !z;
        tLRPC$TL_chatBannedRights.send_photos = !z;
        tLRPC$TL_chatBannedRights.send_videos = !z;
        tLRPC$TL_chatBannedRights.send_stickers = !z;
        tLRPC$TL_chatBannedRights.send_audios = !z;
        tLRPC$TL_chatBannedRights.send_docs = !z;
        tLRPC$TL_chatBannedRights.send_voices = !z;
        tLRPC$TL_chatBannedRights.send_roundvideos = !z;
        tLRPC$TL_chatBannedRights.embed_links = !z;
        tLRPC$TL_chatBannedRights.send_polls = !z;
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isExpandableSendMediaRow(int i) {
        return i == this.sendMediaPhotosRow || i == this.sendMediaVideosRow || i == this.sendMediaStickerGifsRow || i == this.sendMediaMusicRow || i == this.sendMediaFilesRow || i == this.sendMediaVoiceMessagesRow || i == this.sendMediaVideoMessagesRow || i == this.sendMediaEmbededLinksRow || i == this.sendPollsRow;
    }

    public DiffCallback saveState() {
        DiffCallback diffCallback = new DiffCallback();
        diffCallback.oldRowCount = this.rowCount;
        diffCallback.oldBotStartRow = this.botStartRow;
        diffCallback.oldBotEndRow = this.botEndRow;
        diffCallback.oldBots.clear();
        diffCallback.oldBots.addAll(this.bots);
        diffCallback.oldContactsEndRow = this.contactsEndRow;
        diffCallback.oldContactsStartRow = this.contactsStartRow;
        diffCallback.oldContacts.clear();
        diffCallback.oldContacts.addAll(this.contacts);
        diffCallback.oldParticipantsStartRow = this.participantsStartRow;
        diffCallback.oldParticipantsEndRow = this.participantsEndRow;
        diffCallback.oldParticipants.clear();
        diffCallback.oldParticipants.addAll(this.participants);
        diffCallback.fillPositions(diffCallback.oldPositionToItem);
        return diffCallback;
    }

    public void updateListAnimated(DiffCallback diffCallback) {
        if (this.listViewAdapter == null) {
            updateRows();
            return;
        }
        updateRows();
        diffCallback.fillPositions(diffCallback.newPositionToItem);
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this.listViewAdapter);
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || this.layoutManager == null || recyclerListView.getChildCount() <= 0) {
            return;
        }
        View view = null;
        int i = 0;
        int i2 = -1;
        while (true) {
            if (i >= this.listView.getChildCount()) {
                break;
            }
            RecyclerListView recyclerListView2 = this.listView;
            i2 = recyclerListView2.getChildAdapterPosition(recyclerListView2.getChildAt(i));
            if (i2 != -1) {
                view = this.listView.getChildAt(i);
                break;
            }
            i++;
        }
        if (view != null) {
            this.layoutManager.scrollToPositionWithOffset(i2, view.getTop() - this.listView.getPaddingTop());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DiffCallback extends DiffUtil.Callback {
        SparseIntArray newPositionToItem;
        int oldBotEndRow;
        int oldBotStartRow;
        private ArrayList<TLObject> oldBots;
        private ArrayList<TLObject> oldContacts;
        int oldContactsEndRow;
        int oldContactsStartRow;
        private ArrayList<TLObject> oldParticipants;
        int oldParticipantsEndRow;
        int oldParticipantsStartRow;
        SparseIntArray oldPositionToItem;
        int oldRowCount;

        private DiffCallback() {
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
            this.oldParticipants = new ArrayList<>();
            this.oldBots = new ArrayList<>();
            this.oldContacts = new ArrayList<>();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.oldRowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return ChatUsersActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            if (i >= this.oldBotStartRow && i < this.oldBotEndRow && i2 >= ChatUsersActivity.this.botStartRow && i2 < ChatUsersActivity.this.botEndRow) {
                return this.oldBots.get(i - this.oldBotStartRow).equals(ChatUsersActivity.this.bots.get(i2 - ChatUsersActivity.this.botStartRow));
            }
            if (i >= this.oldContactsStartRow && i < this.oldContactsEndRow && i2 >= ChatUsersActivity.this.contactsStartRow && i2 < ChatUsersActivity.this.contactsEndRow) {
                return this.oldContacts.get(i - this.oldContactsStartRow).equals(ChatUsersActivity.this.contacts.get(i2 - ChatUsersActivity.this.contactsStartRow));
            }
            if (i < this.oldParticipantsStartRow || i >= this.oldParticipantsEndRow || i2 < ChatUsersActivity.this.participantsStartRow || i2 >= ChatUsersActivity.this.participantsEndRow) {
                return this.oldPositionToItem.get(i) == this.newPositionToItem.get(i2);
            }
            return this.oldParticipants.get(i - this.oldParticipantsStartRow).equals(ChatUsersActivity.this.participants.get(i2 - ChatUsersActivity.this.participantsStartRow));
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return areItemsTheSame(i, i2) && ChatUsersActivity.this.restricted1SectionRow != i2;
        }

        public void fillPositions(SparseIntArray sparseIntArray) {
            sparseIntArray.clear();
            put(1, ChatUsersActivity.this.recentActionsRow, sparseIntArray);
            put(2, ChatUsersActivity.this.addNewRow, sparseIntArray);
            put(3, ChatUsersActivity.this.addNew2Row, sparseIntArray);
            put(4, ChatUsersActivity.this.addNewSectionRow, sparseIntArray);
            put(5, ChatUsersActivity.this.restricted1SectionRow, sparseIntArray);
            put(6, ChatUsersActivity.this.participantsDividerRow, sparseIntArray);
            put(7, ChatUsersActivity.this.participantsDivider2Row, sparseIntArray);
            put(8, ChatUsersActivity.this.gigaHeaderRow, sparseIntArray);
            put(9, ChatUsersActivity.this.gigaConvertRow, sparseIntArray);
            put(10, ChatUsersActivity.this.gigaInfoRow, sparseIntArray);
            put(11, ChatUsersActivity.this.participantsInfoRow, sparseIntArray);
            put(12, ChatUsersActivity.this.blockedEmptyRow, sparseIntArray);
            put(13, ChatUsersActivity.this.permissionsSectionRow, sparseIntArray);
            put(14, ChatUsersActivity.this.sendMessagesRow, sparseIntArray);
            put(15, ChatUsersActivity.this.sendMediaRow, sparseIntArray);
            put(16, ChatUsersActivity.this.sendStickersRow, sparseIntArray);
            put(17, ChatUsersActivity.this.sendPollsRow, sparseIntArray);
            put(18, ChatUsersActivity.this.embedLinksRow, sparseIntArray);
            put(19, ChatUsersActivity.this.addUsersRow, sparseIntArray);
            int i = 20;
            put(20, ChatUsersActivity.this.pinMessagesRow, sparseIntArray);
            if (ChatUsersActivity.this.isForum) {
                i = 21;
                put(21, ChatUsersActivity.this.manageTopicsRow, sparseIntArray);
            }
            int i2 = i + 1;
            put(i2, ChatUsersActivity.this.changeInfoRow, sparseIntArray);
            int i3 = i2 + 1;
            put(i3, ChatUsersActivity.this.removedUsersRow, sparseIntArray);
            int i4 = i3 + 1;
            put(i4, ChatUsersActivity.this.contactsHeaderRow, sparseIntArray);
            int i5 = i4 + 1;
            put(i5, ChatUsersActivity.this.botHeaderRow, sparseIntArray);
            int i6 = i5 + 1;
            put(i6, ChatUsersActivity.this.membersHeaderRow, sparseIntArray);
            int i7 = i6 + 1;
            put(i7, ChatUsersActivity.this.slowmodeRow, sparseIntArray);
            int i8 = i7 + 1;
            put(i8, ChatUsersActivity.this.slowmodeSelectRow, sparseIntArray);
            int i9 = i8 + 1;
            put(i9, ChatUsersActivity.this.slowmodeInfoRow, sparseIntArray);
            int i10 = i9 + 1;
            put(i10, ChatUsersActivity.this.loadingProgressRow, sparseIntArray);
            int i11 = i10 + 1;
            put(i11, ChatUsersActivity.this.loadingUserCellRow, sparseIntArray);
            put(i11 + 1, ChatUsersActivity.this.loadingHeaderRow, sparseIntArray);
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSendMediaSelectedCount() {
        return getSendMediaSelectedCount(this.defaultBannedRights);
    }

    public static int getSendMediaSelectedCount(TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights) {
        int i = !tLRPC$TL_chatBannedRights.send_photos ? 1 : 0;
        if (!tLRPC$TL_chatBannedRights.send_videos) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.send_stickers) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.send_audios) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.send_docs) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.send_voices) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.send_roundvideos) {
            i++;
        }
        if (!tLRPC$TL_chatBannedRights.embed_links && !tLRPC$TL_chatBannedRights.send_plain) {
            i++;
        }
        return !tLRPC$TL_chatBannedRights.send_polls ? i + 1 : i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ChatUsersActivity$$ExternalSyntheticLambda20
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ChatUsersActivity.this.lambda$getThemeDescriptions$22();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, ManageChatUserCell.class, ManageChatTextCell.class, TextCheckCell2.class, TextSettingsCell.class, SlideChooseView.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switch2Track"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switch2TrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "undo_background"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{UndoView.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerEmptyView.class}, new String[]{"title"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerEmptyView.class}, new String[]{"subtitle"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.emptyView.title, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.emptyView.subtitle, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundPink"));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$22() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof ManageChatUserCell) {
                    ((ManageChatUserCell) childAt).update(0);
                }
            }
        }
    }
}
