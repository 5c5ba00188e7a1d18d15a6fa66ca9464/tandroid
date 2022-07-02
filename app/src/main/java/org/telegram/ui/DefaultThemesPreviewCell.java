package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ThemeSmallPreviewView;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class DefaultThemesPreviewCell extends LinearLayout {
    private final ChatThemeBottomSheet.Adapter adapter;
    TextCell browseThemesCell;
    int currentType;
    RLottieDrawable darkThemeDrawable;
    TextCell dayNightCell;
    private ValueAnimator navBarAnimator;
    private int navBarColor;
    private final FlickerLoadingView progressView;
    private final RecyclerListView recyclerView;
    int themeIndex;
    private LinearLayoutManager layoutManager = null;
    private int selectedPosition = -1;
    private Boolean wasPortrait = null;

    public DefaultThemesPreviewCell(final Context context, final BaseFragment baseFragment, int i) {
        super(context);
        LinearLayoutManager linearLayoutManager;
        this.currentType = i;
        setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        addView(frameLayout, LayoutHelper.createFrame(-1, -2.0f));
        int i2 = 0;
        ChatThemeBottomSheet.Adapter adapter = new ChatThemeBottomSheet.Adapter(baseFragment.getCurrentAccount(), null, this.currentType == 0 ? 0 : 1);
        this.adapter = adapter;
        RecyclerListView recyclerListView = new RecyclerListView(getContext());
        this.recyclerView = recyclerListView;
        recyclerListView.setAdapter(adapter);
        recyclerListView.setSelectorDrawableColor(0);
        recyclerListView.setClipChildren(false);
        recyclerListView.setClipToPadding(false);
        recyclerListView.setHasFixedSize(true);
        recyclerListView.setItemAnimator(null);
        recyclerListView.setNestedScrollingEnabled(false);
        updateLayoutManager();
        recyclerListView.setFocusable(false);
        recyclerListView.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                DefaultThemesPreviewCell.this.lambda$new$0(baseFragment, view, i3);
            }
        });
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(getContext(), null);
        this.progressView = flickerLoadingView;
        flickerLoadingView.setViewType(14);
        flickerLoadingView.setVisibility(0);
        if (this.currentType == 0) {
            frameLayout.addView(flickerLoadingView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 8.0f, 0.0f, 8.0f));
            frameLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 8.0f, 0.0f, 8.0f));
        } else {
            frameLayout.addView(flickerLoadingView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 0.0f, 8.0f, 0.0f, 8.0f));
            frameLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, -2.0f, 8388611, 0.0f, 8.0f, 0.0f, 8.0f));
        }
        recyclerListView.setEmptyView(flickerLoadingView);
        recyclerListView.setAnimateEmptyView(true, 0);
        if (this.currentType == 0) {
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.sun_outline, "2131558541", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.darkThemeDrawable = rLottieDrawable;
            rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
            this.darkThemeDrawable.beginApplyLayerColors();
            this.darkThemeDrawable.commitApplyLayerColors();
            TextCell textCell = new TextCell(context);
            this.dayNightCell = textCell;
            textCell.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 2));
            TextCell textCell2 = this.dayNightCell;
            textCell2.imageLeft = 21;
            addView(textCell2, LayoutHelper.createFrame(-1, -2.0f));
            TextCell textCell3 = new TextCell(context);
            this.browseThemesCell = textCell3;
            textCell3.setTextAndIcon(LocaleController.getString("SettingsBrowseThemes", R.string.SettingsBrowseThemes), R.drawable.msg_colors, false);
            addView(this.browseThemesCell, LayoutHelper.createFrame(-1, -2.0f));
            this.dayNightCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1
                /* JADX WARN: Removed duplicated region for block: B:28:0x0078  */
                /* JADX WARN: Removed duplicated region for block: B:29:0x007d  */
                /* JADX WARN: Removed duplicated region for block: B:32:0x0087  */
                /* JADX WARN: Removed duplicated region for block: B:33:0x008d  */
                /* JADX WARN: Removed duplicated region for block: B:36:0x0151  */
                /* JADX WARN: Removed duplicated region for block: B:37:0x0158  */
                /* JADX WARN: Removed duplicated region for block: B:40:0x015c  */
                /* JADX WARN: Removed duplicated region for block: B:52:0x01d1  */
                /* JADX WARN: Removed duplicated region for block: B:53:0x01e6  */
                @Override // android.view.View.OnClickListener
                @SuppressLint({"NotifyDataSetChanged"})
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onClick(View view) {
                    boolean z;
                    Theme.ThemeInfo themeInfo;
                    final Window window;
                    if (DrawerProfileCell.switchingTheme) {
                        return;
                    }
                    final int color = Theme.getColor("windowBackgroundWhiteBlueText4");
                    final int color2 = Theme.getColor("windowBackgroundGray");
                    DrawerProfileCell.switchingTheme = true;
                    SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
                    String str = "Blue";
                    String string = sharedPreferences.getString("lastDayTheme", str);
                    if (Theme.getTheme(string) == null || Theme.getTheme(string).isDark()) {
                        string = str;
                    }
                    String str2 = "Dark Blue";
                    String string2 = sharedPreferences.getString("lastDarkTheme", str2);
                    if (Theme.getTheme(string2) == null || !Theme.getTheme(string2).isDark()) {
                        string2 = str2;
                    }
                    Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
                    if (!string.equals(string2)) {
                        str2 = string2;
                    } else if (activeTheme.isDark() || string.equals(str2) || string.equals("Night")) {
                        str2 = string2;
                        z = !Theme.isCurrentThemeDark();
                        if (!z) {
                            themeInfo = Theme.getTheme(str2);
                        } else {
                            themeInfo = Theme.getTheme(str);
                        }
                        RLottieDrawable rLottieDrawable2 = DefaultThemesPreviewCell.this.darkThemeDrawable;
                        rLottieDrawable2.setCustomEndFrame(!z ? rLottieDrawable2.getFramesCount() - 1 : 0);
                        DefaultThemesPreviewCell.this.dayNightCell.getImageView().playAnimation();
                        DefaultThemesPreviewCell.this.dayNightCell.getImageView().getLocationInWindow(r9);
                        int[] iArr = {iArr[0] + (DefaultThemesPreviewCell.this.dayNightCell.getImageView().getMeasuredWidth() / 2), iArr[1] + (DefaultThemesPreviewCell.this.dayNightCell.getImageView().getMeasuredHeight() / 2) + AndroidUtilities.dp(3.0f)};
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, iArr, -1, Boolean.valueOf(z), DefaultThemesPreviewCell.this.dayNightCell.getImageView(), DefaultThemesPreviewCell.this.dayNightCell);
                        DefaultThemesPreviewCell.this.updateDayNightMode();
                        DefaultThemesPreviewCell.this.updateSelectedPosition();
                        final int color3 = Theme.getColor("windowBackgroundWhiteBlueText4");
                        DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN));
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(color, color3, ((Float) valueAnimator.getAnimatedValue()).floatValue()), PorterDuff.Mode.SRC_IN));
                            }
                        });
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN));
                                super.onAnimationEnd(animator);
                            }
                        });
                        ofFloat.setDuration(350L);
                        ofFloat.start();
                        final int color4 = Theme.getColor("windowBackgroundGray");
                        Context context2 = context;
                        window = !(context2 instanceof Activity) ? ((Activity) context2).getWindow() : null;
                        if (window != null) {
                            if (DefaultThemesPreviewCell.this.navBarAnimator != null && DefaultThemesPreviewCell.this.navBarAnimator.isRunning()) {
                                int i3 = DefaultThemesPreviewCell.this.navBarColor;
                                DefaultThemesPreviewCell.this.navBarAnimator.cancel();
                                color2 = i3;
                            }
                            DefaultThemesPreviewCell.this.navBarAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                            final float f = z ? 50.0f : 200.0f;
                            DefaultThemesPreviewCell.this.navBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.3
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    float max = Math.max(0.0f, Math.min(1.0f, ((((Float) valueAnimator.getAnimatedValue()).floatValue() * 350.0f) - f) / 150.0f));
                                    DefaultThemesPreviewCell.this.navBarColor = ColorUtils.blendARGB(color2, color4, max);
                                    boolean z2 = false;
                                    AndroidUtilities.setNavigationBarColor(window, DefaultThemesPreviewCell.this.navBarColor, false);
                                    Window window2 = window;
                                    if (AndroidUtilities.computePerceivedBrightness(DefaultThemesPreviewCell.this.navBarColor) >= 0.721f) {
                                        z2 = true;
                                    }
                                    AndroidUtilities.setLightNavigationBar(window2, z2);
                                }
                            });
                            DefaultThemesPreviewCell.this.navBarAnimator.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.4
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    boolean z2 = false;
                                    AndroidUtilities.setNavigationBarColor(window, color4, false);
                                    Window window2 = window;
                                    if (AndroidUtilities.computePerceivedBrightness(color4) >= 0.721f) {
                                        z2 = true;
                                    }
                                    AndroidUtilities.setLightNavigationBar(window2, z2);
                                }
                            });
                            DefaultThemesPreviewCell.this.navBarAnimator.setDuration(350L);
                            DefaultThemesPreviewCell.this.navBarAnimator.start();
                        }
                        if (!Theme.isCurrentThemeDay()) {
                            DefaultThemesPreviewCell.this.dayNightCell.setTextAndIcon(LocaleController.getString("SettingsSwitchToNightMode", R.string.SettingsSwitchToNightMode), (Drawable) DefaultThemesPreviewCell.this.darkThemeDrawable, true);
                            return;
                        } else {
                            DefaultThemesPreviewCell.this.dayNightCell.setTextAndIcon(LocaleController.getString("SettingsSwitchToDayMode", R.string.SettingsSwitchToDayMode), (Drawable) DefaultThemesPreviewCell.this.darkThemeDrawable, true);
                            return;
                        }
                    }
                    str = string;
                    z = !Theme.isCurrentThemeDark();
                    if (!z) {
                    }
                    RLottieDrawable rLottieDrawable22 = DefaultThemesPreviewCell.this.darkThemeDrawable;
                    rLottieDrawable22.setCustomEndFrame(!z ? rLottieDrawable22.getFramesCount() - 1 : 0);
                    DefaultThemesPreviewCell.this.dayNightCell.getImageView().playAnimation();
                    DefaultThemesPreviewCell.this.dayNightCell.getImageView().getLocationInWindow(iArr);
                    int[] iArr2 = {iArr2[0] + (DefaultThemesPreviewCell.this.dayNightCell.getImageView().getMeasuredWidth() / 2), iArr2[1] + (DefaultThemesPreviewCell.this.dayNightCell.getImageView().getMeasuredHeight() / 2) + AndroidUtilities.dp(3.0f)};
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, iArr2, -1, Boolean.valueOf(z), DefaultThemesPreviewCell.this.dayNightCell.getImageView(), DefaultThemesPreviewCell.this.dayNightCell);
                    DefaultThemesPreviewCell.this.updateDayNightMode();
                    DefaultThemesPreviewCell.this.updateSelectedPosition();
                    final int color32 = Theme.getColor("windowBackgroundWhiteBlueText4");
                    DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(color32, PorterDuff.Mode.SRC_IN));
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(color, color32, ((Float) valueAnimator.getAnimatedValue()).floatValue()), PorterDuff.Mode.SRC_IN));
                        }
                    });
                    ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DefaultThemesPreviewCell.1.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DefaultThemesPreviewCell.this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(color32, PorterDuff.Mode.SRC_IN));
                            super.onAnimationEnd(animator);
                        }
                    });
                    ofFloat2.setDuration(350L);
                    ofFloat2.start();
                    final int color42 = Theme.getColor("windowBackgroundGray");
                    Context context22 = context;
                    window = !(context22 instanceof Activity) ? ((Activity) context22).getWindow() : null;
                    if (window != null) {
                    }
                    if (!Theme.isCurrentThemeDay()) {
                    }
                }
            });
            this.darkThemeDrawable.setPlayInDirectionOfCustomEndFrame(true);
            this.browseThemesCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DefaultThemesPreviewCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DefaultThemesPreviewCell.lambda$new$1(BaseFragment.this, view);
                }
            });
            if (!Theme.isCurrentThemeDay()) {
                RLottieDrawable rLottieDrawable2 = this.darkThemeDrawable;
                rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getFramesCount() - 1);
                this.dayNightCell.setTextAndIcon(LocaleController.getString("SettingsSwitchToDayMode", R.string.SettingsSwitchToDayMode), (Drawable) this.darkThemeDrawable, true);
            } else {
                this.dayNightCell.setTextAndIcon(LocaleController.getString("SettingsSwitchToNightMode", R.string.SettingsSwitchToNightMode), (Drawable) this.darkThemeDrawable, true);
            }
        }
        if (!MediaDataController.getInstance(baseFragment.getCurrentAccount()).defaultEmojiThemes.isEmpty()) {
            ArrayList arrayList = new ArrayList(MediaDataController.getInstance(baseFragment.getCurrentAccount()).defaultEmojiThemes);
            if (this.currentType == 0) {
                EmojiThemes createPreviewCustom = EmojiThemes.createPreviewCustom();
                createPreviewCustom.loadPreviewColors(baseFragment.getCurrentAccount());
                ChatThemeBottomSheet.ChatThemeItem chatThemeItem = new ChatThemeBottomSheet.ChatThemeItem(createPreviewCustom);
                chatThemeItem.themeIndex = !Theme.isCurrentThemeDay() ? 2 : i2;
                arrayList.add(chatThemeItem);
            }
            adapter.setItems(arrayList);
        }
        updateDayNightMode();
        updateSelectedPosition();
        updateColors();
        int i3 = this.selectedPosition;
        if (i3 < 0 || (linearLayoutManager = this.layoutManager) == null) {
            return;
        }
        linearLayoutManager.scrollToPositionWithOffset(i3, AndroidUtilities.dp(16.0f));
    }

    public /* synthetic */ void lambda$new$0(BaseFragment baseFragment, View view, int i) {
        ChatThemeBottomSheet.ChatThemeItem chatThemeItem = this.adapter.items.get(i);
        Theme.ThemeInfo themeInfo = chatThemeItem.chatTheme.getThemeInfo(this.themeIndex);
        int accentId = (chatThemeItem.chatTheme.getEmoticon().equals("🏠") || chatThemeItem.chatTheme.getEmoticon().equals("🎨")) ? chatThemeItem.chatTheme.getAccentId(this.themeIndex) : -1;
        if (themeInfo == null) {
            TLRPC$TL_theme tlTheme = chatThemeItem.chatTheme.getTlTheme(this.themeIndex);
            Theme.ThemeInfo theme = Theme.getTheme(Theme.getBaseThemeKey(tlTheme.settings.get(chatThemeItem.chatTheme.getSettingsIndex(this.themeIndex))));
            if (theme != null) {
                Theme.ThemeAccent themeAccent = theme.accentsByThemeId.get(tlTheme.id);
                if (themeAccent == null) {
                    themeAccent = theme.createNewAccent(tlTheme, baseFragment.getCurrentAccount());
                }
                accentId = themeAccent.id;
                theme.setCurrentAccentId(accentId);
            }
            themeInfo = theme;
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, null, Integer.valueOf(accentId));
        this.selectedPosition = i;
        int i2 = 0;
        while (i2 < this.adapter.items.size()) {
            this.adapter.items.get(i2).isSelected = i2 == this.selectedPosition;
            i2++;
        }
        this.adapter.setSelectedItem(this.selectedPosition);
        for (int i3 = 0; i3 < this.recyclerView.getChildCount(); i3++) {
            ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) this.recyclerView.getChildAt(i3);
            if (themeSmallPreviewView != view) {
                themeSmallPreviewView.cancelAnimation();
            }
        }
        ((ThemeSmallPreviewView) view).playEmojiAnimation();
        if (themeInfo != null) {
            SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
            edit.putString((this.currentType == 1 || themeInfo.isDark()) ? "lastDarkTheme" : "lastDayTheme", themeInfo.getKey());
            edit.commit();
        }
    }

    public static /* synthetic */ void lambda$new$1(BaseFragment baseFragment, View view) {
        baseFragment.presentFragment(new ThemeActivity(3));
    }

    public void updateLayoutManager() {
        Point point = AndroidUtilities.displaySize;
        boolean z = point.y > point.x;
        Boolean bool = this.wasPortrait;
        if (bool == null || bool.booleanValue() != z) {
            if (this.currentType == 0) {
                if (this.layoutManager == null) {
                    RecyclerListView recyclerListView = this.recyclerView;
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
                    this.layoutManager = linearLayoutManager;
                    recyclerListView.setLayoutManager(linearLayoutManager);
                }
            } else {
                int i = z ? 3 : 9;
                LinearLayoutManager linearLayoutManager2 = this.layoutManager;
                if (linearLayoutManager2 instanceof GridLayoutManager) {
                    ((GridLayoutManager) linearLayoutManager2).setSpanCount(i);
                } else {
                    this.recyclerView.setHasFixedSize(false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), i);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(this) { // from class: org.telegram.ui.DefaultThemesPreviewCell.2
                        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                        public int getSpanSize(int i2) {
                            return 1;
                        }
                    });
                    RecyclerListView recyclerListView2 = this.recyclerView;
                    this.layoutManager = gridLayoutManager;
                    recyclerListView2.setLayoutManager(gridLayoutManager);
                }
            }
            this.wasPortrait = Boolean.valueOf(z);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        updateLayoutManager();
        super.onMeasure(i, i2);
    }

    public void updateDayNightMode() {
        int i;
        int i2;
        int i3 = 2;
        if (this.currentType == 0) {
            if (Theme.isCurrentThemeDay()) {
                i3 = 0;
            }
            this.themeIndex = i3;
        } else if (Theme.getActiveTheme().getKey().equals("Blue")) {
            this.themeIndex = 0;
        } else if (Theme.getActiveTheme().getKey().equals("Day")) {
            this.themeIndex = 1;
        } else if (Theme.getActiveTheme().getKey().equals("Night")) {
            this.themeIndex = 2;
        } else if (Theme.getActiveTheme().getKey().equals("Dark Blue")) {
            this.themeIndex = 3;
        } else {
            if (Theme.isCurrentThemeDay() && ((i2 = this.themeIndex) == 2 || i2 == 3)) {
                this.themeIndex = 0;
            }
            if (!Theme.isCurrentThemeDay() && ((i = this.themeIndex) == 0 || i == 1)) {
                this.themeIndex = 2;
            }
        }
        if (this.adapter.items != null) {
            for (int i4 = 0; i4 < this.adapter.items.size(); i4++) {
                this.adapter.items.get(i4).themeIndex = this.themeIndex;
            }
            ChatThemeBottomSheet.Adapter adapter = this.adapter;
            adapter.notifyItemRangeChanged(0, adapter.items.size());
        }
        updateSelectedPosition();
    }

    public void updateSelectedPosition() {
        if (this.adapter.items == null) {
            return;
        }
        this.selectedPosition = -1;
        int i = 0;
        while (true) {
            if (i >= this.adapter.items.size()) {
                break;
            }
            TLRPC$TL_theme tlTheme = this.adapter.items.get(i).chatTheme.getTlTheme(this.themeIndex);
            Theme.ThemeInfo themeInfo = this.adapter.items.get(i).chatTheme.getThemeInfo(this.themeIndex);
            if (tlTheme != null) {
                if (Theme.getActiveTheme().name.equals(Theme.getBaseThemeKey(tlTheme.settings.get(this.adapter.items.get(i).chatTheme.getSettingsIndex(this.themeIndex))))) {
                    if (Theme.getActiveTheme().accentsByThemeId == null) {
                        this.selectedPosition = i;
                        break;
                    }
                    Theme.ThemeAccent themeAccent = Theme.getActiveTheme().accentsByThemeId.get(tlTheme.id);
                    if (themeAccent != null && themeAccent.id == Theme.getActiveTheme().currentAccentId) {
                        this.selectedPosition = i;
                        break;
                    }
                } else {
                    continue;
                }
                i++;
            } else {
                if (themeInfo != null) {
                    if (Theme.getActiveTheme().name.equals(themeInfo.getKey()) && this.adapter.items.get(i).chatTheme.getAccentId(this.themeIndex) == Theme.getActiveTheme().currentAccentId) {
                        this.selectedPosition = i;
                        break;
                    }
                } else {
                    continue;
                }
                i++;
            }
        }
        if (this.selectedPosition == -1 && this.currentType != 3) {
            this.selectedPosition = this.adapter.items.size() - 1;
        }
        int i2 = 0;
        while (i2 < this.adapter.items.size()) {
            this.adapter.items.get(i2).isSelected = i2 == this.selectedPosition;
            i2++;
        }
        this.adapter.setSelectedItem(this.selectedPosition);
    }

    public void updateColors() {
        if (this.currentType == 0) {
            this.darkThemeDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlueText4"), PorterDuff.Mode.SRC_IN));
            Theme.setSelectorDrawableColor(this.dayNightCell.getBackground(), Theme.getColor("listSelectorSDK21"), true);
            this.browseThemesCell.setBackground(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("windowBackgroundWhite"), Theme.getColor("listSelectorSDK21")));
            this.dayNightCell.setColors(null, "windowBackgroundWhiteBlueText4");
            this.browseThemesCell.setColors("windowBackgroundWhiteBlueText4", "windowBackgroundWhiteBlueText4");
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        updateColors();
    }
}
