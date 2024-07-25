package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputUser;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes3.dex */
public class TL_bots$allowSendMessage extends TLObject {
    public TLRPC$InputUser bot;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-248323089);
        this.bot.serializeToStream(abstractSerializedData);
    }
}
