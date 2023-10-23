package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_jsonString extends TLRPC$JSONValue {
    public String value;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.value = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1222740358);
        abstractSerializedData.writeString(this.value);
    }
}
