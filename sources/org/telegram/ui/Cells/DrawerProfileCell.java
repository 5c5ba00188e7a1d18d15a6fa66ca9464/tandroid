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
import android.graphics.drawable.RippleDrawable;
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
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.SnowflakesEffect;
import org.telegram.ui.ThemeActivity;
/* loaded from: classes3.dex */
public class DrawerProfileCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static RLottieDrawable sunDrawable;
    public static boolean switchingTheme;
    private boolean accountsShown;
    private AnimatedStatusView animatedStatus;
    private ImageView arrowView;
    private BackupImageView avatarImageView;
    private Integer currentColor;
    private Integer currentMoonColor;
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

    protected void onPremiumClick() {
    }

    public DrawerProfileCell(Context context, final DrawerLayoutContainer drawerLayoutContainer) {
        super(context);
        this.updateRightDrawable = true;
        this.srcRect = new Rect();
        this.destRect = new Rect();
        this.paint = new Paint();
        new Paint(1);
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
            @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
            protected void onDraw(Canvas canvas) {
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
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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
        imageView2.setScaleType(ImageView.ScaleType.CENTER);
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
        RLottieImageView rLottieImageView = new RLottieImageView(this, context) { // from class: org.telegram.ui.Cells.DrawerProfileCell.2
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                if (Theme.isCurrentThemeDark()) {
                    accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToDayTheme", R.string.AccDescrSwitchToDayTheme));
                } else {
                    accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToNightTheme", R.string.AccDescrSwitchToNightTheme));
                }
            }
        };
        this.darkThemeView = rLottieImageView;
        rLottieImageView.setFocusable(true);
        this.darkThemeView.setBackground(Theme.createCircleSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0, 0));
        sunDrawable.beginApplyLayerColors();
        int color = Theme.getColor("chats_menuName");
        sunDrawable.setLayerColor("Sunny.**", color);
        sunDrawable.setLayerColor("Path 6.**", color);
        sunDrawable.setLayerColor("Path.**", color);
        sunDrawable.setLayerColor("Path 5.**", color);
        sunDrawable.commitApplyLayerColors();
        this.darkThemeView.setScaleType(ImageView.ScaleType.CENTER);
        this.darkThemeView.setAnimation(sunDrawable);
        if (Build.VERSION.SDK_INT >= 21) {
            this.darkThemeView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 1, AndroidUtilities.dp(17.0f)));
            Theme.setRippleDrawableForceSoftware((RippleDrawable) this.darkThemeView.getBackground());
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
            snowflakesEffect.setColorKey("chats_menuName");
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
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
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
            if (!equals) {
                theme = Theme.getTheme(str2);
                sunDrawable.setCustomEndFrame(36);
            } else {
                theme = Theme.getTheme(str);
                sunDrawable.setCustomEndFrame(0);
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
        if (!equals) {
        }
        this.darkThemeView.playAnimation();
        switchTheme(theme, equals);
        if (drawerLayoutContainer == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(DrawerLayoutContainer drawerLayoutContainer) {
        drawerLayoutContainer.closeDrawer(false);
        drawerLayoutContainer.presentFragment(new ThemeActivity(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$3(DrawerLayoutContainer drawerLayoutContainer, View view) {
        if (drawerLayoutContainer != null) {
            drawerLayoutContainer.presentFragment(new ThemeActivity(0));
            return true;
        }
        return false;
    }

    /* loaded from: classes3.dex */
    public static class AnimatedStatusView extends View {
        private int animationUniq;
        private ArrayList<Object> animations;
        private Integer color;
        private int effectsSize;
        private int renderedEffectsSize;
        private int stateSize;
        private float y1;
        private float y2;

        public AnimatedStatusView(Context context, int i, int i2) {
            super(context);
            this.animations = new ArrayList<>();
            this.stateSize = i;
            this.effectsSize = i2;
            this.renderedEffectsSize = i2;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(Math.max(this.renderedEffectsSize, Math.max(this.stateSize, this.effectsSize))), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(Math.max(this.renderedEffectsSize, Math.max(this.stateSize, this.effectsSize))), 1073741824));
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
                    if (animatedEmojiEffect.done()) {
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

        private void detach() {
            if (!this.animations.isEmpty()) {
                Iterator<Object> it = this.animations.iterator();
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

        public void animateChange(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
            AnimatedEmojiDrawable make;
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
            if (tLRPC$Document == null && tLRPC$TL_availableReaction != null) {
                ImageReceiver imageReceiver = new ImageReceiver();
                imageReceiver.setParentView(this);
                int i = this.animationUniq;
                this.animationUniq = i + 1;
                imageReceiver.setUniqKeyPrefix(Integer.toString(i));
                ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation);
                imageReceiver.setImage(forDocument, this.effectsSize + "_" + this.effectsSize + "_nolimit", null, "tgs", tLRPC$TL_availableReaction, 1);
                imageReceiver.setAutoRepeat(0);
                imageReceiver.onAttachedToWindow();
                this.animations.add(imageReceiver);
                invalidate();
                return;
            }
            if (tLRPC$Document == null) {
                make = AnimatedEmojiDrawable.make(2, UserConfig.selectedAccount, visibleReaction.documentId);
            } else {
                make = AnimatedEmojiDrawable.make(2, UserConfig.selectedAccount, tLRPC$Document);
            }
            if (this.color != null) {
                make.setColorFilter(new PorterDuffColorFilter(this.color.intValue(), PorterDuff.Mode.MULTIPLY));
            }
            AnimatedEmojiEffect createFrom = AnimatedEmojiEffect.createFrom(make, false, !make.canOverrideColor());
            createFrom.setView(this);
            this.animations.add(createFrom);
            invalidate();
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
    }

    public void animateStateChange(long j) {
        this.animatedStatus.animateChange(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(Long.valueOf(j)));
        this.updateRightDrawable = true;
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

    private void switchTheme(Theme.ThemeInfo themeInfo, boolean z) {
        this.darkThemeView.getLocationInWindow(r1);
        int[] iArr = {iArr[0] + (this.darkThemeView.getMeasuredWidth() / 2), iArr[1] + (this.darkThemeView.getMeasuredHeight() / 2)};
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, iArr, -1, Boolean.valueOf(z), this.darkThemeView);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateColors();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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

    /* JADX WARN: Removed duplicated region for block: B:71:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:84:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int serviceMessageColor;
        boolean z;
        float clamp;
        SnowflakesEffect snowflakesEffect;
        Drawable cachedWallpaper = Theme.getCachedWallpaper();
        boolean z2 = (applyBackground(false).equals("chats_menuTopBackground") || !Theme.isCustomTheme() || Theme.isPatternWallpaper() || cachedWallpaper == null || (cachedWallpaper instanceof ColorDrawable) || (cachedWallpaper instanceof GradientDrawable)) ? false : true;
        if (!z2 && Theme.hasThemeKey("chats_menuTopShadowCats")) {
            serviceMessageColor = Theme.getColor("chats_menuTopShadowCats");
            z = true;
        } else {
            if (Theme.hasThemeKey("chats_menuTopShadow")) {
                serviceMessageColor = Theme.getColor("chats_menuTopShadow");
            } else {
                serviceMessageColor = Theme.getServiceMessageColor() | (-16777216);
            }
            z = false;
        }
        Integer num = this.currentColor;
        if (num == null || num.intValue() != serviceMessageColor) {
            this.currentColor = Integer.valueOf(serviceMessageColor);
            this.shadowView.getDrawable().setColorFilter(new PorterDuffColorFilter(serviceMessageColor, PorterDuff.Mode.MULTIPLY));
        }
        int color = Theme.getColor("chats_menuName");
        Integer num2 = this.currentMoonColor;
        if (num2 == null || num2.intValue() != color) {
            this.currentMoonColor = Integer.valueOf(color);
            sunDrawable.beginApplyLayerColors();
            sunDrawable.setLayerColor("Sunny.**", this.currentMoonColor.intValue());
            sunDrawable.setLayerColor("Path 6.**", this.currentMoonColor.intValue());
            sunDrawable.setLayerColor("Path.**", this.currentMoonColor.intValue());
            sunDrawable.setLayerColor("Path 5.**", this.currentMoonColor.intValue());
            sunDrawable.commitApplyLayerColors();
        }
        this.nameTextView.setTextColor(Theme.getColor("chats_menuName"));
        if (z2) {
            this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhone"));
            if (this.shadowView.getVisibility() != 0) {
                this.shadowView.setVisibility(0);
            }
            if ((cachedWallpaper instanceof ColorDrawable) || (cachedWallpaper instanceof GradientDrawable)) {
                cachedWallpaper.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                cachedWallpaper.draw(canvas);
                Theme.getColor("listSelectorSDK21");
            } else if (cachedWallpaper instanceof BitmapDrawable) {
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
        } else {
            int i = z ? 0 : 4;
            if (this.shadowView.getVisibility() != i) {
                this.shadowView.setVisibility(i);
            }
            this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhoneCats"));
            super.onDraw(canvas);
            Theme.getColor("listSelectorSDK21");
        }
        boolean z3 = this.drawPremium;
        if (z3) {
            float f = this.drawPremiumProgress;
            if (f != 1.0f) {
                this.drawPremiumProgress = f + 0.07272727f;
                clamp = Utilities.clamp(this.drawPremiumProgress, 1.0f, 0.0f);
                this.drawPremiumProgress = clamp;
                if (clamp != 0.0f) {
                    if (this.gradientTools == null) {
                        PremiumGradient.PremiumGradientTools premiumGradientTools = new PremiumGradient.PremiumGradientTools("premiumGradientBottomSheet1", "premiumGradientBottomSheet2", "premiumGradientBottomSheet3", null);
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
        if (!z3) {
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

    public boolean isInAvatar(float f, float f2) {
        return f >= ((float) this.avatarImageView.getLeft()) && f <= ((float) this.avatarImageView.getRight()) && f2 >= ((float) this.avatarImageView.getTop()) && f2 <= ((float) this.avatarImageView.getBottom());
    }

    public boolean hasAvatar() {
        return this.avatarImageView.getImageReceiver().hasNotThumb();
    }

    public void setAccountsShown(boolean z, boolean z2) {
        if (this.accountsShown == z) {
            return;
        }
        this.accountsShown = z;
        setArrowState(z2);
    }

    public void setUser(TLRPC$User tLRPC$User, boolean z) {
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
        } else if (tLRPC$User.premium) {
            this.animatedStatus.animate().alpha(1.0f).setDuration(200L).start();
            this.nameTextView.setDrawablePadding(AndroidUtilities.dp(4.0f));
            if (this.premiumStar == null) {
                this.premiumStar = getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
            }
            this.premiumStar.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_menuPhoneCats"), PorterDuff.Mode.MULTIPLY));
            this.status.set(this.premiumStar, true);
        } else {
            this.animatedStatus.animateChange(null);
            this.animatedStatus.animate().alpha(0.0f).setDuration(200L).start();
            this.status.set((Drawable) null, true);
        }
        this.animatedStatus.setColor(Theme.getColor(Theme.isCurrentThemeDark() ? "chats_verifiedBackground" : "chats_menuPhoneCats"));
        this.status.setColor(Integer.valueOf(Theme.getColor(Theme.isCurrentThemeDark() ? "chats_verifiedBackground" : "chats_menuPhoneCats")));
        TextView textView = this.phoneTextView;
        PhoneFormat phoneFormat = PhoneFormat.getInstance();
        textView.setText(phoneFormat.format("+" + tLRPC$User.phone));
        AvatarDrawable avatarDrawable = new AvatarDrawable(tLRPC$User);
        avatarDrawable.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
        this.avatarImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
        applyBackground(true);
        this.updateRightDrawable = true;
    }

    public String applyBackground(boolean z) {
        String str = (String) getTag();
        String str2 = "chats_menuTopBackground";
        str2 = (!Theme.hasThemeKey("chats_menuTopBackground") || Theme.getColor("chats_menuTopBackground") == 0) ? "chats_menuTopBackgroundCats" : "chats_menuTopBackgroundCats";
        if (z || !str2.equals(str)) {
            setBackgroundColor(Theme.getColor(str2));
            setTag(str2);
        }
        return str2;
    }

    public void updateColors() {
        SnowflakesEffect snowflakesEffect = this.snowflakesEffect;
        if (snowflakesEffect != null) {
            snowflakesEffect.updateColors();
        }
        AnimatedStatusView animatedStatusView = this.animatedStatus;
        if (animatedStatusView != null) {
            animatedStatusView.setColor(Theme.getColor(Theme.isCurrentThemeDark() ? "chats_verifiedBackground" : "chats_menuPhoneCats"));
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.status;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.setColor(Integer.valueOf(Theme.getColor(Theme.isCurrentThemeDark() ? "chats_verifiedBackground" : "chats_menuPhoneCats")));
        }
    }

    private void setArrowState(boolean z) {
        int i;
        String str;
        float f = this.accountsShown ? 180.0f : 0.0f;
        if (z) {
            this.arrowView.animate().rotation(f).setDuration(220L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
        } else {
            this.arrowView.animate().cancel();
            this.arrowView.setRotation(f);
        }
        ImageView imageView = this.arrowView;
        if (this.accountsShown) {
            i = R.string.AccDescrHideAccounts;
            str = "AccDescrHideAccounts";
        } else {
            i = R.string.AccDescrShowAccounts;
            str = "AccDescrShowAccounts";
        }
        imageView.setContentDescription(LocaleController.getString(str, i));
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            this.nameTextView.invalidate();
        } else if (i == NotificationCenter.userEmojiStatusUpdated) {
            setUser((TLRPC$User) objArr[0], this.accountsShown);
        } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
            setUser(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser(), this.accountsShown);
        } else if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            if ((MessagesController.UPDATE_MASK_NAME & intValue) == 0 && (MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_STATUS & intValue) == 0 && (MessagesController.UPDATE_MASK_PHONE & intValue) == 0 && (intValue & MessagesController.UPDATE_MASK_EMOJI_STATUS) == 0) {
                return;
            }
            setUser(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser(), this.accountsShown);
        }
    }

    public AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable getEmojiStatusDrawable() {
        return this.status;
    }

    public View getEmojiStatusDrawableParent() {
        return this.nameTextView;
    }
}
