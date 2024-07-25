package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputUser;
/* loaded from: classes3.dex */
public class TL_bots$getPreviewInfo extends TLObject {
    public TLRPC$InputUser bot;
    public String lang_code = "";

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TL_bots$previewInfo.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1111143341);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.lang_code);
    }
}
