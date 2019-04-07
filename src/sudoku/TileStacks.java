/**
 * @file TileStacks.java
 * @author Melanie Roy-Plommer
 * @version 1.0
 *
 * @section DESCRIPTION
 * < >
 *
 * @section LICENSE Copyright 2018 - 2019 Permission to use, copy, modify,
 * and/or distribute this software for any purpose with or without fee is hereby
 * granted, provided that the above copyright notice and this permission notice
 * appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 *
 * @section Academic Integrity I certify that this work is solely my own and
 * complies with NBCC Academic Integrity Policy (policy 1111)
 */
package sudoku;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Melanie Roy-Plommer
 */
public class TileStacks extends AnchorManager {

    private ArrayList<ArrayList<Tile>> stacks = new ArrayList<ArrayList<Tile>>();

    public TileStacks(Dimension size, Point position) {
        super(size, position, 0);

        addImage("resources/blank.png");
        addImage("resources/tileout.png");

        //stacks
        for (int i = 0; i < 9; i++) {
            stacks.add(new ArrayList<Tile>());
        }

        //Set up anchors
        for (int i = 0; i < 9; i++) {
            addAnchor(new Point((int) (x - w / 2 + i * w / 9 + w / 18), (int) (y)));
        }

    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        for (int i = 0; i < anchors.size(); i++) {
            g.drawImage(images.get(1), (int) (anchors.get(i).getX() - 10), (int) (anchors.get(i).getY() - 10), 20, 20, null);
        }

    }

    /**
     * Pushes a tile to the stack
     *
     * @param tile
     */
    public void pushTile(Tile tile) {
        // if not empty, lock previous tile
        int stackSize = stacks.get(tile.getValue() - 1).size();

        if (stackSize > 0) {
            stacks.get(tile.getValue() - 1).get(stackSize - 1).setIsDraggable(false);
        }

        // set tile home
        tile.setHome(anchorAtIndex(tile.getValue() - 1));
        // add tile to appropriate stack
        stacks.get(tile.getValue() - 1).add(tile);

    }

    /**
     * Pops the top tile
     *
     * @param tile
     */
    public void popTile(Tile tile) {
        stacks.get(tile.getValue() - 1).remove(stacks.get(tile.getValue() - 1).size() - 1);
    }

    /**
     * will be used to reset tiles for next puzzle
     */
    public void popAllTiles() {
        for (int i = 0; i < stacks.size(); i++) {
            stacks.get(i).clear();
        }
    }

    /**
     * Sets every tile from every stack to isDraggable = false
     */
    public void lockTiles() {
        for (int i = 0; i < stacks.size(); i++) {
            for (int j = 0; j < stacks.get(i).size(); j++) {
                stacks.get(i).get(j).setIsDraggable(false);
            }
        }
    }

    /**
     * Set the top tile from every stack to isDraggable = true
     */
    public void unlockTiles() {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).size() > 0) {
                stacks.get(i).get(stacks.get(i).size() - 1).setIsDraggable(true);
            }
        }
    }

}
