package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
abstract class Atom {
    public final int type;

    /* loaded from: classes.dex */
    static final class ContainerAtom extends Atom {
        public final List containerChildren;
        public final long endPosition;
        public final List leafChildren;

        public ContainerAtom(int i, long j) {
            super(i);
            this.endPosition = j;
            this.leafChildren = new ArrayList();
            this.containerChildren = new ArrayList();
        }

        public void add(ContainerAtom containerAtom) {
            this.containerChildren.add(containerAtom);
        }

        public void add(LeafAtom leafAtom) {
            this.leafChildren.add(leafAtom);
        }

        public ContainerAtom getContainerAtomOfType(int i) {
            int size = this.containerChildren.size();
            for (int i2 = 0; i2 < size; i2++) {
                ContainerAtom containerAtom = (ContainerAtom) this.containerChildren.get(i2);
                if (containerAtom.type == i) {
                    return containerAtom;
                }
            }
            return null;
        }

        public LeafAtom getLeafAtomOfType(int i) {
            int size = this.leafChildren.size();
            for (int i2 = 0; i2 < size; i2++) {
                LeafAtom leafAtom = (LeafAtom) this.leafChildren.get(i2);
                if (leafAtom.type == i) {
                    return leafAtom;
                }
            }
            return null;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.Atom
        public String toString() {
            return Atom.getAtomTypeString(this.type) + " leaves: " + Arrays.toString(this.leafChildren.toArray()) + " containers: " + Arrays.toString(this.containerChildren.toArray());
        }
    }

    /* loaded from: classes.dex */
    static final class LeafAtom extends Atom {
        public final ParsableByteArray data;

        public LeafAtom(int i, ParsableByteArray parsableByteArray) {
            super(i);
            this.data = parsableByteArray;
        }
    }

    public Atom(int i) {
        this.type = i;
    }

    public static String getAtomTypeString(int i) {
        return "" + ((char) ((i >> 24) & NotificationCenter.closeSearchByActiveAction)) + ((char) ((i >> 16) & NotificationCenter.closeSearchByActiveAction)) + ((char) ((i >> 8) & NotificationCenter.closeSearchByActiveAction)) + ((char) (i & NotificationCenter.closeSearchByActiveAction));
    }

    public static int parseFullAtomFlags(int i) {
        return i & 16777215;
    }

    public static int parseFullAtomVersion(int i) {
        return (i >> 24) & NotificationCenter.closeSearchByActiveAction;
    }

    public String toString() {
        return getAtomTypeString(this.type);
    }
}
