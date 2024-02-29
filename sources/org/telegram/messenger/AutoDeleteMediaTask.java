package org.telegram.messenger;

import android.util.SparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes3.dex */
public class AutoDeleteMediaTask {
    public static Set<String> usingFilePaths = Collections.newSetFromMap(new ConcurrentHashMap());

    public static void run() {
        final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (Math.abs(currentTimeMillis - SharedConfig.lastKeepMediaCheckTime) < 86400) {
            return;
        }
        SharedConfig.lastKeepMediaCheckTime = currentTimeMillis;
        final File checkDirectory = FileLoader.checkDirectory(4);
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AutoDeleteMediaTask.lambda$run$1(currentTimeMillis, checkDirectory);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02db  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x018d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:198:0x01df A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:208:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$run$1(int i, File file) {
        int i2;
        int i3;
        long j;
        int i4;
        int i5;
        File file2;
        int[] iArr;
        long j2;
        boolean z;
        long j3;
        long j4;
        ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList;
        boolean z2;
        long currentTimeMillis = System.currentTimeMillis();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("checkKeepMedia start task");
        }
        ArrayList arrayList2 = new ArrayList();
        int i6 = 0;
        boolean z3 = false;
        while (true) {
            i2 = 4;
            i3 = 1;
            if (i6 >= 4) {
                break;
            }
            if (UserConfig.getInstance(i6).isClientActivated()) {
                CacheByChatsController cacheByChatsController = UserConfig.getInstance(i6).getMessagesController().getCacheByChatsController();
                arrayList2.add(cacheByChatsController);
                if (cacheByChatsController.getKeepMediaExceptionsByDialogs().size() > 0) {
                    z3 = true;
                }
            }
            i6++;
        }
        int[] iArr2 = new int[4];
        long j5 = Long.MAX_VALUE;
        boolean z4 = true;
        for (int i7 = 0; i7 < 4; i7++) {
            iArr2[i7] = SharedConfig.getPreferences().getInt("keep_media_type_" + i7, CacheByChatsController.getDefault(i7));
            if (iArr2[i7] != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                z4 = false;
            }
            long daysInSeconds = CacheByChatsController.getDaysInSeconds(iArr2[i7]);
            if (daysInSeconds < j5) {
                j5 = daysInSeconds;
            }
        }
        if (z3) {
            z4 = false;
        }
        SparseArray<File> createMediaPaths = ImageLoader.getInstance().createMediaPaths();
        int i8 = 0;
        int i9 = 0;
        long j6 = 0;
        while (i8 < createMediaPaths.size()) {
            if (z4 && (createMediaPaths.keyAt(i8) == i3 || createMediaPaths.keyAt(i8) == 3)) {
                iArr = iArr2;
                j2 = j5;
            } else {
                boolean z5 = createMediaPaths.keyAt(i8) == i2;
                try {
                    File[] listFiles = createMediaPaths.valueAt(i8).listFiles();
                    ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList3 = new ArrayList<>();
                    if (listFiles != null) {
                        int i10 = 0;
                        while (i10 < listFiles.length) {
                            try {
                                if (listFiles[i10].isDirectory()) {
                                    j2 = j5;
                                } else {
                                    j2 = j5;
                                    try {
                                        if (!usingFilePaths.contains(listFiles[i10].getAbsolutePath())) {
                                            arrayList3.add(new CacheByChatsController.KeepMediaFile(listFiles[i10]));
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        iArr = iArr2;
                                        FileLog.e(th);
                                        i8++;
                                        j5 = j2;
                                        iArr2 = iArr;
                                        i2 = 4;
                                        i3 = 1;
                                    }
                                }
                                i10++;
                                j5 = j2;
                            } catch (Throwable th2) {
                                th = th2;
                                j2 = j5;
                            }
                        }
                    }
                    j2 = j5;
                    for (int i11 = 0; i11 < arrayList2.size(); i11++) {
                        ((CacheByChatsController) arrayList2.get(i11)).lookupFiles(arrayList3);
                    }
                    int i12 = 0;
                    while (i12 < arrayList3.size()) {
                        CacheByChatsController.KeepMediaFile keepMediaFile = (CacheByChatsController.KeepMediaFile) arrayList3.get(i12);
                        if (keepMediaFile.isStory) {
                            z = z5;
                            j4 = i - CacheByChatsController.getDaysInSeconds(iArr2[3]);
                        } else {
                            z = z5;
                            int i13 = keepMediaFile.keepMedia;
                            if (i13 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                                if (i13 >= 0) {
                                    j3 = CacheByChatsController.getDaysInSeconds(i13);
                                } else {
                                    int i14 = keepMediaFile.dialogType;
                                    if (i14 >= 0) {
                                        j3 = CacheByChatsController.getDaysInSeconds(iArr2[i14]);
                                    } else if (!z) {
                                        j3 = j2;
                                    }
                                }
                                if (j3 != Long.MAX_VALUE) {
                                    j4 = i - j3;
                                }
                            }
                            arrayList = arrayList3;
                            iArr = iArr2;
                            i12++;
                            z5 = z;
                            arrayList3 = arrayList;
                            iArr2 = iArr;
                        }
                        long lastUsageFileTime = Utilities.getLastUsageFileTime(keepMediaFile.file.getAbsolutePath());
                        if (lastUsageFileTime <= 316000000 || lastUsageFileTime >= j4) {
                            arrayList = arrayList3;
                            iArr = iArr2;
                        } else {
                            arrayList = arrayList3;
                            iArr = iArr2;
                            try {
                                if (!usingFilePaths.contains(keepMediaFile.file.getPath())) {
                                    z2 = true;
                                    if (!z2) {
                                        try {
                                            if (BuildVars.LOGS_ENABLED) {
                                                i9++;
                                                j6 += keepMediaFile.file.length();
                                            }
                                            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                                                FileLog.d("delete file " + keepMediaFile.file.getPath() + " last_usage_time=" + lastUsageFileTime + " time_local=" + j4 + " story=" + keepMediaFile.isStory);
                                            }
                                            keepMediaFile.file.delete();
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                    i12++;
                                    z5 = z;
                                    arrayList3 = arrayList;
                                    iArr2 = iArr;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                FileLog.e(th);
                                i8++;
                                j5 = j2;
                                iArr2 = iArr;
                                i2 = 4;
                                i3 = 1;
                            }
                        }
                        z2 = false;
                        if (!z2) {
                        }
                        i12++;
                        z5 = z;
                        arrayList3 = arrayList;
                        iArr2 = iArr;
                    }
                    iArr = iArr2;
                } catch (Throwable th4) {
                    th = th4;
                    iArr = iArr2;
                    j2 = j5;
                }
            }
            i8++;
            j5 = j2;
            iArr2 = iArr;
            i2 = 4;
            i3 = 1;
        }
        int i15 = SharedConfig.getPreferences().getInt("cache_limit", ConnectionsManager.DEFAULT_DATACENTER_ID);
        if (i15 != Integer.MAX_VALUE) {
            long j7 = i15 == 1 ? 314572800L : i15 * 1024 * 1024 * 1000;
            long j8 = 0;
            for (int i16 = 0; i16 < createMediaPaths.size(); i16++) {
                j8 += Utilities.getDirSize(createMediaPaths.valueAt(i16).getAbsolutePath(), 0, true);
            }
            if (j8 > j7) {
                ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList4 = new ArrayList<>();
                for (int i17 = 0; i17 < createMediaPaths.size(); i17++) {
                    fillFilesRecursive(createMediaPaths.valueAt(i17), arrayList4);
                }
                for (int i18 = 0; i18 < arrayList2.size(); i18++) {
                    ((CacheByChatsController) arrayList2.get(i18)).lookupFiles(arrayList4);
                }
                Collections.sort(arrayList4, AutoDeleteMediaTask$$ExternalSyntheticLambda1.INSTANCE);
                int i19 = 0;
                i4 = 0;
                j = 0;
                for (int i20 = 0; i20 < arrayList4.size(); i20++) {
                    if (((FileInfoInternal) arrayList4.get(i20)).keepMedia != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                        if (((FileInfoInternal) arrayList4.get(i20)).lastUsageDate > 0) {
                            long length = ((FileInfoInternal) arrayList4.get(i20)).file.length();
                            j8 -= length;
                            i4++;
                            j += length;
                            try {
                                ((FileInfoInternal) arrayList4.get(i20)).file.delete();
                            } catch (Exception unused) {
                            }
                            if (j8 < j7) {
                                break;
                            }
                        } else {
                            i19++;
                        }
                    }
                }
                i5 = i19;
                file2 = new File(file, "acache");
                if (file2.exists()) {
                    try {
                        Utilities.clearDir(file2.getAbsolutePath(), 0, i - 86400, false);
                    } catch (Throwable th5) {
                        FileLog.e(th5);
                    }
                }
                MessagesController.getGlobalMainSettings().edit().putInt("lastKeepMediaCheckTime", SharedConfig.lastKeepMediaCheckTime).apply();
                if (BuildVars.LOGS_ENABLED) {
                    return;
                }
                FileLog.d("checkKeepMedia task end time " + (System.currentTimeMillis() - currentTimeMillis) + " auto deleted info: files " + i9 + " size " + AndroidUtilities.formatFileSize(j6) + "   deleted by size limit info: files " + i4 + " size " + AndroidUtilities.formatFileSize(j) + " unknownTimeFiles " + i5);
                return;
            }
        }
        j = 0;
        i4 = 0;
        i5 = 0;
        file2 = new File(file, "acache");
        if (file2.exists()) {
        }
        MessagesController.getGlobalMainSettings().edit().putInt("lastKeepMediaCheckTime", SharedConfig.lastKeepMediaCheckTime).apply();
        if (BuildVars.LOGS_ENABLED) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$run$0(FileInfoInternal fileInfoInternal, FileInfoInternal fileInfoInternal2) {
        long j = fileInfoInternal2.lastUsageDate;
        long j2 = fileInfoInternal.lastUsageDate;
        if (j > j2) {
            return -1;
        }
        return j < j2 ? 1 : 0;
    }

    private static void fillFilesRecursive(File file, ArrayList<FileInfoInternal> arrayList) {
        File[] listFiles;
        if (file == null || (listFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                fillFilesRecursive(file2, arrayList);
            } else if (!file2.getName().equals(".nomedia") && !usingFilePaths.contains(file2.getAbsolutePath())) {
                arrayList.add(new FileInfoInternal(file2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class FileInfoInternal extends CacheByChatsController.KeepMediaFile {
        final long lastUsageDate;

        private FileInfoInternal(File file) {
            super(file);
            this.lastUsageDate = Utilities.getLastUsageFileTime(file.getAbsolutePath());
        }
    }

    public static void lockFile(File file) {
        if (file == null) {
            return;
        }
        lockFile(file.getAbsolutePath());
    }

    public static void unlockFile(File file) {
        if (file == null) {
            return;
        }
        unlockFile(file.getAbsolutePath());
    }

    public static void lockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.add(str);
    }

    public static void unlockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.remove(str);
    }
}
