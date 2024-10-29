package org.telegram.messenger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import j$.util.Comparator$-CC;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.ToIntFunction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueuePoolBackground;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.utils.BitmapsCache;
import org.telegram.ui.Components.RLottieDrawable;

/* loaded from: classes3.dex */
public class BitmapsCache {
    private static ThreadPoolExecutor bitmapCompressExecutor;
    static volatile boolean cleanupScheduled;
    private static boolean mkdir;
    private static CacheGeneratorSharedTools sharedTools;
    private static int taskCounter;
    byte[] bufferTmp;
    volatile boolean cacheCreated;
    RandomAccessFile cachedFile;
    public volatile boolean checked;
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
    static ConcurrentHashMap sharedBuffers = new ConcurrentHashMap();
    private static final int N = Utilities.clamp(Runtime.getRuntime().availableProcessors() - 2, 6, 1);
    public final AtomicInteger framesProcessed = new AtomicInteger(0);
    ArrayList frameOffsets = new ArrayList();
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
            if (BitmapsCache.sharedBuffers.isEmpty()) {
                BitmapsCache.cleanupScheduled = false;
            } else {
                AndroidUtilities.runOnUIThread(BitmapsCache.this.cleanupSharedBuffers, 5000L);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class CacheGeneratorSharedTools {
        private Bitmap[] bitmap;
        ImmutableByteArrayOutputStream[] byteArrayOutputStream;
        private int lastSize;

        private CacheGeneratorSharedTools() {
            this.byteArrayOutputStream = new ImmutableByteArrayOutputStream[BitmapsCache.N];
            this.bitmap = new Bitmap[BitmapsCache.N];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$allocate$0(Bitmap bitmap) {
            try {
                bitmap.recycle();
            } catch (Exception unused) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$release$1(ArrayList arrayList) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Bitmap) it.next()).recycle();
            }
        }

