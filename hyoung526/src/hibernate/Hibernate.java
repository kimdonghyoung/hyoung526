/**
 * hibernate.xml 파일을 로딩하여 hibernate를 사용할 수 있게 하는 객체를 얻어오는 필수 파일
 * 단, 스프링을 사용한다면 이 파일은 존재 하지 않음
 */
package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {
	//hibernate.xml 위치
	private final static String RESOURCE = "hibernate/hibernate.xml";
	private static SessionFactory sessionFactory = null;
	
	//hibernate 세션팩토리를 얻는 메소드
	@SuppressWarnings("deprecation")
	public static SessionFactory getSessionFactory(){
		if(sessionFactory == null){
			try {
				sessionFactory = new Configuration().configure(RESOURCE).buildSessionFactory();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return sessionFactory;
	}

}
