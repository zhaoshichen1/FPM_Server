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

import javax.jws.soap.SOAPBinding;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

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

    private static ObjectNode buildJsonResponse(String type, String message){

        ObjectNode msg = Json.newObject();
        msg.put("message",message);

        ObjectNode wrapper = Json.newObject();
        wrapper.put(type,msg);

        return wrapper;
    }

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

}
