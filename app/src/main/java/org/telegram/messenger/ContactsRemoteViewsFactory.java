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
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import androidx.collection.LongSparseArray;
import java.io.File;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
/* compiled from: ContactsWidgetService.java */
/* loaded from: classes4.dex */
class ContactsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AccountInstance accountInstance;
    private int appWidgetId;
    private RectF bitmapRect;
    private boolean deleted;
    private Context mContext;
    private Paint roundPaint;
    private ArrayList<Long> dids = new ArrayList<>();
    private LongSparseArray<TLRPC.Dialog> dialogs = new LongSparseArray<>();

    public ContactsRemoteViewsFactory(Context context, Intent intent) {
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
        int count = (int) Math.ceil(this.dids.size() / 2.0f);
        return count + 1;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(26:24|(1:26)(2:27|(1:29)(2:30|(1:32)(1:33)))|34|(19:47|(1:62)(1:63)|64|(3:137|66|67)|70|139|71|(3:(2:74|(1:76)(2:77|(1:79)))(1:80)|81|82)(10:83|84|135|85|(2:141|87)|90|91|92|143|93)|94|(1:96)(1:97)|98|105|(3:120|(1:122)|123)(6:109|(1:111)(1:112)|(1:114)(1:115)|116|(1:118)|119)|124|(1:126)(1:127)|128|(1:130)(1:131)|132|146)|59|(0)(0)|64|(0)|70|139|71|(0)(0)|94|(0)(0)|98|105|(1:107)|120|(0)|123|124|(0)(0)|128|(0)(0)|132|146) */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0265, code lost:
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0281  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02e0  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x018d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01ef A[Catch: all -> 0x0265, TRY_ENTER, TRY_LEAVE, TryCatch #2 {all -> 0x0265, blocks: (B:71:0x01aa, B:83:0x01ef), top: B:139:0x01aa }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0258  */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RemoteViews getViewAt(int position) {
        String name;
        TLRPC.FileLocation photoPath;
        Bitmap bitmap;
        TLRPC.Dialog dialog;
        int i;
        AvatarDrawable avatarDrawable;
        String name2;
        int i2 = position;
        if (this.deleted) {
            RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.widget_deleted);
            rv.setTextViewText(org.telegram.messenger.beta.R.id.widget_deleted_text, LocaleController.getString("WidgetLoggedOff", org.telegram.messenger.beta.R.string.WidgetLoggedOff));
            return rv;
        }
        boolean z = true;
        if (i2 >= getCount() - 1) {
            RemoteViews rv2 = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.widget_edititem);
            rv2.setTextViewText(org.telegram.messenger.beta.R.id.widget_edititem_text, LocaleController.getString("TapToEditWidgetShort", org.telegram.messenger.beta.R.string.TapToEditWidgetShort));
            Bundle extras = new Bundle();
            extras.putInt("appWidgetId", this.appWidgetId);
            extras.putInt("appWidgetType", 1);
            extras.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv2.setOnClickFillInIntent(org.telegram.messenger.beta.R.id.widget_edititem, fillInIntent);
            return rv2;
        }
        RemoteViews rv3 = new RemoteViews(this.mContext.getPackageName(), (int) org.telegram.messenger.beta.R.layout.contacts_widget_item);
        int a = 0;
        while (a < 2) {
            int num = (i2 * 2) + a;
            if (num >= this.dids.size()) {
                rv3.setViewVisibility(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, 4);
            } else {
                rv3.setViewVisibility(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, 0);
                Long id = this.dids.get((i2 * 2) + a);
                TLRPC.User user = null;
                TLRPC.Chat chat = null;
                if (DialogObject.isUserDialog(id.longValue())) {
                    user = this.accountInstance.getMessagesController().getUser(id);
                    if (UserObject.isUserSelf(user)) {
                        name2 = LocaleController.getString("SavedMessages", org.telegram.messenger.beta.R.string.SavedMessages);
                    } else if (UserObject.isReplyUser(user)) {
                        name2 = LocaleController.getString("RepliesTitle", org.telegram.messenger.beta.R.string.RepliesTitle);
                    } else if (UserObject.isDeleted(user)) {
                        name2 = LocaleController.getString("HiddenName", org.telegram.messenger.beta.R.string.HiddenName);
                    } else {
                        name2 = UserObject.getFirstName(user);
                    }
                    if (!UserObject.isReplyUser(user) && !UserObject.isUserSelf(user) && user != null && user.photo != null && user.photo.photo_small != null && user.photo.photo_small.volume_id != 0 && user.photo.photo_small.local_id != 0) {
                        name = name2;
                        photoPath = user.photo.photo_small;
                        rv3.setTextViewText(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_text1 : org.telegram.messenger.beta.R.id.contacts_widget_item_text2, name);
                        bitmap = null;
                        if (photoPath != null) {
                            try {
                                File path = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoPath, z);
                                bitmap = BitmapFactory.decodeFile(path.toString());
                            } catch (Throwable th) {
                                e = th;
                                FileLog.e(e);
                                dialog = this.dialogs.get(id.longValue());
                                i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                                if (dialog == null) {
                                }
                                if (a != 0) {
                                }
                                rv3.setViewVisibility(i, 8);
                                Bundle extras2 = new Bundle();
                                if (!DialogObject.isUserDialog(id.longValue())) {
                                }
                                extras2.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                Intent fillInIntent2 = new Intent();
                                fillInIntent2.putExtras(extras2);
                                rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent2);
                                a++;
                                i2 = position;
                                z = true;
                            }
                        }
                        int size = AndroidUtilities.dp(48.0f);
                        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                        result.eraseColor(0);
                        Canvas canvas = new Canvas(result);
                        if (bitmap == null) {
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
                            try {
                                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                                if (this.roundPaint == null) {
                                    try {
                                        this.roundPaint = new Paint(1);
                                        this.bitmapRect = new RectF();
                                    } catch (Throwable th2) {
                                        e = th2;
                                        FileLog.e(e);
                                        dialog = this.dialogs.get(id.longValue());
                                        i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                                        if (dialog == null) {
                                        }
                                        if (a != 0) {
                                        }
                                        rv3.setViewVisibility(i, 8);
                                        Bundle extras22 = new Bundle();
                                        if (!DialogObject.isUserDialog(id.longValue())) {
                                        }
                                        extras22.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                        Intent fillInIntent22 = new Intent();
                                        fillInIntent22.putExtras(extras22);
                                        rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent22);
                                        a++;
                                        i2 = position;
                                        z = true;
                                    }
                                }
                                float scale = size / bitmap.getWidth();
                                canvas.save();
                                canvas.scale(scale, scale);
                                this.roundPaint.setShader(shader);
                            } catch (Throwable th3) {
                                e = th3;
                            }
                            try {
                                this.bitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
                                canvas.drawRoundRect(this.bitmapRect, bitmap.getWidth(), bitmap.getHeight(), this.roundPaint);
                                canvas.restore();
                            } catch (Throwable th4) {
                                e = th4;
                                FileLog.e(e);
                                dialog = this.dialogs.get(id.longValue());
                                i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                                if (dialog == null) {
                                }
                                if (a != 0) {
                                }
                                rv3.setViewVisibility(i, 8);
                                Bundle extras222 = new Bundle();
                                if (!DialogObject.isUserDialog(id.longValue())) {
                                }
                                extras222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                                Intent fillInIntent222 = new Intent();
                                fillInIntent222.putExtras(extras222);
                                rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent222);
                                a++;
                                i2 = position;
                                z = true;
                            }
                        }
                        canvas.setBitmap(null);
                        rv3.setImageViewBitmap(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_avatar1 : org.telegram.messenger.beta.R.id.contacts_widget_item_avatar2, result);
                        dialog = this.dialogs.get(id.longValue());
                        i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                        if (dialog == null && dialog.unread_count > 0) {
                            String count = dialog.unread_count > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(dialog.unread_count));
                            rv3.setTextViewText(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_badge1 : org.telegram.messenger.beta.R.id.contacts_widget_item_badge2, count);
                            if (a != 0) {
                                i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg2;
                            }
                            rv3.setViewVisibility(i, 0);
                        } else {
                            if (a != 0) {
                                i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg2;
                            }
                            rv3.setViewVisibility(i, 8);
                        }
                        Bundle extras2222 = new Bundle();
                        if (!DialogObject.isUserDialog(id.longValue())) {
                            extras2222.putLong("userId", id.longValue());
                        } else {
                            extras2222.putLong("chatId", -id.longValue());
                        }
                        extras2222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                        Intent fillInIntent2222 = new Intent();
                        fillInIntent2222.putExtras(extras2222);
                        rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent2222);
                    }
                    name = name2;
                    photoPath = null;
                    rv3.setTextViewText(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_text1 : org.telegram.messenger.beta.R.id.contacts_widget_item_text2, name);
                    bitmap = null;
                    if (photoPath != null) {
                    }
                    int size2 = AndroidUtilities.dp(48.0f);
                    Bitmap result2 = Bitmap.createBitmap(size2, size2, Bitmap.Config.ARGB_8888);
                    result2.eraseColor(0);
                    Canvas canvas2 = new Canvas(result2);
                    if (bitmap == null) {
                    }
                    canvas2.setBitmap(null);
                    rv3.setImageViewBitmap(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_avatar1 : org.telegram.messenger.beta.R.id.contacts_widget_item_avatar2, result2);
                    dialog = this.dialogs.get(id.longValue());
                    i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                    if (dialog == null) {
                    }
                    if (a != 0) {
                    }
                    rv3.setViewVisibility(i, 8);
                    Bundle extras22222 = new Bundle();
                    if (!DialogObject.isUserDialog(id.longValue())) {
                    }
                    extras22222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                    Intent fillInIntent22222 = new Intent();
                    fillInIntent22222.putExtras(extras22222);
                    rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent22222);
                } else {
                    chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-id.longValue()));
                    if (chat != null) {
                        name2 = chat.title;
                        if (chat.photo != null && chat.photo.photo_small != null && chat.photo.photo_small.volume_id != 0 && chat.photo.photo_small.local_id != 0) {
                            name = name2;
                            photoPath = chat.photo.photo_small;
                        }
                        name = name2;
                        photoPath = null;
                    } else {
                        name = "";
                        photoPath = null;
                    }
                    rv3.setTextViewText(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_text1 : org.telegram.messenger.beta.R.id.contacts_widget_item_text2, name);
                    bitmap = null;
                    if (photoPath != null) {
                    }
                    int size22 = AndroidUtilities.dp(48.0f);
                    Bitmap result22 = Bitmap.createBitmap(size22, size22, Bitmap.Config.ARGB_8888);
                    result22.eraseColor(0);
                    Canvas canvas22 = new Canvas(result22);
                    if (bitmap == null) {
                    }
                    canvas22.setBitmap(null);
                    rv3.setImageViewBitmap(a == 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item_avatar1 : org.telegram.messenger.beta.R.id.contacts_widget_item_avatar2, result22);
                    dialog = this.dialogs.get(id.longValue());
                    i = org.telegram.messenger.beta.R.id.contacts_widget_item_badge_bg1;
                    if (dialog == null) {
                    }
                    if (a != 0) {
                    }
                    rv3.setViewVisibility(i, 8);
                    Bundle extras222222 = new Bundle();
                    if (!DialogObject.isUserDialog(id.longValue())) {
                    }
                    extras222222.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                    Intent fillInIntent222222 = new Intent();
                    fillInIntent222222.putExtras(extras222222);
                    rv3.setOnClickFillInIntent(a != 0 ? org.telegram.messenger.beta.R.id.contacts_widget_item1 : org.telegram.messenger.beta.R.id.contacts_widget_item2, fillInIntent222222);
                }
            }
            a++;
            i2 = position;
            z = true;
        }
        return rv3;
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
        AccountInstance accountInstance = this.accountInstance;
        if (accountInstance == null || !accountInstance.getUserConfig().isClientActivated()) {
            return;
        }
        ArrayList<TLRPC.User> users = new ArrayList<>();
        ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        LongSparseArray<TLRPC.Message> messages = new LongSparseArray<>();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 1, this.dids, this.dialogs, messages, users, chats);
        this.accountInstance.getMessagesController().putUsers(users, true);
        this.accountInstance.getMessagesController().putChats(chats, true);
    }
}
