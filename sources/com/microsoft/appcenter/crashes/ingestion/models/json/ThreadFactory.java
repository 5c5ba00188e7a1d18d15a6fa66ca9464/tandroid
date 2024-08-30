package com.microsoft.appcenter.crashes.ingestion.models.json;

import com.microsoft.appcenter.crashes.ingestion.models.Thread;
import com.microsoft.appcenter.ingestion.models.json.ModelFactory;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ThreadFactory implements ModelFactory {
    private static final ThreadFactory sInstance = new ThreadFactory();

    private ThreadFactory() {
    }

    public static ThreadFactory getInstance() {
        return sInstance;
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    public Thread create() {
        return new Thread();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    public List createList(int i) {
        return new ArrayList(i);
    }
}
