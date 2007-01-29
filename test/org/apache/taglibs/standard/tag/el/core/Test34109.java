/*                                                                             
 * Copyright 1999,2004 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */

package org.apache.taglibs.standard.tag.el.core;

import javax.servlet.jsp.*;
import org.apache.cactus.*;
import org.apache.taglibs.standard.testutil.TestUtil;

public class Test34109 extends JspTestCase {

    public Test34109(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void test34109() throws Exception {
	String toInclude = TestUtil.getTestJsp(this);
	pageContext.include(toInclude);

	String genurl = (String) pageContext.getAttribute("genurl", PageContext.APPLICATION_SCOPE);

	assertEquals("The URL should be /foo/test.jsp", "/foo/test.jsp", genurl);
    }
}
