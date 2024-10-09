package com.microsoft.appcenter.distribute.download.manager;

import android.app.DownloadManager;
import android.database.Cursor;
import android.os.AsyncTask;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
class DownloadManagerUpdateTask extends AsyncTask {
    private final DownloadManagerReleaseDownloader mDownloader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DownloadManagerUpdateTask(DownloadManagerReleaseDownloader downloadManagerReleaseDownloader) {
        this.mDownloader = downloadManagerReleaseDownloader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        Cursor query;
        DownloadManager downloadManager = this.mDownloader.getDownloadManager();
        long downloadId = this.mDownloader.getDownloadId();
        if (downloadId == -1) {
            this.mDownloader.onStart();
            return null;
        }
        try {
            query = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
        } catch (RuntimeException e) {
            this.mDownloader.onDownloadError(e);
        }
        if (query == null) {
            throw new NoSuchElementException("Cannot find download with id=" + downloadId);
        }
        try {
            if (!query.moveToFirst()) {
                throw new NoSuchElementException("Cannot find download with id=" + downloadId);
            }
            if (isCancelled()) {
                return null;
            }
            int i = query.getInt(query.getColumnIndexOrThrow("status"));
            if (i != 16) {
                if (i != 8) {
                    this.mDownloader.onDownloadProgress(query);
                    return null;
                }
                this.mDownloader.onDownloadComplete(query);
                return null;
            }
            throw new IllegalStateException("The download has failed with reason code: " + query.getInt(query.getColumnIndexOrThrow("reason")));
        } finally {
            query.close();
        }
    }
}
