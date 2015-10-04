package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.avaje.ebean.*;
import play.data.format.Formats.*;

/**
 * Created by zhaoshichen on 10/3/15.
 */
@Entity
public class BlogPost extends Model {

    /* Variables - Begin */
    @Id
    public Long id;

    // the subject of the blog/article
    @Column (length = 255, unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String subject;

    // the content of the blog/article
    @Column (columnDefinition = "TEXT", nullable = false)
    public String content;

    // the blog is still active or not
    @Column (columnDefinition = "BOOLEAN")
    public boolean is_active;

    // the author of the blog/article
    @ManyToOne
    @Column
    public User author;

    @Column (columnDefinition = "DATE", nullable = false)
    public Date createTime;

    @Column (columnDefinition = "DATE")
    public Date closeTime;

    // the guy who closes the topic
    @ManyToOne
    @Column
    public User closer;

    // the number of comments
    public Long commentCount;

    // the articles posted by this guy
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    public List<PostComment> comments;

    /* Variables - End */

    /* Methods - Begin */

    // build the topic --> one author and the create time
    public void build_the_topic(User user){
        this.author = user;
        this.createTime = new Date();
    }

    // close the topic --> one closer and the close date
    public void close_the_topic(User user){
        this.closer = user;
        this.closeTime = new Date();
    }

    public static final Model.Finder<Long, BlogPost> find = new Model.Finder<Long, BlogPost>(Long.class,BlogPost.class);

    // find all the topics created by the given user
    public static List<BlogPost> findBlogPostsByUser(final User user){
        return find
                .where()
                .eq("author", user)
                .findList();
    }

    // find a blog with its ID
    public static BlogPost findBlogPostById(final Long id){
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

    /* Methods - End */
}