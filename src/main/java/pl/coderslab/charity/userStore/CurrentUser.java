package pl.coderslab.charity.userStore;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.List;

public class CurrentUser implements HttpSessionBindingListener {

    private String username;
    private Long userId;
    private ActiveUserStore activeUserStore;

    public CurrentUser(String username, Long userId, ActiveUserStore activeUserStore) {
        this.username = username;
        this.userId = userId;
        this.activeUserStore = activeUserStore;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        List<String> users = activeUserStore.getUsers();
        List<Long> usersId = activeUserStore.getUsersId();
        CurrentUser user = (CurrentUser) event.getValue();
        if (!users.contains(user.getUsername())) {
            users.add(user.getUsername());
        }
        if (!usersId.contains(user.getUserId())){
            usersId.add(user.getUserId());
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        List<String> users = activeUserStore.getUsers();
        List<Long> usersId = activeUserStore.getUsersId();
        CurrentUser user = (CurrentUser) event.getValue();
        if (users.contains(user.getUsername())) {
            users.remove(user.getUsername());
        }
        if (usersId.contains(user.getUserId())){
            usersId.remove(user.getUserId());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActiveUserStore getActiveUserStore() {
        return activeUserStore;
    }

    public void setActiveUserStore(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }
}
