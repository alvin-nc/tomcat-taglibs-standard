/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
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
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xalan" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
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
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Lotus
 * Development Corporation., http://www.lotus.com.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.taglibs.standard.tag.common.xml;

import javax.servlet.jsp.JspTagException;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.NodeList;

import org.apache.taglibs.standard.resources.Resources;

import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.dtm.ref.DTMManagerDefault;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.compiler.XPathParser;

/**
 * The methods in this class are convenience methods into the
 * low-level XPath API.
 * These functions tend to be a little slow, since a number of objects must be
 * created for each evaluation.  A faster way is to precompile the
 * XPaths using the low-level API, and then just use the XPaths
 * over and over.
 *
 * NOTE: In particular, each call to this method will create a new
 * XPathContext, a new DTMManager... and thus a new DTM. That's very
 * safe, since it guarantees that you're always processing against a
 * fully up-to-date view of your document. But it's also portentially
 * very expensive, since you're rebuilding the DTM every time. You should
 * consider using an instance of CachedXPathAPI rather than these static
 * methods.
 *
 * @see <a href="http://www.w3.org/TR/xpath">XPath Specification</a>
 * 
 */
public class JSTLXPathAPI extends XPathAPI {    
    /**
     * Use an XPath string to select a single node.
     * XPath namespace prefixes are resolved using the prefixResolver.
     *
     * @param contextNode The node to start searching from.
     * @param str A valid XPath string.
     * @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     * @return The first node found that matches the XPath, or null.
     *
     * @throws JspTagException
     */
    public static Node selectSingleNode(
    Node contextNode, String str, PrefixResolver prefixResolver)
    throws JspTagException {
        
        // Have the XObject return its result as a NodeSetDTM.
        NodeIterator nl = selectNodeIterator(contextNode, str, prefixResolver);
        
        // Return the first node, or null
        return nl.nextNode();
    }
    
    /**
     * Use an XPath string to select a single node.
     * XPath namespace prefixes are resolved using the prefixResolver.
     *
     * @param contextNode The node to start searching from.
     * @param str A valid XPath string.
     * @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     * @return The first node found that matches the XPath, or null.
     *
     * @throws JspTagException
     */
    public static Node selectSingleNode(
    Node contextNode, String str, PrefixResolver prefixResolver,
    XPathContext xpathSupport ) throws JspTagException {
        
        // Have the XObject return its result as a NodeSetDTM.
        NodeIterator nl = selectNodeIterator(contextNode, str, prefixResolver, xpathSupport);
        
        // Return the first node, or null
        return nl.nextNode();
    }
    
    /**
     *  Use an XPath string to select a nodelist.
     *  XPath namespace prefixes are resolved using PrefixResolver.
     *
     *  @param contextNode The node to start searching from.
     *  @param str A valid XPath string.
     *  @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     *  @return A NodeIterator, should never be null.
     *
     * @throws JspTagException
     */
    public static NodeIterator selectNodeIterator(
    Node contextNode, String str, PrefixResolver prefixResolver)
    throws JspTagException {
        
        // Execute the XPath, and have it return the result
        XObject list = eval(contextNode, str, prefixResolver, null);
        
        // Have the XObject return its result as a NodeSetDTM.
        return getNodeIterator(list);
    }
    
    /**
     *  Use an XPath string to select a nodelist.
     *  XPath namespace prefixes are resolved using PrefixResolver.
     *
     *  @param contextNode The node to start searching from.
     *  @param str A valid XPath string.
     *  @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     *  @return A NodeIterator, should never be null.
     *
     * @throws JspTagException
     */
    public static NodeIterator selectNodeIterator(
    Node contextNode, String str, PrefixResolver prefixResolver,
    XPathContext xpathSupport ) throws JspTagException {
        
        // Execute the XPath, and have it return the result
        XObject list = eval(contextNode, str, prefixResolver, xpathSupport);
        
        // Have the XObject return its result as a NodeSetDTM.
        return getNodeIterator(list);
    }
    
    /**
     *  Use an XPath string to select a nodelist.
     *  XPath namespace prefixes are resolved using the prefixResolver.
     *
     *  @param contextNode The node to start searching from.
     *  @param str A valid XPath string.
     *  @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     *  @return A NodeIterator, should never be null.
     *
     * @throws JspTagException
     */
    private static NodeList selectNodeList(
    Node contextNode, String str, PrefixResolver prefixResolver)
    throws JspTagException {
        // Execute the XPath, and have it return the result
        XObject list = eval(contextNode, str, prefixResolver, null);
        
        // Return a NodeList.
        return getNodeList(list);
    }
    
