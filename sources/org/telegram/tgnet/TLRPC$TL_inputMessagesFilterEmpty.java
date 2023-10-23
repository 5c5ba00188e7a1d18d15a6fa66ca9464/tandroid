package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputMessagesFilterEmpty extends TLRPC$MessagesFilter {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1474492012);
    }
}
