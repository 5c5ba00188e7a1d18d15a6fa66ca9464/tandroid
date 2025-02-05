package org.telegram.ui.Stories;

import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.support.LongSparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Stories.UserListPoller;

/* loaded from: classes5.dex */
public class UserListPoller {
    private static UserListPoller[] istances = new UserListPoller[4];
    final int currentAccount;
    LongSparseLongArray userPollLastTime = new LongSparseLongArray();
    ArrayList dialogIds = new ArrayList();
    ArrayList collectedDialogIds = new ArrayList();
    ArrayList runningRequests = new ArrayList();
    Runnable requestCollectedRunnables = new 1();

    class 1 implements Runnable {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(TLObject tLObject, ArrayList arrayList) {
            if (tLObject instanceof Vector) {
                Vector vector = (Vector) tLObject;
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (int i = 0; i < vector.objects.size(); i++) {
                    long longValue = ((Long) arrayList.get(i)).longValue();
                    MessagesController messagesController = MessagesController.getInstance(UserListPoller.this.currentAccount);
                    Long l = (Long) arrayList.get(i);
                    if (longValue > 0) {
                        TLRPC.User user = messagesController.getUser(l);
                        if (user != null) {
                            int i2 = ((Vector.Int) vector.objects.get(i)).value;
                            user.stories_max_id = i2;
                            user.flags2 = i2 != 0 ? user.flags2 | 32 : user.flags2 & (-33);
                            arrayList2.add(user);
                        }
                    } else {
                        TLRPC.Chat chat = messagesController.getChat(l);
                        if (chat != null) {
                            int i3 = ((Vector.Int) vector.objects.get(i)).value;
                            chat.stories_max_id = i3;
                            chat.flags2 = i3 != 0 ? chat.flags2 | 16 : chat.flags2 & (-17);
                            arrayList3.add(chat);
                        }
                    }
                }
                MessagesStorage.getInstance(UserListPoller.this.currentAccount).putUsersAndChats(arrayList2, arrayList3, true, true);
                NotificationCenter.getInstance(UserListPoller.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final ArrayList arrayList, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.UserListPoller$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UserListPoller.1.this.lambda$run$0(tLObject, arrayList);
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            if (UserListPoller.this.collectedDialogIds.isEmpty()) {
                return;
            }
            final ArrayList arrayList = new ArrayList(UserListPoller.this.collectedDialogIds);
            UserListPoller.this.collectedDialogIds.clear();
            TL_stories.TL_stories_getPeerMaxIDs tL_stories_getPeerMaxIDs = new TL_stories.TL_stories_getPeerMaxIDs();
            for (int i = 0; i < arrayList.size(); i++) {
                tL_stories_getPeerMaxIDs.id.add(MessagesController.getInstance(UserListPoller.this.currentAccount).getInputPeer(((Long) arrayList.get(i)).longValue()));
            }
            ConnectionsManager.getInstance(UserListPoller.this.currentAccount).sendRequest(tL_stories_getPeerMaxIDs, new RequestDelegate() { // from class: org.telegram.ui.Stories.UserListPoller$1$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    UserListPoller.1.this.lambda$run$1(arrayList, tLObject, tL_error);
                }
            });
        }
    }

    private UserListPoller(int i) {
        this.currentAccount = i;
    }

    public static UserListPoller getInstance(int i) {
        UserListPoller[] userListPollerArr = istances;
        if (userListPollerArr[i] == null) {
            userListPollerArr[i] = new UserListPoller(i);
        }
        return istances[i];
    }

    public void checkList(RecyclerListView recyclerListView) {
        TLRPC.User user;
        TLRPC.UserStatus userStatus;
        long currentTimeMillis = System.currentTimeMillis();
        this.dialogIds.clear();
        for (int i = 0; i < recyclerListView.getChildCount(); i++) {
            View childAt = recyclerListView.getChildAt(i);
            long dialogId = childAt instanceof DialogCell ? ((DialogCell) childAt).getDialogId() : childAt instanceof UserCell ? ((UserCell) childAt).getDialogId() : 0L;
            if (dialogId <= 0 ? !(!ChatObject.isChannel(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId))) || currentTimeMillis - this.userPollLastTime.get(dialogId, 0L) <= 3600000) : !((user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialogId))) == null || user.bot || user.self || user.contact || (userStatus = user.status) == null || (userStatus instanceof TLRPC.TL_userStatusEmpty) || currentTimeMillis - this.userPollLastTime.get(dialogId, 0L) <= 3600000)) {
                this.userPollLastTime.put(dialogId, currentTimeMillis);
                this.dialogIds.add(Long.valueOf(dialogId));
            }
        }
        if (this.dialogIds.isEmpty()) {
            return;
        }
        this.collectedDialogIds.addAll(this.dialogIds);
        AndroidUtilities.cancelRunOnUIThread(this.requestCollectedRunnables);
        AndroidUtilities.runOnUIThread(this.requestCollectedRunnables, 300L);
    }
}
