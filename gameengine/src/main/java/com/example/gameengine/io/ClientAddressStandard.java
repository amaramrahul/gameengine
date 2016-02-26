package com.example.gameengine.io;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;

/**
 * Created by rahul on 25/2/16.
 */
public class ClientAddressStandard implements ClientAddressORM {
    private static AerospikeClient aerospikeClient = new AerospikeClient("127.0.0.1", 3000);
    private Integer userId;
    public ClientAddressStandard(Integer userId) {
        this.userId = userId;
    }
    public String get() {
        WritePolicy policy = new WritePolicy();
        Key key = new Key("clientaddress", "", userId);
        Record record = aerospikeClient.get(policy, key);
        return record.getString("");
    }
    public void set(String address) {
        WritePolicy policy = new WritePolicy();
        Key key = new Key("clientaddress", "", userId);
        Bin bin = new Bin("", address);
        aerospikeClient.put(policy, key, bin);
    }

    public void remove() {
        WritePolicy policy = new WritePolicy();
        Key key = new Key("clientaddress", "", userId);
        aerospikeClient.delete(policy, key);
    }
}
