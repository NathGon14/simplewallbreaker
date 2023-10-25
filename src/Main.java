import Code.Board;
import Code.Game;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Game breakerGame = new Game();
            breakerGame.setVisible(true);
        });







    }
}