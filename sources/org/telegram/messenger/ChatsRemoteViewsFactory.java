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
import org.telegram.tgnet.TLRPC;
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
        if ((r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom) != false) goto L55;
     */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int i) {
        TLRPC.Chat chat;
        String str;
        TLRPC.User user;
        TLRPC.FileLocation fileLocation;
        TLRPC.FileLocation fileLocation2;
        Bitmap decodeFile;
        int i2;
        String str2;
        int i3;
        int i4;
        TLRPC.Chat chat2;
        TLRPC.User user2;
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
        TLRPC.UserProfilePhoto userProfilePhoto;
        TLRPC.FileLocation fileLocation3;
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
                user = this.accountInstance.getMessagesController().getUser(l);
                if (user != null) {
                    if (UserObject.isUserSelf(user)) {
                        i7 = R.string.SavedMessages;
                    } else if (UserObject.isReplyUser(user)) {
                        i7 = R.string.RepliesTitle;
                    } else if (UserObject.isDeleted(user)) {
                        i7 = R.string.HiddenName;
                    } else {
                        formatName = ContactsController.formatName(user.first_name, user.last_name);
                        if (!UserObject.isReplyUser(user) || UserObject.isUserSelf(user) || (userProfilePhoto = user.photo) == null || (fileLocation3 = userProfilePhoto.photo_small) == null || fileLocation3.volume_id == 0 || fileLocation3.local_id == 0) {
                            fileLocation = null;
                            str = formatName;
                            chat = null;
                        } else {
                            fileLocation = fileLocation3;
                            str = formatName;
                            chat = null;
                        }
                    }
                    formatName = LocaleController.getString(i7);
                    if (UserObject.isReplyUser(user)) {
                    }
                    fileLocation = null;
                    str = formatName;
                    chat = null;
                } else {
                    str = "";
                    chat = null;
                    fileLocation = null;
                }
            } else {
                TLRPC.Chat chat3 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (chat3 != null) {
                    String str3 = chat3.title;
                    TLRPC.ChatPhoto chatPhoto = chat3.photo;
                    if (chatPhoto == null || (fileLocation2 = chatPhoto.photo_small) == null || fileLocation2.volume_id == 0 || fileLocation2.local_id == 0) {
                        fileLocation = null;
                        str = str3;
                        chat = chat3;
                        user = null;
                    } else {
                        fileLocation = fileLocation2;
                        str = str3;
                        chat = chat3;
                        user = null;
                    }
                } else {
                    chat = chat3;
                    str = "";
                    user = null;
                    fileLocation = null;
                }
            }
            RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.shortcut_widget_item);
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_text, str);
            if (fileLocation != null) {
                try {
                    decodeFile = BitmapFactory.decodeFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(fileLocation, true).toString());
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
                if (user != null) {
                    avatarDrawable = new AvatarDrawable(user);
                    if (UserObject.isReplyUser(user)) {
                        avatarDrawable.setAvatarType(12);
                    } else if (UserObject.isUserSelf(user)) {
                        avatarDrawable.setAvatarType(1);
                    }
                } else {
                    avatarDrawable = new AvatarDrawable();
                    avatarDrawable.setInfo(this.accountInstance.getCurrentAccount(), chat);
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
            TLRPC.Dialog dialog = (TLRPC.Dialog) this.dialogs.get(l.longValue());
            if (messageObject != null) {
                long fromChatId = messageObject.getFromChatId();
                if (DialogObject.isUserDialog(fromChatId)) {
                    user2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                    chat2 = null;
                } else {
                    chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                    user2 = null;
                }
                int color = this.mContext.getResources().getColor(R.color.widget_text);
                if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                    if (ChatObject.isChannel(chat)) {
                        TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
                        charSequence4 = charSequence5;
                        if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear)) {
                            charSequence4 = charSequence5;
                        }
                    }
                    charSequence4 = messageObject.messageText;
                } else {
                    String str4 = "📎 ";
                    if (chat == null || chat2 != null || (ChatObject.isChannel(chat) && !ChatObject.isMegagroup(chat))) {
                        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                        if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (messageMedia.photo instanceof TLRPC.TL_photoEmpty) && messageMedia.ttl_seconds != 0) {
                            i5 = R.string.AttachPhotoExpired;
                        } else if ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && (messageMedia.document instanceof TLRPC.TL_documentEmpty) && messageMedia.ttl_seconds != 0) {
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
                            if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                            } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
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
                        String string = messageObject.isOutOwner() ? LocaleController.getString(R.string.FromYou) : user2 != null ? UserObject.getFirstName(user2).replace("\n", "") : "DELETED";
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
                                TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
                                if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                    c = 1;
                                    charSequence2 = String.format("📊 \u2068%s\u2069", ((TLRPC.TL_messageMediaPoll) messageMedia2).poll.question.text);
                                } else {
                                    c = 1;
                                    if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                        charSequence2 = String.format("🎮 \u2068%s\u2069", messageMedia2.game.title);
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
                if (dialog == null || (i2 = dialog.last_message_date) == 0) {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, "");
                } else {
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(i2));
                }
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, "");
            }
            if (dialog == null || (i3 = dialog.unread_count) <= 0) {
                remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
            } else {
                int i9 = R.id.shortcut_widget_item_badge;
                remoteViews3.setTextViewText(i9, String.format("%d", Integer.valueOf(i3)));
                remoteViews3.setViewVisibility(i9, 0);
                if (this.accountInstance.getMessagesController().isDialogMuted(dialog.id, 0L)) {
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
        ArrayList<TLRPC.User> arrayList = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList2 = new ArrayList<>();
        LongSparseArray longSparseArray = new LongSparseArray();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 0, this.dids, this.dialogs, longSparseArray, arrayList, arrayList2);
        this.accountInstance.getMessagesController().putUsers(arrayList, true);
        this.accountInstance.getMessagesController().putChats(arrayList2, true);
        this.messageObjects.clear();
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            this.messageObjects.put(longSparseArray.keyAt(i), new MessageObject(this.accountInstance.getCurrentAccount(), (TLRPC.Message) longSparseArray.valueAt(i), (LongSparseArray) null, (LongSparseArray) null, false, true));
        }
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDestroy() {
    }
}
