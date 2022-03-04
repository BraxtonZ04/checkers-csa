
    import java.awt.*;

public class GameCanvas extends Canvas {
    private final int SLOT_SIZE = 50;

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }

                g.fillRect(i * SLOT_SIZE, j * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
            }
        }
    }
}

