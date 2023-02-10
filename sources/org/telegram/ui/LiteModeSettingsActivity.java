package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class LiteModeSettingsActivity extends BaseFragment {
    TextCheckCell animatedBackground;
    TextCheckCell animatedEmoji;
    TextCheckCell enableMode;
    ArrayList<TextCheckCell> checkBoxViews = new ArrayList<>();
    SharedConfig.LiteMode liteMode = SharedConfig.getLiteMode();

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("LightMode", R.string.LightMode));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LiteModeSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    LiteModeSettingsActivity.this.finishFragment();
                }
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        this.fragmentView = linearLayout;
        TextCheckCell textCheckCell = new TextCheckCell(context);
        this.enableMode = textCheckCell;
        textCheckCell.setHeight(56);
        this.enableMode.setTextAndCheck(LocaleController.getString("EnableLightMode", R.string.EnableLightMode), SharedConfig.getLiteMode().enabled(), false);
        TextCheckCell textCheckCell2 = this.enableMode;
        textCheckCell2.setBackgroundColor(Theme.getColor(textCheckCell2.isChecked() ? "windowBackgroundChecked" : "windowBackgroundUnchecked"));
        this.enableMode.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.enableMode.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$0(view);
            }
        });
        linearLayout.addView(this.enableMode, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(context, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
        linearLayout.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
        TextCheckCell textCheckCell3 = new TextCheckCell(context);
        this.animatedEmoji = textCheckCell3;
        textCheckCell3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.animatedEmoji, LayoutHelper.createLinear(-1, -2));
        this.animatedEmoji.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$1(view);
            }
        });
        TextCheckCell textCheckCell4 = new TextCheckCell(context);
        this.animatedBackground = textCheckCell4;
        textCheckCell4.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView(this.animatedBackground, LayoutHelper.createLinear(-1, -2));
        this.animatedBackground.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$2(view);
            }
        });
        update();
        this.checkBoxViews.add(this.animatedEmoji);
        this.checkBoxViews.add(this.animatedBackground);
        updateEnableMode();
        updateColors();
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        SharedConfig.getLiteMode().toggleMode();
        updateEnableMode();
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        SharedConfig.LiteMode liteMode = this.liteMode;
        liteMode.animatedEmoji = !liteMode.animatedEmoji;
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        SharedConfig.LiteMode liteMode = this.liteMode;
        liteMode.animatedBackground = !liteMode.animatedBackground;
        update();
    }

    private void update() {
        this.animatedEmoji.setTextAndCheck("Animated Emoji", this.liteMode.animatedEmojiEnabled(), true);
        this.animatedBackground.setTextAndCheck("Animated Backgrounds", this.liteMode.animatedBackgroundEnabled(), true);
    }

    private void updateEnableMode() {
        boolean enabled = SharedConfig.getLiteMode().enabled();
        this.enableMode.setChecked(enabled);
        int color = Theme.getColor(enabled ? "windowBackgroundChecked" : "windowBackgroundUnchecked");
        if (enabled) {
            this.enableMode.setBackgroundColorAnimated(enabled, color);
        } else {
            this.enableMode.setBackgroundColorAnimatedReverse(color);
        }
        for (int i = 0; i < this.checkBoxViews.size(); i++) {
            this.checkBoxViews.get(i).setVisibility(enabled ? 0 : 8);
        }
    }

    @SuppressLint({"NotifyDataSetChanged"})
    private void updateColors() {
        this.enableMode.setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
        this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.liteMode.savePreference();
        AnimatedEmojiDrawable.lightModeChanged();
        Theme.reloadWallpaper();
    }
}
