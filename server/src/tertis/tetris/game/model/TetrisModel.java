package tertis.tetris.game.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.server.PlayerQueue;

public interface TetrisModel extends Remote {

	public boolean connect(String player) throws RemoteException;

	public void disconnect(String player) throws RemoteException;

	public boolean isStopped() throws RemoteException;
	
	public boolean isMyTurn(String player) throws RemoteException;
	
	public int getScore() throws RemoteException;

	public IntMatrix getViewBoard() throws RemoteException;

	public IntMatrix getPreviewShape() throws RemoteException;
	
	public PlayerQueue getPlayerQueue() throws RemoteException;

	public void left() throws RemoteException;

	public void right() throws RemoteException;

	public void rotate() throws RemoteException;

	public void down() throws RemoteException;
}