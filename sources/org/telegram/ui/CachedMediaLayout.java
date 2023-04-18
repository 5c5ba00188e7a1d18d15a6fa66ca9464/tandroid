package org.telegram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.CachedMediaLayout;
import org.telegram.ui.Cells.SharedAudioCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Cells.SharedPhotoVideoCell2;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.NestedSizeNotifierLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Storage.CacheModel;
/* loaded from: classes3.dex */
public class CachedMediaLayout extends FrameLayout implements NestedSizeNotifierLayout.ChildLayout {
    private final LinearLayout actionModeLayout;
    private final ArrayList<View> actionModeViews;
    Page[] allPages;
    private final BackDrawable backDrawable;
    private int bottomPadding;
    CacheModel cacheModel;
    private final ActionBarMenuItem clearItem;
    private final ImageView closeButton;
    Delegate delegate;
    private final View divider;
    ArrayList<Page> pages;
    BaseFragment parentFragment;
    BasePlaceProvider placeProvider;
    public final AnimatedTextView selectedMessagesCountTextView;
    private final ViewPagerFixed.TabsView tabs;
    ViewPagerFixed viewPagerFixed;

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static void $default$dismiss(Delegate delegate) {
            }
        }

        void clear();

        void clearSelection();

        void dismiss();

        void onItemSelected(CacheControlActivity.DialogFileEntities dialogFileEntities, CacheModel.FileInfo fileInfo, boolean z);
    }

    @Override // org.telegram.ui.Components.NestedSizeNotifierLayout.ChildLayout
    public boolean isAttached() {
        return true;
    }

    public void showActionMode(boolean z) {
    }

    public CachedMediaLayout(Context context, BaseFragment baseFragment) {
        super(context);
        this.actionModeViews = new ArrayList<>();
        this.pages = new ArrayList<>();
        Page[] pageArr = new Page[5];
        this.allPages = pageArr;
        this.parentFragment = baseFragment;
        pageArr[0] = new Page(this, LocaleController.getString("FilterChats", R.string.FilterChats), 0, new DialogsAdapter(this, null), null);
        this.allPages[1] = new Page(this, LocaleController.getString("MediaTab", R.string.MediaTab), 1, new MediaAdapter(this, null), null);
        this.allPages[2] = new Page(this, LocaleController.getString("SharedFilesTab2", R.string.SharedFilesTab2), 2, new DocumentsAdapter(this, null), null);
        this.allPages[3] = new Page(this, LocaleController.getString("Music", R.string.Music), 3, new MusicAdapter(this, null), null);
        int i = 0;
        while (true) {
            Page[] pageArr2 = this.allPages;
            if (i < pageArr2.length) {
                if (pageArr2[i] != null) {
                    this.pages.add(i, pageArr2[i]);
                }
                i++;
            } else {
                ViewPagerFixed viewPagerFixed = new ViewPagerFixed(getContext());
                this.viewPagerFixed = viewPagerFixed;
                viewPagerFixed.setAllowDisallowInterceptTouch(false);
                addView(this.viewPagerFixed, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 48.0f, 0.0f, 0.0f));
                ViewPagerFixed.TabsView createTabsView = this.viewPagerFixed.createTabsView(true, 3);
                this.tabs = createTabsView;
                addView(createTabsView, LayoutHelper.createFrame(-1, 48.0f));
                View view = new View(getContext());
                this.divider = view;
                view.setBackgroundColor(Theme.getColor("divider"));
                addView(view, LayoutHelper.createFrame(-1, 1.0f, 0, 0.0f, 48.0f, 0.0f, 0.0f));
                view.getLayoutParams().height = 1;
                this.viewPagerFixed.setAdapter(new 1(context, baseFragment));
                LinearLayout linearLayout = new LinearLayout(context);
                this.actionModeLayout = linearLayout;
                linearLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                linearLayout.setAlpha(0.0f);
                linearLayout.setClickable(true);
                addView(linearLayout, LayoutHelper.createFrame(-1, 48.0f));
                AndroidUtilities.updateViewVisibilityAnimated(linearLayout, false, 1.0f, false);
                ImageView imageView = new ImageView(context);
                this.closeButton = imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                BackDrawable backDrawable = new BackDrawable(true);
                this.backDrawable = backDrawable;
                imageView.setImageDrawable(backDrawable);
                backDrawable.setColor(Theme.getColor("actionBarActionModeDefaultIcon"));
                imageView.setBackground(Theme.createSelectorDrawable(Theme.getColor("actionBarActionModeDefaultSelector"), 1));
                imageView.setContentDescription(LocaleController.getString("Close", R.string.Close));
                linearLayout.addView(imageView, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
                this.actionModeViews.add(imageView);
                imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        CachedMediaLayout.this.lambda$new$0(view2);
                    }
                });
                AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, true);
                this.selectedMessagesCountTextView = animatedTextView;
                animatedTextView.setTextSize(AndroidUtilities.dp(18.0f));
                animatedTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                animatedTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
                linearLayout.addView(animatedTextView, LayoutHelper.createLinear(0, -1, 1.0f, 18, 0, 0, 0));
                this.actionModeViews.add(animatedTextView);
                ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, Theme.getColor("actionBarActionModeDefaultSelector"), Theme.getColor("actionBarActionModeDefaultIcon"), false);
                this.clearItem = actionBarMenuItem;
                actionBarMenuItem.setIcon(R.drawable.msg_clear);
                actionBarMenuItem.setContentDescription(LocaleController.getString("Delete", R.string.Delete));
                actionBarMenuItem.setDuplicateParentStateEnabled(false);
                linearLayout.addView(actionBarMenuItem, new LinearLayout.LayoutParams(AndroidUtilities.dp(54.0f), -1));
                this.actionModeViews.add(actionBarMenuItem);
                actionBarMenuItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        CachedMediaLayout.this.lambda$new$1(view2);
                    }
                });
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class 1 extends ViewPagerFixed.Adapter {
        private ActionBarPopupWindow popupWindow;
        final /* synthetic */ Context val$context;
        final /* synthetic */ BaseFragment val$parentFragment;

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public boolean hasStableId() {
            return true;
        }

        1(Context context, BaseFragment baseFragment) {
            CachedMediaLayout.this = r1;
            this.val$context = context;
            this.val$parentFragment = baseFragment;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public String getItemTitle(int i) {
            return CachedMediaLayout.this.pages.get(i).title;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public int getItemCount() {
            return CachedMediaLayout.this.pages.size();
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public int getItemId(int i) {
            return CachedMediaLayout.this.pages.get(i).type;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public View createView(int i) {
            final RecyclerListView recyclerListView = new RecyclerListView(this.val$context);
            DefaultItemAnimator defaultItemAnimator = (DefaultItemAnimator) recyclerListView.getItemAnimator();
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setSupportsChangeAnimations(false);
            recyclerListView.setClipToPadding(false);
            recyclerListView.setPadding(0, 0, 0, CachedMediaLayout.this.bottomPadding);
            recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.CachedMediaLayout.1.1
                {
                    1.this = this;
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public void onItemClick(View view, int i2) {
                    BaseAdapter baseAdapter = (BaseAdapter) recyclerListView.getAdapter();
                    ItemInner itemInner = baseAdapter.itemInners.get(i2);
                    if (view instanceof SharedPhotoVideoCell2) {
                        CachedMediaLayout.this.openPhoto(itemInner, (MediaAdapter) baseAdapter, recyclerListView, (SharedPhotoVideoCell2) view);
                        return;
                    }
                    Delegate delegate = CachedMediaLayout.this.delegate;
                    if (delegate != null) {
                        delegate.onItemSelected(itemInner.entities, itemInner.file, false);
                    }
                }
            });
            final BaseFragment baseFragment = this.val$parentFragment;
            recyclerListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda5
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public final boolean onItemClick(View view, int i2, float f, float f2) {
                    boolean lambda$createView$5;
                    lambda$createView$5 = CachedMediaLayout.1.this.lambda$createView$5(recyclerListView, baseFragment, view, i2, f, f2);
                    return lambda$createView$5;
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public /* synthetic */ void onLongClickRelease() {
                    RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onLongClickRelease(this);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public /* synthetic */ void onMove(float f, float f2) {
                    RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onMove(this, f, f2);
                }
            });
            return recyclerListView;
        }

        public /* synthetic */ boolean lambda$createView$5(final RecyclerListView recyclerListView, final BaseFragment baseFragment, final View view, int i, float f, float f2) {
            int i2;
            String str;
            final BaseAdapter baseAdapter = (BaseAdapter) recyclerListView.getAdapter();
            final ItemInner itemInner = baseAdapter.itemInners.get(i);
            if ((view instanceof CacheCell) || (view instanceof SharedPhotoVideoCell2)) {
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(CachedMediaLayout.this.getContext());
                if (view instanceof SharedPhotoVideoCell2) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_view_file, LocaleController.getString("CacheOpenFile", R.string.CacheOpenFile), false, null).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            CachedMediaLayout.1.this.lambda$createView$0(itemInner, baseAdapter, recyclerListView, view, view2);
                        }
                    });
                } else if (((CacheCell) view).container.getChildAt(0) instanceof SharedAudioCell) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_played, LocaleController.getString("PlayFile", R.string.PlayFile), false, null).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            CachedMediaLayout.1.this.lambda$createView$1(itemInner, view, view2);
                        }
                    });
                } else {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_view_file, LocaleController.getString("CacheOpenFile", R.string.CacheOpenFile), false, null).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            CachedMediaLayout.1.this.lambda$createView$2(itemInner, view, view2);
                        }
                    });
                }
                CacheModel.FileInfo fileInfo = itemInner.file;
                if (fileInfo.dialogId != 0 && fileInfo.messageId != 0) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_viewintopic, LocaleController.getString("ViewInChat", R.string.ViewInChat), false, null).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            CachedMediaLayout.1.this.lambda$createView$3(itemInner, baseFragment, view2);
                        }
                    });
                }
                int i3 = R.drawable.msg_select;
                if (CachedMediaLayout.this.cacheModel.selectedFiles.contains(itemInner.file)) {
                    i2 = R.string.Deselect;
                    str = "Deselect";
                } else {
                    i2 = R.string.Select;
                    str = "Select";
                }
                ActionBarMenuItem.addItem(actionBarPopupWindowLayout, i3, LocaleController.getString(str, i2), false, null).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$1$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        CachedMediaLayout.1.this.lambda$createView$4(itemInner, view2);
                    }
                });
                this.popupWindow = AlertsCreator.createSimplePopup(baseFragment, actionBarPopupWindowLayout, view, (int) f, (int) f2);
                CachedMediaLayout.this.getRootView().dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
                return true;
            }
            Delegate delegate = CachedMediaLayout.this.delegate;
            if (delegate != null) {
                delegate.onItemSelected(itemInner.entities, itemInner.file, true);
            }
            return true;
        }

        public /* synthetic */ void lambda$createView$0(ItemInner itemInner, BaseAdapter baseAdapter, RecyclerListView recyclerListView, View view, View view2) {
            CachedMediaLayout.this.openPhoto(itemInner, (MediaAdapter) baseAdapter, recyclerListView, (SharedPhotoVideoCell2) view);
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        public /* synthetic */ void lambda$createView$1(ItemInner itemInner, View view, View view2) {
            CachedMediaLayout.this.openItem(itemInner.file, (CacheCell) view);
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        public /* synthetic */ void lambda$createView$2(ItemInner itemInner, View view, View view2) {
            CachedMediaLayout.this.openItem(itemInner.file, (CacheCell) view);
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        public /* synthetic */ void lambda$createView$3(ItemInner itemInner, BaseFragment baseFragment, View view) {
            Bundle bundle = new Bundle();
            long j = itemInner.file.dialogId;
            if (j > 0) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            bundle.putInt("message_id", itemInner.file.messageId);
            baseFragment.presentFragment(new ChatActivity(bundle));
            CachedMediaLayout.this.delegate.dismiss();
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        public /* synthetic */ void lambda$createView$4(ItemInner itemInner, View view) {
            Delegate delegate = CachedMediaLayout.this.delegate;
            if (delegate != null) {
                delegate.onItemSelected(itemInner.entities, itemInner.file, true);
            }
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
            }
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public void bindView(View view, int i, int i2) {
            RecyclerListView recyclerListView = (RecyclerListView) view;
            recyclerListView.setAdapter(CachedMediaLayout.this.pages.get(i).adapter);
            if (CachedMediaLayout.this.pages.get(i).type == 1) {
                recyclerListView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
            } else {
                recyclerListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            }
            recyclerListView.setTag(Integer.valueOf(CachedMediaLayout.this.pages.get(i).type));
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        this.delegate.clearSelection();
    }

    public /* synthetic */ void lambda$new$1(View view) {
        this.delegate.clear();
    }

    public void openPhoto(ItemInner itemInner, MediaAdapter mediaAdapter, RecyclerListView recyclerListView, SharedPhotoVideoCell2 sharedPhotoVideoCell2) {
        PhotoViewer.getInstance().setParentActivity(this.parentFragment);
        if (this.placeProvider == null) {
            this.placeProvider = new BasePlaceProvider(this, null);
        }
        this.placeProvider.setRecyclerListView(recyclerListView);
        if (mediaAdapter.itemInners.indexOf(itemInner) >= 0) {
            PhotoViewer.getInstance().openPhotoForSelect(mediaAdapter.getPhotos(), mediaAdapter.itemInners.indexOf(itemInner), -1, false, this.placeProvider, null);
        }
    }

    public void openItem(CacheModel.FileInfo fileInfo, CacheCell cacheCell) {
        RecyclerListView recyclerListView = (RecyclerListView) this.viewPagerFixed.getCurrentView();
        if (cacheCell.type == 2) {
            if (!(recyclerListView.getAdapter() instanceof DocumentsAdapter)) {
                return;
            }
            DocumentsAdapter documentsAdapter = (DocumentsAdapter) recyclerListView.getAdapter();
            PhotoViewer.getInstance().setParentActivity(this.parentFragment);
            if (this.placeProvider == null) {
                this.placeProvider = new BasePlaceProvider(this, null);
            }
            this.placeProvider.setRecyclerListView(recyclerListView);
            if (fileIsMedia(fileInfo.file)) {
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.add(new MediaController.PhotoEntry(0, 0, 0L, fileInfo.file.getPath(), 0, fileInfo.type == 1, 0, 0, 0L));
                PhotoViewer.getInstance().openPhotoForSelect(arrayList, 0, -1, false, this.placeProvider, null);
            } else {
                File file = fileInfo.file;
                AndroidUtilities.openForView(file, file.getName(), null, this.parentFragment.getParentActivity(), null);
            }
        }
        if (cacheCell.type == 3) {
            if (MediaController.getInstance().isPlayingMessage(fileInfo.messageObject)) {
                if (!MediaController.getInstance().isMessagePaused()) {
                    MediaController.getInstance().lambda$startAudioAgain$7(fileInfo.messageObject);
                    return;
                } else {
                    MediaController.getInstance().playMessage(fileInfo.messageObject);
                    return;
                }
            }
            MediaController.getInstance().playMessage(fileInfo.messageObject);
        }
    }

    public SharedPhotoVideoCell2 getCellForIndex(int i) {
        RecyclerListView listView = getListView();
        for (int i2 = 0; i2 < listView.getChildCount(); i2++) {
            View childAt = listView.getChildAt(i2);
            if (listView.getChildAdapterPosition(childAt) == i && (childAt instanceof SharedPhotoVideoCell2)) {
                return (SharedPhotoVideoCell2) childAt;
            }
        }
        return null;
    }

    public void setCacheModel(CacheModel cacheModel) {
        this.cacheModel = cacheModel;
        update();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:129:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0118  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.pages);
        this.pages.clear();
        if (this.cacheModel != null) {
            int i = 0;
            while (true) {
                Page[] pageArr = this.allPages;
                if (i >= pageArr.length) {
                    break;
                }
                if (pageArr[i] != null) {
                    if (pageArr[i].type == 0 && !this.cacheModel.entities.isEmpty()) {
                        this.pages.add(this.allPages[i]);
                    } else if (this.allPages[i].type == 1 && !this.cacheModel.media.isEmpty()) {
                        this.pages.add(this.allPages[i]);
                    } else if (this.allPages[i].type == 2 && !this.cacheModel.documents.isEmpty()) {
                        this.pages.add(this.allPages[i]);
                    } else if (this.allPages[i].type == 3 && !this.cacheModel.music.isEmpty()) {
                        this.pages.add(this.allPages[i]);
                    } else if (this.allPages[i].type == 4 && !this.cacheModel.voice.isEmpty()) {
                        this.pages.add(this.allPages[i]);
                    }
                }
                i++;
            }
        }
        if (this.pages.size() == 1 && this.cacheModel.isDialog) {
            this.tabs.setVisibility(8);
            ((ViewGroup.MarginLayoutParams) this.viewPagerFixed.getLayoutParams()).topMargin = 0;
            ((ViewGroup.MarginLayoutParams) this.divider.getLayoutParams()).topMargin = 0;
        }
        if (arrayList.size() == this.pages.size()) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (((Page) arrayList.get(i2)).type == this.pages.get(i2).type) {
                }
            }
            z = false;
            if (z) {
                this.viewPagerFixed.rebuild(true);
            }
            for (int i3 = 0; i3 < this.pages.size(); i3++) {
                if (this.pages.get(i3).adapter != null) {
                    this.pages.get(i3).adapter.update();
                }
            }
        }
        z = true;
        if (z) {
        }
        while (i3 < this.pages.size()) {
        }
    }

    @Override // org.telegram.ui.Components.NestedSizeNotifierLayout.ChildLayout
    public RecyclerListView getListView() {
        if (this.viewPagerFixed.getCurrentView() == null) {
            return null;
        }
        return (RecyclerListView) this.viewPagerFixed.getCurrentView();
    }

    public void updateVisibleRows() {
        for (int i = 0; i < this.viewPagerFixed.getViewPages().length; i++) {
            AndroidUtilities.updateVisibleRows((RecyclerListView) this.viewPagerFixed.getViewPages()[i]);
        }
    }

    public void setBottomPadding(int i) {
        this.bottomPadding = i;
        for (int i2 = 0; i2 < this.viewPagerFixed.getViewPages().length; i2++) {
            RecyclerListView recyclerListView = (RecyclerListView) this.viewPagerFixed.getViewPages()[i2];
            if (recyclerListView != null) {
                recyclerListView.setPadding(0, 0, 0, i);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class Page {
        public final BaseAdapter adapter;
        public final String title;
        public final int type;

        /* synthetic */ Page(CachedMediaLayout cachedMediaLayout, String str, int i, BaseAdapter baseAdapter, 1 r5) {
            this(cachedMediaLayout, str, i, baseAdapter);
        }

        private Page(CachedMediaLayout cachedMediaLayout, String str, int i, BaseAdapter baseAdapter) {
            this.title = str;
            this.type = i;
            this.adapter = baseAdapter;
        }
    }

    /* loaded from: classes3.dex */
    public abstract class BaseAdapter extends AdapterWithDiffUtils {
        ArrayList<ItemInner> itemInners = new ArrayList<>();
        final int type;

        abstract void update();

        protected BaseAdapter(CachedMediaLayout cachedMediaLayout, int i) {
            this.type = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return this.itemInners.get(i).viewType;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.itemInners.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DialogsAdapter extends BaseAdapter {
        ArrayList<ItemInner> old;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        /* synthetic */ DialogsAdapter(CachedMediaLayout cachedMediaLayout, 1 r2) {
            this();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private DialogsAdapter() {
            super(r2, 0);
            CachedMediaLayout.this = r2;
            this.old = new ArrayList<>();
        }

        @Override // org.telegram.ui.CachedMediaLayout.BaseAdapter
        void update() {
            this.old.clear();
            this.old.addAll(this.itemInners);
            this.itemInners.clear();
            if (CachedMediaLayout.this.cacheModel != null) {
                for (int i = 0; i < CachedMediaLayout.this.cacheModel.entities.size(); i++) {
                    ArrayList<ItemInner> arrayList = this.itemInners;
                    CachedMediaLayout cachedMediaLayout = CachedMediaLayout.this;
                    arrayList.add(new ItemInner(cachedMediaLayout, 1, cachedMediaLayout.cacheModel.entities.get(i)));
                }
            }
            setItems(this.old, this.itemInners);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            CacheControlActivity.UserCell userCell = null;
            if (i == 1) {
                CacheControlActivity.UserCell userCell2 = new CacheControlActivity.UserCell(CachedMediaLayout.this.getContext(), null);
                userCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                userCell = userCell2;
            }
            return new RecyclerListView.Holder(userCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String dialogPhotoTitle;
            if (viewHolder.getItemViewType() != 1) {
                return;
            }
            CacheControlActivity.UserCell userCell = (CacheControlActivity.UserCell) viewHolder.itemView;
            CacheControlActivity.DialogFileEntities dialogFileEntities = this.itemInners.get(i).entities;
            TLObject userOrChat = CachedMediaLayout.this.parentFragment.getMessagesController().getUserOrChat(dialogFileEntities.dialogId);
            CacheControlActivity.DialogFileEntities dialogFileEntities2 = userCell.dialogFileEntities;
            boolean z = dialogFileEntities2 != null && dialogFileEntities2.dialogId == dialogFileEntities.dialogId;
            if (dialogFileEntities.dialogId == Long.MAX_VALUE) {
                dialogPhotoTitle = LocaleController.getString("CacheOtherChats", R.string.CacheOtherChats);
                userCell.getImageView().getAvatarDrawable().setAvatarType(14);
                userCell.getImageView().setForUserOrChat(null, userCell.getImageView().getAvatarDrawable());
            } else {
                dialogPhotoTitle = DialogObject.setDialogPhotoTitle(userCell.getImageView(), userOrChat);
            }
            userCell.dialogFileEntities = dialogFileEntities;
            userCell.getImageView().setRoundRadius(AndroidUtilities.dp(((userOrChat instanceof TLRPC$Chat) && ((TLRPC$Chat) userOrChat).forum) ? 12.0f : 19.0f));
            userCell.setTextAndValue(dialogPhotoTitle, AndroidUtilities.formatFileSize(dialogFileEntities.totalSize), i < getItemCount() - 1);
            userCell.setChecked(CachedMediaLayout.this.cacheModel.isSelected(dialogFileEntities.dialogId), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public abstract class BaseFilesAdapter extends BaseAdapter {
        ArrayList<ItemInner> oldItems;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        protected BaseFilesAdapter(int i) {
            super(r1, i);
            CachedMediaLayout.this = r1;
            this.oldItems = new ArrayList<>();
        }

        @Override // org.telegram.ui.CachedMediaLayout.BaseAdapter
        void update() {
            this.oldItems.clear();
            this.oldItems.addAll(this.itemInners);
            this.itemInners.clear();
            CacheModel cacheModel = CachedMediaLayout.this.cacheModel;
            if (cacheModel != null) {
                ArrayList<CacheModel.FileInfo> arrayList = null;
                int i = this.type;
                if (i == 1) {
                    arrayList = cacheModel.media;
                } else if (i == 2) {
                    arrayList = cacheModel.documents;
                } else if (i == 3) {
                    arrayList = cacheModel.music;
                } else if (i == 4) {
                    arrayList = cacheModel.voice;
                }
                if (arrayList != null) {
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        this.itemInners.add(new ItemInner(CachedMediaLayout.this, 2, arrayList.get(i2)));
                    }
                }
            }
            setItems(this.oldItems, this.itemInners);
        }
    }

    /* loaded from: classes3.dex */
    public class ItemInner extends AdapterWithDiffUtils.Item {
        CacheControlActivity.DialogFileEntities entities;
        CacheModel.FileInfo file;

        public ItemInner(CachedMediaLayout cachedMediaLayout, int i, CacheControlActivity.DialogFileEntities dialogFileEntities) {
            super(i, true);
            this.entities = dialogFileEntities;
        }

        public ItemInner(CachedMediaLayout cachedMediaLayout, int i, CacheModel.FileInfo fileInfo) {
            super(i, true);
            this.file = fileInfo;
        }

        public boolean equals(Object obj) {
            CacheModel.FileInfo fileInfo;
            CacheModel.FileInfo fileInfo2;
            CacheControlActivity.DialogFileEntities dialogFileEntities;
            CacheControlActivity.DialogFileEntities dialogFileEntities2;
            if (this == obj) {
                return true;
            }
            if (obj != null && ItemInner.class == obj.getClass()) {
                ItemInner itemInner = (ItemInner) obj;
                int i = this.viewType;
                if (i == itemInner.viewType) {
                    if (i == 1 && (dialogFileEntities = this.entities) != null && (dialogFileEntities2 = itemInner.entities) != null) {
                        return dialogFileEntities.dialogId == dialogFileEntities2.dialogId;
                    } else if (i == 2 && (fileInfo = this.file) != null && (fileInfo2 = itemInner.file) != null) {
                        return Objects.equals(fileInfo.file, fileInfo2.file);
                    }
                }
            }
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public class MediaAdapter extends BaseFilesAdapter {
        ArrayList<Object> photoEntries;
        private SharedPhotoVideoCell2.SharedResources sharedResources;
        CombinedDrawable thumb;

        @Override // org.telegram.ui.CachedMediaLayout.BaseFilesAdapter, org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        /* synthetic */ MediaAdapter(CachedMediaLayout cachedMediaLayout, 1 r2) {
            this();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private MediaAdapter() {
            super(1);
            CachedMediaLayout.this = r2;
            this.photoEntries = new ArrayList<>();
        }

        @Override // org.telegram.ui.CachedMediaLayout.BaseFilesAdapter, org.telegram.ui.CachedMediaLayout.BaseAdapter
        void update() {
            super.update();
            this.photoEntries.clear();
            for (int i = 0; i < this.itemInners.size(); i++) {
                ArrayList<Object> arrayList = this.photoEntries;
                String path = this.itemInners.get(i).file.file.getPath();
                boolean z = true;
                if (this.itemInners.get(i).file.type != 1) {
                    z = false;
                }
                arrayList.add(new MediaController.PhotoEntry(0, 0, 0L, path, 0, z, 0, 0, 0L));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (this.sharedResources == null) {
                this.sharedResources = new SharedPhotoVideoCell2.SharedResources(viewGroup.getContext(), null);
            }
            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = new SharedPhotoVideoCell2(viewGroup.getContext(), this.sharedResources, CachedMediaLayout.this.parentFragment.getCurrentAccount()) { // from class: org.telegram.ui.CachedMediaLayout.MediaAdapter.1
                {
                    MediaAdapter.this = this;
                }

                @Override // org.telegram.ui.Cells.SharedPhotoVideoCell2
                public void onCheckBoxPressed() {
                    CachedMediaLayout.this.delegate.onItemSelected(null, (CacheModel.FileInfo) getTag(), true);
                }
            };
            sharedPhotoVideoCell2.setStyle(1);
            return new RecyclerListView.Holder(sharedPhotoVideoCell2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (this.thumb == null) {
                CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor("chat_attachPhotoBackground")), Theme.chat_attachEmptyDrawable);
                this.thumb = combinedDrawable;
                combinedDrawable.setFullsize(true);
            }
            SharedPhotoVideoCell2 sharedPhotoVideoCell2 = (SharedPhotoVideoCell2) viewHolder.itemView;
            CacheModel.FileInfo fileInfo = this.itemInners.get(i).file;
            boolean z = fileInfo == sharedPhotoVideoCell2.getTag();
            sharedPhotoVideoCell2.setTag(fileInfo);
            int max = (int) Math.max(100.0f, AndroidUtilities.getRealScreenSize().x / AndroidUtilities.density);
            if (fileInfo.type == 1) {
                ImageReceiver imageReceiver = sharedPhotoVideoCell2.imageReceiver;
                ImageLocation forPath = ImageLocation.getForPath("vthumb://0:" + fileInfo.file.getAbsolutePath());
                imageReceiver.setImage(forPath, max + "_" + max, this.thumb, null, null, 0);
                sharedPhotoVideoCell2.setVideoText(AndroidUtilities.formatFileSize(fileInfo.size), true);
            } else {
                ImageReceiver imageReceiver2 = sharedPhotoVideoCell2.imageReceiver;
                ImageLocation forPath2 = ImageLocation.getForPath("thumb://0:" + fileInfo.file.getAbsolutePath());
                imageReceiver2.setImage(forPath2, max + "_" + max, this.thumb, null, null, 0);
                sharedPhotoVideoCell2.setVideoText(AndroidUtilities.formatFileSize(fileInfo.size), false);
            }
            sharedPhotoVideoCell2.setChecked(CachedMediaLayout.this.cacheModel.isSelected(fileInfo), z);
        }

        public ArrayList<Object> getPhotos() {
            return this.photoEntries;
        }
    }

    /* loaded from: classes3.dex */
    public class DocumentsAdapter extends BaseFilesAdapter {
        ArrayList<Object> photoEntries;

        /* synthetic */ DocumentsAdapter(CachedMediaLayout cachedMediaLayout, 1 r2) {
            this();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private DocumentsAdapter() {
            super(2);
            CachedMediaLayout.this = r2;
            this.photoEntries = new ArrayList<>();
        }

        @Override // org.telegram.ui.CachedMediaLayout.BaseFilesAdapter, org.telegram.ui.CachedMediaLayout.BaseAdapter
        void update() {
            super.update();
            this.photoEntries.clear();
            for (int i = 0; i < this.itemInners.size(); i++) {
                ArrayList<Object> arrayList = this.photoEntries;
                String path = this.itemInners.get(i).file.file.getPath();
                boolean z = true;
                if (this.itemInners.get(i).file.type != 1) {
                    z = false;
                }
                arrayList.add(new MediaController.PhotoEntry(0, 0, 0L, path, 0, z, 0, 0, 0L));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            CacheCell cacheCell = new CacheCell(viewGroup.getContext()) { // from class: org.telegram.ui.CachedMediaLayout.DocumentsAdapter.1
                {
                    DocumentsAdapter.this = this;
                    CachedMediaLayout cachedMediaLayout = CachedMediaLayout.this;
                }

                @Override // org.telegram.ui.CachedMediaLayout.CacheCell
                public void onCheckBoxPressed() {
                    CachedMediaLayout.this.delegate.onItemSelected(null, (CacheModel.FileInfo) getTag(), true);
                }
            };
            cacheCell.type = 2;
            cacheCell.container.addView(new SharedDocumentCell(viewGroup.getContext(), 3, null));
            return new RecyclerListView.Holder(cacheCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            CacheCell cacheCell = (CacheCell) viewHolder.itemView;
            SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) cacheCell.container.getChildAt(0);
            CacheModel.FileInfo fileInfo = this.itemInners.get(i).file;
            boolean z = fileInfo == viewHolder.itemView.getTag();
            boolean z2 = i != this.itemInners.size() - 1;
            viewHolder.itemView.setTag(fileInfo);
            sharedDocumentCell.setTextAndValueAndTypeAndThumb(fileInfo.messageType == 5 ? LocaleController.getString("AttachRound", R.string.AttachRound) : fileInfo.file.getName(), LocaleController.formatDateAudio(fileInfo.file.lastModified() / 1000, true), Utilities.getExtension(fileInfo.file.getName()), null, 0, z2);
            if (!z) {
                sharedDocumentCell.setPhoto(fileInfo.file.getPath());
            }
            sharedDocumentCell.getImageView().setRoundRadius(AndroidUtilities.dp(fileInfo.messageType == 5 ? 20.0f : 4.0f));
            cacheCell.drawDivider = z2;
            cacheCell.sizeTextView.setText(AndroidUtilities.formatFileSize(fileInfo.size));
            cacheCell.checkBox.setChecked(CachedMediaLayout.this.cacheModel.isSelected(fileInfo), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class MusicAdapter extends BaseFilesAdapter {
        /* synthetic */ MusicAdapter(CachedMediaLayout cachedMediaLayout, 1 r2) {
            this();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private MusicAdapter() {
            super(3);
            CachedMediaLayout.this = r2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final CacheCell cacheCell = new CacheCell(viewGroup.getContext()) { // from class: org.telegram.ui.CachedMediaLayout.MusicAdapter.1
                {
                    MusicAdapter.this = this;
                    CachedMediaLayout cachedMediaLayout = CachedMediaLayout.this;
                }

                @Override // org.telegram.ui.CachedMediaLayout.CacheCell
                public void onCheckBoxPressed() {
                    CachedMediaLayout.this.delegate.onItemSelected(null, (CacheModel.FileInfo) getTag(), true);
                }
            };
            cacheCell.type = 3;
            SharedAudioCell sharedAudioCell = new SharedAudioCell(viewGroup.getContext(), 0, null) { // from class: org.telegram.ui.CachedMediaLayout.MusicAdapter.2
                {
                    MusicAdapter.this = this;
                }

                @Override // org.telegram.ui.Cells.SharedAudioCell
                public void didPressedButton() {
                    CachedMediaLayout.this.openItem((CacheModel.FileInfo) cacheCell.getTag(), cacheCell);
                }
            };
            sharedAudioCell.setCheckForButtonPress(true);
            cacheCell.container.addView(sharedAudioCell);
            return new RecyclerListView.Holder(cacheCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            CacheCell cacheCell = (CacheCell) viewHolder.itemView;
            SharedAudioCell sharedAudioCell = (SharedAudioCell) cacheCell.container.getChildAt(0);
            CacheModel.FileInfo fileInfo = this.itemInners.get(i).file;
            boolean z = fileInfo == cacheCell.getTag();
            boolean z2 = i != this.itemInners.size() - 1;
            cacheCell.setTag(fileInfo);
            CachedMediaLayout.this.checkMessageObjectForAudio(fileInfo, i);
            sharedAudioCell.setMessageObject(fileInfo.messageObject, z2);
            sharedAudioCell.showName(!fileInfo.metadata.loading, z);
            cacheCell.drawDivider = z2;
            cacheCell.sizeTextView.setText(AndroidUtilities.formatFileSize(fileInfo.size));
            cacheCell.checkBox.setChecked(CachedMediaLayout.this.cacheModel.isSelected(fileInfo), z);
        }
    }

    public void checkMessageObjectForAudio(final CacheModel.FileInfo fileInfo, int i) {
        if (fileInfo.messageObject == null) {
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.out = true;
            tLRPC$TL_message.id = i;
            tLRPC$TL_message.peer_id = new TLRPC$TL_peerUser();
            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
            TLRPC$Peer tLRPC$Peer = tLRPC$TL_message.peer_id;
            long clientUserId = UserConfig.getInstance(this.parentFragment.getCurrentAccount()).getClientUserId();
            tLRPC$TL_peerUser.user_id = clientUserId;
            tLRPC$Peer.user_id = clientUserId;
            tLRPC$TL_message.date = (int) (System.currentTimeMillis() / 1000);
            tLRPC$TL_message.message = "";
            tLRPC$TL_message.attachPath = fileInfo.file.getPath();
            TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
            tLRPC$TL_message.media = tLRPC$TL_messageMediaDocument;
            tLRPC$TL_messageMediaDocument.flags |= 3;
            tLRPC$TL_messageMediaDocument.document = new TLRPC$TL_document();
            tLRPC$TL_message.flags |= 768;
            String fileExtension = FileLoader.getFileExtension(fileInfo.file);
            TLRPC$Document tLRPC$Document = tLRPC$TL_message.media.document;
            tLRPC$Document.id = 0L;
            tLRPC$Document.access_hash = 0L;
            tLRPC$Document.file_reference = new byte[0];
            tLRPC$Document.date = tLRPC$TL_message.date;
            StringBuilder sb = new StringBuilder();
            sb.append("audio/");
            if (fileExtension.length() <= 0) {
                fileExtension = "mp3";
            }
            sb.append(fileExtension);
            tLRPC$Document.mime_type = sb.toString();
            TLRPC$Document tLRPC$Document2 = tLRPC$TL_message.media.document;
            tLRPC$Document2.size = fileInfo.size;
            tLRPC$Document2.dc_id = 0;
            final TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
            if (fileInfo.metadata == null) {
                CacheModel.FileInfo.FileMetadata fileMetadata = new CacheModel.FileInfo.FileMetadata();
                fileInfo.metadata = fileMetadata;
                fileMetadata.loading = true;
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        CachedMediaLayout.this.lambda$checkMessageObjectForAudio$3(fileInfo, tLRPC$TL_documentAttributeAudio);
                    }
                });
            }
            tLRPC$TL_documentAttributeAudio.flags |= 3;
            tLRPC$TL_message.media.document.attributes.add(tLRPC$TL_documentAttributeAudio);
            TLRPC$TL_documentAttributeFilename tLRPC$TL_documentAttributeFilename = new TLRPC$TL_documentAttributeFilename();
            tLRPC$TL_documentAttributeFilename.file_name = fileInfo.file.getName();
            tLRPC$TL_message.media.document.attributes.add(tLRPC$TL_documentAttributeFilename);
            MessageObject messageObject = new MessageObject(this.parentFragment.getCurrentAccount(), tLRPC$TL_message, false, false);
            fileInfo.messageObject = messageObject;
            messageObject.mediaExists = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x0039 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$checkMessageObjectForAudio$3(final CacheModel.FileInfo fileInfo, final TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio) {
        String str;
        MediaMetadataRetriever mediaMetadataRetriever;
        final String str2;
        final String str3;
        MediaMetadataRetriever mediaMetadataRetriever2;
        String extractMetadata;
        MediaMetadataRetriever mediaMetadataRetriever3 = null;
        try {
            try {
                mediaMetadataRetriever2 = new MediaMetadataRetriever();
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    mediaMetadataRetriever2.setDataSource(getContext(), Uri.fromFile(fileInfo.file));
                    extractMetadata = mediaMetadataRetriever2.extractMetadata(7);
                } catch (Exception e) {
                    e = e;
                    mediaMetadataRetriever3 = mediaMetadataRetriever2;
                    str = "";
                    mediaMetadataRetriever = mediaMetadataRetriever3;
                    FileLog.e(e);
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Throwable unused) {
                        }
                    }
                    str2 = "";
                    str3 = str;
                    mediaMetadataRetriever3 = mediaMetadataRetriever;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            CachedMediaLayout.this.lambda$checkMessageObjectForAudio$2(fileInfo, tLRPC$TL_documentAttributeAudio, str3, str2);
                        }
                    });
                }
                try {
                    String extractMetadata2 = mediaMetadataRetriever2.extractMetadata(2);
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (Throwable unused2) {
                    }
                    str2 = extractMetadata2;
                    str3 = extractMetadata;
                    mediaMetadataRetriever3 = extractMetadata;
                } catch (Exception e2) {
                    e = e2;
                    str = extractMetadata;
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                    FileLog.e(e);
                    if (mediaMetadataRetriever != null) {
                    }
                    str2 = "";
                    str3 = str;
                    mediaMetadataRetriever3 = mediaMetadataRetriever;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            CachedMediaLayout.this.lambda$checkMessageObjectForAudio$2(fileInfo, tLRPC$TL_documentAttributeAudio, str3, str2);
                        }
                    });
                }
            } catch (Throwable th2) {
                th = th2;
                mediaMetadataRetriever3 = mediaMetadataRetriever2;
                if (mediaMetadataRetriever3 != null) {
                    try {
                        mediaMetadataRetriever3.release();
                    } catch (Throwable unused3) {
                    }
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CachedMediaLayout$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CachedMediaLayout.this.lambda$checkMessageObjectForAudio$2(fileInfo, tLRPC$TL_documentAttributeAudio, str3, str2);
            }
        });
    }

    public /* synthetic */ void lambda$checkMessageObjectForAudio$2(CacheModel.FileInfo fileInfo, TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio, String str, String str2) {
        CacheModel.FileInfo.FileMetadata fileMetadata = fileInfo.metadata;
        fileMetadata.loading = false;
        fileMetadata.title = str;
        tLRPC$TL_documentAttributeAudio.title = str;
        fileMetadata.author = str2;
        tLRPC$TL_documentAttributeAudio.performer = str2;
        updateRow(fileInfo, 3);
    }

    private void updateRow(CacheModel.FileInfo fileInfo, int i) {
        for (int i2 = 0; i2 < this.viewPagerFixed.getViewPages().length; i2++) {
            RecyclerListView recyclerListView = (RecyclerListView) this.viewPagerFixed.getViewPages()[i2];
            if (recyclerListView != null && ((BaseAdapter) recyclerListView.getAdapter()).type == i) {
                BaseAdapter baseAdapter = (BaseAdapter) recyclerListView.getAdapter();
                int i3 = 0;
                while (true) {
                    if (i3 >= baseAdapter.itemInners.size()) {
                        break;
                    } else if (baseAdapter.itemInners.get(i3).file == fileInfo) {
                        baseAdapter.notifyItemChanged(i3);
                        break;
                    } else {
                        i3++;
                    }
                }
            }
        }
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    /* loaded from: classes3.dex */
    public class BasePlaceProvider extends PhotoViewer.EmptyPhotoViewerProvider {
        RecyclerListView recyclerListView;

        private BasePlaceProvider() {
            CachedMediaLayout.this = r1;
        }

        /* synthetic */ BasePlaceProvider(CachedMediaLayout cachedMediaLayout, 1 r2) {
            this();
        }

        public void setRecyclerListView(RecyclerListView recyclerListView) {
            this.recyclerListView = recyclerListView;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            SharedPhotoVideoCell2 cellForIndex = CachedMediaLayout.this.getCellForIndex(i);
            if (cellForIndex != null) {
                int[] iArr = new int[2];
                cellForIndex.getLocationInWindow(iArr);
                PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                placeProviderObject.viewX = iArr[0];
                placeProviderObject.viewY = iArr[1];
                placeProviderObject.parentView = this.recyclerListView;
                ImageReceiver imageReceiver = cellForIndex.imageReceiver;
                placeProviderObject.imageReceiver = imageReceiver;
                placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                placeProviderObject.scale = cellForIndex.getScaleX();
                return placeProviderObject;
            }
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public class CacheCell extends FrameLayout {
        CheckBox2 checkBox;
        FrameLayout container;
        boolean drawDivider;
        TextView sizeTextView;
        int type;

        public void onCheckBoxPressed() {
        }

        public CacheCell(CachedMediaLayout cachedMediaLayout, Context context) {
            super(context);
            CheckBox2 checkBox2 = new CheckBox2(context, 21);
            this.checkBox = checkBox2;
            checkBox2.setDrawBackgroundAsArc(14);
            this.checkBox.setColor("checkbox", "radioBackground", "checkboxCheck");
            View view = new View(getContext());
            view.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CachedMediaLayout$CacheCell$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    CachedMediaLayout.CacheCell.this.lambda$new$0(view2);
                }
            });
            this.container = new FrameLayout(context);
            TextView textView = new TextView(context);
            this.sizeTextView = textView;
            textView.setTextSize(1, 16.0f);
            this.sizeTextView.setGravity(5);
            this.sizeTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
            if (LocaleController.isRTL) {
                addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 21, 0.0f, 0.0f, 18.0f, 0.0f));
                addView(view, LayoutHelper.createFrame(40, 40.0f, 21, 0.0f, 0.0f, 0.0f, 0.0f));
                addView(this.container, LayoutHelper.createFrame(-1, -2.0f, 0, 90.0f, 0.0f, 40.0f, 0.0f));
                addView(this.sizeTextView, LayoutHelper.createFrame(69, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
                return;
            }
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 19, 18.0f, 0.0f, 0.0f, 0.0f));
            addView(view, LayoutHelper.createFrame(40, 40.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
            addView(this.container, LayoutHelper.createFrame(-1, -2.0f, 0, 48.0f, 0.0f, 90.0f, 0.0f));
            addView(this.sizeTextView, LayoutHelper.createFrame(69, -2.0f, 21, 0.0f, 0.0f, 21.0f, 0.0f));
        }

        public /* synthetic */ void lambda$new$0(View view) {
            onCheckBoxPressed();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (this.drawDivider) {
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(48.0f), getMeasuredHeight() - 1, Theme.dividerPaint);
                } else {
                    canvas.drawLine(getMeasuredWidth() - AndroidUtilities.dp(90.0f), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            }
        }
    }

    public static boolean fileIsMedia(File file) {
        String lowerCase = file.getName().toLowerCase();
        return file.getName().endsWith("mp4") || file.getName().endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif");
    }
}
