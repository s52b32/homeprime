package homeprime.manager.tasks.pojos.enums;

/**
 * Types of task status.
 *
 * @author Milan Ramljak
 *
 */
public enum TaskStatusType {
    /**
     * Task created but not initiated.
     */
    New,
    /**
     * Task is running.
     */
    Running,
    /**
     * Task finished successfully.
     */
    Completed,
    /**
     * Failure happened during task execution.
     */
    Failed;

    @Override
    public String toString() {
        return super.toString();
    }

}
