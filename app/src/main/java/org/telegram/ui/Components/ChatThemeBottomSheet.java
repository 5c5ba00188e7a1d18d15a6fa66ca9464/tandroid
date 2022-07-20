package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ChatThemeBottomSheet extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private final Adapter adapter;
    private final View applyButton;
    private TextView applyTextView;
    private View changeDayNightView;
    private ValueAnimator changeDayNightViewAnimator;
    private float changeDayNightViewProgress;
    private final ChatActivity chatActivity;
    private final RLottieDrawable darkThemeDrawable;
    private final RLottieImageView darkThemeView;
    HintView hintView;
    private boolean isApplyClicked;
    private boolean isLightDarkChangeAnimation;
    private final LinearLayoutManager layoutManager;
    private final EmojiThemes originalTheme;
    private final FlickerLoadingView progressView;
    private final RecyclerListView recyclerView;
    private TextView resetTextView;
    private FrameLayout rootLayout;
    private ChatThemeItem selectedItem;
    private final ChatActivity.ThemeDelegate themeDelegate;
    private final TextView titleView;
    private int prevSelectedPosition = -1;
    private final boolean originalIsDark = Theme.getActiveTheme().isDark();
    private boolean forceDark = !Theme.getActiveTheme().isDark();
    private final LinearSmoothScroller scroller = new AnonymousClass2(this, getContext());

    public ChatThemeBottomSheet(ChatActivity chatActivity, ChatActivity.ThemeDelegate themeDelegate) {
        super(chatActivity.getParentActivity(), true, themeDelegate);
        String str;
        int i;
        this.chatActivity = chatActivity;
        this.themeDelegate = themeDelegate;
        this.originalTheme = themeDelegate.getCurrentTheme();
        Adapter adapter = new Adapter(this.currentAccount, themeDelegate, 0);
        this.adapter = adapter;
        setDimBehind(false);
        setCanDismissWithSwipe(false);
        setApplyBottomPadding(false);
        this.drawNavigationBar = true;
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.rootLayout = frameLayout;
        setCustomView(frameLayout);
        TextView textView = new TextView(getContext());
        this.titleView = textView;
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setLines(1);
        textView.setSingleLine(true);
        textView.setText(LocaleController.getString("SelectTheme", 2131628171));
        textView.setTextColor(getThemedColor("dialogTextBlack"));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
        this.rootLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 8388659, 0.0f, 0.0f, 62.0f, 0.0f));
        int themedColor = getThemedColor("featuredStickers_addButton");
        int dp = AndroidUtilities.dp(28.0f);
        RLottieDrawable rLottieDrawable = new RLottieDrawable(2131558541, "2131558541", dp, dp, false, null);
        this.darkThemeDrawable = rLottieDrawable;
        setForceDark(Theme.getActiveTheme().isDark(), false);
        rLottieDrawable.setAllowDecodeSingleFrame(true);
        rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
        rLottieDrawable.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.MULTIPLY));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(getContext());
        this.darkThemeView = anonymousClass1;
        anonymousClass1.setAnimation(rLottieDrawable);
        anonymousClass1.setScaleType(ImageView.ScaleType.CENTER);
        anonymousClass1.setOnClickListener(new ChatThemeBottomSheet$$ExternalSyntheticLambda2(this));
        this.rootLayout.addView(anonymousClass1, LayoutHelper.createFrame(44, 44.0f, 8388661, 0.0f, -2.0f, 7.0f, 0.0f));
        RecyclerListView recyclerListView = new RecyclerListView(getContext());
        this.recyclerView = recyclerListView;
        recyclerListView.setAdapter(adapter);
        recyclerListView.setClipChildren(false);
        recyclerListView.setClipToPadding(false);
        recyclerListView.setHasFixedSize(true);
        recyclerListView.setItemAnimator(null);
        recyclerListView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        recyclerListView.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        recyclerListView.setOnItemClickListener(new ChatThemeBottomSheet$$ExternalSyntheticLambda6(this, themeDelegate));
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(getContext(), this.resourcesProvider);
        this.progressView = flickerLoadingView;
        flickerLoadingView.setViewType(14);
        flickerLoadingView.setVisibility(0);
        this.rootLayout.addView(flickerLoadingView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
        this.rootLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 44.0f, 0.0f, 0.0f));
        View view = new View(getContext());
        this.applyButton = view;
        view.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor("featuredStickers_addButton"), getThemedColor("featuredStickers_addButtonPressed")));
        view.setEnabled(false);
        view.setOnClickListener(new ChatThemeBottomSheet$$ExternalSyntheticLambda3(this));
        this.rootLayout.addView(view, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        TextView textView2 = new TextView(getContext());
        this.resetTextView = textView2;
        textView2.setAlpha(0.0f);
        this.resetTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.resetTextView.setGravity(17);
        this.resetTextView.setLines(1);
        this.resetTextView.setSingleLine(true);
        TextView textView3 = this.resetTextView;
        if (themeDelegate.getCurrentTheme() == null) {
            i = 2131625522;
            str = "DoNoSetTheme";
        } else {
            i = 2131625026;
            str = "ChatResetTheme";
        }
        textView3.setText(LocaleController.getString(str, i));
        this.resetTextView.setTextColor(getThemedColor("featuredStickers_buttonText"));
        this.resetTextView.setTextSize(1, 15.0f);
        this.resetTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.resetTextView.setVisibility(4);
        this.rootLayout.addView(this.resetTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
        TextView textView4 = new TextView(getContext());
        this.applyTextView = textView4;
        textView4.setEllipsize(TextUtils.TruncateAt.END);
        this.applyTextView.setGravity(17);
        this.applyTextView.setLines(1);
        this.applyTextView.setSingleLine(true);
        this.applyTextView.setText(LocaleController.getString("ChatApplyTheme", 2131624999));
        this.applyTextView.setTextColor(getThemedColor("featuredStickers_buttonText"));
        this.applyTextView.setTextSize(1, 15.0f);
        this.applyTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.applyTextView.setVisibility(4);
        this.rootLayout.addView(this.applyTextView, LayoutHelper.createFrame(-1, 48.0f, 8388611, 16.0f, 162.0f, 16.0f, 16.0f));
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends RLottieImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            ChatThemeBottomSheet.this = r1;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (ChatThemeBottomSheet.this.forceDark) {
                accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToDayTheme", 2131624092));
            } else {
                accessibilityNodeInfo.setText(LocaleController.getString("AccDescrSwitchToNightTheme", 2131624093));
            }
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        if (this.changeDayNightViewAnimator != null) {
            return;
        }
        setupLightDarkTheme(!this.forceDark);
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends LinearSmoothScroller {
        AnonymousClass2(ChatThemeBottomSheet chatThemeBottomSheet, Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public int calculateTimeForScrolling(int i) {
            return super.calculateTimeForScrolling(i) * 6;
        }
    }

    public /* synthetic */ void lambda$new$1(ChatActivity.ThemeDelegate themeDelegate, View view, int i) {
        if (this.adapter.items.get(i) == this.selectedItem || this.changeDayNightView != null) {
            return;
        }
        ChatThemeItem chatThemeItem = this.adapter.items.get(i);
        this.selectedItem = chatThemeItem;
        this.isLightDarkChangeAnimation = false;
        EmojiThemes emojiThemes = chatThemeItem.chatTheme;
        if (emojiThemes == null || emojiThemes.showAsDefaultStub) {
            this.applyTextView.animate().alpha(0.0f).setDuration(300L).start();
            this.resetTextView.animate().alpha(1.0f).setDuration(300L).start();
        } else {
            this.resetTextView.animate().alpha(0.0f).setDuration(300L).start();
            this.applyTextView.animate().alpha(1.0f).setDuration(300L).start();
        }
        EmojiThemes emojiThemes2 = this.selectedItem.chatTheme;
        if (emojiThemes2.showAsDefaultStub) {
            themeDelegate.setCurrentTheme(null, true, Boolean.valueOf(this.forceDark));
        } else {
            themeDelegate.setCurrentTheme(emojiThemes2, true, Boolean.valueOf(this.forceDark));
        }
        this.adapter.setSelectedItem(i);
        this.containerView.postDelayed(new AnonymousClass3(i), 100L);
        for (int i2 = 0; i2 < this.recyclerView.getChildCount(); i2++) {
            ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) this.recyclerView.getChildAt(i2);
            if (themeSmallPreviewView != view) {
                themeSmallPreviewView.cancelAnimation();
            }
        }
        if (this.adapter.items.get(i).chatTheme.showAsDefaultStub) {
            return;
        }
        ((ThemeSmallPreviewView) view).playEmojiAnimation();
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ int val$position;

        AnonymousClass3(int i) {
            ChatThemeBottomSheet.this = r1;
            this.val$position = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i;
            RecyclerView.LayoutManager layoutManager = ChatThemeBottomSheet.this.recyclerView.getLayoutManager();
            if (layoutManager != null) {
                if (this.val$position > ChatThemeBottomSheet.this.prevSelectedPosition) {
                    i = Math.min(this.val$position + 1, ChatThemeBottomSheet.this.adapter.items.size() - 1);
                } else {
                    i = Math.max(this.val$position - 1, 0);
                }
                ChatThemeBottomSheet.this.scroller.setTargetPosition(i);
                layoutManager.startSmoothScroll(ChatThemeBottomSheet.this.scroller);
            }
            ChatThemeBottomSheet.this.prevSelectedPosition = this.val$position;
        }
    }

    public /* synthetic */ void lambda$new$2(View view) {
        applySelectedTheme();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ChatThemeController.preloadAllWallpaperThumbs(true);
        ChatThemeController.preloadAllWallpaperThumbs(false);
        ChatThemeController.preloadAllWallpaperImages(true);
        ChatThemeController.preloadAllWallpaperImages(false);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        this.isApplyClicked = false;
        List<EmojiThemes> cachedThemes = this.themeDelegate.getCachedThemes();
        if (cachedThemes == null || cachedThemes.isEmpty()) {
            ChatThemeController.requestAllChatThemes(new AnonymousClass4(), true);
        } else {
            onDataLoaded(cachedThemes);
        }
        if (this.chatActivity.getCurrentUser() == null || SharedConfig.dayNightThemeSwitchHintCount <= 0 || this.chatActivity.getCurrentUser().self) {
            return;
        }
        SharedConfig.updateDayNightThemeSwitchHintCount(SharedConfig.dayNightThemeSwitchHintCount - 1);
        HintView hintView = new HintView(getContext(), 9, this.chatActivity.getResourceProvider());
        this.hintView = hintView;
        hintView.setVisibility(4);
        this.hintView.setShowingDuration(5000L);
        this.hintView.setBottomOffset(-AndroidUtilities.dp(8.0f));
        this.hintView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ChatThemeDayNightSwitchTooltip", 2131625035, this.chatActivity.getCurrentUser().first_name)));
        AndroidUtilities.runOnUIThread(new ChatThemeBottomSheet$$ExternalSyntheticLambda4(this), 1500L);
        this.container.addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 implements ResultCallback<List<EmojiThemes>> {
        AnonymousClass4() {
            ChatThemeBottomSheet.this = r1;
        }

        public void onComplete(List<EmojiThemes> list) {
            if (list != null && !list.isEmpty()) {
                ChatThemeBottomSheet.this.themeDelegate.setCachedThemes(list);
            }
            ChatThemeBottomSheet.this.onDataLoaded(list);
        }

        @Override // org.telegram.tgnet.ResultCallback
        public void onError(TLRPC$TL_error tLRPC$TL_error) {
            Toast.makeText(ChatThemeBottomSheet.this.getContext(), tLRPC$TL_error.text, 0).show();
        }
    }

    public /* synthetic */ void lambda$onCreate$3() {
        this.hintView.showForView(this.darkThemeView, true);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onContainerTranslationYChanged(float f) {
        HintView hintView = this.hintView;
        if (hintView != null) {
            hintView.hide();
        }
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        close();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        super.dismiss();
        if (!this.isApplyClicked) {
            this.themeDelegate.setCurrentTheme(this.originalTheme, true, Boolean.valueOf(this.originalIsDark));
        }
    }

    public void close() {
        if (hasChanges()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
            builder.setTitle(LocaleController.getString("ChatThemeSaveDialogTitle", 2131625041));
            builder.setSubtitle(LocaleController.getString("ChatThemeSaveDialogText", 2131625040));
            builder.setPositiveButton(LocaleController.getString("ChatThemeSaveDialogApply", 2131625038), new ChatThemeBottomSheet$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("ChatThemeSaveDialogDiscard", 2131625039), new ChatThemeBottomSheet$$ExternalSyntheticLambda1(this));
            builder.show();
            return;
        }
        dismiss();
    }

    public /* synthetic */ void lambda$close$4(DialogInterface dialogInterface, int i) {
        applySelectedTheme();
    }

    public /* synthetic */ void lambda$close$5(DialogInterface dialogInterface, int i) {
        dismiss();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    @SuppressLint({"NotifyDataSetChanged"})
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.emojiLoaded) {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 implements ThemeDescription.ThemeDescriptionDelegate {
        private boolean isAnimationStarted = false;

        @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
        public void didSetColor() {
        }

        AnonymousClass5() {
            ChatThemeBottomSheet.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
        public void onAnimationProgress(float f) {
            if (f == 0.0f && !this.isAnimationStarted) {
                ChatThemeBottomSheet.this.onAnimationStart();
                this.isAnimationStarted = true;
            }
            ChatThemeBottomSheet.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(ChatThemeBottomSheet.this.getThemedColor("featuredStickers_addButton"), PorterDuff.Mode.MULTIPLY));
            ChatThemeBottomSheet chatThemeBottomSheet = ChatThemeBottomSheet.this;
            chatThemeBottomSheet.setOverlayNavBarColor(chatThemeBottomSheet.getThemedColor("windowBackgroundGray"));
            if (ChatThemeBottomSheet.this.isLightDarkChangeAnimation) {
                ChatThemeBottomSheet.this.setItemsAnimationProgress(f);
            }
            if (f != 1.0f || !this.isAnimationStarted) {
                return;
            }
            ChatThemeBottomSheet.this.isLightDarkChangeAnimation = false;
            ChatThemeBottomSheet.this.onAnimationEnd();
            this.isAnimationStarted = false;
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        AnonymousClass5 anonymousClass5 = new AnonymousClass5();
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{this.shadowDrawable}, anonymousClass5, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.titleView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.recyclerView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ThemeSmallPreviewView.class}, null, null, null, "dialogBackgroundGray"));
        arrayList.add(new ThemeDescription(this.applyButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.applyButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "featuredStickers_addButtonPressed"));
        Iterator<ThemeDescription> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().resourcesProvider = this.themeDelegate;
        }
        return arrayList;
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public void setupLightDarkTheme(boolean z) {
        ValueAnimator valueAnimator = this.changeDayNightViewAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        FrameLayout frameLayout = (FrameLayout) getWindow().getDecorView();
        Bitmap createBitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.darkThemeView.setAlpha(0.0f);
        ((FrameLayout) this.chatActivity.getParentActivity().getWindow().getDecorView()).draw(canvas);
        frameLayout.draw(canvas);
        this.darkThemeView.setAlpha(1.0f);
        Paint paint = new Paint(1);
        paint.setColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Paint paint2 = new Paint(1);
        paint2.setFilterBitmap(true);
        int[] iArr = new int[2];
        this.darkThemeView.getLocationInWindow(iArr);
        float f = iArr[0];
        float f2 = iArr[1];
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint2.setShader(new BitmapShader(createBitmap, tileMode, tileMode));
        this.changeDayNightView = new AnonymousClass6(getContext(), z, canvas, f + (this.darkThemeView.getMeasuredWidth() / 2.0f), f2 + (this.darkThemeView.getMeasuredHeight() / 2.0f), Math.max(createBitmap.getHeight(), createBitmap.getWidth()) * 0.9f, paint, createBitmap, paint2, f, f2);
        this.changeDayNightViewProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.changeDayNightViewAnimator = ofFloat;
        ofFloat.addUpdateListener(new AnonymousClass7(z));
        this.changeDayNightViewAnimator.addListener(new AnonymousClass8());
        this.changeDayNightViewAnimator.setDuration(400L);
        this.changeDayNightViewAnimator.setInterpolator(Easings.easeInOutQuad);
        this.changeDayNightViewAnimator.start();
        frameLayout.addView(this.changeDayNightView, new ViewGroup.LayoutParams(-1, -1));
        AndroidUtilities.runOnUIThread(new ChatThemeBottomSheet$$ExternalSyntheticLambda5(this, z));
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends View {
        final /* synthetic */ Bitmap val$bitmap;
        final /* synthetic */ Canvas val$bitmapCanvas;
        final /* synthetic */ Paint val$bitmapPaint;
        final /* synthetic */ float val$cx;
        final /* synthetic */ float val$cy;
        final /* synthetic */ boolean val$isDark;
        final /* synthetic */ float val$r;
        final /* synthetic */ float val$x;
        final /* synthetic */ Paint val$xRefPaint;
        final /* synthetic */ float val$y;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context, boolean z, Canvas canvas, float f, float f2, float f3, Paint paint, Bitmap bitmap, Paint paint2, float f4, float f5) {
            super(context);
            ChatThemeBottomSheet.this = r1;
            this.val$isDark = z;
            this.val$bitmapCanvas = canvas;
            this.val$cx = f;
            this.val$cy = f2;
            this.val$r = f3;
            this.val$xRefPaint = paint;
            this.val$bitmap = bitmap;
            this.val$bitmapPaint = paint2;
            this.val$x = f4;
            this.val$y = f5;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.val$isDark) {
                if (ChatThemeBottomSheet.this.changeDayNightViewProgress > 0.0f) {
                    this.val$bitmapCanvas.drawCircle(this.val$cx, this.val$cy, this.val$r * ChatThemeBottomSheet.this.changeDayNightViewProgress, this.val$xRefPaint);
                }
                canvas.drawBitmap(this.val$bitmap, 0.0f, 0.0f, this.val$bitmapPaint);
            } else {
                canvas.drawCircle(this.val$cx, this.val$cy, this.val$r * (1.0f - ChatThemeBottomSheet.this.changeDayNightViewProgress), this.val$bitmapPaint);
            }
            canvas.save();
            canvas.translate(this.val$x, this.val$y);
            ChatThemeBottomSheet.this.darkThemeView.draw(canvas);
            canvas.restore();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements ValueAnimator.AnimatorUpdateListener {
        boolean changedNavigationBarColor = false;
        final /* synthetic */ boolean val$isDark;

        AnonymousClass7(boolean z) {
            ChatThemeBottomSheet.this = r1;
            this.val$isDark = z;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ChatThemeBottomSheet.this.changeDayNightViewProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ChatThemeBottomSheet.this.changeDayNightView.invalidate();
            if (this.changedNavigationBarColor || ChatThemeBottomSheet.this.changeDayNightViewProgress <= 0.5f) {
                return;
            }
            this.changedNavigationBarColor = true;
            AndroidUtilities.setLightNavigationBar(ChatThemeBottomSheet.this.getWindow(), true ^ this.val$isDark);
            AndroidUtilities.setNavigationBarColor(ChatThemeBottomSheet.this.getWindow(), ChatThemeBottomSheet.this.getThemedColor("windowBackgroundGray"));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatThemeBottomSheet$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        AnonymousClass8() {
            ChatThemeBottomSheet.this = r1;
        }

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
    }

    public /* synthetic */ void lambda$setupLightDarkTheme$6(boolean z) {
        Adapter adapter = this.adapter;
        if (adapter == null || adapter.items == null) {
            return;
        }
        setForceDark(z, true);
        ChatThemeItem chatThemeItem = this.selectedItem;
        if (chatThemeItem != null) {
            this.isLightDarkChangeAnimation = true;
            EmojiThemes emojiThemes = chatThemeItem.chatTheme;
            if (emojiThemes.showAsDefaultStub) {
                this.themeDelegate.setCurrentTheme(null, false, Boolean.valueOf(z));
            } else {
                this.themeDelegate.setCurrentTheme(emojiThemes, false, Boolean.valueOf(z));
            }
        }
        Adapter adapter2 = this.adapter;
        if (adapter2 == null || adapter2.items == null) {
            return;
        }
        for (int i = 0; i < this.adapter.items.size(); i++) {
            this.adapter.items.get(i).themeIndex = z ? 1 : 0;
        }
        this.adapter.notifyDataSetChanged();
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

    public void onDataLoaded(List<EmojiThemes> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        boolean z = false;
        ChatThemeItem chatThemeItem = new ChatThemeItem(list.get(0));
        ArrayList arrayList = new ArrayList(list.size());
        EmojiThemes currentTheme = this.themeDelegate.getCurrentTheme();
        arrayList.add(0, chatThemeItem);
        this.selectedItem = chatThemeItem;
        for (int i = 1; i < list.size(); i++) {
            EmojiThemes emojiThemes = list.get(i);
            ChatThemeItem chatThemeItem2 = new ChatThemeItem(emojiThemes);
            emojiThemes.loadPreviewColors(this.currentAccount);
            chatThemeItem2.themeIndex = this.forceDark ? 1 : 0;
            arrayList.add(chatThemeItem2);
        }
        this.adapter.setItems(arrayList);
        this.applyButton.setEnabled(true);
        this.applyTextView.setAlpha(0.0f);
        this.resetTextView.setAlpha(0.0f);
        this.recyclerView.setAlpha(0.0f);
        this.applyTextView.setVisibility(0);
        this.resetTextView.setVisibility(0);
        this.darkThemeView.setVisibility(0);
        if (currentTheme != null) {
            int i2 = 0;
            while (true) {
                if (i2 == arrayList.size()) {
                    i2 = -1;
                    break;
                } else if (((ChatThemeItem) arrayList.get(i2)).chatTheme.getEmoticon().equals(currentTheme.getEmoticon())) {
                    this.selectedItem = (ChatThemeItem) arrayList.get(i2);
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 != -1) {
                this.prevSelectedPosition = i2;
                this.adapter.setSelectedItem(i2);
                if (i2 > 0 && i2 < arrayList.size() / 2) {
                    i2--;
                }
                this.layoutManager.scrollToPositionWithOffset(Math.min(i2, this.adapter.items.size() - 1), 0);
            }
        } else {
            this.adapter.setSelectedItem(0);
            this.layoutManager.scrollToPositionWithOffset(0, 0);
            z = true;
        }
        float f = 1.0f;
        this.recyclerView.animate().alpha(1.0f).setDuration(150L).start();
        this.resetTextView.animate().alpha(z ? 1.0f : 0.0f).setDuration(150L).start();
        ViewPropertyAnimator animate = this.applyTextView.animate();
        if (z) {
            f = 0.0f;
        }
        animate.alpha(f).setDuration(150L).start();
        this.progressView.animate().alpha(0.0f).setListener(new HideViewAfterAnimation(this.progressView)).setDuration(150L).start();
    }

    public void onAnimationStart() {
        List<ChatThemeItem> list;
        Adapter adapter = this.adapter;
        if (adapter != null && (list = adapter.items) != null) {
            for (ChatThemeItem chatThemeItem : list) {
                chatThemeItem.themeIndex = this.forceDark ? 1 : 0;
            }
        }
        if (!this.isLightDarkChangeAnimation) {
            setItemsAnimationProgress(1.0f);
        }
    }

    public void onAnimationEnd() {
        this.isLightDarkChangeAnimation = false;
    }

    private void setForceDark(boolean z, boolean z2) {
        if (this.forceDark == z) {
            return;
        }
        this.forceDark = z;
        int i = 0;
        if (z2) {
            RLottieDrawable rLottieDrawable = this.darkThemeDrawable;
            if (z) {
                i = rLottieDrawable.getFramesCount();
            }
            rLottieDrawable.setCustomEndFrame(i);
            RLottieImageView rLottieImageView = this.darkThemeView;
            if (rLottieImageView == null) {
                return;
            }
            rLottieImageView.playAnimation();
            return;
        }
        int framesCount = z ? this.darkThemeDrawable.getFramesCount() - 1 : 0;
        this.darkThemeDrawable.setCurrentFrame(framesCount, false, true);
        this.darkThemeDrawable.setCustomEndFrame(framesCount);
        RLottieImageView rLottieImageView2 = this.darkThemeView;
        if (rLottieImageView2 == null) {
            return;
        }
        rLottieImageView2.invalidate();
    }

    public void setItemsAnimationProgress(float f) {
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            this.adapter.items.get(i).animationProgress = f;
        }
    }

    private void applySelectedTheme() {
        boolean z;
        ChatThemeItem chatThemeItem = this.selectedItem;
        EmojiThemes emojiThemes = chatThemeItem.chatTheme;
        boolean z2 = emojiThemes.showAsDefaultStub;
        Bulletin bulletin = null;
        EmojiThemes emojiThemes2 = z2 ? null : emojiThemes;
        if (chatThemeItem != null && emojiThemes2 != this.originalTheme) {
            String emoticon = (emojiThemes == null || z2) ? null : emojiThemes.getEmoticon();
            ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.chatActivity.getDialogId(), emoticon, true);
            if (emojiThemes != null && !emojiThemes.showAsDefaultStub) {
                this.themeDelegate.setCurrentTheme(emojiThemes, true, Boolean.valueOf(this.originalIsDark));
            } else {
                this.themeDelegate.setCurrentTheme(null, true, Boolean.valueOf(this.originalIsDark));
            }
            this.isApplyClicked = true;
            TLRPC$User currentUser = this.chatActivity.getCurrentUser();
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
                    stickerSetBulletinLayout.titleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ThemeAlsoDisabledForHint", 2131628600, currentUser.first_name)));
                } else {
                    stickerSetBulletinLayout.titleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ThemeAlsoAppliedForHint", 2131628599, currentUser.first_name)));
                }
                stickerSetBulletinLayout.titleTextView.setTypeface(null);
                bulletin = Bulletin.make(this.chatActivity, stickerSetBulletinLayout, 2750);
            }
        }
        dismiss();
        if (bulletin != null) {
            bulletin.show();
        }
    }

    private boolean hasChanges() {
        if (this.selectedItem == null) {
            return false;
        }
        EmojiThemes emojiThemes = this.originalTheme;
        String str = null;
        String emoticon = emojiThemes != null ? emojiThemes.getEmoticon() : null;
        String str2 = "❌";
        if (TextUtils.isEmpty(emoticon)) {
            emoticon = str2;
        }
        EmojiThemes emojiThemes2 = this.selectedItem.chatTheme;
        if (emojiThemes2 != null) {
            str = emojiThemes2.getEmoticon();
        }
        if (!TextUtils.isEmpty(str)) {
            str2 = str;
        }
        return !ObjectsCompat$$ExternalSyntheticBackport0.m(emoticon, str2);
    }

    @SuppressLint({"NotifyDataSetChanged"})
    /* loaded from: classes3.dex */
    public static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int currentAccount;
        private final int currentViewType;
        public List<ChatThemeItem> items;
        private final Theme.ResourcesProvider resourcesProvider;
        private WeakReference<ThemeSmallPreviewView> selectedViewRef;
        private int selectedItemPosition = -1;
        private HashMap<String, Theme.ThemeInfo> loadingThemes = new HashMap<>();
        private HashMap<Theme.ThemeInfo, String> loadingWallpapers = new HashMap<>();

        public Adapter(int i, Theme.ResourcesProvider resourcesProvider, int i2) {
            this.currentViewType = i2;
            this.resourcesProvider = resourcesProvider;
            this.currentAccount = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new ThemeSmallPreviewView(viewGroup.getContext(), this.currentAccount, this.resourcesProvider, this.currentViewType));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) viewHolder.itemView;
            Theme.ThemeInfo themeInfo = this.items.get(i).chatTheme.getThemeInfo(this.items.get(i).themeIndex);
            if (themeInfo != null && themeInfo.pathToFile != null && !themeInfo.previewParsed && new File(themeInfo.pathToFile).exists()) {
                parseTheme(themeInfo);
            }
            ChatThemeItem chatThemeItem = this.items.get(i);
            ChatThemeItem chatThemeItem2 = themeSmallPreviewView.chatThemeItem;
            boolean z = false;
            boolean z2 = chatThemeItem2 != null && chatThemeItem2.chatTheme.getEmoticon().equals(chatThemeItem.chatTheme.getEmoticon()) && !DrawerProfileCell.switchingTheme && themeSmallPreviewView.lastThemeIndex == chatThemeItem.themeIndex;
            themeSmallPreviewView.setFocusable(true);
            themeSmallPreviewView.setEnabled(true);
            themeSmallPreviewView.setBackgroundColor(Theme.getColor("dialogBackgroundGray"));
            themeSmallPreviewView.setItem(chatThemeItem, z2);
            if (i == this.selectedItemPosition) {
                z = true;
            }
            themeSmallPreviewView.setSelected(z, z2);
            if (i == this.selectedItemPosition) {
                this.selectedViewRef = new WeakReference<>(themeSmallPreviewView);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:75:0x01bd, code lost:
            if (r13.equals("key_chat_wallpaper_gradient_to3") == false) goto L124;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean parseTheme(Theme.ThemeInfo themeInfo) {
            FileInputStream fileInputStream;
            Throwable th;
            FileInputStream fileInputStream2;
            int i;
            boolean z;
            int i2;
            char c;
            String[] split;
            if (themeInfo == null || themeInfo.pathToFile == null) {
                return false;
            }
            int i3 = 1;
            try {
                FileInputStream fileInputStream3 = new FileInputStream(new File(themeInfo.pathToFile));
                int i4 = 0;
                boolean z2 = false;
                while (true) {
                    try {
                        int read = fileInputStream3.read(ThemesHorizontalListCell.bytes);
                        if (read != -1) {
                            int i5 = i4;
                            int i6 = 0;
                            int i7 = 0;
                            while (true) {
                                if (i6 < read) {
                                    byte[] bArr = ThemesHorizontalListCell.bytes;
                                    if (bArr[i6] == 10) {
                                        int i8 = (i6 - i7) + i3;
                                        String str = new String(bArr, i7, i8 - 1, "UTF-8");
                                        if (str.startsWith("WLS=")) {
                                            String substring = str.substring(4);
                                            Uri parse = Uri.parse(substring);
                                            themeInfo.slug = parse.getQueryParameter("slug");
                                            File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                                            themeInfo.pathToWallpaper = new File(filesDirFixed, Utilities.MD5(substring) + ".wp").getAbsolutePath();
                                            String queryParameter = parse.getQueryParameter("mode");
                                            if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                                                int i9 = 0;
                                                while (true) {
                                                    if (i9 < split.length) {
                                                        if ("blur".equals(split[i9])) {
                                                            themeInfo.isBlured = true;
                                                        } else {
                                                            i9++;
                                                        }
                                                    }
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
                                        } else if (str.startsWith("WPS")) {
                                            themeInfo.previewWallpaperOffset = i8 + i5;
                                            fileInputStream2 = fileInputStream3;
                                            z2 = true;
                                        } else {
                                            int indexOf = str.indexOf(61);
                                            if (indexOf != -1) {
                                                String substring2 = str.substring(0, indexOf);
                                                z = z2;
                                                i = read;
                                                fileInputStream = fileInputStream3;
                                                if (!substring2.equals("chat_inBubble")) {
                                                    try {
                                                        if (!substring2.equals("chat_outBubble")) {
                                                            if (!substring2.equals("chat_wallpaper")) {
                                                                if (!substring2.equals("chat_wallpaper_gradient_to")) {
                                                                    if (!substring2.equals("key_chat_wallpaper_gradient_to2")) {
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Throwable th2) {
                                                        th = th2;
                                                        try {
                                                            fileInputStream.close();
                                                        } catch (Throwable unused3) {
                                                        }
                                                        throw th;
                                                    }
                                                }
                                                String substring3 = str.substring(indexOf + 1);
                                                if (substring3.length() > 0 && substring3.charAt(0) == '#') {
                                                    try {
                                                        i2 = Color.parseColor(substring3);
                                                    } catch (Exception unused4) {
                                                        i2 = Utilities.parseInt((CharSequence) substring3).intValue();
                                                    }
                                                } else {
                                                    i2 = Utilities.parseInt((CharSequence) substring3).intValue();
                                                }
                                                switch (substring2.hashCode()) {
                                                    case -1625862693:
                                                        if (substring2.equals("chat_wallpaper")) {
                                                            c = 2;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case -633951866:
                                                        if (substring2.equals("chat_wallpaper_gradient_to")) {
                                                            c = 3;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 1269980952:
                                                        if (substring2.equals("chat_inBubble")) {
                                                            c = 0;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 1381936524:
                                                        if (substring2.equals("key_chat_wallpaper_gradient_to2")) {
                                                            c = 4;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 1381936525:
                                                        if (substring2.equals("key_chat_wallpaper_gradient_to3")) {
                                                            c = 5;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 2052611411:
                                                        if (substring2.equals("chat_outBubble")) {
                                                            c = 1;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    default:
                                                        c = 65535;
                                                        break;
                                                }
                                                if (c == 0) {
                                                    themeInfo.setPreviewInColor(i2);
                                                } else if (c == 1) {
                                                    themeInfo.setPreviewOutColor(i2);
                                                } else if (c == 2) {
                                                    themeInfo.setPreviewBackgroundColor(i2);
                                                } else if (c == 3) {
                                                    themeInfo.previewBackgroundGradientColor1 = i2;
                                                } else if (c == 4) {
                                                    themeInfo.previewBackgroundGradientColor2 = i2;
                                                } else if (c == 5) {
                                                    themeInfo.previewBackgroundGradientColor3 = i2;
                                                }
                                                i7 += i8;
                                                i5 += i8;
                                            }
                                        }
                                        fileInputStream = fileInputStream3;
                                        z = z2;
                                        i = read;
                                        i7 += i8;
                                        i5 += i8;
                                    } else {
                                        fileInputStream = fileInputStream3;
                                        z = z2;
                                        i = read;
                                    }
                                    i6++;
                                    z2 = z;
                                    read = i;
                                    fileInputStream3 = fileInputStream;
                                    i3 = 1;
                                } else {
                                    fileInputStream2 = fileInputStream3;
                                }
                            }
                            if (!z2 && i4 != i5) {
                                fileInputStream2.getChannel().position(i5);
                                i4 = i5;
                                fileInputStream3 = fileInputStream2;
                                i3 = 1;
                            }
                        } else {
                            fileInputStream2 = fileInputStream3;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileInputStream = fileInputStream3;
                    }
                }
                fileInputStream2.close();
            } catch (Throwable th4) {
                FileLog.e(th4);
            }
            if (themeInfo.pathToWallpaper != null && !themeInfo.badWallpaper && !new File(themeInfo.pathToWallpaper).exists()) {
                if (this.loadingWallpapers.containsKey(themeInfo)) {
                    return false;
                }
                this.loadingWallpapers.put(themeInfo, themeInfo.slug);
                TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                tLRPC$TL_inputWallPaperSlug.slug = themeInfo.slug;
                tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                ConnectionsManager.getInstance(themeInfo.account).sendRequest(tLRPC$TL_account_getWallPaper, new ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda1(this, themeInfo));
                return false;
            }
            themeInfo.previewParsed = true;
            return true;
        }

        public /* synthetic */ void lambda$parseTheme$1(Theme.ThemeInfo themeInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda0(this, tLObject, themeInfo));
        }

        public /* synthetic */ void lambda$parseTheme$0(TLObject tLObject, Theme.ThemeInfo themeInfo) {
            if (tLObject instanceof TLRPC$TL_wallPaper) {
                TLRPC$WallPaper tLRPC$WallPaper = (TLRPC$WallPaper) tLObject;
                String attachFileName = FileLoader.getAttachFileName(tLRPC$WallPaper.document);
                if (this.loadingThemes.containsKey(attachFileName)) {
                    return;
                }
                this.loadingThemes.put(attachFileName, themeInfo);
                FileLoader.getInstance(themeInfo.account).loadFile(tLRPC$WallPaper.document, tLRPC$WallPaper, 1, 1);
                return;
            }
            themeInfo.badWallpaper = true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<ChatThemeItem> list = this.items;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        public void setItems(List<ChatThemeItem> list) {
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
                WeakReference<ThemeSmallPreviewView> weakReference = this.selectedViewRef;
                ThemeSmallPreviewView themeSmallPreviewView = weakReference == null ? null : weakReference.get();
                if (themeSmallPreviewView != null) {
                    themeSmallPreviewView.setSelected(false);
                }
            }
            this.selectedItemPosition = i;
            notifyItemChanged(i);
        }
    }

    /* loaded from: classes3.dex */
    public static class ChatThemeItem {
        public float animationProgress;
        public final EmojiThemes chatTheme;
        public Bitmap icon;
        public boolean isSelected;
        public Drawable previewDrawable;
        public int themeIndex;

        public ChatThemeItem(EmojiThemes emojiThemes) {
            this.chatTheme = emojiThemes;
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        String str;
        int i;
        super.show();
        TextView textView = this.resetTextView;
        if (this.themeDelegate.getCurrentTheme() == null) {
            i = 2131625522;
            str = "DoNoSetTheme";
        } else {
            i = 2131625026;
            str = "ChatResetTheme";
        }
        textView.setText(LocaleController.getString(str, i));
    }
}
