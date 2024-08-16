package org.telegram.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SaveToGallerySettingsHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.VoIPHelper;
/* loaded from: classes4.dex */
public class DataSettingsActivity extends BaseFragment {
    private int callsSection2Row;
    private int callsSectionRow;
    private int clearDraftsRow;
    private int clearDraftsSectionRow;
    private int dataUsageRow;
    private int enableAllStreamInfoRow;
    private int enableAllStreamRow;
    private int enableCacheStreamRow;
    private int enableMkvRow;
    private int enableStreamRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int mediaDownloadSection2Row;
    private int mediaDownloadSectionRow;
    private int mobileRow;
    private int proxyRow;
    private int proxySection2Row;
    private int proxySectionRow;
    private int roamingRow;
    private int rowCount;
    private int saveToGalleryChannelsRow;
    private int saveToGalleryDividerRow;
    private int saveToGalleryGroupsRow;
    private int saveToGalleryPeerRow;
    private int saveToGallerySectionRow;
    private ArrayList<File> storageDirs;
    private int storageNumRow;
    private boolean storageUsageLoading;
    private int storageUsageRow;
    private long storageUsageSize;
    private int streamSectionRow;
    private boolean updateStorageUsageAnimated;
    private boolean updateVoipUseLessData;
    private int usageSection2Row;
    private int usageSectionRow;
    private int useLessDataForCallsRow;
    private int wifiRow;
    private int resetDownloadRow = -1;
    private int autoplayHeaderRow = -1;
    private int autoplayGifsRow = -1;
    private int autoplayVideoRow = -1;
    private int autoplaySectionRow = -1;
    private int quickRepliesRow = -1;

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DownloadController.getInstance(this.currentAccount).loadAutoDownloadConfig(true);
        updateRows(true);
        return true;
    }

    private void updateRows(boolean z) {
        int i;
        boolean z2 = false;
        this.usageSectionRow = 0;
        this.storageUsageRow = 1;
        this.rowCount = 3;
        this.dataUsageRow = 2;
        this.storageNumRow = -1;
        ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
        this.storageDirs = rootDirs;
        if (rootDirs.size() > 1) {
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.storageNumRow = i2;
        }
        int i3 = this.rowCount;
        this.usageSection2Row = i3;
        this.mediaDownloadSectionRow = i3 + 1;
        this.mobileRow = i3 + 2;
        this.wifiRow = i3 + 3;
        this.rowCount = i3 + 5;
        this.roamingRow = i3 + 4;
        DownloadController downloadController = getDownloadController();
        if (downloadController.lowPreset.equals(downloadController.getCurrentRoamingPreset()) && downloadController.lowPreset.isEnabled() == downloadController.roamingPreset.enabled && downloadController.mediumPreset.equals(downloadController.getCurrentMobilePreset()) && downloadController.mediumPreset.isEnabled() == downloadController.mobilePreset.enabled && downloadController.highPreset.equals(downloadController.getCurrentWiFiPreset()) && downloadController.highPreset.isEnabled() == downloadController.wifiPreset.enabled) {
            z2 = true;
        }
        int i4 = this.resetDownloadRow;
        if (z2) {
            i = -1;
        } else {
            i = this.rowCount;
            this.rowCount = i + 1;
        }
        this.resetDownloadRow = i;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null && !z) {
            if (i4 < 0 && i >= 0) {
                listAdapter.notifyItemChanged(this.roamingRow);
                this.listAdapter.notifyItemInserted(this.resetDownloadRow);
            } else if (i4 < 0 || i >= 0) {
                z = true;
            } else {
                listAdapter.notifyItemChanged(this.roamingRow);
                this.listAdapter.notifyItemRemoved(i4);
            }
        }
        int i5 = this.rowCount;
        this.mediaDownloadSection2Row = i5;
        this.saveToGallerySectionRow = i5 + 1;
        this.saveToGalleryPeerRow = i5 + 2;
        this.saveToGalleryGroupsRow = i5 + 3;
        this.saveToGalleryChannelsRow = i5 + 4;
        this.saveToGalleryDividerRow = i5 + 5;
        this.streamSectionRow = i5 + 6;
        int i6 = i5 + 8;
        this.rowCount = i6;
        this.enableStreamRow = i5 + 7;
        if (BuildVars.DEBUG_VERSION) {
            this.enableMkvRow = i6;
            this.rowCount = i5 + 10;
            this.enableAllStreamRow = i5 + 9;
        } else {
            this.enableAllStreamRow = -1;
            this.enableMkvRow = -1;
        }
        int i7 = this.rowCount;
        this.enableAllStreamInfoRow = i7;
        this.enableCacheStreamRow = -1;
        this.callsSectionRow = i7 + 1;
        this.useLessDataForCallsRow = i7 + 2;
        this.callsSection2Row = i7 + 3;
        this.proxySectionRow = i7 + 4;
        this.proxyRow = i7 + 5;
        this.proxySection2Row = i7 + 6;
        this.clearDraftsRow = i7 + 7;
        this.rowCount = i7 + 9;
        this.clearDraftsSectionRow = i7 + 8;
        ListAdapter listAdapter2 = this.listAdapter;
        if (listAdapter2 == null || !z) {
            return;
        }
        listAdapter2.notifyDataSetChanged();
    }

    private void loadCacheSize() {
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DataSettingsActivity.this.lambda$loadCacheSize$0();
            }
        };
        AndroidUtilities.runOnUIThread(runnable, 100L);
        final long currentTimeMillis = System.currentTimeMillis();
        CacheControlActivity.calculateTotalSize(new Utilities.Callback() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DataSettingsActivity.this.lambda$loadCacheSize$1(runnable, currentTimeMillis, (Long) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCacheSize$0() {
        int i;
        this.storageUsageLoading = true;
        if (this.listAdapter == null || (i = this.storageUsageRow) < 0) {
            return;
        }
        rebind(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCacheSize$1(Runnable runnable, long j, Long l) {
        int i;
        AndroidUtilities.cancelRunOnUIThread(runnable);
        this.updateStorageUsageAnimated = this.updateStorageUsageAnimated || System.currentTimeMillis() - j > 120;
        this.storageUsageSize = l.longValue();
        this.storageUsageLoading = false;
        if (this.listAdapter == null || (i = this.storageUsageRow) < 0) {
            return;
        }
        rebind(i);
    }

    private void rebind(int i) {
        if (this.listView == null || this.listAdapter == null) {
            return;
        }
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            RecyclerView.ViewHolder childViewHolder = this.listView.getChildViewHolder(this.listView.getChildAt(i2));
            if (childViewHolder != null && childViewHolder.getAdapterPosition() == i) {
                this.listAdapter.onBindViewHolder(childViewHolder, i);
                return;
            }
        }
    }

    private void rebindAll() {
        if (this.listView == null || this.listAdapter == null) {
            return;
        }
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            RecyclerView.ViewHolder childViewHolder = this.listView.getChildViewHolder(childAt);
            if (childViewHolder != null) {
                this.listAdapter.onBindViewHolder(childViewHolder, this.listView.getChildAdapterPosition(childAt));
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        CacheControlActivity.canceled = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString("DataSettings", R.string.DataSettings));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.DataSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    DataSettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.DataSettingsActivity.2
            @Override // org.telegram.ui.Components.RecyclerListView
            public Integer getSelectorColor(int i) {
                if (i == DataSettingsActivity.this.resetDownloadRow) {
                    return Integer.valueOf(Theme.multAlpha(getThemedColor(Theme.key_text_RedRegular), 0.1f));
                }
                return Integer.valueOf(getThemedColor(Theme.key_listSelector));
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        ((FrameLayout) this.fragmentView).addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                DataSettingsActivity.this.lambda$createView$8(context, view, i, f, f2);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDurations(350L);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$8(Context context, View view, final int i, float f, float f2) {
        int i2;
        DownloadController.Preset preset;
        DownloadController.Preset preset2;
        String str;
        String str2;
        boolean z;
        boolean z2;
        int size;
        int i3;
        String str3;
        boolean z3;
        String formatString;
        int i4;
        String str4;
        int i5 = 0;
        String str5 = "/storage/emulated/";
        int i6 = this.saveToGalleryGroupsRow;
        if (i == i6 || i == this.saveToGalleryChannelsRow || i == this.saveToGalleryPeerRow) {
            if (i == i6) {
                i2 = 2;
            } else {
                i2 = i == this.saveToGalleryChannelsRow ? 4 : 1;
            }
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                SaveToGallerySettingsHelper.getSettings(i2).toggle();
                AndroidUtilities.updateVisibleRows(this.listView);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("type", i2);
            presentFragment(new SaveToGallerySettingsActivity(bundle));
        } else if (i == this.mobileRow || i == this.roamingRow || i == this.wifiRow) {
            int i7 = 2;
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                this.listAdapter.isRowEnabled(this.resetDownloadRow);
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                boolean isChecked = notificationsCheckCell.isChecked();
                if (i == this.mobileRow) {
                    preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                    str2 = "currentMobilePreset";
                    str = "mobilePreset";
                    i7 = 0;
                } else if (i == this.wifiRow) {
                    preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                    str2 = "currentWifiPreset";
                    str = "wifiPreset";
                    i7 = 1;
                } else {
                    preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                    str = "roamingPreset";
                    str2 = "currentRoamingPreset";
                }
                if (!isChecked && preset.enabled) {
                    preset.set(preset2);
                    z = true;
                } else {
                    z = true;
                    preset.enabled = !preset.enabled;
                }
                SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                edit.putString(str, preset.toString());
                edit.putInt(str2, 3);
                edit.commit();
                notificationsCheckCell.setChecked(isChecked ^ z);
                RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
                if (findContainingViewHolder != null) {
                    this.listAdapter.onBindViewHolder(findContainingViewHolder, i);
                }
                DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                DownloadController.getInstance(this.currentAccount).savePresetToServer(i7);
                updateRows(false);
                return;
            }
            if (i != this.mobileRow) {
                i5 = i == this.wifiRow ? 1 : 2;
            }
            presentFragment(new DataAutoDownloadActivity(i5));
        } else if (i == this.resetDownloadRow) {
            if (getParentActivity() == null || !view.isEnabled()) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("ResetAutomaticMediaDownloadAlertTitle", R.string.ResetAutomaticMediaDownloadAlertTitle));
            builder.setMessage(LocaleController.getString("ResetAutomaticMediaDownloadAlert", R.string.ResetAutomaticMediaDownloadAlert));
            builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i8) {
                    DataSettingsActivity.this.lambda$createView$2(dialogInterface, i8);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        } else if (i == this.storageUsageRow) {
            presentFragment(new CacheControlActivity());
        } else if (i == this.useLessDataForCallsRow) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            int i8 = globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
            if (i8 != 0) {
                if (i8 == 1) {
                    i5 = 2;
                } else if (i8 == 2) {
                    i5 = 3;
                } else if (i8 == 3) {
                    i5 = 1;
                }
            }
            Dialog createSingleChoiceDialog = AlertsCreator.createSingleChoiceDialog(getParentActivity(), new String[]{LocaleController.getString("UseLessDataNever", R.string.UseLessDataNever), LocaleController.getString("UseLessDataOnRoaming", R.string.UseLessDataOnRoaming), LocaleController.getString("UseLessDataOnMobile", R.string.UseLessDataOnMobile), LocaleController.getString("UseLessDataAlways", R.string.UseLessDataAlways)}, LocaleController.getString("VoipUseLessData", R.string.VoipUseLessData), i5, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i9) {
                    DataSettingsActivity.this.lambda$createView$3(globalMainSettings, i, dialogInterface, i9);
                }
            });
            setVisibleDialog(createSingleChoiceDialog);
            createSingleChoiceDialog.show();
        } else if (i == this.dataUsageRow) {
            presentFragment(new DataUsage2Activity());
        } else if (i == this.storageNumRow) {
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString("StoragePath", R.string.StoragePath));
            LinearLayout linearLayout = new LinearLayout(getParentActivity());
            linearLayout.setOrientation(1);
            builder2.setView(linearLayout);
            String absolutePath = this.storageDirs.get(0).getAbsolutePath();
            if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                int size2 = this.storageDirs.size();
                for (int i9 = 0; i9 < size2; i9++) {
                    String absolutePath2 = this.storageDirs.get(i9).getAbsolutePath();
                    if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                        absolutePath = absolutePath2;
                        break;
                    }
                }
            }
            if (this.storageDirs.size() == 2) {
                if (this.storageDirs.get(0).getAbsolutePath().contains("/storage/emulated/") != this.storageDirs.get(1).getAbsolutePath().contains("/storage/emulated/")) {
                    z2 = false;
                    size = this.storageDirs.size();
                    i3 = 0;
                    while (i3 < size) {
                        File file = this.storageDirs.get(i3);
                        final String absolutePath3 = file.getAbsolutePath();
                        LanguageCell languageCell = new LanguageCell(context);
                        languageCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        languageCell.setTag(Integer.valueOf(i3));
                        final boolean contains = absolutePath3.contains(str5);
                        if (!z2 || contains) {
                            str3 = str5;
                            z3 = z2;
                            if (contains) {
                                formatString = LocaleController.formatString("StoragePathFreeInternal", R.string.StoragePathFreeInternal, AndroidUtilities.formatFileSize(file.getFreeSpace()));
                            } else {
                                formatString = LocaleController.formatString("StoragePathFreeExternal", R.string.StoragePathFreeExternal, AndroidUtilities.formatFileSize(file.getFreeSpace()));
                            }
                        } else {
                            str3 = str5;
                            z3 = z2;
                            formatString = LocaleController.formatString("StoragePathFreeValueExternal", R.string.StoragePathFreeValueExternal, AndroidUtilities.formatFileSize(file.getFreeSpace()), absolutePath3);
                        }
                        if (contains) {
                            i4 = R.string.InternalStorage;
                            str4 = "InternalStorage";
                        } else {
                            i4 = R.string.SdCard;
                            str4 = "SdCard";
                        }
                        languageCell.setValue(LocaleController.getString(str4, i4), formatString);
                        languageCell.setLanguageSelected(absolutePath3.startsWith(absolutePath), false);
                        languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                        linearLayout.addView(languageCell);
                        languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                DataSettingsActivity.this.lambda$createView$4(absolutePath3, contains, builder2, view2);
                            }
                        });
                        i3++;
                        z2 = z3;
                        str5 = str3;
                    }
                    builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder2.create());
                }
            }
            z2 = true;
            size = this.storageDirs.size();
            i3 = 0;
            while (i3 < size) {
            }
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder2.create());
        } else if (i == this.proxyRow) {
            presentFragment(new ProxyListActivity());
        } else if (i == this.enableStreamRow) {
            SharedConfig.toggleStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMedia);
        } else if (i == this.enableAllStreamRow) {
            SharedConfig.toggleStreamAllVideo();
            ((TextCheckCell) view).setChecked(SharedConfig.streamAllVideo);
        } else if (i == this.enableMkvRow) {
            SharedConfig.toggleStreamMkv();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMkv);
        } else if (i == this.enableCacheStreamRow) {
            SharedConfig.toggleSaveStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.saveStreamMedia);
        } else if (i == this.quickRepliesRow) {
            presentFragment(new QuickRepliesSettingsActivity());
        } else if (i == this.autoplayGifsRow) {
            SharedConfig.toggleAutoplayGifs();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.isAutoplayGifs());
            }
        } else if (i == this.autoplayVideoRow) {
            SharedConfig.toggleAutoplayVideo();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.isAutoplayVideo());
            }
        } else if (i == this.clearDraftsRow) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(getParentActivity());
            builder3.setTitle(LocaleController.getString("AreYouSureClearDraftsTitle", R.string.AreYouSureClearDraftsTitle));
            builder3.setMessage(LocaleController.getString("AreYouSureClearDrafts", R.string.AreYouSureClearDrafts));
            builder3.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i10) {
                    DataSettingsActivity.this.lambda$createView$7(dialogInterface, i10);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create2 = builder3.create();
            showDialog(create2);
            TextView textView2 = (TextView) create2.getButton(-1);
            if (textView2 != null) {
                textView2.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(DialogInterface dialogInterface, int i) {
        DownloadController.Preset preset;
        DownloadController.Preset preset2;
        String str;
        SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        for (int i2 = 0; i2 < 3; i2++) {
            if (i2 == 0) {
                preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                str = "mobilePreset";
            } else if (i2 == 1) {
                preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                str = "wifiPreset";
            } else {
                preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                str = "roamingPreset";
            }
            preset.set(preset2);
            preset.enabled = preset2.isEnabled();
            DownloadController.getInstance(this.currentAccount).currentMobilePreset = 3;
            edit.putInt("currentMobilePreset", 3);
            DownloadController.getInstance(this.currentAccount).currentWifiPreset = 3;
            edit.putInt("currentWifiPreset", 3);
            DownloadController.getInstance(this.currentAccount).currentRoamingPreset = 3;
            edit.putInt("currentRoamingPreset", 3);
            edit.putString(str, preset.toString());
        }
        edit.commit();
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
        for (int i3 = 0; i3 < 3; i3++) {
            DownloadController.getInstance(this.currentAccount).savePresetToServer(i3);
        }
        this.listAdapter.notifyItemRangeChanged(this.mobileRow, 4);
        updateRows(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(SharedPreferences sharedPreferences, int i, DialogInterface dialogInterface, int i2) {
        int i3;
        if (i2 != 0) {
            i3 = 3;
            if (i2 != 1) {
                i3 = i2 != 2 ? i2 != 3 ? -1 : 2 : 1;
            }
        } else {
            i3 = 0;
        }
        if (i3 != -1) {
            sharedPreferences.edit().putInt("VoipDataSaving", i3).commit();
            this.updateVoipUseLessData = true;
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(final String str, boolean z, final AlertDialog.Builder builder, View view) {
        if (TextUtils.equals(SharedConfig.storageCacheDir, str)) {
            return;
        }
        if (!z) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle(LocaleController.getString("DecreaseSpeed", R.string.DecreaseSpeed));
            builder2.setMessage(LocaleController.getString("SdCardAlert", R.string.SdCardAlert));
            builder2.setPositiveButton(LocaleController.getString("Proceed", R.string.Proceed), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    DataSettingsActivity.this.setStorageDirectory(str);
                    builder.getDismissRunnable().run();
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Back", R.string.Back), null);
            builder2.show();
            return;
        }
        setStorageDirectory(str);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(DialogInterface dialogInterface, int i) {
        getConnectionsManager().sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_messages_clearAllDrafts
            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i2, boolean z) {
                return TLRPC$Bool.TLdeserialize(abstractSerializedData, i2, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(2119757468);
            }
        }, new RequestDelegate() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                DataSettingsActivity.this.lambda$createView$6(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5() {
        getMediaDataController().clearAllDrafts(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DataSettingsActivity.this.lambda$createView$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStorageDirectory(String str) {
        SharedConfig.storageCacheDir = str;
        SharedConfig.saveConfig();
        if (str != null) {
            SharedConfig.readOnlyStorageDirAlertShowed = false;
        }
        rebind(this.storageNumRow);
        ImageLoader.getInstance().checkMediaPaths(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                DataSettingsActivity.this.lambda$setStorageDirectory$9();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStorageDirectory$9() {
        CacheControlActivity.resetCalculatedTotalSIze();
        loadCacheSize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        loadCacheSize();
        rebindAll();
        updateRows(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DataSettingsActivity.this.rowCount;
        }

        /* JADX WARN: Removed duplicated region for block: B:106:0x0331  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x026d  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            DownloadController.Preset currentRoamingPreset;
            String str;
            boolean z2;
            String string;
            CharSequence charSequence;
            String string2;
            boolean z3;
            StringBuilder sb;
            boolean z4;
            int i2;
            String str2;
            String str3 = null;
            r5 = null;
            DownloadController.Preset preset = null;
            int i3 = 0;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    if (i == DataSettingsActivity.this.clearDraftsSectionRow) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    }
                case 1:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    textSettingsCell.setCanDisable(false);
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (i != DataSettingsActivity.this.useLessDataForCallsRow) {
                        if (i != DataSettingsActivity.this.proxyRow) {
                            if (i != DataSettingsActivity.this.resetDownloadRow) {
                                if (i != DataSettingsActivity.this.quickRepliesRow) {
                                    if (i == DataSettingsActivity.this.clearDraftsRow) {
                                        textSettingsCell.setIcon(0);
                                        textSettingsCell.setText(LocaleController.getString("PrivacyDeleteCloudDrafts", R.string.PrivacyDeleteCloudDrafts), false);
                                        return;
                                    }
                                    return;
                                }
                                textSettingsCell.setIcon(0);
                                textSettingsCell.setText(LocaleController.getString("VoipQuickReplies", R.string.VoipQuickReplies), false);
                                return;
                            }
                            textSettingsCell.setIcon(0);
                            textSettingsCell.setCanDisable(true);
                            textSettingsCell.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                            textSettingsCell.setText(LocaleController.getString("ResetAutomaticMediaDownload", R.string.ResetAutomaticMediaDownload), false);
                            return;
                        }
                        textSettingsCell.setIcon(0);
                        textSettingsCell.setText(LocaleController.getString("ProxySettings", R.string.ProxySettings), false);
                        return;
                    }
                    textSettingsCell.setIcon(0);
                    int i4 = MessagesController.getGlobalMainSettings().getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                    if (i4 == 0) {
                        str3 = LocaleController.getString("UseLessDataNever", R.string.UseLessDataNever);
                    } else if (i4 == 1) {
                        str3 = LocaleController.getString("UseLessDataOnMobile", R.string.UseLessDataOnMobile);
                    } else if (i4 == 2) {
                        str3 = LocaleController.getString("UseLessDataAlways", R.string.UseLessDataAlways);
                    } else if (i4 == 3) {
                        str3 = LocaleController.getString("UseLessDataOnRoaming", R.string.UseLessDataOnRoaming);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("VoipUseLessData", R.string.VoipUseLessData), str3, DataSettingsActivity.this.updateVoipUseLessData, true);
                    DataSettingsActivity.this.updateVoipUseLessData = false;
                    return;
                case 2:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != DataSettingsActivity.this.mediaDownloadSectionRow) {
                        if (i != DataSettingsActivity.this.usageSectionRow) {
                            if (i != DataSettingsActivity.this.callsSectionRow) {
                                if (i != DataSettingsActivity.this.proxySectionRow) {
                                    if (i != DataSettingsActivity.this.streamSectionRow) {
                                        if (i != DataSettingsActivity.this.autoplayHeaderRow) {
                                            if (i == DataSettingsActivity.this.saveToGallerySectionRow) {
                                                headerCell.setText(LocaleController.getString("SaveToGallerySettings", R.string.SaveToGallerySettings));
                                                return;
                                            }
                                            return;
                                        }
                                        headerCell.setText(LocaleController.getString("AutoplayMedia", R.string.AutoplayMedia));
                                        return;
                                    }
                                    headerCell.setText(LocaleController.getString("Streaming", R.string.Streaming));
                                    return;
                                }
                                headerCell.setText(LocaleController.getString("Proxy", R.string.Proxy));
                                return;
                            }
                            headerCell.setText(LocaleController.getString("Calls", R.string.Calls));
                            return;
                        }
                        headerCell.setText(LocaleController.getString("DataUsage", R.string.DataUsage));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("AutomaticMediaDownload", R.string.AutomaticMediaDownload));
                    return;
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.enableStreamRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("EnableStreaming", R.string.EnableStreaming), SharedConfig.streamMedia, DataSettingsActivity.this.enableAllStreamRow != -1);
                        return;
                    } else if (i == DataSettingsActivity.this.enableCacheStreamRow) {
                        return;
                    } else {
                        if (i != DataSettingsActivity.this.enableMkvRow) {
                            if (i != DataSettingsActivity.this.enableAllStreamRow) {
                                if (i != DataSettingsActivity.this.autoplayGifsRow) {
                                    if (i == DataSettingsActivity.this.autoplayVideoRow) {
                                        textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayVideo", R.string.AutoplayVideo), SharedConfig.isAutoplayVideo(), false);
                                        return;
                                    }
                                    return;
                                }
                                textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayGIF", R.string.AutoplayGIF), SharedConfig.isAutoplayGifs(), true);
                                return;
                            }
                            textCheckCell.setTextAndCheck("(beta only) Stream All Videos", SharedConfig.streamAllVideo, false);
                            return;
                        }
                        textCheckCell.setTextAndCheck("(beta only) Show MKV as Video", SharedConfig.streamMkv, true);
                        return;
                    }
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("EnableAllStreamingInfo", R.string.EnableAllStreamingInfo));
                        return;
                    }
                    return;
                case 5:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    if (i != DataSettingsActivity.this.saveToGalleryPeerRow) {
                        if (i != DataSettingsActivity.this.saveToGalleryGroupsRow) {
                            if (i != DataSettingsActivity.this.saveToGalleryChannelsRow) {
                                if (i != DataSettingsActivity.this.mobileRow) {
                                    if (i == DataSettingsActivity.this.wifiRow) {
                                        string = LocaleController.getString("WhenConnectedOnWiFi", R.string.WhenConnectedOnWiFi);
                                        z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).wifiPreset.enabled;
                                        currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentWiFiPreset();
                                    } else {
                                        String string3 = LocaleController.getString("WhenRoaming", R.string.WhenRoaming);
                                        z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).roamingPreset.enabled;
                                        currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentRoamingPreset();
                                        str = string3;
                                        z2 = DataSettingsActivity.this.resetDownloadRow >= 0;
                                        preset = currentRoamingPreset;
                                        charSequence = null;
                                    }
                                } else {
                                    string = LocaleController.getString("WhenUsingMobileData", R.string.WhenUsingMobileData);
                                    z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).mobilePreset.enabled;
                                    currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentMobilePreset();
                                }
                                str = string;
                                z2 = true;
                                preset = currentRoamingPreset;
                                charSequence = null;
                            } else {
                                String string4 = LocaleController.getString("SaveToGalleryChannels", R.string.SaveToGalleryChannels);
                                charSequence = SaveToGallerySettingsHelper.channels.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                                z = SaveToGallerySettingsHelper.channels.enabled();
                                str = string4;
                                z2 = false;
                            }
                            if (preset == null) {
                                StringBuilder sb2 = new StringBuilder();
                                int i5 = 0;
                                boolean z5 = false;
                                int i6 = 0;
                                boolean z6 = false;
                                boolean z7 = false;
                                while (true) {
                                    int[] iArr = preset.mask;
                                    if (i5 < iArr.length) {
                                        if (!z5 && (iArr[i5] & 1) != 0) {
                                            i6++;
                                            z5 = true;
                                        }
                                        if (!z6 && (iArr[i5] & 4) != 0) {
                                            i6++;
                                            z6 = true;
                                        }
                                        if (!z7 && (iArr[i5] & 8) != 0) {
                                            i6++;
                                            z7 = true;
                                        }
                                        i5++;
                                    } else {
                                        if (!preset.enabled || i6 == 0) {
                                            z3 = z2;
                                            sb2.append(LocaleController.getString("NoMediaAutoDownload", R.string.NoMediaAutoDownload));
                                        } else {
                                            if (z5) {
                                                sb2.append(LocaleController.getString("AutoDownloadPhotosOn", R.string.AutoDownloadPhotosOn));
                                            }
                                            if (z6) {
                                                if (sb2.length() > 0) {
                                                    sb2.append(", ");
                                                }
                                                sb2.append(LocaleController.getString("AutoDownloadVideosOn", R.string.AutoDownloadVideosOn));
                                                z3 = z2;
                                                sb2.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(4)], true, false)));
                                            } else {
                                                z3 = z2;
                                            }
                                            if (z7) {
                                                if (sb2.length() > 0) {
                                                    sb2.append(", ");
                                                }
                                                sb2.append(LocaleController.getString("AutoDownloadFilesOn", R.string.AutoDownloadFilesOn));
                                                sb2.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(8)], true, false)));
                                            }
                                        }
                                        if ((z5 || z6 || z7) && z) {
                                            sb = sb2;
                                            z4 = true;
                                        } else {
                                            sb = sb2;
                                            z4 = false;
                                        }
                                    }
                                }
                            } else {
                                z3 = z2;
                                sb = charSequence;
                                z4 = z;
                            }
                            notificationsCheckCell.setAnimationsEnabled(true);
                            notificationsCheckCell.setTextAndValueAndCheck(str, sb, z4, 0, true, z3);
                            return;
                        }
                        string2 = LocaleController.getString("SaveToGalleryGroups", R.string.SaveToGalleryGroups);
                        charSequence = SaveToGallerySettingsHelper.groups.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        z = SaveToGallerySettingsHelper.groups.enabled();
                    } else {
                        string2 = LocaleController.getString("SaveToGalleryPrivate", R.string.SaveToGalleryPrivate);
                        charSequence = SaveToGallerySettingsHelper.user.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        z = SaveToGallerySettingsHelper.user.enabled();
                    }
                    str = string2;
                    z2 = true;
                    if (preset == null) {
                    }
                    notificationsCheckCell.setAnimationsEnabled(true);
                    notificationsCheckCell.setTextAndValueAndCheck(str, sb, z4, 0, true, z3);
                    return;
                case 6:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.storageUsageRow) {
                        if (!DataSettingsActivity.this.storageUsageLoading) {
                            textCell.setTextAndValueAndColorfulIcon(LocaleController.getString("StorageUsage", R.string.StorageUsage), DataSettingsActivity.this.storageUsageSize <= 0 ? "" : AndroidUtilities.formatFileSize(DataSettingsActivity.this.storageUsageSize), true, R.drawable.msg_filled_storageusage, DataSettingsActivity.this.getThemedColor(Theme.key_color_lightblue), true);
                            textCell.setDrawLoading(false, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        } else {
                            textCell.setTextAndValueAndColorfulIcon(LocaleController.getString("StorageUsage", R.string.StorageUsage), "", false, R.drawable.msg_filled_storageusage, DataSettingsActivity.this.getThemedColor(Theme.key_color_lightblue), true);
                            textCell.setDrawLoading(true, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        }
                        DataSettingsActivity.this.updateStorageUsageAnimated = false;
                        return;
                    } else if (i == DataSettingsActivity.this.dataUsageRow) {
                        StatsController statsController = StatsController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        textCell.setTextAndValueAndColorfulIcon(LocaleController.getString("NetworkUsage", R.string.NetworkUsage), AndroidUtilities.formatFileSize(statsController.getReceivedBytesCount(0, 6) + statsController.getReceivedBytesCount(1, 6) + statsController.getReceivedBytesCount(2, 6) + statsController.getSentBytesCount(0, 6) + statsController.getSentBytesCount(1, 6) + statsController.getSentBytesCount(2, 6)), true, R.drawable.msg_filled_datausage, DataSettingsActivity.this.getThemedColor(Theme.key_color_green), DataSettingsActivity.this.storageNumRow != -1);
                        return;
                    } else if (i == DataSettingsActivity.this.storageNumRow) {
                        String absolutePath = ((File) DataSettingsActivity.this.storageDirs.get(0)).getAbsolutePath();
                        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                            int size = DataSettingsActivity.this.storageDirs.size();
                            while (true) {
                                if (i3 < size) {
                                    String absolutePath2 = ((File) DataSettingsActivity.this.storageDirs.get(i3)).getAbsolutePath();
                                    if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                                        absolutePath = absolutePath2;
                                    } else {
                                        i3++;
                                    }
                                }
                            }
                        }
                        if (absolutePath == null || absolutePath.contains("/storage/emulated/")) {
                            i2 = R.string.InternalStorage;
                            str2 = "InternalStorage";
                        } else {
                            i2 = R.string.SdCard;
                            str2 = "SdCard";
                        }
                        textCell.setTextAndValueAndColorfulIcon(LocaleController.getString("StoragePath", R.string.StoragePath), LocaleController.getString(str2, i2), true, R.drawable.msg_filled_sdcard, DataSettingsActivity.this.getThemedColor(Theme.key_color_yellow), false);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 3) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != DataSettingsActivity.this.enableCacheStreamRow) {
                    if (adapterPosition != DataSettingsActivity.this.enableStreamRow) {
                        if (adapterPosition != DataSettingsActivity.this.enableAllStreamRow) {
                            if (adapterPosition != DataSettingsActivity.this.enableMkvRow) {
                                if (adapterPosition != DataSettingsActivity.this.autoplayGifsRow) {
                                    if (adapterPosition == DataSettingsActivity.this.autoplayVideoRow) {
                                        textCheckCell.setChecked(SharedConfig.isAutoplayVideo());
                                        return;
                                    }
                                    return;
                                }
                                textCheckCell.setChecked(SharedConfig.isAutoplayGifs());
                                return;
                            }
                            textCheckCell.setChecked(SharedConfig.streamMkv);
                            return;
                        }
                        textCheckCell.setChecked(SharedConfig.streamAllVideo);
                        return;
                    }
                    textCheckCell.setChecked(SharedConfig.streamMedia);
                    return;
                }
                textCheckCell.setChecked(SharedConfig.saveStreamMedia);
            }
        }

        public boolean isRowEnabled(int i) {
            return i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.useLessDataForCallsRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.proxyRow || i == DataSettingsActivity.this.clearDraftsRow || i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.quickRepliesRow || i == DataSettingsActivity.this.autoplayVideoRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.storageNumRow || i == DataSettingsActivity.this.saveToGalleryGroupsRow || i == DataSettingsActivity.this.saveToGalleryPeerRow || i == DataSettingsActivity.this.saveToGalleryChannelsRow || i == DataSettingsActivity.this.resetDownloadRow;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return isRowEnabled(viewHolder.getAdapterPosition());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shadowSectionCell;
            if (i == 0) {
                shadowSectionCell = new ShadowSectionCell(this.mContext);
            } else if (i == 1) {
                shadowSectionCell = new TextSettingsCell(this.mContext);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 2) {
                shadowSectionCell = new HeaderCell(this.mContext, 22);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 3) {
                shadowSectionCell = new TextCheckCell(this.mContext);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 4) {
                shadowSectionCell = new TextInfoPrivacyCell(this.mContext);
                shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            } else if (i == 5) {
                shadowSectionCell = new NotificationsCheckCell(this.mContext);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                shadowSectionCell = new TextCell(this.mContext);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            shadowSectionCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(shadowSectionCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == DataSettingsActivity.this.mediaDownloadSection2Row || i == DataSettingsActivity.this.usageSection2Row || i == DataSettingsActivity.this.callsSection2Row || i == DataSettingsActivity.this.proxySection2Row || i == DataSettingsActivity.this.autoplaySectionRow || i == DataSettingsActivity.this.clearDraftsSectionRow || i == DataSettingsActivity.this.saveToGalleryDividerRow) {
                return 0;
            }
            if (i == DataSettingsActivity.this.mediaDownloadSectionRow || i == DataSettingsActivity.this.streamSectionRow || i == DataSettingsActivity.this.callsSectionRow || i == DataSettingsActivity.this.usageSectionRow || i == DataSettingsActivity.this.proxySectionRow || i == DataSettingsActivity.this.autoplayHeaderRow || i == DataSettingsActivity.this.saveToGallerySectionRow) {
                return 2;
            }
            if (i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.autoplayVideoRow) {
                return 3;
            }
            if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                return 4;
            }
            if (i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.saveToGalleryGroupsRow || i == DataSettingsActivity.this.saveToGalleryPeerRow || i == DataSettingsActivity.this.saveToGalleryChannelsRow) {
                return 5;
            }
            return (i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.storageNumRow) ? 6 : 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i7 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        return arrayList;
    }
}
