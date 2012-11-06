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

import java.util.Collections;

import javax.ws.rs.WebApplicationException;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.junit.Test;

import com.sun.jersey.spi.container.ContainerRequest;

public class RolesFilterTest {

   @Test
   public void constructor() {
      RolesFilter filter = new RolesFilter("Admin");
      assertNull(filter.getResponseFilter());
      assertEquals(filter, filter.getRequestFilter());
      assertNotNull(filter.getRequiredRoles());
      assertEquals(1, filter.getRequiredRoles().size());
   }
   
   @Test
   public void emptyRoles() {
      RolesFilter filter = new RolesFilter();
      assertNotNull(filter.getRequiredRoles());
      assertEquals(0, filter.getRequiredRoles().size());
   }
   
   @Test
   public void filterWithPermission() {
      RolesFilter filter = mock(RolesFilter.class);
      doCallRealMethod().when(filter).filter(any(ContainerRequest.class));
      when(filter.checkConditions()).thenReturn(true);
      
      ContainerRequest request = mock(ContainerRequest.class);
      assertEquals(request, filter.filter(request));
   }
   
   @Test(expected = WebApplicationException.class)
   public void filterWithoutPermission() {
      RolesFilter filter = mock(RolesFilter.class);
      doCallRealMethod().when(filter).filter(any(ContainerRequest.class));
      when(filter.checkConditions()).thenReturn(false);
      
      ContainerRequest request = mock(ContainerRequest.class);
      assertEquals(request, filter.filter(request));
   }
   
   @Test
   public void checkConditions() {
      Runnable runnable = new Runnable() {
         
         @Override
         public void run() {
            RolesFilter filter = new RolesFilter("Admin");
            
            Subject subject = mock(Subject.class);
            SubjectThreadState threadState = new SubjectThreadState(subject);
            threadState.bind();
            
            assertFalse(RolesFilter.checkConditions(Collections.<String>singletonList("Chief")));
            assertFalse(filter.checkConditions());
         }
      };
      
      runnable.run();
   }

}