    /**
     *  Use an XPath string to select a nodelist.
     *  XPath namespace prefixes are resolved using the prefixResolver.
     *
     *  @param contextNode The node to start searching from.
     *  @param str A valid XPath string.
     *  @param prefixResolver The PrefixResolver using which prefixes in the XPath will be resolved to namespaces.
     *  @return A NodeIterator, should never be null.
     *
     * @throws JspTagException
     */
    public static NodeList selectNodeList(
    Node contextNode, String str, PrefixResolver prefixResolver,
    XPathContext xpathSupport ) 
    throws JspTagException 
    {        
        // Execute the XPath, and have it return the result
        XObject list = eval(contextNode, str, prefixResolver, xpathSupport);
        
        // Return a NodeList.
        return getNodeList(list);
    }
        
    /**
     * Returns a NodeIterator from an XObject.
     *  @param list The XObject from which a NodeIterator is returned.
     *  @return A NodeIterator, should never be null.
     *  @throws JspTagException
     */
    private static NodeIterator getNodeIterator(XObject list) 
    throws JspTagException {
        try {
            return list.nodeset();
        } catch (TransformerException ex) {
            throw new JspTagException(
                Resources.getMessage("XPATH_ERROR_XOBJECT", ex.getMessage()), ex);            
        }
    }        

    /**
     * Returns a NodeList from an XObject.
     *  @param list The XObject from which a NodeList is returned.
     *  @return A NodeList, should never be null.
     *  @throws JspTagException
     */
    static NodeList getNodeList(XObject list) 
    throws JspTagException {
        try {
            return list.nodelist();
        } catch (TransformerException ex) {
            throw new JspTagException(
                Resources.getMessage("XPATH_ERROR_XOBJECT", ex.getMessage()), ex);            
        }
    }        
    
    /**
     *   Evaluate XPath string to an XObject.
     *   XPath namespace prefixes are resolved from the namespaceNode.
     *   The implementation of this is a little slow, since it creates
     *   a number of objects each time it is called.  This could be optimized
     *   to keep the same objects around, but then thread-safety issues would arise.
     *
     *   @param contextNode The node to start searching from.
     *   @param str A valid XPath string.
     *   @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
     *   @param prefixResolver Will be called if the parser encounters namespace
     *                         prefixes, to resolve the prefixes to URLs.
     *   @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
     *   @see org.apache.xpath.objects.XObject
     *   @see org.apache.xpath.objects.XNull
     *   @see org.apache.xpath.objects.XBoolean
     *   @see org.apache.xpath.objects.XNumber
     *   @see org.apache.xpath.objects.XString
     *   @see org.apache.xpath.objects.XRTreeFrag
     *
     * @throws JspTagException
     */
    public static XObject eval(
    Node contextNode, String str, PrefixResolver prefixResolver,
    XPathContext xpathSupport) throws JspTagException {
        //System.out.println("eval of XPathContext params: contextNode:str(xpath)"+
        // ":prefixResolver:xpathSupport => " + contextNode + ":" + str + ":" +
        //  prefixResolver + ":" + xpathSupport );        
        try {
            if (xpathSupport == null) {
                return eval(contextNode, str, prefixResolver);
            }
            
            // Since we don't have a XML Parser involved here, install some default support
            // for things like namespaces, etc.
            // (Changed from: XPathContext xpathSupport = new XPathContext();
            //    because XPathContext is weak in a number of areas... perhaps
            //    XPathContext should be done away with.)
            // Create the XPath object.
            XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);
            
            // Execute the XPath, and have it return the result
            int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
            
            // System.out.println("Context Node id ( after getDTMHandlerFromNode) => " + ctxtNode );
            XObject xobj = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
            return xobj;
        } catch (TransformerException ex) {
            throw new JspTagException(
                Resources.getMessage("XPATH_ERROR_EVALUATING_EXPR", str, ex.getMessage()), ex);            
        } catch (IllegalArgumentException ex) {
            throw new JspTagException(
                Resources.getMessage("XPATH_ILLEGAL_ARG_EVALUATING_EXPR", str, ex.getMessage()), ex);            
        }
    }
}
