package tertis.tetris.game.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import tertis.tetris.game.model.ServerTetrisModel;
import tertis.tetris.game.model.TetrisModel;

public class TetrisServer {

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
        	String name = "tetrisModel";
        	TetrisModel model = new ServerTetrisModel(20, 10);
            TetrisModel stub = (TetrisModel) UnicastRemoteObject.exportObject(model, 0);
            Registry newR = LocateRegistry.createRegistry(1099);
            newR.rebind(name, stub);
            System.out.println("TetrisModel bound");
        } catch (Exception e) {
            System.err.println("TetrisServer exception:");
            e.printStackTrace();
        }
	}
}