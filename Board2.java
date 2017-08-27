/*import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board2 {
    // define the constants will be used in the game.
    private int boardArray[][] = new int[16][16];
    private int hexTileHeight = 20;
    private int hexTileWidth = 20;
    private double verticalOffset = hexTileHeight * 3 / 4;
    private double horizontalOffset = hexTileWidth;
    private double startX;
    private double startY;
    private double startXInit = hexTileWidth / 2;
    private double startYInit = hexTileHeight / 2;

    private final int NUM_IMAGES = 13;
    private boolean inGame;
    private int mines_left;
    private Image[] img;

    private int all_cells;
    private JLabel statusbar;
    private JLabel timeBar;
    //var hexTile;

    // Board2's constructor
    public Board2() {

        img = new Image[NUM_IMAGES];  // NUM_IMAGES== 13
        for (int i = 0; i < NUM_IMAGES; i++) { // 12 pictures in total
            img[i] = (new ImageIcon(this.getClass().getResource(i + ".png"))).getImage();
        }


        newGame();
    }

    public void newGame() {
        for (int i = 0; i < 16; i++)

        {
            if (i % 2 != 0) {
                startX = 2 * startXInit;
            } else {
                startX = startXInit;
            }
            startY = startYInit + (i * verticalOffset);
            for (int j = 0; j < 16; j++) {
                hexGrid.add(hexTile);
            }
            startX += horizontalOffset;
        }
    }


    public int findHexTile() {
        var pos = game.input.activePointer.position;
        pos.x -= hexGrid.x;
        pos.y -= hexGrid.y;
        var xVal = Math.floor((pos.x) / hexTileWidth);
        var yVal = Math.floor((pos.y) / (hexTileHeight * 3 / 4));
        var dX = (pos.x) % hexTileWidth;
        var dY = (pos.y) % (hexTileHeight * 3 / 4);
        var slope = (hexTileHeight / 4) / (hexTileWidth / 2);
        var caldY = dX * slope;
        var delta = hexTileHeight / 4 - caldY;

        if (yVal % 2 == = 0) {
            //correction needs to happen in triangular portions & the offset rows
            if (Math.abs(delta) > dY) {
                if (delta > 0) {//odd row bottom right half
                    xVal--;
                    yVal--;
                } else {//odd row bottom left half
                    yVal--;
                }
            }
        } else {
            if (dX > hexTileWidth / 2) {// available values don't work for even row bottom right half
                if (dY < ((hexTileHeight / 2) - caldY)) {//even row bottom right half
                    yVal--;
                }
            } else {
                if (dY > caldY) {//odd row top right & mid right halves
                    xVal--;
                } else {//even row bottom left half
                    yVal--;
                }
            }
        }
        pos.x = yVal;
        pos.y = xVal;
        return pos;
    }

    function findHexTile() {
        var pos = game.input.activePointer.position;
        pos.x -= hexGrid.x;
        pos.y -= hexGrid.y;
        var xVal = Math.floor((pos.x) / (hexTileWidth * 3 / 4));
        var yVal = Math.floor((pos.y) / (hexTileHeight));
        var dX = (pos.x) % (hexTileWidth * 3 / 4);
        var dY = (pos.y) % (hexTileHeight);
        var slope = (hexTileHeight / 2) / (hexTileWidth / 4);
        var caldX = dY / slope;
        var delta = hexTileWidth / 4 - caldX;
        if (xVal % 2 == = 0) {
            if (dX > Math.abs(delta)) {// even left

            } else {//odd right
                if (delta > 0) {//odd right bottom
                    xVal--;
                    yVal--;
                } else {//odd right top
                    xVal--;
                }
            }
        } else {
            if (delta > 0) {
                if (dX < caldX) {//even right top
                    xVal--;
                } else {//odd mid
                    yVal--;
                }
            } else {//current values wont help for even right bottom
                if (dX < ((hexTileWidth / 2) - caldX)) {//even right bottom
                    xVal--;
                }
            }
        }
        pos.x = yVal;
        pos.y = xVal;
        return pos;
    }

    function addMines() {
        var tileType = 0;
        var tempArray =[];
        var newPt = new Phaser.Point();
        for (var i = 0; i < levelData.length; i++) {
            for (var j = 0; j < levelData[0].length; j++) {
                tileType = levelData[i][j];
                if (tileType == = 0) {
                    newPt = new Phaser.Point();
                    newPt.x = i;
                    newPt.y = j;
                    tempArray.push(newPt);
                }
            }
        }
        for (var i = 0; i < numMines; i++) {
            newPt = Phaser.ArrayUtils.removeRandomItem(tempArray);
            levelData[newPt.x][newPt.y] = 10;//10 is mine
            updateNeighbors(newPt.x, newPt.y);
        }
    }

    function updateNeighbors(i, j) {//update neighbors around this mine
        var tileType = 0;
        var tempArray = getNeighbors(i, j);
        var tmpPt;
        for (var k = 0; k < tempArray.length; k++) {
            tmpPt = tempArray[k];
            tileType = levelData[tmpPt.x][tmpPt.y];
            levelData[tmpPt.x][tmpPt.y] = tileType + 1;
        }
    }
}
*/