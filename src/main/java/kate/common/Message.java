package kate.common;

public class Message {

    public static final String TEXT_INDENTATION = "    ";

    public static final String COMMAND_TODO = "todo [description]";
    public static final String COMMAND_DEADLINE = "deadline [description] /by [dd-MM-yyyy] <HH:ss>";
    public static final String COMMAND_EVENT = "event [description] /at [dd-MM-yyyy]";
    public static final String COMMAND_DONE = "done [task number shown in list]";
    public static final String COMMAND_DELETE = "delete [task number]";
    public static final String COMMAND_FIND = "find [keyword]";
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_BYE = "bye";

    public static final String FAILURE_PARSE_DATE = TEXT_INDENTATION
            + "I don't understand the date or time format!\n"
            + TEXT_INDENTATION + "Please enter date and time in [dd-MM-yyyy] <HH:ss> format\n";

}
