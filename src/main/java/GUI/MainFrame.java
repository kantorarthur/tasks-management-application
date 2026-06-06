

package GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{


    public MainFrame()
    {
        this.setSize(850, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 1));

        JButton employeesManagementBtn = new JButton("Employees Management");
        JButton tasksManagementBtn = new JButton("Tasks Management");

        panel.add(employeesManagementBtn);
        panel.add(tasksManagementBtn);

        employeesManagementBtn.addActionListener(e -> {
            new EmployeesFrame();
        });

        tasksManagementBtn.addActionListener(e -> {
            new TasksFrame();
        });



        this.add(panel);
        this.setVisible(true);
    }

}
