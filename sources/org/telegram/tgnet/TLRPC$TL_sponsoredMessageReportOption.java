package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_sponsoredMessageReportOption extends TLObject {
    public static int constructor = 1124938064;
    public byte[] option;
    public String text;

    public static TLRPC$TL_sponsoredMessageReportOption TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (constructor != i) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_sponsoredMessageReportOption", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_sponsoredMessageReportOption tLRPC$TL_sponsoredMessageReportOption = new TLRPC$TL_sponsoredMessageReportOption();
        tLRPC$TL_sponsoredMessageReportOption.readParams(abstractSerializedData, z);
        return tLRPC$TL_sponsoredMessageReportOption;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.option = abstractSerializedData.readByteArray(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeByteArray(this.option);
    }
}
