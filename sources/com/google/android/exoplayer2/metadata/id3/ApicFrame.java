package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class ApicFrame extends Id3Frame {
    public static final Parcelable.Creator<ApicFrame> CREATOR = new Parcelable.Creator() { // from class: com.google.android.exoplayer2.metadata.id3.ApicFrame.1
        @Override // android.os.Parcelable.Creator
        public ApicFrame createFromParcel(Parcel parcel) {
            return new ApicFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ApicFrame[] newArray(int i) {
            return new ApicFrame[i];
        }
    };
    public final String description;
    public final String mimeType;
    public final byte[] pictureData;
    public final int pictureType;

    ApicFrame(Parcel parcel) {
        super("APIC");
        this.mimeType = (String) Util.castNonNull(parcel.readString());
        this.description = parcel.readString();
        this.pictureType = parcel.readInt();
        this.pictureData = (byte[]) Util.castNonNull(parcel.createByteArray());
    }

    public ApicFrame(String str, String str2, int i, byte[] bArr) {
        super("APIC");
        this.mimeType = str;
        this.description = str2;
        this.pictureType = i;
        this.pictureData = bArr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ApicFrame.class != obj.getClass()) {
            return false;
        }
        ApicFrame apicFrame = (ApicFrame) obj;
        return this.pictureType == apicFrame.pictureType && Util.areEqual(this.mimeType, apicFrame.mimeType) && Util.areEqual(this.description, apicFrame.description) && Arrays.equals(this.pictureData, apicFrame.pictureData);
    }

    public int hashCode() {
        int i = (this.pictureType + 527) * 31;
        String str = this.mimeType;
        int hashCode = (i + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.description;
        return ((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + Arrays.hashCode(this.pictureData);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame, com.google.android.exoplayer2.metadata.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder builder) {
        builder.maybeSetArtworkData(this.pictureData, this.pictureType);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.id + ": mimeType=" + this.mimeType + ", description=" + this.description;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mimeType);
        parcel.writeString(this.description);
        parcel.writeInt(this.pictureType);
        parcel.writeByteArray(this.pictureData);
    }
}
