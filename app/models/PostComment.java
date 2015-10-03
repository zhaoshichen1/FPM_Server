package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.geometry.Pos;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaoshichen on 10/3/15.
 */
@Entity
public class PostComment extends Model {

    /* Variables - Begin */
    @Id
    public Long id;

    // the article to which the comment is linked
    @ManyToOne
    @JsonIgnore
    public BlogPost blogPost;

    // the author of the post
    @ManyToOne
    public User author;

    /* Variables - End */

    /* Methods - Begin */

    public static final Model.Finder<Long, PostComment> find = new Model.Finder<Long, PostComment>(Long.class,PostComment.class);

    // find all the topics created by the given user
    public static List<PostComment> findAllCommentsByPost(final BlogPost blogPost){
        return find
                .where()
                .eq("blogPost", blogPost)
                .findList();
    }

    // find a blog with its ID
    public static List<PostComment> findBlogPostByUser(final User user){
        return find
                .where()
                .eq("author", user)
                .findList();
    }

    /* Methods - End */
}