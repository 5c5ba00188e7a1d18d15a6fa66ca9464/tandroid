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
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
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
    private TLRPC.FileLocation lastAvatar;
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
    private TL_stories.StoryItem storyItem;

    /* loaded from: classes4.dex */
    public interface ManageChatUserCellDelegate {
        boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z);
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
                if (ManageChatUserCell.this.storyItem == null) {
                    super.onDraw(canvas);
                    return;
                }
                float dp = AndroidUtilities.dp(1.0f);
                ManageChatUserCell.this.storyAvatarParams.originalAvatarRect.set(dp, dp, getMeasuredWidth() - r0, getMeasuredHeight() - r0);
                ManageChatUserCell.this.storyAvatarParams.drawSegments = false;
                ManageChatUserCell.this.storyAvatarParams.animate = false;
                ManageChatUserCell.this.storyAvatarParams.drawInside = true;
                ManageChatUserCell.this.storyAvatarParams.isArchive = false;
                ManageChatUserCell.this.storyAvatarParams.resourcesProvider = resourcesProvider;
                ManageChatUserCell.this.storyAvatarParams.storyItem = ManageChatUserCell.this.storyItem;
                StoriesUtilities.drawAvatarWithStory(ManageChatUserCell.this.storyItem.dialogId, canvas, this.imageReceiver, ManageChatUserCell.this.storyAvatarParams);
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

    public BackupImageView getAvatarImageView() {
        return this.avatarImageView;
    }

    public Object getCurrentObject() {
        return this.currentObject;
    }

    public StoriesUtilities.AvatarStoryParams getStoryAvatarParams() {
        return this.storyAvatarParams;
    }

    public TL_stories.StoryItem getStoryItem() {
        return this.storyItem;
    }

    public long getUserId() {
        Object obj = this.currentObject;
        if (obj instanceof TLRPC.User) {
            return ((TLRPC.User) obj).id;
        }
        return 0L;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
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

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }

    public void setCustomImageVisible(boolean z) {
        ImageView imageView = this.customImageView;
        if (imageView == null) {
            return;
        }
        imageView.setVisibility(z ? 0 : 8);
    }

    public void setCustomRightImage(int i) {
        ImageView imageView = new ImageView(getContext());
        this.customImageView = imageView;
        imageView.setImageResource(i);
        this.customImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.customImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_voipgroup_mutedIconUnscrolled, this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
        addView(this.customImageView, LayoutHelper.createFrame(52, 64, (LocaleController.isRTL ? 3 : 5) | 48));
    }

    public void setData(Object obj, CharSequence charSequence, CharSequence charSequence2, boolean z) {
        SimpleTextView simpleTextView;
        int i;
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
        int i2 = 28;
        if (this.optionsButton == null) {
            ImageView imageView = this.customImageView;
            if (imageView != null) {
                boolean z2 = imageView.getVisibility() == 0;
                SimpleTextView simpleTextView2 = this.nameTextView;
                boolean z3 = LocaleController.isRTL;
                simpleTextView2.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? z2 ? 54 : 28 : this.namePadding + 68, (charSequence2 == null || charSequence2.length() > 0) ? 11.5f : 20.5f, LocaleController.isRTL ? this.namePadding + 68 : z2 ? 54 : 28, 0.0f));
                simpleTextView = this.statusTextView;
                boolean z4 = LocaleController.isRTL;
                i = (z4 ? 5 : 3) | 48;
                f = z4 ? z2 ? 54 : 28 : this.namePadding + 68;
                if (!z4) {
                    if (z2) {
                        i2 = 54;
                    }
                    f2 = i2;
                }
                f2 = this.namePadding + 68;
            }
            this.needDivider = z;
            setWillNotDraw(!z);
            update(0);
        }
        boolean onOptionsButtonCheck = this.delegate.onOptionsButtonCheck(this, false);
        this.optionsButton.setVisibility(onOptionsButtonCheck ? 0 : 4);
        SimpleTextView simpleTextView3 = this.nameTextView;
        boolean z5 = LocaleController.isRTL;
        simpleTextView3.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, (z5 ? 5 : 3) | 48, z5 ? onOptionsButtonCheck ? 46 : 28 : this.namePadding + 68, (charSequence2 == null || charSequence2.length() > 0) ? 11.5f : 20.5f, LocaleController.isRTL ? this.namePadding + 68 : onOptionsButtonCheck ? 46 : 28, 0.0f));
        simpleTextView = this.statusTextView;
        boolean z6 = LocaleController.isRTL;
        i = (z6 ? 5 : 3) | 48;
        f = z6 ? onOptionsButtonCheck ? 46 : 28 : this.namePadding + 68;
        if (!z6) {
            if (onOptionsButtonCheck) {
                i2 = 46;
            }
            f2 = i2;
        }
        f2 = this.namePadding + 68;
        simpleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, i, f, 34.5f, f2, 0.0f));
        this.needDivider = z;
        setWillNotDraw(!z);
        update(0);
    }

    public void setDelegate(ManageChatUserCellDelegate manageChatUserCellDelegate) {
        this.delegate = manageChatUserCellDelegate;
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
    }

    public void setIsAdmin(boolean z) {
        this.isAdmin = z;
    }

    public void setNameColor(int i) {
        this.nameTextView.setTextColor(i);
    }

    public void setStatusColors(int i, int i2) {
        this.statusColor = i;
        this.statusOnlineColor = i2;
    }

    public void setStoryItem(TL_stories.StoryItem storyItem, View.OnClickListener onClickListener) {
        this.storyItem = storyItem;
        this.avatarImageView.setOnClickListener(onClickListener);
    }

    /* JADX WARN: Code restructure failed: missing block: B:105:0x0173, code lost:
    
        if (r12.equals(r6) == false) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0067, code lost:
    
        if (r12.equals(r11.lastName) == false) goto L48;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i) {
        TLRPC.FileLocation fileLocation;
        String str;
        SimpleTextView simpleTextView;
        int i2;
        CharSequence formatPluralString;
        TLRPC.FileLocation fileLocation2;
        TLRPC.Chat chat;
        TLRPC.Chat chat2;
        TLRPC.Chat chat3;
        String str2;
        TLRPC.UserStatus userStatus;
        TLRPC.FileLocation fileLocation3;
        Object obj = this.currentObject;
        if (obj == null) {
            return;
        }
        boolean z = true;
        if (obj instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) obj;
            TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
            fileLocation = userProfilePhoto != null ? userProfilePhoto.photo_small : null;
            if (i != 0) {
                boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((fileLocation3 = this.lastAvatar) != null && fileLocation == null) || ((fileLocation3 == null && fileLocation != null) || !(fileLocation3 == null || (fileLocation3.volume_id == fileLocation.volume_id && fileLocation3.local_id == fileLocation.local_id))));
                if (!z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                    TLRPC.UserStatus userStatus2 = user.status;
                    if ((userStatus2 != null ? userStatus2.expires : 0) != this.lastStatus) {
                        z2 = true;
                    }
                }
                if (z2 || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str2 = null;
                } else {
                    str2 = UserObject.getUserName(user);
                }
                z = z2;
                if (!z) {
                    return;
                }
            } else {
                str2 = null;
            }
            this.avatarDrawable.setInfo(this.currentAccount, user);
            TLRPC.UserStatus userStatus3 = user.status;
            if (userStatus3 != null) {
                this.lastStatus = userStatus3.expires;
            } else {
                this.lastStatus = 0;
            }
            CharSequence charSequence = this.currentName;
            if (charSequence != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence);
            } else {
                if (str2 == null) {
                    str2 = UserObject.getUserName(user);
                }
                this.lastName = str2;
                SimpleTextView simpleTextView2 = this.nameTextView;
                simpleTextView2.setText(Emoji.replaceEmoji((CharSequence) str2, simpleTextView2.getPaint().getFontMetricsInt(), AndroidUtilities.dp(15.0f), false));
            }
            chat = user;
            if (this.currentStatus == null) {
                if (user.bot) {
                    this.statusTextView.setTextColor(this.statusColor);
                    if (user.bot_chat_history || this.isAdmin) {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.BotStatusRead;
                        chat3 = user;
                    } else {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.BotStatusCantRead;
                        chat3 = user;
                    }
                } else if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || (((userStatus = user.status) != null && userStatus.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(user.id)))) {
                    this.statusTextView.setTextColor(this.statusOnlineColor);
                    simpleTextView = this.statusTextView;
                    i2 = R.string.Online;
                    chat3 = user;
                } else {
                    this.statusTextView.setTextColor(this.statusColor);
                    simpleTextView = this.statusTextView;
                    formatPluralString = LocaleController.formatUserStatus(this.currentAccount, user);
                    chat2 = user;
                }
                formatPluralString = LocaleController.getString(i2);
                chat2 = chat3;
            }
            this.statusTextView.setTextColor(this.statusColor);
            simpleTextView = this.statusTextView;
            formatPluralString = this.currentStatus;
            chat2 = chat;
        } else {
            if (!(obj instanceof TLRPC.Chat)) {
                if (obj instanceof Integer) {
                    this.nameTextView.setText(this.currentName);
                    this.statusTextView.setTextColor(this.statusColor);
                    this.statusTextView.setText(this.currentStatus);
                    this.avatarDrawable.setAvatarType(3);
                    this.avatarImageView.setImage(null, "50_50", this.avatarDrawable);
                    return;
                }
                return;
            }
            TLRPC.Chat chat4 = (TLRPC.Chat) obj;
            TLRPC.ChatPhoto chatPhoto = chat4.photo;
            fileLocation = chatPhoto != null ? chatPhoto.photo_small : null;
            if (i != 0) {
                boolean z3 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((fileLocation2 = this.lastAvatar) != null && fileLocation == null) || ((fileLocation2 == null && fileLocation != null) || !(fileLocation2 == null || (fileLocation2.volume_id == fileLocation.volume_id && fileLocation2.local_id == fileLocation.local_id))));
                if (z3 || this.currentName != null || (r6 = this.lastName) == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str = null;
                } else {
                    str = chat4.title;
                }
                z = z3;
                if (!z) {
                    return;
                }
            } else {
                str = null;
            }
            this.avatarDrawable.setInfo(this.currentAccount, chat4);
            CharSequence charSequence2 = this.currentName;
            if (charSequence2 != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence2);
            } else {
                if (str == null) {
                    str = chat4.title;
                }
                this.lastName = str;
                this.nameTextView.setText(str);
            }
            chat = chat4;
            if (this.currentStatus == null) {
                this.statusTextView.setTextColor(this.statusColor);
                if (chat4.participants_count == 0) {
                    if (chat4.has_geo) {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.MegaLocation;
                        chat3 = chat4;
                    } else if (ChatObject.isPublic(chat4)) {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.MegaPublic;
                        chat3 = chat4;
                    } else {
                        simpleTextView = this.statusTextView;
                        i2 = R.string.MegaPrivate;
                        chat3 = chat4;
                    }
                    formatPluralString = LocaleController.getString(i2);
                    chat2 = chat3;
                } else if (!ChatObject.isChannel(chat4) || chat4.megagroup) {
                    simpleTextView = this.statusTextView;
                    formatPluralString = LocaleController.formatPluralString("Members", chat4.participants_count, new Object[0]);
                    chat2 = chat4;
                } else {
                    simpleTextView = this.statusTextView;
                    formatPluralString = LocaleController.formatPluralString("Subscribers", chat4.participants_count, new Object[0]);
                    chat2 = chat4;
                }
            }
            this.statusTextView.setTextColor(this.statusColor);
            simpleTextView = this.statusTextView;
            formatPluralString = this.currentStatus;
            chat2 = chat;
        }
        simpleTextView.setText(formatPluralString);
        this.lastAvatar = fileLocation;
        this.avatarImageView.setForUserOrChat(chat2, this.avatarDrawable);
    }
}
