package tertis.tetris.game.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import tertis.tetris.game.model.ServerTetrisModel;
import tertis.tetris.game.model.TetrisModel;

public class TetrisServer {

	public static void main(String[] args) {
        try {
        	String name = "tetrisModel";
        	TetrisModel model = new ServerTetrisModel(20, 10);
        	
            TetrisModel stub = (TetrisModel) UnicastRemoteObject.exportObject(model, 8080);
            
            LocateRegistry.createRegistry(1099);
            
            Naming.rebind("//ec2-50-112-190-58.us-west-2.compute.amazonaws.com/" + name, stub);
            
            System.out.println("Server Online!");
        } catch (Exception e) {
            System.err.println("TetrisServer exception:");
            e.printStackTrace();
        }
	}
}