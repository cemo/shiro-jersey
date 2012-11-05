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

/*******************************************************************************
 * Copyright (c) 2012 PE INTERNATIONAL AG.
 * All rights reserved.
 * 
 * Contributors:
 *    Jan Stamer - initial API and implementation
 *******************************************************************************/
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.WebApplicationException;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.junit.Test;

import com.sun.jersey.spi.container.ContainerRequest;

public class PermissionsFilterTest {

   @Test
   public void constructor() {
      PermissionsFilter filter = new PermissionsFilter("repository:read");
      assertNull(filter.getResponseFilter());
      assertEquals(filter, filter.getRequestFilter());
      assertNotNull(filter.getRequiredPermissions());
      assertEquals(1, filter.getRequiredPermissions().length);
   }
   
   @Test
   public void emptyPermissions() {
      PermissionsFilter filter = new PermissionsFilter();
      assertNotNull(filter.getRequiredPermissions());
      assertEquals(0, filter.getRequiredPermissions().length);
   }
   
   @Test
   public void filterWithPermission() {
      PermissionsFilter filter = mock(PermissionsFilter.class);
      doCallRealMethod().when(filter).filter(any(ContainerRequest.class));
      when(filter.isPermitted()).thenReturn(true);
      
      ContainerRequest request = mock(ContainerRequest.class);
      assertEquals(request, filter.filter(request));
   }
   
   @Test(expected = WebApplicationException.class)
   public void filterWithoutPermission() {
      PermissionsFilter filter = mock(PermissionsFilter.class);
      doCallRealMethod().when(filter).filter(any(ContainerRequest.class));
      when(filter.isPermitted()).thenReturn(false);
      
      ContainerRequest request = mock(ContainerRequest.class);
      assertEquals(request, filter.filter(request));
   }
   
   @Test
   public void isPermitted() {
      Runnable runnable = new Runnable() {
         
         @Override
         public void run() {
            PermissionsFilter filter = new PermissionsFilter("repository:read");
            
            Subject subject = mock(Subject.class);
            SubjectThreadState threadState = new SubjectThreadState(subject);
            threadState.bind();
            
            assertFalse(PermissionsFilter.isPermitted("repository:write"));
            assertFalse(filter.isPermitted());
         }
      };
      
      runnable.run();
   }

}
