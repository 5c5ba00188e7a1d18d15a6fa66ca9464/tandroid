package org.telegram.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.C;
import com.microsoft.appcenter.ingestion.models.CommonProperties;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatsWidgetProvider;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsWidgetProvider;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.InviteMembersBottomSheet;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.EditWidgetActivity;
/* loaded from: classes4.dex */
public class EditWidgetActivity extends BaseFragment {
    public static final int TYPE_CHATS = 0;
    public static final int TYPE_CONTACTS = 1;
    private static final int done_item = 1;
    private int chatsEndRow;
    private int chatsStartRow;
    private int currentWidgetId;
    private EditWidgetActivityDelegate delegate;
    private int infoRow;
    private ItemTouchHelper itemTouchHelper;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ImageView previewImageView;
    private int previewRow;
    private int rowCount;
    private int selectChatsRow;
    private ArrayList<Long> selectedDialogs = new ArrayList<>();
    private FrameLayout widgetPreview;
    private WidgetPreviewCell widgetPreviewCell;
    private int widgetType;

    /* loaded from: classes4.dex */
    public interface EditWidgetActivityDelegate {
        void didSelectDialogs(ArrayList<Long> arrayList);
    }

    /* loaded from: classes4.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        private boolean moved;

        public TouchHelperCallback() {
            EditWidgetActivity.this = this$0;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 3) {
                return makeMovementFlags(0, 0);
            }
            return makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            boolean z = false;
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            int p1 = source.getAdapterPosition();
            int p2 = target.getAdapterPosition();
            if (EditWidgetActivity.this.listAdapter.swapElements(p1, p2)) {
                ((GroupCreateUserCell) source.itemView).setDrawDivider(p2 != EditWidgetActivity.this.chatsEndRow - 1);
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) target.itemView;
                if (p1 != EditWidgetActivity.this.chatsEndRow - 1) {
                    z = true;
                }
                groupCreateUserCell.setDrawDivider(z);
                this.moved = true;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != 0) {
                EditWidgetActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            } else if (this.moved) {
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
                this.moved = false;
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    /* loaded from: classes4.dex */
    public class WidgetPreviewCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
        private Drawable oldBackgroundDrawable;
        private BackgroundGradientDrawable.Disposable oldBackgroundGradientDisposable;
        private Drawable shadowDrawable;
        private Paint roundPaint = new Paint(1);
        private RectF bitmapRect = new RectF();
        private ViewGroup[] cells = new ViewGroup[2];

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WidgetPreviewCell(Context context) {
            super(context);
            EditWidgetActivity.this = this$0;
            setWillNotDraw(false);
            setPadding(0, AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
            ChatActionCell chatActionCell = new ChatActionCell(context);
            chatActionCell.setCustomText(LocaleController.getString("WidgetPreview", R.string.WidgetPreview));
            linearLayout.addView(chatActionCell, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 4));
            LinearLayout widgetPreview = new LinearLayout(context);
            widgetPreview.setOrientation(1);
            widgetPreview.setBackgroundResource(R.drawable.widget_bg);
            linearLayout.addView(widgetPreview, LayoutHelper.createLinear(-2, -2, 17, 10, 0, 10, 0));
            this$0.previewImageView = new ImageView(context);
            if (this$0.widgetType != 0) {
                if (this$0.widgetType == 1) {
                    for (int a = 0; a < 2; a++) {
                        this.cells[a] = (ViewGroup) this$0.getParentActivity().getLayoutInflater().inflate(R.layout.contacts_widget_item, (ViewGroup) null);
                        widgetPreview.addView(this.cells[a], LayoutHelper.createLinear(160, -2));
                    }
                    widgetPreview.addView(this$0.previewImageView, LayoutHelper.createLinear(160, 160, 17));
                    this$0.previewImageView.setImageResource(R.drawable.contacts_widget_preview);
                }
            } else {
                for (int a2 = 0; a2 < 2; a2++) {
                    this.cells[a2] = (ViewGroup) this$0.getParentActivity().getLayoutInflater().inflate(R.layout.shortcut_widget_item, (ViewGroup) null);
                    widgetPreview.addView(this.cells[a2], LayoutHelper.createLinear(-1, -2));
                }
                widgetPreview.addView(this$0.previewImageView, LayoutHelper.createLinear(218, 160, 17));
                this$0.previewImageView.setImageResource(R.drawable.chats_widget_preview);
            }
            updateDialogs();
            this.shadowDrawable = Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
        }

