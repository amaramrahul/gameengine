package com.example.gameengine.game;

import com.example.gameengine.exception.InvalidGameMove;
import com.example.gameengine.io.GameORMStandard;
import com.example.gameengine.io.GameORM;
import com.example.gameengine.io.GroupInfo;
import com.example.gameengine.io.GroupInfoStandard;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by rahul on 23/2/16.
 */
public abstract class Game extends Observable {

    protected GameBean gameobj;
    protected final GroupInfo groupInfo;
    protected final GameORM gameORM;

    protected abstract GameEvent getGameEvent(Integer gameEventId);

    public abstract String getGameName();

    public Game() {
        gameobj = new GameBean();
        gameobj.setGameName(getGameName());
        gameobj.setGameId((int)Math.floor(Math.random() * 1000000) + 1000000);
        gameORM = new GameORMStandard(gameobj.getGameId()); // need to use better formula for ID
        groupInfo = new GroupInfoStandard();
    }

    public Game(GameORM gameORM) {
        gameobj = gameORM.getCached();
        this.gameORM = gameORM;
        groupInfo = new GroupInfoStandard();
    }

    public void load() {
        gameobj = gameORM.get();
    }

    public void persist() {
        gameORM.set(gameobj);
    }

    public void newGame(Integer userId, Map<String, Object> params) throws IOException,InvalidGameMove {
        Integer groupId = (Integer) params.get("groupId");
        gameobj.setGroupId(groupId);
        gameobj.setGroupVersion(groupInfo.getGroupVersion(groupId));
        GameEvent gameEvent = getGameEvent(GameEvent.NEW_EVENT);
        gameEvent.updateState(userId, params);
        persist();

        // Notify observers
        setChanged();
        notifyObservers("new");
    }

    public void moveGame(Integer userId, Map<String, Object> params) throws IOException,InvalidGameMove {
        GameEvent gameEvent = getGameEvent(GameEvent.MOVE_EVENT);
        gameEvent.updateState(userId, params);
        persist();

        // Notify observers
        setChanged();
        notifyObservers("move");
    }

    public void timeoutGame(Integer userId, Map<String, Object> params) throws IOException,InvalidGameMove {
        GameEvent gameEvent = getGameEvent(GameEvent.TIMEOUT_EVENT);
        gameEvent.updateState(userId, params);
        persist();

        // Notify observers
        setChanged();
        notifyObservers("timeout");
    }

    public Map<Object, Object> getState() {
        return gameobj.getStateStack().get(0);
    }

    public Integer getGameId() {
        return gameobj.getGameId();
    }

    public Integer getGroupId() {
        return gameobj.getGroupId();
    }

    public List<Integer> getUserIds() throws IOException {
        return groupInfo.getUserIds(gameobj.getGroupId(), gameobj.getGroupVersion());
    }

    public abstract Integer getTimeoutInterval();

    public void setState(Map<Object, Object> state) {
        gameobj.getStateStack().add(0, state);
    }
}
