package Model;

import java.util.ArrayList;
import java.io.*;

public final class ComplexTask extends Task implements Serializable{
    private ArrayList<Task> miniTask;
    public ComplexTask()
    {
        super();
        this.miniTask = new ArrayList<>();
    }


    public ArrayList<Task> getMiniTasks()
    {
        return this.miniTask;
    }


    public void addTask(Task task)
    {
        miniTask.add(task);
    }

    public void removeTask(Task task)
    {
        miniTask.remove(task);
    }

    @Override
    public int estimateDuration()
    {
        int totalDuration = 0;
        for(Task task : miniTask)
        {
            totalDuration = totalDuration + task.estimateDuration();
        }
        return totalDuration;
    }

    public void viewTasks()
    {
        System.out.println("This complex task, with task ID of: " + this.getIdTask() + " ,contains these tasks:");
        for(Task task : this.miniTask)
        {
            System.out.println("Type of task: " + task.getClass() + " with ID of: " + task.getIdTask() + " ,estimate duration: " + task.estimateDuration() + " hours ,and status: "
            + task.getStatusTask());
        }
    }

    @Override
    public String toString()
    {
        return "Complex task, ID task: " + this.getIdTask() + " ,status of task: " + this.getStatusTask() + " ,estimate duration of task: " + this.estimateDuration() +"\n";
    }

}
