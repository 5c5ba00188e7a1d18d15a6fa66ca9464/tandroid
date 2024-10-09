package androidx.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.telegram.messenger.LiteMode;

/* loaded from: classes.dex */
final class MultiDexExtractor implements Closeable {
    private final FileLock cacheLock;
    private final File dexDir;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File sourceApk;
    private final long sourceCrc;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ExtractedDex extends File {
        public long crc;

        public ExtractedDex(File file, String str) {
            super(file, str);
            this.crc = -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiDexExtractor(File file, File file2) {
        Log.i("MultiDex", "MultiDexExtractor(" + file.getPath() + ", " + file2.getPath() + ")");
        this.sourceApk = file;
        this.dexDir = file2;
        this.sourceCrc = getZipCrc(file);
        File file3 = new File(file2, "MultiDex.lock");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file3, "rw");
        this.lockRaf = randomAccessFile;
        try {
            FileChannel channel = randomAccessFile.getChannel();
            this.lockChannel = channel;
            try {
                Log.i("MultiDex", "Blocking on lock " + file3.getPath());
                this.cacheLock = channel.lock();
                Log.i("MultiDex", file3.getPath() + " locked");
            } catch (IOException e) {
                e = e;
                closeQuietly(this.lockChannel);
                throw e;
            } catch (Error e2) {
                e = e2;
                closeQuietly(this.lockChannel);
                throw e;
            } catch (RuntimeException e3) {
                e = e3;
                closeQuietly(this.lockChannel);
                throw e;
            }
        } catch (IOException e4) {
            e = e4;
            closeQuietly(this.lockRaf);
            throw e;
        } catch (Error e5) {
            e = e5;
            closeQuietly(this.lockRaf);
            throw e;
        } catch (RuntimeException e6) {
            e = e6;
            closeQuietly(this.lockRaf);
            throw e;
        }
    }

    private void clearDexDir() {
        File[] listFiles = this.dexDir.listFiles(new FileFilter() { // from class: androidx.multidex.MultiDexExtractor.1
            @Override // java.io.FileFilter
            public boolean accept(File file) {
                return !file.getName().equals("MultiDex.lock");
            }
        });
        if (listFiles == null) {
            Log.w("MultiDex", "Failed to list secondary dex dir content (" + this.dexDir.getPath() + ").");
            return;
        }
        for (File file : listFiles) {
            Log.i("MultiDex", "Trying to delete old file " + file.getPath() + " of size " + file.length());
            if (file.delete()) {
                Log.i("MultiDex", "Deleted old file " + file.getPath());
            } else {
                Log.w("MultiDex", "Failed to delete old file " + file.getPath());
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            Log.w("MultiDex", "Failed to close resource", e);
        }
    }

    private static void extract(ZipFile zipFile, ZipEntry zipEntry, File file, String str) {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        File createTempFile = File.createTempFile("tmp-" + str, ".zip", file.getParentFile());
        Log.i("MultiDex", "Extracting " + createTempFile.getPath());
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(createTempFile)));
            try {
                ZipEntry zipEntry2 = new ZipEntry("classes.dex");
                zipEntry2.setTime(zipEntry.getTime());
                zipOutputStream.putNextEntry(zipEntry2);
                byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    } else {
                        zipOutputStream.write(bArr, 0, read);
                    }
                }
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                if (!createTempFile.setReadOnly()) {
                    throw new IOException("Failed to mark readonly \"" + createTempFile.getAbsolutePath() + "\" (tmp of \"" + file.getAbsolutePath() + "\")");
                }
                Log.i("MultiDex", "Renaming to " + file.getPath());
                if (createTempFile.renameTo(file)) {
                    closeQuietly(inputStream);
                    createTempFile.delete();
                    return;
                }
                throw new IOException("Failed to rename \"" + createTempFile.getAbsolutePath() + "\" to \"" + file.getAbsolutePath() + "\"");
            } catch (Throwable th) {
                zipOutputStream.close();
                throw th;
            }
        } catch (Throwable th2) {
            closeQuietly(inputStream);
            createTempFile.delete();
            throw th2;
        }
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences("multidex.version", 4);
    }

    private static long getTimeStamp(File file) {
        long lastModified = file.lastModified();
        return lastModified == -1 ? lastModified - 1 : lastModified;
    }

    private static long getZipCrc(File file) {
        long zipCrc = ZipUtil.getZipCrc(file);
        return zipCrc == -1 ? zipCrc - 1 : zipCrc;
    }

    private static boolean isModified(Context context, File file, long j, String str) {
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        if (multiDexPreferences.getLong(str + "timestamp", -1L) == getTimeStamp(file)) {
            if (multiDexPreferences.getLong(str + "crc", -1L) == j) {
                return false;
            }
        }
        return true;
    }

    private List loadExistingExtractions(Context context, String str) {
        Log.i("MultiDex", "loading existing secondary dex files");
        String str2 = this.sourceApk.getName() + ".classes";
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        int i = multiDexPreferences.getInt(str + "dex.number", 1);
        ArrayList arrayList = new ArrayList(i + (-1));
        int i2 = 2;
        while (i2 <= i) {
            ExtractedDex extractedDex = new ExtractedDex(this.dexDir, str2 + i2 + ".zip");
            if (!extractedDex.isFile()) {
                throw new IOException("Missing extracted secondary dex file '" + extractedDex.getPath() + "'");
            }
            extractedDex.crc = getZipCrc(extractedDex);
            long j = multiDexPreferences.getLong(str + "dex.crc." + i2, -1L);
            long j2 = multiDexPreferences.getLong(str + "dex.time." + i2, -1L);
            long lastModified = extractedDex.lastModified();
            if (j2 == lastModified) {
                String str3 = str2;
                SharedPreferences sharedPreferences = multiDexPreferences;
                if (j == extractedDex.crc) {
                    arrayList.add(extractedDex);
                    i2++;
                    multiDexPreferences = sharedPreferences;
                    str2 = str3;
                }
            }
            throw new IOException("Invalid extracted dex: " + extractedDex + " (key \"" + str + "\"), expected modification time: " + j2 + ", modification time: " + lastModified + ", expected crc: " + j + ", file crc: " + extractedDex.crc);
        }
        return arrayList;
    }

    private List performExtractions() {
        boolean z;
        String str = this.sourceApk.getName() + ".classes";
        clearDexDir();
        ArrayList arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(this.sourceApk);
        try {
            ZipEntry entry = zipFile.getEntry("classes2.dex");
            int i = 2;
            while (entry != null) {
                ExtractedDex extractedDex = new ExtractedDex(this.dexDir, str + i + ".zip");
                arrayList.add(extractedDex);
                Log.i("MultiDex", "Extraction is needed for file " + extractedDex);
                int i2 = 0;
                boolean z2 = false;
                while (i2 < 3 && !z2) {
                    int i3 = i2 + 1;
                    extract(zipFile, entry, extractedDex, str);
                    try {
                        extractedDex.crc = getZipCrc(extractedDex);
                        z = true;
                    } catch (IOException e) {
                        Log.w("MultiDex", "Failed to read crc from " + extractedDex.getAbsolutePath(), e);
                        z = false;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Extraction ");
                    sb.append(z ? "succeeded" : "failed");
                    sb.append(" '");
                    sb.append(extractedDex.getAbsolutePath());
                    sb.append("': length ");
                    sb.append(extractedDex.length());
                    sb.append(" - crc: ");
                    sb.append(extractedDex.crc);
                    Log.i("MultiDex", sb.toString());
                    if (!z) {
                        extractedDex.delete();
                        if (extractedDex.exists()) {
                            Log.w("MultiDex", "Failed to delete corrupted secondary dex '" + extractedDex.getPath() + "'");
                        }
                    }
                    z2 = z;
                    i2 = i3;
                }
                if (!z2) {
                    throw new IOException("Could not create zip file " + extractedDex.getAbsolutePath() + " for secondary dex (" + i + ")");
                }
                i++;
                entry = zipFile.getEntry("classes" + i + ".dex");
            }
            try {
                zipFile.close();
            } catch (IOException e2) {
                Log.w("MultiDex", "Failed to close resource", e2);
            }
            return arrayList;
        } finally {
        }
    }

    private static void putStoredApkInfo(Context context, String str, long j, long j2, List list) {
        SharedPreferences.Editor edit = getMultiDexPreferences(context).edit();
        edit.putLong(str + "timestamp", j);
        edit.putLong(str + "crc", j2);
        edit.putInt(str + "dex.number", list.size() + 1);
        Iterator it = list.iterator();
        int i = 2;
        while (it.hasNext()) {
            ExtractedDex extractedDex = (ExtractedDex) it.next();
            edit.putLong(str + "dex.crc." + i, extractedDex.crc);
            edit.putLong(str + "dex.time." + i, extractedDex.lastModified());
            i++;
        }
        edit.commit();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List load(Context context, String str, boolean z) {
        List list;
        Log.i("MultiDex", "MultiDexExtractor.load(" + this.sourceApk.getPath() + ", " + z + ", " + str + ")");
        if (!this.cacheLock.isValid()) {
            throw new IllegalStateException("MultiDexExtractor was closed");
        }
        if (!z && !isModified(context, this.sourceApk, this.sourceCrc, str)) {
            try {
                list = loadExistingExtractions(context, str);
            } catch (IOException e) {
                Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", e);
            }
            Log.i("MultiDex", "load found " + list.size() + " secondary dex files");
            return list;
        }
        Log.i("MultiDex", z ? "Forced extraction must be performed." : "Detected that extraction must be performed.");
        List performExtractions = performExtractions();
        putStoredApkInfo(context, str, getTimeStamp(this.sourceApk), this.sourceCrc, performExtractions);
        list = performExtractions;
        Log.i("MultiDex", "load found " + list.size() + " secondary dex files");
        return list;
    }
}
