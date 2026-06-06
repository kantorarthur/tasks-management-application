package Model;
import java.io.*;


public abstract sealed class Task implements Serializable permits SimpleTask, ComplexTask {
    private static int nextIdTask=0;
    private final int idTask;
    private String statusTask;
    public Task()
    {
        this.idTask = ++nextIdTask;
        this.statusTask = "Uncompleted";
    }

    public String getStatusTask()
    {
        return this.statusTask;
    }

    public void setStatusTask(String statusTask)
    {
        this.statusTask = statusTask;
    }

    public int getIdTask()
    {
        return this.idTask;
    }

    public static int getNextIdTask()
    {
        return nextIdTask;
    }

    public static void setNextIdTask(int id)
    {
        nextIdTask = id;
    }

    public abstract int estimateDuration();

    @Override
    public String toString()
    {
        return  "Status of task:" + this.getStatusTask() + "Id of task: " + this.getIdTask() + "\n";
    }
}
