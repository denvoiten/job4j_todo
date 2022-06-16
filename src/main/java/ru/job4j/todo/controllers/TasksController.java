package ru.job4j.todo.controllers;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.models.Task;
import ru.job4j.todo.services.TasksService;

@ThreadSafe
@Controller
public class TasksController {
    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/allTasks")
    public String allTasks(Model model) {
        model.addAttribute("tasks", tasksService.findAll());
        return "allTasks";
    }

    @GetMapping("/addTask")
    public String addTask(Model model) {
        return "addTask";
    }

    @GetMapping("/description/{taskId}")
    public String description(Model model, @PathVariable("taskId") int id) {
        model.addAttribute("task", tasksService.findById(id));
        return "description";
    }

    @GetMapping("/setDone/{taskId}")
    public String setDone(Model model, @PathVariable("taskId") int id) {
        tasksService.setDoneById(id);
        return "redirect:/description/{taskId}";
    }

    @GetMapping("/setActive/{taskId}")
    public String setActive(Model model, @PathVariable("taskId") int id) {
        tasksService.setActiveById(id);
        return "redirect:/description/{taskId}";
    }

    @GetMapping("/showActive")
    public String showActive(Model model) {
        model.addAttribute("tasks", tasksService.findActive());
        return "showActive";
    }

    @GetMapping("/showDone")
    public String showDone(Model model) {
        model.addAttribute("tasks", tasksService.findDone());
        return "showDone";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task) {
        tasksService.add(task);
        return "redirect:/allTasks";
    }

    @GetMapping("/editTask/{taskId}")
    public String editTask(Model model, @PathVariable("taskId") int id) {
        model.addAttribute("task", tasksService.findById(id));
        return "editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute Task task) {
        tasksService.update(task);
        return "redirect:/description/" + task.getId();
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") int id) {
        tasksService.delete(id);
        return "redirect:/allTasks";
    }
}
