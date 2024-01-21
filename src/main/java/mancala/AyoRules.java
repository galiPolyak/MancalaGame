package mancala;
import java.util.List;

public class AyoRules extends GameRules {
    private static final long serialVersionUID = 1L;
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;
    private MancalaDataStructure struct = new MancalaDataStructure();

    public AyoRules() {
        super();
        struct = getDataStructure();
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {

        if (startPit < 1 || startPit > 12){
            throw new InvalidMoveException();
        } 

        final int stones = struct.getNumStones(startPit);

        if (stones == 0) {
            throw new InvalidMoveException();
        }else if (playerNum == 1 && startPit > 6 || playerNum == 2 && startPit <= 6) {
            throw new InvalidMoveException();
        } else { 
            setGameType("Ayo");  
            struct.setSkip(startPit);
            distributeStones(startPit);

            final int store = (playerNum == 1) ? 6 : 13;

            return getList().get(store).getStoneCount();
        }
    }


    private List<Countable> getList(){
        return struct.getData();
    }

    private boolean oppositeNotEmpty(final int stoppingPoint){
        final int oppositePit = 13 - stoppingPoint;
        return struct.getNumStones(oppositePit) != 0;
    }

    private boolean lastPitBelongsToPlayer(final int pit, final int playerNum) {
        return playerNum == 1 && pit >= 0 && pit <=5 || playerNum == 2 && pit >= 7 && pit <= 12;
    }

    private boolean isLastStoneInStore(final int pit, final int playerNum) {
        return playerNum == 1 && pit == 6 || playerNum == 2 && pit == 13;
    }

    private int iterateStones(int stones, final int opponentStorePos, Countable lastPit){
        int lastPitIndex = 0;

        for (int i = 0; i < stones; i++) {
            int currentPos = struct.getIteratorPosition() + 1;
            if (currentPos != opponentStorePos && currentPos != (struct.getSkip() - 1)) {
                lastPit = struct.next();
                lastPit.addStone();

                if (currentPos == 6 || currentPos == 13){
                    struct.setStore(getList().get(currentPos), getPlayerNum());
                }

            } else if (currentPos == opponentStorePos){
                currentPos = currentPos%13;
                lastPit = struct.next();
                lastPit.addStone();
            } else{
                lastPit = struct.next();
                if (i != 0) {
                    stones++;
                }
            }

            currentPos = currentPos%13;
            lastPitIndex = currentPos;
        }

        return lastPitIndex;
    }

    private int checkForBaseCase(final int lastPitIndex, final int lastIndexFixed, final int stones){
        if (isLastStoneInStore(lastPitIndex, getPlayerNum())) {
            
            if (getPlayerNum() == PLAYER_ONE){
                setPlayer(PLAYER_TWO);
            }else{
                setPlayer(PLAYER_ONE);
            }
            return stones;
        }

        if (struct.getNumStones(lastIndexFixed) == 1) {
            
            if (lastPitBelongsToPlayer(lastPitIndex, getPlayerNum()) 
            && oppositeNotEmpty(lastIndexFixed)){
                captureStones(lastIndexFixed);
            }

            if (getPlayerNum() == PLAYER_ONE){
                setPlayer(PLAYER_TWO);
            }else{
                setPlayer(PLAYER_ONE);
            }

            return stones;
        }

        if (struct.getNumStones(lastIndexFixed) >= 2){
            distributeStones(lastIndexFixed);
            
        }
        return stones;
        
    }


    @Override
    public int distributeStones(final int startPit) {
        
        final int stones = struct.removeStones(startPit);
        final int opponentStorePos = (getPlayerNum() == 1) ? 13 : 6;

        struct.setIterator(startPit, getPlayerNum(), true);
        final Countable lastPit = getList().get(startPit);

        final int lastPitIndex = iterateStones(stones, opponentStorePos, lastPit);
        final int fix = lastPitIndex <= 6 ? 1 : 0;
        final int lastIndexFixed = lastPitIndex + fix;

        return checkForBaseCase(lastPitIndex, lastIndexFixed, stones);

    }

    @Override
    public int captureStones(final int stoppingPoint){
        final int playerNum = stoppingPoint >= 1 && stoppingPoint <= 5 ? 1 : 2;
        final int whichStore = stoppingPoint >= 1 && stoppingPoint <= 5 ? 6 : 13;

        final int oppositePit = 13 - stoppingPoint;
        final int capturedStones = struct.removeStones(oppositePit);

        getList().get(whichStore).addStones(capturedStones);
        struct.setStore(getList().get(whichStore), playerNum); 

        return capturedStones;
    }
    
}