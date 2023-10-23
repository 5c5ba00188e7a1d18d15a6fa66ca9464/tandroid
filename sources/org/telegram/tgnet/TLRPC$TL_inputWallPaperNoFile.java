package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputWallPaperNoFile extends TLRPC$InputWallPaper {
    public long id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1770371538);
        abstractSerializedData.writeInt64(this.id);
    }
}
