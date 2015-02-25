package servlets.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.board.BoardModel;
import dao.board.BoardDAOImpl;
import dao.board.BoardHibernateDAO;
import dao.board.BoardMyBatisDAO;

@WebServlet("/board/boardModifyServlet")
public class BoardModifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BoardDAOImpl boardDAO = null;   
    public BoardModifyServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Get Parameters
		String num = request.getParameter("num");
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
		
		BoardModel boardModel = new BoardModel();
		boardModel.setNum(Integer.parseInt(num));
		boardModel.setPageNum(pageNum);
		boardModel.setSearchType(searchType);
		boardModel.setSearchText(searchText);
		
		try {
			boardDAO = new BoardHibernateDAO();
			boardModel = boardDAO.select(boardModel);
			
			request.setAttribute("boardModel", boardModel);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/jsps/boardModify.jsp");
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//POST 방식 한글 꺠짐 처리
		request.setCharacterEncoding("UTF-8");
		
		//Get Parameters
		String num = request.getParameter("num");
		String subject = request.getParameter("subject");
		String writer = request.getParameter("writer");
		String contents = request.getParameter("contents");
		String ip = request.getRemoteAddr();
		
		BoardModel boardModel = new BoardModel();
		//Set Parameters
		boardModel.setNum(Integer.parseInt(num));
		boardModel.setSubject(subject);
		boardModel.setContents(contents);
		boardModel.setWriter(writer);
		boardModel.setIp(ip);
		try {
			boardDAO = new BoardMyBatisDAO();
			boardDAO.update(boardModel);
			
			response.sendRedirect("boardListServlet");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
