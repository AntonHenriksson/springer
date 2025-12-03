package se.jensen.anton.springer.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {
    List<String> messages = new ArrayList<>();

    @GetMapping
    public List<String> get() {
        return messages;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable int id) {
        if (id < 0 || id >= messages.size() || messages.get(id) == null) {
            return "No message at that ID";
        }
        return messages.get(id);
    }

    @PutMapping("/{id}")
    public String put(@PathVariable int id, String message) {
        if (id < 0 || id >= messages.size() || messages.get(id) == null) {
            return "No message at that ID";
        }
        return messages.set(id, message);
    }

    @PostMapping
    public String post(@RequestBody String message) {
        messages.add(message);
        return message + " recieved";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        if (id < 0 || id >= messages.size() || messages.get(id) == null) {
            return "No message at that ID";
        }
        String removed = messages.remove(id);
        return removed + " !!!removed message";
    }

}