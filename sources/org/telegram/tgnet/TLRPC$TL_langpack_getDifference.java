package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_langpack_getDifference extends TLObject {
    public int from_version;
    public String lang_code;
    public String lang_pack;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_langPackDifference.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-845657435);
        abstractSerializedData.writeString(this.lang_pack);
        abstractSerializedData.writeString(this.lang_code);
        abstractSerializedData.writeInt32(this.from_version);
    }
}
