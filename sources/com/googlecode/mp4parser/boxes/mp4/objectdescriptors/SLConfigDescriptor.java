package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class SLConfigDescriptor extends BaseDescriptor {
    int predefined;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.predefined == ((SLConfigDescriptor) obj).predefined;
    }

    public int hashCode() {
        return this.predefined;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public void parseDetail(ByteBuffer byteBuffer) {
        this.predefined = IsoTypeReader.readUInt8(byteBuffer);
    }

    public ByteBuffer serialize() {
        ByteBuffer allocate = ByteBuffer.allocate(3);
        IsoTypeWriter.writeUInt8(allocate, 6);
        IsoTypeWriter.writeUInt8(allocate, 1);
        IsoTypeWriter.writeUInt8(allocate, this.predefined);
        return allocate;
    }

    public int serializedSize() {
        return 3;
    }

    public void setPredefined(int i) {
        this.predefined = i;
    }

    public String toString() {
        return "SLConfigDescriptor{predefined=" + this.predefined + '}';
    }
}
