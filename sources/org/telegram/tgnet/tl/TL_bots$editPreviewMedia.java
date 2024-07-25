package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputMedia;
import org.telegram.tgnet.TLRPC$InputUser;
/* loaded from: classes3.dex */
public class TL_bots$editPreviewMedia extends TLObject {
    public TLRPC$InputUser bot;
    public String lang_code = "";
    public TLRPC$InputMedia media;
    public TLRPC$InputMedia new_media;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TL_bots$botPreviewMedia.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2061148049);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.lang_code);
        this.media.serializeToStream(abstractSerializedData);
        this.new_media.serializeToStream(abstractSerializedData);
    }
}
