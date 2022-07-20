package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputKeyboardButtonUserProfile extends TLRPC$KeyboardButton {
    public static int constructor = -376962181;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.inputUser = TLRPC$InputUser.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        this.inputUser.serializeToStream(abstractSerializedData);
    }
}
