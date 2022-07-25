package com.microsoft.appcenter.crashes.ingestion.models.json;

import com.microsoft.appcenter.crashes.ingestion.models.Exception;
import com.microsoft.appcenter.ingestion.models.json.ModelFactory;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ExceptionFactory implements ModelFactory<Exception> {
    private static final ExceptionFactory sInstance = new ExceptionFactory();

    private ExceptionFactory() {
    }

    public static ExceptionFactory getInstance() {
        return sInstance;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    /* renamed from: create */
    public Exception mo246create() {
        return new Exception();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    public List<Exception> createList(int i) {
        return new ArrayList(i);
    }
}
