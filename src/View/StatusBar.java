package View;

import Controller.GameController;
import Model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Object constructor for View.StatusBar JPanel which stays at the bottom of the screen and displays
 */
public class StatusBar extends JPanel {

    private final JLabel currentPlayerIcon;
    private final JPanel descriptionPanel;
    private final JButton nextStep;
    private final JLabel currentAction;
    private final JLabel currentName;
    private JLabel deployLabel;

    public StatusBar(){
        this.setLayout(new GridLayout(1, 3, 3, 0));

        descriptionPanel = new JPanel();
        descriptionPanel.setMinimumSize(new Dimension(200, 40));
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel,BoxLayout.Y_AXIS));
        Border darkLine = BorderFactory.createLineBorder(Color.black,3);

        currentAction = new JLabel();

        currentName = new JLabel();
        currentName.setFont(new Font("Impact",Font.PLAIN,20));

        descriptionPanel.add(currentName);
        descriptionPanel.add(currentAction);

        currentPlayerIcon = new JLabel();
        currentPlayerIcon.setHorizontalAlignment(currentPlayerIcon.CENTER);
        currentPlayerIcon.setVerticalAlignment(currentPlayerIcon.CENTER);

        nextStep = new JButton("Next");
        nextStep.setFont(new Font("Impact",Font.PLAIN,30));
        nextStep.setBackground(new Color(178, 236, 83));

        setBounds(306,697,539,86);
        add(currentPlayerIcon);
        add(descriptionPanel);
        setBorder(darkLine);
        add(nextStep);

    }

    /**
     * Set current player in the object Status bar may reference variables from the player such as deployLabel which
     * updates when the deployable troops value goes down
     * @param player Model.Player Object
     */
    public void setPlayer(Player player){
        PlayerView playerView = player.getPlayerView();
        deployLabel = playerView.getDeployLabel();
        currentName.setText(player.getName());
        currentPlayerIcon.setIcon(playerView.getplayer_icon());
        descriptionPanel.setBackground(playerView.getBackground().brighter());
        setBackground(playerView.getBackground());
        displayReinforce();
    }

    /**
     * Set the action for nextStep button from nextButtonController
     * @param control Game controller which contains action listener nextState
     */
    public void setController(GameController control){
        nextStep.addActionListener(control::nextState);
    }

    /**
     * Displays to the user that it is currently the attacking phase, and removes the deployLabel and repaints the canvas
     * The method is called by updateDisplay for when the player interacts with nextStep
     */
    public void displayAttack(){
        descriptionPanel.remove(deployLabel);
        this.revalidate();
        this.repaint();
        currentAction.setText("<html>Time to FIGHT!<br>Attack enemy Territories</html>");
    }

    /**
     * Displays to the user that it is currently the Reinforce phase, and adds the deployLabel and repaints the canvas
     * The method is called by updateDisplay for when the player interacts with nextStep
     */
    public void displayReinforce(){
        descriptionPanel.add(deployLabel);
        this.revalidate();
        this.repaint();
        nextStep.setBackground(new Color(113, 220, 70));
        nextStep.setText("Next"); //add to first if after implementation
        currentAction.setText("<html>Troops to deploy!");

    }

    /**
     * Displays to the user that it is currently the Fortify Phase
     * The method is called by updateDisplay for when the player interacts with nextStep
     */
    public void displayFortify(){
        currentAction.setText("<html>Fortify!<br>Move troops from<br>Your territories</html>");
        nextStep.setBackground(new Color(0xF16262));
        nextStep.setText("End Turn");
    }

    /**
     * Called in View.GameView and occurs everytime the player clicks nextStep Button.
     * @param state State of the game can either be Reinforce,Attack, or Fortify.
     */
    public void updateDisplay(String state){
        switch (state) {
            case "Reinforce" -> displayReinforce();
            case "Attack" -> displayAttack();
            case "Fortify" -> displayFortify();
        }
    }
}