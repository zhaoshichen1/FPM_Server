package controllers;

import play.mvc.Security.*;
import play.mvc.Http.*;
import play.mvc.*;

/**
 * Created by zhaoshichen on 10/4/15.
 */
public class Secured extends Authenticator{

    @Override
    public String getUsername(Context ctx){
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx){
        return unauthorized();
    }
}
