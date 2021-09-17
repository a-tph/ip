package kate.parser;

import kate.command.Command;
import kate.exception.EmptyFieldException;
import kate.exception.EmptyTaskException;
import kate.exception.FileCorruptedException;
import kate.exception.InvalidCommandException;
import kate.exception.InvalidFieldException;
import kate.task.Task;
import kate.tasklist.TaskList;

import java.util.ArrayList;

public class Parser {

    private static final int LENGTH_TODO = 4;
    private static final int LENGTH_DEADLINE = 8;
    private static final int LENGTH_EVENT = 5;
    private static final int LENGTH_DELETE = 6;

    private static final String DELIM_PIPE =  " \\| ";
    private static final String DELIM_SPACE = " ";

    /**
     * Default constructor for Parser Class
     */
    public Parser() {
    }

    /**
     * Extracts the command parameter from the user input
     *
     * @param userInput Input provided by user
     * @return Command object of the given valid command
     * @throws InvalidCommandException If command is invalid
     */
    public static Command extractCommand(String userInput) throws InvalidCommandException {
        String[] inputArr = userInput.split(" ");
        String givenCommand = inputArr[0].toUpperCase();
        for (Command command : Command.values()) {
            if (command.name().equals(givenCommand)) {
                return command;
            }
        }
        throw new InvalidCommandException();
    }

    /**
     * Processes the user input to extract description
     *
     * @param userInput Input provided by user
     * @return Task description for ToDo
     * @throws EmptyFieldException If task description is empty
     */
    public static String processToDoInput(String userInput) throws EmptyFieldException {
        String taskDescription = userInput.substring(LENGTH_TODO).strip();

        if (taskDescription.isEmpty()) {
            throw new EmptyFieldException();
        }
        return taskDescription;
    }

    /**
     * Process the user input to extract description and deadline
     *
     * @param userInput Input provided by user
     * @return String array of description and deadline
     * @throws EmptyFieldException If description or deadline is empty
     */
    public static String[] processDeadlineInput(String userInput) throws EmptyFieldException {
        String taskInfo = userInput.substring(LENGTH_DEADLINE).strip();
        String[] infoArr = taskInfo.split(" /by ", 2);

        try {
            String taskDescription = infoArr[0].strip();
            String deadline = infoArr[1].strip();

            if (taskDescription.isEmpty() || deadline.isEmpty()) {
                throw new EmptyFieldException();
            }

            infoArr[0] = taskDescription;
            infoArr[1] = deadline;

            return infoArr;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyFieldException();
        }
    }

    /**
     * Process the user input to extract description and time frame
     *
     * @param userInput Input provided by user
     * @return String array of description and time frame
     * @throws EmptyFieldException If description or time frame is empty
     */
    public static String[] processEventInput(String userInput) throws EmptyFieldException {
        String taskInfo = userInput.substring(LENGTH_EVENT).strip();
        String[] infoArr = taskInfo.split(" /at ", 2);

        try {
            String taskDescription = infoArr[0].strip();
            String timeframe = infoArr[1].strip();

            if (taskDescription.isEmpty() || timeframe.isEmpty()) {
                throw new EmptyFieldException();
            }

            infoArr[0] = taskDescription;
            infoArr[1] = timeframe;

            return infoArr;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyFieldException();
        }
    }

    /**
     * Process user input to extract the object of the associated task number
     *
     * @param tasks     The array list of tasks
     * @param userInput Input provided by user
     * @return Task object of the task that is done
     * @throws EmptyFieldException   If task number provided is empty
     * @throws InvalidFieldException If task number provided is invalid
     * @throws EmptyTaskException    If task list is empty
     */
    public static Task processDoneInput(ArrayList<Task> tasks, String userInput) throws EmptyFieldException,
            InvalidFieldException, EmptyTaskException {

        if (tasks.isEmpty()) {
            throw new EmptyTaskException();
        }

        String[] inputArr = userInput.split(DELIM_SPACE);
        try {
            String doneInput = inputArr[1];
            if (doneInput.isEmpty()) {
                throw new EmptyFieldException();
            }

            int taskNumber = Integer.parseInt(doneInput);
            if ((taskNumber > tasks.size() || (taskNumber < 1) || inputArr.length != 2)) {
                throw new InvalidFieldException();
            }
            int taskNumberIndex = taskNumber - 1;

            return tasks.get(taskNumberIndex);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidFieldException();
        }
    }

    /**
     * Process user input to extract the deleted task number
     *
     * @param tasks     The array list of tasks
     * @param userInput Input provided by the user
     * @return Task object of the deleted task
     * @throws EmptyFieldException   If task number provided is empty
     * @throws InvalidFieldException If task number provided is invalid
     * @throws EmptyTaskException    If task list is empty
     */
    public static Task processDeleteInput(ArrayList<Task> tasks, String userInput) throws EmptyFieldException,
            InvalidFieldException, EmptyTaskException {

        if (tasks.isEmpty()) {
            throw new EmptyTaskException();
        }

        String taskInput = userInput.substring(LENGTH_DELETE).strip();

        if (taskInput.isEmpty()) {
            throw new EmptyFieldException();
        }

        try {
            int taskNumber = Integer.parseInt(taskInput);

            if ((taskNumber > tasks.size()) || (taskNumber < 1)) {
                throw new InvalidFieldException();
            }

            int taskIndex = taskNumber - 1;

            return tasks.get(taskIndex);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidFieldException();
        }
    }

    /**
     * Process the text input of the saved tasks and pass it into the current task list;
     *
     * @param tasks      TasksList object to add task to the task list
     * @param storedTask One line of the record in data/kate.txt
     * @throws FileCorruptedException If file content has been tampered cannot be parsed
     */
    public static void processData(TaskList tasks, String storedTask) throws FileCorruptedException {
        try {
            String[] storedArr = storedTask.split(DELIM_PIPE);
            String taskLabel = storedArr[0];
            boolean isDone = Boolean.parseBoolean(storedArr[1]);
            String description = storedArr[2];
            switch (taskLabel) {
            case "T":
                tasks.addToDoFromFile(description, isDone);
                break;
            case "D":
                String deadline = storedArr[3];
                tasks.addDeadlineFromFile(description, isDone, deadline);
                break;
            case "E":
                String event = storedArr[3];
                tasks.addEventFromFile(description, isDone, event);
                break;
            default:
                throw new FileCorruptedException();
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new FileCorruptedException();
        }

    }

}