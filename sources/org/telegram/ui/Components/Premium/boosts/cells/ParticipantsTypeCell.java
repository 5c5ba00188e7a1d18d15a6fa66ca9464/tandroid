package org.telegram.ui.Components.Premium.boosts.cells;

import android.content.Context;
import java.util.List;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_help_country;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ParticipantsTypeCell extends BaseCell {
    public static int TYPE_ALL = 0;
    public static int TYPE_NEW = 1;
    private int selectedType;

    public ParticipantsTypeCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        this.imageView.setVisibility(8);
    }

    public int getSelectedType() {
        return this.selectedType;
    }

    @Override // org.telegram.ui.Components.Premium.boosts.cells.BaseCell
    protected boolean needCheck() {
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setType(int i, boolean z, boolean z2, List list, TLRPC$Chat tLRPC$Chat) {
        SimpleTextView simpleTextView;
        String formatString;
        this.selectedType = i;
        boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
        if (i != TYPE_ALL) {
            if (i == TYPE_NEW) {
                simpleTextView = this.titleTextView;
                formatString = LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.BoostingNewSubscribers : R.string.BoostingNewMembers, new Object[0]);
            }
            this.radioButton.setChecked(z, false);
            setDivider(z2);
            this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
            setSubtitle(withArrow(list.size() != 0 ? LocaleController.getString(R.string.BoostingFromAllCountries) : list.size() <= 3 ? list.size() == 1 ? LocaleController.formatString("BoostingFromAllCountries1", R.string.BoostingFromAllCountries1, ((TLRPC$TL_help_country) list.get(0)).default_name) : list.size() == 2 ? LocaleController.formatString("BoostingFromAllCountries2", R.string.BoostingFromAllCountries2, ((TLRPC$TL_help_country) list.get(0)).default_name, ((TLRPC$TL_help_country) list.get(1)).default_name) : LocaleController.formatString("BoostingFromAllCountries3", R.string.BoostingFromAllCountries3, ((TLRPC$TL_help_country) list.get(0)).default_name, ((TLRPC$TL_help_country) list.get(1)).default_name, ((TLRPC$TL_help_country) list.get(2)).default_name) : LocaleController.formatPluralString("BoostingFromCountriesCount", list.size(), new Object[0])));
        }
        simpleTextView = this.titleTextView;
        formatString = LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.BoostingAllSubscribers : R.string.BoostingAllMembers, new Object[0]);
        simpleTextView.setText(formatString);
        this.radioButton.setChecked(z, false);
        setDivider(z2);
        this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2, this.resourcesProvider));
        setSubtitle(withArrow(list.size() != 0 ? LocaleController.getString(R.string.BoostingFromAllCountries) : list.size() <= 3 ? list.size() == 1 ? LocaleController.formatString("BoostingFromAllCountries1", R.string.BoostingFromAllCountries1, ((TLRPC$TL_help_country) list.get(0)).default_name) : list.size() == 2 ? LocaleController.formatString("BoostingFromAllCountries2", R.string.BoostingFromAllCountries2, ((TLRPC$TL_help_country) list.get(0)).default_name, ((TLRPC$TL_help_country) list.get(1)).default_name) : LocaleController.formatString("BoostingFromAllCountries3", R.string.BoostingFromAllCountries3, ((TLRPC$TL_help_country) list.get(0)).default_name, ((TLRPC$TL_help_country) list.get(1)).default_name, ((TLRPC$TL_help_country) list.get(2)).default_name) : LocaleController.formatPluralString("BoostingFromCountriesCount", list.size(), new Object[0])));
    }
}
