package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_botInfo_layer131 extends TLRPC$TL_botInfo {
    public static int constructor = -1729618630;

    @Override // org.telegram.tgnet.TLRPC$TL_botInfo, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt32(z);
        this.description = abstractSerializedData.readString(z);
        int readInt32 = abstractSerializedData.readInt32(z);
        if (readInt32 != 481674261) {
            if (z) {
                throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt32)));
            }
            return;
        }
        int readInt322 = abstractSerializedData.readInt32(z);
        for (int i = 0; i < readInt322; i++) {
            TLRPC$TL_botCommand TLdeserialize = TLRPC$TL_botCommand.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (TLdeserialize == null) {
                return;
            }
            this.commands.add(TLdeserialize);
        }
    }

    @Override // org.telegram.tgnet.TLRPC$TL_botInfo, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.user_id);
        abstractSerializedData.writeString(this.description);
        abstractSerializedData.writeInt32(481674261);
        int size = this.commands.size();
        abstractSerializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            this.commands.get(i).serializeToStream(abstractSerializedData);
        }
    }
}
