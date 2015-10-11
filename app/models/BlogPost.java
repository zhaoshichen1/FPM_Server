package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.avaje.ebean.*;
import play.data.format.Formats.*;
import java.text.SimpleDateFormat;

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

    @Column (nullable = false)
    public String createTime;

    @Column
    public String closeTime;

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

    public static String DateToYYYYMMDD(Date date){

        SimpleDateFormat sy1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormat=sy1.format(date);

        System.out.println("Date is "+dateFormat);

        return dateFormat;
    }

    /**
     * the constructor of a blog
     * @param s the subject
     * @param c the content
     * @param u the author
     */
    public BlogPost(String s,String c,User u){

        this.subject = s;
        this.content = c;
        this.author = u;
        this.createTime = DateToYYYYMMDD(new Date());
        this.closeTime = DateToYYYYMMDD(new Date());
        this.closer = u;
        this.commentCount = 0L;
        this.comments = new ArrayList<PostComment>();
        this.is_active = true;

    }

    // close the topic --> one closer and the close date
    public void close_the_topic(User user){
        this.closer = user;
        this.closeTime = DateToYYYYMMDD(new Date());
        this.is_active = false;

        System.out.println("The post "+this.id+" "+this.subject+" is closed by "+user.codename+" at "+this.closeTime);
        System.out.println("Is Active = "+this.is_active);
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