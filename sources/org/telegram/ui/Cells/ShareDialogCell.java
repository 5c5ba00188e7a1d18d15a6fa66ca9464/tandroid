package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
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
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.RLottieDrawable;

/* loaded from: classes4.dex */
public class ShareDialogCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private final AvatarDrawable avatarDrawable;
    private final CheckBox2 checkBox;
    private final int currentAccount;
    private long currentDialog;
    private final int currentType;
    private final BackupImageView imageView;
    private long lastUpdateTime;
    private Drawable lockDrawable;
    private final TextView nameTextView;
    private float onlineProgress;
    private boolean premiumBlocked;
    private final AnimatedFloat premiumBlockedT;
    private PremiumGradient.PremiumGradientTools premiumGradient;
    private RepostStoryDrawable repostStoryDrawable;
    public final Theme.ResourcesProvider resourcesProvider;
    private final SimpleTextView topicTextView;
    private boolean topicWasVisible;
    private TLRPC.User user;

    public static class RepostStoryDrawable extends Drawable {
        int alpha;
        private final Drawable drawable;
        private final LinearGradient gradient;
        private final RLottieDrawable lottieDrawable;
        private final Paint paint;

        public RepostStoryDrawable(Context context, View view, boolean z, Theme.ResourcesProvider resourcesProvider) {
            Paint paint = new Paint(1);
            this.paint = paint;
            this.alpha = NotificationCenter.liveLocationsChanged;
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f), new int[]{Theme.getColor(Theme.key_stories_circle1, resourcesProvider), Theme.getColor(Theme.key_stories_circle2, resourcesProvider)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
            this.gradient = linearGradient;
            paint.setShader(linearGradient);
            if (!z) {
                this.lottieDrawable = null;
                Drawable mutate = context.getResources().getDrawable(R.drawable.large_repost_story).mutate();
                this.drawable = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                return;
            }
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.story_repost, "story_repost", AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f), true, null);
            this.lottieDrawable = rLottieDrawable;
            rLottieDrawable.setMasterParent(view);
            AndroidUtilities.runOnUIThread(new ShareDialogCell$RepostStoryDrawable$$ExternalSyntheticLambda0(rLottieDrawable), 450L);
            this.drawable = null;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.translate(getBounds().left, getBounds().top);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getBounds().width(), getBounds().height());
            this.paint.setAlpha(this.alpha);
            float min = (Math.min(getBounds().width(), getBounds().height()) / 2.0f) * (this.alpha / 255.0f);
            canvas.drawRoundRect(rectF, min, min, this.paint);
            canvas.restore();
            int dp = AndroidUtilities.dp(this.lottieDrawable != null ? 20.0f : 15.0f);
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set(getBounds().centerX() - dp, getBounds().centerY() - dp, getBounds().centerX() + dp, getBounds().centerY() + dp);
            Drawable drawable = this.lottieDrawable;
            if (drawable == null) {
                drawable = this.drawable;
            }
            if (drawable != null) {
                drawable.setBounds(rect);
                drawable.setAlpha(this.alpha);
                drawable.draw(canvas);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(56.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(56.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.alpha = i;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    public ShareDialogCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i2;
        float f;
        this.currentAccount = UserConfig.selectedAccount;
        this.premiumBlockedT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.resourcesProvider = resourcesProvider;
        this.avatarDrawable = new AvatarDrawable(resourcesProvider) { // from class: org.telegram.ui.Cells.ShareDialogCell.1
            @Override // android.graphics.drawable.Drawable
            public void invalidateSelf() {
                super.invalidateSelf();
                ShareDialogCell.this.imageView.invalidate();
            }
        };
        setWillNotDraw(false);
        this.currentType = i;
        BackupImageView backupImageView = new BackupImageView(context);
        this.imageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(28.0f));
        if (i == 2) {
            i2 = 48;
            f = 48.0f;
        } else {
            i2 = 56;
            f = 56.0f;
        }
        addView(backupImageView, LayoutHelper.createFrame(i2, f, 49, 0.0f, 7.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context) { // from class: org.telegram.ui.Cells.ShareDialogCell.2
            @Override // android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), false), bufferType);
            }
        };
        this.nameTextView = textView;
        NotificationCenter.listenEmojiLoading(textView);
        textView.setTextColor(getThemedColor(this.premiumBlocked ? Theme.key_windowBackgroundWhiteGrayText5 : Theme.key_dialogTextBlack));
        textView.setTextSize(1, 12.0f);
        textView.setMaxLines(2);
        textView.setGravity(49);
        textView.setLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        addView(textView, LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, i == 2 ? 58.0f : 66.0f, 6.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.topicTextView = simpleTextView;
        simpleTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
        simpleTextView.setTextSize(12);
        simpleTextView.setMaxLines(2);
        simpleTextView.setGravity(49);
        simpleTextView.setAlignment(Layout.Alignment.ALIGN_CENTER);
        addView(simpleTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, i == 2 ? 58.0f : 66.0f, 6.0f, 0.0f));
        CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setColor(Theme.key_dialogRoundCheckBox, Theme.key_dialogBackground, Theme.key_dialogRoundCheckBoxCheck);
        checkBox2.setDrawUnchecked(false);
        checkBox2.setDrawBackgroundAsArc(4);
        checkBox2.setProgressDelegate(new CheckBoxBase.ProgressDelegate() { // from class: org.telegram.ui.Cells.ShareDialogCell$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.CheckBoxBase.ProgressDelegate
            public final void setProgress(float f2) {
                ShareDialogCell.this.lambda$new$0(f2);
            }
        });
        addView(checkBox2, LayoutHelper.createFrame(24, 24.0f, 49, 19.0f, i == 2 ? -40.0f : 42.0f, 0.0f, 0.0f));
        setBackground(Theme.createRadSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f)));
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(float f) {
        float progress = 1.0f - (this.checkBox.getProgress() * 0.143f);
        this.imageView.setScaleX(progress);
        this.imageView.setScaleY(progress);
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTopic$1(DynamicAnimation dynamicAnimation, float f, float f2) {
        float f3 = f / 1000.0f;
        this.topicTextView.setAlpha(f3);
        float f4 = 1.0f - f3;
        this.nameTextView.setAlpha(f4);
        this.topicTextView.setTranslationX(f4 * (-AndroidUtilities.dp(10.0f)));
        this.nameTextView.setTranslationX(f3 * AndroidUtilities.dp(10.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTopic$2(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.topicTextView.setTag(R.id.spring_tag, null);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.userIsPremiumBlockedUpadted) {
            boolean z = this.premiumBlocked;
            boolean z2 = this.user != null && MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(this.user.id);
            this.premiumBlocked = z2;
            this.nameTextView.setTextColor(getThemedColor(z2 ? Theme.key_windowBackgroundWhiteGrayText5 : Theme.key_dialogTextBlack));
            if (this.premiumBlocked != z) {
                invalidate();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x01df, code lost:
    
        if (r3 > 1.0f) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x01e1, code lost:
    
        r25.onlineProgress = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x01f4, code lost:
    
        r25.imageView.invalidate();
        invalidate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01f1, code lost:
    
        if (r3 < 0.0f) goto L45;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01e4  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean drawChild(Canvas canvas, View view, long j) {
        TLRPC.User user;
        boolean z;
        TLRPC.UserStatus userStatus;
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.imageView && this.currentType != 2 && (user = this.user) != null && !MessagesController.isSupportUser(user)) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j2 = elapsedRealtime - this.lastUpdateTime;
            if (j2 > 17) {
                j2 = 17;
            }
            this.lastUpdateTime = elapsedRealtime;
            float f = this.premiumBlockedT.set(this.premiumBlocked);
            float f2 = 0.0f;
            if (f > 0.0f) {
                int bottom = this.imageView.getBottom() - AndroidUtilities.dp(9.0f);
                int right = this.imageView.getRight() - AndroidUtilities.dp(9.33f);
                canvas.save();
                Theme.dialogs_onlineCirclePaint.setColor(getThemedColor(Theme.key_windowBackgroundWhite));
                float f3 = right;
                float f4 = bottom;
                canvas.drawCircle(f3, f4, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                if (this.premiumGradient == null) {
                    this.premiumGradient = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradient1, Theme.key_premiumGradient2, -1, -1, -1, this.resourcesProvider);
                }
                this.premiumGradient.gradientMatrix(right - AndroidUtilities.dp(10.0f), bottom - AndroidUtilities.dp(10.0f), right + AndroidUtilities.dp(10.0f), bottom + AndroidUtilities.dp(10.0f), 0.0f, 0.0f);
                canvas.drawCircle(f3, f4, AndroidUtilities.dp(10.0f) * f, this.premiumGradient.paint);
                if (this.lockDrawable == null) {
                    Drawable mutate = getContext().getResources().getDrawable(R.drawable.msg_mini_lock2).mutate();
                    this.lockDrawable = mutate;
                    mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                }
                this.lockDrawable.setBounds((int) (f3 - (((r4.getIntrinsicWidth() / 2.0f) * 0.875f) * f)), (int) (f4 - (((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f) * f)), (int) (f3 + ((this.lockDrawable.getIntrinsicWidth() / 2.0f) * 0.875f * f)), (int) (f4 + ((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f * f)));
                this.lockDrawable.setAlpha((int) (f * 255.0f));
                this.lockDrawable.draw(canvas);
                canvas.restore();
            } else {
                if (!this.premiumBlocked) {
                    TLRPC.User user2 = this.user;
                    if (!user2.self && !user2.bot && (((userStatus = user2.status) != null && userStatus.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(this.user.id)))) {
                        z = true;
                        if (!z || this.onlineProgress != 0.0f) {
                            int bottom2 = this.imageView.getBottom() - AndroidUtilities.dp(6.0f);
                            int right2 = this.imageView.getRight() - AndroidUtilities.dp(10.0f);
                            Theme.dialogs_onlineCirclePaint.setColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            float f5 = right2;
                            float f6 = bottom2;
                            canvas.drawCircle(f5, f6, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                            Theme.dialogs_onlineCirclePaint.setColor(getThemedColor(Theme.key_chats_onlineCircle));
                            canvas.drawCircle(f5, f6, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                            if (z) {
                                float f7 = this.onlineProgress;
                                if (f7 > 0.0f) {
                                    float f8 = f7 - (j2 / 150.0f);
                                    this.onlineProgress = f8;
                                }
                            } else {
                                float f9 = this.onlineProgress;
                                f2 = 1.0f;
                                if (f9 < 1.0f) {
                                    float f10 = f9 + (j2 / 150.0f);
                                    this.onlineProgress = f10;
                                }
                            }
                        }
                    }
                }
                z = false;
                if (!z) {
                }
                int bottom22 = this.imageView.getBottom() - AndroidUtilities.dp(6.0f);
                int right22 = this.imageView.getRight() - AndroidUtilities.dp(10.0f);
                Theme.dialogs_onlineCirclePaint.setColor(getThemedColor(Theme.key_windowBackgroundWhite));
                float f52 = right22;
                float f62 = bottom22;
                canvas.drawCircle(f52, f62, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                Theme.dialogs_onlineCirclePaint.setColor(getThemedColor(Theme.key_chats_onlineCircle));
                canvas.drawCircle(f52, f62, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                if (z) {
                }
            }
        }
        return drawChild;
    }

    public long getCurrentDialog() {
        return this.currentDialog;
    }

    public BackupImageView getImageView() {
        return this.imageView;
    }

    public boolean isBlocked() {
        return this.premiumBlocked;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int left = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() / 2);
        int top = this.imageView.getTop() + (this.imageView.getMeasuredHeight() / 2);
        Theme.checkboxSquare_checkPaint.setColor(getThemedColor(Theme.key_dialogRoundCheckBox));
        Theme.checkboxSquare_checkPaint.setAlpha((int) (this.checkBox.getProgress() * 255.0f));
        int dp = AndroidUtilities.dp(this.currentType == 2 ? 24.0f : 28.0f);
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(left - dp, top - dp, left + dp, top + dp);
        canvas.drawRoundRect(rectF, this.imageView.getRoundRadius()[0], this.imageView.getRoundRadius()[0], Theme.checkboxSquare_checkPaint);
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setSelected(true);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.currentType == 2 ? 95.0f : 103.0f), 1073741824));
    }

    protected String repostToCustomName() {
        return LocaleController.getString(R.string.FwdMyStory);
    }

    public void setChecked(boolean z, boolean z2) {
        this.checkBox.setChecked(z, z2);
        if (z) {
            return;
        }
        setTopic(null, true);
    }

    public void setDialog(long j, boolean z, CharSequence charSequence) {
        BackupImageView backupImageView;
        int dp;
        TextView textView;
        if (j == Long.MAX_VALUE) {
            this.nameTextView.setText(repostToCustomName());
            if (this.repostStoryDrawable == null) {
                this.repostStoryDrawable = new RepostStoryDrawable(getContext(), this.imageView, true, this.resourcesProvider);
            }
            this.imageView.setImage((ImageLocation) null, (String) null, this.repostStoryDrawable, (Object) null);
        } else {
            if (DialogObject.isUserDialog(j)) {
                this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
                boolean isUserPremiumBlocked = MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(j);
                this.premiumBlocked = isUserPremiumBlocked;
                this.nameTextView.setTextColor(getThemedColor(isUserPremiumBlocked ? Theme.key_windowBackgroundWhiteGrayText5 : Theme.key_dialogTextBlack));
                this.premiumBlockedT.set(this.premiumBlocked, true);
                invalidate();
                this.avatarDrawable.setInfo(this.currentAccount, this.user);
                if (this.currentType != 2 && UserObject.isReplyUser(this.user)) {
                    this.nameTextView.setText(LocaleController.getString(R.string.RepliesTitle));
                    this.avatarDrawable.setAvatarType(12);
                } else if (this.currentType == 2 || !UserObject.isUserSelf(this.user)) {
                    if (charSequence != null) {
                        textView = this.nameTextView;
                    } else {
                        TLRPC.User user = this.user;
                        if (user != null) {
                            textView = this.nameTextView;
                            charSequence = ContactsController.formatName(user.first_name, user.last_name);
                        } else {
                            this.nameTextView.setText("");
                            this.imageView.setForUserOrChat(this.user, this.avatarDrawable);
                            backupImageView = this.imageView;
                        }
                    }
                    textView.setText(charSequence);
                    this.imageView.setForUserOrChat(this.user, this.avatarDrawable);
                    backupImageView = this.imageView;
                } else {
                    this.nameTextView.setText(LocaleController.getString(R.string.SavedMessages));
                    this.avatarDrawable.setAvatarType(1);
                }
                this.imageView.setImage((ImageLocation) null, (String) null, this.avatarDrawable, this.user);
                backupImageView = this.imageView;
            } else {
                this.user = null;
                this.premiumBlocked = false;
                this.premiumBlockedT.set(0.0f, true);
                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
                if (charSequence != null) {
                    this.nameTextView.setText(charSequence);
                } else {
                    TextView textView2 = this.nameTextView;
                    if (chat != null) {
                        textView2.setText(chat.title);
                    } else {
                        textView2.setText("");
                    }
                }
                this.avatarDrawable.setInfo(this.currentAccount, chat);
                this.imageView.setForUserOrChat(chat, this.avatarDrawable);
                backupImageView = this.imageView;
                if (chat != null && chat.forum) {
                    dp = AndroidUtilities.dp(16.0f);
                    backupImageView.setRoundRadius(dp);
                }
            }
            dp = AndroidUtilities.dp(28.0f);
            backupImageView.setRoundRadius(dp);
        }
        this.currentDialog = j;
        this.checkBox.setChecked(z, false);
    }

    public void setTopic(TLRPC.TL_forumTopic tL_forumTopic, boolean z) {
        boolean z2 = this.topicWasVisible;
        boolean z3 = tL_forumTopic != null;
        if (z2 == z3 && z) {
            return;
        }
        SimpleTextView simpleTextView = this.topicTextView;
        int i = R.id.spring_tag;
        SpringAnimation springAnimation = (SpringAnimation) simpleTextView.getTag(i);
        if (springAnimation != null) {
            springAnimation.cancel();
        }
        if (z3) {
            SimpleTextView simpleTextView2 = this.topicTextView;
            simpleTextView2.setText(ForumUtilities.getTopicSpannedName(tL_forumTopic, simpleTextView2.getTextPaint(), false));
            this.topicTextView.requestLayout();
        }
        if (z) {
            SpringAnimation springAnimation2 = (SpringAnimation) ((SpringAnimation) new SpringAnimation(new FloatValueHolder(z3 ? 0.0f : 1000.0f)).setSpring(new SpringForce(z3 ? 1000.0f : 0.0f).setStiffness(1500.0f).setDampingRatio(1.0f)).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Cells.ShareDialogCell$$ExternalSyntheticLambda1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                    ShareDialogCell.this.lambda$setTopic$1(dynamicAnimation, f, f2);
                }
            })).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Cells.ShareDialogCell$$ExternalSyntheticLambda2
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z4, float f, float f2) {
                    ShareDialogCell.this.lambda$setTopic$2(dynamicAnimation, z4, f, f2);
                }
            });
            this.topicTextView.setTag(i, springAnimation2);
            springAnimation2.start();
        } else {
            SimpleTextView simpleTextView3 = this.topicTextView;
            if (z3) {
                simpleTextView3.setAlpha(1.0f);
                this.nameTextView.setAlpha(0.0f);
                this.topicTextView.setTranslationX(0.0f);
                this.nameTextView.setTranslationX(AndroidUtilities.dp(10.0f));
            } else {
                simpleTextView3.setAlpha(0.0f);
                this.nameTextView.setAlpha(1.0f);
                this.topicTextView.setTranslationX(-AndroidUtilities.dp(10.0f));
                this.nameTextView.setTranslationX(0.0f);
            }
        }
        this.topicWasVisible = z3;
    }
}
