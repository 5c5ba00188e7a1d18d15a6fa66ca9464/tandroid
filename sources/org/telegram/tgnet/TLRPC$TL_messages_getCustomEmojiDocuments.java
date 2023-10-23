package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_getCustomEmojiDocuments extends TLObject {
    public ArrayList<Long> document_id = new ArrayList<>();

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$Vector tLRPC$Vector = new TLRPC$Vector();
        int readInt32 = abstractSerializedData.readInt32(z);
        for (int i2 = 0; i2 < readInt32; i2++) {
            TLRPC$Document TLdeserialize = TLRPC$Document.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (TLdeserialize == null) {
                return tLRPC$Vector;
            }
            tLRPC$Vector.objects.add(TLdeserialize);
        }
        return tLRPC$Vector;
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-643100844);
        abstractSerializedData.writeInt32(481674261);
        int size = this.document_id.size();
        abstractSerializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            abstractSerializedData.writeInt64(this.document_id.get(i).longValue());
        }
    }
}
