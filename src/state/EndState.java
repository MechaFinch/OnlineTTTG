package state;

//TODO: write values
public enum EndState {
	IN_PROGRESS (0),
	
	HORIZONTAL_TOP_X (1),
	HORIZONTAL_TOP_O (2),
	
	HORIZONTAL_CENTER_X (3),
	HORIZONTAL_CENTER_O (4),
	
	HORIZONTAL_BOTTOM_X (5),
	HORIZONTAL_BOTTOM_O (6),
	
	VERTICAL_LEFT_X (5),
	VERTICAL_LEFT_O (6),
	
	VERTICAL_CENTER_X (7),
	VERTICAL_CENTER_O (8),
	
	VERTICAL_RIGHT_X (9),
	VERTICAL_RIGHT_O (10),
	
	DIAGONAL_LR_X (11),
	DIAGONAL_LR_O (12),
	
	DIAGONAL_RL_X (13),
	DIAGONAL_RL_O (14);
	
	int val;	//Integer version of the state
	
	EndState(int val){
		this.val = val;
	}
	
	public String getPlayer() {
		if(val == IN_PROGRESS.val) {
			return "";
		}
		
		return val % 2 == 1 ? "x" : "o";
	}
}
