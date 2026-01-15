package se.jensen.anton.springer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.dto.FeedPostDTO;
import se.jensen.anton.springer.mapper.FeedMapper;
import se.jensen.anton.springer.repo.PostRepository;

@Service
public class FeedService {
    private final PostRepository postRepository;
    private final FeedMapper feedMapper;

    public FeedService(PostRepository postRepository,  FeedMapper feedMapper) {
        this.postRepository = postRepository;
        this.feedMapper = feedMapper;
    }

    public Page<FeedPostDTO> getGlobalFeed(int page, int size) {
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));

        return postRepository.findAll(pageRequest)
                .map(feedMapper::toDto);
    }
}
