import Code.Board;
import Code.Game;

import javax.swing.*;
import java.awt.*;

public class Main  {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Game breakerGame = new Game();
            breakerGame.setVisible(true);
        });


    }
}