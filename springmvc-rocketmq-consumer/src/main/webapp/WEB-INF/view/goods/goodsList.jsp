<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>商品列表查询</title>
</head>
<body>
当前用户:${username } ,
<c:if test="${username!=null }">
    <a href="${pageContext.request.contextPath }/logout">退出</a>
</c:if>

<form action="${pageContext.request.contextPath }/goods/query"
      method="post">
    查询条件：
    <table width="80%" border=1>
        <tr>
            <td colspan="5"><input type="submit" value="查询" /></td>
        </tr>
        <tr>
            <td>商品名称</td>
            <td>价格</td>
            <td>生成日期</td>
            <td>描述</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${goodsList }" var="goods">
            <tr>
                <td>${goods.goodsName }</td>
                <td>${goods.price }</td>
                <td><fmt:formatDate value="${goods.createtime }"
                                    pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>${goods.detail }</td>
                <td><a
                        href="${pageContext.request.contextPath }/goods/modify?id=${goods.goodsId}">操作</a></td>
            </tr>
        </c:forEach>
    </table>
</form>

</body>
</html>