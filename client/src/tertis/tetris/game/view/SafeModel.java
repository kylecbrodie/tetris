package tertis.tetris.game.view;

import java.rmi.RemoteException;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.model.TetrisModel;
import tertis.tetris.game.server.PlayerQueue;

public class SafeModel implements TetrisModel {

	private final TetrisModel proxy;
	
	public SafeModel(TetrisModel m) {
		proxy = m;
	}
	
	@Override
	public void connect(TetrisView player) {
		try {
			proxy.connect(player);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect(TetrisView player) {
		try {
			proxy.disconnect(player);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isStopped() {
		try {
			return proxy.isStopped();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public int getScore() {
		try {
			return proxy.getScore();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public IntMatrix getViewBoard() {
		try {
			return proxy.getViewBoard();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IntMatrix getPreviewShape() {
		try {
			return proxy.getPreviewShape();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PlayerQueue getPlayerQueue() {
		try {
			return proxy.getPlayerQueue();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void left() {
		try {
			proxy.left();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			proxy.right();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rotate() {
		try {
			proxy.rotate();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void down() {
		try {
			proxy.down();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}