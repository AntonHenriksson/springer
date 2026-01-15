package se.jensen.anton.springer.controller;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.anton.springer.dto.FeedPostDTO;
import se.jensen.anton.springer.service.FeedService;

@RestController
@RequestMapping("/api/feed")
public class FeedController {
    private final FeedService feedService;


    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public Page<FeedPostDTO> getGlobalFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size
    ) {
        return feedService.getGlobalFeed(page, size);
    }
}
