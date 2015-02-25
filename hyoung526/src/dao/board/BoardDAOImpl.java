/**
 * 이 파일은 DAO 인터페이스로서, BoardDAO, BoardMyBatisDAO가 구현받게 된다.
 */
package dao.board;

import java.util.List;

import model.board.BoardModel;

public interface BoardDAOImpl {
	public List<BoardModel> selectList(BoardModel boardModel);
	public int selectCount(BoardModel boardModel);
	public BoardModel select(BoardModel boardModel);
	public void insert(BoardModel boardModel);
	public void update(BoardModel boardModel);
	public void updateHit(BoardModel boardModel);
	public void delete(BoardModel boardModel);
}
