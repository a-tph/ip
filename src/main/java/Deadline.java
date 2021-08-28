public class Deadline extends Task {
    protected String description;
    protected String deadline;
    private static final String DATELINE_CHECKBOX = "[D]";

    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    @Override
    public String printTaskInfo() {
        return DATELINE_CHECKBOX + super.printTaskInfo()
                + " (by: " + deadline + ")";
    }
}