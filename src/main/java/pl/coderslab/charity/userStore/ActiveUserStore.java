package pl.coderslab.charity.userStore;

import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {

    public List<Long> usersId;
    public List<String> users;

    public ActiveUserStore() {
        usersId = new ArrayList<>();
        users = new ArrayList<>();
    }

    public List<Long> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Long> usersId) {
        this.usersId = usersId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
