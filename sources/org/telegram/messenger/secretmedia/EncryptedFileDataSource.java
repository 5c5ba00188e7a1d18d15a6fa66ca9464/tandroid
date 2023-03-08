package org.telegram.messenger.secretmedia;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
/* loaded from: classes.dex */
public final class EncryptedFileDataSource extends BaseDataSource {
    private long bytesRemaining;
    EncryptedFileInputStream fileInputStream;
    private int fileOffset;
    private boolean opened;
    private Uri uri;

    @Override // com.google.android.exoplayer2.upstream.BaseDataSource, com.google.android.exoplayer2.upstream.DataSource
    public /* bridge */ /* synthetic */ Map<String, List<String>> getResponseHeaders() {
        Map<String, List<String>> emptyMap;
        emptyMap = Collections.emptyMap();
        return emptyMap;
    }

    /* loaded from: classes.dex */
    public static class EncryptedFileDataSourceException extends IOException {
        public EncryptedFileDataSourceException(Throwable th) {
            super(th);
        }
    }

    public EncryptedFileDataSource() {
        super(false);
    }

    @Deprecated
    public EncryptedFileDataSource(TransferListener transferListener) {
        this();
        if (transferListener != null) {
            addTransferListener(transferListener);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws EncryptedFileDataSourceException {
        try {
            this.uri = dataSpec.uri;
            File file = new File(dataSpec.uri.getPath());
            String name = file.getName();
            File file2 = new File(FileLoader.getInternalCacheDir(), name + ".key");
            FileLog.d("EncryptedFileDataSource " + file + " " + file2);
            EncryptedFileInputStream encryptedFileInputStream = new EncryptedFileInputStream(file, file2);
            this.fileInputStream = encryptedFileInputStream;
            encryptedFileInputStream.skip(dataSpec.position);
            long j = dataSpec.length;
            if (j == -1) {
                j = this.fileInputStream.available();
            }
            this.bytesRemaining = j;
            FileLog.d("EncryptedFileDataSource bytesRemaining" + this.bytesRemaining);
            if (this.bytesRemaining < 0) {
                throw new EOFException();
            }
            FileLog.d("EncryptedFileDataSource opened");
            this.opened = true;
            transferStarted(dataSpec);
            return this.bytesRemaining;
        } catch (Exception e) {
            FileLog.e(e);
            throw new EncryptedFileDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataReader
    public int read(byte[] bArr, int i, int i2) throws EncryptedFileDataSourceException {
        if (i2 == 0) {
            return 0;
        }
        long j = this.bytesRemaining;
        if (j == 0) {
            return -1;
        }
        try {
            int read = this.fileInputStream.read(bArr, i, (int) Math.min(j, i2));
            this.fileOffset += read;
            if (read > 0) {
                this.bytesRemaining -= read;
                bytesTransferred(read);
            }
            return read;
        } catch (IOException e) {
            FileLog.e(e);
            throw new EncryptedFileDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws EncryptedFileDataSourceException {
        this.uri = null;
        this.fileOffset = 0;
        try {
            try {
                EncryptedFileInputStream encryptedFileInputStream = this.fileInputStream;
                if (encryptedFileInputStream != null) {
                    encryptedFileInputStream.close();
                }
            } catch (IOException e) {
                FileLog.e(e);
                throw new EncryptedFileDataSourceException(e);
            }
        } finally {
            if (this.opened) {
                this.opened = false;
                transferEnded();
            }
        }
    }
}
