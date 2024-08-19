package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Stories.StoriesUtilities;
/* loaded from: classes4.dex */
public class ManageChatUserCell extends FrameLayout {
    private final AvatarDrawable avatarDrawable;
    private final BackupImageView avatarImageView;
    private final int currentAccount;
    private CharSequence currentName;
    private Object currentObject;
    private CharSequence currentStatus;
    private ImageView customImageView;
    private ManageChatUserCellDelegate delegate;
    private int dividerColor;
    private boolean isAdmin;
    private TLRPC$FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private final int namePadding;
    private final SimpleTextView nameTextView;
    private boolean needDivider;
    private ImageView optionsButton;
    private final Theme.ResourcesProvider resourcesProvider;
    private int statusColor;
    private int statusOnlineColor;
    private final SimpleTextView statusTextView;
    private final StoriesUtilities.AvatarStoryParams storyAvatarParams;
    private TL_stories$StoryItem storyItem;

    /* loaded from: classes4.dex */
    public interface ManageChatUserCellDelegate {
        boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public ManageChatUserCell(Context context, int i, int i2, boolean z) {
        this(context, i, i2, z, null);
    }

    public ManageChatUserCell(Context context, int i, int i2, boolean z, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.dividerColor = -1;
        this.currentAccount = UserConfig.selectedAccount;
        this.storyAvatarParams = new StoriesUtilities.AvatarStoryParams(false);
        this.resourcesProvider = resourcesProvider;
        this.statusColor = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, resourcesProvider);
        this.statusOnlineColor = Theme.getColor(Theme.key_windowBackgroundWhiteBlueText, resourcesProvider);
        this.namePadding = i2;
        this.avatarDrawable = new AvatarDrawable();
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Cells.ManageChatUserCell.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            public void onDraw(Canvas canvas) {
                int dp;
                if (ManageChatUserCell.this.storyItem != null) {
                    float dp2 = AndroidUtilities.dp(1.0f);
                    ManageChatUserCell.this.storyAvatarParams.originalAvatarRect.set(dp2, dp2, getMeasuredWidth() - dp, getMeasuredHeight() - dp);
                    ManageChatUserCell.this.storyAvatarParams.drawSegments = false;
                    ManageChatUserCell.this.storyAvatarParams.animate = false;
                    ManageChatUserCell.this.storyAvatarParams.drawInside = true;
                    ManageChatUserCell.this.storyAvatarParams.isArchive = false;
                    ManageChatUserCell.this.storyAvatarParams.resourcesProvider = resourcesProvider;
                    ManageChatUserCell.this.storyAvatarParams.storyItem = ManageChatUserCell.this.storyItem;
                    StoriesUtilities.drawAvatarWithStory(ManageChatUserCell.this.storyItem.dialogId, canvas, this.imageReceiver, ManageChatUserCell.this.storyAvatarParams);
                    return;
                }
                super.onDraw(canvas);
            }
        };
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(23.0f));
        boolean z2 = LocaleController.isRTL;
        addView(backupImageView, LayoutHelper.createFrame(46, 46.0f, (z2 ? 5 : 3) | 48, z2 ? 0.0f : i + 7, 8.0f, z2 ? i + 7 : 0.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.nameTextView = simpleTextView;
        simpleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        simpleTextView.setTextSize(17);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        boolean z3 = LocaleController.isRTL;
        addView(simpleTextView, LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? 46.0f : i2 + 68, 11.5f, z3 ? i2 + 68 : 46.0f, 0.0f));
        NotificationCenter.listenEmojiLoading(simpleTextView);
        SimpleTextView simpleTextView2 = new SimpleTextView(context);
        this.statusTextView = simpleTextView2;
        simpleTextView2.setTextSize(14);
        simpleTextView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        boolean z4 = LocaleController.isRTL;
        addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, (z4 ? 5 : 3) | 48, z4 ? 28.0f : i2 + 68, 34.5f, z4 ? i2 + 68 : 28.0f, 0.0f));
        if (z) {
            ImageView imageView = new ImageView(context);
            this.optionsButton = imageView;
            imageView.setFocusable(false);
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_stickers_menuSelector, resourcesProvider)));
            this.optionsButton.setImageResource(R.drawable.ic_ab_other);
            this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_stickers_menu, resourcesProvider), PorterDuff.Mode.MULTIPLY));
            this.optionsButton.setScaleType(ImageView.ScaleType.CENTER);
            addView(this.optionsButton, LayoutHelper.createFrame(60, 64, (LocaleController.isRTL ? 3 : 5) | 48));
            this.optionsButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Cells.ManageChatUserCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ManageChatUserCell.this.lambda$new$0(view);
                }
            });
            this.optionsButton.setContentDescription(LocaleController.getString(R.string.AccDescrUserOptions));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        this.delegate.onOptionsButtonCheck(this, true);
    }

    public void setStoryItem(TL_stories$StoryItem tL_stories$StoryItem, View.OnClickListener onClickListener) {
        this.storyItem = tL_stories$StoryItem;
        this.avatarImageView.setOnClickListener(onClickListener);
    }

    public TL_stories$StoryItem getStoryItem() {
        return this.storyItem;
    }

    public BackupImageView getAvatarImageView() {
        return this.avatarImageView;
    }

    public StoriesUtilities.AvatarStoryParams getStoryAvatarParams() {
        return this.storyAvatarParams;
    }

    public void setCustomRightImage(int i) {
        ImageView imageView = new ImageView(getContext());
        this.customImageView = imageView;
        imageView.setImageResource(i);
        this.customImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.customImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_voipgroup_mutedIconUnscrolled, this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
        addView(this.customImageView, LayoutHelper.createFrame(52, 64, (LocaleController.isRTL ? 3 : 5) | 48));
    }

    public void setCustomImageVisible(boolean z) {
        ImageView imageView = this.customImageView;
        if (imageView == null) {
            return;
        }
        imageView.setVisibility(z ? 0 : 8);
    }

    public void setData(Object obj, CharSequence charSequence, CharSequence charSequence2, boolean z) {
        float f;
        float f2;
        if (obj == null) {
            this.currentStatus = null;
            this.currentName = null;
            this.currentObject = null;
            this.nameTextView.setText("");
            this.statusTextView.setText("");
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.currentStatus = charSequence2;
        this.currentName = charSequence;
        this.currentObject = obj;
        if (this.optionsButton != null) {
            boolean onOptionsButtonCheck = this.delegate.onOptionsButtonCheck(this, false);
            this.optionsButton.setVisibility(onOptionsButtonCheck ? 0 : 4);
            SimpleTextView simpleTextView = this.nameTextView;
            boolean z2 = LocaleController.isRTL;
            simpleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, (z2 ? 5 : 3) | 48, z2 ? onOptionsButtonCheck ? 46 : 28 : this.namePadding + 68, (charSequence2 == null || charSequence2.length() > 0) ? 11.5f : 20.5f, LocaleController.isRTL ? this.namePadding + 68 : onOptionsButtonCheck ? 46 : 28, 0.0f));
            SimpleTextView simpleTextView2 = this.statusTextView;
            boolean z3 = LocaleController.isRTL;
            int i = (z3 ? 5 : 3) | 48;
            float f3 = z3 ? onOptionsButtonCheck ? 46 : 28 : this.namePadding + 68;
            if (z3) {
                f2 = this.namePadding + 68;
            } else {
                f2 = onOptionsButtonCheck ? 46 : 28;
            }
            simpleTextView2.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, i, f3, 34.5f, f2, 0.0f));
        } else {
            ImageView imageView = this.customImageView;
            if (imageView != null) {
                boolean z4 = imageView.getVisibility() == 0;
                SimpleTextView simpleTextView3 = this.nameTextView;
                boolean z5 = LocaleController.isRTL;
                simpleTextView3.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, (z5 ? 5 : 3) | 48, z5 ? z4 ? 54 : 28 : this.namePadding + 68, (charSequence2 == null || charSequence2.length() > 0) ? 11.5f : 20.5f, LocaleController.isRTL ? this.namePadding + 68 : z4 ? 54 : 28, 0.0f));
                SimpleTextView simpleTextView4 = this.statusTextView;
                boolean z6 = LocaleController.isRTL;
                int i2 = (z6 ? 5 : 3) | 48;
                float f4 = z6 ? z4 ? 54 : 28 : this.namePadding + 68;
                if (z6) {
                    f = this.namePadding + 68;
                } else {
                    f = z4 ? 54 : 28;
                }
                simpleTextView4.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, i2, f4, 34.5f, f, 0.0f));
            }
        }
        this.needDivider = z;
        setWillNotDraw(!z);
        update(0);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    public long getUserId() {
        Object obj = this.currentObject;
        if (obj instanceof TLRPC$User) {
            return ((TLRPC$User) obj).id;
        }
        return 0L;
    }

    public void setStatusColors(int i, int i2) {
        this.statusColor = i;
        this.statusOnlineColor = i2;
    }

    public void setIsAdmin(boolean z) {
        this.isAdmin = z;
    }

    public boolean hasAvatarSet() {
        return this.avatarImageView.getImageReceiver().hasNotThumb();
    }

    public void setNameColor(int i) {
        this.nameTextView.setTextColor(i);
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x019d, code lost:
        if (r12.equals(r6) == false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0067, code lost:
        if (r12.equals(r11.lastName) == false) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i) {
        String str;
        TLRPC$FileLocation tLRPC$FileLocation;
        String str2;
        TLRPC$UserStatus tLRPC$UserStatus;
        TLRPC$FileLocation tLRPC$FileLocation2;
        Object obj = this.currentObject;
        if (obj == null) {
            return;
        }
        boolean z = true;
        if (obj instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) obj;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
            TLRPC$FileLocation tLRPC$FileLocation3 = tLRPC$UserProfilePhoto != null ? tLRPC$UserProfilePhoto.photo_small : null;
            if (i != 0) {
                boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation2 = this.lastAvatar) != null && tLRPC$FileLocation3 == null) || ((tLRPC$FileLocation2 == null && tLRPC$FileLocation3 != null) || !(tLRPC$FileLocation2 == null || (tLRPC$FileLocation2.volume_id == tLRPC$FileLocation3.volume_id && tLRPC$FileLocation2.local_id == tLRPC$FileLocation3.local_id))));
                if (!z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                    TLRPC$UserStatus tLRPC$UserStatus2 = tLRPC$User.status;
                    if ((tLRPC$UserStatus2 != null ? tLRPC$UserStatus2.expires : 0) != this.lastStatus) {
                        z2 = true;
                    }
                }
                if (z2 || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str2 = null;
                } else {
                    str2 = UserObject.getUserName(tLRPC$User);
                }
                z = z2;
                if (!z) {
                    return;
                }
            } else {
                str2 = null;
            }
            this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User);
            TLRPC$UserStatus tLRPC$UserStatus3 = tLRPC$User.status;
            if (tLRPC$UserStatus3 != null) {
                this.lastStatus = tLRPC$UserStatus3.expires;
            } else {
                this.lastStatus = 0;
            }
            CharSequence charSequence = this.currentName;
            if (charSequence != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence);
            } else {
                if (str2 == null) {
                    str2 = UserObject.getUserName(tLRPC$User);
                }
                this.lastName = str2;
                SimpleTextView simpleTextView = this.nameTextView;
                simpleTextView.setText(Emoji.replaceEmoji((CharSequence) str2, simpleTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(15.0f), false));
            }
            if (this.currentStatus != null) {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(this.currentStatus);
            } else if (tLRPC$User.bot) {
                this.statusTextView.setTextColor(this.statusColor);
                if (tLRPC$User.bot_chat_history || this.isAdmin) {
                    this.statusTextView.setText(LocaleController.getString(R.string.BotStatusRead));
                } else {
                    this.statusTextView.setText(LocaleController.getString(R.string.BotStatusCantRead));
                }
            } else if (tLRPC$User.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || (((tLRPC$UserStatus = tLRPC$User.status) != null && tLRPC$UserStatus.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(tLRPC$User.id)))) {
                this.statusTextView.setTextColor(this.statusOnlineColor);
                this.statusTextView.setText(LocaleController.getString(R.string.Online));
            } else {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, tLRPC$User));
            }
            this.lastAvatar = tLRPC$FileLocation3;
            this.avatarImageView.setForUserOrChat(tLRPC$User, this.avatarDrawable);
        } else if (obj instanceof TLRPC$Chat) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
            TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
            TLRPC$FileLocation tLRPC$FileLocation4 = tLRPC$ChatPhoto != null ? tLRPC$ChatPhoto.photo_small : null;
            if (i != 0) {
                boolean z3 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation = this.lastAvatar) != null && tLRPC$FileLocation4 == null) || ((tLRPC$FileLocation == null && tLRPC$FileLocation4 != null) || !(tLRPC$FileLocation == null || (tLRPC$FileLocation.volume_id == tLRPC$FileLocation4.volume_id && tLRPC$FileLocation.local_id == tLRPC$FileLocation4.local_id))));
                if (z3 || this.currentName != null || (r6 = this.lastName) == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str = null;
                } else {
                    str = tLRPC$Chat.title;
                }
                z = z3;
                if (!z) {
                    return;
                }
            } else {
                str = null;
            }
            this.avatarDrawable.setInfo(this.currentAccount, tLRPC$Chat);
            CharSequence charSequence2 = this.currentName;
            if (charSequence2 != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence2);
            } else {
                if (str == null) {
                    str = tLRPC$Chat.title;
                }
                this.lastName = str;
                this.nameTextView.setText(str);
            }
            if (this.currentStatus != null) {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(this.currentStatus);
            } else {
                this.statusTextView.setTextColor(this.statusColor);
                if (tLRPC$Chat.participants_count != 0) {
                    if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                        this.statusTextView.setText(LocaleController.formatPluralString("Subscribers", tLRPC$Chat.participants_count, new Object[0]));
                    } else {
                        this.statusTextView.setText(LocaleController.formatPluralString("Members", tLRPC$Chat.participants_count, new Object[0]));
                    }
                } else if (tLRPC$Chat.has_geo) {
                    this.statusTextView.setText(LocaleController.getString(R.string.MegaLocation));
                } else if (!ChatObject.isPublic(tLRPC$Chat)) {
                    this.statusTextView.setText(LocaleController.getString(R.string.MegaPrivate));
                } else {
                    this.statusTextView.setText(LocaleController.getString(R.string.MegaPublic));
                }
            }
            this.lastAvatar = tLRPC$FileLocation4;
            this.avatarImageView.setForUserOrChat(tLRPC$Chat, this.avatarDrawable);
        } else if (obj instanceof Integer) {
            this.nameTextView.setText(this.currentName);
            this.statusTextView.setTextColor(this.statusColor);
            this.statusTextView.setText(this.currentStatus);
            this.avatarDrawable.setAvatarType(3);
            this.avatarImageView.setImage(null, "50_50", this.avatarDrawable);
        }
    }

    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }

    public void setDelegate(ManageChatUserCellDelegate manageChatUserCellDelegate) {
        this.delegate = manageChatUserCellDelegate;
    }

    public Object getCurrentObject() {
        return this.currentObject;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.needDivider) {
            int i = this.dividerColor;
            if (i >= 0) {
                Theme.dividerExtraPaint.setColor(Theme.getColor(i, this.resourcesProvider));
            }
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(68.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(68.0f) : 0), getMeasuredHeight() - 1, this.dividerColor >= 0 ? Theme.dividerExtraPaint : Theme.dividerPaint);
        }
    }
}
