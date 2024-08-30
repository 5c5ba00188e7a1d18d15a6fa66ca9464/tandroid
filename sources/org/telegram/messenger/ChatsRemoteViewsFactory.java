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
/* loaded from: classes3.dex */
class ChatsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AccountInstance accountInstance;
    private int appWidgetId;
    private RectF bitmapRect;
    private boolean deleted;
    private Context mContext;
    private Paint roundPaint;
    private ArrayList<Long> dids = new ArrayList<>();
    private LongSparseArray dialogs = new LongSparseArray();
    private LongSparseArray messageObjects = new LongSparseArray();

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
        this.deleted = (sharedPreferences.getBoolean(sb.toString(), false) || this.accountInstance == null) ? true : true;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getCount() {
        if (this.deleted) {
            return 1;
        }
        return this.dids.size() + 1;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public RemoteViews getLoadingView() {
        return null;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(8:118|(1:120)(2:128|(1:130)(8:131|(1:133)(1:135)|134|122|123|124|100|101))|121|122|123|124|100|101) */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0397, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0398, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r10 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x04ba, code lost:
        if (r3.isMediaEmpty() == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0250, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L55;
     */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int i) {
        TLRPC$Chat tLRPC$Chat;
        String str;
        TLRPC$User tLRPC$User;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$FileLocation tLRPC$FileLocation2;
        Bitmap decodeFile;
        int i2;
        String str2;
        int i3;
        int i4;
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User2;
        CharSequence charSequence;
        int i5;
        SpannableStringBuilder valueOf;
        String format;
        char c;
        int i6;
        String charSequence2;
        char c2;
        SpannableStringBuilder spannableStringBuilder;
        CharSequence charSequence3;
        CharSequence charSequence4;
        AvatarDrawable avatarDrawable;
        String formatName;
        int i7;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation3;
        if (this.deleted) {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_deleted);
            remoteViews.setTextViewText(R.id.widget_deleted_text, LocaleController.getString(R.string.WidgetLoggedOff));
            return remoteViews;
        } else if (i >= this.dids.size()) {
            RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_edititem);
            remoteViews2.setTextViewText(R.id.widget_edititem_text, LocaleController.getString(R.string.TapToEditWidget));
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
            CharSequence charSequence5 = "";
            if (DialogObject.isUserDialog(l.longValue())) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(l);
                if (tLRPC$User != null) {
                    if (UserObject.isUserSelf(tLRPC$User)) {
                        i7 = R.string.SavedMessages;
                    } else if (UserObject.isReplyUser(tLRPC$User)) {
                        i7 = R.string.RepliesTitle;
                    } else if (UserObject.isDeleted(tLRPC$User)) {
                        i7 = R.string.HiddenName;
                    } else {
                        formatName = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                        if (!UserObject.isReplyUser(tLRPC$User) || UserObject.isUserSelf(tLRPC$User) || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null || (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation3.volume_id == 0 || tLRPC$FileLocation3.local_id == 0) {
                            tLRPC$FileLocation = null;
                            str = formatName;
                            tLRPC$Chat = null;
                        } else {
                            tLRPC$FileLocation = tLRPC$FileLocation3;
                            str = formatName;
                            tLRPC$Chat = null;
                        }
                    }
                    formatName = LocaleController.getString(i7);
                    if (UserObject.isReplyUser(tLRPC$User)) {
                    }
                    tLRPC$FileLocation = null;
                    str = formatName;
                    tLRPC$Chat = null;
                } else {
                    str = "";
                    tLRPC$Chat = null;
                    tLRPC$FileLocation = null;
                }
            } else {
                TLRPC$Chat chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (chat2 != null) {
                    String str3 = chat2.title;
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat2.photo;
                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) == null || tLRPC$FileLocation2.volume_id == 0 || tLRPC$FileLocation2.local_id == 0) {
                        tLRPC$FileLocation = null;
                        str = str3;
                        tLRPC$Chat = chat2;
                        tLRPC$User = null;
                    } else {
                        tLRPC$FileLocation = tLRPC$FileLocation2;
                        str = str3;
                        tLRPC$Chat = chat2;
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$Chat = chat2;
                    str = "";
                    tLRPC$User = null;
                    tLRPC$FileLocation = null;
                }
            }
            RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.shortcut_widget_item);
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_text, str);
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
                    avatarDrawable = new AvatarDrawable();
                    avatarDrawable.setInfo(this.accountInstance.getCurrentAccount(), tLRPC$Chat);
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
            MessageObject messageObject = (MessageObject) this.messageObjects.get(l.longValue());
            TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) this.dialogs.get(l.longValue());
            if (messageObject != null) {
                long fromChatId = messageObject.getFromChatId();
                if (DialogObject.isUserDialog(fromChatId)) {
                    tLRPC$User2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                    chat = null;
                } else {
                    chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                    tLRPC$User2 = null;
                }
                int color = this.mContext.getResources().getColor(R.color.widget_text);
                if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                    if (ChatObject.isChannel(tLRPC$Chat)) {
                        TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                        charSequence4 = charSequence5;
                        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                            charSequence4 = charSequence5;
                        }
                    }
                    charSequence4 = messageObject.messageText;
                } else {
                    String str4 = "📎 ";
                    if (tLRPC$Chat == null || chat != null || (ChatObject.isChannel(tLRPC$Chat) && !ChatObject.isMegagroup(tLRPC$Chat))) {
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                            i5 = R.string.AttachPhotoExpired;
                        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                            i5 = R.string.AttachVideoExpired;
                        } else if (messageObject.caption != null) {
                            if (messageObject.isVideo()) {
                                str4 = "📹 ";
                            } else if (messageObject.isVoice()) {
                                str4 = "🎤 ";
                            } else if (messageObject.isMusic()) {
                                str4 = "🎧 ";
                            } else if (messageObject.isPhoto()) {
                                str4 = "🖼 ";
                            }
                            charSequence3 = str4 + ((Object) messageObject.caption);
                        } else {
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                charSequence = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.question.text;
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                            } else if (messageObject.type == 14) {
                                charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                            } else {
                                charSequence = messageObject.messageText;
                                AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                            }
                            CharSequence charSequence6 = charSequence;
                            charSequence3 = charSequence6;
                            if (messageObject.messageOwner.media != null) {
                                charSequence4 = charSequence6;
                                charSequence3 = charSequence6;
                            }
                        }
                        charSequence3 = LocaleController.getString(i5);
                    } else {
                        String string = messageObject.isOutOwner() ? LocaleController.getString(R.string.FromYou) : tLRPC$User2 != null ? UserObject.getFirstName(tLRPC$User2).replace("\n", "") : "DELETED";
                        CharSequence charSequence7 = messageObject.caption;
                        char c3 = ' ';
                        try {
                            if (charSequence7 != null) {
                                String charSequence8 = charSequence7.toString();
                                if (charSequence8.length() > 150) {
                                    charSequence8 = charSequence8.substring(0, 150);
                                }
                                if (messageObject.isVideo()) {
                                    str4 = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str4 = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str4 = "🎧 ";
                                } else if (messageObject.isPhoto()) {
                                    str4 = "🖼 ";
                                }
                                format = String.format("%2$s: \u2068%1$s\u2069", str4 + charSequence8.replace('\n', ' '), string);
                            } else if (messageObject.messageOwner.media == null || messageObject.isMediaEmpty()) {
                                String str5 = messageObject.messageOwner.message;
                                if (str5 != null) {
                                    if (str5.length() > 150) {
                                        str5 = str5.substring(0, 150);
                                    }
                                    format = String.format("%2$s: \u2068%1$s\u2069", str5.replace('\n', ' ').trim(), string);
                                } else {
                                    valueOf = SpannableStringBuilder.valueOf("");
                                    spannableStringBuilder = valueOf;
                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                    charSequence3 = spannableStringBuilder;
                                }
                            } else {
                                color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                                if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                    c = 1;
                                    charSequence2 = String.format("📊 \u2068%s\u2069", ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question.text);
                                } else {
                                    c = 1;
                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                        charSequence2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia2.game.title);
                                    } else {
                                        if (messageObject.type == 14) {
                                            i6 = 2;
                                            charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                        } else {
                                            i6 = 2;
                                            charSequence2 = messageObject.messageText.toString();
                                        }
                                        c3 = ' ';
                                        c2 = '\n';
                                        String replace = charSequence2.replace(c2, c3);
                                        Object[] objArr = new Object[i6];
                                        objArr[0] = replace;
                                        objArr[c] = string;
                                        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                        valueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), string.length() + 2, valueOf2.length(), 33);
                                        spannableStringBuilder = valueOf2;
                                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                        charSequence3 = spannableStringBuilder;
                                    }
                                }
                                c2 = '\n';
                                i6 = 2;
                                String replace2 = charSequence2.replace(c2, c3);
                                Object[] objArr2 = new Object[i6];
                                objArr2[0] = replace2;
                                objArr2[c] = string;
                                SpannableStringBuilder valueOf22 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                valueOf22.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), string.length() + 2, valueOf22.length(), 33);
                                spannableStringBuilder = valueOf22;
                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                charSequence3 = spannableStringBuilder;
                            }
                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                            charSequence3 = spannableStringBuilder;
                        } catch (Exception e) {
                            FileLog.e(e);
                            charSequence3 = spannableStringBuilder;
                        }
                        valueOf = SpannableStringBuilder.valueOf(format);
                        spannableStringBuilder = valueOf;
                    }
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                    int i8 = R.id.shortcut_widget_item_message;
                    remoteViews3.setTextViewText(i8, charSequence3.toString());
                    remoteViews3.setTextColor(i8, color);
                }
                color = this.mContext.getResources().getColor(R.color.widget_action_text);
                charSequence3 = charSequence4;
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                int i82 = R.id.shortcut_widget_item_message;
                remoteViews3.setTextViewText(i82, charSequence3.toString());
                remoteViews3.setTextColor(i82, color);
            } else {
                if (tLRPC$Dialog == null || (i2 = tLRPC$Dialog.last_message_date) == 0) {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, "");
                } else {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(i2));
                }
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, "");
            }
            if (tLRPC$Dialog == null || (i3 = tLRPC$Dialog.unread_count) <= 0) {
                remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
            } else {
                int i9 = R.id.shortcut_widget_item_badge;
                remoteViews3.setTextViewText(i9, String.format("%d", Integer.valueOf(i3)));
                remoteViews3.setViewVisibility(i9, 0);
                if (this.accountInstance.getMessagesController().isDialogMuted(tLRPC$Dialog.id, 0L)) {
                    remoteViews3.setBoolean(i9, "setEnabled", false);
                    i4 = R.drawable.widget_badge_muted_background;
                } else {
                    remoteViews3.setBoolean(i9, "setEnabled", true);
                    i4 = R.drawable.widget_badge_background;
                }
                remoteViews3.setInt(i9, "setBackgroundResource", i4);
            }
            Bundle bundle2 = new Bundle();
            boolean isUserDialog = DialogObject.isUserDialog(l.longValue());
            long longValue = l.longValue();
            if (isUserDialog) {
                str2 = "userId";
            } else {
                longValue = -longValue;
                str2 = "chatId";
            }
            bundle2.putLong(str2, longValue);
            bundle2.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent intent2 = new Intent();
            intent2.putExtras(bundle2);
            remoteViews3.setOnClickFillInIntent(R.id.shortcut_widget_item, intent2);
            remoteViews3.setViewVisibility(R.id.shortcut_widget_item_divider, i == getCount() ? 8 : 0);
            return remoteViews3;
        }
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
    public void onCreate() {
        ApplicationLoader.postInitApplication();
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
        LongSparseArray longSparseArray = new LongSparseArray();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 0, this.dids, this.dialogs, longSparseArray, arrayList, arrayList2);
        this.accountInstance.getMessagesController().putUsers(arrayList, true);
        this.accountInstance.getMessagesController().putChats(arrayList2, true);
        this.messageObjects.clear();
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            this.messageObjects.put(longSparseArray.keyAt(i), new MessageObject(this.accountInstance.getCurrentAccount(), (TLRPC$Message) longSparseArray.valueAt(i), (LongSparseArray) null, (LongSparseArray) null, false, true));
        }
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDestroy() {
    }
}
