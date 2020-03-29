<%@ page import="ru.tictactoe.model.Game" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TicTacToe</title>
    <link rel="stylesheet" type="text/css" href="style.css">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="script.js"></script>
</head>
<body>
<div class="window">
    <h4>TicTacToe</h4>

    <%
        String stop = request.getParameter("stop");
        if (stop != null) {
            session.removeAttribute("game");
        }
        Game g = (Game) session.getAttribute("game");

        if (g == null) {
            String sizeStr = request.getParameter("size");
            if (sizeStr != null) {
                try {
                    int size = Integer.parseInt(sizeStr);
                    if (size != 3 && size != 5 && size != 7) {
                        throw new IllegalArgumentException();
                    }
                    g = new Game(size);
                    session.setAttribute("game", g);
    %>
    <jsp:forward page="index.jsp"/>
    <%
    } catch (IllegalArgumentException e) {
    %>
    <script>alert("Неверно указан размер поля")</script>
    <%
            }
        }
    %>

    <p>Выберите размер поля:</p>
    <form action="index.jsp" method="get">
        <label>
            3x3<input type="radio" value="3" name="size" checked>
        </label>
        <label>
            5x5<input type="radio" value="5" name="size">
        </label>
        <label>
            7x7<input type="radio" value="7" name="size">
        </label>
        <input type="submit" value="Старт">
    </form>
    <%
    } else {
    %>
    <p id="winnerMessage" style="display: none;"></p>
    <div class="field">
        <c:forEach items="${game.gameField.field}" var="field_row" varStatus="i">
            <div class="row">
                <c:forEach items="${field_row}" var="_cell" varStatus="j">
                    <c:set var="index" value="${(i.count - 1) * game.gameField.size + j.count - 1}"/>
                    <div class="cell" id="${index}" onclick="nextStep(${i.count - 1}, ${j.count - 1});">
                            ${_cell}
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>

    <form action="index.jsp" method="get">
        <input type="hidden" name="stop">
        <input type="submit" value="Выход">
    </form>
    <%
        }
    %>
</div>
</body>
</html>
