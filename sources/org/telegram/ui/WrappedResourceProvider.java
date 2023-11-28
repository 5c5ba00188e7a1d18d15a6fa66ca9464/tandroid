package org.telegram.ui;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class WrappedResourceProvider implements Theme.ResourcesProvider {
    Theme.ResourcesProvider resourcesProvider;
    public SparseIntArray sparseIntArray = new SparseIntArray();

    public void appendColors() {
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public /* synthetic */ boolean isDark() {
        boolean isCurrentThemeDark;
        isCurrentThemeDark = Theme.isCurrentThemeDark();
        return isCurrentThemeDark;
    }

    public WrappedResourceProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        appendColors();
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public int getColor(int i) {
        int indexOfKey = this.sparseIntArray.indexOfKey(i);
        if (indexOfKey >= 0) {
            return this.sparseIntArray.valueAt(indexOfKey);
        }
        return this.resourcesProvider.getColor(i);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public int getColorOrDefault(int i) {
        return this.resourcesProvider.getColorOrDefault(i);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public int getCurrentColor(int i) {
        return this.resourcesProvider.getCurrentColor(i);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public void setAnimatedColor(int i, int i2) {
        this.resourcesProvider.setAnimatedColor(i, i2);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public Drawable getDrawable(String str) {
        return this.resourcesProvider.getDrawable(str);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public Paint getPaint(String str) {
        return this.resourcesProvider.getPaint(str);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public boolean hasGradientService() {
        return this.resourcesProvider.hasGradientService();
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
        this.resourcesProvider.applyServiceShaderMatrix(i, i2, f, f2);
    }

    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
    public ColorFilter getAnimatedEmojiColorFilter() {
        return this.resourcesProvider.getAnimatedEmojiColorFilter();
    }
}
