package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.upstream.Loader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public abstract class SntpClient {
    private static long elapsedRealtimeOffsetMs = 0;
    private static boolean isInitialized = false;
    private static String ntpHost = "time.android.com";
    private static final Object loaderLock = new Object();
    private static final Object valueLock = new Object();

    /* loaded from: classes.dex */
    public interface InitializationCallback {
        void onInitializationFailed(IOException iOException);

        void onInitialized();
    }

    /* loaded from: classes.dex */
    private static final class NtpTimeCallback implements Loader.Callback {
        private final InitializationCallback callback;

        public NtpTimeCallback(InitializationCallback initializationCallback) {
            this.callback = initializationCallback;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCanceled(Loader.Loadable loadable, long j, long j2, boolean z) {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCompleted(Loader.Loadable loadable, long j, long j2) {
            if (this.callback != null) {
                if (SntpClient.isInitialized()) {
                    this.callback.onInitialized();
                } else {
                    this.callback.onInitializationFailed(new IOException(new ConcurrentModificationException()));
                }
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public Loader.LoadErrorAction onLoadError(Loader.Loadable loadable, long j, long j2, IOException iOException, int i) {
            InitializationCallback initializationCallback = this.callback;
            if (initializationCallback != null) {
                initializationCallback.onInitializationFailed(iOException);
            }
            return Loader.DONT_RETRY;
        }
    }

    /* loaded from: classes.dex */
    private static final class NtpTimeLoadable implements Loader.Loadable {
        private NtpTimeLoadable() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
        public void cancelLoad() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
        public void load() {
            synchronized (SntpClient.loaderLock) {
                synchronized (SntpClient.valueLock) {
                    if (SntpClient.isInitialized) {
                        return;
                    }
                    long access$400 = SntpClient.access$400();
                    synchronized (SntpClient.valueLock) {
                        long unused = SntpClient.elapsedRealtimeOffsetMs = access$400;
                        boolean unused2 = SntpClient.isInitialized = true;
                    }
                }
            }
        }
    }

    static /* synthetic */ long access$400() {
        return loadNtpTimeOffsetMs();
    }

    private static void checkValidServerReply(byte b, byte b2, int i, long j) {
        if (b == 3) {
            throw new IOException("SNTP: Unsynchronized server");
        }
        if (b2 != 4 && b2 != 5) {
            throw new IOException("SNTP: Untrusted mode: " + ((int) b2));
        }
        if (i != 0 && i <= 15) {
            if (j == 0) {
                throw new IOException("SNTP: Zero transmitTime");
            }
        } else {
            throw new IOException("SNTP: Untrusted stratum: " + i);
        }
    }

    public static long getElapsedRealtimeOffsetMs() {
        long j;
        synchronized (valueLock) {
            try {
                j = isInitialized ? elapsedRealtimeOffsetMs : -9223372036854775807L;
            } catch (Throwable th) {
                throw th;
            }
        }
        return j;
    }

    public static String getNtpHost() {
        String str;
        synchronized (valueLock) {
            str = ntpHost;
        }
        return str;
    }

    public static void initialize(Loader loader, InitializationCallback initializationCallback) {
        if (isInitialized()) {
            if (initializationCallback != null) {
                initializationCallback.onInitialized();
            }
        } else {
            if (loader == null) {
                loader = new Loader("SntpClient");
            }
            loader.startLoading(new NtpTimeLoadable(), new NtpTimeCallback(initializationCallback), 1);
        }
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (valueLock) {
            z = isInitialized;
        }
        return z;
    }

    private static long loadNtpTimeOffsetMs() {
        InetAddress byName = InetAddress.getByName(getNtpHost());
        DatagramSocket datagramSocket = new DatagramSocket();
        try {
            datagramSocket.setSoTimeout(10000);
            byte[] bArr = new byte[48];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, 48, byName, 123);
            bArr[0] = 27;
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            writeTimestamp(bArr, 40, currentTimeMillis);
            datagramSocket.send(datagramPacket);
            datagramSocket.receive(new DatagramPacket(bArr, 48));
            long elapsedRealtime2 = android.os.SystemClock.elapsedRealtime();
            long j = currentTimeMillis + (elapsedRealtime2 - elapsedRealtime);
            byte b = bArr[0];
            int i = bArr[1] & 255;
            long readTimestamp = readTimestamp(bArr, 24);
            long readTimestamp2 = readTimestamp(bArr, 32);
            long readTimestamp3 = readTimestamp(bArr, 40);
            checkValidServerReply((byte) ((b >> 6) & 3), (byte) (b & 7), i, readTimestamp3);
            long j2 = (j + (((readTimestamp2 - readTimestamp) + (readTimestamp3 - j)) / 2)) - elapsedRealtime2;
            datagramSocket.close();
            return j2;
        } catch (Throwable th) {
            try {
                datagramSocket.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static long read32(byte[] bArr, int i) {
        int i2 = bArr[i];
        int i3 = bArr[i + 1];
        int i4 = bArr[i + 2];
        int i5 = bArr[i + 3];
        if ((i2 & 128) == 128) {
            i2 = (i2 & NotificationCenter.dialogTranslate) + 128;
        }
        if ((i3 & 128) == 128) {
            i3 = (i3 & NotificationCenter.dialogTranslate) + 128;
        }
        if ((i4 & 128) == 128) {
            i4 = (i4 & NotificationCenter.dialogTranslate) + 128;
        }
        if ((i5 & 128) == 128) {
            i5 = (i5 & NotificationCenter.dialogTranslate) + 128;
        }
        return (i2 << 24) + (i3 << 16) + (i4 << 8) + i5;
    }

    private static long readTimestamp(byte[] bArr, int i) {
        long read32 = read32(bArr, i);
        long read322 = read32(bArr, i + 4);
        if (read32 == 0 && read322 == 0) {
            return 0L;
        }
        return ((read32 - 2208988800L) * 1000) + ((read322 * 1000) / 4294967296L);
    }

    private static void writeTimestamp(byte[] bArr, int i, long j) {
        if (j == 0) {
            Arrays.fill(bArr, i, i + 8, (byte) 0);
            return;
        }
        long j2 = j / 1000;
        long j3 = j - (j2 * 1000);
        bArr[i] = (byte) (r2 >> 24);
        bArr[i + 1] = (byte) (r2 >> 16);
        bArr[i + 2] = (byte) (r2 >> 8);
        bArr[i + 3] = (byte) (j2 + 2208988800L);
        long j4 = (j3 * 4294967296L) / 1000;
        bArr[i + 4] = (byte) (j4 >> 24);
        bArr[i + 5] = (byte) (j4 >> 16);
        bArr[i + 6] = (byte) (j4 >> 8);
        bArr[i + 7] = (byte) (Math.random() * 255.0d);
    }
}
