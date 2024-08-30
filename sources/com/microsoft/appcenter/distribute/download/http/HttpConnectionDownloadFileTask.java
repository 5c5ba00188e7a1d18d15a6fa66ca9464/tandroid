package com.microsoft.appcenter.distribute.download.http;

import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import com.microsoft.appcenter.http.HttpUtils;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
/* loaded from: classes.dex */
class HttpConnectionDownloadFileTask extends AsyncTask {
    private final Uri mDownloadUri;
    private final HttpConnectionReleaseDownloader mDownloader;
    private final File mTargetFile;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpConnectionDownloadFileTask(HttpConnectionReleaseDownloader httpConnectionReleaseDownloader, Uri uri, File file) {
        this.mDownloader = httpConnectionReleaseDownloader;
        this.mDownloadUri = uri;
        this.mTargetFile = file;
    }

    private static void close(Closeable... closeableArr) {
        for (Closeable closeable : closeableArr) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException unused) {
                }
            }
        }
    }

    private long copyStream(InputStream inputStream, OutputStream outputStream, long j) {
        long j2;
        byte[] bArr = new byte[1024];
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                break;
            }
            j3 += read;
            outputStream.write(bArr, 0, read);
            long currentTimeMillis = System.currentTimeMillis();
            if (j3 >= 524288 + j4 || j3 == j || currentTimeMillis >= 500 + j5) {
                this.mDownloader.onDownloadProgress(j3, j);
                j2 = j3;
            } else {
                currentTimeMillis = j5;
                j2 = j4;
            }
            if (isCancelled()) {
                break;
            }
            j4 = j2;
            j5 = currentTimeMillis;
        }
        outputStream.flush();
        return j3;
    }

    private URLConnection createConnection() {
        HttpsURLConnection createHttpsConnection = HttpUtils.createHttpsConnection(new URL(this.mDownloadUri.toString()));
        createHttpsConnection.setInstanceFollowRedirects(true);
        createHttpsConnection.connect();
        if (!"application/vnd.android.package-archive".equals(createHttpsConnection.getContentType())) {
            AppCenterLog.warn("AppCenterDistribute", "The requested download has not expected content type.");
        }
        int responseCode = createHttpsConnection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new IOException("Download failed with HTTP error code: " + responseCode);
        }
        return createHttpsConnection;
    }

    private long downloadFile(URLConnection uRLConnection) {
        FileOutputStream fileOutputStream;
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(uRLConnection.getInputStream());
            try {
                fileOutputStream = new FileOutputStream(this.mTargetFile);
                try {
                    long copyStream = copyStream(bufferedInputStream2, fileOutputStream, uRLConnection.getContentLength());
                    close(bufferedInputStream2, fileOutputStream);
                    return copyStream;
                } catch (Throwable th) {
                    th = th;
                    bufferedInputStream = bufferedInputStream2;
                    close(bufferedInputStream, fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        TrafficStats.setThreadStatsTag(-667034599);
        try {
            try {
                this.mDownloader.onDownloadStarted(System.currentTimeMillis());
            } catch (IOException e) {
                this.mDownloader.onDownloadError(e.getMessage());
            }
            if (downloadFile(createConnection()) > 0) {
                this.mDownloader.onDownloadComplete(this.mTargetFile);
                return null;
            }
            throw new IOException("The content of downloaded file is empty");
        } finally {
            TrafficStats.clearThreadStatsTag();
        }
    }
}
