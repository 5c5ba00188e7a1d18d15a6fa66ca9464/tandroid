package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.CounterView;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes4.dex */
public class HintDialogCell extends FrameLayout {
    CheckBox2 checkBox;
    CounterView counterView;
    private TLRPC.User currentUser;
    private long dialogId;
    private final boolean drawCheckbox;
    private BackupImageView imageView;
    private int lastUnreadCount;
    private TextView nameTextView;
    float showOnlineProgress;
    boolean wasDraw;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private RectF rect = new RectF();
    private int currentAccount = UserConfig.selectedAccount;
    private String backgroundColorKey = Theme.key_windowBackgroundWhite;

    public HintDialogCell(Context context, boolean drawCheckbox) {
        super(context);
        this.drawCheckbox = drawCheckbox;
        BackupImageView backupImageView = new BackupImageView(context);
        this.imageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(27.0f));
        addView(this.imageView, LayoutHelper.createFrame(54, 54.0f, 49, 0.0f, 7.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.nameTextView = textView;
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setGravity(49);
        this.nameTextView.setLines(1);
        this.nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, 64.0f, 6.0f, 0.0f));
        CounterView counterView = new CounterView(context, null);
        this.counterView = counterView;
        addView(counterView, LayoutHelper.createFrame(-1, 28.0f, 48, 0.0f, 4.0f, 0.0f, 0.0f));
        this.counterView.setColors(Theme.key_chats_unreadCounterText, Theme.key_chats_unreadCounter);
        this.counterView.setGravity(5);
        if (drawCheckbox) {
            CheckBox2 checkBox2 = new CheckBox2(context, 21);
            this.checkBox = checkBox2;
            checkBox2.setColor(Theme.key_dialogRoundCheckBox, Theme.key_dialogBackground, Theme.key_dialogRoundCheckBoxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(4);
            this.checkBox.setProgressDelegate(new CheckBoxBase.ProgressDelegate() { // from class: org.telegram.ui.Cells.HintDialogCell$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.CheckBoxBase.ProgressDelegate
                public final void setProgress(float f) {
                    HintDialogCell.this.m1656lambda$new$0$orgtelegramuiCellsHintDialogCell(f);
                }
            });
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 49, 19.0f, 42.0f, 0.0f, 0.0f));
            this.checkBox.setChecked(false, false);
            setWillNotDraw(false);
        }
    }

    /* renamed from: lambda$new$0$org-telegram-ui-Cells-HintDialogCell */
    public /* synthetic */ void m1656lambda$new$0$orgtelegramuiCellsHintDialogCell(float progress) {
        float scale = 1.0f - (this.checkBox.getProgress() * 0.143f);
        this.imageView.setScaleX(scale);
        this.imageView.setScaleY(scale);
        invalidate();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), C.BUFFER_FLAG_ENCRYPTED));
        this.counterView.counterDrawable.horizontalPadding = AndroidUtilities.dp(13.0f);
    }

    public void update(int mask) {
        if ((MessagesController.UPDATE_MASK_STATUS & mask) != 0 && this.currentUser != null) {
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentUser.id));
            this.imageView.invalidate();
            invalidate();
        }
        if (mask != 0 && (MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE & mask) == 0 && (MessagesController.UPDATE_MASK_NEW_MESSAGE & mask) == 0) {
            return;
        }
        TLRPC.Dialog dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialogId);
        if (dialog != null && dialog.unread_count != 0) {
            if (this.lastUnreadCount != dialog.unread_count) {
                int i = dialog.unread_count;
                this.lastUnreadCount = i;
                this.counterView.setCount(i, this.wasDraw);
                return;
            }
            return;
        }
        this.lastUnreadCount = 0;
        this.counterView.setCount(0, this.wasDraw);
    }

    public void update() {
        if (DialogObject.isUserDialog(this.dialogId)) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
            this.currentUser = user;
            this.avatarDrawable.setInfo(user);
            return;
        }
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        this.avatarDrawable.setInfo(chat);
        this.currentUser = null;
    }

    public void setColors(String textColorKey, String backgroundColorKey) {
        this.nameTextView.setTextColor(Theme.getColor(textColorKey));
        this.backgroundColorKey = backgroundColorKey;
        this.checkBox.setColor(Theme.key_dialogRoundCheckBox, backgroundColorKey, Theme.key_dialogRoundCheckBoxCheck);
    }

    public void setDialog(long uid, boolean counter, CharSequence name) {
        if (this.dialogId != uid) {
            this.wasDraw = false;
            invalidate();
        }
        this.dialogId = uid;
        if (DialogObject.isUserDialog(uid)) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(uid));
            this.currentUser = user;
            if (name != null) {
                this.nameTextView.setText(name);
            } else if (user != null) {
                this.nameTextView.setText(UserObject.getFirstName(user));
            } else {
                this.nameTextView.setText("");
            }
            this.avatarDrawable.setInfo(this.currentUser);
            this.imageView.setForUserOrChat(this.currentUser, this.avatarDrawable);
        } else {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-uid));
            if (name != null) {
                this.nameTextView.setText(name);
            } else if (chat != null) {
                this.nameTextView.setText(chat.title);
            } else {
                this.nameTextView.setText("");
            }
            this.avatarDrawable.setInfo(chat);
            this.currentUser = null;
            this.imageView.setForUserOrChat(chat, this.avatarDrawable);
        }
        if (counter) {
            update(0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0083  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (child == this.imageView) {
            TLRPC.User user = this.currentUser;
            boolean showOnline = user != null && !user.bot && ((this.currentUser.status != null && this.currentUser.status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(this.currentUser.id)));
            if (!this.wasDraw) {
                this.showOnlineProgress = showOnline ? 1.0f : 0.0f;
            }
            if (showOnline) {
                float f = this.showOnlineProgress;
                if (f != 1.0f) {
                    float f2 = f + 0.10666667f;
                    this.showOnlineProgress = f2;
                    if (f2 > 1.0f) {
                        this.showOnlineProgress = 1.0f;
                    }
                    invalidate();
                    if (this.showOnlineProgress != 0.0f) {
                        int top = AndroidUtilities.dp(53.0f);
                        int left = AndroidUtilities.dp(59.0f);
                        canvas.save();
                        float f3 = this.showOnlineProgress;
                        canvas.scale(f3, f3, left, top);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(this.backgroundColorKey));
                        canvas.drawCircle(left, top, AndroidUtilities.dp(7.0f), Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle));
                        canvas.drawCircle(left, top, AndroidUtilities.dp(5.0f), Theme.dialogs_onlineCirclePaint);
                        canvas.restore();
                    }
                    this.wasDraw = true;
                }
            }
            if (!showOnline) {
                float f4 = this.showOnlineProgress;
                if (f4 != 0.0f) {
                    float f5 = f4 - 0.10666667f;
                    this.showOnlineProgress = f5;
                    if (f5 < 0.0f) {
                        this.showOnlineProgress = 0.0f;
                    }
                    invalidate();
                }
            }
            if (this.showOnlineProgress != 0.0f) {
            }
            this.wasDraw = true;
        }
        return result;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.drawCheckbox) {
            int cx = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() / 2);
            int cy = this.imageView.getTop() + (this.imageView.getMeasuredHeight() / 2);
            Theme.checkboxSquare_checkPaint.setColor(Theme.getColor(Theme.key_dialogRoundCheckBox));
            Theme.checkboxSquare_checkPaint.setAlpha((int) (this.checkBox.getProgress() * 255.0f));
            canvas.drawCircle(cx, cy, AndroidUtilities.dp(28.0f), Theme.checkboxSquare_checkPaint);
        }
    }

    public void setChecked(boolean checked, boolean animated) {
        if (this.drawCheckbox) {
            this.checkBox.setChecked(checked, animated);
        }
    }

    public long getDialogId() {
        return this.dialogId;
    }
}
