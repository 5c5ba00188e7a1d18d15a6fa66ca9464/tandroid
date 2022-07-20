package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updates_differenceTooLong extends TLRPC$updates_Difference {
    public static int constructor = 1258196845;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.pts = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.pts);
    }
}
