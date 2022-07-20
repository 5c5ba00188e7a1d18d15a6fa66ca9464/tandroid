package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$help_CountriesList extends TLObject {
    public static TLRPC$help_CountriesList TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$help_CountriesList tLRPC$help_CountriesList;
        if (i != -2016381538) {
            tLRPC$help_CountriesList = i != -1815339214 ? null : new TLRPC$TL_help_countriesListNotModified();
        } else {
            tLRPC$help_CountriesList = new TLRPC$TL_help_countriesList();
        }
        if (tLRPC$help_CountriesList != null || !z) {
            if (tLRPC$help_CountriesList != null) {
                tLRPC$help_CountriesList.readParams(abstractSerializedData, z);
            }
            return tLRPC$help_CountriesList;
        }
        throw new RuntimeException(String.format("can't parse magic %x in help_CountriesList", Integer.valueOf(i)));
    }
}
