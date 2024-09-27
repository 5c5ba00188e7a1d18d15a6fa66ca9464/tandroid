package org.telegram.ui.Components.Premium.boosts.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.cells.ActionBtnCell;
import org.telegram.ui.Components.Premium.boosts.cells.HeaderCell;
import org.telegram.ui.Components.Premium.boosts.cells.LinkCell;
import org.telegram.ui.Components.Premium.boosts.cells.TableCell;
import org.telegram.ui.Components.Premium.boosts.cells.TextInfoCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.TopicsFragment;
/* loaded from: classes3.dex */
public abstract class GiftInfoAdapter extends RecyclerListView.SelectionAdapter {
    private BaseFragment baseFragment;
    private FrameLayout container;
    private TLRPC.TL_payments_checkedGiftCode giftCode;
    private boolean isUnused;
    private final Theme.ResourcesProvider resourcesProvider;
    private String slug;

    public GiftInfoAdapter(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    public /* synthetic */ void lambda$onBindViewHolder$0(ActionBtnCell actionBtnCell, Void r2) {
        actionBtnCell.updateLoading(false);
        afterCodeApplied();
        dismiss();
    }

    public /* synthetic */ void lambda$onBindViewHolder$1(ActionBtnCell actionBtnCell, TLRPC.TL_error tL_error) {
        actionBtnCell.updateLoading(false);
        BoostDialogs.processApplyGiftCodeError(tL_error, this.container, this.resourcesProvider, new GiftInfoAdapter$$ExternalSyntheticLambda2(this));
    }

    public /* synthetic */ void lambda$onBindViewHolder$2(final ActionBtnCell actionBtnCell, View view) {
        if (!this.isUnused) {
            dismiss();
        } else if (actionBtnCell.isLoading()) {
        } else {
            actionBtnCell.updateLoading(true);
            BoostRepository.applyGiftCode(this.slug, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    GiftInfoAdapter.this.lambda$onBindViewHolder$0(actionBtnCell, (Void) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    GiftInfoAdapter.this.lambda$onBindViewHolder$1(actionBtnCell, (TLRPC.TL_error) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onBindViewHolder$3(View view) {
        dismiss();
    }

    public /* synthetic */ boolean lambda$share$4(String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        long j = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            j = ((MessagesStorage.TopicKey) arrayList.get(i2)).dialogId;
            this.baseFragment.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(str, j, null, null, null, true, null, null, null, true, 0, null, false));
        }
        dialogsActivity.finishFragment();
        BoostDialogs.showGiftLinkForwardedBulletin(j);
        return true;
    }

    public void share() {
        final String str = "https://t.me/giftcode/" + this.slug;
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putInt("dialogsType", 3);
        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
                boolean lambda$share$4;
                lambda$share$4 = GiftInfoAdapter.this.lambda$share$4(str, dialogsActivity2, arrayList, charSequence, z, z2, i, topicsFragment);
                return lambda$share$4;
            }
        });
        this.baseFragment.presentFragment(dialogsActivity);
        dismiss();
    }

    protected abstract void afterCodeApplied();

    protected abstract void dismiss();

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 5;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i != 0) {
            int i2 = 1;
            if (i != 1) {
                i2 = 2;
                if (i != 2) {
                    i2 = 3;
                    if (i != 3) {
                        i2 = 4;
                        if (i != 4) {
                            return 5;
                        }
                    }
                }
            }
            return i2;
        }
        return 0;
    }

    public void init(BaseFragment baseFragment, TLRPC.TL_payments_checkedGiftCode tL_payments_checkedGiftCode, String str, FrameLayout frameLayout) {
        this.isUnused = tL_payments_checkedGiftCode.used_date == 0;
        this.baseFragment = baseFragment;
        this.giftCode = tL_payments_checkedGiftCode;
        this.slug = str;
        this.container = frameLayout;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        CharSequence formatString;
        String string;
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
            if (this.isUnused) {
                headerCell.setGiftLinkText();
            } else {
                headerCell.setUsedGiftLinkText();
            }
            TLRPC.TL_payments_checkedGiftCode tL_payments_checkedGiftCode = this.giftCode;
            if (tL_payments_checkedGiftCode.boost != null) {
                headerCell.setGiftLinkToUserText(tL_payments_checkedGiftCode.to_id, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        GiftInfoAdapter.this.onObjectClicked((TLObject) obj);
                    }
                });
            }
            if (this.giftCode.to_id == -1) {
                headerCell.setUnclaimedText();
            }
        } else if (itemViewType == 1) {
            LinkCell linkCell = (LinkCell) viewHolder.itemView;
            linkCell.setSlug(this.slug);
            if (this.giftCode.boost != null && this.slug == null) {
                linkCell.hideSlug(new Runnable() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        GiftInfoAdapter.this.onHiddenLinkClicked();
                    }
                });
            }
            String str = this.slug;
            if ((str == null || str.isEmpty()) && this.giftCode.to_id == -1) {
                linkCell.hideSlug(new Runnable() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        GiftInfoAdapter.this.onHiddenLinkClicked();
                    }
                });
            }
        } else if (itemViewType == 2) {
            ((TableCell) viewHolder.itemView).setData(this.giftCode, new Utilities.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    GiftInfoAdapter.this.onObjectClicked((TLObject) obj);
                }
            });
        } else if (itemViewType != 3) {
            if (itemViewType != 4) {
                return;
            }
            final ActionBtnCell actionBtnCell = (ActionBtnCell) viewHolder.itemView;
            actionBtnCell.setOkStyle(this.isUnused);
            actionBtnCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GiftInfoAdapter.this.lambda$onBindViewHolder$2(actionBtnCell, view);
                }
            });
            TLRPC.TL_payments_checkedGiftCode tL_payments_checkedGiftCode2 = this.giftCode;
            if (tL_payments_checkedGiftCode2.boost != null || tL_payments_checkedGiftCode2.flags == -1) {
                actionBtnCell.setCloseStyle();
                actionBtnCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.boosts.adapters.GiftInfoAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        GiftInfoAdapter.this.lambda$onBindViewHolder$3(view);
                    }
                });
            }
        } else {
            TextInfoCell textInfoCell = (TextInfoCell) viewHolder.itemView;
            textInfoCell.setTextGravity(17);
            textInfoCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            textInfoCell.setTopPadding(14);
            textInfoCell.setBottomPadding(15);
            TLRPC.TL_payments_checkedGiftCode tL_payments_checkedGiftCode3 = this.giftCode;
            if (tL_payments_checkedGiftCode3.boost == null) {
                if (this.isUnused) {
                    formatString = AndroidUtilities.replaceSingleTag(LocaleController.getString(tL_payments_checkedGiftCode3.to_id == -1 ? R.string.BoostingSendLinkToAnyone : R.string.BoostingSendLinkToFriends), Theme.key_chat_messageLinkIn, 0, new GiftInfoAdapter$$ExternalSyntheticLambda2(this), this.resourcesProvider);
                } else {
                    Date date = new Date(this.giftCode.used_date * 1000);
                    formatString = LocaleController.formatString("BoostingUsedLinkDate", R.string.BoostingUsedLinkDate, LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(date), LocaleController.getInstance().getFormatterDay().format(date)));
                }
                textInfoCell.setText(formatString);
                return;
            }
            String str2 = this.slug;
            if (str2 == null || str2.isEmpty()) {
                string = LocaleController.getString(R.string.BoostingLinkNotActivated);
            } else {
                textInfoCell.setFixedSize(14);
                string = null;
            }
            textInfoCell.setText(string);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View linkCell;
        Context context = viewGroup.getContext();
        if (i == 1) {
            linkCell = new LinkCell(context, this.baseFragment, this.resourcesProvider);
        } else if (i == 2) {
            linkCell = new TableCell(context, this.resourcesProvider);
        } else if (i == 3) {
            linkCell = new TextInfoCell(context, this.resourcesProvider);
        } else if (i != 4) {
            linkCell = i != 5 ? new HeaderCell(context, this.resourcesProvider) : new View(context);
        } else {
            linkCell = new ActionBtnCell(context, this.resourcesProvider);
            linkCell.setPadding(0, 0, 0, AndroidUtilities.dp(14.0f));
        }
        linkCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        return new RecyclerListView.Holder(linkCell);
    }

    public abstract void onHiddenLinkClicked();

    public abstract void onObjectClicked(TLObject tLObject);
}
