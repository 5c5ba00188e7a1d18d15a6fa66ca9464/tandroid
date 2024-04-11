package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_setReactionsNotifySettings extends TLObject {
    public TLRPC$TL_reactionsNotifySettings settings;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_reactionsNotifySettings.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(829220168);
        this.settings.serializeToStream(abstractSerializedData);
    }
}
