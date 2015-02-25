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

@WebServlet("/board/boardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    BoardDAOImpl boardDAO = null;   
    public BoardDeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get Parameters
		String num = request.getParameter("num");
		String pageNum = request.getParameter("pageNum");
		String searchType = request.getParameter("searchType");
		String searchText = request.getParameter("searchText");
		String searchTextUTF8 = new String(searchText.getBytes("ISO-8859-1"), "UTF-8");
		BoardModel boardModel = new BoardModel();
		//Set Parameters
		boardModel.setNum(Integer.parseInt(num));
		boardModel.setPageNum(pageNum);
		boardModel.setSearchType(searchType);
		boardModel.setSearchText(searchText);
		try {
			boardDAO = new BoardHibernateDAO();
			boardDAO.delete(boardModel);
			
			List<BoardModel> boardList = boardDAO.selectList(boardModel);
			
			int totalCount = boardDAO.selectCount(boardModel);
			
			request.setAttribute("boardList", boardList);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("boardModel", boardModel);
			request.setAttribute("pageNavigator", new PageNavigator().getPageNavigator(totalCount, boardModel.getListCount(), boardModel.getPagePerBlock(), 
					Integer.parseInt(pageNum), searchType, searchTextUTF8));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/jsps/boardList.jsp");
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
