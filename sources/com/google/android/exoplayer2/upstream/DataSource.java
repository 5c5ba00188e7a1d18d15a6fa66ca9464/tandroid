package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.util.Map;

/* loaded from: classes.dex */
public interface DataSource extends DataReader {

    public abstract /* synthetic */ class -CC {
    }

    public interface Factory {
        DataSource createDataSource();
    }

    void addTransferListener(TransferListener transferListener);

    void close();

    Map getResponseHeaders();

    Uri getUri();

    long open(DataSpec dataSpec);
}
