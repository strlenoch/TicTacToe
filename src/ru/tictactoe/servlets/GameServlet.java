package ru.tictactoe.servlets;

import com.google.gson.Gson;
import ru.tictactoe.model.Game;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/game")
public class GameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");

        Game game = (Game) httpServletRequest.getSession().getAttribute("game");

        if (game == null) {
            httpServletResponse.getWriter().print("Игра не начата");
            return;
        }

        if (game.isOver()) {
            httpServletResponse.getWriter().print(new Gson().toJson(game));
            return;
        }

        int i, j;
        try {
            i = Integer.parseInt(httpServletRequest.getParameter("i"));
            j = Integer.parseInt(httpServletRequest.getParameter("j"));
        } catch (NumberFormatException e) {
            httpServletResponse.getWriter().print("Ошибка: " + e.getMessage());
            return;
        }
        try {
            game.nextStep(i, j);
            httpServletResponse.getWriter().print(new Gson().toJson(game));
        } catch (RuntimeException e) {
            httpServletResponse.getWriter().print("Ошибка: " + e.getMessage());
        }
    }
}
