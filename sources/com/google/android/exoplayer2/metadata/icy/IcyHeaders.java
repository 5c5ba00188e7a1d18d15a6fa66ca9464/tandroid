package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class IcyHeaders implements Metadata.Entry {
    public static final Parcelable.Creator<IcyHeaders> CREATOR = new Parcelable.Creator() { // from class: com.google.android.exoplayer2.metadata.icy.IcyHeaders.1
        @Override // android.os.Parcelable.Creator
        public IcyHeaders createFromParcel(Parcel parcel) {
            return new IcyHeaders(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public IcyHeaders[] newArray(int i) {
            return new IcyHeaders[i];
        }
    };
    public final int bitrate;
    public final String genre;
    public final boolean isPublic;
    public final int metadataInterval;
    public final String name;
    public final String url;

    public IcyHeaders(int i, String str, String str2, String str3, boolean z, int i2) {
        Assertions.checkArgument(i2 == -1 || i2 > 0);
        this.bitrate = i;
        this.genre = str;
        this.name = str2;
        this.url = str3;
        this.isPublic = z;
        this.metadataInterval = i2;
    }

    IcyHeaders(Parcel parcel) {
        this.bitrate = parcel.readInt();
        this.genre = parcel.readString();
        this.name = parcel.readString();
        this.url = parcel.readString();
        this.isPublic = Util.readBoolean(parcel);
        this.metadataInterval = parcel.readInt();
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static IcyHeaders parse(Map map) {
        boolean z;
        int i;
        List list;
        String str;
        List list2;
        String str2;
        List list3;
        String str3;
        List list4;
        boolean z2;
        List list5;
        int i2;
        int parseInt;
        int i3;
        List list6 = (List) map.get("icy-br");
        boolean z3 = true;
        int i4 = -1;
        if (list6 != null) {
            String str4 = (String) list6.get(0);
            try {
                i3 = Integer.parseInt(str4) * 1000;
            } catch (NumberFormatException unused) {
                i3 = -1;
            }
            if (i3 > 0) {
                i = i3;
                z = true;
                list = (List) map.get("icy-genre");
                if (list != null) {
                    str = (String) list.get(0);
                    z = true;
                } else {
                    str = null;
                }
                list2 = (List) map.get("icy-name");
                if (list2 != null) {
                    str2 = (String) list2.get(0);
                    z = true;
                } else {
                    str2 = null;
                }
                list3 = (List) map.get("icy-url");
                if (list3 != null) {
                    str3 = (String) list3.get(0);
                    z = true;
                } else {
                    str3 = null;
                }
                list4 = (List) map.get("icy-pub");
                if (list4 != null) {
                    z2 = ((String) list4.get(0)).equals("1");
                    z = true;
                } else {
                    z2 = false;
                }
                list5 = (List) map.get("icy-metaint");
                if (list5 != null) {
                    String str5 = (String) list5.get(0);
                    try {
                        parseInt = Integer.parseInt(str5);
                    } catch (NumberFormatException unused2) {
                    }
                    if (parseInt > 0) {
                        i2 = parseInt;
                        if (z3) {
                            return new IcyHeaders(i, str, str2, str3, z2, i2);
                        }
                        return null;
                    }
                    try {
                        Log.w("IcyHeaders", "Invalid metadata interval: " + str5);
                    } catch (NumberFormatException unused3) {
                        i4 = parseInt;
                        Log.w("IcyHeaders", "Invalid metadata interval: " + str5);
                        z3 = z;
                        i2 = i4;
                        if (z3) {
                        }
                    }
                }
                z3 = z;
                i2 = -1;
                if (z3) {
                }
            } else {
                try {
                    Log.w("IcyHeaders", "Invalid bitrate: " + str4);
                } catch (NumberFormatException unused4) {
                    Log.w("IcyHeaders", "Invalid bitrate header: " + str4);
                    i = i3;
                    z = false;
                    list = (List) map.get("icy-genre");
                    if (list != null) {
                    }
                    list2 = (List) map.get("icy-name");
                    if (list2 != null) {
                    }
                    list3 = (List) map.get("icy-url");
                    if (list3 != null) {
                    }
                    list4 = (List) map.get("icy-pub");
                    if (list4 != null) {
                    }
                    list5 = (List) map.get("icy-metaint");
                    if (list5 != null) {
                    }
                    z3 = z;
                    i2 = -1;
                    if (z3) {
                    }
                }
            }
        }
        z = false;
        i = -1;
        list = (List) map.get("icy-genre");
        if (list != null) {
        }
        list2 = (List) map.get("icy-name");
        if (list2 != null) {
        }
        list3 = (List) map.get("icy-url");
        if (list3 != null) {
        }
        list4 = (List) map.get("icy-pub");
        if (list4 != null) {
        }
        list5 = (List) map.get("icy-metaint");
        if (list5 != null) {
        }
        z3 = z;
        i2 = -1;
        if (z3) {
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || IcyHeaders.class != obj.getClass()) {
            return false;
        }
        IcyHeaders icyHeaders = (IcyHeaders) obj;
        return this.bitrate == icyHeaders.bitrate && Util.areEqual(this.genre, icyHeaders.genre) && Util.areEqual(this.name, icyHeaders.name) && Util.areEqual(this.url, icyHeaders.url) && this.isPublic == icyHeaders.isPublic && this.metadataInterval == icyHeaders.metadataInterval;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public /* synthetic */ byte[] getWrappedMetadataBytes() {
        return Metadata.Entry.-CC.$default$getWrappedMetadataBytes(this);
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public /* synthetic */ Format getWrappedMetadataFormat() {
        return Metadata.Entry.-CC.$default$getWrappedMetadataFormat(this);
    }

    public int hashCode() {
        int i = (this.bitrate + 527) * 31;
        String str = this.genre;
        int hashCode = (i + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.name;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.url;
        return ((((hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31) + (this.isPublic ? 1 : 0)) * 31) + this.metadataInterval;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder builder) {
        String str = this.name;
        if (str != null) {
            builder.setStation(str);
        }
        String str2 = this.genre;
        if (str2 != null) {
            builder.setGenre(str2);
        }
    }

    public String toString() {
        return "IcyHeaders: name=\"" + this.name + "\", genre=\"" + this.genre + "\", bitrate=" + this.bitrate + ", metadataInterval=" + this.metadataInterval;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bitrate);
        parcel.writeString(this.genre);
        parcel.writeString(this.name);
        parcel.writeString(this.url);
        Util.writeBoolean(parcel, this.isPublic);
        parcel.writeInt(this.metadataInterval);
    }
}
