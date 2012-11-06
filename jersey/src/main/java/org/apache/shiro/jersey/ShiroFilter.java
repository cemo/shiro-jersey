/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shiro.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public abstract class ShiroFilter implements ResourceFilter, ContainerRequestFilter {
   
   public ShiroFilter(final AbstractMethod method) {}
   
   public ShiroFilter() {}
   
   /**
    * Checks if the current subject has all required permissions.
    */
   protected abstract boolean checkConditions();
   
   /**
    * If the user has sufficient permissions the request is executed. Otherwise
    * an exception is thrown which results in the HTTP status 403 (Forbidden).
    */
   public ContainerRequest filter(final ContainerRequest request) {
      if (checkConditions()) {
         return request;
      }
      throw new WebApplicationException(Response.Status.FORBIDDEN);
   }

   public ContainerRequestFilter getRequestFilter() {
      return this;
   }
   
   public ContainerResponseFilter getResponseFilter() {
      return null;
   }
   
}
