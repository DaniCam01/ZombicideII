package engine;

import main.PanelGame;
import main.ServerGame;

public class GameLoop extends Thread {
	private static final int FPS = 30;
	private ServerGame serverGame;

	public GameLoop(ServerGame serverGame) {
		super();
		this.serverGame = serverGame;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			while (serverGame.isRunning) {
				long time = System.currentTimeMillis();

				if (!serverGame.isPause) {
					serverGame.update();
					serverGame.sendWorld();
				}
				// delay for each frame - time it took for one frame
				time = (1000 / FPS) - (System.currentTimeMillis() - time);

				if (time > 0) {
					try {
						Thread.sleep(time);
					} catch (Exception e) {
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
