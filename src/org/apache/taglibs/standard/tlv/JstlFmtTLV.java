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

package org.apache.taglibs.standard.tlv;

import java.io.*;
import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluator;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.apache.taglibs.standard.resources.Resources;

/**
 * <p>A SAX-based TagLibraryValidator for the JSTL i18n-capable formatting
 * library. Currently implements the following checks:</p>
 * 
 * <ul>
 *   <li>Expression syntax validation, with full support for
 *      &lt;jx:expressionLanguage&gt;</li>
 *   <li>Any <message> or <messageFormat> action with a 'messageArgs'
 *      attribute must not have any <messageArg> subtags as its direct
 *      children.</li>
 *   <li>Tag bodies that must either be empty or non-empty given
 *      particular attributes.</li>
 * </ul>
 * 
 * @author Shawn Bayern
 */
public class JstlFmtTLV extends JstlBaseTLV {

    //*********************************************************************
    // Implementation Overview

    /*
     * We essentially just run the page through a SAX parser, handling
     * the callbacks that interest us.  We collapse <jsp:text> elements
     * into the text they contain, since this simplifies processing
     * somewhat.  Even a quick glance at the implementation shows its
     * necessary, tree-oriented nature:  multiple Stacks, an understanding
     * of 'depth', and so on all are important as we recover necessary
     * state upon each callback.  This TLV demonstrates various techniques,
     * from the general "how do I use a SAX parser for a TLV?" to
     * "how do I read my init parameters and then validate?"  But also,
     * the specific SAX methodology was kept as general as possible to
     * allow for experimentation and flexibility.
     *
     * Much of the code and structure is duplicated from JstlCoreTLV.
     * An effort has been made to re-use code where unambiguously useful.
     * However, splitting logic among parent/child classes isn't
     * necessarily the cleanest approach when writing a parser like the
     * one we need.  I'd like to reorganize this somewhat, but it's not
     * a priority.
     */


    //*********************************************************************
    // Constants

    // tag names
    private final String MESSAGE = "message";
    private final String MESSAGE_ARG = "messageArg";
    private final String MESSAGE_FORMAT = "messageFormat";
    private final String MESSAGE_OR_MESSAGE_FORMAT = "message(Format)";
    private final String FORMAT_NUMBER = "formatNumber";
    private final String PARSE_NUMBER = "parseNumber";
    private final String PARSE_DATE = "parseDate";
    private final String EXPLANG = "expressionLanguage";
    private final String JSP_TEXT = "jsp:text";

    // attribute names
    private final String EVAL = "evaluator";
    private final String MESSAGE_KEY = "key";
    private final String MESSAGE_ARGS = "messageArgs";
    private final String VALUE = "value";


    //*********************************************************************
    // Contract fulfillment

    protected DefaultHandler getHandler() {
	return new Handler();
    }


    //*********************************************************************
    // SAX event handler

    /** The handler that provides the base of our implementation. */
    private class Handler extends DefaultHandler {

	// parser state
	private int depth = 0;
	private Stack expressionLanguage = new Stack();
	private Stack messageDepths = new Stack();
	private Stack messageHasMessageArgs = new Stack();
	private String lastElementName = null;
	private boolean bodyNecessary = false;
	private boolean bodyIllegal = false;

	public Handler() {
	    // "install" the default evaluator
	    String defaultEvaluator = JstlCoreTLVHelper.getEvaluatorName();
	    if (defaultEvaluator != null)
		expressionLanguage.push(defaultEvaluator);
	}

