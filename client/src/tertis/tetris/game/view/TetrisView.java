package tertis.tetris.game.view;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tertis.tetris.game.model.TetrisModel;

/**
 * View interface of the Tetris game. It is the VIEW in M-VC pattern.
 */
public interface TetrisView extends Remote {

	/**
	 * Set the Tetris model of the view to establish the two-way association.
	 * This method will be invoked by TetirsModel.setView().
	 */
	void setModel(TetrisModel model) throws RemoteException;

	/**
	 * Notify the view that the main board has changed.
	 */
	void boardChanged() throws RemoteException;

	/**
	 * Notify the view that the score has changed.
	 */
	void scoreChanged() throws RemoteException;

	/**
	 * Notify the view that the preview piece has changed.
	 */
	void previewChanged() throws RemoteException;
	
	/**
	 * Notify the view that the player queue has changed
	 */
	void queueChanged() throws RemoteException;
	
	/**
	 * Notify the view that they are the primary player
	 */
	void yourTurn() throws RemoteException;

	/**
	 * Notify the view that there are rows will be deleted in the map.
	 */
	void rowsToDelete(int row[], int count) throws RemoteException;

	/**
	 * Notify the view that the game is over.
	 */
	void gameOver() throws RemoteException;
	
	/**
	 * gets users name
	 */
	String getName() throws RemoteException;
}