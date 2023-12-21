package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateGroupCallConnection extends TLRPC$Update {
    public int flags;
    public TLRPC$TL_dataJSON params;
    public boolean presentation;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.presentation = (readInt32 & 1) != 0;
        this.params = TLRPC$TL_dataJSON.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(192428418);
        int i = this.presentation ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        this.params.serializeToStream(abstractSerializedData);
    }
}
