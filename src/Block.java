import java.awt.*;


public class Block {
    int x;
    int y;
    int width;
    int height;
    Image image;
    int startX;
    int startY;
    char direction = 'U';  // i am to use U D L R
    int velocityX = 0;
    int velocityY = 0;
    PacMan pacMan;
   // int cellSize = 32;




    public Block(Image image, int x, int y, int width, int height ,PacMan pacMan) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.startX = x;
        this.startY = y;
        this.pacMan = pacMan;

    }

    void updateDirection(char direction) {
        char oldDirection = this.direction;
        this.direction = direction;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall : pacMan.walls) {
            if (pacMan.collision(this, wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = oldDirection;
                updateVelocity();
            }
        }
    }

    private void updateVelocity() {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -pacMan.cellSize / 4;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = pacMan.cellSize / 4;

        } else if (this.direction == 'L') {
            this.velocityX = -pacMan.cellSize / 4;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = pacMan.cellSize / 4;
            this.velocityY = 0;
        }
    }

    void reset() {
        this.x = startX;
        this.y = startY;
    }
}
