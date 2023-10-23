package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputMediaEmpty extends TLRPC$InputMedia {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1771768449);
    }
}
