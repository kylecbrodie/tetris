# Design Document

The game will be divided between client and server. Client is an applet loaded via a webpage, servers a headless app.

Major Components:
	- Renderer
	- Input Handler (client only)
	- Pieces Enum and Randomizer
	- Board
	- Synchronizer (Network)

Break down of each component:
	Renderer:
		Get the datastructure representing the board
		draw background of board
		draw UI
		interate through the board pieces and draw them
		draw the current piece

	Input Handler (client only):
		Mantain keymaps (real keys -> virtual commands [A->"Left"])
		Listen for keypresses
		Tell Synchronizer the virtual commands

	Pieces Enum and Randomizer:
		Maintains an Enum of all the possible pieces (7)
		Each piece has a datastructure to represent itself on the board
		Each pieces has data to draw it correctly (Rectangles and colours)

	Board:
		Maintains the datastructure representing the board
		Holds all pieces in play
		Holds and manipulates the current piece
		Checks for completed lines to clear them
		Maintains the score

	Synchronizer:
		Sends the board datastructure and current piece to the clients
		Get input from current player (client) via Input Handler
		maintain input and Board update synchronization
			(player presses left -> board moves current piece left -> send updated data to client)

