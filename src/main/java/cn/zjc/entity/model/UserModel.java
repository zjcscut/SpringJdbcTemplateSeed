package cn.zjc.entity.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjinci
 * @version 2016/10/31 17:55
 * @function
 */
public class UserModel implements Serializable{

    private Integer id;
    private String name;
    private Long age;
    private Date birth;

    public UserModel() {
    }

    public UserModel(Integer id, String name, Long age, Date birth) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birth = birth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birth=" + birth +
                '}';
    }
}
