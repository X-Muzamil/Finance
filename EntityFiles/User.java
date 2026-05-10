package Finance.EntityFiles;


public class User {

    private int    userId;
    private String userName;
    private String email;
    private String passwordHash;

    public User() {}

    public User(int userId, String userName, String email, String passwordHash) {
        this.userId       = userId;
        this.userName     = userName;
        this.email        = email;
        this.passwordHash = passwordHash;
    }

    public int    getUserId()                         { return userId; }
    public void   setUserId(int userId)               { this.userId = userId; }

    public String getUsername()                       { return userName; }
    public void   setUsername(String userName)        { this.userName = userName; }

    public String getEmail()                          { return email; }
    public void   setEmail(String email)              { this.email = email; }

    public String getPasswordHash()                   { return passwordHash; }
    public void   setPasswordHash(String passwordHash){ this.passwordHash = passwordHash; }

    @Override
    public String toString() {
        return "User{id=" + userId + ", name='" + userName + "', email='" + email + "'}";
    }
}
