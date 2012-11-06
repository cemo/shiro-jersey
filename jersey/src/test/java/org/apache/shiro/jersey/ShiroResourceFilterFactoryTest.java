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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.junit.Test;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResource;
public class ShiroResourceFilterFactoryTest {

   @Test
   public void createWithoutAnnotations() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      assertNull(resourceFilter.create(method));
   }
   
   @Test
   public void createWithResourceAnnotationRequiresPermissions() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresPermissions permissions = mock(RequiresPermissions.class);
      String [] requiredPermissions = new String [] {"repository:read"};
      when(permissions.value()).thenReturn(requiredPermissions);
      when(resource.getAnnotation(eq(RequiresPermissions.class))).thenReturn(permissions);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      PermissionsFilter filter = (PermissionsFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(requiredPermissions, filter.getRequiredPermissions());
   }
   
   @Test
   public void createWithMethodAnnotationRequiresPermissions() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresPermissions permissions = mock(RequiresPermissions.class);
      String [] requiredPermissions = new String [] {"repository:read"};
      when(permissions.value()).thenReturn(requiredPermissions);
      when(method.getAnnotation(eq(RequiresPermissions.class))).thenReturn(permissions);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      PermissionsFilter filter = (PermissionsFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(requiredPermissions, filter.getRequiredPermissions());
   }
   
   @Test
   public void createWithResourceAndMethodAnnotationRequiresPermissions() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresPermissions permissionsResource = mock(RequiresPermissions.class);
      String [] requiredPermissionsResource = new String [] {"repository:read"};
      when(permissionsResource.value()).thenReturn(requiredPermissionsResource);
      when(resource.getAnnotation(eq(RequiresPermissions.class))).thenReturn(permissionsResource);
      
      RequiresPermissions permissionsMethod = mock(RequiresPermissions.class);
      String [] requiredPermissionsMethod = new String [] {"repository:write"};
      when(permissionsMethod.value()).thenReturn(requiredPermissionsMethod);
      when(method.getAnnotation(eq(RequiresPermissions.class))).thenReturn(permissionsMethod);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      PermissionsFilter filter = (PermissionsFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(new String [] {"repository:read", "repository:write"}, filter.getRequiredPermissions());
   }
   
   @Test
   public void createWithResourceAnnotationRequiresRoles() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresRoles Roles = mock(RequiresRoles.class);
      String [] requiredRoles = new String [] {"Admin"};
      when(Roles.value()).thenReturn(requiredRoles);
      when(resource.getAnnotation(eq(RequiresRoles.class))).thenReturn(Roles);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      RolesFilter filter = (RolesFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(requiredRoles, filter.getRequiredRoles().toArray(new String [filter.getRequiredRoles().size()]));
   }
   
   @Test
   public void createWithMethodAnnotationRequiresRoles() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresRoles Roles = mock(RequiresRoles.class);
      String [] requiredRoles = new String [] {"Admin"};
      when(Roles.value()).thenReturn(requiredRoles);
      when(method.getAnnotation(eq(RequiresRoles.class))).thenReturn(Roles);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      RolesFilter filter = (RolesFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(requiredRoles, filter.getRequiredRoles().toArray(new String [filter.getRequiredRoles().size()]));
   }
   
   @Test
   public void createWithResourceAndMethodAnnotationRequiresRoles() {
      ShiroResourceFilterFactory resourceFilter = new ShiroResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      
      RequiresRoles RolesResource = mock(RequiresRoles.class);
      String [] requiredRolesResource = new String [] {"Admin"};
      when(RolesResource.value()).thenReturn(requiredRolesResource);
      when(resource.getAnnotation(eq(RequiresRoles.class))).thenReturn(RolesResource);
      
      RequiresRoles RolesMethod = mock(RequiresRoles.class);
      String [] requiredRolesMethod = new String [] {"Chief"};
      when(RolesMethod.value()).thenReturn(requiredRolesMethod);
      when(method.getAnnotation(eq(RequiresRoles.class))).thenReturn(RolesMethod);
      
      assertNotNull(resourceFilter.create(method));
      assertEquals(1, resourceFilter.create(method).size());
      RolesFilter filter = (RolesFilter) resourceFilter.create(method).iterator().next();
      assertArrayEquals(new String [] {"Admin", "Chief"}, filter.getRequiredRoles().toArray(new String [filter.getRequiredRoles().size()]));
   }

}
