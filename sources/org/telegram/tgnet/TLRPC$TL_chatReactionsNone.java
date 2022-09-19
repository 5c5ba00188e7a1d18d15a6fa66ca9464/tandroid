package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatReactionsNone extends TLRPC$ChatReactions {
    public static int constructor = -352570692;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
