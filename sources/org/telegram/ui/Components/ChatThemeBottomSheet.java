package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.StatisticActivity;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.WallpapersListActivity;

/* loaded from: classes3.dex */
public class ChatThemeBottomSheet extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private final Adapter adapter;
    private final View applyButton;
    private AnimatedTextView applySubTextView;
    private AnimatedTextView applyTextView;
    private final BackDrawable backButtonDrawable;
    private final ImageView backButtonView;
    private TL_stories.TL_premium_boostsStatus boostsStatus;
    private TextView cancelOrResetTextView;
    private View changeDayNightView;
    private ValueAnimator changeDayNightViewAnimator;
    private float changeDayNightViewProgress;
    private final ChatActivity chatActivity;
    public ChatAttachAlert chatAttachAlert;
    private FrameLayout chatAttachButton;
    private AnimatedTextView chatAttachButtonText;
    private boolean checkedBoostsLevel;
    private boolean checkingBoostsLevel;
    private TextView chooseBackgroundTextView;
    private EmojiThemes currentTheme;
    private TLRPC.WallPaper currentWallpaper;
    private final RLottieDrawable darkThemeDrawable;
    private final RLottieImageView darkThemeView;
    private boolean dataLoaded;
    private boolean forceDark;
    HintView hintView;
    private boolean isApplyClicked;
    private boolean isLightDarkChangeAnimation;
    private final LinearLayoutManager layoutManager;
    private ColoredImageSpan lockSpan;
    private final boolean originalIsDark;
    private final EmojiThemes originalTheme;
    BaseFragment overlayFragment;
    private int prevSelectedPosition;
    private final FlickerLoadingView progressView;
    private final RecyclerListView recyclerView;
    private FrameLayout rootLayout;
    private final LinearSmoothScroller scroller;
    private ChatThemeItem selectedItem;
    private float subTextTranslation;
    private ValueAnimator subTextTranslationAnimator;
    private final ChatActivity.ThemeDelegate themeDelegate;
    private TextView themeHintTextView;
    private final TextView titleView;

    class 11 implements ChatAttachAlert.ChatAttachViewDelegate {
        long start;
        final /* synthetic */ TL_stories.TL_premium_boostsStatus val$cachedBoostsStatus;
        final /* synthetic */ ChatAttachAlert val$chatAttachAlert;
        final /* synthetic */ long val$dialogId;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ Utilities.Callback val$onSet;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ ThemePreviewActivity.DayNightSwitchDelegate val$toggleTheme;

        11(ChatAttachAlert chatAttachAlert, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, Theme.ResourcesProvider resourcesProvider, ThemePreviewActivity.DayNightSwitchDelegate dayNightSwitchDelegate, long j, Utilities.Callback callback, BaseFragment baseFragment) {
            this.val$chatAttachAlert = chatAttachAlert;
            this.val$cachedBoostsStatus = tL_premium_boostsStatus;
            this.val$resourcesProvider = resourcesProvider;
            this.val$toggleTheme = dayNightSwitchDelegate;
            this.val$dialogId = j;
            this.val$onSet = callback;
            this.val$fragment = baseFragment;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$didPressedButton$0(ChatAttachAlert chatAttachAlert, Utilities.Callback callback, TLRPC.WallPaper wallPaper) {
            chatAttachAlert.dismissInternal();
            if (callback != null) {
                callback.run(wallPaper);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onWallpaperSelected$1(ChatAttachAlert chatAttachAlert, Utilities.Callback callback, TLRPC.WallPaper wallPaper) {
            chatAttachAlert.dismissInternal();
            if (callback != null) {
                callback.run(wallPaper);
            }
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void didPressedButton(int i, boolean z, boolean z2, int i2, long j, boolean z3, boolean z4) {
            try {
                HashMap<Object, Object> selectedPhotos = this.val$chatAttachAlert.getPhotoLayout().getSelectedPhotos();
                if (selectedPhotos.isEmpty()) {
                    return;
                }
                MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.values().iterator().next();
                String str = photoEntry.imagePath;
                if (str == null) {
                    str = photoEntry.path;
                }
                if (str != null) {
                    File file = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                    android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
                    Bitmap loadBitmap = ImageLoader.loadBitmap(str, null, (float) realScreenSize.x, (float) realScreenSize.y, true);
                    loadBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file));
                    ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(new WallpapersListActivity.FileWallpaper("", file, file), loadBitmap) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.11.1
                        @Override // org.telegram.ui.ThemePreviewActivity
                        public boolean insideBottomSheet() {
                            return true;
                        }
                    };
                    themePreviewActivity.boostsStatus = this.val$cachedBoostsStatus;
                    themePreviewActivity.setResourceProvider(this.val$resourcesProvider);
                    themePreviewActivity.setOnSwitchDayNightDelegate(this.val$toggleTheme);
                    themePreviewActivity.setInitialModes(false, false, 0.2f);
                    themePreviewActivity.setDialogId(this.val$dialogId);
                    final ChatAttachAlert chatAttachAlert = this.val$chatAttachAlert;
                    final Utilities.Callback callback = this.val$onSet;
                    themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$11$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                        public final void didSetNewBackground(TLRPC.WallPaper wallPaper) {
                            ChatThemeBottomSheet.11.lambda$didPressedButton$0(ChatAttachAlert.this, callback, wallPaper);
                        }
                    });
                    BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
                    bottomSheetParams.transitionFromLeft = true;
                    bottomSheetParams.allowNestedScroll = false;
                    bottomSheetParams.occupyNavigationBar = true;
                    this.val$fragment.showAsSheet(themePreviewActivity, bottomSheetParams);
                    this.val$chatAttachAlert.lambda$new$0();
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void didSelectBot(TLRPC.User user) {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$didSelectBot(this, user);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void doOnIdle(Runnable runnable) {
            runnable.run();
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ boolean needEnterComment() {
            return ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$needEnterComment(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void onCameraOpened() {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$onCameraOpened(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void onWallpaperSelected(Object obj) {
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(obj, null, true, false) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.11.2
                @Override // org.telegram.ui.ThemePreviewActivity
                public boolean insideBottomSheet() {
                    return true;
                }
            };
            themePreviewActivity.boostsStatus = this.val$cachedBoostsStatus;
            themePreviewActivity.setResourceProvider(this.val$resourcesProvider);
            themePreviewActivity.setOnSwitchDayNightDelegate(this.val$toggleTheme);
            themePreviewActivity.setDialogId(this.val$dialogId);
            final ChatAttachAlert chatAttachAlert = this.val$chatAttachAlert;
            final Utilities.Callback callback = this.val$onSet;
            themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$11$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                public final void didSetNewBackground(TLRPC.WallPaper wallPaper) {
                    ChatThemeBottomSheet.11.lambda$onWallpaperSelected$1(ChatAttachAlert.this, callback, wallPaper);
                }
            });
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            bottomSheetParams.occupyNavigationBar = true;
            this.val$fragment.showAsSheet(themePreviewActivity, bottomSheetParams);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void openAvatarsSearch() {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$openAvatarsSearch(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public boolean selectItemOnClicking() {
            this.start = System.currentTimeMillis();
            return true;
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$sendAudio(this, arrayList, charSequence, z, i, j, z2);
        }
    }

    class 12 implements ChatAttachAlert.ChatAttachViewDelegate {
        long start;

        12() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didPressedButton$0(TLRPC.WallPaper wallPaper) {
            ChatThemeBottomSheet.this.chatAttachAlert.dismissInternal();
            ChatThemeBottomSheet.this.lambda$new$0();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWallpaperSelected$1(TLRPC.WallPaper wallPaper) {
            ChatThemeBottomSheet.this.chatAttachAlert.dismissInternal();
            ChatThemeBottomSheet.this.lambda$new$0();
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void didPressedButton(int i, boolean z, boolean z2, int i2, long j, boolean z3, boolean z4) {
            try {
                HashMap<Object, Object> selectedPhotos = ChatThemeBottomSheet.this.chatAttachAlert.getPhotoLayout().getSelectedPhotos();
                if (selectedPhotos.isEmpty()) {
                    return;
                }
                MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.values().iterator().next();
                String str = photoEntry.imagePath;
                if (str == null) {
                    str = photoEntry.path;
                }
                if (str != null) {
                    File file = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                    android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
                    Bitmap loadBitmap = ImageLoader.loadBitmap(str, null, (float) realScreenSize.x, (float) realScreenSize.y, true);
                    loadBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file));
                    ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(new WallpapersListActivity.FileWallpaper("", file, file), loadBitmap) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.12.1
                        @Override // org.telegram.ui.ThemePreviewActivity
                        public boolean insideBottomSheet() {
                            return true;
                        }
                    };
                    themePreviewActivity.boostsStatus = ChatThemeBottomSheet.this.boostsStatus;
                    themePreviewActivity.setInitialModes(false, false, 0.2f);
                    themePreviewActivity.setDialogId(ChatThemeBottomSheet.this.chatActivity.getDialogId());
                    themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$12$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                        public final void didSetNewBackground(TLRPC.WallPaper wallPaper) {
                            ChatThemeBottomSheet.12.this.lambda$didPressedButton$0(wallPaper);
                        }
                    });
                    ChatThemeBottomSheet.this.showAsSheet(themePreviewActivity);
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void didSelectBot(TLRPC.User user) {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$didSelectBot(this, user);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void doOnIdle(Runnable runnable) {
            runnable.run();
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ boolean needEnterComment() {
            return ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$needEnterComment(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void onCameraOpened() {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$onCameraOpened(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void onWallpaperSelected(Object obj) {
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(obj, null, true, false) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.12.2
                @Override // org.telegram.ui.ThemePreviewActivity
                public boolean insideBottomSheet() {
                    return true;
                }
            };
            themePreviewActivity.boostsStatus = ChatThemeBottomSheet.this.boostsStatus;
            themePreviewActivity.setDialogId(ChatThemeBottomSheet.this.chatActivity.getDialogId());
            themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$12$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                public final void didSetNewBackground(TLRPC.WallPaper wallPaper) {
                    ChatThemeBottomSheet.12.this.lambda$onWallpaperSelected$1(wallPaper);
                }
            });
            ChatThemeBottomSheet.this.showAsSheet(themePreviewActivity);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void openAvatarsSearch() {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$openAvatarsSearch(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public boolean selectItemOnClicking() {
            this.start = System.currentTimeMillis();
            return true;
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
            ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$sendAudio(this, arrayList, charSequence, z, i, j, z2);
        }
    }

    class 6 implements ResultCallback {
        6() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onComplete$0(List list) {
            ChatThemeBottomSheet.this.onDataLoaded(list);
        }

        @Override // org.telegram.tgnet.ResultCallback
        public void onComplete(final List list) {
            if (list != null && !list.isEmpty()) {
                ChatThemeBottomSheet.this.themeDelegate.setCachedThemes(list);
            }
            NotificationCenter.getInstance(((BottomSheet) ChatThemeBottomSheet.this).currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatThemeBottomSheet.6.this.lambda$onComplete$0(list);
                }
            });
        }

        @Override // org.telegram.tgnet.ResultCallback
        public /* synthetic */ void onError(Throwable th) {
            ResultCallback.-CC.$default$onError(this, th);
        }

        @Override // org.telegram.tgnet.ResultCallback
        public void onError(TLRPC.TL_error tL_error) {
            Toast.makeText(ChatThemeBottomSheet.this.getContext(), tL_error.text, 0).show();
        }
    }

    public static class Adapter extends RecyclerListView.SelectionAdapter {
        private final int currentAccount;
        private final int currentViewType;
        public List items;
        private final Theme.ResourcesProvider resourcesProvider;
        private WeakReference selectedViewRef;
        private int selectedItemPosition = -1;
        private HashMap loadingThemes = new HashMap();
        private HashMap loadingWallpapers = new HashMap();

        public Adapter(int i, Theme.ResourcesProvider resourcesProvider, int i2) {
            this.currentViewType = i2;
            this.resourcesProvider = resourcesProvider;
            this.currentAccount = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$0(TLObject tLObject, Theme.ThemeInfo themeInfo) {
            if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
                themeInfo.badWallpaper = true;
                return;
            }
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) tLObject;
            String attachFileName = FileLoader.getAttachFileName(wallPaper.document);
            if (this.loadingThemes.containsKey(attachFileName)) {
                return;
            }
            this.loadingThemes.put(attachFileName, themeInfo);
            FileLoader.getInstance(themeInfo.account).loadFile(wallPaper.document, wallPaper, 1, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$1(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatThemeBottomSheet.Adapter.this.lambda$parseTheme$0(tLObject, themeInfo);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:96:0x01c9 A[Catch: all -> 0x00a3, TryCatch #2 {all -> 0x00a3, blocks: (B:10:0x001c, B:14:0x002a, B:16:0x0032, B:18:0x0045, B:20:0x0083, B:22:0x008f, B:25:0x0093, B:27:0x0096, B:31:0x00a0, B:29:0x00a7, B:33:0x00aa, B:40:0x00b6, B:42:0x00c2, B:44:0x00dc, B:46:0x00e6, B:47:0x00f3, B:49:0x00fb, B:51:0x0105, B:52:0x0113, B:54:0x011b, B:56:0x0125, B:36:0x01f1, B:38:0x01f5, B:58:0x0133, B:60:0x0140, B:61:0x014a, B:63:0x0156, B:64:0x0160, B:66:0x0164, B:71:0x016b, B:116:0x0173, B:73:0x0179, B:75:0x0182, B:77:0x018f, B:79:0x0193, B:81:0x0197, B:83:0x019b, B:85:0x019f, B:87:0x01a3, B:89:0x01af, B:93:0x01b8, B:94:0x01c5, B:96:0x01c9, B:97:0x01cd, B:99:0x01d1, B:100:0x01d5, B:102:0x01d9, B:103:0x01dd, B:105:0x01e1, B:106:0x01e4, B:108:0x01e8, B:109:0x01eb, B:111:0x01ef, B:114:0x01bd, B:119:0x0200), top: B:9:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:97:0x01cd A[Catch: all -> 0x00a3, TryCatch #2 {all -> 0x00a3, blocks: (B:10:0x001c, B:14:0x002a, B:16:0x0032, B:18:0x0045, B:20:0x0083, B:22:0x008f, B:25:0x0093, B:27:0x0096, B:31:0x00a0, B:29:0x00a7, B:33:0x00aa, B:40:0x00b6, B:42:0x00c2, B:44:0x00dc, B:46:0x00e6, B:47:0x00f3, B:49:0x00fb, B:51:0x0105, B:52:0x0113, B:54:0x011b, B:56:0x0125, B:36:0x01f1, B:38:0x01f5, B:58:0x0133, B:60:0x0140, B:61:0x014a, B:63:0x0156, B:64:0x0160, B:66:0x0164, B:71:0x016b, B:116:0x0173, B:73:0x0179, B:75:0x0182, B:77:0x018f, B:79:0x0193, B:81:0x0197, B:83:0x019b, B:85:0x019f, B:87:0x01a3, B:89:0x01af, B:93:0x01b8, B:94:0x01c5, B:96:0x01c9, B:97:0x01cd, B:99:0x01d1, B:100:0x01d5, B:102:0x01d9, B:103:0x01dd, B:105:0x01e1, B:106:0x01e4, B:108:0x01e8, B:109:0x01eb, B:111:0x01ef, B:114:0x01bd, B:119:0x0200), top: B:9:0x001c, outer: #0 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean parseTheme(final Theme.ThemeInfo themeInfo) {
            int stringKeyToInt;
            int intValue;
            String[] split;
            if (themeInfo == null || themeInfo.pathToFile == null) {
                return false;
            }
            boolean z = true;
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(themeInfo.pathToFile));
                int i = 0;
                boolean z2 = false;
                while (true) {
                    try {
                        int read = fileInputStream.read(ThemesHorizontalListCell.bytes);
                        if (read == -1) {
                            break;
                        }
                        int i2 = i;
                        int i3 = 0;
                        int i4 = 0;
                        while (true) {
                            if (i3 >= read) {
                                break;
                            }
                            byte[] bArr = ThemesHorizontalListCell.bytes;
                            if (bArr[i3] == 10) {
                                int i5 = i3 - i4;
                                int i6 = i5 + 1;
                                String str = new String(bArr, i4, i5, "UTF-8");
                                if (str.startsWith("WLS=")) {
                                    String substring = str.substring(4);
                                    Uri parse = Uri.parse(substring);
                                    themeInfo.slug = parse.getQueryParameter("slug");
                                    themeInfo.pathToWallpaper = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(substring) + ".wp").getAbsolutePath();
                                    String queryParameter = parse.getQueryParameter("mode");
                                    if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                                        int i7 = 0;
                                        while (true) {
                                            if (i7 >= split.length) {
                                                break;
                                            }
                                            if ("blur".equals(split[i7])) {
                                                themeInfo.isBlured = z;
                                                break;
                                            }
                                            i7++;
                                        }
                                    }
                                    if (!TextUtils.isEmpty(parse.getQueryParameter("pattern"))) {
                                        try {
                                            String queryParameter2 = parse.getQueryParameter("bg_color");
                                            if (!TextUtils.isEmpty(queryParameter2)) {
                                                themeInfo.patternBgColor = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                                if (queryParameter2.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(6))) {
                                                    themeInfo.patternBgGradientColor1 = Integer.parseInt(queryParameter2.substring(7, 13), 16) | (-16777216);
                                                }
                                                if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                    themeInfo.patternBgGradientColor2 = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                                }
                                                if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                    themeInfo.patternBgGradientColor3 = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                                }
                                            }
                                        } catch (Exception unused) {
                                        }
                                        try {
                                            String queryParameter3 = parse.getQueryParameter("rotation");
                                            if (!TextUtils.isEmpty(queryParameter3)) {
                                                themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                                            }
                                        } catch (Exception unused2) {
                                        }
                                        String queryParameter4 = parse.getQueryParameter("intensity");
                                        if (!TextUtils.isEmpty(queryParameter4)) {
                                            themeInfo.patternIntensity = Utilities.parseInt((CharSequence) queryParameter4).intValue();
                                        }
                                        if (themeInfo.patternIntensity == 0) {
                                            themeInfo.patternIntensity = 50;
                                        }
                                    }
                                } else {
                                    if (str.startsWith("WPS")) {
                                        themeInfo.previewWallpaperOffset = i6 + i2;
                                        z2 = true;
                                        break;
                                    }
                                    int indexOf = str.indexOf(61);
                                    if (indexOf != -1 && ((stringKeyToInt = ThemeColors.stringKeyToInt(str.substring(0, indexOf))) == Theme.key_chat_inBubble || stringKeyToInt == Theme.key_chat_outBubble || stringKeyToInt == Theme.key_chat_wallpaper || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3)) {
                                        String substring2 = str.substring(indexOf + 1);
                                        if (substring2.length() > 0 && substring2.charAt(0) == '#') {
                                            try {
                                                intValue = Color.parseColor(substring2);
                                            } catch (Exception unused3) {
                                            }
                                            if (stringKeyToInt != Theme.key_chat_inBubble) {
                                                themeInfo.setPreviewInColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_outBubble) {
                                                themeInfo.setPreviewOutColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper) {
                                                themeInfo.setPreviewBackgroundColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1) {
                                                themeInfo.previewBackgroundGradientColor1 = intValue;
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2) {
                                                themeInfo.previewBackgroundGradientColor2 = intValue;
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3) {
                                                themeInfo.previewBackgroundGradientColor3 = intValue;
                                            }
                                        }
                                        intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                        if (stringKeyToInt != Theme.key_chat_inBubble) {
                                        }
                                    }
                                }
                                i4 += i6;
                                i2 += i6;
                            }
                            i3++;
                            z = true;
                        }
                        if (z2 || i == i2) {
                            break;
                        }
                        fileInputStream.getChannel().position(i2);
                        i = i2;
                        z = true;
                    } finally {
                    }
                }
                fileInputStream.close();
            } catch (Throwable th) {
                FileLog.e(th);
            }
            if (themeInfo.pathToWallpaper == null || themeInfo.badWallpaper || new File(themeInfo.pathToWallpaper).exists()) {
                themeInfo.previewParsed = true;
                return true;
            }
            if (this.loadingWallpapers.containsKey(themeInfo)) {
                return false;
            }
            this.loadingWallpapers.put(themeInfo, themeInfo.slug);
            TL_account.getWallPaper getwallpaper = new TL_account.getWallPaper();
            TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
            tL_inputWallPaperSlug.slug = themeInfo.slug;
            getwallpaper.wallpaper = tL_inputWallPaperSlug;
            ConnectionsManager.getInstance(themeInfo.account).sendRequest(getwallpaper, new RequestDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChatThemeBottomSheet.Adapter.this.lambda$parseTheme$1(themeInfo, tLObject, tL_error);
                }
            });
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List list = this.items;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) viewHolder.itemView;
            Theme.ThemeInfo themeInfo = ((ChatThemeItem) this.items.get(i)).chatTheme.getThemeInfo(((ChatThemeItem) this.items.get(i)).themeIndex);
            if (themeInfo != null && themeInfo.pathToFile != null && !themeInfo.previewParsed && new File(themeInfo.pathToFile).exists()) {
                parseTheme(themeInfo);
            }
            ChatThemeItem chatThemeItem = (ChatThemeItem) this.items.get(i);
            ChatThemeItem chatThemeItem2 = themeSmallPreviewView.chatThemeItem;
            boolean z = chatThemeItem2 != null && chatThemeItem2.chatTheme.getEmoticon().equals(chatThemeItem.chatTheme.getEmoticon()) && !DrawerProfileCell.switchingTheme && themeSmallPreviewView.lastThemeIndex == chatThemeItem.themeIndex;
            themeSmallPreviewView.setFocusable(true);
            themeSmallPreviewView.setEnabled(true);
            themeSmallPreviewView.setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray));
            themeSmallPreviewView.setItem(chatThemeItem, z);
            themeSmallPreviewView.setSelected(i == this.selectedItemPosition, z);
            if (i == this.selectedItemPosition) {
                this.selectedViewRef = new WeakReference(themeSmallPreviewView);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new ThemeSmallPreviewView(viewGroup.getContext(), this.currentAccount, this.resourcesProvider, this.currentViewType));
        }

        public void setItems(List list) {
            this.items = list;
            notifyDataSetChanged();
        }

        public void setSelectedItem(int i) {
            int i2 = this.selectedItemPosition;
            if (i2 == i) {
                return;
            }
            if (i2 >= 0) {
                notifyItemChanged(i2);
                WeakReference weakReference = this.selectedViewRef;
                ThemeSmallPreviewView themeSmallPreviewView = weakReference == null ? null : (ThemeSmallPreviewView) weakReference.get();
                if (themeSmallPreviewView != null) {
                    themeSmallPreviewView.setSelected(false);
                }
            }
            this.selectedItemPosition = i;
            notifyItemChanged(i);
        }
    }

    public static class ChatThemeItem {
        public float animationProgress = 1.0f;
        public final EmojiThemes chatTheme;
        public Bitmap icon;
        public boolean isSelected;
        public Drawable previewDrawable;
        public int themeIndex;

        public ChatThemeItem(EmojiThemes emojiThemes) {
            this.chatTheme = emojiThemes;
        }

        public String getEmoticon() {
            EmojiThemes emojiThemes = this.chatTheme;
            if (emojiThemes == null || emojiThemes.showAsDefaultStub) {
                return null;
            }
            return emojiThemes.getEmoticon();
        }
    }

    public ChatThemeBottomSheet(final ChatActivity chatActivity, ChatActivity.ThemeDelegate themeDelegate) {
        super(chatActivity.getParentActivity(), true, themeDelegate);
        TextView textView;
        int i;
        this.prevSelectedPosition = -1;
        this.checkingBoostsLevel = false;
        this.checkedBoostsLevel = false;
        this.subTextTranslation = 0.0f;
        this.chatActivity = chatActivity;
        this.themeDelegate = themeDelegate;
        this.originalTheme = themeDelegate.getCurrentTheme();
        this.currentWallpaper = themeDelegate.getCurrentWallpaper();
        this.originalIsDark = Theme.getActiveTheme().isDark();
        Adapter adapter = new Adapter(this.currentAccount, themeDelegate, 0);
        this.adapter = adapter;
        setDimBehind(false);
        setCanDismissWithSwipe(false);
        setApplyBottomPadding(false);
        if (Build.VERSION.SDK_INT >= 30) {
            this.navBarColorKey = -1;
            int i2 = Theme.key_dialogBackgroundGray;
            this.navBarColor = getThemedColor(i2);
            AndroidUtilities.setNavigationBarColor(getWindow(), getThemedColor(i2), false);
            AndroidUtilities.setLightNavigationBar(getWindow(), ((double) AndroidUtilities.computePerceivedBrightness(this.navBarColor)) > 0.721d);
        } else {
            fixNavigationBar(getThemedColor(Theme.key_dialogBackgroundGray));
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.rootLayout = frameLayout;
        setCustomView(frameLayout);
        TextView textView2 = new TextView(getContext());
        this.titleView = textView2;
        textView2.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView2.setLines(1);
        textView2.setSingleLine(true);
        textView2.setText(LocaleController.getString(R.string.SelectTheme));
        textView2.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f));
        ImageView imageView = new ImageView(getContext());
        this.backButtonView = imageView;
        int dp = AndroidUtilities.dp(10.0f);
        imageView.setPadding(dp, dp, dp, dp);
        BackDrawable backDrawable = new BackDrawable(false);
        this.backButtonDrawable = backDrawable;
        imageView.setImageDrawable(backDrawable);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatThemeBottomSheet.this.lambda$new$0(view);
            }
        });
        this.rootLayout.addView(imageView, LayoutHelper.createFrame(44, 44.0f, 8388659, 4.0f, -2.0f, 62.0f, 12.0f));
        this.rootLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 8388659, 44.0f, 0.0f, 62.0f, 0.0f));
        int i3 = Theme.key_featuredStickers_addButton;
        int themedColor = getThemedColor(i3);
        int dp2 = AndroidUtilities.dp(28.0f);
        int i4 = R.raw.sun_outline;
        RLottieDrawable rLottieDrawable = new RLottieDrawable(i4, "" + i4, dp2, dp2, false, null);
        this.darkThemeDrawable = rLottieDrawable;
        this.forceDark = Theme.getActiveTheme().isDark() ^ true;
        setForceDark(Theme.getActiveTheme().isDark(), false);
        rLottieDrawable.setAllowDecodeSingleFrame(true);
        rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
        rLottieDrawable.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.MULTIPLY));
        RLottieImageView rLottieImageView = new RLottieImageView(getContext()) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.1
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setText(LocaleController.getString(ChatThemeBottomSheet.this.forceDark ? R.string.AccDescrSwitchToDayTheme : R.string.AccDescrSwitchToNightTheme));
            }
        };
        this.darkThemeView = rLottieImageView;
        rLottieImageView.setAnimation(rLottieDrawable);
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        rLottieImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatThemeBottomSheet.this.lambda$new$1(view);
            }
        });
        this.rootLayout.addView(rLottieImageView, LayoutHelper.createFrame(44, 44.0f, 8388661, 0.0f, -2.0f, 7.0f, 0.0f));
        this.scroller = new LinearSmoothScroller(getContext()) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.2
            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            protected int calculateTimeForScrolling(int i5) {
                return super.calculateTimeForScrolling(i5) * 6;
            }
        };
        RecyclerListView recyclerListView = new RecyclerListView(getContext());
        this.recyclerView = recyclerListView;
        recyclerListView.setAdapter(adapter);
        recyclerListView.setDrawSelection(false);
        recyclerListView.setClipChildren(false);
        recyclerListView.setClipToPadding(false);
        recyclerListView.setHasFixedSize(true);
        recyclerListView.setItemAnimator(null);
        recyclerListView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        recyclerListView.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i5) {
                ChatThemeBottomSheet.this.lambda$new$2(view, i5);
            }
        });
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(getContext(), this.resourcesProvider);
        this.progressView = flickerLoadingView;
        flickerLoadingView.setViewType(14);
        flickerLoadingView.setVisibility(0);
        this.rootLayout.addView(flickerLoadingView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
        this.rootLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
        View view = new View(getContext());
        this.applyButton = view;
        view.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor(i3), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
        view.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatThemeBottomSheet.this.lambda$new$3(view2);
            }
        });
        this.rootLayout.addView(view, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        TextView textView3 = new TextView(getContext());
        this.chooseBackgroundTextView = textView3;
        TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
        textView3.setEllipsize(truncateAt);
        this.chooseBackgroundTextView.setGravity(17);
        this.chooseBackgroundTextView.setLines(1);
        this.chooseBackgroundTextView.setSingleLine(true);
        if (this.currentWallpaper == null) {
            textView = this.chooseBackgroundTextView;
            i = R.string.ChooseBackgroundFromGallery;
        } else {
            textView = this.chooseBackgroundTextView;
            i = R.string.ChooseANewWallpaper;
        }
        textView.setText(LocaleController.getString(i));
        this.chooseBackgroundTextView.setTextSize(1, 15.0f);
        this.chooseBackgroundTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ChatThemeBottomSheet.this.openGalleryForBackground();
            }
        });
        this.rootLayout.addView(this.chooseBackgroundTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        AnimatedTextView animatedTextView = new AnimatedTextView(getContext(), true, true, true);
        this.applyTextView = animatedTextView;
        animatedTextView.getDrawable().setEllipsizeByGradient(true);
        AnimatedTextView animatedTextView2 = this.applyTextView;
        animatedTextView2.adaptWidth = false;
        animatedTextView2.setGravity(17);
        AnimatedTextView animatedTextView3 = this.applyTextView;
        int i5 = Theme.key_featuredStickers_buttonText;
        animatedTextView3.setTextColor(getThemedColor(i5));
        this.applyTextView.setTextSize(AndroidUtilities.dp(15.0f));
        this.applyTextView.setTypeface(AndroidUtilities.bold());
        this.rootLayout.addView(this.applyTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        AnimatedTextView animatedTextView4 = new AnimatedTextView(getContext(), true, true, true);
        this.applySubTextView = animatedTextView4;
        animatedTextView4.getDrawable().setEllipsizeByGradient(true);
        AnimatedTextView animatedTextView5 = this.applySubTextView;
        animatedTextView5.adaptWidth = false;
        animatedTextView5.setGravity(17);
        this.applySubTextView.setTextColor(getThemedColor(i5));
        this.applySubTextView.setTextSize(AndroidUtilities.dp(12.0f));
        this.applySubTextView.setAlpha(0.0f);
        this.applySubTextView.setTranslationY(AndroidUtilities.dp(11.0f));
        this.rootLayout.addView(this.applySubTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        if (this.currentWallpaper != null) {
            TextView textView4 = new TextView(getContext());
            this.cancelOrResetTextView = textView4;
            textView4.setEllipsize(truncateAt);
            this.cancelOrResetTextView.setGravity(17);
            this.cancelOrResetTextView.setLines(1);
            this.cancelOrResetTextView.setSingleLine(true);
            this.cancelOrResetTextView.setText(LocaleController.getString(R.string.RestToDefaultBackground));
            this.cancelOrResetTextView.setTextSize(1, 15.0f);
            this.cancelOrResetTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatThemeBottomSheet.this.lambda$new$4(chatActivity, view2);
                }
            });
            this.rootLayout.addView(this.cancelOrResetTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 214.0f, 16.0f, 12.0f));
            TextView textView5 = new TextView(getContext());
            this.themeHintTextView = textView5;
            textView5.setEllipsize(truncateAt);
            this.themeHintTextView.setGravity(17);
            this.themeHintTextView.setLines(1);
            this.themeHintTextView.setSingleLine(true);
            this.themeHintTextView.setText(LocaleController.formatString("ChatThemeApplyHint", R.string.ChatThemeApplyHint, chatActivity.getCurrentUser() != null ? UserObject.getFirstName(chatActivity.getCurrentUser()) : chatActivity.getCurrentChat() != null ? chatActivity.getCurrentChat().title : ""));
            this.themeHintTextView.setTextSize(1, 15.0f);
            this.rootLayout.addView(this.themeHintTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 214.0f, 16.0f, 12.0f));
        }
        updateButtonColors();
        updateState(false);
    }

    private void applySelectedTheme() {
        boolean z;
        TextView textView;
        String formatString;
        if (this.checkingBoostsLevel) {
            return;
        }
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        if (tL_premium_boostsStatus != null && tL_premium_boostsStatus.level < this.chatActivity.getMessagesController().channelWallpaperLevelMin) {
            this.chatActivity.getMessagesController().getBoostsController().userCanBoostChannel(this.chatActivity.getDialogId(), this.boostsStatus, new Consumer() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda12
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ChatThemeBottomSheet.this.lambda$applySelectedTheme$14((ChannelBoostsController.CanApplyBoost) obj);
                }
            });
            return;
        }
        EmojiThemes emojiThemes = this.selectedItem.chatTheme;
        Bulletin bulletin = null;
        if (emojiThemes != this.currentTheme) {
            String emoticon = !emojiThemes.showAsDefaultStub ? emojiThemes.getEmoticon() : null;
            ChatThemeController.getInstance(this.currentAccount).clearWallpaper(this.chatActivity.getDialogId(), false);
            ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.chatActivity.getDialogId(), emoticon, true);
            TLRPC.WallPaper currentWallpaper = hasChanges() ? null : this.themeDelegate.getCurrentWallpaper();
            if (emojiThemes.showAsDefaultStub) {
                this.themeDelegate.setCurrentTheme(null, currentWallpaper, true, Boolean.valueOf(this.originalIsDark));
            } else {
                this.themeDelegate.setCurrentTheme(emojiThemes, currentWallpaper, true, Boolean.valueOf(this.originalIsDark));
            }
            this.isApplyClicked = true;
            TLRPC.User currentUser = this.chatActivity.getCurrentUser();
            if (currentUser != null && !currentUser.self) {
                if (TextUtils.isEmpty(emoticon)) {
                    emoticon = "❌";
                    z = true;
                } else {
                    z = false;
                }
                StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(getContext(), null, -1, emoticon != null ? MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(emoticon) : null, this.chatActivity.getResourceProvider());
                stickerSetBulletinLayout.subtitleTextView.setVisibility(8);
                if (z) {
                    textView = stickerSetBulletinLayout.titleTextView;
                    formatString = LocaleController.formatString("ThemeAlsoDisabledForHint", R.string.ThemeAlsoDisabledForHint, currentUser.first_name);
                } else {
                    textView = stickerSetBulletinLayout.titleTextView;
                    formatString = LocaleController.formatString("ThemeAlsoAppliedForHint", R.string.ThemeAlsoAppliedForHint, currentUser.first_name);
                }
                textView.setText(AndroidUtilities.replaceTags(formatString));
                stickerSetBulletinLayout.titleTextView.setTypeface(null);
                bulletin = Bulletin.make(this.chatActivity, stickerSetBulletinLayout, 2750);
            }
        }
        lambda$new$0();
        if (bulletin != null) {
            bulletin.show();
        }
    }

    private void checkBoostsLevel() {
        ChatActivity chatActivity = this.chatActivity;
        if (chatActivity == null || this.checkingBoostsLevel || this.checkedBoostsLevel || this.boostsStatus != null) {
            return;
        }
        this.checkingBoostsLevel = true;
        chatActivity.getMessagesController().getBoostsController().getBoostsStats(this.chatActivity.getDialogId(), new Consumer() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda13
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ChatThemeBottomSheet.this.lambda$checkBoostsLevel$5((TL_stories.TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: fixColorsAfterAnotherWindow, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$showAsSheet$20() {
        Boolean valueOf;
        boolean z;
        boolean z2;
        if (isDismissed() || this.isApplyClicked) {
            return;
        }
        Theme.disallowChangeServiceMessageColor = false;
        TLRPC.WallPaper currentWallpaper = hasChanges() ? null : this.themeDelegate.getCurrentWallpaper();
        EmojiThemes emojiThemes = this.selectedItem.chatTheme;
        boolean z3 = emojiThemes.showAsDefaultStub;
        ChatActivity.ThemeDelegate themeDelegate = this.themeDelegate;
        if (z3) {
            valueOf = Boolean.valueOf(this.forceDark);
            z = false;
            z2 = true;
            emojiThemes = null;
        } else {
            valueOf = Boolean.valueOf(this.forceDark);
            z = false;
            z2 = true;
        }
        themeDelegate.setCurrentTheme(emojiThemes, currentWallpaper, z, valueOf, z2);
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            ChatAttachAlertColorsLayout chatAttachAlertColorsLayout = chatAttachAlert.colorsLayout;
            if (chatAttachAlertColorsLayout != null) {
                chatAttachAlertColorsLayout.updateColors(this.forceDark);
            }
            this.chatAttachAlert.checkColors();
        }
        Adapter adapter = this.adapter;
        if (adapter == null || adapter.items == null) {
            return;
        }
        for (int i = 0; i < this.adapter.items.size(); i++) {
            ((ChatThemeItem) this.adapter.items.get(i)).themeIndex = this.forceDark ? 1 : 0;
        }
        this.adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasChanges() {
        if (this.selectedItem == null) {
            return false;
        }
        EmojiThemes emojiThemes = this.currentTheme;
        String emoticon = emojiThemes != null ? emojiThemes.getEmoticon() : null;
        if (TextUtils.isEmpty(emoticon)) {
            emoticon = "❌";
        }
        EmojiThemes emojiThemes2 = this.selectedItem.chatTheme;
        return !Objects.equals(emoticon, TextUtils.isEmpty(emojiThemes2 != null ? emojiThemes2.getEmoticon() : null) ? "❌" : r1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applySelectedTheme$13() {
        showAsSheet(StatisticActivity.create(this.chatActivity.getMessagesController().getChat(Long.valueOf(-this.chatActivity.getDialogId()))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applySelectedTheme$14(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        if (getContext() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this.chatActivity, getContext(), 22, this.currentAccount, this.resourcesProvider);
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(this.boostsStatus, true);
        limitReachedBottomSheet.setDialogId(this.chatActivity.getDialogId());
        limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$applySelectedTheme$13();
            }
        });
        limitReachedBottomSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkBoostsLevel$5(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        this.boostsStatus = tL_premium_boostsStatus;
        this.checkedBoostsLevel = true;
        updateState(true);
        this.checkingBoostsLevel = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$close$8(AlertDialog alertDialog, int i) {
        applySelectedTheme();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$close$9(AlertDialog alertDialog, int i) {
        lambda$new$0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$10() {
        this.adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (!hasChanges()) {
            lambda$new$0();
        } else {
            resetToPrimaryState(true);
            updateState(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        if (this.changeDayNightViewAnimator != null) {
            return;
        }
        setupLightDarkTheme(!this.forceDark);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view, final int i) {
        if (this.adapter.items.get(i) == this.selectedItem || this.changeDayNightView != null) {
            return;
        }
        this.selectedItem = (ChatThemeItem) this.adapter.items.get(i);
        previewSelectedTheme();
        this.adapter.setSelectedItem(i);
        this.containerView.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.3
            @Override // java.lang.Runnable
            public void run() {
                RecyclerView.LayoutManager layoutManager = ChatThemeBottomSheet.this.recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    ChatThemeBottomSheet.this.scroller.setTargetPosition(i > ChatThemeBottomSheet.this.prevSelectedPosition ? Math.min(i + 1, ChatThemeBottomSheet.this.adapter.items.size() - 1) : Math.max(i - 1, 0));
                    layoutManager.startSmoothScroll(ChatThemeBottomSheet.this.scroller);
                }
                ChatThemeBottomSheet.this.prevSelectedPosition = i;
            }
        }, 100L);
        for (int i2 = 0; i2 < this.recyclerView.getChildCount(); i2++) {
            ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) this.recyclerView.getChildAt(i2);
            if (themeSmallPreviewView != view) {
                themeSmallPreviewView.cancelAnimation();
            }
        }
        if (!((ChatThemeItem) this.adapter.items.get(i)).chatTheme.showAsDefaultStub) {
            ((ThemeSmallPreviewView) view).playEmojiAnimation();
        }
        updateState(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        applySelectedTheme();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(ChatActivity chatActivity, View view) {
        if (this.currentWallpaper == null) {
            lambda$new$0();
            return;
        }
        this.currentWallpaper = null;
        lambda$new$0();
        ChatThemeController.getInstance(this.currentAccount).clearWallpaper(chatActivity.getDialogId(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$7() {
        this.hintView.showForView(this.darkThemeView, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openGalleryForBackground$15(View view) {
        if (this.chatAttachAlert.getCurrentAttachLayout() == this.chatAttachAlert.getPhotoLayout()) {
            this.chatAttachButtonText.setText(LocaleController.getString(R.string.ChooseBackgroundFromGallery));
            this.chatAttachAlert.openColorsLayout();
            this.chatAttachAlert.colorsLayout.updateColors(this.forceDark);
        } else {
            this.chatAttachButtonText.setText(LocaleController.getString(R.string.SetColorAsBackground));
            ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
            chatAttachAlert.showLayout(chatAttachAlert.getPhotoLayout());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$setupLightDarkTheme$11(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupLightDarkTheme$12(boolean z) {
        Adapter adapter = this.adapter;
        if (adapter == null || adapter.items == null || isDismissed()) {
            return;
        }
        setForceDark(z, true);
        if (this.selectedItem != null) {
            this.isLightDarkChangeAnimation = true;
            TLRPC.WallPaper currentWallpaper = hasChanges() ? null : this.themeDelegate.getCurrentWallpaper();
            EmojiThemes emojiThemes = this.selectedItem.chatTheme;
            if (emojiThemes.showAsDefaultStub) {
                this.themeDelegate.setCurrentTheme(null, currentWallpaper, false, Boolean.valueOf(z));
            } else {
                this.themeDelegate.setCurrentTheme(emojiThemes, currentWallpaper, false, Boolean.valueOf(z));
            }
        }
        Adapter adapter2 = this.adapter;
        if (adapter2 == null || adapter2.items == null) {
            return;
        }
        for (int i = 0; i < this.adapter.items.size(); i++) {
            ((ChatThemeItem) this.adapter.items.get(i)).themeIndex = z ? 1 : 0;
        }
        this.adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showAsSheet$16() {
        PhotoViewer.getInstance().closePhoto(false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAsSheet$18() {
        this.overlayFragment = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showAsSheet$19() {
        PhotoViewer.getInstance().closePhoto(false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAsSheet$21() {
        this.overlayFragment = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateApplySubTextTranslation$6(ValueAnimator valueAnimator) {
        this.subTextTranslation = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.applyTextView.setTranslationY((-AndroidUtilities.dp(7.0f)) * this.subTextTranslation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationEnd() {
        this.isLightDarkChangeAnimation = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationStart() {
        List list;
        Adapter adapter = this.adapter;
        if (adapter != null && (list = adapter.items) != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ((ChatThemeItem) it.next()).themeIndex = this.forceDark ? 1 : 0;
            }
        }
        if (this.isLightDarkChangeAnimation) {
            return;
        }
        setItemsAnimationProgress(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataLoaded(List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.dataLoaded = true;
        ChatThemeItem chatThemeItem = new ChatThemeItem((EmojiThemes) list.get(0));
        ArrayList arrayList = new ArrayList(list.size());
        this.currentTheme = this.themeDelegate.getCurrentTheme();
        arrayList.add(0, chatThemeItem);
        this.selectedItem = chatThemeItem;
        for (int i = 1; i < list.size(); i++) {
            EmojiThemes emojiThemes = (EmojiThemes) list.get(i);
            ChatThemeItem chatThemeItem2 = new ChatThemeItem(emojiThemes);
            emojiThemes.loadPreviewColors(this.currentAccount);
            chatThemeItem2.themeIndex = this.forceDark ? 1 : 0;
            arrayList.add(chatThemeItem2);
        }
        this.adapter.setItems(arrayList);
        this.darkThemeView.setVisibility(0);
        resetToPrimaryState(false);
        this.recyclerView.animate().alpha(1.0f).setDuration(150L).start();
        updateState(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openGalleryForBackground() {
        Activity parentActivity = this.chatActivity.getParentActivity();
        ChatActivity chatActivity = this.chatActivity;
        ChatAttachAlert chatAttachAlert = new ChatAttachAlert(parentActivity, chatActivity, false, false, false, chatActivity.getResourceProvider());
        this.chatAttachAlert = chatAttachAlert;
        chatAttachAlert.drawNavigationBar = true;
        chatAttachAlert.setupPhotoPicker(LocaleController.getString(R.string.ChooseBackground));
        this.chatAttachAlert.setDelegate(new 12());
        this.chatAttachAlert.setMaxSelectedPhotos(1, false);
        this.chatAttachAlert.init();
        this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        this.chatAttachAlert.show();
        this.chatAttachButton = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.13
            Paint paint = new Paint();

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                this.paint.setColor(ChatThemeBottomSheet.this.getThemedColor(Theme.key_divider));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), 1.0f, this.paint);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
            }
        };
        AnimatedTextView animatedTextView = new AnimatedTextView(getContext(), true, true, true);
        this.chatAttachButtonText = animatedTextView;
        animatedTextView.setTextSize(AndroidUtilities.dp(14.0f));
        this.chatAttachButtonText.setText(LocaleController.getString(R.string.SetColorAsBackground));
        this.chatAttachButtonText.setGravity(17);
        AnimatedTextView animatedTextView2 = this.chatAttachButtonText;
        int i = Theme.key_featuredStickers_addButton;
        animatedTextView2.setTextColor(getThemedColor(i));
        this.chatAttachButton.addView(this.chatAttachButtonText, LayoutHelper.createFrame(-1, -2, 17));
        this.chatAttachButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(0.0f), getThemedColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(getThemedColor(i), 76)));
        this.chatAttachButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatThemeBottomSheet.this.lambda$openGalleryForBackground$15(view);
            }
        });
        this.chatAttachAlert.sizeNotifierFrameLayout.addView(this.chatAttachButton, LayoutHelper.createFrame(-1, -2, 80));
    }

    public static void openGalleryForBackground(Activity activity, BaseFragment baseFragment, long j, Theme.ResourcesProvider resourcesProvider, Utilities.Callback callback, ThemePreviewActivity.DayNightSwitchDelegate dayNightSwitchDelegate, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        ChatAttachAlert chatAttachAlert = new ChatAttachAlert(activity, baseFragment, false, false, false, resourcesProvider);
        chatAttachAlert.drawNavigationBar = true;
        chatAttachAlert.setupPhotoPicker(LocaleController.getString(R.string.ChooseBackground));
        chatAttachAlert.setDelegate(new 11(chatAttachAlert, tL_premium_boostsStatus, resourcesProvider, dayNightSwitchDelegate, j, callback, baseFragment));
        chatAttachAlert.setMaxSelectedPhotos(1, false);
        chatAttachAlert.init();
        chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        chatAttachAlert.show();
    }

    private void previewSelectedTheme() {
        if (isDismissed() || this.isApplyClicked) {
            return;
        }
        this.isLightDarkChangeAnimation = false;
        this.chatActivity.forceDisallowApplyWallpeper = false;
        TLRPC.WallPaper wallPaper = hasChanges() ? null : this.currentWallpaper;
        EmojiThemes emojiThemes = this.selectedItem.chatTheme;
        if (emojiThemes.showAsDefaultStub) {
            this.themeDelegate.setCurrentTheme(null, wallPaper, true, Boolean.valueOf(this.forceDark));
        } else {
            this.themeDelegate.setCurrentTheme(emojiThemes, wallPaper, true, Boolean.valueOf(this.forceDark));
        }
    }

    private void resetToPrimaryState(boolean z) {
        List list = this.adapter.items;
        if (this.currentTheme != null) {
            int i = 0;
            while (true) {
                if (i == list.size()) {
                    i = -1;
                    break;
                } else {
                    if (((ChatThemeItem) list.get(i)).chatTheme.getEmoticon().equals(this.currentTheme.getEmoticon())) {
                        this.selectedItem = (ChatThemeItem) list.get(i);
                        break;
                    }
                    i++;
                }
            }
            if (i != -1) {
                this.prevSelectedPosition = i;
                this.adapter.setSelectedItem(i);
                if (i > 0 && i < list.size() / 2) {
                    i--;
                }
                int min = Math.min(i, this.adapter.items.size() - 1);
                if (z) {
                    this.recyclerView.smoothScrollToPosition(min);
                } else {
                    this.layoutManager.scrollToPositionWithOffset(min, 0);
                }
            }
        } else {
            this.selectedItem = (ChatThemeItem) list.get(0);
            this.adapter.setSelectedItem(0);
            if (z) {
                this.recyclerView.smoothScrollToPosition(0);
            } else {
                this.layoutManager.scrollToPositionWithOffset(0, 0);
            }
        }
        previewSelectedTheme();
    }

    private void setForceDark(boolean z, boolean z2) {
        if (this.forceDark == z) {
            return;
        }
        this.forceDark = z;
        if (z2) {
            RLottieDrawable rLottieDrawable = this.darkThemeDrawable;
            rLottieDrawable.setCustomEndFrame(z ? rLottieDrawable.getFramesCount() : 0);
            RLottieImageView rLottieImageView = this.darkThemeView;
            if (rLottieImageView != null) {
                rLottieImageView.playAnimation();
                return;
            }
            return;
        }
        int framesCount = z ? this.darkThemeDrawable.getFramesCount() - 1 : 0;
        this.darkThemeDrawable.setCurrentFrame(framesCount, false, true);
        this.darkThemeDrawable.setCustomEndFrame(framesCount);
        RLottieImageView rLottieImageView2 = this.darkThemeView;
        if (rLottieImageView2 != null) {
            rLottieImageView2.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setItemsAnimationProgress(float f) {
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            ((ChatThemeItem) this.adapter.items.get(i)).animationProgress = f;
        }
    }

    private void showAsSheet(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        baseFragment.setResourceProvider(this.chatActivity.getResourceProvider());
        bottomSheetParams.onOpenAnimationFinished = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.lambda$showAsSheet$16();
            }
        };
        bottomSheetParams.onPreFinished = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$showAsSheet$17();
            }
        };
        bottomSheetParams.onDismiss = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$showAsSheet$18();
            }
        };
        bottomSheetParams.occupyNavigationBar = true;
        ChatActivity chatActivity = this.chatActivity;
        this.overlayFragment = baseFragment;
        chatActivity.showAsSheet(baseFragment, bottomSheetParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAsSheet(ThemePreviewActivity themePreviewActivity) {
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        themePreviewActivity.setResourceProvider(this.chatActivity.getResourceProvider());
        themePreviewActivity.setOnSwitchDayNightDelegate(new ThemePreviewActivity.DayNightSwitchDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.14
            @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
            public boolean isDark() {
                return ChatThemeBottomSheet.this.forceDark;
            }

            @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
            public boolean supportsAnimation() {
                return true;
            }

            @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
            public void switchDayNight(boolean z) {
                ChatThemeBottomSheet.this.forceDark = !r0.forceDark;
                if (ChatThemeBottomSheet.this.selectedItem != null) {
                    ChatThemeBottomSheet.this.isLightDarkChangeAnimation = true;
                    ChatThemeBottomSheet.this.chatActivity.forceDisallowRedrawThemeDescriptions = true;
                    TLRPC.WallPaper currentWallpaper = ChatThemeBottomSheet.this.hasChanges() ? null : ChatThemeBottomSheet.this.themeDelegate.getCurrentWallpaper();
                    if (ChatThemeBottomSheet.this.selectedItem.chatTheme.showAsDefaultStub) {
                        ChatThemeBottomSheet.this.themeDelegate.setCurrentTheme(null, currentWallpaper, z, Boolean.valueOf(ChatThemeBottomSheet.this.forceDark));
                    } else {
                        ChatThemeBottomSheet.this.themeDelegate.setCurrentTheme(ChatThemeBottomSheet.this.selectedItem.chatTheme, currentWallpaper, z, Boolean.valueOf(ChatThemeBottomSheet.this.forceDark));
                    }
                    ChatThemeBottomSheet.this.chatActivity.forceDisallowRedrawThemeDescriptions = false;
                }
            }
        });
        bottomSheetParams.onOpenAnimationFinished = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.lambda$showAsSheet$19();
            }
        };
        bottomSheetParams.onPreFinished = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$showAsSheet$20();
            }
        };
        bottomSheetParams.onDismiss = new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$showAsSheet$21();
            }
        };
        bottomSheetParams.occupyNavigationBar = true;
        this.overlayFragment = themePreviewActivity;
        this.chatActivity.showAsSheet(themePreviewActivity, bottomSheetParams);
    }

    private void updateApplySubTextTranslation(final boolean z, boolean z2) {
        ValueAnimator valueAnimator = this.subTextTranslationAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.subTextTranslationAnimator = null;
        }
        if (!z2) {
            this.subTextTranslation = z ? 1.0f : 0.0f;
            this.applyTextView.setTranslationY((-AndroidUtilities.dp(7.0f)) * this.subTextTranslation);
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.subTextTranslation, z ? 1.0f : 0.0f);
        this.subTextTranslationAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda14
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ChatThemeBottomSheet.this.lambda$updateApplySubTextTranslation$6(valueAnimator2);
            }
        });
        this.subTextTranslationAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ChatThemeBottomSheet.this.subTextTranslation = z ? 1.0f : 0.0f;
                ChatThemeBottomSheet.this.applyTextView.setTranslationY((-AndroidUtilities.dp(7.0f)) * ChatThemeBottomSheet.this.subTextTranslation);
            }
        });
        this.subTextTranslationAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonColors() {
        TextView textView = this.themeHintTextView;
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_dialogTextGray));
            this.themeHintTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(getThemedColor(Theme.key_featuredStickers_addButton), 76)));
        }
        TextView textView2 = this.cancelOrResetTextView;
        if (textView2 != null) {
            int i = Theme.key_text_RedRegular;
            textView2.setTextColor(getThemedColor(i));
            this.cancelOrResetTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(getThemedColor(i), 76)));
        }
        ImageView imageView = this.backButtonView;
        int i2 = Theme.key_dialogTextBlack;
        imageView.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(getThemedColor(i2), 30), 1));
        this.backButtonDrawable.setColor(getThemedColor(i2));
        this.backButtonDrawable.setRotatedColor(getThemedColor(i2));
        this.backButtonView.invalidate();
        RLottieImageView rLottieImageView = this.darkThemeView;
        int i3 = Theme.key_featuredStickers_addButton;
        rLottieImageView.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(getThemedColor(i3), 30), 1));
        this.chooseBackgroundTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlue));
        this.chooseBackgroundTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(getThemedColor(i3), 76)));
    }

    private void updateState(boolean z) {
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus;
        boolean z2;
        EmojiThemes emojiThemes;
        TLRPC.Chat currentChat = this.chatActivity.getCurrentChat();
        if (currentChat != null) {
            checkBoostsLevel();
        }
        if (!this.dataLoaded) {
            this.backButtonDrawable.setRotation(1.0f, z);
            this.applyButton.setEnabled(false);
            AndroidUtilities.updateViewVisibilityAnimated(this.chooseBackgroundTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.cancelOrResetTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applyButton, false, 1.0f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applyTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applySubTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.themeHintTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.progressView, true, 1.0f, true, z);
            return;
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.progressView, false, 1.0f, true, z);
        if (!hasChanges()) {
            this.backButtonDrawable.setRotation(1.0f, z);
            this.applyButton.setEnabled(false);
            AndroidUtilities.updateViewVisibilityAnimated(this.chooseBackgroundTextView, true, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.cancelOrResetTextView, true, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applyButton, false, 1.0f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applyTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.applySubTextView, false, 0.9f, false, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.themeHintTextView, false, 0.9f, false, z);
            return;
        }
        this.backButtonDrawable.setRotation(0.0f, z);
        this.applyButton.setEnabled(true);
        ChatThemeItem chatThemeItem = this.selectedItem;
        if (chatThemeItem == null || (emojiThemes = chatThemeItem.chatTheme) == null || !emojiThemes.showAsDefaultStub || emojiThemes.wallpaper != null) {
            this.applyTextView.setText(LocaleController.getString(R.string.ChatApplyTheme));
            if (currentChat != null && (tL_premium_boostsStatus = this.boostsStatus) != null && tL_premium_boostsStatus.level < this.chatActivity.getMessagesController().channelWallpaperLevelMin) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("l");
                if (this.lockSpan == null) {
                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_switch_lock);
                    this.lockSpan = coloredImageSpan;
                    coloredImageSpan.setTopOffset(1);
                }
                spannableStringBuilder.setSpan(this.lockSpan, 0, 1, 33);
                spannableStringBuilder.append((CharSequence) " ").append((CharSequence) LocaleController.formatPluralString("ReactionLevelRequiredBtn", this.chatActivity.getMessagesController().channelWallpaperLevelMin, new Object[0]));
                this.applySubTextView.setText(spannableStringBuilder);
                z2 = true;
                updateApplySubTextTranslation(z2, !z && this.applyTextView.getAlpha() > 0.8f);
                AndroidUtilities.updateViewVisibilityAnimated(this.chooseBackgroundTextView, false, 0.9f, false, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.cancelOrResetTextView, false, 0.9f, false, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.applyButton, true, 1.0f, false, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.applyTextView, true, 0.9f, false, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.applySubTextView, z2, 0.9f, false, 0.7f, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.themeHintTextView, true, 0.9f, false, z);
            }
        } else {
            this.applyTextView.setText(LocaleController.getString(R.string.ChatResetTheme));
        }
        z2 = false;
        updateApplySubTextTranslation(z2, !z && this.applyTextView.getAlpha() > 0.8f);
        AndroidUtilities.updateViewVisibilityAnimated(this.chooseBackgroundTextView, false, 0.9f, false, z);
        AndroidUtilities.updateViewVisibilityAnimated(this.cancelOrResetTextView, false, 0.9f, false, z);
        AndroidUtilities.updateViewVisibilityAnimated(this.applyButton, true, 1.0f, false, z);
        AndroidUtilities.updateViewVisibilityAnimated(this.applyTextView, true, 0.9f, false, z);
        AndroidUtilities.updateViewVisibilityAnimated(this.applySubTextView, z2, 0.9f, false, 0.7f, z);
        AndroidUtilities.updateViewVisibilityAnimated(this.themeHintTextView, true, 0.9f, false, z);
    }

    public void close() {
        if (!hasChanges()) {
            lambda$new$0();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.ChatThemeSaveDialogTitle));
        builder.setSubtitle(LocaleController.getString(R.string.ChatThemeSaveDialogText));
        builder.setPositiveButton(LocaleController.getString(R.string.ChatThemeSaveDialogApply), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                ChatThemeBottomSheet.this.lambda$close$8(alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.ChatThemeSaveDialogDiscard), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                ChatThemeBottomSheet.this.lambda$close$9(alertDialog, i);
            }
        });
        builder.show();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatThemeBottomSheet.this.lambda$didReceivedNotification$10();
                }
            });
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    /* renamed from: dismiss */
    public void lambda$new$0() {
        Theme.ThemeInfo theme;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        super.lambda$new$0();
        this.chatActivity.forceDisallowApplyWallpeper = false;
        if (!this.isApplyClicked) {
            TLRPC.WallPaper currentWallpaper = this.themeDelegate.getCurrentWallpaper();
            if (currentWallpaper == null) {
                currentWallpaper = this.currentWallpaper;
            }
            this.themeDelegate.setCurrentTheme(this.originalTheme, currentWallpaper, true, Boolean.valueOf(this.originalIsDark));
        }
        if (this.forceDark != this.originalIsDark) {
            if (Theme.getActiveTheme().isDark() == this.originalIsDark) {
                theme = Theme.getActiveTheme();
            } else {
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
                String str = "Blue";
                String string = sharedPreferences.getString("lastDayTheme", "Blue");
                if (Theme.getTheme(string) != null && !Theme.getTheme(string).isDark()) {
                    str = string;
                }
                String str2 = "Dark Blue";
                String string2 = sharedPreferences.getString("lastDarkTheme", "Dark Blue");
                if (Theme.getTheme(string2) != null && Theme.getTheme(string2).isDark()) {
                    str2 = string2;
                }
                theme = this.originalIsDark ? Theme.getTheme(str2) : Theme.getTheme(str);
            }
            Theme.applyTheme(theme, false, this.originalIsDark);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.7
            private boolean isAnimationStarted = false;

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public void didSetColor() {
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public void onAnimationProgress(float f) {
                if (f == 0.0f && !this.isAnimationStarted) {
                    ChatThemeBottomSheet.this.onAnimationStart();
                    this.isAnimationStarted = true;
                }
                RLottieDrawable rLottieDrawable = ChatThemeBottomSheet.this.darkThemeDrawable;
                ChatThemeBottomSheet chatThemeBottomSheet = ChatThemeBottomSheet.this;
                int i = Theme.key_featuredStickers_addButton;
                rLottieDrawable.setColorFilter(new PorterDuffColorFilter(chatThemeBottomSheet.getThemedColor(i), PorterDuff.Mode.MULTIPLY));
                ChatThemeBottomSheet chatThemeBottomSheet2 = ChatThemeBottomSheet.this;
                chatThemeBottomSheet2.setOverlayNavBarColor(chatThemeBottomSheet2.getThemedColor(Theme.key_windowBackgroundGray));
                if (ChatThemeBottomSheet.this.isLightDarkChangeAnimation) {
                    ChatThemeBottomSheet.this.setItemsAnimationProgress(f);
                }
                if (f == 1.0f && this.isAnimationStarted) {
                    ChatThemeBottomSheet.this.isLightDarkChangeAnimation = false;
                    ChatThemeBottomSheet.this.onAnimationEnd();
                    this.isAnimationStarted = false;
                }
                ChatThemeBottomSheet.this.updateButtonColors();
                if (ChatThemeBottomSheet.this.chatAttachButton != null) {
                    ChatThemeBottomSheet.this.chatAttachButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(0.0f), ChatThemeBottomSheet.this.getThemedColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(ChatThemeBottomSheet.this.getThemedColor(i), 76)));
                }
                if (ChatThemeBottomSheet.this.chatAttachButtonText != null) {
                    ChatThemeBottomSheet.this.chatAttachButtonText.setTextColor(ChatThemeBottomSheet.this.getThemedColor(i));
                }
            }
        };
        ArrayList arrayList = new ArrayList();
        if (this.chatActivity.forceDisallowRedrawThemeDescriptions) {
            BaseFragment baseFragment = this.overlayFragment;
            if (baseFragment instanceof ThemePreviewActivity) {
                arrayList.addAll(((ThemePreviewActivity) baseFragment).getThemeDescriptionsInternal());
                return arrayList;
            }
        }
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            arrayList.addAll(chatAttachAlert.getThemeDescriptions());
        }
        arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{this.shadowDrawable}, themeDescriptionDelegate, Theme.key_dialogBackground));
        arrayList.add(new ThemeDescription(this.titleView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_dialogTextBlack));
        arrayList.add(new ThemeDescription(this.recyclerView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ThemeSmallPreviewView.class}, null, null, null, Theme.key_dialogBackgroundGray));
        arrayList.add(new ThemeDescription(this.applyButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_featuredStickers_addButton));
        arrayList.add(new ThemeDescription(this.applyButton, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_featuredStickers_addButtonPressed));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((ThemeDescription) it.next()).resourcesProvider = this.themeDelegate;
        }
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        close();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onContainerTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null || !hasChanges()) {
            return false;
        }
        int x = (int) motionEvent.getX();
        if (((int) motionEvent.getY()) >= this.containerView.getTop() && x >= this.containerView.getLeft() && x <= this.containerView.getRight()) {
            return false;
        }
        this.chatActivity.getFragmentView().dispatchTouchEvent(motionEvent);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onContainerTranslationYChanged(float f) {
        HintView hintView = this.hintView;
        if (hintView != null) {
            hintView.hide();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        HintView hintView;
        String formatString;
        super.onCreate(bundle);
        ChatThemeController chatThemeController = ChatThemeController.getInstance(this.currentAccount);
        chatThemeController.preloadAllWallpaperThumbs(true);
        chatThemeController.preloadAllWallpaperThumbs(false);
        chatThemeController.preloadAllWallpaperImages(true);
        chatThemeController.preloadAllWallpaperImages(false);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        this.isApplyClicked = false;
        List cachedThemes = this.themeDelegate.getCachedThemes();
        if (cachedThemes == null || cachedThemes.isEmpty()) {
            chatThemeController.requestAllChatThemes(new 6(), true);
        } else {
            onDataLoaded(cachedThemes);
        }
        if (this.chatActivity.getCurrentUser() == null || SharedConfig.dayNightThemeSwitchHintCount <= 0 || this.chatActivity.getCurrentUser().self) {
            return;
        }
        SharedConfig.updateDayNightThemeSwitchHintCount(SharedConfig.dayNightThemeSwitchHintCount - 1);
        HintView hintView2 = new HintView(getContext(), 9, this.chatActivity.getResourceProvider());
        this.hintView = hintView2;
        hintView2.setVisibility(4);
        this.hintView.setShowingDuration(5000L);
        this.hintView.setBottomOffset(-AndroidUtilities.dp(8.0f));
        if (this.forceDark) {
            hintView = this.hintView;
            formatString = LocaleController.formatString("ChatThemeDaySwitchTooltip", R.string.ChatThemeDaySwitchTooltip, new Object[0]);
        } else {
            hintView = this.hintView;
            formatString = LocaleController.formatString("ChatThemeNightSwitchTooltip", R.string.ChatThemeNightSwitchTooltip, new Object[0]);
        }
        hintView.setText(AndroidUtilities.replaceTags(formatString));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$onCreate$7();
            }
        }, 1500L);
        this.container.addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
    }

    public void setupLightDarkTheme(final boolean z) {
        if (isDismissed()) {
            return;
        }
        ValueAnimator valueAnimator = this.changeDayNightViewAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        FrameLayout frameLayout = (FrameLayout) this.chatActivity.getParentActivity().getWindow().getDecorView();
        FrameLayout frameLayout2 = (FrameLayout) getWindow().getDecorView();
        final Bitmap createBitmap = Bitmap.createBitmap(frameLayout2.getWidth(), frameLayout2.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(createBitmap);
        this.darkThemeView.setAlpha(0.0f);
        frameLayout.draw(canvas);
        frameLayout2.draw(canvas);
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
        View view = new View(getContext()) { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.8
            @Override // android.view.View
            protected void onDraw(Canvas canvas2) {
                super.onDraw(canvas2);
                if (z) {
                    if (ChatThemeBottomSheet.this.changeDayNightViewProgress > 0.0f) {
                        canvas.drawCircle(measuredWidth, measuredHeight, max * ChatThemeBottomSheet.this.changeDayNightViewProgress, paint);
                    }
                    canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
                } else {
                    canvas2.drawCircle(measuredWidth, measuredHeight, max * (1.0f - ChatThemeBottomSheet.this.changeDayNightViewProgress), paint2);
                }
                canvas2.save();
                canvas2.translate(f, f2);
                ChatThemeBottomSheet.this.darkThemeView.draw(canvas2);
                canvas2.restore();
            }
        };
        this.changeDayNightView = view;
        view.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda9
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean lambda$setupLightDarkTheme$11;
                lambda$setupLightDarkTheme$11 = ChatThemeBottomSheet.lambda$setupLightDarkTheme$11(view2, motionEvent);
                return lambda$setupLightDarkTheme$11;
            }
        });
        this.changeDayNightViewProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.changeDayNightViewAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.9
            boolean changedNavigationBarColor = false;

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ChatThemeBottomSheet.this.changeDayNightViewProgress = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                ChatThemeBottomSheet.this.changeDayNightView.invalidate();
                if (this.changedNavigationBarColor || ChatThemeBottomSheet.this.changeDayNightViewProgress <= 0.5f) {
                    return;
                }
                this.changedNavigationBarColor = true;
            }
        });
        this.changeDayNightViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ChatThemeBottomSheet.this.changeDayNightView != null) {
                    if (ChatThemeBottomSheet.this.changeDayNightView.getParent() != null) {
                        ((ViewGroup) ChatThemeBottomSheet.this.changeDayNightView.getParent()).removeView(ChatThemeBottomSheet.this.changeDayNightView);
                    }
                    ChatThemeBottomSheet.this.changeDayNightView = null;
                }
                ChatThemeBottomSheet.this.changeDayNightViewAnimator = null;
                super.onAnimationEnd(animator);
            }
        });
        this.changeDayNightViewAnimator.setDuration(400L);
        this.changeDayNightViewAnimator.setInterpolator(Easings.easeInOutQuad);
        this.changeDayNightViewAnimator.start();
        frameLayout2.addView(this.changeDayNightView, new ViewGroup.LayoutParams(-1, -1));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatThemeBottomSheet$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeBottomSheet.this.lambda$setupLightDarkTheme$12(z);
            }
        });
    }
}
