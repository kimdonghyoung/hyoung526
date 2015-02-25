/**
 * mybatis.xml 파일을 로딩하여 mybatis를 사용할 수 있게 하는 객체를 얻어오는 필수 파일
 * 단, 스프링을 사용하는 경우 이 파일은 존재하지 않을 것이다. 스프링 내의 설정파일에서 이 부분을 얻는 곳이 있다.
 */
package mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatis {
	//mybatis.xml 위치
	private final static String RESOURCE = "mybatis/mybatis.xml";
	private static SqlSessionFactory sqlMapper = null;
	//MyBatis 세션팩토리를 얻는 메소드
	public static SqlSessionFactory getSqlSessionFactory(){
		if(sqlMapper == null){
			Reader reader;
			try {
				//mybatis.xml 자원을 얻는다.
				reader = Resources.getResourceAsReader(RESOURCE);
				sqlMapper = new SqlSessionFactoryBuilder().build(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sqlMapper;
	}
}
