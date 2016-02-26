package com.example.gameengine.io;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.example.gameengine.common.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public class GroupInfoStandard implements GroupInfo {
    private static AerospikeClient aerospikeClient = new AerospikeClient("127.0.0.1", 3000);

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String USERAPI_ENDPOINT = "http://127.0.0.1:8080/";

    public Integer getGroupVersion(Integer groupId) throws IOException {
        String response = Utils.sendHttpGetRequest(
                new URL(new URL(USERAPI_ENDPOINT), "/group/" + groupId));
        Map<String, Object> groupInfo = mapper.readValue(response, Map.class);
        return (Integer) groupInfo.get("version");
    }

    public List<Integer> getUserIds(Integer groupId, Integer groupVersion) throws IOException {
        List<Integer> userIds = null;

        WritePolicy policy = new WritePolicy();
        Key key = new Key("groupinfo", "", groupId+"_"+groupVersion);
        Record record = aerospikeClient.get(policy, key);
        if (record == null) {
            String response = Utils.sendHttpGetRequest(
                    new URL(new URL(USERAPI_ENDPOINT), "/group/" + groupId + "?version=" + groupVersion));
            Map<String, Object> groupInfo = mapper.readValue(response, Map.class);
            userIds = (ArrayList<Integer>) groupInfo.get("userIds");
            Bin bin = new Bin("", userIds);
            aerospikeClient.put(policy, key, bin);
        } else {
            userIds = new ArrayList<Integer>();
            for (Long uid: (List<Long>) record.getList("")) {
                userIds.add(Integer.parseInt(uid.toString()));
            }
        }

        return userIds;
    }

    public static void main(String[] args) throws IOException {
        GroupInfo groupInfo = new GroupInfoStandard();
        System.out.println(groupInfo.getGroupVersion(2));
        System.out.println(groupInfo.getUserIds(2, 12));
    }
}
