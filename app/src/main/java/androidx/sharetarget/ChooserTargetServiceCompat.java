package androidx.sharetarget;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.util.Log;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.sharetarget.ShareTargetCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes3.dex */
public class ChooserTargetServiceCompat extends ChooserTargetService {
    static final String TAG = "ChooserServiceCompat";

    @Override // android.service.chooser.ChooserTargetService
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName, IntentFilter matchedFilter) {
        Context context = getApplicationContext();
        List<ShareTargetCompat> targets = ShareTargetXmlParser.getShareTargets(context);
        List<ShareTargetCompat> matchedTargets = new ArrayList<>();
        for (ShareTargetCompat target : targets) {
            if (target.mTargetClass.equals(targetActivityName.getClassName())) {
                ShareTargetCompat.TargetData[] targetDataArr = target.mTargetData;
                int length = targetDataArr.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        ShareTargetCompat.TargetData data = targetDataArr[i];
                        if (!matchedFilter.hasDataType(data.mMimeType)) {
                            i++;
                        } else {
                            matchedTargets.add(target);
                            break;
                        }
                    }
                }
            }
        }
        if (matchedTargets.isEmpty()) {
            return Collections.emptyList();
        }
        ShortcutInfoCompatSaverImpl shortcutSaver = ShortcutInfoCompatSaverImpl.getInstance(context);
        try {
            List<ShortcutInfoCompat> shortcuts = shortcutSaver.getShortcuts();
            if (shortcuts == null || shortcuts.isEmpty()) {
                return Collections.emptyList();
            }
            List<ShortcutHolder> matchedShortcuts = new ArrayList<>();
            for (ShortcutInfoCompat shortcut : shortcuts) {
                Iterator<ShareTargetCompat> it = matchedTargets.iterator();
                while (true) {
                    if (it.hasNext()) {
                        ShareTargetCompat item = it.next();
                        if (shortcut.getCategories().containsAll(Arrays.asList(item.mCategories))) {
                            matchedShortcuts.add(new ShortcutHolder(shortcut, new ComponentName(context.getPackageName(), item.mTargetClass)));
                            break;
                        }
                    }
                }
            }
            return convertShortcutsToChooserTargets(shortcutSaver, matchedShortcuts);
        } catch (Exception e) {
            Log.e(TAG, "Failed to retrieve shortcuts: ", e);
            return Collections.emptyList();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static List<ChooserTarget> convertShortcutsToChooserTargets(ShortcutInfoCompatSaverImpl shortcutSaver, List<ShortcutHolder> matchedShortcuts) {
        IconCompat shortcutIcon;
        Exception e;
        if (matchedShortcuts.isEmpty()) {
            return new ArrayList();
        }
        Collections.sort(matchedShortcuts);
        ArrayList<ChooserTarget> chooserTargets = new ArrayList<>();
        int lastRank = matchedShortcuts.get(0).getShortcut().getRank();
        int lastRank2 = lastRank;
        float currentScore = 1.0f;
        for (ShortcutHolder holder : matchedShortcuts) {
            ShortcutInfoCompat shortcut = holder.getShortcut();
            try {
            } catch (Exception e2) {
                e = e2;
            }
            try {
                shortcutIcon = shortcutSaver.getShortcutIcon(shortcut.getId());
            } catch (Exception e3) {
                e = e3;
                Log.e(TAG, "Failed to retrieve shortcut icon: ", e);
                shortcutIcon = null;
                Bundle extras = new Bundle();
                extras.putString(ShortcutManagerCompat.EXTRA_SHORTCUT_ID, shortcut.getId());
                if (lastRank2 != shortcut.getRank()) {
                }
                chooserTargets.add(new ChooserTarget(shortcut.getShortLabel(), shortcutIcon != null ? null : shortcutIcon.toIcon(), currentScore, holder.getTargetClass(), extras));
            }
            Bundle extras2 = new Bundle();
            extras2.putString(ShortcutManagerCompat.EXTRA_SHORTCUT_ID, shortcut.getId());
            if (lastRank2 != shortcut.getRank()) {
                currentScore -= 0.01f;
                lastRank2 = shortcut.getRank();
            }
            chooserTargets.add(new ChooserTarget(shortcut.getShortLabel(), shortcutIcon != null ? null : shortcutIcon.toIcon(), currentScore, holder.getTargetClass(), extras2));
        }
        return chooserTargets;
    }

    /* loaded from: classes3.dex */
    public static class ShortcutHolder implements Comparable<ShortcutHolder> {
        private final ShortcutInfoCompat mShortcut;
        private final ComponentName mTargetClass;

        ShortcutHolder(ShortcutInfoCompat shortcut, ComponentName targetClass) {
            this.mShortcut = shortcut;
            this.mTargetClass = targetClass;
        }

        ShortcutInfoCompat getShortcut() {
            return this.mShortcut;
        }

        ComponentName getTargetClass() {
            return this.mTargetClass;
        }

        public int compareTo(ShortcutHolder other) {
            return getShortcut().getRank() - other.getShortcut().getRank();
        }
    }
}
