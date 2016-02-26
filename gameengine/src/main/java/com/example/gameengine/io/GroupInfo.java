package com.example.gameengine.io;

import java.io.IOException;
import java.util.List;

/**
 * Created by rahul on 24/2/16.
 */
public interface GroupInfo {
    Integer getGroupVersion(Integer groupId) throws IOException;
    List<Integer> getUserIds(Integer groupId, Integer groupVersion) throws IOException;
}
