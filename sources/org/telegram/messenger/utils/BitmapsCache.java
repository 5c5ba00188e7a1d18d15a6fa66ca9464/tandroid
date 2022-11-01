package org.telegram.messenger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import j$.util.Comparator$-CC;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
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

    /* JADX WARN: Code restructure failed: missing block: B:118:0x0048, code lost:
        if (r0 == null) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void createCache() {
        RandomAccessFile randomAccessFile;
        try {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                byte[] bArr = null;
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
                        randomAccessFile.close();
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
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.file, "rw");
                int i = N;
                Bitmap[] bitmapArr = new Bitmap[i];
                ByteArrayOutputStream[] byteArrayOutputStreamArr = new ByteArrayOutputStream[i];
                CountDownLatch[] countDownLatchArr = new CountDownLatch[i];
                for (int i2 = 0; i2 < N; i2++) {
                    bitmapArr[i2] = Bitmap.createBitmap(this.w, this.h, Bitmap.Config.ARGB_8888);
                    byteArrayOutputStreamArr[i2] = new ByteArrayOutputStream(this.w * this.h * 2);
                }
                ArrayList arrayList = new ArrayList();
                randomAccessFile2.writeBoolean(false);
                randomAccessFile2.writeInt(0);
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                this.source.prepareForGenerateCache();
                long j = 0;
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    if (countDownLatchArr[i3] != null) {
                        try {
                            countDownLatchArr[i3].await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.cancelled.get() || atomicBoolean.get()) {
                        break;
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    if (this.source.getNextFrame(bitmapArr[i3]) == 1) {
                        long currentTimeMillis3 = j + (System.currentTimeMillis() - currentTimeMillis2);
                        countDownLatchArr[i3] = new CountDownLatch(1);
                        final AtomicBoolean atomicBoolean2 = atomicBoolean;
                        final Bitmap[] bitmapArr2 = bitmapArr;
                        AtomicBoolean atomicBoolean3 = atomicBoolean;
                        final int i5 = i3;
                        final ArrayList arrayList2 = arrayList;
                        final ByteArrayOutputStream[] byteArrayOutputStreamArr2 = byteArrayOutputStreamArr;
                        final CountDownLatch[] countDownLatchArr2 = countDownLatchArr;
                        final int i6 = i4;
                        ByteArrayOutputStream[] byteArrayOutputStreamArr3 = byteArrayOutputStreamArr;
                        final RandomAccessFile randomAccessFile3 = randomAccessFile2;
                        Bitmap[] bitmapArr3 = bitmapArr;
                        RandomAccessFile randomAccessFile4 = randomAccessFile2;
                        bitmapCompressExecutor.execute(new Runnable() { // from class: org.telegram.messenger.utils.BitmapsCache$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                BitmapsCache.this.lambda$createCache$0(atomicBoolean2, bitmapArr2, i5, byteArrayOutputStreamArr2, i6, randomAccessFile3, arrayList2, countDownLatchArr2);
                            }
                        });
                        int i7 = i3 + 1;
                        i4++;
                        i3 = i7 >= N ? 0 : i7;
                        atomicBoolean = atomicBoolean3;
                        j = currentTimeMillis3;
                        arrayList = arrayList2;
                        countDownLatchArr = countDownLatchArr2;
                        byteArrayOutputStreamArr = byteArrayOutputStreamArr3;
                        bitmapArr = bitmapArr3;
                        randomAccessFile2 = randomAccessFile4;
                        bArr = null;
                    } else {
                        for (int i8 = 0; i8 < N; i8++) {
                            if (countDownLatchArr[i8] != null) {
                                try {
                                    countDownLatchArr[i8].await();
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (bitmapArr[i8] != null) {
                                try {
                                    bitmapArr[i8].recycle();
                                } catch (Exception unused6) {
                                }
                            }
                            if (byteArrayOutputStreamArr[i8] != null) {
                                byteArrayOutputStreamArr[i8].buf = bArr;
                            }
                        }
                        int length = (int) randomAccessFile2.length();
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(BitmapsCache$$ExternalSyntheticLambda1.INSTANCE));
                        randomAccessFile2.writeInt(arrayList.size());
                        for (int i9 = 0; i9 < arrayList.size(); i9++) {
                            randomAccessFile2.writeInt(((FrameOffset) arrayList.get(i9)).frameOffset);
                            randomAccessFile2.writeInt(((FrameOffset) arrayList.get(i9)).frameSize);
                        }
                        randomAccessFile2.seek(0L);
                        randomAccessFile2.writeBoolean(true);
                        randomAccessFile2.writeInt(length);
                        atomicBoolean.set(true);
                        randomAccessFile2.close();
                        if (BuildVars.DEBUG_VERSION) {
                            FileLog.d("generate cache for time = " + (System.currentTimeMillis() - currentTimeMillis) + " drawFrameTime = " + j + " comressQuality = " + this.compressQuality + " fileSize = " + AndroidUtilities.formatFileSize(this.file.length()) + " " + this.fileName);
                        }
                    }
                }
                AtomicBoolean atomicBoolean4 = atomicBoolean;
                CountDownLatch[] countDownLatchArr3 = countDownLatchArr;
                Bitmap[] bitmapArr4 = bitmapArr;
                RandomAccessFile randomAccessFile5 = randomAccessFile2;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("cancelled cache generation");
                }
                atomicBoolean4.set(true);
                for (int i10 = 0; i10 < N; i10++) {
                    if (countDownLatchArr3[i10] != null) {
                        try {
                            countDownLatchArr3[i10].await();
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (bitmapArr4[i10] != null) {
                        try {
                            bitmapArr4[i10].recycle();
                        } catch (Exception unused7) {
                        }
                    }
                }
                randomAccessFile5.close();
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
                        FileLog.e(th, false);
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
