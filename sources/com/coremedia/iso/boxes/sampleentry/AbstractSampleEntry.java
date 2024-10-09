package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.boxes.Box;
import com.googlecode.mp4parser.AbstractContainerBox;

/* loaded from: classes.dex */
public abstract class AbstractSampleEntry extends AbstractContainerBox implements Box {
    protected int dataReferenceIndex;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractSampleEntry(String str) {
        super(str);
        this.dataReferenceIndex = 1;
    }

    public void setDataReferenceIndex(int i) {
        this.dataReferenceIndex = i;
    }
}
