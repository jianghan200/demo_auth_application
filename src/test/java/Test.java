import com.hsbc.auth.entities.Response;
import com.hsbc.auth.handler.LoginHandler;
import com.hsbc.auth.handler.RoleHandler;
import com.hsbc.auth.handler.UserHandler;
import com.hsbc.auth.utils.EncryptUtil;
import com.hsbc.auth.utils.ObjectParser;
import com.hsbc.auth.entities.User;

public class Test {

    /**
     * Test, no third party library is allowed
     * Do functional testing
     * @param args
     */
    public static void main(String[] args){
        try{
            assert 1==1;
            assert 2==1:"Error";
            testObejctParserWithUser();
            testObejctParserWithUser2();
            testCreateUser();
            testCreateUser_already_exist();

            testDeleteUserNotExist();
            testDeleteUser();

            testAddRoleForUser();
            testGetRoleForUser();

            checkUserRole();
            checkUserRoleNotExist();

            testCreateRole();
            testCreateRole_alreay_exist();

            testDeleteRole();
            testDeleteRoleNotExist();

            testAuthentication();
            testInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void testObejctParserWithUser() throws Exception {
        ObjectParser parser = new ObjectParser();
        User user = ObjectParser.parseUser("roles=[]&id=1&username=hi&password=hello");
        if( user.getId() != 1 || !"hi".equals(user.getUsername()) ||
                !EncryptUtil.md5("hello").equals(user.getPassword()) || user.getRoles().size() != 0){
            throw new Exception("testParseObejctWithUser not work expected");
        }
    }

    public static void testObejctParserWithUser2() throws Exception {
        ObjectParser parser = new ObjectParser();
        User user = ObjectParser.parseUser("roles=[{1:admin},{2:user}]&id=2&username=hi&password=hello");
        if( user.getId() != 2 || !"hi".equals(user.getUsername())
                || !EncryptUtil.md5("hello").equals(user.getPassword())
                || user.getRoles().size() != 2){
            throw new Exception("testObejctParserWithUser2 not work expected");
        }
    }

    public static void testAuthenticatinon() throws Exception {
        ObjectParser parser = new ObjectParser();
        User user = ObjectParser.parseUser("roles=[{1:admin},{2:user}]&id=2&username=hi&password=hello");
        if( user.getId() != 2 || !"hi".equals(user.getUsername())
                || !"hello".equals(user.getPassword())
                || user.getRoles().size() != 2){
            throw new Exception("testObejctParserWithUser2 not work expected");
        }
    }

    public static void testCreateUser() throws Exception {
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.createUser("roles=[{1:admin},{2:user}]&id=2&username=jerry&password=hello");
        if("The user jerry create success".equals(response.getMsg())){
            System.out.println("testCreatUser success");
        }else{
            System.err.println("testCreatUser failed");
        }
    }


    public static void testDeleteUserNotExist() throws Exception {
//        1663056385413-3b7cc9b0-97de-421f-b105-3bad78b0ffcd
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.deleteUser("roles=[{1:admin},{2:user}]&id=2&username=Henry&password=hello");
        if("User does not exist".equals(response.getMsg())){
            System.out.println("testDeleteUserNotExist success");
        }else{
            System.err.println("testDeleteUserNotExist failed");
        }
    }

    public static void testDeleteUser() throws Exception {
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.deleteUser("roles=[{1:admin},{2:user}]&id=2&username=jerry&password=hello");
        if("User delete success".equals(response.getMsg())){
            System.out.println("testDeleteUser success");
        }else{
            System.err.println("testDeleteUser failed");
        }
    }

    //    11L, "john", "pass123"
    public static void testCreateUser_already_exist() throws Exception {
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.createUser("roles=[{1:admin},{2:user}]&id=11&username=john&password=pass123");
        if("User already exist".equals(response.getMsg())){
            System.out.println("testCreatUser_already_exist success");
        }else{
            System.err.println("testCreatUser_already_exist failed");
        }
    }

    public static void testAddRoleForUser() throws Exception {
        String token = "1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1";
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.addRoleForUser(token, "{1:admin}");
        if("Add role to user success".equals(response.getMsg())){
            System.out.println("addRoleForUser success");
        }else{
            System.err.println("addRoleForUser failed");
        }
    }


    public static void testGetRoleForUser() throws Exception {
        String token = "1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1";
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.getRoleForUser(token);
        if("[{1:admin}]".equals(response.getMsg())){
            System.out.println("getRoleForUser success");
        }else{
            System.err.println("getRoleForUser failed");
        }
    }

    public static void checkUserRole() throws Exception {
        String token = "1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1";
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.checkUserRole(token, "{1:admin}");
        if("true".equals(response.getMsg())){
            System.out.println("checkUserRole success");
        }else{
            System.err.println("checkUserRole failed");
        }
    }

    public static void checkUserRoleNotExist() throws Exception {
        String token = "1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1";
        UserHandler userHandler = new UserHandler();
        Response response = userHandler.checkUserRole(token, "{15:admin2}");
        if("false".equals(response.getMsg())){
            System.out.println("checkUserRoleNotExist success");
        }else{
            System.err.println("checkUserRoleNotExist failed");
        }
    }

    public static void testCreateRole() throws Exception {
        RoleHandler roleHandler = new RoleHandler();
        Response response = roleHandler.createRole("{1:admin}");
        if("the role admin create success".equals(response.getMsg())){
            System.out.println("testCreateRole success");
        }else{
            System.err.println("testCreateRole failed");
        }
    }

    public static void testCreateRole_alreay_exist() throws Exception {
        RoleHandler roleHandler = new RoleHandler();
        Response response = roleHandler.createRole("{100:FTE}");
        if("the role already exist".equals(response.getMsg())){
            System.out.println("testCreateRole_alreay_exist success");
        }else{
            System.err.println("testCreateRole_alreay_exist failed");
        }
    }

    public static void testDeleteRole() throws Exception {
        RoleHandler roleHandler = new RoleHandler();
        Response response = roleHandler.deleteRole("{100:FTE}");
        if("role delete success".equals(response.getMsg())){
            System.out.println("testDeleteRole success");
        }else{
            System.err.println("testDeleteRole failed");
        }
    }

    public static void testDeleteRoleNotExist() throws Exception {
        RoleHandler roleHandler = new RoleHandler();
        Response response = roleHandler.deleteRole("{101:CONTRACT}");
        if("role does not exist".equals(response.getMsg())){
            System.out.println("testDeleteRoleNotExist success");
        }else{
            System.err.println("testDeleteRoleNotExist failed");
        }
    }


    public static void testAuthentication() throws Exception {
        LoginHandler loginHandler = new LoginHandler();
        Response response = loginHandler.authentication("username=merry&password=pass123");
        if("the user merry create success".equals(response.getMsg())){
            System.out.println("testAuthentication success");
        }else{
            System.err.println("testAuthentication failed");
        }
    }

    public static void testInvalidate() throws Exception {
        String token = "1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1";
        LoginHandler loginHandler = new LoginHandler();
        Response response = loginHandler.invalidate(token);
        if("".equals(response.getMsg())){
            System.out.println("testInvalidate success");
        }else{
            System.err.println("testInvalidate failed");
        }
    }





}
