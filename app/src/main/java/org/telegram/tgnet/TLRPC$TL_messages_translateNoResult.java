package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_translateNoResult extends TLRPC$messages_TranslatedText {
    public static int constructor = 1741309751;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
