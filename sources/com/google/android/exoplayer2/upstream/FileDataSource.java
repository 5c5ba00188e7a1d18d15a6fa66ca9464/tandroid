package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import android.system.OsConstants;
import android.text.TextUtils;
import com.google.android.exoplayer2.analytics.MediaMetricsListener$$ExternalSyntheticApiModelOutline52;
import com.google.android.exoplayer2.analytics.MediaMetricsListener$$ExternalSyntheticApiModelOutline53;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public final class FileDataSource extends BaseDataSource {
    private long bytesRemaining;
    private RandomAccessFile file;
    private boolean opened;
    private Uri uri;

    private static final class Api21 {
        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isPermissionError(Throwable th) {
            int i;
            int i2;
            if (MediaMetricsListener$$ExternalSyntheticApiModelOutline52.m(th)) {
                i = MediaMetricsListener$$ExternalSyntheticApiModelOutline53.m(th).errno;
                i2 = OsConstants.EACCES;
                if (i == i2) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class FileDataSourceException extends DataSourceException {
        public FileDataSourceException(String str, Throwable th, int i) {
            super(str, th, i);
        }

        public FileDataSourceException(Throwable th, int i) {
            super(th, i);
        }
    }

    public FileDataSource() {
        super(false);
    }

    private static RandomAccessFile openLocalFile(Uri uri) {
        try {
            return new RandomAccessFile((String) Assertions.checkNotNull(uri.getPath()), "r");
        } catch (FileNotFoundException e) {
            if (TextUtils.isEmpty(uri.getQuery()) && TextUtils.isEmpty(uri.getFragment())) {
                throw new FileDataSourceException(e, (Util.SDK_INT < 21 || !Api21.isPermissionError(e.getCause())) ? 2005 : 2006);
            }
            throw new FileDataSourceException(String.format("uri has query and/or fragment, which are not supported. Did you call Uri.parse() on a string containing '?' or '#'? Use Uri.fromFile(new File(path)) to avoid this. path=%s,query=%s,fragment=%s", uri.getPath(), uri.getQuery(), uri.getFragment()), e, 1004);
        } catch (SecurityException e2) {
            throw new FileDataSourceException(e2, 2006);
        } catch (RuntimeException e3) {
            throw new FileDataSourceException(e3, 2000);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() {
        this.uri = null;
        try {
            try {
                RandomAccessFile randomAccessFile = this.file;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                throw new FileDataSourceException(e, 2000);
            }
        } finally {
            this.file = null;
            if (this.opened) {
                this.opened = false;
                transferEnded();
            }
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) {
        Uri uri = dataSpec.uri;
        this.uri = uri;
        transferInitializing(dataSpec);
        RandomAccessFile openLocalFile = openLocalFile(uri);
        this.file = openLocalFile;
        try {
            openLocalFile.seek(dataSpec.position);
            long j = dataSpec.length;
            if (j == -1) {
                j = this.file.length() - dataSpec.position;
            }
            this.bytesRemaining = j;
            if (j < 0) {
                throw new FileDataSourceException(null, null, 2008);
            }
            this.opened = true;
            transferStarted(dataSpec);
            return this.bytesRemaining;
        } catch (IOException e) {
            throw new FileDataSourceException(e, 2000);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataReader
    public int read(byte[] bArr, int i, int i2) {
        if (i2 == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int read = ((RandomAccessFile) Util.castNonNull(this.file)).read(bArr, i, (int) Math.min(this.bytesRemaining, i2));
            if (read > 0) {
                this.bytesRemaining -= read;
                bytesTransferred(read);
            }
            return read;
        } catch (IOException e) {
            throw new FileDataSourceException(e, 2000);
        }
    }
}
