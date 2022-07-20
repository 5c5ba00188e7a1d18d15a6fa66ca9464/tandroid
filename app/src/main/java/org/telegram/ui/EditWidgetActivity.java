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
/* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
    public interface EditWidgetActivityDelegate {
        void didSelectDialogs(ArrayList<Long> arrayList);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    /* loaded from: classes3.dex */
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
            EditWidgetActivity.this = r1;
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
            boolean z = false;
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            int adapterPosition = viewHolder.getAdapterPosition();
            int adapterPosition2 = viewHolder2.getAdapterPosition();
            if (EditWidgetActivity.this.listAdapter.swapElements(adapterPosition, adapterPosition2)) {
                ((GroupCreateUserCell) viewHolder.itemView).setDrawDivider(adapterPosition2 != EditWidgetActivity.this.chatsEndRow - 1);
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder2.itemView;
                if (adapterPosition != EditWidgetActivity.this.chatsEndRow - 1) {
                    z = true;
                }
                groupCreateUserCell.setDrawDivider(z);
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

    /* loaded from: classes3.dex */
    public class WidgetPreviewCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
        private Drawable oldBackgroundDrawable;
        private BackgroundGradientDrawable.Disposable oldBackgroundGradientDisposable;
        private Drawable shadowDrawable;
        private Paint roundPaint = new Paint(1);
        private RectF bitmapRect = new RectF();
        private ViewGroup[] cells = new ViewGroup[2];

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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WidgetPreviewCell(Context context) {
            super(context);
            EditWidgetActivity.this = r19;
            int i = 0;
            setWillNotDraw(false);
            setPadding(0, AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
            ChatActionCell chatActionCell = new ChatActionCell(context);
            chatActionCell.setCustomText(LocaleController.getString("WidgetPreview", 2131629247));
            linearLayout.addView(chatActionCell, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 4));
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            linearLayout2.setBackgroundResource(2131166229);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 17, 10, 0, 10, 0));
            r19.previewImageView = new ImageView(context);
            if (r19.widgetType == 0) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) r19.getParentActivity().getLayoutInflater().inflate(2131427349, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear(-1, -2));
                    i++;
                }
                linearLayout2.addView(r19.previewImageView, LayoutHelper.createLinear(218, 160, 17));
                r19.previewImageView.setImageResource(2131165344);
            } else if (r19.widgetType == 1) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) r19.getParentActivity().getLayoutInflater().inflate(2131427330, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear(160, -2));
                    i++;
                }
                linearLayout2.addView(r19.previewImageView, LayoutHelper.createLinear(160, 160, 17));
                r19.previewImageView.setImageResource(2131165370);
            }
            updateDialogs();
            this.shadowDrawable = Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow");
        }

        /* JADX WARN: Can't wrap try/catch for region: R(9:145|(2:147|(1:149)(1:150))(2:151|(2:153|(1:155)(1:156))(8:158|(2:160|(1:162)(1:163))(1:164)|165|389|166|169|395|178))|157|165|389|166|169|395|178) */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x028a, code lost:
            if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) != false) goto L105;
         */
        /* JADX WARN: Code restructure failed: missing block: B:167:0x0418, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:168:0x0419, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:20:0x008e  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0099  */
        /* JADX WARN: Removed duplicated region for block: B:228:0x0591  */
        /* JADX WARN: Removed duplicated region for block: B:235:0x05d4  */
        /* JADX WARN: Removed duplicated region for block: B:239:0x0629  */
        /* JADX WARN: Removed duplicated region for block: B:358:0x08dd  */
        /* JADX WARN: Removed duplicated region for block: B:371:0x092d  */
        /* JADX WARN: Removed duplicated region for block: B:385:0x016a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:70:0x017e  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0193  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x01be A[Catch: all -> 0x0222, TryCatch #1 {all -> 0x0222, blocks: (B:69:0x016a, B:71:0x017f, B:74:0x0195, B:76:0x01a0, B:77:0x01a6, B:79:0x01ac, B:80:0x01b1, B:81:0x01b6, B:83:0x01be, B:85:0x01c9, B:86:0x01d8, B:87:0x020e), top: B:385:0x016a }] */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0239  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateDialogs() {
            TLRPC$Dialog tLRPC$Dialog;
            String str;
            String str2;
            TLRPC$Chat tLRPC$Chat;
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$User tLRPC$User;
            Bitmap decodeFile;
            int i;
            Bitmap createBitmap;
            Canvas canvas;
            AvatarDrawable avatarDrawable;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$FileLocation tLRPC$FileLocation2;
            TLRPC$Dialog tLRPC$Dialog2;
            TLRPC$Dialog tLRPC$Dialog3;
            String str3;
            CharSequence charSequence;
            TLRPC$FileLocation tLRPC$FileLocation3;
            TLRPC$Chat tLRPC$Chat2;
            TLRPC$User tLRPC$User2;
            Bitmap decodeFile2;
            MessageObject messageObject;
            TLRPC$User tLRPC$User3;
            TLRPC$Chat tLRPC$Chat3;
            CharSequence charSequence2;
            CharSequence charSequence3;
            String str4;
            String replace;
            SpannableStringBuilder spannableStringBuilder;
            SpannableStringBuilder spannableStringBuilder2;
            char c;
            String str5;
            String str6;
            CharSequence charSequence4;
            AvatarDrawable avatarDrawable2;
            TLRPC$FileLocation tLRPC$FileLocation4;
            CharSequence charSequence5;
            CharSequence charSequence6;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
            TLRPC$FileLocation tLRPC$FileLocation5;
            TLRPC$Dialog tLRPC$Dialog4;
            float f = 48.0f;
            int i2 = 2131628077;
            String str7 = "SavedMessages";
            float f2 = 0.0f;
            long j = 0;
            int i3 = 8;
            if (EditWidgetActivity.this.widgetType != 0) {
                String str8 = str7;
                if (EditWidgetActivity.this.widgetType == 1) {
                    int i4 = 0;
                    int i5 = 2;
                    while (i4 < i5) {
                        int i6 = 0;
                        while (i6 < i5) {
                            int i7 = (i4 * 2) + i6;
                            if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                                if (i7 < EditWidgetActivity.this.selectedDialogs.size()) {
                                    tLRPC$Dialog2 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i7)).longValue());
                                    if (tLRPC$Dialog2 == null) {
                                        tLRPC$Dialog = new TLRPC$TL_dialog();
                                        tLRPC$Dialog.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i7)).longValue();
                                    }
                                    tLRPC$Dialog = tLRPC$Dialog2;
                                }
                                tLRPC$Dialog = null;
                            } else {
                                if (i7 < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                    long j2 = EditWidgetActivity.this.getMediaDataController().hints.get(i7).peer.user_id;
                                    TLRPC$Dialog tLRPC$Dialog5 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(j2);
                                    if (tLRPC$Dialog5 == null) {
                                        tLRPC$Dialog5 = new TLRPC$TL_dialog();
                                        tLRPC$Dialog5.id = j2;
                                    }
                                    tLRPC$Dialog2 = tLRPC$Dialog5;
                                    tLRPC$Dialog = tLRPC$Dialog2;
                                }
                                tLRPC$Dialog = null;
                            }
                            int i8 = 2131230792;
                            if (tLRPC$Dialog == null) {
                                ViewGroup viewGroup = this.cells[i4];
                                if (i6 != 0) {
                                    i8 = 2131230793;
                                }
                                viewGroup.findViewById(i8).setVisibility(4);
                                if (i7 == 0 || i7 == 2) {
                                    this.cells[i4].setVisibility(8);
                                }
                                str = str8;
                            } else {
                                ViewGroup viewGroup2 = this.cells[i4];
                                if (i6 != 0) {
                                    i8 = 2131230793;
                                }
                                viewGroup2.findViewById(i8).setVisibility(0);
                                if (i7 == 0 || i7 == 2) {
                                    this.cells[i4].setVisibility(0);
                                }
                                if (DialogObject.isUserDialog(tLRPC$Dialog.id)) {
                                    tLRPC$User = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$Dialog.id));
                                    if (UserObject.isUserSelf(tLRPC$User)) {
                                        str = str8;
                                        str2 = LocaleController.getString(str, 2131628077);
                                    } else {
                                        str = str8;
                                        if (UserObject.isReplyUser(tLRPC$User)) {
                                            str2 = LocaleController.getString("RepliesTitle", 2131627920);
                                        } else if (UserObject.isDeleted(tLRPC$User)) {
                                            str2 = LocaleController.getString("HiddenName", 2131626131);
                                        } else {
                                            str2 = UserObject.getFirstName(tLRPC$User);
                                        }
                                    }
                                    tLRPC$FileLocation = (UserObject.isReplyUser(tLRPC$User) || UserObject.isUserSelf(tLRPC$User) || tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation2.volume_id == j || tLRPC$FileLocation2.local_id == 0) ? null : tLRPC$FileLocation2;
                                    tLRPC$Chat = null;
                                } else {
                                    str = str8;
                                    TLRPC$Chat chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id));
                                    str2 = chat.title;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null || tLRPC$FileLocation.volume_id == j || tLRPC$FileLocation.local_id == 0) {
                                        tLRPC$Chat = chat;
                                        tLRPC$User = null;
                                        tLRPC$FileLocation = null;
                                    } else {
                                        tLRPC$Chat = chat;
                                        tLRPC$User = null;
                                    }
                                }
                                ((TextView) this.cells[i4].findViewById(i6 == 0 ? 2131230800 : 2131230801)).setText(str2);
                                if (tLRPC$FileLocation != null) {
                                    try {
                                        decodeFile = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation, true).toString());
                                    } catch (Throwable th) {
                                        th = th;
                                        FileLog.e(th);
                                        i = tLRPC$Dialog.unread_count;
                                        if (i <= 0) {
                                        }
                                        i6++;
                                        str8 = str;
                                        i5 = 2;
                                        j = 0;
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
                                        float width = dp / decodeFile.getWidth();
                                        canvas.save();
                                        canvas.scale(width, width);
                                        this.roundPaint.setShader(bitmapShader);
                                        try {
                                            this.bitmapRect.set(0.0f, 0.0f, decodeFile.getWidth(), decodeFile.getHeight());
                                            canvas.drawRoundRect(this.bitmapRect, decodeFile.getWidth(), decodeFile.getHeight(), this.roundPaint);
                                            canvas.restore();
                                        } catch (Throwable th2) {
                                            th = th2;
                                            FileLog.e(th);
                                            i = tLRPC$Dialog.unread_count;
                                            if (i <= 0) {
                                            }
                                            i6++;
                                            str8 = str;
                                            i5 = 2;
                                            j = 0;
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                                try {
                                    canvas.setBitmap(null);
                                    ((ImageView) this.cells[i4].findViewById(i6 == 0 ? 2131230794 : 2131230795)).setImageBitmap(createBitmap);
                                } catch (Throwable th4) {
                                    th = th4;
                                    FileLog.e(th);
                                    i = tLRPC$Dialog.unread_count;
                                    if (i <= 0) {
                                    }
                                    i6++;
                                    str8 = str;
                                    i5 = 2;
                                    j = 0;
                                }
                                i = tLRPC$Dialog.unread_count;
                                if (i <= 0) {
                                    ((TextView) this.cells[i4].findViewById(i6 == 0 ? 2131230796 : 2131230797)).setText(i > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(i)));
                                    this.cells[i4].findViewById(i6 == 0 ? 2131230798 : 2131230799).setVisibility(0);
                                } else {
                                    this.cells[i4].findViewById(i6 == 0 ? 2131230798 : 2131230799).setVisibility(8);
                                }
                            }
                            i6++;
                            str8 = str;
                            i5 = 2;
                            j = 0;
                        }
                        i4++;
                        i5 = 2;
                        j = 0;
                    }
                }
            } else {
                int i9 = 0;
                for (int i10 = 2; i9 < i10; i10 = 2) {
                    if (!EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                        if (i9 < EditWidgetActivity.this.selectedDialogs.size()) {
                            tLRPC$Dialog4 = EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i9)).longValue());
                            if (tLRPC$Dialog4 == null) {
                                tLRPC$Dialog4 = new TLRPC$TL_dialog();
                                tLRPC$Dialog4.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i9)).longValue();
                            }
                        } else {
                            tLRPC$Dialog3 = null;
                            if (tLRPC$Dialog3 != null) {
                                this.cells[i9].setVisibility(i3);
                                str3 = str7;
                            } else {
                                this.cells[i9].setVisibility(0);
                                if (DialogObject.isUserDialog(tLRPC$Dialog3.id)) {
                                    tLRPC$User2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$Dialog3.id));
                                    if (tLRPC$User2 != null) {
                                        if (UserObject.isUserSelf(tLRPC$User2)) {
                                            charSequence5 = LocaleController.getString(str7, i2);
                                        } else if (UserObject.isReplyUser(tLRPC$User2)) {
                                            charSequence5 = LocaleController.getString("RepliesTitle", 2131627920);
                                        } else if (UserObject.isDeleted(tLRPC$User2)) {
                                            charSequence5 = LocaleController.getString("HiddenName", 2131626131);
                                        } else {
                                            charSequence5 = ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name);
                                        }
                                        if (UserObject.isReplyUser(tLRPC$User2) || UserObject.isUserSelf(tLRPC$User2) || (tLRPC$UserProfilePhoto2 = tLRPC$User2.photo) == null || (tLRPC$FileLocation5 = tLRPC$UserProfilePhoto2.photo_small) == null) {
                                            charSequence6 = charSequence5;
                                        } else {
                                            charSequence6 = charSequence5;
                                            if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                                tLRPC$FileLocation3 = tLRPC$FileLocation5;
                                                charSequence = charSequence6;
                                                tLRPC$Chat2 = null;
                                                str3 = str7;
                                                ((TextView) this.cells[i9].findViewById(2131230917)).setText(charSequence);
                                                if (tLRPC$FileLocation3 != null) {
                                                    try {
                                                        decodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(tLRPC$FileLocation3, true).toString());
                                                    } catch (Throwable th5) {
                                                        FileLog.e(th5);
                                                    }
                                                } else {
                                                    decodeFile2 = null;
                                                }
                                                int dp2 = AndroidUtilities.dp(f);
                                                Bitmap createBitmap2 = Bitmap.createBitmap(dp2, dp2, Bitmap.Config.ARGB_8888);
                                                createBitmap2.eraseColor(0);
                                                Canvas canvas2 = new Canvas(createBitmap2);
                                                if (decodeFile2 == null) {
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
                                                    this.bitmapRect.set(f2, f2, decodeFile2.getWidth(), decodeFile2.getHeight());
                                                    canvas2.drawRoundRect(this.bitmapRect, decodeFile2.getWidth(), decodeFile2.getHeight(), this.roundPaint);
                                                    canvas2.restore();
                                                }
                                                canvas2.setBitmap(null);
                                                ((ImageView) this.cells[i9].findViewById(2131230913)).setImageBitmap(createBitmap2);
                                                messageObject = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                                                if (messageObject != null) {
                                                    long fromChatId = messageObject.getFromChatId();
                                                    if (fromChatId > 0) {
                                                        tLRPC$User3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                                        tLRPC$Chat3 = null;
                                                    } else {
                                                        tLRPC$Chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                                        tLRPC$User3 = null;
                                                    }
                                                    int color = getContext().getResources().getColor(2131034146);
                                                    if (messageObject.messageOwner instanceof TLRPC$TL_messageService) {
                                                        if (ChatObject.isChannel(tLRPC$Chat2)) {
                                                            TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
                                                            charSequence4 = "";
                                                            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                                charSequence4 = "";
                                                            }
                                                            color = getContext().getResources().getColor(2131034141);
                                                            charSequence2 = charSequence4;
                                                        }
                                                        charSequence4 = messageObject.messageText;
                                                        color = getContext().getResources().getColor(2131034141);
                                                        charSequence2 = charSequence4;
                                                    } else if (tLRPC$Chat2 != null && tLRPC$Chat2.id > 0 && tLRPC$Chat3 == null && (!ChatObject.isChannel(tLRPC$Chat2) || ChatObject.isMegagroup(tLRPC$Chat2))) {
                                                        if (messageObject.isOutOwner()) {
                                                            replace = LocaleController.getString("FromYou", 2131626036);
                                                        } else {
                                                            replace = tLRPC$User3 != null ? UserObject.getFirstName(tLRPC$User3).replace("\n", "") : "DELETED";
                                                        }
                                                        String str9 = replace;
                                                        CharSequence charSequence7 = messageObject.caption;
                                                        try {
                                                            if (charSequence7 != null) {
                                                                String charSequence8 = charSequence7.toString();
                                                                if (charSequence8.length() > 150) {
                                                                    charSequence8 = charSequence8.substring(0, 150);
                                                                }
                                                                if (messageObject.isVideo()) {
                                                                    str6 = "📹 ";
                                                                } else if (messageObject.isVoice()) {
                                                                    str6 = "🎤 ";
                                                                } else if (messageObject.isMusic()) {
                                                                    str6 = "🎧 ";
                                                                } else {
                                                                    str6 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                                                }
                                                                spannableStringBuilder2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str6 + charSequence8.replace('\n', ' '), str9));
                                                            } else if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                                                int color2 = getContext().getResources().getColor(2131034141);
                                                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                    str5 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                    str5 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                } else {
                                                                    if (messageObject.type == 14) {
                                                                        if (Build.VERSION.SDK_INT >= 18) {
                                                                            c = 1;
                                                                            str5 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                                        } else {
                                                                            c = 1;
                                                                            str5 = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                                        }
                                                                    } else {
                                                                        c = 1;
                                                                        str5 = messageObject.messageText.toString();
                                                                    }
                                                                    Object[] objArr = new Object[2];
                                                                    objArr[0] = str5.replace('\n', ' ');
                                                                    objArr[c] = str9;
                                                                    SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                                                    valueOf.setSpan(new ForegroundColorSpanThemable("chats_attachMessage"), str9.length() + 2, valueOf.length(), 33);
                                                                    spannableStringBuilder = valueOf;
                                                                    color = color2;
                                                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str9.length() + 1, 33);
                                                                    charSequence2 = spannableStringBuilder;
                                                                }
                                                                c = 1;
                                                                Object[] objArr2 = new Object[2];
                                                                objArr2[0] = str5.replace('\n', ' ');
                                                                objArr2[c] = str9;
                                                                SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                                                valueOf2.setSpan(new ForegroundColorSpanThemable("chats_attachMessage"), str9.length() + 2, valueOf2.length(), 33);
                                                                spannableStringBuilder = valueOf2;
                                                                color = color2;
                                                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str9.length() + 1, 33);
                                                                charSequence2 = spannableStringBuilder;
                                                            } else {
                                                                String str10 = messageObject.messageOwner.message;
                                                                if (str10 != null) {
                                                                    if (str10.length() > 150) {
                                                                        str10 = str10.substring(0, 150);
                                                                    }
                                                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", str10.replace('\n', ' ').trim(), str9));
                                                                } else {
                                                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf("");
                                                                }
                                                            }
                                                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_nameMessage"), 0, str9.length() + 1, 33);
                                                            charSequence2 = spannableStringBuilder;
                                                        } catch (Exception e) {
                                                            FileLog.e(e);
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
                                                                str4 = "📹 ";
                                                            } else if (messageObject.isVoice()) {
                                                                str4 = "🎤 ";
                                                            } else if (messageObject.isMusic()) {
                                                                str4 = "🎧 ";
                                                            } else {
                                                                str4 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                                                            }
                                                            charSequence2 = str4 + ((Object) messageObject.caption);
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
                                                            CharSequence charSequence9 = charSequence3;
                                                            charSequence2 = charSequence9;
                                                            if (messageObject.messageOwner.media != null) {
                                                                charSequence2 = charSequence9;
                                                                if (!messageObject.isMediaEmpty()) {
                                                                    color = getContext().getResources().getColor(2131034141);
                                                                    charSequence2 = charSequence9;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    ((TextView) this.cells[i9].findViewById(2131230918)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                                                    ((TextView) this.cells[i9].findViewById(2131230916)).setText(charSequence2.toString());
                                                    ((TextView) this.cells[i9].findViewById(2131230916)).setTextColor(color);
                                                } else {
                                                    if (tLRPC$Dialog3.last_message_date != 0) {
                                                        ((TextView) this.cells[i9].findViewById(2131230918)).setText(LocaleController.stringForMessageListDate(tLRPC$Dialog3.last_message_date));
                                                    } else {
                                                        ((TextView) this.cells[i9].findViewById(2131230918)).setText("");
                                                    }
                                                    ((TextView) this.cells[i9].findViewById(2131230916)).setText("");
                                                }
                                                if (tLRPC$Dialog3.unread_count > 0) {
                                                    ((TextView) this.cells[i9].findViewById(2131230914)).setText(String.format("%d", Integer.valueOf(tLRPC$Dialog3.unread_count)));
                                                    this.cells[i9].findViewById(2131230914).setVisibility(0);
                                                    if (EditWidgetActivity.this.getMessagesController().isDialogMuted(tLRPC$Dialog3.id)) {
                                                        this.cells[i9].findViewById(2131230914).setBackgroundResource(2131166233);
                                                    } else {
                                                        this.cells[i9].findViewById(2131230914).setBackgroundResource(2131166232);
                                                    }
                                                } else {
                                                    this.cells[i9].findViewById(2131230914).setVisibility(8);
                                                }
                                            }
                                        }
                                        charSequence = charSequence6;
                                        tLRPC$Chat2 = null;
                                        tLRPC$FileLocation3 = null;
                                        str3 = str7;
                                        ((TextView) this.cells[i9].findViewById(2131230917)).setText(charSequence);
                                        if (tLRPC$FileLocation3 != null) {
                                        }
                                        int dp22 = AndroidUtilities.dp(f);
                                        Bitmap createBitmap22 = Bitmap.createBitmap(dp22, dp22, Bitmap.Config.ARGB_8888);
                                        createBitmap22.eraseColor(0);
                                        Canvas canvas22 = new Canvas(createBitmap22);
                                        if (decodeFile2 == null) {
                                        }
                                        canvas22.setBitmap(null);
                                        ((ImageView) this.cells[i9].findViewById(2131230913)).setImageBitmap(createBitmap22);
                                        messageObject = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                                        if (messageObject != null) {
                                        }
                                        if (tLRPC$Dialog3.unread_count > 0) {
                                        }
                                    } else {
                                        str3 = str7;
                                        charSequence = "";
                                        tLRPC$Chat2 = null;
                                        tLRPC$FileLocation3 = null;
                                        ((TextView) this.cells[i9].findViewById(2131230917)).setText(charSequence);
                                        if (tLRPC$FileLocation3 != null) {
                                        }
                                        int dp222 = AndroidUtilities.dp(f);
                                        Bitmap createBitmap222 = Bitmap.createBitmap(dp222, dp222, Bitmap.Config.ARGB_8888);
                                        createBitmap222.eraseColor(0);
                                        Canvas canvas222 = new Canvas(createBitmap222);
                                        if (decodeFile2 == null) {
                                        }
                                        canvas222.setBitmap(null);
                                        ((ImageView) this.cells[i9].findViewById(2131230913)).setImageBitmap(createBitmap222);
                                        messageObject = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                                        if (messageObject != null) {
                                        }
                                        if (tLRPC$Dialog3.unread_count > 0) {
                                        }
                                    }
                                } else {
                                    TLRPC$Chat chat2 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog3.id));
                                    if (chat2 != null) {
                                        charSequence = chat2.title;
                                        TLRPC$ChatPhoto tLRPC$ChatPhoto2 = chat2.photo;
                                        if (tLRPC$ChatPhoto2 == null || (tLRPC$FileLocation4 = tLRPC$ChatPhoto2.photo_small) == null) {
                                            str3 = str7;
                                        } else {
                                            str3 = str7;
                                            if (tLRPC$FileLocation4.volume_id != 0 && tLRPC$FileLocation4.local_id != 0) {
                                                tLRPC$Chat2 = chat2;
                                                tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                                tLRPC$User2 = null;
                                                ((TextView) this.cells[i9].findViewById(2131230917)).setText(charSequence);
                                                if (tLRPC$FileLocation3 != null) {
                                                }
                                                int dp2222 = AndroidUtilities.dp(f);
                                                Bitmap createBitmap2222 = Bitmap.createBitmap(dp2222, dp2222, Bitmap.Config.ARGB_8888);
                                                createBitmap2222.eraseColor(0);
                                                Canvas canvas2222 = new Canvas(createBitmap2222);
                                                if (decodeFile2 == null) {
                                                }
                                                canvas2222.setBitmap(null);
                                                ((ImageView) this.cells[i9].findViewById(2131230913)).setImageBitmap(createBitmap2222);
                                                messageObject = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                                                if (messageObject != null) {
                                                }
                                                if (tLRPC$Dialog3.unread_count > 0) {
                                                }
                                            }
                                        }
                                    } else {
                                        str3 = str7;
                                        charSequence = "";
                                    }
                                    tLRPC$Chat2 = chat2;
                                    tLRPC$User2 = null;
                                    tLRPC$FileLocation3 = null;
                                    ((TextView) this.cells[i9].findViewById(2131230917)).setText(charSequence);
                                    if (tLRPC$FileLocation3 != null) {
                                    }
                                    int dp22222 = AndroidUtilities.dp(f);
                                    Bitmap createBitmap22222 = Bitmap.createBitmap(dp22222, dp22222, Bitmap.Config.ARGB_8888);
                                    createBitmap22222.eraseColor(0);
                                    Canvas canvas22222 = new Canvas(createBitmap22222);
                                    if (decodeFile2 == null) {
                                    }
                                    canvas22222.setBitmap(null);
                                    ((ImageView) this.cells[i9].findViewById(2131230913)).setImageBitmap(createBitmap22222);
                                    messageObject = EditWidgetActivity.this.getMessagesController().dialogMessage.get(tLRPC$Dialog3.id);
                                    if (messageObject != null) {
                                    }
                                    if (tLRPC$Dialog3.unread_count > 0) {
                                    }
                                }
                            }
                            i9++;
                            str7 = str3;
                            f = 48.0f;
                            i2 = 2131628077;
                            f2 = 0.0f;
                            i3 = 8;
                        }
                    } else {
                        tLRPC$Dialog4 = i9 < EditWidgetActivity.this.getMessagesController().dialogsServerOnly.size() ? EditWidgetActivity.this.getMessagesController().dialogsServerOnly.get(i9) : null;
                    }
                    tLRPC$Dialog3 = tLRPC$Dialog4;
                    if (tLRPC$Dialog3 != null) {
                    }
                    i9++;
                    str7 = str3;
                    f = 48.0f;
                    i2 = 2131628077;
                    f2 = 0.0f;
                    i3 = 8;
                }
                this.cells[0].findViewById(2131230915).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(2131230915).setVisibility(8);
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
                        drawable.setAlpha(255);
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
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        if (this.widgetType == 0) {
            this.actionBar.setTitle(LocaleController.getString("WidgetChats", 2131629243));
        } else {
            this.actionBar.setTitle(LocaleController.getString("WidgetShortcuts", 2131629248));
        }
        this.actionBar.createMenu().addItem(1, LocaleController.getString("Done", 2131625525).toUpperCase());
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
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
        this.listView.setOnItemClickListener(new EditWidgetActivity$$ExternalSyntheticLambda2(this, context));
        this.listView.setOnItemLongClickListener(new AnonymousClass2());
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.EditWidgetActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            EditWidgetActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (EditWidgetActivity.this.delegate == null) {
                    EditWidgetActivity.this.finishActivity();
                } else {
                    EditWidgetActivity.this.finishFragment();
                }
            } else if (i != 1 || EditWidgetActivity.this.getParentActivity() == null) {
            } else {
                EditWidgetActivity.this.getMessagesStorage().putWidgetDialogs(EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.selectedDialogs);
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
    }

    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        if (i == this.selectChatsRow) {
            InviteMembersBottomSheet inviteMembersBottomSheet = new InviteMembersBottomSheet(context, this.currentAccount, null, 0L, this, null);
            inviteMembersBottomSheet.setDelegate(new EditWidgetActivity$$ExternalSyntheticLambda1(this), this.selectedDialogs);
            inviteMembersBottomSheet.setSelectedContacts(this.selectedDialogs);
            showDialog(inviteMembersBottomSheet);
        }
    }

    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        this.selectedDialogs.clear();
        this.selectedDialogs.addAll(arrayList);
        updateRows();
        WidgetPreviewCell widgetPreviewCell = this.widgetPreviewCell;
        if (widgetPreviewCell != null) {
            widgetPreviewCell.updateDialogs();
        }
    }

    /* renamed from: org.telegram.ui.EditWidgetActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements RecyclerListView.OnItemLongClickListenerExtended {
        private Rect rect = new Rect();

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onMove(float f, float f2) {
        }

        AnonymousClass2() {
            EditWidgetActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, int i, float f, float f2) {
            if (EditWidgetActivity.this.getParentActivity() != null && (view instanceof GroupCreateUserCell)) {
                ((ImageView) view.getTag(2131230875)).getHitRect(this.rect);
                if (!this.rect.contains((int) f, (int) f2)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditWidgetActivity.this.getParentActivity());
                    builder.setItems(new CharSequence[]{LocaleController.getString("Delete", 2131625368)}, new EditWidgetActivity$2$$ExternalSyntheticLambda0(this, i));
                    EditWidgetActivity.this.showDialog(builder.create());
                    return true;
                }
            }
            return false;
        }

        public /* synthetic */ void lambda$onItemClick$0(int i, DialogInterface dialogInterface, int i2) {
            if (i2 == 0) {
                EditWidgetActivity.this.selectedDialogs.remove(i - EditWidgetActivity.this.chatsStartRow);
                EditWidgetActivity.this.updateRows();
                if (EditWidgetActivity.this.widgetPreviewCell == null) {
                    return;
                }
                EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
            }
        }
    }

    public void finishActivity() {
        if (getParentActivity() == null) {
            return;
        }
        getParentActivity().finish();
        AndroidUtilities.runOnUIThread(new EditWidgetActivity$$ExternalSyntheticLambda0(this), 1000L);
    }

    /* loaded from: classes3.dex */
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
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 1 || itemViewType == 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GroupCreateUserCell groupCreateUserCell;
            if (i == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                groupCreateUserCell = textInfoPrivacyCell;
            } else if (i == 1) {
                TextCell textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                groupCreateUserCell = textCell;
            } else if (i == 2) {
                groupCreateUserCell = EditWidgetActivity.this.widgetPreviewCell = new WidgetPreviewCell(this.mContext);
            } else {
                GroupCreateUserCell groupCreateUserCell2 = new GroupCreateUserCell(this.mContext, 0, 0, false);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(2131165577);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                groupCreateUserCell2.setTag(2131230875, imageView);
                groupCreateUserCell2.addView(imageView, LayoutHelper.createFrame(40, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 10.0f, 0.0f, 10.0f, 0.0f));
                imageView.setOnTouchListener(new EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0(this, groupCreateUserCell2));
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_pinnedIcon"), PorterDuff.Mode.MULTIPLY));
                groupCreateUserCell = groupCreateUserCell2;
            }
            return new RecyclerListView.Holder(groupCreateUserCell);
        }

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
            boolean z = true;
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != EditWidgetActivity.this.infoRow) {
                    return;
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                if (EditWidgetActivity.this.widgetType != 0) {
                    if (EditWidgetActivity.this.widgetType == 1) {
                        spannableStringBuilder.append((CharSequence) LocaleController.getString("EditWidgetContactsInfo", 2131625595));
                    }
                } else {
                    spannableStringBuilder.append((CharSequence) LocaleController.getString("EditWidgetChatsInfo", 2131625594));
                }
                if (SharedConfig.passcodeHash.length() > 0) {
                    spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString("WidgetPasscode2", 2131629245)));
                }
                textInfoPrivacyCell.setText(spannableStringBuilder);
            } else if (itemViewType == 1) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(null, "windowBackgroundWhiteBlueText4");
                Drawable drawable = this.mContext.getResources().getDrawable(2131166078);
                Drawable drawable2 = this.mContext.getResources().getDrawable(2131166079);
                drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("switchTrackChecked"), PorterDuff.Mode.MULTIPLY));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("checkboxCheck"), PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable = new CombinedDrawable(drawable, drawable2);
                String string = LocaleController.getString("SelectChats", 2131628160);
                if (EditWidgetActivity.this.chatsStartRow == -1) {
                    z = false;
                }
                textCell.setTextAndIcon(string, combinedDrawable, z);
                textCell.getImageView().setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
            } else if (itemViewType != 3) {
            } else {
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                long longValue = ((Long) EditWidgetActivity.this.selectedDialogs.get(i - EditWidgetActivity.this.chatsStartRow)).longValue();
                if (DialogObject.isUserDialog(longValue)) {
                    TLRPC$User user = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(longValue));
                    if (i == EditWidgetActivity.this.chatsEndRow - 1) {
                        z = false;
                    }
                    groupCreateUserCell.setObject(user, null, null, z);
                    return;
                }
                TLRPC$Chat chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-longValue));
                if (i == EditWidgetActivity.this.chatsEndRow - 1) {
                    z = false;
                }
                groupCreateUserCell.setObject(chat, null, null, z);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 3 || itemViewType == 1) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
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
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
        return arrayList;
    }
}
