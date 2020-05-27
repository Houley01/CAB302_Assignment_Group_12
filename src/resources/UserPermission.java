package resources;

public class UserPermission {
    private static boolean createBillboard;
    private static boolean editAllBillboard;
    private static boolean scheduleBillboard;
    private static boolean editUser;
    
    public boolean GetUserPermission(String permission) {

        switch (permission) {
            case "CreateBillboard":
                return CreateBillboard();
            case "EditAllBillboard":
                return EditAllBillboard();
            case "ScheduleBillboard":
                return ScheduleBillboard();
            case "EditUser":
                return EditUser();
            default:
                return false;
        }
    }
    public void SetUserPermission(boolean[] permission) {
        createBillboard = permission[0];
        editAllBillboard = permission[1];
        scheduleBillboard = permission[2];
        editUser = permission[3];
    }
    private boolean CreateBillboard() {
        return createBillboard;
    }
    private boolean EditAllBillboard() {
        return editAllBillboard;
    }
    private boolean ScheduleBillboard() {
        return scheduleBillboard;
    }
    private boolean EditUser() {
        return editUser;
    }
}
