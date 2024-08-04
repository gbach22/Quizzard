package quiz_web.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/topPerformers")
public class topPerformersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String fromDateStr = httpServletRequest.getParameter("fromDate");
        String toDateStr = httpServletRequest.getParameter("toDate");
        int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));

        Timestamp fromDate = null;
        Timestamp toDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromDate = new Timestamp(dateFormat.parse(fromDateStr).getTime());
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                toDate = new Timestamp(dateFormat.parse(toDateStr).getTime());
            }


            httpServletRequest.setAttribute("fromDate", fromDate);
            httpServletRequest.setAttribute("toDate", toDate);
            httpServletRequest.setAttribute("quizId", quizId);

            httpServletRequest.getRequestDispatcher("/homePage/quiz.jsp").forward(httpServletRequest, httpServletResponse);
        } catch (ParseException e) {
            throw new ServletException("Error processing date range and fetching statistics", e);
        }
    }
}
