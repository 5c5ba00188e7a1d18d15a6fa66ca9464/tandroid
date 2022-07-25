package org.telegram.messenger;

import android.os.Looper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLiteException;
import org.telegram.SQLite.SQLitePreparedStatement;
/* loaded from: classes.dex */
public class FilePathDatabase {
    private static final String DATABASE_BACKUP_NAME = "file_to_path_backup";
    private static final String DATABASE_NAME = "file_to_path";
    private static final int LAST_DB_VERSION = 2;
    private File cacheFile;
    private final int currentAccount;
    private SQLiteDatabase database;
    private final DispatchQueue dispatchQueue;
    private File shmCacheFile;

    public FilePathDatabase(int i) {
        this.currentAccount = i;
        DispatchQueue dispatchQueue = new DispatchQueue("files_database_queue_" + i);
        this.dispatchQueue = dispatchQueue;
        dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        createDatabase(0, false);
    }

    public void createDatabase(int i, boolean z) {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        this.cacheFile = new File(filesDirFixed, "file_to_path.db");
        this.shmCacheFile = new File(filesDirFixed, "file_to_path.db-shm");
        boolean z2 = !this.cacheFile.exists();
        try {
            SQLiteDatabase sQLiteDatabase = new SQLiteDatabase(this.cacheFile.getPath());
            this.database = sQLiteDatabase;
            sQLiteDatabase.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
            this.database.executeFast("PRAGMA temp_store = MEMORY").stepThis().dispose();
            if (z2) {
                this.database.executeFast("CREATE TABLE paths(document_id INTEGER, dc_id INTEGER, type INTEGER, path TEXT, PRIMARY KEY(document_id, dc_id, type));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS path_in_paths ON paths(path);").stepThis().dispose();
                this.database.executeFast("PRAGMA user_version = 2").stepThis().dispose();
            } else {
                int intValue = this.database.executeInt("PRAGMA user_version", new Object[0]).intValue();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("current files db version = " + intValue);
                }
                if (intValue == 0) {
                    throw new Exception("malformed");
                }
                migrateDatabase(intValue);
            }
            if (!z) {
                createBackup();
            }
            FileLog.d("files db created from_backup= " + z);
        } catch (Exception e) {
            if (i < 4) {
                if (!z && restoreBackup()) {
                    createDatabase(i + 1, true);
                    return;
                }
                this.cacheFile.delete();
                this.shmCacheFile.delete();
                createDatabase(i + 1, false);
            }
            if (!BuildVars.DEBUG_VERSION) {
                return;
            }
            FileLog.e(e);
        }
    }

    private void migrateDatabase(int i) throws SQLiteException {
        if (i == 1) {
            this.database.executeFast("CREATE INDEX IF NOT EXISTS path_in_paths ON paths(path);").stepThis().dispose();
            this.database.executeFast("PRAGMA user_version = 2").stepThis().dispose();
        }
    }

    private void createBackup() {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        File file2 = new File(filesDirFixed, "file_to_path_backup.db");
        try {
            AndroidUtilities.copyFile(this.cacheFile, file2);
            FileLog.d("file db backup created " + file2.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean restoreBackup() {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        File file2 = new File(filesDirFixed, "file_to_path_backup.db");
        if (!file2.exists()) {
            return false;
        }
        try {
            return AndroidUtilities.copyFile(file2, this.cacheFile);
        } catch (IOException e) {
            FileLog.e(e);
            return false;
        }
    }

    public String getPath(final long j, final int i, final int i2, boolean z) {
        SQLiteException sQLiteException;
        String str;
        SQLiteCursor queryFinalized;
        if (z) {
            if (BuildVars.DEBUG_VERSION && this.dispatchQueue.getHandler() != null && Thread.currentThread() == this.dispatchQueue.getHandler().getLooper().getThread()) {
                throw new RuntimeException("Error, lead to infinity loop");
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final String[] strArr = new String[1];
            System.currentTimeMillis();
            this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    FilePathDatabase.this.lambda$getPath$1(j, i, i2, strArr, countDownLatch);
                }
            });
            try {
                countDownLatch.await();
            } catch (Exception unused) {
            }
            return strArr[0];
        }
        SQLiteCursor sQLiteCursor = null;
        r1 = null;
        String str2 = null;
        sQLiteCursor = null;
        try {
            try {
                queryFinalized = this.database.queryFinalized("SELECT path FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2, new Object[0]);
            } catch (SQLiteException e) {
                sQLiteException = e;
                str = null;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (queryFinalized.next()) {
                str2 = queryFinalized.stringValue(0);
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("get file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + str2);
                }
            }
            queryFinalized.dispose();
            return str2;
        } catch (SQLiteException e2) {
            sQLiteException = e2;
            str = str2;
            sQLiteCursor = queryFinalized;
            FileLog.e(sQLiteException);
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            return str;
        } catch (Throwable th2) {
            th = th2;
            sQLiteCursor = queryFinalized;
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0070, code lost:
        if (r0 == null) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getPath$1(long j, int i, int i2, String[] strArr, CountDownLatch countDownLatch) {
        SQLiteCursor sQLiteCursor = null;
        try {
            try {
                SQLiteDatabase sQLiteDatabase = this.database;
                sQLiteCursor = sQLiteDatabase.queryFinalized("SELECT path FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2, new Object[0]);
                if (sQLiteCursor.next()) {
                    strArr[0] = sQLiteCursor.stringValue(0);
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("get file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + strArr[0]);
                    }
                }
            } catch (SQLiteException e) {
                FileLog.e(e);
            }
            sQLiteCursor.dispose();
            countDownLatch.countDown();
        } catch (Throwable th) {
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
    }

    public void putPath(final long j, final int i, final int i2, final String str) {
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$putPath$2(j, i, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$putPath$2(long j, int i, int i2, String str) {
        SQLitePreparedStatement sQLitePreparedStatement;
        SQLitePreparedStatement sQLitePreparedStatement2;
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("put file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + str);
        }
        SQLitePreparedStatement sQLitePreparedStatement3 = null;
        try {
            if (str != null) {
                SQLitePreparedStatement executeFast = this.database.executeFast("DELETE FROM paths WHERE path = ?");
                try {
                    executeFast.bindString(1, str);
                    executeFast.step();
                    sQLitePreparedStatement3 = this.database.executeFast("REPLACE INTO paths VALUES(?, ?, ?, ?)");
                    sQLitePreparedStatement3.requery();
                    sQLitePreparedStatement3.bindLong(1, j);
                    sQLitePreparedStatement3.bindInteger(2, i);
                    sQLitePreparedStatement3.bindInteger(3, i2);
                    sQLitePreparedStatement3.bindString(4, str);
                    sQLitePreparedStatement3.step();
                    sQLitePreparedStatement3.dispose();
                    sQLitePreparedStatement2 = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                } catch (SQLiteException e) {
                    e = e;
                    sQLitePreparedStatement = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                    try {
                        FileLog.e(e);
                        if (sQLitePreparedStatement3 != null) {
                            sQLitePreparedStatement3.dispose();
                        }
                        if (sQLitePreparedStatement == null) {
                            return;
                        }
                        sQLitePreparedStatement.dispose();
                        return;
                    } catch (Throwable th) {
                        th = th;
                        if (sQLitePreparedStatement3 != null) {
                            sQLitePreparedStatement3.dispose();
                        }
                        if (sQLitePreparedStatement != null) {
                            sQLitePreparedStatement.dispose();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLitePreparedStatement = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                    if (sQLitePreparedStatement3 != null) {
                    }
                    if (sQLitePreparedStatement != null) {
                    }
                    throw th;
                }
            } else {
                this.database.executeFast("DELETE FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2).stepThis().dispose();
                sQLitePreparedStatement2 = null;
            }
            if (sQLitePreparedStatement3 != null) {
                sQLitePreparedStatement3.dispose();
            }
            if (sQLitePreparedStatement2 == null) {
                return;
            }
            sQLitePreparedStatement2.dispose();
        } catch (SQLiteException e2) {
            e = e2;
            sQLitePreparedStatement = sQLitePreparedStatement3;
        } catch (Throwable th3) {
            th = th3;
            sQLitePreparedStatement = sQLitePreparedStatement3;
        }
    }

    public void checkMediaExistance(ArrayList<MessageObject> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        final ArrayList arrayList2 = new ArrayList(arrayList);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.lambda$checkMediaExistance$3(arrayList2, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        FileLog.d("checkMediaExistance size=" + arrayList.size() + " time=" + (System.currentTimeMillis() - currentTimeMillis));
        if (!BuildVars.DEBUG_VERSION || Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }
        FileLog.e(new Exception("warning, not allowed in main thread"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkMediaExistance$3(ArrayList arrayList, CountDownLatch countDownLatch) {
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                ((MessageObject) arrayList.get(i)).checkMediaExistance(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }

    public void clear() {
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$clear$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clear$4() {
        try {
            this.database.executeFast("DELETE FROM paths WHERE 1").stepThis().dispose();
        } catch (SQLiteException e) {
            FileLog.e(e);
        }
    }

    public boolean hasAnotherRefOnFile(final String str) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] zArr = {false};
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$hasAnotherRefOnFile$5(str, zArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hasAnotherRefOnFile$5(String str, boolean[] zArr, CountDownLatch countDownLatch) {
        try {
            SQLiteDatabase sQLiteDatabase = this.database;
            if (sQLiteDatabase.queryFinalized("SELECT document_id FROM paths WHERE path = '" + str + "'", new Object[0]).next()) {
                zArr[0] = true;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        countDownLatch.countDown();
    }

    /* loaded from: classes.dex */
    public static class PathData {
        public final int dc;
        public final long id;
        public final int type;

        public PathData(long j, int i, int i2) {
            this.id = j;
            this.dc = i;
            this.type = i2;
        }
    }
}
