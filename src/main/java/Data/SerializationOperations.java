package Data;
import java.io.*;
import BusinessLogic.*;
import Model.*;

public class SerializationOperations{

    private static final String fileName = "data.ser";
    private static final String employeeNextIdFileName = "employeeNextId.ser";
    private static final String taskNextIdFileName = "taskNextId.ser";

    public static void save(TasksManagement manager)
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            out.writeObject(manager);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static TasksManagement load()
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (TasksManagement) in.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            return new TasksManagement();
        }
    }

    public static void saveEmployeeNextId(int nextId)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(employeeNextIdFileName)))
        {
            oos.writeInt(nextId);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static int loadEmployeeNextId()
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(employeeNextIdFileName)))
        {
            return ois.readInt();
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public static void saveTaskNextId(int nextId)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(taskNextIdFileName)))
        {
            oos.writeInt(nextId);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static int loadTaskNextId()
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(taskNextIdFileName)))
        {
            return ois.readInt();
        }
        catch (IOException e)
        {
            return 0;
        }
    }

}