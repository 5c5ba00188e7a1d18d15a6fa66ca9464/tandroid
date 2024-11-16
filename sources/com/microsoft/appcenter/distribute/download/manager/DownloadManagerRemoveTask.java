package com.microsoft.appcenter.distribute.download.manager;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;

/* loaded from: classes.dex */
class DownloadManagerRemoveTask extends AsyncTask {
    private final Context mContext;
    private final long mDownloadId;

    DownloadManagerRemoveTask(Context context, long j) {
        this.mContext = context;
        this.mDownloadId = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        ((DownloadManager) this.mContext.getSystemService("download")).remove(this.mDownloadId);
        return null;
    }
}
