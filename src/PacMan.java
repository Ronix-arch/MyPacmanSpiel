import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Random;

public class PacMan extends JPanel implements ActionListener, KeyListener {


    private int rowCount = 21;
    private int columnCount = 19;
    public int cellSize = 32;
    private int boardWidth = columnCount * cellSize;
    private int boardHeight = rowCount * cellSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private String[] cellMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"};
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;
    Timer gametimer;
    char[] directions = {'U', 'D', 'L', 'R'};// for Up down left right
    Random rand = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.GRAY);
        addKeyListener(this);
        setFocusable(true);

        // Loading Images "C:\Users\srgro\IdeaProjects\PacMan\src\main\java\Pacman\wall.png"
        wallImage = new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("redGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        loadMap();
        for (Block ghost : ghosts) {
            char newDirection = directions[rand.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gametimer = new Timer(50, this);
        gametimer.start();


    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                String row = cellMap[i];
                char cellMapChar = row.charAt(j);
                int x = j * cellSize;
                int y = i * cellSize;
                if (cellMapChar == 'X') {// we have a wall
                    Block wall = new Block(wallImage, x, y, cellSize, cellSize,this);
                    walls.add(wall);

                } else if (cellMapChar == 'b') {
                    Block ghost = new Block(blueGhostImage, x, y, cellSize, cellSize,this);
                    ghosts.add(ghost);

                } else if (cellMapChar == 'o') {
                    Block ghost = new Block(orangeGhostImage, x, y, cellSize, cellSize,this);
                    ghosts.add(ghost);
                } else if (cellMapChar == 'p') {
                    Block ghost = new Block(pinkGhostImage, x, y, cellSize, cellSize,this);
                    ghosts.add(ghost);
                } else if (cellMapChar == 'r') {
                    Block ghost = new Block(redGhostImage, x, y, cellSize, cellSize,this);
                    ghosts.add(ghost);
                } else if (cellMapChar == 'P') {
                    pacman = new Block(pacmanRightImage, x, y, cellSize, cellSize,this);
                } else if (cellMapChar == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 4, 4, this);
                    foods.add(food);
                }


            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);

        }
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.BLACK);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        // score
        g.setFont(new Font("Arial", Font.BOLD, 18));
        if (gameOver) {
            g.drawString("GAME OVER: " + String.valueOf(score), cellSize / 2, cellSize / 2);
        } else {
            g.drawString("Lives: " + String.valueOf(lives) + " Score: " + String.valueOf(score), cellSize / 2, cellSize / 2);
        }

    }

    public void move() {

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        // check for ghost colllisions
        for (Block ghost : ghosts) {
            if (collision(pacman, ghost)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();

            }
            if (ghost.y == cellSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                // here
              ghost.updateDirection('U'); // the one above
//  if(ghost.x == cellSize * 15 && ghost.direction == 'L'||ghost.x == cellSize * 5 && ghost.direction == 'R') {
    // ghost.updateDirection('U');}  does not work yet the one above ?
                    if(ghost.x<=0 && ghost.direction == 'L') {
                    ghost.x= boardWidth-ghost.width;
                } else if(ghost.x>=boardWidth-ghost.width && ghost.direction == 'R'){
                    ghost.x= 0;

                }
           }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {


                if (collision(ghost, wall) /*|| ghost.x <= 0 || ghost.x + ghost.width >= boardWidth*/) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[rand.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        } // food collisions ditection
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }


    public  boolean collision(Block one, Block two) {
        return one.x < two.x + two.width &&
                one.x + one.width > two.x &&
                one.y < two.y + two.height &&
                one.y + one.height > two.y;
    }

    private void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[rand.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gametimer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gametimer.start();
        }
        //  System.out.println("keyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }
        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;

        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }




}
