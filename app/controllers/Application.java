package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.data.validation.Constraints;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import views.html.*;
import models.BlogPost;
import models.PostComment;
import javax.jws.soap.SOAPBinding;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /* Internal Classes - Begin */

    // the classes for User's model
    public static class UserForm {
        @Constraints.Required
        public String codename;
    }

    public static class OrgIsNeeded extends UserForm {
        @Constraints.Required
        public String org;
    }

    public static class SignUp extends OrgIsNeeded {
        @Constraints.Required
        @Constraints.MinLength(6)
        public String password;
    }

    // the classes for Login's model
    public static class Login extends UserForm {
        @Constraints.Required
        public String password;
    }

    /* Internal Classes - End */

    /* Tool Methods - Begin */

    /**
     * To build a JSON response for AngularJS
     * @param type success or failure
     * @param message the message to show
     * @return the JSON response
     */
    protected static ObjectNode buildJsonResponse(String type, String message){

        ObjectNode msg = Json.newObject();
        msg.put("message",message);

        ObjectNode wrapper = Json.newObject();
        wrapper.put(type,msg);

        return wrapper;
    }

    /**
     * Build a JSON node for login response
     * @param user the user information for the login user
     * @return the response in JSON
     */
    private static ObjectNode buildJsonForLoginAuthentification(User user,String t){

        ObjectNode msg = Json.newObject();
        msg.put("message",t);
        msg.put("user",user.codename);

        ObjectNode wrapper = Json.newObject();
        wrapper.put("success",msg);

        return wrapper;
    }

    /* Tool Methods - End */

    /* Action Methods - Begin */

    /**
     * A real sign up action which checks whether the users exists or not by its codename
     * And it could insert a new user's info into DB
     * @return the response for the registration - Good for success, Bad request for failure which can be treated by AngularJS
     */
    public Result signup(){

        Form<SignUp> signUpForm = Form.
                form(SignUp.class).bindFromRequest();

        if(signUpForm.hasErrors()) return badRequest(signUpForm.errorsAsJson());

        SignUp newUser = signUpForm.get();

        // if the new User's code name exists already in our DB, just return an error
        java.util.List<User> t= User.findAllUsers();
        for(int i = 0;i<t.size();i++){
            if(t.get(i).codename.equals(newUser.codename)) {
                return badRequest(buildJsonResponse("error", "User exists!!"));
            }
        }

        // otherwise, we insert the new user into our DB
        User n = new User(newUser.codename,newUser.org,newUser.password);
        n.save();

        session().clear();
        session("username", newUser.codename);

        return ok(buildJsonResponse("success","User created successfully!!"));

    }

    /**
     * the action for login ~~
     * @return the response for the login, success or failure
     */
    public Result login(){

        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            return badRequest(loginForm.errorsAsJson());
        }

        Login loggingUser = loginForm.get();

        // authentification
        User user = User.findByCodenameAndPassword(loggingUser.codename,loggingUser.password);

        if( user == null ){
            return badRequest(buildJsonResponse("error","Incorrect email or password"));
        }
        else{
            session().clear();
            session("username",loggingUser.codename);

            return ok(buildJsonForLoginAuthentification(user, "Logged in successfully"));
        }
    }

    /**
     * the action for logout
     * @return the response in JSON of success
     */
    public Result logout(){

        session().clear();
        // System.out.println("Logged out!");
        return ok(buildJsonResponse("success","Logged out successfully"));

    }

    /**
     * the action to check whether there is already a user logged in
     * @return
     */
    public Result isAuthenticated(){

        if(session().get("username") == null ){
            return unauthorized();
        }

        else{
            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "User is logged in Already");
            msg.put("user", session().get("username"));
            wrapper.put("success",msg);
            return ok(wrapper);
        }
    }

    /**
     * get all the blogs to show before unlogged users
     * @return the result of the posts
     */
    public Result getPosts(){
        return(ok(Json.toJson(BlogPost.find.findList())));
    }

    /* Action Methods - End */
}
