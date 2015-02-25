/**
 * 특이한 점은 객체의 멤버변수로 데이터베이스를 접근한다.
 */
package dao.board;

import hibernate.Hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.ComUtil;
import model.board.BoardModel;

public class BoardHibernateDAO implements BoardDAOImpl{
	private SessionFactory sessionFactory = null;
	public BoardHibernateDAO(){
		this.sessionFactory = Hibernate.getSessionFactory();
	}
	
	//게시판 목록 조회
	@SuppressWarnings("unchecked")
	public List<BoardModel> selectList(BoardModel boardModel) {
		
		//Hibernate 세션 객체 Open
		Session session = this.sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(BoardModel.class);
			criteria.setFirstResult(boardModel.getStartIndex());
			criteria.setMaxResults(boardModel.getListCount());
			
			//검색어
			String searchText = boardModel.getSearchText();
			String searchType = boardModel.getSearchType();
			//검색어 파라미터 존재 시
			if(!"".equals(searchText)){
				//Like 검색을 위한 메소드
				Criterion subject = Restrictions.like("subject", "%"+ searchText +"%");
				Criterion writer = Restrictions.like("writer", "%"+ searchText +"%");
				Criterion contents = Restrictions.like("contents", "%"+ searchText +"%");
				if("ALL".equals(searchType)){
					criteria.add(Restrictions.or(subject, writer, contents));
				}else if("SUBJECT".equals(searchType)){
					criteria.add(subject);
				}else if("WRITER".equals(searchType)){
					criteria.add(writer);
				}else if("CONTENTS".equals(searchType)){
					criteria.add(contents);
				}
			}
			//등록일시로 정렬(**컬럼명이 아니라 멤버변수)
			criteria.addOrder(Order.desc("regDate"));
			return criteria.list(); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session != null) session.close();
		}
		return null;
	}

	public int selectCount(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(BoardModel.class);
			
			//검색어
			String searchText = boardModel.getSearchText();
			String searchType = boardModel.getSearchType();
			//검색어 파라미터 존재 시
			if(!"".equals(searchText)){
				//Like 검색을 위한 메소드
				Criterion subject = Restrictions.like("subject", "%"+ searchText +"%");
				Criterion writer = Restrictions.like("writer", "%"+ searchText +"%");
				Criterion contents = Restrictions.like("contents", "%"+ searchText +"%");
				if("ALL".equals(searchType)){
					criteria.add(Restrictions.or(subject, writer, contents));
				}else if("SUBJECT".equals(searchType)){
					criteria.add(subject);
				}else if("WRITER".equals(searchType)){
					criteria.add(writer);
				}else if("CONTENTS".equals(searchType)){
					criteria.add(contents);
				}
			}
			criteria.setProjection(Projections.rowCount());
			return ((Long) criteria.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session != null) session.close();
		}
		return 0;
	}

	public BoardModel select(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		try {
			return (BoardModel)session.get(BoardModel.class, boardModel.getNum());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session != null) session.close();
		}
		return null;
	}

	public void insert(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		//INSERT, UPDATE, DELETE 사용 시 트랜잭션을 얻어 온다.
		Transaction transaction = session.beginTransaction();
		try {
			boardModel.setRegDate(ComUtil.getDate());
			boardModel.setModDate(ComUtil.getDate());
			session.save("BoardModel", boardModel);
			//커밋
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally{
			if(session != null)session.close();
		}
	}

	public void update(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		//INSERT, UPDATE, DELETE 사용 시 트랜잭션을 얻어 온다.
		Transaction transaction = session.beginTransaction();
		try {
			//기존의 값을 조회
			BoardModel oldBoardModel = (BoardModel) session.get(BoardModel.class, boardModel.getNum());
			//변경된 내용을 기존의 BoardModel 설정
			oldBoardModel.setSubject(boardModel.getSubject());
			oldBoardModel.setContents(boardModel.getContents());
			oldBoardModel.setIp(boardModel.getIp());
			oldBoardModel.setWriter(boardModel.getWriter());
			oldBoardModel.setModDate(ComUtil.getDate());
			//커밋
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally{
			if(session != null)session.close();
		}
	}

	public void updateHit(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		//INSERT, UPDATE, DELETE 사용 시 트랜잭션을 얻어 온다.
		Transaction transaction = session.beginTransaction();
		try {
			boardModel.setHit(boardModel.getHit()+1);
			session.update(boardModel);
			//커밋
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally{
			if(session != null)session.close();
		}
	}

	public void delete(BoardModel boardModel) {
		Session session = this.sessionFactory.openSession();
		//INSERT, UPDATE, DELETE 사용 시 트랜잭션을 얻어 온다.
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(boardModel);
			//커밋
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally{
			if(session != null)session.close();
		}
	}

}
