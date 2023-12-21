package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_installStickerSet extends TLObject {
    public boolean archived;
    public TLRPC$InputStickerSet stickerset;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_StickerSetInstallResult.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-946871200);
        this.stickerset.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.archived);
    }
}
