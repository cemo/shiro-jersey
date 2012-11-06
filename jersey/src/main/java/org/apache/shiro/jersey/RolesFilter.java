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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.sun.jersey.api.model.AbstractMethod;

/**
 * Performs a security check for each request. This guarantees that requests can only be
 * executed if the subject has all required roles.
 */
public class RolesFilter extends ShiroFilter {
   
   /**
    * The permissions required to access a REST resource.
    */
   private Collection<String> requiredRoles;
   
   public RolesFilter(final AbstractMethod method) {
      super(method);
      final RequiresRoles methodRoles = method.getAnnotation(RequiresRoles.class);
      final RequiresRoles resourceRoles = method.getResource().getAnnotation(RequiresRoles.class);
      if (methodRoles.logical().equals(Logical.OR) || resourceRoles.logical().equals(Logical.OR)) {
         throw new IllegalArgumentException("RequiresRoles combined with OR is not supported yet.");
      }

      // Combine Roles on both resource and method.
      requiredRoles = new ArrayList<String>();
      if (resourceRoles != null) {
         for (String resourceRole : resourceRoles.value()) {
            requiredRoles.add(resourceRole);
         }
      }
      if (methodRoles != null) {
         for (String methodRole : methodRoles.value()) {
            requiredRoles.add(methodRole);
         }
      }
   }
   
   public RolesFilter(final String... requiredRoles) {
      this.requiredRoles = new ArrayList<String>();
      for (String requiredRole : requiredRoles) {
         this.requiredRoles.add(requiredRole);
      }
   }

   /**
    * Checks if the current subject has all required permissions.
    */
   protected boolean checkConditions() {
      return checkConditions(requiredRoles); 
   }

   protected static boolean checkConditions(final Collection<String> requiredRoles) {
      return SecurityUtils.getSubject().hasAllRoles(requiredRoles);
   }
   
   public Collection<String> getRequiredRoles() {
      return requiredRoles;
   }

   
}
