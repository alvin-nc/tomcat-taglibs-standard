/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 

package org.apache.taglibs.standard.tag.common.fmt;

import java.util.*;
import java.text.*;
import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.apache.taglibs.standard.resources.Resources;

/**
 * Support for tag handlers for &lt;formatNumber&gt;, the number
 * formatting tag in JSTL 1.0.
 *
 * @author Jan Luehe
 */

public abstract class FormatNumberSupport extends TagSupport {

    //*********************************************************************
    // Public constants

    public static final String NUMBER_STRING = "number";    
    public static final String CURRENCY_STRING = "currency";
    public static final String PERCENT_STRING = "percent";


    //*********************************************************************
    // Package-scoped constants

    static final int NUMBER_TYPE = 0;
    static final int CURRENCY_TYPE = 1;
    static final int PERCENT_TYPE = 2;


    //*********************************************************************
    // Protected state

    protected Object value;                      // 'value' attribute
    protected String pattern;                    // 'pattern' attribute


    //*********************************************************************
    // Private state

    private int type;                            // 'type' attribute
    private String var;                          // 'var' attribute
    private int scope;                           // 'scope' attribute


    //*********************************************************************
    // Constructor and initialization

    public FormatNumberSupport() {
	super();
	init();
    }

    private void init() {
	pattern = var = null;
	value = null;
	type = NUMBER_TYPE;
	scope = PageContext.PAGE_SCOPE;
    }


   //*********************************************************************
    // Tag attributes known at translation time

    public void setType(String type) {
	if (CURRENCY_STRING.equalsIgnoreCase(type))
	    this.type = CURRENCY_TYPE;
	else if (PERCENT_STRING.equalsIgnoreCase(type))
	    this.type = PERCENT_TYPE;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
	this.scope = Util.getScope(scope);
    }


    //*********************************************************************
    // Tag logic

    public int doEndTag() throws JspException {
	NumberFormat formatter = null;

	Locale locale = LocaleSupport.getFormattingLocale(
            pageContext,
	    this,
	    NumberFormat.getAvailableLocales());

	/*
	 * If the value given is a string literal, it is first parsed into an
	 * instance of java.lang.Number according to the default pattern of
	 * the page's locale.
	 */
	if (value instanceof String) {
	    formatter = NumberFormat.getNumberInstance(locale);
	    try {
		value = formatter.parse((String) value);
	    } catch (ParseException pe) {
		throw new JspTagException(pe.getMessage());
	    }
	}

	switch (type) {
	case NUMBER_TYPE:
	    if (formatter == null)
		formatter = NumberFormat.getNumberInstance(locale);
	    if (pattern != null) {
		DecimalFormat df = (DecimalFormat) formatter;
		df.applyPattern(pattern);
	    }
	    break;
	case CURRENCY_TYPE:
	    formatter = NumberFormat.getCurrencyInstance(locale);
	    break;
	case PERCENT_TYPE:
	    formatter = NumberFormat.getPercentInstance(locale);
	    break;
	} // switch

	String formatted = formatter.format(value);
	if (var != null) {
	    pageContext.setAttribute(var, formatted, scope);	
	} else {
	    try {
		pageContext.getOut().print(formatted);
	    } catch (IOException ioe) {
		throw new JspTagException(ioe.getMessage());
	    }
	}

	return EVAL_PAGE;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
	init();
    }
}
