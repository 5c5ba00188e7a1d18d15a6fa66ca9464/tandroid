package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_jsonBool extends TLRPC$JSONValue {
    public boolean value;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.value = abstractSerializedData.readBool(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-952869270);
        abstractSerializedData.writeBool(this.value);
    }
}
