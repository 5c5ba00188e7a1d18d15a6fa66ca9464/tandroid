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
    volatile boolean checkCache;
    int compressQuality;
    boolean error;
    final File file;
    boolean fileExist;
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
    private static final int N = Utilities.clamp(Runtime.getRuntime().availableProcessors(), 8, 1);
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

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x00ed -> B:28:0x00fd). Please submit an issue!!! */
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
        boolean exists = file3.exists();
        this.fileExist = exists;
        if (exists) {
            try {
                try {
                    randomAccessFile = new RandomAccessFile(file3, "r");
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                        if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                            randomAccessFile.seek(randomAccessFile.readInt());
                            int readInt = randomAccessFile.readInt();
                            fillFrames(randomAccessFile, readInt);
                            if (readInt == 0) {
                                file3.delete();
                                this.cacheCreated = false;
                                this.fileExist = false;
                            }
                        }
                        randomAccessFile.close();
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            th.printStackTrace();
                            this.file.delete();
                            this.fileExist = false;
                            if (randomAccessFile == null) {
                                return;
                            }
                            randomAccessFile.close();
                        } catch (Throwable th3) {
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            throw th3;
                        }
                    }
                } catch (Throwable th4) {
                    randomAccessFile = null;
                    th = th4;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
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

    /* JADX WARN: Code restructure failed: missing block: B:106:0x005e, code lost:
        if (r0 == null) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void createCache() {
        RandomAccessFile randomAccessFile;
        try {
            try {
                System.currentTimeMillis();
                boolean z = true;
                int i = 0;
                if (this.file.exists()) {
                    try {
                        randomAccessFile = new RandomAccessFile(this.file, "r");
                    } catch (Throwable unused) {
                        randomAccessFile = null;
                    }
                    try {
                        this.cacheCreated = randomAccessFile.readBoolean();
                        if (randomAccessFile.readInt() == 0) {
                            this.cacheCreated = false;
                        }
                    } catch (Throwable unused2) {
                        try {
                            this.file.delete();
                        } catch (Throwable unused3) {
                        }
                    }
                    if (this.cacheCreated) {
                        this.frameOffsets.clear();
                        randomAccessFile.seek(randomAccessFile.readInt());
                        fillFrames(randomAccessFile, randomAccessFile.readInt());
                        randomAccessFile.close();
                        this.fileExist = true;
                        try {
                            randomAccessFile.close();
                        } catch (Throwable unused4) {
                        }
                    }
                    this.file.delete();
                    try {
                        randomAccessFile.close();
                    } catch (Throwable unused5) {
                    }
                }
                final RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                if (sharedTools == null) {
                    sharedTools = new CacheGeneratorSharedTools();
                }
                sharedTools.allocate(this.h, this.w);
                final Bitmap[] bitmapArr = sharedTools.bitmap;
                ByteArrayOutputStream[] byteArrayOutputStreamArr = sharedTools.byteArrayOutputStream;
                CountDownLatch[] countDownLatchArr = new CountDownLatch[N];
                ArrayList arrayList = new ArrayList();
                randomAccessFile2.writeBoolean(false);
                randomAccessFile2.writeInt(0);
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                this.source.prepareForGenerateCache();
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    if (countDownLatchArr[i2] != null) {
                        try {
                            countDownLatchArr[i2].await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.cancelled.get() || atomicBoolean.get()) {
                        break;
                    }
                    System.currentTimeMillis();
                    if (this.source.getNextFrame(bitmapArr[i2]) == z) {
                        System.currentTimeMillis();
                        int i4 = z ? 1 : 0;
                        int i5 = z ? 1 : 0;
                        countDownLatchArr[i2] = new CountDownLatch(i4);
                        final AtomicBoolean atomicBoolean2 = atomicBoolean;
                        final int i6 = i2;
                        final ByteArrayOutputStream[] byteArrayOutputStreamArr2 = byteArrayOutputStreamArr;
                        AtomicBoolean atomicBoolean3 = atomicBoolean;
                        final int i7 = i3;
                        final ArrayList arrayList2 = arrayList;
                        final CountDownLatch[] countDownLatchArr2 = countDownLatchArr;
                        ByteArrayOutputStream[] byteArrayOutputStreamArr3 = byteArrayOutputStreamArr;
                        bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.this.lambda$createCache$1(atomicBoolean2, bitmapArr, i6, byteArrayOutputStreamArr2, i7, randomAccessFile2, arrayList2, countDownLatchArr2);
                            }
                        });
                        int i8 = i2 + 1;
                        i3++;
                        i2 = i8 >= N ? 0 : i8;
                        atomicBoolean = atomicBoolean3;
                        arrayList = arrayList2;
                        countDownLatchArr = countDownLatchArr2;
                        byteArrayOutputStreamArr = byteArrayOutputStreamArr3;
                        z = true;
                        i = 0;
                    } else {
                        for (int i9 = 0; i9 < N; i9++) {
                            if (countDownLatchArr[i9] != null) {
                                try {
                                    countDownLatchArr[i9].await();
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                        int length = (int) randomAccessFile2.length();
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(BitmapsCache$$ExternalSyntheticLambda2.INSTANCE));
                        byteArrayOutputStreamArr[i].reset();
                        int size = arrayList.size();
                        byteArrayOutputStreamArr[i].writeInt(size);
                        for (int i10 = 0; i10 < arrayList.size(); i10++) {
                            byteArrayOutputStreamArr[i].writeInt(((FrameOffset) arrayList.get(i10)).frameOffset);
                            byteArrayOutputStreamArr[i].writeInt(((FrameOffset) arrayList.get(i10)).frameSize);
                        }
                        randomAccessFile2.write(byteArrayOutputStreamArr[i].buf, i, (size * 8) + 4);
                        byteArrayOutputStreamArr[i].reset();
                        randomAccessFile2.seek(0L);
                        randomAccessFile2.writeBoolean(z);
                        randomAccessFile2.writeInt(length);
                        atomicBoolean.set(z);
                        randomAccessFile2.close();
                        this.frameOffsets.clear();
                        this.fileExist = z;
                    }
                }
                AtomicBoolean atomicBoolean4 = atomicBoolean;
                CountDownLatch[] countDownLatchArr3 = countDownLatchArr;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("cancelled cache generation");
                }
                atomicBoolean4.set(true);
                for (int i11 = 0; i11 < N; i11++) {
                    if (countDownLatchArr3[i11] != null) {
                        try {
                            countDownLatchArr3[i11].await();
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (bitmapArr[i11] != null) {
                        try {
                            bitmapArr[i11].recycle();
                        } catch (Exception unused6) {
                        }
                    }
                }
                randomAccessFile2.close();
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

    public boolean cacheExist() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        if (this.checkCache) {
            return this.cacheCreated;
        }
        RandomAccessFile randomAccessFile2 = null;
        try {
            try {
                try {
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception unused) {
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
        }
        synchronized (this.mutex) {
            try {
                RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.file, "r");
                this.cacheCreated = randomAccessFile3.readBoolean();
                if (randomAccessFile3.readInt() <= 0) {
                    this.cacheCreated = false;
                }
                randomAccessFile3.close();
                this.checkCache = true;
                return this.cacheCreated;
            } catch (Throwable th4) {
                randomAccessFile = null;
                th = th4;
                try {
                    throw th;
                } catch (Exception unused2) {
                    randomAccessFile2 = null;
                    if (randomAccessFile2 != null) {
                        randomAccessFile2.close();
                    }
                    this.checkCache = true;
                    return this.cacheCreated;
                } catch (Throwable th5) {
                    th = th5;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        }
    }

    public int getFrame(int i, Bitmap bitmap) {
        RandomAccessFile randomAccessFile;
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
            FrameOffset frameOffset = this.frameOffsets.get(Utilities.clamp(i, this.frameOffsets.size() - 1, 0));
            randomAccessFile.seek(frameOffset.frameOffset);
            byte[] buffer = getBuffer(frameOffset);
            randomAccessFile.readFully(buffer, 0, frameOffset.frameSize);
            if (!this.recycled) {
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
            return 0;
        } catch (FileNotFoundException unused2) {
        } catch (Throwable th2) {
            th = th2;
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
        return !this.cacheCreated || !this.fileExist;
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
                if (i <= 2147483639) {
                    return 2147483639;
                }
                return ConnectionsManager.DEFAULT_DATACENTER_ID;
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
            if (!arrayList.isEmpty()) {
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$CacheGeneratorSharedTools$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        BitmapsCache.CacheGeneratorSharedTools.lambda$release$1(arrayList);
                    }
                });
            }
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
