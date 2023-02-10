package org.telegram.tgnet;

import android.graphics.Path;
import org.telegram.messenger.SvgHelper;
/* loaded from: classes.dex */
public class TLRPC$TL_photoPathSize extends TLRPC$PhotoSize {
    public static int constructor = -668906175;
    public Path svgPath;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.type = abstractSerializedData.readString(z);
        byte[] readByteArray = abstractSerializedData.readByteArray(z);
        this.bytes = readByteArray;
        this.h = 50;
        this.w = 50;
        this.svgPath = SvgHelper.doPath(SvgHelper.decompress(readByteArray));
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.type);
        abstractSerializedData.writeByteArray(this.bytes);
    }
}
