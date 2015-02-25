<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.board.BoardModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>게시판 등록 폼</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/ckeditor/ckeditor.js"></script>
<style type="text/css">
	* {font-size: 9pt;}
	.btn_align {width: 600px; text-align: right;}
	table tbody tr th {background-color: gray;}
</style>
<script type="text/javascript">
	function goUrl(url){
		location.href = url;
	}
	
	function boardWriteCheck(){
		var frm = document.boardWriteForm;
		
		if(frm.subject.value == ""){
			alert("제목을 입력해 주세요.");
			frm.subject.focus();
			return false;
		}
		if(frm.writer.value == ""){
			alert("작성자를 입력해 주세요.");
			frm.writer.focus();
			return false;
		}
		
		return true;
	}
</script>
</head>

<body>
	<form name="boardWriteForm" method="post" action="<%=request.getContextPath()%>/board/boardWriteServlet" onsubmit="return boardWriteCheck();">
		
		<table border="1" summary="게시판 등록 폼">
			<caption>게시판 등록 폼</caption>
			<colgroup>
				<col width="100"/>
				<col width="500"/>
			</colgroup>
			<tbody>
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="subject" />
					</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>
						<input type="text" name="writer"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="contents" rows="10" cols="80"></textarea>
						<script>
							CKEDITOR.replace('contents');
						</script>
					</td>
				</tr>
			</tbody>
		</table>
		<p class="btn_align">
			<input type="button" value="목록" onclick="goUrl('<%=request.getContextPath()%>/board/boardListServlet');" />
			<input type="submit" value="글쓰기"/>
		</p>
	</form>
</body>
</html>