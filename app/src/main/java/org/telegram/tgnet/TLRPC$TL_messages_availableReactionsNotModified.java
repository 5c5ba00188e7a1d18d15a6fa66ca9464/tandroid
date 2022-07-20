package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_availableReactionsNotModified extends TLRPC$messages_AvailableReactions {
    public static int constructor = -1626924713;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
