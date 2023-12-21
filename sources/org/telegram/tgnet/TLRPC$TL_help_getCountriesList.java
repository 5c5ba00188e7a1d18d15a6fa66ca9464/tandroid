package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_help_getCountriesList extends TLObject {
    public int hash;
    public String lang_code;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$help_CountriesList.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1935116200);
        abstractSerializedData.writeString(this.lang_code);
        abstractSerializedData.writeInt32(this.hash);
    }
}
