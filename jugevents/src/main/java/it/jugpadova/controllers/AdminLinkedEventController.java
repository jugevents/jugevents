/*
 *  Copyright 2009 JUG Events Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package it.jugpadova.controllers;

import it.jugpadova.dao.LinkedEventDao;
import it.jugpadova.po.LinkedEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * A controller for administering the linked events.
 *
 * @author Lucio Benfante
 */
@Controller
@RequestMapping("/admin/linkedevent/*.html")
@SessionAttributes(AdminLinkedEventController.LINKED_EVENT_ATTR_NAME)
public class AdminLinkedEventController {
    public static final String LINKED_EVENT_ATTR_NAME = "linkedEvent";
    public static final String EDIT_VIEW = "admin/linkedevent/edit";
    public static final String LIST_VIEW = "admin/linkedevent/list";

    @Resource
    private LinkedEventDao linkedEventDao;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

   @RequestMapping
    public String edit(@RequestParam("id") Long id, Model model) {
        LinkedEvent linkedEvent = linkedEventDao.get(id);
        if (linkedEvent == null) {
            throw new RuntimeException("Linked event not found");
        }
        model.addAttribute(LINKED_EVENT_ATTR_NAME, linkedEvent);
        return EDIT_VIEW;
    }

    @RequestMapping
    @Validation(view=EDIT_VIEW)
    public String save(@ModelAttribute(LINKED_EVENT_ATTR_NAME) LinkedEvent linkedEvent,
            BindingResult result, SessionStatus status) {
        if (linkedEvent.getId() != null && (linkedEvent.getBackground() == null || linkedEvent.getBackground().length == 0)) {
            LinkedEvent originalEvent = linkedEventDao.get(linkedEvent.getId());
            linkedEvent.setBackground(originalEvent.getBackground());
        }
        linkedEventDao.store(linkedEvent);
        status.setComplete();
        return "redirect:list.html";
    }

    @RequestMapping
    public String list(Model model) {
        List<LinkedEvent> linkedEvents = linkedEventDao.findAll();
        model.addAttribute("events", linkedEvents);
        return LIST_VIEW;
    }

    @RequestMapping
    public String create(Model model) {
        LinkedEvent linkedEvent = new LinkedEvent();
        model.addAttribute(LINKED_EVENT_ATTR_NAME, linkedEvent);
        return EDIT_VIEW;
    }

    @RequestMapping
    public String delete(@RequestParam("id") Long id, Model model) {
        LinkedEvent linkedEvent = linkedEventDao.get(id);
        if (linkedEvent == null) {
            throw new RuntimeException("Linked event not found");
        }
        linkedEventDao.delete(linkedEvent);
        return "redirect:list.html";
    }

}
