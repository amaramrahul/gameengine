package com.example.userapi.ws;

import com.example.userapi.group.GroupBean;
import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {
    @GET
    @JSONP
    @Path("/{groupId}")
    public GroupBean getGroup(@PathParam("groupId") Integer groupId) {
        System.out.println("Received group details request for groupId " + groupId);
        // Generate some random entry
        GroupBean groupBean = new GroupBean();
        groupBean.setId(groupId);
        groupBean.setVersion(12);
        groupBean.setName("group" + groupId);
        List<Integer> userIds = new ArrayList<>();
        for (int i = 1; i <= groupId; i++) {
            userIds.add(i);
        }
        groupBean.setUserIds(userIds);
        System.out.println("Sending response groupId: " + groupBean.getId() +
                ", version: " + groupBean.getVersion() + ", userIds: " + groupBean.getUserIds());
        return groupBean;
    }
}
