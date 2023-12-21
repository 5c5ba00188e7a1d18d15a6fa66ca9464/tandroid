package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_upload_getCdnFile extends TLObject {
    public byte[] file_token;
    public int limit;
    public long offset;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$upload_CdnFile.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(962554330);
        abstractSerializedData.writeByteArray(this.file_token);
        abstractSerializedData.writeInt64(this.offset);
        abstractSerializedData.writeInt32(this.limit);
    }
}
