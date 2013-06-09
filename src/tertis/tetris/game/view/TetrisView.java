package tertis.tetris.game.view;

import tertis.tetris.game.model.TetrisModel;

/**
 * View interface of the Tetris game. It is the VIEW in M-VC pattern.
 */
public interface TetrisView {
	
	/**
	 * Set the Tetris model of the view to establish the two-way association.
	 * This method will be invoked by TetirsModel.setView().
	 */
	void setModel(TetrisModel model);

	/**
	 * Notify the view that the main map is changed.
	 */
	void boardChanged();

	/**
	 * Notify the view that the score is changed.
	 */
	void scoreChanged();

	/**
	 * Notify the view that the preview cube is changed.
	 */
	void previewChanged();

	/**
	 * Notify the view that there are rows will be deleted in the map.
	 */
	void rowsToDelete(int row[], int count);

	/**
	 * Notify the view that the game is over.
	 */
	void gameOver();
}