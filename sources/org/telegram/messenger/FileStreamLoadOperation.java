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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes3.dex */
public class FileStreamLoadOperation extends BaseDataSource implements FileLoadOperationStream {
    public static final ConcurrentHashMap<Long, FileStreamLoadOperation> allStreams = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Integer> priorityMap = new ConcurrentHashMap<>();
    private long bytesRemaining;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    File currentFile;
    private long currentOffset;
    private boolean customLength;
    private TLRPC.Document document;
    private RandomAccessFile file;
    private FileLoadOperation loadOperation;
    private boolean opened;
    private Object parentObject;
    private Uri uri;

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

    private int getCurrentPriority() {
        Integer num = (Integer) ConcurrentMap$-EL.getOrDefault(priorityMap, Long.valueOf(this.document.id), null);
        if (num != null) {
            return num.intValue();
        }
        return 3;
    }

    public static int getStreamPrioriy(TLRPC.Document document) {
        Integer num;
        if (document == null || (num = priorityMap.get(Long.valueOf(document.id))) == null) {
            return 3;
        }
        return num.intValue();
    }

    public static Uri prepareUri(int i, TLRPC.Document document, Object obj) {
        String attachFileName = FileLoader.getAttachFileName(document);
        File pathToAttach = FileLoader.getInstance(i).getPathToAttach(document);
        if (pathToAttach != null && pathToAttach.exists()) {
            return Uri.fromFile(pathToAttach);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("?account=");
            sb.append(i);
            sb.append("&id=");
            sb.append(document.id);
            sb.append("&hash=");
            sb.append(document.access_hash);
            sb.append("&dc=");
            sb.append(document.dc_id);
            sb.append("&size=");
            sb.append(document.size);
            sb.append("&mime=");
            sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
            sb.append("&rid=");
            sb.append(FileLoader.getInstance(i).getFileReference(obj));
            sb.append("&name=");
            sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
            sb.append("&reference=");
            byte[] bArr = document.file_reference;
            if (bArr == null) {
                bArr = new byte[0];
            }
            sb.append(Utilities.bytesToHex(bArr));
            return Uri.parse("tg://" + attachFileName + sb.toString());
        } catch (UnsupportedEncodingException e) {
            FileLog.e(e);
            return null;
        }
    }

