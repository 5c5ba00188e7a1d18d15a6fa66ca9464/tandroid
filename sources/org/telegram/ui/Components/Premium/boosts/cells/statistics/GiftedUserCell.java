package org.telegram.ui.Components.Premium.boosts.cells.statistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Date;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.LayoutHelper;

/* loaded from: classes3.dex */
public class GiftedUserCell extends UserCell {
    private FrameLayout badgeLayout;
    private TextView badgeTextView;
    private TL_stories.Boost boost;
    private CounterDrawable counterDrawable;
    private Drawable giftDrawable;
    private Drawable giveawayDrawable;

    public GiftedUserCell(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
        init();
    }

    private void init() {
        this.counterDrawable = new CounterDrawable(getContext());
        this.badgeLayout = new FrameLayout(getContext());
        TextView textView = new TextView(getContext());
        this.badgeTextView = textView;
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        this.badgeTextView.setTypeface(AndroidUtilities.bold());
        this.badgeTextView.setTextSize(12.0f);
        this.badgeTextView.setGravity(17);
        this.badgeLayout.addView(this.badgeTextView, LayoutHelper.createFrame(-2, 22.0f));
        this.badgeLayout.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        FrameLayout frameLayout = this.badgeLayout;
        boolean z = LocaleController.isRTL;
        addView(frameLayout, LayoutHelper.createFrame(-2, -2.0f, (z ? 3 : 5) | 48, z ? 9 : 0, 9.0f, z ? 0 : 9, 0.0f));
    }

    private void setAvatarColorByMonths(int i) {
        AvatarDrawable avatarDrawable;
        int i2;
        int i3;
        if (i == 12) {
            avatarDrawable = this.avatarDrawable;
            i2 = -31392;
            i3 = -2796986;
        } else if (i == 6) {
            avatarDrawable = this.avatarDrawable;
            i2 = -10703110;
            i3 = -12481584;
        } else {
            avatarDrawable = this.avatarDrawable;
            i2 = -6631068;
            i3 = -11945404;
        }
        avatarDrawable.setColor(i2, i3);
    }

    public TL_stories.Boost getBoost() {
        return this.boost;
    }

    @Override // org.telegram.ui.Cells.UserCell, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(70.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(70.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setStatus(TL_stories.Boost boost) {
        AvatarDrawable avatarDrawable;
        int i;
        this.boost = boost;
        if (boost.gift || boost.giveaway) {
            this.badgeLayout.setVisibility(0);
            int i2 = ((boost.expires - boost.date) / 30) / 86400;
            long j = boost.stars;
            if (j > 0) {
                this.nameTextView.setText(LocaleController.formatPluralString("BoostingBoostStars", (int) j, new Object[0]));
                this.avatarDrawable.setAvatarType(26);
            } else {
                if (boost.unclaimed) {
                    this.nameTextView.setText(LocaleController.getString(R.string.BoostingUnclaimed));
                    avatarDrawable = this.avatarDrawable;
                    i = 18;
                } else {
                    if (boost.user_id == -1) {
                        this.nameTextView.setText(LocaleController.getString(R.string.BoostingToBeDistributed));
                        avatarDrawable = this.avatarDrawable;
                        i = 19;
                    }
                    String format = LocaleController.getInstance().getFormatterBoostExpired().format(new Date(boost.expires * 1000));
                    this.statusTextView.setText(boost.stars <= 0 ? LocaleController.formatString(R.string.BoostingStarsExpires, format) : LocaleController.formatString(R.string.BoostingExpires, format));
                    if (boost.gift) {
                        if (this.giftDrawable == null) {
                            Drawable drawable = getResources().getDrawable(R.drawable.mini_gift);
                            this.giftDrawable = drawable;
                            drawable.setColorFilter(new PorterDuffColorFilter(-3240417, PorterDuff.Mode.MULTIPLY));
                        }
                        this.badgeTextView.setTextColor(-3240417);
                        this.badgeTextView.setCompoundDrawablesWithIntrinsicBounds(this.giftDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
                        this.badgeTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                        this.badgeTextView.setText(LocaleController.getString(R.string.BoostingGift));
                        this.badgeLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), Theme.multAlpha(-3240417, 0.2f)));
                    }
                    if (boost.giveaway) {
                        if (this.giveawayDrawable == null) {
                            Drawable drawable2 = getResources().getDrawable(R.drawable.mini_giveaway);
                            this.giveawayDrawable = drawable2;
                            drawable2.setColorFilter(new PorterDuffColorFilter(-13397548, PorterDuff.Mode.MULTIPLY));
                        }
                        this.badgeTextView.setTextColor(-13397548);
                        this.badgeTextView.setCompoundDrawablesWithIntrinsicBounds(this.giveawayDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
                        this.badgeTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                        this.badgeTextView.setText(LocaleController.getString(R.string.BoostingGiveaway));
                        this.badgeLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), Theme.multAlpha(-13397548, 0.2f)));
                    }
                }
                avatarDrawable.setAvatarType(i);
                setAvatarColorByMonths(i2);
            }
            this.avatarImageView.setForUserOrChat(null, this.avatarDrawable);
            this.nameTextView.setRightDrawable((Drawable) null);
            String format2 = LocaleController.getInstance().getFormatterBoostExpired().format(new Date(boost.expires * 1000));
            this.statusTextView.setText(boost.stars <= 0 ? LocaleController.formatString(R.string.BoostingStarsExpires, format2) : LocaleController.formatString(R.string.BoostingExpires, format2));
            if (boost.gift) {
            }
            if (boost.giveaway) {
            }
        } else {
            this.badgeLayout.setVisibility(8);
        }
        int i3 = boost.multiplier;
        if (i3 > 0) {
            this.counterDrawable.setText(String.valueOf(i3));
            this.nameTextView.setRightDrawable(this.counterDrawable);
        } else {
            this.nameTextView.setRightDrawable((Drawable) null);
        }
        if (this.badgeLayout.getVisibility() != 0) {
            SimpleTextView simpleTextView = this.nameTextView;
            simpleTextView.setPadding(0, simpleTextView.getPaddingTop(), 0, this.nameTextView.getPaddingBottom());
        } else {
            int measureText = ((int) this.badgeTextView.getPaint().measureText(this.badgeTextView.getText().toString())) + AndroidUtilities.dp(22.0f);
            SimpleTextView simpleTextView2 = this.nameTextView;
            simpleTextView2.setPadding(LocaleController.isRTL ? measureText : 0, simpleTextView2.getPaddingTop(), LocaleController.isRTL ? 0 : measureText, this.nameTextView.getPaddingBottom());
        }
    }
}
