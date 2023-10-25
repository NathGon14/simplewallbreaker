package Code;

import javax.swing.*;

public class Game extends JFrame {
    public Game(){
    add(new Board());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(377, 700);
        setLocationRelativeTo(null);
    }
}
