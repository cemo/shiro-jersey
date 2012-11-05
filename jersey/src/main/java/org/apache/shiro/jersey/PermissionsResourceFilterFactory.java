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
import java.util.Collections;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * Enforces security constraints of Apache Shiro in REST resources. Resources and methods can 
 * use the Shiro annotation {@link RequiresPermissions}. These permissions are then enforced
 * for each method call on the resource. If both resource and method are annotated all permissions 
 * need to be given. 
 * @see RequiresPermissions
 */
public class PermissionsResourceFilterFactory implements ResourceFilterFactory {

   @Override
   public List<ResourceFilter> create(final AbstractMethod method) {
      final RequiresPermissions methodPermissions = method.getAnnotation(RequiresPermissions.class);
      final RequiresPermissions resourcePermissions = method.getResource().getAnnotation(RequiresPermissions.class);

      // Combine permissions on both resource and method.
      String[] combinedPermissions = new String [] {};
      if (resourcePermissions != null) {
         combinedPermissions = concat(combinedPermissions, resourcePermissions.value());
      }
      if (methodPermissions != null) {
         combinedPermissions = concat(combinedPermissions, methodPermissions.value());
      }

      if (combinedPermissions.length > 0) {
         return Collections.<ResourceFilter>singletonList(createFilter(combinedPermissions));
      }

      return null;
   }

   protected ResourceFilter createFilter(final String[] allowedPermissions) {
      return new PermissionsFilter(allowedPermissions);
   }

   public static <T> T[] concat(T[] first, T[] second) {
      T[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }

}
