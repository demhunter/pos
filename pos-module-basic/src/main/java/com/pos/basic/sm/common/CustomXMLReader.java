/**
 *                      GNU Public License
 * Copyright (C) 2014 Free Software Foundation, Inc. <http://fsf.org>
 * 
 * This file is part of library EasyFSM.
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. This library can be redistributed
 * or used in case this license is copied as it is.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Author : Ankit
 * Report bugs to : hiiankit (at) gmail (dot) com
**/
package com.pos.basic.sm.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @file CustomXMLReader.java
 * <p>
 * This implementation implements all methods required to read 
 * fsm configuration from the XML file.
 * </p>
 * 
 * @version 1.00
 * @author ANKIT
 */
public class CustomXMLReader implements java.io.Serializable {
    
    /*
     * This field specifies the file name
     */
    private String _ConfigFileName;
    /*
     * This field specifies the file stream
     */
    private InputStream _ConfigFileStream;
    private File fXmlFile = null;
    private Document doc = null;
    
    /*
     * Section to mark the tags to be read from
     * the XML Configuration file
     */
    private final String __StateTag     = "STATE";
    private final String __FromTag      = "fromState";
    private final String __ActionTag    = "action";
    private final String __HandlerTag   = "handler";
    private final String __ToStateTag = "toState";
    
    /**
     * 
     * @param configFile
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public CustomXMLReader(String configFile) throws ParserConfigurationException, SAXException, IOException {
        this._ConfigFileName = configFile;
        this.fXmlFile = new File(this._ConfigFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(this.fXmlFile);
        doc.getDocumentElement().normalize();
    }
    
    /**
     * 
     * @param configFile
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public CustomXMLReader(InputStream configFile) throws ParserConfigurationException, SAXException, IOException {
        this._ConfigFileStream = configFile;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(this._ConfigFileStream);
        doc.getDocumentElement().normalize();
    }

    /*
     * Expected XML Format
     * <fsm>
     *  <STATE id="state">
     *      <MESSAGE id="message01" action="action01" nextState="next01">
     *      </MESSAGE>
     *      <MESSAGE id="message02" action="action02" nextState="next02">
     *      </MESSAGE>
     *  </STATE>
     * </fsm>
     */
    
    /**
     * 获取状态列表
     *
     * @return 状态列表
     */
    public ArrayList<String> getStates() {
        ArrayList<String> _a = new ArrayList<>();
        NodeList nList = this.doc.getElementsByTagName(this.__StateTag);
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                _a.add(((Element)nNode).getAttribute(this.__FromTag));
            }
        }
        return _a;
    }
    
    /**
     * 获取与stateId对应的状态元素节点
     *
     * @param stateId 状态的id，如<STATE id="XXX"/>
     * @return 元素节点
     */
    public Node getStateNode(String stateId) {
        Node _a = null;
        NodeList nList = this.doc.getElementsByTagName(this.__StateTag);
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE &&
               ((Element)nNode).getAttribute(__FromTag).equals(stateId)) {
                _a = nNode;
            }
        }
        return _a;
    }

    /**
     * 获取状态变更图（transition map）
     *
     * @param StateId 状态节点
     * @return 该状态节点下的变更图
     */
    public HashMap getStateInfo(String StateId) {
        HashMap _m = new HashMap();
        Element element = (Element)getStateNode(StateId);
        if( element == null) return _m;
        NodeList nList = element.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                _m.put(((Element)nNode).getAttribute(this.__ActionTag),
                                ((Element)nNode).getAttribute(this.__ToStateTag) + ":" +
                                ((Element)nNode).getAttribute(this.__HandlerTag));
            }
        }
        return _m;
    }
}
