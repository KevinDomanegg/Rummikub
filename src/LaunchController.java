import java.awt.event.*;


public class LaunchController implements ActionListener{
  private LaunchModel launchmodel = new LaunchModel();
  public LaunchView launchview = new LaunchView();


  public void actionPerformed(ActionEvent e) {
    launchview.setNameLabel(LaunchModel.getGameName());
  }


}
