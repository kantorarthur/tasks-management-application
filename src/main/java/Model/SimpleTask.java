package Model;

import java.io.*;

public final class SimpleTask extends Task implements Serializable{
    private int startHour;
    private int endHour;
    public SimpleTask(int startHour, int endHour)
    {
        super();
        this.startHour = startHour;
        this.endHour = endHour;
    }
    public int getStartHour()
    {
        return this.startHour;
    }

    public int getEndHour()
    {
        return this.endHour;
    }


    public void setEndHour(int endHour)
    {
        this.endHour = endHour;
    }


    public void setStartHour(int startHour)
    {
        this.startHour = startHour;
    }
    @Override
    public int estimateDuration()
    {
        if(this.startHour > this.endHour)
            return 24-this.startHour + this.endHour;
        else
            return this.endHour - this.startHour;
    }
    @Override
    public String toString()
    {
        return "Simple Task, Start Hour: " + this.getStartHour() + " ,End Hour: " + this.getEndHour() + " ,Task ID:" + this.getIdTask() + " ,estimate duration:"
                + this.estimateDuration() + " ,status of task: " + this.getStatusTask() + "\n";
    }
}

