package Model;
import java.io.*;

public class Employee implements Serializable{
    private static int nextIdEmployee=0;
    private int idEmployee;
    private String name;

    @Override
    public int hashCode()
    {
        return Integer.hashCode(this.getIdEmployee());
    }

    public Employee(String name)
    {
        idEmployee = ++nextIdEmployee;
        this.name = name;
    }

    public int getIdEmployee()
    {
        return this.idEmployee;
    }

    public static int getNextIdEmployee()
    {
        return nextIdEmployee;
    }

    public static void setNextIdEmployee(int id)
    {
        nextIdEmployee = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {

        return "Employee name: " + this.getName() + " , employee ID: " + this.getIdEmployee() + "\n";
    }
}
