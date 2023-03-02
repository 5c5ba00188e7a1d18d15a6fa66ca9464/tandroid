package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputBotAppShortName extends TLRPC$InputBotApp {
    public static int constructor = -1869872121;
    public TLRPC$InputUser bot_id;
    public String short_name;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.bot_id = TLRPC$InputUser.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.short_name = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.bot_id.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.short_name);
    }
}
