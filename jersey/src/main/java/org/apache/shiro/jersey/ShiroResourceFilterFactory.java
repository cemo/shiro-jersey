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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * Enforces security constraints of Apache Shiro in REST resources. Resources and methods can 
 * use the Shiro annotation {@link RequiresPermissions}. These permissions are then enforced
 * for each method call on the resource. If both resource and method are annotated all permissions 
 * need to be given. 
 * 
 * The implementation is similar to standard JAX-WS security mechanisms as supported by {@link RolesAllowedResourceFilterFactory}.
 * 
 * @see RequiresPermissions
 * @see RequiresRoles
 */
public class ShiroResourceFilterFactory implements ResourceFilterFactory {

   private static final Map<Class<? extends Annotation>, Class<? extends ShiroFilter>> FILTERS_BY_ANNOTATION = new HashMap<Class<? extends Annotation>, Class<? extends ShiroFilter>>();
   static {
      FILTERS_BY_ANNOTATION.put(RequiresPermissions.class, PermissionsFilter.class);
      FILTERS_BY_ANNOTATION.put(RequiresRoles.class, RolesFilter.class);
   }

   @Override
   public List<ResourceFilter> create(final AbstractMethod method) {
      final List<ResourceFilter> filters = new ArrayList<ResourceFilter>();
      for (Map.Entry<Class<? extends Annotation>, Class<? extends ShiroFilter>> entry : FILTERS_BY_ANNOTATION.entrySet()) {
         if (hasAnnotation(method, entry.getKey())) {
            try {
               final ShiroFilter filter = entry.getValue().getConstructor(AbstractMethod.class).newInstance(method);
               filters.add(filter);
            } catch (Exception e) {
               throw new IllegalStateException("Could not create filter for Shiro Annotation.", e);
            }
         }
      }

      if (filters.size() > 0) {
         return filters;
      }

      return null;
   }

   private boolean hasAnnotation(final AbstractMethod method, final Class<? extends Annotation> annotation) {
      return method.getAnnotation(annotation) != null || method.getResource().getAnnotation(annotation) != null;
   }

}
