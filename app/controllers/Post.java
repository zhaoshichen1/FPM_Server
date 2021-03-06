package controllers;

import com.avaje.ebean.Ebean;
import models.BlogPost;
import models.User;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.data.validation.Constraints;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import views.html.*;
import models.PostComment;

/**
 * Created by zhaoshichen on 10/11/15.
 */
public class Post extends Controller {

    /**
     * get the current user who is logged in
     * @return the current user
     */
    protected static User getCurrentLoggedInUser(){

        System.out.println(session().get("username"));
        System.out.println(User.findByCodename(session().get("username")).codename);

        return User.findByCodename(session().get("username"));
    }

    /**
     * internal class which defined the constraince of the form
     */
    public static class PostForm{

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String subject;

        @Constraints.Required
        public String content;
    }

    /**
     * the action in back-office to add the post
     * @return good Result if success, otherwise an error message
     */
    public Result addPost(){

        Form<PostForm> postForm = Form.form(PostForm.class).bindFromRequest();

        if(postForm.hasErrors()){
            return badRequest(postForm.errorsAsJson());
        }

        else{
            BlogPost newBlog = new BlogPost(postForm.get().subject,postForm.get().content,getCurrentLoggedInUser());
            newBlog.save();
        }

        return ok(Application.buildJsonResponse("success", "Post added successfully!!"));
    }

    /**
     * return all the posts written by the current user
     * @return
     */
    public Result getUserPosts(){
        User user = getCurrentLoggedInUser();
        if( user == null )
            return badRequest(Application.buildJsonResponse("error","No such user"));
        return ok(Json.toJson(BlogPost.findBlogPostsByUser(user)));
    }

    public static class CommentForm{

        @Constraints.Required
        public Long postId;

        @Constraints.Required
        public String comment;

    }

    /**
     * Add a comment to an indicated Post
     * @return
     */
    public Result addComment(){

        System.out.println("In Add Comment!");

        Form<CommentForm> commentForm = Form.form(CommentForm.class).bindFromRequest();

        if (commentForm.hasErrors()){

            System.out.println("Comment has error!");
            return badRequest(commentForm.errorsAsJson());

        } else {

            System.out.println("Comment is going to be done");

            Long postIID = commentForm.get().postId;
            BlogPost blog = BlogPost.findBlogPostById(postIID);
            PostComment newComment = new PostComment(blog,getCurrentLoggedInUser(),commentForm.get().comment);

            blog.comments.add(newComment);
            blog.commentCount = new Long(blog.comments.size());
            blog.save();
            newComment.save();

            return ok(Application.buildJsonResponse("success","Comment added successfully"));
        }
    }
}
