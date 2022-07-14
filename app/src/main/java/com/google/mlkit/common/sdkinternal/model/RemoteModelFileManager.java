package com.google.mlkit.common.sdkinternal.model;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.internal.mlkit_common.zzan;
import com.google.android.gms.internal.mlkit_common.zzdx;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.internal.model.ModelUtils;
import com.google.mlkit.common.model.RemoteModel;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.ModelType;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import com.google.mlkit.common.sdkinternal.model.ModelValidator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public class RemoteModelFileManager {
    private static final GmsLogger zza = new GmsLogger("RemoteModelFileManager", "");
    private final MlKitContext zzb;
    private final String zzc;
    private final ModelType zzd;
    private final ModelValidator zze;
    private final RemoteModelFileMover zzf;
    private final SharedPrefManager zzg;
    private final ModelFileHelper zzh;

    public RemoteModelFileManager(MlKitContext mlKitContext, RemoteModel remoteModel, ModelValidator modelValidator, ModelFileHelper modelFileHelper, RemoteModelFileMover remoteModelFileMover) {
        String str;
        this.zzb = mlKitContext;
        ModelType modelType = remoteModel.getModelType();
        this.zzd = modelType;
        if (modelType == ModelType.TRANSLATE) {
            str = remoteModel.getModelNameForBackend();
        } else {
            str = remoteModel.getUniqueModelNameForPersist();
        }
        this.zzc = str;
        this.zze = modelValidator;
        this.zzg = SharedPrefManager.getInstance(mlKitContext);
        this.zzh = modelFileHelper;
        this.zzf = remoteModelFileMover;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00be, code lost:
        r9 = com.google.mlkit.common.sdkinternal.model.RemoteModelFileManager.zza;
        r10 = java.lang.String.valueOf(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00cc, code lost:
        if (r10.length() == 0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ce, code lost:
        r10 = "Hash does not match with expected: ".concat(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00d3, code lost:
        r10 = new java.lang.String("Hash does not match with expected: ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d8, code lost:
        r9.d("RemoteModelFileManager", r10);
        r0.zza(25, true, r8.zzd, 6);
        r9 = new com.google.mlkit.common.MlKitException("Hash does not match with expected", 102);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized File moveModelToPrivateFolder(ParcelFileDescriptor parcelFileDescriptor, String str, RemoteModel remoteModel) throws MlKitException {
        File file;
        boolean z;
        zzdx zzdxVar = ((zzdx.zza) this.zzb.get(zzdx.zza.class)).get(remoteModel);
        file = new File(this.zzh.zzb(this.zzc, this.zzd), "to_be_validated_model.tmp");
        try {
            ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = autoCloseInputStream.read(bArr);
                    z = false;
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.getFD().sync();
                fileOutputStream.close();
                autoCloseInputStream.close();
                boolean zza2 = ModelUtils.zza(file, str);
                if (zza2) {
                    ModelValidator.ValidationResult validateModel = this.zze.validateModel(file, remoteModel);
                    z = validateModel.isValid();
                    if (validateModel.getErrorCode().equals(ModelValidator.ValidationResult.ErrorCode.TFLITE_VERSION_INCOMPATIBLE)) {
                        String appVersion = CommonUtils.getAppVersion(this.zzb.getApplicationContext());
                        this.zzg.setIncompatibleModelInfo(remoteModel, str, appVersion);
                        GmsLogger gmsLogger = zza;
                        String valueOf = String.valueOf(str);
                        gmsLogger.d("RemoteModelFileManager", valueOf.length() != 0 ? "Model is not compatible. Model hash: ".concat(valueOf) : new String("Model is not compatible. Model hash: "));
                        String valueOf2 = String.valueOf(appVersion);
                        gmsLogger.d("RemoteModelFileManager", valueOf2.length() != 0 ? "The current app version is: ".concat(valueOf2) : new String("The current app version is: "));
                    }
                }
                if (zza2 && z) {
                }
                MlKitException mlKitException = new MlKitException("Model is not compatible with TFLite run time", 100);
                if (!file.delete()) {
                    GmsLogger gmsLogger2 = zza;
                    String valueOf3 = String.valueOf(file.getAbsolutePath());
                    gmsLogger2.d("RemoteModelFileManager", valueOf3.length() != 0 ? "Failed to delete the temp file: ".concat(valueOf3) : new String("Failed to delete the temp file: "));
                }
                throw mlKitException;
            } catch (Throwable th) {
                try {
                    autoCloseInputStream.close();
                } catch (Throwable th2) {
                    zzan.zza(th, th2);
                }
                throw th;
            }
        } catch (IOException e) {
            GmsLogger gmsLogger3 = zza;
            String valueOf4 = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf4).length() + 56);
            sb.append("Failed to copy downloaded model file to private folder: ");
            sb.append(valueOf4);
            gmsLogger3.e("RemoteModelFileManager", sb.toString());
            return null;
        }
        return this.zzf.moveAllFilesFromPrivateTempToPrivateDestination(file);
    }

    public final synchronized boolean zza(File file) throws MlKitException {
        File[] listFiles;
        File modelDir = this.zzh.getModelDir(this.zzc, this.zzd);
        if (!modelDir.exists()) {
            return false;
        }
        boolean z = true;
        for (File file2 : modelDir.listFiles()) {
            if (!file2.equals(file) && !this.zzh.deleteRecursively(file2)) {
                z = false;
            }
        }
        return z;
    }

    public final synchronized void zzb(File file) {
        File modelDirUnsafe = getModelDirUnsafe(false);
        if (!modelDirUnsafe.exists()) {
            return;
        }
        for (File file2 : modelDirUnsafe.listFiles()) {
            if (file2.equals(file)) {
                this.zzh.deleteRecursively(file);
                return;
            }
        }
    }

    public final synchronized File zzc(File file) throws MlKitException {
        File file2 = new File(String.valueOf(this.zzh.getModelDir(this.zzc, this.zzd).getAbsolutePath()).concat("/0"));
        return file2.exists() ? file : file.renameTo(file2) ? file2 : file;
    }

    public final synchronized String zza() throws MlKitException {
        return this.zzh.zza(this.zzc, this.zzd);
    }

    public File getModelDirUnsafe(boolean z) {
        return this.zzh.getModelDirUnsafe(this.zzc, this.zzd, z);
    }
}
