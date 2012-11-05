/*
 * Copyright (c) 2012, Jan Stamer. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.shiro.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Performs a security check for each request. This guarantees that requests can only be
 * executed if all required permissions are given.
 */
public class PermissionsFilter implements ResourceFilter, ContainerRequestFilter {
   
   /**
    * The permissions required to access a REST resource.
    */
   private final String[] requiredPermissions;

   public PermissionsFilter(final String... requiredPermissions) {
      this.requiredPermissions = requiredPermissions;
   }

   /**
    * If the user has sufficient permissions the request is executed. Otherwise
    * an exception is thrown which results in the HTTP status 403 (Forbidden).
    */
   public ContainerRequest filter(final ContainerRequest request) {
      if (isPermitted()) {
         return request;
      }
      throw new WebApplicationException(Response.Status.FORBIDDEN);
   }
   
   /**
    * Checks if the current subject has all required permissions.
    */
   protected boolean isPermitted() {
      return SecurityUtils.getSubject().isPermittedAll(requiredPermissions); 
   }

   protected static boolean isPermitted(final String... requiredPermissions) {
      return SecurityUtils.getSubject().isPermittedAll(requiredPermissions);
   }
   
   public String[] getRequiredPermissions() {
      return requiredPermissions.clone();
   }
   
   public ContainerRequestFilter getRequestFilter() {
      return this;
   }
   
   public ContainerResponseFilter getResponseFilter() {
      return null;
   }
   
}
