package com.google.android.exoplayer2.metadata.vorbis;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class VorbisComment extends com.google.android.exoplayer2.metadata.flac.VorbisComment {
    public static final Parcelable.Creator<VorbisComment> CREATOR = new Parcelable.Creator() { // from class: com.google.android.exoplayer2.metadata.vorbis.VorbisComment.1
        @Override // android.os.Parcelable.Creator
        public VorbisComment createFromParcel(Parcel parcel) {
            return new VorbisComment(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public VorbisComment[] newArray(int i) {
            return new VorbisComment[i];
        }
    };

    VorbisComment(Parcel parcel) {
        super(parcel);
    }

    public VorbisComment(String str, String str2) {
        super(str, str2);
    }
}
