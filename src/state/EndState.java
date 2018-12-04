package state;

//TODO: write values
public enum EndState {
	IN_PROGRESS (0),
	
	HORIZONTAL_TOP_X,
	HORIZONTAL_TOP_O,
	
	HORIZONTAL_CENTER_X,
	HORIZONTAL_CENTER_O,
	
	HORIZONTAL_BOTTOM_X,
	HORIZONTAL_BOTTOM_O,
	
	VERTICAL_LEFT_X,
	VERTICAL_LEFT_O,
	
	VERTICAL_CENTER_X,
	VERTICAL_CENTER_O,
	
	VERTICAL_RIGHT_X,
	VERTICAL_RIGHT_O,
	
	DIAGONAL_LR_X,
	DIAGONAL_LR_O,
	
	DIAGONAL_RL_X,
	DIAGONAL_RL_O;
	
	int val;	//Integer version of the state
	
	EndState(int val){
		this.val = val;
	}
	
	public String getPlayer() {
		
	}
}
