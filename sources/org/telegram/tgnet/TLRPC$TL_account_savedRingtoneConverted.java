package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_savedRingtoneConverted extends TLRPC$account_SavedRingtone {
    public TLRPC$Document document;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.document = TLRPC$Document.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(523271863);
        this.document.serializeToStream(abstractSerializedData);
    }
}
