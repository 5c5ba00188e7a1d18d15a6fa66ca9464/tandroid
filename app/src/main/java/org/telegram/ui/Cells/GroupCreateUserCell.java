package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.google.android.exoplayer2.C;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes4.dex */
public class GroupCreateUserCell extends FrameLayout {
    private ValueAnimator animator;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox2 checkBox;
    private int checkBoxType;
    private float checkProgress;
    private int currentAccount;
    private CharSequence currentName;
    private Object currentObject;
    private CharSequence currentStatus;
    private boolean drawDivider;
    private boolean forceDarkTheme;
    private boolean isChecked;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private int padding;
    private Paint paint;
    private boolean showSelfAsSaved;
    private SimpleTextView statusTextView;

    public GroupCreateUserCell(Context context, int checkBoxType, int pad, boolean selfAsSaved) {
        this(context, checkBoxType, pad, selfAsSaved, false);
    }

    public GroupCreateUserCell(Context context, int checkBoxType, int pad, boolean selfAsSaved, boolean forCall) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.checkBoxType = checkBoxType;
        this.forceDarkTheme = forCall;
        this.drawDivider = false;
        this.padding = pad;
        this.showSelfAsSaved = selfAsSaved;
        this.avatarDrawable = new AvatarDrawable();
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(24.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(46, 46.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : this.padding + 13, 6.0f, LocaleController.isRTL ? this.padding + 13 : 0.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.nameTextView = simpleTextView;
        simpleTextView.setTextColor(Theme.getColor(this.forceDarkTheme ? Theme.key_voipgroup_nameText : Theme.key_windowBackgroundWhiteBlackText));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.nameTextView.setTextSize(16);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        int i = 28;
        addView(this.nameTextView, LayoutHelper.createFrame(-1, 20.0f, (LocaleController.isRTL ? 5 : 3) | 48, (LocaleController.isRTL ? 28 : 72) + this.padding, 10.0f, (LocaleController.isRTL ? 72 : 28) + this.padding, 0.0f));
        SimpleTextView simpleTextView2 = new SimpleTextView(context);
        this.statusTextView = simpleTextView2;
        simpleTextView2.setTextSize(14);
        this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.statusTextView, LayoutHelper.createFrame(-1, 20.0f, (LocaleController.isRTL ? 5 : 3) | 48, (LocaleController.isRTL ? 28 : 72) + this.padding, 32.0f, (LocaleController.isRTL ? 72 : i) + this.padding, 0.0f));
        if (checkBoxType == 1) {
            CheckBox2 checkBox2 = new CheckBox2(context, 21);
            this.checkBox = checkBox2;
            checkBox2.setColor(null, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : this.padding + 40, 33.0f, LocaleController.isRTL ? this.padding + 39 : 0.0f, 0.0f));
        } else if (checkBoxType == 2) {
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        }
        setWillNotDraw(false);
    }

    public void setObject(TLObject object, CharSequence name, CharSequence status, boolean drawDivider) {
        setObject(object, name, status);
        this.drawDivider = drawDivider;
    }

    public void setObject(Object object, CharSequence name, CharSequence status) {
        this.currentObject = object;
        this.currentStatus = status;
        this.currentName = name;
        this.drawDivider = false;
        update(0);
    }

    public void setChecked(boolean checked, boolean animated) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.setChecked(checked, animated);
        } else if (this.checkBoxType != 2 || this.isChecked == checked) {
        } else {
            this.isChecked = checked;
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (animated) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.GroupCreateUserCell$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        GroupCreateUserCell.this.m1655lambda$setChecked$0$orgtelegramuiCellsGroupCreateUserCell(valueAnimator2);
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.GroupCreateUserCell.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        GroupCreateUserCell.this.animator = null;
                    }
                });
                this.animator.setDuration(180L);
                this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.animator.start();
            } else {
                float f = 0.82f;
                float f2 = 1.0f;
                this.avatarImageView.setScaleX(this.isChecked ? 0.82f : 1.0f);
                BackupImageView backupImageView = this.avatarImageView;
                if (!this.isChecked) {
                    f = 1.0f;
                }
                backupImageView.setScaleY(f);
                if (!this.isChecked) {
                    f2 = 0.0f;
                }
                this.checkProgress = f2;
            }
            invalidate();
        }
    }

    /* renamed from: lambda$setChecked$0$org-telegram-ui-Cells-GroupCreateUserCell */
    public /* synthetic */ void m1655lambda$setChecked$0$orgtelegramuiCellsGroupCreateUserCell(ValueAnimator animation) {
        float v = ((Float) animation.getAnimatedValue()).floatValue();
        float scale = this.isChecked ? 1.0f - (0.18f * v) : 0.82f + (0.18f * v);
        this.avatarImageView.setScaleX(scale);
        this.avatarImageView.setScaleY(scale);
        this.checkProgress = this.isChecked ? v : 1.0f - v;
        invalidate();
    }

    public void setCheckBoxEnabled(boolean enabled) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.setEnabled(enabled);
        }
    }

    public boolean isChecked() {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            return checkBox2.isChecked();
        }
        return this.isChecked;
    }

    public Object getObject() {
        return this.currentObject;
    }

    public void setDrawDivider(boolean value) {
        this.drawDivider = value;
        invalidate();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.currentObject instanceof String ? 50.0f : 58.0f), C.BUFFER_FLAG_ENCRYPTED));
    }

    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b2, code lost:
        if (r1.equals("contacts") != false) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int mask) {
        TLRPC.FileLocation fileLocation;
        TLRPC.FileLocation fileLocation2;
        Object obj = this.currentObject;
        if (obj == null) {
            return;
        }
        TLRPC.FileLocation photo = null;
        String newName = null;
        boolean z = obj instanceof String;
        String str = Theme.key_voipgroup_lastSeenText;
        int i = 0;
        if (z) {
            ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(15.0f);
            ViewGroup.LayoutParams layoutParams = this.avatarImageView.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = this.avatarImageView.getLayoutParams();
            int dp = AndroidUtilities.dp(38.0f);
            layoutParams2.height = dp;
            layoutParams.width = dp;
            CheckBox2 checkBox2 = this.checkBox;
            if (checkBox2 != null) {
                ((FrameLayout.LayoutParams) checkBox2.getLayoutParams()).topMargin = AndroidUtilities.dp(25.0f);
                if (LocaleController.isRTL) {
                    ((FrameLayout.LayoutParams) this.checkBox.getLayoutParams()).rightMargin = AndroidUtilities.dp(31.0f);
                } else {
                    ((FrameLayout.LayoutParams) this.checkBox.getLayoutParams()).leftMargin = AndroidUtilities.dp(32.0f);
                }
            }
            String str2 = (String) this.currentObject;
            switch (str2.hashCode()) {
                case -1716307998:
                    if (str2.equals("archived")) {
                        i = 7;
                        break;
                    }
                    i = -1;
                    break;
                case -1237460524:
                    if (str2.equals("groups")) {
                        i = 2;
                        break;
                    }
                    i = -1;
                    break;
                case -1197490811:
                    if (str2.equals("non_contacts")) {
                        i = 1;
                        break;
                    }
                    i = -1;
                    break;
                case -567451565:
                    break;
                case 3029900:
                    if (str2.equals("bots")) {
                        i = 4;
                        break;
                    }
                    i = -1;
                    break;
                case 3496342:
                    if (str2.equals("read")) {
                        i = 6;
                        break;
                    }
                    i = -1;
                    break;
                case 104264043:
                    if (str2.equals("muted")) {
                        i = 5;
                        break;
                    }
                    i = -1;
                    break;
                case 1432626128:
                    if (str2.equals("channels")) {
                        i = 3;
                        break;
                    }
                    i = -1;
                    break;
                default:
                    i = -1;
                    break;
            }
            switch (i) {
                case 0:
                    this.avatarDrawable.setAvatarType(4);
                    break;
                case 1:
                    this.avatarDrawable.setAvatarType(5);
                    break;
                case 2:
                    this.avatarDrawable.setAvatarType(6);
                    break;
                case 3:
                    this.avatarDrawable.setAvatarType(7);
                    break;
                case 4:
                    this.avatarDrawable.setAvatarType(8);
                    break;
                case 5:
                    this.avatarDrawable.setAvatarType(9);
                    break;
                case 6:
                    this.avatarDrawable.setAvatarType(10);
                    break;
                case 7:
                    this.avatarDrawable.setAvatarType(11);
                    break;
            }
            this.lastName = null;
            this.nameTextView.setText(this.currentName, true);
            this.statusTextView.setText(null);
            this.avatarImageView.setImage(null, "50_50", this.avatarDrawable);
        } else {
            CharSequence charSequence = this.currentStatus;
            if (charSequence != null && TextUtils.isEmpty(charSequence)) {
                ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(19.0f);
            } else {
                ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(10.0f);
            }
            ViewGroup.LayoutParams layoutParams3 = this.avatarImageView.getLayoutParams();
            ViewGroup.LayoutParams layoutParams4 = this.avatarImageView.getLayoutParams();
            int dp2 = AndroidUtilities.dp(46.0f);
            layoutParams4.height = dp2;
            layoutParams3.width = dp2;
            CheckBox2 checkBox22 = this.checkBox;
            if (checkBox22 != null) {
                ((FrameLayout.LayoutParams) checkBox22.getLayoutParams()).topMargin = AndroidUtilities.dp(33.0f) + this.padding;
                if (LocaleController.isRTL) {
                    ((FrameLayout.LayoutParams) this.checkBox.getLayoutParams()).rightMargin = AndroidUtilities.dp(39.0f) + this.padding;
                } else {
                    ((FrameLayout.LayoutParams) this.checkBox.getLayoutParams()).leftMargin = AndroidUtilities.dp(40.0f) + this.padding;
                }
            }
            Object obj2 = this.currentObject;
            if (obj2 instanceof TLRPC.User) {
                TLRPC.User currentUser = (TLRPC.User) obj2;
                if (this.showSelfAsSaved && UserObject.isUserSelf(currentUser)) {
                    this.nameTextView.setText(LocaleController.getString("SavedMessages", R.string.SavedMessages), true);
                    this.statusTextView.setText(null);
                    this.avatarDrawable.setAvatarType(1);
                    this.avatarImageView.setImage((ImageLocation) null, "50_50", this.avatarDrawable, currentUser);
                    ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(19.0f);
                    return;
                }
                if (currentUser.photo != null) {
                    photo = currentUser.photo.photo_small;
                }
                if (mask != 0) {
                    boolean continueUpdate = false;
                    if ((mask & MessagesController.UPDATE_MASK_AVATAR) != 0 && (((fileLocation2 = this.lastAvatar) != null && photo == null) || ((fileLocation2 == null && photo != null) || (fileLocation2 != null && photo != null && (fileLocation2.volume_id != photo.volume_id || this.lastAvatar.local_id != photo.local_id))))) {
                        continueUpdate = true;
                    }
                    if (currentUser != null && this.currentStatus == null && !continueUpdate && (mask & MessagesController.UPDATE_MASK_STATUS) != 0) {
                        int newStatus = 0;
                        if (currentUser.status != null) {
                            newStatus = currentUser.status.expires;
                        }
                        if (newStatus != this.lastStatus) {
                            continueUpdate = true;
                        }
                    }
                    if (!continueUpdate && this.currentName == null && this.lastName != null && (mask & MessagesController.UPDATE_MASK_NAME) != 0) {
                        newName = UserObject.getUserName(currentUser);
                        if (!newName.equals(this.lastName)) {
                            continueUpdate = true;
                        }
                    }
                    if (!continueUpdate) {
                        return;
                    }
                }
                this.avatarDrawable.setInfo(currentUser);
                if (currentUser.status != null) {
                    i = currentUser.status.expires;
                }
                this.lastStatus = i;
                CharSequence charSequence2 = this.currentName;
                if (charSequence2 != null) {
                    this.lastName = null;
                    this.nameTextView.setText(charSequence2, true);
                } else {
                    String userName = newName == null ? UserObject.getUserName(currentUser) : newName;
                    this.lastName = userName;
                    this.nameTextView.setText(userName);
                }
                if (this.currentStatus == null) {
                    if (!currentUser.bot) {
                        if (currentUser.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || ((currentUser.status != null && currentUser.status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(currentUser.id)))) {
                            SimpleTextView simpleTextView = this.statusTextView;
                            String str3 = Theme.key_windowBackgroundWhiteBlueText;
                            simpleTextView.setTag(str3);
                            SimpleTextView simpleTextView2 = this.statusTextView;
                            if (this.forceDarkTheme) {
                                str3 = Theme.key_voipgroup_listeningText;
                            }
                            simpleTextView2.setTextColor(Theme.getColor(str3));
                            this.statusTextView.setText(LocaleController.getString("Online", R.string.Online));
                        } else {
                            this.statusTextView.setTag(Theme.key_windowBackgroundWhiteGrayText);
                            this.statusTextView.setTextColor(Theme.getColor(this.forceDarkTheme ? str : Theme.key_windowBackgroundWhiteGrayText));
                            this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, currentUser));
                        }
                    } else {
                        this.statusTextView.setTag(Theme.key_windowBackgroundWhiteGrayText);
                        this.statusTextView.setTextColor(Theme.getColor(this.forceDarkTheme ? str : Theme.key_windowBackgroundWhiteGrayText));
                        this.statusTextView.setText(LocaleController.getString("Bot", R.string.Bot));
                    }
                }
                this.avatarImageView.setForUserOrChat(currentUser, this.avatarDrawable);
            } else {
                TLRPC.Chat currentChat = (TLRPC.Chat) obj2;
                if (currentChat.photo != null) {
                    photo = currentChat.photo.photo_small;
                }
                if (mask != 0) {
                    boolean continueUpdate2 = false;
                    if ((mask & MessagesController.UPDATE_MASK_AVATAR) != 0 && (((fileLocation = this.lastAvatar) != null && photo == null) || ((fileLocation == null && photo != null) || (fileLocation != null && photo != null && (fileLocation.volume_id != photo.volume_id || this.lastAvatar.local_id != photo.local_id))))) {
                        continueUpdate2 = true;
                    }
                    if (!continueUpdate2 && this.currentName == null && this.lastName != null && (mask & MessagesController.UPDATE_MASK_NAME) != 0) {
                        newName = currentChat.title;
                        if (!newName.equals(this.lastName)) {
                            continueUpdate2 = true;
                        }
                    }
                    if (!continueUpdate2) {
                        return;
                    }
                }
                this.avatarDrawable.setInfo(currentChat);
                CharSequence charSequence3 = this.currentName;
                if (charSequence3 != null) {
                    this.lastName = null;
                    this.nameTextView.setText(charSequence3, true);
                } else {
                    String str4 = newName == null ? currentChat.title : newName;
                    this.lastName = str4;
                    this.nameTextView.setText(str4);
                }
                if (this.currentStatus == null) {
                    this.statusTextView.setTag(Theme.key_windowBackgroundWhiteGrayText);
                    this.statusTextView.setTextColor(Theme.getColor(this.forceDarkTheme ? str : Theme.key_windowBackgroundWhiteGrayText));
                    if (currentChat.participants_count != 0) {
                        if (ChatObject.isChannel(currentChat) && !currentChat.megagroup) {
                            this.statusTextView.setText(LocaleController.formatPluralString("Subscribers", currentChat.participants_count, new Object[0]));
                        } else {
                            this.statusTextView.setText(LocaleController.formatPluralString("Members", currentChat.participants_count, new Object[0]));
                        }
                    } else if (currentChat.has_geo) {
                        this.statusTextView.setText(LocaleController.getString("MegaLocation", R.string.MegaLocation));
                    } else if (TextUtils.isEmpty(currentChat.username)) {
                        if (ChatObject.isChannel(currentChat) && !currentChat.megagroup) {
                            this.statusTextView.setText(LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate));
                        } else {
                            this.statusTextView.setText(LocaleController.getString("MegaPrivate", R.string.MegaPrivate));
                        }
                    } else if (ChatObject.isChannel(currentChat) && !currentChat.megagroup) {
                        this.statusTextView.setText(LocaleController.getString("ChannelPublic", R.string.ChannelPublic));
                    } else {
                        this.statusTextView.setText(LocaleController.getString("MegaPublic", R.string.MegaPublic));
                    }
                }
                this.avatarImageView.setForUserOrChat(currentChat, this.avatarDrawable);
            }
        }
        CharSequence charSequence4 = this.currentStatus;
        if (charSequence4 != null) {
            this.statusTextView.setText(charSequence4, true);
            this.statusTextView.setTag(Theme.key_windowBackgroundWhiteGrayText);
            SimpleTextView simpleTextView3 = this.statusTextView;
            if (!this.forceDarkTheme) {
                str = Theme.key_windowBackgroundWhiteGrayText;
            }
            simpleTextView3.setTextColor(Theme.getColor(str));
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = 0.0f;
        if (this.checkBoxType == 2 && (this.isChecked || this.checkProgress > 0.0f)) {
            this.paint.setColor(Theme.getColor(Theme.key_checkboxSquareBackground));
            float cx = this.avatarImageView.getLeft() + (this.avatarImageView.getMeasuredWidth() / 2);
            float cy = this.avatarImageView.getTop() + (this.avatarImageView.getMeasuredHeight() / 2);
            canvas.drawCircle(cx, cy, AndroidUtilities.dp(18.0f) + (AndroidUtilities.dp(4.0f) * this.checkProgress), this.paint);
        }
        if (this.drawDivider) {
            int start = AndroidUtilities.dp(LocaleController.isRTL ? 0.0f : this.padding + 72);
            int measuredWidth = getMeasuredWidth();
            if (LocaleController.isRTL) {
                f = this.padding + 72;
            }
            int end = measuredWidth - AndroidUtilities.dp(f);
            if (this.forceDarkTheme) {
                Theme.dividerExtraPaint.setColor(Theme.getColor(Theme.key_voipgroup_actionBar));
                canvas.drawRect(start, getMeasuredHeight() - 1, end, getMeasuredHeight(), Theme.dividerExtraPaint);
                return;
            }
            canvas.drawRect(start, getMeasuredHeight() - 1, end, getMeasuredHeight(), Theme.dividerPaint);
        }
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (isChecked()) {
            info.setCheckable(true);
            info.setChecked(true);
        }
    }
}
