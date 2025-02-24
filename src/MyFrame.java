import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyFrame extends JFrame {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    int id = 1;

    JButton addBTN = new JButton("Add");

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel fNameL = new JLabel("Име:");
    JLabel lNameL = new JLabel("Фамилия:");
    JLabel sexL = new JLabel("Пол:");
    JLabel ageL = new JLabel("Години:");
    JLabel salaryL = new JLabel("Заплата:");

    JTextField fNameTf = new JTextField();
    JTextField lNameTf = new JTextField();
    JTextField ageTf = new JTextField();
    JTextField salaryTf = new JTextField();

    String[] item ={"Мъж" , "Жена"};
    JComboBox<String>  sexCombo = new JComboBox<String>(item);
    JComboBox<String> personCombo = new JComboBox<>();

    JButton addBNT = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton editBTN = new JButton("Edit");
    JButton searchBTN = new JButton("Search");
    JButton refreshBTN = new JButton("Refresh");


    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);




    public MyFrame(){

        this.setSize(400, 500);
        this.setLayout(new GridLayout(3, 1));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        upPanel.setLayout(new GridLayout(5, 2));
        upPanel.add(fNameL);
        upPanel.add(fNameTf);
        upPanel.add(lNameL);
        upPanel.add(lNameTf);
        upPanel.add(sexL);
        upPanel.add(sexCombo);
        upPanel.add(ageL);
        upPanel.add(ageTf);
        upPanel.add(salaryL);
        upPanel.add(salaryTf);

        this.add(upPanel);

        midPanel.add(addBTN);
        midPanel.add(deleteButton);
        midPanel.add(editBTN);
        midPanel.add(searchBTN);
        midPanel.add(refreshBTN);
        midPanel.add(personCombo);
        this.add(midPanel);

        addBTN.addActionListener(new AddAction());
        table.addMouseListener(new MouseAction());
        deleteButton.addActionListener(new DeleteAction());
        searchBTN.addActionListener(new SearchAction());
        refreshBTN.addActionListener(new RefreshAction());

        myScroll.setPreferredSize(new Dimension(350 , 150));
        downPanel.add(myScroll);
        this.add(downPanel);
        refreshCombo();
        refreshTable();


        this.setVisible(true);
    }

    public void refreshTable(){

        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement("select * from PERSON;");
            resultSet = statement.executeQuery();
            table.setModel(new MyModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void refreshCombo(){
        personCombo.removeAllItems();
        try {
            connection = DBConnection.getConnection();
            String sql = "select id, fname , lname from PERSON;";
            String item = "";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                item = resultSet.getObject(1).toString() + " " +
                        resultSet.getObject(2).toString() + " " + resultSet.getObject(3).toString();
                personCombo.addItem(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearForm(){
        fNameTf.setText("");
        lNameTf.setText("");
        salaryTf.setText("");
        ageTf.setText("");
        sexCombo.setSelectedIndex(0);
    }


    class AddAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String sql="insert into PERSON (fname, lname , sex , age , salary) " +
                    "values (?,?,?,?,?)";
            try {
                statement=connection.prepareStatement(sql);
                statement.setString(1, fNameTf.getText());
                statement.setString(2, lNameTf.getText());
                statement.setString(3, sexCombo.getSelectedItem().toString());
                statement.setInt(4, Integer.parseInt(ageTf.getText()));
                statement.setDouble(5, Double.parseDouble(salaryTf.getText()));
                statement.execute();
                refreshTable();
                clearForm();
                refreshCombo();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public class MouseAction implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            id = Integer.parseInt(table.getValueAt(row , 0).toString());
            fNameTf.setText(table.getValueAt(row , 1).toString());
            lNameTf.setText(table.getValueAt(row , 2).toString());
            ageTf.setText(table.getValueAt(row , 4).toString());
            salaryTf.setText(table.getValueAt(row , 5).toString());
            if(table.getValueAt(row , 3).toString().equals("Мъж")){
                sexCombo.setSelectedIndex(0);
            } else {
                sexCombo.setSelectedIndex(1);
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class DeleteAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "DELETE FROM PERSON WHERE ID = ?";
                statement=connection.prepareStatement(sql);
                statement.setInt(1 ,id);
                statement.execute();
                refreshTable();
                clearForm();
                refreshCombo();
                id = -1;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public class SearchAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                connection = DBConnection.getConnection();
                String sql = "select * from PERSON where age = ?;";
                statement = connection.prepareStatement(sql);
                statement.setInt(1 , Integer.parseInt(ageTf.getText()));
                resultSet = statement.executeQuery();
                table.setModel(new MyModel(resultSet));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public class RefreshAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           refreshTable();
           clearForm();
        }
    }
}