package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_replyKeyboardMarkup_layer129 extends TLRPC$TL_replyKeyboardMarkup {
    public static int constructor = 889353612;

    @Override // org.telegram.tgnet.TLRPC$TL_replyKeyboardMarkup, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.resize = (readInt32 & 1) != 0;
        this.single_use = (readInt32 & 2) != 0;
        this.selective = (readInt32 & 4) != 0;
        int readInt322 = abstractSerializedData.readInt32(z);
        if (readInt322 != 481674261) {
            if (z) {
                throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
            }
            return;
        }
        int readInt323 = abstractSerializedData.readInt32(z);
        for (int i = 0; i < readInt323; i++) {
            TLRPC$TL_keyboardButtonRow TLdeserialize = TLRPC$TL_keyboardButtonRow.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (TLdeserialize == null) {
                return;
            }
            this.rows.add(TLdeserialize);
        }
    }

    @Override // org.telegram.tgnet.TLRPC$TL_replyKeyboardMarkup, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.resize ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.single_use ? i | 2 : i & (-3);
        this.flags = i2;
        int i3 = this.selective ? i2 | 4 : i2 & (-5);
        this.flags = i3;
        abstractSerializedData.writeInt32(i3);
        abstractSerializedData.writeInt32(481674261);
        int size = this.rows.size();
        abstractSerializedData.writeInt32(size);
        for (int i4 = 0; i4 < size; i4++) {
            this.rows.get(i4).serializeToStream(abstractSerializedData);
        }
    }
}
