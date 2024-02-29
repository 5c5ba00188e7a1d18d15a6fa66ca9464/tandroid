package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes4.dex */
public class ChatAttachRestrictedLayout extends ChatAttachAlert.AttachAlertLayout {
    private final RecyclerView.Adapter adapter;
    private int gridExtraSpace;
    public final int id;
    private final RecyclerListView listView;
    private final EmptyTextProgressView progressView;

    public ChatAttachRestrictedLayout(int i, ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.id = i;
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context, null, resourcesProvider);
        this.progressView = emptyTextProgressView;
        emptyTextProgressView.setText(LocaleController.getString("NoPhotos", R.string.NoPhotos));
        emptyTextProgressView.setOnTouchListener(null);
        emptyTextProgressView.setTextSize(16);
        addView(emptyTextProgressView, LayoutHelper.createFrame(-1, -2.0f));
        emptyTextProgressView.setLottie(R.raw.media_forbidden, ImageReceiver.DEFAULT_CROSSFADE_DURATION, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
        TLRPC$Chat chat = this.parentAlert.getChat();
        if (i == 1) {
            emptyTextProgressView.setText(ChatObject.getRestrictedErrorText(chat, 7));
        } else if (i == 3) {
            emptyTextProgressView.setText(ChatObject.getRestrictedErrorText(chat, 18));
        } else if (i == 4) {
            emptyTextProgressView.setText(ChatObject.getRestrictedErrorText(chat, 19));
        } else {
            emptyTextProgressView.setText(ChatObject.getRestrictedErrorText(chat, 22));
        }
        emptyTextProgressView.showTextView();
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider);
        this.listView = recyclerListView;
        recyclerListView.setSectionsType(2);
        recyclerListView.setVerticalScrollBarEnabled(false);
        recyclerListView.setLayoutManager(new LinearLayoutManager(context));
        recyclerListView.setClipToPadding(false);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() { // from class: org.telegram.ui.Components.ChatAttachRestrictedLayout.1
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return 1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
                return new RecyclerListView.Holder(new View(ChatAttachRestrictedLayout.this.getContext()) { // from class: org.telegram.ui.Components.ChatAttachRestrictedLayout.1.1
                    @Override // android.view.View
                    protected void onMeasure(int i3, int i4) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3), 1073741824), View.MeasureSpec.makeMeasureSpec(ChatAttachRestrictedLayout.this.gridExtraSpace, 1073741824));
                    }
                });
            }
        };
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        recyclerListView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        recyclerListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ChatAttachRestrictedLayout.2
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                ChatAttachRestrictedLayout chatAttachRestrictedLayout = ChatAttachRestrictedLayout.this;
                chatAttachRestrictedLayout.parentAlert.updateLayout(chatAttachRestrictedLayout, true, i3);
            }
        });
        addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        int i = 0;
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(8.0f);
        if (top > 0 && holder != null && holder.getAdapterPosition() == 0) {
            i = top;
        }
        if (top < 0 || holder == null || holder.getAdapterPosition() != 0) {
            top = i;
        }
        this.progressView.setTranslationY(((((getMeasuredHeight() - top) - AndroidUtilities.dp(50.0f)) - this.progressView.getMeasuredHeight()) / 2) + top);
        return top + AndroidUtilities.dp(12.0f);
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(4.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPreMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onPreMeasure(i, i2);
        int max = Math.max(0, i2 - ActionBar.getCurrentActionBarHeight());
        if (this.gridExtraSpace != max) {
            this.gridExtraSpace = max;
            this.adapter.notifyDataSetChanged();
        }
        if (!AndroidUtilities.isTablet()) {
            android.graphics.Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                i3 = (int) (i2 / 3.5f);
                int dp = i3 - AndroidUtilities.dp(52.0f);
                i4 = dp >= 0 ? dp : 0;
                if (this.listView.getPaddingTop() == i4) {
                    this.listView.setPadding(AndroidUtilities.dp(6.0f), i4, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(48.0f));
                    return;
                }
                return;
            }
        }
        i3 = (i2 / 5) * 2;
        int dp2 = i3 - AndroidUtilities.dp(52.0f);
        if (dp2 >= 0) {
        }
        if (this.listView.getPaddingTop() == i4) {
        }
    }
}