        void allocate(int i, int i2) {
            int i3 = (i2 << 16) + i;
            boolean z = this.lastSize != i3;
            this.lastSize = i3;
            for (int i4 = 0; i4 < BitmapsCache.N; i4++) {
                if (z || this.bitmap[i4] == null) {
                    final Bitmap bitmap = this.bitmap[i4];
                    if (bitmap != null) {
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.CacheGeneratorSharedTools.lambda$allocate$0(bitmap);
                            }
                        });
                    }
                    this.bitmap[i4] = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
                }
                ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr = this.byteArrayOutputStream;
                if (immutableByteArrayOutputStreamArr[i4] == null) {
                    immutableByteArrayOutputStreamArr[i4] = new ImmutableByteArrayOutputStream(i2 * i * 2);
                }
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BitmapsCache.CacheGeneratorSharedTools.lambda$release$1(arrayList);
                }
            });
        }
    }

    /* loaded from: classes3.dex */
    public static class CacheOptions {
        public int compressQuality = 100;
        public boolean fallback = false;
        public boolean firstFrame;
    }

    /* loaded from: classes3.dex */
    public interface Cacheable {
        int getNextFrame(Bitmap bitmap);

        void prepareForGenerateCache();

        void releaseForGenerateCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class FrameOffset {
        int frameOffset;
        int frameSize;
        final int index;

        private FrameOffset(int i) {
            this.index = i;
        }
    }

    /* loaded from: classes3.dex */
    public static class Metadata {
        public int frame;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:67:0x010f -> B:42:0x013c). Please report as a decompilation issue!!! */
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
        if (!mkdir) {
            file2.mkdir();
            mkdir = true;
        }
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
        if (SharedConfig.getDevicePerformanceClass() < 2) {
            this.fileExist = false;
            this.cacheCreated = false;
            return;
        }
        this.fileExist = file3.exists();
        if (this.fileExist) {
            try {
                try {
                    randomAccessFile = new RandomAccessFile(file3, "r");
                } catch (Throwable th2) {
                    randomAccessFile = null;
                    th = th2;
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
                            this.checked = true;
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
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        th.printStackTrace();
                        this.file.delete();
                        this.fileExist = false;
                        this.checked = true;
                        if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                        this.checked = true;
                    } catch (Throwable th4) {
                        try {
                            if (this.cachedFile != randomAccessFile && randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        throw th4;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        this.checked = true;
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

    public static void decrementTaskCounter() {
        int i = taskCounter - 1;
        taskCounter = i;
        if (i <= 0) {
            taskCounter = 0;
            RLottieDrawable.lottieCacheGenerateQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BitmapsCache.lambda$decrementTaskCounter$0();
                }
            });
        }
    }

    private void fillFrames(RandomAccessFile randomAccessFile, int i) {
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

    private byte[] getBuffer(FrameOffset frameOffset) {
        boolean z = this.useSharedBuffers && Thread.currentThread().getName().startsWith(DispatchQueuePoolBackground.THREAD_PREFIX);
        byte[] bArr = z ? (byte[]) sharedBuffers.get(Thread.currentThread()) : this.bufferTmp;
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

    public static void incrementTaskCounter() {
        taskCounter++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCache$1(AtomicBoolean atomicBoolean, Bitmap[] bitmapArr, int i, ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr, int i2, RandomAccessFile randomAccessFile, ArrayList arrayList, CountDownLatch[] countDownLatchArr) {
        if (this.cancelled.get() || atomicBoolean.get()) {
            return;
        }
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.WEBP;
        if (Build.VERSION.SDK_INT <= 28) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        bitmapArr[i].compress(compressFormat, this.compressQuality, immutableByteArrayOutputStreamArr[i]);
        int i3 = immutableByteArrayOutputStreamArr[i].count;
        try {
            synchronized (this.mutex) {
                FrameOffset frameOffset = new FrameOffset(i2);
                frameOffset.frameOffset = (int) randomAccessFile.length();
                arrayList.add(frameOffset);
                randomAccessFile.write(immutableByteArrayOutputStreamArr[i].buf, 0, i3);
                frameOffset.frameSize = i3;
                immutableByteArrayOutputStreamArr[i].reset();
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$decrementTaskCounter$0() {
        CacheGeneratorSharedTools cacheGeneratorSharedTools = sharedTools;
        if (cacheGeneratorSharedTools != null) {
            cacheGeneratorSharedTools.release();
            sharedTools = null;
        }
    }

    public void cancelCreate() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:112:0x0074, code lost:
    
        if (r24.cachedFile != r0) goto L117;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01e2 A[Catch: all -> 0x005a, IOException -> 0x005d, FileNotFoundException | IOException -> 0x0060, TryCatch #6 {all -> 0x005a, blocks: (B:3:0x0002, B:101:0x004d, B:111:0x0072, B:120:0x0080, B:6:0x0087, B:8:0x0095, B:9:0x009c, B:10:0x00cc, B:82:0x00d0, B:12:0x00d9, B:14:0x00e1, B:16:0x00f0, B:27:0x00fb, B:29:0x00ff, B:32:0x0103, B:35:0x010c, B:38:0x0109, B:42:0x010f, B:43:0x012f, B:45:0x0135, B:47:0x0152, B:18:0x018f, B:22:0x01c9, B:53:0x01de, B:55:0x01e2, B:56:0x01e7, B:57:0x01eb, B:59:0x01ef, B:71:0x01f3, B:61:0x01fc, B:64:0x0200, B:75:0x01f9, B:77:0x0206, B:86:0x00d6, B:131:0x0210), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01ef A[Catch: all -> 0x005a, IOException -> 0x005d, FileNotFoundException | IOException -> 0x0060, TRY_LEAVE, TryCatch #6 {all -> 0x005a, blocks: (B:3:0x0002, B:101:0x004d, B:111:0x0072, B:120:0x0080, B:6:0x0087, B:8:0x0095, B:9:0x009c, B:10:0x00cc, B:82:0x00d0, B:12:0x00d9, B:14:0x00e1, B:16:0x00f0, B:27:0x00fb, B:29:0x00ff, B:32:0x0103, B:35:0x010c, B:38:0x0109, B:42:0x010f, B:43:0x012f, B:45:0x0135, B:47:0x0152, B:18:0x018f, B:22:0x01c9, B:53:0x01de, B:55:0x01e2, B:56:0x01e7, B:57:0x01eb, B:59:0x01ef, B:71:0x01f3, B:61:0x01fc, B:64:0x0200, B:75:0x01f9, B:77:0x0206, B:86:0x00d6, B:131:0x0210), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00d0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0095 A[Catch: all -> 0x005a, IOException -> 0x005d, FileNotFoundException | IOException -> 0x0060, TryCatch #6 {all -> 0x005a, blocks: (B:3:0x0002, B:101:0x004d, B:111:0x0072, B:120:0x0080, B:6:0x0087, B:8:0x0095, B:9:0x009c, B:10:0x00cc, B:82:0x00d0, B:12:0x00d9, B:14:0x00e1, B:16:0x00f0, B:27:0x00fb, B:29:0x00ff, B:32:0x0103, B:35:0x010c, B:38:0x0109, B:42:0x010f, B:43:0x012f, B:45:0x0135, B:47:0x0152, B:18:0x018f, B:22:0x01c9, B:53:0x01de, B:55:0x01e2, B:56:0x01e7, B:57:0x01eb, B:59:0x01ef, B:71:0x01f3, B:61:0x01fc, B:64:0x0200, B:75:0x01f9, B:77:0x0206, B:86:0x00d6, B:131:0x0210), top: B:2:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void createCache() {
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr;
        CountDownLatch[] countDownLatchArr;
        ArrayList arrayList;
        AtomicBoolean atomicBoolean;
        int i;
        int i2;
        CountDownLatch countDownLatch;
        int i3;
        try {
            try {
                long j = 0;
                int i4 = 0;
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
                        final Bitmap[] bitmapArr = sharedTools.bitmap;
                        immutableByteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                        countDownLatchArr = new CountDownLatch[N];
                        arrayList = new ArrayList();
                        randomAccessFile2.writeBoolean(false);
                        randomAccessFile2.writeInt(0);
                        atomicBoolean = new AtomicBoolean(false);
                        this.source.prepareForGenerateCache();
                        i = 0;
                        i2 = 0;
                        while (true) {
                            countDownLatch = countDownLatchArr[i];
                            if (countDownLatch != null) {
                            }
                            if (this.cancelled.get()) {
                                break;
                            } else {
                                break;
                            }
                            this.framesProcessed.set(r1);
                            i2 = r1;
                            atomicBoolean = r13;
                            arrayList = r19;
                            countDownLatchArr = r20;
                            immutableByteArrayOutputStreamArr = r21;
                            randomAccessFile2 = r22;
                            i4 = 0;
                            j = 0;
                        }
                        AtomicBoolean atomicBoolean2 = atomicBoolean;
                        CountDownLatch[] countDownLatchArr2 = countDownLatchArr;
                        RandomAccessFile randomAccessFile3 = randomAccessFile2;
                        if (BuildVars.DEBUG_VERSION) {
                        }
                        atomicBoolean2.set(true);
                        while (i3 < N) {
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
                            this.checked = true;
                            if (this.cachedFile != randomAccessFile) {
                                try {
                                    randomAccessFile.close();
                                } catch (Throwable unused5) {
                                }
                            }
                        }
                        this.fileExist = false;
                        this.cacheCreated = false;
                        this.checked = true;
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
                final Bitmap[] bitmapArr2 = sharedTools.bitmap;
                immutableByteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                countDownLatchArr = new CountDownLatch[N];
                arrayList = new ArrayList();
                randomAccessFile2.writeBoolean(false);
                randomAccessFile2.writeInt(0);
                atomicBoolean = new AtomicBoolean(false);
                this.source.prepareForGenerateCache();
                i = 0;
                i2 = 0;
                while (true) {
                    countDownLatch = countDownLatchArr[i];
                    if (countDownLatch != null) {
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.cancelled.get() || atomicBoolean.get()) {
                        break;
                    }
                    if (this.source.getNextFrame(bitmapArr2[i]) != 1) {
                        for (int i5 = 0; i5 < N; i5++) {
                            CountDownLatch countDownLatch2 = countDownLatchArr[i5];
                            if (countDownLatch2 != null) {
                                try {
                                    countDownLatch2.await();
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                        int length = (int) randomAccessFile2.length();
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda1
                            @Override // j$.util.function.ToIntFunction
                            public final int applyAsInt(Object obj) {
                                int i6;
                                i6 = ((BitmapsCache.FrameOffset) obj).index;
                                return i6;
                            }
                        }));
                        immutableByteArrayOutputStreamArr[i4].reset();
                        int size = arrayList.size();
                        immutableByteArrayOutputStreamArr[i4].writeInt(size);
                        for (int i6 = 0; i6 < arrayList.size(); i6++) {
                            immutableByteArrayOutputStreamArr[i4].writeInt(((FrameOffset) arrayList.get(i6)).frameOffset);
                            immutableByteArrayOutputStreamArr[i4].writeInt(((FrameOffset) arrayList.get(i6)).frameSize);
                        }
                        randomAccessFile2.write(immutableByteArrayOutputStreamArr[i4].buf, i4, (size * 8) + 4);
                        immutableByteArrayOutputStreamArr[i4].reset();
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
                        this.checked = true;
                    } else {
                        countDownLatchArr[i] = new CountDownLatch(1);
                        final AtomicBoolean atomicBoolean3 = atomicBoolean;
                        AtomicBoolean atomicBoolean4 = atomicBoolean;
                        final int i7 = i;
                        final ArrayList arrayList2 = arrayList;
                        final ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr2 = immutableByteArrayOutputStreamArr;
                        final CountDownLatch[] countDownLatchArr3 = countDownLatchArr;
                        final int i8 = i2;
                        ImmutableByteArrayOutputStream[] immutableByteArrayOutputStreamArr3 = immutableByteArrayOutputStreamArr;
                        final RandomAccessFile randomAccessFile4 = randomAccessFile2;
                        RandomAccessFile randomAccessFile5 = randomAccessFile2;
                        bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.this.lambda$createCache$1(atomicBoolean3, bitmapArr2, i7, immutableByteArrayOutputStreamArr2, i8, randomAccessFile4, arrayList2, countDownLatchArr3);
                            }
                        });
                        int i9 = i + 1;
                        int i10 = i2 + 1;
                        i = i9 >= N ? 0 : i9;
                        this.framesProcessed.set(i10);
                        i2 = i10;
                        atomicBoolean = atomicBoolean4;
                        arrayList = arrayList2;
                        countDownLatchArr = countDownLatchArr3;
                        immutableByteArrayOutputStreamArr = immutableByteArrayOutputStreamArr3;
                        randomAccessFile2 = randomAccessFile5;
                        i4 = 0;
                        j = 0;
                    }
                }
                AtomicBoolean atomicBoolean22 = atomicBoolean;
                CountDownLatch[] countDownLatchArr22 = countDownLatchArr;
                RandomAccessFile randomAccessFile32 = randomAccessFile2;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("cancelled cache generation");
                }
                atomicBoolean22.set(true);
                for (i3 = 0; i3 < N; i3++) {
                    CountDownLatch countDownLatch3 = countDownLatchArr22[i3];
                    if (countDownLatch3 != null) {
                        try {
                            countDownLatch3.await();
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                    }
                    Bitmap bitmap = bitmapArr2[i3];
                    if (bitmap != null) {
                        try {
                            bitmap.recycle();
                        } catch (Exception unused6) {
                        }
                    }
                }
                randomAccessFile32.close();
                this.source.releaseForGenerateCache();
            } finally {
                this.source.releaseForGenerateCache();
            }
        } catch (FileNotFoundException | IOException e4) {
            e4.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getFrame(int i, Bitmap bitmap) {
        RandomAccessFile randomAccessFile;
        int i2;
        if (this.error) {
            return -1;
        }
        RandomAccessFile randomAccessFile2 = null;
        try {
            if (!this.cacheCreated && !this.fileExist) {
                return -1;
            }
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
                        this.checked = true;
                    }
                    if (!this.cacheCreated) {
                        randomAccessFile.close();
                        return -1;
                    }
                } catch (FileNotFoundException unused) {
                    if (this.error && randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return -1;
                } catch (Throwable th) {
                    th = th;
                    FileLog.e(th, false);
                    i2 = this.tryCount + 1;
                    this.tryCount = i2;
                    if (i2 > 10) {
                        this.error = true;
                    }
                    if (this.error) {
                        randomAccessFile.close();
                    }
                    return -1;
                }
            }
            try {
                if (this.frameOffsets.size() == 0) {
                    return -1;
                }
                FrameOffset frameOffset = (FrameOffset) this.frameOffsets.get(Utilities.clamp(i, this.frameOffsets.size() - 1, 0));
                randomAccessFile.seek(frameOffset.frameOffset);
                byte[] buffer = getBuffer(frameOffset);
                randomAccessFile.readFully(buffer, 0, frameOffset.frameSize);
                if (this.recycled) {
                    this.cachedFile = null;
                    randomAccessFile.close();
                } else {
                    if (this.cachedFile != randomAccessFile) {
                        closeCachedFile();
                    }
                    this.cachedFile = randomAccessFile;
                }
                if (this.options == null) {
                    this.options = new BitmapFactory.Options();
                }
                BitmapFactory.Options options = this.options;
                options.inBitmap = bitmap;
                BitmapFactory.decodeByteArray(buffer, 0, frameOffset.frameSize, options);
                this.options.inBitmap = null;
                return 0;
            } catch (FileNotFoundException unused2) {
                randomAccessFile2 = randomAccessFile;
                randomAccessFile = randomAccessFile2;
                if (this.error) {
                }
                return -1;
            } catch (Throwable th2) {
                th = th2;
                randomAccessFile2 = randomAccessFile;
                randomAccessFile = randomAccessFile2;
                FileLog.e(th, false);
                i2 = this.tryCount + 1;
                this.tryCount = i2;
                if (i2 > 10) {
                }
                if (this.error) {
                }
                return -1;
            }
        } catch (FileNotFoundException unused3) {
        } catch (Throwable th3) {
            th = th3;
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

    public int getFrameCount() {
        return this.frameOffsets.size();
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
}
