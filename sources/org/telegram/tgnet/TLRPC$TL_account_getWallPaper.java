package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_getWallPaper extends TLObject {
    public TLRPC$InputWallPaper wallpaper;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$WallPaper.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-57811990);
        this.wallpaper.serializeToStream(abstractSerializedData);
    }
}
