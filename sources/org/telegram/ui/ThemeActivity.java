package org.telegram.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AppIconsSelectorCell;
import org.telegram.ui.Cells.BrightnessControlCell;
import org.telegram.ui.Cells.ChatListCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
import org.telegram.ui.Cells.ThemeTypeCell;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.SwipeGestureSettingsView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.ThemeActivity;
import org.telegram.ui.web.SearchEngine;
import org.telegram.ui.web.WebBrowserSettings;

/* loaded from: classes4.dex */
public class ThemeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int appIconHeaderRow;
    private int appIconSelectorRow;
    private int appIconShadowRow;
    private int automaticBrightnessInfoRow;
    private int automaticBrightnessRow;
    private int automaticHeaderRow;
    private int backgroundRow;
    private int bluetoothScoRow;
    private int browserRow;
    private int bubbleRadiusHeaderRow;
    private int bubbleRadiusInfoRow;
    private int bubbleRadiusRow;
    private int changeUserColor;
    private int chatBlurRow;
    private int chatListHeaderRow;
    private int chatListInfoRow;
    private int chatListRow;
    private int contactsReimportRow;
    private int contactsSortRow;
    private int createNewThemeRow;
    private int currentType;
    private ArrayList darkThemes = new ArrayList();
    private ArrayList defaultThemes = new ArrayList();
    private int directShareRow;
    private int distanceRow;
    private int editThemeRow;
    private int enableAnimationsRow;
    private GpsLocationListener gpsLocationListener;
    boolean hasThemeAccents;
    private boolean highlightSensitiveRow;
    boolean lastIsDarkTheme;
    private int lastShadowRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int liteModeInfoRow;
    private int liteModeRow;
    private int mediaSoundHeaderRow;
    private int mediaSoundSectionRow;
    private ActionBarMenuItem menuItem;
    private GpsLocationListener networkLocationListener;
    private int newThemeInfoRow;
    private int nextMediaTapRow;
    private int nightAutomaticRow;
    private int nightDisabledRow;
    private int nightScheduledRow;
    private int nightSystemDefaultRow;
    private int nightThemeRow;
    private int nightTypeInfoRow;
    private int otherHeaderRow;
    private int otherSectionRow;
    private int pauseOnMediaRow;
    private int pauseOnRecordRow;
    private int preferedHeaderRow;
    private boolean previousByLocation;
    private int previousUpdatedType;
    private int raiseToListenRow;
    private int raiseToSpeakRow;
    private int rowCount;
    private int saveToGalleryOption1Row;
    private int saveToGalleryOption2Row;
    private int saveToGallerySectionRow;
    private int scheduleFromRow;
    private int scheduleFromToInfoRow;
    private int scheduleHeaderRow;
    private int scheduleLocationInfoRow;
    private int scheduleLocationRow;
    private int scheduleToRow;
    private int scheduleUpdateLocationRow;
    private int searchEngineRow;
    private int selectThemeHeaderRow;
    private int sendByEnterRow;
    private int sensitiveContentRow;
    private int settings2Row;
    private int settingsRow;
    private Theme.ThemeAccent sharingAccent;
    private AlertDialog sharingProgressDialog;
    private Theme.ThemeInfo sharingTheme;
    private int stickersInfoRow;
    private int stickersRow;
    private int stickersSectionRow;
    private RLottieDrawable sunDrawable;
    private int swipeGestureHeaderRow;
    private int swipeGestureInfoRow;
    private int swipeGestureRow;
    private int textSizeHeaderRow;
    private int textSizeRow;
    private int themeAccentListRow;
    private int themeHeaderRow;
    private int themeInfoRow;
    private int themeListRow;
    private int themeListRow2;
    private int themePreviewRow;
    private ThemesHorizontalListCell themesHorizontalListCell;
    private boolean updateDistance;
    private boolean updateRecordViaSco;
    private boolean updateSearchEngine;
    private boolean updatingLocation;

    class 1 extends ActionBar.ActionBarMenuOnItemClick {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(DialogInterface dialogInterface, int i) {
            boolean fontSize = ThemeActivity.this.setFontSize(AndroidUtilities.isTablet() ? 18 : 16);
            if (ThemeActivity.this.setBubbleRadius(17, true)) {
                fontSize = true;
            }
            if (fontSize) {
                ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.textSizeRow, new Object());
                ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.bubbleRadiusRow, new Object());
            }
            if (ThemeActivity.this.themesHorizontalListCell != null) {
                Theme.ThemeInfo theme = Theme.getTheme("Blue");
                Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
                Theme.ThemeAccent themeAccent = (Theme.ThemeAccent) theme.themeAccentsMap.get(Theme.DEFALT_THEME_ACCENT_ID);
                if (themeAccent != null) {
                    Theme.OverrideWallpaperInfo overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo.slug = "d";
                    overrideWallpaperInfo.fileName = "Blue_99_wp.jpg";
                    overrideWallpaperInfo.originalFileName = "Blue_99_wp.jpg";
                    themeAccent.overrideWallpaper = overrideWallpaperInfo;
                    theme.setOverrideWallpaper(overrideWallpaperInfo);
                }
                if (theme != currentTheme) {
                    theme.setCurrentAccentId(Theme.DEFALT_THEME_ACCENT_ID);
                    Theme.saveThemeAccents(theme, true, false, true, false);
                    ThemeActivity.this.themesHorizontalListCell.selectTheme(theme);
                    ThemeActivity.this.themesHorizontalListCell.smoothScrollToPosition(0);
                    return;
                }
                if (theme.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID) {
                    Theme.reloadWallpaper(true);
                } else {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, currentTheme, Boolean.valueOf(ThemeActivity.this.currentType == 1), null, Integer.valueOf(Theme.DEFALT_THEME_ACCENT_ID));
                    ThemeActivity.this.listAdapter.notifyItemChanged(ThemeActivity.this.themeAccentListRow);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:48:0x0159  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x015e  */
        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onItemClick(int i) {
            if (i == -1) {
                ThemeActivity.this.lambda$onBackPressed$321();
                return;
            }
            if (i == 1) {
                ThemeActivity.this.createNewTheme();
                return;
            }
            if (i == 2) {
                Theme.ThemeAccent accent = Theme.getCurrentTheme().getAccent(false);
                if (accent.info == null) {
                    ThemeActivity.this.getMessagesController().saveThemeToServer(accent.parentTheme, accent);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShareTheme, accent.parentTheme, accent);
                    return;
                }
                String str = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + accent.info.slug;
                ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, str, false, str, false));
                return;
            }
            if (i == 3) {
                ThemeActivity.this.editTheme();
                return;
            }
            if (i == 4) {
                if (ThemeActivity.this.getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("ThemeResetToDefaultsTitle", R.string.ThemeResetToDefaultsTitle));
                builder.setMessage(LocaleController.getString("ThemeResetToDefaultsText", R.string.ThemeResetToDefaultsText));
                builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$1$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ThemeActivity.1.this.lambda$onItemClick$0(dialogInterface, i2);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create = builder.create();
                ThemeActivity.this.showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            }
            if (i == 5) {
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
                String str2 = "Blue";
                String string = sharedPreferences.getString("lastDayTheme", "Blue");
                if (Theme.getTheme(string) == null || Theme.getTheme(string).isDark()) {
                    string = "Blue";
                }
                String str3 = "Dark Blue";
                String string2 = sharedPreferences.getString("lastDarkTheme", "Dark Blue");
                if (Theme.getTheme(string2) == null || !Theme.getTheme(string2).isDark()) {
                    string2 = "Dark Blue";
                }
                Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
                if (!string.equals(string2)) {
                    str3 = string2;
                } else if (activeTheme.isDark() || string.equals("Dark Blue") || string.equals("Night")) {
                    str3 = string2;
                    boolean equals = str2.equals(activeTheme.getKey());
                    Theme.ThemeInfo theme = !equals ? Theme.getTheme(str3) : Theme.getTheme(str2);
                    int[] iArr = new int[2];
                    ThemeActivity.this.menuItem.getIconView().getLocationInWindow(iArr);
                    iArr[0] = iArr[0] + (ThemeActivity.this.menuItem.getIconView().getMeasuredWidth() / 2);
                    iArr[1] = iArr[1] + (ThemeActivity.this.menuItem.getIconView().getMeasuredHeight() / 2);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, theme, Boolean.FALSE, iArr, -1, Boolean.valueOf(equals), ThemeActivity.this.menuItem.getIconView());
                    ThemeActivity.this.updateRows(true);
                    Theme.turnOffAutoNight(ThemeActivity.this);
                }
                str2 = string;
                boolean equals2 = str2.equals(activeTheme.getKey());
                if (!equals2) {
                }
                int[] iArr2 = new int[2];
                ThemeActivity.this.menuItem.getIconView().getLocationInWindow(iArr2);
                iArr2[0] = iArr2[0] + (ThemeActivity.this.menuItem.getIconView().getMeasuredWidth() / 2);
                iArr2[1] = iArr2[1] + (ThemeActivity.this.menuItem.getIconView().getMeasuredHeight() / 2);
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, theme, Boolean.FALSE, iArr2, -1, Boolean.valueOf(equals2), ThemeActivity.this.menuItem.getIconView());
                ThemeActivity.this.updateRows(true);
                Theme.turnOffAutoNight(ThemeActivity.this);
            }
        }
    }

    private class BubbleRadiusCell extends FrameLayout {
        private int endRadius;
        private SeekBarView sizeBar;
        private int startRadius;
        private TextPaint textPaint;

        public BubbleRadiusCell(Context context) {
            super(context);
            this.startRadius = 0;
            this.endRadius = 17;
            setWillNotDraw(false);
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(16.0f));
            SeekBarView seekBarView = new SeekBarView(context);
            this.sizeBar = seekBarView;
            seekBarView.setReportChanges(true);
            this.sizeBar.setSeparatorsCount((this.endRadius - this.startRadius) + 1);
            this.sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.ThemeActivity.BubbleRadiusCell.1
                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public CharSequence getContentDescription() {
                    return String.valueOf(Math.round(BubbleRadiusCell.this.startRadius + ((BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius) * BubbleRadiusCell.this.sizeBar.getProgress())));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public int getStepsCount() {
                    return BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius;
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarDrag(boolean z, float f) {
                    ThemeActivity.this.setBubbleRadius(Math.round(r4.startRadius + ((BubbleRadiusCell.this.endRadius - BubbleRadiusCell.this.startRadius) * f)), false);
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarPressed(boolean z) {
                }
            });
            this.sizeBar.setImportantForAccessibility(2);
            addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 5.0f, 39.0f, 0.0f));
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.sizeBar.invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText("" + SharedConfig.bubbleRadius, getMeasuredWidth() - AndroidUtilities.dp(39.0f), AndroidUtilities.dp(28.0f), this.textPaint);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            this.sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
            SeekBarView seekBarView = this.sizeBar;
            int i3 = SharedConfig.bubbleRadius;
            int i4 = this.startRadius;
            seekBarView.setProgress((i3 - i4) / (this.endRadius - i4));
        }

        @Override // android.view.View
        public boolean performAccessibilityAction(int i, Bundle bundle) {
            return super.performAccessibilityAction(i, bundle) || this.sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, i, bundle);
        }
    }

    private class GpsLocationListener implements LocationListener {
        private GpsLocationListener() {
        }

        /* synthetic */ GpsLocationListener(ThemeActivity themeActivity, 1 r2) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            ThemeActivity.this.stopLocationUpdate();
            ThemeActivity.this.updateSunTime(location, false);
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private static class InnerAccentView extends View {
        private ObjectAnimator checkAnimator;
        private boolean checked;
        private float checkedState;
        private Theme.ThemeAccent currentAccent;
        private Theme.ThemeInfo currentTheme;
        private final Paint paint;

        InnerAccentView(Context context) {
            super(context);
            this.paint = new Paint(1);
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateCheckedState(false);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float dp = AndroidUtilities.dp(20.0f);
            float measuredWidth = getMeasuredWidth() * 0.5f;
            float measuredHeight = getMeasuredHeight() * 0.5f;
            this.paint.setColor(this.currentAccent.accentColor);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
            this.paint.setAlpha(Math.round(this.checkedState * 255.0f));
            canvas.drawCircle(measuredWidth, measuredHeight, dp - (this.paint.getStrokeWidth() * 0.5f), this.paint);
            this.paint.setAlpha(NotificationCenter.notificationsCountUpdated);
            this.paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(measuredWidth, measuredHeight, dp - (AndroidUtilities.dp(5.0f) * this.checkedState), this.paint);
            if (this.checkedState != 0.0f) {
                this.paint.setColor(-1);
                this.paint.setAlpha(Math.round(this.checkedState * 255.0f));
                canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(2.0f), this.paint);
                canvas.drawCircle(measuredWidth - (AndroidUtilities.dp(7.0f) * this.checkedState), measuredHeight, AndroidUtilities.dp(2.0f), this.paint);
                canvas.drawCircle((AndroidUtilities.dp(7.0f) * this.checkedState) + measuredWidth, measuredHeight, AndroidUtilities.dp(2.0f), this.paint);
            }
            int i = this.currentAccent.myMessagesAccentColor;
            if (i == 0 || this.checkedState == 1.0f) {
                return;
            }
            this.paint.setColor(i);
            canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(8.0f) * (1.0f - this.checkedState), this.paint);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(LocaleController.getString("ColorPickerMainColor", R.string.ColorPickerMainColor));
            accessibilityNodeInfo.setClassName(Button.class.getName());
            accessibilityNodeInfo.setChecked(this.checked);
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setEnabled(true);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824));
        }

        public void setCheckedState(float f) {
            this.checkedState = f;
            invalidate();
        }

        void setThemeAndColor(Theme.ThemeInfo themeInfo, Theme.ThemeAccent themeAccent) {
            this.currentTheme = themeInfo;
            this.currentAccent = themeAccent;
            updateCheckedState(false);
        }

        void updateCheckedState(boolean z) {
            this.checked = this.currentTheme.currentAccentId == this.currentAccent.id;
            ObjectAnimator objectAnimator = this.checkAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            if (!z) {
                setCheckedState(this.checked ? 1.0f : 0.0f);
                return;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "checkedState", this.checked ? 1.0f : 0.0f);
            this.checkAnimator = ofFloat;
            ofFloat.setDuration(200L);
            this.checkAnimator.start();
        }
    }

    private static class InnerCustomAccentView extends View {
        private int[] colors;
        private final Paint paint;

        InnerCustomAccentView(Context context) {
            super(context);
            this.paint = new Paint(1);
            this.colors = new int[7];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTheme(Theme.ThemeInfo themeInfo) {
            if (themeInfo.defaultAccentCount >= 8) {
                this.colors = new int[]{themeInfo.getAccentColor(6), themeInfo.getAccentColor(4), themeInfo.getAccentColor(7), themeInfo.getAccentColor(2), themeInfo.getAccentColor(0), themeInfo.getAccentColor(5), themeInfo.getAccentColor(3)};
            } else {
                this.colors = new int[7];
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float measuredWidth = getMeasuredWidth() * 0.5f;
            float measuredHeight = getMeasuredHeight() * 0.5f;
            float dp = AndroidUtilities.dp(5.0f);
            float dp2 = AndroidUtilities.dp(20.0f) - dp;
            this.paint.setStyle(Paint.Style.FILL);
            int i = 0;
            this.paint.setColor(this.colors[0]);
            canvas.drawCircle(measuredWidth, measuredHeight, dp, this.paint);
            double d = 0.0d;
            while (i < 6) {
                float sin = (((float) Math.sin(d)) * dp2) + measuredWidth;
                float cos = measuredHeight - (((float) Math.cos(d)) * dp2);
                i++;
                this.paint.setColor(this.colors[i]);
                canvas.drawCircle(sin, cos, dp, this.paint);
                d += 1.0471975511965976d;
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(LocaleController.getString("ColorPickerMainColor", R.string.ColorPickerMainColor));
            accessibilityNodeInfo.setClassName(Button.class.getName());
            accessibilityNodeInfo.setEnabled(true);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(62.0f), 1073741824));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListAdapter extends RecyclerListView.SelectionAdapter {
        private boolean first = true;
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:14:0x00b9  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x00d2  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00bd  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onCreateViewHolder$2(ThemeAccentsListAdapter themeAccentsListAdapter, RecyclerListView recyclerListView, View view, int i) {
            ThemeActivity themeActivity;
            ThemePreviewActivity themePreviewActivity;
            int i2;
            int childCount;
            Theme.ThemeInfo currentNightTheme = ThemeActivity.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme();
            if (i == themeAccentsListAdapter.getItemCount() - 1) {
                themeActivity = ThemeActivity.this;
                themePreviewActivity = new ThemePreviewActivity(currentNightTheme, false, 1, false, themeActivity.currentType == 1);
            } else {
                Theme.ThemeAccent themeAccent = (Theme.ThemeAccent) themeAccentsListAdapter.themeAccents.get(i);
                if (!TextUtils.isEmpty(themeAccent.patternSlug) && themeAccent.id != Theme.DEFALT_THEME_ACCENT_ID) {
                    Theme.PatternsLoader.createLoader(false);
                }
                int i3 = currentNightTheme.currentAccentId;
                int i4 = themeAccent.id;
                if (i3 != i4) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, currentNightTheme, Boolean.valueOf(ThemeActivity.this.currentType == 1), null, Integer.valueOf(themeAccent.id));
                    EmojiThemes.saveCustomTheme(currentNightTheme, themeAccent.id);
                    Theme.turnOffAutoNight(ThemeActivity.this);
                    int left = view.getLeft();
                    int right = view.getRight();
                    int dp = AndroidUtilities.dp(52.0f);
                    i2 = left - dp;
                    if (i2 >= 0) {
                        recyclerListView.smoothScrollBy(i2, 0);
                    } else {
                        int i5 = right + dp;
                        if (i5 > recyclerListView.getMeasuredWidth()) {
                            recyclerListView.smoothScrollBy(i5 - recyclerListView.getMeasuredWidth(), 0);
                        }
                    }
                    childCount = recyclerListView.getChildCount();
                    for (int i6 = 0; i6 < childCount; i6++) {
                        View childAt = recyclerListView.getChildAt(i6);
                        if (childAt instanceof InnerAccentView) {
                            ((InnerAccentView) childAt).updateCheckedState(true);
                        }
                    }
                }
                themeActivity = ThemeActivity.this;
                themePreviewActivity = new ThemePreviewActivity(currentNightTheme, false, 1, i4 >= 100, themeActivity.currentType == 1);
            }
            themeActivity.presentFragment(themePreviewActivity);
            int left2 = view.getLeft();
            int right2 = view.getRight();
            int dp2 = AndroidUtilities.dp(52.0f);
            i2 = left2 - dp2;
            if (i2 >= 0) {
            }
            childCount = recyclerListView.getChildCount();
            while (i6 < childCount) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$3(ThemeAccentsListAdapter themeAccentsListAdapter, Theme.ThemeAccent themeAccent, DialogInterface dialogInterface, int i) {
            if (Theme.deleteThemeAccent(themeAccentsListAdapter.currentTheme, themeAccent, true)) {
                Theme.refreshThemeColors();
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, Theme.getActiveTheme(), Boolean.valueOf(ThemeActivity.this.currentType == 1), null, -1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$4(final Theme.ThemeAccent themeAccent, final ThemeAccentsListAdapter themeAccentsListAdapter, DialogInterface dialogInterface, int i) {
            if (ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            if (i == 0) {
                AlertsCreator.createThemeCreateDialog(ThemeActivity.this, i == 1 ? 2 : 1, themeAccent.parentTheme, themeAccent);
                return;
            }
            if (i == 1) {
                if (themeAccent.info == null) {
                    ThemeActivity.this.getMessagesController().saveThemeToServer(themeAccent.parentTheme, themeAccent);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShareTheme, themeAccent.parentTheme, themeAccent);
                    return;
                }
                String str = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + themeAccent.info.slug;
                ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, str, false, str, false));
                return;
            }
            if (i == 2) {
                ThemeActivity.this.presentFragment(new ThemeSetUrlActivity(themeAccent.parentTheme, themeAccent, false));
                return;
            }
            if (i != 3 || ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("DeleteThemeTitle", R.string.DeleteThemeTitle));
            builder.setMessage(LocaleController.getString("DeleteThemeAlert", R.string.DeleteThemeAlert));
            builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    ThemeActivity.ListAdapter.this.lambda$onCreateViewHolder$3(themeAccentsListAdapter, themeAccent, dialogInterface2, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            ThemeActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$5(final ThemeAccentsListAdapter themeAccentsListAdapter, View view, int i) {
            if (i >= 0 && i < themeAccentsListAdapter.themeAccents.size()) {
                final Theme.ThemeAccent themeAccent = (Theme.ThemeAccent) themeAccentsListAdapter.themeAccents.get(i);
                if (themeAccent.id >= 100 && !themeAccent.isDefault) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                    String string = LocaleController.getString("OpenInEditor", R.string.OpenInEditor);
                    String string2 = LocaleController.getString("ShareTheme", R.string.ShareTheme);
                    TLRPC.TL_theme tL_theme = themeAccent.info;
                    builder.setItems(new CharSequence[]{string, string2, (tL_theme == null || !tL_theme.creator) ? null : LocaleController.getString("ThemeSetUrl", R.string.ThemeSetUrl), LocaleController.getString("DeleteTheme", R.string.DeleteTheme)}, new int[]{R.drawable.msg_edit, R.drawable.msg_share, R.drawable.msg_link, R.drawable.msg_delete}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            ThemeActivity.ListAdapter.this.lambda$onCreateViewHolder$4(themeAccent, themeAccentsListAdapter, dialogInterface, i2);
                        }
                    });
                    AlertDialog create = builder.create();
                    ThemeActivity.this.showDialog(create);
                    create.setItemColor(create.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showOptionsForTheme$0(Theme.ThemeInfo themeInfo, DialogInterface dialogInterface, int i) {
            MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, null, themeInfo == Theme.getCurrentNightTheme(), true);
            if (Theme.deleteTheme(themeInfo)) {
                ((BaseFragment) ThemeActivity.this).parentLayout.rebuildAllFragmentViews(true, true);
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeListUpdated, new Object[0]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00f3  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0116 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0117 A[Catch: Exception -> 0x0156, TRY_LEAVE, TryCatch #3 {Exception -> 0x0156, blocks: (B:37:0x0110, B:40:0x0117, B:45:0x015b, B:43:0x0151, B:44:0x0158), top: B:36:0x0110 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$showOptionsForTheme$1(final Theme.ThemeInfo themeInfo, DialogInterface dialogInterface, int i) {
            File assetFile;
            String str;
            File file;
            FileOutputStream fileOutputStream;
            if (ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            FileOutputStream fileOutputStream2 = null;
            if (i == 0) {
                if (themeInfo.info == null) {
                    ThemeActivity.this.getMessagesController().saveThemeToServer(themeInfo, null);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShareTheme, themeInfo, null);
                    return;
                }
                String str2 = "https://" + ThemeActivity.this.getMessagesController().linkPrefix + "/addtheme/" + themeInfo.info.slug;
                ThemeActivity.this.showDialog(new ShareAlert(ThemeActivity.this.getParentActivity(), null, str2, false, str2, false));
                return;
            }
            if (i != 1) {
                if (i == 2) {
                    if (((BaseFragment) ThemeActivity.this).parentLayout != null) {
                        Theme.applyTheme(themeInfo);
                        ((BaseFragment) ThemeActivity.this).parentLayout.rebuildAllFragmentViews(true, true);
                        new ThemeEditorView().show(ThemeActivity.this.getParentActivity(), themeInfo);
                        return;
                    }
                    return;
                }
                if (i == 3) {
                    ThemeActivity.this.presentFragment(new ThemeSetUrlActivity(themeInfo, null, false));
                    return;
                }
                if (ThemeActivity.this.getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("DeleteThemeTitle", R.string.DeleteThemeTitle));
                builder.setMessage(LocaleController.getString("DeleteThemeAlert", R.string.DeleteThemeAlert));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface2, int i2) {
                        ThemeActivity.ListAdapter.this.lambda$showOptionsForTheme$0(themeInfo, dialogInterface2, i2);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create = builder.create();
                ThemeActivity.this.showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            }
            if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
                StringBuilder sb = new StringBuilder();
                int[] defaultColors = Theme.getDefaultColors();
                for (int i2 = 0; i2 < defaultColors.length; i2++) {
                    sb.append(ThemeColors.getStringName(i2));
                    sb.append("=");
                    sb.append(defaultColors[i2]);
                    sb.append("\n");
                }
                assetFile = new File(ApplicationLoader.getFilesDirFixed(), "default_theme.attheme");
                try {
                    try {
                        try {
                            fileOutputStream = new FileOutputStream(assetFile);
                        } catch (Exception e) {
                            e = e;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                try {
                    fileOutputStream.write(AndroidUtilities.getStringBytes(sb.toString()));
                    fileOutputStream.close();
                } catch (Exception e3) {
                    e = e3;
                    fileOutputStream2 = fileOutputStream;
                    FileLog.e(e);
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                    str = themeInfo.name;
                    if (!str.endsWith(".attheme")) {
                    }
                    file = new File(FileLoader.getDirectory(4), FileLoader.fixFileName(str));
                    if (AndroidUtilities.copyFile(assetFile, file)) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            FileLog.e(e4);
                        }
                    }
                    throw th;
                }
            } else {
                String str3 = themeInfo.assetName;
                assetFile = str3 != null ? Theme.getAssetFile(str3) : new File(themeInfo.pathToFile);
            }
            str = themeInfo.name;
            if (!str.endsWith(".attheme")) {
                str = str + ".attheme";
            }
            file = new File(FileLoader.getDirectory(4), FileLoader.fixFileName(str));
            try {
                if (AndroidUtilities.copyFile(assetFile, file)) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/xml");
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(ThemeActivity.this.getParentActivity(), ApplicationLoader.getApplicationId() + ".provider", file));
                        intent.setFlags(1);
                    } catch (Exception unused) {
                    }
                    ThemeActivity.this.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
                }
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                ThemeActivity.this.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
            } catch (Exception e5) {
                FileLog.e(e5);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showOptionsForTheme(final Theme.ThemeInfo themeInfo) {
            int[] iArr;
            CharSequence[] charSequenceArr;
            if (ThemeActivity.this.getParentActivity() != null) {
                if ((themeInfo.info == null || themeInfo.themeLoaded) && ThemeActivity.this.currentType != 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                    boolean z = false;
                    if (themeInfo.pathToFile == null) {
                        charSequenceArr = new CharSequence[]{null, LocaleController.getString("ExportTheme", R.string.ExportTheme)};
                        iArr = new int[]{0, R.drawable.msg_shareout};
                    } else {
                        TLRPC.TL_theme tL_theme = themeInfo.info;
                        boolean z2 = tL_theme == null || !tL_theme.isDefault;
                        String string = LocaleController.getString("ShareFile", R.string.ShareFile);
                        String string2 = LocaleController.getString("ExportTheme", R.string.ExportTheme);
                        TLRPC.TL_theme tL_theme2 = themeInfo.info;
                        String string3 = (tL_theme2 == null || (!tL_theme2.isDefault && tL_theme2.creator)) ? LocaleController.getString("Edit", R.string.Edit) : null;
                        TLRPC.TL_theme tL_theme3 = themeInfo.info;
                        CharSequence[] charSequenceArr2 = {string, string2, string3, (tL_theme3 == null || !tL_theme3.creator) ? null : LocaleController.getString("ThemeSetUrl", R.string.ThemeSetUrl), z2 ? LocaleController.getString("Delete", R.string.Delete) : null};
                        z = z2;
                        iArr = new int[]{R.drawable.msg_share, R.drawable.msg_shareout, R.drawable.msg_edit, R.drawable.msg_link, R.drawable.msg_delete};
                        charSequenceArr = charSequenceArr2;
                    }
                    builder.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            ThemeActivity.ListAdapter.this.lambda$showOptionsForTheme$1(themeInfo, dialogInterface, i);
                        }
                    });
                    AlertDialog create = builder.create();
                    ThemeActivity.this.showDialog(create);
                    if (z) {
                        create.setItemColor(create.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ThemeActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ThemeActivity.this.scheduleFromRow || i == ThemeActivity.this.distanceRow || i == ThemeActivity.this.scheduleToRow || i == ThemeActivity.this.scheduleUpdateLocationRow || i == ThemeActivity.this.contactsReimportRow || i == ThemeActivity.this.contactsSortRow || i == ThemeActivity.this.bluetoothScoRow || i == ThemeActivity.this.searchEngineRow) {
                return 1;
            }
            if (i == ThemeActivity.this.automaticBrightnessInfoRow || i == ThemeActivity.this.scheduleLocationInfoRow || i == ThemeActivity.this.swipeGestureInfoRow || i == ThemeActivity.this.stickersInfoRow || i == ThemeActivity.this.liteModeInfoRow) {
                return 2;
            }
            if (i == ThemeActivity.this.themeInfoRow || i == ThemeActivity.this.nightTypeInfoRow || i == ThemeActivity.this.scheduleFromToInfoRow || i == ThemeActivity.this.settings2Row || i == ThemeActivity.this.newThemeInfoRow || i == ThemeActivity.this.chatListInfoRow || i == ThemeActivity.this.bubbleRadiusInfoRow || i == ThemeActivity.this.saveToGallerySectionRow || i == ThemeActivity.this.appIconShadowRow || i == ThemeActivity.this.lastShadowRow || i == ThemeActivity.this.stickersSectionRow || i == ThemeActivity.this.mediaSoundSectionRow || i == ThemeActivity.this.otherSectionRow) {
                return 3;
            }
            if (i == ThemeActivity.this.nightDisabledRow || i == ThemeActivity.this.nightScheduledRow || i == ThemeActivity.this.nightAutomaticRow || i == ThemeActivity.this.nightSystemDefaultRow) {
                return 4;
            }
            if (i == ThemeActivity.this.scheduleHeaderRow || i == ThemeActivity.this.automaticHeaderRow || i == ThemeActivity.this.preferedHeaderRow || i == ThemeActivity.this.settingsRow || i == ThemeActivity.this.themeHeaderRow || i == ThemeActivity.this.textSizeHeaderRow || i == ThemeActivity.this.chatListHeaderRow || i == ThemeActivity.this.bubbleRadiusHeaderRow || i == ThemeActivity.this.swipeGestureHeaderRow || i == ThemeActivity.this.selectThemeHeaderRow || i == ThemeActivity.this.appIconHeaderRow || i == ThemeActivity.this.mediaSoundHeaderRow || i == ThemeActivity.this.otherHeaderRow) {
                return 5;
            }
            if (i == ThemeActivity.this.automaticBrightnessRow) {
                return 6;
            }
            if (i == ThemeActivity.this.scheduleLocationRow || i == ThemeActivity.this.sendByEnterRow || i == ThemeActivity.this.raiseToSpeakRow || i == ThemeActivity.this.raiseToListenRow || i == ThemeActivity.this.pauseOnRecordRow || i == ThemeActivity.this.directShareRow || i == ThemeActivity.this.chatBlurRow || i == ThemeActivity.this.pauseOnMediaRow || i == ThemeActivity.this.nextMediaTapRow || i == ThemeActivity.this.sensitiveContentRow) {
                return 7;
            }
            if (i == ThemeActivity.this.textSizeRow) {
                return 8;
            }
            if (i == ThemeActivity.this.chatListRow) {
                return 9;
            }
            if (i == ThemeActivity.this.nightThemeRow || i == ThemeActivity.this.browserRow) {
                return 10;
            }
            if (i == ThemeActivity.this.themeListRow) {
                return 11;
            }
            if (i == ThemeActivity.this.themeAccentListRow) {
                return 12;
            }
            if (i == ThemeActivity.this.bubbleRadiusRow) {
                return 13;
            }
            if (i == ThemeActivity.this.backgroundRow || i == ThemeActivity.this.editThemeRow || i == ThemeActivity.this.createNewThemeRow || i == ThemeActivity.this.liteModeRow || i == ThemeActivity.this.stickersRow) {
                return 14;
            }
            if (i == ThemeActivity.this.swipeGestureRow) {
                return 15;
            }
            if (i == ThemeActivity.this.themePreviewRow) {
                return 16;
            }
            if (i == ThemeActivity.this.themeListRow2) {
                return 17;
            }
            if (i == ThemeActivity.this.saveToGalleryOption1Row || i == ThemeActivity.this.saveToGalleryOption2Row) {
                return 19;
            }
            if (i == ThemeActivity.this.appIconSelectorRow) {
                return 20;
            }
            return i == ThemeActivity.this.changeUserColor ? 21 : 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 1 || itemViewType == 4 || itemViewType == 7 || itemViewType == 10 || itemViewType == 11 || itemViewType == 12 || itemViewType == 14 || itemViewType == 18 || itemViewType == 20 || itemViewType == 21;
        }

        /* JADX WARN: Code restructure failed: missing block: B:165:0x0404, code lost:
        
            if (org.telegram.ui.ActionBar.Theme.selectedAutoNightType == 0) goto L152;
         */
        /* JADX WARN: Code restructure failed: missing block: B:168:0x0419, code lost:
        
            r13 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:172:0x0417, code lost:
        
            if (org.telegram.ui.ActionBar.Theme.selectedAutoNightType == 1) goto L152;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String str;
            int i3;
            String str2;
            String string;
            String string2;
            String string3;
            String str3;
            String string4;
            View view;
            Context context;
            int i4;
            String string5;
            int i5;
            String str4;
            String string6;
            String string7;
            boolean chatBlurEnabled;
            String string8;
            String string9;
            boolean showSensitiveContent;
            boolean z;
            String string10;
            boolean z2;
            String string11;
            String string12;
            int i6;
            boolean z3;
            String string13;
            int i7;
            switch (viewHolder.getItemViewType()) {
                case 1:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    if (i != ThemeActivity.this.nightThemeRow) {
                        if (i != ThemeActivity.this.scheduleFromRow) {
                            if (i != ThemeActivity.this.scheduleToRow) {
                                if (i != ThemeActivity.this.scheduleUpdateLocationRow) {
                                    if (i != ThemeActivity.this.contactsSortRow) {
                                        if (i != ThemeActivity.this.contactsReimportRow) {
                                            if (i != ThemeActivity.this.distanceRow) {
                                                if (i != ThemeActivity.this.searchEngineRow) {
                                                    if (i == ThemeActivity.this.bluetoothScoRow) {
                                                        textSettingsCell.setTextAndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessages), LocaleController.getString(SharedConfig.recordViaSco ? R.string.MicrophoneForVoiceMessagesSco : R.string.MicrophoneForVoiceMessagesBuiltIn), ThemeActivity.this.updateRecordViaSco, false);
                                                        ThemeActivity.this.updateRecordViaSco = false;
                                                        break;
                                                    }
                                                } else {
                                                    textSettingsCell.setTextAndValue(LocaleController.getString(R.string.SearchEngine), SearchEngine.getCurrent().name, ThemeActivity.this.updateSearchEngine, false);
                                                    break;
                                                }
                                            } else {
                                                int i8 = SharedConfig.distanceSystemType;
                                                if (i8 == 0) {
                                                    i2 = R.string.DistanceUnitsAutomatic;
                                                    str = "DistanceUnitsAutomatic";
                                                } else if (i8 == 1) {
                                                    i2 = R.string.DistanceUnitsKilometers;
                                                    str = "DistanceUnitsKilometers";
                                                } else {
                                                    i2 = R.string.DistanceUnitsMiles;
                                                    str = "DistanceUnitsMiles";
                                                }
                                                textSettingsCell.setTextAndValue(LocaleController.getString("DistanceUnits", R.string.DistanceUnits), LocaleController.getString(str, i2), ThemeActivity.this.updateDistance, true);
                                                ThemeActivity.this.updateDistance = false;
                                                break;
                                            }
                                        } else {
                                            textSettingsCell.setText(LocaleController.getString("ImportContacts", R.string.ImportContacts), true);
                                            break;
                                        }
                                    } else {
                                        int i9 = MessagesController.getGlobalMainSettings().getInt("sortContactsBy", 0);
                                        if (i9 == 0) {
                                            i3 = R.string.Default;
                                            str2 = "Default";
                                        } else if (i9 == 1) {
                                            i3 = R.string.SortFirstName;
                                            str2 = "FirstName";
                                        } else {
                                            i3 = R.string.SortLastName;
                                            str2 = "LastName";
                                        }
                                        string = LocaleController.getString(str2, i3);
                                        string2 = LocaleController.getString("SortBy", R.string.SortBy);
                                    }
                                } else {
                                    string3 = LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation);
                                    str3 = Theme.autoNightCityName;
                                }
                            } else {
                                int i10 = Theme.autoNightDayEndTime;
                                int i11 = i10 / 60;
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTo", R.string.AutoNightTo), String.format("%02d:%02d", Integer.valueOf(i11), Integer.valueOf(i10 - (i11 * 60))), false);
                                break;
                            }
                        } else {
                            int i12 = Theme.autoNightDayStartTime;
                            int i13 = i12 / 60;
                            string2 = LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom);
                            string = String.format("%02d:%02d", Integer.valueOf(i13), Integer.valueOf(i12 - (i13 * 60)));
                        }
                        textSettingsCell.setTextAndValue(string2, string, true);
                        break;
                    } else if (Theme.selectedAutoNightType == 0 || Theme.getCurrentNightTheme() == null) {
                        string3 = LocaleController.getString(R.string.AutoNightTheme);
                        str3 = LocaleController.getString(R.string.AutoNightThemeOff);
                    } else {
                        string3 = LocaleController.getString(R.string.AutoNightTheme);
                        str3 = Theme.getCurrentNightThemeName();
                    }
                    textSettingsCell.setTextAndValue(string3, str3, false);
                    break;
                case 2:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    textInfoPrivacyCell.setFixedSize(0);
                    if (i != ThemeActivity.this.automaticBrightnessInfoRow) {
                        if (i != ThemeActivity.this.scheduleLocationInfoRow) {
                            if (i != ThemeActivity.this.swipeGestureInfoRow) {
                                if (i != ThemeActivity.this.liteModeInfoRow) {
                                    textInfoPrivacyCell.setFixedSize(12);
                                    textInfoPrivacyCell.setText("");
                                    break;
                                } else {
                                    string4 = LocaleController.getString("LiteModeInfo", R.string.LiteModeInfo);
                                }
                            } else {
                                string4 = LocaleController.getString("ChatListSwipeGestureInfo", R.string.ChatListSwipeGestureInfo);
                            }
                        } else {
                            string4 = ThemeActivity.this.getLocationSunString();
                        }
                    } else {
                        string4 = LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f)));
                    }
                    textInfoPrivacyCell.setText(string4);
                    break;
                case 3:
                    if ((i == ThemeActivity.this.nightTypeInfoRow && ThemeActivity.this.themeInfoRow == -1) || i == ThemeActivity.this.lastShadowRow || ((i == ThemeActivity.this.themeInfoRow && ThemeActivity.this.nightTypeInfoRow != -1) || i == ThemeActivity.this.saveToGallerySectionRow || i == ThemeActivity.this.settings2Row)) {
                        view = viewHolder.itemView;
                        context = this.mContext;
                        i4 = R.drawable.greydivider_bottom;
                    } else {
                        view = viewHolder.itemView;
                        context = this.mContext;
                        i4 = R.drawable.greydivider;
                    }
                    view.setBackground(Theme.getThemedDrawableByKey(context, i4, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 4:
                    ThemeTypeCell themeTypeCell = (ThemeTypeCell) viewHolder.itemView;
                    if (i != ThemeActivity.this.nightDisabledRow) {
                        if (i != ThemeActivity.this.nightScheduledRow) {
                            if (i != ThemeActivity.this.nightAutomaticRow) {
                                if (i == ThemeActivity.this.nightSystemDefaultRow) {
                                    themeTypeCell.setValue(LocaleController.getString("AutoNightSystemDefault", R.string.AutoNightSystemDefault), Theme.selectedAutoNightType == 3, false);
                                    break;
                                }
                            } else {
                                themeTypeCell.setValue(LocaleController.getString("AutoNightAdaptive", R.string.AutoNightAdaptive), Theme.selectedAutoNightType == 2, ThemeActivity.this.nightSystemDefaultRow != -1);
                                break;
                            }
                        } else {
                            string5 = LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled);
                            break;
                        }
                    } else {
                        string5 = LocaleController.getString("AutoNightDisabled", R.string.AutoNightDisabled);
                        break;
                    }
                    themeTypeCell.setValue(string5, r13, true);
                    break;
                case 5:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == ThemeActivity.this.scheduleHeaderRow) {
                        i5 = R.string.AutoNightSchedule;
                        str4 = "AutoNightSchedule";
                    } else if (i == ThemeActivity.this.automaticHeaderRow) {
                        i5 = R.string.AutoNightBrightness;
                        str4 = "AutoNightBrightness";
                    } else if (i == ThemeActivity.this.preferedHeaderRow) {
                        i5 = R.string.AutoNightPreferred;
                        str4 = "AutoNightPreferred";
                    } else if (i == ThemeActivity.this.settingsRow) {
                        i5 = R.string.SETTINGS;
                        str4 = "SETTINGS";
                    } else if (i == ThemeActivity.this.themeHeaderRow) {
                        if (ThemeActivity.this.currentType == 3) {
                            i5 = R.string.BuildMyOwnTheme;
                            str4 = "BuildMyOwnTheme";
                        } else {
                            i5 = R.string.ColorTheme;
                            str4 = "ColorTheme";
                        }
                    } else if (i == ThemeActivity.this.textSizeHeaderRow) {
                        i5 = R.string.TextSizeHeader;
                        str4 = "TextSizeHeader";
                    } else if (i == ThemeActivity.this.chatListHeaderRow) {
                        i5 = R.string.ChatList;
                        str4 = "ChatList";
                    } else if (i == ThemeActivity.this.bubbleRadiusHeaderRow) {
                        i5 = R.string.BubbleRadius;
                        str4 = "BubbleRadius";
                    } else if (i == ThemeActivity.this.swipeGestureHeaderRow) {
                        i5 = R.string.ChatListSwipeGesture;
                        str4 = "ChatListSwipeGesture";
                    } else if (i == ThemeActivity.this.selectThemeHeaderRow) {
                        i5 = R.string.SelectTheme;
                        str4 = "SelectTheme";
                    } else if (i == ThemeActivity.this.appIconHeaderRow) {
                        string6 = LocaleController.getString(R.string.AppIcon);
                        headerCell.setText(string6);
                        break;
                    } else if (i == ThemeActivity.this.otherHeaderRow) {
                        i5 = R.string.OtherSettings;
                        str4 = "OtherSettings";
                    } else if (i == ThemeActivity.this.mediaSoundHeaderRow) {
                        i5 = R.string.MediaAndSoundSettings;
                        str4 = "MediaAndSoundSettings";
                    }
                    string6 = LocaleController.getString(str4, i5);
                    headerCell.setText(string6);
                    break;
                case 6:
                    ((BrightnessControlCell) viewHolder.itemView).setProgress(Theme.autoNightBrighnessThreshold);
                    break;
                case 7:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i != ThemeActivity.this.scheduleLocationRow) {
                        if (i == ThemeActivity.this.enableAnimationsRow) {
                            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                            string10 = LocaleController.getString("EnableAnimations", R.string.EnableAnimations);
                            z2 = globalMainSettings.getBoolean("view_animations", true);
                        } else if (i == ThemeActivity.this.sendByEnterRow) {
                            SharedPreferences globalMainSettings2 = MessagesController.getGlobalMainSettings();
                            string10 = LocaleController.getString("SendByEnter", R.string.SendByEnter);
                            z2 = globalMainSettings2.getBoolean("send_by_enter", false);
                        } else {
                            if (i == ThemeActivity.this.raiseToSpeakRow) {
                                string8 = LocaleController.getString("RaiseToSpeak", R.string.RaiseToSpeak);
                                string9 = LocaleController.getString("RaiseToSpeakInfo", R.string.RaiseToSpeakInfo);
                                showSensitiveContent = SharedConfig.raiseToSpeak;
                            } else if (i == ThemeActivity.this.raiseToListenRow) {
                                string8 = LocaleController.getString("RaiseToListen", R.string.RaiseToListen);
                                string9 = LocaleController.getString("RaiseToListenInfo", R.string.RaiseToListenInfo);
                                showSensitiveContent = SharedConfig.raiseToListen;
                            } else if (i == ThemeActivity.this.nextMediaTapRow) {
                                string8 = LocaleController.getString("NextMediaTap", R.string.NextMediaTap);
                                string9 = LocaleController.getString("NextMediaTapInfo", R.string.NextMediaTapInfo);
                                showSensitiveContent = SharedConfig.nextMediaTap;
                            } else if (i == ThemeActivity.this.pauseOnRecordRow) {
                                string8 = LocaleController.getString(R.string.PauseMusicOnRecord);
                                string9 = LocaleController.getString("PauseMusicOnRecordInfo", R.string.PauseMusicOnRecordInfo);
                                showSensitiveContent = SharedConfig.pauseMusicOnRecord;
                            } else if (i == ThemeActivity.this.pauseOnMediaRow) {
                                string7 = LocaleController.getString(R.string.PauseMusicOnMedia);
                                chatBlurEnabled = SharedConfig.pauseMusicOnMedia;
                            } else if (i == ThemeActivity.this.directShareRow) {
                                string8 = LocaleController.getString("DirectShare", R.string.DirectShare);
                                string9 = LocaleController.getString("DirectShareInfo", R.string.DirectShareInfo);
                                showSensitiveContent = SharedConfig.directShare;
                                z = false;
                                textCheckCell.setTextAndValueAndCheck(string8, string9, showSensitiveContent, z, true);
                                break;
                            } else if (i == ThemeActivity.this.sensitiveContentRow) {
                                string8 = LocaleController.getString(R.string.ShowSensitiveContent);
                                string9 = LocaleController.getString(R.string.ShowSensitiveContentInfo);
                                showSensitiveContent = ThemeActivity.this.getMessagesController().showSensitiveContent();
                            } else if (i == ThemeActivity.this.chatBlurRow) {
                                string7 = LocaleController.getString("BlurInChat", R.string.BlurInChat);
                                chatBlurEnabled = SharedConfig.chatBlurEnabled();
                            }
                            z = true;
                            textCheckCell.setTextAndValueAndCheck(string8, string9, showSensitiveContent, z, true);
                        }
                        textCheckCell.setTextAndCheck(string10, z2, true);
                        break;
                    } else {
                        string7 = LocaleController.getString("AutoNightLocation", R.string.AutoNightLocation);
                        chatBlurEnabled = Theme.autoNightScheduleByLocation;
                    }
                    textCheckCell.setTextAndCheck(string7, chatBlurEnabled, true);
                    break;
                case 10:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    if (i == ThemeActivity.this.nightThemeRow) {
                        z3 = Theme.selectedAutoNightType != 0;
                        String currentNightThemeName = z3 ? Theme.getCurrentNightThemeName() : LocaleController.getString("AutoNightThemeOff", R.string.AutoNightThemeOff);
                        if (z3) {
                            int i14 = Theme.selectedAutoNightType;
                            currentNightThemeName = (i14 == 1 ? LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled) : i14 == 3 ? LocaleController.getString("AutoNightSystemDefault", R.string.AutoNightSystemDefault) : LocaleController.getString("AutoNightAdaptive", R.string.AutoNightAdaptive)) + " " + currentNightThemeName;
                        }
                        string12 = currentNightThemeName;
                        string11 = LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme);
                        i6 = R.drawable.msg2_night_auto;
                    } else if (i == ThemeActivity.this.browserRow) {
                        string11 = LocaleController.getString(R.string.InappBrowser);
                        string12 = LocaleController.getString(R.string.InappBrowserInfo);
                        i6 = R.drawable.msg2_language;
                        z3 = SharedConfig.inappBrowser;
                    }
                    notificationsCheckCell.setTextAndValueAndIconAndCheck(string11, string12, i6, z3, 0, false, true);
                    break;
                case 11:
                    if (this.first) {
                        ThemeActivity.this.themesHorizontalListCell.scrollToCurrentTheme(ThemeActivity.this.listView.getMeasuredWidth(), false);
                        this.first = false;
                        break;
                    }
                    break;
                case 12:
                    RecyclerListView recyclerListView = (RecyclerListView) viewHolder.itemView;
                    ThemeAccentsListAdapter themeAccentsListAdapter = (ThemeAccentsListAdapter) recyclerListView.getAdapter();
                    themeAccentsListAdapter.notifyDataSetChanged();
                    int findCurrentAccent = themeAccentsListAdapter.findCurrentAccent();
                    if (findCurrentAccent == -1) {
                        findCurrentAccent = themeAccentsListAdapter.getItemCount() - 1;
                    }
                    if (findCurrentAccent != -1) {
                        ((LinearLayoutManager) recyclerListView.getLayoutManager()).scrollToPositionWithOffset(findCurrentAccent, (ThemeActivity.this.listView.getMeasuredWidth() / 2) - AndroidUtilities.dp(42.0f));
                        break;
                    }
                    break;
                case 14:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    textCell.heightDp = 48;
                    if (i != ThemeActivity.this.backgroundRow) {
                        if (i != ThemeActivity.this.editThemeRow) {
                            if (i != ThemeActivity.this.createNewThemeRow) {
                                if (i == ThemeActivity.this.liteModeRow) {
                                    textCell.setColors(Theme.key_dialogIcon, Theme.key_windowBackgroundWhiteBlackText);
                                    textCell.setTextAndIcon((CharSequence) LocaleController.getString("LiteMode", R.string.LiteMode), R.drawable.msg2_animations, true);
                                    textCell.setSubtitle(LocaleController.getString("LiteModeInfo", R.string.LiteModeInfo));
                                    textCell.heightDp = 60;
                                    textCell.offsetFromImage = 64;
                                } else if (i == ThemeActivity.this.stickersRow) {
                                    textCell.setColors(Theme.key_dialogIcon, Theme.key_windowBackgroundWhiteBlackText);
                                    textCell.setTextAndIcon((CharSequence) LocaleController.getString("StickersName", R.string.StickersName), R.drawable.msg2_sticker, false);
                                    textCell.setSubtitle(LocaleController.getString("StickersNameInfo2", R.string.StickersNameInfo2));
                                    textCell.offsetFromImage = 64;
                                    textCell.heightDp = 60;
                                }
                                textCell.imageLeft = 20;
                                break;
                            } else {
                                textCell.setSubtitle(null);
                                int i15 = Theme.key_windowBackgroundWhiteBlueText4;
                                textCell.setColors(i15, i15);
                                string13 = LocaleController.getString("CreateNewTheme", R.string.CreateNewTheme);
                                i7 = R.drawable.msg_colors;
                            }
                        } else {
                            textCell.setSubtitle(null);
                            int i16 = Theme.key_windowBackgroundWhiteBlueText4;
                            textCell.setColors(i16, i16);
                            textCell.setTextAndIcon((CharSequence) LocaleController.getString("EditCurrentTheme", R.string.EditCurrentTheme), R.drawable.msg_theme, true);
                            break;
                        }
                    } else {
                        textCell.setSubtitle(null);
                        int i17 = Theme.key_windowBackgroundWhiteBlueText4;
                        textCell.setColors(i17, i17);
                        string13 = LocaleController.getString(R.string.ChangeChatBackground);
                        i7 = R.drawable.msg_background;
                        if (ThemeActivity.this.changeUserColor >= 0) {
                            r13 = true;
                        }
                    }
                    textCell.setTextAndIcon(string13, i7, r13);
                    break;
                case 17:
                    ((DefaultThemesPreviewCell) viewHolder.itemView).updateDayNightMode();
                    break;
                case 19:
                    ((RadioButtonCell) viewHolder.itemView).setTextAndValue(i == ThemeActivity.this.saveToGalleryOption1Row ? "save media only from peer chats" : "save media from all chats", "", true, false);
                    break;
                case 21:
                    ((PeerColorActivity.ChangeNameColorCell) viewHolder.itemView).set(ThemeActivity.this.getUserConfig().getCurrentUser());
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View notificationsCheckCell;
            ViewGroup.LayoutParams layoutParams;
            View view;
            View view2;
            int i2 = 0;
            switch (i) {
                case 1:
                    notificationsCheckCell = new TextSettingsCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 2:
                    View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    view2 = textInfoPrivacyCell;
                    break;
                case 3:
                    view2 = new ShadowSectionCell(this.mContext);
                    break;
                case 4:
                    notificationsCheckCell = new ThemeTypeCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 5:
                    notificationsCheckCell = new HeaderCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 6:
                    notificationsCheckCell = new BrightnessControlCell(this.mContext, i2) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.1
                        @Override // org.telegram.ui.Cells.BrightnessControlCell
                        protected void didChangedValue(float f) {
                            int i3 = (int) (Theme.autoNightBrighnessThreshold * 100.0f);
                            int i4 = (int) (f * 100.0f);
                            Theme.autoNightBrighnessThreshold = f;
                            if (i3 != i4) {
                                RecyclerListView.Holder holder = (RecyclerListView.Holder) ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                                if (holder != null) {
                                    ((TextInfoPrivacyCell) holder.itemView).setText(LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f))));
                                }
                                Theme.checkAutoNightThemeConditions(true);
                            }
                        }
                    };
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 7:
                    notificationsCheckCell = new TextCheckCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 8:
                    notificationsCheckCell = ThemeActivity.this.new TextSizeCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 9:
                    notificationsCheckCell = new ChatListCell(this.mContext) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.2
                        @Override // org.telegram.ui.Cells.ChatListCell
                        protected void didSelectChatType(boolean z) {
                            SharedConfig.setUseThreeLinesLayout(z);
                        }
                    };
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 10:
                    notificationsCheckCell = new NotificationsCheckCell(this.mContext, 21, 60, true);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 11:
                    this.first = true;
                    ThemeActivity themeActivity = ThemeActivity.this;
                    Context context = this.mContext;
                    ThemeActivity themeActivity2 = ThemeActivity.this;
                    themeActivity.themesHorizontalListCell = new ThemesHorizontalListCell(context, themeActivity2, themeActivity2.currentType, ThemeActivity.this.defaultThemes, ThemeActivity.this.darkThemes) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.3
                        @Override // org.telegram.ui.Cells.ThemesHorizontalListCell
                        protected void showOptionsForTheme(Theme.ThemeInfo themeInfo) {
                            ThemeActivity.this.listAdapter.showOptionsForTheme(themeInfo);
                        }

                        @Override // org.telegram.ui.Cells.ThemesHorizontalListCell
                        protected void updateRows() {
                            ThemeActivity.this.updateRows(false);
                        }
                    };
                    ThemeActivity.this.themesHorizontalListCell.setDrawDivider(ThemeActivity.this.hasThemeAccents);
                    ThemeActivity.this.themesHorizontalListCell.setFocusable(false);
                    View view3 = ThemeActivity.this.themesHorizontalListCell;
                    layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(148.0f));
                    view = view3;
                    view.setLayoutParams(layoutParams);
                    view2 = view;
                    break;
                case 12:
                    final TintRecyclerListView tintRecyclerListView = new TintRecyclerListView(this.mContext) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.4
                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            if (getParent() != null && getParent().getParent() != null) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                    };
                    tintRecyclerListView.setFocusable(false);
                    tintRecyclerListView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    tintRecyclerListView.setItemAnimator(null);
                    tintRecyclerListView.setLayoutAnimation(null);
                    tintRecyclerListView.setPadding(AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f), 0);
                    tintRecyclerListView.setClipToPadding(false);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext);
                    linearLayoutManager.setOrientation(0);
                    tintRecyclerListView.setLayoutManager(linearLayoutManager);
                    final ThemeAccentsListAdapter themeAccentsListAdapter = ThemeActivity.this.new ThemeAccentsListAdapter(this.mContext);
                    tintRecyclerListView.setAdapter(themeAccentsListAdapter);
                    tintRecyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view4, int i3) {
                            ThemeActivity.ListAdapter.this.lambda$onCreateViewHolder$2(themeAccentsListAdapter, tintRecyclerListView, view4, i3);
                        }
                    });
                    tintRecyclerListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.ThemeActivity$ListAdapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                        public final boolean onItemClick(View view4, int i3) {
                            boolean lambda$onCreateViewHolder$5;
                            lambda$onCreateViewHolder$5 = ThemeActivity.ListAdapter.this.lambda$onCreateViewHolder$5(themeAccentsListAdapter, view4, i3);
                            return lambda$onCreateViewHolder$5;
                        }
                    });
                    layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(62.0f));
                    view = tintRecyclerListView;
                    view.setLayoutParams(layoutParams);
                    view2 = view;
                    break;
                case 13:
                    notificationsCheckCell = ThemeActivity.this.new BubbleRadiusCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 14:
                case 18:
                default:
                    notificationsCheckCell = new TextCell(this.mContext);
                    notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = notificationsCheckCell;
                    break;
                case 15:
                    view2 = new SwipeGestureSettingsView(this.mContext, ((BaseFragment) ThemeActivity.this).currentAccount);
                    break;
                case 16:
                    View view4 = new ThemePreviewMessagesCell(this.mContext, ((BaseFragment) ThemeActivity.this).parentLayout, i2) { // from class: org.telegram.ui.ThemeActivity.ListAdapter.5
                        @Override // org.telegram.ui.Cells.ThemePreviewMessagesCell, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            if (getParent() != null && getParent().getParent() != null) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                    };
                    view4.setImportantForAccessibility(4);
                    view2 = view4;
                    break;
                case 17:
                    Context context2 = this.mContext;
                    ThemeActivity themeActivity3 = ThemeActivity.this;
                    View defaultThemesPreviewCell = new DefaultThemesPreviewCell(context2, themeActivity3, themeActivity3.currentType);
                    defaultThemesPreviewCell.setFocusable(false);
                    layoutParams = new RecyclerView.LayoutParams(-1, -2);
                    view = defaultThemesPreviewCell;
                    view.setLayoutParams(layoutParams);
                    view2 = view;
                    break;
                case 19:
                    view2 = new RadioButtonCell(this.mContext);
                    break;
                case 20:
                    Context context3 = this.mContext;
                    ThemeActivity themeActivity4 = ThemeActivity.this;
                    view2 = new AppIconsSelectorCell(context3, themeActivity4, ((BaseFragment) themeActivity4).currentAccount);
                    break;
                case 21:
                    view2 = new PeerColorActivity.ChangeNameColorCell(((BaseFragment) ThemeActivity.this).currentAccount, 0L, this.mContext, ThemeActivity.this.getResourceProvider());
                    break;
            }
            return new RecyclerListView.Holder(view2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 4) {
                ((ThemeTypeCell) viewHolder.itemView).setTypeChecked(viewHolder.getAdapterPosition() == Theme.selectedAutoNightType);
            }
            if (itemViewType == 2 || itemViewType == 3) {
                return;
            }
            viewHolder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
    }

    private class TextSizeCell extends FrameLayout {
        private int endFontSize;
        private int lastWidth;
        private ThemePreviewMessagesCell messagesCell;
        private SeekBarView sizeBar;
        private int startFontSize;
        private TextPaint textPaint;

        public TextSizeCell(Context context) {
            super(context);
            this.startFontSize = 12;
            this.endFontSize = 30;
            setWillNotDraw(false);
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(16.0f));
            SeekBarView seekBarView = new SeekBarView(context);
            this.sizeBar = seekBarView;
            seekBarView.setReportChanges(true);
            this.sizeBar.setSeparatorsCount((this.endFontSize - this.startFontSize) + 1);
            this.sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.ThemeActivity.TextSizeCell.1
                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public CharSequence getContentDescription() {
                    return String.valueOf(Math.round(TextSizeCell.this.startFontSize + ((TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize) * TextSizeCell.this.sizeBar.getProgress())));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public int getStepsCount() {
                    return TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize;
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarDrag(boolean z, float f) {
                    ThemeActivity.this.setFontSize(Math.round(r4.startFontSize + ((TextSizeCell.this.endFontSize - TextSizeCell.this.startFontSize) * f)));
                }

                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                public void onSeekBarPressed(boolean z) {
                }
            });
            this.sizeBar.setImportantForAccessibility(2);
            addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 5.0f, 39.0f, 0.0f));
            ThemePreviewMessagesCell themePreviewMessagesCell = new ThemePreviewMessagesCell(context, ((BaseFragment) ThemeActivity.this).parentLayout, 0);
            this.messagesCell = themePreviewMessagesCell;
            themePreviewMessagesCell.setImportantForAccessibility(4);
            addView(this.messagesCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 53.0f, 0.0f, 0.0f));
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.messagesCell.invalidate();
            this.sizeBar.invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText("" + SharedConfig.fontSize, getMeasuredWidth() - AndroidUtilities.dp(39.0f), AndroidUtilities.dp(28.0f), this.textPaint);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            this.sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int size = View.MeasureSpec.getSize(i);
            if (this.lastWidth != size) {
                SeekBarView seekBarView = this.sizeBar;
                int i3 = SharedConfig.fontSize;
                int i4 = this.startFontSize;
                seekBarView.setProgress((i3 - i4) / (this.endFontSize - i4));
                this.lastWidth = size;
            }
        }

        @Override // android.view.View
        public boolean performAccessibilityAction(int i, Bundle bundle) {
            return super.performAccessibilityAction(i, bundle) || this.sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, i, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ThemeAccentsListAdapter extends RecyclerListView.SelectionAdapter {
        private Theme.ThemeInfo currentTheme;
        private Context mContext;
        private ArrayList themeAccents;

        ThemeAccentsListAdapter(Context context) {
            this.mContext = context;
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int findCurrentAccent() {
            return this.themeAccents.indexOf(this.currentTheme.getAccent(false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (this.themeAccents.isEmpty()) {
                return 0;
            }
            return this.themeAccents.size() + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == getItemCount() - 1 ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            this.currentTheme = ThemeActivity.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme();
            this.themeAccents = new ArrayList(this.currentTheme.themeAccents);
            super.notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                ((InnerAccentView) viewHolder.itemView).setThemeAndColor(this.currentTheme, (Theme.ThemeAccent) this.themeAccents.get(i));
            } else {
                if (itemViewType != 1) {
                    return;
                }
                ((InnerCustomAccentView) viewHolder.itemView).setTheme(this.currentTheme);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return i != 0 ? new RecyclerListView.Holder(new InnerCustomAccentView(this.mContext)) : new RecyclerListView.Holder(new InnerAccentView(this.mContext));
        }
    }

    private static abstract class TintRecyclerListView extends RecyclerListView {
        TintRecyclerListView(Context context) {
            super(context);
        }
    }

    public ThemeActivity(int i) {
        1 r1 = null;
        this.gpsLocationListener = new GpsLocationListener(this, r1);
        this.networkLocationListener = new GpsLocationListener(this, r1);
        this.currentType = i;
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createNewTheme() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("NewTheme", R.string.NewTheme));
        builder.setMessage(LocaleController.getString("CreateNewThemeAlert", R.string.CreateNewThemeAlert));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("CreateTheme", R.string.CreateTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda18
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ThemeActivity.this.lambda$createNewTheme$14(dialogInterface, i);
            }
        });
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editTheme() {
        Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
        presentFragment(new ThemePreviewActivity(currentTheme, false, 1, currentTheme.getAccent(false).id >= 100, this.currentType == 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLocationSunString() {
        int i = Theme.autoNightSunriseTime;
        int i2 = i / 60;
        String format = String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i - (i2 * 60)));
        int i3 = Theme.autoNightSunsetTime;
        int i4 = i3 / 60;
        return LocaleController.formatString("AutoNightUpdateLocationInfo", R.string.AutoNightUpdateLocationInfo, String.format("%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3 - (i4 * 60))), format);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createNewTheme$14(DialogInterface dialogInterface, int i) {
        AlertsCreator.createThemeCreateDialog(this, 0, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(int i, TextSettingsCell textSettingsCell, TimePicker timePicker, int i2, int i3) {
        String string;
        String format;
        int i4 = (i2 * 60) + i3;
        if (i == this.scheduleFromRow) {
            Theme.autoNightDayStartTime = i4;
            string = LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom);
            format = String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3));
        } else {
            Theme.autoNightDayEndTime = i4;
            string = LocaleController.getString("AutoNightTo", R.string.AutoNightTo);
            format = String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3));
        }
        textSettingsCell.setTextAndValue(string, format, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(Context context, final View view, final int i, float f, float f2) {
        BaseFragment liteModeSettingsActivity;
        int i2;
        String str;
        TextCheckCell textCheckCell;
        boolean chatBlurEnabled;
        AlertDialog.Builder builder;
        AlertDialog create;
        final AtomicReference atomicReference;
        LinearLayout linearLayout;
        AlertDialog.Builder builder2;
        int i3;
        String string;
        boolean z;
        if (i != this.enableAnimationsRow) {
            if (i == this.backgroundRow) {
                liteModeSettingsActivity = new WallpapersListActivity(0);
            } else if (i == this.changeUserColor) {
                liteModeSettingsActivity = new PeerColorActivity(0L).setOnApplied(this);
            } else {
                if (i != this.sendByEnterRow) {
                    if (i == this.raiseToSpeakRow) {
                        SharedConfig.toggleRaiseToSpeak();
                        if (!(view instanceof TextCheckCell)) {
                            return;
                        }
                        textCheckCell = (TextCheckCell) view;
                        chatBlurEnabled = SharedConfig.raiseToSpeak;
                    } else if (i == this.nextMediaTapRow) {
                        SharedConfig.toggleNextMediaTap();
                        if (!(view instanceof TextCheckCell)) {
                            return;
                        }
                        textCheckCell = (TextCheckCell) view;
                        chatBlurEnabled = SharedConfig.nextMediaTap;
                    } else {
                        if (i == this.raiseToListenRow) {
                            SharedConfig.toggleRaiseToListen();
                            if (view instanceof TextCheckCell) {
                                ((TextCheckCell) view).setChecked(SharedConfig.raiseToListen);
                            }
                            if (!SharedConfig.raiseToListen && this.raiseToSpeakRow != -1) {
                                for (int i4 = 0; i4 < this.listView.getChildCount(); i4++) {
                                    View childAt = this.listView.getChildAt(i4);
                                    if ((childAt instanceof TextCheckCell) && this.listView.getChildAdapterPosition(childAt) == this.raiseToSpeakRow) {
                                        ((TextCheckCell) childAt).setChecked(false);
                                    }
                                }
                            }
                            updateRows(false);
                            return;
                        }
                        if (i == this.pauseOnRecordRow) {
                            SharedConfig.togglePauseMusicOnRecord();
                            if (!(view instanceof TextCheckCell)) {
                                return;
                            }
                            textCheckCell = (TextCheckCell) view;
                            chatBlurEnabled = SharedConfig.pauseMusicOnRecord;
                        } else {
                            if (i != this.pauseOnMediaRow) {
                                if (i != this.distanceRow) {
                                    if (i == this.searchEngineRow) {
                                        if (getParentActivity() == null) {
                                            return;
                                        }
                                        atomicReference = new AtomicReference();
                                        linearLayout = new LinearLayout(context);
                                        linearLayout.setOrientation(1);
                                        ArrayList searchEngines = SearchEngine.getSearchEngines();
                                        int size = searchEngines.size();
                                        CharSequence[] charSequenceArr = new CharSequence[size];
                                        final int i5 = 0;
                                        while (i5 < size) {
                                            charSequenceArr[i5] = ((SearchEngine) searchEngines.get(i5)).name;
                                            RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
                                            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                                            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                                            radioColorCell.setTextAndValue(charSequenceArr[i5], i5 == SharedConfig.searchEngineType);
                                            radioColorCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                                            linearLayout.addView(radioColorCell);
                                            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda11
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view2) {
                                                    ThemeActivity.this.lambda$createView$4(i5, atomicReference, view2);
                                                }
                                            });
                                            i5++;
                                        }
                                        builder2 = new AlertDialog.Builder(getParentActivity());
                                        i3 = R.string.SearchEngine;
                                    } else if (i == this.bluetoothScoRow) {
                                        if (getParentActivity() == null) {
                                            return;
                                        }
                                        atomicReference = new AtomicReference();
                                        linearLayout = new LinearLayout(context);
                                        linearLayout.setOrientation(1);
                                        RadioColorCell radioColorCell2 = new RadioColorCell(getParentActivity());
                                        radioColorCell2.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                                        int i6 = Theme.key_radioBackground;
                                        int color = Theme.getColor(i6);
                                        int i7 = Theme.key_dialogRadioBackgroundChecked;
                                        radioColorCell2.setCheckColor(color, Theme.getColor(i7));
                                        radioColorCell2.setTextAndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessagesBuiltIn), true ^ SharedConfig.recordViaSco);
                                        int i8 = Theme.key_listSelector;
                                        radioColorCell2.setBackground(Theme.createSelectorDrawable(Theme.getColor(i8), 2));
                                        linearLayout.addView(radioColorCell2);
                                        radioColorCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda12
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view2) {
                                                ThemeActivity.this.lambda$createView$5(atomicReference, view2);
                                            }
                                        });
                                        RadioColorCell radioColorCell3 = new RadioColorCell(getParentActivity());
                                        radioColorCell3.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                                        radioColorCell3.setCheckColor(Theme.getColor(i6), Theme.getColor(i7));
                                        radioColorCell3.setTextAndText2AndValue(LocaleController.getString(R.string.MicrophoneForVoiceMessagesScoIfConnected), LocaleController.getString(R.string.MicrophoneForVoiceMessagesScoHint), SharedConfig.recordViaSco);
                                        radioColorCell3.setBackground(Theme.createSelectorDrawable(Theme.getColor(i8), 2));
                                        linearLayout.addView(radioColorCell3);
                                        radioColorCell3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda13
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view2) {
                                                ThemeActivity.this.lambda$createView$7(atomicReference, view2);
                                            }
                                        });
                                        builder2 = new AlertDialog.Builder(getParentActivity());
                                        i3 = R.string.MicrophoneForVoiceMessages;
                                    } else {
                                        if (i != this.directShareRow) {
                                            if (i != this.sensitiveContentRow) {
                                                if (i == this.contactsReimportRow) {
                                                    return;
                                                }
                                                if (i == this.contactsSortRow) {
                                                    if (getParentActivity() == null) {
                                                        return;
                                                    }
                                                    builder = new AlertDialog.Builder(getParentActivity());
                                                    builder.setTitle(LocaleController.getString("SortBy", R.string.SortBy));
                                                    builder.setItems(new CharSequence[]{LocaleController.getString("Default", R.string.Default), LocaleController.getString("SortFirstName", R.string.SortFirstName), LocaleController.getString("SortLastName", R.string.SortLastName)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda15
                                                        @Override // android.content.DialogInterface.OnClickListener
                                                        public final void onClick(DialogInterface dialogInterface, int i9) {
                                                            ThemeActivity.this.lambda$createView$9(i, dialogInterface, i9);
                                                        }
                                                    });
                                                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                                    create = builder.create();
                                                } else if (i == this.chatBlurRow) {
                                                    SharedConfig.toggleChatBlur();
                                                    if (!(view instanceof TextCheckCell)) {
                                                        return;
                                                    }
                                                    textCheckCell = (TextCheckCell) view;
                                                    chatBlurEnabled = SharedConfig.chatBlurEnabled();
                                                } else if (i == this.nightThemeRow) {
                                                    if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                                                        NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                                                        if (Theme.selectedAutoNightType == 0) {
                                                            Theme.selectedAutoNightType = 2;
                                                            notificationsCheckCell.setChecked(true);
                                                        } else {
                                                            Theme.selectedAutoNightType = 0;
                                                            notificationsCheckCell.setChecked(false);
                                                        }
                                                        Theme.saveAutoNightThemeConfig();
                                                        Theme.checkAutoNightThemeConditions(true);
                                                        boolean z2 = Theme.selectedAutoNightType != 0;
                                                        String currentNightThemeName = z2 ? Theme.getCurrentNightThemeName() : LocaleController.getString("AutoNightThemeOff", R.string.AutoNightThemeOff);
                                                        if (z2) {
                                                            int i9 = Theme.selectedAutoNightType;
                                                            if (i9 == 1) {
                                                                i2 = R.string.AutoNightScheduled;
                                                                str = "AutoNightScheduled";
                                                            } else if (i9 == 3) {
                                                                i2 = R.string.AutoNightSystemDefault;
                                                                str = "AutoNightSystemDefault";
                                                            } else {
                                                                i2 = R.string.AutoNightAdaptive;
                                                                str = "AutoNightAdaptive";
                                                            }
                                                            currentNightThemeName = LocaleController.getString(str, i2) + " " + currentNightThemeName;
                                                        }
                                                        notificationsCheckCell.setTextAndValueAndIconAndCheck(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), currentNightThemeName, R.drawable.msg2_night_auto, z2, 0, false, true);
                                                        return;
                                                    }
                                                    liteModeSettingsActivity = new ThemeActivity(1);
                                                } else {
                                                    if (i != this.browserRow) {
                                                        if (i == this.nightDisabledRow) {
                                                            if (Theme.selectedAutoNightType == 0) {
                                                                return;
                                                            } else {
                                                                Theme.selectedAutoNightType = 0;
                                                            }
                                                        } else if (i == this.nightScheduledRow) {
                                                            if (Theme.selectedAutoNightType == 1) {
                                                                return;
                                                            }
                                                            Theme.selectedAutoNightType = 1;
                                                            if (Theme.autoNightScheduleByLocation) {
                                                                updateSunTime(null, true);
                                                            }
                                                        } else if (i == this.nightAutomaticRow) {
                                                            if (Theme.selectedAutoNightType == 2) {
                                                                return;
                                                            } else {
                                                                Theme.selectedAutoNightType = 2;
                                                            }
                                                        } else if (i != this.nightSystemDefaultRow) {
                                                            if (i == this.scheduleLocationRow) {
                                                                boolean z3 = !Theme.autoNightScheduleByLocation;
                                                                Theme.autoNightScheduleByLocation = z3;
                                                                ((TextCheckCell) view).setChecked(z3);
                                                                updateRows(true);
                                                                if (Theme.autoNightScheduleByLocation) {
                                                                    updateSunTime(null, true);
                                                                }
                                                                Theme.checkAutoNightThemeConditions();
                                                                return;
                                                            }
                                                            if (i == this.scheduleFromRow || i == this.scheduleToRow) {
                                                                if (getParentActivity() == null) {
                                                                    return;
                                                                }
                                                                int i10 = i == this.scheduleFromRow ? Theme.autoNightDayStartTime : Theme.autoNightDayEndTime;
                                                                int i11 = i10 / 60;
                                                                int i12 = i10 - (i11 * 60);
                                                                final TextSettingsCell textSettingsCell = (TextSettingsCell) view;
                                                                showDialog(new TimePickerDialog(getParentActivity(), new TimePickerDialog.OnTimeSetListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda16
                                                                    @Override // android.app.TimePickerDialog.OnTimeSetListener
                                                                    public final void onTimeSet(TimePicker timePicker, int i13, int i14) {
                                                                        ThemeActivity.this.lambda$createView$10(i, textSettingsCell, timePicker, i13, i14);
                                                                    }
                                                                }, i11, i12, true));
                                                                return;
                                                            }
                                                            if (i == this.scheduleUpdateLocationRow) {
                                                                updateSunTime(null, true);
                                                                return;
                                                            }
                                                            if (i == this.createNewThemeRow) {
                                                                createNewTheme();
                                                                return;
                                                            }
                                                            if (i == this.editThemeRow) {
                                                                editTheme();
                                                                return;
                                                            } else if (i == this.stickersRow) {
                                                                liteModeSettingsActivity = new StickersActivity(0, null);
                                                            } else if (i != this.liteModeRow) {
                                                                return;
                                                            } else {
                                                                liteModeSettingsActivity = new LiteModeSettingsActivity();
                                                            }
                                                        } else if (Theme.selectedAutoNightType == 3) {
                                                            return;
                                                        } else {
                                                            Theme.selectedAutoNightType = 3;
                                                        }
                                                        updateRows(true);
                                                        Theme.checkAutoNightThemeConditions();
                                                        return;
                                                    }
                                                    if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                                                        SharedConfig.toggleInappBrowser();
                                                        ((NotificationsCheckCell) view).setChecked(SharedConfig.inappBrowser);
                                                        return;
                                                    }
                                                    liteModeSettingsActivity = new WebBrowserSettings(null);
                                                }
                                            } else if (getMessagesController().showSensitiveContent()) {
                                                getMessagesController().setContentSettings(false);
                                                if (!(view instanceof TextCheckCell)) {
                                                    return;
                                                }
                                                textCheckCell = (TextCheckCell) view;
                                                chatBlurEnabled = getMessagesController().showSensitiveContent();
                                            } else {
                                                builder = new AlertDialog.Builder(context, this.resourceProvider).setTitle(LocaleController.getString(R.string.ConfirmSensitiveContentTitle)).setMessage(LocaleController.getString(R.string.ConfirmSensitiveContentText)).setPositiveButton(LocaleController.getString(R.string.Confirm), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda14
                                                    @Override // android.content.DialogInterface.OnClickListener
                                                    public final void onClick(DialogInterface dialogInterface, int i13) {
                                                        ThemeActivity.this.lambda$createView$8(view, dialogInterface, i13);
                                                    }
                                                }).setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                                                create = builder.create();
                                            }
                                            showDialog(create);
                                            return;
                                        }
                                        SharedConfig.toggleDirectShare();
                                        if (!(view instanceof TextCheckCell)) {
                                            return;
                                        }
                                        textCheckCell = (TextCheckCell) view;
                                        chatBlurEnabled = SharedConfig.directShare;
                                    }
                                    string = LocaleController.getString(i3);
                                } else {
                                    if (getParentActivity() == null) {
                                        return;
                                    }
                                    atomicReference = new AtomicReference();
                                    linearLayout = new LinearLayout(context);
                                    linearLayout.setOrientation(1);
                                    CharSequence[] charSequenceArr2 = {LocaleController.getString("DistanceUnitsAutomatic", R.string.DistanceUnitsAutomatic), LocaleController.getString("DistanceUnitsKilometers", R.string.DistanceUnitsKilometers), LocaleController.getString("DistanceUnitsMiles", R.string.DistanceUnitsMiles)};
                                    final int i13 = 0;
                                    while (i13 < 3) {
                                        RadioColorCell radioColorCell4 = new RadioColorCell(getParentActivity());
                                        radioColorCell4.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                                        radioColorCell4.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                                        radioColorCell4.setTextAndValue(charSequenceArr2[i13], i13 == SharedConfig.distanceSystemType);
                                        radioColorCell4.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                                        linearLayout.addView(radioColorCell4);
                                        radioColorCell4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda10
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view2) {
                                                ThemeActivity.this.lambda$createView$3(i13, atomicReference, view2);
                                            }
                                        });
                                        i13++;
                                    }
                                    builder2 = new AlertDialog.Builder(getParentActivity());
                                    string = LocaleController.getString("DistanceUnitsTitle", R.string.DistanceUnitsTitle);
                                }
                                create = builder2.setTitle(string).setView(linearLayout).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                                atomicReference.set(create);
                                showDialog(create);
                                return;
                            }
                            SharedConfig.togglePauseMusicOnMedia();
                            if (!(view instanceof TextCheckCell)) {
                                return;
                            }
                            textCheckCell = (TextCheckCell) view;
                            chatBlurEnabled = SharedConfig.pauseMusicOnMedia;
                        }
                    }
                    textCheckCell.setChecked(chatBlurEnabled);
                }
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                z = globalMainSettings.getBoolean("send_by_enter", false);
                SharedPreferences.Editor edit = globalMainSettings.edit();
                edit.putBoolean("send_by_enter", !z);
                edit.commit();
                if (!(view instanceof TextCheckCell)) {
                    return;
                }
            }
            presentFragment(liteModeSettingsActivity);
            return;
        }
        SharedPreferences globalMainSettings2 = MessagesController.getGlobalMainSettings();
        z = globalMainSettings2.getBoolean("view_animations", true);
        SharedPreferences.Editor edit2 = globalMainSettings2.edit();
        boolean z4 = !z;
        edit2.putBoolean("view_animations", z4);
        SharedConfig.setAnimationsEnabled(z4);
        edit2.commit();
        if (!(view instanceof TextCheckCell)) {
            return;
        }
        textCheckCell = (TextCheckCell) view;
        chatBlurEnabled = !z;
        textCheckCell.setChecked(chatBlurEnabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$createView$12() {
        return this.sensitiveContentRow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13() {
        this.listView.highlightRow(new RecyclerListView.IntReturnCallback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.Components.RecyclerListView.IntReturnCallback
            public final int run() {
                int lambda$createView$12;
                lambda$createView$12 = ThemeActivity.this.lambda$createView$12();
                return lambda$createView$12;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(TL_account.contentSettings contentsettings) {
        ListAdapter listAdapter;
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || !recyclerListView.isAttachedToWindow() || (listAdapter = this.listAdapter) == null) {
            return;
        }
        int i = this.sensitiveContentRow;
        boolean z = false;
        boolean z2 = i >= 0;
        if (contentsettings != null && contentsettings.sensitive_can_change) {
            z = true;
        }
        if (z2 == z) {
            listAdapter.notifyItemChanged(i);
        } else {
            updateRows(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(int i, AtomicReference atomicReference, View view) {
        SharedConfig.setDistanceSystemType(i);
        this.updateDistance = true;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.distanceRow);
        if (findViewHolderForAdapterPosition != null) {
            this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, this.distanceRow);
        }
        ((Dialog) atomicReference.get()).dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(int i, AtomicReference atomicReference, View view) {
        SharedConfig.setSearchEngineType(i);
        this.updateSearchEngine = true;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.searchEngineRow);
        if (findViewHolderForAdapterPosition != null) {
            this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, this.searchEngineRow);
        }
        ((Dialog) atomicReference.get()).dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(AtomicReference atomicReference, View view) {
        SharedConfig.recordViaSco = false;
        SharedConfig.saveConfig();
        this.updateRecordViaSco = true;
        ((Dialog) atomicReference.get()).dismiss();
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow);
        if (findViewHolderForAdapterPosition != null) {
            this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, this.bluetoothScoRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(AtomicReference atomicReference, Boolean bool) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        if (bool.booleanValue()) {
            return;
        }
        SharedConfig.recordViaSco = false;
        SharedConfig.saveConfig();
        this.updateRecordViaSco = true;
        ((Dialog) atomicReference.get()).dismiss();
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || !recyclerListView.isAttachedToWindow() || (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow)) == null) {
            return;
        }
        this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, this.bluetoothScoRow);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(final AtomicReference atomicReference, View view) {
        PermissionRequest.ensurePermission(R.raw.permission_request_microphone, R.string.PermissionNoBluetoothWithHint, "android.permission.BLUETOOTH_CONNECT", new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda17
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ThemeActivity.this.lambda$createView$6(atomicReference, (Boolean) obj);
            }
        });
        SharedConfig.recordViaSco = true;
        SharedConfig.saveConfig();
        this.updateRecordViaSco = true;
        ((Dialog) atomicReference.get()).dismiss();
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.bluetoothScoRow);
        if (findViewHolderForAdapterPosition != null) {
            this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, this.bluetoothScoRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view, DialogInterface dialogInterface, int i) {
        getMessagesController().setContentSettings(true);
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(getMessagesController().showSensitiveContent());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(int i, DialogInterface dialogInterface, int i2) {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("sortContactsBy", i2);
        edit.commit();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$1(DialogInterface dialogInterface) {
        this.sharingProgressDialog = null;
        this.sharingTheme = null;
        this.sharingAccent = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$18() {
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) childAt).getAdapter().notifyDataSetChanged();
            } else if (childAt instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) childAt).updateColors();
            }
        }
        for (int i2 = 0; i2 < this.listView.getCachedChildCount(); i2++) {
            View cachedChildAt = this.listView.getCachedChildAt(i2);
            if (cachedChildAt instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) cachedChildAt).getAdapter().notifyDataSetChanged();
            } else if (cachedChildAt instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) cachedChildAt).updateColors();
            }
        }
        for (int i3 = 0; i3 < this.listView.getHiddenChildCount(); i3++) {
            View hiddenChildAt = this.listView.getHiddenChildAt(i3);
            if (hiddenChildAt instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) hiddenChildAt).getAdapter().notifyDataSetChanged();
            } else if (hiddenChildAt instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) hiddenChildAt).updateColors();
            }
        }
        for (int i4 = 0; i4 < this.listView.getAttachedScrapChildCount(); i4++) {
            View attachedScrapChildAt = this.listView.getAttachedScrapChildAt(i4);
            if (attachedScrapChildAt instanceof AppIconsSelectorCell) {
                ((AppIconsSelectorCell) attachedScrapChildAt).getAdapter().notifyDataSetChanged();
            } else if (attachedScrapChildAt instanceof PeerColorActivity.ChangeNameColorCell) {
                ((PeerColorActivity.ChangeNameColorCell) attachedScrapChildAt).updateColors();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateRows$0(Theme.ThemeInfo themeInfo, Theme.ThemeInfo themeInfo2) {
        return Integer.compare(themeInfo.sortIndex, themeInfo2.sortIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSunTime$15(DialogInterface dialogInterface, int i) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSunTime$16(String str) {
        RecyclerListView.Holder holder;
        Theme.autoNightCityName = str;
        if (str == null) {
            Theme.autoNightCityName = String.format("(%.06f, %.06f)", Double.valueOf(Theme.autoNightLocationLatitude), Double.valueOf(Theme.autoNightLocationLongitude));
        }
        Theme.saveAutoNightThemeConfig();
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || (holder = (RecyclerListView.Holder) recyclerListView.findViewHolderForAdapterPosition(this.scheduleUpdateLocationRow)) == null) {
            return;
        }
        View view = holder.itemView;
        if (view instanceof TextSettingsCell) {
            ((TextSettingsCell) view).setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSunTime$17() {
        final String str;
        List<Address> fromLocation;
        try {
            fromLocation = new Geocoder(ApplicationLoader.applicationContext, Locale.getDefault()).getFromLocation(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude, 1);
        } catch (Exception unused) {
        }
        if (fromLocation.size() > 0) {
            str = fromLocation.get(0).getLocality();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeActivity.this.lambda$updateSunTime$16(str);
                }
            });
        }
        str = null;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ThemeActivity.this.lambda$updateSunTime$16(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setBubbleRadius(int i, boolean z) {
        if (i == SharedConfig.bubbleRadius) {
            return false;
        }
        SharedConfig.bubbleRadius = i;
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("bubbleRadius", SharedConfig.bubbleRadius);
        edit.commit();
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.textSizeRow);
        if (findViewHolderForAdapterPosition != null) {
            View view = findViewHolderForAdapterPosition.itemView;
            if (view instanceof TextSizeCell) {
                TextSizeCell textSizeCell = (TextSizeCell) view;
                ChatMessageCell[] cells = textSizeCell.messagesCell.getCells();
                for (int i2 = 0; i2 < cells.length; i2++) {
                    cells[i2].getMessageObject().resetLayout();
                    cells[i2].requestLayout();
                }
                textSizeCell.invalidate();
            }
        }
        RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.bubbleRadiusRow);
        if (findViewHolderForAdapterPosition2 != null) {
            View view2 = findViewHolderForAdapterPosition2.itemView;
            if (view2 instanceof BubbleRadiusCell) {
                BubbleRadiusCell bubbleRadiusCell = (BubbleRadiusCell) view2;
                if (z) {
                    bubbleRadiusCell.requestLayout();
                } else {
                    bubbleRadiusCell.invalidate();
                }
            }
        }
        updateMenuItem();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setFontSize(int i) {
        if (i == SharedConfig.fontSize) {
            return false;
        }
        SharedConfig.fontSize = i;
        SharedConfig.fontSizeIsDefault = false;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        if (sharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("fons_size", SharedConfig.fontSize);
        edit.commit();
        Theme.createCommonMessageResources();
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.textSizeRow);
        if (findViewHolderForAdapterPosition != null) {
            View view = findViewHolderForAdapterPosition.itemView;
            if (view instanceof TextSizeCell) {
                ChatMessageCell[] cells = ((TextSizeCell) view).messagesCell.getCells();
                for (int i2 = 0; i2 < cells.length; i2++) {
                    cells[i2].getMessageObject().resetLayout();
                    cells[i2].requestLayout();
                }
            }
        }
        updateMenuItem();
        return true;
    }

    private void startLocationUpdate() {
        if (this.updatingLocation) {
            return;
        }
        this.updatingLocation = true;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        try {
            locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLocationUpdate() {
        this.updatingLocation = false;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        locationManager.removeUpdates(this.gpsLocationListener);
        locationManager.removeUpdates(this.networkLocationListener);
    }

    private void updateMenuItem() {
        Theme.OverrideWallpaperInfo overrideWallpaperInfo;
        if (this.menuItem == null) {
            return;
        }
        Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
        Theme.ThemeAccent accent = currentTheme.getAccent(false);
        ArrayList arrayList = currentTheme.themeAccents;
        if (arrayList == null || arrayList.isEmpty() || accent == null || accent.id < 100) {
            this.menuItem.hideSubItem(2);
            this.menuItem.hideSubItem(3);
        } else {
            this.menuItem.showSubItem(2);
            this.menuItem.showSubItem(3);
        }
        int i = AndroidUtilities.isTablet() ? 18 : 16;
        Theme.ThemeInfo currentTheme2 = Theme.getCurrentTheme();
        if (SharedConfig.fontSize == i && SharedConfig.bubbleRadius == 17 && currentTheme2.firstAccentIsDefault && currentTheme2.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID && (accent == null || (overrideWallpaperInfo = accent.overrideWallpaper) == null || "d".equals(overrideWallpaperInfo.slug))) {
            this.menuItem.hideSubItem(4);
        } else {
            this.menuItem.showSubItem(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x031e, code lost:
    
        if (org.telegram.ui.ActionBar.Theme.autoNightScheduleByLocation != false) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x032a, code lost:
    
        r1.notifyItemRangeInserted(r3, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0329, code lost:
    
        r8 = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0327, code lost:
    
        if (org.telegram.ui.ActionBar.Theme.autoNightScheduleByLocation != false) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0349, code lost:
    
        if (r1 == 3) goto L120;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRows(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ListAdapter listAdapter;
        TLRPC.TL_theme tL_theme;
        int i6 = this.rowCount;
        int i7 = this.themeAccentListRow;
        int i8 = this.editThemeRow;
        int i9 = this.raiseToSpeakRow;
        this.rowCount = 0;
        this.contactsReimportRow = -1;
        this.contactsSortRow = -1;
        this.scheduleLocationRow = -1;
        this.scheduleUpdateLocationRow = -1;
        this.scheduleLocationInfoRow = -1;
        this.nightDisabledRow = -1;
        this.nightScheduledRow = -1;
        this.nightAutomaticRow = -1;
        this.nightSystemDefaultRow = -1;
        this.nightTypeInfoRow = -1;
        this.scheduleHeaderRow = -1;
        this.nightThemeRow = -1;
        this.browserRow = -1;
        this.newThemeInfoRow = -1;
        this.scheduleFromRow = -1;
        this.scheduleToRow = -1;
        this.scheduleFromToInfoRow = -1;
        this.themeListRow = -1;
        this.themeListRow2 = -1;
        this.themeAccentListRow = -1;
        this.themeInfoRow = -1;
        this.preferedHeaderRow = -1;
        this.automaticHeaderRow = -1;
        this.automaticBrightnessRow = -1;
        this.automaticBrightnessInfoRow = -1;
        this.textSizeHeaderRow = -1;
        this.themeHeaderRow = -1;
        this.bubbleRadiusHeaderRow = -1;
        this.bubbleRadiusRow = -1;
        this.bubbleRadiusInfoRow = -1;
        this.chatListHeaderRow = -1;
        this.chatListRow = -1;
        this.chatListInfoRow = -1;
        this.chatBlurRow = -1;
        this.pauseOnRecordRow = -1;
        this.pauseOnMediaRow = -1;
        this.stickersRow = -1;
        this.stickersInfoRow = -1;
        this.stickersSectionRow = -1;
        this.mediaSoundHeaderRow = -1;
        this.otherHeaderRow = -1;
        this.mediaSoundSectionRow = -1;
        this.otherSectionRow = -1;
        this.liteModeRow = -1;
        this.liteModeInfoRow = -1;
        this.textSizeRow = -1;
        this.backgroundRow = -1;
        this.changeUserColor = -1;
        this.settingsRow = -1;
        this.directShareRow = -1;
        this.sensitiveContentRow = -1;
        this.enableAnimationsRow = -1;
        this.raiseToSpeakRow = -1;
        this.raiseToListenRow = -1;
        this.nextMediaTapRow = -1;
        this.sendByEnterRow = -1;
        this.saveToGalleryOption1Row = -1;
        this.saveToGalleryOption2Row = -1;
        this.saveToGallerySectionRow = -1;
        this.distanceRow = -1;
        this.searchEngineRow = -1;
        this.bluetoothScoRow = -1;
        this.settings2Row = -1;
        this.swipeGestureHeaderRow = -1;
        this.swipeGestureRow = -1;
        this.swipeGestureInfoRow = -1;
        this.selectThemeHeaderRow = -1;
        this.themePreviewRow = -1;
        this.editThemeRow = -1;
        this.createNewThemeRow = -1;
        this.appIconHeaderRow = -1;
        this.appIconSelectorRow = -1;
        this.appIconShadowRow = -1;
        this.lastShadowRow = -1;
        this.defaultThemes.clear();
        this.darkThemes.clear();
        int size = Theme.themes.size();
        int i10 = 0;
        while (true) {
            if (i10 >= size) {
                break;
            }
            Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) Theme.themes.get(i10);
            int i11 = this.currentType;
            if (i11 == 0 || i11 == 3 || (!themeInfo.isLight() && ((tL_theme = themeInfo.info) == null || tL_theme.document != null))) {
                (themeInfo.pathToFile != null ? this.darkThemes : this.defaultThemes).add(themeInfo);
            }
            i10++;
        }
        Collections.sort(this.defaultThemes, new Comparator() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$updateRows$0;
                lambda$updateRows$0 = ThemeActivity.lambda$updateRows$0((Theme.ThemeInfo) obj, (Theme.ThemeInfo) obj2);
                return lambda$updateRows$0;
            }
        });
        int i12 = this.currentType;
        if (i12 == 3) {
            int i13 = this.rowCount;
            this.selectThemeHeaderRow = i13;
            this.themeListRow2 = i13 + 1;
            this.chatListInfoRow = i13 + 2;
            this.themePreviewRow = i13 + 3;
            this.themeHeaderRow = i13 + 4;
            this.rowCount = i13 + 6;
            this.themeListRow = i13 + 5;
            boolean hasAccentColors = Theme.getCurrentTheme().hasAccentColors();
            this.hasThemeAccents = hasAccentColors;
            ThemesHorizontalListCell themesHorizontalListCell = this.themesHorizontalListCell;
            if (themesHorizontalListCell != null) {
                themesHorizontalListCell.setDrawDivider(hasAccentColors);
            }
            if (this.hasThemeAccents) {
                int i14 = this.rowCount;
                this.rowCount = i14 + 1;
                this.themeAccentListRow = i14;
            }
            int i15 = this.rowCount;
            this.rowCount = i15 + 1;
            this.bubbleRadiusInfoRow = i15;
            Theme.ThemeInfo currentTheme = Theme.getCurrentTheme();
            Theme.ThemeAccent accent = currentTheme.getAccent(false);
            ArrayList arrayList = currentTheme.themeAccents;
            if (arrayList != null && !arrayList.isEmpty() && accent != null && accent.id >= 100) {
                int i16 = this.rowCount;
                this.rowCount = i16 + 1;
                this.editThemeRow = i16;
            }
            int i17 = this.rowCount;
            this.createNewThemeRow = i17;
            this.rowCount = i17 + 2;
            this.lastShadowRow = i17 + 1;
        } else if (i12 == 0) {
            int i18 = this.rowCount;
            this.textSizeHeaderRow = i18;
            this.textSizeRow = i18 + 1;
            this.backgroundRow = i18 + 2;
            this.changeUserColor = i18 + 3;
            this.newThemeInfoRow = i18 + 4;
            this.themeHeaderRow = i18 + 5;
            this.themeListRow2 = i18 + 6;
            this.themeInfoRow = i18 + 7;
            this.bubbleRadiusHeaderRow = i18 + 8;
            this.bubbleRadiusRow = i18 + 9;
            this.bubbleRadiusInfoRow = i18 + 10;
            this.chatListHeaderRow = i18 + 11;
            this.chatListRow = i18 + 12;
            this.chatListInfoRow = i18 + 13;
            this.appIconHeaderRow = i18 + 14;
            this.appIconSelectorRow = i18 + 15;
            this.appIconShadowRow = i18 + 16;
            this.swipeGestureHeaderRow = i18 + 17;
            this.swipeGestureRow = i18 + 18;
            this.swipeGestureInfoRow = i18 + 19;
            this.nightThemeRow = i18 + 20;
            this.browserRow = i18 + 21;
            this.liteModeRow = i18 + 22;
            this.stickersRow = i18 + 23;
            this.stickersSectionRow = i18 + 24;
            this.mediaSoundHeaderRow = i18 + 25;
            this.nextMediaTapRow = i18 + 26;
            int i19 = i18 + 28;
            this.rowCount = i19;
            this.raiseToListenRow = i18 + 27;
            if (SharedConfig.raiseToListen) {
                this.rowCount = i18 + 29;
                this.raiseToSpeakRow = i19;
            }
            int i20 = this.rowCount;
            this.pauseOnRecordRow = i20;
            this.pauseOnMediaRow = i20 + 1;
            this.bluetoothScoRow = i20 + 2;
            this.mediaSoundSectionRow = i20 + 3;
            this.otherHeaderRow = i20 + 4;
            this.rowCount = i20 + 6;
            this.directShareRow = i20 + 5;
            TL_account.contentSettings contentSettings = getMessagesController().getContentSettings();
            if (contentSettings != null && contentSettings.sensitive_can_change) {
                int i21 = this.rowCount;
                this.rowCount = i21 + 1;
                this.sensitiveContentRow = i21;
            }
            int i22 = this.rowCount;
            this.sendByEnterRow = i22;
            this.distanceRow = i22 + 1;
            this.rowCount = i22 + 3;
            this.otherSectionRow = i22 + 2;
        } else {
            int i23 = this.rowCount;
            this.nightDisabledRow = i23;
            this.nightScheduledRow = i23 + 1;
            int i24 = i23 + 3;
            this.rowCount = i24;
            this.nightAutomaticRow = i23 + 2;
            if (Build.VERSION.SDK_INT >= 29) {
                this.rowCount = i23 + 4;
                this.nightSystemDefaultRow = i24;
            }
            int i25 = this.rowCount;
            int i26 = i25 + 1;
            this.rowCount = i26;
            this.nightTypeInfoRow = i25;
            int i27 = Theme.selectedAutoNightType;
            if (i27 == 1) {
                this.scheduleHeaderRow = i26;
                int i28 = i25 + 3;
                this.rowCount = i28;
                this.scheduleLocationRow = i25 + 2;
                if (Theme.autoNightScheduleByLocation) {
                    this.scheduleUpdateLocationRow = i28;
                    this.rowCount = i25 + 5;
                    this.scheduleLocationInfoRow = i25 + 4;
                } else {
                    this.scheduleFromRow = i28;
                    this.scheduleToRow = i25 + 4;
                    this.rowCount = i25 + 6;
                    this.scheduleFromToInfoRow = i25 + 5;
                }
            } else if (i27 == 2) {
                this.automaticHeaderRow = i26;
                this.automaticBrightnessRow = i25 + 2;
                this.rowCount = i25 + 4;
                this.automaticBrightnessInfoRow = i25 + 3;
            }
            if (Theme.selectedAutoNightType != 0) {
                int i29 = this.rowCount;
                this.preferedHeaderRow = i29;
                this.rowCount = i29 + 2;
                this.themeListRow = i29 + 1;
                boolean hasAccentColors2 = Theme.getCurrentNightTheme().hasAccentColors();
                this.hasThemeAccents = hasAccentColors2;
                ThemesHorizontalListCell themesHorizontalListCell2 = this.themesHorizontalListCell;
                if (themesHorizontalListCell2 != null) {
                    themesHorizontalListCell2.setDrawDivider(hasAccentColors2);
                }
                if (this.hasThemeAccents) {
                    int i30 = this.rowCount;
                    this.rowCount = i30 + 1;
                    this.themeAccentListRow = i30;
                }
                int i31 = this.rowCount;
                this.rowCount = i31 + 1;
                this.themeInfoRow = i31;
            }
        }
        ThemesHorizontalListCell themesHorizontalListCell3 = this.themesHorizontalListCell;
        if (themesHorizontalListCell3 != null) {
            themesHorizontalListCell3.notifyDataSetChanged(this.listView.getWidth());
        }
        ListAdapter listAdapter2 = this.listAdapter;
        if (listAdapter2 != null) {
            if (this.currentType == 1 && (i4 = this.previousUpdatedType) != (i5 = Theme.selectedAutoNightType) && i4 != -1) {
                int i32 = this.nightTypeInfoRow;
                int i33 = i32 + 1;
                if (i4 != i5) {
                    int i34 = 0;
                    while (i34 < 4) {
                        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(i34);
                        if (holder != null) {
                            View view = holder.itemView;
                            if (view instanceof ThemeTypeCell) {
                                ((ThemeTypeCell) view).setTypeChecked(i34 == Theme.selectedAutoNightType);
                            }
                        }
                        i34++;
                    }
                    int i35 = Theme.selectedAutoNightType;
                    if (i35 == 0) {
                        this.listAdapter.notifyItemRangeRemoved(i33, i6 - i33);
                    } else if (i35 == 1) {
                        int i36 = this.previousUpdatedType;
                        if (i36 != 0) {
                            if (i36 == 2) {
                                this.listAdapter.notifyItemRangeRemoved(i33, 3);
                                listAdapter = this.listAdapter;
                            } else if (i36 == 3) {
                                listAdapter = this.listAdapter;
                            }
                        }
                        this.listAdapter.notifyItemRangeInserted(i33, this.rowCount - i33);
                    } else if (i35 == 2) {
                        int i37 = this.previousUpdatedType;
                        if (i37 != 0) {
                            if (i37 == 1) {
                                this.listAdapter.notifyItemRangeRemoved(i33, Theme.autoNightScheduleByLocation ? 4 : 5);
                            }
                            this.listAdapter.notifyItemRangeInserted(i33, 3);
                        }
                        this.listAdapter.notifyItemRangeInserted(i33, this.rowCount - i33);
                    } else if (i35 == 3) {
                        int i38 = this.previousUpdatedType;
                        if (i38 != 0) {
                            if (i38 == 2) {
                                this.listAdapter.notifyItemRangeRemoved(i33, 3);
                            } else if (i38 == 1) {
                                this.listAdapter.notifyItemRangeRemoved(i33, Theme.autoNightScheduleByLocation ? 4 : 5);
                            }
                        }
                        this.listAdapter.notifyItemRangeInserted(i33, this.rowCount - i33);
                    }
                } else {
                    boolean z2 = this.previousByLocation;
                    boolean z3 = Theme.autoNightScheduleByLocation;
                    if (z2 != z3) {
                        int i39 = i32 + 3;
                        listAdapter2.notifyItemRangeRemoved(i39, z3 ? 3 : 2);
                        this.listAdapter.notifyItemRangeInserted(i39, Theme.autoNightScheduleByLocation ? 2 : 3);
                    }
                }
            } else if (z || this.previousUpdatedType == -1) {
                listAdapter2.notifyDataSetChanged();
            } else {
                if (i7 == -1 && (i3 = this.themeAccentListRow) != -1) {
                    listAdapter2.notifyItemInserted(i3);
                } else if (i7 == -1 || this.themeAccentListRow != -1) {
                    int i40 = this.themeAccentListRow;
                    if (i40 != -1) {
                        listAdapter2.notifyItemChanged(i40);
                    }
                } else {
                    listAdapter2.notifyItemRemoved(i7);
                    if (i8 != -1) {
                        i8--;
                    }
                }
                if (i8 == -1 && (i2 = this.editThemeRow) != -1) {
                    this.listAdapter.notifyItemInserted(i2);
                } else if (i8 != -1 && this.editThemeRow == -1) {
                    this.listAdapter.notifyItemRemoved(i8);
                }
                if (i9 == -1 && (i = this.raiseToSpeakRow) != -1) {
                    this.listAdapter.notifyItemInserted(i);
                } else if (i9 != -1 && this.raiseToSpeakRow == -1) {
                    this.listAdapter.notifyItemRemoved(i9);
                }
            }
        }
        if (this.currentType == 1) {
            this.previousByLocation = Theme.autoNightScheduleByLocation;
            this.previousUpdatedType = Theme.selectedAutoNightType;
        }
        updateMenuItem();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSunTime(Location location, boolean z) {
        Activity parentActivity;
        int checkSelfPermission;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        if (Build.VERSION.SDK_INT >= 23 && (parentActivity = getParentActivity()) != null) {
            checkSelfPermission = parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
                parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                return;
            }
        }
        if (getParentActivity() != null) {
            if (!getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                return;
            }
            try {
                if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTopAnimation(R.raw.permission_request_location, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
                    builder.setMessage(LocaleController.getString("GpsDisabledAlertText", R.string.GpsDisabledAlertText));
                    builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", R.string.ConnectingToProxyEnable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            ThemeActivity.this.lambda$updateSunTime$15(dialogInterface, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder.create());
                    return;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        try {
            location = locationManager.getLastKnownLocation("gps");
            if (location == null) {
                location = locationManager.getLastKnownLocation("network");
            }
            if (location == null) {
                location = locationManager.getLastKnownLocation("passive");
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        if (location == null || z) {
            startLocationUpdate();
            if (location == null) {
                return;
            }
        }
        Theme.autoNightLocationLatitude = location.getLatitude();
        Theme.autoNightLocationLongitude = location.getLongitude();
        int[] calculateSunriseSunset = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
        Theme.autoNightSunriseTime = calculateSunriseSunset[0];
        Theme.autoNightSunsetTime = calculateSunriseSunset[1];
        Theme.autoNightCityName = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Theme.autoNightLastSunCheckDay = calendar.get(5);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ThemeActivity.this.lambda$updateSunTime$17();
            }
        });
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
        if (holder != null) {
            View view = holder.itemView;
            if (view instanceof TextInfoPrivacyCell) {
                ((TextInfoPrivacyCell) view).setText(getLocationSunString());
            }
        }
        if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    public void checkCurrentDayNight() {
        if (this.currentType != 3) {
            return;
        }
        boolean z = !Theme.isCurrentThemeDay();
        if (this.lastIsDarkTheme != z) {
            this.lastIsDarkTheme = z;
            this.sunDrawable.setCustomEndFrame(z ? r1.getFramesCount() - 1 : 0);
            this.menuItem.getIconView().playAnimation();
        }
        if (this.themeListRow2 >= 0) {
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                if (this.listView.getChildAt(i) instanceof DefaultThemesPreviewCell) {
                    ((DefaultThemesPreviewCell) this.listView.getChildAt(i)).updateDayNightMode();
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.lastIsDarkTheme = !Theme.isCurrentThemeDay();
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        int i = this.currentType;
        if (i == 3) {
            this.actionBar.setTitle(LocaleController.getString("BrowseThemes", R.string.BrowseThemes));
            ActionBarMenu createMenu = this.actionBar.createMenu();
            int i2 = R.raw.sun;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.sunDrawable = rLottieDrawable;
            if (this.lastIsDarkTheme) {
                rLottieDrawable.setCurrentFrame(rLottieDrawable.getFramesCount() - 1);
            } else {
                rLottieDrawable.setCurrentFrame(0);
            }
            this.sunDrawable.setPlayInDirectionOfCustomEndFrame(true);
            this.menuItem = createMenu.addItem(5, this.sunDrawable);
        } else if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("ChatSettings", R.string.ChatSettings));
            ActionBarMenuItem addItem = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_other);
            this.menuItem = addItem;
            addItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
            this.menuItem.addSubItem(2, R.drawable.msg_share, LocaleController.getString("ShareTheme", R.string.ShareTheme));
            this.menuItem.addSubItem(3, R.drawable.msg_edit, LocaleController.getString("EditThemeColors", R.string.EditThemeColors));
            this.menuItem.addSubItem(1, R.drawable.msg_palette, LocaleController.getString("CreateNewThemeMenu", R.string.CreateNewThemeMenu));
            this.menuItem.addSubItem(4, R.drawable.msg_reset, LocaleController.getString("ThemeResetToDefaults", R.string.ThemeResetToDefaults));
            if (getMessagesController().getContentSettings() == null) {
                getMessagesController().getContentSettings(new Utilities.Callback() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda5
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        ThemeActivity.this.lambda$createView$2((TL_account.contentSettings) obj);
                    }
                });
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme));
        }
        this.actionBar.setActionBarMenuOnItemClick(new 1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i3) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i3);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i3, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i3, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i3, float f, float f2) {
                ThemeActivity.this.lambda$createView$11(context, view, i3, f, f2);
            }
        });
        if (this.currentType == 0) {
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setDurations(350L);
            defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setSupportsChangeAnimations(false);
            this.listView.setItemAnimator(defaultItemAnimator);
        }
        if (this.highlightSensitiveRow) {
            updateRows(false);
            this.highlightSensitiveRow = false;
            this.listView.scrollToPosition(this.listAdapter.getItemCount() - 1);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeActivity.this.lambda$createView$13();
                }
            }, 200L);
        }
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3;
        AlertDialog alertDialog;
        int i4;
        if (i == NotificationCenter.locationPermissionGranted) {
            updateSunTime(null, true);
            return;
        }
        if (i == NotificationCenter.didSetNewWallpapper || i == NotificationCenter.emojiLoaded) {
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView != null) {
                recyclerListView.invalidateViews();
            }
            updateMenuItem();
            return;
        }
        if (i == NotificationCenter.themeAccentListUpdated) {
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter == null || (i4 = this.themeAccentListRow) == -1) {
                return;
            }
            listAdapter.notifyItemChanged(i4, new Object());
            return;
        }
        if (i == NotificationCenter.themeListUpdated) {
            updateRows(true);
            return;
        }
        if (i == NotificationCenter.themeUploadedToServer) {
            Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) objArr[0];
            Theme.ThemeAccent themeAccent = (Theme.ThemeAccent) objArr[1];
            if (themeInfo != this.sharingTheme || themeAccent != this.sharingAccent) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("https://");
            sb.append(getMessagesController().linkPrefix);
            sb.append("/addtheme/");
            sb.append((themeAccent != null ? themeAccent.info : themeInfo.info).slug);
            String sb2 = sb.toString();
            showDialog(new ShareAlert(getParentActivity(), null, sb2, false, sb2, false));
            alertDialog = this.sharingProgressDialog;
            if (alertDialog == null) {
                return;
            }
        } else {
            if (i != NotificationCenter.themeUploadError) {
                if (i != NotificationCenter.needShareTheme) {
                    if (i == NotificationCenter.needSetDayNightTheme) {
                        updateMenuItem();
                        checkCurrentDayNight();
                        return;
                    } else {
                        if (i != NotificationCenter.emojiPreviewThemesChanged || (i3 = this.themeListRow2) < 0) {
                            return;
                        }
                        this.listAdapter.notifyItemChanged(i3);
                        return;
                    }
                }
                if (getParentActivity() == null || this.isPaused) {
                    return;
                }
                this.sharingTheme = (Theme.ThemeInfo) objArr[0];
                this.sharingAccent = (Theme.ThemeAccent) objArr[1];
                AlertDialog alertDialog2 = new AlertDialog(getParentActivity(), 3);
                this.sharingProgressDialog = alertDialog2;
                alertDialog2.setCanCancel(true);
                showDialog(this.sharingProgressDialog, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        ThemeActivity.this.lambda$didReceivedNotification$1(dialogInterface);
                    }
                });
                return;
            }
            Theme.ThemeInfo themeInfo2 = (Theme.ThemeInfo) objArr[0];
            Theme.ThemeAccent themeAccent2 = (Theme.ThemeAccent) objArr[1];
            if (themeInfo2 != this.sharingTheme || themeAccent2 != this.sharingAccent || (alertDialog = this.sharingProgressDialog) != null) {
                return;
            }
        }
        alertDialog.dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, TextSizeCell.class, BubbleRadiusCell.class, ChatListCell.class, NotificationsCheckCell.class, ThemesHorizontalListCell.class, TintRecyclerListView.class, TextCell.class, PeerColorActivity.ChangeNameColorCell.class, SwipeGestureSettingsView.class, DefaultThemesPreviewCell.class, AppIconsSelectorCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i4 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i5 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_windowBackgroundWhiteValueText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i7 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i8 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        int i9 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        int i10 = Theme.key_windowBackgroundWhiteGrayIcon;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"leftImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"rightImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        int i11 = Theme.key_player_progressBackground;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        int i12 = Theme.key_player_progress;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"checkImage"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addedIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BubbleRadiusCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i12));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{BubbleRadiusCell.class}, new String[]{"sizeBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackground));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackgroundChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i9));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected));
        Drawable[] shadowDrawables = Theme.chat_msgInDrawable.getShadowDrawables();
        int i13 = Theme.key_chat_inBubbleShadow;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, shadowDrawables, null, i13));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, Theme.chat_msgInMediaDrawable.getShadowDrawables(), null, i13));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient1));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, i13));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_messageTextIn));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_messageTextOut));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckDrawable}, null, Theme.key_chat_outSentCheck));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheckRead));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckReadSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyLine));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyLine));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyNameText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyNameText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyMessageText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyMessageText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inTimeText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outTimeText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_inTimeSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, null, null, null, Theme.key_chat_outTimeSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, i5));
        int i14 = Theme.key_windowBackgroundWhiteHintText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, i14));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AppIconsSelectorCell.class}, null, null, null, i6));
        arrayList.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ThemeActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ThemeActivity.this.lambda$getThemeDescriptions$18();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, i14, i5, i6));
        return arrayList;
    }

    public ThemeActivity highlightSensitiveRow() {
        this.highlightSensitiveRow = true;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeListUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeAccentListUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShareTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiPreviewThemesChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.themeUploadedToServer);
        getNotificationCenter().addObserver(this, NotificationCenter.themeUploadError);
        if (this.currentType == 0) {
            Theme.loadRemoteThemes(this.currentAccount, true);
            Theme.checkCurrentRemoteTheme(true);
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        stopLocationUpdate();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeListUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeAccentListUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShareTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiPreviewThemesChanged);
        getNotificationCenter().removeObserver(this, NotificationCenter.themeUploadedToServer);
        getNotificationCenter().removeObserver(this, NotificationCenter.themeUploadError);
        Theme.saveAutoNightThemeConfig();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            updateRows(true);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
            AndroidUtilities.setAdjustResizeToNothing(getParentActivity(), this.classGuid);
        }
    }
}
