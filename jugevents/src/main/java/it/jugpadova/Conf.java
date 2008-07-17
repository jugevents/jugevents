// Copyright 2006-2007 The Parancoe Team
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
package it.jugpadova;

import org.parancoe.util.BaseConf;

/**
 * here you can put configuration specific getters
 */
public class Conf extends BaseConf {

    public String getMyParam() {
        return getConfiguration().getString("myparam");
    }

    public String getAnalyticsKey() {
        return getConfiguration().getString("analyticsKey");
    }

    public String getJugeventsBaseUrl() {
        return getConfiguration().getString("jugeventsBaseUrl");
    }

    public String getConfirmationSenderEmailAddress() {
        return getConfiguration().getString("confirmationSenderEmailAddress");
    }

    public int getUpcomingEventDays() {
        return getConfiguration().getInt("upcomingEventDays", 7);
    }

    public int getNewEventDays() {
        return getConfiguration().getInt("newEventDays", 7);
    }

    public String getDefaultKmlUrl() {
        return getConfiguration().getString("defaultKmlUrl");
    }

    public double getThresholdAccess() {
        return getConfiguration().getDouble("thresholdAccess", 1);
    }

    public String getAdminMailJE() {
        return getConfiguration().getString("adminMailJE");
    }

    public String getKmlUpdateFromAddress() {
        return getConfiguration().getString("kmlUpdateFromAddress");
    }

    public String getKmlUpdateToAddress() {
        return getConfiguration().getString("kmlUpdateToAddress");
    }

    public String getKmlUpdateReplyAddress() {
        return getConfiguration().getString("kmlUpdateReplyAddress");
    }

    public String getKmlUpdateSubjectPrefix() {
        return getConfiguration().getString("kmlUpdateSubjectPrefix");
    }
    
    public String getInternalMail() {
        return getConfiguration().getString("internalMail");
    }
}
