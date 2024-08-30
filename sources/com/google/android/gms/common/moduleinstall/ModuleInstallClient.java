package com.google.android.gms.common.moduleinstall;

import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
public interface ModuleInstallClient {
    Task areModulesAvailable(OptionalModuleApi... optionalModuleApiArr);

    Task installModules(ModuleInstallRequest moduleInstallRequest);
}
