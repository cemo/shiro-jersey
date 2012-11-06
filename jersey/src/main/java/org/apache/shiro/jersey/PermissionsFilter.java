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

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.sun.jersey.api.model.AbstractMethod;

/**
 * Performs a security check for each request. This guarantees that requests can only be
 * executed if all required permissions are given.
 */
public class PermissionsFilter extends ShiroFilter {

   /**
    * The permissions required to access a REST resource.
    */
   private String[] requiredPermissions;
   
   public PermissionsFilter(final AbstractMethod method) {
      super(method);
      final RequiresPermissions methodPermissions = method.getAnnotation(RequiresPermissions.class);
      final RequiresPermissions resourcePermissions = method.getResource().getAnnotation(RequiresPermissions.class);

      // Combine permissions on both resource and method.
      requiredPermissions = new String [] {};
      if (resourcePermissions != null) {
         requiredPermissions = concat(requiredPermissions, resourcePermissions.value());
      }
      if (methodPermissions != null) {
         requiredPermissions = concat(requiredPermissions, methodPermissions.value());
      }
   }
   
   public PermissionsFilter(final String... requiredPermissions) {
      this.requiredPermissions = requiredPermissions;
   }

   /**
    * Checks if the current subject has all required permissions.
    */
   protected boolean checkConditions() {
      return checkConditions(requiredPermissions); 
   }

   protected static boolean checkConditions(final String... requiredPermissions) {
      return SecurityUtils.getSubject().isPermittedAll(requiredPermissions);
   }

   public String[] getRequiredPermissions() {
      return requiredPermissions.clone();
   }

   private static <T> T[] concat(T[] first, T[] second) {
      T[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }

}
