package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.avaje.ebean.*;

/**
 * Created by zhaoshichen on 10/3/15.
 */
@Entity
public class User extends Model {

    /* Variables - Begin */
    @Id
    public Long id;

    // the codename of the guy such as SZO for Shichen ZHAO
    @Column (length = 255, unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String codename;

    // the organization of the guy such as CS-CRM-ZAS
    @Column (length = 255, nullable = false)
    public String org;

    @Column (length = 65, nullable = false)
    private byte[] shaPassword;

//    // the articles posted by this guy
//    @OneToMany(cascade = CascadeType.ALL)
//    @JsonIgnore
//    public List<BlogPost> posts;

    /* Variables - End */

    /* Methods - Begin */

    public void setPassword(String password){
        this.shaPassword = getSha512(password);
    }

    public User(String c,String o,String p){
        this.codename = c;
        this.org = o;
        setPassword(p);
    }

    // encryption of the password
    public static byte[] getSha512(String value){
        try{
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
        catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    public static final Finder<Long, User> find = new Finder<Long, User>(Long.class,User.class);

    public static User findByCodenameAndPassword(String codename,String password){
        return find
                .where()
                .eq("codeanme",codename.toLowerCase())
                .eq("shaPassword",getSha512(password))
                .findUnique();
    }

    public static User findByCodename(String codename){
        return find
                .where()
                .eq("codename",codename.toLowerCase())
                .findUnique();
    }

    /* Methods - End */

}
