package org.telegram.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsRecent;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_chatFull;
import org.telegram.tgnet.TLRPC$TL_messages_getFullChat;
import org.telegram.tgnet.TLRPC$TL_messages_getMessageReadParticipants;
import org.telegram.tgnet.TLRPC$TL_readParticipantDate;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckDrawable;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.HideViewAfterAnimation;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class MessageSeenView extends FrameLayout {
    AvatarsImageView avatarsImageView;
    int currentAccount;
    ArrayList<Integer> dates;
    FlickerLoadingView flickerLoadingView;
    ImageView iconView;
    boolean ignoreLayout;
    boolean isVoice;
    private RecyclerListView listView;
    ArrayList<Long> peerIds;
    SimpleTextView titleView;
    public ArrayList<TLRPC$User> users;

    public MessageSeenView(Context context, final int i, MessageObject messageObject, final TLRPC$Chat tLRPC$Chat) {
        super(context);
        this.peerIds = new ArrayList<>();
        this.dates = new ArrayList<>();
        this.users = new ArrayList<>();
        this.currentAccount = i;
        this.isVoice = messageObject.isRoundVideo() || messageObject.isVoice();
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.flickerLoadingView = flickerLoadingView;
        flickerLoadingView.setColors("actionBarDefaultSubmenuBackground", "listSelectorSDK21", null);
        this.flickerLoadingView.setViewType(13);
        this.flickerLoadingView.setIsSingleCell(false);
        addView(this.flickerLoadingView, LayoutHelper.createFrame(-2, -1.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.titleView = simpleTextView;
        simpleTextView.setTextSize(16);
        this.titleView.setEllipsizeByGradient(true);
        this.titleView.setRightPadding(AndroidUtilities.dp(62.0f));
        addView(this.titleView, LayoutHelper.createFrame(0, -2.0f, 19, 40.0f, 0.0f, 0.0f, 0.0f));
        AvatarsImageView avatarsImageView = new AvatarsImageView(context, false);
        this.avatarsImageView = avatarsImageView;
        avatarsImageView.setStyle(11);
        this.avatarsImageView.setAvatarsTextSize(AndroidUtilities.dp(22.0f));
        addView(this.avatarsImageView, LayoutHelper.createFrame(56, -1.0f, 21, 0.0f, 0.0f, 0.0f, 0.0f));
        this.titleView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        TLRPC$TL_messages_getMessageReadParticipants tLRPC$TL_messages_getMessageReadParticipants = new TLRPC$TL_messages_getMessageReadParticipants();
        tLRPC$TL_messages_getMessageReadParticipants.msg_id = messageObject.getId();
        tLRPC$TL_messages_getMessageReadParticipants.peer = MessagesController.getInstance(i).getInputPeer(messageObject.getDialogId());
        ImageView imageView = new ImageView(context);
        this.iconView = imageView;
        addView(imageView, LayoutHelper.createFrame(24, 24.0f, 19, 11.0f, 0.0f, 0.0f, 0.0f));
        Drawable mutate = ContextCompat.getDrawable(context, this.isVoice ? R.drawable.msg_played : R.drawable.msg_seen).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuItemIcon"), PorterDuff.Mode.MULTIPLY));
        this.iconView.setImageDrawable(mutate);
        this.avatarsImageView.setAlpha(0.0f);
        this.titleView.setAlpha(0.0f);
        TLRPC$Peer tLRPC$Peer = messageObject.messageOwner.from_id;
        final long j = tLRPC$Peer != null ? tLRPC$Peer.user_id : 0L;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getMessageReadParticipants, new RequestDelegate() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda5
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                MessageSeenView.this.lambda$new$5(j, i, tLRPC$Chat, tLObject, tLRPC$TL_error);
            }
        });
        setBackground(Theme.createRadSelectorDrawable(Theme.getColor("dialogButtonSelector"), 6, 0));
        setEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(final long j, final int i, final TLRPC$Chat tLRPC$Chat, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MessageSeenView.this.lambda$new$4(tLRPC$TL_error, tLObject, j, i, tLRPC$Chat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, long j, final int i, TLRPC$Chat tLRPC$Chat) {
        if (tLRPC$TL_error == null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            ArrayList arrayList = new ArrayList();
            final HashMap hashMap = new HashMap();
            final ArrayList arrayList2 = new ArrayList();
            int size = tLRPC$Vector.objects.size();
            for (int i2 = 0; i2 < size; i2++) {
                Object obj = tLRPC$Vector.objects.get(i2);
                if (obj instanceof TLRPC$TL_readParticipantDate) {
                    TLRPC$TL_readParticipantDate tLRPC$TL_readParticipantDate = (TLRPC$TL_readParticipantDate) obj;
                    int i3 = tLRPC$TL_readParticipantDate.date;
                    Long valueOf = Long.valueOf(tLRPC$TL_readParticipantDate.user_id);
                    if (j != valueOf.longValue()) {
                        MessagesController.getInstance(i).getUser(valueOf);
                        arrayList2.add(new Pair(valueOf, Integer.valueOf(i3)));
                        arrayList.add(valueOf);
                    }
                } else if (obj instanceof Long) {
                    Long l = (Long) obj;
                    if (j != l.longValue()) {
                        MessagesController.getInstance(i).getUser(l);
                        arrayList2.add(new Pair(l, 0));
                        arrayList.add(l);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                    Pair pair = (Pair) arrayList2.get(i4);
                    this.peerIds.add((Long) pair.first);
                    this.dates.add((Integer) pair.second);
                    this.users.add((TLRPC$User) hashMap.get(pair.first));
                }
                updateView();
                return;
            } else if (ChatObject.isChannel(tLRPC$Chat)) {
                TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
                tLRPC$TL_channels_getParticipants.limit = MessagesController.getInstance(i).chatReadMarkSizeThreshold;
                tLRPC$TL_channels_getParticipants.offset = 0;
                tLRPC$TL_channels_getParticipants.filter = new TLRPC$TL_channelParticipantsRecent();
                tLRPC$TL_channels_getParticipants.channel = MessagesController.getInstance(i).getInputChannel(tLRPC$Chat.id);
                ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda4
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        MessageSeenView.this.lambda$new$1(i, hashMap, arrayList2, tLObject2, tLRPC$TL_error2);
                    }
                });
                return;
            } else {
                TLRPC$TL_messages_getFullChat tLRPC$TL_messages_getFullChat = new TLRPC$TL_messages_getFullChat();
                tLRPC$TL_messages_getFullChat.chat_id = tLRPC$Chat.id;
                ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getFullChat, new RequestDelegate() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        MessageSeenView.this.lambda$new$3(i, hashMap, arrayList2, tLObject2, tLRPC$TL_error2);
                    }
                });
                return;
            }
        }
        updateView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final int i, final HashMap hashMap, final ArrayList arrayList, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MessageSeenView.this.lambda$new$0(tLObject, i, hashMap, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLObject tLObject, int i, HashMap hashMap, ArrayList arrayList) {
        if (tLObject != null) {
            TLRPC$TL_channels_channelParticipants tLRPC$TL_channels_channelParticipants = (TLRPC$TL_channels_channelParticipants) tLObject;
            for (int i2 = 0; i2 < tLRPC$TL_channels_channelParticipants.users.size(); i2++) {
                TLRPC$User tLRPC$User = tLRPC$TL_channels_channelParticipants.users.get(i2);
                MessagesController.getInstance(i).putUser(tLRPC$User, false);
                hashMap.put(Long.valueOf(tLRPC$User.id), tLRPC$User);
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                Pair pair = (Pair) arrayList.get(i3);
                this.peerIds.add((Long) pair.first);
                this.dates.add((Integer) pair.second);
                this.users.add((TLRPC$User) hashMap.get(pair.first));
            }
        }
        updateView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(final int i, final HashMap hashMap, final ArrayList arrayList, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.MessageSeenView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MessageSeenView.this.lambda$new$2(tLObject, i, hashMap, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(TLObject tLObject, int i, HashMap hashMap, ArrayList arrayList) {
        if (tLObject != null) {
            TLRPC$TL_messages_chatFull tLRPC$TL_messages_chatFull = (TLRPC$TL_messages_chatFull) tLObject;
            for (int i2 = 0; i2 < tLRPC$TL_messages_chatFull.users.size(); i2++) {
                TLRPC$User tLRPC$User = tLRPC$TL_messages_chatFull.users.get(i2);
                MessagesController.getInstance(i).putUser(tLRPC$User, false);
                hashMap.put(Long.valueOf(tLRPC$User.id), tLRPC$User);
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                Pair pair = (Pair) arrayList.get(i3);
                this.peerIds.add((Long) pair.first);
                this.dates.add((Integer) pair.second);
                this.users.add((TLRPC$User) hashMap.get(pair.first));
            }
        }
        updateView();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        View view = (View) getParent();
        if (view != null && view.getWidth() > 0) {
            i = View.MeasureSpec.makeMeasureSpec(view.getWidth(), 1073741824);
        }
        this.ignoreLayout = true;
        boolean z = this.flickerLoadingView.getVisibility() == 0;
        this.titleView.setVisibility(8);
        if (z) {
            this.flickerLoadingView.setVisibility(8);
        }
        super.onMeasure(i, i2);
        if (z) {
            this.flickerLoadingView.getLayoutParams().width = getMeasuredWidth();
            this.flickerLoadingView.setVisibility(0);
        }
        this.titleView.setVisibility(0);
        this.titleView.getLayoutParams().width = getMeasuredWidth() - AndroidUtilities.dp(40.0f);
        this.ignoreLayout = false;
        super.onMeasure(i, i2);
    }

    private void updateView() {
        setEnabled(this.users.size() > 0);
        for (int i = 0; i < 3; i++) {
            if (i < this.users.size()) {
                this.avatarsImageView.setObject(i, this.currentAccount, this.users.get(i));
            } else {
                this.avatarsImageView.setObject(i, this.currentAccount, null);
            }
        }
        if (this.users.size() == 1) {
            this.avatarsImageView.setTranslationX(AndroidUtilities.dp(24.0f));
        } else if (this.users.size() == 2) {
            this.avatarsImageView.setTranslationX(AndroidUtilities.dp(12.0f));
        } else {
            this.avatarsImageView.setTranslationX(0.0f);
        }
        this.titleView.setRightPadding(AndroidUtilities.dp((Math.min(2, this.users.size() - 1) * 12) + 32 + 6));
        this.avatarsImageView.commitTransition(false);
        if (this.peerIds.size() == 1 && this.users.get(0) != null) {
            this.titleView.setText(ContactsController.formatName(this.users.get(0).first_name, this.users.get(0).last_name));
        } else if (this.peerIds.size() == 0) {
            this.titleView.setText(LocaleController.getString("NobodyViewed", R.string.NobodyViewed));
        } else {
            this.titleView.setText(LocaleController.formatPluralString(this.isVoice ? "MessagePlayed" : "MessageSeen", this.peerIds.size(), new Object[0]));
        }
        this.titleView.animate().alpha(1.0f).setDuration(220L).start();
        this.avatarsImageView.animate().alpha(1.0f).setDuration(220L).start();
        this.flickerLoadingView.animate().alpha(0.0f).setDuration(220L).setListener(new HideViewAfterAnimation(this.flickerLoadingView)).start();
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.getAdapter();
        }
    }

    public RecyclerListView createListView() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            return recyclerListView;
        }
        RecyclerListView recyclerListView2 = new RecyclerListView(this, getContext()) { // from class: org.telegram.ui.MessageSeenView.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public void onMeasure(int i, int i2) {
                int size = View.MeasureSpec.getSize(i2);
                int dp = AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(50.0f) * getAdapter().getItemCount());
                if (dp <= size) {
                    size = dp;
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            }
        };
        this.listView = recyclerListView2;
        recyclerListView2.setLayoutManager(new LinearLayoutManager(getContext()));
        this.listView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.MessageSeenView.2
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                if (recyclerView.getChildAdapterPosition(view) == MessageSeenView.this.users.size() - 1) {
                    rect.bottom = AndroidUtilities.dp(4.0f);
                }
            }
        });
        this.listView.setAdapter(new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.MessageSeenView.3
            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                UserCell userCell = new UserCell(viewGroup.getContext());
                userCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(50.0f)));
                return new RecyclerListView.Holder(userCell);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((UserCell) viewHolder.itemView).setUser(MessageSeenView.this.users.get(i), MessageSeenView.this.dates.get(i).intValue());
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return MessageSeenView.this.users.size();
            }
        });
        return this.listView;
    }

    /* loaded from: classes3.dex */
    private static class UserCell extends FrameLayout {
        AvatarDrawable avatarDrawable;
        BackupImageView avatarImageView;
        TextView nameView;
        TextView readView;

        public UserCell(Context context) {
            super(context);
            this.avatarDrawable = new AvatarDrawable();
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(18.0f));
            addView(this.avatarImageView, LayoutHelper.createFrame(34, 34.0f, 16, 10.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.nameView = textView;
            textView.setTextSize(1, 16.0f);
            this.nameView.setLines(1);
            this.nameView.setEllipsize(TextUtils.TruncateAt.END);
            this.nameView.setImportantForAccessibility(2);
            this.nameView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
            addView(this.nameView, LayoutHelper.createFrame(-2, -2.0f, 51, 55.0f, 5.33f, 13.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.readView = textView2;
            textView2.setTextSize(1, 13.0f);
            this.readView.setLines(1);
            this.readView.setEllipsize(TextUtils.TruncateAt.END);
            this.readView.setImportantForAccessibility(2);
            this.readView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            addView(this.readView, LayoutHelper.createFrame(-2, -2.0f, 51, 55.0f, 19.0f, 13.0f, 0.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        public void setUser(TLRPC$User tLRPC$User, int i) {
            if (tLRPC$User != null) {
                this.avatarDrawable.setInfo(tLRPC$User);
                this.avatarImageView.setImage(ImageLocation.getForUser(tLRPC$User, 1), "50_50", this.avatarDrawable, tLRPC$User);
                this.nameView.setText(ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name));
            }
            if (i <= 0) {
                this.readView.setVisibility(8);
                this.nameView.setTranslationY(AndroidUtilities.dp(9.0f));
                return;
            }
            this.readView.setText(TextUtils.concat(CheckDrawable.getSpanned(getContext()), LocaleController.formatSeenDate(i)));
            this.readView.setVisibility(0);
            this.nameView.setTranslationY(0.0f);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(LocaleController.formatString("AccDescrPersonHasSeen", R.string.AccDescrPersonHasSeen, this.nameView.getText()));
        }
    }
}
