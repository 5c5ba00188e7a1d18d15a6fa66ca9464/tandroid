package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageMediaContact_layer131 extends TLRPC$TL_messageMediaContact {
    public static int constructor = -873313984;

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaContact, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.phone_number = abstractSerializedData.readString(z);
        this.first_name = abstractSerializedData.readString(z);
        this.last_name = abstractSerializedData.readString(z);
        this.vcard = abstractSerializedData.readString(z);
        this.user_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaContact, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.phone_number);
        abstractSerializedData.writeString(this.first_name);
        abstractSerializedData.writeString(this.last_name);
        abstractSerializedData.writeString(this.vcard);
        abstractSerializedData.writeInt32((int) this.user_id);
    }
}
