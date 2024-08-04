package DatabaseTests;

import org.junit.Test;

import quiz_web.Models.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


// test answer class
public class AnswersTest {

    @Test
    public void testGetters() {
        Answer answer = new Answer(1, "answer", 4);
        assertEquals(1, answer.getQuestionId());
        assertEquals("answer", answer.getAnswer());
        assertEquals(4, answer.getAnswerNum());
    }

    @Test
    public void testSetters() {
        Answer answer = new Answer(1, "answer", 4);

        answer.setQuestionId(2);
        answer.setAnswer("lala");
        answer.setAnswerNum(5);

        assertEquals(2, answer.getQuestionId());
        assertEquals("lala", answer.getAnswer());
        assertEquals(5, answer.getAnswerNum());

        System.out.println(answer.getAnswerNum());
    }

    @Test
    public void testEqual() {
        Answer one = new Answer(1, "answer", 4);
        Answer two = new Answer(81, "answer", 42);

        assertTrue(one.equals(two));

        one = new Answer(1, "answer", 4);
        two = new Answer(81, "lalala", 42);

        assertFalse(one.equals(two));
    }
}
