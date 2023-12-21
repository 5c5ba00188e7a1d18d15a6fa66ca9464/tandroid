package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputUserEmpty extends TLRPC$InputUser {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1182234929);
    }
}
