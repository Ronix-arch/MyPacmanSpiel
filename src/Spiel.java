import javax.swing.*;

public class Spiel {

        public static void main(String[] args) {
            int rowCount = 21;
            int columnCount = 19;
            int cellSize = 32;
            int boardWidth = columnCount*cellSize;
            int boardHeight = rowCount*cellSize;
             
            JFrame frame = new JFrame("PacManSpiel");
          // frame.setVisible(true);
            frame.setSize(boardWidth,boardHeight);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PacMan myPacManGame = new PacMan();
            frame.add(myPacManGame);
            frame.pack();
            myPacManGame.requestFocus();
            frame.setVisible(true);

        }

}
