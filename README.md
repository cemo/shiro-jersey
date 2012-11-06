Apache Shiro Jersey Extension
============

Extension for Apache Shiro to work with [Sun Jersey] (http://jersey.java.net "Sun Jersey"). This extension enables Shiro annotations in the JAX-RS implementation [Sun Jersey] (http://jersey.java.net "Sun Jersey"). 

If the user is not authenticated Http Status Code 401 is returned. If the user has insufficient privileges Status Code 403 is returned. This extension provides security using Apache Shiro 
similar to the standard JAX-WS-RS security for Jersey.

The following Shiro Annotations are supported:
+ RequiresPermissions
+ RequiresRoles (combined with AND only)

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

