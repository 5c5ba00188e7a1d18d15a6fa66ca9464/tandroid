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
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
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
    private TLRPC$Document document;
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

    public static int getStreamPrioriy(TLRPC$Document tLRPC$Document) {
        Integer num;
        if (tLRPC$Document == null || (num = priorityMap.get(Long.valueOf(tLRPC$Document.id))) == null) {
            return 3;
        }
        return num.intValue();
    }

    public static Uri prepareUri(int i, TLRPC$Document tLRPC$Document, Object obj) {
        String attachFileName = FileLoader.getAttachFileName(tLRPC$Document);
        File pathToAttach = FileLoader.getInstance(i).getPathToAttach(tLRPC$Document);
        if (pathToAttach == null || !pathToAttach.exists()) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("?account=");
                sb.append(i);
                sb.append("&id=");
                sb.append(tLRPC$Document.id);
                sb.append("&hash=");
                sb.append(tLRPC$Document.access_hash);
                sb.append("&dc=");
                sb.append(tLRPC$Document.dc_id);
                sb.append("&size=");
                sb.append(tLRPC$Document.size);
                sb.append("&mime=");
                sb.append(URLEncoder.encode(tLRPC$Document.mime_type, "UTF-8"));
                sb.append("&rid=");
                sb.append(FileLoader.getInstance(i).getFileReference(obj));
                sb.append("&name=");
                sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(tLRPC$Document), "UTF-8"));
                sb.append("&reference=");
                byte[] bArr = tLRPC$Document.file_reference;
                if (bArr == null) {
                    bArr = new byte[0];
                }
                sb.append(Utilities.bytesToHex(bArr));
                String sb2 = sb.toString();
                return Uri.parse("tg://" + attachFileName + sb2);
            } catch (UnsupportedEncodingException e) {
                FileLog.e(e);
                return null;
            }
        }
        return Uri.fromFile(pathToAttach);
    }

    public static void setPriorityForDocument(TLRPC$Document tLRPC$Document, int i) {
        if (tLRPC$Document != null) {
            priorityMap.put(Long.valueOf(tLRPC$Document.id), Integer.valueOf(i));
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
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0161  */
    @Override // com.google.android.exoplayer2.upstream.DataSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long open(DataSpec dataSpec) {
        ArrayList<TLRPC$DocumentAttribute> arrayList;
        TLRPC$DocumentAttribute tLRPC$TL_documentAttributeAudio;
        long j;
        this.uri = dataSpec.uri;
        transferInitializing(dataSpec);
        int intValue = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("account")).intValue();
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
        if (!this.document.mime_type.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
            if (this.document.mime_type.startsWith(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                arrayList = this.document.attributes;
                tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
            }
            allStreams.put(Long.valueOf(this.document.id), this);
            FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
            TLRPC$Document tLRPC$Document = this.document;
            Object obj = this.parentObject;
            long j2 = dataSpec.position;
            this.currentOffset = j2;
            this.loadOperation = fileLoader.loadStreamFile(this, tLRPC$Document, null, obj, j2, false, getCurrentPriority());
            j = dataSpec.length;
            if (j == -1) {
                j = this.document.size - dataSpec.position;
            }
            this.bytesRemaining = j;
            if (j < 0) {
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
                            if (this.loadOperation.isFinished()) {
                                this.bytesRemaining = this.currentFile.length() - this.currentOffset;
                            }
                        } catch (Throwable unused) {
                        }
                    }
                }
                return this.bytesRemaining;
            }
            throw new EOFException();
        }
        arrayList = this.document.attributes;
        tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeVideo();
        arrayList.add(tLRPC$TL_documentAttributeAudio);
        allStreams.put(Long.valueOf(this.document.id), this);
        FileLoader fileLoader2 = FileLoader.getInstance(this.currentAccount);
        TLRPC$Document tLRPC$Document2 = this.document;
        Object obj2 = this.parentObject;
        long j22 = dataSpec.position;
        this.currentOffset = j22;
        this.loadOperation = fileLoader2.loadStreamFile(this, tLRPC$Document2, null, obj2, j22, false, getCurrentPriority());
        j = dataSpec.length;
        if (j == -1) {
        }
        this.bytesRemaining = j;
        if (j < 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0031 A[Catch: Exception -> 0x001c, TryCatch #0 {Exception -> 0x001c, blocks: (B:14:0x0017, B:21:0x0023, B:23:0x0031, B:25:0x0054, B:26:0x0059, B:28:0x005d, B:29:0x0063, B:31:0x006d, B:33:0x0075, B:35:0x0079, B:36:0x008d, B:39:0x0094, B:19:0x001f, B:46:0x00c0, B:49:0x00c5, B:51:0x00cb), top: B:57:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0079 A[Catch: Exception -> 0x001c, TryCatch #0 {Exception -> 0x001c, blocks: (B:14:0x0017, B:21:0x0023, B:23:0x0031, B:25:0x0054, B:26:0x0059, B:28:0x005d, B:29:0x0063, B:31:0x006d, B:33:0x0075, B:35:0x0079, B:36:0x008d, B:39:0x0094, B:19:0x001f, B:46:0x00c0, B:49:0x00c5, B:51:0x00cb), top: B:57:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0091 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0098 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0015 A[SYNTHETIC] */
    @Override // com.google.android.exoplayer2.upstream.DataReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(byte[] bArr, int i, int i2) {
        File currentFileFast;
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
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
                    if (!this.opened) {
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
                                if (this.loadOperation.isFinished()) {
                                    this.bytesRemaining = this.currentFile.length() - this.currentOffset;
                                }
                            } catch (Throwable unused2) {
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
            randomAccessFile2 = this.file;
            if (randomAccessFile2 != null) {
                break;
            }
            i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i2)[0];
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
        }
        if (this.opened) {
            int read = randomAccessFile2.read(bArr, i, i3);
            if (read > 0) {
                long j2 = read;
                this.currentOffset += j2;
                this.bytesRemaining -= j2;
                bytesTransferred(read);
            }
            return read;
        }
        return 0;
    }
}
