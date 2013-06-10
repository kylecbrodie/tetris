package tertis.tetris.game.server;

import java.util.ArrayDeque;

/**
 * maintains a queue of the players connected
 */
@SuppressWarnings("serial")
public class PlayerQueue extends ArrayDeque<String> {
	
	/**
	 * Forces player name uniqueness
	 */
	@Override
	public boolean add(String s) {
		if(contains(s)) {
			return false;
		}
		return super.add(s);
	}
	
	public boolean reQueue() {
		if(pollFirst() != null) {
			return add(removeFirst());
		}
		return false;
	}
	
	public boolean hasCurrentPlayer() {
		return pollFirst() != null;
	}
	
	public String getCurrentPlayer() {
		return peekFirst();
	}

	public boolean isMyTurn(String player) {
		String first = pollFirst();
			return first != null && first == player;
	}
}