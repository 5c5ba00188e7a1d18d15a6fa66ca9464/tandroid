package com.microsoft.appcenter.distribute.download.http;

import android.os.AsyncTask;
import java.io.File;
/* loaded from: classes.dex */
class HttpConnectionRemoveFileTask extends AsyncTask {
    private final File mFile;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpConnectionRemoveFileTask(File file) {
        this.mFile = file;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        this.mFile.delete();
        return null;
    }
}
