package tertis.tetris.game.server;

import java.rmi.RemoteException;
import java.util.LinkedList;

import tertis.tetris.game.view.TetrisView;

/**
 * maintains a queue of the players connected
 */
@SuppressWarnings("serial")
public class PlayerQueue extends LinkedList<TetrisView> {
	
	public boolean reQueue() {
		return add(removeFirst());
	}

	public void notifyPlayerTurn(boolean isYourTurn) {
		try {
			getFirst().yourTurn(isYourTurn);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void boardChanged() {
		for(TetrisView t : this) {
			try {
				t.boardChanged();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void scoreChanged() {
		for(TetrisView t : this) {
			try {
				t.scoreChanged();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void previewChanged() {
		for(TetrisView t : this) {
			try {
				t.previewChanged();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void rowsToDelete(int[] todelete, int count) {
		for(TetrisView t : this) {
			try {
				t.rowsToDelete(todelete, count);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void gameOver() {
		for(TetrisView t : this) {
			try {
				t.gameOver();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}