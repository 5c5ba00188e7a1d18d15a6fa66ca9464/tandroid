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

    /* JADX WARN: Code restructure failed: missing block: B:88:0x025e, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L90;
     */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int i) {
        TLRPC$FileLocation tLRPC$FileLocation;
        CharSequence charSequence;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        Bitmap decodeFile;
        int i2;
        int i3;
        TLRPC$User tLRPC$User2;
        TLRPC$Chat tLRPC$Chat2;
        CharSequence charSequence2;
        CharSequence charSequence3;
        String str;
        String replace;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2;
        char c;
        char c2;
        char c3;
        char c4;
        String str2;
        String str3;
        CharSequence charSequence4;
        AvatarDrawable avatarDrawable;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        if (this.deleted) {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), 2131427356);
            remoteViews.setTextViewText(2131230951, LocaleController.getString("WidgetLoggedOff", 2131629244));
            return remoteViews;
        } else if (i >= this.dids.size()) {
            RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), 2131427357);
            remoteViews2.setTextViewText(2131230953, LocaleController.getString("TapToEditWidget", 2131628548));
            Bundle bundle = new Bundle();
            bundle.putInt("appWidgetId", this.appWidgetId);
            bundle.putInt("appWidgetType", 0);
            bundle.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            remoteViews2.setOnClickFillInIntent(2131230952, intent);
            return remoteViews2;
        } else {
            Long l = this.dids.get(i);
            CharSequence charSequence5 = "";
            if (DialogObject.isUserDialog(l.longValue())) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(l);
                if (tLRPC$User != null) {
                    if (UserObject.isUserSelf(tLRPC$User)) {
                        charSequence = LocaleController.getString("SavedMessages", 2131628077);
                    } else if (UserObject.isReplyUser(tLRPC$User)) {
                        charSequence = LocaleController.getString("RepliesTitle", 2131627920);
                    } else if (UserObject.isDeleted(tLRPC$User)) {
                        charSequence = LocaleController.getString("HiddenName", 2131626131);
                    } else {
                        charSequence = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                    }
                    if (!UserObject.isReplyUser(tLRPC$User) && !UserObject.isUserSelf(tLRPC$User) && (tLRPC$UserProfilePhoto = tLRPC$User.photo) != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                        tLRPC$Chat = null;
                    }
                } else {
                    charSequence = charSequence5;
                }
                tLRPC$Chat = null;
                tLRPC$FileLocation = null;
            } else {
                TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (chat != null) {
                    charSequence = chat.title;
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null || tLRPC$FileLocation.volume_id == 0 || tLRPC$FileLocation.local_id == 0) {
                        tLRPC$Chat = chat;
                    } else {
                        tLRPC$Chat = chat;
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$Chat = chat;
                    charSequence = charSequence5;
                }
                tLRPC$User = null;
                tLRPC$FileLocation = null;
            }
            RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), 2131427349);
            remoteViews3.setTextViewText(2131230917, charSequence);
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
            remoteViews3.setImageViewBitmap(2131230913, createBitmap);
            MessageObject messageObject = this.messageObjects.get(l.longValue());
            TLRPC$Dialog tLRPC$Dialog = this.dialogs.get(l.longValue());
            if (messageObject != null) {
                long fromChatId = messageObject.getFromChatId();
                if (DialogObject.isUserDialog(fromChatId)) {
                    tLRPC$User2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                    tLRPC$Chat2 = null;
                } else {
                    tLRPC$Chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                    tLRPC$User2 = null;
                }
                int color = this.mContext.getResources().getColor(2131034146);
                if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                    if (ChatObject.isChannel(tLRPC$Chat)) {
                        TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                        charSequence4 = charSequence5;
                        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                            charSequence4 = charSequence5;
                        }
                        color = this.mContext.getResources().getColor(2131034141);
                        charSequence2 = charSequence4;
                    }
                    charSequence4 = messageObject.messageText;
                    color = this.mContext.getResources().getColor(2131034141);
                    charSequence2 = charSequence4;
                } else {
                    String str4 = "🎤 ";
                    if (tLRPC$Chat != null && tLRPC$Chat2 == null && (!ChatObject.isChannel(tLRPC$Chat) || ChatObject.isMegagroup(tLRPC$Chat))) {
                        if (messageObject.isOutOwner()) {
                            replace = LocaleController.getString("FromYou", 2131626036);
                        } else {
                            replace = tLRPC$User2 != null ? UserObject.getFirstName(tLRPC$User2).replace("\n", charSequence5) : "DELETED";
                        }
                        String str5 = replace;
                        CharSequence charSequence6 = messageObject.caption;
                        try {
                            if (charSequence6 != null) {
                                String charSequence7 = charSequence6.toString();
                                if (charSequence7.length() > 150) {
                                    charSequence7 = charSequence7.substring(0, 150);
                                }
                                if (messageObject.isVideo()) {
                                    str3 = "📹 ";
                                } else {
                                    if (!messageObject.isVoice()) {
                                        if (messageObject.isMusic()) {
                                            str4 = "🎧 ";
                                        } else {
                                            str4 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                        }
                                    }
                                    str3 = str4;
                                }
                                spannableStringBuilder2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str3 + charSequence7.replace('\n', ' '), str5));
                            } else if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                int color2 = this.mContext.getResources().getColor(2131034141);
                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                    str2 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                    c4 = '\n';
                                    c3 = 1;
                                    c2 = 0;
                                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                    if (Build.VERSION.SDK_INT >= 18) {
                                        c2 = 0;
                                        str2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
                                    } else {
                                        c2 = 0;
                                        str2 = String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                    }
                                    c4 = '\n';
                                    c3 = 1;
                                } else {
                                    c2 = 0;
                                    if (messageObject.type == 14) {
                                        if (Build.VERSION.SDK_INT >= 18) {
                                            c3 = 1;
                                            str2 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                        } else {
                                            c3 = 1;
                                            str2 = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                        }
                                    } else {
                                        c3 = 1;
                                        str2 = messageObject.messageText.toString();
                                    }
                                    c4 = '\n';
                                }
                                Object[] objArr = new Object[2];
                                objArr[c2] = str2.replace(c4, ' ');
                                objArr[c3] = str5;
                                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                try {
                                    valueOf.setSpan(new ForegroundColorSpanThemable("chats_attachMessage"), str5.length() + 2, valueOf.length(), 33);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                color = color2;
                                spannableStringBuilder = valueOf;
                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str5.length() + 1, 33);
                                charSequence2 = spannableStringBuilder;
                            } else {
                                String str6 = messageObject.messageOwner.message;
                                if (str6 != null) {
                                    if (str6.length() > 150) {
                                        c = 0;
                                        str6 = str6.substring(0, 150);
                                    } else {
                                        c = 0;
                                    }
                                    Object[] objArr2 = new Object[2];
                                    objArr2[c] = str6.replace('\n', ' ').trim();
                                    objArr2[1] = str5;
                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                } else {
                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf(charSequence5);
                                }
                            }
                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str5.length() + 1, 33);
                            charSequence2 = spannableStringBuilder;
                        } catch (Exception e2) {
                            FileLog.e(e2);
                            charSequence2 = spannableStringBuilder;
                        }
                        spannableStringBuilder = spannableStringBuilder2;
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                        if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                            charSequence2 = LocaleController.getString("AttachPhotoExpired", 2131624503);
                        } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                            charSequence2 = LocaleController.getString("AttachVideoExpired", 2131624509);
                        } else if (messageObject.caption != null) {
                            if (messageObject.isVideo()) {
                                str = "📹 ";
                            } else {
                                if (!messageObject.isVoice()) {
                                    if (messageObject.isMusic()) {
                                        str4 = "🎧 ";
                                    } else {
                                        str4 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                    }
                                }
                                str = str4;
                            }
                            charSequence2 = str + ((Object) messageObject.caption);
                        } else {
                            if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                charSequence3 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                charSequence3 = "🎮 " + messageObject.messageOwner.media.game.title;
                            } else if (messageObject.type == 14) {
                                charSequence3 = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                            } else {
                                charSequence3 = messageObject.messageText;
                                AndroidUtilities.highlightText(charSequence3, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                            }
                            CharSequence charSequence8 = charSequence3;
                            charSequence2 = charSequence8;
                            if (messageObject.messageOwner.media != null) {
                                charSequence2 = charSequence8;
                                if (!messageObject.isMediaEmpty()) {
                                    color = this.mContext.getResources().getColor(2131034141);
                                    charSequence2 = charSequence8;
                                }
                            }
                        }
                    }
                }
                remoteViews3.setTextViewText(2131230918, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                remoteViews3.setTextViewText(2131230916, charSequence2.toString());
                remoteViews3.setTextColor(2131230916, color);
            } else {
                if (tLRPC$Dialog != null && (i3 = tLRPC$Dialog.last_message_date) != 0) {
                    remoteViews3.setTextViewText(2131230918, LocaleController.stringForMessageListDate(i3));
                } else {
                    remoteViews3.setTextViewText(2131230918, charSequence5);
                }
                remoteViews3.setTextViewText(2131230916, charSequence5);
            }
            if (tLRPC$Dialog != null && (i2 = tLRPC$Dialog.unread_count) > 0) {
                remoteViews3.setTextViewText(2131230914, String.format("%d", Integer.valueOf(i2)));
                remoteViews3.setViewVisibility(2131230914, 0);
                if (this.accountInstance.getMessagesController().isDialogMuted(tLRPC$Dialog.id)) {
                    remoteViews3.setBoolean(2131230914, "setEnabled", false);
                    remoteViews3.setInt(2131230914, "setBackgroundResource", 2131166228);
                } else {
                    remoteViews3.setBoolean(2131230914, "setEnabled", true);
                    remoteViews3.setInt(2131230914, "setBackgroundResource", 2131166227);
                }
            } else {
                remoteViews3.setViewVisibility(2131230914, 8);
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
            remoteViews3.setOnClickFillInIntent(2131230912, intent2);
            remoteViews3.setViewVisibility(2131230915, i == getCount() ? 8 : 0);
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
