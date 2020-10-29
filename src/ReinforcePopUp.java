import javax.naming.TimeLimitExceededException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ReinforcePopUp extends JPopupMenu {

    private Territory selectedTerritory;
    private JButton minusButton;
    private JButton plusButton;
    private JButton deployButton;
    private JLabel title;
    private JLabel territoryName;
    private JPanel reinforcePanel;
    private JLabel photo;
    private JLabel troops;

    public ReinforcePopUp(Territory t){
        super();
        this.selectedTerritory = t;
        ReinforcePopUpController controller = new ReinforcePopUpController(this);

        reinforcePanel = new JPanel();
        setLayout(new GridLayout(1,3));

        title = new JLabel("REINFORCE");
        title.setHorizontalAlignment(title.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.ITALIC,35));

        territoryName = new JLabel();
        territoryName.setHorizontalAlignment(territoryName.CENTER);
        territoryName.setVerticalAlignment(territoryName.CENTER);
        territoryName.setText(selectedTerritory.getTerritoryName());
        territoryName.setFont(new Font("Comic Sans MS", Font.ITALIC,20));

        photo = new JLabel();
        photo.setIcon(scaleImage("resources/Captain.png"));
        photo.setHorizontalAlignment(photo.CENTER);
        photo.setVerticalAlignment(photo.CENTER);

        minusButton = new JButton("-");
        minusButton.setFont(new Font("Impact", Font.PLAIN,35));
        plusButton = new JButton("+");
        plusButton.setFont(new Font("Impact", Font.PLAIN,35));
        minusButton.addActionListener(controller);
        plusButton.addActionListener(controller);

        deployButton = new JButton("DEPLOY");
        deployButton.setFont(new Font("Impact", Font.PLAIN,35));
        deployButton.setBackground(new Color(178, 236, 83));
        deployButton.addActionListener(controller);

        troops = new JLabel("1");
        troops.setFont(new Font("Impact", Font.PLAIN,20));


        add(photo, BorderLayout.WEST);

        JPanel middlePanel = new JPanel();

        GridLayout middleLayout = new GridLayout(3,1);
        middlePanel.setLayout(middleLayout);
        middlePanel.add(title);
        middlePanel.add(territoryName);

        JPanel troopPanel = new JPanel();
        GridLayout buttonGrid = new GridLayout(0,3);
        troopPanel.setLayout(buttonGrid);
        troopPanel.add(minusButton);
        troops.setHorizontalAlignment(troops.CENTER);
        troops.setVerticalAlignment(troops.CENTER);
        troopPanel.add(troops);
        troopPanel.add(plusButton);

        middlePanel.add(troopPanel);

        JPanel rightPanel = new JPanel();
        GridLayout rightGrid = new GridLayout(3,1);
        rightPanel.setLayout(rightGrid);
        rightPanel.add(new JLabel());
        rightPanel.add(deployButton);
        rightPanel.add(new JLabel());

        add(middlePanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        setVisible(true);
    }

    public JLabel getTroops(){
        return troops;
    }

    public void setTroops(int x){
        troops.setText(String.valueOf(x));
    }

    public JButton getPlus(){
        return plusButton;
    }

    public JButton getMinus(){
        return minusButton;
    }

    public JButton getDeployButton(){
        return deployButton;
    }

    public Territory getTerritory(){
        return selectedTerritory;
    }

    public Player getPlayer(){
        return selectedTerritory.getOccupant();
    }

    private ImageIcon scaleImage(String filename) {
        ImageIcon scaledImg = new ImageIcon(getClass().getResource(filename));
        Image img = scaledImg.getImage().getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH );
        scaledImg = new ImageIcon(img);
        return scaledImg;
    }
}
