package com.microsoft.appcenter.crashes.ingestion.models.json;

import com.microsoft.appcenter.crashes.ingestion.models.StackFrame;
import com.microsoft.appcenter.ingestion.models.json.ModelFactory;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StackFrameFactory implements ModelFactory<StackFrame> {
    private static final StackFrameFactory sInstance = new StackFrameFactory();

    private StackFrameFactory() {
    }

    public static StackFrameFactory getInstance() {
        return sInstance;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    /* renamed from: create */
    public StackFrame mo246create() {
        return new StackFrame();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.ModelFactory
    public List<StackFrame> createList(int i) {
        return new ArrayList(i);
    }
}
