package datasets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class UData implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true, updatable = false)
    private String login;

    @Column(name = "pass", unique = false, updatable = false)
    private String pass;

    @SuppressWarnings("UnusedDeclaration")
    public UData() {
    }

    public UData(String login, String pass) {
        setId(-1);
        setLogin(login);
        setPass(pass);
    }

    public long getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "UData{" +
                "id=" + id + ", " +
                "login='" + login + "', " +
                "pass='" + pass + "'" +
                '}';
    }
}
