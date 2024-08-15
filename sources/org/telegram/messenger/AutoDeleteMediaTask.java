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

    public static void run() {
        final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (Math.abs(currentTimeMillis - SharedConfig.lastKeepMediaCheckTime) < 86400) {
            return;
        }
        SharedConfig.lastKeepMediaCheckTime = currentTimeMillis;
        final File checkDirectory = FileLoader.checkDirectory(4);
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AutoDeleteMediaTask.lambda$run$1(currentTimeMillis, checkDirectory);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02da  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0189 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:198:0x01db A[SYNTHETIC] */
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
            int i8 = SharedConfig.getPreferences().getInt("keep_media_type_" + i7, CacheByChatsController.getDefault(i7));
            iArr2[i7] = i8;
            if (i8 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                z4 = false;
            }
            long daysInSeconds = CacheByChatsController.getDaysInSeconds(i8);
            if (daysInSeconds < j5) {
                j5 = daysInSeconds;
            }
        }
        if (z3) {
            z4 = false;
        }
        SparseArray<File> createMediaPaths = ImageLoader.getInstance().createMediaPaths();
        int i9 = 0;
        int i10 = 0;
        long j6 = 0;
        while (i9 < createMediaPaths.size()) {
            if (z4 && (createMediaPaths.keyAt(i9) == i3 || createMediaPaths.keyAt(i9) == 3)) {
                iArr = iArr2;
                j2 = j5;
            } else {
                boolean z5 = createMediaPaths.keyAt(i9) == i2;
                try {
                    File[] listFiles = createMediaPaths.valueAt(i9).listFiles();
                    ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList3 = new ArrayList<>();
                    if (listFiles != null) {
                        int i11 = 0;
                        while (i11 < listFiles.length) {
                            try {
                                if (listFiles[i11].isDirectory()) {
                                    j2 = j5;
                                } else {
                                    j2 = j5;
                                    try {
                                        if (!usingFilePaths.contains(listFiles[i11].getAbsolutePath())) {
                                            arrayList3.add(new CacheByChatsController.KeepMediaFile(listFiles[i11]));
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        iArr = iArr2;
                                        FileLog.e(th);
                                        i9++;
                                        j5 = j2;
                                        iArr2 = iArr;
                                        i2 = 4;
                                        i3 = 1;
                                    }
                                }
                                i11++;
                                j5 = j2;
                            } catch (Throwable th2) {
                                th = th2;
                                j2 = j5;
                            }
                        }
                    }
                    j2 = j5;
                    for (int i12 = 0; i12 < arrayList2.size(); i12++) {
                        ((CacheByChatsController) arrayList2.get(i12)).lookupFiles(arrayList3);
                    }
                    int i13 = 0;
                    while (i13 < arrayList3.size()) {
                        CacheByChatsController.KeepMediaFile keepMediaFile = (CacheByChatsController.KeepMediaFile) arrayList3.get(i13);
                        if (keepMediaFile.isStory) {
                            z = z5;
                            j4 = i - CacheByChatsController.getDaysInSeconds(iArr2[3]);
                        } else {
                            z = z5;
                            int i14 = keepMediaFile.keepMedia;
                            if (i14 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                                if (i14 >= 0) {
                                    j3 = CacheByChatsController.getDaysInSeconds(i14);
                                } else {
                                    int i15 = keepMediaFile.dialogType;
                                    if (i15 >= 0) {
                                        j3 = CacheByChatsController.getDaysInSeconds(iArr2[i15]);
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
                            i13++;
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
                                                i10++;
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
                                    i13++;
                                    z5 = z;
                                    arrayList3 = arrayList;
                                    iArr2 = iArr;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                FileLog.e(th);
                                i9++;
                                j5 = j2;
                                iArr2 = iArr;
                                i2 = 4;
                                i3 = 1;
                            }
                        }
                        z2 = false;
                        if (!z2) {
                        }
                        i13++;
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
            i9++;
            j5 = j2;
            iArr2 = iArr;
            i2 = 4;
            i3 = 1;
        }
        int i16 = SharedConfig.getPreferences().getInt("cache_limit", ConnectionsManager.DEFAULT_DATACENTER_ID);
        if (i16 != Integer.MAX_VALUE) {
            long j7 = i16 == 1 ? 314572800L : i16 * 1024 * 1024 * 1000;
            long j8 = 0;
            for (int i17 = 0; i17 < createMediaPaths.size(); i17++) {
                j8 += Utilities.getDirSize(createMediaPaths.valueAt(i17).getAbsolutePath(), 0, true);
            }
            if (j8 > j7) {
                ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList4 = new ArrayList<>();
                for (int i18 = 0; i18 < createMediaPaths.size(); i18++) {
                    fillFilesRecursive(createMediaPaths.valueAt(i18), arrayList4);
                }
                for (int i19 = 0; i19 < arrayList2.size(); i19++) {
                    ((CacheByChatsController) arrayList2.get(i19)).lookupFiles(arrayList4);
                }
                Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$run$0;
                        lambda$run$0 = AutoDeleteMediaTask.lambda$run$0((AutoDeleteMediaTask.FileInfoInternal) obj, (AutoDeleteMediaTask.FileInfoInternal) obj2);
                        return lambda$run$0;
                    }
                });
                int i20 = 0;
                i4 = 0;
                j = 0;
                for (int i21 = 0; i21 < arrayList4.size(); i21++) {
                    if (((FileInfoInternal) arrayList4.get(i21)).keepMedia != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                        if (((FileInfoInternal) arrayList4.get(i21)).lastUsageDate > 0) {
                            long length = ((FileInfoInternal) arrayList4.get(i21)).file.length();
                            j8 -= length;
                            i4++;
                            j += length;
                            try {
                                ((FileInfoInternal) arrayList4.get(i21)).file.delete();
                            } catch (Exception unused) {
                            }
                            if (j8 < j7) {
                                break;
                            }
                        } else {
                            i20++;
                        }
                    }
                }
                i5 = i20;
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
                FileLog.d("checkKeepMedia task end time " + (System.currentTimeMillis() - currentTimeMillis) + " auto deleted info: files " + i10 + " size " + AndroidUtilities.formatFileSize(j6) + "   deleted by size limit info: files " + i4 + " size " + AndroidUtilities.formatFileSize(j) + " unknownTimeFiles " + i5);
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