    public static void setPriorityForDocument(TLRPC.Document document, int i) {
        if (document != null) {
            priorityMap.put(Long.valueOf(document.id), Integer.valueOf(i));
        }
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
        allStreams.remove(Long.valueOf(this.document.id));
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

    @Override // com.google.android.exoplayer2.upstream.BaseDataSource, com.google.android.exoplayer2.upstream.DataSource
    public /* bridge */ /* synthetic */ Map getResponseHeaders() {
        Map emptyMap;
        emptyMap = Collections.emptyMap();
        return emptyMap;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // org.telegram.messenger.FileLoadOperationStream
    public void newDataAvailable() {
        CountDownLatch countDownLatch = this.countDownLatch;
        this.countDownLatch = null;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x011c  */
    @Override // com.google.android.exoplayer2.upstream.DataSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long open(DataSpec dataSpec) {
        ArrayList<TLRPC.DocumentAttribute> arrayList;
        TLRPC.DocumentAttribute tL_documentAttributeAudio;
        long j;
        boolean z;
        this.uri = dataSpec.uri;
        transferInitializing(dataSpec);
        int intValue = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("account")).intValue();
        this.currentAccount = intValue;
        this.parentObject = FileLoader.getInstance(intValue).getParentObject(Utilities.parseInt((CharSequence) this.uri.getQueryParameter("rid")).intValue());
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        this.document = tL_document;
        tL_document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash")).longValue();
        this.document.id = Utilities.parseLong(this.uri.getQueryParameter("id")).longValue();
        this.document.size = Utilities.parseLong(this.uri.getQueryParameter("size")).longValue();
        this.document.dc_id = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("dc")).intValue();
        this.document.mime_type = this.uri.getQueryParameter("mime");
        this.document.file_reference = Utilities.hexToBytes(this.uri.getQueryParameter("reference"));
        TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
        tL_documentAttributeFilename.file_name = this.uri.getQueryParameter("name");
        this.document.attributes.add(tL_documentAttributeFilename);
        if (!this.document.mime_type.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
            if (this.document.mime_type.startsWith(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                arrayList = this.document.attributes;
                tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
            }
            allStreams.put(Long.valueOf(this.document.id), this);
            FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
            TLRPC.Document document = this.document;
            Object obj = this.parentObject;
            long j2 = dataSpec.position;
            this.currentOffset = j2;
            this.loadOperation = fileLoader.loadStreamFile(this, document, null, obj, j2, false, getCurrentPriority());
            j = dataSpec.length;
            z = j == -1;
            this.customLength = z;
            if (!z) {
                j = this.document.size - dataSpec.position;
            }
            this.bytesRemaining = j;
            if (j >= 0) {
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
                        if (this.loadOperation.isFinished() && !this.customLength) {
                            this.bytesRemaining = this.currentFile.length() - this.currentOffset;
                        }
                    } catch (Throwable unused) {
                    }
                }
            }
            return this.bytesRemaining;
        }
        arrayList = this.document.attributes;
        tL_documentAttributeAudio = new TLRPC.TL_documentAttributeVideo();
        arrayList.add(tL_documentAttributeAudio);
        allStreams.put(Long.valueOf(this.document.id), this);
        FileLoader fileLoader2 = FileLoader.getInstance(this.currentAccount);
        TLRPC.Document document2 = this.document;
        Object obj2 = this.parentObject;
        long j22 = dataSpec.position;
        this.currentOffset = j22;
        this.loadOperation = fileLoader2.loadStreamFile(this, document2, null, obj2, j22, false, getCurrentPriority());
        j = dataSpec.length;
        if (j == -1) {
        }
        this.customLength = z;
        if (!z) {
        }
        this.bytesRemaining = j;
        if (j >= 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0038 A[Catch: Exception -> 0x0023, TryCatch #2 {Exception -> 0x0023, blocks: (B:65:0x001e, B:15:0x002a, B:17:0x0038, B:19:0x005c, B:20:0x0061, B:22:0x0065, B:23:0x006b, B:25:0x0075, B:29:0x007d, B:31:0x0081, B:32:0x0095, B:34:0x009c, B:13:0x0026, B:54:0x00cc, B:57:0x00d1, B:59:0x00db, B:62:0x00e0), top: B:64:0x001e }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0081 A[Catch: Exception -> 0x0023, TryCatch #2 {Exception -> 0x0023, blocks: (B:65:0x001e, B:15:0x002a, B:17:0x0038, B:19:0x005c, B:20:0x0061, B:22:0x0065, B:23:0x006b, B:25:0x0075, B:29:0x007d, B:31:0x0081, B:32:0x0095, B:34:0x009c, B:13:0x0026, B:54:0x00cc, B:57:0x00d1, B:59:0x00db, B:62:0x00e0), top: B:64:0x001e }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00c9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0099 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.google.android.exoplayer2.upstream.DataReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(byte[] bArr, int i, int i2) {
        int i3;
        File currentFileFast;
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        int i4 = i2;
        System.currentTimeMillis();
        if (i4 == 0) {
            return 0;
        }
        long j = this.bytesRemaining;
        if (j <= 0) {
            return -1;
        }
        if (j < i4) {
            i4 = (int) j;
        }
        int i5 = 0;
        while (true) {
            if (i5 == 0) {
                try {
                    if (!this.opened) {
                    }
                    i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i4)[0];
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
                    currentFileFast = this.loadOperation.getCurrentFileFast();
                    if (this.file != null || !Objects.equals(this.currentFile, currentFileFast)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("check stream file " + currentFileFast);
                        }
                        randomAccessFile = this.file;
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                            } catch (Exception unused) {
                            }
                        }
                        this.currentFile = currentFileFast;
                        if (currentFileFast == null) {
                            try {
                                RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.currentFile, "r");
                                this.file = randomAccessFile3;
                                randomAccessFile3.seek(this.currentOffset);
                                if (this.loadOperation.isFinished() && !this.customLength) {
                                    this.bytesRemaining = this.currentFile.length() - this.currentOffset;
                                }
                            } catch (Throwable unused2) {
                            }
                        }
                    }
                    i5 = i3;
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
            randomAccessFile2 = this.file;
            if (randomAccessFile2 != null) {
                break;
            }
            i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i4)[0];
            if (i3 == 0) {
            }
            currentFileFast = this.loadOperation.getCurrentFileFast();
            if (this.file != null) {
            }
            if (BuildVars.LOGS_ENABLED) {
            }
            randomAccessFile = this.file;
            if (randomAccessFile != null) {
            }
            this.currentFile = currentFileFast;
            if (currentFileFast == null) {
            }
            i5 = i3;
        }
        if (!this.opened) {
            return 0;
        }
        int read = randomAccessFile2.read(bArr, i, i5);
        if (read == -1) {
            this.bytesRemaining = 0L;
            return -1;
        }
        if (read > 0) {
            long j2 = read;
            this.currentOffset += j2;
            this.bytesRemaining -= j2;
            bytesTransferred(read);
        }
        return read;
    }
}
