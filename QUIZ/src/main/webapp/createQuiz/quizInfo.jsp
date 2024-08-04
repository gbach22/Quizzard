<%@ page import="quiz_web.Models.Categories" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<String> tags = (List<String>)session.getAttribute("create_tags");
    String quizName = (String)session.getAttribute("create_quiz_name");
    String quizDesc = (String)session.getAttribute("create_quiz_desc");
    String picUrl = (String)session.getAttribute("create_quiz_pic");
    String category = (String)session.getAttribute("create_category");
    Boolean multi = (Boolean)session.getAttribute("create_is_multi");
    Boolean random = (Boolean)session.getAttribute("create_is_random");
    Boolean immediate = (Boolean)session.getAttribute("create_is_immediate");
    Boolean practice = (Boolean)session.getAttribute("create_is_practice");
%>
<html>
<head>
    <title>Add Quiz Info</title>
    <link rel="stylesheet" type="text/css" href="../style/quizInfoStyle.css">
</head>
<body>
<div class="header">
    <div class="home-link">
        <a href="../homePage/homePage.jsp">
            <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
        </a>
    </div>
</div>
<div class="container">
    <form action="<%= request.getContextPath() %>/createQuizInfo" method="post">
        <input type="text" maxlength="50" id="quiz_name" placeholder="Quiz Name" name="quiz_name" value="<%= quizName != null ? quizName : "" %>" required>
        <label for="description">Leave a description:</label>
            <textarea id="description" placeholder="Quiz Description" name="quiz_description" rows="4" cols="5" maxlength="100" oninput="updateCharCount()"><%= quizDesc != null ? quizDesc : "" %></textarea>
        <div id="charCount" style="margin-bottom: 10px; text-align: center">100 characters remaining</div>
        <textarea id="picture" maxlength="1000" placeholder="Quiz Picture Address" name="quiz_picture" rows="2"><%= picUrl != null ? picUrl : "" %></textarea>

        <label>
            <select name="category">
                <%
                    for (Categories cur : Categories.values()) {
                        String selected = cur.name().equalsIgnoreCase(category) ? "selected" : "";
                %>
                <option value="<%= cur.name() %>" <%= selected %>><%= cur.name() %></option>
                <%
                    }
                %>
            </select>
        </label>

        <div class="checkbox-container">
            <div class="checkbox-item">
                <input type="checkbox" name="multi" value="multi" id="multi" <%= (multi != null && multi) ? "checked" : "" %>>
                <label for="multi">Multiple Page</label>
            </div>

            <div class="checkbox-item">
                <input type="checkbox" name="random" value="random" id="random" <%= (random != null && random) ? "checked" : "" %>>
                <label for="random">Randomize Order</label>
            </div>

            <div class="checkbox-item">
                <input type="checkbox" name="immediate" value="multi" id="immediate" <%= (immediate != null && immediate) ? "checked" : "" %>>
                <label for="immediate">Immediate Correction</label>
            </div>

            <div class="checkbox-item">
                <input type="checkbox" name="practice" value="practice" id="practice" <%= (practice != null && practice) ? "checked" : "" %>>
                <label for="practice">Practice Mode</label>
            </div>
        </div>

        <div class="input-container">
            <input type="text" maxlength="50" name="tag" placeholder="Tag" style="text-align:center;">
            <input type="submit" name="button" value="Add Tag">
        </div>

        <ul>
            <%
                for (int i = 0; (tags != null) && i < tags.size(); i++) {
            %>
            <li>
                <%= tags.get(i) %>
                <button type="submit" name="button" value="Delete Tag <%= i %>">Delete</button>
            </li>
            <%
                }
            %>
        </ul>

        <div class="add-question-container">
            <input type="submit" name="button" value="Add Questions">
        </div>
    </form>
</div>
</body>
<script>
    function updateCharCount() {
        const textarea = document.getElementById('description');
        const charCount = document.getElementById('charCount');
        const remaining = textarea.maxLength - textarea.value.length;
        charCount.textContent = `${remaining} characters remaining`;
    }
</script>
</html>
