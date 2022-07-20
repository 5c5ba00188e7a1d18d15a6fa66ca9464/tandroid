package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_keyboardButtonUserProfile extends TLRPC$KeyboardButton {
    public static int constructor = 814112961;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.user_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeInt64(this.user_id);
    }
}
