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
package com.pos.basic.sm.fsm;

import com.pos.basic.sm.action.FSMAction;
import com.pos.basic.sm.states.FSMStateAction;
import com.pos.basic.sm.states.FSMTransitionInfo;
import com.pos.basic.sm.states.FSMStates;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to allow creation of the fsm<br/>
 * 
 * This class allows developer to either specify the XML Configuration File or
 * can pass the input-stream of the XML Configuration File.<br/>
 * Configuration file's format is as follows:
 * 
 * @author ANKIT & modified by cc
 */
public class FSM implements java.io.Serializable {
    
    /*
     * Any fsm requires three things:
     * * states
     * * Messages
     * * Actions
     */
    private FSMStates _fsm;
    private transient FSMAction _action;
    private transient Object _sharedData;

    /**
     * 不需要默认的Action
     *
     * @param configName 状态机配置文件
     * @param context 上下文信息
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FSM(String configName, Object context)
            throws ParserConfigurationException, SAXException, IOException {
        this._fsm = new FSMStates(configName, !"".equals(configName));
        this._sharedData = context;
    }

    
    /**
     * Constructor allows to create an fsm from a specified file-name<br/>
     * and specified Actions along with Shared Data<br/>
     * 
     * @param configFName : Configuration file-name.
     * @param action      : Actions to be configured.
     * @param sharedData  : Specify any shared data required.
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FSM(String configFName, FSMAction action, Object sharedData) 
            throws ParserConfigurationException, SAXException, IOException {
            this(configFName, action);
            this._sharedData = sharedData;
    }
    
    /**
     * Constructor takes the default configuration file<br/>
     * Shall not be used in production environment<br/>
     * 
     * @param action FSMAction
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Deprecated
    public FSM(FSMAction action) 
            throws ParserConfigurationException, SAXException, IOException {
        this("",action);
    }
    
    /**
     * Constructor takes the default configuration file<br/>
     * Shall not be used in production environment<br/>
     * 
     * @param action FSMAction
     * @param sharedData Context Data
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Deprecated
    public FSM(FSMAction action, Object sharedData) 
            throws ParserConfigurationException, SAXException, IOException {
        this(action);
        this._sharedData = sharedData;
    }
    
    /**
     * Constructor takes the default configuration file<br/>
     * Shall not be used in production environment<br/>
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Deprecated
    public FSM() 
            throws ParserConfigurationException, SAXException, IOException {
        this("",null);
    }

    /**
     * Constructor allows to create a fsm from a specified Input-Stream<br/>
     * and specified Actions along with Shared data<br/>
     * 
     * @param configFStream Input Stream of the XML Configuration file.
     * @param action    Specified actions for the given fsm
     * @param sharedData Shared Data passed across in fsm
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FSM(InputStream configFStream, FSMAction action, Object sharedData) 
            throws ParserConfigurationException, SAXException, IOException {
        this._fsm = new FSMStates(configFStream);
        this._action = action;
        this._sharedData = sharedData;
    }

    /**
     * Constructor allows to create a fsm from a specified Input-Stream<br/>
     * and specified Actions<br/>
     * 
     * @param configFStream Input Stream of the XML Configuration file.
     * @param action    Specified actions for the given fsm
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FSM(InputStream configFStream, FSMAction action) 
            throws ParserConfigurationException, SAXException, IOException {
        this(configFStream, action, null);
    }

    /**
     * 不需要默认的Action
     *
     * @param configFStream 配置文件输入流
     * @param context 上下文信息
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FSM(InputStream configFStream, Object context)
            throws ParserConfigurationException, SAXException, IOException {
        this(configFStream, null, context);
    }

    /**
     * Method on receiving the Message Id, takes appropriate action<br/>
     * and on successful execution of the action Transitions to the new-state<br/>
     * as per the transition map.<br/>
     * 
     * @param recvdMsgId Received Message Id
     * 
     * @return Returns the Current State as String
     */
    public Object processFSM(String recvdMsgId) {
        Object _r;
        // 获取当前状态所对应的状态迁移图中与传入消息相匹配的迁移对象
        _r = this._fsm.getCurrentState().getNewTransitionMap().get(recvdMsgId);
        if ( null != _r) {
            boolean status = true;
            FSMAction act = ((FSMTransitionInfo)_r).getAction();
            if (act!=null) {
                /* If customized action is declared, call an entry function */
                act.entry(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
                status = act.action(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
            } else if (null != this._action) {
                // 正常情况下不使用状态机的默认处理动作
                status = this._action.action(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
            }

            if(status) {
                // 设置状态机的当前状态为迁移后的状态
                this._fsm.setCurrentState(((FSMTransitionInfo)_r).getNextState());

                if (act!=null) {
                    act.afterTransition(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
                }else if ( null != this._action) {
                    // 正常情况下不使用状态机的默认处理动作
                    this._action.afterTransition(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
                }
            }

            if (act!=null) {
                /* Exit function called irrespective of transition status */
                act.exit(_fsm.getCurrentState().getCurrentState(), ((FSMTransitionInfo) _r).getNextState(), this._sharedData);
            }
        }
        return _r;
    }

    /**
     * Method returns the current state of the fsm<br/>
     * 
     * @return Current state of the fsm
     */
    public String getCurrentState() { return this._fsm.getCurrentState().getCurrentState(); }
    
    /**
     * Method sets the shared data for the fsm<br/>
     * This method overwrites the previous shared data<br/>
     * 
     * @param data  Set shared data for the fsm.<br/>
     *              <b>Note:</b> Call to this function overwrites any previous shared data.
     */
    public void setShareData(Object data) { this._sharedData = data; }

    public void setAction(ArrayList<String> states, String message, 
            FSMAction act) {
        _fsm.setAction(states, message, act);
    }

    public void setAction(String state, String message, 
            FSMAction act) {
        setAction(new ArrayList(Arrays.asList(state)), message, act);
    }

    public void setAction(String message, FSMAction act) {
        _fsm.setAction(message, act);
    }

    public void setStatesBeforeTransition(String state, FSMStateAction act) {
        _fsm.setStateBeforeTransition(state, act);
    }
    
    public void setStatesBeforeTransition(ArrayList<String> states, 
            FSMStateAction act) {
        _fsm.setStateBeforeTransition(states, act);
    }

    public void setStatesBeforeTransition(FSMStateAction act) {
        ArrayList<String> l = null;
        _fsm.setStateBeforeTransition(l, act);
    }
    
    public void setStatesAfterTransition(String state, FSMStateAction act) {
        _fsm.setStateAfterTransition(state, act);
    }
    
    public void setStatesAfterTransition(ArrayList<String> states, 
            FSMStateAction act) {
        _fsm.setStateAfterTransition(states, act);
    }

    public void setStatesAfterTransition(FSMStateAction act) {
        ArrayList<String> l = null;
        _fsm.setStateAfterTransition(l, act);
    }

    /**
     * Method returns all states associated with the fsm<br/>
     * 
     * @return Returns all states of the fsm
     */
    public List getAllStates() { return _fsm.getAllStates(); }
 
    /**
     * 
     * @param act Default action method for the fsm
     */
    public void setDefaultFsmAction(FSMAction act) { _action = act; }

    /**
     * 设置状态机当前状态
     *
     * @param newStat 当前状态String
     */
    public void setCurrentState(String newStat) {
        this._fsm.setCurrentState(newStat);
    }
}
