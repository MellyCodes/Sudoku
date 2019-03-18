/**
* @file PuzzleManager.java
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

/**
 * 
 * @author Melanie Roy-Plommer
 */
public class PuzzleManager {

    private final Puzzle currentPuzzle;
    
    public PuzzleManager(){
        String puzzleString = "2 4 8 5 3 9 6 7 1 6 7 5 4 1 2 8 9 3 3 9 1 6 7 8 2 4 5 7 1 2 8 6 3 9 5 4 4 3 6 1 9 5 7 8 2 5 8 9 2 4 7 1 3 6 8 2 4 9 5 6 3 1 7 9 5 7 3 2 1 4 6 8 1 6 3 7 8 4 5 2 9";
        String maskString = "1 1 0 0 0 1 1 1 1 1 1 0 1 0 0 1 1 1 0 0 0 1 1 1 1 1 1 0 1 1 1 1 0 1 0 1 0 0 1 1 1 1 1 0 0 1 0 1 0 1 1 1 1 0 1 1 1 1 1 1 0 0 0 1 1 1 0 0 1 0 1 1 1 1 1 1 0 0 0 1 1";

        currentPuzzle = new Puzzle(puzzleString, maskString);
    }
 
    
    public Puzzle getCurrentPuzzle(){    
        return currentPuzzle;
    }
}
