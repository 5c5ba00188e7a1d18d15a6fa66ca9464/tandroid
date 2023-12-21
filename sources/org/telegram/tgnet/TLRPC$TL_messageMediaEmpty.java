package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageMediaEmpty extends TLRPC$MessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1038967584);
    }
}
