Apache Shiro Jersey Extension
============

Extension for Apache Shiro to work with [Sun Jersey] (http://jersey.java.net "Sun Jersey"). This extension enables Shiro annotations in the JAX-RS implementation [Sun Jersey] (http://jersey.java.net "Sun Jersey"). 

You can do the following with it: 
```java
@Path("/changelog") 
@RequiresPermissions("repository:read") 
public class ChangelogResourceImpl { 

   @POST 
   @Consumes(MediaType.APPLICATION_JSON) 
   @Path("/addObject") 
   @RequiresPermissions("repository:write") 
   public Response addObject(ObjectJson objectJson) { 
      someService.addObject(object); 
      return Response.ok().build(); 
   }

} 
```

If the user is not authenticated Http Status Code 401 is returned. If the user has insufficient privileges Status Code 403 is returned. 

Right now we've only added support for the annoation @RequiresPermissions. The other Shiro annoations could easily be added in the same fashion. Yet currently that's the only one we need.