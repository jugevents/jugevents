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
package org.parancoe.persistence.dao;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;


/**
 * An aspect for instrumenting a dao provider.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: 294faa0af621 $
 */
@Aspect()
public class DaoProviderInstrumentation {
    private static final Logger logger = Logger.getLogger(DaoProviderInstrumentation.class);
    
    @Around("execution(* *(..)) && target(org.parancoe.persistence.dao.DaoProvider)")
    public Object executeFinder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final DaoProvider target = (DaoProvider)pjp.getTarget();
        final Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        final Object[] args = pjp.getArgs();
        logger.debug("target: "+target);
        logger.debug("method: "+method);
        logger.debug("args: "+args);
        
        if (method.getName().startsWith("get")) {
            result = target.getDaoMap().get(daoNameFromMethod(method));
            if (result == null) {
                result = pjp.proceed(args);
            }
        } else {
            // Call an instance method
            result = pjp.proceed(args);
        }
        return result;
    }

    private String daoNameFromMethod(Method getterMethod) {
        return StringUtils.uncapitalize(getterMethod.getName().substring(3));
    }
}
