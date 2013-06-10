package tertis.tetris.game.server;

import tertis.tetris.game.model.ServerTetrisModel;
import tertis.tetris.game.model.TetrisModel;

public class TetrisServer {

	public static void main(String[] args) {
		TetrisModel model = new ServerTetrisModel(20, 10);
		//TODO set up Java RMI
	}
}