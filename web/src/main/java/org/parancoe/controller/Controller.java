// Copyright 2006 The Parancoe Team
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.parancoe.controller;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.parancoe.session.SessionBean;
import org.parancoe.utility.ProfileHandler;


public interface Controller {

  public String getLayout();

  public void setLayout(String layout);
  
  public String getContent();
  
  public void setContent(String content);

  public BeanHandler getBeanHandler();

  public SessionBean getSession(HttpServletRequest request);

  public void init(ServletContextEvent sce) throws ServletException;

  public ProfileHandler getProfileHandler();

}