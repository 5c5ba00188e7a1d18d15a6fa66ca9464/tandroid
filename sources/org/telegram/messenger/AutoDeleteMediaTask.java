package org.telegram.messenger;

import android.util.SparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import org.telegram.messenger.AutoDeleteMediaTask;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes3.dex */
public class AutoDeleteMediaTask {
    public static Set<String> usingFilePaths = Collections.newSetFromMap(new ConcurrentHashMap());

    /* JADX INFO: Access modifiers changed from: private */
    static class FileInfoInternal extends CacheByChatsController.KeepMediaFile {
        final long lastUsageDate;

        private FileInfoInternal(File file) {
            super(file);
            this.lastUsageDate = Utilities.getLastUsageFileTime(file.getAbsolutePath());
        }
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
    public static /* synthetic */ int lambda$run$0(FileInfoInternal fileInfoInternal, FileInfoInternal fileInfoInternal2) {
        long j = fileInfoInternal2.lastUsageDate;
        long j2 = fileInfoInternal.lastUsageDate;
        if (j > j2) {
            return -1;
        }
        return j < j2 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0157 A[Catch: all -> 0x00e8, TryCatch #4 {all -> 0x00e8, blocks: (B:59:0x00d2, B:63:0x00f3, B:64:0x00dd, B:78:0x00fb, B:80:0x0101, B:83:0x010e, B:85:0x0114, B:87:0x011e, B:88:0x015a, B:95:0x016f, B:118:0x012a, B:121:0x013a, B:126:0x0157, B:127:0x013f, B:129:0x0143), top: B:58:0x00d2 }] */
    /* JADX WARN: Removed duplicated region for block: B:184:0x02d5  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:195:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$run$1(int i, File file) {
        int i2;
        int i3;
        int i4;
        long j;
        int i5;
        int i6;
        File file2;
        int[] iArr;
        long j2;
        boolean z;
        long j3;
        long j4;
        ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList;
        long currentTimeMillis = System.currentTimeMillis();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("checkKeepMedia start task");
        }
        ArrayList arrayList2 = new ArrayList();
        int i7 = 0;
        boolean z2 = false;
        while (true) {
            i2 = 4;
            i3 = 1;
            if (i7 >= 4) {
                break;
            }
            if (UserConfig.getInstance(i7).isClientActivated()) {
                CacheByChatsController cacheByChatsController = UserConfig.getInstance(i7).getMessagesController().getCacheByChatsController();
                arrayList2.add(cacheByChatsController);
                if (cacheByChatsController.getKeepMediaExceptionsByDialogs().size() > 0) {
                    z2 = true;
                }
            }
            i7++;
        }
        int[] iArr2 = new int[4];
        long j5 = Long.MAX_VALUE;
        boolean z3 = true;
        for (int i8 = 0; i8 < 4; i8++) {
            int i9 = SharedConfig.getPreferences().getInt("keep_media_type_" + i8, CacheByChatsController.getDefault(i8));
            iArr2[i8] = i9;
            if (i9 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                z3 = false;
            }
            long daysInSeconds = CacheByChatsController.getDaysInSeconds(i9);
            if (daysInSeconds < j5) {
                j5 = daysInSeconds;
            }
        }
        if (z2) {
            z3 = false;
        }
        SparseArray<File> createMediaPaths = ImageLoader.getInstance().createMediaPaths();
        int i10 = 0;
        int i11 = 0;
        long j6 = 0;
        while (i10 < createMediaPaths.size()) {
            if (z3 && (createMediaPaths.keyAt(i10) == i3 || createMediaPaths.keyAt(i10) == 3)) {
                iArr = iArr2;
                j2 = j5;
            } else {
                boolean z4 = createMediaPaths.keyAt(i10) == i2;
                try {
                    File[] listFiles = createMediaPaths.valueAt(i10).listFiles();
                    ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList3 = new ArrayList<>();
                    if (listFiles != null) {
                        int i12 = 0;
                        while (i12 < listFiles.length) {
                            try {
                                if (listFiles[i12].isDirectory()) {
                                    j2 = j5;
                                } else {
                                    j2 = j5;
                                    try {
                                        if (!usingFilePaths.contains(listFiles[i12].getAbsolutePath())) {
                                            arrayList3.add(new CacheByChatsController.KeepMediaFile(listFiles[i12]));
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        iArr = iArr2;
                                        FileLog.e(th);
                                        i10++;
                                        j5 = j2;
                                        iArr2 = iArr;
                                        i2 = 4;
                                        i3 = 1;
                                    }
                                }
                                i12++;
                                j5 = j2;
                            } catch (Throwable th2) {
                                th = th2;
                                j2 = j5;
                            }
                        }
                    }
                    j2 = j5;
                    for (int i13 = 0; i13 < arrayList2.size(); i13++) {
                        ((CacheByChatsController) arrayList2.get(i13)).lookupFiles(arrayList3);
                    }
                    int i14 = 0;
                    while (i14 < arrayList3.size()) {
                        CacheByChatsController.KeepMediaFile keepMediaFile = (CacheByChatsController.KeepMediaFile) arrayList3.get(i14);
                        if (keepMediaFile.isStory) {
                            z = z4;
                            j4 = i - CacheByChatsController.getDaysInSeconds(iArr2[3]);
                        } else {
                            z = z4;
                            int i15 = keepMediaFile.keepMedia;
                            if (i15 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                                if (i15 < 0) {
                                    int i16 = keepMediaFile.dialogType;
                                    if (i16 >= 0) {
                                        i15 = iArr2[i16];
                                    } else if (!z) {
                                        j3 = j2;
                                        if (j3 == Long.MAX_VALUE) {
                                            j4 = i - j3;
                                        }
                                    }
                                }
                                j3 = CacheByChatsController.getDaysInSeconds(i15);
                                if (j3 == Long.MAX_VALUE) {
                                }
                            }
                            arrayList = arrayList3;
                            iArr = iArr2;
                            i14++;
                            z4 = z;
                            arrayList3 = arrayList;
                            iArr2 = iArr;
                        }
                        long lastUsageFileTime = Utilities.getLastUsageFileTime(keepMediaFile.file.getAbsolutePath());
                        if (lastUsageFileTime > 316000000 && lastUsageFileTime < j4) {
                            arrayList = arrayList3;
                            iArr = iArr2;
                            try {
                                if (!usingFilePaths.contains(keepMediaFile.file.getPath())) {
                                    try {
                                        if (BuildVars.LOGS_ENABLED) {
                                            i11++;
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
                                i14++;
                                z4 = z;
                                arrayList3 = arrayList;
                                iArr2 = iArr;
                            } catch (Throwable th3) {
                                th = th3;
                                FileLog.e(th);
                                i10++;
                                j5 = j2;
                                iArr2 = iArr;
                                i2 = 4;
                                i3 = 1;
                            }
                        }
                        arrayList = arrayList3;
                        iArr = iArr2;
                        i14++;
                        z4 = z;
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
            i10++;
            j5 = j2;
            iArr2 = iArr;
            i2 = 4;
            i3 = 1;
        }
        int i17 = SharedConfig.getPreferences().getInt("cache_limit", ConnectionsManager.DEFAULT_DATACENTER_ID);
        if (i17 != Integer.MAX_VALUE) {
            long j7 = i17 == 1 ? 314572800L : i17 * 1048576000;
            long j8 = 0;
            for (int i18 = 0; i18 < createMediaPaths.size(); i18++) {
                j8 += Utilities.getDirSize(createMediaPaths.valueAt(i18).getAbsolutePath(), 0, true);
            }
            if (j8 > j7) {
                ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList4 = new ArrayList<>();
                for (int i19 = 0; i19 < createMediaPaths.size(); i19++) {
                    fillFilesRecursive(createMediaPaths.valueAt(i19), arrayList4);
                }
                for (int i20 = 0; i20 < arrayList2.size(); i20++) {
                    ((CacheByChatsController) arrayList2.get(i20)).lookupFiles(arrayList4);
                }
                Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda1
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$run$0;
                        lambda$run$0 = AutoDeleteMediaTask.lambda$run$0((AutoDeleteMediaTask.FileInfoInternal) obj, (AutoDeleteMediaTask.FileInfoInternal) obj2);
                        return lambda$run$0;
                    }
                });
                i5 = 0;
                i6 = 0;
                int i21 = 0;
                long j9 = 0;
                while (true) {
                    if (i21 >= arrayList4.size()) {
                        i4 = i11;
                        break;
                    }
                    if (((FileInfoInternal) arrayList4.get(i21)).keepMedia != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                        i4 = i11;
                        if (((FileInfoInternal) arrayList4.get(i21)).lastUsageDate > 0) {
                            long length = ((FileInfoInternal) arrayList4.get(i21)).file.length();
                            j8 -= length;
                            i5++;
                            j9 += length;
                            try {
                                ((FileInfoInternal) arrayList4.get(i21)).file.delete();
                            } catch (Exception unused) {
                            }
                            if (j8 < j7) {
                                break;
                            }
                        } else {
                            i6++;
                        }
                    } else {
                        i4 = i11;
                    }
                    i21++;
                    i11 = i4;
                }
                j = j9;
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
                FileLog.d("checkKeepMedia task end time " + (System.currentTimeMillis() - currentTimeMillis) + " auto deleted info: files " + i4 + " size " + AndroidUtilities.formatFileSize(j6) + "   deleted by size limit info: files " + i5 + " size " + AndroidUtilities.formatFileSize(j) + " unknownTimeFiles " + i6);
                return;
            }
        }
        i4 = i11;
        j = 0;
        i5 = 0;
        i6 = 0;
        file2 = new File(file, "acache");
        if (file2.exists()) {
        }
        MessagesController.getGlobalMainSettings().edit().putInt("lastKeepMediaCheckTime", SharedConfig.lastKeepMediaCheckTime).apply();
        if (BuildVars.LOGS_ENABLED) {
        }
    }

    public static void lockFile(File file) {
        if (file == null) {
            return;
        }
        lockFile(file.getAbsolutePath());
    }

    public static void lockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.add(str);
    }

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

    public static void unlockFile(File file) {
        if (file == null) {
            return;
        }
        unlockFile(file.getAbsolutePath());
    }

    public static void unlockFile(String str) {
        if (str == null) {
            return;
        }
        usingFilePaths.remove(str);
    }
}
