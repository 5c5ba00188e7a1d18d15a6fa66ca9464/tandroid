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
import java.io.File;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
/* compiled from: ChatsWidgetService.java */
/* loaded from: classes4.dex */
class ChatsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AccountInstance accountInstance;
    private int appWidgetId;
    private RectF bitmapRect;
    private boolean deleted;
    private Context mContext;
    private Paint roundPaint;
    private ArrayList<Long> dids = new ArrayList<>();
    private LongSparseArray<TLRPC.Dialog> dialogs = new LongSparseArray<>();
    private LongSparseArray<MessageObject> messageObjects = new LongSparseArray<>();

    public ChatsRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        Theme.createDialogsResources(context);
        boolean z = false;
        this.appWidgetId = intent.getIntExtra("appWidgetId", 0);
        SharedPreferences preferences = context.getSharedPreferences("shortcut_widget", 0);
        int accountId = preferences.getInt("account" + this.appWidgetId, -1);
        if (accountId >= 0) {
            this.accountInstance = AccountInstance.getInstance(accountId);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("deleted");
        sb.append(this.appWidgetId);
        this.deleted = (preferences.getBoolean(sb.toString(), false) || this.accountInstance == null) ? true : z;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onCreate() {
        ApplicationLoader.postInitApplication();
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDestroy() {
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getCount() {
        if (this.deleted) {
            return 1;
        }
        return this.dids.size() + 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:237:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0659  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x06af  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x06b9  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0169 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01c9 A[Catch: all -> 0x0232, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0232, blocks: (B:58:0x0186, B:70:0x01c9, B:74:0x01e6), top: B:262:0x0186 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0258  */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int position) {
        TLRPC.Chat chat;
        TLRPC.User user;
        TLRPC.FileLocation photoPath;
        String name;
        RemoteViews rv;
        Bitmap bitmap;
        MessageObject message;
        TLRPC.Dialog dialog;
        TLRPC.User fromUser;
        int textColor;
        String messageString;
        String emoji;
        CharSequence messageNameString;
        SpannableStringBuilder stringBuilder;
        int textColor2;
        int textColor3;
        Exception e;
        char c;
        String innerMessage;
        int textColor4;
        Exception e2;
        String emoji2;
        AvatarDrawable avatarDrawable;
        String name2;
        if (this.deleted) {
            RemoteViews rv2 = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.widget_deleted);
            rv2.setTextViewText(org.telegram.messenger.beta.R.id.widget_deleted_text, LocaleController.getString("WidgetLoggedOff", org.telegram.messenger.beta.R.string.WidgetLoggedOff));
            return rv2;
        } else if (position >= this.dids.size()) {
            RemoteViews rv3 = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.widget_edititem);
            rv3.setTextViewText(org.telegram.messenger.beta.R.id.widget_edititem_text, LocaleController.getString("TapToEditWidget", org.telegram.messenger.beta.R.string.TapToEditWidget));
            Bundle extras = new Bundle();
            extras.putInt("appWidgetId", this.appWidgetId);
            extras.putInt("appWidgetType", 0);
            extras.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv3.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.widget_edititem, fillInIntent);
            return rv3;
        } else {
            Long id = this.dids.get(position);
            TLRPC.User user2 = null;
            TLRPC.Chat chat2 = null;
            if (DialogObject.isUserDialog(id.longValue())) {
                user2 = this.accountInstance.getMessagesController().getUser(id);
                if (user2 == null) {
                    chat = null;
                    user = user2;
                    photoPath = null;
                    name = "";
                } else {
                    if (UserObject.isUserSelf(user2)) {
                        name2 = LocaleController.getString("SavedMessages", org.telegram.messenger.beta.R.string.SavedMessages);
                    } else if (UserObject.isReplyUser(user2)) {
                        name2 = LocaleController.getString("RepliesTitle", org.telegram.messenger.beta.R.string.RepliesTitle);
                    } else if (UserObject.isDeleted(user2)) {
                        name2 = LocaleController.getString("HiddenName", org.telegram.messenger.beta.R.string.HiddenName);
                    } else {
                        name2 = ContactsController.formatName(user2.first_name, user2.last_name);
                    }
                    if (!UserObject.isReplyUser(user2) && !UserObject.isUserSelf(user2) && user2.photo != null && user2.photo.photo_small != null && user2.photo.photo_small.volume_id != 0 && user2.photo.photo_small.local_id != 0) {
                        chat = null;
                        user = user2;
                        photoPath = user2.photo.photo_small;
                        name = name2;
                    }
                    chat = chat2;
                    user = user2;
                    photoPath = null;
                    name = name2;
                }
                rv = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.shortcut_widget_item);
                rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_text, name);
                bitmap = null;
                if (photoPath != null) {
                    try {
                        File path = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoPath, true);
                        bitmap = BitmapFactory.decodeFile(path.toString());
                    } catch (Throwable th) {
                        e = th;
                        FileLog.e(e);
                        message = this.messageObjects.get(id.longValue());
                        dialog = this.dialogs.get(id.longValue());
                        if (message == null) {
                        }
                        int i = 8;
                        if (dialog == null) {
                        }
                        rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                        Bundle extras2 = new Bundle();
                        if (DialogObject.isUserDialog(id.longValue())) {
                        }
                        extras2.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                        Intent fillInIntent2 = new Intent();
                        fillInIntent2.putExtras(extras2);
                        rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent2);
                        if (position != getCount()) {
                        }
                        rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i);
                        return rv;
                    }
                }
                try {
                    int size = AndroidUtilities.dp(48.0f);
                    Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                    result.eraseColor(0);
                    Canvas canvas = new Canvas(result);
                    if (bitmap != null) {
                        if (user != null) {
                            avatarDrawable = new AvatarDrawable(user);
                            if (UserObject.isReplyUser(user)) {
                                avatarDrawable.setAvatarType(12);
                            } else if (UserObject.isUserSelf(user)) {
                                avatarDrawable.setAvatarType(1);
                            }
                        } else {
                            avatarDrawable = new AvatarDrawable(chat);
                        }
                        avatarDrawable.setBounds(0, 0, size, size);
                        avatarDrawable.draw(canvas);
                    } else {
                        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                        if (this.roundPaint == null) {
                            this.roundPaint = new Paint(1);
                            this.bitmapRect = new RectF();
                        }
                        float scale = size / bitmap.getWidth();
                        canvas.save();
                        canvas.scale(scale, scale);
                        this.roundPaint.setShader(shader);
                        try {
                            try {
                                this.bitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
                                canvas.drawRoundRect(this.bitmapRect, bitmap.getWidth(), bitmap.getHeight(), this.roundPaint);
                                canvas.restore();
                            } catch (Throwable th2) {
                                e = th2;
                                FileLog.e(e);
                                message = this.messageObjects.get(id.longValue());
                                dialog = this.dialogs.get(id.longValue());
                                if (message == null) {
                                }
                                int i2 = 8;
                                if (dialog == null) {
                                }
                                rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                                Bundle extras22 = new Bundle();
                                if (DialogObject.isUserDialog(id.longValue())) {
                                }
                                extras22.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                Intent fillInIntent22 = new Intent();
                                fillInIntent22.putExtras(extras22);
                                rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent22);
                                if (position != getCount()) {
                                }
                                rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i2);
                                return rv;
                            }
                        } catch (Throwable th3) {
                            e = th3;
                            FileLog.e(e);
                            message = this.messageObjects.get(id.longValue());
                            dialog = this.dialogs.get(id.longValue());
                            if (message == null) {
                            }
                            int i22 = 8;
                            if (dialog == null) {
                            }
                            rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                            Bundle extras222 = new Bundle();
                            if (DialogObject.isUserDialog(id.longValue())) {
                            }
                            extras222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                            Intent fillInIntent222 = new Intent();
                            fillInIntent222.putExtras(extras222);
                            rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent222);
                            if (position != getCount()) {
                            }
                            rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i22);
                            return rv;
                        }
                    }
                    canvas.setBitmap(null);
                    rv.setImageViewBitmap(org.telegram.messenger.beta.R.id.shortcut_widget_item_avatar, result);
                } catch (Throwable th4) {
                    e = th4;
                }
                message = this.messageObjects.get(id.longValue());
                dialog = this.dialogs.get(id.longValue());
                if (message == null) {
                    if (dialog != null && dialog.last_message_date != 0) {
                        rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(dialog.last_message_date));
                    } else {
                        rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_time, "");
                    }
                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, "");
                } else {
                    TLRPC.Chat fromChat = null;
                    long fromId = message.getFromChatId();
                    if (DialogObject.isUserDialog(fromId)) {
                        fromUser = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromId));
                    } else {
                        fromChat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromId));
                        fromUser = null;
                    }
                    int textColor5 = this.mContext.getResources().getColor(org.telegram.messenger.beta.R.color.widget_text);
                    if (message.messageOwner instanceof TLRPC.TL_messageService) {
                        if (ChatObject.isChannel(chat) && ((message.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear) || (message.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom))) {
                            messageString = "";
                        } else {
                            messageString = message.messageText;
                        }
                        textColor = this.mContext.getResources().getColor(org.telegram.messenger.beta.R.color.widget_action_text);
                    } else {
                        if (chat != null && fromChat == null) {
                            if (!ChatObject.isChannel(chat) || ChatObject.isMegagroup(chat)) {
                                if (message.isOutOwner()) {
                                    messageNameString = LocaleController.getString("FromYou", org.telegram.messenger.beta.R.string.FromYou);
                                } else if (fromUser != null) {
                                    messageNameString = UserObject.getFirstName(fromUser).replace("\n", "");
                                } else {
                                    messageNameString = "DELETED";
                                }
                                if (message.caption != null) {
                                    String mess = message.caption.toString();
                                    if (mess.length() > 150) {
                                        mess = mess.substring(0, 150);
                                    }
                                    if (message.isVideo()) {
                                        emoji2 = "📹 ";
                                    } else if (message.isVoice()) {
                                        emoji2 = "🎤 ";
                                    } else if (message.isMusic()) {
                                        emoji2 = "🎧 ";
                                    } else if (message.isPhoto()) {
                                        emoji2 = "🖼 ";
                                    } else {
                                        emoji2 = "📎 ";
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(emoji2);
                                    String emoji3 = mess.replace('\n', ' ');
                                    sb.append(emoji3);
                                    stringBuilder = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", sb.toString(), messageNameString));
                                    textColor2 = textColor5;
                                } else if (message.messageOwner.media != null && !message.isMediaEmpty()) {
                                    int textColor6 = this.mContext.getResources().getColor(org.telegram.messenger.beta.R.color.widget_action_text);
                                    if (message.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                        TLRPC.TL_messageMediaPoll mediaPoll = (TLRPC.TL_messageMediaPoll) message.messageOwner.media;
                                        innerMessage = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", mediaPoll.poll.question) : String.format("📊 %s", mediaPoll.poll.question);
                                    } else if (message.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                        innerMessage = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", message.messageOwner.media.game.title) : String.format("🎮 %s", message.messageOwner.media.game.title);
                                    } else if (message.type == 14) {
                                        innerMessage = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", message.getMusicAuthor(), message.getMusicTitle()) : String.format("🎧 %s - %s", message.getMusicAuthor(), message.getMusicTitle());
                                    } else {
                                        innerMessage = message.messageText.toString();
                                    }
                                    stringBuilder = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", innerMessage.replace('\n', ' '), messageNameString));
                                    try {
                                        try {
                                            textColor4 = textColor6;
                                        } catch (Exception e3) {
                                            e2 = e3;
                                            textColor4 = textColor6;
                                        }
                                    } catch (Exception e4) {
                                        e2 = e4;
                                        textColor4 = textColor6;
                                    }
                                    try {
                                        stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), messageNameString.length() + 2, stringBuilder.length(), 33);
                                    } catch (Exception e5) {
                                        e2 = e5;
                                        FileLog.e(e2);
                                        textColor2 = textColor4;
                                        textColor3 = textColor2;
                                        stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, messageNameString.length() + 1, 33);
                                        messageString = stringBuilder;
                                        textColor = textColor3;
                                        rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(message.messageOwner.date));
                                        rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, messageString.toString());
                                        rv.setTextColor(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, textColor);
                                        int i222 = 8;
                                        if (dialog == null) {
                                        }
                                        rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                                        Bundle extras2222 = new Bundle();
                                        if (DialogObject.isUserDialog(id.longValue())) {
                                        }
                                        extras2222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                        Intent fillInIntent2222 = new Intent();
                                        fillInIntent2222.putExtras(extras2222);
                                        rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent2222);
                                        if (position != getCount()) {
                                        }
                                        rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i222);
                                        return rv;
                                    }
                                    textColor2 = textColor4;
                                } else if (message.messageOwner.message != null) {
                                    String mess2 = message.messageOwner.message;
                                    if (mess2.length() <= 150) {
                                        c = 0;
                                    } else {
                                        c = 0;
                                        mess2 = mess2.substring(0, 150);
                                    }
                                    Object[] objArr = new Object[2];
                                    objArr[c] = mess2.replace('\n', ' ').trim();
                                    objArr[1] = messageNameString;
                                    stringBuilder = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                    textColor2 = textColor5;
                                } else {
                                    stringBuilder = SpannableStringBuilder.valueOf("");
                                    textColor2 = textColor5;
                                }
                                try {
                                    textColor3 = textColor2;
                                } catch (Exception e6) {
                                    e = e6;
                                    textColor3 = textColor2;
                                }
                                try {
                                    stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, messageNameString.length() + 1, 33);
                                } catch (Exception e7) {
                                    e = e7;
                                    FileLog.e(e);
                                    messageString = stringBuilder;
                                    textColor = textColor3;
                                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(message.messageOwner.date));
                                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, messageString.toString());
                                    rv.setTextColor(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, textColor);
                                    int i2222 = 8;
                                    if (dialog == null) {
                                    }
                                    rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                                    Bundle extras22222 = new Bundle();
                                    if (DialogObject.isUserDialog(id.longValue())) {
                                    }
                                    extras22222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                    Intent fillInIntent22222 = new Intent();
                                    fillInIntent22222.putExtras(extras22222);
                                    rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent22222);
                                    if (position != getCount()) {
                                    }
                                    rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i2222);
                                    return rv;
                                }
                                messageString = stringBuilder;
                                textColor = textColor3;
                            }
                        }
                        if ((message.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && (message.messageOwner.media.photo instanceof TLRPC.TL_photoEmpty) && message.messageOwner.media.ttl_seconds != 0) {
                            messageString = LocaleController.getString("AttachPhotoExpired", org.telegram.messenger.beta.R.string.AttachPhotoExpired);
                            textColor = textColor5;
                        } else if ((message.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) && (message.messageOwner.media.document instanceof TLRPC.TL_documentEmpty) && message.messageOwner.media.ttl_seconds != 0) {
                            messageString = LocaleController.getString("AttachVideoExpired", org.telegram.messenger.beta.R.string.AttachVideoExpired);
                            textColor = textColor5;
                        } else {
                            CharSequence messageString2 = message.caption;
                            if (messageString2 != null) {
                                if (message.isVideo()) {
                                    emoji = "📹 ";
                                } else if (message.isVoice()) {
                                    emoji = "🎤 ";
                                } else if (message.isMusic()) {
                                    emoji = "🎧 ";
                                } else if (message.isPhoto()) {
                                    emoji = "🖼 ";
                                } else {
                                    emoji = "📎 ";
                                }
                                messageString = emoji + ((Object) message.caption);
                                textColor = textColor5;
                            } else {
                                if (message.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                    messageString = "📊 " + ((TLRPC.TL_messageMediaPoll) message.messageOwner.media).poll.question;
                                } else if (message.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                    messageString = "🎮 " + message.messageOwner.media.game.title;
                                } else if (message.type == 14) {
                                    messageString = String.format("🎧 %s - %s", message.getMusicAuthor(), message.getMusicTitle());
                                } else {
                                    messageString = message.messageText;
                                    AndroidUtilities.highlightText(messageString, message.highlightedWords, (Theme.ResourcesProvider) null);
                                }
                                if (message.messageOwner.media != null && !message.isMediaEmpty()) {
                                    textColor = this.mContext.getResources().getColor(org.telegram.messenger.beta.R.color.widget_action_text);
                                } else {
                                    textColor = textColor5;
                                }
                            }
                        }
                    }
                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(message.messageOwner.date));
                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, messageString.toString());
                    rv.setTextColor(org.telegram.messenger.beta.R.id.shortcut_widget_item_message, textColor);
                }
                int i22222 = 8;
                if (dialog == null && dialog.unread_count > 0) {
                    rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, String.format("%d", Integer.valueOf(dialog.unread_count)));
                    rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 0);
                    if (this.accountInstance.getMessagesController().isDialogMuted(dialog.id)) {
                        rv.setBoolean(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, "setEnabled", false);
                        rv.setInt(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, "setBackgroundResource", org.telegram.messenger.beta.R.drawable.widget_badge_muted_background);
                    } else {
                        rv.setBoolean(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, "setEnabled", true);
                        rv.setInt(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, "setBackgroundResource", org.telegram.messenger.beta.R.drawable.widget_badge_background);
                    }
                } else {
                    rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
                }
                Bundle extras222222 = new Bundle();
                if (DialogObject.isUserDialog(id.longValue())) {
                    extras222222.putLong("userId", id.longValue());
                } else {
                    extras222222.putLong("chatId", -id.longValue());
                }
                extras222222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                Intent fillInIntent222222 = new Intent();
                fillInIntent222222.putExtras(extras222222);
                rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent222222);
                if (position != getCount()) {
                    i22222 = 0;
                }
                rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i22222);
                return rv;
            }
            chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-id.longValue()));
            if (chat2 == null) {
                chat = chat2;
                user = null;
                photoPath = null;
                name = "";
            } else {
                name2 = chat2.title;
                if (chat2.photo != null && chat2.photo.photo_small != null && chat2.photo.photo_small.volume_id != 0 && chat2.photo.photo_small.local_id != 0) {
                    TLRPC.FileLocation photoPath2 = chat2.photo.photo_small;
                    chat = chat2;
                    user = null;
                    photoPath = photoPath2;
                    name = name2;
                }
                chat = chat2;
                user = user2;
                photoPath = null;
                name = name2;
            }
            rv = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.shortcut_widget_item);
            rv.setTextViewText(org.telegram.messenger.beta.R.id.shortcut_widget_item_text, name);
            bitmap = null;
            if (photoPath != null) {
            }
            int size2 = AndroidUtilities.dp(48.0f);
            Bitmap result2 = Bitmap.createBitmap(size2, size2, Bitmap.Config.ARGB_8888);
            result2.eraseColor(0);
            Canvas canvas2 = new Canvas(result2);
            if (bitmap != null) {
            }
            canvas2.setBitmap(null);
            rv.setImageViewBitmap(org.telegram.messenger.beta.R.id.shortcut_widget_item_avatar, result2);
            message = this.messageObjects.get(id.longValue());
            dialog = this.dialogs.get(id.longValue());
            if (message == null) {
            }
            int i222222 = 8;
            if (dialog == null) {
            }
            rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_badge, 8);
            Bundle extras2222222 = new Bundle();
            if (DialogObject.isUserDialog(id.longValue())) {
            }
            extras2222222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent fillInIntent2222222 = new Intent();
            fillInIntent2222222.putExtras(extras2222222);
            rv.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.shortcut_widget_item, fillInIntent2222222);
            if (position != getCount()) {
            }
            rv.setViewVisibility(org.telegram.messenger.beta.R.id.shortcut_widget_item_divider, i222222);
            return rv;
        }
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
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDataSetChanged() {
        this.dids.clear();
        this.messageObjects.clear();
        AccountInstance accountInstance = this.accountInstance;
        if (accountInstance == null || !accountInstance.getUserConfig().isClientActivated()) {
            return;
        }
        ArrayList<TLRPC.User> users = new ArrayList<>();
        ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        LongSparseArray<TLRPC.Message> messages = new LongSparseArray<>();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 0, this.dids, this.dialogs, messages, users, chats);
        this.accountInstance.getMessagesController().putUsers(users, true);
        this.accountInstance.getMessagesController().putChats(chats, true);
        this.messageObjects.clear();
        int N = messages.size();
        for (int a = 0; a < N; a++) {
            MessageObject messageObject = new MessageObject(this.accountInstance.getCurrentAccount(), messages.valueAt(a), (LongSparseArray<TLRPC.User>) null, (LongSparseArray<TLRPC.Chat>) null, false, true);
            this.messageObjects.put(messages.keyAt(a), messageObject);
        }
    }
}
