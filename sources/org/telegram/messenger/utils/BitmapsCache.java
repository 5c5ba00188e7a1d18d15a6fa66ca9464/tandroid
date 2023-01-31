package org.telegram.messenger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import j$.util.Comparator$-CC;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueuePoolBackground;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.utils.BitmapsCache;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes.dex */
public class BitmapsCache {
    private static ThreadPoolExecutor bitmapCompressExecutor;
    static volatile boolean cleanupScheduled;
    private static CacheGeneratorSharedTools sharedTools;
    private static int taskCounter;
    byte[] bufferTmp;
    volatile boolean cacheCreated;
    RandomAccessFile cachedFile;
    int compressQuality;
    boolean error;
    final File file;
    volatile boolean fileExist;
    String fileName;
    private int frameIndex;
    int h;
    BitmapFactory.Options options;
    volatile boolean recycled;
    private final Cacheable source;
    private int tryCount;
    final boolean useSharedBuffers;
    int w;
    static ConcurrentHashMap<Thread, byte[]> sharedBuffers = new ConcurrentHashMap<>();
    private static final int N = Utilities.clamp(Runtime.getRuntime().availableProcessors() - 2, 6, 1);
    ArrayList<FrameOffset> frameOffsets = new ArrayList<>();
    private final Object mutex = new Object();
    public AtomicBoolean cancelled = new AtomicBoolean(false);
    private Runnable cleanupSharedBuffers = new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache.1
        @Override // java.lang.Runnable
        public void run() {
            for (Thread thread : BitmapsCache.sharedBuffers.keySet()) {
                if (!thread.isAlive()) {
                    BitmapsCache.sharedBuffers.remove(thread);
                }
            }
            if (!BitmapsCache.sharedBuffers.isEmpty()) {
                AndroidUtilities.runOnUIThread(BitmapsCache.this.cleanupSharedBuffers, 5000L);
            } else {
                BitmapsCache.cleanupScheduled = false;
            }
        }
    };

    /* loaded from: classes.dex */
    public static class CacheOptions {
        public int compressQuality = 100;
        public boolean fallback = false;
        public boolean firstFrame;
    }

    /* loaded from: classes.dex */
    public interface Cacheable {
        int getNextFrame(Bitmap bitmap);

        void prepareForGenerateCache();

        void releaseForGenerateCache();
    }

    /* loaded from: classes.dex */
    public static class Metadata {
        public int frame;
    }

    public void cancelCreate() {
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x0113 -> B:66:0x012b). Please submit an issue!!! */
    public BitmapsCache(File file, Cacheable cacheable, CacheOptions cacheOptions, int i, int i2, boolean z) {
        RandomAccessFile randomAccessFile;
        Throwable th;
        this.source = cacheable;
        this.w = i;
        this.h = i2;
        this.compressQuality = cacheOptions.compressQuality;
        this.fileName = file.getName();
        if (bitmapCompressExecutor == null) {
            int i3 = N;
            bitmapCompressExecutor = new ThreadPoolExecutor(i3, i3, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        File file2 = new File(FileLoader.checkDirectory(4), "acache");
        StringBuilder sb = new StringBuilder();
        sb.append(this.fileName);
        sb.append("_");
        sb.append(i);
        sb.append("_");
        sb.append(i2);
        sb.append(z ? "_nolimit" : " ");
        sb.append(".pcache2");
        File file3 = new File(file2, sb.toString());
        this.file = file3;
        this.useSharedBuffers = i < AndroidUtilities.dp(60.0f) && i2 < AndroidUtilities.dp(60.0f);
        if (SharedConfig.getDevicePerformanceClass() >= 2) {
            this.fileExist = file3.exists();
            if (this.fileExist) {
                try {
                    try {
                        randomAccessFile = new RandomAccessFile(file3, "r");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                        if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                            randomAccessFile.seek(randomAccessFile.readInt());
                            int readInt = randomAccessFile.readInt();
                            fillFrames(randomAccessFile, readInt > 10000 ? 0 : readInt);
                            if (this.frameOffsets.size() == 0) {
                                this.cacheCreated = false;
                                this.fileExist = false;
                                file3.delete();
                            } else {
                                if (this.cachedFile != randomAccessFile) {
                                    closeCachedFile();
                                }
                                this.cachedFile = randomAccessFile;
                            }
                        }
                        if (this.cachedFile != randomAccessFile) {
                            randomAccessFile.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            th.printStackTrace();
                            this.file.delete();
                            this.fileExist = false;
                            if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                            return;
                        } catch (Throwable th3) {
                            try {
                                if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                                    randomAccessFile.close();
                                }
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                            throw th3;
                        }
                    }
                } catch (Throwable th4) {
                    randomAccessFile = null;
                    th = th4;
                }
                return;
            }
            return;
        }
        this.fileExist = false;
        this.cacheCreated = false;
    }

    public static void incrementTaskCounter() {
        taskCounter++;
    }

    public static void decrementTaskCounter() {
        int i = taskCounter - 1;
        taskCounter = i;
        if (i <= 0) {
            taskCounter = 0;
            RLottieDrawable.lottieCacheGenerateQueue.postRunnable(BitmapsCache$$ExternalSyntheticLambda1.INSTANCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$decrementTaskCounter$0() {
        CacheGeneratorSharedTools cacheGeneratorSharedTools = sharedTools;
        if (cacheGeneratorSharedTools != null) {
            cacheGeneratorSharedTools.release();
            sharedTools = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0067, code lost:
        if (r24.cachedFile != r0) goto L110;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x00c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x00e6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0087 A[Catch: all -> 0x01fb, IOException -> 0x01fd, FileNotFoundException -> 0x0202, TryCatch #12 {FileNotFoundException -> 0x0202, IOException -> 0x01fd, blocks: (B:3:0x0002, B:17:0x004b, B:26:0x0065, B:32:0x0073, B:36:0x007a, B:38:0x0087, B:39:0x008e, B:40:0x00be, B:42:0x00c2, B:46:0x00cc, B:48:0x00d4, B:51:0x00dc, B:54:0x00e7, B:56:0x00eb, B:58:0x00ef, B:62:0x00f9, B:61:0x00f6, B:63:0x00fc, B:64:0x0119, B:66:0x011f, B:67:0x013c, B:68:0x0177, B:73:0x01bf, B:75:0x01ca, B:76:0x01cf, B:77:0x01d3, B:79:0x01d7, B:81:0x01db, B:85:0x01e5, B:84:0x01e2, B:89:0x01f1, B:45:0x00c9), top: B:119:0x0002, outer: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0177 A[Catch: all -> 0x01fb, IOException -> 0x01fd, FileNotFoundException -> 0x0202, TryCatch #12 {FileNotFoundException -> 0x0202, IOException -> 0x01fd, blocks: (B:3:0x0002, B:17:0x004b, B:26:0x0065, B:32:0x0073, B:36:0x007a, B:38:0x0087, B:39:0x008e, B:40:0x00be, B:42:0x00c2, B:46:0x00cc, B:48:0x00d4, B:51:0x00dc, B:54:0x00e7, B:56:0x00eb, B:58:0x00ef, B:62:0x00f9, B:61:0x00f6, B:63:0x00fc, B:64:0x0119, B:66:0x011f, B:67:0x013c, B:68:0x0177, B:73:0x01bf, B:75:0x01ca, B:76:0x01cf, B:77:0x01d3, B:79:0x01d7, B:81:0x01db, B:85:0x01e5, B:84:0x01e2, B:89:0x01f1, B:45:0x00c9), top: B:119:0x0002, outer: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01ca A[Catch: all -> 0x01fb, IOException -> 0x01fd, FileNotFoundException -> 0x0202, TryCatch #12 {FileNotFoundException -> 0x0202, IOException -> 0x01fd, blocks: (B:3:0x0002, B:17:0x004b, B:26:0x0065, B:32:0x0073, B:36:0x007a, B:38:0x0087, B:39:0x008e, B:40:0x00be, B:42:0x00c2, B:46:0x00cc, B:48:0x00d4, B:51:0x00dc, B:54:0x00e7, B:56:0x00eb, B:58:0x00ef, B:62:0x00f9, B:61:0x00f6, B:63:0x00fc, B:64:0x0119, B:66:0x011f, B:67:0x013c, B:68:0x0177, B:73:0x01bf, B:75:0x01ca, B:76:0x01cf, B:77:0x01d3, B:79:0x01d7, B:81:0x01db, B:85:0x01e5, B:84:0x01e2, B:89:0x01f1, B:45:0x00c9), top: B:119:0x0002, outer: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01d7 A[Catch: all -> 0x01fb, IOException -> 0x01fd, FileNotFoundException -> 0x0202, TRY_LEAVE, TryCatch #12 {FileNotFoundException -> 0x0202, IOException -> 0x01fd, blocks: (B:3:0x0002, B:17:0x004b, B:26:0x0065, B:32:0x0073, B:36:0x007a, B:38:0x0087, B:39:0x008e, B:40:0x00be, B:42:0x00c2, B:46:0x00cc, B:48:0x00d4, B:51:0x00dc, B:54:0x00e7, B:56:0x00eb, B:58:0x00ef, B:62:0x00f9, B:61:0x00f6, B:63:0x00fc, B:64:0x0119, B:66:0x011f, B:67:0x013c, B:68:0x0177, B:73:0x01bf, B:75:0x01ca, B:76:0x01cf, B:77:0x01d3, B:79:0x01d7, B:81:0x01db, B:85:0x01e5, B:84:0x01e2, B:89:0x01f1, B:45:0x00c9), top: B:119:0x0002, outer: #7 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void createCache() {
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        final Bitmap[] bitmapArr;
        ByteArrayOutputStream[] byteArrayOutputStreamArr;
        CountDownLatch[] countDownLatchArr;
        ArrayList arrayList;
        AtomicBoolean atomicBoolean;
        int i;
        int i2;
        try {
            try {
                long j = 0;
                int i3 = 0;
                if (this.file.exists()) {
                    try {
                        randomAccessFile = new RandomAccessFile(this.file, "r");
                    } catch (Throwable unused) {
                        randomAccessFile = null;
                    }
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                    } catch (Throwable unused2) {
                        try {
                            this.file.delete();
                        } catch (Throwable unused3) {
                        }
                        if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                            } catch (Throwable unused4) {
                            }
                        }
                        randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                        if (sharedTools == null) {
                        }
                        sharedTools.allocate(this.h, this.w);
                        bitmapArr = sharedTools.bitmap;
                        byteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                        countDownLatchArr = new CountDownLatch[N];
                        arrayList = new ArrayList();
                        randomAccessFile2.writeBoolean(false);
                        randomAccessFile2.writeInt(0);
                        atomicBoolean = new AtomicBoolean(false);
                        this.source.prepareForGenerateCache();
                        i = 0;
                        int i4 = 0;
                        while (true) {
                            if (countDownLatchArr[i] != null) {
                            }
                            if (!this.cancelled.get()) {
                                break;
                            } else if (this.source.getNextFrame(bitmapArr[i]) != 1) {
                            }
                            atomicBoolean = r13;
                            arrayList = r19;
                            countDownLatchArr = r20;
                            byteArrayOutputStreamArr = r21;
                            randomAccessFile2 = r22;
                            i3 = 0;
                            j = 0;
                        }
                        AtomicBoolean atomicBoolean2 = atomicBoolean;
                        CountDownLatch[] countDownLatchArr2 = countDownLatchArr;
                        RandomAccessFile randomAccessFile3 = randomAccessFile2;
                        if (BuildVars.DEBUG_VERSION) {
                        }
                        atomicBoolean2.set(true);
                        while (i2 < N) {
                        }
                        randomAccessFile3.close();
                        this.source.releaseForGenerateCache();
                    }
                    if (this.cacheCreated) {
                        this.frameOffsets.clear();
                        randomAccessFile.seek(randomAccessFile.readInt());
                        int readInt = randomAccessFile.readInt();
                        if (readInt > 10000) {
                            readInt = 0;
                        }
                        if (readInt > 0) {
                            fillFrames(randomAccessFile, readInt);
                            randomAccessFile.seek(0L);
                            if (this.cachedFile != randomAccessFile) {
                                closeCachedFile();
                            }
                            this.cachedFile = randomAccessFile;
                            this.fileExist = true;
                            if (this.cachedFile != randomAccessFile) {
                                try {
                                    randomAccessFile.close();
                                } catch (Throwable unused5) {
                                }
                            }
                        }
                        this.fileExist = false;
                        this.cacheCreated = false;
                    }
                    if (!this.cacheCreated) {
                        this.file.delete();
                    }
                }
                randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                if (sharedTools == null) {
                    sharedTools = new CacheGeneratorSharedTools();
                }
                sharedTools.allocate(this.h, this.w);
                bitmapArr = sharedTools.bitmap;
                byteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                countDownLatchArr = new CountDownLatch[N];
                arrayList = new ArrayList();
                randomAccessFile2.writeBoolean(false);
                randomAccessFile2.writeInt(0);
                atomicBoolean = new AtomicBoolean(false);
                this.source.prepareForGenerateCache();
                i = 0;
                int i42 = 0;
                while (true) {
                    if (countDownLatchArr[i] != null) {
                        try {
                            countDownLatchArr[i].await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!this.cancelled.get() || atomicBoolean.get()) {
                        break;
                        break;
                    } else if (this.source.getNextFrame(bitmapArr[i]) != 1) {
                        countDownLatchArr[i] = new CountDownLatch(1);
                        final AtomicBoolean atomicBoolean3 = atomicBoolean;
                        AtomicBoolean atomicBoolean4 = atomicBoolean;
                        final int i5 = i;
                        final ArrayList arrayList2 = arrayList;
                        final ByteArrayOutputStream[] byteArrayOutputStreamArr2 = byteArrayOutputStreamArr;
                        final CountDownLatch[] countDownLatchArr3 = countDownLatchArr;
                        final int i6 = i42;
                        ByteArrayOutputStream[] byteArrayOutputStreamArr3 = byteArrayOutputStreamArr;
                        final RandomAccessFile randomAccessFile4 = randomAccessFile2;
                        RandomAccessFile randomAccessFile5 = randomAccessFile2;
                        bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.this.lambda$createCache$1(atomicBoolean3, bitmapArr, i5, byteArrayOutputStreamArr2, i6, randomAccessFile4, arrayList2, countDownLatchArr3);
                            }
                        });
                        int i7 = i + 1;
                        i42++;
                        i = i7 >= N ? 0 : i7;
                        atomicBoolean = atomicBoolean4;
                        arrayList = arrayList2;
                        countDownLatchArr = countDownLatchArr3;
                        byteArrayOutputStreamArr = byteArrayOutputStreamArr3;
                        randomAccessFile2 = randomAccessFile5;
                        i3 = 0;
                        j = 0;
                    } else {
                        for (int i8 = 0; i8 < N; i8++) {
                            if (countDownLatchArr[i8] != null) {
                                try {
                                    countDownLatchArr[i8].await();
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                        int length = (int) randomAccessFile2.length();
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(BitmapsCache$$ExternalSyntheticLambda2.INSTANCE));
                        byteArrayOutputStreamArr[i3].reset();
                        int size = arrayList.size();
                        byteArrayOutputStreamArr[i3].writeInt(size);
                        for (int i9 = 0; i9 < arrayList.size(); i9++) {
                            byteArrayOutputStreamArr[i3].writeInt(((FrameOffset) arrayList.get(i9)).frameOffset);
                            byteArrayOutputStreamArr[i3].writeInt(((FrameOffset) arrayList.get(i9)).frameSize);
                        }
                        randomAccessFile2.write(byteArrayOutputStreamArr[i3].buf, i3, (size * 8) + 4);
                        byteArrayOutputStreamArr[i3].reset();
                        randomAccessFile2.seek(j);
                        randomAccessFile2.writeBoolean(true);
                        randomAccessFile2.writeInt(length);
                        atomicBoolean.set(true);
                        randomAccessFile2.close();
                        this.frameOffsets.clear();
                        this.frameOffsets.addAll(arrayList);
                        closeCachedFile();
                        this.cachedFile = new RandomAccessFile(this.file, "r");
                        this.cacheCreated = true;
                        this.fileExist = true;
                    }
                }
                AtomicBoolean atomicBoolean22 = atomicBoolean;
                CountDownLatch[] countDownLatchArr22 = countDownLatchArr;
                RandomAccessFile randomAccessFile32 = randomAccessFile2;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("cancelled cache generation");
                }
                atomicBoolean22.set(true);
                for (i2 = 0; i2 < N; i2++) {
                    if (countDownLatchArr22[i2] != null) {
                        try {
                            countDownLatchArr22[i2].await();
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (bitmapArr[i2] != null) {
                        try {
                            bitmapArr[i2].recycle();
                        } catch (Exception unused6) {
                        }
                    }
                }
                randomAccessFile32.close();
                this.source.releaseForGenerateCache();
            } finally {
                this.source.releaseForGenerateCache();
            }
        } catch (FileNotFoundException e4) {
            e4.printStackTrace();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCache$1(AtomicBoolean atomicBoolean, Bitmap[] bitmapArr, int i, ByteArrayOutputStream[] byteArrayOutputStreamArr, int i2, RandomAccessFile randomAccessFile, ArrayList arrayList, CountDownLatch[] countDownLatchArr) {
        if (this.cancelled.get() || atomicBoolean.get()) {
            return;
        }
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.WEBP;
        if (Build.VERSION.SDK_INT <= 26) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        bitmapArr[i].compress(compressFormat, this.compressQuality, byteArrayOutputStreamArr[i]);
        int i3 = byteArrayOutputStreamArr[i].count;
        try {
            synchronized (this.mutex) {
                FrameOffset frameOffset = new FrameOffset(i2);
                frameOffset.frameOffset = (int) randomAccessFile.length();
                arrayList.add(frameOffset);
                randomAccessFile.write(byteArrayOutputStreamArr[i].buf, 0, i3);
                frameOffset.frameSize = i3;
                byteArrayOutputStreamArr[i].reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                randomAccessFile.close();
            } catch (Exception unused) {
            } catch (Throwable th) {
                atomicBoolean.set(true);
                throw th;
            }
            atomicBoolean.set(true);
        }
        countDownLatchArr[i].countDown();
    }

    private void fillFrames(RandomAccessFile randomAccessFile, int i) throws Throwable {
        if (i == 0) {
            return;
        }
        byte[] bArr = new byte[i * 8];
        randomAccessFile.read(bArr);
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        for (int i2 = 0; i2 < i; i2++) {
            FrameOffset frameOffset = new FrameOffset(i2);
            frameOffset.frameOffset = wrap.getInt();
            frameOffset.frameSize = wrap.getInt();
            this.frameOffsets.add(frameOffset);
        }
    }

    public int getFrame(Bitmap bitmap, Metadata metadata) {
        int frame = getFrame(this.frameIndex, bitmap);
        metadata.frame = this.frameIndex;
        if (this.cacheCreated && !this.frameOffsets.isEmpty()) {
            int i = this.frameIndex + 1;
            this.frameIndex = i;
            if (i >= this.frameOffsets.size()) {
                this.frameIndex = 0;
            }
        }
        return frame;
    }

    public int getFrame(int i, Bitmap bitmap) {
        RandomAccessFile randomAccessFile;
        if (this.error) {
            return -1;
        }
        RandomAccessFile randomAccessFile2 = null;
        try {
            if (this.cacheCreated || this.fileExist) {
                if (!this.cacheCreated || (randomAccessFile = this.cachedFile) == null) {
                    randomAccessFile = new RandomAccessFile(this.file, "r");
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                        if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                            randomAccessFile.seek(randomAccessFile.readInt());
                            fillFrames(randomAccessFile, randomAccessFile.readInt());
                        }
                        if (this.frameOffsets.size() == 0) {
                            this.cacheCreated = false;
                        }
                        if (!this.cacheCreated) {
                            randomAccessFile.close();
                            return -1;
                        }
                    } catch (FileNotFoundException unused) {
                        randomAccessFile2 = randomAccessFile;
                        if (this.error && randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return -1;
                    } catch (Throwable th) {
                        th = th;
                        randomAccessFile2 = randomAccessFile;
                        FileLog.e(th, false);
                        int i2 = this.tryCount + 1;
                        this.tryCount = i2;
                        if (i2 > 10) {
                            this.error = true;
                        }
                        if (this.error) {
                            randomAccessFile2.close();
                        }
                        return -1;
                    }
                }
                if (this.frameOffsets.size() == 0) {
                    return -1;
                }
                FrameOffset frameOffset = this.frameOffsets.get(Utilities.clamp(i, this.frameOffsets.size() - 1, 0));
                randomAccessFile.seek(frameOffset.frameOffset);
                byte[] buffer = getBuffer(frameOffset);
                randomAccessFile.readFully(buffer, 0, frameOffset.frameSize);
                if (!this.recycled) {
                    if (this.cachedFile != randomAccessFile) {
                        closeCachedFile();
                    }
                    this.cachedFile = randomAccessFile;
                } else {
                    this.cachedFile = null;
                    randomAccessFile.close();
                }
                if (this.options == null) {
                    this.options = new BitmapFactory.Options();
                }
                BitmapFactory.Options options = this.options;
                options.inBitmap = bitmap;
                BitmapFactory.decodeByteArray(buffer, 0, frameOffset.frameSize, options);
                this.options.inBitmap = null;
                return 0;
            }
            return -1;
        } catch (FileNotFoundException unused2) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void closeCachedFile() {
        RandomAccessFile randomAccessFile = this.cachedFile;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBuffer(FrameOffset frameOffset) {
        byte[] bArr;
        boolean z = this.useSharedBuffers && Thread.currentThread().getName().startsWith(DispatchQueuePoolBackground.THREAD_PREFIX);
        if (z) {
            bArr = sharedBuffers.get(Thread.currentThread());
        } else {
            bArr = this.bufferTmp;
        }
        if (bArr == null || bArr.length < frameOffset.frameSize) {
            bArr = new byte[(int) (frameOffset.frameSize * 1.3f)];
            if (z) {
                sharedBuffers.put(Thread.currentThread(), bArr);
                if (!cleanupScheduled) {
                    cleanupScheduled = true;
                    AndroidUtilities.runOnUIThread(this.cleanupSharedBuffers, 5000L);
                }
            } else {
                this.bufferTmp = bArr;
            }
        }
        return bArr;
    }

    public boolean needGenCache() {
        return (this.cacheCreated && this.fileExist) ? false : true;
    }

    public void recycle() {
        RandomAccessFile randomAccessFile = this.cachedFile;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cachedFile = null;
        }
        this.recycled = true;
    }

    public int getFrameCount() {
        return this.frameOffsets.size();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FrameOffset {
        int frameOffset;
        int frameSize;
        final int index;

        private FrameOffset(BitmapsCache bitmapsCache, int i) {
            this.index = i;
        }
    }

    /* loaded from: classes.dex */
    public static class ByteArrayOutputStream extends OutputStream {
        protected byte[] buf;
        protected int count;

        public ByteArrayOutputStream(int i) {
            this.buf = new byte[i];
        }

        private void ensureCapacity(int i) {
            if (i - this.buf.length > 0) {
                grow(i);
            }
        }

        private void grow(int i) {
            int length = this.buf.length << 1;
            if (length - i < 0) {
                length = i;
            }
            if (length - 2147483639 > 0) {
                length = hugeCapacity(i);
            }
            this.buf = Arrays.copyOf(this.buf, length);
        }

        private static int hugeCapacity(int i) {
            if (i >= 0) {
                if (i > 2147483639) {
                    return ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
                return 2147483639;
            }
            throw new OutOfMemoryError();
        }

        @Override // java.io.OutputStream
        public synchronized void write(int i) {
            ensureCapacity(this.count + 1);
            byte[] bArr = this.buf;
            int i2 = this.count;
            bArr[i2] = (byte) i;
            this.count = i2 + 1;
        }

        public void writeInt(int i) {
            ensureCapacity(this.count + 4);
            byte[] bArr = this.buf;
            int i2 = this.count;
            bArr[i2] = (byte) (i >>> 24);
            bArr[i2 + 1] = (byte) (i >>> 16);
            bArr[i2 + 2] = (byte) (i >>> 8);
            bArr[i2 + 3] = (byte) i;
            this.count = i2 + 4;
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr, int i, int i2) {
            if (i >= 0) {
                if (i <= bArr.length && i2 >= 0 && (i + i2) - bArr.length <= 0) {
                    ensureCapacity(this.count + i2);
                    System.arraycopy(bArr, i, this.buf, this.count, i2);
                    this.count += i2;
                }
            }
            throw new IndexOutOfBoundsException();
        }

        public synchronized void reset() {
            this.count = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CacheGeneratorSharedTools {
        private Bitmap[] bitmap;
        ByteArrayOutputStream[] byteArrayOutputStream;
        private int lastSize;

        private CacheGeneratorSharedTools() {
            this.byteArrayOutputStream = new ByteArrayOutputStream[BitmapsCache.N];
            this.bitmap = new Bitmap[BitmapsCache.N];
        }

        void allocate(int i, int i2) {
            int i3 = (i2 << 16) + i;
            boolean z = this.lastSize != i3;
            this.lastSize = i3;
            for (int i4 = 0; i4 < BitmapsCache.N; i4++) {
                if (z || this.bitmap[i4] == null) {
                    Bitmap[] bitmapArr = this.bitmap;
                    if (bitmapArr[i4] != null) {
                        final Bitmap bitmap = bitmapArr[i4];
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.CacheGeneratorSharedTools.lambda$allocate$0(bitmap);
                            }
                        });
                    }
                    this.bitmap[i4] = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
                }
                ByteArrayOutputStream[] byteArrayOutputStreamArr = this.byteArrayOutputStream;
                if (byteArrayOutputStreamArr[i4] == null) {
                    byteArrayOutputStreamArr[i4] = new ByteArrayOutputStream(i2 * i * 2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$allocate$0(Bitmap bitmap) {
            try {
                bitmap.recycle();
            } catch (Exception unused) {
            }
        }

        void release() {
            final ArrayList arrayList = null;
            for (int i = 0; i < BitmapsCache.N; i++) {
                if (this.bitmap[i] != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(this.bitmap[i]);
                }
                this.bitmap[i] = null;
                this.byteArrayOutputStream[i] = null;
            }
            if (arrayList.isEmpty()) {
                return;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BitmapsCache.CacheGeneratorSharedTools.lambda$release$1(arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$release$1(ArrayList arrayList) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Bitmap) it.next()).recycle();
            }
        }
    }
}
