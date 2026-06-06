import GUI.MainFrame;

public class Main {
    public static void main(String[] args) {
        try
        {
            com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme.setup();
        }
        catch( Exception ex )
        {
            System.err.println( "Failed to initialize LaF" );
        }
        new MainFrame();
    }

}
