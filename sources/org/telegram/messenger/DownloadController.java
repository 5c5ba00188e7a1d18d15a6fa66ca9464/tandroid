package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Pair;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.LaunchActivity;

/* loaded from: classes.dex */
public class DownloadController extends BaseController implements NotificationCenter.NotificationCenterDelegate {
    public static final int AUTODOWNLOAD_TYPE_AUDIO = 2;
    public static final int AUTODOWNLOAD_TYPE_DOCUMENT = 8;
    public static final int AUTODOWNLOAD_TYPE_PHOTO = 1;
    public static final int AUTODOWNLOAD_TYPE_VIDEO = 4;
    private static volatile DownloadController[] Instance = new DownloadController[4];
    public static final int PRESET_NUM_CHANNEL = 3;
    public static final int PRESET_NUM_CONTACT = 0;
    public static final int PRESET_NUM_GROUP = 2;
    public static final int PRESET_NUM_PM = 1;
    public static final int PRESET_SIZE_NUM_AUDIO = 3;
    public static final int PRESET_SIZE_NUM_DOCUMENT = 2;
    public static final int PRESET_SIZE_NUM_PHOTO = 0;
    public static final int PRESET_SIZE_NUM_VIDEO = 1;
    private HashMap<String, FileDownloadProgressListener> addLaterArray;
    private ArrayList<DownloadObject> audioDownloadQueue;
    Runnable clearUnviewedDownloadsRunnale;
    public int currentMobilePreset;
    public int currentRoamingPreset;
    public int currentWifiPreset;
    private ArrayList<FileDownloadProgressListener> deleteLaterArray;
    private ArrayList<DownloadObject> documentDownloadQueue;
    private HashMap<String, DownloadObject> downloadQueueKeys;
    private HashMap<Pair<Long, Integer>, DownloadObject> downloadQueuePairs;
    public final ArrayList<MessageObject> downloadingFiles;
    public Preset highPreset;
    private int lastCheckMask;
    private int lastTag;
    private boolean listenerInProgress;
    private boolean loadingAutoDownloadConfig;
    private HashMap<String, ArrayList<MessageObject>> loadingFileMessagesObservers;
    private HashMap<String, ArrayList<WeakReference<FileDownloadProgressListener>>> loadingFileObservers;
    public Preset lowPreset;
    public Preset mediumPreset;
    public Preset mobilePreset;
    private SparseArray<String> observersByTag;
    private ArrayList<DownloadObject> photoDownloadQueue;
    public final ArrayList<MessageObject> recentDownloadingFiles;
    public Preset roamingPreset;
    private LongSparseArray typingTimes;
    public final SparseArray<MessageObject> unviewedDownloads;
    private ArrayList<DownloadObject> videoDownloadQueue;
    public Preset wifiPreset;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DownloadingDocumentEntry {
        int hash;
        long id;

        private DownloadingDocumentEntry() {
        }
    }

    /* loaded from: classes3.dex */
    public interface FileDownloadProgressListener {
        int getObserverTag();

        void onFailedDownload(String str, boolean z);

        void onProgressDownload(String str, long j, long j2);

        void onProgressUpload(String str, long j, long j2, boolean z);

        void onSuccessDownload(String str);
    }

    /* loaded from: classes3.dex */
    public static class Preset {
        public boolean enabled;
        public boolean lessCallData;
        public int[] mask;
        public int maxVideoBitrate;
        public boolean preloadMusic;
        public boolean preloadStories;
        public boolean preloadVideo;
        public long[] sizes;

