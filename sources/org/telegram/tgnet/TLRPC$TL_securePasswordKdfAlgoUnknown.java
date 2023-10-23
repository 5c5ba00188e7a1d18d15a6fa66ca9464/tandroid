package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_securePasswordKdfAlgoUnknown extends TLRPC$SecurePasswordKdfAlgo {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(4883767);
    }
}
