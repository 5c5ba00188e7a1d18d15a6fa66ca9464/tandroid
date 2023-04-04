package org.telegram.ui.Components.FloatingDebug;

import java.util.List;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
/* loaded from: classes.dex */
public interface FloatingDebugProvider {
    List<FloatingDebugController.DebugItem> onGetDebugItems();
}
