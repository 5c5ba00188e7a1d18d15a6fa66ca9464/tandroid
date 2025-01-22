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
import org.telegram.tgnet.TLRPC;
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
    private ArrayList storageDirs;
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

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DataSettingsActivity.this.rowCount;
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

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return isRowEnabled(viewHolder.getAdapterPosition());
        }

        public boolean isRowEnabled(int i) {
            return i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.useLessDataForCallsRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.proxyRow || i == DataSettingsActivity.this.clearDraftsRow || i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.quickRepliesRow || i == DataSettingsActivity.this.autoplayVideoRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.storageNumRow || i == DataSettingsActivity.this.saveToGalleryGroupsRow || i == DataSettingsActivity.this.saveToGalleryPeerRow || i == DataSettingsActivity.this.saveToGalleryChannelsRow || i == DataSettingsActivity.this.resetDownloadRow;
        }

        /* JADX WARN: Removed duplicated region for block: B:51:0x024f  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x0309  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            Context context;
            int i2;
            int i3;
            int i4;
            int i5;
            String string;
            boolean isAutoplayGifs;
            boolean z;
            DownloadController.Preset currentRoamingPreset;
            String str;
            boolean z2;
            String string2;
            CharSequence charSequence;
            String string3;
            SaveToGallerySettingsHelper.SharedSettings sharedSettings;
            boolean z3;
            CharSequence charSequence2;
            boolean z4;
            String string4;
            String string5;
            String string6;
            int i6;
            int themedColor;
            boolean z5;
            boolean z6;
            TextCell textCell;
            String str2 = null;
            r5 = null;
            DownloadController.Preset preset = null;
            boolean z7 = true;
            int i7 = 0;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    int i8 = DataSettingsActivity.this.clearDraftsSectionRow;
                    View view = viewHolder.itemView;
                    if (i == i8) {
                        context = this.mContext;
                        i2 = R.drawable.greydivider_bottom;
                    } else {
                        context = this.mContext;
                        i2 = R.drawable.greydivider;
                    }
                    view.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i2, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 1:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    textSettingsCell.setCanDisable(false);
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (i != DataSettingsActivity.this.useLessDataForCallsRow) {
                        if (i == DataSettingsActivity.this.proxyRow) {
                            textSettingsCell.setIcon(0);
                            i3 = R.string.ProxySettings;
                        } else if (i == DataSettingsActivity.this.resetDownloadRow) {
                            textSettingsCell.setIcon(0);
                            textSettingsCell.setCanDisable(true);
                            textSettingsCell.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                            i3 = R.string.ResetAutomaticMediaDownload;
                        } else if (i == DataSettingsActivity.this.quickRepliesRow) {
                            textSettingsCell.setIcon(0);
                            i3 = R.string.VoipQuickReplies;
                        } else if (i == DataSettingsActivity.this.clearDraftsRow) {
                            textSettingsCell.setIcon(0);
                            i3 = R.string.PrivacyDeleteCloudDrafts;
                        }
                        textSettingsCell.setText(LocaleController.getString(i3), false);
                        break;
                    } else {
                        textSettingsCell.setIcon(0);
                        int i9 = MessagesController.getGlobalMainSettings().getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                        if (i9 == 0) {
                            i4 = R.string.UseLessDataNever;
                        } else if (i9 == 1) {
                            i4 = R.string.UseLessDataOnMobile;
                        } else if (i9 != 2) {
                            if (i9 == 3) {
                                i4 = R.string.UseLessDataOnRoaming;
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString(R.string.VoipUseLessData), str2, DataSettingsActivity.this.updateVoipUseLessData, true);
                            DataSettingsActivity.this.updateVoipUseLessData = false;
                            break;
                        } else {
                            i4 = R.string.UseLessDataAlways;
                        }
                        str2 = LocaleController.getString(i4);
                        textSettingsCell.setTextAndValue(LocaleController.getString(R.string.VoipUseLessData), str2, DataSettingsActivity.this.updateVoipUseLessData, true);
                        DataSettingsActivity.this.updateVoipUseLessData = false;
                    }
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.mediaDownloadSectionRow) {
                        i5 = R.string.AutomaticMediaDownload;
                    } else if (i == DataSettingsActivity.this.usageSectionRow) {
                        i5 = R.string.DataUsage;
                    } else if (i == DataSettingsActivity.this.callsSectionRow) {
                        i5 = R.string.Calls;
                    } else if (i == DataSettingsActivity.this.proxySectionRow) {
                        i5 = R.string.Proxy;
                    } else if (i == DataSettingsActivity.this.streamSectionRow) {
                        i5 = R.string.Streaming;
                    } else if (i == DataSettingsActivity.this.autoplayHeaderRow) {
                        i5 = R.string.AutoplayMedia;
                    } else if (i == DataSettingsActivity.this.saveToGallerySectionRow) {
                        i5 = R.string.SaveToGallerySettings;
                    }
                    headerCell.setText(LocaleController.getString(i5));
                    break;
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i != DataSettingsActivity.this.enableStreamRow) {
                        if (i != DataSettingsActivity.this.enableCacheStreamRow) {
                            if (i != DataSettingsActivity.this.enableMkvRow) {
                                if (i != DataSettingsActivity.this.enableAllStreamRow) {
                                    if (i != DataSettingsActivity.this.autoplayGifsRow) {
                                        if (i == DataSettingsActivity.this.autoplayVideoRow) {
                                            textCheckCell.setTextAndCheck(LocaleController.getString(R.string.AutoplayVideo), SharedConfig.isAutoplayVideo(), false);
                                            break;
                                        }
                                    } else {
                                        string = LocaleController.getString(R.string.AutoplayGIF);
                                        isAutoplayGifs = SharedConfig.isAutoplayGifs();
                                    }
                                } else {
                                    textCheckCell.setTextAndCheck("(beta only) Stream All Videos", SharedConfig.streamAllVideo, false);
                                    break;
                                }
                            } else {
                                textCheckCell.setTextAndCheck("(beta only) Show MKV as Video", SharedConfig.streamMkv, true);
                                break;
                            }
                        }
                    } else {
                        string = LocaleController.getString(R.string.EnableStreaming);
                        isAutoplayGifs = SharedConfig.streamMedia;
                        if (DataSettingsActivity.this.enableAllStreamRow == -1) {
                            z7 = false;
                        }
                    }
                    textCheckCell.setTextAndCheck(string, isAutoplayGifs, z7);
                    break;
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.EnableAllStreamingInfo));
                        break;
                    }
                    break;
                case 5:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    if (i != DataSettingsActivity.this.saveToGalleryPeerRow) {
                        if (i != DataSettingsActivity.this.saveToGalleryGroupsRow) {
                            if (i == DataSettingsActivity.this.saveToGalleryChannelsRow) {
                                String string7 = LocaleController.getString(R.string.SaveToGalleryChannels);
                                charSequence = SaveToGallerySettingsHelper.channels.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                                z = SaveToGallerySettingsHelper.channels.enabled();
                                str = string7;
                                z2 = false;
                            } else {
                                if (i == DataSettingsActivity.this.mobileRow) {
                                    string2 = LocaleController.getString(R.string.WhenUsingMobileData);
                                    z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).mobilePreset.enabled;
                                    currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentMobilePreset();
                                } else if (i == DataSettingsActivity.this.wifiRow) {
                                    string2 = LocaleController.getString(R.string.WhenConnectedOnWiFi);
                                    z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).wifiPreset.enabled;
                                    currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentWiFiPreset();
                                } else {
                                    String string8 = LocaleController.getString(R.string.WhenRoaming);
                                    z = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).roamingPreset.enabled;
                                    currentRoamingPreset = DownloadController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount).getCurrentRoamingPreset();
                                    str = string8;
                                    z2 = DataSettingsActivity.this.resetDownloadRow >= 0;
                                    preset = currentRoamingPreset;
                                    charSequence = null;
                                }
                                str = string2;
                                z2 = true;
                                preset = currentRoamingPreset;
                                charSequence = null;
                            }
                            if (preset == null) {
                                StringBuilder sb = new StringBuilder();
                                int i10 = 0;
                                boolean z8 = false;
                                int i11 = 0;
                                boolean z9 = false;
                                boolean z10 = false;
                                while (true) {
                                    int[] iArr = preset.mask;
                                    if (i10 < iArr.length) {
                                        if (!z8 && (iArr[i10] & 1) != 0) {
                                            i11++;
                                            z8 = true;
                                        }
                                        if (!z9 && (iArr[i10] & 4) != 0) {
                                            i11++;
                                            z9 = true;
                                        }
                                        if (!z10 && (iArr[i10] & 8) != 0) {
                                            i11++;
                                            z10 = true;
                                        }
                                        i10++;
                                    } else {
                                        if (!preset.enabled || i11 == 0) {
                                            z3 = z2;
                                            string4 = LocaleController.getString(R.string.NoMediaAutoDownload);
                                        } else {
                                            if (z8) {
                                                sb.append(LocaleController.getString(R.string.AutoDownloadPhotosOn));
                                            }
                                            if (z9) {
                                                if (sb.length() > 0) {
                                                    sb.append(", ");
                                                }
                                                sb.append(LocaleController.getString(R.string.AutoDownloadVideosOn));
                                                z3 = z2;
                                                sb.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(4)], true, false)));
                                            } else {
                                                z3 = z2;
                                            }
                                            if (z10) {
                                                if (sb.length() > 0) {
                                                    sb.append(", ");
                                                }
                                                sb.append(LocaleController.getString(R.string.AutoDownloadFilesOn));
                                                string4 = String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(8)], true, false));
                                            }
                                            if ((!z8 || z9 || z10) && z) {
                                                charSequence2 = sb;
                                                z4 = true;
                                            } else {
                                                charSequence2 = sb;
                                                z4 = false;
                                            }
                                        }
                                        sb.append(string4);
                                        if (z8) {
                                        }
                                        charSequence2 = sb;
                                        z4 = true;
                                    }
                                }
                            } else {
                                z3 = z2;
                                charSequence2 = charSequence;
                                z4 = z;
                            }
                            notificationsCheckCell.setAnimationsEnabled(true);
                            notificationsCheckCell.setTextAndValueAndCheck(str, charSequence2, z4, 0, true, z3);
                            break;
                        } else {
                            string3 = LocaleController.getString(R.string.SaveToGalleryGroups);
                            charSequence = SaveToGallerySettingsHelper.groups.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                            sharedSettings = SaveToGallerySettingsHelper.groups;
                        }
                    } else {
                        string3 = LocaleController.getString(R.string.SaveToGalleryPrivate);
                        charSequence = SaveToGallerySettingsHelper.user.createDescription(((BaseFragment) DataSettingsActivity.this).currentAccount);
                        sharedSettings = SaveToGallerySettingsHelper.user;
                    }
                    z = sharedSettings.enabled();
                    str = string3;
                    z2 = true;
                    if (preset == null) {
                    }
                    notificationsCheckCell.setAnimationsEnabled(true);
                    notificationsCheckCell.setTextAndValueAndCheck(str, charSequence2, z4, 0, true, z3);
                    break;
                case 6:
                    TextCell textCell2 = (TextCell) viewHolder.itemView;
                    if (i != DataSettingsActivity.this.storageUsageRow) {
                        if (i == DataSettingsActivity.this.dataUsageRow) {
                            StatsController statsController = StatsController.getInstance(((BaseFragment) DataSettingsActivity.this).currentAccount);
                            long receivedBytesCount = statsController.getReceivedBytesCount(0, 6) + statsController.getReceivedBytesCount(1, 6) + statsController.getReceivedBytesCount(2, 6) + statsController.getSentBytesCount(0, 6) + statsController.getSentBytesCount(1, 6) + statsController.getSentBytesCount(2, 6);
                            String string9 = LocaleController.getString(R.string.NetworkUsage);
                            string5 = AndroidUtilities.formatFileSize(receivedBytesCount);
                            i6 = R.drawable.msg_filled_datausage;
                            themedColor = DataSettingsActivity.this.getThemedColor(Theme.key_color_green);
                            z6 = DataSettingsActivity.this.storageNumRow != -1;
                            z5 = true;
                            textCell = textCell2;
                            string6 = string9;
                        } else if (i == DataSettingsActivity.this.storageNumRow) {
                            String absolutePath = ((File) DataSettingsActivity.this.storageDirs.get(0)).getAbsolutePath();
                            if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                                int size = DataSettingsActivity.this.storageDirs.size();
                                while (true) {
                                    if (i7 < size) {
                                        String absolutePath2 = ((File) DataSettingsActivity.this.storageDirs.get(i7)).getAbsolutePath();
                                        if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                                            absolutePath = absolutePath2;
                                        } else {
                                            i7++;
                                        }
                                    }
                                }
                            }
                            string5 = LocaleController.getString((absolutePath == null || absolutePath.contains("/storage/emulated/")) ? R.string.InternalStorage : R.string.SdCard);
                            string6 = LocaleController.getString(R.string.StoragePath);
                            i6 = R.drawable.msg_filled_sdcard;
                            themedColor = DataSettingsActivity.this.getThemedColor(Theme.key_color_yellow);
                            z5 = true;
                            z6 = false;
                            textCell = textCell2;
                        }
                        textCell.setTextAndValueAndColorfulIcon(string6, string5, z5, i6, themedColor, z6);
                        break;
                    } else {
                        if (DataSettingsActivity.this.storageUsageLoading) {
                            textCell2.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.StorageUsage), "", false, R.drawable.msg_filled_storageusage, DataSettingsActivity.this.getThemedColor(Theme.key_color_lightblue), true);
                            textCell2.setDrawLoading(true, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        } else {
                            textCell2.setTextAndValueAndColorfulIcon(LocaleController.getString(R.string.StorageUsage), DataSettingsActivity.this.storageUsageSize <= 0 ? "" : AndroidUtilities.formatFileSize(DataSettingsActivity.this.storageUsageSize), true, R.drawable.msg_filled_storageusage, DataSettingsActivity.this.getThemedColor(Theme.key_color_lightblue), true);
                            textCell2.setDrawLoading(false, 45, DataSettingsActivity.this.updateStorageUsageAnimated);
                        }
                        DataSettingsActivity.this.updateStorageUsageAnimated = false;
                        break;
                    }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shadowSectionCell;
            if (i != 0) {
                if (i == 1) {
                    shadowSectionCell = new TextSettingsCell(this.mContext);
                } else if (i == 2) {
                    shadowSectionCell = new HeaderCell(this.mContext, 22);
                } else if (i == 3) {
                    shadowSectionCell = new TextCheckCell(this.mContext);
                } else if (i != 4) {
                    shadowSectionCell = i != 5 ? new TextCell(this.mContext) : new NotificationsCheckCell(this.mContext);
                } else {
                    shadowSectionCell = new TextInfoPrivacyCell(this.mContext);
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                }
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                shadowSectionCell = new ShadowSectionCell(this.mContext);
            }
            shadowSectionCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(shadowSectionCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            boolean isAutoplayVideo;
            if (viewHolder.getItemViewType() == 3) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == DataSettingsActivity.this.enableCacheStreamRow) {
                    isAutoplayVideo = SharedConfig.saveStreamMedia;
                } else if (adapterPosition == DataSettingsActivity.this.enableStreamRow) {
                    isAutoplayVideo = SharedConfig.streamMedia;
                } else if (adapterPosition == DataSettingsActivity.this.enableAllStreamRow) {
                    isAutoplayVideo = SharedConfig.streamAllVideo;
                } else if (adapterPosition == DataSettingsActivity.this.enableMkvRow) {
                    isAutoplayVideo = SharedConfig.streamMkv;
                } else if (adapterPosition == DataSettingsActivity.this.autoplayGifsRow) {
                    isAutoplayVideo = SharedConfig.isAutoplayGifs();
                } else if (adapterPosition != DataSettingsActivity.this.autoplayVideoRow) {
                    return;
                } else {
                    isAutoplayVideo = SharedConfig.isAutoplayVideo();
                }
                textCheckCell.setChecked(isAutoplayVideo);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(AlertDialog alertDialog, int i) {
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
    public /* synthetic */ void lambda$createView$4(String str, AlertDialog.Builder builder, AlertDialog alertDialog, int i) {
        setStorageDirectory(str);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(final String str, boolean z, final AlertDialog.Builder builder, View view) {
        if (TextUtils.equals(SharedConfig.storageCacheDir, str)) {
            return;
        }
        if (z) {
            setStorageDirectory(str);
            builder.getDismissRunnable().run();
            return;
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        builder2.setTitle(LocaleController.getString(R.string.DecreaseSpeed));
        builder2.setMessage(LocaleController.getString(R.string.SdCardAlert));
        builder2.setPositiveButton(LocaleController.getString(R.string.Proceed), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                DataSettingsActivity.this.lambda$createView$4(str, builder, alertDialog, i);
            }
        });
        builder2.setNegativeButton(LocaleController.getString(R.string.Back), null);
        builder2.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        getMediaDataController().clearAllDrafts(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                DataSettingsActivity.this.lambda$createView$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(AlertDialog alertDialog, int i) {
        getConnectionsManager().sendRequest(new TLRPC.TL_messages_clearAllDrafts(), new RequestDelegate() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                DataSettingsActivity.this.lambda$createView$7(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0182  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$9(Context context, View view, final int i, float f, float f2) {
        BaseFragment dataAutoDownloadActivity;
        DownloadController.Preset preset;
        String str;
        String str2;
        DownloadController.Preset preset2;
        int i2;
        boolean z;
        TextView textView;
        TextCheckCell textCheckCell;
        boolean isAutoplayVideo;
        boolean z2;
        int size;
        int i3;
        String str3;
        String formatString;
        String str4 = "/storage/emulated/";
        int i4 = this.saveToGalleryGroupsRow;
        if (i == i4 || i == this.saveToGalleryChannelsRow || i == this.saveToGalleryPeerRow) {
            r7 = i != i4 ? i == this.saveToGalleryChannelsRow ? 4 : 1 : 2;
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                SaveToGallerySettingsHelper.getSettings(r7).toggle();
                AndroidUtilities.updateVisibleRows(this.listView);
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("type", r7);
                presentFragment(new SaveToGallerySettingsActivity(bundle));
                return;
            }
        }
        if (i != this.mobileRow && i != this.roamingRow && i != this.wifiRow) {
            if (i == this.resetDownloadRow) {
                if (getParentActivity() == null || !view.isEnabled()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString(R.string.ResetAutomaticMediaDownloadAlertTitle));
                builder.setMessage(LocaleController.getString(R.string.ResetAutomaticMediaDownloadAlert));
                builder.setPositiveButton(LocaleController.getString(R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda3
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i5) {
                        DataSettingsActivity.this.lambda$createView$2(alertDialog, i5);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                AlertDialog create = builder.create();
                showDialog(create);
                textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
            } else if (i == this.storageUsageRow) {
                dataAutoDownloadActivity = new CacheControlActivity();
            } else {
                if (i == this.useLessDataForCallsRow) {
                    final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    int i5 = globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                    if (i5 != 0) {
                        if (i5 == 1) {
                            r3 = 2;
                        } else if (i5 == 2) {
                            r3 = 3;
                        } else if (i5 == 3) {
                            r3 = 1;
                        }
                    }
                    Dialog createSingleChoiceDialog = AlertsCreator.createSingleChoiceDialog(getParentActivity(), new String[]{LocaleController.getString(R.string.UseLessDataNever), LocaleController.getString(R.string.UseLessDataOnRoaming), LocaleController.getString(R.string.UseLessDataOnMobile), LocaleController.getString(R.string.UseLessDataAlways)}, LocaleController.getString(R.string.VoipUseLessData), r3, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i6) {
                            DataSettingsActivity.this.lambda$createView$3(globalMainSettings, i, dialogInterface, i6);
                        }
                    });
                    setVisibleDialog(createSingleChoiceDialog);
                    createSingleChoiceDialog.show();
                    return;
                }
                if (i == this.dataUsageRow) {
                    dataAutoDownloadActivity = new DataUsage2Activity();
                } else {
                    if (i == this.storageNumRow) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                        builder2.setTitle(LocaleController.getString(R.string.StoragePath));
                        LinearLayout linearLayout = new LinearLayout(getParentActivity());
                        linearLayout.setOrientation(1);
                        builder2.setView(linearLayout);
                        String absolutePath = ((File) this.storageDirs.get(0)).getAbsolutePath();
                        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                            int size2 = this.storageDirs.size();
                            for (int i6 = 0; i6 < size2; i6++) {
                                String absolutePath2 = ((File) this.storageDirs.get(i6)).getAbsolutePath();
                                if (absolutePath2.startsWith(SharedConfig.storageCacheDir)) {
                                    absolutePath = absolutePath2;
                                    break;
                                }
                            }
                        }
                        if (this.storageDirs.size() == 2) {
                            if (((File) this.storageDirs.get(0)).getAbsolutePath().contains("/storage/emulated/") != ((File) this.storageDirs.get(1)).getAbsolutePath().contains("/storage/emulated/")) {
                                z2 = false;
                                size = this.storageDirs.size();
                                i3 = 0;
                                while (i3 < size) {
                                    File file = (File) this.storageDirs.get(i3);
                                    final String absolutePath3 = file.getAbsolutePath();
                                    LanguageCell languageCell = new LanguageCell(context);
                                    languageCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                                    languageCell.setTag(Integer.valueOf(i3));
                                    final boolean contains = absolutePath3.contains(str4);
                                    if (!z2 || contains) {
                                        str3 = str4;
                                        formatString = contains ? LocaleController.formatString("StoragePathFreeInternal", R.string.StoragePathFreeInternal, AndroidUtilities.formatFileSize(file.getFreeSpace())) : LocaleController.formatString("StoragePathFreeExternal", R.string.StoragePathFreeExternal, AndroidUtilities.formatFileSize(file.getFreeSpace()));
                                    } else {
                                        int i7 = R.string.StoragePathFreeValueExternal;
                                        str3 = str4;
                                        Object[] objArr = new Object[r7];
                                        objArr[0] = AndroidUtilities.formatFileSize(file.getFreeSpace());
                                        objArr[1] = absolutePath3;
                                        formatString = LocaleController.formatString("StoragePathFreeValueExternal", i7, objArr);
                                    }
                                    languageCell.setValue(LocaleController.getString(contains ? R.string.InternalStorage : R.string.SdCard), formatString);
                                    languageCell.setLanguageSelected(absolutePath3.startsWith(absolutePath), false);
                                    r7 = 2;
                                    languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                                    linearLayout.addView(languageCell);
                                    languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda5
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            DataSettingsActivity.this.lambda$createView$5(absolutePath3, contains, builder2, view2);
                                        }
                                    });
                                    i3++;
                                    str4 = str3;
                                }
                                builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                                showDialog(builder2.create());
                                return;
                            }
                        }
                        z2 = true;
                        size = this.storageDirs.size();
                        i3 = 0;
                        while (i3 < size) {
                        }
                        builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                        showDialog(builder2.create());
                        return;
                    }
                    if (i != this.proxyRow) {
                        if (i == this.enableStreamRow) {
                            SharedConfig.toggleStreamMedia();
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.streamMedia;
                        } else if (i == this.enableAllStreamRow) {
                            SharedConfig.toggleStreamAllVideo();
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.streamAllVideo;
                        } else if (i == this.enableMkvRow) {
                            SharedConfig.toggleStreamMkv();
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.streamMkv;
                        } else if (i == this.enableCacheStreamRow) {
                            SharedConfig.toggleSaveStreamMedia();
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.saveStreamMedia;
                        } else if (i == this.quickRepliesRow) {
                            dataAutoDownloadActivity = new QuickRepliesSettingsActivity();
                        } else if (i == this.autoplayGifsRow) {
                            SharedConfig.toggleAutoplayGifs();
                            if (!(view instanceof TextCheckCell)) {
                                return;
                            }
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.isAutoplayGifs();
                        } else if (i == this.autoplayVideoRow) {
                            SharedConfig.toggleAutoplayVideo();
                            if (!(view instanceof TextCheckCell)) {
                                return;
                            }
                            textCheckCell = (TextCheckCell) view;
                            isAutoplayVideo = SharedConfig.isAutoplayVideo();
                        } else {
                            if (i != this.clearDraftsRow) {
                                return;
                            }
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(getParentActivity());
                            builder3.setTitle(LocaleController.getString(R.string.AreYouSureClearDraftsTitle));
                            builder3.setMessage(LocaleController.getString(R.string.AreYouSureClearDrafts));
                            builder3.setPositiveButton(LocaleController.getString(R.string.Delete), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda6
                                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                                public final void onClick(AlertDialog alertDialog, int i8) {
                                    DataSettingsActivity.this.lambda$createView$8(alertDialog, i8);
                                }
                            });
                            builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                            AlertDialog create2 = builder3.create();
                            showDialog(create2);
                            textView = (TextView) create2.getButton(-1);
                            if (textView == null) {
                                return;
                            }
                        }
                        textCheckCell.setChecked(isAutoplayVideo);
                        return;
                    }
                    dataAutoDownloadActivity = new ProxyListActivity();
                }
            }
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            return;
        }
        if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
            this.listAdapter.isRowEnabled(this.resetDownloadRow);
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            boolean isChecked = notificationsCheckCell.isChecked();
            if (i == this.mobileRow) {
                preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                str2 = "currentMobilePreset";
                str = "mobilePreset";
                i2 = 0;
            } else if (i == this.wifiRow) {
                preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                str2 = "currentWifiPreset";
                str = "wifiPreset";
                i2 = 1;
            } else {
                preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                str = "roamingPreset";
                str2 = "currentRoamingPreset";
                preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                i2 = 2;
            }
            if (isChecked || !preset.enabled) {
                z = true;
                preset.enabled = !preset.enabled;
            } else {
                preset.set(preset2);
                z = true;
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
            DownloadController.getInstance(this.currentAccount).savePresetToServer(i2);
            updateRows(false);
            return;
        }
        dataAutoDownloadActivity = new DataAutoDownloadActivity(i != this.mobileRow ? i == this.wifiRow ? 1 : 2 : 0);
        presentFragment(dataAutoDownloadActivity);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStorageDirectory$10() {
        CacheControlActivity.resetCalculatedTotalSIze();
        loadCacheSize();
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

    private void setStorageDirectory(String str) {
        SharedConfig.storageCacheDir = str;
        SharedConfig.saveConfig();
        if (str != null) {
            SharedConfig.readOnlyStorageDirAlertShowed = false;
        }
        rebind(this.storageNumRow);
        ImageLoader.getInstance().checkMediaPaths(new Runnable() { // from class: org.telegram.ui.DataSettingsActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DataSettingsActivity.this.lambda$setStorageDirectory$10();
            }
        });
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString(R.string.DataSettings));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.DataSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    DataSettingsActivity.this.lambda$onBackPressed$323();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.DataSettingsActivity.2
            @Override // org.telegram.ui.Components.RecyclerListView
            public Integer getSelectorColor(int i) {
                return Integer.valueOf(i == DataSettingsActivity.this.resetDownloadRow ? Theme.multAlpha(getThemedColor(Theme.key_text_RedRegular), 0.1f) : getThemedColor(Theme.key_listSelector));
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
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
                DataSettingsActivity.this.lambda$createView$9(context, view, i, f, f2);
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    protected void onDialogDismiss(Dialog dialog) {
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DownloadController.getInstance(this.currentAccount).loadAutoDownloadConfig(true);
        updateRows(true);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        CacheControlActivity.canceled = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        loadCacheSize();
        rebindAll();
        updateRows(false);
    }
}
