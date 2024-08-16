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
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_dialog;
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
import org.telegram.ui.ActionBar.ActionBar;
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
    private WidgetPreviewCell widgetPreviewCell;
    private int widgetType;

    /* loaded from: classes4.dex */
    public interface EditWidgetActivityDelegate {
        void didSelectDialogs(ArrayList<Long> arrayList);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    /* loaded from: classes4.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        private boolean moved;

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 3) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            int adapterPosition = viewHolder.getAdapterPosition();
            int adapterPosition2 = viewHolder2.getAdapterPosition();
            if (EditWidgetActivity.this.listAdapter.swapElements(adapterPosition, adapterPosition2)) {
                ((GroupCreateUserCell) viewHolder.itemView).setDrawDivider(adapterPosition2 != EditWidgetActivity.this.chatsEndRow - 1);
                ((GroupCreateUserCell) viewHolder2.itemView).setDrawDivider(adapterPosition != EditWidgetActivity.this.chatsEndRow - 1);
                this.moved = true;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                EditWidgetActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            } else if (this.moved) {
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
                this.moved = false;
            }
            super.onSelectedChanged(viewHolder, i);
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
        private RectF bitmapRect;
        private ViewGroup[] cells;
        private Drawable oldBackgroundDrawable;
        private BackgroundGradientDrawable.Disposable oldBackgroundGradientDisposable;
        private Paint roundPaint;
        private Drawable shadowDrawable;

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchSetPressed(boolean z) {
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public WidgetPreviewCell(Context context) {
            super(context);
            this.roundPaint = new Paint(1);
            this.bitmapRect = new RectF();
            this.cells = new ViewGroup[2];
            int i = 0;
            setWillNotDraw(false);
            setPadding(0, AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
            ChatActionCell chatActionCell = new ChatActionCell(context);
            chatActionCell.setCustomText(LocaleController.getString("WidgetPreview", R.string.WidgetPreview));
            linearLayout.addView(chatActionCell, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 4));
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            linearLayout2.setBackgroundResource(R.drawable.widget_bg);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 17, 10, 0, 10, 0));
            EditWidgetActivity.this.previewImageView = new ImageView(context);
            if (EditWidgetActivity.this.widgetType == 0) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.shortcut_widget_item, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear(-1, -2));
                    i++;
                }
                linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear((int) NotificationCenter.webViewResolved, (int) NotificationCenter.audioRouteChanged, 17));
                EditWidgetActivity.this.previewImageView.setImageResource(R.drawable.chats_widget_preview);
            } else if (EditWidgetActivity.this.widgetType == 1) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.contacts_widget_item, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, -2));
                    i++;
                }
                linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, (int) NotificationCenter.audioRouteChanged, 17));
                EditWidgetActivity.this.previewImageView.setImageResource(R.drawable.contacts_widget_preview);
            }
            updateDialogs();
            this.shadowDrawable = Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
        }

        /* JADX WARN: Can't wrap try/catch for region: R(12:126|(1:128)(2:137|(1:139)(12:140|(1:142)(1:144)|143|130|131|132|133|101|102|103|104|105))|129|130|131|132|133|101|102|103|104|105) */
        /* JADX WARN: Code restructure failed: missing block: B:112:0x029c, code lost:
            if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L66;
         */
        /* JADX WARN: Code restructure failed: missing block: B:173:0x0402, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:174:0x0403, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x024d  */
        /* JADX WARN: Removed duplicated region for block: B:239:0x0580  */
        /* JADX WARN: Removed duplicated region for block: B:246:0x05bf  */
        /* JADX WARN: Removed duplicated region for block: B:251:0x0616  */
        /* JADX WARN: Removed duplicated region for block: B:384:0x08e2  */
        /* JADX WARN: Removed duplicated region for block: B:398:0x092e  */
        /* JADX WARN: Removed duplicated region for block: B:416:0x0161 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:73:0x017a  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x0191  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01bf A[Catch: all -> 0x0177, TryCatch #3 {all -> 0x0177, blocks: (B:69:0x0161, B:74:0x017c, B:77:0x0193, B:79:0x019e, B:85:0x01b6, B:91:0x0212, B:80:0x01a4, B:82:0x01aa, B:84:0x01b0, B:87:0x01bf, B:89:0x01ca, B:90:0x01da), top: B:416:0x0161 }] */
        /* JADX WARN: Removed duplicated region for block: B:96:0x023b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateDialogs() {
            TLRPC$Dialog tLRPC$Dialog;
            String str;
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$Chat tLRPC$Chat;
            TLRPC$User tLRPC$User;
            Bitmap decodeFile;
            int i;
            int i2;
            Bitmap createBitmap;
            Canvas canvas;
            Bitmap bitmap;
            AvatarDrawable avatarDrawable;
            int i3;
            String str2;
            String firstName;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$FileLocation tLRPC$FileLocation2;
            TLRPC$Dialog tLRPC$Dialog2;
            TLRPC$Dialog tLRPC$Dialog3;
            String str3;
            TLRPC$Dialog tLRPC$Dialog4;
            TLRPC$Chat tLRPC$Chat2;
            String str4;
            TLRPC$User tLRPC$User2;
            TLRPC$FileLocation tLRPC$FileLocation3;
            Bitmap decodeFile2;
            TLRPC$Dialog tLRPC$Dialog5;
            MessageObject messageObject;
            int i4;
            TLRPC$Chat chat;
            TLRPC$User tLRPC$User3;
            CharSequence charSequence;
            String str5;
            String str6;
            SpannableStringBuilder valueOf;
            char c;
            char c2;
            int i5;
            String charSequence2;
            SpannableStringBuilder spannableStringBuilder;
            String str7;
            CharSequence charSequence3;
            CharSequence charSequence4;
            Bitmap bitmap2;
            AvatarDrawable avatarDrawable2;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
            TLRPC$FileLocation tLRPC$FileLocation4;
            String str8 = "HiddenName";
            int i6 = 8;
            long j = 0;
            int i7 = 0;
            if (EditWidgetActivity.this.widgetType != 0) {
                String str9 = "HiddenName";
                if (EditWidgetActivity.this.widgetType == 1) {
                    int i8 = 0;
                    while (true) {
                        if (i8 >= 2) {
                            break;
                        }
                        int i9 = 0;
                        for (int i10 = 2; i9 < i10; i10 = 2) {
                            int i11 = (i8 * 2) + i9;
                            if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                                if (i11 < EditWidgetActivity.this.selectedDialogs.size()) {
                                    tLRPC$Dialog = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i11)).longValue());
                                    if (tLRPC$Dialog == null) {
                                        tLRPC$Dialog = new TLRPC$TL_dialog();
                                        tLRPC$Dialog.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i11)).longValue();
                                    }
                                }
                                tLRPC$Dialog = null;
                            } else {
                                if (i11 < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                    long j2 = EditWidgetActivity.this.getMediaDataController().hints.get(i11).peer.user_id;
                                    tLRPC$Dialog = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(j2);
                                    if (tLRPC$Dialog == null) {
                                        tLRPC$Dialog = new TLRPC$TL_dialog();
                                        tLRPC$Dialog.id = j2;
                                    }
                                }
                                tLRPC$Dialog = null;
                            }
                            if (tLRPC$Dialog == null) {
                                this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(4);
                                if (i11 == 0 || i11 == 2) {
                                    this.cells[i8].setVisibility(8);
                                }
                                i2 = 1;
                            } else {
                                this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(0);
                                if (i11 == 0 || i11 == 2) {
                                    this.cells[i8].setVisibility(0);
                                }
                                if (DialogObject.isUserDialog(tLRPC$Dialog.id)) {
                                    tLRPC$User = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$Dialog.id));
                                    if (UserObject.isUserSelf(tLRPC$User)) {
                                        firstName = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                                    } else if (UserObject.isReplyUser(tLRPC$User)) {
                                        firstName = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                    } else {
                                        if (UserObject.isDeleted(tLRPC$User)) {
                                            str2 = str9;
                                            firstName = LocaleController.getString(str2, R.string.HiddenName);
                                        } else {
                                            str2 = str9;
                                            firstName = UserObject.getFirstName(tLRPC$User);
                                        }
                                        if (!UserObject.isReplyUser(tLRPC$User) || UserObject.isUserSelf(tLRPC$User) || tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation2.volume_id == 0 || tLRPC$FileLocation2.local_id == 0) {
                                            str = firstName;
                                            str9 = str2;
                                            tLRPC$FileLocation = null;
                                        } else {
                                            str = firstName;
                                            str9 = str2;
                                            tLRPC$FileLocation = tLRPC$FileLocation2;
                                        }
                                        tLRPC$Chat = null;
                                    }
                                    str2 = str9;
                                    if (UserObject.isReplyUser(tLRPC$User)) {
                                    }
                                    str = firstName;
                                    str9 = str2;
                                    tLRPC$FileLocation = null;
                                    tLRPC$Chat = null;
                                } else {
                                    String str10 = str9;
                                    TLRPC$Chat chat2 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id));
                                    String str11 = chat2.title;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat2.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null) {
                                        str9 = str10;
                                    } else {
                                        str9 = str10;
                                        if (tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                            str = str11;
                                            tLRPC$Chat = chat2;
                                            tLRPC$User = null;
                                        }
                                    }
                                    str = str11;
                                    tLRPC$FileLocation = null;
                                    tLRPC$Chat = chat2;
                                    tLRPC$User = null;
                                }
                                ((TextView) this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                if (tLRPC$FileLocation != null) {
                                    try {
                                        decodeFile = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation, true).toString());
                                    } catch (Throwable th) {
                                        th = th;
                                        FileLog.e(th);
                                        i = tLRPC$Dialog.unread_count;
                                        if (i <= 0) {
                                        }
                                        i2 = 1;
                                        i9 += i2;
                                    }
                                } else {
                                    decodeFile = null;
                                }
                                try {
                                    int dp = AndroidUtilities.dp(48.0f);
                                    createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                                    createBitmap.eraseColor(0);
                                    canvas = new Canvas(createBitmap);
                                    if (decodeFile == null) {
                                        if (tLRPC$User != null) {
                                            try {
                                                avatarDrawable = new AvatarDrawable(tLRPC$User);
                                                if (UserObject.isReplyUser(tLRPC$User)) {
                                                    avatarDrawable.setAvatarType(12);
                                                } else if (UserObject.isUserSelf(tLRPC$User)) {
                                                    avatarDrawable.setAvatarType(1);
                                                }
                                                i3 = 0;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                FileLog.e(th);
                                                i = tLRPC$Dialog.unread_count;
                                                if (i <= 0) {
                                                }
                                                i2 = 1;
                                                i9 += i2;
                                            }
                                        } else {
                                            try {
                                                avatarDrawable = new AvatarDrawable(tLRPC$Chat);
                                                i3 = 0;
                                            } catch (Throwable th3) {
                                                th = th3;
                                                FileLog.e(th);
                                                i = tLRPC$Dialog.unread_count;
                                                if (i <= 0) {
                                                }
                                                i2 = 1;
                                                i9 += i2;
                                            }
                                        }
                                        avatarDrawable.setBounds(i3, i3, dp, dp);
                                        avatarDrawable.draw(canvas);
                                        bitmap = null;
                                    } else {
                                        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                        BitmapShader bitmapShader = new BitmapShader(decodeFile, tileMode, tileMode);
                                        float width = dp / decodeFile.getWidth();
                                        canvas.save();
                                        canvas.scale(width, width);
                                        this.roundPaint.setShader(bitmapShader);
                                        try {
                                            this.bitmapRect.set(0.0f, 0.0f, decodeFile.getWidth(), decodeFile.getHeight());
                                            canvas.drawRoundRect(this.bitmapRect, decodeFile.getWidth(), decodeFile.getHeight(), this.roundPaint);
                                            canvas.restore();
                                            bitmap = null;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            FileLog.e(th);
                                            i = tLRPC$Dialog.unread_count;
                                            if (i <= 0) {
                                            }
                                            i2 = 1;
                                            i9 += i2;
                                        }
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                }
                                try {
                                    canvas.setBitmap(bitmap);
                                    ((ImageView) this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap);
                                } catch (Throwable th6) {
                                    th = th6;
                                    FileLog.e(th);
                                    i = tLRPC$Dialog.unread_count;
                                    if (i <= 0) {
                                    }
                                    i2 = 1;
                                    i9 += i2;
                                }
                                i = tLRPC$Dialog.unread_count;
                                if (i <= 0) {
                                    ((TextView) this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item_badge1 : R.id.contacts_widget_item_badge2)).setText(i > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(i)));
                                    this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(0);
                                } else {
                                    this.cells[i8].findViewById(i9 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(8);
                                }
                                i2 = 1;
                            }
                            i9 += i2;
                        }
                        i8++;
                    }
                }
            } else {
                int i12 = 0;
                for (int i13 = 2; i12 < i13; i13 = 2) {
                    if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                        if (i12 < EditWidgetActivity.this.selectedDialogs.size()) {
                            tLRPC$Dialog2 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i12)).longValue());
                            if (tLRPC$Dialog2 == null) {
                                tLRPC$Dialog2 = new TLRPC$TL_dialog();
                                tLRPC$Dialog2.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i12)).longValue();
                            }
                            tLRPC$Dialog3 = tLRPC$Dialog2;
                        }
                        tLRPC$Dialog3 = null;
                    } else {
                        if (i12 < EditWidgetActivity.this.getMessagesController().dialogsServerOnly.size()) {
                            tLRPC$Dialog2 = EditWidgetActivity.this.getMessagesController().dialogsServerOnly.get(i12);
                            tLRPC$Dialog3 = tLRPC$Dialog2;
                        }
                        tLRPC$Dialog3 = null;
                    }
                    if (tLRPC$Dialog3 == null) {
                        this.cells[i12].setVisibility(i6);
                        str3 = str8;
                        i4 = 1;
                    } else {
                        this.cells[i12].setVisibility(i7);
                        if (DialogObject.isUserDialog(tLRPC$Dialog3.id)) {
                            tLRPC$User2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$Dialog3.id));
                            if (tLRPC$User2 != null) {
                                if (UserObject.isUserSelf(tLRPC$User2)) {
                                    str4 = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                                } else if (UserObject.isReplyUser(tLRPC$User2)) {
                                    str4 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                } else if (UserObject.isDeleted(tLRPC$User2)) {
                                    str4 = LocaleController.getString(str8, R.string.HiddenName);
                                } else {
                                    str4 = ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name);
                                }
                                if (UserObject.isReplyUser(tLRPC$User2) || UserObject.isUserSelf(tLRPC$User2) || (tLRPC$UserProfilePhoto2 = tLRPC$User2.photo) == null || (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto2.photo_small) == null) {
                                    str3 = str8;
                                } else {
                                    str3 = str8;
                                    if (tLRPC$FileLocation4.volume_id != j && tLRPC$FileLocation4.local_id != 0) {
                                        tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                        tLRPC$Chat2 = null;
                                        tLRPC$Dialog4 = tLRPC$Dialog3;
                                        ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_text)).setText(str4);
                                        if (tLRPC$FileLocation3 == null) {
                                            try {
                                                decodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation3, true).toString());
                                            } catch (Throwable th7) {
                                                FileLog.e(th7);
                                            }
                                        } else {
                                            decodeFile2 = null;
                                        }
                                        int dp2 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap2 = Bitmap.createBitmap(dp2, dp2, Bitmap.Config.ARGB_8888);
                                        createBitmap2.eraseColor(0);
                                        Canvas canvas2 = new Canvas(createBitmap2);
                                        if (decodeFile2 != null) {
                                            if (tLRPC$User2 != null) {
                                                avatarDrawable2 = new AvatarDrawable(tLRPC$User2);
                                                if (UserObject.isReplyUser(tLRPC$User2)) {
                                                    avatarDrawable2.setAvatarType(12);
                                                } else if (UserObject.isUserSelf(tLRPC$User2)) {
                                                    avatarDrawable2.setAvatarType(1);
                                                }
                                            } else {
                                                avatarDrawable2 = new AvatarDrawable(tLRPC$Chat2);
                                            }
                                            avatarDrawable2.setBounds(0, 0, dp2, dp2);
                                            avatarDrawable2.draw(canvas2);
                                            bitmap2 = createBitmap2;
                                        } else {
                                            Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
                                            BitmapShader bitmapShader2 = new BitmapShader(decodeFile2, tileMode2, tileMode2);
                                            if (this.roundPaint == null) {
                                                this.roundPaint = new Paint(1);
                                                this.bitmapRect = new RectF();
                                            }
                                            float width2 = dp2 / decodeFile2.getWidth();
                                            canvas2.save();
                                            canvas2.scale(width2, width2);
                                            this.roundPaint.setShader(bitmapShader2);
                                            this.bitmapRect.set(0.0f, 0.0f, decodeFile2.getWidth(), decodeFile2.getHeight());
                                            bitmap2 = createBitmap2;
                                            canvas2.drawRoundRect(this.bitmapRect, decodeFile2.getWidth(), decodeFile2.getHeight(), this.roundPaint);
                                            canvas2.restore();
                                        }
                                        canvas2.setBitmap(null);
                                        ((ImageView) this.cells[i12].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmap2);
                                        tLRPC$Dialog5 = tLRPC$Dialog4;
                                        ArrayList<MessageObject> arrayList = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog5.id);
                                        messageObject = (arrayList != null || arrayList.size() <= 0) ? null : arrayList.get(0);
                                        if (messageObject == null) {
                                            long fromChatId = messageObject.getFromChatId();
                                            if (fromChatId > 0) {
                                                tLRPC$User3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                                chat = null;
                                            } else {
                                                chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                                tLRPC$User3 = null;
                                            }
                                            int color = getContext().getResources().getColor(R.color.widget_text);
                                            if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                                                if (ChatObject.isChannel(tLRPC$Chat2)) {
                                                    TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                                                    charSequence4 = "";
                                                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                        charSequence4 = "";
                                                    }
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
                                                    charSequence3 = charSequence4;
                                                }
                                                charSequence4 = messageObject.messageText;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
                                                charSequence3 = charSequence4;
                                            } else if (tLRPC$Chat2 != null && tLRPC$Chat2.id > 0 && chat == null && (!ChatObject.isChannel(tLRPC$Chat2) || ChatObject.isMegagroup(tLRPC$Chat2))) {
                                                if (messageObject.isOutOwner()) {
                                                    str6 = LocaleController.getString("FromYou", R.string.FromYou);
                                                } else if (tLRPC$User3 != null) {
                                                    str6 = UserObject.getFirstName(tLRPC$User3).replace("\n", "");
                                                } else {
                                                    str6 = "DELETED";
                                                }
                                                String str12 = str6;
                                                CharSequence charSequence5 = messageObject.caption;
                                                char c3 = ' ';
                                                try {
                                                    try {
                                                        if (charSequence5 != null) {
                                                            String charSequence6 = charSequence5.toString();
                                                            if (charSequence6.length() > 150) {
                                                                charSequence6 = charSequence6.substring(0, 150);
                                                            }
                                                            if (messageObject.isVideo()) {
                                                                str7 = "📹 ";
                                                            } else if (messageObject.isVoice()) {
                                                                str7 = "🎤 ";
                                                            } else if (messageObject.isMusic()) {
                                                                str7 = "🎧 ";
                                                            } else {
                                                                str7 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                                            }
                                                            valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str7 + charSequence6.replace('\n', ' '), str12));
                                                        } else if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                                            color = getContext().getResources().getColor(R.color.widget_action_text);
                                                            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                c = 1;
                                                                c2 = 0;
                                                                charSequence2 = String.format("📊 \u2068%s\u2069", ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.question.text);
                                                            } else {
                                                                c = 1;
                                                                c2 = 0;
                                                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                    charSequence2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
                                                                } else {
                                                                    if (messageObject.type == 14) {
                                                                        i5 = 2;
                                                                        charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                                    } else {
                                                                        i5 = 2;
                                                                        charSequence2 = messageObject.messageText.toString();
                                                                    }
                                                                    c3 = ' ';
                                                                    String replace = charSequence2.replace('\n', c3);
                                                                    Object[] objArr = new Object[i5];
                                                                    objArr[c2] = replace;
                                                                    objArr[c] = str12;
                                                                    SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                                                    valueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str12.length() + 2, valueOf2.length(), 33);
                                                                    spannableStringBuilder = valueOf2;
                                                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str12.length() + 1, 33);
                                                                    charSequence3 = spannableStringBuilder;
                                                                }
                                                            }
                                                            i5 = 2;
                                                            String replace2 = charSequence2.replace('\n', c3);
                                                            Object[] objArr2 = new Object[i5];
                                                            objArr2[c2] = replace2;
                                                            objArr2[c] = str12;
                                                            SpannableStringBuilder valueOf22 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                                            valueOf22.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str12.length() + 2, valueOf22.length(), 33);
                                                            spannableStringBuilder = valueOf22;
                                                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str12.length() + 1, 33);
                                                            charSequence3 = spannableStringBuilder;
                                                        } else {
                                                            String str13 = messageObject.messageOwner.message;
                                                            if (str13 != null) {
                                                                if (str13.length() > 150) {
                                                                    str13 = str13.substring(0, 150);
                                                                }
                                                                valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str13.replace('\n', ' ').trim(), str12));
                                                            } else {
                                                                valueOf = SpannableStringBuilder.valueOf("");
                                                            }
                                                        }
                                                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str12.length() + 1, 33);
                                                        charSequence3 = spannableStringBuilder;
                                                    } catch (Exception e) {
                                                        e = e;
                                                        FileLog.e(e);
                                                        charSequence3 = spannableStringBuilder;
                                                        ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                                                        ViewGroup viewGroup = this.cells[i12];
                                                        int i14 = R.id.shortcut_widget_item_message;
                                                        ((TextView) viewGroup.findViewById(i14)).setText(charSequence3.toString());
                                                        ((TextView) this.cells[i12].findViewById(i14)).setTextColor(color);
                                                        if (tLRPC$Dialog5.unread_count > 0) {
                                                        }
                                                        i4 = 1;
                                                        i12 += i4;
                                                        str8 = str3;
                                                        i7 = 0;
                                                        i6 = 8;
                                                        j = 0;
                                                    }
                                                } catch (Exception e2) {
                                                    e = e2;
                                                }
                                                spannableStringBuilder = valueOf;
                                            } else {
                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                                                if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                    charSequence3 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                    charSequence3 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                } else if (messageObject.caption != null) {
                                                    if (messageObject.isVideo()) {
                                                        str5 = "📹 ";
                                                    } else if (messageObject.isVoice()) {
                                                        str5 = "🎤 ";
                                                    } else if (messageObject.isMusic()) {
                                                        str5 = "🎧 ";
                                                    } else {
                                                        str5 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                                    }
                                                    charSequence3 = str5 + ((Object) messageObject.caption);
                                                } else {
                                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                        charSequence = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question.text;
                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                        charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                                    } else if (messageObject.type == 14) {
                                                        charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                    } else {
                                                        charSequence = messageObject.messageText;
                                                        AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    }
                                                    CharSequence charSequence7 = charSequence;
                                                    charSequence3 = charSequence7;
                                                    if (messageObject.messageOwner.media != null) {
                                                        charSequence3 = charSequence7;
                                                        if (!messageObject.isMediaEmpty()) {
                                                            color = getContext().getResources().getColor(R.color.widget_action_text);
                                                            charSequence3 = charSequence7;
                                                        }
                                                    }
                                                }
                                            }
                                            ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                                            ViewGroup viewGroup2 = this.cells[i12];
                                            int i142 = R.id.shortcut_widget_item_message;
                                            ((TextView) viewGroup2.findViewById(i142)).setText(charSequence3.toString());
                                            ((TextView) this.cells[i12].findViewById(i142)).setTextColor(color);
                                        } else {
                                            if (tLRPC$Dialog5.last_message_date != 0) {
                                                ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(tLRPC$Dialog5.last_message_date));
                                            } else {
                                                ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_time)).setText("");
                                            }
                                            ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_message)).setText("");
                                        }
                                        if (tLRPC$Dialog5.unread_count > 0) {
                                            ViewGroup viewGroup3 = this.cells[i12];
                                            int i15 = R.id.shortcut_widget_item_badge;
                                            ((TextView) viewGroup3.findViewById(i15)).setText(String.format("%d", Integer.valueOf(tLRPC$Dialog5.unread_count)));
                                            this.cells[i12].findViewById(i15).setVisibility(0);
                                            if (EditWidgetActivity.this.getMessagesController().isDialogMuted(tLRPC$Dialog5.id, 0L)) {
                                                this.cells[i12].findViewById(i15).setBackgroundResource(R.drawable.widget_counter_muted);
                                            } else {
                                                this.cells[i12].findViewById(i15).setBackgroundResource(R.drawable.widget_counter);
                                            }
                                        } else {
                                            this.cells[i12].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                                        }
                                        i4 = 1;
                                    }
                                }
                                tLRPC$Dialog4 = tLRPC$Dialog3;
                            } else {
                                str3 = str8;
                                tLRPC$Dialog4 = tLRPC$Dialog3;
                                str4 = "";
                            }
                            tLRPC$Chat2 = null;
                            tLRPC$FileLocation3 = null;
                            ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_text)).setText(str4);
                            if (tLRPC$FileLocation3 == null) {
                            }
                            int dp22 = AndroidUtilities.dp(48.0f);
                            Bitmap createBitmap22 = Bitmap.createBitmap(dp22, dp22, Bitmap.Config.ARGB_8888);
                            createBitmap22.eraseColor(0);
                            Canvas canvas22 = new Canvas(createBitmap22);
                            if (decodeFile2 != null) {
                            }
                            canvas22.setBitmap(null);
                            ((ImageView) this.cells[i12].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmap2);
                            tLRPC$Dialog5 = tLRPC$Dialog4;
                            ArrayList<MessageObject> arrayList2 = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog5.id);
                            if (arrayList2 != null) {
                            }
                            if (messageObject == null) {
                            }
                            if (tLRPC$Dialog5.unread_count > 0) {
                            }
                            i4 = 1;
                        } else {
                            str3 = str8;
                            TLRPC$Chat chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog3.id));
                            if (chat3 != null) {
                                str4 = chat3.title;
                                TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat3.photo;
                                if (tLRPC$ChatPhoto2 == null || (tLRPC$FileLocation3 = tLRPC$ChatPhoto2.photo_small) == null) {
                                    tLRPC$Dialog4 = tLRPC$Dialog3;
                                } else {
                                    tLRPC$Dialog4 = tLRPC$Dialog3;
                                    if (tLRPC$FileLocation3.volume_id != j && tLRPC$FileLocation3.local_id != 0) {
                                        tLRPC$Chat2 = chat3;
                                        tLRPC$User2 = null;
                                        ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_text)).setText(str4);
                                        if (tLRPC$FileLocation3 == null) {
                                        }
                                        int dp222 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap222 = Bitmap.createBitmap(dp222, dp222, Bitmap.Config.ARGB_8888);
                                        createBitmap222.eraseColor(0);
                                        Canvas canvas222 = new Canvas(createBitmap222);
                                        if (decodeFile2 != null) {
                                        }
                                        canvas222.setBitmap(null);
                                        ((ImageView) this.cells[i12].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmap2);
                                        tLRPC$Dialog5 = tLRPC$Dialog4;
                                        ArrayList<MessageObject> arrayList22 = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog5.id);
                                        if (arrayList22 != null) {
                                        }
                                        if (messageObject == null) {
                                        }
                                        if (tLRPC$Dialog5.unread_count > 0) {
                                        }
                                        i4 = 1;
                                    }
                                }
                                tLRPC$Chat2 = chat3;
                            } else {
                                tLRPC$Dialog4 = tLRPC$Dialog3;
                                tLRPC$Chat2 = chat3;
                                str4 = "";
                            }
                            tLRPC$User2 = null;
                            tLRPC$FileLocation3 = null;
                            ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_text)).setText(str4);
                            if (tLRPC$FileLocation3 == null) {
                            }
                            int dp2222 = AndroidUtilities.dp(48.0f);
                            Bitmap createBitmap2222 = Bitmap.createBitmap(dp2222, dp2222, Bitmap.Config.ARGB_8888);
                            createBitmap2222.eraseColor(0);
                            Canvas canvas2222 = new Canvas(createBitmap2222);
                            if (decodeFile2 != null) {
                            }
                            canvas2222.setBitmap(null);
                            ((ImageView) this.cells[i12].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmap2);
                            tLRPC$Dialog5 = tLRPC$Dialog4;
                            ArrayList<MessageObject> arrayList222 = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog5.id);
                            if (arrayList222 != null) {
                            }
                            if (messageObject == null) {
                            }
                            if (tLRPC$Dialog5.unread_count > 0) {
                            }
                            i4 = 1;
                        }
                    }
                    i12 += i4;
                    str8 = str3;
                    i7 = 0;
                    i6 = 8;
                    j = 0;
                }
                ViewGroup viewGroup4 = this.cells[0];
                int i16 = R.id.shortcut_widget_item_divider;
                viewGroup4.findViewById(i16).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(i16).setVisibility(8);
            }
            if (this.cells[0].getVisibility() == 0) {
                EditWidgetActivity.this.previewImageView.setVisibility(8);
            } else {
                EditWidgetActivity.this.previewImageView.setVisibility(0);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(264.0f), 1073741824));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != this.backgroundDrawable && cachedWallpaperNonBlocking != null) {
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
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            float themeAnimationValue = ((BaseFragment) EditWidgetActivity.this).parentLayout.getThemeAnimationValue();
            int i = 0;
            while (i < 2) {
                Drawable drawable = i == 0 ? this.oldBackgroundDrawable : this.backgroundDrawable;
                if (drawable != null) {
                    if (i == 1 && this.oldBackgroundDrawable != null && ((BaseFragment) EditWidgetActivity.this).parentLayout != null) {
                        drawable.setAlpha((int) (255.0f * themeAnimationValue));
                    } else {
                        drawable.setAlpha(NotificationCenter.voipServiceCreated);
                    }
                    if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        if (drawable instanceof BackgroundGradientDrawable) {
                            this.backgroundGradientDisposable = ((BackgroundGradientDrawable) drawable).drawExactBoundsSize(canvas, this);
                        } else {
                            drawable.draw(canvas);
                        }
                    } else if (drawable instanceof BitmapDrawable) {
                        if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                            canvas.save();
                            float f = 2.0f / AndroidUtilities.density;
                            canvas.scale(f, f);
                            drawable.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f), (int) Math.ceil(getMeasuredHeight() / f));
                        } else {
                            int measuredHeight = getMeasuredHeight();
                            float max = Math.max(getMeasuredWidth() / drawable.getIntrinsicWidth(), measuredHeight / drawable.getIntrinsicHeight());
                            int ceil = (int) Math.ceil(drawable.getIntrinsicWidth() * max);
                            int ceil2 = (int) Math.ceil(drawable.getIntrinsicHeight() * max);
                            int measuredWidth = (getMeasuredWidth() - ceil) / 2;
                            int i2 = (measuredHeight - ceil2) / 2;
                            canvas.save();
                            canvas.clipRect(0, 0, ceil, getMeasuredHeight());
                            drawable.setBounds(measuredWidth, i2, ceil + measuredWidth, ceil2 + i2);
                        }
                        drawable.draw(canvas);
                        canvas.restore();
                    }
                    if (i == 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                        BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
                        if (disposable2 != null) {
                            disposable2.dispose();
                            this.oldBackgroundGradientDisposable = null;
                        }
                        this.oldBackgroundDrawable = null;
                        invalidate();
                    }
                }
                i++;
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
    }

    public EditWidgetActivity(int i, int i2) {
        this.widgetType = i;
        this.currentWidgetId = i2;
        ArrayList<TLRPC$User> arrayList = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList2 = new ArrayList<>();
        getMessagesStorage().getWidgetDialogIds(this.currentWidgetId, this.widgetType, this.selectedDialogs, arrayList, arrayList2, true);
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        updateRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        getMediaDataController().loadHints(true);
        return super.onFragmentCreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows() {
        this.previewRow = 0;
        this.rowCount = 2;
        this.selectChatsRow = 1;
        if (this.selectedDialogs.isEmpty()) {
            this.chatsStartRow = -1;
            this.chatsEndRow = -1;
        } else {
            int i = this.rowCount;
            this.chatsStartRow = i;
            int size = i + this.selectedDialogs.size();
            this.rowCount = size;
            this.chatsEndRow = size;
        }
        int i2 = this.rowCount;
        this.rowCount = i2 + 1;
        this.infoRow = i2;
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
        this.actionBar.createMenu().addItem(1, LocaleController.getString("Done", R.string.Done).toUpperCase());
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.EditWidgetActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    if (EditWidgetActivity.this.delegate == null) {
                        EditWidgetActivity.this.finishActivity();
                    } else {
                        EditWidgetActivity.this.finishFragment();
                    }
                } else if (i == 1 && EditWidgetActivity.this.getParentActivity() != null) {
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    for (int i2 = 0; i2 < EditWidgetActivity.this.selectedDialogs.size(); i2++) {
                        arrayList.add(MessagesStorage.TopicKey.of(((Long) EditWidgetActivity.this.selectedDialogs.get(i2)).longValue(), 0L));
                    }
                    EditWidgetActivity.this.getMessagesStorage().putWidgetDialogs(EditWidgetActivity.this.currentWidgetId, arrayList);
                    SharedPreferences.Editor edit = EditWidgetActivity.this.getParentActivity().getSharedPreferences("shortcut_widget", 0).edit();
                    edit.putInt("account" + EditWidgetActivity.this.currentWidgetId, ((BaseFragment) EditWidgetActivity.this).currentAccount);
                    edit.putInt("type" + EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.widgetType);
                    edit.commit();
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
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                EditWidgetActivity.this.lambda$createView$1(context, view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new 2());
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        if (i == this.selectChatsRow) {
            InviteMembersBottomSheet inviteMembersBottomSheet = new InviteMembersBottomSheet(context, this.currentAccount, null, 0L, this, null);
            inviteMembersBottomSheet.setDelegate(new InviteMembersBottomSheet.InviteMembersBottomSheetDelegate() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.InviteMembersBottomSheet.InviteMembersBottomSheetDelegate
                public final void didSelectDialogs(ArrayList arrayList) {
                    EditWidgetActivity.this.lambda$createView$0(arrayList);
                }
            }, this.selectedDialogs);
            inviteMembersBottomSheet.setSelectedContacts(this.selectedDialogs);
            showDialog(inviteMembersBottomSheet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        this.selectedDialogs.clear();
        this.selectedDialogs.addAll(arrayList);
        updateRows();
        WidgetPreviewCell widgetPreviewCell = this.widgetPreviewCell;
        if (widgetPreviewCell != null) {
            widgetPreviewCell.updateDialogs();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 implements RecyclerListView.OnItemLongClickListenerExtended {
        private Rect rect = new Rect();

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onMove(float f, float f2) {
        }

        2() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, final int i, float f, float f2) {
            if (EditWidgetActivity.this.getParentActivity() != null && (view instanceof GroupCreateUserCell)) {
                ((ImageView) view.getTag(R.id.object_tag)).getHitRect(this.rect);
                if (!this.rect.contains((int) f, (int) f2)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditWidgetActivity.this.getParentActivity());
                    builder.setItems(new CharSequence[]{LocaleController.getString("Delete", R.string.Delete)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.EditWidgetActivity$2$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            EditWidgetActivity.2.this.lambda$onItemClick$0(i, dialogInterface, i2);
                        }
                    });
                    EditWidgetActivity.this.showDialog(builder.create());
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(int i, DialogInterface dialogInterface, int i2) {
            if (i2 == 0) {
                EditWidgetActivity.this.selectedDialogs.remove(i - EditWidgetActivity.this.chatsStartRow);
                EditWidgetActivity.this.updateRows();
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditWidgetActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 1 || itemViewType == 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GroupCreateUserCell groupCreateUserCell;
            if (i == 0) {
                FrameLayout textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                groupCreateUserCell = textInfoPrivacyCell;
            } else if (i == 1) {
                FrameLayout textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                groupCreateUserCell = textCell;
            } else if (i == 2) {
                groupCreateUserCell = EditWidgetActivity.this.widgetPreviewCell = new WidgetPreviewCell(this.mContext);
            } else {
                final GroupCreateUserCell groupCreateUserCell2 = new GroupCreateUserCell(this.mContext, 0, 0, false);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(R.drawable.list_reorder);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                groupCreateUserCell2.setTag(R.id.object_tag, imageView);
                groupCreateUserCell2.addView(imageView, LayoutHelper.createFrame(40, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 10.0f, 0.0f, 10.0f, 0.0f));
                imageView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$onCreateViewHolder$0;
                        lambda$onCreateViewHolder$0 = EditWidgetActivity.ListAdapter.this.lambda$onCreateViewHolder$0(groupCreateUserCell2, view, motionEvent);
                        return lambda$onCreateViewHolder$0;
                    }
                });
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_pinnedIcon), PorterDuff.Mode.MULTIPLY));
                groupCreateUserCell = groupCreateUserCell2;
            }
            return new RecyclerListView.Holder(groupCreateUserCell);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$0(GroupCreateUserCell groupCreateUserCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                EditWidgetActivity.this.itemTouchHelper.startDrag(EditWidgetActivity.this.listView.getChildViewHolder(groupCreateUserCell));
                return false;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == EditWidgetActivity.this.infoRow) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (EditWidgetActivity.this.widgetType != 0) {
                        if (EditWidgetActivity.this.widgetType == 1) {
                            spannableStringBuilder.append((CharSequence) LocaleController.getString("EditWidgetContactsInfo", R.string.EditWidgetContactsInfo));
                        }
                    } else {
                        spannableStringBuilder.append((CharSequence) LocaleController.getString("EditWidgetChatsInfo", R.string.EditWidgetChatsInfo));
                    }
                    if (SharedConfig.passcodeHash.length() > 0) {
                        spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString("WidgetPasscode2", R.string.WidgetPasscode2)));
                    }
                    textInfoPrivacyCell.setText(spannableStringBuilder);
                }
            } else if (itemViewType != 1) {
                if (itemViewType != 3) {
                    return;
                }
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                Long l = (Long) EditWidgetActivity.this.selectedDialogs.get(i - EditWidgetActivity.this.chatsStartRow);
                long longValue = l.longValue();
                if (DialogObject.isUserDialog(longValue)) {
                    groupCreateUserCell.setObject(EditWidgetActivity.this.getMessagesController().getUser(l), null, null, i != EditWidgetActivity.this.chatsEndRow - 1);
                } else {
                    groupCreateUserCell.setObject(EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-longValue)), null, null, i != EditWidgetActivity.this.chatsEndRow - 1);
                }
            } else {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText4);
                Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
                Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
                int color = Theme.getColor(Theme.key_switchTrackChecked);
                PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                drawable.setColorFilter(new PorterDuffColorFilter(color, mode));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), mode));
                textCell.setTextAndIcon(LocaleController.getString("SelectChats", R.string.SelectChats), new CombinedDrawable(drawable, drawable2), EditWidgetActivity.this.chatsStartRow != -1);
                textCell.getImageView().setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 3 || itemViewType == 1) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == EditWidgetActivity.this.previewRow) {
                return 2;
            }
            if (i == EditWidgetActivity.this.selectChatsRow) {
                return 1;
            }
            return i == EditWidgetActivity.this.infoRow ? 0 : 3;
        }

        public boolean swapElements(int i, int i2) {
            int i3 = i - EditWidgetActivity.this.chatsStartRow;
            int i4 = i2 - EditWidgetActivity.this.chatsStartRow;
            int i5 = EditWidgetActivity.this.chatsEndRow - EditWidgetActivity.this.chatsStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= i5 || i4 >= i5) {
                return false;
            }
            EditWidgetActivity.this.selectedDialogs.set(i3, (Long) EditWidgetActivity.this.selectedDialogs.get(i4));
            EditWidgetActivity.this.selectedDialogs.set(i4, (Long) EditWidgetActivity.this.selectedDialogs.get(i3));
            notifyItemMoved(i, i2);
            return true;
        }
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
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i3 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        return arrayList;
    }
}
