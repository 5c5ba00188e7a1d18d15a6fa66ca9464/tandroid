package org.telegram.messenger;

import android.util.SparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes.dex */
public class AutoDeleteMediaTask {
    public static Set<String> usingFilePaths = Collections.newSetFromMap(new ConcurrentHashMap());

    public static void run() {
        final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (BuildVars.DEBUG_PRIVATE_VERSION || Math.abs(currentTimeMillis - SharedConfig.lastKeepMediaCheckTime) >= 86400) {
            SharedConfig.lastKeepMediaCheckTime = currentTimeMillis;
            final File checkDirectory = FileLoader.checkDirectory(4);
            Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.AutoDeleteMediaTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoDeleteMediaTask.lambda$run$1(currentTimeMillis, checkDirectory);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:140:0x027f  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0156 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x016e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:192:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$run$1(int i, File file) {
        int i2;
        int i3;
        long j;
        long j2;
        int i4;
        int i5;
        File file2;
        SparseArray<File> sparseArray;
        long j3;
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("checkKeepMedia start task");
        }
        ArrayList arrayList = new ArrayList();
        int i6 = 0;
        boolean z2 = false;
        while (true) {
            i2 = 4;
            if (i6 >= 4) {
                break;
            }
            if (UserConfig.getInstance(i6).isClientActivated()) {
                CacheByChatsController cacheByChatsController = UserConfig.getInstance(i6).getMessagesController().getCacheByChatsController();
                arrayList.add(cacheByChatsController);
                if (cacheByChatsController.getKeepMediaExceptionsByDialogs().size() > 0) {
                    z2 = true;
                }
            }
            i6++;
        }
        int[] iArr = new int[3];
        int i7 = 0;
        boolean z3 = true;
        long j4 = Long.MAX_VALUE;
        for (int i8 = 3; i7 < i8; i8 = 3) {
            iArr[i7] = SharedConfig.getPreferences().getInt("keep_media_type_" + i7, CacheByChatsController.getDefault(i7));
            if (iArr[i7] != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                z3 = false;
            }
            long daysInSeconds = CacheByChatsController.getDaysInSeconds(iArr[i7]);
            if (daysInSeconds < j4) {
                j4 = daysInSeconds;
            }
            i7++;
        }
        if (z2) {
            z3 = false;
        }
        if (z3) {
            i3 = 0;
            j = 0;
        } else {
            SparseArray<File> createMediaPaths = ImageLoader.getInstance().createMediaPaths();
            int i9 = 0;
            i3 = 0;
            j = 0;
            while (i9 < createMediaPaths.size()) {
                boolean z4 = createMediaPaths.keyAt(i9) == i2;
                try {
                    File[] listFiles = createMediaPaths.valueAt(i9).listFiles();
                    ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList2 = new ArrayList<>();
                    if (listFiles != null) {
                        for (int i10 = 0; i10 < listFiles.length; i10++) {
                            if (!listFiles[i10].isDirectory() && !usingFilePaths.contains(listFiles[i10].getAbsolutePath())) {
                                arrayList2.add(new CacheByChatsController.KeepMediaFile(listFiles[i10]));
                            }
                        }
                    }
                    for (int i11 = 0; i11 < arrayList.size(); i11++) {
                        ((CacheByChatsController) arrayList.get(i11)).lookupFiles(arrayList2);
                    }
                    int i12 = 0;
                    while (i12 < arrayList2.size()) {
                        CacheByChatsController.KeepMediaFile keepMediaFile = (CacheByChatsController.KeepMediaFile) arrayList2.get(i12);
                        int i13 = keepMediaFile.keepMedia;
                        if (i13 != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                            if (i13 >= 0) {
                                j3 = CacheByChatsController.getDaysInSeconds(i13);
                            } else {
                                int i14 = keepMediaFile.dialogType;
                                if (i14 >= 0) {
                                    j3 = CacheByChatsController.getDaysInSeconds(iArr[i14]);
                                } else if (!z4) {
                                    j3 = j4;
                                }
                            }
                            if (j3 == Long.MAX_VALUE) {
                                sparseArray = createMediaPaths;
                            } else {
                                try {
                                    sparseArray = createMediaPaths;
                                    if (Utilities.getLastUsageFileTime(keepMediaFile.file.getAbsolutePath()) < i - j3) {
                                        try {
                                            if (!usingFilePaths.contains(keepMediaFile.file.getPath())) {
                                                z = true;
                                                if (!z) {
                                                    try {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            i3++;
                                                            j += keepMediaFile.file.length();
                                                        }
                                                        keepMediaFile.file.delete();
                                                    } catch (Exception e) {
                                                        FileLog.e(e);
                                                    }
                                                }
                                            }
                                        } catch (Throwable th) {
                                            th = th;
                                            FileLog.e(th);
                                            i9++;
                                            createMediaPaths = sparseArray;
                                            i2 = 4;
                                        }
                                    }
                                    z = false;
                                    if (!z) {
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    sparseArray = createMediaPaths;
                                }
                            }
                            i12++;
                            createMediaPaths = sparseArray;
                        }
                        sparseArray = createMediaPaths;
                        i12++;
                        createMediaPaths = sparseArray;
                    }
                    sparseArray = createMediaPaths;
                } catch (Throwable th3) {
                    th = th3;
                    sparseArray = createMediaPaths;
                }
                i9++;
                createMediaPaths = sparseArray;
                i2 = 4;
            }
        }
        int i15 = SharedConfig.getPreferences().getInt("cache_limit", ConnectionsManager.DEFAULT_DATACENTER_ID);
        if (i15 != Integer.MAX_VALUE) {
            long j5 = i15 == 1 ? 314572800L : i15 * 1024 * 1024 * 1000;
            SparseArray<File> createMediaPaths2 = ImageLoader.getInstance().createMediaPaths();
            long j6 = 0;
            for (int i16 = 0; i16 < createMediaPaths2.size(); i16++) {
                j6 += Utilities.getDirSize(createMediaPaths2.valueAt(i16).getAbsolutePath(), 0, true);
            }
            if (j6 > j5) {
                ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList3 = new ArrayList<>();
                for (int i17 = 0; i17 < createMediaPaths2.size(); i17++) {
                    if (createMediaPaths2.keyAt(i17) != 4) {
                        fillFilesRecursive(createMediaPaths2.valueAt(i17), arrayList3);
                    }
                }
                for (int i18 = 0; i18 < arrayList.size(); i18++) {
                    ((CacheByChatsController) arrayList.get(i18)).lookupFiles(arrayList3);
                }
                Collections.sort(arrayList3, AutoDeleteMediaTask$$ExternalSyntheticLambda1.INSTANCE);
                int i19 = 0;
                i4 = 0;
                long j7 = 0;
                for (int i20 = 0; i20 < arrayList3.size(); i20++) {
                    if (((FileInfoInternal) arrayList3.get(i20)).keepMedia != CacheByChatsController.KEEP_MEDIA_FOREVER) {
                        if (((FileInfoInternal) arrayList3.get(i20)).lastUsageDate > 0) {
                            long length = ((FileInfoInternal) arrayList3.get(i20)).file.length();
                            j6 -= length;
                            i4++;
                            j7 += length;
                            try {
                                ((FileInfoInternal) arrayList3.get(i20)).file.delete();
                            } catch (Exception unused) {
                            }
                            if (j6 < j5) {
                                break;
                            }
                        } else {
                            i19++;
                        }
                    }
                }
                i5 = i19;
                j2 = j7;
                file2 = new File(file, "acache");
                if (file2.exists()) {
                    try {
                        Utilities.clearDir(file2.getAbsolutePath(), 0, i - 86400, false);
                    } catch (Throwable th4) {
                        FileLog.e(th4);
                    }
                }
                MessagesController.getGlobalMainSettings().edit().putInt("lastKeepMediaCheckTime", SharedConfig.lastKeepMediaCheckTime).apply();
                if (BuildVars.LOGS_ENABLED) {
                    return;
                }
                FileLog.d("checkKeepMedia task end time " + (System.currentTimeMillis() - currentTimeMillis) + "auto deleted info: files " + i3 + " size " + AndroidUtilities.formatFileSize(j) + "   deleted by size limit info: files " + i4 + " size " + AndroidUtilities.formatFileSize(j2) + " unknownTimeFiles " + i5);
                return;
            }
        }
        j2 = 0;
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
    /* loaded from: classes.dex */
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
