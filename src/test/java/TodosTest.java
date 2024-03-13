import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TodosTest {

    @Test
    public void shouldAddThreeTasksOfDifferentType() {
        SimpleTask simpleTask = new SimpleTask(5, "Позвонить родителям");

        String[] subtasks = { "Молоко", "Яйца", "Хлеб" };
        Epic epic = new Epic(55, subtasks);

        Meeting meeting = new Meeting(
                555,
                "Выкатка 3й версии приложения",
                "Приложение НетоБанка",
                "Во вторник после обеда"
        );

        Todos todos = new Todos();

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] expected = { simpleTask, epic, meeting };
        Task[] actual = todos.findAll();
        Assertions.assertArrayEquals(expected, actual);
    }
    @Test
    public void shouldReturnEmptyArrayWhenSearchingWithNoMatches() {
        Todos todos = new Todos();
        todos.add(new SimpleTask(1, "Купить чай"));
        todos.add(new Epic(2, new String[]{"Сходить в уникк"}));
        Task[] expected = {};
        Task[] actual = todos.search("поучиться");
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnArrayOfTasksWhenSearchingWithOneMatch() {
        Todos todos = new Todos();
        todos.add(new SimpleTask(1, "попить чай"));
        todos.add(new Epic(2, new String[]{"Сделать домашнее задание"}));
        Task[] expected = { new SimpleTask(1, "Сходить в уник") };
        Task[] actual = todos.search("чай");
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnArrayOfAllTasksWhenSearchingWithEmptyQuery() {
        Todos todos = new Todos();
        todos.add(new SimpleTask(1, "попить чай"));
        todos.add(new Epic(2, new String[]{"Сделать домашнее задание"}));
        Task[] expected = { new SimpleTask(1, "попить чай"), new Epic(2, new String[]{"Сделать домашнее задание"}) };
        Task[] actual = todos.search("");
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsFalseWhenQueryNotInSubtasks() {
        Epic epic = new Epic(1, new String[]{"Subtask 1", "Subtask 2", "Subtask 3"});
        String query = "Subtask 4";
        boolean expected = false;
        boolean actual = epic.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsTrueWhenQueryInSubtasks() {
        String[] subtasks = {"Subtask 1", "Subtask 2", "Subtask 3"};
        Epic epic = new Epic(1, subtasks);
        String query = "Subtask 2";
        boolean expected = true;
        boolean actual = epic.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsTrueWhenQueryInTitle() {
        SimpleTask task = new SimpleTask(1, "Some task");
        String query = "task";
        boolean expected = true; //строка запроса содержится в названии задачи
        boolean actual = task.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsFalseWhenQueryNotInTitle() {
        SimpleTask task = new SimpleTask(1, "task");
        String query = "other";
        boolean expected = false; //строка запроса не содержится в названии задачи
        boolean actual = task.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsTrueWhenQueryInTopic() {
        Meeting meeting = new Meeting(1, "topic", "project", "2024-03-20T10:00:00Z");
        String query = "topic";
        boolean expected = true; //строка запроса содержится в теме встречи
        boolean actual = meeting.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsTrueWhenQueryInProject() {
        Meeting meeting = new Meeting(1, "topic", "project", "2024-03-20T10:00:00Z");
        String query = "project";
        boolean expected = true; //строка запроса содержится в проекте, к которому относится встреча
        boolean actual = meeting.matches(query);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesReturnsFalseWhenQueryNotInTopicOrProject() {
        Meeting meeting = new Meeting(1, "topic", "project", "2024-03-20T10:00:00Z");
        String query = "other";
        boolean expected = false; //строка запроса не содержится ни в теме встречи, ни в проекте
        boolean actual = meeting.matches(query);
        Assertions.assertEquals(expected, actual);
    }

}