        /* JADX WARN: Code restructure failed: missing block: B:293:0x0853, code lost:
            if (r3 != 2) goto L296;
         */
        /* JADX WARN: Removed duplicated region for block: B:246:0x06c0  */
        /* JADX WARN: Removed duplicated region for block: B:250:0x0716  */
        /* JADX WARN: Removed duplicated region for block: B:337:0x0939  */
        /* JADX WARN: Removed duplicated region for block: B:338:0x093d  */
        /* JADX WARN: Removed duplicated region for block: B:348:0x0983  */
        /* JADX WARN: Removed duplicated region for block: B:358:0x09b4 A[Catch: all -> 0x0a28, TRY_ENTER, TRY_LEAVE, TryCatch #5 {all -> 0x0a28, blocks: (B:346:0x096e, B:358:0x09b4), top: B:418:0x096e }] */
        /* JADX WARN: Removed duplicated region for block: B:368:0x0a06  */
        /* JADX WARN: Removed duplicated region for block: B:369:0x0a0a  */
        /* JADX WARN: Removed duplicated region for block: B:383:0x0a37  */
        /* JADX WARN: Removed duplicated region for block: B:396:0x0a8e  */
        /* JADX WARN: Removed duplicated region for block: B:412:0x094c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r11v0 */
        /* JADX WARN: Type inference failed for: r11v17, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r11v18 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateDialogs() {
            TLRPC.Dialog dialog;
            String str;
            String name;
            TLRPC.Chat chat;
            TLRPC.User user;
            TLRPC.FileLocation photoPath;
            Bitmap bitmap;
            Canvas canvas;
            AvatarDrawable avatarDrawable;
            String name2;
            TLRPC.Dialog dialog2;
            int a;
            String str2;
            TLRPC.User user2;
            TLRPC.Chat chat2;
            String name3;
            TLRPC.User fromUser;
            int textColor;
            CharSequence messageString;
            int textColor2;
            CharSequence messageString2;
            String emoji;
            CharSequence messageNameString;
            int textColor3;
            SpannableStringBuilder stringBuilder;
            int textColor4;
            Exception e;
            char c;
            String innerMessage;
            int textColor5;
            Exception e2;
            String emoji2;
            CharSequence messageString3;
            Canvas canvas2;
            AvatarDrawable avatarDrawable2;
            String name4;
            int i = EditWidgetActivity.this.widgetType;
            float f = 48.0f;
            int i2 = R.string.SavedMessages;
            String str3 = "SavedMessages";
            long j = 0;
            int i3 = 8;
            int i4 = 2;
            ?? r11 = 1;
            int i5 = 0;
            if (i == 0) {
                int a2 = 0;
                while (a2 < i4) {
                    if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                        if (a2 < EditWidgetActivity.this.selectedDialogs.size()) {
                            TLRPC.Dialog dialog3 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(a2)).longValue());
                            if (dialog3 != null) {
                                dialog2 = dialog3;
                            } else {
                                TLRPC.Dialog dialog4 = new TLRPC.TL_dialog();
                                dialog4.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(a2)).longValue();
                                dialog2 = dialog4;
                            }
                        } else {
                            dialog2 = null;
                        }
                    } else {
                        dialog2 = a2 < EditWidgetActivity.this.getMessagesController().dialogsServerOnly.size() ? EditWidgetActivity.this.getMessagesController().dialogsServerOnly.get(a2) : null;
                    }
                    if (dialog2 == null) {
                        this.cells[a2].setVisibility(i3);
                        str2 = str3;
                        a = a2;
                    } else {
                        this.cells[a2].setVisibility(i5);
                        TLRPC.FileLocation photoPath2 = null;
                        if (DialogObject.isUserDialog(dialog2.id)) {
                            user2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog2.id));
                            if (user2 == null) {
                                str2 = str3;
                                name3 = "";
                                chat2 = null;
                            } else {
                                if (UserObject.isUserSelf(user2)) {
                                    name4 = LocaleController.getString(str3, i2);
                                } else if (UserObject.isReplyUser(user2)) {
                                    name4 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                } else if (UserObject.isDeleted(user2)) {
                                    name4 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                } else {
                                    name4 = ContactsController.formatName(user2.first_name, user2.last_name);
                                }
                                if (UserObject.isReplyUser(user2) || UserObject.isUserSelf(user2) || user2.photo == null || user2.photo.photo_small == null) {
                                    str2 = str3;
                                } else {
                                    str2 = str3;
                                    if (user2.photo.photo_small.volume_id != j && user2.photo.photo_small.local_id != 0) {
                                        photoPath2 = user2.photo.photo_small;
                                        name3 = name4;
                                        chat2 = null;
                                    }
                                }
                                name3 = name4;
                                chat2 = null;
                            }
                        } else {
                            str2 = str3;
                            TLRPC.Chat chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog2.id));
                            if (chat3 == null) {
                                chat2 = chat3;
                                user2 = null;
                                name3 = "";
                            } else {
                                String name5 = chat3.title;
                                if (chat3.photo != null && chat3.photo.photo_small != null && chat3.photo.photo_small.volume_id != j && chat3.photo.photo_small.local_id != 0) {
                                    photoPath2 = chat3.photo.photo_small;
                                    chat2 = chat3;
                                    user2 = null;
                                    name3 = name5;
                                } else {
                                    chat2 = chat3;
                                    user2 = null;
                                    name3 = name5;
                                }
                            }
                        }
                        ((TextView) this.cells[a2].findViewById(R.id.shortcut_widget_item_text)).setText(name3);
                        Bitmap bitmap2 = null;
                        if (photoPath2 != null) {
                            try {
                                File path = EditWidgetActivity.this.getFileLoader().getPathToAttach(photoPath2, r11);
                                bitmap2 = BitmapFactory.decodeFile(path.toString());
                            } catch (Throwable e3) {
                                FileLog.e(e3);
                            }
                        }
                        int size = AndroidUtilities.dp(f);
                        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                        result.eraseColor(i5);
                        Canvas canvas3 = new Canvas(result);
                        if (bitmap2 == null) {
                            if (user2 != null) {
                                avatarDrawable2 = new AvatarDrawable(user2);
                                if (UserObject.isReplyUser(user2)) {
                                    avatarDrawable2.setAvatarType(12);
                                } else if (UserObject.isUserSelf(user2)) {
                                    int i6 = r11 == true ? 1 : 0;
                                    int i7 = r11 == true ? 1 : 0;
                                    avatarDrawable2.setAvatarType(i6);
                                }
                            } else {
                                avatarDrawable2 = new AvatarDrawable(chat2);
                            }
                            avatarDrawable2.setBounds(i5, i5, size, size);
                            avatarDrawable2.draw(canvas3);
                            canvas2 = canvas3;
                        } else {
                            BitmapShader shader = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                            if (this.roundPaint == null) {
                                this.roundPaint = new Paint((int) r11);
                                this.bitmapRect = new RectF();
                            }
                            float scale = size / bitmap2.getWidth();
                            canvas3.save();
                            canvas3.scale(scale, scale);
                            this.roundPaint.setShader(shader);
                            this.bitmapRect.set(0.0f, 0.0f, bitmap2.getWidth(), bitmap2.getHeight());
                            canvas2 = canvas3;
                            canvas2.drawRoundRect(this.bitmapRect, bitmap2.getWidth(), bitmap2.getHeight(), this.roundPaint);
                            canvas2.restore();
                        }
                        canvas2.setBitmap(null);
                        ((ImageView) this.cells[a2].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(result);
                        MessageObject message = EditWidgetActivity.this.getMessagesController().dialogMessage.get(dialog2.id);
                        if (message == null) {
                            a = a2;
                            if (dialog2.last_message_date == 0) {
                                ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_time)).setText("");
                            } else {
                                ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(dialog2.last_message_date));
                            }
                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setText("");
                        } else {
                            TLRPC.Chat fromChat = null;
                            long fromId = message.getFromChatId();
                            if (fromId > 0) {
                                fromUser = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromId));
                            } else {
                                fromChat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromId));
                                fromUser = null;
                            }
                            int textColor6 = getContext().getResources().getColor(R.color.widget_text);
                            if (message.messageOwner instanceof TLRPC.TL_messageService) {
                                if (ChatObject.isChannel(chat2) && ((message.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear) || (message.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom))) {
                                    messageString3 = "";
                                } else {
                                    messageString3 = message.messageText;
                                }
                                textColor = getContext().getResources().getColor(R.color.widget_action_text);
                                a = a2;
                                messageString = messageString3;
                            } else {
                                if (chat2 != null) {
                                    a = a2;
                                    if (chat2.id <= 0 || fromChat != null) {
                                        textColor2 = textColor6;
                                    } else if (!ChatObject.isChannel(chat2) || ChatObject.isMegagroup(chat2)) {
                                        if (message.isOutOwner()) {
                                            messageNameString = LocaleController.getString("FromYou", R.string.FromYou);
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
                                            textColor3 = textColor6;
                                        } else if (message.messageOwner.media != null && !message.isMediaEmpty()) {
                                            int textColor7 = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                textColor5 = textColor7;
                                                try {
                                                } catch (Exception e4) {
                                                    e2 = e4;
                                                }
                                                try {
                                                    stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), messageNameString.length() + 2, stringBuilder.length(), 33);
                                                } catch (Exception e5) {
                                                    e2 = e5;
                                                    FileLog.e(e2);
                                                    textColor3 = textColor5;
                                                    textColor4 = textColor3;
                                                    stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, messageNameString.length() + 1, 33);
                                                    messageString = stringBuilder;
                                                    textColor = textColor4;
                                                    ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(message.messageOwner.date));
                                                    ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setText(messageString.toString());
                                                    ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setTextColor(textColor);
                                                    if (dialog2.unread_count > 0) {
                                                    }
                                                    a2 = a + 1;
                                                    str3 = str2;
                                                    f = 48.0f;
                                                    i2 = R.string.SavedMessages;
                                                    j = 0;
                                                    i3 = 8;
                                                    i4 = 2;
                                                    r11 = 1;
                                                    i5 = 0;
                                                }
                                            } catch (Exception e6) {
                                                e2 = e6;
                                                textColor5 = textColor7;
                                            }
                                            textColor3 = textColor5;
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
                                            textColor3 = textColor6;
                                        } else {
                                            stringBuilder = SpannableStringBuilder.valueOf("");
                                            textColor3 = textColor6;
                                        }
                                        try {
                                            textColor4 = textColor3;
                                        } catch (Exception e7) {
                                            e = e7;
                                            textColor4 = textColor3;
                                        }
                                        try {
                                            stringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, messageNameString.length() + 1, 33);
                                        } catch (Exception e8) {
                                            e = e8;
                                            FileLog.e(e);
                                            messageString = stringBuilder;
                                            textColor = textColor4;
                                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(message.messageOwner.date));
                                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setText(messageString.toString());
                                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setTextColor(textColor);
                                            if (dialog2.unread_count > 0) {
                                            }
                                            a2 = a + 1;
                                            str3 = str2;
                                            f = 48.0f;
                                            i2 = R.string.SavedMessages;
                                            j = 0;
                                            i3 = 8;
                                            i4 = 2;
                                            r11 = 1;
                                            i5 = 0;
                                        }
                                        messageString = stringBuilder;
                                        textColor = textColor4;
                                    } else {
                                        textColor2 = textColor6;
                                    }
                                } else {
                                    textColor2 = textColor6;
                                    a = a2;
                                }
                                if ((message.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && (message.messageOwner.media.photo instanceof TLRPC.TL_photoEmpty) && message.messageOwner.media.ttl_seconds != 0) {
                                    textColor = textColor2;
                                    messageString = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                } else if ((message.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) && (message.messageOwner.media.document instanceof TLRPC.TL_documentEmpty) && message.messageOwner.media.ttl_seconds != 0) {
                                    textColor = textColor2;
                                    messageString = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                } else {
                                    CharSequence messageString4 = message.caption;
                                    if (messageString4 != null) {
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
                                        textColor = textColor2;
                                        messageString = emoji + ((Object) message.caption);
                                    } else {
                                        if (message.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                            messageString2 = "📊 " + ((TLRPC.TL_messageMediaPoll) message.messageOwner.media).poll.question;
                                        } else if (message.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                            messageString2 = "🎮 " + message.messageOwner.media.game.title;
                                        } else if (message.type == 14) {
                                            messageString2 = String.format("🎧 %s - %s", message.getMusicAuthor(), message.getMusicTitle());
                                        } else {
                                            messageString2 = message.messageText;
                                            AndroidUtilities.highlightText(messageString2, message.highlightedWords, (Theme.ResourcesProvider) null);
                                        }
                                        if (message.messageOwner.media != null && !message.isMediaEmpty()) {
                                            textColor = getContext().getResources().getColor(R.color.widget_action_text);
                                            messageString = messageString2;
                                        } else {
                                            textColor = textColor2;
                                            messageString = messageString2;
                                        }
                                    }
                                }
                            }
                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(message.messageOwner.date));
                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setText(messageString.toString());
                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_message)).setTextColor(textColor);
                        }
                        if (dialog2.unread_count > 0) {
                            ((TextView) this.cells[a].findViewById(R.id.shortcut_widget_item_badge)).setText(String.format("%d", Integer.valueOf(dialog2.unread_count)));
                            this.cells[a].findViewById(R.id.shortcut_widget_item_badge).setVisibility(0);
                            if (EditWidgetActivity.this.getMessagesController().isDialogMuted(dialog2.id)) {
                                this.cells[a].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter_muted);
                            } else {
                                this.cells[a].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter);
                            }
                        } else {
                            this.cells[a].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                        }
                    }
                    a2 = a + 1;
                    str3 = str2;
                    f = 48.0f;
                    i2 = R.string.SavedMessages;
                    j = 0;
                    i3 = 8;
                    i4 = 2;
                    r11 = 1;
                    i5 = 0;
                }
                this.cells[0].findViewById(R.id.shortcut_widget_item_divider).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(R.id.shortcut_widget_item_divider).setVisibility(8);
            } else {
                String str4 = str3;
                if (EditWidgetActivity.this.widgetType == 1) {
                    int position = 0;
                    while (true) {
                        if (position >= 2) {
                            break;
                        }
                        int a3 = 0;
                        for (int i8 = 2; a3 < i8; i8 = 2) {
                            int num = (position * 2) + a3;
                            if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                                if (num < EditWidgetActivity.this.selectedDialogs.size()) {
                                    TLRPC.Dialog dialog5 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(num)).longValue());
                                    if (dialog5 != null) {
                                        dialog = dialog5;
                                    } else {
                                        TLRPC.Dialog dialog6 = new TLRPC.TL_dialog();
                                        dialog6.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(num)).longValue();
                                        dialog = dialog6;
                                    }
                                } else {
                                    dialog = null;
                                }
                            } else if (num < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                long userId = EditWidgetActivity.this.getMediaDataController().hints.get(num).peer.user_id;
                                TLRPC.Dialog dialog7 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(userId);
                                if (dialog7 == null) {
                                    dialog7 = new TLRPC.TL_dialog();
                                    dialog7.id = userId;
                                }
                                dialog = dialog7;
                            } else {
                                dialog = null;
                            }
                            int i9 = R.id.contacts_widget_item1;
                            if (dialog == null) {
                                ViewGroup viewGroup = this.cells[position];
                                if (a3 != 0) {
                                    i9 = R.id.contacts_widget_item2;
                                }
                                viewGroup.findViewById(i9).setVisibility(4);
                                if (num == 0 || num == 2) {
                                    this.cells[position].setVisibility(8);
                                    str = str4;
                                } else {
                                    str = str4;
                                }
                            } else {
                                ViewGroup viewGroup2 = this.cells[position];
                                if (a3 != 0) {
                                    i9 = R.id.contacts_widget_item2;
                                }
                                viewGroup2.findViewById(i9).setVisibility(0);
                                if (num == 0) {
                                }
                                this.cells[position].setVisibility(0);
                                TLRPC.User user3 = null;
                                TLRPC.Chat chat4 = null;
                                if (DialogObject.isUserDialog(dialog.id)) {
                                    user3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog.id));
                                    if (UserObject.isUserSelf(user3)) {
                                        str = str4;
                                        name2 = LocaleController.getString(str, R.string.SavedMessages);
                                    } else {
                                        str = str4;
                                        if (UserObject.isReplyUser(user3)) {
                                            name2 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                        } else if (UserObject.isDeleted(user3)) {
                                            name2 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                        } else {
                                            name2 = UserObject.getFirstName(user3);
                                        }
                                    }
                                    if (!UserObject.isReplyUser(user3) && !UserObject.isUserSelf(user3) && user3 != null && user3.photo != null && user3.photo.photo_small != null && user3.photo.photo_small.volume_id != 0 && user3.photo.photo_small.local_id != 0) {
                                        name = name2;
                                        chat = null;
                                        user = user3;
                                        photoPath = user3.photo.photo_small;
                                        ((TextView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(name);
                                        bitmap = null;
                                        if (photoPath != null) {
                                        }
                                        int size2 = AndroidUtilities.dp(48.0f);
                                        Bitmap result2 = Bitmap.createBitmap(size2, size2, Bitmap.Config.ARGB_8888);
                                        result2.eraseColor(0);
                                        canvas = new Canvas(result2);
                                        if (bitmap != null) {
                                        }
                                        canvas.setBitmap(null);
                                        ((ImageView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(result2);
                                        if (dialog.unread_count > 0) {
                                        }
                                    } else {
                                        name = name2;
                                        chat = chat4;
                                        user = user3;
                                        photoPath = null;
                                        ((TextView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(name);
                                        bitmap = null;
                                        if (photoPath != null) {
                                            try {
                                                File path2 = EditWidgetActivity.this.getFileLoader().getPathToAttach(photoPath, true);
                                                bitmap = BitmapFactory.decodeFile(path2.toString());
                                            } catch (Throwable th) {
                                                e = th;
                                                FileLog.e(e);
                                                if (dialog.unread_count > 0) {
                                                }
                                                a3++;
                                                str4 = str;
                                            }
                                        }
                                        try {
                                            int size22 = AndroidUtilities.dp(48.0f);
                                            Bitmap result22 = Bitmap.createBitmap(size22, size22, Bitmap.Config.ARGB_8888);
                                            result22.eraseColor(0);
                                            canvas = new Canvas(result22);
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
                                                avatarDrawable.setBounds(0, 0, size22, size22);
                                                avatarDrawable.draw(canvas);
                                            } else {
                                                try {
                                                    BitmapShader shader2 = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                                                    float scale2 = size22 / bitmap.getWidth();
                                                    canvas.save();
                                                    canvas.scale(scale2, scale2);
                                                    this.roundPaint.setShader(shader2);
                                                    try {
                                                        try {
                                                            this.bitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
                                                            canvas.drawRoundRect(this.bitmapRect, bitmap.getWidth(), bitmap.getHeight(), this.roundPaint);
                                                            canvas.restore();
                                                        } catch (Throwable th2) {
                                                            e = th2;
                                                            FileLog.e(e);
                                                            if (dialog.unread_count > 0) {
                                                            }
                                                            a3++;
                                                            str4 = str;
                                                        }
                                                    } catch (Throwable th3) {
                                                        e = th3;
                                                    }
                                                } catch (Throwable th4) {
                                                    e = th4;
                                                }
                                            }
                                            try {
                                                canvas.setBitmap(null);
                                                ((ImageView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(result22);
                                            } catch (Throwable th5) {
                                                e = th5;
                                                FileLog.e(e);
                                                if (dialog.unread_count > 0) {
                                                }
                                                a3++;
                                                str4 = str;
                                            }
                                        } catch (Throwable th6) {
                                            e = th6;
                                        }
                                        if (dialog.unread_count > 0) {
                                            String count = dialog.unread_count > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(dialog.unread_count));
                                            ((TextView) this.cells[position].findViewById(a3 == 0 ? R.id.contacts_widget_item_badge1 : R.id.contacts_widget_item_badge2)).setText(count);
                                            this.cells[position].findViewById(a3 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(0);
                                        } else {
                                            this.cells[position].findViewById(a3 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(8);
                                        }
                                    }
                                } else {
                                    str = str4;
                                    chat4 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog.id));
                                    String name6 = chat4.title;
                                    if (chat4.photo != null && chat4.photo.photo_small != null) {
                                        if (chat4.photo.photo_small.volume_id != 0 && chat4.photo.photo_small.local_id != 0) {
                                            TLRPC.FileLocation photoPath3 = chat4.photo.photo_small;
                                            name = name6;
                                            chat = chat4;
                                            user = null;
                                            photoPath = photoPath3;
                                            ((TextView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(name);
                                            bitmap = null;
                                            if (photoPath != null) {
                                            }
                                            int size222 = AndroidUtilities.dp(48.0f);
                                            Bitmap result222 = Bitmap.createBitmap(size222, size222, Bitmap.Config.ARGB_8888);
                                            result222.eraseColor(0);
                                            canvas = new Canvas(result222);
                                            if (bitmap != null) {
                                            }
                                            canvas.setBitmap(null);
                                            ((ImageView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(result222);
                                            if (dialog.unread_count > 0) {
                                            }
                                        }
                                    }
                                    name = name6;
                                    chat = chat4;
                                    user = user3;
                                    photoPath = null;
                                    ((TextView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(name);
                                    bitmap = null;
                                    if (photoPath != null) {
                                    }
                                    int size2222 = AndroidUtilities.dp(48.0f);
                                    Bitmap result2222 = Bitmap.createBitmap(size2222, size2222, Bitmap.Config.ARGB_8888);
                                    result2222.eraseColor(0);
                                    canvas = new Canvas(result2222);
                                    if (bitmap != null) {
                                    }
                                    canvas.setBitmap(null);
                                    ((ImageView) this.cells[position].findViewById(a3 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(result2222);
                                    if (dialog.unread_count > 0) {
                                    }
                                }
                            }
                            a3++;
                            str4 = str;
                        }
                        position++;
                    }
                }
            }
            if (this.cells[0].getVisibility() == 0) {
                EditWidgetActivity.this.previewImageView.setVisibility(8);
            } else {
                EditWidgetActivity.this.previewImageView.setVisibility(0);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(264.0f), C.BUFFER_FLAG_ENCRYPTED));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int a;
            Drawable drawable;
            Drawable newDrawable = Theme.getCachedWallpaperNonBlocking();
            if (newDrawable != this.backgroundDrawable && newDrawable != null) {
                if (Theme.isAnimatingColor()) {
                    this.oldBackgroundDrawable = this.backgroundDrawable;
                    this.oldBackgroundGradientDisposable = this.backgroundGradientDisposable;
                } else {
                    BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                    if (disposable != null) {
                        disposable.dispose();
                        this.backgroundGradientDisposable = null;
                    }
                }
                this.backgroundDrawable = newDrawable;
            }
            float themeAnimationValue = EditWidgetActivity.this.parentLayout.getThemeAnimationValue();
            int a2 = 0;
            while (a2 < 2) {
                Drawable drawable2 = a2 == 0 ? this.oldBackgroundDrawable : this.backgroundDrawable;
                if (drawable2 == null) {
                    a = a2;
                } else {
                    if (a2 == 1 && this.oldBackgroundDrawable != null && EditWidgetActivity.this.parentLayout != null) {
                        drawable2.setAlpha((int) (255.0f * themeAnimationValue));
                    } else {
                        drawable2.setAlpha(255);
                    }
                    if ((drawable2 instanceof ColorDrawable) || (drawable2 instanceof GradientDrawable)) {
                        a = a2;
                    } else if (drawable2 instanceof MotionBackgroundDrawable) {
                        a = a2;
                    } else {
                        if (!(drawable2 instanceof BitmapDrawable)) {
                            a = a2;
                        } else {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable2;
                            if (bitmapDrawable.getTileModeX() == Shader.TileMode.REPEAT) {
                                canvas.save();
                                float scale = 2.0f / AndroidUtilities.density;
                                canvas.scale(scale, scale);
                                drawable2.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / scale), (int) Math.ceil(getMeasuredHeight() / scale));
                                a = a2;
                            } else {
                                int viewHeight = getMeasuredHeight();
                                float scaleX = getMeasuredWidth() / drawable2.getIntrinsicWidth();
                                float scaleY = viewHeight / drawable2.getIntrinsicHeight();
                                float scale2 = Math.max(scaleX, scaleY);
                                int width = (int) Math.ceil(drawable2.getIntrinsicWidth() * scale2);
                                a = a2;
                                int height = (int) Math.ceil(drawable2.getIntrinsicHeight() * scale2);
                                int x = (getMeasuredWidth() - width) / 2;
                                int y = (viewHeight - height) / 2;
                                canvas.save();
                                canvas.clipRect(0, 0, width, getMeasuredHeight());
                                drawable2.setBounds(x, y, x + width, y + height);
                            }
                            drawable2.draw(canvas);
                            canvas.restore();
                        }
                        if (a != 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                            BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
                            if (disposable2 == null) {
                                drawable = null;
                            } else {
                                disposable2.dispose();
                                drawable = null;
                                this.oldBackgroundGradientDisposable = null;
                            }
                            this.oldBackgroundDrawable = drawable;
                            invalidate();
                        }
                    }
                    drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    if (drawable2 instanceof BackgroundGradientDrawable) {
                        BackgroundGradientDrawable backgroundGradientDrawable = (BackgroundGradientDrawable) drawable2;
                        this.backgroundGradientDisposable = backgroundGradientDrawable.drawExactBoundsSize(canvas, this);
                    } else {
                        drawable2.draw(canvas);
                    }
                    if (a != 0) {
                    }
                }
                a2 = a + 1;
            }
            this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.backgroundGradientDisposable = null;
            }
            BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
            if (disposable2 != null) {
                disposable2.dispose();
                this.oldBackgroundGradientDisposable = null;
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent ev) {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchSetPressed(boolean pressed) {
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }
    }

    public EditWidgetActivity(int type, int widgetId) {
        this.widgetType = type;
        this.currentWidgetId = widgetId;
        ArrayList<TLRPC.User> users = new ArrayList<>();
        ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        getMessagesStorage().getWidgetDialogIds(this.currentWidgetId, this.widgetType, this.selectedDialogs, users, chats, true);
        getMessagesController().putUsers(users, true);
        getMessagesController().putChats(chats, true);
        updateRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        getMediaDataController().loadHints(true);
        return super.onFragmentCreate();
    }

    public void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.previewRow = 0;
        this.rowCount = i + 1;
        this.selectChatsRow = i;
        if (this.selectedDialogs.isEmpty()) {
            this.chatsStartRow = -1;
            this.chatsEndRow = -1;
        } else {
            int i2 = this.rowCount;
            this.chatsStartRow = i2;
            int size = i2 + this.selectedDialogs.size();
            this.rowCount = size;
            this.chatsEndRow = size;
        }
        int i3 = this.rowCount;
        this.rowCount = i3 + 1;
        this.infoRow = i3;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void setDelegate(EditWidgetActivityDelegate editWidgetActivityDelegate) {
        this.delegate = editWidgetActivityDelegate;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        if (this.widgetType == 0) {
            this.actionBar.setTitle(LocaleController.getString("WidgetChats", R.string.WidgetChats));
        } else {
            this.actionBar.setTitle(LocaleController.getString("WidgetShortcuts", R.string.WidgetShortcuts));
        }
        ActionBarMenu menu = this.actionBar.createMenu();
        menu.addItem(1, LocaleController.getString("Done", R.string.Done).toUpperCase());
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.EditWidgetActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int id) {
                if (id == -1) {
                    if (EditWidgetActivity.this.delegate == null) {
                        EditWidgetActivity.this.finishActivity();
                    } else {
                        EditWidgetActivity.this.finishFragment();
                    }
                } else if (id == 1 && EditWidgetActivity.this.getParentActivity() != null) {
                    EditWidgetActivity.this.getMessagesStorage().putWidgetDialogs(EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.selectedDialogs);
                    SharedPreferences preferences = EditWidgetActivity.this.getParentActivity().getSharedPreferences("shortcut_widget", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("account" + EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.currentAccount);
                    editor.putInt(CommonProperties.TYPE + EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.widgetType);
                    editor.commit();
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EditWidgetActivity.this.getParentActivity());
                    if (EditWidgetActivity.this.widgetType == 0) {
                        ChatsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                    } else {
                        ContactsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                    }
                    if (EditWidgetActivity.this.delegate != null) {
                        EditWidgetActivity.this.delegate.didSelectDialogs(EditWidgetActivity.this.selectedDialogs);
                    } else {
                        EditWidgetActivity.this.finishActivity();
                    }
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                EditWidgetActivity.this.m3402lambda$createView$1$orgtelegramuiEditWidgetActivity(context, view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new AnonymousClass2());
        return this.fragmentView;
    }

    /* renamed from: lambda$createView$1$org-telegram-ui-EditWidgetActivity */
    public /* synthetic */ void m3402lambda$createView$1$orgtelegramuiEditWidgetActivity(Context context, View view, int position) {
        if (position == this.selectChatsRow) {
            InviteMembersBottomSheet bottomSheet = new InviteMembersBottomSheet(context, this.currentAccount, null, 0L, this, null);
            bottomSheet.setDelegate(new InviteMembersBottomSheet.InviteMembersBottomSheetDelegate() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.InviteMembersBottomSheet.InviteMembersBottomSheetDelegate
                public final void didSelectDialogs(ArrayList arrayList) {
                    EditWidgetActivity.this.m3401lambda$createView$0$orgtelegramuiEditWidgetActivity(arrayList);
                }
            }, this.selectedDialogs);
            bottomSheet.setSelectedContacts(this.selectedDialogs);
            showDialog(bottomSheet);
        }
    }

    /* renamed from: lambda$createView$0$org-telegram-ui-EditWidgetActivity */
    public /* synthetic */ void m3401lambda$createView$0$orgtelegramuiEditWidgetActivity(ArrayList dids) {
        this.selectedDialogs.clear();
        this.selectedDialogs.addAll(dids);
        updateRows();
        WidgetPreviewCell widgetPreviewCell = this.widgetPreviewCell;
        if (widgetPreviewCell != null) {
            widgetPreviewCell.updateDialogs();
        }
    }

    /* renamed from: org.telegram.ui.EditWidgetActivity$2 */
    /* loaded from: classes4.dex */
    public class AnonymousClass2 implements RecyclerListView.OnItemLongClickListenerExtended {
        private Rect rect = new Rect();

        AnonymousClass2() {
            EditWidgetActivity.this = this$0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, final int position, float x, float y) {
            if (EditWidgetActivity.this.getParentActivity() == null || !(view instanceof GroupCreateUserCell)) {
                return false;
            }
            ImageView imageView = (ImageView) view.getTag(R.id.object_tag);
            imageView.getHitRect(this.rect);
            if (this.rect.contains((int) x, (int) y)) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(EditWidgetActivity.this.getParentActivity());
            CharSequence[] items = {LocaleController.getString("Delete", R.string.Delete)};
            builder.setItems(items, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.EditWidgetActivity$2$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    EditWidgetActivity.AnonymousClass2.this.m3403lambda$onItemClick$0$orgtelegramuiEditWidgetActivity$2(position, dialogInterface, i);
                }
            });
            EditWidgetActivity.this.showDialog(builder.create());
            return true;
        }

        /* renamed from: lambda$onItemClick$0$org-telegram-ui-EditWidgetActivity$2 */
        public /* synthetic */ void m3403lambda$onItemClick$0$orgtelegramuiEditWidgetActivity$2(int position, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                EditWidgetActivity.this.selectedDialogs.remove(position - EditWidgetActivity.this.chatsStartRow);
                EditWidgetActivity.this.updateRows();
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onMove(float dx, float dy) {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
        }
    }

    public void finishActivity() {
        if (getParentActivity() == null) {
            return;
        }
        getParentActivity().finish();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                EditWidgetActivity.this.removeSelfFromStack();
            }
        }, 1000L);
    }

    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            EditWidgetActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditWidgetActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 1 || type == 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GroupCreateUserCell cell;
            switch (viewType) {
                case 0:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    cell = textInfoPrivacyCell;
                    break;
                case 1:
                    TextCell textCell = new TextCell(this.mContext);
                    textCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    cell = textCell;
                    break;
                case 2:
                    cell = EditWidgetActivity.this.widgetPreviewCell = new WidgetPreviewCell(this.mContext);
                    break;
                default:
                    final GroupCreateUserCell cell2 = new GroupCreateUserCell(this.mContext, 0, 0, false);
                    ImageView sortImageView = new ImageView(this.mContext);
                    sortImageView.setImageResource(R.drawable.list_reorder);
                    sortImageView.setScaleType(ImageView.ScaleType.CENTER);
                    cell2.setTag(R.id.object_tag, sortImageView);
                    cell2.addView(sortImageView, LayoutHelper.createFrame(40, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 10.0f, 0.0f, 10.0f, 0.0f));
                    sortImageView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnTouchListener
                        public final boolean onTouch(View view, MotionEvent motionEvent) {
                            return EditWidgetActivity.ListAdapter.this.m3404x37efab4f(cell2, view, motionEvent);
                        }
                    });
                    sortImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_pinnedIcon), PorterDuff.Mode.MULTIPLY));
                    cell = cell2;
                    break;
            }
            return new RecyclerListView.Holder(cell);
        }

        /* renamed from: lambda$onCreateViewHolder$0$org-telegram-ui-EditWidgetActivity$ListAdapter */
        public /* synthetic */ boolean m3404x37efab4f(GroupCreateUserCell cell, View v, MotionEvent event) {
            if (event.getAction() == 0) {
                EditWidgetActivity.this.itemTouchHelper.startDrag(EditWidgetActivity.this.listView.getChildViewHolder(cell));
                return false;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            boolean z = true;
            switch (holder.getItemViewType()) {
                case 0:
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == EditWidgetActivity.this.infoRow) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        if (EditWidgetActivity.this.widgetType != 0) {
                            if (EditWidgetActivity.this.widgetType == 1) {
                                builder.append((CharSequence) LocaleController.getString("EditWidgetContactsInfo", R.string.EditWidgetContactsInfo));
                            }
                        } else {
                            builder.append((CharSequence) LocaleController.getString("EditWidgetChatsInfo", R.string.EditWidgetChatsInfo));
                        }
                        if (SharedConfig.passcodeHash.length() > 0) {
                            builder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString("WidgetPasscode2", R.string.WidgetPasscode2)));
                        }
                        cell.setText(builder);
                        return;
                    }
                    return;
                case 1:
                    TextCell cell2 = (TextCell) holder.itemView;
                    cell2.setColors(null, Theme.key_windowBackgroundWhiteBlueText4);
                    Drawable drawable1 = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
                    Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
                    drawable1.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_switchTrackChecked), PorterDuff.Mode.MULTIPLY));
                    drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(drawable1, drawable2);
                    String string = LocaleController.getString("SelectChats", R.string.SelectChats);
                    if (EditWidgetActivity.this.chatsStartRow == -1) {
                        z = false;
                    }
                    cell2.setTextAndIcon(string, combinedDrawable, z);
                    cell2.getImageView().setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
                    return;
                case 2:
                default:
                    return;
                case 3:
                    GroupCreateUserCell cell3 = (GroupCreateUserCell) holder.itemView;
                    long did = ((Long) EditWidgetActivity.this.selectedDialogs.get(position - EditWidgetActivity.this.chatsStartRow)).longValue();
                    if (DialogObject.isUserDialog(did)) {
                        TLRPC.User user = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(did));
                        if (position == EditWidgetActivity.this.chatsEndRow - 1) {
                            z = false;
                        }
                        cell3.setObject(user, null, null, z);
                        return;
                    }
                    TLRPC.Chat chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-did));
                    if (position == EditWidgetActivity.this.chatsEndRow - 1) {
                        z = false;
                    }
                    cell3.setObject(chat, null, null, z);
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            if (type == 3 || type == 1) {
                holder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            if (position != EditWidgetActivity.this.previewRow) {
                if (position != EditWidgetActivity.this.selectChatsRow) {
                    if (position == EditWidgetActivity.this.infoRow) {
                        return 0;
                    }
                    return 3;
                }
                return 1;
            }
            return 2;
        }

        public boolean swapElements(int fromIndex, int toIndex) {
            int idx1 = fromIndex - EditWidgetActivity.this.chatsStartRow;
            int idx2 = toIndex - EditWidgetActivity.this.chatsStartRow;
            int count = EditWidgetActivity.this.chatsEndRow - EditWidgetActivity.this.chatsStartRow;
            if (idx1 >= 0 && idx2 >= 0 && idx1 < count && idx2 < count) {
                Long did1 = (Long) EditWidgetActivity.this.selectedDialogs.get(idx1);
                Long did2 = (Long) EditWidgetActivity.this.selectedDialogs.get(idx2);
                EditWidgetActivity.this.selectedDialogs.set(idx1, did2);
                EditWidgetActivity.this.selectedDialogs.set(idx2, did1);
                notifyItemMoved(fromIndex, toIndex);
                return true;
            }
            return false;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent event) {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.delegate == null) {
            finishActivity();
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        themeDescriptions.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        themeDescriptions.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText4));
        themeDescriptions.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText4));
        return themeDescriptions;
    }
}
