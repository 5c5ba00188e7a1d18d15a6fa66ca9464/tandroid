package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_jsonNull extends TLRPC$JSONValue {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1064139624);
    }
}
