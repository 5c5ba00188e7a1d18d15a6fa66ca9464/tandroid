package org.telegram.ui.bots;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.bots.BotBiometry;

/* loaded from: classes4.dex */
public class BotBiometrySettings extends BaseFragment {
    private final ArrayList biometryBots = new ArrayList();
    private final HashMap botName = new HashMap();
    private UniversalRecyclerView listView;

    /* JADX INFO: Access modifiers changed from: private */
    public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        for (int i = 0; i < this.biometryBots.size(); i++) {
            BotBiometry.Bot bot = (BotBiometry.Bot) this.biometryBots.get(i);
            SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) this.botName.get(bot);
            if (spannableStringBuilder == null) {
                spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append((CharSequence) "a   ");
                AvatarSpan avatarSpan = new AvatarSpan(null, this.currentAccount, 24.0f);
                avatarSpan.setUser(bot.user);
                spannableStringBuilder.setSpan(avatarSpan, 0, 1, 33);
                spannableStringBuilder.append((CharSequence) UserObject.getUserName(bot.user));
                this.botName.put(bot, spannableStringBuilder);
            }
            arrayList.add(UItem.asCheck(i, spannableStringBuilder).setChecked(!bot.disabled));
        }
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.PrivacyBiometryBotsInfo)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        UniversalAdapter universalAdapter;
        this.biometryBots.clear();
        this.biometryBots.addAll(arrayList);
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || (universalAdapter = universalRecyclerView.adapter) == null) {
            return;
        }
        universalAdapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2;
        UniversalAdapter universalAdapter;
        if (uItem.viewType != 4 || (i2 = uItem.id) < 0 || i2 >= this.biometryBots.size()) {
            return;
        }
        BotBiometry.Bot bot = (BotBiometry.Bot) this.biometryBots.get(uItem.id);
        bot.disabled = !bot.disabled;
        BotBiometry.toggleBotDisabled(getContext(), this.currentAccount, bot.user.id, bot.disabled);
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || (universalAdapter = universalRecyclerView.adapter) == null) {
            return;
        }
        universalAdapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString(R.string.PrivacyBiometryBots));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.bots.BotBiometrySettings.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    BotBiometrySettings.this.lambda$onBackPressed$321();
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray, this.resourceProvider));
        UniversalRecyclerView universalRecyclerView = new UniversalRecyclerView(this, new Utilities.Callback2() { // from class: org.telegram.ui.bots.BotBiometrySettings$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                BotBiometrySettings.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, new Utilities.Callback5() { // from class: org.telegram.ui.bots.BotBiometrySettings$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.Utilities.Callback5
            public final void run(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                BotBiometrySettings.this.onClick((UItem) obj, (View) obj2, ((Integer) obj3).intValue(), ((Float) obj4).floatValue(), ((Float) obj5).floatValue());
            }
        }, new Utilities.Callback5Return() { // from class: org.telegram.ui.bots.BotBiometrySettings$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback5Return
            public final Object run(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                boolean onLongClick;
                onLongClick = BotBiometrySettings.this.onLongClick((UItem) obj, (View) obj2, ((Integer) obj3).intValue(), ((Float) obj4).floatValue(), ((Float) obj5).floatValue());
                return Boolean.valueOf(onLongClick);
            }
        });
        this.listView = universalRecyclerView;
        frameLayout.addView(universalRecyclerView, LayoutHelper.createFrame(-1, -1, 119));
        BotBiometry.getBots(getContext(), this.currentAccount, new Utilities.Callback() { // from class: org.telegram.ui.bots.BotBiometrySettings$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                BotBiometrySettings.this.lambda$createView$0((ArrayList) obj);
            }
        });
        this.fragmentView = frameLayout;
        return frameLayout;
    }
}
