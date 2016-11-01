package com.smartfarm.db.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.User;
import com.smartfarm.tools.ExceptionUtils;

public class UserDao {

	public static User add(User user) {
		
		User res = null;
		try {
			
			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			User old = dao.queryBuilder().where().eq("phone_num", user.getPhone()).queryForFirst();
			
			if(old == null) {
				
				dao.create(user);
				res = user;
			} else {
				 
				old.setBirthday(user.getBirthday());
				old.setEmail(user.getEmail());
				old.setQq(user.getQq());
				old.setSignature(user.getSignature());
				old.setFace(user.getFace());
				old.setAddr(user.getAddr());
				old.setName(user.getName());
				old.setRemark(user.getRemark());
				old.setSex(user.isSex());
				old.setTime(user.getTime());
				old.setTime(user.getTime());

				dao.update(old);
				res =  old;
			}
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}
		
		return res;
	}
	
	public static boolean delete(User user) {
		
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			
			return dao.delete(user) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static boolean delete(int id) {
		
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			
			return dao.deleteById(id) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static User findById(int id) {
		
		User user = null;
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			
			user = dao.queryForId(id);
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}

		return user;
	}
	
	public static void update(User user) {
		
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			dao.update(user);
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}
	}
}
