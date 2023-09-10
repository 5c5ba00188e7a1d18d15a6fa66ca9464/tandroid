package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_bots_invokeWebViewCustomMethod extends TLObject {
    public static int constructor = 142591463;
    public TLRPC$InputUser bot;
    public String custom_method;
    public TLRPC$TL_dataJSON params;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_dataJSON.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.custom_method);
        this.params.serializeToStream(abstractSerializedData);
    }
}