	// process under the existing context (state), then modify it
	public void startElement(
	        String ns, String ln, String qn, Attributes a) {

            // substitute our own parsed 'ln' if it's not provided
            if (ln == null)
                ln = getLocalPart(qn);

	    // for simplicity, we can ignore <jsp:text> for our purposes
	    // (don't bother distinguishing between it and its characters)
	    if (qn.equals(JSP_TEXT))
		return;

	    // check body-related constraint
	    if (bodyIllegal)
		fail(Resources.getMessage("TLV_ILLEGAL_BODY",
					  lastElementName));

	    // temporarily "install" new expression language if appropriate
	    if (isTag(qn, EXPLANG))
		expressionLanguage.push(a.getValue(EVAL));

	    // validate expression syntax if we need to
	    Set expAtts;
	    if (qn.startsWith(prefix + ":")
		    && (expAtts = (Set) config.get(ln)) != null) {
		for (int i = 0; i < a.getLength(); i++) {
		    String attName = a.getLocalName(i);
		    if (expAtts.contains(attName)) {
			if (expressionLanguage.empty())
			    fail("Unexpected failure to determine "
				+ "expression language.");
			String vMsg =
			    validateExpression(
				(String) expressionLanguage.peek(),
				ln,
				attName,
				a.getValue(i));
			if (vMsg != null)
			    fail(vMsg);
		    }
		}
	    }

            // validate attributes
            if (!hasNoInvalidScope(a))
                fail(Resources.getMessage("TLV_INVALID_ATTRIBUTE",
                    SCOPE, qn, a.getValue(SCOPE)));

	    // check invariants for <message> and <messageFormat>
	    if (isTag(qn, MESSAGE_ARG) && messageChild()
		&& ((Boolean) messageHasMessageArgs.peek()).booleanValue()) {
		/*
		 * we're a <messageArg> tag and the direct child of a
		 * <message> or <messageFormat> tag with a 'messageArgs'
		 * attribute, which is illegal
		 */
		fail(Resources.getMessage("TLV_ILLEGAL_PARAM",
	            prefix, MESSAGE_ARG, MESSAGE_OR_MESSAGE_FORMAT,
					  MESSAGE_ARGS));
	    }

	    // now, modify state

	    /*
	     * if we're in a <message> or <messageFormat>, record relevant
	     * state
	     */
	    if (isTag(qn, MESSAGE) || isTag(qn, MESSAGE_FORMAT)) {
		messageDepths.push(new Integer(depth));
		if (hasAttribute(a, MESSAGE_ARGS))
		    messageHasMessageArgs.push(new Boolean(true));
		else
		    messageHasMessageArgs.push(new Boolean(false));
	    }

	    // set up a check against illegal attribute/body combinations
	    bodyIllegal = false;
	    bodyNecessary = false;
	    if (isTag(qn, MESSAGE_ARG)
		    || isTag(qn, FORMAT_NUMBER)
		    || isTag(qn, PARSE_NUMBER)
		    || isTag(qn, PARSE_DATE)) {
		if (hasAttribute(a, VALUE))
		    bodyIllegal = true;
		else
		    bodyNecessary = true;
	    } else if (isTag(qn, MESSAGE) && !hasAttribute(a, MESSAGE_KEY)) {
		bodyNecessary = true;
	    } else if (isTag(qn, MESSAGE_FORMAT) && !hasAttribute(a, VALUE)) {
		bodyNecessary = true;
	    }

	    // record the most recent tag (for error reporting)
	    lastElementName = qn;
	    lastElementId = a.getValue("id");

	    // we're a new element, so increase depth
	    depth++;
	}

	public void characters(char[] ch, int start, int length) {

	    // ignore strings that are just whitespace
	    String s = new String(ch, start, length).trim();
	    if (s.equals(""))
		return;

	    // check and update body-related constraints
	    if (bodyIllegal)
		fail(Resources.getMessage("TLV_ILLEGAL_BODY",
					  lastElementName));
	    bodyNecessary = false;		// body is no longer necessary!
	}

	public void endElement(String ns, String ln, String qn) {

	    // consistently, we ignore JSP_TEXT
	    if (qn.equals(JSP_TEXT))
		return;

	    // handle body-related invariant
	    if (bodyNecessary)
		fail(Resources.getMessage("TLV_MISSING_BODY",
		    lastElementName));
	    bodyIllegal = false;	// reset: we've left the tag

	    // update <message>- or <messageFormat>-related state
	    if (isTag(qn, MESSAGE) || isTag(qn, MESSAGE_FORMAT)) {
		messageDepths.pop();
		messageHasMessageArgs.pop();
	    }

	    // update language state
	    if (isTag(qn, EXPLANG))
		expressionLanguage.pop();

	    // update our depth
	    depth--;
	}

	// are we directly under a <message> or <messageFormat>?
	private boolean messageChild() {
	    return (!messageDepths.empty()
		&& (depth - 1) == ((Integer) messageDepths.peek()).intValue());
	}
    }
}