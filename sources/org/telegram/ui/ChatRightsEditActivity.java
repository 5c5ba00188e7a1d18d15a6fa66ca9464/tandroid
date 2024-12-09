package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.DialogRadioCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell2;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CrossfadeDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.TwoStepVerificationActivity;

/* loaded from: classes4.dex */
public class ChatRightsEditActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int addAdminsRow;
    private FrameLayout addBotButton;
    private FrameLayout addBotButtonContainer;
    private int addBotButtonRow;
    private AnimatedTextView addBotButtonText;
    private int addUsersRow;
    private TLRPC.TL_chatAdminRights adminRights;
    private int anonymousRow;
    private boolean asAdmin;
    private ValueAnimator asAdminAnimator;
    private float asAdminT;
    private int banUsersRow;
    private TLRPC.TL_chatBannedRights bannedRights;
    public boolean banning;
    private String botHash;
    private boolean canEdit;
    private int cantEditInfoRow;
    private int changeInfoRow;
    private int channelDeleteMessagesRow;
    private int channelDeleteStoriesRow;
    private int channelEditMessagesRow;
    private int channelEditStoriesRow;
    private boolean channelMessagesExpanded;
    private int channelMessagesRow;
    private int channelPostMessagesRow;
    private int channelPostStoriesRow;
    private boolean channelStoriesExpanded;
    private int channelStoriesRow;
    private long chatId;
    private String currentBannedRights;
    private TLRPC.Chat currentChat;
    private String currentRank;
    private int currentType;
    private TLRPC.User currentUser;
    private TLRPC.TL_chatBannedRights defaultBannedRights;
    private ChatRightsEditActivityDelegate delegate;
    private int deleteMessagesRow;
    private CrossfadeDrawable doneDrawable;
    private ValueAnimator doneDrawableAnimator;
    private int editMesagesRow;
    private int embedLinksRow;
    private boolean initialAsAdmin;
    private boolean initialIsSet;
    private String initialRank;
    private boolean isAddingNew;
    private boolean isChannel;
    private boolean isForum;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private int manageRow;
    private int manageTopicsRow;
    private TLRPC.TL_chatAdminRights myAdminRights;
    private int permissionsEndRow;
    private int permissionsStartRow;
    private int pinMessagesRow;
    private int postMessagesRow;
    private PollEditTextCell rankEditTextCell;
    private int rankHeaderRow;
    private int rankInfoRow;
    private int rankRow;
    private int removeAdminRow;
    private int removeAdminShadowRow;
    private int rightsShadowRow;
    private int rowCount;
    private int sendFilesRow;
    private boolean sendMediaExpanded;
    private int sendMediaRow;
    private int sendMessagesRow;
    private int sendMusicRow;
    private int sendPhotosRow;
    private int sendPollsRow;
    private int sendRoundRow;
    private int sendStickersRow;
    private int sendVideosRow;
    private int sendVoiceRow;
    private int startVoiceChatRow;
    private int transferOwnerRow;
    private int transferOwnerShadowRow;
    private int untilDateRow;
    private int untilSectionRow;
    private boolean loading = false;
    private boolean closingKeyboardAfterFinish = false;

    public interface ChatRightsEditActivityDelegate {
        void didChangeOwner(TLRPC.User user);

        void didSetRights(int i, TLRPC.TL_chatAdminRights tL_chatAdminRights, TLRPC.TL_chatBannedRights tL_chatBannedRights, String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListAdapter extends RecyclerListView.SelectionAdapter {
        private boolean ignoreTextChange;
        private Context mContext;
        private final int VIEW_TYPE_USER_CELL = 0;
        private final int VIEW_TYPE_INFO_CELL = 1;
        private final int VIEW_TYPE_TRANSFER_CELL = 2;
        private final int VIEW_TYPE_HEADER_CELL = 3;
        private final int VIEW_TYPE_SWITCH_CELL = 4;
        private final int VIEW_TYPE_SHADOW_CELL = 5;
        private final int VIEW_TYPE_UNTIL_DATE_CELL = 6;
        private final int VIEW_TYPE_RANK_CELL = 7;
        private final int VIEW_TYPE_ADD_BOT_CELL = 8;
        private final int VIEW_TYPE_EXPANDABLE_SWITCH = 9;
        private final int VIEW_TYPE_INNER_CHECK = 10;

        public ListAdapter(Context context) {
            if (ChatRightsEditActivity.this.currentType == 2) {
                setHasStableIds(true);
            }
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(TextCheckCell2 textCheckCell2) {
            if (textCheckCell2.isEnabled()) {
                if (ChatRightsEditActivity.this.allDefaultMediaBanned()) {
                    new AlertDialog.Builder(ChatRightsEditActivity.this.getParentActivity()).setTitle(LocaleController.getString(R.string.UserRestrictionsCantModify)).setMessage(LocaleController.getString(R.string.UserRestrictionsCantModifyEnabled)).setPositiveButton(LocaleController.getString(R.string.OK), null).create().show();
                    return;
                }
                boolean z = !textCheckCell2.isChecked();
                textCheckCell2.setChecked(z);
                ChatRightsEditActivity.this.setSendMediaEnabled(z);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$2(TextCheckCell2 textCheckCell2) {
            if (textCheckCell2.isEnabled()) {
                boolean isChecked = textCheckCell2.isChecked();
                textCheckCell2.setChecked(isChecked);
                ChatRightsEditActivity.this.setChannelMessagesEnabled(isChecked);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$3(TextCheckCell2 textCheckCell2) {
            if (textCheckCell2.isEnabled()) {
                boolean isChecked = textCheckCell2.isChecked();
                textCheckCell2.setChecked(isChecked);
                ChatRightsEditActivity.this.setChannelStoriesEnabled(isChecked);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            ChatRightsEditActivity.this.onDonePressed();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChatRightsEditActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            if (ChatRightsEditActivity.this.currentType != 2) {
                return super.getItemId(i);
            }
            if (i == ChatRightsEditActivity.this.manageRow) {
                return 1L;
            }
            if (i == ChatRightsEditActivity.this.changeInfoRow) {
                return 2L;
            }
            if (i == ChatRightsEditActivity.this.postMessagesRow) {
                return 3L;
            }
            if (i == ChatRightsEditActivity.this.editMesagesRow) {
                return 4L;
            }
            if (i == ChatRightsEditActivity.this.deleteMessagesRow) {
                return 5L;
            }
            if (i == ChatRightsEditActivity.this.addAdminsRow) {
                return 6L;
            }
            if (i == ChatRightsEditActivity.this.anonymousRow) {
                return 7L;
            }
            if (i == ChatRightsEditActivity.this.banUsersRow) {
                return 8L;
            }
            if (i == ChatRightsEditActivity.this.addUsersRow) {
                return 9L;
            }
            if (i == ChatRightsEditActivity.this.pinMessagesRow) {
                return 10L;
            }
            if (i == ChatRightsEditActivity.this.rightsShadowRow) {
                return 11L;
            }
            if (i == ChatRightsEditActivity.this.removeAdminRow) {
                return 12L;
            }
            if (i == ChatRightsEditActivity.this.removeAdminShadowRow) {
                return 13L;
            }
            if (i == ChatRightsEditActivity.this.cantEditInfoRow) {
                return 14L;
            }
            if (i == ChatRightsEditActivity.this.transferOwnerShadowRow) {
                return 15L;
            }
            if (i == ChatRightsEditActivity.this.transferOwnerRow) {
                return 16L;
            }
            if (i == ChatRightsEditActivity.this.rankHeaderRow) {
                return 17L;
            }
            if (i == ChatRightsEditActivity.this.rankRow) {
                return 18L;
            }
            if (i == ChatRightsEditActivity.this.rankInfoRow) {
                return 19L;
            }
            if (i == ChatRightsEditActivity.this.sendMessagesRow) {
                return 20L;
            }
            if (i == ChatRightsEditActivity.this.sendPhotosRow) {
                return 21L;
            }
            if (i == ChatRightsEditActivity.this.sendStickersRow) {
                return 22L;
            }
            if (i == ChatRightsEditActivity.this.sendPollsRow) {
                return 23L;
            }
            if (i == ChatRightsEditActivity.this.embedLinksRow) {
                return 24L;
            }
            if (i == ChatRightsEditActivity.this.startVoiceChatRow) {
                return 25L;
            }
            if (i == ChatRightsEditActivity.this.untilSectionRow) {
                return 26L;
            }
            if (i == ChatRightsEditActivity.this.untilDateRow) {
                return 27L;
            }
            if (i == ChatRightsEditActivity.this.addBotButtonRow) {
                return 28L;
            }
            if (i == ChatRightsEditActivity.this.manageTopicsRow) {
                return 29L;
            }
            if (i == ChatRightsEditActivity.this.sendVideosRow) {
                return 30L;
            }
            if (i == ChatRightsEditActivity.this.sendFilesRow) {
                return 31L;
            }
            if (i == ChatRightsEditActivity.this.sendMusicRow) {
                return 32L;
            }
            if (i == ChatRightsEditActivity.this.sendVoiceRow) {
                return 33L;
            }
            if (i == ChatRightsEditActivity.this.sendRoundRow) {
                return 34L;
            }
            if (i == ChatRightsEditActivity.this.sendMediaRow) {
                return 35L;
            }
            if (i == ChatRightsEditActivity.this.channelMessagesRow) {
                return 36L;
            }
            if (i == ChatRightsEditActivity.this.channelPostMessagesRow) {
                return 37L;
            }
            if (i == ChatRightsEditActivity.this.channelEditMessagesRow) {
                return 38L;
            }
            if (i == ChatRightsEditActivity.this.channelDeleteMessagesRow) {
                return 39L;
            }
            if (i == ChatRightsEditActivity.this.channelStoriesRow) {
                return 40L;
            }
            if (i == ChatRightsEditActivity.this.channelPostStoriesRow) {
                return 41L;
            }
            if (i == ChatRightsEditActivity.this.channelEditStoriesRow) {
                return 42L;
            }
            return i == ChatRightsEditActivity.this.channelDeleteStoriesRow ? 43L : 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (ChatRightsEditActivity.this.isExpandableSendMediaRow(i)) {
                return 10;
            }
            if (i == ChatRightsEditActivity.this.sendMediaRow || i == ChatRightsEditActivity.this.channelMessagesRow || i == ChatRightsEditActivity.this.channelStoriesRow) {
                return 9;
            }
            if (i == 0) {
                return 0;
            }
            if (i == 1 || i == ChatRightsEditActivity.this.rightsShadowRow || i == ChatRightsEditActivity.this.removeAdminShadowRow || i == ChatRightsEditActivity.this.untilSectionRow || i == ChatRightsEditActivity.this.transferOwnerShadowRow) {
                return 5;
            }
            if (i == 2 || i == ChatRightsEditActivity.this.rankHeaderRow) {
                return 3;
            }
            if (i == ChatRightsEditActivity.this.changeInfoRow || i == ChatRightsEditActivity.this.postMessagesRow || i == ChatRightsEditActivity.this.editMesagesRow || i == ChatRightsEditActivity.this.deleteMessagesRow || i == ChatRightsEditActivity.this.addAdminsRow || i == ChatRightsEditActivity.this.banUsersRow || i == ChatRightsEditActivity.this.addUsersRow || i == ChatRightsEditActivity.this.pinMessagesRow || i == ChatRightsEditActivity.this.sendMessagesRow || i == ChatRightsEditActivity.this.anonymousRow || i == ChatRightsEditActivity.this.startVoiceChatRow || i == ChatRightsEditActivity.this.manageRow || i == ChatRightsEditActivity.this.manageTopicsRow) {
                return 4;
            }
            if (i == ChatRightsEditActivity.this.cantEditInfoRow || i == ChatRightsEditActivity.this.rankInfoRow) {
                return 1;
            }
            if (i == ChatRightsEditActivity.this.untilDateRow) {
                return 6;
            }
            if (i == ChatRightsEditActivity.this.rankRow) {
                return 7;
            }
            return i == ChatRightsEditActivity.this.addBotButtonRow ? 8 : 2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (ChatRightsEditActivity.this.currentChat.creator && ((ChatRightsEditActivity.this.currentType == 0 || (ChatRightsEditActivity.this.currentType == 2 && ChatRightsEditActivity.this.asAdmin)) && itemViewType == 4 && viewHolder.getAdapterPosition() == ChatRightsEditActivity.this.anonymousRow)) {
                return true;
            }
            if (!ChatRightsEditActivity.this.canEdit) {
                return false;
            }
            if ((ChatRightsEditActivity.this.currentType == 0 || ChatRightsEditActivity.this.currentType == 2) && itemViewType == 4) {
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == ChatRightsEditActivity.this.manageRow) {
                    if (ChatRightsEditActivity.this.myAdminRights.add_admins) {
                        return true;
                    }
                    return ChatRightsEditActivity.this.currentChat != null && ChatRightsEditActivity.this.currentChat.creator;
                }
                if (ChatRightsEditActivity.this.currentType == 2 && !ChatRightsEditActivity.this.asAdmin) {
                    return false;
                }
                if (adapterPosition == ChatRightsEditActivity.this.changeInfoRow) {
                    return ChatRightsEditActivity.this.myAdminRights.change_info && (ChatRightsEditActivity.this.defaultBannedRights == null || ChatRightsEditActivity.this.defaultBannedRights.change_info || ChatRightsEditActivity.this.isChannel);
                }
                if (adapterPosition == ChatRightsEditActivity.this.postMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.post_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.editMesagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.edit_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.deleteMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.delete_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.startVoiceChatRow) {
                    return ChatRightsEditActivity.this.myAdminRights.manage_call;
                }
                if (adapterPosition == ChatRightsEditActivity.this.addAdminsRow) {
                    return ChatRightsEditActivity.this.myAdminRights.add_admins;
                }
                if (adapterPosition == ChatRightsEditActivity.this.anonymousRow) {
                    return ChatRightsEditActivity.this.myAdminRights.anonymous;
                }
                if (adapterPosition == ChatRightsEditActivity.this.banUsersRow) {
                    return ChatRightsEditActivity.this.myAdminRights.ban_users;
                }
                if (adapterPosition == ChatRightsEditActivity.this.addUsersRow) {
                    return ChatRightsEditActivity.this.myAdminRights.invite_users;
                }
                if (adapterPosition == ChatRightsEditActivity.this.pinMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.pin_messages && (ChatRightsEditActivity.this.defaultBannedRights == null || ChatRightsEditActivity.this.defaultBannedRights.pin_messages);
                }
                if (adapterPosition == ChatRightsEditActivity.this.manageTopicsRow) {
                    return ChatRightsEditActivity.this.myAdminRights.manage_topics;
                }
                if (adapterPosition == ChatRightsEditActivity.this.channelPostStoriesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.post_stories;
                }
                if (adapterPosition == ChatRightsEditActivity.this.channelEditStoriesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.edit_stories;
                }
                if (adapterPosition == ChatRightsEditActivity.this.channelDeleteStoriesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.delete_stories;
                }
            }
            return (itemViewType == 3 || itemViewType == 1 || itemViewType == 5 || itemViewType == 8) ? false : true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:104:0x0234, code lost:
        
            if (r11.this$0.defaultBannedRights.send_roundvideos != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:168:0x03c5, code lost:
        
            if (r11.this$0.rankRow == (-1)) goto L179;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0061, code lost:
        
            if (r11.this$0.defaultBannedRights.send_stickers != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:179:0x03ef, code lost:
        
            if (r11.this$0.canEdit != false) goto L187;
         */
        /* JADX WARN: Code restructure failed: missing block: B:198:0x0465, code lost:
        
            if (r11.this$0.allDefaultMediaBanned() != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0236, code lost:
        
            r3 = org.telegram.messenger.R.drawable.permission_locked;
         */
        /* JADX WARN: Code restructure failed: missing block: B:214:0x092e, code lost:
        
            r0 = org.telegram.messenger.R.drawable.permission_locked;
         */
        /* JADX WARN: Code restructure failed: missing block: B:235:0x04fe, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:253:0x0556, code lost:
        
            if (r11.this$0.defaultBannedRights.change_info != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:261:0x0572, code lost:
        
            if (r11.this$0.adminRights.change_info != false) goto L266;
         */
        /* JADX WARN: Code restructure failed: missing block: B:262:0x0591, code lost:
        
            r0 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:267:0x05a9, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:272:0x0585, code lost:
        
            if (r11.this$0.adminRights.change_info != false) goto L266;
         */
        /* JADX WARN: Code restructure failed: missing block: B:274:0x058f, code lost:
        
            if (r11.this$0.defaultBannedRights.change_info == false) goto L266;
         */
        /* JADX WARN: Code restructure failed: missing block: B:286:0x05df, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:299:0x0615, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:308:0x0639, code lost:
        
            if (r11.this$0.adminRights.delete_messages != false) goto L315;
         */
        /* JADX WARN: Code restructure failed: missing block: B:309:0x064e, code lost:
        
            r0 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:314:0x0666, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:319:0x064c, code lost:
        
            if (r11.this$0.adminRights.delete_messages != false) goto L315;
         */
        /* JADX WARN: Code restructure failed: missing block: B:334:0x06a7, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00ad, code lost:
        
            if (r11.this$0.defaultBannedRights.embed_links != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:351:0x06e8, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:365:0x071e, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:378:0x0754, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:399:0x07b2, code lost:
        
            if (r11.this$0.defaultBannedRights.manage_topics != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:410:0x07e0, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:429:0x0847, code lost:
        
            if (r11.this$0.defaultBannedRights.invite_users != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:440:0x0875, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x00e5, code lost:
        
            if (r11.this$0.defaultBannedRights.send_polls != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:456:0x08c2, code lost:
        
            if (r11.this$0.defaultBannedRights.pin_messages != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:467:0x08f9, code lost:
        
            if (r5 == false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:479:0x092c, code lost:
        
            if (r11.this$0.defaultBannedRights.send_plain != false) goto L495;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x011d, code lost:
        
            if (r11.this$0.defaultBannedRights.send_photos != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:64:0x0155, code lost:
        
            if (r11.this$0.defaultBannedRights.send_videos != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:74:0x018d, code lost:
        
            if (r11.this$0.defaultBannedRights.send_audios != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x01c5, code lost:
        
            if (r11.this$0.defaultBannedRights.send_docs != false) goto L113;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x01fd, code lost:
        
            if (r11.this$0.defaultBannedRights.send_voices != false) goto L113;
         */
        /* JADX WARN: Removed duplicated region for block: B:203:0x093f  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String formatString;
            int i2;
            int i3;
            String string;
            boolean z;
            String string2;
            boolean z2;
            String format;
            boolean z3;
            Runnable runnable;
            int i4;
            Context context;
            int i5;
            String string3;
            boolean z4;
            boolean z5 = false;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            r3 = 0;
            int i6 = 0;
            z5 = false;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    ((UserCell2) viewHolder.itemView).setData(ChatRightsEditActivity.this.currentUser, null, ChatRightsEditActivity.this.currentType == 2 ? LocaleController.getString(R.string.Bot) : null, 0);
                    break;
                case 1:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == ChatRightsEditActivity.this.cantEditInfoRow) {
                        formatString = LocaleController.getString(R.string.EditAdminCantEdit);
                    } else if (i == ChatRightsEditActivity.this.rankInfoRow) {
                        formatString = LocaleController.formatString("EditAdminRankInfo", R.string.EditAdminRankInfo, LocaleController.getString((UserObject.isUserSelf(ChatRightsEditActivity.this.currentUser) && ChatRightsEditActivity.this.currentChat.creator) ? R.string.ChannelCreator : R.string.ChannelAdmin));
                    }
                    textInfoPrivacyCell.setText(formatString);
                    break;
                case 2:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    if (i == ChatRightsEditActivity.this.removeAdminRow) {
                        int i7 = Theme.key_text_RedRegular;
                        textSettingsCell.setTextColor(Theme.getColor(i7));
                        textSettingsCell.setTag(Integer.valueOf(i7));
                        if (ChatRightsEditActivity.this.currentType == 0) {
                            i2 = R.string.EditAdminRemoveAdmin;
                        } else if (ChatRightsEditActivity.this.currentType == 1) {
                            i2 = R.string.UserRestrictionsBlock;
                        }
                    } else if (i == ChatRightsEditActivity.this.transferOwnerRow) {
                        int i8 = Theme.key_windowBackgroundWhiteBlackText;
                        textSettingsCell.setTextColor(Theme.getColor(i8));
                        textSettingsCell.setTag(Integer.valueOf(i8));
                        i2 = ChatRightsEditActivity.this.isChannel ? R.string.EditAdminChannelTransfer : R.string.EditAdminGroupTransfer;
                    }
                    textSettingsCell.setText(LocaleController.getString(i2), false);
                    break;
                case 3:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == 2) {
                        if (ChatRightsEditActivity.this.currentType == 2 || (ChatRightsEditActivity.this.currentUser != null && ChatRightsEditActivity.this.currentUser.bot)) {
                            i3 = R.string.BotRestrictionsCanDo;
                        } else if (ChatRightsEditActivity.this.currentType == 0) {
                            i3 = R.string.EditAdminWhatCanDo;
                        } else if (ChatRightsEditActivity.this.currentType == 1) {
                            i3 = R.string.UserRestrictionsCanDo;
                        }
                    } else if (i == ChatRightsEditActivity.this.rankHeaderRow) {
                        i3 = R.string.EditAdminRank;
                    }
                    headerCell.setText(LocaleController.getString(i3));
                    break;
                case 4:
                case 9:
                    final TextCheckCell2 textCheckCell2 = (TextCheckCell2) viewHolder.itemView;
                    boolean z6 = ChatRightsEditActivity.this.currentType != 2 || ChatRightsEditActivity.this.asAdmin;
                    boolean z7 = ChatRightsEditActivity.this.currentChat != null && ChatRightsEditActivity.this.currentChat.creator;
                    if (i == ChatRightsEditActivity.this.sendMediaRow) {
                        int sendMediaSelectedCount = ChatRightsEditActivity.this.getSendMediaSelectedCount();
                        textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.UserRestrictionsSendMedia), sendMediaSelectedCount > 0, true, true);
                        textCheckCell2.setCollapseArrow(String.format(Locale.US, "%d/9", Integer.valueOf(sendMediaSelectedCount)), !ChatRightsEditActivity.this.sendMediaExpanded, new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$ListAdapter$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatRightsEditActivity.ListAdapter.this.lambda$onBindViewHolder$1(textCheckCell2);
                            }
                        });
                        break;
                    } else {
                        if (i == ChatRightsEditActivity.this.channelMessagesRow) {
                            int channelMessagesSelectedCount = ChatRightsEditActivity.this.getChannelMessagesSelectedCount();
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.ChannelManageMessages), channelMessagesSelectedCount > 0, true, true);
                            format = String.format(Locale.US, "%d/3", Integer.valueOf(channelMessagesSelectedCount));
                            z3 = !ChatRightsEditActivity.this.channelMessagesExpanded;
                            runnable = new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$ListAdapter$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatRightsEditActivity.ListAdapter.this.lambda$onBindViewHolder$2(textCheckCell2);
                                }
                            };
                        } else if (i == ChatRightsEditActivity.this.channelStoriesRow) {
                            int channelStoriesSelectedCount = ChatRightsEditActivity.this.getChannelStoriesSelectedCount();
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.ChannelManageStories), channelStoriesSelectedCount > 0, true, true);
                            format = String.format(Locale.US, "%d/3", Integer.valueOf(channelStoriesSelectedCount));
                            z3 = !ChatRightsEditActivity.this.channelStoriesExpanded;
                            runnable = new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$ListAdapter$$ExternalSyntheticLambda3
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatRightsEditActivity.ListAdapter.this.lambda$onBindViewHolder$3(textCheckCell2);
                                }
                            };
                        } else if (i == ChatRightsEditActivity.this.manageRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.ManageGroup), ChatRightsEditActivity.this.asAdmin, true);
                            if (!ChatRightsEditActivity.this.myAdminRights.add_admins) {
                            }
                            i4 = 0;
                        } else if (i == ChatRightsEditActivity.this.changeInfoRow) {
                            if (ChatRightsEditActivity.this.currentType == 0 || ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.isChannel) {
                                    string2 = LocaleController.getString(R.string.EditAdminChangeGroupInfo);
                                    if (z6) {
                                        break;
                                    }
                                    break;
                                } else {
                                    string2 = LocaleController.getString(R.string.EditAdminChangeChannelInfo);
                                    if (z6) {
                                        break;
                                    }
                                    z2 = false;
                                }
                                textCheckCell2.setTextAndCheck(string2, z2, true);
                                if (ChatRightsEditActivity.this.currentType == 2) {
                                    if (!ChatRightsEditActivity.this.myAdminRights.change_info) {
                                    }
                                    i4 = 0;
                                }
                                if (ChatRightsEditActivity.this.currentType != 2 && i == ChatRightsEditActivity.this.sendMessagesRow) {
                                    if (!ChatRightsEditActivity.this.bannedRights.view_messages && !ChatRightsEditActivity.this.defaultBannedRights.view_messages) {
                                        z5 = true;
                                    }
                                    textCheckCell2.setEnabled(z5);
                                    break;
                                }
                            } else {
                                if (ChatRightsEditActivity.this.currentType == 1) {
                                    textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.UserRestrictionsChangeInfo), (ChatRightsEditActivity.this.bannedRights.change_info || ChatRightsEditActivity.this.defaultBannedRights.change_info) ? false : true, ChatRightsEditActivity.this.manageTopicsRow != -1);
                                    break;
                                }
                                if (ChatRightsEditActivity.this.currentType != 2) {
                                    if (!ChatRightsEditActivity.this.bannedRights.view_messages) {
                                        z5 = true;
                                    }
                                    textCheckCell2.setEnabled(z5);
                                }
                            }
                        } else if (i == ChatRightsEditActivity.this.postMessagesRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminPostMessages), z6 && ChatRightsEditActivity.this.adminRights.post_messages, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.post_messages) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.editMesagesRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminEditMessages), z6 && ChatRightsEditActivity.this.adminRights.edit_messages, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.edit_messages) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.deleteMessagesRow) {
                            if (ChatRightsEditActivity.this.isChannel) {
                                string = LocaleController.getString(R.string.EditAdminDeleteMessages);
                                if (z6) {
                                    break;
                                }
                                z = false;
                            } else {
                                string = LocaleController.getString(R.string.EditAdminGroupDeleteMessages);
                                if (z6) {
                                    break;
                                }
                                z = false;
                            }
                            textCheckCell2.setTextAndCheck(string, z, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.delete_messages) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.addAdminsRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminAddAdmins), z6 && ChatRightsEditActivity.this.adminRights.add_admins, ChatRightsEditActivity.this.anonymousRow != -1);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.add_admins) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.anonymousRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminSendAnonymously), z6 && ChatRightsEditActivity.this.adminRights.anonymous, ChatRightsEditActivity.this.manageTopicsRow != -1);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.anonymous) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.banUsersRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminBanUsers), z6 && ChatRightsEditActivity.this.adminRights.ban_users, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.ban_users) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.startVoiceChatRow) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.StartVoipChatPermission), z6 && ChatRightsEditActivity.this.adminRights.manage_call, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.manage_call) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.manageTopicsRow) {
                            if (ChatRightsEditActivity.this.currentType == 0) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.ManageTopicsPermission), z6 && ChatRightsEditActivity.this.adminRights.manage_topics, false);
                            } else if (ChatRightsEditActivity.this.currentType == 1) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.CreateTopicsPermission), (ChatRightsEditActivity.this.bannedRights.manage_topics || ChatRightsEditActivity.this.defaultBannedRights.manage_topics) ? false : true, false);
                                break;
                            } else if (ChatRightsEditActivity.this.currentType == 2) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.ManageTopicsPermission), z6 && ChatRightsEditActivity.this.adminRights.manage_topics, false);
                                if (!ChatRightsEditActivity.this.myAdminRights.manage_topics) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i == ChatRightsEditActivity.this.addUsersRow) {
                            if (ChatRightsEditActivity.this.currentType == 0) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(ChatObject.isActionBannedByDefault(ChatRightsEditActivity.this.currentChat, 3) ? R.string.EditAdminAddUsers : R.string.EditAdminAddUsersViaLink), ChatRightsEditActivity.this.adminRights.invite_users, true);
                            } else if (ChatRightsEditActivity.this.currentType == 1) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.UserRestrictionsInviteUsers), (ChatRightsEditActivity.this.bannedRights.invite_users || ChatRightsEditActivity.this.defaultBannedRights.invite_users) ? false : true, true);
                                break;
                            } else if (ChatRightsEditActivity.this.currentType == 2) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminAddUsersViaLink), z6 && ChatRightsEditActivity.this.adminRights.invite_users, true);
                                if (!ChatRightsEditActivity.this.myAdminRights.invite_users) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (i != ChatRightsEditActivity.this.pinMessagesRow) {
                            if (i == ChatRightsEditActivity.this.sendMessagesRow) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.UserRestrictionsSend), (ChatRightsEditActivity.this.bannedRights.send_plain || ChatRightsEditActivity.this.defaultBannedRights.send_plain) ? false : true, true);
                                break;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else if (ChatRightsEditActivity.this.currentType == 0 || ChatRightsEditActivity.this.currentType == 2) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.EditAdminPinMessages), (z6 && ChatRightsEditActivity.this.adminRights.pin_messages) || !ChatRightsEditActivity.this.defaultBannedRights.pin_messages, true);
                            if (ChatRightsEditActivity.this.currentType == 2) {
                                if (!ChatRightsEditActivity.this.myAdminRights.pin_messages) {
                                }
                                i4 = 0;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        } else {
                            if (ChatRightsEditActivity.this.currentType == 1) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString(R.string.UserRestrictionsPinMessages), (ChatRightsEditActivity.this.bannedRights.pin_messages || ChatRightsEditActivity.this.defaultBannedRights.pin_messages) ? false : true, true);
                                break;
                            }
                            if (ChatRightsEditActivity.this.currentType != 2) {
                            }
                        }
                        textCheckCell2.setCollapseArrow(format, z3, runnable);
                        if (ChatRightsEditActivity.this.currentType != 2) {
                        }
                    }
                    textCheckCell2.setIcon(i4);
                    if (ChatRightsEditActivity.this.currentType != 2) {
                    }
                    break;
                case 5:
                    ShadowSectionCell shadowSectionCell = (ShadowSectionCell) viewHolder.itemView;
                    shadowSectionCell.setAlpha((ChatRightsEditActivity.this.currentType == 2 && (i == ChatRightsEditActivity.this.rightsShadowRow || i == ChatRightsEditActivity.this.rankInfoRow)) ? ChatRightsEditActivity.this.asAdminT : 1.0f);
                    if (i == ChatRightsEditActivity.this.rightsShadowRow) {
                        context = this.mContext;
                        if (ChatRightsEditActivity.this.removeAdminRow == -1) {
                            break;
                        }
                        i5 = R.drawable.greydivider;
                    } else {
                        if (i == ChatRightsEditActivity.this.removeAdminShadowRow) {
                            context = this.mContext;
                        } else {
                            if (i == ChatRightsEditActivity.this.rankInfoRow) {
                                context = this.mContext;
                                break;
                            } else {
                                context = this.mContext;
                            }
                            i5 = R.drawable.greydivider;
                        }
                        i5 = R.drawable.greydivider_bottom;
                    }
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i5, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 6:
                    TextDetailCell textDetailCell = (TextDetailCell) viewHolder.itemView;
                    if (i == ChatRightsEditActivity.this.untilDateRow) {
                        textDetailCell.setTextAndValue(LocaleController.getString(R.string.UserRestrictionsDuration), (ChatRightsEditActivity.this.bannedRights.until_date == 0 || Math.abs(((long) ChatRightsEditActivity.this.bannedRights.until_date) - (System.currentTimeMillis() / 1000)) > 315360000) ? LocaleController.getString(R.string.UserRestrictionsUntilForever) : LocaleController.formatDateForBan(ChatRightsEditActivity.this.bannedRights.until_date), false);
                        break;
                    }
                    break;
                case 7:
                    PollEditTextCell pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                    String string4 = LocaleController.getString((UserObject.isUserSelf(ChatRightsEditActivity.this.currentUser) && ChatRightsEditActivity.this.currentChat.creator) ? R.string.ChannelCreator : R.string.ChannelAdmin);
                    this.ignoreTextChange = true;
                    pollEditTextCell.getTextView().setEnabled(ChatRightsEditActivity.this.canEdit || ChatRightsEditActivity.this.currentChat.creator);
                    pollEditTextCell.getTextView().setSingleLine(true);
                    pollEditTextCell.getTextView().setImeOptions(6);
                    pollEditTextCell.setTextAndHint(ChatRightsEditActivity.this.currentRank, string4, false);
                    this.ignoreTextChange = false;
                    break;
                case 10:
                    CheckBoxCell checkBoxCell = (CheckBoxCell) viewHolder.itemView;
                    boolean z8 = checkBoxCell.getTag() != null && ((Integer) checkBoxCell.getTag()).intValue() == i;
                    checkBoxCell.setTag(Integer.valueOf(i));
                    if (i != ChatRightsEditActivity.this.sendStickersRow) {
                        if (i != ChatRightsEditActivity.this.embedLinksRow) {
                            if (i != ChatRightsEditActivity.this.sendPollsRow) {
                                if (i != ChatRightsEditActivity.this.sendPhotosRow) {
                                    if (i != ChatRightsEditActivity.this.sendVideosRow) {
                                        if (i != ChatRightsEditActivity.this.sendMusicRow) {
                                            if (i != ChatRightsEditActivity.this.sendFilesRow) {
                                                if (i != ChatRightsEditActivity.this.sendVoiceRow) {
                                                    if (i != ChatRightsEditActivity.this.sendRoundRow) {
                                                        if (i == ChatRightsEditActivity.this.channelPostMessagesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminPostMessages);
                                                            z4 = ChatRightsEditActivity.this.adminRights.post_messages;
                                                        } else if (i == ChatRightsEditActivity.this.channelEditMessagesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminEditMessages);
                                                            z4 = ChatRightsEditActivity.this.adminRights.edit_messages;
                                                        } else if (i == ChatRightsEditActivity.this.channelDeleteMessagesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminDeleteMessages);
                                                            z4 = ChatRightsEditActivity.this.adminRights.delete_messages;
                                                        } else if (i == ChatRightsEditActivity.this.channelPostStoriesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminPostStories);
                                                            z4 = ChatRightsEditActivity.this.adminRights.post_stories;
                                                        } else if (i == ChatRightsEditActivity.this.channelEditStoriesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminEditStories);
                                                            z4 = ChatRightsEditActivity.this.adminRights.edit_stories;
                                                        } else if (i == ChatRightsEditActivity.this.channelDeleteStoriesRow) {
                                                            string3 = LocaleController.getString(R.string.EditAdminDeleteStories);
                                                            z4 = ChatRightsEditActivity.this.adminRights.delete_stories;
                                                        }
                                                        checkBoxCell.setText(string3, "", z4, true, z8);
                                                        break;
                                                    } else {
                                                        checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionRound), "", (ChatRightsEditActivity.this.bannedRights.send_roundvideos || ChatRightsEditActivity.this.defaultBannedRights.send_roundvideos) ? false : true, true, z8);
                                                        break;
                                                    }
                                                } else {
                                                    checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionVoice), "", (ChatRightsEditActivity.this.bannedRights.send_voices || ChatRightsEditActivity.this.defaultBannedRights.send_voices) ? false : true, true, z8);
                                                    break;
                                                }
                                            } else {
                                                checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionFiles), "", (ChatRightsEditActivity.this.bannedRights.send_docs || ChatRightsEditActivity.this.defaultBannedRights.send_docs) ? false : true, true, z8);
                                                break;
                                            }
                                        } else {
                                            checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionMusic), "", (ChatRightsEditActivity.this.bannedRights.send_audios || ChatRightsEditActivity.this.defaultBannedRights.send_audios) ? false : true, true, z8);
                                            break;
                                        }
                                    } else {
                                        checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionVideos), "", (ChatRightsEditActivity.this.bannedRights.send_videos || ChatRightsEditActivity.this.defaultBannedRights.send_videos) ? false : true, true, z8);
                                        break;
                                    }
                                } else {
                                    checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionPhotos), "", (ChatRightsEditActivity.this.bannedRights.send_photos || ChatRightsEditActivity.this.defaultBannedRights.send_photos) ? false : true, true, z8);
                                    break;
                                }
                            } else {
                                checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPolls), "", (ChatRightsEditActivity.this.bannedRights.send_polls || ChatRightsEditActivity.this.defaultBannedRights.send_polls) ? false : true, true, z8);
                                break;
                            }
                        } else {
                            checkBoxCell.setText(LocaleController.getString(R.string.UserRestrictionsEmbedLinks), "", (ChatRightsEditActivity.this.bannedRights.embed_links || ChatRightsEditActivity.this.defaultBannedRights.embed_links || ChatRightsEditActivity.this.bannedRights.send_plain || ChatRightsEditActivity.this.defaultBannedRights.send_plain) ? false : true, true, z8);
                            break;
                        }
                    } else {
                        checkBoxCell.setText(LocaleController.getString(R.string.SendMediaPermissionStickersGifs), "", (ChatRightsEditActivity.this.bannedRights.send_stickers || ChatRightsEditActivity.this.defaultBannedRights.send_stickers) ? false : true, true, z8);
                        break;
                    }
                    checkBoxCell.setIcon(i6);
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            View view3;
            switch (i) {
                case 0:
                    view = new UserCell2(this.mContext, 4, 0);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view;
                    break;
                case 1:
                    View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    view2 = textInfoPrivacyCell;
                    break;
                case 2:
                default:
                    view3 = new TextSettingsCell(this.mContext);
                    view3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view3;
                    break;
                case 3:
                    view3 = new HeaderCell(this.mContext, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, true);
                    view3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view3;
                    break;
                case 4:
                case 9:
                    view3 = new TextCheckCell2(this.mContext);
                    view3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view3;
                    break;
                case 5:
                    view2 = new ShadowSectionCell(this.mContext);
                    break;
                case 6:
                    view3 = new TextDetailCell(this.mContext);
                    view3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view3;
                    break;
                case 7:
                    PollEditTextCell pollEditTextCell = ChatRightsEditActivity.this.rankEditTextCell = new PollEditTextCell(this.mContext, null);
                    pollEditTextCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    pollEditTextCell.addTextWatcher(new TextWatcher() { // from class: org.telegram.ui.ChatRightsEditActivity.ListAdapter.1
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (ListAdapter.this.ignoreTextChange) {
                                return;
                            }
                            ChatRightsEditActivity.this.currentRank = editable.toString();
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = ChatRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChatRightsEditActivity.this.rankHeaderRow);
                            if (findViewHolderForAdapterPosition != null) {
                                ChatRightsEditActivity.this.setTextLeft(findViewHolderForAdapterPosition.itemView);
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                        }
                    });
                    view2 = pollEditTextCell;
                    break;
                case 8:
                    ChatRightsEditActivity.this.addBotButtonContainer = new FrameLayout(this.mContext);
                    FrameLayout frameLayout = ChatRightsEditActivity.this.addBotButtonContainer;
                    int i2 = Theme.key_windowBackgroundGray;
                    frameLayout.setBackgroundColor(Theme.getColor(i2));
                    ChatRightsEditActivity.this.addBotButton = new FrameLayout(this.mContext);
                    ChatRightsEditActivity.this.addBotButtonText = new AnimatedTextView(this.mContext, true, false, false);
                    ChatRightsEditActivity.this.addBotButtonText.setTypeface(AndroidUtilities.bold());
                    ChatRightsEditActivity.this.addBotButtonText.setTextColor(-1);
                    ChatRightsEditActivity.this.addBotButtonText.setTextSize(AndroidUtilities.dp(14.0f));
                    ChatRightsEditActivity.this.addBotButtonText.setGravity(17);
                    AnimatedTextView animatedTextView = ChatRightsEditActivity.this.addBotButtonText;
                    StringBuilder sb = new StringBuilder();
                    sb.append(LocaleController.getString(R.string.AddBotButton));
                    sb.append(" ");
                    sb.append(LocaleController.getString(ChatRightsEditActivity.this.asAdmin ? R.string.AddBotButtonAsAdmin : R.string.AddBotButtonAsMember));
                    animatedTextView.setText(sb.toString());
                    ChatRightsEditActivity.this.addBotButton.addView(ChatRightsEditActivity.this.addBotButtonText, LayoutHelper.createFrame(-2, -2, 17));
                    ChatRightsEditActivity.this.addBotButton.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 4.0f));
                    ChatRightsEditActivity.this.addBotButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view4) {
                            ChatRightsEditActivity.ListAdapter.this.lambda$onCreateViewHolder$0(view4);
                        }
                    });
                    ChatRightsEditActivity.this.addBotButtonContainer.addView(ChatRightsEditActivity.this.addBotButton, LayoutHelper.createFrame(-1, 48.0f, 119, 14.0f, 28.0f, 14.0f, 14.0f));
                    ChatRightsEditActivity.this.addBotButtonContainer.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                    View view4 = new View(this.mContext);
                    view4.setBackgroundColor(Theme.getColor(i2));
                    ChatRightsEditActivity.this.addBotButtonContainer.setClipChildren(false);
                    ChatRightsEditActivity.this.addBotButtonContainer.setClipToPadding(false);
                    ChatRightsEditActivity.this.addBotButtonContainer.addView(view4, LayoutHelper.createFrame(-1, 800.0f, 87, 0.0f, 0.0f, 0.0f, -800.0f));
                    view2 = ChatRightsEditActivity.this.addBotButtonContainer;
                    break;
                case 10:
                    CheckBoxCell checkBoxCell = new CheckBoxCell(this.mContext, 4, 21, ChatRightsEditActivity.this.getResourceProvider());
                    checkBoxCell.setPad(1);
                    checkBoxCell.getCheckBoxRound().setDrawBackgroundAsArc(14);
                    checkBoxCell.getCheckBoxRound().setColor(Theme.key_switch2TrackChecked, Theme.key_radioBackground, Theme.key_checkboxCheck);
                    checkBoxCell.setEnabled(true);
                    view = checkBoxCell;
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    view2 = view;
                    break;
            }
            return new RecyclerListView.Holder(view2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() == ChatRightsEditActivity.this.rankHeaderRow) {
                ChatRightsEditActivity.this.setTextLeft(viewHolder.itemView);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() != ChatRightsEditActivity.this.rankRow || ChatRightsEditActivity.this.getParentActivity() == null) {
                return;
            }
            AndroidUtilities.hideKeyboard(ChatRightsEditActivity.this.getParentActivity().getCurrentFocus());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:203:0x02ce, code lost:
    
        if (r1 != false) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0389, code lost:
    
        r7 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x038a, code lost:
    
        r16.asAdminT = r7;
        r16.initialIsSet = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0386, code lost:
    
        r7 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0384, code lost:
    
        if (r1 != false) goto L248;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChatRightsEditActivity(long j, long j2, TLRPC.TL_chatAdminRights tL_chatAdminRights, TLRPC.TL_chatBannedRights tL_chatBannedRights, TLRPC.TL_chatBannedRights tL_chatBannedRights2, String str, int i, boolean z, boolean z2, String str2) {
        boolean z3;
        TLRPC.UserFull userFull;
        TLRPC.Chat chat;
        TLRPC.TL_chatAdminRights tL_chatAdminRights2 = tL_chatAdminRights;
        this.asAdminT = 0.0f;
        this.asAdmin = false;
        this.initialAsAdmin = false;
        this.currentBannedRights = "";
        this.isAddingNew = z2;
        this.chatId = j2;
        this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
        this.currentType = i;
        this.canEdit = z;
        boolean z4 = true;
        boolean z5 = !z;
        this.channelStoriesExpanded = z5;
        this.channelMessagesExpanded = z5;
        this.botHash = str2;
        TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chatId));
        this.currentChat = chat2;
        String str3 = str != null ? str : "";
        this.currentRank = str3;
        this.initialRank = str3;
        if (chat2 != null) {
            this.isChannel = ChatObject.isChannel(chat2) && !this.currentChat.megagroup;
            this.isForum = ChatObject.isForum(this.currentChat);
            this.myAdminRights = this.currentChat.admin_rights;
        }
        if (this.myAdminRights == null) {
            this.myAdminRights = emptyAdminRights(this.currentType != 2 || ((chat = this.currentChat) != null && chat.creator));
        }
        if (i == 0 || i == 2) {
            if (i == 2 && (userFull = getMessagesController().getUserFull(j)) != null) {
                TLRPC.TL_chatAdminRights tL_chatAdminRights3 = this.isChannel ? userFull.bot_broadcast_admin_rights : userFull.bot_group_admin_rights;
                if (tL_chatAdminRights3 != null) {
                    if (tL_chatAdminRights2 == null) {
                        tL_chatAdminRights2 = tL_chatAdminRights3;
                    } else {
                        tL_chatAdminRights2.ban_users = tL_chatAdminRights2.ban_users || tL_chatAdminRights3.ban_users;
                        tL_chatAdminRights2.add_admins = tL_chatAdminRights2.add_admins || tL_chatAdminRights3.add_admins;
                        tL_chatAdminRights2.post_messages = tL_chatAdminRights2.post_messages || tL_chatAdminRights3.post_messages;
                        tL_chatAdminRights2.pin_messages = tL_chatAdminRights2.pin_messages || tL_chatAdminRights3.pin_messages;
                        tL_chatAdminRights2.delete_messages = tL_chatAdminRights2.delete_messages || tL_chatAdminRights3.delete_messages;
                        tL_chatAdminRights2.change_info = tL_chatAdminRights2.change_info || tL_chatAdminRights3.change_info;
                        tL_chatAdminRights2.anonymous = tL_chatAdminRights2.anonymous || tL_chatAdminRights3.anonymous;
                        tL_chatAdminRights2.edit_messages = tL_chatAdminRights2.edit_messages || tL_chatAdminRights3.edit_messages;
                        tL_chatAdminRights2.manage_call = tL_chatAdminRights2.manage_call || tL_chatAdminRights3.manage_call;
                        tL_chatAdminRights2.manage_topics = tL_chatAdminRights2.manage_topics || tL_chatAdminRights3.manage_topics;
                        tL_chatAdminRights2.post_stories = tL_chatAdminRights2.post_stories || tL_chatAdminRights3.post_stories;
                        tL_chatAdminRights2.edit_stories = tL_chatAdminRights2.edit_stories || tL_chatAdminRights3.edit_stories;
                        tL_chatAdminRights2.delete_stories = tL_chatAdminRights2.delete_stories || tL_chatAdminRights3.delete_stories;
                        tL_chatAdminRights2.other = tL_chatAdminRights2.other || tL_chatAdminRights3.other;
                    }
                }
            }
            if (tL_chatAdminRights2 == null) {
                this.initialAsAdmin = false;
                if (i == 2) {
                    this.adminRights = emptyAdminRights(false);
                    boolean z6 = this.isChannel;
                    this.asAdmin = z6;
                } else {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights4 = new TLRPC.TL_chatAdminRights();
                    this.adminRights = tL_chatAdminRights4;
                    TLRPC.TL_chatAdminRights tL_chatAdminRights5 = this.myAdminRights;
                    tL_chatAdminRights4.change_info = tL_chatAdminRights5.change_info;
                    tL_chatAdminRights4.post_messages = tL_chatAdminRights5.post_messages;
                    tL_chatAdminRights4.edit_messages = tL_chatAdminRights5.edit_messages;
                    tL_chatAdminRights4.delete_messages = tL_chatAdminRights5.delete_messages;
                    tL_chatAdminRights4.manage_call = tL_chatAdminRights5.manage_call;
                    tL_chatAdminRights4.ban_users = tL_chatAdminRights5.ban_users;
                    tL_chatAdminRights4.invite_users = tL_chatAdminRights5.invite_users;
                    tL_chatAdminRights4.pin_messages = tL_chatAdminRights5.pin_messages;
                    tL_chatAdminRights4.manage_topics = tL_chatAdminRights5.manage_topics;
                    tL_chatAdminRights4.post_stories = tL_chatAdminRights5.post_stories;
                    tL_chatAdminRights4.edit_stories = tL_chatAdminRights5.edit_stories;
                    tL_chatAdminRights4.delete_stories = tL_chatAdminRights5.delete_stories;
                    tL_chatAdminRights4.other = tL_chatAdminRights5.other;
                    this.initialIsSet = false;
                }
            } else {
                this.initialAsAdmin = true;
                TLRPC.TL_chatAdminRights tL_chatAdminRights6 = new TLRPC.TL_chatAdminRights();
                this.adminRights = tL_chatAdminRights6;
                boolean z7 = tL_chatAdminRights2.change_info;
                tL_chatAdminRights6.change_info = z7;
                boolean z8 = tL_chatAdminRights2.post_messages;
                tL_chatAdminRights6.post_messages = z8;
                boolean z9 = tL_chatAdminRights2.edit_messages;
                tL_chatAdminRights6.edit_messages = z9;
                boolean z10 = tL_chatAdminRights2.delete_messages;
                tL_chatAdminRights6.delete_messages = z10;
                boolean z11 = tL_chatAdminRights2.manage_call;
                tL_chatAdminRights6.manage_call = z11;
                boolean z12 = tL_chatAdminRights2.ban_users;
                tL_chatAdminRights6.ban_users = z12;
                boolean z13 = tL_chatAdminRights2.invite_users;
                tL_chatAdminRights6.invite_users = z13;
                boolean z14 = tL_chatAdminRights2.pin_messages;
                tL_chatAdminRights6.pin_messages = z14;
                boolean z15 = tL_chatAdminRights2.manage_topics;
                tL_chatAdminRights6.manage_topics = z15;
                tL_chatAdminRights6.post_stories = tL_chatAdminRights2.post_stories;
                tL_chatAdminRights6.edit_stories = tL_chatAdminRights2.edit_stories;
                tL_chatAdminRights6.delete_stories = tL_chatAdminRights2.delete_stories;
                boolean z16 = tL_chatAdminRights2.add_admins;
                tL_chatAdminRights6.add_admins = z16;
                boolean z17 = tL_chatAdminRights2.anonymous;
                tL_chatAdminRights6.anonymous = z17;
                boolean z18 = tL_chatAdminRights2.other;
                tL_chatAdminRights6.other = z18;
                boolean z19 = z7 || z8 || z9 || z10 || z12 || z13 || z14 || z16 || z11 || z17 || z15 || z18;
                this.initialIsSet = z19;
                if (i == 2) {
                    boolean z20 = this.isChannel || z19;
                    this.asAdmin = z20;
                }
            }
            TLRPC.Chat chat3 = this.currentChat;
            if (chat3 != null) {
                this.defaultBannedRights = chat3.default_banned_rights;
            }
            if (this.defaultBannedRights == null) {
                TLRPC.TL_chatBannedRights tL_chatBannedRights3 = new TLRPC.TL_chatBannedRights();
                this.defaultBannedRights = tL_chatBannedRights3;
                tL_chatBannedRights3.send_roundvideos = false;
                tL_chatBannedRights3.send_voices = false;
                tL_chatBannedRights3.send_docs = false;
                tL_chatBannedRights3.send_audios = false;
                tL_chatBannedRights3.send_photos = false;
                tL_chatBannedRights3.send_videos = false;
                tL_chatBannedRights3.send_plain = false;
                tL_chatBannedRights3.manage_topics = false;
                tL_chatBannedRights3.pin_messages = false;
                tL_chatBannedRights3.change_info = false;
                tL_chatBannedRights3.invite_users = false;
                tL_chatBannedRights3.send_polls = false;
                tL_chatBannedRights3.send_inline = false;
                tL_chatBannedRights3.send_games = false;
                tL_chatBannedRights3.send_gifs = false;
                tL_chatBannedRights3.send_stickers = false;
                tL_chatBannedRights3.embed_links = false;
                tL_chatBannedRights3.send_messages = false;
                tL_chatBannedRights3.send_media = false;
                tL_chatBannedRights3.view_messages = false;
            }
            TLRPC.TL_chatBannedRights tL_chatBannedRights4 = this.defaultBannedRights;
            if (tL_chatBannedRights4.change_info || this.isChannel) {
                z3 = true;
            } else {
                z3 = true;
                this.adminRights.change_info = true;
            }
            if (!tL_chatBannedRights4.pin_messages) {
                this.adminRights.pin_messages = z3;
            }
        } else if (i == 1) {
            this.defaultBannedRights = tL_chatBannedRights;
            if (tL_chatBannedRights == null) {
                TLRPC.TL_chatBannedRights tL_chatBannedRights5 = new TLRPC.TL_chatBannedRights();
                this.defaultBannedRights = tL_chatBannedRights5;
                tL_chatBannedRights5.send_roundvideos = false;
                tL_chatBannedRights5.send_voices = false;
                tL_chatBannedRights5.send_docs = false;
                tL_chatBannedRights5.send_audios = false;
                tL_chatBannedRights5.send_photos = false;
                tL_chatBannedRights5.send_videos = false;
                tL_chatBannedRights5.send_plain = false;
                tL_chatBannedRights5.manage_topics = false;
                tL_chatBannedRights5.pin_messages = false;
                tL_chatBannedRights5.change_info = false;
                tL_chatBannedRights5.invite_users = false;
                tL_chatBannedRights5.send_polls = false;
                tL_chatBannedRights5.send_inline = false;
                tL_chatBannedRights5.send_games = false;
                tL_chatBannedRights5.send_gifs = false;
                tL_chatBannedRights5.send_stickers = false;
                tL_chatBannedRights5.embed_links = false;
                tL_chatBannedRights5.send_messages = false;
                tL_chatBannedRights5.send_media = false;
                tL_chatBannedRights5.view_messages = false;
            }
            TLRPC.TL_chatBannedRights tL_chatBannedRights6 = new TLRPC.TL_chatBannedRights();
            this.bannedRights = tL_chatBannedRights6;
            if (tL_chatBannedRights2 == null) {
                tL_chatBannedRights6.manage_topics = false;
                tL_chatBannedRights6.pin_messages = false;
                tL_chatBannedRights6.change_info = false;
                tL_chatBannedRights6.invite_users = false;
                tL_chatBannedRights6.send_polls = false;
                tL_chatBannedRights6.send_inline = false;
                tL_chatBannedRights6.send_games = false;
                tL_chatBannedRights6.send_gifs = false;
                tL_chatBannedRights6.send_stickers = false;
                tL_chatBannedRights6.embed_links = false;
                tL_chatBannedRights6.send_messages = false;
                tL_chatBannedRights6.send_media = false;
                tL_chatBannedRights6.view_messages = false;
            } else {
                tL_chatBannedRights6.view_messages = tL_chatBannedRights2.view_messages;
                tL_chatBannedRights6.send_messages = tL_chatBannedRights2.send_messages;
                tL_chatBannedRights6.send_media = tL_chatBannedRights2.send_media;
                tL_chatBannedRights6.send_stickers = tL_chatBannedRights2.send_stickers;
                tL_chatBannedRights6.send_gifs = tL_chatBannedRights2.send_gifs;
                tL_chatBannedRights6.send_games = tL_chatBannedRights2.send_games;
                tL_chatBannedRights6.send_inline = tL_chatBannedRights2.send_inline;
                tL_chatBannedRights6.embed_links = tL_chatBannedRights2.embed_links;
                tL_chatBannedRights6.send_polls = tL_chatBannedRights2.send_polls;
                tL_chatBannedRights6.invite_users = tL_chatBannedRights2.invite_users;
                tL_chatBannedRights6.change_info = tL_chatBannedRights2.change_info;
                tL_chatBannedRights6.pin_messages = tL_chatBannedRights2.pin_messages;
                tL_chatBannedRights6.until_date = tL_chatBannedRights2.until_date;
                tL_chatBannedRights6.manage_topics = tL_chatBannedRights2.manage_topics;
                tL_chatBannedRights6.send_photos = tL_chatBannedRights2.send_photos;
                tL_chatBannedRights6.send_videos = tL_chatBannedRights2.send_videos;
                tL_chatBannedRights6.send_roundvideos = tL_chatBannedRights2.send_roundvideos;
                tL_chatBannedRights6.send_audios = tL_chatBannedRights2.send_audios;
                tL_chatBannedRights6.send_voices = tL_chatBannedRights2.send_voices;
                tL_chatBannedRights6.send_docs = tL_chatBannedRights2.send_docs;
                tL_chatBannedRights6.send_plain = tL_chatBannedRights2.send_plain;
            }
            TLRPC.TL_chatBannedRights tL_chatBannedRights7 = this.defaultBannedRights;
            if (tL_chatBannedRights7.view_messages) {
                tL_chatBannedRights6.view_messages = true;
            }
            if (tL_chatBannedRights7.send_messages) {
                tL_chatBannedRights6.send_messages = true;
            }
            if (tL_chatBannedRights7.send_media) {
                tL_chatBannedRights6.send_media = true;
            }
            if (tL_chatBannedRights7.send_stickers) {
                tL_chatBannedRights6.send_stickers = true;
            }
            if (tL_chatBannedRights7.send_gifs) {
                tL_chatBannedRights6.send_gifs = true;
            }
            if (tL_chatBannedRights7.send_games) {
                tL_chatBannedRights6.send_games = true;
            }
            if (tL_chatBannedRights7.send_inline) {
                tL_chatBannedRights6.send_inline = true;
            }
            if (tL_chatBannedRights7.embed_links) {
                tL_chatBannedRights6.embed_links = true;
            }
            if (tL_chatBannedRights7.send_polls) {
                tL_chatBannedRights6.send_polls = true;
            }
            if (tL_chatBannedRights7.invite_users) {
                tL_chatBannedRights6.invite_users = true;
            }
            if (tL_chatBannedRights7.change_info) {
                tL_chatBannedRights6.change_info = true;
            }
            if (tL_chatBannedRights7.pin_messages) {
                tL_chatBannedRights6.pin_messages = true;
            }
            if (tL_chatBannedRights7.manage_topics) {
                tL_chatBannedRights6.manage_topics = true;
            }
            if (tL_chatBannedRights7.send_photos) {
                tL_chatBannedRights6.send_photos = true;
            }
            if (tL_chatBannedRights7.send_videos) {
                tL_chatBannedRights6.send_videos = true;
            }
            if (tL_chatBannedRights7.send_audios) {
                tL_chatBannedRights6.send_audios = true;
            }
            if (tL_chatBannedRights7.send_docs) {
                tL_chatBannedRights6.send_docs = true;
            }
            if (tL_chatBannedRights7.send_voices) {
                tL_chatBannedRights6.send_voices = true;
            }
            if (tL_chatBannedRights7.send_roundvideos) {
                tL_chatBannedRights6.send_roundvideos = true;
            }
            if (tL_chatBannedRights7.send_plain) {
                tL_chatBannedRights6.send_plain = true;
            }
            this.currentBannedRights = ChatObject.getBannedRightsString(tL_chatBannedRights6);
            if (tL_chatBannedRights2 != null && tL_chatBannedRights2.view_messages) {
                z4 = false;
            }
            this.initialIsSet = z4;
        }
        updateRows(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean allDefaultMediaBanned() {
        TLRPC.TL_chatBannedRights tL_chatBannedRights = this.defaultBannedRights;
        return tL_chatBannedRights.send_photos && tL_chatBannedRights.send_videos && tL_chatBannedRights.send_stickers && tL_chatBannedRights.send_audios && tL_chatBannedRights.send_docs && tL_chatBannedRights.send_voices && tL_chatBannedRights.send_roundvideos && tL_chatBannedRights.embed_links && tL_chatBannedRights.send_polls;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        int i = this.currentType;
        if (i == 2) {
            return true;
        }
        if (!(!(i == 1 ? this.currentBannedRights.equals(ChatObject.getBannedRightsString(this.bannedRights)) : this.initialRank.equals(this.currentRank)))) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.UserRestrictionsApplyChanges));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("UserRestrictionsApplyChangesText", R.string.UserRestrictionsApplyChangesText, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chatId)).title)));
        builder.setPositiveButton(LocaleController.getString(R.string.ApplyTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                ChatRightsEditActivity.this.lambda$checkDiscard$23(dialogInterface, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                ChatRightsEditActivity.this.lambda$checkDiscard$24(dialogInterface, i2);
            }
        });
        showDialog(builder.create());
        return false;
    }

    public static TLRPC.TL_chatAdminRights emptyAdminRights(boolean z) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = new TLRPC.TL_chatAdminRights();
        tL_chatAdminRights.delete_stories = z;
        tL_chatAdminRights.edit_stories = z;
        tL_chatAdminRights.post_stories = z;
        tL_chatAdminRights.manage_topics = z;
        tL_chatAdminRights.manage_call = z;
        tL_chatAdminRights.add_admins = z;
        tL_chatAdminRights.pin_messages = z;
        tL_chatAdminRights.invite_users = z;
        tL_chatAdminRights.ban_users = z;
        tL_chatAdminRights.delete_messages = z;
        tL_chatAdminRights.edit_messages = z;
        tL_chatAdminRights.post_messages = z;
        tL_chatAdminRights.change_info = z;
        return tL_chatAdminRights;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r1v0, types: [boolean, int] */
    public int getChannelMessagesSelectedCount() {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
        ?? r1 = tL_chatAdminRights.post_messages;
        int i = r1;
        if (tL_chatAdminRights.edit_messages) {
            i = r1 + 1;
        }
        return tL_chatAdminRights.delete_messages ? i + 1 : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r1v0, types: [boolean, int] */
    public int getChannelStoriesSelectedCount() {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
        ?? r1 = tL_chatAdminRights.post_stories;
        int i = r1;
        if (tL_chatAdminRights.edit_stories) {
            i = r1 + 1;
        }
        return tL_chatAdminRights.delete_stories ? i + 1 : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSendMediaSelectedCount() {
        TLRPC.TL_chatBannedRights tL_chatBannedRights = this.bannedRights;
        int i = (tL_chatBannedRights.send_photos || this.defaultBannedRights.send_photos) ? 0 : 1;
        if (!tL_chatBannedRights.send_videos && !this.defaultBannedRights.send_videos) {
            i++;
        }
        if (!tL_chatBannedRights.send_stickers && !this.defaultBannedRights.send_stickers) {
            i++;
        }
        if (!tL_chatBannedRights.send_audios && !this.defaultBannedRights.send_audios) {
            i++;
        }
        if (!tL_chatBannedRights.send_docs && !this.defaultBannedRights.send_docs) {
            i++;
        }
        if (!tL_chatBannedRights.send_voices && !this.defaultBannedRights.send_voices) {
            i++;
        }
        if (!tL_chatBannedRights.send_roundvideos && !this.defaultBannedRights.send_roundvideos) {
            i++;
        }
        if (!tL_chatBannedRights.embed_links) {
            TLRPC.TL_chatBannedRights tL_chatBannedRights2 = this.defaultBannedRights;
            if (!tL_chatBannedRights2.embed_links && !tL_chatBannedRights.send_plain && !tL_chatBannedRights2.send_plain) {
                i++;
            }
        }
        return (tL_chatBannedRights.send_polls || this.defaultBannedRights.send_polls) ? i : i + 1;
    }

    private boolean hasAllAdminRights() {
        if (this.isChannel) {
            TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
            return tL_chatAdminRights.change_info && tL_chatAdminRights.post_messages && tL_chatAdminRights.edit_messages && tL_chatAdminRights.delete_messages && tL_chatAdminRights.invite_users && tL_chatAdminRights.add_admins && tL_chatAdminRights.manage_call && tL_chatAdminRights.post_stories && tL_chatAdminRights.edit_stories && tL_chatAdminRights.delete_stories;
        }
        TLRPC.TL_chatAdminRights tL_chatAdminRights2 = this.adminRights;
        if (tL_chatAdminRights2.change_info && tL_chatAdminRights2.delete_messages && tL_chatAdminRights2.ban_users && tL_chatAdminRights2.invite_users && tL_chatAdminRights2.pin_messages && tL_chatAdminRights2.add_admins && tL_chatAdminRights2.manage_call) {
            return !this.isForum || tL_chatAdminRights2.manage_topics;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initTransfer, reason: merged with bridge method [inline-methods] */
    public void lambda$initTransfer$8(final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final TwoStepVerificationActivity twoStepVerificationActivity) {
        if (getParentActivity() == null) {
            return;
        }
        if (inputCheckPasswordSRP != null && !ChatObject.isChannel(this.currentChat)) {
            MessagesController.getInstance(this.currentAccount).convertToMegaGroup(getParentActivity(), this.chatId, this, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda14
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    ChatRightsEditActivity.this.lambda$initTransfer$7(inputCheckPasswordSRP, twoStepVerificationActivity, j);
                }
            });
            return;
        }
        final TLRPC.TL_channels_editCreator tL_channels_editCreator = new TLRPC.TL_channels_editCreator();
        if (ChatObject.isChannel(this.currentChat)) {
            TLRPC.TL_inputChannel tL_inputChannel = new TLRPC.TL_inputChannel();
            tL_channels_editCreator.channel = tL_inputChannel;
            TLRPC.Chat chat = this.currentChat;
            tL_inputChannel.channel_id = chat.id;
            tL_inputChannel.access_hash = chat.access_hash;
        } else {
            tL_channels_editCreator.channel = new TLRPC.TL_inputChannelEmpty();
        }
        tL_channels_editCreator.password = inputCheckPasswordSRP != null ? inputCheckPasswordSRP : new TLRPC.TL_inputCheckPasswordEmpty();
        tL_channels_editCreator.user_id = getMessagesController().getInputUser(this.currentUser);
        getConnectionsManager().sendRequest(tL_channels_editCreator, new RequestDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ChatRightsEditActivity.this.lambda$initTransfer$14(inputCheckPasswordSRP, twoStepVerificationActivity, tL_channels_editCreator, tLObject, tL_error);
            }
        });
    }

    private boolean isDefaultAdminRights() {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
        boolean z = tL_chatAdminRights.change_info;
        return (z && tL_chatAdminRights.delete_messages && tL_chatAdminRights.ban_users && tL_chatAdminRights.invite_users && tL_chatAdminRights.pin_messages && ((!this.isForum || tL_chatAdminRights.manage_topics) && tL_chatAdminRights.manage_call && !tL_chatAdminRights.add_admins && !tL_chatAdminRights.anonymous)) || !(z || tL_chatAdminRights.delete_messages || tL_chatAdminRights.ban_users || tL_chatAdminRights.invite_users || tL_chatAdminRights.pin_messages || ((this.isForum && tL_chatAdminRights.manage_topics) || tL_chatAdminRights.manage_call || tL_chatAdminRights.add_admins || tL_chatAdminRights.anonymous));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isExpandableSendMediaRow(int i) {
        return i == this.sendStickersRow || i == this.embedLinksRow || i == this.sendPollsRow || i == this.sendPhotosRow || i == this.sendVideosRow || i == this.sendFilesRow || i == this.sendMusicRow || i == this.sendRoundRow || i == this.sendVoiceRow || i == this.channelPostMessagesRow || i == this.channelEditMessagesRow || i == this.channelDeleteMessagesRow || i == this.channelPostStoriesRow || i == this.channelEditStoriesRow || i == this.channelDeleteStoriesRow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$23(DialogInterface dialogInterface, int i) {
        onDonePressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$24(DialogInterface dialogInterface, int i) {
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(int i, TimePicker timePicker, int i2, int i3) {
        this.bannedRights.until_date = i + (i2 * 3600) + (i3 * 60);
        this.listViewAdapter.notifyItemChanged(this.untilDateRow);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$1(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(DatePicker datePicker, int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(i, i2, i3);
        final int time = (int) (calendar.getTime().getTime() / 1000);
        try {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getParentActivity(), new TimePickerDialog.OnTimeSetListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda19
                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public final void onTimeSet(TimePicker timePicker, int i4, int i5) {
                    ChatRightsEditActivity.this.lambda$createView$0(time, timePicker, i4, i5);
                }
            }, 0, 0, true);
            timePickerDialog.setButton(-1, LocaleController.getString(R.string.Set), timePickerDialog);
            timePickerDialog.setButton(-2, LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda20
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    ChatRightsEditActivity.lambda$createView$1(dialogInterface, i4);
                }
            });
            showDialog(timePickerDialog);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$3(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$4(DatePicker datePicker, DialogInterface dialogInterface) {
        int childCount = datePicker.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = datePicker.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            layoutParams.width = -1;
            childAt.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(BottomSheet.Builder builder, View view) {
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        int i;
        int currentTime;
        int i2;
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue != 0) {
            if (intValue == 1) {
                tL_chatBannedRights = this.bannedRights;
                currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                i2 = 86400;
            } else if (intValue == 2) {
                tL_chatBannedRights = this.bannedRights;
                currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                i2 = 604800;
            } else {
                if (intValue != 3) {
                    if (intValue == 4) {
                        Calendar calendar = Calendar.getInstance();
                        try {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getParentActivity(), new DatePickerDialog.OnDateSetListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda11
                                @Override // android.app.DatePickerDialog.OnDateSetListener
                                public final void onDateSet(DatePicker datePicker, int i3, int i4, int i5) {
                                    ChatRightsEditActivity.this.lambda$createView$2(datePicker, i3, i4, i5);
                                }
                            }, calendar.get(1), calendar.get(2), calendar.get(5));
                            final DatePicker datePicker = datePickerDialog.getDatePicker();
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(System.currentTimeMillis());
                            calendar2.set(11, calendar2.getMinimum(11));
                            calendar2.set(12, calendar2.getMinimum(12));
                            calendar2.set(13, calendar2.getMinimum(13));
                            calendar2.set(14, calendar2.getMinimum(14));
                            datePicker.setMinDate(calendar2.getTimeInMillis());
                            calendar2.setTimeInMillis(System.currentTimeMillis() + 31536000000L);
                            calendar2.set(11, calendar2.getMaximum(11));
                            calendar2.set(12, calendar2.getMaximum(12));
                            calendar2.set(13, calendar2.getMaximum(13));
                            calendar2.set(14, calendar2.getMaximum(14));
                            datePicker.setMaxDate(calendar2.getTimeInMillis());
                            datePickerDialog.setButton(-1, LocaleController.getString(R.string.Set), datePickerDialog);
                            datePickerDialog.setButton(-2, LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda12
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i3) {
                                    ChatRightsEditActivity.lambda$createView$3(dialogInterface, i3);
                                }
                            });
                            if (Build.VERSION.SDK_INT >= 21) {
                                datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda13
                                    @Override // android.content.DialogInterface.OnShowListener
                                    public final void onShow(DialogInterface dialogInterface) {
                                        ChatRightsEditActivity.lambda$createView$4(datePicker, dialogInterface);
                                    }
                                });
                            }
                            showDialog(datePickerDialog);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    builder.getDismissRunnable().run();
                }
                tL_chatBannedRights = this.bannedRights;
                currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                i2 = 2592000;
            }
            i = currentTime + i2;
        } else {
            tL_chatBannedRights = this.bannedRights;
            i = 0;
        }
        tL_chatBannedRights.until_date = i;
        this.listViewAdapter.notifyItemChanged(this.untilDateRow);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(Context context, View view, int i) {
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        TLRPC.TL_chatBannedRights tL_chatBannedRights2;
        boolean z;
        boolean z2;
        View findViewByPosition;
        int i2;
        String str;
        if (this.canEdit || (this.currentChat.creator && this.currentType == 0 && i == this.anonymousRow)) {
            boolean z3 = false;
            if (i == this.sendMediaRow) {
                if (!(view instanceof TextCheckCell2) || ((TextCheckCell2) view).isEnabled()) {
                    this.sendMediaExpanded = !this.sendMediaExpanded;
                    updateRows(false);
                    if (this.sendMediaExpanded) {
                        this.listViewAdapter.notifyItemRangeInserted(this.sendMediaRow + 1, 9);
                        return;
                    } else {
                        this.listViewAdapter.notifyItemRangeRemoved(this.sendMediaRow + 1, 9);
                        return;
                    }
                }
                return;
            }
            int i3 = this.channelMessagesRow;
            if (i == i3) {
                if (!(view instanceof TextCheckCell2) || ((TextCheckCell2) view).isEnabled()) {
                    this.channelMessagesExpanded = !this.channelMessagesExpanded;
                    updateRows(false);
                    this.listViewAdapter.notifyItemChanged(this.channelMessagesRow);
                    if (this.channelMessagesExpanded) {
                        this.listViewAdapter.notifyItemRangeInserted(this.channelMessagesRow + 1, 3);
                        return;
                    } else {
                        this.listViewAdapter.notifyItemRangeRemoved(this.channelMessagesRow + 1, 3);
                        return;
                    }
                }
                return;
            }
            int i4 = this.channelStoriesRow;
            if (i == i4) {
                if (!(view instanceof TextCheckCell2) || ((TextCheckCell2) view).isEnabled()) {
                    this.channelStoriesExpanded = !this.channelStoriesExpanded;
                    updateRows(false);
                    this.listViewAdapter.notifyItemChanged(this.channelStoriesRow);
                    if (this.channelStoriesExpanded) {
                        this.listViewAdapter.notifyItemRangeInserted(this.channelStoriesRow + 1, 3);
                        return;
                    } else {
                        this.listViewAdapter.notifyItemRangeRemoved(this.channelStoriesRow + 1, 3);
                        return;
                    }
                }
                return;
            }
            if (i == 0) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.currentUser.id);
                presentFragment(new ProfileActivity(bundle));
                return;
            }
            if (i == this.removeAdminRow) {
                int i5 = this.currentType;
                if (i5 == 0) {
                    MessagesController.getInstance(this.currentAccount).setUserAdminRole(this.chatId, this.currentUser, new TLRPC.TL_chatAdminRights(), this.currentRank, this.isChannel, getFragmentForAlert(0), this.isAddingNew, false, null, null);
                    ChatRightsEditActivityDelegate chatRightsEditActivityDelegate = this.delegate;
                    if (chatRightsEditActivityDelegate != null) {
                        chatRightsEditActivityDelegate.didSetRights(0, this.adminRights, this.bannedRights, this.currentRank);
                    }
                    lambda$onBackPressed$321();
                    return;
                }
                if (i5 == 1) {
                    this.banning = true;
                    TLRPC.TL_chatBannedRights tL_chatBannedRights3 = new TLRPC.TL_chatBannedRights();
                    this.bannedRights = tL_chatBannedRights3;
                    tL_chatBannedRights3.view_messages = true;
                    tL_chatBannedRights3.send_media = true;
                    tL_chatBannedRights3.send_messages = true;
                    tL_chatBannedRights3.send_stickers = true;
                    tL_chatBannedRights3.send_gifs = true;
                    tL_chatBannedRights3.send_games = true;
                    tL_chatBannedRights3.send_inline = true;
                    tL_chatBannedRights3.embed_links = true;
                    tL_chatBannedRights3.pin_messages = true;
                    tL_chatBannedRights3.send_polls = true;
                    tL_chatBannedRights3.invite_users = true;
                    tL_chatBannedRights3.change_info = true;
                    tL_chatBannedRights3.manage_topics = true;
                    tL_chatBannedRights3.until_date = 0;
                    onDonePressed();
                    return;
                }
                return;
            }
            if (i == this.transferOwnerRow) {
                lambda$initTransfer$8(null, null);
                return;
            }
            if (i == this.untilDateRow) {
                if (getParentActivity() == null) {
                    return;
                }
                final BottomSheet.Builder builder = new BottomSheet.Builder(context);
                builder.setApplyTopPadding(false);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(1);
                HeaderCell headerCell = new HeaderCell(context, Theme.key_dialogTextBlue2, 23, 15, false);
                headerCell.setHeight(47);
                headerCell.setText(LocaleController.getString(R.string.UserRestrictionsDuration));
                linearLayout.addView(headerCell);
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2));
                BottomSheet.BottomSheetCell[] bottomSheetCellArr = new BottomSheet.BottomSheetCell[5];
                int i6 = 0;
                for (int i7 = 5; i6 < i7; i7 = 5) {
                    BottomSheet.BottomSheetCell bottomSheetCell = new BottomSheet.BottomSheetCell(context, 0);
                    bottomSheetCellArr[i6] = bottomSheetCell;
                    bottomSheetCell.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
                    bottomSheetCellArr[i6].setTag(Integer.valueOf(i6));
                    bottomSheetCellArr[i6].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    if (i6 != 0) {
                        if (i6 == 1) {
                            str = LocaleController.formatPluralString("Days", 1, new Object[0]);
                        } else if (i6 == 2) {
                            str = LocaleController.formatPluralString("Weeks", 1, new Object[0]);
                        } else if (i6 != 3) {
                            i2 = R.string.UserRestrictionsCustom;
                        } else {
                            str = LocaleController.formatPluralString("Months", 1, new Object[0]);
                        }
                        bottomSheetCellArr[i6].setTextAndIcon(str, 0);
                        linearLayout2.addView(bottomSheetCellArr[i6], LayoutHelper.createLinear(-1, -2));
                        bottomSheetCellArr[i6].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ChatRightsEditActivity.this.lambda$createView$5(builder, view2);
                            }
                        });
                        i6++;
                    } else {
                        i2 = R.string.UserRestrictionsUntilForever;
                    }
                    str = LocaleController.getString(i2);
                    bottomSheetCellArr[i6].setTextAndIcon(str, 0);
                    linearLayout2.addView(bottomSheetCellArr[i6], LayoutHelper.createLinear(-1, -2));
                    bottomSheetCellArr[i6].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatRightsEditActivity.this.lambda$createView$5(builder, view2);
                        }
                    });
                    i6++;
                }
                builder.setCustomView(linearLayout);
                showDialog(builder.create());
                return;
            }
            if (view instanceof CheckBoxCell) {
                CheckBoxCell checkBoxCell = (CheckBoxCell) view;
                int i8 = this.channelPostMessagesRow;
                if (i == i8 || i == this.channelEditMessagesRow || i == this.channelDeleteMessagesRow) {
                    if (i == i8) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
                        z = !tL_chatAdminRights.post_messages;
                        tL_chatAdminRights.post_messages = z;
                    } else if (i == this.channelEditMessagesRow) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights2 = this.adminRights;
                        z = !tL_chatAdminRights2.edit_messages;
                        tL_chatAdminRights2.edit_messages = z;
                    } else {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights3 = this.adminRights;
                        z = !tL_chatAdminRights3.delete_messages;
                        tL_chatAdminRights3.delete_messages = z;
                    }
                    this.listViewAdapter.notifyItemChanged(i3);
                    checkBoxCell.setChecked(z, true);
                    return;
                }
                int i9 = this.channelPostStoriesRow;
                if (i == i9 || i == this.channelEditStoriesRow || i == this.channelDeleteStoriesRow) {
                    if (i == i9) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights4 = this.adminRights;
                        z2 = !tL_chatAdminRights4.post_stories;
                        tL_chatAdminRights4.post_stories = z2;
                    } else if (i == this.channelEditStoriesRow) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights5 = this.adminRights;
                        z2 = !tL_chatAdminRights5.edit_stories;
                        tL_chatAdminRights5.edit_stories = z2;
                    } else {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights6 = this.adminRights;
                        z2 = !tL_chatAdminRights6.delete_stories;
                        tL_chatAdminRights6.delete_stories = z2;
                    }
                    this.listViewAdapter.notifyItemChanged(i4);
                    checkBoxCell.setChecked(z2, true);
                    return;
                }
                if (this.currentType != 1 || this.bannedRights == null) {
                    return;
                }
                checkBoxCell.isChecked();
                if (checkBoxCell.hasIcon()) {
                    if (this.currentType != 2) {
                        new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.UserRestrictionsCantModify)).setMessage(LocaleController.getString(R.string.UserRestrictionsCantModifyDisabled)).setPositiveButton(LocaleController.getString(R.string.OK), null).create().show();
                        return;
                    }
                    return;
                }
                if (i == this.sendPhotosRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights4 = this.bannedRights;
                    z3 = !tL_chatBannedRights4.send_photos;
                    tL_chatBannedRights4.send_photos = z3;
                } else if (i == this.sendVideosRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights5 = this.bannedRights;
                    z3 = !tL_chatBannedRights5.send_videos;
                    tL_chatBannedRights5.send_videos = z3;
                } else if (i == this.sendMusicRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights6 = this.bannedRights;
                    z3 = !tL_chatBannedRights6.send_audios;
                    tL_chatBannedRights6.send_audios = z3;
                } else if (i == this.sendFilesRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights7 = this.bannedRights;
                    z3 = !tL_chatBannedRights7.send_docs;
                    tL_chatBannedRights7.send_docs = z3;
                } else if (i == this.sendRoundRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights8 = this.bannedRights;
                    z3 = !tL_chatBannedRights8.send_roundvideos;
                    tL_chatBannedRights8.send_roundvideos = z3;
                } else if (i == this.sendVoiceRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights9 = this.bannedRights;
                    z3 = !tL_chatBannedRights9.send_voices;
                    tL_chatBannedRights9.send_voices = z3;
                } else if (i == this.sendStickersRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights10 = this.bannedRights;
                    z3 = !tL_chatBannedRights10.send_stickers;
                    tL_chatBannedRights10.send_inline = z3;
                    tL_chatBannedRights10.send_gifs = z3;
                    tL_chatBannedRights10.send_games = z3;
                    tL_chatBannedRights10.send_stickers = z3;
                } else if (i == this.embedLinksRow) {
                    if ((this.bannedRights.send_plain || this.defaultBannedRights.send_plain) && (findViewByPosition = this.linearLayoutManager.findViewByPosition(this.sendMessagesRow)) != null) {
                        AndroidUtilities.shakeViewSpring(findViewByPosition);
                        BotWebViewVibrationEffect.APP_ERROR.vibrate();
                        return;
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights11 = this.bannedRights;
                        z3 = !tL_chatBannedRights11.embed_links;
                        tL_chatBannedRights11.embed_links = z3;
                    }
                } else if (i == this.sendPollsRow) {
                    TLRPC.TL_chatBannedRights tL_chatBannedRights12 = this.bannedRights;
                    z3 = !tL_chatBannedRights12.send_polls;
                    tL_chatBannedRights12.send_polls = z3;
                }
                this.listViewAdapter.notifyItemChanged(this.sendMediaRow);
                checkBoxCell.setChecked(!z3, true);
                return;
            }
            if (view instanceof TextCheckCell2) {
                TextCheckCell2 textCheckCell2 = (TextCheckCell2) view;
                if (textCheckCell2.hasIcon()) {
                    if (this.currentType != 2) {
                        new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.UserRestrictionsCantModify)).setMessage(LocaleController.getString(R.string.UserRestrictionsCantModifyDisabled)).setPositiveButton(LocaleController.getString(R.string.OK), null).create().show();
                        return;
                    }
                    return;
                }
                if (!textCheckCell2.isEnabled()) {
                    int i10 = this.currentType;
                    if (i10 == 2 || i10 == 0) {
                        if ((i != this.changeInfoRow || (tL_chatBannedRights2 = this.defaultBannedRights) == null || tL_chatBannedRights2.change_info) && (i != this.pinMessagesRow || (tL_chatBannedRights = this.defaultBannedRights) == null || tL_chatBannedRights.pin_messages)) {
                            return;
                        }
                        new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.UserRestrictionsCantModify)).setMessage(LocaleController.getString(R.string.UserRestrictionsCantModifyEnabled)).setPositiveButton(LocaleController.getString(R.string.OK), null).create().show();
                        return;
                    }
                    return;
                }
                if (this.currentType != 2) {
                    textCheckCell2.setChecked(!textCheckCell2.isChecked());
                }
                boolean isChecked = textCheckCell2.isChecked();
                if (i == this.manageRow) {
                    isChecked = !this.asAdmin;
                    this.asAdmin = isChecked;
                    updateAsAdmin(true);
                } else if (i == this.changeInfoRow) {
                    int i11 = this.currentType;
                    if (i11 == 0 || i11 == 2) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights7 = this.adminRights;
                        isChecked = !tL_chatAdminRights7.change_info;
                        tL_chatAdminRights7.change_info = isChecked;
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights13 = this.bannedRights;
                        isChecked = !tL_chatBannedRights13.change_info;
                        tL_chatBannedRights13.change_info = isChecked;
                    }
                } else if (i == this.postMessagesRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights8 = this.adminRights;
                    isChecked = !tL_chatAdminRights8.post_messages;
                    tL_chatAdminRights8.post_messages = isChecked;
                } else if (i == this.editMesagesRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights9 = this.adminRights;
                    isChecked = !tL_chatAdminRights9.edit_messages;
                    tL_chatAdminRights9.edit_messages = isChecked;
                } else if (i == this.deleteMessagesRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights10 = this.adminRights;
                    isChecked = !tL_chatAdminRights10.delete_messages;
                    tL_chatAdminRights10.delete_messages = isChecked;
                } else if (i == this.addAdminsRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights11 = this.adminRights;
                    isChecked = !tL_chatAdminRights11.add_admins;
                    tL_chatAdminRights11.add_admins = isChecked;
                } else if (i == this.anonymousRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights12 = this.adminRights;
                    isChecked = !tL_chatAdminRights12.anonymous;
                    tL_chatAdminRights12.anonymous = isChecked;
                } else if (i == this.banUsersRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights13 = this.adminRights;
                    isChecked = !tL_chatAdminRights13.ban_users;
                    tL_chatAdminRights13.ban_users = isChecked;
                } else if (i == this.startVoiceChatRow) {
                    TLRPC.TL_chatAdminRights tL_chatAdminRights14 = this.adminRights;
                    isChecked = !tL_chatAdminRights14.manage_call;
                    tL_chatAdminRights14.manage_call = isChecked;
                } else if (i == this.manageTopicsRow) {
                    int i12 = this.currentType;
                    if (i12 == 0 || i12 == 2) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights15 = this.adminRights;
                        isChecked = !tL_chatAdminRights15.manage_topics;
                        tL_chatAdminRights15.manage_topics = isChecked;
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights14 = this.bannedRights;
                        isChecked = !tL_chatBannedRights14.manage_topics;
                        tL_chatBannedRights14.manage_topics = isChecked;
                    }
                } else if (i == this.addUsersRow) {
                    int i13 = this.currentType;
                    if (i13 == 0 || i13 == 2) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights16 = this.adminRights;
                        isChecked = !tL_chatAdminRights16.invite_users;
                        tL_chatAdminRights16.invite_users = isChecked;
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights15 = this.bannedRights;
                        isChecked = !tL_chatBannedRights15.invite_users;
                        tL_chatBannedRights15.invite_users = isChecked;
                    }
                } else if (i == this.pinMessagesRow) {
                    int i14 = this.currentType;
                    if (i14 == 0 || i14 == 2) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights17 = this.adminRights;
                        isChecked = !tL_chatAdminRights17.pin_messages;
                        tL_chatAdminRights17.pin_messages = isChecked;
                    } else {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights16 = this.bannedRights;
                        isChecked = !tL_chatBannedRights16.pin_messages;
                        tL_chatBannedRights16.pin_messages = isChecked;
                    }
                } else if (this.currentType == 1 && this.bannedRights != null) {
                    boolean z4 = !textCheckCell2.isChecked();
                    if (i == this.sendMessagesRow) {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights17 = this.bannedRights;
                        isChecked = !tL_chatBannedRights17.send_plain;
                        tL_chatBannedRights17.send_plain = isChecked;
                    }
                    if (!z4) {
                        TLRPC.TL_chatBannedRights tL_chatBannedRights18 = this.bannedRights;
                        if ((!tL_chatBannedRights18.send_plain || !tL_chatBannedRights18.embed_links || !tL_chatBannedRights18.send_inline || !tL_chatBannedRights18.send_photos || !tL_chatBannedRights18.send_videos || !tL_chatBannedRights18.send_audios || !tL_chatBannedRights18.send_docs || !tL_chatBannedRights18.send_voices || !tL_chatBannedRights18.send_roundvideos || !tL_chatBannedRights18.send_polls) && tL_chatBannedRights18.view_messages) {
                            tL_chatBannedRights18.view_messages = false;
                        }
                    }
                    int i15 = this.embedLinksRow;
                    if (i15 >= 0) {
                        this.listViewAdapter.notifyItemChanged(i15);
                    }
                    int i16 = this.sendMediaRow;
                    if (i16 >= 0) {
                        this.listViewAdapter.notifyItemChanged(i16);
                    }
                }
                if (this.currentType == 2) {
                    if (this.asAdmin && isChecked) {
                        z3 = true;
                    }
                    textCheckCell2.setChecked(z3);
                }
                updateRows(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$26() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell2) {
                    ((UserCell2) childAt).update(0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$10(DialogInterface dialogInterface, int i) {
        presentFragment(new TwoStepVerificationSetupActivity(6, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$11(TLRPC.TL_error tL_error, TLObject tLObject, TwoStepVerificationActivity twoStepVerificationActivity) {
        if (tL_error == null) {
            TLRPC.account_Password account_password = (TLRPC.account_Password) tLObject;
            twoStepVerificationActivity.setCurrentPasswordInfo(null, account_password);
            TwoStepVerificationActivity.initPasswordNewAlgo(account_password);
            lambda$initTransfer$8(twoStepVerificationActivity.getNewSrpPassword(), twoStepVerificationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$12(final TwoStepVerificationActivity twoStepVerificationActivity, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                ChatRightsEditActivity.this.lambda$initTransfer$11(tL_error, tLObject, twoStepVerificationActivity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$13(TLRPC.TL_error tL_error, TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final TwoStepVerificationActivity twoStepVerificationActivity, TLRPC.TL_channels_editCreator tL_channels_editCreator) {
        int i;
        AlertDialog create;
        if (tL_error == null) {
            if (inputCheckPasswordSRP != null) {
                this.delegate.didChangeOwner(this.currentUser);
                removeSelfFromStack();
                twoStepVerificationActivity.needHideProgress();
                twoStepVerificationActivity.lambda$onBackPressed$321();
                return;
            }
            return;
        }
        if (getParentActivity() == null) {
            return;
        }
        if ("PASSWORD_HASH_INVALID".equals(tL_error.text)) {
            if (inputCheckPasswordSRP != null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(this.isChannel ? R.string.EditAdminChannelTransfer : R.string.EditAdminGroupTransfer));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferReadyAlertText", R.string.EditAdminTransferReadyAlertText, this.currentChat.title, UserObject.getFirstName(this.currentUser))));
            builder.setPositiveButton(LocaleController.getString(R.string.EditAdminTransferChangeOwner), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda22
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    ChatRightsEditActivity.this.lambda$initTransfer$9(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            create = builder.create();
        } else {
            if (!"PASSWORD_MISSING".equals(tL_error.text) && !tL_error.text.startsWith("PASSWORD_TOO_FRESH_") && !tL_error.text.startsWith("SESSION_TOO_FRESH_")) {
                if ("SRP_ID_INVALID".equals(tL_error.text)) {
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda23
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error2) {
                            ChatRightsEditActivity.this.lambda$initTransfer$12(twoStepVerificationActivity, tLObject, tL_error2);
                        }
                    }, 8);
                    return;
                }
                if (!tL_error.text.equals("CHANNELS_TOO_MUCH")) {
                    if (twoStepVerificationActivity != null) {
                        twoStepVerificationActivity.needHideProgress();
                        twoStepVerificationActivity.lambda$onBackPressed$321();
                    }
                    AlertsCreator.showAddUserAlert(tL_error.text, this, this.isChannel, tL_channels_editCreator);
                    return;
                }
                if (getParentActivity() == null || AccountInstance.getInstance(this.currentAccount).getUserConfig().isPremium()) {
                    presentFragment(new TooManyCommunitiesActivity(1));
                    return;
                } else {
                    showDialog(new LimitReachedBottomSheet(this, getParentActivity(), 5, this.currentAccount, null));
                    return;
                }
            }
            if (twoStepVerificationActivity != null) {
                twoStepVerificationActivity.needHideProgress();
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString(R.string.EditAdminTransferAlertTitle));
            LinearLayout linearLayout = new LinearLayout(getParentActivity());
            linearLayout.setPadding(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(24.0f), 0);
            linearLayout.setOrientation(1);
            builder2.setView(linearLayout);
            TextView textView = new TextView(getParentActivity());
            int i2 = Theme.key_dialogTextBlack;
            textView.setTextColor(Theme.getColor(i2));
            textView.setTextSize(1, 16.0f);
            textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView.setText(AndroidUtilities.replaceTags(this.isChannel ? LocaleController.formatString("EditChannelAdminTransferAlertText", R.string.EditChannelAdminTransferAlertText, UserObject.getFirstName(this.currentUser)) : LocaleController.formatString("EditAdminTransferAlertText", R.string.EditAdminTransferAlertText, UserObject.getFirstName(this.currentUser))));
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout2 = new LinearLayout(getParentActivity());
            linearLayout2.setOrientation(0);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView = new ImageView(getParentActivity());
            int i3 = R.drawable.list_circle;
            imageView.setImageResource(i3);
            imageView.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
            int color = Theme.getColor(i2);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            imageView.setColorFilter(new PorterDuffColorFilter(color, mode));
            TextView textView2 = new TextView(getParentActivity());
            textView2.setTextColor(Theme.getColor(i2));
            textView2.setTextSize(1, 16.0f);
            textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText1)));
            if (LocaleController.isRTL) {
                linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
                linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 5));
            } else {
                linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2));
                linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
            }
            LinearLayout linearLayout3 = new LinearLayout(getParentActivity());
            linearLayout3.setOrientation(0);
            linearLayout.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(getParentActivity());
            imageView2.setImageResource(i3);
            imageView2.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
            imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i2), mode));
            TextView textView3 = new TextView(getParentActivity());
            textView3.setTextColor(Theme.getColor(i2));
            textView3.setTextSize(1, 16.0f);
            textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView3.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText2)));
            if (LocaleController.isRTL) {
                linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
                linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2, 5));
            } else {
                linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2));
                linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
            }
            if ("PASSWORD_MISSING".equals(tL_error.text)) {
                builder2.setPositiveButton(LocaleController.getString(R.string.EditAdminTransferSetPassword), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda24
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        ChatRightsEditActivity.this.lambda$initTransfer$10(dialogInterface, i4);
                    }
                });
                i = R.string.Cancel;
            } else {
                TextView textView4 = new TextView(getParentActivity());
                textView4.setTextColor(Theme.getColor(i2));
                textView4.setTextSize(1, 16.0f);
                textView4.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                textView4.setText(LocaleController.getString(R.string.EditAdminTransferAlertText3));
                linearLayout.addView(textView4, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
                i = R.string.OK;
            }
            builder2.setNegativeButton(LocaleController.getString(i), null);
            create = builder2.create();
        }
        showDialog(create);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$14(final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final TwoStepVerificationActivity twoStepVerificationActivity, final TLRPC.TL_channels_editCreator tL_channels_editCreator, TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                ChatRightsEditActivity.this.lambda$initTransfer$13(tL_error, inputCheckPasswordSRP, twoStepVerificationActivity, tL_channels_editCreator);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$7(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, TwoStepVerificationActivity twoStepVerificationActivity, long j) {
        if (j != 0) {
            this.chatId = j;
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
            lambda$initTransfer$8(inputCheckPasswordSRP, twoStepVerificationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTransfer$9(DialogInterface dialogInterface, int i) {
        final TwoStepVerificationActivity twoStepVerificationActivity = new TwoStepVerificationActivity();
        twoStepVerificationActivity.setDelegate(0, new TwoStepVerificationActivity.TwoStepVerificationActivityDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda25
            @Override // org.telegram.ui.TwoStepVerificationActivity.TwoStepVerificationActivityDelegate
            public final void didEnterPassword(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP) {
                ChatRightsEditActivity.this.lambda$initTransfer$8(twoStepVerificationActivity, inputCheckPasswordSRP);
            }
        });
        presentFragment(twoStepVerificationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDonePressed$15(long j) {
        if (j != 0) {
            this.chatId = j;
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
            onDonePressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDonePressed$16() {
        ChatRightsEditActivityDelegate chatRightsEditActivityDelegate = this.delegate;
        if (chatRightsEditActivityDelegate != null) {
            TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
            chatRightsEditActivityDelegate.didSetRights((tL_chatAdminRights.change_info || tL_chatAdminRights.post_messages || tL_chatAdminRights.edit_messages || tL_chatAdminRights.delete_messages || tL_chatAdminRights.ban_users || tL_chatAdminRights.invite_users || (this.isForum && tL_chatAdminRights.manage_topics) || tL_chatAdminRights.pin_messages || tL_chatAdminRights.add_admins || tL_chatAdminRights.anonymous || tL_chatAdminRights.manage_call || ((this.isChannel && (tL_chatAdminRights.post_stories || tL_chatAdminRights.edit_stories || tL_chatAdminRights.delete_stories)) || tL_chatAdminRights.other)) ? 1 : 0, tL_chatAdminRights, this.bannedRights, this.currentRank);
            lambda$onBackPressed$321();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDonePressed$17(TLRPC.TL_error tL_error) {
        setLoading(false);
        if (tL_error == null || !"USER_PRIVACY_RESTRICTED".equals(tL_error.text)) {
            return true;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getParentActivity(), 11, this.currentAccount, getResourceProvider());
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.currentUser);
        limitReachedBottomSheet.setRestrictedUsers(this.currentChat, arrayList, null, null);
        limitReachedBottomSheet.show();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDonePressed$18() {
        Bulletin createPromoteToAdminBulletin;
        ChatRightsEditActivityDelegate chatRightsEditActivityDelegate = this.delegate;
        if (chatRightsEditActivityDelegate != null) {
            chatRightsEditActivityDelegate.didSetRights(0, this.asAdmin ? this.adminRights : null, null, this.currentRank);
        }
        this.closingKeyboardAfterFinish = true;
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", this.currentChat.id);
        if (!getMessagesController().checkCanOpenChat(bundle, this)) {
            setLoading(false);
            return;
        }
        ChatActivity chatActivity = new ChatActivity(bundle);
        presentFragment(chatActivity, true);
        if (BulletinFactory.canShowBulletin(chatActivity)) {
            boolean z = this.isAddingNew;
            if (z && this.asAdmin) {
                createPromoteToAdminBulletin = BulletinFactory.createAddedAsAdminBulletin(chatActivity, this.currentUser.first_name);
            } else if (z || this.initialAsAdmin || !this.asAdmin) {
                return;
            } else {
                createPromoteToAdminBulletin = BulletinFactory.createPromoteToAdminBulletin(chatActivity, this.currentUser.first_name);
            }
            createPromoteToAdminBulletin.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDonePressed$19(TLRPC.TL_error tL_error) {
        setLoading(false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDonePressed$20(TLRPC.TL_error tL_error) {
        setLoading(false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDonePressed$21(DialogInterface dialogInterface, int i) {
        setLoading(true);
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                ChatRightsEditActivity.this.lambda$onDonePressed$18();
            }
        };
        if (this.asAdmin || this.initialAsAdmin) {
            getMessagesController().setUserAdminRole(this.currentChat.id, this.currentUser, this.asAdmin ? this.adminRights : emptyAdminRights(false), this.currentRank, false, this, this.isAddingNew, this.asAdmin, this.botHash, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda18
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC.TL_error tL_error) {
                    boolean lambda$onDonePressed$19;
                    lambda$onDonePressed$19 = ChatRightsEditActivity.this.lambda$onDonePressed$19(tL_error);
                    return lambda$onDonePressed$19;
                }
            });
        } else {
            getMessagesController().addUserToChat(this.currentChat.id, this.currentUser, 0, this.botHash, this, true, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda17
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC.TL_error tL_error) {
                    boolean lambda$onDonePressed$20;
                    lambda$onDonePressed$20 = ChatRightsEditActivity.this.lambda$onDonePressed$20(tL_error);
                    return lambda$onDonePressed$20;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLoading$22(ValueAnimator valueAnimator) {
        this.doneDrawable.setProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
        this.doneDrawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAsAdmin$25(ValueAnimator valueAnimator) {
        this.asAdminT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        FrameLayout frameLayout = this.addBotButton;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002d, code lost:
    
        if (r3.codePointCount(0, r3.length()) <= 16) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003b, code lost:
    
        if (isDefaultAdminRights() == false) goto L22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDonePressed() {
        int i = 2;
        if (this.loading) {
            return;
        }
        if (!ChatObject.isChannel(this.currentChat)) {
            int i2 = this.currentType;
            if (i2 != 1) {
                if (i2 == 0) {
                    if (isDefaultAdminRights()) {
                        if (this.rankRow != -1) {
                            String str = this.currentRank;
                        }
                    }
                }
                if (this.currentType == 2) {
                    if (this.currentRank == null) {
                    }
                }
            }
            MessagesController.getInstance(this.currentAccount).convertToMegaGroup(getParentActivity(), this.chatId, this, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda5
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    ChatRightsEditActivity.this.lambda$onDonePressed$15(j);
                }
            });
            return;
        }
        int i3 = this.currentType;
        if (i3 == 0 || i3 == 2) {
            if (this.rankRow != -1) {
                String str2 = this.currentRank;
                if (str2.codePointCount(0, str2.length()) > 16) {
                    this.listView.smoothScrollToPosition(this.rankRow);
                    Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(200L);
                    }
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.rankHeaderRow);
                    if (findViewHolderForAdapterPosition != null) {
                        AndroidUtilities.shakeView(findViewHolderForAdapterPosition.itemView);
                        return;
                    }
                    return;
                }
            }
            boolean z = this.isChannel;
            TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
            if (z) {
                tL_chatAdminRights.ban_users = false;
                tL_chatAdminRights.pin_messages = false;
            } else {
                tL_chatAdminRights.edit_messages = false;
                tL_chatAdminRights.post_messages = false;
            }
            TLRPC.TL_chatAdminRights tL_chatAdminRights2 = this.adminRights;
            if (tL_chatAdminRights2.change_info || tL_chatAdminRights2.post_messages || tL_chatAdminRights2.edit_messages || tL_chatAdminRights2.delete_messages || tL_chatAdminRights2.ban_users || tL_chatAdminRights2.invite_users || ((this.isForum && tL_chatAdminRights2.manage_topics) || tL_chatAdminRights2.pin_messages || tL_chatAdminRights2.add_admins || tL_chatAdminRights2.anonymous || tL_chatAdminRights2.manage_call || (z && (tL_chatAdminRights2.post_stories || tL_chatAdminRights2.edit_stories || tL_chatAdminRights2.delete_stories)))) {
                tL_chatAdminRights2.other = false;
            } else {
                tL_chatAdminRights2.other = true;
            }
        }
        int i4 = this.currentType;
        if (i4 == 0) {
            r1 = this.delegate == null;
            setLoading(true);
            MessagesController.getInstance(this.currentAccount).setUserAdminRole(this.chatId, this.currentUser, this.adminRights, this.currentRank, this.isChannel, this, this.isAddingNew, false, null, new Runnable() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ChatRightsEditActivity.this.lambda$onDonePressed$16();
                }
            }, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC.TL_error tL_error) {
                    boolean lambda$onDonePressed$17;
                    lambda$onDonePressed$17 = ChatRightsEditActivity.this.lambda$onDonePressed$17(tL_error);
                    return lambda$onDonePressed$17;
                }
            });
        } else {
            if (i4 == 1) {
                MessagesController.getInstance(this.currentAccount).setParticipantBannedRole(this.chatId, this.currentUser, null, this.bannedRights, this.isChannel, getFragmentForAlert(1));
                TLRPC.TL_chatBannedRights tL_chatBannedRights = this.bannedRights;
                if (tL_chatBannedRights.send_messages || tL_chatBannedRights.send_stickers || tL_chatBannedRights.embed_links || tL_chatBannedRights.send_media || tL_chatBannedRights.send_gifs || tL_chatBannedRights.send_games || tL_chatBannedRights.send_inline) {
                    i = 1;
                } else {
                    tL_chatBannedRights.until_date = 0;
                }
                ChatRightsEditActivityDelegate chatRightsEditActivityDelegate = this.delegate;
                if (chatRightsEditActivityDelegate != null) {
                    chatRightsEditActivityDelegate.didSetRights(i, this.adminRights, tL_chatBannedRights, this.currentRank);
                }
            } else if (i4 == 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString(this.asAdmin ? R.string.AddBotAdmin : R.string.AddBot));
                boolean z2 = ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup;
                TLRPC.Chat chat = this.currentChat;
                String str3 = chat == null ? "" : chat.title;
                builder.setMessage(AndroidUtilities.replaceTags(this.asAdmin ? z2 ? LocaleController.formatString("AddBotMessageAdminChannel", R.string.AddBotMessageAdminChannel, str3) : LocaleController.formatString("AddBotMessageAdminGroup", R.string.AddBotMessageAdminGroup, str3) : LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(this.currentUser), str3)));
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                builder.setPositiveButton(LocaleController.getString(this.asAdmin ? R.string.AddAsAdmin : R.string.AddBot), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda8
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        ChatRightsEditActivity.this.lambda$onDonePressed$21(dialogInterface, i5);
                    }
                });
                showDialog(builder.create());
            }
            r1 = true;
        }
        if (r1) {
            lambda$onBackPressed$321();
        }
    }

    public static TLRPC.TL_chatAdminRights rightsOR(TLRPC.TL_chatAdminRights tL_chatAdminRights, TLRPC.TL_chatAdminRights tL_chatAdminRights2) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights3 = new TLRPC.TL_chatAdminRights();
        boolean z = true;
        tL_chatAdminRights3.change_info = tL_chatAdminRights.change_info || tL_chatAdminRights2.change_info;
        tL_chatAdminRights3.post_messages = tL_chatAdminRights.post_messages || tL_chatAdminRights2.post_messages;
        tL_chatAdminRights3.edit_messages = tL_chatAdminRights.edit_messages || tL_chatAdminRights2.edit_messages;
        tL_chatAdminRights3.delete_messages = tL_chatAdminRights.delete_messages || tL_chatAdminRights2.delete_messages;
        tL_chatAdminRights3.ban_users = tL_chatAdminRights.ban_users || tL_chatAdminRights2.ban_users;
        tL_chatAdminRights3.invite_users = tL_chatAdminRights.invite_users || tL_chatAdminRights2.invite_users;
        tL_chatAdminRights3.pin_messages = tL_chatAdminRights.pin_messages || tL_chatAdminRights2.pin_messages;
        tL_chatAdminRights3.add_admins = tL_chatAdminRights.add_admins || tL_chatAdminRights2.add_admins;
        tL_chatAdminRights3.manage_call = tL_chatAdminRights.manage_call || tL_chatAdminRights2.manage_call;
        tL_chatAdminRights3.manage_topics = tL_chatAdminRights.manage_topics || tL_chatAdminRights2.manage_topics;
        tL_chatAdminRights3.post_stories = tL_chatAdminRights.post_stories || tL_chatAdminRights2.post_stories;
        tL_chatAdminRights3.edit_stories = tL_chatAdminRights.edit_stories || tL_chatAdminRights2.edit_stories;
        if (!tL_chatAdminRights.delete_stories && !tL_chatAdminRights2.delete_stories) {
            z = false;
        }
        tL_chatAdminRights3.delete_stories = z;
        return tL_chatAdminRights3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChannelMessagesEnabled(boolean z) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
        boolean z2 = !z;
        tL_chatAdminRights.post_messages = z2;
        tL_chatAdminRights.edit_messages = z2;
        tL_chatAdminRights.delete_messages = z2;
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChannelStoriesEnabled(boolean z) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights = this.adminRights;
        boolean z2 = !z;
        tL_chatAdminRights.post_stories = z2;
        tL_chatAdminRights.edit_stories = z2;
        tL_chatAdminRights.delete_stories = z2;
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSendMediaEnabled(boolean z) {
        TLRPC.TL_chatBannedRights tL_chatBannedRights = this.bannedRights;
        boolean z2 = !z;
        tL_chatBannedRights.send_media = z2;
        tL_chatBannedRights.send_photos = z2;
        tL_chatBannedRights.send_videos = z2;
        tL_chatBannedRights.send_stickers = z2;
        tL_chatBannedRights.send_gifs = z2;
        tL_chatBannedRights.send_games = z2;
        tL_chatBannedRights.send_inline = z2;
        tL_chatBannedRights.send_audios = z2;
        tL_chatBannedRights.send_docs = z2;
        tL_chatBannedRights.send_voices = z2;
        tL_chatBannedRights.send_roundvideos = z2;
        tL_chatBannedRights.embed_links = z2;
        tL_chatBannedRights.send_polls = z2;
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextLeft(View view) {
        if (view instanceof HeaderCell) {
            HeaderCell headerCell = (HeaderCell) view;
            String str = this.currentRank;
            int codePointCount = 16 - (str != null ? str.codePointCount(0, str.length()) : 0);
            if (codePointCount > 4.8f) {
                headerCell.setText2("");
                return;
            }
            headerCell.setText2(String.format("%d", Integer.valueOf(codePointCount)));
            SimpleTextView textView2 = headerCell.getTextView2();
            int i = codePointCount < 0 ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayText3;
            textView2.setTextColor(Theme.getColor(i));
            textView2.setTag(Integer.valueOf(i));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0065, code lost:
    
        if (r5.creator == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x007b, code lost:
    
        if (r8.defaultBannedRights.change_info != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00d6, code lost:
    
        if (r8.defaultBannedRights.pin_messages != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0107, code lost:
    
        if (r5.creator == false) goto L78;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAsAdmin(boolean z) {
        boolean z2;
        boolean z3;
        FrameLayout frameLayout = this.addBotButton;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
            if (childAt instanceof TextCheckCell2) {
                boolean z4 = this.asAdmin;
                if (z4) {
                    if (childAdapterPosition == this.manageRow) {
                        if (!this.myAdminRights.add_admins) {
                            TLRPC.Chat chat = this.currentChat;
                            if (chat != null) {
                            }
                            z2 = z4;
                            z3 = false;
                            TextCheckCell2 textCheckCell2 = (TextCheckCell2) childAt;
                            textCheckCell2.setChecked(z2);
                            textCheckCell2.setEnabled(z3, z);
                        }
                        z2 = z4;
                        z3 = true;
                        TextCheckCell2 textCheckCell22 = (TextCheckCell2) childAt;
                        textCheckCell22.setChecked(z2);
                        textCheckCell22.setEnabled(z3, z);
                    } else if (childAdapterPosition == this.changeInfoRow) {
                        z4 = this.adminRights.change_info;
                        if (this.myAdminRights.change_info) {
                        }
                        z2 = z4;
                        z3 = false;
                        TextCheckCell2 textCheckCell222 = (TextCheckCell2) childAt;
                        textCheckCell222.setChecked(z2);
                        textCheckCell222.setEnabled(z3, z);
                    } else {
                        if (childAdapterPosition == this.postMessagesRow) {
                            z2 = this.adminRights.post_messages;
                            z3 = this.myAdminRights.post_messages;
                        } else if (childAdapterPosition == this.editMesagesRow) {
                            z2 = this.adminRights.edit_messages;
                            z3 = this.myAdminRights.edit_messages;
                        } else if (childAdapterPosition == this.deleteMessagesRow) {
                            z2 = this.adminRights.delete_messages;
                            z3 = this.myAdminRights.delete_messages;
                        } else if (childAdapterPosition == this.banUsersRow) {
                            z2 = this.adminRights.ban_users;
                            z3 = this.myAdminRights.ban_users;
                        } else if (childAdapterPosition == this.addUsersRow) {
                            z2 = this.adminRights.invite_users;
                            z3 = this.myAdminRights.invite_users;
                        } else if (childAdapterPosition == this.pinMessagesRow) {
                            z4 = this.adminRights.pin_messages;
                            if (this.myAdminRights.pin_messages) {
                            }
                            z2 = z4;
                            z3 = false;
                        } else if (childAdapterPosition == this.startVoiceChatRow) {
                            z2 = this.adminRights.manage_call;
                            z3 = this.myAdminRights.manage_call;
                        } else if (childAdapterPosition == this.addAdminsRow) {
                            z2 = this.adminRights.add_admins;
                            z3 = this.myAdminRights.add_admins;
                        } else if (childAdapterPosition == this.anonymousRow) {
                            z4 = this.adminRights.anonymous;
                            if (!this.myAdminRights.anonymous) {
                                TLRPC.Chat chat2 = this.currentChat;
                                if (chat2 != null) {
                                }
                                z2 = z4;
                                z3 = false;
                            }
                            z2 = z4;
                            z3 = true;
                        } else if (childAdapterPosition == this.manageTopicsRow) {
                            z2 = this.adminRights.manage_topics;
                            z3 = this.myAdminRights.manage_topics;
                        } else {
                            z2 = false;
                            z3 = false;
                        }
                        TextCheckCell2 textCheckCell2222 = (TextCheckCell2) childAt;
                        textCheckCell2222.setChecked(z2);
                        textCheckCell2222.setEnabled(z3, z);
                    }
                } else if ((childAdapterPosition != this.changeInfoRow || this.defaultBannedRights.change_info) && (childAdapterPosition != this.pinMessagesRow || this.defaultBannedRights.pin_messages)) {
                    TextCheckCell2 textCheckCell23 = (TextCheckCell2) childAt;
                    textCheckCell23.setChecked(false);
                    textCheckCell23.setEnabled(childAdapterPosition == this.manageRow, z);
                } else {
                    TextCheckCell2 textCheckCell24 = (TextCheckCell2) childAt;
                    textCheckCell24.setChecked(true);
                    textCheckCell24.setEnabled(false, false);
                }
            }
        }
        this.listViewAdapter.notifyDataSetChanged();
        AnimatedTextView animatedTextView = this.addBotButtonText;
        if (animatedTextView != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString(R.string.AddBotButton));
            sb.append(" ");
            sb.append(LocaleController.getString(this.asAdmin ? R.string.AddBotButtonAsAdmin : R.string.AddBotButtonAsMember));
            animatedTextView.setText(sb.toString(), z, this.asAdmin);
        }
        ValueAnimator valueAnimator = this.asAdminAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.asAdminAnimator = null;
        }
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.asAdminT, this.asAdmin ? 1.0f : 0.0f);
            this.asAdminAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda10
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ChatRightsEditActivity.this.lambda$updateAsAdmin$25(valueAnimator2);
                }
            });
            this.asAdminAnimator.setDuration((long) (Math.abs(this.asAdminT - (this.asAdmin ? 1.0f : 0.0f)) * 200.0f));
            this.asAdminAnimator.start();
            return;
        }
        this.asAdminT = this.asAdmin ? 1.0f : 0.0f;
        FrameLayout frameLayout2 = this.addBotButton;
        if (frameLayout2 != null) {
            frameLayout2.invalidate();
        }
    }

    private void updateRows(boolean z) {
        int i;
        int i2;
        int min = Math.min(this.transferOwnerShadowRow, this.transferOwnerRow);
        this.manageRow = -1;
        this.changeInfoRow = -1;
        this.postMessagesRow = -1;
        this.editMesagesRow = -1;
        this.deleteMessagesRow = -1;
        this.addAdminsRow = -1;
        this.anonymousRow = -1;
        this.banUsersRow = -1;
        this.addUsersRow = -1;
        this.pinMessagesRow = -1;
        this.rightsShadowRow = -1;
        this.removeAdminRow = -1;
        this.removeAdminShadowRow = -1;
        this.cantEditInfoRow = -1;
        this.transferOwnerShadowRow = -1;
        this.transferOwnerRow = -1;
        this.rankHeaderRow = -1;
        this.rankRow = -1;
        this.rankInfoRow = -1;
        this.sendMessagesRow = -1;
        this.sendMediaRow = -1;
        this.channelMessagesRow = -1;
        this.channelPostMessagesRow = -1;
        this.channelEditMessagesRow = -1;
        this.channelDeleteMessagesRow = -1;
        this.channelStoriesRow = -1;
        this.channelPostStoriesRow = -1;
        this.channelEditStoriesRow = -1;
        this.channelDeleteStoriesRow = -1;
        this.sendPhotosRow = -1;
        this.sendVideosRow = -1;
        this.sendMusicRow = -1;
        this.sendFilesRow = -1;
        this.sendVoiceRow = -1;
        this.sendRoundRow = -1;
        this.sendStickersRow = -1;
        this.sendPollsRow = -1;
        this.embedLinksRow = -1;
        this.startVoiceChatRow = -1;
        this.untilSectionRow = -1;
        this.untilDateRow = -1;
        this.addBotButtonRow = -1;
        this.manageTopicsRow = -1;
        this.rowCount = 3;
        this.permissionsStartRow = 3;
        int i3 = this.currentType;
        if (i3 == 0 || i3 == 2) {
            if (this.isChannel) {
                this.changeInfoRow = 3;
                this.rowCount = 5;
                this.channelMessagesRow = 4;
                if (this.channelMessagesExpanded) {
                    this.channelPostMessagesRow = 5;
                    this.channelEditMessagesRow = 6;
                    this.rowCount = 8;
                    this.channelDeleteMessagesRow = 7;
                }
                int i4 = this.rowCount;
                int i5 = i4 + 1;
                this.rowCount = i5;
                this.channelStoriesRow = i4;
                if (this.channelStoriesExpanded) {
                    this.channelPostStoriesRow = i5;
                    this.channelEditStoriesRow = i4 + 2;
                    this.rowCount = i4 + 4;
                    this.channelDeleteStoriesRow = i4 + 3;
                }
                int i6 = this.rowCount;
                this.addUsersRow = i6;
                this.startVoiceChatRow = i6 + 1;
                this.rowCount = i6 + 3;
                this.addAdminsRow = i6 + 2;
            } else {
                if (i3 == 2) {
                    this.rowCount = 4;
                    this.manageRow = 3;
                }
                int i7 = this.rowCount;
                this.changeInfoRow = i7;
                this.deleteMessagesRow = i7 + 1;
                this.banUsersRow = i7 + 2;
                this.addUsersRow = i7 + 3;
                this.rowCount = i7 + 5;
                this.pinMessagesRow = i7 + 4;
                if (ChatObject.isChannel(this.currentChat)) {
                    int i8 = this.rowCount;
                    int i9 = i8 + 1;
                    this.rowCount = i9;
                    this.channelStoriesRow = i8;
                    if (this.channelStoriesExpanded) {
                        this.channelPostStoriesRow = i9;
                        this.channelEditStoriesRow = i8 + 2;
                        this.rowCount = i8 + 4;
                        this.channelDeleteStoriesRow = i8 + 3;
                    }
                }
                int i10 = this.rowCount;
                this.startVoiceChatRow = i10;
                this.addAdminsRow = i10 + 1;
                int i11 = i10 + 3;
                this.rowCount = i11;
                this.anonymousRow = i10 + 2;
                if (this.isForum) {
                    this.rowCount = i10 + 4;
                    this.manageTopicsRow = i11;
                }
            }
        } else if (i3 == 1) {
            this.sendMessagesRow = 3;
            this.rowCount = 5;
            this.sendMediaRow = 4;
            if (this.sendMediaExpanded) {
                this.sendPhotosRow = 5;
                this.sendVideosRow = 6;
                this.sendFilesRow = 7;
                this.sendMusicRow = 8;
                this.sendVoiceRow = 9;
                this.sendRoundRow = 10;
                this.sendStickersRow = 11;
                this.sendPollsRow = 12;
                this.rowCount = 14;
                this.embedLinksRow = 13;
            }
            int i12 = this.rowCount;
            this.addUsersRow = i12;
            this.pinMessagesRow = i12 + 1;
            int i13 = i12 + 3;
            this.rowCount = i13;
            this.changeInfoRow = i12 + 2;
            if (this.isForum) {
                this.rowCount = i12 + 4;
                this.manageTopicsRow = i13;
            }
            int i14 = this.rowCount;
            this.untilSectionRow = i14;
            this.rowCount = i14 + 2;
            this.untilDateRow = i14 + 1;
        }
        int i15 = this.rowCount;
        this.permissionsEndRow = i15;
        if (this.canEdit) {
            if (!this.isChannel && ((i2 = this.currentType) == 0 || (i2 == 2 && this.asAdmin))) {
                this.rightsShadowRow = i15;
                this.rankHeaderRow = i15 + 1;
                this.rankRow = i15 + 2;
                this.rowCount = i15 + 4;
                this.rankInfoRow = i15 + 3;
            }
            TLRPC.Chat chat = this.currentChat;
            if (chat != null && chat.creator && this.currentType == 0 && hasAllAdminRights() && !this.currentUser.bot) {
                int i16 = this.rightsShadowRow;
                if (i16 == -1) {
                    int i17 = this.rowCount;
                    this.rowCount = i17 + 1;
                    this.transferOwnerShadowRow = i17;
                }
                int i18 = this.rowCount;
                int i19 = i18 + 1;
                this.rowCount = i19;
                this.transferOwnerRow = i18;
                if (i16 != -1) {
                    this.rowCount = i18 + 2;
                    this.transferOwnerShadowRow = i19;
                }
            }
            if (this.initialIsSet) {
                if (this.rightsShadowRow == -1) {
                    int i20 = this.rowCount;
                    this.rowCount = i20 + 1;
                    this.rightsShadowRow = i20;
                }
                int i21 = this.rowCount;
                this.removeAdminRow = i21;
                this.rowCount = i21 + 2;
                this.removeAdminShadowRow = i21 + 1;
            }
        } else if (this.currentType == 0) {
            if (!this.isChannel && (!this.currentRank.isEmpty() || (this.currentChat.creator && UserObject.isUserSelf(this.currentUser)))) {
                int i22 = this.rowCount;
                this.rightsShadowRow = i22;
                this.rankHeaderRow = i22 + 1;
                this.rowCount = i22 + 3;
                this.rankRow = i22 + 2;
                if (this.currentChat.creator && UserObject.isUserSelf(this.currentUser)) {
                    int i23 = this.rowCount;
                    this.rowCount = i23 + 1;
                    this.rankInfoRow = i23;
                }
            }
            int i24 = this.rowCount;
            this.rowCount = i24 + 1;
            this.cantEditInfoRow = i24;
        } else {
            this.rowCount = i15 + 1;
            this.rightsShadowRow = i15;
        }
        if (this.currentType == 2) {
            int i25 = this.rowCount;
            this.rowCount = i25 + 1;
            this.addBotButtonRow = i25;
        }
        if (z) {
            if (min == -1 && (i = this.transferOwnerShadowRow) != -1) {
                this.listViewAdapter.notifyItemRangeInserted(Math.min(i, this.transferOwnerRow), 2);
            } else {
                if (min == -1 || this.transferOwnerShadowRow != -1) {
                    return;
                }
                this.listViewAdapter.notifyItemRangeRemoved(min, 2);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.currentType;
        if (i2 == 0) {
            actionBar = this.actionBar;
            i = R.string.EditAdmin;
        } else if (i2 == 2) {
            actionBar = this.actionBar;
            i = R.string.AddBot;
        } else {
            actionBar = this.actionBar;
            i = R.string.UserRestrictions;
        }
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ChatRightsEditActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    if (ChatRightsEditActivity.this.checkDiscard()) {
                        ChatRightsEditActivity.this.lambda$onBackPressed$321();
                    }
                } else if (i3 == 1) {
                    ChatRightsEditActivity.this.onDonePressed();
                }
            }
        });
        boolean z = false;
        if (this.canEdit || (!this.isChannel && this.currentChat.creator && UserObject.isUserSelf(this.currentUser))) {
            ActionBarMenu createMenu = this.actionBar.createMenu();
            Drawable mutate = context.getResources().getDrawable(R.drawable.ic_ab_done).mutate();
            int i3 = Theme.key_actionBarDefaultIcon;
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i3), PorterDuff.Mode.MULTIPLY));
            this.doneDrawable = new CrossfadeDrawable(mutate, new CircularProgressDrawable(Theme.getColor(i3)));
            createMenu.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
            createMenu.getItem(1).setIcon(this.doneDrawable);
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.ChatRightsEditActivity.2
            private int previousHeight = -1;

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i4, int i5, int i6, int i7) {
                super.onLayout(z2, i4, i5, i6, i7);
                int i8 = i7 - i5;
                int i9 = this.previousHeight;
                if (i9 != -1 && Math.abs(i9 - i8) > AndroidUtilities.dp(20.0f)) {
                    ChatRightsEditActivity.this.listView.smoothScrollToPosition(ChatRightsEditActivity.this.rowCount - 1);
                }
                this.previousHeight = i8;
            }
        };
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        View view = this.fragmentView;
        FrameLayout frameLayout2 = (FrameLayout) view;
        view.setFocusableInTouchMode(true);
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.ChatRightsEditActivity.3
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (ChatRightsEditActivity.this.loading) {
                    return false;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatRightsEditActivity.this.loading) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setClipChildren(this.currentType != 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, r1, z) { // from class: org.telegram.ui.ChatRightsEditActivity.4
            @Override // androidx.recyclerview.widget.LinearLayoutManager
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 5000;
            }
        };
        this.linearLayoutManager = linearLayoutManager;
        linearLayoutManager.setInitialPrefetchItemCount(100);
        this.listView.setLayoutManager(this.linearLayoutManager);
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        if (this.currentType == 2) {
            this.listView.setResetSelectorOnChanged(false);
        }
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDurations(350L);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ChatRightsEditActivity.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i4) {
                if (i4 == 1) {
                    AndroidUtilities.hideKeyboard(ChatRightsEditActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i4) {
                ChatRightsEditActivity.this.lambda$createView$6(context, view2, i4);
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.dialogDeleted) {
            if ((-this.chatId) == ((Long) objArr[0]).longValue()) {
                INavigationLayout iNavigationLayout = this.parentLayout;
                if (iNavigationLayout == null || iNavigationLayout.getLastFragment() != this) {
                    removeSelfFromStack();
                } else {
                    lambda$onBackPressed$321();
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ChatRightsEditActivity.this.lambda$getThemeDescriptions$26();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{UserCell2.class, TextSettingsCell.class, TextCheckCell2.class, HeaderCell.class, TextDetailCell.class, PollEditTextCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i3 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i4 = Theme.key_text_RedRegular;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switch2Track));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switch2TrackChecked));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription((View) null, 0, new Class[]{DialogRadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_dialogTextBlack));
        arrayList.add(new ThemeDescription((View) null, 0, new Class[]{DialogRadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_dialogTextGray2));
        arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOX, new Class[]{DialogRadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_dialogRadioBackground));
        arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{DialogRadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_dialogRadioBackgroundChecked));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return checkDiscard();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.dialogDeleted);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        getNotificationCenter().removeObserver(this, NotificationCenter.dialogDeleted);
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public void setDelegate(ChatRightsEditActivityDelegate chatRightsEditActivityDelegate) {
        this.delegate = chatRightsEditActivityDelegate;
    }

    public void setLoading(boolean z) {
        ValueAnimator valueAnimator = this.doneDrawableAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.loading = z;
        this.actionBar.getBackButton().setEnabled(!this.loading);
        CrossfadeDrawable crossfadeDrawable = this.doneDrawable;
        if (crossfadeDrawable != null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(crossfadeDrawable.getProgress(), this.loading ? 1.0f : 0.0f);
            this.doneDrawableAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ChatRightsEditActivity$$ExternalSyntheticLambda9
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ChatRightsEditActivity.this.lambda$setLoading$22(valueAnimator2);
                }
            });
            this.doneDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ChatRightsEditActivity.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    ChatRightsEditActivity.this.doneDrawable.setProgress(ChatRightsEditActivity.this.loading ? 1.0f : 0.0f);
                    ChatRightsEditActivity.this.doneDrawable.invalidateSelf();
                }
            });
            this.doneDrawableAnimator.setDuration((long) (Math.abs(this.doneDrawable.getProgress() - (this.loading ? 1.0f : 0.0f)) * 150.0f));
            this.doneDrawableAnimator.start();
        }
    }
}
