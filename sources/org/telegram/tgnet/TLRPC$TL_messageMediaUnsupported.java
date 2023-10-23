package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageMediaUnsupported extends TLRPC$MessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1618676578);
    }
}
