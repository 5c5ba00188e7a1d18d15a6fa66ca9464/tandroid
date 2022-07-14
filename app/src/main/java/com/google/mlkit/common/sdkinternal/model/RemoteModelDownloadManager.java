package com.google.mlkit.common.sdkinternal.model;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.LongSparseArray;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.mlkit_common.zzdx;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModel;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.ModelInfo;
import com.google.mlkit.common.sdkinternal.ModelType;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public class RemoteModelDownloadManager {
    private static final GmsLogger zza = new GmsLogger("ModelDownloadManager", "");
    private static final Map<String, RemoteModelDownloadManager> zzb = new HashMap();
    private final LongSparseArray<zza> zzc = new LongSparseArray<>();
    private final LongSparseArray<TaskCompletionSource<Void>> zzd = new LongSparseArray<>();
    private final MlKitContext zze;
    private final DownloadManager zzf;
    private final RemoteModel zzg;
    private final ModelType zzh;
    private final zzdx zzi;
    private final SharedPrefManager zzj;
    private final ModelFileHelper zzk;
    private final ModelInfoRetrieverInterop zzl;
    private final RemoteModelFileManager zzm;
    private DownloadConditions zzn;

    public static synchronized RemoteModelDownloadManager getInstance(MlKitContext mlKitContext, RemoteModel remoteModel, ModelFileHelper modelFileHelper, RemoteModelFileManager remoteModelFileManager, ModelInfoRetrieverInterop modelInfoRetrieverInterop) {
        RemoteModelDownloadManager remoteModelDownloadManager;
        synchronized (RemoteModelDownloadManager.class) {
            String uniqueModelNameForPersist = remoteModel.getUniqueModelNameForPersist();
            Map<String, RemoteModelDownloadManager> map = zzb;
            if (!map.containsKey(uniqueModelNameForPersist)) {
                map.put(uniqueModelNameForPersist, new RemoteModelDownloadManager(mlKitContext, remoteModel, modelFileHelper, remoteModelFileManager, modelInfoRetrieverInterop));
            }
            remoteModelDownloadManager = map.get(uniqueModelNameForPersist);
        }
        return remoteModelDownloadManager;
    }

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes3.dex */
    public class zza extends BroadcastReceiver {
        private final long zza;
        private final TaskCompletionSource<Void> zzb;

        private zza(long j, TaskCompletionSource<Void> taskCompletionSource) {
            RemoteModelDownloadManager.this = r1;
            this.zza = j;
            this.zzb = taskCompletionSource;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            long longExtra = intent.getLongExtra("extra_download_id", -1L);
            if (longExtra != this.zza) {
                return;
            }
            Integer downloadingModelStatusCode = RemoteModelDownloadManager.this.getDownloadingModelStatusCode();
            synchronized (RemoteModelDownloadManager.this) {
                try {
                    RemoteModelDownloadManager.this.zze.getApplicationContext().unregisterReceiver(this);
                } catch (IllegalArgumentException e) {
                    RemoteModelDownloadManager.zza.w("ModelDownloadManager", "Exception thrown while trying to unregister the broadcast receiver for the download", e);
                }
                RemoteModelDownloadManager.this.zzc.remove(this.zza);
                RemoteModelDownloadManager.this.zzd.remove(this.zza);
            }
            if (downloadingModelStatusCode != null) {
                if (downloadingModelStatusCode.intValue() == 16) {
                    RemoteModelDownloadManager.this.zzi.zza(false, RemoteModelDownloadManager.this.zzg.getModelType(), RemoteModelDownloadManager.this.getFailureReason(Long.valueOf(longExtra)));
                    this.zzb.setException(RemoteModelDownloadManager.this.zza(Long.valueOf(longExtra)));
                    return;
                } else if (downloadingModelStatusCode.intValue() == 8) {
                    RemoteModelDownloadManager.this.zzi.zza(0, RemoteModelDownloadManager.this.zzg.getModelType(), 6);
                    this.zzb.setResult(null);
                    return;
                }
            }
            RemoteModelDownloadManager.this.zzi.zza(false, RemoteModelDownloadManager.this.zzg.getModelType(), 0);
            this.zzb.setException(new MlKitException("Model downloading failed", 13));
        }
    }

    public void setDownloadConditions(DownloadConditions downloadConditions) {
        Preconditions.checkNotNull(downloadConditions, "DownloadConditions can not be null");
        this.zzn = downloadConditions;
    }

    private RemoteModelDownloadManager(MlKitContext mlKitContext, RemoteModel remoteModel, ModelFileHelper modelFileHelper, RemoteModelFileManager remoteModelFileManager, ModelInfoRetrieverInterop modelInfoRetrieverInterop) {
        this.zze = mlKitContext;
        this.zzh = remoteModel.getModelType();
        this.zzg = remoteModel;
        DownloadManager downloadManager = (DownloadManager) mlKitContext.getApplicationContext().getSystemService("download");
        this.zzf = downloadManager;
        if (downloadManager == null) {
            zza.d("ModelDownloadManager", "Download manager service is not available in the service.");
        }
        this.zzk = modelFileHelper;
        this.zzi = ((zzdx.zza) mlKitContext.get(zzdx.zza.class)).get(remoteModel);
        this.zzj = SharedPrefManager.getInstance(mlKitContext);
        this.zzl = modelInfoRetrieverInterop;
        this.zzm = remoteModelFileManager;
    }

    public synchronized Long getDownloadingId() {
        return this.zzj.getDownloadingModelId(this.zzg);
    }

    public synchronized String getDownloadingModelHash() {
        return this.zzj.getDownloadingModelHash(this.zzg);
    }

    public synchronized void removeOrCancelDownload() throws MlKitException {
        Long downloadingId = getDownloadingId();
        if (this.zzf != null && downloadingId != null) {
            GmsLogger gmsLogger = zza;
            String valueOf = String.valueOf(downloadingId);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 44);
            sb.append("Cancel or remove existing downloading task: ");
            sb.append(valueOf);
            gmsLogger.d("ModelDownloadManager", sb.toString());
            if (this.zzf.remove(downloadingId.longValue()) > 0 || getDownloadingModelStatusCode() == null) {
                this.zzk.deleteTempFilesInPrivateFolder(this.zzg.getUniqueModelNameForPersist(), this.zzg.getModelType());
                this.zzj.clearDownloadingModelInfo(this.zzg);
            }
        }
    }

    private final synchronized Long zza(DownloadManager.Request request, ModelInfo modelInfo) {
        DownloadManager downloadManager = this.zzf;
        if (downloadManager == null) {
            return null;
        }
        long enqueue = downloadManager.enqueue(request);
        GmsLogger gmsLogger = zza;
        StringBuilder sb = new StringBuilder(53);
        sb.append("Schedule a new downloading task: ");
        sb.append(enqueue);
        gmsLogger.d("ModelDownloadManager", sb.toString());
        this.zzj.setDownloadingModelInfo(enqueue, modelInfo);
        this.zzi.zza(0, false, modelInfo.getModelType(), 4);
        return Long.valueOf(enqueue);
    }

    public synchronized Integer getDownloadingModelStatusCode() {
        Integer num;
        Long downloadingId = getDownloadingId();
        DownloadManager downloadManager = this.zzf;
        Integer num2 = null;
        if (downloadManager != null && downloadingId != null) {
            Cursor query = downloadManager.query(new DownloadManager.Query().setFilterById(downloadingId.longValue()));
            if (query != null && query.moveToFirst()) {
                num = Integer.valueOf(query.getInt(query.getColumnIndex(NotificationCompat.CATEGORY_STATUS)));
            } else {
                num = null;
            }
            if (num == null) {
                if (query != null) {
                    query.close();
                }
                return null;
            }
            if (num.intValue() == 2 || num.intValue() == 4 || num.intValue() == 1 || num.intValue() == 8 || num.intValue() == 16) {
                num2 = num;
            }
            if (query != null) {
                query.close();
            }
            return num2;
        }
        return null;
    }

    public synchronized ParcelFileDescriptor getDownloadedFile() {
        Long downloadingId = getDownloadingId();
        DownloadManager downloadManager = this.zzf;
        ParcelFileDescriptor parcelFileDescriptor = null;
        if (downloadManager == null || downloadingId == null) {
            return null;
        }
        try {
            parcelFileDescriptor = downloadManager.openDownloadedFile(downloadingId.longValue());
        } catch (FileNotFoundException e) {
            zza.e("ModelDownloadManager", "Downloaded file is not found");
        }
        return parcelFileDescriptor;
    }

    public synchronized void updateLatestModelHashAndType(String str) throws MlKitException {
        this.zzj.setLatestModelHash(this.zzg, str);
        removeOrCancelDownload();
    }

    private final synchronized ModelInfo zzb() throws MlKitException {
        boolean modelExistsLocally = modelExistsLocally();
        boolean z = false;
        if (modelExistsLocally) {
            this.zzi.zza(0, false, this.zzg.getModelType(), 8);
        }
        ModelInfoRetrieverInterop modelInfoRetrieverInterop = this.zzl;
        if (modelInfoRetrieverInterop != null) {
            ModelInfo retrieveRemoteModelInfo = modelInfoRetrieverInterop.retrieveRemoteModelInfo(this.zzg);
            if (retrieveRemoteModelInfo == null) {
                return null;
            }
            MlKitContext mlKitContext = this.zze;
            RemoteModel remoteModel = this.zzg;
            String modelHash = retrieveRemoteModelInfo.getModelHash();
            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(mlKitContext);
            if (modelHash.equals(sharedPrefManager.getIncompatibleModelHash(remoteModel)) && CommonUtils.getAppVersion(mlKitContext.getApplicationContext()).equals(sharedPrefManager.getPreviousAppVersion())) {
                zza.e("ModelDownloadManager", "The model is incompatible with TFLite and the app is not upgraded, do not download");
            } else {
                z = true;
            }
            if (!modelExistsLocally) {
                this.zzj.clearLatestModelHash(this.zzg);
            }
            boolean z2 = !retrieveRemoteModelInfo.getModelHash().equals(SharedPrefManager.getInstance(this.zze).getLatestModelHash(this.zzg));
            if (z && (!modelExistsLocally || z2)) {
                return retrieveRemoteModelInfo;
            }
            if (modelExistsLocally && (z2 ^ z)) {
                return null;
            }
            String modelName = this.zzg.getModelName();
            StringBuilder sb = new StringBuilder(String.valueOf(modelName).length() + 46);
            sb.append("The model ");
            sb.append(modelName);
            sb.append(" is incompatible with TFLite runtime");
            throw new MlKitException(sb.toString(), 100);
        }
        throw new MlKitException("Please include com.google.mlkit:linkfirebase sdk as your dependency when you try to download from Firebase.", 14);
    }

    private final synchronized Long zza(ModelInfo modelInfo, DownloadConditions downloadConditions) throws MlKitException {
        Preconditions.checkNotNull(downloadConditions, "DownloadConditions can not be null");
        String downloadingModelHash = this.zzj.getDownloadingModelHash(this.zzg);
        Integer downloadingModelStatusCode = getDownloadingModelStatusCode();
        if (downloadingModelHash != null && downloadingModelHash.equals(modelInfo.getModelHash()) && downloadingModelStatusCode != null) {
            Integer downloadingModelStatusCode2 = getDownloadingModelStatusCode();
            if (!(downloadingModelStatusCode2 != null && (downloadingModelStatusCode2.intValue() == 8 || downloadingModelStatusCode2.intValue() == 16))) {
                this.zzi.zza(0, false, this.zzg.getModelType(), 5);
            }
            zza.d("ModelDownloadManager", "New model is already in downloading, do nothing.");
            return null;
        }
        GmsLogger gmsLogger = zza;
        gmsLogger.d("ModelDownloadManager", "Need to download a new model.");
        removeOrCancelDownload();
        DownloadManager.Request request = new DownloadManager.Request(modelInfo.getModelUri());
        request.setDestinationUri(null);
        if (this.zzk.modelExistsLocally(modelInfo.getModelNameForPersist(), modelInfo.getModelType())) {
            gmsLogger.d("ModelDownloadManager", "Model update is enabled and have a previous downloaded model, use download condition");
            this.zzi.zza(0, false, modelInfo.getModelType(), 9);
        }
        if (Build.VERSION.SDK_INT >= 24) {
            request.setRequiresCharging(downloadConditions.isChargingRequired());
        }
        if (downloadConditions.isWifiRequired()) {
            request.setAllowedNetworkTypes(2);
        }
        return zza(request, modelInfo);
    }

    public boolean isModelDownloadedAndValid() throws MlKitException {
        try {
            if (modelExistsLocally()) {
                return true;
            }
        } catch (MlKitException e) {
            zza.d("ModelDownloadManager", "Failed to check if the model exist locally.");
        }
        Long downloadingId = getDownloadingId();
        String downloadingModelHash = getDownloadingModelHash();
        if (downloadingId == null || downloadingModelHash == null) {
            zza.d("ModelDownloadManager", "No new model is downloading.");
            removeOrCancelDownload();
            return false;
        }
        Integer downloadingModelStatusCode = getDownloadingModelStatusCode();
        GmsLogger gmsLogger = zza;
        String valueOf = String.valueOf(downloadingModelStatusCode);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
        sb.append("Download Status code: ");
        sb.append(valueOf);
        gmsLogger.d("ModelDownloadManager", sb.toString());
        if (downloadingModelStatusCode != null) {
            return Objects.equal(downloadingModelStatusCode, 8) && zza(downloadingModelHash) != null;
        }
        removeOrCancelDownload();
        return false;
    }

    public final File zza(String str) throws MlKitException {
        GmsLogger gmsLogger = zza;
        gmsLogger.d("ModelDownloadManager", "Model downloaded successfully");
        this.zzi.zza(0, true, this.zzh, 6);
        ParcelFileDescriptor downloadedFile = getDownloadedFile();
        if (downloadedFile == null) {
            removeOrCancelDownload();
            return null;
        }
        gmsLogger.d("ModelDownloadManager", "moving downloaded model from external storage to private folder.");
        try {
            return this.zzm.moveModelToPrivateFolder(downloadedFile, str, this.zzg);
        } finally {
            removeOrCancelDownload();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00a1, code lost:
        r1 = zza(r1, r10.zzn);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00a7, code lost:
        if (r1 == null) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b1, code lost:
        return zzc(r1.longValue());
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b2, code lost:
        com.google.mlkit.common.sdkinternal.model.RemoteModelDownloadManager.zza.i("ModelDownloadManager", "Didn't schedule download for the updated model");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Task<Void> ensureModelDownloaded() {
        MlKitException mlKitException;
        ModelInfo modelInfo;
        boolean z = true;
        this.zzi.zza(0, false, ModelType.UNKNOWN, 1);
        Long l = null;
        try {
            modelInfo = zzb();
            mlKitException = null;
        } catch (MlKitException e) {
            mlKitException = e;
            modelInfo = null;
        }
        try {
            Integer downloadingModelStatusCode = getDownloadingModelStatusCode();
            Long downloadingId = getDownloadingId();
            if (!modelExistsLocally() && (downloadingModelStatusCode == null || downloadingModelStatusCode.intValue() != 8)) {
                if (downloadingModelStatusCode != null && downloadingModelStatusCode.intValue() == 16) {
                    MlKitException zza2 = zza(downloadingId);
                    removeOrCancelDownload();
                    return Tasks.forException(zza2);
                }
                if (downloadingModelStatusCode == null || (downloadingModelStatusCode.intValue() != 4 && downloadingModelStatusCode.intValue() != 2 && downloadingModelStatusCode.intValue() != 1)) {
                    z = false;
                }
                if (z && downloadingId != null && getDownloadingModelHash() != null) {
                    this.zzi.zza(0, false, this.zzg.getModelType(), 5);
                    return zzc(downloadingId.longValue());
                }
                if (modelInfo != null) {
                    l = zza(modelInfo, this.zzn);
                }
                if (l == null) {
                    return Tasks.forException(new MlKitException("Failed to schedule the download task", 13, mlKitException));
                }
                return zzc(l.longValue());
            }
            return Tasks.forResult(null);
        } catch (MlKitException e2) {
            return Tasks.forException(new MlKitException("Failed to ensure the model is downloaded.", 13, e2));
        }
    }

    public int getFailureReason(Long l) {
        int columnIndex;
        DownloadManager downloadManager = this.zzf;
        Cursor query = (downloadManager == null || l == null) ? null : downloadManager.query(new DownloadManager.Query().setFilterById(l.longValue()));
        if (query == null || !query.moveToFirst() || (columnIndex = query.getColumnIndex("reason")) == -1) {
            return 0;
        }
        return query.getInt(columnIndex);
    }

    public boolean modelExistsLocally() throws MlKitException {
        return this.zzk.modelExistsLocally(this.zzg.getUniqueModelNameForPersist(), this.zzh);
    }

    private final synchronized zza zza(long j) {
        zza zzaVar;
        zzaVar = this.zzc.get(j);
        if (zzaVar == null) {
            zzaVar = new zza(j, zzb(j));
            this.zzc.put(j, zzaVar);
        }
        return zzaVar;
    }

    private final synchronized TaskCompletionSource<Void> zzb(long j) {
        TaskCompletionSource<Void> taskCompletionSource;
        taskCompletionSource = this.zzd.get(j);
        if (taskCompletionSource == null) {
            taskCompletionSource = new TaskCompletionSource<>();
            this.zzd.put(j, taskCompletionSource);
        }
        return taskCompletionSource;
    }

    public final MlKitException zza(Long l) {
        String str;
        DownloadManager downloadManager = this.zzf;
        Cursor query = (downloadManager == null || l == null) ? null : downloadManager.query(new DownloadManager.Query().setFilterById(l.longValue()));
        int i = 13;
        if (query != null && query.moveToFirst()) {
            int i2 = query.getInt(query.getColumnIndex("reason"));
            if (i2 == 1006) {
                i = 101;
                str = "Model downloading failed due to insufficient space on the device.";
            } else {
                StringBuilder sb = new StringBuilder(84);
                sb.append("Model downloading failed due to error code: ");
                sb.append(i2);
                sb.append(" from Android DownloadManager");
                str = sb.toString();
            }
        } else {
            str = "Model downloading failed";
        }
        return new MlKitException(str, i);
    }

    private final Task<Void> zzc(long j) {
        this.zze.getApplicationContext().registerReceiver(zza(j), new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"), null, MLTaskExecutor.getInstance().getHandler());
        return zzb(j).getTask();
    }
}
