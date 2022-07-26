package gomokugame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Pham Nhat Quang
 */
public class Piece extends JPanel{

    private int row, col;

    /**
     *
     */
    public static final int RED = 0;

    /**
     *
     */
    public static final int BLACK = 1;
    private GomokuGame parent;
    private MouseListener mouseClicked;
    private Graphics g;
    private boolean drawCircle, drawX;
    private boolean occupied;
    private int side;
    private int drawLine;

    /**
     *
     * @param row
     * @param col
     * @param parent
     */
    public Piece(int row, int col, GomokuGame parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        drawCircle = false;
        drawX = false;
        occupied = false;
        drawLine = -1;
        this.mouseClicked = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardClicked();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                changeCursor();
            }

        };
        this.addMouseListener(mouseClicked);
    }

    /**
     *
     */
    public Piece() {
    }

    /**
     *
     */
    public void changeCursor() {
        if (parent.isGameover()) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     *
     */
    public void cardClicked() {
        if (parent.playable(row, col) && !parent.isGameover()) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            switch (parent.getSide()) {
                case RED:
                    drawCircle = true;
                    break;
                case BLACK:
                    drawX = true;
            }
            occupied = true;
            side = parent.getSide();
            parent.countAllDirections(row, col, side);
            parent.flipSide();
            parent.increaseTurn();
            this.removeMouseListener(mouseClicked);
            repaint();
        }
    }

    /**
     *
     * @return
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     *
     * @param side
     * @return
     */
    public boolean isSameSide(int side) {
        return this.side == side;
    }

    /**
     * |<br>
     * |<br>
     * |<br>
     */
    public static final int VERTICAL_LINE = 0;

    /**
     * ---
     */
    public static final int HORIZONTAL_LINE = 1;

    /**
     * \<br>
     *  &nbsp\<br>
     *   &nbsp&nbsp\<br>
     */
    public static final int CROSS_LINE1 = 2;

    /**
     *  &nbsp&nbsp/<br>
     *  &nbsp/<br>
     * /  <br>
     */
    public static final int CROSS_LINE2 = 3;

    /**
     *
     * @param type
     */
    public void drawLine(int type) {
        this.drawLine = type;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(2));
        this.setBackground(Color.white);
        this.setBorder(new LineBorder(Color.black));
        if (drawCircle) {
            g2d.setColor(Color.RED);
            g2d.drawOval(2, 2, this.size().width - 4, this.getSize().height - 4);
        }

        if (drawX) {
            g2d.setColor(Color.BLACK);
            g2d.drawLine(0, 0, this.size().width, this.getSize().height);
            g2d.drawLine(0, this.size().height, this.getSize().width, 0);
        }
        if (drawLine!=-1){
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.GREEN);
        switch (drawLine) {
            case VERTICAL_LINE:
                g2d.drawLine(this.getSize().width / 2, 0, this.getSize().width / 2, this.getSize().height);
                break;
            case HORIZONTAL_LINE:
                g2d.drawLine(0, this.getSize().height / 2, this.getSize().width, this.getSize().height / 2);
                break;
            case CROSS_LINE1:
                g2d.drawLine(0, 0, this.size().width, this.getSize().height);
                break;
            case CROSS_LINE2:
                g2d.drawLine(0, this.size().height, this.getSize().width, 0);
                break;
        }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.col != other.col) {
            return false;
        }
        return true;
    }

    
    
    
}
