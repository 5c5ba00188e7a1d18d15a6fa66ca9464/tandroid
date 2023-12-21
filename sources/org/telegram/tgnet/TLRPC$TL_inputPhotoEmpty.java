package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputPhotoEmpty extends TLRPC$InputPhoto {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(483901197);
    }
}
