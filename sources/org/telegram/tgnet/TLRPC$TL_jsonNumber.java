package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_jsonNumber extends TLRPC$JSONValue {
    public double value;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.value = abstractSerializedData.readDouble(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(736157604);
        abstractSerializedData.writeDouble(this.value);
    }
}
