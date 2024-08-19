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
            chatActionCell.setCustomText(LocaleController.getString(R.string.WidgetPreview));
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

        /* JADX WARN: Can't wrap try/catch for region: R(8:119|(1:121)(2:129|(1:131)(7:132|(1:134)(1:135)|123|124|125|100|101))|122|123|124|125|100|101) */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0280, code lost:
            if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L67;
         */
        /* JADX WARN: Code restructure failed: missing block: B:168:0x03da, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:169:0x03db, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r5 = r5;
         */
        /* JADX WARN: Removed duplicated region for block: B:325:0x07c2  */
        /* JADX WARN: Removed duplicated region for block: B:326:0x07c5  */
        /* JADX WARN: Removed duplicated region for block: B:333:0x07f1  */
        /* JADX WARN: Removed duplicated region for block: B:336:0x0808  */
        /* JADX WARN: Removed duplicated region for block: B:353:0x0844 A[Catch: all -> 0x0840, TRY_LEAVE, TryCatch #1 {all -> 0x0840, blocks: (B:350:0x0837, B:349:0x0831, B:353:0x0844), top: B:398:0x0837 }] */
        /* JADX WARN: Removed duplicated region for block: B:359:0x088d A[Catch: all -> 0x0890, TryCatch #0 {all -> 0x0890, blocks: (B:357:0x0884, B:359:0x088d, B:363:0x0894, B:362:0x0892), top: B:396:0x0884 }] */
        /* JADX WARN: Removed duplicated region for block: B:362:0x0892 A[Catch: all -> 0x0890, TryCatch #0 {all -> 0x0890, blocks: (B:357:0x0884, B:359:0x088d, B:363:0x0894, B:362:0x0892), top: B:396:0x0884 }] */
        /* JADX WARN: Removed duplicated region for block: B:370:0x08a7  */
        /* JADX WARN: Removed duplicated region for block: B:384:0x08f3  */
        /* JADX WARN: Removed duplicated region for block: B:404:0x07d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateDialogs() {
            TLRPC$Dialog tLRPC$Dialog;
            TLRPC$TL_dialog tLRPC$TL_dialog;
            TLRPC$TL_dialog tLRPC$TL_dialog2;
            String str;
            TLRPC$Chat tLRPC$Chat;
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$User tLRPC$User;
            TLRPC$FileLocation tLRPC$FileLocation2;
            TLRPC$FileLocation tLRPC$FileLocation3;
            TLRPC$Chat tLRPC$Chat2;
            TLRPC$User tLRPC$User2;
            Bitmap decodeFile;
            int i;
            int i2;
            Canvas canvas;
            Bitmap bitmap;
            AvatarDrawable avatarDrawable;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$FileLocation tLRPC$FileLocation4;
            TLRPC$Dialog tLRPC$Dialog2;
            TLRPC$Dialog tLRPC$Dialog3;
            TLRPC$Chat tLRPC$Chat3;
            String str2;
            TLRPC$User tLRPC$User3;
            TLRPC$FileLocation tLRPC$FileLocation5;
            Bitmap decodeFile2;
            int i3;
            TLRPC$Chat chat;
            TLRPC$User tLRPC$User4;
            CharSequence charSequence;
            String str3;
            String str4;
            SpannableStringBuilder valueOf;
            char c;
            char c2;
            char c3;
            int i4;
            String charSequence2;
            SpannableStringBuilder spannableStringBuilder;
            String str5;
            CharSequence charSequence3;
            CharSequence charSequence4;
            AvatarDrawable avatarDrawable2;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
            TLRPC$FileLocation tLRPC$FileLocation6;
            int i5 = 8;
            long j = 0;
            int i6 = 2;
            int i7 = 0;
            if (EditWidgetActivity.this.widgetType != 0) {
                if (EditWidgetActivity.this.widgetType == 1) {
                    int i8 = 2;
                    int i9 = 0;
                    while (i9 < i8) {
                        int i10 = 0;
                        while (i10 < i8) {
                            int i11 = (i9 * 2) + i10;
                            if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                                if (i11 < EditWidgetActivity.this.selectedDialogs.size()) {
                                    tLRPC$Dialog = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i11)).longValue());
                                    if (tLRPC$Dialog == null) {
                                        tLRPC$TL_dialog = new TLRPC$TL_dialog();
                                        tLRPC$TL_dialog.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i11)).longValue();
                                        tLRPC$TL_dialog2 = tLRPC$TL_dialog;
                                    }
                                    tLRPC$TL_dialog2 = tLRPC$Dialog;
                                }
                                tLRPC$TL_dialog2 = null;
                            } else {
                                if (i11 < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                    long j2 = EditWidgetActivity.this.getMediaDataController().hints.get(i11).peer.user_id;
                                    tLRPC$Dialog = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(j2);
                                    if (tLRPC$Dialog == null) {
                                        tLRPC$TL_dialog = new TLRPC$TL_dialog();
                                        tLRPC$TL_dialog.id = j2;
                                        tLRPC$TL_dialog2 = tLRPC$TL_dialog;
                                    }
                                    tLRPC$TL_dialog2 = tLRPC$Dialog;
                                }
                                tLRPC$TL_dialog2 = null;
                            }
                            if (tLRPC$TL_dialog2 == null) {
                                this.cells[i9].findViewById(i10 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(4);
                                if (i11 == 0 || i11 == 2) {
                                    this.cells[i9].setVisibility(8);
                                }
                                i2 = 1;
                            } else {
                                this.cells[i9].findViewById(i10 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(0);
                                if (i11 == 0 || i11 == 2) {
                                    this.cells[i9].setVisibility(0);
                                }
                                if (DialogObject.isUserDialog(tLRPC$TL_dialog2.id)) {
                                    tLRPC$User2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$TL_dialog2.id));
                                    if (UserObject.isUserSelf(tLRPC$User2)) {
                                        str = LocaleController.getString(R.string.SavedMessages);
                                    } else if (UserObject.isReplyUser(tLRPC$User2)) {
                                        str = LocaleController.getString(R.string.RepliesTitle);
                                    } else if (UserObject.isDeleted(tLRPC$User2)) {
                                        str = LocaleController.getString(R.string.HiddenName);
                                    } else {
                                        str = UserObject.getFirstName(tLRPC$User2);
                                    }
                                    if (UserObject.isReplyUser(tLRPC$User2) || UserObject.isUserSelf(tLRPC$User2) || tLRPC$User2 == null || (tLRPC$UserProfilePhoto = tLRPC$User2.photo) == null || (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation4.volume_id == 0 || tLRPC$FileLocation4.local_id == 0) {
                                        tLRPC$User = tLRPC$User2;
                                        tLRPC$FileLocation = null;
                                        tLRPC$Chat = null;
                                        ((TextView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                        if (tLRPC$FileLocation == null) {
                                            try {
                                                decodeFile = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation, true).toString());
                                            } catch (Throwable th) {
                                                th = th;
                                                FileLog.e(th);
                                                i = tLRPC$TL_dialog2.unread_count;
                                                if (i > 0) {
                                                }
                                                i2 = 1;
                                                i10 += i2;
                                                i8 = 2;
                                            }
                                        } else {
                                            decodeFile = null;
                                        }
                                        int dp = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                                        createBitmap.eraseColor(0);
                                        canvas = new Canvas(createBitmap);
                                        if (decodeFile != null) {
                                            if (tLRPC$User != null) {
                                                try {
                                                    avatarDrawable = new AvatarDrawable(tLRPC$User);
                                                    if (UserObject.isReplyUser(tLRPC$User)) {
                                                        avatarDrawable.setAvatarType(12);
                                                    } else if (UserObject.isUserSelf(tLRPC$User)) {
                                                        avatarDrawable.setAvatarType(1);
                                                    }
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    FileLog.e(th);
                                                    i = tLRPC$TL_dialog2.unread_count;
                                                    if (i > 0) {
                                                    }
                                                    i2 = 1;
                                                    i10 += i2;
                                                    i8 = 2;
                                                }
                                            } else {
                                                avatarDrawable = new AvatarDrawable(tLRPC$Chat);
                                            }
                                            try {
                                                avatarDrawable.setBounds(0, 0, dp, dp);
                                                avatarDrawable.draw(canvas);
                                                bitmap = null;
                                            } catch (Throwable th3) {
                                                th = th3;
                                                FileLog.e(th);
                                                i = tLRPC$TL_dialog2.unread_count;
                                                if (i > 0) {
                                                }
                                                i2 = 1;
                                                i10 += i2;
                                                i8 = 2;
                                            }
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
                                                i = tLRPC$TL_dialog2.unread_count;
                                                if (i > 0) {
                                                }
                                                i2 = 1;
                                                i10 += i2;
                                                i8 = 2;
                                            }
                                        }
                                        try {
                                            canvas.setBitmap(bitmap);
                                            ((ImageView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap);
                                        } catch (Throwable th5) {
                                            th = th5;
                                            FileLog.e(th);
                                            i = tLRPC$TL_dialog2.unread_count;
                                            if (i > 0) {
                                            }
                                            i2 = 1;
                                            i10 += i2;
                                            i8 = 2;
                                        }
                                        i = tLRPC$TL_dialog2.unread_count;
                                        if (i > 0) {
                                            ((TextView) this.cells[i9].findViewById(i10 == 0 ? R.id.contacts_widget_item_badge1 : R.id.contacts_widget_item_badge2)).setText(i > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(i)));
                                            this.cells[i9].findViewById(i10 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(0);
                                        } else {
                                            this.cells[i9].findViewById(i10 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2).setVisibility(8);
                                        }
                                        i2 = 1;
                                    } else {
                                        tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                        tLRPC$Chat2 = null;
                                        tLRPC$Chat = tLRPC$Chat2;
                                        tLRPC$User = tLRPC$User2;
                                        tLRPC$FileLocation = tLRPC$FileLocation3;
                                        ((TextView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                        if (tLRPC$FileLocation == null) {
                                        }
                                        int dp2 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap2 = Bitmap.createBitmap(dp2, dp2, Bitmap.Config.ARGB_8888);
                                        createBitmap2.eraseColor(0);
                                        canvas = new Canvas(createBitmap2);
                                        if (decodeFile != null) {
                                        }
                                        canvas.setBitmap(bitmap);
                                        ((ImageView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap2);
                                        i = tLRPC$TL_dialog2.unread_count;
                                        if (i > 0) {
                                        }
                                        i2 = 1;
                                    }
                                } else {
                                    TLRPC$Chat chat2 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$TL_dialog2.id));
                                    str = chat2.title;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat2.photo;
                                    if (tLRPC$ChatPhoto != null && (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) != null) {
                                        if (tLRPC$FileLocation2.volume_id != 0 && tLRPC$FileLocation2.local_id != 0) {
                                            tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                            tLRPC$Chat2 = chat2;
                                            tLRPC$User2 = null;
                                            tLRPC$Chat = tLRPC$Chat2;
                                            tLRPC$User = tLRPC$User2;
                                            tLRPC$FileLocation = tLRPC$FileLocation3;
                                            ((TextView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                            if (tLRPC$FileLocation == null) {
                                            }
                                            int dp22 = AndroidUtilities.dp(48.0f);
                                            Bitmap createBitmap22 = Bitmap.createBitmap(dp22, dp22, Bitmap.Config.ARGB_8888);
                                            createBitmap22.eraseColor(0);
                                            canvas = new Canvas(createBitmap22);
                                            if (decodeFile != null) {
                                            }
                                            canvas.setBitmap(bitmap);
                                            ((ImageView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap22);
                                            i = tLRPC$TL_dialog2.unread_count;
                                            if (i > 0) {
                                            }
                                            i2 = 1;
                                        }
                                    }
                                    tLRPC$Chat = chat2;
                                    tLRPC$FileLocation = null;
                                    tLRPC$User = null;
                                    ((TextView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                    if (tLRPC$FileLocation == null) {
                                    }
                                    int dp222 = AndroidUtilities.dp(48.0f);
                                    Bitmap createBitmap222 = Bitmap.createBitmap(dp222, dp222, Bitmap.Config.ARGB_8888);
                                    createBitmap222.eraseColor(0);
                                    canvas = new Canvas(createBitmap222);
                                    if (decodeFile != null) {
                                    }
                                    canvas.setBitmap(bitmap);
                                    ((ImageView) this.cells[i9].findViewById(i10 != 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap222);
                                    i = tLRPC$TL_dialog2.unread_count;
                                    if (i > 0) {
                                    }
                                    i2 = 1;
                                }
                            }
                            i10 += i2;
                            i8 = 2;
                        }
                        i9++;
                        i8 = 2;
                    }
                }
            } else {
                int i12 = 0;
                while (i12 < i6) {
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
                        this.cells[i12].setVisibility(i5);
                        i3 = i12;
                    } else {
                        this.cells[i12].setVisibility(i7);
                        if (DialogObject.isUserDialog(tLRPC$Dialog3.id)) {
                            tLRPC$User3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$Dialog3.id));
                            if (tLRPC$User3 != null) {
                                if (UserObject.isUserSelf(tLRPC$User3)) {
                                    str2 = LocaleController.getString(R.string.SavedMessages);
                                } else if (UserObject.isReplyUser(tLRPC$User3)) {
                                    str2 = LocaleController.getString(R.string.RepliesTitle);
                                } else if (UserObject.isDeleted(tLRPC$User3)) {
                                    str2 = LocaleController.getString(R.string.HiddenName);
                                } else {
                                    str2 = ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name);
                                }
                                if (!UserObject.isReplyUser(tLRPC$User3) && !UserObject.isUserSelf(tLRPC$User3) && (tLRPC$UserProfilePhoto2 = tLRPC$User3.photo) != null && (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto2.photo_small) != null && tLRPC$FileLocation6.volume_id != j && tLRPC$FileLocation6.local_id != 0) {
                                    tLRPC$FileLocation5 = tLRPC$FileLocation6;
                                    tLRPC$Chat3 = null;
                                }
                            } else {
                                str2 = "";
                            }
                            tLRPC$FileLocation5 = null;
                            tLRPC$Chat3 = null;
                        } else {
                            TLRPC$Chat chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog3.id));
                            if (chat3 != null) {
                                str2 = chat3.title;
                                TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat3.photo;
                                if (tLRPC$ChatPhoto2 == null || (tLRPC$FileLocation5 = tLRPC$ChatPhoto2.photo_small) == null || tLRPC$FileLocation5.volume_id == j || tLRPC$FileLocation5.local_id == 0) {
                                    tLRPC$Chat3 = chat3;
                                } else {
                                    tLRPC$Chat3 = chat3;
                                    tLRPC$User3 = null;
                                }
                            } else {
                                tLRPC$Chat3 = chat3;
                                str2 = "";
                            }
                            tLRPC$User3 = null;
                            tLRPC$FileLocation5 = null;
                        }
                        ((TextView) this.cells[i12].findViewById(R.id.shortcut_widget_item_text)).setText(str2);
                        if (tLRPC$FileLocation5 != null) {
                            try {
                                decodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation5, true).toString());
                            } catch (Throwable th6) {
                                FileLog.e(th6);
                            }
                        } else {
                            decodeFile2 = null;
                        }
                        int dp3 = AndroidUtilities.dp(48.0f);
                        Bitmap createBitmap3 = Bitmap.createBitmap(dp3, dp3, Bitmap.Config.ARGB_8888);
                        createBitmap3.eraseColor(0);
                        Canvas canvas2 = new Canvas(createBitmap3);
                        if (decodeFile2 == null) {
                            if (tLRPC$User3 != null) {
                                avatarDrawable2 = new AvatarDrawable(tLRPC$User3);
                                if (UserObject.isReplyUser(tLRPC$User3)) {
                                    avatarDrawable2.setAvatarType(12);
                                } else if (UserObject.isUserSelf(tLRPC$User3)) {
                                    avatarDrawable2.setAvatarType(1);
                                }
                            } else {
                                avatarDrawable2 = new AvatarDrawable(tLRPC$Chat3);
                            }
                            avatarDrawable2.setBounds(0, 0, dp3, dp3);
                            avatarDrawable2.draw(canvas2);
                        } else {
                            Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
                            BitmapShader bitmapShader2 = new BitmapShader(decodeFile2, tileMode2, tileMode2);
                            if (this.roundPaint == null) {
                                this.roundPaint = new Paint(1);
                                this.bitmapRect = new RectF();
                            }
                            float width2 = dp3 / decodeFile2.getWidth();
                            canvas2.save();
                            canvas2.scale(width2, width2);
                            this.roundPaint.setShader(bitmapShader2);
                            this.bitmapRect.set(0.0f, 0.0f, decodeFile2.getWidth(), decodeFile2.getHeight());
                            canvas2.drawRoundRect(this.bitmapRect, decodeFile2.getWidth(), decodeFile2.getHeight(), this.roundPaint);
                            canvas2.restore();
                        }
                        canvas2.setBitmap(null);
                        ((ImageView) this.cells[i12].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(createBitmap3);
                        ArrayList<MessageObject> arrayList = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                        MessageObject messageObject = (arrayList == null || arrayList.size() <= 0) ? null : arrayList.get(0);
                        if (messageObject != null) {
                            long fromChatId = messageObject.getFromChatId();
                            if (fromChatId > j) {
                                tLRPC$User4 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                chat = null;
                            } else {
                                chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                tLRPC$User4 = null;
                            }
                            int color = getContext().getResources().getColor(R.color.widget_text);
                            if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                                if (ChatObject.isChannel(tLRPC$Chat3)) {
                                    TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                                    charSequence4 = "";
                                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                                        charSequence4 = "";
                                    }
                                    color = getContext().getResources().getColor(R.color.widget_action_text);
                                    i3 = i12;
                                    charSequence3 = charSequence4;
                                }
                                charSequence4 = messageObject.messageText;
                                color = getContext().getResources().getColor(R.color.widget_action_text);
                                i3 = i12;
                                charSequence3 = charSequence4;
                            } else {
                                i3 = i12;
                                if (tLRPC$Chat3 != null && tLRPC$Chat3.id > j && chat == null && (!ChatObject.isChannel(tLRPC$Chat3) || ChatObject.isMegagroup(tLRPC$Chat3))) {
                                    if (messageObject.isOutOwner()) {
                                        str4 = LocaleController.getString(R.string.FromYou);
                                    } else if (tLRPC$User4 != null) {
                                        str4 = UserObject.getFirstName(tLRPC$User4).replace("\n", "");
                                    } else {
                                        str4 = "DELETED";
                                    }
                                    String str6 = str4;
                                    CharSequence charSequence5 = messageObject.caption;
                                    try {
                                        if (charSequence5 != null) {
                                            String charSequence6 = charSequence5.toString();
                                            if (charSequence6.length() > 150) {
                                                charSequence6 = charSequence6.substring(0, 150);
                                            }
                                            if (messageObject.isVideo()) {
                                                str5 = "📹 ";
                                            } else if (messageObject.isVoice()) {
                                                str5 = "🎤 ";
                                            } else if (messageObject.isMusic()) {
                                                str5 = "🎧 ";
                                            } else {
                                                str5 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                            }
                                            valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str5 + charSequence6.replace('\n', ' '), str6));
                                        } else if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                            color = getContext().getResources().getColor(R.color.widget_action_text);
                                            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                c2 = 1;
                                                c3 = 0;
                                                charSequence2 = String.format("📊 \u2068%s\u2069", ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.question.text);
                                            } else {
                                                c2 = 1;
                                                c3 = 0;
                                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                    charSequence2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
                                                } else {
                                                    if (messageObject.type == 14) {
                                                        i4 = 2;
                                                        charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                    } else {
                                                        i4 = 2;
                                                        charSequence2 = messageObject.messageText.toString();
                                                    }
                                                    String replace = charSequence2.replace('\n', ' ');
                                                    Object[] objArr = new Object[i4];
                                                    objArr[c3] = replace;
                                                    objArr[c2] = str6;
                                                    SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                                    valueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str6.length() + 2, valueOf2.length(), 33);
                                                    spannableStringBuilder = valueOf2;
                                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str6.length() + 1, 33);
                                                    charSequence3 = spannableStringBuilder;
                                                }
                                            }
                                            i4 = 2;
                                            String replace2 = charSequence2.replace('\n', ' ');
                                            Object[] objArr2 = new Object[i4];
                                            objArr2[c3] = replace2;
                                            objArr2[c2] = str6;
                                            SpannableStringBuilder valueOf22 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                            valueOf22.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str6.length() + 2, valueOf22.length(), 33);
                                            spannableStringBuilder = valueOf22;
                                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str6.length() + 1, 33);
                                            charSequence3 = spannableStringBuilder;
                                        } else {
                                            String str7 = messageObject.messageOwner.message;
                                            if (str7 != null) {
                                                if (str7.length() > 150) {
                                                    c = 0;
                                                    str7 = str7.substring(0, 150);
                                                } else {
                                                    c = 0;
                                                }
                                                Object[] objArr3 = new Object[2];
                                                objArr3[c] = str7.replace('\n', ' ').trim();
                                                objArr3[1] = str6;
                                                valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr3));
                                            } else {
                                                valueOf = SpannableStringBuilder.valueOf("");
                                            }
                                        }
                                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str6.length() + 1, 33);
                                        charSequence3 = spannableStringBuilder;
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                        charSequence3 = spannableStringBuilder;
                                    }
                                    spannableStringBuilder = valueOf;
                                } else {
                                    TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                                    if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                        charSequence3 = LocaleController.getString(R.string.AttachPhotoExpired);
                                    } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                        charSequence3 = LocaleController.getString(R.string.AttachVideoExpired);
                                    } else if (messageObject.caption != null) {
                                        if (messageObject.isVideo()) {
                                            str3 = "📹 ";
                                        } else if (messageObject.isVoice()) {
                                            str3 = "🎤 ";
                                        } else if (messageObject.isMusic()) {
                                            str3 = "🎧 ";
                                        } else {
                                            str3 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                        }
                                        charSequence3 = str3 + ((Object) messageObject.caption);
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
                            }
                            ((TextView) this.cells[i3].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                            ViewGroup viewGroup = this.cells[i3];
                            int i13 = R.id.shortcut_widget_item_message;
                            ((TextView) viewGroup.findViewById(i13)).setText(charSequence3.toString());
                            ((TextView) this.cells[i3].findViewById(i13)).setTextColor(color);
                        } else {
                            i3 = i12;
                            if (tLRPC$Dialog3.last_message_date != 0) {
                                ((TextView) this.cells[i3].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(tLRPC$Dialog3.last_message_date));
                            } else {
                                ((TextView) this.cells[i3].findViewById(R.id.shortcut_widget_item_time)).setText("");
                            }
                            ((TextView) this.cells[i3].findViewById(R.id.shortcut_widget_item_message)).setText("");
                        }
                        if (tLRPC$Dialog3.unread_count > 0) {
                            ViewGroup viewGroup2 = this.cells[i3];
                            int i14 = R.id.shortcut_widget_item_badge;
                            ((TextView) viewGroup2.findViewById(i14)).setText(String.format("%d", Integer.valueOf(tLRPC$Dialog3.unread_count)));
                            this.cells[i3].findViewById(i14).setVisibility(0);
                            if (EditWidgetActivity.this.getMessagesController().isDialogMuted(tLRPC$Dialog3.id, 0L)) {
                                this.cells[i3].findViewById(i14).setBackgroundResource(R.drawable.widget_counter_muted);
                            } else {
                                this.cells[i3].findViewById(i14).setBackgroundResource(R.drawable.widget_counter);
                            }
                        } else {
                            this.cells[i3].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                        }
                    }
                    i12 = i3 + 1;
                    i5 = 8;
                    j = 0;
                    i6 = 2;
                    i7 = 0;
                }
                ViewGroup viewGroup3 = this.cells[0];
                int i15 = R.id.shortcut_widget_item_divider;
                viewGroup3.findViewById(i15).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(i15).setVisibility(8);
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
            this.actionBar.setTitle(LocaleController.getString(R.string.WidgetChats));
        } else {
            this.actionBar.setTitle(LocaleController.getString(R.string.WidgetShortcuts));
        }
        this.actionBar.createMenu().addItem(1, LocaleController.getString(R.string.Done).toUpperCase());
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
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Delete)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.EditWidgetActivity$2$$ExternalSyntheticLambda0
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
                            spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EditWidgetContactsInfo));
                        }
                    } else {
                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EditWidgetChatsInfo));
                    }
                    if (SharedConfig.passcodeHash.length() > 0) {
                        spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(R.string.WidgetPasscode2)));
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
                textCell.setTextAndIcon(LocaleController.getString(R.string.SelectChats), new CombinedDrawable(drawable, drawable2), EditWidgetActivity.this.chatsStartRow != -1);
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
