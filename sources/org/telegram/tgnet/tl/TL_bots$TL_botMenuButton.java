package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
/* loaded from: classes3.dex */
public class TL_bots$TL_botMenuButton extends TL_bots$BotMenuButton {
    public String text;
    public String url;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-944407322);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeString(this.url);
    }
}
