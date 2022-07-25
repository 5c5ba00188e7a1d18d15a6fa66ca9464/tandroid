package androidx.core.content.pm;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class ShortcutInfoCompatSaver<T> {
    /* renamed from: addShortcuts */
    public abstract T mo31addShortcuts(List<ShortcutInfoCompat> shortcuts);

    /* renamed from: removeAllShortcuts */
    public abstract T mo32removeAllShortcuts();

    /* renamed from: removeShortcuts */
    public abstract T mo33removeShortcuts(List<String> shortcutIds);

    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList();
    }

    /* loaded from: classes.dex */
    public static class NoopImpl extends ShortcutInfoCompatSaver<Void> {
        @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
        /* renamed from: addShortcuts  reason: avoid collision after fix types in other method */
        public Void mo31addShortcuts(List<ShortcutInfoCompat> shortcuts) {
            return null;
        }

        @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
        /* renamed from: removeAllShortcuts  reason: avoid collision after fix types in other method */
        public Void mo32removeAllShortcuts() {
            return null;
        }

        @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
        /* renamed from: removeShortcuts  reason: avoid collision after fix types in other method */
        public Void mo33removeShortcuts(List<String> shortcutIds) {
            return null;
        }

        @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
        /* renamed from: addShortcuts */
        public /* bridge */ /* synthetic */ Void mo31addShortcuts(List shortcuts) {
            return mo31addShortcuts((List<ShortcutInfoCompat>) shortcuts);
        }

        @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
        /* renamed from: removeShortcuts */
        public /* bridge */ /* synthetic */ Void mo33removeShortcuts(List shortcutIds) {
            return mo33removeShortcuts((List<String>) shortcutIds);
        }
    }
}
