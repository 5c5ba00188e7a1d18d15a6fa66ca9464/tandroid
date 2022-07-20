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
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
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
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.GigagroupConvertAlert;
import org.telegram.ui.Components.IntSeekBarAccessibilityDelegate;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarAccessibilityDelegate;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.GroupCreateActivity;
/* loaded from: classes3.dex */
public class ChatUsersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int addNew2Row;
    private int addNewRow;
    private int addNewSectionRow;
    private int addUsersRow;
    private int blockedEmptyRow;
    private int botEndRow;
    private int botHeaderRow;
    private int botStartRow;
    private boolean botsEndReached;
    private int changeInfoRow;
    private boolean contactsEndReached;
    private int contactsEndRow;
    private int contactsHeaderRow;
    private int contactsStartRow;
    private TLRPC$Chat currentChat;
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
    private LongSparseArray<TLRPC$TL_groupCallParticipant> ignoredUsers;
    private TLRPC$ChatFull info;
    private String initialBannedRights;
    private int initialSlowmode;
    private boolean isChannel;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private int loadingHeaderRow;
    private int loadingProgressRow;
    private int loadingUserCellRow;
    private boolean loadingUsers;
    private int membersHeaderRow;
    private boolean openTransitionStarted;
    private int participantsDivider2Row;
    private int participantsDividerRow;
    private int participantsEndRow;
    private int participantsInfoRow;
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
    private int selectedSlowmode;
    private int sendMediaRow;
    private int sendMessagesRow;
    private int sendPollsRow;
    private int sendStickersRow;
    private int slowmodeInfoRow;
    private int slowmodeRow;
    private int slowmodeSelectRow;
    private UndoView undoView;
    private TLRPC$TL_chatBannedRights defaultBannedRights = new TLRPC$TL_chatBannedRights();
    private ArrayList<TLObject> participants = new ArrayList<>();
    private ArrayList<TLObject> bots = new ArrayList<>();
    private ArrayList<TLObject> contacts = new ArrayList<>();
    private LongSparseArray<TLObject> participantsMap = new LongSparseArray<>();
    private LongSparseArray<TLObject> botsMap = new LongSparseArray<>();
    private LongSparseArray<TLObject> contactsMap = new LongSparseArray<>();
    private long chatId = this.arguments.getLong("chat_id");
    private int type = this.arguments.getInt("type");
    private boolean needOpenSearch = this.arguments.getBoolean("open_search");
    private int selectType = this.arguments.getInt("selectType");

    /* loaded from: classes3.dex */
    public interface ChatUsersActivityDelegate {

        /* renamed from: org.telegram.ui.ChatUsersActivity$ChatUsersActivityDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
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

    /* loaded from: classes3.dex */
    public class ChooseView extends View {
        private final SeekBarAccessibilityDelegate accessibilityDelegate;
        private int circleSize;
        private int gapSize;
        private int lineSize;
        private boolean moving;
        private int sideSide;
        private boolean startMoving;
        private int startMovingItem;
        private float startX;
        private final TextPaint textPaint;
        private ArrayList<String> strings = new ArrayList<>();
        private ArrayList<Integer> sizes = new ArrayList<>();
        private final Paint paint = new Paint(1);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChooseView(Context context) {
            super(context);
            String str;
            ChatUsersActivity.this = r8;
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(13.0f));
            int i = 0;
            while (i < 7) {
                if (i == 0) {
                    str = LocaleController.getString("SlowmodeOff", 2131628365);
                } else if (i == 1) {
                    str = LocaleController.formatString("SlowmodeSeconds", 2131628366, 10);
                } else if (i == 2) {
                    str = LocaleController.formatString("SlowmodeSeconds", 2131628366, 30);
                } else if (i == 3) {
                    str = LocaleController.formatString("SlowmodeMinutes", 2131628364, 1);
                } else if (i != 4) {
                    str = i != 5 ? LocaleController.formatString("SlowmodeHours", 2131628361, 1) : LocaleController.formatString("SlowmodeMinutes", 2131628364, 15);
                } else {
                    str = LocaleController.formatString("SlowmodeMinutes", 2131628364, 5);
                }
                this.strings.add(str);
                this.sizes.add(Integer.valueOf((int) Math.ceil(this.textPaint.measureText(str))));
                i++;
            }
            this.accessibilityDelegate = new AnonymousClass1(r8);
        }

        /* renamed from: org.telegram.ui.ChatUsersActivity$ChooseView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends IntSeekBarAccessibilityDelegate {
            AnonymousClass1(ChatUsersActivity chatUsersActivity) {
                ChooseView.this = r1;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            public int getProgress() {
                return ChatUsersActivity.this.selectedSlowmode;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            public void setProgress(int i) {
                ChooseView.this.setItem(i);
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            public int getMaxValue() {
                return ChooseView.this.strings.size() - 1;
            }

            @Override // org.telegram.ui.Components.SeekBarAccessibilityDelegate
            protected CharSequence getContentDescription(View view) {
                if (ChatUsersActivity.this.selectedSlowmode == 0) {
                    return LocaleController.getString("SlowmodeOff", 2131628365);
                }
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                return chatUsersActivity.formatSeconds(chatUsersActivity.getSecondsForIndex(chatUsersActivity.selectedSlowmode));
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            this.accessibilityDelegate.onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
        }

        @Override // android.view.View
        public boolean performAccessibilityAction(int i, Bundle bundle) {
            return super.performAccessibilityAction(i, bundle) || this.accessibilityDelegate.performAccessibilityActionInternal(this, i, bundle);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float x = motionEvent.getX();
            int i = 0;
            boolean z = false;
            if (motionEvent.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
                int i2 = 0;
                while (true) {
                    if (i2 >= this.strings.size()) {
                        break;
                    }
                    int i3 = this.sideSide;
                    int i4 = this.lineSize + (this.gapSize * 2);
                    int i5 = this.circleSize;
                    int i6 = i3 + ((i4 + i5) * i2) + (i5 / 2);
                    if (x <= i6 - AndroidUtilities.dp(15.0f) || x >= i6 + AndroidUtilities.dp(15.0f)) {
                        i2++;
                    } else {
                        if (i2 == ChatUsersActivity.this.selectedSlowmode) {
                            z = true;
                        }
                        this.startMoving = z;
                        this.startX = x;
                        this.startMovingItem = ChatUsersActivity.this.selectedSlowmode;
                    }
                }
            } else if (motionEvent.getAction() == 2) {
                if (this.startMoving) {
                    if (Math.abs(this.startX - x) >= AndroidUtilities.getPixelsInCM(0.5f, true)) {
                        this.moving = true;
                        this.startMoving = false;
                    }
                } else if (this.moving) {
                    while (true) {
                        if (i >= this.strings.size()) {
                            break;
                        }
                        int i7 = this.sideSide;
                        int i8 = this.lineSize;
                        int i9 = this.gapSize;
                        int i10 = this.circleSize;
                        int i11 = i7 + (((i9 * 2) + i8 + i10) * i) + (i10 / 2);
                        int i12 = (i8 / 2) + (i10 / 2) + i9;
                        if (x <= i11 - i12 || x >= i11 + i12) {
                            i++;
                        } else if (ChatUsersActivity.this.selectedSlowmode != i) {
                            setItem(i);
                        }
                    }
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (!this.moving) {
                    int i13 = 0;
                    while (true) {
                        if (i13 >= this.strings.size()) {
                            break;
                        }
                        int i14 = this.sideSide;
                        int i15 = this.lineSize + (this.gapSize * 2);
                        int i16 = this.circleSize;
                        int i17 = i14 + ((i15 + i16) * i13) + (i16 / 2);
                        if (x <= i17 - AndroidUtilities.dp(15.0f) || x >= i17 + AndroidUtilities.dp(15.0f)) {
                            i13++;
                        } else if (ChatUsersActivity.this.selectedSlowmode != i13) {
                            setItem(i13);
                        }
                    }
                } else if (ChatUsersActivity.this.selectedSlowmode != this.startMovingItem) {
                    setItem(ChatUsersActivity.this.selectedSlowmode);
                }
                this.startMoving = false;
                this.moving = false;
            }
            return true;
        }

        public void setItem(int i) {
            if (ChatUsersActivity.this.info == null) {
                return;
            }
            ChatUsersActivity.this.selectedSlowmode = i;
            ChatUsersActivity.this.listViewAdapter.notifyItemChanged(ChatUsersActivity.this.slowmodeInfoRow);
            invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(74.0f), 1073741824));
            this.circleSize = AndroidUtilities.dp(6.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(22.0f);
            this.lineSize = (((getMeasuredWidth() - (this.circleSize * this.strings.size())) - ((this.gapSize * 2) * (this.strings.size() - 1))) - (this.sideSide * 2)) / (this.strings.size() - 1);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            int measuredHeight = (getMeasuredHeight() / 2) + AndroidUtilities.dp(11.0f);
            int i = 0;
            while (i < this.strings.size()) {
                int i2 = this.sideSide;
                int i3 = this.lineSize + (this.gapSize * 2);
                int i4 = this.circleSize;
                int i5 = i2 + ((i3 + i4) * i) + (i4 / 2);
                if (i <= ChatUsersActivity.this.selectedSlowmode) {
                    this.paint.setColor(Theme.getColor("switchTrackChecked"));
                } else {
                    this.paint.setColor(Theme.getColor("switchTrack"));
                }
                canvas.drawCircle(i5, measuredHeight, i == ChatUsersActivity.this.selectedSlowmode ? AndroidUtilities.dp(6.0f) : this.circleSize / 2, this.paint);
                if (i != 0) {
                    int i6 = (i5 - (this.circleSize / 2)) - this.gapSize;
                    int i7 = this.lineSize;
                    int i8 = i6 - i7;
                    if (i == ChatUsersActivity.this.selectedSlowmode || i == ChatUsersActivity.this.selectedSlowmode + 1) {
                        i7 -= AndroidUtilities.dp(3.0f);
                    }
                    if (i == ChatUsersActivity.this.selectedSlowmode + 1) {
                        i8 += AndroidUtilities.dp(3.0f);
                    }
                    canvas.drawRect(i8, measuredHeight - AndroidUtilities.dp(1.0f), i8 + i7, AndroidUtilities.dp(1.0f) + measuredHeight, this.paint);
                }
                int intValue = this.sizes.get(i).intValue();
                String str = this.strings.get(i);
                if (i == 0) {
                    canvas.drawText(str, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(28.0f), this.textPaint);
                } else if (i == this.strings.size() - 1) {
                    canvas.drawText(str, (getMeasuredWidth() - intValue) - AndroidUtilities.dp(22.0f), AndroidUtilities.dp(28.0f), this.textPaint);
                } else {
                    canvas.drawText(str, i5 - (intValue / 2), AndroidUtilities.dp(28.0f), this.textPaint);
                }
                i++;
            }
        }
    }

    public ChatUsersActivity(Bundle bundle) {
        super(bundle);
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        this.currentChat = chat;
        if (chat != null && (tLRPC$TL_chatBannedRights = chat.default_banned_rights) != null) {
            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = this.defaultBannedRights;
            tLRPC$TL_chatBannedRights2.view_messages = tLRPC$TL_chatBannedRights.view_messages;
            tLRPC$TL_chatBannedRights2.send_stickers = tLRPC$TL_chatBannedRights.send_stickers;
            tLRPC$TL_chatBannedRights2.send_media = tLRPC$TL_chatBannedRights.send_media;
            tLRPC$TL_chatBannedRights2.embed_links = tLRPC$TL_chatBannedRights.embed_links;
            tLRPC$TL_chatBannedRights2.send_messages = tLRPC$TL_chatBannedRights.send_messages;
            tLRPC$TL_chatBannedRights2.send_games = tLRPC$TL_chatBannedRights.send_games;
            tLRPC$TL_chatBannedRights2.send_inline = tLRPC$TL_chatBannedRights.send_inline;
            tLRPC$TL_chatBannedRights2.send_gifs = tLRPC$TL_chatBannedRights.send_gifs;
            tLRPC$TL_chatBannedRights2.pin_messages = tLRPC$TL_chatBannedRights.pin_messages;
            tLRPC$TL_chatBannedRights2.send_polls = tLRPC$TL_chatBannedRights.send_polls;
            tLRPC$TL_chatBannedRights2.invite_users = tLRPC$TL_chatBannedRights.invite_users;
            tLRPC$TL_chatBannedRights2.change_info = tLRPC$TL_chatBannedRights.change_info;
        }
        this.initialBannedRights = ChatObject.getBannedRightsString(this.defaultBannedRights);
        this.isChannel = ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup;
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x00fd, code lost:
        if (org.telegram.messenger.ChatObject.canBlockUsers(r0) != false) goto L31;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01a4  */
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
        this.addNewRow = -1;
        this.addNew2Row = -1;
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
        int i = 0;
        this.rowCount = 0;
        int i2 = this.type;
        int i3 = 1;
        if (i2 == 3) {
            int i4 = 0 + 1;
            this.rowCount = i4;
            this.permissionsSectionRow = 0;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.sendMessagesRow = i4;
            int i6 = i5 + 1;
            this.rowCount = i6;
            this.sendMediaRow = i5;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.sendStickersRow = i6;
            int i8 = i7 + 1;
            this.rowCount = i8;
            this.sendPollsRow = i7;
            int i9 = i8 + 1;
            this.rowCount = i9;
            this.embedLinksRow = i8;
            int i10 = i9 + 1;
            this.rowCount = i10;
            this.addUsersRow = i9;
            int i11 = i10 + 1;
            this.rowCount = i11;
            this.pinMessagesRow = i10;
            this.rowCount = i11 + 1;
            this.changeInfoRow = i11;
            if (ChatObject.isChannel(chat)) {
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                if (tLRPC$Chat.creator && tLRPC$Chat.megagroup && !tLRPC$Chat.gigagroup) {
                    int i12 = tLRPC$Chat.participants_count;
                    TLRPC$ChatFull tLRPC$ChatFull4 = this.info;
                    if (tLRPC$ChatFull4 != null) {
                        i = tLRPC$ChatFull4.participants_count;
                    }
                    if (Math.max(i12, i) >= getMessagesController().maxMegagroupCount - 1000) {
                        int i13 = this.rowCount;
                        int i14 = i13 + 1;
                        this.rowCount = i14;
                        this.participantsDivider2Row = i13;
                        int i15 = i14 + 1;
                        this.rowCount = i15;
                        this.gigaHeaderRow = i14;
                        int i16 = i15 + 1;
                        this.rowCount = i16;
                        this.gigaConvertRow = i15;
                        this.rowCount = i16 + 1;
                        this.gigaInfoRow = i16;
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
                        int i17 = this.rowCount;
                        this.rowCount = i17 + 1;
                        this.participantsDivider2Row = i17;
                    }
                    int i18 = this.rowCount;
                    this.rowCount = i18 + 1;
                    this.removedUsersRow = i18;
                }
                if ((this.slowmodeInfoRow == -1 && this.gigaHeaderRow == -1) || this.removedUsersRow != -1) {
                    int i19 = this.rowCount;
                    this.rowCount = i19 + 1;
                    this.participantsDividerRow = i19;
                }
                if (ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
                    int i20 = this.rowCount;
                    this.rowCount = i20 + 1;
                    this.addNewRow = i20;
                }
                if (!this.loadingUsers && !(z4 = this.firstLoaded)) {
                    if (z4 || (tLRPC$ChatFull3 = this.info) == null || tLRPC$ChatFull3.banned_count <= 0) {
                        return;
                    }
                    int i21 = this.rowCount;
                    this.rowCount = i21 + 1;
                    this.loadingUserCellRow = i21;
                    return;
                }
                if (!this.participants.isEmpty()) {
                    int i22 = this.rowCount;
                    this.participantsStartRow = i22;
                    int size = i22 + this.participants.size();
                    this.rowCount = size;
                    this.participantsEndRow = size;
                }
                if (this.addNewRow != -1 && this.participantsStartRow == -1) {
                    return;
                }
                int i23 = this.rowCount;
                this.rowCount = i23 + 1;
                this.addNewSectionRow = i23;
            }
            if (this.participantsDivider2Row == -1) {
                int i24 = this.rowCount;
                this.rowCount = i24 + 1;
                this.participantsDivider2Row = i24;
            }
            int i25 = this.rowCount;
            int i26 = i25 + 1;
            this.rowCount = i26;
            this.slowmodeRow = i25;
            int i27 = i26 + 1;
            this.rowCount = i27;
            this.slowmodeSelectRow = i26;
            this.rowCount = i27 + 1;
            this.slowmodeInfoRow = i27;
            if (ChatObject.isChannel(this.currentChat)) {
            }
            if (this.slowmodeInfoRow == -1) {
                int i192 = this.rowCount;
                this.rowCount = i192 + 1;
                this.participantsDividerRow = i192;
                if (ChatObject.canBlockUsers(this.currentChat)) {
                    int i202 = this.rowCount;
                    this.rowCount = i202 + 1;
                    this.addNewRow = i202;
                }
                if (!this.loadingUsers) {
                }
                if (!this.participants.isEmpty()) {
                }
                if (this.addNewRow != -1) {
                }
                int i232 = this.rowCount;
                this.rowCount = i232 + 1;
                this.addNewSectionRow = i232;
            }
            int i1922 = this.rowCount;
            this.rowCount = i1922 + 1;
            this.participantsDividerRow = i1922;
            if (ChatObject.canBlockUsers(this.currentChat)) {
            }
            if (!this.loadingUsers) {
            }
            if (!this.participants.isEmpty()) {
            }
            if (this.addNewRow != -1) {
            }
            int i2322 = this.rowCount;
            this.rowCount = i2322 + 1;
            this.addNewSectionRow = i2322;
        } else if (i2 == 0) {
            if (ChatObject.canBlockUsers(chat)) {
                int i28 = this.rowCount;
                this.rowCount = i28 + 1;
                this.addNewRow = i28;
                if (!this.participants.isEmpty() || (this.loadingUsers && !this.firstLoaded && (tLRPC$ChatFull2 = this.info) != null && tLRPC$ChatFull2.kicked_count > 0)) {
                    int i29 = this.rowCount;
                    this.rowCount = i29 + 1;
                    this.participantsInfoRow = i29;
                }
            }
            if (this.loadingUsers && !(z3 = this.firstLoaded)) {
                if (z3) {
                    return;
                }
                int i30 = this.rowCount;
                int i31 = i30 + 1;
                this.rowCount = i31;
                this.restricted1SectionRow = i30;
                this.rowCount = i31 + 1;
                this.loadingUserCellRow = i31;
                return;
            }
            if (!this.participants.isEmpty()) {
                int i32 = this.rowCount;
                int i33 = i32 + 1;
                this.rowCount = i33;
                this.restricted1SectionRow = i32;
                this.participantsStartRow = i33;
                int size2 = i33 + this.participants.size();
                this.rowCount = size2;
                this.participantsEndRow = size2;
            }
            if (this.participantsStartRow != -1) {
                if (this.participantsInfoRow == -1) {
                    int i34 = this.rowCount;
                    this.rowCount = i34 + 1;
                    this.participantsInfoRow = i34;
                    return;
                }
                int i35 = this.rowCount;
                this.rowCount = i35 + 1;
                this.addNewSectionRow = i35;
                return;
            }
            int i36 = this.rowCount;
            this.rowCount = i36 + 1;
            this.blockedEmptyRow = i36;
        } else if (i2 == 1) {
            if (ChatObject.isChannel(chat)) {
                TLRPC$Chat tLRPC$Chat3 = this.currentChat;
                if (tLRPC$Chat3.megagroup && !tLRPC$Chat3.gigagroup && ((tLRPC$ChatFull = this.info) == null || tLRPC$ChatFull.participants_count <= 200 || (!this.isChannel && tLRPC$ChatFull.can_set_stickers))) {
                    int i37 = this.rowCount;
                    int i38 = i37 + 1;
                    this.rowCount = i38;
                    this.recentActionsRow = i37;
                    this.rowCount = i38 + 1;
                    this.addNewSectionRow = i38;
                }
            }
            if (ChatObject.canAddAdmins(this.currentChat)) {
                int i39 = this.rowCount;
                this.rowCount = i39 + 1;
                this.addNewRow = i39;
            }
            if (this.loadingUsers && !(z2 = this.firstLoaded)) {
                if (z2) {
                    return;
                }
                int i40 = this.rowCount;
                this.rowCount = i40 + 1;
                this.loadingUserCellRow = i40;
                return;
            }
            if (!this.participants.isEmpty()) {
                int i41 = this.rowCount;
                this.participantsStartRow = i41;
                int size3 = i41 + this.participants.size();
                this.rowCount = size3;
                this.participantsEndRow = size3;
            }
            int i42 = this.rowCount;
            this.rowCount = i42 + 1;
            this.participantsInfoRow = i42;
        } else if (i2 != 2) {
        } else {
            if (this.selectType == 0 && ChatObject.canAddUsers(chat)) {
                int i43 = this.rowCount;
                this.rowCount = i43 + 1;
                this.addNewRow = i43;
            }
            if (this.selectType == 0 && ChatObject.canUserDoAdminAction(this.currentChat, 3)) {
                int i44 = this.rowCount;
                this.rowCount = i44 + 1;
                this.addNew2Row = i44;
            }
            if (this.loadingUsers && !(z = this.firstLoaded)) {
                if (z) {
                    return;
                }
                if (this.selectType == 0) {
                    int i45 = this.rowCount;
                    this.rowCount = i45 + 1;
                    this.loadingHeaderRow = i45;
                }
                int i46 = this.rowCount;
                this.rowCount = i46 + 1;
                this.loadingUserCellRow = i46;
                return;
            }
            if (!this.contacts.isEmpty()) {
                int i47 = this.rowCount;
                int i48 = i47 + 1;
                this.rowCount = i48;
                this.contactsHeaderRow = i47;
                this.contactsStartRow = i48;
                int size4 = i48 + this.contacts.size();
                this.rowCount = size4;
                this.contactsEndRow = size4;
                i = 1;
            }
            if (!this.bots.isEmpty()) {
                int i49 = this.rowCount;
                int i50 = i49 + 1;
                this.rowCount = i50;
                this.botHeaderRow = i49;
                this.botStartRow = i50;
                int size5 = i50 + this.bots.size();
                this.rowCount = size5;
                this.botEndRow = size5;
            } else {
                i3 = i;
            }
            if (!this.participants.isEmpty()) {
                if (i3 != 0) {
                    int i51 = this.rowCount;
                    this.rowCount = i51 + 1;
                    this.membersHeaderRow = i51;
                }
                int i52 = this.rowCount;
                this.participantsStartRow = i52;
                int size6 = i52 + this.participants.size();
                this.rowCount = size6;
                this.participantsEndRow = size6;
            }
            int i53 = this.rowCount;
            if (i53 == 0) {
                return;
            }
            this.rowCount = i53 + 1;
            this.participantsInfoRow = i53;
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
        this.actionBar.setBackButtonImage(2131165449);
        int i2 = 1;
        this.actionBar.setAllowOverlayTitle(true);
        int i3 = this.type;
        if (i3 == 3) {
            this.actionBar.setTitle(LocaleController.getString("ChannelPermissions", 2131624942));
        } else if (i3 == 0) {
            this.actionBar.setTitle(LocaleController.getString("ChannelBlacklist", 2131624882));
        } else if (i3 == 1) {
            this.actionBar.setTitle(LocaleController.getString("ChannelAdministrators", 2131624877));
        } else if (i3 == 2) {
            int i4 = this.selectType;
            if (i4 == 0) {
                if (this.isChannel) {
                    this.actionBar.setTitle(LocaleController.getString("ChannelSubscribers", 2131624968));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("ChannelMembers", 2131624917));
                }
            } else if (i4 == 1) {
                this.actionBar.setTitle(LocaleController.getString("ChannelAddAdmin", 2131624867));
            } else if (i4 == 2) {
                this.actionBar.setTitle(LocaleController.getString("ChannelBlockUser", 2131624883));
            } else if (i4 == 3) {
                this.actionBar.setTitle(LocaleController.getString("ChannelAddException", 2131624868));
            }
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        if (this.selectType != 0 || (i = this.type) == 2 || i == 0 || i == 3) {
            this.searchListViewAdapter = new SearchAdapter(context);
            ActionBarMenu createMenu = this.actionBar.createMenu();
            ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2());
            this.searchItem = actionBarMenuItemSearchListener;
            if (this.type == 0 && !this.firstLoaded) {
                actionBarMenuItemSearchListener.setVisibility(8);
            }
            if (this.type == 3) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("ChannelSearchException", 2131624956));
            } else {
                this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131628092));
            }
            if (!ChatObject.isChannel(this.currentChat) && !this.currentChat.creator) {
                this.searchItem.setVisibility(8);
            }
            if (this.type == 3) {
                this.doneItem = createMenu.addItemWithWidth(1, 2131165450, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131625525));
            }
        }
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.fragmentView = anonymousClass3;
        AnonymousClass3 anonymousClass32 = anonymousClass3;
        FrameLayout frameLayout = new FrameLayout(context);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.flickerLoadingView = flickerLoadingView;
        flickerLoadingView.setViewType(6);
        this.flickerLoadingView.showDate(false);
        this.flickerLoadingView.setUseHeaderOffset(true);
        frameLayout.addView(this.flickerLoadingView);
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressBar = radialProgressView;
        frameLayout.addView(radialProgressView, LayoutHelper.createFrame(-2, -2, 17));
        this.flickerLoadingView.setVisibility(8);
        this.progressBar.setVisibility(8);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, frameLayout, 1);
        this.emptyView = stickerEmptyView;
        stickerEmptyView.title.setText(LocaleController.getString("NoResult", 2131626858));
        this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628098));
        this.emptyView.setVisibility(8);
        this.emptyView.setAnimateLayoutChange(true);
        this.emptyView.showProgress(true, false);
        anonymousClass32.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.addView(frameLayout, 0);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
        this.listView = anonymousClass4;
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, 1, false);
        this.layoutManager = anonymousClass5;
        anonymousClass4.setLayoutManager(anonymousClass5);
        AnonymousClass6 anonymousClass6 = new AnonymousClass6();
        this.listView.setItemAnimator(anonymousClass6);
        anonymousClass6.setSupportsChangeAnimations(false);
        this.listView.setAnimateEmptyView(true, 0);
        RecyclerListView recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        RecyclerListView recyclerListView2 = this.listView;
        if (!LocaleController.isRTL) {
            i2 = 2;
        }
        recyclerListView2.setVerticalScrollbarPosition(i2);
        anonymousClass32.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new ChatUsersActivity$$ExternalSyntheticLambda17(this, context));
        this.listView.setOnItemLongClickListener(new ChatUsersActivity$$ExternalSyntheticLambda18(this));
        if (this.searchItem != null) {
            this.listView.setOnScrollListener(new AnonymousClass12());
        }
        UndoView undoView = new UndoView(context);
        this.undoView = undoView;
        anonymousClass32.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        updateRows();
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(false, 0);
        if (this.needOpenSearch) {
            this.searchItem.openSearch(false);
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChatUsersActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!ChatUsersActivity.this.checkDiscard()) {
                    return;
                }
                ChatUsersActivity.this.finishFragment();
            } else if (i != 1) {
            } else {
                ChatUsersActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass2() {
            ChatUsersActivity.this = r1;
        }

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
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ChatUsersActivity.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            canvas.drawColor(Theme.getColor(ChatUsersActivity.this.listView.getAdapter() == ChatUsersActivity.this.searchListViewAdapter ? "windowBackgroundWhite" : "windowBackgroundGray"));
            super.dispatchDraw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            ChatUsersActivity.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            if (((BaseFragment) ChatUsersActivity.this).fragmentView != null) {
                ((BaseFragment) ChatUsersActivity.this).fragmentView.invalidate();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends LinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, int i, boolean z) {
            super(context, i, z);
            ChatUsersActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (!ChatUsersActivity.this.firstLoaded && ChatUsersActivity.this.type == 0 && ChatUsersActivity.this.participants.size() == 0) {
                return 0;
            }
            return super.scrollVerticallyBy(i, recycler, state);
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends DefaultItemAnimator {
        int animationIndex = -1;

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getAddAnimationDelay(long j, long j2, long j3) {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getAddDuration() {
            return 220L;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getMoveAnimationDelay() {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getMoveDuration() {
            return 220L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getRemoveDuration() {
            return 220L;
        }

        AnonymousClass6() {
            ChatUsersActivity.this = r1;
        }

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
    }

    /* JADX WARN: Removed duplicated region for block: B:210:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0509 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x050a  */
    /* JADX WARN: Removed duplicated region for block: B:288:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        String str;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        boolean z;
        long j;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLObject tLObject;
        boolean z2;
        int i2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2;
        boolean z3;
        long peerId;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights5;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights6;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights7;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights8;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights9;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights10;
        boolean z4 = this.listView.getAdapter() == this.listViewAdapter;
        int i3 = 3;
        if (z4) {
            if (i == this.addNewRow) {
                int i4 = this.type;
                if (i4 == 0 || i4 == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", this.chatId);
                    bundle.putInt("type", 2);
                    if (this.type == 0) {
                        i3 = 2;
                    }
                    bundle.putInt("selectType", i3);
                    ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
                    chatUsersActivity.setInfo(this.info);
                    chatUsersActivity.setDelegate(new AnonymousClass7());
                    presentFragment(chatUsersActivity);
                    return;
                } else if (i4 == 1) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", this.chatId);
                    bundle2.putInt("type", 2);
                    bundle2.putInt("selectType", 1);
                    ChatUsersActivity chatUsersActivity2 = new ChatUsersActivity(bundle2);
                    chatUsersActivity2.setDelegate(new AnonymousClass8());
                    chatUsersActivity2.setInfo(this.info);
                    presentFragment(chatUsersActivity2);
                    return;
                } else if (i4 != 2) {
                    return;
                } else {
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("addToGroup", true);
                    bundle3.putLong(this.isChannel ? "channelId" : "chatId", this.currentChat.id);
                    GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle3);
                    groupCreateActivity.setInfo(this.info);
                    LongSparseArray<TLObject> longSparseArray = this.contactsMap;
                    groupCreateActivity.setIgnoreUsers((longSparseArray == null || longSparseArray.size() == 0) ? this.participantsMap : this.contactsMap);
                    groupCreateActivity.setDelegate(new AnonymousClass9(context));
                    presentFragment(groupCreateActivity);
                    return;
                }
            } else if (i == this.recentActionsRow) {
                presentFragment(new ChannelAdminLogActivity(this.currentChat));
                return;
            } else if (i == this.removedUsersRow) {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("chat_id", this.chatId);
                bundle4.putInt("type", 0);
                ChatUsersActivity chatUsersActivity3 = new ChatUsersActivity(bundle4);
                chatUsersActivity3.setInfo(this.info);
                presentFragment(chatUsersActivity3);
                return;
            } else if (i == this.gigaConvertRow) {
                showDialog(new AnonymousClass10(getParentActivity(), this));
            } else if (i == this.addNew2Row) {
                if (this.info == null) {
                    return;
                }
                ManageLinksActivity manageLinksActivity = new ManageLinksActivity(this.chatId, 0L, 0);
                TLRPC$ChatFull tLRPC$ChatFull = this.info;
                manageLinksActivity.setInfo(tLRPC$ChatFull, tLRPC$ChatFull.exported_invite);
                presentFragment(manageLinksActivity);
                return;
            } else if (i > this.permissionsSectionRow && i <= this.changeInfoRow) {
                TextCheckCell2 textCheckCell2 = (TextCheckCell2) view;
                if (!textCheckCell2.isEnabled()) {
                    return;
                }
                if (textCheckCell2.hasIcon()) {
                    if (!TextUtils.isEmpty(this.currentChat.username) && (i == this.pinMessagesRow || i == this.changeInfoRow)) {
                        BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("EditCantEditPermissionsPublic", 2131625571)).show();
                        return;
                    } else {
                        BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("EditCantEditPermissions", 2131625570)).show();
                        return;
                    }
                }
                textCheckCell2.setChecked(!textCheckCell2.isChecked());
                if (i == this.changeInfoRow) {
                    this.defaultBannedRights.change_info = !tLRPC$TL_chatBannedRights10.change_info;
                    return;
                } else if (i == this.addUsersRow) {
                    this.defaultBannedRights.invite_users = !tLRPC$TL_chatBannedRights9.invite_users;
                    return;
                } else if (i == this.pinMessagesRow) {
                    this.defaultBannedRights.pin_messages = !tLRPC$TL_chatBannedRights8.pin_messages;
                    return;
                } else {
                    boolean z5 = !textCheckCell2.isChecked();
                    int i5 = this.sendMessagesRow;
                    if (i == i5) {
                        this.defaultBannedRights.send_messages = !tLRPC$TL_chatBannedRights7.send_messages;
                    } else if (i == this.sendMediaRow) {
                        this.defaultBannedRights.send_media = !tLRPC$TL_chatBannedRights6.send_media;
                    } else if (i == this.sendStickersRow) {
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights11 = this.defaultBannedRights;
                        boolean z6 = !tLRPC$TL_chatBannedRights11.send_stickers;
                        tLRPC$TL_chatBannedRights11.send_inline = z6;
                        tLRPC$TL_chatBannedRights11.send_gifs = z6;
                        tLRPC$TL_chatBannedRights11.send_games = z6;
                        tLRPC$TL_chatBannedRights11.send_stickers = z6;
                    } else if (i == this.embedLinksRow) {
                        this.defaultBannedRights.embed_links = !tLRPC$TL_chatBannedRights5.embed_links;
                    } else if (i == this.sendPollsRow) {
                        this.defaultBannedRights.send_polls = !tLRPC$TL_chatBannedRights4.send_polls;
                    }
                    if (z5) {
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights12 = this.defaultBannedRights;
                        if (tLRPC$TL_chatBannedRights12.view_messages && !tLRPC$TL_chatBannedRights12.send_messages) {
                            tLRPC$TL_chatBannedRights12.send_messages = true;
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i5);
                            if (findViewHolderForAdapterPosition != null) {
                                ((TextCheckCell2) findViewHolderForAdapterPosition.itemView).setChecked(false);
                            }
                        }
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights13 = this.defaultBannedRights;
                        if ((tLRPC$TL_chatBannedRights13.view_messages || tLRPC$TL_chatBannedRights13.send_messages) && !tLRPC$TL_chatBannedRights13.send_media) {
                            tLRPC$TL_chatBannedRights13.send_media = true;
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.sendMediaRow);
                            if (findViewHolderForAdapterPosition2 != null) {
                                ((TextCheckCell2) findViewHolderForAdapterPosition2.itemView).setChecked(false);
                            }
                        }
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights14 = this.defaultBannedRights;
                        if ((tLRPC$TL_chatBannedRights14.view_messages || tLRPC$TL_chatBannedRights14.send_messages) && !tLRPC$TL_chatBannedRights14.send_polls) {
                            tLRPC$TL_chatBannedRights14.send_polls = true;
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition3 = this.listView.findViewHolderForAdapterPosition(this.sendPollsRow);
                            if (findViewHolderForAdapterPosition3 != null) {
                                ((TextCheckCell2) findViewHolderForAdapterPosition3.itemView).setChecked(false);
                            }
                        }
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights15 = this.defaultBannedRights;
                        if ((tLRPC$TL_chatBannedRights15.view_messages || tLRPC$TL_chatBannedRights15.send_messages) && !tLRPC$TL_chatBannedRights15.send_stickers) {
                            tLRPC$TL_chatBannedRights15.send_inline = true;
                            tLRPC$TL_chatBannedRights15.send_gifs = true;
                            tLRPC$TL_chatBannedRights15.send_games = true;
                            tLRPC$TL_chatBannedRights15.send_stickers = true;
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition4 = this.listView.findViewHolderForAdapterPosition(this.sendStickersRow);
                            if (findViewHolderForAdapterPosition4 != null) {
                                ((TextCheckCell2) findViewHolderForAdapterPosition4.itemView).setChecked(false);
                            }
                        }
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights16 = this.defaultBannedRights;
                        if ((!tLRPC$TL_chatBannedRights16.view_messages && !tLRPC$TL_chatBannedRights16.send_messages) || tLRPC$TL_chatBannedRights16.embed_links) {
                            return;
                        }
                        tLRPC$TL_chatBannedRights16.embed_links = true;
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition5 = this.listView.findViewHolderForAdapterPosition(this.embedLinksRow);
                        if (findViewHolderForAdapterPosition5 == null) {
                            return;
                        }
                        ((TextCheckCell2) findViewHolderForAdapterPosition5.itemView).setChecked(false);
                        return;
                    }
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights17 = this.defaultBannedRights;
                    if ((tLRPC$TL_chatBannedRights17.embed_links && tLRPC$TL_chatBannedRights17.send_inline && tLRPC$TL_chatBannedRights17.send_media && tLRPC$TL_chatBannedRights17.send_polls) || !tLRPC$TL_chatBannedRights17.send_messages) {
                        return;
                    }
                    tLRPC$TL_chatBannedRights17.send_messages = false;
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition6 = this.listView.findViewHolderForAdapterPosition(i5);
                    if (findViewHolderForAdapterPosition6 == null) {
                        return;
                    }
                    ((TextCheckCell2) findViewHolderForAdapterPosition6.itemView).setChecked(true);
                    return;
                }
            }
        }
        if (z4) {
            TLObject item = this.listViewAdapter.getItem(i);
            if (item instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) item;
                peerId = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                tLRPC$TL_chatBannedRights3 = tLRPC$ChannelParticipant.banned_rights;
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3 = tLRPC$ChannelParticipant.admin_rights;
                String str2 = tLRPC$ChannelParticipant.rank;
                boolean z7 = (!(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) && !(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant.can_edit;
                if ((item instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$TL_chatAdminRights3 = ((TLRPC$TL_channelParticipantCreator) item).admin_rights) == null) {
                    tLRPC$TL_chatAdminRights3 = new TLRPC$TL_chatAdminRights();
                    tLRPC$TL_chatAdminRights3.add_admins = true;
                    tLRPC$TL_chatAdminRights3.pin_messages = true;
                    tLRPC$TL_chatAdminRights3.invite_users = true;
                    tLRPC$TL_chatAdminRights3.ban_users = true;
                    tLRPC$TL_chatAdminRights3.delete_messages = true;
                    tLRPC$TL_chatAdminRights3.edit_messages = true;
                    tLRPC$TL_chatAdminRights3.post_messages = true;
                    tLRPC$TL_chatAdminRights3.change_info = true;
                    if (!this.isChannel) {
                        tLRPC$TL_chatAdminRights3.manage_call = true;
                    }
                }
                tLObject = item;
                tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights3;
                str = str2;
                z = z7;
                long j2 = peerId;
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights3;
                j = j2;
                if (j == 0) {
                    return;
                }
                int i6 = this.selectType;
                if (i6 != 0) {
                    if (i6 == 3 || i6 == 1) {
                        if (i6 != 1 && z && ((tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin))) {
                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
                            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString("AppName", 2131624375));
                            builder.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131624306, UserObject.getUserName(user)));
                            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ChatUsersActivity$$ExternalSyntheticLambda4(this, user, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z));
                            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                            showDialog(builder.create());
                            return;
                        }
                        openRightsEdit(j, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, i6 == 1 ? 0 : 1, i6 == 1 || i6 == 3);
                        return;
                    }
                    removeParticipant(j);
                    return;
                }
                int i7 = this.type;
                if (i7 == 1) {
                    z3 = j != getUserConfig().getClientUserId() && (this.currentChat.creator || z);
                } else if (i7 == 0 || i7 == 3) {
                    z3 = ChatObject.canBlockUsers(this.currentChat);
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
                    if (tLRPC$TL_chatBannedRights == null) {
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights18 = new TLRPC$TL_chatBannedRights();
                        tLRPC$TL_chatBannedRights18.view_messages = true;
                        tLRPC$TL_chatBannedRights18.send_stickers = true;
                        tLRPC$TL_chatBannedRights18.send_media = true;
                        tLRPC$TL_chatBannedRights18.embed_links = true;
                        tLRPC$TL_chatBannedRights18.send_messages = true;
                        tLRPC$TL_chatBannedRights18.send_games = true;
                        tLRPC$TL_chatBannedRights18.send_inline = true;
                        tLRPC$TL_chatBannedRights18.send_gifs = true;
                        tLRPC$TL_chatBannedRights18.pin_messages = true;
                        tLRPC$TL_chatBannedRights18.send_polls = true;
                        tLRPC$TL_chatBannedRights18.invite_users = true;
                        tLRPC$TL_chatBannedRights18.change_info = true;
                        tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights18;
                    } else {
                        tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights;
                    }
                    ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, this.defaultBannedRights, tLRPC$TL_chatBannedRights2, str, this.type == 1 ? 0 : 1, z2, tLObject == null, null);
                    chatRightsEditActivity.setDelegate(new AnonymousClass11(tLObject));
                    presentFragment(chatRightsEditActivity);
                    return;
                }
                z2 = z3;
                i2 = this.type;
                if (i2 != 0) {
                }
                if (j != getUserConfig().getClientUserId()) {
                }
            } else if (item instanceof TLRPC$ChatParticipant) {
                j = ((TLRPC$ChatParticipant) item).user_id;
                boolean z8 = this.currentChat.creator;
                if (item instanceof TLRPC$TL_chatParticipantCreator) {
                    tLRPC$TL_chatAdminRights2 = new TLRPC$TL_chatAdminRights();
                    tLRPC$TL_chatAdminRights2.add_admins = true;
                    tLRPC$TL_chatAdminRights2.pin_messages = true;
                    tLRPC$TL_chatAdminRights2.invite_users = true;
                    tLRPC$TL_chatAdminRights2.ban_users = true;
                    tLRPC$TL_chatAdminRights2.delete_messages = true;
                    tLRPC$TL_chatAdminRights2.edit_messages = true;
                    tLRPC$TL_chatAdminRights2.post_messages = true;
                    tLRPC$TL_chatAdminRights2.change_info = true;
                    if (!this.isChannel) {
                        tLRPC$TL_chatAdminRights2.manage_call = true;
                    }
                } else {
                    tLRPC$TL_chatAdminRights2 = null;
                }
                z = z8;
                tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights2;
                str = "";
                tLObject = item;
                tLRPC$TL_chatBannedRights = null;
                if (j == 0) {
                }
            } else {
                tLObject = item;
                j = 0;
                tLRPC$TL_chatAdminRights = null;
                str = "";
                z = false;
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatAdminRights;
                if (j == 0) {
                }
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
                boolean z9 = (!(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantAdmin) && !(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) || tLRPC$ChannelParticipant2.can_edit;
                tLRPC$TL_chatBannedRights3 = tLRPC$ChannelParticipant2.banned_rights;
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights4 = tLRPC$ChannelParticipant2.admin_rights;
                str = tLRPC$ChannelParticipant2.rank;
                tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights4;
                z = z9;
                tLObject = item2;
                long j22 = peerId;
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights3;
                j = j22;
                if (j == 0) {
                }
            } else {
                if (item2 instanceof TLRPC$ChatParticipant) {
                    j = ((TLRPC$ChatParticipant) item2).user_id;
                    tLObject = item2;
                    z = this.currentChat.creator;
                    tLRPC$TL_chatAdminRights = null;
                    str = "";
                } else {
                    tLObject = item2;
                    tLRPC$TL_chatAdminRights = null;
                    str = "";
                    if (item2 == null) {
                        z = true;
                    }
                    z = false;
                }
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatAdminRights;
                if (j == 0) {
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements ChatUsersActivityDelegate {
        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public /* synthetic */ void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivityDelegate.CC.$default$didChangeOwner(this, tLRPC$User);
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public /* synthetic */ void didSelectUser(long j) {
            ChatUsersActivityDelegate.CC.$default$didSelectUser(this, j);
        }

        AnonymousClass7() {
            ChatUsersActivity.this = r1;
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public void didAddParticipantToList(long j, TLObject tLObject) {
            if (ChatUsersActivity.this.participantsMap.get(j) == null) {
                DiffCallback saveState = ChatUsersActivity.this.saveState();
                ChatUsersActivity.this.participants.add(tLObject);
                ChatUsersActivity.this.participantsMap.put(j, tLObject);
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                chatUsersActivity.sortUsers(chatUsersActivity.participants);
                ChatUsersActivity.this.updateListAnimated(saveState);
            }
        }

        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public void didKickParticipant(long j) {
            if (ChatUsersActivity.this.participantsMap.get(j) == null) {
                DiffCallback saveState = ChatUsersActivity.this.saveState();
                TLRPC$TL_channelParticipantBanned tLRPC$TL_channelParticipantBanned = new TLRPC$TL_channelParticipantBanned();
                if (j > 0) {
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_channelParticipantBanned.peer = tLRPC$TL_peerUser;
                    tLRPC$TL_peerUser.user_id = j;
                } else {
                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                    tLRPC$TL_channelParticipantBanned.peer = tLRPC$TL_peerChannel;
                    tLRPC$TL_peerChannel.channel_id = -j;
                }
                tLRPC$TL_channelParticipantBanned.date = ChatUsersActivity.this.getConnectionsManager().getCurrentTime();
                tLRPC$TL_channelParticipantBanned.kicked_by = ChatUsersActivity.this.getAccountInstance().getUserConfig().clientUserId;
                ChatUsersActivity.this.info.kicked_count++;
                ChatUsersActivity.this.participants.add(tLRPC$TL_channelParticipantBanned);
                ChatUsersActivity.this.participantsMap.put(j, tLRPC$TL_channelParticipantBanned);
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                chatUsersActivity.sortUsers(chatUsersActivity.participants);
                ChatUsersActivity.this.updateListAnimated(saveState);
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements ChatUsersActivityDelegate {
        @Override // org.telegram.ui.ChatUsersActivity.ChatUsersActivityDelegate
        public /* synthetic */ void didKickParticipant(long j) {
            ChatUsersActivityDelegate.CC.$default$didKickParticipant(this, j);
        }

        AnonymousClass8() {
            ChatUsersActivity.this = r1;
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
            TLRPC$User user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j));
            if (user != null) {
                AndroidUtilities.runOnUIThread(new ChatUsersActivity$8$$ExternalSyntheticLambda0(this, user), 200L);
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

        public /* synthetic */ void lambda$didSelectUser$0(TLRPC$User tLRPC$User) {
            if (BulletinFactory.canShowBulletin(ChatUsersActivity.this)) {
                BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, tLRPC$User.first_name).show();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 implements GroupCreateActivity.ContactsAddActivityDelegate {
        final /* synthetic */ Context val$context;

        AnonymousClass9(Context context) {
            ChatUsersActivity.this = r1;
            this.val$context = context;
        }

        @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
        public void didSelectUsers(ArrayList<TLRPC$User> arrayList, int i) {
            int size = arrayList.size();
            ArrayList arrayList2 = new ArrayList();
            int[] iArr = {0};
            ChatUsersActivity$9$$ExternalSyntheticLambda0 chatUsersActivity$9$$ExternalSyntheticLambda0 = new ChatUsersActivity$9$$ExternalSyntheticLambda0(arrayList2, size, this.val$context);
            for (int i2 = 0; i2 < size; i2++) {
                TLRPC$User tLRPC$User = arrayList.get(i2);
                ChatUsersActivity.this.getMessagesController().addUserToChat(ChatUsersActivity.this.chatId, tLRPC$User, i, null, ChatUsersActivity.this, false, new ChatUsersActivity$9$$ExternalSyntheticLambda1(this, iArr, size, arrayList2, chatUsersActivity$9$$ExternalSyntheticLambda0, tLRPC$User), new ChatUsersActivity$9$$ExternalSyntheticLambda2(iArr, arrayList2, tLRPC$User, size, chatUsersActivity$9$$ExternalSyntheticLambda0));
                ChatUsersActivity.this.getMessagesController().putUser(tLRPC$User, false);
            }
        }

        public static /* synthetic */ void lambda$didSelectUsers$0(ArrayList arrayList, int i, Context context) {
            String str;
            CharSequence charSequence;
            if (arrayList.size() == 1) {
                if (i > 1) {
                    str = LocaleController.getString("InviteToGroupErrorTitleAUser", 2131626286);
                } else {
                    str = LocaleController.getString("InviteToGroupErrorTitleThisUser", 2131626289);
                }
                charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("InviteToGroupErrorMessageSingle", 2131626285, UserObject.getFirstName((TLRPC$User) arrayList.get(0))));
            } else if (arrayList.size() == 2) {
                str = LocaleController.getString("InviteToGroupErrorTitleSomeUsers", 2131626287);
                charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("InviteToGroupErrorMessageDouble", 2131626282, UserObject.getFirstName((TLRPC$User) arrayList.get(0)), UserObject.getFirstName((TLRPC$User) arrayList.get(1))));
            } else if (arrayList.size() == i) {
                str = LocaleController.getString("InviteToGroupErrorTitleTheseUsers", 2131626288);
                charSequence = LocaleController.getString("InviteToGroupErrorMessageMultipleAll", 2131626283);
            } else {
                str = LocaleController.getString("InviteToGroupErrorTitleSomeUsers", 2131626287);
                charSequence = LocaleController.getString("InviteToGroupErrorMessageMultipleSome", 2131626284);
            }
            new AlertDialog.Builder(context).setTitle(str).setMessage(charSequence).setPositiveButton(LocaleController.getString("OK", 2131627075), null).show();
        }

        public /* synthetic */ void lambda$didSelectUsers$1(int[] iArr, int i, ArrayList arrayList, Runnable runnable, TLRPC$User tLRPC$User) {
            iArr[0] = iArr[0] + 1;
            if (iArr[0] >= i && arrayList.size() > 0) {
                runnable.run();
            }
            DiffCallback saveState = ChatUsersActivity.this.saveState();
            ArrayList arrayList2 = (ChatUsersActivity.this.contactsMap == null || ChatUsersActivity.this.contactsMap.size() == 0) ? ChatUsersActivity.this.participants : ChatUsersActivity.this.contacts;
            LongSparseArray longSparseArray = (ChatUsersActivity.this.contactsMap == null || ChatUsersActivity.this.contactsMap.size() == 0) ? ChatUsersActivity.this.participantsMap : ChatUsersActivity.this.contactsMap;
            if (longSparseArray.get(tLRPC$User.id) == null) {
                if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                    TLRPC$TL_channelParticipant tLRPC$TL_channelParticipant = new TLRPC$TL_channelParticipant();
                    tLRPC$TL_channelParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_channelParticipant.peer = tLRPC$TL_peerUser;
                    tLRPC$TL_peerUser.user_id = tLRPC$User.id;
                    tLRPC$TL_channelParticipant.date = ChatUsersActivity.this.getConnectionsManager().getCurrentTime();
                    arrayList2.add(0, tLRPC$TL_channelParticipant);
                    longSparseArray.put(tLRPC$User.id, tLRPC$TL_channelParticipant);
                } else {
                    TLRPC$TL_chatParticipant tLRPC$TL_chatParticipant = new TLRPC$TL_chatParticipant();
                    tLRPC$TL_chatParticipant.user_id = tLRPC$User.id;
                    tLRPC$TL_chatParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                    arrayList2.add(0, tLRPC$TL_chatParticipant);
                    longSparseArray.put(tLRPC$User.id, tLRPC$TL_chatParticipant);
                }
            }
            if (arrayList2 == ChatUsersActivity.this.participants) {
                ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                chatUsersActivity.sortAdmins(chatUsersActivity.participants);
            }
            ChatUsersActivity.this.updateListAnimated(saveState);
        }

        public static /* synthetic */ boolean lambda$didSelectUsers$2(int[] iArr, ArrayList arrayList, TLRPC$User tLRPC$User, int i, Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
            iArr[0] = iArr[0] + 1;
            boolean z = tLRPC$TL_error != null && "USER_PRIVACY_RESTRICTED".equals(tLRPC$TL_error.text);
            if (z) {
                arrayList.add(tLRPC$User);
            }
            if (iArr[0] >= i && arrayList.size() > 0) {
                runnable.run();
            }
            return !z;
        }

        @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
        public void needAddBot(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.openRightsEdit(tLRPC$User.id, null, null, null, "", true, 0, false);
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends GigagroupConvertAlert {
        @Override // org.telegram.ui.Components.GigagroupConvertAlert
        protected void onCancel() {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context, BaseFragment baseFragment) {
            super(context, baseFragment);
            ChatUsersActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.GigagroupConvertAlert
        protected void onCovert() {
            ChatUsersActivity.this.getMessagesController().convertToGigaGroup(ChatUsersActivity.this.getParentActivity(), ChatUsersActivity.this.currentChat, ChatUsersActivity.this, new ChatUsersActivity$10$$ExternalSyntheticLambda0(this));
        }

        public /* synthetic */ void lambda$onCovert$0(boolean z) {
            if (!z || ((BaseFragment) ChatUsersActivity.this).parentLayout == null) {
                return;
            }
            BaseFragment baseFragment = ((BaseFragment) ChatUsersActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) ChatUsersActivity.this).parentLayout.fragmentsStack.size() - 2);
            if (baseFragment instanceof ChatEditActivity) {
                baseFragment.removeSelfFromStack();
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", ChatUsersActivity.this.chatId);
                ChatEditActivity chatEditActivity = new ChatEditActivity(bundle);
                chatEditActivity.setInfo(ChatUsersActivity.this.info);
                ((BaseFragment) ChatUsersActivity.this).parentLayout.addFragmentToStack(chatEditActivity, ((BaseFragment) ChatUsersActivity.this).parentLayout.fragmentsStack.size() - 1);
                ChatUsersActivity.this.finishFragment();
                chatEditActivity.showConvertTooltip();
                return;
            }
            ChatUsersActivity.this.finishFragment();
        }
    }

    public /* synthetic */ void lambda$createView$0(TLRPC$User tLRPC$User, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, DialogInterface dialogInterface, int i) {
        openRightsEdit(tLRPC$User.id, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, this.selectType == 1 ? 0 : 1, false);
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ TLObject val$participant;

        AnonymousClass11(TLObject tLObject) {
            ChatUsersActivity.this = r1;
            this.val$participant = tLObject;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            TLObject tLObject = this.val$participant;
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                tLRPC$ChannelParticipant.rank = str;
                ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, 0L, false);
            }
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }
    }

    public /* synthetic */ boolean lambda$createView$2(View view, int i) {
        if (getParentActivity() != null) {
            RecyclerView.Adapter adapter = this.listView.getAdapter();
            ListAdapter listAdapter = this.listViewAdapter;
            return adapter == listAdapter && createMenuForParticipant(listAdapter.getItem(i), false);
        }
        return false;
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$12 */
    /* loaded from: classes3.dex */
    class AnonymousClass12 extends RecyclerView.OnScrollListener {
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        }

        AnonymousClass12() {
            ChatUsersActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(ChatUsersActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    public void sortAdmins(ArrayList<TLObject> arrayList) {
        Collections.sort(arrayList, new ChatUsersActivity$$ExternalSyntheticLambda11(this));
    }

    public /* synthetic */ int lambda$sortAdmins$3(TLObject tLObject, TLObject tLObject2) {
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

    public void showItemsAnimated(int i) {
        if (this.isPaused || !this.openTransitionStarted) {
            return;
        }
        if (this.listView.getAdapter() == this.listViewAdapter && this.firstLoaded) {
            return;
        }
        View view = null;
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
        this.listView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass13(view, i));
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$finalFrom;
        final /* synthetic */ View val$finalProgressView;

        AnonymousClass13(View view, int i) {
            ChatUsersActivity.this = r1;
            this.val$finalProgressView = view;
            this.val$finalFrom = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ChatUsersActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            int childCount = ChatUsersActivity.this.listView.getChildCount();
            AnimatorSet animatorSet = new AnimatorSet();
            for (int i = 0; i < childCount; i++) {
                View childAt = ChatUsersActivity.this.listView.getChildAt(i);
                if (childAt != this.val$finalProgressView && ChatUsersActivity.this.listView.getChildAdapterPosition(childAt) >= this.val$finalFrom) {
                    childAt.setAlpha(0.0f);
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt, View.ALPHA, 0.0f, 1.0f);
                    ofFloat.setStartDelay((int) ((Math.min(ChatUsersActivity.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / ChatUsersActivity.this.listView.getMeasuredHeight()) * 100.0f));
                    ofFloat.setDuration(200L);
                    animatorSet.playTogether(ofFloat);
                }
            }
            View view = this.val$finalProgressView;
            if (view != null && view.getParent() == null) {
                ChatUsersActivity.this.listView.addView(this.val$finalProgressView);
                RecyclerView.LayoutManager layoutManager = ChatUsersActivity.this.listView.getLayoutManager();
                if (layoutManager != null) {
                    layoutManager.ignoreView(this.val$finalProgressView);
                    View view2 = this.val$finalProgressView;
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.ALPHA, view2.getAlpha(), 0.0f);
                    ofFloat2.addListener(new AnonymousClass1(layoutManager));
                    ofFloat2.start();
                }
            }
            animatorSet.start();
            return true;
        }

        /* renamed from: org.telegram.ui.ChatUsersActivity$13$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ RecyclerView.LayoutManager val$layoutManager;

            AnonymousClass1(RecyclerView.LayoutManager layoutManager) {
                AnonymousClass13.this = r1;
                this.val$layoutManager = layoutManager;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AnonymousClass13.this.val$finalProgressView.setAlpha(1.0f);
                this.val$layoutManager.stopIgnoringView(AnonymousClass13.this.val$finalProgressView);
                ChatUsersActivity.this.listView.removeView(AnonymousClass13.this.val$finalProgressView);
            }
        }
    }

    public void onOwnerChaged(TLRPC$User tLRPC$User) {
        ArrayList<TLObject> arrayList;
        LongSparseArray<TLObject> longSparseArray;
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
                Collections.sort(arrayList, new ChatUsersActivity$$ExternalSyntheticLambda10(this));
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

    public /* synthetic */ int lambda$onOwnerChaged$4(TLObject tLObject, TLObject tLObject2) {
        int channelAdminParticipantType = getChannelAdminParticipantType(tLObject);
        int channelAdminParticipantType2 = getChannelAdminParticipantType(tLObject2);
        if (channelAdminParticipantType > channelAdminParticipantType2) {
            return 1;
        }
        return channelAdminParticipantType < channelAdminParticipantType2 ? -1 : 0;
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends ChatRightsEditActivity {
        final /* synthetic */ boolean[] val$needShowBulletin;
        final /* synthetic */ long val$peerId;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass14(long j, long j2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2, String str, int i, boolean z, boolean z2, String str2, boolean[] zArr, long j3) {
            super(j, j2, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, tLRPC$TL_chatBannedRights2, str, i, z, z2, str2);
            ChatUsersActivity.this = r15;
            this.val$needShowBulletin = zArr;
            this.val$peerId = j3;
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public void onTransitionAnimationEnd(boolean z, boolean z2) {
            if (z || !z2 || !this.val$needShowBulletin[0] || !BulletinFactory.canShowBulletin(ChatUsersActivity.this)) {
                return;
            }
            if (this.val$peerId > 0) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.val$peerId));
                if (user == null) {
                    return;
                }
                BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, user.first_name).show();
                return;
            }
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.val$peerId));
            if (chat == null) {
                return;
            }
            BulletinFactory.createPromoteToAdminBulletin(ChatUsersActivity.this, chat.title).show();
        }
    }

    private void openRightsEdit2(long j, int i, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, int i2, boolean z2) {
        boolean[] zArr = new boolean[1];
        boolean z3 = (tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin);
        AnonymousClass14 anonymousClass14 = new AnonymousClass14(j, this.chatId, tLRPC$TL_chatAdminRights, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, i2, true, false, null, zArr, j);
        anonymousClass14.setDelegate(new AnonymousClass15(i2, j, i, z3, zArr));
        presentFragment(anonymousClass14);
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ int val$date;
        final /* synthetic */ boolean val$isAdmin;
        final /* synthetic */ boolean[] val$needShowBulletin;
        final /* synthetic */ long val$peerId;
        final /* synthetic */ int val$type;

        AnonymousClass15(int i, long j, int i2, boolean z, boolean[] zArr) {
            ChatUsersActivity.this = r1;
            this.val$type = i;
            this.val$peerId = j;
            this.val$date = i2;
            this.val$isAdmin = z;
            this.val$needShowBulletin = zArr;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            TLRPC$ChatParticipant tLRPC$ChatParticipant;
            TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
            int i2 = this.val$type;
            if (i2 != 0) {
                if (i2 != 1 || i != 0) {
                    return;
                }
                ChatUsersActivity.this.removeParticipants(this.val$peerId);
                return;
            }
            int i3 = 0;
            while (true) {
                if (i3 >= ChatUsersActivity.this.participants.size()) {
                    break;
                }
                TLObject tLObject = (TLObject) ChatUsersActivity.this.participants.get(i3);
                if (tLObject instanceof TLRPC$ChannelParticipant) {
                    if (MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer) == this.val$peerId) {
                        if (i == 1) {
                            tLRPC$ChannelParticipant = new TLRPC$TL_channelParticipantAdmin();
                        } else {
                            tLRPC$ChannelParticipant = new TLRPC$TL_channelParticipant();
                        }
                        tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                        tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                        tLRPC$ChannelParticipant.inviter_id = ChatUsersActivity.this.getUserConfig().getClientUserId();
                        if (this.val$peerId > 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            tLRPC$ChannelParticipant.peer = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = this.val$peerId;
                        } else {
                            TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                            tLRPC$ChannelParticipant.peer = tLRPC$TL_peerChannel;
                            tLRPC$TL_peerChannel.channel_id = -this.val$peerId;
                        }
                        tLRPC$ChannelParticipant.date = this.val$date;
                        tLRPC$ChannelParticipant.flags |= 4;
                        tLRPC$ChannelParticipant.rank = str;
                        ChatUsersActivity.this.participants.set(i3, tLRPC$ChannelParticipant);
                    }
                } else if (tLObject instanceof TLRPC$ChatParticipant) {
                    TLRPC$ChatParticipant tLRPC$ChatParticipant2 = (TLRPC$ChatParticipant) tLObject;
                    if (i == 1) {
                        tLRPC$ChatParticipant = new TLRPC$TL_chatParticipantAdmin();
                    } else {
                        tLRPC$ChatParticipant = new TLRPC$TL_chatParticipant();
                    }
                    tLRPC$ChatParticipant.user_id = tLRPC$ChatParticipant2.user_id;
                    tLRPC$ChatParticipant.date = tLRPC$ChatParticipant2.date;
                    tLRPC$ChatParticipant.inviter_id = tLRPC$ChatParticipant2.inviter_id;
                    int indexOf = ChatUsersActivity.this.info.participants.participants.indexOf(tLRPC$ChatParticipant2);
                    if (indexOf >= 0) {
                        ChatUsersActivity.this.info.participants.participants.set(indexOf, tLRPC$ChatParticipant);
                    }
                    ChatUsersActivity.this.loadChatParticipants(0, 200);
                }
                i3++;
            }
            if (i != 1 || this.val$isAdmin) {
                return;
            }
            this.val$needShowBulletin[0] = true;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        return checkDiscard();
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ TLObject val$participant;
        final /* synthetic */ boolean val$removeFragment;
        final /* synthetic */ long val$user_id;

        AnonymousClass16(TLObject tLObject, long j, boolean z) {
            ChatUsersActivity.this = r1;
            this.val$participant = tLObject;
            this.val$user_id = j;
            this.val$removeFragment = z;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            TLObject tLObject = this.val$participant;
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                tLRPC$ChannelParticipant.rank = str;
            }
            if (ChatUsersActivity.this.delegate == null || i != 1) {
                if (ChatUsersActivity.this.delegate != null) {
                    ChatUsersActivity.this.delegate.didAddParticipantToList(this.val$user_id, this.val$participant);
                }
            } else {
                ChatUsersActivity.this.delegate.didSelectUser(this.val$user_id);
            }
            if (this.val$removeFragment) {
                ChatUsersActivity.this.removeSelfFromStack();
            }
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }
    }

    public void openRightsEdit(long j, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, int i, boolean z2) {
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, i, z, tLObject == null, null);
        chatRightsEditActivity.setDelegate(new AnonymousClass16(tLObject, j, z2));
        presentFragment(chatRightsEditActivity, z2);
    }

    private void removeParticipant(long j) {
        if (!ChatObject.isChannel(this.currentChat)) {
            return;
        }
        getMessagesController().deleteParticipantFromChat(this.chatId, getMessagesController().getUser(Long.valueOf(j)), null);
        ChatUsersActivityDelegate chatUsersActivityDelegate = this.delegate;
        if (chatUsersActivityDelegate != null) {
            chatUsersActivityDelegate.didKickParticipant(j);
        }
        finishFragment();
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
        } else if (!(tLObject instanceof TLRPC$ChannelParticipant)) {
        } else {
            removeParticipants(MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer));
        }
    }

    public void removeParticipants(long j) {
        ArrayList<TLObject> arrayList;
        LongSparseArray<TLObject> longSparseArray;
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

    /* JADX WARN: Removed duplicated region for block: B:126:0x02a3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean createMenuForParticipant(TLObject tLObject, boolean z) {
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        String str;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        int i;
        boolean z2;
        long j;
        int[] iArr;
        CharSequence[] charSequenceArr;
        boolean z3;
        String str2;
        char c;
        String str3;
        int i2;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
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
            int i3 = tLRPC$ChatParticipant.date;
            z2 = ChatObject.canAddAdmins(this.currentChat);
            str = "";
            tLRPC$TL_chatAdminRights = null;
            tLRPC$TL_chatBannedRights = null;
            i = i3;
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
        int i4 = this.type;
        if (i4 == 2) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
            boolean z5 = ChatObject.canAddAdmins(this.currentChat) && ((tLObject instanceof TLRPC$TL_channelParticipant) || (tLObject instanceof TLRPC$TL_channelParticipantBanned) || (tLObject instanceof TLRPC$TL_chatParticipant) || z2);
            boolean z6 = tLObject instanceof TLRPC$TL_channelParticipantAdmin;
            boolean z7 = (!z6 && !(tLObject instanceof TLRPC$TL_channelParticipantCreator) && !(tLObject instanceof TLRPC$TL_chatParticipantCreator) && !(tLObject instanceof TLRPC$TL_chatParticipantAdmin)) || z2;
            boolean z8 = z6 || (tLObject instanceof TLRPC$TL_chatParticipantAdmin);
            if (this.selectType == 0) {
                z5 &= !UserObject.isDeleted(user);
            }
            if (!z) {
                arrayList3 = new ArrayList();
                arrayList2 = new ArrayList();
                arrayList = new ArrayList();
            } else {
                arrayList3 = null;
                arrayList2 = null;
                arrayList = null;
            }
            if (!z5) {
                arrayList4 = arrayList;
            } else if (z) {
                return true;
            } else {
                arrayList3.add(z8 ? LocaleController.getString("EditAdminRights", 2131625557) : LocaleController.getString("SetAsAdmin", 2131628238));
                arrayList4 = arrayList;
                arrayList4.add(2131165635);
                arrayList2.add(0);
            }
            if (!ChatObject.canBlockUsers(this.currentChat) || !z7) {
                z4 = false;
            } else if (z) {
                return true;
            } else {
                if (!this.isChannel) {
                    if (ChatObject.isChannel(this.currentChat) && !this.currentChat.gigagroup) {
                        arrayList3.add(LocaleController.getString("ChangePermissions", 2131624856));
                        arrayList4.add(2131165841);
                        arrayList2.add(1);
                    }
                    arrayList3.add(LocaleController.getString("KickFromGroup", 2131626344));
                } else {
                    arrayList3.add(LocaleController.getString("ChannelRemoveUser", 2131624953));
                }
                arrayList4.add(2131165892);
                arrayList2.add(2);
                z4 = true;
            }
            if (arrayList2 == null || arrayList2.isEmpty()) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            boolean z9 = z4;
            ArrayList arrayList5 = arrayList3;
            builder.setItems((CharSequence[]) arrayList3.toArray(new CharSequence[arrayList2.size()]), AndroidUtilities.toIntArray(arrayList4), new ChatUsersActivity$$ExternalSyntheticLambda3(this, arrayList2, user, j, z7, tLObject, i, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str));
            AlertDialog create = builder.create();
            showDialog(create);
            if (z9) {
                create.setItemColor(arrayList5.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
            }
            return true;
        }
        if (i4 != 3 || !ChatObject.canBlockUsers(this.currentChat)) {
            if (this.type != 0 || !ChatObject.canBlockUsers(this.currentChat)) {
                if (this.type != 1 || !ChatObject.canAddAdmins(this.currentChat) || !z2) {
                    z3 = false;
                    charSequenceArr = null;
                    iArr = null;
                } else if (z) {
                    return true;
                } else {
                    if (this.currentChat.creator || (!(tLObject instanceof TLRPC$TL_channelParticipantCreator) && z2)) {
                        z3 = false;
                        charSequenceArr = new CharSequence[]{LocaleController.getString("EditAdminRights", 2131625557), LocaleController.getString("ChannelRemoveUserAdmin", 2131624954)};
                        iArr = new int[]{2131165635, 2131165892};
                    } else {
                        z3 = false;
                        iArr = new int[]{2131165892};
                        charSequenceArr = new CharSequence[]{LocaleController.getString("ChannelRemoveUserAdmin", 2131624954)};
                    }
                }
                if (charSequenceArr != null) {
                    return z3;
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                builder2.setItems(charSequenceArr, iArr, new ChatUsersActivity$$ExternalSyntheticLambda5(this, charSequenceArr, j, tLRPC$TL_chatAdminRights, str, tLObject, tLRPC$TL_chatBannedRights));
                AlertDialog create2 = builder2.create();
                showDialog(create2);
                if (this.type != 1) {
                    return true;
                }
                create2.setItemColor(charSequenceArr.length - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
                return true;
            } else if (z) {
                return true;
            } else {
                CharSequence[] charSequenceArr2 = new CharSequence[2];
                if (!ChatObject.canAddUsers(this.currentChat) || j <= 0) {
                    c = 0;
                    str2 = null;
                } else {
                    if (this.isChannel) {
                        i2 = 2131624871;
                        str3 = "ChannelAddToChannel";
                    } else {
                        i2 = 2131624872;
                        str3 = "ChannelAddToGroup";
                    }
                    str2 = LocaleController.getString(str3, i2);
                    c = 0;
                }
                charSequenceArr2[c] = str2;
                charSequenceArr2[1] = LocaleController.getString("ChannelDeleteFromList", 2131624897);
                charSequenceArr = charSequenceArr2;
                iArr = new int[]{2131165690, 2131165702};
            }
        } else if (z) {
            return true;
        } else {
            iArr = new int[]{2131165841, 2131165702};
            charSequenceArr = new CharSequence[]{LocaleController.getString("ChannelEditPermissions", 2131624904), LocaleController.getString("ChannelDeleteFromList", 2131624897)};
        }
        z3 = false;
        if (charSequenceArr != null) {
        }
    }

    public /* synthetic */ void lambda$createMenuForParticipant$6(ArrayList arrayList, TLRPC$User tLRPC$User, long j, boolean z, TLObject tLObject, int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, DialogInterface dialogInterface, int i2) {
        if (((Integer) arrayList.get(i2)).intValue() == 2) {
            getMessagesController().deleteParticipantFromChat(this.chatId, tLRPC$User, null);
            removeParticipants(j);
            if (this.currentChat == null || tLRPC$User == null || !BulletinFactory.canShowBulletin(this)) {
                return;
            }
            BulletinFactory.createRemoveFromChatBulletin(this, tLRPC$User, this.currentChat.title).show();
        } else if (((Integer) arrayList.get(i2)).intValue() == 1 && z && ((tLObject instanceof TLRPC$TL_channelParticipantAdmin) || (tLObject instanceof TLRPC$TL_chatParticipantAdmin))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131624306, UserObject.getUserName(tLRPC$User)));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ChatUsersActivity$$ExternalSyntheticLambda2(this, j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, arrayList, i2));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            showDialog(builder.create());
        } else {
            openRightsEdit2(j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, ((Integer) arrayList.get(i2)).intValue(), false);
        }
    }

    public /* synthetic */ void lambda$createMenuForParticipant$5(long j, int i, TLObject tLObject, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str, boolean z, ArrayList arrayList, int i2, DialogInterface dialogInterface, int i3) {
        openRightsEdit2(j, i, tLObject, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, str, z, ((Integer) arrayList.get(i2)).intValue(), false);
    }

    public /* synthetic */ void lambda$createMenuForParticipant$9(CharSequence[] charSequenceArr, long j, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str, TLObject tLObject, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, DialogInterface dialogInterface, int i) {
        TLRPC$Chat tLRPC$Chat;
        int i2 = this.type;
        if (i2 == 1) {
            if (i == 0 && charSequenceArr.length == 2) {
                ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(j, this.chatId, tLRPC$TL_chatAdminRights, null, null, str, 0, true, false, null);
                chatRightsEditActivity.setDelegate(new AnonymousClass17(tLObject));
                presentFragment(chatRightsEditActivity);
                return;
            }
            getMessagesController().setUserAdminRole(this.chatId, getMessagesController().getUser(Long.valueOf(j)), new TLRPC$TL_chatAdminRights(), "", !this.isChannel, this, false, false, null, null);
            removeParticipants(j);
        } else if (i2 != 0 && i2 != 3) {
            if (i != 0) {
                return;
            }
            TLRPC$User tLRPC$User = null;
            if (j > 0) {
                tLRPC$Chat = null;
                tLRPC$User = getMessagesController().getUser(Long.valueOf(j));
            } else {
                tLRPC$Chat = getMessagesController().getChat(Long.valueOf(-j));
            }
            getMessagesController().deleteParticipantFromChat(this.chatId, tLRPC$User, tLRPC$Chat, null, false, false);
        } else {
            if (i == 0) {
                if (i2 == 3) {
                    ChatRightsEditActivity chatRightsEditActivity2 = new ChatRightsEditActivity(j, this.chatId, null, this.defaultBannedRights, tLRPC$TL_chatBannedRights, str, 1, true, false, null);
                    chatRightsEditActivity2.setDelegate(new AnonymousClass18(tLObject));
                    presentFragment(chatRightsEditActivity2);
                } else if (i2 == 0 && j > 0) {
                    getMessagesController().addUserToChat(this.chatId, getMessagesController().getUser(Long.valueOf(j)), 0, null, this, null);
                }
            } else if (i == 1) {
                TLRPC$TL_channels_editBanned tLRPC$TL_channels_editBanned = new TLRPC$TL_channels_editBanned();
                tLRPC$TL_channels_editBanned.participant = getMessagesController().getInputPeer(j);
                tLRPC$TL_channels_editBanned.channel = getMessagesController().getInputChannel(this.chatId);
                tLRPC$TL_channels_editBanned.banned_rights = new TLRPC$TL_chatBannedRights();
                getConnectionsManager().sendRequest(tLRPC$TL_channels_editBanned, new ChatUsersActivity$$ExternalSyntheticLambda15(this));
            }
            if ((i != 0 || this.type != 0) && i != 1) {
                return;
            }
            removeParticipants(tLObject);
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ TLObject val$participant;

        AnonymousClass17(TLObject tLObject) {
            ChatUsersActivity.this = r1;
            this.val$participant = tLObject;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            TLObject tLObject = this.val$participant;
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                tLRPC$ChannelParticipant.rank = str;
                ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, 0L, false);
            }
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }
    }

    /* renamed from: org.telegram.ui.ChatUsersActivity$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ TLObject val$participant;

        AnonymousClass18(TLObject tLObject) {
            ChatUsersActivity.this = r1;
            this.val$participant = tLObject;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            TLObject tLObject = this.val$participant;
            if (tLObject instanceof TLRPC$ChannelParticipant) {
                TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) tLObject;
                tLRPC$ChannelParticipant.admin_rights = tLRPC$TL_chatAdminRights;
                tLRPC$ChannelParticipant.banned_rights = tLRPC$TL_chatBannedRights;
                tLRPC$ChannelParticipant.rank = str;
                ChatUsersActivity.this.updateParticipantWithRights(tLRPC$ChannelParticipant, tLRPC$TL_chatAdminRights, tLRPC$TL_chatBannedRights, 0L, false);
            }
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
            ChatUsersActivity.this.onOwnerChaged(tLRPC$User);
        }
    }

    public /* synthetic */ void lambda$createMenuForParticipant$8(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
            getMessagesController().processUpdates(tLRPC$Updates, false);
            if (tLRPC$Updates.chats.isEmpty()) {
                return;
            }
            AndroidUtilities.runOnUIThread(new ChatUsersActivity$$ExternalSyntheticLambda9(this, tLRPC$Updates), 1000L);
        }
    }

    public /* synthetic */ void lambda$createMenuForParticipant$7(TLRPC$Updates tLRPC$Updates) {
        getMessagesController().loadFullChat(tLRPC$Updates.chats.get(0).id, 0, true);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatInfoDidLoad) {
            boolean z = false;
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            if (tLRPC$ChatFull.id != this.chatId) {
                return;
            }
            if (booleanValue && ChatObject.isChannel(this.currentChat)) {
                return;
            }
            if (this.info != null) {
                z = true;
            }
            this.info = tLRPC$ChatFull;
            if (!z) {
                int currentSlowmode = getCurrentSlowmode();
                this.initialSlowmode = currentSlowmode;
                this.selectedSlowmode = currentSlowmode;
            }
            AndroidUtilities.runOnUIThread(new ChatUsersActivity$$ExternalSyntheticLambda7(this));
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$10() {
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

    public String formatSeconds(int i) {
        if (i < 60) {
            return LocaleController.formatPluralString("Seconds", i, new Object[0]);
        }
        if (i < 3600) {
            return LocaleController.formatPluralString("Minutes", i / 60, new Object[0]);
        }
        return LocaleController.formatPluralString("Hours", (i / 60) / 60, new Object[0]);
    }

    public boolean checkDiscard() {
        if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights) || this.initialSlowmode != this.selectedSlowmode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131628812));
            if (this.isChannel) {
                builder.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131624958));
            } else {
                builder.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131626102));
            }
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131624387), new ChatUsersActivity$$ExternalSyntheticLambda1(this));
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131627220), new ChatUsersActivity$$ExternalSyntheticLambda0(this));
            showDialog(builder.create());
            return false;
        }
        return true;
    }

    public /* synthetic */ void lambda$checkDiscard$11(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$12(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    public boolean hasSelectType() {
        return this.selectType != 0;
    }

    public String formatUserPermissions(TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights) {
        if (tLRPC$TL_chatBannedRights == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = tLRPC$TL_chatBannedRights.view_messages;
        if (z && this.defaultBannedRights.view_messages != z) {
            sb.append(LocaleController.getString("UserRestrictionsNoRead", 2131628829));
        }
        boolean z2 = tLRPC$TL_chatBannedRights.send_messages;
        if (z2 && this.defaultBannedRights.send_messages != z2) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSend", 2131628830));
        }
        boolean z3 = tLRPC$TL_chatBannedRights.send_media;
        if (z3 && this.defaultBannedRights.send_media != z3) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendMedia", 2131628831));
        }
        boolean z4 = tLRPC$TL_chatBannedRights.send_stickers;
        if (z4 && this.defaultBannedRights.send_stickers != z4) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendStickers", 2131628833));
        }
        boolean z5 = tLRPC$TL_chatBannedRights.send_polls;
        if (z5 && this.defaultBannedRights.send_polls != z5) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendPolls", 2131628832));
        }
        boolean z6 = tLRPC$TL_chatBannedRights.embed_links;
        if (z6 && this.defaultBannedRights.embed_links != z6) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoEmbedLinks", 2131628826));
        }
        boolean z7 = tLRPC$TL_chatBannedRights.invite_users;
        if (z7 && this.defaultBannedRights.invite_users != z7) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoInviteUsers", 2131628827));
        }
        boolean z8 = tLRPC$TL_chatBannedRights.pin_messages;
        if (z8 && this.defaultBannedRights.pin_messages != z8) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoPinMessages", 2131628828));
        }
        boolean z9 = tLRPC$TL_chatBannedRights.change_info;
        if (z9 && this.defaultBannedRights.change_info != z9) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoChangeInfo", 2131628825));
        }
        if (sb.length() != 0) {
            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
            sb.append('.');
        }
        return sb.toString();
    }

    public void processDone() {
        TLRPC$ChatFull tLRPC$ChatFull;
        if (this.type != 3) {
            return;
        }
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        if (tLRPC$Chat.creator && !ChatObject.isChannel(tLRPC$Chat) && this.selectedSlowmode != this.initialSlowmode && this.info != null) {
            MessagesController.getInstance(this.currentAccount).convertToMegaGroup(getParentActivity(), this.chatId, this, new ChatUsersActivity$$ExternalSyntheticLambda13(this));
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

    public /* synthetic */ void lambda$processDone$13(long j) {
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
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsBots();
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
            if (listAdapter2 == null) {
                return;
            }
            listAdapter2.notifyDataSetChanged();
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
        ArrayList<TLRPC$TL_channels_getParticipants> loadChatParticipantsRequests = loadChatParticipantsRequests(i, i2, z);
        ArrayList arrayList = new ArrayList();
        ChatUsersActivity$$ExternalSyntheticLambda8 chatUsersActivity$$ExternalSyntheticLambda8 = new ChatUsersActivity$$ExternalSyntheticLambda8(this, loadChatParticipantsRequests, arrayList);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        while (i3 < loadChatParticipantsRequests.size()) {
            arrayList.add(null);
            getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(loadChatParticipantsRequests.get(i3), new ChatUsersActivity$$ExternalSyntheticLambda14(arrayList, i3, atomicInteger, loadChatParticipantsRequests, chatUsersActivity$$ExternalSyntheticLambda8)), this.classGuid);
            i3++;
        }
    }

    public /* synthetic */ void lambda$loadChatParticipants$14(ArrayList arrayList, ArrayList arrayList2) {
        int i;
        LongSparseArray<TLObject> longSparseArray;
        ArrayList<TLObject> arrayList3;
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

    public static /* synthetic */ void lambda$loadChatParticipants$16(ArrayList arrayList, int i, AtomicInteger atomicInteger, ArrayList arrayList2, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatUsersActivity$$ExternalSyntheticLambda6(tLRPC$TL_error, tLObject, arrayList, i, atomicInteger, arrayList2, runnable));
    }

    public static /* synthetic */ void lambda$loadChatParticipants$15(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, ArrayList arrayList, int i, AtomicInteger atomicInteger, ArrayList arrayList2, Runnable runnable) {
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_channels_channelParticipants)) {
            arrayList.set(i, (TLRPC$TL_channels_channelParticipants) tLObject);
        }
        atomicInteger.getAndIncrement();
        if (atomicInteger.get() == arrayList2.size()) {
            runnable.run();
        }
    }

    public void sortUsers(ArrayList<TLObject> arrayList) {
        Collections.sort(arrayList, new ChatUsersActivity$$ExternalSyntheticLambda12(this, getConnectionsManager().getCurrentTime()));
    }

    public /* synthetic */ int lambda$sortUsers$17(int i, TLObject tLObject, TLObject tLObject2) {
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
        } else if ((i2 < 0 && i3 > 0) || (i2 == 0 && i3 != 0)) {
            return -1;
        } else {
            return ((i3 >= 0 || i2 <= 0) && (i3 != 0 || i2 == 0)) ? 0 : 1;
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
        if (!z || z2 || !this.needOpenSearch) {
            return;
        }
        this.searchItem.getSearchField().requestFocus();
        AndroidUtilities.showKeyboard(this.searchItem.getSearchField());
        this.searchItem.setVisibility(8);
    }

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
            ChatUsersActivity.this = r1;
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda4(this));
        }

        public /* synthetic */ void lambda$new$0(int i) {
            if (!this.searchAdapterHelper.isSearchInProgress()) {
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
        }

        public void searchUsers(String str) {
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
            if (!TextUtils.isEmpty(str)) {
                this.searchInProgress = true;
                ChatUsersActivity.this.emptyView.showProgress(true, true);
                DispatchQueue dispatchQueue = Utilities.searchQueue;
                ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1 chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1 = new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1(this, str);
                this.searchRunnable = chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1;
                dispatchQueue.postRunnable(chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda1, 300L);
            }
        }

        /* renamed from: processSearch */
        public void lambda$searchUsers$1(String str) {
            AndroidUtilities.runOnUIThread(new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda0(this, str));
        }

        public /* synthetic */ void lambda$processSearch$3(String str) {
            ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2 chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2 = null;
            this.searchRunnable = null;
            ArrayList arrayList = (ChatObject.isChannel(ChatUsersActivity.this.currentChat) || ChatUsersActivity.this.info == null) ? null : new ArrayList(ChatUsersActivity.this.info.participants.participants);
            ArrayList arrayList2 = ChatUsersActivity.this.selectType == 1 ? new ArrayList(ChatUsersActivity.this.getContactsController().contacts) : null;
            if (arrayList != null || arrayList2 != null) {
                chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2 = new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2(this, str, arrayList, arrayList2);
            } else {
                this.searchInProgress = false;
            }
            this.searchAdapterHelper.queryServerSearch(str, ChatUsersActivity.this.selectType != 0, false, true, false, false, ChatObject.isChannel(ChatUsersActivity.this.currentChat) ? ChatUsersActivity.this.chatId : 0L, false, ChatUsersActivity.this.type, 1, chatUsersActivity$SearchAdapter$$ExternalSyntheticLambda2);
        }

        /* JADX WARN: Code restructure failed: missing block: B:48:0x013e, code lost:
            if (r15.contains(" " + r4) != false) goto L55;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x0247, code lost:
            if (r5.contains(" " + r9) != false) goto L94;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:101:0x02a1 A[LOOP:3: B:79:0x020d->B:101:0x02a1, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:111:0x0153 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:116:0x0259 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0188 A[LOOP:1: B:38:0x0100->B:62:0x0188, LOOP_END] */
        /* JADX WARN: Type inference failed for: r15v3, types: [java.util.ArrayList] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList, ArrayList arrayList2) {
            ArrayList<Object> arrayList3;
            LongSparseArray<TLObject> longSparseArray;
            int i;
            String[] strArr;
            int i2;
            ArrayList<Object> arrayList4;
            LongSparseArray<TLObject> longSparseArray2;
            int i3;
            int i4;
            long j;
            String str2;
            String str3;
            String str4;
            String str5;
            char c;
            ArrayList arrayList5 = arrayList;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new LongSparseArray<>(), new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
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
                        i4 = i6;
                        j = ((TLRPC$ChatParticipant) tLObject).user_id;
                    } else {
                        i4 = i6;
                        if (tLObject instanceof TLRPC$ChannelParticipant) {
                            j = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLObject).peer);
                        }
                        arrayList4 = arrayList6;
                        longSparseArray2 = longSparseArray3;
                        strArr = strArr2;
                        i3 = size;
                        i2 = i4;
                        i5++;
                        arrayList5 = arrayList;
                        size = i3;
                        longSparseArray3 = longSparseArray2;
                        arrayList6 = arrayList4;
                        i6 = i2;
                        strArr2 = strArr;
                    }
                    if (j > 0) {
                        TLRPC$User user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j));
                        if (user.id != ChatUsersActivity.this.getUserConfig().getClientUserId()) {
                            str5 = UserObject.getUserName(user).toLowerCase();
                            str3 = user.username;
                            str2 = user.first_name;
                            str4 = user.last_name;
                            i3 = size;
                        }
                        arrayList4 = arrayList6;
                        longSparseArray2 = longSparseArray3;
                        strArr = strArr2;
                        i3 = size;
                        i2 = i4;
                        i5++;
                        arrayList5 = arrayList;
                        size = i3;
                        longSparseArray3 = longSparseArray2;
                        arrayList6 = arrayList4;
                        i6 = i2;
                        strArr2 = strArr;
                    } else {
                        TLRPC$Chat chat = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-j));
                        String lowerCase2 = chat.title.toLowerCase();
                        str3 = chat.username;
                        str2 = chat.title;
                        str5 = lowerCase2;
                        i3 = size;
                        str4 = null;
                    }
                    String translitString2 = LocaleController.getInstance().getTranslitString(str5);
                    if (str5.equals(translitString2)) {
                        translitString2 = null;
                    }
                    arrayList4 = arrayList6;
                    longSparseArray2 = longSparseArray3;
                    int i7 = i4;
                    int i8 = 0;
                    char c2 = 0;
                    while (true) {
                        i2 = i7;
                        if (i8 >= i7) {
                            strArr = strArr2;
                            break;
                        }
                        String str6 = strArr2[i8];
                        if (!str5.startsWith(str6)) {
                            strArr = strArr2;
                            if (!str5.contains(" " + str6)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str6)) {
                                    }
                                }
                                c = (str3 == null || !str3.startsWith(str6)) ? c2 : (char) 2;
                                if (c == 0) {
                                    if (c == 1) {
                                        arrayList7.add(AndroidUtilities.generateSearchName(str2, str4, str6));
                                    } else {
                                        arrayList7.add(AndroidUtilities.generateSearchName("@" + str3, null, "@" + str6));
                                    }
                                    arrayList8.add(tLObject);
                                } else {
                                    i8++;
                                    i7 = i2;
                                    c2 = c;
                                    strArr2 = strArr;
                                }
                            }
                        } else {
                            strArr = strArr2;
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
                    i6 = i2;
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
                            String str7 = strArr3[i11];
                            if (!lowerCase3.startsWith(str7)) {
                                if (!lowerCase3.contains(" " + str7)) {
                                    if (translitString3 != null) {
                                        if (!translitString3.startsWith(str7)) {
                                        }
                                    }
                                    String str8 = user2.username;
                                    if (str8 != null && str8.startsWith(str7)) {
                                        c3 = 2;
                                    }
                                    if (c3 == 0) {
                                        if (c3 == 1) {
                                            arrayList7.add(AndroidUtilities.generateSearchName(user2.first_name, user2.last_name, str7));
                                            arrayList3 = arrayList9;
                                        } else {
                                            arrayList7.add(AndroidUtilities.generateSearchName("@" + user2.username, null, "@" + str7));
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

        private void updateSearchResults(ArrayList<Object> arrayList, LongSparseArray<TLObject> longSparseArray, ArrayList<CharSequence> arrayList2, ArrayList<TLObject> arrayList3) {
            AndroidUtilities.runOnUIThread(new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda3(this, arrayList, longSparseArray, arrayList2, arrayList3));
        }

        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, ArrayList arrayList3) {
            if (!ChatUsersActivity.this.searching) {
                return;
            }
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
                    if (i != 0) {
                        return this.searchAdapterHelper.getGroupSearch().get(i - 1);
                    }
                    return null;
                }
                i -= i2;
            }
            int size2 = this.searchResult.size();
            if (size2 != 0) {
                int i3 = size2 + 1;
                if (i3 > i) {
                    if (i != 0) {
                        return (TLObject) this.searchResult.get(i - 1);
                    }
                    return null;
                }
                i -= i3;
            }
            int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            if (size3 == 0 || size3 + 1 <= i || i == 0) {
                return null;
            }
            return this.searchAdapterHelper.getGlobalSearch().get(i - 1);
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$5(ManageChatUserCell manageChatUserCell, boolean z) {
            TLObject item = getItem(((Integer) manageChatUserCell.getTag()).intValue());
            if (item instanceof TLRPC$ChannelParticipant) {
                return ChatUsersActivity.this.createMenuForParticipant((TLRPC$ChannelParticipant) item, !z);
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GraySectionCell graySectionCell;
            if (i == 0) {
                ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 2, 2, ChatUsersActivity.this.selectType == 0);
                manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                manageChatUserCell.setDelegate(new ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda5(this));
                graySectionCell = manageChatUserCell;
            } else {
                graySectionCell = new GraySectionCell(this.mContext);
            }
            return new RecyclerListView.Holder(graySectionCell);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:41:0x00f6  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0108  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0113  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0145  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0151 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:68:0x015f  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x01a3 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:86:0x01b0  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            TLRPC$User tLRPC$User;
            int size;
            boolean z;
            String str2;
            SpannableStringBuilder spannableStringBuilder;
            String str3;
            int i2;
            boolean z2;
            String str4;
            int indexOfIgnoreCase;
            int size2;
            int size3;
            int i3;
            String str5;
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
                                graySectionCell.setText(LocaleController.getString("ChannelSubscribers", 2131624968));
                                return;
                            } else {
                                graySectionCell.setText(LocaleController.getString("ChannelMembers", 2131624917));
                                return;
                            }
                        }
                        graySectionCell.setText(LocaleController.getString("ChannelRestrictedUsers", 2131624955));
                        return;
                    }
                    graySectionCell.setText(LocaleController.getString("ChannelBlockedUsers", 2131624884));
                    return;
                } else if (i4 == this.globalStartRow) {
                    graySectionCell.setText(LocaleController.getString("GlobalSearch", 2131626079));
                    return;
                } else if (i4 != this.contactsStartRow) {
                    return;
                } else {
                    graySectionCell.setText(LocaleController.getString("Contacts", 2131625242));
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
                            str = user.username;
                            tLRPC$User = user;
                            size = this.searchAdapterHelper.getGroupSearch().size();
                            if (size != 0) {
                                int i5 = size + 1;
                                if (i5 > i4) {
                                    str2 = this.searchAdapterHelper.getLastFoundChannel();
                                    z = true;
                                    if (!z && (size3 = this.searchResult.size()) != 0) {
                                        i3 = size3 + 1;
                                        if (i3 <= i4) {
                                            CharSequence charSequence = this.searchResultNames.get(i4 - 1);
                                            if (charSequence != 0 && !TextUtils.isEmpty(str)) {
                                                if (charSequence.toString().startsWith("@" + str)) {
                                                    spannableStringBuilder = null;
                                                    str5 = charSequence;
                                                    i2 = i4;
                                                    z2 = true;
                                                    str3 = str5;
                                                    str4 = str3;
                                                    str4 = str3;
                                                    if (!z2 && str != null) {
                                                        size2 = this.searchAdapterHelper.getGlobalSearch().size();
                                                        str4 = str3;
                                                        if (size2 != 0) {
                                                            str4 = str3;
                                                            if (size2 + 1 > i2) {
                                                                String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                                                                if (lastFoundUsername.startsWith("@")) {
                                                                    lastFoundUsername = lastFoundUsername.substring(1);
                                                                }
                                                                try {
                                                                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                                                    spannableStringBuilder2.append((CharSequence) "@");
                                                                    spannableStringBuilder2.append((CharSequence) str);
                                                                    int indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str, lastFoundUsername);
                                                                    str4 = spannableStringBuilder2;
                                                                    if (indexOfIgnoreCase2 != -1) {
                                                                        int length = lastFoundUsername.length();
                                                                        if (indexOfIgnoreCase2 == 0) {
                                                                            length++;
                                                                        } else {
                                                                            indexOfIgnoreCase2++;
                                                                        }
                                                                        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), indexOfIgnoreCase2, length + indexOfIgnoreCase2, 33);
                                                                        str4 = spannableStringBuilder2;
                                                                    }
                                                                } catch (Exception e) {
                                                                    FileLog.e(e);
                                                                    str4 = str;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (str2 != null && str != null) {
                                                        spannableStringBuilder = new SpannableStringBuilder(str);
                                                        indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str, str2);
                                                        if (indexOfIgnoreCase != -1) {
                                                            spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), indexOfIgnoreCase, str2.length() + indexOfIgnoreCase, 33);
                                                        }
                                                    }
                                                    ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                                                    manageChatUserCell.setTag(Integer.valueOf(i2));
                                                    manageChatUserCell.setData(tLRPC$User, spannableStringBuilder, str4, false);
                                                }
                                            }
                                            spannableStringBuilder = charSequence;
                                            str5 = null;
                                            i2 = i4;
                                            z2 = true;
                                            str3 = str5;
                                            str4 = str3;
                                            str4 = str3;
                                            if (!z2) {
                                                size2 = this.searchAdapterHelper.getGlobalSearch().size();
                                                str4 = str3;
                                                if (size2 != 0) {
                                                }
                                            }
                                            if (str2 != null) {
                                                spannableStringBuilder = new SpannableStringBuilder(str);
                                                indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str, str2);
                                                if (indexOfIgnoreCase != -1) {
                                                }
                                            }
                                            ManageChatUserCell manageChatUserCell2 = (ManageChatUserCell) viewHolder.itemView;
                                            manageChatUserCell2.setTag(Integer.valueOf(i2));
                                            manageChatUserCell2.setData(tLRPC$User, spannableStringBuilder, str4, false);
                                        }
                                        i4 -= i3;
                                    }
                                    spannableStringBuilder = null;
                                    i2 = i4;
                                    z2 = z;
                                    str3 = null;
                                    str4 = str3;
                                    str4 = str3;
                                    if (!z2) {
                                    }
                                    if (str2 != null) {
                                    }
                                    ManageChatUserCell manageChatUserCell22 = (ManageChatUserCell) viewHolder.itemView;
                                    manageChatUserCell22.setTag(Integer.valueOf(i2));
                                    manageChatUserCell22.setData(tLRPC$User, spannableStringBuilder, str4, false);
                                }
                                i4 -= i5;
                            }
                            str2 = null;
                            z = false;
                            if (!z) {
                                i3 = size3 + 1;
                                if (i3 <= i4) {
                                }
                            }
                            spannableStringBuilder = null;
                            i2 = i4;
                            z2 = z;
                            str3 = null;
                            str4 = str3;
                            str4 = str3;
                            if (!z2) {
                            }
                            if (str2 != null) {
                            }
                            ManageChatUserCell manageChatUserCell222 = (ManageChatUserCell) viewHolder.itemView;
                            manageChatUserCell222.setTag(Integer.valueOf(i2));
                            manageChatUserCell222.setData(tLRPC$User, spannableStringBuilder, str4, false);
                        }
                    } else {
                        TLRPC$Chat chat = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-peerId));
                        tLRPC$User2 = chat;
                        if (chat != null) {
                            str = chat.username;
                            tLRPC$User = chat;
                            size = this.searchAdapterHelper.getGroupSearch().size();
                            if (size != 0) {
                            }
                            str2 = null;
                            z = false;
                            if (!z) {
                            }
                            spannableStringBuilder = null;
                            i2 = i4;
                            z2 = z;
                            str3 = null;
                            str4 = str3;
                            str4 = str3;
                            if (!z2) {
                            }
                            if (str2 != null) {
                            }
                            ManageChatUserCell manageChatUserCell2222 = (ManageChatUserCell) viewHolder.itemView;
                            manageChatUserCell2222.setTag(Integer.valueOf(i2));
                            manageChatUserCell2222.setData(tLRPC$User, spannableStringBuilder, str4, false);
                        }
                    }
                } else if (!(item instanceof TLRPC$ChatParticipant)) {
                    return;
                } else {
                    tLRPC$User2 = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(((TLRPC$ChatParticipant) item).user_id));
                }
            }
            str = null;
            tLRPC$User = tLRPC$User2;
            size = this.searchAdapterHelper.getGroupSearch().size();
            if (size != 0) {
            }
            str2 = null;
            z = false;
            if (!z) {
            }
            spannableStringBuilder = null;
            i2 = i4;
            z2 = z;
            str3 = null;
            str4 = str3;
            str4 = str3;
            if (!z2) {
            }
            if (str2 != null) {
            }
            ManageChatUserCell manageChatUserCell22222 = (ManageChatUserCell) viewHolder.itemView;
            manageChatUserCell22222.setTag(Integer.valueOf(i2));
            manageChatUserCell22222.setData(tLRPC$User, spannableStringBuilder, str4, false);
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

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            ChatUsersActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 7) {
                return ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat);
            }
            if (itemViewType != 0) {
                return itemViewType == 0 || itemViewType == 2 || itemViewType == 6;
            }
            Object currentObject = ((ManageChatUserCell) viewHolder.itemView).getCurrentObject();
            return ChatUsersActivity.this.type == 1 || !(currentObject instanceof TLRPC$User) || !((TLRPC$User) currentObject).self;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChatUsersActivity.this.rowCount;
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$0(ManageChatUserCell manageChatUserCell, boolean z) {
            return ChatUsersActivity.this.createMenuForParticipant(ChatUsersActivity.this.listViewAdapter.getItem(((Integer) manageChatUserCell.getTag()).intValue()), !z);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ChooseView chooseView;
            boolean z = false;
            int i2 = 6;
            switch (i) {
                case 0:
                    Context context = this.mContext;
                    int i3 = (ChatUsersActivity.this.type == 0 || ChatUsersActivity.this.type == 3) ? 7 : 6;
                    if (ChatUsersActivity.this.type != 0 && ChatUsersActivity.this.type != 3) {
                        i2 = 2;
                    }
                    if (ChatUsersActivity.this.selectType == 0) {
                        z = true;
                    }
                    ManageChatUserCell manageChatUserCell = new ManageChatUserCell(context, i3, i2, z);
                    manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    manageChatUserCell.setDelegate(new ChatUsersActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                    chooseView = manageChatUserCell;
                    break;
                case 1:
                    chooseView = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 2:
                    ManageChatTextCell manageChatTextCell = new ManageChatTextCell(this.mContext);
                    manageChatTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    chooseView = manageChatTextCell;
                    break;
                case 3:
                    chooseView = new ShadowSectionCell(this.mContext);
                    break;
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                    if (ChatUsersActivity.this.isChannel) {
                        textInfoPrivacyCell.setText(LocaleController.getString(2131626808));
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.getString(2131626809));
                    }
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    chooseView = textInfoPrivacyCell;
                    break;
                case 5:
                    HeaderCell headerCell = new HeaderCell(this.mContext, "windowBackgroundWhiteBlueHeader", 21, 11, false);
                    headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    headerCell.setHeight(43);
                    chooseView = headerCell;
                    break;
                case 6:
                    TextSettingsCell textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    chooseView = textSettingsCell;
                    break;
                case 7:
                    TextCheckCell2 textCheckCell2 = new TextCheckCell2(this.mContext);
                    textCheckCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    chooseView = textCheckCell2;
                    break;
                case 8:
                    GraySectionCell graySectionCell = new GraySectionCell(this.mContext);
                    graySectionCell.setBackground(null);
                    chooseView = graySectionCell;
                    break;
                case 9:
                default:
                    ChooseView chooseView2 = new ChooseView(this.mContext);
                    chooseView2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    chooseView = chooseView2;
                    break;
                case 10:
                    chooseView = new LoadingCell(this.mContext, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(120.0f));
                    break;
                case 11:
                    FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext);
                    flickerLoadingView.setIsSingleCell(true);
                    flickerLoadingView.setViewType(6);
                    flickerLoadingView.showDate(false);
                    flickerLoadingView.setPaddingLeft(AndroidUtilities.dp(5.0f));
                    flickerLoadingView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    flickerLoadingView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                    chooseView = flickerLoadingView;
                    break;
            }
            return new RecyclerListView.Holder(chooseView);
        }

        /* JADX WARN: Code restructure failed: missing block: B:263:0x0698, code lost:
            if (org.telegram.ui.ChatUsersActivity.this.currentChat.megagroup == false) goto L264;
         */
        /* JADX WARN: Code restructure failed: missing block: B:264:0x069a, code lost:
            r10 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:272:0x06c6, code lost:
            if (org.telegram.ui.ChatUsersActivity.this.currentChat.megagroup == false) goto L264;
         */
        /* JADX WARN: Removed duplicated region for block: B:321:0x07e2  */
        /* JADX WARN: Removed duplicated region for block: B:322:0x07e5  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            int i2;
            int i3;
            long j;
            int i4;
            boolean z2;
            boolean z3;
            boolean z4;
            long j2;
            long j3;
            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
            long j4;
            int i5;
            Object obj;
            boolean z5;
            CharSequence charSequence;
            String str;
            boolean z6;
            CharSequence charSequence2;
            TLRPC$User user;
            boolean z7;
            CharSequence charSequence3;
            TLRPC$User user2;
            boolean z8;
            CharSequence charSequence4;
            int itemViewType = viewHolder.getItemViewType();
            boolean z9 = false;
            int i6 = 1;
            if (itemViewType == 0) {
                ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                manageChatUserCell.setTag(Integer.valueOf(i));
                TLObject item = getItem(i);
                if (i < ChatUsersActivity.this.participantsStartRow || i >= ChatUsersActivity.this.participantsEndRow) {
                    if (i < ChatUsersActivity.this.contactsStartRow || i >= ChatUsersActivity.this.contactsEndRow) {
                        i2 = ChatUsersActivity.this.botEndRow;
                    } else {
                        i2 = ChatUsersActivity.this.contactsEndRow;
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                        }
                    }
                    z = false;
                } else {
                    i2 = ChatUsersActivity.this.participantsEndRow;
                    if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                    }
                    z = false;
                }
                if (item instanceof TLRPC$ChannelParticipant) {
                    TLRPC$ChannelParticipant tLRPC$ChannelParticipant = (TLRPC$ChannelParticipant) item;
                    j = MessageObject.getPeerId(tLRPC$ChannelParticipant.peer);
                    j2 = tLRPC$ChannelParticipant.kicked_by;
                    i3 = i2;
                    j3 = tLRPC$ChannelParticipant.promoted_by;
                    tLRPC$TL_chatBannedRights = tLRPC$ChannelParticipant.banned_rights;
                    i4 = tLRPC$ChannelParticipant.date;
                    z4 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantBanned;
                    z2 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator;
                    z3 = tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin;
                } else {
                    i3 = i2;
                    if (!(item instanceof TLRPC$ChatParticipant)) {
                        return;
                    }
                    TLRPC$ChatParticipant tLRPC$ChatParticipant = (TLRPC$ChatParticipant) item;
                    j = tLRPC$ChatParticipant.user_id;
                    i4 = tLRPC$ChatParticipant.date;
                    z2 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantCreator;
                    z3 = tLRPC$ChatParticipant instanceof TLRPC$TL_chatParticipantAdmin;
                    z4 = false;
                    j2 = 0;
                    j3 = 0;
                    tLRPC$TL_chatBannedRights = null;
                }
                if (j > 0) {
                    i5 = i4;
                    obj = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j));
                    j4 = j3;
                } else {
                    i5 = i4;
                    j4 = j3;
                    obj = ChatUsersActivity.this.getMessagesController().getChat(Long.valueOf(-j));
                }
                if (obj == null) {
                    return;
                }
                if (ChatUsersActivity.this.type == 3) {
                    String formatUserPermissions = ChatUsersActivity.this.formatUserPermissions(tLRPC$TL_chatBannedRights);
                    if (i != i3 - 1) {
                        charSequence4 = null;
                        z8 = true;
                    } else {
                        charSequence4 = null;
                        z8 = false;
                    }
                    manageChatUserCell.setData(obj, charSequence4, formatUserPermissions, z8);
                } else if (ChatUsersActivity.this.type != 0) {
                    if (ChatUsersActivity.this.type != 1) {
                        if (ChatUsersActivity.this.type != 2) {
                            return;
                        }
                        String formatJoined = (!z || i5 == 0) ? null : LocaleController.formatJoined(i5);
                        if (i != i3 - 1) {
                            charSequence = null;
                            z5 = true;
                        } else {
                            charSequence = null;
                            z5 = false;
                        }
                        manageChatUserCell.setData(obj, charSequence, formatJoined, z5);
                        return;
                    }
                    if (z2) {
                        str = LocaleController.getString("ChannelCreator", 2131624894);
                    } else {
                        if (!z3 || (user = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j4))) == null) {
                            str = null;
                        } else if (user.id == j) {
                            str = LocaleController.getString("ChannelAdministrator", 2131624876);
                        } else {
                            str = LocaleController.formatString("EditAdminPromotedBy", 2131625553, UserObject.getUserName(user));
                        }
                        if (i == i3 - 1) {
                            charSequence2 = null;
                            z6 = true;
                        } else {
                            charSequence2 = null;
                            z6 = false;
                        }
                        manageChatUserCell.setData(obj, charSequence2, str, z6);
                    }
                    if (i == i3 - 1) {
                    }
                    manageChatUserCell.setData(obj, charSequence2, str, z6);
                } else {
                    String formatString = (!z4 || (user2 = ChatUsersActivity.this.getMessagesController().getUser(Long.valueOf(j2))) == null) ? null : LocaleController.formatString("UserRemovedBy", 2131628808, UserObject.getUserName(user2));
                    if (i != i3 - 1) {
                        charSequence3 = null;
                        z7 = true;
                    } else {
                        charSequence3 = null;
                        z7 = false;
                    }
                    manageChatUserCell.setData(obj, charSequence3, formatString, z7);
                }
            } else if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == ChatUsersActivity.this.participantsInfoRow) {
                    if (ChatUsersActivity.this.type == 0 || ChatUsersActivity.this.type == 3) {
                        if (ChatUsersActivity.this.isChannel) {
                            textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedChannel2", 2131626808));
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedGroup2", 2131626809));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    } else if (ChatUsersActivity.this.type == 1) {
                        if (ChatUsersActivity.this.addNewRow != -1) {
                            if (ChatUsersActivity.this.isChannel) {
                                textInfoPrivacyCell.setText(LocaleController.getString("ChannelAdminsInfo", 2131624878));
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.getString("MegaAdminsInfo", 2131626580));
                            }
                        } else {
                            textInfoPrivacyCell.setText("");
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    } else if (ChatUsersActivity.this.type != 2) {
                    } else {
                        if (!ChatUsersActivity.this.isChannel || ChatUsersActivity.this.selectType != 0) {
                            textInfoPrivacyCell.setText("");
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("ChannelMembersInfo", 2131624918));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    }
                } else if (i != ChatUsersActivity.this.slowmodeInfoRow) {
                    if (i != ChatUsersActivity.this.gigaInfoRow) {
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("BroadcastGroupConvertInfo", 2131624757));
                } else {
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    ChatUsersActivity chatUsersActivity = ChatUsersActivity.this;
                    int secondsForIndex = chatUsersActivity.getSecondsForIndex(chatUsersActivity.selectedSlowmode);
                    if (ChatUsersActivity.this.info == null || secondsForIndex == 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("SlowmodeInfoOff", 2131628362));
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.formatString("SlowmodeInfoSelected", 2131628363, ChatUsersActivity.this.formatSeconds(secondsForIndex)));
                    }
                }
            } else if (itemViewType == 2) {
                ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                manageChatTextCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                if (i == ChatUsersActivity.this.addNewRow) {
                    if (ChatUsersActivity.this.type != 3) {
                        if (ChatUsersActivity.this.type != 0) {
                            if (ChatUsersActivity.this.type != 1) {
                                if (ChatUsersActivity.this.type != 2) {
                                    return;
                                }
                                manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                if (ChatUsersActivity.this.addNew2Row != -1 || ((!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) && ChatUsersActivity.this.membersHeaderRow == -1 && !ChatUsersActivity.this.participants.isEmpty())) {
                                    z9 = true;
                                }
                                if (ChatUsersActivity.this.isChannel) {
                                    manageChatTextCell.setText(LocaleController.getString("AddSubscriber", 2131624287), null, 2131165690, z9);
                                    return;
                                } else {
                                    manageChatTextCell.setText(LocaleController.getString("AddMember", 2131624269), null, 2131165690, z9);
                                    return;
                                }
                            }
                            manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                            if (!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) {
                                z9 = true;
                            }
                            manageChatTextCell.setText(LocaleController.getString("ChannelAddAdmin", 2131624867), null, 2131165634, z9);
                            return;
                        }
                        manageChatTextCell.setText(LocaleController.getString("ChannelBlockUser", 2131624883), null, 2131165970, false);
                        return;
                    }
                    manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    String string = LocaleController.getString("ChannelAddException", 2131624868);
                    if (ChatUsersActivity.this.participantsStartRow != -1) {
                        z9 = true;
                    }
                    manageChatTextCell.setText(string, null, 2131165690, z9);
                } else if (i != ChatUsersActivity.this.recentActionsRow) {
                    if (i != ChatUsersActivity.this.addNew2Row) {
                        if (i != ChatUsersActivity.this.gigaConvertRow) {
                            return;
                        }
                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        manageChatTextCell.setText(LocaleController.getString("BroadcastGroupConvert", 2131624756), null, 2131165673, false);
                        return;
                    }
                    manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    if ((!ChatUsersActivity.this.loadingUsers || ChatUsersActivity.this.firstLoaded) && ChatUsersActivity.this.membersHeaderRow == -1 && !ChatUsersActivity.this.participants.isEmpty()) {
                        z9 = true;
                    }
                    manageChatTextCell.setText(LocaleController.getString("ChannelInviteViaLink", 2131624906), null, 2131165783, z9);
                } else {
                    manageChatTextCell.setText(LocaleController.getString("EventLog", 2131625667), null, 2131165791, false);
                }
            } else if (itemViewType == 3) {
                if (i == ChatUsersActivity.this.addNewSectionRow || (ChatUsersActivity.this.type == 3 && i == ChatUsersActivity.this.participantsDividerRow && ChatUsersActivity.this.addNewRow == -1 && ChatUsersActivity.this.participantsStartRow == -1)) {
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                } else {
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                }
            } else if (itemViewType == 5) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == ChatUsersActivity.this.restricted1SectionRow) {
                    if (ChatUsersActivity.this.type == 0) {
                        int size = ChatUsersActivity.this.info != null ? ChatUsersActivity.this.info.kicked_count : ChatUsersActivity.this.participants.size();
                        if (size != 0) {
                            headerCell.setText(LocaleController.formatPluralString("RemovedUser", size, new Object[0]));
                            return;
                        } else {
                            headerCell.setText(LocaleController.getString("ChannelBlockedUsers", 2131624884));
                            return;
                        }
                    }
                    headerCell.setText(LocaleController.getString("ChannelRestrictedUsers", 2131624955));
                } else if (i != ChatUsersActivity.this.permissionsSectionRow) {
                    if (i != ChatUsersActivity.this.slowmodeRow) {
                        if (i != ChatUsersActivity.this.gigaHeaderRow) {
                            return;
                        }
                        headerCell.setText(LocaleController.getString("BroadcastGroup", 2131624755));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("Slowmode", 2131628360));
                } else {
                    headerCell.setText(LocaleController.getString("ChannelPermissionsHeader", 2131624943));
                }
            } else if (itemViewType == 6) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                String string2 = LocaleController.getString("ChannelBlacklist", 2131624882);
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf(ChatUsersActivity.this.info != null ? ChatUsersActivity.this.info.kicked_count : 0);
                textSettingsCell.setTextAndValue(string2, String.format("%d", objArr), false);
            } else if (itemViewType != 7) {
                if (itemViewType != 8) {
                    if (itemViewType != 11) {
                        return;
                    }
                    FlickerLoadingView flickerLoadingView = (FlickerLoadingView) viewHolder.itemView;
                    if (ChatUsersActivity.this.type == 0) {
                        if (ChatUsersActivity.this.info != null) {
                            i6 = ChatUsersActivity.this.info.kicked_count;
                        }
                        flickerLoadingView.setItemsCount(i6);
                        return;
                    }
                    flickerLoadingView.setItemsCount(1);
                    return;
                }
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (i == ChatUsersActivity.this.membersHeaderRow) {
                    if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                        graySectionCell.setText(LocaleController.getString("ChannelOtherSubscribers", 2131624941));
                    } else {
                        graySectionCell.setText(LocaleController.getString("ChannelOtherMembers", 2131624939));
                    }
                } else if (i != ChatUsersActivity.this.botHeaderRow) {
                    if (i == ChatUsersActivity.this.contactsHeaderRow) {
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                            graySectionCell.setText(LocaleController.getString("ChannelContacts", 2131624893));
                        } else {
                            graySectionCell.setText(LocaleController.getString("GroupContacts", 2131626089));
                        }
                    } else if (i != ChatUsersActivity.this.loadingHeaderRow) {
                    } else {
                        graySectionCell.setText("");
                    }
                } else {
                    graySectionCell.setText(LocaleController.getString("ChannelBots", 2131624885));
                }
            } else {
                TextCheckCell2 textCheckCell2 = (TextCheckCell2) viewHolder.itemView;
                if (i == ChatUsersActivity.this.changeInfoRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsChangeInfo", 2131628819), !ChatUsersActivity.this.defaultBannedRights.change_info && TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username), false);
                } else if (i == ChatUsersActivity.this.addUsersRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsInviteUsers", 2131628824), !ChatUsersActivity.this.defaultBannedRights.invite_users, true);
                } else if (i == ChatUsersActivity.this.pinMessagesRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsPinMessages", 2131628834), !ChatUsersActivity.this.defaultBannedRights.pin_messages && TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username), true);
                } else if (i == ChatUsersActivity.this.sendMessagesRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSend", 2131628836), !ChatUsersActivity.this.defaultBannedRights.send_messages, true);
                } else if (i == ChatUsersActivity.this.sendMediaRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendMedia", 2131628837), !ChatUsersActivity.this.defaultBannedRights.send_media, true);
                } else if (i == ChatUsersActivity.this.sendStickersRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendStickers", 2131628839), !ChatUsersActivity.this.defaultBannedRights.send_stickers, true);
                } else if (i == ChatUsersActivity.this.embedLinksRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsEmbedLinks", 2131628823), !ChatUsersActivity.this.defaultBannedRights.embed_links, true);
                } else if (i == ChatUsersActivity.this.sendPollsRow) {
                    textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendPolls", 2131628838), !ChatUsersActivity.this.defaultBannedRights.send_polls, true);
                }
                if (i == ChatUsersActivity.this.sendMediaRow || i == ChatUsersActivity.this.sendStickersRow || i == ChatUsersActivity.this.embedLinksRow || i == ChatUsersActivity.this.sendPollsRow) {
                    textCheckCell2.setEnabled(!ChatUsersActivity.this.defaultBannedRights.send_messages && !ChatUsersActivity.this.defaultBannedRights.view_messages);
                } else if (i == ChatUsersActivity.this.sendMessagesRow) {
                    textCheckCell2.setEnabled(!ChatUsersActivity.this.defaultBannedRights.view_messages);
                }
                if (ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
                    if ((i == ChatUsersActivity.this.addUsersRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 3)) || ((i == ChatUsersActivity.this.pinMessagesRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 0)) || ((i == ChatUsersActivity.this.changeInfoRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 1)) || (!TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username) && (i == ChatUsersActivity.this.pinMessagesRow || i == ChatUsersActivity.this.changeInfoRow))))) {
                        textCheckCell2.setIcon(2131166033);
                        return;
                    } else {
                        textCheckCell2.setIcon(0);
                        return;
                    }
                }
                textCheckCell2.setIcon(0);
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
            if ((i >= ChatUsersActivity.this.participantsStartRow && i < ChatUsersActivity.this.participantsEndRow) || ((i >= ChatUsersActivity.this.botStartRow && i < ChatUsersActivity.this.botEndRow) || (i >= ChatUsersActivity.this.contactsStartRow && i < ChatUsersActivity.this.contactsEndRow))) {
                return 0;
            }
            if (i == ChatUsersActivity.this.addNewSectionRow || i == ChatUsersActivity.this.participantsDividerRow || i == ChatUsersActivity.this.participantsDivider2Row) {
                return 3;
            }
            if (i == ChatUsersActivity.this.restricted1SectionRow || i == ChatUsersActivity.this.permissionsSectionRow || i == ChatUsersActivity.this.slowmodeRow || i == ChatUsersActivity.this.gigaHeaderRow) {
                return 5;
            }
            if (i == ChatUsersActivity.this.participantsInfoRow || i == ChatUsersActivity.this.slowmodeInfoRow || i == ChatUsersActivity.this.gigaInfoRow) {
                return 1;
            }
            if (i == ChatUsersActivity.this.blockedEmptyRow) {
                return 4;
            }
            if (i == ChatUsersActivity.this.removedUsersRow) {
                return 6;
            }
            if (i == ChatUsersActivity.this.changeInfoRow || i == ChatUsersActivity.this.addUsersRow || i == ChatUsersActivity.this.pinMessagesRow || i == ChatUsersActivity.this.sendMessagesRow || i == ChatUsersActivity.this.sendMediaRow || i == ChatUsersActivity.this.sendStickersRow || i == ChatUsersActivity.this.embedLinksRow || i == ChatUsersActivity.this.sendPollsRow) {
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
            return i == ChatUsersActivity.this.loadingUserCellRow ? 11 : 0;
        }

        public TLObject getItem(int i) {
            if (i < ChatUsersActivity.this.participantsStartRow || i >= ChatUsersActivity.this.participantsEndRow) {
                if (i < ChatUsersActivity.this.contactsStartRow || i >= ChatUsersActivity.this.contactsEndRow) {
                    if (i >= ChatUsersActivity.this.botStartRow && i < ChatUsersActivity.this.botEndRow) {
                        return (TLObject) ChatUsersActivity.this.bots.get(i - ChatUsersActivity.this.botStartRow);
                    }
                    return null;
                }
                return (TLObject) ChatUsersActivity.this.contacts.get(i - ChatUsersActivity.this.contactsStartRow);
            }
            return (TLObject) ChatUsersActivity.this.participants.get(i - ChatUsersActivity.this.participantsStartRow);
        }
    }

    public DiffCallback saveState() {
        DiffCallback diffCallback = new DiffCallback(this, null);
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
        if (view == null) {
            return;
        }
        this.layoutManager.scrollToPositionWithOffset(i2, view.getTop() - this.listView.getPaddingTop());
    }

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
            ChatUsersActivity.this = r1;
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
            this.oldParticipants = new ArrayList<>();
            this.oldBots = new ArrayList<>();
            this.oldContacts = new ArrayList<>();
        }

        /* synthetic */ DiffCallback(ChatUsersActivity chatUsersActivity, AnonymousClass1 anonymousClass1) {
            this();
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
            if (i >= this.oldParticipantsStartRow && i < this.oldParticipantsEndRow && i2 >= ChatUsersActivity.this.participantsStartRow && i2 < ChatUsersActivity.this.participantsEndRow) {
                return this.oldParticipants.get(i - this.oldParticipantsStartRow).equals(ChatUsersActivity.this.participants.get(i2 - ChatUsersActivity.this.participantsStartRow));
            }
            return this.oldPositionToItem.get(i) == this.newPositionToItem.get(i2);
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
            put(20, ChatUsersActivity.this.pinMessagesRow, sparseIntArray);
            put(21, ChatUsersActivity.this.changeInfoRow, sparseIntArray);
            put(22, ChatUsersActivity.this.removedUsersRow, sparseIntArray);
            put(23, ChatUsersActivity.this.contactsHeaderRow, sparseIntArray);
            put(24, ChatUsersActivity.this.botHeaderRow, sparseIntArray);
            put(25, ChatUsersActivity.this.membersHeaderRow, sparseIntArray);
            put(26, ChatUsersActivity.this.slowmodeRow, sparseIntArray);
            put(27, ChatUsersActivity.this.slowmodeSelectRow, sparseIntArray);
            put(28, ChatUsersActivity.this.slowmodeInfoRow, sparseIntArray);
            put(29, ChatUsersActivity.this.loadingProgressRow, sparseIntArray);
            put(30, ChatUsersActivity.this.loadingUserCellRow, sparseIntArray);
            put(31, ChatUsersActivity.this.loadingHeaderRow, sparseIntArray);
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChatUsersActivity$$ExternalSyntheticLambda16 chatUsersActivity$$ExternalSyntheticLambda16 = new ChatUsersActivity$$ExternalSyntheticLambda16(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, ManageChatUserCell.class, ManageChatTextCell.class, TextCheckCell2.class, TextSettingsCell.class, ChooseView.class}, null, null, null, "windowBackgroundWhite"));
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
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, chatUsersActivity$$ExternalSyntheticLambda16, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, chatUsersActivity$$ExternalSyntheticLambda16, "windowBackgroundWhiteBlueText"));
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
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatUsersActivity$$ExternalSyntheticLambda16, "avatar_backgroundPink"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$18() {
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
