package cn.zjc.dao;


import cn.zjc.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author zhangjinci
 * @version 2016/10/7 17:37
 * @function
 */
@Repository
public class UserRepository extends AbstractCRUDDAO<User>{

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	public User queryByName(String name){
		return (User) getCurrentSession().createQuery("From User u where u.name = :name")
				.setString("name",name)
				.uniqueResult();
	}


}