        /* JADX WARN: Code restructure failed: missing block: B:15:0x00db, code lost:
        
            if (org.telegram.messenger.Utilities.parseInt((java.lang.CharSequence) r11[11]).intValue() == 1) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x011f, code lost:
        
            if (org.telegram.messenger.Utilities.parseInt((java.lang.CharSequence) r11[13]).intValue() == 1) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x0134, code lost:
        
            r4 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0132, code lost:
        
            if (org.telegram.messenger.Utilities.parseInt((java.lang.CharSequence) (r3 == null ? r12.split("_") : r3)[13]).intValue() == 1) goto L43;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Preset(String str, String str2) {
            String[] strArr;
            boolean z;
            String str3;
            this.mask = new int[4];
            this.sizes = new long[4];
            String[] split = str.split("_");
            if (split.length >= 11) {
                boolean z2 = false;
                this.mask[0] = Utilities.parseInt((CharSequence) split[0]).intValue();
                this.mask[1] = Utilities.parseInt((CharSequence) split[1]).intValue();
                this.mask[2] = Utilities.parseInt((CharSequence) split[2]).intValue();
                this.mask[3] = Utilities.parseInt((CharSequence) split[3]).intValue();
                this.sizes[0] = Utilities.parseInt((CharSequence) split[4]).intValue();
                this.sizes[1] = Utilities.parseInt((CharSequence) split[5]).intValue();
                this.sizes[2] = Utilities.parseInt((CharSequence) split[6]).intValue();
                this.sizes[3] = Utilities.parseInt((CharSequence) split[7]).intValue();
                this.preloadVideo = Utilities.parseInt((CharSequence) split[8]).intValue() == 1;
                this.preloadMusic = Utilities.parseInt((CharSequence) split[9]).intValue() == 1;
                this.enabled = Utilities.parseInt((CharSequence) split[10]).intValue() == 1;
                if (split.length >= 12) {
                    strArr = null;
                } else {
                    String[] split2 = str2.split("_");
                    if (Utilities.parseInt((CharSequence) split2[11]).intValue() == 1) {
                        strArr = split2;
                        z = true;
                    } else {
                        strArr = split2;
                        z = false;
                    }
                }
                this.lessCallData = z;
                if (split.length >= 13) {
                    str3 = split[12];
                } else {
                    strArr = strArr == null ? str2.split("_") : strArr;
                    str3 = strArr[12];
                }
                this.maxVideoBitrate = Utilities.parseInt((CharSequence) str3).intValue();
                if (split.length >= 14) {
                }
                this.preloadStories = z2;
            }
        }

        public Preset(int[] iArr, long j, long j2, long j3, boolean z, boolean z2, boolean z3, boolean z4, int i, boolean z5) {
            int[] iArr2 = new int[4];
            this.mask = iArr2;
            this.sizes = new long[4];
            System.arraycopy(iArr, 0, iArr2, 0, Math.max(iArr.length, 4));
            long[] jArr = this.sizes;
            jArr[0] = j;
            jArr[1] = j2;
            jArr[2] = j3;
            jArr[3] = 524288;
            this.preloadVideo = z;
            this.preloadMusic = z2;
            this.lessCallData = z4;
            this.maxVideoBitrate = i;
            this.enabled = z3;
            this.preloadStories = z5;
        }

        public boolean equals(Preset preset) {
            int[] iArr = this.mask;
            int i = iArr[0];
            int[] iArr2 = preset.mask;
            if (i != iArr2[0] || iArr[1] != iArr2[1] || iArr[2] != iArr2[2] || iArr[3] != iArr2[3]) {
                return false;
            }
            long[] jArr = this.sizes;
            long j = jArr[0];
            long[] jArr2 = preset.sizes;
            return j == jArr2[0] && jArr[1] == jArr2[1] && jArr[2] == jArr2[2] && jArr[3] == jArr2[3] && this.preloadVideo == preset.preloadVideo && this.preloadMusic == preset.preloadMusic && this.maxVideoBitrate == preset.maxVideoBitrate && this.preloadStories == preset.preloadStories;
        }

        public boolean isEnabled() {
            int i = 0;
            while (true) {
                int[] iArr = this.mask;
                if (i >= iArr.length) {
                    return false;
                }
                if (iArr[i] != 0) {
                    return true;
                }
                i++;
            }
        }

        public void set(Preset preset) {
            int[] iArr = preset.mask;
            int[] iArr2 = this.mask;
            System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
            long[] jArr = preset.sizes;
            long[] jArr2 = this.sizes;
            System.arraycopy(jArr, 0, jArr2, 0, jArr2.length);
            this.preloadVideo = preset.preloadVideo;
            this.preloadMusic = preset.preloadMusic;
            this.lessCallData = preset.lessCallData;
            this.maxVideoBitrate = preset.maxVideoBitrate;
            this.preloadStories = preset.preloadStories;
        }

        public void set(TLRPC.TL_autoDownloadSettings tL_autoDownloadSettings) {
            this.preloadMusic = tL_autoDownloadSettings.audio_preload_next;
            this.preloadVideo = tL_autoDownloadSettings.video_preload_large;
            this.lessCallData = tL_autoDownloadSettings.phonecalls_less_data;
            this.maxVideoBitrate = tL_autoDownloadSettings.video_upload_maxbitrate;
            int i = 0;
            this.sizes[0] = Math.max(512000, tL_autoDownloadSettings.photo_size_max);
            this.sizes[1] = Math.max(512000L, tL_autoDownloadSettings.video_size_max);
            this.sizes[2] = Math.max(512000L, tL_autoDownloadSettings.file_size_max);
            while (true) {
                int[] iArr = this.mask;
                if (i >= iArr.length) {
                    this.preloadStories = true;
                    return;
                }
                if (tL_autoDownloadSettings.photo_size_max == 0 || tL_autoDownloadSettings.disabled) {
                    iArr[i] = iArr[i] & (-2);
                } else {
                    iArr[i] = iArr[i] | 1;
                }
                if (tL_autoDownloadSettings.video_size_max == 0 || tL_autoDownloadSettings.disabled) {
                    iArr[i] = iArr[i] & (-5);
                } else {
                    iArr[i] = iArr[i] | 4;
                }
                if (tL_autoDownloadSettings.file_size_max == 0 || tL_autoDownloadSettings.disabled) {
                    iArr[i] = iArr[i] & (-9);
                } else {
                    iArr[i] = iArr[i] | 8;
                }
                i++;
            }
        }

        public String toString() {
            return this.mask[0] + "_" + this.mask[1] + "_" + this.mask[2] + "_" + this.mask[3] + "_" + this.sizes[0] + "_" + this.sizes[1] + "_" + this.sizes[2] + "_" + this.sizes[3] + "_" + (this.preloadVideo ? 1 : 0) + "_" + (this.preloadMusic ? 1 : 0) + "_" + (this.enabled ? 1 : 0) + "_" + (this.lessCallData ? 1 : 0) + "_" + this.maxVideoBitrate + "_" + (this.preloadStories ? 1 : 0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x02e2  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x02e9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DownloadController(int i) {
        super(i);
        SharedPreferences.Editor putBoolean;
        Object obj;
        this.lastCheckMask = 0;
        this.photoDownloadQueue = new ArrayList<>();
        this.audioDownloadQueue = new ArrayList<>();
        this.documentDownloadQueue = new ArrayList<>();
        this.videoDownloadQueue = new ArrayList<>();
        this.downloadQueueKeys = new HashMap<>();
        this.downloadQueuePairs = new HashMap<>();
        this.loadingFileObservers = new HashMap<>();
        this.loadingFileMessagesObservers = new HashMap<>();
        this.observersByTag = new SparseArray<>();
        this.listenerInProgress = false;
        this.addLaterArray = new HashMap<>();
        this.deleteLaterArray = new ArrayList<>();
        this.lastTag = 0;
        this.typingTimes = new LongSparseArray();
        this.downloadingFiles = new ArrayList<>();
        this.recentDownloadingFiles = new ArrayList<>();
        this.unviewedDownloads = new SparseArray<>();
        this.clearUnviewedDownloadsRunnale = new Runnable() { // from class: org.telegram.messenger.DownloadController.2
            @Override // java.lang.Runnable
            public void run() {
                DownloadController.this.clearUnviewedDownloads();
                DownloadController.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
            }
        };
        SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
        Preset preset = new Preset(mainSettings.getString("preset0", "1_1_1_1_1048576_512000_512000_524288_0_0_1_1_50_0"), "1_1_1_1_1048576_512000_512000_524288_0_0_1_1_50_0");
        this.lowPreset = preset;
        preset.preloadStories = false;
        this.mediumPreset = new Preset(mainSettings.getString("preset1", "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0_100_1"), "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0_100_1");
        this.highPreset = new Preset(mainSettings.getString("preset2", "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0_100_1"), "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0_100_1");
        boolean contains = mainSettings.contains("newConfig");
        String str = "currentWifiPreset";
        if (contains || !getUserConfig().isClientActivated()) {
            this.mobilePreset = new Preset(mainSettings.getString("mobilePreset", "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0_100_1"), "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0_100_1");
            this.wifiPreset = new Preset(mainSettings.getString("wifiPreset", "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0_100_1"), "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0_100_1");
            this.roamingPreset = new Preset(mainSettings.getString("roamingPreset", "1_1_1_1_1048576_512000_512000_524288_0_0_1_1_50_0"), "1_1_1_1_1048576_512000_512000_524288_0_0_1_1_50_0");
            this.currentMobilePreset = mainSettings.getInt("currentMobilePreset", 3);
            this.currentWifiPreset = mainSettings.getInt("currentWifiPreset", 3);
            this.currentRoamingPreset = mainSettings.getInt("currentRoamingPreset", 3);
            putBoolean = contains ? putBoolean : mainSettings.edit().putBoolean("newConfig", true);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DownloadController.this.lambda$new$0();
                }
            });
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: org.telegram.messenger.DownloadController.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    DownloadController.this.checkAutodownloadSettings();
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            if (Build.VERSION.SDK_INT < 33) {
                ApplicationLoader.applicationContext.registerReceiver(broadcastReceiver, intentFilter, 4);
            } else {
                ApplicationLoader.applicationContext.registerReceiver(broadcastReceiver, intentFilter);
            }
            if (getUserConfig().isClientActivated()) {
                return;
            }
            checkAutodownloadSettings();
            return;
        }
        int[] iArr = new int[4];
        int[] iArr2 = new int[4];
        int[] iArr3 = new int[4];
        long[] jArr = new long[7];
        long[] jArr2 = new long[7];
        long[] jArr3 = new long[7];
        int i2 = 0;
        for (int i3 = 4; i2 < i3; i3 = 4) {
            StringBuilder sb = new StringBuilder();
            String str2 = str;
            sb.append("mobileDataDownloadMask");
            Object obj2 = "";
            if (i2 == 0) {
                obj = "";
            } else {
                obj = "";
                obj2 = Integer.valueOf(i2);
            }
            sb.append(obj2);
            String sb2 = sb.toString();
            if (i2 == 0 || mainSettings.contains(sb2)) {
                iArr[i2] = mainSettings.getInt(sb2, 13);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("wifiDownloadMask");
                sb3.append(i2 == 0 ? obj : Integer.valueOf(i2));
                iArr2[i2] = mainSettings.getInt(sb3.toString(), 13);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("roamingDownloadMask");
                sb4.append(i2 == 0 ? obj : Integer.valueOf(i2));
                iArr3[i2] = mainSettings.getInt(sb4.toString(), 1);
            } else {
                iArr[i2] = iArr[0];
                iArr2[i2] = iArr2[0];
                iArr3[i2] = iArr3[0];
            }
            i2++;
            str = str2;
        }
        jArr[2] = mainSettings.getLong("mobileMaxDownloadSize2", this.mediumPreset.sizes[1]);
        jArr[3] = mainSettings.getLong("mobileMaxDownloadSize3", this.mediumPreset.sizes[2]);
        jArr2[2] = mainSettings.getLong("wifiMaxDownloadSize2", this.highPreset.sizes[1]);
        jArr2[3] = mainSettings.getLong("wifiMaxDownloadSize3", this.highPreset.sizes[2]);
        jArr3[2] = mainSettings.getLong("roamingMaxDownloadSize2", this.lowPreset.sizes[1]);
        jArr3[3] = mainSettings.getLong("roamingMaxDownloadSize3", this.lowPreset.sizes[2]);
        boolean z = mainSettings.getBoolean("globalAutodownloadEnabled", true);
        this.mobilePreset = new Preset(iArr, this.mediumPreset.sizes[0], jArr[2], jArr[3], true, true, z, false, 100, false);
        this.wifiPreset = new Preset(iArr2, this.highPreset.sizes[0], jArr2[2], jArr2[3], true, true, z, false, 100, true);
        this.roamingPreset = new Preset(iArr3, this.lowPreset.sizes[0], jArr3[2], jArr3[3], false, false, z, true, 50, true);
        putBoolean = mainSettings.edit();
        putBoolean.putBoolean("newConfig", true);
        putBoolean.putString("mobilePreset", this.mobilePreset.toString());
        putBoolean.putString("wifiPreset", this.wifiPreset.toString());
        putBoolean.putString("roamingPreset", this.roamingPreset.toString());
        this.currentMobilePreset = 3;
        putBoolean.putInt("currentMobilePreset", 3);
        this.currentWifiPreset = 3;
        putBoolean.putInt(str, 3);
        this.currentRoamingPreset = 3;
        putBoolean.putInt("currentRoamingPreset", 3);
        putBoolean.commit();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$new$0();
            }
        });
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: org.telegram.messenger.DownloadController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                DownloadController.this.checkAutodownloadSettings();
            }
        };
        IntentFilter intentFilter2 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        if (Build.VERSION.SDK_INT < 33) {
        }
        if (getUserConfig().isClientActivated()) {
        }
    }

    private void checkDownloadFinished(String str, int i) {
        DownloadObject downloadObject = this.downloadQueueKeys.get(str);
        if (downloadObject != null) {
            this.downloadQueueKeys.remove(str);
            this.downloadQueuePairs.remove(new Pair(Long.valueOf(downloadObject.id), Integer.valueOf(downloadObject.type)));
            int i2 = 2;
            if (i == 0 || i == 2) {
                getMessagesStorage().removeFromDownloadQueue(downloadObject.id, downloadObject.type, false);
            }
            int i3 = downloadObject.type;
            if (i3 == 1) {
                this.photoDownloadQueue.remove(downloadObject);
                if (this.photoDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(1);
                    return;
                }
                return;
            }
            if (i3 == 2) {
                this.audioDownloadQueue.remove(downloadObject);
                if (!this.audioDownloadQueue.isEmpty()) {
                    return;
                }
            } else {
                i2 = 4;
                if (i3 == 4) {
                    this.videoDownloadQueue.remove(downloadObject);
                    if (!this.videoDownloadQueue.isEmpty()) {
                        return;
                    }
                } else {
                    i2 = 8;
                    if (i3 != 8) {
                        return;
                    }
                    this.documentDownloadQueue.remove(downloadObject);
                    if (!this.documentDownloadQueue.isEmpty()) {
                        return;
                    }
                }
            }
            newDownloadObjectsAvailable(i2);
        }
    }

    public static DownloadController getInstance(int i) {
        DownloadController downloadController = Instance[i];
        if (downloadController == null) {
            synchronized (DownloadController.class) {
                try {
                    downloadController = Instance[i];
                    if (downloadController == null) {
                        DownloadController[] downloadControllerArr = Instance;
                        DownloadController downloadController2 = new DownloadController(i);
                        downloadControllerArr[i] = downloadController2;
                        downloadController = downloadController2;
                    }
                } finally {
                }
            }
        }
        return downloadController;
    }

    public static float getProgress(long[] jArr) {
        if (jArr == null || jArr.length < 2) {
            return 0.0f;
        }
        long j = jArr[1];
        if (j == 0) {
            return 0.0f;
        }
        return Math.min(1.0f, ((float) jArr[0]) / ((float) j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentDownloadedFiles$12() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM downloading_documents WHERE state = 1").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteRecentFiles$13(ArrayList arrayList) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM downloading_documents WHERE hash = ? AND id = ?");
            for (int i = 0; i < arrayList.size(); i++) {
                executeFast.requery();
                executeFast.bindInteger(1, ((MessageObject) arrayList.get(i)).getDocument().dc_id);
                executeFast.bindLong(2, ((MessageObject) arrayList.get(i)).getDocument().id);
                executeFast.step();
                try {
                    FileLoader.getInstance(this.currentAccount).getPathToMessage(((MessageObject) arrayList.get(i)).messageOwner).delete();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            executeFast.dispose();
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAutoDownloadConfig$1(TLObject tLObject) {
        TLRPC.TL_autoDownloadSettings tL_autoDownloadSettings;
        this.loadingAutoDownloadConfig = false;
        getUserConfig().autoDownloadConfigLoadTime = System.currentTimeMillis();
        getUserConfig().saveConfig(false);
        if (tLObject != null) {
            TLRPC.TL_account_autoDownloadSettings tL_account_autoDownloadSettings = (TLRPC.TL_account_autoDownloadSettings) tLObject;
            this.lowPreset.set(tL_account_autoDownloadSettings.low);
            this.lowPreset.preloadStories = false;
            this.mediumPreset.set(tL_account_autoDownloadSettings.medium);
            this.highPreset.set(tL_account_autoDownloadSettings.high);
            int i = 0;
            while (i < 3) {
                Preset preset = i == 0 ? this.mobilePreset : i == 1 ? this.wifiPreset : this.roamingPreset;
                if (preset.equals(this.lowPreset)) {
                    preset.set(tL_account_autoDownloadSettings.low);
                    preset.preloadStories = false;
                } else {
                    if (preset.equals(this.mediumPreset)) {
                        tL_autoDownloadSettings = tL_account_autoDownloadSettings.medium;
                    } else if (preset.equals(this.highPreset)) {
                        tL_autoDownloadSettings = tL_account_autoDownloadSettings.high;
                    }
                    preset.set(tL_autoDownloadSettings);
                }
                i++;
            }
            SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
            edit.putString("mobilePreset", this.mobilePreset.toString());
            edit.putString("wifiPreset", this.wifiPreset.toString());
            edit.putString("roamingPreset", this.roamingPreset.toString());
            edit.putString("preset0", this.lowPreset.toString());
            edit.putString("preset1", this.mediumPreset.toString());
            edit.putString("preset2", this.highPreset.toString());
            edit.commit();
            this.lowPreset.toString();
            this.mediumPreset.toString();
            this.highPreset.toString();
            checkAutodownloadSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAutoDownloadConfig$2(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$loadAutoDownloadConfig$1(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDownloadingFiles$10(ArrayList arrayList, ArrayList arrayList2) {
        this.downloadingFiles.clear();
        this.downloadingFiles.addAll(arrayList);
        this.recentDownloadingFiles.clear();
        this.recentDownloadingFiles.addAll(arrayList2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDownloadingFiles$11() {
        final ArrayList<MessageObject> arrayList = new ArrayList<>();
        final ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT data, state FROM downloading_documents ORDER BY date DESC", new Object[0]);
            while (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                int intValue = queryFinalized.intValue(1);
                if (byteBufferValue != null) {
                    TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    if (TLdeserialize != null) {
                        TLdeserialize.readAttachPath(byteBufferValue, UserConfig.getInstance(this.currentAccount).clientUserId);
                        MessageObject messageObject = new MessageObject(this.currentAccount, TLdeserialize, false, false);
                        arrayList3.add(messageObject);
                        if (intValue == 0) {
                            arrayList.add(messageObject);
                        } else {
                            arrayList2.add(messageObject);
                        }
                    }
                    byteBufferValue.reuse();
                }
            }
            queryFinalized.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
        getFileLoader().checkMediaExistance(arrayList);
        getFileLoader().checkMediaExistance(arrayList2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$loadDownloadingFiles$10(arrayList, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoadProgressChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadProgressChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidFailedLoad);
        loadAutoDownloadConfig(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDownloadComplete$6(MessageObject messageObject) {
        try {
            getMessagesStorage().getDatabase().executeFast(String.format(Locale.ENGLISH, "UPDATE downloading_documents SET state = 1, date = %d WHERE hash = %d AND id = %d", Long.valueOf(System.currentTimeMillis()), Integer.valueOf(messageObject.getDocument().dc_id), Long.valueOf(messageObject.getDocument().id))).stepThis().dispose();
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT COUNT(*) FROM downloading_documents WHERE state = 1", new Object[0]);
            int intValue = queryFinalized.next() ? queryFinalized.intValue(0) : 0;
            queryFinalized.dispose();
            SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized("SELECT state FROM downloading_documents WHERE state = 1", new Object[0]);
            if (queryFinalized2.next()) {
                queryFinalized2.intValue(0);
            }
            queryFinalized2.dispose();
            if (intValue > 100) {
                SQLiteCursor queryFinalized3 = getMessagesStorage().getDatabase().queryFinalized("SELECT hash, id FROM downloading_documents WHERE state = 1 ORDER BY date ASC LIMIT " + (100 - intValue), new Object[0]);
                ArrayList arrayList = new ArrayList();
                while (queryFinalized3.next()) {
                    DownloadingDocumentEntry downloadingDocumentEntry = new DownloadingDocumentEntry();
                    downloadingDocumentEntry.hash = queryFinalized3.intValue(0);
                    downloadingDocumentEntry.id = queryFinalized3.longValue(1);
                    arrayList.add(downloadingDocumentEntry);
                }
                queryFinalized3.dispose();
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM downloading_documents WHERE hash = ? AND id = ?");
                for (int i = 0; i < arrayList.size(); i++) {
                    executeFast.requery();
                    executeFast.bindInteger(1, ((DownloadingDocumentEntry) arrayList.get(i)).hash);
                    executeFast.bindLong(2, ((DownloadingDocumentEntry) arrayList.get(i)).id);
                    executeFast.step();
                }
                executeFast.dispose();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDownloadComplete$7(TLRPC.Document document, final MessageObject messageObject) {
        for (int i = 0; i < this.downloadingFiles.size(); i++) {
            if (this.downloadingFiles.get(i).getDocument() != null && this.downloadingFiles.get(i).getDocument().id == document.id) {
                this.downloadingFiles.remove(i);
                int i2 = 0;
                while (true) {
                    if (i2 >= this.recentDownloadingFiles.size()) {
                        this.recentDownloadingFiles.add(0, messageObject);
                        putToUnviewedDownloads(messageObject);
                        break;
                    } else if (this.recentDownloadingFiles.get(i2).getDocument() != null && this.recentDownloadingFiles.get(i2).getDocument().id == document.id) {
                        break;
                    } else {
                        i2++;
                    }
                }
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        DownloadController.this.lambda$onDownloadComplete$6(messageObject);
                    }
                });
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDownloadFail$8(MessageObject messageObject, int i) {
        TLRPC.Document document = messageObject.getDocument();
        for (int i2 = 0; i2 < this.downloadingFiles.size(); i2++) {
            TLRPC.Document document2 = this.downloadingFiles.get(i2).getDocument();
            if (document2 == null || (document != null && document2.id == document.id)) {
                this.downloadingFiles.remove(i2);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
                if (i == 0) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 1, LocaleController.formatString("MessageNotFound", R.string.MessageNotFound, new Object[0]));
                    return;
                } else {
                    if (i == -1) {
                        LaunchActivity.checkFreeDiscSpaceStatic(2);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDownloadFail$9(MessageObject messageObject) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM downloading_documents WHERE hash = ? AND id = ?");
            executeFast.bindInteger(1, messageObject.getDocument().dc_id);
            executeFast.bindLong(2, messageObject.getDocument().id);
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$savePresetToServer$3(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadFile$4(MessageObject messageObject) {
        try {
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(messageObject.messageOwner.getObjectSize());
            messageObject.messageOwner.serializeToStream(nativeByteBuffer);
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO downloading_documents VALUES(?, ?, ?, ?, ?)");
            executeFast.bindByteBuffer(1, nativeByteBuffer);
            executeFast.bindInteger(2, messageObject.getDocument().dc_id);
            executeFast.bindLong(3, messageObject.getDocument().id);
            executeFast.bindLong(4, System.currentTimeMillis());
            executeFast.bindInteger(4, 0);
            executeFast.step();
            executeFast.dispose();
            nativeByteBuffer.reuse();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadFile$5(TLRPC.Document document, final MessageObject messageObject) {
        boolean z;
        boolean z2;
        TLRPC.Document document2;
        TLRPC.Document document3;
        if (document == null) {
            return;
        }
        int i = 0;
        while (true) {
            z = true;
            if (i >= this.recentDownloadingFiles.size()) {
                z2 = false;
                break;
            }
            MessageObject messageObject2 = this.recentDownloadingFiles.get(i);
            if (messageObject2 != null && (document3 = messageObject2.getDocument()) != null && document3.id == document.id) {
                z2 = true;
                break;
            }
            i++;
        }
        if (!z2) {
            for (int i2 = 0; i2 < this.downloadingFiles.size(); i2++) {
                MessageObject messageObject3 = this.downloadingFiles.get(i2);
                if (messageObject3 != null && (document2 = messageObject3.getDocument()) != null && document2.id == document.id) {
                    break;
                }
            }
        }
        z = z2;
        if (!z) {
            this.downloadingFiles.add(0, messageObject);
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    DownloadController.this.lambda$startDownloadFile$4(messageObject);
                }
            });
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
    }

    private void processLaterArrays() {
        for (Map.Entry<String, FileDownloadProgressListener> entry : this.addLaterArray.entrySet()) {
            addLoadingFileObserver(entry.getKey(), entry.getValue());
        }
        this.addLaterArray.clear();
        Iterator<FileDownloadProgressListener> it = this.deleteLaterArray.iterator();
        while (it.hasNext()) {
            removeLoadingFileObserver(it.next());
        }
        this.deleteLaterArray.clear();
    }

    private void putToUnviewedDownloads(MessageObject messageObject) {
        this.unviewedDownloads.put(messageObject.getId(), messageObject);
        AndroidUtilities.cancelRunOnUIThread(this.clearUnviewedDownloadsRunnale);
        AndroidUtilities.runOnUIThread(this.clearUnviewedDownloadsRunnale, 60000L);
    }

    public static int typeToIndex(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 2;
        }
        if (i == 4) {
            return 1;
        }
        return i == 8 ? 2 : 0;
    }

    public void addLoadingFileObserver(String str, FileDownloadProgressListener fileDownloadProgressListener) {
        addLoadingFileObserver(str, null, fileDownloadProgressListener);
    }

    public void addLoadingFileObserver(String str, MessageObject messageObject, FileDownloadProgressListener fileDownloadProgressListener) {
        if (this.listenerInProgress) {
            this.addLaterArray.put(str, fileDownloadProgressListener);
            return;
        }
        removeLoadingFileObserver(fileDownloadProgressListener);
        ArrayList<WeakReference<FileDownloadProgressListener>> arrayList = this.loadingFileObservers.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.loadingFileObservers.put(str, arrayList);
        }
        arrayList.add(new WeakReference<>(fileDownloadProgressListener));
        if (messageObject != null) {
            ArrayList<MessageObject> arrayList2 = this.loadingFileMessagesObservers.get(str);
            if (arrayList2 == null) {
                arrayList2 = new ArrayList<>();
                this.loadingFileMessagesObservers.put(str, arrayList2);
            }
            arrayList2.add(messageObject);
        }
        this.observersByTag.put(fileDownloadProgressListener.getObserverTag(), str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x006e, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r7.user_id)) != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x008f, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r18.from_id.user_id)) != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x00cc, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r18.from_id.user_id)) != false) goto L58;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int canDownloadMedia(TLRPC.Message message) {
        int i;
        char c;
        int autodownloadNetworkType;
        Preset currentMobilePreset;
        int i2;
        if (message == null || (message.media instanceof TLRPC.TL_messageMediaStory)) {
            return canPreloadStories() ? 2 : 0;
        }
        boolean isVideoMessage = MessageObject.isVideoMessage(message);
        if (isVideoMessage || MessageObject.isGifMessage(message) || MessageObject.isRoundVideoMessage(message) || MessageObject.isGameMessage(message)) {
            i = 4;
        } else if (MessageObject.isVoiceMessage(message)) {
            i = 2;
        } else if (MessageObject.isPhoto(message) || MessageObject.isStickerMessage(message) || MessageObject.isAnimatedStickerMessage(message)) {
            i = 1;
        } else {
            if (MessageObject.getDocument(message) == null) {
                return 0;
            }
            i = 8;
        }
        TLRPC.Peer peer = message.peer_id;
        if (peer != null) {
            if (peer.user_id == 0) {
                if (peer.chat_id != 0) {
                    if (message.from_id instanceof TLRPC.TL_peerUser) {
                    }
                    c = 2;
                } else {
                    TLRPC.Chat chat = peer.channel_id != 0 ? getMessagesController().getChat(Long.valueOf(message.peer_id.channel_id)) : null;
                    if (ChatObject.isChannel(chat) && chat.megagroup) {
                        if (message.from_id instanceof TLRPC.TL_peerUser) {
                        }
                        c = 2;
                    } else {
                        c = 3;
                    }
                }
                autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
                if (autodownloadNetworkType == 1) {
                    if (!this.wifiPreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentWiFiPreset();
                } else if (autodownloadNetworkType == 2) {
                    if (!this.roamingPreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentRoamingPreset();
                } else {
                    if (!this.mobilePreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentMobilePreset();
                }
                i2 = currentMobilePreset.mask[c];
                long[] jArr = currentMobilePreset.sizes;
                int typeToIndex = typeToIndex(i);
                long max = i == 2 ? Math.max(524288L, jArr[typeToIndex]) : jArr[typeToIndex];
                long messageSize = MessageObject.getMessageSize(message);
                if (!isVideoMessage && currentMobilePreset.preloadVideo && messageSize > max && max > 2097152) {
                    return (i2 & i) != 0 ? 2 : 0;
                }
                if (i != 1 || (messageSize != 0 && messageSize <= max)) {
                    return (i == 2 && (i2 & i) == 0) ? 0 : 1;
                }
                return 0;
            }
            c = 0;
            autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
            if (autodownloadNetworkType == 1) {
            }
            i2 = currentMobilePreset.mask[c];
            long[] jArr2 = currentMobilePreset.sizes;
            int typeToIndex2 = typeToIndex(i);
            if (i == 2) {
            }
            long messageSize2 = MessageObject.getMessageSize(message);
            if (!isVideoMessage) {
            }
            if (i != 1) {
            }
            if (i == 2) {
            }
        }
        c = 1;
        autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        if (autodownloadNetworkType == 1) {
        }
        i2 = currentMobilePreset.mask[c];
        long[] jArr22 = currentMobilePreset.sizes;
        int typeToIndex22 = typeToIndex(i);
        if (i == 2) {
        }
        long messageSize22 = MessageObject.getMessageSize(message);
        if (!isVideoMessage) {
        }
        if (i != 1) {
        }
        if (i == 2) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x004a, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r5.user_id)) != false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x006b, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r14.from_id.user_id)) != false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00a8, code lost:
    
        if (getContactsController().contactsDict.containsKey(java.lang.Long.valueOf(r14.from_id.user_id)) != false) goto L46;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int canDownloadMedia(TLRPC.Message message, TLRPC.MessageMedia messageMedia) {
        int i;
        boolean z;
        char c;
        int autodownloadNetworkType;
        Preset currentMobilePreset;
        int i2;
        if (message == null || (messageMedia instanceof TLRPC.TL_messageMediaStory)) {
            return canPreloadStories() ? 2 : 0;
        }
        if (MessageObject.isVideoDocument(messageMedia.document)) {
            i = 4;
            z = true;
        } else {
            if (MessageObject.isVoiceDocument(messageMedia.document)) {
                i = 2;
            } else if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                i = 1;
            } else {
                if (messageMedia.document == null) {
                    return 0;
                }
                i = 8;
            }
            z = false;
        }
        TLRPC.Peer peer = message.peer_id;
        if (peer != null) {
            if (peer.user_id == 0) {
                if (peer.chat_id != 0) {
                    if (message.from_id instanceof TLRPC.TL_peerUser) {
                    }
                    c = 2;
                } else {
                    TLRPC.Chat chat = peer.channel_id != 0 ? getMessagesController().getChat(Long.valueOf(message.peer_id.channel_id)) : null;
                    if (ChatObject.isChannel(chat) && chat.megagroup) {
                        if (message.from_id instanceof TLRPC.TL_peerUser) {
                        }
                        c = 2;
                    } else {
                        c = 3;
                    }
                }
                autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
                if (autodownloadNetworkType == 1) {
                    if (!this.wifiPreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentWiFiPreset();
                } else if (autodownloadNetworkType == 2) {
                    if (!this.roamingPreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentRoamingPreset();
                } else {
                    if (!this.mobilePreset.enabled) {
                        return 0;
                    }
                    currentMobilePreset = getCurrentMobilePreset();
                }
                i2 = currentMobilePreset.mask[c];
                long[] jArr = currentMobilePreset.sizes;
                int typeToIndex = typeToIndex(i);
                long max = i == 2 ? Math.max(524288L, jArr[typeToIndex]) : jArr[typeToIndex];
                long mediaSize = MessageObject.getMediaSize(messageMedia);
                if (!z && currentMobilePreset.preloadVideo && mediaSize > max && max > 2097152) {
                    return (i2 & i) != 0 ? 2 : 0;
                }
                if (i != 1 || (mediaSize != 0 && mediaSize <= max)) {
                    return (i == 2 && (i2 & i) == 0) ? 0 : 1;
                }
                return 0;
            }
            c = 0;
            autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
            if (autodownloadNetworkType == 1) {
            }
            i2 = currentMobilePreset.mask[c];
            long[] jArr2 = currentMobilePreset.sizes;
            int typeToIndex2 = typeToIndex(i);
            if (i == 2) {
            }
            long mediaSize2 = MessageObject.getMediaSize(messageMedia);
            if (!z) {
            }
            if (i != 1) {
            }
            if (i == 2) {
            }
        }
        c = 1;
        autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        if (autodownloadNetworkType == 1) {
        }
        i2 = currentMobilePreset.mask[c];
        long[] jArr22 = currentMobilePreset.sizes;
        int typeToIndex22 = typeToIndex(i);
        if (i == 2) {
        }
        long mediaSize22 = MessageObject.getMediaSize(messageMedia);
        if (!z) {
        }
        if (i != 1) {
        }
        if (i == 2) {
        }
    }

    public boolean canDownloadMedia(int i, long j) {
        Preset currentMobilePreset;
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        if (autodownloadNetworkType == 1) {
            if (!this.wifiPreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentWiFiPreset();
        } else if (autodownloadNetworkType == 2) {
            if (!this.roamingPreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentRoamingPreset();
        } else {
            if (!this.mobilePreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentMobilePreset();
        }
        int i2 = currentMobilePreset.mask[1];
        long j2 = currentMobilePreset.sizes[typeToIndex(i)];
        if (i == 1 || (j != 0 && j <= j2)) {
            return i == 2 || (i & i2) != 0;
        }
        return false;
    }

    public boolean canDownloadMedia(MessageObject messageObject) {
        TL_stories.StoryItem storyItem;
        TLRPC.MessageMedia messageMedia;
        if (messageObject.type == 23) {
            return (!SharedConfig.isAutoplayVideo() || (storyItem = ((TLRPC.TL_messageMediaStory) MessageObject.getMedia(messageObject)).storyItem) == null || (messageMedia = storyItem.media) == null || messageMedia.document == null || !storyItem.isPublic) ? false : true;
        }
        if (messageObject.sponsoredMedia != null) {
            return true;
        }
        return !messageObject.isHiddenSensitive() && canDownloadMedia(messageObject.messageOwner) == 1;
    }

    public int canDownloadMediaType(MessageObject messageObject) {
        TL_stories.StoryItem storyItem;
        TLRPC.MessageMedia messageMedia;
        if (messageObject.type == 23) {
            return (!SharedConfig.isAutoplayVideo() || (storyItem = ((TLRPC.TL_messageMediaStory) MessageObject.getMedia(messageObject)).storyItem) == null || (messageMedia = storyItem.media) == null || messageMedia.document == null || !storyItem.isPublic) ? 0 : 2;
        }
        if (messageObject.sponsoredMedia != null) {
            return 2;
        }
        if (messageObject.isHiddenSensitive()) {
            return 0;
        }
        return canDownloadMedia(messageObject.messageOwner);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canDownloadNextTrack() {
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        return autodownloadNetworkType == 1 ? this.wifiPreset.enabled && getCurrentWiFiPreset().preloadMusic : autodownloadNetworkType == 2 ? this.roamingPreset.enabled && getCurrentRoamingPreset().preloadMusic : this.mobilePreset.enabled && getCurrentMobilePreset().preloadMusic;
    }

    public boolean canPreloadStories() {
        Preset currentMobilePreset;
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        if (autodownloadNetworkType == 1) {
            if (!this.wifiPreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentWiFiPreset();
        } else if (autodownloadNetworkType == 2) {
            if (!this.roamingPreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentRoamingPreset();
        } else {
            if (!this.mobilePreset.enabled) {
                return false;
            }
            currentMobilePreset = getCurrentMobilePreset();
        }
        return currentMobilePreset.preloadStories;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancelDownloading(ArrayList<Pair<Long, Integer>> arrayList) {
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            DownloadObject downloadObject = this.downloadQueuePairs.get(arrayList.get(i));
            if (downloadObject != null) {
                TLObject tLObject = downloadObject.object;
                if (tLObject instanceof TLRPC.Document) {
                    getFileLoader().cancelLoadFile((TLRPC.Document) tLObject, true);
                } else if ((tLObject instanceof TLRPC.Photo) && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo) tLObject).sizes, AndroidUtilities.getPhotoSize())) != null) {
                    getFileLoader().cancelLoadFile(closestPhotoSizeWithSize, true);
                }
            }
        }
    }

    public void checkAutodownloadSettings() {
        int currentDownloadMask = getCurrentDownloadMask();
        if (currentDownloadMask == this.lastCheckMask) {
            return;
        }
        this.lastCheckMask = currentDownloadMask;
        if ((currentDownloadMask & 1) == 0) {
            for (int i = 0; i < this.photoDownloadQueue.size(); i++) {
                DownloadObject downloadObject = this.photoDownloadQueue.get(i);
                TLObject tLObject = downloadObject.object;
                if (tLObject instanceof TLRPC.Photo) {
                    getFileLoader().cancelLoadFile(FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo) tLObject).sizes, AndroidUtilities.getPhotoSize()));
                } else if (tLObject instanceof TLRPC.Document) {
                    getFileLoader().cancelLoadFile((TLRPC.Document) downloadObject.object);
                }
            }
            this.photoDownloadQueue.clear();
        } else if (this.photoDownloadQueue.isEmpty()) {
            newDownloadObjectsAvailable(1);
        }
        if ((currentDownloadMask & 2) == 0) {
            for (int i2 = 0; i2 < this.audioDownloadQueue.size(); i2++) {
                getFileLoader().cancelLoadFile((TLRPC.Document) this.audioDownloadQueue.get(i2).object);
            }
            this.audioDownloadQueue.clear();
        } else if (this.audioDownloadQueue.isEmpty()) {
            newDownloadObjectsAvailable(2);
        }
        if ((currentDownloadMask & 8) == 0) {
            for (int i3 = 0; i3 < this.documentDownloadQueue.size(); i3++) {
                getFileLoader().cancelLoadFile((TLRPC.Document) this.documentDownloadQueue.get(i3).object);
            }
            this.documentDownloadQueue.clear();
        } else if (this.documentDownloadQueue.isEmpty()) {
            newDownloadObjectsAvailable(8);
        }
        if ((currentDownloadMask & 4) == 0) {
            for (int i4 = 0; i4 < this.videoDownloadQueue.size(); i4++) {
                getFileLoader().cancelLoadFile((TLRPC.Document) this.videoDownloadQueue.get(i4).object);
            }
            this.videoDownloadQueue.clear();
        } else if (this.videoDownloadQueue.isEmpty()) {
            newDownloadObjectsAvailable(4);
        }
        int autodownloadMaskAll = getAutodownloadMaskAll();
        if (autodownloadMaskAll == 0) {
            getMessagesStorage().clearDownloadQueue(0);
            return;
        }
        if ((autodownloadMaskAll & 1) == 0) {
            getMessagesStorage().clearDownloadQueue(1);
        }
        if ((autodownloadMaskAll & 2) == 0) {
            getMessagesStorage().clearDownloadQueue(2);
        }
        if ((autodownloadMaskAll & 4) == 0) {
            getMessagesStorage().clearDownloadQueue(4);
        }
        if ((autodownloadMaskAll & 8) == 0) {
            getMessagesStorage().clearDownloadQueue(8);
        }
    }

    public void checkUnviewedDownloads(int i, long j) {
        MessageObject messageObject = this.unviewedDownloads.get(i);
        if (messageObject == null || messageObject.getDialogId() != j) {
            return;
        }
        this.unviewedDownloads.remove(i);
        if (this.unviewedDownloads.size() == 0) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
        }
    }

    public void cleanup() {
        this.photoDownloadQueue.clear();
        this.audioDownloadQueue.clear();
        this.documentDownloadQueue.clear();
        this.videoDownloadQueue.clear();
        this.downloadQueueKeys.clear();
        this.downloadQueuePairs.clear();
        this.typingTimes.clear();
    }

    public void clearRecentDownloadedFiles() {
        this.recentDownloadingFiles.clear();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$clearRecentDownloadedFiles$12();
            }
        });
    }

    public void clearUnviewedDownloads() {
        this.unviewedDownloads.clear();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0045, code lost:
    
        r3.remove(r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void deleteRecentFiles(final ArrayList<MessageObject> arrayList) {
        ArrayList<MessageObject> arrayList2;
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.recentDownloadingFiles.size()) {
                    i2 = 0;
                    while (i2 < this.downloadingFiles.size()) {
                        if (arrayList.get(i).getId() == this.downloadingFiles.get(i2).getId() && this.downloadingFiles.get(i2).getDialogId() == arrayList.get(i).getDialogId()) {
                            arrayList2 = this.downloadingFiles;
                        } else {
                            i2++;
                        }
                    }
                } else {
                    if (arrayList.get(i).getId() == this.recentDownloadingFiles.get(i2).getId() && this.recentDownloadingFiles.get(i2).getDialogId() == arrayList.get(i).getDialogId()) {
                        arrayList2 = this.recentDownloadingFiles;
                        break;
                    }
                    i2++;
                }
            }
            arrayList.get(i).putInDownloadsStore = false;
            FileLoader.getInstance(this.currentAccount).loadFile(arrayList.get(i).getDocument(), arrayList.get(i), 0, 0);
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(arrayList.get(i).getDocument(), true);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$deleteRecentFiles$13(arrayList);
            }
        });
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        LongSparseArray longSparseArray;
        long currentTimeMillis;
        if (i == NotificationCenter.fileLoadFailed || i == NotificationCenter.httpFileDidFailedLoad) {
            String str = (String) objArr[0];
            Integer num = (Integer) objArr[1];
            this.listenerInProgress = true;
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList = this.loadingFileObservers.get(str);
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i3 = 0; i3 < size; i3++) {
                    WeakReference<FileDownloadProgressListener> weakReference = arrayList.get(i3);
                    if (weakReference.get() != null) {
                        weakReference.get().onFailedDownload(str, num.intValue() == 1);
                        if (num.intValue() != 1) {
                            this.observersByTag.remove(weakReference.get().getObserverTag());
                        }
                    }
                }
                if (num.intValue() != 1) {
                    this.loadingFileObservers.remove(str);
                }
            }
            this.listenerInProgress = false;
            processLaterArrays();
            checkDownloadFinished(str, num.intValue());
            return;
        }
        if (i == NotificationCenter.fileLoaded || i == NotificationCenter.httpFileDidLoad) {
            this.listenerInProgress = true;
            String str2 = (String) objArr[0];
            ArrayList<MessageObject> arrayList2 = this.loadingFileMessagesObservers.get(str2);
            if (arrayList2 != null) {
                int size2 = arrayList2.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    arrayList2.get(i4).mediaExists = true;
                }
                this.loadingFileMessagesObservers.remove(str2);
            }
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList3 = this.loadingFileObservers.get(str2);
            if (arrayList3 != null) {
                int size3 = arrayList3.size();
                for (int i5 = 0; i5 < size3; i5++) {
                    WeakReference<FileDownloadProgressListener> weakReference2 = arrayList3.get(i5);
                    if (weakReference2.get() != null) {
                        weakReference2.get().onSuccessDownload(str2);
                        this.observersByTag.remove(weakReference2.get().getObserverTag());
                    }
                }
                this.loadingFileObservers.remove(str2);
            }
            this.listenerInProgress = false;
            processLaterArrays();
            checkDownloadFinished(str2, 0);
            return;
        }
        if (i == NotificationCenter.fileLoadProgressChanged) {
            this.listenerInProgress = true;
            String str3 = (String) objArr[0];
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList4 = this.loadingFileObservers.get(str3);
            if (arrayList4 != null) {
                Long l = (Long) objArr[1];
                Long l2 = (Long) objArr[2];
                int size4 = arrayList4.size();
                for (int i6 = 0; i6 < size4; i6++) {
                    WeakReference<FileDownloadProgressListener> weakReference3 = arrayList4.get(i6);
                    if (weakReference3.get() != null) {
                        weakReference3.get().onProgressDownload(str3, l.longValue(), l2.longValue());
                    }
                }
            }
            this.listenerInProgress = false;
            processLaterArrays();
            return;
        }
        if (i == NotificationCenter.fileUploadProgressChanged) {
            this.listenerInProgress = true;
            String str4 = (String) objArr[0];
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList5 = this.loadingFileObservers.get(str4);
            if (arrayList5 != null) {
                Long l3 = (Long) objArr[1];
                Long l4 = (Long) objArr[2];
                Boolean bool = (Boolean) objArr[3];
                int size5 = arrayList5.size();
                for (int i7 = 0; i7 < size5; i7++) {
                    WeakReference<FileDownloadProgressListener> weakReference4 = arrayList5.get(i7);
                    if (weakReference4.get() != null) {
                        weakReference4.get().onProgressUpload(str4, l3.longValue(), l4.longValue(), bool.booleanValue());
                    }
                }
            }
            this.listenerInProgress = false;
            processLaterArrays();
            try {
                ArrayList<SendMessagesHelper.DelayedMessage> delayedMessages = getSendMessagesHelper().getDelayedMessages(str4);
                if (delayedMessages != null) {
                    for (int i8 = 0; i8 < delayedMessages.size(); i8++) {
                        SendMessagesHelper.DelayedMessage delayedMessage = delayedMessages.get(i8);
                        if (delayedMessage.encryptedChat == null) {
                            long j = delayedMessage.peer;
                            int i9 = delayedMessage.topMessageId;
                            Long l5 = (Long) this.typingTimes.get(j);
                            if (delayedMessage.type == 4) {
                                if (l5 != null && l5.longValue() + 4000 >= System.currentTimeMillis()) {
                                }
                                MessageObject messageObject = (MessageObject) delayedMessage.extraHashMap.get(str4 + "_i");
                                if (messageObject != null && messageObject.isVideo()) {
                                    getMessagesController().sendTyping(j, i9, 5, 0);
                                } else if (messageObject == null || messageObject.getDocument() == null) {
                                    getMessagesController().sendTyping(j, i9, 4, 0);
                                } else {
                                    getMessagesController().sendTyping(j, i9, 3, 0);
                                }
                                longSparseArray = this.typingTimes;
                                currentTimeMillis = System.currentTimeMillis();
                                longSparseArray.put(j, Long.valueOf(currentTimeMillis));
                            } else {
                                delayedMessage.obj.getDocument();
                                if (l5 == null || l5.longValue() + 4000 < System.currentTimeMillis()) {
                                    if (delayedMessage.obj.isRoundVideo()) {
                                        getMessagesController().sendTyping(j, i9, 8, 0);
                                    } else if (delayedMessage.obj.isVideo()) {
                                        getMessagesController().sendTyping(j, i9, 5, 0);
                                    } else if (delayedMessage.obj.isVoice()) {
                                        getMessagesController().sendTyping(j, i9, 9, 0);
                                    } else if (delayedMessage.obj.getDocument() != null) {
                                        getMessagesController().sendTyping(j, i9, 3, 0);
                                    } else if (delayedMessage.photoSize != null) {
                                        getMessagesController().sendTyping(j, i9, 4, 0);
                                    }
                                    longSparseArray = this.typingTimes;
                                    currentTimeMillis = System.currentTimeMillis();
                                    longSparseArray.put(j, Long.valueOf(currentTimeMillis));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public int generateObserverTag() {
        int i = this.lastTag;
        this.lastTag = i + 1;
        return i;
    }

    public int getAutodownloadMask() {
        Preset currentMobilePreset;
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        if (autodownloadNetworkType == 1) {
            if (!this.wifiPreset.enabled) {
                return 0;
            }
            currentMobilePreset = getCurrentWiFiPreset();
        } else if (autodownloadNetworkType == 2) {
            if (!this.roamingPreset.enabled) {
                return 0;
            }
            currentMobilePreset = getCurrentRoamingPreset();
        } else {
            if (!this.mobilePreset.enabled) {
                return 0;
            }
            currentMobilePreset = getCurrentMobilePreset();
        }
        int[] iArr = currentMobilePreset.mask;
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            int i4 = (i3 & 1) != 0 ? 1 : 0;
            if ((i3 & 2) != 0) {
                i4 |= 2;
            }
            if ((i3 & 4) != 0) {
                i4 |= 4;
            }
            if ((i3 & 8) != 0) {
                i4 |= 8;
            }
            i |= i4 << (i2 * 8);
        }
        return i;
    }

    protected int getAutodownloadMaskAll() {
        if (!this.mobilePreset.enabled && !this.roamingPreset.enabled && !this.wifiPreset.enabled) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            if ((getCurrentMobilePreset().mask[i2] & 1) != 0 || (getCurrentWiFiPreset().mask[i2] & 1) != 0 || (getCurrentRoamingPreset().mask[i2] & 1) != 0) {
                i |= 1;
            }
            if ((getCurrentMobilePreset().mask[i2] & 2) != 0 || (getCurrentWiFiPreset().mask[i2] & 2) != 0 || (getCurrentRoamingPreset().mask[i2] & 2) != 0) {
                i |= 2;
            }
            if ((getCurrentMobilePreset().mask[i2] & 4) != 0 || (getCurrentWiFiPreset().mask[i2] & 4) != 0 || (4 & getCurrentRoamingPreset().mask[i2]) != 0) {
                i |= 4;
            }
            if ((getCurrentMobilePreset().mask[i2] & 8) != 0 || (getCurrentWiFiPreset().mask[i2] & 8) != 0 || (getCurrentRoamingPreset().mask[i2] & 8) != 0) {
                i |= 8;
            }
        }
        return i;
    }

    public int getCurrentDownloadMask() {
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        int i = 0;
        if (autodownloadNetworkType == 1) {
            if (!this.wifiPreset.enabled) {
                return 0;
            }
            int i2 = 0;
            while (i < 4) {
                i2 |= getCurrentWiFiPreset().mask[i];
                i++;
            }
            return i2;
        }
        if (autodownloadNetworkType == 2) {
            if (!this.roamingPreset.enabled) {
                return 0;
            }
            int i3 = 0;
            while (i < 4) {
                i3 |= getCurrentRoamingPreset().mask[i];
                i++;
            }
            return i3;
        }
        if (!this.mobilePreset.enabled) {
            return 0;
        }
        int i4 = 0;
        while (i < 4) {
            i4 |= getCurrentMobilePreset().mask[i];
            i++;
        }
        return i4;
    }

    public Preset getCurrentMobilePreset() {
        int i = this.currentMobilePreset;
        return i == 0 ? this.lowPreset : i == 1 ? this.mediumPreset : i == 2 ? this.highPreset : this.mobilePreset;
    }

    public Preset getCurrentRoamingPreset() {
        int i = this.currentRoamingPreset;
        return i == 0 ? this.lowPreset : i == 1 ? this.mediumPreset : i == 2 ? this.highPreset : this.roamingPreset;
    }

    public Preset getCurrentWiFiPreset() {
        int i = this.currentWifiPreset;
        return i == 0 ? this.lowPreset : i == 1 ? this.mediumPreset : i == 2 ? this.highPreset : this.wifiPreset;
    }

    public int getMaxVideoBitrate() {
        int autodownloadNetworkType = ApplicationLoader.getAutodownloadNetworkType();
        return (autodownloadNetworkType == 1 ? getCurrentWiFiPreset() : autodownloadNetworkType == 2 ? getCurrentRoamingPreset() : getCurrentMobilePreset()).maxVideoBitrate;
    }

    public boolean hasUnviewedDownloads() {
        return this.unviewedDownloads.size() > 0;
    }

    public boolean isDownloading(int i) {
        for (int i2 = 0; i2 < this.downloadingFiles.size(); i2++) {
            if (this.downloadingFiles.get(i2).messageOwner.id == i) {
                return true;
            }
        }
        return false;
    }

    public void loadAutoDownloadConfig(boolean z) {
        if (this.loadingAutoDownloadConfig) {
            return;
        }
        if (z || Math.abs(System.currentTimeMillis() - getUserConfig().autoDownloadConfigLoadTime) >= 86400000) {
            this.loadingAutoDownloadConfig = true;
            getConnectionsManager().sendRequest(new TLRPC.TL_account_getAutoDownloadSettings(), new RequestDelegate() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda7
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    DownloadController.this.lambda$loadAutoDownloadConfig$2(tLObject, tL_error);
                }
            });
        }
    }

    public void loadDownloadingFiles() {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$loadDownloadingFiles$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void newDownloadObjectsAvailable(int i) {
        int currentDownloadMask = getCurrentDownloadMask();
        if ((currentDownloadMask & 1) != 0 && (i & 1) != 0 && this.photoDownloadQueue.isEmpty()) {
            getMessagesStorage().getDownloadQueue(1);
        }
        if ((currentDownloadMask & 2) != 0 && (i & 2) != 0 && this.audioDownloadQueue.isEmpty()) {
            getMessagesStorage().getDownloadQueue(2);
        }
        if ((currentDownloadMask & 4) != 0 && (i & 4) != 0 && this.videoDownloadQueue.isEmpty()) {
            getMessagesStorage().getDownloadQueue(4);
        }
        if ((currentDownloadMask & 8) == 0 || (i & 8) == 0 || !this.documentDownloadQueue.isEmpty()) {
            return;
        }
        getMessagesStorage().getDownloadQueue(8);
    }

    public void onDownloadComplete(final MessageObject messageObject) {
        if (messageObject == null || messageObject.getDocument() == null) {
            return;
        }
        final TLRPC.Document document = messageObject.getDocument();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$onDownloadComplete$7(document, messageObject);
            }
        });
    }

    public void onDownloadFail(final MessageObject messageObject, final int i) {
        if (messageObject == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$onDownloadFail$8(messageObject, i);
            }
        });
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$onDownloadFail$9(messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008a  */
    /* JADX WARN: Type inference failed for: r8v13, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void processDownloadObjects(int i, ArrayList<DownloadObject> arrayList) {
        TLRPC.PhotoSize photoSize;
        TLRPC.PhotoSize photoSize2;
        if (arrayList.isEmpty()) {
            return;
        }
        ArrayList<DownloadObject> arrayList2 = i == 1 ? this.photoDownloadQueue : i == 2 ? this.audioDownloadQueue : i == 4 ? this.videoDownloadQueue : this.documentDownloadQueue;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            DownloadObject downloadObject = arrayList.get(i2);
            TLObject tLObject = downloadObject.object;
            TLRPC.PhotoSize photoSize3 = null;
            if (tLObject instanceof TLRPC.Document) {
                photoSize2 = FileLoader.getAttachFileName((TLRPC.Document) tLObject);
            } else if (tLObject instanceof TLRPC.Photo) {
                photoSize3 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo) tLObject).sizes, AndroidUtilities.getPhotoSize());
                photoSize2 = FileLoader.getAttachFileName(photoSize3);
            } else {
                photoSize = null;
                if (photoSize3 != null && !this.downloadQueueKeys.containsKey(photoSize3)) {
                    if (photoSize == null) {
                        getFileLoader().loadFile(ImageLocation.getForPhoto(photoSize, (TLRPC.Photo) downloadObject.object), downloadObject.parent, null, 0, downloadObject.secret ? 2 : downloadObject.forceCache ? 1 : 0);
                    } else {
                        TLObject tLObject2 = downloadObject.object;
                        if (tLObject2 instanceof TLRPC.Document) {
                            getFileLoader().loadFile((TLRPC.Document) tLObject2, downloadObject.parent, 0, downloadObject.secret ? 2 : 0);
                        }
                    }
                    arrayList2.add(downloadObject);
                    this.downloadQueueKeys.put(photoSize3, downloadObject);
                    this.downloadQueuePairs.put(new Pair<>(Long.valueOf(downloadObject.id), Integer.valueOf(downloadObject.type)), downloadObject);
                }
            }
            TLRPC.PhotoSize photoSize4 = photoSize3;
            photoSize3 = photoSize2;
            photoSize = photoSize4;
            if (photoSize3 != null) {
                if (photoSize == null) {
                }
                arrayList2.add(downloadObject);
                this.downloadQueueKeys.put(photoSize3, downloadObject);
                this.downloadQueuePairs.put(new Pair<>(Long.valueOf(downloadObject.id), Integer.valueOf(downloadObject.type)), downloadObject);
            }
        }
    }

    public void removeLoadingFileObserver(FileDownloadProgressListener fileDownloadProgressListener) {
        if (this.listenerInProgress) {
            this.deleteLaterArray.add(fileDownloadProgressListener);
            return;
        }
        String str = this.observersByTag.get(fileDownloadProgressListener.getObserverTag());
        if (str != null) {
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList = this.loadingFileObservers.get(str);
            if (arrayList != null) {
                int i = 0;
                while (i < arrayList.size()) {
                    WeakReference<FileDownloadProgressListener> weakReference = arrayList.get(i);
                    if (weakReference.get() == null || weakReference.get() == fileDownloadProgressListener) {
                        arrayList.remove(i);
                        i--;
                    }
                    i++;
                }
                if (arrayList.isEmpty()) {
                    this.loadingFileObservers.remove(str);
                }
            }
            this.observersByTag.remove(fileDownloadProgressListener.getObserverTag());
        }
    }

    public void savePresetToServer(int i) {
        Preset currentRoamingPreset;
        Preset preset;
        TLRPC.TL_account_saveAutoDownloadSettings tL_account_saveAutoDownloadSettings = new TLRPC.TL_account_saveAutoDownloadSettings();
        if (i == 0) {
            currentRoamingPreset = getCurrentMobilePreset();
            preset = this.mobilePreset;
        } else if (i == 1) {
            currentRoamingPreset = getCurrentWiFiPreset();
            preset = this.wifiPreset;
        } else {
            currentRoamingPreset = getCurrentRoamingPreset();
            preset = this.roamingPreset;
        }
        boolean z = preset.enabled;
        TLRPC.TL_autoDownloadSettings tL_autoDownloadSettings = new TLRPC.TL_autoDownloadSettings();
        tL_account_saveAutoDownloadSettings.settings = tL_autoDownloadSettings;
        tL_autoDownloadSettings.audio_preload_next = currentRoamingPreset.preloadMusic;
        tL_autoDownloadSettings.video_preload_large = currentRoamingPreset.preloadVideo;
        tL_autoDownloadSettings.phonecalls_less_data = currentRoamingPreset.lessCallData;
        tL_autoDownloadSettings.video_upload_maxbitrate = currentRoamingPreset.maxVideoBitrate;
        tL_autoDownloadSettings.disabled = !z;
        int i2 = 0;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        while (true) {
            int[] iArr = currentRoamingPreset.mask;
            if (i2 >= iArr.length) {
                break;
            }
            int i3 = iArr[i2];
            if ((i3 & 1) != 0) {
                z2 = true;
            }
            if ((i3 & 4) != 0) {
                z3 = true;
            }
            if ((i3 & 8) != 0) {
                z4 = true;
            }
            if (z2 && z3 && z4) {
                break;
            } else {
                i2++;
            }
        }
        TLRPC.TL_autoDownloadSettings tL_autoDownloadSettings2 = tL_account_saveAutoDownloadSettings.settings;
        tL_autoDownloadSettings2.photo_size_max = z2 ? (int) currentRoamingPreset.sizes[0] : 0;
        tL_autoDownloadSettings2.video_size_max = z3 ? currentRoamingPreset.sizes[1] : 0L;
        tL_autoDownloadSettings2.file_size_max = z4 ? currentRoamingPreset.sizes[2] : 0L;
        getConnectionsManager().sendRequest(tL_account_saveAutoDownloadSettings, new RequestDelegate() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                DownloadController.lambda$savePresetToServer$3(tLObject, tL_error);
            }
        });
    }

    public void startDownloadFile(TLRPC.Document document, final MessageObject messageObject) {
        final TLRPC.Document document2;
        if (messageObject == null || (document2 = messageObject.getDocument()) == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DownloadController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DownloadController.this.lambda$startDownloadFile$5(document2, messageObject);
            }
        });
    }

    public void swapLoadingPriority(MessageObject messageObject, MessageObject messageObject2) {
        int indexOf = this.downloadingFiles.indexOf(messageObject);
        int indexOf2 = this.downloadingFiles.indexOf(messageObject2);
        if (indexOf >= 0 && indexOf2 >= 0) {
            this.downloadingFiles.set(indexOf, messageObject2);
            this.downloadingFiles.set(indexOf2, messageObject);
        }
        updateFilesLoadingPriority();
    }

    public void updateFilesLoadingPriority() {
        for (int size = this.downloadingFiles.size() - 1; size >= 0; size--) {
            if (getFileLoader().isLoadingFile(this.downloadingFiles.get(size).getFileName())) {
                getFileLoader().loadFile(this.downloadingFiles.get(size).getDocument(), this.downloadingFiles.get(size), 2, 0);
            }
        }
    }
}
