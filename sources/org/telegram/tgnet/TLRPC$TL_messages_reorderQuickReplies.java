package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_reorderQuickReplies extends TLObject {
    public ArrayList<Integer> order = new ArrayList<>();

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1613961479);
        abstractSerializedData.writeInt32(481674261);
        int size = this.order.size();
        abstractSerializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            abstractSerializedData.writeInt32(this.order.get(i).intValue());
        }
    }
}
