package com.microsoft.appcenter.distribute.download.http;

import android.os.AsyncTask;
import java.io.File;
/* loaded from: classes.dex */
class HttpConnectionCheckTask extends AsyncTask {
    private final HttpConnectionReleaseDownloader mDownloader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpConnectionCheckTask(HttpConnectionReleaseDownloader httpConnectionReleaseDownloader) {
        this.mDownloader = httpConnectionReleaseDownloader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        File targetFile = this.mDownloader.getTargetFile();
        if (targetFile == null) {
            this.mDownloader.onDownloadError("Cannot access to downloads folder. Shared storage is not currently available.");
            return null;
        }
        String downloadedReleaseFilePath = this.mDownloader.getDownloadedReleaseFilePath();
        if (downloadedReleaseFilePath != null) {
            File file = new File(downloadedReleaseFilePath);
            if (!downloadedReleaseFilePath.equals(targetFile.getAbsolutePath())) {
                file.delete();
                this.mDownloader.setDownloadedReleaseFilePath(null);
            } else if (file.exists()) {
                this.mDownloader.onDownloadComplete(targetFile);
                return null;
            }
        }
        if (isCancelled()) {
            return null;
        }
        this.mDownloader.onStart(targetFile);
        return null;
    }
}
