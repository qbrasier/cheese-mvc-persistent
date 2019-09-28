package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute(new Menu());

        return "menu/add";
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors){
        if(errors.hasErrors()){
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value = "view/{ID}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int ID){
        model.addAttribute("menu", menuDao.findOne(ID));
        model.addAttribute("title", menuDao.findOne(ID).getName());
        return "menu/view";
    }
    @RequestMapping(value = "add-item/{ID}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int ID){
        Menu newMenu = menuDao.findOne(ID);
        System.out.println("this ran");
        model.addAttribute("form", new AddMenuItemForm(newMenu, cheeseDao.findAll()));
        model.addAttribute("title", "Add item to menu: " + newMenu.getName() );

        return "menu/add-item";
    }
    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form,
                          Errors errors, @RequestParam int cheeseId, @RequestParam int menuId){
        if(errors.hasErrors()){
            return "menu/add-item";
        }
        Cheese newCheese = cheeseDao.findOne(cheeseId);
        Menu newMenu = menuDao.findOne(menuId);

        newMenu.addItem(newCheese);
        menuDao.save(newMenu);

        return "redirect:view/"+menuId;
    }

}
