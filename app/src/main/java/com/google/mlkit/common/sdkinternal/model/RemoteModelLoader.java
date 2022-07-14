package com.google.mlkit.common.sdkinternal.model;

import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.internal.mlkit_common.zzdx;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.RemoteModel;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.ModelType;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public class RemoteModelLoader {
    private static final GmsLogger zza = new GmsLogger("RemoteModelLoader", "");
    private static final Map<String, RemoteModelLoader> zzb = new HashMap();
    private final MlKitContext zzc;
    private final RemoteModel zzd;
    private final RemoteModelDownloadManager zze;
    private final RemoteModelFileManager zzf;
    private final zzdx zzg;
    private final RemoteModelLoaderHelper zzh;
    private final ModelType zzi;
    private boolean zzj = true;

    private RemoteModelLoader(MlKitContext mlKitContext, RemoteModel remoteModel, ModelValidator modelValidator, RemoteModelLoaderHelper remoteModelLoaderHelper, RemoteModelFileMover remoteModelFileMover) {
        RemoteModelFileManager remoteModelFileManager = new RemoteModelFileManager(mlKitContext, remoteModel, modelValidator, new ModelFileHelper(mlKitContext), remoteModelFileMover);
        this.zzf = remoteModelFileManager;
        this.zzg = ((zzdx.zza) MlKitContext.getInstance().get(zzdx.zza.class)).get(remoteModel);
        this.zze = RemoteModelDownloadManager.getInstance(mlKitContext, remoteModel, new ModelFileHelper(mlKitContext), remoteModelFileManager, (ModelInfoRetrieverInterop) mlKitContext.get(ModelInfoRetrieverInterop.class));
        this.zzh = remoteModelLoaderHelper;
        this.zzc = mlKitContext;
        this.zzd = remoteModel;
        this.zzi = remoteModel.getModelType();
    }

    public static synchronized RemoteModelLoader getInstance(MlKitContext mlKitContext, RemoteModel remoteModel, ModelValidator modelValidator, RemoteModelLoaderHelper remoteModelLoaderHelper, RemoteModelFileMover remoteModelFileMover) {
        RemoteModelLoader remoteModelLoader;
        synchronized (RemoteModelLoader.class) {
            String uniqueModelNameForPersist = remoteModel.getUniqueModelNameForPersist();
            Map<String, RemoteModelLoader> map = zzb;
            if (!map.containsKey(uniqueModelNameForPersist)) {
                map.put(uniqueModelNameForPersist, new RemoteModelLoader(mlKitContext, remoteModel, modelValidator, remoteModelLoaderHelper, remoteModelFileMover));
            }
            remoteModelLoader = map.get(uniqueModelNameForPersist);
        }
        return remoteModelLoader;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00d6 A[Catch: all -> 0x00e3, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:7:0x001e, B:9:0x0027, B:10:0x002e, B:12:0x005a, B:14:0x0062, B:16:0x0078, B:17:0x007d, B:18:0x0083, B:20:0x008d, B:22:0x0095, B:24:0x00a7, B:26:0x00af, B:27:0x00c4, B:30:0x00d6), top: B:35:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized MappedByteBuffer load() throws MlKitException {
        MappedByteBuffer mappedByteBuffer;
        GmsLogger gmsLogger = zza;
        gmsLogger.d("RemoteModelLoader", "Try to load newly downloaded model file.");
        boolean z = this.zzj;
        Long downloadingId = this.zze.getDownloadingId();
        String downloadingModelHash = this.zze.getDownloadingModelHash();
        if (downloadingId != null && downloadingModelHash != null) {
            Integer downloadingModelStatusCode = this.zze.getDownloadingModelStatusCode();
            if (downloadingModelStatusCode == null) {
                this.zze.removeOrCancelDownload();
            } else {
                String valueOf = String.valueOf(downloadingModelStatusCode);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
                sb.append("Download Status code: ");
                sb.append(valueOf);
                gmsLogger.d("RemoteModelLoader", sb.toString());
                if (downloadingModelStatusCode.intValue() == 8) {
                    File zza2 = this.zze.zza(downloadingModelHash);
                    if (zza2 != null) {
                        mappedByteBuffer = zza(zza2);
                        String valueOf2 = String.valueOf(zza2.getParent());
                        gmsLogger.d("RemoteModelLoader", valueOf2.length() != 0 ? "Moved the downloaded model to private folder successfully: ".concat(valueOf2) : new String("Moved the downloaded model to private folder successfully: "));
                        this.zze.updateLatestModelHashAndType(downloadingModelHash);
                        if (z && this.zzf.zza(zza2)) {
                            gmsLogger.d("RemoteModelLoader", "All old models are deleted.");
                            mappedByteBuffer = zza(this.zzf.zzc(zza2));
                        }
                        if (mappedByteBuffer == null) {
                            gmsLogger.d("RemoteModelLoader", "Loading existing model file.");
                            mappedByteBuffer = zza();
                        }
                    }
                } else if (downloadingModelStatusCode.intValue() == 16) {
                    this.zzg.zza(false, this.zzi, this.zze.getFailureReason(downloadingId));
                    this.zze.removeOrCancelDownload();
                }
            }
            mappedByteBuffer = null;
            if (mappedByteBuffer == null) {
            }
        }
        gmsLogger.d("RemoteModelLoader", "No new model is downloading.");
        this.zze.removeOrCancelDownload();
        mappedByteBuffer = null;
        if (mappedByteBuffer == null) {
        }
        return mappedByteBuffer;
    }

    public RemoteModel getRemoteModel() {
        return this.zzd;
    }

    private final MappedByteBuffer zza(File file) throws MlKitException {
        try {
            return zza(file.getAbsolutePath());
        } catch (Exception e) {
            this.zzf.zzb(file);
            throw new MlKitException("Failed to load newly downloaded model.", 14, e);
        }
    }

    private final MappedByteBuffer zza() throws MlKitException {
        String zza2 = this.zzf.zza();
        if (zza2 == null) {
            zza.d("RemoteModelLoader", "No existing model file");
            return null;
        }
        try {
            return zza(zza2);
        } catch (Exception e) {
            this.zzf.zzb(new File(zza2));
            SharedPrefManager.getInstance(this.zzc).clearLatestModelHash(this.zzd);
            throw new MlKitException("Failed to load an already downloaded model.", 14, e);
        }
    }

    private final MappedByteBuffer zza(String str) throws MlKitException {
        return this.zzh.loadModelAtPath(str);
    }
}
