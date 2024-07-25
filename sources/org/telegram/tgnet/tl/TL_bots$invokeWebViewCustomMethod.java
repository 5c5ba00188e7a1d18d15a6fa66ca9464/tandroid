package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputUser;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
/* loaded from: classes3.dex */
public class TL_bots$invokeWebViewCustomMethod extends TLObject {
    public TLRPC$InputUser bot;
    public String custom_method;
    public TLRPC$TL_dataJSON params;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_dataJSON.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(142591463);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.custom_method);
        this.params.serializeToStream(abstractSerializedData);
    }
}
