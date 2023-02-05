package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$PrivacyRule;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_exportedContactToken;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowContacts;
import org.telegram.tgnet.TLRPC$TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.SettingsSearchCell;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.HideViewAfterAnimation;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.ThemeSmallPreviewView;
import org.telegram.ui.QrActivity;
/* loaded from: classes3.dex */
public class QrActivity extends BaseFragment {
    private static List<EmojiThemes> cachedThemes;
    private static boolean firstOpen;
    private static final ArrayMap<String, int[]> qrColorsMap;
    private BackupImageView avatarImageView;
    private View backgroundView;
    private long chatId;
    private ImageView closeImageView;
    private MotionBackgroundDrawable currMotionDrawable;
    private EmojiThemes currentTheme;
    private final ArrayMap<String, Bitmap> emojiThemeDarkIcons;
    private Bitmap emojiThemeIcon;
    private final EmojiThemes homeTheme;
    private boolean isCurrentThemeDark;
    private RLottieImageView logoImageView;
    private final Rect logoRect;
    private ValueAnimator patternAlphaAnimator;
    private ValueAnimator patternIntensityAnimator;
    private MotionBackgroundDrawable prevMotionDrawable;
    private int[] prevQrColors;
    private int prevSystemUiVisibility;
    private QrView qrView;
    private final ThemeResourcesProvider resourcesProvider;
    private int selectedPosition;
    private MotionBackgroundDrawable tempMotionDrawable;
    private FrameLayout themeLayout;
    private ThemeListViewController themesViewController;
    private long userId;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public interface OnItemSelectedListener {
        void onItemSelected(EmojiThemes emojiThemes, int i);
    }

    static {
        ArrayMap<String, int[]> arrayMap = new ArrayMap<>();
        qrColorsMap = arrayMap;
        arrayMap.put("🏠d", new int[]{-9324972, -13856649, -6636738, -9915042});
        arrayMap.put("🐥d", new int[]{-12344463, -7684788, -6442695, -8013488});
        arrayMap.put("⛄d", new int[]{-10051073, -10897938, -12469550, -7694337});
        arrayMap.put("💎d", new int[]{-11429643, -11814958, -5408261, -2128185});
        arrayMap.put("👨\u200d🏫d", new int[]{-6637227, -12015466, -13198627, -10631557});
        arrayMap.put("🌷d", new int[]{-1146812, -1991901, -1745517, -3443241});
        arrayMap.put("💜d", new int[]{-1156738, -1876046, -5412366, -28073});
        arrayMap.put("🎄d", new int[]{-1281978, -551386, -1870308, -742870});
        arrayMap.put("🎮d", new int[]{-15092782, -2333964, -1684365, -1269214});
        arrayMap.put("🏠n", new int[]{-15368239, -11899662, -15173939, -13850930});
        arrayMap.put("🐥n", new int[]{-11033320, -14780848, -9594089, -12604587});
        arrayMap.put("⛄n", new int[]{-13930790, -13665098, -14833975, -9732865});
        arrayMap.put("💎n", new int[]{-5089608, -9481473, -14378302, -13337899});
        arrayMap.put("👨\u200d🏫n", new int[]{-14447768, -9199261, -15356801, -15823723});
        arrayMap.put("🌷n", new int[]{-2534316, -2984177, -3258783, -5480504});
        arrayMap.put("💜n", new int[]{-3123030, -2067394, -2599576, -6067757});
        arrayMap.put("🎄n", new int[]{-2725857, -3242459, -3248848, -3569123});
        arrayMap.put("🎮n", new int[]{-3718333, -1278154, -16338695, -6076417});
        firstOpen = true;
    }

