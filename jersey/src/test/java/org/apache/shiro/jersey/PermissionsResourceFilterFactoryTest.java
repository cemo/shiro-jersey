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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Test;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResource;
public class PermissionsResourceFilterFactoryTest {

   @Test
   public void createWithoutAnnotations() {
      PermissionsResourceFilterFactory resourceFilter = new PermissionsResourceFilterFactory();
      AbstractMethod method = mock(AbstractMethod.class);
      AbstractResource resource = mock(AbstractResource.class);
      when(method.getResource()).thenReturn(resource);
      assertNull(resourceFilter.create(method));
   }
   
   @Test
   public void createWithResourceAnnotation() {
      PermissionsResourceFilterFactory resourceFilter = new PermissionsResourceFilterFactory();
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
   public void createWithMethodAnnotation() {
      PermissionsResourceFilterFactory resourceFilter = new PermissionsResourceFilterFactory();
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
   public void createWithResourceAndMethodAnnotation() {
      PermissionsResourceFilterFactory resourceFilter = new PermissionsResourceFilterFactory();
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

}
