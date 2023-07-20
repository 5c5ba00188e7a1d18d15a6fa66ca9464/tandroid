package org.telegram.messenger;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.concurrent.ConcurrentMap$-EL;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes.dex */
public class FileStreamLoadOperation extends BaseDataSource implements FileLoadOperationStream {
    private static final ConcurrentHashMap<Long, Integer> priorityMap = new ConcurrentHashMap<>();
    private long bytesRemaining;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    File currentFile;
    private long currentOffset;
    private TLRPC$Document document;
    private RandomAccessFile file;
    private FileLoadOperation loadOperation;
    private boolean opened;
    private Object parentObject;
    private Uri uri;

    @Override // com.google.android.exoplayer2.upstream.BaseDataSource, com.google.android.exoplayer2.upstream.DataSource
    public /* bridge */ /* synthetic */ Map<String, List<String>> getResponseHeaders() {
        Map<String, List<String>> emptyMap;
        emptyMap = Collections.emptyMap();
        return emptyMap;
    }

    public FileStreamLoadOperation() {
        super(false);
    }

    @Deprecated
    public FileStreamLoadOperation(TransferListener transferListener) {
        this();
        if (transferListener != null) {
            addTransferListener(transferListener);
        }
    }

    public static int getStreamPrioriy(TLRPC$Document tLRPC$Document) {
        Integer num;
        if (tLRPC$Document == null || (num = priorityMap.get(Long.valueOf(tLRPC$Document.id))) == null) {
            return 3;
        }
        return num.intValue();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        Uri uri = dataSpec.uri;
        this.uri = uri;
        int intValue = Utilities.parseInt((CharSequence) uri.getQueryParameter("account")).intValue();
        this.currentAccount = intValue;
        this.parentObject = FileLoader.getInstance(intValue).getParentObject(Utilities.parseInt((CharSequence) this.uri.getQueryParameter("rid")).intValue());
        TLRPC$TL_document tLRPC$TL_document = new TLRPC$TL_document();
        this.document = tLRPC$TL_document;
        tLRPC$TL_document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash")).longValue();
        this.document.id = Utilities.parseLong(this.uri.getQueryParameter("id")).longValue();
        this.document.size = Utilities.parseLong(this.uri.getQueryParameter("size")).longValue();
        this.document.dc_id = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("dc")).intValue();
        this.document.mime_type = this.uri.getQueryParameter("mime");
        this.document.file_reference = Utilities.hexToBytes(this.uri.getQueryParameter("reference"));
        TLRPC$TL_documentAttributeFilename tLRPC$TL_documentAttributeFilename = new TLRPC$TL_documentAttributeFilename();
        tLRPC$TL_documentAttributeFilename.file_name = this.uri.getQueryParameter("name");
        this.document.attributes.add(tLRPC$TL_documentAttributeFilename);
        if (this.document.mime_type.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
            this.document.attributes.add(new TLRPC$TL_documentAttributeVideo());
        } else if (this.document.mime_type.startsWith(MediaStreamTrack.AUDIO_TRACK_KIND)) {
            this.document.attributes.add(new TLRPC$TL_documentAttributeAudio());
        }
        FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
        TLRPC$Document tLRPC$Document = this.document;
        Object obj = this.parentObject;
        long j = dataSpec.position;
        this.currentOffset = j;
        this.loadOperation = fileLoader.loadStreamFile(this, tLRPC$Document, null, obj, j, false, getCurrentPriority());
        long j2 = dataSpec.length;
        if (j2 == -1) {
            j2 = this.document.size - dataSpec.position;
        }
        this.bytesRemaining = j2;
        if (j2 < 0) {
            throw new EOFException();
        }
        this.opened = true;
        transferStarted(dataSpec);
        FileLoadOperation fileLoadOperation = this.loadOperation;
        if (fileLoadOperation != null) {
            File currentFile = fileLoadOperation.getCurrentFile();
            this.currentFile = currentFile;
            if (currentFile != null) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(this.currentFile, "r");
                    this.file = randomAccessFile;
                    randomAccessFile.seek(this.currentOffset);
                } catch (Throwable unused) {
                }
            }
        }
        return this.bytesRemaining;
    }

    private int getCurrentPriority() {
        Integer num = (Integer) ConcurrentMap$-EL.getOrDefault(priorityMap, Long.valueOf(this.document.id), null);
        if (num != null) {
            return num.intValue();
        }
        return 3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0019, code lost:
        if (r12.opened == false) goto L13;
     */
    @Override // com.google.android.exoplayer2.upstream.DataReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(byte[] bArr, int i, int i2) throws IOException {
        RandomAccessFile randomAccessFile;
        if (i2 == 0) {
            return 0;
        }
        long j = this.bytesRemaining;
        if (j == 0) {
            return -1;
        }
        if (j < i2) {
            i2 = (int) j;
        }
        int i3 = 0;
        while (true) {
            if (i3 == 0) {
                try {
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
            randomAccessFile = this.file;
            if (randomAccessFile != null) {
                break;
            }
            i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i2)[0];
            if (i3 == 0) {
                this.countDownLatch = new CountDownLatch(1);
                FileLoadOperation loadStreamFile = FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, null, this.parentObject, this.currentOffset, false, getCurrentPriority());
                FileLoadOperation fileLoadOperation = this.loadOperation;
                if (fileLoadOperation != loadStreamFile) {
                    fileLoadOperation.removeStreamListener(this);
                    this.loadOperation = loadStreamFile;
                }
                CountDownLatch countDownLatch = this.countDownLatch;
                if (countDownLatch != null) {
                    countDownLatch.await();
                    this.countDownLatch = null;
                }
            }
            File currentFileFast = this.loadOperation.getCurrentFileFast();
            if (this.file == null || !Objects.equals(this.currentFile, currentFileFast)) {
                RandomAccessFile randomAccessFile2 = this.file;
                if (randomAccessFile2 != null) {
                    try {
                        randomAccessFile2.close();
                    } catch (Exception unused) {
                    }
                }
                this.currentFile = currentFileFast;
                if (currentFileFast != null) {
                    try {
                        RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.currentFile, "r");
                        this.file = randomAccessFile3;
                        randomAccessFile3.seek(this.currentOffset);
                    } catch (Throwable unused2) {
                    }
                }
            }
        }
        if (this.opened) {
            randomAccessFile.readFully(bArr, i, i3);
            long j2 = i3;
            this.currentOffset += j2;
            this.bytesRemaining -= j2;
            bytesTransferred(i3);
            return i3;
        }
        return 0;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() {
        FileLoadOperation fileLoadOperation = this.loadOperation;
        if (fileLoadOperation != null) {
            fileLoadOperation.removeStreamListener(this);
        }
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.file = null;
        }
        this.uri = null;
        if (this.opened) {
            this.opened = false;
            transferEnded();
        }
        CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }

    @Override // org.telegram.messenger.FileLoadOperationStream
    public void newDataAvailable() {
        CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }

    public static void setPriorityForDocument(TLRPC$Document tLRPC$Document, int i) {
        if (tLRPC$Document != null) {
            priorityMap.put(Long.valueOf(tLRPC$Document.id), Integer.valueOf(i));
        }
    }
}
