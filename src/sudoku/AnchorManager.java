/**
* @file AnchorManager.java
* @author Melanie Roy-Plommer
* @version 1.0
*
* @section DESCRIPTION
* <  >
*
* @section LICENSE
* Copyright 2018 - 2019
* Permission to use, copy, modify, and/or distribute this software for
* any purpose with or without fee is hereby granted, provided that the
* above copyright notice and this permission notice appear in all copies.
*
* THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
* WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
* ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
* WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
* ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
* OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*
* @section Academic Integrity
* I certify that this work is solely my own and complies with
* NBCC Academic Integrity Policy (policy 1111)
*/

package sudoku;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * @author Melanie Roy-Plommer
 */
public class AnchorManager extends ImageEntity{
    protected ArrayList<Point> anchors = new ArrayList<>();
    
    public AnchorManager(Dimension size, Point position, int orientation) {
        super(size, position, orientation);
    }
    
    public void addAnchor(Point anchor){
        anchors.add(anchor);
    }
    
    public Point anchorAtIndex(int index){
        return anchors.get(index);
    }
    
}
