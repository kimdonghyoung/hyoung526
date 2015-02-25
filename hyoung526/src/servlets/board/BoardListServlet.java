package servlets.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.PageNavigator;
import model.board.BoardModel;
import dao.board.BoardDAOImpl;
import dao.board.BoardHibernateDAO;
import dao.board.BoardMyBatisDAO;

@WebServlet("/board/boardListServlet")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardDAOImpl boardDAO = null;
    public BoardListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BoardModel boardModel = new BoardModel();
		//Get Parameters
		String pageNum = request.getParameter("pageNum");
		String searchType = request.getParameter("searchType");
		String searchText = request.getParameter("searchText");
		if(pageNum == null){
			pageNum = "1";
		}
		if(searchText == null){
			searchText = "";
			searchType = "";
		}
		String searchTextUTF8 = new String(searchText.getBytes("ISO-8859-1"), "UTF-8");
		
		//Set Parameters
		boardModel.setPageNum(pageNum);
		boardModel.setSearchType(searchType);
		boardModel.setSearchText(searchTextUTF8);
		
		try {
			this.boardDAO = new BoardHibernateDAO();
			// 게시물 목록을 얻는 쿼리 실행
			List<BoardModel> boardList = this.boardDAO.selectList(boardModel);
			
			// 게시물 총 수
			int totalCount = this.boardDAO.selectCount(boardModel);
			// View 사용될 객체 설정
			request.setAttribute("boardList", boardList);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("pageNavigator", new PageNavigator().getPageNavigator(totalCount, boardModel.getListCount(), boardModel.getPagePerBlock(), 
					Integer.parseInt(pageNum), searchType, searchTextUTF8));
			request.setAttribute("boardModel", boardModel);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/jsps/boardList.jsp");
			requestDispatcher.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
