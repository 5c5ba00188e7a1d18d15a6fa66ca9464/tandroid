package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputMessagesFilterRoundVideo extends TLRPC$MessagesFilter {
    public static int constructor = -1253451181;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
