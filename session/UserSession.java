package Finance.session;

import Finance.EntityFiles.User;


public class UserSession {

    private static User currentUser = null;

    private UserSession() {}

    public static void login(User user) {
        currentUser = user;
        System.out.println("[Session] Logged in as: " + user.getUsername());
    }

    public static void logout() {
        System.out.println("[Session] Logged out: " + (currentUser != null ? currentUser.getUsername() : "nobody"));
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
