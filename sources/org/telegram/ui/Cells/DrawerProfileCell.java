package org.telegram.ui.Cells;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme$$ExternalSyntheticApiModelOutline2;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.Reactions.AnimatedEmojiEffect;
import org.telegram.ui.Components.Reactions.HwEmojis;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.SnowflakesEffect;
import org.telegram.ui.ThemeActivity;
/* loaded from: classes4.dex */
public abstract class DrawerProfileCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static RLottieDrawable sunDrawable;
    public static boolean switchingTheme;
    private boolean accountsShown;
    private AnimatedStatusView animatedStatus;
    private ImageView arrowView;
    private BackupImageView avatarImageView;
    private Paint backPaint;
    private Integer currentColor;
    private Integer currentMoonColor;
    private int darkThemeBackgroundColor;
    private RLottieImageView darkThemeView;
    private Rect destRect;
    public boolean drawPremium;
    public float drawPremiumProgress;
    PremiumGradient.PremiumGradientTools gradientTools;
    private int lastAccount;
    private TLRPC$User lastUser;
    private SimpleTextView nameTextView;
    private Paint paint;
    private TextView phoneTextView;
    private Drawable premiumStar;
    private ImageView shadowView;
    private SnowflakesEffect snowflakesEffect;
    private Rect srcRect;
    StarParticlesView.Drawable starParticlesDrawable;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable status;
    private boolean updateRightDrawable;

    /* loaded from: classes4.dex */
    public static class AnimatedStatusView extends View {
        private int animationUniq;
        private ArrayList animations;
        private Integer color;
        private int effectsSize;
        private int renderedEffectsSize;
        private int stateSize;
        private float y1;
        private float y2;

        public AnimatedStatusView(Context context, int i, int i2) {
            super(context);
            this.animations = new ArrayList();
            this.stateSize = i;
            this.effectsSize = i2;
            this.renderedEffectsSize = i2;
        }

        private void detach() {
            if (!this.animations.isEmpty()) {
                Iterator it = this.animations.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (next instanceof ImageReceiver) {
                        ((ImageReceiver) next).onDetachedFromWindow();
                    } else if (next instanceof AnimatedEmojiEffect) {
                        ((AnimatedEmojiEffect) next).removeView(this);
                    }
                }
            }
            this.animations.clear();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void animateChange(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
            AnimatedEmojiEffect animatedEmojiEffect;
            String findAnimatedEmojiEmoticon;
            if (visibleReaction == null) {
                detach();
                return;
            }
            TLRPC$Document tLRPC$Document = null;
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction2 = visibleReaction.emojicon != null ? MediaDataController.getInstance(UserConfig.selectedAccount).getReactionsMap().get(visibleReaction.emojicon) : null;
            if (tLRPC$TL_availableReaction2 == null) {
                TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(UserConfig.selectedAccount, visibleReaction.documentId);
                if (findDocument != null && (findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(findDocument, null)) != null) {
                    tLRPC$TL_availableReaction2 = MediaDataController.getInstance(UserConfig.selectedAccount).getReactionsMap().get(findAnimatedEmojiEmoticon);
                }
                tLRPC$TL_availableReaction = tLRPC$TL_availableReaction2;
                tLRPC$Document = findDocument;
            } else {
                tLRPC$TL_availableReaction = tLRPC$TL_availableReaction2;
            }
            if (tLRPC$Document != null || tLRPC$TL_availableReaction == null) {
                AnimatedEmojiDrawable make = tLRPC$Document == null ? AnimatedEmojiDrawable.make(2, UserConfig.selectedAccount, visibleReaction.documentId) : AnimatedEmojiDrawable.make(2, UserConfig.selectedAccount, tLRPC$Document);
                if (this.color != null) {
                    make.setColorFilter(new PorterDuffColorFilter(this.color.intValue(), PorterDuff.Mode.MULTIPLY));
                }
                AnimatedEmojiEffect createFrom = AnimatedEmojiEffect.createFrom(make, false, !make.canOverrideColor());
                createFrom.setView(this);
                animatedEmojiEffect = createFrom;
            } else {
                ImageReceiver imageReceiver = new ImageReceiver();
                imageReceiver.setParentView(this);
                int i = this.animationUniq;
                this.animationUniq = i + 1;
                imageReceiver.setUniqKeyPrefix(Integer.toString(i));
                ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation);
                imageReceiver.setImage(forDocument, this.effectsSize + "_" + this.effectsSize + "_nolimit", null, "tgs", tLRPC$TL_availableReaction, 1);
                imageReceiver.setAutoRepeat(0);
                imageReceiver.onAttachedToWindow();
                animatedEmojiEffect = imageReceiver;
            }
            this.animations.add(animatedEmojiEffect);
            invalidate();
        }

        @Override // android.view.View
        public void dispatchDraw(Canvas canvas) {
            int dp = AndroidUtilities.dp(this.renderedEffectsSize);
            int dp2 = AndroidUtilities.dp(this.effectsSize);
            for (int i = 0; i < this.animations.size(); i++) {
                Object obj = this.animations.get(i);
                if (obj instanceof ImageReceiver) {
                    ImageReceiver imageReceiver = (ImageReceiver) obj;
                    float f = dp2;
                    imageReceiver.setImageCoords((getMeasuredWidth() - dp2) / 2.0f, (getMeasuredHeight() - dp2) / 2.0f, f, f);
                    imageReceiver.draw(canvas);
                } else if (obj instanceof AnimatedEmojiEffect) {
                    AnimatedEmojiEffect animatedEmojiEffect = (AnimatedEmojiEffect) obj;
                    animatedEmojiEffect.setBounds((int) ((getMeasuredWidth() - dp) / 2.0f), (int) ((getMeasuredHeight() - dp) / 2.0f), (int) ((getMeasuredWidth() + dp) / 2.0f), (int) ((getMeasuredHeight() + dp) / 2.0f));
                    animatedEmojiEffect.draw(canvas);
                    if (animatedEmojiEffect.isDone()) {
                        animatedEmojiEffect.removeView(this);
                        this.animations.remove(animatedEmojiEffect);
                    }
                }
            }
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            detach();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(Math.max(this.renderedEffectsSize, Math.max(this.stateSize, this.effectsSize))), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(Math.max(this.renderedEffectsSize, Math.max(this.stateSize, this.effectsSize))), 1073741824));
        }

        public void setColor(int i) {
            this.color = Integer.valueOf(i);
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
            PorterDuffColorFilter porterDuffColorFilter2 = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
            for (int i2 = 0; i2 < this.animations.size(); i2++) {
                Object obj = this.animations.get(i2);
                if (obj instanceof ImageReceiver) {
                    ((ImageReceiver) obj).setColorFilter(porterDuffColorFilter);
                } else if (obj instanceof AnimatedEmojiEffect) {
                    ((AnimatedEmojiEffect) obj).animatedEmojiDrawable.setColorFilter(porterDuffColorFilter2);
                }
            }
        }

        public void translate(float f, float f2) {
            setTranslationX(f - (getMeasuredWidth() / 2.0f));
            float measuredHeight = f2 - (getMeasuredHeight() / 2.0f);
            this.y1 = measuredHeight;
            setTranslationY(measuredHeight + this.y2);
        }

        public void translateY2(float f) {
            float f2 = this.y1;
            this.y2 = f;
            setTranslationY(f2 + f);
        }
    }

    public DrawerProfileCell(Context context, final DrawerLayoutContainer drawerLayoutContainer) {
        super(context);
        this.updateRightDrawable = true;
        this.srcRect = new Rect();
        this.destRect = new Rect();
        this.paint = new Paint();
        this.backPaint = new Paint(1);
        this.lastAccount = -1;
        this.lastUser = null;
        this.premiumStar = null;
        ImageView imageView = new ImageView(context);
        this.shadowView = imageView;
        imageView.setVisibility(4);
        this.shadowView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.shadowView.setImageResource(R.drawable.bottom_shadow);
        addView(this.shadowView, LayoutHelper.createFrame(-1, 70, 83));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(32.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(64, 64.0f, 83, 16.0f, 0.0f, 0.0f, 67.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context) { // from class: org.telegram.ui.Cells.DrawerProfileCell.1
            @Override // android.view.View
            public void invalidate() {
                if (HwEmojis.grab(this)) {
                    return;
                }
                super.invalidate();
            }

            @Override // android.view.View
            public void invalidate(int i, int i2, int i3, int i4) {
                if (HwEmojis.grab(this)) {
                    return;
                }
                super.invalidate(i, i2, i3, i4);
            }

            @Override // android.view.View
            public void invalidate(Rect rect) {
                if (HwEmojis.grab(this)) {
                    return;
                }
                super.invalidate(rect);
            }

            @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View, android.graphics.drawable.Drawable.Callback
            public void invalidateDrawable(Drawable drawable) {
                if (HwEmojis.grab(this)) {
                    return;
                }
                super.invalidateDrawable(drawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (DrawerProfileCell.this.updateRightDrawable) {
                    DrawerProfileCell.this.updateRightDrawable = false;
                    DrawerProfileCell drawerProfileCell = DrawerProfileCell.this;
                    Rect rect = AndroidUtilities.rectTmp2;
                    drawerProfileCell.getEmojiStatusLocation(rect);
                    DrawerProfileCell.this.animatedStatus.translate(rect.centerX(), rect.centerY());
                }
            }
        };
        this.nameTextView = simpleTextView;
        simpleTextView.setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.Cells.DrawerProfileCell$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DrawerProfileCell.this.lambda$new$0(view);
            }
        });
        this.nameTextView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        this.nameTextView.setTextSize(15);
        this.nameTextView.setTypeface(AndroidUtilities.bold());
        this.nameTextView.setGravity(19);
        this.nameTextView.setEllipsizeByGradient(true);
        this.nameTextView.setRightDrawableOutside(true);
        addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0f, 83, 16.0f, 0.0f, 52.0f, 28.0f));
        TextView textView = new TextView(context);
        this.phoneTextView = textView;
        textView.setTextSize(1, 13.0f);
        this.phoneTextView.setLines(1);
        this.phoneTextView.setMaxLines(1);
        this.phoneTextView.setSingleLine(true);
        this.phoneTextView.setGravity(3);
        addView(this.phoneTextView, LayoutHelper.createFrame(-1, -2.0f, 83, 16.0f, 0.0f, 52.0f, 9.0f));
        ImageView imageView2 = new ImageView(context);
        this.arrowView = imageView2;
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        imageView2.setScaleType(scaleType);
        this.arrowView.setImageResource(R.drawable.msg_expand);
        addView(this.arrowView, LayoutHelper.createFrame(59, 59, 85));
        setArrowState(false);
        boolean z = sunDrawable == null;
        if (z) {
            int i = R.raw.sun;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            sunDrawable = rLottieDrawable;
            rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
            if (Theme.isCurrentThemeDay()) {
                sunDrawable.setCustomEndFrame(0);
                sunDrawable.setCurrentFrame(0);
            } else {
                sunDrawable.setCurrentFrame(35);
                sunDrawable.setCustomEndFrame(36);
            }
        }
        RLottieImageView rLottieImageView = new RLottieImageView(context) { // from class: org.telegram.ui.Cells.DrawerProfileCell.2
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setText(LocaleController.getString(Theme.isCurrentThemeDark() ? R.string.AccDescrSwitchToDayTheme : R.string.AccDescrSwitchToNightTheme));
            }
        };
        this.darkThemeView = rLottieImageView;
        rLottieImageView.setFocusable(true);
        this.darkThemeView.setBackground(Theme.createCircleSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 0, 0));
        sunDrawable.beginApplyLayerColors();
        int i2 = Theme.key_chats_menuName;
        int color = Theme.getColor(i2);
        sunDrawable.setLayerColor("Sunny.**", color);
        sunDrawable.setLayerColor("Path 6.**", color);
        sunDrawable.setLayerColor("Path.**", color);
        sunDrawable.setLayerColor("Path 5.**", color);
        sunDrawable.commitApplyLayerColors();
        this.darkThemeView.setScaleType(scaleType);
        this.darkThemeView.setAnimation(sunDrawable);
        if (Build.VERSION.SDK_INT >= 21) {
            RLottieImageView rLottieImageView2 = this.darkThemeView;
            int color2 = Theme.getColor(Theme.key_listSelector);
            this.darkThemeBackgroundColor = color2;
            rLottieImageView2.setBackgroundDrawable(Theme.createSelectorDrawable(color2, 1, AndroidUtilities.dp(17.0f)));
            Theme.setRippleDrawableForceSoftware(Theme$$ExternalSyntheticApiModelOutline2.m(this.darkThemeView.getBackground()));
        }
        if (!z && sunDrawable.getCustomEndFrame() != sunDrawable.getCurrentFrame()) {
            this.darkThemeView.playAnimation();
        }
        this.darkThemeView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Cells.DrawerProfileCell$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DrawerProfileCell.this.lambda$new$2(drawerLayoutContainer, view);
            }
        });
        this.darkThemeView.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Cells.DrawerProfileCell$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$new$3;
                lambda$new$3 = DrawerProfileCell.lambda$new$3(DrawerLayoutContainer.this, view);
                return lambda$new$3;
            }
        });
        addView(this.darkThemeView, LayoutHelper.createFrame(48, 48.0f, 85, 0.0f, 0.0f, 6.0f, 90.0f));
        if (Theme.getEventType() == 0) {
            SnowflakesEffect snowflakesEffect = new SnowflakesEffect(0);
            this.snowflakesEffect = snowflakesEffect;
            snowflakesEffect.setColorKey(i2);
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(20.0f));
        this.status = swapAnimatedEmojiDrawable;
        this.nameTextView.setRightDrawable(swapAnimatedEmojiDrawable);
        AnimatedStatusView animatedStatusView = new AnimatedStatusView(context, 20, 60);
        this.animatedStatus = animatedStatusView;
        addView(animatedStatusView, LayoutHelper.createFrame(20, 20, 51));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        TLRPC$User tLRPC$User = this.lastUser;
        if (tLRPC$User == null || !tLRPC$User.premium) {
            return;
        }
        onPremiumClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(DrawerLayoutContainer drawerLayoutContainer) {
        drawerLayoutContainer.closeDrawer(false);
        drawerLayoutContainer.presentFragment(new ThemeActivity(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$2(final DrawerLayoutContainer drawerLayoutContainer, View view) {
        boolean equals;
        Theme.ThemeInfo theme;
        if (switchingTheme) {
            return;
        }
        switchingTheme = true;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        String str = "Blue";
        String string = sharedPreferences.getString("lastDayTheme", "Blue");
        string = (Theme.getTheme(string) == null || Theme.getTheme(string).isDark()) ? "Blue" : "Blue";
        String str2 = "Dark Blue";
        String string2 = sharedPreferences.getString("lastDarkTheme", "Dark Blue");
        string2 = (Theme.getTheme(string2) == null || !Theme.getTheme(string2).isDark()) ? "Dark Blue" : "Dark Blue";
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        if (!string.equals(string2)) {
            str2 = string2;
        } else if (activeTheme.isDark() || string.equals("Dark Blue") || string.equals("Night")) {
            str2 = string2;
            equals = str.equals(activeTheme.getKey());
            if (equals) {
                theme = Theme.getTheme(str);
                sunDrawable.setCustomEndFrame(0);
            } else {
                theme = Theme.getTheme(str2);
                sunDrawable.setCustomEndFrame(36);
            }
            this.darkThemeView.playAnimation();
            switchTheme(theme, equals);
            if (drawerLayoutContainer == null) {
                Theme.turnOffAutoNight(drawerLayoutContainer.getParent() instanceof FrameLayout ? (FrameLayout) drawerLayoutContainer.getParent() : null, new Runnable() { // from class: org.telegram.ui.Cells.DrawerProfileCell$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        DrawerProfileCell.lambda$new$1(DrawerLayoutContainer.this);
                    }
                });
                return;
            }
            return;
        }
        str = string;
        equals = str.equals(activeTheme.getKey());
        if (equals) {
        }
        this.darkThemeView.playAnimation();
        switchTheme(theme, equals);
        if (drawerLayoutContainer == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$3(DrawerLayoutContainer drawerLayoutContainer, View view) {
        if (drawerLayoutContainer != null) {
            drawerLayoutContainer.presentFragment(new ThemeActivity(0));
            return true;
        }
        return false;
    }

    private void setArrowState(boolean z) {
        float f = this.accountsShown ? 180.0f : 0.0f;
        if (z) {
            this.arrowView.animate().rotation(f).setDuration(220L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
        } else {
            this.arrowView.animate().cancel();
            this.arrowView.setRotation(f);
        }
        this.arrowView.setContentDescription(LocaleController.getString(this.accountsShown ? R.string.AccDescrHideAccounts : R.string.AccDescrShowAccounts));
    }

    private void switchTheme(Theme.ThemeInfo themeInfo, boolean z) {
        this.darkThemeView.getLocationInWindow(r1);
        int[] iArr = {iArr[0] + (this.darkThemeView.getMeasuredWidth() / 2), iArr[1] + (this.darkThemeView.getMeasuredHeight() / 2)};
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, iArr, -1, Boolean.valueOf(z), this.darkThemeView);
    }

    public void animateStateChange(long j) {
        this.animatedStatus.animateChange(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(Long.valueOf(j)));
        this.updateRightDrawable = true;
    }

    public Integer applyBackground(boolean z) {
        Integer num = (Integer) getTag();
        int i = Theme.key_chats_menuTopBackground;
        if (!Theme.hasThemeKey(i) || Theme.getColor(i) == 0) {
            i = Theme.key_chats_menuTopBackgroundCats;
        }
        if (z || num == null || i != num.intValue()) {
            setBackgroundColor(Theme.getColor(i));
            setTag(Integer.valueOf(i));
        }
        return Integer.valueOf(i);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$User currentUser;
        if (i == NotificationCenter.emojiLoaded) {
            this.nameTextView.invalidate();
            return;
        }
        if (i == NotificationCenter.userEmojiStatusUpdated) {
            currentUser = (TLRPC$User) objArr[0];
        } else {
            if (i != NotificationCenter.currentUserPremiumStatusChanged) {
                if (i != NotificationCenter.updateInterfaces) {
                    return;
                }
                int intValue = ((Integer) objArr[0]).intValue();
                if ((MessagesController.UPDATE_MASK_NAME & intValue) == 0 && (MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_STATUS & intValue) == 0 && (MessagesController.UPDATE_MASK_PHONE & intValue) == 0 && (intValue & MessagesController.UPDATE_MASK_EMOJI_STATUS) == 0) {
                    return;
                }
            }
            currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        }
        setUser(currentUser, this.accountsShown);
    }

    public AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable getEmojiStatusDrawable() {
        return this.status;
    }

    public View getEmojiStatusDrawableParent() {
        return this.nameTextView;
    }

    public void getEmojiStatusLocation(Rect rect) {
        if (this.nameTextView.getRightDrawable() == null) {
            rect.set(this.nameTextView.getWidth() - 1, (this.nameTextView.getHeight() / 2) - 1, this.nameTextView.getWidth() + 1, (this.nameTextView.getHeight() / 2) + 1);
            return;
        }
        rect.set(this.nameTextView.getRightDrawable().getBounds());
        rect.offset((int) this.nameTextView.getX(), (int) this.nameTextView.getY());
        this.animatedStatus.translate(rect.centerX(), rect.centerY());
    }

    public boolean hasAvatar() {
        return this.avatarImageView.getImageReceiver().hasNotThumb();
    }

    public boolean isInAvatar(float f, float f2) {
        return f >= ((float) this.avatarImageView.getLeft()) && f <= ((float) this.avatarImageView.getRight()) && f2 >= ((float) this.avatarImageView.getTop()) && f2 <= ((float) this.avatarImageView.getBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.status.attach();
        updateColors();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.status.detach();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        }
        int i2 = this.lastAccount;
        if (i2 >= 0) {
            NotificationCenter.getInstance(i2).removeObserver(this, NotificationCenter.userEmojiStatusUpdated);
            NotificationCenter.getInstance(this.lastAccount).removeObserver(this, NotificationCenter.updateInterfaces);
            this.lastAccount = -1;
        }
        if (this.nameTextView.getRightDrawable() instanceof AnimatedEmojiDrawable.WrapSizeDrawable) {
            Drawable drawable = ((AnimatedEmojiDrawable.WrapSizeDrawable) this.nameTextView.getRightDrawable()).getDrawable();
            if (drawable instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawable).removeView(this.nameTextView);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int color;
        boolean z;
        Integer num;
        int color2;
        Integer num2;
        boolean z2;
        float clamp;
        SnowflakesEffect snowflakesEffect;
        Drawable cachedWallpaper = Theme.getCachedWallpaper();
        boolean z3 = (applyBackground(false).intValue() == Theme.key_chats_menuTopBackground || !Theme.isCustomTheme() || Theme.isPatternWallpaper() || cachedWallpaper == null || (cachedWallpaper instanceof ColorDrawable) || (cachedWallpaper instanceof GradientDrawable)) ? false : true;
        if (!z3) {
            int i = Theme.key_chats_menuTopShadowCats;
            if (Theme.hasThemeKey(i)) {
                color = Theme.getColor(i);
                z = true;
                num = this.currentColor;
                if (num != null || num.intValue() != color) {
                    this.currentColor = Integer.valueOf(color);
                    this.shadowView.getDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                }
                int i2 = Theme.key_chats_menuName;
                color2 = Theme.getColor(i2);
                num2 = this.currentMoonColor;
                if (num2 != null || num2.intValue() != color2) {
                    this.currentMoonColor = Integer.valueOf(color2);
                    sunDrawable.beginApplyLayerColors();
                    sunDrawable.setLayerColor("Sunny.**", this.currentMoonColor.intValue());
                    sunDrawable.setLayerColor("Path 6.**", this.currentMoonColor.intValue());
                    sunDrawable.setLayerColor("Path.**", this.currentMoonColor.intValue());
                    sunDrawable.setLayerColor("Path 5.**", this.currentMoonColor.intValue());
                    sunDrawable.commitApplyLayerColors();
                }
                this.nameTextView.setTextColor(Theme.getColor(i2));
                if (z3) {
                    int i3 = z ? 0 : 4;
                    if (this.shadowView.getVisibility() != i3) {
                        this.shadowView.setVisibility(i3);
                    }
                    this.phoneTextView.setTextColor(Theme.getColor(Theme.key_chats_menuPhoneCats));
                    super.onDraw(canvas);
                } else {
                    this.phoneTextView.setTextColor(Theme.getColor(Theme.key_chats_menuPhone));
                    if (this.shadowView.getVisibility() != 0) {
                        this.shadowView.setVisibility(0);
                    }
                    if ((cachedWallpaper instanceof ColorDrawable) || (cachedWallpaper instanceof GradientDrawable)) {
                        cachedWallpaper.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        cachedWallpaper.draw(canvas);
                    } else {
                        if (cachedWallpaper instanceof BitmapDrawable) {
                            Bitmap bitmap = ((BitmapDrawable) cachedWallpaper).getBitmap();
                            float max = Math.max(getMeasuredWidth() / bitmap.getWidth(), getMeasuredHeight() / bitmap.getHeight());
                            int measuredWidth = (int) (getMeasuredWidth() / max);
                            int measuredHeight = (int) (getMeasuredHeight() / max);
                            int width = (bitmap.getWidth() - measuredWidth) / 2;
                            int height = (bitmap.getHeight() - measuredHeight) / 2;
                            this.srcRect.set(width, height, measuredWidth + width, measuredHeight + height);
                            this.destRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
                            try {
                                canvas.drawBitmap(bitmap, this.srcRect, this.destRect, this.paint);
                            } catch (Throwable th) {
                                FileLog.e(th);
                            }
                            Theme.getServiceMessageColor();
                        }
                        z2 = this.drawPremium;
                        if (z2) {
                            float f = this.drawPremiumProgress;
                            if (f != 1.0f) {
                                this.drawPremiumProgress = f + 0.07272727f;
                                clamp = Utilities.clamp(this.drawPremiumProgress, 1.0f, 0.0f);
                                this.drawPremiumProgress = clamp;
                                if (clamp != 0.0f) {
                                    if (this.gradientTools == null) {
                                        PremiumGradient.PremiumGradientTools premiumGradientTools = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradientBottomSheet1, Theme.key_premiumGradientBottomSheet2, Theme.key_premiumGradientBottomSheet3, -1);
                                        this.gradientTools = premiumGradientTools;
                                        premiumGradientTools.x1 = 0.0f;
                                        premiumGradientTools.y1 = 1.1f;
                                        premiumGradientTools.x2 = 1.5f;
                                        premiumGradientTools.y2 = -0.2f;
                                        premiumGradientTools.exactly = true;
                                    }
                                    this.gradientTools.gradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), 0.0f, 0.0f);
                                    this.gradientTools.paint.setAlpha((int) (this.drawPremiumProgress * 255.0f));
                                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.gradientTools.paint);
                                    StarParticlesView.Drawable drawable = this.starParticlesDrawable;
                                    if (drawable != null) {
                                        drawable.onDraw(canvas, this.drawPremiumProgress);
                                    }
                                    invalidate();
                                }
                                snowflakesEffect = this.snowflakesEffect;
                                if (snowflakesEffect == null) {
                                    snowflakesEffect.onDraw(this, canvas);
                                    return;
                                }
                                return;
                            }
                        }
                        if (!z2) {
                            float f2 = this.drawPremiumProgress;
                            if (f2 != 0.0f) {
                                this.drawPremiumProgress = f2 - 0.07272727f;
                            }
                        }
                        clamp = Utilities.clamp(this.drawPremiumProgress, 1.0f, 0.0f);
                        this.drawPremiumProgress = clamp;
                        if (clamp != 0.0f) {
                        }
                        snowflakesEffect = this.snowflakesEffect;
                        if (snowflakesEffect == null) {
                        }
                    }
                }
                Theme.getColor(Theme.key_listSelector);
                z2 = this.drawPremium;
                if (z2) {
                }
                if (!z2) {
                }
                clamp = Utilities.clamp(this.drawPremiumProgress, 1.0f, 0.0f);
                this.drawPremiumProgress = clamp;
                if (clamp != 0.0f) {
                }
                snowflakesEffect = this.snowflakesEffect;
                if (snowflakesEffect == null) {
                }
            }
        }
        int i4 = Theme.key_chats_menuTopShadow;
        color = Theme.hasThemeKey(i4) ? Theme.getColor(i4) : Theme.getServiceMessageColor() | (-16777216);
        z = false;
        num = this.currentColor;
        if (num != null) {
        }
        this.currentColor = Integer.valueOf(color);
        this.shadowView.getDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        int i22 = Theme.key_chats_menuName;
        color2 = Theme.getColor(i22);
        num2 = this.currentMoonColor;
        if (num2 != null) {
        }
        this.currentMoonColor = Integer.valueOf(color2);
        sunDrawable.beginApplyLayerColors();
        sunDrawable.setLayerColor("Sunny.**", this.currentMoonColor.intValue());
        sunDrawable.setLayerColor("Path 6.**", this.currentMoonColor.intValue());
        sunDrawable.setLayerColor("Path.**", this.currentMoonColor.intValue());
        sunDrawable.setLayerColor("Path 5.**", this.currentMoonColor.intValue());
        sunDrawable.commitApplyLayerColors();
        this.nameTextView.setTextColor(Theme.getColor(i22));
        if (z3) {
        }
        Theme.getColor(Theme.key_listSelector);
        z2 = this.drawPremium;
        if (z2) {
        }
        if (!z2) {
        }
        clamp = Utilities.clamp(this.drawPremiumProgress, 1.0f, 0.0f);
        this.drawPremiumProgress = clamp;
        if (clamp != 0.0f) {
        }
        snowflakesEffect = this.snowflakesEffect;
        if (snowflakesEffect == null) {
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.drawPremium) {
            if (this.starParticlesDrawable == null) {
                StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(15);
                this.starParticlesDrawable = drawable;
                drawable.init();
                StarParticlesView.Drawable drawable2 = this.starParticlesDrawable;
                drawable2.speedScale = 0.8f;
                drawable2.minLifeTime = 3000L;
            }
            this.starParticlesDrawable.rect.set(this.avatarImageView.getLeft(), this.avatarImageView.getTop(), this.avatarImageView.getRight(), this.avatarImageView.getBottom());
            this.starParticlesDrawable.rect.inset(-AndroidUtilities.dp(20.0f), -AndroidUtilities.dp(20.0f));
            this.starParticlesDrawable.resetPositions();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f) + AndroidUtilities.statusBarHeight, 1073741824));
            return;
        }
        try {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f), 1073741824));
        } catch (Exception e) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(148.0f));
            FileLog.e(e);
        }
    }

    protected abstract void onPremiumClick();

    public void setAccountsShown(boolean z, boolean z2) {
        if (this.accountsShown == z) {
            return;
        }
        this.accountsShown = z;
        setArrowState(z2);
    }

    public void setUser(TLRPC$User tLRPC$User, boolean z) {
        Drawable drawable;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        int i = UserConfig.selectedAccount;
        int i2 = this.lastAccount;
        if (i != i2) {
            if (i2 >= 0) {
                NotificationCenter.getInstance(i2).removeObserver(this, NotificationCenter.userEmojiStatusUpdated);
                NotificationCenter.getInstance(this.lastAccount).removeObserver(this, NotificationCenter.updateInterfaces);
            }
            this.lastAccount = i;
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.userEmojiStatusUpdated);
            this.lastAccount = i;
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.updateInterfaces);
        }
        this.lastUser = tLRPC$User;
        if (tLRPC$User == null) {
            return;
        }
        this.accountsShown = z;
        setArrowState(false);
        CharSequence userName = UserObject.getUserName(tLRPC$User);
        try {
            userName = Emoji.replaceEmoji(userName, this.nameTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(22.0f), false);
        } catch (Exception unused) {
        }
        this.drawPremium = false;
        this.nameTextView.setText(userName);
        Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(tLRPC$User);
        if (emojiStatusDocumentId != null) {
            this.animatedStatus.animate().alpha(1.0f).setDuration(200L).start();
            this.nameTextView.setDrawablePadding(AndroidUtilities.dp(4.0f));
            this.status.set(emojiStatusDocumentId.longValue(), true);
        } else {
            if (tLRPC$User.premium) {
                this.animatedStatus.animate().alpha(1.0f).setDuration(200L).start();
                this.nameTextView.setDrawablePadding(AndroidUtilities.dp(4.0f));
                if (this.premiumStar == null) {
                    this.premiumStar = getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                }
                this.premiumStar.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_menuPhoneCats), PorterDuff.Mode.MULTIPLY));
                swapAnimatedEmojiDrawable = this.status;
                drawable = this.premiumStar;
            } else {
                drawable = null;
                this.animatedStatus.animateChange(null);
                this.animatedStatus.animate().alpha(0.0f).setDuration(200L).start();
                swapAnimatedEmojiDrawable = this.status;
            }
            swapAnimatedEmojiDrawable.set(drawable, true);
        }
        this.animatedStatus.setColor(Theme.getColor(Theme.isCurrentThemeDark() ? Theme.key_chats_verifiedBackground : Theme.key_chats_menuPhoneCats));
        this.status.setColor(Integer.valueOf(Theme.getColor(Theme.isCurrentThemeDark() ? Theme.key_chats_verifiedBackground : Theme.key_chats_menuPhoneCats)));
        TextView textView = this.phoneTextView;
        PhoneFormat phoneFormat = PhoneFormat.getInstance();
        textView.setText(phoneFormat.format("+" + tLRPC$User.phone));
        AvatarDrawable avatarDrawable = new AvatarDrawable(tLRPC$User);
        avatarDrawable.setColor(Theme.getColor(Theme.key_avatar_backgroundInProfileBlue));
        this.avatarImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
        applyBackground(true);
        this.updateRightDrawable = true;
    }

    public void updateColors() {
        SnowflakesEffect snowflakesEffect = this.snowflakesEffect;
        if (snowflakesEffect != null) {
            snowflakesEffect.updateColors();
        }
        AnimatedStatusView animatedStatusView = this.animatedStatus;
        if (animatedStatusView != null) {
            animatedStatusView.setColor(Theme.getColor(Theme.isCurrentThemeDark() ? Theme.key_chats_verifiedBackground : Theme.key_chats_menuPhoneCats));
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.status;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.setColor(Integer.valueOf(Theme.getColor(Theme.isCurrentThemeDark() ? Theme.key_chats_verifiedBackground : Theme.key_chats_menuPhoneCats)));
        }
    }

    public void updateSunDrawable(boolean z) {
        RLottieDrawable rLottieDrawable = sunDrawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.setCustomEndFrame(z ? 36 : 0);
        }
        RLottieImageView rLottieImageView = this.darkThemeView;
        if (rLottieImageView != null) {
            rLottieImageView.playAnimation();
        }
    }
}
