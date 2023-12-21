package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_help_getAppUpdate extends TLObject {
    public String source;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$help_AppUpdate.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1378703997);
        abstractSerializedData.writeString(this.source);
    }
}
