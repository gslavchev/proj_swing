import javax.swing.*;

public class NewMyFrame extends JFrame {
    JPanel personP = new JPanel();
    JPanel carP = new JPanel();
    JPanel rentalP = new JPanel();
    JPanel sprP = new JPanel();

    JTabbedPane tabbedPane = new JTabbedPane();

    public NewMyFrame(){
        this.setSize(400, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane.add(personP , "Clients");
        tabbedPane.add(carP , "Cars");
        tabbedPane.add(rentalP , "rent");
        tabbedPane.add(sprP , "Spravka");
        this.add(tabbedPane);
        this.setVisible(true);
    }
}
