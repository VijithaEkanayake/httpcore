/*
 * $HeadURL$
 * $Revision$
 * $Date$
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.protocol;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;

/**
 */
public class TestBasicHttpProcessor extends TestCase {

    class TestHttpRequestInterceptorPlaceHolder implements HttpRequestInterceptor {
        
        public void process(
                HttpRequest request, 
                HttpContext context) throws HttpException, IOException {
        }
    }

    // ------------------------------------------------------------ Constructor
    public TestBasicHttpProcessor(String testName) {
        super(testName);
    }

    // ------------------------------------------------------------------- Main
    public static void main(String args[]) {
        String[] testCaseName = { TestHttpExecutionContext.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    // ------------------------------------------------------- TestCase Methods

    public static Test suite() {
        return new TestSuite(TestHttpExecutionContext.class);
    }

    public void testAddFirstRequestInterceptorNull() {
        HttpRequestInterceptor itcp = null;
        BasicHttpProcessor instance = new BasicHttpProcessor();

        instance.addRequestInterceptor(itcp, 0);
        int itcpCount = instance.getRequestInterceptorCount();
        assertEquals(0, itcpCount);
        assertEquals(null, instance.getRequestInterceptor(0));
    }

    public void testAddFirsRequestInterceptor() {
        HttpRequestInterceptor itcp1 = new HttpRequestInterceptor() {
            
            public void process(
                    HttpRequest request, 
                    HttpContext context) throws HttpException, IOException {
            }
            
        };
        HttpRequestInterceptor itcp2 = new HttpRequestInterceptor() {
            
            public void process(
                    HttpRequest request, 
                    HttpContext context) throws HttpException, IOException {
            }
            
        };
        BasicHttpProcessor instance = new BasicHttpProcessor();

        instance.addRequestInterceptor(itcp1);
        instance.addRequestInterceptor(itcp2, 0);
        int itcpCount = instance.getRequestInterceptorCount();
        assertEquals(2, itcpCount);
        assertEquals(itcp2, instance.getRequestInterceptor(0));
    }

    public void testAddTailRequestInterceptorNull() {
        HttpRequestInterceptor itcp = null;
        BasicHttpProcessor instance = new BasicHttpProcessor();

        instance.addRequestInterceptor(itcp, 0);
        int itcpCount = instance.getRequestInterceptorCount();
        assertEquals(0, itcpCount);
        assertEquals(null, instance.getRequestInterceptor(itcpCount - 1));
    }

    public void testAddTailRequestInterceptor() {
        HttpRequestInterceptor itcp1 = new HttpRequestInterceptor() {
            
            public void process(
                    HttpRequest request, 
                    HttpContext context) throws HttpException, IOException {
            }
            
        };
        HttpRequestInterceptor itcp2 = new HttpRequestInterceptor() {
            
            public void process(
                    HttpRequest request, 
                    HttpContext context) throws HttpException, IOException {
            }
            
        };
        BasicHttpProcessor instance = new BasicHttpProcessor();

        instance.addRequestInterceptor(itcp1);
        instance.addRequestInterceptor(itcp2, Integer.MAX_VALUE);
        int itcpCount = instance.getRequestInterceptorCount();
        assertEquals(2, itcpCount);
        assertEquals(itcp1, instance.getRequestInterceptor(0));
        assertEquals(itcp2, instance.getRequestInterceptor(itcpCount - 1));

        instance = new BasicHttpProcessor();
        instance.addRequestInterceptor(itcp1, -1);
        assertEquals(1, itcpCount);
        assertEquals(itcp1, instance.getRequestInterceptor(0));
        assertEquals(itcp1, instance.getRequestInterceptor(itcpCount - 1));
    }

    public void testAddRequestInterceptorMiddleIndex() {
        HttpRequestInterceptor itcp1 = new TestHttpRequestInterceptorPlaceHolder();
        BasicHttpProcessor instance = new BasicHttpProcessor();
        instance.addRequestInterceptor(itcp1, 2);
    }

    public void testClearByClass() {
        // remove a present class
        HttpRequestInterceptor itcp1 = new TestHttpRequestInterceptorPlaceHolder();
        HttpRequestInterceptor itcp2 = new TestHttpRequestInterceptorPlaceHolder();
        HttpRequestInterceptor itcp3 = new HttpRequestInterceptor() {
            
            public void process(
                    HttpRequest request, 
                    HttpContext context) throws HttpException, IOException {
            }
            
        };
        BasicHttpProcessor instance = new BasicHttpProcessor();
        instance.addRequestInterceptor(itcp1);
        instance.addRequestInterceptor(itcp2);
        instance.addRequestInterceptor(itcp3);
        instance.removeRequestInterceptorByClass(itcp1.getClass());
        assertEquals(1, instance.getRequestInterceptorCount());
        instance.removeRequestInterceptorByClass(itcp3.getClass());
        assertEquals(0, instance.getRequestInterceptorCount());

        // remove a not present class
        instance.addRequestInterceptor(itcp1);
        instance.addRequestInterceptor(itcp2);
        instance.removeRequestInterceptorByClass(Integer.class);
        assertEquals(2, instance.getRequestInterceptorCount());
    }

}
