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
    LinearLayout contentView;
    TextCheckCell enableMode;
    TextCheckCell other;
    TextCheckCell topicsInRightMenu;
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
        this.contentView = linearLayout;
        linearLayout.setOrientation(1);
        this.fragmentView = this.contentView;
        TextCheckCell textCheckCell = new TextCheckCell(context);
        this.enableMode = textCheckCell;
        textCheckCell.setHeight(56);
        this.enableMode.setTextAndCheck(LocaleController.getString("EnableLightMode", R.string.EnableLightMode), SharedConfig.getLiteMode().enabled, false);
        TextCheckCell textCheckCell2 = this.enableMode;
        textCheckCell2.setBackgroundColor(Theme.getColor(textCheckCell2.isChecked() ? "windowBackgroundChecked" : "windowBackgroundUnchecked"));
        this.enableMode.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.enableMode.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$0(view);
            }
        });
        this.contentView.addView(this.enableMode, LayoutHelper.createLinear(-1, -2));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(context, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
        this.contentView.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
        TextCheckCell createCheckCell = createCheckCell(getContext());
        this.animatedEmoji = createCheckCell;
        createCheckCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$1(view);
            }
        });
        TextCheckCell createCheckCell2 = createCheckCell(getContext());
        this.animatedBackground = createCheckCell2;
        createCheckCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$2(view);
            }
        });
        TextCheckCell createCheckCell3 = createCheckCell(getContext());
        this.topicsInRightMenu = createCheckCell3;
        createCheckCell3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$3(view);
            }
        });
        TextCheckCell createCheckCell4 = createCheckCell(getContext());
        this.other = createCheckCell4;
        createCheckCell4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LiteModeSettingsActivity.this.lambda$createView$4(view);
            }
        });
        update();
        update();
        this.checkBoxViews.add(this.animatedEmoji);
        this.checkBoxViews.add(this.animatedBackground);
        this.checkBoxViews.add(this.topicsInRightMenu);
        this.checkBoxViews.add(this.other);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        SharedConfig.LiteMode liteMode = this.liteMode;
        liteMode.topicsInRightMenu = !liteMode.topicsInRightMenu;
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        SharedConfig.LiteMode liteMode = this.liteMode;
        liteMode.other = !liteMode.other;
        update();
    }

    private TextCheckCell createCheckCell(Context context) {
        TextCheckCell textCheckCell = new TextCheckCell(context);
        textCheckCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.contentView.addView(textCheckCell, LayoutHelper.createLinear(-1, -2));
        return textCheckCell;
    }

    private void update() {
        this.animatedEmoji.setTextAndCheck("Animated Emoji", this.liteMode.animatedEmoji, true);
        this.animatedBackground.setTextAndCheck("Animated Backgrounds", this.liteMode.animatedBackground, true);
        this.other.setTextAndCheck("Other", this.liteMode.other, true);
        this.topicsInRightMenu.setTextAndCheck("Topics in Right Menu", this.liteMode.topicsInRightMenu, true);
    }

    private void updateEnableMode() {
        boolean z = SharedConfig.getLiteMode().enabled;
        this.enableMode.setChecked(z);
        int color = Theme.getColor(z ? "windowBackgroundChecked" : "windowBackgroundUnchecked");
        if (z) {
            this.enableMode.setBackgroundColorAnimated(z, color);
        } else {
            this.enableMode.setBackgroundColorAnimatedReverse(color);
        }
        for (int i = 0; i < this.checkBoxViews.size(); i++) {
            this.checkBoxViews.get(i).setVisibility(z ? 0 : 8);
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
