package resources;

public class UserPermission {
    public boolean GetUserPermission(String username, String permission) {

        switch (permission) {
            case "CreateBillboard":
                return CreateBillboard(username);
            case "EditAllBillboard":
                return EditAllBillboard(username);
            case "ScheduleBillboard":
                return ScheduleBillboard(username);
            case "EditUser":
                return EditUser(username);
            default:
                return false;
        }
    }
    private boolean CreateBillboard(String username) {
        return false;
    }
    private boolean EditAllBillboard(String username) {
        return false;
    }
    private boolean ScheduleBillboard(String username) {
        return false;
    }

    private boolean EditUser(String username) {
        return false;
    }
}