    public QrActivity(Bundle bundle) {
        super(bundle);
        this.resourcesProvider = new ThemeResourcesProvider();
        EmojiThemes createHomeQrTheme = EmojiThemes.createHomeQrTheme();
        this.homeTheme = createHomeQrTheme;
        this.logoRect = new Rect();
        this.emojiThemeDarkIcons = new ArrayMap<>();
        this.prevQrColors = null;
        this.currMotionDrawable = new MotionBackgroundDrawable();
        this.currentTheme = createHomeQrTheme;
        this.selectedPosition = -1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.userId = this.arguments.getLong("user_id");
        this.chatId = this.arguments.getLong("chat_id");
        return super.onFragmentCreate();
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x02a4  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        String str;
        String str2;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        AvatarDrawable avatarDrawable;
        boolean z;
        TLRPC$Chat chat;
        ImageLocation forChat;
        String str3;
        Bitmap decodeResource;
        AvatarDrawable avatarDrawable2;
        ImageLocation imageLocation3;
        boolean z2;
        this.isCurrentThemeDark = Theme.getActiveTheme().isDark();
        boolean z3 = false;
        this.actionBar.setAddToContainer(false);
        this.actionBar.setBackground(null);
        this.actionBar.setItemsColor(-1, false);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.QrActivity.1
            private boolean prevIsPortrait;

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                super.dispatchTouchEvent(motionEvent);
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                int size = View.MeasureSpec.getSize(i);
                int size2 = View.MeasureSpec.getSize(i2);
                boolean z4 = size < size2;
                QrActivity.this.avatarImageView.setVisibility(z4 ? 0 : 8);
                super.onMeasure(i, i2);
                if (z4) {
                    QrActivity.this.themeLayout.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                    QrActivity.this.qrView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(260.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(330.0f), 1073741824));
                } else {
                    QrActivity.this.themeLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(256.0f), 1073741824), i2);
                    QrActivity.this.qrView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(260.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(310.0f), 1073741824));
                }
                if (this.prevIsPortrait != z4) {
                    QrActivity.this.qrView.onSizeChanged(QrActivity.this.qrView.getMeasuredWidth(), QrActivity.this.qrView.getMeasuredHeight(), 0, 0);
                }
                this.prevIsPortrait = z4;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z4, int i, int i2, int i3, int i4) {
                boolean z5 = getWidth() < getHeight();
                QrActivity.this.backgroundView.layout(0, 0, getWidth(), getHeight());
                int measuredHeight = QrActivity.this.themeLayout.getVisibility() == 0 ? QrActivity.this.themeLayout.getMeasuredHeight() : 0;
                int width = z5 ? (getWidth() - QrActivity.this.qrView.getMeasuredWidth()) / 2 : ((getWidth() - QrActivity.this.themeLayout.getMeasuredWidth()) - QrActivity.this.qrView.getMeasuredWidth()) / 2;
                int height = z5 ? ((((getHeight() - measuredHeight) - QrActivity.this.qrView.getMeasuredHeight()) - AndroidUtilities.dp(48.0f)) / 2) + AndroidUtilities.dp(52.0f) : (getHeight() - QrActivity.this.qrView.getMeasuredHeight()) / 2;
                QrActivity.this.qrView.layout(width, height, QrActivity.this.qrView.getMeasuredWidth() + width, QrActivity.this.qrView.getMeasuredHeight() + height);
                if (z5) {
                    int width2 = (getWidth() - QrActivity.this.avatarImageView.getMeasuredWidth()) / 2;
                    int dp = height - AndroidUtilities.dp(48.0f);
                    QrActivity.this.avatarImageView.layout(width2, dp, QrActivity.this.avatarImageView.getMeasuredWidth() + width2, QrActivity.this.avatarImageView.getMeasuredHeight() + dp);
                }
                if (QrActivity.this.themeLayout.getVisibility() == 0) {
                    if (z5) {
                        int width3 = (getWidth() - QrActivity.this.themeLayout.getMeasuredWidth()) / 2;
                        QrActivity.this.themeLayout.layout(width3, i4 - measuredHeight, QrActivity.this.themeLayout.getMeasuredWidth() + width3, i4);
                    } else {
                        int height2 = (getHeight() - QrActivity.this.themeLayout.getMeasuredHeight()) / 2;
                        QrActivity.this.themeLayout.layout(i3 - QrActivity.this.themeLayout.getMeasuredWidth(), height2, i3, QrActivity.this.themeLayout.getMeasuredHeight() + height2);
                    }
                }
                QrActivity.this.logoImageView.layout(QrActivity.this.logoRect.left + width, QrActivity.this.logoRect.top + height, width + QrActivity.this.logoRect.right, height + QrActivity.this.logoRect.bottom);
                int dp2 = AndroidUtilities.dp(z5 ? 14.0f : 17.0f);
                int dp3 = AndroidUtilities.statusBarHeight + AndroidUtilities.dp(z5 ? 10.0f : 5.0f);
                QrActivity.this.closeImageView.layout(dp2, dp3, QrActivity.this.closeImageView.getMeasuredWidth() + dp2, QrActivity.this.closeImageView.getMeasuredHeight() + dp3);
            }
        };
        View view = new View(context) { // from class: org.telegram.ui.QrActivity.2
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                canvas.drawColor(QrActivity.this.isCurrentThemeDark ? -15590870 : -6569073);
                if (QrActivity.this.prevMotionDrawable != null) {
                    QrActivity.this.prevMotionDrawable.setBounds(0, 0, getWidth(), getHeight());
                }
                QrActivity.this.currMotionDrawable.setBounds(0, 0, getWidth(), getHeight());
                if (QrActivity.this.prevMotionDrawable != null) {
                    QrActivity.this.prevMotionDrawable.drawBackground(canvas);
                }
                QrActivity.this.currMotionDrawable.drawBackground(canvas);
                if (QrActivity.this.prevMotionDrawable != null) {
                    QrActivity.this.prevMotionDrawable.drawPattern(canvas);
                }
                QrActivity.this.currMotionDrawable.drawPattern(canvas);
                super.onDraw(canvas);
            }
        };
        this.backgroundView = view;
        frameLayout.addView(view);
        if (this.userId != 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.userId));
            if (user != null) {
                str = UserObject.getPublicUsername(user);
                if (str == null) {
                    str2 = UserObject.getUserName(user);
                    if (phoneIsPublic()) {
                        str = user.phone;
                        if (str != null && !str.startsWith("+")) {
                            str = "+" + str;
                        }
                        z2 = true;
                    } else {
                        z2 = false;
                        z = true;
                        avatarDrawable2 = new AvatarDrawable(user);
                        imageLocation3 = ImageLocation.getForUser(user, 1);
                        forChat = ImageLocation.getForUser(user, 0);
                        z3 = z2;
                    }
                } else {
                    str2 = null;
                    z2 = false;
                }
                z = false;
                avatarDrawable2 = new AvatarDrawable(user);
                imageLocation3 = ImageLocation.getForUser(user, 1);
                forChat = ImageLocation.getForUser(user, 0);
                z3 = z2;
            } else {
                forChat = null;
                str = null;
                str2 = null;
                avatarDrawable2 = null;
                imageLocation3 = null;
                z = false;
            }
            avatarDrawable = avatarDrawable2;
            imageLocation2 = imageLocation3;
        } else if (this.chatId != 0 && (chat = getMessagesController().getChat(Long.valueOf(this.chatId))) != null) {
            str = ChatObject.getPublicUsername(chat);
            AvatarDrawable avatarDrawable3 = new AvatarDrawable(chat);
            ImageLocation forChat2 = ImageLocation.getForChat(chat, 1);
            forChat = ImageLocation.getForChat(chat, 0);
            str2 = null;
            avatarDrawable = avatarDrawable3;
            imageLocation2 = forChat2;
            z = false;
        } else {
            str = null;
            str2 = null;
            imageLocation = null;
            imageLocation2 = null;
            avatarDrawable = null;
            z = false;
            QrView qrView = new QrView(context);
            this.qrView = qrView;
            qrView.setColors(-9324972, -13856649, -6636738, -9915042);
            if (str == null) {
                str3 = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + str;
            } else {
                str3 = null;
            }
            QrView qrView2 = this.qrView;
            if (str2 != null) {
                str = str2;
            }
            qrView2.setData(str3, str, z3, z);
            this.qrView.setCenterChangedListener(new QrView.QrCenterChangedListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.QrActivity.QrView.QrCenterChangedListener
                public final void onCenterChanged(int i, int i2, int i3, int i4) {
                    QrActivity.this.lambda$createView$0(i, i2, i3, i4);
                }
            });
            frameLayout.addView(this.qrView);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.logoImageView = rLottieImageView;
            rLottieImageView.setAutoRepeat(true);
            this.logoImageView.setAnimation(R.raw.qr_code_logo_2, 60, 60);
            this.logoImageView.playAnimation();
            frameLayout.addView(this.logoImageView);
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(42.0f));
            this.avatarImageView.setSize(AndroidUtilities.dp(84.0f), AndroidUtilities.dp(84.0f));
            frameLayout.addView(this.avatarImageView, LayoutHelper.createFrame(84, 84, 51));
            this.avatarImageView.setImage(imageLocation, "84_84", imageLocation2, "50_50", avatarDrawable, null, null, 0, null);
            ImageView imageView = new ImageView(context);
            this.closeImageView = imageView;
            imageView.setBackground(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(34.0f), 671088640, 687865855));
            this.closeImageView.setImageResource(R.drawable.ic_ab_back);
            this.closeImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.closeImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    QrActivity.this.lambda$createView$1(view2);
                }
            });
            frameLayout.addView(this.closeImageView, LayoutHelper.createFrame(34, 34.0f));
            this.emojiThemeIcon = Bitmap.createBitmap(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.emojiThemeIcon);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, this.emojiThemeIcon.getWidth(), this.emojiThemeIcon.getHeight());
            Paint paint = new Paint(1);
            paint.setColor(-1);
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), R.drawable.msg_qr_mini), (this.emojiThemeIcon.getWidth() - decodeResource.getWidth()) * 0.5f, (this.emojiThemeIcon.getHeight() - decodeResource.getHeight()) * 0.5f, paint);
            canvas.setBitmap(null);
            ThemeListViewController themeListViewController = new ThemeListViewController(this, getParentActivity().getWindow()) { // from class: org.telegram.ui.QrActivity.3
                @Override // org.telegram.ui.QrActivity.ThemeListViewController
                protected void setDarkTheme(boolean z4) {
                    super.setDarkTheme(z4);
                    QrActivity.this.isCurrentThemeDark = z4;
                    QrActivity qrActivity = QrActivity.this;
                    qrActivity.onItemSelected(qrActivity.currentTheme, QrActivity.this.selectedPosition, false);
                }
            };
            this.themesViewController = themeListViewController;
            this.themeLayout = themeListViewController.rootLayout;
            themeListViewController.onCreate();
            this.themesViewController.setItemSelectedListener(new OnItemSelectedListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda14
                @Override // org.telegram.ui.QrActivity.OnItemSelectedListener
                public final void onItemSelected(EmojiThemes emojiThemes, int i) {
                    QrActivity.this.lambda$createView$2(emojiThemes, i);
                }
            });
            this.themesViewController.titleView.setText(LocaleController.getString("QrCode", R.string.QrCode));
            this.themesViewController.progressView.setViewType(17);
            this.themesViewController.shareButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    QrActivity.this.lambda$createView$3(view2);
                }
            });
            frameLayout.addView(this.themeLayout, LayoutHelper.createFrame(-1, -2, 80));
            this.currMotionDrawable.setIndeterminateAnimation(true);
            this.fragmentView = frameLayout;
            Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.this.lambda$createView$5();
                }
            }, 25L);
            this.fragmentView.postDelayed(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.this.lambda$createView$6();
                }
            }, !firstOpen ? 250L : 0L);
            this.prevSystemUiVisibility = getParentActivity().getWindow().getDecorView().getSystemUiVisibility();
            applyScreenSettings();
            return this.fragmentView;
        }
        imageLocation = forChat;
        QrView qrView3 = new QrView(context);
        this.qrView = qrView3;
        qrView3.setColors(-9324972, -13856649, -6636738, -9915042);
        if (str == null) {
        }
        QrView qrView22 = this.qrView;
        if (str2 != null) {
        }
        qrView22.setData(str3, str, z3, z);
        this.qrView.setCenterChangedListener(new QrView.QrCenterChangedListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.QrActivity.QrView.QrCenterChangedListener
            public final void onCenterChanged(int i, int i2, int i3, int i4) {
                QrActivity.this.lambda$createView$0(i, i2, i3, i4);
            }
        });
        frameLayout.addView(this.qrView);
        RLottieImageView rLottieImageView2 = new RLottieImageView(context);
        this.logoImageView = rLottieImageView2;
        rLottieImageView2.setAutoRepeat(true);
        this.logoImageView.setAnimation(R.raw.qr_code_logo_2, 60, 60);
        this.logoImageView.playAnimation();
        frameLayout.addView(this.logoImageView);
        BackupImageView backupImageView2 = new BackupImageView(context);
        this.avatarImageView = backupImageView2;
        backupImageView2.setRoundRadius(AndroidUtilities.dp(42.0f));
        this.avatarImageView.setSize(AndroidUtilities.dp(84.0f), AndroidUtilities.dp(84.0f));
        frameLayout.addView(this.avatarImageView, LayoutHelper.createFrame(84, 84, 51));
        this.avatarImageView.setImage(imageLocation, "84_84", imageLocation2, "50_50", avatarDrawable, null, null, 0, null);
        ImageView imageView2 = new ImageView(context);
        this.closeImageView = imageView2;
        imageView2.setBackground(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(34.0f), 671088640, 687865855));
        this.closeImageView.setImageResource(R.drawable.ic_ab_back);
        this.closeImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.closeImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                QrActivity.this.lambda$createView$1(view2);
            }
        });
        frameLayout.addView(this.closeImageView, LayoutHelper.createFrame(34, 34.0f));
        this.emojiThemeIcon = Bitmap.createBitmap(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(this.emojiThemeIcon);
        RectF rectF2 = AndroidUtilities.rectTmp;
        rectF2.set(0.0f, 0.0f, this.emojiThemeIcon.getWidth(), this.emojiThemeIcon.getHeight());
        Paint paint2 = new Paint(1);
        paint2.setColor(-1);
        canvas2.drawRoundRect(rectF2, AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), paint2);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas2.drawBitmap(BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), R.drawable.msg_qr_mini), (this.emojiThemeIcon.getWidth() - decodeResource.getWidth()) * 0.5f, (this.emojiThemeIcon.getHeight() - decodeResource.getHeight()) * 0.5f, paint2);
        canvas2.setBitmap(null);
        ThemeListViewController themeListViewController2 = new ThemeListViewController(this, getParentActivity().getWindow()) { // from class: org.telegram.ui.QrActivity.3
            @Override // org.telegram.ui.QrActivity.ThemeListViewController
            protected void setDarkTheme(boolean z4) {
                super.setDarkTheme(z4);
                QrActivity.this.isCurrentThemeDark = z4;
                QrActivity qrActivity = QrActivity.this;
                qrActivity.onItemSelected(qrActivity.currentTheme, QrActivity.this.selectedPosition, false);
            }
        };
        this.themesViewController = themeListViewController2;
        this.themeLayout = themeListViewController2.rootLayout;
        themeListViewController2.onCreate();
        this.themesViewController.setItemSelectedListener(new OnItemSelectedListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda14
            @Override // org.telegram.ui.QrActivity.OnItemSelectedListener
            public final void onItemSelected(EmojiThemes emojiThemes, int i) {
                QrActivity.this.lambda$createView$2(emojiThemes, i);
            }
        });
        this.themesViewController.titleView.setText(LocaleController.getString("QrCode", R.string.QrCode));
        this.themesViewController.progressView.setViewType(17);
        this.themesViewController.shareButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                QrActivity.this.lambda$createView$3(view2);
            }
        });
        frameLayout.addView(this.themeLayout, LayoutHelper.createFrame(-1, -2, 80));
        this.currMotionDrawable.setIndeterminateAnimation(true);
        this.fragmentView = frameLayout;
        Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$createView$5();
            }
        }, 25L);
        this.fragmentView.postDelayed(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$createView$6();
            }
        }, !firstOpen ? 250L : 0L);
        this.prevSystemUiVisibility = getParentActivity().getWindow().getDecorView().getSystemUiVisibility();
        applyScreenSettings();
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(int i, int i2, int i3, int i4) {
        this.logoRect.set(i, i2, i3, i4);
        this.qrView.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(EmojiThemes emojiThemes, int i) {
        onItemSelected(emojiThemes, i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        this.themesViewController.shareButton.setClickable(false);
        performShare();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5() {
        this.homeTheme.loadPreviewColors(this.currentAccount);
        View view = this.fragmentView;
        if (view == null) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$createView$4();
            }
        }, 17L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        onItemSelected(this.currentTheme, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        firstOpen = false;
        List<EmojiThemes> list = cachedThemes;
        if (list == null || list.isEmpty()) {
            ChatThemeController.requestAllChatThemes(new ResultCallback<List<EmojiThemes>>() { // from class: org.telegram.ui.QrActivity.4
                @Override // org.telegram.tgnet.ResultCallback
                public void onComplete(List<EmojiThemes> list2) {
                    QrActivity.this.onDataLoaded(list2);
                    List unused = QrActivity.cachedThemes = list2;
                }

                @Override // org.telegram.tgnet.ResultCallback
                public void onError(TLRPC$TL_error tLRPC$TL_error) {
                    Toast.makeText(QrActivity.this.getParentActivity(), tLRPC$TL_error.text, 0).show();
                }
            }, true);
        } else {
            onDataLoaded(cachedThemes);
        }
    }

    private boolean phoneIsPublic() {
        char c;
        ArrayList<TLRPC$PrivacyRule> privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(6);
        if (privacyRules == null) {
            return false;
        }
        for (int i = 0; i < privacyRules.size(); i++) {
            TLRPC$PrivacyRule tLRPC$PrivacyRule = privacyRules.get(i);
            if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowAll) {
                c = 0;
                break;
            } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueDisallowAll) {
                break;
            } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowContacts) {
                c = 1;
                break;
            }
        }
        c = 2;
        if (c == 2) {
            ArrayList<TLRPC$PrivacyRule> privacyRules2 = ContactsController.getInstance(this.currentAccount).getPrivacyRules(7);
            if (privacyRules2 == null || privacyRules2.size() == 0) {
                return true;
            }
            for (int i2 = 0; i2 < privacyRules2.size(); i2++) {
                TLRPC$PrivacyRule tLRPC$PrivacyRule2 = privacyRules2.get(i2);
                if (tLRPC$PrivacyRule2 instanceof TLRPC$TL_privacyValueAllowAll) {
                    return true;
                }
                if ((tLRPC$PrivacyRule2 instanceof TLRPC$TL_privacyValueDisallowAll) || (tLRPC$PrivacyRule2 instanceof TLRPC$TL_privacyValueAllowContacts)) {
                    return false;
                }
            }
        }
        return c == 0 || c == 1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"SourceLockedOrientationActivity"})
    public void onResume() {
        super.onResume();
        applyScreenSettings();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        restoreScreenSettings();
        super.onPause();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        this.themesViewController.onDestroy();
        this.themesViewController = null;
        this.emojiThemeIcon.recycle();
        this.emojiThemeIcon = null;
        for (int i = 0; i < this.emojiThemeDarkIcons.size(); i++) {
            Bitmap valueAt = this.emojiThemeDarkIcons.valueAt(i);
            if (valueAt != null) {
                valueAt.recycle();
            }
        }
        this.emojiThemeDarkIcons.clear();
        restoreScreenSettings();
        super.onFragmentDestroy();
    }

    private void applyScreenSettings() {
        if (getParentActivity() != null) {
            getParentActivity().getWindow().getDecorView().setSystemUiVisibility(this.prevSystemUiVisibility | ConnectionsManager.RequestFlagDoNotWaitFloodWait | 4);
        }
    }

    private void restoreScreenSettings() {
        if (getParentActivity() != null) {
            getParentActivity().getWindow().getDecorView().setSystemUiVisibility(this.prevSystemUiVisibility);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return getThemedColor("windowBackgroundGray");
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataLoaded(List<EmojiThemes> list) {
        if (list == null || list.isEmpty() || this.themesViewController == null) {
            return;
        }
        int i = 0;
        list.set(0, this.homeTheme);
        ArrayList arrayList = new ArrayList(list.size());
        for (int i2 = 0; i2 < list.size(); i2++) {
            EmojiThemes emojiThemes = list.get(i2);
            emojiThemes.loadPreviewColors(this.currentAccount);
            ChatThemeBottomSheet.ChatThemeItem chatThemeItem = new ChatThemeBottomSheet.ChatThemeItem(emojiThemes);
            boolean z = this.isCurrentThemeDark;
            chatThemeItem.themeIndex = z ? 1 : 0;
            chatThemeItem.icon = getEmojiThemeIcon(emojiThemes, z);
            arrayList.add(chatThemeItem);
        }
        this.themesViewController.adapter.setItems(arrayList);
        while (true) {
            if (i == arrayList.size()) {
                i = -1;
                break;
            } else if (((ChatThemeBottomSheet.ChatThemeItem) arrayList.get(i)).chatTheme.getEmoticon().equals(this.currentTheme.getEmoticon())) {
                this.themesViewController.selectedItem = (ChatThemeBottomSheet.ChatThemeItem) arrayList.get(i);
                break;
            } else {
                i++;
            }
        }
        if (i != -1) {
            this.themesViewController.setSelectedPosition(i);
        }
        this.themesViewController.onDataLoaded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getEmojiThemeIcon(EmojiThemes emojiThemes, boolean z) {
        if (z) {
            Bitmap bitmap = this.emojiThemeDarkIcons.get(emojiThemes.emoji);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(this.emojiThemeIcon.getWidth(), this.emojiThemeIcon.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                ArrayMap<String, int[]> arrayMap = qrColorsMap;
                int[] iArr = arrayMap.get(emojiThemes.emoji + "n");
                if (iArr != null) {
                    if (this.tempMotionDrawable == null) {
                        this.tempMotionDrawable = new MotionBackgroundDrawable(0, 0, 0, 0, true);
                    }
                    this.tempMotionDrawable.setColors(iArr[0], iArr[1], iArr[2], iArr[3]);
                    this.tempMotionDrawable.setBounds(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), canvas.getWidth() - AndroidUtilities.dp(6.0f), canvas.getHeight() - AndroidUtilities.dp(6.0f));
                    this.tempMotionDrawable.draw(canvas);
                }
                canvas.drawBitmap(this.emojiThemeIcon, 0.0f, 0.0f, (Paint) null);
                canvas.setBitmap(null);
                this.emojiThemeDarkIcons.put(emojiThemes.emoji, bitmap);
            }
            return bitmap;
        }
        return this.emojiThemeIcon;
    }

    private void onPatternLoaded(Bitmap bitmap, int i, boolean z) {
        if (bitmap != null) {
            this.currMotionDrawable.setPatternBitmap(i, bitmap, true);
            ValueAnimator valueAnimator = this.patternIntensityAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (z) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.patternIntensityAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        QrActivity.this.lambda$onPatternLoaded$7(valueAnimator2);
                    }
                });
                this.patternIntensityAnimator.setDuration(250L);
                this.patternIntensityAnimator.start();
                return;
            }
            this.currMotionDrawable.setPatternAlpha(1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPatternLoaded$7(ValueAnimator valueAnimator) {
        this.currMotionDrawable.setPatternAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onItemSelected(EmojiThemes emojiThemes, int i, final boolean z) {
        float f;
        this.selectedPosition = i;
        final EmojiThemes emojiThemes2 = this.currentTheme;
        final boolean z2 = this.isCurrentThemeDark;
        this.currentTheme = emojiThemes;
        EmojiThemes.ThemeItem themeItem = emojiThemes.getThemeItem(z2 ? 1 : 0);
        ValueAnimator valueAnimator = this.patternAlphaAnimator;
        if (valueAnimator != null) {
            f = Math.max(0.5f, 1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue()) * 1.0f;
            this.patternAlphaAnimator.cancel();
        } else {
            f = 1.0f;
        }
        MotionBackgroundDrawable motionBackgroundDrawable = this.currMotionDrawable;
        this.prevMotionDrawable = motionBackgroundDrawable;
        motionBackgroundDrawable.setIndeterminateAnimation(false);
        this.prevMotionDrawable.setAlpha(255);
        MotionBackgroundDrawable motionBackgroundDrawable2 = new MotionBackgroundDrawable();
        this.currMotionDrawable = motionBackgroundDrawable2;
        motionBackgroundDrawable2.setCallback(this.backgroundView);
        this.currMotionDrawable.setColors(themeItem.patternBgColor, themeItem.patternBgGradientColor1, themeItem.patternBgGradientColor2, themeItem.patternBgGradientColor3);
        this.currMotionDrawable.setParentView(this.backgroundView);
        this.currMotionDrawable.setPatternAlpha(1.0f);
        this.currMotionDrawable.setIndeterminateAnimation(true);
        MotionBackgroundDrawable motionBackgroundDrawable3 = this.prevMotionDrawable;
        if (motionBackgroundDrawable3 != null) {
            this.currMotionDrawable.posAnimationProgress = motionBackgroundDrawable3.posAnimationProgress;
        }
        this.qrView.setPosAnimationProgress(this.currMotionDrawable.posAnimationProgress);
        TLRPC$WallPaper wallpaper = this.currentTheme.getWallpaper(z2 ? 1 : 0);
        if (wallpaper != null) {
            this.currMotionDrawable.setPatternBitmap(wallpaper.settings.intensity);
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            this.currentTheme.loadWallpaper(z2 ? 1 : 0, new ResultCallback() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda12
                @Override // org.telegram.tgnet.ResultCallback
                public final void onComplete(Object obj) {
                    QrActivity.this.lambda$onItemSelected$8(z2, elapsedRealtime, (Pair) obj);
                }

                @Override // org.telegram.tgnet.ResultCallback
                public /* synthetic */ void onError(TLRPC$TL_error tLRPC$TL_error) {
                    ResultCallback.-CC.$default$onError(this, tLRPC$TL_error);
                }
            });
        } else {
            Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.this.lambda$onItemSelected$10();
                }
            }, 35L);
        }
        MotionBackgroundDrawable motionBackgroundDrawable4 = this.currMotionDrawable;
        motionBackgroundDrawable4.setPatternColorFilter(motionBackgroundDrawable4.getPatternColor());
        ArrayMap<String, int[]> arrayMap = qrColorsMap;
        StringBuilder sb = new StringBuilder();
        sb.append(emojiThemes.emoji);
        sb.append(z2 ? "n" : "d");
        final int[] iArr = arrayMap.get(sb.toString());
        if (z) {
            if (this.prevQrColors == null) {
                int[] iArr2 = new int[4];
                this.prevQrColors = iArr2;
                System.arraycopy(iArr, 0, iArr2, 0, 4);
            }
            this.currMotionDrawable.setAlpha(255);
            this.currMotionDrawable.setBackgroundAlpha(0.0f);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.patternAlphaAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    QrActivity.this.lambda$onItemSelected$11(iArr, valueAnimator2);
                }
            });
            this.patternAlphaAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.QrActivity.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    int[] iArr3 = iArr;
                    if (iArr3 != null) {
                        System.arraycopy(iArr3, 0, QrActivity.this.prevQrColors, 0, 4);
                    }
                    QrActivity.this.prevMotionDrawable = null;
                    QrActivity.this.patternAlphaAnimator = null;
                    QrActivity.this.currMotionDrawable.setBackgroundAlpha(1.0f);
                    QrActivity.this.currMotionDrawable.setPatternAlpha(1.0f);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    float floatValue = ((Float) ((ValueAnimator) animator).getAnimatedValue()).floatValue();
                    if (iArr != null) {
                        System.arraycopy(new int[]{ColorUtils.blendARGB(QrActivity.this.prevQrColors[0], iArr[0], floatValue), ColorUtils.blendARGB(QrActivity.this.prevQrColors[1], iArr[1], floatValue), ColorUtils.blendARGB(QrActivity.this.prevQrColors[2], iArr[2], floatValue), ColorUtils.blendARGB(QrActivity.this.prevQrColors[3], iArr[3], floatValue)}, 0, QrActivity.this.prevQrColors, 0, 4);
                    }
                }
            });
            this.patternAlphaAnimator.setDuration((int) (f * 250.0f));
            this.patternAlphaAnimator.start();
        } else {
            if (iArr != null) {
                this.qrView.setColors(iArr[0], iArr[1], iArr[2], iArr[3]);
                System.arraycopy(iArr, 0, this.prevQrColors, 0, 4);
            }
            this.prevMotionDrawable = null;
            this.backgroundView.invalidate();
        }
        final INavigationLayout.ThemeAnimationSettings themeAnimationSettings = new INavigationLayout.ThemeAnimationSettings(null, (this.isCurrentThemeDark ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme()).currentAccentId, this.isCurrentThemeDark, !z);
        themeAnimationSettings.applyTheme = false;
        themeAnimationSettings.onlyTopFragment = true;
        themeAnimationSettings.resourcesProvider = getResourceProvider();
        themeAnimationSettings.duration = (int) (f * 250.0f);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$onItemSelected$13(z, emojiThemes2, themeAnimationSettings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$8(boolean z, long j, Pair pair) {
        if (pair == null || this.currentTheme.getTlTheme(z ? 1 : 0) == null) {
            return;
        }
        long longValue = ((Long) pair.first).longValue();
        Bitmap bitmap = (Bitmap) pair.second;
        if (longValue != this.currentTheme.getTlTheme(z ? 1 : 0).id || bitmap == null) {
            return;
        }
        onPatternLoaded(bitmap, this.currMotionDrawable.getIntensity(), SystemClock.elapsedRealtime() - j > 150);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$10() {
        final Bitmap bitmap = SvgHelper.getBitmap(R.raw.default_pattern, this.backgroundView.getWidth(), this.backgroundView.getHeight(), -16777216);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$onItemSelected$9(bitmap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$9(Bitmap bitmap) {
        onPatternLoaded(bitmap, 34, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$11(int[] iArr, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        MotionBackgroundDrawable motionBackgroundDrawable = this.prevMotionDrawable;
        if (motionBackgroundDrawable != null) {
            motionBackgroundDrawable.setBackgroundAlpha(1.0f);
            this.prevMotionDrawable.setPatternAlpha(1.0f - floatValue);
        }
        this.currMotionDrawable.setBackgroundAlpha(floatValue);
        this.currMotionDrawable.setPatternAlpha(floatValue);
        if (iArr != null) {
            this.qrView.setColors(ColorUtils.blendARGB(this.prevQrColors[0], iArr[0], floatValue), ColorUtils.blendARGB(this.prevQrColors[1], iArr[1], floatValue), ColorUtils.blendARGB(this.prevQrColors[2], iArr[2], floatValue), ColorUtils.blendARGB(this.prevQrColors[3], iArr[3], floatValue));
        }
        this.backgroundView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$13(boolean z, EmojiThemes emojiThemes, INavigationLayout.ThemeAnimationSettings themeAnimationSettings) {
        if (z) {
            this.resourcesProvider.initColors(emojiThemes, this.isCurrentThemeDark);
        } else {
            this.resourcesProvider.initColors(this.currentTheme, this.isCurrentThemeDark);
        }
        themeAnimationSettings.afterStartDescriptionsAddedRunnable = new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$onItemSelected$12();
            }
        };
        this.parentLayout.animateThemedValues(themeAnimationSettings, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemSelected$12() {
        this.resourcesProvider.initColors(this.currentTheme, this.isCurrentThemeDark);
    }

    private void performShare() {
        Point point = AndroidUtilities.displaySize;
        int min = Math.min(point.x, point.y);
        Point point2 = AndroidUtilities.displaySize;
        int max = Math.max(point2.x, point2.y);
        float f = min;
        if ((max * 1.0f) / f > 1.92f) {
            max = (int) (f * 1.92f);
        }
        Bitmap createBitmap = Bitmap.createBitmap(min, max, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.themeLayout.setVisibility(8);
        this.closeImageView.setVisibility(8);
        this.logoImageView.stopAnimation();
        RLottieDrawable animatedDrawable = this.logoImageView.getAnimatedDrawable();
        int currentFrame = animatedDrawable.getCurrentFrame();
        animatedDrawable.setCurrentFrame(33, false);
        QrView qrView = this.qrView;
        if (qrView != null) {
            qrView.setForShare(true);
        }
        this.fragmentView.measure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), View.MeasureSpec.makeMeasureSpec(max, 1073741824));
        this.fragmentView.layout(0, 0, min, max);
        this.fragmentView.draw(canvas);
        canvas.setBitmap(null);
        this.themeLayout.setVisibility(0);
        this.closeImageView.setVisibility(0);
        animatedDrawable.setCurrentFrame(currentFrame, false);
        this.logoImageView.playAnimation();
        ViewGroup viewGroup = (ViewGroup) this.fragmentView.getParent();
        this.fragmentView.layout(0, 0, viewGroup.getWidth(), viewGroup.getHeight());
        QrView qrView2 = this.qrView;
        if (qrView2 != null) {
            qrView2.setForShare(false);
        }
        Uri bitmapShareUri = AndroidUtilities.getBitmapShareUri(createBitmap, "qr_tmp.jpg", Bitmap.CompressFormat.JPEG);
        if (bitmapShareUri != null) {
            try {
                getParentActivity().startActivityForResult(Intent.createChooser(new Intent("android.intent.action.SEND").setType("image/*").putExtra("android.intent.extra.STREAM", bitmapShareUri), LocaleController.getString("InviteByQRCode", R.string.InviteByQRCode)), 500);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                QrActivity.this.lambda$performShare$14();
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performShare$14() {
        ThemeListViewController themeListViewController = this.themesViewController;
        if (themeListViewController == null) {
            return;
        }
        themeListViewController.shareButton.setClickable(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = super.getThemeDescriptions();
        themeDescriptions.addAll(this.themesViewController.getThemeDescriptions());
        themeDescriptions.add(new ThemeDescription(this.themesViewController.shareButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.QrActivity$$ExternalSyntheticLambda13
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                QrActivity.this.lambda$getThemeDescriptions$15();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, "featuredStickers_addButton"));
        themeDescriptions.add(new ThemeDescription(this.themesViewController.shareButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "featuredStickers_addButtonPressed"));
        Iterator<ThemeDescription> it = themeDescriptions.iterator();
        while (it.hasNext()) {
            it.next().resourcesProvider = getResourceProvider();
        }
        return themeDescriptions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$15() {
        setNavigationBarColor(getThemedColor("windowBackgroundGray"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ThemeResourcesProvider implements Theme.ResourcesProvider {
        private HashMap<String, Integer> colors;

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
            Theme.applyServiceShaderMatrix(i, i2, f, f2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ int getColorOrDefault(String str) {
            return getColor(str);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ Integer getCurrentColor(String str) {
            Integer color;
            color = getColor(str);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ Drawable getDrawable(String str) {
            return Theme.ResourcesProvider.-CC.$default$getDrawable(this, str);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ Paint getPaint(String str) {
            return Theme.ResourcesProvider.-CC.$default$getPaint(this, str);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ boolean hasGradientService() {
            return Theme.ResourcesProvider.-CC.$default$hasGradientService(this);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void setAnimatedColor(String str, int i) {
            Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, str, i);
        }

        private ThemeResourcesProvider() {
        }

        void initColors(EmojiThemes emojiThemes, boolean z) {
            this.colors = emojiThemes.createColors(((BaseFragment) QrActivity.this).currentAccount, z ? 1 : 0);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Integer getColor(String str) {
            HashMap<String, Integer> hashMap = this.colors;
            if (hashMap != null) {
                return hashMap.get(str);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class QrView extends View {
        private Bitmap backgroundBitmap;
        private final Paint bitmapGradientPaint;
        private QrCenterChangedListener centerChangedListener;
        private Runnable checkTimerToken;
        private Bitmap contentBitmap;
        private AnimatedFloat contentBitmapAlpha;
        private Paint crossfadeFromPaint;
        private Paint crossfadeToPaint;
        private boolean firstPrepare;
        private final MotionBackgroundDrawable gradientDrawable;
        private final BitmapShader gradientShader;
        private final BitmapShader gradientTextShader;
        private Integer hadHeight;
        private String hadLink;
        private String hadUserText;
        private Integer hadWidth;
        private boolean hasTimer;
        private boolean isPhone;
        private String link;
        private int linkExpires;
        private RLottieDrawable loadingMatrix;
        private boolean logoCenterSet;
        private Bitmap oldContentBitmap;
        private float[] radii;
        private boolean setData;
        private StaticLayout shareUsernameLayout;
        private TextPaint shareUsernameLayoutPaint;
        private AnimatedTextView.AnimatedTextDrawable timerTextDrawable;
        private String username;
        private static final float SHADOW_SIZE = AndroidUtilities.dp(2.0f);
        private static final float RADIUS = AndroidUtilities.dp(20.0f);

        /* loaded from: classes3.dex */
        public interface QrCenterChangedListener {
            void onCenterChanged(int i, int i2, int i3, int i4);
        }

        QrView(Context context) {
            super(context);
            MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable();
            this.gradientDrawable = motionBackgroundDrawable;
            Paint paint = new Paint(1);
            this.bitmapGradientPaint = paint;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.contentBitmapAlpha = new AnimatedFloat(1.0f, this, 0L, 2000L, cubicBezierInterpolator);
            this.crossfadeFromPaint = new Paint(1);
            this.crossfadeToPaint = new Paint(1);
            this.radii = new float[8];
            this.checkTimerToken = new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.QrView.this.lambda$new$5();
                }
            };
            this.firstPrepare = true;
            motionBackgroundDrawable.setIndeterminateAnimation(true);
            motionBackgroundDrawable.setParentView(this);
            Bitmap bitmap = motionBackgroundDrawable.getBitmap();
            Shader.TileMode tileMode = Shader.TileMode.MIRROR;
            BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
            this.gradientShader = bitmapShader;
            Bitmap bitmap2 = motionBackgroundDrawable.getBitmap();
            Shader.TileMode tileMode2 = Shader.TileMode.MIRROR;
            BitmapShader bitmapShader2 = new BitmapShader(bitmap2, tileMode2, tileMode2);
            this.gradientTextShader = bitmapShader2;
            paint.setShader(bitmapShader);
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, false) { // from class: org.telegram.ui.QrActivity.QrView.1
                @Override // android.graphics.drawable.Drawable
                public void invalidateSelf() {
                    QrView.this.invalidate();
                }
            };
            this.timerTextDrawable = animatedTextDrawable;
            animatedTextDrawable.setAnimationProperties(0.35f, 0L, 300L, cubicBezierInterpolator);
            this.timerTextDrawable.setCallback(this);
            this.timerTextDrawable.setTypeface(AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf"));
            this.timerTextDrawable.getPaint().setShader(bitmapShader2);
            this.timerTextDrawable.setGravity(17);
            this.timerTextDrawable.setTextSize(AndroidUtilities.dp(35.0f));
            this.timerTextDrawable.setText("");
            this.crossfadeFromPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(120.0f), new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
            this.crossfadeFromPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            this.crossfadeToPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(120.0f), new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
            this.crossfadeToPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }

        public void setForShare(boolean z) {
            if (this.hasTimer) {
                if (z) {
                    if (this.shareUsernameLayoutPaint == null) {
                        this.shareUsernameLayoutPaint = new TextPaint(1);
                    }
                    this.shareUsernameLayoutPaint.setShader(this.gradientTextShader);
                    this.shareUsernameLayoutPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf"));
                    this.shareUsernameLayoutPaint.setTextSize(AndroidUtilities.dp(25.0f));
                    String str = this.username;
                    if (str == null) {
                        str = "";
                    }
                    this.shareUsernameLayout = StaticLayoutEx.createStaticLayout(Emoji.replaceEmoji(str, this.shareUsernameLayoutPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false), this.shareUsernameLayoutPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, getWidth() - AndroidUtilities.dp(60.0f), 1);
                    return;
                }
                this.shareUsernameLayout = null;
            }
        }

        @Override // android.view.View
        protected void onSizeChanged(final int i, final int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            if (i == i3 && i2 == i4) {
                return;
            }
            Bitmap bitmap = this.backgroundBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.backgroundBitmap = null;
            }
            Paint paint = new Paint(1);
            paint.setColor(-1);
            float f = SHADOW_SIZE;
            paint.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, f, AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY);
            this.backgroundBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.backgroundBitmap);
            float f2 = i;
            RectF rectF = new RectF(f, f, f2 - f, getHeight() - f);
            float f3 = RADIUS;
            canvas.drawRoundRect(rectF, f3, f3, paint);
            if (this.setData) {
                Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        QrActivity.QrView.this.lambda$onSizeChanged$0(i, i2);
                    }
                });
            }
            float max = Math.max((getWidth() * 1.0f) / this.gradientDrawable.getBitmap().getWidth(), (getHeight() * 1.0f) / this.gradientDrawable.getBitmap().getHeight());
            Matrix matrix = new Matrix();
            matrix.setScale(max, max);
            this.gradientShader.setLocalMatrix(matrix);
            Matrix matrix2 = new Matrix();
            matrix2.setScale(max, max);
            matrix2.postTranslate(f2 / 2.0f, getWidth() + AndroidUtilities.dp(6.0f));
            this.gradientTextShader.setLocalMatrix(matrix2);
        }

        private void drawLoading(Canvas canvas) {
            float f;
            QrCenterChangedListener qrCenterChangedListener;
            if (this.loadingMatrix != null) {
                int width = (getWidth() - AndroidUtilities.dp(60.0f)) / 33;
                int i = (width * 33) + 32;
                int width2 = (getWidth() - i) / 2;
                int height = (int) (getHeight() * 0.15f);
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    height = (int) (getHeight() * 0.09f);
                }
                int i2 = height;
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.saveLayerAlpha(rectF, 255, 31);
                int i3 = width2 + 16;
                int i4 = i2 + 16;
                canvas.drawRect(i3, i4, (getWidth() - width2) - 16, (((getWidth() + i2) - width2) - width2) - 16, this.bitmapGradientPaint);
                canvas.save();
                this.loadingMatrix.setBounds(i3, i4, (getWidth() - width2) - 16, (((getWidth() + i2) - width2) - width2) - 16);
                this.loadingMatrix.draw(canvas);
                canvas.restore();
                canvas.restore();
                float width3 = getWidth() / 2.0f;
                float f2 = i2;
                float f3 = width2;
                float width4 = ((getWidth() / 2.0f) + f2) - f3;
                float round = ((Math.round(((i - 32) / 4.65f) / f) * width) / 2) * 0.75f;
                canvas.drawCircle(width3, width4, round, this.bitmapGradientPaint);
                QRCodeWriter.drawSideQuads(canvas, f3, f2, this.bitmapGradientPaint, 7.0f, width, 16, i, 0.75f, this.radii, true);
                if (this.logoCenterSet || (qrCenterChangedListener = this.centerChangedListener) == null) {
                    return;
                }
                qrCenterChangedListener.onCenterChanged((int) (width3 - round), (int) (width4 - round), (int) (width3 + round), (int) (width4 + round));
                this.logoCenterSet = true;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x008f  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00f7  */
        /* JADX WARN: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            super.onDraw(canvas);
            Bitmap bitmap = this.backgroundBitmap;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            }
            float f = this.contentBitmapAlpha.set(1.0f);
            boolean z = f > 0.0f && f < 1.0f;
            if (f < 1.0f) {
                if (z) {
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                    canvas.saveLayerAlpha(rectF, 255, 31);
                }
                Bitmap bitmap2 = this.oldContentBitmap;
                if (bitmap2 != null) {
                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, this.bitmapGradientPaint);
                } else {
                    drawLoading(canvas);
                }
                if (z) {
                    float dp = AndroidUtilities.dp(120.0f);
                    canvas.save();
                    canvas.translate(0.0f, (-dp) + ((getHeight() + dp) * (1.0f - f)));
                    i = 255;
                    canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight() + dp, this.crossfadeToPaint);
                    canvas.restore();
                    canvas.restore();
                    if (f > 0.0f) {
                        if (z) {
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            rectF2.set(0.0f, 0.0f, getWidth(), getHeight());
                            canvas.saveLayerAlpha(rectF2, i, 31);
                        }
                        Bitmap bitmap3 = this.contentBitmap;
                        if (bitmap3 != null) {
                            canvas.drawBitmap(bitmap3, 0.0f, 0.0f, this.bitmapGradientPaint);
                            this.gradientDrawable.updateAnimation(true);
                        } else {
                            drawLoading(canvas);
                        }
                        if (z) {
                            float dp2 = AndroidUtilities.dp(120.0f);
                            canvas.save();
                            float f2 = -dp2;
                            canvas.translate(0.0f, ((getHeight() + dp2) * (1.0f - f)) + f2);
                            canvas.drawRect(0.0f, f2 - getHeight(), getWidth(), getHeight() + dp2, this.crossfadeFromPaint);
                            canvas.restore();
                            canvas.restore();
                        }
                    }
                    if (this.hasTimer) {
                        return;
                    }
                    float width = getWidth() + AndroidUtilities.dp(6.0f);
                    if (this.shareUsernameLayout != null) {
                        canvas.save();
                        canvas.translate(0.0f, width);
                        if (this.shareUsernameLayout.getWidth() != getWidth()) {
                            setForShare(true);
                        }
                        this.shareUsernameLayout.draw(canvas);
                        canvas.restore();
                        return;
                    }
                    int i2 = (int) width;
                    this.timerTextDrawable.setBounds(0, i2, getWidth(), AndroidUtilities.dp(40.0f) + i2);
                    this.timerTextDrawable.draw(canvas);
                    return;
                }
            }
            i = 255;
            if (f > 0.0f) {
            }
            if (this.hasTimer) {
            }
        }

        void setCenterChangedListener(QrCenterChangedListener qrCenterChangedListener) {
            this.centerChangedListener = qrCenterChangedListener;
        }

        void setData(String str, String str2, boolean z, boolean z2) {
            this.setData = true;
            this.username = str2;
            this.isPhone = z;
            if (z2) {
                TLRPC$TL_exportedContactToken cachedContactToken = MessagesController.getInstance(UserConfig.selectedAccount).getCachedContactToken();
                if (cachedContactToken != null) {
                    this.link = cachedContactToken.url;
                    this.linkExpires = cachedContactToken.expires;
                } else {
                    this.link = null;
                }
            } else {
                this.link = str;
            }
            this.hasTimer = z2;
            final int width = getWidth();
            final int height = getHeight();
            Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.QrView.this.lambda$setData$1(width, height);
                }
            });
            invalidate();
            this.checkTimerToken.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5() {
            AndroidUtilities.cancelRunOnUIThread(this.checkTimerToken);
            boolean z = this.hasTimer;
            if (z) {
                if (z && this.loadingMatrix == null) {
                    RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.qr_matrix, "qr_matrix", AndroidUtilities.dp(200.0f), AndroidUtilities.dp(200.0f));
                    this.loadingMatrix = rLottieDrawable;
                    rLottieDrawable.setMasterParent(this);
                    this.loadingMatrix.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    this.loadingMatrix.setAutoRepeat(1);
                    this.loadingMatrix.start();
                }
                if (this.linkExpires == 0 || System.currentTimeMillis() / 1000 >= this.linkExpires) {
                    if (this.linkExpires != 0) {
                        this.link = null;
                        final int width = getWidth();
                        final int height = getHeight();
                        Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                QrActivity.QrView.this.lambda$new$2(width, height);
                            }
                        });
                        this.timerTextDrawable.setText("");
                    }
                    MessagesController.getInstance(UserConfig.selectedAccount).requestContactToken(this.linkExpires == 0 ? 750L : 1750L, new Utilities.Callback() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda7
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            QrActivity.QrView.this.lambda$new$4((TLRPC$TL_exportedContactToken) obj);
                        }
                    });
                }
                int i = this.linkExpires;
                if (i > 0 && this.link != null) {
                    long max = Math.max(0L, (i - (System.currentTimeMillis() / 1000)) - 1);
                    int i2 = (int) (max % 60);
                    int min = Math.min(99, (int) (max / 60));
                    AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.timerTextDrawable;
                    StringBuilder sb = new StringBuilder();
                    sb.append(min < 10 ? "0" : "");
                    sb.append(min);
                    sb.append(":");
                    sb.append(i2 < 10 ? "0" : "");
                    sb.append(i2);
                    animatedTextDrawable.setText(sb.toString(), true, false);
                }
                if (isAttachedToWindow()) {
                    AndroidUtilities.runOnUIThread(this.checkTimerToken, 1000L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(final TLRPC$TL_exportedContactToken tLRPC$TL_exportedContactToken) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.QrView.this.lambda$new$3(tLRPC$TL_exportedContactToken);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(TLRPC$TL_exportedContactToken tLRPC$TL_exportedContactToken) {
            if (tLRPC$TL_exportedContactToken == null) {
                return;
            }
            int i = this.linkExpires;
            if (i != 0 && i < tLRPC$TL_exportedContactToken.expires) {
                try {
                    Vibrator vibrator = (Vibrator) getContext().getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(100L);
                    }
                } catch (Exception unused) {
                    try {
                        performHapticFeedback(0, 2);
                    } catch (Exception unused2) {
                    }
                }
            }
            this.linkExpires = tLRPC$TL_exportedContactToken.expires;
            setData(tLRPC$TL_exportedContactToken.url, null, false, true);
        }

        void setColors(int i, int i2, int i3, int i4) {
            this.gradientDrawable.setColors(i, i2, i3, i4);
            invalidate();
        }

        void setPosAnimationProgress(float f) {
            this.gradientDrawable.posAnimationProgress = f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:67:0x01b3  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x01b5  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0219 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x021a  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x01e5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* renamed from: prepareContent */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void lambda$setData$1(int i, int i2) {
            TextPaint textPaint;
            int i3;
            int i4;
            StaticLayout staticLayout;
            int i5;
            Bitmap bitmap;
            Drawable drawable;
            int i6;
            Integer num;
            if (i == 0 || i2 == 0) {
                return;
            }
            if ((TextUtils.isEmpty(this.username) && !this.hasTimer) || TextUtils.isEmpty(this.link)) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        QrActivity.QrView.this.lambda$prepareContent$6();
                    }
                });
                return;
            }
            String upperCase = this.hasTimer ? null : this.isPhone ? this.username : this.username.toUpperCase();
            if (TextUtils.equals(upperCase, this.hadUserText) && TextUtils.equals(this.link, this.hadLink) && (num = this.hadWidth) != null && this.hadHeight != null && num.intValue() == i && this.hadHeight.intValue() == i2) {
                return;
            }
            final Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            int i7 = -16777216;
            TextPaint textPaint2 = new TextPaint(65);
            textPaint2.setColor(-16777216);
            textPaint2.setTypeface(AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf"));
            int width = createBitmap.getWidth() - (AndroidUtilities.dp(20.0f) * 2);
            if (!this.hasTimer) {
                int i8 = 0;
                while (i8 <= 2) {
                    if (i8 == 0) {
                        drawable = ContextCompat.getDrawable(getContext(), R.drawable.qr_at_large);
                        textPaint2.setTextSize(AndroidUtilities.dp(30.0f));
                    } else if (i8 == 1) {
                        drawable = ContextCompat.getDrawable(getContext(), R.drawable.qr_at_medium);
                        textPaint2.setTextSize(AndroidUtilities.dp(25.0f));
                    } else {
                        drawable = ContextCompat.getDrawable(getContext(), R.drawable.qr_at_small);
                        textPaint2.setTextSize(AndroidUtilities.dp(19.0f));
                    }
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        drawable.setColorFilter(new PorterDuffColorFilter(i7, PorterDuff.Mode.SRC_IN));
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(" " + upperCase);
                    if (!this.isPhone) {
                        spannableStringBuilder.setSpan(new SettingsSearchCell.VerticalImageSpan(drawable), 0, 1, 33);
                    }
                    float measureText = textPaint2.measureText(spannableStringBuilder, 1, spannableStringBuilder.length()) + drawable.getBounds().width();
                    if (i8 > 1 || measureText <= width) {
                        int i9 = measureText > ((float) width) ? 2 : 1;
                        int width2 = i9 > 1 ? (((int) (drawable.getBounds().width() + measureText)) / 2) + AndroidUtilities.dp(2.0f) : width;
                        if (width2 > width) {
                            width2 = (((int) (measureText + drawable.getBounds().width())) / 3) + AndroidUtilities.dp(4.0f);
                            i6 = 3;
                        } else {
                            i6 = i9;
                        }
                        i3 = 0;
                        i4 = 3;
                        textPaint = textPaint2;
                        staticLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder, textPaint2, width2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false, null, Math.min(AndroidUtilities.dp(10.0f) + width2, createBitmap.getWidth()), i6);
                        float descent = (textPaint.descent() - textPaint.ascent()) * (staticLayout != null ? 0 : staticLayout.getLineCount());
                        int dp = i - (AndroidUtilities.dp(30.0f) * 2);
                        HashMap hashMap = new HashMap();
                        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                        hashMap.put(EncodeHintType.MARGIN, Integer.valueOf(i3));
                        QRCodeWriter qRCodeWriter = new QRCodeWriter();
                        int i10 = 0;
                        Bitmap bitmap2 = null;
                        for (i5 = 3; i5 < 5; i5++) {
                            try {
                                hashMap.put(EncodeHintType.QR_VERSION, Integer.valueOf(i5));
                                bitmap2 = qRCodeWriter.encode(this.link, dp, dp, hashMap, null, 0.75f, 16777215, -16777216);
                                i10 = qRCodeWriter.getImageSize();
                            } catch (Exception unused) {
                            }
                            if (bitmap2 != null) {
                                break;
                            }
                        }
                        bitmap = bitmap2;
                        if (bitmap != null) {
                            return;
                        }
                        Canvas canvas = new Canvas(createBitmap);
                        canvas.drawColor(16777215);
                        float width3 = (i - bitmap.getWidth()) / 2.0f;
                        float f = i2;
                        float f2 = 0.15f * f;
                        if (staticLayout != null && staticLayout.getLineCount() == i4) {
                            f2 = 0.13f * f;
                        }
                        if (!(((ViewGroup) getParent()).getMeasuredWidth() < ((ViewGroup) getParent()).getMeasuredHeight())) {
                            f2 = f * 0.09f;
                        }
                        canvas.drawBitmap(bitmap, width3, f2, new Paint(i4));
                        Paint paint = new Paint(1);
                        paint.setColor(-16777216);
                        final float width4 = (bitmap.getWidth() * 0.5f) + width3;
                        final float width5 = (bitmap.getWidth() * 0.5f) + f2;
                        canvas.drawCircle(width4, width5, i10 * 0.5f, paint);
                        if (staticLayout != null) {
                            canvas.save();
                            canvas.translate((canvas.getWidth() - staticLayout.getWidth()) * 0.5f, ((bitmap.getHeight() + f2) + (((canvas.getHeight() - (f2 + bitmap.getHeight())) - descent) * 0.5f)) - AndroidUtilities.dp(4.0f));
                            staticLayout.draw(canvas);
                            canvas.restore();
                            bitmap.recycle();
                        }
                        this.hadWidth = Integer.valueOf(i);
                        this.hadHeight = Integer.valueOf(i2);
                        this.hadUserText = upperCase;
                        this.hadLink = this.link;
                        final int i11 = i10;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$QrView$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                QrActivity.QrView.this.lambda$prepareContent$7(createBitmap, width4, i11, width5);
                            }
                        });
                        return;
                    }
                    i8++;
                    i7 = -16777216;
                }
            }
            textPaint = textPaint2;
            i3 = 0;
            i4 = 3;
            staticLayout = null;
            float descent2 = (textPaint.descent() - textPaint.ascent()) * (staticLayout != null ? 0 : staticLayout.getLineCount());
            int dp2 = i - (AndroidUtilities.dp(30.0f) * 2);
            HashMap hashMap2 = new HashMap();
            hashMap2.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hashMap2.put(EncodeHintType.MARGIN, Integer.valueOf(i3));
            QRCodeWriter qRCodeWriter2 = new QRCodeWriter();
            int i102 = 0;
            Bitmap bitmap22 = null;
            while (i5 < 5) {
            }
            bitmap = bitmap22;
            if (bitmap != null) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareContent$6() {
            this.firstPrepare = false;
            Bitmap bitmap = this.contentBitmap;
            if (bitmap != null) {
                this.contentBitmap = null;
                this.contentBitmapAlpha.set(0.0f, true);
                Bitmap bitmap2 = this.oldContentBitmap;
                if (bitmap2 != null) {
                    bitmap2.recycle();
                }
                this.oldContentBitmap = bitmap;
                invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareContent$7(Bitmap bitmap, float f, int i, float f2) {
            Bitmap bitmap2 = this.contentBitmap;
            this.contentBitmap = bitmap.extractAlpha();
            if (!this.firstPrepare) {
                this.contentBitmapAlpha.set(0.0f, true);
            }
            this.firstPrepare = false;
            Bitmap bitmap3 = this.oldContentBitmap;
            if (bitmap3 != null) {
                bitmap3.recycle();
            }
            this.oldContentBitmap = bitmap2;
            QrCenterChangedListener qrCenterChangedListener = this.centerChangedListener;
            if (qrCenterChangedListener != null) {
                float f3 = i * 0.5f;
                qrCenterChangedListener.onCenterChanged((int) (f - f3), (int) (f2 - f3), (int) (f + f3), (int) (f2 + f3));
                this.logoCenterSet = true;
            }
            invalidate();
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.checkTimerToken.run();
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            RLottieDrawable rLottieDrawable = this.loadingMatrix;
            if (rLottieDrawable != null) {
                rLottieDrawable.stop();
                this.loadingMatrix.recycle(false);
                this.loadingMatrix = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ThemeListViewController implements NotificationCenter.NotificationCenterDelegate {
        public final ChatThemeBottomSheet.Adapter adapter;
        private final Drawable backgroundDrawable;
        private final View bottomShadow;
        private View changeDayNightView;
        private ValueAnimator changeDayNightViewAnimator;
        private float changeDayNightViewProgress;
        private final RLottieDrawable darkThemeDrawable;
        private final RLottieImageView darkThemeView;
        private boolean forceDark;
        private final BaseFragment fragment;
        protected boolean isLightDarkChangeAnimation;
        private OnItemSelectedListener itemSelectedListener;
        private LinearLayoutManager layoutManager;
        private boolean prevIsPortrait;
        public final FlickerLoadingView progressView;
        private final RecyclerListView recyclerView;
        public final FrameLayout rootLayout;
        private final LinearSmoothScroller scroller;
        public ChatThemeBottomSheet.ChatThemeItem selectedItem;
        public final TextView shareButton;
        public final TextView titleView;
        private final View topShadow;
        private final Window window;
        private final Paint backgroundPaint = new Paint(1);
        public int prevSelectedPosition = -1;

        protected void setDarkTheme(boolean z) {
        }

        public ThemeListViewController(BaseFragment baseFragment, Window window) {
            this.fragment = baseFragment;
            this.window = window;
            Activity parentActivity = baseFragment.getParentActivity();
            this.scroller = new LinearSmoothScroller(this, parentActivity, QrActivity.this) { // from class: org.telegram.ui.QrActivity.ThemeListViewController.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.LinearSmoothScroller
                public int calculateTimeForScrolling(int i) {
                    return super.calculateTimeForScrolling(i) * 6;
                }
            };
            Drawable mutate = parentActivity.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            this.backgroundDrawable = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(baseFragment.getThemedColor("dialogBackground"), PorterDuff.Mode.MULTIPLY));
            FrameLayout frameLayout = new FrameLayout(parentActivity, QrActivity.this, baseFragment) { // from class: org.telegram.ui.QrActivity.ThemeListViewController.2
                private final Rect backgroundPadding;
                final /* synthetic */ BaseFragment val$fragment;

                {
                    this.val$fragment = baseFragment;
                    Rect rect = new Rect();
                    this.backgroundPadding = rect;
                    ThemeListViewController.this.backgroundPaint.setColor(baseFragment.getThemedColor("windowBackgroundWhite"));
                    ThemeListViewController.this.backgroundDrawable.setCallback(this);
                    ThemeListViewController.this.backgroundDrawable.getPadding(rect);
                    setPadding(0, rect.top + AndroidUtilities.dp(8.0f), 0, rect.bottom);
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i, int i2) {
                    Point point = AndroidUtilities.displaySize;
                    boolean z = point.x < point.y;
                    int dp = AndroidUtilities.dp(12.0f);
                    if (z) {
                        ThemeListViewController.this.recyclerView.setLayoutParams(LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
                        ThemeListViewController.this.recyclerView.setPadding(dp, 0, dp, 0);
                        ThemeListViewController.this.shareButton.setLayoutParams(LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
                    } else {
                        ThemeListViewController.this.recyclerView.setLayoutParams(LayoutHelper.createFrame(-1, -1.0f, 8388611, 0.0f, 44.0f, 0.0f, 80.0f));
                        ThemeListViewController.this.recyclerView.setPadding(dp, dp / 2, dp, dp);
                        ThemeListViewController.this.shareButton.setLayoutParams(LayoutHelper.createFrame(-1, 48.0f, 80, 16.0f, 0.0f, 16.0f, 16.0f));
                    }
                    if (z) {
                        ThemeListViewController.this.bottomShadow.setVisibility(8);
                        ThemeListViewController.this.topShadow.setVisibility(8);
                    } else {
                        ThemeListViewController.this.bottomShadow.setVisibility(0);
                        ThemeListViewController.this.bottomShadow.setLayoutParams(LayoutHelper.createFrame(-1, AndroidUtilities.dp(2.0f), 80, 0.0f, 0.0f, 0.0f, 80.0f));
                        ThemeListViewController.this.topShadow.setVisibility(0);
                        ThemeListViewController.this.topShadow.setLayoutParams(LayoutHelper.createFrame(-1, AndroidUtilities.dp(2.0f), 48, 0.0f, 44.0f, 0.0f, 0.0f));
                    }
                    if (ThemeListViewController.this.prevIsPortrait != z) {
                        RecyclerListView recyclerListView = ThemeListViewController.this.recyclerView;
                        ThemeListViewController themeListViewController = ThemeListViewController.this;
                        recyclerListView.setLayoutManager(themeListViewController.layoutManager = themeListViewController.getLayoutManager(z));
                        ThemeListViewController.this.recyclerView.requestLayout();
                        ThemeListViewController themeListViewController2 = ThemeListViewController.this;
                        int i3 = themeListViewController2.prevSelectedPosition;
                        if (i3 != -1) {
                            themeListViewController2.setSelectedPosition(i3);
                        }
                        ThemeListViewController.this.prevIsPortrait = z;
                    }
                    super.onMeasure(i, i2);
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    if (ThemeListViewController.this.prevIsPortrait) {
                        ThemeListViewController.this.backgroundDrawable.setBounds(-this.backgroundPadding.left, 0, getWidth() + this.backgroundPadding.right, getHeight());
                        ThemeListViewController.this.backgroundDrawable.draw(canvas);
                    } else {
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(0.0f, 0.0f, getWidth() + AndroidUtilities.dp(14.0f), getHeight());
                        canvas.drawRoundRect(rectF, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f), ThemeListViewController.this.backgroundPaint);
                    }
                    super.dispatchDraw(canvas);
                }

                @Override // android.view.View
                protected boolean verifyDrawable(Drawable drawable) {
                    return drawable == ThemeListViewController.this.backgroundDrawable || super.verifyDrawable(drawable);
                }
            };
            this.rootLayout = frameLayout;
            TextView textView = new TextView(parentActivity);
            this.titleView = textView;
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            textView.setLines(1);
            textView.setSingleLine(true);
            textView.setTextColor(baseFragment.getThemedColor("dialogTextBlack"));
            textView.setTextSize(1, 20.0f);
            textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            textView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
            frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 8388659, 0.0f, 0.0f, 62.0f, 0.0f));
            int themedColor = baseFragment.getThemedColor("featuredStickers_addButton");
            int dp = AndroidUtilities.dp(28.0f);
            int i = R.raw.sun_outline;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, dp, dp, false, null);
            this.darkThemeDrawable = rLottieDrawable;
            this.forceDark = Theme.getActiveTheme().isDark() ^ true;
            setForceDark(Theme.getActiveTheme().isDark(), false);
            rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
            rLottieDrawable.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.MULTIPLY));
            RLottieImageView rLottieImageView = new RLottieImageView(parentActivity, QrActivity.this) { // from class: org.telegram.ui.QrActivity.ThemeListViewController.3
                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    if (QrActivity.this.isCurrentThemeDark) {
                        accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToDayTheme", R.string.AccDescrSwitchToDayTheme));
                    } else {
                        accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToNightTheme", R.string.AccDescrSwitchToNightTheme));
                    }
                }
            };
            this.darkThemeView = rLottieImageView;
            rLottieImageView.setAnimation(rLottieDrawable);
            rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
            rLottieImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.QrActivity$ThemeListViewController$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    QrActivity.ThemeListViewController.this.lambda$new$0(view);
                }
            });
            rLottieImageView.setAlpha(0.0f);
            rLottieImageView.setVisibility(4);
            frameLayout.addView(rLottieImageView, LayoutHelper.createFrame(44, 44.0f, 8388661, 0.0f, -2.0f, 7.0f, 0.0f));
            FlickerLoadingView flickerLoadingView = new FlickerLoadingView(parentActivity, baseFragment.getResourceProvider());
            this.progressView = flickerLoadingView;
            flickerLoadingView.setVisibility(0);
            frameLayout.addView(flickerLoadingView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
            Point point = AndroidUtilities.displaySize;
            this.prevIsPortrait = point.x < point.y;
            RecyclerListView recyclerListView = new RecyclerListView(parentActivity);
            this.recyclerView = recyclerListView;
            ChatThemeBottomSheet.Adapter adapter = new ChatThemeBottomSheet.Adapter(((BaseFragment) QrActivity.this).currentAccount, QrActivity.this.resourcesProvider, 2);
            this.adapter = adapter;
            recyclerListView.setAdapter(adapter);
            recyclerListView.setClipChildren(false);
            recyclerListView.setClipToPadding(false);
            recyclerListView.setItemAnimator(null);
            recyclerListView.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = getLayoutManager(this.prevIsPortrait);
            this.layoutManager = layoutManager;
            recyclerListView.setLayoutManager(layoutManager);
            recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.QrActivity$ThemeListViewController$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i2) {
                    QrActivity.ThemeListViewController.this.onItemClicked(view, i2);
                }
            });
            recyclerListView.setOnScrollListener(new RecyclerView.OnScrollListener(QrActivity.this) { // from class: org.telegram.ui.QrActivity.ThemeListViewController.4
                private int yScroll = 0;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                    super.onScrolled(recyclerView, i2, i3);
                    this.yScroll += i3;
                    ThemeListViewController.this.topShadow.setAlpha((this.yScroll * 1.0f) / AndroidUtilities.dp(6.0f));
                }
            });
            frameLayout.addView(recyclerListView);
            View view = new View(parentActivity);
            this.topShadow = view;
            view.setAlpha(0.0f);
            int i2 = R.drawable.shadowdown;
            view.setBackground(ContextCompat.getDrawable(parentActivity, i2));
            view.setRotation(180.0f);
            frameLayout.addView(view);
            View view2 = new View(parentActivity);
            this.bottomShadow = view2;
            view2.setBackground(ContextCompat.getDrawable(parentActivity, i2));
            frameLayout.addView(view2);
            TextView textView2 = new TextView(parentActivity);
            this.shareButton = textView2;
            textView2.setBackground(Theme.AdaptiveRipple.filledRect(baseFragment.getThemedColor("featuredStickers_addButton"), 6.0f));
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setGravity(17);
            textView2.setLines(1);
            textView2.setSingleLine(true);
            textView2.setText(LocaleController.getString("ShareQrCode", R.string.ShareQrCode));
            textView2.setTextColor(baseFragment.getThemedColor("featuredStickers_buttonText"));
            textView2.setTextSize(1, 15.0f);
            textView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            frameLayout.addView(textView2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            if (this.changeDayNightViewAnimator != null) {
                return;
            }
            setupLightDarkTheme(!this.forceDark);
        }

        public void onCreate() {
            ChatThemeController.preloadAllWallpaperThumbs(true);
            ChatThemeController.preloadAllWallpaperThumbs(false);
            ChatThemeController.preloadAllWallpaperImages(true);
            ChatThemeController.preloadAllWallpaperImages(false);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        @SuppressLint({"NotifyDataSetChanged"})
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.emojiLoaded) {
                this.adapter.notifyDataSetChanged();
            }
        }

        public void onDestroy() {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        }

        public void setItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            this.itemSelectedListener = onItemSelectedListener;
        }

        public void onDataLoaded() {
            this.darkThemeView.setAlpha(0.0f);
            this.darkThemeView.animate().alpha(1.0f).setDuration(150L).start();
            this.darkThemeView.setVisibility(0);
            this.progressView.animate().alpha(0.0f).setListener(new HideViewAfterAnimation(this.progressView)).setDuration(150L).start();
            this.recyclerView.setAlpha(0.0f);
            this.recyclerView.animate().alpha(1.0f).setDuration(150L).start();
        }

        public void setSelectedPosition(int i) {
            this.prevSelectedPosition = i;
            this.adapter.setSelectedItem(i);
            if (i > 0 && i < this.adapter.items.size() / 2) {
                i--;
            }
            this.layoutManager.scrollToPositionWithOffset(Math.min(i, this.adapter.items.size() - 1), 0);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onItemClicked(View view, final int i) {
            if (this.adapter.items.get(i) == this.selectedItem || this.changeDayNightView != null) {
                return;
            }
            this.isLightDarkChangeAnimation = false;
            this.selectedItem = this.adapter.items.get(i);
            this.adapter.setSelectedItem(i);
            this.rootLayout.postDelayed(new Runnable() { // from class: org.telegram.ui.QrActivity$ThemeListViewController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.ThemeListViewController.this.lambda$onItemClicked$1(i);
                }
            }, 100L);
            for (int i2 = 0; i2 < this.recyclerView.getChildCount(); i2++) {
                ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) this.recyclerView.getChildAt(i2);
                if (themeSmallPreviewView != view) {
                    themeSmallPreviewView.cancelAnimation();
                }
            }
            if (!this.adapter.items.get(i).chatTheme.showAsDefaultStub) {
                ((ThemeSmallPreviewView) view).playEmojiAnimation();
            }
            OnItemSelectedListener onItemSelectedListener = this.itemSelectedListener;
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(this.selectedItem.chatTheme, i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClicked$1(int i) {
            int max;
            RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
            if (layoutManager != null) {
                if (i > this.prevSelectedPosition) {
                    max = Math.min(i + 1, this.adapter.items.size() - 1);
                } else {
                    max = Math.max(i - 1, 0);
                }
                this.scroller.setTargetPosition(max);
                layoutManager.startSmoothScroll(this.scroller);
            }
            this.prevSelectedPosition = i;
        }

        @SuppressLint({"NotifyDataSetChanged"})
        private void setupLightDarkTheme(final boolean z) {
            ValueAnimator valueAnimator = this.changeDayNightViewAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            FrameLayout frameLayout = (FrameLayout) this.window.getDecorView();
            final Bitmap createBitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(createBitmap);
            this.darkThemeView.setAlpha(0.0f);
            ((FrameLayout) this.fragment.getParentActivity().getWindow().getDecorView()).draw(canvas);
            frameLayout.draw(canvas);
            this.darkThemeView.setAlpha(1.0f);
            final Paint paint = new Paint(1);
            paint.setColor(-16777216);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            final Paint paint2 = new Paint(1);
            paint2.setFilterBitmap(true);
            int[] iArr = new int[2];
            this.darkThemeView.getLocationInWindow(iArr);
            final float f = iArr[0];
            final float f2 = iArr[1];
            final float measuredWidth = f + (this.darkThemeView.getMeasuredWidth() / 2.0f);
            final float measuredHeight = f2 + (this.darkThemeView.getMeasuredHeight() / 2.0f);
            final float max = Math.max(createBitmap.getHeight(), createBitmap.getWidth()) * 0.9f;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            paint2.setShader(new BitmapShader(createBitmap, tileMode, tileMode));
            this.changeDayNightView = new View(this.fragment.getParentActivity()) { // from class: org.telegram.ui.QrActivity.ThemeListViewController.5
                @Override // android.view.View
                protected void onDraw(Canvas canvas2) {
                    super.onDraw(canvas2);
                    if (z) {
                        if (ThemeListViewController.this.changeDayNightViewProgress > 0.0f) {
                            canvas.drawCircle(measuredWidth, measuredHeight, max * ThemeListViewController.this.changeDayNightViewProgress, paint);
                        }
                        canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
                    } else {
                        canvas2.drawCircle(measuredWidth, measuredHeight, max * (1.0f - ThemeListViewController.this.changeDayNightViewProgress), paint2);
                    }
                    canvas2.save();
                    canvas2.translate(f, f2);
                    ThemeListViewController.this.darkThemeView.draw(canvas2);
                    canvas2.restore();
                }
            };
            this.changeDayNightViewProgress = 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.changeDayNightViewAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.QrActivity$ThemeListViewController$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    QrActivity.ThemeListViewController.this.lambda$setupLightDarkTheme$2(valueAnimator2);
                }
            });
            this.changeDayNightViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.QrActivity.ThemeListViewController.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ThemeListViewController.this.changeDayNightView != null) {
                        if (ThemeListViewController.this.changeDayNightView.getParent() != null) {
                            ((ViewGroup) ThemeListViewController.this.changeDayNightView.getParent()).removeView(ThemeListViewController.this.changeDayNightView);
                        }
                        ThemeListViewController.this.changeDayNightView = null;
                    }
                    ThemeListViewController.this.changeDayNightViewAnimator = null;
                    super.onAnimationEnd(animator);
                }
            });
            this.changeDayNightViewAnimator.setDuration(400L);
            this.changeDayNightViewAnimator.setInterpolator(Easings.easeInOutQuad);
            this.changeDayNightViewAnimator.start();
            frameLayout.addView(this.changeDayNightView, new ViewGroup.LayoutParams(-1, -1));
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.QrActivity$ThemeListViewController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    QrActivity.ThemeListViewController.this.lambda$setupLightDarkTheme$3(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setupLightDarkTheme$2(ValueAnimator valueAnimator) {
            this.changeDayNightViewProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.changeDayNightView.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setupLightDarkTheme$3(boolean z) {
            ChatThemeBottomSheet.Adapter adapter = this.adapter;
            if (adapter == null || adapter.items == null) {
                return;
            }
            setForceDark(z, true);
            if (this.selectedItem != null) {
                this.isLightDarkChangeAnimation = true;
                setDarkTheme(z);
            }
            if (this.adapter.items != null) {
                for (int i = 0; i < this.adapter.items.size(); i++) {
                    this.adapter.items.get(i).themeIndex = z ? 1 : 0;
                    this.adapter.items.get(i).icon = QrActivity.this.getEmojiThemeIcon(this.adapter.items.get(i).chatTheme, z);
                }
                QrActivity.this.tempMotionDrawable = null;
                this.adapter.notifyDataSetChanged();
            }
        }

        public void setForceDark(boolean z, boolean z2) {
            if (this.forceDark == z) {
                return;
            }
            this.forceDark = z;
            int framesCount = z ? this.darkThemeDrawable.getFramesCount() - 1 : 0;
            if (z2) {
                this.darkThemeDrawable.setCustomEndFrame(framesCount);
                RLottieImageView rLottieImageView = this.darkThemeView;
                if (rLottieImageView != null) {
                    rLottieImageView.playAnimation();
                    return;
                }
                return;
            }
            this.darkThemeDrawable.setCustomEndFrame(framesCount);
            this.darkThemeDrawable.setCurrentFrame(framesCount, false, true);
            RLottieImageView rLottieImageView2 = this.darkThemeView;
            if (rLottieImageView2 != null) {
                rLottieImageView2.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public LinearLayoutManager getLayoutManager(boolean z) {
            if (z) {
                return new LinearLayoutManager(this.fragment.getParentActivity(), 0, false);
            }
            return new GridLayoutManager(this.fragment.getParentActivity(), 3, 1, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAnimationStart() {
            List<ChatThemeBottomSheet.ChatThemeItem> list;
            ChatThemeBottomSheet.Adapter adapter = this.adapter;
            if (adapter != null && (list = adapter.items) != null) {
                for (ChatThemeBottomSheet.ChatThemeItem chatThemeItem : list) {
                    chatThemeItem.themeIndex = this.forceDark ? 1 : 0;
                }
            }
            if (this.isLightDarkChangeAnimation) {
                return;
            }
            setItemsAnimationProgress(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setItemsAnimationProgress(float f) {
            for (int i = 0; i < this.adapter.getItemCount(); i++) {
                this.adapter.items.get(i).animationProgress = f;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAnimationEnd() {
            this.isLightDarkChangeAnimation = false;
        }

        public ArrayList<ThemeDescription> getThemeDescriptions() {
            ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.QrActivity.ThemeListViewController.7
                private boolean isAnimationStarted = false;

                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public void didSetColor() {
                }

                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public void onAnimationProgress(float f) {
                    if (f == 0.0f && !this.isAnimationStarted) {
                        ThemeListViewController.this.onAnimationStart();
                        this.isAnimationStarted = true;
                    }
                    ThemeListViewController.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(ThemeListViewController.this.fragment.getThemedColor("featuredStickers_addButton"), PorterDuff.Mode.MULTIPLY));
                    ThemeListViewController themeListViewController = ThemeListViewController.this;
                    if (themeListViewController.isLightDarkChangeAnimation) {
                        themeListViewController.setItemsAnimationProgress(f);
                    }
                    if (f == 1.0f && this.isAnimationStarted) {
                        ThemeListViewController themeListViewController2 = ThemeListViewController.this;
                        themeListViewController2.isLightDarkChangeAnimation = false;
                        themeListViewController2.onAnimationEnd();
                        this.isAnimationStarted = false;
                    }
                }
            };
            ArrayList<ThemeDescription> arrayList = new ArrayList<>();
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUND, null, this.backgroundPaint, null, null, "dialogBackground"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{this.backgroundDrawable}, themeDescriptionDelegate, "dialogBackground"));
            arrayList.add(new ThemeDescription(this.titleView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogTextBlack"));
            arrayList.add(new ThemeDescription(this.recyclerView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ThemeSmallPreviewView.class}, null, null, null, "dialogBackgroundGray"));
            Iterator<ThemeDescription> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().resourcesProvider = this.fragment.getResourceProvider();
            }
            return arrayList;
        }
    }
}
