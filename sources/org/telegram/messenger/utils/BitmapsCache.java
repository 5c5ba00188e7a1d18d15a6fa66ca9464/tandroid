package org.telegram.messenger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes.dex */
public class BitmapsCache {
    private static final int N = Utilities.clamp(Runtime.getRuntime().availableProcessors() - 2, 8, 1);
    private static ThreadPoolExecutor bitmapCompressExecutor;
    byte[] bufferTmp;
    volatile boolean cacheCreated;
    RandomAccessFile cachedFile;
    volatile boolean checkCache;
    int compressQuality;
    boolean error;
    final File file;
    String fileName;
    private int frameIndex;
    int h;
    BitmapFactory.Options options;
    volatile boolean recycled;
    private final Cacheable source;
    int w;
    ArrayList<FrameOffset> frameOffsets = new ArrayList<>();
    private final Object mutex = new Object();
    public AtomicBoolean cancelled = new AtomicBoolean(false);

    /* loaded from: classes.dex */
    public static class CacheOptions {
        public int compressQuality = 100;
        public boolean fallback = false;
    }

    /* loaded from: classes.dex */
    public interface Cacheable {
        Bitmap getFirstFrame(Bitmap bitmap);

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

    public BitmapsCache(File file, Cacheable cacheable, CacheOptions cacheOptions, int i, int i2, boolean z) {
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
        this.file = new File(file2, sb.toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00ae, code lost:
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b1, code lost:
        if (r8 >= org.telegram.messenger.utils.BitmapsCache.N) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00b5, code lost:
        if (r9[r8] == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b7, code lost:
        r9[r8].await();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00bd, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00be, code lost:
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d7, code lost:
        r0 = (int) r14.length();
        java.util.Collections.sort(r7, j$.util.Comparator$-CC.comparingInt(org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda1.INSTANCE));
        r14.writeInt(r7.size());
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00f1, code lost:
        if (r8 >= r7.size()) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00f3, code lost:
        r14.writeInt(((org.telegram.messenger.utils.BitmapsCache.FrameOffset) r7.get(r8)).frameOffset);
        r14.writeInt(((org.telegram.messenger.utils.BitmapsCache.FrameOffset) r7.get(r8)).frameSize);
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x010c, code lost:
        r14.seek(r4);
        r14.writeBoolean(true);
        r14.writeInt(r0);
        r6.set(true);
        r14.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x011e, code lost:
        if (org.telegram.messenger.BuildVars.DEBUG_VERSION == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0120, code lost:
        org.telegram.messenger.FileLog.d("generate cache for time = " + (java.lang.System.currentTimeMillis() - r12) + " drawFrameTime = " + r1 + " comressQuality = " + r11.compressQuality + " fileSize = " + org.telegram.messenger.AndroidUtilities.formatFileSize(r11.file.length()) + " " + r11.fileName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0167, code lost:
        r11.source.releaseForGenerateCache();
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:?, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ce A[Catch: all -> 0x0211, IOException -> 0x0214, FileNotFoundException -> 0x021a, TryCatch #11 {all -> 0x0211, blocks: (B:3:0x0004, B:119:0x0017, B:121:0x0021, B:123:0x002a, B:5:0x002f, B:6:0x0043, B:8:0x0047, B:10:0x0065, B:11:0x0081, B:114:0x0085, B:13:0x008f, B:30:0x00af, B:32:0x00b3, B:47:0x00b7, B:34:0x00c1, B:43:0x00c5, B:36:0x00ca, B:38:0x00ce, B:40:0x00d3, B:50:0x00be, B:52:0x00d7, B:53:0x00ed, B:55:0x00f3, B:57:0x010c, B:59:0x0120, B:117:0x008c), top: B:2:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void createCache() {
        BitmapsCache bitmapsCache;
        BitmapsCache bitmapsCache2;
        Cacheable cacheable;
        int i;
        BitmapsCache bitmapsCache3 = this;
        try {
            try {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(bitmapsCache3.file, "rw");
                    try {
                        if (bitmapsCache3.file.exists()) {
                            try {
                                bitmapsCache3.cacheCreated = randomAccessFile.readBoolean();
                            } catch (Exception unused) {
                            }
                            if (bitmapsCache3.cacheCreated) {
                                randomAccessFile.close();
                                cacheable = bitmapsCache3.source;
                                cacheable.releaseForGenerateCache();
                                return;
                            }
                            bitmapsCache3.file.delete();
                        }
                        bitmapsCache.source.releaseForGenerateCache();
                        cacheable = bitmapsCache.source;
                        cacheable.releaseForGenerateCache();
                        return;
                    } catch (FileNotFoundException e) {
                        e = e;
                        e.printStackTrace();
                        bitmapsCache.source.releaseForGenerateCache();
                        return;
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        bitmapsCache.source.releaseForGenerateCache();
                        return;
                    }
                    randomAccessFile.close();
                    final RandomAccessFile randomAccessFile2 = new RandomAccessFile(bitmapsCache3.file, "rw");
                    int i2 = N;
                    final Bitmap[] bitmapArr = new Bitmap[i2];
                    ByteArrayOutputStream[] byteArrayOutputStreamArr = new ByteArrayOutputStream[i2];
                    CountDownLatch[] countDownLatchArr = new CountDownLatch[i2];
                    for (int i3 = 0; i3 < N; i3++) {
                        bitmapArr[i3] = Bitmap.createBitmap(bitmapsCache3.w, bitmapsCache3.h, Bitmap.Config.ARGB_8888);
                        byteArrayOutputStreamArr[i3] = new ByteArrayOutputStream(bitmapsCache3.w * bitmapsCache3.h * 2);
                    }
                    ArrayList arrayList = new ArrayList();
                    randomAccessFile2.writeBoolean(false);
                    randomAccessFile2.writeInt(0);
                    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    bitmapsCache3.source.prepareForGenerateCache();
                    long j = 0;
                    long j2 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    while (true) {
                        if (countDownLatchArr[i4] != null) {
                            try {
                                countDownLatchArr[i4].await();
                            } catch (InterruptedException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (bitmapsCache3.cancelled.get()) {
                            break;
                        }
                        try {
                            try {
                                if (atomicBoolean.get()) {
                                    break;
                                }
                                long currentTimeMillis2 = System.currentTimeMillis();
                                if (bitmapsCache3.source.getNextFrame(bitmapArr[i4]) != 1) {
                                    break;
                                }
                                long currentTimeMillis3 = j2 + (System.currentTimeMillis() - currentTimeMillis2);
                                countDownLatchArr[i4] = new CountDownLatch(1);
                                final AtomicBoolean atomicBoolean2 = atomicBoolean;
                                long j3 = j;
                                final int i6 = i4;
                                long j4 = currentTimeMillis;
                                AtomicBoolean atomicBoolean3 = atomicBoolean;
                                final ByteArrayOutputStream[] byteArrayOutputStreamArr2 = byteArrayOutputStreamArr;
                                final ArrayList arrayList2 = arrayList;
                                final int i7 = i5;
                                final CountDownLatch[] countDownLatchArr2 = countDownLatchArr;
                                ByteArrayOutputStream[] byteArrayOutputStreamArr3 = byteArrayOutputStreamArr;
                                bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        BitmapsCache.this.lambda$createCache$0(atomicBoolean2, bitmapArr, i6, byteArrayOutputStreamArr2, i7, randomAccessFile2, arrayList2, countDownLatchArr2);
                                    }
                                });
                                int i8 = i4 + 1;
                                i5++;
                                i4 = i8 >= N ? 0 : i8;
                                bitmapsCache3 = this;
                                atomicBoolean = atomicBoolean3;
                                arrayList = arrayList2;
                                j2 = currentTimeMillis3;
                                j = j3;
                                currentTimeMillis = j4;
                                countDownLatchArr = countDownLatchArr2;
                                byteArrayOutputStreamArr = byteArrayOutputStreamArr3;
                            } catch (Throwable th) {
                                th = th;
                                bitmapsCache2 = this;
                                bitmapsCache2.source.releaseForGenerateCache();
                                throw th;
                            }
                        } catch (FileNotFoundException e4) {
                            e = e4;
                            bitmapsCache = this;
                            e.printStackTrace();
                            bitmapsCache.source.releaseForGenerateCache();
                            return;
                        } catch (IOException e5) {
                            e = e5;
                            bitmapsCache = this;
                            e.printStackTrace();
                            bitmapsCache.source.releaseForGenerateCache();
                            return;
                        }
                    }
                    AtomicBoolean atomicBoolean4 = atomicBoolean;
                    CountDownLatch[] countDownLatchArr3 = countDownLatchArr;
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("cancelled cache generation");
                    }
                    atomicBoolean4.set(true);
                    for (int i9 = 0; i9 < N; i9++) {
                        if (countDownLatchArr3[i9] != null) {
                            try {
                                countDownLatchArr3[i9].await();
                            } catch (InterruptedException e6) {
                                e6.printStackTrace();
                            }
                        }
                        if (bitmapArr[i9] != null) {
                            try {
                                bitmapArr[i9].recycle();
                            } catch (Exception unused2) {
                            }
                        }
                    }
                    randomAccessFile2.close();
                    bitmapsCache = this;
                    if (bitmapArr[i] != null) {
                        try {
                            bitmapArr[i].recycle();
                        } catch (Exception unused3) {
                        }
                    }
                    if (byteArrayOutputStreamArr[i] == null) {
                        byteArrayOutputStreamArr[i].buf = null;
                    }
                    i++;
                    if (byteArrayOutputStreamArr[i] == null) {
                    }
                    i++;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                bitmapsCache2 = bitmapsCache3;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            bitmapsCache = bitmapsCache3;
        } catch (IOException e8) {
            e = e8;
            bitmapsCache = bitmapsCache3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCache$0(AtomicBoolean atomicBoolean, Bitmap[] bitmapArr, int i, ByteArrayOutputStream[] byteArrayOutputStreamArr, int i2, RandomAccessFile randomAccessFile, ArrayList arrayList, CountDownLatch[] countDownLatchArr) {
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
                randomAccessFile3.close();
                this.checkCache = false;
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
                    this.checkCache = false;
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

    /* JADX WARN: Removed duplicated region for block: B:32:0x00df A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getFrame(int i, Bitmap bitmap) {
        RandomAccessFile randomAccessFile;
        if (this.error) {
            return -1;
        }
        RandomAccessFile randomAccessFile2 = null;
        try {
            synchronized (this.mutex) {
                try {
                    if (!this.cacheCreated || (randomAccessFile = this.cachedFile) == null) {
                        randomAccessFile = new RandomAccessFile(this.file, "r");
                        try {
                            this.cacheCreated = randomAccessFile.readBoolean();
                            if (this.cacheCreated && this.frameOffsets.isEmpty()) {
                                randomAccessFile.seek(randomAccessFile.readInt());
                                int readInt = randomAccessFile.readInt();
                                for (int i2 = 0; i2 < readInt; i2++) {
                                    FrameOffset frameOffset = new FrameOffset(i2);
                                    frameOffset.frameOffset = randomAccessFile.readInt();
                                    frameOffset.frameSize = randomAccessFile.readInt();
                                    this.frameOffsets.add(frameOffset);
                                }
                            }
                            if (!this.cacheCreated) {
                                randomAccessFile.close();
                                this.source.getFirstFrame(bitmap);
                                return 0;
                            } else if (this.frameOffsets.isEmpty()) {
                                randomAccessFile.close();
                                return -1;
                            }
                        } catch (Throwable th) {
                            th = th;
                            randomAccessFile2 = randomAccessFile;
                            throw th;
                        }
                    }
                    FrameOffset frameOffset2 = this.frameOffsets.get(Utilities.clamp(i, this.frameOffsets.size() - 1, 0));
                    randomAccessFile.seek(frameOffset2.frameOffset);
                    byte[] bArr = this.bufferTmp;
                    if (bArr == null || bArr.length < frameOffset2.frameSize) {
                        this.bufferTmp = new byte[(int) (frameOffset2.frameSize * 1.3f)];
                    }
                    randomAccessFile.readFully(this.bufferTmp, 0, frameOffset2.frameSize);
                    if (!this.recycled) {
                        this.cachedFile = randomAccessFile;
                    } else {
                        this.cachedFile = null;
                        randomAccessFile.close();
                    }
                    try {
                        if (this.options == null) {
                            this.options = new BitmapFactory.Options();
                        }
                        BitmapFactory.Options options = this.options;
                        options.inBitmap = bitmap;
                        BitmapFactory.decodeByteArray(this.bufferTmp, 0, frameOffset2.frameSize, options);
                        return 0;
                    } catch (FileNotFoundException unused) {
                        randomAccessFile2 = randomAccessFile;
                        if (randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return -1;
                    } catch (Throwable th2) {
                        th = th2;
                        randomAccessFile2 = randomAccessFile;
                        FileLog.e(th);
                        if (randomAccessFile2 != null) {
                        }
                        return -1;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
        } catch (FileNotFoundException unused2) {
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public boolean needGenCache() {
        return !this.cacheCreated;
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
}
