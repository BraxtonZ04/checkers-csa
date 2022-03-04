
    import javax.swing.*;
    
    public class Main {
        private static final JFrame mainFrame = new JFrame("Checkers");
        private static final GameCanvas gameCanvas = new GameCanvas();
    
        public static void main(String[] args) {
            mainFrame.setSize(500, 500);
            mainFrame.add(gameCanvas);
    
            mainFrame.setVisible(true);
        }
    }
   
