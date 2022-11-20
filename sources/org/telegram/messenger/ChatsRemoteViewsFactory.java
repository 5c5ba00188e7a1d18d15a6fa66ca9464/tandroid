package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import androidx.collection.LongSparseArray;
import com.google.android.exoplayer2.util.Log;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
/* compiled from: ChatsWidgetService.java */
/* loaded from: classes.dex */
class ChatsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AccountInstance accountInstance;
    private int appWidgetId;
    private RectF bitmapRect;
    private boolean deleted;
    private Context mContext;
    private Paint roundPaint;
    private ArrayList<Long> dids = new ArrayList<>();
    private LongSparseArray<TLRPC$Dialog> dialogs = new LongSparseArray<>();
    private LongSparseArray<MessageObject> messageObjects = new LongSparseArray<>();

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getViewTypeCount() {
        return 2;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDestroy() {
    }

    public ChatsRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        Theme.createDialogsResources(context);
        boolean z = false;
        this.appWidgetId = intent.getIntExtra("appWidgetId", 0);
        SharedPreferences sharedPreferences = context.getSharedPreferences("shortcut_widget", 0);
        int i = sharedPreferences.getInt("account" + this.appWidgetId, -1);
        if (i >= 0) {
            this.accountInstance = AccountInstance.getInstance(i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("deleted");
        sb.append(this.appWidgetId);
        this.deleted = (sharedPreferences.getBoolean(sb.toString(), false) || this.accountInstance == null) ? true : z;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onCreate() {
        ApplicationLoader.postInitApplication();
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getCount() {
        if (this.deleted) {
            return 1;
        }
        return this.dids.size() + 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x024c, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L54;
     */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int i) {
        TLRPC$Chat tLRPC$Chat;
        CharSequence charSequence;
        TLRPC$User tLRPC$User;
        TLRPC$FileLocation tLRPC$FileLocation;
        Bitmap decodeFile;
        int i2;
        int i3;
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User2;
        CharSequence charSequence2;
        int color;
        CharSequence charSequence3;
        String replace;
        SpannableStringBuilder valueOf;
        char c;
        char c2;
        int i4;
        String charSequence4;
        char c3;
        char c4;
        String format;
        SpannableStringBuilder spannableStringBuilder;
        CharSequence charSequence5;
        CharSequence charSequence6;
        AvatarDrawable avatarDrawable;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        if (this.deleted) {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_deleted);
            remoteViews.setTextViewText(R.id.widget_deleted_text, LocaleController.getString("WidgetLoggedOff", R.string.WidgetLoggedOff));
            return remoteViews;
        } else if (i >= this.dids.size()) {
            RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_edititem);
            remoteViews2.setTextViewText(R.id.widget_edititem_text, LocaleController.getString("TapToEditWidget", R.string.TapToEditWidget));
            Bundle bundle = new Bundle();
            bundle.putInt("appWidgetId", this.appWidgetId);
            bundle.putInt("appWidgetType", 0);
            bundle.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            remoteViews2.setOnClickFillInIntent(R.id.widget_edititem, intent);
            return remoteViews2;
        } else {
            Long l = this.dids.get(i);
            CharSequence charSequence7 = "";
            if (DialogObject.isUserDialog(l.longValue())) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(l);
                if (tLRPC$User != null) {
                    if (UserObject.isUserSelf(tLRPC$User)) {
                        charSequence = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                    } else if (UserObject.isReplyUser(tLRPC$User)) {
                        charSequence = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                    } else if (UserObject.isDeleted(tLRPC$User)) {
                        charSequence = LocaleController.getString("HiddenName", R.string.HiddenName);
                    } else {
                        charSequence = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                    }
                    if (!UserObject.isReplyUser(tLRPC$User) && !UserObject.isUserSelf(tLRPC$User) && (tLRPC$UserProfilePhoto = tLRPC$User.photo) != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                        tLRPC$Chat = null;
                    }
                } else {
                    charSequence = charSequence7;
                }
                tLRPC$Chat = null;
                tLRPC$FileLocation = null;
            } else {
                TLRPC$Chat chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (chat2 != null) {
                    charSequence = chat2.title;
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat2.photo;
                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null || tLRPC$FileLocation.volume_id == 0 || tLRPC$FileLocation.local_id == 0) {
                        tLRPC$Chat = chat2;
                    } else {
                        tLRPC$Chat = chat2;
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$Chat = chat2;
                    charSequence = charSequence7;
                }
                tLRPC$User = null;
                tLRPC$FileLocation = null;
            }
            RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.shortcut_widget_item);
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_text, charSequence);
            if (tLRPC$FileLocation != null) {
                try {
                    decodeFile = BitmapFactory.decodeFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$FileLocation, true).toString());
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            } else {
                decodeFile = null;
            }
            int dp = AndroidUtilities.dp(48.0f);
            Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
            createBitmap.eraseColor(0);
            Canvas canvas = new Canvas(createBitmap);
            if (decodeFile == null) {
                if (tLRPC$User != null) {
                    avatarDrawable = new AvatarDrawable(tLRPC$User);
                    if (UserObject.isReplyUser(tLRPC$User)) {
                        avatarDrawable.setAvatarType(12);
                    } else if (UserObject.isUserSelf(tLRPC$User)) {
                        avatarDrawable.setAvatarType(1);
                    }
                } else {
                    avatarDrawable = new AvatarDrawable(tLRPC$Chat);
                }
                avatarDrawable.setBounds(0, 0, dp, dp);
                avatarDrawable.draw(canvas);
            } else {
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                BitmapShader bitmapShader = new BitmapShader(decodeFile, tileMode, tileMode);
                if (this.roundPaint == null) {
                    this.roundPaint = new Paint(1);
                    this.bitmapRect = new RectF();
                }
                float width = dp / decodeFile.getWidth();
                canvas.save();
                canvas.scale(width, width);
                this.roundPaint.setShader(bitmapShader);
                this.bitmapRect.set(0.0f, 0.0f, decodeFile.getWidth(), decodeFile.getHeight());
                canvas.drawRoundRect(this.bitmapRect, decodeFile.getWidth(), decodeFile.getHeight(), this.roundPaint);
                canvas.restore();
            }
            canvas.setBitmap(null);
            remoteViews3.setImageViewBitmap(R.id.shortcut_widget_item_avatar, createBitmap);
            MessageObject messageObject = this.messageObjects.get(l.longValue());
            TLRPC$Dialog tLRPC$Dialog = this.dialogs.get(l.longValue());
            if (messageObject != null) {
                long fromChatId = messageObject.getFromChatId();
                if (DialogObject.isUserDialog(fromChatId)) {
                    tLRPC$User2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                    chat = null;
                } else {
                    chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                    tLRPC$User2 = null;
                }
                int color2 = this.mContext.getResources().getColor(R.color.widget_text);
                if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                    if (ChatObject.isChannel(tLRPC$Chat)) {
                        TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                        charSequence6 = charSequence7;
                        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                            charSequence6 = charSequence7;
                        }
                        color = this.mContext.getResources().getColor(R.color.widget_action_text);
                        charSequence5 = charSequence6;
                    }
                    charSequence6 = messageObject.messageText;
                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                    charSequence5 = charSequence6;
                } else {
                    String str = "🖼 ";
                    if (tLRPC$Chat != null && chat == null && (!ChatObject.isChannel(tLRPC$Chat) || ChatObject.isMegagroup(tLRPC$Chat))) {
                        if (messageObject.isOutOwner()) {
                            replace = LocaleController.getString("FromYou", R.string.FromYou);
                        } else {
                            replace = tLRPC$User2 != null ? UserObject.getFirstName(tLRPC$User2).replace("\n", charSequence7) : "DELETED";
                        }
                        String str2 = replace;
                        CharSequence charSequence8 = messageObject.caption;
                        try {
                            if (charSequence8 != null) {
                                String charSequence9 = charSequence8.toString();
                                if (charSequence9.length() > 150) {
                                    charSequence9 = charSequence9.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                }
                                if (messageObject.isVideo()) {
                                    str = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str = "🎧 ";
                                } else if (!messageObject.isPhoto()) {
                                    str = "📎 ";
                                }
                                valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str + charSequence9.replace('\n', ' '), str2));
                            } else if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                int color3 = this.mContext.getResources().getColor(R.color.widget_action_text);
                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                    charSequence4 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                    i4 = 2;
                                    c3 = ' ';
                                    c4 = '\n';
                                    c2 = 0;
                                } else {
                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                        if (Build.VERSION.SDK_INT >= 18) {
                                            c2 = 0;
                                            format = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
                                        } else {
                                            c2 = 0;
                                            format = String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                        }
                                        charSequence4 = format;
                                        i4 = 2;
                                    } else {
                                        c2 = 0;
                                        if (messageObject.type == 14) {
                                            if (Build.VERSION.SDK_INT >= 18) {
                                                i4 = 2;
                                                charSequence4 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                            } else {
                                                i4 = 2;
                                                charSequence4 = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                            }
                                        } else {
                                            i4 = 2;
                                            charSequence4 = messageObject.messageText.toString();
                                        }
                                    }
                                    c3 = ' ';
                                    c4 = '\n';
                                }
                                String replace2 = charSequence4.replace(c4, c3);
                                Object[] objArr = new Object[i4];
                                objArr[c2] = replace2;
                                objArr[1] = str2;
                                SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                try {
                                    valueOf2.setSpan(new ForegroundColorSpanThemable("chats_attachMessage"), str2.length() + 2, valueOf2.length(), 33);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                color = color3;
                                spannableStringBuilder = valueOf2;
                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str2.length() + 1, 33);
                                charSequence5 = spannableStringBuilder;
                            } else {
                                String str3 = messageObject.messageOwner.message;
                                if (str3 != null) {
                                    if (str3.length() > 150) {
                                        c = 0;
                                        str3 = str3.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                    } else {
                                        c = 0;
                                    }
                                    Object[] objArr2 = new Object[2];
                                    objArr2[c] = str3.replace('\n', ' ').trim();
                                    objArr2[1] = str2;
                                    valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                } else {
                                    valueOf = SpannableStringBuilder.valueOf(charSequence7);
                                }
                            }
                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str2.length() + 1, 33);
                            charSequence5 = spannableStringBuilder;
                        } catch (Exception e2) {
                            FileLog.e(e2);
                            charSequence5 = spannableStringBuilder;
                        }
                        spannableStringBuilder = valueOf;
                        color = color2;
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                        if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                            charSequence3 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                        } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                            charSequence3 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                        } else if (messageObject.caption != null) {
                            if (messageObject.isVideo()) {
                                str = "📹 ";
                            } else if (messageObject.isVoice()) {
                                str = "🎤 ";
                            } else if (messageObject.isMusic()) {
                                str = "🎧 ";
                            } else if (!messageObject.isPhoto()) {
                                str = "📎 ";
                            }
                            charSequence3 = str + ((Object) messageObject.caption);
                        } else {
                            if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                charSequence2 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                charSequence2 = "🎮 " + messageObject.messageOwner.media.game.title;
                            } else if (messageObject.type == 14) {
                                charSequence2 = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                            } else {
                                charSequence2 = messageObject.messageText;
                                AndroidUtilities.highlightText(charSequence2, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                            }
                            CharSequence charSequence10 = charSequence2;
                            charSequence3 = charSequence10;
                            if (messageObject.messageOwner.media != null) {
                                charSequence3 = charSequence10;
                                if (!messageObject.isMediaEmpty()) {
                                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                    charSequence5 = charSequence10;
                                }
                            }
                        }
                        color = color2;
                        charSequence5 = charSequence3;
                    }
                }
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                int i5 = R.id.shortcut_widget_item_message;
                remoteViews3.setTextViewText(i5, charSequence5.toString());
                remoteViews3.setTextColor(i5, color);
            } else {
                if (tLRPC$Dialog != null && (i2 = tLRPC$Dialog.last_message_date) != 0) {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(i2));
                } else {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, charSequence7);
                }
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, charSequence7);
            }
            if (tLRPC$Dialog != null && (i3 = tLRPC$Dialog.unread_count) > 0) {
                int i6 = R.id.shortcut_widget_item_badge;
                remoteViews3.setTextViewText(i6, String.format("%d", Integer.valueOf(i3)));
                remoteViews3.setViewVisibility(i6, 0);
                if (this.accountInstance.getMessagesController().isDialogMuted(tLRPC$Dialog.id, 0)) {
                    remoteViews3.setBoolean(i6, "setEnabled", false);
                    remoteViews3.setInt(i6, "setBackgroundResource", R.drawable.widget_badge_muted_background);
                } else {
                    remoteViews3.setBoolean(i6, "setEnabled", true);
                    remoteViews3.setInt(i6, "setBackgroundResource", R.drawable.widget_badge_background);
                }
            } else {
                remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
            }
            Bundle bundle2 = new Bundle();
            if (DialogObject.isUserDialog(l.longValue())) {
                bundle2.putLong("userId", l.longValue());
            } else {
                bundle2.putLong("chatId", -l.longValue());
            }
            bundle2.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent intent2 = new Intent();
            intent2.putExtras(bundle2);
            remoteViews3.setOnClickFillInIntent(R.id.shortcut_widget_item, intent2);
            Log.d("kek", "kek " + charSequence);
            remoteViews3.setViewVisibility(R.id.shortcut_widget_item_divider, i == getCount() ? 8 : 0);
            return remoteViews3;
        }
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDataSetChanged() {
        this.dids.clear();
        this.messageObjects.clear();
        AccountInstance accountInstance = this.accountInstance;
        if (accountInstance == null || !accountInstance.getUserConfig().isClientActivated()) {
            return;
        }
        ArrayList<TLRPC$User> arrayList = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList2 = new ArrayList<>();
        LongSparseArray<TLRPC$Message> longSparseArray = new LongSparseArray<>();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 0, this.dids, this.dialogs, longSparseArray, arrayList, arrayList2);
        this.accountInstance.getMessagesController().putUsers(arrayList, true);
        this.accountInstance.getMessagesController().putChats(arrayList2, true);
        this.messageObjects.clear();
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            this.messageObjects.put(longSparseArray.keyAt(i), new MessageObject(this.accountInstance.getCurrentAccount(), longSparseArray.valueAt(i), (LongSparseArray<TLRPC$User>) null, (LongSparseArray<TLRPC$Chat>) null, false, true));
        }
    }
}
