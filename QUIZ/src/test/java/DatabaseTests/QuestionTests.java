package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Models.Question;
import quiz_web.Models.questionTypes;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


// test question class
public class QuestionTests {
    @Test
    public void testGetters() {
        Question newQuestion = new Question(1, "question", questionTypes.FILL_IN_THE_BLANK,
                "", false, 2, 3.4);

        assertEquals(1, newQuestion.getQuestionId());
        assertEquals("question", newQuestion.getQuestionText());
        assertEquals(questionTypes.FILL_IN_THE_BLANK, newQuestion.getQuestionType());
        assertEquals("", newQuestion.getPictureUrl());
        assertEquals(false, newQuestion.isSortedRelevant());
        assertEquals(2, newQuestion.getQuizId());
        assertEquals(3.4, newQuestion.getPoint(), 0);
    }

    @Test
    public void testSetters() {
        Question newQuestion = new Question(1, "question", questionTypes.FILL_IN_THE_BLANK,
                "", false, 2, 3.4);

        newQuestion.setQuestionId(2);
        newQuestion.setQuestionText("lalala");
        newQuestion.setQuestionType(questionTypes.QUESTION_RESPONSE);
        newQuestion.setPictureUrl("jpg");
        newQuestion.setSortedRelevant(true);
        newQuestion.setQuizId(3);
        newQuestion.setPoint(11.5);

        assertEquals(2, newQuestion.getQuestionId());
        assertEquals("lalala", newQuestion.getQuestionText());
        assertEquals(questionTypes.QUESTION_RESPONSE, newQuestion.getQuestionType());
        assertEquals("jpg", newQuestion.getPictureUrl());
        assertEquals(true, newQuestion.isSortedRelevant());
        assertEquals(3, newQuestion.getQuizId());
        assertEquals(11.5, newQuestion.getPoint(), 0);

        System.out.println(newQuestion.toString());
    }

}
