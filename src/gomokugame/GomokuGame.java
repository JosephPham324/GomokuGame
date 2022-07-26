package gomokugame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Pham Nhat Quang
 */
public class GomokuGame extends javax.swing.JFrame {

    public static int numRow = 30;
    public static int numCol = 30;
    private int side;
    Piece[][] map;
    private int turns;
    ArrayList<Piece> lineRed;
    ArrayList<Piece> lineBlack;
    boolean gameover;

    public int getSide() {
        return side;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void initPanel() {
        map = new Piece[numRow][numCol];
        this.gamePanel.setLayout(new GridLayout(numRow, numCol));
        this.gamePanel.removeAll();
        this.gamePanel.revalidate();
        this.gamePanel.repaint();
        gameover = false;
        lineRed = new ArrayList<>();
        lineBlack = new ArrayList<>();
        side = Piece.RED;
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                map[i][j] = new Piece(i, j, this);
                gamePanel.add(map[i][j]);
            }
        }
        turns = 0;
    }

    public void increaseTurn() {
        ++turns;
    }

    public boolean playable(int row, int col) {
        return (row!=0 && map[row - 1][col].isOccupied()) || (row!=0 && col!=0 && map[row - 1][col - 1].isOccupied()) || (row!=0 && col + 1 < numCol && map[row - 1][col + 1].isOccupied())
                || (col!=0 && map[row][col - 1].isOccupied()) || (col + 1 < numCol && map[row][col + 1].isOccupied())
                || (row + 1<numRow && col!=0 && map[row + 1][col - 1].isOccupied()) || (row + 1< numRow && map[row + 1][col].isOccupied()) || (row + 1<numRow && col + 1<numCol && map[row + 1][col + 1].isOccupied())
                || turns == 0;
    }

    public void flipSide() {
        this.side = Piece.BLACK - side;
    }

    public void drawWinningLine(int side, int lineType) {
        switch (side) {
            case Piece.BLACK:
                for (int i = 0; i < lineBlack.size(); i++) {
                    lineBlack.get(i).drawLine(lineType);
                }
                break;
            case Piece.RED:
                for (int i = 0; i < lineRed.size(); i++) {
                    lineRed.get(i).drawLine(lineType);
                }
                break;
        }
    }

    private ArrayList<Piece> countLeft(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = col; i > col - 5 && i >= 0; i--) {
            if (map[row][i].isOccupied() && map[row][i].isSameSide(side)) {
                localLine.add(map[row][i]);
            }
        }
        return localLine;
    }

    private ArrayList<Piece> countRight(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = col; i < col + 5 && i < numCol; i++) {
            if (map[row][i].isOccupied() && map[row][i].isSameSide(side)) {
                localLine.add(map[row][i]);
            }
        }
        return localLine;
    }

    private boolean countHorizontal(int row, int col, int side) {
        HashSet<Piece> localLine = new HashSet<>();
        localLine.addAll(countLeft(row, col, side));
        localLine.addAll(countRight(row, col, side));
        if (localLine.size() == 5) {
            switch (side) {
                case Piece.BLACK:
                    lineBlack.addAll(localLine);
                    drawWinningLine(side, Piece.HORIZONTAL_LINE);
                    break;
                case Piece.RED:
                    lineRed.addAll(localLine);
                    drawWinningLine(side, Piece.HORIZONTAL_LINE);
                    break;
            }
            return true;
        }
        return false;
    }

    private ArrayList<Piece> countUp(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row; i > row - 5 && i >= 0; i--) {
            if (map[i][col].isOccupied() && map[i][col].isSameSide(side)) {
                localLine.add(map[i][col]);
            }
        }
        return localLine;
    }

    private ArrayList<Piece> countDown(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row; i < row + 5 && i < numRow; i++) {
            if (map[i][col].isOccupied() && map[i][col].isSameSide(side)) {
                localLine.add(map[i][col]);
            }
        }
        return localLine;
    }

    private boolean countVertical(int row, int col, int side) {
        HashSet<Piece> localLine = new HashSet<>();
        localLine.addAll(countUp(row, col, side));
        localLine.addAll(countDown(row, col, side));
        if (localLine.size() == 5) {
            switch (side) {
                case Piece.BLACK:
                    lineBlack.addAll(localLine);
                    drawWinningLine(side, Piece.VERTICAL_LINE);
                    break;
                case Piece.RED:
                    lineRed.addAll(localLine);
                    drawWinningLine(side, Piece.VERTICAL_LINE);
                    break;
            }
            return true;
        }
        return false;
    }

    private ArrayList<Piece> countUpLeft(int row, int col, int turn) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row, j = col; i > row - 5 && j > col - 5 && i >= 0 && j >= 0; i--, j--) {
            if (map[i][j].isOccupied() && map[i][j].isSameSide(side)) {
                localLine.add(map[i][j]);
            }
        }
        return localLine;
    }

    private ArrayList<Piece> countDownRight(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row, j = col; i < row + 5 && j < col + 5 && i < numRow && j < numCol; i++, j++) {
            if (map[i][j].isOccupied() && map[i][j].isSameSide(side)) {
                localLine.add(map[i][j]);
            }
        }
        return localLine;
    }

    private boolean countCrossLine1(int row, int col, int side) {
        HashSet<Piece> localLine = new HashSet<>();
        localLine.addAll(countUpLeft(row, col, side));
        localLine.addAll(countDownRight(row, col, side));
        if (localLine.size() == 5) {
            switch (side) {
                case Piece.BLACK:
                    lineBlack.addAll(localLine);
                    drawWinningLine(side, Piece.CROSS_LINE1);
                    break;
                case Piece.RED:
                    lineRed.addAll(localLine);
                    drawWinningLine(side, Piece.CROSS_LINE1);
                    break;
            }
            return true;
        }
        return false;
    }

    private ArrayList<Piece> countUpRight(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row, j = col; i > row - 5 && j < col + 5 && i >= 0 && j < numCol; i--, j++) {
            if (map[i][j].isOccupied() && map[i][j].isSameSide(side)) {
                localLine.add(map[i][j]);
            }
        }
        return localLine;
    }

    private ArrayList<Piece> countDownLeft(int row, int col, int side) {
        ArrayList<Piece> localLine = new ArrayList();
        for (int i = row, j = col; i < row + 5 && j > col - 5 && i < numRow && j >= 0; i++, j--) {
            if (map[i][j].isOccupied() && map[i][j].isSameSide(side)) {
                localLine.add(map[i][j]);
            }
        }
        return localLine;
    }
    private boolean countCrossline2(int row, int col,int side){
        HashSet<Piece> localLine = new HashSet<>();
        localLine.addAll(countUpRight(row, col, side));
        localLine.addAll(countDownLeft(row, col, side));
        if (localLine.size() == 5) {
            switch (side) {
                case Piece.BLACK:
                    lineBlack.addAll(localLine);
                    drawWinningLine(side, Piece.CROSS_LINE2);
                    break;
                case Piece.RED:
                    lineRed.addAll(localLine);
                    drawWinningLine(side, Piece.CROSS_LINE2);
                    break;
            }
            return true;
        }
        return false;
    }

    public void countAllDirections(int row, int col, int side) {
        lineBlack = new ArrayList<>();
        lineRed = new ArrayList<>();
        if (countHorizontal(row, col, side) || countVertical(row, col, side)
                ||countCrossLine1(row, col, side)||countCrossline2(row, col, side)) {
            gameover = true;
            JOptionPane.showMessageDialog(this, "Game over");
        }
    }

    /**
     * Creates new form GomokuGame
     */
    public GomokuGame() {
        initComponents();
        this.setTitle("Gomoku Game");
        initPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();        //Get size of screen
        //Set size of JFRAME as a square proportional to screen height
        this.setSize((int) screenSize.getHeight(), (int) screenSize.getHeight());
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        reset = new javax.swing.JButton();
        gamePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Control Panel", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 3, 18))); // NOI18N

        reset.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        reset.setText("NEW GAME");
        reset.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        gamePanel.setBorder(null);
        gamePanel.setPreferredSize(new java.awt.Dimension(720, 720));
        gamePanel.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        initPanel();
    }//GEN-LAST:event_resetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GomokuGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GomokuGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GomokuGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GomokuGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GomokuGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gamePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton reset;
    // End of variables declaration//GEN-END:variables
}
