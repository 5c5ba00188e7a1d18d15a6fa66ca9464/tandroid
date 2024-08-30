package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$PollAnswer extends TLObject {
    public byte[] option;
    public TLRPC$TL_textWithEntities text = new TLRPC$TL_textWithEntities();

    public static TLRPC$PollAnswer TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$TL_pollAnswer tLRPC$TL_pollAnswer = i != -15277366 ? i != 1823064809 ? null : new TLRPC$TL_pollAnswer() { // from class: org.telegram.tgnet.TLRPC$TL_pollAnswer_layer178
            @Override // org.telegram.tgnet.TLRPC$TL_pollAnswer, org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = new TLRPC$TL_textWithEntities();
                this.text = tLRPC$TL_textWithEntities;
                tLRPC$TL_textWithEntities.text = abstractSerializedData2.readString(z2);
                this.option = abstractSerializedData2.readByteArray(z2);
            }

            @Override // org.telegram.tgnet.TLRPC$TL_pollAnswer, org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(1823064809);
                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = this.text;
                abstractSerializedData2.writeString(tLRPC$TL_textWithEntities == null ? "" : tLRPC$TL_textWithEntities.text);
                abstractSerializedData2.writeByteArray(this.option);
            }
        } : new TLRPC$TL_pollAnswer();
        if (tLRPC$TL_pollAnswer == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in JSONValue", Integer.valueOf(i)));
        }
        if (tLRPC$TL_pollAnswer != null) {
            tLRPC$TL_pollAnswer.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_pollAnswer;
    }
}
