import java.awt.event.ActionListener;
import javax.swing.*;


public class LaunchView extends JFrame {


  private JLabel nameLabel = new JLabel("Name: ");
  private JTextField nameField = new JTextField(10);
  private JLabel ageLabel = new JLabel("Alter: ");
  private JTextField ageField = new JTextField(10);
  private JButton hostButton = new JButton("Host");
  private JButton joinButton = new JButton("Join");


  LaunchView(){
    JPanel loginPanel = new JPanel();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(600,200);

    loginPanel.add(nameLabel);
    loginPanel.add(nameField);
    loginPanel.add(ageLabel);
    loginPanel.add(ageField);
    loginPanel.add(hostButton);
    loginPanel.add(joinButton);

    this.add(loginPanel);
  }

  public void setNameLabel (String headline) {
    nameLabel.setText(headline);
  }

  public String getTheName(){
    return nameField.getText();
  }


  //public String getTheAge(){
  //  return Integer.parseInt(ageLabel.getText());
  //}

  //void addHostListener(ActionListener listenForHostButton){
  //  startHost
  //}



}
