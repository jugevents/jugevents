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
package org.parancoe.xml;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * The parser for the DAO bean definition.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: 64975375e01c $
 */
public class DaoBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    
    /** Creates a new instance of DaoBeanDefinitionParser */
    public DaoBeanDefinitionParser() {
    }
    
    protected Class getBeanClass(Element element) {
        return ProxyFactoryBean.class;
    }
    
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String daoInterface = element.getAttribute("interface");
        bean.addPropertyValue("proxyInterfaces", daoInterface);
        String genericDao = element.getAttribute("genericDao");
        BeanDefinitionBuilder genericDaoBDB = bean.childBeanDefinition(genericDao);        
        bean.addPropertyValue("target", genericDaoBDB.getBeanDefinition());
    }
}
