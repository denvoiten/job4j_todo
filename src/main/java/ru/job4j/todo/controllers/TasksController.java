package ru.job4j.todo.controllers;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.models.Task;
import ru.job4j.todo.models.User;
import ru.job4j.todo.services.TasksService;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class TasksController {
    private static final String TASKS = "tasks";
    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    private void setUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        model.addAttribute("user", user);
    }

    @GetMapping("/allTasks")
    public String allTasks(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute(TASKS, tasksService.findAll());
        return "allTasks";
    }

    @GetMapping("/addTask")
    public String addTask(Model model, HttpSession session) {
        setUser(model, session);
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, HttpSession session) {
        task.setUser((User) session.getAttribute("user"));
        tasksService.add(task);
        return "redirect:/allTasks";
    }

    @GetMapping("/description/{taskId}")
    public String description(Model model, @PathVariable("taskId") int id, HttpSession session) {
        setUser(model, session);
        model.addAttribute("task", tasksService.findById(id));
        return "description";
    }

    @GetMapping("/editTask/{taskId}")
    public String editTask(Model model, @PathVariable("taskId") int id, HttpSession session) {
        setUser(model, session);
        model.addAttribute("task", tasksService.findById(id));
        return "editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute Task task) {
        tasksService.update(task);
        return "redirect:/description/" + task.getId();
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") int id, HttpSession session) {
        tasksService.delete(id);
        return "redirect:/allTasks";
    }

    @GetMapping("/setDone/{taskId}")
    public String setDone(Model model, @PathVariable("taskId") int id, HttpSession session) {
        setUser(model, session);
        tasksService.setDoneById(id);
        return "redirect:/description/{taskId}";
    }

    @GetMapping("/setActive/{taskId}")
    public String setActive(Model model, @PathVariable("taskId") int id, HttpSession session) {
        tasksService.setActiveById(id);
        return "redirect:/description/{taskId}";
    }

    @GetMapping("/showActive")
    public String showActive(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute(TASKS, tasksService.findActive());
        return "showActive";
    }

    @GetMapping("/showDone")
    public String showDone(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute(TASKS, tasksService.findDone());
        return "showDone";
    }
}
