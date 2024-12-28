package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoriesUtilities;

/* loaded from: classes4.dex */
public class UserCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private TextView addButton;
    private TextView adminTextView;
    protected AvatarDrawable avatarDrawable;
    public BackupImageView avatarImageView;
    private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable botVerification;
    private CheckBox2 checkBox;
    private ImageView checkBox3;
    private CheckBoxSquare checkBoxBig;
    private ImageView closeView;
    private int currentAccount;
    private int currentDrawable;
    private int currentId;
    private CharSequence currentName;
    private Object currentObject;
    private CharSequence currentStatus;
    protected long dialogId;
    private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emojiStatus;
    private TLRPC.EncryptedChat encryptedChat;
    private ImageView imageView;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    protected SimpleTextView nameTextView;
    public boolean needDivider;
    private Drawable premiumDrawable;
    protected Theme.ResourcesProvider resourcesProvider;
    private boolean selfAsSavedMessages;
    private int statusColor;
    private int statusOnlineColor;
    protected SimpleTextView statusTextView;
    private boolean storiable;
    public StoriesUtilities.AvatarStoryParams storyParams;

    public UserCell(Context context, int i, int i2, boolean z) {
        this(context, i, i2, z, false, null);
    }

    public UserCell(Context context, int i, int i2, boolean z, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, z, false, resourcesProvider);
    }

    public UserCell(Context context, int i, int i2, boolean z, boolean z2) {
        this(context, i, i2, z, z2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x0302  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public UserCell(Context context, int i, int i2, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i3;
        int i4;
        float f;
        int i5;
        float f2;
        float f3;
        int i6;
        float f4;
        float f5;
        float f6;
        View view;
        View view2;
        FrameLayout.LayoutParams createFrame;
        View view3;
        this.currentAccount = UserConfig.selectedAccount;
        this.storyParams = new StoriesUtilities.AvatarStoryParams(false) { // from class: org.telegram.ui.Cells.UserCell.1
            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void openStory(long j, Runnable runnable) {
                UserCell.this.openStory(j, runnable);
            }
        };
        this.resourcesProvider = resourcesProvider;
        if (z2) {
            TextView textView = new TextView(context);
            this.addButton = textView;
            textView.setGravity(17);
            this.addButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            this.addButton.setTextSize(1, 14.0f);
            this.addButton.setTypeface(AndroidUtilities.bold());
            this.addButton.setBackgroundDrawable(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 4.0f));
            this.addButton.setText(LocaleController.getString(R.string.Add));
            this.addButton.setPadding(AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(17.0f), 0);
            TextView textView2 = this.addButton;
            boolean z3 = LocaleController.isRTL;
            addView(textView2, LayoutHelper.createFrame(-2, 28.0f, (z3 ? 3 : 5) | 48, z3 ? 14.0f : 0.0f, 15.0f, z3 ? 0.0f : 14.0f, 0.0f));
            i3 = (int) Math.ceil((this.addButton.getPaint().measureText(this.addButton.getText().toString()) + AndroidUtilities.dp(48.0f)) / AndroidUtilities.density);
        } else {
            i3 = 0;
        }
        this.statusColor = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, resourcesProvider);
        this.statusOnlineColor = Theme.getColor(Theme.key_windowBackgroundWhiteBlueText, resourcesProvider);
        this.avatarDrawable = new AvatarDrawable();
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Cells.UserCell.2
            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            protected void onDraw(Canvas canvas) {
                if (!UserCell.this.storiable) {
                    super.onDraw(canvas);
                    return;
                }
                UserCell.this.storyParams.originalAvatarRect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                UserCell userCell = UserCell.this;
                StoriesUtilities.drawAvatarWithStory(userCell.dialogId, canvas, this.imageReceiver, userCell.storyParams);
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (UserCell.this.storyParams.checkOnTouchEvent(motionEvent, this)) {
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(24.0f));
        BackupImageView backupImageView2 = this.avatarImageView;
        boolean z4 = LocaleController.isRTL;
        addView(backupImageView2, LayoutHelper.createFrame(46, 46.0f, (z4 ? 5 : 3) | 48, z4 ? 0.0f : i + 7, 6.0f, z4 ? i + 7 : 0.0f, 0.0f));
        setClipChildren(false);
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.nameTextView = simpleTextView;
        simpleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        this.nameTextView.setTypeface(AndroidUtilities.bold());
        this.nameTextView.setTextSize(16);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        SimpleTextView simpleTextView2 = this.nameTextView;
        boolean z5 = LocaleController.isRTL;
        int i7 = (z5 ? 5 : 3) | 48;
        if (z5) {
            i4 = (i2 == 2 ? 18 : 0) + 28 + i3;
        } else {
            i4 = i + 64;
        }
        float f7 = i4;
        if (z5) {
            f = i + 64;
        } else {
            f = (i2 != 2 ? 0 : 18) + 28 + i3;
        }
        addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, i7, f7, 10.0f, f, 0.0f));
        this.botVerification = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.nameTextView, AndroidUtilities.dp(20.0f));
        this.emojiStatus = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.nameTextView, AndroidUtilities.dp(20.0f));
        SimpleTextView simpleTextView3 = new SimpleTextView(context);
        this.statusTextView = simpleTextView3;
        simpleTextView3.setTextSize(15);
        this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        SimpleTextView simpleTextView4 = this.statusTextView;
        boolean z6 = LocaleController.isRTL;
        addView(simpleTextView4, LayoutHelper.createFrame(-1, 20.0f, (z6 ? 5 : 3) | 48, z6 ? i3 + 28 : i + 64, 32.0f, z6 ? i + 64 : i3 + 28, 0.0f));
        ImageView imageView = new ImageView(context);
        this.imageView = imageView;
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        imageView.setScaleType(scaleType);
        ImageView imageView2 = this.imageView;
        int color = Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon, resourcesProvider);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        imageView2.setColorFilter(new PorterDuffColorFilter(color, mode));
        this.imageView.setVisibility(8);
        ImageView imageView3 = this.imageView;
        boolean z7 = LocaleController.isRTL;
        addView(imageView3, LayoutHelper.createFrame(-2, -2.0f, (z7 ? 5 : 3) | 16, z7 ? 0.0f : 16.0f, 0.0f, z7 ? 16.0f : 0.0f, 0.0f));
        if (i2 != 2) {
            if (i2 == 1) {
                CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
                this.checkBox = checkBox2;
                checkBox2.setDrawUnchecked(false);
                this.checkBox.setDrawBackgroundAsArc(3);
                this.checkBox.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
                CheckBox2 checkBox22 = this.checkBox;
                boolean z8 = LocaleController.isRTL;
                i5 = (z8 ? 5 : 3) | 48;
                f2 = z8 ? 0.0f : i + 37;
                f6 = 36.0f;
                view = checkBox22;
                if (z8) {
                    f3 = i + 37;
                    i6 = i5;
                    f4 = f2;
                    f5 = 36.0f;
                    view2 = checkBox22;
                    createFrame = LayoutHelper.createFrame(24, 24.0f, i6, f4, f5, f3, 0.0f);
                    view3 = view2;
                }
                i6 = i5;
                f4 = f2;
                f5 = f6;
                f3 = 0.0f;
                view2 = view;
                createFrame = LayoutHelper.createFrame(24, 24.0f, i6, f4, f5, f3, 0.0f);
                view3 = view2;
            } else if (i2 == 3) {
                ImageView imageView4 = new ImageView(context);
                this.checkBox3 = imageView4;
                imageView4.setScaleType(scaleType);
                this.checkBox3.setImageResource(R.drawable.account_check);
                this.checkBox3.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), mode));
                this.checkBox3.setVisibility(8);
                ImageView imageView5 = this.checkBox3;
                boolean z9 = LocaleController.isRTL;
                i5 = (z9 ? 3 : 5) | 16;
                f2 = z9 ? i + 10 : 0.0f;
                if (z9) {
                    f6 = 0.0f;
                    view = imageView5;
                    i6 = i5;
                    f4 = f2;
                    f5 = f6;
                    f3 = 0.0f;
                    view2 = view;
                    createFrame = LayoutHelper.createFrame(24, 24.0f, i6, f4, f5, f3, 0.0f);
                    view3 = view2;
                } else {
                    f3 = i + 10;
                    i6 = i5;
                    f4 = f2;
                    f5 = 0.0f;
                    view2 = imageView5;
                    createFrame = LayoutHelper.createFrame(24, 24.0f, i6, f4, f5, f3, 0.0f);
                    view3 = view2;
                }
            }
            if (z) {
                TextView textView3 = new TextView(context);
                this.adminTextView = textView3;
                textView3.setTextSize(1, 14.0f);
                this.adminTextView.setTextColor(Theme.getColor(Theme.key_profile_creatorIcon, resourcesProvider));
                TextView textView4 = this.adminTextView;
                boolean z10 = LocaleController.isRTL;
                addView(textView4, LayoutHelper.createFrame(-2, -2.0f, (z10 ? 3 : 5) | 48, z10 ? 23.0f : 0.0f, 10.0f, z10 ? 0.0f : 23.0f, 0.0f));
            }
            setFocusable(true);
        }
        CheckBoxSquare checkBoxSquare = new CheckBoxSquare(context, false);
        this.checkBoxBig = checkBoxSquare;
        boolean z11 = LocaleController.isRTL;
        createFrame = LayoutHelper.createFrame(18, 18.0f, (z11 ? 3 : 5) | 16, z11 ? 19.0f : 0.0f, 0.0f, z11 ? 0.0f : 19.0f, 0.0f);
        view3 = checkBoxSquare;
        addView(view3, createFrame);
        if (z) {
        }
        setFocusable(true);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            this.nameTextView.invalidate();
        }
    }

    public Object getCurrentObject() {
        return this.currentObject;
    }

    public long getDialogId() {
        return this.dialogId;
    }

    public CharSequence getName() {
        return this.nameTextView.getText();
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        this.emojiStatus.attach();
        this.botVerification.attach();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        this.emojiStatus.detach();
        this.botVerification.detach();
        this.storyParams.onDetachFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(68.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(68.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        boolean isChecked;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare == null || checkBoxSquare.getVisibility() != 0) {
            CheckBox2 checkBox2 = this.checkBox;
            if (checkBox2 == null || checkBox2.getVisibility() != 0) {
                return;
            }
            accessibilityNodeInfo.setCheckable(true);
            isChecked = this.checkBox.isChecked();
        } else {
            accessibilityNodeInfo.setCheckable(true);
            isChecked = this.checkBoxBig.isChecked();
        }
        accessibilityNodeInfo.setChecked(isChecked);
        accessibilityNodeInfo.setClassName("android.widget.CheckBox");
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    public void openStory(long j, Runnable runnable) {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment != null) {
            lastFragment.getOrCreateStoryViewer().doOnAnimationReady(runnable);
            lastFragment.getOrCreateStoryViewer().open(getContext(), j, StoriesListPlaceProvider.of((RecyclerListView) getParent()));
        }
    }

    public void setAddButtonVisible(boolean z) {
        TextView textView = this.addButton;
        if (textView == null) {
            return;
        }
        textView.setVisibility(z ? 0 : 8);
    }

    public void setAdminRole(String str) {
        TextView textView = this.adminTextView;
        if (textView == null) {
            return;
        }
        textView.setVisibility(str != null ? 0 : 8);
        this.adminTextView.setText(str);
        if (str == null) {
            setRightPadding(0, true, false);
        } else {
            CharSequence text = this.adminTextView.getText();
            setRightPadding((int) Math.ceil(this.adminTextView.getPaint().measureText(text, 0, text.length())), true, false);
        }
    }

    public void setAvatarPadding(int i) {
        int i2;
        float f;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.avatarImageView.getLayoutParams();
        layoutParams.leftMargin = AndroidUtilities.dp(LocaleController.isRTL ? 0.0f : i + 7);
        layoutParams.rightMargin = AndroidUtilities.dp(LocaleController.isRTL ? i + 7 : 0.0f);
        this.avatarImageView.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.nameTextView.getLayoutParams();
        if (LocaleController.isRTL) {
            i2 = (this.checkBoxBig != null ? 18 : 0) + 28;
        } else {
            i2 = i + 64;
        }
        layoutParams2.leftMargin = AndroidUtilities.dp(i2);
        if (LocaleController.isRTL) {
            f = i + 64;
        } else {
            f = (this.checkBoxBig != null ? 18 : 0) + 28;
        }
        layoutParams2.rightMargin = AndroidUtilities.dp(f);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.statusTextView.getLayoutParams();
        layoutParams3.leftMargin = AndroidUtilities.dp(LocaleController.isRTL ? 28.0f : i + 64);
        layoutParams3.rightMargin = AndroidUtilities.dp(LocaleController.isRTL ? i + 64 : 28.0f);
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) checkBox2.getLayoutParams();
            layoutParams4.leftMargin = AndroidUtilities.dp(LocaleController.isRTL ? 0.0f : i + 37);
            layoutParams4.rightMargin = AndroidUtilities.dp(LocaleController.isRTL ? i + 37 : 0.0f);
        }
    }

    public void setCheckDisabled(boolean z) {
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.setDisabled(z);
        }
    }

    public void setChecked(boolean z, boolean z2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            if (checkBox2.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(z, z2);
            return;
        }
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            if (checkBoxSquare.getVisibility() != 0) {
                this.checkBoxBig.setVisibility(0);
            }
            this.checkBoxBig.setChecked(z, z2);
        } else {
            ImageView imageView = this.checkBox3;
            if (imageView != null) {
                imageView.setVisibility(z ? 0 : 8);
            }
        }
    }

    public void setCloseIcon(View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            ImageView imageView = this.closeView;
            if (imageView != null) {
                removeView(imageView);
                this.closeView = null;
                return;
            }
            return;
        }
        if (this.closeView == null) {
            ImageView imageView2 = new ImageView(getContext());
            this.closeView = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            ScaleStateListAnimator.apply(this.closeView);
            this.closeView.setImageResource(R.drawable.ic_close_white);
            this.closeView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3, this.resourcesProvider), PorterDuff.Mode.SRC_IN));
            this.closeView.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector, this.resourcesProvider), 5));
            ImageView imageView3 = this.closeView;
            boolean z = LocaleController.isRTL;
            addView(imageView3, LayoutHelper.createFrame(30, 30.0f, (z ? 3 : 5) | 16, z ? 14.0f : 0.0f, 0.0f, z ? 0.0f : 14.0f, 0.0f));
        }
        this.closeView.setOnClickListener(onClickListener);
    }

    public void setCurrentId(int i) {
        this.currentId = i;
    }

    public void setData(Object obj, CharSequence charSequence, CharSequence charSequence2, int i) {
        setData(obj, null, charSequence, charSequence2, i, false);
    }

    public void setData(Object obj, CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        setData(obj, null, charSequence, charSequence2, i, z);
    }

    public void setData(Object obj, TLRPC.EncryptedChat encryptedChat, CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        if (obj == null && charSequence == null && charSequence2 == null) {
            this.currentStatus = null;
            this.currentName = null;
            this.storiable = false;
            this.currentObject = null;
            this.nameTextView.setText("");
            this.statusTextView.setText("");
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.encryptedChat = encryptedChat;
        this.currentStatus = charSequence2;
        if (charSequence != null) {
            try {
                SimpleTextView simpleTextView = this.nameTextView;
                if (simpleTextView != null) {
                    charSequence = Emoji.replaceEmoji(charSequence, simpleTextView.getPaint().getFontMetricsInt(), false);
                }
            } catch (Exception unused) {
            }
        }
        this.currentName = charSequence;
        this.storiable = !(obj instanceof String);
        this.currentObject = obj;
        this.currentDrawable = i;
        this.needDivider = z;
        setWillNotDraw(!z);
        update(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x003a, code lost:
    
        if (r2 != false) goto L41;
     */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setException(NotificationsSettingsActivity.NotificationException notificationException, CharSequence charSequence, boolean z) {
        int i;
        String str;
        Object chat;
        Object obj;
        TLRPC.EncryptedChat encryptedChat;
        int i2;
        UserCell userCell;
        CharSequence charSequence2;
        boolean z2;
        boolean z3 = true;
        if (notificationException.story) {
            int i3 = notificationException.notify;
            str = LocaleController.getString((i3 > 0 || !notificationException.auto) ? i3 <= 0 ? R.string.NotificationEnabled : R.string.NotificationDisabled : R.string.NotificationEnabledAutomatically);
        } else {
            boolean z4 = notificationException.hasCustom;
            int i4 = notificationException.notify;
            int i5 = notificationException.muteUntil;
            if (i4 != 3 || i5 == Integer.MAX_VALUE) {
                if (i4 != 0 && i4 != 1) {
                    z3 = false;
                }
                if (!z3 || !z4) {
                    if (!z3) {
                        i = R.string.NotificationsMuted;
                    }
                    i = R.string.NotificationsUnmuted;
                }
                i = R.string.NotificationsCustom;
            } else {
                int currentTime = i5 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                if (currentTime > 0) {
                    str = currentTime < 3600 ? LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0])) : currentTime < 86400 ? LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Hours", (int) Math.ceil((currentTime / 60.0f) / 60.0f), new Object[0])) : currentTime < 31536000 ? LocaleController.formatString("WillUnmuteIn", R.string.WillUnmuteIn, LocaleController.formatPluralString("Days", (int) Math.ceil(((currentTime / 60.0f) / 60.0f) / 24.0f), new Object[0])) : null;
                    if (str == null) {
                        str = LocaleController.getString(R.string.NotificationsOff);
                    }
                    if (notificationException.auto) {
                        str = str + ", Auto";
                    }
                }
            }
            str = LocaleController.getString(i);
            if (str == null) {
            }
            if (notificationException.auto) {
            }
        }
        String str2 = str;
        if (DialogObject.isEncryptedDialog(notificationException.did)) {
            encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(notificationException.did)));
            if (encryptedChat == null || (obj = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id))) == null) {
                return;
            }
            i2 = 0;
            z2 = false;
            userCell = this;
            charSequence2 = charSequence;
        } else {
            if (DialogObject.isUserDialog(notificationException.did)) {
                chat = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(notificationException.did));
                if (chat == null) {
                    return;
                }
            } else {
                chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-notificationException.did));
                if (chat == null) {
                    return;
                }
            }
            obj = chat;
            encryptedChat = null;
            i2 = 0;
            userCell = this;
            charSequence2 = charSequence;
            z2 = z;
        }
        userCell.setData(obj, encryptedChat, charSequence2, str2, i2, z2);
    }

    public void setFromUItem(int i, UItem uItem, boolean z) {
        int i2;
        int i3;
        String str;
        String formatPluralStringComma;
        TLRPC.Chat chat;
        TLRPC.Chat chat2;
        String str2 = uItem.chatType;
        if (str2 != null) {
            setData(str2, uItem.text, null, 0, z);
            return;
        }
        long j = uItem.dialogId;
        MessagesController messagesController = MessagesController.getInstance(i);
        if (j > 0) {
            TLRPC.User user = messagesController.getUser(Long.valueOf(j));
            if (user == null) {
                return;
            }
            if (user.bot) {
                i2 = R.string.Bot;
                chat2 = user;
            } else if (user.contact) {
                i2 = R.string.FilterContact;
                chat2 = user;
            } else {
                i2 = R.string.FilterNonContact;
                chat2 = user;
            }
        } else {
            TLRPC.Chat chat3 = messagesController.getChat(Long.valueOf(-j));
            if (chat3 == null) {
                return;
            }
            if (chat3.participants_count != 0) {
                if (ChatObject.isChannelAndNotMegaGroup(chat3)) {
                    i3 = chat3.participants_count;
                    str = "Subscribers";
                } else {
                    i3 = chat3.participants_count;
                    str = "Members";
                }
                formatPluralStringComma = LocaleController.formatPluralStringComma(str, i3);
                chat = chat3;
                setData(chat, null, formatPluralStringComma, 0, z);
            }
            if (ChatObject.isPublic(chat3)) {
                if (!ChatObject.isChannel(chat3) || chat3.megagroup) {
                    i2 = R.string.MegaPublic;
                    chat2 = chat3;
                } else {
                    i2 = R.string.ChannelPublic;
                    chat2 = chat3;
                }
            } else if (!ChatObject.isChannel(chat3) || chat3.megagroup) {
                i2 = R.string.MegaPrivate;
                chat2 = chat3;
            } else {
                i2 = R.string.ChannelPrivate;
                chat2 = chat3;
            }
        }
        formatPluralStringComma = LocaleController.getString(i2);
        chat = chat2;
        setData(chat, null, formatPluralStringComma, 0, z);
    }

    public void setNameTypeface(Typeface typeface) {
        this.nameTextView.setTypeface(typeface);
    }

    public void setRightPadding(int i, boolean z, boolean z2) {
        if (i > 0) {
            i += AndroidUtilities.dp(6.0f);
        }
        if (z) {
            SimpleTextView simpleTextView = this.nameTextView;
            boolean z3 = LocaleController.isRTL;
            simpleTextView.setPadding(z3 ? i : 0, 0, !z3 ? i : 0, 0);
        }
        if (z2) {
            SimpleTextView simpleTextView2 = this.statusTextView;
            boolean z4 = LocaleController.isRTL;
            int i2 = z4 ? i : 0;
            if (z4) {
                i = 0;
            }
            simpleTextView2.setPadding(i2, 0, i, 0);
        }
    }

    public void setSelfAsSavedMessages(boolean z) {
        this.selfAsSavedMessages = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x0402  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0416  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:137:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x041c  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0243 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i) {
        TLRPC.User user;
        TLRPC.Chat chat;
        TLRPC.FileLocation fileLocation;
        String str;
        SimpleTextView simpleTextView;
        int i2;
        TLRPC.UserStatus userStatus;
        CharSequence formatUserStatus;
        TextView textView;
        TextView textView2;
        SimpleTextView simpleTextView2;
        Drawable drawable;
        int i3;
        AvatarDrawable avatarDrawable;
        TLRPC.FileLocation fileLocation2;
        this.dialogId = 0L;
        Object obj = this.currentObject;
        if (obj instanceof TLRPC.User) {
            user = (TLRPC.User) obj;
            TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
            TLRPC.FileLocation fileLocation3 = userProfilePhoto != null ? userProfilePhoto.photo_small : null;
            this.dialogId = user.id;
            fileLocation = fileLocation3;
            chat = null;
        } else if (obj instanceof TLRPC.Chat) {
            TLRPC.Chat chat2 = (TLRPC.Chat) obj;
            TLRPC.ChatPhoto chatPhoto = chat2.photo;
            TLRPC.FileLocation fileLocation4 = chatPhoto != null ? chatPhoto.photo_small : null;
            this.dialogId = chat2.id;
            fileLocation = fileLocation4;
            chat = chat2;
            user = null;
        } else {
            user = null;
            chat = null;
            fileLocation = null;
        }
        if (i != 0) {
            boolean z = (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && (((fileLocation2 = this.lastAvatar) != null && fileLocation == null) || ((fileLocation2 == null && fileLocation != null) || !(fileLocation2 == null || (fileLocation2.volume_id == fileLocation.volume_id && fileLocation2.local_id == fileLocation.local_id))));
            if (user != null && !z && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                TLRPC.UserStatus userStatus2 = user.status;
                if ((userStatus2 != null ? userStatus2.expires : 0) != this.lastStatus) {
                    z = true;
                }
            }
            if (z || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                str = null;
            } else {
                str = AndroidUtilities.removeDiacritics(user != null ? UserObject.getUserName(user) : chat == null ? "" : chat.title);
                if (!str.equals(this.lastName)) {
                    z = true;
                }
            }
            if (!z) {
                return;
            }
        } else {
            str = null;
        }
        if (this.currentObject instanceof String) {
            ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(19.0f);
            String str2 = (String) this.currentObject;
            str2.hashCode();
            i3 = 6;
            switch (str2) {
                case "archived":
                    avatarDrawable = this.avatarDrawable;
                    i3 = 11;
                    avatarDrawable.setAvatarType(i3);
                    break;
                case "groups":
                    avatarDrawable = this.avatarDrawable;
                    avatarDrawable.setAvatarType(i3);
                    break;
                case "non_contacts":
                    this.avatarDrawable.setAvatarType(5);
                    break;
                case "contacts":
                    this.avatarDrawable.setAvatarType(4);
                    break;
                case "new_chats":
                    avatarDrawable = this.avatarDrawable;
                    i3 = 24;
                    avatarDrawable.setAvatarType(i3);
                    break;
                case "bots":
                    this.avatarDrawable.setAvatarType(8);
                    break;
                case "read":
                    avatarDrawable = this.avatarDrawable;
                    i3 = 10;
                    avatarDrawable.setAvatarType(i3);
                    break;
                case "muted":
                    this.avatarDrawable.setAvatarType(9);
                    break;
                case "existing_chats":
                    avatarDrawable = this.avatarDrawable;
                    i3 = 23;
                    avatarDrawable.setAvatarType(i3);
                    break;
                case "channels":
                    this.avatarDrawable.setAvatarType(7);
                    break;
            }
            this.avatarImageView.setImage(null, "50_50", this.avatarDrawable);
            this.currentStatus = "";
        } else {
            ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(10.0f);
            if (user != null) {
                if (this.selfAsSavedMessages && UserObject.isUserSelf(user)) {
                    this.nameTextView.setText(LocaleController.getString(R.string.SavedMessages), true);
                    this.statusTextView.setText(null);
                    this.avatarDrawable.setAvatarType(1);
                    this.avatarImageView.setImage((ImageLocation) null, "50_50", this.avatarDrawable, user);
                    ((FrameLayout.LayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(19.0f);
                    return;
                }
                this.avatarDrawable.setInfo(this.currentAccount, user);
                TLRPC.UserStatus userStatus3 = user.status;
                this.lastStatus = userStatus3 != null ? userStatus3.expires : 0;
            } else if (chat != null) {
                this.avatarDrawable.setInfo(this.currentAccount, chat);
            } else {
                CharSequence charSequence = this.currentName;
                if (charSequence != null) {
                    this.avatarDrawable.setInfo(this.currentId, charSequence.toString(), null);
                } else {
                    this.avatarDrawable.setInfo(this.currentId, "#", null);
                }
            }
        }
        CharSequence charSequence2 = this.currentName;
        if (charSequence2 != null) {
            this.lastName = null;
        } else {
            if (user != null) {
                if (str == null) {
                    str = UserObject.getUserName(user);
                }
            } else if (chat == null) {
                this.lastName = "";
                charSequence2 = this.lastName;
                if (charSequence2 != null) {
                    try {
                        charSequence2 = Emoji.replaceEmoji(charSequence2, this.nameTextView.getPaint().getFontMetricsInt(), false);
                    } catch (Exception unused) {
                    }
                }
            } else if (str == null) {
                str = chat.title;
            }
            this.lastName = AndroidUtilities.removeDiacritics(str);
            charSequence2 = this.lastName;
            if (charSequence2 != null) {
            }
        }
        this.nameTextView.setText(charSequence2);
        long botVerificationIcon = user != null ? DialogObject.getBotVerificationIcon(user) : chat != null ? DialogObject.getBotVerificationIcon(chat) : 0L;
        if (botVerificationIcon == 0) {
            this.botVerification.set((Drawable) null, false);
            this.nameTextView.setLeftDrawable((Drawable) null);
        } else {
            this.botVerification.set(botVerificationIcon, false);
            this.botVerification.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
            this.nameTextView.setLeftDrawable(this.botVerification);
        }
        if (user == null || !MessagesController.getInstance(this.currentAccount).isPremiumUser(user) || MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked()) {
            this.nameTextView.setRightDrawable((Drawable) null);
            this.nameTextView.setRightDrawableTopPadding(0);
        } else {
            TLRPC.EmojiStatus emojiStatus = user.emoji_status;
            if (!(emojiStatus instanceof TLRPC.TL_emojiStatusUntil) || ((TLRPC.TL_emojiStatusUntil) emojiStatus).until <= ((int) (System.currentTimeMillis() / 1000))) {
                TLRPC.EmojiStatus emojiStatus2 = user.emoji_status;
                if (emojiStatus2 instanceof TLRPC.TL_emojiStatus) {
                    this.emojiStatus.set(((TLRPC.TL_emojiStatus) emojiStatus2).document_id, false);
                } else {
                    if (this.premiumDrawable == null) {
                        this.premiumDrawable = getContext().getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                        AnimatedEmojiDrawable.WrapSizeDrawable wrapSizeDrawable = new AnimatedEmojiDrawable.WrapSizeDrawable(this.premiumDrawable, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f)) { // from class: org.telegram.ui.Cells.UserCell.3
                            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.WrapSizeDrawable, android.graphics.drawable.Drawable
                            public void draw(Canvas canvas) {
                                canvas.save();
                                canvas.translate(0.0f, AndroidUtilities.dp(1.0f));
                                super.draw(canvas);
                                canvas.restore();
                            }
                        };
                        this.premiumDrawable = wrapSizeDrawable;
                        wrapSizeDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
                    }
                    simpleTextView2 = this.nameTextView;
                    drawable = this.premiumDrawable;
                    simpleTextView2.setRightDrawable(drawable);
                    this.nameTextView.setRightDrawableTopPadding(-AndroidUtilities.dp(0.5f));
                }
            } else {
                this.emojiStatus.set(((TLRPC.TL_emojiStatusUntil) user.emoji_status).document_id, false);
            }
            this.emojiStatus.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
            simpleTextView2 = this.nameTextView;
            drawable = this.emojiStatus;
            simpleTextView2.setRightDrawable(drawable);
            this.nameTextView.setRightDrawableTopPadding(-AndroidUtilities.dp(0.5f));
        }
        if (this.currentStatus == null) {
            if (user != null) {
                if (user.bot) {
                    this.statusTextView.setTextColor(this.statusColor);
                    if (user.bot_chat_history || ((textView = this.adminTextView) != null && textView.getVisibility() == 0)) {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.BotStatusRead;
                    } else {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.BotStatusCantRead;
                    }
                } else if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || (((userStatus = user.status) != null && userStatus.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(user.id)))) {
                    this.statusTextView.setTextColor(this.statusOnlineColor);
                    simpleTextView = this.statusTextView;
                    i2 = R.string.Online;
                } else {
                    this.statusTextView.setTextColor(this.statusColor);
                    simpleTextView = this.statusTextView;
                    formatUserStatus = LocaleController.formatUserStatus(this.currentAccount, user);
                }
                formatUserStatus = LocaleController.getString(i2);
            }
            if ((this.imageView.getVisibility() == 0 && this.currentDrawable == 0) || (this.imageView.getVisibility() == 8 && this.currentDrawable != 0)) {
                this.imageView.setVisibility(this.currentDrawable == 0 ? 8 : 0);
                this.imageView.setImageResource(this.currentDrawable);
            }
            this.lastAvatar = fileLocation;
            BackupImageView backupImageView = this.avatarImageView;
            if (user != null) {
                backupImageView.setForUserOrChat(user, this.avatarDrawable);
            } else {
                AvatarDrawable avatarDrawable2 = this.avatarDrawable;
                if (chat != null) {
                    backupImageView.setForUserOrChat(chat, avatarDrawable2);
                } else {
                    backupImageView.setImageDrawable(avatarDrawable2);
                }
            }
            this.avatarImageView.setRoundRadius(AndroidUtilities.dp((chat == null && chat.forum) ? 14.0f : 24.0f));
            this.nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            textView2 = this.adminTextView;
            if (textView2 != null) {
                textView2.setTextColor(Theme.getColor(Theme.key_profile_creatorIcon, this.resourcesProvider));
                return;
            }
            return;
        }
        this.statusTextView.setTextColor(this.statusColor);
        simpleTextView = this.statusTextView;
        formatUserStatus = this.currentStatus;
        simpleTextView.setText(formatUserStatus);
        if (this.imageView.getVisibility() == 0) {
            this.imageView.setVisibility(this.currentDrawable == 0 ? 8 : 0);
            this.imageView.setImageResource(this.currentDrawable);
            this.lastAvatar = fileLocation;
            BackupImageView backupImageView2 = this.avatarImageView;
            if (user != null) {
            }
            this.avatarImageView.setRoundRadius(AndroidUtilities.dp((chat == null && chat.forum) ? 14.0f : 24.0f));
            this.nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            textView2 = this.adminTextView;
            if (textView2 != null) {
            }
        }
        this.imageView.setVisibility(this.currentDrawable == 0 ? 8 : 0);
        this.imageView.setImageResource(this.currentDrawable);
        this.lastAvatar = fileLocation;
        BackupImageView backupImageView22 = this.avatarImageView;
        if (user != null) {
        }
        this.avatarImageView.setRoundRadius(AndroidUtilities.dp((chat == null && chat.forum) ? 14.0f : 24.0f));
        this.nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        textView2 = this.adminTextView;
        if (textView2 != null) {
        }
    }
}
