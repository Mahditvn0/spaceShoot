package game.main;


import game.component.panelGame;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    public Main() {
        init();
    }

    public void init(){
        setTitle("SpaceShoot");
        setSize( 1300, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame. EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panelGame panel = new panelGame();
        add (panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                panel.start();
            }
        });
    }
    public static void main (String[]args){
        Main main = new Main();
        main.setVisible(true);
    }
}

